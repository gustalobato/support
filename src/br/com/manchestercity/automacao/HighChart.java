
package br.com.manchestercity.automacao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe auxiliar para gerar javascript de gr�ficos Highcharts. Caso seja necess�rio realizar alguma customiza��o n�o implementada, buscar demais par�metros na Documenta��o da API Highcharts: http://api.highcharts.com/highcharts Data de cria��o: 13/09/2013
 */
public class HighChart {

	public static int ROUNDTYPE_CEIL = 0; // Arredonda pra cima
	public static int ROUNDTYPE_FLOOR = 1; // Arredonda pra baixo
	public static int ROUNDTYPE_ROUND = 2; // Se maior ou igual a 1.5 arredonda pra cima se n�o pra baixo

	private User user; // Usu�rio logado no sistema
	private String id; // Identificador da div onde o gr�fico ser� renderizado ou da imagem
	private String divClass; // Classe CSS da div onde o gr�fico ser� renderizado. Caso n�o seja fornecido, recebe a classe padr�o (print.css - defaultDivChart)
	private String title; // T�tulo do Gr�fico
	private int width; // Width nesse chart n�o aceita %, portanto ou voc� seta n�merico sabendo exatamente o valor ou n�o seta. PS: No IE abaixo de 9 � necess�rio setar em alguns casos
	private String subTitle; // Subt�tulo do Gr�fico
	private String xAxisTitle; // T�tulo do eixo X
	private String xAxisLabels; // Labels do eixo X separados por v�rgula
	private boolean xAxisRotation; // True: rotaciona em -45� os labels do eixo X. False (default): labels alinhados ao centro
	private String yAxisTitle; // T�tulo do eixo Y
	private String yAxisUnit; // Unidade de Medida do eixo Y (Ex.: m, %, R$)
	private String yAxisMaxValue; // Maior valor exibido no eixo Y (�ltimo valor da escala). Caso n�o seja fornecido, o Highcharts calcular� automaticamente
	private String yAxisMinValue; // Menor valor exibido no eixo Y (primeiro valor da escala). Caso n�o seja fornecido, o Highcharts calcular� automaticamente
	private String dataLabelPrefix; // Prefixo que ser� exibido no label de cada ponto do gr�fico
	private String dataLabelSufix; // Sufixo que ser� exibido no label de cada ponto do gr�fico
	private List<HighChartSerie> serieList; // Lista contendo as s�ries (CSerieChart) do gr�fico
	private boolean enableMultipleColor; // Aplic�vel quando o gr�fico possui apenas uma s�rie. True: para cada ponto da s�rie gera uma cor diferente (m�ximo de 10 ou repete cor). False (default) usa apenas uma cor
	private boolean enableOnClick; // True: ativa evento de click para os pontos do gr�fico. False (default): n�o permite evento de click para os pontos do gr�fico
	private boolean enableLegend; // True (default): exibe legenda do gr�fico na parte inferior alinhada ao centro. False: n�o exibe legenda do gr�fico.
	private boolean enableTooltip; // False (default): exibe Tooltip.
	private boolean enableDataLabel; // true (default): exibir os valores acumulados.
	private boolean isDecimal; // False (default): Eibir em formato decimal com N casas decimais (se casas n�o definidas, default � 2).
	private int precision; // Quantidade de casas decimais de um valor decimal. Valor default: 2 se isDecimal = true, caso contr�rio 0. Somente utilizado se isDecimal = true.
	private int roundType; // Tipo de arredondamento de um valor decimal. Valor default: ROUNDTYPE_ROUND. Somente utilizado se isDecimal = true.
	private String formId; // Identificador do formul�rio onde um textarea contendo o svg do gr�fico ser� adicionado para futura renderiza��o em JPEG. Default = "_frm" (CReportGeral). Para n�o renderizar o textarea, fornecer ""
	private boolean overlaySeries; // True: Unifica todas as s�ries do tipo COLUMN sobrepondo-as. Recomendado a utiliza��o quando somente uma das s�ries tiver valor em cada posi��o. False (default): Cada s�rie do tipo COLUMN � uma coluna.
	private boolean isPieChart; // True: Define que somente uma Serie do tipo PIE vai ser adicionada ao gr�fico. Utilizamos para fazer formata�ao diferente dos valores (Valor : %). False: demais tipos de gr�fico.
	private int legendItemWidth; // Define largura de cada item da legenda (necess�rio setar para IE9 por exemplo)
	private boolean stackingBars; // Define que as colunas ser�o sobrepostas
	private String pieColors; // Define as cores de cada s�rie num gr�fico do tipo Pie
	private String basicColors; // Define as cores b�sicas utilizadas no plot de gr�ficos
	private boolean reversedLegend; // Define se a orderm de exibi��o da legenda deve ser invertida
	private boolean ocultarValorColunaZero; // Oculta o label do valor na coluna caso este seja igual a 0.
	private boolean enableStackDiff; // habilitar e alimentar o stack na classe CSerieChart Utilizado para agraupar colunas sobrepostas. Ex. Uma coluna sobreposta e uma coluna normal.
	private boolean enableExporting; // habilitar o bot�o de exporta��o da Imagem do gr�fico.

	/**
	 * Construtor.
	 * 
	 * @param user
	 *            - Usu�rio logado no sistema.
	 */
	public HighChart(User user) {
		this.user = user;
		this.id = "";
		this.divClass = "";
		this.title = "";
		this.width = 0;
		this.subTitle = "";
		this.xAxisTitle = "";
		this.xAxisLabels = "";
		this.xAxisRotation = false;
		this.yAxisTitle = "";
		this.yAxisUnit = "";
		this.yAxisMaxValue = "";
		this.yAxisMinValue = "";
		this.dataLabelPrefix = "";
		this.dataLabelSufix = "";
		this.serieList = new ArrayList<HighChartSerie>();
		this.setEnableMultipleColor(false);
		this.enableOnClick = false;
		this.enableLegend = true;
		this.enableTooltip = false;
		this.enableDataLabel = true;
		this.isDecimal = false;
		this.precision = 0;
		this.roundType = HighChart.ROUNDTYPE_ROUND;
		this.formId = "_frm";
		this.overlaySeries = false;
		this.isPieChart = false;
		this.legendItemWidth = 0;
		this.stackingBars = false;
		this.pieColors = "";
		this.basicColors = "";
		this.reversedLegend = false;
		this.ocultarValorColunaZero = false;
		this.enableStackDiff = false;
		this.enableExporting = true;
	}

	/**
	 * Limpa os dados do chart.
	 */
	public void cleanChart() {
		this.id = "";
		this.divClass = "";
		this.title = "";
		this.width = 0;
		this.subTitle = "";
		this.xAxisTitle = "";
		this.xAxisLabels = "";
		this.xAxisRotation = false;
		this.yAxisTitle = "";
		this.yAxisUnit = "";
		this.yAxisMaxValue = "";
		this.yAxisMinValue = "";
		this.dataLabelPrefix = "";
		this.dataLabelSufix = "";

		if (this.serieList != null) {
			this.serieList.clear();
		}
		else {
			this.serieList = new ArrayList<HighChartSerie>();
		}

		this.setEnableMultipleColor(false);
		this.enableOnClick = false;
		this.enableLegend = true;
		this.enableTooltip = false;
		this.enableDataLabel = true;
		this.isDecimal = false;
		this.precision = 0;
		this.roundType = HighChart.ROUNDTYPE_ROUND;
		this.formId = "_frm";
		this.overlaySeries = false;
		this.isPieChart = false;
		this.legendItemWidth = 0;
		this.stackingBars = false;
		this.pieColors = "";
		this.basicColors = "";
		this.reversedLegend = false;
	}

	/**
	 * Imports Javascript que devem ser adicionados ao header da p�gina onde o gr�fico ser� renderizado. Utilizando Highcharts v3.0.5 e JQuery 1.7.2 (ficar atento � compatibilidade ao modificar a vers�o de um dos componentes).
	 * 
	 * @return Imports necess�rios para gerar gr�ficos.
	 */
	public static String printImports() {
		return printImports(true);
	}

	public static String printImports(boolean jQuery) {
		StringBuffer out = new StringBuffer();

		if (jQuery) {
			out.append("<script type='text/javascript' src='metronic/global/plugins/jquery-1.11.0.min.js'></script>\n");
		}

		out.append("<script type='text/javascript' src='isg/plugins/highcharts/highcharts.js'></script>\n");
		out.append("<script type='text/javascript' src='isg/plugins/highcharts/modules/exporting.js'></script>\n");
		out.append("<script type='text/javascript' src='isg/plugins/highcharts/highcharts-more.js'></script>\n");
		out.append("<script type='text/javascript' src='isg/plugins/highcharts/jquery.detectmobile.js'></script>\n");
		return out.toString();
	}

	/**
	 * Seta a configura��es globais dos gr�ficos (apenas 1 vez por p�gina).
	 * 
	 * @return Fun��o Javascript com as configura��es globais dos gr�ficos.
	 */
	public static String printGlobalOptions(User user) {
		StringBuffer out = new StringBuffer();
		try {
			out.append("<script>\n");
			out.append("  Highcharts.setOptions({\n");
			out.append("    lang: {\n");
			out.append("      contextButtonTitle: '" + user.getTermo("MENUGRAFICO") + "',\n");
			out.append("      decimalPoint: '" + user.getUserNumberDec() + "',\n");
			out.append("      downloadJPEG: '" + user.getTermo("EXPORTARJPEG") + "',\n");
			out.append("      downloadPDF: '',\n");
			out.append("      downloadPNG: '" + user.getTermo("EXPORTARPNG") + "',\n");
			out.append("      downloadSVG: '',\n");
			out.append("      loading: '" + user.getTermo("CARREGANDO") + "',\n");
			out.append("      printChart: '" + user.getTermo("IMPRIMIR") + "',\n");
			out.append("      thousandsSep: '" + user.getUserNumberMil() + "'\n");
			out.append("    }\n");
			out.append("  });\n");
			out.append("</script>\n");
		}
		catch (Exception ex) {
			Utils.printSystemError("CChart - printGlobalOptions", ex.getMessage());
			out.setLength(0);
		}

		return out.toString();
	}

	/**
	 * Fun��o Javascript para gerar o gr�fico. Tamb�m � gerada a div onde o gr�fico ser� renderizado e um textarea hidden contendo a svg.
	 * 
	 * @return Fun��o Javascript e div para gera��o do gr�fico.
	 */
	public String printChart() {
		StringBuffer out = new StringBuffer();
		HighChartSerie cSerieChart;
		try {
			out.append("<div id='" + this.id + "' class='" + (this.divClass.equals("") ? "defaultDivChart" : this.divClass) + "'></div>\n");

			// Adiciona o textarea que ir� receber a svg do gr�fico se existir um formul�rio
			if (!this.formId.equals("")) {
				out.append("<textarea id='" + this.id + "_svg' name='" + this.id + "_svg' style='display:none;'></textarea>\n");
			}

			out.append("<script>\n");
			out.append("    $('#" + this.id + "').highcharts({\n");
			if (!this.basicColors.equals("")) {
				out.append("      colors: [" + this.basicColors + "],\n");
			}
			out.append("      chart: {\n");
			if (this.width > 0) {
				out.append("        width: " + this.width + ",\n");
			}
			out.append("        zoomType: 'xy' \n");
			out.append("      },\n");
			out.append("      title: {\n");
			out.append("        text: '" + (!this.title.equals("") ? this.title : " ") + "',\n");
			out.append("        style: { color: '#000000' }\n");
			out.append("      },\n");
			if (!this.subTitle.equals("")) {
				out.append("  subtitle: {\n");
				out.append("    text: '" + this.subTitle + "',\n");
				out.append("    style: { color: '#000000' }\n");
				out.append("  },\n");
			}
			if (!this.xAxisLabels.equals("")) { // Caso n�o forne�a labels para o eixo X, o gr�fico deve ser do tipo pie
				out.append("  xAxis: {\n");
				out.append("    categories: [" + this.xAxisLabels + "]\n");
				if (!this.xAxisTitle.equals("")) {
					out.append(",title: {\n");
					out.append("  text: '" + this.xAxisTitle + "',\n");
					out.append("  style: { color: '#666666', fontWeight: 'normal' }\n");
					out.append("}\n");
				}
				if (this.xAxisRotation) {
					out.append(",labels: { rotation: -45, align: 'right' }\n");
				}
				out.append("  },\n");
				out.append("  yAxis: {\n");
				out.append("    labels: {\n");
				out.append("      format: '{value}" + this.yAxisUnit + "',\n");
				out.append("      formatter: function () {\n");
				out.append("        if (this.value % 1 != 0) \n");
				out.append("          return Highcharts.numberFormat(this.value.toFixed(2), 2, '" + user.getUserNumberDec() + "', '" + user.getUserNumberMil() + "');\n");
				out.append("        else\n");
				out.append("          return this.value;\n");
				out.append("      } \n");
				out.append("    },\n");
				out.append("    title: {\n");
				out.append("      text: '" + (!this.yAxisTitle.equals("") ? this.yAxisTitle : " ") + "',\n");
				out.append("      style: { color: '#666666', fontWeight: 'normal' }\n");
				out.append("    }\n");
				if (!this.yAxisMaxValue.equals("")) {
					out.append(",max: " + this.yAxisMaxValue + "\n");
				}
				if (!this.yAxisMinValue.equals("")) {
					out.append(",min: " + this.yAxisMinValue + "\n");
				}
				out.append("  },\n");
			}
			out.append("      tooltip: {\n");
			if (!this.enableTooltip) {
				out.append("        enabled: false\n"); // Tooltip desabilitado por incompatibilidade com vers�es antigas do IE
			}
			else {
				out.append("  valueDecimals: 2 ");
				// out.append(" }");
			}

			out.append("      },\n");
			if (!this.enableLegend) {
				out.append("  legend: { enabled: false },\n");
			}
			else if (this.legendItemWidth > 0) {
				out.append("  legend: { itemWidth: " + this.legendItemWidth + " \n");
				if (this.reversedLegend) {
					out.append("  , reversed: true \n");
				}
				out.append("  },\n");
			}
			else if (this.reversedLegend) {
				out.append("      legend: { reversed: true },\n");
			}
			out.append("      credits: { enabled: false },\n");
			out.append("      exporting: { url: 'exportchart' " + (this.enableExporting ? "" : ", enabled: false") + " },\n"); // Servlet para convers�o do svg do gr�fico para imagem (Highcharts automaticamente seta os par�metros)
			out.append("      navigation: { menuStyle: { width: '150px' } },\n"); // Tamanho do menu (incompatibilidade com vers�es antigas do IE)
			out.append("      plotOptions: {\n");
			if (this.overlaySeries) {
				out.append("        column: {\n");
				out.append("          groupPadding: 0.5,\n");
				out.append("          pointWidth: 40\n");
				out.append("        },\n");
			}

			if (enableStackDiff || ocultarValorColunaZero) {
				out.append("        column: {\n");
				out.append("          stacking: 'normal',\n");
				out.append("          dataLabels: {\n");
				out.append("              enabled:  true, \n");

				out.append("               verticalAlign:'top', \n");
				out.append("               borderRadius: 5, \n");
				out.append("               backgroundColor: 'rgba(0, 0, 0, 0.7)', \n");
				out.append("               borderColor:'#CDCDB4', \n");
				out.append("               borderWidth:1, \n");
				out.append("              color: '#FFFFFF', \n");
				out.append("              Align:'top' \n");
				if (ocultarValorColunaZero) {
					out.append("              ,formatter:  function() {\n");
					out.append("                  if (this.y != 0){\n");
					out.append("              	      return this.y;\n");
					out.append("                  }\n");
					out.append("              }\n");
				}
				out.append("          }\n");
				out.append("        },\n");
			}

			out.append("        series: {\n"); // Op��es que afetam todas as s�ries
			if (this.enableOnClick) {
				out.append("      cursor: 'pointer',\n");
				out.append("      point: {\n");
				out.append("        events: {\n");
				out.append("          click: function() { eval(this.options.functionclick); }\n"); // Executa a fun��o setada em cada ponto da s�rie (functionclick)
				out.append("        }\n");
				out.append("      },\n");
			}
			if (this.stackingBars) {
				out.append("      stacking: 'normal',\n");
			}
			out.append("          dataLabels: {\n");
			if (this.isPieChart) {
				out.append("            formatter:function() {\n");
				if (this.roundType == HighChart.ROUNDTYPE_CEIL) {
					out.append("              return '<b>' + Highcharts.numberFormat(Math.ceil((this.y*Math.pow(10, " + this.precision + "))/Math.pow(10, " + this.precision + "), " + this.precision + ") + '</b> : ' + Highcharts.numberFormat(this.percentage.toFixed(" + this.precision + "), " + this.precision + ") + '%';");
				}
				else if (this.roundType == HighChart.ROUNDTYPE_FLOOR) {
					out.append("              return '<b>' + Highcharts.numberFormat(Math.floor(this.y*Math.pow(10, " + this.precision + "))/Math.pow(10, " + this.precision + "), " + this.precision + ") + '</b> : ' + Highcharts.numberFormat(this.percentage.toFixed(" + this.precision + "), " + this.precision + ") + '%';");
				}
				else if (this.roundType == HighChart.ROUNDTYPE_ROUND) {
					out.append("              return '<b>' + Highcharts.numberFormat(this.y.toFixed(" + this.precision + "), " + this.precision + ") + '</b> : ' + Highcharts.numberFormat(this.percentage.toFixed(" + this.precision + "), " + this.precision + ") + '%';");
				}
				out.append("},");
			}
			else if (this.overlaySeries || this.isDecimal) {
				out.append("            formatter:function() {\n");
				// Se n�o � decimal, fazemos somente a formata��o para n�o exibir os valores com zero
				if (!this.isDecimal) {
					out.append("              if(this.y != 0) return this.y; \n");
				}
				// Se � decimal, mas n�o � overlay, ent�o fazemos somente a formata��o de decimal
				else if (!this.overlaySeries) {
					if (this.roundType == HighChart.ROUNDTYPE_CEIL) {
						out.append("              return Highcharts.numberFormat(Math.ceil((this.y*Math.pow(10, " + this.precision + "))/Math.pow(10, " + this.precision + "), " + this.precision + ");");
					}
					else if (this.roundType == HighChart.ROUNDTYPE_FLOOR) {
						out.append("              return Highcharts.numberFormat(Math.floor(this.y*Math.pow(10, " + this.precision + "))/Math.pow(10, " + this.precision + "), " + this.precision + ");");
					}
					else if (this.roundType == HighChart.ROUNDTYPE_ROUND) {
						if (ocultarValorColunaZero) {
							out.append(" var lValue = this.y; \n");
							out.append(" if(lValue != 0){\n");
							out.append(" 	return Highcharts.numberFormat(this.y.toFixed(" + this.precision + "), " + this.precision + "); \n");
							out.append(" }\n");
						}
						else {
							out.append("              return Highcharts.numberFormat(this.y.toFixed(" + this.precision + "), " + this.precision + ");");
						}
					}
				}
				// Aqui � decimal e overlay, ent�o formatamos para n�o exibir os zeros e tamb�m as casas decimais
				else {
					if (this.roundType == HighChart.ROUNDTYPE_CEIL) {
						out.append("              if(this.y != 0) return Highcharts.numberFormat(Math.ceil(this.y).toFixed(" + this.precision + "), " + this.precision + ", '" + user.getUserNumberDec() + "', '" + user.getUserNumberMil() + "');");
					}
					else if (this.roundType == HighChart.ROUNDTYPE_FLOOR) {
						out.append("              if(this.y != 0) return Highcharts.numberFormat(Math.floor(this.y).toFixed(" + this.precision + "), " + this.precision + ", '" + user.getUserNumberDec() + "', '" + user.getUserNumberMil() + "');");
					}
					else if (this.roundType == HighChart.ROUNDTYPE_ROUND) {
						out.append("              if(this.y != 0) return Highcharts.numberFormat(this.y.toFixed(" + this.precision + "), " + this.precision + ", '" + user.getUserNumberDec() + "', '" + user.getUserNumberMil() + "');");
					}
				}

				out.append("},");
			}
			out.append("            enabled: true,\n"); // Exibe os valores de cada ponto do gr�fico
			// altera a cor da legenda para branco, porque vai ficar dentro da barra.
			if (!enableStackDiff && !ocultarValorColunaZero) {
				out.append("            color: '#000000',\n");
			}
			out.append("            crop: false\n");
			if (!this.dataLabelPrefix.equals("") || !this.dataLabelSufix.equals("")) { // Seta o prefixo e sufixo do label. Sobrescreve o valor padr�o do TYPE_PIE
				out.append("        , format: '" + this.dataLabelPrefix + " {point.y} " + this.dataLabelSufix + "'\n");
			}
			out.append("          }\n");
			out.append("        },\n");
			out.append("        pie: {\n"); // Op��es que afetam apenas as s�ries TYPE_PIE
			out.append("          cursor: 'pointer',\n");
			if (!this.pieColors.equals("")) {
				out.append("          colors: [" + this.pieColors + "],\n");
			}
			out.append("          allowPointSelect: true,\n");
			// Mantive isso s� para n�o dar erro nos relat�rios que j� utilizam a formata��o desse jeito, mas teoricamente n�o deve ser mais usado esse formato.
			if (!this.isPieChart) {
				out.append("          dataLabels: {\n");
				out.append("            format: '<b>{point.y}</b> : {point.percentage:.1f}%'\n");
				out.append("          },\n");
			}
			out.append("          showInLegend: true\n");
			out.append("        }\n");
			out.append("      },\n"); // fim plotOptions
			out.append("      series: [\n");
			for (int position = 0; position < this.serieList.size(); position++) {
				cSerieChart = this.serieList.get(position);
				out.append("    {\n");
				if (!cSerieChart.getName().equals("")) {
					out.append("  name: '" + cSerieChart.getName() + "',\n");
				}
				if (!cSerieChart.getColor().equals("")) {
					out.append("  color: '" + cSerieChart.getColor() + "',\n");
				}
				out.append("      type: '" + cSerieChart.getType() + "',\n");

				if (cSerieChart.getFunctionOnClickList() == null || cSerieChart.getFunctionOnClickList().size() == 0) {
					out.append("  data: [" + (!cSerieChart.getType().equals(HighChartSerie.TYPE_PIE) ? cSerieChart.getData(false, this.enableMultipleColor) : cSerieChart.getPieData()) + "],\n");
				}
				else {
					out.append("  data: [" + (!cSerieChart.getType().equals(HighChartSerie.TYPE_PIE) ? cSerieChart.getData(this.enableOnClick, this.enableMultipleColor) : cSerieChart.getPieData(this.enableOnClick)) + "],\n");
				}

				if (!cSerieChart.getStack().equals("") && enableStackDiff) {
					out.append("  stack: " + cSerieChart.getStack() + ",\n");
				}

				// n�o utilizar favor usar o showDataLabel do cSerieChart conforme ja utilizado acima.
				if (cSerieChart.getType().equals(HighChartSerie.TYPE_SPLINE) && !this.enableDataLabel) {
					out.append(" dataLabels: {");
					out.append("      enabled: false");
					out.append(" }");
				}
				else {
					out.append(" dataLabels:  {enabled: " + (cSerieChart.getShowDataLabels() ? "true" : "false") + "}\n");
				}

				out.append("    }\n");

				if (position + 1 != this.serieList.size()) {
					out.append(", ");
				}
			}
			out.append("      ]\n");
			out.append("    });\n");

			// Verifica se o dispositivo � mobile
			// Caso seja um dispositivo m�vel, o sistema dever� bloquear o zoom, para possibilitar o click em colunas e setores
			out.append("    if (jQuery && jQuery.browser.mobile) {\n");
			out.append("      $('#" + this.id + "').highcharts().options.chart.zoomType = 'none';\n"); // Zoom na dire��o XY (pode ser apenas X ou apenas Y)
			out.append("      var chart = new Highcharts.Chart($('#" + this.id + "').highcharts().options);\n");
			out.append("      chart.redraw();\n");
			out.append("    }\n");

			// Seta o svg do gr�fico no textarea e o adiciona no form
			if (!this.formId.equals("")) {
				out.append("    var textareaSvg = document.getElementById('" + this.id + "_svg');\n");
				out.append("    textareaSvg.value = $('#" + this.id + "').highcharts().getSVG();\n");
				out.append("    document.getElementById('" + this.formId + "').appendChild(textareaSvg);\n");
			}
			out.append("</script>\n");
		}
		catch (Exception ex) {
			Utils.printSystemError("CChart - printChart", ex.getMessage());
			out.setLength(0);
		}

		return out.toString();
	}

	/**
	 * Recupera o gr�fico como imagem. � necess�rio que o gr�fico tenha sido gerado (printChart).
	 * 
	 * @param request
	 *            - Requisi��o atual.
	 * @param cReportGeral
	 *            - Report que est� gerando o gr�fico.
	 * @return - Tag img com a imagem (jpeg) do gr�fico.
	 */
	public String printImageJPEG(HttpServletRequest request, ReportContent cReportGeral) {
		StringBuffer out = new StringBuffer();
		try {
			// Gera um nome de contexto �nico
			String contextNameSvg = UUID.randomUUID().toString();

			// Adiciona o nome de contexto para remo��o quando o PDF for gerado (CReportGeral)
			// N�o foi poss�vel remover no servlet pois o mesmo � chamado v�rias vezes pelo gerador de PDF
			cReportGeral.addChartContextName(contextNameSvg);

			// Adiciona no ServletContext o svg da imagem (para ser recuperado no servlet)
			request.getServletContext().setAttribute(contextNameSvg, request.getParameter(this.id + "_svg"));

			// Recupera a imagem atrav�s do servlet exportchart no formato jpeg
			out.append("<img width='100%' src='" + cReportGeral.getAppURL() + "exportchart?type=image/jpeg&contextNameSvg=" + contextNameSvg + "'>\n");
		}
		catch (Exception ex) {
			Utils.printSystemError("CChart - printImageJPEG", ex.getMessage());
			out.setLength(0);
		}

		return out.toString();
	}

	public User getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            - Usu�rio logado no sistema.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return this.id;
	}

	/**
	 * @param divClass
	 *            - Identificador da div onde o gr�fico ser� renderizado ou da imagem.
	 */
	public void setId(String id) {
		this.id = Database.verifyNull(id);
	}

	public String getDivClass() {
		return this.divClass;
	}

	/**
	 * @param divClass
	 *            - Classe CSS da div onde o gr�fico ser� renderizado. Caso n�o seja fornecido, recebe a classe padr�o (print.css - defaultDivChart)
	 */
	public void setDivClass(String divClass) {
		this.divClass = Database.verifyNull(divClass);
	}

	public String getTitle() {
		return this.title;
	}

	/**
	 * @param width
	 *            - Width nesse chart n�o aceita %, portanto ou voc� seta n�merico sabendo exatamente o valor ou n�o seta. PS: No IE abaixo de 9 � necess�rio setar em alguns casos
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return this.width;
	}

	/**
	 * @param title
	 *            - T�tulo do Gr�fico.
	 */
	public void setTitle(String title) {
		this.title = Database.verifyNull(title);
	}

	public String getSubTitle() {
		return this.subTitle;
	}

	/**
	 * @param subTitle
	 *            - Subt�tulo do Gr�fico
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = Database.verifyNull(subTitle);
	}

	public String getXAxisTitle() {
		return this.xAxisTitle;
	}

	/**
	 * @param xAxisTitle
	 *            - T�tulo do eixo X.
	 */
	public void setXAxisTitle(String xAxisTitle) {
		this.xAxisTitle = Database.verifyNull(xAxisTitle);
	}

	public String getXAxisLabels() {
		return this.xAxisLabels;
	}

	/**
	 * @param xAxisLabels
	 *            - Labels do eixo X separados por v�rgula.
	 */
	public void setXAxisLabels(String xAxisLabels) {
		// Recupera o eixo X adicionando aspas simples a cada elemento
		String lXAxisLabels = Database.verifyNull(xAxisLabels);
		String[] lXAxisLabelsArray = lXAxisLabels.split(",");
		this.xAxisLabels = "";
		for (int position = 0; position < lXAxisLabelsArray.length; position++) {
			this.xAxisLabels += "'" + lXAxisLabelsArray[position] + "'";
			if (position + 1 != lXAxisLabelsArray.length) {
				this.xAxisLabels += ", ";
			}
		}
	}

	public boolean getXAxisRotation() {
		return this.xAxisRotation;
	}

	/**
	 * @param xAxisRotation
	 *            - True: Rotaciona em -45� os labels do eixo X. False: Alinhados ao centro.
	 */
	public void setXAxisRotation(boolean xAxisRotation) {
		this.xAxisRotation = xAxisRotation;
	}

	public String getYAxisTitle() {
		return this.yAxisTitle;
	}

	/**
	 * @param yAxisTitle
	 *            - T�tulo do eixo Y.
	 */
	public void setYAxisTitle(String yAxisTitle) {
		this.yAxisTitle = Database.verifyNull(yAxisTitle);
	}

	public String getYAxisUnit() {
		return this.yAxisUnit;
	}

	/**
	 * @param yAxisUnit
	 *            - Unidade de Medida do eixo Y (Ex.: m, %, R$).
	 */
	public void setYAxisUnit(String yAxisUnit) {
		this.yAxisUnit = Database.verifyNull(yAxisUnit);
	}

	public String getYAxisMaxValue() {
		return this.yAxisMaxValue;
	}

	/**
	 * @param yAxisMaxValue
	 *            - Maior valor exibido no eixo Y (�ltimo valor da escala). Caso n�o seja fornecido, o Highcharts calcular� automaticamente.
	 */
	public void setYAxisMaxValue(String yAxisMaxValue) {
		this.yAxisMaxValue = Database.verifyNull(yAxisMaxValue);
	}

	public String getYAxisMinValue() {
		return this.yAxisMinValue;
	}

	/**
	 * @param yAxisMinValue
	 *            - Menor valor exibido no eixo Y (primeiro valor da escala). Caso n�o seja fornecido, o Highcharts calcular� automaticamente.
	 */
	public void setYAxisMinValue(String yAxisMinValue) {
		this.yAxisMinValue = Database.verifyNull(yAxisMinValue);
	}

	public String getDataLabelPrefix() {
		return this.dataLabelPrefix;
	}

	/**
	 * @param dataLabelPrefix
	 *            - Prefixo que ser� exibido no label de cada ponto do gr�fico.
	 */
	public void setDataLabelPrefix(String dataLabelPrefix) {
		this.dataLabelPrefix = Database.verifyNull(dataLabelPrefix);
	}

	public String getDataLabelSufix() {
		return this.dataLabelSufix;
	}

	/**
	 * @param dataLabelSufix
	 *            - Sufixo que ser� exibido no label de cada ponto do gr�fico
	 */
	public void setDataLabelSufix(String dataLabelSufix) {
		this.dataLabelSufix = Database.verifyNull(dataLabelSufix);
	}

	/**
	 * Adiciona uma s�rie ao gr�fico.
	 * 
	 * @param cSerieChart
	 *            - S�rie que se deseja adicionar ao gr�fico.
	 */
	public void addSerie(HighChartSerie cSerieChart) {
		this.serieList.add(cSerieChart);
	}

	public boolean getEnableOnClick() {
		return this.enableOnClick;
	}

	public boolean getEnableMultipleColor() {
		return this.enableMultipleColor;
	}

	/**
	 * @param enableMultipleColor
	 *            - Aplic�vel quando o gr�fico possui apenas uma s�rie. True: para cada ponto da s�rie gera uma cor diferente (m�ximo de 10 ou repete cor). False (default) usa apenas uma cor.
	 */
	public void setEnableMultipleColor(boolean enableMultipleColor) {
		this.enableMultipleColor = enableMultipleColor;
	}

	/**
	 * @param enableOnClick
	 *            - True: ativa evento de click para os pontos do gr�fico. False (default): n�o permite evento de click para os pontos do gr�fico.
	 */
	public void setEnableOnClick(boolean enableOnClick) {
		this.enableOnClick = enableOnClick;
	}

	public boolean getEnableLegend() {
		return this.enableLegend;
	}

	/**
	 * @param enableLegend
	 *            - True (default): exibe legenda do gr�fico na parte inferior alinhada ao centro. False: n�o exibe legenda do gr�fico.
	 */
	public void setEnableLegend(boolean enableLegend) {
		this.enableLegend = enableLegend;
	}

	public boolean getEnableToolTip() {
		return this.enableTooltip;
	}

	/**
	 * @param enableTooltip
	 *            - FALSE (default): exibe ToolTip.
	 */
	public void setEnableToolTip(boolean enableTooltip) {
		this.enableTooltip = enableTooltip;
	}

	public boolean getEnableDataLabel() {
		return this.enableDataLabel;
	}

	/**
	 * @param EnableDataLabel
	 *            - True (default): exibe os valores acumulados.
	 */
	public void setEnableDataLabel(boolean enableDataLabel) {
		this.enableDataLabel = enableDataLabel;
	}

	public int getRountType() {
		return this.roundType;
	}

	public int getPrecision() {
		return this.precision;
	}

	public boolean getIsDecimal() {
		return this.isDecimal;
	}

	/**
	 * @param IsDecimal
	 *            - Exibir em formato decimal. Valor default FALSE. Valor default de precis�o: 2. Valor default de arredondamento: ROUNDTYPE_ROUND.
	 */
	public void setIsDecimal(boolean isDecimal) {
		this.setIsDecimal(isDecimal, 2);
	}

	/**
	 * @param IsDecimal
	 *            - Exibir em formato decimal. Valor default FALSE.
	 * @param precision
	 *            - Precis�o de casas decimais.
	 */
	public void setIsDecimal(boolean isDecimal, int precision) {
		this.setIsDecimal(isDecimal, precision, HighChart.ROUNDTYPE_ROUND);
	}

	/**
	 * @param IsDecimal
	 *            - Exibir em formato decimal. Valor default FALSE.
	 * @param precision
	 *            - Precis�o de casas decimais.
	 * @param roundType
	 *            - Tipo de arredondamento de um valor decimal.
	 */
	public void setIsDecimal(boolean isDecimal, int precision, int roundType) {
		this.isDecimal = isDecimal;
		this.precision = precision;
		this.roundType = roundType;
	}

	public String getFormId() {
		return this.formId;
	}

	/**
	 * @param formId
	 *            - Identificador do formul�rio onde um textarea contendo o svg do gr�fico ser� adicionado para futura renderiza��o em JPEG. Default = "_frm" (CReportGeral). Para n�o renderizar o textarea, fornecer "".
	 */
	public void setFormId(String formId) {
		this.formId = Database.verifyNull(formId);
	}

	/**
	 * @param overlaySeries
	 *            - True: Unifica todas as s�ries do tipo COLUMN sobrepondo-as. Recomendado a utiliza��o quando somente uma das s�ries tiver valor em cada posi��o. False (default): Cada s�rie do tipo COLUMN � uma coluna.
	 */
	public void setOverlaySeries(boolean overlaySeries) {
		this.overlaySeries = overlaySeries;
	}

	public boolean getOverlaySeries() {
		return this.overlaySeries;
	}

	/**
	 * @param isPieChart
	 *            - True: Define que somente uma Serie do tipo PIE vai ser adicionada ao gr�fico. Utilizamos para fazer formata�ao diferente dos valores (Valor : %). False: Demais tipos de gr�fico.
	 */
	public void setIsPieChart(boolean isPieChart) {
		this.isPieChart = isPieChart;
	}

	public boolean getIsPieChart() {
		return this.isPieChart;
	}

	/**
	 * @param legendItemWidth
	 *            - Define largura de cada item da legenda (necess�rio setar para IE9 por exemplo)
	 */
	public void setLegendItemWidth(int legendItemWidth) {
		this.legendItemWidth = legendItemWidth;
	}

	public int getLegendItemWidth() {
		return this.legendItemWidth;
	}

	/**
	 * @param stackingBars
	 *            - Define que as colunas ser�o sobrepostas
	 */
	public void setStackingBars(boolean stackingBars) {
		this.stackingBars = stackingBars;
	}

	public boolean getStackingBars() {
		return this.stackingBars;
	}

	/**
	 * @param pieColors
	 *            - Array de cores que ser�o utilizadas no gr�fico do tipo Pie. Exemplo: '#5B9BD5','#009966','#ED7D31','#A5A5A5'.
	 */
	public void setPieColors(String pieColors) {
		this.pieColors = pieColors;
	}

	public String getPieColors() {
		return this.pieColors;
	}

	/**
	 * @param basicColors
	 *            - Array de cores b�sicas que ser�o utilizadas no plot de gr�ficos. Exemplo: '#5B9BD5','#009966','#ED7D31','#A5A5A5'.
	 */
	public void setBasicColors(String basicColors) {
		this.basicColors = basicColors;
	}

	public String getBasicColors() {
		return this.basicColors;
	}

	/**
	 * @param reversedLegend
	 *            - Define se a orderm de exibi��o da legenda deve ser invertida
	 */
	public void setReversedLegend(boolean reversedLegend) {
		this.reversedLegend = reversedLegend;
	}

	public boolean getReversedLegend() {
		return this.reversedLegend;
	}

	/**
	 * @param ocultarValorColunaZero
	 *            - Oculta o label do valor na coluna caso este seja igual a 0.
	 */
	public void setOcultarValorColunaZero(boolean ocultarValorColunaZero) {
		this.ocultarValorColunaZero = ocultarValorColunaZero;
	}

	public boolean getOcultarValorColunaZero() {
		return this.ocultarValorColunaZero;
	}

	/**
	 * @param habilitar
	 *            e alimentar o stack na classe CSerieChart Utilizado para agraupar colunas sobrepostas. Ex. Uma coluna sobreposta e uma coluna normal.
	 */
	public void setEnableStackDiff(boolean enableStackDiff) {
		this.enableStackDiff = enableStackDiff;
	}

	public boolean getEnableStackDiff() {
		return this.enableStackDiff;
	}

	/**
	 * @param habilitar
	 *            o bot�o de exporta��o da Imagem do gr�fico.
	 */
	public void setEnableExporting(boolean enableExporting) {
		this.enableExporting = enableExporting;
	}

	public boolean getEnableExporting() {
		return this.enableExporting;
	}
}
