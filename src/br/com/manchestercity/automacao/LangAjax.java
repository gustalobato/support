
package br.com.manchestercity.automacao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.process.LoadTranslation;

public class LangAjax extends HttpServlet {

	private static final long serialVersionUID = 5617751296146852363L;

	private static final String CONTENT_TYPE = "text/html";

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);

		Utils.noCacheStatic(response, request);

		User lUser = User.getSession(request, response);

		response.setHeader("Content-Type", "text/html;charset=" + lUser.getUserCharset());

		Database lcdb = new Database(lUser);
		Connection lConn = lcdb.openConnection();

		PrintWriter out = response.getWriter();

		String act = Database.verifyNull(request.getParameter("act")).trim();
		String prp = Database.verifyNull(request.getParameter("prp")).trim();
		String val = Database.verifyNull(request.getParameter("val")).trim();

		String language = Database.verifyNull(request.getParameter("language"), User.DEFAULT_LANGUAGE).trim();
		String languageFile = language.toLowerCase() + ".properties";

		if (act.equals("update")) {
			out.println("<script>");
			if (LangManipulation.updLanguageTranslation(languageFile, prp, val)) {
				out.println("  parent.$('#_value_" + prp + "').data('alt', '" + val + "');");
			}
			out.println("  parent.highlightProp('" + prp + "');");
			out.println("  parent.closeChargerPage();");
			out.println("</script>");
		}
		else if (act.equals("validate")) {
			String termos = LangManipulation.translationComparison(languageFile, val);

			out.println("<script>");
			if (LangManipulation.termExists(languageFile, prp)) {
				out.println("  top.bootbox.alert('" + lUser.getTermo("INFO_TERMOEXISTENTE") + "');");
				out.println("  parent.closeChargerPage();");
			}
			else if (termos.length() > 0) {
				out.println("  top.bootbox.confirm('" + lUser.getTermo("INFO_TERMOSEXISTENTES") + "<br/><br/>" + termos + "', function(result) {");
				out.println("    if (result) parent.insertProp('" + prp + "');");
				out.println("  });");
				out.println("  parent.closeChargerPage();");
			}
			else {
				out.println("    parent.insertProp('" + prp + "');");
			}
			out.println("</script>");
		}
		else if (act.equals("insert")) {
			out.println("<script>");

			ArrayList<String> langFiles = LangManipulation.getLanguageFiles();
			for (String langFile : langFiles) {
				if (!LangManipulation.termExists(langFile, prp)) {
					LangManipulation.insLanguageTranslation(langFile, prp, langFile.equals(languageFile) ? val : "");
				}
			}

			out.println("  parent.facao('reload');");
			out.println("</script>");
		}
		else if (act.equals("delete")) {
			out.println("<script>");
			if (LangManipulation.delLanguageTranslation(languageFile, prp)) {
				
				ArrayList<String> langFiles = LangManipulation.getLanguageFiles();
				for (String langFile : langFiles) {
					if (!langFile.equals(languageFile)) {
						LangManipulation.delLanguageTranslation(langFile, prp);
					}
				}
				
				out.println("  parent.facao('reload');");
			}
			else {
				out.println("  parent.$('#_value_" + prp + "').closest('td').removeClass('success');");
				out.println("  parent.$('#_value_" + prp + "').closest('td').removeClass('warning');");
				out.println("  parent.$('#_value_" + prp + "').closest('td').addClass('danger');");
				out.println("  parent.setTimeout(\"$('#_value_" + prp + "').closest('td').removeClass('danger');\", 7500);");
			}
			out.println("  parent.closeChargerPage();");
			out.println("</script>");
		}
		else if (act.equals("export")) {
			File export = null;

			String termo = "";
			String valor = "";

			@SuppressWarnings( "rawtypes" )
			Enumeration propNames = LangManipulation.getLanguageTranslations(languageFile);
			while (propNames.hasMoreElements()) {
				termo = (String) propNames.nextElement();
				valor += termo + "=" + LangManipulation.getLanguageTranslation(languageFile, termo) + "\r\n";
			}

			try {
				export = File.createTempFile("tmp", "$tmp$");
				FileWriter fileWriter = new FileWriter(export);
				fileWriter.write(valor);
				fileWriter.close();

				FileInputStream mInputStream = new FileInputStream(export);
				try {
					int mBufferSize = 2048;

					// CONTENTTYPE DE SAIDA
					response.setContentType("application/csv");
					// CABEÇALHO DE SAIDA
					response.setHeader("Content-Disposition", "attachment; filename=" + languageFile + ";");

					byte temp[] = new byte[mBufferSize];
					BufferedInputStream bis = new BufferedInputStream(mInputStream);
					while (bis.read(temp) != -1) {
						response.getOutputStream().write(temp);
					}
					bis.close();
					response.getOutputStream().flush();
				}
				catch (Exception e) {
				}

			}
			catch (Exception e) {
				Utils.printSystemError("ini.export", e.getMessage());
			}

			// REMOVE ARQUIVO TEMPORÁRIO
			try {
				export.delete();
			}
			catch (Exception e) {
				Utils.printSystemError("ini.export.delete", e.getMessage());
			}
		}
		else if (act.equals("reload")) {
			LoadTranslation loadTranslation = new LoadTranslation(request.getServletContext());
			loadTranslation.loadTranslation();

			out.println("<script>");
			out.println("  parent.closeChargerPage();");
			out.println("</script>");
		}

		out.flush();

		Database.closeObject(lConn);
	}

}
