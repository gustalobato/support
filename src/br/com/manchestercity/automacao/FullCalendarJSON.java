
package br.com.manchestercity.automacao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class FullCalendarJSON extends HttpServlet {

	private static final long serialVersionUID = -1783345618144634370L;

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<FullCalendarDTO> list = new ArrayList<FullCalendarDTO>();

		String targetPage = Database.verifyNull(request.getParameter("targetPage"));
		targetPage = targetPage.indexOf("?") < 0 ? targetPage + "?" : targetPage;

		String table = Database.verifyNull(request.getParameter("table"));
		String code = Database.verifyNull(request.getParameter("code"));
		String descript = Database.verifyNull(request.getParameter("descript"));
		String date = Database.verifyNull(request.getParameter("date"));
		String color = Database.verifyNull(request.getParameter("color"));

		String companyCode = Database.verifyNull(request.getParameter("companyCode"));
		String companyField = Database.verifyNull(request.getParameter("companyField"));

		String month = Database.verifyNull(request.getParameter("month"));
		String year = Database.verifyNull(request.getParameter("year"));

		User user = User.getSession(request, response, false, true, "USER");

		Database lcdb = new Database(user);
		Connection conn = lcdb.openConnection();

		Utils util = new Utils(user);

		if (month.trim().equals("")) {
			month = lcdb.getActualDate(DateType.TIME);
		}
		if (year.trim().equals("")) {
			year = lcdb.getActualDate(DateType.DATE_TIME);
		}
		if (color.trim().equals("")) {
			color = "'#4B77BE'";
		}

		String sql = "";
		sql += "SELECT \n";
		sql += "  " + code + " AS EVENT_CODE, \n";
		sql += "  " + descript + " AS EVENT_DESCRIPT, \n";
		sql += "  " + date + " AS EVENT_DATE, \n";
		sql += "  " + color + " AS EVENT_COLOR \n";
		sql += "FROM " + table + " \n";
		sql += "WHERE " + BuildSql.getTruncateDate(date) + " >= " + BuildSql.getTruncateDate(lcdb.charToDate("'" + monthLastWeek(util, Database.verifyNullInt(year), Database.verifyNullInt(month) - 1) + "'")) + " \n";
		sql += "  AND " + BuildSql.getTruncateDate(date) + " <= " + BuildSql.getTruncateDate(lcdb.charToDate("'" + monthFirstWeek(util, Database.verifyNullInt(year), Database.verifyNullInt(month) - 1) + "'")) + " \n";
		sql += "  AND " + date + " IS NOT NULL \n";
		if (!companyCode.trim().equals("") && !companyField.trim().equals("")) {
			sql += "  AND " + companyField + " = " + lcdb.verifyInsertNull(companyCode, DataType.INTEGER) + " \n";
		}

		ResultSet rs = lcdb.openResultSet(sql, conn);
		try {
			while (rs.next()) {
				FullCalendarDTO calendar = new FullCalendarDTO();

				calendar = new FullCalendarDTO();
				calendar.setId(Database.verifyNull(rs.getObject("EVENT_CODE")));
				calendar.setStart(Database.verifyNull(rs.getObject("EVENT_DATE")));
				calendar.setTitle(Database.verifyNull(rs.getObject("EVENT_DESCRIPT")));
				calendar.setColor(Database.verifyNull(rs.getObject("EVENT_COLOR")));
				calendar.setUrl(targetPage + "&" + ( code.contains(".") ? code.split("\\.")[1] : code ) + "=" + Database.verifyNull(rs.getObject("EVENT_CODE")));

				list.add(calendar);
			}
		}
		catch (Exception e) {
			System.err.println("ERRO: CCadCalendarJSON: " + e.getMessage());
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(new Gson().toJson(list));
	}

	private String monthLastWeek(Utils util, int actualYear, int actualMonth) {
		Calendar lastWeek = new GregorianCalendar();

		// SET THE DATE TO THE 1st DAY OF THE MONTH
		lastWeek.set(actualYear, actualMonth, 1);
		lastWeek.add(Calendar.DAY_OF_WEEK, -7);

		return util.calendarToString(lastWeek);
	}

	private String monthFirstWeek(Utils util, int actualYear, int actualMonth) {
		Calendar firstWeek = new GregorianCalendar();

		// SET THE DATE TO THE DAY BEFORE THE 1st DAY OF THE NEXT MONTH
		firstWeek.set(actualYear, actualMonth + 1, 0);
		firstWeek.add(Calendar.DAY_OF_WEEK, 7);

		return util.calendarToString(firstWeek);
	}

}
