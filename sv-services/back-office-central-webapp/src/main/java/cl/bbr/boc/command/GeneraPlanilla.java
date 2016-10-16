package cl.bbr.boc.command;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDesp;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que modifica a los Usuarios
 * @author bbr
 *
 */
public class GeneraPlanilla extends Command {
	//private final static long serialVersionUID = 1;
 
 
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
		String fecha = "", Local = "", hora_desp = "";
	    
	    int local = 0;
	    
	    try{
            String Fecha = "";
            fecha = (req.getParameter("fecha")==null?"":req.getParameter("fecha"));
            local = Integer.parseInt(req.getParameter("local")==null?"0":req.getParameter("local"));
            hora_desp = req.getParameter("hora_desp")==null?"0":req.getParameter("hora_desp");
            
            Calendar cal = Calendar.getInstance();
            if (!fecha.equals("")){
				int dia  = Integer.parseInt(fecha.substring(0,2));
				int mes  = Integer.parseInt(fecha.substring(3,5)) - 1;
	            int anho = Integer.parseInt(fecha.substring(6,10));
			    cal.set(anho, mes, dia);
			}
            
            DateFormat DateFormat  = new SimpleDateFormat("yyyyMMdd", new Locale("es", "ES", ""));
			Fecha = DateFormat.format(cal.getTime()).toUpperCase();
	        
			BizDelegate biz = new BizDelegate();
			
			HashMap lstPedidos = biz.getListadoOP(cal, local, hora_desp);
			HSSFWorkbook wb = generaExcel(lstPedidos, Fecha);
			
			LocalDTO loc = biz.getLocalById(local);
			Local = loc.getNom_local().trim();
			
			String filename="OP_Despacho_" + Fecha + "_" + Local + ".xls";
			
			res.setContentType ("application/vnd.ms-excel");
            res.addHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
			OutputStream output = res.getOutputStream();
			wb.write(output);
			output.close();
	    }catch(Exception e){
	        e.printStackTrace();
	    }
    }


    private static HSSFWorkbook generaExcel(HashMap lstPedidos, String Fecha){
		
		HSSFWorkbook wb = new HSSFWorkbook();
		try{
         HSSFDataFormat format = wb.createDataFormat();

			HSSFSheet sheet = wb.createSheet("Planilla Despacho");
			
			// Create a new font and alter it.
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short)10);
			font.setFontName("Arial");
			font.setItalic(false);
			//font.setStrikeout(true);

			
			// Create a new font and alter it.
			HSSFFont font2 = wb.createFont();
			font2.setFontHeightInPoints((short)10);
			font2.setFontName("Arial");
			font2.setItalic(false);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font2.setColor(HSSFColor.WHITE.index);
			

			// Fonts are set into a style so create a new one to use.
			HSSFCellStyle style = wb.createCellStyle();
			style.setFont(font2);
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			style.setFillBackgroundColor(HSSFColor.BLUE.index);
			style.setFillForegroundColor(HSSFColor.GREEN.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//style.setWrapText(true);

			HSSFCellStyle style2 = wb.createCellStyle();
			style2.setFont(font);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setFillForegroundColor(HSSFColor.WHITE.index);
			//style2.setFillPattern(HSSFCellStyle.DIAMONDS);
			//style2.setWrapText(true);

			
			HSSFCellStyle style3 = wb.createCellStyle();
			style3.setFont(font);
			style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style3.setFillForegroundColor(HSSFColor.WHITE.index);
			//style3.setFillPattern(HSSFCellStyle.DIAMONDS);
			style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//style3.setWrapText(true);

			
			HSSFCellStyle styleMoneda = wb.createCellStyle();
			styleMoneda.setFont(font);
			styleMoneda.setBorderTop(HSSFCellStyle.BORDER_THIN);
			styleMoneda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			styleMoneda.setBorderRight(HSSFCellStyle.BORDER_THIN);
			styleMoneda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			styleMoneda.setFillForegroundColor(HSSFColor.WHITE.index);
			//styleMoneda.setFillPattern(HSSFCellStyle.DIAMONDS);
			styleMoneda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			//styleMoneda.setDataFormat((short) 5); //5, "($#,##0_);($#,##0)"
			styleMoneda.setDataFormat(format.getFormat("$ #,##0_)"));

			
			HSSFRow row0 = sheet.createRow((short) 0);
			
			HSSFCell cell_0_0 = row0.createCell(0);
			cell_0_0.setCellValue(new HSSFRichTextString("Fecha"));
			cell_0_0.setCellStyle(style);

			HSSFCell cell_0_1 = row0.createCell(1);
			cell_0_1.setCellValue(new HSSFRichTextString("OP"));
			cell_0_1.setCellStyle(style);
			
			HSSFCell cell_0_2 = row0.createCell(2);
			cell_0_2.setCellValue(new HSSFRichTextString("Rgo. Entrega"));
			cell_0_2.setCellStyle(style);
			
			HSSFCell cell_0_3 = row0.createCell(3);
			cell_0_3.setCellValue(new HSSFRichTextString("Tipo Desp."));
			cell_0_3.setCellStyle(style);

			HSSFCell cell_0_4 = row0.createCell(4);
			cell_0_4.setCellValue(new HSSFRichTextString("Monto $"));
			cell_0_4.setCellStyle(style);

			HSSFCell cell_0_5 = row0.createCell(5);
			cell_0_5.setCellValue(new HSSFRichTextString("Hr. Corte Boleta"));
			cell_0_5.setCellStyle(style);

			HSSFCell cell_0_6 = row0.createCell(6);
			cell_0_6.setCellValue(new HSSFRichTextString("Cant. Bins"));
			cell_0_6.setCellStyle(style);

			HSSFCell cell_0_7 = row0.createCell(7);
			cell_0_7.setCellValue(new HSSFRichTextString("Nombre"));
			cell_0_7.setCellStyle(style);

			HSSFCell cell_0_8 = row0.createCell(8);
			cell_0_8.setCellValue(new HSSFRichTextString("Dirección"));
			cell_0_8.setCellStyle(style);

			HSSFCell cell_0_9 = row0.createCell(9);
			cell_0_9.setCellValue(new HSSFRichTextString("Comuna"));
			cell_0_9.setCellStyle(style);

			HSSFCell cell_0_10 = row0.createCell(10);
			cell_0_10.setCellValue(new HSSFRichTextString("Teléfono"));
			cell_0_10.setCellStyle(style);

			HSSFCell cell_0_11 = row0.createCell(11);
			cell_0_11.setCellValue(new HSSFRichTextString("E-Mail"));
			cell_0_11.setCellStyle(style);

			HSSFCell cell_0_12 = row0.createCell(12);
			cell_0_12.setCellValue(new HSSFRichTextString("Confirmación"));
			cell_0_12.setCellStyle(style);

			
			/*****************************************************/
			int j = 0;
			//int k = 0;
			Iterator it = lstPedidos.values().iterator();
			while (it.hasNext()) {
			    PedidoDesp ped = (PedidoDesp)it.next();
			    j++;
			    
			    HSSFRow row = sheet.createRow((short) j);
			    
			    String fecha = ped.getFecha();
			    Calendar cal = Calendar.getInstance();
			    int year = Integer.parseInt(fecha.substring(0, 4));
			    int mes  = Integer.parseInt(fecha.substring(5, 7)) - 1;
			    int dia  = Integer.parseInt(fecha.substring(8, 10));
			    cal.set(year, mes, dia);
			    
	            DateFormat DateFormat  = new SimpleDateFormat("dd MMM", new Locale("es", "ES", ""));
				Fecha = DateFormat.format(cal.getTime()).toUpperCase();

				// Create a cell and put a value in it.
				HSSFCell cell_0 = row.createCell(0);
				cell_0.setCellValue(new HSSFRichTextString(Fecha));
				cell_0.setCellStyle(style2);

				HSSFCell cell_1 = row.createCell(1);
				cell_1.setCellValue(new HSSFRichTextString(ped.getOp()));
				cell_1.setCellStyle(style2);

				HSSFCell cell_2 = row.createCell(2);
				cell_2.setCellValue(new HSSFRichTextString(ped.getHorario()));
				cell_2.setCellStyle(style3);

				HSSFCell cell_3 = row.createCell(3);
				cell_3.setCellValue(new HSSFRichTextString(ped.getTipo_despacho()));
				cell_3.setCellStyle(style3);
				
				HSSFCell cell_4 = row.createCell(4);
				cell_4.setCellValue(ped.getPos_monto());
				cell_4.setCellStyle(styleMoneda);
				
				HSSFCell cell_5 = row.createCell(5);
				cell_5.setCellValue(new HSSFRichTextString(ped.getPos_hora()));
				cell_5.setCellStyle(style3);

				HSSFCell cell_6 = row.createCell(6);
				cell_6.setCellValue(new HSSFRichTextString(ped.getCant_bins()));
				cell_6.setCellStyle(style3);

				HSSFCell cell_7 = row.createCell(7);
				cell_7.setCellValue(new HSSFRichTextString(ped.getNom_cliente()));
				cell_7.setCellStyle(style2);

				HSSFCell cell_8 = row.createCell(8);
				cell_8.setCellValue(new HSSFRichTextString(ped.getDireccion()));
				cell_8.setCellStyle(style2);

				HSSFCell cell_9 = row.createCell(9);
				cell_9.setCellValue(new HSSFRichTextString(ped.getComuna()));
				cell_9.setCellStyle(style2);

				HSSFCell cell_10 = row.createCell(10);
				cell_10.setCellValue(new HSSFRichTextString(ped.getTelefonos()));
				cell_10.setCellStyle(style2);

				HSSFCell cell_11 = row.createCell(11);
				cell_11.setCellValue(new HSSFRichTextString(ped.getEMail()));
				cell_11.setCellStyle(style2);

				HSSFCell cell_12 = row.createCell(12);
				cell_12.setCellValue(new HSSFRichTextString(ped.getTipo_tarjeta()));
				cell_12.setCellStyle(style3);

			}
			
			sheet.autoSizeColumn((short)0);  // FECHA DESPACHO
			sheet.autoSizeColumn((short)1);  // OP
			sheet.autoSizeColumn((short)2);  // HORARIO DESPACHO
			sheet.autoSizeColumn((short)3);  // TIPO DESPACHO
			sheet.autoSizeColumn((short)4);  // MONTO POS
			sheet.autoSizeColumn((short)5);  // HORA CORTE BOLETA
			sheet.autoSizeColumn((short)6);  // CANTIDAD DE BINS
			sheet.autoSizeColumn((short)7);  // NOMBRE CLIENTE
			sheet.autoSizeColumn((short)8);  // DIRECCION
			sheet.autoSizeColumn((short)9);  // COMUNA
			sheet.autoSizeColumn((short)10); // TELEFONOS
			sheet.autoSizeColumn((short)11); // EMAIL
			sheet.autoSizeColumn((short)12); // TIPO_TARJETA

		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
    }
}
