
package br.com.manchestercity.automacao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.largeobject.LargeObject;

public class BlobControlSQLServer extends BlobControl {

	public BlobControlSQLServer(User pUser) {
		super(pUser);
	}

	public boolean saveField(String pCampo, String pTabela, String pFiltro, String pPath, boolean pAutoDelete) throws Exception {
		boolean lReturn = false;
		String lStrQuery;
		FileInputStream lInputStream = null;
		PreparedStatement lPstmt = null;
		Connection lConn = lcdb.openConnection();

		try {
			lConn.setAutoCommit(false);
			lStrQuery = "UPDATE " + pTabela + " SET " + pCampo + " = ?";
			if (!pFiltro.equals("")) {
				lStrQuery += " WHERE " + pFiltro;
			}

			// Salva o arquivo
			File lFile = new File(pPath);
			lInputStream = new FileInputStream(lFile);
			lPstmt = lConn.prepareStatement(lStrQuery);
			lPstmt.setBinaryStream(1, lInputStream, lFile.length());
			lPstmt.executeUpdate();

			lConn.commit();
			lConn.setAutoCommit(true);
			lReturn = true;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			lConn.rollback();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			lConn.rollback();
		}
		finally {
			Database.closeObject(lConn);
			if (lInputStream != null) {
				lInputStream.close();
			}
			if (lPstmt != null) {
				lPstmt.close();
			}
		}

		return lReturn;
	}

	public boolean saveField(String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception {
		return saveField(pCampo, pTabela, pFiltro, pFile, false);
	}

	public boolean saveField(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception {
		return saveField(pConn, pCampo, pTabela, pFiltro, pFile, false);
	}

	public boolean saveField(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile, boolean pCreateThumbFile) throws Exception {
		boolean lReturn = false;
		String lStrQuery;
		ByteArrayInputStream inputStream = null;
		PreparedStatement pstmt = null;

		try {
			lStrQuery = "UPDATE " + pTabela + " SET " + pCampo + " = ?";
			if (!pFiltro.equals("")) {
				lStrQuery += " WHERE " + pFiltro;
			}

			// Salva o array de bytes
			inputStream = new ByteArrayInputStream(pFile);
			pstmt = pConn.prepareStatement(lStrQuery);
			pstmt.setBinaryStream(1, inputStream, pFile.length);
			pstmt.executeUpdate();

			if (pCreateThumbFile) {
				lReturn = saveThumb(pConn, pCampo, pTabela, pFiltro, pFile);
			}

			lReturn = true;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}

		return lReturn;
	}

	public boolean saveField(String pCampo, String pTabela, String pFiltro, byte[] pFile, boolean pCreateThumbFile) throws Exception {
		boolean lReturn = false;
		String lStrQuery;
		ByteArrayInputStream inputStream = null;
		PreparedStatement pstmt = null;
		Connection lConn = lcdb.openConnection();

		try {
			lConn.setAutoCommit(false);
			lStrQuery = "UPDATE " + pTabela + " SET " + pCampo + " = ?";
			if (!pFiltro.equals("")) {
				lStrQuery += " WHERE " + pFiltro;
			}

			// Salva o array de bytes
			inputStream = new ByteArrayInputStream(pFile);
			pstmt = lConn.prepareStatement(lStrQuery);
			pstmt.setBinaryStream(1, inputStream, pFile.length);
			pstmt.executeUpdate();

			if (pCreateThumbFile) {
				lReturn = saveThumb(lConn, pCampo, pTabela, pFiltro, pFile);
			}

			lConn.commit();
			lConn.setAutoCommit(true);
			lReturn = true;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			lConn.rollback();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			lConn.rollback();
		}
		finally {
			Database.closeObject(lConn);
			if (inputStream != null) {
				inputStream.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		}

		return lReturn;
	}

	public void openField(String pCampo, String pTabela, String pFiltro, String pPath) throws Exception {
		openField(pCampo, pTabela, pFiltro, pPath, false);
	}

	public byte[] openFieldFile(String pCampo, String pTabela, String pFiltro) throws Exception {
		return openFieldFile(pCampo, pTabela, pFiltro, false);
	}

	public InputStream openInputStream(Connection pConn, String pCampo, String pTabela, String pFiltro) throws Exception {
		return openInputStream(pConn, pCampo, pTabela, pFiltro, false);
	}

	public void openField(String pCampo, String pTabela, String pFiltro, String pPath, boolean pIsThumbFile) throws Exception {
		String lStrQuery;
		ResultSet rset = null;
		Connection mConn = lcdb.openConnection();

		lStrQuery = "SELECT " + pCampo + (pIsThumbFile ? "_THUMB" : "") + " FROM " + pTabela;
		if (!pFiltro.equals("")) {
			lStrQuery += " WHERE " + pFiltro;
		}

		try {
			rset = lcdb.openResultSet(lStrQuery, mConn);
			if (rset.next()) {
				byte[] lByte = rset.getBytes(1);
				writeToFile(lByte, pPath);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			Database.closeObject(mConn);
		}
	}

	public byte[] openFieldFile(String pCampo, String pTabela, String pFiltro, boolean pIsThumbFile) throws Exception {
		String lStrQuery;
		byte[] lFile = null;
		ResultSet rset = null;
		Connection mConn = lcdb.openConnection();

		lStrQuery = "SELECT " + pCampo + (pIsThumbFile ? "_THUMB" : "") + " FROM " + pTabela;
		if (!pFiltro.equals("")) {
			lStrQuery += " WHERE " + pFiltro;
		}

		try {
			rset = lcdb.openResultSet(lStrQuery, mConn);
			if (rset.next()) {
				lFile = rset.getBytes(1);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			Database.closeObject(mConn);
		}

		return lFile;
	}

	public InputStream openInputStream(Connection pConn, String pCampo, String pTabela, String pFiltro, boolean pIsThumbFile) throws Exception {
		InputStream lInputStream = null;

		byte[] lByte = null;
		lByte = this.openFieldFile(pCampo, pTabela, pFiltro, pIsThumbFile);
		if (lByte != null) {
			lInputStream = new ByteArrayInputStream(lByte);
		}

		return lInputStream;
	}

	public boolean writeToDatabase(Object pBlob, String pPath, boolean pAutoDelete) throws Exception {
		boolean lReturn = false;
		OutputStream lOutputStream = null;
		InputStream lInputStream = null;
		try {
			lOutputStream = ((LargeObject) pBlob).getOutputStream();

			File file2Load = new File(pPath);
			if (!file2Load.exists()) {
				return false;
			}
			lInputStream = new FileInputStream(file2Load);
			byte[] bBuffer = new byte[((LargeObject) pBlob).size()];
			int intBytesRead = 0;

			// Escreve no BLOB
			while ((intBytesRead = lInputStream.read(bBuffer)) != -1) {
				lOutputStream.write(bBuffer, 0, intBytesRead);
			}

			lInputStream.close();
			lOutputStream.close();
			if (pAutoDelete) {
				file2Load.delete();
			}

			lReturn = true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			lReturn = false;
		}

		return lReturn;
	}

	public boolean writeToDatabase(Object pBlob, byte[] pFile) throws Exception {
		boolean lReturn = false;
		OutputStream llOutputStream = null;
		InputStream lInputStream = null;
		try {
			llOutputStream = ((LargeObject) pBlob).getOutputStream();

			lInputStream = new ByteArrayInputStream(pFile);
			byte[] bBuffer = new byte[((LargeObject) pBlob).size()];

			int intBytesRead = 0;

			// Escreve no BLOB
			while ((intBytesRead = lInputStream.read(bBuffer)) != -1) {
				llOutputStream.write(bBuffer, 0, intBytesRead);
			}

			lInputStream.close();
			llOutputStream.close();
			lReturn = true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			lReturn = false;
		}

		return lReturn;
	}

	public void writeToFile(Object pBlob, String pPath) throws Exception {
		try {
			int lLength;

			FileOutputStream lFileOutputStream = null;
			lFileOutputStream = new FileOutputStream(pPath);
			ByteArrayInputStream lByteArrayInputStream = new ByteArrayInputStream((byte[]) pBlob);

			int size = (int) ((LargeObject) pBlob).size();
			byte[] buffer = new byte[size];

			// Escreve no Arquivo
			while ((lLength = lByteArrayInputStream.read(buffer, 0, size)) != -1) {
				lFileOutputStream.write(buffer, 0, lLength);
			}

			lByteArrayInputStream.close();
			lFileOutputStream.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean saveThumb(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception {
		String lStrQuery;
		boolean lReturn = false;

		try {
			lStrQuery = "UPDATE " + pTabela + " SET " + pCampo + "_THUMB = ?";

			if (!pFiltro.equals("")) {
				lStrQuery += " WHERE " + pFiltro;
			}

			byte[] file = BlobControl.geraThumb(pFile);
			ByteArrayInputStream lByteArrayInputStream = new ByteArrayInputStream(file);
			PreparedStatement lPreparedStatement = pConn.prepareStatement(lStrQuery);
			lPreparedStatement.setBinaryStream(1, lByteArrayInputStream, file.length);
			lPreparedStatement.executeUpdate();

			lByteArrayInputStream.close();
			lPreparedStatement.close();
			lReturn = true;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			lReturn = false;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			lReturn = false;
		}

		return lReturn;
	}

}
