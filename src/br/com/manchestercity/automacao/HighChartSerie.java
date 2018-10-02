
package br.com.manchestercity.automacao;

import java.util.List;

/**
 * Classe auxiliar para armazenar Series de gr�ficos gerados pela classe CChart. Data de cria��o: 13/09/2013
 */
public class HighChartSerie {

	private String name; // Nome da s�rie (aparece na legenda)
	private String type; // Tipo do gr�fico sendo: area, areaspline, bar, column, line, pie, scatter ou spline
	private String color; // Cor da s�rie. Caso n�o seja especificado, segue o padr�o do Highcharts. � sobrescrita quando a op��o "enableMultipleColor" do gr�fico (CChart) � ativada
	private String pieLabels; // Labels do gr�fico TYPE_PIE separados por v�rgula
	private String values; // Valores da s�rie separados por virgula. Para n�o informar valor para alguma posi��o, fornecer null
	private String stack; // Utilizado para agraupar colunas sobrepostas. Ex. Uma coluna sobreposta e uma coluna normal.
	private List<String> functionOnClickList; // Lista de Strings contendo as fun��es que devem ser executadas no evento de click para cada ponto do gr�fico. As fun��es n�o devem conter aspas duplas. N�o aplic�vel ao TYPE_PIE.
	private boolean showDataLabels; // Mostra os valores.

	// Tipos de S�rie
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
	 *            - Nome da s�rie (aparece na legenda quando o gr�fico n�o � TYPE_PIE).
	 * @param type
	 *            - Tipo do gr�fico (vari�veis est�ticas).
	 * @param color
	 *            - Cor da s�rie (Ex.: #000000). Caso n�o seja especificado, segue o padr�o do Highcharts.
	 * @param pieLabels
	 *            - Labels do gr�fico TYPE_PIE separados por v�rgula
	 * @param values
	 *            - Valores da s�rie separados por virgula. Para n�o informar valor para alguma posi��o, fornecer null.
	 * @param functionOnClick
	 *            - Lista de Strings contendo as fun��es que devem ser executadas no evento de click para cada ponto do gr�fico. As fun��es n�o devem conter aspas duplas.
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
	 *            - Nome da s�rie (aparece na legenda quando o gr�fico n�o � TYPE_PIE).
	 * @param type
	 *            - Tipo do gr�fico (vari�veis est�ticas).
	 * @param color
	 *            - Cor da s�rie (Ex.: #000000). Caso n�o seja especificado, segue o padr�o do Highcharts.
	 * @param pieLabels
	 *            - Labels do gr�fico TYPE_PIE separados por v�rgula
	 * @param values
	 *            - Valores da s�rie separados por virgula. Para n�o informar valor para alguma posi��o, fornecer null.
	 * @param functionOnClick
	 *            - Lista de Strings contendo as fun��es que devem ser executadas no evento de click para cada ponto do gr�fico. As fun��es n�o devem conter aspas duplas.
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
	 *            - Nome da s�rie (aparece na legenda quando o gr�fico n�o � TYPE_PIE).
	 */
	public void setName(String name) {
		this.name = Database.verifyNull(name);
	}

	public String getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            - Tipo do gr�fico (vari�veis est�ticas).
	 */
	public void setType(String type) {
		this.type = Database.verifyNull(type);
	}

	public String getColor() {
		return this.color;
	}

	/**
	 * @param color
	 *            - Cor da s�rie (Ex.: #000000). Caso n�o seja especificado, segue o padr�o do Highcharts.
	 */
	public void setColor(String color) {
		this.color = Database.verifyNull(color);
	}

	public String getPieLabels() {
		return this.pieLabels;
	}

	/**
	 * @param pieLabels
	 *            - Labels do gr�fico TYPE_PIE separados por v�rgula.
	 */
	public void setPieLabels(String pieLabels) {
		this.pieLabels = Database.verifyNull(pieLabels);
	}

	public String getValues() {
		return this.values;
	}

	/**
	 * @param data
	 *            - Valores da s�rie separados por virgula. Para n�o informar valor para alguma posi��o, fornecer null.
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
	 *            - Lista de Strings contendo as fun��es que devem ser executadas no evento de click para cada ponto do gr�fico. As fun��es n�o devem conter aspas duplas. N�o aplic�vel ao TYPE_PIE.
	 */
	public void setFunctionOnClickList(List<String> functionOnClickList) {
		this.functionOnClickList = functionOnClickList;
	}

	/**
	 * Recupera os dados de uma s�rie TYPE_PIE no seguinte formato: "['label', valor], ['label', valor]".
	 * 
	 * @return - Dados da s�rie tipo TYPE_PIE formatados.
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
	 * com evento onclick no seguinte formato: "{y:valor, functionclick:"fun��o"}, {y:valor, functionclick:"fun��o"}".
	 * 
	 * @return Dados da s�rie com evento onclick formatados.
	 */

	/**
	 * Recupera os dados de uma s�rie.
	 * 
	 * @param enableOnClick
	 *            - True caso a possua fun��es de onClick (functionOnClickList). False caso contr�rio.
	 * @param enableMultipleColor
	 *            - True caso o gr�fico possua a op��o de mutiplas cores. False caso contr�rio.
	 * @return Dados da s�rie formatados "{y:valor, color:'cor', functionclick:'fun��o'}, {y:valor, color:'cor', functionclick:'fun��o'}".
	 */
	public String getData(boolean enableOnClick, boolean enableMultipleColor) {
		StringBuffer out = new StringBuffer();
		try {
			// Recupera um vetor contendo os valores fornecidos
			String[] valuesArray = this.values.split(",");

			// Vari�vel para recuperar as cores do vetor do Highcharts
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
					// Repete as cores caso necess�rio
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
