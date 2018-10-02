
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLSTEST {
	Database lcdb;
	User user;

	public SQLSTEST(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_STEST, String pNM_STEST, String pID_STEST_STATU) {
		long maxPK = lcdb.maxDB("STEST", "STEST.CD_STEST", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO STEST (\n");
			query.append("  CD_STEST,\n");
			query.append("  NM_STEST,\n");
			query.append("  ID_STEST_STATU");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_STEST, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pID_STEST_STATU, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_STEST, String pNM_STEST, String pID_STEST_STATU) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE STEST SET \n");

			if (pNM_STEST != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  STEST.NM_STEST = " + lcdb.verifyInsertNull(pNM_STEST, DataType.TEXT));
			}

			if (pID_STEST_STATU != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  STEST.ID_STEST_STATU = " + lcdb.verifyInsertNull(pID_STEST_STATU, DataType.TEXT));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE STEST.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_STEST) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM STEST WHERE STEST.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_STEST, String pNM_STEST, String pID_STEST_STATU, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  STEST.CD_STEST,\n");
		query.append("  STEST.NM_STEST,\n");
		query.append("  STEST.ID_STEST_STATU\n");
		query.append("FROM STEST \n");

		if (!Database.verifyNull(pCD_STEST).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" STEST.CD_STEST = " + lcdb.verifyInsertNull(pCD_STEST, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_STEST).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" STEST.NM_STEST = " + lcdb.verifyInsertNull(pNM_STEST, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pID_STEST_STATU).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" STEST.ID_STEST_STATU = " + lcdb.verifyInsertNull(pID_STEST_STATU, DataType.TEXT) + "\n");
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
