
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLAREAS {
	Database lcdb;
	User user;

	public SQLAREAS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_AREAS, String pCD_STEST, String pCD_AREAS_PAI, String pNM_AREAS, String pID_AREAS_STATU, String pID_AREAS_DFAUT) {
		long maxPK = lcdb.maxDB("AREAS", "AREAS.CD_AREAS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO AREAS (\n");
			query.append("  CD_AREAS,\n");
			query.append("  CD_STEST,\n");
			query.append("  CD_AREAS_PAI,\n");
			query.append("  NM_AREAS,\n");
			query.append("  ID_AREAS_STATU,\n");
			query.append("  ID_AREAS_DFAUT");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_AREAS_PAI, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_AREAS, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_AREAS_STATU, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_AREAS_DFAUT, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_AREAS, String pCD_STEST, String pCD_AREAS_PAI, String pNM_AREAS, String pID_AREAS_STATU, String pID_AREAS_DFAUT) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE AREAS SET \n");

			if (pCD_STEST != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  AREAS.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER));
			}

			if (pCD_AREAS_PAI != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  AREAS.CD_AREAS_PAI = " + lcdb.verifyInsertNull(pCD_AREAS_PAI, DataType.INTEGER));
			}

			if (pNM_AREAS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  AREAS.NM_AREAS = " + lcdb.verifyInsertNull(pNM_AREAS, DataType.TEXT));
			}

			if (pID_AREAS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  AREAS.ID_AREAS_STATU = " + lcdb.verifyInsertNull(pID_AREAS_STATU, DataType.TEXT));
			}

			if (pID_AREAS_DFAUT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  AREAS.ID_AREAS_DFAUT = " + lcdb.verifyInsertNull(pID_AREAS_DFAUT, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE AREAS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_AREAS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM AREAS WHERE AREAS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_AREAS, String pCD_STEST, String pCD_AREAS_PAI, String pNM_AREAS, String pID_AREAS_STATU, String pID_AREAS_DFAUT, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  AREAS.CD_AREAS,\n");
		query.append("  AREAS.CD_STEST,\n");
		query.append("  AREAS.CD_AREAS_PAI,\n");
		query.append("  AREAS.NM_AREAS,\n");
		query.append("  AREAS.ID_AREAS_STATU,\n");
		query.append("  AREAS.ID_AREAS_DFAUT\n");
		query.append("FROM AREAS \n");

		if (!Database.verifyNull(pCD_AREAS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" AREAS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_STEST).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" AREAS.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_AREAS_PAI).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" AREAS.CD_AREAS_PAI = " + lcdb.verifyInsertNull(pCD_AREAS_PAI, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_AREAS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" AREAS.NM_AREAS = " + lcdb.verifyInsertNull(pNM_AREAS, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_AREAS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" AREAS.ID_AREAS_STATU = " + lcdb.verifyInsertNull(pID_AREAS_STATU, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_AREAS_DFAUT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" AREAS.ID_AREAS_DFAUT = " + lcdb.verifyInsertNull(pID_AREAS_DFAUT, DataType.TEXT) + "\n");
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
			commit = lcdb.executeQuery("UPDATE AREAS SET AREAS.ID_AREAS_DFAUT = 'N'", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}
}
