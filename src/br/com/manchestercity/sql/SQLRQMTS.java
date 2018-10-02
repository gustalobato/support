
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLRQMTS {
	Database lcdb;
	User user;

	public SQLRQMTS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_RQMTS, String pCD_OFMTS, String pCD_SLMTS, String pCD_RSMTS, String pCD_AREAS, String pCD_USUPA_RQSTE, String pCD_USUPA_ENCR, String pNO_RQMTS_QT, String pDT_RQMTS_REG, String pDT_RQMTS_RECBT, String pDT_RQMTS_ENCR, String pID_RQMTS_STATU, String pCD_USUPA_INSRT) {
		long maxPK = lcdb.maxDB("RQMTS", "RQMTS.CD_RQMTS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO RQMTS (\n");
			query.append("  CD_RQMTS,\n");
			query.append("  CD_OFMTS,\n");
			query.append("  CD_SLMTS,\n");
			query.append("  CD_RSMTS,\n");
			query.append("  CD_AREAS,\n");
			query.append("  CD_USUPA_RQSTE,\n");
			query.append("  CD_USUPA_ENCR,\n");
			query.append("  NO_RQMTS_QT,\n");
			query.append("  DT_RQMTS_REG,\n");
			query.append("  DT_RQMTS_RECBT,\n");
			query.append("  DT_RQMTS_ENCR,\n");
			query.append("  ID_RQMTS_STATU,\n");
			query.append("  CD_USUPA_INSRT");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_SLMTS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_RSMTS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_RQSTE, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_ENCR, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_RQMTS_QT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_RQMTS_REG, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_RQMTS_RECBT, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_RQMTS_ENCR, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_RQMTS_STATU, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_INSRT, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_RQMTS, String pCD_OFMTS, String pCD_SLMTS, String pCD_RSMTS, String pCD_AREAS, String pCD_USUPA_RQSTE, String pCD_USUPA_ENCR, String pNO_RQMTS_QT, String pDT_RQMTS_REG, String pDT_RQMTS_RECBT, String pDT_RQMTS_ENCR, String pID_RQMTS_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE RQMTS SET \n");

			if (pCD_OFMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER));
			}

			if (pCD_SLMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.CD_SLMTS = " + lcdb.verifyInsertNull(pCD_SLMTS, DataType.INTEGER));
			}

			if (pCD_RSMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.CD_RSMTS = " + lcdb.verifyInsertNull(pCD_RSMTS, DataType.INTEGER));
			}

			if (pCD_AREAS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER));
			}

			if (pCD_USUPA_RQSTE != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.CD_USUPA_RQSTE = " + lcdb.verifyInsertNull(pCD_USUPA_RQSTE, DataType.INTEGER));
			}

			if (pCD_USUPA_ENCR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.CD_USUPA_ENCR = " + lcdb.verifyInsertNull(pCD_USUPA_ENCR, DataType.INTEGER));
			}

			if (pNO_RQMTS_QT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.NO_RQMTS_QT = " + lcdb.verifyInsertNull(pNO_RQMTS_QT, DataType.INTEGER));
			}

			if (pDT_RQMTS_REG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.DT_RQMTS_REG = " + lcdb.verifyInsertNull(pDT_RQMTS_REG, DataType.DATE));
			}

			if (pDT_RQMTS_RECBT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.DT_RQMTS_RECBT = " + lcdb.verifyInsertNull(pDT_RQMTS_RECBT, DataType.DATE));
			}

			if (pDT_RQMTS_ENCR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.DT_RQMTS_ENCR = " + lcdb.verifyInsertNull(pDT_RQMTS_ENCR, DataType.DATE));
			}

			if (pID_RQMTS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RQMTS.ID_RQMTS_STATU = " + lcdb.verifyInsertNull(pID_RQMTS_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE RQMTS.CD_RQMTS = " + lcdb.verifyInsertNull(pCD_RQMTS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_RQMTS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM RQMTS WHERE RQMTS.CD_RQMTS = " + lcdb.verifyInsertNull(pCD_RQMTS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean deleteByOFMTS(Connection conn, StringBuffer errors, String pCD_OFMTS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM RQMTS WHERE RQMTS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_RQMTS, String pCD_OFMTS, String pCD_SLMTS, String pCD_RSMTS, String pCD_AREAS, String pCD_USUPA_RQSTE, String pCD_USUPA_ENCR, String pNO_RQMTS_QT, String pDT_RQMTS_REG, String pDT_RQMTS_RECBT, String pDT_RQMTS_ENCR, String pID_RQMTS_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  RQMTS.CD_RQMTS,\n");
		query.append("  RQMTS.CD_OFMTS,\n");
		query.append("  RQMTS.CD_SLMTS,\n");
		query.append("  RQMTS.CD_RSMTS,\n");
		query.append("  RQMTS.CD_AREAS,\n");
		query.append("  RQMTS.CD_USUPA_RQSTE,\n");
		query.append("  RQMTS.CD_USUPA_ENCR,\n");
		query.append("  RQMTS.NO_RQMTS_QT,\n");
		query.append("  " + lcdb.dateToChar("RQMTS.DT_RQMTS_REG") + " AS DT_RQMTS_REG,\n");
		query.append("  " + lcdb.dateToChar("RQMTS.DT_RQMTS_RECBT") + " AS DT_RQMTS_RECBT,\n");
		query.append("  " + lcdb.dateToChar("RQMTS.DT_RQMTS_ENCR") + " AS DT_RQMTS_ENCR,\n");
		query.append("  RQMTS.ID_RQMTS_STATU,\n");
		query.append("  RQMTS.CD_USUPA_INSRT \n");
		query.append("FROM RQMTS \n");

		if (!Database.verifyNull(pCD_RQMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.CD_RQMTS = " + lcdb.verifyInsertNull(pCD_RQMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_OFMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_SLMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.CD_SLMTS = " + lcdb.verifyInsertNull(pCD_SLMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_RSMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.CD_RSMTS = " + lcdb.verifyInsertNull(pCD_RSMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_AREAS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_USUPA_RQSTE).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.CD_USUPA_RQSTE = " + lcdb.verifyInsertNull(pCD_USUPA_RQSTE, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_USUPA_ENCR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.CD_USUPA_ENCR = " + lcdb.verifyInsertNull(pCD_USUPA_ENCR, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNO_RQMTS_QT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.NO_RQMTS_QT = " + lcdb.verifyInsertNull(pNO_RQMTS_QT, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pDT_RQMTS_REG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.DT_RQMTS_REG = " + lcdb.verifyInsertNull(pDT_RQMTS_REG, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pDT_RQMTS_RECBT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.DT_RQMTS_RECBT = " + lcdb.verifyInsertNull(pDT_RQMTS_RECBT, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pDT_RQMTS_ENCR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.DT_RQMTS_ENCR = " + lcdb.verifyInsertNull(pDT_RQMTS_ENCR, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_RQMTS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RQMTS.ID_RQMTS_STATU = " + lcdb.verifyInsertNull(pID_RQMTS_STATU, DataType.TEXT) + "\n");
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

	public String refreshHistorico(String pCD_OFMTS) {
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append(BuildSql.getExecFunction("GETAREASHIERARQUIA", "RQMTS.CD_AREAS, 1") + " AS HIERARQUIA, \n");
		query.append("  USUPA.NM_USUPA, \n");
		query.append("  INSRT.NM_USUPA AS NM_INSRT, \n");
		query.append("  " + lcdb.dateToChar("RQMTS.DT_RQMTS_REG") + " AS DT_RQMTS_REG, \n");
		query.append("  " + lcdb.dateToChar("RQMTS.DT_RQMTS_ENCR") + " AS DT_RQMTS_ENCR, \n");
		query.append("  " + lcdb.dateToChar("RQMTS.DT_RQMTS_RECBT") + " AS DT_RQMTS_RECBT, \n");
		query.append("  CASE RQMTS.ID_RQMTS_STATU \n");
		query.append("    WHEN 'R' THEN '" + user.getTermo("AGUARDMAT") + "' \n");
		query.append("    WHEN 'E' THEN '" + user.getTermo("MATENTREGUE") + "' \n");
		query.append("    ELSE '" + user.getTermo("CANCELADA") + "' \n");
		query.append("  END AS STATUS \n");
		query.append("FROM RQMTS \n");
		query.append("  LEFT JOIN USUPA ON RQMTS.CD_USUPA_RQSTE = USUPA.CD_USUPA \n");
		query.append("  LEFT JOIN USUPA INSRT ON RQMTS.CD_USUPA_INSRT = INSRT.CD_USUPA \n");
		query.append("WHERE RQMTS.CD_OFMTS = " + pCD_OFMTS + " \n");
		query.append("ORDER BY RQMTS.DT_RQMTS_REG ASC");

		return query.toString();
	}
}
