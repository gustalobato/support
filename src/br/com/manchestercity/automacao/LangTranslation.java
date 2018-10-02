
package br.com.manchestercity.automacao;

import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.automacao.FrameworkDefaults.TableRow;

public class LangTranslation extends PageContent {

	private static final long serialVersionUID = 3415967107234330705L;

	String language;

	public void initialize(HttpServletResponse response) {
		super.mShowMenu = true;
		super.mVerifySecurityFunction = false;
		super.mSecurityFunction = "cad_traducao_termo";
		super.mVerifySecurityFunction = true;
		language = User.DEFAULT_LANGUAGE;

		super.setPageWidth(95);
	}

	public void getPrimaries(HttpServletResponse response) {
		language = getParameter("language", User.DEFAULT_LANGUAGE);
		addPrimary("language", language);

		this.setToolBars();
	}

	public void setTitle() {
		mTitle = mUser.getTermo("IDIOMA") + ": <strong>" + mUser.getTermo(language) + "</strong>";
	}

	public void setAction() {
		super.mAction = "langtranslation";
	}

	public void menuConfig(TitleMenu titleMenu) {
		titleMenu.setShowAllButtons(false);
		titleMenu.setShowVoltar(false);

		titleMenu.addExtraButton("_button_down", mUser.getTermo("DOWNLOAD"), "exportProps()", SystemIcons.ICON_DOWNLOAD, DataList.FAROL_BLUE_STEEL, false);
		titleMenu.addExtraButton("_button_load", mUser.getTermo("CARREGAR"), "translation()", SystemIcons.ICON_REFRESH, DataList.FAROL_YELLOW_GOLD, true);
	}

	public void script(PrintWriter out) {
		out.println("  function highlightProp(property) {");
		out.println("    if ($('#_value_' + property).val().toString() !== $('#_value_' + property).data('alt').toString()) {");
		out.println("      $('#_value_' + property).closest('td').removeClass('success');");
		out.println("      $('#_value_' + property).closest('td').removeClass('danger');");
		out.println("      $('#_value_' + property).closest('td').addClass('warning');");
		out.println("      setTimeout(\"$('#_value_\" + property + \"').closest('td').removeClass('warning');\", 7500);");
		out.println("    }");
		out.println("    else {");
		out.println("      $('#_value_' + property).closest('td').removeClass('warning');");
		out.println("      $('#_value_' + property).closest('td').removeClass('danger');");
		out.println("      $('#_value_' + property).closest('td').addClass('success');");
		out.println("      setTimeout(\"$('#_value_\" + property + \"').closest('td').removeClass('success');\", 7500);");
		out.println("    }");
		out.println("  }");
		out.println("  function validateProp(property) {");
		out.println("    if ( $('#prp').val() ) {");
		out.println("      $('#act').val('validate');");
		out.println("      submitForm();");
		out.println("    }");
		out.println("    else {");
		out.println("      $('#_field_ins_prp').closest('.form-group').addClass('has-error');");
		out.println("    }");
		out.println("  }");
		out.println("  function insertProp(property) {");
		out.println("    $('#act').val('insert');");
		out.println("    submitForm();");
		out.println("  }");
		out.println("  function updateProp(property) {");
		out.println("    $('#act').val('update');");
		out.println("    $('#prp').val(property);");
		out.println("    $('#val').val( $('#_value_' + property).val() );");
		out.println("    submitForm();");
		out.println("    $('#prp').val( $('#_field_ins_prp').val() );");
		out.println("    $('#val').val( $('#_field_ins_val').val() );");
		out.println("  }");
		out.println("  function deleteProp(property) {");
		out.println("    top.bootbox.confirm('" + mUser.getTermo("INFO_CONFIRMAOPERACAO") + "', function(result) {");
		out.println("      if (result) {");
		out.println("        $('#act').val('delete');");
		out.println("        $('#prp').val(property);");
		out.println("        submitForm();");
		out.println("      }");
		out.println("    });");
		out.println("  }");
		out.println("  function exportProps() {");
		out.println("    $('#act').val('export');");
		out.println("    submitForm();");
		out.println("    closeChargerPage();");
		out.println("  }");
		out.println("  function translation() {");
		out.println("    $('#act').val('reload');");
		out.println("    submitForm();");
		out.println("  }");
		out.println("  function submitForm() {");
		out.println("    $('#_frm').prop('action', 'langajax');");
		out.println("    $('#_frm').prop('target', '_frame_aux');");
		out.println("    $('#_frm').submit();");
		out.println("    $('#_frm').prop('action', '" + mAction + "');");
		out.println("    $('#_frm').prop('target', '_self');");
		out.println("    $('#act').val('');");
		out.println("    showChargerPage();");
		out.println("  }");
	}

	public void html(PrintWriter out) throws InputMaxLengthException {
		dataTable.setDataTable("_table_props", "table-condensed", "");

		TableRow row = defaults.new TableRow();
		row.addCell(mUser.getTermo("TERMO"), "text-left", "vertical-align: middle; width: 20%;");
		row.addCell(mUser.getTermo("VALOR"), "text-left", "vertical-align: middle; width: 80%;");
		row.addCell("&nbsp;", "text-center", "vertical-align: middle; width: 20px;");

		dataTable.addRow(row, true);

		String termo = "";
		String valor = "";

		out.println("                  <input type='hidden' name='act' id='act' value=''>\n");
		out.println("                  <input type='hidden' name='prp' id='prp' value=''>\n");
		out.println("                  <input type='hidden' name='val' id='val' value=''>\n");
		if (mUser.direitoFuncao("cad_traducao_termo", "C")) {
			out.println("                  <div class='row'>");
			out.println("                    <div class='col-md-3'>");
			out.println("                      <div class='form-group'>");
			out.println("                        <div class='input-group input-icon'>");
			out.println("                          <span class='input-group-addon'>" + SystemIcons.ICON_GEAR + "</span>");
			out.println("                          <input class='form-control' type='text' value='' placeholder='" + mUser.getTermo("TERMO") + "' onkeyup=\"$('#prp').val( this.value );\" onblur=\"$('#prp').val( this.value );\" id='_field_ins_prp' />");
			out.println("                        </div>");
			out.println("                      </div>");
			out.println("                    </div>");
			out.println("                    <div class='col-md-9'>");
			out.println("                      <div class='form-group'>");
			out.println("                        <div class='input-group input-icon'>");
			out.println("                          <span class='input-group-addon'>" + SystemIcons.ICON_FILE_TEXT_O + "</span>");
			out.println("                          <input class='form-control' type='text' value='' placeholder='" + mUser.getTermo("VALOR") + "' onkeyup=\"$('#val').val( this.value );\" onblur=\"$('#val').val( this.value );\" id='_field_ins_val' />");
			out.println("                          <div class='input-group-btn'>");
			out.println("                            <button type='button' class='btn green' onclick=\"validateProp();\"> " + SystemIcons.ICON_PLUS_CIRCLE + " " + mUser.getTermo("SALVAR") + " </button>");
			out.println("                          </div>");
			out.println("                        </div>");
			out.println("                      </div>");
			out.println("                    </div>");
			out.println("                  </div>");
		}
		String languageFile = language.toLowerCase();

		@SuppressWarnings( "rawtypes" )
		Enumeration propNames = LangManipulation.getLanguageTranslations(languageFile + ".properties");
		while (propNames.hasMoreElements()) {
			termo = (String) propNames.nextElement();
			valor = LangManipulation.getLanguageTranslation(languageFile + ".properties", termo);

			row = defaults.new TableRow();
			row.addCell(termo, "", "vertical-align: middle; ");
			row.addCell("<input type='text' class='form-control input-sm ini-prop' value='" + valor + "' id='_value_" + termo + "' name='_value_" + termo + "' data-alt='" + valor + "' onblur=\"updateProp('" + termo + "');\" " + ( !mUser.direitoFuncao("cad_traducao_termo", "U") ? "readonly=true" : "" ) + " />", "", "vertical-align: middle; ");
			row.addCell(termo.startsWith("format") ? "&nbsp;" : FrameworkDefaults.printButton("_excl_" + termo, "", SystemIcons.ICON_X, "btn-sm red-thunderbird", "", ( mUser.direitoFuncao("cad_traducao_termo", "D") ? "deleteProp('" + termo + "');" : "" ), !mUser.direitoFuncao("cad_traducao_termo", "D")), "text-center", "vertical-align: middle; ");

			dataTable.addRow(row, false);
		}

		out.println("                  <div class='row'>");
		out.println("                    <div class='col-md-12'>");
		out.println(dataTable.printDataTable());
		out.println("                    </div>");
		out.println("                  </div>");
		out.println("                  <iframe src='about:blank' name='_frame_aux' id='_frame_aux' style='display: none;'></iframe>");
	}
}
