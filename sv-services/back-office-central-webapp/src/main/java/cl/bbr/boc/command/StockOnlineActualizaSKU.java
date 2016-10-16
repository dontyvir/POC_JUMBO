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
import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.BOStockONLineDTO;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.utils.Constantes;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import com.oreilly.servlet.MultipartRequest;

/**
 * 
 * @author jolazogu
 *
 */
public class StockOnlineActualizaSKU extends Command{

    private static final long serialVersionUID = 6373335584019595322L;
 	
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

    	JdbcTransaccion trx = new JdbcTransaccion();
    	
 		try{
 			
 			trx.begin();
 			
 			res.setCharacterEncoding("UTF-8");
			logger.info( "Inicio de StockOnlineActualizaSKU ::"+ usr.getLogin() );
			
			int localCheck = 5;
			
			if( req.getParameter( "local_check" ) != null ) {
			
				localCheck = Integer.parseInt( req.getParameter( "local_check" ) );
			
			}
			
			BizDelegate bizDelegate = new BizDelegate();
			LocalDTO local = bizDelegate.getLocalById( localCheck );
			
			String rutaUpload = req.getSession().getServletContext().getRealPath( "upload_stock_online" );
			
			logger.debug( "rutaUpload:" + rutaUpload );
			
			MultipartRequest multi = new MultipartRequest( req, rutaUpload, 900000000 );
	        
	        ForwardParameters fp = new ForwardParameters();
	        fp.add( req.getParameterMap() );
	        
	        File archivo = multi.getFile( "archivoStockOnline" );
	       
	        if ( !archivo.getName().substring( archivo.getName().length()-4, archivo.getName().length() ).equalsIgnoreCase(".xls") ) {
	        	
	            logger.error( "Existió un error en la carga de actualizacion de la configuracion de stock online. No es un archivo XLS." );
	            res.sendRedirect( cmd_dsp_error +"?mensaje="+Constantes.MSJ_EVENTO_SIN_ARCHIVO+"&PagErr=1" );
	            return;
            
	        }
	        
	        FileInputStream archivoStockOnline = new FileInputStream( archivo ); 
	    
	        logger.debug( "Eliminamos archivo ::::: " +archivo.getName()+ " ...." );
			
	        File directorioDestino = new File( rutaUpload );
			
	        borrar( directorioDestino, archivo );
			
			logger.debug( "Archivo eliminado..." );
			 
	        List archivoFiltrado = filtrarArchivoExcelModoOn( archivoStockOnline );
	        
	        logger.info( "numRows StockOnline Filtrado::::: " + archivoFiltrado.size() );
	        
	        List listaMensajesValidacion = new ArrayList();
	        BizDelegate biz = new BizDelegate();
	        
	        if( archivoFiltrado.size() != 0 ) {
	        	
	        	listaMensajesValidacion = validarExcel( archivoFiltrado, local.getCod_local() );
		    
	        }
	        
	        res.setCharacterEncoding( "UTF-8" );
		    View salida = new View(res);

		    String html = getServletConfig().getInitParameter( "TplFile" );
		    html = path_html + html;

		    TemplateLoader load = new TemplateLoader( html );
		    ITemplate tem = load.getTemplate();
		    IValueSet top = new ValueSet();
		    
		    top.setVariable( "{msj}", "");
		    top.setVariable( "{error}", "");
		    
		    if( listaMensajesValidacion.size() == 0 && archivoFiltrado.size() != 0 ) {
	            
	        	logger.info( "Archivo OK..." );
	        	
	        	biz.getLimpiarTablaStockOnlinePorLocal( local.getId_local() );
		    	
	        	logger.info( "se borraron registros antiguos del local " +local.getNom_local() + " de la tabla BO_STOCK_ONLINE.");
	        	
	        	if ( !biz.setInsertRegistroExcelToBD( archivoFiltrado, local.getId_local() ) ) {
	        		
	        		 logger.error( "Existió un error en la carga de actualizacion de la configuracion de stock online. Archivo erroneo." );
			         res.sendRedirect( cmd_dsp_error +"?mensaje="+Constantes.MSJ_EVENTO_ARCHIVO_EXTENSION+"&PagErr=1" );
			         
			         return;
			    
	        	}
	        	
			    top.setVariable( "{msj}", "Archivo cargado correctamente para local: " + local.getNom_local().replaceAll( "ñ", "&ntilde;" ) + ", <br>los cambios seran efectivos apartir de ma&ntilde;ana" );
	        
		    }
	        else if( archivoFiltrado.size() == 0 ) {
	        		
	        	    top.setVariable( "{msj}", "No se pudo cargar el archivo, favor revisar el campo Estado esten en ON." );
	    	    
	        }
	        else {
	        		
	        		top.setVariable( "{error}", "La planilla tiene los siguientes errores:" );	        	
	        
	        }		      
	        
	        archivo.delete();	    			    
		    
	        top.setDynamicValueSets( "listaMensajesValidacion", listaMensajesValidacion );
		 	top.setVariable( "{loc_"+localCheck+"}", "checked" );
		  
		    top.setVariable( "{hdr_nombre}"	, usr.getNombre() + " " + usr.getApe_paterno() );
			Date now = new Date();
			top.setVariable( "{hdr_fecha}"	, now.toString() );
			
		    String result = tem.toString( top );
	        salida.setHtmlOut( result );
	        
	        salida.Output();
	        
		
 		}catch( Exception e ) {
			
 			trx.rollback();
 			logger.error("Problema al importar archivo XLS : " + e);
		
 		}
        
 		 trx.end();
 		
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
     * @param archivo
     * @param cod_local
     * @return
     */
    private List validarExcel( List archivo, String cod_local ) {

    	List listaMensajesValidacion = new ArrayList();
    	 
    	try {
    		
    		
    		logger.info("validando archivo StockOnline.....");
    		   
    		Iterator it = archivo.iterator();
 
    		while ( it.hasNext() ) {
   	        
   				BOStockONLineDTO stockOnLine = ( BOStockONLineDTO )it.next();
   				
   				String mensajeValidacion = validarRegistro(	
   	   			    		stockOnLine.getSkuProducto(),
   	    		    		stockOnLine.getUnidadDeMedida().toString(),
   	    		    		stockOnLine.getModo(),        		
   	    			       	stockOnLine.getStockMinimo(),
   	    		    		stockOnLine.getEstado(),
   	    			       	cod_local,
   	    			       	stockOnLine.getCodLocal() );
   	    		       
   	    		if( !"".equals( mensajeValidacion.trim() ) ) {
   	    		    	
   	    		        	IValueSet pr = new ValueSet();
   	    		        	pr.setVariable( "{mensaje}", mensajeValidacion );
   	    		        	listaMensajesValidacion.add( pr );
   	    		        	break;
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
     * @param idModo
     * @param stockMinimo
     * @param idEstado
     * @param cod_local
     * @param cell_CodLocal
     * @return
     * @throws BocException
     */
    private String validarRegistro( String cell_Sku, String cell_Media, int idModo, int stockMinimo,
		                           int idEstado, String cod_local, String cell_CodLocal ) throws BocException {
	
	    String msjError = "Error SKU: " + cell_Sku;
		boolean error = false;
		BizDelegate biz = new BizDelegate();
	
		if( !biz.getProductoBySkuUniMed( cell_Sku, cell_Media ) ) {
		
			msjError = msjError + " ; campo Medida : " +cell_Media+ ", No Existe.";
			error = true;
	
		}
	
		if( idModo == -1000 ) {		
		
			msjError = msjError + " ; campo Stock Online";
			error = true;
	    
		}
	
		if( stockMinimo == -1000 ) {
			
			msjError = msjError + " ; campo Stock Mínimo";
			error = true;
	
		}
	
		if( idEstado == -1000 ) {
			
			msjError = msjError + " ; campo Estado";
			error = true;
	
		}
	
		if( !cod_local.equals( cell_CodLocal ) ) {
			
			msjError = " Error Local; campo Cod. Local no corresponde al local seleccionado " +cod_local;
			error = true;
	
		}
	
		if( error == true ) {
		
			return msjError;	
	
		}
	
		return "";
	
    }
	

    /**
     * 
     * @param input
     * @return
     */
	private List filtrarArchivoExcelModoOn( InputStream input ) {

		BOStockONLineDTO stockOnLine = null;
		List ListStockOnLine = new ArrayList();
	    POIFSFileSystem fs;
	  
	    try {
		   
	    	fs = new POIFSFileSystem( input );
		    HSSFWorkbook wb = new HSSFWorkbook( fs );
		    HSSFSheet sheet = wb.getSheetAt( 0 );
		    int numRows = sheet.getLastRowNum();
		    
		    logger.info( "numRows StockOnline::::: " + numRows );
		
		    Iterator rows = sheet.rowIterator();
		   
		    while ( rows.hasNext() ) {
		       
		    	HSSFRow row = ( HSSFRow ) rows.next();
		       
		    	if( row.getRowNum() == 0 )
		            continue;
		
		        if( row.getCell( 8 ).toString().equals( Constantes.CONSTANTE_MODO_ON ) ) {
		    	   
		    	    stockOnLine = new BOStockONLineDTO();
		    	    
		    	    stockOnLine.setCodLocal( String.valueOf( row.getCell( 0 ) ) );
		    	    stockOnLine.setSkuProducto( getSkuProducto( row.getCell( 5 ).toString() ) );
		    	    stockOnLine.setUnidadDeMedida( String.valueOf( row.getCell( 7 ) ) );
		    	    stockOnLine.setModo( getIdModo( row.getCell( 8 ).toString() ) );
			        stockOnLine.setStockMinimo( getStockMinimo( row.getCell( 9 ).toString() ) );
			        stockOnLine.setEstado( getIdEstado( row.getCell( 10 ).toString() ) );
			        stockOnLine.setTotalMaestra( numRows );
			       
			        ListStockOnLine.add( stockOnLine );
			     
		        }
		        
		    }
	   
	    } catch ( Exception e ) {
		    
	    	e.printStackTrace();
		    logger.info( e.toString() );
	   
	    }
		
	    return ListStockOnLine;

	}
	

	private String getSkuProducto(String sku) {
	
		String d = sku;
		
		if( sku.indexOf( '.' ) != -1 ) {
			
			 d = sku.substring( 0, sku.indexOf( "." ) );
		
		}
		
		return d;
	}
	
	private int getStockMinimo(String stockMinimo) {
		
		int sm = 0;
		
		if( stockMinimo.indexOf( '.' ) != -1 ) {
			
			 sm = Integer.parseInt( stockMinimo.substring( 0, stockMinimo.indexOf( "." ) ) );
		
		}else {
			
			sm = Integer.parseInt(stockMinimo);
		}
		
		return sm;
	}

	/**
	 * 
	 * @param modo
	 * @return
	 */
	private int getIdModo( String modo ) {
		
		int modoStockOnline = Constantes.CONSTANTE_MODO_INT_SIN_VALOR;

		if( Constantes.CONSTANTE_MODO_ON.equalsIgnoreCase( modo.trim() ) ) {
			
			modoStockOnline = Constantes.CONSTANTE_MODO_INT_ON;		
		
		}else if( Constantes.CONSTANTE_MODO_OFF.equalsIgnoreCase( modo.trim() ) ) {
			
			modoStockOnline = Constantes.CONSTANTE_MODO_INT_OFF;		
		
		}else {
			
			modoStockOnline = Constantes.CONSTANTE_INT_ERROR;
		}
		
		return modoStockOnline;
	
	}
	
	/**
	 * 
	 * @param estado
	 * @return
	 */
	private int getIdEstado( String estado ) {
		
		int estadoStockOnline = Constantes.CONSTANTE_ESTADO_INT_SIN_VALOR;
			
		estado = eliminarAcentos( estado.trim() );
			
		if( Constantes.CONSTANTE_ESTADO_AUTOMATICO.equalsIgnoreCase( estado ) ) {
				
			estadoStockOnline = Constantes.CONSTANTE_ESTADO_INT_AUTOMATICO;		
			
		}else if( Constantes.CONSTANTE_ESTADO_SEMIAUTOMATICO.equalsIgnoreCase( estado ) ){
				
			estadoStockOnline = Constantes.CONSTANTE_ESTADO_INT_SEMIAUTOMATICO;		
			
		}else{
				
			estadoStockOnline = Constantes.CONSTANTE_INT_ERROR;
			
		}
		
		return estadoStockOnline;
	
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