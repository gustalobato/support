
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLGFSEG {
	Database lcdb;
	User user;

	public SQLGFSEG(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_GFSEG, String pNM_GFSEG) {
		long maxPK = lcdb.maxDB("GFSEG", "GFSEG.CD_GFSEG", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO GFSEG (\n");
			query.append("  CD_GFSEG,\n");
			query.append("  NM_GFSEG");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_GFSEG, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_GFSEG, String pNM_GFSEG) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE GFSEG SET \n");

			if (pNM_GFSEG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  GFSEG.NM_GFSEG = " + lcdb.verifyInsertNull(pNM_GFSEG, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE GFSEG.CD_GFSEG = " + lcdb.verifyInsertNull(pCD_GFSEG, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_GFSEG) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM GFSEG WHERE GFSEG.CD_GFSEG = " + lcdb.verifyInsertNull(pCD_GFSEG, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_GFSEG, String pNM_GFSEG, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  GFSEG.CD_GFSEG,\n");
		query.append("  GFSEG.NM_GFSEG\n");
		query.append("FROM GFSEG \n");

		if (!Database.verifyNull(pCD_GFSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" GFSEG.CD_GFSEG = " + lcdb.verifyInsertNull(pCD_GFSEG, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_GFSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" GFSEG.NM_GFSEG = " + lcdb.verifyInsertNull(pNM_GFSEG, DataType.TEXT) + "\n");
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
