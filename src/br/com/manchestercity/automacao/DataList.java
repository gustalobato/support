
package br.com.manchestercity.automacao;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.manchestercity.SystemConfig;

public class DataList {
	public static final String FAROL_BLUE = "blue";
	public static final String FAROL_BLUE_HOKI = "blue-hoki";
	public static final String FAROL_BLUE_STEEL = "blue-steel";
	public static final String FAROL_BLUE_MADISON = "blue-madison";
	public static final String FAROL_BLUE_CHAMBRAY = "blue-chambray";
	public static final String FAROL_BLUE_EBONYCLAY = "blue-ebonyclay";

	public static final String FAROL_GREEN = "green";
	public static final String FAROL_GREEN_MEADOW = "green-meadow";
	public static final String FAROL_GREEN_SEAGREEN = "green-seagreen";
	public static final String FAROL_GREEN_TURQUOISE = "green-turquoise";
	public static final String FAROL_GREEN_HAZE = "green-haze";
	public static final String FAROL_GREEN_JUNGLE = "green-jungle";

	public static final String FAROL_RED = "red";
	public static final String FAROL_RED_PINK = "red-pink";
	public static final String FAROL_RED_SUNGLO = "red-sunglo";
	public static final String FAROL_RED_INTENSE = "red-intense";
	public static final String FAROL_RED_THUNDERBIRD = "red-thunderbird";
	public static final String FAROL_RED_FLAMINGO = "red-flamingo";

	public static final String FAROL_YELLOW = "yellow";
	public static final String FAROL_YELLOW_GOLD = "yellow-gold";
	public static final String FAROL_YELLOW_CASABLANCA = "yellow-casablanca";
	public static final String FAROL_YELLOW_CRUSTA = "yellow-crusta";
	public static final String FAROL_YELLOW_LEMON = "yellow-lemon";
	public static final String FAROL_YELLOW_SAFFRON = "yellow-saffron";

	public static final String FAROL_PURPLE = "purple";
	public static final String FAROL_PURPLE_PLUM = "purple-plum";
	public static final String FAROL_PURPLE_MEDIUM = "purple-medium";
	public static final String FAROL_PURPLE_STUDIO = "purple-studio";
	public static final String FAROL_PURPLE_WISTERIA = "purple-wisteria";
	public static final String FAROL_PURPLE_SEANCE = "purple-seance";

	public static final String FAROL_GREY = "grey";
	public static final String FAROL_GREY_CASCADE = "grey-cascade";
	public static final String FAROL_GREY_SILVER = "grey-silver";
	public static final String FAROL_GREY_STEEL = "grey-steel";
	public static final String FAROL_GREY_CARRARA = "grey-carrara";
	public static final String FAROL_GREY_GALLERY = "grey-gallery";

	public static final String FAROL_BLACK = "black";
	public static final String FAROL_WHITE = "white";

	protected String mPageHeaderTags = "";
	protected String mPageFooterTags = "";
	protected String mPageFooterScripts = "";

	User mUser;
	Database lcdb = null;
	Connection mConn = null;
	TitleMenu mMenu = null;

	ResultSet rs = null;

	HttpServletRequest request;

	StringBuffer lHtml;

	int mCurPage;
	int mPageCount;
	int mItemCount;
	int mColumnCount;
	int mPrimaryCount;
	DataType mFarolamentoType;

	long mRecordCount = 0;

	boolean mPrint = false;
	boolean mUseQuickSearch = true;

	boolean mFirstOpen = true;

	boolean mShowExportar = true;
	boolean mShowPagination = true;
	boolean mShowQuickSearch = true;
	boolean mShowFarolamento = false;

	boolean mShowNew;
	boolean mShowVoltar;
	boolean mReadOnly;
	boolean mCheckBox;
	boolean mLocalizar;
	boolean mRadioButton;
	boolean mShowMenuBar;
	boolean mShowLegenda;

	String mFieldFarolamento = "";
	String mLabelFarolamento = "";
	String mFarolamentoQuery = "";
	ArrayList<String> mFarolamentos = new ArrayList<String>();

	private String[] mCheckboxContition;

	String mQuickSearch;

	String mBeforeGoURL;
	String mGoURLScript;

	String mColumnOrder = "";
	String mLastSelItem = "";

	String mURL;
	String mPage;
	String mTitle;
	String mSubTitle;
	String mDblAction = "goURL";
	String mExtraTitle = "";
	String mActionPrint = "";
	String mPrimaryOrder = "";
	String mPrimaryDirection = "";

	String[] lAlign = { "left", "center", "right" };

	ArrayList<String> mValuesToKeep = new ArrayList<String>();
	ArrayList<String> mValuesToKeepLabels = new ArrayList<String>();

	String primary[] = new String[10];
	DataColumn columns[] = new DataColumn[50];
	DataColumn rsColumns[] = new DataColumn[50];

	String mSql;
	String mSqlPage;
	String mSqlCount;
	String mForm;
	String mVoltar = "";

	String mOrder;
	String mSentido;
	String mFunction;
	String mCurFilter;
	String mCurLabels;

	String mJSNovo;
	String mJSLocalizar;
	String mJSRadioAction;

	String mControlParameters;
	String mExtraNewParameters;

	String mID = "";
	String mAjaxURL = "";
	String mTable = "";
	String mBaseFilter = "";
	String mHavingClause = "";

	String mLimparFiltro = "";
	String mURLLimparFiltro = "";

	boolean mDistinct = false;

	String mFinalSQL = "";
	String mSQLWithoutOrder = "";

	String mHeaderScripts = "";
	String mDataListExtraParameters = "";
	String mExtraClasses = "";

	private ColumnDisplay mColumnOrderBy;
	private ColumnDisplay mColumnCheckbox;
	private ArrayList<ColumnDisplay> mPrimaries = new ArrayList<ColumnDisplay>();
	private ArrayList<ColumnDisplay> mColumnsDisplay = new ArrayList<ColumnDisplay>();

	private HashMap<String, String> mMapLocalizar = new HashMap<String, String>();
	private HashMap<String, String> mMapDescricao = new HashMap<String, String>();

	private boolean hideDataTableBootstrap = false;

	public void setHideDataTableBootstrap(boolean hideDataTableBootstrap) {
		this.hideDataTableBootstrap = hideDataTableBootstrap;
	}

	public String getID() {
		return mID;
	}

	public void setID(String pValue) {
		mID = pValue;
	}

	public void setAjaxURL(String pValue) {
		mAjaxURL = pValue;
	}

	public void setTable(String pValue) {
		mTable = pValue;
	}

	public void setBaseFilter(String pValue) {
		mBaseFilter = pValue;
	}

	public void setHavingClause(String pValue) {
		mHavingClause = pValue;
	}

	public void setItemCount(int pValue) {
		mItemCount = pValue;
	}

	public void setShowExportar(boolean pValue) {
		mShowExportar = pValue;
	}

	public void setActionPrint(String pValue) {
		mActionPrint = pValue;
	}

	public void setURLLimparFiltro(String pValue) {
		mURLLimparFiltro = pValue;
	}

	public void setJSLocalizar(String pValue) {
		mJSLocalizar = pValue;
	}

	public void setGoUrlScript(String pValue) {
		mGoURLScript = pValue;
	}

	public void setSelItem(String pValue) {
		mLastSelItem = pValue;
	}

	public void setJSRadioAction(String pValue) {
		mJSRadioAction = pValue;
	}

	public void setJSNovo(String pValue) {
		mJSNovo = pValue;
	}

	public void setPrint(boolean pValue) {
		mPrint = pValue;
	}

	public void setLocalizar(boolean pValue) {
		mLocalizar = pValue;
	}

	public void setURL(String pValue) {
		mURL = pValue;
	}

	public void setSentido(String pValue) {
		mSentido = pValue;
	}

	public void setOrdem(String pValue) {
		mOrder = pValue;
	}

	public void setBeforeGoURL(String pValue) {
		mBeforeGoURL = pValue;
	}

	public void setPagina(int pValue) {
		mCurPage = pValue;
	}

	public void setFiltro(String pValue) {
		mCurFilter = pValue;
	}

	public void setFiltroDesc(String pValue) {
		mCurLabels = pValue;
	}

	public void setFuncao(String pValue) {
		mFunction = pValue;
	}

	public void setShowMenuBar(boolean pValue) {
		mShowMenuBar = pValue;
	}

	public void setShowLegenda(boolean pValue) {
		mShowLegenda = pValue;
	}

	public void setVoltar(String pValue) {
		mVoltar = pValue;
	}

	public void setTitulo(String pValue) {
		mTitle = pValue;
	}

	public void setSubTitulo(String pValue) {
		mSubTitle = pValue;
	}

	public void setSQL(String pValue) {
		mSql = pValue;
	}

	public void setSQLCount(String pValue) {
		mSqlCount = pValue;
	}

	public void setForm(String pValue) {
		mForm = pValue;
	}

	public void setCheckbox(boolean pValue) {
		mCheckBox = pValue;
	}

	public void setRadioButton(boolean pValue) {
		mRadioButton = pValue;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setReadOnly(boolean pValue) {
		mReadOnly = pValue;
	}

	public void setControlParameters(String pValue) {
		mControlParameters = pValue;
	}

	public void setParametrosExtras(String pValue) {
		mExtraNewParameters = pValue;
	}

	public void setShowNew(boolean pValue) {
		mShowNew = pValue;
	}

	public void setShowVoltar(boolean pValue) {
		mShowVoltar = pValue;
	}

	public void setExtraTitle(String pValue) {
		mExtraTitle = pValue;
	}

	public void setHeaderScripts(String pValue) {
		mHeaderScripts = pValue;
	}

	public void setDataListExtraParameters(String pValue) {
		mDataListExtraParameters = pValue;
	}

	public void setExtraClasses(String pValue) {
		mExtraClasses = pValue;
	}

	public void setPrimaryDirection(String pDirection) {
		mPrimaryDirection = pDirection;
	}

	public void setShowQuickSearch(boolean pShowQuickSearch) {
		mShowQuickSearch = pShowQuickSearch;
	}

	public void setShowPagination(boolean pShowPagination) {
		mShowPagination = pShowPagination;
	}

	public void setPageHeaderTags(String pPageHeaderTags) {
		mPageHeaderTags = pPageHeaderTags;
	}

	public void setPageFooterTags(String pPageFooterTags) {
		mPageFooterTags = pPageFooterTags;
	}

	public String getPrimaryOrder() {
		return mPrimaryOrder;
	}

	public boolean getShowExportar() {
		return mShowExportar;
	}

	public boolean isCheckbox() {
		return mCheckBox;
	}

	public boolean isRadioButton() {
		return mRadioButton;
	}

	public boolean isShowFarolamento() {
		return mShowFarolamento;
	}

	public String getControlParameters() {
		return mControlParameters;
	}

	public void setPrimaryOrder(String pOrder) {
		this.setPrimaryOrder(pOrder, "ASC");
	}

	public void setPrimaryOrder(String pOrder, String pDirection) {
		mPrimaryOrder = pOrder;
		mPrimaryDirection = pDirection;
	}

	public void setColumnOrderBy(String pName, Operation pOperation, DataType pDataType, Ordination pOrdination) {
		this.mColumnOrderBy = new ColumnDisplay();

		this.mColumnOrderBy.setName(pName);
		this.mColumnOrderBy.setOperation(pOperation);
		this.mColumnOrderBy.setType(pDataType);
		this.mColumnOrderBy.setOrdination(pOrdination);
	}

	public void setColumnCheckbox(String pName, DataType pDataType, Operation pOperation) {
		mColumnCheckbox = new ColumnDisplay();

		mColumnCheckbox.setName(pName);
		mColumnCheckbox.setType(pDataType);
		mColumnCheckbox.setOperation(pOperation);
	}

	public void addPrimary(String pName, DataType pDataType) {
		ColumnDisplay primary = new ColumnDisplay();

		primary.setName(pName);
		primary.setType(pDataType);

		addPrimary(primary.getName());

		mPrimaries.add(primary);
	}

	public void addColumnDisplay(ColumnDisplay pColumn) {
		mColumnsDisplay.add(pColumn);
	}

	public ColumnDisplay addColumnDisplay(String pName, String pLabel, String pWidth, DataType pDataType, Operation pOperation, ColumnAlign pAlign) {
		ColumnDisplay columnDisplay = new ColumnDisplay();

		columnDisplay.setName(pName);
		columnDisplay.setLabel(pLabel);
		columnDisplay.setWidth(pWidth);
		columnDisplay.setType(pDataType);
		columnDisplay.setOperation(pOperation);
		columnDisplay.setAlign(pAlign);

		mColumnsDisplay.add(columnDisplay);

		return columnDisplay;
	}

	// A COLUNA PODERÁ SER ORDENADA NO SQL DE ACORDO COM O pName PASSADO (ex.: "SLCPI.CD_SLCPI AS TESTE")
	public ColumnDisplay addColumnIcon(String pName, String pLabel, DataType pDataType, Operation pOperation, String pIcon, String pPageToGo, String pJSExecute) {
		ColumnDisplay columnDisplay = new ColumnDisplay();

		columnDisplay.setName(pName);
		columnDisplay.setLabel(pLabel);
		columnDisplay.setWidth("1%");
		columnDisplay.setType(pDataType);
		columnDisplay.setOperation(pOperation);
		columnDisplay.setAlign(ColumnAlign.CENTER);
		columnDisplay.setIcon(pIcon);
		columnDisplay.setPageToGo(pPageToGo);
		columnDisplay.setJSExecute(pJSExecute);

		mColumnsDisplay.add(columnDisplay);

		return columnDisplay;
	}

	// String[][] pKeyFilters = new String[x][2];
	// pKeyFilters[x][0]: deve conter a parte do filtro que será passada como VARCHAR para a função
	// pKeyFilters[x][1]: deve conter a parte do filtro que será o valor da Query externa (da Display)
	public ColumnDisplay addColumnList(String pLabel, String pWidth, String pNameReturn, String pTable, String pColumn, String pWhere, String[][] pKeyFilters) {
		ColumnDisplay columnDisplay = new ColumnDisplay();

		columnDisplay.setLabel(pLabel);
		columnDisplay.setWidth(pWidth);
		columnDisplay.setType(DataType.TEXT);
		columnDisplay.setOperation(null);
		columnDisplay.setAlign(ColumnAlign.LEFT);

		columnDisplay.setName(BuildSql.getColumnList(pNameReturn, pTable, pColumn, pWhere, pKeyFilters));

		mColumnsDisplay.add(columnDisplay);

		return columnDisplay;
	}

	public void setPage(String pValue) {
		mPage = pValue;
		mURL = pValue;
	}

	public void setQuickSearch(String pValue) {
		mQuickSearch = pValue;
		mUseQuickSearch = true;
	}

	public void setFirstOpen(boolean pFirstOpen) {
		mFirstOpen = pFirstOpen;
	}

	public void addValueToKeep(String pLabel, String pValue) {
		mValuesToKeep.add(pValue);
		mValuesToKeepLabels.add(pLabel);
	}

	public String getCorFarolamento(String pFieldValue) {

		for (int i = 0; i < mFarolamentos.size(); i++) {
			if (mFarolamentos.get(i).toString().split("\\|")[0].trim().equals(pFieldValue.trim())) {
				return mFarolamentos.get(i).toString().split("\\|")[1];
			}
		}

		return "";
	}

	public String getNameFarolamento(String pFieldValue) {

		for (int i = 0; i < mFarolamentos.size(); i++) {
			if (mFarolamentos.get(i).toString().split("\\|")[0].equals(pFieldValue)) {
				return mFarolamentos.get(i).toString().split("\\|")[2];
			}
		}

		return "";
	}

	public void setCheckboxCondition(String pFieldCondition, String pValueDisabled) {
		mCheckboxContition = new String[] { pFieldCondition, pValueDisabled };
	}

	public void addFarolamento(String pValue, String pCorFarolamento, String pName) {
		mShowFarolamento = true;
		mFarolamentos.add(pValue + "|" + pCorFarolamento + "|" + pName);
	}

	public void setFieldFarolamento(String pValue, String pLabel, DataType pType) {
		setFieldFarolamento(pValue, pLabel, pType, "");
	}

	public void setFieldFarolamento(String pValue, String pLabel, DataType pType, String pQueryField) {
		mFieldFarolamento = pValue;
		mLabelFarolamento = pLabel;
		mFarolamentoType = pType;
		mFarolamentoQuery = pQueryField;
	}

	private void init() {
		mFarolamentoType = DataType.TEXT;
		mFarolamentoQuery = "";
		mShowFarolamento = false;
		lHtml = new StringBuffer("");
		lcdb = new Database(mUser);
		mConn = null;
		rs = null;

		mCheckboxContition = new String[2];

		mMenu = new TitleMenu(mUser);

		mLastSelItem = "";

		mBeforeGoURL = "";

		mGoURLScript = "";

		mPrint = false;
		mLocalizar = true;

		mPrimaryOrder = "";
		mPrimaryDirection = "";
		mActionPrint = "";
		mJSLocalizar = "";
		mJSRadioAction = "";
		mJSNovo = "";
		mURLLimparFiltro = "";

		mTitle = "";
		mSubTitle = "";

		mPrimaryCount = 0;
		mColumnCount = 0;
		mRecordCount = 0;

		mSql = "";
		mSqlCount = "";
		mForm = "";

		mCheckBox = false;
		mRadioButton = false;
		mReadOnly = false;

		mPage = "";
		mControlParameters = "";
		mExtraNewParameters = "";

		mOrder = "";
		mSentido = "";
		mCurPage = 1;
		mCurFilter = "";
		mCurLabels = "";

		mFunction = "";

		mShowNew = true;
		mShowVoltar = false;

		mPageCount = 20;
		mItemCount = 25;

		mFieldFarolamento = "";
		mLabelFarolamento = "";
		mShowMenuBar = true;
		mShowLegenda = true;

		mQuickSearch = "";
		mUseQuickSearch = false;

		mFirstOpen = true;

		mShowPagination = true;
		mShowQuickSearch = true;

		mHeaderScripts = "";
	}

	public DataList(User pUser) {
		this.mUser = pUser;
		this.init();
	}

	public TitleMenu getMenu() {
		return mMenu;
	}

	public void menuExtraButton(String pLabel, String pJSAction, String pIcon) {
		menuExtraButton("", pLabel, pJSAction, pIcon, "");
	}

	public void menuExtraButton(String pID, String pLabel, String pJSAction, String pIcon, String pButtonColor) {
		if (!pLabel.equals("")) {
			mMenu.addExtraButton(pID, pLabel, pJSAction, pIcon, pButtonColor, false);
		}
	}

	public String menuConfig() {
		String lStr = "";

		String valuesKeep = "";
		for (int x = 0; x < mValuesToKeep.size(); x++) {
			valuesKeep += "&" + mValuesToKeepLabels.get(x).toString() + "=" + mValuesToKeep.get(x).toString();
		}

		if (!mReadOnly && mShowMenuBar) {
			mMenu.setTitle(mTitle);

			mMenu.setShowAllButtons(false);

			mMenu.setIDNovo("_botao_novo_" + mID);
			mMenu.setShowNovo(mShowNew);
			if (mJSNovo.equals("")) {
				mMenu.setJSNovo("goURL('" + mForm + "?acao=ins" + mControlParameters + mExtraNewParameters + valuesKeep + "','" + mForm.replace(".", "") + "')");
			}
			else {
				mMenu.setJSNovo(mJSNovo);
			}

			mMenu.setIDVoltar("_botao_voltar_" + mID);
			mMenu.setShowVoltar(mShowVoltar);
			if (mMenu.getJSVoltar().equals("")) {
				mMenu.setJSVoltar("goURL('" + mVoltar + "&acao=upd" + mControlParameters + mExtraNewParameters + valuesKeep + "')");
			}

			if (mMenu.getShowFavorito() && request != null) {
				mMenu.setLabelFavorito("");
				mMenu.setShowFavorito(true, request);
			}

			if (!mCurFilter.equals("")) {
				if (!mCurFilter.equals("")) {
					mMenu.addExtraButton(mUser.getTermo("LIMPAFILTRO"), "limpaFiltro();", SystemIcons.ICON_BAN);
				}
			}

			if (mJSLocalizar.length() > 0) {
				mMenu.setShowLocalizar(true);
				mMenu.setJSLocalizar(mJSLocalizar);
			}
			else {
				mMenu.setShowLocalizar(false);
				if (mLocalizar) {
					mMenu.addModalButton("_botao_localizar_" + mID, "_search_modal_" + mID, mUser.getTermo("LOCALIZAR"), SystemIcons.ICON_SEARCH, DataList.FAROL_BLUE_STEEL);
				}
			}

			lStr += mMenu.ShowMenu();

		}

		return lStr;
	}

	public DataColumn getColumn(int i) {
		DataColumn coluna = new DataColumn();
		try {
			coluna = columns[i];
		}
		catch (Exception e) {
			return new DataColumn();
		}

		return coluna;
	}

	// BUSCA NO REQUEST OS PARAMETROS NECESSARIOS PARA A CHAMADA AJAX
	public void requestParameters() {
		if (request != null) {
			String sSearch = Database.verifyNull(request.getParameter("sSearch"));

			String iDisplayStart = Database.verifyNull(request.getParameter("iDisplayStart"));
			String iDisplayLength = Database.verifyNull(request.getParameter("iDisplayLength"));

			String iSortCol = Database.verifyNull(request.getParameter("iSortCol_0"));
			String sSortDir = Database.verifyNull(request.getParameter("sSortDir_0"));

			mCurFilter = Database.verifyNull(request.getParameter("filtro"));

			int countColumn = 1;
			if (mCheckBox || mRadioButton) {
				countColumn--;
			}
			if (mShowFarolamento) {
				countColumn--;
			}

			this.setQuickSearch(sSearch);
			this.setItemCount(Database.verifyNullInt(iDisplayLength));
			this.setPagina(Database.verifyNullInt(iDisplayStart) / Database.verifyNullInt(iDisplayLength));
			this.setPrimaryDirection(sSortDir);

			if (mTable != null && !mTable.trim().equals("")) {
				int test = Database.verifyNullInt(iSortCol) + ( countColumn - 1 );
				ColumnDisplay coluna = mColumnsDisplay.get(test < 0 ? 0 : test);
				this.setColumnOrderBy(coluna.getName(), coluna.getOperation(), coluna.getType(), sSortDir.equalsIgnoreCase("DESC") ? Ordination.DESC : Ordination.ASC);
			}
			else {
				this.setOrdem(getColumn(Database.verifyNullInt(iSortCol) + countColumn).getField());
			}
		}
	}

	// ADICIONAR NOVAS COLUNAS
	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth) {
		return addColumn(pField, pAlias, pAlign, pType, pWidth, false, false, pType, "", true, true);
	}

	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth, int pTypeOrder) {
		return addColumn(pField, pAlias, pAlign, pType, pWidth, false, false, pTypeOrder, "", true, true);
	}

	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth, String pRealName) {
		return addColumn(pField, pAlias, pAlign, pType, pWidth, false, false, pType, pRealName, true, true);
	}

	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth, boolean pIsOrdenable) {
		return addColumn(pField, pAlias, pAlign, pType, pWidth, false, false, pType, "", pIsOrdenable, true);
	}

	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth, String pRealName, boolean pIsInterval) {
		return addColumn(pField, pAlias, pAlign, pType, pWidth, false, pIsInterval, pType, pRealName, true, true);
	}

	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth, boolean pHiddenFilter, boolean pIsInterval) {
		return addColumn(pField, pAlias, pAlign, pType, pWidth, pHiddenFilter, pIsInterval, pType, "", true, true);
	}

	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth, boolean pHiddenFilter, boolean pIsOrdenable, boolean pUseInQuickSearch) {
		return addColumn(pField, pAlias, pAlign, pType, pWidth, pHiddenFilter, false, pType, "", pIsOrdenable, pUseInQuickSearch);
	}

	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth, boolean pHiddenFilter, boolean pIsInterval, int pTypeOrder, String pRealName, boolean pIsOrdenable, boolean pUseInQuickSearch, boolean pShowFilter) {
		return addColumn(pField, pAlias, pAlign, pType, pWidth, pHiddenFilter, pIsInterval, pTypeOrder, pRealName, pIsOrdenable, pUseInQuickSearch, pShowFilter, true);
	}

	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth, boolean pHiddenFilter, boolean pIsInterval, int pTypeOrder, String pRealName, boolean pIsOrdenable, boolean pUseInQuickSearch, boolean pShowFilter, boolean pShowExcel) {
		DataColumn lCol = new DataColumn();

		lCol.setAlias(pAlias);
		lCol.setField(pField);
		lCol.setAlign(pAlign);
		lCol.setType(pType);
		lCol.setTypeOrder(pTypeOrder);
		lCol.setWidth(pWidth);
		lCol.setHiddenSearch(pHiddenFilter);
		lCol.setIsInterval(pIsInterval);
		lCol.setRealName(( pRealName.equals("") ? pField : pRealName ));
		lCol.setIsOrdenable(pIsOrdenable);
		lCol.setUseInQuickSearch(pUseInQuickSearch);
		lCol.setShowFilter(pShowFilter);
		lCol.setShowExcel(pShowExcel);
		mColumnCount++;
		columns[mColumnCount] = lCol;
		return lCol;
	}

	public DataColumn addColumnList(String pField, String pAlias, int pAlign, int pType, String pWidth, String pQuery, String pStringReplaceInWhere, String pSeparator, String pFieldList) {
		return addColumnList(pField, pAlias, pAlign, pType, pWidth, pQuery, pStringReplaceInWhere, pSeparator, pFieldList, "", true);
	}

	public DataColumn addColumnList(String pField, String pAlias, int pAlign, int pType, String pWidth, String pQuery, String pStringReplaceInWhere, String pSeparator, String pFieldList, String pRealNameFieldList) {
		return addColumnList(pField, pAlias, pAlign, pType, pWidth, pQuery, pStringReplaceInWhere, pSeparator, pFieldList, pRealNameFieldList, true);
	}

	public DataColumn addColumn(String pField, String pAlias, int pAlign, int pType, String pWidth, boolean pHiddenFilter, boolean pIsInterval, int pTypeOrder, String pRealName, boolean pIsOrdenable, boolean pUseInQuickSearch) {
		return addColumn(pField, pAlias, pAlign, pType, pWidth, pHiddenFilter, pIsInterval, pTypeOrder, pRealName, pIsOrdenable, pUseInQuickSearch, true);
	}

	public DataColumn addColumnList(String pField, String pAlias, int pAlign, int pType, String pWidth, String pQuery, String pStringReplaceInWhere, String pSeparator, String pFieldList, String pRealNameFieldList, boolean pShowExcel) {
		DataColumn lCol = new DataColumn();

		lCol.setAlias(pAlias);
		lCol.setField(pField);
		lCol.setAlign(pAlign);
		lCol.setType(pType);
		lCol.setTypeOrder(pType);
		lCol.setWidth(pWidth);
		lCol.setHiddenSearch(true);
		lCol.setIsInterval(false);
		lCol.setFieldList(pFieldList);
		lCol.setRealName(pRealNameFieldList.trim().equals("") ? pFieldList : pRealNameFieldList);
		lCol.setQueryList(pQuery);
		lCol.setStringReplace(pStringReplaceInWhere);
		lCol.setSeparator(pSeparator);
		lCol.setShowExcel(pShowExcel);

		mColumnCount++;
		columns[mColumnCount] = lCol;
		return lCol;
	}

	public void addPrimary(String pField) {
		mPrimaryCount++;
		primary[mPrimaryCount] = pField;
	}

	public String printDataTable(String pTableID) {
		StringBuffer lHtml = new StringBuffer();

		StringBuffer tableColumns = new StringBuffer();

		if (mRadioButton || mCheckBox) {
			tableColumns.append("<th style='width:25px; align:center;'>&nbsp;</th>");
		}

		if (mShowFarolamento) {
			tableColumns.append("<th style='width:50px; align:center;'>&nbsp;</th>");
		}

		if (mTable != null && !mTable.trim().equals("")) {
			for (ColumnDisplay coluna : mColumnsDisplay) {
				if (coluna != null) {
					tableColumns.append("<th style='width:" + coluna.getWidth() + "; align:" + coluna.getAlign() + "'>\n");
					tableColumns.append(coluna.getLabel());
					tableColumns.append("\n</th>\n");
				}
			}
		}
		else {
			for (DataColumn coluna : columns) {
				if (coluna != null) {
					tableColumns.append("<th style='width:" + coluna.getWidth() + "; align:" + lAlign[coluna.getAlign()] + "'>\n");
					tableColumns.append(coluna.getAlias());
					tableColumns.append("\n</th>\n");
				}
			}
		}

		String funcao = "";
		if (request != null) {
			funcao = Database.verifyNull(request.getParameter("funcao"));
		}
		this.getFilterRequest();

		// BEGIN TABLE
		lHtml.append("    <form method='post' target='_self' action='" + mURL + "' accept-charset='" + mUser.getUserCharset() + "' id='_frm_" + mID + "' name='_frm_" + mID + "'>\n");
		lHtml.append("    <input type='hidden' name='limpar' value=''>\n");
		lHtml.append("    <input type='hidden' name='funcao' value='" + funcao + "'>\n");
		lHtml.append("    <input type='hidden' name='" + pTableID + "_firstOpen' id='" + pTableID + "_firstOpen' value='" + mFirstOpen + "'>\n");

		lHtml.append("    <textarea name='filtro' style='visibility:hidden; position:absolute'>" + ( mCurLabels + " | " + mCurFilter.replaceAll(">", "&gt;") ) + "</textarea> \n");
		lHtml.append("    <textarea name='baseFilter' style='visibility:hidden; position:absolute'>" + mBaseFilter + "</textarea> \n");
		lHtml.append("    <table class='table table-scrollable table-striped table-bordered table-hover " + mExtraClasses + "' id='" + pTableID + "'>\n");
		lHtml.append("      <thead>\n");
		lHtml.append("        <tr>\n");
		lHtml.append(tableColumns.toString());
		lHtml.append("        </tr>\n");
		lHtml.append("      </thead>\n");
		lHtml.append("    </table>\n");
		if (mJSLocalizar.length() <= 0) {
			lHtml.append(buildDataFilter());
		}
		lHtml.append("    </form>\n");
		// END TABLE

		return lHtml.toString();
	}

	@SuppressWarnings( "deprecation" )
	public String printTableScript(String pTableID, String pAjaxSource) {
		StringBuffer lHtml = new StringBuffer();

		// DETECTAR INDEX DE ORDENACAO INICIAL
		int count = 0;
		int countColumns = 0;
		String disableSorting = "";

		ArrayList<ColumnAlign> aligns = new ArrayList<ColumnAlign>();

		if (mRadioButton || mCheckBox) {
			count++;
			countColumns++;
			disableSorting = "0";
			aligns.add(ColumnAlign.CENTER);
		}

		if (mTable != null && !mTable.trim().equals("")) {
			for (ColumnDisplay column : mColumnsDisplay) {
				if (mColumnOrderBy == null) {
					this.setColumnOrderBy(column.getName(), column.getOperation(), column.getType(), Ordination.ASC);
				}

				if (column.getName().equalsIgnoreCase(mColumnOrderBy.getName())) {
					break;
				}

				countColumns++;
			}

			for (ColumnDisplay column : mColumnsDisplay) {
				aligns.add(column.getAlign());
			}

			count += mColumnsDisplay.size();
		}
		else {
			if (mShowFarolamento) {
				count++;
				countColumns++;
				disableSorting += disableSorting.equals("") ? "0" : ", 1";
				aligns.add(ColumnAlign.CENTER);
			}

			for (int i = 0; i < columns.length; i++) {
				if (columns[i] != null && mPrimaryOrder.trim().equals("")) {
					mPrimaryOrder = columns[i].getField();
				}

				if (columns[i] != null && columns[i].getField().equals(mPrimaryOrder)) {
					countColumns += ( i - 1 );
					break;
				}
			}

			for (int i = 0; i < columns.length; i++) {
				if (columns[i] != null) {
					count++;

					aligns.add(columns[i].mAlign == 0 ? ColumnAlign.LEFT : ( columns[i].mAlign == 1 ? ColumnAlign.CENTER : ColumnAlign.RIGHT ));
				}
			}
		}

		while (aligns.size() < count) {
			aligns.add(ColumnAlign.LEFT);
		}

		lHtml.append("<style>\n");
		lHtml.append("._" + pTableID + "_right  { text-align: right;  } \n");
		lHtml.append("._" + pTableID + "_left   { text-align: left;   } \n");
		lHtml.append("._" + pTableID + "_center { text-align: center; } \n");
		lHtml.append("._" + pTableID + "_padding_right { padding-right: 4px; } \n");
		lHtml.append("</style>\n");
		lHtml.append("<script>\n");
		lHtml.append("var table_" + pTableID + " = function () { \n");
		lHtml.append("  var init" + pTableID + " = function () { \n");
		lHtml.append("    var table = $('#" + pTableID + "'); \n");

		// TABLE TOOLS SAMPLES: https://www.datatables.net/release-datatables/extras/TableTools/

		// SET TABLETOOL BUTTONS AND BUTTON CONTAINER
		lHtml.append("    $.extend(true, $.fn.DataTable.TableTools.classes, { \n");
		lHtml.append("      'container': 'btn-group tabletools-btn-group pull-right hidden-xs hidden-sm', \n");
		lHtml.append("      'buttons': { \n");
		lHtml.append("        'normal': 'btn btn-sm blue-steel', \n");
		lHtml.append("        'disabled': 'btn btn-sm blue-steel disabled' \n");
		lHtml.append("      } \n");
		lHtml.append("    }); \n");

		lHtml.append("    var oTable = table.dataTable({ \n");
		lHtml.append("      'bStateSave': true, \n");
		lHtml.append("		'fnStateLoadParams': function (oSettings, oData) { \n");
		lHtml.append("      	if($('#" + pTableID + "_firstOpen').val() == 'true') { \n");
		lHtml.append("				oData.search.search = ''; \n");
		lHtml.append("				$('#" + pTableID + "_firstOpen').val(false); \n");
		lHtml.append("			} \n");
		lHtml.append("    	}, \n");
		lHtml.append("      'sDom': \"<'row'<'col-md-6 col-sm-12'fl><'col-md-6 col-sm-12'T>r><'table-scrollable't><'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>\", \n"); // HORIZONTAL SCROLLABLE DATATABLE
		lHtml.append("      'aaSorting': [ [" + countColumns + ", '" + ( mColumnOrderBy == null || mColumnOrderBy.getOrdination() == null ? "asc" : mColumnOrderBy.getOrdination().toString().toLowerCase() ) + "'] ], \n");
		if (!disableSorting.equals("")) {
			lHtml.append("      'aoColumnDefs': [ { \n");
			lHtml.append("        'bSortable': false, \n");
			lHtml.append("        'aTargets': [ " + disableSorting + " ] \n");
			lHtml.append("      } ], \n");
		}
		lHtml.append("      'aoColumns': [ \n");
		for (int c = 0; c < count; c++) {
			lHtml.append("        { 'aaData': '" + c + "', 'sClass' : '_" + pTableID + "_" + aligns.get(c).toString().toLowerCase() + "' }" + ( c == ( count - 1 ) ? "" : "," ) + " \n");
		}
		lHtml.append("      ], \n");
		lHtml.append("      'aLengthMenu': [ \n");
		lHtml.append("        [25, 50, 75, 100, -1], \n");
		lHtml.append("        [25, 50, 75, 100, '" + mUser.getTermo("TODOS") + "'] \n"); // CHANGE PER PAGE LABELS
		lHtml.append("      ], \n");
		// lHtml.append("\"scrollX\": true,\n");
		// lHtml.append("\"width\": \"1422px\",\n");
		lHtml.append("      'iDisplayLength': 25, \n");
		lHtml.append("      'sPaginationType': 'bootstrap_full_number', \n");
		lHtml.append("      'bPaginate': " + mShowPagination + ", \n");
		lHtml.append("      'bFilter': " + mShowQuickSearch + ", \n");
		lHtml.append("      'bProcessing': true, \n");
		lHtml.append("      'bServerSide': true, \n"); // SERVER SIDE PROCESSING
		lHtml.append("      'sAjaxSource': '" + pAjaxSource + ( ( pAjaxSource.contains("?") ? "&" : "?" ) + "filtro=" + URLEncoder.encode(mCurFilter) ) + "', \n"); // AJAX SOURCE
		lHtml.append("      'sAjaxDataProp': 'aaData', \n");
		lHtml.append("      'oTableTools': { \n");
		lHtml.append("        'sSwfPath': 'metronic/global/plugins/data-tables/tabletools/swf/copy_csv_xls_pdf.swf', \n"); // CAMINHO DO ARQUIVO SWF QUE EXPORTA ARQUIVOS EM PDF E EXCEL
		lHtml.append("        'aButtons': [{ \n");
		lHtml.append("            'sExtends': 'pdf', \n");
		lHtml.append("            'sButtonText': '" + mUser.getTermo("PDF") + "' \n");
		lHtml.append("          }, { \n");
		lHtml.append("            'sExtends': 'xls', \n");
		lHtml.append("            'sButtonText': '" + mUser.getTermo("EXCEL") + "' \n");
		lHtml.append("          }, { \n");
		lHtml.append("            'sExtends': 'print', \n");
		lHtml.append("            'sButtonText': '" + mUser.getTermo("IMPRIMIR") + "', \n");
		lHtml.append("            'sInfo': \"<h2><span class='label bg-" + DataList.FAROL_BLUE_STEEL + "'><b>" + Utils.replaceAll(mUser.getTermo("DATATABLEPRINT"), "\"", "&quot;") + "</b></span></h2>\" \n");
		lHtml.append("          } \n");
		lHtml.append("        ] \n");
		lHtml.append("      }, \n");
		lHtml.append("      'oLanguage': { \n");
		lHtml.append("        'sEmptyTable': '" + mUser.getTermo("MSGNENHUMREGENC") + "', \n");
		lHtml.append("        'sInfo': '" + mUser.getTermo("DATATABLEINFO") + "', \n");
		lHtml.append("        'sInfoEmpty': '" + mUser.getTermo("MSGNENHUMREGENC") + "', \n");
		lHtml.append("        'sInfoFiltered': ' (" + mUser.getTermo("DATATABLEFILTER") + ") ', \n");
		lHtml.append("        'sInfoThousands': '" + mUser.getUserNumberMil() + "', \n");
		lHtml.append("        'sLengthMenu': '_MENU_ " + mUser.getTermo("REGISTROS") + "', \n");
		lHtml.append("        'sLoadingRecords': '&nbsp;<img src=\"metronic/global/img/loading-spinner-grey.gif\"/>&nbsp;" + mUser.getTermo("AGUARDE") + "...', \n");
		lHtml.append("        'sProcessing': '&nbsp;<img src=\"metronic/global/img/loading-spinner-grey.gif\"/>&nbsp;" + mUser.getTermo("AGUARDE") + "...', \n");
		lHtml.append("        'sSearch': '" + mUser.getTermo("BUSCAR") + "', \n");
		lHtml.append("        'sZeroRecords': '" + mUser.getTermo("MSGNENHUMREGENC") + "', \n");
		lHtml.append("        'oPaginate': { \n");
		lHtml.append("          'sFirst': '" + mUser.getTermo("PAGEFIRST") + "', \n");
		lHtml.append("          'sPrevious': '" + mUser.getTermo("PAGEPREVIOUS") + "', \n");
		lHtml.append("          'sNext': '" + mUser.getTermo("PAGENEXT") + "', \n");
		lHtml.append("          'sLast': '" + mUser.getTermo("PAGELAST") + "' \n");
		lHtml.append("        }");
		lHtml.append("      }, \n");
		lHtml.append("      'fnDrawCallback': function( oSettings ) {");
		lHtml.append("        jQuery('.page-content').attr('style', '');");
		lHtml.append("        try { top.closeChargerPage('fr' + window.name.replace('id', '')); } catch(e) {} \n");
		lHtml.append("        jQuery('.tooltips').tooltip();");
		lHtml.append("      }, ");
		lHtml.append("      'fnServerData': function (sUrl, aoData, fnCallback, oSettings) { \n");
		lHtml.append("        oSettings.jqXHR = $.ajax( { \n");
		lHtml.append("          'cache':  false, \n");
		lHtml.append("          'url':  sUrl, \n");
		lHtml.append("          'data': aoData, \n");
		lHtml.append("          'success': function (json) { \n");
		lHtml.append("            if (json.sError) { \n");
		lHtml.append("              oSettings.oApi._fnLog( oSettings, 0, json.sError ); \n");
		lHtml.append("            } \n");
		lHtml.append("            $(oSettings.oInstance).trigger('xhr', [oSettings, json]); \n");
		lHtml.append("            fnCallback( json ); \n");
		lHtml.append("            Metronic.initAjax();");
		lHtml.append("          }, \n");
		lHtml.append("          'dataType': 'json', \n");
		lHtml.append("          'cache': false, \n");
		lHtml.append("          'type': oSettings.sServerMethod, \n");
		lHtml.append("          'error': function (xhr, error, thrown) { \n");
		lHtml.append("			      if (xhr.responseText.indexOf('function open'+'Login()') > 0) { \n");
		lHtml.append("              var lStr = xhr.responseText; \n");
		lHtml.append("              var lEval = ''; \n");
		lHtml.append("              if (lStr.indexOf('<scr'+'ipt') >= 0) { \n");
		lHtml.append("                var lScript = lStr.split('<scr'+'ipt'); \n");
		lHtml.append("                for (i = 0; i < lScript.length; i++) { \n");
		lHtml.append("                  if (lScript[i].indexOf('</scr'+'ipt>') != '-1') { \n");
		lHtml.append("                    lScriptAux = lScript[i].split('</scr'+'ipt>'); \n");
		lHtml.append("                    lEval += lScriptAux[0].substring(lScriptAux[0].indexOf('>') + 1); \n");
		lHtml.append("                  } \n");
		lHtml.append("                } \n");
		lHtml.append("              } \n");
		lHtml.append("              if (lEval != '') { \n");
		lHtml.append("                lEval = lEval.replace('function submitForm()', 'function oldSubmitForm()'); \n");
		lHtml.append("                jQuery.globalEval(lEval); \n");
		lHtml.append("              } \n");
		lHtml.append("            } \n");
		lHtml.append("            else if (error == 'parsererror') { \n");
		lHtml.append("              oSettings.oApi._fnLog(oSettings, 0, '" + mUser.getTermo("MSGERRODISPLAY") + "'); \n");
		lHtml.append("            } \n");
		lHtml.append("          } \n");
		lHtml.append("        } ); \n");
		lHtml.append("      }" + ( !mDataListExtraParameters.trim().equals("") ? ", " + mDataListExtraParameters : "" ));
		lHtml.append("    }); \n");
		lHtml.append("    oTable.fnSetFilteringDelay(1000); \n");

		lHtml.append("    var tableWrapper = $('#" + pTableID + "_wrapper'); \n"); // DATATABLE CREATES THE TABBLE WRAPPER BY ADDING WITH ID {table_id}_wrapper
		lHtml.append("    jQuery('.dataTables_filter', tableWrapper).addClass('pull-left _" + pTableID + "_padding_right'); \n"); // MODIFY TABLE SEARCH INPUT
		lHtml.append("    jQuery('.dataTables_length', tableWrapper).addClass('pull-left _" + pTableID + "_padding_right'); \n"); // MODIFY TABLE LENGTH INPUT
		lHtml.append("    jQuery('.dataTables_filter input', tableWrapper).addClass('form-control input-small input-inline'); \n"); // MODIFY TABLE SEARCH INPUT
		lHtml.append("    jQuery('.dataTables_length select', tableWrapper).addClass('form-control input-small'); \n"); // MODIFY TABLE PER PAGE DROPDOWN
		lHtml.append("    jQuery('.dataTables_length select', tableWrapper).select2(); \n"); // INITIALIZE SELECT2 DROPDOWN
		lHtml.append("  } \n");

		lHtml.append("	return { \n");
		lHtml.append("    init: function () { \n");
		lHtml.append("      if (!jQuery().dataTable) { \n");
		lHtml.append("        return; \n");
		lHtml.append("      } \n");
		lHtml.append("      init" + pTableID + "(); \n");
		lHtml.append("    } \n");
		lHtml.append("  }; \n");
		lHtml.append("}(); \n");

		lHtml.append("");
		lHtml.append("function submitForm() {");
		lHtml.append("  var table = $('#" + pTableID + "').dataTable(); \n");
		lHtml.append("  table.fnReloadAjax(); \n");
		lHtml.append("}");
		lHtml.append("</script>\n");

		return lHtml.toString();
	}

	public String printTableInit(String pTableID) {
		StringBuffer out = new StringBuffer();

		String valuesKeep = "";
		for (int x = 0; x < mValuesToKeep.size(); x++) {
			valuesKeep += "&" + mValuesToKeepLabels.get(x).toString() + "=" + mValuesToKeep.get(x).toString();
		}

		out.append("      table_" + pTableID + ".init(); \n");
		out.append("      var auxTable = $('#" + pTableID + "').DataTable(); \n");
		out.append("      $('#" + pTableID + " tbody').on('click', 'td', function() { \n");
		out.append("        var myTR = $(this).closest('tr');");
		out.append("        try {");
		out.append("          var arrID = myTR.attr('id').replace('_row_" + pTableID + "_', '').split('|$|'); \n");

		if (!mReadOnly) {
			String auxID = "";
			for (int i = 1; i <= mPrimaryCount; i++) {
				if (primary[i] != null) {
					auxID += "&" + getAlias(primary[i]) + "=' + arrID[" + ( i - 1 ) + "] + '";
				}
			}

			if (mCheckBox || mRadioButton) {
				out.append("          if ($(this).index() > 0) { \n");
				out.append("            auxTable.$('tr.active').removeClass('active'); \n");
				out.append("            myTR.addClass('active'); \n");
				out.append("            showLoading(); \n");
				out.append("            var expDate = new Date(); \n");
				out.append("            expDate.setDate(expDate.getDate() + 2); \n");
				out.append("            document.cookie = '_" + mID + "_" + mForm + "=' + myTR.attr('id') + ';expires=' + expDate.toUTCString(); \n");
				out.append("          }");
			}

			int count = 0;
			boolean printElse = false;

			if (mCheckBox || mRadioButton) {
				out.append("if ($(this).index() == 0) { \n");
				out.append("  void(0); \n");
				out.append("} \n");
				printElse = true;
			}
			for (ColumnDisplay cd : mColumnsDisplay) {
				if (( cd.getJSExecute() != null && !cd.getJSExecute().trim().equals("") ) || ( cd.getPageToGo() != null && !cd.getPageToGo().trim().equals("") )) {
					out.append("          " + ( !printElse ? "" : "else " ) + "if ($(this).index() == " + count + ") { \n");
					if (cd.getJSExecute() != null && !cd.getJSExecute().trim().equals("")) {
						out.append("            " + cd.getJSExecute() + "; \n");
					}
					else {
						out.append("            " + mDblAction + "('" + cd.getPageToGo().trim() + ( cd.getPageToGo().contains("?") ? "&" : "?" ) + auxID + mControlParameters + mExtraNewParameters + valuesKeep + "'); \n");
					}
					out.append("          } \n");
					printElse = true;
				}
				count++;
			}

			if (printElse) {
				out.append("        else {");
				out.append("          " + mDblAction + "('" + mForm + "?acao=upd" + auxID + mControlParameters + mExtraNewParameters + valuesKeep + "'); \n");
				out.append("        }");
			}
			else {
				out.append("        " + mDblAction + "('" + mForm + "?acao=upd" + auxID + mControlParameters + mExtraNewParameters + valuesKeep + "'); \n");
			}
		}
		out.append("    } catch(e) {} \n");
		out.append("      }); \n");

		return out.toString();
	}

	public static String getAlias(String pColumnName) {
		return getAlias(pColumnName, true);
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

	public String printFullContent() {
		StringBuffer out = new StringBuffer();

		out.append("<!DOCTYPE html> \n");
		out.append("<!--[if IE 8]> <html lang='en' class='ie8 no-js'> <![endif]--> \n");
		out.append("<!--[if IE 9]> <html lang='en' class='ie9 no-js'> <![endif]--> \n");
		out.append("<!--[if !IE]><!--> \n");
		out.append("<html lang='en' class='no-js'> \n");
		out.append("<!--<![endif]--> \n");

		// BEGIN HEAD
		out.append("<head> \n");
		// out.append(" <meta charset='utf-8'/> \n");
		out.append("  <title>" + SystemConfig.getSystemName() + " | The Citizens Brasil</title> \n");
		out.append("  <meta http-equiv='X-UA-Compatible' content='IE=edge'> \n");
		out.append("  <meta http-equiv='Content-Type' content='text/html; pageEncoding=" + mUser.getUserCharset() + " charset=" + mUser.getUserCharset() + "'>\n");
		out.append("  <meta content='width=device-width, initial-scale=1.0' name='viewport'/> \n");
		out.append("  <meta content='' name='description'/> \n");
		out.append("  <meta content='' name='author'/> \n");

		out.append(mPageHeaderTags);

		// BEGIN GLOBAL MANDATORY STYLES
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/open-sans/css/open-sans.css' /> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/font-awesome/css/font-awesome.min.css' /> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap/css/bootstrap.min.css' /> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/uniform/css/uniform.default.css' /> \n");
		// END GLOBAL MANDATORY STYLES

		// BEGIN PAGE LEVEL STYLES
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css'/> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/select2/select2.css'/> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css'/> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/bootstrap-datetimepicker/css/datetimepicker.css'/> \n");
		if (!hideDataTableBootstrap)
			out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/plugins/data-tables/DT_bootstrap.css'/> \n");
		// END PAGE LEVEL STYLES

		// BEGIN THEME STYLES
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/css/components.css' /> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/global/css/plugins.css' /> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/layout.css' /> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/themes/" + ( IniManipulation.getProperty(IniManipulation.LAYOUT_THEME).equalsIgnoreCase("light") ? "isg-light.css" : "isg-default.css" ) + "' /> \n");
		out.append("  <link rel='stylesheet' type='text/css' href='metronic/admin/layout/css/custom.css' /> \n");
		// END THEME STYLES

		String valuesKeep = "";
		for (int x = 0; x < mValuesToKeep.size(); x++) {
			valuesKeep += "&" + mValuesToKeepLabels.get(x).toString() + "=" + mValuesToKeep.get(x).toString();
		}

		this.getFilterRequest();
		if (!mLimparFiltro.equals("S")) {
			this.buildFilterRequest();
		}

		out.append("  <link rel='shortcut icon' href='favicon.ico'/> \n");
		out.append("  <script src='funcoes.jsp'></script> \n");
		out.append("  <script src='metronic/global/plugins/jquery-1.11.0.min.js' type='text/javascript'></script> \n");
		out.append(this.printTableScript(mID, mAjaxURL + valuesKeep));
		out.append("</head> \n");
		// END HEAD

		// BEGIN BODY
		// DOC: Apply 'page-header-fixed-mobile' and 'page-footer-fixed-mobile' class to body element to force fixed header or footer in mobile devices
		// DOC: Apply 'page-sidebar-closed' class to the body and 'page-sidebar-menu-closed' class to the sidebar menu element to hide the sidebar by default
		// DOC: Apply 'page-sidebar-hide' class to the body to make the sidebar completely hidden on toggle
		// DOC: Apply 'page-sidebar-closed-hide-logo' class to the body element to make the logo hidden on sidebar toggle
		// DOC: Apply 'page-sidebar-hide' class to body element to completely hide the sidebar on sidebar toggle
		// DOC: Apply 'page-sidebar-fixed' class to have fixed sidebar
		// DOC: Apply 'page-footer-fixed' class to the body element to have fixed footer
		// DOC: Apply 'page-sidebar-reversed' class to put the sidebar on the right side
		// DOC: Apply 'page-full-width' class to the body element to have full width page without the sidebar menu
		out.append("<body class='page-full-width'> \n");
		// BEGIN CONTAINER
		out.append("  <div class='page-container'> \n");

		// BEGIN CONTENT
		out.append("    <div class='page-content-wrapper'> \n");
		out.append("      <div class='page-content'> \n");

		// BEGIN PAGE HEADER
		out.append(this.menuConfig());

		// SUBTITLE
		if (!mSubTitle.equals("")) {
			out.append("      <div class='row'>");
			out.append("        <div class='col-md-12'> \n");
			out.append("          <div class='alert alert-info' alert-dismissable style='padding:8px'>");
			out.append("            <button type='button' class='close' data-dismiss='alert' aria-hidden='true'></button>");
			out.append(mSubTitle);
			out.append("          </div>");
			out.append("        </div>");
			out.append("      </div>");
		}

		// END PAGE HEADER

		// BEGIN PAGE CONTENT
		out.append("        <div class='row'> \n");
		out.append("          <div class='col-md-12'> \n");
		out.append(this.printDataTable(mID));
		if (mShowLegenda) {
			out.append(this.buildListLegend());
		}
		out.append("          </div> \n");
		if (!mCurLabels.trim().equals("")) {
			out.append("          <center> \n");
			out.append("            <a href='javascript:;' onclick='limpaFiltro();'>" + mUser.getTermo("LIMPARFILTRO") + " | " + mCurLabels + "</a>");
			out.append("          </center> \n");
		}

		out.append("        </div> \n");
		out.append("        <div id='_div_isg_page_content_overlay' style='display: none;'></div>\n");
		// END PAGE CONTENT

		out.append("      </div> \n");
		out.append("    </div> \n");
		// END CONTENT

		out.append("  </div> \n");
		// END CONTAINER

		// BEGIN JAVASCRIPTS (Load javascripts at bottom, this will reduce page load time)
		// BEGIN CORE PLUGINS
		out.append("  <!--[if lt IE 9]> \n");
		out.append("  <script src='metronic/global/plugins/respond.min.js'></script> \n");
		out.append("  <script src='metronic/global/plugins/excanvas.min.js'></script>  \n");
		out.append("  <![endif]--> \n");
		out.append("  <script src='metronic/global/plugins/jquery-migrate-1.2.1.min.js' type='text/javascript'></script> \n");
		out.append("  <script src='metronic/global/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js' type='text/javascript'></script> \n");
		out.append("  <script src='metronic/global/plugins/bootstrap/js/bootstrap.js' type='text/javascript'></script> \n");
		out.append("  <script src='metronic/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js' type='text/javascript'></script> \n");
		out.append("  <script src='metronic/global/plugins/jquery.blockui.min.js' type='text/javascript'></script> \n");
		out.append("  <script src='metronic/global/plugins/jquery.cokie.min.js' type='text/javascript'></script> \n");
		out.append("  <script src='metronic/global/plugins/uniform/jquery.uniform.min.js' type='text/javascript'></script> \n");
		// END CORE PLUGINS

		out.append(mPageFooterTags);

		// BEGIN PAGE LEVEL PLUGINS
		out.append("  <script type='text/javascript' src='metronic/global/plugins/select2/select2.js'></script> \n");
		out.append("  <script type='text/javascript' src='metronic/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js'></script> \n");
		out.append("  <script type='text/javascript' src='metronic/global/plugins/data-tables/jquery.dataTables.js'></script> \n");
		out.append("  <script type='text/javascript' src='metronic/global/plugins/data-tables/tabletools/js/dataTables.tableTools.min.js'></script> \n");
		out.append("  <script type='text/javascript' src='metronic/global/plugins/data-tables/fnReloadAjax.js'></script> \n");
		out.append("  <script type='text/javascript' src='metronic/global/plugins/data-tables/fnSetFilteringDelay.js'></script> \n");
		out.append("  <script type='text/javascript' src='metronic/global/plugins/data-tables/DT_bootstrap.js'></script> \n");
		// out.append(" <script type='text/javascript' src='metronic/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker-twitter.min.js'></script> \n");
		// if (IniManipulation.getProperty("new_layout").trim().equals("")) {
		// out.append(" <script type='text/javascript' src='metronic/global/plugins/bootbox/bootbox.min.js'></script>");
		// }
		// END PAGE LEVEL PLUGINS

		// BEGIN JAVASCRIPTS
		out.append("  <script src='metronic/global/scripts/metronic.js' type='text/javascript'></script> \n");
		out.append("  <script src='metronic/admin/layout/scripts/layout.js' type='text/javascript'></script> \n");
		// out.append(" <script src='metronic/admin/pages/scripts/ui-general.js' type='text/javascript'></script> \n");

		out.append("  <script> \n");
		out.append("    function showLoading() {");
		out.append("      try { top.showChargerPage('fr' + window.name.replace('id', '')); } catch(e) {}");
		out.append("    }");
		out.append("    jQuery(document).ready(function() { \n");
		out.append("      Metronic.init(); \n");
		out.append("      Layout.init(); \n");
		out.append("      $('.make-switch').bootstrapSwitch();");
		out.append(this.printTableInit(mID));
		out.append("      try { top.closeChargerPage('fr' + window.name.replace('id', '')); } catch(e) {} \n");
		out.append("    }); \n");
		out.append("    $('form').submit(function() {");
		out.append("      $('body').find('*').off();");
		out.append("      for (var x in jQuery.cache) {");
		out.append("        delete jQuery.cache[x];");
		out.append("      }");
		out.append("    });");
		out.append("    function goURL(pURL) { \n");
		out.append(mBeforeGoURL);
		out.append(" \n");
		if (mGoURLScript.trim().equals("")) {
			out.append("      showLoading(); \n");
			out.append("      document._frm_" + mID + ".action = pURL; \n");
			out.append("      document._frm_" + mID + ".submit(); \n");
		}
		else {
			out.append(mGoURLScript);
		}
		out.append("    } \n");
		if (mLocalizar) {
			out.append("    function submitLocalizar() { \n");
			out.append("      showLoading(); \n");
			out.append("      document._frm_" + mID + ".filtro.value = ''; \n");
			out.append("      document._frm_" + mID + ".action = document.location.href; \n");
			out.append("      document._frm_" + mID + ".submit(); \n");
			out.append("    } \n");
			out.append("    function limpaFiltro() {" + "\n");
			out.append("      showLoading(); \n");
			if (!mURLLimparFiltro.trim().equals("")) {
				out.append("      self.document.location.href = '" + mURLLimparFiltro + "'; \n");
			}
			else {
				out.append("      document._frm_" + mID + ".target = '_self'; \n");
				out.append("      document._frm_" + mID + ".filtro.value = ''; \n");
				out.append("      document._frm_" + mID + ".limpar.value = 'S'; \n");
				out.append("      document._frm_" + mID + ".action = document.location.href; \n");
				out.append("      document._frm_" + mID + ".submit(); \n");
			}
			out.append("    } \n");
		}
		out.append(mHeaderScripts);
		out.append("    function submitDataList() { \n");
		out.append("      showLoading(); \n");
		out.append("      document._frm_" + mID + ".action = document.location.href; \n");
		out.append("      document._frm_" + mID + ".submit(); \n");
		out.append("    } \n");
		out.append("  showLoading();");
		out.append("  </script> \n");
		// END JAVASCRIPTS

		out.append("</body> \n");
		// END BODY
		out.append("</html> \n");

		return out.toString();
	}

	// ============================================
	// METODOS - NOVA FORMA DE CRIAR SQL
	// ============================================

	public String getAlias(Column c) {
		return getAlias(buildColumn(c), true);
	}

	public String removeAlias(Column c) {
		String alias = getAlias(c);
		return c.getName().replaceAll(" AS " + alias, "").trim();
	}

	private String buildOperation(Column c) {
		if (c.getOperation() == null) {

			if (c.getType().equals(DataType.DATE)) {
				return lcdb.dateToChar(c.getName());
			}
			else if (c.getType().equals(DataType.DATE_TIME) || c.getType().equals(DataType.TIME)) {
				return lcdb.dateTimeToChar(c.getName());
			}

			return c.getName();
		}

		if (c.getOperation().equals(Operation.COUNT_DISTINCT)) {
			return "COUNT(DISTINCT " + c.getName() + ")";
		}

		return c.getOperation() + "(" + c.getName() + ")";
	}

	private String buildAlias(Column c) {
		String alias = "";

		if (c.getOperation() != null) {
			alias = c.getOperation() + "_";
		}

		alias += c.getName().replaceAll("\\.", "_");

		return alias;
	}

	private String buildColumn(Column c) {
		if (c.getName().toUpperCase().contains(" AS ")) {
			return c.getName();
		}

		return this.buildOperation(c) + " AS " + this.buildAlias(c);
	}

	protected StringBuffer buildQueryBase() {

		StringBuffer query = new StringBuffer();
		StringBuffer columnsQuery = new StringBuffer();
		StringBuffer groupByQuery = new StringBuffer();

		boolean needGroupBy = false;

		if (mColumnCheckbox != null) {
			columnsQuery.append(this.buildColumn(mColumnCheckbox));
			if (mColumnCheckbox.getOperation() == null) {
				if (groupByQuery.length() > 0) {
					groupByQuery.append(", \n");
				}
				groupByQuery.append(removeAlias(mColumnCheckbox));
			}
			else {
				needGroupBy = true;
			}
		}

		for (ColumnDisplay cd : mColumnsDisplay) {
			if (columnsQuery.length() > 0) {
				columnsQuery.append(", \n");
			}
			columnsQuery.append(this.buildColumn(cd));
			if (cd.getOperation() == null) {
				if (groupByQuery.length() > 0) {
					groupByQuery.append(", \n");
				}
				groupByQuery.append(removeAlias(cd));
			}
			else {
				needGroupBy = true;
			}
		}

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
				if (columnsQuery.length() > 0) {
					columnsQuery.append(", \n");
				}
				columnsQuery.append(this.buildColumn(p));

				if (groupByQuery.length() > 0) {
					groupByQuery.append(", \n");
				}
				groupByQuery.append(removeAlias(p));
			}
		}

		query.append("SELECT ");
		if (mDistinct) {
			query.append("DISTINCT ");
		}
		query.append("\n");
		query.append(columnsQuery);
		query.append(" FROM ");
		query.append(mTable);
		query.append("\n");

		if (mBaseFilter != null && mBaseFilter.trim().length() > 0) {
			query.append(" WHERE ");
			query.append(mBaseFilter);
			query.append("\n");
		}

		if (needGroupBy) {
			query.append(" GROUP BY ");
			query.append(groupByQuery);
			query.append("\n");
		}

		if (mHavingClause != null && mHavingClause.trim().length() > 0) {
			query.append(" HAVING ");
			query.append(mHavingClause);
			query.append("\n");
		}

		return query;
	}

	private String buildOrdination(ColumnDisplay rc) {
		if (rc != null && !Database.verifyNull(rc.getName()).equals("")) {
			if (rc.getOrdination() != null) {
				return " ORDER BY " + removeAlias(rc) + " " + rc.getOrdination();
			}
			return " ORDER BY " + removeAlias(rc);
		}
		return "";
	}

	// CRIADO ESPECIFICAMENTE PARA CRIAR O ORDER BY DE PAGINACOES DO SQL SERVER
	private String buildOrdinationByAlias(ColumnDisplay rc) {
		if (rc != null && !Database.verifyNull(rc.getName()).equals("")) {
			for (ColumnDisplay cd : mColumnsDisplay) {
				if (removeAlias(cd).equals(removeAlias(rc))) {
					return " ORDER BY " + getAlias(cd) + " " + ( rc.getOrdination() != null ? rc.getOrdination() : "" );
				}
			}
		}
		return " ORDER BY OBJECT_NAME(0) " + ( rc.getOrdination() != null ? rc.getOrdination() : "" ) + " ";
	}

	public void buildQueryDisplay() {

		String filterConcat = mCurFilter;
		if (!filterConcat.equals("")) {
			mBaseFilter += ( mBaseFilter != null && !mBaseFilter.trim().equals("") ? " AND " : "" ) + filterConcat;
		}

		StringBuffer query = this.buildQueryBase();
		String queryOrderBy = this.buildOrdination(mColumnOrderBy);

		mSql = query.toString();
		mSQLWithoutOrder = query.toString();

		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			queryOrderBy = this.buildOrdinationByAlias(mColumnOrderBy);
		}
		else {
			query.append(queryOrderBy);
		}

		// QUICKSEARCH
		if (!mQuickSearch.equals("")) {
			String quickSearch = "";
			String quickArray[] = mQuickSearch.split(" ");

			for (int parts = 0; parts < quickArray.length; parts++) {
				if (!quickArray[parts].trim().equals("")) {
					quickSearch += ( quickSearch.equals("") ? " ( " : " ) AND ( " );

					int auxCount = 0;
					for (ColumnDisplay column : mColumnsDisplay) {
						quickSearch += auxCount == 0 ? "" : " OR ";
						auxCount++;

						switch (column.getType()) {
							case TEXT:
							case CLOB:
								quickSearch += " " + BuildSql.getRemoveAccent(getAlias(column), false) + " LIKE " + BuildSql.getRemoveAccent(lcdb.verifyInsertNull("%" + quickArray[parts].trim() + "%", DataType.TEXT), false);
								break;

							case INTEGER:
							case DATE:
							case DATE_TIME:
							case TIME:
								quickSearch += " " + BuildSql.getToChar(getAlias(column)) + " LIKE " + lcdb.verifyInsertNull("%" + quickArray[parts].trim() + "%", DataType.TEXT) + " ";
								break;

							case DECIMAL:
							default:
								quickSearch += " " + BuildSql.getToChar(getAlias(column)) + " LIKE " + lcdb.verifyInsertNull("%" + lcdb.verifyInsertNull(quickArray[parts].trim(), DataType.INTEGER) + "%", DataType.TEXT) + " ";
								break;
						}
					}
				}
			}

			quickSearch += " ) ";

			query.insert(0, "SELECT * FROM (");
			query.append(") ");
			query.append(BuildSql.getAsSubQuery("DATALIST_TB_1234"));
			query.append(" WHERE ");
			query.append(quickSearch);

			mSQLWithoutOrder = query.toString();
		}

		if (mItemCount > 0) {
			if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
				query.insert(0, "SELECT * \n FROM (SELECT DATALIST_TABLE_AUX.*, DATALIST_ROW_NUMBER = ROW_NUMBER() OVER (" + queryOrderBy + ") \n FROM (");
				query.append(") DATALIST_TABLE_AUX) DATALIST_TABLE_AUX_2 \n WHERE DATALIST_TABLE_AUX_2.DATALIST_ROW_NUMBER >= " + ( ( mItemCount * mCurPage ) + 1 ) + " AND DATALIST_TABLE_AUX_2.DATALIST_ROW_NUMBER <= " + ( ( mItemCount * mCurPage ) + mItemCount ));
			}
			else if (Database.isDBMS(Database.DBMS_ORACLE) && mItemCount > 0) {
				query.insert(0, "SELECT * \n FROM (SELECT DATALIST_TABLE_AUX.*, ROWNUM AS DATALIST_ROW_NUMBER \n FROM (");
				query.append(") DATALIST_TABLE_AUX \n WHERE ROWNUM <= " + ( ( mItemCount * mCurPage ) + mItemCount ) + ") DATALIST_TABLE_AUX_2 \n WHERE DATALIST_TABLE_AUX_2.DATALIST_ROW_NUMBER >= " + ( ( mItemCount * mCurPage ) + 1 ));
			}
			else if (Database.isDBMS(Database.DBMS_MYSQL) && mItemCount > 0) {
				query.append(" LIMIT ");
				query.append(mItemCount);
				query.append(" OFFSET ");
				query.append(( mCurPage ) * mItemCount);
			}
		}

		mFinalSQL = query.toString();
	}

	private void buildFilterConcat() {
		if (mMapLocalizar.size() > 0) {

			mCurFilter = "";
			mCurLabels = "";

			for (ColumnDisplay columnFilter : mColumnsDisplay) {
				if (!Database.verifyNull(mMapLocalizar.get(getAlias(columnFilter))).trim().equals("")) {
					mCurFilter += ( mCurFilter.length() > 0 ? " AND " : "" ) + mMapLocalizar.get(getAlias(columnFilter));
				}
				mCurLabels += Database.verifyNull(mMapDescricao.get(getAlias(columnFilter)));
			}

		}
	}

	private String buildAjaxDatalist() {

		this.requestParameters();

		long lRow;
		long lStop;
		int lInt;

		if (request != null) {
			for (int x = 0; x < request.getCookies().length; x++) {
				if (request.getCookies()[x].getName().equalsIgnoreCase("_" + mID + "_" + mForm)) {
					mLastSelItem = request.getCookies()[x].getValue().trim();
				}
			}
		}

		Database.closeObject(mConn);
		mConn = lcdb.openConnection();

		buildQueryDisplay();
		mRecordCount = lcdb.getRecordCount(mSQLWithoutOrder);

		long totalRecords = lcdb.getRecordCount(mSql);

		lHtml.append("{ \n");
		lHtml.append("  \"iTotalRecords\": " + totalRecords + ", \n");
		lHtml.append("  \"iTotalDisplayRecords\": " + mRecordCount + ", \n");
		lHtml.append("  \"aaData\": [ \n");

		try {
			rs = lcdb.openResultSet(mFinalSQL, mConn);

			lInt = 1;

			if (Database.isDBMS(Database.DBMS_SQLSERVER) && mItemCount > 0) {
				lRow = ( mItemCount * mCurPage );
				lStop = lRow + mItemCount;
			}
			else if (mItemCount > 0) {
				lRow = 1;
				lStop = mItemCount;
			}
			else {
				lRow = 1;
				lStop = mRecordCount;
			}

			if (mRecordCount > 0) {
				String auxID = "";

				int countColumns = 0;

				while (( lRow <= lStop ) && ( rs.next() )) {

					auxID = "";

					for (Column column : mPrimaries) {
						auxID += ( auxID.equals("") ? "" : "|$|" ) + Database.verifyNull(rs.getObject(getAlias(column)));
					}

					mDblAction = "goURL";

					lHtml.append("    { \n");
					lHtml.append("      \"DT_RowId\": \"_row_" + mID + "_" + auxID + "\", \n");

					if (mLastSelItem.equals("_row_" + mID + "_" + auxID)) {
						lHtml.append("      \"DT_RowClass\": \"active\", \n");
					}

					if (mCheckBox || mRadioButton) {
						lHtml.append("      \"" + countColumns + "\": \"");

						String lDisabled = "";
						if (Database.verifyNull(mCheckboxContition[0]) != "" && mCheckboxContition[1].equalsIgnoreCase(Database.verifyNull(rs.getObject(mCheckboxContition[0])))) {
							lDisabled = "disabled";
						}
						if (mCheckBox) {
							lHtml.append("<input " + lDisabled + " type='checkbox' name='_cbx_" + mID + "' id='_cbx_" + mID + "_" + auxID + "' value='" + auxID + "'>");
							countColumns++;
						}
						else if (mRadioButton) {
							lHtml.append("<input " + lDisabled + " ");

							if (request != null && Database.verifyNull(request.getParameter(primary[1])).equals(Database.verifyNull(rs.getObject(primary[1])))) {
								lHtml.append(" checked ");
							}

							lHtml.append(" type='radio' name='_rd_" + mID + "' id='_rd_" + mID + "_" + auxID + "' value='" + auxID + "'>");

							countColumns++;
						}

						lHtml.append("\", \n");
					}

					int index = 0;
					for (ColumnDisplay column : mColumnsDisplay) { // LISTAGEM DE COLUNAS
						String value = "";

						if (column.getIcon() != null && !column.getIcon().trim().equals("")) {
							value = column.getIcon();
						}
						else if (column.getType() == DataType.DECIMAL) {
							value = lcdb.decimalFormat(Database.verifyNull(rs.getObject(getAlias(column))), 2);
						}
						else {
							value = Database.verifyNull(rs.getObject(getAlias(column)));
							if (column.getType() == DataType.TIME) {
								value = value.split(" ").length > 1 ? value.split(" ")[1] : value;
							}
						}
						if (!mQuickSearch.equals("")) {
							String lControl = Utils.removeAccent(value.toLowerCase());
							String lVetQuick[] = mQuickSearch.toLowerCase().split(" ");
							String lPartQuick = "";
							int lPos = 0;
							for (int lPart = 0; lPart < lVetQuick.length; lPart++) {
								if (!lVetQuick[lPart].trim().equals("")) {
									lPartQuick = Utils.removeAccent(lVetQuick[lPart].trim());
									lPos = lControl.indexOf(lPartQuick);
									while (lPos >= 0) {
										value = value.substring(0, lPos) + "<b>" + value.substring(lPos, lPos + lPartQuick.length()) + "</b>" + value.substring(lPos + lPartQuick.length());
										lControl = value.toLowerCase();
										lPos = lControl.indexOf(lPartQuick, lPos + lPartQuick.length() + 7);
									}
								}
							}
						}
						index++;

						value = Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(value, "\\", "\\\\"), "\"", "\\\""), "\n", " "), "\r", " ").trim();

						if (column.getFlagsLength() > 0) {
							String auxStr = Database.verifyNull(rs.getObject(getAlias(column)));
							lHtml.append("      \"" + countColumns + "\" : \"<span class='label bg-" + Utils.replaceAll(Database.verifyNull(column.getFlag(auxStr), "none"), "\"", "\\\"").trim() + " tooltips' data-placement='top' data-original-title='" + Utils.replaceAll(auxStr, "\"", "\\\"").trim() + "'> " + ( column.isLegend() ? "&nbsp;&nbsp;&nbsp;" : value ) + " </span>\"" + ( index < mColumnsDisplay.size() ? "," : "" ) + " \n");
						}
						else {
							lHtml.append("      \"" + countColumns + "\" : \"" + value + "\"" + ( index < mColumnsDisplay.size() ? "," : "" ) + " \n");
						}

						countColumns++;
					}
					countColumns = 0;

					lHtml.append("    }, \n");
					lRow = lRow + 1;
					lInt = lInt + 1;
				}
			}
			lHtml.deleteCharAt(lHtml.length() - 3);
			lHtml.append("  ] \n");
			lHtml.append("} \n");

			if (mRecordCount <= 0) {
				lHtml = new StringBuffer();
				lHtml.append("{\"iTotalRecords\": 0, \"iTotalDisplayRecords\": 0, \"aaData\": []}");
			}

			if (rs != null) {
				rs.close();
			}
		}
		catch (Exception e) {
			lHtml = new StringBuffer();
			lHtml.append("{\"iTotalRecords\": 0, \"iTotalDisplayRecords\": 0, \"aaData\": []}");

			Utils.printSystemError("ajaxFillDataList", e.getMessage());
		}
		finally {
			Database.closeObject(mConn);
		}

		return lHtml.toString();
	}

	public String returnAjaxDataList() {
		return this.buildAjaxDatalist();
	}

	public String buildDataFilter() {
		StringBuffer lHtml = new StringBuffer();

		lHtml.append("<div id='_search_modal_" + mID + "' class='modal fade modal-scroll' tabindex='-1' data-replace='true'> \n");
		lHtml.append("  <div class='modal-dialog'> \n");
		lHtml.append("    <div class='modal-content'> \n");
		lHtml.append("      <div class='modal-header'> \n");
		lHtml.append("        <button type='button' class='close' data-dismiss='modal' aria-hidden='true'></button> \n");
		lHtml.append("        <h4 class='modal-title'>" + mTitle + "</h4> \n");
		lHtml.append("      </div> \n");
		lHtml.append("      <div class='modal-body form'> \n");
		lHtml.append("        <div class='form-horizontal form-row-seperated'> \n");

		if (mTable != null && !mTable.trim().equals("")) {
			int auxCount = 0;

			String[] params = null;
			List<String> listaCheck = new ArrayList<String>();

			for (ColumnDisplay column : mColumnsDisplay) {
				auxCount++;
				lHtml.append("        <div class='form-group" + ( auxCount == mColumnsDisplay.size() ? " last" : "" ) + "'> \n");
				lHtml.append("          <label class='control-label col-sm-4'>" + column.getLabel() + "</label> \n");
				lHtml.append("          <div class='col-sm-8'> \n");

				if (column.getFlagsLength() > 0) {

					if (!mLimparFiltro.equals("S")) {
						params = request.getParameterValues("_field_search_" + getAlias(column));
						if (params != null) {
							listaCheck = Arrays.asList(params);
						}
					}

					int count = 0;
					for (String key : column.getFlagList().keySet()) {
						lHtml.append("            <div class='margin-bottom-10 col-sm-12 vcenter'> \n");
						lHtml.append("              <label class='control-label col-sm-4' for='_field_search_" + getAlias(column) + "_" + count + "'>" + key + "</label>&nbsp;");
						lHtml.append("              <input type='checkbox' value='" + key + "' id='_field_search_" + getAlias(column) + "_" + count + "' name='_field_search_" + getAlias(column) + "' class='make-switch switch-small' data-on-text=\"" + SystemIcons.ICON_CHECK + "\" data-off-text=\"" + SystemIcons.ICON_X + "\" data-on-color='success' data-off-color='danger' " + ( listaCheck.contains(key) ? "checked" : "" ) + "> \n");
						lHtml.append("            </div> \n");
						count++;
					}
				}
				else {

					DatePicker date = new DatePicker(mUser);
					date.setID("_field_search_" + getAlias(column));
					date.setName("_field_search_" + getAlias(column));
					date.setValue(mLimparFiltro.equals("S") ? "" : Database.verifyNull(request.getParameter("_field_search_" + getAlias(column))));

					switch (column.getType()) {
						case DATE:
							lHtml.append(date.printDatePicker(DateType.DATE));
							break;
						case TIME:
							lHtml.append(date.printDatePicker(DateType.TIME));
							break;
						case DATE_TIME:
							lHtml.append(date.printDatePicker(DateType.DATE_TIME));
							break;
						case DECIMAL:
							lHtml.append("            <div class='input-group' style='width:100%'> \n");
							lHtml.append("              <input type='text' id='_field_search_" + getAlias(column) + "' name='_field_search_" + getAlias(column) + "' value='" + ( mLimparFiltro.equals("S") ? "" : Database.verifyNull(request.getParameter("_field_search_" + getAlias(column))) ) + "' class='form-control' onkeyup='this.hasFocus = true; FormataValor(this, event, 2, 12);' onblur='this.hasFocus = false; FormataValor(this, event, 2, 12);' oncontextmenu='return false' />");
							lHtml.append("            </div> \n");
							break;
						case INTEGER:
							lHtml.append("            <div class='input-group' style='width:100%'> \n");
							lHtml.append("              <input type='text' id='_field_search_" + getAlias(column) + "' name='_field_search_" + getAlias(column) + "' value='" + ( mLimparFiltro.equals("S") ? "" : Database.verifyNull(request.getParameter("_field_search_" + getAlias(column))) ) + "' class='form-control' onkeyup='this.hasFocus = true; FormataValor(this, event, 0, 12);' onblur='this.hasFocus = false; FormataValor(this, event, 0, 12);' oncontextmenu='return false' />");
							lHtml.append("            </div> \n");
							break;
						default:
							lHtml.append("            <div class='input-group' style='width:100%'> \n");
							lHtml.append("              <input type='text' id='_field_search_" + getAlias(column) + "' name='_field_search_" + getAlias(column) + "' value='" + ( mLimparFiltro.equals("S") ? "" : Database.verifyNull(request.getParameter("_field_search_" + getAlias(column))) ) + "' class='form-control'/>");
							lHtml.append("            </div> \n");
							break;
					}
				}

				lHtml.append("          </div> \n");
				lHtml.append("        </div> \n");
			}
		}

		lHtml.append("         </div> \n");
		lHtml.append("       </div> \n");
		lHtml.append("       <div class='modal-footer'> \n");
		lHtml.append("         <button type='button' class='btn btn-default' data-dismiss='modal'> " + mUser.getTermo("CANCELAR") + " </button> \n");
		lHtml.append("         <button type='button' class='btn btn-primary' onclick='submitLocalizar();'>" + SystemIcons.ICON_SEARCH + " " + mUser.getTermo("LOCALIZAR") + " </button> \n");
		lHtml.append("       </div> \n");
		lHtml.append("     </div> \n");
		lHtml.append("  </div> \n");
		lHtml.append("</div> \n");

		return lHtml.toString();
	}

	public void buildFilterRequest() {
		if (request != null) {
			String param = "";

			String[] params = null;
			List<String> listaCheck = new ArrayList<String>();

			for (ColumnDisplay column : mColumnsDisplay) {
				param = Database.verifyNull(request.getParameter("_field_search_" + getAlias(column)));

				if (column.getFlagsLength() > 0) {
					param = "";
					params = request.getParameterValues("_field_search_" + getAlias(column));

					if (params != null) {
						listaCheck = Arrays.asList(params);

						for (String valores : listaCheck) {
							param += ( param.equals("") ? "" : ", " ) + lcdb.verifyInsertNull(valores, DataType.TEXT);
						}

						if (!param.trim().equals("")) {
							mMapLocalizar.put(getAlias(column), removeAlias(column) + " IN (" + param + ")");
							mMapDescricao.put(getAlias(column), "<b>" + column.getLabel() + ":</b> (" + param + ");  ");
						}
					}
				}
				else if (!param.trim().equals("")) {
					// mPostCurFilter += "&_field_search_" + getAlias(column) + "=" + param;
					switch (column.getType()) {
						case DATE:
							mMapLocalizar.put(getAlias(column), lcdb.dateToChar(removeAlias(column)) + " = " + lcdb.verifyInsertNull(param, column.getType()));
							mMapDescricao.put(getAlias(column), "<b>" + column.getLabel() + ":</b> " + param + ";  ");
							break;
						case DATE_TIME:
							mMapLocalizar.put(getAlias(column), lcdb.dateTimeToChar(removeAlias(column)) + " = " + lcdb.verifyInsertNull(param, column.getType()));
							mMapDescricao.put(getAlias(column), "<b>" + column.getLabel() + ":</b> " + param + ";  ");
							break;
						case TIME:
							mMapLocalizar.put(getAlias(column), BuildSql.getHH24MItoChar(removeAlias(column), true) + " = " + lcdb.verifyInsertNull(param, DataType.TEXT));
							mMapDescricao.put(getAlias(column), "<b>" + column.getLabel() + ":</b> " + param + ";  ");
							break;
						case DECIMAL:
						case INTEGER:
							mMapLocalizar.put(getAlias(column), removeAlias(column) + " = " + lcdb.verifyInsertNull(param, column.getType()));
							mMapDescricao.put(getAlias(column), "<b>" + column.getLabel() + ":</b> " + param + ";  ");
							break;
						default:
							mMapLocalizar.put(getAlias(column), BuildSql.getToChar(removeAlias(column)) + " LIKE " + lcdb.verifyInsertNull("%" + param + "%", column.getType()));
							mMapDescricao.put(getAlias(column), "<b>" + column.getLabel() + ":</b> '" + param + "';  ");
							break;
					}
				}
			}
		}

		this.buildFilterConcat();
	}

	private void getFilterRequest() {
		if (request != null) {
			mLimparFiltro = Database.verifyNull(request.getParameter("limpar"));
			String filtro = Database.verifyNull(request.getParameter("filtro"));
			try {
				if (!filtro.trim().equals("") && !filtro.trim().equals("|")) {
					mCurLabels = filtro.split(" \\| ")[0];
					mCurFilter = filtro.split(" \\| ")[1];
				}
			}
			catch (Exception e) {
			}
		}
	}

	public String buildListLegend() {
		String legendas = "";

		for (ColumnDisplay column : mColumnsDisplay) {
			if (column.getFlagsLength() > 0) {
				for (String key : column.getFlagList().keySet()) {
					if (legendas.equals("")) {
						legendas += "<div class='clear'> \n";
						legendas += "<table class='table table-bordered' style='width:100%;'> \n";
						legendas += "  <tr>";
						legendas += "    <td style='text-align:center;'><strong>" + mUser.getTermo("LEGENDA") + "</strong></td> \n";
						legendas += "    <td style='width:100%;'> \n";
					}
					legendas += "      <span><span class='label bg-" + column.getFlag(key) + "'>&nbsp;&nbsp;&nbsp;</span> " + key + " </span>&nbsp;&nbsp;&nbsp; \n";
				}
			}
		}

		if (!legendas.equals("")) {
			legendas += "    </td> \n";
			legendas += "  </tr> \n";
			legendas += "</table> \n";
			legendas += "</div> \n";
		}

		return legendas;
	}

	public static String fullTable(User pUser, String pTableID) {
		StringBuffer lHtml = new StringBuffer();

		lHtml.append("<script>\n");
		lHtml.append("  jQuery(document).ready( function() { \n");
		lHtml.append("    $.extend(true, $.fn.DataTable.TableTools.classes, { \n");
		lHtml.append("      'container': 'btn-group tabletools-btn-group pull-right hidden-xs hidden-sm', \n");
		lHtml.append("      'buttons': { \n");
		lHtml.append("        'normal': 'btn btn-sm blue-steel', \n");
		lHtml.append("        'disabled': 'btn btn-sm blue-steel disabled' \n");
		lHtml.append("      } \n");
		lHtml.append("    }); \n");
		lHtml.append("    $('#" + pTableID + "').dataTable({ \n");
		lHtml.append("      'sDom': \"<'row'<'col-md-6 col-sm-12'fl><'col-md-6 col-sm-12'T>r><'table-scrollable't><'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>\", \n"); // HORIZONTAL SCROLLABLE DATATABLE
		lHtml.append("      'aLengthMenu': [ \n");
		lHtml.append("        [25, 50, 75, 100, -1], \n");
		lHtml.append("        [25, 50, 75, 100, '" + pUser.getTermo("TODOS") + "'] \n");
		lHtml.append("      ], \n");
		lHtml.append("      'iDisplayLength': 50, \n");
		lHtml.append("      'sPaginationType': 'bootstrap_full_number', \n");
		lHtml.append("      'fnDrawCallback': function( oSettings ) {");
		lHtml.append("        jQuery('.page-content').attr('style', '');");
		lHtml.append("        try { top.closeChargerPage('fr' + window.name.replace('id', '')); } catch(e) {} \n");
		lHtml.append("        jQuery('.tooltips').tooltip();");
		lHtml.append("      }, ");
		lHtml.append("      'oTableTools': { \n");
		lHtml.append("        'sSwfPath': 'metronic/global/plugins/data-tables/tabletools/swf/copy_csv_xls_pdf.swf', \n");
		lHtml.append("        'aButtons': [{ \n");
		lHtml.append("            'sExtends': 'pdf', \n");
		lHtml.append("            'sButtonText': '" + pUser.getTermo("PDF") + "' \n");
		lHtml.append("          }, { \n");
		lHtml.append("            'sExtends': 'xls', \n");
		lHtml.append("            'sButtonText': '" + pUser.getTermo("EXCEL") + "' \n");
		lHtml.append("          }, { \n");
		lHtml.append("            'sExtends': 'print', \n");
		lHtml.append("            'sButtonText': '" + pUser.getTermo("IMPRIMIR") + "', \n");
		lHtml.append("            'sInfo': \"<h2><span class='label bg-" + DataList.FAROL_BLUE_STEEL + "'><b>" + Utils.replaceAll(pUser.getTermo("DATATABLEPRINT"), "\"", "&quot;") + "</b></span></h2>\" \n");
		lHtml.append("          } \n");
		lHtml.append("        ] \n");
		lHtml.append("      }, \n");
		lHtml.append("      'oLanguage': { \n");
		lHtml.append("        'sEmptyTable': '" + pUser.getTermo("MSGNENHUMREGENC") + "', \n");
		lHtml.append("        'sInfo': '" + pUser.getTermo("DATATABLEINFO") + "', \n");
		lHtml.append("        'sInfoEmpty': '" + pUser.getTermo("MSGNENHUMREGENC") + "', \n");
		lHtml.append("        'sInfoFiltered': ' (" + pUser.getTermo("DATATABLEFILTER") + ") ', \n");
		lHtml.append("        'sInfoThousands': '" + pUser.getUserNumberMil() + "', \n");
		lHtml.append("        'sLengthMenu': '_MENU_ " + pUser.getTermo("REGISTROS") + "', \n");
		lHtml.append("        'sLoadingRecords': '&nbsp;<img src=\"metronic/global/img/loading-spinner-grey.gif\"/>&nbsp;" + pUser.getTermo("AGUARDE") + "...', \n");
		lHtml.append("        'sProcessing': '&nbsp;<img src=\"metronic/global/img/loading-spinner-grey.gif\"/>&nbsp;" + pUser.getTermo("AGUARDE") + "...', \n");
		lHtml.append("        'sSearch': '" + pUser.getTermo("BUSCAR") + "', \n");
		lHtml.append("        'sZeroRecords': '" + pUser.getTermo("MSGNENHUMREGENC") + "', \n");
		lHtml.append("        'oPaginate': { \n");
		lHtml.append("          'sFirst': '" + pUser.getTermo("PAGEFIRST") + "', \n");
		lHtml.append("          'sPrevious': '" + pUser.getTermo("PAGEPREVIOUS") + "', \n");
		lHtml.append("          'sNext': '" + pUser.getTermo("PAGENEXT") + "', \n");
		lHtml.append("          'sLast': '" + pUser.getTermo("PAGELAST") + "' \n");
		lHtml.append("        }");
		lHtml.append("      } \n");
		lHtml.append("    }); \n");
		lHtml.append("    var tableWrapper = $('#" + pTableID + "_wrapper'); \n"); // DATATABLE CREATES THE TABBLE WRAPPER BY ADDING WITH ID {table_id}_wrapper
		lHtml.append("    jQuery('.dataTables_filter', tableWrapper).addClass('pull-left _" + pTableID + "_padding_right'); \n"); // MODIFY TABLE SEARCH INPUT
		lHtml.append("    jQuery('.dataTables_length', tableWrapper).addClass('pull-left _" + pTableID + "_padding_right'); \n"); // MODIFY TABLE LENGTH INPUT
		lHtml.append("    jQuery('.dataTables_filter input', tableWrapper).addClass('form-control input-small input-inline'); \n"); // MODIFY TABLE SEARCH INPUT
		lHtml.append("    jQuery('.dataTables_length select', tableWrapper).addClass('form-control input-small'); \n"); // MODIFY TABLE PER PAGE DROPDOWN
		lHtml.append("    jQuery('.dataTables_length select', tableWrapper).select2(); \n"); // INITIALIZE SELECT2 DROPDOWN
		lHtml.append("  }); \n");
		lHtml.append("</script>");

		return lHtml.toString();
	}

}
