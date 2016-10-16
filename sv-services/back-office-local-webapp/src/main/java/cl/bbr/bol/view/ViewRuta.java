package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
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
public class ViewRuta extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewRuta Execute");
	    
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
		
        if (ruta.getEstado().getId_estado() == Constantes.ESTADO_RUTA_ANULADA) {
            top.setVariable("{btn_disabled}", "disabled");
        } else {
            top.setVariable("{btn_disabled}", "");    
        }        
		top.setVariable("{id_ruta}", "" + idRuta);
        top.setVariable("{estado}", "" + ruta.getEstado().getNombre());
        top.setVariable("{fono}", "" + ruta.getFono().getCodigo() + " " + ruta.getFono().getNumero());
        top.setVariable("{chofer}", "" + ruta.getChofer().getNombre());
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
        boolean puedeImprimir = true;
        List opsRuta = biz.getPedidosByRuta(idRuta);
        ArrayList htmlPeds = new ArrayList();
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
            if ( ped.getPedidoExt().getReprogramada() > 0 ) {
                fila.setVariable("{repr}" , "Si");
            } else {
                fila.setVariable("{repr}" , "No");
            }
            fila.setVariable("{direc}", ped.getDir_tipo_calle() + " " + ped.getDir_calle() + " " + ped.getDir_numero() + " " + ped.getDir_depto());
            fila.setVariable("{comuna}" , ped.getNom_comuna());
            fila.setVariable("{zona}" , ped.getNomZona());
            fila.setVariable("{jornada}" , Formatos.frmHoraSola(ped.getHdespacho()) + "-" + Formatos.frmHoraSola(ped.getHfindespacho()) + "   " + Formatos.frmFecha(ped.getFdespacho()));
            fila.setVariable("{estado}" , ped.getEstado());
            fila.setVariable("{llegada}" , ped.getPedidoExt().getFcHoraLlegadaDomicilio());
            fila.setVariable("{salida}" , ped.getPedidoExt().getFcHoraSalidaDomicilio());
            fila.setVariable("{doc}" , ped.getTipo_doc());
            if ( !estadoParaImprimir( ped.getId_estado() ) ) {
                puedeImprimir = false;
            }
            
            htmlPeds.add(fila);
        }
        if (opsRuta.size()>0) {
            top.setVariable("{msj_peds}", "");     
        } else {
            puedeImprimir = false;
            top.setVariable("{msj_peds}", "No existen datos");     
        }
        if (puedeImprimir) {
            top.setVariable("{link_1}", "abrirVentana('ViewHojaDespacho', 1);");
            top.setVariable("{link_2}", "abrirVentana('ViewHojaDespacho2', 1);");
        } else {
            top.setVariable("{link_1}", "alert('Acción no permitida');");
            top.setVariable("{link_2}", "alert('Acción no permitida');");
        }
        
        
        
        top.setDynamicValueSets("LOG_RUTA", htmlLogs);
        top.setDynamicValueSets("OPS", htmlPeds);
        
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());		
		
		logger.debug("Fin ViewRuta Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();	
	}

    /**
     * @param id_estado
     * @return
     */
    private boolean estadoParaImprimir(long idEstado) {
        for (int i=0; i < Constantes.ESTADOS_PEDIDO_PARA_IMPRIMIR_HOJAS_DESPACHO.length; i++) {
            if ( Long.parseLong(Constantes.ESTADOS_PEDIDO_PARA_IMPRIMIR_HOJAS_DESPACHO[i]) == idEstado ) {
                return true;
            }
        }
        return false;
    }
}
