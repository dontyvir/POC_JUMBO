package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega una hoja de ruta
 * @author imoyano
 */
public class ViewHojaRuta extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long 	idRuta		=-1;
		
		ResourceBundle rb = ResourceBundle.getBundle("bo");
		String key = rb.getString("GoogleMaps.key");

		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		
		idRuta = Long.parseLong(req.getParameter("id_ruta"));
		
		//String local_tipo_picking = usr.getLocal_tipo_picking();
		
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

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
            top.setVariable("{fecha_hora_out}", "" + Formatos.frmFechaHora(ruta.getFechaSalida() ));
        } else {
            top.setVariable("{fecha_hora_out}", "");    
        }
        
		LocalDTO local = biz.getLocalByID(usr.getId_local());
		top.setVariable("{latitud}", local.getLatitud()+"");
		top.setVariable("{longitud}", local.getLongitud()+"");
		top.setVariable("{nom_local}", usr.getLocal());
		top.setVariable("{key}", key);

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
        
        //char marca = 'A';
	    
        int j=0;
        List opsRuta = biz.getPedidosByRuta(idRuta);
        ArrayList htmlPeds = new ArrayList();
        for (int i=0; i < opsRuta.size(); i++) {
            PedidoDTO ped = (PedidoDTO) opsRuta.get(i);
            IValueSet fila = new ValueSet();
            if (ped.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE)) {
                fila.setVariable("{op}"      , "C" + ped.getPedidoExt().getNroGuiaCaso());
            } else if (ped.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
                fila.setVariable("{op}"      , "JV" + ped.getPedidoExt().getNroGuiaCaso());
            } else {
                fila.setVariable("{op}"    , String.valueOf(ped.getId_pedido()));
            }
            fila.setVariable("{id_pedido_real}"      , String.valueOf(ped.getId_pedido()));
            fila.setVariable("{tipo}"       , Formatos.getDescripcionOrigenPedido(ped.getOrigen()));
            if (ped.getPedidoExt().getReprogramada() > 0) {
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
            fila.setVariable("{mpa}" , medioPago( ped ));
            fila.setVariable("{lat}" , ped.getLatitud()+"");
            fila.setVariable("{lng}" , ped.getLongitud()+"");
            //fila.setVariable("{confirmada}" , ped.isConfirmada());
            //marca = (char)(65 + i);
            if (ped.isConfirmada()){
                fila.setVariable("{marca}" , new Character((char)(65+j)).toString());
                j++;
            }else{
                fila.setVariable("{marca}" , " ");
            }

            //fila.setVariable("{marca}" , (char)(65 + i)+"");
            //fila.setVariable("{marca}" , i+"");
            htmlPeds.add(fila);
        }
        top.setDynamicValueSets("OPS", htmlPeds);

		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}

    /**
     * @param ped
     * @return
     */
    private String medioPago(PedidoDTO ped) {
        if ( "CHEQUE".equalsIgnoreCase(ped.getNom_tbancaria()) && Constantes.MEDIO_PAGO_CAT.equalsIgnoreCase(ped.getMedio_pago()) ) {
            return "Cheque";
        }
        return ped.getMedio_pago();
    }
}
