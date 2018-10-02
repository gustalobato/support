
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

public class LoadTranslation extends TimerTask {

	private ServletContext context;

	public LoadTranslation(ServletContext context) {
		this.context = context;
	}

	public void run() {
		this.loadTranslation();
	}

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	
	public synchronized void loadTranslation() {
		Database lcdb = new Database(new User(null));
		Connection conn = lcdb.openConnection();

		String termo = "";
		String translation = "";

		HashMap<String, String> translations = new HashMap<String, String>();
		HashMap<String, HashMap<String, String>> languages = new HashMap<String, HashMap<String, String>>();

		ArrayList<String> langFiles = LangManipulation.getLanguageFiles();
		try {
			Enumeration propNames = null;
			for (String langFile : langFiles) {
				translations = new HashMap<String, String>();
				propNames = LangManipulation.getLanguageTranslations(langFile);
				while (propNames.hasMoreElements()) {
					termo = (String) propNames.nextElement();
					translation = LangManipulation.getLanguageTranslation(langFile, termo);
					translations.put(termo, translation);
				}
				languages.put(Utils.replaceAll(langFile, ".properties", "").toUpperCase().trim(), translations);
			}
		}
		catch (Exception e) {
			Utils.printSystemError("ERROR 01", e.getMessage());
			e.printStackTrace();
		}
		finally {
			langFiles.clear();
		}

		try {
			HashMap<String, HashMap<String, String>> languagesOld = (HashMap<String, HashMap<String, String>>) context.getAttribute("TRANSLATIONS");
			if (languagesOld == null) {
				context.setAttribute("TRANSLATIONS", languages);
			}
			else {
				languagesOld.clear();
				languagesOld.putAll(languages);
			}
		}
		catch (Exception e) {
			Utils.printSystemError("ERROR 02", e.getMessage());
			e.printStackTrace();
		}

		Database.closeObject(conn);
	}
}
