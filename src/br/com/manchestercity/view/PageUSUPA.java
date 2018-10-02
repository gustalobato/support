
package br.com.manchestercity.view;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.InputMaxLengthException;
import br.com.manchestercity.automacao.Ordination;
import br.com.manchestercity.automacao.PageContent;
import br.com.manchestercity.automacao.SmartCombo;
import br.com.manchestercity.automacao.SystemIcons;
import br.com.manchestercity.automacao.TitleMenu;
import br.com.manchestercity.automacao.Utils;
import br.com.manchestercity.automacao.ValidateReturn;
import br.com.manchestercity.automacao.FrameworkDefaults.ColumnWidth;
import br.com.manchestercity.automacao.FrameworkDefaults.InputType;
import br.com.manchestercity.negocio.NEGUSUPA;

public class PageUSUPA extends PageContent {

	private static final long serialVersionUID = 0L;

	NEGUSUPA negUSUPA;

	String lCD_USUPA = "";

	public void initialize(HttpServletResponse response) {
		super.setPageWidth(60);
		super.mShowMenu = true;
		super.mSecurityFunction = "cad_usuario";
		super.mVerifySecurityFunction = true;

		this.negUSUPA = new NEGUSUPA(mUser);

		lCD_USUPA = "";
	}

	public void getPrimaries(HttpServletResponse response) {
		lCD_USUPA = getParameter("CD_USUPA");
		addPrimary("CD_USUPA", lCD_USUPA);
	}

	public void getParameters(HttpServletResponse response) {
		this.negUSUPA.setCD_USUPA(lCD_USUPA);
		this.negUSUPA.setCD_MTAUS(getParameter("_column_MTAUS_MTAUS_CD_MTAUS"));
		this.negUSUPA.setCD_AREAS(getParameter("_column_AREAS_AREAS_CD_AREAS"));
		this.negUSUPA.setNM_USUPA(getParameter("_field_NM_USUPA"));
		this.negUSUPA.setNM_USUPA_EMAIL(getParameter("_field_NM_USUPA_EMAIL"));
		this.negUSUPA.setNO_USUPA_RAMAL(getParameter("_field_NO_USUPA_RAMAL"));
		this.negUSUPA.setNM_USUPA_LOGIN(getParameter("_field_NM_USUPA_LOGIN"));
	//	if (getParameter("_column_MTAUS_MTAUS_ID_MTAUS_TIPO").trim().equals(NEGMTAUS.ID_MTAUS_SISTEMA))
		this.negUSUPA.setNM_USUPA_SENHA(getParameter("_field_NM_USUPA_SENHA"));
		this.negUSUPA.setID_USUPA_STATU(getParameter("_field_ID_USUPA_STATU"));
	}

	public void setTitle() {
		super.mTitle = mUser.getTermo("USUARIO");
	}

	public void setAction() {
		super.mAction = "pageusupa";
	}

	public void menuConfig(TitleMenu titleMenu) {
		// TODO - REQALIZAR AS CONFIGURAÇÕES NECESSÁRIAS PARA O MENU;
	}

	public boolean validate(HttpServletResponse response, PrintWriter out) {
		ValidateReturn validate = negUSUPA.validate(mConn);

		if (!negUSUPA.getCD_MTAUS().trim().equals("")) {
			if (!negUSUPA.getNM_USUPA_LOGIN().trim().equals("")) {
				long qtde = lcdb.countDB(mConn, "USUPA", "CD_MTAUS = " + negUSUPA.getCD_MTAUS() + " AND LOWER(NM_USUPA_LOGIN) = LOWER('" + negUSUPA.getNM_USUPA_LOGIN().trim() + "')" + ( mAcao.startsWith("upd") ? " AND CD_USUPA <> " + lCD_USUPA : "" ));
				if (qtde > 0) {
					validate.setMessage(validate.getMessage() + mUser.getTermo("ERROR_LOGINMETAUTH") + "<br/>");
					validate.addMandatoryId("_field_NM_USUPA_LOGIN");
				}
			}
		}

		if (!Database.verifyNull(negUSUPA.getNM_USUPA_SENHA()).trim().equals("") && Database.verifyNullDouble(getParameter("_field_score")) < 26) {
			validate.setMessage(validate.getMessage() + mUser.getTermo("ERROR_SEGSENHA") + "<br/>");
			validate.addMandatoryId("_field_NM_USUPA_SENHA");
		}

		if (!validate.getMessage().trim().equals("")) {
			super.mMandatoryIDs = validate.getMandatoryIDs();
			writeMsg(validate.getMessage(), out);
		}

		return validate.getMessage().trim().equals("");
	}

	public void insert(HttpServletResponse response) {
		long lcod = this.negUSUPA.insert(mConn, mErro);
		if (lcod > 0) {
			lCD_USUPA = String.valueOf(lcod);
			addPrimary("CD_USUPA", lCD_USUPA);

			
		}
		mAcao = "upd";
	}

	public void update(HttpServletResponse response) {
		this.negUSUPA.update(mConn, mErro);
	}

	public boolean delete(HttpServletResponse response) {
		boolean commit = false;
		boolean isAutoCommit = lcdb.isAutoCommit(mConn);
		if (isAutoCommit) {
			lcdb.setAutoCommit(mConn, false);
		}

		try {

			commit = negUSUPA.delete(mConn, mErro);
				
		}
		catch (Exception ex) {
			Utils.printSystemError("PageOFMTS:anexar :", ex.getMessage());
		}
		finally {
			if (isAutoCommit) {
				if (commit) {
					lcdb.commit(mConn);
				}
				else {
					lcdb.rollback(mConn);
				}
				lcdb.setAutoCommit(mConn, true);
			}
		}
		return commit;
	}

	public void addControls() {
		addControl("_column_MTAUS_MTAUS_CD_MTAUS", "CD_MTAUS");
		addControl("_column_AREAS_AREAS_CD_AREAS", "CD_AREAS");
		addControl("_field_NM_USUPA", "NM_USUPA");
		addControl("_field_NM_USUPA_EMAIL", "NM_USUPA_EMAIL");
		addControl("_field_NO_USUPA_RAMAL", "NO_USUPA_RAMAL");
		addControl("_field_NM_USUPA_LOGIN", "NM_USUPA_LOGIN");
		addControl("_field_NM_USUPA_SENHA", "NM_USUPA_SENHA");
		addControl("_field_ID_USUPA_STATU", "ID_USUPA_STATU");
	}

	public ResultSet show() {
		return this.negUSUPA.refresh(mConn, lCD_USUPA, "", "", "", "", "", "", "", "", "");
	}

	public void script(PrintWriter out) {
		out.println("  function trocaTipo(tipo){");
	
		out.println("  }");

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
		out.println("        usernameField: '#_field_NM_USUPA_LOGIN',");
		out.println("	     onKeyUp: function (evt, data) {");
		out.println("	        $('#_field_score').val(data.score);");
		out.println("	    }");
		out.println("      };");
		out.println("    $('#_field_NM_USUPA_SENHA').pwstrength(options);");
		out.println("  });");
	}

	public void html(PrintWriter out) throws InputMaxLengthException {

		out.println("<div class='row'>");
		formInput.setInput("_field_NM_USUPA", "_field_NM_USUPA", mUser.getTermo("NOME"), getValueByField("NM_USUPA"), InputType.TEXT, ColumnWidth.TWELVE, true, false);
		formInput.setInputMaxLength(256);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_NM_USUPA_EMAIL", "_field_NM_USUPA_EMAIL", mUser.getTermo("EMAIL"), getValueByField("NM_USUPA_EMAIL"), InputType.TEXT, ColumnWidth.EIGHT, true, false);
		formInput.setInputMaxLength(256);
		out.println(formInput.printInput());
		// out.println("</div>");
		//
		// out.println("<div class='row'>");
		formInput.setInput("_field_NO_USUPA_RAMAL", "_field_NO_USUPA_RAMAL", mUser.getTermo("TELEFONE"), getValueByField("NO_USUPA_RAMAL"), InputType.TEXT, ColumnWidth.FOUR, true, false);
		formInput.setInputMaxLength(15);
		formInput.setOnBlur("mascaraTelefone(this, event);");
		formInput.setOnKeyUp("mascaraTelefone(this, event);");
		out.println(formInput.printInput());
		out.println("</div>");

		String CD_STEST = "";
		if (!getValueByField("CD_AREAS").trim().equals("")) {
			CD_STEST = lcdb.valorDB("AREAS", "CD_STEST", "CD_AREAS = " + getValueByField("CD_AREAS").trim(), mConn);
		}

	
		out.println("<script>");
		out.println("	function afterFillMTAUS() {");
		out.println("		trocaTipo( $('#_column_MTAUS_MTAUS_ID_MTAUS_TIPO').val() );");
		out.println("	}");
		out.println("</script>");

		out.println("<div class='row'>");
		formInput.setInput("_field_NM_USUPA_LOGIN", "_field_NM_USUPA_LOGIN", mUser.getTermo("LOGIN"), getValueByField("NM_USUPA_LOGIN"), InputType.TEXT, ColumnWidth.SIX, true, false);
		formInput.setInputMaxLength(32);
		out.println(formInput.printInput());

		out.println("<div id='_div_senha' >");
		formInput.setInput("_field_NM_USUPA_SENHA", "_field_NM_USUPA_SENHA", mUser.getTermo("SENHA"), "", InputType.EXISTING_FIELD, ColumnWidth.SIX, true, false);
		// formInput.setInputMaxLength(32);
		formInput.setInputHTML("    <input type='password' name='_field_NM_USUPA_SENHA' id='_field_NM_USUPA_SENHA' value='" + ( mAcao.startsWith("ins") ? "" : "******" ) + "' class='form-control' autocomplete='off' maxlength='32' />");
		out.println(formInput.printInput());
		out.println("	</div>");
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_ID_USUPA_STATU", "_field_ID_USUPA_STATU", mUser.getTermo("STATUS"), Database.verifyNull(getValueByField("ID_USUPA_STATU"), "A"), InputType.RADIO, ColumnWidth.TWELVE, true, false);
		formInput.addInputOption(mUser.getTermo("ATIVO"), "A");
		formInput.addInputOption(mUser.getTermo("INATIVO"), "I");
		formInput.setOptionsInline(true);
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
