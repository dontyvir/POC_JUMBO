package cl.cencosud.jumbo.main;

import java.io.File;
import java.io.FileOutputStream;
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

import cl.cencosud.jumbo.data.InformeVentaEmpresaData;
import cl.cencosud.jumbo.util.JFactory;
import cl.cencosud.jumbo.util.Util;

public class InformeVentaEmpresa {
	
	protected static Logger logger = Logger.getLogger(InformeVentaEmpresa.class.getName());

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args)  {
		
		try {
			logger.info("INIT -- Informe Venta Empresa");
			 
			String fechaSolicitadaInicio  = (args.length > 0)?args[0].toString():null;
			String fechaSolicitadaFin     = (args.length == 2)?args[1].toString():null;

			
			if(fechaSolicitadaInicio != null && fechaSolicitadaFin != null){
				
				if(!Util.validaFechaParam(fechaSolicitadaInicio)){
					String f = "Parametro fecha inicio:["+fechaSolicitadaInicio+"] invalida";
					throw new Exception(f);
				}
				
				if(!Util.validaFechaParam(fechaSolicitadaFin)){
					String f = "Parametro fecha fin:["+fechaSolicitadaFin+"] invalida";
					throw new Exception(f);
				}
			}
					
			InformeVentaEmpresaData oInforme = new InformeVentaEmpresaData();
			List listInformeVenta = oInforme.getVentaEmpresa(fechaSolicitadaInicio, fechaSolicitadaFin);
			String fechaReporte = oInforme.getfechaBD();
			List destinatariosReporte = oInforme.getEmailUsuarios();
			oInforme.purge();
			
			enviaInforme(creaInforme(listInformeVenta, fechaReporte), destinatariosReporte);
			logger.info("END -- Informe Venta Empresa");
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		
	}
	
	public static File creaInforme(List listInformeVenta, String fechaReporte) throws Exception {	

		logger.info("Init Construccion de xls");			
		String rutaInforme = Util.getPropertiesString("path.archivo.excel") + fechaReporte +"_InformeVentaEmpresa.xls";
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
		String colNameEscel[] ={ "FFACT","TIPODOCTO","NDOCUMENTO", "FECHA", "FCREACION", "ID_PEDIDO", "RUT_CLIENTE", "NOM_CLIENTE",                                                                                                                                               
				 "TELEFONO", "TELEFONO2", "POS_MONTO_FP", "MEDIO_PAGO", "NOM_LOCAL", "NOMBRE", "REG_NOMBRE", "ESTADO"};
		
		HSSFSheet hoja	= libro.createSheet("Venta Empresa");
		hoja.setGridsPrinted(false);
		hoja.setPrintGridlines(false);
		hoja.setDisplayGridlines(false); 
		
		HSSFRow fila	= hoja.createRow(contFilas);		
		for(int i=0; i < colNameEscel.length; i++){
			celda = fila.createCell(i);
			celda.setCellStyle(styleHead);
			celda.setCellValue(new HSSFRichTextString(colNameEscel[i].replaceAll("_"," ")));
            // FIXME: Utiliza librerias X11 no disponibles en COPIHUE
			// hoja.autoSizeColumn((short) i);
		}
		
		contFilas++;
		Iterator it =  listInformeVenta.iterator();
		while (it.hasNext()) {
			Map informeVenta = (Map) it.next(); 
			
			fila = hoja.createRow(contFilas);
			for(int i=0; i < colNameEscel.length; i++){
				celda = fila.createCell(i);
				celda.setCellStyle(stylebody);				
				celda.setCellValue(new HSSFRichTextString(String.valueOf(informeVenta.get(colNameEscel[i]))));
			}
			contFilas++;
		}
		//____________________________________________________________________________________________________________________________
				
		logger.info("Se creara el archivo excel " + rutaInforme);
		logger.info("Cantidad registros: " + listInformeVenta.size());
	
		FileOutputStream setFile = new FileOutputStream(rutaInforme);
		libro.write(setFile);
		setFile.close();			
		logger.info("End Construccion de xls");
		
		return new File(rutaInforme);
	}
	
	public static void enviaInforme(File file, List destinatariosReporte) throws Exception {
		
		logger.info("Init Enviar correo");		
		
		String host 	= Util.getPropertiesString("mail.smtp.host");
		String from 	= Util.getPropertiesString("mail.from");	
		
		String to 		= ""; //Util.getPropertiesString("mail.to");
		Iterator it = destinatariosReporte.iterator();
		while(it.hasNext()){
			String email = (String) it.next();
			to += email + ",";			
		}
		
		String cc 		= null;//Util.getPropertiesString("mail.cc");		
		
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
			
			JFactory.getSendMail(host, from).enviar(to, cc, subject, bodyHtml, archivos);
			
		}else{
			logger.info("[IMPORTANTE] La funcion que envia el reporte via email se encuentra DESACTIVADA (Para activar el envio de email cambie el parametro 'mail.send=1' en la configuracion.)");
		}
		
		logger.info("End Enviar correo");
	}

}
