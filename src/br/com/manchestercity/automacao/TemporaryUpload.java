
package br.com.manchestercity.automacao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;

import sun.misc.BASE64Encoder;

// DEVE SER UTILIZADO DENTRO DE UM IFRAME
// REALIZA AS OPERÇÕES E APLICA OS RESULTADOS NO PARENT
public class TemporaryUpload extends HttpServlet {

	private static final long serialVersionUID = 2639479886891999203L;

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings( "unchecked" )
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		User user = User.getSession(request, response, false, true, "USER");

		response.setHeader("Content-Type", "text/html;charset=" + user.getUserCharset());

		Utils.noCacheStatic(response, request);

		StringBuffer erro = new StringBuffer();

		String fieldImage = "";
		String fieldTarget = Database.verifyNull(request.getParameter("field_target_id")); // ID DO OBJETO ONDE A IMAGEM SERA EXIBIDA
		String functTarget = Database.verifyNull(request.getParameter("field_target_fn")); // ID DO METODO DE INSERCAO DO HTML NO CKEDITOR
		String fieldTargetType = Database.verifyNull(request.getParameter("field_target_type")); // ckeditor OU html (EXECUTA A FUNCAO insertHTML OU innerHTML)

		String script = "";

		Object[] tempImgArray = null;

		try {
			if (FileUpload.isMultipartContent(request)) {
				tempImgArray = (Object[]) request.getSession().getAttribute("TEMP_IMG_ARRAY");
				tempImgArray = tempImgArray == null ? new Object[1] : Arrays.copyOf(tempImgArray, tempImgArray.length + 1);

				FileItem myImgItem = null;

				DiskFileUpload upload = new DiskFileUpload();
				Iterator<FileItem> i = upload.parseRequest(request).iterator();
				while (i.hasNext()) {
					FileItem item = (FileItem) i.next();
					if (item.isFormField() && item.getFieldName().equalsIgnoreCase("field_target_id")) {
						fieldTarget = item.getString().trim();
					}
					else if (item.isFormField() && item.getFieldName().equalsIgnoreCase("field_target_fn")) {
						functTarget = item.getString().trim();
					}
					else if (item.isFormField() && item.getFieldName().equalsIgnoreCase("field_target_type")) {
						fieldTargetType = item.getString().trim();
					}
					else if (item.getSize() > 0 && item.getFieldName().equalsIgnoreCase("txtUploadField")) {
						myImgItem = item;
					}
				}

				if (myImgItem != null && myImgItem.getContentType().toLowerCase().indexOf("image") > -1) {
					int size = tempImgArray.length - 1;

					tempImgArray[size] = myImgItem.get();

					request.getSession().setAttribute("TEMP_IMG_ARRAY", tempImgArray);

					BASE64Encoder encoder = new BASE64Encoder();
					String byteString = encoder.encode(myImgItem.get()).replaceAll("\\r\\n", "");
					// String byteString = Base64.encodeBase64String(myImgItem.get()); // desse jeito dava pau na c&a

					if (!byteString.trim().equals("")) {
						fieldImage = "<img id=\"temp_img_" + size + "\" src=\"data:" + myImgItem.getContentType().toLowerCase() + ";base64," + byteString + "\" />";

						if (fieldTargetType.trim().equals("ckeditor") && !functTarget.trim().equals("")) {
							script = "parent." + functTarget + "('" + fieldImage + "');";
						}
						else {
							script = "parent.document.getElementById('" + fieldTarget + "').innerHTML = '" + fieldImage + "' + parent.document.getElementById('" + fieldTarget + "').innerHTML;";
						}
					}
				}
				else {
					erro.append(user.getTermo("MSGIMAGEMVALIDA"));
				}
			}
		}
		catch (Exception ex) {
			erro.append(user.getTermo("MSGERROENVIO"));
		}

		out.println("<html>");
		out.println("  <head>");
		out.println("    <title>Temporary File Upload</title>");
		out.println("  </head>");
		out.println("  <script>");
		out.println(script);
		if (!erro.toString().trim().equals("")) {
			out.println("alert('" + erro.toString() + "');");
		}
		out.println("  </script>");
		out.println("  <body style='border:0; margin:0'>");
		out.println("    <form id='upload_temp_form' name='upload_temp_form' enctype='multipart/form-data' method='post' action='cuploadtemp' target='_self'>");
		out.println("      <input type='hidden' id='field_target_id' name='field_target_id' value='" + fieldTarget + "'>");
		out.println("      <input type='hidden' id='field_target_fn' name='field_target_fn' value='" + functTarget + "'>");
		out.println("      <input type='hidden' id='field_target_type' name='field_target_type' value='" + fieldTargetType + "'>");
		out.println("      <table width='100%' cellpadding='2' cellspacing='2' border='0' align='center'>");
		out.println("        <tr>");
		out.println("          <td width='90%' valign='top'>");
		out.println("            <input type='file' class='field' name='txtUploadField' id='txtUploadField' value='' />");
		out.println("          </td>");
		out.println("          <td width='10%' valign='top'>");
		out.println("            <input type='button' class='button' name='submitUploadField' id='submitUploadField' value='" + user.getTermo("ENVIAR") + "' onclick='document.upload_temp_form.submit();' />");
		out.println("          </td>");
		out.println("        </tr>");
		out.println("      </table>");
		out.println("    </form>");
		out.println("  </body>");
		out.println("</html>");
	}

}
