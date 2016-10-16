/*
 * Creado el 23-11-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.boc.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.bbr.log.Logging;

/**
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class Planilla {
    
    protected Logging logger = new Logging(this);    
    private HSSFWorkbook workbook = new HSSFWorkbook();
	private FileInputStream inputfile;
	private FileOutputStream outputfile;

	public Planilla() {
	}

	/*
	 * 
	 * Creamos el workbook que sirve para leer el excel, le pasamos un parametro
	 * que es la path completo de donde se encuentra en excel
	 *  
	 */
	public HSSFWorkbook getWorkbook(String s) {

		try {
			inputfile = new FileInputStream(s);
			workbook = new HSSFWorkbook(inputfile);
		} catch (Exception e) {
		    logger.error("Error al crear la planilla excel: " + e.getMessage());
		}
		return workbook;
	}

}
