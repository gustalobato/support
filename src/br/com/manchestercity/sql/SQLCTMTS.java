
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLCTMTS {
	Database lcdb;
	User user;

	public SQLCTMTS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_CTMTS, String pNM_CTMTS, String pID_CTMTS_CD_SAP, String pID_CTMTS_STATU) {
		long maxPK = lcdb.maxDB("CTMTS", "CTMTS.CD_CTMTS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO CTMTS (\n");
			query.append("  CD_CTMTS,\n");
			query.append("  NM_CTMTS,\n");
			query.append("  ID_CTMTS_CD_SAP,\n");
			query.append("  ID_CTMTS_STATU");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_CTMTS, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_CTMTS_CD_SAP, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_CTMTS_STATU, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_CTMTS, String pNM_CTMTS, String pID_CTMTS_CD_SAP, String pID_CTMTS_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE CTMTS SET \n");

			if (pNM_CTMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  CTMTS.NM_CTMTS = " + lcdb.verifyInsertNull(pNM_CTMTS, DataType.TEXT));
			}

			if (pID_CTMTS_CD_SAP != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  CTMTS.ID_CTMTS_CD_SAP = " + lcdb.verifyInsertNull(pID_CTMTS_CD_SAP, DataType.TEXT));
			}

			if (pID_CTMTS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  CTMTS.ID_CTMTS_STATU = " + lcdb.verifyInsertNull(pID_CTMTS_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE CTMTS.CD_CTMTS = " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_CTMTS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM CTMTS WHERE CTMTS.CD_CTMTS = " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_CTMTS, String pNM_CTMTS, String pID_CTMTS_CD_SAP, String pID_CTMTS_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  CTMTS.CD_CTMTS,\n");
		query.append("  CTMTS.NM_CTMTS,\n");
		query.append("  CTMTS.ID_CTMTS_CD_SAP,\n");
		query.append("  CTMTS.ID_CTMTS_STATU\n");
		query.append("FROM CTMTS \n");

		if (!Database.verifyNull(pCD_CTMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" CTMTS.CD_CTMTS = " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_CTMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" CTMTS.NM_CTMTS = " + lcdb.verifyInsertNull(pNM_CTMTS, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_CTMTS_CD_SAP).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" CTMTS.ID_CTMTS_CD_SAP = " + lcdb.verifyInsertNull(pID_CTMTS_CD_SAP, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_CTMTS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" CTMTS.ID_CTMTS_STATU = " + lcdb.verifyInsertNull(pID_CTMTS_STATU, DataType.TEXT) + "\n");
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
