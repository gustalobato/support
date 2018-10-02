
package br.com.manchestercity.automacao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.automacao.FrameworkDefaults.ColumnWidth;
import br.com.manchestercity.automacao.FrameworkDefaults.FormInput;
import br.com.manchestercity.automacao.FrameworkDefaults.InputType;

public class ClassGenerator extends HttpServlet {

	private static final long serialVersionUID = 1551586615384710013L;

	private final String ACAO_CONNECT = "ACAO_CONNECT";
	private final String ACAO_CREATE_FILES = "ACAO_CREATE_FILES";

	// INITIALIZE GLOBAL VARIABLES
	public void init() throws ServletException {
	}

	// PROCESS THE HTTP GET REQUEST
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		StringBuffer html = new StringBuffer();

		String acttion = Database.verifyNull(request.getParameter("action"), ACAO_CONNECT).trim();
		if (acttion.equals(ACAO_CONNECT)) {
			html.append("<!DOCTYPE html>\n");
			html.append("<!--[if IE 8]> <html lang='en' class='ie8 no-js'> <![endif]-->\n");
			html.append("<!--[if IE 9]> <html lang='en' class='ie9 no-js'> <![endif]-->\n");
			html.append("<!--[if !IE]><!--> <html lang='en' class='no-js'> <!--<![endif]-->\n");
			html.append("<title> Class Generator | The Citizens Brasil </title>");
			html.append(FrameworkDefaults.printHeaderEssentials(request, response, ""));
			html.append("<body bgcolor='#FFFFFF' leftmargin='0' bottonmargin='0' rightmargin='0' topmargin='0' class='page-full-width'>\n");
			html.append("  <iframe id='ifrm' name='ifrm' src='about:blank' style='display: none;'></iframe>");
			html.append("  <form method='POST' enctype='text/html' action='classgenerator?action=" + ACAO_CREATE_FILES + "' name='frm' id='frm' target='ifrm'>\n");
			html.append("  <div class='page-container'>\n");
			html.append("    <div class='page-content-wrapper'>\n");
			html.append("      <div class='page-content' id='_div_isg_page_content'>\n");
			html.append("        <div class='row'>\n");
			html.append("          <div style='width: 60%; margin-left: auto; margin-right: auto;'>\n");
			html.append("            <div class='form col-md-12'>\n");
			html.append("              <div class='horizontal-form'>\n");

			FrameworkDefaults defaults = new FrameworkDefaults();
			FormInput formInput = defaults.new FormInput();

			try {
				html.append("                <div class='row'>\n");
				formInput.setInput("dir", "dir", "Diretório de Destino", "", InputType.TEXT, ColumnWidth.TWELVE, true, false);
				formInput.setInputMaxLength(999999);
				html.append(formInput.printInput());
				html.append("                </div>\n");

				html.append("                <div class='row'>\n");
				formInput.setInput("class", "class", "Classes", "", InputType.CHECKBOX, ColumnWidth.TWELVE, true, false);
				formInput.addInputOption("Classe de Manipulação SQL", "SQL", true);
				formInput.addInputOption("Classe de Negócio", "NEG", true);
				formInput.addInputOption("Classe de Página HTML", "PAGE", true);
				formInput.setOptionsInline(false);
				html.append(formInput.printInput());
				html.append("                </div>\n");

				html.append("                <div class='row'>\n");
				formInput.setInput("table", "table", "Tabelas", "", InputType.CHECKBOX, ColumnWidth.TWELVE, true, false);

				User user = new User(request);
				Database lcdb = user.getDatabase();
				Connection conn = lcdb.openConnection();
				ResultSet rs = null;

				try {
					DatabaseMetaData dbmd = conn.getMetaData();
					rs = dbmd.getTables(null, null, "%", new String[] { "TABLE" });
					while (rs.next()) {
						if (rs.getString("TABLE_SCHEM").equalsIgnoreCase(IniManipulation.getProperty(IniManipulation.DB_USER))) {
							formInput.addInputOption(rs.getString("TABLE_NAME"), rs.getString("TABLE_NAME"), true);
						}
					}
				}
				catch (Exception e) {
					Utils.printSystemError("dbmd.printTables", e.getMessage());
					e.printStackTrace();
				}
				finally {
					Database.closeObject(rs);
					Database.closeObject(conn);
					lcdb = null;
					user = null;
				}

				html.append(formInput.printInput());
				html.append("                </div>\n");
			}
			catch (Exception e) {
				Utils.printSystemError("formInput", e.getMessage());
			}

			html.append("                <div class='row'>\n");
			html.append("                  <div class='col-md-12'>\n");
			html.append(FrameworkDefaults.printButton("_button_generate", "Gerar Arquivos", SystemIcons.ICON_CHECK_CIRCLE, "blue-steel", "float: right;", "Metronic.blockUI(); $('#frm').submit();", false));
			html.append("                  </div>\n");
			html.append("                </div>\n");

			html.append("              </div>\n");
			html.append("            </div>\n");
			html.append("          </div>\n");
			html.append("        </div>\n");
			html.append("      </div>\n");
			html.append("    </div>\n");
			html.append("  </div>\n");
			html.append("  </form>\n");
			html.append("</body>\n");
			html.append(FrameworkDefaults.printFooterEssentials(""));
			html.append("</html>\n");
		}
		else if (acttion.equals(ACAO_CREATE_FILES)) {
			String dir = Utils.replaceAll(Database.verifyNull(request.getParameter("dir"), IniManipulation.getRealPath()), "\\", "/");
			List<String> tables = Arrays.asList(request.getParameterValues("table"));
			List<String> classes = Arrays.asList(request.getParameterValues("class"));

			User user = new User(request);
			Database lcdb = user.getDatabase();
			Connection conn = lcdb.openConnection();

			try {
				String fileSQL;
				String fileNEG;
				String filePAGE;

				File file;

				FileWriter fw;
				BufferedWriter bw;

				DatabaseMetaData dbmd = conn.getMetaData();
				for (String table : tables) {
					fileSQL = "";
					fileNEG = "";
					filePAGE = "";

					if (classes.contains("SQL")) {
						fileSQL = sqlBuilder(dbmd, table);
					}

					if (classes.contains("NEG")) {
						fileNEG = negocioBuilder(dbmd, table);
					}

					if (classes.contains("PAGE")) {
						filePAGE = pageBuilder(dbmd, table);
					}

					// CRIA OS NOVOS ARQUIVOS NO DIRETORIO ESCOLHIDO
					if (!fileSQL.equals("")) {
						file = new File(dir + "/src_gen/br/com/isg/sql/SQL" + table + ".java");

						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}

						if (!file.exists()) {
							file.createNewFile();
						}

						fw = new FileWriter(file.getAbsoluteFile());
						bw = new BufferedWriter(fw);

						bw.write(fileSQL);
						bw.close();
					}

					if (!fileNEG.equals("")) {
						file = new File(dir + "/src_gen/br/com/isg/negocio/NEG" + table + ".java");

						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}

						if (!file.exists()) {
							file.createNewFile();
						}

						fw = new FileWriter(file.getAbsoluteFile());
						bw = new BufferedWriter(fw);

						bw.write(fileNEG);
						bw.close();
					}

					if (!filePAGE.equals("")) {
						file = new File(dir + "/src_gen/br/com/isg/view/Page" + table + ".java");

						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}

						if (!file.exists()) {
							file.createNewFile();
						}

						fw = new FileWriter(file.getAbsoluteFile());
						bw = new BufferedWriter(fw);

						bw.write(filePAGE);
						bw.close();
					}
				}
				html.append("<script>parent.Metronic.unblockUI(); parent.bootbox.alert(\"<span class='text-success'>As classes foram geradas com sucesso!<br/>Por favor, verifique os arquivos no diretório: '" + dir + "'</span>\");</script>");
			}
			catch (Exception e) {
				e.printStackTrace();
				html.delete(0, html.length());
				html.append("<script>parent.Metronic.unblockUI(); parent.bootbox.alert(\"<span class='text-danger'>Ocorreu um erro na geração das classes!!!<br/>" + e.getMessage() + "</span>\");</script>");
			}
			finally {
				Database.closeObject(conn);
				lcdb = null;
				user = null;
			}
		}
		out.print(html);
		out.flush();
	}

	// CLEAN UP RESOURCES
	public void destroy() {
	}

	private String sqlBuilder(DatabaseMetaData dbmd, String tableName) {
		ResultSet rs = null;

		StringBuffer auxParams = new StringBuffer();
		StringBuffer auxCLOB = new StringBuffer();

		ArrayList<String> auxPK = new ArrayList<String>();
		ArrayList<String> auxFields = new ArrayList<String>();

		StringBuffer auxInsertInto = new StringBuffer();
		StringBuffer auxInsertValues = new StringBuffer();
		StringBuffer auxWhere = new StringBuffer();

		try {
			String field = "";

			rs = dbmd.getPrimaryKeys(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName);
			while (rs.next()) {
				field = Database.verifyNull(rs.getObject("COLUMN_NAME"));
				auxPK.add(field);

				if (auxWhere.length() > 0) {
					auxWhere.append(" AND ");
				}
				auxWhere.append(tableName + "." + field + " = \" + lcdb.verifyInsertNull(p" + field + ", DataType.INTEGER) + \"");
			}
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.getPrimaries", e.getMessage());
		}
		finally {
			Database.closeObject(rs);
		}

		try {
			String field = "";
			String type = "";

			int dataType = 0;

			rs = dbmd.getColumns(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName, "%");
			while (rs.next()) {
				field = Database.verifyNull(rs.getObject("COLUMN_NAME"));
				dataType = Database.verifyNullInt(rs.getInt("DATA_TYPE"));

				if (dataType == Types.INTEGER || dataType == Types.NUMERIC || dataType == Types.DECIMAL || dataType == Types.DOUBLE) {
					type = "DataType.INTEGER";
				}
				else if (dataType == Types.DATE) {
					type = "DataType.DATE_TIME";
				}
				else {
					type = "DataType.TEXT";
				}

				auxFields.add(field);

				// CAMPOS UTILIZADOS COMO PARAMETROS
				if (auxParams.length() > 0) {
					auxParams.append(", ");
				}
				auxParams.append("String p" + field);

				if (rs.getInt("DATA_TYPE") == Types.CLOB) {
					auxCLOB.append("			if (commit) {\n");
					auxCLOB.append("				commit = lcdb.updateCLOB(\"" + tableName + "\", \"" + tableName + "." + field + "\", \"" + tableName + "." + auxFields.get(0) + " = \" + lcdb.verifyInsertNull(maxPK, DataType.INTEGER), p" + field + ", conn, true);\n");
					auxCLOB.append("			}\n\n");
				}
				else {
					if (auxInsertInto.length() > 0) {
						auxInsertInto.append(",\\n\");\n");
					}
					auxInsertInto.append("			query.append(\"  " + field);

					if (auxInsertValues.length() > 0) {
						auxInsertValues.append(",\\n\");\n");
					}
					auxInsertValues.append("			query.append(\"  \" + lcdb.verifyInsertNull(p" + field + ", " + type + ") + \"");
				}
			}
			auxInsertInto.append("\");\n");
			auxInsertValues.append("\");\n");
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printFieldName", e.getMessage());
			e.printStackTrace();

			auxInsertInto.delete(0, auxInsertInto.length());
			auxInsertValues.delete(0, auxInsertValues.length());
		}
		finally {
			Database.closeObject(rs);
		}

		StringBuffer isgClass = new StringBuffer();
		isgClass.append("\n");
		isgClass.append("package br.com.isg.sql;\n\n");

		isgClass.append("import java.sql.Connection;\n");
		isgClass.append("import java.sql.ResultSet;\n\n");

		isgClass.append("import br.com.isg.automacao.AutoException;\n");
		isgClass.append("import br.com.isg.automacao.BuildSql;\n");
		isgClass.append("import br.com.isg.automacao.DataType;\n");
		isgClass.append("import br.com.isg.automacao.Database;\n");
		isgClass.append("import br.com.isg.automacao.User;\n\n");

		isgClass.append("public class SQL" + tableName + " {\n");
		isgClass.append("	Database lcdb;\n");
		isgClass.append("	User user;\n\n");

		isgClass.append("	public SQL" + tableName + "(User user) {\n");
		isgClass.append("		this.lcdb = new Database(user);\n");
		isgClass.append("		this.user = user;\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, ");
		isgClass.append(auxParams);
		isgClass.append(") {\n");
		isgClass.append("		long maxPK = lcdb.maxDB(\"" + tableName + "\", \"" + tableName + "." + auxFields.get(0) + "\", \"\", conn);\n");
		isgClass.append("		boolean commit = false;\n\n");

		isgClass.append("		try {\n");
		isgClass.append("			StringBuffer query = new StringBuffer();\n");
		isgClass.append("			query.append(\"INSERT INTO " + tableName + " (\\n\");\n");
		isgClass.append(auxInsertInto);
		isgClass.append("			query.append(\")\\n\");\n");
		isgClass.append("			query.append(\"VALUES (\\n\");\n");
		isgClass.append(Utils.replaceAll(auxInsertValues.toString(), "p" + auxFields.get(0), "String.valueOf(maxPK)"));
		isgClass.append("			query.append(\")\\n\");\n\n");

		isgClass.append("			commit = lcdb.executeQuery(query.toString(), conn);\n\n");

		isgClass.append(auxCLOB);
		isgClass.append("		}\n");
		isgClass.append("		catch (AutoException e) {\n");
		isgClass.append("			errors.append(e.getMessage());\n");
		isgClass.append("			return 0;\n");
		isgClass.append("		}\n");
		isgClass.append("		return commit ? maxPK : 0;\n");
		isgClass.append("	}\n\n");

		auxCLOB.delete(0, auxCLOB.length());
		auxInsertInto.delete(0, auxInsertInto.length());
		auxInsertValues.delete(0, auxInsertValues.length());

		try {
			String field = "";
			String type = "";

			int dataType = 0;

			rs = dbmd.getColumns(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName, "%");
			while (rs.next()) {
				field = Database.verifyNull(rs.getObject("COLUMN_NAME"));
				dataType = Database.verifyNullInt(rs.getInt("DATA_TYPE"));

				if (dataType == Types.INTEGER || dataType == Types.NUMERIC || dataType == Types.DECIMAL || dataType == Types.DOUBLE) {
					type = "DataType.INTEGER";
				}
				else if (dataType == Types.DATE) {
					type = "DataType.DATE_TIME";
				}
				else {
					type = "DataType.TEXT";
				}

				if (rs.getInt("DATA_TYPE") == Types.CLOB) {
					auxCLOB.append("			if (commit && p" + field + " != null) {\n");
					auxCLOB.append("				commit = lcdb.updateCLOB(\"" + tableName + "\", \"" + tableName + "." + field + "\", \"" + tableName + "." + auxFields.get(0) + " = \" + lcdb.verifyInsertNull(maxPK, DataType.INTEGER), p" + field + ", conn, true);\n");
					auxCLOB.append("			}\n\n");
				}
				else if (!auxPK.contains(field)) {
					auxInsertInto.append("			if (p" + field + " != null) {\n");
					auxInsertInto.append("				if (aux.length() > 0) {\n");
					auxInsertInto.append("					aux.append(\",\\n\");\n");
					auxInsertInto.append("				}\n");
					auxInsertInto.append("				aux.append(\"  " + tableName + "." + field + " = \" + lcdb.verifyInsertNull(p" + field + ", " + type + "));\n");
					auxInsertInto.append("			}\n\n");
				}
			}
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printFieldName", e.getMessage());
			e.printStackTrace();

			auxInsertInto.delete(0, auxInsertInto.length());
			auxInsertValues.delete(0, auxInsertValues.length());
		}
		finally {
			Database.closeObject(rs);
		}

		isgClass.append("	public boolean update(Connection conn, StringBuffer errors, ");
		isgClass.append(auxParams);
		isgClass.append(") {\n");
		isgClass.append("		boolean commit = false;\n");

		isgClass.append("		try {\n");
		isgClass.append("			StringBuffer aux = new StringBuffer();\n");
		isgClass.append("			StringBuffer query = new StringBuffer();\n");
		isgClass.append("			query.append(\"UPDATE " + tableName + " SET \\n\");\n\n");

		isgClass.append(auxInsertInto);

		isgClass.append("			query.append(aux);\n");
		isgClass.append("			query.append(\"\\n\");\n");
		isgClass.append("			query.append(\"WHERE " + auxWhere.toString() + "\");\n\n");

		isgClass.append("			commit = lcdb.executeQuery(query.toString(), conn);\n");

		isgClass.append(auxCLOB);
		isgClass.append("		}\n");
		isgClass.append("		catch (AutoException e) {\n");
		isgClass.append("			errors.append(e.getMessage());\n");
		isgClass.append("			return false;\n");
		isgClass.append("		}\n");
		isgClass.append("		return commit;\n");
		isgClass.append("	}\n\n");

		auxInsertInto.delete(0, auxInsertInto.length());
		auxInsertValues.delete(0, auxInsertValues.length());

		try {
			String field = "";
			String type = "";

			int dataType = 0;

			rs = dbmd.getColumns(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName, "%");
			while (rs.next()) {
				field = Database.verifyNull(rs.getObject("COLUMN_NAME"));
				dataType = Database.verifyNullInt(rs.getInt("DATA_TYPE"));

				if (dataType == Types.INTEGER || dataType == Types.NUMERIC || dataType == Types.DECIMAL || dataType == Types.DOUBLE) {
					type = "DataType.INTEGER";
				}
				else if (dataType == Types.DATE) {
					type = "DataType.DATE_TIME";
				}
				else {
					type = "DataType.TEXT";
				}

				if (auxInsertInto.length() > 0) {
					auxInsertInto.append(",\\n\");\n");
				}
				auxInsertInto.append("		query.append(\"  " + tableName + "." + field);

				auxInsertValues.append("		if (!Database.verifyNull(p" + field + ").equals(\"\")) {\n");
				auxInsertValues.append("			if (aux.length() > 0) {\n");
				auxInsertValues.append("				aux.append(\"  AND\");\n");
				auxInsertValues.append("			}\n");
				auxInsertValues.append("			aux.append(\" " + tableName + "." + field + " = \" + lcdb.verifyInsertNull(p" + field + ", " + type + ") + \"\\n\");\n");
				auxInsertValues.append("		}\n\n");
			}
			auxInsertInto.append("\\n\");\n");
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printFieldName", e.getMessage());
			e.printStackTrace();

			auxInsertInto.delete(0, auxInsertInto.length());
			auxInsertValues.delete(0, auxInsertValues.length());
		}
		finally {
			Database.closeObject(rs);
		}

		isgClass.append("	public boolean delete(Connection conn, StringBuffer errors");
		try {
			rs = dbmd.getPrimaryKeys(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName);
			while (rs.next()) {
				isgClass.append(", String p" + Database.verifyNull(rs.getObject("COLUMN_NAME")));
			}
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printPrimaries", e.getMessage());
		}
		finally {
			Database.closeObject(rs);
		}
		isgClass.append(") {\n");
		isgClass.append("		boolean commit = false;\n");

		isgClass.append("		try {\n");
		isgClass.append("			commit = lcdb.executeQuery(\"DELETE FROM " + tableName + " WHERE " + auxWhere.toString() + "\", conn);\n");
		isgClass.append("		}\n");
		isgClass.append("		catch (AutoException e) {\n");
		isgClass.append("			errors.append(e.getMessage());\n");
		isgClass.append("			return false;\n");
		isgClass.append("		}\n");
		isgClass.append("		return commit;\n");
		isgClass.append("	}\n\n");

		auxInsertInto.delete(0, auxInsertInto.length());
		auxInsertValues.delete(0, auxInsertValues.length());

		try {
			String field = "";
			String type = "";

			int dataType = 0;

			rs = dbmd.getColumns(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName, "%");
			while (rs.next()) {
				field = Database.verifyNull(rs.getObject("COLUMN_NAME"));
				dataType = Database.verifyNullInt(rs.getInt("DATA_TYPE"));

				if (dataType == Types.INTEGER || dataType == Types.NUMERIC || dataType == Types.DECIMAL || dataType == Types.DOUBLE) {
					type = "DataType.INTEGER";
				}
				else if (dataType == Types.DATE) {
					type = "DataType.DATE_TIME";
				}
				else {
					type = "DataType.TEXT";
				}

				if (auxInsertInto.length() > 0) {
					auxInsertInto.append(",\\n\");\n");
				}
				auxInsertInto.append("		query.append(\"  " + tableName + "." + field);

				auxInsertValues.append("		if (!Database.verifyNull(p" + field + ").equals(\"\")) {\n");
				auxInsertValues.append("			if (aux.length() > 0) {\n");
				auxInsertValues.append("				aux.append(\"  AND\");\n");
				auxInsertValues.append("			}\n");
				auxInsertValues.append("			aux.append(\" " + tableName + "." + field + " = \" + lcdb.verifyInsertNull(p" + field + ", " + type + ") + \"\\n\");\n");
				auxInsertValues.append("		}\n\n");
			}
			auxInsertInto.append("\\n\");\n");
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printFieldName", e.getMessage());
			e.printStackTrace();

			auxInsertInto.delete(0, auxInsertInto.length());
			auxInsertValues.delete(0, auxInsertValues.length());
		}
		finally {
			Database.closeObject(rs);
		}

		isgClass.append("	public String refresh(");
		isgClass.append(auxParams);
		isgClass.append(", String pOrderBy) {\n");
		isgClass.append("		StringBuffer aux = new StringBuffer();\n");
		isgClass.append("		StringBuffer query = new StringBuffer();\n\n");
		isgClass.append("		query.append(\"SELECT \\n\");\n");
		isgClass.append(auxInsertInto);
		isgClass.append("		query.append(\"FROM " + tableName + " \\n\");\n\n");

		isgClass.append(auxInsertValues);

		isgClass.append("		if (aux.length() > 0) {\n");
		isgClass.append("			query.append(\"WHERE\");\n");
		isgClass.append("			query.append(aux);\n");
		isgClass.append("		}\n\n");

		isgClass.append("		if (Database.verifyNull(pOrderBy).trim().length() > 0) {\n");
		isgClass.append("			query.append(\"ORDER BY \" + pOrderBy.trim() + \" \");\n");
		isgClass.append("		}\n\n");

		isgClass.append("		return query.toString();\n");
		isgClass.append("	}\n");

		isgClass.append("}\n");

		return isgClass.toString();
	}

	private String negocioBuilder(DatabaseMetaData dbmd, String tableName) {
		ResultSet rs = null;

		StringBuffer auxFields = new StringBuffer();
		StringBuffer auxMethods = new StringBuffer();
		StringBuffer auxParams = new StringBuffer();

		ArrayList<String> pks = new ArrayList<String>();

		StringBuffer isgClass = new StringBuffer();
		isgClass.append("\n");
		isgClass.append("package br.com.isg.negocio;\n\n");

		isgClass.append("import java.sql.Connection;\n");
		isgClass.append("import java.sql.ResultSet;\n\n");

		isgClass.append("import br.com.isg.automacao.Database;\n");
		isgClass.append("import br.com.isg.automacao.User;\n");
		isgClass.append("import br.com.isg.automacao.ValidateReturn;\n");
		isgClass.append("import br.com.isg.sql.SQL" + tableName + ";\n\n");

		isgClass.append("public class NEG" + tableName + " {\n");
		isgClass.append("	Database lcdb;\n");
		isgClass.append("	User user;\n\n");

		try {
			String field = "";

			rs = dbmd.getColumns(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName, "%");
			while (rs.next()) {
				field = Database.verifyNull(rs.getObject("COLUMN_NAME"));

				// DECLARACAO DOS CAMPOS GLOBAIS
				isgClass.append("	String m" + field + ";\n");

				// CAMPOS NULOS PARA CONSTRUTOR
				auxFields.append("		this.m" + field + " = null;\n");

				// GETTERS & SETTERS
				auxMethods.append("	public void set" + field + "(String m" + field + ") {\n");
				auxMethods.append("		this.m" + field + " = m" + field + ";\n");
				auxMethods.append("	}\n\n");

				auxMethods.append("	public String get" + field + "() {\n");
				auxMethods.append("		return m" + field + ";\n");
				auxMethods.append("	}\n\n");

				// CAMPOS UTILIZADOS COMO PARAMETROS
				if (auxParams.length() > 0) {
					auxParams.append(", ");
				}
				auxParams.append("this.m" + field);
			}
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printFieldName", e.getMessage());
			e.printStackTrace();
		}
		finally {
			Database.closeObject(rs);
		}

		isgClass.append("\n");
		isgClass.append("	public NEG" + tableName + "(User user) {\n");
		isgClass.append("		this.lcdb = new Database(user);\n");
		isgClass.append("		this.user = user;\n\n");
		isgClass.append("		this.nullFields();\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public void nullFields() {\n");
		isgClass.append(auxFields);
		isgClass.append("	}\n\n");

		isgClass.append("	public void emptyFields() {\n");
		isgClass.append(Utils.replaceAll(auxFields.toString(), " = null;", " = \"\";"));
		isgClass.append("	}\n\n");

		isgClass.append(auxMethods);

		isgClass.append("	public long insert(Connection conn, StringBuffer errors) {\n");
		isgClass.append("		try {\n");
		isgClass.append("			return new SQL" + tableName + "(user).insert(lcdb, conn, errors, ");
		isgClass.append(auxParams);
		isgClass.append(");\n");
		isgClass.append("		}\n");
		isgClass.append("		catch (Exception e) {\n");
		isgClass.append("			errors.append(e.getMessage());\n");
		isgClass.append("		}\n");
		isgClass.append("		return -1;\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public boolean update(Connection conn, StringBuffer errors) {\n");
		isgClass.append("		try {\n");
		isgClass.append("			return new SQL" + tableName + "(user).update(conn, errors, ");
		isgClass.append(auxParams);
		isgClass.append(");\n");
		isgClass.append("		}\n");
		isgClass.append("		catch (Exception e) {\n");
		isgClass.append("			errors.append(e.getMessage());\n");
		isgClass.append("		}\n");
		isgClass.append("		return false;\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public boolean delete(Connection conn, StringBuffer errors) {\n");
		isgClass.append("		try {\n");
		isgClass.append("			return new SQL" + tableName + "(user).delete(conn, errors");
		try {
			rs = dbmd.getPrimaryKeys(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName);
			while (rs.next()) {
				pks.add(Database.verifyNull(rs.getObject("COLUMN_NAME")));
				isgClass.append(", m" + Database.verifyNull(rs.getObject("COLUMN_NAME")));
			}
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printPrimaries", e.getMessage());
		}
		finally {
			Database.closeObject(rs);
		}
		isgClass.append(");\n");
		isgClass.append("		}\n");
		isgClass.append("		catch (Exception e) {\n");
		isgClass.append("			errors.append(e.getMessage());\n");
		isgClass.append("		}\n");
		isgClass.append("		return false;\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public ResultSet refresh(Connection conn, ");
		isgClass.append(Utils.replaceAll(auxParams.toString(), "this.m", "String p"));
		isgClass.append(", String pOrderBy) {\n");
		isgClass.append("		try {\n");
		isgClass.append("			return lcdb.openResultSet(new SQL" + tableName + "(user).refresh(");
		isgClass.append(Utils.replaceAll(auxParams.toString(), "this.m", "p"));
		isgClass.append(", pOrderBy), conn);\n");
		isgClass.append("		}\n");
		isgClass.append("		catch (Exception e) {\n");
		isgClass.append("			return null;\n");
		isgClass.append("		}\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public ValidateReturn validate(Connection conn) {\n");
		isgClass.append("		ValidateReturn validate = new ValidateReturn();\n");
		isgClass.append("		StringBuffer message = new StringBuffer();\n\n");

		try {
			String field = "";

			rs = dbmd.getColumns(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName, "%");
			while (rs.next()) {
				field = Database.verifyNull(rs.getObject("COLUMN_NAME"));
				// VERIFICA SE O CAMPO ACEITA VALORES NULOS OU NÃO
				if (!pks.contains(field) && Database.verifyNull(rs.getObject("IS_NULLABLE"), "NO").trim().equals("NO")) {
					isgClass.append("        if (Database.verifyNull(m" + field + ").trim().length() <= 0) {\n");
					isgClass.append("   message.append(lcdb.validateMessage(this.user.getTermo(\"VALIDACAMPO\"), \"" + field + "\") + \"<br/>\");\n");

					// VERIFICA SE O CAMPO É UMA FK
					if (field.startsWith("CD_")) {
						isgClass.append("   validate.addMandatoryId(\"smartCombo_" + Utils.replaceAll(field, "CD_", "") + "\");\n");
					}
					else {
						isgClass.append("   validate.addMandatoryId(\"_field_" + field + "\");\n");
					}

					isgClass.append("  }\n");
				}
			}
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printNullables", e.getMessage());
			e.printStackTrace();
		}
		finally {
			Database.closeObject(rs);
		}

		isgClass.append("		validate.setMessage(message.toString());\n");
		isgClass.append("		\n");
		isgClass.append("		return validate;\n");
		isgClass.append("	}\n\n");
		isgClass.append("}\n");

		return isgClass.toString();
	}

	private String pageBuilder(DatabaseMetaData dbmd, String tableName) {
		ResultSet rs = null;

		ArrayList<String> auxPK = new ArrayList<String>();

		StringBuffer auxParams = new StringBuffer();
		StringBuffer auxControls = new StringBuffer();

		StringBuffer isgClass = new StringBuffer();
		isgClass.append("\n");
		isgClass.append("package br.com.isg.view;\n\n");

		isgClass.append("import java.io.PrintWriter;\n");
		isgClass.append("import java.sql.ResultSet;\n\n");

		isgClass.append("import javax.servlet.http.HttpServletResponse;\n\n");

		isgClass.append("import br.com.isg.automacao.Database;\n");
		isgClass.append("import br.com.isg.automacao.FrameworkDefaults.ColumnWidth;\n");
		isgClass.append("import br.com.isg.automacao.FrameworkDefaults.InputType;\n");
		isgClass.append("import br.com.isg.automacao.InputMaxLengthException;\n");
		isgClass.append("import br.com.isg.automacao.PageContent;\n");
		isgClass.append("import br.com.isg.automacao.TitleMenu;\n");
		isgClass.append("import br.com.isg.automacao.Utils;\n");
		isgClass.append("import br.com.isg.automacao.ValidateReturn;\n");
		isgClass.append("import br.com.isg.negocio.NEG" + tableName + ";\n\n");

		isgClass.append("public class Page" + tableName + " extends PageContent {\n\n");

		isgClass.append("	private static final long serialVersionUID = 0L;\n\n");

		isgClass.append("	NEG" + tableName + " neg" + tableName + ";\n\n");

		try {
			rs = dbmd.getPrimaryKeys(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName);
			while (rs.next()) {
				auxPK.add(Database.verifyNull(rs.getObject("COLUMN_NAME")));
				isgClass.append("	String l" + Database.verifyNull(rs.getObject("COLUMN_NAME")));
				isgClass.append(" = \"\";\n");
			}
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.getPrimaries", e.getMessage());
		}
		finally {
			Database.closeObject(rs);
		}

		isgClass.append("\n");
		isgClass.append("	public void initialize(HttpServletResponse response) {\n");
		isgClass.append("		super.setPageWidth(60);\n");
		isgClass.append("		super.mShowMenu = true;\n\n");

		isgClass.append("		this.neg" + tableName + " = new NEG" + tableName + "(mUser);\n\n");

		for (String pk : auxPK) {
			isgClass.append("		l" + pk + " = \"\";\n");
		}

		isgClass.append("	}\n\n");

		isgClass.append("	public void getPrimaries(HttpServletResponse response) {\n");
		for (String pk : auxPK) {
			isgClass.append("		l" + pk + " = getParameter(\"" + pk + "\");\n");
			isgClass.append("		addPrimary(\"" + pk + "\", l" + pk + ");\n\n");
		}
		isgClass.delete(isgClass.length() - 2, isgClass.length()); // REMOVE A ULTIMA QUEBRA DE LINHA
		isgClass.append("	}\n\n");

		isgClass.append("	public void getParameters(HttpServletResponse response) {\n");
		for (String pk : auxPK) {
			isgClass.append("		this.neg" + tableName + ".set" + pk + "(l" + pk + ");\n");
		}
		try {
			String field = "";
			rs = dbmd.getColumns(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName, "%");
			while (rs.next()) {
				field = Database.verifyNull(rs.getObject("COLUMN_NAME"));
				if (!auxPK.contains(field)) {
					if (field.startsWith("CD_")) {
						isgClass.append("		this.neg" + tableName + ".set" + field + "(getParameter(\"_column_" + Utils.replaceAll(field, "CD_", "") + "_" + Utils.replaceAll(field, "CD_", "") + "_" + field + "\"));\n");
						auxControls.append("		addControl(\"_column_" + Utils.replaceAll(field, "CD_", "") + "_" + Utils.replaceAll(field, "CD_", "") + "_" + field + "\", \"" + field + "\");\n");
					}
					else {
						isgClass.append("		this.neg" + tableName + ".set" + field + "(getParameter(\"_field_" + field + "\"));\n");
						auxControls.append("		addControl(\"_field_" + field + "\", \"" + field + "\");\n");
					}

					if (auxParams.length() > 0) {
						auxParams.append(",");
					}
					auxParams.append("\"\"");
				}
			}
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printFieldName", e.getMessage());
			e.printStackTrace();
		}
		finally {
			Database.closeObject(rs);
		}
		isgClass.append("	}\n\n");

		isgClass.append("	public void setTitle() {\n");
		isgClass.append("		super.mTitle = mUser.getTermo(\"" + tableName + "\");\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public void setAction() {\n");
		isgClass.append("		super.mAction = \"page" + tableName.toLowerCase() + "\";\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public void menuConfig(TitleMenu titleMenu) {\n");
		isgClass.append("		// TODO - REQALIZAR AS CONFIGURAÇÕES NECESSÁRIAS PARA O MENU;\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public boolean validate(HttpServletResponse response, PrintWriter out) {\n");
		isgClass.append("		ValidateReturn validate = neg" + tableName + ".validate(mConn);\n\n");

		isgClass.append("		if (!validate.getMessage().trim().equals(\"\")) {\n");
		isgClass.append("			super.mMandatoryIDs = validate.getMandatoryIDs();\n");
		isgClass.append("			writeMsg(validate.getMessage(), out);\n");
		isgClass.append("		}\n\n");

		isgClass.append("		return validate.getMessage().trim().equals(\"\");\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public void insert(HttpServletResponse response) {\n");
		isgClass.append("		this.neg" + tableName + ".insert(mConn, mErro);\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public void update(HttpServletResponse response) {\n");
		isgClass.append("		this.neg" + tableName + ".update(mConn, mErro);\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public boolean delete(HttpServletResponse response) {\n");
		isgClass.append("		return this.neg" + tableName + ".delete(mConn, mErro);\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public void addControls() {\n");
		isgClass.append(auxControls);
		isgClass.append("	}\n\n");

		isgClass.append("	public ResultSet show() {\n");
		isgClass.append("		return this.neg" + tableName + ".refresh(mConn");
		for (String pk : auxPK) {
			isgClass.append(", l" + pk);
		}
		if (auxParams.length() > 0) {
			isgClass.append(", ");
			isgClass.append(auxParams);
		}
		isgClass.append(", \"\");\n");
		isgClass.append("	}\n\n");

		isgClass.append("	public void html(PrintWriter out) throws InputMaxLengthException {\n");
		try {
			int type = 0;
			String aux = "";
			String field = "";

			rs = dbmd.getColumns(IniManipulation.getProperty(IniManipulation.DB_DATABASE), null, tableName, "%");
			while (rs.next()) {
				type = Database.verifyNullInt(rs.getInt("DATA_TYPE"));
				field = Database.verifyNull(rs.getObject("COLUMN_NAME"));
				if (!auxPK.contains(field)) {
					if (type == Types.INTEGER || type == Types.NUMERIC || type == Types.DECIMAL || type == Types.DOUBLE) {
						if (Database.verifyNullInt(rs.getInt("DECIMAL_DIGITS")) > 0) {
							isgClass.append("		out.println(\"<div class='row'>\");\n");
							isgClass.append("		formInput.setInput(\"_field_" + field + "\", \"_field_" + field + "\", mUser.getTermo(\"" + field + "\"), getValueByField(\"" + field + "\"), InputType.TEXT, ColumnWidth.TWELVE, " + Database.verifyNull(rs.getObject("IS_NULLABLE"), "NO").trim().equals("NO") + ", false);\n");
							isgClass.append("		formInput.setInputExtraClasses(\"text-right\");\n");
							isgClass.append("		formInput.setInputMaxLength(" + Database.verifyNull(rs.getObject("COLUMN_SIZE")) + ");\n");
							isgClass.append("		formInput.setOnBlur(\"FormataValor(this, event, " + Database.verifyNull(rs.getObject("DECIMAL_DIGITS")) + ", " + Database.verifyNull(rs.getObject("COLUMN_SIZE")) + ");\");\n");
							isgClass.append("		formInput.setOnKeyUp(\"FormataValor(this, event, " + Database.verifyNull(rs.getObject("DECIMAL_DIGITS")) + ", " + Database.verifyNull(rs.getObject("COLUMN_SIZE")) + ");\");\n");
							isgClass.append("		out.println(formInput.printInput());\n");
							isgClass.append("		out.println(\"</div>\");\n\n");
						}
						else if (field.startsWith("CD_")) {
							aux = Utils.replaceAll(field, "CD_", "");
							isgClass.append("		out.println(\"<div class='row'>\");\n");
							isgClass.append("		formInput.setInput(\"_field_" + field + "\", \"_field_" + field + "\", mUser.getTermo(\"" + field + "\"), getValueByField(\"" + field + "\"), InputType.EXISTING_FIELD, ColumnWidth.TWELVE, " + Database.verifyNull(rs.getObject("IS_NULLABLE"), "NO").trim().equals("NO") + ", false);\n");
							isgClass.append("		loc = new SmartCombo(mUser);\n");
							isgClass.append("		loc.setID(\"" + aux + "\");\n");
							isgClass.append("		loc.setTable(\"" + aux + "\");\n");
							isgClass.append("		loc.addPrimary(\"" + aux + "." + field + "\", DataType.INTEGER, getValueByField(\"" + field + "\"));\n");
							isgClass.append("		loc.addColumnDisplay(\"" + aux + "." + Utils.replaceAll(field, "CD_", "NM_") + "\", DataType.TEXT, null);\n");
							isgClass.append("		loc.setColumnOrderBy(\"" + aux + "." + Utils.replaceAll(field, "CD_", "NM_") + "\", DataType.TEXT, null, Ordination.ASC);\n");
							isgClass.append("		formInput.setInputHTML(loc.printSmartCombo());\n");
							isgClass.append("		out.println(formInput.printInput());\n");
							isgClass.append("		out.println(\"</div>\");\n\n");
						}
						else {
							isgClass.append("		out.println(\"<div class='row'>\");\n");
							isgClass.append("		formInput.setInput(\"_field_" + field + "\", \"_field_" + field + "\", mUser.getTermo(\"" + field + "\"), getValueByField(\"" + field + "\"), InputType.TEXT, ColumnWidth.TWELVE, " + Database.verifyNull(rs.getObject("IS_NULLABLE"), "NO").trim().equals("NO") + ", false);\n");
							isgClass.append("		formInput.setInputExtraClasses(\"text-right\");\n");
							isgClass.append("		formInput.setInputMaxLength(" + Database.verifyNull(rs.getObject("COLUMN_SIZE")) + ");\n");
							isgClass.append("		formInput.setOnKeyPress(\"MascaraInteiro();\");\n");
							isgClass.append("		out.println(formInput.printInput());\n");
							isgClass.append("		out.println(\"</div>\");\n\n");
						}
					}
					else if (type == Types.DATE) {
						isgClass.append("		out.println(\"<div class='row'>\");\n");
						isgClass.append("		formInput.setInput(\"_field_" + field + "\", \"_field_" + field + "\", mUser.getTermo(\"" + field + "\"), getValueByField(\"" + field + "\"), InputType.EXISTING_FIELD, ColumnWidth.TWELVE, " + Database.verifyNull(rs.getObject("IS_NULLABLE"), "NO").trim().equals("NO") + ", false);\n");
						isgClass.append("		dtp = new DatePicker(mUser);\n");
						isgClass.append("		dtp.setID(\"_field_" + field + "\");\n");
						isgClass.append("		dtp.setName(\"_field_" + field + "\");\n");
						isgClass.append("		dtp.setValue(getValueByField(\"" + field + "\"));\n");
						isgClass.append("		formInput.setInputHTML(dtp.printDatePicker(DateType.DATE_TIME));\n");
						isgClass.append("		out.println(formInput.printInput());\n");
						isgClass.append("		out.println(\"</div>\");\n\n");
					}
					else if (type == Types.CLOB) {
						isgClass.append("		out.println(\"<div class='row'>\");\n");
						isgClass.append("		formInput.setInput(\"_field_" + field + "\", \"_field_" + field + "\", mUser.getTermo(\"" + field + "\"), getValueByField(\"" + field + "\"), InputType.TEXTAREA, ColumnWidth.TWELVE, " + Database.verifyNull(rs.getObject("IS_NULLABLE"), "NO").trim().equals("NO") + ", false);\n");
						isgClass.append("		formInput.setInputRows(\"5\");\n");
						isgClass.append("		if (CDatabase.isDBMS(Database.DBMS_SQLSERVER)) {\n");
						isgClass.append("			formInput.setInputMaxLength(8000);\n");
						isgClass.append("		}\n");
						isgClass.append("		else {\n");
						isgClass.append("			formInput.setInputMaxLength(999999);\n");
						isgClass.append("		}\n");
						isgClass.append("		out.println(formInput.printInput());\n");
						isgClass.append("		out.println(\"</div>\");\n\n");
					}
					else if (type == Types.BLOB) {
						isgClass.append("		out.println(\"<div class='row'>\");\n");
						isgClass.append("		formInput.setInput(\"_field_" + field + "\", \"_field_" + field + "\", mUser.getTermo(\"" + field + "\"), getValueByField(\"" + field + "\"), InputType.FILE, ColumnWidth.TWELVE, " + Database.verifyNull(rs.getObject("IS_NULLABLE"), "NO").trim().equals("NO") + ", false);\n");
						isgClass.append("		out.println(formInput.printInput());\n");
						isgClass.append("		out.println(\"</div>\");\n\n");
					}
					else {
						isgClass.append("		out.println(\"<div class='row'>\");\n");
						isgClass.append("		formInput.setInput(\"_field_" + field + "\", \"_field_" + field + "\", mUser.getTermo(\"" + field + "\"), getValueByField(\"" + field + "\"), InputType.TEXT, ColumnWidth.TWELVE, " + Database.verifyNull(rs.getObject("IS_NULLABLE"), "NO").trim().equals("NO") + ", false);\n");
						isgClass.append("		formInput.setInputMaxLength(" + Database.verifyNull(rs.getObject("COLUMN_SIZE")) + ");\n");
						isgClass.append("		out.println(formInput.printInput());\n");
						isgClass.append("		out.println(\"</div>\");\n\n");
					}
				}
			}
		}
		catch (Exception e) {
			Utils.printSystemError("dbmd.printFieldName", e.getMessage());
			e.printStackTrace();
		}
		finally {
			Database.closeObject(rs);
		}
		isgClass.append("	}\n\n");

		isgClass.append("}\n");

		return isgClass.toString();
	}
}
