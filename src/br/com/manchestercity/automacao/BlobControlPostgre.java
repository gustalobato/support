
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

public class BlobControlPostgre extends BlobControl {

	public BlobControlPostgre(User pUser) {
		super(pUser);
	}

	public boolean saveField(String pCampo, String pTabela, String pFiltro, String pPath, boolean pAutoDelete) throws Exception {
		String lStrQuery;
		boolean lReturn = false;

		Connection mConn = lcdb.openConnection();
		try {
			mConn.setAutoCommit(false);

			File lFile = new File(pPath);
			if (lFile.exists()) {
				// Gravando blob

				lStrQuery = "Update " + pTabela + " set " + pCampo + " = ?";

				if (!pFiltro.equals("")) {
					lStrQuery += " WHERE " + pFiltro;
				}

				FileInputStream lInput = new FileInputStream(lFile);
				PreparedStatement ps = mConn.prepareStatement(lStrQuery);

				ps.setBinaryStream(1, lInput, (int) lFile.length());
				ps.executeUpdate();

				ps.close();
				lInput.close();

				mConn.commit();
				lReturn = true;
			}

		}
		catch (SQLException ex) {
			System.out.print(ex.getMessage());
			mConn.rollback();
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
			mConn.rollback();
		}
		Database.closeObject(mConn);

		return lReturn;

	}

	public boolean saveField(String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception {
		return saveField(pCampo, pTabela, pFiltro, pFile, false);
	}

	public boolean saveField(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception {
		return saveField(pConn, pCampo, pTabela, pFiltro, pFile, false);
	}

	public boolean saveField(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile, boolean pCreateThumbFile) throws Exception {
		String lStrQuery;
		boolean lReturn = false;

		try {
			lcdb.mLastError = "Commit;";

			if (pFile != null) {

				lStrQuery = "Update " + pTabela + " set " + pCampo + " = ?";

				if (!pFiltro.equals("")) {
					lStrQuery += " WHERE " + pFiltro;
				}

				ByteArrayInputStream in = new ByteArrayInputStream(pFile);
				PreparedStatement ps = pConn.prepareStatement(lStrQuery);
				ps.setBinaryStream(1, in, pFile.length);
				ps.executeUpdate();

				in.close();
				ps.close();

				lReturn = true;
			}

		}
		catch (SQLException ex) {
			System.out.print(ex.getMessage());
			lReturn = false;
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
			lReturn = false;
		}

		return lReturn;
	}

	public boolean saveField(String pCampo, String pTabela, String pFiltro, byte[] pFile, boolean pCreateThumbFile) throws Exception {
		String lStrQuery;
		boolean lReturn = false;
		Connection mConn = lcdb.openConnection();

		try {
			mConn.setAutoCommit(false);
			lcdb.mLastError = "Commit;";

			if (pFile != null) {
				// Gravando blob

				lStrQuery = "Update " + pTabela + " set " + pCampo + " = ?";

				if (!pFiltro.equals("")) {
					lStrQuery += " WHERE " + pFiltro;
				}

				ByteArrayInputStream in = new ByteArrayInputStream(pFile);
				PreparedStatement ps = mConn.prepareStatement(lStrQuery);
				ps.setBinaryStream(1, in, pFile.length);
				ps.executeUpdate();

				in.close();
				ps.close();

				mConn.commit();
				mConn.setAutoCommit(true);
				Database.closeObject(mConn);

				lReturn = true;
			}

		}
		catch (SQLException ex) {
			System.out.print(ex.getMessage());
			mConn.rollback();
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
			mConn.rollback();
		}
		Database.closeObject(mConn);

		return lReturn;
	}

	// Este método so é utilizando para arquivos gravados em sistema de arquivos.
	public boolean deleteFile(String pFileName) throws Exception {
		return true;
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

		mConn.setAutoCommit(false);

		// Criando select
		lStrQuery = "SELECT " + pCampo + ( pIsThumbFile ? "_THUMB" : "" ) + " FROM " + pTabela;
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
		}
		Database.closeObject(mConn);
		mConn.setAutoCommit(true);
	}

	public byte[] openFieldFile(String pCampo, String pTabela, String pFiltro, boolean pIsThumbFile) throws Exception {
		String lStrQuery;
		byte[] lRet = null;
		ResultSet rset = null;
		Connection mConn = lcdb.openConnection();

		mConn.setAutoCommit(false);

		// Criando select
		lStrQuery = "SELECT " + pCampo + ( pIsThumbFile ? "_THUMB" : "" ) + " FROM " + pTabela;
		if (!pFiltro.equals("")) {
			lStrQuery += " WHERE " + pFiltro;
		}

		try {
			rset = lcdb.openResultSet(lStrQuery, mConn);
			if (rset.next()) {
				lRet = rset.getBytes(1);
			}
		}
		catch (Exception ex) {
		}

		mConn.setAutoCommit(true);
		Database.closeObject(mConn);
		return lRet;

	}

	public InputStream openInputStream(Connection pConn, String pCampo, String pTabela, String pFiltro, boolean pIsThumbFile) throws Exception {

		InputStream lRet = null;

		byte[] lByte = null;
		lByte = this.openFieldFile(pCampo, pTabela, pFiltro, pIsThumbFile);
		if (lByte != null) {
			lRet = new ByteArrayInputStream(lByte);
		}

		return lRet;

	}

	public boolean writeToDatabase(Object pBlob, String pPath, boolean pAutoDelete) throws Exception {
		OutputStream blobOutputStream = null;
		InputStream sampleFileStream = null;
		try {
			blobOutputStream = ( (LargeObject) pBlob ).getOutputStream();

			File file2Load = new File(pPath);
			if (!file2Load.exists()) {
				return false;
			}
			sampleFileStream = new FileInputStream(file2Load);
			byte[] bBuffer = new byte[( (LargeObject) pBlob ).size()];
			int intBytesRead = 0;
			// read from file until done
			while (( intBytesRead = sampleFileStream.read(bBuffer) ) != -1) {
				// write to BLOB
				blobOutputStream.write(bBuffer, 0, intBytesRead);
			}
			// closing the streams and committing
			sampleFileStream.close();
			blobOutputStream.close();
			if (pAutoDelete) {
				file2Load.delete();
			}

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return true;

	}

	public boolean writeToDatabase(Object pBlob, byte[] pFile) throws Exception {
		OutputStream blobOutputStream = null;
		InputStream sampleFileStream = null;
		try {
			blobOutputStream = ( (LargeObject) pBlob ).getOutputStream();

			sampleFileStream = new ByteArrayInputStream(pFile);
			// Buffer to hold chunks of data to being written to the BLOB.
			byte[] bBuffer = new byte[( (LargeObject) pBlob ).size()];
			// Read a chunk of data from the sample file input stream,
			// and write the chunk to the BLOB column output stream.
			// Repeat till file has been fully read.
			int intBytesRead = 0;
			// read from file until done
			while (( intBytesRead = sampleFileStream.read(bBuffer) ) != -1) {
				// write to BLOB
				blobOutputStream.write(bBuffer, 0, intBytesRead);
			}
			// closing the streams and committing
			sampleFileStream.close();
			blobOutputStream.close();

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return true;

	}

	public void writeToFile(Object pBlob, String pPath) throws Exception {
		try {

			int length;

			FileOutputStream outFile = null;
			outFile = new FileOutputStream(pPath);
			ByteArrayInputStream instream = new ByteArrayInputStream((byte[]) pBlob);

			int size = (int) ( (LargeObject) pBlob ).size();
			byte[] buffer = new byte[size];

			// Fetch data
			while (( length = instream.read(buffer, 0, size) ) != -1) {
				outFile.write(buffer, 0, length);
			}

			// Close input and output streams
			instream.close();
			outFile.close();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * SaveThumb
	 *
	 * @param pConn
	 *            Connection
	 * @param pCampo
	 *            String
	 * @param pTabela
	 *            String
	 * @param pFiltro
	 *            String
	 * @param pFile
	 *            byte[]
	 * @return boolean
	 */
	public boolean saveThumb(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception {
		String lStrQuery;
		boolean lReturn = false;

		try {
			lcdb.mLastError = "Commit;";

			if (pFile != null) {

				lStrQuery = "Update " + pTabela + " set " + pCampo + "_THUMB = ?";

				if (!pFiltro.equals("")) {
					lStrQuery += " WHERE " + pFiltro;
				}
				byte[] file = BlobControl.geraThumb(pFile);
				ByteArrayInputStream in = new ByteArrayInputStream(file);
				PreparedStatement ps = pConn.prepareStatement(lStrQuery);
				ps.setBinaryStream(1, in, file.length);
				ps.executeUpdate();

				in.close();
				ps.close();

				lReturn = true;
			}

		}
		catch (SQLException ex) {
			System.out.print(ex.getMessage());
			lReturn = false;
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
			lReturn = false;
		}

		return lReturn;
	}

}
