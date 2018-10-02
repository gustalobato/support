
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.DateType;
import br.com.manchestercity.automacao.User;

public class SQLRSMTS {
	Database lcdb;
	User user;

	public SQLRSMTS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_RSMTS, String pCD_OFMTS, String pCD_AREAS, String pCD_USUPA_SOLCT, String pCD_USUPA_ENCR, String pCD_SLMTS, String pNO_RSMTS_QT, String pDT_RSMTS_REG, String pDT_RSMTS_VAL, String pDT_RSMTS_ENCR, String pID_RSMTS_STATU, String pCD_USUPA_INSRT) {
		long maxPK = lcdb.maxDB("RSMTS", "RSMTS.CD_RSMTS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO RSMTS (\n");
			query.append("  CD_RSMTS,\n");
			query.append("  CD_OFMTS,\n");
			query.append("  CD_AREAS,\n");
			query.append("  CD_USUPA_SOLCT,\n");
			query.append("  CD_USUPA_ENCR,\n");
			query.append("  CD_SLMTS,\n");
			query.append("  NO_RSMTS_QT,\n");
			query.append("  DT_RSMTS_REG,\n");
			query.append("  DT_RSMTS_VAL,\n");
			query.append("  DT_RSMTS_ENCR,\n");
			query.append("  ID_RSMTS_STATU,\n");
			query.append("  CD_USUPA_INSRT");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_SOLCT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_ENCR, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_SLMTS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_RSMTS_QT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_RSMTS_REG, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_RSMTS_VAL, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_RSMTS_ENCR, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_RSMTS_STATU, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_INSRT, DataType.INTEGER) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_RSMTS, String pCD_OFMTS, String pCD_AREAS, String pCD_USUPA_SOLCT, String pCD_USUPA_ENCR, String pCD_SLMTS, String pNO_RSMTS_QT, String pDT_RSMTS_REG, String pDT_RSMTS_VAL, String pDT_RSMTS_ENCR, String pID_RSMTS_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE RSMTS SET \n");

			if (pCD_OFMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER));
			}

			if (pCD_AREAS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER));
			}

			if (pCD_USUPA_SOLCT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.CD_USUPA_SOLCT = " + lcdb.verifyInsertNull(pCD_USUPA_SOLCT, DataType.INTEGER));
			}

			if (pCD_USUPA_ENCR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.CD_USUPA_ENCR = " + lcdb.verifyInsertNull(pCD_USUPA_ENCR, DataType.INTEGER));
			}

			if (pCD_SLMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.CD_SLMTS = " + lcdb.verifyInsertNull(pCD_SLMTS, DataType.INTEGER));
			}

			if (pNO_RSMTS_QT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.NO_RSMTS_QT = " + lcdb.verifyInsertNull(pNO_RSMTS_QT, DataType.INTEGER));
			}

			if (pDT_RSMTS_REG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.DT_RSMTS_REG = " + lcdb.verifyInsertNull(pDT_RSMTS_REG, DataType.TEXT));
			}

			if (pDT_RSMTS_VAL != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.DT_RSMTS_VAL = " + lcdb.verifyInsertNull(pDT_RSMTS_VAL, DataType.DATE));
			}

			if (pDT_RSMTS_ENCR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.DT_RSMTS_ENCR = " + lcdb.verifyInsertNull(pDT_RSMTS_ENCR, DataType.DATE));
			}

			if (pID_RSMTS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  RSMTS.ID_RSMTS_STATU = " + lcdb.verifyInsertNull(pID_RSMTS_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE RSMTS.CD_RSMTS = " + lcdb.verifyInsertNull(pCD_RSMTS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_RSMTS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM RSMTS WHERE RSMTS.CD_RSMTS = " + lcdb.verifyInsertNull(pCD_RSMTS, DataType.INTEGER) + "", conn);
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
			commit = lcdb.executeQuery("DELETE FROM RSMTS WHERE RSMTS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_RSMTS, String pCD_OFMTS, String pCD_AREAS, String pCD_USUPA_SOLCT, String pCD_USUPA_ENCR, String pCD_SLMTS, String pNO_RSMTS_QT, String pDT_RSMTS_REG, String pDT_RSMTS_VAL, String pDT_RSMTS_ENCR, String pID_RSMTS_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  RSMTS.CD_RSMTS,\n");
		query.append("  RSMTS.CD_OFMTS,\n");
		query.append("  RSMTS.CD_AREAS,\n");
		query.append("  RSMTS.CD_USUPA_SOLCT,\n");
		query.append("  RSMTS.CD_USUPA_ENCR,\n");
		query.append("  RSMTS.CD_SLMTS,\n");
		query.append("  RSMTS.NO_RSMTS_QT,\n");
		query.append("  " + lcdb.dateToChar("RSMTS.DT_RSMTS_REG") + " AS DT_RSMTS_REG,\n");
		query.append("  " + lcdb.dateToChar("RSMTS.DT_RSMTS_VAL") + " AS DT_RSMTS_VAL,\n");
		query.append("  RSMTS.DT_RSMTS_ENCR,\n");
		query.append("  RSMTS.ID_RSMTS_STATU,\n");
		query.append("  RSMTS.CD_USUPA_INSRT \n");
		query.append("FROM RSMTS \n");

		if (!Database.verifyNull(pCD_RSMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.CD_RSMTS = " + lcdb.verifyInsertNull(pCD_RSMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_OFMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_AREAS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_USUPA_SOLCT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.CD_USUPA_SOLCT = " + lcdb.verifyInsertNull(pCD_USUPA_SOLCT, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_USUPA_ENCR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.CD_USUPA_ENCR = " + lcdb.verifyInsertNull(pCD_USUPA_ENCR, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_SLMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.CD_SLMTS = " + lcdb.verifyInsertNull(pCD_SLMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNO_RSMTS_QT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.NO_RSMTS_QT = " + lcdb.verifyInsertNull(pNO_RSMTS_QT, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pDT_RSMTS_REG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.DT_RSMTS_REG = " + lcdb.verifyInsertNull(pDT_RSMTS_REG, DataType.DATE) + "\n");
		}

		if (!Database.verifyNull(pDT_RSMTS_VAL).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.DT_RSMTS_VAL = " + lcdb.verifyInsertNull(pDT_RSMTS_VAL, DataType.DATE) + "\n");
		}

		if (!Database.verifyNull(pDT_RSMTS_ENCR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.DT_RSMTS_ENCR = " + lcdb.verifyInsertNull(pDT_RSMTS_ENCR, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_RSMTS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" RSMTS.ID_RSMTS_STATU = " + lcdb.verifyInsertNull(pID_RSMTS_STATU, DataType.TEXT) + "\n");
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
		query.append(BuildSql.getExecFunction("GETAREASHIERARQUIA", "RSMTS.CD_AREAS, 1") + " AS HIERARQUIA, \n");
		query.append("  USUPA.NM_USUPA, \n");
		query.append("  INSRT.NM_USUPA AS NM_INSRT, \n");
		query.append("  " + lcdb.dateToChar("RSMTS.DT_RSMTS_REG") + " AS DT_RSMTS_REG, \n");
		query.append("  CASE RSMTS.ID_RSMTS_STATU \n");
		query.append("    WHEN 'A' THEN '" + user.getTermo("ATIVA") + "' \n");
		query.append("    WHEN 'V' THEN '" + user.getTermo("EXPIRADA") + "' \n");
		query.append("    WHEN 'E' THEN '" + user.getTermo("EFETIVADA") + "' \n");
		query.append("    ELSE '" + user.getTermo("CANCELADA") + "' \n");
		query.append("  END AS STATUS, \n");
		query.append("  " + lcdb.dateToChar("RSMTS.DT_RSMTS_VAL") + " AS  DT_RSMTS_VAL, \n");
		query.append("  " + lcdb.dateToChar("RSMTS.DT_RSMTS_ENCR") + " AS  DT_RSMTS_ENCR, \n");
		query.append("  RSMTS.NO_RSMTS_QT \n");
		query.append("FROM RSMTS \n");
		query.append("  LEFT JOIN USUPA ON RSMTS.CD_USUPA_SOLCT = USUPA.CD_USUPA \n");
		query.append("  LEFT JOIN USUPA INSRT ON RSMTS.CD_USUPA_INSRT = INSRT.CD_USUPA \n");
		query.append("WHERE RSMTS.CD_OFMTS = " + pCD_OFMTS + "  \n");
		query.append("ORDER BY RSMTS.DT_RSMTS_REG ASC, RSMTS.DT_RSMTS_ENCR ASC  \n");

		return query.toString();
	}

	public String refreshExpirada() {
		StringBuffer query = new StringBuffer();
		query.append(this.refresh("", "", "", "", "", "", "", "", "", "", "", ""));
		query.append("WHERE RSMTS.ID_RSMTS_STATU = 'A' AND RSMTS.DT_RSMTS_VAL < " + lcdb.charToDate(lcdb.verifyInsertNull(lcdb.getActualDate(DateType.DATE), DataType.TEXT)));

		return query.toString();
	}
}
