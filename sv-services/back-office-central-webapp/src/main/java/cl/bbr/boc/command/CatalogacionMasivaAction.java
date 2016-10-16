package cl.bbr.boc.command;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import org.apache.poi.hssf.record.formula.functions.Cell;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.BOCatalogacionMasivaDTO;
import cl.bbr.boc.dto.BOStockONLineDTO;
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
public class CatalogacionMasivaAction extends Command{

    private static final long serialVersionUID = 6373335584019595322L;
 	
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

    	HttpSession session = null;
    	
 		try{
 			
 			session = req.getSession();
			
 			res.setCharacterEncoding("UTF-8");
			logger.info( "Inicio de CatalogacionMasivaAction ::"+ usr.getLogin() );
			
			String rutaUpload = req.getSession().getServletContext().getRealPath( "upload_catalogacion_masiva" );
			
			logger.debug( "rutaUpload:" + rutaUpload );
			
			MultipartRequest multi = new MultipartRequest( req, rutaUpload, 900000000 );
	        
	        ForwardParameters fp = new ForwardParameters();
	        fp.add( req.getParameterMap() );
	        
	        File archivo = multi.getFile( "archivoCatalogacionMasiva" );
	       
	        if ( !archivo.getName().substring( archivo.getName().length()-4, archivo.getName().length() ).equalsIgnoreCase(".xls") ) {
	        	
	            logger.error( "Existió un error en la carga de actualizacion de la configuracion de stock online. No es un archivo XLS." );
	            res.sendRedirect( cmd_dsp_error +"?mensaje="+Constantes.MSJ_EVENTO_SIN_ARCHIVO+"&PagErr=1" );
	            return;
            
	        }
	        
	        FileInputStream archivoCargaMasivaXLS = new FileInputStream( archivo ); 
	        
	        logger.debug( "Se empieza a leer archivo ::::: " +archivo.getName()+ " ...." );
			
	        List listProductosXls = listaProductosXls( archivoCargaMasivaXLS );
	        
	        logger.debug( "Se obtiene los datos del archivo ::::: " +archivo.getName()+ " ...." );
			
	        logger.debug( "Eliminamos archivo ::::: " +archivo.getName()+ " ...." );
			
	        File directorioDestino = new File( rutaUpload );
			
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
		    
		    if( listaMensajesValidacion.size() == 0 ) {
	            
	        	logger.info( "Los datos contenidos en el archivo " +archivo.getName()+ "...esta OK..." );
	        	
	        	logger.info( "Los datos fueron validados con exito en base de datos...." );
	        		
	        	logger.info( "Se inicia el proceso de agregar/modificar los productos al Mix...." );
	        		
	        	if( biz.getAgregandoProductosAlMix(listProductosXls, usr)) {
	        		
	        		logger.info( "Se agregaron/modificaron los productos al Mix...." );
	        			
	        		logger.info( "Se inicia el proceso de catalogar los productos...." );
		        		
	        		if (biz.getCatalogarProducto(listProductosXls, usr)) {
		        			
	        			top.setVariable( "{msj}", "Los productos ha sido catalogados masivamente." );
	        			session.setAttribute("listaProductosCatalogacionMasiva", listProductosXls);
	        			top.setVariable( "{mensajeError}", " " );
	        			
	        		}else
	        		
	        			top.setVariable( "{msj}", "No se asociaron los productos con exito..." );
	 	        		
	        	}else {
	        		
	        		top.setVariable( "{msj}", "No se agregaron los productos al Mix con exito..." );
	        	
	        	}
	        	
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

		BOCatalogacionMasivaDTO catalogacionMasiva = null;
		List listCatalogacionMasiva = new ArrayList();
	    POIFSFileSystem fs;
	    
	    List listaCategorias = null;
	  
	    try {
		   
	    	fs = new POIFSFileSystem( input );
		    HSSFWorkbook wb = new HSSFWorkbook( fs );
		    HSSFSheet sheet = wb.getSheetAt( 0 );
		    int numRows = sheet.getLastRowNum();
		    
		    logger.info( "numRows Catalogacion Masiva::::: " + numRows );
		
		    Iterator rows = sheet.rowIterator();
		   
		    while ( rows.hasNext() ) {
		       
		    	HSSFRow row = ( HSSFRow ) rows.next();
		       
		    	if( row.getRowNum() == 0 )
		            continue;
		
		    	catalogacionMasiva = new BOCatalogacionMasivaDTO();
		    	listaCategorias = new ArrayList(); 
		    	
		    	
		    	 HSSFCell colAlias = row.getCell(0); //primera columna
		    	
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setCodigoSap(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING){
			    		 catalogacionMasiva.setCodigoSap(  colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setCodigoSap(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setCodigoSap("");
		    	 }
		    	  
		    	 colAlias = row.getCell(1);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setUnidadMedida( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setUnidadMedida(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setUnidadMedida(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setUnidadMedida("");
		    	 }
		    	  
		    	 colAlias = row.getCell(2);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setTipoProducto( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setTipoProducto(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setTipoProducto(colAlias.toString());
			    	 }
		    		 
			    	 
		    	 }else {
		    		 catalogacionMasiva.setTipoProducto("");
		    	 }
		    	 
		    	 colAlias = row.getCell(3);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setMarcaProducto( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setMarcaProducto(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setMarcaProducto(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setMarcaProducto("");
		    	 }
		    	 
		    	 colAlias = row.getCell(4);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setDescCortaProducto( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setDescCortaProducto(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setDescCortaProducto(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setDescCortaProducto("");
		    	 }
		    	  
		    	 colAlias = row.getCell(5);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setDescLargaProducto( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setDescLargaProducto(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setDescLargaProducto(colAlias.toString());
			    	 }
		    		 
		    	 }else {
		    		 catalogacionMasiva.setDescLargaProducto("");
		    	 }
		    	 
		    	 colAlias = row.getCell(6);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setUnidadMedidaPPM( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setUnidadMedidaPPM(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setUnidadMedidaPPM(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setUnidadMedidaPPM("");
		    	 }
		    	  
		    	 colAlias = row.getCell(7);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setContenidoPPM( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setContenidoPPM(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setContenidoPPM(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setContenidoPPM("");
		    	 }
		    	  
		    	 colAlias = row.getCell(8);
		    	
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setComentarioPPM( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setComentarioPPM(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setComentarioPPM(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setComentarioPPM("");
		    	 }
		    	  
		    	 colAlias = row.getCell(9);
			    	
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setPreparable( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setPreparable(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setPreparable(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setPreparable("");
		    	 }
		    	  
		    	 colAlias = row.getCell(10);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setIntervaloMedida( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setIntervaloMedida(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setIntervaloMedida(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setIntervaloMedida("");
		    	 }
		    	  
		    	 colAlias = row.getCell(11);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setMaximo( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setMaximo(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setMaximo(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setMaximo("");
		    	 }
		    	 
		    	 colAlias = row.getCell(12);
			    	
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setParticionable( colAlias.toString() );
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setParticionable(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
		    		 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setParticionable(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setParticionable("");
		    	 }
		    	  
		    	 colAlias = row.getCell(13);
			    	
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setSectorPicking( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setSectorPicking(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setSectorPicking(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setSectorPicking("");
		    	 }
		    	 
		    	 colAlias = row.getCell(14);
			    	
		    		 catalogacionMasiva.setStockLocales( "" );
		    	
		    	 colAlias = row.getCell(15);
			    	
		    		 catalogacionMasiva.setPublicadoLocales( "" );
		    	
		    	 colAlias = row.getCell(16);
			    	
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 catalogacionMasiva.setEvitarPublicacion( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 catalogacionMasiva.setEvitarPublicacion(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 catalogacionMasiva.setEvitarPublicacion(colAlias.toString());
			    	 }
			    	
		    	 }else {
		    		 catalogacionMasiva.setEvitarPublicacion("");
		    	 }
		    	 
		    	 colAlias = row.getCell(17);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(18);
		    	 
		    	 if (colAlias != null){
		    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    		 
		    	 colAlias = row.getCell(19);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(20);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(21);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(22);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(23);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(24);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(25);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(26);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(27);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(28);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(29);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(30);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	 colAlias = row.getCell(31);
			    	
		    	 if (colAlias != null){
				    	
		    		 if (colAlias.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			    		 listaCategorias.add( colAlias.toString() );
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			    		 listaCategorias.add(String.valueOf( (int) Double.parseDouble( String.valueOf( colAlias.getNumericCellValue() ))));
			    	 }
			    	 else if (colAlias.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			    		 //listaCategorias.add(colAlias.toString());
			    	 }
			    	
		    	 }
		    	 
		    	
		    	catalogacionMasiva.setCategorias(listaCategorias);
		    	
		    	if (catalogacionMasiva.getCodigoSap() != null){
		   
		    		listCatalogacionMasiva.add( catalogacionMasiva );
		    		
		    	}
		        
		    }
		    
	   
	    } catch ( Exception e ) {
		    
	    	e.printStackTrace();
		    logger.info( e.toString() );
	   
	    }
		
	    return listCatalogacionMasiva;

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
    private List validarExcel( List listaCatalogacionMasiva ) {

    	List listaMensajesValidacion = new ArrayList();
    	
    	try {
    		 
    		logger.info("validando archivo catalogacion masiva.....");
    		   
    		Iterator it = listaCatalogacionMasiva.iterator();
    		
		    while ( it.hasNext() ) {
 
		    	BOCatalogacionMasivaDTO catalogacionMasiva = ( BOCatalogacionMasivaDTO )it.next();
		    	
   				String mensajeValidacion = validarRegistro( catalogacionMasiva );
   				
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
     * @param cell_Sku
     * @param cell_Media
     * @param cell_Marca
     * @param cell_UnidadMedidaPPM
     * @param cell_ContenidoPPM
     * @param cell_AdmiteComentario
     * @param cell_EsPreparable
     * @param cell_intervaloMedida
     * @param cell_Maximo
     * @param cell_Particionable
     * @param cell_SectorPicking
     * @param cell_Stock_Locales
     * @param cell_Publicado_Locales
     * @param cell_EvitarPublicacion
     * @param categorias
     * @return
     * @throws BocException
     */
    private String validarRegistro( BOCatalogacionMasivaDTO catalogacionMasiva ) throws BocException {
	
    	String msjError = "SKU: " +catalogacionMasiva.getCodigoSap() + " - " +catalogacionMasiva.getUnidadMedida()+ "<BR>";
		boolean error = false;
		BizDelegate biz = new BizDelegate();
		String mensaje = "";
		
		if (catalogacionMasiva.getCodigoSap() != null) {
		
		/**
		 *  Se valida que los campos no esten vacios.
		 */
		if( catalogacionMasiva.getCodigoSap().equals("") ) {
			
			msjError = msjError + " ; campo Codigo SAP debe ser obligatorio.<BR>";
			error = true;
	
		}else if( !isNumeric(catalogacionMasiva.getCodigoSap() )) {
			
			msjError = msjError + " ; campo Codigo SAP debe ser numerico.<BR>";
			error = true;
	
		}else{
			
			mensaje = biz.getValidandoDatosBO( catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida() );
			
			if( !mensaje.equals("") ) {
			
				msjError = msjError + mensaje;
				error = true;
			
			}
			
		}
		
		if( catalogacionMasiva.getUnidadMedida().equals("") ) {
			
			msjError = msjError + " ; campo Unidad de medida debe ser obligatorio.<BR>";
			error = true;
	
		}else if( isNumeric(catalogacionMasiva.getUnidadMedida()) ) {
			
			msjError = msjError + " ; campo Unidad de medida debe no debe ser numerico.<BR>";
			error = true;
	
		}
		
		if( catalogacionMasiva.getTipoProducto().equals("") ) {
			
			msjError = msjError + " ; campo Tipo producto debe ser obligatorio.<BR>";
			error = true;
	
		}else if( isNumeric(catalogacionMasiva.getTipoProducto()) ) {
			
			msjError = msjError + " ; campo Tipo producto debe no debe ser numerico.<BR>";
			error = true;
	
		}
		
		if( catalogacionMasiva.getMarcaProducto().equals("") ) {
			
			msjError = msjError + " ; campo Marca producto debe ser obligatorio.<BR>";
			error = true;
	
		}else if( isNumeric( catalogacionMasiva.getMarcaProducto()) ) {
			
			msjError = msjError + " ; campo Marca producto no debe ser numerico.<BR>";
			error = true;
	
		}else {
			
			if( !biz.getExisteMarcaProducto( catalogacionMasiva.getMarcaProducto() ) ) {
				
				msjError = msjError + " ; campo Marca : " +catalogacionMasiva.getMarcaProducto()+ ", No Existe o esta inhabilitado.<br>";
				error = true;
		
			}
			
		}
		
		if( catalogacionMasiva.getDescCortaProducto().equals("") ) {
			
			msjError = msjError + " ; campo Descripcion corta producto debe ser obligatorio.<BR>";
			error = true;
	
		}else if( isNumeric( catalogacionMasiva.getDescCortaProducto()) ) {
			
			msjError = msjError + " ; campo Descripcion corta producto no debe ser numerico.<BR>";
			error = true;
	
		}
		
		if( catalogacionMasiva.getDescLargaProducto().equals("") ) {
			
			msjError = msjError + " ; campo Descripcion larga producto debe ser obligatorio.<BR>";
			error = true;
	
		}else if( isNumeric( catalogacionMasiva.getDescLargaProducto()) ) {
			
			msjError = msjError + " ; campo Descripcion larga producto debe ser numerico.<BR>";
			error = true;
	
		}
		
		if( catalogacionMasiva.getUnidadMedidaPPM().equals("") ) {
			
			msjError = msjError + " ; campo Unidad de medida PPM debe ser obligatorio.<BR>";
			error = true;
	
		}else if( isNumeric( catalogacionMasiva.getUnidadMedidaPPM()) ) {
			
			msjError = msjError + " ; campo Unidad de medida PPM no debe ser numerico.<BR>";
			error = true;
	
		}else {
			
			if( !biz.getExisteUnidadMedidaPPM( catalogacionMasiva.getUnidadMedidaPPM() ) ) {
				
				msjError = msjError + " ; campo Unidad de medida PPM : " +catalogacionMasiva.getUnidadMedidaPPM()+ ", No Existe o esta inhabilitado.<br>";
				error = true;
		
			}

		}

		if( catalogacionMasiva.getContenidoPPM().equals("") ) {
			
			msjError = msjError + " ; campo contenido PPM debe ser obligatorio.<BR>";
			error = true;
	
		}else if( !isNumeric(catalogacionMasiva.getContenidoPPM()) ) {
			
			msjError = msjError + " ; campo contenido PPM debe ser numerico.<BR>";
			error = true;
	
		}
		
		
		if( catalogacionMasiva.getComentarioPPM().equals("") ) {
			
			msjError = msjError + " ; campo Admite comentario debe ser obligatorio.<BR>";
			error = true;
	
		}else if( !validar_S_N(catalogacionMasiva.getComentarioPPM()) ) {
			
			msjError = msjError + " ; campo Admite comentario : " + catalogacionMasiva.getComentarioPPM() + ", No corresponde.<BR>";
			error = true;
	
		}
		
		if( catalogacionMasiva.getPreparable().equals("") ) {
			
			msjError = msjError + " ; campo Es preparable debe ser obligatorio.<BR>";
			error = true;
	
		}else if( !validar_S_N(catalogacionMasiva.getPreparable()))  {
			
			msjError = msjError + " ; campo Es preparable : " + catalogacionMasiva.getPreparable() + ", No corresponde.<BR>";
			error = true;
	
		}
		
		if (catalogacionMasiva.getIntervaloMedida().equals("")) {
			
			msjError = msjError + " ; campo Intervalo medida debe ser obligatorio.<BR>";
			error = true;
	
		}else if (!isNumeric(catalogacionMasiva.getIntervaloMedida())) {
			
			msjError = msjError + " ; campo Intervalo medida debe ser numérico.<BR>";
			error = true;
	
		}
		
		if (catalogacionMasiva.getMaximo().equals("")) {
			
			msjError = msjError + " ; campo Maximo debe ser obligatorio.<BR>";
			error = true;
	
		}else if (!isNumeric(catalogacionMasiva.getMaximo())) {
			
			msjError = msjError + " ; campo Maximo debe ser numérico.<BR>";
			error = true;
	
		}
		
		if( catalogacionMasiva.getParticionable().equals("") ) {
			
			msjError = msjError + " ; campo Particionable debe ser obligatorio.<BR>";
			error = true;
	
		}else if( !validar_S_N(catalogacionMasiva.getParticionable()) ) {
			
			msjError = msjError + " ; campo Particionable : " + catalogacionMasiva.getParticionable() + ", No corresponde.<BR>";
			error = true;
	
		}
		
		if (catalogacionMasiva.getSectorPicking().equals("")){
		
			msjError = msjError + " ; campo Sector Picking debe ser obligatorio.<BR>";
			error = true;
			
		}else if (!isNumeric(catalogacionMasiva.getSectorPicking())){
			
			msjError = msjError + " ; campo Sector Picking debe ser numérico.<BR>";
			error = true;
			
		}else {
			
			if( !biz.getExisteSectorPicking( catalogacionMasiva.getSectorPicking() ) ) {
				
				msjError = msjError + " ; campo Sector Picking : " +catalogacionMasiva.getSectorPicking()+ ", No Existe.<br>";
				error = true;

			}
			
		}
		
		/*if ( !catalogacionMasiva.getStockLocales().equals("") && !catalogacionMasiva.getStockLocales().equals("TODOS") ) {
			
			mensaje = biz.getLocalNoExiste( catalogacionMasiva.getStockLocales() );
			
			if( !mensaje.equals("") ) {
				
				msjError = msjError + " ; campo Stock locales : " +mensaje+ ", No Existe.<br>";
				error = true;
		
			}
		
		}
			
		if ( !catalogacionMasiva.getPublicadoLocales().equals("") && !catalogacionMasiva.getPublicadoLocales().equals("TODOS")) {
		
			mensaje = biz.getLocalNoExiste( catalogacionMasiva.getPublicadoLocales());
			
			if( !mensaje.equals("") ) {
				
				msjError = msjError + " ; campo Publicado locales : " +mensaje+ ", No Existe.<br>";
				error = true;
		
			}
			
		}*/

		
		if( catalogacionMasiva.getEvitarPublicacion().equals("")) {
			
			msjError = msjError + " ; campo Evitar Publicacion por bloqueo debe ser obligatorio.<BR>";
			error = true;
	
		}else if( !validar_S_N(catalogacionMasiva.getEvitarPublicacion()) ) {
			
			msjError = msjError + " ; campo Evitar Publicacion por bloqueo : " + catalogacionMasiva.getEvitarPublicacion() + ", No corresponde.<BR>";
			error = true;
	
		}
		
	
		if( catalogacionMasiva.getCategorias().isEmpty() ) {
			
			msjError = msjError + " ; campo ids categorias debe ingresar un dato obligatoro.<BR>";
			error = true;
	
		}else if( !isNumeric(catalogacionMasiva.getCategorias()) ) {
			
			msjError = msjError + " ; campo ids categorias debe ser numérico.<BR>";
			error = true;
	
		}else{
			
			mensaje = biz.getExisteCategoria(catalogacionMasiva.getCategorias());
			
			if (!mensaje.equals("")){
				
				msjError = msjError + " ; campo Ids Categorias : " +mensaje+ ".<br>";
				error = true;
		
			}
			
		}
		

		}
	
		
		if( error == true )		
			return msjError;	
		else
			return "";
		
	
    }
    
	private String getExtraerNumeroEntero(String sku) {
	
		String d = sku;
		
		if( sku.indexOf( '.' ) != -1 ) {
			
			 d = sku.substring( 0, sku.indexOf( "." ) );
		
		}
		
		return d;
	}
	
	private List getExtraerNumeroEntero(List categorias) {
		
		List listaCategorias = new ArrayList();
		Iterator it = categorias.iterator();
		
		while(it.hasNext()) {
			
			String categoria = (String)it.next();
		
			if(categoria != null) {
				
				if(!categoria.equals("")) {
					
					if( categoria.indexOf( '.' ) != -1 )
						listaCategorias.add( categoria.substring( 0, categoria.indexOf( "." ) ) );
					else
						listaCategorias.add( categoria );
				
				}
			
			}
			
		}
		
		return listaCategorias;
	
	}

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
	

	
	private static boolean isNumeric(List cadena){
		
	boolean result = false;
	
		try {
			
			Iterator it = cadena.iterator();
			
			while (it.hasNext()) {
				
				Integer.parseInt((String)it.next());	
				result = true;
			}
		
		} catch (NumberFormatException nfe){
			result = false;
		}
		
		return result;
	}
	
	
	private static boolean validar_S_N(String cadena){
		boolean result = false;
				
		if (("S").equals(cadena.toString().trim().toUpperCase()) || ("N").equals(cadena.toString().trim().toUpperCase()))
			result = true;
				
		return result;
			
	}
	
	/**
	 * 
	 * @param cadena
	 * @return
	 */
	private String eliminarAcentos( String cadena ) {
		
		cadena = cadena.replaceAll( "á", "a" );
		cadena = cadena.replaceAll( "é", "e" );
		cadena = cadena.replaceAll( "í", "i" );
		cadena = cadena.replaceAll( "ó", "o" );
		cadena = cadena.replaceAll( "ú", "u" );
		
		cadena = cadena.replaceAll( "Á", "A" );
		cadena = cadena.replaceAll( "É", "E" );
		cadena = cadena.replaceAll( "Í", "I" );
		cadena = cadena.replaceAll( "Ó", "O" );
		cadena = cadena.replaceAll( "Ú", "U" );
		
		return cadena;
		
	}


}