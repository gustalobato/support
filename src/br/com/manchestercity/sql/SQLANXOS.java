
package br.com.manchestercity.sql;

import java.sql.Connection;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLANXOS {
	Database lcdb;
	User user;

	public SQLANXOS(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_ANXOS, String pCD_OFMTS, String pNM_ANXOS, String pAQ_ANXOS, String pAQ_ANXOS_THUMB) {
		long maxPK = lcdb.maxDB("ANXOS", "ANXOS.CD_ANXOS", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO ANXOS (\n");
			query.append("  CD_ANXOS,\n");
			query.append("  CD_OFMTS,\n");
			query.append("  NM_ANXOS,\n");
			query.append("  AQ_ANXOS,\n");
			query.append("  AQ_ANXOS_THUMB");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(String.valueOf(maxPK), DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNM_ANXOS, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pAQ_ANXOS, DataType.BLOB) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pAQ_ANXOS_THUMB, DataType.BLOB) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_ANXOS, String pCD_OFMTS, String pNM_ANXOS, String pAQ_ANXOS, String pAQ_ANXOS_THUMB) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE ANXOS SET \n");

			if (pCD_OFMTS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  ANXOS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER));
			}

			if (pNM_ANXOS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  ANXOS.NM_ANXOS = " + lcdb.verifyInsertNull(pNM_ANXOS, DataType.TEXT));
			}

			if (pAQ_ANXOS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  ANXOS.AQ_ANXOS = " + lcdb.verifyInsertNull(pAQ_ANXOS, DataType.BLOB));
			}

			if (pAQ_ANXOS_THUMB != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  ANXOS.AQ_ANXOS_THUMB = " + lcdb.verifyInsertNull(pAQ_ANXOS_THUMB, DataType.BLOB));
			}

			query.append(aux);
			query.append("\n");
			query.append("WHERE ANXOS.CD_ANXOS = " + lcdb.verifyInsertNull(pCD_ANXOS, DataType.INTEGER) + "");

			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors, String pCD_ANXOS) {
		boolean commit = false;
		try {
			commit = lcdb.executeQuery("DELETE FROM ANXOS WHERE ANXOS.CD_ANXOS = " + lcdb.verifyInsertNull(pCD_ANXOS, DataType.INTEGER) + "", conn);
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
			commit = lcdb.executeQuery("DELETE FROM ANXOS WHERE ANXOS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + "", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_ANXOS, String pCD_OFMTS, String pNM_ANXOS, String pAQ_ANXOS, String pAQ_ANXOS_THUMB, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  ANXOS.CD_ANXOS,\n");
		query.append("  ANXOS.CD_OFMTS,\n");
		query.append("  ANXOS.NM_ANXOS,\n");
		query.append("  ANXOS.AQ_ANXOS,\n");
		query.append("  ANXOS.AQ_ANXOS_THUMB,\n");
		query.append("  'showimage?imgTable=ANXOS&imgColumn=AQ_ANXOS&keyColumn=CD_ANXOS&keyValue='" + BuildSql.getConcatCharacter() + "ANXOS.CD_ANXOS as URL_IMG \n");
		query.append("FROM ANXOS \n");

		if (!Database.verifyNull(pCD_ANXOS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" ANXOS.CD_ANXOS = " + lcdb.verifyInsertNull(pCD_ANXOS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pCD_OFMTS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" ANXOS.CD_OFMTS = " + lcdb.verifyInsertNull(pCD_OFMTS, DataType.INTEGER) + "\n");
		}

		if (!Database.verifyNull(pNM_ANXOS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" ANXOS.NM_ANXOS = " + lcdb.verifyInsertNull(pNM_ANXOS, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pAQ_ANXOS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" ANXOS.AQ_ANXOS = " + lcdb.verifyInsertNull(pAQ_ANXOS, DataType.BLOB) + "\n");
		}

		if (!Database.verifyNull(pAQ_ANXOS_THUMB).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" ANXOS.AQ_ANXOS_THUMB = " + lcdb.verifyInsertNull(pAQ_ANXOS_THUMB, DataType.BLOB) + "\n");
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
