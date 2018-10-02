
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLPFSEG {
	Database lcdb;
	User user;

	public SQLPFSEG(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_PFSEG, String pNM_PFSEG, String pID_PFSEG_STATU, String pID_PFSEG_DFAUT) {
		long maxPK = lcdb.maxDB("PFSEG", "PFSEG.CD_PFSEG", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO PFSEG (\n");
			query.append("  CD_PFSEG,\n");
			query.append("  NM_PFSEG,\n");
			query.append("  ID_PFSEG_STATU,\n");
			query.append("  ID_PFSEG_DFAUT");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_PFSEG, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_PFSEG_STATU, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_PFSEG_DFAUT, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_PFSEG, String pNM_PFSEG, String pID_PFSEG_STATU, String pID_PFSEG_DFAUT) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE PFSEG SET \n");

			if (pNM_PFSEG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PFSEG.NM_PFSEG = " + lcdb.verifyInsertNull(pNM_PFSEG, DataType.TEXT));
			}

			if (pID_PFSEG_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PFSEG.ID_PFSEG_STATU = " + lcdb.verifyInsertNull(pID_PFSEG_STATU, DataType.TEXT));
			}

			if (pID_PFSEG_DFAUT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PFSEG.ID_PFSEG_DFAUT = " + lcdb.verifyInsertNull(pID_PFSEG_DFAUT, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE PFSEG.CD_PFSEG = " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_PFSEG) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM PFSEG WHERE PFSEG.CD_PFSEG = " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_PFSEG, String pNM_PFSEG, String pID_PFSEG_STATU, String pID_PFSEG_DFAUT, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  PFSEG.CD_PFSEG,\n");
		query.append("  PFSEG.NM_PFSEG,\n");
		query.append("  PFSEG.ID_PFSEG_STATU,\n");
		query.append("  PFSEG.ID_PFSEG_DFAUT\n");
		query.append("FROM PFSEG \n");

		if (!Database.verifyNull(pCD_PFSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PFSEG.CD_PFSEG = " + lcdb.verifyInsertNull(pCD_PFSEG, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_PFSEG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PFSEG.NM_PFSEG = " + lcdb.verifyInsertNull(pNM_PFSEG, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_PFSEG_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PFSEG.ID_PFSEG_STATU = " + lcdb.verifyInsertNull(pID_PFSEG_STATU, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_PFSEG_DFAUT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PFSEG.ID_PFSEG_DFAUT = " + lcdb.verifyInsertNull(pID_PFSEG_DFAUT, DataType.TEXT) + "\n");
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

	public boolean clearDefaults(Connection conn, StringBuffer errors) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("UPDATE PFSEG SET PFSEG.ID_PFSEG_DFAUT = 'N'", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}
}
