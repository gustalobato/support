
package br.com.manchestercity.sql;

import br.com.manchestercity.automacao.BuildSql;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.DateType;
import br.com.manchestercity.automacao.User;

public class SQLDashboard {

	Database lcdb;

	public SQLDashboard(User user) {
		lcdb = new Database(user);
	}

	public String minhasOfertas(String pCD_USUPA, int qtdeDias) {
		StringBuffer query = new StringBuffer();

		query.append("/* MINHAS OFERTAS */ \n");
		query.append("SELECT TERMO, QTDE, URL, TITULO FROM( \n");
		query.append("\n");
		query.append("  SELECT 'AEXPIRAR' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_oferta&acao_oferta=dashboard&status=E&qtdeDias=" + String.valueOf(qtdeDias) + "&CD_USUPA_OFRTT=" + pCD_USUPA + "' URL, 'HISTORICO' TITULO FROM OFMTS \n");
		query.append("  WHERE CD_USUPA_OFRTT = " + pCD_USUPA + " \n");
		query.append("    AND ID_OFMTS_STATU = 'E' AND " + BuildSql.getDateDiff("OFMTS.DT_OFMTS_FIM_EXTER", lcdb.charToDate("'" + lcdb.getActualDate(DateType.DATE) + "'")) + " BETWEEN 0 AND " + String.valueOf(qtdeDias) + " \n");
		query.append(" \n");
		query.append("  UNION ALL \n");
		query.append(" \n");
		query.append("  SELECT 'EXPIRADAS' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_oferta&acao_oferta=dashboard&status=V&CD_USUPA_OFRTT=" + pCD_USUPA + "' URL, 'HISTORICO' TITULO FROM OFMTS \n");
		query.append("  WHERE CD_USUPA_OFRTT = " + pCD_USUPA + " \n");
		query.append("    AND ID_OFMTS_STATU = 'V' \n");
		query.append(" \n");
		query.append("  UNION ALL \n");
		query.append(" \n");
		query.append("  SELECT 'REQUISITADAS' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_oferta&acao_oferta=dashboard&status=Q&CD_USUPA_OFRTT=" + pCD_USUPA + "' URL, 'HISTORICO' TITULO FROM OFMTS \n");
		query.append("  LEFT JOIN RQMTS ON OFMTS.CD_OFMTS = RQMTS.CD_OFMTS \n");
		query.append("  WHERE CD_USUPA_OFRTT = " + pCD_USUPA + " \n");
		query.append("    AND OFMTS.ID_OFMTS_STATU = 'Q' AND RQMTS.ID_RQMTS_STATU = 'R' \n");
		query.append(" \n");
		query.append("    UNION ALL \n");
		query.append(" \n");
		query.append("  SELECT 'RESERVADAS' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_oferta&acao_oferta=dashboard&status=R&CD_USUPA_OFRTT=" + pCD_USUPA + "' URL, 'HISTORICO' TITULO FROM OFMTS \n");
		query.append("  LEFT JOIN RSMTS ON OFMTS.CD_OFMTS = RSMTS.CD_OFMTS \n");
		query.append("  WHERE CD_USUPA_OFRTT = " + pCD_USUPA + " \n");
		query.append("    AND OFMTS.ID_OFMTS_STATU = 'R' AND RSMTS.ID_RSMTS_STATU = 'A' \n");
		query.append(")MINHASOFERTAS \n");
		query.append(" WHERE QTDE > 0 \n");
		query.append("ORDER BY TERMO \n");

		return query.toString();
	}

	public String minhasSolicitacoes(String pCD_USUPA, int qtdeDias) {
		StringBuffer query = new StringBuffer();

		query.append("/* MINHAS SOLICITAÇÕES */ \n");
		query.append("SELECT TERMO, QTDE, URL, TITULO FROM( \n");
		query.append(" \n");
		query.append("  SELECT 'RESERVADAS' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_oferta&acao_oferta=dashboard&status=R&CD_USUPA_SOLCT=" + pCD_USUPA + "' URL, 'HISTORICO' TITULO FROM OFMTS \n");
		query.append("  LEFT JOIN RSMTS ON OFMTS.CD_OFMTS = RSMTS.CD_OFMTS \n");
		query.append("  WHERE CD_USUPA_SOLCT = " + pCD_USUPA + " \n");
		query.append("    AND OFMTS.ID_OFMTS_STATU = 'R' AND RSMTS.ID_RSMTS_STATU = 'A' \n");
		query.append(" \n");
		query.append("  UNION ALL \n");
		query.append(" \n");
		query.append("  SELECT 'REQUISITADAS' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_oferta&acao_oferta=dashboard&status=Q&CD_USUPA_RQSTE=" + pCD_USUPA + "' URL, 'HISTORICO' TITULO FROM OFMTS \n");
		query.append("  LEFT JOIN RQMTS ON OFMTS.CD_OFMTS = RQMTS.CD_OFMTS \n");
		query.append("  WHERE CD_USUPA_RQSTE = " + pCD_USUPA + " \n");
		query.append("    AND OFMTS.ID_OFMTS_STATU = 'Q' AND RQMTS.ID_RQMTS_STATU = 'R' \n");
		query.append(" \n");
		query.append("  UNION ALL \n");
		query.append(" \n");
		query.append("  SELECT 'AEXPIRAR' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_solicitacao&status=A&qtdeDias=" + String.valueOf(qtdeDias) + "&CD_USUPA=" + pCD_USUPA + "' URL, 'HISTORICO' TITULO FROM SLMTS \n");
		query.append("  WHERE CD_USUPA = " + pCD_USUPA + " \n");
		query.append("    AND ID_SLMTS_STATU = 'A' AND " + BuildSql.getDateDiff("SLMTS.DT_SLMTS_VAL", lcdb.charToDate("'" + lcdb.getActualDate(DateType.DATE) + "'")) + " BETWEEN 0 AND " + String.valueOf(qtdeDias) + " \n");
		query.append(" \n");
		query.append("  UNION ALL \n");
		query.append(" \n");
		query.append("  SELECT 'EXPIRADAS' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_solicitacao&status=V&CD_USUPA=" + pCD_USUPA + "' URL, 'HISTORICO' TITULO FROM SLMTS \n");
		query.append("  WHERE CD_USUPA = " + pCD_USUPA + " \n");
		query.append("    AND ID_SLMTS_STATU = 'V' \n");
		query.append(")MINHASSOLICITACOES \n");
		query.append(" WHERE QTDE > 0 \n");
		query.append("ORDER BY TERMO \n");
		query.append(" \n");

		return query.toString();
	}

	public String movimentacoes(String pCD_AREAS_USUPA, String pCD_AREAS_BAIXA, String pCD_AREAS_AJUSTE) {
		StringBuffer query = new StringBuffer();

		if (pCD_AREAS_USUPA.trim().equals(pCD_AREAS_BAIXA.trim())) {
			query.append("    SELECT 'AGUARDANDOBAIXA' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_oferta&acao_oferta=baixa_oferta&status=V' URL, 'FINALIZAROFERTA' TITULO FROM OFMTS \n");
			query.append("    WHERE OFMTS.ID_OFMTS_STATU = 'V' \n");
		}
		if (pCD_AREAS_USUPA.trim().equals(pCD_AREAS_AJUSTE.trim())) {
			if (query.length() > 0) {
				query.append(" \n");
				query.append("    UNION ALL \n");
				query.append(" \n");
			}
			query.append("    SELECT 'AGUARDANDOAJUSTE' TERMO, COUNT(1) QTDE, 'displaydatalist?funcao=cad_oferta&acao_oferta=ajuste_valor_oficial' URL, 'AJUSTARVALORES' TITULO FROM OFMTS \n");
			query.append("    WHERE OFMTS.ID_OFMTS_STATU = 'F' AND OFMTS.VL_OFMTS_MOEDA_NAC_OFI IS NULL \n");
		}

		return "SELECT TERMO, QTDE, URL, TITULO FROM( \n" + query.toString() + ")MOVIMENTACOES \n WHERE QTDE > 0";
	}
}
