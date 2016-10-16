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
public class FiltroArchivosInverso implements FilenameFilter {

	private String maskFileName = null;
	private String maskFileExtension = null;
	
	public FiltroArchivosInverso(String maskFileName, String maskFileExtension) {
		super();
		this.maskFileName = maskFileName;
		this.maskFileExtension = maskFileExtension;
	}

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String name) {
		String[] nombreSegmentado = name.split("\\.");
		if (nombreSegmentado.length > 1) {
			String mantiza = nombreSegmentado[0];
			String extension = nombreSegmentado[1];
			if (maskFileName == null) {
				if (!(extension.equalsIgnoreCase(maskFileExtension)))
					return true;
				else
					return false;
			} else if (maskFileExtension == null) {
				if (!(mantiza.toUpperCase().startsWith(maskFileName)))
					return true;
				else
					return false;
			} else if (!(mantiza.toUpperCase().startsWith(maskFileName) && !(extension.equalsIgnoreCase(maskFileExtension))))
				return true;
		}
		return false;
	}

}
