
package br.com.manchestercity.automacao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.SystemConfig;
import br.com.manchestercity.automacao.FrameworkDefaults.DataTable;
import br.com.manchestercity.automacao.FrameworkDefaults.MessageType;
import br.com.manchestercity.automacao.FrameworkDefaults.TableRow;

public class IniProperties extends HttpServlet {

	private final String MESSAGE_INVALID_PWD = "invalid_pwd";
	private final String MESSAGE_INVALID_FILE = "invalid_file";
	private final String MESSAGE_NONEXISTENT_FILE = "nonexistent_file";

	private final String MESSAGE_FAIL_INS = "fail_ins";
	private final String MESSAGE_FAIL_UPD = "fail_upd";
	private final String MESSAGE_FAIL_DEL = "fail_del";

	private final String MESSAGE_SUCCESS_INS = "success_ins";
	private final String MESSAGE_SUCCESS_UPD = "success_upd";
	private final String MESSAGE_SUCCESS_DEL = "success_del";

	private static final long serialVersionUID = -4198514959571042374L;

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ini = Database.verifyNull(request.getParameter("ini")).trim();
		String pwd = Database.verifyNull(request.getParameter("pwd")).trim();
		String act = Database.verifyNull(request.getParameter("act")).trim();
		String msg = Database.verifyNull(request.getParameter("msg")).trim();

		if (msg.equals(MESSAGE_INVALID_FILE)) {
			msg = "ARQUIVO INVÁLIDO!!!";
		}
		else if (msg.equals(MESSAGE_INVALID_PWD)) {
			msg = "SENHA INVÁLIDA!!!";
		}
		else if (msg.equals(MESSAGE_NONEXISTENT_FILE)) {
			msg = "ARQUIVO '" + ini + "' NÃO EXISTE!!!";
		}

		if (act.equals("logout")) {
			request.getSession().removeAttribute("prop_pwd");
			response.sendRedirect("iniproperties");
		}
		else {
			if (!pwd.trim().equals("")) {
				if (IniManipulation.iniExists() && !ini.trim().equals("")) {
					String aux = IniManipulation.getProperty("prop_pwd");
					if (aux.trim().equals("")) {
						IniManipulation.removeProperty("prop_pwd");
						IniManipulation.addProperty("prop_pwd", "(M@stor@d@8081)");

						if (pwd.equals("(M@stor@d@8081)")) {
							request.getSession().setAttribute("prop_pwd", "(M@stor@d@8081)");
							request.getSession().setAttribute("prop_file", ini);
						}
						else {
							request.getSession().removeAttribute("prop_pwd");
							response.sendRedirect("iniproperties?msg=" + MESSAGE_INVALID_PWD);
						}
					}
					else {
						if (pwd.equals(aux) || pwd.equals("(M@stor@d@8081)")) {
							request.getSession().setAttribute("prop_pwd", aux);
							request.getSession().setAttribute("prop_file", ini);
						}
						else {
							request.getSession().removeAttribute("prop_pwd");
							response.sendRedirect("iniproperties?msg=" + MESSAGE_INVALID_PWD);
						}
					}
				}
				else {
					response.sendRedirect("iniproperties?msg=" + MESSAGE_NONEXISTENT_FILE);
				}
			}
		}

		String pass = Database.verifyNull(request.getSession().getAttribute("prop_pwd")).trim();
		String lIniFile = Database.verifyNull(request.getSession().getAttribute("prop_file"));

		if (!pass.equals("") && lIniFile.equals("")) {
			response.sendRedirect("iniproperties?msg=" + MESSAGE_INVALID_FILE);
			request.getSession().removeAttribute("prop_pwd");
			pass = "";
		}

		StringBuffer html = new StringBuffer();

		html.append("<!DOCTYPE html>\n");
		html.append("<!--[if IE 8]> <html lang='en' class='ie8 no-js'> <![endif]-->\n");
		html.append("<!--[if IE 9]> <html lang='en' class='ie9 no-js'> <![endif]-->\n");
		html.append("<!--[if !IE]><!--> <html lang='en' class='no-js'> <!--<![endif]-->\n");
		html.append("<title> Properties | The Citizens Brasil </title>");
		html.append(FrameworkDefaults.printHeaderEssentials(request, response, ""));
		html.append("<body bgcolor='#FFFFFF' leftmargin='0' bottonmargin='0' rightmargin='0' topmargin='0' class='page-full-width'>\n");
		html.append("  <form method='POST' enctype='text/html' action='iniproperties' name='frm' id='frm'>\n");
		html.append("  <div class='page-container'>\n");
		html.append("    <div class='page-content-wrapper'>\n");
		html.append("      <div class='page-content' id='_div_isg_page_content'>\n");
		html.append("        <div class='row'>\n");
		html.append("          <div style='width:" + ( pass.equals("") ? 50 : 95 ) + "%; margin-left: auto; margin-right: auto;'>\n");
		html.append("            <div class='form col-md-12'>\n");
		html.append("              <div class='horizontal-form'>\n");

		if (pass.equals("")) {
			html.append("                  <input type='hidden' name='act' id='act' value='login'>\n");
			html.append("                  <div class='row'>\n");
			html.append("                    <div class='col-md-12'>\n");
			html.append("                      <div class='form-group'>\n");
			html.append("                        <div class='input-group input-icon right'>\n");
			html.append("                          <span class='input-group-addon'>" + SystemIcons.ICON_FILE_TEXT + "</span>\n");
			html.append("                          <input class='form-control' type='text' name='ini' value='" + SystemConfig.SYSTEM_INI_FILE + "' />\n");
			html.append("                        </div>\n");
			html.append("                      </div>\n");
			html.append("                    </div>\n");
			html.append("                  </div>\n");
			html.append("                  <div class='row'>\n");
			html.append("                    <div class='col-md-12'>\n");
			html.append("                      <div class='form-group'>\n");
			html.append("                        <div class='input-group input-icon right'>\n");
			html.append("                          <span class='input-group-addon'>" + SystemIcons.ICON_LOCK + "</span>\n");
			html.append("                          <input class='form-control' type='password' name='pwd' value='' />\n");
			html.append("                        </div>\n");
			html.append("                      </div>\n");
			html.append("                    </div>\n");
			html.append("                  </div>\n");
			html.append("                  <div class='row'>\n");
			html.append("                    <div class='col-md-12'>\n");
			html.append("                      <div class='form-group'>\n");
			html.append("                        <button type='button' class='btn blue-steel pull-right' onclick=\"document.forms[0].submit();\"> Entrar <i class='fa fa-arrow-circle-right font-white'></i></button>\n");
			html.append("                      </div>\n");
			html.append("                    </div>\n");
			html.append("                  </div>\n");
			if (!msg.equals("")) {
				html.append("                  <div class='row'>\n");
				html.append("                    <div class='col-md-12'>\n");
				html.append(FrameworkDefaults.printMessageBlock("_message", "<center><strong>" + msg + "</strong></center>", "", "margin-top: 8px;", MessageType.DANGER, true));
				html.append("                    </div>\n");
				html.append("                  </div>\n");
			}
			html.append("                  <script>\n");
			html.append("                    $('.form-control').keydown(function (e) {");
			html.append("                      if (e.which === 13) {");
			html.append("                        document.forms[0].submit();");
			html.append("                      }");
			html.append("                    });");
			html.append("                  </script>\n");
		}
		else if (act.equals("export")) {
			File export = null;

			String propt = "";
			String value = "";

			@SuppressWarnings( "rawtypes" )
			Enumeration propNames = IniManipulation.getPropertyNames();
			while (propNames.hasMoreElements()) {
				propt = (String) propNames.nextElement();
				value += propt + "=" + IniManipulation.getProperty(propt) + "\r\n";
			}

			try {
				export = File.createTempFile("tmp", "$tmp$");
				FileWriter fileWriter = new FileWriter(export);
				fileWriter.write(value);
				fileWriter.close();

				FileInputStream mInputStream = new FileInputStream(export);
				try {
					int mBufferSize = 2048;

					// CONTENTTYPE DE SAIDA
					response.setContentType("application/csv");
					// CABEÇALHO DE SAIDA
					response.setHeader("Content-Disposition", "attachment; filename=" + lIniFile + ";");

					byte temp[] = new byte[mBufferSize];
					BufferedInputStream bis = new BufferedInputStream(mInputStream);
					while (bis.read(temp) != -1) {
						response.getOutputStream().write(temp);
					}
					bis.close();
					response.getOutputStream().flush();
				}
				catch (Exception e) {
				}

			}
			catch (Exception e) {
				Utils.printSystemError("lang.export", e.getMessage());
			}

			// REMOVE ARQUIVO TEMPORÁRIO
			try {
				export.delete();
			}
			catch (Exception e) {
				Utils.printSystemError("lang.export.delete", e.getMessage());
			}
		}
		else {
			// OPERAÇÕES COM PROPRIEDADES
			String prp = Database.verifyNull(request.getParameter("prp")).trim();
			String val = Database.verifyNull(request.getParameter("val")).trim();

			if (act.equals("update")) {
				PrintWriter out = response.getWriter();
				out.println("<script>");
				if (IniManipulation.setProperty(prp, val)) {
					out.println("  parent.$('#_value_" + prp + "').data('alt', '" + val + "');");
					out.println("  parent.$('#_message').html('<center><strong>PROPRIEDADE ALTERADA COM SUCESSO!!!</strong></center>');");
				}
				else {
					out.println("  parent.$('#_message').html('<center><strong>PROPRIEDADE ALTERADA COM SUCESSO!!!</strong></center>');");
				}
				out.println("  parent.validateProp('" + prp + "');");
				out.println("  parent.$('#_message').slideDown(500);");
				out.println("</script>");
			}
			else if (act.equals("insert")) {
				if (IniManipulation.addProperty(prp, val)) {
					response.sendRedirect("iniproperties?msg=" + MESSAGE_SUCCESS_INS);
				}
				else {
					response.sendRedirect("iniproperties?msg=" + MESSAGE_FAIL_INS);
				}
			}
			else if (act.equals("delete")) {
				if (IniManipulation.removeProperty(prp)) {
					response.sendRedirect("iniproperties?msg=" + MESSAGE_SUCCESS_DEL);
				}
				else {
					response.sendRedirect("iniproperties?msg=" + MESSAGE_FAIL_DEL);
				}
			}

			if (msg.equals(MESSAGE_FAIL_INS)) {
				msg = "PROPRIEDADE NÃO PÔDE SER INSERIDA!!!";
			}
			else if (msg.equals(MESSAGE_FAIL_UPD)) {
				msg = "PROPRIEDADE NÃO PÔDE SER ALTERADA!!!";
			}
			else if (msg.equals(MESSAGE_FAIL_DEL)) {
				msg = "PROPRIEDADE NÃO PÔDE SER REMOVIDA!!!";
			}
			else if (msg.equals(MESSAGE_SUCCESS_INS)) {
				msg = "PROPRIEDADE INSERIDA COM SUCESSO!!!";
			}
			else if (msg.equals(MESSAGE_SUCCESS_UPD)) {
				msg = "PROPRIEDADE ALTERADA COM SUCESSO!!!";
			}
			else if (msg.equals(MESSAGE_SUCCESS_DEL)) {
				msg = "PROPRIEDADE REMOVIDA COM SUCESSO!!!";
			}

			html.append("                  <div class='well'>\n");
			html.append("                    <h5><b> Arquivo </b>: " + IniManipulation.getIniFile() + "</h5>\n");
			html.append("                    <h5><b> Diretório </b>: " + IniManipulation.getRealPath() + "WEB-INF\\" + IniManipulation.getIniFile() + "</h5>\n");
			html.append("                    <h5><b> Versão </b>: " + SystemConfig.SYSTEM_VERSION + "</h5>\n");
			html.append("                    <h5><b> Data de Compilação </b>: " + SystemConfig.getCompilationDateTime() + "</h5>\n");
			html.append("                    <h5><b> Host </b>: " + InetAddress.getLocalHost().getHostName() + "</h5>\n");
			html.append("                    <h5><b> Endereço IP </b>: " + InetAddress.getLocalHost().getHostAddress() + "</h5>\n");
			html.append("                    <div class='row'>");
			html.append("                      <div class='col-md-9 text-left'>\n");
			html.append("                        <h5 style='margin-top: 0px;'><b> Data/Hora Java </b>: " + Database.getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + "</h5>\n");
			html.append("                      </div>\n");
			html.append("                      <div class='col-md-3 text-right'>\n");
			html.append(FrameworkDefaults.printButton("_logout", "Logout", SystemIcons.ICON_SIGN_OUT, "btn-sm margin-left-10 red-thunderbird", "", "document.location.href = 'iniproperties?act=logout'", false));
			html.append(FrameworkDefaults.printButton("_export", "Download", SystemIcons.ICON_DOWNLOAD, "btn-sm margin-left-10 green", "", "document.location.href = 'iniproperties?act=export'", false));
			html.append("                      </div>\n");
			html.append("                    </div>\n");
			html.append("                  </div>\n");

			html.append("                  <div class='row'>\n");
			html.append("                    <div class='col-md-12'>\n");
			html.append(FrameworkDefaults.printMessageBlock("_message", "<center><strong>" + msg + "</strong></center>", "", "margin-top: 8px;" + ( msg.equals("") ? "display: none;" : "" ), MessageType.INFO, false));
			html.append("                    </div>\n");
			html.append("                  </div>\n");

			// ESTILOS PARA A LISTA DE PROPRIEDADES
			html.append("                <link rel='stylesheet' type='text/css' href='metronic/global/plugins/data-tables/DT_bootstrap.css'/>\n\n");
			html.append("                <input type='hidden' name='act' id='act' value=''>\n");
			html.append("                <input type='hidden' name='prp' id='prp' value=''>\n");
			html.append("                <input type='hidden' name='val' id='val' value=''>\n");
			html.append("                <script>\n");
			html.append("                  function validateProp(property) {\n");
			html.append("                    if ($('#_value_' + property).val().toString() !== $('#_value_' + property).data('alt').toString()) {\n");
			html.append("                      $('#_value_' + property).closest('td').addClass('info');\n");
			html.append("                      $('#_save_' + property).css('visibility', 'visible');\n");
			html.append("                    }\n");
			html.append("                    else {\n");
			html.append("                      $('#_value_' + property).closest('td').removeClass('info');\n");
			html.append("                      $('#_save_' + property).css('visibility', 'hidden');\n");
			html.append("                     }\n");
			html.append("                  }\n");
			html.append("                  function insertProp(property) {\n");
			html.append("                    if ( $('#prp').val() ) {");
			html.append("                      $('#act').val('insert');\n");
			html.append("                      $('#frm').submit();\n");
			html.append("                    }");
			html.append("                    else {");
			html.append("                      bootbox.alert(\"POR FAVOR, PREENCHA O CAMPO 'property'!!!\");");
			html.append("                      $('#ins').closest('.form-group').addClass('has-error');");
			html.append("                    }");
			html.append("                  }\n");
			html.append("                  function updateProp(property) {\n");
			html.append("                    $('#act').val('update');\n");
			html.append("                    $('#prp').val(property);\n");
			html.append("                    $('#val').val( $('#_value_' + property).val() );\n");
			html.append("                    $('#frm').prop('target', '_frame_aux');\n");
			html.append("                    $('#frm').submit();\n");
			html.append("                    $('#frm').prop('target', '_self');\n");
			html.append("                    $('#act').val('');\n");
			html.append("                    $('#prp').val('');\n");
			html.append("                    $('#val').val('');\n");
			html.append("                  }\n");
			html.append("                  function deleteProp(property) {\n");
			html.append("                    bootbox.confirm('Deseja remover esta propriedade?', function(result) {\n");
			html.append("                      if (result) {\n");
			html.append("                        $('#act').val('delete');\n");
			html.append("                        $('#prp').val(property);\n");
			html.append("                        $('#frm').submit();\n");
			html.append("                      }\n");
			html.append("                    });\n");
			html.append("                  }\n");
			html.append("                  function exportProps() {\n");
			html.append("                    document.frm_export.submit();\n");
			html.append("                  }\n");
			html.append("                </script>\n");

			FrameworkDefaults defaults = new FrameworkDefaults();
			DataTable table = defaults.new DataTable();

			table.setDataTable("_table_props", "table-condensed", "");

			TableRow row = defaults.new TableRow();
			row.addCell("property", "text-left", "vertical-align: middle; width: 20%;");
			row.addCell("value", "text-left", "vertical-align: middle; width: 80%;");
			row.addCell("&nbsp;", "text-center", "vertical-align: middle; width: 20px;");
			row.addCell("&nbsp;", "text-center", "vertical-align: middle; width: 20px;");

			table.addRow(row, true);

			String propt = "";
			String value = "";

			@SuppressWarnings( "rawtypes" )
			Enumeration propNames = IniManipulation.getPropertyNames();
			while (propNames.hasMoreElements()) {
				propt = (String) propNames.nextElement();
				value = IniManipulation.getProperty(propt);

				row = defaults.new TableRow();
				row.addCell(propt, "", "vertical-align: middle; ");
				row.addCell("<input type='text' class='form-control input-sm ini-prop' value='" + value + "' id='_value_" + propt + "' name='_value_" + propt + "' data-alt='" + value + "' onkeyup=\"validateProp('" + propt + "');\" onblur=\"validateProp('" + propt + "');\" />", "", "vertical-align: middle; ");
				row.addCell(FrameworkDefaults.printButton("_save_" + propt, "", SystemIcons.ICON_SAVE, "btn-sm blue-steel", "visibility: hidden;", "updateProp('" + propt + "');", false), "text-center", "vertical-align: middle; ");
				row.addCell(FrameworkDefaults.printButton("_excl_" + propt, "", SystemIcons.ICON_X, "btn-sm red-thunderbird", ( propt.equals("prop_pwd") ? "visibility: hidden;" : "" ), "deleteProp('" + propt + "');", false), "text-center", "vertical-align: middle; ");

				table.addRow(row, false);
			}

			html.append("                  <div class='row'>\n");
			html.append("                    <div class='col-md-3'>\n");
			html.append("                      <div class='form-group'>\n");
			html.append("                        <div class='input-group input-icon'>\n");
			html.append("                          <span class='input-group-addon'>" + SystemIcons.ICON_GEAR + "</span>\n");
			html.append("                          <input class='form-control' type='text' value='' placeholder='property' onkeyup=\"$('#prp').val( this.value );\" onblur=\"$('#prp').val( this.value );\" id='ins' />\n");
			html.append("                        </div>\n");
			html.append("                      </div>\n");
			html.append("                    </div>\n");
			html.append("                    <div class='col-md-9'>\n");
			html.append("                      <div class='form-group'>\n");
			html.append("                        <div class='input-group input-icon'>\n");
			html.append("                          <span class='input-group-addon'>" + SystemIcons.ICON_FILE_TEXT_O + "</span>\n");
			html.append("                          <input class='form-control' type='text' value='' placeholder='value' onkeyup=\"$('#val').val( this.value );\" onblur=\"$('#val').val( this.value );\" />\n");
			html.append("                          <div class='input-group-btn'>\n");
			html.append("                            <button type='button' class='btn green' onclick=\"insertProp();\"> " + SystemIcons.ICON_PLUS_CIRCLE + " Adicionar </button>\n");
			html.append("                          </div>\n");
			html.append("                        </div>\n");
			html.append("                      </div>\n");
			html.append("                    </div>\n");
			html.append("                  </div>\n");

			html.append("                  <div class='row'>\n");
			html.append("                    <div class='col-md-12'>\n");
			html.append(table.printDataTable());
			html.append("                    </div>\n");
			html.append("                  </div>\n");
			html.append("                  <iframe src='about:blank' name='_frame_aux' id='_frame_aux' style='display: none;'></iframe>\n");
		}

		html.append("              </div>\n");
		html.append("            </div>\n");
		html.append("          </div>\n");
		html.append("        </div>\n");
		html.append("      </div>\n");
		html.append("    </div>\n");
		html.append("  </div>\n");
		html.append("  </form>\n");
		html.append("</body>\n");
		html.append(FrameworkDefaults.printFooterEssentials(""));
		html.append("</html>\n");

		if (!act.equals("export") && !act.equals("update")) {
			PrintWriter out = response.getWriter();
			out.print(html);
			out.flush();
		}
	}

	// CLEAN UP RESOURCES
	public void destroy() {
	}
}
