
package br.com.manchestercity.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.Column;
import br.com.manchestercity.automacao.ColumnAlign;
import br.com.manchestercity.automacao.DataList;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.DateType;
import br.com.manchestercity.automacao.Ordination;
import br.com.manchestercity.automacao.User;
import br.com.manchestercity.automacao.Utils;

public class DisplayDataList extends HttpServlet {

	private static final long serialVersionUID = 4432495343020265452L;

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");

		User mUser = User.getSession(request, response);

		response.setHeader("Content-Type", "text/html;charset=" + mUser.getUserCharset());

		Database lcdb = new Database(mUser);
		Connection mConn = lcdb.openConnection();

		Utils mCUtil = new Utils(mUser);
		mCUtil.noCache(response, request);

		String funcao = Database.verifyNull(request.getParameter("funcao"));

		boolean ajax = Database.verifyNull(request.getParameter("ajax"), "false").equals("true");

		StringBuffer sql = new StringBuffer();
		Column auxColumn = new Column();

		DataList dataList = new DataList(mUser);
		dataList.setRequest(request);
		dataList.setID("DisplayDataList_" + funcao);
		dataList.setAjaxURL("displaydatalist?funcao=" + funcao + "&ajax=true");
		dataList.setControlParameters("&funcao=" + funcao);
		dataList.setFirstOpen(Database.verifyNull(request.getParameter("firstOpen")).equals(""));

		dataList.setShowVoltar(false);
		dataList.setTitulo(mUser.funcaoDesc(funcao));
		dataList.setShowNew(true);
		dataList.getMenu().setShowFavorito(false, request);

		
		if (funcao.equals("cad_request")) {

			sql.delete(0, sql.length());
			dataList.setTable("REQUEST");
			dataList.addPrimary("REQUEST.CD_REQUEST", DataType.TEXT);
			dataList.addColumnDisplay("REQUEST.CD_REQUEST", "CODIGO", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("REQUEST.FirstName", "Nome", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("REQUEST.LastName", "Sobrenome", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("DateBirth", "Nasc", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("Natonality", "Nacion.", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("Gender", "Sexo", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("Country", "País", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("REQUEST.City", "Cidade", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("PostCode", "CEP", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("REQUEST.Email", "E-Mail", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("REQUEST.Phone", "Telefone", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("IsCityzen", "IsCityzen", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("SupporterNumber", "Support N", "7", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("lAllowContact", "Allow Cont.", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("TrdPartyContact", "3rdPr", "7%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("OpenText", "Open Text", "*", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("Status", "Status", "7", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("ORIGEM", "ORIGEM", "7", DataType.TEXT, null, ColumnAlign.LEFT);

			dataList.setForm("pagerequest");
		}else if (funcao.equals("cad_usuario")) {
			sql.delete(0, sql.length());
			sql.append("USUPA \n");
			sql.append("LEFT JOIN MTAUS ON USUPA.CD_MTAUS = MTAUS.CD_MTAUS \n");

			dataList.setTable(sql.toString());
			dataList.addPrimary("USUPA.CD_USUPA", DataType.INTEGER);
			dataList.addColumnDisplay("USUPA.NM_USUPA", mUser.getTermo("NOME"), "30%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("USUPA.NM_USUPA_EMAIL", mUser.getTermo("EMAIL"), "30%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.addColumnDisplay("USUPA.NM_USUPA_LOGIN", mUser.getTermo("LOGIN"), "10%", DataType.TEXT, null, ColumnAlign.LEFT);
			dataList.setColumnOrderBy("USUPA.NM_USUPA", null, DataType.TEXT, Ordination.ASC);
			dataList.setForm("pageusupa");
		}
		
		

		String layer = Database.verifyNull(request.getParameter("layer"));

		if (layer.equals("S")) {
			dataList.setShowVoltar(true);
			dataList.getMenu().setJSVoltar("parent.hideSidebarLayer();");
			dataList.getMenu().setShowFavorito(false, request);
			dataList.addValueToKeep("layer", layer);
		}
		else {
			dataList.setShowVoltar(false);
		}

		// DEFINE A TABELA INICIAL/DEFINE CONTEUDO DA DISPLAY
		PrintWriter out = response.getWriter();

		if (!funcao.trim().equals("")) {
			if (ajax) {
				out.println(dataList.returnAjaxDataList());
			}
			else {
				out.println(dataList.printFullContent());
			}
		}

	}
}
