
package br.com.manchestercity.negocio;

import java.sql.Connection;
import java.sql.ResultSet;

import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.Mail;
import br.com.manchestercity.automacao.User;
import br.com.manchestercity.automacao.ValidateReturn;
import br.com.manchestercity.sql.SQLUSUPA;

public class NEGUSUPA {
	Database lcdb;
	User user;

	String mCD_USUPA;
	String mCD_MTAUS;
	String mCD_AREAS;
	String mNM_USUPA;
	String mNM_USUPA_EMAIL;
	String mNO_USUPA_RAMAL;
	String mNM_USUPA_LOGIN;
	String mNM_USUPA_SENHA;
	String mID_USUPA_STATU;

	public NEGUSUPA(User user) {
		this.lcdb = new Database(user);
		this.user = user;

		this.nullFields();
	}

	public void nullFields() {
		this.mCD_USUPA = null;
		this.mCD_MTAUS = null;
		this.mCD_AREAS = null;
		this.mNM_USUPA = null;
		this.mNM_USUPA_EMAIL = null;
		this.mNO_USUPA_RAMAL = null;
		this.mNM_USUPA_LOGIN = null;
		this.mNM_USUPA_SENHA = null;
		this.mID_USUPA_STATU = null;
	}

	public void emptyFields() {
		this.mCD_USUPA = "";
		this.mCD_MTAUS = "";
		this.mCD_AREAS = "";
		this.mNM_USUPA = "";
		this.mNM_USUPA_EMAIL = "";
		this.mNO_USUPA_RAMAL = "";
		this.mNM_USUPA_LOGIN = "";
		this.mNM_USUPA_SENHA = "";
		this.mID_USUPA_STATU = "";
	}

	public void setCD_USUPA(String mCD_USUPA) {
		this.mCD_USUPA = mCD_USUPA;
	}

	public String getCD_USUPA() {
		return mCD_USUPA;
	}

	public void setCD_MTAUS(String mCD_MTAUS) {
		this.mCD_MTAUS = mCD_MTAUS;
	}

	public String getCD_MTAUS() {
		return mCD_MTAUS;
	}

	public void setCD_AREAS(String mCD_AREAS) {
		this.mCD_AREAS = mCD_AREAS;
	}

	public String getCD_AREAS() {
		return mCD_AREAS;
	}

	public void setNM_USUPA(String mNM_USUPA) {
		this.mNM_USUPA = mNM_USUPA;
	}

	public String getNM_USUPA() {
		return mNM_USUPA;
	}

	public void setNM_USUPA_EMAIL(String mNM_USUPA_EMAIL) {
		this.mNM_USUPA_EMAIL = mNM_USUPA_EMAIL;
	}

	public String getNM_USUPA_EMAIL() {
		return mNM_USUPA_EMAIL;
	}

	public void setNO_USUPA_RAMAL(String mNO_USUPA_RAMAL) {
		this.mNO_USUPA_RAMAL = mNO_USUPA_RAMAL;
	}

	public String getNO_USUPA_RAMAL() {
		return mNO_USUPA_RAMAL;
	}

	public void setNM_USUPA_LOGIN(String mNM_USUPA_LOGIN) {
		this.mNM_USUPA_LOGIN = mNM_USUPA_LOGIN;
	}

	public String getNM_USUPA_LOGIN() {
		return mNM_USUPA_LOGIN;
	}

	public void setNM_USUPA_SENHA(String mNM_USUPA_SENHA) {
		if (mNM_USUPA_SENHA != null && mNM_USUPA_SENHA.trim().equals("******")) {
			this.mNM_USUPA_SENHA = null;
		}
		else {
			this.mNM_USUPA_SENHA = mNM_USUPA_SENHA;
		}
	}

	public String getNM_USUPA_SENHA() {
		return mNM_USUPA_SENHA;
	}

	public void setID_USUPA_STATU(String mID_USUPA_STATU) {
		this.mID_USUPA_STATU = mID_USUPA_STATU;
	}

	public String getID_USUPA_STATU() {
		return mID_USUPA_STATU;
	}

	public long insert(Connection conn, StringBuffer errors) {
		try {
			return SQLUSUPA.insert(lcdb, conn, errors, this.mCD_USUPA, this.mCD_MTAUS, this.mCD_AREAS, this.mNM_USUPA, this.mNM_USUPA_EMAIL, this.mNO_USUPA_RAMAL, this.mNM_USUPA_LOGIN, this.mNM_USUPA_SENHA, this.mID_USUPA_STATU);
		}
		catch (Exception e) {
			errors.append(e.getMessage());
		}
		return -1;
	}

	public boolean update(Connection conn, StringBuffer errors) {
		try {
			return new SQLUSUPA(user).update(conn, errors, this.mCD_USUPA, this.mCD_MTAUS, this.mCD_AREAS, this.mNM_USUPA, this.mNM_USUPA_EMAIL, this.mNO_USUPA_RAMAL, this.mNM_USUPA_LOGIN, this.mNM_USUPA_SENHA, this.mID_USUPA_STATU);
		}
		catch (Exception e) {
			errors.append(e.getMessage());
		}
		return false;
	}

	public boolean delete(Connection conn, StringBuffer errors) {
		try {
			return new SQLUSUPA(user).delete(conn, errors, mCD_USUPA);
		}
		catch (Exception e) {
			errors.append(e.getMessage());
		}
		return false;
	}

	public ResultSet refresh(Connection conn, String pCD_USUPA, String pCD_MTAUS, String pCD_AREAS, String pNM_USUPA, String pNM_USUPA_EMAIL, String pNO_USUPA_RAMAL, String pNM_USUPA_LOGIN, String pNM_USUPA_SENHA, String pID_USUPA_STATU, String pOrderBy) {
		try {
			return lcdb.openResultSet(new SQLUSUPA(user).refresh(pCD_USUPA, pCD_MTAUS, pCD_AREAS, pNM_USUPA, pNM_USUPA_EMAIL, pNO_USUPA_RAMAL, pNM_USUPA_LOGIN, pNM_USUPA_SENHA, pID_USUPA_STATU, pOrderBy), conn);
		}
		catch (Exception e) {
			return null;
		}
	}

	public ResultSet refreshPermissoes(Connection conn, String pCD_USUPA) {
		try {
			return lcdb.openResultSet(new SQLUSUPA(user).refreshPermissoes(pCD_USUPA), conn);
		}
		catch (Exception e) {
			return null;
		}
	}

	public ResultSet permissoesPadrao(Connection conn) {
		try {
			return lcdb.openResultSet(new SQLUSUPA(user).permissoesPadrao(), conn);
		}
		catch (Exception e) {
			return null;
		}
	}

	public ValidateReturn validate(Connection conn) {
		ValidateReturn validate = new ValidateReturn();
		StringBuffer message = new StringBuffer();

		if (Database.verifyNull(mNM_USUPA).trim().equals("")) {
			message.append(Database.validateMessage(user.getTermo("VALIDACAMPO"), user.getTermo("NOME")) + "\\n");
			validate.addMandatoryId("_field_NM_USUPA");
		}
		if (Database.verifyNull(mNM_USUPA_EMAIL).trim().equals("")) {
			message.append(Database.validateMessage(user.getTermo("VALIDACAMPO"), user.getTermo("EMAIL")) + "\\n");
			validate.addMandatoryId("_field_NM_USUPA_EMAIL");
		}
		else if (!Mail.validateEmail(mNM_USUPA_EMAIL.trim())) {
			message.append(user.getTermo("EMAILINVALIDO") + "\\n");
			validate.addMandatoryId("_field_NM_USUPA_EMAIL");
		}
	
		if (Database.verifyNull(mNM_USUPA_LOGIN).trim().equals("")) {
			message.append(Database.validateMessage(user.getTermo("VALIDACAMPO"), user.getTermo("LOGIN")) + "\\n");
			validate.addMandatoryId("_field_NM_USUPA_LOGIN");
		}
		if (Database.verifyNull(mNM_USUPA_SENHA).trim().equals("")) {
			message.append(Database.validateMessage(user.getTermo("VALIDACAMPO"), user.getTermo("SENHA")) + "\\n");
			validate.addMandatoryId("_field_NM_USUPA_SENHA");
		}
		else if (mNM_USUPA_SENHA.trim().length() < 6) {
			message.append(user.getTermo("MSGSENHA6CARAC") + "\\n");
			validate.addMandatoryId("_field_NM_USUPA_SENHA");
		}
		
		if (Database.verifyNull(mID_USUPA_STATU).trim().equals("")) {
			message.append(Database.validateMessage(user.getTermo("VALIDACAMPO"), user.getTermo("STATUS")) + "\\n");
			validate.addMandatoryId("_field_ID_USUPA_STATU");
		}

		validate.setMessage(message.toString());

		return validate;
	}

}
