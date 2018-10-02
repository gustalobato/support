
package br.com.manchestercity.automacao;

import java.util.ArrayList;
import java.util.Iterator;

public class HeaderDropdown {

	protected User mUser;
	protected Database lcdb;
	protected ArrayList<HeaderDropdownNode> mNodes;

	protected String mID;

	public HeaderDropdown(User pUser) {
		mUser = pUser;
		lcdb = new Database(mUser);

		mNodes = new ArrayList<HeaderDropdownNode>();

		mID = "";
	}

	public static HeaderDropdownNode createNode(String pIcon, String pLabel, String pLink, String pActionJS, HeaderDropdownNode pParent) {
		HeaderDropdownNode lNode = new HeaderDropdownNode();

		lNode.setIcon(pIcon);
		lNode.setLabel(pLabel);
		lNode.setLink(pLink);
		lNode.setActionJS(pActionJS);
		lNode.setParent(pParent);

		return lNode;
	}

	public void addNode(HeaderDropdownNode node) {
		node.setId("_isg_node_" + ( mNodes.size() + 1 ));
		mNodes.add(node);
	}

	public HeaderDropdownNode addNode(String pIcon, String pLabel, String pLink, String pActionJS, HeaderDropdownNode pParent) {
		HeaderDropdownNode lNode = new HeaderDropdownNode();

		lNode.setId("_isg_node_" + ( mNodes.size() + 1 ));
		lNode.setIcon(pIcon);
		lNode.setLabel(pLabel);
		lNode.setLink(pLink);
		lNode.setActionJS(pActionJS);
		lNode.setParent(pParent);

		mNodes.add(lNode);

		return lNode;
	}

	private int haveChild(HeaderDropdownNode pNode) {
		int i;
		HeaderDropdownNode lNode = null;
		int lHaveChild = 0;

		for (i = 0; i < mNodes.size(); i++) {
			lNode = (HeaderDropdownNode) mNodes.get(i);
			if (lNode.getParent() != null) {
				if (lNode.getParent().getId() == pNode.getId()) {
					if (!lNode.getLink().equals("")) {
						lHaveChild++;
					}
					else {
						if (haveChild(lNode) > 0) {
							lHaveChild++;
						}
					}
				}
			}
		}
		return lHaveChild;

	}

	public String showHeaderDropdown() {
		StringBuffer lHtml = new StringBuffer();

		lHtml.append("<div class='hor-menu hor-menu-light hidden-sm hidden-xs'> \n");
		lHtml.append("  <ul class='nav navbar-nav'" + ( mID.length() > 0 ? " id='" + mID + "'" : "" ) + "> \n");

		Iterator<HeaderDropdownNode> i = mNodes.iterator();
		while (i.hasNext()) {
			HeaderDropdownNode lNode = (HeaderDropdownNode) i.next();
			if (lNode.getParent() == null && ( haveChild(lNode) > 0 || !Database.verifyNull(lNode.getLink()).equals("") || !Database.verifyNull(lNode.getActionJS()).equals("") )) {
				lHtml.append(printNode(lNode));
			}
		}

		lHtml.append("  </ul> \n");
		lHtml.append("</div> \n");

		return lHtml.toString();
	}

	private String printNode(HeaderDropdownNode pNode) {
		StringBuffer strNode = new StringBuffer();

		int lChildren = haveChild(pNode);
		String nodeLabel = mUser.getTermo(pNode.getLabel()).startsWith("***") ? pNode.getLabel() : mUser.getTermo(pNode.getLabel());
//		
//		String nodeLabel = mUser.getTermo(pNode.getLabel());

		if (lChildren == 0) {
			if (pNode.getParent() == null) {
				if (!Database.verifyNull(pNode.getActionJS()).equals("")) {
					strNode.append("  <li class='search-menu-selector classic-menu-dropdown active' onclick=\"" + pNode.getActionJS() + "\"> \n");
				}
				else {
					strNode.append("  <li class='search-menu-selector classic-menu-dropdown active' onclick=\"addAba('" + pNode.getLink() + "', '" + nodeLabel + "', '');\"> \n");
				}
			}
			else {
				if (!Database.verifyNull(pNode.getActionJS()).equals("")) {
					strNode.append("  <li class='search-menu-selector' onclick=\"" + pNode.getActionJS() + "\"> \n");
				}
				else {
					strNode.append("  <li class='search-menu-selector' onclick=\"addAba('" + pNode.getLink() + "', '" + nodeLabel + "', '');\"> \n");
				}
			}

			strNode.append("    <a href='javascript:;'> \n");
			strNode.append("      " + pNode.getIcon() + "&nbsp;" + nodeLabel + "&nbsp;&nbsp; \n");
			strNode.append("    </a> \n");
			strNode.append("  </li> \n");
		}
		else {
			if (pNode.getParent() == null) {
				strNode.append("  <li class='classic-menu-dropdown'> \n");
				strNode.append("    <a data-toggle='dropdown' href='javascript:;'>" + pNode.getIcon() + "&nbsp;" + nodeLabel + "&nbsp;&nbsp;" + SystemIcons.ICON_ANGLE_DOWN + "</a> \n");
				strNode.append("    <ul class='dropdown-menu pull-left'> \n");
			}
			else {
				strNode.append("  <li class='dropdown-submenu'> \n");
				strNode.append("    <a href='javascript:;'>" + pNode.getIcon() + "&nbsp;" + nodeLabel + "&nbsp;&nbsp;</a> \n");
				strNode.append("    <ul class='dropdown-menu'> \n");
			}

			Iterator<HeaderDropdownNode> i = mNodes.iterator();
			while (i.hasNext()) {
				HeaderDropdownNode lNode = (HeaderDropdownNode) i.next();
				if (lNode.getParent() != null && lNode.getParent().getId() == pNode.getId()) {
					if (haveChild(lNode) > 0 || !Database.verifyNull(lNode.getLink()).equals("") || !Database.verifyNull(lNode.getActionJS()).equals("")) {
						strNode.append(this.printNode(lNode));
					}
				}
			}

			strNode.append("    </ul> \n");
			strNode.append("  </li> \n");
		}

		return strNode.toString();
	}

	public String showAsSidebar() {
		StringBuffer lHtml = new StringBuffer();

		lHtml.append("<div class='page-sidebar-wrapper'> \n");
		// BEGIN HORIZONTAL RESPONSIVE MENU
		// DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing
		// DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed
		lHtml.append("  <div class='page-sidebar navbar-collapse collapse'> \n");
		lHtml.append("    <ul class='page-sidebar-menu hidden-md hidden-lg' data-slide-speed='200' data-auto-scroll='true'> \n");

		Iterator<HeaderDropdownNode> i = mNodes.iterator();
		while (i.hasNext()) {
			HeaderDropdownNode lNode = (HeaderDropdownNode) i.next();
			if (lNode.getParent() == null && ( haveChild(lNode) > 0 || !Database.verifyNull(lNode.getLink()).equals("") )) {
				lHtml.append(printSidebarNode(lNode));
			}
		}

		lHtml.append("    </ul> \n");
		lHtml.append("  </div> \n");
		lHtml.append("</div> \n");

		return lHtml.toString();
	}

	private String printSidebarNode(HeaderDropdownNode pNode) {
		StringBuffer strNode = new StringBuffer();

		int lChildren = haveChild(pNode);

		if (lChildren == 0) {
			if (pNode.getParent() == null) {
				strNode.append("  <li class='active' onclick=\"addAba('" + pNode.getLink() + "', '" + mUser.getTermo(pNode.getLabel()) + "', '');\"> \n");
			}
			else {
				strNode.append("  <li onclick=\"addAba('" + pNode.getLink() + "', '" + mUser.getTermo(pNode.getLabel()) + "', '');\"> \n");
			}

			strNode.append("    <a href='javascript:;'> \n");
			strNode.append("      " + pNode.getIcon() + "&nbsp;" + mUser.getTermo(pNode.getLabel()) + "&nbsp;&nbsp; \n");
			strNode.append("    </a> \n");
			strNode.append("  </li> \n");
		}
		else {
			strNode.append("  <li> \n");
			strNode.append("    <a href='javascript:;'>" + pNode.getIcon() + " " + mUser.getTermo(pNode.getLabel()) + " </a> \n");
			strNode.append("    <ul class='sub-menu'> \n");

			Iterator<HeaderDropdownNode> i = mNodes.iterator();
			while (i.hasNext()) {
				HeaderDropdownNode lNode = (HeaderDropdownNode) i.next();
				if (lNode.getParent() != null && lNode.getParent().getId() == pNode.getId()) {
					if (haveChild(lNode) > 0 || !Database.verifyNull(lNode.getLink()).equals("")) {
						strNode.append(this.printSidebarNode(lNode));
					}
				}
			}

			strNode.append("    </ul> \n");
			strNode.append("  </li> \n");
		}

		return strNode.toString();
	}

	public String getID() {
		return mID;
	}

	public void setID(String mID) {
		this.mID = mID;
	}

}
