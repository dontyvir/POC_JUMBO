package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.vte.bizdelegate.BizDelegate;

/**
 * Página que muestra el listado de pedidos pre-ingresados para que cliente los pague
 * 
 * @author imoyano
 * 
 */
public class ViewPedidosPorPagar extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
        
		try {
            
            ResourceBundle rb = ResourceBundle.getBundle("vte");			
			HttpSession session = arg0.getSession();
			PrintWriter out = arg1.getWriter();		
			
			String pag_form = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			IValueSet top = new ValueSet();
			
            //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
			top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());
			//Se setea la variable tipo usuario
			if ( session.getAttribute("ses_tipo_usuario") != null ) {
                top.setVariable("{tipo_usuario}", session.getAttribute("ses_tipo_usuario").toString());
			} else {
				top.setVariable("{tipo_usuario}", "0");
			}

			// Nombre del comprador para header
			if ( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );		
			
			BizDelegate biz = new BizDelegate();

			// Pedidos por pagar (pre-ingresados)
			List pedidos = biz.getPedidosPorPagar( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
			List datosPedidos = new ArrayList();
			for( int i = 0; i < pedidos.size(); i++ ) {
				PedidoDTO ped = (PedidoDTO) pedidos.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{idop}", ped.getId_pedido() + "" );
                fila.setVariable("{fecha}", Formatos.frmFecha(ped.getFingreso()) );
                fila.setVariable("{monto}", Formatos.formatoPrecio( ped.getMonto() + ped.getCosto_despacho() ) );
                fila.setVariable("{montores}", Formatos.formatoPrecio( ped.getMonto_reservado() ) );
                fila.setVariable("{mpago}", Formatos.getDescripcionMedioPago(ped.getMedio_pago()) );
                fila.setVariable("{doc}", Formatos.getDescripcionTipoDocumento(ped.getTipo_doc()) );
                fila.setVariable("{cant}", Formatos.formatoNumeroSinDecimales( ped.getCant_prods() ));
                datosPedidos.add(fila);
			}
			top.setDynamicValueSets("PEDIDOS", datosPedidos );
			
			String result = tem.toString(top);
			out.print(result);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			//throw new CommandException(e);
		}

	}

}