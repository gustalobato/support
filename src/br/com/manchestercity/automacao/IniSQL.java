
package br.com.manchestercity.automacao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.automacao.FrameworkDefaults.MessageType;

public class IniSQL extends HttpServlet {

	private static final long serialVersionUID = -6758487453398323834L;

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String ini = "";
		String pwd = "";

		if (request.getSession() != null) {
			pwd = Database.verifyNull(request.getSession().getAttribute("prop_pwd")).trim();
			ini = Database.verifyNull(request.getSession().getAttribute("prop_file")).trim();
		}

		if (pwd.equals("") || !IniManipulation.getIniFile().equals(ini)) {
			request.getSession().removeAttribute("prop_pwd");
			response.sendRedirect("iniproperties");
		}
		else {
			String act = Database.verifyNull(request.getParameter("act"));

			out.println("<!DOCTYPE html>");
			out.println("<!--[if IE 8]> <html lang='en' class='ie8 no-js'> <![endif]-->");
			out.println("<!--[if IE 9]> <html lang='en' class='ie9 no-js'> <![endif]-->");
			out.println("<!--[if !IE]><!--> <html lang='en' class='no-js'> <!--<![endif]-->");
			out.println("<title> MNU | The Citizens Brasil </title>");
			out.println(FrameworkDefaults.printHeaderEssentials(request, response, ""));
			out.println("<body bgcolor='#FFFFFF' leftmargin='0' bottonmargin='0' rightmargin='0' topmargin='0' class='page-full-width'" + (!act.equals("sql") ? " style='overflow: hidden;'" : "") + ">");
			out.println("  <form method='POST' enctype='text/html' action='inisql?act=sql' name='frm' id='frm'>");
			out.println("  <div class='page-container'>");
			out.println("    <div class='page-content-wrapper'>");
			out.println("      <div class='page-content' id='_div_isg_page_content'" + (!act.equals("sql") ? "" : " style='padding-top: 4px;'") + ">");
			out.println("        <div class='row'>");
			out.println("          <div style='width: 100%; margin-left: auto; margin-right: auto;'>");
			out.println("            <div class='form col-md-12'>");
			out.println("              <div class='horizontal-form'>");

			if (!act.equals("sql")) {
				// SCRIPTS BASE PARA EXECUCAO DO SQL
				out.println("                <style>");
				out.println("                  .nav-tabs > li > a, .nav-pills > li > a {");
				out.println("                    font-size: 12px;");
				out.println("                  }");
				out.println("                  .nav > li > a {");
				out.println("                    padding: 4px 8px;");
				out.println("                  }");
				out.println("                </style>");
				out.println("                <script>");
				out.println("                  var tabCounter = 1;");
				out.println("                  var tabTemplate = \"<li class='active'><a href='#{href}'>#{label} <i class='fa fa-times font-red ui-icon-close' title='Fechar' style='cursor: pointer;'></i> </a></li>\";");
				out.println("                  function appendTab() {");
				out.println("                    var tabs = $('#tabs');");
				out.println("                    tabCounter = tabs.find( 'li' ).length === 0 ? 1 : tabCounter;");
				out.println("                    var label = 'SQL(' + tabCounter + ')';");
				out.println("                    var tabID = 'tabframe' + tabCounter;");
				out.println("                    var li = $( tabTemplate.replace( /#\\{href\\}/g, '#tab-' + tabCounter ).replace( /#\\{label\\}/g, label ) );");
				out.println("                    $('.active').removeClass('active');");
				out.println("                    tabs.find('.ui-tabs-nav').append( li );");
				out.println("                    tabs.append( \"<div id='tab-\" + tabCounter + \"'><iframe name='\" + tabID + \"' id='\" + tabID + \"' style='border: 0px; width: 100%; height: 346px;'></iframe></div>\" );");
				out.println("                    tabs.delegate( 'i.ui-icon-close', 'click', function() {");
				out.println("                      var panelId = $( this ).closest( 'li' ).attr( 'aria-controls' );");
				out.println("                      $( this ).closest( 'li' ).remove();");
				out.println("                      $('#' + panelId ).remove();");
				out.println("                      $('li').first().click();");
				out.println("                      tabs.tabs( 'refresh' );");
				out.println("                    });");
				out.println("                    $('li').click( function() {");
				out.println("                      $('li.active').removeClass('active');");
				out.println("                      $( this ).addClass('active');");
				out.println("                      tabs.tabs( 'refresh' );");
				out.println("                    });");
				out.println("                    $('#tabs').tabs('refresh');");
				out.println("                    $('#tab-' + tabCounter).show();");
				out.println("                    Metronic.blockUI( { boxed: true, target: '#tab-' + tabCounter } );");
				out.println("                    tabCounter++;");
				out.println("                    return tabID;");
				out.println("                  }");
				out.println("                  function getSelectionText() {");
				out.println("                    var text = '';");
				out.println("                    if (window.getSelection) {");
				out.println("                        text = window.getSelection().toString();");
				out.println("                    }");
				out.println("                    else if (document.selection && document.selection.type !== 'Control') {");
				out.println("                      text = document.selection.createRange().text;");
				out.println("                    }");
				out.println("                    return text;");
				out.println("                  }");
				out.println("                  function prepareTab() {");
				out.println("                    var id = appendTab();");
				out.println("                    var range = getSelectionText();");
				out.println("                    $('#sql').val( (range + '') === '' ? $('#txt').val() : range );");
				out.println("                    $('#frm').prop('target', id);");
				out.println("                    $('#frm').submit();");
				out.println("                  }");
				out.println("                  function keyDownEvent(evt){");
				out.println("                    if (evt != null) {");
				out.println("                      if (evt.keyCode == 120 || evt.keyCode == 116) {"); // F9 OU F5
				out.println("                        evt.keyCode = 0;");
				out.println("                        evt.returnValue = false;");
				out.println("                        prepareTab();");
				out.println("                        event.stopPropagation();");
				out.println("                      }");
				out.println("                    }");
				out.println("                  }");
				out.println("                  document.onkeydown = keyDownEvent;");
				out.println("                </script>");
				// ESSENTIAL FIELDS
				out.println("                <div class='row'>");
				out.println("                  <div class='col-md-12'>");
				out.println("                    <div class='form-group'>");
				out.println("                      <textarea name='sql' id='sql' style='display: none;'></textarea>");
				out.println("                      <textarea name='txt' id='txt' style='text-size: 9px;' class='form-control' onkeydown='keyDownEvent(event);' rows='10'></textarea>");
				out.println("                    </div>");
				out.println("                  </div>");
				out.println("                </div>");
				// TABS
				out.println("                <div class='row'>");
				out.println("                  <div class='col-md-12'>");
				out.println("                    <div id='tabs'>");
				out.println("                      <ul class='nav nav-tabs'>");
				out.println(FrameworkDefaults.printButton("_exec", "EXE", SystemIcons.ICON_GEARS, "btn-xs blue-steel pull-right", "", "prepareTab();", false));
				out.println("                      </ul>");
				out.println("                    </div>");
				out.println("                  </div>");
				out.println("                </div>");
				// INIT TABS
				out.println("                <script>");
				out.println("                  $(document).ready( function() {");
				out.println("                    var tabs = $('#tabs').tabs();");
				out.println("                  });");
				out.println("                </script>");
			}
			else {
				User user = (User) request.getSession().getAttribute("user");

				Database lcdb = new Database(user);
				Connection conn = lcdb.openConnection();

				int findSelect = -1;

				if (conn == null) {
					out.println(FrameworkDefaults.printMessageBlock("", lcdb.getLastError(), "text-center", "", MessageType.DANGER, false));
				}
				else {
					ResultSet rs = null;
					Statement stmt = null;

					ResultSetMetaData rsmt = null;

					String sql;
					String sqlFull = Database.verifyNull(request.getParameter("sql")).trim();

					if (sqlFull.trim().equals("")) {
						out.println(FrameworkDefaults.printMessageBlock("", "CONSULTA VAZIA!", "text-center", "", MessageType.DANGER, false));
					}
					else {
						// PROCURANDO FIM DO COMANDO SQL
						String lChar = "";

						String label = "";
						String value = "";

						Clob clob = null;
						Object str = null;

						int i = 1;
						int pos = 0;
						int cont = 0;

						boolean parenteses = false;

						boolean continuar = true;
						while (continuar) {
							continuar = false;
							parenteses = false;

							sql = "";

							while (pos < sqlFull.length()) {
								lChar = sqlFull.substring(pos, pos + 1);

								pos++;

								if (lChar.equals("'")) {
									parenteses = !parenteses;
								}

								if (lChar.equals(";") && !parenteses) {
									continuar = true;
									break;
								}
								else {
									sql += lChar;
								}
							}
							sql = sql.trim();

							if (!sql.trim().equals("")) {
								// EXIBE O SQL EXECUTADO
								out.println(FrameworkDefaults.printMessageBlock("", sql.replaceAll("\\n", "<br/>"), "text-left", "margin-bottom: 6px;", MessageType.INFO, false));

								try {
									stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

									if (sql.trim().substring(0, 6).toUpperCase().indexOf("SELECT") > -1) {
										rs = stmt.executeQuery(sql);
										findSelect = 0;
									}
									else if (sql.trim().substring(0, 6).toUpperCase().indexOf("UPDATE") > -1 || sql.trim().substring(0, 6).toUpperCase().indexOf("DELETE") > -1) {
										if (sql.toLowerCase().indexOf("where") > -1) {
											findSelect = stmt.executeUpdate(sql);
										}
										else {
											throw new SQLException("EXECUÇÃO CANCELADA!!! QUERIES DE <strong>'UPDATE'</strong> OU <strong>'DELETE'</strong> DEVEM POSSUIR A CLÁUSULA <strong>'WHERE'</strong>!!!");
										}
										rs = null;
									}
									else {
										findSelect = stmt.executeUpdate(sql);
										rs = null;
									}
									// EXIBIR MENSAGEM DE SUCESSO
									out.println("<span class='text-success'><strong>COMANDO EXECUTADO COM SUCESSO!</strong></span>");
								}
								catch (SQLException e) {
									// EXIBIR MENSAGEM DE ERRO
									out.println("<p class='text-danger'><strong>" + e.getMessage().toUpperCase() + "</strong></p>");

									Database.closeObject(rs);

									rs = null;
								}

								if (( rs != null ) && ( findSelect > -1 )) {
									try {
										rsmt = rs.getMetaData();
										cont = rsmt.getColumnCount();

										if (cont > 0) {
											i = 1;
											label = "";
											value = "";

											// INICIO DA TABELA E HEADER
											out.println("                  <table class='table table-bordered table-hover table-condensed table-striped' style='font-size: 10px !important; background-color: #FFFFFF;'>");
											out.println("                    <thead>");
											out.println("                      <tr>");

											for (i = 1; i <= cont; i++) {
												label = rsmt.getColumnName(i).toString() + "<br/>";
												label += "<span style='font-size: 8px;'>" + rsmt.getColumnTypeName(i);

												if (rsmt.getColumnDisplaySize(i) > 0) {
													label += "(" + rsmt.getColumnDisplaySize(i);
												}

												if (rsmt.getScale(i) > 0) {
													label += "," + rsmt.getScale(i);
												}

												if (rsmt.getColumnDisplaySize(i) > 0) {
													label += ")";
												}

												if (rsmt.isNullable(i) > 0) {
													label += " NULL";
												}

												label += "</span>";

												out.println("                        <th nowrap>" + label + "</th>");
											}

											out.println("                      </tr>");
											out.println("                    </thead>");
											out.println("                    <tbody>");

											while (rs.next()) {
												str = null;

												out.println("                      <tr>");
												for (i = 1; i <= cont; i++) {
													try {
														value = "";
														str = rs.getObject(i);
														if (str != null) {
															if (str.getClass().getName().toString().equalsIgnoreCase("oracle.sql.clob")) {
																clob = (Clob) str;
																if (clob != null) {
																	value = clob.getSubString(1, Integer.parseInt(String.valueOf(clob.length())));
																}
															}
															else {
																value = str.toString();
															}
														}
														else {
															value = "<span class='text-warning'>NULL</span>";
														}
													}
													catch (Exception ex) {
														value = "<span class='text-danger'>" + ex.getMessage().trim() + "</span>";
													}
													out.println("<td>" + ( value.equals("") ? "&nbsp;" : value.trim() ) + "</td>");
												}
												out.println("                      </tr>");
											}
											out.println("                    </tbody>");
											out.println("                  </table>");
											out.println("                  <script>");
											out.println("                    $(document).ready( function() { parent.Metronic.unblockUI( '#tab-' + window.name.replace('tabframe', '') ); });");
											out.println("                  </script>");
										}
										else {
											// EXIBE MENSAGEM DE ERRO
											out.println("<p class='text-danger'><strong>A CONSULTA NÃO RETORNOU RESULTADO!</strong></p>");
										}
									}
									catch (SQLException e) {
										if (e.getMessage() != "null") {
											// EXIBE MENSAGEM DE ERRO
											out.println("<p class='text-danger'><strong>" + e.getMessage().toUpperCase() + "</strong></p>");
										}
									}
									catch (Exception e) {
										if (e.getMessage() != "null") {
											// EXIBE MENSAGEM DE ERRO
											out.println("<p class='text-danger'><strong>" + e.getMessage().toUpperCase() + "</strong></p>");
										}
									}
									finally {
										Database.closeObject(rs);
									}
								}
								else if (sql.trim().substring(0, 6).toUpperCase().indexOf("SELECT") > -1) {
									// EXIBE MENSAGEM DE ERRO
									out.println("<p class='text-danger'><strong>A CONSULTA NÃO RETORNOU RESULTADO!</strong></p>");
								}
							}
						}
					}
					Database.closeObject(rs);
				}
				Database.closeObject(conn);
			}

			out.println("              </div>");
			out.println("            </div>");
			out.println("          </div>");
			out.println("        </div>");
			out.println("      </div>");
			out.println("    </div>");
			out.println("  </div>");
			out.println("  </form>");
			out.println("</body>");
			out.println(FrameworkDefaults.printFooterEssentials(""));
			out.println("</html>");

			out.flush();
		}
	}

	public void destroy() {
	}
}
