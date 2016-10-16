/*
 * Created on May 26, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GZUncompressor {

	public GZUncompressor() {
		super();
	}
	
	public static void uncompress(File gzFile, File newFile) throws FileNotFoundException, IOException {
		GZIPInputStream gzipInputStream;
		gzipInputStream = new GZIPInputStream(new FileInputStream(gzFile.getAbsolutePath()));
		OutputStream out = new FileOutputStream(newFile.getAbsolutePath()); 
		byte[] buf = new byte[1024]; 
		int len; 
		while ((len = gzipInputStream.read(buf)) >= 0) 
			out.write(buf, 0, len); 
		gzipInputStream.close(); 
		out.close(); 
	}

}
