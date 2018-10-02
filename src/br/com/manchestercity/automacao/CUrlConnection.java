package br.com.manchestercity.automacao;

import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;

import br.com.manchestercity.negocio.NEGREQUEST;
import sun.net.www.http.HttpCaptureInputStream;
import sun.security.jgss.spnego.NegTokenInit;

import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Iterator;

public class CUrlConnection {

	public static HttpURLConnection OpenUrl(String pUrl, String pQuery, StringBuffer pReturn, CookieStore pCookies, boolean USE, HttpURLConnection pConn ) throws Exception {
		CookieStore cookieJar =  null;
		
		Database lcdb = new Database(null);
		Connection mConn = lcdb.openConnection();		// Install the all-trusting trust manager
		Connection lConn = lcdb.openConnectionFACAPARTE();

		
		NEGREQUEST lNeg = new NEGREQUEST(null);
		try {
			
			 
			 ResultSet lRs = lcdb.openResultSet("SELECT * FROM wp_posts WHERE GUID LIKE 'http://facaparte.manchestercity.com.br/?post_type=wpcf7s&p=%' AND POST_CONTENT LIKE 'Nome%'", lConn);
			 
			 while(lRs.next()) {
				 lNeg.setORIGEM("F");
				 lNeg.setCD_REQUEST(Database.verifyNull(lRs.getObject("ID")));
				 lNeg.setFIRSTNAME(Database.verifyNull(lRs.getObject("POST_CONTENT")).split("Nome: ")[1].split("\n")[0]);
					//lNeg.setLASTNAME(Database.verifyNull(lRs.getObject("POST_CONTENT")).split("Nome:")[1].split("\n")[0]);
					lNeg.setDATEBIRTH(Database.verifyNull(lRs.getObject("POST_CONTENT")).split("Data Nascimento: ")[1].split("\n")[0]);
				//	lNeg.setNATONALITY(lPessoa[4].split("<td>")[2].split("</td>")[0].trim());
				//	lNeg.setGENDER(lPessoa[5].split("<td>")[2].split("</td>")[0].trim());
				//	lNeg.setCOUNTRY(lPessoa[6].split("<td>")[2].split("</td>")[0].trim());
					lNeg.setCITY(Database.verifyNull(lRs.getObject("POST_CONTENT")).split("Cidade: ")[1].split("\n")[0] +" - " +Database.verifyNull(lRs.getObject("POST_CONTENT")).split("Estado: ")[1].split("\n")[0]);
				//	lNeg.setPOSTCODE(lPessoa[8].split("<td>")[2].split("</td>")[0].trim());
					lNeg.setEMAIL(Database.verifyNull(lRs.getObject("POST_CONTENT")).split("E-mail: ")[1].split("\n")[0]);
					lNeg.setPHONE(Database.verifyNull(lRs.getObject("POST_CONTENT")).split("Celular: ")[1].split("\n")[0]);
				//	lNeg.setISCITYZEN(lPessoa[11].split("<td>")[2].split("</td>")[0].trim());
				//	lNeg.setSUPPORTERNUMBER(lPessoa[12].split("<td>")[2].split("</td>")[0].trim());
				//	lNeg.setLALLOWCONTACT(lPessoa[13].split("<td>")[2].split("</td>")[0].trim());
				//	lNeg.setTRDPARTYCONTACT(lPessoa[14].split("<td>")[2].split("</td>")[0].trim());
				//	lNeg.setOPENTEXT(lPessoa[15].split("<td>")[2].split("</td>")[0].trim());
				//	lNeg.setSTATUS(lPessoa[16].split("<td>")[2].split("</td>")[0].trim());		
					if(lcdb.valorDB("REQUEST","CD_REQUEST","ORIGEM ='"+lNeg.getORIGEM()+"' AND CD_REQUEST="+lNeg.getCD_REQUEST(), mConn).equals("")) {
						lNeg.insert(mConn, new StringBuffer());
					}else {	

						lNeg.update(mConn, new StringBuffer());
					}
				 
				// System.err.println(Database.verifyNull(lRs.getObject("POST_CONTENT")));
			 }
			 
			 
			
		}catch(Exception ex) {}
		finally {
			Database.closeObject(lConn);
		}
		
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}
		};
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		String lDADOSPESSOAS = "";
		URL url;
		HttpURLConnection connection = pConn;
		String lRetorno ="";
		try {
			
			//Create connection
			url = new URL(pUrl);
		//	if(USE) {
			 CookieManager manager = new CookieManager();
	        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
	        CookieHandler.setDefault(manager);
		//	}
			
			if (pCookies != null){
				Iterator it = pCookies.getCookies().iterator();
				while (it.hasNext()) {
					HttpCookie cookie = (HttpCookie) it.next();
					cookie.setPath("/membership/requests.htm");
			         pCookies.add(url.toURI(), cookie);
				}
			}
				
			connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(300000);
		         
			
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(pQuery.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			wr.writeBytes (pQuery);
			wr.flush();
			wr.close();

			//Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			
			url = new URL("https://supportersclubportal.mancity.com/membership/requests.htm");
			
			
			connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(300000);
		         
			
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(pQuery.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			wr = new DataOutputStream (connection.getOutputStream ());
			wr.writeBytes (pQuery);
			wr.flush();
			wr.close();

			//Get Response
			is = connection.getInputStream();
			rd = new BufferedReader(new InputStreamReader(is));
			while((line = rd.readLine()) != null) {
				pReturn.append(line);
				pReturn.append('\r');
			}
			rd.close();
			
			String lDados[] = pReturn.toString().split("<table width=");
			String lTable = lDados[1].split("</table>")[0];
		
		
			
			int lFor = Database.verifyNullInt(lDados[1].split("&page=")[lDados.length].split("\"")[0]);
			String lCD_REQUEST = "";
			for (int j=1;j<=lFor+1;j++) {
				lDados = pReturn.toString().split("<table width=");
				lTable = lDados[1].split("</table>")[0];
			
				String[] lPessoas = lTable.split("<tr>");
				
				String[] DadosPessoa = null;
				String lURL = "";
				for(int i=0;i<=lPessoas.length;i++) {
					try {
						DadosPessoa = lPessoas[i].split("<td>");
						lURL = DadosPessoa[6].split("</td>")[1].split("a href=")[1].split(" class=")[0].replaceAll("\"", "");
						
						lURL = "https://supportersclubportal.mancity.com"+lURL;
						lCD_REQUEST = lURL.split("=")[1];
						url = new URL(lURL);
						connection = (HttpURLConnection)url.openConnection();
						connection.setConnectTimeout(300000);
					         
						
						connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko");
						connection.setRequestMethod("POST");
						connection.setRequestProperty("Content-Type", "text/html; charset=ISO-8859-1");
						connection.setRequestProperty("Content-Length", "" + Integer.toString(pQuery.getBytes().length));
						connection.setRequestProperty("Content-Language", "en-uk");
						
						connection.setUseCaches(false);
						connection.setDoInput(true);
						connection.setDoOutput(true);
	
						//Send request
						wr = new DataOutputStream (connection.getOutputStream ());
						wr.writeBytes (pQuery);
						wr.flush();
						wr.close();
						pReturn.delete(0, pReturn.length());

						//Get Response
						is = connection.getInputStream();
						rd = new BufferedReader(new InputStreamReader(is));
						while((line = rd.readLine()) != null) {
							pReturn.append(line);
							pReturn.append('\r');
						}
						rd.close();
						
						lDADOSPESSOAS = pReturn.toString().split("<table")[1];
						lDADOSPESSOAS = lDADOSPESSOAS.split("</table")[0];
						lDADOSPESSOAS = lDADOSPESSOAS.replaceAll("Ã©", "é").replaceAll("Ã§", "ç").replaceAll("Ã­", "í").replaceAll("Ã£", "ã").replaceAll("Ã³", "ó").replaceAll("Ãº", "ú").replaceAll("Ã¡", "á");
						lDADOSPESSOAS = lDADOSPESSOAS.replaceAll("Ã´","ô").replaceAll("Ã¢","â").replaceAll("Ã³", "ó").replaceAll("Ã‰", "É").replaceAll("Ãº", "ú").replaceAll("Ãƒ", "Ã").replaceAll("Ã‰", "É").replaceAll("Ã‡", "Ç").replaceAll("Ãª", "ê");
						
						String[] lPessoa = lDADOSPESSOAS.split("<tr>");
						
						lNeg.setCD_REQUEST(lCD_REQUEST);
						lNeg.setFIRSTNAME(lPessoa[1].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setLASTNAME(lPessoa[2].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setDATEBIRTH(lPessoa[3].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setNATONALITY(lPessoa[4].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setGENDER(lPessoa[5].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setCOUNTRY(lPessoa[6].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setCITY(lPessoa[7].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setPOSTCODE(lPessoa[8].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setEMAIL(lPessoa[9].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setPHONE(lPessoa[10].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setISCITYZEN(lPessoa[11].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setSUPPORTERNUMBER(lPessoa[12].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setLALLOWCONTACT(lPessoa[13].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setTRDPARTYCONTACT(lPessoa[14].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setOPENTEXT(lPessoa[15].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setSTATUS(lPessoa[16].split("<td>")[2].split("</td>")[0].trim());
						lNeg.setORIGEM("C");						
						if(lcdb.valorDB("REQUEST","CD_REQUEST","ORIGEM ='"+lNeg.getORIGEM()+"' AND CD_REQUEST="+lNeg.getCD_REQUEST(), mConn).equals("")) {
							lNeg.insert(mConn, new StringBuffer());
						}else {

							lNeg.update(mConn, new StringBuffer());
						}
					}catch(Exception ex) {}
				}
				
				url = new URL("https://supportersclubportal.mancity.com/membership/requests.htm?page="+j);
				
				
				connection = (HttpURLConnection)url.openConnection();
				connection.setConnectTimeout(300000);
			         
				
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko");
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Content-Length", "" + Integer.toString(pQuery.getBytes().length));
				connection.setRequestProperty("Content-Language", "en-US");
				
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);

				//Send request
				wr = new DataOutputStream (connection.getOutputStream ());
				wr.writeBytes (pQuery);
				wr.flush();
				wr.close();
				pReturn.delete(0, pReturn.length());
				//Get Response
				is = connection.getInputStream();
				rd = new BufferedReader(new InputStreamReader(is));
				while((line = rd.readLine()) != null) {
					pReturn.append(line);
					pReturn.append('\r');
				}
				rd.close();
				
			}
			
		//	System.err.print(lTable);
			
			
			

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			if(connection != null) {
				connection.disconnect();
			}
			Database.closeObject(mConn);
		}
		
		


		return connection;


	}

}

