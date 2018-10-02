
package br.com.manchestercity.automacao;

public class HeaderDropdownNode {

	protected String mId;
	protected String mIcon;
	protected String mLabel;
	protected String mLink;
	protected String mActionJS;
	protected HeaderDropdownNode mParent;

	public HeaderDropdownNode() {
		mId = "";
		mIcon = "";
		mLabel = "";
		mLink = "";
		mActionJS = "";
		mParent = null;
	}

	public String getId() {
		return mId;
	}

	public void setId(String pId) {
		mId = pId;
	}

	public String getIcon() {
		return mIcon;
	}

	public void setIcon(String pIcon) {
		mIcon = pIcon;
	}

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String pLabel) {
		mLabel = pLabel;
	}

	public String getLink() {
		return mLink;
	}

	public void setLink(String pValue) {
		mLink = pValue;
	}

	public String getActionJS() {
		return mActionJS;
	}

	public void setActionJS(String pActionJS) {
		mActionJS = pActionJS;
	}

	public HeaderDropdownNode getParent() {
		return mParent;
	}

	public void setParent(HeaderDropdownNode pParent) {
		mParent = pParent;
	}

}
