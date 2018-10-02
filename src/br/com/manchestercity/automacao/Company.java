
package br.com.manchestercity.automacao;

public class Company {
	String mCompanyCode;
	String mCompanyName;
	String mDomain;
	boolean mHasAccess;

	public String getCompanyName() {
		return mCompanyName;
	}

	public String getDomain() {
		return mDomain;
	}

	public void setCompanyCode(String pCompanyCode) {
		this.mCompanyCode = pCompanyCode;
	}

	public void setCompanyName(String pCompanyName) {
		this.mCompanyName = pCompanyName;
	}

	public void setDomain(String pDomain) {
		this.mDomain = pDomain;
	}

	public String getCompanyCode() {
		return mCompanyCode;
	}

	public void setHasAccess(boolean pHasAccess) {
		this.mHasAccess = pHasAccess;
	}

	public boolean getHasAccess() {
		return mHasAccess;
	}

	public Company() {
		mCompanyCode = "";
		mCompanyName = "";
		mDomain = "";
		mHasAccess = false;
	}

}
