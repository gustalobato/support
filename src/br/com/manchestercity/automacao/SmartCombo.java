
package br.com.manchestercity.automacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

public class SmartCombo {

	User mUser;
	Database lcdb = null;
	Connection mConn = null;

	ResultSet rs = null;

	HttpServletRequest request;

	StringBuffer lHtml;
	StringBuffer lHiddenFields;

	int mCurPage;
	int mPageCount;
	int mItemCount;
	int mColumnCount;
	int mPrimaryCount;

	String mDisplayColumns;

	long mRecordCount = 0;

	boolean mAutoFill;
	boolean mReadOnly;
	boolean mEnableAfterFill;

	String mSQL;
	String mSqlPage;
	String mSqlCount;

	String mID = "";
	String mTable = "";
	String mBaseFilter = "";
	String mHavingClause = "";

	boolean mDistinct = true;

	String mCurFilter = "";
	String mQuickSearch = "";

	protected ColumnDisplay mColumnOrderBy;
	protected ArrayList<ColumnDisplay> mPrimaries = new ArrayList<ColumnDisplay>();
	protected ArrayList<ColumnDisplay> mColumnsDisplay = new ArrayList<ColumnDisplay>();
	protected ArrayList<ColumnDependency> mColumnsDependency = new ArrayList<ColumnDependency>();

	protected ArrayList<ColumnDisplay> mColumnsHidden = new ArrayList<ColumnDisplay>();
	protected ArrayList<ColumnDisplay> mColumnsOrderBy = new ArrayList<ColumnDisplay>();

	private boolean mHasValues = false;

	StringBuffer mColumnsQuery = new StringBuffer();
	StringBuffer mGroupByQuery = new StringBuffer();
	StringBuffer mQueryOrderBy = new StringBuffer();
	StringBuffer mQueryOrderByAlias = new StringBuffer();

	protected String mFunctionFormatResult = "";
	protected String mFunctionFormatSelection = "";

	public SmartCombo(User pUser) {
		this.mUser = pUser;
		this.init();
	}

	private void init() {
		lHtml = new StringBuffer();
		lHiddenFields = new StringBuffer();

		lcdb = new Database(mUser);
		mConn = null;
		rs = null;

		mDisplayColumns = "";

		mPrimaryCount = 0;
		mColumnCount = 0;
		mRecordCount = 0;

		mSQL = "";
		mSqlCount = "";

		mAutoFill = true;
		mReadOnly = false;
		mEnableAfterFill = true;

		mCurPage = 1;
		mCurFilter = "";

		mPageCount = 20;
		mItemCount = 25;

		mQuickSearch = "";

		mHasValues = false;

		mDistinct = true;
	}

	public String getID() {
		return mID;
	}

	public void setID(String pValue) {
		mID = pValue;
	}

	public void setTable(String pValue) {
		mTable = pValue;
	}

	public String getBaseFilter() {
		return mBaseFilter;
	}

	public void setBaseFilter(String pValue) {
		mBaseFilter = pValue;
	}

	public void setHavingClause(String pValue) {
		mHavingClause = pValue;
	}

	public void setAutoFill(boolean pValue) {
		mAutoFill = pValue;
	}

	public boolean isReadOnly() {
		return mReadOnly;
	}

	public void setReadOnly(boolean pValue) {
		mReadOnly = pValue;
	}

	public void setEnableAfterFill(boolean pValue) {
		mEnableAfterFill = pValue;
	}

	public boolean isDistinct() {
		return mDistinct;
	}

	public void setDistinct(boolean mDistinct) {
		this.mDistinct = mDistinct;
	}

	public void setFunctionFormatResult(String functionFormatResult) {
		mFunctionFormatResult = functionFormatResult;
	}

	public void setFunctionFormatSelection(String functionFormatSelection) {
		mFunctionFormatSelection = functionFormatSelection;
	}

	public void setColumnOrderBy(String pName, DataType pDataType, Operation pOperation, Ordination pOrdination) {
		this.mColumnOrderBy = new ColumnDisplay();

		this.mColumnOrderBy.setName(pName);
		this.mColumnOrderBy.setOperation(pOperation);
		this.mColumnOrderBy.setType(pDataType);
		this.mColumnOrderBy.setOrdination(pOrdination);

		mColumnsOrderBy.add(mColumnOrderBy);
	}

	public void addPrimary(String pName, DataType pDataType, String pValue) {
		ColumnDisplay primary = new ColumnDisplay();

		primary.setName(pName);
		primary.setType(pDataType);
		primary.setValue(pValue);

		mPrimaries.add(primary);

		mPrimaryCount++;

		if (pValue != null && !pValue.trim().equals("")) {
			mHasValues = true;
		}
	}

	public void addColumnDisplay(ColumnDisplay pColumn) {
		mColumnsDisplay.add(pColumn);
	}

	public ColumnDisplay addColumnDisplay(String pName, DataType pDataType, Operation pOperation) {
		return addColumnDisplay(pName, pDataType, pOperation, false);
	}

	public ColumnDisplay addColumnDisplay(String pName, DataType pDataType, Operation pOperation, boolean pHiddenColumn) {
		ColumnDisplay columnDisplay = new ColumnDisplay();

		columnDisplay.setName(pName);
		columnDisplay.setType(pDataType);
		columnDisplay.setOperation(pOperation);
		columnDisplay.setValue("");

		if (pHiddenColumn) {
			mColumnsHidden.add(columnDisplay);
		}
		else {
			mColumnsDisplay.add(columnDisplay);
		}

		return columnDisplay;
	}

	public void addDependency(String pRemoteFieldId, String pRemoteColumn, String pLocalColumn, String pRequiredMessage, boolean pEquals) {
		ColumnDependency columnDependency = new ColumnDependency();

		columnDependency.setRemoteFieldId(pRemoteFieldId);
		columnDependency.setRemoteColumn(pRemoteColumn);
		columnDependency.setLocalColumn(pLocalColumn);
		columnDependency.setEquals(pEquals);
		columnDependency.setRequiredMessage(pRequiredMessage);

		mColumnsDependency.add(columnDependency);
	}

	public static String getAlias(String pColumnName, boolean pWithoutWhiteSpace) {
		String ret = "";

		ret = pColumnName;

		if (pColumnName.indexOf(".") >= 0) {
			String[] arr = pColumnName.split("\\.");
			ret = arr[arr.length - 1];
		}

		if (pWithoutWhiteSpace && ret.indexOf(" ") >= 0) {
			String[] arr = pColumnName.split(" ");
			ret = arr[arr.length - 1];
		}

		return ret;
	}

	public String getAlias(Column c) {
		return getAlias(buildColumn(c), true);
	}

	public String removeAlias(Column c) {
		String alias = getAlias(c);
		return c.getName().replaceAll(" AS " + alias, "").trim();
	}

	protected String buildOperation(Column c) {
		if (c.getOperation() == null) {

			if (c.getType().equals(DataType.DATE)) {
				return lcdb.dateToChar(c.getName());
			}
			else if (c.getType().equals(DataType.DATE_TIME) || c.getType().equals(DataType.TIME)) {
				return lcdb.dateTimeToChar(c.getName());
			}

			return c.getName();
		}

		return c.getOperation() + "(" + c.getName() + ")";
	}

	protected String buildAlias(Column c) {
		String alias = "";

		if (c.getOperation() != null) {
			alias = c.getOperation() + "_";
		}

		alias += c.getName().replaceAll("\\.", "_");

		return alias;
	}

	protected String buildColumn(Column c) {
		if (c.getName().toUpperCase().contains(" AS ")) {
			return c.getName();
		}

		return this.buildOperation(c) + " AS " + this.buildAlias(c);
	}

	protected StringBuffer buildQueryBase() {

		StringBuffer query = new StringBuffer();

		boolean needGroupBy = false;

		boolean addPrimary = true;
		for (ColumnDisplay p : mPrimaries) {
			addPrimary = true;

			for (ColumnDisplay cd : mColumnsDisplay) {
				if (p.getName().equals(cd.getName())) {
					addPrimary = false;
					break;
				}
			}

			if (addPrimary) {
				if (mColumnsQuery.length() > 0) {
					mColumnsQuery.append(", ");
				}
				mColumnsQuery.append(this.buildColumn(p));
				mColumnCount++;

				lHiddenFields.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(p) + "' name='_column_" + mID + "_" + getAlias(p) + "' value='' />\n");

				if (mGroupByQuery.length() > 0) {
					mGroupByQuery.append(", ");
				}
				mGroupByQuery.append(removeAlias(p));
			}
		}

		for (ColumnDisplay cd : mColumnsDisplay) {
			mDisplayColumns += ( mDisplayColumns.length() > 0 ? "," : "" ) + getAlias(cd);

			if (mColumnsQuery.length() > 0) {
				mColumnsQuery.append(", ");
			}
			mColumnsQuery.append(this.buildColumn(cd));
			mColumnCount++;

			lHiddenFields.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "' name='_column_" + mID + "_" + getAlias(cd) + "' value='' />\n");

			if (cd.getOperation() == null) {
				if (mGroupByQuery.length() > 0) {
					mGroupByQuery.append(", ");
				}
				mGroupByQuery.append(removeAlias(cd));
			}
			else {
				needGroupBy = true;
			}
		}

		for (ColumnDisplay cd : mColumnsHidden) {
			// mDisplayColumns += (mDisplayColumns.length() > 0 ? "," : "") + getAlias(cd);

			if (mColumnsQuery.length() > 0) {
				mColumnsQuery.append(", ");
			}
			mColumnsQuery.append(this.buildColumn(cd));
			// mColumnCount++;

			lHiddenFields.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "' name='_column_" + mID + "_" + getAlias(cd) + "' value='' />\n");

			if (cd.getOperation() == null) {
				if (mGroupByQuery.length() > 0) {
					mGroupByQuery.append(", ");
				}
				mGroupByQuery.append(removeAlias(cd));
			}
			else {
				needGroupBy = true;
			}
		}

		query.append("SELECT ");
		if (mDistinct) {
			query.append("DISTINCT ");
		}
		query.append(mColumnsQuery);
		query.append(" FROM ");
		query.append(mTable);

		if (mBaseFilter != null && mBaseFilter.trim().length() > 0) {
			query.append(" WHERE ");
			query.append(mBaseFilter);
		}

		if (needGroupBy) {
			query.append(" GROUP BY ");
			query.append(mGroupByQuery);
		}
		else {
			mGroupByQuery.delete(0, mGroupByQuery.length());
		}

		if (mHavingClause != null && mHavingClause.trim().length() > 0) {
			query.append(" HAVING ");
			query.append(mHavingClause);
		}

		return query;
	}

	protected String buildOrdination() {
		String orderBy = "";

		for (ColumnDisplay rc : mColumnsOrderBy) {
			if (rc != null && !Database.verifyNull(rc.getName()).equals("")) {
				if (rc.getOrdination() != null) {
					orderBy += ( orderBy.length() > 0 ? ", " : "" ) + removeAlias(rc) + " " + rc.getOrdination();
				}
				else {
					orderBy += ( orderBy.length() > 0 ? ", " : "" ) + removeAlias(rc);
				}
			}
		}

		return orderBy.length() > 0 ? " ORDER BY " + orderBy : "";
	}

	// CRIADO ESPECIFICAMENTE PARA CRIAR O ORDER BY DE PAGINACOES DO SQL SERVER
	protected String buildOrdinationByAlias() {
		String orderBy = "";

		for (ColumnDisplay rc : mColumnsOrderBy) {
			if (rc != null && !Database.verifyNull(rc.getName()).equals("")) {
				for (ColumnDisplay cd : mColumnsDisplay) {
					if (rc.getOrdination() != null) {
						orderBy += ( orderBy.length() > 0 ? ", " : "" ) + getAlias(cd) + " " + rc.getOrdination();
					}
					else {
						orderBy += ( orderBy.length() > 0 ? ", " : "" ) + getAlias(cd);
					}
				}
			}
		}

		return orderBy.length() > 0 ? " ORDER BY " + orderBy : "";
	}

	public String printFieldsOnly() {
		StringBuffer fields = new StringBuffer();

		fields.append(lHiddenFields);
		fields.append("<input type='hidden' class='bs-select form-control select2' id='smartCombo_" + mID + "' name='smartCombo_" + mID + "' value='' " + ( mReadOnly ? "readonly='readonly' " : "" ) + "/>\n");

		return fields.toString();
	}

	public String printScriptOnly() {
		StringBuffer script = new StringBuffer();

		script.append("var buscaValor_" + mID + " = ''; \n");
		script.append("var filtroExtra_" + mID + " = ''; \n");
		script.append("var enableAfterFill_" + mID + " = " + mEnableAfterFill + "; \n");
		script.append("jQuery(document).ready( function() {\n");
		script.append("  $('#smartCombo_" + mID + "').select2({ \n");
		script.append("    allowClear : true, \n");
		script.append("    placeholder: '" + mUser.getTermo("SELECIONE") + "...', \n");
		script.append("    selectOnBlur: false, \n");
		script.append("    minimumInputLength: 0, \n");

		if (mFunctionFormatResult.length() > 0) {
			script.append("    formatResult: " + mFunctionFormatResult + ", \n");
		}

		if (mFunctionFormatSelection.length() > 0) {
			script.append("    formatSelection: " + mFunctionFormatSelection + ", \n");
		}

		script.append("    ajax: { \n");
		script.append("      url: 'smartcombo', \n");
		script.append("      type: 'POST', \n");
		script.append("      cache: false, \n");
		script.append("      dataType: 'json', \n");
		script.append("      quietMillis: 500, \n");
		// OUR SEARCH TERM AND WHAT PAGE WE ARE ON
		script.append("      data: function (term, page) { \n");
		script.append("        return { \n");
		script.append("          pageSize: 30, \n");
		script.append("          pageNum: page, \n");
		script.append("          searchTerm: term, \n");
		script.append("          selectId: '" + mID + "', \n");
		script.append("          selectCols: \"" + mColumnsQuery + "\", \n");
		script.append("          selectFrom: \"" + mTable + "\", \n");
		script.append("          selectWhere: \"" + mBaseFilter + "\", \n");
		script.append("          selectGroup: \"" + mGroupByQuery + "\", \n");
		script.append("          selectOrder: \"" + mQueryOrderBy + "\", \n");
		script.append("          selectOrderAlias: \"" + mQueryOrderByAlias + "\", \n");
		script.append("          selectHaving: \"" + mHavingClause + "\", \n");
		script.append("          selectSearch: \"" + mQuickSearch + "\", \n");
		script.append("          selectDistinct: \"" + mDistinct + "\", \n");
		script.append("          buscaValor: buscaValor_" + mID + ", \n");
		script.append("          filtroExtra: filtroExtra_" + mID + ", \n");

		/*
		 * if (mColumnsDependency != null && mColumnsDependency.size() > 0) { for (CColumnDependency dep : mColumnsDependency) { script.append("          dependencies: function() { \n"); script.append("            var depValue = $('#_column_" + dep.getRemoteFieldId() + "_" + dep.getRemoteColumn().replaceAll("\\.", "_") + "').val(); \n"); if (dep.getRequiredMessage() != null && !dep.getRequiredMessage().trim().equals("")) { script.append("            if (depValue === undefined || depValue === null || depValue === '') { \n"); script.append("              depValue = '0'; \n"); script.append("              $('#smartCombo_" + mID + "').select2('close'); \n"); script.append("              $('#smartCombo_" + mID + "').select2('val', '', true); \n"); script.append("              top.bootbox.alert(\"" + dep.getRequiredMessage().trim() + "\"); \n"); script.append("            } \n"); } script.append("            return (depValue === undefined || depValue === null || depValue === '' ? '' : \"" + dep.getLocalColumn() + (dep.isEquals() ? "" : " NOT") + " IN ('\" + depValue + \"')\"); \n"); script.append("          }, \n"); } }
		 */

		if (mColumnsDependency != null && mColumnsDependency.size() > 0) {
			script.append("          dependencies: function() { \n");
			script.append("            var vet_return = []; \n");
			for (ColumnDependency dep : mColumnsDependency) {
				script.append("            var depValue = $('#_column_" + dep.getRemoteFieldId() + "_" + dep.getRemoteColumn().replaceAll("\\.", "_") + "').val(); \n");
				if (dep.getRequiredMessage() != null && !dep.getRequiredMessage().trim().equals("")) {
					script.append("            if (depValue === undefined || depValue === null || depValue === '') { \n");
					script.append("              depValue = '0'; \n");
					script.append("              $('#smartCombo_" + mID + "').select2('close'); \n");
					script.append("              $('#smartCombo_" + mID + "').select2('val', '', true); \n");
					// script.append(" " + ( !IniManipulation.getProperty("new_layout").equals("") ? "top." : "" ) + "bootbox.alert(\"" + dep.getRequiredMessage().trim() + "\"); \n");
					script.append("              top.bootbox.alert(\"" + dep.getRequiredMessage().trim() + "\"); \n");
					script.append("            } \n");
				}
				script.append("            vet_return.push (depValue === undefined || depValue === null || depValue === '' ? '' : \"" + dep.getLocalColumn() + ( dep.isEquals() ? "" : " NOT" ) + " IN ('\" + depValue + \"')\"); \n");
			}
			script.append("            return vet_return; \n");
			script.append("          }, \n");
		}

		script.append("          columnCount: " + mColumnCount + ", \n");
		script.append("          primaryCount: \"" + mPrimaryCount + "\", \n");
		script.append("          displayColumns: \"" + mDisplayColumns + "\" \n");
		script.append("        }; \n");
		script.append("      }, \n");
		// USED TO DETERMINE WHETHER OR NOT THERE ARE MORE RESULTS AVAILABLE, AND IF REQUESTS FOR MORE DATA SHOULD BE SENT IN THE INFINITE SCROLLING
		script.append("      results: function (data, page) { \n");
		script.append("        var more = (page * 30) < data.total;  \n");
		script.append("        if ( (buscaValor_" + mID + " !== '' || " + mAutoFill + ") && data.total == 1 ) {\n");
		script.append("          $('#smartCombo_" + mID + "').select2( 'close' );\n");
		script.append("          $('#smartCombo_" + mID + "').select2( 'data', data.results[0] ).trigger('change');\n");
		script.append("        }\n");
		script.append("        return { results: data.results, more: more }; \n");
		script.append("      } \n");
		script.append("    }, \n");
		script.append("    formatNoMatches: function (term) { return \"" + mUser.getTermo("MSGNENHUMREGENC") + "\"; }, \n");
		script.append("    formatLoadMore: function (pageNumber) { return \"" + mUser.getTermo("SELECTCARREGANDO") + "\"; }, \n");
		script.append("    formatSearching: function () { return \"" + mUser.getTermo("SELECTBUSCANDO") + "\"; }, \n");
		script.append("    formatAjaxError: function (xhr, error, thrown) { \n");
		script.append("			                 if (xhr.responseText.indexOf('function open'+'Login()') > 0) { \n");
		script.append("                         $('#smartCombo_" + mID + "').select2( 'close' );\n");
		script.append("                         var lStr = xhr.responseText; \n");
		script.append("                         var lEval = ''; \n");
		script.append("                         if (lStr.indexOf('<scr'+'ipt') >= 0) { \n");
		script.append("                           var lScript = lStr.split('<scr'+'ipt'); \n");
		script.append("                           for (i = 0; i < lScript.length; i++) { \n");
		script.append("                             if (lScript[i].indexOf('</scr'+'ipt>') != '-1') { \n");
		script.append("                               lScriptAux = lScript[i].split('</scr'+'ipt>'); \n");
		script.append("                               lEval += lScriptAux[0].substring(lScriptAux[0].indexOf('>') + 1); \n");
		script.append("                             } \n");
		script.append("                           } \n");
		script.append("                         } \n");
		script.append("                         if (lEval != '') { \n");
		script.append("                           lEval = lEval.replace('submitForm()', 'submitForm_" + mID + "()'); \n");
		script.append("                           jQuery.globalEval(lEval); \n");
		script.append("                         } \n");
		script.append("                         return \"" + mUser.getTermo("AUTENTICACAOSIST") + "\";");
		script.append("                       } \n");
		script.append("                       else { \n");
		script.append("                         return \"" + mUser.getTermo("MSGERRODISPLAY") + "\"; \n");
		script.append("                       } \n");
		script.append("                     } \n");

		// script.append(" formatInputTooShort: function (term, minLength) { var n = minLength - input.length; return \"\"; }, \n");
		// script.append(" formatInputTooLong: function (term, maxLength) { var n = input.length - maxLength; return \"\"; }, \n");
		// script.append(" formatSelectionTooBig: function (maxSize) { return \"\"; }, \n");
		script.append("  }); \n");

		script.append("  $('#smartCombo_" + mID + "').on('change', function() {\n");
		script.append("    var emptyField = $('#smartCombo_" + mID + "').select2('data') === null || $('#smartCombo_" + mID + "').select2('data') === undefined; \n");

		boolean addPrimary = true;
		for (ColumnDisplay p : mPrimaries) {
			addPrimary = true;

			for (ColumnDisplay cd : mColumnsDisplay) {
				if (p.getName().equals(cd.getName())) {
					addPrimary = false;
					break;
				}
			}

			if (addPrimary) {
				script.append("    $('#_column_" + mID + "_" + getAlias(p) + "').val( emptyField ? '' : $('#smartCombo_" + mID + "').select2('data')." + getAlias(p) + " ); \n");
			}

			if (!Database.verifyNull(p.getValue()).equals("")) {
				mHasValues = true;
			}
		}

		for (ColumnDisplay cd : mColumnsDisplay) {
			script.append("    $('#_column_" + mID + "_" + getAlias(cd) + "').val( emptyField ? '' : $('#smartCombo_" + mID + "').select2('data')." + getAlias(cd) + " ); \n");
		}

		for (ColumnDisplay cd : mColumnsHidden) {
			script.append("    $('#_column_" + mID + "_" + getAlias(cd) + "').val( emptyField ? '' : $('#smartCombo_" + mID + "').select2('data')." + getAlias(cd) + " ); \n");
		}

		script.append("    buscaValor_" + mID + " = '';\n");
		script.append("    if (enableAfterFill_" + mID + ") {\n");
		script.append("      if (emptyField) { \n");
		script.append("        try { afterClean" + mID + "(); } catch(e) { } \n");
		script.append("      } \n");
		script.append("      else { \n");
		script.append("        try { afterFill" + mID + "(); } catch(e) { } \n");
		script.append("      } \n");
		script.append("    }\n");
		script.append("  });\n");

		if (( mHasValues || mAutoFill ) && mSQL != null && !mSQL.trim().equals("")) {

			String valueSQL = "";
			valueSQL = "SELECT * FROM ( \n";
			valueSQL += mSQL + " \n";
			valueSQL += ") " + BuildSql.getAsSubQuery("SMARTCOMBO_TB_1234") + " \n";
			valueSQL += "WHERE " + buildValueFilter();

			Connection mConn = lcdb.openConnection();

			long totalReg = lcdb.getRecordCount(mHasValues ? valueSQL : mSQL);

			if (totalReg == 1) {
				ResultSet rs = lcdb.openResultSet(mHasValues ? valueSQL : mSQL, mConn);
				try {

					String id = "";
					String text = "";

					String extraAttrs = "";

					ResultSetMetaData rsmd = rs.getMetaData();

					if (rs.next()) {

						for (int i = 1; i <= rsmd.getColumnCount(); i++) {

							if (i <= mColumnCount) {
								if (i <= mPrimaryCount) {
									id += ( id.trim().equals("") ? "" : "$|$" ) + Database.verifyNull(rs.getObject(i), "0");
								}
								else {
									text += ( text.trim().equals("") ? "" : " - " ) + Database.verifyNull(rs.getObject(i), "0");
								}
							}

							extraAttrs += ", \"" + rsmd.getColumnName(i) + "\": \"" + Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(Database.verifyNull(rs.getObject(i), ""), "\"", "\\\""), "\\", "\\\\"), "\r", ""), "\n", "<br/>") + "\"";
						}

						script.append("  $('#smartCombo_" + mID + "').select2( 'data', { \"id\": \"" + id + "\", \"text\": \"" + Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(text, "\"", "\\\""), "\\", "\\\\"), "\r", ""), "\n", "<br/>") + "\"" + ( extraAttrs.trim().equals("") ? "" : extraAttrs ) + " } ).trigger('change');\n");
					}
				}
				catch (Exception e) {
					Utils.printSystemError("ResultSet", e.getMessage());
				}
				finally {
					Database.closeObject(rs);
					Database.closeObject(mConn);
				}
			}
		}

		if (mColumnsDependency != null && mColumnsDependency.size() > 0) {
			for (ColumnDependency dep : mColumnsDependency) {
				script.append("  $('#smartCombo_" + dep.getRemoteFieldId() + "').on('change', function() {\n");
				script.append("    $('#smartCombo_" + mID + "').select2( 'val', '', true );\n");
				script.append("    var depValue = $('#smartCombo_" + dep.getRemoteFieldId() + "').val();");
				script.append("    if ( depValue !== undefined && depValue !== null && depValue !== '' ) {\n");
				script.append("      $('#smartCombo_" + mID + "').select2( 'triggerAjax' );\n");
				script.append("    }\n");
				script.append("  });\n");
			}
		}
		else if (mAutoFill && !mHasValues) {
			script.append("  $('#smartCombo_" + mID + "').select2( 'triggerAjax' );\n");
		}

		script.append("});\n");

		script.append("function _enable_afterFill_" + mID + "() {\n");
		script.append("  enableAfterFill_" + mID + " = true;\n");
		script.append("}\n");
		script.append("function _disable_afterFill_" + mID + "() {\n");
		script.append("  enableAfterFill_" + mID + " = false;\n");
		script.append("}\n");
		script.append("function setWhere_" + mID + "(where) {\n");
		script.append("  filtroExtra_" + mID + " = where;\n");
		script.append("}\n");
		script.append("function setReadOnly_" + mID + "(enable) {\n");
		script.append("  $('#smartCombo_" + mID + "').select2( 'readonly', enable );\n");
		script.append("}\n");
		script.append("function _limpa_campos_" + mID + "() {\n");
		script.append("  $('#smartCombo_" + mID + "').select2( 'val', '', true );\n");
		script.append("}\n");
		script.append("function _busca_valor_" + mID + "() {\n");
		script.append("  buscaValor_" + mID + " = '';\n");
		script.append("  for (var i = 0; i < arguments.length; i++) {\n");
		script.append("    buscaValor_" + mID + " += (buscaValor_" + mID + " === '' ? '' : ' AND ') + arguments[i];\n");
		script.append("  }\n");
		script.append("  if (buscaValor_" + mID + " !== '') {\n");
		script.append("    $('#smartCombo_" + mID + "').select2( 'triggerAjax' );\n");
		script.append("  }\n");
		script.append("}\n");
		script.append("function submitForm_" + mID + "() {");
		script.append("  $('.select2').select2( 'close' );\n");
		script.append("}\n");

		return script.toString();
	}

	public void prepareCombo() {
		StringBuffer query = this.buildQueryBase();

		if (mColumnsOrderBy.size() <= 0) {
			ColumnDisplay order = mColumnsDisplay.get(0);
			setColumnOrderBy(order.getName(), order.getType(), order.getOperation(), order.getOrdination());
		}

		mQueryOrderBy = new StringBuffer(this.buildOrdination());
		mQueryOrderByAlias = new StringBuffer(this.buildOrdinationByAlias());

		mSQL = query.toString();

		// QUICKSEARCH
		if (mColumnsDisplay.size() > 0) {
			mQuickSearch = ( mQuickSearch.equals("") ? " ( " : " ) AND ( " );

			int auxCount = 0;
			for (ColumnDisplay column : mColumnsDisplay) {
				mQuickSearch += auxCount == 0 ? "" : " OR ";
				auxCount++;

				switch (column.getType()) {
					case TEXT:
					case CLOB:
						mQuickSearch += " " + BuildSql.getRemoveAccent(getAlias(column), false) + " LIKE " + BuildSql.getRemoveAccent(lcdb.verifyInsertNull("%" + "{|SEARCH|}" + "%", DataType.TEXT), false);
						break;

					case INTEGER:
					case DATE:
					case DATE_TIME:
					case TIME:
						mQuickSearch += " " + BuildSql.getToChar(getAlias(column)) + " LIKE " + lcdb.verifyInsertNull("%" + "{|SEARCH|}" + "%", DataType.TEXT) + " ";
						break;

					case DECIMAL:
					default:
						mQuickSearch += " " + BuildSql.getToChar(getAlias(column)) + " LIKE " + lcdb.verifyInsertNull("%" + "{|SEARCH|}" + "%", DataType.TEXT) + " ";
						break;
				}
			}

			mQuickSearch += " ) ";
		}
	}

	public String printSmartCombo() {

		this.prepareCombo();

		lHtml.delete(0, lHtml.length());

		lHtml.append("<script type='text/javascript' src='metronic/global/plugins/select2/select2.js'></script>\n");
		lHtml.append(this.printFieldsOnly());

		lHtml.append("<script>\n");
		lHtml.append(this.printScriptOnly());
		lHtml.append("</script>\n");

		return lHtml.toString();
	}

	public String buildValueFilter() {
		String valueFilter = "";

		for (ColumnDisplay p : mPrimaries) {
			valueFilter += valueFilter.equals("") ? "" : " AND ";

			switch (p.getType()) {
				case DECIMAL:
				case INTEGER:
					valueFilter += getAlias(p) + " = " + lcdb.verifyInsertNull(p.getValue(), DataType.INTEGER);
					break;

				case DATE:
					valueFilter += lcdb.dateToChar(getAlias(p)) + " = " + lcdb.verifyInsertNull(p.getValue(), DataType.TEXT);
					break;

				case DATE_TIME:
					valueFilter += lcdb.dateTimeToChar(getAlias(p)) + " = " + lcdb.verifyInsertNull(p.getValue(), DataType.TEXT);
					break;

				case TIME:
					valueFilter += lcdb.timeToChar(getAlias(p)) + " = " + lcdb.verifyInsertNull(p.getValue(), DataType.TEXT);
					break;

				default:
					valueFilter += BuildSql.getToChar(getAlias(p)) + " = " + lcdb.verifyInsertNull(p.getValue(), DataType.TEXT);
					break;
			}
		}

		return valueFilter;
	}
}
