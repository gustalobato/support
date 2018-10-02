
package br.com.manchestercity.automacao;

import java.util.HashMap;

/**
 * Classe responsável por representar uma coluna do relatório.
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
	 * Método resoponsável por retornar o nome da coluna.
	 * 
	 * @return Nome da coluna.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Método responsável por especificar o nome da coluna.
	 * 
	 * @param name
	 *            Nome da coluna.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Método resoponsável por retornar o label da coluna.
	 * 
	 * @return Label da coluna.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Método responsável por especificar o label da coluna.
	 * 
	 * @param label
	 *            Label da coluna.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Método responsável por retornar a largura da coluna.
	 * 
	 * @return Largura da coluna.
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * Método responsável por especificar a largura da coluna.
	 * 
	 * @param width
	 *            Largura da coluna.
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * Método resoponsável por retornar o prefixo da coluna.
	 * 
	 * @return Prefixo da coluna.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Método responsável por especificar o prefixo da coluna.
	 * 
	 * @param prefix
	 *            Prefixo da coluna.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Método resoponsável por retornar o sufixo da coluna.
	 * 
	 * @return Sufixo da coluna.
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Método responsável por especificar o sufixo da coluna.
	 * 
	 * @param suffix
	 *            Sufixo da coluna.
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Método responsável por retornar o tipo de dado da coluna.
	 * 
	 * @see DataType automacao.DataType
	 * @return Tipo de dado da coluna.
	 */
	public DataType getType() {
		return type;
	}

	/**
	 * Método responsável por especificar o tipo de dado da coluna.
	 * 
	 * @see DataType automacao.DataType
	 * @param type
	 *            Tipo de dado da coluna.
	 */
	public void setType(DataType type) {
		this.type = type;
	}

	/**
	 * Método resoponsável por retornar a oparação da coluna.
	 * 
	 * @see Operation automacao.Operation
	 * @return Operação da coluna.
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * Método responsável por especificar a oparação da coluna.
	 * 
	 * @see Operation automacao.Operation
	 * @param operation
	 *            Operação da coluna.
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * Método resoponsável por retornar o alinhamento da coluna.
	 * 
	 * @see ColumnAlign automacao.CColumnAlign
	 * @return Alinhamento da coluna.
	 */
	public ColumnAlign getAlign() {
		return align;
	}

	/**
	 * Método responsável por especificar o alinhamento da coluna.
	 * 
	 * @see ColumnAlign automacao.CColumnAlign
	 * @param operation
	 *            Alinhamento da coluna.
	 */
	public void setAlign(ColumnAlign align) {
		this.align = align;
	}

	/**
	 * Método responsável por retornar o número de flags desta coluna.
	 * 
	 * @return Número de flags da coluna.
	 */
	public int getFlagsLength() {
		return flags == null ? 0 : flags.size();
	}

	/**
	 * Método responsável por retornar a cor de farolamento da coluna.
	 * 
	 * @param pValue
	 * @return Cor de farolamento da coluna
	 */
	public String getFlag(String pValue) {
		return flags.get(pValue);
	}

	/**
	 * Método responsável por retornar a lista de farolamentos da coluna.
	 * 
	 * @return Lista de farolamentos da coluna
	 */
	public HashMap<String, String> getFlagList() {
		return flags;
	}

	/**
	 * Método responsável por especificar os farolamentos desta coluna.
	 * 
	 * @param pValue
	 *            Resultado da coluna que retornará o farol especificado.
	 * @param pColor
	 *            Cor do farol que será exibida na coluna de acordo com o valor retornado.
	 */
	public void setFlag(String pValue, String pColor) {
		legend = true;
		flags.put(pValue, pColor);
	}

	/**
	 * Método responsável por indicar se coluna será exibida como Farolamento.
	 * 
	 * @param pValue
	 * @return Cor de farolamento da coluna
	 */
	public boolean isLegend() {
		return legend;
	}

	/**
	 * Método responsável por especificar se coluna será exibida como Farolamento.
	 * 
	 * @param pValue
	 *            Indicação se coluna será exibida como Farolamento.
	 */
	public void setLegend(boolean pValue) {
		legend = pValue;
	}
}
