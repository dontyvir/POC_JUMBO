/*
 * Created on 05-feb-2009
 *
 */
package cl.bbr.boc.view;

import java.io.OutputStream;
import java.util.ArrayList;
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
import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.BODetalleSemiautomaticoDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * 
 * @author jolazogu
 *
 */
public class ViewStockOnlineDetalleSemiautomaticoForm extends Command {
	   
	private final static long serialVersionUID = 1;
	    
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	   
		if( "true".equals( req.getParameter( "excel" ) ) ) {
		   
		   this.exportarExcel( req, res, usr );
		   return;
	   
		}
	  
	   res.setCharacterEncoding("UTF-8");
      View salida = new View(res);

      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;

      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      
      int localCheck = -1;
	  if(req.getParameter("local_check")==null){
			localCheck=5;
	  }else{
			localCheck = Integer.parseInt(req.getParameter("local_check"));
	  }
	  
	  int estado = 0;
	  if(req.getParameter("estado") != null) {
	
		  estado = Integer.parseInt(req.getParameter("estado"));
	
	  }
	  
	  String cambioEstado = "ALL";
	  
	  if(req.getParameter("cambioEstado") != null) {
	
		  cambioEstado = req.getParameter("cambioEstado");
	
	  }
		
	  top.setVariable("{sel"+estado+"}","selected");
	  top.setVariable("{sel"+cambioEstado+"}","selected");
	  
	  BizDelegate bizDelegate 	= new BizDelegate();
      LocalDTO local 			= bizDelegate.getLocalById(localCheck);
	 
      List listaDetalleSemiautomatico = new ArrayList();
      
      top.setVariable("{local_check}",String.valueOf(local.getId_local()));
       
	      BizDelegate biz = new BizDelegate();
	      listaDetalleSemiautomatico = biz.getDetalleSemiautomatico(local.getId_local());
      
   	 
	     List detalleSemiautomatico = this.getListaDetalleSemiautomatico(listaDetalleSemiautomatico, estado, cambioEstado);
      
      top.setVariable("{local}",local.getNom_local());
      top.setDynamicValueSets("listaDetalleSemiautomatico", detalleSemiautomatico);
      top.setVariable("{TotalListaDetalleSemiautomatico}", String.valueOf(listaDetalleSemiautomatico.size()));
      top.setVariable("{TotalFiltroListaDetalleSemiautomatico}", String.valueOf(detalleSemiautomatico.size()));
      
      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }
   
   
   private List getListaDetalleSemiautomatico ( List listaDetalleSemiautomatico, int estado, String cambioEstado ) {
	   
	   String filtroALL = "ALL";
		      
	   ArrayList listaSemiautomatico =new  ArrayList();
	     
	   	   	  for (int i = 0; i < listaDetalleSemiautomatico.size(); i++) {
				 IValueSet fila=new ValueSet();
				 BODetalleSemiautomaticoDTO iBODetalleSemiautomaticoDTO=(BODetalleSemiautomaticoDTO) listaDetalleSemiautomatico.get(i);
				 
				 if(filtroALL.equals(cambioEstado) || iBODetalleSemiautomaticoDTO.getCambioEstado().equalsIgnoreCase(cambioEstado) ) {
				  
				 	 if(iBODetalleSemiautomaticoDTO.getEstadoFinal().substring(0, 1).equals("<") && iBODetalleSemiautomaticoDTO.getEstadoFinal().equalsIgnoreCase("<b>SIN STOCK</b>") && estado == 1 ) {
				 
						 fila.setVariable("{articulo}",iBODetalleSemiautomaticoDTO.getSku());
						 fila.setVariable("{estadoInicial}",iBODetalleSemiautomaticoDTO.getEstadoInicial());
						 fila.setVariable("{estadoFinal}",iBODetalleSemiautomaticoDTO.getEstadoFinal());
						 fila.setVariable("{cambioEstado}",iBODetalleSemiautomaticoDTO.getCambioEstado());
						 fila.setVariable("{seccion}",iBODetalleSemiautomaticoDTO.getSeccion());
						 fila.setVariable("{rubro}",iBODetalleSemiautomaticoDTO.getRubro());
						 fila.setVariable("{subrubro}",iBODetalleSemiautomaticoDTO.getSubrubro());
						 
						 listaSemiautomatico.add(fila);
					
				 	 } else if(iBODetalleSemiautomaticoDTO.getEstadoFinal().substring(0, 1).equals("<") && iBODetalleSemiautomaticoDTO.getEstadoFinal().equalsIgnoreCase("<b>CON STOCK</b>") && estado == 2 ) {
					 
					 
				 		 fila.setVariable("{articulo}",iBODetalleSemiautomaticoDTO.getSku());
						 fila.setVariable("{estadoInicial}",iBODetalleSemiautomaticoDTO.getEstadoInicial());
						 fila.setVariable("{estadoFinal}",iBODetalleSemiautomaticoDTO.getEstadoFinal());
						 fila.setVariable("{cambioEstado}",iBODetalleSemiautomaticoDTO.getCambioEstado());
						 fila.setVariable("{seccion}",iBODetalleSemiautomaticoDTO.getSeccion());
						 fila.setVariable("{rubro}",iBODetalleSemiautomaticoDTO.getRubro());
						 fila.setVariable("{subrubro}",iBODetalleSemiautomaticoDTO.getSubrubro());
						 
						 listaSemiautomatico.add(fila);
					 
					 } else if(iBODetalleSemiautomaticoDTO.getEstadoFinal().substring(0, 1).equals("<") && estado == 3 ) {
						 
						 fila.setVariable("{articulo}",iBODetalleSemiautomaticoDTO.getSku());
						 fila.setVariable("{estadoInicial}",iBODetalleSemiautomaticoDTO.getEstadoInicial());
						 fila.setVariable("{estadoFinal}",iBODetalleSemiautomaticoDTO.getEstadoFinal());
						 fila.setVariable("{cambioEstado}",iBODetalleSemiautomaticoDTO.getCambioEstado());
						 fila.setVariable("{seccion}",iBODetalleSemiautomaticoDTO.getSeccion());
						 fila.setVariable("{rubro}",iBODetalleSemiautomaticoDTO.getRubro());
						 fila.setVariable("{subrubro}",iBODetalleSemiautomaticoDTO.getSubrubro());
						 
						 listaSemiautomatico.add(fila);
						 
					 } else if(estado == 0 ) {
							 
						 fila.setVariable("{articulo}",iBODetalleSemiautomaticoDTO.getSku());
						 fila.setVariable("{estadoInicial}",iBODetalleSemiautomaticoDTO.getEstadoInicial());
						 fila.setVariable("{estadoFinal}",iBODetalleSemiautomaticoDTO.getEstadoFinal());
						 fila.setVariable("{cambioEstado}",iBODetalleSemiautomaticoDTO.getCambioEstado());
						 fila.setVariable("{seccion}",iBODetalleSemiautomaticoDTO.getSeccion());
						 fila.setVariable("{rubro}",iBODetalleSemiautomaticoDTO.getRubro());
						 fila.setVariable("{subrubro}",iBODetalleSemiautomaticoDTO.getSubrubro());
						 
						 listaSemiautomatico.add(fila);
						 
					 }
	   	   	  
				 }
	   	   	  
	   	   	  }
	   	   
	   	   	  return listaSemiautomatico;
   
   }

   
   
   /**
    * 
    * @param req
    * @param res
    * @param usr
    * @throws Exception
    */
   private void exportarExcel( HttpServletRequest req, HttpServletResponse res, UserDTO usr ) throws Exception {
	   
	   int localCheck = 5;
		  
	   if( req.getParameter( "local_check" ) != null ) {
				
		   localCheck = Integer.parseInt(req.getParameter("local_check"));
		  
	   }
			
	   BizDelegate biz 	= new BizDelegate();
	   
	   LocalDTO local = biz.getLocalById(localCheck);

	   List listaDetalleSemiautomatico = new ArrayList();
	      
	   listaDetalleSemiautomatico = biz.getDetalleSemiautomatico( local.getId_local() );
	      
	   HSSFWorkbook wb = crerExcelResumen( listaDetalleSemiautomatico, local.getNom_local() );

	   res.setContentType( "application/vnd.ms-excel" );
	   res.addHeader( "Content-Disposition", "inline; filename=DetalleSemiAutomaticoLocal-" +local.getCod_local()+ ".xls" );
	   
	   OutputStream output = res.getOutputStream();
	   wb.write( output );
	   output.close();

	   
   }
   
   /**
    * 
    * @param listaDetalleSemiautomático
    * @param local
    * @return
    */
   private HSSFWorkbook crerExcelResumen( List listaDetalleSemiautomático, String local ) {
		
	   HSSFWorkbook wb = new HSSFWorkbook();
		
	   try{
		   
           HSSFSheet sheet = wb.createSheet( "Semi Automático" );

           HSSFFont font = wb.createFont();
           
           font.setFontHeightInPoints((short)10);
		   font.setFontName("Arial");
		   font.setItalic(false);
			
		   HSSFFont fontEstadoFinal = wb.createFont();
		   fontEstadoFinal.setFontHeightInPoints((short)10);
		   fontEstadoFinal.setFontName("Arial");
		   fontEstadoFinal.setItalic(false);
		   fontEstadoFinal.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
		   HSSFFont font2 = wb.createFont();
		   font2.setFontHeightInPoints((short)10);
		   font2.setFontName("Arial");
		   font2.setItalic(false);
		   font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		   font2.setColor(HSSFColor.WHITE.index);
			
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
			
		   HSSFCellStyle styleEStadoFinal = wb.createCellStyle();
		   styleEStadoFinal.setFont(fontEstadoFinal);
		   styleEStadoFinal.setBorderTop(HSSFCellStyle.BORDER_THIN);
		   styleEStadoFinal.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		   styleEStadoFinal.setBorderRight(HSSFCellStyle.BORDER_THIN);
		   styleEStadoFinal.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		   styleEStadoFinal.setFillForegroundColor(HSSFColor.WHITE.index);
		   styleEStadoFinal.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
		   HSSFRow row0 = sheet.createRow((short) 0);
			
		   HSSFCell cell_0_1 = row0.createCell(0);
		   cell_0_1.setCellValue(new HSSFRichTextString("LOCAL"));
		   cell_0_1.setCellStyle(style);
			
		   HSSFCell cell_0_2 = row0.createCell(1);
		   cell_0_2.setCellValue(new HSSFRichTextString("SKU"));
		   cell_0_2.setCellStyle(style);
			
		   HSSFCell cell_0_3 = row0.createCell(2);
		   cell_0_3.setCellValue(new HSSFRichTextString("ESTADO INICIAL"));
		   cell_0_3.setCellStyle(style);
			
		   HSSFCell cell_0_4 = row0.createCell(3);
		   cell_0_4.setCellValue(new HSSFRichTextString("ESTADO FINAL"));
		   cell_0_4.setCellStyle(style);
			
		   HSSFCell cell_0_5 = row0.createCell(4);
		   cell_0_5.setCellValue(new HSSFRichTextString("CAMBIO"));
		   cell_0_5.setCellStyle(style);
			
		   HSSFCell cell_0_6 = row0.createCell(5);
		   cell_0_6.setCellValue(new HSSFRichTextString("SECCION"));
		   cell_0_6.setCellStyle(style);

		   HSSFCell cell_0_7 = row0.createCell(6);
		   cell_0_7.setCellValue(new HSSFRichTextString("RUBRO"));
		   cell_0_7.setCellStyle(style);

		   HSSFCell cell_0_8 = row0.createCell(7);
		   cell_0_8.setCellValue(new HSSFRichTextString("SUBRUBRO"));
		   cell_0_8.setCellStyle(style);
	
			/*****************************************************/
			int j = 0;
			
			Iterator it = listaDetalleSemiautomático.iterator();
			
			while (it.hasNext()) {
			
				BODetalleSemiautomaticoDTO bODetalleSemiautomaticoDTO = (BODetalleSemiautomaticoDTO)it.next();
			    
				j++;
			    
			    HSSFRow row = sheet.createRow((int) j);

				HSSFCell cell_1 = row.createCell(0);
				cell_1.setCellValue(new HSSFRichTextString(local));
				cell_1.setCellStyle(style2);

				HSSFCell cell_2 = row.createCell(1);
				cell_2.setCellValue(new HSSFRichTextString(bODetalleSemiautomaticoDTO.getSku()));
				cell_2.setCellStyle(style3);

				HSSFCell cell_3 = row.createCell(2);
				cell_3.setCellValue(new HSSFRichTextString(bODetalleSemiautomaticoDTO.getEstadoInicial()));
				cell_3.setCellStyle(style3);
				
				
				HSSFCell cell_4 = row.createCell(3);
				
				if( bODetalleSemiautomaticoDTO.getEstadoFinal().substring(0, 1).equals("<") ){
				
					cell_4.setCellValue(new HSSFRichTextString(bODetalleSemiautomaticoDTO.getEstadoFinal().replaceAll("<b>", "").replaceAll("</b>", "")));
					cell_4.setCellStyle(styleEStadoFinal);
					
				}else {
					cell_4.setCellValue(new HSSFRichTextString(bODetalleSemiautomaticoDTO.getEstadoFinal()));
					cell_4.setCellStyle(style3);
					
				}
				
				HSSFCell cell_5 = row.createCell(4);
				cell_5.setCellValue(new HSSFRichTextString(bODetalleSemiautomaticoDTO.getCambioEstado()));
				cell_5.setCellStyle(style3);
				
				HSSFCell cell_6 = row.createCell(5);
				cell_6.setCellValue(new HSSFRichTextString(bODetalleSemiautomaticoDTO.getSeccion()));
				cell_6.setCellStyle(style3);
				
				HSSFCell cell_7 = row.createCell(6);
				cell_7.setCellValue(new HSSFRichTextString(bODetalleSemiautomaticoDTO.getRubro()));
				cell_7.setCellStyle(style3);
				
				HSSFCell cell_8 = row.createCell(7);
				cell_8.setCellValue(new HSSFRichTextString(bODetalleSemiautomaticoDTO.getSubrubro()));
				cell_8.setCellStyle(style3);
				
			}
			
			sheet.autoSizeColumn((short)0);
			sheet.autoSizeColumn((short)1);
			sheet.autoSizeColumn((short)2);
			sheet.autoSizeColumn((short)3);
			sheet.autoSizeColumn((short)4);
			sheet.autoSizeColumn((short)5);
			sheet.autoSizeColumn((short)6);
			sheet.autoSizeColumn((short)7);
		
		}catch(Exception e){

			e.printStackTrace();
		
		}
	   
		return wb;
   
   }
	
}
