
package br.com.manchestercity.automacao;

public class Crypt {

	public static String addEncriptation(String pString) {

		String lEnc = "";
		int lInt = 0;
		int lAux = 0;
		char lChar = 'a';

		for (lInt = 0; lInt < pString.length(); lInt++) {
			lAux = (int) ( ( lInt + 1 ) / 2 );
			if (lAux * 2 == ( lInt + 1 )) {
				lChar = pString.substring(lInt, lInt + 1).toCharArray()[0];
				lEnc += (char) ( ( (int) lChar ) + 5 );
			}
			else {
				lChar = pString.substring(lInt, lInt + 1).toCharArray()[0];
				lEnc += (char) ( ( (int) lChar ) + 3 );
			}
		}

		return lEnc;
	}

	public static String removeEncriptation(String pString) {

		String lEnc = "";
		int lInt = 0;
		int lAux = 0;
		char lChar = 'a';

		for (lInt = 0; lInt < pString.length(); lInt++) {
			lAux = (int) ( ( lInt + 1 ) / 2 );
			if (lAux * 2 == ( lInt + 1 )) {
				lChar = pString.substring(lInt, lInt + 1).toCharArray()[0];
				lEnc += (char) ( ( (int) lChar ) - 5 );
			}
			else {
				lChar = pString.substring(lInt, lInt + 1).toCharArray()[0];
				lEnc += (char) ( ( (int) lChar ) - 3 );
			}
		}
		return lEnc;
	}
}
