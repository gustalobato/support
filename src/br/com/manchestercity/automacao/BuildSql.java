
package br.com.manchestercity.automacao;

public class BuildSql {

	public static String getFromDual() {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lAux = " from dual ";
		}

		return lAux;
	}

	public static String getFromDual(String pDatabase) {
		String lAux = "";
		if (pDatabase.equals(Database.DBMS_ORACLE)) {
			lAux = " from dual ";
		}

		return lAux;
	}

	public static String getRowNumSemName() {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lAux = " ROWNUM ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = " rownum('" + System.currentTimeMillis() + "') ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			// SÛ funciona em Select ou Order by
			lAux = "ROW_NUMBER() OVER(order by OBJECT_NAME(0))";
		}
		return lAux;
	}

	public static String getRowNum() {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lAux = " ROWNUM ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = " rownum('" + System.currentTimeMillis() + "') ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			// SÛ funciona em Select ou Order by
			lAux = "ROW_NUMBER() OVER(order by OBJECT_NAME(0)) AS LINHA";
		}
		return lAux;
	}

	public static String getToNumber(String pField) {
		return getToNumber(pField, "", "");
	}

	public static String getToNumber(String pField, String pFormat, String pLanguage) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			if (pFormat.equals("") && pLanguage.equals("")) {
				lAux = " to_number(" + pField + ") ";
			}
			else {
				lAux = " to_number(" + pField + "," + pFormat + "," + pLanguage + ")";
			}
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = " " + pField.trim() + "::numeric ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = " CONVERT(NUMERIC," + pField.trim() + ") ";
		}
		return lAux;
	}

	public static String getCeil(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE) || Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = " CEIL(" + pField + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = " CEILING(" + pField + ") ";
		}
		return lAux;
	}

	public static String getRound(String pField) {
		return getRound(pField, "0");
	}

	public static String getRound(String pField, String pCasaDecimal) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE) || Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = " ROUND(" + pField + "," + pCasaDecimal + ")";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = " ROUND(" + pField + "," + pCasaDecimal + ") ";
		}
		return lAux;
	}

	public static String getSysDate() {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lAux = "SYSDATE";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = "localtimestamp";
			// lAux = "CURRENT_TIMESTAMP";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "GETDATE()";
		}
		return lAux;
	}

	public static String getDateDiff(String pDate1, String pDate2) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lAux = "(" + pDate1 + "-" + pDate2 + ")";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = "(EXTRACT(DAY FROM " + pDate1 + "-" + pDate2 + ")+";
			lAux += "(EXTRACT(HOUR FROM " + pDate1 + "-" + pDate2 + ")/24)+";
			lAux += "(EXTRACT(MINUTE FROM " + pDate1 + "-" + pDate2 + ")/1440)+";
			lAux += "(EXTRACT(SECOND FROM " + pDate1 + "-" + pDate2 + ")/86400))";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "DATEDIFF(HOUR," + pDate2 + "," + pDate1 + ")/24.0";
		}
		return lAux;
	}

	public static String getTruncateDate(String pDate) {

		String lAux = "";

		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lAux = "TRUNC(" + pDate + ")";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = "DATE_TRUNC('DAY', " + pDate + ")";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "CAST(FLOOR(CAST(" + pDate + " AS FLOAT)) AS DATETIME)";
		}

		return lAux;

	}

	public static String getDateAddDay(String pDate, String pDay) {
		/* Para subtrair pDay deve conter um n˙mero negativo de dias exemplo -1 */
		String lReturn = "";

		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lReturn = " ((" + pDate + ") + NVL((" + pDay + "), 0)) ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lReturn = " ((" + pDate + ") + (COALESCE((" + pDay + "), 0) || ' DAY')::INTERVAL) ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lReturn = " ((" + pDate + ") + ISNULL((" + pDay + "), 0)) ";
		}

		return lReturn;
	}

	public static String getDateAddMonth(String pDate, String pMonths) {
		/* Para subtrair pDay deve conter um n˙mero negativo de dias exemplo -1 */
		String lReturn = "";

		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lReturn = " DATEADD(MONTH, " + pMonths + ", " + pDate + ") ";
		}
		else {
			lReturn = " ADD_MONTHS(" + pDate + ", " + pMonths + ") ";
		}

		return lReturn;
	}

	public static String getConcatCharacter() {
		/* Para subtrair pDay deve conter um n˙mero negativo de dias exemplo -1 */
		String lReturn = "";

		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lReturn = " || ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lReturn = " || ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lReturn = " + ";
		}

		return lReturn;
	}

	public static String getCotac(String pCD_MOEDA_ORIG, String pCD_MOEDA_DEST, String pDT_REFER) {
		if (Database.isDBMS(Database.DBMS_MYSQL) && pDT_REFER.equals("CURRENT_TIMESTAMP")) {
			pDT_REFER = "to_date(to_char(" + pDT_REFER + ", 'dd/MM/yyyy hh24:mi:ss'), 'dd/MM/yyyy hh24:mi:ss')";
		}
		String lReturn = BuildSql.getDecodeNumeric(new String[] { pCD_MOEDA_ORIG, pCD_MOEDA_DEST, "1", getExecFunction("FN_VALOR_COTAC", pCD_MOEDA_ORIG + ", " + pCD_MOEDA_DEST + ", " + pDT_REFER) });
		return lReturn;
	}

	public static String getExecFunction(String pNM_FUNCTION, String pPARAMETROS) {
		String lReturn = "";

		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lReturn = " " + pNM_FUNCTION + "( " + pPARAMETROS + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lReturn = " " + pNM_FUNCTION + "( " + pPARAMETROS + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lReturn = " dbo." + pNM_FUNCTION + "( " + pPARAMETROS + ") ";
		}

		return lReturn;
	}

	public static String getInStr(String pField, String pStr_to_search) {
		String lReturn = "";

		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lReturn = " INSTR(" + pField + ", '" + pStr_to_search + "') ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lReturn = " POSITION('" + pStr_to_search + "' IN " + pField + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lReturn = " CHARINDEX('" + pStr_to_search + "'," + pField + ") ";
		}

		return lReturn;
	}

	public static String getToChar(String pField) {
		return getToChar(pField, false);
	}

	public static String getToChar(String pField, boolean isCLOB) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			if (isCLOB) {
				// lAux = " DBMS_LOB.SUBSTR("+pField+", DBMS_LOB.GETLENGTH("+pField+"), 1) ";
				lAux = " DBMS_LOB.SUBSTR(" + pField + ", 4000, 1) ";
			}
			else {
				lAux = " to_char(" + pField + ") ";
			}
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = " (" + pField.trim() + ")::text ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = " CONVERT(VARCHAR(8000)," + pField.trim() + ") ";
		}
		return lAux;
	}

	public static String getConcat(String pValue1, String pValue2) {
		String lReturn = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lReturn = " CONCAT(" + pValue1 + ", " + pValue2 + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lReturn = " (" + pValue1 + " || " + pValue2 + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lReturn = " (CONVERT(VARCHAR," + pValue1 + ") + CONVERT(VARCHAR," + pValue2 + ")) ";
		}

		return lReturn;
	}

	public static String getAsSubQuery(String pTableAliasName) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lAux = pTableAliasName.equals("") ? "" : " " + pTableAliasName + " ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = pTableAliasName.equals("") ? " tab_aux_" + System.currentTimeMillis() + " " : " " + pTableAliasName + " ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = pTableAliasName.equals("") ? " tab_aux_" + System.currentTimeMillis() + " " : " " + pTableAliasName + " ";
		}
		return lAux;
	}

	public static String getAsSubQuery() {
		return getAsSubQuery("");
	}

	/*
	 * N„o descomentar antes de falar com Victtor!!!! public static String getDecode(String pValuesCommaSepareted){ return BuildSql.getDecode(pValuesCommaSepareted.split(","),-1); } public static String getDecode(String pValues[]){ return getDecode(pValues,-1); }
	 * 
	 * public static String getDecodeText(String pValuesCommaSepareted){ return BuildSql.getDecode(pValuesCommaSepareted.split(","),0); } public static String getDecodeNumeric(String pValuesCommaSepareted){ return BuildSql.getDecode(pValuesCommaSepareted.split(","),1); }
	 */

	public static String getDecodeText(String pValues[]) {
		return getDecode(pValues, 0);
	}

	public static String getDecodeNumeric(String pValues[]) {
		return getDecode(pValues, 1);
	}

	public static String getDecode(String pValues[]) {
		return getDecode(pValues, -1);
	}

	public static String getSubstring(String pValue, String pIni, String pFim) {
		String lRet = "";
		/* !!!!!!!!!!!!!!!!!!!!INICIAL … 0(ZERO)!!!!!!!!!!!!!!!!!!!!! */

		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lRet += "SUBSTR(" + pValue + "," + pIni + "," + pFim + ")";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lRet += "SUBSTR(" + pValue + "," + pIni + "," + pFim + ")";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lRet += "SUBSTRING(" + pValue + "," + pIni + "," + pFim + ")";
		}
		return lRet;
	}

	public static String getSubstring(String pValue, int pIni, int pFim) {
		String lRet = "";
		/* !!!!!!!!!!!!!!!!!!!!INICIAL … 0(ZERO)!!!!!!!!!!!!!!!!!!!!! */

		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lRet += "SUBSTR(" + pValue + "," + pIni + "," + pFim + ")";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lRet += "SUBSTR(" + pValue + "," + pIni + 1 + "," + pFim + ")";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lRet += "SUBSTRING(" + pValue + "," + pIni + 1 + "," + pFim + ")";
		}
		return lRet;
	}

	private static String getDecode(String pValues[], int pType) {
		String lRet = "";
		int i = 0;

		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lRet = " DECODE(";
			for (i = 0; i < pValues.length; i++) {
				lRet += pValues[i] + ( i < pValues.length - 1 ? "," : "" );
			}
			lRet += ") ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {

			lRet = " (CASE ";
			for (i = 1; i < pValues.length - 1; i = i + 2) {
				if (pValues[i].trim().equalsIgnoreCase("null")) {
					lRet += " WHEN " + pValues[0] + " is null THEN (" + pValues[i + 1] + ")$$TYPE$$";
				}
				else {
					lRet += " WHEN " + pValues[0] + "=" + pValues[i] + " THEN (" + pValues[i + 1] + ")$$TYPE$$";
				}
			}
			if (pValues.length % 2 == 0) {
				lRet += " ELSE (" + pValues[pValues.length - 1] + ")$$TYPE$$";
			}

			lRet += " END)";

			if (pType != -1) {
				if (pType == 1) {
					lRet = lRet.replaceAll("\\$\\$TYPE\\$\\$", "::numeric");
				}
				else {
					lRet = lRet.replaceAll("\\$\\$TYPE\\$\\$", "::text");
				}
			}
			else {
				lRet = lRet.replaceAll("\\$\\$TYPE\\$\\$", "");
			}
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {

			lRet = " (CASE ";
			for (i = 1; i < pValues.length - 1; i = i + 2) {
				if (pValues[i].trim().equalsIgnoreCase("null")) {
					lRet += " WHEN " + pValues[0] + " is null THEN ";
					if (pType == -1) {
						lRet += pValues[i + 1];
					}
					else {
						lRet += "CONVERT($$TYPE$$," + pValues[i + 1] + ")";
					}
				}
				else {
					lRet += " WHEN " + pValues[0] + "=" + pValues[i] + " THEN ";
					if (pType == -1) {
						lRet += pValues[i + 1];
					}
					else {
						lRet += "CONVERT($$TYPE$$," + pValues[i + 1] + ")";
					}
				}
			}
			if (pValues.length % 2 == 0) {
				lRet += " ELSE ";
				if (pType == -1) {
					lRet += pValues[pValues.length - 1];
				}
				else {
					lRet += "CONVERT($$TYPE$$," + pValues[pValues.length - 1] + ")";
				}
			}

			lRet += " END)";

			if (pType == 1) {
				lRet = lRet.replaceAll("\\$\\$TYPE\\$\\$", "NUMERIC");
			}
			else {
				lRet = lRet.replaceAll("\\$\\$TYPE\\$\\$", "VARCHAR(8000)");// VALOR MAXIMO, SE NAO ESPECIFICAR O VALOR SERIA 30
			}
		}

		return lRet;
	}

	public static String getNvl(String pFieldTest, String pValueWhenNull) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lAux = " nvl(" + pFieldTest + "," + pValueWhenNull + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux += " (Case when " + pFieldTest;
			lAux += " is null then " + pValueWhenNull;
			lAux += " else " + castSubquery(pFieldTest);
			lAux += " end) ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = " isnull(" + pFieldTest + "," + pValueWhenNull + ") ";
		}
		return lAux;
	}

	public static String castSubquery(String pSubQuery) {
		String lSQL = "";
		int lPos = pSubQuery.toUpperCase().indexOf("SELECT");
		if (lPos >= 0) {
			String lField = "";
			int lPar = 0;
			for (int i = lPos + 6; i < pSubQuery.length(); i++) {
				if (pSubQuery.substring(i, i + 1).equalsIgnoreCase("(")) {
					lPar++;
				}
				else if (pSubQuery.substring(i, i + 1).equalsIgnoreCase(")")) {
					lPar--;
				}
				else if (pSubQuery.substring(i, i + 4).equalsIgnoreCase("from") && lPar == 0) {
					lField = pSubQuery.substring(lPos + 6, i - 1).trim();
					if (lField.toUpperCase().indexOf("SELECT") >= 0) {
						lSQL = pSubQuery.substring(0, lPos + 6) + " " + castSubquery(lField) + " " + pSubQuery.substring(i);
					}
					else {
						// Removendo Alias do Campo....
						int lPosAux = lField.toUpperCase().indexOf(" AS ");
						if (lPosAux >= 0) {
							lField = lField.substring(0, lPosAux - 1).trim();
						}

						// Fazendo Cast da String para ::text
						if (lField.startsWith("'") && lField.endsWith("'")) {
							lSQL = pSubQuery.substring(0, lPos + 6) + " " + lField + "::text " + pSubQuery.substring(i);
						}
						else {
							lSQL = pSubQuery;
						}
					}
					break;
				}
			}
		}
		else {
			lSQL = pSubQuery;
		}
		return lSQL;
	}

	public static String getSubqueryAlias(String pSubQuery) {
		String sql = pSubQuery.trim();

		int size = sql.split("\\)").length;
		if (size > 0) {
			sql = sql.split("\\)")[size - 1];

			int pos = sql.toUpperCase().indexOf(" AS ");
			if (pos >= 0) {
				sql = sql.substring(0, pos - 1).trim();
			}
		}

		return sql;
	}

	public static String getRemoveAccent(String pField, boolean pUpper) {
		String lAux = "";

		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			// lAux = (pUpper?"UPPER":"LOWER")+"(CONVERT(REPLACE(REPLACE(UPPER("+pField+"), '√', 'A'), '’', 'O'),'US7ASCII','WE8ISO8859P1')) ";
			// lAux = (pUpper?"UPPER":"LOWER")+"(CONVERT(REPLACE(REPLACE(UPPER(DBMS_LOB.SUBSTR ("+pField+", DBMS_LOB.GETLENGTH("+pField+"), 1)), '√', 'A'), '’', 'O'),'US7ASCII','WE8MSWIN1252')) ";
			// lAux = (pUpper?"UPPER":"LOWER")+"(CONVERT(REPLACE(REPLACE(UPPER(DBMS_LOB.SUBSTR ("+pField+", 4000, 1)), '√', 'A'), '’', 'O'),'US7ASCII','WE8MSWIN1252')) ";
			lAux = " " + ( pUpper ? "UPPER" : "LOWER" ) + "(TRANSLATE(" + pField + ", '·‡‚„‰¡¿¬√ƒÈËÍÎ…» ÀÌÏÓÔÕÃŒœÛÚÙıˆ”“‘’÷˙˘˚¸⁄Ÿ€‹Á«Ò—', 'aaaaaAAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuUUUUcCnN')) ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			// lAux = " "+(pUpper?"UPPER":"LOWER")+"(to_ascii("+pField+"))";
			lAux = " " + ( pUpper ? "UPPER" : "LOWER" ) + "(TRANSLATE(" + pField + ", '·‡‚„‰¡¿¬√ƒÈËÍÎ…» ÀÌÏÓÔÕÃŒœÛÚÙıˆ”“‘’÷˙˘˚¸⁄Ÿ€‹Á«Ò—', 'aaaaaAAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuUUUUcCnN')) ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			// lAux = " ("+(pUpper?"UPPER":"LOWER")+"(CAST("+pField+" AS VARCHAR)) COLLATE SQL_Latin1_General_Cp1251_CI_AS) ";
			lAux = " (" + ( pUpper ? "UPPER" : "LOWER" ) + "(" + pField + ") COLLATE SQL_Latin1_General_Cp1251_CI_AS) ";
		}
		return lAux;
	}

	public static String getDayToChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "REPLICATE('0', (2 - LEN(CAST(DATEPART(day, " + pField + ") As VARCHAR)))) + CAST(DATEPART(day, " + pField + ") As VARCHAR)";
		}
		else {
			lAux = "to_char(" + pField + ", 'dd')";
		}

		return lAux;
	}

	public static String getToCharAddZero(String pField, int pQuantidade) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "REPLICATE('0', (" + pQuantidade + " - LEN(CAST(" + pField + " As VARCHAR)))) + CAST(" + pField + " As VARCHAR)";
		}
		else {
			lAux = "to_char(" + pField + ", 'FM";
			for (int i = 0; i < pQuantidade; i++) {
				lAux += "0";
			}
			lAux += "')";
		}

		return lAux;
	}

	public static String getMonthToChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "REPLICATE('0', (2 - LEN(CAST(DATEPART(month, " + pField + ") As VARCHAR)))) + CAST(DATEPART(month, " + pField + ") As VARCHAR)";
		}
		else {
			lAux = "to_char(" + pField + ", 'MM')";
		}

		return lAux;
	}

	public static String getMonthToNumber(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = " DATEPART(month, " + pField + ") ";
		}
		else {
			lAux = " EXTRACT(MONTH FROM " + pField + ") ";
		}

		return lAux;
	}

	public static String getYearToChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "CAST(DATEPART(year, " + pField + ") as varchar)";
		}
		else {
			lAux = "to_char(" + pField + ", 'YYYY')";
		}

		return lAux;
	}

	public static String getYearToNumber(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = " DATEPART(year, " + pField + ") ";
		}
		else {
			lAux = " EXTRACT(YEAR FROM " + pField + ") ";
		}

		return lAux;
	}

	public static String getYearMonthToChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = getYearToChar(pField) + "  +  " + getMonthToChar(pField) + " ";
		}
		else {
			lAux = "to_char(" + pField + ", 'yyyyMM')";
		}

		return lAux;
	}

	public static String getYearBarMonthToChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = getYearToChar(pField) + " + '/' +  " + getMonthToChar(pField) + " ";
		}
		else {
			lAux = "to_char(" + pField + ", 'yyyy/MM')";
		}

		return lAux;
	}

	public static String getYearMonthToDate(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "CAST(" + getToChar(pField) + " + '01' as datetime)";
		}
		else {
			lAux = "TO_DATE(" + pField + ", 'yyyyMM')";
		}

		return lAux;
	}

	public static String getMonthBarYearToDate(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "CAST(01/" + pField + " as datetime)";
		}
		else {
			lAux = "TO_DATE(" + pField + ", 'MM/YYYY')";
		}

		return lAux;
	}

	public static String getDayOfWeek(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "cast(DATEPART(WEEKDAY," + pField + ") as varchar)";
		}
		else {
			lAux = "to_char(" + pField + ", 'D')";
		}

		return lAux;
	}

	public static String getMonthBarYearToChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = getMonthToChar(pField) + " + '/' +  " + getYearToChar(pField) + " ";
		}
		else {
			lAux = "to_char(" + pField + ", 'MM/yyyy')";
		}

		return lAux;
	}

	public static String getMonthYearToChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = getMonthToChar(pField) + " + " + getYearToChar(pField) + " ";
		}
		else {
			lAux = "to_char(" + pField + ", 'MMyyyy')";
		}

		return lAux;
	}

	public static String getYYYYMMDDtoChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = getYearToChar(pField) + " + " + getMonthToChar(pField) + " + " + getDayToChar(pField);
		}
		else {
			lAux = "to_char(" + pField + ", 'YYYYMMDD')";
		}

		return lAux;

	}

	public static String getHH24MItoChar(String pField, boolean pDoisPontos) {
		String lAux = "";

		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "(REPLICATE('0', (2 - LEN(CAST(DATEPART(hour, " + pField + ") As VARCHAR)))) + CAST(DATEPART(hour, " + pField + ") As VARCHAR)) " + ( pDoisPontos ? " + ':'" : "" ) + " + (REPLICATE('0', (2 - LEN(CAST(DATEPART(minute, " + pField + ") As VARCHAR)))) + CAST(DATEPART(minute, " + pField + ") As VARCHAR))";
		}
		else {
			lAux = "to_char(" + pField + ", 'hh24" + ( pDoisPontos ? ":" : "" ) + "mi')";
		}

		return lAux;
	}

	public static String getHH24MItoChar(String pField) {
		return getHH24MItoChar(pField, false);
	}

	public static String getYYYYMMDDHH24MItoChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = getYearToChar(pField) + " + " + getMonthToChar(pField) + " + " + getDayToChar(pField) + " + " + getHH24MItoChar(pField);
		}
		else {
			lAux = "to_char(" + pField + ", 'YYYYMMDDhh24mi')";
		}

		return lAux;

	}

	public static String getToSimpleDate(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "CONVERT(DATETIME," + pField + ", 103)";
		}
		else {
			lAux = "to_date(" + pField + ", 'dd/MM/yyyy')";
		}
		return lAux;
	}

	public static String getFormatMoedaToChar(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "CAST(CONVERT(MONEY, " + pField + ", 1) AS VARCHAR)";
		}
		else {
			lAux = "to_char(" + pField + ", '999,999,999.99')";
		}
		return lAux;

	}

	public static String convertToSQL(User pUser, String lSQL) {
		String sql = lSQL;

		int indice = sql.lastIndexOf("{@");
		while (indice > -1) {
			sql = sql.substring(0, indice) + castToFunction(pUser, sql.substring(indice));
			indice = indice - 1;
			indice = sql.lastIndexOf("{@", indice);
		}

		return sql;
	}

	/**
	 * castToFunction
	 *
	 * @param string
	 *            String
	 * @return String
	 */
	private static String castToFunction(User pUser, String sql) {
		int i = Database.verifyNullInt(sql.substring(5, 7));
		int aux = 0;
		String pField = "", pField1 = "", pField2 = "", strReplace = "", strReplaced = "";
		String[] pArray = null;
		Database lcdb;

		switch (i) {
			case 0:
				// sql = sql.replaceAll("\\{@get00@\\}",BuildSql.getFromDual());
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getFromDual();
				break;
			case 1:
				// sql = sql.replaceAll("\\{@get01@\\}",BuildSql.getRowNum());
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getRowNum();
				break;
			case 2:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getToNumber(pField);
				break;
			case 3:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getCeil(pField);
				break;
			case 4:
				// sql = sql.replaceAll("\\{@get04@\\}",BuildSql.getSysDate());
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getSysDate();
				break;
			case 5:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getDateDiff(pField, pField1);
				break;
			case 6:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getTruncateDate(pField);
				break;
			case 7:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getDateAddDay(pField, pField1);
				break;
			case 8:
				// sql = sql.replaceAll("\\{@get08@\\}",);
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getConcatCharacter();
				break;
			case 9:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField2 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getCotac(pField, pField1, pField2);
				break;
			case 10:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getExecFunction(pField, pField1);
				break;
			case 11:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getInStr(pField, pField1);
				break;
			case 12:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getToChar(pField);
				break;
			case 13:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getToChar(pField, Boolean.valueOf(pField1).booleanValue());
				break;
			case 14:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getConcat(pField, pField1);
				break;
			case 15:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getAsSubQuery(pField);
				break;
			case 16:
				aux = sql.indexOf("(@[{", 5) + 4;
				pField = sql.substring(aux, sql.indexOf("}]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				pArray = pField.split("@;@");
				// pArray = convertToSQL(pUser, pField).split(",");
				strReplaced = BuildSql.getDecodeText(pArray);
				break;
			case 17:
				aux = sql.indexOf("(@[{", 5) + 4;
				pField = sql.substring(aux, sql.indexOf("}]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				// pArray = pField.split(",");
				pArray = pField.split("@;@");
				strReplaced = BuildSql.getDecodeNumeric(pArray);
				break;
			case 18:
				aux = sql.indexOf("(@[{", 5) + 4;
				pField = sql.substring(aux, sql.indexOf("}]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				// pArray = pField.split(",");
				pArray = pField.split("@;@");
				strReplaced = BuildSql.getDecode(pArray);
				break;
			case 19:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField2 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getSubstring(pField, pField1, pField2);
				break;
			case 20:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField2 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getSubstring(pField, Database.verifyNullInt(pField1), Database.verifyNullInt(pField2));
				break;
			case 21:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getNvl(pField, pField1);
				break;
			case 22:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getRemoveAccent(pField, Boolean.getBoolean(pField1));
				break;
			case 23:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getDayToChar(pField);
				break;
			case 24:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getToCharAddZero(pField, Database.verifyNullInt(pField1));
				break;
			case 25:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getMonthToChar(pField);
				break;
			case 26:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getYearToChar(pField);
				break;
			case 27:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getYearMonthToChar(pField);
				break;
			case 28:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getYearBarMonthToChar(pField);
				break;
			case 29:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getYearMonthToDate(pField);
				break;
			case 30:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getMonthBarYearToDate(pField);
				break;
			case 31:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getDayOfWeek(pField);
				break;
			case 32:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getMonthBarYearToChar(pField);
				break;
			case 33:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getMonthYearToChar(pField);
				break;
			case 34:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getYYYYMMDDtoChar(pField);
				break;
			case 35:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]|@,@|", aux));
				aux = sql.indexOf("|@,@|", aux) + 6;
				pField1 = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getHH24MItoChar(pField, Boolean.getBoolean(pField1));
				break;
			case 36:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getHH24MItoChar(pField);
				break;
			case 37:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getYYYYMMDDHH24MItoChar(pField);
				break;
			case 38:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getToSimpleDate(pField);
				break;
			case 39:
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = BuildSql.getFormatMoedaToChar(pField);
				break;
			case 40:
				lcdb = new Database(pUser);
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = lcdb.dateToChar(pField);
				break;
			case 41:
				lcdb = new Database(pUser);
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = lcdb.dateTimeToChar(pField);
				break;
			case 42:
				lcdb = new Database(pUser);
				aux = sql.indexOf("(@[", 5) + 3;
				pField = sql.substring(aux, sql.indexOf("]@", aux));
				aux = sql.indexOf("@}", aux) + 2;
				strReplaced = pUser.getTermo(pField);
				break;

		}

		strReplace = sql.substring(0, aux).replaceAll("\\+", "\\\\+").replaceAll("\\*", "\\\\*").replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}").replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)").replaceAll("\\|", "\\\\|");
		sql = sql.replaceAll(strReplace, strReplaced).replaceAll("\\'\\'", "\\'");

		return sql;
	}

	// FUN«√O GETTRIM
	public static String getTrim(String pField) {
		String lAux = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lAux = " TRIM(" + pField + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lAux = " TRIM(" + pField + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = " RTRIM(LTRIM(" + pField + ")) ";
		}
		return lAux;
	}

	public static String getBlobLength(String pColumnName) {
		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			return " DATALENGTH(" + pColumnName + ") ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			return " LENGTH(" + pColumnName + ") ";
		}
		else {
			return " DBMS_LOB.GETLENGTH(" + pColumnName + ") ";
		}
	}

	public static String getColumnNamesTypes(String pTableName) {
		String lSQL = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lSQL += " SELECT ";
			lSQL += "   CASE WHEN UPPER(COLUNAS.DATA_TYPE) = 'BLOB' THEN 6 ";
			lSQL += "   WHEN UPPER(COLUNAS.DATA_TYPE) = 'DATE' OR UPPER(COLUNAS.DATA_TYPE) = 'TIMESTAMP' THEN 3";
			lSQL += "   WHEN UPPER(COLUNAS.DATA_TYPE) = 'NUMBER' THEN 1";
			lSQL += "   WHEN UPPER(COLUNAS.DATA_TYPE) = 'VARCHAR2' OR UPPER(COLUNAS.DATA_TYPE) = 'TEXT' THEN 0";
			lSQL += "   ELSE 99 END AS TIPO,";
			lSQL += "   UPPER(COLUNAS.COLUMN_NAME) AS COLUNA ";
			lSQL += " FROM USER_TABLES TABELAS, USER_TAB_COLUMNS COLUNAS ";
			lSQL += " WHERE TABELAS.TABLE_NAME = COLUNAS.TABLE_NAME AND UPPER(TABELAS.TABLE_NAME) = '" + pTableName.toUpperCase().trim() + "' ";
			lSQL += " ORDER BY COLUNA ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lSQL += " SELECT ";
			lSQL += "   CASE WHEN UPPER(TIPOS.NAME) = 'IMAGE' THEN 6 ";
			lSQL += "   WHEN UPPER(TIPOS.NAME) = 'DATE' OR UPPER(TIPOS.NAME) = 'DATETIME' THEN 3";
			lSQL += "   WHEN UPPER(TIPOS.NAME) = 'NUMERIC' THEN 1";
			lSQL += "   WHEN UPPER(TIPOS.NAME) = 'VARCHAR' OR UPPER(TIPOS.NAME) = 'TEXT' THEN 0";
			lSQL += "   ELSE 99 END AS TIPO,";
			lSQL += "   UPPER(COLUNAS.NAME) AS COLUNA ";
			lSQL += " FROM SYSOBJECTS AS TABELAS, SYSCOLUMNS AS COLUNAS, SYSTYPES AS TIPOS ";
			lSQL += " WHERE TABELAS.ID = COLUNAS.ID AND COLUNAS.USERTYPE = TIPOS.USERTYPE AND TABELAS.NAME = '" + pTableName.toUpperCase().trim() + "' ";
			lSQL += " ORDER BY COLUNA ";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lSQL += " SELECT ";
			lSQL += "   CASE WHEN upper(data_type) = 'BYTEA' THEN 6 ";
			lSQL += "   WHEN upper(data_type) = 'DATE' OR upper(data_type) LIKE 'TIMESTAMP%' THEN 3";
			lSQL += "   WHEN upper(data_type) = 'NUMERIC' THEN 1";
			lSQL += "   WHEN upper(data_type) = 'CHARACTER VARYING' OR upper(data_type) = 'TEXT' THEN 0";
			lSQL += "   ELSE 99 END AS TIPO,";
			lSQL += "   UPPER(column_name) AS COLUNA ";
			lSQL += " FROM information_schema.columns WHERE UPPER(table_name) = '" + pTableName.toUpperCase().trim() + "' ";
			lSQL += " ORDER BY COLUNA ";
		}
		return lSQL;

	}

	public static String getTablePrimaries(String pTableName) {
		String lSQL = "";
		if (Database.isDBMS(Database.DBMS_ORACLE)) {
			lSQL += "SELECT ";
			lSQL += "  COLS.COLUMN_NAME ";
			lSQL += "FROM ALL_CONSTRAINTS CONS ";
			lSQL += "  INNER JOIN ALL_CONS_COLUMNS COLS ON (CONS.CONSTRAINT_NAME = COLS.CONSTRAINT_NAME AND CONS.OWNER = COLS.OWNER) ";
			lSQL += "WHERE ";
			lSQL += "  CONS.CONSTRAINT_TYPE = 'P' AND UPPER(COLS.TABLE_NAME) = '" + pTableName + "' ";
			lSQL += "GROUP BY COLS.COLUMN_NAME ";
			lSQL += "ORDER BY COLS.COLUMN_NAME ";
		}
		else if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lSQL += "SELECT ";
			lSQL += "  COLUMN_NAME ";
			lSQL += "FROM ";
			lSQL += "  INFORMATION_SCHEMA.KEY_COLUMN_USAGE ";
			lSQL += "WHERE ";
			lSQL += "  OBJECTPROPERTY(OBJECT_ID(CONSTRAINT_NAME), 'IsPrimaryKey') = 1 ";
			lSQL += "  AND TABLE_NAME = '" + pTableName + "'";
		}
		else if (Database.isDBMS(Database.DBMS_MYSQL)) {
			lSQL += "SELECT ";
			lSQL += "  pg_attribute.attname AS COLUMN_NAME, ";
			lSQL += "  format_type(pg_attribute.atttypid, pg_attribute.atttypmod) ";
			lSQL += "FROM ";
			lSQL += "  pg_index, pg_class, pg_attribute ";
			lSQL += "WHERE pg_class.oid = '" + pTableName + "'::regclass ";
			lSQL += "  AND indrelid = pg_class.oid ";
			lSQL += "  AND pg_attribute.attrelid = pg_class.oid ";
			lSQL += "  AND pg_attribute.attnum = any(pg_index.indkey) ";
			lSQL += "  AND indisprimary ";
		}

		return lSQL;
	}

	// PARA REALIZAR ESTA OPERACAO O SQL DEVERA POSSUIR JOIN COM A TABELA FMDAT
	public static String dateToChar(String pField, String pFormat) {
		String lAux = "";

		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = "CASE \n";
			lAux += "  WHEN LOWER("+pFormat+") = 'mm/dd/yyyy' THEN CONVERT(VARCHAR," + pField + ", 101) \n";
			lAux += "  WHEN LOWER("+pFormat+") = 'yyyy.mm.dd' THEN CONVERT(VARCHAR," + pField + ", 102) \n";
			lAux += "  WHEN LOWER("+pFormat+") = 'dd/mm/yyyy' THEN CONVERT(VARCHAR," + pField + ", 103) \n";
			lAux += "  WHEN LOWER("+pFormat+") = 'dd.mm.yy' THEN CONVERT(VARCHAR," + pField + ", 104) \n";
			lAux += "  WHEN LOWER("+pFormat+") = 'dd-mm-yy' THEN CONVERT(VARCHAR," + pField + ", 105) \n";
			lAux += "  WHEN LOWER("+pFormat+") = 'mm-dd-yy' THEN CONVERT(VARCHAR," + pField + ", 110) \n";
			lAux += "  WHEN LOWER("+pFormat+") = 'yy/mm/dd' THEN CONVERT(VARCHAR," + pField + ", 111) \n";
			lAux += "  WHEN LOWER("+pFormat+") = 'yymmdd' OR LOWER("+pFormat+") = 'yyyymmdd' THEN CONVERT(VARCHAR," + pField + ", 112) \n";
			lAux += "  ELSE CONVERT(VARCHAR," + pField + ", 103) \n";
			lAux += "END ";
		}
		else {
			lAux = "to_char(" + pField + ", "+pFormat+")";
		}

		return lAux;

	}

	// PARA REALIZAR ESTA OPERACAO O SQL DEVERA POSSUIR JOIN COM A TABELA FMDAT
	public static String dateTimeToChar(String pField, String pFormat) {
		String lAux = "";

		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			lAux = dateToChar(pField, pFormat) + " + ' ' + " + BuildSql.getHH24MItoChar(pField, true);
		}
		else {
			lAux = "to_char(" + pField + ", '"+pFormat+"'" + BuildSql.getConcatCharacter() + "' hh24:mi')";
		}

		return lAux;

	}

	// String[][] pKeyFilters = new String[x][2];
	// pKeyFilters[x][0]: deve conter a parte do filtro que ser· passada como VARCHAR para a funÁ„o
	// pKeyFilters[x][1]: deve conter a parte do filtro que ser· o valor da Query externa (da Display)
	public static String getColumnList(String pNameReturn, String pTable, String pColumn, String pWhere, String[][] pKeyFilters) {
		String sql = "";
		String aux = "";

		if (Database.isDBMS(Database.DBMS_SQLSERVER)) {
			for (int i = 0; i < pKeyFilters.length; i++) {
				aux += ( aux.length() > 0 || pWhere.length() > 0 ? " AND " : "" ) + Database.verifyNull(pKeyFilters[i][0]) + " = " + Database.verifyNull(pKeyFilters[i][1]);
			}

			sql += "REPLACE(";
			sql += "(SELECT STUFF( \n";
			sql += "(SELECT ' ' + MY_DESCR + ' |' \n";
			sql += "	FROM  \n";
			sql += "	(SELECT " + pColumn + " AS MY_DESCR \n";
			sql += "  FROM " + pTable + " \n";
			sql += "  WHERE " + pWhere + aux + " \n";
			sql += "	) TAB_COLUMNLIST  \n";
			sql += "	FOR XML PATH('')), 1, 1, '')), ' |', '<br/>') " + ( pNameReturn.equals("") ? "" : " AS " + pNameReturn );
		}
		else {
			for (int i = 0; i < pKeyFilters.length; i++) {
				aux += ( aux.length() > 0 || pWhere.length() > 0 ? " || ' AND " : "'" ) + Database.verifyNull(pKeyFilters[i][0]) + " = ' || " + Database.verifyNull(pKeyFilters[i][1]);
			}

			sql += BuildSql.getExecFunction("FN_DISPLAY_COLUMNLIST", "'" + pTable + "', '" + pColumn + "', " + ( pWhere.length() > 0 ? "'" + pWhere + "'" : "" ) + aux) + ( pNameReturn.equals("") ? "" : " AS " + pNameReturn );
		}

		return sql;
	}
}
