
package br.com.manchestercity.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.manchestercity.automacao.BlobControl;
import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.MIMETypes;
import br.com.manchestercity.automacao.User;
import br.com.manchestercity.automacao.Utils;
import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.parsing.CosUploadFile;

public class Ajax extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4431181989356937984L;
	private static final String CONTENT_TYPE = "text/html";

	// Initialize global variables
	public void init() throws ServletException {
	}

	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();

		response.setHeader("Content-Type", "text/html;charset=ISO-8859-1");

		User lUser = User.getSession(request, response);
		Utils lUtil = new Utils(lUser);
		ResultSet rs = null;

		lUtil.noCache(response, request);

		Database lcdb = new Database(lUser);
		Connection conn = lcdb.openConnection();

		String acao = Database.verifyNull(request.getParameter("acao"));

		if (acao.equalsIgnoreCase("suggestionSCMTS")) {
			
		}


		Database.closeObject(conn);
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
