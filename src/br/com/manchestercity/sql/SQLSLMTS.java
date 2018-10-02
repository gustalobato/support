
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.DateType;
import br.com.manchestercity.automacao.User;

public class SQLSLMTS {
	Database lcdb;
	User user;

	public SQLSLMTS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_SLMTS, String pCD_AREAS, String pCD_USUPA, String pCD_SCMTS, String pNO_SLMTS_QT, String pDS_SLMTS_RESM, String pDS_SLMTS_DETL, String pDT_SLMTS_REG, String pDT_SLMTS_VAL, String pDT_SLMTS_ENCR, String pID_SLMTS_STATU, String pCD_USUPA_INSRT) {
		long maxPK = lcdb.maxDB("SLMTS", "SLMTS.CD_SLMTS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO SLMTS (\n");
			query.append("  CD_SLMTS,\n");
			query.append("  CD_AREAS,\n");
			query.append("  CD_USUPA,\n");
			query.append("  CD_SCMTS,\n");
			query.append("  NO_SLMTS_QT,\n");
			query.append("  DS_SLMTS_RESM,\n");
			query.append("  DS_SLMTS_DETL,\n");
			query.append("  DT_SLMTS_REG,\n");
			query.append("  DT_SLMTS_VAL,\n");
			query.append("  DT_SLMTS_ENCR,\n");
			query.append("  ID_SLMTS_STATU,\n");
			query.append("  CD_USUPA_INSRT");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_SLMTS_QT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDS_SLMTS_RESM, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDS_SLMTS_DETL, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_SLMTS_REG, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_SLMTS_VAL, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_SLMTS_ENCR, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_SLMTS_STATU, DataType.TEXT) + ",\n");
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

	public boolean update(Connection conn, StringBuffer errors, String pCD_SLMTS, String pCD_AREAS, String pCD_USUPA, String pCD_SCMTS, String pNO_SLMTS_QT, String pDS_SLMTS_RESM, String pDS_SLMTS_DETL, String pDT_SLMTS_REG, String pDT_SLMTS_VAL, String pDT_SLMTS_ENCR, String pID_SLMTS_STATU, String pCD_USUPA_INSRT) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE SLMTS SET \n");

			if (pCD_AREAS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER));
			}

			if (pCD_USUPA != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER));
			}

			if (pCD_SCMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER));
			}

			if (pNO_SLMTS_QT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.NO_SLMTS_QT = " + lcdb.verifyInsertNull(pNO_SLMTS_QT, DataType.INTEGER));
			}

			if (pDS_SLMTS_RESM != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.DS_SLMTS_RESM = " + lcdb.verifyInsertNull(pDS_SLMTS_RESM, DataType.TEXT));
			}

			if (pDS_SLMTS_DETL != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.DS_SLMTS_DETL = " + lcdb.verifyInsertNull(pDS_SLMTS_DETL, DataType.TEXT));
			}

			if (pDT_SLMTS_REG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.DT_SLMTS_REG = " + lcdb.verifyInsertNull(pDT_SLMTS_REG, DataType.DATE));
			}

			if (pDT_SLMTS_VAL != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.DT_SLMTS_VAL = " + lcdb.verifyInsertNull(pDT_SLMTS_VAL, DataType.DATE));
			}

			if (pDT_SLMTS_ENCR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.DT_SLMTS_ENCR = " + lcdb.verifyInsertNull(pDT_SLMTS_ENCR, DataType.DATE));
			}

			if (pID_SLMTS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.ID_SLMTS_STATU = " + lcdb.verifyInsertNull(pID_SLMTS_STATU, DataType.TEXT));
			}

			if (pCD_USUPA_INSRT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  SLMTS.CD_USUPA_INSRT = " + lcdb.verifyInsertNull(pCD_USUPA_INSRT, DataType.INTEGER));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE SLMTS.CD_SLMTS = " + lcdb.verifyInsertNull(pCD_SLMTS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_SLMTS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM SLMTS WHERE SLMTS.CD_SLMTS = " + lcdb.verifyInsertNull(pCD_SLMTS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_SLMTS, String pCD_AREAS, String pCD_USUPA, String pCD_SCMTS, String pNO_SLMTS_QT, String pDS_SLMTS_RESM, String pDS_SLMTS_DETL, String pDT_SLMTS_REG, String pDT_SLMTS_VAL, String pDT_SLMTS_ENCR, String pID_SLMTS_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  SLMTS.CD_SLMTS,\n");
		query.append("  SLMTS.CD_AREAS,\n");
		query.append("  SLMTS.CD_USUPA,\n");
		query.append("  SLMTS.CD_SCMTS,\n");
		query.append("  SLMTS.NO_SLMTS_QT,\n");
		query.append("  SLMTS.DS_SLMTS_RESM,\n");
		query.append("  SLMTS.DS_SLMTS_DETL,\n");
		query.append("  " + lcdb.dateToChar("SLMTS.DT_SLMTS_REG") + " AS DT_SLMTS_REG,\n");
		query.append("  " + lcdb.dateToChar("SLMTS.DT_SLMTS_VAL") + " AS DT_SLMTS_VAL,\n");
		query.append("  " + lcdb.dateToChar("SLMTS.DT_SLMTS_ENCR") + " AS DT_SLMTS_ENCR,\n");
		query.append("  SLMTS.ID_SLMTS_STATU, \n");
		query.append("  SLMTS.CD_USUPA_INSRT, \n");
		query.append("  SCMTS.NM_SCMTS, \n");
		query.append("  SCMTS.CD_CTMTS, \n");
		query.append("  SCMTS.CD_UNMDS, \n");
		query.append("  RQMTS.CD_OFMTS \n");
		query.append("FROM SLMTS \n");
		query.append("	LEFT JOIN SCMTS ON SLMTS.CD_SCMTS = SCMTS.CD_SCMTS \n");
		query.append("	LEFT JOIN RQMTS ON SLMTS.CD_SLMTS = RQMTS.CD_SLMTS AND RQMTS.ID_RQMTS_STATU <> 'C' \n");

		if (!Database.verifyNull(pCD_SLMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.CD_SLMTS = " + lcdb.verifyInsertNull(pCD_SLMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_AREAS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_USUPA).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_SCMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNO_SLMTS_QT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.NO_SLMTS_QT = " + lcdb.verifyInsertNull(pNO_SLMTS_QT, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pDS_SLMTS_RESM).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.DS_SLMTS_RESM = " + lcdb.verifyInsertNull(pDS_SLMTS_RESM, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pDS_SLMTS_DETL).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.DS_SLMTS_DETL = " + lcdb.verifyInsertNull(pDS_SLMTS_DETL, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pDT_SLMTS_REG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.DT_SLMTS_REG = " + lcdb.verifyInsertNull(pDT_SLMTS_REG, DataType.DATE) + "\n");
		}

		if (!Database.verifyNull(pDT_SLMTS_VAL).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.DT_SLMTS_VAL = " + lcdb.verifyInsertNull(pDT_SLMTS_VAL, DataType.DATE) + "\n");
		}

		if (!Database.verifyNull(pDT_SLMTS_ENCR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.DT_SLMTS_ENCR = " + lcdb.verifyInsertNull(pDT_SLMTS_ENCR, DataType.DATE) + "\n");
		}

		if (!Database.verifyNull(pID_SLMTS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" SLMTS.ID_SLMTS_STATU = " + lcdb.verifyInsertNull(pID_SLMTS_STATU, DataType.TEXT) + "\n");
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

	public String refreshSuggestion(String pCD_SCMTS, String pCD_STEST, String pCD_USUPA) {
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  SLMTS.CD_SLMTS, \n");
		query.append("  SLMTS.DS_SLMTS_RESM, \n");
		query.append("  SLMTS.DS_SLMTS_DETL, \n");
		query.append("  SLMTS.NO_SLMTS_QT, \n");
		query.append("  " + lcdb.dateToChar("SLMTS.DT_SLMTS_VAL") + " AS DT_SLMTS_VAL, \n");
		query.append("  USUPA.NM_USUPA, \n");
		query.append("  AREAS.NM_AREAS \n");
		query.append("FROM SLMTS \n");
		query.append("LEFT JOIN AREAS ON SLMTS.CD_AREAS = AREAS.CD_AREAS \n");
		query.append("LEFT JOIN USUPA ON SLMTS.CD_USUPA = USUPA.CD_USUPA \n");
		query.append("LEFT JOIN SCMTS ON SLMTS.CD_SCMTS = SCMTS.CD_SCMTS \n");
		query.append("WHERE SLMTS.ID_SLMTS_STATU = 'A' AND AREAS.CD_STEST = " + pCD_STEST + "\n");
		query.append("  AND SLMTS.CD_SCMTS = " + pCD_SCMTS + "\n");
		query.append("  AND SLMTS.CD_USUPA <> " + pCD_USUPA + "\n");

		return query.toString();
	}

	public String refreshExpirada() {
		StringBuffer query = new StringBuffer();
		query.append(this.refresh("", "", "", "", "", "", "", "", "", "", "", ""));
		query.append("WHERE SLMTS.ID_SLMTS_STATU = 'A' AND SLMTS.DT_SLMTS_VAL < " + lcdb.charToDate(lcdb.verifyInsertNull(lcdb.getActualDate(DateType.DATE), DataType.TEXT)));

		return query.toString();
	}

	public String refreshHistory(String dias) {
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  SLMTS.CD_SLMTS \n");
		query.append("FROM( \n");
		query.append("  SELECT \n");
		query.append("    CD_SLMTS, \n");
		query.append("    DT_SLMTS_ENCR, \n");
		query.append("    ID_SLMTS_STATU, \n");
		query.append("    (SELECT COUNT(1)QTDE_RES FROM RSMTS WHERE CD_SLMTS = SLMTS.CD_SLMTS) AS QTDE_RSMTS, \n");
		query.append("    (SELECT COUNT(1)QTDE_REQ FROM RQMTS WHERE CD_SLMTS = SLMTS.CD_SLMTS) AS QTDE_RQMTS  \n");
		query.append("  FROM SLMTS \n");
		query.append(")SLMTS \n");
		query.append("WHERE ((SLMTS.ID_SLMTS_STATU = 'E' AND QTDE_RSMTS = 0 AND QTDE_RQMTS = 0) OR (SLMTS.ID_SLMTS_STATU IN('C','V'))) \n");
		query.append(" AND " + BuildSql.getDateDiff(lcdb.charToDate("'" + lcdb.getActualDate(DateType.DATE) + "'"), "SLMTS.DT_SLMTS_ENCR") + " > " + dias);

		return query.toString();
	}

}
