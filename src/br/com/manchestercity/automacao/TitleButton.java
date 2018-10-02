
package br.com.manchestercity.automacao;

public class TitleButton {

	protected String mID;
	protected String mJSAction;
	protected String mLabel;
	protected String mIcon;
	protected boolean mConfirm;
	protected boolean mModal;
	protected String mModalTarget;
	protected String mColor;

	public TitleButton() {
		mID = "";
		mJSAction = "";
		mLabel = "";
		mIcon = "";
		mConfirm = false;
		mModal = false;
		mColor = "default";
	}

	public String getIcon() {
		return mIcon;
	}

	public void setIcon(String pValue) {
		mIcon = pValue;
	}

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String pValue) {
		mLabel = pValue;
	}

	public String getJSAction() {
		return mJSAction;
	}

	public void setJSAction(String pValue) {
		mJSAction = pValue;
	}

	public boolean getConfirm() {
		return mConfirm;
	}

	public void setConfirm(boolean pValue) {
		mConfirm = pValue;
	}

	public String getID() {
		return mID;
	}

	public void setID(String pValue) {
		mID = pValue;
	}

	public boolean getModal() {
		return mModal;
	}

	public void setModal(boolean pValue) {
		mModal = pValue;
	}

	public String getModalTarget() {
		return mModalTarget;
	}

	public void setModalTarget(String pValue) {
		mModalTarget = pValue;
	}

	public String getColor() {
		return mColor;
	}

	public void setColor(String pValue) {
		mColor = pValue;
	}

}
