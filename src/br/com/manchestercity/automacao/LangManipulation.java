
package br.com.manchestercity.automacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class LangManipulation {
	// ARQUIVO DE IDIOMA PADRAO DO SISTEMA
	private static final String LANGUAGE_DEFAULT = "default.properties";
	// PADROES DO IDIOMA
	public static final String FORMAT_CHARSET = "format_charset";
	public static final String FORMAT_CURRENCY = "format_currency";
	public static final String FORMAT_DATE = "format_date";
	public static final String FORMAT_NUM_MIL = "format_num_mil";
	public static final String FORMAT_NUM_DEC = "format_num_dec";
	public static final String FORMAT_FLAG = "format_flag";

	public static boolean languageExists(String fileName) {
		try {
			return new File(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName).exists();
		}
		catch (Exception e) {
			return false;
		}
	}

	public static boolean termExists(String fileName, String termo) {
		boolean result = false;

		Properties p = new Properties();
		FileInputStream file = null;
		try {
			file = new FileInputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);
			p.load(file);
			result = p.containsKey(termo);
		}
		catch (Exception e) {
			result = false;
		}

		try {
			file.close();
		}
		catch (Exception e) {
			Utils.printSystemError("file.close()", e.getMessage());
		}

		return result;
	}
	
	@SuppressWarnings( "rawtypes" )
	public static String translationComparison(String fileName, String traducao) {
		String result = "";

		Properties p = new Properties();
		FileInputStream file = null;
		try {
			file = new FileInputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);
			p.load(file);
			
			String termo = "";
			String valor = "";
			
			Enumeration termos = p.propertyNames();
			while (termos.hasMoreElements()) {
				termo = (String) termos.nextElement();
				valor = p.getProperty(termo);
				
				if (Utils.similarity(Utils.removeSpecialChar(traducao), Utils.removeSpecialChar(valor)) > 0.5) {
					result += (result.equals("") ? "" : "<br/>") + termo + " = " + valor;
				}
			}
		}
		catch (Exception e) {
			result = "";
		}

		try {
			file.close();
		}
		catch (Exception e) {
			Utils.printSystemError("file.close()", e.getMessage());
		}

		return result;
	}

	public static synchronized void createLanguage(String fileName) {
		if (!languageExists(fileName)) {
			Properties p = new Properties();
			FileOutputStream out = null;

			if (!languageExists(LANGUAGE_DEFAULT)) {
				try {
					File file = new File(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);
					out = new FileOutputStream(file);
					// FORMATOS DE IDIOMA
					p.setProperty(LangManipulation.FORMAT_CHARSET, "ISO-8859-1");
					p.setProperty(LangManipulation.FORMAT_CURRENCY, "R$");
					p.setProperty(LangManipulation.FORMAT_DATE, "dd/MM/yyyy");
					p.setProperty(LangManipulation.FORMAT_NUM_MIL, ".");
					p.setProperty(LangManipulation.FORMAT_NUM_DEC, ",");
					p.setProperty(LangManipulation.FORMAT_FLAG, "metronic/global/img/flags/br.png");
					// IDIOMAS PADRAO
					p.setProperty("DEFAULT", "Português (Padrão)");
					p.setProperty("EN", "Inglês");
					p.setProperty("EN_UK", "Inglês (UK)");
					p.setProperty("EN_US", "Inglês (USA)");
					p.setProperty("ES", "Espanhol");
					p.setProperty("ES_AR", "Espanhol (Argentina)");
					p.setProperty("ES_ES", "Espanhol (Espanha)");
					p.setProperty("ES_UY", "Espanhol (Uruguai)");
					p.setProperty("PT", "Português");
					p.setProperty("PT_BR", "Português (Brasil)");
					p.setProperty("PT_PT", "Português (Portugal)");
					// LABELS E TEXTOS
					p.setProperty("ABAS", "Abas");
					p.setProperty("ACESSAR", "Acessar");
					p.setProperty("CANCELAR", "Cancelar");
					p.setProperty("CARREGANDO", "Carregando");
					p.setProperty("DASHBOARD", "Dashboard");
					p.setProperty("EMAIL", "e-Mail");
					p.setProperty("ENVIAR", "Enviar");
					p.setProperty("ESQUECEUSENHA", "Esqueceu sua senha?");
					p.setProperty("EXCEL", "Excel");
					p.setProperty("EXCLUIR", "Excluir");
					p.setProperty("EXPORTAR", "Exportar");
					p.setProperty("FAVORITO", "Favorito");
					p.setProperty("FAVORITOS", "Favoritos");
					p.setProperty("IDIOMA", "Idioma");
					p.setProperty("IMPORTAR", "Importar");
					p.setProperty("IMPRIMIR", "Imprimir");
					p.setProperty("LOCALIZAR", "Localizar");
					p.setProperty("LOGIN", "Login");
					p.setProperty("MENU", "Menu");
					p.setProperty("NOVO", "Novo");
					p.setProperty("OK", "OK");
					p.setProperty("PDF", "PDF");
					p.setProperty("SAIR", "Sair");
					p.setProperty("SALVAR", "Salvar");
					p.setProperty("SENHA", "Senha");
					p.setProperty("TASKLIST", "Task List");
					p.setProperty("USUARIO", "Usuário");
					p.setProperty("VOLTAR", "Voltar");
					// MENSAGENS DE ERRO
					p.setProperty("ERROR_DEFAULT", "Ocorreu um erro durante esta operação. Por favor entre em contato com o administrador do sistema.");
					p.setProperty("ERROR_SENHAINVALIDA", "Senha Inválida");
					// MENSAGENS DE SUCESSO
					p.setProperty("SUCCESS_DEFAULT", "Operação realizada com sucesso!");
					p.setProperty("SUCCESS_SENHAALTERADA", "Senha alterada com Sucesso!");
					// MENSAGENS DE INFORMACAO
					p.setProperty("INFO_DEFAULT", "<font class='text-danger'> * </font> Campo Obrigatório");
					p.setProperty("INFO_ALTSENHAAD", "Seu usuário usa autenticação AD. Entre em contato com o Administrador para alterar sua senha.");
					p.setProperty("INFO_BUSCARAPIDA", "Digite um texto com 2 ou mais caracteres para realizar uma busca em todo o sistema");
					// MENSAGENS DE LINKS
					p.setProperty("LINK_DEFAULT", "Clique aqui");
					p.setProperty("LINK_RECUPERARSENHA", "Clique aqui para gerar uma nova senha.");

					p.store(out, "");
				}
				catch (Exception e) {
				}
			}
			else {
				FileInputStream inp = null;
				try {
					inp = new FileInputStream(new File(IniManipulation.getRealPath() + "/WEB-INF/lang/" + LANGUAGE_DEFAULT));
					out = new FileOutputStream(new File(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName));

					int length;
					byte[] buffer = new byte[1024];

					while (( length = inp.read(buffer) ) > 0) {
						out.write(buffer, 0, length);
					}
				}
				catch (Exception e) {
				}

				try {
					inp.close();
				}
				catch (Exception e) {
				}
			}

			try {
				out.close();
			}
			catch (Exception e) {
			}

			out = null;
			p = null;
		}
	}

	public static synchronized ArrayList<String> getLanguageFiles() {
		File folder = new File(IniManipulation.getRealPath() + "/WEB-INF/lang");
		File[] listOfFiles = folder.listFiles();

		ArrayList<String> fileNames = new ArrayList<String>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				fileNames.add(listOfFiles[i].getName());
			}
		}

		return fileNames;
	}

	@SuppressWarnings( "rawtypes" )
	public static Enumeration getLanguageTranslations(String fileName) {
		Enumeration lEnum = null;
		FileInputStream lFile = null;

		try {
			Properties p = new Properties();
			lFile = new FileInputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);
			p.load(lFile);
			lEnum = p.propertyNames();
		}
		catch (Exception e) {
			lEnum = null;
		}

		try {
			lFile.close();
		}
		catch (IOException e) {
			Utils.printSystemError("file.close", e.getMessage());
		}

		return lEnum;
	}

	public static String getLanguageTranslation(String fileName, String termo) {
		String value = "";
		FileInputStream file = null;

		try {
			Properties p = new Properties();
			file = new FileInputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);
			p.load(file);
			value = p.getProperty(termo);
			if (value == null) {
				value = "";
			}

		}
		catch (Exception e) {
			value = "";
		}

		try {
			file.close();
		}
		catch (Exception e) {
			Utils.printSystemError("file.close()", e.getMessage());
		}

		return value;
	}

	public static synchronized boolean insLanguageTranslation(String fileName, String termo, String valor) {
		boolean result = false;

		FileInputStream file = null;
		FileOutputStream out = null;

		try {
			if (!termo.trim().equals("")) {
				Properties p = new Properties();
				file = new FileInputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);
				p.load(file);
				p.setProperty(termo, valor);

				try {
					file.close();
				}
				catch (Exception e) {
				}

				out = new FileOutputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);

				p.store(out, "");
				result = true;
			}
		}
		catch (Exception e) {
		}

		try {
			file.close();
		}
		catch (Exception e) {
		}

		try {
			out.close();
		}
		catch (Exception e) {
		}

		return result;
	}

	@SuppressWarnings( "rawtypes" )
	public static synchronized boolean delLanguageTranslation(String fileName, String termo) {
		boolean result = false;

		FileInputStream file = null;
		FileOutputStream out = null;

		try {
			Properties p = new Properties();
			Properties aux = new Properties();
			file = new FileInputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);
			p.load(file);

			try {
				file.close();
			}
			catch (Exception e) {
			}

			out = new FileOutputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);

			Enumeration names = p.propertyNames();
			String item = "";
			while (names.hasMoreElements()) {
				item = (String) names.nextElement();
				if (!item.equals(termo)) {
					aux.setProperty(item, p.getProperty(item));
				}
			}

			aux.store(out, "");
			result = true;
		}
		catch (Exception e) {
		}

		try {
			file.close();
		}
		catch (Exception e) {
		}

		try {
			out.close();
		}
		catch (Exception e) {
		}

		return result;
	}

	public static synchronized boolean updLanguageTranslation(String fileName, String termo, String valor) {
		boolean result = false;

		FileInputStream file = null;
		FileOutputStream out = null;

		try {
			Properties p = new Properties();
			file = new FileInputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);
			p.load(file);
			p.setProperty(termo, valor);

			try {
				file.close();
			}
			catch (Exception e) {
			}

			out = new FileOutputStream(IniManipulation.getRealPath() + "/WEB-INF/lang/" + fileName);

			p.store(out, "");
			result = true;
		}
		catch (Exception e) {
		}

		try {
			file.close();
		}
		catch (Exception e) {
		}

		try {
			out.close();
		}
		catch (Exception e) {
		}

		return result;
	}
}
