/*
 * Created on 21-ago-2009
 */
package cl.cencosud.informes.diario;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

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
 * @author Victor
 */
public class ProductividadDiaria implements IProductividadDB{
   static int DAYS_YEAR = 0 ;//se calcula en InformeDiario
   final static int DAY_PICK = Integer.parseInt(Parametros.getString("days.pick")); //últimas semanas de produccion para informe pickedores
   final static int DAYS_CONSECUTIVE =Integer.parseInt(Parametros.getString("days.consecutive"));
   final static int DIAS_ATRAS = Integer.parseInt(Parametros.getString("days.back"));//cantidad de días hacia atrás a generar en informe, a partir del día anterior
   
   final static String NORMAL = "N";
   final static String LIGHT = "L";
   
   private final static Logger log = Logger.getLogger(ProductividadDiaria.class.getName());
   protected final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
   protected HSSFCellStyle estiloCelda(HSSFWorkbook objWB, HSSFFont fuente, short alineacion) {
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
   protected HSSFFont estiloFuente(HSSFWorkbook objWB, short bold, short color) {
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
   protected void setFotter(HSSFSheet hoja) {
      HSSFFooter footer = hoja.getFooter();
      footer.setCenter("&D");
   }
   
   /**
    * 
    * @param con
    * @param diaAnterior
    *           Debe ser Lunes sin hora
    * @param dia
    *           Desde ser Domingo sin hora
    * @param locales
    * @param tipoPicking
    * @return
    * @throws SQLException
    */
   protected Object[][] produccionPorLocal(Connection con, GregorianCalendar diaAnterior, GregorianCalendar dia,
         Hashtable locales, String tipoPicking) throws SQLException {

      //Los numeros de semanas de la base de datos y de java son distintos.
       int ultimoDia = Db.getDia(con, dia);
       
       log.debug("<metodo produccionPorLocal> (hastaDia) desde el dia:"+dia.getTime());
       log.debug("<metodo produccionPorLocal> (ultimoDia) desde el dia por base de datos:"+ultimoDia);
       
       int addPosition = diaAnterior.get(Calendar.DAY_OF_YEAR);
       log.debug("<metodo produccionPorLocal> (diaAnterior en dias del año {Calendar.DAY_OF_YEAR} )addPosition:"+addPosition);
       
       Object[][] produccion = new Object[DIAS_ATRAS][locales.size() + 1];
       Configuracion conf = Db.getConfiguracion(con);

       String sql = SQL_PRODUCCIONPORLOCAL;


       PreparedStatement ps = con.prepareStatement(sql + " With UR");
       ps.setDate(1, new java.sql.Date(diaAnterior.getTimeInMillis()));
       ps.setDate(2, new java.sql.Date(dia.getTimeInMillis()));
       ps.setInt(3, conf.getTiempoMinimoRonda());
       ps.setInt(4, conf.getProductividadMaximaRonda());
       ps.setString(5,tipoPicking);

       ResultSet rs = ps.executeQuery();
       while (rs.next()) {
    	 Integer localId = new Integer(rs.getInt("local_id"));
  
    	 int diaAux =  getColumeExcel(ultimoDia,rs.getInt("w"),false);
    	
    	 log.debug("<metodo produccionPorLocal> posicion dia "+ diaAux +" local id "+localId+" fecha para posicionar: "+rs.getDate("fecha"));
         
         if(produccion[diaAux][0] == null) {
              produccion[diaAux][0] = Fechas.getDiaMes(rs.getDate("fecha"));
         }  
       
         if (locales.get(localId) != null){
            produccion[diaAux][((Local) locales.get(localId)).getOrden()] = rs.getBigDecimal("ph");
         }
         
       }
       Db.close(rs, ps);
       return produccion;     
   }
   
   protected Object[][] produccionPorPickeador(Connection con,GregorianCalendar hastaDia, Hashtable locales, String tipoPicking) throws SQLException {

        //los numeros de semana son distintos en java y base de datos
        int ultimoDia = Db.getDia(con, hastaDia);
        GregorianCalendar desdeDia = (GregorianCalendar) hastaDia.clone();
        desdeDia.add(Calendar.DAY_OF_YEAR, -DAY_PICK);
        
        Configuracion conf = Db.getConfiguracion(con);
      
        String sql = SQL_PRODUCCIONPORPICKEADOR;
        
        log.debug("<metodo produccionPorPickeador> sql:"+sql);
        log.debug("<metodo produccionPorPickeador> conf.getTiempoMinimoRonda():"+conf.getTiempoMinimoRonda());
        log.debug("<metodo produccionPorPickeador> conf.getProductividadMaximaRonda():"+conf.getProductividadMaximaRonda());
        
        PreparedStatement ps = con.prepareStatement(Db.getSqlCount(sql));
        ps.setDate(1, new java.sql.Date(desdeDia.getTimeInMillis()));
        ps.setDate(2, new java.sql.Date(hastaDia.getTimeInMillis()));
        ps.setInt(3, conf.getTiempoMinimoRonda());
        ps.setInt(4, conf.getProductividadMaximaRonda());
        ps.setString(5,tipoPicking);
        ResultSet rs = ps.executeQuery();
        
        int size = 0;
        
        if (rs.next())
           size = rs.getInt(1);

        log.debug("<metodo produccionPorPickeador> numero de registros cargados:"+size);
        
        ps = con.prepareStatement(sql + " With UR");
        ps.setDate(1, new java.sql.Date(desdeDia.getTimeInMillis()));
        ps.setDate(2, new java.sql.Date(hastaDia.getTimeInMillis()));
        ps.setInt(3, conf.getTiempoMinimoRonda());
        ps.setInt(4, conf.getProductividadMaximaRonda());
        ps.setString(5,tipoPicking);
        rs = ps.executeQuery();

        //+locales size: espacio para los locales y semanas
        //+1 espacio para login
        Object[][] produccion = new Object[size + locales.size()][DAY_PICK + 1];

        int row = -1;
        int localIdOld = 0;
        String loginOld = "";
        //supuesto: está ordenado por local, usuario y dias, no son más de DAY PICKS
        while (rs.next()) {
  		 //login es el pikeador
           String login = rs.getString("login");
           int localId = rs.getInt("local_id");
           
         // creacion de una nueva fila y grupo  
  		 // si el login antiguio que se esta procesando es distinto al login que viene ahora
  		 // o el local antiguo es distinto al local que viene ahora
           if (!loginOld.equals(login) || localIdOld != localId) {
              if (localIdOld != localId) {
                 //carga el nuevo local en la lista
  			   produccion[++row][0] = (Local) locales.get(new Integer(rs.getInt("local_id")));
                 GregorianCalendar fecha = (GregorianCalendar) desdeDia.clone();
                 //carga las fechas de los dias
  			   for (int i = 1; i < produccion[row].length; i++) {
                    produccion[row][i] = Fechas.getDiaMes(fecha.getTime());
                    fecha.add(Calendar.DAY_OF_YEAR, 1);
                 }
              }
              row++;
              //carga el usuario a la lista 
  			  produccion[row][0] = rs.getString("login");
              loginOld = login;
              localIdOld = localId;
           }
  		 
           //carga lo pikiado en la fila nuero col.
           int col =  getColumeExcel(ultimoDia, rs.getInt("w"),true);
          
           log.debug("<metodo produccionPorPickeador> posicion dia "+ col +" local id "+localId+" usuario login "+login+", fecha para posicionar: "+rs.getDate("fecha"));
           
           if ( ( col + 1) != 0 ) {
               produccion[row][col + 1] = rs.getBigDecimal("ph");
           } else {
               produccion[row][1] = rs.getBigDecimal("ph");
           }
        }
        Db.close(rs, ps);
        return produccion;
     }
   
   /**
    * metodo que entraga el orden de los registros en el excel, segun la fecha (rsW).
    * 
    * @param ultimoDia es el valor numerico del dia del año del ultimo dia del reporte
    * @param rsW es el dia del registro que se esta procesando (no puede superar el ultimoDia, si lo supera es porque se cruzo a otro año)
    * @param dayPicking indica si es picking para invertir las posiciones en la lista
    * @return
    */
   protected int getColumeExcel(final int ultimoDia, final int rsW, final boolean dayPicking){
	   int col = 0;
	   
	   col = ultimoDia - rsW-1;
	   
	   //se invirtio el indice (se cruzo desde un año a otro).
	   if(col<0){
		   if(-339==col)
			   log.debug("here");
    	   log.info("<metodo getColumeExcel> se invirtio el indice de la lista en "+col);
    	   col = col + DAYS_YEAR;
       }
	   
	   //invierte las posiciones en la lista si es picking
	   col = dayPicking? (DAY_PICK-1)-col:col;
	   
       return col;
   }
   
   protected void hojaProdPickeadores(Object[][] prodPorPick, HSSFWorkbook objWB, HSSFSheet hoja3) {
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

	         //
	         //eliminar pickeadores con más de 3 semanas consecutivas sin pickear
	         int count = 0;
	         for (int j = prodPorPick[i].length - 1; j >= 0; j--) {
	            if (prodPorPick[i][j] == null) {
	               count++;
	            }
	         }

	         if (count-1 >= DAYS_CONSECUTIVE)
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
   
   		
}
