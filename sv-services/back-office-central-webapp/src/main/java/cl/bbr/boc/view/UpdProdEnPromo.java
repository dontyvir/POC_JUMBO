/*
 * 
 *
 */
package cl.bbr.boc.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.DestacadoDTO;
import cl.bbr.boc.dto.FOProductoBannerDTO;
import cl.bbr.boc.dto.FOProductoDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import com.oreilly.servlet.MultipartRequest;

/**
 * @author 
 *  
 */
public class UpdProdEnPromo extends Command {
	
   private final static long serialVersionUID = 1;
   private final int MAXIMO_NUMERO_DE_FILAS = 3000;
   private final String ROW_OK = "OK";
   private final String ROW_NOK = "NOK";
      
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	   String message = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"+
	    "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"></head>"+
	    "<body style=\"font-size: 10px;font-family: Arial, Helvetica, sans-serif;color: #336699;\">";

	   try {
		   String rutaUpload = req.getSession().getServletContext().getRealPath("upload_ruts_eventos");
		   MultipartRequest multi = new MultipartRequest(req, rutaUpload, 10000000);
	       
		   //Rescatamos la accion a realizar carga o revert
		   String tipOperacion = multi.getParameter("tipo_carga").toString();
		   
	       String locales = null;  //verificaNull(multi.getParameter("locales"));
	       String obs = verificaNull(multi.getParameter("obs"));
	
	       File archivo = multi.getFile("excel_archivo");
	       
	       if(archivo == null){
	    	   message += "<ul><li>No fue posible cargar el archivo: Sin informacion </li></ul>";
	    	   
	       }else{  
	    	   StringBuffer cadena = new StringBuffer().append("<ul><li>Lectura archivo: ok </li>");

	    	   Map mapResult = cargarArchivo(archivo);
	    	   
	    	   List productosCargados = (List) mapResult.get("listaProductos");
	    	   String filasTotales = (String) mapResult.get("filasTotales");
	    	   String filasErrores = (String) mapResult.get("filasErrores");
	    	   String filasValidas = (String) mapResult.get("filasValidas");
	    	   String excesoFilas  = (String) mapResult.get("excesoFilas");
	    	      	   
	    	   
		       cadena.append("<li>Filas encontradas: "+filasTotales+"</li>");		       
		       cadena.append("<li>Filas con errores: "+filasErrores+"</li>");
		       cadena.append("<li>Filas Validas    : "+filasValidas+"</li>");
		       
			   BizDelegate biz = new BizDelegate();
		       Map processRow = new HashMap();
		       
		       //accion a realizar
		       if(tipOperacion.equals("0")){
		    	   //carga masiva de banner de productos
		    	   processRow = biz.updBannerProducto(productosCargados, locales, obs, usr);
		       }else{
		    	   //revert, eliminacion de forma masiva
		    	   processRow = biz.revertBannerProducto(productosCargados, locales, obs, usr);
		       }
		       
		       cadena.append("<li>Filas Procesadas : "+processRow.get(ROW_OK)+"</li>");		       
		       cadena.append("<li>Filas Procesadas con errores: "+processRow.get(ROW_NOK)+"</li>");
		       
		       if("SI".equals(excesoFilas)){
		    	   cadena.append("<li>Filas excedentes: si (Maximo permitido 3000 filas)</li>");
		       }
		       cadena.append("<li>Proceso de Carga completa: ok</li></ul>");
		       message += cadena.toString();
		   }
	       
	    } catch (ParametroObligatorioException e) {
			message += "<ul><li>No fue posible cargar el archivo: Parametros imcopletos </li></ul>";
			
	    } catch (IOException e) {
			message += "<ul><li>No fue posible cargar el archivo: Error de lectura </li></ul>";
			
		} catch (Exception e) {
			message += "<ul><li>No fue posible cargar el archivo: Formato incorrecto </li></ul>";
		}
		
	   message += "</body></html><script>parent.$j('#div_cargando').fadeOut('slow');</script>";
       res.setContentType("text/html");
       PrintWriter out = res.getWriter();
       out.println(message); 
       out.flush();
   }
   
   /*
    * cargarArchivo
    * @ param
    */
   private Map cargarArchivo(File archivo) throws IOException {
       FileInputStream inputfile = new FileInputStream(archivo.getAbsolutePath());
       HSSFWorkbook xls = new HSSFWorkbook(inputfile);
       HSSFSheet sheet = xls.getSheetAt(0);
       List listaProductos = new ArrayList();
       Map mapeoListaProductos = new HashMap();
       Map mapResult = new HashMap();
       
       String excesoFilas = "NO";
       int filasTotales = 0;
       int filasErrores = 0;
       int filasValidas = 0;
       
       for (int i = 1; i <= sheet.getLastRowNum(); i++) {
           HSSFRow row = sheet.getRow(i);
       	   if(row.getRowNum() > MAXIMO_NUMERO_DE_FILAS){  
       		   excesoFilas = "SI";
       		   break;
       	   }
       	   
			filasTotales++;
			FOProductoBannerDTO prob = new FOProductoBannerDTO();
               
				//Identificador numereico
               HSSFCell colAlias = row.getCell(0); //primera columna
               if(colAlias!=null){
	               if(HSSFCell.CELL_TYPE_NUMERIC==colAlias.getCellType()){
		        	   	double dato = colAlias.getNumericCellValue();
		           		prob.setId((int) dato);
		           }else{
		        	   filasErrores++;
		        	   continue;
		           }
               }else{
	        	   filasErrores++;
	        	   continue;
	           }
	           
               //ID_PRODUCTO SAP PRODUCTO SOLO SE UTILIZA ID_PRODUCTO
	           colAlias = row.getCell(1); 
	           if(colAlias!=null){
		           if(HSSFCell.CELL_TYPE_NUMERIC==colAlias.getCellType()){
		        	   	double codSap = colAlias.getNumericCellValue();
		           		prob.setCodSap(String.valueOf((int)codSap) );
		           		prob.setIdProducto((long)codSap);
		           }else{
		        	   if(HSSFCell.CELL_TYPE_STRING==colAlias.getCellType()){
		        		    String codSap = colAlias.getStringCellValue();
			           		prob.setCodSap(codSap);
			           		prob.setIdProducto(Long.parseLong(codSap));
		        	   }else{
		        		   filasErrores++;
			        	   continue;
		        	   }
		           }
	           }else{
	        	   filasErrores++;
	        	   continue;
	           }
	           
	           //Imagen banner
	           colAlias = row.getCell(2); 
	           if(colAlias!=null){
	        	   if(HSSFCell.CELL_TYPE_STRING==colAlias.getCellType()){
	        		    String nombreBanner = colAlias.getStringCellValue();	      	        		    
	        		    if(nombreBanner != null){
	        		    	 if(!"".equals(nombreBanner) &&  nombreBanner.matches("([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)")){
	        		    		 prob.setNombreBanner(nombreBanner);
	        		    	 }	else{
	        		    		 prob.setNombreBanner("img_vacio.gif");
	        		    	 }        		    	
	        		    }else{
	        		    	 filasErrores++;
	        		    	 continue;
	        		    }	           		
	        	   }else{
	        		   filasErrores++;
		        	   continue;
	        	   }		           
	           }else{
	        	   filasErrores++;
	        	   continue;
	           }
	           
	           //Descripcion banner
	           colAlias = row.getCell(3); 
	           if(colAlias!=null){
		           if(HSSFCell.CELL_TYPE_NUMERIC==colAlias.getCellType()){
		        	   	double descripcionBanner = colAlias.getNumericCellValue();
		           		prob.setDescripcionBanner(String.valueOf((int)descripcionBanner) );
		           }else{
		        	   if(HSSFCell.CELL_TYPE_STRING==colAlias.getCellType()){
		        		    String descripcionBanner = colAlias.getStringCellValue();
			           		prob.setDescripcionBanner(descripcionBanner);
		        	   }else{
		        		   filasErrores++;
			        	   continue;
		        	   }
		           }
	           }else{
	        	   filasErrores++;
	        	   continue;
	           }
	           
	           //Color banner
	           colAlias = row.getCell(4); 
	           if(colAlias!=null){
		           if(HSSFCell.CELL_TYPE_NUMERIC==colAlias.getCellType()){
		        	   filasErrores++;
		        	   continue;
		           }else{
		        	   if(HSSFCell.CELL_TYPE_STRING==colAlias.getCellType()){
		        		    String colorBanner = colAlias.getStringCellValue();
		        		    if(colorBanner != null && colorBanner.matches("^#[A-Z0-9]{6}")){
		        		      	prob.setColorBanner(colorBanner);
		        		    }else{
		        		    	prob.setColorBanner("#B53137");
		        		    }
		        	   }else{
		        		   filasErrores++;
			        	   continue;
		        	   }
		           }
	           }else{
	        	   filasErrores++;
	        	   continue;
	           }
	           
	           if(!"".equals(prob.getCodSap())){
	        	   if(!mapeoListaProductos.containsKey(prob.getCodSap())){
	        		   mapeoListaProductos.put(prob.getCodSap(), prob);
	        		   filasValidas++;
	        		   listaProductos.add(prob);
	        	   }else{
	        		   filasErrores++;
	        	   }
	           }else{
	        	   filasErrores++;
	           }	           
       }
       mapResult.put("listaProductos", listaProductos);
       mapResult.put("filasValidas", String.valueOf(filasValidas));
       mapResult.put("filasErrores", String.valueOf(filasErrores));
       mapResult.put("filasTotales", String.valueOf(filasTotales));
       mapResult.put("excesoFilas", String.valueOf(excesoFilas));       
       archivo.delete();
       return mapResult;
   }
   
   private String verificaNull(String dato) throws ParametroObligatorioException {
       if (dato == null) {
           throw new ParametroObligatorioException("Dato es null");
       }
       return dato;
   }   
}