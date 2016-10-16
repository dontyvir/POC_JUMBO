package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra listado de op's pendientes de asignar ruta
 * 
 * @author imoyano
 */
public class ViewMonOpPendientes extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewMonOpPendientes Execute");
	    
        String fechaDespacho = "";
        long idZona     = -1;
        long idPedido   = -1;
        long idLocal   = -1;
        String campoCliente = "";
        String campoClienteSel = "";
        String clienteRut = "";
        String clienteApellido = "";
        String tipoPedido = ""; //origen
        String reprogramada = "";
        int pag             = 1;
        String mensajeError = "";
        String mensaje = "";
        String horasJor = ""; 
        
        //Objetos para pintar la pantalla
        View salida = new View(res);
        String html = getServletConfig().getInitParameter("TplFile");       
        html = path_html + html;
        logger.debug("Template: " + html);      
        
        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();
        
        //Sacamos la info de la pagina
        if (req.getParameter("fecha_despacho") != null && !"".equalsIgnoreCase(req.getParameter("fecha_despacho"))) {
            fechaDespacho = req.getParameter("fecha_despacho").toString();
        }
        if (req.getParameter("horas") != null) {
            horasJor = req.getParameter("horas");           
        }
        if (req.getParameter("id_zona") != null && !"".equalsIgnoreCase(req.getParameter("id_zona"))) {
            idZona = Long.parseLong(req.getParameter("id_zona").toString());
        }
        if (req.getParameter("id_local") != null && !"".equalsIgnoreCase(req.getParameter("id_local"))) {
            idLocal = Long.parseLong(req.getParameter("id_local").toString());
        }
        if (req.getParameter("id_pedido") != null && !"".equalsIgnoreCase(req.getParameter("id_pedido"))) {
            idPedido = Long.parseLong(req.getParameter("id_pedido").toString());
        }
        if (req.getParameter("tipo_pedido") != null && !"".equalsIgnoreCase(req.getParameter("tipo_pedido"))) {
            tipoPedido = req.getParameter("tipo_pedido").toString();
        }
        if (req.getParameter("reprogramada") != null && !"".equalsIgnoreCase(req.getParameter("reprogramada"))) {
            reprogramada = req.getParameter("reprogramada").toString();
        }
        if (req.getParameter("mensaje_error") != null && !"".equalsIgnoreCase(req.getParameter("mensaje_error"))) {
            mensajeError = req.getParameter("mensaje_error").toString();
        }
        if (req.getParameter("mensaje") != null && !"".equalsIgnoreCase(req.getParameter("mensaje"))) {
            mensaje = req.getParameter("mensaje").toString();
        }
        
        top.setVariable("{check_1}","checked");
        top.setVariable("{check_2}","");
        if (req.getParameter("campo_cliente_sel") != null && !"".equalsIgnoreCase(req.getParameter("campo_cliente_sel")) && req.getParameter("campo_cliente") != null && !"".equalsIgnoreCase(req.getParameter("campo_cliente")) ) {
            campoCliente = req.getParameter("campo_cliente").toString();
            campoClienteSel = req.getParameter("campo_cliente_sel").toString();
            if (req.getParameter("campo_cliente_sel").toString().equalsIgnoreCase("APE")) {
                clienteApellido = req.getParameter("campo_cliente").toString();
                campoCliente = clienteApellido;
                top.setVariable("{check_1}","");
                top.setVariable("{check_2}","checked");             
            } else {
                clienteRut = req.getParameter("campo_cliente").toString();
                campoCliente = clienteRut;
                top.setVariable("{check_1}","checked");
                top.setVariable("{check_2}","");                
            }           
        }
        
        //Paginacion
        if (req.getParameter("pagina") != null) {
            pag = Integer.parseInt(req.getParameter("pagina"));
            logger.debug("pagina: " + req.getParameter("pagina"));
        } else {
            pag = 1; // por defecto mostramos la página 1
        }
        int regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
        
        //Verificamos si el usuario esta editando una ruta
        BizDelegate biz = new BizDelegate();
        
        // Rescatamos la informacion que necesitamos
        DespachoCriteriaDTO criterio = new DespachoCriteriaDTO();
        criterio.setF_despacho(fechaDespacho);
        
        if (!horasJor.equals("")) {
            int pos = horasJor.indexOf("-");
            criterio.setH_inicio( horasJor.substring(0, pos) );
            criterio.setH_fin( horasJor.substring(pos + 1) );
        }

        criterio.setId_zona(idZona);
        criterio.setId_local(idLocal);
        criterio.setOrigen(tipoPedido);
        criterio.setReprogramada(reprogramada);
        criterio.setId_pedido(idPedido);
        criterio.setClienteRut(clienteRut);
        criterio.setClienteApellido(clienteApellido);
        criterio.setRegsperpag(regsperpage);
        criterio.setPag(pag);
        
        logger.debug("vamos a buscar los pedidos");       
        
        // ---- pedidos ----
        List listPedidos = biz.getPedidosPendientesByCriterio(criterio);
        ArrayList pedidos = new ArrayList();
        for (int i = 0; i < listPedidos.size(); i++) {            
            IValueSet fila = new ValueSet();
            MonitorPedidosDTO ped = (MonitorPedidosDTO)listPedidos.get(i);
            if (ped.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
                fila.setVariable("{op}"      , "C" + ped.getPedExt().getNroGuiaCaso() + "<br/>(" + ped.getId_pedido() + ")");
            } else if (ped.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
                fila.setVariable("{op}"      , "JV" + ped.getPedExt().getNroGuiaCaso() + "<br/>(" + ped.getId_pedido() + ")");
            } else {
                fila.setVariable("{op}"      , String.valueOf(ped.getId_pedido()));
            }
            fila.setVariable("{id_pedido}", String.valueOf(ped.getId_pedido()));
            fila.setVariable("{local}", ped.getLocal_despacho());
            fila.setVariable("{dir}", ped.getDireccion());
            fila.setVariable("{comuna}", ped.getComuna());
            fila.setVariable("{zona}", ped.getZona_nombre());
            fila.setVariable("{fecha_hr_despacho}", Formatos.frmFecha(ped.getFdespacho()) + " " + Formatos.frmHoraSola(ped.getHdespacho()) + "-" + Formatos.frmHoraSola(ped.getHdespacho_fin()));
            fila.setVariable("{estado}", ped.getEstado());
            fila.setVariable("{tipo_pedido}", Formatos.getDescripcionOrigenPedido( ped.getOrigen()));
            fila.setVariable("{reprogramada}", Formatos.getDescripcionReprogramada( ped.getPedExt().getReprogramada()) );
            
            fila.setVariable("{origen}",""+Constantes.LIBERA_OP_ORIGEN_OP_PENDIENTES);
                
            fila.setVariable("{accion1}","ver");
            fila.setVariable("{direccion1}","ViewOPFormPedido");
            
            //el usuario tiene un pedido asignado : solo puede ver ese pedido para editar 
            if (usr.getId_pedido()>0){
                if(ped.getId_pedido()==usr.getId_pedido()){
                    //el pedido es suyo
                    fila.setVariable("{palito}","|");
                    fila.setVariable("{accion2}","editar");
                    fila.setVariable("{direccion2}","AsignaOP");
                    fila.setVariable("{user}",String.valueOf(ped.getId_usuario()));
                } else {
                    //no muestra nada para los pedidos que no son suyos
                    fila.setVariable("{palito}","");
                    fila.setVariable("{accion2}","");
                    fila.setVariable("{direccion2}","");
                    fila.setVariable("{user}","");
                }               
            }else{
                //el usuario no tiene un pedido asignado : puede ver solo los pedidos sin asignaciones
                if ( ped.getId_usuario() <= 0 ) {
                    fila.setVariable("{palito}","|");
                    fila.setVariable("{accion2}","editar");
                    fila.setVariable("{direccion2}","AsignaOP");
                    fila.setVariable("{user}",String.valueOf(ped.getId_usuario()));
                } else {
                    fila.setVariable("{palito}","");
                    fila.setVariable("{accion2}","");
                    fila.setVariable("{direccion2}","");
                    fila.setVariable("{user}","");  
                }
            }
            
            pedidos.add(fila);            
        }
        
        if ( listPedidos.size() < 1 ) {
            top.setVariable("{mje1}","La consulta no arrojó resultados");
        } else {
            top.setVariable("{mje1}","");
        }       
        
        ArrayList htmlZonas = new ArrayList();        
        List zonas = new ArrayList();
        if (idLocal != -1) { 
            zonas = biz.getZonasLocal(idLocal);
            for ( int i=0; i < zonas.size(); i++ ) {
                ZonaDTO zona = (ZonaDTO) zonas.get(i);
                IValueSet fila = new ValueSet();
                fila.setVariable("{id_zona}", String.valueOf(zona.getId_zona()));
                fila.setVariable("{nombre}", zona.getNombre());
                if ( idZona == zona.getId_zona() ) {
                    fila.setVariable("{selected}", "selected");
                } else {
                    fila.setVariable("{selected}", "");
                }
                htmlZonas.add(fila);            
            }        
        } else {
            zonas = biz.getZonas();
            for ( int i=0; i < zonas.size(); i++ ) {
                cl.bbr.jumbocl.clientes.dto.ZonaDTO zona = (cl.bbr.jumbocl.clientes.dto.ZonaDTO) zonas.get(i);
                IValueSet fila = new ValueSet();
                fila.setVariable("{id_zona}", String.valueOf(zona.getId_zona()));
                fila.setVariable("{nombre}", zona.getNombre());
                if ( idZona == zona.getId_zona() ) {
                    fila.setVariable("{selected}", "selected");
                } else {
                    fila.setVariable("{selected}", "");
                }
                htmlZonas.add(fila);            
            }
        }
        /*
        ArrayList htmlJornadas = new ArrayList();
        List jornadas = biz.getJornadasDespachoParaFiltro(idLocal, fechaDespacho, idZona);
        for ( int i=0; i < jornadas.size(); i++ ) {
            JornadaDTO jor = (JornadaDTO) jornadas.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id_jornada}", String.valueOf(jor.getId_jornada()));
            fila.setVariable("{jornada}", Formatos.frmHoraSola(jor.getH_inicio()) + " - " + Formatos.frmHoraSola(jor.getH_fin()) + "   " + Formatos.frmFecha(jor.getF_jornada()));
            if ( idJornada == jor.getId_jornada() ) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlJornadas.add(fila);            
        }*/
        
        List jornadas = biz.getJornadasDespachoParaFiltro(idLocal, fechaDespacho, idZona);
        Set horasJornadas = new TreeSet();
        for ( int i=0; i< jornadas.size();i++ ) {
            JornadaDTO jor = (JornadaDTO) jornadas.get(i);
            horasJornadas.add(Formatos.frmHoraSola( jor.getH_inicio() )+"-"+ Formatos.frmHoraSola( jor.getH_fin() ));
        }
        ArrayList htmlJornadas = new ArrayList();
        for( Iterator it = horasJornadas.iterator(); it.hasNext();) {
            IValueSet fila = new ValueSet();
            String horasTotal =(String)it.next();
            fila.setVariable("{horas}"    ,horasTotal);
            if (horasTotal.equalsIgnoreCase(horasJor))
                fila.setVariable("{selected}", "selected");
            else
                fila.setVariable("{selected}", "");
            htmlJornadas.add(fila);
        }
        
        
        ArrayList htmlLocales = new ArrayList();
        List locales = biz.getLocales();
        for ( int i=0; i < locales.size(); i++ ) {
            LocalDTO local = (LocalDTO) locales.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id_local}", String.valueOf(local.getId_local()));
            fila.setVariable("{nombre}", local.getNom_local());
            if ( idLocal == local.getId_local() ) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlLocales.add(fila);            
        }  
        
        ArrayList htmlTipos = new ArrayList();        
        List tiposPedido = Utils.comboTipoPedido();
        for (int i=0; i < tiposPedido.size(); i++) {
            ObjetoDTO obj = (ObjetoDTO) tiposPedido.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{tipo}", obj.getCodigo());
            fila.setVariable("{nombre}", obj.getNombre());
            if ( obj.getCodigo().equalsIgnoreCase(tipoPedido)) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlTipos.add(fila);
        }
        ArrayList htmlReprogramadas = new ArrayList();
        List reprogramadas = Utils.comboReprogramada();
        for (int i=0; i < reprogramadas.size(); i++) {
            ObjetoDTO obj = (ObjetoDTO) reprogramadas.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{rep}", obj.getCodigo());
            fila.setVariable("{desc}", obj.getNombre());
            if ( obj.getCodigo().equalsIgnoreCase(reprogramada)) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlReprogramadas.add(fila);
        }
        
        
        ArrayList pags = new ArrayList();
        double totalRegistros = biz.getCountPedidosPendientesByCriterio(criterio);
        
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
        
        //Informacion para la pagina
        top.setDynamicValueSets("PAGINAS", pags);
        top.setDynamicValueSets("PEDIDOS", pedidos);
        top.setDynamicValueSets("ZONAS", htmlZonas);
        top.setDynamicValueSets("JORNADAS", htmlJornadas);
        top.setDynamicValueSets("LOCALES", htmlLocales);        
        top.setDynamicValueSets("REPROGRAMADA", htmlReprogramadas);
        top.setDynamicValueSets("TIPOS", htmlTipos);
        
        top.setVariable("{campo_cliente_sel}", ""+campoClienteSel);
        top.setVariable("{campo_cliente}", ""+campoCliente);
        top.setVariable("{fecha_despacho}", fechaDespacho);
        
        top.setVariable("{reprogramada}", reprogramada);
        top.setVariable("{tipo_pedido}", tipoPedido);
        top.setVariable("{horas}", ""+horasJor);

        if ( idZona != -1 )
            top.setVariable("{id_zona}", ""+idZona);
        else
            top.setVariable("{id_zona}", "");
        if ( idLocal != -1 )
            top.setVariable("{id_local}", ""+idLocal);
        else
            top.setVariable("{id_local}", "");        
        if ( idPedido != -1)
            top.setVariable("{id_pedido}", ""+idPedido);
        else
            top.setVariable("{id_pedido}", "");  
        
        top.setVariable("{mensaje_error}", mensajeError);
        top.setVariable("{mensaje}", mensaje);
        
        if ( usr.getId_pedido() > 0 ) {
            top.setVariable("{usr_pedido}", "Ud. está editando la OP: <a href='ViewOPFormPedido?id_pedido=" + usr.getId_pedido() + "&mod=1&origen=" + Constantes.LIBERA_OP_ORIGEN_OP_PENDIENTES + "'>" + usr.getId_pedido() + "</a> (" +
                                            "<a href =\"javascript:popUpWindowModal('ViewLiberaMotivo?id_pedido=" + usr.getId_pedido() + "&origen=" + Constantes.LIBERA_OP_ORIGEN_OP_PENDIENTES + "', 200, 200, 500, 180);\"> Liberar OP </a> )");
        } else {
            top.setVariable("{usr_pedido}", "");
        }
        
        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
        Date now = new Date();
        top.setVariable("{hdr_fecha}", now.toString()); 
		
		logger.debug("Fin ViewMonOpPendientes Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
