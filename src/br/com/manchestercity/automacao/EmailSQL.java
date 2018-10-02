
package br.com.manchestercity.automacao;

public class EmailSQL {
	
	private static final String DATE_FORMAT = LangManipulation.getLanguageTranslation(User.DEFAULT_LANGUAGE.toLowerCase() + ".properties", LangManipulation.FORMAT_DATE);
	
	public static final String SQL_EMAIL_USUARIO = "select \n"
			+ "  USUPA.NM_USUPA, \n"
			+ "  USUPA.NM_USUPA_EMAIL, \n"
			+ "  USUPA.NM_USUPA_LOGIN, \n"
			+ "  USUPA.NM_USUPA_SENHA, \n"
			+ "  MTAUS.NM_MTAUS \n"
			+ "from USUPA \n"
			+ "  LEFT JOIN MTAUS ON USUPA.CD_MTAUS = MTAUS.CD_MTAUS  \n"
			+ "WHERE {sql_where_key} ";

	public static final String SQL_EMAIL_OFERTA = "SELECT \n"
			+ "  OFMTS.CD_OFMTS, \n"
			+ "  AREAS.NM_AREAS, \n"
			+ "  USUPA.NM_USUPA, \n"
			+ "  "+BuildSql.dateToChar("OFMTS.DT_OFMTS_REG","'"+DATE_FORMAT+"'")+" AS DT_OFMTS_REG, \n"
			+ "  "+BuildSql.dateToChar("OFMTS.DT_OFMTS_FIM_INTR","'"+DATE_FORMAT+"'")+" AS DT_OFMTS_FIM_INTR, \n"
			+ "  "+BuildSql.dateToChar("OFMTS.DT_OFMTS_FIM_EXTER","'"+DATE_FORMAT+"'")+" AS DT_OFMTS_FIM_EXTER, \n"
			+ "  "+BuildSql.dateToChar("OFMTS.DT_OFMTS_ENCR","'"+DATE_FORMAT+"'")+" AS DT_OFMTS_ENCR, \n"			
			+ "  CTMTS.NM_CTMTS, \n"
			+ "  SCMTS.NM_SCMTS, \n"
			+ "  UNMDS.NM_UNMDS, \n"
			+ "  OFMTS.DS_OFMTS_RESM, \n"
			+ "  OFMTS.DS_OFMTS_DETL, \n"
			+ "  OFMTS.NO_OFMTS_QT \n"
			+ "FROM OFMTS \n"
			+ "LEFT JOIN AREAS ON OFMTS.CD_AREAS_OFRTT = AREAS.CD_AREAS \n"
			+ "LEFT JOIN USUPA ON OFMTS.CD_USUPA_OFRTT = USUPA.CD_USUPA \n"
			+ "LEFT JOIN SCMTS ON OFMTS.CD_SCMTS = SCMTS.CD_SCMTS \n"
			+ "LEFT JOIN CTMTS ON SCMTS.CD_CTMTS = CTMTS.CD_CTMTS \n"
			+ "LEFT JOIN UNMDS ON SCMTS.CD_UNMDS = UNMDS.CD_UNMDS \n"
			+ "WHERE {sql_where_key} ";	 
	
	public static final String SQL_EMAIL_RESERVA = "SELECT \n"
			+ "  RSMTS.CD_RSMTS, \n"
			+ "  RSMTS.CD_OFMTS, \n"
			+ "  RSMTS.CD_SLMTS, \n"
			+ "  AREAS.NM_AREAS, \n"
			+ "  USUPA.NM_USUPA, \n"
			+ "  "+BuildSql.dateToChar("RSMTS.DT_RSMTS_REG","'"+DATE_FORMAT+"'")+" AS DT_RSMTS_REG, \n"
			+ "  "+BuildSql.dateToChar("RSMTS.DT_RSMTS_VAL","'"+DATE_FORMAT+"'")+" AS DT_RSMTS_VAL, \n"
			+ "  "+BuildSql.dateToChar("RSMTS.DT_RSMTS_ENCR","'"+DATE_FORMAT+"'")+" AS DT_RSMTS_ENCR, \n"
			+ "  RSMTS.NO_RSMTS_QT \n"
			+ "FROM RSMTS \n"
			+ "LEFT JOIN AREAS ON RSMTS.CD_AREAS = AREAS.CD_AREAS \n"
			+ "LEFT JOIN USUPA ON RSMTS.CD_USUPA_SOLCT = USUPA.CD_USUPA \n"
			+ "WHERE {sql_where_key} ";
	
	public static final String SQL_EMAIL_REQUISICAO =  "SELECT \n"
			+ "  RQMTS.CD_RQMTS, \n"
			+ "  RQMTS.CD_OFMTS, \n"
			+ "  RQMTS.CD_RSMTS, \n"
			+ "  RQMTS.CD_SLMTS, \n"
			+ "  AREAS.NM_AREAS, \n"
			+ "  USUPA.NM_USUPA, \n"
			+ "  "+BuildSql.dateToChar("RQMTS.DT_RQMTS_REG","'"+DATE_FORMAT+"'")+" AS DT_RQMTS_REG, \n"
			+ "  "+BuildSql.dateToChar("RQMTS.DT_RQMTS_ENCR","'"+DATE_FORMAT+"'")+" AS DS_RQMTS_ENCR, \n"
			+ "  RQMTS.NO_RQMTS_QT \n"
			+ "FROM RQMTS \n"
			+ "LEFT JOIN AREAS ON RQMTS.CD_AREAS = AREAS.CD_AREAS \n"
			+ "LEFT JOIN USUPA ON RQMTS.CD_USUPA_RQSTE = USUPA.CD_USUPA \n"
			+ "WHERE {sql_where_key} ";

	
	public static final String SQL_EMAIL_SOLICITACAO =  "SELECT \n"
			+ "  SLMTS.CD_SLMTS,      \n"
			+ "  AREAS.NM_AREAS,      \n"
			+ "  USUPA.NM_USUPA,      \n"
			+ "  "+BuildSql.dateToChar("SLMTS.DT_SLMTS_REG","'"+DATE_FORMAT+"'")+" AS DT_SLMTS_REG,  \n"
			+ "  "+BuildSql.dateToChar("SLMTS.DT_SLMTS_VAL","'"+DATE_FORMAT+"'")+" AS DT_SLMTS_VAL,  \n"
			+ "  CTMTS.NM_CTMTS,      \n"
			+ "  SCMTS.NM_SCMTS,      \n"
			+ "  UNMDS.NM_UNMDS,      \n"
			+ "  SLMTS.DS_SLMTS_RESM, \n"
			+ "  SLMTS.DS_SLMTS_DETL, \n"
			+ "  SLMTS.NO_SLMTS_QT,   \n"
			+ "  "+BuildSql.dateToChar("SLMTS.DT_SLMTS_ENCR","'"+DATE_FORMAT+"'")+" AS DT_SLMTS_ENCR  \n"
			+ "FROM SLMTS             \n"
			+ "LEFT JOIN AREAS ON SLMTS.CD_AREAS = AREAS.CD_AREAS \n"
			+ "LEFT JOIN USUPA ON SLMTS.CD_USUPA = USUPA.CD_USUPA \n"
			+ "LEFT JOIN SCMTS ON SLMTS.CD_SCMTS = SCMTS.CD_SCMTS \n"
			+ "LEFT JOIN CTMTS ON SCMTS.CD_CTMTS = CTMTS.CD_CTMTS \n"
			+ "LEFT JOIN UNMDS ON SCMTS.CD_UNMDS = UNMDS.CD_UNMDS \n"
			+ "WHERE {sql_where_key} ";
	
}
