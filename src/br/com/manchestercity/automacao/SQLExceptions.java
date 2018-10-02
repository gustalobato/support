
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class SQLExceptions {
	private ArrayList<UserException> mSqlEx;
	private ArrayList<UserException> mTables;

	public SQLExceptions(User pUser) {
		UserException mEx;
		mSqlEx = new ArrayList<UserException>();

		mEx = new UserException();
		mEx.setNumber("Violation of PRIMARY KEY constraint");
		mEx.setDescription(pUser.getTermo("DBCHAVEVIOLADA"));
		mSqlEx.add(mEx);

		mEx = new UserException();
		mEx.setNumber("REFERENCE constraint");
		mEx.setDescription(pUser.getTermo("DBPOSSUIDEPENDS"));
		mSqlEx.add(mEx);

		mTables = Database.getTables(pUser);
	}

	public String getErrorMessage(String msg) {
		String lReturn = "Erro: ";
		UserException mEx;
		UserException lAux;
		int lint = 0;
		int lk = 0;
		for (lint = 0; lint < mSqlEx.size(); lint++) {
			mEx = mSqlEx.get(lint);
			if (msg.indexOf(mEx.getNumber()) > 0) {
				lReturn += mEx.getDescription();
				if (mEx.getNumber().equals("REFERENCE constraint")) {
					lReturn += " Dependente(s): ";
					for (lk = 0; lk < mTables.size(); lk++) {
						lAux = mTables.get(lk);
						if (msg.toLowerCase().indexOf(lAux.getNumber().toLowerCase()) > 0) {
							lReturn += lAux.getDescription() + ",";
						}
					}
				}
				break;
			}
		}
		return lReturn;
	}
}
