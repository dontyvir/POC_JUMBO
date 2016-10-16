/*
 * Created on 05-Nov-2013
 */
package cl.cencosud.informes.manual;

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
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import cl.cencosud.informes.Estilos;
import cl.cencosud.informes.datos.Configuracion;
import cl.cencosud.informes.datos.Local;
import cl.cencosud.util.Db;
import cl.cencosud.util.Fechas;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 */
public class ProductividadManual {
   private final static Logger log = Logger.getLogger(ProductividadManual.class.getName());
   
   final static int WEEKS_YEAR = 53; //semanas del año como máximo
  final static int WEEKS_PICK = Integer.parseInt(Parametros.getString("weeks.pick")); //últimas semanas de produccion para informe pickedores
   final static int CONSECUTIVE_WEEKS =Integer.parseInt(Parametros.getString("consecutive.weeks"));
   final static String NORMAL = "N";
   final static String LIGHT = "L";

   /**
    * 
    * @param con
    * @param path
    * @param hastaDomingo
    *           Domingo de la última semana
 * @param paramDTO 
    * @return
    * @throws Exception
    */
   public File generarN(Connection con, String path, GregorianCalendar hastaDomingo, ParamDTO paramDTO) throws Exception {
      log.info("... Generar Productividad-N");
      GregorianCalendar desdeLunes =null;

	  // ENERO = 0
	  desdeLunes = new GregorianCalendar();
      desdeLunes.setFirstDayOfWeek(Calendar.MONDAY);
      //TODO DESDE
	  desdeLunes.set(paramDTO.getAgno_ini(), paramDTO.getMes_ini()-1, paramDTO.getDia_ini());
	  
      //sólo para el nombre del archivo
      Hashtable localesMapN = Db.getLocalesMap(con, "N");
      Object[][] prodPorLocalN = produccionPorLocal(con, desdeLunes, hastaDomingo, localesMapN, NORMAL);

      desdeLunes = (GregorianCalendar) hastaDomingo.clone();
      desdeLunes.add(Calendar.WEEK_OF_YEAR, -WEEKS_PICK); //domingo hace 8 semanas
      desdeLunes.add(Calendar.DAY_OF_YEAR, 1); //lunes hace 8 semanas

      Object[][] prodPorPickN = produccionPorPickeador(con, desdeLunes, hastaDomingo, localesMapN, NORMAL);
      List locales = Db.getLocales(con, "N");
      log.info("... [Generar] - produccion-N-semana-" + hastaDomingo.get(Calendar.WEEK_OF_YEAR) + ".xls");
      File archivoN = new File(path + "produccion-N-semana-" + hastaDomingo.get(Calendar.WEEK_OF_YEAR) + ".xls");
      File plantilla = new File(path + "plantilla-produccion-N.xls");
      imprimirN(archivoN, plantilla, locales, prodPorLocalN, prodPorPickN);
      log.info("... [OK] - produccion-N-semana-" + hastaDomingo.get(Calendar.WEEK_OF_YEAR) + ".xls");
      return archivoN;
   }

   public File generarL(Connection con, String path, GregorianCalendar hastaDomingo, ParamDTO paramDTO) throws Exception {
      log.info("... Generar Productividad-L");
      GregorianCalendar desdeLunes =null;

	  // ENERO = 0
	  desdeLunes = new GregorianCalendar();
      desdeLunes.setFirstDayOfWeek(Calendar.MONDAY);
      //TODO DESDE
      desdeLunes.set(paramDTO.getAgno_ini(), paramDTO.getMes_ini()-1, paramDTO.getDia_ini());

      
      //sólo para el nombre del archivo
      
      Hashtable localesMapL = Db.getLocalesMap(con, "L");

      Object[][] prodPorLocalL = produccionPorLocal(con, desdeLunes, hastaDomingo, localesMapL, LIGHT);
      Object[][] prodPorLocalLCaja = produccionPorLocalCaja(con, desdeLunes, hastaDomingo, localesMapL);
      desdeLunes = (GregorianCalendar) hastaDomingo.clone();
      desdeLunes.add(Calendar.WEEK_OF_YEAR, -WEEKS_PICK); //domingo hace 8 semanas
      desdeLunes.add(Calendar.DAY_OF_YEAR, 1); //lunes hace 8 semanas
      Object[][] prodPorPickL = produccionPorPickeador(con, desdeLunes, hastaDomingo, localesMapL, LIGHT);

      List locales = Db.getLocales(con, "L");
      log.info("... [Generar] - produccion-N-semana-" + hastaDomingo.get(Calendar.WEEK_OF_YEAR) + ".xls");
      File archivoL = new File(path + "produccion-L-semana-" + hastaDomingo.get(Calendar.WEEK_OF_YEAR) + ".xls");
      File plantilla = new File(path + "plantilla-produccion-L.xls");
      imprimirL(archivoL, plantilla, locales, prodPorLocalL, prodPorLocalLCaja, prodPorPickL);
      log.info("... [OK] - produccion-N-semana-" + hastaDomingo.get(Calendar.WEEK_OF_YEAR) + ".xls");
      return archivoL;
   }

   private void imprimirN(File archivo, File plantilla, List locales, Object[][] produccion, Object[][] prodPorPick)
         throws IOException {
      FileOutputStream out = new FileOutputStream(archivo);
      HSSFWorkbook objWB = planillaExcelN(plantilla, locales, produccion, prodPorPick);
      objWB.write(out);
      out.close();
   }

   private void imprimirL(File archivo, File plantilla, List locales, Object[][] produccion,
         Object[][] prodPorLocalCaja, Object[][] prodPorPick) throws IOException {
      FileOutputStream out = new FileOutputStream(archivo);
      HSSFWorkbook objWB = planillaExcelL(plantilla, locales, produccion, prodPorLocalCaja, prodPorPick);
      objWB.write(out);
      out.close();
   }

   private HSSFWorkbook planillaExcelN(File plantilla, List locales, Object[][] produccion, Object[][] prodPorPick)
         throws IOException {
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

   private HSSFWorkbook planillaExcelL(File plantilla, List locales, Object[][] produccion,
         Object[][] prodPorLocalCaja, Object[][] prodPorPick) throws IOException {
      InputStream myxls = new FileInputStream(plantilla);
      HSSFWorkbook objWB = new HSSFWorkbook(myxls);
      HSSFSheet hoja1 = objWB.getSheetAt(0);
      setFotter(hoja1);
      HSSFSheet hoja2 = objWB.getSheetAt(1);
      setFotter(hoja2);
      //produccion por local L
      hojaProdLocalLight(locales, produccion, hoja1);
      //produccion por local L caja
      hojaProdLocalLightCaja(locales, prodPorLocalCaja, hoja1);
      //produccion por pickeador L
      hojaProdPickeadores(prodPorPick, objWB, hoja2);
      return objWB;
   }

   /**
    * @param locales
    * @param produccionN
    * @param hoja1
    */
   private void hojaProdLocalNormal(List locales, Object[][] produccionN, HSSFSheet hoja1) {
      //Titulo: semana
      HSSFRow fila = hoja1.getRow(0);
      HSSFCell celda = fila.getCell(0);
      celda.setCellType(HSSFCell.CELL_TYPE_STRING);
      celda.setCellValue(new HSSFRichTextString("Semana"));

      //Titulo locales
      for (int i = 0; i < locales.size(); i++) {
         Local local = (Local) locales.get(i);
         celda = fila.getCell(i + 1);
         celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         celda.setCellValue(new HSSFRichTextString(local.getNombre()));
      }
      //hoja1.setColumnWidth(0, 256 * 16);

      //produccion por local N
      for (int i = 0; i < produccionN.length; i++) {
         fila = hoja1.getRow((short) (i + 1));
         for (int j = 0; j < produccionN[i].length; j++) {
            celda = fila.getCell(j);
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

   /**
    * @param locales
    * @param produccionL
    * @param objWB
    * @param hoja2
    * @return
    */
   private void hojaProdLocalLight(List locales, Object[][] produccionL, HSSFSheet hoja2) {
      int fil = 1;
      //Titulo: semana
      HSSFRow fila = hoja2.getRow(fil);
      HSSFCell celda = fila.getCell(0);
      celda.setCellType(HSSFCell.CELL_TYPE_STRING);
      celda.setCellValue(new HSSFRichTextString("Semana"));

      //Titulo locales
      for (int i = 0; i < locales.size(); i++) {
         Local local = (Local) locales.get(i);
         celda = fila.getCell(i + 1);
         celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         celda.setCellValue(new HSSFRichTextString(local.getNombre()));         
      }

      hoja2.setColumnWidth(0, 256 * 15);

      for (int i = 0; i < produccionL.length; i++) {
         fila = hoja2.getRow((short) (i + fil + 1));
         for (int j = 0; j < produccionL[i].length; j++) {
            celda = fila.getCell(j);
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

   private void hojaProdLocalLightCaja(List locales, Object[][] produccionL, HSSFSheet hoja2) {
      int columna = 6;
      int fil = 1;

      //Titulo: semana
      HSSFRow fila = hoja2.getRow(fil);
      HSSFCell celda = fila.getCell(columna);
      celda.setCellType(HSSFCell.CELL_TYPE_STRING);
      celda.setCellValue(new HSSFRichTextString("Semana"));

      //Titulo locales
      for (int i = 0; i < locales.size(); i++) {
         Local local = (Local) locales.get(i);
         celda = fila.getCell(columna + i + 1);
         if (celda!=null){ 
         	celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         	celda.setCellValue(new HSSFRichTextString(local.getNombre()));
         }
      }

      hoja2.setColumnWidth(0, 256 * 15);

      for (int i = 0; i < produccionL.length; i++) {
         fila = hoja2.getRow((short) (i + fil + 1));
         for (int j = 0; j < produccionL[i].length; j++) {
            celda = fila.getCell(j + columna);
            if (celda != null){
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
   }

   /**
    * @param prodPorPick
    * @param hoja3
    * @param estiloCelda
    * @param estiloCeldaNormal
    */
   private void hojaProdPickeadores(Object[][] prodPorPick, HSSFWorkbook objWB, HSSFSheet hoja3) {
      HSSFRow fila;
      HSSFCell celda;
      HSSFCellStyle estiloTitulo = Estilos.titulo(objWB);
      HSSFCellStyle estiloNumero = Estilos.numero(objWB);
      HSSFCellStyle estiloNumeroDestacado = Estilos.numeroDestacado(objWB);

      HSSFCellStyle estiloCeldaVerde = estiloCelda(objWB, estiloFuente(objWB, HSSFFont.BOLDWEIGHT_NORMAL,
            HSSFColor.GREEN.index), HSSFCellStyle.ALIGN_RIGHT);

      hoja3.setColumnWidth(0, 256 * 15);

      int n = 0;
      for (int i = 0; i < prodPorPick.length; i++) {
         //titulo
         if (prodPorPick[i][0] != null && prodPorPick[i][0] instanceof Local) {
            n++;
            fila = hoja3.createRow((short) n++);
            celda = fila.createCell(0);
            celda.setCellStyle(estiloTitulo);
            celda.setCellType(HSSFCell.CELL_TYPE_STRING);
            celda.setCellValue(new HSSFRichTextString(((Local) prodPorPick[i][0]).getNombre()));
            for (int j = 1; j < prodPorPick[i].length; j++) {
               celda = fila.createCell(j);
               celda.setCellStyle(estiloTitulo);
               celda.setCellType(HSSFCell.CELL_TYPE_STRING);
               celda.setCellValue(new HSSFRichTextString((String) prodPorPick[i][j]));
            }
            continue;
         }

         //eliminar pickeadores con más de 3 semanas consecutivas sin pickear
         int count = 0;
         for (int j = prodPorPick[i].length - 1; j >= 0; j--) {
            if (prodPorPick[i][j] == null) {
               count++;
            }
         }
         if (count > 3)
            continue;

         fila = hoja3.createRow((short) (n++));
         for (int j = 0; j < prodPorPick[i].length; j++) {
            celda = fila.createCell(j);
            celda.setCellStyle(estiloNumero);
            if (prodPorPick[i][j] == null) {
               celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
               celda.setCellValue(new HSSFRichTextString(""));
            } else if (prodPorPick[i][j] instanceof String) {
               celda.setCellType(HSSFCell.CELL_TYPE_STRING);
               celda.setCellValue(new HSSFRichTextString((String) prodPorPick[i][j]));
            } else {
               if (j > 1 && prodPorPick[i][j - 1] != null) {
                  int r = ((BigDecimal) prodPorPick[i][j]).compareTo((BigDecimal) prodPorPick[i][j - 1]);
                  if (r < 0) {
                     celda.setCellStyle(estiloNumeroDestacado);
                  } else if (r > 0) {
                     celda.setCellStyle(estiloCeldaVerde);
                  }
               }
               celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
               celda.setCellValue(((BigDecimal) prodPorPick[i][j]).doubleValue());
            }
         }
      }
   }

   private Object[][] produccionPorPickeador(Connection con, GregorianCalendar desdeLunes,
         GregorianCalendar hastaDomingo, Hashtable locales, String tipoPicking) throws SQLException {

      //int ultimaSemana = hastaDomingo.get(Calendar.WEEK_OF_YEAR);
      //los numeros de semana son distintos en java y base de datos
      int ultimaSemana = Db.getSemana(con, hastaDomingo);
      int addPosition = WEEKS_YEAR - ultimaSemana - 1;

      Configuracion conf = Db.getConfiguracion(con);

      /*String sqlCount = "SELECT local_id, usuario_id FROM BODBA.IPRODUCTIVIDAD       "
            + "where fecha >= ? and fecha <= ? and segundos > ?                       "
            + "  and (cant_pick + cant_pick_sust) * 3600 < ( ? * segundos)  and tipo_pick = '" + tipoPicking + "'"
            + "group by local_id, usuario_id";*/

      //desde un lunes hasta un domingo
      String sql = "SELECT local_id, login, WEEK_ISO(fecha) as w, min(fecha) as fecha,               "
            + "(sum(cant_pick) + sum(cant_pick_sust)) / decimal(sum(segundos) / 3600.0, 10, 3) as ph "
            + "FROM  BODBA.IPRODUCTIVIDAD inner join bodba.bo_usuarios on id_usuario = usuario_id "
            + "where fecha >= ? and fecha <= ? and segundos > ?                         "
            + "  and (cant_pick + cant_pick_sust) * 3600 < ( ? * segundos)                        "
            + "  and tipo_pick = '" + tipoPicking + "'"
            + "group by local_id, login, WEEK_ISO(fecha)                               "
            + "order by local_id, login, w                                             ";

      PreparedStatement ps = con.prepareStatement(Db.getSqlCount(sql));
      ps.setDate(1, new java.sql.Date(desdeLunes.getTimeInMillis()));
      ps.setDate(2, new java.sql.Date(hastaDomingo.getTimeInMillis()));
      ps.setInt(3, conf.getTiempoMinimoRonda());
      ps.setInt(4, conf.getProductividadMaximaRonda());
      ResultSet rs = ps.executeQuery();
      int size = 0;
      if (rs.next())
         size = rs.getInt(1);

      ps = con.prepareStatement(sql + " With UR");
      ps.setDate(1, new java.sql.Date(desdeLunes.getTimeInMillis()));
      ps.setDate(2, new java.sql.Date(hastaDomingo.getTimeInMillis()));
      ps.setInt(3, conf.getTiempoMinimoRonda());
      ps.setInt(4, conf.getProductividadMaximaRonda());
      rs = ps.executeQuery();

      //+locales size: espacio para los locales y semanas
      //+1 espacio para login
      Object[][] produccion = new Object[size + locales.size()][WEEKS_PICK + 1];

      int row = -1;
      int localIdOld = 0;
      String loginOld = "";
      //supuesto: está ordenado por local, usuario y semanas, no son más de WEEK_PICK semanas
      while (rs.next()) {
         String login = rs.getString("login");
         int localId = rs.getInt("local_id");
         if (!loginOld.equals(login) || localIdOld != localId) {
            if (localIdOld != localId) {
               produccion[++row][0] = (Local) locales.get(new Integer(rs.getInt("local_id")));
               GregorianCalendar fecha = (GregorianCalendar) desdeLunes.clone();
               for (int i = 1; i < produccion[row].length; i++) {
                  produccion[row][i] = Fechas.getSemana(fecha.getTime());
                  fecha.add(Calendar.WEEK_OF_YEAR, 1);
               }
            }
            row++;
            produccion[row][0] = rs.getString("login");
            loginOld = login;
            localIdOld = localId;
         }

         int col = ((addPosition + rs.getInt("w")) % WEEKS_YEAR) - (WEEKS_YEAR - WEEKS_PICK);

         if ( ( col + 1) > 0 ) {
             if ( ( col + 1) != 0 ) {
                 produccion[row][col + 1] = rs.getBigDecimal("ph");
             } else {
                 produccion[row][1] = rs.getBigDecimal("ph");
             }
         }  else {
        	 col=col*-1;
             if ( ( col + 1) != 0 ) {
                 produccion[row][col + 1] = rs.getBigDecimal("ph");
             } else {
                 produccion[row][1] = rs.getBigDecimal("ph");
             }
        	 
         }
         
         
        /* 
         if ( ( col + 1) != 0 ) {
             produccion[row][col + 1] = rs.getBigDecimal("ph");
         } else {
             produccion[row][1] = rs.getBigDecimal("ph");
         }
         */
      }
      Db.close(rs, ps);
      return produccion;
   }

   /**
    * 
    * @param con
    * @param desdeLunes
    *           Debe ser Lunes sin hora
    * @param hastaDomingo
    *           Desde ser Domingo sin hora
    * @param locales
    * @param tipoPicking
    * @return
    * @throws SQLException
    */
   private Object[][] produccionPorLocal(Connection con, GregorianCalendar desdeLunes, GregorianCalendar hastaDomingo,
         Hashtable locales, String tipoPicking) throws SQLException {

      //Los numeros de semanas de la base de datos y de java son distintos.
      //int ultimaSemana = hastaDomingo.get(Calendar.WEEK_OF_YEAR);
      int ultimaSemana = Db.getSemana(con, hastaDomingo);

      int addPosition = WEEKS_YEAR - ultimaSemana - 1;
      Object[][] produccion = new Object[WEEKS_YEAR][locales.size() + 1];

      Configuracion conf = Db.getConfiguracion(con);

      //desde un lunes hasta un domingo
      //(cant_pick + cant_pick_sust) / (segundos / 3600.0)) < ? se escribe de otra forma para evitar division por cero
      // tener presente las reglas de desigualdad
      String sql = "SELECT local_id, WEEK_ISO(fecha) as w, min(fecha) as fecha,                      "
            + "(sum(cant_pick) + sum(cant_pick_sust)) / decimal(sum(segundos) / 3600.0, 10, 3) as ph "
            + "FROM  BODBA.IPRODUCTIVIDAD                                              "
            + "where fecha >= ? and fecha <= ? and segundos > ?   "
            + "  and (cant_pick + cant_pick_sust) * 3600 < ( ? * segundos)  and tipo_pick = '" + tipoPicking + "' "
            + "group by local_id, WEEK_ISO(fecha)                                      ";

      PreparedStatement ps = con.prepareStatement(sql + " With UR");
      ps.setDate(1, new java.sql.Date(desdeLunes.getTimeInMillis()));
      ps.setDate(2, new java.sql.Date(hastaDomingo.getTimeInMillis()));
      ps.setInt(3, conf.getTiempoMinimoRonda());
      ps.setInt(4, conf.getProductividadMaximaRonda());

      ResultSet rs = ps.executeQuery();
      //supuesto: está ordenado por local, no son más de 52 semanas
      while (rs.next()) {
         Integer localId = new Integer(rs.getInt("local_id"));
         int semana = rs.getInt("w");

         if (produccion[(addPosition + semana) % WEEKS_YEAR][0] == null) {
            //semana
            produccion[(addPosition + semana) % WEEKS_YEAR][0] = Fechas.getSemana(rs.getDate("fecha"));
         }
         //produccion
         if (locales.get(localId) != null)
            produccion[(addPosition + semana) % WEEKS_YEAR][((Local) locales.get(localId)).getOrden()] = rs
                  .getBigDecimal("ph");
      }
      Db.close(rs, ps);
      return produccion;
   }

   private Object[][] produccionPorLocalCaja(Connection con, GregorianCalendar desdeLunes, GregorianCalendar hastaDomingo,
         Hashtable locales) throws SQLException {

      //int semanaActual = hastaDomingo.get(Calendar.WEEK_OF_YEAR);
      int semanaActual = Db.getSemana(con, hastaDomingo);
      int addPosition = WEEKS_YEAR - semanaActual - 1;
      Object[][] produccion = new Object[WEEKS_YEAR][locales.size() + 1];

      Configuracion conf = Db.getConfiguracion(con);

      //desde un lunes hasta un domingo
      //(cant_pick + cant_pick_sust) / (segundos / 3600.0)) < ? se escribe de otra forma para evitar division por cero
      // tener presente las reglas de desigualdad
      String sql = "SELECT local_id, WEEK_ISO(fecha) as w, min(fecha) as fecha,                           "
            + "(sum(cant_pick) + sum(cant_pick_sust)) / decimal(sum(segundos_caja) / 3600.0, 10, 3) as ph "
            + "FROM  BODBA.IPRODUCTIVIDAD                                              "
            + "where fecha >= ? and fecha <= ? and segundos_caja > ?   "
            + "  and (cant_pick + cant_pick_sust) * 3600 < ( ? * segundos_caja)  and tipo_pick = 'L' "
            + "group by local_id, WEEK_ISO(fecha)                                      ";

      PreparedStatement ps = con.prepareStatement(sql + " With UR");
      ps.setDate(1, new java.sql.Date(desdeLunes.getTimeInMillis()));
      ps.setDate(2, new java.sql.Date(hastaDomingo.getTimeInMillis()));
      ps.setInt(3, conf.getTiempoMinimoRonda());
      ps.setInt(4, conf.getProductividadMaximaRonda());

      ResultSet rs = ps.executeQuery();
      //supuesto: está ordenado por local, no son más de 52 semanas
      while (rs.next()) {
         Integer localId = new Integer(rs.getInt("local_id"));
         if (produccion[(addPosition + rs.getInt("w")) % WEEKS_YEAR][0] == null) {
            //semana
            produccion[(addPosition + rs.getInt("w")) % WEEKS_YEAR][0] = Fechas.getSemana(rs.getDate("fecha"));
         }
         //produccion
         if(locales.get(localId) != null)
            produccion[(addPosition + rs.getInt("w")) % WEEKS_YEAR][((Local) locales.get(localId)).getOrden()] = rs
               .getBigDecimal("ph");
      }
      Db.close(rs, ps);
      return produccion;
   }

   private HSSFCellStyle estiloCelda(HSSFWorkbook objWB, HSSFFont fuente, short alineacion) {
      HSSFCellStyle estiloCelda = objWB.createCellStyle();
      estiloCelda.setWrapText(true);
      estiloCelda.setAlignment(alineacion);
      estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      estiloCelda.setFont(fuente);
      estiloCelda.setBorderBottom((short) 1);
      estiloCelda.setBorderLeft((short) 1);
      estiloCelda.setBorderRight((short) 1);
      estiloCelda.setBorderTop((short) 1);
      return estiloCelda;
   }

   /**
    * @return
    */
   private HSSFFont estiloFuente(HSSFWorkbook objWB, short bold, short color) {
      HSSFFont fuente = objWB.createFont();
      fuente.setColor(color);
      fuente.setFontHeightInPoints((short) 9);
      fuente.setFontName(HSSFFont.FONT_ARIAL);
      fuente.setBoldweight(bold);
      return fuente;
   }

   /**
    * @param hoja
    */
   private void setFotter(HSSFSheet hoja) {
      HSSFFooter footer = hoja.getFooter();
      footer.setCenter("&D");
   }
}
