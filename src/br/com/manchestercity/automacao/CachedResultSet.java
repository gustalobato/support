
package br.com.manchestercity.automacao;

import java.util.ArrayList;

public class CachedResultSet {
	Database lcdb;

	@SuppressWarnings( "rawtypes" )
	ArrayList fields = new ArrayList();
	@SuppressWarnings( "rawtypes" )
	ArrayList values = new ArrayList();

	String[] lValues;
	String[] lFields;
	int index = 0;
	int max = 0;

	@SuppressWarnings( "rawtypes" )
	public CachedResultSet(User pUser) {
		lcdb = new Database(pUser);
		fields = new ArrayList();
		values = new ArrayList();
		index = -1;
		max = 0;
	}

	public boolean next() {
		if (index >= max) {
			return false;
		}
		else {
			index++;
			return true;
		}
	}

	@SuppressWarnings( "unchecked" )
	public boolean populateChachedResultSet(java.sql.ResultSet rs, String pFields[]) {
		max = 0;
		for (int i = 0; i < pFields.length; i++) {
			fields.add(pFields[i]);
		}

		try {
			while (rs.next()) {
				lValues = new String[pFields.length];
				for (int i = 0; i < pFields.length; i++) {
					lValues[i] = Database.verifyNull(rs.getObject(pFields[i]));
				}
				values.add(lValues);
				max++;
			}
		}
		catch (java.sql.SQLException ex) {
		}
		max--;
		return true;
	}

	@SuppressWarnings( "unchecked" )
	public String getField(String field) {
		String[] lArr = new String[fields.size()];
		String[] lArrAux = new String[fields.size()];
		fields.toArray(lArr);
		for (int i = 0; i < lValues.length; i++) {
			if (lArr[i].equals(field)) {
				lArrAux = (String[]) values.get(index);
				return lArrAux[i];
			}
		}
		return "";
	}
}
