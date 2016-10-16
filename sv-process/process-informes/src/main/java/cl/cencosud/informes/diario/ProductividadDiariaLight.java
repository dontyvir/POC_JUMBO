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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import cl.cencosud.informes.datos.Configuracion;
import cl.cencosud.informes.datos.Local;
import cl.cencosud.util.Db;
import cl.cencosud.util.Fechas;


/**
 * @author Victor
 */
public class ProductividadDiariaLight extends ProductividadDiaria{
 
   private final static Logger log = Logger.getLogger(ProductividadDiariaLight.class.getName()); 
   public File generarL(Connection con, String path, final GregorianCalendar dia) throws Exception {
      log.info("inicio proceso productividad light");
      
      GregorianCalendar desdeLunes = (GregorianCalendar) dia.clone();
      desdeLunes.add(Calendar.DAY_OF_YEAR, -DIAS_ATRAS);
      // + 20121227 SBernal
      File archivoL = new File(path + "produccion-L-dia-" +  sdf.format(new Date(dia.getTimeInMillis()-1)) + ".xls");
      // - 20121227 SBernal
      File plantilla = new File(path + "plantilla-diaria-produccion-L.xls");
   
      log.info("desde el dia:"+new Date(desdeLunes.getTimeInMillis()));
      log.info("hasta el dia:"+new Date(dia.getTimeInMillis()));

      Hashtable localesMapL = Db.getLocalesMap(con, LIGHT);
      Object[][] prodPorLocalL = produccionPorLocal(con, desdeLunes, dia, localesMapL, LIGHT);
      Object[][] prodPorLocalLCaja = produccionPorLocalCaja(con, desdeLunes, dia, localesMapL);

      desdeLunes = (GregorianCalendar) dia.clone();
      desdeLunes.add(Calendar.DAY_OF_YEAR, -DIAS_ATRAS);//menos dias pikeados
      
      Object[][] prodPorPickL = produccionPorPickeador(con, dia, localesMapL, LIGHT);
      
      List locales = Db.getLocales(con, LIGHT);
      imprimirL(archivoL, plantilla, locales, prodPorLocalL, prodPorLocalLCaja, prodPorPickL);
      
      log.info("fin proceso productividad light");
      return archivoL;
      
   }
   
   private void imprimirL(File archivo, File plantilla, List locales, Object[][] produccion,
   	  Object[][] prodPorLocalCaja, Object[][] prodPorPick) throws IOException {
   	  FileOutputStream out = new FileOutputStream(archivo);
      HSSFWorkbook objWB = planillaExcelL(plantilla, locales, produccion, prodPorLocalCaja, prodPorPick);
      objWB.write(out);
      out.close();
   }

   private HSSFWorkbook planillaExcelL(File plantilla, List locales, Object[][] produccion, 
		   Object[][] prodPorLocalCaja, Object[][] prodPorPick) throws IOException {
      InputStream myxls = new FileInputStream(plantilla);
      HSSFWorkbook objWB = new HSSFWorkbook(myxls);
      
      HSSFSheet hoja1 = objWB.getSheetAt(0);
      setFotter(hoja1);
      
      HSSFSheet hoja2 = objWB.getSheetAt(1);
      setFotter(hoja2);
      
      HSSFSheet hoja3 = objWB.getSheetAt(2);
      setFotter(hoja3);
      
      //produccion por local L
      hojaProdLocalLight(locales, produccion, hoja1);
      //produccion por local L caja
      hojaProdLocalLightCaja(locales, prodPorLocalCaja, hoja2);
      //produccion por pickeador L
      hojaProdPickeadores(prodPorPick, objWB, hoja3);
      return objWB;
   }
   
   /**
    * @param locales
    * @param produccionL
    * @param objWB
    * @param hoja1
    * @return
    */
   private void hojaProdLocalLight(List locales, Object[][] produccionL, HSSFSheet hoja1) {
      int fil = 1;
      //Titulo: Dias
      HSSFRow fila = hoja1.getRow(fil);
      HSSFCell celda = fila.getCell(0);
      celda.setCellType(HSSFCell.CELL_TYPE_STRING);
      celda.setCellValue(new HSSFRichTextString("Dia"));

      //Titulo locales
      for (int i = 0; i < locales.size(); i++) {
         Local local = (Local) locales.get(i);
         log.debug("<metodo hojaProdLocalLight> Local i+1:"+local.getNombre()+" posicion "+(i+1));
         celda = fila.getCell(i + 1);
         
         if(celda==null)
        	celda=fila.createCell(i + 1);
         
         celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         celda.setCellValue(new HSSFRichTextString(local.getNombre()));
         
      }

      hoja1.setColumnWidth(0, 256 * 15);

      for (int i = 0; i < produccionL.length; i++) {
         fila = hoja1.getRow((short) (i + fil + 1));
         
         if(fila==null)
        	 fila = hoja1.createRow((i + fil + 1));
         
         for (int j = 0; j < produccionL[i].length; j++) {
            celda = fila.getCell(j);

            if(celda==null)
            	celda=fila.createCell(j);
           	 
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            if (produccionL[i][j] == null)
               celda.setCellValue(new HSSFRichTextString(""));
            else if (produccionL[i][j] instanceof String)
               celda.setCellValue(new HSSFRichTextString((String) produccionL[i][j]));
            else if (produccionL[i][j] instanceof Integer)
               celda.setCellValue(((Integer) produccionL[i][j]).intValue());
            else
               celda.setCellValue(((BigDecimal) produccionL[i][j]).doubleValue());
         }
      }
   }

   private void hojaProdLocalLightCaja(List locales, Object[][] produccionL, HSSFSheet hoja3) {
      int columna = 0;
      int fil = 1;

      //Titulo: semana
      HSSFRow fila = hoja3.getRow(fil);
      HSSFCell celda = fila.getCell(columna);
      celda.setCellType(HSSFCell.CELL_TYPE_STRING);
      celda.setCellValue(new HSSFRichTextString("Dia"));

      //Titulo locales
      for (int i = 0; i < locales.size(); i++) {
         Local local = (Local) locales.get(i);
         log.debug("<metodo hojaProdLocalLightCaja> columna + i + 1:"+columna + i + 1);
         
         celda = fila.getCell(columna + i + 1);
         if(celda==null)
        	 celda = fila.createCell(columna + i + 1);
         
         celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         celda.setCellValue(new HSSFRichTextString(local.getNombre()));
      }

      hoja3.setColumnWidth(0, 256 * 15);

      for (int i = 0; i < produccionL.length; i++) {
         fila = hoja3.getRow((short) (i + fil + 1));
       
         if(fila==null)
        	 fila = hoja3.createRow((short) (i + fil + 1));
         
         for (int j = 0; j < produccionL[i].length; j++) {
        	celda = fila.getCell(j + columna);
            
            if(celda==null)
           	 celda = fila.createCell(j + columna);
            
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
            if (produccionL[i][j] == null)
               celda.setCellValue(new HSSFRichTextString(""));
            else if (produccionL[i][j] instanceof String)
               celda.setCellValue(new HSSFRichTextString((String) produccionL[i][j]));
            else if (produccionL[i][j] instanceof Integer)
               celda.setCellValue(((Integer) produccionL[i][j]).intValue());
            else
               celda.setCellValue(((BigDecimal) produccionL[i][j]).doubleValue());
         }
      }
   }
   
   private Object[][] produccionPorLocalCaja(Connection con, GregorianCalendar desdeLunes, GregorianCalendar hastaDomingo,
         Hashtable locales) throws SQLException {

	   int ultimoDia = Db.getDia(con, hastaDomingo);
	   Object[][] produccion = new Object[DAYS_YEAR][locales.size() + 1];

      Configuracion conf = Db.getConfiguracion(con);
    
      String sql = SQL_PRODUCCIONPORLOCALCAJA;

      PreparedStatement ps = con.prepareStatement(sql + " With UR");
      ps.setDate(1, new java.sql.Date(desdeLunes.getTimeInMillis()));
      ps.setDate(2, new java.sql.Date(hastaDomingo.getTimeInMillis()));
      ps.setInt(3, conf.getTiempoMinimoRonda());
      ps.setInt(4, conf.getProductividadMaximaRonda());
      ps.setString(5, LIGHT);

      ResultSet rs = ps.executeQuery();
      //supuesto: está ordenado por local, no son más de 52 semanas
      int i=0;
      while (rs.next()) {
    	 i++;
    	  
         Integer localId = new Integer(rs.getInt("local_id"));
         
         int col= getColumeExcel(ultimoDia, rs.getInt("w"),false);
         
         log.debug("<metodo produccionPorLocal> posicion dia "+ col +" local id "+localId+" fecha para posicionar: "+rs.getDate("fecha"));
         
         if (produccion[col][0] == null) {
            produccion[col][0] = Fechas.getDiaMes(rs.getDate("fecha"));
         }
         
         if(locales.get(localId) != null){
            produccion[col][((Local) locales.get(localId)).getOrden()] = rs.getBigDecimal("ph");
         }
      }
      
      log.debug("<metodo produccionPorLocalCaja> cantidad de registros cargos:"+i);
      
      Db.close(rs, ps);
      return produccion;
   }
   
}
