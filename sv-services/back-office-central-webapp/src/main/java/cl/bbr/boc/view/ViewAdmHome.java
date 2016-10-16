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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.CriterioCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.LogCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCarruselDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra listado para el monitor de casos
 * 
 * @author imoyano
 */
public class ViewAdmHome extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewAdmHome Execute");
	    
	    //Variables
	    String mensaje    = "";
	    String fcInicio   = "";
	    String fcTermino  = "";
	    String fcCreacion = "";
        String fcLogCarrusel = "";
	    
	    int pag 			= 1;
		int regsperpage 	= 10;

		View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;

	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

        if (req.getParameter("msje") != null) {
		    mensaje = req.getParameter("msje").toString();
	    }
	    if (req.getParameter("fc_ini") != null) {
	        fcInicio = req.getParameter("fc_ini").toString();
	    }
        if (req.getParameter("fc_fin") != null) {
            fcTermino = req.getParameter("fc_fin").toString();
        }
        if (req.getParameter("fc_creacion") != null) {
            fcCreacion = req.getParameter("fc_creacion").toString();
        }
        if (req.getParameter("fc_log_carrusel") != null) {
            fcLogCarrusel = req.getParameter("fc_log_carrusel").toString();
        }

        /*** INI - LISTADO PRODUCTOS CARRUSEL ***/
	    //Paginacion
	    if (req.getParameter("pagina") != null) {
			pag = Integer.parseInt(req.getParameter("pagina"));
			logger.debug("pagina: " + req.getParameter("pagina"));
		} else {
			pag = 1; // por defecto mostramos la página 1
		}

        BizDelegate biz = new BizDelegate();
        CriterioCarruselDTO criterio = new CriterioCarruselDTO(0,Formatos.formatFecha(fcInicio),Formatos.formatFecha(fcTermino),Formatos.formatFecha(fcCreacion), pag, regsperpage);
		List productos = biz.getProductosCarruselPorCriterio(criterio);
		ArrayList datos = new ArrayList();
		for (int i = 0; i < productos.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCarruselDTO prod = (ProductoCarruselDTO)productos.get(i);

//+20120322coh
			fila.setVariable("{id_producto_carrusel}" , String.valueOf(prod.getIdProductoCarrusel()));
            fila.setVariable("{descripcion}" , prod.getDescripcion());
            fila.setVariable("{sap}" , prod.getIdCodigoSAP());
            fila.setVariable("{tipo_producto}" , prod.getTipoProducto());
            fila.setVariable("{precio}" , prod.getDescPrecio());
            fila.setVariable("{fecha_inicio}" , Formatos.frmFecha(prod.getFcInicio()));
            fila.setVariable("{fecha_termino}" , Formatos.frmFecha(prod.getFcTermino()));
            fila.setVariable("{estado}" , prod.getEstado());
/*
			fila.setVariable("{id_producto_carrusel}" , String.valueOf(prod.getIdProductoCarrusel()));
            fila.setVariable("{nombre}" , prod.getNombre());
            fila.setVariable("{descripcion}" , prod.getDescripcion());
            fila.setVariable("{precio}" , prod.getDescPrecio());
            fila.setVariable("{fecha_inicio}" , Formatos.frmFecha(prod.getFcInicio()));
            fila.setVariable("{fecha_termino}" , Formatos.frmFecha(prod.getFcTermino()));
            fila.setVariable("{fecha_creacion}" , Formatos.frmFechaHoraSinSegundos(prod.getFcCreacion()));
            if ("S".equalsIgnoreCase(prod.getConCriterio())) {
                fila.setVariable("{con_criterio}" , "Si");
            } else {            
                fila.setVariable("{con_criterio}" , "No");
            }			
*/
//-20120322coh
            datos.add(fila);			
		}
		
		if (productos.size() > 0 ) {
			top.setVariable("{msj_carrusel}","");
		} else {
			top.setVariable("{msj_carrusel}","No hay productos para mostrar");
		}		
		
		// Paginador para la pagina
		ArrayList pags = new ArrayList();
		double totalRegistros = biz.getCountProductosCarruselPorCriterio(criterio);

		double total_pag = (double) Math.ceil( totalRegistros / regsperpage );
		if (total_pag == 0) {
			total_pag = 1;
		}
		for (int i = 1; i <= total_pag; i++) {
			IValueSet fila = new ValueSet();
			fila.setVariable("{pag}", String.valueOf(i));
			if (i == pag) {
				fila.setVariable("{sel}", "selected");
			} else {
				fila.setVariable("{sel}", "");
			}
			pags.add(fila);
		}	
		//anterior y siguiente
		if( pag > 1) {
	    	int anterior = pag-1;
	    	top.setVariable("{anterior_label}","<< anterior");
	    	top.setVariable("{anterior}",String.valueOf(anterior));
	    } else if ( pag == 1) {
	    	top.setVariable("{anterior_label}","");
	    }	    
	    if (pag < total_pag) {
	    	int siguiente = pag+1;
	    	top.setVariable("{siguiente_label}","siguiente >>");
	    	top.setVariable("{siguiente}",String.valueOf(siguiente));
	    } else {
	    	top.setVariable("{siguiente_label}","");
        }
        /*** FIN - LISTADO PRODUCTOS CARRUSEL ***/
        
        
        /*** INI - LOG CARRUSEL ***/        
        List logCarrusel = biz.getLogCarruselByFecha(Formatos.formatFecha(fcLogCarrusel));        
        ArrayList datosLogCarrusel = new ArrayList();
        for (int i = 0; i < logCarrusel.size(); i++) {            
            IValueSet fila = new ValueSet();
            LogCarruselDTO log = (LogCarruselDTO) logCarrusel.get(i);
            fila.setVariable("{usuario}" , log.getUsuario());
            fila.setVariable("{descripcion}" , log.getLog());
            fila.setVariable("{fecha}" , Formatos.frmFechaHoraSinSegundos(log.getFecha()));
            datosLogCarrusel.add(fila);            
        }
        if (datosLogCarrusel.size() > 0 ) {
            top.setVariable("{msj_log_carrusel}","");
        } else {
            top.setVariable("{msj_log_carrusel}","No hay log para esta fecha");
        }
        /*** FIN - LOG CARRUSEL ***/
		
		//Informacion para la pagina
	    top.setDynamicValueSets("PAGINAS", pags);
		top.setDynamicValueSets("PRODUCTOS", datos);
        top.setDynamicValueSets("LOG_CARRUSEL", datosLogCarrusel);        

		top.setVariable("{msj}", mensaje);
        
        top.setVariable("{fc_ini}", fcInicio);
        top.setVariable("{fc_fin}", fcTermino);
        top.setVariable("{fc_creacion}", fcCreacion);
        top.setVariable("{fc_log_carrusel}", fcLogCarrusel);
        
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());		
		
		logger.debug("Fin ViewAdmHome Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
