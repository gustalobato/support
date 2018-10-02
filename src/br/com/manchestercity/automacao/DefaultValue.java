
package br.com.manchestercity.automacao;

public class DefaultValue {
	String mKey;
	String mValue;
	boolean mTermo;

	public DefaultValue() {
		mKey = "";
		mValue = "";
		mTermo = true;
	}

	public String getValue() {
		return mValue;
	}

	public void setKey(String pKey) {
		this.mKey = pKey;
	}

	public void setValue(String pValue) {
		this.mValue = pValue;
	}

	public String getKey() {
		return mKey;
	}

	public boolean isTermo() {
		return mTermo;
	}

	public void setTermo(boolean pValue) {
		this.mTermo = pValue;
	}

}
