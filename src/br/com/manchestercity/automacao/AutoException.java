
package br.com.manchestercity.automacao;

public class AutoException extends Exception {

	private static final long serialVersionUID = 3758777695689857369L;

	User mUser;

	public AutoException(User pUser) {
		super();
		this.mUser = pUser;
	}

	public AutoException(User pUser, String s) {
		super(s);
		this.mUser = pUser;
	}

	public String getMessage() {
		String lAux = "";
		String lMsg = "";

		lMsg = super.getMessage();

		if (IniManipulation.getProperty(IniManipulation.DBMS).equalsIgnoreCase("sqlserver")) {
			lAux = new SQLExceptions(mUser).getErrorMessage(lMsg);
		}
		else if (IniManipulation.getProperty(IniManipulation.DBMS).equalsIgnoreCase("postgre")) {
			lAux = new PostgreExceptions(mUser).getErrorMessage(lMsg);
		}
		else {
			lAux = new OracleExceptions(mUser).getErrorMessage(lMsg);
		}
		if (lAux.equalsIgnoreCase("Erro: ")) {
			return lMsg;
		}
		else {
			return lAux;
		}
	}
}
