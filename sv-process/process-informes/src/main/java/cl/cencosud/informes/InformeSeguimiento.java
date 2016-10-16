/*
 * Created on 08-abr-2010
 */
package cl.cencosud.informes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.cencosud.informes.datos.Asistencia;
import cl.cencosud.informes.datos.Local;
import cl.cencosud.informes.datos.Usuario;
import cl.cencosud.util.Db;
import cl.cencosud.util.Listas;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 *  
 */
public class InformeSeguimiento {
   private Hashtable usuEncargados;
   private static String[] MESES = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
         "Septiembre", "Octubre", "Noviembre", "Diciembre" };

   public static void main(String[] args) throws Exception {
      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String driver = Parametros.getString("DRIVER");
      String url = Parametros.getString("URL");
      Connection con = Db.conexion(user, password, driver, url);
      System.out.println("CON : " + con);

      String path = Parametros.getString("PATH");

      InformeSeguimiento seguimiento = new InformeSeguimiento();
      GregorianCalendar hasta = new GregorianCalendar();
      GregorianCalendar desde = new GregorianCalendar();
      desde.set(Calendar.DAY_OF_MONTH, 1);

      List locales = Db.getLocales(con);

      for (int i = 0; i < locales.size(); i++) {
         Local local = (Local) locales.get(i);
         List matriz = seguimiento.listado(con, desde.getTime(), hasta.getTime(), local.getId());
         List asistencias = seguimiento.asistencia(con);
         String archivo = path + "seguimiento_Local_" + local.getNombre() + ".xls";
         seguimiento.excel(matriz, asistencias, archivo, local.getNombre(), MESES[desde.get(Calendar.MONTH)]);
      }
   }

   private void asistenciaExcel(HSSFSheet sheet, HSSFWorkbook wb, List asistencias, int iFil, int iCol) {
      HSSFCellStyle estilo = Estilos.pequegnaIzq(wb);
      HSSFCellStyle titulo = Estilos.titulo(wb);

      int fil = iFil;
      int col = iCol;
      for (int i = 0; i < asistencias.size(); i++) {
         Asistencia a = (Asistencia) asistencias.get(i);
         HSSFRow row0 = sheet.getRow((short) fil);
         if (row0 == null)
            row0 = sheet.createRow((short) fil);
         fil++;
         HSSFCell cell = row0.createCell(col);
         cell.setCellStyle(titulo);
         cell.setCellValue(new HSSFRichTextString(a.getNombre()));

         cell = row0.createCell(col + 1);
         cell.setCellStyle(estilo);
         cell.setCellValue(new HSSFRichTextString(a.getDescripcion()));
         if ((i + 1) % 4 == 0) {
            fil = iFil;
            col += 6;
         }
      }
   }

   private void encargadosExcel(HSSFSheet sheet, HSSFWorkbook wb, int iFil, int iCol) {
      HSSFCellStyle estilo = Estilos.pequegna(wb);
      HSSFCellStyle titulo = Estilos.titulo(wb);

      int fil = iFil;
      int col = iCol;
      HSSFRow row0 = sheet.createRow((short) fil++);
      HSSFCell cell = row0.createCell(col);
      cell.setCellStyle(titulo);
      cell.setCellValue(new HSSFRichTextString("Inicial"));

      cell = row0.createCell(col + 1);
      cell.setCellStyle(titulo);
      cell.setCellValue(new HSSFRichTextString("Nombre"));

      Enumeration enu = usuEncargados.keys();

      while (enu.hasMoreElements()) {
         Integer key = (Integer) enu.nextElement();
         Usuario usu = (Usuario) usuEncargados.get(key);
         row0 = sheet.createRow((short) fil++);
         cell = row0.createCell(col);
         cell.setCellStyle(estilo);
         cell.setCellValue(new HSSFRichTextString(usu.getIniciales()));
         cell = row0.createCell(col + 1);
         cell.setCellStyle(estilo);
         cell.setCellValue(new HSSFRichTextString(usu.toString()));
      }
   }

   private void encabezadoExcel(HSSFSheet sheet, HSSFWorkbook wb, int iFil, int iCol, String local, String mes) {
      HSSFCellStyle estilo = Estilos.pequegnaIzq(wb);
      HSSFCellStyle titulo = Estilos.titulo(wb);

      int fil = iFil;
      int col = iCol;
      HSSFRow row0 = sheet.createRow((short) fil++);
      HSSFCell cell = row0.createCell(col);
      cell.setCellStyle(titulo);
      cell.setCellValue(new HSSFRichTextString("Local"));

      cell = row0.createCell(col + 1);
      cell.setCellStyle(estilo);
      cell.setCellValue(new HSSFRichTextString(local));

      row0 = sheet.createRow((short) fil);
      cell = row0.createCell(col);
      cell.setCellStyle(titulo);
      cell.setCellValue(new HSSFRichTextString("Mes"));

      cell = row0.createCell(col + 1);
      cell.setCellStyle(estilo);
      cell.setCellValue(new HSSFRichTextString(mes));

   }

   /**
    * @param matriz
    * @throws IOException
    */
   private void excel(List matriz, List asistencias, String archivo, String local, String mes) throws IOException {
      FileOutputStream out = new FileOutputStream(archivo);
      HSSFWorkbook wb = new HSSFWorkbook();
      HSSFCellStyle estilo = Estilos.pequegna(wb);
      HSSFCellStyle titulo = Estilos.titulo(wb);

      HSSFSheet sheet = wb.createSheet("Seguimiento");

      short pFil = (short) (11 + usuEncargados.size());
      int iCol = 0;
      HSSFRow row0 = sheet.createRow((short) pFil + 0);
      sheet.setColumnWidth(iCol, 256 * 5);
      HSSFCell cell = row0.createCell(iCol++);
      cell.setCellStyle(titulo);
      cell.setCellValue(new HSSFRichTextString("N°"));

      sheet.setColumnWidth(iCol, 256 * 10);
      cell = row0.createCell(iCol++);
      cell.setCellStyle(titulo);
      cell.setCellValue(new HSSFRichTextString("Encargado"));

      sheet.setColumnWidth(iCol, 256 * 30);
      cell = row0.createCell(iCol++);
      cell.setCellStyle(titulo);
      cell.setCellValue(new HSSFRichTextString("Nombre"));

      //numero de día
      for (int col = 1; col <= 31; col++) {
         sheet.setColumnWidth(iCol, 256 * 5);
         cell = row0.createCell(iCol++);
         cell.setCellStyle(titulo);
         cell.setCellValue(col);
      }

      for (int fil = 0; fil < matriz.size(); fil++) {
         Object[] array = (Object[]) matriz.get(fil);
         row0 = sheet.createRow((short) pFil + fil + 1);
         for (int col = 0; col < array.length; col++) {
            cell = row0.createCell(col);
            cell.setCellStyle(estilo);
            cell.setCellValue(new HSSFRichTextString(array[col] == null ? "" : array[col].toString()));
         }
      }

      encabezadoExcel(sheet, wb, 0, 1, local, mes);
      encargadosExcel(sheet, wb, 5, 1);
      asistenciaExcel(sheet, wb, asistencias, 5, 6);
      wb.write(out);
      out.close();
   }

   private List asistencia(Connection con) throws SQLException {
      String sql = "select id, nombre, descripcion from bodba.asistencias order by orden";
      PreparedStatement ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      List lista = new ArrayList();
      while (rs.next()) {
         Asistencia asistencia = new Asistencia();
         asistencia.setId(rs.getInt("id"));
         asistencia.setNombre(rs.getString("nombre"));
         asistencia.setDescripcion(rs.getString("descripcion"));
         lista.add(asistencia);
      }
      Db.close(rs, ps);
      return lista;
   }

   /**
    * @param con
    * @throws SQLException
    */
   private List listado(Connection con, Date desde, Date hasta, int localId) throws SQLException {
      usuEncargados = new Hashtable();
      String sql = "select e.encargado_id, id_usuario, login, u.nombre, apellido, apellido_mat, ap.id, ap.fecha, a.nombre as asistencia "
            + "from bodba.encargados_pickeadores e inner join bodba.bo_usuarios u on u.id_usuario = e.pickeador_id "
            + "left outer join bodba.asistencias_pickeadores ap on ap.pickeador_id = e.pickeador_id and ap.local_id = e.local_id "
            + "left outer join bodba.asistencias a on a.id = ap.asistencia_id           "
            + "where ap.fecha >= ? and ap.fecha <= ? and ap.local_id = ? " + "order by id_usuario, ap.fecha";

      String sqlEncargado = "select id_usuario, nombre, apellido, apellido_mat from bodba.bo_usuarios where id_usuario in ";

      PreparedStatement ps = con.prepareStatement(sql + " With UR");
      ps.setDate(1, new java.sql.Date(desde.getTime()));
      ps.setDate(2, new java.sql.Date(hasta.getTime()));
      ps.setInt(3, localId);
      ResultSet rs = ps.executeQuery();

      List matriz = new ArrayList();

      HashSet encargadosId = new HashSet();
      int usuarioIdOld = -1;
      int pCol = 2;
      Object[] array = null;
      while (rs.next()) {
         int usuarioId = rs.getInt("id_usuario");
         int encargadoId = rs.getInt("encargado_id");
         encargadosId.add(new Integer(encargadoId));

         if (usuarioId != usuarioIdOld) {
            array = new Object[34];
            matriz.add(array);
            //Encargados
            if (array[pCol - 1] == null) {
               array[pCol - 1] = new ArrayList();
            }
            //fin
            usuarioIdOld = usuarioId;
            array[pCol + 0] = rs.getString("nombre") + " " + rs.getString("apellido") + " "
                  + rs.getString("apellido_mat");
         }
         if (((List) array[pCol - 1]).indexOf(new Integer(encargadoId)) == -1)
            ((List) array[pCol - 1]).add(new Integer(encargadoId));
         GregorianCalendar fec = new GregorianCalendar();
         fec.setTimeInMillis(rs.getDate("fecha").getTime());
         int dia = fec.get(Calendar.DAY_OF_MONTH);
         if (array[pCol + dia] == null)
            array[pCol + dia] = rs.getString("asistencia");
         //array[pCol + dia] = array[pCol + dia] + " - " + rs.getString("asistencia");

      }//while

      System.out.println(Listas.join(encargadosId));
      if (encargadosId.size() > 0) {
         ps = con.prepareStatement(sqlEncargado + Listas.join(encargadosId) + " With UR");
         rs = ps.executeQuery();

         while (rs.next()) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("id_usuario"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setApellido(rs.getString("apellido"));
            usuario.setApellidoMaterno(rs.getString("apellido_mat"));
            usuEncargados.put(new Integer(usuario.getId()), usuario);
         }

         for (int fil = 0; fil < matriz.size(); fil++) {
            array = (Object[]) matriz.get(fil);
            List encargado = (List) array[1];
            if (encargado == null)
               break;
            array[0] = new Integer(fil + 1);
            String inicial = "";
            for (int j = 0; j < encargado.size(); j++) {
               inicial += ((Usuario) usuEncargados.get(encargado.get(j))).getIniciales();
            }
            array[1] = inicial;
         }
      }

      return matriz;
   }
}
