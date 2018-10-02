
package br.com.manchestercity.view;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.InputMaxLengthException;
import br.com.manchestercity.automacao.PageContent;
import br.com.manchestercity.automacao.TitleMenu;
import br.com.manchestercity.automacao.Utils;
import br.com.manchestercity.automacao.ValidateReturn;
import br.com.manchestercity.automacao.FrameworkDefaults.ColumnWidth;
import br.com.manchestercity.automacao.FrameworkDefaults.InputType;
import br.com.manchestercity.negocio.NEGREQUEST;

public class PageREQUEST extends PageContent {

	private static final long serialVersionUID = 0L;

	NEGREQUEST negREQUEST;
	String CD_REQUEST;


	public void initialize(HttpServletResponse response) {
		super.setPageWidth(60);
		super.mShowMenu = true;
		this.CD_REQUEST="";
		this.negREQUEST = new NEGREQUEST(mUser);

	}

	public void getPrimaries(HttpServletResponse response) { 	
		
		CD_REQUEST = getParameter("CD_REQUEST");
		addPrimary("CD_REQUEST", CD_REQUEST);
		
	}

	public void getParameters(HttpServletResponse response) {
		this.negREQUEST.setFIRSTNAME(getParameter("_field_FIRSTNAME"));
		this.negREQUEST.setLASTNAME(getParameter("_field_LASTNAME"));
		this.negREQUEST.setDATEBIRTH(getParameter("_field_DATEBIRTH"));
		this.negREQUEST.setNATONALITY(getParameter("_field_NATONALITY"));
		this.negREQUEST.setGENDER(getParameter("_field_GENDER"));
		this.negREQUEST.setCOUNTRY(getParameter("_field_COUNTRY"));
		this.negREQUEST.setCITY(getParameter("_field_CITY"));
		this.negREQUEST.setPOSTCODE(getParameter("_field_POSTCODE"));
		this.negREQUEST.setEMAIL(getParameter("_field_EMAIL"));
		this.negREQUEST.setPHONE(getParameter("_field_PHONE"));
		this.negREQUEST.setISCITYZEN(getParameter("_field_ISCITYZEN"));
		this.negREQUEST.setSUPPORTERNUMBER(getParameter("_field_SUPPORTERNUMBER"));
		this.negREQUEST.setLALLOWCONTACT(getParameter("_field_LALLOWCONTACT"));
		this.negREQUEST.setTRDPARTYCONTACT(getParameter("_field_TRDPARTYCONTACT"));
		this.negREQUEST.setOPENTEXT(getParameter("_field_OPENTEXT"));
		this.negREQUEST.setSTATUS(getParameter("_field_STATUS"));
	}

	public void setTitle() {
		super.mTitle = "Requisições Sócio";
	}

	public void setAction() {
		super.mAction = "pagerequest";
	}

	public void menuConfig(TitleMenu titleMenu) {
		// TODO - REQALIZAR AS CONFIGURAÇÕES NECESSÁRIAS PARA O MENU;
	}

	public boolean validate(HttpServletResponse response, PrintWriter out) {
		ValidateReturn validate = negREQUEST.validate(mConn);

		if (!validate.getMessage().trim().equals("")) {
			super.mMandatoryIDs = validate.getMandatoryIDs();
			writeMsg(validate.getMessage(), out);
		}

		return validate.getMessage().trim().equals("");
	}

	public void insert(HttpServletResponse response) {
		long lcod = this.negREQUEST.insert(mConn, mErro);
		if (lcod > 0) {
			CD_REQUEST = String.valueOf(lcod);
			addPrimary("CD_REQUEST", CD_REQUEST);

			
		}
	}

	public void update(HttpServletResponse response) {
		this.negREQUEST.update(mConn, mErro);
	}

	public boolean delete(HttpServletResponse response) {
		return this.negREQUEST.delete(mConn, mErro);
	}

	public void addControls() {
		addControl("_field_FIRSTNAME", "FIRSTNAME");
		addControl("_field_LASTNAME", "LASTNAME");
		addControl("_field_DATEBIRTH", "DATEBIRTH");
		addControl("_field_NATONALITY", "NATONALITY");
		addControl("_field_GENDER", "GENDER");
		addControl("_field_COUNTRY", "COUNTRY");
		addControl("_field_CITY", "CITY");
		addControl("_field_POSTCODE", "POSTCODE");
		addControl("_field_EMAIL", "EMAIL");
		addControl("_field_PHONE", "PHONE");
		addControl("_field_ISCITYZEN", "ISCITYZEN");
		addControl("_field_SUPPORTERNUMBER", "SUPPORTERNUMBER");
		addControl("_field_LALLOWCONTACT", "LALLOWCONTACT");
		addControl("_field_TRDPARTYCONTACT", "TRDPARTYCONTACT");
		addControl("_field_OPENTEXT", "OPENTEXT");
		addControl("_field_STATUS", "STATUS");
	}

	public ResultSet show() {
		return this.negREQUEST.refresh(mConn,CD_REQUEST, "","","","","","","","","","","","","","","","", "");
	}

	public void html(PrintWriter out) throws InputMaxLengthException {
		out.println("<div class='row'>");
		formInput.setInput("_field_FIRSTNAME", "_field_FIRSTNAME", "Nome", getValueByField("FIRSTNAME"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_LASTNAME", "_field_LASTNAME", "Sobrenome", getValueByField("LASTNAME"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_DATEBIRTH", "_field_DATEBIRTH","Data Nascimento", getValueByField("DATEBIRTH"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_NATONALITY", "_field_NATONALITY", "Nacionalidade", getValueByField("NATONALITY"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_GENDER", "_field_GENDER","Gênero", getValueByField("GENDER"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_COUNTRY", "_field_COUNTRY", "País", getValueByField("COUNTRY"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_CITY", "_field_CITY", "Cidade", getValueByField("CITY"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_POSTCODE", "_field_POSTCODE", "CEP", getValueByField("POSTCODE"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_EMAIL", "_field_EMAIL", "E-Mail", getValueByField("EMAIL"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_PHONE", "_field_PHONE","Telefone", getValueByField("PHONE"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_ISCITYZEN", "_field_ISCITYZEN", "Membro Cityzen?", getValueByField("ISCITYZEN"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_SUPPORTERNUMBER", "_field_SUPPORTERNUMBER", "Support Number", getValueByField("SUPPORTERNUMBER"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_LALLOWCONTACT", "_field_LALLOWCONTACT", "Permite Contato?", getValueByField("LALLOWCONTACT"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_TRDPARTYCONTACT", "_field_TRDPARTYCONTACT", "Permite Contato de Terceiro?", getValueByField("TRDPARTYCONTACT"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_OPENTEXT", "_field_OPENTEXT", "Texto Aberto", getValueByField("OPENTEXT"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

		out.println("<div class='row'>");
		formInput.setInput("_field_STATUS", "_field_STATUS", "Status", getValueByField("STATUS"), InputType.TEXT, ColumnWidth.TWELVE, false, false);
		formInput.setInputMaxLength(512);
		out.println(formInput.printInput());
		out.println("</div>");

	}

}
