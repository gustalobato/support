
package br.com.manchestercity.automacao;

import java.util.Comparator;

public class ReportFilterComparator implements Comparator<ReportFilterField> {
	public int compare(ReportFilterField field1, ReportFilterField field2) {
		return field1.compareTo(field2);
	}
}
