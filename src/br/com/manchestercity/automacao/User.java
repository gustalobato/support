
package br.com.manchestercity.automacao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.SystemConfig;
import br.com.manchestercity.negocio.NEGUSUPA;
import javazoom.upload.MultipartFormDataRequest;

public class User {

	public class Permissao {
		private String codigo;
		private String descricao;
		private String crud;
		private String grupo;

		public Permissao() {
			this.codigo = "";
			this.descricao = "";
			this.crud = "";
			this.grupo = "";
		}

		public String getCodigo() {
			return codigo;
		}

		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public String getGrupo() {
			return grupo;
		}

		public void setGrupo(String grupo) {
			this.grupo = grupo;
		}

		public String getCrud() {
			return crud;
		}

		public void setCrud(String crud) {
			this.crud = crud;
		}

		public boolean verifyCrudItem(String pItem) {
			return this.crud.indexOf(pItem) > -1;
		}

	}

	public static final String DEFAULT_LANGUAGE = "DEFAULT";

	protected int userCode;
	protected String userName;
	protected String userLogin;
	protected String userMail;
	protected String userPassword;
	protected String userLanguage;
	protected String userCharset;
	protected String userDateFormat;
	protected String userNumberDec;
	protected String userNumberMil;
	protected String userCurrency;
	protected String userAuth;
	protected String userArea;
	protected String userSite;

	protected boolean authAD;
	protected boolean logged;
	protected boolean noPass;

	protected Utils util;
	protected Database lcdb;

	protected HttpServletRequest request;

	protected HashMap<String, HashMap<String, String>> termos;

	private HashMap<String, Permissao> mPermissoes;

	public User() {
		this.mPermissoes = new HashMap<String, Permissao>();
	}

	private void setPermissions(Connection pConn) {
		boolean useDefault = true;

		Permissao permissao;
		mPermissoes.clear();

		NEGUSUPA negUSUPA = new NEGUSUPA(this);
		ResultSet rs = null;

		try {
			rs = negUSUPA.refreshPermissoes(pConn, String.valueOf(this.getUserCode()));
			while (rs.next()) {
				String key = Database.verifyNull(rs.getObject("CD_FNSEG"));

				permissao = mPermissoes.get(key);
				if (permissao == null) {
					permissao = new Permissao();
				}

				permissao.setCodigo(Database.verifyNull(rs.getObject("CD_FNSEG")));
				permissao.setDescricao(Database.verifyNull(rs.getObject("NM_FNSEG")));
				permissao.setCrud(permissao.getCrud() + Database.verifyNull(rs.getObject("ID_PMSAC_CRUD")));
				permissao.setGrupo(Database.verifyNull(rs.getObject("CD_GFSEG")));
				mPermissoes.put(key, permissao);

				useDefault = false;
			}
		}
		catch (Exception ex) {
			Utils.printSystemError("User", "setPermissions", "negUSUPA.refreshPermissoes", ex.getMessage());
			useDefault = true;
		}
		finally {
			Database.closeObject(rs);
		}
		if (useDefault) {
			mPermissoes.clear();
			try {
				rs = negUSUPA.permissoesPadrao(pConn);
				while (rs.next()) {
					String key = Database.verifyNull(rs.getObject("CD_FNSEG"));

					permissao = mPermissoes.get(key);
					if (permissao == null) {
						permissao = new Permissao();
					}

					permissao.setCodigo(Database.verifyNull(rs.getObject("CD_FNSEG")));
					permissao.setDescricao(Database.verifyNull(rs.getObject("NM_FNSEG")));
					permissao.setCrud(permissao.getCrud() + Database.verifyNull(rs.getObject("ID_PMSAC_CRUD")));
					permissao.setGrupo(Database.verifyNull(rs.getObject("CD_GFSEG")));
					mPermissoes.put(key, permissao);
				}
			}
			catch (Exception ex) {
				Utils.printSystemError("User", "setPermissions", "negUSUPA.permissoesPadrao", ex.getMessage());
				useDefault = true;
			}
			finally {
				Database.closeObject(rs);
			}
		}
	}

	// private void setPermissions(Connection pConn) {
	// Permissao permissao;
	// mPermissoes.clear();
	//
	// NEGUSUPA negUSUPA = new NEGUSUPA(this);
	// ResultSet rs = null;
	//
	// try {
	// rs = negUSUPA.refreshPermissoes(pConn, String.valueOf(this.getUserCode()));
	// while (rs.next()) {
	// String key = Database.verifyNull(rs.getObject("CD_FNSEG"));
	// permissao = (Permissao) mPermissoes.getOrDefault(key, new Permissao());
	// permissao.setCodigo(Database.verifyNull(rs.getObject("CD_FNSEG")));
	// permissao.setDescricao(Database.verifyNull(rs.getObject("NM_FNSEG")));
	// permissao.setCrud(permissao.getCrud() + Database.verifyNull(rs.getObject("ID_PMSAC_CRUD")));
	// permissao.setGrupo(Database.verifyNull(rs.getObject("CD_GFSEG")));
	// mPermissoes.put(key, permissao);
	// }
	//
	// }
	// catch (Exception ex) {
	// Utils.printSystemError("User", "setPermissions", "negPMSAC.refresh", ex.getMessage());
	// }
	// finally {
	// Database.closeObject(rs);
	// }
	// }

	public int getUserCode() {
		return userCode;
	}

	public void setUserCode(int userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserLanguage() {
		return userLanguage;
	}

	public void setUserLanguage(String userLanguage) {
		this.userLanguage = LangManipulation.languageExists(userLanguage.toLowerCase() + ".properties") ? userLanguage : DEFAULT_LANGUAGE;

		// MODIFICANDO PADROES DO IDIOMA
		this.setUserCharset(Database.verifyNull(LangManipulation.getLanguageTranslation(userLanguage.toLowerCase() + ".properties", LangManipulation.FORMAT_CHARSET), LangManipulation.getLanguageTranslation(User.DEFAULT_LANGUAGE.toLowerCase() + ".properties", LangManipulation.FORMAT_CHARSET)));
		this.setUserDateFormat(Database.verifyNull(LangManipulation.getLanguageTranslation(userLanguage.toLowerCase() + ".properties", LangManipulation.FORMAT_DATE), LangManipulation.getLanguageTranslation(User.DEFAULT_LANGUAGE.toLowerCase() + ".properties", LangManipulation.FORMAT_DATE)));
		this.setUserNumberDec(Database.verifyNull(LangManipulation.getLanguageTranslation(userLanguage.toLowerCase() + ".properties", LangManipulation.FORMAT_NUM_DEC), LangManipulation.getLanguageTranslation(User.DEFAULT_LANGUAGE.toLowerCase() + ".properties", LangManipulation.FORMAT_NUM_DEC)));
		this.setUserNumberMil(Database.verifyNull(LangManipulation.getLanguageTranslation(userLanguage.toLowerCase() + ".properties", LangManipulation.FORMAT_NUM_MIL), LangManipulation.getLanguageTranslation(User.DEFAULT_LANGUAGE.toLowerCase() + ".properties", LangManipulation.FORMAT_NUM_MIL)));
		this.setUserCurrency(Database.verifyNull(LangManipulation.getLanguageTranslation(userLanguage.toLowerCase() + ".properties", LangManipulation.FORMAT_CURRENCY), LangManipulation.getLanguageTranslation(User.DEFAULT_LANGUAGE.toLowerCase() + ".properties", LangManipulation.FORMAT_CURRENCY)));
	}

	public String getUserCharset() {
		return userCharset;
	}

	public void setUserCharset(String userCharset) {
		this.userCharset = userCharset;
	}

	public String getUserDateFormat() {
		return userDateFormat;
	}

	public void setUserDateFormat(String userDateFormat) {
		this.userDateFormat = userDateFormat;
	}

	public String getUserNumberDec() {
		return userNumberDec;
	}

	public void setUserNumberDec(String userNumberDec) {
		this.userNumberDec = userNumberDec;
	}

	public String getUserNumberMil() {
		return userNumberMil;
	}

	public void setUserNumberMil(String userNumberMil) {
		this.userNumberMil = userNumberMil;
	}

	public String getUserCurrency() {
		return userCurrency;
	}

	public void setUserCurrency(String userCurrency) {
		this.userCurrency = userCurrency;
	}

	public boolean isAuthAD() {
		return authAD;
	}

	private void setAuthAD(boolean authAD) {
		this.authAD = authAD;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public boolean isNoPass() {
		return noPass;
	}

	public void setNoPass(boolean noPass) {
		this.noPass = noPass;
	}

	public Utils getUtil() {
		return util;
	}

	public void setUtil(Utils util) {
		this.util = util;
	}

	public Database getDatabase() {
		return lcdb;
	}

	public void setDatabase(Database lcdb) {
		this.lcdb = lcdb;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HashMap<String, HashMap<String, String>> getTermos() {
		return termos;
	}

	public void setTermos(HashMap<String, HashMap<String, String>> termos) {
		this.termos = termos;
	}

	public String getUserAuth() {
		return userAuth;
	}



	public String getUserArea() {
		return userArea;
	}

	public void setUserArea(String userArea) {
		this.userArea = userArea;
	}

	public String getUserSite() {
		return userSite;
	}

	public void setUserSite(String userSite) {
		this.userSite = userSite;
	}

	@SuppressWarnings( "unchecked" )
	public User(HttpServletRequest request) {
		this.setUserCode(0);
		this.setUserName("");
		this.setUserLogin("");
		this.setUserMail("");
		this.setUserPassword("");
		this.setUserLanguage(User.DEFAULT_LANGUAGE); // ATUALIZA OS FORMATOS E CHARSETS NECESSARIOS
		this.setAuthAD(false);
		this.setLogged(false);
		this.setNoPass(false);
		this.setUserArea("");
		this.setUserSite("");

		this.setUtil(new Utils(this));
		this.setDatabase(new Database(this));

		this.setRequest(request);
		this.mPermissoes = new HashMap<String, Permissao>();

		if (request != null) {
			this.termos = (HashMap<String, HashMap<String, String>>) request.getSession().getServletContext().getAttribute("TRANSLATIONS");
		}
	}

	protected String getRandomPassword(int length) {
		StringBuffer buffer = new StringBuffer();
		Random random = new Random();
		char[] chars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		for (int i = 0; i < length; i++) {
			buffer.append(chars[random.nextInt(chars.length)]);
		}
		return buffer.toString();
	}

	public String getNewPassword(Connection conn) {
		String msg = "";
		ResultSet rs = null;

		if (this.userLogin.length() > 0 && this.userMail.length() > 0) {
			try {
				NEGUSUPA negUSUPA = new NEGUSUPA(this);
				rs = negUSUPA.refresh(conn, "", "", "", "", this.getUserMail(), "", this.getUserLogin(), "", "", "");

				if (rs.next()) {
					String novaSenha = this.getRandomPassword(10);
					negUSUPA.nullFields();
					negUSUPA.setCD_USUPA(Database.verifyNull(rs.getObject("CD_USUPA")));
					negUSUPA.setNM_USUPA_SENHA(novaSenha);
					if (negUSUPA.update(conn, new StringBuffer())) {
						Mail mail = new Mail(this);
						try {
							String[] strMail = ReplaceEmail.getReplacedEmail(this, conn, null, new DataControl[0], true);

							strMail[1] = strMail[1].replaceAll("\\{" + null + "\\}", novaSenha);
							mail.postMailDefault(this.userMail, strMail[1], strMail[0]);
							msg = "OK";
						}
						catch (Exception ex) {
							msg = this.getTermo("ERRORPSWDEMAIL");
						}
					}
				}
				else {
					msg = this.getTermo("NOUSULOGINEMAIL");
				}
			}
			catch (Exception e) {
				msg = this.getTermo("ERRORPSWDEMAIL");
			}
			finally {
				Database.closeObject(rs);
			}
		}
		else {
			msg = this.getTermo("CAMPOSOBRIGAT");
		}

		return msg;
	}

	public void gravaSessao(String attributeName) {
		try {
			this.getRequest().getSession().removeAttribute(attributeName);
			this.getRequest().getSession().setAttribute(attributeName, this);
		}
		catch (Exception e) {
			Utils.printSystemError("request.getSession", e.getMessage());
		}
	}

	public void gravaSessao() {
		this.gravaSessao("USER");
	}

	public static void gravaCookie(HttpServletResponse response, String userCode) {
		try {
			Cookie lCookie = new Cookie("CHECK_USER", userCode);
			lCookie.setMaxAge(60 * 20); // 20 MINUTOS DE DURAÇÃO
			response.addCookie(lCookie);
		}
		catch (Exception e) {
			Utils.printSystemError("response.addCookie", e.getMessage());
		}
	}

	public static void limpaCookie(HttpServletResponse response) {
		try {
			Cookie lCookie = new Cookie("CHECK_USER", "");
			lCookie.setMaxAge(0); // SUBSTITUI O VALOR ANTERIOR POR UM COOKIE NULO JA EXPIRADO
			response.addCookie(lCookie);
		}
		catch (Exception e) {
			Utils.printSystemError("response.addCookie", e.getMessage());
		}
	}

	public static final void clearSession(HttpServletRequest request, String attributeName) {
		try {
			request.getSession().removeAttribute(attributeName);
		}
		catch (Exception e) {
			System.err.println("ERROR: User: clearSession: " + e.getMessage());
		}
	}

	public static final void clearSession(HttpServletRequest request) {
		clearSession(request, "USER");
	}

	public static User getSessionNotLogged(HttpServletRequest request, HttpServletResponse response) {
		return getSession(request, response, true, false, "USER");
	}

	public static User getSession(HttpServletRequest request, HttpServletResponse response) {
		return getSession(request, response, true, true, "USER");
	}

	@SuppressWarnings( "unchecked" )
	public static User getSession(HttpServletRequest request, HttpServletResponse response, boolean openPopup, boolean verifyLogged, String attributeName) {
		// RETORNO DE TESTES GERAIS DO SISTEMA SEM FAZER LOGIN COM BANCO DE DADOS
		// if (true) {
		// User mUser = new User(request);
		// mUser.setUserLanguage(User.DEFAULT_LANGUAGE);
		// return mUser;
		// }

		boolean logged = true;
		User user = null;

		if (verifyRequest(request, response)) {
			try {
			//	response.getWriter().print("<script>alert('entrei no verifyRequest');</script>");
			//	response.getWriter().print("<script>alert('ATTRIBUTE NAME: "+attributeName+"');</script>");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			user = (User) request.getSession().getAttribute(attributeName);
		}

		if (user != null) {
			user.setRequest(request);
			if (!user.isLogged() && verifyLogged) {
				logged = false;
			}
		}
		else {
			logged = false;
		}

		// VERIFICA LOGIN POR COOKIE
		if (!logged) {
			if (!IniManipulation.getProperty(IniManipulation.LOGIN_COOKIE).trim().equals("")) {
				user = getCookieLogon(request);
				logged = true;
				if (user != null) {
					user.setRequest(request);
					if (!user.isLogged() && verifyLogged) {
						logged = false;
					}
				}
				else {
					logged = false;
				}
			}
		}

		if (logged && response != null && user.getUserCode() > 0) {
			String checkUser = "";
			if (request.getCookies() != null) {
				for (int i = 0; i < request.getCookies().length; i++) {
					if (request.getCookies()[i].getName().equalsIgnoreCase("CHECK_USER")) {
						checkUser = request.getCookies()[i].getValue().trim();
						break;
					}
				}
			}

			if (!checkUser.equals("") && !checkUser.equals(String.valueOf(user.getUserCode()))) {
				logged = false;
				user = null;
			}
			else if (checkUser.equals("")) {
				gravaCookie(response, String.valueOf(user.getUserCode()));
			}

		}

		// NÃO POSSUI SESSÃO
		if (!logged && openPopup && response != null) {

			MultipartFormDataRequest upRequest = null;
			boolean isMultipart = false;
			try {
				if (MultipartFormDataRequest.isMultipartFormData(request)) {
					upRequest = new MultipartFormDataRequest(request);
					isMultipart = true;
				}
			}
			catch (Exception e) {
				upRequest = null;
				isMultipart = false;

				Utils.printSystemError("MultipartFormDataRequest", e.getMessage());
			}

			StringBuffer html = new StringBuffer();
			html.append("</form>\n");
			html.append("<title>" + SystemConfig.getSystemName() + " | The Citizens Brasil</title>\n");
			html.append("<link rel='shortcut icon' href='favicon.ico'/>\n");
			html.append("<link rel='icon' href='favicon.ico' type='image/x-icon'>\n");
			html.append("<div style='visibility:hidden; position:absolute'>\n");
			html.append("<form name='frm_login_sessao' target='_self' method='" + request.getMethod() + "' " + ( isMultipart ? "enctype='multipart/form-data'" : "" ) + " action='" + request.getRequestURI().replaceAll("gustavo6_", "") + "'>\n");

			String key = "";
			Enumeration<String> parameters;

			if (isMultipart) {
				parameters = upRequest.getParameterNames();
				while (parameters.hasMoreElements()) {
					key = parameters.nextElement().toString();
					String[] lValues = upRequest.getParameterValues(key);
					for (int i = 0; i < lValues.length; i++) {
						html.append("<textarea name='" + key + "'>" + lValues[i] + "</textarea>\n");
					}
				}
			}
			else {
				parameters = request.getParameterNames();
				while (parameters.hasMoreElements()) {
					key = parameters.nextElement().toString();
					String[] lValues = request.getParameterValues(key);
					for (int i = 0; i < lValues.length; i++) {
						html.append("<textarea name='" + key + "'>" + lValues[i] + "</textarea>\n");
					}
				}
			}

			html.append("</form></div>\n");
			html.append("<script src='funcoes.jsp'></script> \n");
			html.append("<script src='metronic/global/plugins/jquery-1.11.0.min.js' type='text/javascript'></script>\n");
			html.append("<script>\n");
			html.append("  var lWin = null;\n");
			html.append("  var isSubmit = false;\n");
			html.append("  function openLogin() {\n");
			html.append("    var lAlt = getWindowInfo.getViewportY();\n");
			html.append("    var lLar = getWindowInfo.getViewportX();\n");
			html.append("    lAlt = jQuery(top.document).height() + 'px';\n");
			html.append("    lLar = (jQuery(top.document).width() + 17) + 'px';\n");
			html.append("    var myDiv = top.document.createElement('div');\n");
			html.append("    myDiv.id = 'layer_login';\n");
			html.append("    myDiv.style.position = 'absolute';\n");
			html.append("    myDiv.style.zIndex = '9999999';\n");
			html.append("    myDiv.style.top = '0px';\n");
			html.append("    myDiv.style.left = '0px'\n");
			html.append("    top.document.body.style.overflow = 'hidden';\n");
			html.append("    myDiv.innerHTML = \"<iframe src='loginscreen?layer=S&_inner_report=" + Database.verifyNull(request.getParameter("_inner_report")) + "' id='iframe_login_sessao' name='iframe_login_sessao' frameborder='0' scrolling='no' style='width:\" + lLar + \"; height:\" + lAlt + \"; overflow-y:hidden;'></iframe>\";\n");
			html.append("    top.document.returnForm = function() { parent.document.body.style.overflow = 'auto'; submitForm(); $( top.document.getElementById('layer_login') ).remove() };\n");
			html.append("    top.document.body.appendChild(myDiv);\n");
			html.append("    top._sociall_form_reference = document;\n");
			html.append("    jQuery(window).resize(function() {\n");
			html.append("      try {\n");
			html.append("        jQuery('#iframe_login_sessao').css('width', (jQuery(window).width() + 17) + 'px');\n");
			html.append("        jQuery('#iframe_login_sessao').css('height', jQuery(window).height() + 'px');\n");
			html.append("      }\n");
			html.append("      catch(e) {}\n");
			html.append("    });\n");
			html.append("  }\n");
			html.append("  function FocusTrigger() {\n");
			html.append("    if (isSubmit) return false;\n");
			html.append("    if (lWin != null && !lWin.closed) {\n");
			html.append("      lWin.focus();\n");
			html.append("    }\n");
			html.append("    else {\n");
			html.append("      openLogin();\n");
			html.append("    }\n");
			html.append("  }\n");
			html.append("  function submitForm() {\n");
			html.append("    isSubmit = true; alert(document.frm_login_sessao.action);\n");
			html.append("    document.frm_login_sessao.submit();\n");
			html.append("  }\n");
			html.append("  setTimeout('openLogin();', 100);\n");
			html.append("</script> \n");

			try {
				if (response.getContentType() == null || response.getContentType().trim().equals("")) {
					response.setContentType("text/html");
				}

				response.getWriter().print(html.toString());
				response.getWriter().flush();
				response.getWriter().close();
				response.flushBuffer();

				html = null;

				System.runFinalization();

				Runtime.getRuntime().runFinalization();
			}
			catch (Exception ex) {
			}
		}
		return user;
	}

	public String getTermo(String code) {
		return getTermo(code, null);
	}

	@SuppressWarnings( "unchecked" )
	public String getTermo(String code, ServletContext context) {
		if (code == null) {
			return "---";
		}

		code = code.trim();

		if (code.length() == 0) {
			return "---";
		}

		try {
			if (context != null && this.getTermos() == null) {
				this.setTermos((HashMap<String, HashMap<String, String>>) context.getAttribute("TRANSLATIONS"));
			}

			if (this.getTermos() != null) {
				String termo = "";

				HashMap<String, String> translations = (HashMap<String, String>) this.getTermos().get(this.getUserLanguage().toUpperCase());

				if (translations != null) {
					termo = translations.get(code);
				}

				if (termo == null) {
					translations = (HashMap<String, String>) this.getTermos().get(User.DEFAULT_LANGUAGE.toUpperCase());
					if (translations != null) {
						termo = translations.get(code);
					}
				}

				if (!Database.verifyNull(termo).trim().equals("")) {
					return termo.trim();
				}
			}
		}
		catch (Exception e) {
			Utils.printSystemError("error termo: ", e.getMessage());

			e.printStackTrace();

		}

		return "***" + code;
	}

	private static boolean verifyRequest(HttpServletRequest request, HttpServletResponse response) {
		String logMail = IniManipulation.getProperty(IniManipulation.MAIL_LOG_DB);
		boolean verified = true;
		if (request == null) {
			verified = false;
			if (!logMail.equals("")) {
				Mail lMail = new Mail(null);
				try {
					try {
						response.getWriter().print("<script>alert('REQUEST NULL');</script>");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//	lMail.postMail(logMail, "REQUEST NULL EM <b>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + "</b>.", SystemConfig.getSystemName() + " :: LOG REQUEST", "", SystemConfig.getSystemName(), true);
				}
				catch (Exception e) {
					Utils.printSystemError("request == NULL", e.getMessage());
				}
			}
		}
		else if (request.getSession() == null) {
			verified = false;
			if (!logMail.equals("")) {
				Mail lMail = new Mail(null);
				try {
					try {
						response.getWriter().print("<script>alert('SESSION NULL');</script>");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//lMail.postMail(logMail, "SESSION NULL EM <b>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + "</b>.", SystemConfig.getSystemName() + " :: LOG REQUEST", "", SystemConfig.getSystemName(), true);
				}
				catch (Exception e) {
					Utils.printSystemError("session == NULL", e.getMessage());
				}
			}
		}

		return verified;
	}

	public static User getCookieLogon(HttpServletRequest request) {
		boolean isPortal = false;

		String tokenFAM = ""; // FUNDAÇÃO ARCELOR MITTAL (myarcelormittal.com)
		String tokenAMT = ""; // PORTAL ARCELORMITTAL TUBARAO
		String login = "";
		String senha = "";

		User user = null;
		String idioma = "";

		if (request.getCookies() != null) {
			for (int i = 0; i < request.getCookies().length; i++) {
				if (tokenFAM.equals("") && request.getCookies()[i].getName().equalsIgnoreCase("gatekeeperuser")) {
					tokenFAM = request.getCookies()[i].getValue().trim();
				}
				else if (tokenAMT.equals("") && request.getCookies()[i].getName().equalsIgnoreCase("PS_TOKEN")) {
					tokenAMT = request.getCookies()[i].getValue().trim();
				}
				else if (idioma.equals("") && request.getCookies()[i].getName().equalsIgnoreCase("LOGIN_IDIOMA")) {
					idioma = request.getCookies()[i].getValue().trim();
				}
			}
		}

		if (!tokenFAM.equals("")) {
			login = tokenFAM.split("\\#")[0];
			isPortal = true;
		}
		

		if (!login.equals("")) {
			user = new User(request);
			Connection conn = user.lcdb.openConnection();

			if (idioma.equals("")) {
				idioma = User.DEFAULT_LANGUAGE;
			}


			

			senha = Crypt.removeEncriptation(user.lcdb.valorDB("USUPA", "USUPA.NM_USUPA_SENHA", "UPPER(USUPA.NM_USUPA_LOGIN) = UPPER('" + login.trim() + "')", conn));

			user.setUserLogin(login);
			user.setUserPassword(senha);
			user.setUserLanguage(idioma);

			if (!user.validateUser(conn, true).equals("")) {
				user = null;
			}

			Database.closeObject(conn);
		}

		return user;
	}

	public String validateUser(Connection conn) {
		return validateUser(conn, false);
	}

	public String validateUser(Connection conn, boolean isCookie) {
		// RETORNO DE TESTES GERAIS DO SISTEMA SEM FAZER LOGIN COM BANCO DE DADOS
		// if (true) {
		// return true;
		// }

		// boolean valid = false;

		String mensagem = "";

		String baseDN = "";
		String domain = "";
		String ldap = "";
		String authType = "";
		String cdMtaus = "";
		boolean valid = false;

		ResultSet rs = null;

		if (isCookie) {
			cdMtaus = lcdb.valorDB("MTAUS", "CD_MTAUS", "ID_MTAUS_TIPO = 'T' AND ID_MTAUS_STATU = 'A'", conn);
		}

		if (this.getUserLogin().length() > 0) {
			rs = new NEGUSUPA(this).refresh(conn, "", ( isCookie && !cdMtaus.equals("") ? cdMtaus : this.getUserAuth() ), "", "", "", "", this.getUserLogin(), "", "", "");
			try {
				if (rs.next()) {
					this.setUserCode(Database.verifyNullInt(rs.getObject("CD_USUPA")));
					this.setUserArea(Database.verifyNull(rs.getObject("CD_AREAS"), Database.verifyNull(rs.getObject("CD_AREAS_DFAUT"))));
					this.setUserSite(Database.verifyNull(rs.getObject("CD_STEST"), Database.verifyNull(rs.getObject("CD_STEST_DFAUT"))));

					domain = Database.verifyNull(rs.getObject("NM_MTAUS_DOM"));
					baseDN = this.getUserLogin() + "@" + domain;
					ldap = Database.verifyNull(rs.getObject("NM_MTAUS_LDAP"));
					authType = Database.verifyNull(rs.getObject("ID_MTAUS_TIPO"));

					if (this.getUserCode() > 0) {
						if (Database.verifyNull(rs.getObject("ID_USUPA_STATU")).equals("I")) {
							mensagem = this.getTermo("ERROR_USERDISABLED");
						}
						else if (Database.verifyNull(rs.getObject("NM_USUPA_SENHA")).equals("")) {
							this.setNoPass(true);
							this.setUserName(Database.verifyNull(rs.getObject("NM_USUPA")));
							this.setPermissions(conn);
						}
						else {
							if ((Crypt.addEncriptation(this.getUserPassword()).equalsIgnoreCase(Database.verifyNull(rs.getObject("NM_USUPA_SENHA"))) ) ) {
								// valid = true;
								this.setLogged(true);
								this.setUserName(Database.verifyNull(rs.getObject("NM_USUPA")));
								this.setUserMail(Database.verifyNull(rs.getObject("NM_USUPA_EMAIL")));
								this.setPermissions(conn);
							}
							else
								mensagem = this.getTermo("ERROR_SENHAINVALIDA");
						}
					}
					valid = true;
				}
			}
			catch (Exception e) {
				Utils.printSystemError("usuario.next", e.getMessage());
			}
			finally {
				Database.closeObject(rs);
				if (isCookie && !valid) {
					mensagem = this.getTermo("ERROR_TOKENINVALIDO");
				}
			}
		}

		this.gravaSessao();

		Database.closeObject(rs);

		return mensagem;
	}



	public boolean direitoFuncao(String permissao, String tipo) {
		Permissao lPermissao = (Permissao) this.mPermissoes.get(permissao);
		return ( ( lPermissao != null ) && lPermissao.verifyCrudItem(tipo) );
	}

	public String funcaoDesc(String permissao) {
		Permissao lPermissao = (Permissao) this.mPermissoes.get(permissao);
		if (lPermissao != null) {
			return lPermissao.getDescricao();
		}
		return "";
	}

	public static void acessoNegado(HttpServletResponse response) {
		try {
			response.sendRedirect("acessonegado.html");
		}
		catch (Exception ex) {
			Utils.printSystemError("response.sendRedirect :: acessonegado.html", ex.getMessage());
		}
	}




}
