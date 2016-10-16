package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
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
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Muestra el detalle de un caso
 * 
 * @author imoyano
 */
public class ViewDetalleCaso extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewDetalleCaso Execute");
	    
	    //Variables
	    long idCaso = 0;
	    int pagina = 0;
	    
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//Sacamos la info de la pagina
		if (req.getParameter("id_caso") != null) {
		    idCaso = Long.parseLong(req.getParameter("id_caso").toString());
		}
		if (req.getParameter("pagina") != null) {
		    pagina = Integer.parseInt(req.getParameter("pagina").toString());
		}
		
		BizDelegate bizDelegate = new BizDelegate();
		
		// ----- Datos del caso ----------
		CasoDTO caso = bizDelegate.getCasoByIdCaso(idCaso);
		
		// ---- Productos a enviar ----
		List prodEnviar = bizDelegate.getProductosByCasoAndTipo(idCaso,"E");
		ArrayList pEnviar = new ArrayList();
		
		for (int i = 0; i < prodEnviar.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCasoDTO prod = (ProductoCasoDTO)prodEnviar.get(i);
			fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
			
			fila.setVariable("{producto}",prod.getPpDescripcion());
			fila.setVariable("{cantidad}",prod.getPpCantidad());			
			fila.setVariable("{unidad}",prod.getPpUnidad());
			fila.setVariable("{comentario_boc}",prod.getComentarioBOC());
			
			fila.setVariable("{ps_producto}",prod.getPsDescripcion());
			fila.setVariable("{ps_cantidad}",prod.getPsCantidad());			
			fila.setVariable("{ps_unidad}",prod.getPsUnidad());
			fila.setVariable("{comentario_bol}",prod.getComentarioBOL());
			
			fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
			fila.setVariable("{responsable}",prod.getResponsable().getNombre());
			
			fila.setVariable("{pickeador}",prod.getPickeador());
			
			fila.setVariable("{precio}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(prod.getPrecio()))));
			
			pEnviar.add(fila);
		}
		if (pEnviar.size() > 0) {
		    top.setVariable("{msj_prods_enviar}","");
		} else {
		    top.setVariable("{msj_prods_enviar}","No existen productos para enviar");
		}
		
		// ---- Productos a Retirar ----
		List prodRetirar = bizDelegate.getProductosByCasoAndTipo(idCaso,"R");
		ArrayList pRetirar = new ArrayList();
		
		for (int i = 0; i < prodRetirar.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCasoDTO prod = (ProductoCasoDTO)prodRetirar.get(i);
			fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
			
			fila.setVariable("{producto}",prod.getPpDescripcion());
			fila.setVariable("{cantidad}",prod.getPpCantidad());			
			fila.setVariable("{unidad}",prod.getPpUnidad());
			fila.setVariable("{comentario_boc}",prod.getComentarioBOC());
			
			fila.setVariable("{ps_producto}",prod.getPsDescripcion());
			fila.setVariable("{ps_cantidad}",prod.getPsCantidad());			
			fila.setVariable("{ps_unidad}",prod.getPsUnidad());
			fila.setVariable("{comentario_bol}",prod.getComentarioBOL());
			
			fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
			fila.setVariable("{responsable}",prod.getResponsable().getNombre());
			fila.setVariable("{pickeador}",prod.getPickeador());
			
			fila.setVariable("{precio}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(prod.getPrecio()))));
			
			pRetirar.add(fila);
		}
		if (pRetirar.size() > 0) {
		    top.setVariable("{msj_prods_retirar}","");
		} else {
		    top.setVariable("{msj_prods_retirar}","No existen productos para retirar");
		}
		
		// ---- Productos a Especiales (Documentos) ----
		List prodDocumentos = bizDelegate.getProductosByCasoAndTipo(idCaso,"D");
		ArrayList pDocumentos = new ArrayList();
		
		for (int i = 0; i < prodDocumentos.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCasoDTO prod = (ProductoCasoDTO)prodDocumentos.get(i);
			fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
			
			fila.setVariable("{producto}",prod.getPpDescripcion());
			fila.setVariable("{comentario_boc}",prod.getComentarioBOC());
			
			fila.setVariable("{comentario_bol}",prod.getComentarioBOL());
			
			fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
			fila.setVariable("{responsable}",prod.getResponsable().getNombre());
			pDocumentos.add(fila);
		}
		if (pDocumentos.size() > 0) {
		    top.setVariable("{msj_prods_docs}","");
		} else {
		    top.setVariable("{msj_prods_docs}","No existen documentos");
		}
		
		//---- Envio de dinero ----
        List prodEnvioDinero = bizDelegate.getProductosByCasoAndTipo(idCaso,"P");
        ArrayList pEnvioDinero = new ArrayList();
        
        for (int i = 0; i < prodEnvioDinero.size(); i++) {          
            IValueSet fila = new ValueSet();
            ProductoCasoDTO prod = (ProductoCasoDTO)prodEnvioDinero.get(i);
            fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
            
            fila.setVariable("{producto}",prod.getPpDescripcion());
            fila.setVariable("{cantidad}",prod.getPpCantidad());            
            fila.setVariable("{unidad}",prod.getPpUnidad());
            fila.setVariable("{comentario_boc}",prod.getComentarioBOC());
            
            fila.setVariable("{ps_producto}",prod.getPsDescripcion());
            fila.setVariable("{ps_cantidad}",prod.getPsCantidad());         
            fila.setVariable("{ps_unidad}",prod.getPsUnidad());
            fila.setVariable("{comentario_bol}",prod.getComentarioBOL());
            
            fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
            fila.setVariable("{responsable}",prod.getResponsable().getNombre());
            
            fila.setVariable("{pickeador}",prod.getPickeador());
            
            fila.setVariable("{precio}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(prod.getPrecio()))));
            
            pEnvioDinero.add(fila);
        }
        if (pEnvioDinero.size() > 0) {
            top.setVariable("{msj_envio_dinero}","");
        } else {
            top.setVariable("{msj_envio_dinero}","No existen envio de dinero");
        }
        
		// Log del caso
		List logCaso = bizDelegate.getLogCaso(idCaso);
		ArrayList pLogs = new ArrayList();		
		for (int i = 0; i < logCaso.size(); i++) {			
			IValueSet fila = new ValueSet();
			LogCasosDTO log = (LogCasosDTO)logCaso.get(i);
			fila.setVariable("{fecha}",Formatos.frmFechaHora(log.getFecha()));
			fila.setVariable("{usuario}",log.getUsuario());
			fila.setVariable("{estado}",log.getEstado().getNombre());
			fila.setVariable("{detalle}",log.getDescripcion());			
			pLogs.add(fila);
		}
		if (pLogs.size() > 0) {
		    top.setVariable("{msj_log}","");
		} else {
		    top.setVariable("{msj_log}","No existen registros");
		}
	    
		//Informacion para la pagina
	    top.setVariable("{mensaje1}", "");
	    top.setVariable("{mns}", "");
	    top.setVariable("{id_caso}",String.valueOf(caso.getIdCaso()));
	    top.setVariable("{id_pedido}",String.valueOf(caso.getIdPedido()));
	    top.setVariable("{estado}",caso.getEstado().getNombre());
	    top.setVariable("{direccion}",caso.getDireccion());
	    top.setVariable("{comuna}",caso.getComuna());
	    top.setVariable("{fecha_pedido}",Formatos.frmFecha(caso.getFcPedido()));
	    top.setVariable("{fecha_despacho}",Formatos.frmFecha(caso.getFcDespacho()));
	    top.setVariable("{fecha_ingreso_caso}", CasosUtil.frmFechaHoraSinSegundos(caso.getFcCreacionCaso()));
	    top.setVariable("{cli_rut}",String.valueOf(caso.getCliRut()));
	    top.setVariable("{cli_dv}",caso.getCliDv());
	    top.setVariable("{cli_nombre}",caso.getCliNombre());
	    top.setVariable("{cli_cod1}",caso.getCliFonoCod1());
	    top.setVariable("{cli_fono1}",caso.getCliFonoNum1());
	    top.setVariable("{cli_cod2}",caso.getCliFonoCod2());
	    top.setVariable("{cli_fono2}",caso.getCliFonoNum2());
	    top.setVariable("{cli_cod3}",caso.getCliFonoCod3());
	    top.setVariable("{cli_fono3}",caso.getCliFonoNum3());
	    top.setVariable("{local}",caso.getLocal().getNombre());
	    top.setVariable("{nro_compras}",String.valueOf(bizDelegate.getNroComprasByCliente(caso.getCliRut())));
	    top.setVariable("{nro_casos}",String.valueOf(bizDelegate.getNroCasosByCliente(caso.getCliRut())));
	    top.setVariable("{teleoperador}",caso.getDescripcionCanalCompra());
	    top.setVariable("{fc_resolucion}",Formatos.frmFecha(caso.getFcCompromisoSolucion()));
	    top.setVariable("{jornada_resolucion}",caso.getJornada().getDescripcion());
	    
	    //(13/10/2014) Req. Mejoras al Monitor de Casos (Extras) - NSepulveda.
	    logger.debug("[ViewDetalleCaso.java] [caso.getIndicaciones()] String Indicaciones BD sin replace() : ["+caso.getIndicaciones()+"]");
	    
	    if ( caso.getIndicaciones() != null && caso.getIndicaciones().length() > 0 ) {
            top.setVariable("{indicaciones}",caso.getIndicaciones().replaceAll("(\\r\\n|\\n)", "<br />"));
        } else {
        	top.setVariable("{indicaciones}","");
        }
	    logger.debug("[ViewDetalleCaso.java] [caso.getIndicaciones()] String Indicaciones BD despues del replace() : ["+top.getVariable("{indicaciones}")+"]");
	    
	    top.setVariable("{fc_resolucion_final}",Formatos.frmFecha(caso.getFcCasoVerificado()));
	    top.setVariable("{fecha_entrega}",CasosUtil.frmFechaHoraSinSegundos(caso.getFcHoraEntrega()));
        
        if ( caso.getComentarioCallCenter() != null && caso.getComentarioCallCenter().length() > 0 ) {
            top.setVariable("{msj_call_center}",caso.getComentarioCallCenter().replaceAll("\r\n","<br/>"));
        } else {
            top.setVariable("{msj_call_center}","");
        }
	    
	    if (caso.isEscalamiento()) {
	        top.setVariable("{color_escalamiento}","#FF0000");
	        top.setVariable("{escalamiento}","Si");
	        
        } else {
            top.setVariable("{color_escalamiento}","#333333");
	        top.setVariable("{escalamiento}","No");
	        
        }
	    
	    if (caso.getEditUsuario() != null) {
	        top.setVariable("{usuario_edit}", caso.getEditUsuario());
	    } else {
	        top.setVariable("{usuario_edit}", " ");
	    }
	    if (caso.getCreadorUsuario() != null) {
	        top.setVariable("{usuario_creador}", caso.getCreadorUsuario());
	    } else {
	        top.setVariable("{usuario_creador}", " ");
	    }
	    top.setVariable("{pagina}",String.valueOf(pagina));
	    
	    top.setDynamicValueSets("PRDS_ENVIAR", pEnviar);
	    top.setDynamicValueSets("PRDS_RETIRAR", pRetirar);
	    top.setDynamicValueSets("PRDS_DOCS", pDocumentos);
	    top.setDynamicValueSets("LOG_CASO", pLogs);
        top.setDynamicValueSets("PRDS_ENVIO_DINERO", pEnvioDinero);
	    		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
				
		logger.debug("Fin ViewDetalleCaso Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
