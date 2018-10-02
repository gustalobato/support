
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class MultipleUpload {

	private User mUser;

	private String mID;
	private String mURL;

	private String mFileTextValue;

	private boolean mShowOnlyImages;
	private boolean mShowStartUpload;
	private boolean mShowFileText;
	private boolean mDisableFileUpload;

	private int mFileTextLength;

	private ArrayList<String> extraColumns;

	public MultipleUpload(User pUser) {
		this.mUser = pUser;
		this.init();

		mID = "";
		mURL = "";

		mFileTextValue = "";

		extraColumns = new ArrayList<String>();
	}

	private void init() {
		mShowOnlyImages = false;
		mShowStartUpload = false;
		mShowFileText = false;
		mDisableFileUpload = false;

		mFileTextLength = 30;
	}

	public String getID() {
		return mID;
	}

	public void setID(String pID) {
		this.mID = pID;
	}

	public String getURL() {
		return mURL;
	}

	public void setURL(String pURL) {
		this.mURL = pURL;
	}

	public boolean isShowOnlyImages() {
		return mShowOnlyImages;
	}

	public void setShowOnlyImages(boolean pShowOnlyImages) {
		mShowOnlyImages = pShowOnlyImages;
	}

	public boolean isShowStartUpload() {
		return mShowStartUpload;
	}

	public void setShowStartUpload(boolean pShowStartUpload) {
		mShowStartUpload = pShowStartUpload;
	}

	public boolean isDisableFileUpload() {
		return mDisableFileUpload;
	}

	public void setDisableFileUpload(boolean pDisableFileUpload) {
		mDisableFileUpload = pDisableFileUpload;
	}

	public boolean isShowFileText() {
		return mDisableFileUpload;
	}

	public void setShowFileText(boolean pShowFileText) {
		mShowFileText = pShowFileText;
	}

	public int getFileTextLength() {
		return mFileTextLength;
	}

	public void setFileTextLength(int pFileTextLength) {
		mFileTextLength = pFileTextLength;
	}

	// VALORES DOS ATRIBUTO DO OBJETO VINDOS DO RESPONSE JSON
	/*
	 * {"files": [ { "_isg_id": "_um_valor_qualquer_", "_isg_user": "_outro_valor_qualquer_", . . . . } ]}
	 */

	public void setFileTextValue(String attrJSON) {
		mFileTextValue = attrJSON;
	}

	public void addExtraColumn(String attrJSON) {
		extraColumns.add(attrJSON);
	}

	public String printMultipleUpload() {
		StringBuffer html = new StringBuffer();

		html.append(this.printHTML());

		html.append("<script>");
		// Initialize the jQuery File Upload widget
		html.append(this.printJavaScript());
		html.append("</script>");

		return html.toString();
	}

	public String printHTML() {
		StringBuffer html = new StringBuffer();

		html.append("<div class='row fileupload-buttonbar'>\n");
		html.append("  <div class='col-md-6'>\n");

		// THE FILEINPUT-BUTTON SPAN IS USED TO STYLE THE FILE INPUT FIELD AS BUTTON
		html.append("    <span class='btn blue-steel fileinput-button'>\n");
		html.append("      <i class='fa fa-plus'></i>\n");
		html.append("      <span> " + mUser.getTermo("ADICIONAR") + " </span>\n");
		html.append("      <input id='" + mID + "' type='file' name='files[]' "+(mShowOnlyImages ? "accept='image/*'" : "")+">\n");
		html.append("    </span>\n");

		// if (mShowStartUpload) {
		// html.append(" <button type='reset' class='btn green-seagreen start'>\n");
		// html.append(" <i class='fa fa-upload'></i>\n");
		// html.append(" <span> " + mUser.getTermoByCode("INICIARENVIO") + " </span>\n");
		// html.append(" </button>\n");
		// }

		html.append("    <button type='reset' class='btn red-thunderbird cancel'>\n");
		html.append("      <i class='fa fa-ban'></i>\n");
		html.append("      <span> " + mUser.getTermo("CANCELARTUDO") + " </span>\n");
		html.append("    </button>\n");

		// THE GLOBAL FILE PROCESSING STATE
		html.append("    <span class='fileupload-process'></span>\n");
		html.append("  </div>\n");
		// THE GLOBAL PROGRESS INFORMATION
		html.append("  <div class='col-md-6 fileupload-progress fade'>\n");

		// THE GLOBAL PROGRESS BAR
		html.append("    <div class='progress progress-striped active' role='progressbar' aria-valuemin='0' aria-valuemax='100'>\n");
		html.append("      <div class='progress-bar progress-bar-info' style='width:0%;'></div>\n");
		html.append("    </div>\n");

		// THE EXTENDED GLOBAL PROGRESS INFORMATION
		html.append("    <div class='progress-extended'>&nbsp;</div>\n");
		html.append("  </div>\n");
		html.append("</div>\n");

		// THE TABLE LISTING THE FILES AVAILABLE FOR UPLOAD/DOWNLOAD
		html.append("<table role='presentation' class='table table-striped clearfix'>\n");
		html.append("  <tbody class='files'></tbody>\n");
		html.append("</table>\n");

		html.append("<div id='blueimp-gallery' class='blueimp-gallery blueimp-gallery-controls' data-filter=':even'>\n");
		html.append("  <div class='slides'></div>\n");
		html.append("  <h3 class='title'></h3>\n");
		html.append("  <a class='prev'>‹</a>\n");
		html.append("  <a class='next'>›</a>\n");
		html.append("  <a class='close'>×</a>\n");
		html.append("  <a class='play-pause'></a>\n");
		html.append("  <ol class='indicator'></ol>\n");
		html.append("</div>\n");

		// THE TEMPLATE TO DISPLAY FILES AVAILABLE FOR UPLOAD
		html.append("<script type='text/x-tmpl' id='template-upload'>\n");
		html.append("  {% for (var i=0, file; file=o.files[i]; i++) { %}\n");
		html.append("       <tr class='template-upload fade'>\n");
		html.append("         <td class='text-center'>\n");
		html.append("           <span class='preview'></span>\n");
		html.append("         </td>\n");
		html.append("         <td class='text-left'>\n");
		html.append("           <p class='name'>{%=file.name%}</p>\n");
		if (mShowStartUpload) {
			html.append("           <strong class='error text-danger label label-danger'></strong>\n");
		}
		html.append("         </td>\n");
		if (mShowFileText) {
			html.append("         <td class='text-left'>\n");
			html.append("           <textarea class='form-control col-md-12' rows='3' name='_field_descr_" + mID + "_{%=file._isg_id%}' maxlength='" + mFileTextLength + "'></textarea>\n");
			html.append("         </td>\n");
		}
		html.append("         <td class='text-right'>\n");
		html.append("           <p class='size'>" + mUser.getTermo("CARREGANDO") + "</p>\n");
		if (mShowStartUpload) {
			html.append("           <div class='progress progress-striped active' role='progressbar' aria-valuemin='0' aria-valuemax='100' aria-valuenow='0'>\n");
			html.append("             <div class='progress-bar progress-bar-info' style='width:0%;'></div>\n");
			html.append("           </div>\n");
		}
		html.append("         </td>\n");
		html.append("         <td class='text-center'>\n");
		// if (mShowStartUpload) {
		// html.append(" {% if (!i && !o.options.autoUpload) { %}\n");
		// html.append(" <button class='btn green-seagreen start' disabled>\n");
		// html.append(" <i class='fa fa-upload'></i>\n");
		// html.append(" <span> " + mUser.getTermoByCode("INICIARENVIO") + " </span>\n");
		// html.append(" </button>\n");
		// html.append(" {% } %}\n");
		// }
		html.append("       {% if (!i) { %}\n");
		html.append("            <button class='btn red-thunderbird cancel'>\n");
		html.append("              <i class='fa fa-ban'></i>\n");
		html.append("              <span>" + mUser.getTermo("CANCELAR") + "</span>\n");
		html.append("            </button>\n");
		html.append("       {% } %}\n");
		html.append("         </td>\n");
		html.append("       </tr>\n");
		html.append("  {% } %}\n");
		html.append("</script>\n");

		// THE TEMPLATE TO DISPLAY FILES AVAILABLE FOR DOWNLOAD
		html.append("<script type='text/x-tmpl' id='template-download'>\n");
		html.append("  {% for (var i=0, file; file=o.files[i]; i++) { %}\n");
		html.append("       <tr class='template-download fade'>\n");
		html.append("         <td>\n");
		html.append("           <span class='preview'>\n");
		html.append("           {% if (file.thumbnailUrl) { %}\n");
		html.append("                <a href='{%=file.url%}' title='{%=file.name%}' download='{%=file.name%}' data-gallery><img src='{%=file.thumbnailUrl%}'></a>\n");
		html.append("           {% } %}\n");
		html.append("           </span>\n");
		html.append("         </td>\n");
		html.append("         <td>\n");
		html.append("           <p class='name'>\n");
		html.append("           {% if (file.url) { %}\n");
		html.append("                <a href='{%=file.url%}' title='{%=file.name%}' download='{%=file.name%}' {%=file.thumbnailUrl?'data-gallery':''%}>{%=file.name%}</a>\n");
		html.append("           {% } else { %}\n");
		html.append("                <span>{%=file.name%}</span>\n");
		html.append("           {% } %}\n");
		html.append("           </p>\n");
		html.append("           {% if (file.error) { %}\n");
		html.append("             <div><span class='label label-danger'>" + mUser.getTermo("ERRO") + "</span> {%=file.error%}</div>\n");
		html.append("           {% } %}\n");
		html.append("         </td>\n");
		if (mShowFileText) {
			html.append("         <td class='text-left'>\n");
			html.append("           <textarea class='form-control col-md-12' rows='3' name='_field_descr_" + mID + "_{%=file._isg_id%}' maxlength='" + mFileTextLength + "' {% if (file._isg_disabled) { %} readonly {% } %} onblur='try { _onblur_" + mID + "(this, {%=file._isg_id%}); } catch(e) {}'>" + ( mFileTextValue.length() > 0 ? "{%=file." + mFileTextValue + "%}" : "" ) + "</textarea>\n");
			html.append("         </td>\n");
		}
		for (String str : extraColumns) {
			html.append("         <td>\n");
			html.append("           <span class='size'>{%=file." + str + "%}</span>\n");
			html.append("         </td>\n");
		}
		html.append("         <td class='text-right'>\n");
		html.append("           <span class='size'>{%=o.formatFileSize(file.size)%}</span>\n");
		html.append("         </td>\n");
		html.append("         <td class='text-right'>\n");
		html.append("         {% if (file.deleteUrl) { %}\n");
		html.append("              <button class='btn {% if (file._isg_disabled) { %} default disabled {% } else { %} red-thunderbird {% } %} delete' data-type='{%=file.deleteType%}' data-url='{%=file.deleteUrl%}'{% if (file.deleteWithCredentials) { %} data-xhr-fields='{'withCredentials':true}'{% } %}>\n");
		html.append("                <i class='glyphicon glyphicon-trash'></i>\n");
		html.append("                <span>" + mUser.getTermo("REMOVER") + "</span>\n");
		html.append("              </button>\n");
		html.append("         {% } else { %}\n");
		html.append("              <button class='btn btn-warning cancel'>\n");
		html.append("                <i class='glyphicon glyphicon-ban-circle'></i>\n");
		html.append("                <span>" + mUser.getTermo("CANCELAR") + "</span>\n");
		html.append("              </button>\n");
		html.append("         {% } %}\n");
		html.append("         </td>\n");
		html.append("       </tr>\n");
		html.append("  {% } %}\n");
		html.append("</script>\n");

		return html.toString();
	}

	public String printJavaScript() {
		StringBuffer html = new StringBuffer();

		long fileSize = Database.verifyNullLong(IniManipulation.getProperty(IniManipulation.FILE_SIZE));

		html.append("  jQuery(document).ready(function() { \n");
		html.append("    $('#" + mID + "').fileupload({\n");
		html.append("      autoUpload: " + mShowStartUpload + ",\n");
		html.append("      disableImageResize: /Android(?!.*Chrome)|Opera/.test(window.navigator.userAgent),\n");
		html.append("      formAcceptCharset: '" + mUser.getUserCharset() + "',\n");
		if (fileSize > 0) {
			html.append("      maxFileSize: " + ( fileSize * 1024 ) + ",\n");
		}
		if (mShowOnlyImages) {
			html.append("      acceptFileTypes: /(\\.|\\/)(gif|jpe?g|png)$/i,\n");
		}
		if (mShowStartUpload) {
			html.append("      url: '" + mURL + "',\n");
			html.append("      method: 'POST',\n");
			html.append("      dataType: 'json',\n");
		}
		html.append("      maxNumberOfFiles: 25 \n");
		html.append("    });\n");
		html.append("    $.ajax({\n");
		html.append("      cache: false,\n");
		html.append("      url: $('#" + mID + "').fileupload('option', 'url') + '&loadFiles=S',\n");
		html.append("      dataType: 'json',\n");
		html.append("      context: $('#" + mID + "')[0]\n");
		html.append("    }).always(function () {\n");
		html.append("      $(this).removeClass('fileupload-processing');\n");
		html.append("    }).done(function (result) {\n");
		html.append("      $(this).fileupload('option', 'done').call(this, $.Event('done'), { result: result });\n");
		html.append("    });\n");
		if (mDisableFileUpload) {
			html.append("    $('#" + mID + "').fileupload('disable'); \n");
		}
		html.append("  });\n");

		html.append("  $_count_file_upload_" + mID + " = 0; \n");
		html.append("  $('#" + mID + "').bind('fileuploadadd', function (e, data) {\n");
		html.append("    data._isg_id = '_file_upload_' + $_count_file_upload_" + mID + "; \n");
		html.append("    data.fileInput[0].id = data._isg_id;\n");
		html.append("    data.fileInput[0].name = data._isg_id;\n");
		html.append("    data.fileInput[0].style.display = 'none';\n");
		html.append("    $('#" + mID + "').append( data.fileInput[0] );\n");
		html.append("    $_count_file_upload_" + mID + "++;\n");
		html.append("  });\n");
		html.append("  $('#" + mID + "').bind('fileuploadfail', function (e, data) {\n");
		html.append("    $('#' + data._isg_id).remove();\n");
		html.append("  });\n");

		return html.toString();
	}

	public static String printStyles() {
		StringBuffer style = new StringBuffer();

		style.append("<link rel='stylesheet' href='metronic/global/plugins/jquery-file-upload/blueimp-gallery/blueimp-gallery.min.css' />\n");
		style.append("<link rel='stylesheet' href='metronic/global/plugins/jquery-file-upload/css/jquery.fileupload.css' />\n");
		style.append("<link rel='stylesheet' href='metronic/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css' />\n");

		return style.toString();
	}

	public static String printImports() {
		StringBuffer js = new StringBuffer();

		js.append("<script src='metronic/global/plugins/fancybox/source/jquery.fancybox.pack.js'></script>\n");

		// THE JQUERY UI WIDGET FACTORY, CAN BE OMITTED IF JQUERY UI IS ALREADY INCLUDED
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.js'></script>\n");

		// THE TEMPLATES PLUGIN IS INCLUDED TO RENDER THE UPLOAD/DOWNLOAD LISTINGS
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/vendor/tmpl.min.js'></script>\n");

		// THE LOAD IMAGE PLUGIN IS INCLUDED FOR THE PREVIEW IMAGES AND IMAGE RESIZING FUNCTIONALITY
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/vendor/load-image.min.js'></script>\n");

		// THE CANVAS TO BLOB PLUGIN IS INCLUDED FOR IMAGE RESIZING FUNCTIONALITY
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/vendor/canvas-to-blob.min.js'></script>\n");

		// BLUEIMP GALLERY SCRIPT
		js.append("<script src='metronic/global/plugins/jquery-file-upload/blueimp-gallery/jquery.blueimp-gallery.min.js'></script>\n");

		// THE Iframe Transport IS REQUIRED FOR BROWSERS WITHOUT SUPPORT FOR XHR FILE UPLOADS
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/jquery.iframe-transport.js'></script>\n");

		// THE BASIC FILE UPLOAD PLUGIN
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/jquery.fileupload.js'></script>\n");

		// THE FILE UPLOAD PROCESSING PLUGIN
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js'></script>\n");

		// THE FILE UPLOAD IMAGE PREVIEW & RESIZE PLUGIN
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/jquery.fileupload-image.js'></script>\n");

		// THE FILE UPLOAD AUDIO PREVIEW PLUGIN
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/jquery.fileupload-audio.js'></script>\n");

		// THE FILE UPLOAD VIDEO PREVIEW PLUGIN
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/jquery.fileupload-video.js'></script>\n");

		// THE FILE UPLOAD VALIDATION PLUGIN
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/jquery.fileupload-validate.js'></script>\n");

		// THE FILE UPLOAD USER INTERFACE PLUGIN
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/jquery.fileupload-ui.js'></script>\n");

		// THE MAIN APPLICATION SCRIPT
		// THE XDomainRequest Transport IS INCLUDED FOR CROSS-DOMAIN FILE DELETION FOR IE 8 AND IE 9
		js.append("<!--[if (gte IE 8)&(lt IE 10)]>\n");
		js.append("<script src='metronic/global/plugins/jquery-file-upload/js/cors/jquery.xdr-transport.js'></script>\n");
		js.append("<![endif]-->\n");

		return js.toString();
	}

}
