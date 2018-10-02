
package br.com.manchestercity.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.SystemConfig;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.IniManipulation;
import br.com.manchestercity.automacao.LangManipulation;
import br.com.manchestercity.automacao.User;
import br.com.manchestercity.automacao.Utils;

public class LoginScreen extends HttpServlet {

	private static final long serialVersionUID = -1377700790478732760L;

	private String _acao = "";
	public String cookieLogin;
	public String cookieAuth;
	public String empresa;
	public String idioma;
	public Connection mConn;
	public ResultSet rs;
	public User mUser;
	public Database lcdb;
	private boolean wasLogged = false;

	// AUX FOR INNER HTML SUBMITS
	private String _report = "";

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		mUser = User.getSession(request, response, false, true, "USER");

		idioma = "";
		empresa = "";
		cookieLogin = "";
		cookieAuth = "";
		PrintWriter out = response.getWriter();

		lcdb = new Database(mUser);
		mConn = lcdb.openConnection();
		rs = null;

		for (int i = 0; i < request.getCookies().length; i++) {
			out.println("<script>alert('COMECEI A PERCORRER OS COOKIES QUE TEM: "+request.getCookies().length+"');</script>");

			if (( cookieLogin.equals("") && request.getCookies()[i].getName().equalsIgnoreCase("LOGIN_NOME_HIST") ) || request.getCookies()[i].getName().equalsIgnoreCase("LOGIN_NOME")) {
				out.println("<script>alert('ENTREI NOS COOKIES');</script>");
				cookieLogin = request.getCookies()[i].getValue().trim();
				if (request.getCookies()[i].getName().equalsIgnoreCase("LOGIN_NOME")) {
					out.println("<script>alert('achei o LOGIN_NOME');</script>");
					out.println("<script>alert('o LOGIN_NOME é"+cookieLogin+"');</script>");
					wasLogged = !Database.verifyNull(cookieLogin).trim().equals("");
					out.println("<script>alert('WASLOGGED ESTA COM "+wasLogged+"');</script>");

				}
			}
			else if (( idioma.equals("") && request.getCookies()[i].getName().equalsIgnoreCase("LOGIN_IDIOMA_HIST") ) || request.getCookies()[i].getName().equalsIgnoreCase("LOGIN_IDIOMA")) {
				idioma = request.getCookies()[i].getValue().trim();
			}
			else if (( idioma.equals("") && request.getCookies()[i].getName().equalsIgnoreCase("LOGIN_AUTH_HIST") ) || request.getCookies()[i].getName().equalsIgnoreCase("LOGIN_AUTH")) {
				cookieAuth = request.getCookies()[i].getValue().trim();
			}
		}

		if (idioma.equals("")) {
			idioma = User.DEFAULT_LANGUAGE;
		}

		_report = Database.verifyNull(request.getParameter("_inner_report"));
		_acao = Database.verifyNull(request.getParameter("_acao"));
		out.println("<script>alert('a acao é "+_acao+"');</script>");

		Utils mUtil = new Utils(mUser);
		mUtil.noCache(response, request);

		String login = Database.verifyNull(request.getParameter("_field_login")).replaceAll(" ", "");
		String senha = Database.verifyNull(request.getParameter("_field_password")).replaceAll(" ", "");
		String language = Database.verifyNull(request.getParameter("_field_language"), idioma);
		String auth = Database.verifyNull(request.getParameter("_field_auth"));

		// VERIFICANDO IDIOMAS E ADEQUANDO TELA AS TRADUCOES CORRETAS
		String lang = "";
		String options = "";

		mUser = new User(request);
		mUser.setUserLanguage(language);
		mUser.setUserLogin(login);
		mUser.setUserPassword(senha);
		//mUser.setUserAuth(auth);

		response.setHeader("Content-Type", "text/html;charset=" + mUser.getUserCharset());

		String lMsg = new String("");
		boolean noPassword = false;

		if (_acao.equals("login")) {
			if (!login.equals("")) {
				cookieLogin = login;
			}

			if (!language.equals("")) {
				idioma = language;
			}

			if (!auth.equals("")) {
				cookieAuth = auth;
			}

			// boolean userValidate = mUser.validateUser(mConn);
			lMsg = mUser.validateUser(mConn);

			// if (!userValidate) {
			if (!lMsg.equals("")) {
				noPassword = mUser.isNoPass();
				// lMsg = mUser.getTermo("ERROR_SENHAINVALIDA");
				if (!noPassword) {
					mUser = null;
				}
			}

			if (mUser == null) {
				out.println("<script>");
				out.println("  document.getElementById('divErrorMessage').style.display = 'block';");
				out.println("  document.getElementById('spanErrorMessage').innerHTML = '" + lMsg + "';");
				out.println("  document.getElementById('_field_password').value = '';");
				out.println("  lockFields(false);");
				out.println("  $('#_field_password').focus();");
				out.println("</script>");
			}
			else {
				Cookie lCookie;

				lCookie = new Cookie("LOGIN_NOME", login);
				response.addCookie(lCookie);

				lCookie = new Cookie("LOGIN_NOME_HIST", login);
				lCookie.setMaxAge(60 * 60 * 24 * 10);
				response.addCookie(lCookie);

				lCookie = new Cookie("LOGIN_AUTH", auth);
				response.addCookie(lCookie);

				lCookie = new Cookie("LOGIN_AUTH_HIST", auth);
				lCookie.setMaxAge(60 * 60 * 24 * 10);
				response.addCookie(lCookie);

				out.println("<script> alert(parent.document.frm_login_sessao.action);");
				out.println("  top.document.body.style.overflow = 'auto';");
				if (_report.equals("true")) {
					out.println("reportLogin();");
				}
				else {
					out.println("  parent.document.returnForm();");
				}
				out.println("</script>");
			}
		}
		else if (_acao.equals("send_password")) {
			String lLogin = Database.verifyNull(request.getParameter("_field_login_new")).trim();
			String lEmail = Database.verifyNull(request.getParameter("_field_email")).trim();

			if (!language.equals("")) {
				idioma = language;
			}

			if (idioma.length() == 0) {
				idioma = User.DEFAULT_LANGUAGE;
			}

			if (!lEmail.equals("") & !lLogin.equals("")) {
				mUser = new User(request);
				if (lcdb.valorDB("USUPA LEFT JOIN MTAUS ON USUPA.CD_MTAUS = MTAUS.CD_MTAUS", "MTAUS.ID_MTAUS_TIPO", "USUPA.NM_USUPA_LOGIN = '" + lLogin + "' AND USUPA.NM_USUPA_EMAIL = '" + lEmail + "'", mConn).equals("L")) {
					lMsg = mUser.getTermo("INFO_ALTSENHAAD");
				}
			}

			if (lMsg.equals("")) {
				mUser = new User(request);
				mUser.setUserLanguage(idioma);
				mUser.setUserLogin(lLogin);
				mUser.setUserMail(lEmail);

				lMsg = mUser.getNewPassword(mConn);
			}

			if (lMsg.equals("OK")) {
				out.println("<script>");
				out.println("  document.getElementById('divSuccessMessage').style.display = 'block';");
				out.println("  document.getElementById('spanSuccessMessage').innerHTML = '" + mUser.getTermo("SUCCESS_SENHAALTERADA") + "';");
				out.println("  document.getElementById('divErrorMessage').style.display = 'none';");
				out.println("  document.getElementById('spanErrorMessage').innerHTML = '';");
				out.println("  document.getElementById('divErrorMessageForgot').style.display = 'none';");
				out.println("  document.getElementById('spanErrorMessageForgot').innerHTML = '';");
				out.println("  document.getElementById('_field_email').value = '';");
				out.println("  document.getElementById('_field_login_new').value = '';");
				out.println("  lockFields(false);");
				out.println("</script>");
			}
			else {
				out.println("<script>");
				out.println("  document.getElementById('divSuccessMessage').style.display = 'none';");
				out.println("  document.getElementById('spanSuccessMessage').innerHTML = '';");
				out.println("  document.getElementById('divErrorMessage').style.display = 'none';");
				out.println("  document.getElementById('spanErrorMessage').innerHTML = '';");
				out.println("  document.getElementById('divErrorMessageForgot').style.display = 'block';");
				out.println("  document.getElementById('spanErrorMessageForgot').innerHTML = '" + lMsg + "';");
				out.println("  lockFields(false);");
				out.println("</script>");
			}
		}
		else if (_acao.equals("logout")) {
			User.clearSession(request);
			Cookie lCookie = new Cookie("LOGIN_NOME", "");
			lCookie.setMaxAge(0);
			response.addCookie(lCookie);


			lCookie = new Cookie("LOGIN_AUTH", "");
			lCookie.setMaxAge(0);
			response.addCookie(lCookie);

			User.limpaCookie(response);

			out.println("<script>");
			out.println("  var lWin = (window.opener == undefined) ? window : window.opener;");
			out.println("  var lAux = null;");
			out.println("  while (lWin.opener != undefined) {");
			out.println("    lAux = lWin;");
			out.println("    lWin = lWin.opener;");
			out.println("    lAux.close();");
			out.println("  }");
			out.println("  lWin.top.document.location.href = 'main';");
			out.println("</script>");
		}

		if (_acao.trim().equals("")) {
			

			out.println("<!DOCTYPE html>");
			out.println("<!--[if IE 8]> <html lang='en' class='ie8 no-js'> <![endif]-->");
			out.println("<!--[if IE 9]> <html lang='en' class='ie9 no-js'> <![endif]-->");
			out.println("<!--[if !IE]><!-->");
			out.println("<html lang='en' class='no-js'>");
			out.println("<!--<![endif]-->");

			// BEGIN HEAD
			out.println("<head>");
			// out.println(" <meta charset='utf-8'/>");
			out.println("  <meta http-equiv='Content-Type' content='text/html; pageEncoding=" + mUser.getUserCharset() + " charset=" + mUser.getUserCharset() + "'>");
			out.println("  <title>" + SystemConfig.getSystemName() + " | The Citizens Brasil</title>");
			out.println("  <meta http-equiv='X-UA-Compatible' content='IE=edge'>");
			out.println("  <meta content='width=device-width, initial-scale=1.0' name='viewport'/>");
			out.println("  <meta content='' name='description'/>");
			out.println("  <meta content='' name='author'/>");

			// BEGIN GLOBAL MANDATORY STYLES
			out.println("  <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all' rel='stylesheet' type='text/css'/>");
			out.println("  <link href='metronic/global/plugins/font-awesome/css/font-awesome.min.css' rel='stylesheet' type='text/css'/>");
			out.println("  <link href='metronic/global/plugins/bootstrap/css/bootstrap.min.css' rel='stylesheet' type='text/css'/>");
			out.println("  <link href='metronic/global/plugins/uniform/css/uniform.default.css' rel='stylesheet' type='text/css'/>");
			// END GLOBAL MANDATORY STYLES

			// BEGIN PAGE LEVEL STYLES
			out.println("  <link href='metronic/global/plugins/select2/select2.css' rel='stylesheet' type='text/css'/>");
			out.println("  <link href='metronic/admin/pages/css/login-soft.css' rel='stylesheet' type='text/css'/>");
			// END PAGE LEVEL SCRIPTS

			// BEGIN THEME STYLES
			out.println("  <link href='metronic/global/css/components.css' rel='stylesheet' type='text/css'/>");
			out.println("  <link href='metronic/global/css/plugins.css' rel='stylesheet' type='text/css'/>");
			out.println("  <link href='metronic/admin/layout/css/layout.css' rel='stylesheet' type='text/css'/>");
			out.println("  <link href='metronic/admin/layout/css/themes/" + ( IniManipulation.getProperty(IniManipulation.LAYOUT_THEME).equalsIgnoreCase("light") ? "isg-light.css" : "isg-default.css" ) + "' rel='stylesheet' type='text/css'/ id='style_color'>");
			out.println("  <link href='metronic/admin/layout/css/custom.css' rel='stylesheet' type='text/css'/>");
			// END THEME STYLES
			out.println("  <link rel='shortcut icon' href='favicon.ico'/>");

			out.println("  <script src='funcoes.jsp'></script>");
			out.println("  <script>");
			out.println("    function changeLanguage(lang) {");
			out.println("      $('.login-form').append('<input type=hidden name=_acao value=lang />');");
			out.println("      $('.login-form').attr('action', 'loginscreen');");
			out.println("      $('.login-form').submit()");
			out.println("    }");
			out.println("    function doAjaxLogin(pAction) {");
			out.println("      var _field_login = $('#_field_login').val();");
			out.println("      var _field_login_new = $('#_field_login_new').val();");
			out.println("      var _field_password = $('#_field_password').val();");
			out.println("      var _field_language = '';");
			out.println("      var _field_email = $('#_field_email').val();");
			out.println("      var _field_auth = '';");
			out.println("      lockFields(true);");
			out.println("      getUrl('loginscreen?_acao=' + pAction + '&_field_login=' + _field_login + '&_field_password=' + _field_password + '&_field_auth=' + _field_auth + '&_field_language=' + _field_language + '&_field_login_new=' + _field_login_new + '&_field_email=' + _field_email + '&_inner_report=" + _report + "', true, execOnSuccess(printHTML));");
			out.println("    }");
			out.println("    function lockFields(pLock) {");
			out.println("      if (pLock) {");
			out.println("        document.getElementById('_field_login').wasLocked = document.getElementById('_field_login').disabled;");
			out.println("        document.getElementById('_field_login_new').wasLocked = document.getElementById('_field_login_new').disabled;");
			out.println("        document.getElementById('_field_password').wasLocked = document.getElementById('_field_password').disabled;");
			//out.println("        document.getElementById('_field_auth').wasLocked = document.getElementById('_field_auth').disabled;");
			//out.println("        document.getElementById('_field_language').wasLocked = document.getElementById('_field_language').disabled;");
			out.println("        document.getElementById('_field_email').wasLocked = document.getElementById('_field_email').disabled;");
			out.println("        document.getElementById('_field_login').disabled = true;");
			out.println("        document.getElementById('_field_login_new').disabled = true;");
			out.println("        document.getElementById('_field_password').disabled = true;");
		//	out.println("        document.getElementById('_field_auth').disabled = true;");
		//	out.println("        document.getElementById('_field_language').disabled = true;");
			out.println("        document.getElementById('_field_email').disabled = true;");
			out.println("      }");
			out.println("      else {");
			out.println("        document.getElementById('_field_login').disabled = document.getElementById('_field_login').wasLocked;");
			out.println("        document.getElementById('_field_login_new').disabled = document.getElementById('_field_login_new').wasLocked;");
			out.println("        document.getElementById('_field_password').disabled = document.getElementById('_field_password').wasLocked;");
		//	out.println("        document.getElementById('_field_auth').disabled = document.getElementById('_field_auth').wasLocked;");
			//out.println("        document.getElementById('_field_language').disabled = document.getElementById('_field_language').wasLocked;");
			out.println("        document.getElementById('_field_email').disabled = document.getElementById('_field_email').wasLocked;");
			out.println("      }");
			out.println("    }");
			out.println("    function printHTML(response) {");
			out.println("      var lStr = response.responseText;");
			out.println("      var lHTML = '';");
			out.println("      var lEval = '';");
			out.println("      if (lStr.indexOf('<scr'+'ipt') >= 0) {");
			out.println("        var lScript = lStr.split('<scr'+'ipt');");
			out.println("        for (i=0; i < lScript.length; i++) {");
			out.println("          if (lScript[i].indexOf('</scr'+'ipt>') != '-1') {");
			out.println("            lScriptAux = lScript[i].split('</scr'+'ipt>');");
			out.println("            lEval += lScriptAux[0].substring(lScriptAux[0].indexOf('>')+1);");
			out.println("            lHTML += lScriptAux[1];");
			out.println("          } else {");
			out.println("            lHTML += lScript[i];");
			out.println("          }");
			out.println("        }");
			out.println("      }");
			out.println("      else{");
			out.println("        lHTML = lStr;");
			out.println("      }");
			out.println("      if (lEval != '') {");
			out.println("        jQuery.globalEval(lEval);");
			out.println("      }");
			out.println("    }");
			out.println("  </script>");
			out.println("</head>");
			// END HEAD

			// BEGIN BODY
			// DOC: Apply 'page-header-fixed-mobile' and 'page-footer-fixed-mobile' class to body element to force fixed header or footer in mobile devices
			// DOC: Apply 'page-sidebar-closed' class to the body and 'page-sidebar-menu-closed' class to the sidebar menu element to hide the sidebar by default
			// DOC: Apply 'page-sidebar-hide' class to the body to make the sidebar completely hidden on toggle
			// DOC: Apply 'page-sidebar-closed-hide-logo' class to the body element to make the logo hidden on sidebar toggle
			// DOC: Apply 'page-sidebar-hide' class to body element to completely hide the sidebar on sidebar toggle
			// DOC: Apply 'page-sidebar-fixed' class to have fixed sidebar
			// DOC: Apply 'page-footer-fixed' class to the body element to have fixed footer
			// DOC: Apply 'page-sidebar-reversed' class to put the sidebar on the right side
			// DOC: Apply 'page-full-width' class to the body element to have full width page without the sidebar menu
			out.println("<body class='login page-full-width'>");

			// BEGIN LOGO
			out.println("  <div class='logo' style='margin-top:0px;'>");
			out.println("    <a href='javascript:;'>");
			out.println("      <img src='isg/images/logos/system-logo-small.png' />");
			out.println("    </a>");
			out.println("  </div>");
			// END LOGO

			// BEGIN LOGIN
			out.println("  <div class='content'>");

			// BEGIN LOGIN FORM
			out.println("  <form class='login-form'>");
			out.println("    <h3 class='form-title'>" + mUser.getTermo("LOGIN") + "</h3>");
			out.println("    <div id='divErrorMessage' class='alert alert-danger display-hide'>");
			out.println("      <button type='button' class='close' data-close='alert'></button>");
			out.println("      <span id='spanErrorMessage'></span>");
			out.println("    </div>");
			out.println("    <div class='form-actions'>");
			// ie8, ie9 does not support html5 placeholder, so we just show field title for that
			out.println("      <label class='control-label visible-ie8 visible-ie9'>" + mUser.getTermo("USUARIO") + "</label>");
			out.println("      <div class='input-icon'>");
			out.println("        <i class='fa fa-user'></i>");
			out.println("        <input class='form-control placeholder-no-fix' type='text' autocomplete='" + ( wasLogged ? "off" : "on" ) + "' placeholder='" + mUser.getTermo("USUARIO") + "'  id='_field_login' name='_field_login' value='" + cookieLogin + "' " + ( wasLogged ? "disabled" : "" ) + "/>");
			out.println("      </div>");
			out.println("    </div>");

			out.println("    <div class='form-actions'>");
			out.println("      <label class='control-label visible-ie8 visible-ie9'>" + mUser.getTermo("SENHA") + "</label>");
			out.println("      <div class='input-icon'>");
			out.println("        <i class='fa fa-lock'></i>");
			out.println("        <input class='form-control placeholder-no-fix' type='password' autocomplete='off' placeholder='" + mUser.getTermo("SENHA") + "'  id='_field_password' name='_field_password'/>");
			out.println("      </div>");
			out.println("    </div>");

			out.println("    <div class='form-actions'>");
			if (wasLogged) {
				out.println("      <button type='button' class='btn blue-steel pull-left' onclick=\"doAjaxLogin('logout');\">");
				out.println("        <i class='fa fa-power-off font-white'></i> " + mUser.getTermo("SAIR"));
				out.println("      </button>");
			}
			out.println("      <button type='button' class='btn blue-steel pull-right' onclick=\"doAjaxLogin('login');\">");
			out.println("        " + mUser.getTermo("ACESSAR") + " <i class='fa fa-arrow-circle-right font-white'></i>");
			out.println("      </button>");
			out.println("    </div>");
			out.println("    <div class='forget-password'>");
			out.println("      <h4>" + mUser.getTermo("ESQUECEUSENHA") + "</h4>");
			out.println("      <p><a href='javascript:;' id='forget-password' class=''><strong>" + mUser.getTermo("LINK_RECUPERARSENHA") + "</strong></a></p>");
			out.println("    </div>");
			out.println("  </form>");
			// END LOGIN FORM

			// BEGIN FORGOT PASSWORD FORM
			out.println("  <form class='forget-form'>");
			out.println("    <h3 class='form-title'>" + mUser.getTermo("ESQUECEUSENHA") + "</h3>");
			out.println("    <div id='divSuccessMessage' class='alert alert-success display-hide'>");
			out.println("      <button type='button' class='close' data-close='alert'></button>");
			out.println("      <span id='spanSuccessMessage'></span>");
			out.println("    </div>");
			out.println("    <div id='divErrorMessageForgot' class='alert alert-danger display-hide'>");
			out.println("      <button type='button' class='close' data-close='alert'></button>");
			out.println("      <span id='spanErrorMessageForgot'></span>");
			out.println("    </div>");
			out.println("    <div class='form-actions'>");
			out.println("      <div class='input-icon'>");
			out.println("        <i class='fa fa-user'></i>");
			out.println("        <input class='form-control placeholder-no-fix' type='text' autocomplete='off' placeholder='" + mUser.getTermo("USUARIO") + "' id='_field_login_new' name='_field_login_new'/>");
			out.println("      </div>");
			out.println("    </div>");
			out.println("    <div class='form-actions'>");
			out.println("      <div class='input-icon'>");
			out.println("        <i class='fa fa-envelope'></i>");
			out.println("        <input class='form-control placeholder-no-fix' type='text' autocomplete='off' placeholder='" + mUser.getTermo("EMAIL") + "' id='_field_email' name='_field_email'/>");
			out.println("      </div>");
			out.println("    </div>");
			out.println("    <div class='form-actions'>");
			out.println("      <button type='button' id='back-btn' class='btn'>");
			out.println("        <i class='fa fa-arrow-circle-left font-white'></i> " + mUser.getTermo("VOLTAR") + " ");
			out.println("      </button>");
			out.println("      <button type='button' class='btn blue-steel pull-right' onclick=\"doAjaxLogin('send_password');\">");
			out.println("        " + mUser.getTermo("ENVIAR") + " <i class='fa fa-arrow-circle-right font-white'></i>");
			out.println("      </button>");
			out.println("    </div>");
			out.println("  </form>");
			// END FORGOT PASSWORD FORM
			out.println("</div>");
			// END LOGIN

			SimpleDateFormat format = new SimpleDateFormat("yyyy");
			// BEGIN COPYRIGHT
			out.println("<div class='copyright'> " + format.format(Calendar.getInstance().getTime()).toString() + " &copy; The Citizens Brasil  - v" + SystemConfig.SYSTEM_VERSION + "</div>");
			// END COPYRIGHT

			out.println("<input type='hidden' id='ajaxLogin' name='ajaxLogin' value=''>");

			// BEGIN JAVASCRIPTS (Load javascripts at bottom, this will reduce page load time)
			// BEGIN CORE PLUGINS
			out.println("<!--[if lt IE 9]>");
			out.println("<script src='metronic/global/plugins/respond.min.js'></script>");
			out.println("<script src='metronic/global/plugins/excanvas.min.js'></script>");
			out.println("<![endif]-->");
			out.println("<script src='metronic/global/plugins/jquery-1.11.0.min.js' type='text/javascript'></script>");
			out.println("<script src='metronic/global/plugins/jquery-migrate-1.2.1.min.js' type='text/javascript'></script>");
			out.println("<script src='metronic/global/plugins/bootstrap/js/bootstrap.js' type='text/javascript'></script>");
			// END CORE PLUGINS

			// BEGIN PAGE LEVEL PLUGINS
			out.println("<script src='metronic/global/plugins/backstretch/jquery.backstretch.min.js' type='text/javascript'></script>");
			out.println("<script type='text/javascript' src='metronic/global/plugins/bootstrap-select/bootstrap-select.min.js'></script>");
			out.println("<script type='text/javascript' src='metronic/global/plugins/select2/select2.js'></script>");
			// END PAGE LEVEL PLUGINS

			// BEGIN PAGE LEVEL SCRIPTS
			out.println("<script src='metronic/global/scripts/metronic.js' type='text/javascript'></script>");
			out.println("<script src='metronic/admin/layout/scripts/layout.js' type='text/javascript'></script>");
			// END PAGE LEVEL SCRIPTS

			out.println("<script>");
			out.println("  function reportLogin() {");
			out.println("    $.ajax({");
			out.println("      cache: false,");
			out.println("      contentType: \"application/x-www-form-urlencoded;charset=" + mUser.getUserCharset() + "\",");
			out.println("      data: top.$( top._form_reference.frm_login_sessao ).serialize(),");
			out.println("      type: top.$( top._form_reference.frm_login_sessao ).attr('method'),");
			out.println("      success: function(response) {");
			out.println("        top._form_reference.defaultView.processResponse('_div_results_content', response);");
			out.println("        top._form_reference.defaultView.Metronic.initAjax();");
			out.println("        top._form_reference.defaultView.Metronic.unblockUI('#_div_results_content');");
			out.println("        $( top.document.getElementById('_layer_login') ).remove();");
			out.println("      },");
			out.println("      error: function (xhr, ajaxOptions, thrownError) {");
			out.println("        $( top.document.getElementById('_layer_login') ).remove();");
			out.println("        top.bootbox(\"" + mUser.getTermo("ERROR_DEFAULT") + "\");");
			out.println("        top._form_reference.Metronic.unblockUI('#_div_results_content');");
			out.println("      }");
			out.println("    });");
			out.println("  }");

			out.println("  function format(lang) {");
			out.println("    var langOption = lang.element;");
			out.println("    return \"<img class='flag' src='\" + $(langOption).data('img-src') + \"' />&nbsp;&nbsp;&nbsp;\" + lang.text;");
			out.println("  }");

			out.println("  jQuery(document).ready(function() {");
			out.println("    Metronic.init();"); // init metronic core components
			out.println("    Layout.init();"); // init current layout

			out.println("    jQuery('#forget-password').click(function () {");
			out.println("      jQuery('.login-form').hide();");
			out.println("      jQuery('.forget-form').show();");
			out.println("    });");

			out.println("    jQuery('#back-btn').click(function () {");
			out.println("      jQuery('.login-form').show();");
			out.println("      jQuery('.forget-form').hide();");
			out.println("    });");

			out.println("    $.backstretch([");
			out.println("        'isg/images/backgrounds/blue.png'");
			out.println("      ], {");
			out.println("      fade: 1000,");
			out.println("      duration: 10000");
			out.println("    });");

			// out.println(" $('#_field_language').select2( { minimumResultsForSearch: -1, formatResult: format, formatSelection: format, escapeMarkup: function(m) { return m; } } );");

			out.println("    $('.login-form input').keydown(function (e) {");
			out.println("      if (e.which == 13) {");
			out.println("        if ($('#_field_login').is(':focus')) {");
			out.println("          $('#_field_login').blur();");
			out.println("          $('#_field_password').focus();");
			out.println("        }");
			out.println("        else if ($('#_field_password').is(':focus')) {");
			out.println("          doAjaxLogin('login');");
			out.println("        }");
			out.println("        return false;");
			out.println("      }");
			out.println("    });");
			out.println("    $('.forget-form input').keydown(function (e) {");
			out.println("      if (e.which == 13) {");
			out.println("        if ($('#_field_login_new').is(':focus')) {");
			out.println("          $('#_field_login_new').blur();");
			out.println("          $('#_field_email').focus();");
			out.println("        }");
			out.println("        else if ($('#_field_email').is(':focus')) {");
			out.println("          doAjaxLogin('send_password');");
			out.println("        }");
			out.println("        return false;");
			out.println("      }");
			out.println("    });");
			if (cookieLogin.equals("")) {
				out.println("    $('#_field_login').focus();");
			}
			else {
				out.println("    $('#_field_password').focus();");
			}
			out.println("    try { top.Metronic.scrollTo(); } catch(e) {}");
			out.println("  });");
			out.println("</script>");
			// END JAVASCRIPTS

			out.println("</body>");
			// END BODY

			out.println("</html>");
		}

	}

	public void destroy() {
	}
}
