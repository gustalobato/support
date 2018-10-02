
package br.com.manchestercity.view;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.automacao.Crypt;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.InputMaxLengthException;
import br.com.manchestercity.automacao.PageContent;
import br.com.manchestercity.automacao.TitleMenu;
import br.com.manchestercity.automacao.FrameworkDefaults.ColumnWidth;
import br.com.manchestercity.automacao.FrameworkDefaults.InputType;
import br.com.manchestercity.negocio.NEGUSUPA;

public class PagePassword extends PageContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3343409576350653923L;
	String mErrorMessages;
	String lCD_USUPA;
	NEGUSUPA negUSUPA;

	public void initialize(HttpServletResponse response) {
		mErrorMessages = "";
		lCD_USUPA = "";
		negUSUPA = new NEGUSUPA(mUser);
		super.setPageWidth(40);
	}

	public void getPrimaries(HttpServletResponse response) {
		lCD_USUPA = String.valueOf(mUser.getUserCode());
		addPrimary("CD_USUPA", lCD_USUPA);
	}

	public void getParameters(HttpServletResponse response) {
		negUSUPA.setCD_USUPA(lCD_USUPA);
		negUSUPA.setNM_USUPA_SENHA(getParameter("_field_DS_USUPA_NEW"));
	}

	public void setTitle() {
		mTitle = mUser.getTermo("ALTERARSENHA");
	}

	public void setAction() {
		mAction = "pagepassword";
	}

	public void update(HttpServletResponse response) {
		if (negUSUPA.update(mConn, mErro))
			mErrorMessages = mUser.getTermo("SUCCESS_SENHAALTERADA");
	}

	public void menuConfig(TitleMenu pMenu) {
		pMenu.setShowAllButtons(false);
		pMenu.setShowSalvar(true);
	}

	public boolean validate(HttpServletResponse response, PrintWriter out) {
		String lDS_USUPA_PWD = getParameter("_field_DS_USUPA_PWD");
		String lDS_USUPA_NEW = getParameter("_field_DS_USUPA_NEW");
		String lDS_USUPA_RPT = getParameter("_field_DS_USUPA_RPT");
		boolean valid = true;

		if (lDS_USUPA_PWD == null || lDS_USUPA_PWD.equals("")) {
			mMandatoryIDs.add("_field_DS_USUPA_PWD");
			mErrorMessages += Database.validateMessage(mUser.getTermo("VALIDACAMPO"), mUser.getTermo("SENHAATUAL")) + "<br/>";
			valid = false;
		}
		if (lDS_USUPA_NEW == null || lDS_USUPA_NEW.equals("")) {
			mMandatoryIDs.add("_field_DS_USUPA_NEW");
			mErrorMessages += Database.validateMessage(mUser.getTermo("VALIDACAMPO"), mUser.getTermo("NOVASENHA")) + "<br/>";
			valid = false;
		}
		if (lDS_USUPA_RPT == null || lDS_USUPA_RPT.equals("")) {
			mMandatoryIDs.add("_field_DS_USUPA_RPT");
			mErrorMessages += Database.validateMessage(mUser.getTermo("VALIDACAMPO"), mUser.getTermo("REPETIRSENHA")) + "<br/>";
			valid = false;
		}

		if (valid) {
			if (lDS_USUPA_PWD != null && lDS_USUPA_PWD.trim().length() > 0) {
				// VERIFICA SENHA ATUAL
				if (lcdb.valorDB("USUPA", "CD_USUPA", "CD_USUPA = " + lCD_USUPA + " AND NM_USUPA_SENHA = '" + Crypt.addEncriptation(lDS_USUPA_PWD) + "'", mConn).length() < 1) {
					mMandatoryIDs.add("_field_DS_USUPA_PWD");
					mErrorMessages += "<strong>" + mUser.getTermo("SENHAATUAL") + ": </strong>" + mUser.getTermo("MSGSENHANAOCONF") + "<br/>";
					valid = false;
				}

				if (lDS_USUPA_NEW != null && lDS_USUPA_NEW.trim().length() > 0) {
					if (lDS_USUPA_NEW.length() < 6) {
						mMandatoryIDs.add("_field_DS_USUPA_NEW");
						mErrorMessages += "<strong>" + mUser.getTermo("NOVASENHA") + ": </strong>" + mUser.getTermo("MSGSENHA6CARAC") + "<br/>";
						valid = false;
					}
					if (negUSUPA.getNM_USUPA_SENHA() != null && Double.parseDouble(getParameter("_field_score")) < 26) {
						mErrorMessages += "<strong>" + mUser.getTermo("NOVASENHA") + ": </strong>" + mUser.getTermo("ERROR_SEGSENHA") + "<br/>";
						mMandatoryIDs.add("_field_DS_USUPA_NEW");
						valid = false;
					}

					if (!lDS_USUPA_NEW.equals(lDS_USUPA_RPT)) {
						mMandatoryIDs.add("_field_DS_USUPA_NEW");
						mMandatoryIDs.add("_field_DS_USUPA_RPT");
						mErrorMessages += "<strong>" + mUser.getTermo("NOVASENHA") + ": </strong>" + mUser.getTermo("MSGSENHANAOCONF") + "<br/>";
						valid = false;
					}
				}
				else {
					mMandatoryIDs.add("_field_DS_USUPA_NEW");
					mErrorMessages += Database.validateMessage("VALIDACAMPO", "NOVASENHA") + "<br/>";
					valid = false;
				}
			}
		}
		return valid;
	}

	public void script(PrintWriter out) {
		out.println("  jQuery(document).ready( function() {");
		out.println("		function translate(key){");
		out.println("			var resultado;");
		out.println("			switch(key){");
		out.println("				case 'wordLength': resultado = '" + mUser.getTermo("MSGSENHA6CARAC") + "'; break;");
		out.println("				case 'wordNotEmail': resultado = '" + mUser.getTermo("MSGSENHAEMAIL") + "'; break;");
		out.println("				case 'wordSimilarToUsername': resultado = '" + mUser.getTermo("MSGSENHALOGIN") + "'; break;");
		out.println("				case 'wordTwoCharacterClasses': resultado = '" + mUser.getTermo("MSGSENHATIPOS") + "'; break;");
		out.println("				case 'wordRepetitions': resultado = '" + mUser.getTermo("MSGSENHAREPET") + "'; break;");
		out.println("				case 'wordSequences': resultado = '" + mUser.getTermo("MSGSENHASEQUE") + "'; break;");
		out.println("				case 'errorList': resultado = '" + mUser.getTermo("ERRO") + "'; break;");
		out.println("				case 'veryWeak': resultado = '" + mUser.getTermo("MUITOFRACA") + "'; break;");
		out.println("				case 'weak': resultado = '" + mUser.getTermo("FRACA") + "'; break;");
		out.println("				case 'normal': resultado = '" + mUser.getTermo("NORMAL") + "'; break;");
		out.println("				case 'medium': resultado = '" + mUser.getTermo("MEDIO") + "'; break;");
		out.println("				case 'strong': resultado = '" + mUser.getTermo("FORTE") + "'; break;");
		out.println("				case 'veryStrong': resultado = '" + mUser.getTermo("MUITOFORTE") + "'; break;");
		out.println("				default: resultado = '';");
		out.println("			}");
		out.println("			return resultado;");
		out.println("		}");
		out.println("   \"use strict\";");
		out.println("	var options = {};");
		out.println("      options.ui = {");
		out.println("        showVerdictsInsideProgressBar: true,");
		out.println("        showErrors: true");
		out.println("      }; ");
		out.println("options.i18n = {");
		out.println("        t: function (key) {");
		out.println("			return translate(key);");
		out.println("        }");
		out.println("    };");
		out.println("      options.common = {");
		out.println("        usernameField: '#_hidden_NM_USUPA_LOGIN',");
		out.println("	     onKeyUp: function (evt, data) {");
		out.println("	        $('#_field_score').val(data.score);");
		out.println("	    }");
		out.println("      };");
		out.println("    $('#_field_DS_USUPA_NEW').pwstrength(options);");
		out.println("  });");
	}

	public void html(PrintWriter out) throws InputMaxLengthException {
		if (!mErrorMessages.trim().equals("")) {
			super.writeMsg(mErrorMessages, out);
		}
		out.println("<input type='hidden' value='" + mUser.getUserLogin() + "' id='_hidden_NM_USUPA_LOGIN' name='_hidden_NM_USUPA_LOGIN'/>");

		out.println("<div class='row'>");
		formInput.setInput("_field_DS_USUPA_PWD", "_field_DS_USUPA_PWD", mUser.getTermo("SENHAATUAL"), "", InputType.PASSWORD, ColumnWidth.TWELVE, true, false);
		formInput.setInputMaxLength(20);
		// formInput.setPortalForm(true);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_DS_USUPA_NEW", "_field_DS_USUPA_NEW", mUser.getTermo("NOVASENHA"), "", InputType.EXISTING_FIELD, ColumnWidth.TWELVE, true, false);
		// formInput.setPortalForm(true);
		formInput.setInputHTML("    <input type='password' name='_field_DS_USUPA_NEW' id='_field_DS_USUPA_NEW' value='' class='form-control' autocomplete='off' maxlength='20' />");
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_DS_USUPA_RPT", "_field_DS_USUPA_RPT", mUser.getTermo("REPETIRSENHA"), "", InputType.PASSWORD, ColumnWidth.TWELVE, true, false);
		formInput.setInputMaxLength(20);
		// formInput.setPortalForm(true);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<input type='hidden' value='' id='_field_score' name='_field_score'/>");

		out.println("<script type='text/javascript' src='metronic/global/plugins/bootstrap-pwstrength/pwstrength-bootstrap.js'></script>");

		out.println("<style>");
		out.println("  .progress {");
		out.println("    margin-bottom: 12px !important;");
		out.println("    margin-top: 0px !important;");
		out.println("  }");
		out.println("</style>");
	}
}
