
package br.com.manchestercity.automacao;

public class UserException {
	private String mNumber;
	private String mDescription;

	public String getNumber() {
		return mNumber;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public void setNumber(String mNumber) {
		this.mNumber = mNumber;
	}

	public String getDescription() {
		return mDescription;
	}

	public UserException() {
		mNumber = "";
		mDescription = "";
	}

}
