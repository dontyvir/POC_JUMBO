package cl.bbr.boc.view;

import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * 
 * @author jolazogu
 *
 */
public class ViewStockOnlineResumenSemiautomaticoForm extends Command {
	
    private final static long serialVersionUID = 1;   
  
    
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		  
    	res.setCharacterEncoding( "UTF-8" );
	      
    	View salida = new View( res );
	      		 
	    BizDelegate biz = new BizDelegate();	      
	      
	    String html = getServletConfig().getInitParameter( "TplFile" );
	    html = path_html + html;
	
	    TemplateLoader load = new TemplateLoader( html );
	    ITemplate tem = load.getTemplate();
	    IValueSet top = new ValueSet();
      
	    int localCheck = 5;
	    
	    if( req.getParameter( "local_check" ) != null ) {
	    	
	    	localCheck = Integer.parseInt( req.getParameter( "local_check" ) );
	      
	    }
	      
	    LocalDTO local = biz.getLocalById( localCheck );

	    long[] cantidadesActualmente = { 0, 0, 0, 0 };
		long[] cantidadesDespues = { 0, 0, 0, 0, 0 };
	      
	    logger.info("Consultando cantidades de publicados, despublicados y maestra del " +local.getNom_local());
	    
	    cantidadesActualmente = biz.cantidadDeProductosActualmente( local.getId_local() );
	    cantidadesDespues = biz.cantidadDeProductosTendranCambios( local.getId_local() );
	    
	    top.setVariable( "{ejecutaSemiautomatico}", "" );
	      
	    if( cantidadesDespues[ 4 ] == 0 ) {
	    	  
	    	top.setVariable( "{ejecutaSemiautomatico}", "No existe informaci&oacute;n SAP para el local: " +local.getNom_local() );
	    	top.setVariable( "{btnEjecutaDetalle}", "none" );
	    	top.setVariable( "{resumen}", "none" );
	      
	    } else if ( cantidadesDespues[ 4 ] == 1 ) {	    	  
	    	
	    	top.setVariable( "{cambioConStock}", "+" + formatoNumero( cantidadesDespues[ 0 ] ) + ", -" + formatoNumero( cantidadesDespues[ 2 ] ) );
	    	top.setVariable( "{cambioSinStock}", "+" + formatoNumero( cantidadesDespues[ 1 ] ) + ", -" + formatoNumero( cantidadesDespues[ 3 ] ) );
	    		
	    	top.setVariable( "{btnEjecutaDetalle}", "block" );
	    	top.setVariable( "{resumen}", "block" );

	    }
	      
	    top.setVariable( "{loc_" +localCheck+ "}", "checked" );
	    
	    top.setVariable( "{loc}", String.valueOf( localCheck ) );
	      
	    top.setVariable( "{publicadosConStock}", formatoNumero(cantidadesActualmente[ 1 ] ) );
	    top.setVariable( "{publicadosSinStock}", formatoNumero( cantidadesActualmente[ 0 ] ) );
	    top.setVariable( "{despublicados}", formatoNumero( cantidadesActualmente[ 2 ] ) );
        top.setVariable( "{maestra}", formatoNumero( biz.getTotalMaestra( local.getId_local() ) ) );
      
        String result = tem.toString( top );
        salida.setHtmlOut( result );
        salida.Output();

    }
    
    public static String formatoNumero( long num ) {
		
		NumberFormat df = NumberFormat.getInstance();
		df.setMaximumFractionDigits(2);		
		return df.format( num );

	}	
    
}
