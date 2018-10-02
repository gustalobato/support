
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLMTAUS {
	Database lcdb;
	User user;

	public SQLMTAUS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_MTAUS, String pNM_MTAUS, String pID_MTAUS_TIPO, String pNM_MTAUS_DOM, String pNM_MTAUS_LDAP, String pID_MTAUS_STATU) {
		long maxPK = lcdb.maxDB("MTAUS", "MTAUS.CD_MTAUS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO MTAUS (\n");
			query.append("  CD_MTAUS,\n");
			query.append("  NM_MTAUS,\n");
			query.append("  ID_MTAUS_TIPO,\n");
			query.append("  NM_MTAUS_DOM,\n");
			query.append("  NM_MTAUS_LDAP,\n");
			query.append("  ID_MTAUS_STATU");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_MTAUS, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_MTAUS_TIPO, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_MTAUS_DOM, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_MTAUS_LDAP, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_MTAUS_STATU, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_MTAUS, String pNM_MTAUS, String pID_MTAUS_TIPO, String pNM_MTAUS_DOM, String pNM_MTAUS_LDAP, String pID_MTAUS_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE MTAUS SET \n");

			if (pNM_MTAUS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  MTAUS.NM_MTAUS = " + lcdb.verifyInsertNull(pNM_MTAUS, DataType.TEXT));
			}

			if (pID_MTAUS_TIPO != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  MTAUS.ID_MTAUS_TIPO = " + lcdb.verifyInsertNull(pID_MTAUS_TIPO, DataType.TEXT));
			}

			if (pNM_MTAUS_DOM != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  MTAUS.NM_MTAUS_DOM = " + lcdb.verifyInsertNull(pNM_MTAUS_DOM, DataType.TEXT));
			}

			if (pNM_MTAUS_LDAP != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  MTAUS.NM_MTAUS_LDAP = " + lcdb.verifyInsertNull(pNM_MTAUS_LDAP, DataType.TEXT));
			}

			if (pID_MTAUS_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  MTAUS.ID_MTAUS_STATU = " + lcdb.verifyInsertNull(pID_MTAUS_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE MTAUS.CD_MTAUS = " + lcdb.verifyInsertNull(pCD_MTAUS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_MTAUS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM MTAUS WHERE MTAUS.CD_MTAUS = " + lcdb.verifyInsertNull(pCD_MTAUS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_MTAUS, String pNM_MTAUS, String pID_MTAUS_TIPO, String pNM_MTAUS_DOM, String pNM_MTAUS_LDAP, String pID_MTAUS_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  MTAUS.CD_MTAUS,\n");
		query.append("  MTAUS.NM_MTAUS,\n");
		query.append("  MTAUS.ID_MTAUS_TIPO,\n");
		query.append("  MTAUS.NM_MTAUS_DOM,\n");
		query.append("  MTAUS.NM_MTAUS_LDAP,\n");
		query.append("  MTAUS.ID_MTAUS_STATU\n");
		query.append("FROM MTAUS \n");

		if (!Database.verifyNull(pCD_MTAUS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" MTAUS.CD_MTAUS = " + lcdb.verifyInsertNull(pCD_MTAUS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_MTAUS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" MTAUS.NM_MTAUS = " + lcdb.verifyInsertNull(pNM_MTAUS, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_MTAUS_TIPO).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" MTAUS.ID_MTAUS_TIPO = " + lcdb.verifyInsertNull(pID_MTAUS_TIPO, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pNM_MTAUS_DOM).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" MTAUS.NM_MTAUS_DOM = " + lcdb.verifyInsertNull(pNM_MTAUS_DOM, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pNM_MTAUS_LDAP).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" MTAUS.NM_MTAUS_LDAP = " + lcdb.verifyInsertNull(pNM_MTAUS_LDAP, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_MTAUS_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" MTAUS.ID_MTAUS_STATU = " + lcdb.verifyInsertNull(pID_MTAUS_STATU, DataType.TEXT) + "\n");
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
