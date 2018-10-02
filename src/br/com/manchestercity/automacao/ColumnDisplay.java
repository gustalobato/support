
package br.com.manchestercity.automacao;

public class ColumnDisplay extends Column {

	private Ordination ordination;
	private boolean totalize;
	private String icon;
	private String pageToGo;
	private String jsExecute;
	private String value;

	public Ordination getOrdination() {
		return ordination;
	}

	public void setOrdination(Ordination ordination) {
		this.ordination = ordination;
	}

	public boolean isTotalize() {
		return totalize;
	}

	public void setTotalize(boolean totalize) {
		this.totalize = totalize;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPageToGo() {
		return pageToGo;
	}

	public void setPageToGo(String pageToGo) {
		this.pageToGo = pageToGo;
	}

	public String getJSExecute() {
		return jsExecute;
	}

	public void setJSExecute(String jsExecute) {
		this.jsExecute = jsExecute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
