
package br.com.manchestercity.automacao;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.imageio.ImageIO;

public abstract class BlobControl {

	public Database lcdb;
	public String mPath;

	public BlobControl(User pUser) {
		lcdb = new Database(pUser);
		mPath = IniManipulation.getProperty(IniManipulation.FILE_SYSTEM_PATH);
	}

	public abstract boolean saveField(String pCampo, String pTabela, String pFiltro, String pPath, boolean pAutoDelete) throws Exception;

	public abstract boolean saveField(String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception;

	public abstract boolean saveField(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception;

	public abstract boolean saveThumb(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile) throws Exception;

	public abstract boolean saveField(String pCampo, String pTabela, String pFiltro, byte[] pFile, boolean pCreateThumFile) throws Exception;

	public abstract boolean saveField(Connection pConn, String pCampo, String pTabela, String pFiltro, byte[] pFile, boolean pCreateThumFile) throws Exception;

	public abstract void openField(String pCampo, String pTabela, String pFiltro, String pPath) throws Exception;

	public abstract byte[] openFieldFile(String pCampo, String pTabela, String pFiltro) throws Exception;

	public abstract InputStream openInputStream(Connection pConn, String pCampo, String pTabela, String pFiltro) throws Exception;

	public abstract void openField(String pCampo, String pTabela, String pFiltro, String pPath, boolean pIsThumbFile) throws Exception;

	public abstract byte[] openFieldFile(String pCampo, String pTabela, String pFiltro, boolean pIsThumbFile) throws Exception;

	public abstract InputStream openInputStream(Connection pConn, String pCampo, String pTabela, String pFiltro, boolean pIsThumbFile) throws Exception;

	public abstract boolean writeToDatabase(Object pBlob, String pPath, boolean pAutoDelete) throws Exception;

	public abstract boolean writeToDatabase(Object pBlob, byte[] pFile) throws Exception;

	public abstract void writeToFile(Object pBlob, String pPath) throws Exception;

	public boolean deleteFile(String pFileName) throws Exception {
		boolean commit = true;
		if (mPath != null && mPath.length() > 0) {
			File file = new File(pFileName);
			if (file.exists()) {
				commit = file.delete();
			}
		}
		return commit;
	}

	public String getFileNameByTable(String pTabela, String pFiltro, Connection pConn) {
		return getFileNameByTable(pTabela, pFiltro, pConn, false);
	}

	public String getFileNameByTable(String pTabela, String pFiltro, Connection pConn, boolean isThumb) {
		ResultSet rset = null;

		String lReturn = "";
		String lStrQuery = "";
		String[] lArr;
		try {

			String lAux = pFiltro.toUpperCase();
			if (lAux.indexOf(" AND ") > 0) {
				lArr = lAux.split(" AND ");
				for (int i = 0; i <= lArr.length - 1; i++) {
					int indexOf = lArr[i].trim().indexOf("=");
					if (indexOf > -1) {
						lArr[i] = getAlias(lArr[i].trim().substring(0, indexOf).trim());
					}
					else {
						lArr[i] = "";
					}
				}
			}
			else {
				lArr = new String[] { getAlias(lAux.substring(0, lAux.indexOf("=")).trim()) };
			}

			java.util.Arrays.sort(lArr);

			lStrQuery = "";
			for (int j = 0; j < lArr.length; j++) {
				if (!lArr[j].equals("")) {
					lStrQuery += ( lStrQuery.equals("") ? "" : " , " ) + lArr[j];
				}
			}
			lStrQuery = "Select " + lStrQuery + " FROM " + pTabela + " WHERE " + pFiltro;

			rset = lcdb.openResultSet(lStrQuery, pConn);

			lReturn = mPath + "/" + pTabela;

			if (rset.next()) {
				for (int i = 0; i < lArr.length; i++) {
					if (!lArr[i].equals("")) {
						lReturn += "_" + Database.verifyNull(rset.getObject(lArr[i]));
					}
				}
			}

			lReturn += ( isThumb ? "_thumb" : "" ) + ".rpi";
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
		}

		Database.closeObject(rset);

		return lReturn;
	}

	public static BufferedImage scale(BufferedImage source) {
		int w = 150;
		int h = 100;
		BufferedImage bi = getCompatibleImage(w, h);
		Graphics2D g2d = bi.createGraphics();
		double xScale = (double) w / source.getWidth();
		double yScale = (double) h / source.getHeight();
		AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
		g2d.drawRenderedImage(source, at);
		g2d.dispose();
		return bi;
	}

	public static BufferedImage getCompatibleImage(int w, int h) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage image = gc.createCompatibleImage(w, h);
		return image;
	}

	public static byte[] geraThumb(byte[] imagemOriginal) throws InterruptedException, IOException {
		Image imagem = Toolkit.getDefaultToolkit().createImage(imagemOriginal);
		imagem = imagem.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
		imagemOriginal = null;
		System.gc();
		MediaTracker mediaTracker = new MediaTracker(new Container());
		mediaTracker.addImage(imagem, 0);
		mediaTracker.waitForID(0);

		// define a qualidade da imagem
		// int qualidade = 100; // 100% :: Desnecessário devido à remoção do JPEGCodec

		// define a largura e altura do thumbnail
		int largura = 150;
		int altura = 100;
		double thumbRatio = (double) largura / (double) altura;
		int larguraImagem = imagem.getWidth(null);
		int alturaImagem = imagem.getHeight(null);
		double imageRatio = (double) larguraImagem / (double) alturaImagem;
		if (thumbRatio < imageRatio) {
			altura = (int) ( largura / imageRatio );
		}
		else {
			largura = (int) ( altura * imageRatio );
		}
		// Desenha a imagem original para o thumbnail e
		// redimensiona para o novo tamanho

		BufferedImage thumbImage = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(imagem, 0, 0, largura, altura, null);

		// Salva a nova imagem
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(thumbImage, "JPEG", out);

		// Antigo método de conversão para JPEG
		// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		// JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
		// param.setQuality((float) qualidade / 100.0f, false);
		// encoder.setJPEGEncodeParam(param);
		// encoder.encode(thumbImage);

		byte[] imagemThumb = out.toByteArray();
		out.close();

		return imagemThumb;

	}

	private String getAlias(String pStr) {
		String ret = "";

		ret = pStr;

		if (pStr.indexOf(".") >= 0) {
			String[] arr = pStr.split("\\.");
			ret = arr[arr.length - 1];
		}

		if (ret.indexOf(" ") >= 0) {
			String[] arr = pStr.split(" ");
			ret = arr[arr.length - 1];
		}

		return ret;
	}

}
