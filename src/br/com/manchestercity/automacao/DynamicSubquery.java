
package br.com.manchestercity.automacao;

import br.com.manchestercity.automacao.FrameworkDefaults.ReportFieldType;

public class DynamicSubquery {

	public final static String OPERATOR_EQUALS = "0"; // =, <>
	public final static String OPERATOR_IN = "1"; // in, not in
	public final static String OPERATOR_MORETHEN = "2"; // >=, <=

	private String targetField;
	private String subqueryField;
	private String subqueryTable;

	private ReportFilterField reportField;

	private String operator;

	public String getSubqueryTable() {
		return subqueryTable;
	}

	public String getOperator() {
		return operator;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public void setSubqueryTable(String subqueryTable) {
		this.subqueryTable = subqueryTable;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTargetField() {
		return targetField;
	}

	public ReportFilterField getReportField() {
		return reportField;
	}

	public void setReportField(ReportFilterField reportField) {
		this.reportField = reportField;
	}

	public DynamicSubquery(String targetField, String operator, String subqueryField, String subqueryTable) {
		this.targetField = targetField;
		this.subqueryField = subqueryField;
		this.subqueryTable = subqueryTable;
		this.operator = operator;
	}

	public String createWhere(Database lcdb, boolean useAND) {
		return createWhereReport(lcdb, useAND);
	}

	public String createWhereReport(Database lcdb, boolean useAND) {
		StringBuffer subquery = new StringBuffer();

		if (reportField.getFilterValue().length() > 0) {
			subquery.append(targetField);

			if (reportField.getType().equals(ReportFieldType.SMARTCOMBO_LIST)) {
				if (this.operator.equals(OPERATOR_EQUALS)) { // =, <>
					subquery.append(reportField.getFilterValue().split("//")[1].equals("I") ? " = " : " <> ");
				}
				else if (this.operator.equals(OPERATOR_IN)) { // in, not in
					subquery.append(reportField.getFilterValue().split("//")[1].equals("I") ? " IN " : " NOT IN ");
				}
				else if (this.operator.equals(OPERATOR_MORETHEN)) { // >=, <=
					subquery.append(reportField.getFilterValue().split("//")[1].equals("I") ? " >= " : " <= ");
				}
			}
			else {
				if (this.operator.equals(OPERATOR_EQUALS)) { // =, <>
					subquery.append(" = ");
				}
				else if (this.operator.equals(OPERATOR_IN)) { // in, not in
					subquery.append(" IN ");
				}
				else if (this.operator.equals(OPERATOR_MORETHEN)) { // >=, <=
					subquery.append(" >= ");
				}
			}

			subquery.append(" ( SELECT ");
			subquery.append(subqueryField);
			subquery.append(" FROM ");
			subquery.append(subqueryTable);
			subquery.append(" WHERE ");
			subquery.append(reportField.createWhere(lcdb, useAND));
			subquery.append(" ) ");

		}

		return subquery.toString();
	}
}
