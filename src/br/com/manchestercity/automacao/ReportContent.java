
package br.com.manchestercity.automacao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.SystemConfig;
import br.com.manchestercity.automacao.FrameworkDefaults.MessageType;

public abstract class ReportContent extends HttpServlet {

	private static final long serialVersionUID = -8876883534814874382L;

	private static final String CONTENT_TYPE = "text/html";

	protected User mUser;
	protected Utils mUtil;
	protected Database lcdb;
	protected Connection mConn;

	protected HttpServletRequest request;

	protected String securityFunction = "";

	private boolean showHeader = true;
	private boolean showPrintReport = true;
	private boolean showPrintPDF = true;
	private boolean showPrintEXCEL = true;
	private boolean showFilterDescription = true;

	private String appURL;
	private String reportAction = "";
	private String reportTitle = "";

	private String jsShow = "submitReport();";
	private String jsPrint = "printReport();";
	private String jsPDF = "printPDF();";
	private String jsEXCEL = "printEXCEL();";

	protected boolean isShow = false;
	protected boolean isPDF = false;
	protected boolean isEXCEL = false;

	protected ReportFilter filter;
	protected ReportFilterField field;

	protected TitleMenu titleMenu;

	// protected String lCD_EMPGC = "";
	// protected String lTP_EMPGC_AGRUP = "";

	// LISTA CONTENDO OS NOMES DOS ATRIBUTOS (GUARDAM O SVG) NO SERVLETCONTEXT PARA OS GRÁFICOS GERADOS
	private ArrayList<String> chartContextNames;

	private boolean callSubmit = false;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;

		mUser = User.getSession(this.request, response);
		if (mUser == null) {
			return;
		}

		mUtil = new Utils(mUser);
		mUtil.noCache(response, this.request);

		lcdb = new Database(mUser);
		mConn = lcdb.openConnection();

		chartContextNames = new ArrayList<String>();

		filter = new ReportFilter(request, mUser);

		appURL = "";
		try {
			appURL = mUtil.getUrlApp();
			appURL = appURL.replaceFirst("https|HTTPS", "http");
		}
		catch (Exception ex) {
			Utils.printSystemError("appURL", ex.getMessage());
		}

		response.setHeader("Content-Type", "text/html;charset=" + mUser.getUserCharset());

		setReportAction();
		setReportTitle();

		try {

			requestBasicInfo();

			verifyAccess(response, "R");

			initReportFilter();
			filter.requestValues();

			// PEGA DO REQUEST OS PARÂMETROS NECESSÁRIOS DE CADA RELATÓRIO ESPECÍFICO
			getReportParameters();

			if (isPDF) {
				try {
					response.reset();
					response.setContentType("application/csv");
					response.setHeader("Content-Disposition", "attachment; filename=" + SystemConfig.getSystemName() + "-report.pdf;");

					StringBuffer doc = new StringBuffer();
					doc.append("<html>");
					doc.append("<head>");
					doc.append(printBaseStyles());
					doc.append("</head>");
					doc.append("<body>");
					if (showHeader) {
						doc.append(showReportHeader(showFilterDescription));
					}
					doc.append(showReportResults());
					doc.append("</body>");
					doc.append("</html>");

					InputStream input = new ByteArrayInputStream(doc.toString().getBytes());
					ViewPDF.convert(input, response.getOutputStream());

					// REMOVE DO CONTEXTO OS ATRIBUTOS CRIADOS PARA GUARDAR AS STRINGS COM OS SGVS DOS GRÁFICOS
					for (int position = 0; position < this.chartContextNames.size(); position++) {
						request.getServletContext().removeAttribute(Database.verifyNull(this.chartContextNames.get(position)));
					}
				}
				catch (Exception e) {
					Utils.printSystemError("Print PDF", e.getMessage());
				}
			}
			else if (isEXCEL) {
				try {
					ServletOutputStream out;

					StringBuffer doc = new StringBuffer();
					doc.append("<html>");
					doc.append("<head>");
					doc.append(printBaseStyles());
					doc.append("</head>");
					doc.append("<body>");
					doc.append(showReportResults());
					doc.append("</body>");
					doc.append("</html>");

					byte[] xls = doc.toString().replaceAll("<td", "<td style='border-width: 1px; padding: 1px; border-style: inset; border-color: silver; background-color: white; -moz-border-radius: ;'").replaceAll("<th", "<td style='border-width: 1px; padding: 1px; border-style: inset; border-color: silver; background-color: #EEEEEE; -moz-border-radius: ;'").getBytes();

					// CONTENT TYPE
					response.setContentType("application/msexcel");

					// OUTPUT HEADER
					response.setHeader("Content-Disposition", "attachment; filename=" + SystemConfig.getSystemName() + "-report.xls;");
					response.setIntHeader("Content-length", xls.length);

					out = response.getOutputStream();
					out.flush();
					out.write(xls);
				}
				catch (Exception e) {
					Utils.printSystemError("Print EXCEL", e.getMessage());
				}
			}
			else {
				response.setContentType(CONTENT_TYPE);

				PrintWriter out = response.getWriter();
				if (isShow) {
					// EXIBE O MENU SUPERIOR COM OS BOTÕES NECESSÁRIOS
					out.print(menuConfig());
					if (showHeader) {
						// EXIBE A TABELA COM O LOGO DO CLIENTE E ALGUMAS INFORMAÇÕES BÁSICAS
						out.print(showReportHeader(showFilterDescription));
						out.print("<br/><br/>");
					}

					// EXIBE OS RESULTADOS GERADOS AO SOLICITAR O SUBMIT DO FORMULÁRIO
					out.print(showReportResults());
				}
				else {
					// GERA OS CÓDIGOS CSS, JS E HTML DO RELATÓRIO
					printReportContent(out);

					// GERA OS CÓDIGOS JAVASCRIPT ESSENCIAIS
					printBaseJS(out);

					// GERA OS CÓDIGOS JAVASCRIPT ESPECÍFICOS DE CADA RELATÓRIO
					printReportJS(out);
				}
			}

		}
		catch (Exception ex) {
			Utils.printSystemError("Print Report", ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			Database.closeObject(mConn);
		}
	}

	// =====================================================
	// ============= IMPLEMENTAÇÃO OBRIGATÓRIA =============
	// =====================================================

	protected abstract void setReportAction();

	protected abstract void setReportTitle();

	protected abstract void getReportParameters();

	protected abstract void initReportFilter();

	protected abstract void printReportJS(PrintWriter out);

	protected abstract String createQuerySQL();

	protected abstract String showReportResults();

	// =====================================================
	// =============== MÉTODOS PRÉ DEFINIDOS ===============
	// =====================================================

	protected void addChartContextName(String contextName) {
		this.chartContextNames.add(contextName);
	}

	public ArrayList<String> getChartContextNames() {
		return chartContextNames;
	}

	public void setSecurityFunction(String securityFunction) {
		this.securityFunction = securityFunction;
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	public void setShowPrintReport(boolean showPrintReport) {
		this.showPrintReport = showPrintReport;
	}

	public void setShowPrintPDF(boolean showPrintPDF) {
		this.showPrintPDF = showPrintPDF;
	}

	public void setShowPrintEXCEL(boolean showPrintEXCEL) {
		this.showPrintEXCEL = showPrintEXCEL;
	}

	public void setShowFilterDescription(boolean showFilterDescription) {
		this.showFilterDescription = showFilterDescription;
	}

	protected String getAppURL() {
		if (appURL == null) {
			return "";
		}
		return appURL;
	}

	public void setReportAction(String reportAction) {
		this.reportAction = reportAction;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public void setJsPrint(String jsPrint) {
		this.jsPrint = jsPrint;
	}

	public void setJsPDF(String jsPDF) {
		this.jsPDF = jsPDF;
	}

	public void setJsEXCEL(String jsEXCEL) {
		this.jsEXCEL = jsEXCEL;
	}

	private String menuConfig() {
		titleMenu = new TitleMenu(mUser);
		titleMenu.setTitle(reportTitle);
		titleMenu.setShowAllButtons(false);
		titleMenu.setShowVoltar(true);
		titleMenu.setContentFullWidth(false);

		if (isShow) {
			if (showPrintReport) {
				titleMenu.addExtraButton("_button_print", mUser.getTermo("IMPRIMIR"), jsPrint, SystemIcons.ICON_PRINT, DataList.FAROL_BLUE_STEEL, true);
			}
			if (showPrintEXCEL) {
				titleMenu.addExtraButton("_button_excel", mUser.getTermo("EXCEL"), jsEXCEL, SystemIcons.ICON_TABLE, DataList.FAROL_GREEN, true);
			}
			if (showPrintPDF) {
				titleMenu.addExtraButton("_button_PDF", mUser.getTermo("PDF"), jsPDF, SystemIcons.ICON_FILE_O, DataList.FAROL_RED_THUNDERBIRD, true);
			}

			if (callSubmit) {
				// titleMenu.setJSVoltar(!IniManipulation.getProperty("new_layout").equals("") ? "try { parent.hideSidebarLayer(); } catch(e) {}" : "try { self.close(); } catch(e) {}");
				titleMenu.setJSVoltar("try { parent.hideSidebarLayer(); } catch(e) {}");
			}
		}
		else {
			// titleMenu.setJSVoltar(!IniManipulation.getProperty("new_layout").equals("") ? "top.closeFrame(window.name);" : "try { self.close(); } catch(e) {}");
			titleMenu.setJSVoltar("top.closeFrame(window.name);");
			titleMenu.setLabelVoltar(mUser.getTermo("FECHAR"));
			titleMenu.addExtraButton("_button_clean", mUser.getTermo("LIMPARFILTRO"), filter.getClearFunctionName() + "();", SystemIcons.ICON_UNDO, DataList.FAROL_RED_THUNDERBIRD, true);
			titleMenu.addExtraButton("_button_show", mUser.getTermo("GERARRELATORIO"), jsShow, SystemIcons.ICON_FILE_TEXT_O, DataList.FAROL_BLUE_STEEL, true);
		}

		return titleMenu.ShowMenu();
	}

	private void requestBasicInfo() {
		isShow = !getParameter("show").equals("");
		isPDF = !getParameter("pdf").equals("");
		isEXCEL = !getParameter("excel").equals("");

		// lCD_EMPGC = getParameter("_field_CD_EMPGC");
		// lTP_EMPGC_AGRUP = getParameter("_field_TP_EMPGC_AGRUP");

		callSubmit = getParameter("_call_submit").equals("S");
	}

	protected boolean verifyAccess(HttpServletResponse response, String CRUD) {
		if (!securityFunction.equals("")) {
			if (!mUser.direitoFuncao(securityFunction, CRUD)) {
				User.acessoNegado(response);
			}
		}
		return true;
	}

	public String showReportHeader(boolean showFilterDescription) {
		StringBuffer header = new StringBuffer();

		header.append("<table class='table'>\n");
		header.append("  <tbody>\n");
		header.append("    <tr>\n");
		header.append("      <td class='text-center col-md-2' style='vertical-align: middle;'>\n");
		header.append("        <img src='isg/images/logos/system-logo-small.png' style='width: 120; height: 50; border: 0;' vspace='3'>\n");
		header.append("      </td>\n");
		header.append("      <td class='text-center col-md-8' style='vertical-align: middle;'>\n");
		header.append("        <h3>");
		header.append(reportTitle);
		header.append("</h3>\n");
		header.append("      </td>\n");
		header.append("      <td class='text-center col-md-2' style='vertical-align: middle;'>\n");
		header.append("        <h4>");
		header.append(lcdb.getActualDate(DateType.DATE));
		header.append("</h4>\n");
		header.append("      </td>\n");
		header.append("    </tr>\n");
		if (showFilterDescription) {
			header.append("    <tr>\n");
			header.append("      <td class='text-center' colspan='3'>\n");
			header.append("        <h6>");
			header.append(filter.printDescription());
			header.append("</h2>\n");
			header.append("      </td>\n");
			header.append("    </tr>\n");
		}
		header.append("  </tbody>\n");
		header.append("</table>\n");

		return header.toString();
	}

	public void printReportStyles(PrintWriter out) {
		// Specific styles for each report
	}

	private void printReportContent(PrintWriter out) {
		out.println("<!DOCTYPE html>");
		out.println("<!--[if IE 8]> <html lang='en' class='ie8 no-js'> <![endif]-->");
		out.println("<!--[if IE 9]> <html lang='en' class='ie9 no-js'> <![endif]-->");
		out.println("<!--[if !IE]><!--> <html lang='en' class='no-js'> <!--<![endif]-->\n");
		out.println("<head>");
		out.println("  <meta http-equiv='Content-Type' content='text/html; pageEncoding=" + mUser.getUserCharset() + " charset=" + mUser.getUserCharset() + "'>");
		out.println("  <title>" + SystemConfig.getSystemName() + " | The Citizens Brasil</title>");
		out.println("  <meta http-equiv='X-UA-Compatible' content='IE=edge'>");
		out.println("  <meta content='width=device-width, initial-scale=1.0' name='viewport'/>");
		out.println("  <meta content='The Citizens Brasil' name='description'/>");
		out.println("  <meta content='The Citizens Brasil' name='author'/>\n");

		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/open-sans/css/open-sans.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/font-awesome/css/font-awesome.min.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap/css/bootstrap.min.css'/>");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/uniform/css/uniform.default.css' />\n");

		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/select2/select2.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/datetimepicker.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-markdown/css/bootstrap-markdown.min.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/data-tables/DT_bootstrap.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/jquery-nestable/jquery.nestable.css' />\n");

		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/css/components.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/global/css/plugins.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/layout.css' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/themes/" + ( IniManipulation.getProperty(IniManipulation.LAYOUT_THEME).equalsIgnoreCase("light") ? "isg-light.css" : "isg-default.css" ) + "' />");
		out.println("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/custom.css' />\n");

		this.printReportStyles(out);

		out.println("  <link rel='shortcut icon' href='favicon.ico'/>");
		out.println("  <script src='funcoes.jsp'></script>");
		out.println("  <script src='metronic/global/plugins/jquery-1.11.0.min.js' type='text/javascript'></script>");
		out.println("</head>");

		out.println("<body bgcolor='#FFFFFF' leftmargin='0' bottonmargin='0' rightmargin='0' topmargin='0'" + ( getParameter("layer").equals("") ? "" : " class='page-full-width'" ) + ">");
		out.println("  <form id='_frm' name='_frm' action='" + reportAction + "' target='_self' method='POST' style='margin: 0; padding: 0; border: 0;'>");
		out.println("    <input type='hidden' value='' id='_hidden_pdf' name='pdf'>");
		out.println("    <input type='hidden' value='' id='_hidden_show' name='show' />");
		out.println("    <input type='hidden' value='' id='_hidden_excel' name='excel'>");
		// IDENTIFICA A NECESSIDADE DE GERAR O INNER HTML DEPOIS DA PERDA DE SESSAO
		out.println("    <input type='hidden' value='' id='_inner_report' name='_inner_report'>");
		// IDENTIFICA A NECESSIDADE DE GERAR O INNER HTML LOGO APÓS A CHAMADA DO RELATORIO
		out.println("    <input type='hidden' value='" + ( callSubmit ? "S" : "" ) + "' id='_call_submit' name='_call_submit' />");

		out.println("    <div class='page-container'>");

		if (getParameter("layer").equals("")) {
			out.println("      <div class='page-sidebar navbar-collapse collapse'>");
			out.println("        <ul class='page-sidebar-menu' data-auto-scroll='true' data-slide-speed='200'>");
			out.println("          <li class='menu-report-filter start active'>");
			out.println("            <a href='javascript:;' onclick='showReportFilter();'>");
			out.println("              " + SystemIcons.ICON_SEARCH + "<span class='title'> " + mUser.getTermo("FILTRO") + " </span><span class='selected'></span>");
			out.println("            </a>");
			out.println("          </li>");
			out.println("          <li class='menu-report-results'>");
			out.println("            <a href='javascript:;' class='disabled-link'>");
			out.println("              <span class='disable-target'>" + SystemIcons.ICON_LIST + "<span class='title'> " + mUser.getTermo("RESULTADO") + " </span></span><span class='selected'></span>");
			out.println("            </a>");
			out.println("          </li>");
			out.println("        </ul>");
			out.println("      </div>");
		}

		out.println("      <div class='page-content-wrapper'>");
		out.println("        <div class='page-content' id='_div_isg_page_content'>");

		out.println("          <div class='theme-panel hidden-md hidden-lg'>");
		out.println("            <div class='side-toggler responsive-toggler hidden-md hidden-lg' data-toggle='collapse' data-target='.navbar-collapse' style='float: left; position: relative;'></div>");
		out.println("            <div class='side-toggler-close responsive-toggler hidden-md hidden-lg' data-toggle='collapse' data-target='.navbar-collapse'></div>");
		out.println("          </div>");

		out.println(menuConfig());

		// ROW DIV
		out.println("          <div class='row'>");
		out.println("            <div style='width: 60%; margin-left: auto; margin-right: auto;'>");
		out.println("              <h2 class='page-title hidden-md hidden-lg'> &nbsp; </h2>");
		// FORM DIV
		out.println("              <div class='form col-md-12'>");
		out.println("                <div class='horizontal-form'>");
		out.println(filter.printFields());
		out.println("                </div>");
		out.println("              </div>");
		// END FORM DIV
		out.println("            </div>");
		out.println("          </div>");
		// END ROW DIV
		out.println("        </div>"); // _div_isg_page_content
		out.println("      </div>"); // page-content-wrapper
		out.println("    </div>"); // page-container
		// END PAGE CONTAINER DIV

		out.println("  </form>");

		out.println("  <div id='_div_results_content' style='width: 100%; z-index: 0; display: none;'></div>");
		out.println("  <div id='_div_isg_page_content_overlay' style='display: none;'></div>");

		out.println("  <!--[if lt IE 9]>");
		out.println("  <script src='metronic/global/plugins/excanvas.min.js'></script>");
		out.println("  <script src='metronic/global/plugins/respond.min.js'></script>  ");
		out.println("  <![endif]-->");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/jquery-migrate-1.2.1.min.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js'></script> \n");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/bootstrap/js/bootstrap.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/jquery.blockui.min.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/uniform/jquery.uniform.min.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/select2/select2.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/data-tables/jquery.dataTables.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/data-tables/DT_bootstrap.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/bootbox/bootbox.min.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/global/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js'></script>");

		// PAGE LEVEL SCRIPTS
		out.println("  <script type='text/javascript' src='metronic/global/scripts/metronic.js'></script>");
		out.println("  <script type='text/javascript' src='metronic/admin/layout/scripts/layout.js'></script>");

		out.println("  <script>");
		out.println("    jQuery(document).ready(function() {");
		out.println("      Metronic.init();");
		out.println("      Layout.init();");

		if (callSubmit) {
			out.println("      submitReport();");
		}

		out.println("      try { top.closeChargerPage(window.name.replace('id', 'fr')); } catch(e) {}");
		out.println("      setPageHeight();");
		out.println("    });");
		out.println("  </script>");

		out.println("<script>");
		out.println("  function openRelatorioExtra(url) {");
		out.println("    var trg = document.form_ajax_aux.target;");
		out.println("    var act = document.form_ajax_aux.action;");
		out.println("    document.form_ajax_aux.target = '_layer_relat';");
		out.println("    document.form_ajax_aux.action = url;\n");
		out.println("    document.form_ajax_aux.submit();");
		out.println("    document.form_ajax_aux.target = trg;");
		out.println("    document.form_ajax_aux.action = act;");
		out.println("    $('#_modal_extra_report').modal('show');");
		out.println("  }");
		out.println("</script>");
		out.println(FrameworkDefaults.printModal("_modal_extra_report", mUser.getTermo("DETALHES"), "<iframe frameborder='0' id='_layer_relat' name='_layer_relat' width='100%' height='350px'></iframe>", SystemIcons.ICON_BAN, mUser.getTermo("FECHAR"), "$('#_modal_extra_report').modal('hide');", SystemIcons.ICON_CHECK_CIRCLE, mUser.getTermo("OK"), "$('#_modal_extra_report').modal('hide');", "", "", false));

		out.println("</body>");
		out.println("</html>");
	}

	private void printBaseJS(PrintWriter out) {
		out.println("<script>");
		out.println("  $('#_frm').submit(function() {");
		out.println("    if ( $('#_hidden_pdf').val() || $('#_hidden_excel').val() ) {");
		out.println("      return true;");
		out.println("    }");
		out.println("    Metronic.scrollTop();");
		out.println("    Metronic.blockUI( { target: '#_div_results_content', boxed: true, message: '" + mUser.getTermo("CARREGANDO") + "...' } );");
		out.println("    $('.menu-report-results > a').click( function() { showReportResults(); });");
		out.println("    showReportResults();");
		out.println("    $.ajax({");
		out.println("      contentType: \"application/x-www-form-urlencoded;charset=" + mUser.getUserCharset() + "\",");
		out.println("      data: $(this).serialize(),");
		out.println("      type: $(this).attr('method'),");
		out.println("      url: $(this).attr('action'),");
		out.println("      cache: false,");
		out.println("      success: function(response) {");
		out.println("        processResponse('_div_results_content', response);");
		out.println("        Metronic.initAjax();");
		out.println("        Metronic.unblockUI('#_div_results_content');");
		out.println("      },");
		out.println("      error: function (xhr, ajaxOptions, thrownError) {");
		out.println("        pageContentBody.html(\"" + Utils.replaceAll(FrameworkDefaults.printMessageBlock("_error_message", "<p><strong>" + mUser.getTermo("ATENCAO") + "</strong></p><br/>" + mUser.getTermo("MSGERRODISPLAY"), "text-center", "", MessageType.DANGER, false), "\n", "") + "\");");
		out.println("        Metronic.unblockUI('#_div_results_content');");
		out.println("      }");
		out.println("    });");
		out.println("    return false;");
		out.println("  });");

		out.println("  function submitReport() {");
		out.println("    $('#_hidden_pdf').val('');");
		out.println("    $('#_hidden_show').val('true');");
		out.println("    $('#_hidden_excel').val('');");
		out.println("    $('#_inner_report').val('true');");
		out.println("    $('#_frm').submit();");
		out.println("  }");

		out.println("  function printReport() {");
		out.println("    var contents = $('#_div_results_content').html();");
		out.println("    var printFrame = $('<iframe />');");
		out.println("    printFrame[0].name = 'printFrame';");
		out.println("    printFrame.css({ 'position': 'absolute', 'top': '-1000000px' });");
		out.println("    $('body').append(printFrame);");
		out.println("    var frameDoc = printFrame[0].contentWindow ? printFrame[0].contentWindow : printFrame[0].contentDocument.document ? printFrame[0].contentDocument.document : printFrame[0].contentDocument;");
		out.println("    frameDoc.document.open();");
		// CREATE A NEW HTML DOCUMENT
		out.println("    frameDoc.document.write('<html><head><title>" + this.reportTitle + "</title>');");
		out.println("    frameDoc.document.write('</head><body>');");
		// APPEND THE EXTERNAL CSS FILE
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/open-sans/css/open-sans.css' />\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/font-awesome/css/font-awesome.min.css' />\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap/css/bootstrap.min.css' />\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/uniform/css/uniform.default.css' />\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/select2/select2.css' />\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css'/>\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css'/>\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/datetimepicker.css'/>\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-markdown/css/bootstrap-markdown.min.css'/>\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/data-tables/DT_bootstrap.css'/>\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/plugins/jquery-nestable/jquery.nestable.css'/>\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/css/components.css' />\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/global/css/plugins.css' />\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/layout.css' />\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/themes/" + ( IniManipulation.getProperty(IniManipulation.LAYOUT_THEME).equalsIgnoreCase("light") ? "isg-light.css" : "isg-default.css" ) + "' />\");");
		out.println("    frameDoc.document.write(\"<link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/custom.css' />\");");
		out.println("    frameDoc.document.write(\"<style> @page { size: landscape; margin: 0.5cm; } </style>\");");
		// APPEND THE DIV CONTENTS
		out.println("    frameDoc.document.write(contents);");
		out.println("    frameDoc.document.write('</body></html>');");
		out.println("    frameDoc.document.close();");
		out.println("    setTimeout(function () {");
		out.println("      window.frames['printFrame'].focus();");
		out.println("      window.frames['printFrame'].print();");
		out.println("      printFrame.remove();");
		out.println("    }, 500);");
		out.println("  }");

		out.println("  function printPDF() {");
		out.println("    $('#_hidden_pdf').val('true');");
		out.println("    $('#_hidden_show').val('');");
		out.println("    $('#_hidden_excel').val('');");
		out.println("    $('#_inner_report').val('');");
		out.println("    $('#_frm').submit();");
		out.println("  }");

		out.println("  function printEXCEL() {");
		out.println("    $('#_hidden_pdf').val('');");
		out.println("    $('#_hidden_show').val('');");
		out.println("    $('#_hidden_excel').val('true');");
		out.println("    $('#_inner_report').val('');");
		out.println("    $('#_frm').submit();");
		out.println("  }");

		out.println("  function voltar() {");
		out.println("    showReportFilter();");
		out.println("  }");

		out.println("  function showReportFilter() {");
		out.println("    Metronic.scrollTop();");
		out.println("    var $pageContent = $('.page-content');");
		out.println("    var $resultContent = $('#_div_results_content');");
		out.println("    $resultContent.css({ position: 'relative' });");
		out.println("    $resultContent.zIndex(0);");
		out.println("    $resultContent.slideUp(350);");
		out.println("    $pageContent.slideDown(350);");
		out.println("    $('.menu-report-filter').addClass('start active');");
		out.println("    $('.menu-report-results').removeClass('start active');");
		out.println("  }");

		out.println("  function showReportResults() {");
		out.println("    Metronic.scrollTop();");
		out.println("    var $pageContent = $('.page-content');");
		out.println("    var $resultContent = $('#_div_results_content');");
		out.println("    $resultContent.css({ position: 'absolute', top: $pageContent.position().top, left: $pageContent.position().left, marginTop: $pageContent.css('margin-top'), marginRight: $pageContent.css('margin-right'), marginBottom: $pageContent.css('margin-bottom'), marginLeft: $pageContent.css('margin-left'), paddingTop: $pageContent.css('padding-top'), paddingRight: $pageContent.css('padding-right'), paddingBottom: $pageContent.css('padding-bottom'), paddingLeft: $pageContent.css('padding-left'), backgroundColor: '#FFFFFF' });");
		out.println("    $resultContent.width( $pageContent.width() );");
		// out.println(" $resultContent.height( $pageContent.height() );");
		out.println("    $resultContent.zIndex( $pageContent.zIndex() + 1000 );");
		out.println("    $pageContent.slideUp(350);");
		out.println("    $resultContent.slideDown(350);");
		out.println("    $('.menu-report-filter').removeClass('start active');");
		out.println("    $('.menu-report-results').addClass('start active');");
		out.println("    $('.menu-report-results > a').removeClass('disabled-link');");
		out.println("    $('.menu-report-results > span').removeClass('disable-target');");
		out.println("  }");

		out.println("    function setPageHeight() {");
		out.println("      setTimeout( function() {");
		out.println("        if ( parent.$('#_div_isg_page_content_overlay').height() < $('body').height() ) {");
		out.println("          parent.$('#_div_isg_page_content_overlay').height( $('body').height() );");
		out.println("          parent.$('#_iframe_isg_page_content_overlay').height( $('body').height() );");
		out.println("        }");
		out.println("      }, 500);");
		out.println("    }");

		if (getParameter("_call_show").equals("S")) {

		}
		out.println("</script>");
	}

	private String printBaseStyles() {
		StringBuffer out = new StringBuffer();

		out.append("<style>\n");
		out.append("  @page {");
		out.append("    size: landscape;");
		out.append("    margin: 0.5cm;");
		out.append("  }");
		if (isPDF) {
			out.append("  body {");
			out.append("    zoom:50%;");
			out.append("    -moz-transform: scale(0.5);");
			out.append("    -webkit-transform: scale(0.5);");
			out.append("  }");
		}
		out.append("  .table {\n");
		out.append("    border-collapse: collapse !important;\n");
		out.append("    clear: both;\n");
		out.append("    margin-bottom: 6px !important;\n");
		out.append("    max-width: none !important;\n");
		out.append("  }\n");
		out.append("  .table-bordered th,\n");
		out.append("  .table-bordered td {\n");
		out.append("    border: 1px solid #DDDDDD !important;\n");
		out.append("  }\n");
		out.append("  .table > thead > tr > th,\n");
		out.append("  .table > tbody > tr > th,\n");
		out.append("  .table > tfoot > tr > th,\n");
		out.append("  .table > thead > tr > td,\n");
		out.append("  .table > tbody > tr > td,\n");
		out.append("  .table > tfoot > tr > td {\n");
		out.append("    padding: 8px;\n");
		out.append("    line-height: 1.42857143;\n");
		out.append("    vertical-align: top;\n");
		out.append("    border-top: 1px solid #ddd;\n");
		out.append("  }\n");
		out.append("  .table > thead > tr > th {\n");
		out.append("    vertical-align: bottom;\n");
		out.append("    border-bottom: 2px solid #ddd;\n");
		out.append("  }\n");
		out.append("  .table > tbody + tbody {\n");
		out.append("    border-top: 2px solid #DDDDDD;\n");
		out.append("  }\n");
		out.append("  .table .table {\n");
		out.append("    background-color: #FFFFFF;\n");
		out.append("  }\n");
		out.append("  .table-condensed > thead > tr > th,\n");
		out.append("  .table-condensed > tbody > tr > th,\n");
		out.append("  .table-condensed > tfoot > tr > th,\n");
		out.append("  .table-condensed > thead > tr > td,\n");
		out.append("  .table-condensed > tbody > tr > td,\n");
		out.append("  .table-condensed > tfoot > tr > td {\n");
		out.append("    padding: 5px;\n");
		out.append("  }\n");
		out.append("  .table-bordered {\n");
		out.append("    border: 1px solid #DDDDDD;\n");
		out.append("  }\n");
		out.append("  .table-bordered > thead > tr > th,\n");
		out.append("  .table-bordered > tbody > tr > th,\n");
		out.append("  .table-bordered > tfoot > tr > th,\n");
		out.append("  .table-bordered > thead > tr > td,\n");
		out.append("  .table-bordered > tbody > tr > td,\n");
		out.append("  .table-bordered > tfoot > tr > td {\n");
		out.append("    border: 1px solid #DDDDDD;\n");
		out.append("  }\n");
		out.append("  .table-bordered > thead > tr > th,\n");
		out.append("  .table-bordered > thead > tr > td {\n");
		out.append("    border-bottom-width: 2px;\n");
		out.append("  }\n");
		out.append("  .table-striped > tbody > tr:nth-child(odd) > td,\n");
		out.append("  .table-striped > tbody > tr:nth-child(odd) > th {\n");
		out.append("    background-color: #F9F9F9;\n");
		out.append("  }\n");
		out.append("  .text-left {\n");
		out.append("    text-align: left;\n");
		out.append("  }\n");
		out.append("  .text-right {\n");
		out.append("    text-align: right;\n");
		out.append("  }\n");
		out.append("  .text-center {\n");
		out.append("    text-align: center;\n");
		out.append("  }\n");
		out.append("  .text-justify {\n");
		out.append("    text-align: justify;\n");
		out.append("  }\n");
		out.append("</style>\n");

		return out.toString();
	}

	protected String getParameter(String parameter) {
		return getParameter(parameter, "");
	}

	protected String getParameter(String parameter, String defaultValue) {
		return Database.verifyNull(request.getParameter(parameter), defaultValue);
	}

	protected String[] getParameterValues(String parameter) {
		return Database.verifyNullArray(request.getParameterValues(parameter));
	}

	protected String getParameterValuesSeparated(String parameter) {
		StringBuffer params = new StringBuffer();

		String[] values = Database.verifyNullArray(request.getParameterValues(parameter));
		for (String value : values) {
			params.append(( params.length() > 0 ? "," : "" ) + value);
		}

		return params.toString();
	}

	protected LinkedHashMap<String, String> sortHashMapByValues(Map<String, String> map) {
		List<String> mapKeys = new ArrayList<String>(map.keySet());
		List<String> mapValues = new ArrayList<String>(map.values());

		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap<String, String> sortedMap = new LinkedHashMap<String, String>();

		Iterator<String> valueIt = mapValues.iterator();

		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator<String> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = map.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					map.remove(key);
					mapKeys.remove(key);
					sortedMap.put(key.toString(), val.toString());
					break;
				}
			}
		}

		return sortedMap;
	}

	protected String[] sort(String labels, String values) {
		Map<String, String> mapLabelsValues = new HashMap<String, String>();

		String[] lbl = labels.split(",");
		String[] vls = values.split(",");

		for (int i = 0; i < vls.length; i++) {
			mapLabelsValues.put(vls[i], lbl[i]);
		}

		for (Entry<String, String> entry : mapLabelsValues.entrySet()) {
			if (mUser.getTermo(entry.getValue()).contains("NAO ENCONTRADO ####)")) {
				mapLabelsValues.put(entry.getKey(), entry.getValue());
			}
			else {
				mapLabelsValues.put(entry.getKey(), mUser.getTermo(entry.getValue()));
			}
		}

		Map<String, String> lhm = sortHashMapByValues(mapLabelsValues);

		String newValues = lhm.keySet().toString().replace("[", "").replace("]", "").replace(" ", "");
		String newlabels = lhm.values().toString().replace("[", "").replace("]", "");

		return new String[] { newValues, newlabels };
	}
}
