/*
 * Created on 29-mar-2010
 */
package cl.bbr.bol.command.transporte;

import java.io.OutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;

import cl.bbr.bol.dto.CuadraturaDTO;
import cl.bbr.bol.service.TransporteService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class CuadraturaTransporteGenerar extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      Date fechaIni = sdf.parse(req.getParameter("fecha_desde"));
      Date fechaFin = sdf.parse(req.getParameter("fecha_hasta"));
      int patente_id;
      try {
         patente_id = Integer.parseInt(req.getParameter("patente"));
      } catch (Exception e) {
         patente_id = 0;
      }
      int chofer_id;
      try {
         chofer_id = Integer.parseInt(req.getParameter("chofer"));
      } catch (Exception e) {
         chofer_id = 0;
      }

      Time horaIniDesp = null;
      Time horaFinDesp = null;
      String jornada = req.getParameter("jornada");
      String[] horas = jornada.split("-");
      if (horas.length == 2) {
         sdf = new SimpleDateFormat("HH:mm");
         horaIniDesp = new Time(sdf.parse(horas[0].trim()).getTime());
         horaFinDesp = new Time(sdf.parse(horas[1].trim()).getTime());
      }
      TransporteService service = new TransporteService();
      int localId = (int) usr.getId_local();
      List lista = service.getCuadratura(localId, fechaIni, fechaFin, patente_id, chofer_id, horaIniDesp, horaFinDesp);
      HSSFWorkbook wb = generaExcel(lista);

      String filename = "CuadraturaTransporte-Local-" + usr.getId_local() + ".xls";

      res.setContentType("application/vnd.ms-excel");
      res.addHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
      OutputStream output = res.getOutputStream();
      wb.write(output);
      output.close();
   }

   /**
    * @param lista
    * @return
    */
   private HSSFWorkbook generaExcel(List lista) {
      HSSFWorkbook wb = new HSSFWorkbook();
      HSSFSheet sheet = wb.createSheet("Cuadratura de transporte");
      HSSFRow row0 = sheet.createRow((short) 0);
      int n=0;
      sheet.setColumnWidth(n, 256 * 15);
      HSSFCell cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Fecha de despacho"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("N° de Ruta"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Nº de OP"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Cantidad de BINES"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Patente (Camión)"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Chofer"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Dirección de Despacho"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Comuna"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Jornada de Despacho"));
      
      sheet.setColumnWidth(n, 256 * 15);
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Hora de activación de Ruta"));
      
      sheet.setColumnWidth(n, 256 * 15);
      cell = row0.createCell(n++);    
      cell.setCellValue(new HSSFRichTextString("Hora de llegada a domicilio"));
      
      sheet.setColumnWidth(n, 256 * 15);
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Hora de Salida de domicilio"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Reprogramación"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Responsable"));
      cell = row0.createCell(n++);
      cell.setCellValue(new HSSFRichTextString("Motivo"));
      
      CreationHelper createHelper = wb.getCreationHelper();
      HSSFCellStyle styleDate = wb.createCellStyle();
      styleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
      HSSFCellStyle styleTime = wb.createCellStyle();
      styleTime.setDataFormat(createHelper.createDataFormat().getFormat("hh:mm dd/MM/yy"));


      for (int i = 0; i < lista.size(); i++) {
         HSSFRow row = sheet.createRow((short) i + 1);
         CuadraturaDTO c = (CuadraturaDTO) lista.get(i);
         n = 0;
         cell = row.createCell(n++);
         cell.setCellValue(c.getFechaDespacho());
         cell.setCellStyle(styleDate);
         cell = row.createCell(n++);
         cell.setCellValue(c.getRutaId());
         cell = row.createCell(n++);
         cell.setCellValue(c.getPedidoId());
         cell = row.createCell(n++);
         cell.setCellValue(c.getCantidadBines());
         cell = row.createCell(n++);
         cell.setCellValue(c.getPatente());
         cell = row.createCell(n++);
         cell.setCellValue(c.getChofer());
         cell = row.createCell(n++);
         cell.setCellValue(c.getDireccionDespacho());
         cell = row.createCell(n++);
         cell.setCellValue(c.getComuna());
         cell = row.createCell(n++);
         cell.setCellValue(c.getJornadaDespacho());
         cell = row.createCell(n++);
         cell.setCellValue(c.getHoraActivacionRuta());
         cell.setCellStyle(styleTime);
         cell = row.createCell(n++);
         if (c.getHoraLlegadaDomicilio() != null)
            cell.setCellValue(c.getHoraLlegadaDomicilio());
         cell.setCellStyle(styleTime);
         cell = row.createCell(n++);
         if (c.getHoraSalidaDomicilio()!= null)
            cell.setCellValue(c.getHoraSalidaDomicilio());
         cell.setCellStyle(styleTime);
         cell = row.createCell(n++);
         cell.setCellValue(c.getReprogramacion());
         cell = row.createCell(n++);
         cell.setCellValue(new HSSFRichTextString(c.getResponsable()));
         cell = row.createCell(n++);
         cell.setCellValue(new HSSFRichTextString(c.getMotivo()));
      }
      return wb;
   }
}
