
package br.com.manchestercity.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.SystemConfig;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.DateType;
import br.com.manchestercity.automacao.HeaderDropdown;
import br.com.manchestercity.automacao.HeaderDropdownNode;
import br.com.manchestercity.automacao.IniManipulation;
import br.com.manchestercity.automacao.LangManipulation;
import br.com.manchestercity.automacao.SystemIcons;
import br.com.manchestercity.automacao.User;
import br.com.manchestercity.automacao.Utils;

public class HeaderMenu extends HttpServlet {

	private static final long serialVersionUID = 8675748340268521453L;

	private static final String CONTENT_TYPE = "text/html";

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);

	
		
		PrintWriter out = response.getWriter();


		String acao = Database.verifyNull(request.getParameter("acao"));
		String userLogin = Database.verifyNull(request.getParameter("login"));
		String userPassword = Database.verifyNull(request.getParameter("pwd"));
		String userIdioma = Database.verifyNull(request.getParameter("idioma"));
		String userAuth = Database.verifyNull(request.getParameter("auth"));

		out.println("<script> alert('1-"+acao+"');</script>");
		User mUser = null;//User.getSession(request, response);
		
		Database lcdb = new Database(null);
		Connection lConn = lcdb.openConnection();
		Utils.noCacheStatic(response, request);

		if (acao.equals("logout")) {
			User.clearSession(request);

			Cookie lCookie = new Cookie("LOGIN_NOME", "");
			lCookie.setMaxAge(0);
			response.addCookie(lCookie);

			lCookie = new Cookie("LOGIN_AUTH'", "");
			lCookie.setMaxAge(0);
			response.addCookie(lCookie);

			User.limpaCookie(response);

			response.sendRedirect("main");
		}
		else if (!userLogin.equals("")) {
			mUser = new User(request);
			mUser.setUserLanguage(userIdioma);
			mUser.setUserLogin(userLogin);
			mUser.setUserPassword(userPassword);
		//	mUser.setUserAuth(userAuth);
			mUser.validateUser(lConn);
		}
		else {
			mUser = User.getSession(request, response);
		}

		if (mUser == null) {
			return;
		}


		response.setHeader("Content-Type", "text/html;charset=" + mUser.getUserCharset());

		out.println("<!DOCTYPE html>");
		out.println("<!--[if IE 8]> <html lang='en' class='ie8 no-js'> <![endif]-->");
		out.println("<!--[if IE 9]> <html lang='en' class='ie9 no-js'> <![endif]-->");
		out.println("<!--[if !IE]><!--> <html lang='en' class='no-js'> <!--<![endif]-->");

		// INICIO DO HEAD
		out.println("<head>");
		// out.println(" <meta charset='utf-8'/>");
		out.println("  <meta http-equiv='Content-Type' content='text/html; pageEncoding=" + mUser.getUserCharset() + " charset=" + mUser.getUserCharset() + "'>");
		out.println("  <title>" + SystemConfig.getSystemName() + " | The Citizens Brasil</title>");
		out.println("  <meta http-equiv='X-UA-Compatible' content='IE=edge'>");
		out.println("  <meta content='width=device-width, initial-scale=1.0' name='viewport'/>");
		out.println("  <meta content='The Citizens Brasil' name='description'/>");
		out.println("  <meta content='The Citizens Brasil' name='author'/>");
		out.println("  <meta http-equiv='Expires' content='Mon, 26 Jul 1997 00:00:00 GMT'>");
		out.println("  <meta http-equiv='Pragma' content='no-cache'>");

		// BEGIN GLOBAL MANDATORY STYLES
		out.println("  <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all' rel='stylesheet' type='text/css'/>");
		out.println("  <link href='metronic/global/plugins/font-awesome/css/font-awesome.min.css' rel='stylesheet' type='text/css'/>");
		out.println("  <link href='metronic/global/plugins/bootstrap/css/bootstrap.min.css' rel='stylesheet' type='text/css'/>");
		out.println("  <link href='metronic/global/plugins/uniform/css/uniform.default.css' rel='stylesheet' type='text/css'/>\n");
		// END GLOBAL MANDATORY STYLES

		// BEGIN PAGE LEVEL STYLES
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/select2/select2.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/datetimepicker.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/jquery-tags-input/jquery.tagsinput.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-markdown/css/bootstrap-markdown.min.css'>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/data-tables/DT_bootstrap.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/jquery-nestable/jquery.nestable.css'/>\n");
		// END PAGE LEVEL STYLES

		out.println("  <link rel='stylesheet' type='text/css' href='isg/plugins/jquery-contextMenu-master/jquery.contextMenu.min.css'/>\n");

		// BEGIN THEME STYLES
		out.println("  <link href='metronic/global/css/components.css' rel='stylesheet' type='text/css'/>");
		out.println("  <link href='metronic/global/css/plugins.css' rel='stylesheet' type='text/css'/>");
		out.println("  <link href='metronic/admin/layout/css/layout.css' rel='stylesheet' type='text/css'/>");
		out.println("  <link href='metronic/admin/layout/css/themes/" + ( IniManipulation.getProperty(IniManipulation.LAYOUT_THEME).equalsIgnoreCase("light") ? "isg-light.css" : "isg-default.css" ) + "' rel='stylesheet' type='text/css' id='style_color'/>");
		out.println("  <link href='metronic/admin/layout/css/custom.css' rel='stylesheet' type='text/css'/>\n");
		// END THEME STYLES

		out.println("  <link rel='shortcut icon' href='favicon.ico'/>");
		out.println("  <link rel='icon' href='favicon.ico' type='image/x-icon'>");
		out.println("  <script src='funcoes.jsp'></script>");
		out.println("  <script src='metronic/global/plugins/jquery-1.11.0.min.js' type='text/javascript'></script>");
		out.println("</head>");

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
		out.println("<body class='page-header-fixed page-header-fixed-mobile page-footer-fixed page-footer-fixed-mobile' style='overflow: hidden;'>");

		String lFixedId = String.valueOf(System.currentTimeMillis());

		out.println("<script>");
		out.println("var _form_reference = null;");
		out.println("var fixedValue = " + lFixedId + ";");
		out.println("var idAtual = '';");
		out.println("var vetFrame = new Array();");
		out.println("function getFree() {");
		out.println("  var i = 0;");
		out.println("  var lItem = vetFrame[i];");
		out.println("  while (lItem != undefined && lItem != null) {");
		out.println("    i++;");
		out.println("    lItem = vetFrame[i];");
		out.println("  }");
		out.println("  return i;");
		out.println("}");
		out.println("function getNumber(pTitle) {");
		out.println("  for (var i = 0; i < vetFrame.length; i++) {");
		out.println("    try {");
		out.println("      if (vetFrame[i][1] == pTitle) {");
		out.println("        return i;");
		out.println("      }");
		out.println("    } catch (e) {}");
		out.println("  }");
		out.println("  return -1;");
		out.println("}");
		out.println("function getLast() {");
		out.println("  var i = 100;");
		out.println("  var item = vetFrame[i];");
		out.println("  while ((item == undefined || item == null) && i > -1) {");
		out.println("    i--;");
		out.println("    item = vetFrame[i];");
		out.println("  }");
		out.println("  return i;");
		out.println("}");
		out.println("function getFixedValue() {");
		out.println("  return fixedValue;");
		out.println("}");
		out.println("function getDivAtual() {");
		out.println("  return idAtual;");
		out.println("}");
		out.println("function resizeWindow() {");
		out.println("    document.getElementById('sispart_page_container').style.height = (window.innerHeight - 80) + 'px';");
		out.println("}");
		out.println("function addAba(url, title, toolTip) {");
		out.println("  var cod = getFree();");
		out.println("  var id = 'id" + lFixedId + "' + cod;");
		out.println("  if (getNumber(title) == -1) {");
		out.println("    vetFrame[cod] = new Array(url, title, 0);");
		out.println("  } ");
		out.println("  else {");
		out.println("    cod = getNumber(title);");
		out.println("    id = 'id" + lFixedId + "' + cod;");
		out.println("    $('#li' + id.replace('id', '')).insertBefore( $('.page-footer-tabs-isg > .btn').first() );");
		out.println("  }");
		out.println("  if (document.getElementById('fr' + id.replace('id', '')) == undefined) {");
		// ADICIONA UM NOVO DIV NO CONTAINER DO HEADER
		out.println("    var preface = document.getElementById('sispart_page_container');");
		out.println("    resizeWindow();");
		// CRIA DIV QUE CONTEM IFRAME
		out.println("    var obj = document.createElement('div');");
		out.println("    obj.id = 'fr" + lFixedId + "' + cod;");
		out.println("    obj.style.width = '100%';");
		out.println("    obj.style.height = '100%';");
		out.println("    obj.style.backgroundColor = '#FFFFFF';");
		// CRIA UM NOVO IFRAME
		out.println("    var iframeObj = document.createElement('iframe');");
		// out.println(" iframeObj.src = url;");
		out.println("    iframeObj.id = id;");
		out.println("    iframeObj.name = id;");
		out.println("    iframeObj.frameborder = 0;");
		out.println("    iframeObj.scrolling = 'auto';");
		out.println("    iframeObj.style.border = '0';");
		out.println("    iframeObj.style.position = 'relative';");
		out.println("    iframeObj.style.width = '100%';");
		out.println("    iframeObj.style.height = '100%';");

		out.println("    obj.appendChild(iframeObj);");
		out.println("    preface.appendChild(obj);");

		out.println("    window.open(url, id);");

		out.println("    showChargerPage(id);");
		// ADICIONAR NA LISTA DE PAGINAS ABERTA
		/**
		 * out.println("    preface = document.getElementById('sispart_page_list');");
		 * out.println("    obj = document.createElement('li');");
		 * out.println("    obj.id = 'li" + lFixedId + "' + cod;");
		 * out.println("    obj.innerHTML = \"<a href='javascript:;' onclick=\\\"try { callFrame('\" + id + \"'); } catch(e) {}\\\"><span class='task'><i class='fa fa-ban' onclick=\\\"closeFrame('\" + id + \"');\\\"></i><span class='desc'> \" + title + \"&nbsp;</span></span></a>\";");
		 * out.println("    preface.appendChild(obj);");
		 * out.println("    document.getElementById('sispart_page_counter').innerHTML = new Number(document.getElementById('sispart_page_counter').innerHTML) + 1;");
		 **/
		out.println("    $('.btn-tab-sociall').addClass('btn-default').removeClass('btn-tab-sociall');");
		out.println("    $('.page-footer-tabs-isg').prepend( \"<a class='btn btn-xs btn-tab-sociall' onclick=\\\"try { callFrame('\" + id + \"'); } catch(e) {}\\\" href='javascript:;' id='li" + lFixedId + "\" + cod + \"' style='margin-right: 4px;'> <i class='fa fa-times font-red' onclick=\\\"closeFrame('\" + id + \"');\\\"></i> \" + title + \" </a>\" );");

		out.println("    $.contextMenu({");
		out.println("      selector: '#li" + lFixedId + "' + cod,");
		out.println("      callback: function(key, options) {");
		out.println("        closeFrame( id );");
		out.println("      },");
		out.println("      items: {");
		out.println("        'quit': {name: '" + mUser.getTermo("FECHAR") + "', icon: function() {");
		out.println("          return 'context-menu-icon context-menu-icon-quit';");
		out.println("        }}");
		out.println("      }");
		out.println("    });");

		out.println("  }");
		out.println("  else {");
		out.println("    window.open(url, id);");
		out.println("  }");
		out.println("  callFrame(id);");
		out.println("  $('.page-footer-tabs-isg').animate({ scrollLeft: '0px' }, 200, 'easeOutQuad');");
		out.println("  return id;");
		out.println("}");
		out.println("function callFrame(id) {");
		out.println("  if (document.getElementById(id) != undefined && document.getElementById(id) != null) {");
		out.println("    if (idAtual != '') {");
		out.println("      try {");
		out.println("        $('#fr' + idAtual.replace('id', '')).hide();");
		out.println("      }");
		out.println("      catch(e) {}");
		out.println("    }");
		out.println("    $('.btn-tab-sociall').addClass('btn-default').removeClass('btn-tab-sociall');");
		out.println("    $('#li' + id.replace('id', '')).removeClass('btn-default').addClass('btn-tab-sociall');");
		out.println("    $('#fr' + id.replace('id', '')).show();");
		out.println("    idAtual = id;");
		out.println("    resizeWindow();");
		out.println("  }");
		out.println("}");
		out.println("function closeFrame(id) {");
		out.println("  if (id != '') {");
		out.println("    var idAux = id.replace('id', '');");
		out.println("    $('#fr' + idAux).hide();");
		out.println("    $('#li' + idAux).hide();");

		out.println("    $('#' + id)[0].onLoad = null;");
		out.println("    $('#' + id).contents().find('*').addBack().off().empty();");

		out.println("    for (var prop in $('#' + id)[0].contentWindow.window) {");
		out.println("      eval(\" try { delete $('#' + id)[0].contentWindow.window.\" + prop + \"; } catch(e) {} \");");
		out.println("    }");

		out.println("    $('#' + id).off().unbind().undelegate();");
		out.println("    $('#' + id).removeChildrenFromDom();");
		out.println("    $.removeData( $('#' + id) );");
		out.println("    $('#' + id).purgeFrame();");

		out.println("    $('#fr' + idAux).off().unbind().undelegate();");
		out.println("    $('#fr' + idAux).removeChildrenFromDom();");
		out.println("    $.removeData( $('#fr' + idAux) );");
		out.println("    $('#fr' + idAux)[0].parentNode.removeChild($('#fr' + idAux)[0]);");

		out.println("    $('#li' + idAux).off().unbind().undelegate();");
		out.println("    $('#li' + idAux).removeChildrenFromDom();");
		out.println("    $.removeData( $('#li' + idAux) );");
		out.println("    $('#li' + idAux)[0].parentNode.removeChild($('#li' + idAux)[0]);");

		out.println("    vetFrame[id.replace('id" + lFixedId + "', '') * 1] = null;");
		/** out.println("    document.getElementById('sispart_page_counter').innerHTML = new Number(document.getElementById('sispart_page_counter').innerHTML) - 1;"); **/
		out.println("    if (idAtual == id) {");
		out.println("      idAtual = '';");
		out.println("      var lastId = getLast();");
		out.println("      if (lastId > -1) {");
		out.println("        callFrame('id" + lFixedId + "' + lastId);");
		out.println("      }");
		out.println("      else {");
		out.println("        resizeWindow();");
		out.println("      }");
		out.println("    }");
		out.println("    if (typeof(CollectGarbage) == 'function') {");
		out.println("      CollectGarbage();");
		out.println("    }");
		out.println("  }");
		out.println("}");
		out.println("function makeChange(type, cd_change) {");
		out.println("  if (type == 'idioma') {");
		out.println("    document._frm_changes.idioma.value = cd_change;");
		out.println("    document._frm_changes.empresa.value = '';");
		out.println("  }");
		out.println("  else {");
		out.println("    document._frm_changes.idioma.value = '';");
		out.println("    document._frm_changes.empresa.value = cd_change;");
		out.println("  }");
		out.println("  document._frm_changes.submit();");
		out.println("}");
		out.println("function returnFavorito(ret, nome) {");
		out.println("  var targetId = addAba('', nome);");
		out.println("  var childDoc = document.getElementById(targetId).contentWindow.document;");
		out.println("  var tagForm = childDoc.createElement('div');");
		out.println("  tagForm.innerHTML = ret.responseText;");
		out.println("  tagForm.style.display = 'none';");
		out.println("  childDoc.body.appendChild(tagForm);");
		out.println("  childDoc.getElementById('frm_fvrts').target = '_self';");
		out.println("  childDoc.getElementById('frm_fvrts').submit();");
		out.println("  Metronic.init();"); // IE SUCKS!!! NÃO REMOVER OU A FRAMEWORK PARA DE FUNCIONAR!!!
		out.println("}");
		out.println("</script>");

		out.println("<script>");
		out.println("(function( $ ) {");
		out.println("  $.fn.removeChildrenFromDom = function (i) {");
		out.println("    if (!this) return;");
		out.println("    this.find('input[type=\"submit\"]').unbind();"); // UNWIRE SUBMIT BUTTONS
		out.println("    this.children()");
		out.println("      .empty()"); // jQuery EMPTY OF CHILDREN
		out.println("      .each(function (index, domEle) {");
		out.println("          try { domEle.innerHTML = ''; } catch(e) {}");  // HTML CHILD ELEMENT CLEAR
		out.println("        });");
		out.println("    this.empty();"); // jQuery EMPTY
		out.println("    try { this.get().innerHTML = ''; } catch(e) {}"); // HTML ELEMENT CLEAR
		out.println("  };");
		out.println("})( jQuery );");
		out.println("</script>");

		out.println("<script>\n");
		out.println("  function showChargerPage(id) {");
		out.println("    if ($(id).length > 0) {");
		out.println("      try { Metronic.blockUI( { target: '#' + id, boxed: true, message: '" + mUser.getTermo("CARREGANDO") + "' } ); } catch(e) {}");
		out.println("    }");
		out.println("    else {");
		out.println("      try { Metronic.blockUI( { boxed: true, message: '" + mUser.getTermo("CARREGANDO") + "' } ); } catch(e) {}");
		out.println("    }");
		out.println("  }");
		out.println("  function closeChargerPage(id) {");
		out.println("    if ($(id).length > 0) {");
		out.println("      try { Metronic.unblockUI('#' + id); } catch(e) {}");
		out.println("    }");
		out.println("    else {");
		out.println("      try { Metronic.unblockUI(); } catch(e) {}");
		out.println("    }");
		out.println("  }");
		out.println("</script>");

		userIdioma = userIdioma.equals("") ? mUser.getUserLanguage() : userIdioma;

		out.println("  <form method='POST' name='_frm_changes' target='_self' action='main'>");
		out.println("    <input type='hidden' name='idioma' id='idioma' value='" + userIdioma + "'>");
		out.println("  </form>");

		// BEGIN HEADER MENU
		out.println("  <div class='page-header navbar navbar-fixed-top'>");

		// BEGIN HEADER INNER
		out.println("    <div class='page-header-inner'>");

		// BEGIN LOGO
		out.println("      <div class='page-logo hidden-xs hidden-sm'>");
		out.println("        <span class='span-logo'>");
		// out.println(" <img src='pics/logos/" + ( IniManipulation.getProperty(IniManipulation.LAYOUT_THEME).equalsIgnoreCase("light") ? "isg-light.css" : "isg-default.css" ) + "' alt='" + SystemConfig.getSystemName() + "' title='" + SystemConfig.getSystemName() + "' class='text-logo' style='padding-left:10px;' />");
		out.println("          <img height=50 src='isg/images/logos/" + ( IniManipulation.getProperty(IniManipulation.LAYOUT_THEME).equalsIgnoreCase("light") ? "supertroca.png" : "isg-default.css" ) + "' alt='" + SystemConfig.getSystemName() + "' title='" + SystemConfig.getSystemName() + "' class='text-logo' style='padding-left:10px;' />");
		out.println("        </span>");
		out.println("      </div>");
		// END LOGO

		// BEGIN HORIZANTAL MENU
		// DOC: Remove 'hor-menu-light' class to have a horizontal menu with theme background instead of white background
		// DOC: This is desktop version of the horizontal menu. The mobile version is defined(duplicated) in the responsive menu below along with sidebar menu. So the horizontal menu has 2 seperate versions
		// DOC: Add data-hover='dropdown', data-close-others='true' and data-toggle='dropdown' attributes for the below 'dropdown-toggle' links to enable hover dropdowns
		HeaderDropdown dropdown = new HeaderDropdown(mUser);
		dropdown.addNode("", "Requisições", "displaydatalist?funcao=cad_request", "", null);

		HeaderDropdownNode node1 = new HeaderDropdownNode();
		// HeaderDropdownNode node2 = new HeaderDropdownNode();

		/* SEGURANÇA */
		node1 = dropdown.addNode("", "SEGURANCA", "", "", null);

		
		if (mUser.direitoFuncao("cad_usuario", "R")) {
			dropdown.addNode(SystemIcons.ICON_USER, mUser.funcaoDesc("cad_usuario"), "displaydatalist?funcao=cad_usuario", "", node1);
		}

		

		dropdown.setID("_header_menu");
		out.println(dropdown.showHeaderDropdown());

		// END HORIZANTAL MENU



		// BEGIN TOP NAVIGATION MENU
		out.println("      <div class='top-menu'>");
		out.println("        <ul class='nav navbar-nav pull-right'>");


		// BEGIN USER LOGIN DROPDOWN
		out.println("          <li class='dropdown dropdown-user'>");
		out.println("            <a href='javascript:;' class='dropdown-toggle' data-toggle='dropdown' data-close-others='true'>");
		out.println("              <img alt='' src='metronic/admin/layout/img/spacer.png'/>");
		out.println("              <span class='username'>" + mUser.getUserName().trim() + "</span>");
		out.println("              " + SystemIcons.ICON_ANGLE_DOWN);
		out.println("            </a>");
		out.println("            <ul class='dropdown-menu'>");
		
		out.println("              <li class='divider'></li>");
		out.println("              <li>");
		out.println("                <a href='main?acao=logout'>" + SystemIcons.ICON_POWER_OFF + " " + mUser.getTermo("SAIR") + "&nbsp;</a>");
		out.println("              </li>");
		out.println("            </ul>");
		out.println("          </li>");
		// END USER LOGIN DROPDOWN
		out.println("        </ul>");
		out.println("      </div>");
		// END TOP NAVIGATION MENU

		// BEGIN RESPONSIVE MENU TOGGLER
		out.println("      <div class='menu-toggler responsive-toggler hidden-md hidden-lg' data-toggle='collapse' data-target='.navbar-collapse'></div>");
		// END RESPONSIVE MENU TOGGLER

		out.println("    </div>");
		// END HEADER INNER

		// BOOTSTRAP SIDE MENU
		out.println("    <div style='margin-top: 46px !important;'>");
		out.println(dropdown.showAsSidebar());
		out.println("    </div>");

		out.println("  </div>");
		// END HEADER

		out.println("  <div class='clearfix'></div>");

		// BEGIN CONTAINER
		out.println("  <div class='page-container' id='sispart_page_container' style='width: 100%;'>");
		out.println("  </div>");
		// END CONTAINER

		// BEGIN FOOTER
		out.println("  <div class='page-footer' style='padding: 6px 4px 4px 8px'>");
		out.println("    <div class='page-footer-inner'>");
		out.println("      <div class='page-footer-copy-isg'> " + lcdb.getActualDate(DateType.YEAR) + " &copy; The Citizens Brasil</div>");
		out.println("      <div class='page-footer-tabs-isg'></div>");
		out.println("      <a class='btn btn-xs btn-default' href='javascript:;' onmousedown=\"moveTabs('left');\" style='margin-right: 4px;'> " + SystemIcons.ICON_ANGLE_LEFT + " </a>");
		out.println("      <a class='btn btn-xs btn-default' href='javascript:;' onmousedown=\"moveTabs('right');\"> " + SystemIcons.ICON_ANGLE_RIGHT + " </a>");
		out.println("    </div>");
		out.println("    <div class='page-footer-tools'>");
		out.println("      <span class='go-top'>");
		out.println("        " + SystemIcons.ICON_ANGLE_UP);
		out.println("      </span>");
		out.println("    </div>");
		out.println("  </div>");
		out.println("  <script>");
		out.println("    function moveTabs(side) {");
		out.println("      if (side === 'right') {");
		out.println("        $('.page-footer-tabs-isg').animate({ scrollLeft: '+=64' }, 200, 'easeOutQuad');");
		out.println("      }");
		out.println("      else {");
		out.println("        $('.page-footer-tabs-isg').animate({ scrollLeft: '-=64' }, 200, 'easeOutQuad');");
		out.println("      }");
		out.println("    }");
		out.println("  </script>");
		// END FOOTER

		// BEGIN MODAL FAVORITE
		out.println("<script>");
		out.println("  function toggleFavorite(result) {");
		out.println("    result = result.responseText;");
		out.println("    if (!result || result.indexOf('erro') > -1) {");
		out.println("      top.bootbox.alert('" + mUser.getTermo("ERROR_DEFAULT") + "');");
		out.println("    }");
		out.println("    else if(result) {");
		out.println("      top.bootbox.alert('" + mUser.getTermo("SUCCESS_DEFAULT") + "');");
		out.println("    }");
		out.println("  }");
		out.println("  function saveFavorite() {");
		out.println("    postForm( document._form_ajax_favorito, false, toggleFavorite );");
		out.println("  }");
		out.println("</script>");

		out.println("<div class='modal fade modal-scroll in' data-backdrop='static' data-replace='true' id='_modal_favorito'>");
		out.println("  <div class='modal-dialog'>");
		out.println("    <div class='modal-content'>");
		out.println("      <div class='modal-header'>");
		out.println("        <button type='button' class='close' data-dismiss='modal' aria-hidden='true'></button>");
		out.println("        <h4 class='modal-title'>" + mUser.getTermo("FAVORITOS") + "</h4>");
		out.println("      </div>");
		out.println("      <div class='modal-body form'>");
		out.println("        <div class='form-horizontal form-row-seperated' id='_html_modal_favorito' >");
		out.println("        </div>");
		out.println("      </div>");
		out.println("      <div class='modal-footer'>");
		out.println("        <button type='button' class='btn btn-default' data-dismiss='modal'> " + mUser.getTermo("CANCELAR") + " </button>");
		out.println("        <button type='button' class='btn btn-primary' data-dismiss='modal' onclick='saveFavorite();'>" + SystemIcons.ICON_SAVE + " " + mUser.getTermo("SALVAR") + " </button>");
		out.println("      </div>");
		out.println("    </div>");
		out.println("  </div>");
		out.println("</div>");
		// END MODAL FAVORITE

		out.println("<script>");
		out.println("  var frameLocalizar = '';");
		out.println("</script>");

		// BEGIN MODAL WRAPPER
		out.println("<div id='_sispart_modal_wrapper'></div>");
		// END MODAL WRAPPER

		// BEGIN JAVASCRIPTS (LOAD JAVASCRIPTS AT BOTTOM, THIS WILL REDUCE PAGE LOAD TIME)
		// BEGIN CORE PLUGINS
		out.println("  <!--[if lt IE 9]>");
		out.println("  <script src='metronic/global/plugins/excanvas.min.js'></script>");
		out.println("  <script src='metronic/global/plugins/respond.min.js'></script>  ");
		out.println("  <![endif]-->");
		out.println("  <script src='metronic/global/plugins/jquery-migrate-1.2.1.min.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/global/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js' type='text/javascript'></script> \n");
		out.println("  <script src='metronic/global/plugins/bootstrap/js/bootstrap.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/global/plugins/jquery.blockui.min.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/global/plugins/uniform/jquery.uniform.min.js' type='text/javascript'></script>");
		// END CORE PLUGINS

		out.println("  <script type='text/javascript' src='isg/plugins/jquery-contextMenu-master/jquery.contextMenu.min.js'></script>");
		out.println("  <script type='text/javascript' src='isg/plugins/jquery.purgeFrame.js'></script>");

		// BEGIN PAGE LEVEL PLUGINS
		out.println("  <script type='text/javascript' src='metronic/global/plugins/select2/select2.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/data-tables/jquery.dataTables.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/data-tables/DT_bootstrap.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/bootbox/bootbox.min.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker-twitter.min.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js'></script>");
		// END PAGE LEVEL PLUGINS

		out.println("  <script src='metronic/global/scripts/metronic.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/admin/layout/scripts/layout.js' type='text/javascript'></script>");
		out.println("  <script>");
		out.println("    jQuery(document).ready(function() {");
		out.println("      Metronic.init();"); // INIT METRONIC CORE COMPONENTS
		out.println("      Layout.init();"); // INIT CURRENT LAYOUT
		out.println("      jQuery('.page-footer').on('click', '.go-top', function (e) {");
		out.println("        document.getElementById(idAtual).contentWindow.Metronic.scrollTo();");
		out.println("        e.preventDefault();");
		out.println("      });");
		// HANDLE MENU SEARCH BOX
		out.println("      $('.page-header').on('click', '.search-form', function(e) {");
		out.println("        $(this).addClass('open');");
		out.println("        $(this).find('.form-control').focus();");
		out.println("        $('.page-header .search-form .form-control').on('blur', function(e) {");
		out.println("          $(this).closest('.search-form').removeClass('open');");
		out.println("          $(this).unbind('blur');");
		out.println("        });");
		out.println("      });");
		out.println("      resizeWindow();");
		out.println(" addAba('dashboard', '" + mUser.getTermo("DASHBOARD") + "', '" + mUser.getTermo("DASHBOARD") + "');");
		out.println("    });");
		out.println("    jQuery(window).resize(function() {");
		out.println("      resizeWindow();");
		out.println("    });");
		out.println("  </script>");
		// END JAVASCRIPTS

		out.println("  </body>");
		// END BODY

		out.println("</html>");
	}

	public void finalize() {
	}
}
