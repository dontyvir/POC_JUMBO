package cl.cencosud.jumbo.main;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import cl.cencosud.jumbo.dao.CambioPrecioDao;
import cl.cencosud.jumbo.util.JFactory;
import cl.cencosud.jumbo.util.Util;

public class CambioPrecioPM {
	
	protected static Logger logger = Logger.getLogger(CambioPrecioPM.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			logger.info("INIT REPORTE Mail Cambio Precio PM");
			
			if(!CambioPrecioDao.isActivaCorreccionAutomatica()){
				throw new Exception("Campo ACTIVA_CORRECCION_EXCESO tabla BO_PARAMETROS es distinto a 1 o no existe."); 
			}
			 
			String diasAtras  = (args.length > 0)?args[0].toString():null;
			String dias = (diasAtras != null && Util.isNumeric(diasAtras))? String.valueOf(diasAtras) : "0";			
												
			//Calculamos la fecha del reporte
			String fechaSolicitada = CambioPrecioDao.getfechaReporte(dias);
			
			//Obtenemos la lista de Op con cambio de precio del dia seleccionado
			List opsConCambioPrecio = CambioPrecioDao.getOpsConCambioPrecioPorDia(fechaSolicitada);
			
			//Obtenemos los sectores
			List listSectoresProductos = CambioPrecioDao.getSectoresProductos();
			
			//Creamos el informe para los PM
			File xlsPM = creaInformeXSL(opsConCambioPrecio, listSectoresProductos, fechaSolicitada);
			 
			//Enviamos informe a los PM
			enviaInforme(xlsPM);
			
			logger.info("END REPORTE Mail Cambio Precio PM");
		} catch (Exception e) {
			logger.error("Error: ", e);
		}finally {
			CambioPrecioDao.close();
        }
	}
	
	public static File creaInformeXSL(List opsConCambioPrecio, List listSectoresProductos, String fechaSolicitada) throws Exception {	

		logger.info("Init Construccion de xls");			
		String rutaInforme = Util.getPropertiesString("path.archivo.excel") + fechaSolicitada +"_Cambio_Precio_PM.xls";
		HSSFWorkbook libro = JFactory.getHSSFWorkbook();
		
		HSSFCell celda = null;
		int contFilas = 0;
		
		HSSFCellStyle styleHead = libro.createCellStyle();
		styleHead.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
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
        
    	HSSFCellStyle styleDetallePedido = libro.createCellStyle();
    	styleDetallePedido.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    	styleDetallePedido.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);		
    	styleDetallePedido.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
    	styleDetallePedido.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
    	styleDetallePedido.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
    	styleDetallePedido.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
    	
    	HSSFCellStyle styleDetallePicking = libro.createCellStyle();
    	styleDetallePicking.setFillForegroundColor(HSSFColor.YELLOW.index);
    	styleDetallePicking.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);		
    	styleDetallePicking.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
    	styleDetallePicking.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
    	styleDetallePicking.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
    	styleDetallePicking.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
    	
    	
		
    	//Fin estilos
		//____________________________________________________________________________________________________________________________

        Iterator it = listSectoresProductos.iterator();
        while(it.hasNext()){
        	HashMap sectores = (HashMap) it.next();
        	long idsector = Long.parseLong(String.valueOf(sectores.get("ID_SECTOR")));
        	HSSFSheet hoja	= libro.createSheet(String.valueOf(sectores.get("NOMBRE")));
    		hoja.setGridsPrinted(false);
    		hoja.setPrintGridlines(false);
    		hoja.setDisplayGridlines(false); 
    		    		
    		Iterator itOps =  opsConCambioPrecio.iterator();
    		while(itOps.hasNext()){
    			Object[] tempOps = (Object[])itOps.next();
    			long op = Long.parseLong(String.valueOf(tempOps[0]));
    			HSSFRow fila	= hoja.createRow(contFilas);
    			
    			List detallesPorSector = CambioPrecioDao.getDetallePedidoConCambioPrecioPorSector(op, idsector);
    			if(detallesPorSector.size() > 0){
    				//obtenemos los dato de la op
    				Object[] dataOP = CambioPrecioDao.getInfoOP(op);        				
    				
    				String colNameOP[] = {"ID_PEDIDO", "ID_ESTADO", "ID_LOCAL", "FCREACION", "MONTO_PEDIDO", "MONTO_RESERVADO", "COSTO_DESPACHO", "RUT_CLIENTE", "DV_CLIENTE"};
    				
    				for(int i=0; i < colNameOP.length; i++){
    					celda = fila.createCell(i);
    					celda.setCellStyle(styleHead);
    					celda.setCellValue(new HSSFRichTextString(colNameOP[i]));
    				}
    				contFilas++;
    				
    				fila = hoja.createRow(contFilas);
    				for(int i=0; i < dataOP.length; i++){
	    				celda = fila.createCell(i);
	    				celda.setCellStyle(stylebody);
	    				celda.setCellValue(new HSSFRichTextString(String.valueOf(dataOP[i])));
    				}    			
    				
    				
	    			Iterator itDetallesPorSector =  detallesPorSector.iterator();
	    			while(itDetallesPorSector.hasNext()){
	    				Object[] tempDetallesPorSector = (Object[])itDetallesPorSector.next();
	        			long idDetalle = Long.parseLong(String.valueOf(tempDetallesPorSector[0]));
	        			Object[] dataDetalle = CambioPrecioDao.getDetallePedidoPorIdDetalle(idDetalle);  
	        			List dataPicking = CambioPrecioDao.getDetallePickingPorIdDetalle(idDetalle); 
	        			
	        			contFilas++;
	        			fila = hoja.createRow(contFilas);
	        			String colNamedetalleOP[] = {"ID_DETALLE", "SAP", "DESCRIPCION", "CANT_SOLIC", "PRECIO"};
	    				
	    				for(int i=0; i < colNamedetalleOP.length; i++){
	    					celda = fila.createCell(i);
	    					celda.setCellStyle(styleDetallePedido);
	    					celda.setCellValue(new HSSFRichTextString(colNamedetalleOP[i]));
	    				}
	        			
	    				contFilas++;	    				
	    				fila = hoja.createRow(contFilas);
	    				
	    				for(int i=0; i < dataDetalle.length; i++){
		    				celda = fila.createCell(i);
		    				celda.setCellStyle(stylebody);
		    				celda.setCellValue(new HSSFRichTextString(String.valueOf(dataDetalle[i])));
	    				}
	    				
	    				contFilas++;	    				
	    				fila = hoja.createRow(contFilas);
	    				
	    				String colNameDetPicking[] = {"CBARRA", "DESCRIPCION", "CANT_PICK", "PRECIO", "PRECIO_PICK"};
	    				for(int i=0; i < colNameDetPicking.length; i++){
	    					celda = fila.createCell(i+1);
	    					celda.setCellStyle(styleDetallePicking);
	    					celda.setCellValue(new HSSFRichTextString(String.valueOf(colNameDetPicking[i])));
	    				}
	    				
	    				Iterator itDetallePiking = dataPicking.iterator();
	    				while(itDetallePiking.hasNext()){
	    					HashMap tempDetallePicking = (HashMap)itDetallePiking.next();
	    					contFilas++;	    				
		    				fila = hoja.createRow(contFilas);		    				
		    				
		    				for(int i=0; i < colNameDetPicking.length; i++){
		    					celda = fila.createCell(i+1);
		    					celda.setCellStyle(stylebody);
		    					celda.setCellValue(new HSSFRichTextString(String.valueOf(tempDetallePicking.get(colNameDetPicking[i]))));
		    				}
	    				}
	    			}	
	    			contFilas++;	    				
					fila = hoja.createRow(contFilas);
					celda = fila.createCell(0);
    			}
    		} 
    		contFilas = 0;
        }

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
		String subject	= Util.getPropertiesString("mail.subject");	
		String cc		= Util.getPropertiesString("mail.cc");
		String bodyHtml = Util.getPropertiesString("mail.bodyHtml");
		String sendmail = Util.getPropertiesString("mail.send");
		String mailCargos = Util.getPropertiesString("mail.cargos");
		String development = Util.getPropertiesString("development"); 
						
		List archivos = new ArrayList(1);
		archivos.add(file);

		if("1".equals(sendmail)){
			
			logger.info("Conectando SMTP host["+host+"]");
			
			if (logger.isDebugEnabled()){
				logger.debug("host["+host+"]");
				logger.debug("from["+from+":");
				logger.debug("subject["+subject+"]");
				logger.debug("bodyHtml["+bodyHtml+"]");
			}
			
			List mailPMs = CambioPrecioDao.getMailPM(mailCargos);
			Iterator it = mailPMs.iterator();
			while(it.hasNext()){
				HashMap mailPM = (HashMap)it.next();
				String mail = (mailPM.get("MAILPM")!= null)?String.valueOf(mailPM.get("MAILPM")):null;
				if("desa".equals(development)){
					JFactory.getSendMail(host, from, bodyHtml, archivos).enviar(cc, "", subject);
				}else{
					if(mail != null){
						JFactory.getSendMail(host, from, bodyHtml, archivos).enviar(mail, cc, subject);
					}else{
						logger.error("getSendMail::No tiene destinario valido");
					}
				}				
			}			
			
		}else{
			logger.info("[IMPORTANTE] La funcion que envia el reporte via email se encuentra DESACTIVADA (Para activar el envio de email cambie el parametro 'mail.send=1' en la configuracion.)");
		}
		
		logger.info("End Enviar correo");
	}

}
