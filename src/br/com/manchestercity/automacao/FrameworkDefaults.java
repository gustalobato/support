
package br.com.manchestercity.automacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.SystemConfig;

public class FrameworkDefaults {

	// ENUM UTILIZADO NA EXIBIÇÃO DE CAMPOS
	public enum InputType {
		CHECKBOX, EXISTING_FIELD, FILE, PASSWORD, RADIO, SELECT, TEXT, TEXTAREA, TEXTAREA_EDITOR
	}

	// ENUM UTILIZADO NA EXIBIÇÃO DE FILTROS
	public enum ReportFieldType {
		CHECKBOX, DATE, DATETIME, DECIMAL, INTEGER, KEYWORD, MONTH_YEAR, RADIO, SELECT, SMARTCOMBO, SMARTCOMBO_LIST, TEXT, TEXTAREA
	}

	public enum FormState {
		BACK, INITIAL, SHOW
	}

	// ENUM UTILIZADO NO ESTILO DE MENSAGENS E TEXTOS
	public enum MessageType {
		DANGER, DEFAULT, INFO, SUCCESS, WARNING
	}

	// ENUM UTILIZADO NA ESPECIFICAÇÃO DO TAMANHO
	public enum ColumnWidth {
		ONE("col-md-1"), TWO("col-md-2"), THREE("col-md-3"), FOUR("col-md-4"), FIVE("col-md-5"), SIX("col-md-6"), SEVEN("col-md-7"), EIGHT("col-md-8"), NINE("col-md-9"), TEN("col-md-10"), ELEVEN("col-md-11"), TWELVE("col-md-12");

		private final String value;

		private ColumnWidth(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public static String printIcon(String pId, String pIconClass, String pTooltip, String pOnClick, String pExtraClasses, String pExtraStyles) {
		return "<i id='" + pId + "' class='fa " + pIconClass + " " + pExtraClasses + ( pTooltip.length() > 0 ? " tooltips" : "" ) + "'" + ( pExtraStyles.length() > 0 ? " style='" + pExtraStyles + "'" : "" ) + ( pTooltip.length() > 0 ? " data-placement='top' data-container='body' data-html='true' data-original-title='" + pTooltip + "'" : "" ) + ( pOnClick.length() > 0 ? " onclick=\"" + pOnClick + "\"" : "" ) + "></i>";
	}

	public static String printButton(String pId, String pDescription, String pIcon, String pExtraClasses, String pExtraStyles, String pOnClick, boolean pDisabled) {
		return "<a href='javascript:;'" + ( pId.trim().equals("") ? "" : " id='" + pId + "' " ) + ( pDisabled ? "class='btn " + pExtraClasses + " disabled'" : "class='btn " + pExtraClasses + "' onclick=\"" + pOnClick + "\"" ) + ( pExtraStyles.length() > 0 ? " style='" + pExtraStyles + "'" : "" ) + "> " + pIcon + ( pDescription.trim().equals("") ? "" : "&nbsp;" + pDescription + "&nbsp;" ) + " </a>";
	}

	public static String printMessageBlock(String pId, String pContent, String pExtraClasses, String pExtraStyles, MessageType pType, boolean pShowClose) {
		StringBuffer html = new StringBuffer();

		String type = " alert-info ";
		switch (pType) {
			case DANGER:
				type = " alert-danger ";
				break;

			case INFO:
				type = " alert-info ";
				break;

			case SUCCESS:
				type = " alert-success ";
				break;

			case WARNING:
				type = " alert-warning ";
				break;

			default:
				type = " alert-info ";
				break;
		}

		html.append("<div id='" + pId + "' class='alert alert-dismissable " + type + pExtraClasses + "'" + ( pExtraStyles.length() > 0 ? " style='" + pExtraStyles + "'" : "" ) + ">\n");

		if (pShowClose) {
			html.append("  <button type='button' class='close' data-dismiss='alert' aria-hidden='true'></button>\n");
		}

		html.append(pContent);
		html.append("</div>\n");

		return html.toString();
	}

	// UTILIZAR CSystemIcons PARA ICONES DO MODAL
	public static String printModal(String pId, String pTitle, String pContent, String pCancelIcon, String pCancelLabel, String pCancelAction, String pExtraClasses, String pExtraStyles, boolean pInnerHTML) {
		return printModal(pId, pTitle, pContent, pCancelIcon, pCancelLabel, pCancelAction, false, "", "", "", pExtraClasses, pExtraStyles, pInnerHTML);
	}

	public static String printModal(String pId, String pTitle, String pContent, String pCancelIcon, String pCancelLabel, String pCancelAction, String pConfirmIcon, String pConfirmLabel, String pConfirmAction, String pExtraClasses, String pExtraStyles, boolean pInnerHTML) {
		return printModal(pId, pTitle, pContent, pCancelIcon, pCancelLabel, pCancelAction, true, pConfirmIcon, pConfirmLabel, pConfirmAction, pExtraClasses, pExtraStyles, pInnerHTML);
	}

	public static String printModal(String pId, String pTitle, String pContent, String pCancelIcon, String pCancelLabel, String pCancelAction, boolean showConfirm, String pConfirmIcon, String pConfirmLabel, String pConfirmAction, String pExtraClasses, String pExtraStyles, boolean pInnerHTML) {
		StringBuffer html = new StringBuffer();

		html.append("<div class='modal fade " + pExtraClasses + "' data-backdrop='static' data-replace='true' data-keyboard='false' id='" + pId + "'>\n");
		html.append("  <div class='modal-dialog'>\n");
		html.append("    <div class='modal-content'>\n");
		html.append("      <div class='modal-header'>\n");
		// html.append(" <button type='button' class='close' aria-hidden='true' onclick=\"" + pCancelAction + "\"></button>\n");
		html.append("        <h4 class='modal-title'>\n");
		html.append(pTitle);
		html.append("        </h4>\n");
		html.append("      </div>\n");
		html.append("      <div class='modal-body form'>\n");
		html.append("        <div class='form-body horizontal-form' id='" + pId + "_content'>\n");
		html.append(pContent);
		html.append("        </div>\n");
		html.append("      </div>\n");
		html.append("      <div class='modal-footer'>\n");
		if (showConfirm) {
			html.append("        <button type='button' class='btn btn-default' onclick=\"" + pCancelAction + "\"> " + pCancelIcon + "&nbsp;&nbsp;" + pCancelLabel + " </button>\n");
			html.append("        <button type='button' class='btn btn-primary' onclick=\"" + pConfirmAction + "\"> " + pConfirmIcon + "&nbsp;&nbsp;" + pConfirmLabel + " </button>\n");
		}
		else
			html.append("        <button type='button' class='btn btn-primary' onclick=\"" + pCancelAction + "\"> " + pCancelIcon + "&nbsp;&nbsp;" + pCancelLabel + " </button>\n");
		html.append("      </div>\n");
		html.append("    </div>\n");
		html.append("  </div>\n");
		html.append("</div>\n");

		return pInnerHTML ? "\"" + html.toString().replaceAll("\\r|\\n", "").replaceAll("\\\"", "\\\\\\\"") + "\"" : html.toString();
	}

	// OBJETO UTILIZADO PARA CRIAÇÃO DE PORTLET BOXES RETRÁTEIS
	public class PortletBox {

		private String portletID;
		private String portletColor;
		private String portletTitle;
		private String portletContent;
		private String portletExtraClasses;
		private String portletExtraStyles;

		private String scrollerID;
		private String scrollerColor;
		private String scrollerHeigth;

		private ArrayList<String> portletActionList;

		private boolean boxed;
		private boolean closed;
		private boolean showTools;
		private boolean showScroller;

		public PortletBox() {
			// NOTHING TO DO HERE
		}

		public String getPortletID() {
			return portletID;
		}

		public void setPortletID(String portletID) {
			this.portletID = portletID;
		}

		public String getPortletColor() {
			return portletColor;
		}

		public void setPortletColor(String portletColor) {
			this.portletColor = portletColor;
		}

		public String getPortletTitle() {
			return portletTitle;
		}

		public void setPortletTitle(String portletTitle) {
			this.portletTitle = portletTitle;
		}

		public String getPortletContent() {
			return portletContent;
		}

		public void setPortletContent(String portletContent) {
			this.portletContent = portletContent;
		}

		public String getPortletExtraClasses() {
			return portletExtraClasses;
		}

		public void setPortletExtraClasses(String portletExtraClasses) {
			this.portletExtraClasses = portletExtraClasses;
		}

		public String getPortletExtraStyles() {
			return portletExtraStyles;
		}

		public void setPortletExtraStyles(String portletExtraStyles) {
			this.portletExtraStyles = portletExtraStyles;
		}

		public String getScrollerID() {
			return scrollerID;
		}

		public void setScrollerID(String scrollerID) {
			this.scrollerID = scrollerID;
		}

		public String getScrollerColor() {
			return scrollerColor;
		}

		public void setScrollerColor(String scrollerColor) {
			this.scrollerColor = scrollerColor;
		}

		public String getScrollerHeigth() {
			return scrollerHeigth;
		}

		public void setScrollerHeigth(String scrollerHeigth) {
			this.scrollerHeigth = scrollerHeigth;
		}

		public boolean isBoxed() {
			return boxed;
		}

		public void setBoxed(boolean boxed) {
			this.boxed = boxed;
		}

		public boolean isClosed() {
			return closed;
		}

		public void setClosed(boolean closed) {
			this.closed = closed;
		}

		public boolean isShowTools() {
			return showTools;
		}

		public void setShowTools(boolean showTools) {
			this.showTools = showTools;
		}

		public boolean isShowScroller() {
			return showScroller;
		}

		public void setShowScroller(boolean showScroller) {
			this.showScroller = showScroller;
		}

		public void addActionButton(String html) {
			this.portletActionList.add(html);
		}

		public PortletBox(String portletID, String portletTitle, String portletContent) {
			setPortlet(portletID, portletTitle, portletContent);
		}

		public void setPortlet(String portletID, String portletTitle, String portletContent) {
			this.portletID = portletID;
			this.portletTitle = portletTitle;
			this.portletContent = portletContent;

			this.portletColor = "blue-steel";
			this.portletExtraClasses = "";
			this.portletExtraStyles = "";

			this.scrollerID = "";
			this.scrollerColor = "blue";
			this.scrollerHeigth = "256px";

			if (this.portletActionList != null) {
				this.portletActionList.clear();
			}
			else {
				this.portletActionList = new ArrayList<String>();
			}

			this.boxed = true;
			this.closed = false;
			this.showTools = true;
			this.showScroller = false;
		}

		public String printPortletBox() {
			StringBuffer html = new StringBuffer();

			html.append("<div class='portlet " + ( this.boxed ? "box " : "" ) + this.portletColor + " " + ( portletExtraClasses.length() > 0 ? portletExtraClasses : "" ) + "' " + ( portletExtraStyles.length() > 0 ? " style='" + portletExtraStyles + "'" : "" ) + " id='" + this.portletID + "'>\n");
			html.append("  <div class='portlet-title'>\n");
			html.append("    <div class='caption'>\n");
			html.append(portletTitle);
			html.append("\n");
			html.append("    </div>\n");
			if (showTools) {
				html.append("    <div class='tools'>\n");
				html.append("      <a href='javascript:;' class='" + ( closed ? "expand" : "collapse" ) + "'></a>\n");
				html.append("    </div>\n");
			}
			if (portletActionList.size() > 0) {
				html.append("    <div class='actions'>\n");
				for (String button : portletActionList) {
					html.append(button);
					html.append("\n");
				}
				html.append("    </div>\n");
			}
			html.append("  </div>\n");
			html.append("  <div class='portlet-body'" + ( closed ? " style='display: none;'" : "" ) + ">\n");
			if (showScroller) {
				html.append("    <div class='scroller' style='overflow: hidden; width: auto; height: " + this.scrollerHeigth + ";' data-always-visible='1' data-handle-color='" + this.scrollerColor + "' id='" + this.scrollerID + "'>\n");
			}
			html.append("    <div class='row'>");
			html.append("      <div class='col-md-12'>");
			html.append(portletContent);
			html.append("\n");
			html.append("      </div>\n");
			html.append("    </div>\n");
			if (showScroller) {
				html.append("    </div>\n");
			}
			html.append("  </div>\n");
			html.append("</div>");

			return html.toString();
		}
	}

	// FIELDSET
	public class FieldSet {
		private String head;
		private String body;

		private ArrayList<String> iconsLeft;
		private ArrayList<String> iconsRight;

		public FieldSet() {
			head = "";
			body = "";

			iconsLeft = new ArrayList<String>();
			iconsRight = new ArrayList<String>();
		}

		public void setFieldSet(String pTitle, String pBody) {
			head = pTitle;
			body = pBody;
			iconsLeft.clear();
			iconsRight.clear();
		}

		public String getHead(String head) {
			return head;
		}

		public void setHead(String head) {
			this.head = head;
		}

		public String getBody(String body) {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public void addIcon(String pId, String pIconClass, String pTooltip, String pOnClick, String pExtraClasses, String pExtraStyles, boolean pFloatRight) {
			if (!pFloatRight) {
				this.iconsLeft.add(FrameworkDefaults.printIcon(pId, pIconClass, pTooltip, pOnClick, pExtraClasses, pExtraStyles));
			}
			else {
				this.iconsRight.add(FrameworkDefaults.printIcon(pId, pIconClass, pTooltip, pOnClick, pExtraClasses, pExtraStyles));
			}
		}

		public String printFieldSet() {
			StringBuffer fieldSet = new StringBuffer();

			fieldSet.append("<div class='panel panel-default'>\n");
			fieldSet.append("	 <div class='panel-heading'>\n");
			fieldSet.append(this.printIcons());
			fieldSet.append("    <h3 class='panel-title'>\n");
			fieldSet.append(this.head);
			fieldSet.append("    </h3>\n");
			fieldSet.append("  </div>\n");
			fieldSet.append("  <div class='panel-body'>\n");
			fieldSet.append(this.body);
			fieldSet.append("  </div>\n");
			fieldSet.append("</div>\n");

			return fieldSet.toString();
		}

		private String printIcons() {
			StringBuffer icons = new StringBuffer();

			for (String icon : iconsLeft) {
				icons.append("    <div style='float: left; margin-right: 10px; margin-top: 2px;'>");
				icons.append(icon);
				icons.append("    </div>\n");
			}

			for (String icon : iconsRight) {
				icons.append("    <div style='float: right; margin-left: 10px; margin-top: 2px;'>\n");
				icons.append(icon);
				icons.append("    </div>\n");
			}

			return icons.toString();
		}
	}

	// OBJETO UTILIZADO PARA CRIAÇÃO DE INPUTS COMPLETOS
	public class FormInput {

		Database database;
		Connection connection;

		// INPUT ATTRIBUTES
		private String inputID;
		private String inputLabel;
		private String inputName;
		private String inputRows;
		private String inputValue;

		private int inputMaxLength;

		private InputType inputType;

		private String inputHTML; // Utilizado nos casos de campos já existentes (Ex.: CDatePicker, CSmartCombo, etc.)

		private boolean inputMandatory; // Indicador de inclusão do asterisco que indica obrigatoriedade do campo
		private boolean inputReadOnly;

		private boolean portalForm;

		// CLASSES AND STYLES
		private String inputExtraClasses; // Classes CSS adicionais para definição do campo (Ex.: text-left, text-center, text-right, etc.)
		private String inputExtraStyles; // Estilos embutidos direto no campo para sobrescrever alguma propriedade (Ex.: display:none, font-size:6px, etc.)
		private String divWidthClass; // Classe CSS que indica a largura da DIV na qual o campo será exibido (Ex.: col-md-12, col-md-4, col-sm-6, etc.)
		private String divExtraClasses; // Classes CSS adicionais para definição do campo (Ex.: text-left, text-center, text-right, etc.)
		private String divExtraStyles; // Estilos embutidos direto na div para sobrescrever alguma propriedade (Ex.: display:none, font-size:6px, etc.)

		// EXTRA ATTRIBUTES
		private String inputExtraAttrs;

		// FORM EVENTS
		private String onBlur;
		private String onChange;
		private String onContextMenu;
		private String onFocus;
		private String onSelect;

		// KEY EVENTS
		private String onKeyDown;
		private String onKeyPress;
		private String onKeyUp;

		// MOUSE EVENTS
		private String onClick;
		private String onDoubleClick;

		// OTHER EVENTS
		private String onCopy;
		private String onPaste;

		// INPUT OPTIONS
		public ArrayList<InputOption> inputOptions;

		private boolean optionsInline;

		// INPUT OPTIONS -- PARAMETROS PARA CRIAÇÃO DE CONSSULTAS EM TABELA
		private String optionsTable;
		private String optionsLabel;
		private String optionsValue;
		private String optionsFilter;
		private String optionsOrderBy;

		private String helpBlock;
		private String buttonGroup;

		public void setDatabase(Database lcdb) {
			this.database = lcdb;
		}

		public void setConnection(Connection connection) {
			this.connection = connection;
		}

		public String getInputID() {
			return inputID;
		}

		public void setInputID(String inputID) {
			this.inputID = inputID;
		}

		public String getInputLabel() {
			return inputLabel;
		}

		public void setInputLabel(String inputLabel) {
			this.inputLabel = inputLabel;
		}

		public int getInputMaxLength() {
			return inputMaxLength;
		}

		public void setInputMaxLength(int inputMaxLength) {
			this.inputMaxLength = inputMaxLength;
		}

		public String getInputName() {
			return inputName;
		}

		public void setInputName(String inputName) {
			this.inputName = inputName;
		}

		public String getInputRows() {
			return inputRows;
		}

		public void setInputRows(String inputRows) {
			this.inputRows = inputRows;
		}

		public String getInputValue() {
			return inputValue;
		}

		public void setInputValue(String inputValue) {
			this.inputValue = inputValue;
		}

		public InputType getInputType() {
			return inputType;
		}

		public void setInputType(InputType inputType) {
			this.inputType = inputType;
		}

		public String getInputHTML() {
			return inputHTML;
		}

		public void setInputHTML(String inputHTML) {
			this.inputHTML = inputHTML;
		}

		public boolean isInputMandatory() {
			return inputMandatory;
		}

		public void setInputMandatory(boolean inputMandatory) {
			this.inputMandatory = inputMandatory;
		}

		public boolean isInputReadOnly() {
			return inputReadOnly;
		}

		public void setInputReadOnly(boolean inputReadOnly) {
			this.inputReadOnly = inputReadOnly;
		}

		public boolean isPortalForm() {
			return portalForm;
		}

		public void setPortalForm(boolean portalForm) {
			this.portalForm = portalForm;
		}

		public String getInputExtraClasses() {
			return inputExtraClasses;
		}

		public void setInputExtraClasses(String inputExtraClasses) {
			this.inputExtraClasses = inputExtraClasses;
		}

		public String getInputExtraStyles() {
			return inputExtraStyles;
		}

		public void setInputExtraStyles(String inputExtraStyles) {
			this.inputExtraStyles = inputExtraStyles;
		}

		public String getDivWidthClass() {
			return divWidthClass;
		}

		public void setDivWidthClass(String divWidthClass) {
			this.divWidthClass = divWidthClass;
		}

		public String getDivExtraClasses() {
			return divExtraClasses;
		}

		public void setDivExtraClasses(String divExtraClasses) {
			this.divExtraClasses = divExtraClasses;
		}

		public String getDivExtraStyles() {
			return divExtraStyles;
		}

		public void setDivExtraStyles(String divExtraStyles) {
			this.divExtraStyles = divExtraStyles;
		}

		public String getInputExtraAttrs() {
			return inputExtraAttrs;
		}

		public void setInputExtraAttrs(String inputExtraAttrs) {
			this.inputExtraAttrs = inputExtraAttrs;
		}

		public String getOnBlur() {
			return onBlur;
		}

		public void setOnBlur(String onBlur) {
			this.onBlur = onBlur;
		}

		public String getOnChange() {
			return onChange;
		}

		public void setOnChange(String onChange) {
			this.onChange = onChange;
		}

		public String getOnContextMenu() {
			return onContextMenu;
		}

		public void setOnContextMenu(String onContextMenu) {
			this.onContextMenu = onContextMenu;
		}

		public String getOnFocus() {
			return onFocus;
		}

		public void setOnFocus(String onFocus) {
			this.onFocus = onFocus;
		}

		public String getOnSelect() {
			return onSelect;
		}

		public void setOnSelect(String onSelect) {
			this.onSelect = onSelect;
		}

		public String getOnKeyDown() {
			return onKeyDown;
		}

		public void setOnKeyDown(String onKeyDown) {
			this.onKeyDown = onKeyDown;
		}

		public String getOnKeyPress() {
			return onKeyPress;
		}

		public void setOnKeyPress(String onKeyPress) {
			this.onKeyPress = onKeyPress;
		}

		public String getOnKeyUp() {
			return onKeyUp;
		}

		public void setOnKeyUp(String onKeyUp) {
			this.onKeyUp = onKeyUp;
		}

		public String getOnClick() {
			return onClick;
		}

		public void setOnClick(String onClick) {
			this.onClick = onClick;
		}

		public String getOnDoubleClick() {
			return onDoubleClick;
		}

		public void setOnDoubleClick(String onDoubleClick) {
			this.onDoubleClick = onDoubleClick;
		}

		public String getOnCopy() {
			return onCopy;
		}

		public void setOnCopy(String onCopy) {
			this.onCopy = onCopy;
		}

		public String getOnPaste() {
			return onPaste;
		}

		public void setOnPaste(String onPaste) {
			this.onPaste = onPaste;
		}

		public boolean isOptionsInline() {
			return optionsInline;
		}

		public void setOptionsInline(boolean optionsInline) {
			this.optionsInline = optionsInline;
		}

		public String getOptionsTable() {
			return optionsTable;
		}

		public void setOptionsTable(String optionsTable) {
			this.optionsTable = optionsTable;
		}

		public String getOptionsLabel() {
			return optionsLabel;
		}

		public void setOptionsLabel(String optionsLabel) {
			this.optionsLabel = optionsLabel;
		}

		public String getOptionsValue() {
			return optionsValue;
		}

		public void setOptionsValue(String optionsValue) {
			this.optionsValue = optionsValue;
		}

		public String getOptionsFilter() {
			return optionsFilter;
		}

		public void setOptionsFilter(String optionsFilter) {
			this.optionsFilter = optionsFilter;
		}

		public String getOptionsOrderBy() {
			return optionsOrderBy;
		}

		public void setOptionsOrderBy(String optionsOrderBy) {
			this.optionsOrderBy = optionsOrderBy;
		}

		public ArrayList<InputOption> getInputOptions() {
			return inputOptions;
		}

		public String getHelpBlock() {
			return helpBlock;
		}

		public void setHelpBlock(String helpBlock) {
			this.helpBlock = helpBlock;
		}

		public String getButtonGroup() {
			return buttonGroup;
		}

		public void setButtonGroup(String buttonGroup) {
			this.buttonGroup = buttonGroup;
		}

		public FormInput() {
			setInput("", "", "", "", InputType.EXISTING_FIELD, "", false, false);
		}

		public void setInput(String inputID, String inputName, String inputLabel, String inputValue, InputType inputType, ColumnWidth divWidthClass, boolean inputMandatory, boolean inputReadOnly) {
			setInput(inputID, inputName, inputLabel, inputValue, inputType, divWidthClass.toString(), inputMandatory, inputReadOnly);
		}

		public void setInput(String inputID, String inputName, String inputLabel, String inputValue, InputType inputType, String divWidthClass, boolean inputMandatory, boolean inputReadOnly) {

			this.inputID = inputID;
			this.inputName = inputName;
			this.inputLabel = inputLabel;
			this.inputValue = inputValue;

			this.inputType = inputType;

			this.divWidthClass = divWidthClass;

			this.inputMandatory = inputMandatory;
			this.inputReadOnly = inputReadOnly;

			this.portalForm = false;

			this.inputMaxLength = 0;
			this.inputRows = "";

			this.inputExtraClasses = "";
			this.inputExtraStyles = "";
			this.inputExtraAttrs = "";

			this.divExtraClasses = "";
			this.divExtraStyles = "";

			this.onBlur = "";
			this.onChange = "";
			this.onContextMenu = "";
			this.onFocus = "";
			this.onSelect = "";

			this.onKeyDown = "";
			this.onKeyPress = "";
			this.onKeyUp = "";

			this.onClick = "";
			this.onDoubleClick = "";

			this.onCopy = "";
			this.onPaste = "";

			if (this.inputOptions != null) {
				this.inputOptions.clear();
			}
			else {
				this.inputOptions = new ArrayList<InputOption>();
			}

			this.optionsInline = false;

			this.optionsTable = "";
			this.optionsLabel = "";
			this.optionsValue = "";
			this.optionsFilter = "";
			this.optionsOrderBy = "";
			this.helpBlock = "";
			this.buttonGroup = "";
		}

		public void addInputOption(String label, String value) {
			this.inputOptions.add(new InputOption(label, value, false));
		}

		public void addInputOption(String label, String value, boolean checked) {
			this.inputOptions.add(new InputOption(label, value, checked));
		}

		public void addInputOption(String label, String value, boolean checked, String extra) {
			this.inputOptions.add(new InputOption(label, value, checked, extra));
		}

		public String printInput() throws InputMaxLengthException {

			if (( this.inputType == InputType.TEXT || this.inputType == InputType.TEXTAREA || this.inputType == InputType.TEXTAREA_EDITOR ) && !this.inputReadOnly && this.inputMaxLength <= 0) {
				throw new InputMaxLengthException(this.inputID, this.inputName, this.inputType);
			}

			StringBuffer html = new StringBuffer();
			StringBuffer extras = new StringBuffer();

			if (this.inputMaxLength > 0) {
				extras.append(" maxlength='" + this.inputMaxLength + "' ");
			}
			if (this.inputRows.length() > 0) {
				extras.append(" rows='" + this.inputRows + "' ");
			}
			if (this.onBlur.length() > 0) {
				extras.append(" onblur=\"" + this.onBlur + "\" ");
			}
			if (this.onChange.length() > 0) {
				extras.append(" onchange=\"" + this.onChange + "\" ");
			}
			if (this.onClick.length() > 0) {
				extras.append(" onclick=\"" + this.onClick + "\" ");
			}
			if (this.onContextMenu.length() > 0) {
				extras.append(" oncontextmenu=\"" + this.onContextMenu + "\" ");
			}
			if (this.onCopy.length() > 0) {
				extras.append(" oncopy=\"" + this.onCopy + "\" ");
			}
			if (this.onDoubleClick.length() > 0) {
				extras.append(" ondblclick=\"" + this.onDoubleClick + "\" ");
			}
			if (this.onFocus.length() > 0) {
				extras.append(" onfocus=\"" + this.onFocus + "\" ");
			}
			if (this.onKeyDown.length() > 0) {
				extras.append(" onkeydown=\"" + this.onKeyDown + "\" ");
			}
			if (this.onKeyPress.length() > 0) {
				extras.append(" onkeypress=\"" + this.onKeyPress + "\" ");
			}
			if (this.onKeyUp.length() > 0) {
				extras.append(" onkeyup=\"" + this.onKeyUp + "\" ");
			}
			if (this.onPaste.length() > 0) {
				extras.append(" onpaste=\"" + this.onPaste + "\" ");
			}
			if (this.onSelect.length() > 0) {
				extras.append(" onselect=\"" + this.onSelect + "\" ");
			}
			if (this.inputExtraAttrs.length() > 0) {
				extras.append(" " + this.inputExtraAttrs + " ");
			}

			html.append("<div class='" + this.divWidthClass + " " + this.divExtraClasses + "'" + ( this.divExtraStyles.length() > 0 ? " style='" + this.divExtraStyles + "'" : "" ) + " id='_div_outer" + this.inputID + "'>\n");
			html.append("  <div class='form-group'>\n");

			if (portalForm) {
				html.append("    <label class='control-label col-lg-4 col-md-4'>" + this.inputLabel + ( inputMandatory ? "<span class='text-danger'> * </span>" : "" ) + "</label>\n");
			}
			else {
				html.append("    <label class='control-label'><strong>" + this.inputLabel + "</strong>" + ( inputMandatory ? "<span class='text-danger'> * </span>" : "" ) + "</label>\n");
			}

			if (portalForm) {
				html.append("    <div class='col-lg-8 col-md-8'>");
			}

			if (!this.buttonGroup.equals("")) {
				html.append("        <div class='input-group'>\n");
			}

			switch (this.inputType) {
				case CHECKBOX:
					html.append("    <div class='checkbox-list'>");
					for (InputOption opt : this.inputOptions) {
						html.append("      <label" + ( optionsInline ? " class='checkbox-inline'" : ( this.inputOptions.size() > 15 ? " class='col-md-6'" : "" ) ) + "><input type='checkbox' class='checkbox " + this.inputExtraClasses + "' name='" + this.inputName + "' id='" + this.inputID + "_" + opt.getValue() + "' value='" + opt.getValue() + "' " + ( opt.getValue().equals(this.inputValue) || opt.isChecked() ? "checked" : "" ) + ( this.inputReadOnly ? " disabled" : "" ) + ( extras.length() > 0 ? extras.toString() : "" ) + ( opt.getExtra().length() > 0 ? opt.getExtra().toString() : "" ) + " />" + opt.getLabel() + "</label>");
					}

					if (this.database != null && this.connection != null && this.optionsTable.length() > 0 && this.optionsValue.length() > 0 && this.optionsLabel.length() > 0) {
						ResultSet rs = database.openResultSet("SELECT " + this.optionsLabel + " AS LABEL, " + this.optionsValue + " AS VALUE FROM " + this.optionsTable + ( this.optionsFilter.length() > 0 ? " WHERE " + this.optionsFilter : "" ) + " ORDER BY " + ( this.optionsOrderBy.length() > 0 ? this.optionsOrderBy : this.optionsLabel ), this.connection);
						try {
							while (rs.next()) {
								html.append("      <label" + ( optionsInline ? " class='checkbox-inline'" : "" ) + "><input type='checkbox' class='checkbox " + this.inputExtraClasses + "' name='" + this.inputName + "' id='" + this.inputID + "_" + Database.verifyNull(rs.getObject("VALUE")) + "' value='" + Database.verifyNull(rs.getObject("VALUE")) + "' " + ( Database.verifyNull(rs.getObject("VALUE")).equals(this.inputValue) ? "checked" : "" ) + ( this.inputReadOnly ? " disabled" : "" ) + ( extras.length() > 0 ? extras.toString() : "" ) + " />" + Database.verifyNull(rs.getObject("LABEL")) + "</label>");
							}
						}
						catch (Exception e) {
							Utils.printSystemError("RADIO", e.getMessage());
						}
						finally {
							Database.closeObject(rs);
						}
					}

					html.append("    </div>");
					break;

				case EXISTING_FIELD:
					html.append(this.inputHTML);
					break;

				case FILE:
					html.append("    <div class='fileinput fileinput-new " + this.inputExtraClasses + "' data-provides='fileinput'" + ( this.inputExtraStyles.length() > 0 ? "style='" + this.inputExtraStyles + "'" : "" ) + ">");
					html.append("      <div class='input-group'>");
					html.append("        <div class='form-control uneditable-input span3' data-trigger='fileinput'>");
					html.append("          <i class='fa fa-file fileinput-exists'></i>&nbsp; <span class='fileinput-filename'></span>");
					html.append("        </div>");
					html.append("        <span class='input-group-addon btn default btn-file'>");
					html.append("          <span class='fileinput-new'> " + SystemIcons.ICON_UPLOAD + " </span>");
					html.append("          <span class='fileinput-exists'> " + SystemIcons.ICON_UPLOAD + " </span>");
					html.append("          <input type='file' name='" + this.inputName + "' id='" + this.inputID + "'" + ( extras.length() > 0 ? extras.toString() : "" ) + ">");
					html.append("        </span>");
					html.append("        <a href='javascript:;' class='input-group-addon btn red fileinput-exists' data-dismiss='fileinput'> <i class='fa fa-ban' style='color: #E5E5E5;'></i> </a>");
					html.append("      </div>");
					html.append("    </div>");
					break;

				case PASSWORD:
					html.append("    <input type='password' name='" + this.inputName + "' id='" + this.inputID + "' value='" + this.inputValue + "' class='form-control " + this.inputExtraClasses + "' " + ( this.inputExtraStyles.length() > 0 ? "style='" + this.inputExtraStyles + "'" : "" ) + ( extras.length() > 0 ? extras.toString() : "" ) + ( this.inputReadOnly ? " readonly" : "" ) + " autocomplete='off' />");
					break;

				case RADIO:
					html.append("    <div class='radio-list'>");
					for (InputOption opt : this.inputOptions) {
						html.append("      <label" + ( optionsInline ? " class='radio-inline'" : "" ) + "><input type='radio' class='radio " + this.inputExtraClasses + "' name='" + this.inputName + "' id='" + this.inputID + "_" + opt.getValue() + "' value='" + opt.getValue() + "' " + ( opt.getValue().equals(this.inputValue) || opt.isChecked() ? "checked" : "" ) + ( this.inputReadOnly ? " disabled" : "" ) + ( extras.length() > 0 ? extras.toString() : "" ) + " />" + opt.getLabel() + "</label>");
					}

					if (this.database != null && this.connection != null && this.optionsTable.length() > 0 && this.optionsValue.length() > 0 && this.optionsLabel.length() > 0) {
						ResultSet rs = database.openResultSet("SELECT " + this.optionsLabel + " AS LABEL, " + this.optionsValue + " AS VALUE FROM " + this.optionsTable + ( this.optionsFilter.length() > 0 ? " WHERE " + this.optionsFilter : "" ) + " ORDER BY " + ( this.optionsOrderBy.length() > 0 ? this.optionsOrderBy : this.optionsLabel ), this.connection);
						try {
							while (rs.next()) {
								html.append("      <label" + ( optionsInline ? " class='radio-inline'" : "" ) + "><input type='radio' class='radio " + this.inputExtraClasses + "' name='" + this.inputName + "' id='" + this.inputID + "_" + Database.verifyNull(rs.getObject("VALUE")) + "' value='" + Database.verifyNull(rs.getObject("VALUE")) + "' " + ( Database.verifyNull(rs.getObject("VALUE")).equals(this.inputValue) ? "checked" : "" ) + ( this.inputReadOnly ? " disabled" : "" ) + ( extras.length() > 0 ? extras.toString() : "" ) + " />" + Database.verifyNull(rs.getObject("LABEL")) + "</label>");
							}
						}
						catch (Exception e) {
							Utils.printSystemError("RADIO", e.getMessage());
						}
						finally {
							Database.closeObject(rs);
						}
					}

					html.append("    </div>");
					break;

				case SELECT:
					html.append("    <select name='" + this.inputName + "' id='" + this.inputID + "' class='form-control bs-select select2 " + this.inputExtraClasses + "' " + ( this.inputExtraStyles.length() > 0 ? "style='" + this.inputExtraStyles + "'" : "" ) + ( extras.length() > 0 ? extras.toString() : "" ) + ( this.inputReadOnly ? " readonly='readonly'" : "" ) + ">");

					for (InputOption opt : this.inputOptions) {
						html.append("        <option value='" + opt.getValue() + "' " + ( opt.getValue().equals(this.inputValue) ? "selected" : "" ) + ">" + opt.getLabel() + "</option>");
					}

					if (this.database != null && this.connection != null && this.optionsTable.length() > 0 && this.optionsValue.length() > 0 && this.optionsLabel.length() > 0) {
						ResultSet rs = database.openResultSet("SELECT " + this.optionsLabel + " AS LABEL, " + this.optionsValue + " AS VALUE FROM " + this.optionsTable + ( this.optionsFilter.length() > 0 ? " WHERE " + this.optionsFilter : "" ) + " ORDER BY " + ( this.optionsOrderBy.length() > 0 ? this.optionsOrderBy : this.optionsLabel ), this.connection);
						try {
							while (rs.next()) {
								html.append("        <option value='" + Database.verifyNull(rs.getObject("VALUE")) + "' " + ( Database.verifyNull(rs.getObject("VALUE")).equals(this.inputValue) ? "selected" : "" ) + ">" + Database.verifyNull(rs.getObject("LABEL")) + "</option>");
							}
						}
						catch (Exception e) {
							Utils.printSystemError("RADIO", e.getMessage());
						}
						finally {
							Database.closeObject(rs);
						}
					}

					html.append("    </select>");
					html.append("    <script type='text/javascript' src='metronic/global/plugins/select2/select2.js'></script>\n");
					html.append("    <script>\n");
					html.append("      jQuery(document).ready( function() {\n");
					html.append("        $('#" + this.inputID + "').select2({ minimumResultsForSearch: -1 });\n");
					if (this.inputReadOnly) {
						html.append("        $('#" + this.inputID + "').select2( 'readonly', true );\n");
					}
					html.append("      });\n");
					html.append("    </script>\n");
					break;

				case TEXTAREA:
					html.append("    <textarea name='" + this.inputName + "' id='" + this.inputID + "' class='form-control " + this.inputExtraClasses + "' " + ( this.inputExtraStyles.length() > 0 ? "style='" + this.inputExtraStyles + "'" : "" ) + ( extras.length() > 0 ? extras.toString() : "" ) + ( this.inputReadOnly ? " readonly" : "" ) + ">" + this.inputValue + "</textarea>");
					break;

				case TEXTAREA_EDITOR:
					html.append("    <script type='text/javascript' src='metronic/global/plugins/ckeditor/ckeditor.js'></script>\n");
					html.append("    <textarea name='" + this.inputName + "' id='" + this.inputID + "' data-error-container='#_erro_" + this.inputName + "' class='form-control ckeditor " + this.inputExtraClasses + "' " + ( this.inputExtraStyles.length() > 0 ? "style='" + this.inputExtraStyles + "'" : "" ) + ( extras.length() > 0 ? extras.toString() : "" ) + ( this.inputReadOnly ? " readonly" : "" ) + ">" + this.inputValue + "</textarea>");
					html.append("    <div id='_erro_" + this.inputName + "'></div>");
					break;

				default:
					html.append("    <input type='text' name='" + this.inputName + "' id='" + this.inputID + "' value='" + this.inputValue + "' class='form-control " + this.inputExtraClasses + "' " + ( this.inputExtraStyles.length() > 0 ? "style='" + this.inputExtraStyles + "'" : "" ) + ( extras.length() > 0 ? extras.toString() : "" ) + ( this.inputReadOnly ? " readonly" : "" ) + " />");
					break;
			}

			if (!this.helpBlock.equals("")) {
				html.append("      <p class='help-block'>\n");
				html.append(this.helpBlock);
				html.append("      </p>\n");
			}

			if (!this.buttonGroup.equals("")) {
				html.append("      <span class='input-group-btn'>\n");
				html.append(this.buttonGroup);
				html.append("      </span>\n");
				html.append("        </div>\n");
			}

			if (portalForm) {
				html.append("    </div>");
			}

			if (this.inputMaxLength > 0) {
				html.append("    <script>");
				html.append("      jQuery(document).ready( function() { $('#" + this.inputID + "').maxlength({ limitReachedClass: 'label label-danger', alwaysShow: true }); } );");
				html.append("    </script>");
			}

			html.append("  </div>");
			html.append("</div>");

			return html.toString();
		}
	}

	public class InputOption {
		private String label;
		private String value;
		private boolean checked;
		private String extra;

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public String getExtra() {
			return extra;
		}

		public void setExtra(String extra) {
			this.extra = extra;
		}

		public InputOption(String label, String value) {
			this.label = label;
			this.value = value;
			this.checked = false;
			this.extra = "";
		}

		public InputOption(String label, String value, boolean checked) {
			this.label = label;
			this.value = value;
			this.checked = checked;
			this.extra = "";
		}

		public InputOption(String label, String value, boolean checked, String extra) {
			this.label = label;
			this.value = value;
			this.checked = checked;
			this.extra = extra;
		}
	}

	public static String printHeaderEssentials(HttpServletRequest request, HttpServletResponse response, String pExtraTags) {
		StringBuffer header = new StringBuffer();

		// INICIO DO HEAD
		header.append("<head>\n");
		header.append("  <meta http-equiv='Content-Type' content='text/html; pageEncoding=" + response.getCharacterEncoding() + " charset=" + response.getCharacterEncoding() + "'>\n");
		header.append("  <title>" + SystemConfig.getSystemName() + " | The Citizens Brasil</title>\n");
		header.append("  <meta http-equiv='X-UA-Compatible' content='IE=edge'>\n");
		header.append("  <meta content='width=device-width, initial-scale=1.0' name='viewport'/>\n");
		header.append("  <meta content='The Citizens Brasil' name='description'/>\n");
		header.append("  <meta content='The Citizens Brasil' name='author'/>\n");

		// BEGIN GLOBAL MANDATORY STYLES
		header.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/open-sans/css/open-sans.css' />\n");
		header.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/font-awesome/css/font-awesome.min.css' />\n");
		header.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap/css/bootstrap.min.css' />\n");
		header.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/uniform/css/uniform.default.css' />\n");
		// END GLOBAL MANDATORY STYLES

		header.append(pExtraTags);

		// BEGIN THEME STYLES
		header.append("  <link rel='stylesheet' type='text/css' href='metronic/global/css/components.css' />\n");
		header.append("  <link rel='stylesheet' type='text/css' href='metronic/global/css/plugins.css' />\n");
		header.append("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/layout.css' />\n");
		header.append("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/themes/" + ( IniManipulation.getProperty(IniManipulation.LAYOUT_THEME).equalsIgnoreCase("light") ? "isg-light.css" : "isg-default.css" ) + "' />\n");
		header.append("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/custom.css' />\n");
		// END THEME STYLES

		header.append("  <link rel='shortcut icon' href='favicon.ico'/>\n");
		header.append("  <script src='metronic/global/plugins/jquery-1.11.0.min.js' type='text/javascript'></script>\n");
		header.append("</head>");

		return header.toString();
	}

	public static String printFooterEssentials(String pExtraTags) {
		StringBuffer footer = new StringBuffer();

		// BEGIN JAVASCRIPTS (LOAD JAVASCRIPTS AT BOTTOM, THIS WILL REDUCE PAGE LOAD TIME)
		// BEGIN CORE PLUGINS
		footer.append("  <!--[if lt IE 9]>\n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/excanvas.min.js'></script>\n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/respond.min.js'></script>\n");
		footer.append("  <![endif]-->\n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/jquery-migrate-1.2.1.min.js'></script>\n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js'></script> \n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/bootstrap/js/bootstrap.js'></script>\n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js'></script>\n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/jquery.blockui.min.js'></script>\n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/uniform/jquery.uniform.min.js'></script>\n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/bootbox/bootbox.min.js'></script>\n");
		footer.append("  <script type='text/javascript' src='metronic/global/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js'></script>\n");
		// END CORE PLUGINS

		footer.append(pExtraTags);

		// BEGIN PAGE LEVEL SCRIPTS
		footer.append("  <script type='text/javascript' src='metronic/global/scripts/metronic.js'></script>\n");
		footer.append("  <script type='text/javascript' src='metronic/admin/layout/scripts/layout.js'></script>\n");
		footer.append("  <script>\n");
		footer.append("    jQuery(document).ready(function() {\n");
		footer.append("      Metronic.init();\n");
		footer.append("      Layout.init();\n");
		footer.append("    });\n");
		footer.append("  </script>\n");
		// END JAVASCRIPTS

		return footer.toString();
	}

	public static String printUploadField(User pUser, String pIdent, boolean pDisabled, boolean pCountFiles) {
		return printUploadField(pUser, pIdent, pDisabled, pCountFiles, 0);
	}

	public static String printUploadField(User pUser, String pIdent, boolean pDisabled, boolean pCountFiles, int pInitialCount) {
		return printUploadField(pUser, pIdent, pDisabled, pCountFiles, 0, 0);
	}

	public static String printUploadField(User pUser, String pIdent, boolean pDisabled, boolean pCountFiles, int pInitialCount, long pMaxFileSize) {
		StringBuffer field = new StringBuffer();

		field.append("<script>\n");
		field.append("  var fieldCount_" + pIdent + " = " + pInitialCount + ";\n");
		field.append("  function add_file_upload_" + pIdent + "() {\n");
		field.append("    var objField = document.getElementById('_file_field_" + pIdent + "');\n");
		if (pMaxFileSize > 0) {
			field.append("    if(objField.value != null && objField.files[0].size/1024 > " + pMaxFileSize + "){");
			field.append("      bootbox.alert('" + pUser.getTermo("MSGARQUIVO") + "');");
			field.append("      objField.value = null;");
			field.append("      return;");
			field.append("    }");
		}
		field.append("    if (objField.value !== '') {\n");
		field.append("      fieldCount_" + pIdent + "++;\n");
		field.append("      objField.id = '_file_field_" + pIdent + "_' + fieldCount_" + pIdent + ";\n");
		field.append("      objField.name = '_file_field_" + pIdent + "';\n");
		field.append("      objField.style.display = 'none';\n");
		field.append("      add_label_" + pIdent + "(" + ( pCountFiles ? "\"<a href='javascript:;' onclick='showModalFiles(\\\"" + pIdent + "\\\");'>\" + fieldCount_" + pIdent + " + \" " + pUser.getTermo("ARQUIVOSANEXOS") + "</a>\"" : "objField.value" ) + ");\n");
		field.append("      $('#_div_upload_" + pIdent + "').append( \"<input type='file' style='positions: absolute; display: none;' id='_file_field_" + pIdent + "' onchange='add_file_upload_" + pIdent + "();'>\" );\n");
		field.append("    }\n");
		field.append("  }\n");
		field.append("  function add_label_" + pIdent + "(valor) {\n");
		field.append("    var html = \"<label class='form-label col-md-12' id='_file_labels_" + pIdent + "_\" + fieldCount_" + pIdent + " + \"'>\" + valor + \"" + ( pCountFiles ? "" : "&nbsp;&nbsp;<i class='fa fa-times font-red' style='cursor: pointer;' onclick='del_file_upload_" + pIdent + "(\" + fieldCount_" + pIdent + " + \");'></i>" ) + "</label>\";\n");
		if (pCountFiles) {
			field.append("    $('#_div_labels_" + pIdent + "').html( html );\n");
		}
		else {
			field.append("    $('#_div_labels_" + pIdent + "').append( html );\n");
		}
		field.append("  }\n");
		field.append("  function del_file_upload_" + pIdent + "(id) {\n");
		field.append("      fieldCount_" + pIdent + "--;\n");
		field.append("    $('#_file_field_" + pIdent + "_' + id).remove();\n");
		if (pCountFiles) {
			field.append("    $('#_div_labels_" + pIdent + "').html( \"<label class='form-label col-md-12' id='_file_labels_" + pIdent + "_\" + fieldCount_" + pIdent + " + \"'><a href='javascript:;' onclick='showModalFiles(\\\"" + pIdent + "\\\");'>\" + fieldCount_" + pIdent + " + \" " + pUser.getTermo("ARQUIVOSANEXOS") + "</a></label>\" );\n");
		}
		else {
			field.append("    $('#_file_labels_" + pIdent + "_' + id).remove();\n");
		}
		field.append("  }\n");
		field.append("</script>\n");
		field.append("<div class='form-group' id='_div_upload_" + pIdent + "' style='margin-bottom: 4px;'>\n");
		field.append("  <button type='button' " + ( pDisabled ? "class='btn btn-primary disabled'" : "class='btn btn-primary' onclick=\"$('#_file_field_" + pIdent + "').trigger('click');\"" ) + "> " + SystemIcons.ICON_UPLOAD + "&nbsp;" + pUser.getTermo("SELECIONE") + "... </button>" + ( pMaxFileSize > 0 ? "<br><span class='text-danger'>" + pUser.getTermo("MSGMAXFILESIZE").replaceAll("XXX", String.valueOf(pMaxFileSize)) + "</span>" : "" ) + "\n");
		field.append("  <input type='file' style='positions: absolute; display: none;' name='_file_field' id='_file_field_" + pIdent + "' onchange='add_file_upload_" + pIdent + "();'>\n");
		field.append("  <div id='_div_labels_" + pIdent + "'><label class='form-label col-md-12'>" + ( pCountFiles ? "<a href='javascript:;' onclick=\"showModalFiles('" + pIdent + "');\">" + pInitialCount + " " + pUser.getTermo("ARQUIVOSANEXOS") + "</a>" : "" ) + "</label></div>\n");
		field.append("</div>\n");

		return field.toString();
	}

	// OBJETO UTILIZADO PARA DEFINIÇÃO DE COLUNAS PARA TABELAS DE DADOS
	public class TableCell {
		private int colspan;
		private int rowspan;
		private String content;
		private String extraClasses;
		private String extraStyles;
		private String onClickJS;

		public TableCell() {
			// NOTHING TO DO HERE
		}

		public TableCell(String content, String extraClasses, String extraStyles) {
			this.colspan = 1;
			this.rowspan = 1;
			this.content = content;
			this.extraClasses = extraClasses;
			this.extraStyles = extraStyles;
			this.onClickJS = "";
		}

		public int getColspan() {
			return colspan;
		}

		public void setColspan(int colspan) {
			this.colspan = colspan;
		}

		public int getRowspan() {
			return rowspan;
		}

		public void setRowspan(int rowspan) {
			this.rowspan = rowspan;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getExtraClasses() {
			return extraClasses;
		}

		public void setExtraClasses(String extraClasses) {
			this.extraClasses = extraClasses;
		}

		public String getExtraStyles() {
			return extraStyles;
		}

		public void setExtraStyles(String extraStyles) {
			this.extraStyles = extraStyles;
		}

		public String getOnClickJS() {
			return onClickJS;
		}

		public void setOnClickJS(String onClickJS) {
			this.onClickJS = onClickJS;
		}

		public String printCell(boolean isHeader) {
			StringBuffer cell = new StringBuffer();

			if (isHeader) {
				cell.append("      <th");
			}
			else {
				cell.append("      <td");
			}

			if (this.colspan > 1) {
				cell.append(" colspan='");
				cell.append(this.colspan);
				cell.append("' ");
			}

			if (this.rowspan > 1) {
				cell.append(" rowspan='");
				cell.append(this.rowspan);
				cell.append("' ");
			}

			if (!this.extraClasses.trim().equals("")) {
				cell.append(" class='");
				cell.append(this.extraClasses);
				cell.append("'");
			}

			if (!this.extraStyles.trim().equals("")) {
				cell.append(" style='");
				cell.append(this.extraStyles);
				cell.append("'");
			}

			if (!this.onClickJS.trim().equals("")) {
				cell.append(" onclick=\"");
				cell.append(onClickJS);
				cell.append("\"");
			}

			cell.append(">\n");
			cell.append(this.content);

			if (isHeader) {
				cell.append("      </th>\n");
			}
			else {
				cell.append("      </td>\n");
			}

			return cell.toString();
		}
	}

	// OBJETO UTILIZADO PARA DEFINIÇÃO DE LINHAS PARA TABELAS DE DADOS
	public class TableRow {
		private String id;
		private String onClickJS;
		private String extraClasses;

		private TableCell cell;
		private ArrayList<TableCell> cellList;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getOnClickJS() {
			return onClickJS;
		}

		public void setOnClickJS(String onClickJS) {
			this.onClickJS = onClickJS;
		}

		public String getExtraClasses() {
			return extraClasses;
		}

		public void setExtraClasses(String extraClasses) {
			this.extraClasses = extraClasses;
		}

		public ArrayList<TableCell> getCellList() {
			return cellList;
		}

		public void setCellList(ArrayList<TableCell> cellList) {
			this.cellList = cellList;
		}

		public TableRow() {
			this.id = "";
			this.onClickJS = "";
			this.extraClasses = "";
			this.cellList = new ArrayList<TableCell>();
		}

		public TableCell addCell(String content, String extraClasses, String extraStyles) {
			return addCell(content, extraClasses, extraStyles, "");
		}

		public TableCell addCell(String content, String extraClasses, String extraStyles, String onClickJS) {
			cell = new TableCell(content, extraClasses, extraStyles);
			cell.setOnClickJS(onClickJS);
			cellList.add(cell);

			return this.cell;
		}

		public String printRow(boolean isHeader) {
			StringBuffer row = new StringBuffer();

			row.append("    <tr");

			if (!this.id.trim().equals("")) {
				row.append(" id='");
				row.append(this.id);
				row.append("'");
			}

			if (!this.onClickJS.trim().equals("")) {
				row.append(" onclick=\"");
				row.append(this.onClickJS);
				row.append("\"");
			}

			if (!this.extraClasses.trim().equals("")) {
				row.append(" class=\"");
				row.append(this.extraClasses);
				row.append("\"");
			}

			row.append(">\n");

			for (TableCell cell : cellList) {
				row.append(cell.printCell(isHeader));
			}

			row.append("    </tr>\n");

			return row.toString();
		}
	}

	// OBJETO UTILIZADO PARA CRIAÇÃO DE TABELAS DE DADOS
	public class DataTable {
		private String id;
		private String emptyMessage;
		private String extraClasses;
		private String extraStyles;

		private boolean ordering;

		private ArrayList<TableRow> tableHead;
		private ArrayList<TableRow> tableBody;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getEmptyMessage() {
			return emptyMessage;
		}

		public void setEmptyMessage(String emptyMessage) {
			this.emptyMessage = emptyMessage;
		}

		public String getExtraClasses() {
			return extraClasses;
		}

		public void setExtraClasses(String extraClasses) {
			this.extraClasses = extraClasses;
		}

		public String getExtraStyles() {
			return extraStyles;
		}

		public void setExtraStyles(String extraStyles) {
			this.extraStyles = extraStyles;
		}

		public boolean isOrdering() {
			return ordering;
		}

		public void setOrdering(boolean ordering) {
			this.ordering = ordering;
		}

		public DataTable() {
			this.id = "isgTableDefault";
			this.emptyMessage = "";
			this.extraClasses = "";
			this.extraStyles = "";

			this.ordering = true;

			this.tableHead = new ArrayList<TableRow>();
			this.tableBody = new ArrayList<TableRow>();
		}

		public void setDataTable(String id, String extraClasses, String extraStyles) {
			this.id = id;
			this.extraClasses = extraClasses;
			this.extraStyles = extraStyles;

			this.ordering = true;

			this.tableHead.clear();
			this.tableBody.clear();
		}

		public void addRow(TableRow row, boolean isHeader) {
			if (isHeader) {
				tableHead.add(row);
			}
			else {
				tableBody.add(row);
			}
		}

		public String printDataTable() {
			return this.printDataTable(true);
		}

		public String printDataTable(boolean printScripts) {
			StringBuffer table = new StringBuffer();

			table.append("<table class='table table-striped table-bordered table-hover table-scrollable ");

			if (!this.extraClasses.trim().equals("")) {
				table.append(this.extraClasses);
			}

			table.append("'");

			if (!this.extraStyles.trim().equals("")) {
				table.append(" style='");
				table.append(this.extraStyles);
				table.append("'");
			}

			if (!this.id.trim().equals("")) {
				table.append(" id='");
				table.append(this.id);
				table.append("'");
			}

			table.append(">\n");

			if (this.tableHead.size() > 0) {
				table.append("  <thead>\n");
				for (TableRow row : tableHead) {
					table.append(row.printRow(true));
				}
				table.append("  </thead>\n");
			}
			if (this.tableBody.size() > 0) {
				table.append("  <tbody>\n");
				for (TableRow row : tableBody) {
					table.append(row.printRow(false));
				}
				table.append("  </tbody>\n");
			}

			table.append("</table>\n");

			if (printScripts) {
				table.append("<script type='text/javascript' src='metronic/global/plugins/data-tables/jquery.dataTables.js'></script>\n");
				table.append("<script type='text/javascript' src='metronic/global/plugins/data-tables/DT_bootstrap.js'></script>\n");
				table.append("<script>\n");
				table.append("  jQuery(document).ready(function() { \n");
				table.append(this.printScriptsOnly());
				table.append("  }); \n");
				table.append("</script>\n");
			}

			return table.toString();
		}

		public String printScriptsOnly() {
			StringBuffer scripts = new StringBuffer();

			scripts.append("    $('#" + this.id + "').dataTable({ \n");
			scripts.append("      'bAutoWidth': false, \n");
			scripts.append("      'bInfo': false, \n");
			scripts.append("      'bFilter': false, \n");
			scripts.append("      'bPaginate': false, \n");
			scripts.append("      'bSort': " + this.ordering + ", \n");
			scripts.append("      'oLanguage': { \n");
			scripts.append("        'sEmptyTable': '" + this.emptyMessage + "', \n");
			scripts.append("        'sInfoEmpty': '" + this.emptyMessage + "', \n");
			scripts.append("        'sZeroRecords': '" + this.emptyMessage + "' \n");
			scripts.append("      } \n");
			scripts.append("    }); \n");

			return scripts.toString();
		}
	}

	public class RecaptchaResult {

		private boolean success;
		private String[] errorCodes;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String[] getErrorCodes() {
			return errorCodes;
		}

		public void setErrorCodes(String[] errorCodes) {
			this.errorCodes = errorCodes;
		}

	}

	public class FullCalendar {
		private User user;

		private String calendarID;
		private String calendarIcon;
		private String calendarTitle;
		private String calendarColor;

		private String eventColor;
		private String eventCompanyCode;
		private String eventCompanyField;
		private String eventTargetPage;
		private String eventTable; // TABELA DOS EVENTOS DO CALENDARIO
		private String eventFieldCode; // CAMPO DO CÓDIGO DO EVENTO
		private String eventFieldDesc; // CAMPO DE DESCRIÇÃO DE EXIBIÇÃO DO EVENTO
		private String eventFieldDate; // CAMPO DE DATA DA EXIBIÇÃO DO EVENTO
		private String eventFieldColor; // CAMPO DE COR ESPECÍFICA DE CADA EVENTO VINDO DO BANCO DE DADOS

		public String getCalendarID() {
			return calendarID;
		}

		public void setCalendarID(String calendarID) {
			this.calendarID = calendarID;
		}

		public String getCalendarIcon() {
			return calendarIcon;
		}

		public void setCalendarIcon(String calendarIcon) {
			this.calendarIcon = calendarIcon;
		}

		public String getCalendarTitle() {
			return calendarTitle;
		}

		public void setCalendarTitle(String calendarTitle) {
			this.calendarTitle = calendarTitle;
		}

		public String getCalendarColor() {
			return calendarColor;
		}

		public void setCalendarColor(String calendarColor) {
			this.calendarColor = calendarColor;
		}

		public String getEventColor() {
			return eventColor;
		}

		public void setEventColor(String eventColor) {
			this.eventColor = eventColor;
		}

		public String getEventCompanyCode() {
			return eventCompanyCode;
		}

		public void setEventCompanyCode(String eventCompanyCode) {
			this.eventCompanyCode = eventCompanyCode;
		}

		public String getEventCompanyField() {
			return eventCompanyField;
		}

		public void setEventCompanyField(String eventCompanyField) {
			this.eventCompanyField = eventCompanyField;
		}

		public String getEventTargetPage() {
			return eventTargetPage;
		}

		public void setEventTargetPage(String eventTargetPage) {
			this.eventTargetPage = eventTargetPage;
		}

		public String getEventTable() {
			return eventTable;
		}

		public void setEventTable(String eventTable) {
			this.eventTable = eventTable;
		}

		public String getEventFieldCode() {
			return eventFieldCode;
		}

		public void setEventFieldCode(String eventFieldCode) {
			this.eventFieldCode = eventFieldCode;
		}

		public String getEventFieldDesc() {
			return eventFieldDesc;
		}

		public void setEventFieldDesc(String eventFieldDesc) {
			this.eventFieldDesc = eventFieldDesc;
		}

		public String getEventFieldDate() {
			return eventFieldDate;
		}

		public void setEventFieldDate(String eventFieldDate) {
			this.eventFieldDate = eventFieldDate;
		}

		public String getEventFieldColor() {
			return eventFieldColor;
		}

		public void setEventFieldColor(String eventFieldColor) {
			this.eventFieldColor = eventFieldColor;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public FullCalendar(User user) {
			this.user = user;

			this.calendarID = "myFullCalendar";
			this.calendarColor = DataList.FAROL_BLUE_STEEL;
			this.calendarIcon = SystemIcons.ICON_CALENDAR;
			this.calendarTitle = user.getTermo("CALENDARIO");
		}

		public void setFullCalendar(String calendarID, String calendarTitle) {
			this.calendarID = calendarID;
			this.calendarTitle = calendarTitle;
		}

		public String printFullCalendar() {
			StringBuffer str = new StringBuffer();

			str.append("<div class='portlet box " + this.calendarColor + " calendar'>\n");
			str.append("  <div class='portlet-title'>\n");
			str.append("    <div class='caption'>\n");
			str.append(this.calendarIcon);
			str.append("&nbsp;");
			str.append(this.calendarTitle);
			str.append("    </div>\n");
			str.append("  </div>\n");
			str.append("  <div class='portlet-body light-grey'>\n");
			str.append("    <div id='" + this.calendarID + "'></div>\n");
			str.append("  </div>\n");
			str.append("</div>\n");

			str.append("<script type='text/javascript' src='metronic/global/plugins/fullcalendar/fullcalendar/fullcalendar.min.js'></script>\n");
			str.append("<script>\n");
			str.append("  jQuery(document).ready( function() {\n");
			str.append("    if (!jQuery().fullCalendar) {\n");
			str.append("      top.bootbox.alert('" + this.user.getTermo("MSGERRODISPLAY") + "'); \n");
			str.append("    }\n");
			str.append("    else {\n");
			str.append("      var h = {}\n");
			str.append("      if ($('#" + this.calendarID + "').width() <= 400) {\n");
			str.append("        $('#" + this.calendarID + "').addClass('mobile');\n");
			str.append("        h = {\n");
			str.append("          left: 'title, prev, next',\n");
			str.append("          center: '',\n");
			str.append("          right: 'today, month, agendaWeek, agendaDay'\n");
			str.append("        };\n");
			str.append("      }\n");
			str.append("      else {\n");
			str.append("        $('#" + this.calendarID + "').removeClass('mobile');\n");
			str.append("        if (Metronic.isRTL()) {\n");
			str.append("          h = {\n");
			str.append("            right: 'title',\n");
			str.append("            center: '',\n");
			str.append("            left: 'prev, next, today, month, agendaWeek, agendaDay'\n");
			str.append("          };\n");
			str.append("        }\n");
			str.append("        else {\n");
			str.append("          h = {\n");
			str.append("            left: 'title',\n");
			str.append("            center: '',\n");
			str.append("            right: 'prev, next, today, month, agendaWeek, agendaDay'\n");
			str.append("          };\n");
			str.append("        }\n");
			str.append("      }\n");
			str.append("      $('#" + this.calendarID + "').fullCalendar('destroy');");
			str.append("      $('#" + this.calendarID + "').fullCalendar({\n");
			str.append("        buttonText: {\n");
			str.append("          today: '" + user.getTermo("HOJE") + "',\n");
			str.append("          month: '" + user.getTermo("MES") + "',\n");
			str.append("          week: '" + user.getTermo("SEMANA") + "',\n");
			str.append("          day: '" + user.getTermo("DIA") + "'\n");
			str.append("        },\n");
			str.append("        dayNames: ['" + user.getTermo("DOMINGO") + "', '" + user.getTermo("SEGUNDAFEIRA") + "', '" + user.getTermo("TERCAFEIRA") + "', '" + user.getTermo("QUARTAFEIRA") + "', '" + user.getTermo("QUINTAFEIRA") + "', '" + user.getTermo("SEXTAFEIRA") + "', '" + user.getTermo("SABADO") + "'],\n");
			str.append("        dayNamesShort: ['" + user.getTermo("DOMINGO").substring(0, 3) + "', '" + user.getTermo("SEGUNDAFEIRA").substring(0, 3) + "', '" + user.getTermo("TERCAFEIRA").substring(0, 3) + "', '" + user.getTermo("QUARTAFEIRA").substring(0, 3) + "', '" + user.getTermo("QUINTAFEIRA").substring(0, 3) + "', '" + user.getTermo("SEXTAFEIRA").substring(0, 3) + "', '" + user.getTermo("SABADO").substring(0, 3) + "'],\n");
			str.append("        editable: false,\n");
			str.append("        firstDay: 0,\n");
			str.append("        monthNames: ['" + user.getTermo("JANEIRO") + "', '" + user.getTermo("FEVEREIRO") + "', '" + user.getTermo("MARCO") + "', '" + user.getTermo("ABRIL") + "', '" + user.getTermo("MAIO") + "', '" + user.getTermo("JUNHO") + "', '" + user.getTermo("JULHO") + "', '" + user.getTermo("AGOSTO") + "', '" + user.getTermo("SETEMBRO") + "', '" + user.getTermo("OUTUBRO") + "', '" + user.getTermo("NOVEMBRO") + "', '" + user.getTermo("DEZEMBRO") + "'],\n");
			str.append("        monthNamesShort: ['" + user.getTermo("JANEIRO").substring(0, 3) + "', '" + user.getTermo("FEVEREIRO").substring(0, 3) + "', '" + user.getTermo("MARCO").substring(0, 3) + "', '" + user.getTermo("ABRIL").substring(0, 3) + "', '" + user.getTermo("MAIO").substring(0, 3) + "', '" + user.getTermo("JUNHO").substring(0, 3) + "', '" + user.getTermo("JULHO").substring(0, 3) + "', '" + user.getTermo("AGOSTO").substring(0, 3) + "', '" + user.getTermo("SETEMBRO").substring(0, 3) + "', '" + user.getTermo("OUTUBRO").substring(0, 3) + "', '" + user.getTermo("NOVEMBRO").substring(0, 3) + "', '" + user.getTermo("DEZEMBRO").substring(0, 3) + "'],\n");
			str.append("        weekNumberTitle: '" + user.getTermo("SEMANA") + "',\n");

			str.append("        events: { \n");
			str.append("          url: 'ccalendarjson', \n");
			str.append("          data: function() { \n");
			str.append("            return { \n");
			str.append("              code: '" + this.eventFieldCode + "', \n");
			str.append("              color: '" + ( this.eventFieldColor.trim().equals("") ? "\'#4B77BE\'" : this.eventFieldColor ) + "', \n");
			str.append("              companyCode: '" + ( this.eventCompanyCode.length() > 0 ? this.eventCompanyCode : "" ) + "', \n");
			str.append("              companyField: '" + ( this.eventCompanyField.length() > 0 ? this.eventCompanyField : "" ) + "', \n");
			str.append("              date: '" + this.eventFieldDate + "', \n");
			str.append("              descript: '" + this.eventFieldDesc + "', \n");
			str.append("              month: getCalendarMonth(), \n");
			str.append("              table: '" + this.eventTable + "', \n");
			str.append("              targetPage: '" + this.eventTargetPage + "', \n");
			str.append("              year: getCalendarYear() \n");
			str.append("            }; \n");
			str.append("          }, \n");
			str.append("          error: function() { \n");
			str.append("            top.bootbox.alert('" + this.user.getTermo("MSGERRODISPLAY") + "'); \n");
			str.append("          } \n");
			str.append("        },\n");

			if (this.eventFieldColor.trim().equals("")) {
				str.append("        eventColor: '" + ( this.eventColor.trim().equals("") ? this.eventColor : "#4B77BE" ) + "',\n");
			}

			str.append("        eventClick: function(event) {\n");
			str.append("          if (event.url) {\n");
			str.append("            var _frm = document.forms['_frm'];");
			str.append("            if ( _frm ) {\n");
			str.append("              _frm.action = event.url;");
			str.append("              _frm.method = 'POST';");
			str.append("              _frm.target = '_self';");
			str.append("              _frm.submit();");
			str.append("            }");
			str.append("            else if ( document.forms[0] ) {\n");
			str.append("              document.forms[0].action = event.url;");
			str.append("              document.forms[0].method = 'POST';");
			str.append("              document.forms[0].target = '_self';");
			str.append("              document.forms[0].submit();");
			str.append("            }");
			str.append("            return false;\n");
			str.append("          }\n");
			str.append("        },\n");

			str.append("        eventMouseover: function(event, jsEvent, view) {\n");
			str.append("          this.style.cursor = 'pointer';\n");
			str.append("        },\n");

			str.append("        eventMouseout: function(event, jsEvent, view) {\n");
			str.append("          this.style.cursor = 'default';\n");
			str.append("        }\n");

			str.append("      });\n");
			str.append("    }\n");
			str.append("  });\n");

			str.append("  function getCalendarMonth() { \n");
			str.append("    try { \n");
			str.append("      return $('#" + this.calendarID + "').fullCalendar('getDate').getMonth() + 1; \n");
			str.append("    } \n");
			str.append("    catch(e){ \n");
			str.append("      return (new Date().getMonth() + 1); \n");
			str.append("    } \n");
			str.append("  } \n");

			str.append("  function getCalendarYear() {");
			str.append("    try { \n");
			str.append("      return $('#" + this.calendarID + "').fullCalendar('getDate').getFullYear(); \n");
			str.append("    } \n");
			str.append("    catch(e){ \n");
			str.append("      return (new Date().getFullYear()); \n");
			str.append("    } \n");
			str.append("  }");
			str.append("</script>\n");

			return str.toString();
		}
	}

	public class Legend {
		private HashMap<String, String> legends;

		public Legend() {
			this.legends = new HashMap<String, String>();
		}

		public void add(String pColor, String pLabel) {
			legends.put(pColor, pLabel);
		}

		public String printLegend(String pTitle) {
			if (legends.size() > 0) {
				StringBuffer legendas = new StringBuffer();

				legendas.append("<div class='clear'>\n");
				legendas.append("  <table class='table table-bordered' style='width:100%;'>\n");
				legendas.append("    <tr>\n");
				legendas.append("      <td style='text-align:center;'><strong>" + pTitle + "</strong></td>\n");
				legendas.append("      <td style='width:100%;'>\n");

				for (String key : this.legends.keySet()) {
					legendas.append("      <span><span class='label bg-" + key + "'>&nbsp;&nbsp;&nbsp;</span> " + legends.get(key) + " </span>&nbsp;&nbsp;\n");
				}

				legendas.append("      </td>\n");
				legendas.append("    </tr>\n");
				legendas.append("  </table>\n");
				legendas.append("</div>\n");

				return legendas.toString();
			}

			return "";
		}
	}

	public static String printTypeAhead(User user, String inputID, String inputName, String inputValue, int inputMaxLength, boolean inputReadOnly, String ajaxURL, String openJS, String inputExtraClasses, String inputExtraStyles) {
		StringBuffer input = new StringBuffer();

		input.append("<script type='text/javascript' src='metronic/global/plugins/typeahead.bundle.js'></script>\n");
		input.append("<script type='text/javascript' src='metronic/global/plugins/handlebars-v4.0.5.js'></script>\n");
		input.append("<input type='text' name='" + inputName + "' id='" + inputID + "' value='" + inputValue + "' class='form-control typeahead" + ( inputExtraClasses.length() > 0 ? " " + inputExtraClasses : "" ) + "' " + ( inputExtraStyles.length() > 0 ? "style='" + inputExtraStyles + "'" : "" ) + ( inputReadOnly ? " readonly" : "" ) + " autocomplete='off' />\n");

		input.append("<style>\n");
		input.append("  span.twitter-typeahead {\n");
		input.append("    width: 100%;\n");
		input.append("  }\n");
		input.append("  input.form-control.typeahead {\n");
		input.append("    max-height: 150px;\n");
		input.append("    overflow-y: auto;\n");
		input.append("  }\n");
		input.append("  .tt-menu {\n");
		input.append("    max-height: 256px;\n");
		input.append("    overflow-y: auto;\n");
		input.append("  }\n");
		input.append("</style>\n");

		input.append("<script>\n");
		input.append("  remoteSource = new Bloodhound({\n");
		input.append("    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),\n");
		input.append("    queryTokenizer: Bloodhound.tokenizers.whitespace,\n");
		input.append("    remote: {\n");
		input.append("      url: '" + ajaxURL + ( ajaxURL.contains("?") ? "&" : "?" ) + "typeSearch=%QUERY',\n");
		input.append("      wildcard: '%QUERY'\n");
		input.append("    }\n");
		input.append("  });\n");

		input.append("  $('#" + inputID + "').typeahead(\n");
		input.append("    {\n");
		input.append("      highlight: true \n");
		// input.append(" highlight: true,\n");
		// input.append(" minLength: 3\n");
		input.append("    }, {\n");
		input.append("      display: 'value',\n");
		input.append("      limit: 15,\n");
		input.append("      name: '" + inputName + "',\n");
		input.append("      source: remoteSource,\n");
		input.append("      templates: {\n");
		input.append("        header: \"<div class='col-md-12'><h5><b>" + user.getTermo("SUGESTAOREGISTRO") + "</b></h5></div>\",\n");
		input.append("        suggestion: Handlebars.compile( " + inputID + "_suggestion() )\n");
		input.append("      }\n");
		input.append("    }\n");
		input.append("  );\n");

		input.append("  $('#" + inputID + "').bind('typeahead:select', function(ev, suggestion) {\n");
		input.append("    " + openJS + "( ev, suggestion );\n");
		input.append("  });\n");

		input.append("  function " + inputID + "_suggestion() {");
		input.append("    try { return " + inputID + "_suggestion_format(); } catch(e) { return \"<div class='col-md-12'>{{value}}</div>\"; }");
		input.append("  }\n");
		input.append("</script>\n");

		return input.toString();
	}

	public static String printGoogleMaps(User user, String mapID, String mapLatitudeID, String mapLatitudeName, double mapLatitudeValue, String mapLongitudeID, String mapLongitudeName, double mapLongitudeValue, String mapAddressValue, int mapPointerRadius, int mapHeight, boolean mapReadOnly, String mapUpdateJS) {
		StringBuffer map = new StringBuffer();

		if (!Database.verifyNull(mapUpdateJS).trim().equals("")) {
			map.append("<div class='input-group'>\n");
		}
		map.append("<input type='text' id='_address" + mapID + "' value='' style='maring-bottom: 8px;' class='form-control " + "' " + ( mapReadOnly ? " readonly" : "" ) + " />");
		if (!Database.verifyNull(mapUpdateJS).trim().equals("")) {
			map.append("  <span class='input-group-btn'>\n");
			map.append(printButton("_button" + mapID + "_update", "", SystemIcons.ICON_MAP_MARKER, "red-intense", "", mapUpdateJS, mapReadOnly));
			map.append("  </span>\n");
			map.append("</div>\n");
		}

		map.append("<div id='" + mapID + "' style='margin-bottom: 8px; margin-top: 8px; width: 100%; height: " + mapHeight + "px;'></div>\n");

		map.append("<div class='col-md-6'>\n");
		map.append("  <div class='form-group'>\n");
		map.append("    <label class='control-label'> " + user.getTermo("LATITUDE") + " </label>\n");
		map.append("    <input type='text' maxlength='36' name='" + mapLatitudeName + "' id='" + mapLatitudeID + "' value='" + mapLatitudeValue + "' class='form-control " + "' " + ( mapReadOnly ? " readonly" : "" ) + " />");
		map.append("  </div>");
		map.append("</div>");

		map.append("<div class='col-md-6'>\n");
		map.append("  <div class='form-group'>\n");
		map.append("    <label class='control-label'> " + user.getTermo("LONGITUDE") + " </label>\n");
		map.append("    <input type='text' maxlength='36' name='" + mapLongitudeName + "' id='" + mapLongitudeID + "' value='" + mapLongitudeValue + "' class='form-control " + "' " + ( mapReadOnly ? " readonly" : "" ) + " />");
		map.append("  </div>");
		map.append("</div>");

		map.append("<script type='text/javascript' src='http://maps.google.com/maps/api/js?sensor=false&libraries=places'></script>\n");
		map.append("<script type='text/javascript' src='metronic/global/plugins/locationpicker.jquery.js'></script>\n");
		map.append("<script type='text/javascript' src='metronic/global/plugins/numericInput.min.js'></script>\n");

		map.append("<script>\n");
		map.append("  $('#" + mapLatitudeID + "').numericInput({ allowFloat: true, allowNegative: true });");
		map.append("  $('#" + mapLongitudeID + "').numericInput({ allowFloat: true, allowNegative: true });");
		map.append("  $('#" + mapID + "').locationpicker({\n");
		map.append("    enableAutocomplete: true,\n");
		map.append("    enableReverseGeocode: true,\n");
		map.append("    inputBinding: {\n");
		map.append("      latitudeInput: $('#" + mapLatitudeID + "'),\n");
		map.append("      longitudeInput: $('#" + mapLongitudeID + "'),\n");
		map.append("      locationNameInput: $('#_address" + mapID + "')\n");
		map.append("    },\n");
		map.append("    location: { latitude: " + mapLatitudeValue + ", longitude: " + mapLongitudeValue + " },\n");
		map.append("    radius: " + mapPointerRadius + ",\n");
		map.append("    scrollwheel: true,\n");
		map.append("    onchanged: function( currentLocation, radius, isMarkerDropped ) {\n");
		map.append("      if (currentLocation.latitude >= 86 || currentLocation.latitude <= -86) {\n");
		map.append("        top.bootbox.alert('" + user.getTermo("MSGLOCALIZACAO") + "', function() {\n");
		map.append("          $('#" + mapID + "').locationpicker('location', { latitude: " + mapLatitudeValue + ", longitude: " + mapLatitudeValue + " });\n");
		map.append("          $('#" + mapID + "').locationpicker('map').map.setZoom(1);");
		map.append("        });\n");
		map.append("      }");
		map.append("    }, \n");
		map.append("    zoom: " + ( mapLatitudeValue == 0 && mapLongitudeValue == 0 ? 2 : 12 ) + " \n");
		map.append("  });\n");
		if (mapAddressValue.length() > 0) {
			map.append("  var geocoder = new google.maps.Geocoder();\n");
			map.append("  geocoder.geocode({ 'address': '" + mapAddressValue + "' }, function(results, status) {\n");
			map.append("    if (status == google.maps.GeocoderStatus.OK) {\n");
			map.append("      $('#" + mapID + "').locationpicker('location', { latitude: results[0].geometry.location.lat(), longitude: results[0].geometry.location.lng() });\n");
			map.append("      $('#" + mapID + "').locationpicker('map').map.setZoom(12);");
			map.append("    }\n");
			map.append("  });\n");
		}
		map.append("</script>\n");

		return map.toString();
	}
}
