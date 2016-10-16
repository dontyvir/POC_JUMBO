package cl.bbr.boc.view;

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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.FacturasDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra el detalle de un pedido de ruta
 * 
 * @author imoyano
 *
 */
public class ViewDetPedidoRuta extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewDetPedidoRuta Execute");
	    
	    View salida = new View(res);
        String html = getServletConfig().getInitParameter("TplFile");
        html = path_html + html;
        logger.debug("Template: " + html);

        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();

        long idPedido = Long.parseLong(req.getParameter("id_pedido"));
        
        BizDelegate biz = new BizDelegate();
        
        //Info para la pantalla
        PedidoDTO ped = biz.getPedidosById(idPedido);

        if (ped.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
            top.setVariable("{id_pedido}", "C" + ped.getPedidoExt().getNroGuiaCaso() + " <a href='ViewDetalleCaso?id_caso=" + ped.getPedidoExt().getNroGuiaCaso() + "'>Detalle del Caso</a>" + " (" + ped.getId_pedido() + ")");
        } else if (ped.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
            top.setVariable("{id_pedido}", "JV" + ped.getPedidoExt().getNroGuiaCaso() + " (" + ped.getId_pedido() + ")");
        } else {
            top.setVariable("{id_pedido}", String.valueOf(ped.getId_pedido()));
        }
        
        top.setVariable("{id_ruta}", "" + ped.getPedidoExt().getIdRuta());
        top.setVariable("{fecha_hora_desp}", Formatos.frmFecha(ped.getFdespacho()) + "  " + Formatos.frmHoraSola(ped.getHdespacho()) + "-" + Formatos.frmHoraSola(ped.getHfindespacho()));
        top.setVariable("{zona}", "" + biz.getZonaById(ped.getId_zona()).getDescripcion());
        top.setVariable("{tipo_pedido}", Formatos.getDescripcionOrigenPedido(ped.getOrigen()));
        top.setVariable("{tipo_despacho}", Formatos.getDescripcionTipoDespacho( ped.getTipo_despacho()) );
        top.setVariable("{costo_despacho}", Formatos.formatoPrecio( ped.getCosto_despacho()) );
        top.setVariable("{editor}", "" + ped.getNom_usuario_bo());
        
        if ( ped.getPedidoExt().getReprogramada() > 0 ) {
            top.setVariable("{reprogramada}", "Si");
        } else {
            top.setVariable("{reprogramada}", "No");
        }
        top.setVariable("{local}", ped.getNom_local());
        
        RutaDTO ruta = biz.getRutaById(ped.getPedidoExt().getIdRuta());
        top.setVariable("{chofer}", ruta.getChofer().getNombre());
        top.setVariable("{patente}", ruta.getPatente().getPatente());

        top.setVariable("{nombre_cli}", ped.getNom_cliente());
        top.setVariable("{rut_cli}", ped.getRut_cliente() + "-" + ped.getDv_cliente());
        top.setVariable("{fono1_cli}", ped.getTelefono());
        top.setVariable("{fono2_cli}", ped.getTelefono2());
        top.setVariable("{direc_cli}", ped.getDir_tipo_calle() + " " + ped.getDir_calle() + " " + ped.getDir_numero() + " " + ped.getDir_depto());
        top.setVariable("{comuna_cli}", ped.getNom_comuna());

        top.setVariable("{fc_hora_llegada}", Formatos.frmFechaHora( ped.getPedidoExt().getFcHoraLlegadaDomicilio() ));
        top.setVariable("{fc_hora_salida}", Formatos.frmFechaHora( ped.getPedidoExt().getFcHoraSalidaDomicilio() ));

        top.setVariable("{atrasado}", Formatos.getDescripcionCumplimiento(ped.getPedidoExt().getCumplimiento()));
        
        top.setVariable("{tipo_doc}", descripcionDoc( ped.getTipo_doc() ) );
        top.setVariable("{num_docs}", verDocumentos( biz.getDocumentosByPedido( idPedido ) ));
        
        top.setVariable("{monto}", Formatos.formatoPrecio(ped.getMonto()));
        top.setVariable("{fc_hora_compra}", Formatos.frmFecha(ped.getFingreso()) + " " + Formatos.frmHoraSola(ped.getHingreso()));
        top.setVariable("{id_pedido_real}", String.valueOf(ped.getId_pedido()));
        
        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
        Date now = new Date();
        top.setVariable("{hdr_fecha}", now.toString());	
		
		logger.debug("Fin ViewDetPedidoRuta Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();	
	}

   private String descripcionDoc(String tipoDoc) {
        if ( "B".equalsIgnoreCase( tipoDoc ) ) {
            return "Boleta";
        } else if ( "F".equalsIgnoreCase( tipoDoc ) ) {
            return "Factura";
        } else {
            return "-";
        }        
    }

    private String verDocumentos(List documentos) {
        String docs = "";
        String sep = "";
        
        for ( int i=0; i < documentos.size(); i++ ) {
            FacturasDTO doc = (FacturasDTO) documentos.get(i);
            docs += sep + doc.getNum_doc();
            sep = ", ";
        }
        
        return docs;
    }
}
