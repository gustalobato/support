
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class ValidateReturn {

	private String message;
	private ArrayList<String> mandatoryIDs;

	public ValidateReturn() {
		message = "";
		mandatoryIDs = new ArrayList<String>();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<String> getMandatoryIDs() {
		return mandatoryIDs;
	}

	public void setMandatoryIDs(ArrayList<String> mandatoryIDs) {
		this.mandatoryIDs = mandatoryIDs;
	}

	public void addMandatoryId(String htmlTagID) {
		this.mandatoryIDs.add(htmlTagID);
	}

}
