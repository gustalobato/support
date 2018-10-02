
package br.com.manchestercity.automacao;

import java.sql.Connection;

public interface Persist {
	public boolean insert(User pUser, Connection pConn, StringBuffer pErro, String[] pParam);
	public boolean update(User pUser, Connection pConn, StringBuffer pErro, String[] pParam);
	public boolean remove(User pUser, Connection pConn, StringBuffer pErro, String[] pParam);
}
