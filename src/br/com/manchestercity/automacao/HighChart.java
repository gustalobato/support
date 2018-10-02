
package br.com.manchestercity.automacao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe auxiliar para gerar javascript de gráficos Highcharts. Caso seja necessário realizar alguma customização não implementada, buscar demais parâmetros na Documentação da API Highcharts: http://api.highcharts.com/highcharts Data de criação: 13/09/2013
 */
public class HighChart {

	public static int ROUNDTYPE_CEIL = 0; // Arredonda pra cima
	public static int ROUNDTYPE_FLOOR = 1; // Arredonda pra baixo
	public static int ROUNDTYPE_ROUND = 2; // Se maior ou igual a 1.5 arredonda pra cima se não pra baixo

	private User user; // Usuário logado no sistema
	private String id; // Identificador da div onde o gráfico será renderizado ou da imagem
	private String divClass; // Classe CSS da div onde o gráfico será renderizado. Caso não seja fornecido, recebe a classe padrão (print.css - defaultDivChart)
	private String title; // Título do Gráfico
	private int width; // Width nesse chart não aceita %, portanto ou você seta númerico sabendo exatamente o valor ou não seta. PS: No IE abaixo de 9 é necessário setar em alguns casos
	private String subTitle; // Subtítulo do Gráfico
	private String xAxisTitle; // Título do eixo X
	private String xAxisLabels; // Labels do eixo X separados por vírgula
	private boolean xAxisRotation; // True: rotaciona em -45° os labels do eixo X. False (default): labels alinhados ao centro
	private String yAxisTitle; // Título do eixo Y
	private String yAxisUnit; // Unidade de Medida do eixo Y (Ex.: m, %, R$)
	private String yAxisMaxValue; // Maior valor exibido no eixo Y (último valor da escala). Caso não seja fornecido, o Highcharts calculará automaticamente
	private String yAxisMinValue; // Menor valor exibido no eixo Y (primeiro valor da escala). Caso não seja fornecido, o Highcharts calculará automaticamente
	private String dataLabelPrefix; // Prefixo que será exibido no label de cada ponto do gráfico
	private String dataLabelSufix; // Sufixo que será exibido no label de cada ponto do gráfico
	private List<HighChartSerie> serieList; // Lista contendo as séries (CSerieChart) do gráfico
	private boolean enableMultipleColor; // Aplicável quando o gráfico possui apenas uma série. True: para cada ponto da série gera uma cor diferente (máximo de 10 ou repete cor). False (default) usa apenas uma cor
	private boolean enableOnClick; // True: ativa evento de click para os pontos do gráfico. False (default): não permite evento de click para os pontos do gráfico
	private boolean enableLegend; // True (default): exibe legenda do gráfico na parte inferior alinhada ao centro. False: não exibe legenda do gráfico.
	private boolean enableTooltip; // False (default): exibe Tooltip.
	private boolean enableDataLabel; // true (default): exibir os valores acumulados.
	private boolean isDecimal; // False (default): Eibir em formato decimal com N casas decimais (se casas não definidas, default é 2).
	private int precision; // Quantidade de casas decimais de um valor decimal. Valor default: 2 se isDecimal = true, caso contrário 0. Somente utilizado se isDecimal = true.
	private int roundType; // Tipo de arredondamento de um valor decimal. Valor default: ROUNDTYPE_ROUND. Somente utilizado se isDecimal = true.
	private String formId; // Identificador do formulário onde um textarea contendo o svg do gráfico será adicionado para futura renderização em JPEG. Default = "_frm" (CReportGeral). Para não renderizar o textarea, fornecer ""
	private boolean overlaySeries; // True: Unifica todas as séries do tipo COLUMN sobrepondo-as. Recomendado a utilização quando somente uma das séries tiver valor em cada posição. False (default): Cada série do tipo COLUMN é uma coluna.
	private boolean isPieChart; // True: Define que somente uma Serie do tipo PIE vai ser adicionada ao gráfico. Utilizamos para fazer formataçao diferente dos valores (Valor : %). False: demais tipos de gráfico.
	private int legendItemWidth; // Define largura de cada item da legenda (necessário setar para IE9 por exemplo)
	private boolean stackingBars; // Define que as colunas serão sobrepostas
	private String pieColors; // Define as cores de cada série num gráfico do tipo Pie
	private String basicColors; // Define as cores básicas utilizadas no plot de gráficos
	private boolean reversedLegend; // Define se a orderm de exibição da legenda deve ser invertida
	private boolean ocultarValorColunaZero; // Oculta o label do valor na coluna caso este seja igual a 0.
	private boolean enableStackDiff; // habilitar e alimentar o stack na classe CSerieChart Utilizado para agraupar colunas sobrepostas. Ex. Uma coluna sobreposta e uma coluna normal.
	private boolean enableExporting; // habilitar o botão de exportação da Imagem do gráfico.

	/**
	 * Construtor.
	 * 
	 * @param user
	 *            - Usuário logado no sistema.
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
	 * Imports Javascript que devem ser adicionados ao header da página onde o gráfico será renderizado. Utilizando Highcharts v3.0.5 e JQuery 1.7.2 (ficar atento à compatibilidade ao modificar a versão de um dos componentes).
	 * 
	 * @return Imports necessários para gerar gráficos.
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
	 * Seta a configurações globais dos gráficos (apenas 1 vez por página).
	 * 
	 * @return Função Javascript com as configurações globais dos gráficos.
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
	 * Função Javascript para gerar o gráfico. Também é gerada a div onde o gráfico será renderizado e um textarea hidden contendo a svg.
	 * 
	 * @return Função Javascript e div para geração do gráfico.
	 */
	public String printChart() {
		StringBuffer out = new StringBuffer();
		HighChartSerie cSerieChart;
		try {
			out.append("<div id='" + this.id + "' class='" + (this.divClass.equals("") ? "defaultDivChart" : this.divClass) + "'></div>\n");

			// Adiciona o textarea que irá receber a svg do gráfico se existir um formulário
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
			if (!this.xAxisLabels.equals("")) { // Caso não forneça labels para o eixo X, o gráfico deve ser do tipo pie
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
				out.append("        enabled: false\n"); // Tooltip desabilitado por incompatibilidade com versões antigas do IE
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
			out.append("      exporting: { url: 'exportchart' " + (this.enableExporting ? "" : ", enabled: false") + " },\n"); // Servlet para conversão do svg do gráfico para imagem (Highcharts automaticamente seta os parâmetros)
			out.append("      navigation: { menuStyle: { width: '150px' } },\n"); // Tamanho do menu (incompatibilidade com versões antigas do IE)
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

			out.append("        series: {\n"); // Opções que afetam todas as séries
			if (this.enableOnClick) {
				out.append("      cursor: 'pointer',\n");
				out.append("      point: {\n");
				out.append("        events: {\n");
				out.append("          click: function() { eval(this.options.functionclick); }\n"); // Executa a função setada em cada ponto da série (functionclick)
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
				// Se não é decimal, fazemos somente a formatação para não exibir os valores com zero
				if (!this.isDecimal) {
					out.append("              if(this.y != 0) return this.y; \n");
				}
				// Se é decimal, mas não é overlay, então fazemos somente a formatação de decimal
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
				// Aqui é decimal e overlay, então formatamos para não exibir os zeros e também as casas decimais
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
			out.append("            enabled: true,\n"); // Exibe os valores de cada ponto do gráfico
			// altera a cor da legenda para branco, porque vai ficar dentro da barra.
			if (!enableStackDiff && !ocultarValorColunaZero) {
				out.append("            color: '#000000',\n");
			}
			out.append("            crop: false\n");
			if (!this.dataLabelPrefix.equals("") || !this.dataLabelSufix.equals("")) { // Seta o prefixo e sufixo do label. Sobrescreve o valor padrão do TYPE_PIE
				out.append("        , format: '" + this.dataLabelPrefix + " {point.y} " + this.dataLabelSufix + "'\n");
			}
			out.append("          }\n");
			out.append("        },\n");
			out.append("        pie: {\n"); // Opções que afetam apenas as séries TYPE_PIE
			out.append("          cursor: 'pointer',\n");
			if (!this.pieColors.equals("")) {
				out.append("          colors: [" + this.pieColors + "],\n");
			}
			out.append("          allowPointSelect: true,\n");
			// Mantive isso só para não dar erro nos relatórios que já utilizam a formatação desse jeito, mas teoricamente não deve ser mais usado esse formato.
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

				// não utilizar favor usar o showDataLabel do cSerieChart conforme ja utilizado acima.
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

			// Verifica se o dispositivo é mobile
			// Caso seja um dispositivo móvel, o sistema deverá bloquear o zoom, para possibilitar o click em colunas e setores
			out.append("    if (jQuery && jQuery.browser.mobile) {\n");
			out.append("      $('#" + this.id + "').highcharts().options.chart.zoomType = 'none';\n"); // Zoom na direção XY (pode ser apenas X ou apenas Y)
			out.append("      var chart = new Highcharts.Chart($('#" + this.id + "').highcharts().options);\n");
			out.append("      chart.redraw();\n");
			out.append("    }\n");

			// Seta o svg do gráfico no textarea e o adiciona no form
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
	 * Recupera o gráfico como imagem. É necessário que o gráfico tenha sido gerado (printChart).
	 * 
	 * @param request
	 *            - Requisição atual.
	 * @param cReportGeral
	 *            - Report que está gerando o gráfico.
	 * @return - Tag img com a imagem (jpeg) do gráfico.
	 */
	public String printImageJPEG(HttpServletRequest request, ReportContent cReportGeral) {
		StringBuffer out = new StringBuffer();
		try {
			// Gera um nome de contexto único
			String contextNameSvg = UUID.randomUUID().toString();

			// Adiciona o nome de contexto para remoção quando o PDF for gerado (CReportGeral)
			// Não foi possível remover no servlet pois o mesmo é chamado várias vezes pelo gerador de PDF
			cReportGeral.addChartContextName(contextNameSvg);

			// Adiciona no ServletContext o svg da imagem (para ser recuperado no servlet)
			request.getServletContext().setAttribute(contextNameSvg, request.getParameter(this.id + "_svg"));

			// Recupera a imagem através do servlet exportchart no formato jpeg
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
	 *            - Usuário logado no sistema.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return this.id;
	}

	/**
	 * @param divClass
	 *            - Identificador da div onde o gráfico será renderizado ou da imagem.
	 */
	public void setId(String id) {
		this.id = Database.verifyNull(id);
	}

	public String getDivClass() {
		return this.divClass;
	}

	/**
	 * @param divClass
	 *            - Classe CSS da div onde o gráfico será renderizado. Caso não seja fornecido, recebe a classe padrão (print.css - defaultDivChart)
	 */
	public void setDivClass(String divClass) {
		this.divClass = Database.verifyNull(divClass);
	}

	public String getTitle() {
		return this.title;
	}

	/**
	 * @param width
	 *            - Width nesse chart não aceita %, portanto ou você seta númerico sabendo exatamente o valor ou não seta. PS: No IE abaixo de 9 é necessário setar em alguns casos
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return this.width;
	}

	/**
	 * @param title
	 *            - Título do Gráfico.
	 */
	public void setTitle(String title) {
		this.title = Database.verifyNull(title);
	}

	public String getSubTitle() {
		return this.subTitle;
	}

	/**
	 * @param subTitle
	 *            - Subtítulo do Gráfico
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = Database.verifyNull(subTitle);
	}

	public String getXAxisTitle() {
		return this.xAxisTitle;
	}

	/**
	 * @param xAxisTitle
	 *            - Título do eixo X.
	 */
	public void setXAxisTitle(String xAxisTitle) {
		this.xAxisTitle = Database.verifyNull(xAxisTitle);
	}

	public String getXAxisLabels() {
		return this.xAxisLabels;
	}

	/**
	 * @param xAxisLabels
	 *            - Labels do eixo X separados por vírgula.
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
	 *            - True: Rotaciona em -45° os labels do eixo X. False: Alinhados ao centro.
	 */
	public void setXAxisRotation(boolean xAxisRotation) {
		this.xAxisRotation = xAxisRotation;
	}

	public String getYAxisTitle() {
		return this.yAxisTitle;
	}

	/**
	 * @param yAxisTitle
	 *            - Título do eixo Y.
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
	 *            - Maior valor exibido no eixo Y (último valor da escala). Caso não seja fornecido, o Highcharts calculará automaticamente.
	 */
	public void setYAxisMaxValue(String yAxisMaxValue) {
		this.yAxisMaxValue = Database.verifyNull(yAxisMaxValue);
	}

	public String getYAxisMinValue() {
		return this.yAxisMinValue;
	}

	/**
	 * @param yAxisMinValue
	 *            - Menor valor exibido no eixo Y (primeiro valor da escala). Caso não seja fornecido, o Highcharts calculará automaticamente.
	 */
	public void setYAxisMinValue(String yAxisMinValue) {
		this.yAxisMinValue = Database.verifyNull(yAxisMinValue);
	}

	public String getDataLabelPrefix() {
		return this.dataLabelPrefix;
	}

	/**
	 * @param dataLabelPrefix
	 *            - Prefixo que será exibido no label de cada ponto do gráfico.
	 */
	public void setDataLabelPrefix(String dataLabelPrefix) {
		this.dataLabelPrefix = Database.verifyNull(dataLabelPrefix);
	}

	public String getDataLabelSufix() {
		return this.dataLabelSufix;
	}

	/**
	 * @param dataLabelSufix
	 *            - Sufixo que será exibido no label de cada ponto do gráfico
	 */
	public void setDataLabelSufix(String dataLabelSufix) {
		this.dataLabelSufix = Database.verifyNull(dataLabelSufix);
	}

	/**
	 * Adiciona uma série ao gráfico.
	 * 
	 * @param cSerieChart
	 *            - Série que se deseja adicionar ao gráfico.
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
	 *            - Aplicável quando o gráfico possui apenas uma série. True: para cada ponto da série gera uma cor diferente (máximo de 10 ou repete cor). False (default) usa apenas uma cor.
	 */
	public void setEnableMultipleColor(boolean enableMultipleColor) {
		this.enableMultipleColor = enableMultipleColor;
	}

	/**
	 * @param enableOnClick
	 *            - True: ativa evento de click para os pontos do gráfico. False (default): não permite evento de click para os pontos do gráfico.
	 */
	public void setEnableOnClick(boolean enableOnClick) {
		this.enableOnClick = enableOnClick;
	}

	public boolean getEnableLegend() {
		return this.enableLegend;
	}

	/**
	 * @param enableLegend
	 *            - True (default): exibe legenda do gráfico na parte inferior alinhada ao centro. False: não exibe legenda do gráfico.
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
	 *            - Exibir em formato decimal. Valor default FALSE. Valor default de precisão: 2. Valor default de arredondamento: ROUNDTYPE_ROUND.
	 */
	public void setIsDecimal(boolean isDecimal) {
		this.setIsDecimal(isDecimal, 2);
	}

	/**
	 * @param IsDecimal
	 *            - Exibir em formato decimal. Valor default FALSE.
	 * @param precision
	 *            - Precisão de casas decimais.
	 */
	public void setIsDecimal(boolean isDecimal, int precision) {
		this.setIsDecimal(isDecimal, precision, HighChart.ROUNDTYPE_ROUND);
	}

	/**
	 * @param IsDecimal
	 *            - Exibir em formato decimal. Valor default FALSE.
	 * @param precision
	 *            - Precisão de casas decimais.
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
	 *            - Identificador do formulário onde um textarea contendo o svg do gráfico será adicionado para futura renderização em JPEG. Default = "_frm" (CReportGeral). Para não renderizar o textarea, fornecer "".
	 */
	public void setFormId(String formId) {
		this.formId = Database.verifyNull(formId);
	}

	/**
	 * @param overlaySeries
	 *            - True: Unifica todas as séries do tipo COLUMN sobrepondo-as. Recomendado a utilização quando somente uma das séries tiver valor em cada posição. False (default): Cada série do tipo COLUMN é uma coluna.
	 */
	public void setOverlaySeries(boolean overlaySeries) {
		this.overlaySeries = overlaySeries;
	}

	public boolean getOverlaySeries() {
		return this.overlaySeries;
	}

	/**
	 * @param isPieChart
	 *            - True: Define que somente uma Serie do tipo PIE vai ser adicionada ao gráfico. Utilizamos para fazer formataçao diferente dos valores (Valor : %). False: Demais tipos de gráfico.
	 */
	public void setIsPieChart(boolean isPieChart) {
		this.isPieChart = isPieChart;
	}

	public boolean getIsPieChart() {
		return this.isPieChart;
	}

	/**
	 * @param legendItemWidth
	 *            - Define largura de cada item da legenda (necessário setar para IE9 por exemplo)
	 */
	public void setLegendItemWidth(int legendItemWidth) {
		this.legendItemWidth = legendItemWidth;
	}

	public int getLegendItemWidth() {
		return this.legendItemWidth;
	}

	/**
	 * @param stackingBars
	 *            - Define que as colunas serão sobrepostas
	 */
	public void setStackingBars(boolean stackingBars) {
		this.stackingBars = stackingBars;
	}

	public boolean getStackingBars() {
		return this.stackingBars;
	}

	/**
	 * @param pieColors
	 *            - Array de cores que serão utilizadas no gráfico do tipo Pie. Exemplo: '#5B9BD5','#009966','#ED7D31','#A5A5A5'.
	 */
	public void setPieColors(String pieColors) {
		this.pieColors = pieColors;
	}

	public String getPieColors() {
		return this.pieColors;
	}

	/**
	 * @param basicColors
	 *            - Array de cores básicas que serão utilizadas no plot de gráficos. Exemplo: '#5B9BD5','#009966','#ED7D31','#A5A5A5'.
	 */
	public void setBasicColors(String basicColors) {
		this.basicColors = basicColors;
	}

	public String getBasicColors() {
		return this.basicColors;
	}

	/**
	 * @param reversedLegend
	 *            - Define se a orderm de exibição da legenda deve ser invertida
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
	 *            o botão de exportação da Imagem do gráfico.
	 */
	public void setEnableExporting(boolean enableExporting) {
		this.enableExporting = enableExporting;
	}

	public boolean getEnableExporting() {
		return this.enableExporting;
	}
}
