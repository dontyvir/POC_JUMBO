package cl.cencosud.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class FileGzip {

	public void unGunzipFile(String compressedFile, String decompressedFile)throws IOException, Exception {
		byte[] buffer = new byte[1024];	
		FileInputStream fileIn = new FileInputStream(compressedFile);
		GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);	
		//FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
		int bytes_read;
		while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {			
			//fileOutputStream.write(buffer, 0, bytes_read);
			break;
		}
		gZIPInputStream.close();
		//fileOutputStream.close();
	}
	
}
