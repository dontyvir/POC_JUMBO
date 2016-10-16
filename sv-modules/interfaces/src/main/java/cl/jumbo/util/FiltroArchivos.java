/*
 * Created on May 25, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FiltroArchivos implements FilenameFilter {

	private String maskFileName = null;
	private String maskFileExtension = null;
	
	public FiltroArchivos(String maskFileName, String maskFileExtension) {
		super();
		this.maskFileName = maskFileName;
		this.maskFileExtension = maskFileExtension;
	}

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String name) {
		String[] nombreSegmentado = name.split("\\.");
		if (nombreSegmentado.length >= 1) {
			String mantiza = nombreSegmentado[0];
			for (int i = 1; i < (nombreSegmentado.length - 1); i++)
				mantiza += "." + nombreSegmentado[i];
			String extension = nombreSegmentado[nombreSegmentado.length - 1];
			if ((maskFileName == null) && (extension.matches(maskFileExtension))) 
				return true;
			else if ((maskFileExtension == null) && (mantiza.matches(maskFileName)))
				return true;
			else if ((mantiza.matches(maskFileName)) && (extension.matches(maskFileExtension)))
				return true;
		}
		return false;
	}

}
