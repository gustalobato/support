
package br.com.manchestercity.automacao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmartComboAjax extends HttpServlet {

	private static final long serialVersionUID = 358579531307530473L;

	private static final String CONTENT_TYPE = "text/html";

	int mCurPage;
	int mItemCount;
	int mColumnCount;

	long mTotalCount = 0;
	long mPrimaryCount = 0;

	String mDisplayColumns = "";

	String mQuickSearch = "";

	String mQuerySelect = "";
	String mQueryFrom = "";
	String mQueryWhere = "";
	String mQueryGroup = "";
	String mQueryOrder = "";
	String mQueryHaving = "";

	String mQuerySearch = "";
	String mQueryOrderBy = "";
	String mQueryOrderByAlias = "";

	String[] dependencies = null;

	boolean mDistinct = true;

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		request.getCharacterEncoding();

		User mUser = new User(request); // CUser.getSession(request, response);
		Utils mUtil = new Utils(mUser);
		mUtil.noCache(response, request);

		response.setHeader("Content-Type", "text/html;charset=" + mUser.getUserCharset());

		Database lcdb = new Database(mUser);
		Connection mConn = lcdb.openConnection();

		mCurPage = Database.verifyNullInt(request.getParameter("pageNum")) - 1;
		mItemCount = Database.verifyNullInt(request.getParameter("pageSize"));
		mColumnCount = Database.verifyNullInt(request.getParameter("columnCount"));
		mPrimaryCount = Database.verifyNullInt(request.getParameter("primaryCount"));
		mDisplayColumns = Database.verifyNull(request.getParameter("displayColumns"));

		// mQuickSearch = CDatabase.VerifyNull(request.getParameter("searchTerm"));
		mQuickSearch = Database.verifyNull(new String(request.getParameter("searchTerm").getBytes(mUser.getUserCharset()), "UTF-8"));

		mQuerySelect = Database.verifyNull(request.getParameter("selectCols"));
		mQueryFrom = Database.verifyNull(request.getParameter("selectFrom"));
		mQueryWhere = Database.verifyNull(request.getParameter("selectWhere"));
		mQueryGroup = Database.verifyNull(request.getParameter("selectGroup"));
		mQueryOrder = Database.verifyNull(request.getParameter("selectOrder"));
		mQueryHaving = Database.verifyNull(request.getParameter("selectHaving"));

		// mQuerySearch = CDatabase.VerifyNull(request.getParameter("selectSearch"));
		mQuerySearch = Database.verifyNull(new String(request.getParameter("selectSearch").getBytes(mUser.getUserCharset()), "UTF-8"));
		mQueryOrderBy = Database.verifyNull(request.getParameter("selectOrder"));
		mQueryOrderByAlias = Database.verifyNull(request.getParameter("selectOrderAlias"));

		mDistinct = Database.verifyNull(request.getParameter("selectDistinct")).equals("true");

		ArrayList<String> displayColumns = new ArrayList<String>(Arrays.asList(mDisplayColumns.split(",")));

		dependencies = request.getParameter("dependencies") != null ? request.getParameter("dependencies").split(",") : null;
		if (dependencies != null && dependencies.length > 0) {
			for (String dep : dependencies) {
				if (!dep.isEmpty()) {
					mQueryWhere += (mQueryWhere.trim().equals("") ? "" : " AND ") + dep.replaceAll("##COMMA##", ",");
				}

			}
		}

		String filtroExtra = Database.verifyNull(request.getParameter("filtroExtra")).trim();
		if (!filtroExtra.equals("")) {
			mQueryWhere += (mQueryWhere.trim().equals("") ? "" : " AND ") + filtroExtra;
		}

		String filtroLista = Database.verifyNull(request.getParameter("filtroLista")).trim();
		if (!filtroLista.equals("")) {
			mQueryWhere += (mQueryWhere.trim().equals("") ? "" : " AND ") + filtroLista;
		}

		String buscaValor = Database.verifyNull(request.getParameter("buscaValor")).trim();
		if (!buscaValor.equals("")) {
			mQueryWhere = buscaValor;
		}

		String sql = this.buildQueryDisplay(lcdb);

		// PRINT PAGINATED DATA OBJECT
		PrintWriter out = response.getWriter();
		out.println("{");
		out.println("  \"total\": \"" + mTotalCount + "\",");
		out.println("  \"results\": [");

		ResultSet rs = lcdb.openResultSet(sql, mConn);
		try {
			boolean existe = false;

			String id = "";
			String text = "";

			String extraAttrs = "";

			StringBuffer results = new StringBuffer();

			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {

				for (int i = 1; i <= rsmd.getColumnCount(); i++) {

					if (i <= mColumnCount) {
						if (i <= mPrimaryCount) {
							id += (id.trim().equals("") ? "" : "$|$") + Database.verifyNull(rs.getObject(i), "0");
						}
						if (displayColumns.contains(rsmd.getColumnName(i))) {
							text += (text.trim().equals("") ? "" : " - ") + Database.verifyNull(rs.getObject(i), "0");
						}
					}

					extraAttrs += ", \"" + rsmd.getColumnName(i) + "\": \"" + Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(Database.verifyNull(rs.getObject(i), ""), "\"", "\\\""), "\\", "\\\\"), "\r", ""), "\n", "<br/>") + "\"";
				}

				if (results.length() > 0) {
					results.append(",");
				}
				results.append("{ \"id\": \"" + id + "\", \"text\": \"" + Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(Utils.replaceAll(text, "\"", "\\\""), "\\", "\\\\"), "\r", ""), "\n", "<br/>") + "\"" + (extraAttrs.trim().equals("") ? "" : extraAttrs) + " }");

				id = "";
				text = "";

				extraAttrs = "";

				existe = true;
			}

			if (!existe) {
				out.println("");
			}
			else {
				out.println(results);
			}
		}
		catch (Exception e) {
			Database.closeObject(mConn);
			Utils.printSystemError("ResultSet", e.getMessage());

			out.println("");
		}
		finally {
			Database.closeObject(rs);
		}

		out.println("  ]");
		out.println("}");

		lcdb = null;
		Database.closeObject(mConn);
	}

	private String buildQueryDisplay(Database lcdb) {

		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		if (mDistinct) {
			query.append("DISTINCT ");
		}
		query.append(mQuerySelect);
		query.append(" FROM ");
		query.append(mQueryFrom);

		if (mQueryWhere != null && mQueryWhere.trim().length() > 0) {
			query.append(" WHERE ");
			query.append(mQueryWhere);
		}

		if (mQueryGroup != null && mQueryGroup.trim().length() > 0) {
			query.append(" GROUP BY ");
			query.append(mQueryGroup);
		}

		if (mQueryHaving != null && mQueryHaving.trim().length() > 0) {
			query.append(" HAVING ");
			query.append(mQueryHaving);
		}

		if (!Database.isDBMS(Database.DBMS_SQLSERVER)) {
			query.append(mQueryOrderBy);
		}

		// QUICKSEARCH
		if (!mQuickSearch.trim().equals("") && !mQuerySearch.trim().equals("")) {
			String quickSearch = Utils.replaceAll(mQuerySearch, "{|SEARCH|}", mQuickSearch);

			query.insert(0, "SELECT * FROM (");
			query.append(") ");
			query.append(BuildSql.getAsSubQuery("SMARTCOMBO_TB_1234"));
			query.append(" WHERE ");
			query.append(quickSearch);
		}

		mTotalCount = lcdb.getRecordCount(query.toString());

		if (mItemCount > 0) {
			if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
				query.insert(0, "SELECT * \n FROM (SELECT SMARTCOMBO_TABLE_AUX.*, SMARTCOMBO_ROW_NUMBER = ROW_NUMBER() OVER (" + (mQueryOrderByAlias.length() > 0 ? mQueryOrderByAlias : Utils.replaceAll(mQueryOrderBy, ".", "_")) + ") \n FROM (");
				query.append(") SMARTCOMBO_TABLE_AUX) SMARTCOMBO_TABLE_AUX_2 \n WHERE SMARTCOMBO_TABLE_AUX_2.SMARTCOMBO_ROW_NUMBER >= " + ((mItemCount * mCurPage) + 1) + " AND SMARTCOMBO_TABLE_AUX_2.SMARTCOMBO_ROW_NUMBER <= " + ((mItemCount * mCurPage) + mItemCount));
			}
			else if (Database.isDBMS(Database.DBMS_ORACLE)) {
				query.insert(0, "SELECT * \n FROM (SELECT SMARTCOMBO_TABLE_AUX.*, ROWNUM AS SMARTCOMBO_ROW_NUMBER \n FROM (");
				query.append(") SMARTCOMBO_TABLE_AUX \n WHERE ROWNUM <= " + ((mItemCount * mCurPage) + mItemCount) + ") SMARTCOMBO_TABLE_AUX_2 \n WHERE SMARTCOMBO_TABLE_AUX_2.SMARTCOMBO_ROW_NUMBER >= " + ((mItemCount * mCurPage) + 1));
			}
			else if (Database.isDBMS(Database.DBMS_MYSQL)) {
				query.append(" LIMIT ");
				query.append(mItemCount);
				query.append(" OFFSET ");
				query.append((mCurPage) * mItemCount);
			}
		}

		return query.toString();
	}

	// CLEAN UP RESOURCES
	public void destroy() {
	}
}
