
package br.com.manchestercity.automacao;

import java.util.Comparator;

/**
 * Classe responsável por representar uma coluna de filtro do relatório.
 * 
 * @author Jehan Serafim de Albuquerque
 * @since 2012
 * @version 1.0
 */

public class ColumnFilter extends Column {

	private Logical logical;
	private Comparison comparator;
	private String value;
	private boolean hidden;

	/**
	 * Método responsável por retornar o conector lógico da coluna.
	 * 
	 * @see Logical br.com.cheetah.sql.Logical
	 * @return Conector lógico da coluna.
	 */
	public Logical getLogical() {
		return logical;
	}

	/**
	 * Método responsável por especificar o conector lógico da coluna.
	 * 
	 * @see Logical br.com.cheetah.sql.Logical
	 * @param logical
	 *            Conector lógico da coluna.
	 */
	public void setLogical(Logical logical) {
		this.logical = logical;
	}

	/**
	 * Método responsável por retornar o comparador da coluna.
	 * 
	 * @see Comparator br.com.cheetah.sql.Comparator
	 * @return Comparador da coluna.
	 */
	public Comparison getComparator() {
		return comparator;
	}

	/**
	 * Método responsável por especificar o comparador da coluna.
	 * 
	 * @see Comparator br.com.cheetah.sql.Comparator
	 * @param comparator
	 *            Comparador da coluna.
	 */
	public void setComparator(Comparison comparator) {
		this.comparator = comparator;
	}

	/**
	 * Método responsável por retornar o valor da coluna.
	 * 
	 * @return Valor da coluna.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Método responsável por especificar o valor da coluna.
	 * 
	 * @param value
	 *            Valor da coluna.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Método responsável por retornar verdadeiro caso a coluna seja oculta ou falso caso contrario da coluna.
	 * 
	 * @return Verdadeiro caso a coluna seja oculta ou falso caso contrario da coluna.
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Método responsável por especificar verdadeiro caso a coluna seja oculta ou falso caso contrario da coluna.
	 * 
	 * @param hidden
	 *            Verdadeiro caso a coluna seja oculta ou falso caso contrario da coluna.
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
