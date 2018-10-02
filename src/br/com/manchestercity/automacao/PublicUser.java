
package br.com.manchestercity.automacao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PublicUser extends User {

	public PublicUser(HttpServletRequest request) {
		super(request);
		super.setLogged(true);
		super.setUserLanguage(Database.verifyNull(request.getParameter("lang")));
	}

	public static User getSession(HttpServletRequest pRequest, HttpServletResponse pResponse) {
		return getSession(pRequest, pResponse, true);
	}

	public static User getSession(HttpServletRequest pRequest, HttpServletResponse pResponse, boolean pRedirect) {
		User lUser = null;
		lUser = (User) pRequest.getSession().getAttribute("CUSER");

		if (lUser == null) {
			lUser = new PublicUser(pRequest);
			lUser.gravaSessao("CUSER");
		}

		return lUser;
	}
}
