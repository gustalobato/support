
package br.com.manchestercity.automacao;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.SystemConfig;
import net.sourceforge.jtds.jdbc.ClobImpl;

public class Database {

	public static final String DBMS_MYSQL = "MYSQL";
	public static final String DBMS_ORACLE = "ORACLE";
	public static final String DBMS_SQLSERVER = "SQLSERVER";

	public static final int TYPE_STRING = 0;
	public static final int TYPE_INTEGER = 1;
	public static final int TYPE_DATE = 2;
	public static final int TYPE_DATE_TIME = 3;
	public static final int TYPE_DATE_TIME_SECS = 4;
	public static final int TYPE_CLOB = 5;
	public static final int TYPE_BLOB = 6;

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGHT = 2;

	User mUser;
	String mDBMS;
	String mLastError;

	public User getUser() {
		return mUser;
	}

	public String getDBMS() {
		return mDBMS;
	}

	public String getLastError() {
		return mLastError;
	}

	public Database(User pUser) {
		mUser = pUser;
		mLastError = "";
		mDBMS = getProperty(IniManipulation.DBMS);
		mDBMS = ( mDBMS.equals("") ? Database.DBMS_ORACLE : mDBMS ).toUpperCase();
	}

	public static final String getProperty(String pProperty) {
		return IniManipulation.getProperty(pProperty);
	}

	// ======================================================
	// =============== MANIPULACAO DE NUMEROS ===============
	// ======================================================
	public String decimalFormat(double pValue, int pCasas) {
		return this.decimalFormat(String.valueOf(pValue), pCasas);
	}

	public String decimalFormat(String pValue, int pCasas, String pDefaultValue) {
		if (Database.verifyNull(pValue).trim().equals("")) {
			return pDefaultValue;
		}
		else {
			return this.decimalFormat(String.valueOf(pValue), pCasas);
		}
	}

	public String decimalFormat(String pValue, int pCasas) {
		String negative = "";

		String sepMil = mUser.getUserNumberMil();
		String sepDec = mUser.getUserNumberDec();
		String zeroes = "";

		String result = "";

		if (pValue.startsWith("-")) {
			negative = "-";
			pValue = pValue.substring(1);
		}

		pValue = convertAnyDecimalToDefault(pValue);

		// CRIANDO STRING DE ZEROS
		for (int i = 0; i < 100; i++)
			zeroes += "0";

		// VERIFICANDO VALOR NULO
		if (Database.verifyNull(pValue).equals("")) {
			pValue = "0";
		}

		String dec = "";
		String mil = "";

		// QUEBRANDO PARTES
		if (pValue.indexOf(".") >= 0) {
			dec = pValue.split("\\.")[1];
			mil = pValue.split("\\.")[0];
		}
		else {
			dec = "";
			mil = pValue;
		}

		// VERIFICANDO NOTAÇÃO CIENTÍFICA
		if (dec.indexOf("E") > 0) {
			int not = Integer.parseInt(dec.split("E")[1]);
			if (not > -1) {
				mil += ( dec.split("E")[0] + zeroes ).substring(0, not);
				if (dec.split("E")[0].length() > not) {
					dec = dec.split("E")[0].substring(not);
				}
				else {
					dec = "";
				}
			}
			else {
				if (Math.abs(not) > mil.length()) {
					mil = zeroes.substring(zeroes.length() + not) + mil;
				}
				dec = mil.substring(mil.length() + not, mil.length()) + dec.split("E")[0];
				mil = mil.substring(0, mil.length() + not);
			}
		}

		// DEFININDO TAMANHO DA PARTE DECIMAL
		if (pCasas > 0) {
			dec = ( dec + zeroes ).substring(0, pCasas);
		}
		else {
			dec = "";
		}

		// PREPARANDO O MILHAR
		if (!sepMil.equals("")) {
			String novo = "";
			int num = mil.length();
			while (num > 3) {
				num = num - 3;
				novo = mil.substring(num, num + 3) + ( novo.length() > 0 ? sepMil : "" ) + novo;

			}
			mil = mil.substring(0, num) + ( novo.length() > 0 ? sepMil : "" ) + novo;
		}

		if (dec.equals("")) {
			result = mil;
		}
		else {
			result = mil + sepDec + dec;
		}

		return negative + result;
	}

	public static String convertAnyDecimalToDefault(String pValue) {
		// CORRIGINDO BUG DO RETORNO REQUEST COM ERRO
		// TRATANDO O RETORNO COMO SE FOSSE VALOR DO BANCO
		String lRet = "";
		lRet = verifyNull(pValue);
		String lAux = "";
		String lAuxPos = "";
		int lLastPos = -1;
		for (int i = 0; i < lRet.length(); i++) {
			if (!lRet.substring(i, i + 1).matches("[0-9]|E|-")) {
				lAux += lRet.substring(lLastPos + 1, i);
				lAuxPos = lRet.substring(i + 1);
				lLastPos = i;
			}
		}
		if (!lAux.equals("")) {
			lRet = lAux + "." + lAuxPos;
		}

		return lRet;
	}

	// ======================================================
	// ================ MANIPULACAO DE DATAS ================
	// ======================================================
	public String getDateFormat() {
		return mUser.getUserDateFormat();
	}

	public String charToDate(String pField) {
		return charToDate(pField, false, false);
	}

	public String charToDateTime(String pField, boolean pSeconds) {
		return charToDate(pField, true, pSeconds);
	}

	public String charToDate(String pField, boolean pDateTime, boolean pSeconds) {
		String lFormat = mUser.getUserDateFormat();
		String lAux = "TO_DATE(" + pField + ", '" + lFormat + ( pDateTime ? " + hh24:mi" + ( pSeconds ? ":ss" : "" ) : "" ) + "')";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			if (lFormat.equalsIgnoreCase("dd/mm/yyyy")) {
				lAux = "CONVERT(DATETIME," + pField + ", 103)";
			}
			else if (lFormat.equalsIgnoreCase("mm/dd/yyyy")) {
				lAux = "CONVERT(DATETIME," + pField + ", 101)";
			}
			else if (lFormat.equalsIgnoreCase("yyyy.mm.dd")) {
				lAux = "CONVERT(DATETIME," + pField + ", 102)";
			}
			else if (lFormat.equalsIgnoreCase("dd.mm.yy")) {
				lAux = "CONVERT(DATETIME," + pField + ", 104)";
			}
			else if (lFormat.equalsIgnoreCase("dd-mm-yy")) {
				lAux = "CONVERT(DATETIME," + pField + ", 105)";
			}
			else if (lFormat.equalsIgnoreCase("mm-dd-yy")) {
				lAux = "CONVERT(DATETIME," + pField + ", 110)";
			}
			else if (lFormat.equalsIgnoreCase("yy/mm/dd")) {
				lAux = "CONVERT(DATETIME," + pField + ", 111)";
			}
			else if (lFormat.equalsIgnoreCase("yymmdd") || lFormat.equalsIgnoreCase("yyyymmdd")) {
				lAux = "CONVERT(DATETIME," + pField + ", 112)";
			}
			else {
				lAux = "CONVERT(DATETIME," + pField + ", 103)";
			}
		}
		return lAux;
	}

	public String dateToChar(String pField) {
		return dateToChar(pField, "", false);
	}

	public String dateTimeToChar(String pField) {
		return dateToChar(pField, "", true);
	}

	public String dateToChar(String pField, String pFormat, boolean pDateTime) {
		pFormat = ( !pFormat.equals("") ? pFormat : getDateFormat() );

		String lAux = "to_char(" + pField + ", '" + pFormat + ( pDateTime ? " hh24:mi" : "" ) + "')";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			if (pFormat.equalsIgnoreCase("dd/mm/yyyy")) {
				lAux = "CONVERT(VARCHAR," + pField + ", 103)";
			}
			else if (pFormat.equalsIgnoreCase("mm/dd/yyyy")) {
				lAux = "CONVERT(VARCHAR," + pField + ", 101)";
			}
			else if (pFormat.equalsIgnoreCase("yyyy.mm.dd")) {
				lAux = "CONVERT(VARCHAR," + pField + ", 102)";
			}
			else if (pFormat.equalsIgnoreCase("dd.mm.yy")) {
				lAux = "CONVERT(VARCHAR," + pField + ", 104)";
			}
			else if (pFormat.equalsIgnoreCase("dd-mm-yy")) {
				lAux = "CONVERT(VARCHAR," + pField + ", 105)";
			}
			else if (pFormat.equalsIgnoreCase("mm-dd-yy")) {
				lAux = "CONVERT(VARCHAR," + pField + ", 110)";
			}
			else if (pFormat.equalsIgnoreCase("yy/mm/dd")) {
				lAux = "CONVERT(VARCHAR," + pField + ", 111)";
			}
			else if (pFormat.equalsIgnoreCase("yymmdd") || pFormat.equalsIgnoreCase("yyyymmdd")) {
				lAux = "CONVERT(VARCHAR," + pField + ", 112)";
			}
			else {
				lAux = "CONVERT(VARCHAR," + pField + ", 103)";
			}
		}
		return lAux;
	}

	public String timeToChar(String pField) {
		String lAux = "to_char(" + pField + ", 'hh24:mi')";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = BuildSql.getHH24MItoChar(pField, true);
		}
		return lAux;
	}

	public String getActualDate(DateType pDateType) {
		return getActualDate(pDateType, getDateFormat());
	}

	public static String getActualDate(DateType pDateType, String pFormat) {

		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleFormat;

		String result = "";

		switch (pDateType) {
			case DATE:
				simpleFormat = new SimpleDateFormat(pFormat);
				result = ( simpleFormat.format(date).toString() );
				break;
			case DATE_TIME:
				simpleFormat = new SimpleDateFormat(pFormat + " HH:mm");
				result = ( simpleFormat.format(date).toString() );
				break;
			case YEAR:
				simpleFormat = new SimpleDateFormat("yyyy");
				result = ( simpleFormat.format(date).toString() );
				break;
			case TIME:
				DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR"));
				simpleFormat = new SimpleDateFormat("HH:mm");
				result = ( df.format(date).toString() + " - " + simpleFormat.format(date).toString() );
				break;
			case MONTH:
				simpleFormat = new SimpleDateFormat("MM");
				result = ( simpleFormat.format(date).toString() );
				break;
			case MONTH_YEAR:
				simpleFormat = new SimpleDateFormat("MM/yyyy");
				result = ( simpleFormat.format(date).toString() );
				break;

		}
		return result;
	}

	public String getActualMonthLastDay() {
		Calendar date = Calendar.getInstance();
		return getMonthLastDay(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
	}

	/* MESES {1..12} */
	public String getMonthLastDay(int pMonth, int pYear) {
		int days = Utils.diasPorMes(pMonth, pYear);

		Calendar date = Calendar.getInstance();
		date.set(pYear, pMonth - 1, days);

		SimpleDateFormat simpleFormat = new SimpleDateFormat(getDateFormat());
		return ( simpleFormat.format(date.getTime()).toString() );
	}

	public String getActualMonthFirstDay() {
		Calendar date = Calendar.getInstance();
		return getMonthFirstDay(date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR));
	}

	/* MESES {1..12} */
	public String getMonthFirstDay(int pMonth, int pYear) {
		Calendar date = Calendar.getInstance();
		date.set(pYear, pMonth - 1, 1);

		SimpleDateFormat simpleFormat = new SimpleDateFormat(getDateFormat());
		return ( simpleFormat.format(date.getTime()).toString() );
	}

	public Date parseDate(String pDate, DateType pDateType) {
		Date date = new Date(System.currentTimeMillis());
		try {
			switch (pDateType) {
				case DATE_TIME:
					date = ( new SimpleDateFormat(getDateFormat() + " HH:mm").parse(pDate) );
					break;
				case TIME:
					date = ( new SimpleDateFormat("HH:mm:ss").parse(pDate) );
					break;
				case MONTH_YEAR:
					date = ( new SimpleDateFormat(getDateFormat()).parse(pDate) );
					break;
				default:
					date = ( new SimpleDateFormat(getDateFormat()).parse(pDate) );
					break;
			}
		}
		catch (Exception e) {
			Utils.printSystemError("simpleFormat.parse :: " + pDateType, e.getMessage());
		}
		return date;
	}

	public String formatDate(Date pDate, DateType pDateType) {
		String date = "";
		switch (pDateType) {
			case DATE_TIME:
				date = ( new SimpleDateFormat(getDateFormat() + " HH:mm").format(pDate) );
				break;
			case TIME:
				date = ( new SimpleDateFormat("HH:mm:ss").format(pDate) );
				break;
			case MONTH_YEAR:
				date = ( new SimpleDateFormat(getDateFormat()).format(pDate) );
				break;
			default:
				date = ( new SimpleDateFormat(getDateFormat()).format(pDate) );
				break;
		}
		return date;
	}

	public int daysPerMonth(int pMonth, int pYear) {
		int[] dias = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (( ( pYear % 4 ) == 0 && ( pYear % 100 ) != 0 ) || ( pYear % 400 ) == 0) {
			dias[2] = 29;
		}
		return dias[pMonth];
	}

	public long dateDiff(String pDateIni, String pDateFim, int pTypeResult) {
		long diff = 0;
		Calendar dateIni = Calendar.getInstance();
		Calendar dateFim = Calendar.getInstance();

		SimpleDateFormat dateFormat = new SimpleDateFormat(getDateFormat());

		try {
			dateIni.setTime(dateFormat.parse(pDateIni));
			dateFim.setTime(dateFormat.parse(pDateFim));

			diff = dateFim.getTimeInMillis() - dateIni.getTimeInMillis();

			switch (pTypeResult) {
				case 0:
					diff = diff / ( 24 * 60 * 60 * 1000 ); // Dias
					break;
				case 1:
					diff = diff / ( 60 * 60 * 1000 ); // Horas
					break;
				case 2:
					diff = diff / ( 60 * 1000 ); // Minutos
					break;
				case 3:
					diff = diff / ( 1000 ); // Segundos
					break;
				case 4: {
					diff = diff / ( 360l * 24 * 60 * 60 * 1000 ); // Média de anos
					break;
				}
			}
		}
		catch (ParseException ex) {
			diff = -1;
		}

		return diff;
	}

	// ======================================================
	// ================ MANIPULACAO DE BANCO ================
	// ======================================================
	public static boolean isDBMS(String pTypeDBMS) {
		return getProperty(IniManipulation.DBMS).equalsIgnoreCase(pTypeDBMS) || ( getProperty(IniManipulation.DBMS).trim().equals("") && pTypeDBMS.equalsIgnoreCase(Database.DBMS_ORACLE) );
	}


	public Connection openConnection() {
		Connection conn = null;

		String dbPwd = getProperty(IniManipulation.DB_PWD);
		if (!dbPwd.equals("")) {
			if (dbPwd.startsWith("CRYPT_")) {
				dbPwd = Crypt.removeEncriptation(dbPwd.substring(6));
			}
			else {
				IniManipulation.setProperty(IniManipulation.DB_PWD, "CRYPT_" + Crypt.addEncriptation(dbPwd));
			}
		}

		try {
			
			jdbc:mysql://localhost:3306/sakila?profileSQL=true
			
			if (getProperty(IniManipulation.DB_POOL).trim().equals("")) {
				if (Database.isDBMS(Database.DBMS_MYSQL)) {
					String driverName = "com.mysql.jdbc.Driver";  
					Class.forName(driverName);
					conn = DriverManager.getConnection("jdbc:mysql://" + getProperty(IniManipulation.DB_SERVER) + ":" + getProperty(IniManipulation.DB_PORT) + "/" + getProperty(IniManipulation.DB_DATABASE) + "", getProperty(IniManipulation.DB_USER), dbPwd);
				}
				else if (Database.isDBMS(Database.DBMS_ORACLE)) {
					String driverName = "oracle.jdbc.driver.OracleDriver";
					Class.forName(driverName);

					String myUrl = "jdbc:oracle:thin:@" + getProperty(IniManipulation.DB_SERVER) + ":" + getProperty(IniManipulation.DB_PORT) + ":" + IniManipulation.getProperty(IniManipulation.DB_SID);
					if (!getProperty(IniManipulation.DB_SERVICE_NAME).equals("")) {
						myUrl = "jdbc:oracle:thin:@//" + getProperty(IniManipulation.DB_SERVER) + ":" + getProperty(IniManipulation.DB_PORT) + "/" + getProperty(IniManipulation.DB_SERVICE_NAME);
					}

					conn = DriverManager.getConnection(myUrl, IniManipulation.getProperty(IniManipulation.DB_USER), dbPwd);
				}
				else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
					String driverName = "net.sourceforge.jtds.jdbc.Driver";
					Class.forName(driverName);
					String myInstanceName = "";

					if (getProperty(IniManipulation.DB_INSTANCE).trim().length() > 0) {
						myInstanceName = ";instance=" + getProperty(IniManipulation.DB_INSTANCE);
					}
					String myUrl = "jdbc:jtds:sqlserver://" + getProperty(IniManipulation.DB_SERVER) + myInstanceName + ";DatabaseName=" + getProperty(IniManipulation.DB_DATABASE) + ";User=" + getProperty(IniManipulation.DB_USER) + ";Password=" + dbPwd; // ;namedPipe=true
					conn = DriverManager.getConnection(myUrl, getProperty(IniManipulation.DB_USER), dbPwd);
				}
			}
			else {
				// USANDO POOL
				// InitialContext intcxt = new InitialContext();
				// Context cxt = (Context) intcxt.lookup("java:comp/env");
				// if (cxt != null) {
				// Object oPool = cxt.lookup(getProperty(IniManipulation.DB_POOL));
				//
				// if (oPool != null) {
				// Method[] methods = oPool.getClass().getMethods();
				//
				// for (Method method : methods) {
				// if (method.getName().equalsIgnoreCase("getConnection") && method.getParameterTypes().length == 0) {
				// conn = (Connection) method.invoke(oPool, new Object[] {});
				// break;
				// }
				// }
				// }
				// }
				InitialContext cxt = new InitialContext();
				if (cxt != null) {
					Object oPool = cxt.lookup(getProperty(IniManipulation.DB_POOL));
					if (oPool != null) {
						Method[] methods = oPool.getClass().getMethods();
						Method method = null;
						for (int i = 0; i < methods.length; i++) {
							method = methods[i];
							if (method.getName().equalsIgnoreCase("getConnection")) {
								conn = (Connection) method.invoke(oPool, new Object[] {});
								break;
							}
						}
					}
				}

			}
		}
		catch (ClassNotFoundException e) {
			System.err.println(SystemConfig.getSystemName() + " (" + getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + ") -> openConnection():" + e.getMessage());
			mLastError = e.getMessage();
			e.printStackTrace();
			return null;
		}
		catch (SQLException e) {
			System.err.println(SystemConfig.getSystemName() + " (" + getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + ") -> openConnection():" + e.getMessage());
			mLastError = e.getMessage();
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			System.err.println(SystemConfig.getSystemName() + " (" + getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + ") -> openConnection():" + e.getMessage());
			mLastError = e.getMessage();
			e.printStackTrace(); 
			return null;
		}

		ConnectionISG connISG = new ConnectionISG() {
			Connection mConn;

			public void setActiveConnection(Connection pConn) {
				this.mConn = pConn;
			}

			public void addLog(String log, String date) {
				logList.add(new ItemLog(log, date));
			}

			public void clearLog() {
				logList.clear();
			}

			public void clearWarnings() throws SQLException {
				this.mConn.clearWarnings();
			}

			public void close() throws SQLException {
				this.mConn.close();
			}

			public void commit() throws SQLException {
				this.mConn.commit();
			}

			public Statement createStatement() throws SQLException {
				return this.mConn.createStatement();
			}

			public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
				return this.mConn.createStatement(resultSetType, resultSetConcurrency);
			}

			public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
				return this.mConn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
			}

			public boolean getAutoCommit() throws SQLException {
				return this.mConn.getAutoCommit();
			}

			public String getCatalog() throws SQLException {
				return this.mConn.getCatalog();
			}

			public int getHoldability() throws SQLException {
				return this.mConn.getHoldability();
			}

			public DatabaseMetaData getMetaData() throws SQLException {
				return this.mConn.getMetaData();
			}

			public int getTransactionIsolation() throws SQLException {
				return this.mConn.getTransactionIsolation();
			}

			public Map<String, Class<?>> getTypeMap() throws SQLException {
				return this.mConn.getTypeMap();
			}

			public SQLWarning getWarnings() throws SQLException {
				return this.mConn.getWarnings();
			}

			public boolean isClosed() throws SQLException {
				return this.mConn.isClosed();
			}

			public boolean isReadOnly() throws SQLException {
				return this.mConn.isReadOnly();
			}

			public String nativeSQL(String sql) throws SQLException {
				return this.mConn.nativeSQL(sql);
			}

			public CallableStatement prepareCall(String sql) throws SQLException {
				return this.mConn.prepareCall(sql);
			}

			public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
				return this.mConn.prepareCall(sql, resultSetType, resultSetConcurrency);
			}

			public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
				return this.mConn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			}

			public PreparedStatement prepareStatement(String sql) throws SQLException {
				return this.mConn.prepareStatement(sql);
			}

			public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
				return this.mConn.prepareStatement(sql, autoGeneratedKeys);
			}

			public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
				return this.mConn.prepareStatement(sql, columnIndexes);
			}

			public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
				return this.mConn.prepareStatement(sql, columnNames);
			}

			public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
				return this.mConn.prepareStatement(sql, resultSetType, resultSetConcurrency);
			}

			public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
				return this.mConn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			}

			public void releaseSavepoint(Savepoint savepoint) throws SQLException {
				this.mConn.releaseSavepoint(savepoint);
			}

			public void rollback() throws SQLException {
				this.mConn.rollback();
			}

			public void rollback(Savepoint savepoint) throws SQLException {
				this.mConn.rollback(savepoint);
			}

			public void setAutoCommit(boolean autoCommit) throws SQLException {
				this.mConn.setAutoCommit(autoCommit);
			}

			public void setCatalog(String catalog) throws SQLException {
				this.mConn.setCatalog(catalog);
			}

			public void setHoldability(int holdability) throws SQLException {
				this.mConn.setHoldability(holdability);
			}

			public void setReadOnly(boolean readOnly) throws SQLException {
				this.mConn.setReadOnly(readOnly);
			}

			public Savepoint setSavepoint() throws SQLException {
				return this.mConn.setSavepoint();
			}

			public Savepoint setSavepoint(String name) throws SQLException {
				return this.mConn.setSavepoint(name);
			}

			public void setTransactionIsolation(int level) throws SQLException {
				this.mConn.setTransactionIsolation(level);
			}

			public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
				this.mConn.setTypeMap(map);
			}

			public void abort(Executor executor) throws SQLException {
				this.mConn.abort(executor);
			}

			public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
				return this.mConn.createArrayOf(typeName, elements);
			}

			public Blob createBlob() throws SQLException {
				return this.mConn.createBlob();
			}

			public Clob createClob() throws SQLException {
				return this.mConn.createClob();
			}

			public NClob createNClob() throws SQLException {
				return this.mConn.createNClob();
			}

			public SQLXML createSQLXML() throws SQLException {
				return this.mConn.createSQLXML();
			}

			public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
				return this.mConn.createStruct(typeName, attributes);
			}

			public Properties getClientInfo() throws SQLException {
				return this.mConn.getClientInfo();
			}

			public String getClientInfo(String name) throws SQLException {
				return this.mConn.getClientInfo(name);
			}

			public int getNetworkTimeout() throws SQLException {
				return this.mConn.getNetworkTimeout();
			}

			public String getSchema() throws SQLException {
				return this.mConn.getSchema();
			}

			public boolean isValid(int timeout) throws SQLException {
				return this.mConn.isValid(timeout);
			}

			public void setClientInfo(Properties properties) throws SQLClientInfoException {
				this.mConn.setClientInfo(properties);
			}

			public void setClientInfo(String name, String value) throws SQLClientInfoException {
				this.mConn.setClientInfo(name, value);
			}

			public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
				this.mConn.setNetworkTimeout(executor, milliseconds);
			}

			public void setSchema(String schema) throws SQLException {
				this.mConn.setSchema(schema);
			}

			public boolean isWrapperFor(Class<?> iface) throws SQLException {
				return this.mConn.isWrapperFor(iface);
			}

			@SuppressWarnings( { "rawtypes", "unchecked" } )
			public Object unwrap(Class iface) throws SQLException {
				return this.mConn.unwrap(iface);
			}
		};

		connISG.setActiveConnection(conn);
		return connISG;
	}
	
	
	public Connection openConnectionFACAPARTE() {
		Connection conn = null;

		String dbPwd = getProperty("PWD_FACAPARTE");
		if (!dbPwd.equals("")) {
			if (dbPwd.startsWith("CRYPT_")) {
				dbPwd = Crypt.removeEncriptation(dbPwd.substring(6));
			}
			else {
				IniManipulation.setProperty("PWD_FACAPARTE", "CRYPT_" + Crypt.addEncriptation(dbPwd));
			}
		}

		try {
			
			jdbc:mysql://localhost:3306/sakila?profileSQL=true
			
			if (getProperty(IniManipulation.DB_POOL).trim().equals("")) {
				if (Database.isDBMS(Database.DBMS_MYSQL)) {
					String driverName = "com.mysql.jdbc.Driver";  
					Class.forName(driverName);
					conn = DriverManager.getConnection("jdbc:mysql://" + getProperty(IniManipulation.DB_SERVER) + ":" + getProperty(IniManipulation.DB_PORT) + "/" + getProperty("DATABASE_FACAPARTE") + "", getProperty("USER_FACAPARTE"), dbPwd);
				}
				else if (Database.isDBMS(Database.DBMS_ORACLE)) {
					String driverName = "oracle.jdbc.driver.OracleDriver";
					Class.forName(driverName);

					String myUrl = "jdbc:oracle:thin:@" + getProperty(IniManipulation.DB_SERVER) + ":" + getProperty(IniManipulation.DB_PORT) + ":" + IniManipulation.getProperty(IniManipulation.DB_SID);
					if (!getProperty(IniManipulation.DB_SERVICE_NAME).equals("")) {
						myUrl = "jdbc:oracle:thin:@//" + getProperty(IniManipulation.DB_SERVER) + ":" + getProperty(IniManipulation.DB_PORT) + "/" + getProperty(IniManipulation.DB_SERVICE_NAME);
					}

					conn = DriverManager.getConnection(myUrl, IniManipulation.getProperty(IniManipulation.DB_USER), dbPwd);
				}
				else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
					String driverName = "net.sourceforge.jtds.jdbc.Driver";
					Class.forName(driverName);
					String myInstanceName = "";

					if (getProperty(IniManipulation.DB_INSTANCE).trim().length() > 0) {
						myInstanceName = ";instance=" + getProperty(IniManipulation.DB_INSTANCE);
					}
					String myUrl = "jdbc:jtds:sqlserver://" + getProperty(IniManipulation.DB_SERVER) + myInstanceName + ";DatabaseName=" + getProperty(IniManipulation.DB_DATABASE) + ";User=" + getProperty(IniManipulation.DB_USER) + ";Password=" + dbPwd; // ;namedPipe=true
					conn = DriverManager.getConnection(myUrl, getProperty(IniManipulation.DB_USER), dbPwd);
				}
			}
			else {
				// USANDO POOL
				// InitialContext intcxt = new InitialContext();
				// Context cxt = (Context) intcxt.lookup("java:comp/env");
				// if (cxt != null) {
				// Object oPool = cxt.lookup(getProperty(IniManipulation.DB_POOL));
				//
				// if (oPool != null) {
				// Method[] methods = oPool.getClass().getMethods();
				//
				// for (Method method : methods) {
				// if (method.getName().equalsIgnoreCase("getConnection") && method.getParameterTypes().length == 0) {
				// conn = (Connection) method.invoke(oPool, new Object[] {});
				// break;
				// }
				// }
				// }
				// }
				InitialContext cxt = new InitialContext();
				if (cxt != null) {
					Object oPool = cxt.lookup(getProperty(IniManipulation.DB_POOL));
					if (oPool != null) {
						Method[] methods = oPool.getClass().getMethods();
						Method method = null;
						for (int i = 0; i < methods.length; i++) {
							method = methods[i];
							if (method.getName().equalsIgnoreCase("getConnection")) {
								conn = (Connection) method.invoke(oPool, new Object[] {});
								break;
							}
						}
					}
				}

			}
		}
		catch (ClassNotFoundException e) {
			System.err.println(SystemConfig.getSystemName() + " (" + getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + ") -> openConnection():" + e.getMessage());
			mLastError = e.getMessage();
			e.printStackTrace();
			return null;
		}
		catch (SQLException e) {
			System.err.println(SystemConfig.getSystemName() + " (" + getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + ") -> openConnection():" + e.getMessage());
			mLastError = e.getMessage();
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			System.err.println(SystemConfig.getSystemName() + " (" + getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + ") -> openConnection():" + e.getMessage());
			mLastError = e.getMessage();
			e.printStackTrace(); 
			return null;
		}

		ConnectionISG connISG = new ConnectionISG() {
			Connection mConn;

			public void setActiveConnection(Connection pConn) {
				this.mConn = pConn;
			}

			public void addLog(String log, String date) {
				logList.add(new ItemLog(log, date));
			}

			public void clearLog() {
				logList.clear();
			}

			public void clearWarnings() throws SQLException {
				this.mConn.clearWarnings();
			}

			public void close() throws SQLException {
				this.mConn.close();
			}

			public void commit() throws SQLException {
				this.mConn.commit();
			}

			public Statement createStatement() throws SQLException {
				return this.mConn.createStatement();
			}

			public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
				return this.mConn.createStatement(resultSetType, resultSetConcurrency);
			}

			public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
				return this.mConn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
			}

			public boolean getAutoCommit() throws SQLException {
				return this.mConn.getAutoCommit();
			}

			public String getCatalog() throws SQLException {
				return this.mConn.getCatalog();
			}

			public int getHoldability() throws SQLException {
				return this.mConn.getHoldability();
			}

			public DatabaseMetaData getMetaData() throws SQLException {
				return this.mConn.getMetaData();
			}

			public int getTransactionIsolation() throws SQLException {
				return this.mConn.getTransactionIsolation();
			}

			public Map<String, Class<?>> getTypeMap() throws SQLException {
				return this.mConn.getTypeMap();
			}

			public SQLWarning getWarnings() throws SQLException {
				return this.mConn.getWarnings();
			}

			public boolean isClosed() throws SQLException {
				return this.mConn.isClosed();
			}

			public boolean isReadOnly() throws SQLException {
				return this.mConn.isReadOnly();
			}

			public String nativeSQL(String sql) throws SQLException {
				return this.mConn.nativeSQL(sql);
			}

			public CallableStatement prepareCall(String sql) throws SQLException {
				return this.mConn.prepareCall(sql);
			}

			public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
				return this.mConn.prepareCall(sql, resultSetType, resultSetConcurrency);
			}

			public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
				return this.mConn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			}

			public PreparedStatement prepareStatement(String sql) throws SQLException {
				return this.mConn.prepareStatement(sql);
			}

			public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
				return this.mConn.prepareStatement(sql, autoGeneratedKeys);
			}

			public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
				return this.mConn.prepareStatement(sql, columnIndexes);
			}

			public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
				return this.mConn.prepareStatement(sql, columnNames);
			}

			public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
				return this.mConn.prepareStatement(sql, resultSetType, resultSetConcurrency);
			}

			public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
				return this.mConn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			}

			public void releaseSavepoint(Savepoint savepoint) throws SQLException {
				this.mConn.releaseSavepoint(savepoint);
			}

			public void rollback() throws SQLException {
				this.mConn.rollback();
			}

			public void rollback(Savepoint savepoint) throws SQLException {
				this.mConn.rollback(savepoint);
			}

			public void setAutoCommit(boolean autoCommit) throws SQLException {
				this.mConn.setAutoCommit(autoCommit);
			}

			public void setCatalog(String catalog) throws SQLException {
				this.mConn.setCatalog(catalog);
			}

			public void setHoldability(int holdability) throws SQLException {
				this.mConn.setHoldability(holdability);
			}

			public void setReadOnly(boolean readOnly) throws SQLException {
				this.mConn.setReadOnly(readOnly);
			}

			public Savepoint setSavepoint() throws SQLException {
				return this.mConn.setSavepoint();
			}

			public Savepoint setSavepoint(String name) throws SQLException {
				return this.mConn.setSavepoint(name);
			}

			public void setTransactionIsolation(int level) throws SQLException {
				this.mConn.setTransactionIsolation(level);
			}

			public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
				this.mConn.setTypeMap(map);
			}

			public void abort(Executor executor) throws SQLException {
				this.mConn.abort(executor);
			}

			public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
				return this.mConn.createArrayOf(typeName, elements);
			}

			public Blob createBlob() throws SQLException {
				return this.mConn.createBlob();
			}

			public Clob createClob() throws SQLException {
				return this.mConn.createClob();
			}

			public NClob createNClob() throws SQLException {
				return this.mConn.createNClob();
			}

			public SQLXML createSQLXML() throws SQLException {
				return this.mConn.createSQLXML();
			}

			public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
				return this.mConn.createStruct(typeName, attributes);
			}

			public Properties getClientInfo() throws SQLException {
				return this.mConn.getClientInfo();
			}

			public String getClientInfo(String name) throws SQLException {
				return this.mConn.getClientInfo(name);
			}

			public int getNetworkTimeout() throws SQLException {
				return this.mConn.getNetworkTimeout();
			}

			public String getSchema() throws SQLException {
				return this.mConn.getSchema();
			}

			public boolean isValid(int timeout) throws SQLException {
				return this.mConn.isValid(timeout);
			}

			public void setClientInfo(Properties properties) throws SQLClientInfoException {
				this.mConn.setClientInfo(properties);
			}

			public void setClientInfo(String name, String value) throws SQLClientInfoException {
				this.mConn.setClientInfo(name, value);
			}

			public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
				this.mConn.setNetworkTimeout(executor, milliseconds);
			}

			public void setSchema(String schema) throws SQLException {
				this.mConn.setSchema(schema);
			}

			public boolean isWrapperFor(Class<?> iface) throws SQLException {
				return this.mConn.isWrapperFor(iface);
			}

			@SuppressWarnings( { "rawtypes", "unchecked" } )
			public Object unwrap(Class iface) throws SQLException {
				return this.mConn.unwrap(iface);
			}
		};

		connISG.setActiveConnection(conn);
		return connISG;
	}


	public Connection openConnection(String pDBMS, String pDB_USER, String pDB_PWD, String pDB_SERVER, String pDB_PORT, String pDB_DATABASE, String pDB_SID) {
		Connection conn = null;
		String pDB_INSTANCE = pDB_SID;
		try {
			if (pDBMS.equals("P")) { // POSTGRE
				String driverName = "org.postgresql.Driver";
				Class.forName(driverName);
				conn = DriverManager.getConnection("jdbc:postgresql://" + pDB_SERVER + ":" + pDB_PORT + "/" + pDB_DATABASE + "", pDB_USER, pDB_PWD);
			}
			else if (pDBMS.equals("O")) { // ORACLE
				String driverName = "oracle.jdbc.driver.OracleDriver";
				Class.forName(driverName);
				String myUrl = "jdbc:oracle:thin:@" + pDB_SERVER + ":" + pDB_PORT + ":" + pDB_SID; // + "?CHARSET=WE8ISO8859P1";
				conn = DriverManager.getConnection(myUrl, pDB_USER, pDB_PWD);
			}
			else if (pDBMS.equals("S")) { // SQLSERVER
				String driverName = "net.sourceforge.jtds.jdbc.Driver";
				Class.forName(driverName);
				String myInstanceName = "";

				if (pDB_INSTANCE.trim().length() > 0) {
					myInstanceName = ";instance=" + pDB_INSTANCE;
				}
				String myUrl = "jdbc:jtds:sqlserver://" + pDB_SERVER + myInstanceName + ";DatabaseName=" + pDB_DATABASE + ";User=" + pDB_USER + ";Password=" + pDB_PWD; // ;namedPipe=true
				conn = DriverManager.getConnection(myUrl, pDB_USER, pDB_PWD);
			}

		}
		catch (ClassNotFoundException e) {
			System.err.println(SystemConfig.getSystemName() + " (" + getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + ") -> openConnection():" + e.getMessage());
			mLastError = e.getMessage();
			return null;
		}
		catch (SQLException e) {
			System.err.println(SystemConfig.getSystemName() + " (" + getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + ") -> openConnection():" + e.getMessage());
			mLastError = e.getMessage();
			return null;
		}
		catch (Exception e) {
			System.err.println(SystemConfig.getSystemName() + " (" + getActualDate(DateType.DATE_TIME, "dd/MM/yyyy") + ") -> openConnection():" + e.getMessage());
			mLastError = e.getMessage();
			return null;
		}
		return conn;
	}

	public static Statement openStatement(Connection pConn) {
		return openStatement(pConn, false);
	}

	public static Statement openStatement(Connection pConn, boolean pForResultSet) {
		try {
			if (pForResultSet) {
				return pConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			}
			else {
				return pConn.createStatement();
			}
		}
		catch (Exception e) {
			return null;
		}
	}

	public ResultSet openResultSet(String pSQL, Connection pConn) {
		return openResultSet(pSQL, pConn, openStatement(pConn, true));
	}

	public ResultSet openResultSet(String pSQL, Connection pConn, Statement pStmt) {
		ResultSet rs = null;
		Writer result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);
		String lError = "";
		mLastError = "";

		try {
			rs = pStmt.executeQuery(pSQL);
		}
		catch (SQLException e) {
			lError = "Sistema: " + SystemConfig.getSystemName() + " -> OpenResultSet(" + pSQL + "):" + e.getMessage();
			System.err.println("Sistema: " + SystemConfig.getSystemName() + " -> OpenResultSet(" + pSQL + "):" + e.getMessage());
			mLastError = "Sistema: " + SystemConfig.getSystemName() + " -> OpenResultSet(" + pSQL + "):" + e.getMessage();
			e.printStackTrace(printWriter);
		}
		catch (Exception e) {
			lError = "Sistema: " + SystemConfig.getSystemName() + " -> OpenResultSet(" + pSQL + "): " + e.getMessage();
			System.err.println("Sistema: " + SystemConfig.getSystemName() + " -> OpenResultSet(" + pSQL + "): " + e.getMessage());
			mLastError = "Sistema: " + SystemConfig.getSystemName() + " -> OpenResultSet(" + pSQL + "):" + e.getMessage();
			e.printStackTrace(printWriter);
		}

		// MANDA EMAIL CASO TENHA CONFIGURADO O LOG DE ERROS
		if (!lError.equals("") && !getProperty(IniManipulation.MAIL_LOG_DB).equals("")) {
			Mail lMail = new Mail(mUser);
			String lAcionado = "";

			try {
				lAcionado = "Erro acionado a partir de: " + Utils.callerClass() + "." + Utils.callerMethod() + "<br><br>";
			}
			catch (Exception ex) {
			}

			try {
				lMail.postMail(getProperty(IniManipulation.MAIL_LOG_DB), lAcionado + lError + "<hr>" + result.toString(), SystemConfig.getSystemName() + " :: LOG EXECUTEQUERY", "", SystemConfig.getSystemName(), true);
			}
			catch (MessagingException ex) {
			}
		}

		return rs;
	}

	public boolean isAutoCommit(Connection pConn) {
		try {
			return pConn.getAutoCommit();
		}
		catch (Exception ex) {
			return false;
		}
	}

	public boolean setAutoCommit(Connection pConn, boolean pAutoCommit) {
		try {
			pConn.setAutoCommit(pAutoCommit);
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}

	public boolean commit(Connection pConn) {
		try {
			pConn.commit();
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}

	public boolean rollback(Connection pConn) {
		try {
			pConn.rollback();
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}

	public static void closeObject(Connection pConn) {
		try {
			pConn.close();
		}
		catch (Exception ex) {
		}
	}

	public static void closeObject(ResultSet pResultSet) {
		try {
			pResultSet.getStatement().close();
		}
		catch (Exception ex) {
		}
		try {
			pResultSet.close();
		}
		catch (Exception ex) {
		}
	}

	public boolean executeQuery(String pSQL, Connection pConn) throws AutoException {
		Statement stmt = null;
		boolean lReturn = false;
		mLastError = "";
		Writer result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);

		try {
			stmt = pConn.createStatement();
			lReturn = stmt.executeUpdate(pSQL) > -1;
			stmt.close();
			stmt = null;
		}
		catch (SQLException e) {
			lReturn = false;
			System.err.println("Sistema: " + SystemConfig.getSystemName() + " -> ExecuteQuery(" + pSQL + "): " + e.getMessage());
			mLastError = "Sistema: " + SystemConfig.getSystemName() + " -> ExecuteQuery(" + pSQL + "):" + e.getMessage();
			e.printStackTrace(printWriter);
		}
		catch (Exception e) {
			lReturn = false;
			System.err.println("Sistema: " + SystemConfig.getSystemName() + " -> ExecuteQuery(" + pSQL + "): " + e.getMessage());
			mLastError = "Sistema: " + SystemConfig.getSystemName() + " -> ExecuteQuery(" + pSQL + "):" + e.getMessage();
			e.printStackTrace(printWriter);
		}

		// MANDA EMAIL CASO TENHA CONFIGURADO O LOG DE ERROS
		if (!lReturn) {
			if (!getProperty(IniManipulation.MAIL_LOG_DB).equals("")) {
				Mail lMail = new Mail(mUser);
				String lAcionado = "";

				try {
					lAcionado = "Erro acionado a partir de: " + Utils.callerClass() + "." + Utils.callerMethod() + "<br><br>";
				}
				catch (Exception ex) {
				}

				try {
					lMail.postMail(getProperty(IniManipulation.MAIL_LOG_DB), lAcionado + mLastError + "<hr>" + result.toString(), SystemConfig.getSystemName() + " :: LOG EXECUTEQUERY", "", SystemConfig.getSystemName(), true);
				}
				catch (MessagingException ex) {
				}
			}
			throw new AutoException(mUser, "Erro: " + mLastError);
		}

		// Gravando LOG
		try {
			if (pConn.getAutoCommit() == false) {
				stmt = pConn.createStatement();
				( (ConnectionISG) pConn ).addLog(pSQL + ( mLastError.equals("") ? "" : " ERROR: " + mLastError ), getActualDate(DateType.DATE_TIME));
			}
		}
		catch (SQLException ex1) {
		}

		return lReturn;
	}

	public ResultSet getTablePrimaries(Connection pConn, String pTableName) {
		ResultSet pk = null;
		try {
			pk = openResultSet(BuildSql.getTablePrimaries(pTableName), pConn, pConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));;
		}
		catch (Exception e) {
			System.err.println("ERRO : CDatabase.getTablePrimaries : " + e.getMessage());
		}
		return pk;
	}

	public ResultSet getTableColumnNames(Connection pConn, String pTableName) {
		ResultSet names = null;
		try {
			names = openResultSet(BuildSql.getColumnNamesTypes(pTableName), pConn, pConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
		}
		catch (Exception e) {
			System.err.println("ERRO : CDatabase.getTableColumnsNames : " + e.getMessage());
		}
		return names;
	}

	public static String removeFieldsFromSQL(String pSQL) {
		return pSQL.substring(pSQL.toUpperCase().indexOf("FROM"));
	}

	public long getRecordCount(String pSQL) {
		Connection conn = ( new Database(this.getUser()) ).openConnection();
		long count = 0;

		try {
			count = getRecordCount(pSQL, conn);
		}
		catch (Exception e) {
			count = 0;
		}
		finally {
			Database.closeObject(conn);
		}

		return count;
	}

	public long getRecordCount(String pSQL, Connection pConn) {
		long count;
		String lSQL = "SELECT COUNT(*) FROM (" + pSQL + ") " + BuildSql.getAsSubQuery();
		ResultSet rs = openResultSet(lSQL, pConn);
		try {
			if (rs.next()) {
				count = Integer.parseInt("0" + verifyNull(rs.getObject(1)));
			}
			else {
				count = 0;
			}
		}
		catch (SQLException e) {
			count = 0;
		}
		finally {
			Database.closeObject(rs);
		}

		return count;
	}

	public long countDB(Connection pConn, String pTable, String pFilter) {
		Connection conn;
		ResultSet rmax;
		String lSQL;
		long lReturn;
		lSQL = "SELECT COUNT(*) FROM " + pTable;

		if (!pFilter.equals("")) {
			lSQL += " WHERE " + pFilter;
		}

		try {

			conn = pConn;
			rmax = openResultSet(lSQL, conn);

			if (rmax.next()) {
				lReturn = verifyNullLong(rmax.getObject(1));
				rmax.close();

			}
			else {
				lReturn = 0;
			}
		}
		catch (SQLException e) {
			lReturn = 0;
		}

		return lReturn;
	}

	public long maxDB(String pTable, String pField, String pFilter, Connection pConn) {
		return maxDB(pTable, pField, pFilter, pConn, 1);
	}

	public long maxDB(String pTable, String pField, String pFilter, Connection pConn, long pInit) {
		long result;
		String lSQL = "SELECT MAX(" + pField + ") FROM " + pTable;
		if (!pFilter.equals("")) {
			lSQL += " WHERE " + pFilter;
		}

		ResultSet rmax = openResultSet(lSQL, pConn);
		try {
			if (rmax.next()) {
				result = rmax.getLong(1) + 1;
			}
			else {
				result = pInit;
			}
		}
		catch (SQLException e) {
			result = pInit;
		}
		finally {
			Database.closeObject(rmax);
		}

		return result;
	}

	public String valorDB(String pTable, String pField, String pFilter, Connection pConn) {
		String lSQL = "SELECT " + pField + " FROM " + pTable;
		if (!pFilter.equals("")) {
			lSQL += " WHERE " + pFilter;
		}
		return verifyNull(valorDB(lSQL, pConn));
	}

	public Object valorDB(String pSQL, Connection pConn) {
		Object result;
		ResultSet rs = openResultSet(pSQL, pConn);
		try {
			if (rs.next()) {
				result = rs.getObject(1);
			}
			else {
				result = null;
			}
		}
		catch (Exception e) {
			result = null;
		}
		finally {
			closeObject(rs);
		}
		return result;
	}

	public String[] valoresDB(String pTable, String[] pFields, String pFilter, Connection pConn) {
		String[] result = new String[pFields.length];
		String lSQL = "SELECT ";
		for (int i = 0; i < pFields.length; i++) {
			lSQL += pFields[i];
			if (i < pFields.length - 1) {
				lSQL += ", ";
			}
		}
		lSQL += " FROM " + pTable;
		if (!pFilter.equals("")) {
			lSQL += " WHERE " + pFilter;
		}

		ResultSet rs = openResultSet(lSQL, pConn);
		try {
			if (rs.next()) {
				for (int i = 0; i < pFields.length; i++) {
					result[i] = verifyNull(rs.getObject(i + 1));
				}
			}
			else {
				result = null;
			}
		}
		catch (Exception e) {
			result = null;
		}
		finally {
			closeObject(rs);
		}

		return result;
	}

	public boolean updateCLOB(String pTable, String pField, String pFilter, String pStringClob, Connection pConn) {
		return updateCLOB(pTable, pField, pFilter, pStringClob, pConn, false);
	}

	public boolean updateCLOB(String pTable, String pField, String pFilter, String pStringCLOB, Connection pConn, boolean pUseEscapeHTML) {
		boolean result = false;

		String sql;
		int maxSize = 2000;
		String originalField = pField;

		if (pUseEscapeHTML && pStringCLOB != null) {
			String regex = "(<[^ \r\n\t])";
			Pattern p = Pattern.compile(regex);
			Matcher matcher = p.matcher(pStringCLOB);
			while (matcher.find()) {
				int ini = matcher.start();
				int fim = matcher.end();
				if (ini > -1 && fim > -1) {
					pStringCLOB = pStringCLOB.substring(0, ini + 1) + " " + pStringCLOB.substring(fim - 1);
				}
				matcher = p.matcher(pStringCLOB);
			}
		}

		// LIMPANDO CAMPO
		sql = "UPDATE " + pTable + " SET " + originalField + " = NULL ";
		if (!pFilter.equals("")) {
			sql += " WHERE " + pFilter;
		}
		try {
			executeQuery(sql, pConn);
			result = true;
		}
		catch (AutoException ex) {
			result = false;
		}

		int pos = 0;
		String aux = "";

		if (pStringCLOB != null && result) {
			if (mDBMS.equals(DBMS_SQLSERVER)) {

				sql = "UPDATE " + pTable + " SET " + originalField + " =  ? ";
				if (!pFilter.equals("")) {
					sql += " WHERE " + pFilter;
				}
				try {
					PreparedStatement ps = pConn.prepareStatement(sql);
					ps.setString(1, pStringCLOB);
					ps.executeUpdate();
					result = true;
					ps.close();
				}
				catch (Exception ex) {
					result = false;
				}

			}
			else {
				if (pStringCLOB.length() > pos + maxSize) {
					aux = pStringCLOB.substring(pos, pos + maxSize);
				}
				else {
					aux = pStringCLOB.substring(pos);
				}

				while (aux.length() > 0 && result) {
					sql = "UPDATE " + pTable + " SET " + originalField + " = ";
					if (mDBMS.equals(DBMS_ORACLE)) {
						sql += BuildSql.getConcat(BuildSql.getNvl(pField, "TO_CLOB('')"), "TO_CLOB(" + this.verifyInsertNull(aux, DataType.CLOB, false) + ")");
					}
					else {
						sql += BuildSql.getConcat(BuildSql.getNvl(pField, "''"), this.verifyInsertNull(aux, DataType.CLOB, false));
					}
					if (!pFilter.equals("")) {
						sql += " WHERE " + pFilter;
					}
					try {
						executeQuery(sql, pConn);
						result = true;
					}
					catch (AutoException ex) {
						result = false;
					}
					pos = pos + maxSize;
					if (pStringCLOB.length() > pos + maxSize) {
						aux = pStringCLOB.substring(pos, pos + maxSize);
					}
					else if (pStringCLOB.length() > pos) {
						aux = pStringCLOB.substring(pos);
					}
					else {
						aux = "";
					}
				}
			}
		}
		return result;
	}

	// ======================================================
	// ================= VALIDACAO DE DADOS =================
	// ======================================================
	public static String validateMessage(String pBaseMessage, String pFieldReplace) {
		return pBaseMessage.replaceAll("xxx", pFieldReplace);
	}

	public static String verifyNull(Object pValue) {
		return verifyNull(pValue, "");
	}

	public static String verifyNull(Object pValue, String pDefaultValue) {
		String a = null;

		try {
			if (pValue == null) {
				a = "";
			}
			else {
				if (pValue.getClass().getName().equalsIgnoreCase("oracle.sql.clob")) {
					a = Database.verifyNullCLOB(pValue);
				}
				else if (pValue.getClass().getName().equalsIgnoreCase("net.sourceforge.jtds.jdbc.ClobImpl")) {
					a = Database.verifyNullCLOB(pValue);
				}
				else {
					a = pValue.toString();
				}
			}
		}
		catch (Exception e) {
			a = "";
		}

		if (a == null || a.equals("")) {
			a = pDefaultValue;
		}

		return a;
	}

	public static String[] verifyNullArray(String[] pValue) {
		String[] retorno;
		if (pValue != null) {
			retorno = new String[pValue.length];

			for (int x = 0; x < retorno.length; x++) {
				retorno[x] = verifyNull(pValue[x]);
			}
		}
		else {
			retorno = new String[0];
		}
		return retorno;
	}

	public static String verifyNullCLOB(Object pValue) {
		String a = "";

		if (pValue.getClass().getName().equalsIgnoreCase("oracle.sql.clob")) {
			Clob lClob = (Clob) pValue;
			try {
				if (lClob != null) {
					a = lClob.getSubString(1, Integer.parseInt(String.valueOf(lClob.length())));
				}
			}
			catch (SQLException ex) {
				a = "";
			}
		}
		else if (pValue.getClass().getName().equalsIgnoreCase("net.sourceforge.jtds.jdbc.ClobImpl")) {
			ClobImpl lClob = (ClobImpl) pValue;
			try {
				if (lClob != null) {
					a = lClob.getSubString(1, Integer.parseInt(String.valueOf(lClob.length())));
				}
			}
			catch (SQLException ex) {
				a = "";
			}
		}
		else {
			a = verifyNull(pValue);
		}

		return a;
	}

	public static double verifyNullDouble(Object pValue) {
		return verifyNullDouble(pValue, -1);
	}

	public static double verifyNullDouble(Object pValue, int pCasasDecimais) {
		double a = 0;

		// Corrigindo bug do retorno request com erro.. tratando o retorno como se fosse valor do banco.
		String lRet = "";
		lRet = verifyNull(pValue);
		lRet = Database.convertAnyDecimalToDefault(lRet);

		if (pCasasDecimais >= 0) {
			String lVet = verifyNull(lRet);
			if (lVet.indexOf(".") >= 0) {
				lRet = lVet.split("\\.")[0];
				lRet += "." + lVet.split("\\.")[1].substring(0, ( lVet.split("\\.")[1].length() < pCasasDecimais ? lVet.split("\\.")[1].length() : pCasasDecimais ));
			}
			else {
				lRet = lVet;
			}
		}

		try {
			if (lRet == "") {
				a = 0;
			}
			else {
				a = Double.parseDouble(lRet.toString());
			}
		}
		catch (Exception e) {
			a = 0;
		}

		return a;
	}

	public static float verifyNullFloat(Object pValue) {
		return verifyNullFloat(pValue, -1);
	}

	public static float verifyNullFloat(Object pValue, int pCasasDecimais) {
		float a = 0;

		// CORRIGINDO BUG DO RETORNO REQUEST COM ERRO
		// TRATANDO O RETORNO COMO SE FOSSE VALOR DO BANCO
		String lRet = "";
		lRet = verifyNull(pValue);
		lRet = convertAnyDecimalToDefault(lRet);

		if (pCasasDecimais >= 0) {
			String lVet = verifyNull(lRet);
			if (lVet.indexOf(".") >= 0) {
				lRet = lVet.split("\\.")[0];
				lRet += "." + lVet.split("\\.")[1].substring(0, ( lVet.split("\\.")[1].length() < pCasasDecimais ? lVet.split("\\.")[1].length() : pCasasDecimais ));
			}
			else {
				lRet = lVet;
			}
		}

		try {
			if (lRet == "") {
				a = 0;
			}
			else {
				a = Float.parseFloat(lRet.toString());
			}
		}
		catch (Exception e) {
			a = 0;
		}

		return a;
	}

	public static int verifyNullInt(Object pValue) {
		return verifyNullInt(pValue, 0);
	}

	public static int verifyNullInt(Object pValue, int pDefaultValue) {
		int a = pDefaultValue;

		try {
			if (pValue != null) {
				a = Integer.parseInt(pValue.toString());
			}
		}
		catch (Exception e) {
			a = pDefaultValue;
		}
		return a;
	}

	public static long verifyNullLong(Object pValue) {
		long a = 0;
		try {
			if (pValue == null) {
				a = 0;
			}
			else {
				a = Long.parseLong(pValue.toString());
			}
		}
		catch (Exception e) {
			a = 0;
		}
		return a;
	}

	public String verifyInsertNull(Object pValue, DataType pType) {
		return verifyInsertNull(pValue, pType, true);
	}

	public String verifyInsertNull(Object pValue, DataType pType, boolean pUseTrim) {
		String a = null;
		SimpleDateFormat ft;
		Date lDate = new Date(System.currentTimeMillis());

		String lFormat = "dd/MM/yyyy";

		if (pValue == null || ( pUseTrim ? pValue.toString().trim().equals("") : pValue.toString().equals("") )) {
			a = "null";
		}
		else {
			switch (pType) {
				case DECIMAL:
				case INTEGER:
					// a = pValue.toString().replaceAll(",", ".");
					a = pValue.toString();

					String[] vet = a.split("\\.");
					String pre = "";
					int i = 0;
					while (i < vet.length - 1) {
						pre += vet[i];
						i++;
					}
					if (!pre.equals(""))
						a = pre + "." + vet[vet.length - 1];

					break;
				case DATE:
					ft = new SimpleDateFormat(lFormat);
					try {
						lDate = ft.parse(pValue.toString());
					}
					catch (ParseException e) {
					}

					if (this.mDBMS.equalsIgnoreCase(Database.DBMS_MYSQL)) {
						ft = new SimpleDateFormat("yyyy-MM-dd");
						a = "'" + ft.format(lDate) + "'";
						ft = new SimpleDateFormat(lFormat);
					}
					else if (this.mDBMS.equalsIgnoreCase(Database.DBMS_ORACLE)) {
						ft = new SimpleDateFormat(lFormat);
						a = "TO_DATE('" + ft.format(lDate) + "', '" + lFormat + "')";
					}
					else if (this.mDBMS.equalsIgnoreCase(Database.DBMS_SQLSERVER)) {
						Calendar calend = Calendar.getInstance();
						calend.setTime(lDate);
						int lAno = calend.get(Calendar.YEAR);

						// VERIFICA ANO DA DATA (SQL SERVER NÃO ACEITA ANO MENOR QUE 1753)
						if (lAno < 1753) {
							calend.set(1753, calend.get(Calendar.MONTH), calend.get(Calendar.DATE), calend.get(Calendar.HOUR_OF_DAY), calend.get(Calendar.MINUTE), calend.get(Calendar.SECOND));
							lDate = calend.getTime();
						}

						ft = new SimpleDateFormat("yyyy-MM-dd");
						a = "CONVERT(datetime,'" + ft.format(lDate) + "', 120)";
					}

					break;
				case DATE_TIME: // Date and Time
					ft = new SimpleDateFormat(lFormat + " HH:mm");
					try {
						lDate = ft.parse(pValue.toString());
					}
					catch (ParseException e) {
					}

					if (this.mDBMS.equalsIgnoreCase(Database.DBMS_MYSQL)) {
						ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						a = "'" + ft.format(lDate) + "'";
					}
					else if (this.mDBMS.equalsIgnoreCase(Database.DBMS_ORACLE)) {
						ft = new SimpleDateFormat(lFormat + " HH:mm");
						a = "TO_DATE('" + ft.format(lDate) + "', '" + lFormat + " hh24:mi')";
					}
					else if (this.mDBMS.equalsIgnoreCase(Database.DBMS_SQLSERVER)) {
						Calendar calend = Calendar.getInstance();
						calend.setTime(lDate);
						int lAno = calend.get(Calendar.YEAR);

						// VERIFICA ANO DA DATA (SQL SERVER NÃO ACEITA ANO MENOR QUE 1753)
						if (lAno < 1753) {
							calend.set(1753, calend.get(Calendar.MONTH), calend.get(Calendar.DATE), calend.get(Calendar.HOUR_OF_DAY), calend.get(Calendar.MINUTE), calend.get(Calendar.SECOND));
							lDate = calend.getTime();
						}

						ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						a = "CONVERT(datetime,'" + ft.format(lDate) + "', 120)";
					}

					break;

				case TIME:
					ft = new SimpleDateFormat(lFormat + " HH:mm:ss");
					try {
						lDate = ft.parse(pValue.toString());
					}
					catch (ParseException e) {
					}

					if (this.mDBMS.equalsIgnoreCase(Database.DBMS_MYSQL)) {
						ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						a = "'" + ft.format(lDate) + "'";
					}
					else if (this.mDBMS.equalsIgnoreCase(Database.DBMS_ORACLE)) {
						ft = new SimpleDateFormat(lFormat + " HH:mm");
						a = "TO_DATE('" + ft.format(lDate) + "', '" + lFormat + " hh24:mi')";
					}
					else if (this.mDBMS.equalsIgnoreCase(Database.DBMS_SQLSERVER)) {
						a = charToDate("'" + pValue.toString() + "'");
					}
					else if (this.mDBMS.equalsIgnoreCase(Database.DBMS_SQLSERVER)) {
						Calendar calend = Calendar.getInstance();
						calend.setTime(lDate);
						int lAno = calend.get(Calendar.YEAR);

						// VERIFICA ANO DA DATA (SQL SERVER NÃO ACEITA ANO MENOR QUE 1753)
						if (lAno < 1753) {
							calend.set(1753, calend.get(Calendar.MONTH), calend.get(Calendar.DATE), calend.get(Calendar.HOUR_OF_DAY), calend.get(Calendar.MINUTE), calend.get(Calendar.SECOND));
							lDate = calend.getTime();
						}

						ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						a = "CONVERT(DATETIME,'" + ft.format(lDate) + "', 120)";
					}

					break;

				default:
					if (pUseTrim) {
						a = "'" + pValue.toString().trim().replaceAll("\'", "`") + "'";
					}
					else {
						a = "'" + pValue.toString().replaceAll("\'", "`") + "'";
					}
					break;
			}
		}
		return a;
	}

	// ======================================================
	// =================== DEMAIS METODOS ===================
	// ======================================================
	public static void noCache(HttpServletResponse response, HttpServletRequest request) {
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		if (request.getProtocol().equals("HTTP/1.1")) {
			response.setHeader("Cache-Control", "no-cache");
		}
	}

	public static void clearQueryList(Connection pConn) {
		try {
			( (ConnectionISG) pConn ).clearLog();
		}
		catch (Exception ex) {
		}
	}

	@SuppressWarnings( "static-access" )
	public String getQueryList(Connection pConn) {
		ConnectionISG.ItemLog itemLog = null;
		StringBuffer log = new StringBuffer();

		for (int i = 0; i < ( (ConnectionISG) pConn ).logList.size(); i++) {
			itemLog = (ConnectionISG.ItemLog) ( (ConnectionISG) pConn ).logList.get(i);
			log.append("[" + itemLog.date + "]   ");
			log.append(itemLog.log + "<br>!-------------------------------!<br>");
		}
		return log.toString();
	}

	public static ArrayList<UserException> getTables(User mUser) {
		ArrayList<UserException> tables = new ArrayList<UserException>();
		
		UserException ex = new UserException();
		ex.setNumber("ANXOS");
		ex.setDescription(mUser.getTermo("ANEXOS"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("AREAS");
		ex.setDescription(mUser.getTermo("AREAS"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("CTMTS");
		ex.setDescription(mUser.getTermo("CATEGMATERIAL"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("EMLPR");
		ex.setDescription(mUser.getTermo("EMAILPARAM"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("FNSEG");
		ex.setDescription(mUser.getTermo("FUNCAOSEG"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("GFSEG");
		ex.setDescription(mUser.getTermo("GRPFUNCAOSEG"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("GPARS");
		ex.setDescription(mUser.getTermo("GRUPOAREA"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("ITUSS");
		ex.setDescription(mUser.getTermo("USERINTERESSE"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("LTGAS");
		ex.setDescription(mUser.getTermo("LTGAS"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("LTSEG");
		ex.setDescription(mUser.getTermo("LTSEG"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("MTAUS");
		ex.setDescription(mUser.getTermo("METAUTH"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("OFMTS");
		ex.setDescription(mUser.getTermo("OFERTA"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("PFSEG");
		ex.setDescription(mUser.getTermo("PERFIL"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("PMSAC");
		ex.setDescription(mUser.getTermo("PERMISSOES"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("PRSTS");
		ex.setDescription(mUser.getTermo("PRSTS"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("RQMTS");
		ex.setDescription(mUser.getTermo("REQUISICAO"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("RSMTS");
		ex.setDescription(mUser.getTermo("RESERVAS"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("SCMTS");
		ex.setDescription(mUser.getTermo("SUBCATEGMATERIAL"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("SLMTS");
		ex.setDescription(mUser.getTermo("SOLICITACAO"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("STEST");
		ex.setDescription(mUser.getTermo("SITE"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("UNMDS");
		ex.setDescription(mUser.getTermo("UNIDMEDIDA"));
		tables.add(ex);
		
		ex = new UserException();
		ex.setNumber("USUPA");
		ex.setDescription(mUser.getTermo("USUARIO"));
		tables.add(ex);
		
		return tables;
	}
}
