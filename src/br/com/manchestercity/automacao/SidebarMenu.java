
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class SidebarMenu {

	User mUser;
	Database lcdb;

	String mSelectedItem;

	SidebarItem mSidebarItem;
	ArrayList<SidebarItem> mSidebarItens;

	public SidebarMenu(User pUser) {
		mUser = pUser;
		lcdb = new Database(mUser);

		mSelectedItem = "";

		mSidebarItem = new SidebarItem();
		mSidebarItens = new ArrayList<SidebarItem>();
	}

	public void addMenuItem(String pAcao, String pLink, String pLabel, String pIcone, String pToolTip, String pActionJS, String pQuantidade, boolean pSeparador) {
		this.mSidebarItem = new SidebarItem();
		this.mSidebarItem.setAcao(pAcao);
		this.mSidebarItem.setLink(pLink);
		this.mSidebarItem.setLabel(pLabel);
		this.mSidebarItem.setIcone(pIcone);
		this.mSidebarItem.setToolTip(pToolTip);
		this.mSidebarItem.setActionJS(pActionJS);
		this.mSidebarItem.setQuantidade(pQuantidade);
		this.mSidebarItem.setSeparador(pSeparador);

		mSidebarItens.add(this.mSidebarItem);
	}

	public void setSelectedItem(String pValue) {
		this.mSelectedItem = pValue;
	}

	public String getSelectedItem() {
		return this.mSelectedItem;
	}

	public int getItensCount() {
		return this.mSidebarItens.size();
	}

	public String printSidebarMenu() {
		StringBuffer lHtml = new StringBuffer();

		lHtml.append("<script>");
		lHtml.append("function GoTab(url, acao) { \n");
		lHtml.append("  try { top.showChargerPage(window.name.replace('id', 'fr')); } catch(e) {} \n");
		lHtml.append("  document._frm.action = url; \n");
		lHtml.append("  document._frm.acao.value = acao; \n");
		lHtml.append("  try { document._frm.pagina.value = 0; } catch(e) {} \n");
		lHtml.append("  document._frm.submit(); \n");
		lHtml.append("}");
		lHtml.append("</script>");

		// BEGIN SIDEBAR
		lHtml.append("<div class='page-sidebar navbar-collapse collapse'> \n");
		// DOC: Set data-auto-scroll='false' to disable the sidebar from auto scrolling/focusing
		// DOC: Change data-auto-speed='200' to adjust the sub menu slide up/down speed

		// BEGIN SIDEBAR MENU
		lHtml.append("  <ul class='page-sidebar-menu' data-auto-scroll='true' data-slide-speed='200'> \n");

		// BEGIN SIDEBAR TOGGLER BUTTON
		// lHtml.append(" <li class='sidebar-toggler-wrapper'> \n");
		// lHtml.append(" <div class='sidebar-toggler'></div> \n");
		// lHtml.append(" </li> \n");
		// END SIDEBAR TOGGLER BUTTON

		for (SidebarItem item : this.mSidebarItens) {
			// BEGIN SINGLE ITEM
			if (item.getLabel().trim().length() > 0) {
				if (item.isSeparador()) {
					lHtml.append("    <li class='divider'>\n");
					lHtml.append("      <a href='javascript:;'>\n");
					lHtml.append("        <span class='title'> " + item.getLabel() + " </span>\n");
					if (!item.getQuantidade().trim().equals("") && !item.getQuantidade().trim().equals("0")) {
						lHtml.append("        <span class='badge bg-badge-divider'> " + item.getQuantidade() + " </span> \n");
					}
					lHtml.append("      </a>\n");
					lHtml.append("    </li>\n");
				}
				else {
					if (item.getLink().equals(this.getSelectedItem())) {
						lHtml.append("    <li class='start active'> \n");
					}
					else {
						lHtml.append("    <li class='tooltips' data-placement='right' data-container='body' data-html='true' data-original-title='" + (item.getToolTip().length() > 0 ? item.getToolTip() : item.getLabel()) + "'> \n");
					}
					lHtml.append("      <a href='javascript:;' onclick=\"" + (item.getActionJS().equals("") ? "GoTab('" + item.getLink() + "', '" + item.getAcao() + "')" : item.getActionJS()) + "\"> \n");
					lHtml.append("        " + item.getIcone() + " \n");
					lHtml.append("        <span class='title'> " + item.getLabel() + " </span> \n");
					if (!item.getQuantidade().trim().equals("") && !item.getQuantidade().trim().equals("0")) {
						lHtml.append("        <span class='badge bg-badge-default'> " + item.getQuantidade() + " </span> \n");
					}
					lHtml.append("        <span class='selected'></span> \n");
					lHtml.append("      </a> \n");
					lHtml.append("    </li> \n");
				}
			}
			// END SINGLE DEMO
		}

		lHtml.append("  </ul> \n");
		// END SIDEBAR MENU
		lHtml.append("</div> \n");
		// END SIDEBAR

		return lHtml.toString();
	}
}
