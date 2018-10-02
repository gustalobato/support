
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLGPARS {
	Database lcdb;
	User user;

	public SQLGPARS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_GPARS, String pNM_GPARS, String pID_GPARS_STATU) {
		long maxPK = lcdb.maxDB("GPARS", "GPARS.CD_GPARS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO GPARS (\n");
			query.append("  CD_GPARS,\n");
			query.append("  NM_GPARS,\n");
			query.append("  ID_GPARS_STATU");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_GPARS, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_GPARS_STATU, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_GPARS, String pNM_GPARS, String pID_GPARS_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE GPARS SET \n");

			if (pNM_GPARS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  GPARS.NM_GPARS = " + lcdb.verifyInsertNull(pNM_GPARS, DataType.TEXT));
			}

			if (pID_GPARS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  GPARS.ID_GPARS_STATU = " + lcdb.verifyInsertNull(pID_GPARS_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE GPARS.CD_GPARS = " + lcdb.verifyInsertNull(pCD_GPARS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_GPARS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM GPARS WHERE GPARS.CD_GPARS = " + lcdb.verifyInsertNull(pCD_GPARS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_GPARS, String pNM_GPARS, String pID_GPARS_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  GPARS.CD_GPARS,\n");
		query.append("  GPARS.NM_GPARS,\n");
		query.append("  GPARS.ID_GPARS_STATU\n");
		query.append("FROM GPARS \n");

		if (!Database.verifyNull(pCD_GPARS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" GPARS.CD_GPARS = " + lcdb.verifyInsertNull(pCD_GPARS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_GPARS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" GPARS.NM_GPARS = " + lcdb.verifyInsertNull(pNM_GPARS, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_GPARS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" GPARS.ID_GPARS_STATU = " + lcdb.verifyInsertNull(pID_GPARS_STATU, DataType.TEXT) + "\n");
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
