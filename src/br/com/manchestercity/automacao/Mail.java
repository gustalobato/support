
package br.com.manchestercity.automacao;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import br.com.manchestercity.SystemConfig;

public class Mail {

	public static final int TYPE_RECIPIENT_TO = 1;
	public static final int TYPE_RECIPIENT_CC = 2;
	public static final int TYPE_RECIPIENT_BCC = 3;

	Database lcdb;
	public ArrayList<AddressTo> mAddressTo;
	public ArrayList<Mail.Attachment> mAttachments;
	private BufferedReader template;

	// Classe para destinatários
	public class AddressTo {
		private InternetAddress address;
		private int type;

		public InternetAddress getAddress() {
			return address;
		}

		public void setType(int type) {
			this.type = type;
		}

		public void setAddress(InternetAddress address) {
			this.address = address;
		}

		public int getType() {
			return type;
		}

		public AddressTo() {
			// Type 1->TO, 2->CC, 3->BCC
			address = null;
			type = 1;
		}
	}

	// Classe para anexos...
	public class Attachment {
		private byte[] file;
		private String name;
		private boolean embedded;
		private String cid;

		public boolean isEmbedded() {
			return embedded;
		}

		public String getName() {
			return name;
		}

		public void setFile(byte[] file) {
			this.file = file;
		}

		public void setEmbedded(boolean embedded) {
			this.embedded = embedded;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setCid(String cid) {
			this.cid = cid;
		}

		public byte[] getFile() {
			return file;
		}

		public String getCid() {
			return cid;
		}

		public Attachment() {
			this.file = null;
			this.name = "";
			this.embedded = false;
			this.cid = "";
		}
	}

	// Classe para autenticaçao de envio
	static class SMTPAuthenticator extends Authenticator {
		String user;
		String pass;

		SMTPAuthenticator(String user, String pass) {
			this.user = user;
			this.pass = pass;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, pass);
		}
	}

	// Classe para envio de e-mails
	public class ThreadEnvio extends Thread {
		MimeMessage msg;

		public ThreadEnvio(MimeMessage pMsg) {
			this.msg = pMsg;
		}

		public void run() {
			try {
				Transport.send(msg);
			}
			catch (SendFailedException ex) {
				System.out.println(SystemConfig.getSystemName() + " - Error: (send mail) " + ex.getMessage());
				ex.printStackTrace();
				// System.out.println("\nEMAILS INVÁLIDOS>>>" + getEmails(ex.getInvalidAddresses()));
				// System.out.println("\nEMAILS ENVIADOS>>>" + getEmails(ex.getValidSentAddresses()));
				// System.out.println("\nEMAILS NÃO ENVIADOS>>>" + getEmails(ex.getValidUnsentAddresses()));
			}
			catch (Exception ex) {
				System.out.println(SystemConfig.getSystemName() + " - Error: (send mail) " + ex.getMessage());
				ex.printStackTrace();
			}
		}

		public String getEmails(Address[] addrs) {
			StringBuffer ret = new StringBuffer();
			if (addrs != null && addrs.length > 0) {
				for (int i = 0; i < addrs.length; i++) {
					ret.append(( ret.length() > 0 ? "; " : "" ) + addrs[i].toString());
				}
			}
			return ret.toString();
		}
	}

	public Mail(User pUser) {
		lcdb = new Database(pUser);
		// mMultipart = new MimeMultipart("mixed");
		mAttachments = new ArrayList<Mail.Attachment>();
		mAddressTo = new ArrayList<AddressTo>();

	}

	private MimeMessage initMessage(String pFrom, String pFromName, String pTo, String pSubject) {
		String mailServer = IniManipulation.getProperty(IniManipulation.MAIL_SERVER).trim();
		String mailUser = IniManipulation.getProperty(IniManipulation.MAIL_USER).trim();
		String mailPwd = IniManipulation.getProperty(IniManipulation.MAIL_PWD).trim();
		String mailAuth = IniManipulation.getProperty(IniManipulation.MAIL_AUTH).trim();
		String mailPort = IniManipulation.getProperty(IniManipulation.MAIL_PORT);

		if (pFromName.equals("")) {
			pFromName = ( IniManipulation.getProperty(IniManipulation.MAIL_FROM_NAME).equals("") ) ? SystemConfig.getSystemName() : IniManipulation.getProperty(IniManipulation.MAIL_FROM_NAME).trim();
		}

		if (pFrom.trim().equals("")) {
			pFrom = IniManipulation.getProperty(IniManipulation.MAIL_FROM).trim();
			if (pFrom.trim().equals("")) {
				pFrom = mailUser;
			}
		}

		boolean debug = false;

		// Set the host smtp address
		Properties props = new Properties();
		props.clear();
		props.put("mail.smtp.host", mailServer);
		props.put("mail.smtp.auth", mailAuth);
		props.put("mail.smtp.sendpartial", "true");
		if (mailPort.trim().length() > 0) {
			props.put("mail.smtp.port", mailPort);
		}

		// create some properties and get the default Session
		Session session = null;
		if (mailAuth.equalsIgnoreCase("false")) {
			session = Session.getInstance(props, null);
		}
		else {
			session = Session.getInstance(props, new SMTPAuthenticator(mailUser, mailPwd));
		}
		session.setDebug(debug);

		// create a message
		MimeMessage msg = new MimeMessage(session);

		try {
			// set the from and to address
			InternetAddress addressFrom = null;
			try {
				addressFrom = new InternetAddress(pFrom, pFromName);
			}
			catch (Exception ex) {
				addressFrom = new InternetAddress(pFrom);
			}
			msg.setFrom(addressFrom);
		}
		catch (Exception ex1) {
		}

		try {
			msg.setSubject(pSubject, lcdb.mUser.getUserCharset());
		}
		catch (MessagingException ex2) {
		}

		return msg;

	}

	public void addAddressEmail(String pTo, int pType) {
		InternetAddress addressTo;
		AddressTo lAddress;
		try {

			String[] toArry = pTo.split(";");

			for (int i = 0; i < toArry.length; i++) {
				if (validateEmail(toArry[i])) {
					addressTo = new InternetAddress(toArry[i]);
					lAddress = new AddressTo();
					lAddress.setAddress(addressTo);
					lAddress.setType(pType);
					mAddressTo.add(lAddress);
				}
			}

		}
		catch (AddressException ex) {
			System.out.println("CMail > addAddressEmail > " + ex.getMessage());
		}
	}

	public void setRecipientToMessage(MimeMessage pMsg) {
		AddressTo lAddress;

		for (int i = 0; i < mAddressTo.size(); i++) {
			lAddress = (AddressTo) mAddressTo.get(i);
			try {
				if (lAddress.getType() == 1) {// To
					pMsg.addRecipient(MimeMessage.RecipientType.TO, lAddress.getAddress());
				}
				else if (lAddress.getType() == 2) {// CC
					pMsg.addRecipient(MimeMessage.RecipientType.CC, lAddress.getAddress());
				}
				else if (lAddress.getType() == 3) {// BCC
					pMsg.addRecipient(MimeMessage.RecipientType.BCC, lAddress.getAddress());
				}
			}
			catch (MessagingException ex) {
			}
		}
	}

	public String addFile(byte pByte[], String pName) {
		return addFile(pByte, pName, false);
	}

	public String addFile(byte pByte[], String pName, boolean pEmbedded) {
		Mail.Attachment lAttach = new Mail.Attachment();
		lAttach.setFile(pByte);
		lAttach.setName(pName);
		lAttach.setEmbedded(pEmbedded);
		lAttach.setCid("image" + mAttachments.size() + "@attach");
		mAttachments.add(lAttach);

		return lAttach.getCid();
	}

	public void setAttachments(ArrayList<Mail.Attachment> pAttachments) {
		mAttachments = pAttachments;
	}

	private void setAttachmentsToBody(MimeMultipart pMultiPartAttach) {
		Attachment lAttach = null;
		BufferedOutputStream bos = null;
		for (int i = 0; i < mAttachments.size(); i++) {
			lAttach = (Attachment) mAttachments.get(i);
			try {
				ByteArrayDataSource lDTS = new ByteArrayDataSource(lAttach.getFile(), "application/octet-stream");
				DataHandler lDataHandler = new DataHandler(lDTS);
				MimeBodyPart lMimeBodyPart = new MimeBodyPart();
				
				lMimeBodyPart.setFileName(lAttach.getName());
				File f = new File(lAttach.getName());
				bos = new BufferedOutputStream(new FileOutputStream(f));
				bos.write(lAttach.file);
				bos.close();
				lMimeBodyPart.attachFile(f);
				if (lAttach.isEmbedded()) {
					lMimeBodyPart.setContentID("<" + lAttach.getCid() + ">");
					lMimeBodyPart.setDisposition(Part.INLINE);
				}
				else {
			          lMimeBodyPart.setDisposition(Part.ATTACHMENT);
			    }
				lMimeBodyPart.setDataHandler(lDataHandler);

				pMultiPartAttach.addBodyPart(lMimeBodyPart);
			}
			catch (MessagingException ex) {
				System.out.println(ex.getMessage());
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public void postMail(String pTo, String pText, String pSubject, String pFrom) throws MessagingException {

		if (!Database.verifyNull(pTo).equals("") && !Database.verifyNull(pText).equals("")) {
			postMail(pTo, pText, pSubject, pFrom, pFrom, false);
		}

	}

	public void buildMessage(MimeMessage msg, String pText, boolean isHTML) throws MessagingException {
		MimeMultipart cover = new MimeMultipart("alternative");
		MimeBodyPart html = new MimeBodyPart();
		MimeBodyPart wrap = new MimeBodyPart();
		cover.addBodyPart(html);
		wrap.setContent(cover);
		MimeMultipart content = new MimeMultipart("related");
		msg.setContent(content);
		content.addBodyPart(wrap);
		setAttachmentsToBody(content);
		if (isHTML) {
	      pText = Utils.acentosHTML(pText);
	      html.setContent(pText, "text/html");
	    }
	    else {
	    	html.setContent(pText, "text/plain");
	    }
	}

	public void postMail(String pTo, String pText, String pSubject, String pFrom, String pFromName, boolean isHTML) throws MessagingException {

		if (!pTo.trim().equals("")) {
			addAddressEmail(pTo, TYPE_RECIPIENT_TO);
		}
		else {
			addAddressEmail(( IniManipulation.getProperty(IniManipulation.MAIL_FROM).equals("") ? IniManipulation.getProperty(IniManipulation.MAIL_USER) : IniManipulation.getProperty(IniManipulation.MAIL_FROM) ), TYPE_RECIPIENT_TO);
		}

		// Log de emails enviados..
		if (!IniManipulation.getProperty(IniManipulation.MAIL_LOG).equals("")) {
			addAddressEmail(IniManipulation.getProperty(IniManipulation.MAIL_LOG), TYPE_RECIPIENT_BCC);
		}

		MimeMessage msg = null;
		int count = 0;
		for (int i = 0; i < mAddressTo.size(); i++) {
			if (count == 0) {
				// create a message
				msg = initMessage(pFrom, pFromName, pTo, pSubject);
				buildMessage(msg, pText, isHTML);
				
			}

			AddressTo lAddress = (AddressTo) mAddressTo.get(i);
			try {
				if (lAddress.getType() == 1) {// To
					msg.addRecipient(MimeMessage.RecipientType.TO, lAddress.getAddress());
				}
				else if (lAddress.getType() == 2) {// CC
					msg.addRecipient(MimeMessage.RecipientType.CC, lAddress.getAddress());
				}
				else if (lAddress.getType() == 3) {// BCC
					msg.addRecipient(MimeMessage.RecipientType.BCC, lAddress.getAddress());
				}
				count++;
			}
			catch (MessagingException ex) {
				Utils.printSystemError("postMail",ex.getMessage());
			}

			if (count == 50 || ( i + 1 ) == mAddressTo.size()) {
				count = 0;
				// Send the message;
				ThreadEnvio lEnvio = new ThreadEnvio(msg);
				lEnvio.start();
			}
		}
	}

	public void sendAttachment(String pTo, String pText, String pSubject) throws MessagingException {
		sendAttachment(pTo, pText, pSubject, "", "", false);
	}

	public void sendAttachment(String pTo, String pText, String pSubject, String pFrom, String pFromName, boolean isHTML) throws AddressException, MessagingException {

		// create a message
		MimeMessage msg = initMessage(pFrom, pFromName, pTo, pSubject);

		buildMessage(msg, pText, isHTML);

		// Send the message;
		ThreadEnvio lEnvio = new ThreadEnvio(msg);
		lEnvio.start();
	}

	public void postMailDefault(String pTo, String pText, String pSubject) throws AddressException, MessagingException {
		postMailDefault(pTo, pText, pSubject, "", "");
	}

	public void postMailDefault(String pTo, String pText, String pSubject, String pFrom, String pFromName) throws AddressException, MessagingException {

		if (!Database.verifyNull(pTo).equals("") && !Database.verifyNull(pText).equals("")) {
//			pText = getBodyMail(pText, "isg/images/logos/system-logo-small.png", null);
			pText = getBasicBodyMail(pText);
			postMail(pTo, pText, pSubject, pFrom, pFromName, true);
		}
	}

	public void postMailPadrao(String pHeader, String pTo, String pText, String pSubject) throws AddressException, MessagingException {
		postMailPadrao(pHeader, pTo, pText, pSubject, "", "");
	}

	public void postMailPadrao(String pHeader, String pTo, String pText, String pSubject, String pFrom, String pFromName) throws AddressException, MessagingException {
		if (!Database.verifyNull(pTo).equals("") && !Database.verifyNull(pText).equals("")) {
			pText = getBasicBodyMail(pText);
			postMail(pTo, pText, pSubject, pFrom, pFromName, true);
		}
	}

	// Método utilizado somente para teste (email.jsp)
	public String[] postMailNoThread(String pTo, String pText, String pSubject, String pFrom, String pPassword, String pFromName, String pSMTP, String pAuthentication, boolean pDefault) throws MessagingException {
		ArrayList<String> strMsgs = new ArrayList<String>();
		String arrMsgs[];

		boolean debug = false;

		// Set the host smtp address
		Properties props = new Properties();
		props.clear();
		props.put("mail.smtp.host", pSMTP);
		props.put("mail.smtp.auth", pAuthentication);

		// create some properties and get the default Session
		Session session = null;
		if (pAuthentication.equalsIgnoreCase("false")) {
			session = Session.getInstance(props, null);
		}
		else {
			session = Session.getInstance(props, new SMTPAuthenticator(pFrom, pPassword));
		}
		session.setDebug(debug);

		strMsgs.add("- Session criada.");

		// create a message
		MimeMessage msg = new MimeMessage(session);

		try {
			// set the from and to address
			InternetAddress addressFrom = null;
			try {
				addressFrom = new InternetAddress(pFrom, pFromName);
			}
			catch (Exception ex) {
				strMsgs.add(ex.getMessage());
				addressFrom = new InternetAddress(pFrom);
			}
			msg.setFrom(addressFrom);
		}
		catch (Exception ex1) {
		}

		addAddressEmail(pTo, TYPE_RECIPIENT_TO);
		setRecipientToMessage(msg);

		strMsgs.add("- Destinatário incluído.");

		try {
			msg.setSubject(pSubject);
		}
		catch (MessagingException ex) {
			strMsgs.add(ex.getMessage());
		}

		if (pDefault) {
			MimeMultipart lBodyEmbedded = new MimeMultipart("related");

			MimeBodyPart lBody = new MimeBodyPart();

			// pText = getBodyMail(CSystemConfig.getSystemName(lcdb.mUser), pText);
			pText = getBasicBodyMail(pText);
//			pText = getBodyMail(pText, "isg/images/logos/system-logo-small.png", null);
			
			strMsgs.add("- Texto embedded incluído.");

			pText = Utils.acentosHTML(pText);

			lBody.setContent(pText, "text/html");

			// Adicionando conteúdo HTML ou texto...
			lBodyEmbedded.addBodyPart(lBody);

			// Adicionando conteúdo HTML ou texto...
			setAttachmentsToBody(lBodyEmbedded);
			strMsgs.add("- Anexos incluídos.");

			MimeBodyPart lEncapsuleur = new MimeBodyPart();
			lEncapsuleur.setContent(lBodyEmbedded);
			Multipart mMultipart = new MimeMultipart();

			mMultipart.addBodyPart(lEncapsuleur);

			msg.setContent(mMultipart);
			strMsgs.add("- Mensagem criada.");

		}
		else {
			msg.setContent(pText, "text/plain");
			strMsgs.add("- Texto incluído.");
		}
		try {
			Transport.send(msg);
			strMsgs.add("E-mail enviado com sucesso!");
		}
		catch (Exception ex) {
			strMsgs.add(ex.getMessage());
		}

		arrMsgs = new String[strMsgs.size()];
		strMsgs.toArray(arrMsgs);

		return arrMsgs;
	}

	public static boolean validateEmail(String pEmail) {
		if (!pEmail.trim().equals("")) {
			Pattern p = Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$");
			Matcher m = p.matcher(pEmail.trim());
			if (m.matches()) {
				return true;
			}
		}
		return false;
	}

	private String getBasicBodyMail(String pText) {
		String lHTML = "";
		lHTML += "<html>\n";
		lHTML += "<head>\n";
		lHTML += "  <meta http-equiv='Content-Type' content='text/html; charset=" + lcdb.mUser.getUserCharset() + "' />\n";
		lHTML += "</head>\n";
		lHTML += "<body>\n";

		Connection conn = lcdb.openConnection();
		StringBuffer body = new StringBuffer();
		try{
			template = new BufferedReader(new FileReader(IniManipulation.getRealPath() + "/isg/mail_template.html"));
			String linha;
			while((linha = template.readLine()) != null){
				body.append(linha+"\n");
			}
			template.close();
		}
		catch(Exception ex){
			Utils.printSystemError("Email:loadingTemplate", ex.getMessage());
		}
		
		pText = body.toString().replaceAll("&amp;", "&").replaceAll("\\{CONTENT\\}", pText);


//		String lCD_TPLEM = "";
//		
//		String lCID_ANXTP = addFile(byte, Database.verifyNull(lrs.getObject("NM_ANXTP")), true);

//		if (pText.indexOf("CD_TPLEM=") > -1) {
//			lCD_TPLEM = pText.split("CD_TPLEM=")[1].split("&")[0];
//
//			String sqlANXTP = "";
//
//			sqlANXTP += "SELECT \n";
//			sqlANXTP += "  CD_ANXTP,\n";
//			sqlANXTP += "  NM_ANXTP \n";
//			sqlANXTP += "FROM ANXTP \n";
//			sqlANXTP += "WHERE CD_TPLEM = " + lCD_TPLEM;
//
//			ResultSet lrs = lcdb.openResultSet(sqlANXTP, conn);
//
//			try {
//				while (lrs.next()) {
//
//					String lCID_ANXTP = "";
//					if (lcdb.getUser() != null) {
//						BlobControl lBlob = Database.getBlobControl(lcdb.getUser());
//
//						byte[] lPicture = null;
//
//						lPicture = lBlob.openFieldFile("AQ_ANXTP", "ANXTP", "CD_ANXTP = " + Database.verifyNull(lrs.getObject("CD_ANXTP")));
//						if (lPicture != null) {
//							lCID_ANXTP = addFile(lPicture, Database.verifyNull(lrs.getObject("NM_ANXTP")), true);
//						}
//					}
//
//					pText = pText.replaceAll("&amp;", "&").replaceAll("cdownload\\?CD_TPLEM=" + lCD_TPLEM + "&CD_ANXTP=" + Database.verifyNull(lrs.getObject("CD_ANXTP")), "cid:" + lCID_ANXTP);
//
//				}
//			}
//			catch (Exception e) {
//				System.out.println("ERROR: CMail: getBasicBodyMail(): " + e.getMessage());
//			}
//		}

		if (pText.indexOf("{LOGO}") > -1) {
			String lCID = "";
			if (lcdb.getUser() != null) {
//				BlobControl lBlob = Database.getBlobControl(lcdb.getUser());

				byte[] lPicture = null;

				try {
					//lPicture = lBlob.openFieldFile("AQ_EMPGC_LOGO", "EMPGC", "CD_EMPGC = 0");
					InputStream inputStream = new File(IniManipulation.getRealPath() + "/isg/images/logos/system-logo-small.png").toURI().toURL().openStream();
					lPicture = new byte[inputStream.available()];
					inputStream.read(lPicture);
				}
				catch (Exception e) {
//					Utils.printSystemError("Blob.OpenFieldFile", e.getMessage());
					Utils.printSystemError("Email:loadingLogo", e.getMessage());
				}

				if (lPicture != null) {
					lCID = addFile(lPicture, "logo_sistema.png", true);
				}
			}

			pText = Utils.replaceAll(pText, "{LOGO}", "<img id=\"logo\" src='cid:" + lCID + "' />");
		}
		
		StringBuffer attach = new StringBuffer();
		for(Attachment lAttach : mAttachments){
			if(lAttach.isEmbedded() && !lAttach.getName().equals("logo_sistema.png")){
				attach.append("<div class=\"img-attach\"><img src=\"cid:"+lAttach.getCid()+"\" alt=\""+lAttach.getName()+"\" /></div><br/>");
			}
		}
		
		if(attach.length() > 0)
			pText = Utils.replaceAll(pText, "{ATTACHMENT}", attach.toString());
		else
			pText = Utils.replaceAll(pText, "{ATTACHMENT}", "");

		pText = Utils.replaceAll(pText, "{SYSTEM}", SystemConfig.getSystemName());

		lHTML += pText;
		lHTML += "</body>\n";
		lHTML += "</html>\n";

		Database.closeObject(conn);

		return lHTML;
	}
	
}
