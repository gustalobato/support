
package br.com.manchestercity.automacao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.manchestercity.SystemConfig;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Utils {
	Database lcdb;
	User mUser;

	public static int countID = 1;

	private static final String METODO_ENCRIPTACAO = "AES";

	// Constantes das AÁıes
	public static final int ACTION_NEW = 0;
	public static final int ACTION_RELOAD = 1;
	public static final int ACTION_SAVE = 2;
	public static final int ACTION_DELETE = 3;
	public static final int ACTION_LOGIN = 4;
	public static final int ACTION_SENDMAIL = 5;

	// Constantes do tipo de Editor
	public static final int TYPE_EDITOR_BASIC = 0;
	public static final int TYPE_EDITOR_FULL = 1;
	public static final int TYPE_EDITOR_FULLIMG = 2;

	public static final byte[] CHAVE = { 85, 10, 0, -25, 68, 88, 46, 37, 107, 48, 10, -1, -37, -90, 70, -36 };

	public Utils(User pUser) {
		mUser = pUser;
		lcdb = new Database(pUser);
	}

	public static int getCountID() {
		countID++;
		if (countID >= 100) {
			countID = 1;
		}
		return countID;
	}

	public static String charsURL(String pEntrada, boolean retiraEspeciais) {
		int i = 0;
		ArrayList<String[]> a = new ArrayList<String[]>();

		if (retiraEspeciais) {
			a.add(new String[] { "%", "%25" });
			a.add(new String[] { "\\.", "%2E" });
			a.add(new String[] { "\"", "%22" });
			a.add(new String[] { "#", "%23" });
			a.add(new String[] { "&", "%26" });
			a.add(new String[] { "\\(", "%28" });
			a.add(new String[] { "\\)", "%29" });
			a.add(new String[] { "\\+", "%2B" });
			a.add(new String[] { ",", "%2C" });
			a.add(new String[] { "/", "%2F" });
			a.add(new String[] { ":", "%3A" });
			a.add(new String[] { ";", "%3B" });
			a.add(new String[] { "<", "%3C" });
			a.add(new String[] { "=", "%3D" });
			a.add(new String[] { ">", "%3E" });
			a.add(new String[] { "\\?", "%3F" });
			a.add(new String[] { "@", "%40" });
			a.add(new String[] { "\\[", "%5B" });
			a.add(new String[] { "\\]", "%5D" });
			a.add(new String[] { "\\^", "%5E" });
			a.add(new String[] { "'", "%60" });
			a.add(new String[] { "\\{", "%7B" });
			a.add(new String[] { "\\|", "%7C" });
			a.add(new String[] { "\\}", "%7D" });
			a.add(new String[] { "\\~", "%7E" });
		}
		else {
			a.add(new String[] { "%25", "%" });
			a.add(new String[] { "%2E", "\\." });
			a.add(new String[] { "%22", "\"" });
			a.add(new String[] { "%23", "#" });
			a.add(new String[] { "%26", "&" });
			a.add(new String[] { "%28", "\\(" });
			a.add(new String[] { "%29", "\\)" });
			a.add(new String[] { "%2B", "\\+" });
			a.add(new String[] { "%2C", "," });
			a.add(new String[] { "%2F", "/" });
			a.add(new String[] { "%3A", ":" });
			a.add(new String[] { "%3B", ";" });
			a.add(new String[] { "%3C", "<" });
			a.add(new String[] { "%3D", "=" });
			a.add(new String[] { "%3E", ">" });
			a.add(new String[] { "%3F", "?" });
			a.add(new String[] { "%40", "@" });
			a.add(new String[] { "%5B", "\\[" });
			a.add(new String[] { "%5D", "\\]" });
			a.add(new String[] { "%5E", "\\^" });
			a.add(new String[] { "%60", "'" });
			a.add(new String[] { "%7B", "\\{" });
			a.add(new String[] { "%7C", "\\|" });
			a.add(new String[] { "%7D", "\\}" });
			a.add(new String[] { "%7E", "\\~" });
		}

		try {
			for (i = 0; i < a.size(); i++) {
				String[] b = (String[]) a.get(i);
				try {
					pEntrada = pEntrada.replaceAll(b[0], b[1]);
				}
				catch (Exception ex) {
					System.err.println("ERROR: CUtil: caracteresPermitidosUrl(): indice: [" + i + "]:");
				}
			}
		}
		catch (Exception e) {
			System.err.println("ERROR: CUtil: caracteresPermitidosUrl(): indice: [" + i + "]:");
			System.err.println(e.getMessage());
		}

		return pEntrada;
	}

	public static String replaceAll(String pText, String pFind, String pReplace) {
		int lInt = 0;
		lInt = pText.indexOf(pFind);
		while (lInt >= 0) {
			pText = pText.substring(0, lInt) + pReplace + pText.substring(lInt + pFind.length());
			lInt = pText.indexOf(pFind, lInt + pReplace.length());
		}
		return pText;
	}

	public static String replaceEntre(String pText, String pFindIni, String pFindFim, String pReplace) {
		Pattern p = Pattern.compile("(" + pFindIni + ")");
		Matcher m = p.matcher(pText);
		int start = 0;
		while (m.find()) {
			start = m.start();
			m = Pattern.compile("(" + pFindFim + ")").matcher(pText);

			if (m.find()) {
				pText = Utils.replaceAll(pText, pText.substring(start, m.end()), pReplace);
			}

			p = Pattern.compile("(" + pFindIni + ")");
			m = p.matcher(pText);
		}
		return pText;
	}

	public static String addCrypt(String value) {
		String lRetorno = "";

		try {
			SecretKeySpec skeySpec = new SecretKeySpec(CHAVE, METODO_ENCRIPTACAO);

			Cipher cipher = Cipher.getInstance(METODO_ENCRIPTACAO);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(value.getBytes());

			lRetorno = new BASE64Encoder().encode(encrypted);
		}
		catch (Exception e) {
			System.err.println("ERROR: CUtil: Erro ao criptografar informaÁıes " + e.getMessage());
			lRetorno = "ERROR";
		}

		lRetorno = Utils.charsURL(lRetorno, true);
		return lRetorno;
	}

	public static String removeCrypt(String encrypted) {
		byte[] decrypted = null;

		try {
			// A URL ja entende os caracteres encriptados pela funÁ„o (CUtil.caracteresPermitidosUrl(palavra, true)).
			// encrypted = CUtil.caracteresPermitidosUrl(encrypted, false);
			SecretKeySpec skeySpec = new SecretKeySpec(CHAVE, METODO_ENCRIPTACAO);

			byte[] decoded = new BASE64Decoder().decodeBuffer(encrypted);

			Cipher cipher = Cipher.getInstance(METODO_ENCRIPTACAO);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			decrypted = cipher.doFinal(decoded);
		}
		catch (Exception e) {
			System.err.println("ERROR: CUtil: Erro ao descriptografar informaÁıes " + e.getMessage());
		}

		return new String(decrypted);
	}

	public static void montaString(StringBuffer pResultado, String pValor, String pSeaprador) {
		if (!pValor.equals("")) {
			pResultado.append(pSeaprador + pValor);
		}
	}

	public String getUrlApp() {
		String lURL = mUser.request.getRequestURL().toString();
		return lURL.substring(0, lURL.indexOf(mUser.request.getContextPath()) + mUser.request.getContextPath().length()) + "/";
	}

	public String calendarToString(Calendar pDate) {
		return calendarToString(pDate, false);
	}

	public String calendarToString(Calendar pDate, boolean withHour) {
		String lRet = "";
		SimpleDateFormat ft = null;
		try {
			if (withHour) {
				ft = new SimpleDateFormat(mUser.getUserDateFormat() + " HH:mm:ss");
			}
			else {
				ft = new SimpleDateFormat(mUser.getUserDateFormat());
			}
			lRet = ft.format(pDate.getTime());
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			lRet = "";
		}

		return lRet;
	}

	public Calendar createCalendarDateFormat(String pDate) {
		Calendar ca = null;
		try {
			if (!pDate.equals("")) {
				ca = Calendar.getInstance();
				if (pDate.split(" ").length == 1) {
					pDate += " 12:00:00";
				}
				else if (pDate.split(" ")[1].split(":").length < 3) {
					String aux = pDate.split(" ")[1].trim();
					int i = aux.split(":").length;
					i = ( i == 1 ? 0 : i );
					for (; i < 3; i++)
						aux += ( aux.length() > 0 ? ":" : "" ) + "00";
					pDate = pDate.split(" ")[0] + " " + aux;
				}
				pDate = Utils.formatDate(pDate, mUser.getUserDateFormat() + " HH:mm:ss", "dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				ca.setTime(df.parse(pDate));
			}
		}
		catch (Exception ex) {
			System.err.println("ERRO : transformar de string para calendar : " + ex.getMessage());
		}

		return ca;
	}

	public static Calendar createCalendar(String pDate) {
		Calendar ca = null;
		try {
			if (!pDate.equals("")) {
				ca = Calendar.getInstance();
				if (pDate.split(" ").length == 1) {
					pDate += " 12:00:00";
				}
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				ca.setTime(df.parse(pDate));
			}
		}
		catch (Exception ex) {
			System.err.println("ERRO : transformar de string para calendar (" + Utils.callerClass() + " - " + Utils.callerMethod() + "): " + ex.getMessage());
		}

		return ca;
	}

	public String arrayToString(String[] pArray, String pSeparator) {
		String lReturn = "";
		try {
			int i = 0;
			for (i = 0; i < pArray.length; i++) {
				if (!lReturn.equals("")) {
					lReturn += pSeparator;
				}
				lReturn += pArray[i];
			}
		}
		catch (Exception ex) {
			lReturn = "";
		}
		return lReturn;
	}

	public static void noCacheStatic(HttpServletResponse response, HttpServletRequest request) {
		String lCharsetRequest = request.getCharacterEncoding();
		try {
			request.setCharacterEncoding(lCharsetRequest);
			request.getHeader("Accept-Charset");
			response.setDateHeader("Expires", 0);
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Accept-Charset", lCharsetRequest);
			request.getHeader("Accept-Charset");
			if (request.getProtocol().equals("HTTP/1.1")) {
				response.setHeader("Cache-Control", "no-cache");
			}
		}
		catch (Exception ex) {
		}
	}

	public void noCache(HttpServletResponse response, HttpServletRequest request) {
		Utils.noCacheStatic(response, request);
	}

	public static String mesExtenso(String pMes, User pUser) {
		switch (Integer.parseInt(pMes)) {
			case 1:
				return pUser.getTermo("JANEIRO");
			case 2:
				return pUser.getTermo("FEVEREIRO");
			case 3:
				return pUser.getTermo("MARCO");
			case 4:
				return pUser.getTermo("ABRIL");
			case 5:
				return pUser.getTermo("MAIO");
			case 6:
				return pUser.getTermo("JUNHO");
			case 7:
				return pUser.getTermo("JULHO");
			case 8:
				return pUser.getTermo("AGOSTO");
			case 9:
				return pUser.getTermo("SETEMBRO");
			case 10:
				return pUser.getTermo("OUTUBRO");
			case 11:
				return pUser.getTermo("NOVEMBRO");
			case 12:
				return pUser.getTermo("DEZEMBRO");
		}
		return "";
	}

	public static String mesExtenso(String pMes, User pUser, boolean pResumido) {
		String lMes = mesExtenso(pMes, pUser);
		if (pResumido) {
			if (!lMes.equals(""))
				return lMes.substring(0, 3);
			else
				return "";
		}
		else
			return lMes;
	}

	public String diaSemana(String pDia, boolean pEmIngles) {
		switch (Integer.parseInt(pDia)) {
			case 1:
				return pEmIngles ? "Sunday" : mUser.getTermo("DOMINGO");
			case 2:
				return pEmIngles ? "Monday" : mUser.getTermo("SEGUNDAFEIRA");
			case 3:
				return pEmIngles ? "Tuesday" : mUser.getTermo("TERCAFEIRA");
			case 4:
				return pEmIngles ? "Wednesday" : mUser.getTermo("QUARTAFEIRA");
			case 5:
				return pEmIngles ? "Thusday" : mUser.getTermo("QUINTAFEIRA");
			case 6:
				return pEmIngles ? "Friday" : mUser.getTermo("SEXTAFEIRA");
			case 7:
				return pEmIngles ? "Saturday" : mUser.getTermo("SABADO");
		}
		return "";
	}

	public void saveFile(String pFileName, String pFileContent) {
		File outputFile = new File(pFileName);

		try {
			FileWriter lOut = new FileWriter(outputFile);

			lOut.write(pFileContent);
			lOut.close();
		}
		catch (IOException ex) {
		}
	}

	public String writeMsg(String pMsg) {
		return "<script>bootbox.alert('" + pMsg + "')</script>";
	}

	public String makeBodyEmail(String pText, String pRodape) {
		String lHtml = "";

		lHtml = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"";
		lHtml += "\"http://www.w3.org/TR/html4/loose.dtd\">";
		lHtml += "<html>";
		lHtml += "<head>";
		lHtml += "<title>GRP - Gest&atilde;o de Rela&ccedil;&otilde;es P&uacute;blicas</title>";
		lHtml += "<meta http-equiv='Content-Type' content='text/html; charset=" + mUser.getUserCharset() + "'>";
		lHtml += "<style type='text/css'>";
		lHtml += "<!--";
		lHtml += ".titulo {font-family: Trebuchet MS, Verdana, Arial, Helvetica, sans-serif; color: #000000; font-size: 12pt; height: auto; letter-spacing: -1px; font-weight: bold; }";
		lHtml += ".rodape {";
		lHtml += "	font-family: Verdana;";
		lHtml += "	font-size: 10px;";
		lHtml += "}";
		lHtml += ".texto {";
		lHtml += "	font-family: Verdana;";
		lHtml += "	font-size: 12px;";
		lHtml += "}";
		lHtml += "-->";
		lHtml += "</style>";
		lHtml += "</head>";
		lHtml += "";
		lHtml += "<body>";
		lHtml += "<table width='100%'  border='0' cellspacing='0' cellpadding='0'>";
		lHtml += "  <tr>";
		lHtml += "    <td height='30' valign='middle'>&nbsp;&nbsp;<span class='titulo'>GRP - Gest&atilde;o de Rela&ccedil;&otilde;es P&uacute;blicas </span></td>";
		lHtml += "  </tr>";
		lHtml += "  <tr>";
		lHtml += "    <td height='2' bgcolor='#999999'></td>";
		lHtml += "  </tr>";
		lHtml += "  <tr>";
		lHtml += "    <td height='1' bgcolor='#FFFFFF'></td>";
		lHtml += "  </tr>";
		lHtml += "  <tr>";
		lHtml += "    <td height='1' bgcolor='#999999'></td>";
		lHtml += "  </tr>";
		lHtml += "  <tr>";
		lHtml += "    <td><table width='98%'  border='0' align='center' cellpadding='0' cellspacing='0'>";
		lHtml += "      <tr>";
		lHtml += "        <td>&nbsp;</td>";
		lHtml += "      </tr>";
		lHtml += "      <tr>";
		lHtml += "        <td class='texto'>" + acentosHTML(pText) + "</td>";
		lHtml += "      </tr>";
		lHtml += "      <tr>";
		lHtml += "        <td height='40'>&nbsp;</td>";
		lHtml += "      </tr>";
		lHtml += "    </table></td>";
		lHtml += "  </tr>";
		if (pRodape.equals("S")) {
			lHtml += "  <tr>";
			lHtml += "    <td bgcolor='#EFEFEF'><table  border='0' cellspacing='0' cellpadding='0'>";
			lHtml += "      <tr>";
			lHtml += "        <td width='2' bgcolor='#999999'>&nbsp;</td>";
			lHtml += "        <td height='20' valign='middle'>&nbsp;&nbsp;<span class='rodape'>" + mUser.getTermo("NAORESPEMAIL") + "</span></td>";
			lHtml += "      </tr>";
			lHtml += "    </table></td>";
			lHtml += "  </tr>";
		}
		lHtml += "</table>";
		return lHtml;
	}

	public String makeBodyEmail(String pText) {
		return makeBodyEmail(pText, "S");
	}

	public static final String acentosHTML(String pString) {
		return acentosHTML(pString, true);
	}

	public static final String acentosHTML(String pString, boolean pTrocarSomenteAcentos) {
		StringBuffer sb = new StringBuffer();
		int n = pString.length();
		for (int i = 0; i < n; i++) {
			char c = pString.charAt(i);
			switch (c) {
				case '<':
					if (pTrocarSomenteAcentos) {
						sb.append(c);
					}
					else {
						sb.append("&lt;");
					}
					break;
				case '>':
					if (pTrocarSomenteAcentos) {
						sb.append(c);
					}
					else {
						sb.append("&gt;");
					}
					break;
				case '&':
					if (pTrocarSomenteAcentos) {
						sb.append(c);
					}
					else {
						sb.append("&amp;");
					}
					break;
				case '"':
					if (pTrocarSomenteAcentos) {
						sb.append(c);
					}
					else {
						sb.append("&quot;");
					}
					break;
				case ' ':
					if (pTrocarSomenteAcentos) {
						sb.append(c);
					}
					else {
						sb.append("&nbsp;");
					}
					break;
				case '·':
					sb.append("&aacute;");
					break;
				case '¡':
					sb.append("&Aacute;");
					break;
				case '„':
					sb.append("&atilde;");
					break;
				case '√':
					sb.append("&Atilde;");
					break;
				case '‡':
					sb.append("&agrave;");
					break;
				case '¿':
					sb.append("&Agrave;");
					break;
				case '‚':
					sb.append("&acirc;");
					break;
				case '¬':
					sb.append("&Acirc;");
					break;
				case '‰':
					sb.append("&auml;");
					break;
				case 'ƒ':
					sb.append("&Auml;");
					break;
				case 'Â':
					sb.append("&aring;");
					break;
				case '≈':
					sb.append("&Aring;");
					break;
				case 'Ê':
					sb.append("&aelig;");
					break;
				case '∆':
					sb.append("&AElig;");
					break;
				case 'Á':
					sb.append("&ccedil;");
					break;
				case '«':
					sb.append("&Ccedil;");
					break;
				case 'È':
					sb.append("&eacute;");
					break;
				case '…':
					sb.append("&Eacute;");
					break;
				case 'Ë':
					sb.append("&egrave;");
					break;
				case '»':
					sb.append("&Egrave;");
					break;
				case 'Í':
					sb.append("&ecirc;");
					break;
				case ' ':
					sb.append("&Ecirc;");
					break;
				case 'Î':
					sb.append("&euml;");
					break;
				case 'À':
					sb.append("&Euml;");
					break;
				case 'Ô':
					sb.append("&iuml;");
					break;
				case 'œ':
					sb.append("&Iuml;");
					break;
				case 'Ì':
					sb.append("&iacute;");
					break;
				case 'Õ':
					sb.append("&Iacute;");
					break;
				case 'Ù':
					sb.append("&ocirc;");
					break;
				case '‘':
					sb.append("&Ocirc;");
					break;
				case 'ı':
					sb.append("&otilde;");
					break;
				case '’':
					sb.append("&Otilde;");
					break;
				case 'Û':
					sb.append("&oacute;");
					break;
				case '”':
					sb.append("&Oacute;");
					break;
				case 'ˆ':
					sb.append("&ouml;");
					break;
				case '÷':
					sb.append("&Ouml;");
					break;
				case '¯':
					sb.append("&oslash;");
					break;
				case 'ÿ':
					sb.append("&Oslash;");
					break;
				case 'ﬂ':
					sb.append("&szlig;");
					break;
				case '˘':
					sb.append("&ugrave;");
					break;
				case 'Ÿ':
					sb.append("&Ugrave;");
					break;
				case '˙':
					sb.append("&uacute;");
					break;
				case '⁄':
					sb.append("&Uacute;");
					break;
				case '˚':
					sb.append("&ucirc;");
					break;
				case '€':
					sb.append("&Ucirc;");
					break;
				case '¸':
					sb.append("&uuml;");
					break;
				case '‹':
					sb.append("&Uuml;");
					break;
				case 'Æ':
					sb.append("&reg;");
					break;
				case '©':
					sb.append("&copy;");
					break;
				case 'Ä':
					sb.append("&euro;");
					break;
				default:
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}

	public static final String specialCharsHTML(String pString) {
		StringBuffer sb = new StringBuffer();

		// remover quebra de linha
		pString = pString.replaceAll("\n", " ");
		pString = pString.replaceAll("\r", " ");
		pString = pString.replaceAll("\\\"", "\\\\\\'");

		int n = pString.length();
		for (int i = 0; i < n; i++) {
			char c = pString.charAt(i);
			switch (c) {
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '&':
					sb.append("&amp;");
					break;
				default:
					sb.append(c);
					break;
			}
		}

		return sb.toString();
	}

	private String x3(char cNumber) {
		int number = Integer.parseInt(String.valueOf(cNumber));
		number = number * 3;
		return String.valueOf(number);
	}

	public static boolean verifyDateBetween(Calendar pDate, Calendar pDateIni, Calendar pDateFim) {
		if (pDateIni.getTimeInMillis() <= pDate.getTimeInMillis() && pDateFim.getTimeInMillis() >= pDate.getTimeInMillis()) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getDatePT(String lStr) {
		String lReturn = "";
		try {
			// BUSCANDO SEPARADORES
			String lDateFormat = mUser.getUserDateFormat();
			String Sep1 = "";
			String Sep2 = "";
			int ano = 0;
			int mes = 0;
			int dia = 0;
			int lPos = lDateFormat.toLowerCase().indexOf("yyyy");
			if (lPos == 0) {
				Sep1 = lDateFormat.toLowerCase().substring(4, 5);
				Sep2 = lDateFormat.toLowerCase().substring(7, 8);
				ano = 0;
				mes = ( lDateFormat.toLowerCase().indexOf("mm") > lDateFormat.toLowerCase().indexOf("dd") ) ? 2 : 1;
				dia = ( lDateFormat.toLowerCase().indexOf("dd") > lDateFormat.toLowerCase().indexOf("mm") ) ? 2 : 1;
			}
			if (lPos == 3) {
				Sep1 = lDateFormat.toLowerCase().substring(2, 3);
				Sep2 = lDateFormat.toLowerCase().substring(7, 8);
				ano = 1;
				mes = ( lDateFormat.toLowerCase().indexOf("mm") > lDateFormat.toLowerCase().indexOf("dd") ) ? 2 : 0;
				dia = ( lDateFormat.toLowerCase().indexOf("dd") > lDateFormat.toLowerCase().indexOf("mm") ) ? 2 : 0;
			}
			if (lPos == 6) {
				Sep1 = lDateFormat.toLowerCase().substring(2, 3);
				Sep2 = lDateFormat.toLowerCase().substring(5, 6);
				ano = 2;
				mes = ( lDateFormat.toLowerCase().indexOf("mm") > lDateFormat.toLowerCase().indexOf("dd") ) ? 1 : 0;
				dia = ( lDateFormat.toLowerCase().indexOf("dd") > lDateFormat.toLowerCase().indexOf("mm") ) ? 1 : 0;
			}

			String[] dataAux = { "", "", "" };
			if (Sep1.equals(Sep2)) {
				dataAux[0] = lStr.split(Sep1)[0];
				dataAux[1] = lStr.split(Sep1)[1];
				dataAux[2] = lStr.split(Sep1)[2];
			}
			else {
				dataAux[0] = lStr.split(Sep1)[0];
				dataAux[1] = lStr.split(Sep1)[1];
				dataAux[1] = dataAux[1].split(Sep2)[0];
				dataAux[2] = lStr.split(Sep2)[1];
			}
			lReturn = dataAux[dia] + "/" + dataAux[mes] + "/" + dataAux[ano];
		}
		catch (Exception ex) {
		}
		return lReturn;
	}

	public String generateValidationCode(String cnpj, String date) {
		String letters = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
		String code = "";
		String lDataPt = getDatePT(date);

		code += String.valueOf(letters.charAt(Integer.parseInt(String.valueOf(lDataPt.split("/")[2].charAt(3)))));
		code += String.valueOf(letters.charAt(Integer.parseInt(this.x3(cnpj.charAt(3)))));
		code += String.valueOf(this.x3(cnpj.charAt(3)));
		code += String.valueOf(letters.charAt(Integer.parseInt(this.x3(cnpj.charAt(0)))));
		code += String.valueOf(letters.charAt(Integer.parseInt(this.x3(cnpj.charAt(1)))));
		code += String.valueOf(this.x3(cnpj.charAt(4)).charAt(0));
		code += String.valueOf(Integer.parseInt(String.valueOf(this.x3(cnpj.charAt(4)))));
		code += String.valueOf(Integer.parseInt(String.valueOf(cnpj.charAt(7))));
		code += String.valueOf(letters.charAt(Integer.parseInt(this.x3(cnpj.charAt(8)))));
		code += String.valueOf(Integer.parseInt(String.valueOf(this.x3(cnpj.charAt(9)))));
		code += String.valueOf(this.x3(cnpj.charAt(5)));
		code += String.valueOf(letters.charAt(Integer.parseInt(String.valueOf(this.x3(cnpj.charAt(5))))));
		code += String.valueOf(Integer.parseInt(this.x3(cnpj.charAt(9))));
		code += String.valueOf(letters.charAt(Integer.parseInt(lDataPt.split("/")[0])));
		code += String.valueOf(Integer.parseInt(lDataPt.split("/")[1]));
		code += String.valueOf(letters.charAt(Integer.parseInt(this.x3(cnpj.charAt(17)))));

		int qtd = code.length() - 16;
		code = code.substring(0, 5) + code.substring(5 + qtd, code.length());

		return code;
	}

	public static String removeAccent(String value) {
		return removeAccent(value, false);
	}

	public static String removeAccent(String value, boolean removeSpaces) {
		value = value.replaceAll("\"", "");
		value = value.replaceAll("'", "");
		String Acentos = "·‡„‚‚‰¡¿√¬ÎÈËÍ… ÔÌÏÕˆÛıÙ”‘’˙¸⁄‹Á«Òabcdefghijklmnopqrstuvxwyz" + ( removeSpaces ? " " : "" );
		String Traducao = "aaaaaaAAAAeeeeEEiiiIooooOOOuuUUcCnabcdefghijklmnopqrstuvxwyz" + ( removeSpaces ? "_" : "" );

		int posic;
		String carac;

		String tempLog = "";

		for (int i = 0; i < value.length(); i++) {
			carac = String.valueOf(value.charAt(i));
			posic = Acentos.indexOf(carac);
			if (posic > -1)
				tempLog += Traducao.charAt(posic);
			else
				tempLog += String.valueOf(value.charAt(i));
		}

		return tempLog;
	}

	public static String removeMaiuscula(String pValue) {
		String lRetrun = "";
		String lAcentos = "ABCDEFGHIJKLMNOPQRSTUVXWYZ";
		String lTraducao = "abcdefghijklmnopqrstuvxwyz";
		int lPosicao = 0;
		String lCaracter = "";
		for (int i = 0; i < pValue.length(); i++) {
			lCaracter = String.valueOf(pValue.charAt(i));
			lPosicao = lAcentos.indexOf(lCaracter);
			if (lPosicao > -1) {
				lRetrun += lTraducao.charAt(lPosicao);
			}
			else {
				lRetrun += String.valueOf(pValue.charAt(i));
			}
		}
		return lRetrun;
	}

	public static String removeCaracteres(String value) {
		final String caracteresPermitidos = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String tempLog = "";

		for (int i = 0; i < value.length(); i++) {
			String carac = String.valueOf(value.charAt(i));
			if (caracteresPermitidos.indexOf(carac) > 0) {
				tempLog += String.valueOf(value.charAt(i));
			}
		}
		return tempLog;
	}

	public static String removeSpecialChar(String pValue) {
		String lRetrun = "";
		String lAcentos = "·‡„‚‰¡¿√¬ƒÈËÍÎ…» ÀÌÏÓÔÕÃŒœÛÚıÙˆ”“’‘÷˙˘˚¸⁄Ÿ€‹Á«Ò—";
		String lTraducao = "aaaaaAAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuUUUUcCnN";
		int lPosicao = 0;
		String lCaracter = "";
		for (int i = 0; i < pValue.length(); i++) {
			lCaracter = String.valueOf(pValue.charAt(i));
			lPosicao = lAcentos.indexOf(lCaracter);
			if (lPosicao > -1) {
				lRetrun += lTraducao.charAt(lPosicao);
			}
			else {
				lRetrun += String.valueOf(pValue.charAt(i));
			}
		}
		return removeCaracteres(lRetrun);
	}

	public String addDate(String pData, int pDias) {
		pData = getDatePT(pData);
		String lArrData[] = pData.split("/");
		int lDia = Integer.parseInt(lArrData[0]);
		int lMes = Integer.parseInt(lArrData[1]);
		int lAno = Integer.parseInt(lArrData[2]);
		Calendar lData = Calendar.getInstance();
		lData.set(lAno, lMes - 1, lDia);
		lData.add(Calendar.DATE, pDias);

		SimpleDateFormat formatador;
		String lFormat = mUser.getUserDateFormat();

		formatador = new SimpleDateFormat(lFormat);
		return ( formatador.format(lData.getTime()).toString() );
	}

	public String addDate(String pData, int pDatePartCalendar, int pQuantidade) {
		pData = getDatePT(pData);
		String lArrData[] = pData.split("/");
		int lDia = Integer.parseInt(lArrData[0]);
		int lMes = Integer.parseInt(lArrData[1]);
		int lAno = Integer.parseInt(lArrData[2]);
		Calendar lData = Calendar.getInstance();
		lData.set(lAno, lMes - 1, lDia);
		lData.add(pDatePartCalendar, pQuantidade);

		SimpleDateFormat formatador;
		String lFormat = mUser.getUserDateFormat();

		formatador = new SimpleDateFormat(lFormat);
		return ( formatador.format(lData.getTime()).toString() );
	}

	public static int diasPorMes(int pMes, int pAno) {
		int[] lDias = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (( ( pAno % 4 ) == 0 && ( pAno % 100 ) != 0 ) || ( pAno % 400 ) == 0) {
			lDias[2] = 29;
		}

		return lDias[pMes];
	}

	public String formatCNPJ(String value) {
		if (value.equals("")) {
			return "";
		}
		String out = removeCaracteres(value);
		return out.substring(0, 2) + "." + out.substring(2, 5) + "." + out.substring(5, 8) + "/" + out.substring(8, 12) + "-" + out.substring(12, out.length());
	}

	public String formatCPF(String value) {
		if (value.equals("")) {
			return "";
		}
		String out = removeCaracteres(value);
		return out.substring(0, 3) + "." + out.substring(3, 6) + "." + out.substring(6, 9) + "-" + out.substring(9, out.length());
	}

	public String getAfabeticalSequence(int number) {
		String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZAAABACADAEAFAGAHAIAJAKALAMANAOAPAQARASATAUAVAWAXAYAZBABBBCBDBEBFBGBHBIBJBKBLBMBNBOBPBQBRBSBTBUBVBWBXBYBZCACBCCCDCECFCGCHCICJCKCLCMCNCOCPCQCRCSCTCUCVCWCXCYCZDADBDCDDDEDFDGDHDIDJDKDLDMDNDODPDQDRDSDTDUDVDWDXDYDZEAEBECEDEEEFEGEHEIEJEKELEMENEOEPEQERESETEUEVEWEXEYEZFAFBFCFDFEFFFGFHFIFJFKFLFMFNFOFPFQFRFSFTFUFVFWFXFYFZ";
		return alfabeto.substring(number, number + ( number < 26 ? 1 : 2 ));
	}

	public String getFiltroValor(String pField, String pFiltro) {
		int lPos = pFiltro.indexOf(pField + "=");
		if (lPos >= 0) {
			return pFiltro.substring(lPos + pField.length() + 1, ( ( pFiltro.indexOf(" £ ", lPos) == -1 ) ? pFiltro.length() : pFiltro.indexOf(" £ ", lPos) )).trim();
		}
		else
			return "";
	}

	public static String formatDate(String pDate, String pFormatOld, String lFormatNew) {
		java.util.Date lDate = new java.util.Date(System.currentTimeMillis());
		SimpleDateFormat formatador;
		String lReturn = "";
		try {
			formatador = new SimpleDateFormat(pFormatOld);
			lDate = formatador.parse(pDate);
			formatador = new SimpleDateFormat(lFormatNew);
			lReturn = formatador.format(lDate).toString();
		}
		catch (Exception ex) {
		}
		return lReturn;
	}

	public static int compareCalendar(Calendar date1, Calendar date2) {
		// 1 --> date1 > date2
		// 0 --> date1 = date2
		// -1 --> date1 < date2

		int lReturn = 0;

		if (date1.getTimeInMillis() > date2.getTimeInMillis()) {
			lReturn = 1;
		}
		else if (date1.getTimeInMillis() == date2.getTimeInMillis()) {
			lReturn = 0;
		}
		else if (date1.getTimeInMillis() < date2.getTimeInMillis()) {
			lReturn = -1;
		}

		return lReturn;
	}

	/* Metodo utilizado no Web Dinamic Report */
	public static String superTrim(String pText, String pValue) {
		/* pText -> String auterada */
		/* pValue -> Valor removido */

		String[] lTextVet = pText.split(pValue);
		pText = "";

		for (int i = 0; i < lTextVet.length; i++) {
			if (!lTextVet[i].trim().equals("")) {
				if (!pText.equals("")) {
					pText += pValue;
				}
				pText += lTextVet[i];
			}
		}

		return pText;
	}

	public static String cropText(String pText, int pLength) {
		String lRet = pText;
		if (pText.length() > pLength) {
			lRet = pText.substring(0, pLength - 1);
		}
		return lRet;
	}

	private boolean validateCPF(String pCPF) throws Exception {
		pCPF = pCPF.replaceAll("\\.", "").replaceAll("-", "");

		if (pCPF.length() == 11) {

			if (pCPF.replaceAll(String.valueOf(pCPF.charAt(0)), "").equals("")) {
				return false;
			}

			int lResto = 0;
			int lDigito_1 = 0;
			int lDigito_2 = 0;
			int lDigitoAux_1 = 0;
			int lDigitoAux_2 = 0;

			for (int i = 0; i < 9; i++) {
				int lNumero = Integer.parseInt(String.valueOf(pCPF.charAt(i)));
				lDigitoAux_1 += ( 10 - i ) * lNumero;
				lDigitoAux_2 += ( 11 - i ) * lNumero;
			}

			lResto = lDigitoAux_1 % 11;
			if (lResto < 2) {
				lDigito_1 = 0;
			}
			else {
				lDigito_1 = 11 - lResto;
			}

			lDigitoAux_2 += 2 * lDigito_1;

			lResto = lDigitoAux_2 % 11;
			if (lResto < 2) {
				lDigito_2 = 0;
			}
			else {
				lDigito_2 = 11 - lResto;
			}

			return pCPF.substring(9).equals(String.valueOf(lDigito_1) + String.valueOf(lDigito_2));
		}

		return false;
	}

	private boolean validateCNPJ(String pCNPJ) throws Exception {
		pCNPJ = pCNPJ.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");

		if (pCNPJ.length() == 14 || pCNPJ.length() == 15) {
			boolean lDigitosIguais = true;
			int lTamanho = 0;
			int lSoma = 0;
			int lPos = 0;
			int lResultado = 0;
			String lNumeros = "";
			String lDigitos = "";

			if (lDigitosIguais) {
				lTamanho = pCNPJ.length() - 2;
				lNumeros = pCNPJ.substring(0, lTamanho);
				lDigitos = pCNPJ.substring(lTamanho);
				lPos = lTamanho - 7;

				for (int i = lTamanho; i >= 1; i--) {
					lSoma += Integer.parseInt(String.valueOf(lNumeros.charAt(lTamanho - i))) * lPos--;
					if (lPos < 2) {
						lPos = 9;
					}
				}

				if (lSoma % 11 > 1) {
					lResultado = 11 - lSoma % 11;
				}

				if (lResultado != Integer.parseInt(String.valueOf(lDigitos.charAt(0)))) {
					return false;
				}

				lTamanho = lTamanho + 1;
				lNumeros = pCNPJ.substring(0, lTamanho);
				lSoma = 0;
				lPos = lTamanho - 7;

				for (int i = lTamanho; i >= 1; i--) {
					lSoma += Integer.parseInt(String.valueOf(lNumeros.charAt(lTamanho - i))) * lPos--;
					if (lPos < 2) {
						lPos = 9;
					}
				}

				if (lSoma % 11 < 2) {
					lResultado = 0;
				}
				else {
					lResultado = 11 - lSoma % 11;
				}

				if (lResultado != Integer.parseInt(String.valueOf(lDigitos.charAt(1)))) {
					return false;
				}

				return true;
			}
		}

		return false;
	}

	public boolean validateCPFCNPJ(String pCPFCNPJ) {
		if (Database.verifyNull(pCPFCNPJ).equals("")) {
			return false;
		}
		else if (pCPFCNPJ.length() > 14) {
			try {
				return validateCNPJ(pCPFCNPJ);
			}
			catch (Exception ex) {
				return false;
			}
		}
		else {
			try {
				return validateCPF(pCPFCNPJ);
			}
			catch (Exception ex) {
				return false;
			}
		}
	}

	public String generateIndex(String letra, boolean todos) {
		StringBuffer lHTML = new StringBuffer("");

		lHTML.append("<script>");
		lHTML.append(" function enviarformulario() {");
		lHTML.append("    event.returnValue = null;");
		lHTML.append("    event.keyCode = 0;");
		lHTML.append("    try{indiceAction()} catch(e){} document._frm.submit();");
		lHTML.append(" }");
		lHTML.append("</script>");

		lHTML.append("<table border=0 cellspacing=3 cellpadding=0 align=center width=100%>");
		lHTML.append("<tr>");

		lHTML.append("<td width=100% align=center><input type=text onfocus=\"this.select()\"onkeypress=\"if (event.keyCode==13) { enviarformulario(); }\" name=letra id=letra class=field style='width:80%' value='" + ( letra.equals("") ? "<< " + mUser.getTermo("MSGDIGTXTPROCURA") + " >>" : letra ) + "'><input style='width:70px' class=button type=button value='Localizar' onclick=\"enviarformulario();\"></td>");
		lHTML.append("</tr>");
		lHTML.append("</table>");

		lHTML.append("<script>document.getElementById('letra').focus();document.getElementById('letra').select()</script>");

		return lHTML.toString();
	}

	public static boolean isNotUsed(String pNM_CAMPO, ArrayList<String> pArrCamposOcultos) {
		try {
			for (int i = 0; i < pArrCamposOcultos.size(); i++) {
				if (pNM_CAMPO.equals(pArrCamposOcultos.get(i)))
					return true;
			}
		}
		catch (Exception e) {
			return false;
		}
		return false;
	}

	public static String GetAlias(String pStr) {
		return GetAlias(pStr, true);
	}

	public static String GetAlias(String pStr, boolean pWithoutWhiteSpace) {
		String ret = "";

		ret = pStr;

		if (pStr.indexOf(".") >= 0) {
			String[] arr = pStr.split("\\.");
			ret = arr[arr.length - 1];
		}

		if (pWithoutWhiteSpace && ret.indexOf(" ") >= 0) {
			String[] arr = pStr.split(" ");
			ret = arr[arr.length - 1];
		}

		return ret;
	}

	public static String getCookie(HttpServletRequest pRequest, String pName) {
		String lReturn = "";
		if (pRequest.getCookies() != null) {
			for (int i = 0; i < pRequest.getCookies().length; i++) {
				if (pRequest.getCookies()[i].getName().equalsIgnoreCase(pName)) {
					lReturn = pRequest.getCookies()[i].getValue().trim();
					break;
				}
			}
		}
		return lReturn;
	}

	public static void setCookie(HttpServletResponse pResponse, String pName, String pValue) {
		setCookie(pResponse, pName, pValue, 60 * 60 * 24 * 10);
	}

	public static void setCookie(HttpServletResponse pResponse, String pName, String pValue, int pMaxAge) {
		Cookie lCookie;
		lCookie = new Cookie(pName, pValue);
		lCookie.setMaxAge(pMaxAge);
		pResponse.addCookie(lCookie);
	}

	public static String callerClass() {
		Throwable thr = new Throwable();
		thr.fillInStackTrace();
		StackTraceElement[] ste = thr.getStackTrace();
		return ste[2].getClassName();
	}

	public static String callerMethod() {
		Throwable thr = new Throwable();
		thr.fillInStackTrace();
		StackTraceElement[] ste = thr.getStackTrace();
		return ste[2].getMethodName();
	}

	public static String getTagValue(String pString, String pTag) {
		if (pTag.indexOf('<') < 0) {
			pTag = '<' + pTag;
		}
		if (pTag.indexOf('>') < 0) {
			pTag += '>';
		}
		if (pString.indexOf(pTag) > -1) {
			return pString.substring(pString.indexOf(pTag) + pTag.length(), pString.indexOf(pTag.replaceAll("<", "</")));
		}
		else {
			return "";
		}
	}

	public static void printSystemError(String pSession, String pErro) {
		Utils.printSystemError(Utils.callerClass(), Utils.callerMethod(), pSession, pErro);
	}

	public static void printSystemError(String pClass, String pMethod, String pSession, String pErro) {
		Date lDate = new Date(System.currentTimeMillis());
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String lDataAtual = ( formatador.format(lDate).toString() );

		System.err.println(lDataAtual + " - ERRO: " + SystemConfig.getSystemName() + ": " + pClass + ": " + pMethod + ": " + ( pSession.equals("") ? "" : pSession + ": " ) + pErro);
	}

	public static boolean verifyPrimitive(String pTipo, boolean pOnlyNmuber) {
		if (pTipo.startsWith("s:")) {
			try {
				String pTipoAux = pTipo.split(":")[1];
				pTipo = pTipoAux;
			}
			catch (Exception ex) {
			}
		}
		boolean lRetorno = false;
		if (!pOnlyNmuber) {
			if (pTipo.equalsIgnoreCase("short"))
				lRetorno = true;
			else if (pTipo.equalsIgnoreCase("byte"))
				lRetorno = true;
			else if (pTipo.equalsIgnoreCase("string"))
				lRetorno = true;
			else if (pTipo.equalsIgnoreCase("boolean"))
				lRetorno = true;
			else if (pTipo.equalsIgnoreCase("char"))
				lRetorno = true;
		}
		if (pTipo.equalsIgnoreCase("int"))
			lRetorno = true;
		else if (pTipo.equalsIgnoreCase("long"))
			lRetorno = true;
		else if (pTipo.equalsIgnoreCase("float"))
			lRetorno = true;
		else if (pTipo.equalsIgnoreCase("double"))
			lRetorno = true;
		else if (pTipo.equalsIgnoreCase("decimal"))
			lRetorno = true;

		return lRetorno;

	}

	public static String removerPontoVirgula(String pValue) {
		if (pValue.equals("")) {
			return "";
		}
		else {
			return pValue.replaceAll("\\.", "").replaceAll("\\,", "");
		}
	}

	public static String convertDateFormats(String from, String to, String value) {
		SimpleDateFormat formatFrom = new SimpleDateFormat(from);
		SimpleDateFormat formatTo = new SimpleDateFormat(to);

		Date date = new Date(System.currentTimeMillis());
		try {
			date = formatFrom.parse(value);
		}
		catch (Exception e) {
			date = new Date(System.currentTimeMillis());
		}

		return formatTo.format(date);
	}

	public static String highlightSearch(String originalText, String searchText) {
		String control = Utils.removeAccent(originalText.toLowerCase());
		String splitSearch[] = searchText.toLowerCase().split(" ");
		String partSearch = "";

		int pos = 0;

		for (int part = 0; part < splitSearch.length; part++) {
			if (!splitSearch[part].trim().equals("")) {
				partSearch = Utils.removeAccent(splitSearch[part].trim());
				pos = control.indexOf(partSearch);

				while (pos >= 0) {
					originalText = originalText.substring(0, pos) + "<b>" + originalText.substring(pos, pos + partSearch.length()) + "</b>" + originalText.substring(pos + partSearch.length());
					control = originalText.toLowerCase();
					pos = control.indexOf(partSearch, pos + partSearch.length() + 7);
				}
			}
		}

		return originalText;
	}

	public static double similarity(String s1, String s2) {
		String longer = s1, shorter = s2;
		if (s1.length() < s2.length()) { // longer should always have greater length
			longer = s2;
			shorter = s1;
		}

		int longerLength = longer.length();
		if (longerLength == 0) {
			return 1.0;
		}

		return ( longerLength - getLevenshteinDistance(longer, shorter) ) / (double) longerLength;
	}

	private static int getLevenshteinDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					costs[j] = j;
				else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						}
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}

			if (i > 0) {
				costs[s2.length()] = lastValue;
			}
		}
		return costs[s2.length()];
	}
}
