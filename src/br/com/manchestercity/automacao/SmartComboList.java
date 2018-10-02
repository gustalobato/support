
package br.com.manchestercity.automacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import br.com.manchestercity.automacao.FrameworkDefaults.DataTable;
import br.com.manchestercity.automacao.FrameworkDefaults.FormInput;
import br.com.manchestercity.automacao.FrameworkDefaults.InputType;
import br.com.manchestercity.automacao.FrameworkDefaults.TableRow;
import javazoom.upload.MultipartFormDataRequest;

public class SmartComboList extends SmartCombo {

	MultipartFormDataRequest mUpRequest;
	HttpServletRequest mRequest;
	int mPrimaryListCount;
	private String mEmptyListMessage = "";
	private boolean mIsLoad;
	private boolean mShowDelete;
	private boolean mShowRadio;
	private String mTableList;

	private ArrayList<ColumnDisplay> mPrimariesList = new ArrayList<ColumnDisplay>();

	private ArrayList<String> mTitleList = new ArrayList<String>();

	private boolean mHasValues = false;

	StringBuffer mColumnsQuery = new StringBuffer();
	StringBuffer mGroupByQuery = new StringBuffer();
	StringBuffer mQueryOrderBy = new StringBuffer();

	StringBuffer mColumnsQueryList = new StringBuffer();
	StringBuffer mGroupByQueryList = new StringBuffer();
	StringBuffer mQueryOrderByList = new StringBuffer();

	public SmartComboList(User pUser, HttpServletRequest pRequest) {
		super(pUser);
		this.mUser = pUser;
		this.mRequest = pRequest;
		this.init();
	}

	private void init() {
		lHtml = new StringBuffer();
		// lHiddenFields = new StringBuffer();

		lcdb = new Database(mUser);
		mConn = null;
		rs = null;

		mDisplayColumns = "";

		mPrimaryCount = 0;
		mColumnCount = 0;
		mRecordCount = 0;

		mSQL = "";
		mSqlCount = "";

		mAutoFill = false;
		mReadOnly = false;

		mCurPage = 1;
		mCurFilter = "";

		mPageCount = 20;
		mItemCount = 25;

		mQuickSearch = "";

		mHasValues = false;

		mPrimaryListCount = 0;
		mIsLoad = false;
		mShowDelete = true;
		mShowRadio = false;
		mTableList = "";
	}

	public void setTableList(String mTableList) {
		this.mTableList = mTableList;
	}

	public String getTableList() {
		return this.mTableList;
	}

	public void setIsLoad(boolean mIsLoad) {
		this.mIsLoad = mIsLoad;
	}

	public boolean isLoad() {
		return mIsLoad;
	}

	public void setShowDelete(boolean mShowDelete) {
		this.mShowDelete = mShowDelete;
	}

	public boolean isShowDelete() {
		return mShowDelete;
	}

	public void setShowRadio(boolean mShowRadio) {
		this.mShowRadio = mShowRadio;
	}

	public boolean isShowRadio() {
		return mShowRadio;
	}

	public void setEmptyListMessage(String pValue) {
		mEmptyListMessage = pValue;
	}

	public void addPrimaryList(String pName, DataType pDataType, String pValue) {
		ColumnDisplay primaryList = new ColumnDisplay();

		primaryList.setName(pName);
		primaryList.setType(pDataType);
		primaryList.setValue(pValue);

		mPrimariesList.add(primaryList);

		mPrimaryListCount++;

		if (pValue != null && !pValue.trim().equals("")) {
			mHasValues = true;
		}
	}

	public void addColumnTitleList(String pTitle) {
		mTitleList.add(pTitle);
	}

	public void setMultiPartRequest(MultipartFormDataRequest pRequest) {
		mUpRequest = pRequest;
	}

	public void getParameterList(DataTable pDT, FrameworkDefaults pDefaults) {
		StringBuffer lRequest = new StringBuffer();
		StringBuffer lColumnsHidden = new StringBuffer();
		boolean addPrimary = true;
		String[] PKs = null;

		if (mUpRequest == null) {
			PKs = mRequest.getParameterValues("_column_pk_" + mID);
		}
		else {
			PKs = mUpRequest.getParameterValues("_column_pk_" + mID);
		}

		for (int i = 0; PKs != null && i < PKs.length; i++) {
			TableRow row = pDefaults.new TableRow();
			lRequest.append("<input type='hidden' id='_column_pk_" + mID + "' name='_column_pk_" + mID + "' value='" + PKs[i] + "' />  \n");

			for (ColumnDisplay cd : mColumnsHidden) {
				lColumnsHidden.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "_" + PKs[i] + "' name='_column_" + mID + "_" + getAlias(cd) + "_" + PKs[i] + "' value='" + getParameter("_column_" + mID + "_" + getAlias(cd)) + "' />  \n");
				lColumnsHidden.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "' name='_column_" + mID + "_" + getAlias(cd) + "' value='" + getParameter("_column_" + mID + "_" + getAlias(cd)) + "' />  \n");
			}

			for (ColumnDisplay p : super.mPrimaries) {
				for (ColumnDisplay cd : mColumnsDisplay) {
					if (p.getName().equals(cd.getName())) {
						addPrimary = false;
						break;
					}
				}

				if (addPrimary) {
					lRequest.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(p) + "_" + PKs[i] + "' name='_column_" + mID + "_" + getAlias(p) + "_" + PKs[i] + "' value='" + PKs[i] + "' /> \n");
				}
			}

			for (ColumnDisplay cd : mColumnsDisplay) {
				if (lColumnsHidden.length() > 0) {
					lRequest.append(lColumnsHidden.toString());
					lColumnsHidden.delete(0, lColumnsHidden.length());
				}
				lRequest.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "_" + PKs[i] + "' name='_column_" + mID + "_" + getAlias(cd) + "_" + PKs[i] + "' value='" + getParameter("_column_" + mID + "_" + getAlias(cd) + "_" + PKs[i]) + "' />  \n");

				lRequest.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "' name='_column_" + mID + "_" + getAlias(cd) + "' value='" + getParameter("_column_" + mID + "_" + getAlias(cd) + "_" + PKs[i]) + "' /> \n");
				row.addCell(lRequest + getParameter("_column_" + mID + "_" + getAlias(cd) + "_" + PKs[i], "&nbsp;"), "", "");
				lRequest.delete(0, lRequest.length());
			}
			if (isShowDelete()) {
				row.addCell("<i class='fa fa-times font-red' onclick=deleteRow" + mID + "(this.parentNode.parentNode)></i>", "text-center", "");
			}
			else {
				row.addCell("<i class='fa fa-times font-grey'></i>", "text-center", "");
			}
			pDT.addRow(row, false);
		}
	}

	public static boolean persist(User pUser, Connection pConn, Database pDB, StringBuffer pErro, Persist pPersist, HttpServletRequest request, MultipartFormDataRequest upRequest, String pIdLocalizador, String[] pPrimaryValues) {

		boolean commit = true;
		boolean wasTransactionOpen = !pDB.isAutoCommit(pConn);

		if (!wasTransactionOpen) {
			pDB.setAutoCommit(pConn, false);
		}

		try {
			String[] pk = null;
			String[] pkValues = null;

			pk = request.getParameterValues("_column_pk_" + pIdLocalizador);
			if (upRequest != null) {
				pk = upRequest.getParameterValues("_column_pk_" + pIdLocalizador);
			}

			if (pPrimaryValues != null && pPrimaryValues.length > 0) {
				commit = pPersist.remove(pUser, pConn, pErro, pPrimaryValues);
			}

			if (pk != null && pk.length > 0) {

				String[] valueSent;
				for (int j = 0; commit && j < pk.length; j++) {
					pkValues = pk[j].split("_");
					if (pkValues.length > 1) {
						valueSent = new String[pkValues.length + pPrimaryValues.length];
					}
					else {
						valueSent = new String[1 + pPrimaryValues.length];
					}

					for (int p = 0; p < pPrimaryValues.length; p++) {
						valueSent[p] = pPrimaryValues[p];
					}
					for (int i = 0; i < pkValues.length; i++) {
						valueSent[i + pPrimaryValues.length] = pkValues[i];
					}

					commit = pPersist.insert(pUser, pConn, pErro, valueSent);
				}
			}
		}
		catch (Exception ex) {
			Utils.printSystemError("getParameterValues", ex.getMessage());
			commit = false;
		}

		if (!wasTransactionOpen) {
			if (commit) {
				pDB.commit(pConn);
			}
			else {
				pDB.rollback(pConn);
			}
			pDB.setAutoCommit(pConn, true);
		}

		return commit;
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

			for (ColumnDisplay cd : mColumnsHidden) {
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

			if (mColumnsQuery.length() > 0) {
				mColumnsQuery.append(", ");
			}
			mColumnsQuery.append(this.buildColumn(cd));

			// lHiddenFields.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "' name='_column_" + mID + "_" + getAlias(cd) + "' value='' />\n");

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

	protected StringBuffer buildQueryBaseList() {
		String tblLoc = mTable.trim().startsWith("SELECT") || mTable.trim().startsWith("(") ? BuildSql.getAsSubQuery("tab_aux_" + System.currentTimeMillis()).trim() : "";
		StringBuffer sql = new StringBuffer(" SELECT ");
		int i;
		int cont = 0;
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
				sql.append(tblLoc.length() > 0 ? tblLoc + "." + getAlias(p) : p.getName() + ",");
				cont++;
			}
		}

		for (i = 0; i < mColumnsDisplay.size(); i++) {
			sql.append(( i == 0 && ( !addPrimary || cont > 0 ) ? "" : "," ) + ( tblLoc.length() > 0 ? tblLoc + "." + getAlias(mColumnsDisplay.get(i)) : mColumnsDisplay.get(i).getName() ));
		}

		sql.append(" FROM " + mTableList + " INNER JOIN ");
		if (tblLoc.length() > 0) {
			sql.append("(");
			sql.append(mTable);
			sql.append(")");
			sql.append(tblLoc);
		}
		else {
			sql.append(mTable);
		}
		sql.append(" ON (");
		int prim = 0;

		for (i = 0; i < mPrimaries.size(); i++) {
			sql.append(prim > 0 ? " AND " : "");
			sql.append(mTableList);
			sql.append(".");
			sql.append(getAlias(mPrimaries.get(i).getName(), true));
			sql.append(" = ");
			if (tblLoc.length() > 0) {
				sql.append(tblLoc);
				sql.append(".");
				sql.append(getAlias(mPrimaries.get(i).getName(), true));
			}
			else {
				sql.append(mPrimaries.get(i).getName());
			}
			prim++;
		}
		sql.append(")");
		sql.append(" WHERE ");
		for (i = 0; i < mPrimariesList.size(); i++) {
			if (i > 0) {
				sql.append(" AND ");
			}
			sql.append(mPrimariesList.get(i).getName());
			sql.append(" = ");

			switch (mPrimariesList.get(i).getType()) {
				case DECIMAL:
				case INTEGER:
					sql.append(lcdb.verifyInsertNull(mPrimariesList.get(i).getValue(), DataType.INTEGER));
					break;

				case DATE:
					sql.append(lcdb.verifyInsertNull(mPrimariesList.get(i).getValue(), DataType.DATE));
					break;

				case DATE_TIME:
					sql.append(lcdb.verifyInsertNull(mPrimariesList.get(i).getValue(), DataType.DATE_TIME));
					break;

				default:
					sql.append(lcdb.verifyInsertNull(mPrimariesList.get(i).getValue(), DataType.TEXT));
					break;
			}
		}

		return sql;
	}

	public String printFieldsOnly() {
		StringBuffer fields = new StringBuffer();
		StringBuffer hiddens = new StringBuffer();

		FrameworkDefaults defaults = new FrameworkDefaults();

		DataTable dataTable = defaults.new DataTable();
		dataTable.setDataTable("smartComboList_table_" + mID, "table-condensed", "margin: 0; padding: 0;");

		fields.append("<input type='text' class='bs-select form-control select2' id='smartComboList_" + mID + "' name='smartComboList_" + mID + "' value='' " + ( mReadOnly ? "readonly='readonly' " : "" ) + "/>\n");

		if (mShowRadio) {
			try {
				FormInput input = defaults.new FormInput();
				input.setInput("_radio_filter_include_" + mID, "_radio_filter_include_" + mID, "", "", InputType.RADIO, "", false, false);
				input.setOptionsInline(true);
				input.setDivExtraClasses("text-right");
				input.addInputOption(mUser.getTermo("INCLUIR"), "I", getParameter("_radio_filter_include_" + mID, "I").equals("I"));
				input.addInputOption(mUser.getTermo("EXCLUIR"), "E", !getParameter("_radio_filter_include_" + mID, "I").equals("I"));
				fields.append(input.printInput());
			}
			catch (Exception e) {
				Utils.printSystemError("printRadioInclude", e.getMessage());
			}
		}

		TableRow row = defaults.new TableRow();

		if (mTitleList != null) {
			for (int i = 0; i < mTitleList.size(); i++) {
				row.addCell(mTitleList.get(i), "", "");
			}
			for (int i = mTitleList.size(); i <= mColumnsDisplay.size(); i++) {
				row.addCell("&nbsp;", "", "");
			}
			dataTable.addRow(row, true);
		}
		else {
			for (int i = 0; i <= mColumnsDisplay.size(); i++) {
				row.addCell("&nbsp;", "", "");
			}
			dataTable.addRow(row, true);
		}

		if (isLoad()) {
			getParameterList(dataTable, defaults);
		}
		else {
			rs = null;

			boolean closeConnection = false;
			if (mConn == null) {
				mConn = lcdb.openConnection();
				closeConnection = true;
			}

			try {
				String pk = "";

				rs = lcdb.openResultSet(this.buildQueryBaseList().toString(), mConn);

				while (rs != null && rs.next()) {
					row = defaults.new TableRow();
					pk = "";
					for (ColumnDisplay cd : mPrimaries) {
						if (!pk.equals("")) {
							pk += "_";
						}
						pk += Database.verifyNull(rs.getObject(getAlias(cd.getName(), true)), cd.getValue());
					}

					hiddens.append("<input type='hidden' id='_column_pk_" + mID + "' name='_column_pk_" + mID + "' value='" + pk + "' />  \n");

					for (ColumnDisplay cd : mColumnsDisplay) {
						hiddens.append("<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "_" + pk + "' name='_column_" + mID + "_" + getAlias(cd) + "_" + pk + "' value='" + Database.verifyNull(rs.getObject(getAlias(cd.getName(), true))) + "' />  \n");
						row.addCell(hiddens + Database.verifyNull(rs.getObject(getAlias(cd.getName(), true)), "&nbsp;"), "", "");
						hiddens.delete(0, hiddens.length());
					}

					if (isShowDelete()) {
						row.addCell("<i class='fa fa-times font-red' onclick='deleteRow" + mID + "(this.parentNode.parentNode);'></i>", "text-center", "");
					}
					else {
						row.addCell("<i class='fa fa-times font-grey'></i>", "text-center", "");
					}
					dataTable.addRow(row, false);
				}
			}
			catch (Exception ex) {
				Utils.printSystemError("loadValuesPrimarys", ex.getMessage());
			}
			finally {
				Database.closeObject(rs);
				if (closeConnection) {
					Database.closeObject(mConn);
				}
			}
		}

		fields.append(dataTable.printDataTable(false));

		return fields.toString();
	}

	public String printScriptOnly() {
		StringBuffer script = new StringBuffer();

		script.append("var buscaValor_" + mID + " = ''; \n");
		script.append("var filtroExtra_" + mID + " = ''; \n");
		script.append("var filtroLista_" + mID + " = ''; \n");
		script.append("jQuery(document).ready( function() {\n");
		script.append("  $('#smartComboList_" + mID + "').select2({ \n");
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
		script.append("          selectHaving: \"" + mHavingClause + "\", \n");
		script.append("          selectSearch: \"" + mQuickSearch + "\", \n");
		script.append("          selectDistinct: \"" + mDistinct + "\", \n");
		script.append("          buscaValor: buscaValor_" + mID + ", \n");
		script.append("          filtroExtra: filtroExtra_" + mID + ", \n");
		script.append("          filtroLista: filtroLista_" + mID + ", \n");
		script.append("          dependencies: function() { \n");
		script.append("            var vet_return = []; \n");
		script.append("            var depValue; \n");
		if (mColumnsDependency != null && mColumnsDependency.size() > 0) {
			for (ColumnDependency dep : mColumnsDependency) {
				script.append("            depValue = $('#_column_" + dep.getRemoteFieldId() + "_" + dep.getRemoteColumn().replaceAll("\\.", "_") + "').val(); \n");
				if (dep.getRequiredMessage() != null && !dep.getRequiredMessage().trim().equals("")) {
					script.append("            if (depValue === undefined || depValue === null || depValue === '') { \n");
					script.append("              depValue = '0'; \n");
					script.append("              $('#smartComboList_" + mID + "').select2('close'); \n");
					script.append("              $('#smartComboList_" + mID + "').select2('val', '', true); \n");
					// script.append(" " + ( !IniManipulation.getProperty("new_layout").equals("") ? "top." : "" ) + "bootbox.alert(\"" + dep.getRequiredMessage().trim() + "\"); \n");
					script.append("              top.bootbox.alert(\"" + dep.getRequiredMessage().trim() + "\"); \n");
					script.append("            } \n");
				}
				script.append("            vet_return.push (depValue === undefined || depValue === null || depValue === '' ? '' : \"" + dep.getLocalColumn() + ( dep.isEquals() ? "" : " NOT" ) + " IN ('\" + depValue + \"')\"); \n");
			}
		}

		script.append("            var list;\n");
		for (ColumnDisplay p : mPrimaries) {
			script.append("            list = $(\"[name='_column_" + mID + "_" + getAlias(p) + "']\"); \n");
			script.append("            depValue = ''; \n");

			script.append("            for(i = 0; i < list.length; i++){ \n");
			script.append("              depValue = depValue + (depValue != '' ? '##COMMA##' : '') +  list[i].value; \n");
			script.append("            } \n");

			script.append("            vet_return.push (depValue === undefined || depValue === null || depValue === '' ? '' : \"" + removeAlias(p) + " NOT IN ('\" + depValue + \"')\"); \n");
		}

		script.append("            return vet_return; \n");
		script.append("          }, \n");

		script.append("          columnCount: " + mColumnCount + ", \n");
		script.append("          primaryCount: \"" + mPrimaryCount + "\", \n");
		script.append("          displayColumns: \"" + mDisplayColumns + "\" \n");
		script.append("        }; \n");
		script.append("      }, \n");
		// USED TO DETERMINE WHETHER OR NOT THERE ARE MORE RESULTS AVAILABLE, AND IF REQUESTS FOR MORE DATA SHOULD BE SENT IN THE INFINITE SCROLLING
		script.append("      results: function (data, page) { \n");
		script.append("        var more = (page * 30) < data.total;  \n");
		script.append("        if ( (buscaValor_" + mID + " !== '' || " + mAutoFill + ") && data.total == 1 ) {\n");
		script.append("          $('#smartComboList_" + mID + "').select2( 'close' );\n");
		script.append("          $('#smartComboList_" + mID + "').select2( 'data', data.results[0] ).trigger('change');\n");
		script.append("        }\n");
		script.append("        return { results: data.results, more: more }; \n");
		script.append("      } \n");
		script.append("    }, \n");
		script.append("    formatNoMatches: function (term) { return \"" + mUser.getTermo("MSGNENHUMREGENC") + "\"; }, \n");
		script.append("    formatLoadMore: function (pageNumber) { return \"" + mUser.getTermo("SELECTCARREGANDO") + "\"; }, \n");
		script.append("    formatSearching: function () { return \"" + mUser.getTermo("SELECTBUSCANDO") + "\"; }, \n");
		script.append("    formatAjaxError: function (xhr, error, thrown) { \n");
		script.append("			                 if (xhr.responseText.indexOf('function open'+'Login()') > 0) { \n");
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

		script.append("  }); \n");

		script.append("      $('#smartComboList_table_" + mID + "').dataTable({ \n");
		script.append("        'bInfo': false, \n");
		script.append("        'bFilter': false, \n");
		script.append("        'bPaginate': false, \n");
		script.append("        'bAutoWidth': false, \n");
		script.append("        'bSort': true, \n");
		script.append("        'aoColumns': [ \n");
		for (int i = 0; i < mColumnsDisplay.size(); i++) {
			script.append("          null, \n");
		}
		script.append("          { sClass: 'col-md-1 _text_center' } \n");
		script.append("        ], \n");
		script.append("        'oLanguage': { \n");
		script.append("          'sEmptyTable': '" + ( mEmptyListMessage.equals("") ? mUser.getTermo("MSGNENHUMREGENC") : mEmptyListMessage ) + "' \n");
		script.append("        } \n");

		script.append("      }); \n");
		// script.append(" $('#smartComboList_table_" + mID + "').find('thead').css('display','none'); \n");
		script.append("  $('#smartComboList_" + mID + "').on('change', function() {\n");
		script.append("    var emptyField = $('#smartComboList_" + mID + "').select2('data') === null || $('#smartComboList_" + mID + "').select2('data') === undefined; \n");
		script.append("    var line = new Array();");
		script.append("    var hiddens = \"\";\n");
		script.append("    var codPrimary = \"\";\n");

		String auxCONCAT = "";
		boolean addPrimary = false;

		script.append("    if (!emptyField) { \n");
		for (ColumnDisplay p : super.mPrimaries) {
			addPrimary = true;
			script.append("      if(codPrimary != \"\"){ \n");
			script.append("        codPrimary += \"_\"; \n");
			script.append("      } \n");
			script.append("      codPrimary += $('#smartComboList_" + mID + "').select2('data')." + getAlias(p) + "; \n");

			for (ColumnDisplay cd : mColumnsDisplay) {
				if (p.getName().equals(cd.getName())) {
					addPrimary = false;
					break;
				}
			}

			if (!auxCONCAT.equals("")) {
				auxCONCAT += BuildSql.getConcatCharacter() + "'_'" + BuildSql.getConcatCharacter();
			}
			auxCONCAT += BuildSql.getToChar(p.getName());

			if (addPrimary) {
				script.append("      hiddens += \"<input type='hidden' id='_column_" + mID + "_" + getAlias(p) + "_\" + codPrimary + \"' name='_column_" + mID + "_" + getAlias(p) + "_\"+codPrimary+\"' value='\" + ( emptyField ? '' : $('#smartComboList_" + mID + "').select2('data')." + getAlias(p) + " ) + \"' />\";\n");
			}
		}

		script.append("      hiddens += \"<input type='hidden' id='_column_pk_" + mID + "' name='_column_pk_" + mID + "' value='\" + codPrimary + \"' />\";\n");
		for (ColumnDisplay cd : mColumnsDisplay) {
			script.append("      hiddens += \"<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "_\"+codPrimary+\"' name='_column_" + mID + "_" + getAlias(cd) + "_\"+codPrimary+\"' value='\" + ( emptyField ? '' : $('#smartComboList_" + mID + "').select2('data')." + getAlias(cd) + " ) + \"' />\";\n");

			script.append("      hiddens += \"<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "' name='_column_" + mID + "_" + getAlias(cd) + "' value='\" + ( emptyField ? '' : $('#smartComboList_" + mID + "').select2('data')." + getAlias(cd) + " ) + \"' />\";\n");
			script.append("      line.push( emptyField ? '' : $('#smartComboList_" + mID + "').select2('data')." + getAlias(cd) + " );\n");
		}

		for (ColumnDisplay cd : mColumnsHidden) {
			script.append("      hiddens += \"<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "_\"+codPrimary+\"' name='_column_" + mID + "_" + getAlias(cd) + "_\"+codPrimary+\"' value='\" + ( emptyField ? '' : $('#smartComboList_" + mID + "').select2('data')." + getAlias(cd) + " ) + \"' />\";\n");

			script.append("      hiddens += \"<input type='hidden' id='_column_" + mID + "_" + getAlias(cd) + "' name='_column_" + mID + "_" + getAlias(cd) + "' value='\" + ( emptyField ? '' : $('#smartComboList_" + mID + "').select2('data')." + getAlias(cd) + " ) + \"' />\";\n");
		}
		if (isShowDelete()) {
			script.append("      hiddens += \"<i class='fa fa-times font-red' onclick=deleteRow" + mID + "(this.parentNode.parentNode)></i>\";\n");
		}
		else {
			script.append("      hiddens += \"<i class='fa fa-times font-grey'></i>\";\n");
		}
		script.append("      line.push( hiddens );");
		script.append("    } \n");

		script.append("    buscaValor_" + mID + " = '';\n");
		script.append("    if (emptyField) { \n");
		script.append("      try { afterClean" + mID + "(); } catch(e) { } \n");
		script.append("    } \n");
		script.append("    else { \n");
		script.append("      $('#smartComboList_table_" + mID + "').dataTable().fnAddData( line, true );\n");
		script.append("      _limpa_campo_" + mID + "_only();\n");
		script.append("      setListWhere_" + mID + "();");
		script.append("      try { afterFill" + mID + "(); } catch(e) { } \n");
		script.append("    } \n");
		script.append("  });\n");

		if (( mHasValues || mAutoFill ) && mSQL != null && !mSQL.trim().equals("")) {

			String valueSQL = "";
			valueSQL = "SELECT * FROM ( \n";
			valueSQL += mSQL + " \n";
			valueSQL += ") " + BuildSql.getAsSubQuery("SMARTCOMBOLIST_TB_1234") + " \n";
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

						for (int i = 1; i <= mColumnCount; i++) {
							if (i <= mPrimaryCount) {
								id += ( id.trim().equals("") ? "" : "$|$" ) + Database.verifyNull(rs.getObject(i), "0");
							}
							else {
								text += ( text.trim().equals("") ? "" : " - " ) + Database.verifyNull(rs.getObject(i), "0");
							}
							extraAttrs += ", \"" + rsmd.getColumnName(i) + "\": \"" + Database.verifyNull(rs.getObject(i), "0") + "\"";
						}

						script.append("  $('#smartComboList_" + mID + "').select2( 'data', { \"id\": \"" + id + "\", \"text\": \"" + text + "\"" + ( extraAttrs.trim().equals("") ? "" : extraAttrs ) + " } ).trigger('change');\n");
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

		script.append("  setListWhere_" + mID + "();\n");

		if (mColumnsDependency != null && mColumnsDependency.size() > 0) {
			for (ColumnDependency dep : mColumnsDependency) {
				script.append("  $('#smartCombo_" + dep.getRemoteFieldId() + "').on('change', function() {\n");
				script.append("    $('#smartComboList_" + mID + "').select2( 'val', '', true );\n");
				script.append("    _limpa_campos_" + mID + "();\n");
				script.append("    var depValue = $('#smartCombo_" + dep.getRemoteFieldId() + "').val();");
				script.append("    if ( depValue !== undefined && depValue !== null && depValue !== '' ) {\n");
				script.append("      $('#smartComboList_" + mID + "').select2( 'triggerAjax' );\n");
				script.append("    }\n");
				script.append("  });\n");
			}
		}
		else if (mAutoFill && !mHasValues) {
			script.append("  $('#smartComboList_" + mID + "').select2( 'triggerAjax' );\n");
		}
		script.append("});\n");

		script.append("function deleteRow" + mID + "(pObj) {\n");
		script.append("  $('#smartComboList_table_" + mID + "').dataTable().fnDeleteRow( pObj );\n");
		script.append("  setListWhere_" + mID + "();\n");
		script.append("      try { afterDeleteRow" + mID + "(); } catch(e) { } \n");
		script.append("}\n");

		// FILTRO PARA REMOVER OS ITENS DA LISTA DO SQL
		script.append("function setListWhere_" + mID + "() {\n");
		script.append("  filtroLista_" + mID + " = \"" + auxCONCAT + " NOT IN ('0'\";\n");
		script.append("  $('input[name=_column_pk_" + mID + "]').each(function() {\n");
		script.append("    if ( $( this ).val() ) {\n");
		script.append("      filtroLista_" + mID + " += \",'\" + $( this ).val() + \"'\";\n");
		script.append("    }\n");
		script.append("  });\n");
		script.append("  filtroLista_" + mID + " += \")\";\n");
		script.append("}\n");

		script.append("function setWhere_" + mID + "(where) {\n");
		script.append("  filtroExtra_" + mID + " = where;\n");
		script.append("}\n");
		script.append("function setReadOnly_" + mID + "(enable) {\n");
		script.append("  $('#smartComboList_" + mID + "').select2( 'readonly', enable );\n");
		script.append("}\n");
		script.append("function _limpa_campo_" + mID + "_only() {\n");
		script.append("  $('#smartComboList_" + mID + "').select2( 'val', '', true );\n");
		script.append("}\n");
		script.append("function _limpa_campos_" + mID + "() {\n");
		script.append("  $('#smartComboList_table_" + mID + "').dataTable().fnClearTable(); \n");
		script.append("}\n");
		script.append("function _busca_valor_" + mID + "() {\n");
		script.append("  buscaValor_" + mID + " = '';\n");
		script.append("  for (var i = 0; i < arguments.length; i++) {\n");
		script.append("    buscaValor_" + mID + " += (buscaValor_" + mID + " === '' ? '' : ' AND ') + arguments[i];\n");
		script.append("  }\n");
		script.append("  if (buscaValor_" + mID + " !== '') {\n");
		script.append("    $('#smartComboList_" + mID + "').select2( 'triggerAjax' );\n");
		script.append("  }\n");
		script.append("}\n");
		script.append("function submitForm_" + mID + "() {");
		script.append("  $('.select2').select2( 'close' );\n");
		script.append("}");

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

	public String printSmartComboList() {

		this.prepareCombo();

		lHtml.delete(0, lHtml.length());

		lHtml.append("<link rel='stylesheet' type='text/css' href='metronic/global/plugins/select2/select2.css' />\n");
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

	private String getParameter(String param) {
		return getParameter(param, "");
	}

	private String getParameter(String param, String defaultValue) {
		return Database.verifyNull(mUpRequest == null ? mRequest.getParameter(param) : mUpRequest.getParameter(param), defaultValue);
	}
}
