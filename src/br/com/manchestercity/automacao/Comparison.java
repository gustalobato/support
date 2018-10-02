
package br.com.manchestercity.automacao;

public enum Comparison {

	DIFFERENT {
		public String toString() {
			return "<>";
		}
	},
	EQUAL {
		public String toString() {
			return "=";
		}
	},
	IN {
		public String toString() {
			return "IN";
		}
	},
	IS_NULL {
		public String toString() {
			return "IS NULL";
		}
	},
	IS_NOT_NULL {
		public String toString() {
			return "IS NOT NULL";
		}
	},
	LOWER {
		public String toString() {
			return "<";
		}
	},
	LOWER_EQUAL {
		public String toString() {
			return "<=";
		}
	},
	LIKE, GREATER {
		public String toString() {
			return ">";
		}
	},
	GREATER_EQUAL {
		public String toString() {
			return ">=";
		}
	},
	NOT_IN {
		public String toString() {
			return "NOT IN";
		}
	}
}
