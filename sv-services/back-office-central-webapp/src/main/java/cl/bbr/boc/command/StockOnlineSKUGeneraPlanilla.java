package cl.bbr.boc.command;


import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.BOStockONLineDTO;
import cl.bbr.boc.utils.Constantes;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;



/**
 * 
 * @author jolazogu
 *
 */
public class StockOnlineSKUGeneraPlanilla extends Command {
	
	private static final long serialVersionUID = 1L;
 
 
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
    	JdbcTransaccion trx = new JdbcTransaccion();
    	
		try {
			
			trx.begin();
			
			BizDelegate biz = new BizDelegate();
			
			int localCheck = 5;
			
			if( req.getParameter( "local_check" ) != null) {
			
				localCheck = Integer.parseInt( req.getParameter( "local_check" ) );
			}
				
			LocalDTO local = new LocalDTO(); 
			local = biz.getLocalById( localCheck );
			
			List listaProductos = biz.getListaProductosPorLocal( local.getId_local() );
			
			HSSFWorkbook wb = generaExcel( listaProductos );
			
			res.setContentType ( "application/vnd.ms-excel" );
            res.addHeader( "Content-Disposition", "inline; filename=StockOnLineLocal-" +local.getCod_local()+ Constantes.XLS );
			OutputStream output = res.getOutputStream();
			wb.write( output );
            output.close();
    		
        }catch( Exception e ){
        	
        	trx.rollback();
	        e.printStackTrace();
	    
        }
    
		 trx.end();
		 
    }

    /**
     * 
     * @param listaProductos
     * @return
     */
    private static HSSFWorkbook generaExcel(List listaProductos){
		
		HSSFWorkbook wb = new HSSFWorkbook();
		
		try {

            HSSFSheet sheet = wb.createSheet(Constantes.TITULO_HOJA_STOCK_ONLINE);
			
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short)10);
			font.setFontName("Arial");
			font.setItalic(false);

			HSSFFont font2 = wb.createFont();
			font2.setFontHeightInPoints((short)10);
			font2.setFontName("Arial");
			font2.setItalic(false);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font2.setColor(HSSFColor.WHITE.index);
			
			HSSFFont font3 = wb.createFont();
			font3.setFontHeightInPoints((short)10);
			font3.setFontName("Arial");
			font3.setItalic(false);
			font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font3.setColor(HSSFColor.BLACK.index);
			
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
			
			HSSFCellStyle style1 = wb.createCellStyle();
			style1.setFont(font3);
			style1.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			style1.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style1.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			style1.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			style1.setFillBackgroundColor(HSSFColor.BLUE.index);
			style1.setFillForegroundColor(HSSFColor.YELLOW.index);
			style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			HSSFCellStyle style2 = wb.createCellStyle();
			style2.setFont(font);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setFillForegroundColor(HSSFColor.WHITE.index);
			
			HSSFCellStyle style3 = wb.createCellStyle();
			style3.setFont(font);
			style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style3.setFillForegroundColor(HSSFColor.WHITE.index);
			style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			HSSFCellStyle style4 = wb.createCellStyle();
			style4.setFont(font);
			style4.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style4.setFillForegroundColor(HSSFColor.WHITE.index);
			style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			
			HSSFRow row0 = sheet.createRow((short) 0);
			
			HSSFCell cell_0_1 = row0.createCell(0);
			cell_0_1.setCellValue(new HSSFRichTextString(Constantes.PRIMERA_CELDA));
			cell_0_1.setCellStyle(style);
			
			HSSFCell cell_0_2 = row0.createCell(1);
			cell_0_2.setCellValue(new HSSFRichTextString(Constantes.SEGUNDA_CELDA));
			cell_0_2.setCellStyle(style);
			
			HSSFCell cell_0_3 = row0.createCell(2);
			cell_0_3.setCellValue(new HSSFRichTextString(Constantes.TERCERA_CELDA));
			cell_0_3.setCellStyle(style);
			
			HSSFCell cell_0_4 = row0.createCell(3);
			cell_0_4.setCellValue(new HSSFRichTextString(Constantes.CUARTA_CELDA));
			cell_0_4.setCellStyle(style);
			
			HSSFCell cell_0_5 = row0.createCell(4);
			cell_0_5.setCellValue(new HSSFRichTextString(Constantes.QUINTA_CELDA));
			cell_0_5.setCellStyle(style);
			
			HSSFCell cell_0_6 = row0.createCell(5);
			cell_0_6.setCellValue(new HSSFRichTextString(Constantes.SEXTA_CELDA));
			cell_0_6.setCellStyle(style);
			
			HSSFCell cell_0_7 = row0.createCell(6);
			cell_0_7.setCellValue(new HSSFRichTextString(Constantes.SEPTIMA_CELDA));
			cell_0_7.setCellStyle(style);
			
			HSSFCell cell_0_8 = row0.createCell(7);
			cell_0_8.setCellValue(new HSSFRichTextString(Constantes.OCTAVA_CELDA));
			cell_0_8.setCellStyle(style);

			HSSFCell cell_0_9 = row0.createCell(8);
			cell_0_9.setCellValue(new HSSFRichTextString(Constantes.NOVENA_CELDA));
			cell_0_9.setCellStyle(style1);

			HSSFCell cell_0_10 = row0.createCell(9);
			cell_0_10.setCellValue(new HSSFRichTextString(Constantes.DECIMA_CELDA));
			cell_0_10.setCellStyle(style1);

			HSSFCell cell_0_11 = row0.createCell(10);
			cell_0_11.setCellValue(new HSSFRichTextString(Constantes.UNDECIMA_CELDA));
			cell_0_11.setCellStyle(style1);

				
			/*****************************************************/
			int j = 0;
			
			Iterator it = listaProductos.iterator();
			
			while (it.hasNext()) {
				
				BOStockONLineDTO stockOnLine = (BOStockONLineDTO)it.next();
			    
				j++;
			    
			    HSSFRow row = sheet.createRow((int) j);

			    HSSFCell cell_1 = row.createCell(0);
				cell_1.setCellValue(new HSSFRichTextString(stockOnLine.getCodLocal()));
				cell_1.setCellStyle(style2);
			    
				HSSFCell cell_2 = row.createCell(1);
				cell_2.setCellValue(new HSSFRichTextString(stockOnLine.getNombreLocal()));
				cell_2.setCellStyle(style2);

				HSSFCell cell_3 = row.createCell(2);
				cell_3.setCellValue(new HSSFRichTextString(stockOnLine.getNombreSeccion()));
				cell_3.setCellStyle(style3);

				HSSFCell cell_4 = row.createCell(3);
				cell_4.setCellValue(new HSSFRichTextString(stockOnLine.getNombreRubro()));
				cell_4.setCellStyle(style3);
				
				HSSFCell cell_5 = row.createCell(4);
				cell_5.setCellValue(new HSSFRichTextString(stockOnLine.getNombreSubRubro()));
				cell_5.setCellStyle(style3);
				
				HSSFCell cell_6 = row.createCell(5);
				cell_6.setCellValue(new HSSFRichTextString(stockOnLine.getSkuProducto()));
				cell_6.setCellStyle(style3);
				
				HSSFCell cell_7 = row.createCell(6);
				cell_7.setCellValue(new HSSFRichTextString(stockOnLine.getNombreProducto()));
				cell_7.setCellStyle(style3);
								
				HSSFCell cell_8 = row.createCell(7);
				cell_8.setCellValue(new HSSFRichTextString(stockOnLine.getUnidadDeMedida()));
				cell_8.setCellStyle(style3);
				
				HSSFCell cell_9 = row.createCell(8);
				cell_9.setCellValue(new HSSFRichTextString(getModo(stockOnLine.getModo())));
				cell_9.setCellStyle(style3);
				
				HSSFCell cell_10 = row.createCell(9);
				cell_10.setCellValue(new HSSFRichTextString(String.valueOf(stockOnLine.getStockMinimo())));
				cell_10.setCellStyle(style4);
				
				HSSFCell cell_11 = row.createCell(10);
				cell_11.setCellValue(new HSSFRichTextString(getEstado(stockOnLine.getEstado())));
				cell_11.setCellStyle(style3);

			}
			
			sheet.autoSizeColumn((short)0);
			sheet.autoSizeColumn((short)1);
			sheet.autoSizeColumn((short)2);
			sheet.autoSizeColumn((short)3);
			sheet.autoSizeColumn((short)4);
			sheet.autoSizeColumn((short)5);
			sheet.autoSizeColumn((short)6);
			sheet.autoSizeColumn((short)7);
			sheet.autoSizeColumn((short)8);
			sheet.autoSizeColumn((short)9);
			sheet.autoSizeColumn((short)10);
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
		
		}
		
		return wb;
    
    }


    /**
     * 
     * @param modo
     * @return
     */
	private static String getModo( int modo ) {
		
		String nombreModo = "-------";

		if( Constantes.CONSTANTE_MODO_INT_ON == modo ){
			
			nombreModo = Constantes.CONSTANTE_MODO_ON;		
		
		}else if( Constantes.CONSTANTE_MODO_INT_OFF == modo ){
			
			nombreModo = Constantes.CONSTANTE_MODO_OFF;		
		
		}
		
		return nombreModo;

	}


	/**
	 * 
	 * @param estado
	 * @return
	 */
	private static String getEstado( int estado ) {
		
		String nombreEstado = "-------";
		
		if( Constantes.CONSTANTE_ESTADO_INT_AUTOMATICO == estado ){
		
			nombreEstado = Constantes.CONSTANTE_ESTADO_AUTOMATICO;		
		
		}else if( Constantes.CONSTANTE_ESTADO_INT_SEMIAUTOMATICO == estado ){
			
			nombreEstado = Constantes.CONSTANTE_ESTADO_SEMIAUTOMATICO;		
		
		}
		
		return nombreEstado;
	
	}
	
}
