
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLPRSTS {
	Database lcdb;
	User user;

	public SQLPRSTS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_PRSTS, String pCD_AREAS_ENCR_OFERT, String pCD_AREAS_VALD_VL_MATRL, String pVL_PRSTS_AREA_RQSTE, String pVL_PRSTS_AREA_OFRTT, String pNO_PRSTS_OFERT_INTR, String pNO_PRSTS_OFERT_EXTER, String pNO_PRSTS_RESRV, String pNO_PRSTS_SOL, String pNO_PRSTS_HIST_ANEXO, String pNO_PRSTS_HIST_OFERT) {
		long maxPK = lcdb.maxDB("PRSTS", "PRSTS.CD_PRSTS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO PRSTS (\n");
			query.append("  CD_PRSTS,\n");
			query.append("  CD_AREAS_ENCR_OFERT,\n");
			query.append("  CD_AREAS_VALD_VL_MATRL,\n");
			query.append("  VL_PRSTS_AREA_RQSTE,\n");
			query.append("  VL_PRSTS_AREA_OFRTT,\n");
			query.append("  NO_PRSTS_OFERT_INTR,\n");
			query.append("  NO_PRSTS_OFERT_EXTER,\n");
			query.append("  NO_PRSTS_RESRV,\n");
			query.append("  NO_PRSTS_SOL,\n");
			query.append("  NO_PRSTS_HIST_ANEXO,\n");
			query.append("  NO_PRSTS_HIST_OFERT");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_AREAS_ENCR_OFERT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_AREAS_VALD_VL_MATRL, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pVL_PRSTS_AREA_RQSTE, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pVL_PRSTS_AREA_OFRTT, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_PRSTS_OFERT_INTR, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_PRSTS_OFERT_EXTER, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_PRSTS_RESRV, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_PRSTS_SOL, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_PRSTS_HIST_ANEXO, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_PRSTS_HIST_OFERT, DataType.INTEGER) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_PRSTS, String pCD_AREAS_ENCR_OFERT, String pCD_AREAS_VALD_VL_MATRL, String pVL_PRSTS_AREA_RQSTE, String pVL_PRSTS_AREA_OFRTT, String pNO_PRSTS_OFERT_INTR, String pNO_PRSTS_OFERT_EXTER, String pNO_PRSTS_RESRV, String pNO_PRSTS_SOL, String pNO_PRSTS_HIST_ANEXO, String pNO_PRSTS_HIST_OFERT) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE PRSTS SET \n");

			if (pCD_AREAS_ENCR_OFERT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.CD_AREAS_ENCR_OFERT = " + lcdb.verifyInsertNull(pCD_AREAS_ENCR_OFERT, DataType.INTEGER));
			}

			if (pCD_AREAS_VALD_VL_MATRL != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.CD_AREAS_VALD_VL_MATRL = " + lcdb.verifyInsertNull(pCD_AREAS_VALD_VL_MATRL, DataType.INTEGER));
			}

			if (pVL_PRSTS_AREA_RQSTE != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.VL_PRSTS_AREA_RQSTE = " + lcdb.verifyInsertNull(pVL_PRSTS_AREA_RQSTE, DataType.INTEGER));
			}

			if (pVL_PRSTS_AREA_OFRTT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.VL_PRSTS_AREA_OFRTT = " + lcdb.verifyInsertNull(pVL_PRSTS_AREA_OFRTT, DataType.INTEGER));
			}

			if (pNO_PRSTS_OFERT_INTR != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.NO_PRSTS_OFERT_INTR = " + lcdb.verifyInsertNull(pNO_PRSTS_OFERT_INTR, DataType.INTEGER));
			}

			if (pNO_PRSTS_OFERT_EXTER != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.NO_PRSTS_OFERT_EXTER = " + lcdb.verifyInsertNull(pNO_PRSTS_OFERT_EXTER, DataType.INTEGER));
			}

			if (pNO_PRSTS_RESRV != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.NO_PRSTS_RESRV = " + lcdb.verifyInsertNull(pNO_PRSTS_RESRV, DataType.INTEGER));
			}

			if (pNO_PRSTS_SOL != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.NO_PRSTS_SOL = " + lcdb.verifyInsertNull(pNO_PRSTS_SOL, DataType.INTEGER));
			}

			if (pNO_PRSTS_HIST_ANEXO != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.NO_PRSTS_HIST_ANEXO = " + lcdb.verifyInsertNull(pNO_PRSTS_HIST_ANEXO, DataType.INTEGER));
			}

			if (pNO_PRSTS_HIST_OFERT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  PRSTS.NO_PRSTS_HIST_OFERT = " + lcdb.verifyInsertNull(pNO_PRSTS_HIST_OFERT, DataType.INTEGER));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE PRSTS.CD_PRSTS = " + lcdb.verifyInsertNull(pCD_PRSTS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_PRSTS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM PRSTS WHERE PRSTS.CD_PRSTS = " + lcdb.verifyInsertNull(pCD_PRSTS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_PRSTS, String pCD_AREAS_ENCR_OFERT, String pCD_AREAS_VALD_VL_MATRL, String pVL_PRSTS_AREA_RQSTE, String pVL_PRSTS_AREA_OFRTT, String pNO_PRSTS_OFERT_INTR, String pNO_PRSTS_OFERT_EXTER, String pNO_PRSTS_RESRV, String pNO_PRSTS_SOL, String pNO_PRSTS_HIST_ANEXO, String pNO_PRSTS_HIST_OFERT, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  PRSTS.CD_PRSTS,\n");
		query.append("  PRSTS.CD_AREAS_ENCR_OFERT,\n");
		query.append("  PRSTS.CD_AREAS_VALD_VL_MATRL,\n");
		query.append("  PRSTS.VL_PRSTS_AREA_RQSTE,\n");
		query.append("  PRSTS.VL_PRSTS_AREA_OFRTT,\n");
		query.append("  PRSTS.NO_PRSTS_OFERT_INTR,\n");
		query.append("  PRSTS.NO_PRSTS_OFERT_EXTER,\n");
		query.append("  PRSTS.NO_PRSTS_RESRV,\n");
		query.append("  PRSTS.NO_PRSTS_SOL,\n");
		query.append("  PRSTS.NO_PRSTS_HIST_ANEXO,\n");
		query.append("  PRSTS.NO_PRSTS_HIST_OFERT\n");
		query.append("FROM PRSTS \n");

		if (!Database.verifyNull(pCD_PRSTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.CD_PRSTS = " + lcdb.verifyInsertNull(pCD_PRSTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_AREAS_ENCR_OFERT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.CD_AREAS_ENCR_OFERT = " + lcdb.verifyInsertNull(pCD_AREAS_ENCR_OFERT, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_AREAS_VALD_VL_MATRL).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.CD_AREAS_VALD_VL_MATRL = " + lcdb.verifyInsertNull(pCD_AREAS_VALD_VL_MATRL, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pVL_PRSTS_AREA_RQSTE).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.VL_PRSTS_AREA_RQSTE = " + lcdb.verifyInsertNull(pVL_PRSTS_AREA_RQSTE, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pVL_PRSTS_AREA_OFRTT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.VL_PRSTS_AREA_OFRTT = " + lcdb.verifyInsertNull(pVL_PRSTS_AREA_OFRTT, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNO_PRSTS_OFERT_INTR).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.NO_PRSTS_OFERT_INTR = " + lcdb.verifyInsertNull(pNO_PRSTS_OFERT_INTR, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNO_PRSTS_OFERT_EXTER).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.NO_PRSTS_OFERT_EXTER = " + lcdb.verifyInsertNull(pNO_PRSTS_OFERT_EXTER, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNO_PRSTS_RESRV).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.NO_PRSTS_RESRV = " + lcdb.verifyInsertNull(pNO_PRSTS_RESRV, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNO_PRSTS_SOL).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.NO_PRSTS_SOL = " + lcdb.verifyInsertNull(pNO_PRSTS_SOL, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNO_PRSTS_HIST_ANEXO).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.NO_PRSTS_HIST_ANEXO = " + lcdb.verifyInsertNull(pNO_PRSTS_HIST_ANEXO, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNO_PRSTS_HIST_OFERT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" PRSTS.NO_PRSTS_HIST_OFERT = " + lcdb.verifyInsertNull(pNO_PRSTS_HIST_OFERT, DataType.INTEGER) + "\n");
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
