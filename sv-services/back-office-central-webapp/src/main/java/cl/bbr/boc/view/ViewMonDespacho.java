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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaCriterioDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra listado para el monitor de despacho
 * 
 * @author imoyano
 */
public class ViewMonDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewMonDespacho Execute");
	    
        String fechaDespacho = "";
        long idZona     = -1;
        long idChofer   = -1;
        long idPatente = -1;
        long idEstado = -1;
        long idRuta = -1;
        long idPedido   = -1;
        long idLocal   = -1;
        String campoCliente = "";
        String campoClienteSel = "";
        String clienteRut = "";
        String clienteApellido = "";
        int pag             = 1;
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
        if (req.getParameter("fecha_despacho") != null) {
            fechaDespacho = req.getParameter("fecha_despacho").toString();
        }
        if (req.getParameter("horas") != null) {
            horasJor = req.getParameter("horas");           
        }
        if (req.getParameter("id_zona") != null) {
            idZona = Long.parseLong(req.getParameter("id_zona").toString());
        }
        if (req.getParameter("id_chofer") != null) {
            idChofer = Long.parseLong(req.getParameter("id_chofer").toString());
        }
        if (req.getParameter("id_patente") != null) {
            idPatente = Long.parseLong(req.getParameter("id_patente").toString());
        }
        if (req.getParameter("id_estado") != null) {
            idEstado = Long.parseLong(req.getParameter("id_estado").toString());
        }
        if (req.getParameter("id_ruta") != null && !"".equalsIgnoreCase(req.getParameter("id_ruta"))) {
            idRuta = Long.parseLong(req.getParameter("id_ruta").toString());
        }
        if (req.getParameter("id_pedido") != null && !"".equalsIgnoreCase(req.getParameter("id_pedido"))) {
            idPedido = Long.parseLong(req.getParameter("id_pedido").toString());
        }
        if (req.getParameter("id_local") != null) {
            idLocal = Long.parseLong(req.getParameter("id_local").toString());
        }
        
        top.setVariable("{check_1}","checked");
        top.setVariable("{check_2}","");
        if (req.getParameter("campo_cliente_sel") != null && req.getParameter("campo_cliente") != null) {
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
        
        String hIni = "";
        String hFin = "";
        if (!horasJor.equals("")) {
            int pos = horasJor.indexOf("-");
            hIni = horasJor.substring(0, pos);
            hFin = horasJor.substring(pos + 1);
        }
        
        // Rescatamos la informacion que necesitamos
        RutaCriterioDTO criterio = new RutaCriterioDTO(fechaDespacho, idZona, idChofer, idPatente, idEstado, idRuta, idPedido, clienteRut, clienteApellido, pag, regsperpage, idLocal, hIni, hFin);
        logger.debug("vamos a buscar los casos");       
        
        // ---- rutas ----
        List listRutas = biz.getRutasByCriterio(criterio);
        ArrayList rutas = new ArrayList();
        for (int i = 0; i < listRutas.size(); i++) {            
            IValueSet fila = new ValueSet();
            RutaDTO ruta = (RutaDTO)listRutas.get(i);
            fila.setVariable("{id_ruta}" , String.valueOf(ruta.getIdRuta()));
            fila.setVariable("{fono}" , ruta.getFono().getCodigo() + " " + ruta.getFono().getNumero());
            fila.setVariable("{chofer}" , ruta.getChofer().getNombre());
            fila.setVariable("{patente}" , ruta.getPatente().getPatente());
            if (ruta.getFechaSalida() != null ) {
                fila.setVariable("{fechahoraout}" , Formatos.frmFechaHora( ruta.getFechaSalida() ));
            } else {
                fila.setVariable("{fechahoraout}" , "&nbsp;");
            }
            fila.setVariable("{fecha}" , Formatos.frmFecha( ruta.getFechaCreacion() ));
            fila.setVariable("{estado}" , ruta.getEstado().getNombre());
            fila.setVariable("{local}" , ruta.getLocal().getNom_local());
            
            String zonas = "";
            String comaz = "";
            for (int j=0; j < ruta.getZonas().size(); j++ ) {
                ZonaDTO zona = (ZonaDTO) ruta.getZonas().get(j);
                zonas += ( comaz + zona.getNombre());
                comaz = ", <br/>";
            }
            fila.setVariable("{zonas}" , zonas);
            
            String jornadas = "";
            String comaj = "";
            for (int j=0; j < ruta.getJornadas().size(); j++ ) {
                JornadaDTO jornada = (JornadaDTO) ruta.getJornadas().get(j);
                jornadas += ( comaj + Formatos.frmHoraSola(jornada.getH_inicio()) + "-" + Formatos.frmHoraSola(jornada.getH_fin()) );
                comaj = ", <br/>";
            }
            fila.setVariable("{jornadas}" , jornadas);
            
            rutas.add(fila);            
        }
        
        if (listRutas.size() < 1 ) {
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
        
        
        /*ArrayList htmlJornadas = new ArrayList();
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
        
        ArrayList htmlChoferes = new ArrayList();
        List choferes = biz.getChoferesDeTransporte(0,idLocal);
        for ( int i=0; i < choferes.size(); i++ ) {
            ChoferTransporteDTO chofer = (ChoferTransporteDTO) choferes.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id_chofer}", String.valueOf(chofer.getIdChofer()));
            fila.setVariable("{chofer}", chofer.getNombre());
            if ( idChofer == chofer.getIdChofer() ) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlChoferes.add(fila);            
        }
        ArrayList htmlPatentes = new ArrayList();
        List patentes = biz.getPatentesDeTransporte(0,idLocal);
        for ( int i=0; i < patentes.size(); i++ ) {
            PatenteTransporteDTO patente = (PatenteTransporteDTO) patentes.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id_patente}", String.valueOf(patente.getIdPatente()));
            fila.setVariable("{patente}", patente.getPatente());
            if ( idPatente == patente.getIdPatente() ) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlPatentes.add(fila);            
        }
        ArrayList htmlEstados = new ArrayList();
        List estados = biz.getEstadosRuta();
        for ( int i=0; i < estados.size(); i++ ) {
            EstadoDTO estado = (EstadoDTO) estados.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id_estado}", String.valueOf(estado.getId_estado()));
            fila.setVariable("{estado}", estado.getNombre());
            if ( idEstado == estado.getId_estado() ) {
                fila.setVariable("{selected}", "selected");
            } else {
                fila.setVariable("{selected}", "");
            }
            htmlEstados.add(fila);            
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
        
        ArrayList pags = new ArrayList();
        double totalRegistros = biz.getCountRutasByCriterio(criterio);
        
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
        top.setDynamicValueSets("RUTAS", rutas);
        top.setDynamicValueSets("ZONAS", htmlZonas);
        top.setDynamicValueSets("JORNADAS", htmlJornadas);
        top.setDynamicValueSets("CHOFERES", htmlChoferes);
        top.setDynamicValueSets("PATENTES", htmlPatentes);
        top.setDynamicValueSets("ESTADOS", htmlEstados);
        top.setDynamicValueSets("LOCALES", htmlLocales);        
        
        top.setVariable("{fecha_despacho}", fechaDespacho);
        top.setVariable("{horas}", ""+horasJor);
        top.setVariable("{id_zona}", ""+idZona);
        top.setVariable("{id_chofer}", ""+idChofer);
        top.setVariable("{id_patente}", ""+idPatente);
        top.setVariable("{id_estado}", ""+idEstado);
        if ( idRuta != -1 ) {
            top.setVariable("{id_ruta}", ""+idRuta);
        } else {
            top.setVariable("{id_ruta}", "");    
        }
        if ( idPedido != -1) {
            top.setVariable("{id_pedido}", ""+idPedido);
        } else {
            top.setVariable("{id_pedido}", "");    
        }
        top.setVariable("{id_local}", ""+idLocal);
        top.setVariable("{campo_cliente_sel}", ""+campoClienteSel);
        top.setVariable("{campo_cliente}", ""+campoCliente);
        
        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
        Date now = new Date();
        top.setVariable("{hdr_fecha}", now.toString()); 
		
		logger.debug("Fin ViewMonDespacho Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
