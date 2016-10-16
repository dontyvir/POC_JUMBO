package cl.cencosud.jumbo.main;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import cl.cencosud.jumbo.data.OpsNuevasVsFinalizadasData;
import cl.cencosud.jumbo.util.ConnectionDB2;
import cl.cencosud.jumbo.util.JFactory;
import cl.cencosud.jumbo.util.Util;

public class OpsNuevasVsFinalizadas {
	
	protected static Logger logger = Logger.getLogger(OpsNuevasVsFinalizadas.class.getName());

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args)  {
		
		try {
			logger.info("INIT REPORTE Ops Nuevas Vs Finalizadas");
			 
			String fechaSolicitada  = (args.length > 0)?args[0].toString():null;
			if(fechaSolicitada != null && !Util.validaFechaParam(fechaSolicitada)){
				String f = "Parametro fecha:["+fechaSolicitada+"] es invalido";
				throw new Exception(f);
			}
			
			Connection conn = JFactory.getConnectionDB2();
			String fecha_reporte ="";
			if(fechaSolicitada != null){
				logger.info("PARAM FECHA REPORTE [MANUAL] " +fechaSolicitada);
				fecha_reporte=fechaSolicitada;
			}else{
				fecha_reporte=OpsNuevasVsFinalizadasData.getfechaReporte(conn);
				logger.info("PARAM FECHA REPORTE [AUTOMATICO] " +fecha_reporte);
			}			
			
			List list_OpsCreadas 	= OpsNuevasVsFinalizadasData.getOpsCreadas(conn, fechaSolicitada);
			List list_OpsFinalizada = OpsNuevasVsFinalizadasData.getOpsFinalizadas(conn, fechaSolicitada);

			ConnectionDB2.closeConnectionDB2();  

			enviaInforme(creaInforme(list_OpsCreadas, list_OpsFinalizada, fecha_reporte));
			logger.info("END REPORTE Ops Nuevas Vs Finalizadas");
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		
	}
	
	public static File creaInforme(List list_OpsCreadas, List list_OpsFinalizada, String fecha_reporte) throws Exception {	

		logger.info("Init Construccion de xls");			
		String rutaInforme = Util.getPropertiesString("path.archivo.excel") + fecha_reporte +"_OpsNuevasVsFinalizadas.xls";
		HSSFWorkbook libro = JFactory.getHSSFWorkbook();
		
		HSSFCell celda = null;
		int contFilas = 0;
		
		HSSFCellStyle styleHead = libro.createCellStyle();
		styleHead.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		styleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleHead.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		styleHead.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		styleHead.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		styleHead.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		
		HSSFCellStyle stylebody = libro.createCellStyle();
		stylebody.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        stylebody.setBorderTop(HSSFCellStyle.BORDER_THIN);
        stylebody.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        stylebody.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		//____________________________________________________________________________________________________________________________
		//Ops creadas		
		String colName_OpsCreadas[] ={"ID_PEDIDO","FCREACION","HCREACION","ID_LOCAL","COD_LOCAL","NOMBRE_LOCAL",
				"RUT_CLIENTE","DV_CLIENTE","ID_JDESPACHO","FECHA_DESPACHO","MONTO_RESERVA","MEDIO_PAGO","ID_ESTADO","NOMBRE_ESTADO"};
		
		HSSFSheet hoja	= libro.createSheet("Ops Creadas");
		hoja.setGridsPrinted(false);
		hoja.setPrintGridlines(false);
		hoja.setDisplayGridlines(false); 
		
		HSSFRow fila	= hoja.createRow(contFilas);		
		for(int i=0; i < colName_OpsCreadas.length; i++){
			celda = fila.createCell(i);
			celda.setCellStyle(styleHead);
			celda.setCellValue(new HSSFRichTextString(colName_OpsCreadas[i].replaceAll("_"," ")));
            // FIXME: Utiliza librerias X11 no disponibles en COPIHUE
			// hoja.autoSizeColumn((short) i);
		}
		
		contFilas++;
		Iterator it =  list_OpsCreadas.iterator();
		while (it.hasNext()) {
			Map opsCreadas = (Map) it.next(); 
			
			fila = hoja.createRow(contFilas);
			for(int i=0; i < colName_OpsCreadas.length; i++){
				celda = fila.createCell(i);
				celda.setCellStyle(stylebody);				
				celda.setCellValue(new HSSFRichTextString(String.valueOf(opsCreadas.get(colName_OpsCreadas[i]))));
			}
			contFilas++;
		}
		//____________________________________________________________________________________________________________________________
		//Ops finalizadas		
		String colName_OpsFinalizada[] = {"ID_PEDIDO","FECHA_FINALIZACION","ID_LOCAL","COD_LOCAL","NOMBRE_LOCAL","ID_ESTADO","NOMBRE_ESTADO",
				"MEDIO_PAGO","CAT_COD","CAT_FINAL","TBK_COD","TBK_FINAL","CLI_EMAIL","IDTRXMP","MONTO_COBRADO","RUT_CLIENTE","DV_CLIENTE"};
		
		contFilas = 0;
		hoja = libro.createSheet("Ops Finalizadas");
		hoja.setGridsPrinted(false);
		hoja.setPrintGridlines(false);
		hoja.setDisplayGridlines(false); 
		
		fila = hoja.createRow(contFilas);
		for(int i=0; i < colName_OpsFinalizada.length; i++){
			celda = fila.createCell(i);
			celda.setCellStyle(styleHead);
			celda.setCellValue(new HSSFRichTextString(colName_OpsFinalizada[i].replaceAll("_"," ")));
            // FIXME: Utiliza librerias X11 no disponibles en COPIHUE
			// hoja.autoSizeColumn((short) i);
		}
		
		contFilas++;
		it =  list_OpsFinalizada.iterator();
		while (it.hasNext()) {
			Map opsFinalizada = (Map) it.next(); 
			
			fila = hoja.createRow(contFilas);
			for(int i=0; i < colName_OpsFinalizada.length; i++){
				celda = fila.createCell(i);
				celda.setCellStyle(stylebody);
				celda.setCellValue(new HSSFRichTextString(String.valueOf(opsFinalizada.get(colName_OpsFinalizada[i]))));
			}
			contFilas++;
		}		
		logger.info("Se creara el archivo excel " + rutaInforme);
		logger.info("Cantidad OpsCreadas: " + list_OpsCreadas.size());	
		logger.info("Cantidad OpsFinalizadas: " + list_OpsFinalizada.size());
	
		FileOutputStream setFile = new FileOutputStream(rutaInforme);
		libro.write(setFile);
		setFile.close();			
		logger.info("End Construccion de xls");
		
		return new File(rutaInforme);
	}
	
	public static void enviaInforme(File file) throws Exception {
		
		logger.info("Init Enviar correo");		
		
		String host 	= Util.getPropertiesString("mail.smtp.host");
		String from 	= Util.getPropertiesString("mail.from");		
		String to 		= Util.getPropertiesString("mail.to");
		String cc 		= Util.getPropertiesString("mail.cc");		
		String subject	= Util.getPropertiesString("mail.subject");		
		String bodyHtml = Util.getPropertiesString("mail.bodyHtml");
		String sendmail = Util.getPropertiesString("mail.send");
				
		
		List archivos = new ArrayList(1);
		archivos.add(file);

		if("1".equals(sendmail)){
			logger.info("Conectando SMTP host["+host+"]");
			
			if (logger.isDebugEnabled()){
				logger.debug("host["+host+"]");
				logger.debug("from["+from+":");
				logger.debug("to["+to+"]");
				logger.debug("cc["+cc+"]");
				logger.debug("subject["+subject+"]");
				logger.debug("bodyHtml["+bodyHtml+"]");
			}
			
			JFactory.getSendMail(host, from, bodyHtml, archivos).enviar(to, cc, subject);
			
		}else{
			logger.info("[IMPORTANTE] La funcion que envia el reporte via email se encuentra DESACTIVADA (Para activar el envio de email cambie el parametro 'mail.send=1' en la configuracion.)");
		}
		
		logger.info("End Enviar correo");
	}

}
