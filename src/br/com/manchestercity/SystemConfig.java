
package br.com.manchestercity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemConfig {

	public static final String SYSTEM_INI_FILE = "supporter.properties";
	public final static String SYSTEM_VERSION = "1.00.00";

	private static String mSystemName = "The Citizens Brasil - Supporter Control";

	public static final String getSystemName() {
		return mSystemName;
	}

	public static final String getCompilationDateTime() {
		try {
			SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date d;
			d = new Date(SystemConfig.class.getResource("SystemConfig.class").openConnection().getLastModified());
			return ft.format(d);
		}
		catch (Exception ex) {
			return "Impossível recuperar data";
		}
	}
}
