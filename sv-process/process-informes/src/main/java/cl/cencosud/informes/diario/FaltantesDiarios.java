/*
 * Created on 18-oct-2012
 *
 */
package cl.cencosud.informes.diario;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.cencosud.informes.Estilos;
import cl.cencosud.informes.datos.Configuracion;
import cl.cencosud.informes.datos.Local;
import cl.cencosud.informes.datos.Ranking;
import cl.cencosud.informes.datos.SustitutosYFaltantes;
import cl.cencosud.util.Db;
import cl.cencosud.util.Fechas;

/**
 * @author Victor
 * @version 18/10/2012
 */
public class FaltantesDiarios implements IProductividadDB {
	
	protected final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
   /**
    * @param con
    * @return
    * @throws SQLException
    */
   	protected Hashtable getPorLocal(Connection con, GregorianCalendar dia) throws SQLException {
      Configuracion conf = Db.getConfiguracion(con);
      String sql = SQL_GETPORLOCAL;

      PreparedStatement ps = con.prepareStatement(sql + " WITH UR ");
      ps.setDate(1, new java.sql.Date(dia.getTimeInMillis()));
      ps.setInt(2, conf.getTiempoMinimoRonda());
      ps.setInt(3, conf.getProductividadMaximaRonda());
      ResultSet rs = ps.executeQuery();
      Hashtable datos = new Hashtable();
      List lista = null;
      int localIdOld = 0;
      while (rs.next()) {
         int localId = rs.getInt("local_id");
         if (localId != localIdOld) {
            lista = new ArrayList();
            datos.put(new Integer(localId), lista);
            localIdOld = localId;
         }
         SustitutosYFaltantes obj = new SustitutosYFaltantes();
         obj.setNombre(rs.getString("nombre") + " " + rs.getString("apellido") + " " + rs.getString("apellido_mat"));
         obj.setPickeados(rs.getBigDecimal("cant_pick"));
         obj.setSustituidos(rs.getBigDecimal("cant_pick_sust"));
         obj.setFaltantes(rs.getBigDecimal("cant_falt"));
         obj.setFaltantesNoSustituir(rs.getBigDecimal("cant_falt_no_sust"));
         lista.add(obj);
      }
      datos.put("dia", dia);
      return datos;
   }

   protected void imprimir(File archivo, List locales, Hashtable datos) throws IOException {
      FileOutputStream out = new FileOutputStream(archivo);
      HSSFWorkbook objWB = planillaExcel(locales, datos);
      objWB.write(out);
      out.close();
   }

   private HSSFWorkbook planillaExcel(List locales, Hashtable datos) throws IOException {
      //creamos el libro
      HSSFWorkbook objWB = new HSSFWorkbook();
      //Seteamos estilos para la planilla
      HSSFCellStyle estiloTitulo = Estilos.titulo(objWB);
      HSSFCellStyle estiloTexto = Estilos.texto(objWB);
      HSSFCellStyle estiloNumero = Estilos.numero(objWB);
      HSSFCellStyle estiloNumeroBold = Estilos.numeroBold(objWB);
      HSSFCellStyle estiloPorcentaje0 = Estilos.porcentaje(objWB, "0%");
      HSSFCellStyle estiloPorcentaje1 = Estilos.porcentaje(objWB, "0.00%");
      HSSFCellStyle estiloPorcentajeBold0 = Estilos.porcentajeBold(objWB, "0%");
      HSSFCellStyle estiloPorcentajeBold1 = Estilos.porcentajeBold(objWB, "0.00%");

      HSSFSheet hoja = objWB.createSheet("Ranking");
      setFotter(hoja);

      String[] titulos = new String[] { "Nº", "Nombre", "Total Pickeados", "Faltantes No Sustituidos",
            "Faltantes Criterio: No Sustituir", "Faltantes Sustituidos", "Faltantes totales",
            "% Indice de Sustitución", "% Faltantes" };
      HSSFRow fila = hoja.createRow((short) 0);
      for (int j = 0; j < titulos.length; j++) {
         hoja.setColumnWidth(j, 256 * (j > 1 ? 11 : (j == 0 ? 3 : 20)));
         HSSFCell celda = fila.createCell(j);
         celda.setCellStyle(estiloTitulo);
         celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         celda.setCellValue(new HSSFRichTextString(titulos[j]));
      }

      ///Ordenar por indice de faltantes
      List ranking = new ArrayList();
      for (int i = 0; i < locales.size(); i++) {
         Local local = (Local) locales.get(i);
         List lista = (ArrayList) datos.get(new Integer(local.getId()));
         if (lista == null)
            continue;
         Ranking r = new Ranking();
         r.setLocal(local);

         for (int j = 0; j < lista.size(); j++) {
            SustitutosYFaltantes obj = (SustitutosYFaltantes) lista.get(j);
            r.addPickeados(obj.getTotal());
            r.addFaltantes(obj.getFaltantes());
            r.addSustituidos(obj.getSustituidos());
            r.addFaltantesNoSustituir(obj.getFaltantesNoSustituir());
         }
         ranking.add(r);
      }
      Collections.sort(ranking);
      //FIN

      //Ranking
      for (int i = 0; i < ranking.size(); i++) {
         Ranking r = (Ranking) ranking.get(i);
         fila = hoja.createRow((short) i + 1);
         HSSFCell celda = fila.createCell(0);
         celda.setCellStyle(estiloNumero);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(i + 1);
         celda = fila.createCell(1);
         celda.setCellStyle(estiloTexto);
         celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         celda.setCellValue(new HSSFRichTextString(r.getLocal().getNombre()));
         celda = fila.createCell(2);
         celda.setCellStyle(estiloNumero);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getPickeados().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(3);
         celda.setCellStyle(estiloNumero);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getFaltantes().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(4);
         celda.setCellStyle(estiloNumero);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getFaltantesNoSustituir().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(5);
         celda.setCellStyle(estiloNumero);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getSustituidos().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(6);
         celda.setCellStyle(estiloNumero);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getTotalFaltantes().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(7);
         celda.setCellStyle(estiloPorcentaje0);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getIndiceSustitucion() / 100);
         celda = fila.createCell(8);
         celda.setCellStyle(estiloPorcentaje1);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getIndiceFaltantes() / 100);

      }

      ////////////////////////////////////////////////
      for (int i = 0; i < ranking.size(); i++) {
         Ranking r = (Ranking) ranking.get(i);
         Local local = r.getLocal();
         //creamos hoja
         hoja = objWB.createSheet(local.getNombre());
         setFotter(hoja);
         //creamos una fila
         fila = hoja.createRow((short) 0);
         //fila.setHeight((short) (206 * 5));
         HSSFCell celda = fila.createCell(1);
         HSSFCellStyle estilo = objWB.createCellStyle();
         estilo.cloneStyleFrom(estiloTitulo);
         estilo.setWrapText(false);
         celda.setCellStyle(estilo);
         celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         /*String fechas = Fechas.getDiaMes(((GregorianCalendar) datos.get("desde")).getTime()) + " - "
               + Fechas.getDiaMes(((GregorianCalendar) datos.get("hasta")).getTime());
         celda.setCellValue(new HSSFRichTextString("Dias: " + fechas)); */
         String fecha = Fechas.getDiaMes(((GregorianCalendar) datos.get("dia")).getTime());
         celda.setCellValue(new HSSFRichTextString("Dia: " + fecha)); 

         fila = hoja.createRow((short) 1);

         for (int j = 0; j < titulos.length; j++) {
            hoja.setColumnWidth(j, 256 * (j > 1 ? 11 : (j == 0 ? 3 : 40)));
            celda = fila.createCell(j);
            celda.setCellStyle(estiloTitulo);
            celda.setCellType(HSSFCell.CELL_TYPE_STRING);
            celda.setCellValue(new HSSFRichTextString(titulos[j]));
         }

         List lista = (ArrayList) datos.get(new Integer(local.getId()));
         if (lista == null)
            continue;
         //Detalle por local
         for (int j = 0; j < lista.size(); j++) {
            SustitutosYFaltantes obj = (SustitutosYFaltantes) lista.get(j);
            fila = hoja.createRow((short) (j + 2));
            celda = fila.createCell(0);
            celda.setCellStyle(estiloNumero);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellValue(j + 1);
            celda = fila.createCell(1);
            celda.setCellStyle(estiloTexto);
            celda.setCellType(HSSFCell.CELL_TYPE_STRING);
            celda.setCellValue(new HSSFRichTextString(WordUtils.capitalizeFully(obj.getNombre())));
            celda = fila.createCell(2);
            celda.setCellStyle(estiloNumero);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellValue(obj.getTotal().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
            celda = fila.createCell(3);
            celda.setCellStyle(estiloNumero);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellValue(obj.getFaltantes().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
            celda = fila.createCell(4);
            celda.setCellStyle(estiloNumero);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellValue(obj.getFaltantesNoSustituir().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
            celda = fila.createCell(5);
            celda.setCellStyle(estiloNumero);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellValue(obj.getSustituidos().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
            celda = fila.createCell(6);
            celda.setCellStyle(estiloNumero);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellValue(obj.getTotalFaltantes().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
            celda = fila.createCell(7);
            celda.setCellStyle(estiloPorcentaje0);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellValue(obj.getIndiceSustitucion() / 100);
            celda = fila.createCell(8);
            celda.setCellStyle(estiloPorcentaje1);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellValue(obj.getIndiceFaltantes() / 100);
         }
         fila = hoja.createRow((short) lista.size() + 3);
         celda = fila.createCell(1);
         celda.setCellStyle(estiloTitulo);
         celda.setCellType(HSSFCell.CELL_TYPE_STRING);
         celda.setCellValue(new HSSFRichTextString("Total"));
         celda = fila.createCell(2);
         celda.setCellStyle(estiloNumeroBold);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getPickeados().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(3);
         celda.setCellStyle(estiloNumeroBold);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getFaltantes().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(4);
         celda.setCellStyle(estiloNumeroBold);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getFaltantesNoSustituir().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(5);
         celda.setCellStyle(estiloNumeroBold);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getSustituidos().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(6);
         celda.setCellStyle(estiloNumeroBold);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getTotalFaltantes().setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
         celda = fila.createCell(7);
         celda.setCellStyle(estiloPorcentajeBold0);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getIndiceSustitucion() / 100);
         celda = fila.createCell(8);
         celda.setCellStyle(estiloPorcentajeBold1);
         celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
         celda.setCellValue(r.getIndiceFaltantes() / 100);
      }
      return objWB;
   }

   /**
    * @param hoja
    */
   private void setFotter(HSSFSheet hoja) {
      HSSFFooter footer = hoja.getFooter();
      footer.setCenter("&D");
   }

}
