package cl.cencosud.excel.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import cl.cencosud.beans.Precio;
import cl.cencosud.constantes.Constantes;
import cl.cencosud.mail.Mail;
import cl.cencosud.util.FileUtil;

public class XlsFileImpl {
	
	private static Logger logger = Logger.getLogger(XlsFileImpl.class);		
	private final static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss a");
		
	public void createFilePreciosSinActualizar(String path, List listaPreciosNoValidas, List listaPreciosNoActualizados ) {						
		File newFile  = null;
		File fileTemplate = null;
		FileOutputStream fos = null;
		try{
			fileTemplate = new File(Constantes.PATH_TEMPLATE + Constantes.TEMPLATE_PRECIOS_PARCIALES);
			newFile = new File(Constantes.PATH_TEMPLATE +Constantes.NAME_FOLDER_GENERACION +File.separator+ Constantes.NAME_FILE_ALERTA+"_"+sdf.format(new Date())+".xls");				
			
			List listaFileCorruptos = FileUtil.validaArchivosGZ(FileUtil.listaArchivosExtGZ(path), path);	
			HSSFWorkbook objWB = planillaExcelN(fileTemplate, listaFileCorruptos, listaPreciosNoValidas, listaPreciosNoActualizados);
			
			fos = new FileOutputStream(newFile);
			objWB.write(fos);
			
			Mail.sendMail(newFile);
	        logger.debug("excel ha sido enviado!");
	        
		}catch(MessagingException me){
			logger.error("Error " + me);
		}catch(Exception ex){
			logger.info("Error al crear excel y enviar mail de precios:" + ex);
			if (fos != null) {
				try {
					fos.close();
				}catch (IOException iox) {
					logger.error("ERROR, Archivo["+newFile+"], descripcion:" + iox);
				}
			}
		}
	}	
	
	private HSSFWorkbook planillaExcelN(File plantilla, List listaFileCorruptos, List listaPreciosNoValidas, List listaPreciosNoActualizados)throws Exception {
	     HSSFWorkbook objWB = new HSSFWorkbook( new FileInputStream(plantilla) );
	     createDataFileCorruptos(listaFileCorruptos, objWB);
	     createDataUnidadesNoValidas(listaPreciosNoValidas, objWB);
	     createDataPreciosSinActualizar(listaPreciosNoActualizados, objWB); 
	     return objWB;
	 }

	 private void createDataFileCorruptos(List listaFileCorruptos, HSSFWorkbook objWB)throws Exception {
		HSSFCell cell = null; 
		HSSFSheet hoja1 = objWB.getSheetAt(0); 
		int conta=7;
		for (Iterator iter = listaFileCorruptos.iterator(); iter.hasNext();) {
			String nombreArchivo = (String) iter.next();
			HSSFRow rowhead = hoja1.createRow((short)conta);
			cell = rowhead.createCell(1);
			cell.setCellValue(new HSSFRichTextString(nombreArchivo));
			cell.setCellStyle(getBorderStyle(objWB));
			conta++;
		}
	 }
	 
	 private void createDataUnidadesNoValidas(List listaPrecios, HSSFWorkbook objWB)throws Exception {
		 HSSFSheet hoja1 = objWB.getSheetAt(1);
		 String interfazold = "";
		 int conta = 8;
		 Iterator iter = listaPrecios.iterator();
		 while(iter.hasNext()){
			 List list1 = (List)iter.next();
			 HSSFCell cell = null;
			 Iterator iter2 = list1.iterator();
			 while(iter2.hasNext()){					
				 Precio producto = (Precio)iter2.next();
					
				 if(producto.getCodigoLocal()!=null && String.valueOf(producto.getCodigoProducto())!=null &&
						 String.valueOf(producto.getPrecio())!=null && String.valueOf(producto.getCodigoBarra())!=null &&
						producto.getUnidadMedida()!=null ){

					 HSSFCellStyle style = getBorderStyle(objWB);

					 HSSFRow rowhead = hoja1.createRow((short)conta);
					 if(!interfazold.equals(producto.getNombreArchivo())){
						 cell = rowhead.createCell(1);
						 cell.setCellValue(new HSSFRichTextString(producto.getNombreArchivo()));
						 cell.setCellStyle(getBorderStyle(objWB));
						 interfazold = producto.getNombreArchivo();
					}else{
						cell = rowhead.createCell(1);
						cell.setCellValue(new HSSFRichTextString(""));
						cell.setCellStyle(getBorderStyle(objWB));
					}
					cell = rowhead.createCell(2);
					cell.setCellValue(new HSSFRichTextString(producto.getCodigoLocal()));
					cell.setCellStyle(style);
					
					cell = rowhead.createCell(3);
					cell.setCellValue(new HSSFRichTextString(producto.getCodigoProducto()));
					cell.setCellStyle(style);
					
					cell = rowhead.createCell(4);
					cell.setCellValue(new HSSFRichTextString(""+producto.getCodigoBarra()));
					cell.setCellStyle(style);
					
					cell = rowhead.createCell(5);
					cell.setCellValue(new HSSFRichTextString(""+producto.getPrecio()));
					cell.setCellStyle(style);
					
					cell = rowhead.createCell(6);
					cell.setCellValue(new HSSFRichTextString(""+producto.getUnidadMedida()));
					cell.setCellStyle(style);
					conta++;
				}
			}
		} 
	 }

	 private void createDataPreciosSinActualizar(List listaPrecios, HSSFWorkbook objWB)throws Exception {
		 HSSFSheet hoja1 = objWB.getSheetAt(2);
		 String interfazold = "";
		 int conta = 8;
		 Iterator iter = listaPrecios.iterator();
		 while(iter.hasNext()){
			 List list1 = (List)iter.next();
			 HSSFCell cell = null;
			 Iterator iter2 = list1.iterator();
			 while(iter2.hasNext()){					
				 Precio producto = (Precio)iter2.next();
					
				 if(producto.getCodigoLocal()!=null && String.valueOf(producto.getCodigoProducto())!=null &&
						 String.valueOf(producto.getPrecio())!=null && String.valueOf(producto.getCodigoBarra())!=null &&
						producto.getUnidadMedida()!=null ){

					 HSSFCellStyle style = getBorderStyle(objWB);

					 HSSFRow rowhead = hoja1.createRow((short)conta);
					 if(!interfazold.equals(producto.getNombreArchivo())){
						 cell = rowhead.createCell(1);
						 cell.setCellValue(new HSSFRichTextString(producto.getNombreArchivo()));
						 cell.setCellStyle(getBorderStyle(objWB));
						 interfazold = producto.getNombreArchivo();
					}else{
						cell = rowhead.createCell(1);
						cell.setCellValue(new HSSFRichTextString(""));
						cell.setCellStyle(getBorderStyle(objWB));
					}
					cell = rowhead.createCell(2);
					cell.setCellValue(new HSSFRichTextString(producto.getCodigoLocal()));
					cell.setCellStyle(style);
					
					cell = rowhead.createCell(3);
					cell.setCellValue(new HSSFRichTextString(producto.getCodigoProducto()));
					cell.setCellStyle(style);
					
					cell = rowhead.createCell(4);
					cell.setCellValue(new HSSFRichTextString(""+producto.getCodigoBarra()));
					cell.setCellStyle(style);
					
					cell = rowhead.createCell(5);
					cell.setCellValue(new HSSFRichTextString(""+producto.getPrecio()));
					cell.setCellStyle(style);
					
					cell = rowhead.createCell(6);
					cell.setCellValue(new HSSFRichTextString(""+producto.getPrecioActual()));
					cell.setCellStyle(style);
					
					cell = rowhead.createCell(7);
					cell.setCellValue(new HSSFRichTextString(""+producto.getUnidadMedida()));
					cell.setCellStyle(style);
					conta++;
				}
			}
		}
  	}

	 private static HSSFCellStyle getBorderStyle(HSSFWorkbook objWB) {
		 HSSFCellStyle style = objWB.createCellStyle();
		 style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		 style.setBorderLeft(HSSFCellStyle.BORDER_THIN);				      	    
		 return style;
	 }	 
}
