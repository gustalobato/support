
package br.com.manchestercity.automacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import br.com.manchestercity.SystemConfig;

public class IniManipulation {
	private static String realPath = "";
	private static final String INI_FILE = SystemConfig.SYSTEM_INI_FILE;

	public static final String DBMS = "dbms";

	public static final String DB_DATABASE = "db_database";
	public static final String DB_INSTANCE = "db_instance";
	public static final String DB_POOL = "db_pool";
	public static final String DB_PORT = "db_port";
	public static final String DB_PWD = "db_pwd";
	public static final String DB_SERVER = "db_server";
	public static final String DB_SERVICE_NAME = "db_service_name";
	public static final String DB_SID = "db_sid";
	public static final String DB_USER = "db_user";

	public static final String FILE_SIZE = "file_size";
	public static final String FILE_SYSTEM_PATH = "file_system_path";

	public static final String INI_SENHA = "ini_senha";

	public static final String LAYOUT_THEME = "layout_theme";

	public static final String LOGIN_COOKIE = "login_cookie";

	public static final String MAIL_AUTH = "mail_auth";
	public static final String MAIL_FROM = "mail_from";
	public static final String MAIL_FROM_NAME = "mail_from_name";
	public static final String MAIL_LOG = "mail_log";
	public static final String MAIL_LOG_DB = "mail_log_db";
	public static final String MAIL_PORT = "mail_port";
	public static final String MAIL_PWD = "mail_pwd";
	public static final String MAIL_SERVER = "mail_server";
	public static final String MAIL_USER = "mail_user";

	// WEBSERVICE PORTAL ARCELO MITTAL TUBARAO
	public static final String WS_URL_HR = "ws_url_hr";
	public static final String WS_URL_ADD = "ws_url_add";
	public static final String WS_URL_PORTAL = "ws_url_portal";

	public static String setRealPath(String realPath) {
		return IniManipulation.realPath = realPath;
	}

	public static String getRealPath() {
		return IniManipulation.realPath;
	}

	public static String getIniFile() {
		return INI_FILE;
	}

	public static String getProperty(String pProperty) {
		String lValue = "";
		FileInputStream lFile = null;

		try {
			Properties p = new Properties();
			lFile = new FileInputStream(getRealPath() + "/WEB-INF/" + getIniFile());
			p.load(lFile);
			lValue = p.getProperty(pProperty);
			if (lValue == null) {
				lValue = "";
			}

		}
		catch (Exception e) {
			lValue = "";
		}

		try {
			lFile.close();
		}
		catch (IOException ex) {
		}

		return lValue;
	}

	@SuppressWarnings( "rawtypes" )
	public static Enumeration getPropertyNames() {
		Enumeration lEnum = null;
		FileInputStream lFile = null;

		try {
			Properties p = new Properties();
			lFile = new FileInputStream(getRealPath() + "/WEB-INF/" + getIniFile());
			p.load(lFile);
			lEnum = p.propertyNames();
		}
		catch (Exception e) {
			lEnum = null;
		}

		try {
			lFile.close();
		}
		catch (IOException ex) {
		}

		return lEnum;
	}

	public static synchronized boolean addProperty(String pName, String pValue) {
		boolean lRet = false;
		FileInputStream lFile = null;
		FileOutputStream lFileOut = null;

		try {
			if (!pName.trim().equals("")) {

				Properties p = new Properties();
				lFile = new FileInputStream(getRealPath() + "/WEB-INF/" + getIniFile());
				p.load(lFile);
				p.setProperty(pName, pValue);

				try {
					lFile.close();
				}
				catch (IOException ex) {
				}

				lFileOut = new FileOutputStream(getRealPath() + "/WEB-INF/" + getIniFile());

				p.store(lFileOut, "");
				lRet = true;
			}
		}
		catch (Exception e) {
		}

		try {
			lFile.close();
		}
		catch (IOException ex) {
		}

		try {
			lFileOut.close();
		}
		catch (IOException ex1) {
		}

		return lRet;

	}

	public static boolean iniExists() {
		try {
			return new File(getRealPath() + "/WEB-INF/" + getIniFile()).exists();
		}
		catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings( "rawtypes" )
	public static synchronized boolean removeProperty(String pName) {
		boolean lRet = false;
		FileInputStream lFile = null;
		FileOutputStream lFileOut = null;

		try {
			Properties p = new Properties();
			Properties pAux = new Properties();
			lFile = new FileInputStream(getRealPath() + "/WEB-INF/" + getIniFile());
			p.load(lFile);

			try {
				lFile.close();
			}
			catch (Exception ex) {
			}

			lFileOut = new FileOutputStream(getRealPath() + "/WEB-INF/" + getIniFile());
			Enumeration lEnum = p.propertyNames();
			String lItem = "";
			while (lEnum.hasMoreElements()) {
				lItem = (String) lEnum.nextElement();
				if (!lItem.equals(pName)) {
					pAux.setProperty(lItem, p.getProperty(lItem));
				}
			}
			pAux.store(lFileOut, "");
			lRet = true;
		}
		catch (Exception e) {
		}

		try {
			lFile.close();
		}
		catch (Exception ex) {
		}

		try {
			lFileOut.close();
		}
		catch (Exception ex1) {
		}

		return lRet;
	}

	public static synchronized boolean setProperty(String pName, String pValue) {
		boolean lRet = false;
		FileInputStream lFile = null;
		FileOutputStream lFileOut = null;

		try {
			Properties p = new Properties();
			lFile = new FileInputStream(getRealPath() + "/WEB-INF/" + getIniFile());
			p.load(lFile);
			p.setProperty(pName, pValue);

			try {
				lFile.close();
			}
			catch (IOException ex) {
			}

			lFileOut = new FileOutputStream(getRealPath() + "/WEB-INF/" + getIniFile());
			p.store(lFileOut, "");
			lRet = true;
		}
		catch (Exception e) {
		}

		try {
			lFile.close();
		}
		catch (Exception e) {
		}

		try {
			lFileOut.close();
		}
		catch (Exception e) {
		}

		return lRet;
	}

	public static synchronized void createIni() {
		if (!iniExists()) {
			Properties p = new Properties();
			FileOutputStream out = null;
			try {
				File file = new File(getRealPath() + "/WEB-INF/" + getIniFile());
				out = new FileOutputStream(file);
				p.setProperty(IniManipulation.DBMS, "");
				p.setProperty(IniManipulation.DB_DATABASE, "");
				p.setProperty(IniManipulation.DB_INSTANCE, "");
				p.setProperty(IniManipulation.DB_POOL, "");
				p.setProperty(IniManipulation.DB_PORT, "");
				p.setProperty(IniManipulation.DB_PWD, "");
				p.setProperty(IniManipulation.DB_SERVER, "");
				p.setProperty(IniManipulation.DB_SID, "");
				p.setProperty(IniManipulation.DB_USER, "");
				p.store(out, "");
			}
			catch (Exception e) {
			}

			try {
				out.close();
			}
			catch (Exception e) {
			}

			out = null;
			p = null;
		}
	}

}
