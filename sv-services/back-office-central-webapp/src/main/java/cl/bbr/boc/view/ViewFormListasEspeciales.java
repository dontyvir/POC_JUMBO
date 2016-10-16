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
import cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Entrega los datos para la configuración de Casos
 * 
 * @author imoyano
 */
public class ViewFormListasEspeciales extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewFormListasEspeciales Execute");

	    //Objetos para pintar la pantalla
	    View salida = new View(res);
		String html = "";
        String mensaje = "";
        String cargaListas = "";
        String destino = "NEW";
        long idLista = 0;
	    
        if (req.getParameter("to") != null) {
            destino = req.getParameter("to").toString();
        }
        if (destino.equalsIgnoreCase("NEW")) {
            html = getServletConfig().getInitParameter("FileAdd");
        } else if (destino.equalsIgnoreCase("VIE")) {
            html = getServletConfig().getInitParameter("FileView");
        } else {
            html = getServletConfig().getInitParameter("FileMod");
        }
        html = path_html + html;
        logger.debug("Template: " + html);  
        
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
        
        if (req.getParameter("mensaje") != null) {
            mensaje = req.getParameter("mensaje").toString();
        }        
        if (req.getParameter("cargo_listas") != null) {
            cargaListas = req.getParameter("cargo_listas").toString();
        }
        if (req.getParameter("id_lista") != null) {
            idLista = Long.parseLong( req.getParameter("id_lista").toString() );
        }
        
        if ( destino.equalsIgnoreCase("MOD") ) {
            
            BizDelegate biz = new BizDelegate();
            List gruposAsociadosLista = biz.getGruposAsociadosLista(idLista);
            UltimasComprasDTO lista = biz.getListaById(idLista);
            ArrayList gruposAsociados = new ArrayList();
            
            for (int i = 0; i < gruposAsociadosLista.size(); i++) {            
                IValueSet fila = new ValueSet();
                ListaGrupoDTO lg = (ListaGrupoDTO) gruposAsociadosLista.get(i);
                fila.setVariable("{id_lista_grupo}",String.valueOf(lg.getIdListaGrupo()));          
                fila.setVariable("{nombre_lista_grupo}",lg.getNombre());
                gruposAsociados.add(fila);
            }
            
            List gruposNoAsociadosLista = biz.getGruposNoAsociadosLista(idLista);
            ArrayList gruposNoAsociados = new ArrayList();
            
            for (int i = 0; i < gruposNoAsociadosLista.size(); i++) {            
                IValueSet fila = new ValueSet();
                ListaGrupoDTO lg = (ListaGrupoDTO) gruposNoAsociadosLista.get(i);
                fila.setVariable("{id_lista_grupo}",String.valueOf(lg.getIdListaGrupo()));          
                fila.setVariable("{nombre_lista_grupo}",lg.getNombre());
                gruposNoAsociados.add(fila);
            }
            
            top.setDynamicValueSets("LIST_ASOCIADAS", gruposAsociados);
            top.setDynamicValueSets("LIST_NO_ASOCIADAS", gruposNoAsociados);
            top.setVariable("{id_lista}",    ""+idLista);
            top.setVariable("{name_lista}",    lista.getNombre());
            
        } else if (destino.equalsIgnoreCase("VIE")) {
            BizDelegate biz = new BizDelegate();
            UltimasComprasDTO lista = biz.getListaById(idLista);
            
            List productos = biz.listaDeProductosByLista(idLista);
            ArrayList prds = new ArrayList();
            for (int i=0; i < productos.size(); i++) {
                IValueSet fila = new ValueSet();
                UltimasComprasDTO producto = (UltimasComprasDTO) productos.get(i);
                fila.setVariable("{id_producto}",String.valueOf(producto.getId()));          
                fila.setVariable("{descripcion}",producto.getNombre());
                fila.setVariable("{cantidad}",String.valueOf(producto.getUnidades()));
                fila.setVariable("{unidad}",producto.getTipo());
                prds.add(fila);
            }            
            top.setDynamicValueSets("PRODUCTOS", prds);
            top.setVariable("{name_lista}",    lista.getNombre());
        }
	    
        top.setVariable("{mensaje}",         mensaje);
        top.setVariable("{cargo_listas}",    cargaListas);
        
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		logger.debug("Fin ViewFormListasEspeciales Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
