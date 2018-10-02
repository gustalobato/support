
package br.com.manchestercity.automacao;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultServlet extends HttpServlet {

	private static final long serialVersionUID = 1887059190452754450L;

	// INITIALIZE GLOBAL VARIABLES
	public void init() throws ServletException {
	}

	// PROCESS THE HTTP GET REQUEST
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String lServletName = request.getServletPath().substring(1);

		String lClass = getServletConfig().getInitParameter(lServletName);

		try {

			Object lServlet = Class.forName(lClass).newInstance();

			Method lMethod = null;

			// EXECUTA INIT
			try {
				lMethod = lServlet.getClass().getMethod("init");
				try {
					lMethod.invoke(lServlet);
				}
				catch (Exception ex2) {
					Utils.printSystemError("lMethod.invoke(lServlet, null)", ex2.getMessage());
				}
			}
			catch (NoSuchMethodException ex1) {
				Utils.printSystemError("lServlet.getClass().getMethod('init', null)", ex1.getMessage());
			}

			// PROCURA doPost OU doGet
			try {
				lMethod = lServlet.getClass().getMethod("doPost", new Class[] { HttpServletRequest.class, HttpServletResponse.class });
			}
			catch (NoSuchMethodException ex1) {
				lMethod = lServlet.getClass().getMethod("doGet", new Class[] { HttpServletRequest.class, HttpServletResponse.class });
			}

			// EXECUTA METODO
			if (lMethod != null) {
				lMethod.invoke(lServlet, new Object[] { request, response });
			}

			// FINALIZA SERVLET
			try {
				lMethod = lServlet.getClass().getMethod("destroy");
				try {
					lMethod.invoke(lServlet);
				}
				catch (Exception ex2) {
					Utils.printSystemError("lMethod.invoke(lServlet, null)", ex2.getMessage());
				}
			}
			catch (NoSuchMethodException ex1) {
				Utils.printSystemError("lServlet.getClass().getMethod('destroy', null)", ex1.getMessage());
			}
		}
		catch (ClassNotFoundException ex) {
			Utils.printSystemError("Class.forName(lClass).newInstance() : ClassNotFoundException", ex.getMessage());
			ex.printStackTrace();
		}
		catch (IllegalAccessException ex) {
			Utils.printSystemError("Class.forName(lClass).newInstance() : IllegalAccessException", ex.getMessage());
		}
		catch (InstantiationException ex) {
			Utils.printSystemError("Class.forName(lClass).newInstance() : InstantiationException", ex.getMessage());
		}
		catch (SecurityException ex) {
			Utils.printSystemError("Class.forName(lClass).newInstance() : SecurityException", ex.getMessage());
		}
		catch (Exception ex) {
			Utils.printSystemError("Class.forName(lClass).newInstance() : Exception", ex.getMessage());
			ex.printStackTrace();
		}
	}

	// CLEAN UP RESOURCES
	public void destroy() {
	}
}
