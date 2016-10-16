package cl.bbr.boc.command;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
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
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class CategoriasMobileExcel  extends Command{	

	private static final long serialVersionUID = 3472815874347090269L;

	protected void Execute(HttpServletRequest request, HttpServletResponse response,UserDTO usr) throws Exception {
		
		try {			

			response.addCookie(new Cookie("fileDownload","true"));			
			response.setContentType("application/octet-stream");
			String action = request.getParameter("action"); 
			BizDelegate bizDelegate = new BizDelegate();
			
			Date date = new Date();
			DateFormat hourdateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm");
		
			if("ExcelInGRB".equals(action)){
				
				response.addHeader( "Content-Disposition", "attachment; filename=CategoriasEnMovil_"+hourdateFormat.format(date)+"_export.xls");				
				OutputStream fileOut = response.getOutputStream(); 
				HSSFWorkbook workbook = workBookCategorias(bizDelegate, "CatInGRB");
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
				response.flushBuffer();
			}
			
			else if("ExcelNoGRB".equals(action)){

				response.addHeader( "Content-Disposition", "attachment; filename=CategoriasNoMovil_"+hourdateFormat.format(date)+"_export.xls");				
				OutputStream fileOut = response.getOutputStream(); 
				HSSFWorkbook workbook = workBookCategorias(bizDelegate, "CatNoGRB");			    
				workbook.write(fileOut);
				fileOut.flush();
				fileOut.close();
				response.flushBuffer();
			}
		} catch (Exception ex) {
			logger.error(ex);
		} 
	}
	
	//Crea excel para ambas categorias.
	private HSSFWorkbook workBookCategorias(BizDelegate bizDelegate, String tipo) throws Exception{
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet worksheet = null;
		
		if("CatInGRB".equals(tipo)){
			worksheet = workbook.createSheet("Categorias en movil");
		}else if("CatNoGRB".equals(tipo)){
			worksheet = workbook.createSheet("Categorias no movil");
		}
 
		worksheet.setZoom(1,2);
		worksheet.setGridsPrinted(false);
		worksheet.setPrintGridlines(false);
		worksheet.setDisplayGridlines(false); 
		
	    int filaSeccion = 0;
	    
	    HSSFRow row = worksheet.createRow((short) filaSeccion);			    
	    worksheet.setColumnWidth(0, 10000);
	    worksheet.setColumnWidth(1, 15000);
	    worksheet.setColumnWidth(2, 15000);
	    worksheet.setColumnWidth(3, 15000);
	    
	    HSSFCell seccion = row.createCell((short) 0);			    
	    HSSFCell rubro = row.createCell((short) 1);
	    HSSFCell subRubro = row.createCell((short) 2);
	    HSSFCell grupo = row.createCell((short) 3);
	    
	    
	    HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 16);
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    
	    HSSFCellStyle cssHead = workbook.createCellStyle();
	    cssHead.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	    cssHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    cssHead.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	    cssHead.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	    cssHead.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	    cssHead.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	    cssHead.setFont(hSSFFont);
			    
	    seccion = row.createCell((short) 0);
		seccion.setCellValue("SECCION");		
		seccion.setCellStyle(cssHead);
		
		rubro = row.createCell((short) 1);
		rubro.setCellValue("RUBRO");
		rubro.setCellStyle(cssHead);
		
		subRubro = row.createCell((short) 2);
		subRubro.setCellValue("SUBRUBRO");
		subRubro.setCellStyle(cssHead);
		
		grupo = row.createCell((short) 3);
		grupo.setCellValue("GRUPO");
		grupo.setCellStyle(cssHead);
	 
	    
	    String rubroText ="";
	    String subRubroText ="";	
	    String grupoText ="";
	    filaSeccion++;
	    LinkedHashMap oMapSeccion = new LinkedHashMap();
	    
	    if("CatInGRB".equals(tipo)){
	    	oMapSeccion = bizDelegate.getCategoriasInGRB(1,2,"0");
	    }else if("CatNoGRB".equals(tipo)){
	    	oMapSeccion = bizDelegate.getCategoriasNoGRB("0");
	    }
	    
		Set setSeccion = oMapSeccion.entrySet();
	    Iterator iSeccion = setSeccion.iterator();			    
	    while(iSeccion.hasNext()) {
	    	Map.Entry meSeccion = (Map.Entry)iSeccion.next();
	      	CategoriaSapDTO oCatdto = (CategoriaSapDTO) meSeccion.getValue();
	      	
	      	//crea la fila por seccion
			row = worksheet.createRow((short) filaSeccion);
													
			//rubros
			rubroText ="";	
			subRubroText ="";	
			grupoText ="";
			
	      	LinkedHashMap oMapRubro = new LinkedHashMap();
	      	
	      	 if("CatInGRB".equals(tipo)){
	      		oMapRubro = bizDelegate.getCategoriasInGRB(1,4,oCatdto.getId_cat());
	 	    }else if("CatNoGRB".equals(tipo)){
	 	    	oMapRubro = bizDelegate.getCategoriasNoGRB(oCatdto.getId_cat());
	 	    }
	      	 
	      	Set setRubro = oMapRubro.entrySet();
			Iterator iRubro = setRubro.iterator();
			while(iRubro.hasNext()) {
			  Map.Entry meRubro = (Map.Entry)iRubro.next();
			  CategoriaSapDTO oCatdtoRubro = (CategoriaSapDTO) meRubro.getValue();
			  rubroText += oCatdtoRubro.getId_cat()+" | "+oCatdtoRubro.getDescrip()+". \n";
			  
			  //subrubros
			  if(!"".equals(subRubroText))
				  subRubroText +="\n";				
			  
			  LinkedHashMap oMapSubRubro =  new LinkedHashMap();
				if("CatInGRB".equals(tipo)){
				   oMapSubRubro = bizDelegate.getCategoriasInGRB(1,6,oCatdtoRubro.getId_cat());
				}else if("CatNoGRB".equals(tipo)){
				   oMapSubRubro = bizDelegate.getCategoriasNoGRB(oCatdtoRubro.getId_cat());
				}
			 			  
			  Set setSubRubro = oMapSubRubro.entrySet();
			  Iterator iSubRubro = setSubRubro.iterator();	
			  while(iSubRubro.hasNext()) {
				  Map.Entry meSubRubro = (Map.Entry)iSubRubro.next();
				  CategoriaSapDTO oCatdtoSubRubro = (CategoriaSapDTO) meSubRubro.getValue();
				  subRubroText += oCatdtoSubRubro.getId_cat()+" | "+oCatdtoSubRubro.getDescrip()+". \n";

				  //grupos
				  if(!"".equals(grupoText))
					  grupoText +="\n";
				  
				  LinkedHashMap oMapGrupo = new LinkedHashMap();
				  if("CatInGRB".equals(tipo)){
					  oMapGrupo = bizDelegate.getCategoriasInGRB(1,9,oCatdtoSubRubro.getId_cat());
					}else if("CatNoGRB".equals(tipo)){
						oMapGrupo = bizDelegate.getCategoriasNoGRB(oCatdtoSubRubro.getId_cat());
					}
				  				 
				  Set setGrupo = oMapGrupo.entrySet();
				  Iterator iGrupo = setGrupo.iterator();						
				  while(iGrupo.hasNext()) {
					  Map.Entry meGrupo = (Map.Entry)iGrupo.next();
					  CategoriaSapDTO oCatdtoGrupo = (CategoriaSapDTO) meGrupo.getValue();
					  grupoText += oCatdtoGrupo.getId_cat()+" | "+oCatdtoGrupo.getDescrip()+". \n";
				  }						  
			  }					  
			} 					
			
			short color = HSSFColor.LEMON_CHIFFON.index;
			if (filaSeccion%2!=0){
				color = HSSFColor.LIGHT_YELLOW.index;
			}

			seccion = row.createCell((short) 0);
			seccion.setCellValue(oCatdto.getId_cat()+" | "+oCatdto.getDescrip().toUpperCase()+".\n");
			HSSFCellStyle csSeccion = workbook.createCellStyle();
			csSeccion.setFillForegroundColor(color);
			csSeccion.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);				
			csSeccion.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			csSeccion.setWrapText(true);
			csSeccion.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			csSeccion.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			csSeccion.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			csSeccion.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			seccion.setCellStyle(csSeccion);
			
			rubro = row.createCell((short) 1);
			rubro.setCellValue(new HSSFRichTextString(rubroText.toUpperCase()));
			HSSFCellStyle csRubro = workbook.createCellStyle();
			csRubro.setFillForegroundColor(color);
			csRubro.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			csRubro.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		    csRubro.setWrapText(true);
		    csRubro.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		    csRubro.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		    csRubro.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		    csRubro.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			rubro.setCellStyle(csRubro);
			
			subRubro = row.createCell((short) 2);
			subRubro.setCellValue(new HSSFRichTextString(subRubroText.toUpperCase()));
			HSSFCellStyle csSubRubro = workbook.createCellStyle();
			csSubRubro.setFillForegroundColor(color);
			csSubRubro.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			csSubRubro.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			csSubRubro.setWrapText(true);
			csSubRubro.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			csSubRubro.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			csSubRubro.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			csSubRubro.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			subRubro.setCellStyle(csSubRubro);
			
			grupo = row.createCell((short) 3);
			grupo.setCellValue(new HSSFRichTextString(grupoText.toUpperCase()));
			HSSFCellStyle csGrupo = workbook.createCellStyle();
			csGrupo.setFillForegroundColor(color);
			csGrupo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			csGrupo.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			csGrupo.setWrapText(true);
			csGrupo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			csGrupo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			csGrupo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			csGrupo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			grupo.setCellStyle(csGrupo);					
								
			filaSeccion++;
	    }
	    
	    return workbook;
	    
	}
	

}
