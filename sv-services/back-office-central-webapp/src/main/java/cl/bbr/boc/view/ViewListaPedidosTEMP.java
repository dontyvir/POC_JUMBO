package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Pedidos en transicion
 * 
 * @author imoyano
 *
 */
public class ViewListaPedidosTEMP extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewListaPedidosTEMP Execute");
	    
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");		
		html = path_html + html;
		logger.debug("Template: " + html);	    
	    
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
        
        long idLocal = -1;
        
        if ( req.getParameter("id_local") != null ) {
            idLocal = Long.parseLong(req.getParameter("id_local"));
        }
		
        ResourceBundle rb = ResourceBundle.getBundle("bo");
        String key = rb.getString("conf.bo.key");
        
		BizDelegate biz = new BizDelegate();
        List pedidos = biz.getPedidosEnTransicionTEMP(idLocal);
        ArrayList htmlPedido = new ArrayList();
        for (int i=0; i < pedidos.size(); i++) {
            PedidoDTO pedido = (PedidoDTO) pedidos.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{id_pedido}", String.valueOf(pedido.getId_pedido()));
            fila.setVariable("{id_local}", String.valueOf(pedido.getId_local()));
            fila.setVariable("{local}", pedido.getNom_local());
            fila.setVariable("{estado}", pedido.getEstado());
            
            if ( pedido.getNum_mp() != null && 
                    !"null".equalsIgnoreCase(pedido.getNum_mp()) && 
                    !"".equalsIgnoreCase(pedido.getNum_mp()) ) {
                fila.setVariable("{tarjeta}", Cifrador.desencriptar(key,pedido.getNum_mp()));
            } else {
                fila.setVariable("{tarjeta}", "");    
            }
            
            fila.setVariable("{rut}", pedido.getRut_cliente() + "-" + pedido.getDv_cliente());
            fila.setVariable("{fc_ex}", ""+pedido.getFecha_exp());
            fila.setVariable("{tarj}", ""+pedido.getNom_tbancaria());
            
            fila.setVariable("{cuotas}", ""+pedido.getN_cuotas());
            fila.setVariable("{fc_compra}", Formatos.frmFecha( pedido.getFingreso() ));
            fila.setVariable("{fc_despacho}", Formatos.frmFecha( pedido.getFdespacho() ));
            fila.setVariable("{monto}", Formatos.formatoPrecio( pedido.getMonto() ));            
            htmlPedido.add(fila);            
        }        
        if ( pedidos.size() > 0 ) {
            top.setVariable("{msg}", "");
        } else {
            top.setVariable("{msg}", "No existen pedidos para listar");    
        }
        
        top.setDynamicValueSets("PEDIDOS", htmlPedido);
        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());		
		
		logger.debug("Fin ViewListaPedidosTEMP Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
