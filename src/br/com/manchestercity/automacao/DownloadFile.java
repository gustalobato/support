
package br.com.manchestercity.automacao;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadFile extends HttpServlet {

	private static final long serialVersionUID = -5349413517304602163L;

	// INITIALIZE GLOBAL VARIABLES
	public void init() throws ServletException {
	}

	// PROCESS THE HTTP GET REQUEST
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// SESSÃO DESABILITADA PARA USO EM FORMULÁRIOS ABERTOS AO PÚBLICO
		User mUser = User.getSession(request, null);
		Utils mUtil = new Utils(mUser);
		mUtil.noCache(response, request);

		Database lcdb = new Database(mUser);
		Connection mConn = lcdb.openConnection();

		String fileTable = Database.verifyNull(request.getParameter("fileTable"));
		String fileColumn = Database.verifyNull(request.getParameter("fileColumn"));
		String nameColumn = Database.verifyNull(request.getParameter("nameColumn"));
		String keyColumns[] = Database.verifyNullArray(request.getParameterValues("keyColumn"));
		String keyValues[] = Database.verifyNullArray(request.getParameterValues("keyValue"));

		String fileName = null;
		byte[] fileByte = null;

		try {
			String filter = "";
			for (int i = 0; i < keyColumns.length; i++) {
				if (i < keyValues.length) {
					filter += ( filter.equals("") ? "" : "  AND " ) + BuildSql.getToChar(keyColumns[i]) + " = " + lcdb.verifyInsertNull(keyValues[i], DataType.TEXT);
				}
			}

			fileName = lcdb.valorDB(fileTable, nameColumn, filter, mConn);
		//	fileByte = Database.getBlobControl(mUser).openFieldFile(fileColumn, fileTable, filter);
		}
		catch (Exception e) {
			Utils.printSystemError("getBlobControl.openFieldFile", e.getMessage());
		}

		if (fileByte != null) {
			response.reset();
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setContentType(MIMETypes.getContentTypeByFileName(fileName));
			fileName = URLEncoder.encode(fileName, "US-ASCII");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");
			ServletOutputStream outPutStream = response.getOutputStream();
			try {
				outPutStream.write(fileByte);
				outPutStream.flush();
				outPutStream.close();
			}
			catch (Exception e) {
				System.err.println("ERRO : " + e.getMessage());
			}
		}

		Database.closeObject(mConn);

		mUser = null;
		mUtil = null;

		lcdb = null;

		fileTable = null;
		fileColumn = null;
		keyColumns = null;
		keyValues = null;
	}

	// CLEAN UP RESOURCES
	public void destroy() {
	}
}
