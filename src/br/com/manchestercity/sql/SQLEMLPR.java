
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLEMLPR {
	Database lcdb;
	User user;

	public SQLEMLPR(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized String insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_EMLPR, String pNM_EMLPR_ASSN, String pDS_EMLPR_CORPO, String pID_EMLPR_STATU) {
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO EMLPR (\n");
			query.append("  CD_EMLPR,\n");
			query.append("  NM_EMLPR_ASSN,\n");
			query.append("  DS_EMLPR_CORPO,\n");
			query.append("  ID_EMLPR_STATU");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_EMLPR, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_EMLPR_ASSN, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDS_EMLPR_CORPO, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_EMLPR_STATU, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return "";
		}
		return commit ? pCD_EMLPR : "";
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_EMLPR, String pNM_EMLPR_ASSN, String pDS_EMLPR_CORPO, String pID_EMLPR_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE EMLPR SET \n");

			if (pNM_EMLPR_ASSN != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  EMLPR.NM_EMLPR_ASSN = " + lcdb.verifyInsertNull(pNM_EMLPR_ASSN, DataType.TEXT));
			}

			if (pDS_EMLPR_CORPO != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  EMLPR.DS_EMLPR_CORPO = " + lcdb.verifyInsertNull(pDS_EMLPR_CORPO, DataType.TEXT));
			}

			if (pID_EMLPR_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  EMLPR.ID_EMLPR_STATU = " + lcdb.verifyInsertNull(pID_EMLPR_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE EMLPR.CD_EMLPR = " + lcdb.verifyInsertNull(pCD_EMLPR, DataType.TEXT) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_EMLPR) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM EMLPR WHERE EMLPR.CD_EMLPR = " + lcdb.verifyInsertNull(pCD_EMLPR, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_EMLPR, String pNM_EMLPR_ASSN, String pDS_EMLPR_CORPO, String pID_EMLPR_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  EMLPR.CD_EMLPR,\n");
		query.append("  EMLPR.NM_EMLPR_ASSN,\n");
		query.append("  EMLPR.DS_EMLPR_CORPO,\n");
		query.append("  EMLPR.ID_EMLPR_STATU\n");
		query.append("FROM EMLPR \n");

		if (!Database.verifyNull(pCD_EMLPR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" EMLPR.CD_EMLPR = " + lcdb.verifyInsertNull(pCD_EMLPR, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pNM_EMLPR_ASSN).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" EMLPR.NM_EMLPR_ASSN = " + lcdb.verifyInsertNull(pNM_EMLPR_ASSN, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pDS_EMLPR_CORPO).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" EMLPR.DS_EMLPR_CORPO = " + lcdb.verifyInsertNull(pDS_EMLPR_CORPO, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_EMLPR_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" EMLPR.ID_EMLPR_STATU = " + lcdb.verifyInsertNull(pID_EMLPR_STATU, DataType.TEXT) + "\n");
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

	public String refreshDestinatarios(String pAcao, String pCD_STEST, String pCD_SCMTS, String pCD_USUPA, String pCD_RSMTS) {
		StringBuffer query = new StringBuffer();

		if (pAcao.trim().startsWith("NEW_OFERTA") && pAcao.indexOf("SOLICITACAO") < 0) {
			// INTERESSES
			query.append("  SELECT NM_USUPA_EMAIL AS EMAIL FROM ITUSS \n");
			query.append("  LEFT JOIN USUPA ON ITUSS.CD_USUPA = USUPA.CD_USUPA \n");
			query.append("  LEFT JOIN AREAS ON USUPA.CD_AREAS = AREAS.CD_AREAS \n");
			query.append("  LEFT JOIN AREAS DFAUT ON DFAUT.ID_AREAS_DFAUT = 'S' \n");
			query.append("  WHERE ITUSS.CD_CTMTS = (SELECT CD_CTMTS FROM SCMTS WHERE CD_SCMTS = " + pCD_SCMTS + ")\n");
			if (pAcao.trim().equals("NEW_OFERTA"))
				query.append("  AND (AREAS.CD_STEST = " + pCD_STEST + " OR DFAUT.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + ")\n");
			else
				query.append("  AND (AREAS.CD_STEST <> " + pCD_STEST + " OR DFAUT.CD_STEST <> " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + ")\n");
			query.append("  AND (SELECT COUNT(PMSAC.CD_FNSEG) FROM LTSEG INNER JOIN PFSEG ON LTSEG.CD_PFSEG = PFSEG.CD_PFSEG INNER JOIN PMSAC ON PFSEG.CD_PFSEG = PMSAC.CD_PFSEG WHERE LTSEG.CD_USUPA = USUPA.CD_USUPA AND PMSAC.CD_FNSEG = 'adm_receber_emails' AND PMSAC.ID_PMSAC_CRUD LIKE '%R%') > 0\n");

			query.append("\n");
			query.append("  UNION ALL \n");
			query.append("\n");

			// SOLICITANTES
			query.append("  SELECT NM_USUPA_EMAIL AS EMAIL FROM SLMTS \n");
			query.append("  LEFT JOIN AREAS ON SLMTS.CD_AREAS = AREAS.CD_AREAS \n");
			query.append("  LEFT JOIN USUPA ON SLMTS.CD_USUPA = USUPA.CD_USUPA \n");
			query.append("  LEFT JOIN AREAS DFAUT ON DFAUT.ID_AREAS_DFAUT = 'S' \n");
			query.append("  WHERE CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + " AND USUPA.ID_SLMTS_STATU = 'A' \n");
			if (pAcao.trim().equals("NEW_OFERTA"))
				query.append("  AND (AREAS.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + " OR DFAUT.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + ")\n");
			else
				query.append("  AND (AREAS.CD_STEST <> " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + " OR DFAUT.CD_STEST <> " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + ")\n");
			query.append("  AND (SELECT COUNT(PMSAC.CD_FNSEG) FROM LTSEG INNER JOIN PFSEG ON LTSEG.CD_PFSEG = PFSEG.CD_PFSEG INNER JOIN PMSAC ON PFSEG.CD_PFSEG = PMSAC.CD_PFSEG WHERE LTSEG.CD_USUPA = USUPA.CD_USUPA AND PMSAC.CD_FNSEG = 'adm_receber_emails' AND PMSAC.ID_PMSAC_CRUD LIKE '%R%') > 0\n");
		}
		if (pAcao.trim().startsWith("NEW_SOLICITACAO")) {
			if (query.length() > 0) {
				query.append("\n");
				query.append("  UNION ALL \n");
				query.append("\n");
			}
			query.append("  SELECT \n");
			query.append("	USUPA.NM_USUPA_EMAIL AS EMAIL \n");
			query.append("  FROM OFMTS \n");
			query.append("  LEFT JOIN USUPA ON OFMTS.CD_USUPA_OFRTT = USUPA.CD_USUPA \n");
			query.append("  LEFT JOIN AREAS ON OFMTS.CD_AREAS_OFRTT = AREAS.CD_AREAS \n");
			query.append("  LEFT JOIN AREAS DFAUT ON DFAUT.ID_AREAS_DFAUT = 'S' \n");
			query.append("  WHERE ((OFMTS.ID_OFMTS_STATU = 'I'");
			query.append("  AND AREAS.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + ") OR (OFMTS.ID_OFMTS_STATU = 'E' AND AREAS.CD_STEST <> " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + "))\n");
			query.append("  AND OFMTS.CD_SCMTS = " + lcdb.verifyInsertNull(pCD_SCMTS, DataType.INTEGER) + " \n");
			query.append("  AND (SELECT COUNT(PMSAC.CD_FNSEG) FROM LTSEG INNER JOIN PFSEG ON LTSEG.CD_PFSEG = PFSEG.CD_PFSEG INNER JOIN PMSAC ON PFSEG.CD_PFSEG = PMSAC.CD_PFSEG WHERE LTSEG.CD_USUPA = USUPA.CD_USUPA AND PMSAC.CD_FNSEG = 'adm_receber_emails' AND PMSAC.ID_PMSAC_CRUD LIKE '%R%') > 0\n");

			query.append("\n");
			query.append("  UNION ALL \n");
			query.append("\n");

			query.append("  SELECT \n");
			query.append("  USUPA.NM_USUPA_EMAIL AS EMAIL \n");
			query.append("  FROM USUPA \n");
			query.append("  LEFT JOIN AREAS ON USUPA.CD_AREAS = AREAS.CD_AREAS \n");
			query.append("  LEFT JOIN AREAS DFAUT ON DFAUT.ID_AREAS_DFAUT = 'S' \n");
			query.append("  WHERE USUPA.ID_USUPA_STATU = 'A'\n");
			query.append("  AND (AREAS.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + "OR DFAUT.CD_STEST <> " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + ")\n");
			query.append("  AND (SELECT COUNT(PMSAC.CD_FNSEG) FROM LTSEG INNER JOIN PFSEG ON LTSEG.CD_PFSEG = PFSEG.CD_PFSEG INNER JOIN PMSAC ON PFSEG.CD_PFSEG = PMSAC.CD_PFSEG WHERE LTSEG.CD_USUPA = USUPA.CD_USUPA AND PMSAC.CD_FNSEG = 'adm_receber_emails' AND PMSAC.ID_PMSAC_CRUD LIKE '%R%') > 0\n");
		}
		if (pAcao.trim().startsWith("EXPIRED_RESERVA")) {
			if (query.length() > 0) {
				query.append("\n");
				query.append("  UNION ALL \n");
				query.append("\n");
			}
			query.append("  SELECT USUPA.NM_USUPA_EMAIL AS EMAIL FROM USUPA \n");
			query.append("  WHERE USUPA.CD_USUPA IN ( \n");
			query.append("    SELECT RSMTS.CD_USUPA_SOLCT AS CD_USUPA FROM RSMTS \n");
			query.append("    WHERE RSMTS.CD_RSMTS = " + lcdb.verifyInsertNull(pCD_RSMTS, DataType.INTEGER) + " \n");
			query.append("\n");
			query.append("    UNION ALL \n");
			query.append("\n");
			query.append("    SELECT OFMTS.CD_USUPA_OFRTT AS CD_USUPA FROM RSMTS \n");
			query.append("    LEFT JOIN OFMTS ON RSMTS.CD_OFMTS = OFMTS.CD_OFMTS \n");
			query.append("    WHERE RSMTS.CD_RSMTS = " + lcdb.verifyInsertNull(pCD_RSMTS, DataType.INTEGER) + " \n");
			query.append("  )\n");
			query.append("  AND (SELECT COUNT(PMSAC.CD_FNSEG) FROM LTSEG INNER JOIN PFSEG ON LTSEG.CD_PFSEG = PFSEG.CD_PFSEG INNER JOIN PMSAC ON PFSEG.CD_PFSEG = PMSAC.CD_PFSEG WHERE LTSEG.CD_USUPA = USUPA.CD_USUPA AND PMSAC.CD_FNSEG = 'adm_receber_emails' AND PMSAC.ID_PMSAC_CRUD LIKE '%R%') > 0\n");
		}
		if (!pCD_USUPA.trim().equals("")) {
			if (query.length() > 0) {
				query.append("\n");
				query.append("  UNION ALL \n");
				query.append("\n");
			}
			// RESERVA / REQUISICAO / CANCELAMENTO / SOLICITACAO ATENDIDA
			query.append("  SELECT NM_USUPA_EMAIL AS EMAIL FROM USUPA \n");
			query.append("  WHERE CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + " \n");
			query.append("  AND (SELECT COUNT(PMSAC.CD_FNSEG) FROM LTSEG INNER JOIN PFSEG ON LTSEG.CD_PFSEG = PFSEG.CD_PFSEG INNER JOIN PMSAC ON PFSEG.CD_PFSEG = PMSAC.CD_PFSEG WHERE LTSEG.CD_USUPA = USUPA.CD_USUPA AND PMSAC.CD_FNSEG = 'adm_receber_emails' AND PMSAC.ID_PMSAC_CRUD LIKE '%R%') > 0\n");
		}

		if (query.length() > 0)
			return "SELECT DISTINCT EMAIL FROM ( \n " + query.toString() + ") A \n";
		else
			return "";
	}

}
