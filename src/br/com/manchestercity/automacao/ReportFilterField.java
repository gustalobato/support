
package br.com.manchestercity.automacao;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import br.com.manchestercity.automacao.FrameworkDefaults.ColumnWidth;
import br.com.manchestercity.automacao.FrameworkDefaults.FormInput;
import br.com.manchestercity.automacao.FrameworkDefaults.FormState;
import br.com.manchestercity.automacao.FrameworkDefaults.InputOption;
import br.com.manchestercity.automacao.FrameworkDefaults.InputType;
import br.com.manchestercity.automacao.FrameworkDefaults.ReportFieldType;

public class ReportFilterField {

	private HttpServletRequest request;

	private User user;

	private FrameworkDefaults defaults;

	private FormInput input;
	private DatePicker dtp;
	private SmartCombo combo;
	private SmartComboList comboList;

	// ATRIBUTOS ESPECIFICOS DO CAMPO DE FILTRO
	private ReportFieldType type;

	private int order;
	private int filterGroup;

	private boolean show;
	private boolean interval;
	private boolean mandatory;
	private boolean filterDescription;

	private String filterKey;

	private String filterTable;
	private String filterColumn;
	private String filterValue;
	private String defaultValue;

	private DynamicSubquery dinamicSubquery;

	private ArrayList<String> campos;
	private ArrayList<String> camposCLOB;

	private ReportFilter parentFilter;

	public ReportFilterField(HttpServletRequest request, User user, ReportFieldType type) {
		show = false;
		interval = false;
		mandatory = false;
		filterDescription = true;

		filterTable = "";
		filterColumn = "";
		filterValue = "";
		defaultValue = "";

		campos = new ArrayList<String>();
		camposCLOB = new ArrayList<String>();

		defaults = new FrameworkDefaults();
		input = defaults.new FormInput();

		this.request = request;

		this.user = user;

		this.type = type;

		switch (this.type) {
			case CHECKBOX:
				input.setInputType(InputType.CHECKBOX);
				input.setOptionsInline(true);
				break;

			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				dtp = new DatePicker(this.user);
				input.setInputType(InputType.EXISTING_FIELD);
				break;

			case DECIMAL:
				input.setInputType(InputType.TEXT);
				input.setInputMaxLength(12);
				input.setInputExtraClasses("text-right");
				input.setOnBlur("FormataValor(this, event, 2, 12);");
				input.setOnKeyUp("FormataValor(this, event, 2, 12);");
				break;

			case INTEGER:
				input.setInputType(InputType.TEXT);
				input.setInputMaxLength(12);
				input.setInputExtraClasses("text-right");
				input.setOnKeyPress("MascaraInteiro();");
				break;

			case RADIO:
				input.setInputType(InputType.RADIO);
				input.setOptionsInline(true);
				break;

			case SELECT:
				input.setInputType(InputType.SELECT);
				break;

			case SMARTCOMBO:
				input.setInputType(InputType.EXISTING_FIELD);
				combo = new SmartCombo(this.user);
				combo.setAutoFill(false);
				break;

			case SMARTCOMBO_LIST:
				input.setInputType(InputType.EXISTING_FIELD);
				comboList = new SmartComboList(this.user, this.request);
				comboList.setAutoFill(false);
				comboList.setShowRadio(true);
				comboList.setIsLoad(true);
				break;

			case TEXTAREA:
				input.setInputType(InputType.TEXTAREA);
				input.setInputMaxLength(4000);
				break;

			default:
				input.setInputType(InputType.TEXT);
				input.setInputMaxLength(128);
				break;
		}
	}

	public ReportFieldType getType() {
		return type;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getFilterGroup() {
		return filterGroup;
	}

	public void setFilterGroup(int filterGroup) {
		this.filterGroup = filterGroup;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isInterval() {
		return interval;
	}

	public void setInterval(boolean interval) {
		switch (this.type) {
			case CHECKBOX:
			case KEYWORD:
			case RADIO:
			case SELECT:
			case SMARTCOMBO:
			case SMARTCOMBO_LIST:
			case TEXT:
			case TEXTAREA:
				this.interval = false;
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade Interval");
				break;

			default:
				this.interval = interval;
				break;
		}
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
		this.input.setInputMandatory(true);
	}

	public boolean isFilterDescription() {
		return filterDescription;
	}

	public void setFilterDescription(boolean filterDescription) {
		this.filterDescription = filterDescription;
	}

	public String getFilterKey() {
		return filterKey;
	}

	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	public String getFilterTable() {
		return filterTable;
	}

	public void setFilterTable(String filterTable) {
		this.filterTable = filterTable;
	}

	public String getFilterColumn() {
		return filterColumn;
	}

	public void setFilterColumn(String filterColumn) {
		this.filterColumn = filterColumn;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public DynamicSubquery getDinamicSubquery() {
		return dinamicSubquery;
	}

	public void setDinamicSubquery(DynamicSubquery dinamicSubquery) {
		switch (this.type) {
			case SMARTCOMBO:
			case SMARTCOMBO_LIST:
				this.dinamicSubquery = dinamicSubquery;
				this.dinamicSubquery.setReportField(this);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade DinamicSubquery");
				this.dinamicSubquery = null;
				break;
		}
	}

	public ReportFilter getParentFilter() {
		return parentFilter;
	}

	public void setParentFilter(ReportFilter parentFilter) {
		this.parentFilter = parentFilter;
	}

	// METODOS COMUNS ENTRE OBJETOS
	public String getID() {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				return dtp.getID();

			case SMARTCOMBO:
				return combo.getID();

			case SMARTCOMBO_LIST:
				return comboList.getID();

			default:
				return input.getInputID();
		}
	}

	public void setID(String pID) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				dtp.setID(pID);
				input.setInputID("_dtp" + pID);
				break;

			case SMARTCOMBO:
				combo.setID(pID);
				input.setInputID("_combo" + pID);
				break;

			case SMARTCOMBO_LIST:
				comboList.setID(pID);
				input.setInputID("_comboList" + pID);
				break;

			default:
				input.setInputID(pID);
				break;
		}
	}

	// METODOS DO FORM INPUT

	public String getLabel() {
		return input.getInputLabel();
	}

	public void setLabel(String label) {
		this.input.setInputLabel(label);
	}

	public int getInputMaxLength() {
		switch (this.type) {
			case DECIMAL:
			case INTEGER:
			case TEXT:
			case TEXTAREA:
				return input.getInputMaxLength();

			default:
				return 0;
		}
	}

	public void setInputMaxLength(int inputMaxLength) {
		switch (this.type) {
			case DECIMAL:
			case INTEGER:
			case KEYWORD:
			case TEXT:
			case TEXTAREA:
				input.setInputMaxLength(inputMaxLength);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade MaxLength");
				break;
		}
	}

	public String getName() {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				return dtp.getName();

			case SMARTCOMBO:
			case SMARTCOMBO_LIST:
				return "";

			default:
				return input.getInputName();
		}
	}

	public void setName(String name) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				dtp.setName(name);
				break;

			case SMARTCOMBO:
			case SMARTCOMBO_LIST:
				input.setInputName("");
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade Name");
				break;

			default:
				input.setInputName(name);
				break;
		}
	}

	public boolean isReadOnly() {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				return !dtp.isEnabled();

			case SMARTCOMBO:
				return combo.isReadOnly();

			case SMARTCOMBO_LIST:
				return comboList.isReadOnly();

			default:
				return input.isInputReadOnly();
		}
	}

	public void setReadOnly(boolean readOnly) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				dtp.setEnabled(!readOnly);
				break;

			case SMARTCOMBO:
				combo.setReadOnly(readOnly);
				break;

			case SMARTCOMBO_LIST:
				comboList.setReadOnly(readOnly);
				break;

			default:
				input.setInputReadOnly(readOnly);
				break;
		}
	}

	public void setInputExtraClasses(String inputExtraClasses) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade InputExtraClasses");
				break;

			default:
				input.setInputExtraClasses(inputExtraClasses);
				break;
		}
	}

	public void setInputExtraStyles(String inputExtraStyles) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade InputExtraStyles");
				break;

			default:
				input.setInputExtraStyles(inputExtraStyles);
				break;
		}
	}

	public void setDivExtraClasses(String divExtraClasses) {
		input.setDivExtraClasses(divExtraClasses);
	}

	public void setDivExtraStyles(String divExtraStyles) {
		input.setDivExtraStyles(divExtraStyles);
	}

	public void setOnBlur(String onBlur) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade OnBlur");
				break;

			case DECIMAL:
				input.setOnBlur("FormataValor(this, event, 2, 12);" + onBlur);
				break;

			default:
				input.setOnBlur(onBlur);
				break;
		}
	}

	public void setOnChange(String onChange) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade OnChange");
				break;

			default:
				input.setOnChange(onChange);
				break;
		}
	}

	public void setOnContextMenu(String onContextMenu) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade OnContextMenu");
				break;

			default:
				input.setOnContextMenu(onContextMenu);
				break;
		}
	}

	public void setOnFocus(String onFocus) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade OnFocus");
				break;

			default:
				input.setOnFocus(onFocus);
				break;
		}
	}

	public void setOnSelect(String onSelect) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade OnSelect");
				break;

			default:
				input.setOnSelect(onSelect);
				break;
		}
	}

	public void setOnKeyDown(String onKeyDown) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade OnKeyDown");
				break;

			default:
				input.setOnKeyDown(onKeyDown);
				break;
		}
	}

	public void setOnKeyPress(String onKeyPress) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade OnKeyPress");
				break;

			case INTEGER:
				input.setOnKeyPress("MascaraInteiro();" + onKeyPress);
				break;

			default:
				input.setOnKeyPress(onKeyPress);
				break;
		}
	}

	public void setOnKeyUp(String onKeyUp) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade OnKeyUp");
				break;

			case DECIMAL:
				input.setOnKeyUp("FormataValor(this, event, 2, 12);" + onKeyUp);
				break;

			default:
				input.setOnKeyUp(onKeyUp);
				break;
		}
	}

	public void setOnClick(String onClick) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade onClick");
				break;

			default:
				input.setOnClick(onClick);
				break;
		}
	}

	public void setOnDoubleClick(String onDoubleClick) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade onDoubleClick");
				break;

			default:
				input.setOnDoubleClick(onDoubleClick);
				break;
		}
	}

	public void setOnCopy(String onCopy) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade onCopy");
				break;

			default:
				input.setOnCopy(onCopy);
				break;
		}
	}

	public void setOnPaste(String onPaste) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
			case SMARTCOMBO_LIST:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade onPaste");
				break;

			default:
				input.setOnPaste(onPaste);
				break;
		}
	}

	public boolean isOptionsInline() {
		switch (this.type) {
			case CHECKBOX:
			case RADIO:
				return input.isOptionsInline();

			default:
				return false;
		}
	}

	public void setOptionsInline(boolean optionsInline) {
		switch (this.type) {
			case CHECKBOX:
			case RADIO:
				input.setOptionsInline(optionsInline);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade OptionsInline");
				break;
		}
	}

	public void addInputOption(String label, String value, boolean checked) {
		switch (this.type) {
			case CHECKBOX:
			case RADIO:
			case SELECT:
				input.addInputOption(label, value, checked);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade InputOption");
				break;
		}
	}

	// METODOS DO DATE PICKER

	public void setMinDate(String mMinDate) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				dtp.setMinDate(mMinDate);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade MinDate");
				break;
		}
	}

	public void setMaxDate(String mMaxDate) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				dtp.setMaxDate(mMaxDate);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade MaxDate");
				break;
		}
	}

	public void setMinDateReference(String mMinDateReference) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				dtp.setMinDateReference(mMinDateReference);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade MinDateReference");
				break;
		}
	}

	public void setMaxDateReference(String mMaxDateReference) {
		switch (this.type) {
			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				dtp.setMaxDateReference(mMaxDateReference);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade MaxDateReference");
				break;
		}
	}

	// METODOS DO SMARTCOMBO

	public void addPrimary(String pName, DataType pDataType) {
		switch (this.type) {
			case SMARTCOMBO:
				combo.addPrimary(pName, pDataType, "");
				break;

			case SMARTCOMBO_LIST:
				comboList.addPrimary(pName, pDataType, "");
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade addPrimary");
				break;
		}
	}

	public void addColumnDisplay(String pName, DataType pDataType, Operation pOperation, boolean pHiddenColumn) {
		switch (this.type) {
			case SMARTCOMBO:
				combo.addColumnDisplay(pName, pDataType, pOperation, pHiddenColumn);
				break;

			case SMARTCOMBO_LIST:
				comboList.addColumnDisplay(pName, pDataType, pOperation, pHiddenColumn);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade addColumnDisplay");
				break;
		}
	}

	public void setColumnOrderBy(String pName, DataType pDataType, Operation pOperation, Ordination pOrdination) {
		switch (this.type) {
			case SMARTCOMBO:
				combo.setDistinct(false);
				combo.setColumnOrderBy(pName, pDataType, pOperation, pOrdination);
				break;

			case SMARTCOMBO_LIST:
				comboList.setDistinct(false);
				comboList.setColumnOrderBy(pName, pDataType, pOperation, pOrdination);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade setColumnOrderBy");
				break;
		}
	}

	public void addDependency(String pRemoteFieldId, String pRemoteColumn, String pLocalColumn, String pRequiredMessage) {
		switch (this.type) {
			case SMARTCOMBO:
				combo.addDependency(pRemoteFieldId, pRemoteColumn, pLocalColumn, pRequiredMessage, true);
				break;

			case SMARTCOMBO_LIST:
				comboList.addDependency(pRemoteFieldId, pRemoteColumn, pLocalColumn, pRequiredMessage, true);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade addDependency");
				break;
		}
	}

	public void setComboTable(String table) {
		switch (this.type) {
			case SMARTCOMBO:
				combo.setTable(table);
				break;

			case SMARTCOMBO_LIST:
				comboList.setTable(table);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade setTable");
				break;
		}
	}

	public void setBaseFilter(String baseFilter) {
		switch (this.type) {
			case SMARTCOMBO:
				combo.setBaseFilter(baseFilter);
				break;

			case SMARTCOMBO_LIST:
				comboList.setBaseFilter(baseFilter);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade setBaseFilter");
				break;
		}
	}

	public void setAutoFill(boolean isAutoFill) {
		switch (this.type) {
			case SMARTCOMBO:
				combo.setAutoFill(isAutoFill);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade setAutoFill");
				break;
		}
	}

	public void setDistinct(boolean isDistinct) {
		switch (this.type) {
			case SMARTCOMBO:
				combo.setDistinct(isDistinct);
				break;

			case SMARTCOMBO_LIST:
				comboList.setDistinct(isDistinct);
				break;

			default:
				Utils.printSystemError("", "Filtros do tipo '" + this.type.name() + "' não permitem alterar a propriedade setAutoFill");
				break;
		}
	}

	// MÉTODOS DO REPORT FILTER FIELD

	public void addFilterField(String field, boolean isCLOB) {
		if (isCLOB) {
			if (!camposCLOB.contains(field)) {
				camposCLOB.add(field);
			}
		}
		else {
			if (!campos.contains(field)) {
				campos.add(field);
			}
		}
	}

	public long countFilterFields() {
		return campos.size() + camposCLOB.size();
	}

	public void requestValues() {
		switch (this.type) {
			case CHECKBOX:
				this.setFilterValue(this.getParameterValuesSeparated(input.getInputName()));
				// String[] values = filterValue.split(",");
				// for (int i = 0; i < values.length; i++) {
				// for (InputOption o : input.inputOptions) {
				// o.setChecked(o.getValue().equals(values[i]));
				// }
				// }
				break;

			case DATE:
			case DATETIME:
			case MONTH_YEAR:
				if (this.interval) {
					this.setFilterValue(this.getParameter(dtp.getName() + "_INI") + ( this.getParameter(dtp.getName() + "_FIM").equals("") ? "" : "," ) + this.getParameter(dtp.getName() + "_FIM"));
				}
				else {
					this.setFilterValue(this.getParameter(dtp.getName()));
				}
				break;

			case DECIMAL:
			case INTEGER:
				if (this.interval) {
					this.setFilterValue(this.getParameter(input.getInputName() + "_INI") + ( this.getParameter(input.getInputName() + "_FIM").equals("") ? "" : "//" ) + this.getParameter(input.getInputName() + "_FIM"));
				}
				else {
					this.setFilterValue(this.getParameter(input.getInputName()));
				}
				break;

			case SMARTCOMBO:
				for (ColumnDisplay p : combo.mPrimaries) {
					// VALOR PARA CRIAR WHERE
					this.setFilterValue(filterValue + ( filterValue.length() > 0 ? "|" : "" ) + getParameter("_column_" + combo.getID() + "_" + combo.getAlias(p)));
				}
				break;

			case SMARTCOMBO_LIST:
				// VALOR PARA CRIAR WHERE
				this.setFilterValue(getParameterValuesSeparated("_column_pk_" + comboList.getID()));
				this.setFilterValue(filterValue + ( filterValue.length() > 0 ? "//" + getParameter("_radio_filter_include_" + comboList.getID()) : "" ));
				break;

			default:
				// VALOR PARA CRIAR WHERE
				this.setFilterValue(this.getParameter(input.getInputName()));
				break;
		}
	}

	public String createWhere(Database lcdb, boolean useAND) {
		String auxSQL = "";

		String[] primaryFields = null;
		String[] primaryValues = null;

		int auxCount;

		if (this.filterTable.length() > 0 && ( ( this.type != ReportFieldType.KEYWORD && this.filterColumn.length() > 0 ) || ( this.type == ReportFieldType.KEYWORD && ( this.campos.size() > 0 || this.camposCLOB.size() > 0 || this.filterColumn.length() > 0 ) ) ) && this.filterValue.length() > 0 && !this.filterValue.equals(",") && !this.filterValue.equals("//")) {
			switch (this.type) {
				case CHECKBOX:
					auxSQL = " " + this.filterTable + "." + this.filterColumn + " IN (";
					String[] values = this.filterValue.split(",");
					for (int i = 0; i < values.length; i++) {
						auxSQL += ( i > 0 ? ", " : "" ) + lcdb.verifyInsertNull(values[i], DataType.TEXT);
					}
					auxSQL += ")";
					break;

				case SELECT:
				case RADIO:
					if (this.filterValue.equals("NULL")) {
						auxSQL = " " + this.filterTable + "." + this.filterColumn + " IS NULL ";
					}
					else {
						auxSQL = " " + this.filterTable + "." + this.filterColumn + " = " + lcdb.verifyInsertNull(this.filterValue, DataType.TEXT) + " ";
					}
					break;

				case DATE:
					if (this.isInterval()) {
						// PRIMEIRA PARTE DO INTERVALO
						try {
							if (!this.filterValue.split(",")[0].equals("")) {
								auxSQL = " " + this.filterTable + "." + this.filterColumn + " >= " + lcdb.verifyInsertNull(this.filterValue.split(",")[0] + " 00:00:00", DataType.TIME) + " ";
							}
						}
						catch (Exception e) {
						}
						// SEGUNDA PARTE DO INTERVALO
						try {
							if (!this.filterValue.split(",")[1].equals("")) {
								auxSQL += ( this.filterValue.split(",")[0].equals("") ? "" : ( useAND ? "AND" : "OR" ) ) + " " + this.filterTable + "." + this.filterColumn + " <= " + lcdb.verifyInsertNull(this.filterValue.split(",")[1] + " 23:59:59", DataType.TIME) + " ";
							}
						}
						catch (Exception e) {
						}
					}
					else {
						auxSQL = " " + this.filterTable + "." + this.filterColumn + " = " + lcdb.verifyInsertNull(this.filterValue, DataType.DATE) + " ";
					}
					break;

				case DATETIME:
					if (this.isInterval()) {
						// PRIMEIRA PARTE DO INTERVALO
						try {
							if (!this.filterValue.split(",")[0].equals("")) {
								auxSQL = " " + this.filterTable + "." + this.filterColumn + " >= " + lcdb.verifyInsertNull(this.filterValue.split(",")[0], DataType.DATE_TIME) + " ";
							}
						}
						catch (Exception e) {
						}
						// SEGUNDA PARTE DO INTERVALO
						try {
							if (!this.filterValue.split(",")[1].equals("")) {
								auxSQL += ( this.filterValue.split(",")[0].equals("") ? "" : ( useAND ? "AND" : "OR" ) ) + " " + this.filterTable + "." + this.filterColumn + " <= " + lcdb.verifyInsertNull(this.filterValue.split(",")[1], DataType.DATE_TIME) + " ";
							}
						}
						catch (Exception e) {
						}
					}
					else {
						auxSQL = " " + this.filterTable + "." + this.filterColumn + " = " + lcdb.verifyInsertNull(this.filterValue, DataType.DATE_TIME) + " ";
					}
					break;

				case MONTH_YEAR:
					if (this.isInterval()) {
						// PRIMEIRA PARTE DO INTERVALO
						try {
							if (!this.filterValue.split(",")[0].equals("")) {
								String dateAux = Utils.convertDateFormats("dd/MM/yyyy HH:mm:ss", user.getUserDateFormat() + " HH:mm:ss", "01/" + this.filterValue.split(",")[0] + " 00:00:00");
								auxSQL = " " + this.filterTable + "." + this.filterColumn + " >= " + lcdb.verifyInsertNull(dateAux, DataType.TIME) + " ";
							}
						}
						catch (Exception e) {
						}
						// SEGUNDA PARTE DO INTERVALO
						try {
							if (!this.filterValue.split(",")[1].equals("")) {
								String dateAux = Utils.convertDateFormats("dd/MM/yyyy HH:mm:ss", user.getUserDateFormat() + " HH:mm:ss", Utils.diasPorMes(new Integer(this.filterValue.split(",")[1].split("/")[0]).intValue(), new Integer(this.filterValue.split(",")[1].split("/")[1]).intValue()) + "/" + this.filterValue.split(",")[1] + " 23:59:59");
								auxSQL += ( this.filterValue.split(",")[0].equals("") ? "" : ( useAND ? "AND" : "OR" ) ) + " " + this.filterTable + "." + this.filterColumn + " <= " + lcdb.verifyInsertNull(dateAux, DataType.TIME) + " ";
							}
						}
						catch (Exception e) {
						}
					}
					else {
						auxSQL = " " + BuildSql.getMonthBarYearToChar(this.filterTable + "." + this.filterColumn) + " = " + lcdb.verifyInsertNull(this.filterValue, DataType.TEXT) + " ";
					}
					break;

				case DECIMAL:
				case INTEGER:
					if (this.isInterval()) {
						// PRIMEIRA PARTE DO INTERVALO
						try {
							auxSQL = !this.filterValue.split("//")[0].equals("") ? " " + this.filterTable + "." + this.filterColumn + " >= " + lcdb.verifyInsertNull(this.filterValue.split("//")[0], DataType.INTEGER) + " " : "";
						}
						catch (Exception e) {
						}
						// SEGUNDA PARTE DO INTERVALO
						try {
							auxSQL += ( this.filterValue.split("//")[0].equals("") ? "" : ( useAND ? "AND" : "OR" ) ) + " " + this.filterTable + "." + this.filterColumn + " <= " + lcdb.verifyInsertNull(this.filterValue.split("//")[1], DataType.INTEGER) + " ";
						}
						catch (Exception e) {
						}
					}
					else {
						auxSQL = " " + this.filterTable + "." + this.filterColumn + " = " + lcdb.verifyInsertNull(this.filterValue, DataType.INTEGER) + " ";
					}
					break;

				case KEYWORD:
					StringBuffer sbf = new StringBuffer();
					for (int i = 0; i < campos.size(); i++) {
						sbf.append(sbf.length() > 0 ? " OR " : "");
						sbf.append(BuildSql.getRemoveAccent(BuildSql.getToChar((String) campos.get(i)), true) + " LIKE " + BuildSql.getRemoveAccent("'%" + this.filterValue + "%'", true));
					}

					for (int i = 0; i < camposCLOB.size(); i++) {
						sbf.append(sbf.length() > 0 ? " OR " : "");
						sbf.append(BuildSql.getRemoveAccent(BuildSql.getToChar((String) camposCLOB.get(i), true), true) + " LIKE " + BuildSql.getRemoveAccent("'%" + this.filterValue + "%'", true));
					}

					if (this.filterColumn.length() > 0) {
						sbf.append(sbf.length() > 0 ? " OR " : "");
						sbf.append(BuildSql.getRemoveAccent(BuildSql.getToChar(this.filterTable + "." + this.filterColumn/* , true */), true) + " LIKE " + BuildSql.getRemoveAccent("'%" + this.filterValue + "%'", true));
					}

					auxSQL = " ( " + sbf.toString() + " ) ";
					break;

				case SMARTCOMBO:
					auxSQL = "";

					primaryFields = new String[combo.mPrimaries.size()];
					primaryValues = Database.verifyNull(this.filterValue).length() > 0 ? this.filterValue.split("\\|") : null;

					auxCount = 0;
					for (Column p : combo.mPrimaries) {
						primaryFields[auxCount] = p.getName();
						auxCount++;
					}

					if (primaryFields != null && primaryValues != null) {
						for (int i = 0; i < primaryFields.length; i++) {
							if (i < primaryValues.length) {
								if (primaryValues[i].equals("-1")) {
									auxSQL += ( auxSQL.trim().length() > 0 ? " AND " : " " ) + " " + BuildSql.getNvl(this.filterTable + "." + ( primaryFields[i].contains(".") ? primaryFields[i].split("\\.")[1] : primaryFields[i] ), "-1") + " = " + lcdb.verifyInsertNull(primaryValues[i], DataType.TEXT) + " ";
								}
								else {
									auxSQL += ( auxSQL.trim().length() > 0 ? " AND " : " " ) + " " + this.filterTable + "." + ( primaryFields[i].contains(".") ? primaryFields[i].split("\\.")[1] : primaryFields[i] ) + " = " + lcdb.verifyInsertNull(primaryValues[i], DataType.TEXT) + " ";
								}
							}
						}
					}
					break;

				case SMARTCOMBO_LIST:
					auxSQL = "";

					if (this.filterValue.split("//").length > 0) {

						String auxCONCAT = "";
						boolean hasValue = false;
						for (Column p : comboList.mPrimaries) {
							if (!auxCONCAT.equals("")) {
								auxCONCAT += BuildSql.getConcatCharacter() + "'_'" + BuildSql.getConcatCharacter();
							}
							auxCONCAT += BuildSql.getToChar(this.filterTable + "." + ( p.getName().contains(".") ? p.getName().split("\\.")[1] : p.getName() ));
						}

						boolean exclude = this.filterValue.split("//").length > 1 && !Database.verifyNull(this.filterValue.split("//")[1], "I").equals("I");
						auxSQL = auxCONCAT + ( exclude ? " NOT " : "" ) + " IN (";

						primaryValues = this.filterValue.split("//")[0].split(",");
						for (String value : primaryValues) {
							if (hasValue) {
								auxSQL += ",";
							}
							auxSQL += lcdb.verifyInsertNull(value, DataType.TEXT);
							hasValue = true;
						}
						if (!hasValue) {
							auxSQL += "NULL";
						}
						auxSQL += ") ";

						if (exclude) {
							auxSQL = " ( (" + auxSQL + ") OR " + this.filterTable + "." + this.filterColumn + " IS NULL) ";
						}

					}
					break;

				default:
					auxSQL = " " + BuildSql.getTrim("UPPER(" + BuildSql.getRemoveAccent(this.filterTable + "." + this.filterColumn, true) + ")") + " LIKE " + BuildSql.getTrim("UPPER(" + BuildSql.getRemoveAccent(lcdb.verifyInsertNull("%" + this.filterValue + "%", DataType.TEXT), true) + ")") + " ";
					break;
			}
		}

		return auxSQL.toString();
	}

	public String printField(FormState state) {
		StringBuffer field = new StringBuffer();

		field.append("<div class='row' id='_div_filter" + this.getID() + "'" + ( this.show ? "" : " style='display: none;'" ) + ">\n");
		try {
			boolean loadDefault = state.equals(FormState.INITIAL);

			String auxID = input.getInputID();
			String auxName = input.getInputName();
			String auxLabel = input.getInputLabel();

			String[] auxInterval = null;

			input.setDivWidthClass(ColumnWidth.TWELVE.toString());

			switch (this.type) {
				case CHECKBOX:
					input.setInputValue("");
					for (InputOption opt : input.inputOptions) {
						for (String value : loadDefault ? defaultValue.split(",") : filterValue.split(",")) {
							if (opt.getValue().equals(value)) {
								opt.setChecked(true);
							}
						}
					}
					field.append(input.printInput());
					field.append("\n");
					break;

				case DECIMAL:
				case INTEGER:
					if (this.interval) {
						auxInterval = loadDefault ? defaultValue.split("//") : filterValue.split("//");

						// INICIO DO INTERVALO
						input.setInputLabel(auxLabel + " [" + user.getTermo("INICIO") + "]");
						input.setInputID(auxID + "_INI");
						input.setInputName(auxName + "_INI");
						input.setDivWidthClass(ColumnWidth.SIX.toString());
						input.setInputValue(auxInterval.length > 0 ? auxInterval[0] : "");
						input.setOnBlur("$('#" + auxID + "_FIM').val( this.value );");
						field.append(input.printInput());
						field.append("\n");

						// FIM DO INTERVALO
						input.setInputLabel(auxLabel + " [" + user.getTermo("FIM") + "]");
						input.setInputID(auxID + "_FIM");
						input.setInputName(auxName + "_FIM");
						input.setDivWidthClass(ColumnWidth.SIX.toString());
						input.setInputValue(auxInterval.length > 1 ? auxInterval[1] : "");
						field.append(input.printInput());
						field.append("\n");

						input.setInputLabel(auxLabel);
						input.setInputID(auxID);
						input.setInputName(auxName);
					}
					else {
						input.setInputValue(loadDefault ? defaultValue : filterValue);
						field.append(input.printInput());
						field.append("\n");
					}
					break;

				case DATE:
				case DATETIME:
				case MONTH_YEAR:
					String dateID = dtp.getID();
					String dateName = dtp.getName();

					if (this.interval) {
						auxInterval = loadDefault ? defaultValue.split(",") : filterValue.split(",");

						// INICIO DO INTERVALO
						dtp.setID(dateID + "_INI");
						dtp.setName(dateName + "_INI");
						dtp.setMinDateReference("");
						dtp.setMaxDateReference(dateID + "_FIM");
						dtp.setValue(auxInterval.length > 0 ? auxInterval[0] : "");

						input.setInputLabel(auxLabel + " [" + user.getTermo("INICIO") + "]");
						input.setInputID(auxID + "_INI");
						input.setInputName(auxName + "_INI");
						input.setDivWidthClass(ColumnWidth.SIX.toString());
						input.setInputHTML(dtp.printDatePicker(this.type.equals(ReportFieldType.DATE) ? DateType.DATE : ( this.type.equals(ReportFieldType.DATETIME) ? DateType.DATE_TIME : DateType.MONTH_YEAR )));
						field.append(input.printInput());
						field.append("\n");

						// FIM DO INTERVALO
						dtp.setID(dateID + "_FIM");
						dtp.setName(dateName + "_FIM");
						dtp.setMaxDateReference("");
						dtp.setMinDateReference(dateID + "_INI");
						dtp.setValue(auxInterval.length > 1 ? auxInterval[1] : "");

						input.setInputLabel(auxLabel + " [" + user.getTermo("FIM") + "]");
						input.setInputID(auxID + "_FIM");
						input.setInputName(auxName + "_FIM");
						input.setDivWidthClass(ColumnWidth.SIX.toString());
						input.setInputHTML(dtp.printDatePicker(this.type.equals(ReportFieldType.DATE) ? DateType.DATE : ( this.type.equals(ReportFieldType.DATETIME) ? DateType.DATE_TIME : DateType.MONTH_YEAR )));
						field.append(input.printInput());
						field.append("\n");

						dtp.setID(dateID);
						dtp.setName(dateName);
						dtp.setMaxDateReference("");
						dtp.setMinDateReference("");
					}
					else {
						dtp.setValue(loadDefault ? defaultValue : filterValue);
						input.setInputHTML(dtp.printDatePicker(this.type.equals(ReportFieldType.DATE) ? DateType.DATE : ( this.type.equals(ReportFieldType.DATETIME) ? DateType.DATE_TIME : DateType.MONTH_YEAR )));
						field.append(input.printInput());
						field.append("\n");
					}
					break;

				case SMARTCOMBO:
					String[] primaryValues = loadDefault ? defaultValue.split("\\|") : filterValue.split("\\|");

					int auxCount = 0;
					for (ColumnDisplay p : combo.mPrimaries) {
						if (auxCount < primaryValues.length) {
							p.setValue(primaryValues[auxCount]);
						}
						auxCount++;
					}

					input.setInputHTML(combo.printSmartCombo());
					field.append(input.printInput());
					field.append("\n");
					break;

				case SMARTCOMBO_LIST:
					input.setInputHTML(comboList.printSmartCombo());
					field.append(input.printInput());
					field.append("\n");
					break;

				default:
					input.setInputValue(loadDefault ? defaultValue : filterValue);
					field.append(input.printInput());
					field.append("\n");
					break;
			}
		}
		catch (Exception e) {
			Utils.printSystemError("switch.type : " + this.type + " : " + this.getID(), e.getMessage());
		}
		field.append("</div>\n");

		return field.toString();
	}

	public String printHidden() {
		StringBuffer field = new StringBuffer();

		try {
			String auxID = input.getInputID();
			String auxName = input.getInputName();

			String dateID = dtp.getID();
			String dateName = dtp.getName();

			String[] auxInterval = null;

			input.setDivWidthClass(ColumnWidth.TWELVE.toString());

			switch (this.type) {
				case DECIMAL:
				case INTEGER:
					if (this.interval) {
						auxInterval = filterValue.split("//");
						// INICIO DO INTERVALO
						field.append("<input type='hidden' name='" + ( auxName + "_INI" ) + "' id='" + ( auxID + "_INI" ) + "' value='" + ( auxInterval.length > 0 ? auxInterval[0] : "" ) + "' />\n");
						// FIM DO INTERVALO
						field.append("<input type='hidden' name='" + ( auxName + "_FIM" ) + "' id='" + ( auxID + "_FIM" ) + "' value='" + ( auxInterval.length > 1 ? auxInterval[1] : "" ) + "' />\n");
					}
					else {
						field.append("<input type='hidden' name='" + auxName + "' id='" + auxID + "' value='" + filterValue + "' />\n");
					}
					break;

				case DATE:
				case DATETIME:
				case MONTH_YEAR:
					if (this.interval) {
						auxInterval = filterValue.split("\\|");
						// INICIO DO INTERVALO
						field.append("<input type='hidden' name='" + ( dateName + "_INI" ) + "' id='" + ( dateID + "_INI" ) + "' value='" + ( auxInterval.length > 0 ? auxInterval[0] : "" ) + "' />\n");
						// FIM DO INTERVALO
						field.append("<input type='hidden' name='" + ( dateName + "_FIM" ) + "' id='" + ( dateID + "_FIM" ) + "' value='" + ( auxInterval.length > 1 ? auxInterval[1] : "" ) + "' />\n");
					}
					else {
						field.append("<input type='hidden' name='" + dateName + "' id='" + dateID + "' value='" + filterValue + "' />\n");
					}
					break;

				case SMARTCOMBO:
					for (ColumnDisplay p : combo.mPrimaries) {
						field.append("<input type='hidden' id='_column_" + combo.getID() + "_" + combo.getAlias(p) + "' name='_column_" + combo.getID() + "_" + combo.getAlias(p) + "' value='" + p.getValue() + "' />\n");
					}
					break;

				case SMARTCOMBO_LIST:
					auxInterval = new String[0];

					if (this.filterValue.split("\\").length > 0) {
						auxInterval = this.filterValue.split("\\")[0].split(",");
					}

					for (String pk : auxInterval) {
						field.append("<input type='hidden' id='_column_pk_" + comboList.getID() + "' name='_column_pk_" + comboList.getID() + "' value='" + pk + "' />  \n");
					}

					field.append("<input type='hidden' id='_radio_filter_include_" + comboList.getID() + "' name='_radio_filter_include_" + comboList.getID() + "' value='" + ( this.filterValue.split("\\").length > 1 ? this.filterValue.split("\\")[1] : "I" ) + "' />  \n");
					break;

				default:
					field.append("<input type='hidden' name='" + auxName + "' id='" + auxID + "' value='" + filterValue + "' />\n");
					break;
			}
		}
		catch (Exception e) {
			Utils.printSystemError("switch.type : " + this.type + " : " + this.getID(), e.getMessage());
		}

		return field.toString();
	}

	public String printDescription() {
		StringBuffer field = new StringBuffer();

		try {
			String[] auxInterval = null;

			if (this.filterDescription && this.isShow()) {
				switch (this.type) {
					case CHECKBOX:
						String[] values = filterValue.split(",");

						field.append("<span><strong>");
						field.append(input.getInputLabel().trim());
						field.append(":&nbsp;</strong>&nbsp;");
						for (int i = 0; i < values.length; i++) {
							for (InputOption opt : input.inputOptions) {
								if (values[i].equals(opt.getValue())) {
									field.append(( i > 0 ? ", " : "" ) + opt.getLabel().trim());
								}
							}
						}
						field.append(";&nbsp;</span>");
						break;

					case DECIMAL:
					case INTEGER:
						if (filterValue.length() > 0 && !filterValue.equals("//")) {
							if (this.interval) {
								auxInterval = filterValue.split("//");

								if (auxInterval.length > 0) {
									field.append("<span><strong>");
									field.append(input.getInputLabel().trim());
									field.append(":&nbsp;</strong>&nbsp;");
									field.append(auxInterval.length > 0 ? auxInterval[0].trim() : "---");
									field.append("&nbsp;" + user.getTermo("ATE") + "&nbsp;");
									field.append(auxInterval.length > 1 ? auxInterval[1].trim() : "---");
									field.append(";&nbsp;</span>");
								}
							}
							else if (filterValue.length() > 0) {
								field.append("<span><strong>");
								field.append(input.getInputLabel().trim());
								field.append(":&nbsp;</strong>&nbsp;");
								field.append(filterValue.trim());
								field.append(";&nbsp;</span>");
							}
						}
						break;

					case DATE:
					case DATETIME:
					case MONTH_YEAR:
						if (filterValue.length() > 0 && !filterValue.equals(",")) {
							if (this.interval) {
								auxInterval = filterValue.split(",");

								if (auxInterval.length > 0) {
									field.append("<span><strong>");
									field.append(input.getInputLabel().trim());
									field.append(":&nbsp;</strong>&nbsp;");
									field.append(auxInterval.length > 0 ? auxInterval[0].trim() : "---");
									field.append("&nbsp;" + user.getTermo("ATE") + "&nbsp;");
									field.append(auxInterval.length > 1 ? auxInterval[1].trim() : "---");
									field.append(";&nbsp;</span>");
								}
							}
							else if (dtp.getValue().length() > 0) {
								field.append("<span><strong>");
								field.append(input.getInputLabel().trim());
								field.append(":&nbsp;</strong>&nbsp;");
								field.append(filterValue.trim());
								field.append(";&nbsp;</span>");
							}
						}
						break;

					case RADIO:
					case SELECT:
						field.append("<span><strong>");
						field.append(input.getInputLabel().trim());
						field.append(":&nbsp;</strong>&nbsp;");
						for (InputOption opt : input.inputOptions) {
							if (filterValue.equals(opt.getValue())) {
								field.append(opt.getLabel().trim());
							}
						}
						field.append(";&nbsp;</span>");
						break;

					case SMARTCOMBO:
						String descr = "";

						for (Column c : combo.mColumnsDisplay) {
							descr += ( descr.length() > 0 ? " - " : "" ) + getParameter("_column_" + combo.getID() + "_" + combo.getAlias(c));
						}

						if (descr.length() > 0) {
							field.append("<span><strong>");
							field.append(input.getInputLabel().trim());
							field.append(":&nbsp;</strong>&nbsp;");
							field.append(descr.trim());
							field.append(";&nbsp;</span>");
						}
						break;

					case SMARTCOMBO_LIST:
						String lista = "";

						String[] primaryValues = this.filterValue.split("//")[0].split(",");
						for (String value : primaryValues) {

							if (lista.length() > 0) {
								lista += ", ";
							}

							for (Column c : comboList.mColumnsDisplay) {
								lista += ( lista.length() > 0 ? " - " : "" ) + getParameter("_column_" + comboList.getID() + "_" + comboList.getAlias(c) + "_" + value);
							}
						}

						if (lista.length() > 0) {
							field.append("<span><strong>");
							field.append(input.getInputLabel().trim());
							field.append("&nbsp;[");
							field.append(!getParameter("_radio_filter_include_" + comboList.getID()).equals("I") ? user.getTermo("EXCLUIR") : user.getTermo("INCLUIR"));
							field.append("]:&nbsp;</strong>&nbsp;(");
							field.append(lista.trim());
							field.append(");&nbsp;</span>");
						}
						break;

					default:
						if (filterValue.length() > 0) {
							field.append("<span><strong>");
							field.append(input.getInputLabel().trim());
							field.append(":&nbsp;</strong>&nbsp;");
							field.append(filterValue.trim());
							field.append(";&nbsp;</span>");
						}
						break;
				}
			}
		}
		catch (Exception e) {
			Utils.printSystemError("switch.type : " + this.type + " : " + this.getID(), e.getMessage());
		}

		return field.toString();
	}

	public String clearFieldJS(Database lcdb) {
		StringBuffer clear = new StringBuffer();

		try {
			String[] auxInterval = null;

			switch (this.type) {
				case CHECKBOX:
					for (InputOption opt : input.inputOptions) {
						for (String value : defaultValue.split(",")) {
							clear.append("  $('#" + this.getID() + "_" + opt.getValue() + "').prop('checked', " + opt.getValue().equals(value) + ");\n");
						}
					}
					clear.append("  $.uniform.update();");
					break;

				case DECIMAL:
				case INTEGER:
					if (this.interval) {
						auxInterval = defaultValue.split("//");

						clear.append("  $('#" + this.getID() + "_INI').val('" + ( auxInterval.length > 0 ? auxInterval[0] : "" ) + "');\n");
						clear.append("  $('#" + this.getID() + "_FIM').val('" + ( auxInterval.length > 1 ? auxInterval[1] : "" ) + "');\n");
					}
					else {
						clear.append("  $('#" + this.getID() + "').val('" + defaultValue + "');\n");
					}
					break;

				case DATE:
				case DATETIME:
				case MONTH_YEAR:
					if (this.interval) {
						auxInterval = defaultValue.split(",");

						clear.append("  _set_value" + this.getID() + "_INI('" + ( auxInterval.length > 0 ? auxInterval[0] : "" ) + "');\n");
						clear.append("  _set_value" + this.getID() + "_FIM('" + ( auxInterval.length > 1 ? auxInterval[1] : "" ) + "');\n");
					}
					else {
						clear.append("  _set_value" + this.getID() + "('" + defaultValue + "');\n");
					}
					break;

				case RADIO:
					for (InputOption opt : input.inputOptions) {
						clear.append("  $('#" + this.getID() + "_" + opt.getValue() + "').prop('checked', " + opt.getValue().equals(defaultValue) + ");\n");
					}
					clear.append("  $.uniform.update();");
					break;

				case SMARTCOMBO:
					String[] primaryValues = defaultValue.split("\\|");

					if (primaryValues.length > 0 && !primaryValues[0].equals("")) {
						int auxCount = 0;
						String auxWhere = "";
						for (ColumnDisplay p : combo.mPrimaries) {
							if (auxCount < primaryValues.length) {
								auxWhere += ( auxWhere.length() > 0 ? ", " : "" );

								switch (p.getType()) {
									case DATE:
										auxWhere += "\"" + p.getName() + " = " + lcdb.verifyInsertNull(primaryValues[auxCount], DataType.DATE) + "\"";
										break;

									case DATE_TIME:
										auxWhere += "\"" + p.getName() + " = " + lcdb.verifyInsertNull(primaryValues[auxCount], DataType.DATE_TIME) + "\"";
										break;

									case DECIMAL:
									case INTEGER:
										auxWhere += "\"" + p.getName() + " = " + lcdb.verifyInsertNull(primaryValues[auxCount], DataType.INTEGER) + "\"";
										break;

									default:
										auxWhere += "\"" + BuildSql.getToChar(p.getName()) + " = " + lcdb.verifyInsertNull(primaryValues[auxCount], DataType.TEXT) + "\"";
										break;
								}
							}
							auxCount++;
						}

						if (auxWhere.length() > 0) {
							clear.append("  _busca_valor_" + this.getID() + "(" + auxWhere + ");\n");
						}
						else {
							clear.append("  _limpa_campos_" + this.getID() + "();\n");
						}
					}
					else {
						clear.append("  _limpa_campos_" + this.getID() + "();\n");
					}
					break;

				case SMARTCOMBO_LIST:
					clear.append("  _limpa_campos_" + this.getID() + "();\n");
					break;

				default:
					clear.append("  $('#" + this.getID() + "').val('" + defaultValue + "');\n");
					break;
			}
		}
		catch (Exception e) {
			Utils.printSystemError("switch.type : " + this.type + " : " + this.getID(), e.getMessage());
		}

		return clear.toString();
	}

	private String getParameter(String parameter) {
		return Database.verifyNull(request.getParameter(parameter));
	}

	private String getParameterValuesSeparated(String parameter) {
		StringBuffer params = new StringBuffer();

		String[] values = Database.verifyNullArray(request.getParameterValues(parameter));
		for (String value : values) {
			params.append(( params.length() > 0 ? "," : "" ) + value);
		}

		return params.toString();
	}

	// ORDENAÇÃO DOS CAMPOS NO FORMULÁRIO DE FILTRO
	public int compareTo(Object obj) {
		if (!( obj instanceof ReportFilterField )) {
			throw new ClassCastException();
		}

		ReportFilterField arg = (ReportFilterField) obj;

		if (this.filterGroup < arg.getFilterGroup()) {
			return -1;
		}
		else if (this.filterGroup > arg.getFilterGroup()) {
			return 1;
		}

		return new Integer(this.order).compareTo(new Integer(arg.getOrder()));
	}

}
