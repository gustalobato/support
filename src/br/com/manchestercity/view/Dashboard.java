
package br.com.manchestercity.view;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.automacao.DataList;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.InputMaxLengthException;
import br.com.manchestercity.automacao.PageContent;
import br.com.manchestercity.automacao.SystemIcons;
import br.com.manchestercity.automacao.Utils;
import br.com.manchestercity.negocio.NEGDashboard;

public class Dashboard extends PageContent {

	private static final long serialVersionUID = 1L;

	int diasInterna = 0;
	int diasExterna = 0;
	int diasReserva = 0;
	int diasSolicitacao = 0;
	final float percDias = 0.2f;
	String CD_AREAS_USUPA = "";
	String CD_AREAS_BAIXA = "";
	String CD_AREAS_AJUSTE = "";
	NEGDashboard neg;

	public void initialize(HttpServletResponse response) {
		super.mShowMenu = false;
		setPageWidth(85);

	

		neg = new NEGDashboard(mUser);
	}

	public void html(PrintWriter out) throws InputMaxLengthException {
		StringBuffer lHtml = new StringBuffer();
		StringBuffer page = new StringBuffer();


		if (page.length() > 0)
			out.println(page.toString());
		else {
			out.println("<div style='text-align: center; margin: 20% auto;'>");
			out.println(mUser.getTermo("DASHBOARD_EMPTY"));
			out.println("</div>");
		}
	}

}
