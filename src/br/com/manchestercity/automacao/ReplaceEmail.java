
package br.com.manchestercity.automacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.mail.MessagingException;

import br.com.manchestercity.SystemConfig;

public abstract class ReplaceEmail {

	public static String[] getReplacedEmail(User pUser, Connection pConn, EmailType pEmailType, DataControl[] pKeys, boolean pUseTPLEM) {
		return getReplacedEmail(pUser, pConn, pEmailType, pKeys, pUseTPLEM, "", "", "");
	}

	public static String[] getReplacedEmail(User pUser, Connection pConn, EmailType pEmailType, DataControl[] pKeys, boolean pUseTPLEM, String pAssunto, String pMensagem, String pTemplate) {

		Database lcdb = new Database(pUser);
		String[] email = new String[3];

		if (pMensagem.trim().equals("")) {
			//email = pUser.getEmailByTermo(pConn, pEmailType.getChave());
			if (email == null) {
				// Manda e-mail caso um e-mail não exista no banco de dados
				if (!IniManipulation.getProperty(IniManipulation.MAIL_LOG_DB).equals("")) {
					Mail lMail = new Mail(pUser);
					String lAcionado = "Erro acionado a partir de: ReplaceEmail.getReplacedEmail<br><br>";
					String lMensagem = "O e-mail \"" + pEmailType.getChave() + "\" não está cadastrado no banco de dados e não poderá ser enviado.";
					try {
						lMail.postMail(IniManipulation.getProperty(IniManipulation.MAIL_LOG_DB), lAcionado + "<hr>" + lMensagem, SystemConfig.getSystemName() + " :: LOG EMAIL", "", SystemConfig.getSystemName(), true);
					}
					catch (MessagingException ex) {
						Utils.printSystemError("ReplaceEmail.getReplacedEmail(): ", ex.getMessage());
					}
				}
				// RETORNA UM E-MAIL VAZIO
				return new String[3];
			}
		}
		else {
			email[0] = pAssunto;
			email[1] = pMensagem;
			if (pTemplate.trim().equals("")) {
				pUseTPLEM = false;
			}
			else {
				email[2] = pTemplate;
			}
		}

		HashMap<String, String> fields = new HashMap<String, String>();
		ResultSet rs = null;

		Calendar calendario = Calendar.getInstance();
	
		String replaceWhere = createWhereEmail(lcdb, pKeys);
		if (!replaceWhere.equals("")) {
			rs = lcdb.openResultSet(Utils.replaceAll(pEmailType.getSql(), "{sql_where_key}", replaceWhere), pConn);
			try {
				if (rs.next()) {
					ResultSetMetaData rsmd = rs.getMetaData();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						fields.put(rsmd.getColumnName(i), Database.verifyNull(rs.getObject(i)));
					}
				}
			}
			catch (Exception ex) {
				Utils.printSystemError("ReplaceEmail.getReplacedEmail(): ", ex.getMessage());
			}
			finally {
				Database.closeObject(rs);
			}
		}

		String assunto = Database.verifyNull(email[0]);
		String mensagem = Database.verifyNull(email[1]);

		String value = "";

		try {
			ArrayList<EmailField> campos = pEmailType.getCampos();

			EmailField auxField;
			for (int i = 0; i < campos.size(); i++) {
				auxField = campos.get(i);
				try {
					value = Database.verifyNull(fields.get(auxField.getFieldSQL()));
					assunto = Utils.replaceAll(assunto, "{" + auxField.getFieldEmail() + "}", auxField.getDomainItem(pUser, value, true));

					// A SENHA É GERADA AUTOMATICAMENTE AO SOLICITAR UMA MUDANÇA DE SENHA
					// A SUBSTITUIÇÃO DEVE SER FEITA APÓS A SUBSTITUIÇÃO DE E-MAIL PADRÃO, PARA CADA PESSOA
						mensagem = Utils.replaceAll(mensagem, "{" + auxField.getFieldEmail() + "}", "{" + auxField.getFieldEmail() + "}");
					
					
				}
				catch (Exception ex) {
					System.err.println("ERROR: ReplaceEmail: getReplacedEmail(): " + ex.getMessage());
				}
			}

		}
		catch (Exception e) {
			System.err.println("ERROR: ReplaceEmail.getReplacedEmail: " + e.getMessage());
		}

		email[0] = assunto;
		if (pUseTPLEM) {
			email[1] = replaceTemplate(email[2], mensagem, assunto, pUser);
		}
		else {
			email[1] = mensagem;
		}

		return email;
	}

	public static String replaceTemplate(String pTemplate, String pCorpo, String pAssunto, User pUser) {
		String retorno = "";

		if (!Database.verifyNull(pTemplate).equals("")) {
			retorno = Utils.replaceAll(pTemplate, "{SUBJECT}", pAssunto);
			retorno = Utils.replaceAll(retorno, "{CONTENT}", pCorpo);
			retorno = Utils.replaceAll(retorno, "{SYSTEM}", SystemConfig.getSystemName());
		}
		else {
			retorno = pCorpo;
		}

		return retorno;
	}

	public static String createWhereEmail(Database lcdb, DataControl[] pKeys) {
		String filter = "";

		for (int j = 0; j < pKeys.length; j++) {
			if (pKeys[j] != null && pKeys[j].getType() != null && !pKeys[j].getValue().equals("") && !pKeys[j].getField().equals("")) {
				if (filter.length() > 0) {
					filter += " AND ";
				}
				filter += pKeys[j].getField() + " = " + lcdb.verifyInsertNull(pKeys[j].getValue(), pKeys[j].getType());
			}
		}

		return filter;
	}
}
