
package br.com.manchestercity.automacao;

import java.util.Comparator;

/**
 * Classe respons�vel por representar uma coluna de filtro do relat�rio.
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
	 * M�todo respons�vel por retornar o conector l�gico da coluna.
	 * 
	 * @see Logical br.com.cheetah.sql.Logical
	 * @return Conector l�gico da coluna.
	 */
	public Logical getLogical() {
		return logical;
	}

	/**
	 * M�todo respons�vel por especificar o conector l�gico da coluna.
	 * 
	 * @see Logical br.com.cheetah.sql.Logical
	 * @param logical
	 *            Conector l�gico da coluna.
	 */
	public void setLogical(Logical logical) {
		this.logical = logical;
	}

	/**
	 * M�todo respons�vel por retornar o comparador da coluna.
	 * 
	 * @see Comparator br.com.cheetah.sql.Comparator
	 * @return Comparador da coluna.
	 */
	public Comparison getComparator() {
		return comparator;
	}

	/**
	 * M�todo respons�vel por especificar o comparador da coluna.
	 * 
	 * @see Comparator br.com.cheetah.sql.Comparator
	 * @param comparator
	 *            Comparador da coluna.
	 */
	public void setComparator(Comparison comparator) {
		this.comparator = comparator;
	}

	/**
	 * M�todo respons�vel por retornar o valor da coluna.
	 * 
	 * @return Valor da coluna.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * M�todo respons�vel por especificar o valor da coluna.
	 * 
	 * @param value
	 *            Valor da coluna.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * M�todo respons�vel por retornar verdadeiro caso a coluna seja oculta ou falso caso contrario da coluna.
	 * 
	 * @return Verdadeiro caso a coluna seja oculta ou falso caso contrario da coluna.
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * M�todo respons�vel por especificar verdadeiro caso a coluna seja oculta ou falso caso contrario da coluna.
	 * 
	 * @param hidden
	 *            Verdadeiro caso a coluna seja oculta ou falso caso contrario da coluna.
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
