package cl.bbr.boc.command;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.BOCargaSapMasivaDTO;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.utils.Constantes;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.contenidos.exceptions.ProductosException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import com.oreilly.servlet.MultipartRequest;

/**
 * 
 * @author jolazogu
 *
 */
public class CargaSapMasivaAction extends Command{

    private static final long serialVersionUID = 6373335584019595322L;
 	
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

    	HttpSession session = null;
    	
 		try{
 			
 			session = req.getSession();
			
 			res.setCharacterEncoding("UTF-8");
			logger.info( "Inicio de CargaSapMasivaAction ::"+ usr.getLogin() );
			
			String rutaUpload = req.getSession().getServletContext().getRealPath( "upload_carga_sap_masiva" );
			
			logger.debug( "rutaUpload:" + rutaUpload );
			
			MultipartRequest multi = new MultipartRequest( req, rutaUpload, 900000000 );
	        
	        ForwardParameters fp = new ForwardParameters();
	        fp.add( req.getParameterMap() );
	        
	        File archivo = multi.getFile( "archivoCargaSAP" );
	        
	        File directorioDestino = new File( rutaUpload );
	       
	        if ( !archivo.getName().substring( archivo.getName().length()-4, archivo.getName().length() ).equalsIgnoreCase(".xls") ) {
	        	
	            logger.error( "Existió un error en la carga de actualizacion de la configuracion de carga SAP. No es un archivo XLS." );
	            res.sendRedirect( cmd_dsp_error +"?mensaje="+Constantes.MSJ_EVENTO_SIN_ARCHIVO+"&PagErr=1" );
	            borrar( directorioDestino, archivo );
	            return;
            
	        }
	        
	        FileInputStream archivoCargaMasivaXLS = new FileInputStream( archivo ); 
	        
	        logger.debug( "Se empieza a leer archivo ::::: " +archivo.getName()+ " ...." );
			
	        List listProductosXls = listaProductosXls( archivoCargaMasivaXLS );
	        
	        logger.debug( "Se obtiene los datos del archivo ::::: " +archivo.getName()+ " ...." );
			
	        logger.debug( "Eliminamos archivo ::::: " +archivo.getName()+ " ...." );
			
	        borrar( directorioDestino, archivo );
			
			logger.debug( "Archivo eliminado..." );
			 
			logger.debug( "Se empieza a validar los datos en el archivo ::::: " +archivo.getName()+ " ...." );
			
			List listaMensajesValidacion = new ArrayList();
			
			listaMensajesValidacion = validarExcel( listProductosXls );
	        
			logger.debug( "Se valido los datos en el archivo ::::: " +archivo.getName()+ " ...." );
			
	        BizDelegate biz = new BizDelegate();
	        
	        res.setCharacterEncoding( "UTF-8" );
		    View salida = new View(res);

		    String html = getServletConfig().getInitParameter( "TplFile" );
		    html = path_html + html;

		    TemplateLoader load = new TemplateLoader( html );
		    ITemplate tem = load.getTemplate();
		    IValueSet top = new ValueSet();
		    
		    top.setVariable( "{msj}", "");
		    top.setVariable( "{error}", "");
		    List listaVacia = new ArrayList();
		    top.setDynamicValueSets("listaMensajesValidacion", listaVacia );
		    
		    if( listaMensajesValidacion.size() == 0 && listProductosXls.size() !=0 ) {
	            
	        	logger.info( "Los datos contenidos en el archivo " +archivo.getName()+ "...esta OK..." );
	        	
	        	logger.info( "Los datos fueron validados con exito en base de datos...." );
	        		
	        	logger.info( "Se inicia el proceso de cargar los productos nuevos de SAP...." );
	        		
	        	if( biz.getInsertandoBOProducto(listProductosXls, usr)) {
	        		
	        		top.setVariable( "{msj}", "Los productos han sido agregados masivamente." );
        			session.setAttribute("listaProductosCargaSapMasiva", listProductosXls);
        			top.setVariable( "{mensajeError}", " " );
        			
		        }else
		        	top.setVariable( "{msj}", "No se asociaron los productos con exito..." );
	        			
	        }else if( listProductosXls.size() == 0 ) {
	        		
	        	top.setVariable( "{error}", "La planilla tiene errores en los datos." );	        	
	        	top.setDynamicValueSets( "listaMensajesValidacion", listaMensajesValidacion );
	        	top.setVariable( "{mensajeError}", " " );	        
	        	
	    		 	
	        }else if( listaMensajesValidacion.size() != 0 ) {
        		
        		top.setVariable( "{error}", "La planilla tiene los siguientes errores:" );	        	
	        	top.setDynamicValueSets( "listaMensajesValidacion", listaMensajesValidacion );
	        	top.setVariable( "{mensajeError}", "style='overflow-y: scroll; height:300px;'" );	        
        	 	
	        }		
		    
	        
	        archivo.delete();	    			    
		    
		    top.setVariable( "{hdr_nombre}"	, usr.getNombre() + " " + usr.getApe_paterno() );
			Date now = new Date();
			top.setVariable( "{hdr_fecha}"	, now.toString() );
			
		    String result = tem.toString( top );
	        salida.setHtmlOut( result );
	        
	        salida.Output();
	        
		
 		}catch( Exception e ) {
			
 			logger.error("Problema al importar archivo XLS : " + e);
 			throw new ProductosException("Problema al importar archivo XLS");
 		}
        
	}
    
   /**
    * 
    * @param input
    * @return
    */
    private List listaProductosXls( InputStream input ) {

		BOCargaSapMasivaDTO cargaSapMasiva = null;
		List listCargaSapMasiva = new ArrayList();
	    POIFSFileSystem fs;
	    
	    Map listaLocalesON = null;
	    Map listaLocalesOFF = null;
	  
	    try {
		   
	    	fs = new POIFSFileSystem( input );
		    HSSFWorkbook wb = new HSSFWorkbook( fs );
		    HSSFSheet sheet = wb.getSheetAt( 0 );
		    int numRows = sheet.getLastRowNum();
		    
		    logger.info( "numRows Carga SAP Masiva::::: " + numRows );
		
		    Iterator rows = sheet.rowIterator();
		   
		    while ( rows.hasNext() ) {
		       
		    	HSSFRow row = ( HSSFRow ) rows.next();
		       
		    	if( row.getRowNum() == 0 )
		            continue;
		
		    	cargaSapMasiva = new BOCargaSapMasivaDTO();
		    	
		    	listaLocalesON = new HashMap();
		    	listaLocalesOFF = new HashMap();
		    	
		    	 HSSFCell colAlias = row.getCell(0); //primera columna
		    	
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
		    			 cargaSapMasiva.setCodigoSap(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING){
			    		 cargaSapMasiva.setCodigoSap(  colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 cargaSapMasiva.setCodigoSap(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 cargaSapMasiva.setCodigoSap("");
		    	 }
		    	  
		    	 colAlias = row.getCell(1);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 cargaSapMasiva.setUnidadMedida( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
		    			 cargaSapMasiva.setUnidadMedida(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
		    			 cargaSapMasiva.setUnidadMedida(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 cargaSapMasiva.setUnidadMedida("");
		    	 }
		    	  
		    	 colAlias = row.getCell(2);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 cargaSapMasiva.setDescripcion( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
		    			 cargaSapMasiva.setDescripcion(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
		    			 cargaSapMasiva.setDescripcion(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 cargaSapMasiva.setDescripcion("");
		    	 }
		    	  
		    	 colAlias = row.getCell(3);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 cargaSapMasiva.setIdGrupo( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
		    			 cargaSapMasiva.setIdGrupo(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
		    			 cargaSapMasiva.setIdGrupo(colAlias.toString());
			    	 }
		    		 
		    	 }else {
		    		 cargaSapMasiva.setIdGrupo("");
		    	 }
		    	 
		    	 colAlias = row.getCell(4);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 cargaSapMasiva.setCodBarra( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
		    			 cargaSapMasiva.setCodBarra(String.valueOf((long)colAlias.getNumericCellValue()));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
		    			 cargaSapMasiva.setCodBarra(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 cargaSapMasiva.setCodBarra("");
		    	 }
		    	  
		    	 colAlias = row.getCell(5);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 cargaSapMasiva.setPrecio( new BigDecimal(colAlias.toString()) );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 cargaSapMasiva.setPrecio(new BigDecimal( colAlias.getNumericCellValue() ));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 cargaSapMasiva.setPrecio(new BigDecimal(colAlias.toString()));
			    	 }
			    	
		    	 }else {
		    		 cargaSapMasiva.setPrecio(new BigDecimal(0));
		    	 }
		    	  
		    	 colAlias = row.getCell(6);
			    	
		    	 if (colAlias != null){
		    		 	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 listaLocalesON.put( "J506", colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaLocalesON.put( "J506", colAlias.toString()  );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 
		    		 listaLocalesOFF.put( "J506", "" );
		    	 
		    	 }
		    	 
		    	 colAlias = row.getCell(7);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 
		    			 if (colAlias.toString().trim().equalsIgnoreCase("X"))
		    				 listaLocalesON.put( "J507", colAlias.toString() );
		    			 
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 
			    		 if (colAlias.toString().trim().equalsIgnoreCase("X"))
			    			 listaLocalesON.put("J507", colAlias.toString() );
			    		 
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 
		    		 listaLocalesOFF.put( "J507", "" );
		    	 
		    	 }
		    	 
		    	 colAlias = row.getCell(8);
			    	
		    	 if (colAlias != null){
				    	 
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 
		    			 if (colAlias.toString().trim().equalsIgnoreCase("X"))
		    			 	 listaLocalesON.put( "J508", colAlias.toString() );
		    			 
		    		 }
				     else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				    	 
				    	 if (colAlias.toString().trim().equalsIgnoreCase("X"))
				    	 	 listaLocalesON.put( "J508", colAlias.toString() );
				    	 
				     }
				    
		    	 }else {
		    	
		    		 listaLocalesOFF.put( "J508", "" );
		    	 
		    	 }
		    	 
		    	 colAlias = row.getCell(9);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 
		    			 if (colAlias.toString().trim().equalsIgnoreCase("X"))
		    				 listaLocalesON.put( "J510", colAlias.toString() );
		    			 
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 
			    		 if (colAlias.toString().trim().equalsIgnoreCase("X"))
			    			 listaLocalesON.put( "J510", colAlias.toString() );
			    		 
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 
		    		 listaLocalesOFF.put( "J510", "" );
		    	 
		    	 }
		    	 
		    	 colAlias = row.getCell(10);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 
		    			 if (colAlias.toString().trim().equalsIgnoreCase("X"))
		    				 listaLocalesON.put( "J512", colAlias.toString() );
		    			 
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 
			    		 if (colAlias.toString().trim().equalsIgnoreCase("X"))	
			    			 listaLocalesON.put("J512", colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		
		    		 listaLocalesOFF.put( "J512", "" );
		    	 
		    	 }
		    	
		    	 colAlias = row.getCell(11);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 
		    			 if (colAlias.toString().trim().equalsIgnoreCase("X"))
		    				 listaLocalesON.put( "J514", colAlias.toString() );
			    	 
		    		 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    	
			    		 if (colAlias.toString().trim().equalsIgnoreCase("X"))
			    			 listaLocalesON.put( "J514", colAlias.toString()  );
			    	 
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    	
		    		 listaLocalesOFF.put( "J514", "" );
		    	 
		    	 }
		    	 
		    	 colAlias = row.getCell(12);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 
		    			 if (colAlias.toString().trim().equalsIgnoreCase("X"))
		    				 listaLocalesON.put( "J521", colAlias.toString() );
			    	 
		    		 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    	
			    		 if (colAlias.toString().trim().equalsIgnoreCase("X"))
			    			 listaLocalesON.put( "J521", colAlias.toString() );
			    	 
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 
		    		 listaLocalesOFF.put( "J521", "" );
		    	 
		    	 }
		    	 
		    	 colAlias = row.getCell(13);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
		    			 
		    			 if (colAlias.toString().trim().equalsIgnoreCase("X"))
		    				 listaLocalesON.put( "J624", colAlias.toString() );
			    	 
		    		 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 
			    		 if (colAlias.toString().trim().equalsIgnoreCase("X"))
			    			 listaLocalesON.put( "J624", colAlias.toString() );
			    	 
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    	
		    		 listaLocalesOFF.put( "J624", "" );
		    	 
		    	 }
		    	 
		    	 
		    	cargaSapMasiva.setLocalesON(listaLocalesON);
		    	cargaSapMasiva.setLocalesOFF(listaLocalesOFF);
		    	
		    	if (cargaSapMasiva.getCodigoSap() != null){
		   
		    		listCargaSapMasiva.add( cargaSapMasiva );
		    		
		    	}
		        
		    }
		    
	   
	    } catch ( Exception e ) {
		    
	    	e.printStackTrace();
		    logger.info( e.toString() );
	   
	    }
		
	    return listCargaSapMasiva;

	}

 	/**
 	 * 
 	 * @param directorio
 	 * @param archivo
 	 */
    public static void borrar( File directorio, File archivo ) {
    	
    	if ( directorio.exists() ) {
            
    		for ( int i = 0; i < directorio.listFiles().length; i++ ) {
                
    			File file = ( File ) directorio.listFiles()[ i ];
                    
    			if( file.exists() && file.equals( archivo ) ) {
	            
    				file.delete(); 
	            
    			}
                
    		}                
            
    	}		
    
    }

    /**
     * 
     * @param listaCatalogacionMasiva
     * @return
     */
    private List validarExcel( List listaCargaSapMasiva ) {

    	List listaMensajesValidacion = new ArrayList();
    	
    	try {
    		 
    		logger.info("validando archivo carga SAP masiva.....");
    		   
    		Iterator it = listaCargaSapMasiva.iterator();
    		
		    while ( it.hasNext() ) {
 
		    	BOCargaSapMasivaDTO cargaSapMasiva = ( BOCargaSapMasivaDTO )it.next();
		    	
   				String mensajeValidacion = validarRegistro( cargaSapMasiva );
   				
   				if( !"".equals( mensajeValidacion.trim() ) ) {
   	    		    	
   	    		        	IValueSet pr = new ValueSet();
   	    		        	pr.setVariable( "{mensaje}", mensajeValidacion );
   	    		        	listaMensajesValidacion.add( pr );
   	    		}
   				
    		}
    	   
    	} catch ( Exception e ) {
    		   	
    		IValueSet pr = new ValueSet();
    		pr.setVariable( "{mensaje}", "Error, verifique que si los datos estan correctos en la planilla." );
    		listaMensajesValidacion = new ArrayList();
    		listaMensajesValidacion.add( pr );
    	   
    	}
    		
    	return listaMensajesValidacion;
    	
    }
    
    
    /**
     * 
     * @param cargaSapMasiva
     * @return
     * @throws BocException
     */
    private String validarRegistro( BOCargaSapMasivaDTO cargaSapMasiva ) throws BocException {
	
    	String msjError = "SKU: " +cargaSapMasiva.getCodigoSap() + " - " +cargaSapMasiva.getUnidadMedida()+ "<BR>";
		boolean error = false;
		BizDelegate big = new BizDelegate();
		
		if( cargaSapMasiva.getCodigoSap().trim().equals("") ) {
			
			msjError = msjError + " ; campo Codigo SAP debe ser obligatorio.<BR>";
			error = true;
	
		}else if( !isNumeric(cargaSapMasiva.getCodigoSap().trim() )) {
			
			msjError = msjError + " ; campo Codigo SAP debe ser numerico.<BR>";
			error = true;
	
		}
		
		if( cargaSapMasiva.getUnidadMedida().trim().equals("") ) {
			
			msjError = msjError + " ; campo Unidad de medida debe ser obligatorio.<BR>";
			error = true;
	
		}else if( isNumeric(cargaSapMasiva.getUnidadMedida().trim()) ) {
			
			msjError = msjError + " ; campo Unidad de medida debe no debe ser numerico.<BR>";
			error = true;
	
		}
		
		if( cargaSapMasiva.getDescripcion().trim().equals("") ) {
			
			msjError = msjError + " ; campo Descripcion debe ser obligatorio.<BR>";
			error = true;
	
		}else if( isNumeric(cargaSapMasiva.getDescripcion().trim()) ) {
			
			msjError = msjError + " ; campo Descripcion debe no debe ser numerico.<BR>";
			error = true;
	
		}
		
		if( cargaSapMasiva.getIdGrupo().trim().equals("") ) {
			
			msjError = msjError + " ; campo Id Grupo debe ser obligatorio.<BR>";
			error = true;
	
		}else if( !isLong( cargaSapMasiva.getIdGrupo().trim()) ) {
			
			msjError = msjError + " ; campo Id Grupo debe ser numerico.<BR>";
			error = true;
	
		}
		else if ( !big.getExisteIdGrupo(cargaSapMasiva.getIdGrupo().trim())){
			
			msjError = msjError + " ; campo Id Grupo " +cargaSapMasiva.getIdGrupo()+ " no es valido.<BR>";
		}
		
		if( cargaSapMasiva.getCodBarra().trim().equals("") ) {
			
			msjError = msjError + " ; campo Codigo Barra debe ser obligatorio.<BR>";
			error = true;
	
		}else if( !isLong( cargaSapMasiva.getCodBarra().trim()) ) {
			
			msjError = msjError + " ; campo Codigo Barra debe ser numerico.<BR>";
			error = true;
	
		}
		
		if( cargaSapMasiva.getPrecio() == null || cargaSapMasiva.getPrecio().equals(new BigDecimal(0))) {
			
			msjError = msjError + " ; campo Precio debe ser obligatorio.<BR>";
			error = true;
			
		}else if( !isNumeric( cargaSapMasiva.getPrecio()) ) {
			
			msjError = msjError + " ; campo Precio debe ser numerico y/o sin puntos.<BR>";
			error = true;
	
		}
		
		if( cargaSapMasiva.getLocalesON().isEmpty() ) {
			
			msjError = msjError + " ; es obligatorio marcar algun local.<BR>";
			error = true;
			
		}
	
		
		if( error == true )		
			return msjError;	
		else
			return "";
		
	
    }
    
   
    /**
     * 
     * @param cadena
     * @return
     */
	private static boolean isNumeric(String cadena){
		
		try {
			
			if( cadena.indexOf( '.' ) != -1 ){
				
				Double.parseDouble(cadena);
				return true;
			
			}
			else { 
			
				Integer.parseInt(cadena);
				return true;
			
			}
			
		} catch (NumberFormatException nfe){
			return false;
		}
	
	}
	

	/**
	 * 
	 * @param cadena
	 * @return
	 */
	private static boolean isNumeric(BigDecimal cadena){
		
		try {
			if( cadena.toString().indexOf( '.' ) != -1 )
				return false;
			else 
				return true;
			
		} catch (NumberFormatException nfe){
			return false;
		}
	
	}
	
	/**
	 * 
	 * @param cadena
	 * @return
	 */
	private static boolean isLong(String cadena){
		
		try {
			if( cadena.toString().indexOf( '.' ) != -1 )
				return false;
			else{
				Long.parseLong(cadena);
				return true;
			}
				
		} catch (NumberFormatException nfe){
			return false;
		}
	
	}
	
}