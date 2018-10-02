
package br.com.manchestercity.automacao;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PassAuthenticator extends Authenticator {

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(IniManipulation.getProperty(IniManipulation.MAIL_USER), IniManipulation.getProperty(IniManipulation.MAIL_PWD));
	}
}
