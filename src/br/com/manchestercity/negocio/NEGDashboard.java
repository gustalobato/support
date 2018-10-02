
package br.com.manchestercity.negocio;

import java.sql.Connection;
import java.sql.ResultSet;

import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;
import br.com.manchestercity.sql.SQLDashboard;

public class NEGDashboard {
	Database lcdb;
	User user;

	public NEGDashboard(User user) {
		this.lcdb = new Database(user);
		this.user = user;
	}

	public ResultSet minhasOfertas(Connection conn, int qtdeDias) {
		try {
			return lcdb.openResultSet(new SQLDashboard(user).minhasOfertas(String.valueOf(user.getUserCode()), qtdeDias), conn);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public ResultSet minhasSolicitacoes(Connection conn, int qtdeDias) {
		try {
			return lcdb.openResultSet(new SQLDashboard(user).minhasSolicitacoes(String.valueOf(user.getUserCode()), qtdeDias), conn);
		}
		catch (Exception ex) {
			return null;
		}
	}

	public ResultSet movimentacoes(Connection conn, String pCD_AREAS_USUPA, String pCD_AREAS_BAIXA, String pCD_AREAS_AJUSTE) {
		try {
			return lcdb.openResultSet(new SQLDashboard(user).movimentacoes(pCD_AREAS_USUPA, pCD_AREAS_BAIXA, pCD_AREAS_AJUSTE), conn);
		}
		catch (Exception ex) {
			return null;
		}
	}
}
