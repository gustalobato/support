
package br.com.manchestercity.automacao;

import java.sql.Connection;
import java.util.ArrayList;

public interface ConnectionISG extends Connection {

	public ArrayList<ItemLog> logList = new ArrayList<ItemLog>();

	public class ItemLog {
		String log;
		String date;

		public ItemLog(String log, String date) {
			this.log = log;
			this.date = date;
		}
	}

	public void addLog(String log, String date);

	public void clearLog();
	
	public void setActiveConnection(Connection pConn);
}
