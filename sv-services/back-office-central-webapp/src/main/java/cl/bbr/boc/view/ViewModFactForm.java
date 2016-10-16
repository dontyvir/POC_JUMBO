package cl.bbr.boc.view;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.FacturaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * formulario que permite ingresar datos para modificar un documento de pago de un pedido
 * @author BBRI
 */
public class ViewModFactForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String paramId_pedido = "";
		String mensaje 		  = "";
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
		
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		
		if ( req.getParameter("id_pedido") == null ){
			logger.error("Parámetro id_pedido es obligatorio");
			throw new ParametroObligatorioException("id_pedido es null");
		}	
		
		paramId_pedido = req.getParameter("id_pedido");
		long id_pedido = Long.parseLong(paramId_pedido);

		if(req.getParameter("mensaje")!=null)
			mensaje = req.getParameter("mensaje");		
		
		
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//datos asociados al Medio de Pago
		BizDelegate bizDelegate = new BizDelegate();
		List lst_fact =  bizDelegate.getFacturasByIdPedido(id_pedido);
		logger.debug("lst_fact.size(): "+lst_fact.size());
		if(lst_fact.size()>0){
			for(int i=0; i<lst_fact.size(); i++){
				FacturaDTO fact = (FacturaDTO)lst_fact.get(i);	
				if(fact.getTipo_doc().equals(Constantes.TIPO_DOC_FACTURA)){
					top.setVariable("{razon}"		, fact.getRazon());
					top.setVariable("{direccion}"	, fact.getDireccion());
					top.setVariable("{rut}"			, String.valueOf(fact.getRut()));
					top.setVariable("{dv}"			, fact.getDv());
					logger.debug("dv:"+fact.getDv());
					top.setVariable("{fono}"	    , fact.getFono());
					top.setVariable("{giro}"  	    , fact.getGiro());
					top.setVariable("{ciudad}" 	    , fact.getCiudad());
					top.setVariable("{comuna}" 	    , fact.getComuna());								
					top.setVariable("{tipo_doc}"	, fact.getTipo_doc());			
					top.setVariable("{mensaje_fact}", "");
					top.setVariable("{mensaje_bol}", "");
					top.setVariable("{check_1}"," ");
					top.setVariable("{check_2}","checked");
					top.setVariable("{height_fac}","110px");
					top.setVariable("{height_bol}","120px");				
				}else{				
					top.setVariable("{tipo_doc}"	, fact.getTipo_doc());
					top.setVariable("{razon}"		, "");
					top.setVariable("{direccion}"	, "");
					top.setVariable("{rut}"			, "");
					top.setVariable("{dv}"			, "");
					logger.debug("dv:"+fact.getDv());
					top.setVariable("{fono}"	    , "");
					top.setVariable("{giro}"  	    , "");
					top.setVariable("{ciudad}" 	    , "");
					top.setVariable("{comuna}" 	    , "");				
					top.setVariable("{tipo_doc}"	, "");
					top.setVariable("{mensaje_fact}", "");
					top.setVariable("{mensaje_bol}", "");
					top.setVariable("{check_1}","checked");
					top.setVariable("{check_2}"," ");
					top.setVariable("{height_fac}","120px");
					top.setVariable("{height_bol}","110px");								
				}	
			}		
		}
		else{			
			top.setVariable("{razon}"	, "");
			top.setVariable("{direccion}"	, "");
			top.setVariable("{rut}"	, "");
			top.setVariable("{dv}"	, "");			
			top.setVariable("{fono}"	, "");
			top.setVariable("{giro}"	, "");
			top.setVariable("{id_factura}"	, "");
			top.setVariable("{num_doc}" 	, "");
			top.setVariable("{tipo_doc}"	, "");	
			top.setVariable("{ciudad}" 	    , "");
			top.setVariable("{comuna}" 	    , "");	
			top.setVariable("{mensaje_fact}"	, "no existen facturas asociadas");			
			top.setVariable("{mensaje_bol}"	, "no existen boletas asociadas");			
			top.setVariable("{mensaje_bol}"	, "no existen boletas asociadas");			
		}
				
		top.setVariable("{id_pedido}"		, paramId_pedido);
		
		if(req.getParameter("mensaje")!=null)
			top.setVariable("{mensaje}"		, mensaje);
		else
			top.setVariable("{mensaje}"		, mensaje);
				
		
		String result = tem.toString(top);	
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}