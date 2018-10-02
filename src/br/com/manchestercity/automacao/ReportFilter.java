
package br.com.manchestercity.automacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.manchestercity.automacao.FrameworkDefaults.ColumnWidth;
import br.com.manchestercity.automacao.FrameworkDefaults.FormState;
import br.com.manchestercity.automacao.FrameworkDefaults.PortletBox;

public class ReportFilter {

	public static int GRUPO_GERAL = 0;
	public static int GRUPO_OFERTA = 1;
	public static int GRUPO_REQUISICAO = 2;

	public static int GRUPO_VAZIO = 999;

	private int orderCount = 0;

	private HttpServletRequest request;
	private User user;
	private Database lcdb;

	private String clearFunctionName;

	private String submitJS;
	private StringBuffer filterJS;

	private boolean onlyLogged;
	private boolean groupsClosed;

	private HashMap<Integer, String> groupList;
	private HashMap<String, ReportFilterField> fieldList;

	private FrameworkDefaults defaults;
	private PortletBox portletBox;

	private ReportFilterField field;

	private FormState filterState;

	public ReportFilter(HttpServletRequest request, User user) {
		this.request = request;

		this.user = user;

		this.lcdb = new Database(user);
		this.groupList = new HashMap<Integer, String>();
		this.fieldList = new HashMap<String, ReportFilterField>();

		defaults = new FrameworkDefaults();
		portletBox = defaults.new PortletBox();

		field = null;

		filterState = Database.verifyNull(request.getParameter("_filter_state")).equals(FormState.SHOW.toString()) ? FormState.SHOW : Database.verifyNull(request.getParameter("_filter_state")).equals(FormState.BACK.toString()) ? FormState.BACK : FormState.INITIAL;

		this.clearFunctionName = "clearReportFilter";

		this.submitJS = "$('form').submit( function() { top.showChargerPage(window.name); } );";
		this.filterJS = new StringBuffer();

		// this.init();
	}

	public void init() {
	}

	public String getClearFunctionName() {
		return clearFunctionName;
	}

	public void setClearFunctionName(String clearFunctionName) {
		this.clearFunctionName = clearFunctionName;
	}

	public String getSubmitJS() {
		return submitJS;
	}

	public void setSubmitJS(String submitJS) {
		this.submitJS = submitJS;
	}

	public boolean isOnlyLogged() {
		return onlyLogged;
	}

	public void setOnlyLogged(boolean onlyLogged) {
		this.onlyLogged = onlyLogged;
	}

	public boolean isGroupsClosed() {
		return groupsClosed;
	}

	public void setGroupsClosed(boolean groupsClosed) {
		this.groupsClosed = groupsClosed;
	}

	public FormState getFilterState() {
		return filterState;
	}

	public void setFilterState(FormState filterState) {
		this.filterState = filterState;
	}

	public void includeNewGroup(int key, String title) {
		if (key < 999 && key > 9 && !Database.verifyNull(title).trim().equals("")) {
			this.groupList.put(key, title);
		}
	}

	public ReportFilterField getField(String key) {
		return fieldList.get(key);
	}

	public void setShow(String key, boolean show) {
		if (Database.verifyNull(key).length() > 0 && fieldList.containsKey(key)) {
			fieldList.get(key).setShow(show);
			if (show) {
				fieldList.get(key).setOrder(this.orderCount);
				this.orderCount++;
			}
		}
	}

	public boolean isShow(String key) {
		if (Database.verifyNull(key).length() > 0 && fieldList.containsKey(key)) {
			return fieldList.get(key).isShow();
		}
		return false;
	}

	public void includeField(String key, ReportFilterField field) {
		if (field != null) {
			field.setFilterKey(key);
			field.setParentFilter(this);

			this.fieldList.put(key, field);
		}
	}

	public void removeField(String key) {
		if (Database.verifyNull(key).length() > 0 && fieldList.containsKey(key)) {
			fieldList.remove(key);
		}
	}

	public void requestValues() {
		Iterator<ReportFilterField> i = fieldList.values().iterator();

		while (i.hasNext()) {
			( (ReportFilterField) i.next() ).requestValues();
		}
	}

	public String printFields() {
		StringBuffer html = new StringBuffer();
		StringBuffer content = new StringBuffer();

		html.append("<input type='hidden' name='_filter_state' id='_filter_state' value='" + filterState + "' />\n");

		Iterator<ReportFilterField> i = null;
		try {
			List<ReportFilterField> valores = new ArrayList<ReportFilterField>(fieldList.values());
			Collections.sort(valores, new ReportFilterComparator());

			i = valores.iterator();
		}
		catch (Exception e) {
			i = fieldList.values().iterator();
		}

		this.loadFilterJS();

		int grupo = -1;
		boolean showGroup = false;

		StringBuffer clearField = new StringBuffer();

		ReportFilterField f;
		while (i.hasNext()) {
			f = i.next();

			if (grupo >= 0 && grupo != f.getFilterGroup()) {
				portletBox.setPortlet("_report_filter_group_" + grupo, getGroupTitle(grupo), content.toString());
				portletBox.setPortletExtraClasses(ColumnWidth.TWELVE.toString());
				portletBox.setClosed(this.groupsClosed);
				if (!showGroup) {
					portletBox.setPortletExtraStyles("display: none;");
				}

				html.append(portletBox.printPortletBox());

				content.delete(0, content.length());

				showGroup = false;
			}

			grupo = f.getFilterGroup();
			showGroup = showGroup || f.isShow();

			content.append(f.printField(filterState));

			clearField.append(f.clearFieldJS(lcdb));
		}

		if (grupo >= 0) {
			if (grupo == GRUPO_VAZIO) {
				html.append(content.toString());
			}
			else {
				portletBox.setPortlet("_report_filter_group_" + grupo, getGroupTitle(grupo), content.toString());
				portletBox.setPortletExtraClasses(ColumnWidth.TWELVE.toString());
				portletBox.setClosed(this.groupsClosed);
				if (!showGroup) {
					portletBox.setPortletExtraStyles("display: none;");
				}

				html.append(portletBox.printPortletBox());
			}
			content.delete(0, content.length());
		}

		if (filterJS.length() > 0) {
			html.append("<script>\n");
			html.append(filterJS.toString());
			html.append("</script>\n");
		}

		if (clearField.length() > 0) {
			html.append("<script>\n");
			html.append("  function " + this.clearFunctionName + "() {\n");
			html.append(clearField.toString());
			html.append("  }\n");
			html.append("</script>\n");
		}

		return html.toString();
	}

	public String printDescription() {
		StringBuffer filterJS = new StringBuffer();

		ReportFilterField f;

		Iterator<ReportFilterField> i = fieldList.values().iterator();
		while (i.hasNext()) {
			f = i.next();
			filterJS.append(f.printDescription());
		}

		return filterJS.toString();
	}

	public String printHidden() {
		StringBuffer filterJS = new StringBuffer();

		ReportFilterField f;

		Iterator<ReportFilterField> i = fieldList.values().iterator();
		while (i.hasNext()) {
			f = i.next();
			filterJS.append(f.printHidden());
		}

		return filterJS.toString();
	}

	public String createWhere(boolean useAND) {
		StringBuffer sql = new StringBuffer();

		String auxSQL = "";

		ReportFilterField f;

		Iterator<ReportFilterField> i = fieldList.values().iterator();
		while (i.hasNext()) {
			f = i.next();

			if (f.getDinamicSubquery() != null) {
				auxSQL = f.getDinamicSubquery().createWhere(lcdb, useAND).trim();
			}
			else {
				auxSQL = f.createWhere(lcdb, useAND).trim();
			}

			if (auxSQL.length() > 0) {
				if (sql.length() > 0) {
					sql.append(useAND ? "  AND " : "  OR ");
				}
				sql.append(auxSQL);
				sql.append("\n");
			}
		}

		return sql.toString();
	}

	private String getGroupTitle(int group) {
		if (group == GRUPO_GERAL) {
			return user.getTermo("GERAL");
		}
		else if (group == GRUPO_OFERTA) {
			return user.getTermo("OFERTA");
		}
		else if (group == GRUPO_REQUISICAO) {
			return user.getTermo("REQUISICAO");
		}

		return groupList.get(group);
	}

	private void loadFilterJS() {
	}

}
