
package br.com.manchestercity.automacao;

import java.util.HashMap;

/**
 * Classe respons�vel por representar uma coluna do relat�rio.
 * 
 * @author Jehan Serafim de Albuquerque
 * @since 2012
 * @version 1.0
 */

public class Column {

	private String name;
	private String label;
	private String width;
	private String prefix;
	private String suffix;
	private DataType type;
	private Operation operation;
	private ColumnAlign align;

	private HashMap<String, String> flags = new HashMap<String, String>();

	private boolean legend = false;

	/**
	 * M�todo resopons�vel por retornar o nome da coluna.
	 * 
	 * @return Nome da coluna.
	 */
	public String getName() {
		return name;
	}

	/**
	 * M�todo respons�vel por especificar o nome da coluna.
	 * 
	 * @param name
	 *            Nome da coluna.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * M�todo resopons�vel por retornar o label da coluna.
	 * 
	 * @return Label da coluna.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * M�todo respons�vel por especificar o label da coluna.
	 * 
	 * @param label
	 *            Label da coluna.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * M�todo respons�vel por retornar a largura da coluna.
	 * 
	 * @return Largura da coluna.
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * M�todo respons�vel por especificar a largura da coluna.
	 * 
	 * @param width
	 *            Largura da coluna.
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * M�todo resopons�vel por retornar o prefixo da coluna.
	 * 
	 * @return Prefixo da coluna.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * M�todo respons�vel por especificar o prefixo da coluna.
	 * 
	 * @param prefix
	 *            Prefixo da coluna.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * M�todo resopons�vel por retornar o sufixo da coluna.
	 * 
	 * @return Sufixo da coluna.
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * M�todo respons�vel por especificar o sufixo da coluna.
	 * 
	 * @param suffix
	 *            Sufixo da coluna.
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * M�todo respons�vel por retornar o tipo de dado da coluna.
	 * 
	 * @see DataType automacao.DataType
	 * @return Tipo de dado da coluna.
	 */
	public DataType getType() {
		return type;
	}

	/**
	 * M�todo respons�vel por especificar o tipo de dado da coluna.
	 * 
	 * @see DataType automacao.DataType
	 * @param type
	 *            Tipo de dado da coluna.
	 */
	public void setType(DataType type) {
		this.type = type;
	}

	/**
	 * M�todo resopons�vel por retornar a opara��o da coluna.
	 * 
	 * @see Operation automacao.Operation
	 * @return Opera��o da coluna.
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * M�todo respons�vel por especificar a opara��o da coluna.
	 * 
	 * @see Operation automacao.Operation
	 * @param operation
	 *            Opera��o da coluna.
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * M�todo resopons�vel por retornar o alinhamento da coluna.
	 * 
	 * @see ColumnAlign automacao.CColumnAlign
	 * @return Alinhamento da coluna.
	 */
	public ColumnAlign getAlign() {
		return align;
	}

	/**
	 * M�todo respons�vel por especificar o alinhamento da coluna.
	 * 
	 * @see ColumnAlign automacao.CColumnAlign
	 * @param operation
	 *            Alinhamento da coluna.
	 */
	public void setAlign(ColumnAlign align) {
		this.align = align;
	}

	/**
	 * M�todo respons�vel por retornar o n�mero de flags desta coluna.
	 * 
	 * @return N�mero de flags da coluna.
	 */
	public int getFlagsLength() {
		return flags == null ? 0 : flags.size();
	}

	/**
	 * M�todo respons�vel por retornar a cor de farolamento da coluna.
	 * 
	 * @param pValue
	 * @return Cor de farolamento da coluna
	 */
	public String getFlag(String pValue) {
		return flags.get(pValue);
	}

	/**
	 * M�todo respons�vel por retornar a lista de farolamentos da coluna.
	 * 
	 * @return Lista de farolamentos da coluna
	 */
	public HashMap<String, String> getFlagList() {
		return flags;
	}

	/**
	 * M�todo respons�vel por especificar os farolamentos desta coluna.
	 * 
	 * @param pValue
	 *            Resultado da coluna que retornar� o farol especificado.
	 * @param pColor
	 *            Cor do farol que ser� exibida na coluna de acordo com o valor retornado.
	 */
	public void setFlag(String pValue, String pColor) {
		legend = true;
		flags.put(pValue, pColor);
	}

	/**
	 * M�todo respons�vel por indicar se coluna ser� exibida como Farolamento.
	 * 
	 * @param pValue
	 * @return Cor de farolamento da coluna
	 */
	public boolean isLegend() {
		return legend;
	}

	/**
	 * M�todo respons�vel por especificar se coluna ser� exibida como Farolamento.
	 * 
	 * @param pValue
	 *            Indica��o se coluna ser� exibida como Farolamento.
	 */
	public void setLegend(boolean pValue) {
		legend = pValue;
	}
}
