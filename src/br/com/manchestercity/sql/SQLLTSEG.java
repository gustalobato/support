
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLLTSEG {
	Database lcdb;
	User user;

	public SQLLTSEG(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_USUPA, String pCD_PFSEG) {
		long maxPK = lcdb.maxDB("LTSEG", "LTSEG.CD_USUPA", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO LTSEG (\n");
			query.append("  CD_USUPA,\n");
			query.append("  CD_PFSEG");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_USUPA, String pCD_PFSEG) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE LTSEG SET \n");

			query.append(aux);
			query.append("\n");
			query.append("WHERE LTSEG.CD_PFSEG = " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + " AND LTSEG.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_PFSEG, String pCD_USUPA) {
		boolean commit = false;
		StringBuffer query = new StringBuffer();
		StringBuffer aux = new StringBuffer();
		try {
			if (!Database.verifyNull(pCD_USUPA).equals("")) {
				aux.append(" LTSEG.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "\n");
			}

			if (!Database.verifyNull(pCD_PFSEG).equals("")) {
				if (aux.length() > 0) {
					aux.append("  AND");
				}
				aux.append(" LTSEG.CD_PFSEG = " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + "\n");
			}
			if(aux.length() > 0){
				query.append(" DELETE FROM LTSEG \n");
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

	public String refresh(String pCD_USUPA, String pCD_PFSEG, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  LTSEG.CD_USUPA,\n");
		query.append("  LTSEG.CD_PFSEG,\n");
		query.append("	PFSEG.NM_PFSEG \n");
		query.append("FROM LTSEG \n");
		query.append("LEFT JOIN PFSEG ON LTSEG.CD_PFSEG = PFSEG.CD_PFSEG \n");

		if (!Database.verifyNull(pCD_USUPA).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" LTSEG.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_PFSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" LTSEG.CD_PFSEG = " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + "\n");
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
