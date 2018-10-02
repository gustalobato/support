
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLPMSAC {
	Database lcdb;
	User user;

	public SQLPMSAC(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_PFSEG, String pCD_FNSEG, String pID_PMSAC_CRUD) {
		long maxPK = lcdb.maxDB("PMSAC", "PMSAC.CD_PFSEG", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO PMSAC (\n");
			query.append("  CD_PFSEG,\n");
			query.append("  CD_FNSEG,\n");
			query.append("  ID_PMSAC_CRUD");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_FNSEG, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_PMSAC_CRUD, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_PFSEG, String pCD_FNSEG, String pID_PMSAC_CRUD) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE PMSAC SET \n");

			if (pID_PMSAC_CRUD != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PMSAC.ID_PMSAC_CRUD = " + lcdb.verifyInsertNull(pID_PMSAC_CRUD, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE PMSAC.CD_FNSEG = " + lcdb.verifyInsertNull(pCD_FNSEG, DataType.TEXT) + " AND PMSAC.CD_PFSEG = " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_FNSEG, String pCD_PFSEG) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM PMSAC WHERE PMSAC.CD_FNSEG = " + lcdb.verifyInsertNull(pCD_FNSEG, DataType.TEXT) + " AND PMSAC.CD_PFSEG = " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_PFSEG, String pCD_FNSEG, String pID_PMSAC_CRUD, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  PMSAC.CD_PFSEG,\n");
		query.append("  FNSEG.CD_GFSEG,\n");
		query.append("  PMSAC.CD_FNSEG,\n");
		query.append("  FNSEG.NM_FNSEG,\n");
		query.append("  PMSAC.ID_PMSAC_CRUD\n");
		query.append("FROM PMSAC \n");
		query.append("	LEFT JOIN FNSEG ON PMSAC.CD_FNSEG = FNSEG.CD_FNSEG \n");

		if (!Database.verifyNull(pCD_PFSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PMSAC.CD_PFSEG = " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_FNSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PMSAC.CD_FNSEG = " + lcdb.verifyInsertNull(pCD_FNSEG, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_PMSAC_CRUD).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PMSAC.ID_PMSAC_CRUD = " + lcdb.verifyInsertNull(pID_PMSAC_CRUD, DataType.TEXT) + "\n");
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
