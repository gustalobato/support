
package br.com.manchestercity.automacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.Connection;

public class BlobControlFileSystem extends BlobControl {

	public BlobControlFileSystem(User pUser) {
		super(pUser);
	}

	public boolean saveField(String pCampo, String pTabela, String pFiltro, String pPath, boolean pAutoDelete) throws Exception {

		boolean lReturn = false;
		Connection mConn = lcdb.openConnection();

		try {

			File lFileIn = new File(pPath);
			File lFileOut = new File(getFileNameByTable(pTabela, pFiltro, mConn));
			if (lFileIn.exists()) {
				lFileIn.renameTo(lFileOut);
				lReturn = true;
			}
			else {
				System.out.println("File does not exist (" + pPath + ")");
			}

		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
		finally {
			Database.closeObject(mConn);
		}

		return lReturn;
	}

	public boolean saveField(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception {
		return saveField(pConn, pCampo, pTabela, pFiltro, pFile, false);
	}

	public boolean saveField(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile, boolean pCreateThumbFile) throws Exception {

		boolean lReturn = false;

		try {
			File lFileCreate = new File(getFileNameByTable(pTabela, pFiltro, pConn));
			lFileCreate.createNewFile();
			FileOutputStream lFileOut = new FileOutputStream(lFileCreate);
			lFileOut.write(pFile);
			lFileOut.close();
			lReturn = true;
			if (pCreateThumbFile) {
				lReturn = saveThumb(pConn, pCampo, pTabela, pFiltro, pFile);
			}
		}
		catch (Exception ex) {
			lReturn = false;
		}

		return lReturn;
	}

	public boolean saveField(String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception {
		return saveField(pCampo, pTabela, pFiltro, pFile, false);
	}

	public boolean saveField(String pCampo, String pTabela, String pFiltro, byte[] pFile, boolean pCreateThumbFile) throws Exception {

		boolean lReturn = false;
		Connection mConn = lcdb.openConnection();

		try {
			FileOutputStream lFileOut = new FileOutputStream(getFileNameByTable(pTabela, pFiltro, mConn));
			lFileOut.write(pFile);
			lFileOut.close();
			lReturn = true;
			if (pCreateThumbFile) {
				lReturn = saveThumb(mConn, pCampo, pTabela, pFiltro, pFile);
			}
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());

		}
		finally {
			Database.closeObject(mConn);
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

		Connection mConn = lcdb.openConnection();
		try {
			File lFileIn = new File(getFileNameByTable(pTabela, pFiltro, mConn, pIsThumbFile));
			File lFileOut = new File(pPath);

			FileReader in = new FileReader(lFileIn);
			FileWriter out = new FileWriter(lFileOut);
			int c;

			while (( c = in.read() ) != -1)
				out.write(c);

			in.close();
			out.close();

		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
		finally {
			Database.closeObject(mConn);
		}
	}

	public byte[] openFieldFile(String pCampo, String pTabela, String pFiltro, boolean pIsThumbFile) throws Exception {
		byte[] bytes = null;
		Connection mConn = lcdb.openConnection();
		try {
			File lFileIn = new File(getFileNameByTable(pTabela, pFiltro, mConn, pIsThumbFile));
			InputStream is = new FileInputStream(lFileIn);

			long length = lFileIn.length();

			bytes = new byte[(int) length];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && ( numRead = is.read(bytes, offset, bytes.length - offset) ) >= 0) {
				offset += numRead;
			}

			is.close();
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
		finally {
			Database.closeObject(mConn);
		}
		return bytes;
	}

	public InputStream openInputStream(Connection pConn, String pCampo, String pTabela, String pFiltro, boolean pIsThumbFile) throws Exception {
		InputStream is = null;
		try {
			File lFileIn = new File(getFileNameByTable(pTabela, pFiltro, pConn, pIsThumbFile));
			is = new FileInputStream(lFileIn);
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
		return is;
	}

	public boolean writeToDatabase(Object pBlob, String pPath, boolean pAutoDelete) throws Exception {
		return true;
	}

	public boolean writeToDatabase(Object pBlob, byte[] pFile) throws Exception {
		return true;
	}

	public void writeToFile(Object pBlob, String pPath) throws Exception {
	}

	public boolean saveThumb(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception {
		boolean lReturn = false;

		try {
			FileOutputStream lFileOut = new FileOutputStream(getFileNameByTable(pTabela, pFiltro, pConn, true));
			lFileOut.write(BlobControl.geraThumb(pFile));
			lFileOut.close();
			lReturn = true;
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());

		}

		return lReturn;
	}

}
