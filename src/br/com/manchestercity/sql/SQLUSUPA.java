
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.Crypt;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLUSUPA {
	Database lcdb;
	User user;

	public SQLUSUPA(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_USUPA, String pCD_MTAUS, String pCD_AREAS, String pNM_USUPA, String pNM_USUPA_EMAIL, String pNO_USUPA_RAMAL, String pNM_USUPA_LOGIN, String pNM_USUPA_SENHA, String pID_USUPA_STATU) {
		long maxPK = lcdb.maxDB("USUPA", "USUPA.CD_USUPA", "", conn);
		boolean commit = false;
		String ID_MTAUS_TIPO = Database.verifyNull(lcdb.valorDB("MTAUS", "ID_MTAUS_TIPO", "CD_MTAUS = " + pCD_MTAUS, conn));

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO USUPA (\n");
			query.append("  CD_USUPA,\n");
			query.append("  CD_MTAUS,\n");
			query.append("  CD_AREAS,\n");
			query.append("  NM_USUPA,\n");
			query.append("  NM_USUPA_EMAIL,\n");
			query.append("  NO_USUPA_RAMAL,\n");
			query.append("  NM_USUPA_LOGIN,\n");
			query.append("  NM_USUPA_SENHA,\n");
			query.append("  ID_USUPA_STATU");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_MTAUS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_USUPA, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_USUPA_EMAIL, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNO_USUPA_RAMAL, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_USUPA_LOGIN, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(Crypt.addEncriptation(pNM_USUPA_SENHA), DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_USUPA_STATU, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_USUPA, String pCD_MTAUS, String pCD_AREAS, String pNM_USUPA, String pNM_USUPA_EMAIL, String pNO_USUPA_RAMAL, String pNM_USUPA_LOGIN, String pNM_USUPA_SENHA, String pID_USUPA_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE USUPA SET \n");

			if (pCD_MTAUS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  USUPA.CD_MTAUS = " + lcdb.verifyInsertNull(pCD_MTAUS, DataType.INTEGER));
			}

			if (pCD_AREAS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  USUPA.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER));
			}

			if (pNM_USUPA != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  USUPA.NM_USUPA = " + lcdb.verifyInsertNull(pNM_USUPA, DataType.TEXT));
			}

			if (pNM_USUPA_EMAIL != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  USUPA.NM_USUPA_EMAIL = " + lcdb.verifyInsertNull(pNM_USUPA_EMAIL, DataType.TEXT));
			}

			if (pNO_USUPA_RAMAL != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  USUPA.NO_USUPA_RAMAL = " + lcdb.verifyInsertNull(pNO_USUPA_RAMAL, DataType.TEXT));
			}

			if (pNM_USUPA_LOGIN != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  USUPA.NM_USUPA_LOGIN = " + lcdb.verifyInsertNull(pNM_USUPA_LOGIN, DataType.TEXT));
			}

			if (pNM_USUPA_SENHA != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  USUPA.NM_USUPA_SENHA = " + lcdb.verifyInsertNull(Crypt.addEncriptation(pNM_USUPA_SENHA), DataType.TEXT));
			}

			if (pID_USUPA_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  USUPA.ID_USUPA_STATU = " + lcdb.verifyInsertNull(pID_USUPA_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE USUPA.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_USUPA) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM USUPA WHERE USUPA.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_USUPA, String pCD_MTAUS, String pCD_AREAS, String pNM_USUPA, String pNM_USUPA_EMAIL, String pNO_USUPA_RAMAL, String pNM_USUPA_LOGIN, String pNM_USUPA_SENHA, String pID_USUPA_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  USUPA.CD_USUPA,\n");
		query.append("  USUPA.CD_MTAUS,\n");
		query.append("  USUPA.CD_AREAS,\n");
		query.append("  USUPA.NM_USUPA,\n");
		query.append("  USUPA.NM_USUPA_EMAIL,\n");
		query.append("  USUPA.NO_USUPA_RAMAL,\n");
		query.append("  USUPA.NM_USUPA_LOGIN,\n");
		query.append("  USUPA.NM_USUPA_SENHA,\n");
		query.append("  USUPA.ID_USUPA_STATU,\n");
		query.append("  MTAUS.ID_MTAUS_TIPO,\n");
		query.append("  MTAUS.NM_MTAUS_LDAP,\n");
		query.append("  MTAUS.NM_MTAUS_DOM,\n");
		query.append("  AREAS.CD_AREAS,\n");
		query.append("  AREAS.CD_STEST,\n");
		query.append("  DFAUT.CD_AREAS AS CD_AREAS_DFAUT,\n");
		query.append("  DFAUT.CD_STEST AS CD_STEST_DFAUT \n");
		query.append("FROM USUPA \n");
		query.append("  LEFT JOIN MTAUS ON USUPA.CD_MTAUS = MTAUS.CD_MTAUS \n");
		query.append("  LEFT JOIN AREAS ON USUPA.CD_AREAS = AREAS.CD_AREAS \n");
		query.append("  LEFT JOIN AREAS DFAUT ON DFAUT.ID_AREAS_DFAUT = 'S' \n");

		if (!Database.verifyNull(pCD_USUPA).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" USUPA.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_MTAUS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" USUPA.CD_MTAUS = " + lcdb.verifyInsertNull(pCD_MTAUS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_AREAS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" USUPA.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_USUPA).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" USUPA.NM_USUPA = " + lcdb.verifyInsertNull(pNM_USUPA, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pNM_USUPA_EMAIL).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" LOWER(USUPA.NM_USUPA_EMAIL) = " + lcdb.verifyInsertNull(pNM_USUPA_EMAIL, DataType.TEXT).toLowerCase() + "\n");
		}

		if (!Database.verifyNull(pNO_USUPA_RAMAL).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" USUPA.NO_USUPA_RAMAL = " + lcdb.verifyInsertNull(pNO_USUPA_RAMAL, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pNM_USUPA_LOGIN).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" LOWER(USUPA.NM_USUPA_LOGIN) = " + lcdb.verifyInsertNull(pNM_USUPA_LOGIN, DataType.TEXT).toLowerCase() + "\n");
		}

		if (!Database.verifyNull(pNM_USUPA_SENHA).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" USUPA.NM_USUPA_SENHA = " + lcdb.verifyInsertNull(pNM_USUPA_SENHA, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_USUPA_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" USUPA.ID_USUPA_STATU = " + lcdb.verifyInsertNull(pID_USUPA_STATU, DataType.TEXT) + "\n");
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

	public String refreshPermissoes(String pCD_USUPA) {
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  FNSEG.CD_FNSEG,\n");
		query.append("  FNSEG.NM_FNSEG,\n");
		query.append("  PMSAC.ID_PMSAC_CRUD,\n");
		query.append("  FNSEG.CD_GFSEG\n");
		query.append("FROM LTSEG \n");
		query.append("	INNER JOIN PFSEG ON LTSEG.CD_PFSEG = PFSEG.CD_PFSEG \n");
		query.append("	INNER JOIN PMSAC ON PFSEG.CD_PFSEG = PMSAC.CD_PFSEG \n");
		query.append("	INNER JOIN FNSEG ON PMSAC.CD_FNSEG = FNSEG.CD_FNSEG \n");
		query.append("WHERE LTSEG.CD_USUPA = " + lcdb.verifyInsertNull(pCD_USUPA, DataType.INTEGER) + "\n");

		return query.toString();
	}

	public String permissoesPadrao() {
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  FNSEG.CD_FNSEG,\n");
		query.append("  FNSEG.NM_FNSEG,\n");
		query.append("  PMSAC.ID_PMSAC_CRUD,\n");
		query.append("  FNSEG.CD_GFSEG\n");
		query.append("FROM PFSEG \n");
		query.append("	INNER JOIN PMSAC ON PFSEG.CD_PFSEG = PMSAC.CD_PFSEG \n");
		query.append("	INNER JOIN FNSEG ON PMSAC.CD_FNSEG = FNSEG.CD_FNSEG \n");
		query.append("WHERE PFSEG.ID_PFSEG_DFAUT = 'S'\n");

		return query.toString();
	}
}
