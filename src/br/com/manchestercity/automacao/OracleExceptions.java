
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class OracleExceptions {
	private ArrayList<UserException> mOraEx;
	private ArrayList<UserException> mTables;

	public OracleExceptions(User pUser) {
		UserException mEx;
		mOraEx = new ArrayList<UserException>();

		mEx = new UserException();
		mEx.setNumber("ORA-00001");
		mEx.setDescription(pUser.getTermo("DBCHAVEVIOLADA"));
		mOraEx.add(mEx);

		mEx = new UserException();
		mEx.setNumber("ORA-02292");
		mEx.setDescription(pUser.getTermo("DBPOSSUIDEPENDS"));
		mOraEx.add(mEx);

		mTables = Database.getTables(pUser);
	}

	public String getErrorMessage(String msg) {
		String lReturn = "Erro: ";
		UserException mEx;
		UserException lAux;
		int lint = 0;
		int lk = 0;
		for (lint = 0; lint < mOraEx.size(); lint++) {
			mEx = mOraEx.get(lint);
			if (msg.indexOf(mEx.getNumber()) > 0) {
				lReturn += mEx.getDescription();
				if (mEx.getNumber().equals("ORA-02292")) {
					lReturn += " Dependência entre: ";
					for (lk = 0; lk < mTables.size(); lk++) {
						lAux = mTables.get(lk);
						if (msg.indexOf(lAux.getNumber()) > 0) {
							lReturn += lAux.getDescription() + " e ";
						}
					}
					lReturn = lReturn.substring(0, lReturn.length() - 3);
				}
				break;
			}
		}
		return lReturn;
	}
}
