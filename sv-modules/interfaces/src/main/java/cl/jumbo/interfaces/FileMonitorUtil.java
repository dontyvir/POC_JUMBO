package cl.jumbo.interfaces;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import cl.jumbo.interfaces.exceptions.ProcesoInterfazException;
import cl.jumbo.util.FiltroArchivos;
import cl.jumbo.util.GZUncompressor;

/**
 * @author jvillalobos
 */
public class FileMonitorUtil {
	
	public FileMonitorUtil() {
	}
	
	public void copyFile(File source, File target) throws IOException {
	    char c = '\u0400';
	    byte abyte0[] = new byte[c];
	    FileInputStream fileinputstream = new FileInputStream(source);
	    BufferedInputStream bufferedinputstream = new BufferedInputStream(fileinputstream);
	    FileOutputStream fileoutputstream = new FileOutputStream(target);
	    BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(fileoutputstream);
	    int i;
	    while((i = fileinputstream.read(abyte0)) != -1) {
	        fileoutputstream.write(abyte0, 0, i);
	    }
	    bufferedoutputstream.close();
	    bufferedinputstream.close();
	}
	
	public File[] findFiles(File directory, String sufix, String extension) {
		FiltroArchivos filter = new FiltroArchivos(sufix, extension);
		File[] result = directory.listFiles(filter);
		return result;
	}
	
	public void moveFile(File source, File target) throws ProcesoInterfazException {
		if (source != null) {
			if (source.exists()) {
				try {
					copyFile(source, target);
				} catch (IOException e) {
					throw new ProcesoInterfazException("Error al copiar archivo " + source.getAbsolutePath());
				}
				source.delete();
			} else {
				throw new ProcesoInterfazException("Archivo " + source.getAbsolutePath() + " a mover, no existe!");
			}
		} else {
			throw new ProcesoInterfazException("No hay archivo a mover");
		}
	}
	
	public Vector uncompressFilesSAP(File[] files, File directoryDestiny) throws ProcesoInterfazException {
		return uncompressFiles(files, null, directoryDestiny);
	}

	public Vector uncompressFiles(File[] filesGZ, String[] newNamesFile, File directoryDestiny) throws ProcesoInterfazException {
		Vector exceptions = new Vector();
		if ((filesGZ != null) && (filesGZ.length > 0)) {
			for (int i = 0; i < filesGZ.length; i++) {
				if (filesGZ[i].exists()) {
					String uncompressName = filesGZ[i].getName().replaceAll("\\.gz", "");
					if ((newNamesFile != null) && (newNamesFile.length == filesGZ.length))
						uncompressName = newNamesFile[i];
					File uncompressFile = new File(directoryDestiny + File.separator + uncompressName);
					try {
						GZUncompressor.uncompress(filesGZ[i], uncompressFile);
						filesGZ[i].delete();
					} catch (FileNotFoundException e) {
						exceptions.addElement(new Exception("Archivo " + filesGZ[i].getAbsolutePath() + " no encontrado!"));
					} catch (IOException e) {
						if (filesGZ[i].length() == 0) {
							exceptions.addElement(new Exception("Tamaño archivo: " + filesGZ[i].getAbsolutePath() + " = 0.0 KB"));
						} else {
							exceptions.addElement(new Exception(uncompressFile.getAbsolutePath() + " sin derechos de acceso"));
						}
					}
				} else {
					exceptions.addElement(new Exception("Archivo " + filesGZ[i].getAbsolutePath() + " no existe!"));
				}
			}
		} else {
			exceptions.addElement(new Exception("No hay archivos a descomprimir"));
		}
		
		return exceptions;
	}
	
	public void uncompressFiles(File fileGZ, String newNameFile, File directoryDestiny) throws ProcesoInterfazException {
		if (fileGZ != null) {
			if (fileGZ.exists()) {
				String uncompressName = fileGZ.getName().replaceAll("\\.gz", "");
				if (newNameFile != null)
					uncompressName = newNameFile;
				File uncompressFile = new File(directoryDestiny + File.separator + uncompressName);
				try {
					GZUncompressor.uncompress(fileGZ, uncompressFile);
					fileGZ.delete();
				} catch (FileNotFoundException e) {
					throw new ProcesoInterfazException("Archivo " + fileGZ.getName() + " no encontrado!");
				} catch (IOException e) {
					if (fileGZ.length() == 0) {
						throw new ProcesoInterfazException("Tamaño archivo: " + fileGZ.getName() + " = 0.0 KB");
					} else {
						throw new ProcesoInterfazException(uncompressFile.getName() + " sin derechos de acceso");
					}
				}
			} else {
				throw new ProcesoInterfazException("Archivo " + fileGZ.getName() + " no existe!");
			}
		} else {
			throw new ProcesoInterfazException("No hay archivo a descomprimir");
		}
	}

	public static void main(String[] args) {
	}
	
}
