
package br.com.manchestercity.automacao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import javazoom.upload.MultipartFormDataRequest;

public class TitleMenu {

	private String mDataFavorito;
	private String mJSNovo = "";
	private String mJSSalvar = "";
	private String mJSVoltar = "";
	private String mJSExcluir = "";
	private String mJSLocalizar = "";
	private String mJSFavorito = "";
	private String mJSImprimir = "";
	private String mJSExportar = "";
	private String mLabelNovo = "";
	private String mLabelSalvar = "";
	private String mLabelFavorito = "";
	private String mLabelVoltar = "";
	private String mLabelExcluir = "";
	private String mLabelLocalizar = "";
	private String mLabelImprimir = "";
	private String mLabelExportar = "";
	private String mIDNovo = "";
	private String mIDSalvar = "";
	private String mIDFavorito = "";
	private String mIDVoltar = "";
	private String mIDExcluir = "";
	private String mIDLocalizar = "";
	private String mIDImprimir = "";
	private String mIDExportar = "";

	private boolean mShowSalvar = true;
	private boolean mShowNovo = true;
	private boolean mShowVoltar = true;
	private boolean mShowExcluir = true;
	private boolean mShowExportar = false;
	private boolean mReadOnly = false;
	private boolean mShowLocalizar = true;
	private boolean mShowImprimir = true;
	private boolean mShowFavorito = true;
	private boolean mConfirmSalvar = false;
	private boolean mConfirmNovo = false;
	private boolean mConfirmVoltar = false;
	private boolean mConfirmExcluir = false;
	private boolean mConfirmLocalizar = false;
	private boolean mConfirmFavorito = false;
	private boolean mConfirmImprimir = false;
	private boolean mConfirmExportar = false;

	private boolean mContentFullWidth = true;

	private boolean mIsMultipart = false;

	private User mUser = null;
	private Database lcdb = null;
	private ArrayList<TitleButton> mExtraButtons;

	private DataControl mPageControls[];

	private String mTitle = "";
	private HttpServletRequest mRequest = null;

	private void init() {
		mIDNovo = "_button_menu_novo";
		mIDSalvar = "_button_menu_salvar";
		mIDFavorito = "_button_menu_favorito";
		mIDVoltar = "_button_menu_voltar";
		mIDExcluir = "_button_menu_excluir";
		mIDLocalizar = "_button_menu_localizar";
		mIDImprimir = "_button_menu_imprimir";
		mIDExportar = "_button_menu_exportar";

		mJSNovo = "facao('ins');";
		mJSSalvar = "facao('salvar');";
		mJSVoltar = "voltar();";
		mJSExcluir = "facao('delete');";
		mJSLocalizar = "";
		mJSImprimir = "";
		mJSFavorito = "doFavorito();";
		mJSExportar = "printVersion('cdisplayexcel', '_iframe_excel');";
		mDataFavorito = "";
		mShowFavorito = true;
		mRequest = null;

		if (mUser != null) {
			mLabelNovo = mUser.getTermo("NOVO");
			mLabelSalvar = mUser.getTermo("SALVAR");
			mLabelVoltar = mUser.getTermo("VOLTAR");
			mLabelExcluir = mUser.getTermo("EXCLUIR");
			mLabelLocalizar = mUser.getTermo("LOCALIZAR");
			mLabelImprimir = "";
			mLabelFavorito = "";
			mLabelExportar = mUser.getTermo("EXPORTAREXCEL");
		}
		else {
			mLabelNovo = "Novo";
			mLabelSalvar = "Salvar";
			mLabelVoltar = "Voltar";
			mLabelExcluir = "Excluir";
			mLabelLocalizar = "Localizar";
			mLabelImprimir = "";
			mLabelFavorito = "";
			mLabelExportar = "Exportar Excel";
		}

		mShowSalvar = true;
		mShowNovo = true;
		mShowVoltar = true;
		mShowExcluir = true;
		mShowExportar = false;
		mReadOnly = false;
		mShowLocalizar = true;
		mShowImprimir = true;

		mConfirmSalvar = false;
		mConfirmNovo = false;
		mConfirmVoltar = false;
		mConfirmExcluir = false;
		mConfirmLocalizar = false;
		mConfirmImprimir = false;
		mConfirmExportar = false;

		mTitle = "";
		mExtraButtons = new ArrayList<TitleButton>();
	}

	public String getDataFavorito() {
		return mDataFavorito;
	}

	public void addExtraButton(String pLabel, String pJSAction, String pIcon) {
		addExtraButton(pLabel, pJSAction, pIcon, false);
	}

	public void setCcadGeralControls(DataControl[] pValue) {
		mPageControls = pValue;
	}

	public void setShowFavorito(boolean pValue, HttpServletRequest request) {
		mShowFavorito = pValue;
		mRequest = request;
	}

	public boolean getShowFavorito() {
		return mShowFavorito;
	}

	@SuppressWarnings( "unchecked" )
	public void setInitFavorito(HttpServletRequest request) {
		mIsMultipart = MultipartFormDataRequest.isMultipartFormData(mRequest);
		mDataFavorito = "<span id='_span_favorito' style='display:none'> \n";

		Enumeration<String> parameters;
		String key = "";
		boolean onlyPrimaries = false;
		boolean addFavorito;

		if (mIsMultipart) {
			MultipartFormDataRequest upRequest = null;
			try {
				upRequest = new MultipartFormDataRequest(mRequest);
			}
			catch (Exception e) {
				upRequest = null;
				Utils.printSystemError("MultipartFormDataRequest", e.getMessage());
			}

			if (upRequest != null) {
				parameters = upRequest.getParameterNames();
				while (parameters.hasMoreElements()) {
					key = parameters.nextElement().toString();
					addFavorito = true;
					if (onlyPrimaries) {
						for (int y = 0; y < mPageControls.length; y++) {
							if (mPageControls[y] == null)
								break;
							if (mPageControls[y].getControl().equals(key)) {
								addFavorito = false;
								break;
							}
						}
					}

					if (addFavorito) {
						String[] lValues = upRequest.getParameterValues(key);
						for (int i = 0; i < lValues.length; i++) {
							mDataFavorito += "<TXT_FAV name='" + key + "'>" + filtraAcao(key, lValues[i]) + "</TXT_FAV>\n";
						}
					}
				}
			}
		}
		else {
			parameters = request.getParameterNames();
			while (parameters.hasMoreElements()) {
				key = parameters.nextElement().toString();

				addFavorito = true;
				if (onlyPrimaries) {
					for (int y = 0; y < mPageControls.length; y++) {
						if (mPageControls[y] == null)
							break;
						if (mPageControls[y].getControl().equals(key)) {
							addFavorito = false;
							break;
						}
					}
				}

				if (addFavorito) {
					String[] lValues = request.getParameterValues(key);
					for (int i = 0; i < lValues.length; i++) {
						mDataFavorito += "<TXT_FAV name='" + key + "'>" + filtraAcao(key, lValues[i]) + "</TXT_FAV>\n";
					}
				}
			}
		}
		mDataFavorito += "</span>";

		mDataFavorito += "<script> \n";
		mDataFavorito += "  function doFavorito(){ \n";
		mDataFavorito += "    var lTagForm = document.createElement('FORM'); \n";
		mDataFavorito += "    var lTagItem = document.createElement('TEXTAREA'); \n";
		mDataFavorito += "    lTagItem.value = document.getElementById('_span_favorito').innerHTML; \n";
		mDataFavorito += "    lTagItem.name  = 'txtDS_FVRTS_POST'; \n";
		mDataFavorito += "    lTagForm.appendChild(lTagItem); \n";

		mDataFavorito += "    lTagItem = document.createElement('TEXTAREA'); \n";
		mDataFavorito += "    lTagItem.value = '" + request.getRequestURI().replaceAll("gustavo6_", "") + "'; \n";
		mDataFavorito += "    lTagItem.name  = 'txtDS_FVRTS_URL'; \n";
		mDataFavorito += "    lTagForm.appendChild(lTagItem); \n";

		mDataFavorito += "    lTagItem = document.createElement('INPUT'); \n";
		mDataFavorito += "    lTagItem.value = '" + request.getMethod() + "'; \n";
		mDataFavorito += "    lTagItem.name  = 'txtDS_FVRTS_METHD'; \n";
		mDataFavorito += "    lTagForm.appendChild(lTagItem); \n";

		mDataFavorito += "    lTagItem = document.createElement('INPUT'); \n";
		mDataFavorito += "    lTagItem.value = '" + ( mIsMultipart ? "multipart/form-data" : "" ) + "'; \n";
		mDataFavorito += "    lTagItem.name  = 'txtDS_FVRTS_TYPE'; \n";
		mDataFavorito += "    lTagForm.appendChild(lTagItem); \n";

		mDataFavorito += "    lTagItem = document.createElement('INPUT'); \n";
		mDataFavorito += "    lTagItem.value = '" + mTitle.replaceAll("'", "\"") + "'; \n";
		mDataFavorito += "    lTagItem.name  = 'txtNM_FVRTS'; \n";
		mDataFavorito += "    lTagForm.appendChild(lTagItem); \n";

		mDataFavorito += "    lTagItem = document.createElement('INPUT'); \n";
		mDataFavorito += "    lTagItem.value = 'insFavorite'; \n";
		mDataFavorito += "    lTagItem.name  = 'acao'; \n";
		mDataFavorito += "    lTagForm.appendChild(lTagItem); \n";

		mDataFavorito += "    lTagForm.style.display = 'none'; \n";

		mDataFavorito += "    var carregou = function(e) { \n";
		mDataFavorito += "      var iframeFav = document.getElementById('_iframe_favorito');\n";
		mDataFavorito += "      if ( iframeFav.detachEvent ) { \n";
		mDataFavorito += "        iframeFav.detachEvent( 'onload', this ); \n";
		mDataFavorito += "      } \n";
		mDataFavorito += "      if ( iframeFav.removeEventListener ) { \n";
		mDataFavorito += "        iframeFav.removeEventListener( 'load', this, false ); \n";
		mDataFavorito += "      } \n";
		mDataFavorito += "      ajaxFavorito( (iframeFav.contentWindow || iframeFav.contentDocument).document.body.innerHTML ); \n";
		mDataFavorito += "      e.stopPropagation();\n";
		mDataFavorito += "      e.preventDefault();\n";
		mDataFavorito += "    }; \n";

		mDataFavorito += "    if ( document.getElementById('_iframe_favorito').addEventListener ) { \n";
		mDataFavorito += "      document.getElementById('_iframe_favorito').addEventListener( 'load', carregou, true ); \n";
		mDataFavorito += "    } \n";
		mDataFavorito += "    else if ( document.getElementById('_iframe_favorito').attachEvent ) { \n";
		mDataFavorito += "      document.getElementById('_iframe_favorito').attachEvent( 'onload', carregou ); \n";
		mDataFavorito += "    } \n";

		mDataFavorito += "    lTagForm.setAttribute('id', '_form_favorito'); \n";
		mDataFavorito += "    lTagForm.setAttribute('target', '_iframe_favorito'); \n";
		mDataFavorito += "    lTagForm.setAttribute('action', 'ajax'); \n";
		mDataFavorito += "    lTagForm.setAttribute('method', 'POST'); \n";
		// mDataFavorito += " lTagForm.setAttribute('accept-charset', '" + mUser.getCharsetResponse() + "'); \n"; <input name='txtCD_FNCGC' type="hidden"
		mDataFavorito += "    document.body.appendChild(lTagForm); \n";
		mDataFavorito += "    lTagForm.submit(); \n";
		mDataFavorito += "    document.body.removeChild(lTagForm); \n";
		mDataFavorito += "  } \n";

		mDataFavorito += "  function ajaxFavorito(modalContent) { \n";
		mDataFavorito += "    if (modalContent) { \n";

		mDataFavorito += "      var html = '';\n";
		mDataFavorito += "      var js = '';\n";
		mDataFavorito += "      if (modalContent.indexOf('<scr' + 'ipt') >= 0) { \n";
		mDataFavorito += "        var jsAux = modalContent.split('<scr' + 'ipt'); \n";
		mDataFavorito += "        for (var i = 0; i < jsAux.length; i++) {\n";
		mDataFavorito += "          if (jsAux[i].indexOf('</scr' + 'ipt>') != '-1') {\n";
		mDataFavorito += "            scriptAux = jsAux[i].split('</scr' + 'ipt>');\n";
		mDataFavorito += "            js += scriptAux[0].substring(scriptAux[0].indexOf('>') + 1);\n";
		mDataFavorito += "            html += scriptAux[1];\n";
		mDataFavorito += "          }\n";
		mDataFavorito += "          else {\n";
		mDataFavorito += "            html += jsAux[i];\n";
		mDataFavorito += "          }\n";
		mDataFavorito += "        }\n";
		mDataFavorito += "      }\n";
		mDataFavorito += "      else {\n";
		mDataFavorito += "        html = modalContent;\n";
		mDataFavorito += "      }\n";

		mDataFavorito += "      try { \n";
		mDataFavorito += "        $('#_html_modal_favorito', top.document).html( html ); \n";
		mDataFavorito += "        if (js != null && js != '') {\n";
		mDataFavorito += "          top.jQuery.globalEval( js );\n";
		mDataFavorito += "        }\n";
		mDataFavorito += "        $('#_modal_favorito', top.document).modal('show'); \n";
		mDataFavorito += "        $('body', top.document).removeClass('modal-open');";
		mDataFavorito += "      } catch(e) {} \n";
		mDataFavorito += "    } \n";
		mDataFavorito += "  } \n";
		mDataFavorito += "</script> \n";

		mDataFavorito += "<div id='_iframe_layer_favorito' style='display:none; width:500px; height:300px; position:absolute; top:300%; left:30%; background-color:#FFFFFF; border: 1px solid black; z-index:99999999'> \n";
		mDataFavorito += "  <iframe name='_iframe_favorito' id='_iframe_favorito' scrolling='yes' style='width:100%; height:100%;' src='about:blank'></iframe> \n";
		mDataFavorito += "</div>";

		mShowFavorito = true;

	}

	public String filtraAcao(String pName, String pValue) {
		if (pName.equals("acao") && pValue.equals("update")) {
			pValue = "upd";
		}
		else if (pName.equals("acao") && pValue.equals("insert")) {
			pValue = "ins";
		}
		else if (pName.equals("acao") && pValue.equals("delete")) {
			pValue = "ins";
		}

		return pValue;

	}

	public void addExtraButton(String pLabel, String pJSAction, String pIcon, boolean pConfirm) {
		addExtraButton("", pLabel, pJSAction, pIcon, pConfirm);
	}

	public void addExtraButton(String pID, String pLabel, String pJSAction, String pIcon, boolean pConfirm) {
		addExtraButton(pID, pLabel, pJSAction, pIcon, "default", pConfirm);
	}

	public void addExtraButton(String pID, String pLabel, String pJSAction, String pIcon, String pButtonColor, boolean pConfirm) {
		TitleButton lMenuItem = new TitleButton();

		lMenuItem.setID(pID);
		lMenuItem.setConfirm(pConfirm);
		lMenuItem.setIcon(pIcon);
		lMenuItem.setJSAction(pJSAction);
		lMenuItem.setLabel(pLabel);
		lMenuItem.setColor(pButtonColor);

		mExtraButtons.add(lMenuItem);

	}

	public void addModalButton(String pID, String pModalTarget, String pLabel, String pIcon, String pButtonColor) {
		TitleButton lMenuItem = new TitleButton();

		lMenuItem.setID(pID);
		lMenuItem.setConfirm(false);
		lMenuItem.setIcon(pIcon);
		lMenuItem.setLabel(pLabel);
		lMenuItem.setColor(pButtonColor);
		lMenuItem.setModal(true);
		lMenuItem.setModalTarget(pModalTarget);

		mExtraButtons.add(lMenuItem);

	}

	public TitleMenu() {
		this.mUser = null;
		this.init();
	}

	public TitleMenu(User pUser) {
		this.mUser = pUser;
		this.init();
	}

	private String printFunction(String pName, String pFunction, boolean pConfirm) {
		lcdb = new Database(mUser);
		if (pConfirm)
			// return ( !IniManipulation.getProperty("new_layout").equals("") ? "top." : "" ) + "bootbox.confirm('" + lcdb.getUser().getTermo("MSGREALOPERACAO").replaceAll("xxx", pName) + "', function(result) { if (result) {" + pFunction + "} } );";
			return "top.bootbox.confirm('" + lcdb.getUser().getTermo("MSGREALOPERACAO").replaceAll("xxx", pName) + "', function(result) { if (result) {" + pFunction + "} } );";
		else
			return pFunction;
	}

	public void setTitle(String pValue) {
		mTitle = pValue;
	}

	public void setJSImprimir(String pValue) {
		mJSImprimir = pValue;
	}

	public void setJSNovo(String pValue) {
		mJSNovo = pValue;
	}

	public void setJSSalvar(String pValue) {
		mJSSalvar = pValue;
	}

	public void setJSExcluir(String pValue) {
		mJSExcluir = pValue;
	}

	public void setJSVoltar(String pValue) {
		mJSVoltar = pValue;
	}

	public void setJSLocalizar(String pValue) {
		mJSLocalizar = pValue;
	}

	public void setJSExportar(String pValue) {
		mJSExportar = pValue;
	}

	public void setLabelImprimir(String pValue) {
		mLabelImprimir = pValue;
	}

	public void setLabelFavorito(String pValue) {
		mLabelFavorito = pValue;
	}

	public void setLabelExportar(String pValue) {
		mLabelExportar = pValue;
	}

	public void setLabelNovo(String pValue) {
		mLabelNovo = pValue;
	}

	public void setLabelSalvar(String pValue) {
		mLabelSalvar = pValue;
	}

	public void setLabelExcluir(String pValue) {
		mLabelExcluir = pValue;
	}

	public void setLabelVoltar(String pValue) {
		mLabelVoltar = pValue;
	}

	public void setLabelLocalizar(String pValue) {
		mLabelLocalizar = pValue;
	}

	public void setIDImprimir(String pValue) {
		mIDImprimir = pValue;
	}

	public void setIDExportar(String pValue) {
		mIDExportar = pValue;
	}

	public void setIDNovo(String pValue) {
		mIDNovo = pValue;
	}

	public void setIDSalvar(String pValue) {
		mIDSalvar = pValue;
	}

	public void setIDExcluir(String pValue) {
		mIDExcluir = pValue;
	}

	public void setIDVoltar(String pValue) {
		mIDVoltar = pValue;
	}

	public void setIDLocalizar(String pValue) {
		mIDLocalizar = pValue;
	}

	public void setShowAllButtons(boolean pValue) {
		mShowNovo = pValue;
		mShowImprimir = pValue;
		mShowSalvar = pValue;
		mShowExcluir = pValue;
		mShowVoltar = pValue;
		mShowLocalizar = pValue;
		mShowExportar = false;
	}

	public void setShowNovo(boolean pValue) {
		mShowNovo = pValue;
	}

	public void setShowImprimir(boolean pValue) {
		mShowImprimir = pValue;
	}

	public void setShowExportar(boolean pValue) {
		mShowExportar = pValue;
	}

	public void setShowSalvar(boolean pValue) {
		mShowSalvar = pValue;
	}

	public void setShowExcluir(boolean pValue) {
		mShowExcluir = pValue;
	}

	public void setShowVoltar(boolean pValue) {
		mShowVoltar = pValue;
	}

	public void setShowLocalizar(boolean pValue) {
		mShowLocalizar = pValue;
	}

	public void setConfirmNovo(boolean pValue) {
		mConfirmNovo = pValue;
	}

	public void setConfirmImprimir(boolean pValue) {
		mConfirmImprimir = pValue;
	}

	public void setConfirmExportar(boolean pValue) {
		mConfirmExportar = pValue;
	}

	public void setConfirmSalvar(boolean pValue) {
		mConfirmSalvar = pValue;
	}

	public void setConfirmExcluir(boolean pValue) {
		mConfirmExcluir = pValue;
	}

	public void setConfirmVoltar(boolean pValue) {
		mConfirmVoltar = pValue;
	}

	public void setConfirmLocalizar(boolean pValue) {
		mConfirmLocalizar = pValue;
	}

	public void setReadOnly(boolean pValue) {
		mReadOnly = pValue;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getJSImprimir() {
		return mJSImprimir;
	}

	public String getJSNovo() {
		return mJSNovo;
	}

	public String getJSSalvar() {
		return mJSSalvar;
	}

	public String getJSExcluir() {
		return mJSExcluir;
	}

	public String getJSVoltar() {
		return mJSVoltar;
	}

	public String getJSLocalizar() {
		return mJSLocalizar;
	}

	public String getJSExportar() {
		return mJSExportar;
	}

	public String getLabelImprimir() {
		return mLabelImprimir;
	}

	public String getLabelExportar() {
		return mLabelExportar;
	}

	public String getLabelNovo() {
		return mLabelNovo;
	}

	public String getLabelSalvar() {
		return mLabelSalvar;
	}

	public String getLabelExcluir() {
		return mLabelExcluir;
	}

	public String getLabelVoltar() {
		return mLabelVoltar;
	}

	public String getLabelLocalizar() {
		return mLabelLocalizar;
	}

	public String getIDImprimir() {
		return mIDImprimir;
	}

	public String getIDExportar() {
		return mIDExportar;
	}

	public String getIDNovo() {
		return mIDNovo;
	}

	public String getIDSalvar() {
		return mIDSalvar;
	}

	public String getIDExcluir() {
		return mIDExcluir;
	}

	public String getIDVoltar() {
		return mIDVoltar;
	}

	public String getIDLocalizar() {
		return mIDLocalizar;
	}

	public boolean getShowNovo() {
		return mShowNovo;
	}

	public boolean getShowImprimir() {
		return mShowImprimir;
	}

	public boolean getShowSalvar() {
		return mShowSalvar;
	}

	public boolean getShowExcluir() {
		return mShowExcluir;
	}

	public boolean getShowVoltar() {
		return mShowVoltar;
	}

	public boolean getShowLocalizar() {
		return mShowLocalizar;
	}

	public boolean getShowExportar() {
		return mShowExportar;
	}

	public boolean getConfirmNovo() {
		return mConfirmNovo;
	}

	public boolean getConfirmImprimir() {
		return mConfirmImprimir;
	}

	public boolean getConfirmExportar() {
		return mConfirmExportar;
	}

	public boolean getConfirmSalvar() {
		return mConfirmSalvar;
	}

	public boolean getConfirmExcluir() {
		return mConfirmExcluir;
	}

	public boolean getConfirmVoltar() {
		return mConfirmVoltar;
	}

	public boolean getConfirmLocalizar() {
		return mConfirmLocalizar;
	}

	public boolean getReadOnly() {
		return mReadOnly;
	}

	public boolean isContentFullWidth() {
		return mContentFullWidth;
	}

	public void setContentFullWidth(boolean pContentFullWidth) {
		this.mContentFullWidth = pContentFullWidth;
	}

	private String printTitleMenu() {

		if (mShowFavorito && mRequest != null) {
			setInitFavorito(mRequest);
		}

		StringBuffer lHtml = new StringBuffer();

		if (mJSNovo.equals("")) {
			mShowNovo = false;
		}
		if (mJSSalvar.equals("")) {
			mShowSalvar = false;
		}
		if (mJSExcluir.equals("")) {
			mShowExcluir = false;
		}
		if (mJSVoltar.equals("")) {
			mShowVoltar = false;
		}
		if (mJSLocalizar.equals("")) {
			mShowLocalizar = false;
		}
		if (mJSImprimir.equals("")) {
			mShowImprimir = false;
		}
		if (mJSExportar.equals("")) {
			mShowExportar = false;
		}

		lHtml.append("<style media='print'> .title-menu-no-print { display: none; } </style> \n");
		lHtml.append("<style> \n");
		lHtml.append("  @media (min-width: 992px) {\n");
		lHtml.append("    .title-menu-right-margin {\n");
		lHtml.append("      margin-right: " + ( mContentFullWidth ? "0px" : "230px" ) + ";\n");
		lHtml.append("    }\n");
		lHtml.append("    .title-menu-fixed {\n");
		lHtml.append("      position: fixed; \n");
		lHtml.append("    }");
		lHtml.append("  }\n");
		lHtml.append("  @media (max-width: 992px) {\n");
		lHtml.append("    .title-menu-fixed {\n");
		lHtml.append("      position: static; \n");
		lHtml.append("    }");
		lHtml.append("  }\n");
		lHtml.append("</style> \n");
		lHtml.append("<script> \n");
		lHtml.append("  $(document).keydown(keyDownEvent); \n");
		lHtml.append("  $(top.document).keydown(keyDownEvent); \n");

		lHtml.append("  $global_menu_flag_novo      = " + mShowNovo + ";\n");
		lHtml.append("  $global_menu_flag_salvar    = " + mShowSalvar + ";\n");
		lHtml.append("  $global_menu_flag_excluir   = " + mShowExcluir + ";\n");
		lHtml.append("  $global_menu_flag_voltar    = " + mShowVoltar + ";\n");
		lHtml.append("  $global_menu_flag_localizar = " + mShowLocalizar + ";\n");
		lHtml.append("  $global_menu_flag_imprimir  = " + mShowImprimir + ";\n");

		lHtml.append("  function keyDownEvent() { \n");
		lHtml.append("    if (event == null) { \n");
		lHtml.append("      evt = top.event; \n");
		lHtml.append("    }");
		lHtml.append("    else { \n");
		lHtml.append("      evt = event; \n");
		lHtml.append("    }");
		lHtml.append("    if (evt != null) { \n");
		lHtml.append("      if (evt.keyCode >= 112 && evt.keyCode <= 123) { \n");
		lHtml.append("        try { \n");
		if (mShowNovo) {
			lHtml.append("          if (evt.keyCode == 113 && $global_menu_flag_novo) { \n"); // TECLA F2
			lHtml.append("            " + printFunction(mLabelNovo, mJSNovo, mConfirmNovo) + " \n");
			lHtml.append("          } \n");
		}
		if (mShowSalvar) {
			lHtml.append("          if (evt.keyCode == 114 && $global_menu_flag_salvar) { \n"); // TECLA F3
			lHtml.append("            " + printFunction(mLabelSalvar, mJSSalvar, mConfirmSalvar) + " \n");
			lHtml.append("          } \n");
		}
		if (mShowExcluir) {
			lHtml.append("          if (evt.keyCode == 118 && $global_menu_flag_excluir) { \n"); // TECLA F7
			lHtml.append("            " + printFunction(mLabelExcluir, mJSExcluir, mConfirmExcluir) + " \n");
			lHtml.append("          } \n");
		}
		if (mShowVoltar) {
			lHtml.append("          if (evt.keyCode == 119 && $global_menu_flag_voltar) { \n"); // TECLA F8
			lHtml.append("            " + printFunction(mLabelVoltar, mJSVoltar, mConfirmVoltar) + " \n");
			lHtml.append("          } \n");
		}
		if (mShowLocalizar) {
			lHtml.append("          if (evt.keyCode == 122 && $global_menu_flag_localizar) { \n"); // TECLA F11
			lHtml.append("            " + printFunction(mLabelLocalizar, mJSLocalizar, mConfirmLocalizar) + " \n");
			lHtml.append("          } \n");
		}
		if (mShowImprimir) {
			lHtml.append("          if (evt.keyCode == 123 && $global_menu_flag_imprimir) { \n"); // TECLA F12
			lHtml.append("            " + printFunction(mLabelImprimir, mJSImprimir, mConfirmImprimir) + " \n");
			lHtml.append("          } \n");
		}
		lHtml.append("        } catch(e) {} \n");
		lHtml.append("        evt.keyCode = 0; \n");
		lHtml.append("        evt.returnValue = false; \n");
		lHtml.append("      } \n");
		lHtml.append("    } \n");
		lHtml.append("  } \n");
		lHtml.append("  self.focus();");
		lHtml.append("</script> \n");

		// BEGIN PAGE TITLE MENU
		lHtml.append("<div id='_div_title_menu' class='row'>\n");

		// BEGIN PAGE TITLE & BREADCRUMB
		lHtml.append("  <ul class='page-breadcrumb breadcrumb title-menu-fixed title-menu-no-print' style='margin: auto; top: 6px; width: 100%; z-index: 4;'>");
		lHtml.append("    <li>");
		lHtml.append("      <span style='font-weight: bold;'> " + mTitle + " </span>");
		lHtml.append("    </li>");
		lHtml.append("    <li class='btn-group title-menu-right-margin'>");

		for (TitleButton button : mExtraButtons) {
			if (button.getModal()) {
				lHtml.append("      <button type='button' " + ( button.getID().trim().length() > 0 ? " id='" + button.getID() + "'" : "" ) + " class='btn " + button.getColor() + "' data-target='#" + button.getModalTarget() + "' data-toggle='modal'>");
				lHtml.append("        " + button.getIcon() + " " + button.getLabel());
				lHtml.append("      </button>");
			}
			else {
				lHtml.append("      <button type='button' " + ( button.getID().trim().length() > 0 ? " id='" + button.getID() + "'" : "" ) + " class='btn " + button.getColor() + "' " + ( button.getJSAction() != null && button.getJSAction().length() > 0 ? "onclick=\"" + printFunction(button.getLabel(), button.getJSAction(), button.getConfirm()) + "\"" : "" ) + ">");
				lHtml.append("        " + button.getIcon() + " " + button.getLabel());
				lHtml.append("      </button>");
			}
		}

		if (mShowExcluir) {
			lHtml.append("      <button type='button' " + ( mIDExcluir.trim().length() > 0 ? " id='" + mIDExcluir + "'" : "" ) + " class='btn red-thunderbird' onclick=\"" + printFunction(mLabelExcluir, mJSExcluir, mConfirmExcluir) + "\">");
			lHtml.append("        " + SystemIcons.ICON_MINUS_CIRCLE + " " + mLabelExcluir + " (F7) ");
			lHtml.append("      </button>");
		}

		if (mShowNovo) {
			lHtml.append("      <button type='button' " + ( mIDNovo.trim().length() > 0 ? " id='" + mIDNovo + "'" : "" ) + " class='btn green' onclick=\"" + printFunction(mLabelNovo, mJSNovo, mConfirmNovo) + "\">");
			lHtml.append("        " + SystemIcons.ICON_PLUS_CIRCLE + " " + mLabelNovo + " (F2) ");
			lHtml.append("      </button>");
		}

		if (mShowSalvar) {
			lHtml.append("      <button type='button' " + ( mIDSalvar.trim().length() > 0 ? " id='" + mIDSalvar + "'" : "" ) + " class='btn blue-steel' onclick=\"" + printFunction(mLabelSalvar, mJSSalvar, mConfirmSalvar) + "\">");
			lHtml.append("        " + SystemIcons.ICON_SAVE + " " + mLabelSalvar + " (F3) ");
			lHtml.append("      </button>");
		}

		if (mShowVoltar) {
			lHtml.append("      <button type='button' " + ( mIDVoltar.trim().length() > 0 ? " id='" + mIDVoltar + "'" : "" ) + " class='btn grey-silver' onclick=\"" + printFunction(mLabelVoltar, mJSVoltar, mConfirmVoltar) + "\">");
			lHtml.append("        " + SystemIcons.ICON_ARROW_CIRCLE_LEFT + " " + mLabelVoltar + " (F8) ");
			lHtml.append("      </button>");
		}

		if (mShowLocalizar) {
			lHtml.append("      <button type='button' " + ( mIDLocalizar.trim().length() > 0 ? " id='" + mIDLocalizar + "'" : "" ) + " class='btn blue-steel' onclick=\"" + printFunction(mLabelLocalizar, mJSLocalizar, mConfirmLocalizar) + "\"> \n");
			lHtml.append("        " + SystemIcons.ICON_SEARCH + " " + mLabelLocalizar + " (F11) ");
			lHtml.append("      </button>");
		}

		if (mShowImprimir) {
			lHtml.append("      <button type='button' " + ( mIDImprimir.trim().length() > 0 ? " id='" + mIDImprimir + "'" : "" ) + " class='btn blue-steel hidden-sm hidden-xs' onclick=\"" + printFunction(mLabelImprimir, mJSImprimir, mConfirmImprimir) + "\"> \n");
			lHtml.append("        " + SystemIcons.ICON_PRINT + " " + mLabelImprimir + " (F12) ");
			lHtml.append("      </button>");
		}

		if (mShowExportar) {
			lHtml.append("      <button type='button' " + ( mIDExportar.trim().length() > 0 ? " id='" + mIDExportar + "'" : "" ) + " class='btn blue-steel' onclick=\"" + printFunction(mLabelExportar, mJSExportar, mConfirmExportar) + "\"> \n");
			lHtml.append("        " + SystemIcons.ICON_FILE_TEXT_O + " " + mLabelExportar + " ");
			lHtml.append("      </button>");
		}

		if (mShowFavorito && mRequest != null) {
			lHtml.append("      <button type='button' " + ( mIDFavorito.trim().length() > 0 ? " id='" + mIDFavorito + "'" : "" ) + " class='btn yellow-lemon' onclick=\"" + printFunction(mLabelFavorito, mJSFavorito, mConfirmFavorito) + "\"> \n");
			lHtml.append("        " + SystemIcons.ICON_STAR + " " + mLabelFavorito + " ");
			lHtml.append("      </button>");
		}
		lHtml.append("    </li>");
		lHtml.append("  </ul>");
		// END PAGE TITLE & BREADCRUMB

		lHtml.append("</div>");
		if (mShowFavorito && mRequest != null) {
			lHtml.append(getDataFavorito());
		}

		lHtml.append("<div id='_div_space_menu' class='col-md-12' style='margin-top: 40px;'></div>\n");

		// END PAGE TITLE MENU

		return lHtml.toString();
	}

	public void ShowMenu(JspWriter out) {
		try {
			out.println(printTitleMenu());
			out.flush();
		}
		catch (IOException ex) {
		}
	}

	public void ShowMenu(PrintWriter out) {
		out.println(printTitleMenu());
		out.flush();
	}

	public String ShowMenu() {
		return printTitleMenu();
	}
}
