
package br.com.manchestercity.automacao;

public class DataControl {

	protected String mControl;
	protected String mField;
	protected String mValue;
	protected DataType mType;

	public DataControl() {
		mControl = null;
		mField = "";
		mValue = "";
	}

	public DataControl(String control, String field, String value, DataType type) {
		mControl = control;
		mField = field;
		mValue = value;
		mType = type;
	}

	public DataType getType() {
		return mType;
	}

	public void setType(DataType mType) {
		this.mType = mType;
	}

	public String getControl() {
		return mControl;
	}

	public void setControl(String pValue) {
		mControl = pValue;
	}

	public String getField() {
		return mField;
	}

	public void setField(String pValue) {
		mField = pValue;
	}

	public String getValue() {
		return mValue;
	}

	public void setValue(String pValue) {
		mValue = pValue;
	}
}
