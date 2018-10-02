
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class EmailType {

	private String chave;
	private String sql;
	private ArrayList<EmailField> campos;

	public EmailType(String chave, String sql) {
		this.chave = chave;
		this.sql = sql;
		this.campos = new ArrayList<EmailField>();
	}

	public EmailType(String chave, String sql, ArrayList<EmailField> campos) {
		this.chave = chave;
		this.sql = sql;
		this.campos = campos;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public ArrayList<EmailField> getCampos() {
		return campos;
	}

	public void setCampos(ArrayList<EmailField> campos) {
		this.campos = campos;
	}

}
