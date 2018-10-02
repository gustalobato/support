
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLUNMDS {
	Database lcdb;
	User user;

	public SQLUNMDS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_UNMDS, String pNM_UNMDS, String pID_UNMDS_STATU) {
		long maxPK = lcdb.maxDB("UNMDS", "UNMDS.CD_UNMDS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO UNMDS (\n");
			query.append("  CD_UNMDS,\n");
			query.append("  NM_UNMDS,\n");
			query.append("  ID_UNMDS_STATU");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_UNMDS, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_UNMDS_STATU, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_UNMDS, String pNM_UNMDS, String pID_UNMDS_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE UNMDS SET \n");

			if (pNM_UNMDS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  UNMDS.NM_UNMDS = " + lcdb.verifyInsertNull(pNM_UNMDS, DataType.TEXT));
			}

			if (pID_UNMDS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  UNMDS.ID_UNMDS_STATU = " + lcdb.verifyInsertNull(pID_UNMDS_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE UNMDS.CD_UNMDS = " + lcdb.verifyInsertNull(pCD_UNMDS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_UNMDS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM UNMDS WHERE UNMDS.CD_UNMDS = " + lcdb.verifyInsertNull(pCD_UNMDS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_UNMDS, String pNM_UNMDS, String pID_UNMDS_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  UNMDS.CD_UNMDS,\n");
		query.append("  UNMDS.NM_UNMDS,\n");
		query.append("  UNMDS.ID_UNMDS_STATU\n");
		query.append("FROM UNMDS \n");

		if (!Database.verifyNull(pCD_UNMDS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" UNMDS.CD_UNMDS = " + lcdb.verifyInsertNull(pCD_UNMDS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_UNMDS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" UNMDS.NM_UNMDS = " + lcdb.verifyInsertNull(pNM_UNMDS, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_UNMDS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" UNMDS.ID_UNMDS_STATU = " + lcdb.verifyInsertNull(pID_UNMDS_STATU, DataType.TEXT) + "\n");
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
