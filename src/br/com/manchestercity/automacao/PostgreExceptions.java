
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class PostgreExceptions {

	private ArrayList<UserException> mPostEx;
	private ArrayList<UserException> mTables;

	public PostgreExceptions(User pUser) {
		UserException mEx;
		mPostEx = new ArrayList<UserException>();

		mEx = new UserException();
		mEx.setNumber("duplicate key violates");
		mEx.setDescription(pUser.getTermo("DBCHAVEVIOLADA"));
		mPostEx.add(mEx);

		mEx = new UserException();
		mEx.setNumber("violates foreign key");
		mEx.setDescription(pUser.getTermo("DBPOSSUIDEPENDS"));
		mPostEx.add(mEx);

		mTables = Database.getTables(pUser);
	}

	public String getErrorMessage(String msg) {
		String lReturn = "Erro: ";
		UserException mEx;
		UserException lAux;
		int lint = 0;
		int lk = 0;
		for (lint = 0; lint < mPostEx.size(); lint++) {
			mEx = mPostEx.get(lint);
			if (msg.indexOf(mEx.getNumber()) > 0) {
				lReturn += mEx.getDescription();
				if (mEx.getNumber().equals("violates foreign key")) {
					lReturn += " Dependente(s): ";
					for (lk = 0; lk < mTables.size(); lk++) {
						lAux = mTables.get(lk);
						if (msg.toLowerCase().indexOf("on table \"" + lAux.getNumber().toLowerCase() + "\"") > 0) {
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
