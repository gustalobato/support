
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.DateType;
import br.com.manchestercity.automacao.User;

public class SQLOFMTS {
	Database lcdb;
	User user;

	public SQLOFMTS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_OFMTS, String pCD_SCMTS, String pCD_AREAS_OFRTT, String pCD_USUPA_OFRTT, String pCD_USUPA_ENCR, String pDS_OFMTS_RESM, String pDS_OFMTS_DETL, String pNO_OFMTS_QT, String pVL_OFMTS_MOEDA_NAC_SUGR, String pVL_OFMTS_MOEDA_NAC_OFI, String pID_OFMTS_STATU, String pID_OFMTS_DEST, String pDT_OFMTS_REG, String pDT_OFMTS_FIM_INTR, String pDT_OFMTS_FIM_EXTER, String pDT_OFMTS_ENCR, String pCD_OFMTS_SAP, String pCD_USUPA_INSRT, String pVL_OFMTS_PERCT_RQSTE, String pVL_OFMTS_PERCT_OFRTT) {
		long maxPK = lcdb.maxDB("OFMTS", "OFMTS.CD_OFMTS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO OFMTS (\n");
			query.append("  CD_OFMTS,\n");
			query.append("  CD_SCMTS,\n");
			query.append("  CD_AREAS_OFRTT,\n");
			query.append("  CD_USUPA_OFRTT,\n");
			query.append("  CD_USUPA_ENCR,\n");
			query.append("  DS_OFMTS_RESM,\n");
			query.append("  DS_OFMTS_DETL,\n");
			query.append("  NO_OFMTS_QT,\n");
			query.append("  VL_OFMTS_MOEDA_NAC_SUGR,\n");
			query.append("  VL_OFMTS_MOEDA_NAC_OFI,\n");
			query.append("  ID_OFMTS_STATU,\n");
			query.append("  ID_OFMTS_DEST,\n");
			query.append("  DT_OFMTS_REG,\n");
			query.append("  DT_OFMTS_FIM_INTR,\n");
			query.append("  DT_OFMTS_FIM_EXTER,\n");
			query.append("  DT_OFMTS_ENCR,\n");
			query.append("  CD_OFMTS_SAP,\n");
			query.append("  CD_USUPA_INSRT,\n");
			query.append("  VL_OFMTS_PERCT_RQSTE,\n");
			query.append("  VL_OFMTS_PERCT_OFRTT");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_AREAS_OFRTT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_OFRTT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_ENCR, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDS_OFMTS_RESM, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDS_OFMTS_DETL, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_OFMTS_QT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pVL_OFMTS_MOEDA_NAC_SUGR, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pVL_OFMTS_MOEDA_NAC_OFI, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_OFMTS_STATU, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_OFMTS_DEST, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_OFMTS_REG, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_OFMTS_FIM_INTR, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_OFMTS_FIM_EXTER, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDT_OFMTS_ENCR, DataType.DATE) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_OFMTS_SAP, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_USUPA_INSRT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pVL_OFMTS_PERCT_RQSTE, DataType.DECIMAL) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pVL_OFMTS_PERCT_OFRTT, DataType.DECIMAL) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_OFMTS, String pCD_SCMTS, String pCD_AREAS_OFRTT, String pCD_USUPA_OFRTT, String pCD_USUPA_ENCR, String pDS_OFMTS_RESM, String pDS_OFMTS_DETL, String pNO_OFMTS_QT, String pVL_OFMTS_MOEDA_NAC_SUGR, String pVL_OFMTS_MOEDA_NAC_OFI, String pID_OFMTS_STATU, String pID_OFMTS_DEST, String pDT_OFMTS_REG, String pDT_OFMTS_FIM_INTR, String pDT_OFMTS_FIM_EXTER, String pDT_OFMTS_ENCR, String pCD_OFMTS_SAP, String pCD_USUPA_INSRT, String pVL_OFMTS_PERCT_RQSTE, String pVL_OFMTS_PERCT_OFRTT) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE OFMTS SET \n");

			if (pCD_SCMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER));
			}

			if (pCD_AREAS_OFRTT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.CD_AREAS_OFRTT = " + lcdb.verifyInsertNull(pCD_AREAS_OFRTT, DataType.INTEGER));
			}

			if (pCD_USUPA_OFRTT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.CD_USUPA_OFRTT = " + lcdb.verifyInsertNull(pCD_USUPA_OFRTT, DataType.INTEGER));
			}

			if (pCD_USUPA_ENCR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.CD_USUPA_ENCR = " + lcdb.verifyInsertNull(pCD_USUPA_ENCR, DataType.INTEGER));
			}

			if (pDS_OFMTS_RESM != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.DS_OFMTS_RESM = " + lcdb.verifyInsertNull(pDS_OFMTS_RESM, DataType.TEXT));
			}

			if (pDS_OFMTS_DETL != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.DS_OFMTS_DETL = " + lcdb.verifyInsertNull(pDS_OFMTS_DETL, DataType.TEXT));
			}

			if (pNO_OFMTS_QT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.NO_OFMTS_QT = " + lcdb.verifyInsertNull(pNO_OFMTS_QT, DataType.INTEGER));
			}

			if (pVL_OFMTS_MOEDA_NAC_SUGR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.VL_OFMTS_MOEDA_NAC_SUGR = " + lcdb.verifyInsertNull(pVL_OFMTS_MOEDA_NAC_SUGR, DataType.INTEGER));
			}

			if (pVL_OFMTS_MOEDA_NAC_OFI != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.VL_OFMTS_MOEDA_NAC_OFI = " + lcdb.verifyInsertNull(pVL_OFMTS_MOEDA_NAC_OFI, DataType.INTEGER));
			}

			if (pID_OFMTS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.ID_OFMTS_STATU = " + lcdb.verifyInsertNull(pID_OFMTS_STATU, DataType.TEXT));
			}

			if (pID_OFMTS_DEST != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.ID_OFMTS_DEST = " + lcdb.verifyInsertNull(pID_OFMTS_DEST, DataType.TEXT));
			}

			if (pDT_OFMTS_REG != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.DT_OFMTS_REG = " + lcdb.verifyInsertNull(pDT_OFMTS_REG, DataType.DATE));
			}

			if (pDT_OFMTS_FIM_INTR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.DT_OFMTS_FIM_INTR = " + lcdb.verifyInsertNull(pDT_OFMTS_FIM_INTR, DataType.DATE));
			}

			if (pDT_OFMTS_FIM_EXTER != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.DT_OFMTS_FIM_EXTER = " + lcdb.verifyInsertNull(pDT_OFMTS_FIM_EXTER, DataType.DATE));
			}

			if (pDT_OFMTS_ENCR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.DT_OFMTS_ENCR = " + lcdb.verifyInsertNull(pDT_OFMTS_ENCR, DataType.DATE));
			}

			if (pCD_OFMTS_SAP != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.CD_OFMTS_SAP = " + lcdb.verifyInsertNull(pCD_OFMTS_SAP, DataType.TEXT));
			}

			if (pCD_USUPA_INSRT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.CD_USUPA_INSRT = " + lcdb.verifyInsertNull(pCD_USUPA_INSRT, DataType.INTEGER));
			}

			if (pVL_OFMTS_PERCT_RQSTE != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.VL_OFMTS_PERCT_RQSTE = " + lcdb.verifyInsertNull(pVL_OFMTS_PERCT_RQSTE, DataType.DECIMAL));
			}

			if (pVL_OFMTS_PERCT_OFRTT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  OFMTS.VL_OFMTS_PERCT_OFRTT = " + lcdb.verifyInsertNull(pVL_OFMTS_PERCT_OFRTT, DataType.DECIMAL));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE OFMTS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_OFMTS) {
		boolean commit = false;
		try {
			new SQLANXOS(user).deleteByOFMTS(conn, errors, pCD_OFMTS);
			new SQLRQMTS(user).deleteByOFMTS(conn, errors, pCD_OFMTS);
			new SQLRSMTS(user).deleteByOFMTS(conn, errors, pCD_OFMTS);
			commit = lcdb.executeQuery("DELETE FROM OFMTS WHERE OFMTS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_OFMTS, String pCD_SCMTS, String pCD_AREAS_OFRTT, String pCD_USUPA_OFRTT, String pCD_USUPA_ENCR, String pDS_OFMTS_RESM, String pDS_OFMTS_DETL, String pNO_OFMTS_QT, String pVL_OFMTS_MOEDA_NAC_SUGR, String pVL_OFMTS_MOEDA_NAC_OFI, String pID_OFMTS_STATU, String pID_OFMTS_DEST, String pDT_OFMTS_REG, String pDT_OFMTS_FIM_INTR, String pDT_OFMTS_FIM_EXTER, String pDT_OFMTS_ENCR, String pCD_OFMTS_SAP, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  OFMTS.CD_OFMTS,\n");
		query.append("  OFMTS.CD_SCMTS,\n");
		query.append("  OFMTS.CD_AREAS_OFRTT,\n");
		query.append("  OFMTS.CD_USUPA_OFRTT,\n");
		query.append("  OFMTS.CD_USUPA_ENCR,\n");
		query.append("  OFMTS.DS_OFMTS_RESM,\n");
		query.append("  OFMTS.DS_OFMTS_DETL,\n");
		query.append("  OFMTS.NO_OFMTS_QT,\n");
		query.append("  OFMTS.VL_OFMTS_MOEDA_NAC_SUGR,\n");
		query.append("  OFMTS.VL_OFMTS_MOEDA_NAC_OFI,\n");
		query.append("  OFMTS.ID_OFMTS_STATU,\n");
		query.append("  OFMTS.ID_OFMTS_DEST,\n");
		query.append("  " + lcdb.dateToChar("OFMTS.DT_OFMTS_REG") + " AS DT_OFMTS_REG,\n");
		query.append("  " + lcdb.dateToChar("OFMTS.DT_OFMTS_FIM_INTR") + " AS DT_OFMTS_FIM_INTR,\n");
		query.append("  " + lcdb.dateToChar("OFMTS.DT_OFMTS_FIM_EXTER") + " AS DT_OFMTS_FIM_EXTER,\n");
		query.append("  " + lcdb.dateToChar("OFMTS.DT_OFMTS_ENCR") + " AS DT_OFMTS_ENCR,\n");
		query.append("  OFMTS.CD_OFMTS_SAP,\n");
		query.append("  OFMTS.CD_USUPA_INSRT,\n");
		query.append("  SCMTS.NM_SCMTS,\n");
		query.append("  SCMTS.CD_CTMTS,\n");
		query.append("  SCMTS.CD_UNMDS,\n");
		query.append("  RSMTS.CD_SLMTS,\n");
		query.append("  OFMTS.VL_OFMTS_PERCT_RQSTE,\n");
		query.append("  OFMTS.VL_OFMTS_PERCT_OFRTT,\n");
		query.append("  OFMTS.ID_OFMTS_NOVO \n");
		query.append("FROM OFMTS \n");
		query.append("  LEFT JOIN SCMTS ON (OFMTS.CD_SCMTS = SCMTS.CD_SCMTS)\n");
		query.append("  LEFT JOIN RSMTS ON (OFMTS.CD_OFMTS = RSMTS.CD_OFMTS AND RSMTS.ID_RSMTS_STATU = 'A')\n");

		if (!Database.verifyNull(pCD_OFMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_SCMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_AREAS_OFRTT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.CD_AREAS_OFRTT = " + lcdb.verifyInsertNull(pCD_AREAS_OFRTT, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_USUPA_OFRTT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.CD_USUPA_OFRTT = " + lcdb.verifyInsertNull(pCD_USUPA_OFRTT, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_USUPA_ENCR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.CD_USUPA_ENCR = " + lcdb.verifyInsertNull(pCD_USUPA_ENCR, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pDS_OFMTS_RESM).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.DS_OFMTS_RESM = " + lcdb.verifyInsertNull(pDS_OFMTS_RESM, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pDS_OFMTS_DETL).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.DS_OFMTS_DETL = " + lcdb.verifyInsertNull(pDS_OFMTS_DETL, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pNO_OFMTS_QT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.NO_OFMTS_QT = " + lcdb.verifyInsertNull(pNO_OFMTS_QT, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pVL_OFMTS_MOEDA_NAC_SUGR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.VL_OFMTS_MOEDA_NAC_SUGR = " + lcdb.verifyInsertNull(pVL_OFMTS_MOEDA_NAC_SUGR, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pVL_OFMTS_MOEDA_NAC_OFI).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.VL_OFMTS_MOEDA_NAC_OFI = " + lcdb.verifyInsertNull(pVL_OFMTS_MOEDA_NAC_OFI, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pID_OFMTS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.ID_OFMTS_STATU = " + lcdb.verifyInsertNull(pID_OFMTS_STATU, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_OFMTS_DEST).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.ID_OFMTS_DEST = " + lcdb.verifyInsertNull(pID_OFMTS_DEST, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pDT_OFMTS_REG).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.DT_OFMTS_REG = " + lcdb.verifyInsertNull(pDT_OFMTS_REG, DataType.DATE) + "\n");
		}

		if (!Database.verifyNull(pDT_OFMTS_FIM_INTR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.DT_OFMTS_FIM_INTR = " + lcdb.verifyInsertNull(pDT_OFMTS_FIM_INTR, DataType.DATE) + "\n");
		}

		if (!Database.verifyNull(pDT_OFMTS_FIM_EXTER).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.DT_OFMTS_FIM_EXTER = " + lcdb.verifyInsertNull(pDT_OFMTS_FIM_EXTER, DataType.DATE) + "\n");
		}

		if (!Database.verifyNull(pDT_OFMTS_ENCR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.DT_OFMTS_ENCR = " + lcdb.verifyInsertNull(pDT_OFMTS_ENCR, DataType.DATE) + "\n");
		}

		if (!Database.verifyNull(pCD_OFMTS_SAP).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" OFMTS.CD_OFMTS_SAP = " + lcdb.verifyInsertNull(pCD_OFMTS_SAP, DataType.TEXT) + "\n");
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

	public String refreshResumo(String pCD_OFMTS, String pCD_RSMTS) {
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  OFMTS.CD_OFMTS, \n");
		query.append("  OFMTS.CD_OFMTS_SAP, \n");
		query.append("    OFMTS.CD_USUPA_OFRTT, \n");
		query.append("    OFMTS.CD_AREAS_OFRTT, \n");
		query.append("    AREAS.CD_STEST,       \n");
		query.append("    OFMTS.ID_OFMTS_STATU, \n");
		query.append("  CASE OFMTS.ID_OFMTS_STATU \n");
		query.append("    WHEN 'I' THEN '" + user.getTermo("INTERNA") + "'\n");
		query.append("    WHEN 'E' THEN '" + user.getTermo("EXTERNA") + "' \n");
		query.append("    WHEN 'R' THEN '" + user.getTermo("RESERVADA") + "' \n");
		query.append("    WHEN 'Q' THEN '" + user.getTermo("REQUISITADA") + "' \n");
		query.append("    WHEN 'V' THEN '" + user.getTermo("EXPIRADA") + "' \n");
		query.append("    ELSE '" + user.getTermo("FINALIZADA") + "' \n");
		query.append("  END AS STATUS, \n");
		query.append("  USUPA.NM_USUPA, \n");
		query.append("  USUPA.NM_USUPA_EMAIL,\n");
		query.append("  USUPA.NO_USUPA_RAMAL,\n");
		query.append("  U_REG.NM_USUPA REG_NM_USUPA, \n");
		query.append("  GETAREASHIERARQUIA(OFMTS.CD_AREAS_OFRTT, 1) HIERARQUIA_AREA, \n");
		query.append("  AREAS.NM_AREAS, \n");
		query.append("  " + lcdb.dateToChar("OFMTS.DT_OFMTS_REG") + " AS DT_OFMTS_REG, \n");
		query.append("  " + lcdb.dateToChar("OFMTS.DT_OFMTS_FIM_INTR") + " AS DT_OFMTS_FIM_INTR, \n");
		query.append("  " + lcdb.dateToChar("OFMTS.DT_OFMTS_FIM_EXTER") + " AS DT_OFMTS_FIM_EXTER, \n");
		query.append("  " + lcdb.dateToChar("OFMTS.DT_OFMTS_ENCR") + " AS DT_OFMTS_ENCR, \n");
		query.append("  " + lcdb.dateToChar("RSMTS.DT_RSMTS_REG") + " AS DT_RSMTS_REG, \n");
		query.append("  " + lcdb.dateToChar("RSMTS.DT_RSMTS_VAL") + " AS DT_RSMTS_VAL, \n");
		query.append("  " + lcdb.dateToChar("RQMTS.DT_RQMTS_REG") + " AS DT_RQMTS_REG, \n");
		query.append("  CTMTS.NM_CTMTS, \n");
		query.append("  SCMTS.NM_SCMTS, \n");
		query.append("  UNMDS.NM_UNMDS, \n");
		query.append("  OFMTS.DS_OFMTS_RESM, \n");
		query.append("  OFMTS.DS_OFMTS_DETL, \n");
		query.append("  OFMTS.NO_OFMTS_QT, \n");
		query.append("  OFMTS.VL_OFMTS_MOEDA_NAC_OFI, \n");
		query.append("  OFMTS.VL_OFMTS_MOEDA_NAC_SUGR, \n");
		query.append("  OFMTS.NO_OFMTS_QT * NVL(OFMTS.VL_OFMTS_MOEDA_NAC_OFI, OFMTS.VL_OFMTS_MOEDA_NAC_SUGR) AS VL_OFMTS_TOTAL, \n");
		query.append("  OFMTS.ID_OFMTS_DEST, \n");
		query.append("  OFMTS.ID_OFMTS_NOVO, \n");
		query.append("  RSMTS.CD_RSMTS, \n");
		query.append("  RSMTS.CD_USUPA_SOLCT, \n");
		query.append("  RSMTS.ID_RSMTS_STATU, \n");
		query.append("  RQMTS.CD_RQMTS, \n");
		query.append("  RQMTS.CD_USUPA_RQSTE, \n");
		query.append("  RQMTS.ID_RQMTS_STATU, \n");
		query.append("  CASE WHEN CASE OFMTS.ID_OFMTS_STATU \n");
		query.append("              WHEN 'R' THEN RSMTS.DT_RSMTS_REG \n");
		query.append("              WHEN 'Q' THEN RQMTS.DT_RQMTS_REG \n");
		query.append("              ELSE " + lcdb.charToDate(lcdb.verifyInsertNull(lcdb.getActualDate(DateType.DATE), DataType.TEXT)) + " \n");
		query.append("            END BETWEEN OFMTS.DT_OFMTS_REG AND OFMTS.DT_OFMTS_FIM_INTR THEN 'I' \n");
		query.append("       ELSE 'E' \n");
		query.append("  END AS LOCAL_OFERTA, \n");
		query.append("  SLMTS.CD_SLMTS, \n");
		query.append("  " + lcdb.dateToChar("SLMTS.DT_SLMTS_VAL") + " AS DT_SLMTS_VAL \n");
		query.append("FROM OFMTS \n");
		query.append("LEFT JOIN SCMTS ON OFMTS.CD_SCMTS = SCMTS.CD_SCMTS \n");
		query.append("LEFT JOIN CTMTS ON SCMTS.CD_CTMTS = CTMTS.CD_CTMTS \n");
		query.append("LEFT JOIN UNMDS ON SCMTS.CD_UNMDS = UNMDS.CD_UNMDS \n");
		query.append("LEFT JOIN USUPA ON OFMTS.CD_USUPA_OFRTT = USUPA.CD_USUPA \n");
		query.append("LEFT JOIN AREAS ON OFMTS.CD_AREAS_OFRTT = AREAS.CD_AREAS \n");
		query.append("LEFT JOIN USUPA U_REG ON OFMTS.CD_USUPA_INSRT = U_REG.CD_USUPA \n");
		query.append("LEFT JOIN RSMTS ON OFMTS.CD_OFMTS = RSMTS.CD_OFMTS" + ( pCD_RSMTS.trim().equals("") ? " AND RSMTS.ID_RSMTS_STATU in('A','E')" : "" ) + " \n");
		query.append("LEFT JOIN RQMTS ON OFMTS.CD_OFMTS = RQMTS.CD_OFMTS AND RQMTS.ID_RQMTS_STATU <> 'C' \n");
		query.append("LEFT JOIN SLMTS ON (OFMTS.ID_OFMTS_STATU = 'R' AND RSMTS.CD_SLMTS = SLMTS.CD_SLMTS) OR (OFMTS.ID_OFMTS_STATU = 'Q' AND RQMTS.CD_SLMTS = SLMTS.CD_SLMTS) \n");
		query.append("	WHERE OFMTS.CD_OFMTS = " + pCD_OFMTS + ( !pCD_RSMTS.trim().equals("") ? " AND RSMTS.CD_RSMTS = " + pCD_RSMTS.trim() : "" ));

		return query.toString();
	}

	public String refreshSuggestion(String pCD_SCMTS, String pCD_STEST, String pCD_USUPA) {
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("	OFMTS.CD_OFMTS, \n");
		query.append("	'<img src='''" + BuildSql.getConcatCharacter() + "CASE WHEN NOT ANXOS.CD_ANXOS IS NULL THEN 'showimage?imgTable=ANXOS&imgColumn=AQ_ANXOS&keyColumn=CD_ANXOS&keyValue='" + BuildSql.getConcatCharacter() + BuildSql.getNvl("ANXOS.CD_ANXOS", "''") + BuildSql.getConcatCharacter() + "'&thumb=yes' ELSE 'isg/images/logos/semimagem_thumb.png' END " + BuildSql.getConcatCharacter() + "''' />' AS IMG, \n");
		query.append("	OFMTS.DS_OFMTS_RESM, \n");
		query.append("	OFMTS.DS_OFMTS_DETL, \n");
		query.append("	OFMTS.NO_OFMTS_QT, \n");
		query.append("	CASE WHEN OFMTS.ID_OFMTS_STATU = 'I' THEN " + lcdb.dateToChar("OFMTS.DT_OFMTS_FIM_INTR") + " ELSE " + lcdb.dateToChar("OFMTS.DT_OFMTS_FIM_EXTER") + " END AS VALIDADE, \n");
		query.append("	AREAS.NM_AREAS, \n");
		query.append("	USUPA.NM_USUPA, \n");
		query.append("	SCMTS.NM_SCMTS \n");
		query.append("FROM (SELECT \n");
		query.append("    OFMTS.*, \n");
		query.append("    (select ANXOS.CD_ANXOS FROM ANXOS WHERE ANXOS.CD_OFMTS = OFMTS.CD_OFMTS AND ROWNUM = 1) AS CD_ANXOS \n");
		query.append("  FROM OFMTS \n");
		query.append("  ) OFMTS \n");
		query.append("LEFT JOIN SCMTS ON OFMTS.CD_SCMTS = SCMTS.CD_SCMTS \n");
		query.append("LEFT JOIN AREAS ON OFMTS.CD_AREAS_OFRTT = AREAS.CD_AREAS \n");
		query.append("LEFT JOIN USUPA ON OFMTS.CD_USUPA_OFRTT = USUPA.CD_USUPA \n");
		query.append("LEFT JOIN ANXOS ON OFMTS.CD_ANXOS = ANXOS.CD_ANXOS \n");
		query.append("WHERE ((OFMTS.ID_OFMTS_STATU = 'I' AND AREAS.CD_STEST = " + pCD_STEST + ") OR (OFMTS.ID_OFMTS_STATU = 'E' AND AREAS.CD_STEST <> " + pCD_STEST + ")) \n");
		query.append(" AND OFMTS.CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + "\n");
		query.append(" AND OFMTS.CD_USUPA_OFRTT <> " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "\n");

		return query.toString();
	}

	public String refreshToExterna() {
		StringBuffer query = new StringBuffer();
		query.append(this.refresh("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
		query.append("WHERE OFMTS.ID_OFMTS_STATU = 'I' AND OFMTS.DT_OFMTS_FIM_INTR < " + lcdb.charToDate(lcdb.verifyInsertNull(lcdb.getActualDate(DateType.DATE), DataType.TEXT)));

		return query.toString();
	}

	public String refreshExpirada() {
		StringBuffer query = new StringBuffer();
		query.append(this.refresh("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
		query.append("WHERE OFMTS.ID_OFMTS_STATU = 'E' AND OFMTS.DT_OFMTS_FIM_EXTER < " + lcdb.charToDate(lcdb.verifyInsertNull(lcdb.getActualDate(DateType.DATE), DataType.TEXT)));

		return query.toString();
	}

	public String refreshHistory(String dias) {
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  OFMTS.CD_OFMTS \n");
		query.append("FROM OFMTS \n");
		query.append("WHERE " + BuildSql.getDateDiff(lcdb.charToDate("'" + lcdb.getActualDate(DateType.DATE) + "'"), "OFMTS.DT_OFMTS_ENCR") + " > " + dias);

		return query.toString();
	}
}
