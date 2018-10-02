
package br.com.manchestercity.automacao;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Command {

	public Command() {
	}

	public static boolean executeCommand(String pCommand, StringBuffer pSucess, StringBuffer pErr) {
		pSucess.delete(0, pSucess.length());
		pErr.delete(0, pErr.length());

		try {
			String osName = "";
			String[] cmd = new String[3];

			if (osName.equals("Windows 95")) {
				cmd[0] = "command.com";
				cmd[1] = "/C";
				cmd[2] = pCommand;
			}
			else {
				cmd[0] = "cmd.exe";
				cmd[1] = "/C";
				cmd[2] = pCommand;
			}

			Runtime rt = Runtime.getRuntime();
			System.out.println("Execing " + cmd[0] + " " + cmd[1] + " " + cmd[2]);
			Process proc = rt.exec(cmd);
			// any error message?
			InputStreamReader isr = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while (( line = br.readLine() ) != null)
				pSucess.append(line + "\n");

			isr = new InputStreamReader(proc.getErrorStream());
			br = new BufferedReader(isr);
			line = null;
			while (( line = br.readLine() ) != null)
				pErr.append(line + "\n");

			proc.waitFor();
		}
		catch (Throwable t) {
			return false;
		}

		return pErr.length() == 0;
	}
}
