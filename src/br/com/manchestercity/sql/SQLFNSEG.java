
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLFNSEG {
	Database lcdb;
	User user;

	public SQLFNSEG(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized String insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_FNSEG, String pCD_GFSEG, String pNM_FNSEG, String pID_FNSEG_CRUD) {
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO FNSEG (\n");
			query.append("  CD_FNSEG,\n");
			query.append("  CD_GFSEG,\n");
			query.append("  NM_FNSEG,\n");
			query.append("  ID_FNSEG_CRUD");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_FNSEG, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_GFSEG, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_FNSEG, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_FNSEG_CRUD, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return "";
		}
		return commit ? pCD_FNSEG : "";
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_FNSEG, String pCD_GFSEG, String pNM_FNSEG, String pID_FNSEG_CRUD) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE FNSEG SET \n");

			if (pCD_GFSEG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  FNSEG.CD_GFSEG = " + lcdb.verifyInsertNull(pCD_GFSEG, DataType.INTEGER));
			}

			if (pNM_FNSEG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  FNSEG.NM_FNSEG = " + lcdb.verifyInsertNull(pNM_FNSEG, DataType.TEXT));
			}

			if (pID_FNSEG_CRUD != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  FNSEG.ID_FNSEG_CRUD = " + lcdb.verifyInsertNull(pID_FNSEG_CRUD, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE FNSEG.CD_FNSEG = " + lcdb.verifyInsertNull(pCD_FNSEG, DataType.TEXT) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_FNSEG) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM FNSEG WHERE FNSEG.CD_FNSEG = " + lcdb.verifyInsertNull(pCD_FNSEG, DataType.TEXT) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_FNSEG, String pCD_GFSEG, String pNM_FNSEG, String pID_FNSEG_CRUD, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  FNSEG.CD_FNSEG,\n");
		query.append("  FNSEG.CD_GFSEG,\n");
		query.append("  FNSEG.NM_FNSEG,\n");
		query.append("  FNSEG.ID_FNSEG_CRUD\n");
		query.append("FROM FNSEG \n");

		if (!Database.verifyNull(pCD_FNSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" FNSEG.CD_FNSEG = " + lcdb.verifyInsertNull(pCD_FNSEG, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pCD_GFSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" FNSEG.CD_GFSEG = " + lcdb.verifyInsertNull(pCD_GFSEG, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_FNSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" FNSEG.NM_FNSEG = " + lcdb.verifyInsertNull(pNM_FNSEG, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_FNSEG_CRUD).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" FNSEG.ID_FNSEG_CRUD = " + lcdb.verifyInsertNull(pID_FNSEG_CRUD, DataType.TEXT) + "\n");
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
