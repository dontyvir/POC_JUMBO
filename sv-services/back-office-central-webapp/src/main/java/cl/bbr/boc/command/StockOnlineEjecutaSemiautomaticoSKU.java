package cl.bbr.boc.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

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

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.BOStockONLineDTO;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.utils.Constantes;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * 
 * @author jolazogu
 *
 */
public class StockOnlineEjecutaSemiautomaticoSKU extends Command{

    private static final long serialVersionUID = 6373335584019595322L;
    private static Logging logger = new Logging(StockOnlineEjecutaSemiautomaticoSKU.class); 
    private static ResourceBundle rb = ResourceBundle.getBundle( "bo" );
    private static String path = rb.getString("PATH_ARCHIVOS_STOCK_ONLINE");
    private static String pathRespaldos = rb.getString( "PATH_ARCHIVOS_STOCK_ONLINE" );
    private static String SEMIAUTOMATICO = "SemiAutomatico";
	private static String XLS = "xls";
			 
  	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

  		JdbcTransaccion trx = new JdbcTransaccion();
  		
  		try{
			
  			trx.begin();
  			
  			res.setCharacterEncoding( "UTF-8" );
		    View salida = new View( res );

	        String html = getServletConfig().getInitParameter( "TplFile" );
	        html = path_html + html;
	        
	        TemplateLoader load = new TemplateLoader( html );
	        ITemplate tem = load.getTemplate();
	        IValueSet top = new ValueSet();
  			
			logger.info( "Inicio de StockOnlineEjecutaSemiautomaticoSKU ::" + usr.getLogin() );
			
			String msjRetorno = "";
			
			int localCheck = 5;
	    	
			if( req.getParameter( "local_check" ) != null) {
			
				localCheck = Integer.parseInt( req.getParameter( "local_check" ) );
			
			}
			
			BizDelegate biz = new BizDelegate();
			
	    	LocalDTO local = biz.getLocalById( localCheck );

	    	long[] cantidadesActualmente = { 0, 0, 0, 0 };
			long[] cantidadesDespues = { 0, 0, 0, 0, 0 };
		    
	    	cantidadesActualmente = biz.cantidadDeProductosActualmente( local.getId_local() );
		    cantidadesDespues = biz.cantidadDeProductosTendranCambios( local.getId_local() );
	    	
		    List ProductosDetallesSemiautomatico = biz.getProductosDetalleSemiautomatico(local.getId_local());

	    	logger.debug("Entrando a realizar los cambios Semiautomatico....");
		    if( biz.setEjecutaProcesoSemiAutomatico( local.getId_local() ) ) {

		    	logger.debug("eliminando archivo....");
		    	eliminarArchivo(local);
		    	
		    	generaExcel( local, ProductosDetallesSemiautomatico );
		    	logger.debug("archivo Excel Terminado....");

		    	
		    	enviarMailStockOnline( biz, local, cantidadesActualmente, cantidadesDespues );	    	
		    	logger.debug("Email enviado con archivo Excel adjunto....");
		    	
		    	msjRetorno = "Los cambios se realizaron con exito en local " +local.getNom_local();
	   		
		    	//biz.getLimpiarTablaStockOnlinePorLocal( local.getId_local() );
		    	
		    	// confirmar que se realizo semiautomatico y no volver a mostrar el detalle nuevamente
		    	biz.getConfirmarSemiautomaticoStockOnlinePorLocal( local.getId_local() );
		    	
		    	logger.debug("eliminando archivo....");
		    	eliminarArchivo(local);
		    	
		    }else{
				
	   			msjRetorno = "No se pudo realizar los cambios en el local " +local.getNom_local();
			
		    }

	    top.setVariable( "{ejecutaSemiautomatico}", msjRetorno );        
	    top.setVariable( "{btnEjecutaDetalle}", "none" );
    	top.setVariable( "{resumen}", "none" );
	        
	    String result = tem.toString( top );
	    
	    salida.setHtmlOut( result );
	    
	    salida.Output();
	        
	    }catch( Throwable e ){
		
	    	trx.rollback();
  			e.printStackTrace();
			logger.error( "El proceso no se ejecutó correctamente (ejecutar nuevamente)- problemas al procesar: " + e );
		
  		}
  		
  		trx.end();
	
  	}
  	
    	
  	private static void enviarMailStockOnline( BizDelegate biz, LocalDTO local, long[] cantidadesActualmente, long[] cantidadesDespues ) throws Exception {
  		
  		logger.debug("creando email....");
  		
  		String formatoMail = "/mails/mailStockOnlineSemiAutomatico.html";

   		String mail_tpl = rb.getString( "conf.path.html" ) + "" + formatoMail;

   		logger.debug("creando email...." +mail_tpl);
   		
   		TemplateLoader mail_load = new TemplateLoader( mail_tpl );
   		ITemplate mail_tem = mail_load.getTemplate();	

   		String mail_result = mail_tem.toString( contenidoMailStockOnline( biz, local, cantidadesActualmente, cantidadesDespues ) );
   		
   		String ruta = pathRespaldos + "/" + nombreArchivoStockOnline( pathRespaldos, SEMIAUTOMATICO+"_"+local.getCod_local(), XLS );
   		
   		File file = new File( ruta );
   		
   		logger.info("Init	Enviar correo  archivo " +file.getName());
		String to = rb.getString("stockOnline.destinatario");
		String cc = rb.getString("stockOnline.cc");
		String host = rb.getString("stockOnline.mail.smtp.host");
		String subject = "Resumen Stock Online Proceso Semi Automático";
		String from = "Stock Online <no-reply@cencosud.cl>";
		
		List archivos = new ArrayList();
		archivos.add(file);
		
		new SendMail(host, from, mail_result, archivos).enviar(to, cc, subject);
		
		logger.debug("	Host: " + host);
		logger.debug("	To: " + to);
		logger.debug("	Cc: " + cc);
		logger.info("End	Enviar correo");
		
	}
  	
  	public static String nombreArchivoStockOnline( String ruta, String prefijo, String extension ) {

        if ( ruta != null )
             path = ruta;
        
        File directorio = new File(path+"/");

        String[] lista = directorio.list(new Filtro(prefijo, extension));

        if ( lista.length == 1 )
             return lista[0];
        
        String archivo = "";
        for ( int i = 0; i < lista.length; i++ ) {
            
        	archivo = lista[i].substring(0,19);
             
        	if( archivo.equals( prefijo ) )
            	 return lista[i];
             
        }

        return archivo;
     
  	}
  	
  	/**
  	 * 
  	 * @param local
  	 * @param ProductosDetallesSemiautomatico
  	 * @return
  	 * @throws IOException
  	 */
  	private static HSSFWorkbook generaExcel( LocalDTO local, List ProductosDetallesSemiautomatico ) throws IOException{
		
  		logger.debug("*******************************************creando archivo Excel....");
    	
  		String ruta =  pathRespaldos + "/" + getArchivoSemiAutomaticoXLS( local.getCod_local() );
    	
  		File file = new File( ruta );
		logger.debug("creando archivo Excel...." +file);
    	
  		
		HSSFWorkbook wb = new HSSFWorkbook();
		
		try {

            HSSFSheet sheet = wb.createSheet("Planilla Stock Online");
			
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
			cell_0_1.setCellValue(new HSSFRichTextString("LOCAL"));
			cell_0_1.setCellStyle(style);
			
			HSSFCell cell_0_2 = row0.createCell(1);
			cell_0_2.setCellValue(new HSSFRichTextString("SKU"));
			cell_0_2.setCellStyle(style);
			
			/*HSSFCell cell_0_3 = row0.createCell(2);
			cell_0_3.setCellValue(new HSSFRichTextString("STOCK REAL"));
			cell_0_3.setCellStyle(style);*/
			
			HSSFCell cell_0_3 = row0.createCell(2);
			cell_0_3.setCellValue(new HSSFRichTextString("STOCK MINIMO"));
			cell_0_3.setCellStyle(style);
			
			HSSFCell cell_0_4 = row0.createCell(3);
			cell_0_4.setCellValue(new HSSFRichTextString("ID PRODUCTO"));
			cell_0_4.setCellStyle(style);
			
			HSSFCell cell_0_5 = row0.createCell(4);
			cell_0_5.setCellValue(new HSSFRichTextString("ESTADO INICIAL"));
			cell_0_5.setCellStyle(style);
			
			HSSFCell cell_0_6 = row0.createCell(5);
			cell_0_6.setCellValue(new HSSFRichTextString("ESTADO FINAL"));
			cell_0_6.setCellStyle(style);
			
			HSSFCell cell_0_7 = row0.createCell(6);
			cell_0_7.setCellValue(new HSSFRichTextString("RESULTADO TRANSACCIÓN"));
			cell_0_7.setCellStyle(style);

			HSSFCell cell_0_8 = row0.createCell(7);
			cell_0_8.setCellValue(new HSSFRichTextString("ESTADO"));
			cell_0_8.setCellStyle(style);

				
			/*****************************************************/
			int j = 0;
			
			Iterator it = ProductosDetallesSemiautomatico.iterator();
			
			while (it.hasNext()) {
				
				BOStockONLineDTO stockOnLine = (BOStockONLineDTO)it.next();
				
				j++;
			    
			    HSSFRow row = sheet.createRow((int) j);

			    HSSFCell cell_1 = row.createCell(0);
				cell_1.setCellValue(new HSSFRichTextString(local.getNom_local()));
				cell_1.setCellStyle(style2);
			    
				HSSFCell cell_2 = row.createCell(1);
				cell_2.setCellValue(new HSSFRichTextString(stockOnLine.getSkuProducto()));
				cell_2.setCellStyle(style2);

				/*HSSFCell cell_3 = row.createCell(2);
				cell_3.setCellValue(new HSSFRichTextString(String.valueOf(stockOnLine.getStockReal())));
				cell_3.setCellStyle(style3);*/

				HSSFCell cell_3 = row.createCell(2);
				cell_3.setCellValue(new HSSFRichTextString(String.valueOf(stockOnLine.getStockMinimo())));
				cell_3.setCellStyle(style3);
				
				HSSFCell cell_4 = row.createCell(3);
				cell_4.setCellValue(new HSSFRichTextString(String.valueOf(stockOnLine.getIdProducto())));
				cell_4.setCellStyle(style3);
				
				HSSFCell cell_5 = row.createCell(4);
				cell_5.setCellValue(new HSSFRichTextString(stockOnLine.getEstadoInicial()));
				cell_5.setCellStyle(style3);
				
				HSSFCell cell_6 = row.createCell(5);
				cell_6.setCellValue(new HSSFRichTextString(stockOnLine.getEstadoFinal()));
				cell_6.setCellStyle(style3);
								
				HSSFCell cell_7 = row.createCell(6);
				cell_7.setCellValue(new HSSFRichTextString(stockOnLine.getResultadoTransaccion()));
				cell_7.setCellStyle(style3);
				
				HSSFCell cell_8 = row.createCell(7);
				cell_8.setCellValue(new HSSFRichTextString(getEstado(stockOnLine.getEstado())));
				cell_8.setCellStyle(style3);
				
			}
			
			/*sheet.autoSizeColumn((short)0);
			sheet.autoSizeColumn((short)1);
			sheet.autoSizeColumn((short)2);
			sheet.autoSizeColumn((short)3);
			sheet.autoSizeColumn((short)4);
			sheet.autoSizeColumn((short)5);
			sheet.autoSizeColumn((short)6);
			sheet.autoSizeColumn((short)7);
			sheet.autoSizeColumn((short)8);*/
			
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);
	        fileOut.close();
			
		}catch(Exception e) {
			
			e.printStackTrace();
		
		}
		
		return wb;
   
    }
  	
  	
  	/**
	 * 
	 * @return
	 */
	private static String getArchivoSemiAutomaticoXLS( String cod_local ) {

		Date date = new Date();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
		
		String nombre = "SemiAutomatico_" + cod_local + "_" +dateFormat.format( date ) + ".xls";
		
		return nombre;
		
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
	

  	/**
  	 * 
  	 * @param biz
  	 * @return
  	 * @throws BocException 
  	 */
  	private static IValueSet contenidoMailStockOnline( BizDelegate biz, LocalDTO local, long[] cantidadesActualmente, long[] cantidadesDespues ) throws BocException {
  		
  		IValueSet mail_top = new ValueSet();
  		
  		
  		mail_top.setVariable( "{local}",local.getNom_local());
    	
  		mail_top.setVariable( "{publicadosConStock}", formatoNumero( cantidadesActualmente[ 1 ] ) );
	    mail_top.setVariable( "{publicadosSinStock}", formatoNumero( cantidadesActualmente[ 0 ] ) );
	    mail_top.setVariable( "{despublicados}", formatoNumero( cantidadesActualmente[ 2 ] ) );
      
	    
	    mail_top.setVariable( "{cambioConStock}", "+" + formatoNumero( cantidadesDespues[ 0 ] ) + ", -" + formatoNumero( cantidadesDespues[ 2 ] ) + "" );
	    mail_top.setVariable( "{cambioSinStock}", "+" + formatoNumero( cantidadesDespues[ 1 ] ) + ", -" + formatoNumero( cantidadesDespues[ 3 ] ) + "" );
	   
	    mail_top.setVariable( "{maestra}", formatoNumero( biz.getTotalMaestra( local.getId_local() ) ) );
	   
	    mail_top.setVariable( "{tipoProceso}", "Semi Autom&aacute;tico"  );
	    mail_top.setVariable( "{proceso}", "Autom&aacute;tico"  );  
	    
	    
  		return mail_top;
  	
  	}
 	
  	
  	public static String formatoNumero( long num ) {
		
		NumberFormat df = NumberFormat.getInstance();
		df.setMaximumFractionDigits(2);		
		return df.format( num );

	}	

 

	/**
     * @param string
     */
    public static void eliminarArchivo( LocalDTO local ) {

    	File file = new File( pathRespaldos + "/" + nombreArchivoStockOnline( pathRespaldos, SEMIAUTOMATICO+"_"+local.getCod_local(), XLS ) );
        file.delete();
        
        logger.debug(" archivo eliminado....");
    	
    }




       

}