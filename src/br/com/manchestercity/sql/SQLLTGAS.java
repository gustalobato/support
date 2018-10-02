
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLLTGAS {
	Database lcdb;
	User user;

	public SQLLTGAS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_AREAS, String pCD_GPARS) {
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO LTGAS (\n");
			query.append("  CD_AREAS,\n");
			query.append("  CD_GPARS");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_GPARS, DataType.INTEGER) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? Integer.parseInt(pCD_AREAS) : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_AREAS, String pCD_GPARS) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE LTGAS SET \n");

			query.append(aux);
			query.append("\n");
			query.append("WHERE LTGAS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + " AND LTGAS.CD_GPARS = " + lcdb.verifyInsertNull(pCD_GPARS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_AREAS, String pCD_GPARS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM LTGAS WHERE LTGAS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + " AND LTGAS.CD_GPARS = " + lcdb.verifyInsertNull(pCD_GPARS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_AREAS, String pCD_GPARS, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  LTGAS.CD_AREAS,\n");
		query.append("  AREAS.NM_AREAS,\n");
		query.append("  LTGAS.CD_GPARS,\n");
		query.append(BuildSql.getExecFunction("GETAREASHIERARQUIA", "LTGAS.CD_AREAS, 1") + " AS HIERARQUIA\n");
		query.append("FROM LTGAS \n");
		query.append("	LEFT JOIN AREAS ON LTGAS.CD_AREAS = AREAS.CD_AREAS \n");

		if (!Database.verifyNull(pCD_AREAS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" LTGAS.CD_AREAS = " + lcdb.verifyInsertNull(pCD_AREAS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_GPARS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" LTGAS.CD_GPARS = " + lcdb.verifyInsertNull(pCD_GPARS, DataType.INTEGER) + "\n");
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
