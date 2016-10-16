/*
 * Created on 21-ago-2009
 */
package cl.cencosud.informes.diario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.cencosud.informes.datos.Local;
import cl.cencosud.util.Db;

/**
 * @author Victor
 */
public class ProductividadDiariaNormal extends ProductividadDiaria {
	
	private final static Logger log = Logger.getLogger(ProductividadDiariaNormal.class.getName());
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   
	public File generarN(Connection con, String path, final GregorianCalendar dia) throws Exception {
	  log.info("inicio proceso productividad normal");
      
      GregorianCalendar diaAnterior = (GregorianCalendar) dia.clone();
      diaAnterior.add(Calendar.DAY_OF_YEAR, -DIAS_ATRAS);
      
      int diaNombre = dia.get(Calendar.DAY_OF_YEAR);
      // + 20121227 SBernal
      File archivoN = new File(path + "produccion-N-dia-" +  sdf.format(new Date(dia.getTimeInMillis()-1)) + ".xls");
      // - 20121227 SBernal
      File plantilla = new File(path + "plantilla-diaria-produccion-N.xls");
          
      log.info("desde: "  + sdf.format(diaAnterior.getTime()));
      log.info("hasta: "  + sdf.format(dia.getTime()));
      log.info("dia: " + diaNombre);
      log.info("diaAntes: " + diaAnterior.get(Calendar.DAY_OF_YEAR)); 
      log.debug("dias de diferencia "+ (dia.get(Calendar.DAY_OF_YEAR) - diaAnterior.get(Calendar.DAY_OF_YEAR)));
           
      Hashtable localesMapN = Db.getLocalesMap(con, "N");
      Object[][] prodPorLocalN = produccionPorLocal(con, diaAnterior, dia, localesMapN, NORMAL);

      diaAnterior = (GregorianCalendar) dia.clone();
      diaAnterior.add(Calendar.DAY_OF_YEAR, -DAY_PICK); //dia menos los dias pick
      
      Object[][] prodPorPickN = produccionPorPickeador(con, dia, localesMapN, NORMAL);  
      List locales = Db.getLocales(con, "N");
     
      imprimirN(archivoN, plantilla, locales, prodPorLocalN, prodPorPickN);
      
      log.info("fin proceso productividad normal");
      
      return archivoN;
   }

   
   private void imprimirN(File archivo, File plantilla, List locales, Object[][] produccion, Object[][] prodPorPick) throws IOException {
      FileOutputStream out = new FileOutputStream(archivo);
      HSSFWorkbook objWB = planillaExcelN(plantilla, locales, produccion, prodPorPick);
      objWB.write(out);
      out.close();
   }
  

   private HSSFWorkbook planillaExcelN(File plantilla, List locales, Object[][] produccion, Object[][] prodPorPick)throws IOException {
      
   	  InputStream myxls = new FileInputStream(plantilla);
      HSSFWorkbook objWB = new HSSFWorkbook(myxls);
      HSSFSheet hoja1 = objWB.getSheetAt(0);
      setFotter(hoja1);

      HSSFSheet hoja2 = objWB.getSheetAt(1);
      setFotter(hoja2);
      
      //produccion por local Normal
      hojaProdLocalNormal(locales, produccion, hoja1);
      //produccion por pickeador N
      hojaProdPickeadores(prodPorPick, objWB, hoja2);
      
      return objWB;
   }

   
   /**
    * @param locales
    * @param produccionN
    * @param hoja1
    */
   private void hojaProdLocalNormal(List locales, Object[][] produccionN, HSSFSheet hoja1) {
      //Titulo: día
      HSSFRow fila = hoja1.getRow(0);
      HSSFCell celda = fila.getCell(0);
      celda.setCellType(HSSFCell.CELL_TYPE_STRING);
      celda.setCellValue(new HSSFRichTextString("Dia"));

      //Titulo locales
      for (int i = 0; i < locales.size(); i++) {
         Local local = (Local) locales.get(i);
         celda = fila.getCell(i + 1);
         celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         celda.setCellValue(new HSSFRichTextString(local.getNombre()));
      }
      
      //for (int i = 0; i < produccionN.length; i++) {
      for (int i = 0; i < produccionN.length; i++) {
         fila = hoja1.getRow((short) (i + 1));
         
         if(fila==null){
        	 fila = hoja1.createRow((i + 1));	 
         }
         
         for (int j = 0; j < produccionN[i].length; j++) {
         	celda = fila.getCell(j);
         	
         	if(celda==null)
         		celda = fila.createCell(j);
         	
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            if (produccionN[i][j] == null)
               celda.setCellValue(new HSSFRichTextString(""));
            else if (produccionN[i][j] instanceof String)
               celda.setCellValue(new HSSFRichTextString((String) produccionN[i][j]));
            else if (produccionN[i][j] instanceof Integer)
               celda.setCellValue(((Integer) produccionN[i][j]).intValue());
            else
               celda.setCellValue(((BigDecimal) produccionN[i][j]).doubleValue());
         }
      }  
   }
}
