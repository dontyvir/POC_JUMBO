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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra el detalle de una ruta
 * 
 * @author imoyano
 *
 */
public class ViewDetRuta extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewDetRuta Execute");
	    
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
        long idRuta = Long.parseLong(req.getParameter("id_ruta"));
		
		BizDelegate biz = new BizDelegate();
        
        RutaDTO ruta = biz.getRutaById(idRuta);       
        
        if ( req.getParameter("mensaje_error") != null ) {
            top.setVariable("{msj}", req.getParameter("mensaje_error"));
        } else {
            top.setVariable("{msj}", "");
        }       
        
        if (ruta.getEstado().getId_estado() == Constantes.ESTADO_RUTA_ANULADA) {
            top.setVariable("{btn_disabled}", "disabled");
        } else {
            top.setVariable("{btn_disabled}", "");    
        }        
		top.setVariable("{id_ruta}", "" + idRuta);
        top.setVariable("{estado}", "" + ruta.getEstado().getNombre());
        top.setVariable("{fono}", "" + ruta.getFono().getCodigo() + " " + ruta.getFono().getNumero());
        top.setVariable("{chofer}", "" + ruta.getChofer().getNombre());
        top.setVariable("{local}", "" + ruta.getLocal().getNom_local());
        top.setVariable("{patente}", "" + ruta.getPatente().getPatente());
        if ( ruta.getFechaSalida() != null ) {
            top.setVariable("{fecha_hora_out}", "" + Formatos.frmFechaHora( ruta.getFechaSalida() ));
        } else {
            top.setVariable("{fecha_hora_out}", "");    
        }

        String zonas = "";
        String comaz = "";
        for (int j=0; j < ruta.getZonas().size(); j++ ) {
            ZonaDTO zona = (ZonaDTO) ruta.getZonas().get(j);
            zonas += ( comaz + zona.getNombre());
            comaz = ", ";
        }
        if ( !zonas.equalsIgnoreCase("null") ) {
            top.setVariable("{zonas}" , zonas);
        } else {
            top.setVariable("{zonas}" , "");    
        }
        
        String jornadas = "";
        String comaj = "";
        for (int j=0; j < ruta.getJornadas().size(); j++ ) {
            JornadaDTO jornada = (JornadaDTO) ruta.getJornadas().get(j);
            jornadas += ( comaj + Formatos.frmHoraSola(jornada.getH_inicio()) + "-" + Formatos.frmHoraSola(jornada.getH_fin()) );
            comaj = ", ";
        }
        top.setVariable("{jornadas}" , jornadas);
        
        if ( ruta.getFechaCreacion() != null ) {
            top.setVariable("{fecha}", "" + Formatos.frmFecha( ruta.getFechaCreacion() ));
        } else {
            top.setVariable("{fecha}", "");
        }
        top.setVariable("{bins}", "" + ruta.getCantBins());
        
        List logs = biz.getLogRuta(idRuta);
        ArrayList htmlLogs = new ArrayList();
        for (int i=0; i < logs.size(); i++) {
            LogRutaDTO log = (LogRutaDTO) logs.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{fecha}"      , Formatos.frmFechaHora( log.getFecha() ));
            fila.setVariable("{usuario}"      , log.getUsuario().getLogin());
            fila.setVariable("{estado}"      , log.getEstado().getNombre());
            fila.setVariable("{detalle}"      , log.getDescripcion());
            htmlLogs.add(fila);
        }
        if (logs.size()>0) {
            top.setVariable("{msj_log}", "");     
        } else {
            top.setVariable("{msj_log}", "No existen datos");     
        }
        
        List responsables = biz.getResponsablesDespachoNC();
        ArrayList htmlResp = new ArrayList();
        for (int i=0; i < responsables.size(); i++) {
            ObjetoDTO resp = (ObjetoDTO) responsables.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id}", String.valueOf(resp.getIdObjeto()));
            fila.setVariable("{nombre}", resp.getNombre());
            htmlResp.add(fila);            
        }
                
        List motivos = biz.getMotivosDespachoNC();
        ArrayList htmlMotvs = new ArrayList();
        for (int i=0; i < motivos.size(); i++) {
            ObjetoDTO mot = (ObjetoDTO) motivos.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id}", String.valueOf(mot.getIdObjeto()));
            fila.setVariable("{nombre}", mot.getNombre());
            htmlMotvs.add(fila);            
        }
        
        List opsRuta = biz.getPedidosByRuta(idRuta);
        ArrayList htmlPeds = new ArrayList();
        
        ArrayList htmlPedsEnDespacho = new ArrayList();
        
        for (int i=0; i < opsRuta.size(); i++) {
            PedidoDTO ped = (PedidoDTO) opsRuta.get(i);
            IValueSet fila = new ValueSet();
            if (ped.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
                fila.setVariable("{op}"      , "C" + ped.getPedidoExt().getNroGuiaCaso() + "<br/>(" + ped.getId_pedido() + ")");
            } else if (ped.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
                fila.setVariable("{op}"      , "JV" + ped.getPedidoExt().getNroGuiaCaso() + "<br/>(" + ped.getId_pedido() + ")");
            } else {
                fila.setVariable("{op}"      , String.valueOf(ped.getId_pedido()));
            }
            fila.setVariable("{id_pedido_real}"      , String.valueOf(ped.getId_pedido()));
            fila.setVariable("{tipo}"       , Formatos.getDescripcionOrigenPedido(ped.getOrigen()));
            if (ped.getPedidoExt().getReprogramada() > 0) {
                fila.setVariable("{repr}" , "Si");
                JornadaDTO jorOriginal = biz.getJornadaDespachoOriginalDePedidoReprogramado(ped.getId_pedido());
                fila.setVariable("{jornada}" , "ORI: " + Formatos.frmHoraSola(jorOriginal.getH_inicio()) + "-" + Formatos.frmHoraSola(jorOriginal.getH_fin()) + "   " + Formatos.frmFecha(jorOriginal.getF_jornada()) + " <br /> NVA: " + Formatos.frmHoraSola(ped.getHdespacho()) + "-" + Formatos.frmHoraSola(ped.getHfindespacho()) + "   " + Formatos.frmFecha(ped.getFdespacho()));
                fila.setVariable("{hini_despacho}", Formatos.frmHoraSola(jorOriginal.getH_inicio()));
                fila.setVariable("{hfin_despacho}", Formatos.frmHoraSola(jorOriginal.getH_fin()));
                fila.setVariable("{fc_despacho}",Formatos.frmFecha(jorOriginal.getF_jornada()));
            } else {
                fila.setVariable("{repr}" , "No");
                fila.setVariable("{jornada}" , Formatos.frmHoraSola(ped.getHdespacho()) + "-" + Formatos.frmHoraSola(ped.getHfindespacho()) + "   " + Formatos.frmFecha(ped.getFdespacho()));
                fila.setVariable("{hini_despacho}", Formatos.frmHoraSola(ped.getHdespacho()));
                fila.setVariable("{hfin_despacho}", Formatos.frmHoraSola(ped.getHfindespacho()));
                fila.setVariable("{fc_despacho}",Formatos.frmFecha(ped.getFdespacho()));
            }
            fila.setVariable("{direc}", ped.getDir_tipo_calle() + " " + ped.getDir_calle() + " " + ped.getDir_numero() + " " + ped.getDir_depto());
            fila.setVariable("{comuna}" , ped.getNom_comuna());
            fila.setVariable("{zona}" , ped.getNomZona());
            fila.setVariable("{estado}" , ped.getEstado());
            fila.setVariable("{llegada}" , Formatos.frmFechaHora( ped.getPedidoExt().getFcHoraLlegadaDomicilio()) );
            fila.setVariable("{salida}" , Formatos.frmFechaHora( ped.getPedidoExt().getFcHoraSalidaDomicilio()) );
            fila.setVariable("{doc}" , ped.getTipo_doc());
            
            if ( usr.getId_pedido() == ped.getId_pedido() ) {
                fila.setVariable("{edit}" , "&nbsp;| <a href=\"ViewModDetPedidoRuta?id_pedido="+ped.getId_pedido()+"&id_ruta="+idRuta+"\">editar</a>");
            } else if ( usr.getId_pedido() == 0 && ped.getId_usuario() == 0) {
                fila.setVariable("{edit}" , "&nbsp;| <a href=\"ViewModDetPedidoRuta?id_pedido="+ped.getId_pedido()+"&id_ruta="+idRuta+"\">editar</a>");
            } else {
                fila.setVariable("{edit}" , "");
            }
            
            if ((i % 2) == 0) {
                fila.setVariable("{bg_color}" , "#DEDEDE");
            } else {
                fila.setVariable("{bg_color}" , "#EFEFDF");    
            }
            fila.setDynamicValueSets("RESPONSABLES", htmlResp);
            fila.setDynamicValueSets("MOTIVOS", htmlMotvs);
            
            htmlPeds.add(fila);
            if ( ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_DESPACHO ) {
                htmlPedsEnDespacho.add(fila);
            }
        }
        if (opsRuta.size()>0) {
            top.setVariable("{msj_peds}", "");     
        } else {
            top.setVariable("{msj_peds}", "No existen datos");     
        }
        
        if (htmlPedsEnDespacho.size()>0) {
            top.setVariable("{msj_peds_finalizar}", "");     
        } else {
            top.setVariable("{msj_peds_finalizar}", "No existen pedidos para finalizar");     
        }
        
        top.setDynamicValueSets("LOG_RUTA", htmlLogs);
        top.setDynamicValueSets("OPS", htmlPeds);
        top.setDynamicValueSets("OPS_PARA_FINALIZAR", htmlPedsEnDespacho);
        
        String var_usrped = "Ud. está editando la OP: <a href='ViewOPFormPedido?id_pedido=" + usr.getId_pedido() + "&mod=1&id_ruta="+idRuta+"'> "+usr.getId_pedido()+"</a> " +
                            "(<a href =\"javascript:popUpWindowModal('ViewLiberaMotivo?id_pedido=" +usr.getId_pedido()+"&origen=3&id_ruta="+idRuta+"', 200, 200, 500, 180);\"> Liberar OP </a> )";
        
        if (usr.getId_pedido() > 0 ) {
            top.setVariable("{usr_pedido}", var_usrped);
        } else {
            top.setVariable("{usr_pedido}", "");
        }
        
        
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());		
		
		logger.debug("Fin ViewDetRuta Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
