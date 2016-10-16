/*
 * Creado el 30-nov-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.bol.command.faltantes;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.apache.poi.ss.util.CellRangeAddress;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.dto.ParametroConsultaFaltantesDTO;
import cl.bbr.bol.dto.ProductoFaltantesDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * @author Sebastian
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class InformeFaltantesExcel extends Command{
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		long idLocal = usr.getId_local();
		
		String inputJornadaConsulta = req.getParameter("jornadaActual");
		String fecha = req.getParameter("fechaConsulta");
		String indJornada = req.getParameter("indicadorJornada");
		String txtHorario = req.getParameter("txtHorario");
		req.getParameterMap();
		BizDelegate bizDelegate = new BizDelegate();
		ParametroConsultaFaltantesDTO parametro = new ParametroConsultaFaltantesDTO();
		parametro.setNumeroJornada(Long.parseLong(inputJornadaConsulta.compareToIgnoreCase("") != 0 ? inputJornadaConsulta : "0"));
		parametro.setFechaConsulta(sdf.parse(fecha));
		parametro.setIndicadorJornada(Long.parseLong(indJornada.compareToIgnoreCase("") != 0 ? indJornada : "0"));
		parametro.setIdLocal(idLocal);
		parametro.setTextoJornada(txtHorario);
		
		String strFila ="";
		try {
			HashMap informe = bizDelegate.getInformeFaltantes(parametro);
			List lista = (List)informe.get("listaResultado");
			String jornadaActual = (String)informe.get("jornadaActual");
			String fechaActual = (String)informe.get("fechaActual");
			String textoHorario = (String)informe.get("textoHorario");
			
			List productosList = new ArrayList();
			List listaJornadas = new ArrayList();
			for (int x = 0; x < lista.size(); x++){
				
				HashMap fila = new HashMap();
				List productos = (List)lista.get(x);
				ProductoFaltantesDTO prod = new ProductoFaltantesDTO();
				prod = (ProductoFaltantesDTO)productos.get(0);
				fila.put("idProducto", String.valueOf(prod.getIdProducto()));
				fila.put("descripcion", String.valueOf(prod.getDescripcion()));
				fila.put("sectorPicking", String.valueOf(prod.getSectorPicking()));
				
				for (int y = 0; y < productos.size(); y++){
					ProductoFaltantesDTO producto = new ProductoFaltantesDTO();
					producto = (ProductoFaltantesDTO)productos.get(y);
					fila.put("cantidadProductos", String.valueOf(producto.getCantidadProductos()));
					fila.put("presenciaProductosEnJornada", String.valueOf(producto.getPresenciaProductosEnJornada()));
					fila.put("opsTotalesPorJornada", String.valueOf(producto.getOpsTotalesPorJornada()));
					fila.put("porcentajePresenciaJ", String.valueOf(producto.getPorcentajePresencia()));
					if (!listaJornadas.contains(String.valueOf(producto.getIdJornada()))){
						listaJornadas.add(String.valueOf(producto.getIdJornada()));
					}
				}
				productosList.add(fila);
			}
			
			HSSFWorkbook wb = generaExcel(lista, listaJornadas);
			fechaActual = fechaActual.substring(6, 16);
			String filename = "InformeFaltantes-" + fechaActual + ".xls";

			res.setContentType("application/vnd.ms-excel");
			res.addHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
			OutputStream output = res.getOutputStream();
			wb.write(output);
			output.close();
				
		}catch(Exception e) {
			logger.error(e);
			throw new Exception(e);
		}
		
	}

	private static HSSFWorkbook generaExcel(List lista, List listaJornadas){
		
		HSSFWorkbook wb = new HSSFWorkbook();
		try{
			HSSFDataFormat format = wb.createDataFormat();

			HSSFSheet sheet = wb.createSheet("Informe Faltantes");
			
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
			
			/*UNION CELDAS*/
			CellRangeAddress bloque1 = new CellRangeAddress(0, 0, 3, 6);
			CellRangeAddress bloque2 = new CellRangeAddress(0, 0, 7, 10);
			CellRangeAddress bloque3 = new CellRangeAddress(0, 0, 11, 14);
			CellRangeAddress bloque4 = new CellRangeAddress(0, 0, 15, 18);
			
			sheet.addMergedRegion(bloque1);
			sheet.addMergedRegion(bloque2);
			sheet.addMergedRegion(bloque3);
			sheet.addMergedRegion(bloque4);
			HSSFRow row0 = sheet.createRow((short) 0);
			
			HSSFCell celdaJornada1 = row0.createCell(3);
			celdaJornada1.setCellValue(new HSSFRichTextString((String)listaJornadas.get(0)));
			celdaJornada1.setCellStyle(style);
			HSSFCell celdaJornada2 = row0.createCell(7);
			celdaJornada2.setCellValue(new HSSFRichTextString((String)listaJornadas.get(1)));
			celdaJornada2.setCellStyle(style);
			HSSFCell celdaJornada3 = row0.createCell(11);
			celdaJornada3.setCellValue(new HSSFRichTextString((String)listaJornadas.get(2)));
			celdaJornada3.setCellStyle(style);
			HSSFCell celdaJornada4 = row0.createCell(15);
			celdaJornada4.setCellValue(new HSSFRichTextString((String)listaJornadas.get(3)));
			celdaJornada4.setCellStyle(style);
			/*FIN UNION CELDAS*/
			
			/*Creacion Filas Cabecera*/
			HSSFRow row1 = sheet.createRow((short) 1);
			
			HSSFCell cell_0_0 = row1.createCell(0);
			cell_0_0.setCellValue(new HSSFRichTextString("Id Producto"));
			cell_0_0.setCellStyle(style);
			
			HSSFCell cell_0_1 = row1.createCell(1);
			cell_0_1.setCellValue(new HSSFRichTextString("Descripción"));
			cell_0_1.setCellStyle(style);
			
			HSSFCell cell_0_2 = row1.createCell(2);
			cell_0_2.setCellValue(new HSSFRichTextString("Sector"));
			cell_0_2.setCellStyle(style);
			
			int contador = 3;
			for (int x = 0; x < listaJornadas.size(); x++){
				HSSFCell cell_0_3 = row1.createCell(contador);
				cell_0_3.setCellValue(new HSSFRichTextString("CantidadProductos"));
				cell_0_3.setCellStyle(style);
				contador++;
				HSSFCell cell_0_4 = row1.createCell(contador);
				cell_0_4.setCellValue(new HSSFRichTextString("Presencia en Jornada"));
				cell_0_4.setCellStyle(style);
				contador++;
				HSSFCell cell_0_5 = row1.createCell(contador);
				cell_0_5.setCellValue(new HSSFRichTextString("Pedidos por Jornada"));
				cell_0_5.setCellStyle(style);

				contador++;
				HSSFCell cell_0_6 = row1.createCell(contador);
				cell_0_6.setCellValue(new HSSFRichTextString("Porcentaje Presencia"));
				cell_0_6.setCellStyle(style);
				contador++;
			}
			
			int contadorFila = 2;
			List productosList = new ArrayList();
			for (int x = 0; x < lista.size(); x++){ 
				HashMap fila = new HashMap();
				List productos = (List)lista.get(x);
				ProductoFaltantesDTO prod = new ProductoFaltantesDTO();
				prod = (ProductoFaltantesDTO)productos.get(0);
				//Creacion nueva Fila
				HSSFRow row = sheet.createRow((short) contadorFila);	
				
				HSSFCell cell_0 = row.createCell(0);
				cell_0.setCellValue(new HSSFRichTextString(String.valueOf(prod.getIdProducto())));
				cell_0.setCellStyle(style2);
				
				HSSFCell cell_1 = row.createCell(1);
				cell_1.setCellValue(new HSSFRichTextString(String.valueOf(prod.getDescripcion())));
				cell_1.setCellStyle(style2);
				
				HSSFCell cell_2 = row.createCell(2);
				cell_2.setCellValue(new HSSFRichTextString(String.valueOf(prod.getSectorPicking())));
				cell_2.setCellStyle(style2);
				int k = 3;
				for (int y = 0; y < productos.size(); y++){
					ProductoFaltantesDTO producto = new ProductoFaltantesDTO();
					producto = (ProductoFaltantesDTO)productos.get(y);
					
					HSSFCell cell_3 = row.createCell(k);
					cell_3.setCellValue(new HSSFRichTextString(String.valueOf(producto.getCantidadProductos())));
					cell_3.setCellStyle(style2);
					k++;
					
					HSSFCell cell_4 = row.createCell(k);
					cell_4.setCellValue(new HSSFRichTextString(String.valueOf(producto.getPresenciaProductosEnJornada())));
					cell_4.setCellStyle(style2);
					k++;
					
					HSSFCell cell_5 = row.createCell(k);
					cell_5.setCellValue(new HSSFRichTextString(String.valueOf(producto.getOpsTotalesPorJornada())));
					cell_5.setCellStyle(style2);
					k++;
					
					HSSFCell cell_6 = row.createCell(k);
					cell_6.setCellValue(new HSSFRichTextString(String.valueOf(producto.getPorcentajePresencia())));
					cell_6.setCellStyle(style2);
					k++;
				}
				contadorFila++;
			}		
			/*Autoajuste de Celdas*/
			for (int x = 0; x<19; x++){
				sheet.autoSizeColumn((short)x);
			}
			  
		}catch(Exception e){
			e.printStackTrace();
		}
		return wb;
    }
}
