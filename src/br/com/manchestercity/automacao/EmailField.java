
package br.com.manchestercity.automacao;

import java.util.ArrayList;
import java.util.Arrays;

public class EmailField {

	private String fieldSQL;
	private String fieldEmail;
	private String labelCombo;
	private ArrayList<DefaultValue> domain;

	public ArrayList<DefaultValue> getDomain() {
		return domain;
	}

	public String getLabelCombo() {
		return labelCombo;
	}

	public String getFieldEmail() {
		return fieldEmail;
	}

	public void setFieldSQL(String fieldSQL) {
		this.fieldSQL = fieldSQL;
	}

	public void setDomain(ArrayList<DefaultValue> domain) {
		this.domain = domain;
	}

	public void setLabelCombo(String labelCombo) {
		this.labelCombo = labelCombo;
	}

	public void setFieldEmail(String fieldEmail) {
		this.fieldEmail = fieldEmail;
	}

	public String getFieldSQL() {
		return fieldSQL;
	}

	public EmailField addDomain(String pId, String pValue) {
		return addDomain(pId, pValue, true);
	}

	public EmailField addDomain(String pId, String pValue, boolean pTermo) {
		DefaultValue lItem = new DefaultValue();
		lItem.setKey(pId);
		lItem.setValue(pValue);
		lItem.setTermo(pTermo);

		domain.add(lItem);

		return this;
	}

	public EmailField() {
		this.fieldSQL = "";
		this.fieldEmail = "";
		this.labelCombo = "";
		this.domain = new ArrayList<DefaultValue>();
	}

	public EmailField(String fieldEmail, String labelCombo, String fieldSQL) {
		this.fieldSQL = fieldSQL;
		this.fieldEmail = fieldEmail;
		this.labelCombo = labelCombo;
		this.domain = new ArrayList<DefaultValue>();
	}

	public EmailField(String fieldEmail, String labelCombo, String fieldSQL, DefaultValue[] domain) {
		this.fieldSQL = fieldSQL;
		this.fieldEmail = fieldEmail;
		this.labelCombo = labelCombo;
		this.domain = (ArrayList<DefaultValue>) Arrays.asList(domain);
	}

	public String getDomainSplited(String pSplit) {
		String lRet = "";
		DefaultValue lItem;
		for (int lInt = 0; lInt < domain.size(); lInt++) {
			lItem = (DefaultValue) domain.get(lInt);
			lRet += ( ( lRet.equals("") ) ? "" : pSplit ) + lItem.getKey() + "|" + lItem.getValue();
		}
		return lRet;
	}

	public String getDomainItem(User pUser, String pId) {
		return getDomainItem(pUser, pId, false);
	}

	public String getDomainItem(User pUser, String pId, boolean pVerifyOnly) {
		int lInt;
		boolean lFound = false;
		String lReturn = "";
		DefaultValue lItem;
		for (lInt = 0; lInt < domain.size(); lInt++) {
			lItem = (DefaultValue) domain.get(lInt);
			if (lItem.getKey().equalsIgnoreCase(pId)) {
				lReturn = lItem.isTermo() && pUser != null ? pUser.getTermo(lItem.getValue()) : lItem.getValue();
				lFound = true;
				break;
			}
		}

		if (!lFound && pVerifyOnly) {
			lReturn = pId;
		}
		return lReturn;
	}

}
