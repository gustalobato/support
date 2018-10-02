
package br.com.manchestercity.automacao;

import java.util.List;

/**
 * Classe auxiliar para armazenar Series de gráficos gerados pela classe CChart. Data de criação: 13/09/2013
 */
public class HighChartSerie {

	private String name; // Nome da série (aparece na legenda)
	private String type; // Tipo do gráfico sendo: area, areaspline, bar, column, line, pie, scatter ou spline
	private String color; // Cor da série. Caso não seja especificado, segue o padrão do Highcharts. É sobrescrita quando a opção "enableMultipleColor" do gráfico (CChart) é ativada
	private String pieLabels; // Labels do gráfico TYPE_PIE separados por vírgula
	private String values; // Valores da série separados por virgula. Para não informar valor para alguma posição, fornecer null
	private String stack; // Utilizado para agraupar colunas sobrepostas. Ex. Uma coluna sobreposta e uma coluna normal.
	private List<String> functionOnClickList; // Lista de Strings contendo as funções que devem ser executadas no evento de click para cada ponto do gráfico. As funções não devem conter aspas duplas. Não aplicável ao TYPE_PIE.
	private boolean showDataLabels; // Mostra os valores.

	// Tipos de Série
	public static final String TYPE_AREA = "area";
	public static final String TYPE_AREASPLINE = "areaspline";
	public static final String TYPE_BAR = "bar";
	public static final String TYPE_COLUMN = "column";
	public static final String TYPE_LINE = "line";
	public static final String TYPE_PIE = "pie";
	public static final String TYPE_SCATTER = "scatter";
	public static final String TYPE_SPLINE = "spline";
	public static final String TYPE_AREARANGE = "arearange";

	/**
	 * Construtor da classe.
	 * 
	 * @param name
	 *            - Nome da série (aparece na legenda quando o gráfico não é TYPE_PIE).
	 * @param type
	 *            - Tipo do gráfico (variáveis estáticas).
	 * @param color
	 *            - Cor da série (Ex.: #000000). Caso não seja especificado, segue o padrão do Highcharts.
	 * @param pieLabels
	 *            - Labels do gráfico TYPE_PIE separados por vírgula
	 * @param values
	 *            - Valores da série separados por virgula. Para não informar valor para alguma posição, fornecer null.
	 * @param functionOnClick
	 *            - Lista de Strings contendo as funções que devem ser executadas no evento de click para cada ponto do gráfico. As funções não devem conter aspas duplas.
	 */
	public HighChartSerie(String name, String type, String color, String pieLabels, String values, List<String> functionOnClickList) {
		this.name = Database.verifyNull(name);
		this.type = Database.verifyNull(type);
		this.color = Database.verifyNull(color);
		this.pieLabels = Database.verifyNull(pieLabels);
		this.values = Database.verifyNull(values);
		this.functionOnClickList = functionOnClickList;
		this.showDataLabels = true;
		this.stack = "";
	}

	/**
	 * Construtor da classe.
	 * 
	 * @param name
	 *            - Nome da série (aparece na legenda quando o gráfico não é TYPE_PIE).
	 * @param type
	 *            - Tipo do gráfico (variáveis estáticas).
	 * @param color
	 *            - Cor da série (Ex.: #000000). Caso não seja especificado, segue o padrão do Highcharts.
	 * @param pieLabels
	 *            - Labels do gráfico TYPE_PIE separados por vírgula
	 * @param values
	 *            - Valores da série separados por virgula. Para não informar valor para alguma posição, fornecer null.
	 * @param functionOnClick
	 *            - Lista de Strings contendo as funções que devem ser executadas no evento de click para cada ponto do gráfico. As funções não devem conter aspas duplas.
	 * @param stack
	 *            - Utilizado para agraupar colunas sobrepostas. Ex. Uma coluna sobreposta e uma coluna normal.
	 */
	public HighChartSerie(String name, String type, String color, String pieLabels, String values, List<String> functionOnClickList, String stack) {
		this.name = Database.verifyNull(name);
		this.type = Database.verifyNull(type);
		this.color = Database.verifyNull(color);
		this.pieLabels = Database.verifyNull(pieLabels);
		this.values = Database.verifyNull(values);
		this.functionOnClickList = functionOnClickList;
		this.showDataLabels = true;
		this.stack = stack;
	}

	public HighChartSerie() {
		this.name = "";
		this.type = "";
		this.color = "";
		this.pieLabels = "";
		this.values = "";
		this.functionOnClickList = null;
		this.showDataLabels = false;
		this.stack = "";

	}

	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            - Nome da série (aparece na legenda quando o gráfico não é TYPE_PIE).
	 */
	public void setName(String name) {
		this.name = Database.verifyNull(name);
	}

	public String getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            - Tipo do gráfico (variáveis estáticas).
	 */
	public void setType(String type) {
		this.type = Database.verifyNull(type);
	}

	public String getColor() {
		return this.color;
	}

	/**
	 * @param color
	 *            - Cor da série (Ex.: #000000). Caso não seja especificado, segue o padrão do Highcharts.
	 */
	public void setColor(String color) {
		this.color = Database.verifyNull(color);
	}

	public String getPieLabels() {
		return this.pieLabels;
	}

	/**
	 * @param pieLabels
	 *            - Labels do gráfico TYPE_PIE separados por vírgula.
	 */
	public void setPieLabels(String pieLabels) {
		this.pieLabels = Database.verifyNull(pieLabels);
	}

	public String getValues() {
		return this.values;
	}

	/**
	 * @param data
	 *            - Valores da série separados por virgula. Para não informar valor para alguma posição, fornecer null.
	 */
	public void setValues(String values) {
		this.values = Database.verifyNull(values);
	}

	public boolean getShowDataLabels() {
		return this.showDataLabels;
	}

	/**
	 * @param dataLabels
	 *            - mostra valores.
	 */
	public void setShowDataLabels(boolean values) {
		this.showDataLabels = values;
	}

	public String getStack() {
		return this.stack;
	}

	/**
	 * @param stack
	 *            - Utilizado para agraupar colunas sobrepostas. Ex. Uma coluna sobreposta e uma coluna normal..
	 */
	public void setStack(String values) {
		this.stack = values;
	}

	public List<String> getFunctionOnClickList() {
		return this.functionOnClickList;
	}

	/**
	 * @param functionOnClick
	 *            - Lista de Strings contendo as funções que devem ser executadas no evento de click para cada ponto do gráfico. As funções não devem conter aspas duplas. Não aplicável ao TYPE_PIE.
	 */
	public void setFunctionOnClickList(List<String> functionOnClickList) {
		this.functionOnClickList = functionOnClickList;
	}

	/**
	 * Recupera os dados de uma série TYPE_PIE no seguinte formato: "['label', valor], ['label', valor]".
	 * 
	 * @return - Dados da série tipo TYPE_PIE formatados.
	 */
	public String getPieData() {
		return getPieData(false);
	}

	public String getPieData(boolean enableClick) {
		StringBuffer out = new StringBuffer();
		try {
			// Recupera um vetor contendo os labels fornecidos
			String[] labelsArray = this.pieLabels.split(",");

			// Recupera um vetor contendo os valores fornecidos
			String[] valuesArray = this.values.split(",");

			for (int position = 0; position < labelsArray.length; position++) {
				out.append("{name:'" + labelsArray[position] + "', y:" + valuesArray[position]);
				if (enableClick && this.functionOnClickList != null && position < this.functionOnClickList.size()) {
					out.append(", functionclick: \"" + Database.verifyNull(this.functionOnClickList.get(position)).replaceAll("\"", "'") + "\"");
				}
				out.append("}");

				if (position + 1 != labelsArray.length) {
					out.append(", ");
				}
			}
		}
		catch (Exception ex) {
			Utils.printSystemError("CSerieChart - getPieData", ex.getMessage());
			out.setLength(0);
		}
		return out.toString();
	}

	/**
	 * com evento onclick no seguinte formato: "{y:valor, functionclick:"função"}, {y:valor, functionclick:"função"}".
	 * 
	 * @return Dados da série com evento onclick formatados.
	 */

	/**
	 * Recupera os dados de uma série.
	 * 
	 * @param enableOnClick
	 *            - True caso a possua funções de onClick (functionOnClickList). False caso contrário.
	 * @param enableMultipleColor
	 *            - True caso o gráfico possua a opção de mutiplas cores. False caso contrário.
	 * @return Dados da série formatados "{y:valor, color:'cor', functionclick:'função'}, {y:valor, color:'cor', functionclick:'função'}".
	 */
	public String getData(boolean enableOnClick, boolean enableMultipleColor) {
		StringBuffer out = new StringBuffer();
		try {
			// Recupera um vetor contendo os valores fornecidos
			String[] valuesArray = this.values.split(",");

			// Variável para recuperar as cores do vetor do Highcharts
			int colorPosition = 0;

			for (int position = 0; position < valuesArray.length; position++) {
				if (this.getType().equals(TYPE_AREARANGE)) {
					String[] arrayInterno = valuesArray[position].split("#");
					out.append("[");
					for (int i = 0; i < arrayInterno.length; i++) {
						out.append(arrayInterno[i]);
						if (i + 1 != arrayInterno.length) {
							out.append(", ");
						}
					}
					out.append("]");
				}
				else {
					out.append("{y: " + valuesArray[position]);
					if (enableMultipleColor) {
						out.append(", color: Highcharts.getOptions().colors[" + colorPosition + "]");
					}

					if (enableOnClick) {
						out.append(", functionclick: \"" + Database.verifyNull(this.functionOnClickList.get(position)).replaceAll("\"", "'") + "\"");
					}
					out.append("}");

					// O vetor de cores do Highcharts possui 10 cores
					// Repete as cores caso necessário
					colorPosition++;
					if (colorPosition == 20) {
						colorPosition = 0;
					}
				}

				if (position + 1 != valuesArray.length) {
					out.append(", ");
				}
			}
		}
		catch (Exception ex) {
			Utils.printSystemError("CSerieChart - getValuesOnClick", ex.getMessage());
			out.setLength(0);
		}
		return out.toString();
	}

}
