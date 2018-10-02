
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class DatePicker {

	protected User mUser;

	protected String mID = "";
	protected String mName = "";
	protected String mValue = "";
	protected String mWidth = "";

	protected String mMinDate = "";
	protected String mMaxDate = "";
	protected String mMinHour = "";
	protected String mMaxHour = "";

	protected String mMinDateReference = "";
	protected String mMaxDateReference = "";

	protected boolean mEnabled = true;

	protected ArrayList<String> mDisabledDates;
	protected ArrayList<WeekDays> mDisabledWeekDays;

	public DatePicker(User pUser) {
		this.mUser = pUser;
		this.mDisabledDates = new ArrayList<String>();
		this.mDisabledWeekDays = new ArrayList<WeekDays>();
	}

	public String getID() {
		return mID;
	}

	public void setID(String mID) {
		this.mID = mID;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getValue() {
		return mValue;
	}

	public void setValue(String mValue) {
		this.mValue = mValue;
	}

	public String getWidth() {
		return mWidth;
	}

	public void setWidth(String mWidth) {
		this.mWidth = mWidth;
	}

	public String getMinDate() {
		return mMinDate;
	}

	public void setMinDate(String mMinDate) {
		this.mMinDate = mMinDate;
	}

	public String getMaxDate() {
		return mMaxDate;
	}

	public void setMaxDate(String mMaxDate) {
		this.mMaxDate = mMaxDate;
	}

	public String getMinDateReference() {
		return mMinDateReference;
	}

	public void setMinDateReference(String mMinDateReference) {
		this.mMinDateReference = mMinDateReference;
	}

	public String getMaxDateReference() {
		return mMaxDateReference;
	}

	public void setMaxDateReference(String mMaxDateReference) {
		this.mMaxDateReference = mMaxDateReference;
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	public void setEnabled(boolean mEnabled) {
		this.mEnabled = mEnabled;
	}

	public String printPickerJS(DateType pType) {
		return printPickerJS(pType, false);
	}

	public String printPickerJS(DateType pType, boolean pParentField) {
		StringBuffer js = new StringBuffer();

		String datesArray = "";
		for (String date : mDisabledDates) {
			if (datesArray.equals("")) {
				datesArray += "[";
			}
			else {
				datesArray += ", ";
			}
			datesArray += "'" + date + "'";
		}
		if (!datesArray.equals("")) {
			datesArray += "]";
		}

		String weekDaysArray = "";
		for (WeekDays day : mDisabledWeekDays) {
			if (weekDaysArray.equals("")) {
				weekDaysArray += "[";
			}
			else {
				weekDaysArray += ", ";
			}
			weekDaysArray += day.ordinal();
		}
		if (!weekDaysArray.equals("")) {
			weekDaysArray += "]";
		}

		if (!pParentField) {
			js.append("<script>");
		}

		if (!pType.equals(DateType.TIME)) {
			if (mMaxDate != null && !mMaxDate.trim().equals("")) {
				js.append("var dt_max_" + mID + " = '';");
			}
			if (mMinDate != null && !mMinDate.trim().equals("")) {
				js.append("var dt_min_" + mID + " = '';");
			}
		}
		js.append("var dt_obj_" + mID + " = function() {");
		js.append("  " + (pParentField ? "parent." : "") + "$.fn.datetimepicker.dates['en'] = {");
		js.append("    clear: '" + mUser.getTermo("LIMPARDADOS") + "', \n");
		js.append("    days: [ '" + mUser.getTermo("DOMINGO") + "', '" + mUser.getTermo("SEGUNDAFEIRA") + "', '" + mUser.getTermo("TERCAFEIRA") + "', '" + mUser.getTermo("QUARTAFEIRA") + "', '" + mUser.getTermo("QUINTAFEIRA") + "', '" + mUser.getTermo("SEXTAFEIRA") + "', '" + mUser.getTermo("SABADO") + "' ], \n");
		js.append("    daysMin: [ '" + mUser.getTermo("DOMINGO").substring(0, 1) + "', '" + mUser.getTermo("SEGUNDAFEIRA").substring(0, 1) + "', '" + mUser.getTermo("TERCAFEIRA").substring(0, 1) + "', '" + mUser.getTermo("QUARTAFEIRA").substring(0, 1) + "', '" + mUser.getTermo("QUINTAFEIRA").substring(0, 1) + "', '" + mUser.getTermo("SEXTAFEIRA").substring(0, 1) + "', '" + mUser.getTermo("SABADO").substring(0, 1) + "' ], \n");
		js.append("    daysShort: [ '" + mUser.getTermo("DOMINGO").substring(0, 3) + "', '" + mUser.getTermo("SEGUNDAFEIRA").substring(0, 3) + "', '" + mUser.getTermo("TERCAFEIRA").substring(0, 3) + "', '" + mUser.getTermo("QUARTAFEIRA").substring(0, 3) + "', '" + mUser.getTermo("QUINTAFEIRA").substring(0, 3) + "', '" + mUser.getTermo("SEXTAFEIRA").substring(0, 3) + "', '" + mUser.getTermo("SABADO").substring(0, 3) + "' ], \n");
		js.append("    months: [ '" + mUser.getTermo("JANEIRO") + "', '" + mUser.getTermo("FEVEREIRO") + "', '" + mUser.getTermo("MARCO") + "', '" + mUser.getTermo("ABRIL") + "', '" + mUser.getTermo("MAIO") + "', '" + mUser.getTermo("JUNHO") + "', '" + mUser.getTermo("JULHO") + "', '" + mUser.getTermo("AGOSTO") + "', '" + mUser.getTermo("SETEMBRO") + "', '" + mUser.getTermo("OUTUBRO") + "', '" + mUser.getTermo("NOVEMBRO") + "', '" + mUser.getTermo("DEZEMBRO") + "' ], \n");
		js.append("    monthsShort: [ '" + mUser.getTermo("JANEIRO").substring(0, 3) + "', '" + mUser.getTermo("FEVEREIRO").substring(0, 3) + "', '" + mUser.getTermo("MARCO").substring(0, 3) + "', '" + mUser.getTermo("ABRIL").substring(0, 3) + "', '" + mUser.getTermo("MAIO").substring(0, 3) + "', '" + mUser.getTermo("JUNHO").substring(0, 3) + "', '" + mUser.getTermo("JULHO").substring(0, 3) + "', '" + mUser.getTermo("AGOSTO").substring(0, 3) + "', '" + mUser.getTermo("SETEMBRO").substring(0, 3) + "', '" + mUser.getTermo("OUTUBRO").substring(0, 3) + "', '" + mUser.getTermo("NOVEMBRO").substring(0, 3) + "', '" + mUser.getTermo("DEZEMBRO").substring(0, 3) + "' ], \n");
		js.append("    today: '" + mUser.getTermo("HOJE") + "' \n");
		js.append("  }; \n");

		js.append("  " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').datetimepicker({ \n");
		js.append("    isRTL: true, \n");
		js.append("    autoclose: true, \n");
		js.append("    maskInput: true, \n");
		js.append("    forceParse: true, \n");
		js.append("    pickerType: '" + pType.toString() + "', \n");
		js.append("    highlightToday: true, \n");

		if (pType.equals(DateType.MONTH_YEAR)) {
			js.append("    viewMode: 'months', \n");
			js.append("    minViewMode: 'months', \n");
		}

		if (!pType.equals(DateType.TIME)) {
			if (mMaxDate != null && !mMaxDate.trim().equals("")) {
				js.append("    maxDate: '" + mMaxDate + "', \n");
				js.append("    endDate: '" + mMaxDate + "', \n");
			}

			if (mMinDate != null && !mMinDate.trim().equals("")) {
				js.append("    minDate: '" + mMinDate + "', \n");
				js.append("    startDate: '" + mMinDate + "', \n");
			}

			if (!datesArray.equals("")) {
				js.append("    datesDisabled: " + datesArray + ", \n");
			}

			if (!weekDaysArray.equals("")) {
				js.append("    daysOfWeekDisabled: " + weekDaysArray + ", \n");
			}
		}

		// SELEÇÃO DE FORMATOS
		switch (pType) {
			case TIME:
				js.append("    format: 'hh:mm', \n");
				js.append("    pickDate: false, \n");
				js.append("    pickTime: true, \n");
				break;
			case DATE_TIME:
				js.append("    format: '" + mUser.getUserDateFormat() + " hh:mm', \n");
				js.append("    pickDate: true, \n");
				js.append("    pickTime: true, \n");
				break;
			case MONTH_YEAR:
				js.append("    format: 'MM/yyyy', \n");
				js.append("    pickDate: true, \n");
				js.append("    pickTime: false, \n");
				js.append("    viewMode: 'months', \n");
				break;
			case DATE:
			default:
				js.append("    format: '" + mUser.getUserDateFormat() + "', \n");
				js.append("    pickDate: true, \n");
				js.append("    pickTime: false, \n");
				break;
		}

		js.append("    pickSeconds: false, \n");
		js.append("    pick12HourFormat: false \n");

		js.append("  }); \n");

		if (mMaxDateReference != null && !mMaxDateReference.trim().equals("")) {
			js.append("  " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mMaxDateReference + "').on('changeDate', function(e) {\n");
			js.append("    if (" + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mMaxDateReference + "').data('datetimepicker')._date.getTime() < " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker')._date.getTime()) {\n");
			js.append("      " + (pParentField ? "parent." : "") + "$('#" + mID + "').val('');\n");
			js.append("      " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker')._date = " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mMaxDateReference + "').data('datetimepicker')._date;");
			js.append("      " + (pParentField ? "parent." : "") + "$('#" + mID + "').change();\n");
			js.append("    }\n");
			js.append("    " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker').setEndDate(e.date ? e.date : (dt_max_" + mID + " ? dt_max_" + mID + " : Infinity));\n");
			js.append("  });\n");
		}
		if (mMinDateReference != null && !mMinDateReference.trim().equals("")) {
			js.append("  " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mMinDateReference + "').on('changeDate', function(e) {\n");
			js.append("    if (" + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mMinDateReference + "').data('datetimepicker')._date.getTime() > " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker')._date.getTime()) {\n");
			js.append("      " + (pParentField ? "parent." : "") + "$('#" + mID + "').val('');\n");
			js.append("      " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker')._date = " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mMinDateReference + "').data('datetimepicker')._date;");
			js.append("      " + (pParentField ? "parent." : "") + "$('#" + mID + "').change();\n");
			js.append("    }\n");
			js.append("    " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker').setStartDate(e.date ? e.date : (dt_min_" + mID + " ? dt_min_" + mID + " : -Infinity)); \n");
			js.append("  });\n");
		}

		js.append("  " + (pParentField ? "parent." : "") + "$('body').removeClass('modal-open'); \n");
		js.append("} \n");

		js.append("$(document).ready(function() {\n");
		js.append("  dt_obj_" + mID + "(); \n");
		if (!mEnabled) {
			js.append("  " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker').disable();\n");
			js.append("  " + (pParentField ? "parent." : "") + "$('#" + mID + "').attr('readonly', true);\n");
			js.append("  " + (pParentField ? "parent." : "") + "$('#" + mID + "').attr('disabled', false);\n");
		}
		js.append("}); \n");

		js.append("function _limpa_campos" + mID + "() {\n");
		js.append("  " + (pParentField ? "parent." : "") + "$('#" + mID + "').val('');\n");
		js.append("  " + (pParentField ? "parent." : "") + "$('#" + mID + "').change();\n");
		js.append("}\n");
		js.append("function _max_date" + mID + "(data) {\n");
		js.append("  " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker').setEndDate(data); \n");
		js.append("}\n");
		js.append("function _min_date" + mID + "(data) {\n");
		js.append("  " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker').setStartDate(data); \n");
		js.append("}\n");
		js.append("function _set_value" + mID + "(valor) {\n");
		js.append("  " + (pParentField ? "parent." : "") + "$('#" + mID + "').val( valor );\n");
		js.append("  " + (pParentField ? "parent." : "") + "$('#" + mID + "').change();\n");
		js.append("}\n");
		js.append("function _enable" + mID + "(enable) {\n");
		js.append("  if (enable) {\n");
		js.append("    " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker').enable();\n");
		js.append("    " + (pParentField ? "parent." : "") + "$('#" + mID + "').attr('readonly', false);\n");
		js.append("    " + (pParentField ? "parent." : "") + "$('#" + mID + "').attr('disabled', false);\n");
		js.append("  }\n");
		js.append("  else {\n");
		js.append("    " + (pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').data('datetimepicker').disable();\n");
		js.append("    " + (pParentField ? "parent." : "") + "$('#" + mID + "').attr('readonly', true);\n");
		js.append("    " + (pParentField ? "parent." : "") + "$('#" + mID + "').attr('disabled', false);\n");
		js.append("  }\n");
		js.append("}\n");
		
		if (mValue != null && !mValue.trim().equals("")) {
			js.append("$(window).load(function() {\n");
			js.append("  " + (pParentField ? "parent." : "") + "$('#" + mID + "').val('" + mValue + "');\n");
			js.append("  " + (pParentField ? "parent." : "") + "$('#" + mID + "').change();\n");
			js.append("});\n");
		}

		// SETTING ON CHANGE EVENT
		js.append((pParentField ? "parent." : "") + "$('.dt-picker-class-" + mID + "').on('changeDate', function(e) {\n");
		js.append("  try { afterChange" + mID + "(); } catch(e) {}");
		js.append("});\n");

		if (!pParentField) {
			js.append("</script>");
		}

		return js.toString();
	}

	public String printDatePicker(DateType pType) {
		return printDatePicker(pType, true);
	}

	public String printDatePicker(DateType pType, boolean pPrintJS) {
		StringBuffer dt = new StringBuffer();

		dt.append("<link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css'/>\n");
		dt.append("<link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/datetimepicker.css'/>\n");
		dt.append("<script type='text/javascript' src='metronic/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker-twitter.min.js'></script>\n");
		dt.append("<div class='input-group input-append date dt-picker-class-" + mID + "'> \n");
		dt.append("  <input type='text' id='" + mID + "' name='" + mName + "' value='" + mValue + "' class='form-control' style='text-align:center;'> \n");
		dt.append("  <span class='input-group-btn add-on'> \n");
		dt.append("    <button class='btn default date-set' type='button'><i class='fa fa-" + (pType.equals(DateType.TIME) ? "clock-o" : "calendar") + "'></i></button> \n");
		dt.append("  </span> \n");
		dt.append("</div> \n");

		if (pPrintJS) {
			dt.append(this.printPickerJS(pType));
		}

		return dt.toString();
	}

	public void addDisabledDate(String disabledDate) {
		mDisabledDates.add(disabledDate);
	}

}
