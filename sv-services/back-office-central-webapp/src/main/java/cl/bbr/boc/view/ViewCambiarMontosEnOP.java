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
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite cambiar los precios de un pedido pickeado
 * @author imoyano
 */
public class ViewCambiarMontosEnOP extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo ViewCambiarMontosEnOP Execute");

        View salida = new View(res);
        String html = getServletConfig().getInitParameter("TplFile");
        html = path_html + html;
        
        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();

        long idPedido = Long.parseLong(req.getParameter("id_pedido"));

        BizDelegate biz = new BizDelegate();
        
        
        if(biz.isActivaCorreccionAutomatica()){	
        	//res.sendRedirect("/JumboBOCentral/ViewError?mensaje=Correccion de excesos automatico esta activado.&url=&PagErr=1");        
        	res.sendRedirect("/JumboBOCentral/ViewFormCambiaPrecio?id_pedido="+req.getParameter("id_pedido"));
        }
        
        double totalPedido = 0;
        
        ArrayList susts = new ArrayList();
        List listaSust = biz.getSustitutosYPesablesByPedidoId(idPedido);
        
        for ( int i = 0; i < listaSust.size(); i++ ) {         
            IValueSet filaSust = new ValueSet();
            SustitutoDTO s = (SustitutoDTO )listaSust.get(i);            

            filaSust.setVariable("{i}"      ,String.valueOf(i));
            
            filaSust.setVariable("{cod_prod1}"      ,s.getCod_prod1());
            filaSust.setVariable("{desc_prod1}"     ,s.getDescr1());            
            filaSust.setVariable("{cant_prod1}"      ,String.valueOf(s.getCant1()));
            filaSust.setVariable("{precio1}"   , Formatos.redondear(s.getPrecio1())+"");
            
            filaSust.setVariable("{id_picking}"      ,s.getId_detalle_pick1()+"");
            
            filaSust.setVariable("{cod_prod2}"      ,s.getCod_prod2());
            filaSust.setVariable("{desc_prod2}"     ,s.getDescr2());            
            filaSust.setVariable("{cant_prod2}"      ,String.valueOf(s.getCant2()));
            filaSust.setVariable("{precio2}"   , Formatos.redondear(s.getPrecio2())+"");
            
            filaSust.setVariable("{precio_tot}"   , Formatos.redondear( ( s.getCant2() * s.getPrecio2()) ) + "");
            
            susts.add(filaSust);
        }

        List pedidosPickeados = biz.getProductosPickeadosByIdPedido(idPedido);
        for (int i=0; i < pedidosPickeados.size(); i++) {
            ProductosPedidoDTO p = (ProductosPedidoDTO) pedidosPickeados.get(i);
            totalPedido += ( p.getCant_pick() * p.getPrecio() );
        }
        
        top.setDynamicValueSets("SUSTITUTOS", susts);

        PedidoDTO pedido = biz.getPedidosById(idPedido);
        top.setVariable("{id_pedido}", String.valueOf(idPedido));
        
        top.setVariable("{total}", Formatos.redondear( pedido.getCosto_despacho() + totalPedido ) + "" );
        top.setVariable("{total_reservado}", Formatos.redondear( pedido.getMonto_reservado() ) + "" );
        
        double excedido = pedido.getMonto_reservado() - ( pedido.getCosto_despacho() + totalPedido ); 
        
        top.setVariable("{excedido}", Formatos.redondear( excedido ) + "" );
        
        if ( excedido < 0 ) {
            top.setVariable("{color}", "red");
            top.setVariable("{disabled}", "");
            
        } else {
            top.setVariable("{color}", "blue");
            top.setVariable("{disabled}", "disabled=\"disabled\"");
        }
        
        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
        Date now = new Date();
        top.setVariable("{hdr_fecha}", now.toString());
        
        if ( req.getParameter("msg") != null && "OK".equalsIgnoreCase(req.getParameter("msg")) ) {
            top.setVariable("{msg}", "Cambio realizado exitosamente");
        } else {
            top.setVariable("{msg}", "");
        }

        logger.debug("Fin ViewCambiarMontosEnOP Execute");
        String result = tem.toString(top);
        salida.setHtmlOut(result);
        salida.Output();	
		
	}

}