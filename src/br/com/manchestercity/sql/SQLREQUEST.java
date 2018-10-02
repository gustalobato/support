
package br.com.manchestercity.sql;

import java.sql.Connection;
import java.sql.ResultSet;

import br.com.manchestercity.automacao.AutoException;
import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.DataType;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;

public class SQLREQUEST {
	Database lcdb;
	User user;

	public SQLREQUEST(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public static synchronized long insert(Database lcdb, Connection conn, StringBuffer errors, String pCD_REQUEST, String pFIRSTNAME, String pLASTNAME, String pDATEBIRTH, String pNATONALITY, String pGENDER, String pCOUNTRY, String pCITY, String pPOSTCODE, String pEMAIL, String pPHONE, String pISCITYZEN, String pSUPPORTERNUMBER, String pLALLOWCONTACT, String pTRDPARTYCONTACT, String pOPENTEXT, String pSTATUS, String pORIGEM) {
		long maxPK = lcdb.maxDB("REQUEST", "REQUEST.CD_REQUEST", "", conn);
		boolean commit = false;

		try {
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO REQUEST (\n");
			query.append("  CD_REQUEST,\n");
			query.append("  FIRSTNAME,\n");
			query.append("  LASTNAME,\n");
			query.append("  DATEBIRTH,\n");
			query.append("  NATONALITY,\n");
			query.append("  GENDER,\n");
			query.append("  COUNTRY,\n");
			query.append("  CITY,\n");
			query.append("  POSTCODE,\n");
			query.append("  EMAIL,\n");
			query.append("  PHONE,\n");
			query.append("  ISCITYZEN,\n");
			query.append("  SUPPORTERNUMBER,\n");
			query.append("  LALLOWCONTACT,\n");
			query.append("  TRDPARTYCONTACT,\n");
			query.append("  OPENTEXT,\n");
			query.append("  ORIGEM,\n");
			query.append("  STATUS");
			query.append(")\n");
			query.append("VALUES (\n");
			query.append("  " + lcdb.verifyInsertNull(pCD_REQUEST, DataType.INTEGER) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pFIRSTNAME, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pLASTNAME, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pDATEBIRTH, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pNATONALITY, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pGENDER, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCOUNTRY, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pCITY, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pPOSTCODE, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pEMAIL, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pPHONE, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pISCITYZEN, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pSUPPORTERNUMBER, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pLALLOWCONTACT, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pTRDPARTYCONTACT, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pOPENTEXT, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pORIGEM, DataType.TEXT) + ",\n");
			query.append("  " + lcdb.verifyInsertNull(pSTATUS, DataType.TEXT) + "");
			query.append(")\n");

			commit = lcdb.executeQuery(query.toString(), conn);

		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return 0;
		}
		return commit ? maxPK : 0;
	}

	public boolean update(Connection conn, StringBuffer errors, String pCD_REQUEST, String pFIRSTNAME, String pLASTNAME, String pDATEBIRTH, String pNATONALITY, String pGENDER, String pCOUNTRY, String pCITY, String pPOSTCODE, String pEMAIL, String pPHONE, String pISCITYZEN, String pSUPPORTERNUMBER, String pLALLOWCONTACT, String pTRDPARTYCONTACT, String pOPENTEXT, String pSTATUS, String pORIGEM) {
		boolean commit = false;
		try {
			StringBuffer aux = new StringBuffer();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE REQUEST SET \n");

			if (pFIRSTNAME != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.FIRSTNAME = " + lcdb.verifyInsertNull(pFIRSTNAME, DataType.TEXT));
			}

			if (pLASTNAME != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.LASTNAME = " + lcdb.verifyInsertNull(pLASTNAME, DataType.TEXT));
			}

			if (pDATEBIRTH != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.DATEBIRTH = " + lcdb.verifyInsertNull(pDATEBIRTH, DataType.TEXT));
			}

			if (pNATONALITY != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.NATONALITY = " + lcdb.verifyInsertNull(pNATONALITY, DataType.TEXT));
			}

			if (pGENDER != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.GENDER = " + lcdb.verifyInsertNull(pGENDER, DataType.TEXT));
			}

			if (pCOUNTRY != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.COUNTRY = " + lcdb.verifyInsertNull(pCOUNTRY, DataType.TEXT));
			}

			if (pCITY != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.CITY = " + lcdb.verifyInsertNull(pCITY, DataType.TEXT));
			}

			if (pPOSTCODE != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.POSTCODE = " + lcdb.verifyInsertNull(pPOSTCODE, DataType.TEXT));
			}

			if (pEMAIL != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.EMAIL = " + lcdb.verifyInsertNull(pEMAIL, DataType.TEXT));
			}

			if (pPHONE != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.PHONE = " + lcdb.verifyInsertNull(pPHONE, DataType.TEXT));
			}

			if (pISCITYZEN != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.ISCITYZEN = " + lcdb.verifyInsertNull(pISCITYZEN, DataType.TEXT));
			}

			if (pSUPPORTERNUMBER != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.SUPPORTERNUMBER = " + lcdb.verifyInsertNull(pSUPPORTERNUMBER, DataType.TEXT));
			}

			if (pLALLOWCONTACT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.LALLOWCONTACT = " + lcdb.verifyInsertNull(pLALLOWCONTACT, DataType.TEXT));
			}

			if (pTRDPARTYCONTACT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.TRDPARTYCONTACT = " + lcdb.verifyInsertNull(pTRDPARTYCONTACT, DataType.TEXT));
			}

			if (pOPENTEXT != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.OPENTEXT = " + lcdb.verifyInsertNull(pOPENTEXT, DataType.TEXT));
			}

			if (pSTATUS != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.STATUS = " + lcdb.verifyInsertNull(pSTATUS, DataType.TEXT));
			}
			
			if (pORIGEM != null) {
				if (aux.length() > 0) {
					aux.append(",\n");
				}
				aux.append("  REQUEST.ORIGEM = " + lcdb.verifyInsertNull(pORIGEM, DataType.TEXT));
			}
			

			query.append(aux);
			query.append("\n");
			query.append("WHERE ");

			query.append("  REQUEST.CD_REQUEST = " + lcdb.verifyInsertNull(pCD_REQUEST, DataType.INTEGER));

			//query.append(aux);
			
			commit = lcdb.executeQuery(query.toString(), conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public boolean delete(Connection conn, StringBuffer errors) {
		boolean commit = false;	
		try {
			commit = lcdb.executeQuery("DELETE FROM REQUEST WHERE ", conn);
		}
		catch (AutoException e) {
			errors.append(e.getMessage());
			return false;
		}
		return commit;
	}

	public String refresh(String pCD_REQUEST, String pFIRSTNAME, String pLASTNAME, String pDATEBIRTH, String pNATONALITY, String pGENDER, String pCOUNTRY, String pCITY, String pPOSTCODE, String pEMAIL, String pPHONE, String pISCITYZEN, String pSUPPORTERNUMBER, String pLALLOWCONTACT, String pTRDPARTYCONTACT, String pOPENTEXT, String pSTATUS, String pOrderBy) {
		StringBuffer aux = new StringBuffer();
		StringBuffer query = new StringBuffer();

		query.append("SELECT \n");
		query.append("  REQUEST.CD_REQUEST,\n");
		query.append("  REQUEST.FIRSTNAME,\n");
		query.append("  REQUEST.LASTNAME,\n");
		query.append("  REQUEST.DATEBIRTH,\n");
		query.append("  REQUEST.NATONALITY,\n");
		query.append("  REQUEST.GENDER,\n");
		query.append("  REQUEST.COUNTRY,\n");
		query.append("  REQUEST.CITY,\n");
		query.append("  REQUEST.POSTCODE,\n");
		query.append("  REQUEST.EMAIL,\n");
		query.append("  REQUEST.PHONE,\n");
		query.append("  REQUEST.ISCITYZEN,\n");
		query.append("  REQUEST.SUPPORTERNUMBER,\n");
		query.append("  REQUEST.LALLOWCONTACT,\n");
		query.append("  REQUEST.TRDPARTYCONTACT,\n");
		query.append("  REQUEST.OPENTEXT,\n");
		query.append("  REQUEST.ORIGEM,\n");
		query.append("  REQUEST.STATUS\n");
		query.append("FROM REQUEST \n");

		
		if (!Database.verifyNull(pCD_REQUEST).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.CD_REQUEST = " + lcdb.verifyInsertNull(pCD_REQUEST, DataType.INTEGER) + "\n");
		}
		
		if (!Database.verifyNull(pFIRSTNAME).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.FIRSTNAME = " + lcdb.verifyInsertNull(pFIRSTNAME, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pLASTNAME).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.LASTNAME = " + lcdb.verifyInsertNull(pLASTNAME, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pDATEBIRTH).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.DATEBIRTH = " + lcdb.verifyInsertNull(pDATEBIRTH, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pNATONALITY).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.NATONALITY = " + lcdb.verifyInsertNull(pNATONALITY, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pGENDER).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.GENDER = " + lcdb.verifyInsertNull(pGENDER, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pCOUNTRY).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.COUNTRY = " + lcdb.verifyInsertNull(pCOUNTRY, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pCITY).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.CITY = " + lcdb.verifyInsertNull(pCITY, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pPOSTCODE).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.POSTCODE = " + lcdb.verifyInsertNull(pPOSTCODE, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pEMAIL).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.EMAIL = " + lcdb.verifyInsertNull(pEMAIL, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pPHONE).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.PHONE = " + lcdb.verifyInsertNull(pPHONE, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pISCITYZEN).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.ISCITYZEN = " + lcdb.verifyInsertNull(pISCITYZEN, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pSUPPORTERNUMBER).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.SUPPORTERNUMBER = " + lcdb.verifyInsertNull(pSUPPORTERNUMBER, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pLALLOWCONTACT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.LALLOWCONTACT = " + lcdb.verifyInsertNull(pLALLOWCONTACT, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pTRDPARTYCONTACT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.TRDPARTYCONTACT = " + lcdb.verifyInsertNull(pTRDPARTYCONTACT, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pOPENTEXT).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.OPENTEXT = " + lcdb.verifyInsertNull(pOPENTEXT, DataType.TEXT) + "\n");
		}

		if (!Database.verifyNull(pSTATUS).equals("")) {
			if (aux.length() > 0) {
				aux.append("  AND");
			}
			aux.append(" REQUEST.STATUS = " + lcdb.verifyInsertNull(pSTATUS, DataType.TEXT) + "\n");
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
