
package br.com.manchestercity.process;

import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import br.com.manchestercity.automacao.CUrlConnection;
import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.LangManipulation;
import br.com.manchestercity.automacao.User;
import br.com.manchestercity.automacao.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadRequest extends TimerTask {

	private ServletContext context;

	public LoadRequest(ServletContext context) {
		this.context = context;
	}

	public void run() {
		this.loadRequest();
	}

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public synchronized void loadRequest() {
		Database lcdb = new Database(new User(null));
		Connection conn2 = lcdb.openConnection();

		CookieStore lCookies = null;
		
		
		
		String lLogin = "gustavo@manchestercity.com.br";
		String lSenha = "porragustavo2017";
		String query = "";

		StringBuffer lsb = new StringBuffer();
		try {
			HttpURLConnection conn = null;
			query="j_username=" + URLEncoder.encode(lLogin) + "&j_password=" + URLEncoder.encode(lSenha) ;
	//		conn = CUrlConnection.OpenUrl("https://supportersclubportal.mancity.com/j_spring_security_check", query,lsb, lCookies,false,null);
			
			//System.err.println(lsb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.err.println(lsb.toString());
		
		
		
		
		Database.closeObject(conn2);
	}
	
}
