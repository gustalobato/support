
package br.com.manchestercity.automacao;

import java.util.Vector;

public class DataColumn {

	protected String mField;
	protected String mAlias;
	protected int mType;
	protected int mAlign;
	protected String mGroup;
	protected String mGroupInfo;
	protected String mWidth;
	protected String mValue;
	protected boolean mHidden;
	protected boolean mPrimary;
	protected String mRealName;
	protected int mTypeOrder;
	protected boolean mHiddenSearch;
	protected Vector<DataColumn.DomainItem> mDomain;
	protected boolean mIsInterval;
	protected boolean mIsOrdenable;
	protected String mQueryList;
	protected String mStringReplace;
	protected String mSeparator;
	protected String mFieldList;
	protected boolean mUseInQuickSearch;
	protected boolean mShowFilter;
	private boolean mShowExcel;

	protected boolean mShowDisplay;

	public DataColumn() {
		mField = "";
		mAlias = "";
		mType = 0;
		mAlign = 0;
		mGroup = "";
		mGroupInfo = "";
		mWidth = "";
		mValue = "";
		mHidden = false;
		mRealName = "";
		mTypeOrder = mType;
		mDomain = new Vector<DataColumn.DomainItem>();
		mHiddenSearch = false;
		mIsInterval = false;
		mIsOrdenable = true;
		mQueryList = "";
		mSeparator = "";
		mStringReplace = "";
		mFieldList = "";
		mUseInQuickSearch = true;
		mShowExcel = true;

		mShowDisplay = true;
	}

	public boolean getShowDisplay() {
		return mShowDisplay;
	}

	public void setShowDisplay(boolean pValue) {
		mShowDisplay = pValue;
	}

	public boolean getShowFilter() {
		return mShowFilter;
	}

	public void setShowFilter(boolean pValue) {
		mShowFilter = pValue;
	}

	public boolean getUseInQuickSearch() {
		return mUseInQuickSearch;
	}

	public void setUseInQuickSearch(boolean pValue) {
		mUseInQuickSearch = pValue;
	}

	public String getFieldList() {
		return mFieldList;
	}

	public void setFieldList(String pValue) {
		mFieldList = pValue;
	}

	public String getStringReplace() {
		return mStringReplace;
	}

	public void setStringReplace(String pValue) {
		mStringReplace = pValue;
	}

	public String getSeparator() {
		return mSeparator;
	}

	public void setSeparator(String pValue) {
		mSeparator = pValue;
	}

	public String getQueryList() {
		return mQueryList;
	}

	public void setQueryList(String pValue) {
		mQueryList = pValue;
	}

	public boolean getIsInterval() {
		return mIsInterval;
	}

	public void setIsInterval(boolean pValue) {
		mIsInterval = pValue;
	}

	public boolean getIsOrdenable() {
		return mIsOrdenable;
	}

	public void setIsOrdenable(boolean pValue) {
		mIsOrdenable = pValue;
	}

	public int getTypeOrder() {
		return mTypeOrder;
	}

	public boolean getHiddenSearch() {
		return mHiddenSearch;
	}

	public String getRealName() {
		return mRealName;
	}

	public void setRealName(String pRealName) {
		mRealName = pRealName;
	}

	public String getValue() {
		return mValue;
	}

	public boolean getPrimary() {
		return mPrimary;
	}

	public String getField() {
		return mField;
	}

	public String getAlias() {
		return mAlias;
	}

	public int getType() {
		return mType;
	}

	public int getAlign() {
		return mAlign;
	}

	public boolean getHidden() {
		return mHidden;
	}

	public String getGroup() {
		return mGroup;
	}

	public String getGroupInfo() {
		return mGroupInfo;
	}

	public String getWidth() {
		return mWidth;
	}

	public boolean getShowExcel() {
		return mShowExcel;
	}

	public void setTypeOrder(int pValue) {
		mTypeOrder = pValue;
	}

	public void setValue(String pValue) {
		mValue = pValue;
	}

	public void setHidden(boolean pValue) {
		mHidden = pValue;
	}

	public void setHiddenSearch(boolean pValue) {
		mHiddenSearch = pValue;
	}

	public void setPrimary(boolean pValue) {
		mPrimary = pValue;
	}

	public void setField(String pValue) {
		mField = pValue;
	}

	public void setAlias(String pValue) {
		mAlias = pValue;
	}

	public void setType(int pValue) {
		mType = pValue;
	}

	public void setAlign(int pValue) {
		mAlign = pValue;
	}

	public void setGroup(String pValue) {
		mGroup = pValue;
	}

	public void setGroupInfo(String pValue) {
		mGroupInfo = pValue;
	}

	public void setWidth(long pValue) {
		mWidth = String.valueOf(pValue);
	}

	public void setWidth(String pValue) {
		mWidth = pValue;
	}

	public void setShowExcel(boolean pValue) {
		mShowExcel = pValue;
	}

	public void addDomain(String pId, String pValue) {
		DomainItem lItem = new DomainItem();
		lItem.setId(pId);
		lItem.setValue(pValue);
		mDomain.add(lItem);
	}

	public Vector<DataColumn.DomainItem> getDomain() {
		return mDomain;
	}

	public String getDomainSplited(String pSplit) {
		String lRet = "";
		DomainItem lItem;
		for (int lInt = 0; lInt < mDomain.size(); lInt++) {
			lItem = (DomainItem) mDomain.get(lInt);
			lRet += ( ( lRet.equals("") ) ? "" : pSplit ) + lItem.getId() + "|" + lItem.getValue();
		}

		return lRet;
	}

	public String getDomainItem(String pId) {
		int lInt;
		String lReturn = "";
		DomainItem lItem;
		for (lInt = 0; lInt < mDomain.size(); lInt++) {
			lItem = (DomainItem) mDomain.get(lInt);
			if (lItem.getId().equalsIgnoreCase(pId)) {
				lReturn = lItem.getValue();
				break;
			}
		}

		return lReturn;
	}

	class DomainItem {
		protected String mId;
		protected String mValue;

		public DomainItem() {
			mId = "";
			mValue = "";
		}

		public void setId(String pValue) {
			mId = pValue;
		}

		public String getId() {
			return mId;
		}

		public void setValue(String pValue) {
			mValue = pValue;
		}

		public String getValue() {
			return mValue;
		}

	}

}
