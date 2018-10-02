
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLITUSS {
	Database lcdb;
	User user;

	public SQLITUSS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_USUPA, String pCD_CTMTS) {
		long maxPK = lcdb.maxDB("ITUSS", "ITUSS.CD_USUPA", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO ITUSS (\n");
			query.append("  CD_USUPA,\n");
			query.append("  CD_CTMTS");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_USUPA, String pCD_CTMTS) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE ITUSS SET \n");

			query.append(aux);
			query.append("\n");
			query.append("WHERE ITUSS.CD_CTMTS = " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER) + " AND ITUSS.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_CTMTS, String pCD_USUPA) {
		boolean commit = false;
		StringBuffer query = new StringBuffer();
		StringBuffer aux = new StringBuffer();
		try {
			if (!Database.verifyNull(pCD_USUPA).equals("")) {
				aux.append(" ITUSS.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "\n");
			}

			if (!Database.verifyNull(pCD_CTMTS).equals("")) {
				if (aux.length() > 0) {
					aux.append("  AND");
				}
				aux.append(" ITUSS.CD_CTMTS = " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER) + "\n");
			}
			if(aux.length() > 0){
				query.append(" DELETE FROM ITUSS \n");
				query.append(" WHERE "+aux.toString());
				commit = lcdb.executeQuery(query.toString(), conn);
			}			
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_USUPA, String pCD_CTMTS, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  ITUSS.CD_USUPA,\n");
		query.append("  ITUSS.CD_CTMTS\n");
		query.append("FROM ITUSS \n");

		if (!Database.verifyNull(pCD_USUPA).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" ITUSS.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_CTMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" ITUSS.CD_CTMTS = " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER) + "\n");
		}

		if (aux.length() > 0) {
			query.append("WHERE");
			query.append(aux);
		}

		if (Database.verifyNull(pOrderBy).trim().length() > 0) {
			query.append("ORDER BY " + pOrderBy.trim() + " ");
		}

		return query.toString();
	}
}
