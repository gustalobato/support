
package br.com.manchestercity.automacao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.SystemConfig;
import br.com.manchestercity.automacao.FrameworkDefaults.DataTable;
import br.com.manchestercity.automacao.FrameworkDefaults.FieldSet;
import br.com.manchestercity.automacao.FrameworkDefaults.FormInput;
import br.com.manchestercity.automacao.FrameworkDefaults.PortletBox;
import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.parsing.CosUploadFile;

public class PageContent extends HttpServlet {

	protected String mAcao = "";
	protected String mTitle = "";
	protected String mAction = "";
	protected String mSentido = "";
	protected String mOrdem = "";
	protected String mFiltro = "";
	protected String mBaseFilter = "";
	protected String mPagina = "";
	protected String mQuickSearch;
	protected String mFuncao = "";
	protected String mActionJSForm = "";
	protected String mSQL = "";
	protected String mNewWindow = "";
	protected String mAcaoNew = "";
	protected String mDisabled = "";
	protected String mUrlPrint = "";
	protected String mExtraParameters = "";
	protected String mReturnPage = "displaydatalist?firstOpen=false";
	protected String mSecurityFunction;

	protected String mPageHeaderTags = "";
	protected String mPageFooterTags = "";
	protected String mPageFooterScripts = "";

	protected boolean mRequestOnError;
	protected boolean mContinueUpdate;
	protected boolean mAutoBack;
	protected boolean mValidateError;
	protected boolean mShowMenu;
	protected boolean mReadOnly;
	protected boolean mVerifySecurityFunction = false;
	protected boolean mNoDelete;
	protected boolean mNoSave;
	protected boolean mNoNew;
	protected boolean mNoBack;

	protected boolean mSidebarLayer = false;

	protected int mCountControls = 0;
	protected int mCountPrimarys = 0;

	protected User mUser;
	protected Utils mUtil;
	protected ResultSet rs;
	protected Database lcdb;
	protected Connection mConn;
	protected TitleMenu mTitleMenu;
	protected SidebarMenu mSidebarMenu;
	protected DataControl controls[];
	protected DataControl primarys[];

	protected StringBuffer mErro = new StringBuffer();

	protected ArrayList<String> mMandatoryIDs = new ArrayList<String>();

	private String mAcaoOld;
	private String mStrError;
	private boolean mIsUpload;
	private boolean mPrintUploadForm;
	private HttpServletRequest mRequest;
	private MultipartFormDataRequest mUpRequest;

	private static final long serialVersionUID = 8675748340268521453L;

	private static final String CONTENT_TYPE = "text/html";

	public FrameworkDefaults defaults;
	public FormInput formInput;
	public PortletBox portletBox;
	public DataTable dataTable;
	public FieldSet fieldset;
	public SmartCombo loc;
	public DatePicker dtp;

	private int mPageWidth;

	protected boolean mPublicSession = false;
	protected String mSessionName = "";
	protected String mLangParameter = "";

	private boolean hideDataTableBootstrap = false;

	public void setHideDataTableBootstrap(boolean hideDataTableBootstrap) {
		this.hideDataTableBootstrap = hideDataTableBootstrap;
	}

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		mRequest = request;

		response.setContentType(CONTENT_TYPE);

		response.setHeader("X-XSS-Protection", "0");

		Utils.noCacheStatic(response, mRequest);

		// METODO PARA SETAR AS VARIAVEIS DE SESSOES ABERTAS AO PUBLICO
		this.setPublicSession();

		if (mPublicSession) {
			mUser = User.getSession(request, null, false, false, mSessionName);
			if (mUser == null) {
				mUser = new User(request);
				mUser.setUserLanguage(Database.verifyNull(request.getParameter(mLangParameter), User.DEFAULT_LANGUAGE));
			}
		}
		else {
			mUser = User.getSession(request, response);
		}

		response.setHeader("Content-Type", "text/html;charset=" + mUser.getUserCharset());

		mSecurityFunction = "";
		mAcaoOld = "";
		mRequestOnError = false;
		mErro = new StringBuffer();
		mValidateError = false;
		mIsUpload = false;
		mPrintUploadForm = false;
		mContinueUpdate = false;
		mAutoBack = true;
		mShowMenu = true;
		mNoDelete = false;
		mNoSave = false;
		mNoNew = false;
		mNoBack = false;

		mSidebarLayer = false;

		lcdb = new Database(mUser);
		mUtil = new Utils(mUser);
		mConn = lcdb.openConnection();
		mTitleMenu = new TitleMenu(mUser);
		mSidebarMenu = new SidebarMenu(mUser);
		mStrError = "";

		mCountPrimarys = 0;
		mCountControls = 0;

		primarys = new DataControl[120];
		controls = new DataControl[120];

		defaults = new FrameworkDefaults();
		formInput = defaults.new FormInput();
		portletBox = defaults.new PortletBox();
		dataTable = defaults.new DataTable();
		fieldset = defaults.new FieldSet();
		dataTable.setEmptyMessage(mUser.getTermo("MSGNENHUMREGENC"));

		mPageWidth = 60;

		PrintWriter out = response.getWriter();
		boolean lVoltar = false;
		int i = 0;

		initialize(response);

		getBasicInformation();

		getPrimaries(response);

		verifyAccess(response, "R");

		setAction();

		getParameters(response);

		extraParameters(response);

		setTitle();

		addControls();

		out.println("<!DOCTYPE html>");
		out.println("<!--[if IE 8]> <html lang='en' class='ie8 no-js'> <![endif]-->");
		out.println("<!--[if IE 9]> <html lang='en' class='ie9 no-js'> <![endif]-->");
		out.println("<!--[if !IE]><!--> <html lang='en' class='no-js'> <!--<![endif]-->\n");

		// INICIO DO HEAD
		out.println("<head>");
		// out.println(" <meta charset='utf-8'/>");
		out.println("  <meta http-equiv='Content-Type' content='text/html; pageEncoding=" + mUser.getUserCharset() + " charset=" + mUser.getUserCharset() + "'>");
		out.println("  <title>" + SystemConfig.getSystemName() + " | The Citizens Brasil</title>");
		out.println("  <meta http-equiv='X-UA-Compatible' content='IE=edge'>");
		out.println("  <meta content='width=device-width, initial-scale=1.0' name='viewport'/>");
		out.println("  <meta content='The Citizens Brasil' name='description'/>");
		out.println("  <meta content='The Citizens Brasil' name='author'/>\n");

		// BEGIN GLOBAL MANDATORY STYLES
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/open-sans/css/open-sans.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/font-awesome/css/font-awesome.min.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap/css/bootstrap.min.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/uniform/css/uniform.default.css' />\n");
		// END GLOBAL MANDATORY STYLES

		// BEGIN PAGE LEVEL STYLES
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/select2/select2.css' />\n");
		if (mPrintUploadForm) {
			out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-fileinput/bootstrap-fileinput.css'/>");
		}
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/datetimepicker.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-markdown/css/bootstrap-markdown.min.css'>");
		if (!hideDataTableBootstrap)
			out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/data-tables/DT_bootstrap.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/jquery-nestable/jquery.nestable.css'/>\n");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/fullcalendar/fullcalendar/fullcalendar.css' />");
		// END PAGE LEVEL STYLES

		out.println(mPageHeaderTags);

		// BEGIN THEME STYLES
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/css/components.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/css/plugins.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/layout.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/themes/" + ( IniManipulation.getProperty(IniManipulation.LAYOUT_THEME).equalsIgnoreCase("light") ? "isg-light.css" : "isg-default.css" ) + "' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/custom.css' />\n");
		// END THEME STYLES

		out.println("  <link rel='shortcut icon' href='favicon.ico'/>");
		out.println("  <script src='metronic/global/plugins/jquery-1.11.0.min.js' type='text/javascript'></script>\n");

		out.println("<script>\n");
		out.println("  function showChargerPage() {");
		out.println("    try { Metronic.blockUI( { boxed: true, message: '" + mUser.getTermo("CARREGANDO") + "' } ); } catch(e) {}");
		out.println("  }");
		out.println("  function closeChargerPage() {");
		out.println("    try { Metronic.unblockUI(); } catch(e) {}");
		out.println("  }");
		out.println("</script> \n");

		if (!mAcao.equals("")) {
			if (mAcao.equals("upd")) {
				retrieveInformation();
			}
			else {
				if (mAcao.equals("delete") && verifyAccess(response, "D")) {
					if (validateDelete(out)) {
						transactionBegin(mConn);
						if (!delete(response)) {
							mAcao = "upd";
							retrieveInformation();
						}
						else {
							if (!mAutoBack) {
								mAcao = "ins";
							}
							else {
								mAcao = "ins";
								lVoltar = true;
							}
						}
						transactionCommit(mConn);
					}
					else {
						mAcao = "upd";
						retrieveInformation();
					}
				}
				else {
					if (!mAcao.equals("ins")) {
						if (validate(out) && validate(response, out)) {
							if (mAcao.equals("insert") && verifyAccess(response, "C")) {
								transactionBegin(mConn);
								insert(response);
								if (mContinueUpdate || mAcao.equals("upd") || mAcao.equals("update")) {
									mAcao = "upd";
									retrieveInformation();
								}
								transactionCommit(mConn);
							}
							else {
								if (mAcao.equals("update") && verifyAccess(response, "U")) {
									transactionBegin(mConn);
									update(response);
									retrieveInformation();
									transactionCommit(mConn);
								}
								if (mAcao.equals("update")) {
									mAcao = "upd";
									retrieveInformation();
								}
							}
						}
						else {
							mValidateError = true;
							getOldValues();
						}
					}
				}
			}
		}

		setToolBars();

		out.println(printPageLoader());

		if (!mStrError.equals("")) {
			// out.println(" <script>alert('" + mStrError + "');</script>");
			// out.println(" <script>jQuery(document).ready(function() { " + ( !IniManipulation.getProperty("new_layout").equals("") ? "top." : "" ) + "bootbox.alert('" + Utils.replaceAll(mStrError, "\\n", "<br/>") + "'); });</script>");
			out.println("  <script>jQuery(document).ready(function() { top.bootbox.alert('" + Utils.replaceAll(mStrError, "\\n", "<br/>") + "'); });</script>");
		}

		out.println("<script>\n");
		script(out);
		out.println("</script>\n");
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
		sidebarConfig(mSidebarMenu);
		out.println("<body bgcolor='#FFFFFF' leftmargin='0' bottonmargin='0' rightmargin='0' topmargin='0'" + ( mSidebarMenu.getItensCount() > 0 ? "" : "class='page-full-width'" ) + ">");

		out.println("  <form name='_frm' id='_frm' action='" + mAction + "' method='POST' enctype='text/html'>");

		if (mPrintUploadForm) {
			out.println("    <input type='hidden' name='acao' id='acao' value='" + mAcao + "'>");
			out.println("    <input type='hidden' name='acao_old' value='" + mAcao + "'>");
			out.println("    <input type='hidden' name='sentido' value='" + mSentido + "'>");
			out.println("    <input type='hidden' name='ordem' value='" + mOrdem + "'>");
			out.println("    <input type='hidden' name='pagina' value='" + mPagina + "'>");
			out.println("    <input type='hidden' name='funcao' value='" + mFuncao + "'>");
			out.println("    <input type='hidden' name='quick_search' value='" + mQuickSearch + "'>");
			out.println("    <input type='hidden' name='extraparameters' value='" + mExtraParameters + "'>");
			out.println("    <textarea style='display:none; position:absolute;' name='filtro'>" + mFiltro + "</textarea>");
			out.println("    <textarea style='display:none; position:absolute;' name='baseFilter'>" + mBaseFilter + "</textarea>");
			for (i = 0; i < mCountPrimarys; i++) {
				out.println("    <input type='hidden' name='" + primarys[i].getControl() + "' value='" + Database.verifyNull(primarys[i].getValue()) + "'>");
			}
			out.println("  </form>");
			out.println("  <form name='_frm_multipart' id='_frm_multipart' action='" + mAction + "' method='POST' enctype='multipart/form-data'>");
		}

		out.println("    <input type='hidden' name='acao' id='acao' value='" + mAcao + "'>");
		out.println("    <input type='hidden' name='acao_old' value='" + mAcao + "'>");
		out.println("    <input type='hidden' name='sentido' value='" + mSentido + "'>");
		out.println("    <input type='hidden' name='ordem' value='" + mOrdem + "'>");
		out.println("    <input type='hidden' name='pagina' value='" + mPagina + "'>");
		out.println("    <input type='hidden' name='funcao' value='" + mFuncao + "'>");
		out.println("    <input type='hidden' name='quick_search' value='" + mQuickSearch + "'>");
		out.println("    <input type='hidden' name='extraparameters' value='" + mExtraParameters + "'>");
		out.println("    <textarea style='display:none; position:absolute;' name='filtro'>" + mFiltro + "</textarea>");
		out.println("    <textarea style='display:none; position:absolute;' name='baseFilter'>" + mBaseFilter + "</textarea>");
		for (i = 0; i < mCountPrimarys; i++) {
			out.println("<input type='hidden' name='" + primarys[i].getControl() + "' value='" + Database.verifyNull(primarys[i].getValue()) + "'>");
		}

		if (mAcao.startsWith("ins")) {
			mNoDelete = true;
		}

		buildScripts(out);

		if (mErro.length() > 0) {
			getOldValues();
			if (mErro.toString().toUpperCase().indexOf("CHILD RECORD FOUND") < 0) {
				// out.println("<script>jQuery(document).ready(function() { " + ( !IniManipulation.getProperty("new_layout").equals("") ? "top." : "" ) + "bootbox.alert('" + mErro.toString().replaceAll("'", "´").replaceAll("\\n", "<br/>") + "'); });</script>");
				out.println("<script>jQuery(document).ready(function() { top.bootbox.alert('" + mErro.toString().replaceAll("'", "´").replaceAll("\\n", "<br/>") + "'); });</script>");
			}
			else {
				// out.println("<script>jQuery(document).ready(function() { " + ( !IniManipulation.getProperty("new_layout").equals("") ? "top." : "" ) + "bootbox.alert('" + mUser.getTermo("MSGDEPENDENTES") + "'); });</script>");
				out.println("<script>jQuery(document).ready(function() { top.bootbox.alert('" + mUser.getTermo("MSGDEPENDENTES") + "'); });</script>");
			}
		}

		// BEGIN CONTAINER
		out.println("  <div class='page-container'>");

		// BEGIN SIDEBAR
		if (mSidebarMenu.getItensCount() > 0) {
			out.println(mSidebarMenu.printSidebarMenu());
		}
		// END SIDEBAR

		// BEGIN CONTENT
		out.println("    <div class='page-content-wrapper'>");
		// if(mAction.equals("pagebuscaoferta"))
		// out.println(" <div class='page-content' id='_div_isg_page_content' style='padding-bottom: 0px;'>");
		// else
		out.println("      <div class='page-content' id='_div_isg_page_content'>");

		// BEGIN RESPONSIVE MENU TOGGLER
		if (mSidebarMenu.getItensCount() > 0) {
			out.println("        <div class='theme-panel hidden-md hidden-lg'>");
			out.println("          <div class='side-toggler responsive-toggler hidden-md hidden-lg' data-toggle='collapse' data-target='.navbar-collapse' style='float: left; position: relative;'></div>");
			out.println("          <div class='side-toggler-close responsive-toggler hidden-md hidden-lg' data-toggle='collapse' data-target='.navbar-collapse'></div>");
			out.println("        </div>");
		}
		// END RESPONSIVE MENU TOGGLER

		// BEGIN PAGE TITLE MENU
		buildTitleMenu(out);
		// END PAGE TITLE MENU

		out.println("        <div class='row'>");
		out.println("          <div class='col-md-12'>");

		// AUX FOR POSITIONING RESPONSIVE MENU TOGGLER
		if (mSidebarMenu.getItensCount() > 0) {
			out.println("            <h2 class='page-title hidden-md hidden-lg'> &nbsp; </h2>");
		}

		// BEGIN PAGE HTML

		if (mPageWidth > 100) {
			mPageWidth = 100;
		}
		else if (mPageWidth < 10) {
			mPageWidth = 50;
		}

		out.println("<div class='row'>");
		out.println("  <div style='width:" + mPageWidth + "%; margin-left: auto; margin-right: auto;'>");
		out.println("    <div class='form col-md-12'>");
		out.println("      <div class='horizontal-form'>");
		try {
			html(out);
		}
		catch (InputMaxLengthException e) {
			Utils.printSystemError("Html(out)", e.getMessage());
		}
		out.println("      </div>");// horizontal-form
		out.println("    </div>");// 'form col-md-12'
		out.println("  </div>");// mPageWidth
		out.println("</div>");//// row
		// END PAGE HTML

		out.println("          </div>");// col-md-12
		out.println("        </div>");// row
		out.println("        <div id='_div_isg_page_content_overlay' style='display: none;'></div>");
		out.println("      </div>");// id='_div_isg_page_content'
		out.println("    </div>");// page-content-wrapper
		// END CONTENT
		out.println("  </div>"); // page-container
		// END CONTAINER

		if (lVoltar) {
			out.println("<script>");
			out.println("  try {");
			out.println("    " + ( mTitleMenu.getJSVoltar().trim().length() > 0 ? mTitleMenu.getJSVoltar() : "voltar()" ) + ";");
			out.println("  } ");
			out.println("  catch(e) {}");
			out.println("</script>");
		}
		out.println("</form>");

		try {
			out.println("<script>try { document._frm.txt" + controls[0].getField() + ".focus(); } catch(e) {}</script>");
		}
		catch (Exception ex) {
		}

		// BEGIN JAVASCRIPTS (LOAD JAVASCRIPTS AT BOTTOM, THIS WILL REDUCE PAGE LOAD TIME)
		// BEGIN CORE PLUGINS
		out.println("  <!--[if lt IE 9]>");
		out.println("  <script src='metronic/global/plugins/excanvas.min.js'></script>");
		out.println("  <script src='metronic/global/plugins/respond.min.js'></script>  ");
		out.println("  <![endif]-->");
		out.println("  <script src='metronic/global/plugins/jquery-migrate-1.2.1.min.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/global/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js' type='text/javascript'></script> \n");
		out.println("  <script src='metronic/global/plugins/bootstrap/js/bootstrap.js' type='text/javascript'></script>");
		// out.println(" <script src='metronic/global/plugins/bootstrap/js/bootstrap2-typeahead.min.js' type='text/javascript'></script>");
		// out.println(" <script src='metronic/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/global/plugins/jquery.blockui.min.js' type='text/javascript'></script>");
		// out.println(" <script src='metronic/global/plugins/jquery.cokie.min.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/global/plugins/uniform/jquery.uniform.min.js' type='text/javascript'></script>");
		// END CORE PLUGINS

		// BEGIN PAGE LEVEL PLUGINS
		out.println("  <script type='text/javascript' src='metronic/global/plugins/select2/select2.js'></script>");
		if (mPrintUploadForm) {
			out.println("  <script type='text/javascript' src='metronic/global/plugins/bootstrap-fileinput/bootstrap-fileinput.js'></script>");
		}
		// out.println(" <script type='text/javascript' src='metronic/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/data-tables/jquery.dataTables.js'></script>");
		// out.println(" <script type='text/javascript' src='metronic/global/plugins/data-tables/tabletools/js/dataTables.tableTools.min.js'></script>");
		// out.println(" <script type='text/javascript' src='metronic/global/plugins/data-tables/fnReloadAjax.js'></script>");
		// out.println(" <script type='text/javascript' src='metronic/global/plugins/data-tables/fnSetFilteringDelay.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/data-tables/DT_bootstrap.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/bootbox/bootbox.min.js'></script>");
		// out.println(" <script type='text/javascript' src='metronic/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker-twitter.min.js'></script>");
		// out.println(" <script type='text/javascript' src='metronic/global/plugins/jquery-nestable/jquery.nestable.js'></script>\n");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js'></script>");
		// END PAGE LEVEL PLUGINS

		out.println(mPageFooterTags);

		// BEGIN PAGE LEVEL SCRIPTS
		out.println("  <script src='metronic/global/scripts/metronic.js' type='text/javascript'></script>");
		out.println("  <script src='metronic/admin/layout/scripts/layout.js' type='text/javascript'></script>");
		// out.println(" <script src='metronic/global/scripts/datatable.js'></script>");
		out.println("  <script>");
		out.println("    jQuery(document).ready(function() {");
		out.println("      Metronic.init();");
		out.println("      Layout.init();");
		out.println("      $('.tooltips').tooltip();");
		out.println("      try { top.closeChargerPage(window.name.replace('id', 'fr')); } catch(e) {}");

		if (mMandatoryIDs != null && mMandatoryIDs.size() > 0) {
			for (String id : mMandatoryIDs) {
				out.println("      highlightMandatory('" + id + "');");
			}
		}
		out.println("      setPageHeight();");
		out.println("    });");
		out.println("    $('form').submit(function() {");
		out.println("      $('body').find('*').off();");
		out.println("      for (var x in jQuery.cache) {");
		out.println("        delete jQuery.cache[x];");
		out.println("      }");
		out.println("    });");
		out.println("    function setPageHeight() {");
		out.println("      setTimeout( function() {");
		out.println("        if ( parent.$('#_div_isg_page_content_overlay').height() < $('body').height() ) {");
		out.println("          parent.$('#_div_isg_page_content_overlay').height( $('body').height() );");
		out.println("          parent.$('#_iframe_isg_page_content_overlay').height( $('body').height() );");
		out.println("        }");
		out.println("      }, 500);");
		out.println("    }");

		out.println(mPageFooterScripts);

		out.println("  </script>");
		// END JAVASCRIPTS

		out.println("  </body>");
		// END BODY

		out.println("</html>");

		finalize();
		out.flush();
	}

	public void finalize() {
	}

	@SuppressWarnings( "rawtypes" )
	public final Enumeration getParameterNames() {
		if (mIsUpload) {
			return mUpRequest.getParameterNames();
		}
		else {
			return mRequest.getParameterNames();
		}
	}

	public final String getParameter(String pParameter) {
		return getParameter(pParameter, "");
	}

	public final String getParameter(String pParameter, String pDefault) {
		if (mIsUpload) {
			return Database.verifyNull(mUpRequest.getParameter(pParameter), pDefault).trim();
		}
		else {
			return Database.verifyNull(mRequest.getParameter(pParameter), pDefault).trim();
		}
	}

	public final String[] getParameterValues(String parameter) {
		if (mIsUpload) {
			return Database.verifyNullArray(mUpRequest.getParameterValues(parameter));
		}
		else {
			return Database.verifyNullArray(mRequest.getParameterValues(parameter));
		}
	}

	public final CosUploadFile getFile(String parameter) {
		if (mIsUpload) {
			return (CosUploadFile) mUpRequest.getFiles().get(parameter);
		}
		else {
			return null;
		}
	}

	public final String getRequestURL() {
		return Database.verifyNull(mRequest.getRequestURL()).trim();
	}

	public final String getErrorMessage() {
		return mStrError;
	}

	public final void setErrorMessage(String lError) {
		mStrError = lError;
	}

	protected void getOldValues() {
		mRequestOnError = true;
		for (int i = 0; i < mCountControls; i++) {
			controls[i].setValue(getParameter(controls[i].getControl()));
		}
	}

	private void getBasicInformation() {
		mFiltro = getParameter("filtro");
		mBaseFilter = getParameter("baseFilter");
		mSentido = getParameter("sentido");
		mOrdem = getParameter("ordem");
		mAcao = getParameter("acao");
		mPagina = getParameter("pagina");
		mFuncao = getParameter("funcao");
		mAcaoOld = getParameter("acao_old");
		mQuickSearch = getParameter("quick_search");
		mExtraParameters = getParameter("extraparameters");

		if (mAcaoOld.equals("")) {
			mAcaoOld = mAcao;
		}
	}

	private void retrieveInformation() {
		int i;

		rs = show();
		if (rs != null) {
			try {
				if (rs.next()) {
					for (i = 0; i < mCountControls; i++) {
						controls[i].setValue(Database.verifyNull(rs.getObject(controls[i].getField())).trim());
					}
				}
			}
			catch (SQLException e) {
				Utils.printSystemError("controls.setValue", e.getMessage());
			}
			finally {
				Database.closeObject(rs);
			}
		}
	}

	public boolean verifyAccess(HttpServletResponse response, String pID) {
		if (mVerifySecurityFunction) {
			return mUser.direitoFuncao(mSecurityFunction, pID);
		}
		else {
			return true;
		}
	}

	public final void initFormUpload() {
		mPrintUploadForm = true;
		try {
			if (MultipartFormDataRequest.isMultipartFormData(mRequest)) {
				mUpRequest = new MultipartFormDataRequest(mRequest);
				mIsUpload = true;
			}
		}
		catch (Exception e) {
			Utils.printSystemError("MultipartFormDataRequest", e.getMessage());
		}
	}

	public void addControl(String pName, String pField) {
		controls[mCountControls] = new DataControl();
		controls[mCountControls].setControl(pName);
		controls[mCountControls].setField(pField);
		mCountControls += 1;
	}

	public final String getValueByName(String pName) {
		int i = 0;
		String lreturn = "";

		while (i < controls.length) {
			if (controls[i].getControl().equals(pName)) {
				lreturn = controls[i].getValue();
				i = controls.length;
			}
			else {
				i += 1;
			}
		}

		return lreturn;
	}

	public final String getValueByField(String pField) {
		int i = 0;
		String lreturn = "";

		while (i < controls.length) {
			if (controls[i].getField().equals(pField)) {
				lreturn = controls[i].getValue();
				i = controls.length;
			}
			else {
				i += 1;
			}
		}
		return lreturn;
	}

	public final void setHideButtons(boolean pValue) {
		if (pValue) {
			mNoNew = true;
			mNoDelete = true;
			mNoSave = true;
		}
	}

	public void setSidebarLayer(boolean pSidebarLayer) {
		this.mSidebarLayer = pSidebarLayer;
	}

	public void setPageHeaderTags(String pPageHeaderTags) {
		mPageHeaderTags = pPageHeaderTags;
	}

	public void setPageFooterTags(String pPageFooterTags) {
		mPageFooterTags = pPageFooterTags;
	}

	public void setPageFooterScripts(String pPageFooterScripts) {
		mPageFooterScripts = pPageFooterScripts;
	}

	public final void addPrimary(String pName, String pValue) {
		addPrimary(pName, pValue, null, "");
	}

	public void addPrimary(String pName, String pValue, DataType pType, String pField) {
		boolean lBlnEncontrou = false;

		for (int i = 0; i < primarys.length; i++) {
			if (primarys[i] != null && primarys[i].getControl().equals(pName)) {
				primarys[i].setControl(pName);
				primarys[i].setValue(pValue);
				primarys[i].setType(pType);
				primarys[i].setField(pField);
				lBlnEncontrou = true;
				break;
			}
		}

		if (!lBlnEncontrou) {
			primarys[mCountPrimarys] = new DataControl();
			primarys[mCountPrimarys].setControl(pName);
			primarys[mCountPrimarys].setValue(pValue);
			primarys[mCountPrimarys].setType(pType);
			primarys[mCountPrimarys].setField(pField);
			mCountPrimarys += 1;
		}
	}

	public final void writeMsg(String pMsg, PrintWriter out) {
		out.println("<script>jQuery(document).ready(function() { top.bootbox.alert('" + Utils.replaceAll(pMsg, "\\n", "<br/>") + "'); });</script>");
	}

	public void addSidebarItem(String pAcao, String pLink, String pLabel, String pIcone, String pToolTip, String pActionJS, String pQuantidade) {
		mSidebarMenu.addMenuItem(pAcao, pLink, pLabel, pIcone, pToolTip, pActionJS, pQuantidade, false);
	}

	public void addSeparador(String pLabel, String pQuantidade) {
		mSidebarMenu.addMenuItem("", "", pLabel, "", "", "", pQuantidade, true);
	}

	private void buildTitleMenu(PrintWriter out) {
		mTitleMenu.setReadOnly(mReadOnly);

		if (!mSecurityFunction.equals("")) {
			mTitleMenu.setShowNovo(mUser.direitoFuncao(mSecurityFunction, "C"));
			mTitleMenu.setShowSalvar(mAcao.startsWith("ins") ? mUser.direitoFuncao(mSecurityFunction, "C") : mUser.direitoFuncao(mSecurityFunction, "U"));
			mTitleMenu.setShowExcluir(mUser.direitoFuncao(mSecurityFunction, "D"));
		}

		if (mNoDelete) {
			mTitleMenu.setShowExcluir(!mNoDelete);
		}
		if (mNoNew) {
			mTitleMenu.setShowNovo(!mNoNew);
		}
		if (mNoBack) {
			mTitleMenu.setShowVoltar(!mNoBack);
		}
		if (mNoSave) {
			mTitleMenu.setShowSalvar(!mNoSave);
		}
		mTitleMenu.setShowLocalizar(false);
		mTitleMenu.setConfirmExcluir(true);
		mTitleMenu.setConfirmNovo(true);
		mTitleMenu.setConfirmSalvar(true);
		mTitleMenu.setTitle(mTitle);
		if (mNewWindow.length() > 0) {
			String lPrimarys = "";
			for (int i = 0; i < mCountPrimarys; i++) {
				lPrimarys += "&" + primarys[i].getControl() + "=" + Database.verifyNull(primarys[i].getValue());
			}
			mAcaoNew = "goLink('" + mNewWindow + "?acao=ins" + lPrimarys + "', 'jan_" + mNewWindow.replaceAll(".", "_") + "', 550, 450, 'scrollbars=yes')";
		}
		else {
			mAcaoNew = "facao('ins')";
		}
		mTitleMenu.setJSNovo(mAcaoNew);

		mTitleMenu.setContentFullWidth(mSidebarMenu.getItensCount() <= 0);

		menuConfig(mTitleMenu);
		if (mShowMenu) {
			mTitleMenu.ShowMenu(out);
		}
	}

	private void buildScripts(PrintWriter out) {
		out.println("<script src='funcoes.jsp'></script>");
		out.println("<script>");
		out.println("function facao(pacao){");
		if (mPrintUploadForm) {
			out.println("  switch (pacao) {");
			out.println("    case 'ins':");
			out.println("      document._frm_multipart.acao.value = pacao;");
			out.println("      break;");
			out.println("    case 'salvar':");
			out.println("      if ((document._frm_multipart.acao.value == 'ins') || (document._frm_multipart.acao.value == 'insert') || (document._frm_multipart.acao.value == 'delete')) {");
			out.println("        document._frm_multipart.acao.value = 'insert';");
			out.println("      }");
			out.println("      else {");
			out.println("        document._frm_multipart.acao.value = 'update';");
			out.println("      }");
			out.println("      break;");
			out.println("     case 'delete':");
			out.println("       document._frm_multipart.acao.value = pacao;");
			out.println("       break;");
			out.println("  }");
			out.println("  " + mActionJSForm);
			out.println("  try { top.showChargerPage(window.name.replace('id', 'fr')); } catch(e) {}");
			out.println("  document._frm_multipart.submit();");
		}
		else {
			out.println("  switch (pacao) {");
			out.println("    case 'ins':");
			out.println("      document._frm.acao.value = pacao;");
			out.println("      break;");
			out.println("    case 'salvar':");
			out.println("      if ((document._frm.acao.value == 'ins') || (document._frm.acao.value == 'insert') || (document._frm.acao.value == 'delete')) {");
			out.println("        document._frm.acao.value = 'insert';");
			out.println("      }");
			out.println("      else {");
			out.println("        document._frm.acao.value = 'update';");
			out.println("      }");
			out.println("      break;");
			out.println("    case 'delete':");
			out.println("      document._frm.acao.value = pacao;");
			out.println("      break;");
			out.println("  }");
			out.println(mActionJSForm);
			out.println("  try { top.showChargerPage(window.name.replace('id', 'fr')); } catch(e) {}");
			out.println("  document._frm.submit();");
		}
		out.println("}");

		out.println("function voltar() {");
		out.println("  document._frm.action = '" + mReturnPage + "';");
		out.println("  document._frm.target = '_self';");
		out.println("  document._frm.method = 'post';");
		out.println("  document._frm.submit();");
		out.println("}");

		out.println("function highlightMandatory(elementId) {");
		out.println("  try { $('#' + elementId).closest('.input-icon').children('i').addClass('fa-warning'); } catch(e) {}");
		out.println("  try { $('#' + elementId).closest('.form-group').addClass('has-error'); } catch(e) {}");
		out.println("}");

		out.println("</script>");

		/*
		 * FORMATO DE CAMPO EM QUE PODE SER APLICADO O ICONE DE ALERTA DE PREENCHIMENTO (function highlightMandatory) <div class='form-group'> <label class='control-label col-md-3'>Name <span class='required'> * </span></label> <div class='col-md-4'> <div class='input-icon right'> <i class='fa' data-original-title='please write a valid email'></i> <input type='text' class='form-control' name='name'/> </div> </div> </div>
		 */

	}

	public String printPageLoader() {
		StringBuffer lHtml = new StringBuffer();

		lHtml.append("<script>\n");
		lHtml.append("  try { top.showChargerPage(window.name.replace('id', 'fr')); } catch(e) {} \n");
		lHtml.append("</script> \n");

		return lHtml.toString();

	}

	public void setPageWidth(int pPageWidth) {
		this.mPageWidth = pPageWidth;
	}

	public HttpServletRequest getRequest() {
		return mRequest;
	}

	public MultipartFormDataRequest getRequestUpload() {
		return mUpRequest;
	}

	public void initialize(HttpServletResponse response) throws ServletException {
	}

	public void extraParameters(HttpServletResponse response) {
	}

	public void getPrimaries(HttpServletResponse response) {
	}

	public void setToolBars() {
	}

	public void getParameters(HttpServletResponse response) {
	}

	public boolean validate(PrintWriter out) {
		return true;
	}

	public boolean validate(HttpServletResponse response, PrintWriter out) {
		return true;
	}

	public boolean validateDelete(PrintWriter out) {
		return true;
	}

	public void insert(HttpServletResponse response) {
	}

	public void update(HttpServletResponse response) {
	}

	public boolean delete(HttpServletResponse response) {
		return true;
	}

	public void setTitle() {
	}

	public void setAction() {
	}

	public void transactionBegin(Connection pConn) {
	}

	public void transactionCommit(Connection pConn) {
	}

	public ResultSet show() {
		return null;
	}

	public void addControls() {
	}

	public void menuConfig(TitleMenu pTitleMenu) {
	}

	public void sidebarConfig(SidebarMenu pSidebarMenu) {
	}

	public void script(PrintWriter out) {
	}

	public void html(PrintWriter out) throws InputMaxLengthException {
	}

	public void setPublicSession() {
		mPublicSession = false;

		mSessionName = "";
		mLangParameter = "";
	}
}
