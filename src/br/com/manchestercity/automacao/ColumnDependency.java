
package br.com.manchestercity.automacao;

/**
 * Classe responsável por representar uma coluna de dependências do novo Localizador (CSmartCombo).
 * 
 * @author Bruno Arrivabene
 * @since 2014
 * @version 1.0
 */

public class ColumnDependency extends Column {

	private String remoteFieldId;
	private String remoteColumn;
	private String localColumn;
	private boolean equals;
	private String requiredMessage;

	public String getRemoteFieldId() {
		return remoteFieldId;
	}

	public void setRemoteFieldId(String remoteFieldId) {
		this.remoteFieldId = remoteFieldId;
	}

	public String getRemoteColumn() {
		return remoteColumn;
	}

	public void setRemoteColumn(String remoteColumn) {
		this.remoteColumn = remoteColumn;
	}

	public String getLocalColumn() {
		return localColumn;
	}

	public void setLocalColumn(String localColumn) {
		this.localColumn = localColumn;
	}

	public boolean isEquals() {
		return equals;
	}

	public void setEquals(boolean equals) {
		this.equals = equals;
	}

	public String getRequiredMessage() {
		return requiredMessage;
	}

	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}

}
