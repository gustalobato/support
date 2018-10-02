
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLSCMTS {
	Database lcdb;
	User user;

	public SQLSCMTS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_SCMTS, String pCD_CTMTS, String pCD_UNMDS, String pCD_USUPA_REG, String pNM_SCMTS, String pDT_SCMTS_REG, String pID_SCMTS_STATU) {
		long maxPK = lcdb.maxDB("SCMTS", "SCMTS.CD_SCMTS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO SCMTS (\n");
			query.append("  CD_SCMTS,\n");
			query.append("  CD_CTMTS,\n");
			query.append("  CD_UNMDS,\n");
			query.append("  CD_USUPA_REG,\n");
			query.append("  NM_SCMTS,\n");
			query.append("  DT_SCMTS_REG,\n");
			query.append("  ID_SCMTS_STATU");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_UNMDS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_REG, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_SCMTS, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_SCMTS_REG, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_SCMTS_STATU, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_SCMTS, String pCD_CTMTS, String pCD_UNMDS, String pCD_USUPA_REG, String pNM_SCMTS, String pDT_SCMTS_REG, String pID_SCMTS_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE SCMTS SET \n");

			if (pCD_CTMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SCMTS.CD_CTMTS = " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER));
			}

			if (pCD_UNMDS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SCMTS.CD_UNMDS = " + lcdb.verifyInsertNull(pCD_UNMDS, DataType.INTEGER));
			}

			if (pCD_USUPA_REG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SCMTS.CD_USUPA_REG = " + lcdb.verifyInsertNull(pCD_USUPA_REG, DataType.INTEGER));
			}

			if (pNM_SCMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SCMTS.NM_SCMTS = " + lcdb.verifyInsertNull(pNM_SCMTS, DataType.TEXT));
			}

			if (pDT_SCMTS_REG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SCMTS.DT_SCMTS_REG = " + lcdb.verifyInsertNull(pDT_SCMTS_REG, DataType.DATE));
			}

			if (pID_SCMTS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SCMTS.ID_SCMTS_STATU = " + lcdb.verifyInsertNull(pID_SCMTS_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE SCMTS.CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_SCMTS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM SCMTS WHERE SCMTS.CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_SCMTS, String pCD_CTMTS, String pCD_UNMDS, String pCD_USUPA_REG, String pNM_SCMTS, String pDT_SCMTS_REG, String pID_SCMTS_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  SCMTS.CD_SCMTS,\n");
		query.append("  SCMTS.CD_CTMTS,\n");
		query.append("  SCMTS.CD_UNMDS,\n");
		query.append("  SCMTS.CD_USUPA_REG,\n");
		query.append("  SCMTS.NM_SCMTS,\n");
		query.append("  " + lcdb.dateToChar("SCMTS.DT_SCMTS_REG") + " AS DT_SCMTS_REG,\n");
		query.append("  SCMTS.ID_SCMTS_STATU\n");
		query.append("FROM SCMTS \n");

		if (!Database.verifyNull(pCD_SCMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SCMTS.CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_CTMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SCMTS.CD_CTMTS = " + lcdb.verifyInsertNull(pCD_CTMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_UNMDS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SCMTS.CD_UNMDS = " + lcdb.verifyInsertNull(pCD_UNMDS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_USUPA_REG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SCMTS.CD_USUPA_REG = " + lcdb.verifyInsertNull(pCD_USUPA_REG, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_SCMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SCMTS.NM_SCMTS = " + lcdb.verifyInsertNull(pNM_SCMTS, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pDT_SCMTS_REG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SCMTS.DT_SCMTS_REG = " + lcdb.verifyInsertNull(pDT_SCMTS_REG, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_SCMTS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SCMTS.ID_SCMTS_STATU = " + lcdb.verifyInsertNull(pID_SCMTS_STATU, DataType.TEXT) + "\n");
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
