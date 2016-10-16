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
import cl.bbr.jumbocl.pedidos.dto.ListaGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Entrega los datos para la configuración de Casos
 * 
 * @author imoyano
 */
public class ViewConfigListasEspeciales extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewConfigListasEspeciales Execute");

	    //Objetos para pintar la pantalla
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
        
        String msj = "";
        
        if ( req.getParameter("mensaje") != null ) {
            msj = req.getParameter("mensaje");
        }
        
		BizDelegate bizDelegate = new BizDelegate();        
        
		// ---- Grupos de Listado ----
		List gruposListado = bizDelegate.getGruposListado();
		ArrayList lGrupos = new ArrayList();
		
		for (int i = 0; i < gruposListado.size(); i++) {			
			IValueSet fila = new ValueSet();
			ListaGrupoDTO lg = (ListaGrupoDTO) gruposListado.get(i);
			fila.setVariable("{id_lista_grupo}",String.valueOf(lg.getIdListaGrupo()));			
			fila.setVariable("{nombre_lista_grupo}",lg.getNombre());
			fila.setVariable("{nombre_tipo_lista_grupo}", lg.getTipo().getDescripcion());
            fila.setVariable("{id_tipo_lista_grupo}",String.valueOf(lg.getTipo().getIdListaTipoGrupo()));
			if (lg.getActivado().equals("1")) {
			    fila.setVariable("{activado}","Si");
			} else {
			    fila.setVariable("{activado}","No");
			}
			fila.setVariable("{est_activado}",lg.getActivado());
			lGrupos.add(fila);
		}
		if (lGrupos.size() > 0) {
		    top.setVariable("{msj_lista_grupo}","");
		} else {
		    top.setVariable("{msj_lista_grupo}","No existen grupos de lista.");
		}

        // ---- Listas Especiales ----
        long idGrupoLista = 0;
        List listasEspeciales = bizDelegate.clienteListasEspeciales(idGrupoLista);
        ArrayList listas = new ArrayList();
        
        for (int i = 0; i < listasEspeciales.size(); i++) {            
            IValueSet fila = new ValueSet();
            UltimasComprasDTO lista = (UltimasComprasDTO) listasEspeciales.get(i);
            fila.setVariable("{id_lista}",String.valueOf(lista.getId()));          
            fila.setVariable("{nombre_lista}",lista.getNombre());
            fila.setVariable("{id_grupo}",String.valueOf(lista.getGrupoLista().getIdListaGrupo()));
            fila.setVariable("{nombre_grupo}",lista.getGrupoLista().getNombre());
            listas.add(fila);
        }
        if (listas.size() > 0) {
            top.setVariable("{msj_listas}","");
        } else {
            top.setVariable("{msj_listas}","No existen listas.");
        }

        // ---- Tipos de Grupos de Listado Activas ----
        List tiposGruposListadoActivos = bizDelegate.getTiposGruposListadoActivos();
        ArrayList lTiposGruposActivos = new ArrayList();
        
        for (int i = 0; i < tiposGruposListadoActivos.size(); i++) {            
            IValueSet fila = new ValueSet();
            ListaTipoGrupoDTO ltg = (ListaTipoGrupoDTO) tiposGruposListadoActivos.get(i);
            fila.setVariable("{id_tipo_lista_grupo}",String.valueOf(ltg.getIdListaTipoGrupo()));          
            fila.setVariable("{nombre_tipo_lista_grupo}",ltg.getDescripcion());
            lTiposGruposActivos.add(fila);
        }	
        
        List tiposGruposListado = bizDelegate.getTiposGruposListado();
        ArrayList lTiposGrupos = new ArrayList();
        
        for (int i = 0; i < tiposGruposListado.size(); i++) {            
            IValueSet fila = new ValueSet();
            ListaTipoGrupoDTO ltg = (ListaTipoGrupoDTO) tiposGruposListado.get(i);
            fila.setVariable("{id_tipo_lista_grupo}",String.valueOf(ltg.getIdListaTipoGrupo()));          
            fila.setVariable("{nombre_tipo_lista_grupo}",ltg.getDescripcion());
            if ("S".equalsIgnoreCase(ltg.getActivado())) {
                fila.setVariable("{activado}","Si");
            } else {
                fila.setVariable("{activado}","No");
            }
            fila.setVariable("{est_activado}",ltg.getActivado());
            lTiposGrupos.add(fila);
        }   
        if (lTiposGrupos.size() > 0) {
            top.setVariable("{msj_marcos}","");
        } else {
            top.setVariable("{msj_marcos}","No existen marcos.");
        }

        top.setDynamicValueSets("LIST_LISTAS", listas);
        top.setDynamicValueSets("LIST_GRUPO", lGrupos);
        top.setDynamicValueSets("LIST_TIPO_GRUPO", lTiposGrupos);
        top.setDynamicValueSets("LIST_TIPO_GRUPO_ACTIVAS", lTiposGruposActivos);
        
		
		top.setVariable("{cantGruposLista}", String.valueOf(lGrupos.size()));
        top.setVariable("{cantListas}", String.valueOf(listas.size()));
        top.setVariable("{msj}", msj);
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		logger.debug("Fin ViewConfigListasEspeciales Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
