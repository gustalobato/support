
package br.com.manchestercity.process;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.manchestercity.automacao.IniManipulation;

public class StartProcess implements ServletContextListener {

	static final int DAY_IN_MILLIS = 86400000;
	static final int HOUR_IN_MILLIS = 3600000;
	static final int MIN_IN_MILLIS = 60000;

	public void contextInitialized(ServletContextEvent e) {

		// CARREGA O REALPATH DA APLICAÇÃO
		IniManipulation.setRealPath(e.getServletContext().getRealPath("/"));

		// CRIA ARQUIVO .PROPERTIES CASO ELE NÃO EXISTA
		IniManipulation.createIni();
		PrintStream out;
		try {
			FileOutputStream fos = new FileOutputStream(IniManipulation.getRealPath() + "/teste_output.txt");
			fos.write(("").getBytes());
			fos.close();
			fos = new FileOutputStream(IniManipulation.getRealPath() + "/teste_output.txt");
			out = new PrintStream(fos);
			System.setOut(out);
			System.setErr(out);
			System.out.println("Hello, Finally I've printed output to file..");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		// CARREGA OS TERMOS NO CONTEXTO DA APLICAÇÃO
		LoadTranslation loadTranslation = new LoadTranslation(e.getServletContext());
		loadTranslation.loadTranslation();

		Calendar dateStart = Calendar.getInstance();
		dateStart.add(Calendar.MINUTE, 10);
		// INICIA EM 10 MINUTOS E DEPOIS VAI RODAR DE 10 EM 10 MINUTOS
		new TaskProcess(dateStart.get(Calendar.HOUR_OF_DAY), dateStart.get(Calendar.MINUTE), 0, StartProcess.MIN_IN_MILLIS * 10, loadTranslation).start();
		
		LoadRequest loadRequest = new LoadRequest(e.getServletContext());
		dateStart = Calendar.getInstance();
		dateStart.add(Calendar.MINUTE, 1);
		new TaskProcess(dateStart.get(Calendar.HOUR_OF_DAY), dateStart.get(Calendar.MINUTE), 0, StartProcess.MIN_IN_MILLIS * 10, loadRequest).start();

	}

	public void contextDestroyed(ServletContextEvent e) {
	}
}
