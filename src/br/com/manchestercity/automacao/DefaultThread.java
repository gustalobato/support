
package br.com.manchestercity.automacao;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class DefaultThread extends Thread {

	String mClass;
	Object[] mClassParameters;
	String mMethod;
	Object[] mMethodParameters;
	@SuppressWarnings( "rawtypes" )
	Class[] mMethodParametersDefinition;
	@SuppressWarnings( "rawtypes" )
	Class[] mClassParametersDefinition;

	public DefaultThread(String pClass, Object[] pClassParameters, String pMethod, Object[] pMethodParameters) {
		mClass = pClass;
		mClassParameters = pClassParameters;
		mMethod = pMethod;
		mMethodParameters = pMethodParameters;
		this.start();
	}

	@SuppressWarnings( "rawtypes" )
	public DefaultThread(String pClass, Class[] pClassParametersDefinition, Object[] pClassParameters, String pMethod, Class[] pMethodParametersDefinition, Object[] pMethodParameters) {
		mClass = pClass;
		mClassParameters = pClassParameters;
		mMethod = pMethod;
		mMethodParameters = pMethodParameters;
		mClassParametersDefinition = pClassParametersDefinition;
		mMethodParametersDefinition = pMethodParametersDefinition;
		this.start();
	}

	@SuppressWarnings( "rawtypes" )
	public DefaultThread(String pClass, String pMethod, Class[] pMethodParametersDefinition, Object[] pMethodParameters) {
		mClass = pClass;
		mMethod = pMethod;
		mMethodParameters = pMethodParameters;
		mMethodParametersDefinition = pMethodParametersDefinition;
		this.start();
	}

	public DefaultThread(String pClass, String pMethod, Object[] pMethodParameters) {
		mClass = pClass;
		mMethod = pMethod;
		mMethodParameters = pMethodParameters;
		this.start();
	}

	@SuppressWarnings( "rawtypes" )
	public void run() {
		try {
			Object lObject;
			Method lMethod = null;
			// CAPTURA AS CLASSES DOS PARAMETROS DO MÉTODO E GERA UM ARRAY DE CLASSES
			if (mMethodParameters != null) {
				if (mMethodParametersDefinition == null) {
					mMethodParametersDefinition = new Class[mMethodParameters.length];
					for (int x = 0; x < mMethodParameters.length; x++) {
						mMethodParametersDefinition[x] = mMethodParameters[x].getClass();
					}
				}
			}

			// SE TIVER CONSTRUTORES DIFERENTES, PASSANDO O ClassParameters ELE VAI INSTANCIAR A CLASSE A PARTIR DO CONSTRUTOR
			if (mClassParameters != null) {
				// CAPTURA AS CLASSES DOS PARÂMETROS DO CONSTRUTOR E GERA UM ARRAY DE CLASSES
				if (mClassParametersDefinition == null) {
					mClassParametersDefinition = new Class[mClassParameters.length];
					for (int x = 0; x < mClassParameters.length; x++) {
						mClassParametersDefinition[x] = mClassParameters[x].getClass();
					}
				}
				// CAPTURA O CONSTRUTOR QUE TEM O CABECALHO IGUAL AO PASSADO POR PARAMETRO
				Constructor lConstructor = Class.forName(mClass).getConstructor(mClassParametersDefinition);
				// INSTANCIA O OBJETO A PARTIR DO CONSTRUTOR
				lObject = lConstructor.newInstance(mClassParameters);
			}
			else {
				// INSTANCIA O OBJETO A PARTIR DO NOME DA CLASSE PASSADO POR PARAMETRO
				lObject = Class.forName(mClass).newInstance();
			}

			// CAPTURA O MÉTODO A SER CHAMADO QUE POSSUA O CABECALHO DOS PARAMETROS PASSADOS
			lMethod = lObject.getClass().getMethod(mMethod, mMethodParametersDefinition);
			// CHAMA O MÉTODO COM OS PARAMETROS PASSADOS
			lMethod.invoke(lObject, mMethodParameters);

		}
		catch (Exception ex) {
			Utils.printSystemError("CDefaultThread", "run()", "", ex.getMessage());
			ex.printStackTrace();
		}
	}
}
