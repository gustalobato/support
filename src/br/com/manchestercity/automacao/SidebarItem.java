
package br.com.manchestercity.automacao;

public class SidebarItem {

	String mAcao;
	String mLink;
	String mLabel;
	String mIcone;
	String mToolTip;
	String mActionJS;
	String mQuantidade;

	boolean mSeparador;

	public SidebarItem() {
		mAcao = "";
		mLink = "";
		mLabel = "";
		mIcone = "";
		mToolTip = "";
		mActionJS = "";
		mQuantidade = "";

		mSeparador = false;
	}

	public void setAcao(String pValue) {
		this.mAcao = pValue;
	}

	public String getAcao() {
		return this.mAcao;
	}

	public void setLink(String pValue) {
		this.mLink = pValue;
	}

	public String getLink() {
		return this.mLink;
	}

	public void setLabel(String pValue) {
		this.mLabel = pValue;
	}

	public String getLabel() {
		return this.mLabel;
	}

	public void setIcone(String pValue) {
		this.mIcone = pValue;
	}

	public String getIcone() {
		return this.mIcone;
	}

	public void setToolTip(String pValue) {
		this.mToolTip = pValue;
	}

	public String getToolTip() {
		return this.mToolTip;
	}

	public void setActionJS(String pValue) {
		this.mActionJS = pValue;
	}

	public String getActionJS() {
		return this.mActionJS;
	}

	public void setQuantidade(String pValue) {
		this.mQuantidade = pValue;
	}

	public String getQuantidade() {
		return this.mQuantidade;
	}

	public void setSeparador(boolean pValue) {
		this.mSeparador = pValue;
	}

	public boolean isSeparador() {
		return this.mSeparador;
	}
}
