package cl.bbr.fo.command;

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
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;

/**
 * Página de tracking de OP's para clientes
 * 
 * @author imoyano
 * 
 */
public class TrackingOP extends Command {
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("Sustitutos", arg0);

			// Recupera la sesion del usuario
			HttpSession session = arg0.getSession();
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
            
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
            String volver = "javascript:history.back();";
            if ( arg0.getParameter("from") != null ) {
                volver = arg0.getParameter("from").toString();
            }
            top.setVariable("{from}", volver);
            
            long idCliente = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			
            if(session.getAttribute("ses_cli_nombre").toString().indexOf(" ") != -1) {
        		top.setVariable("{nombre_cliente}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
        	} else {
			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
        	}    
			//top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
            
            List ops = biz.clienteListaOPsTracking(idCliente);
            
            List theOps = new ArrayList();
            
            if(ops.size() > 0 ){
            for ( int i = 0; i < ops.size(); i++ ) {
                PedidoDTO op = (PedidoDTO) ops.get(i);
                IValueSet filaOp = new ValueSet();
                //String idOp = Utils.numOP(Long.parseLong(rb.getString("op.funcion.transformacion.a")),Long.parseLong(rb.getString("op.funcion.transformacion.b")),op.getId_pedido() );
                filaOp.setVariable("{id_op}", op.getId_pedido() + op.getSecuenciaPago() );
                    
                if (i == 0) {

						List mostarEstadoPedido = new ArrayList(); 

						IValueSet filaEstadoPedido = new ValueSet();

						filaEstadoPedido.setVariable("{id_op}", op.getId_pedido() + op.getSecuenciaPago()); 
						filaEstadoPedido.setVariable("{estado}", op.getEstado() );

						mostarEstadoPedido.add(filaEstadoPedido);
						top.setDynamicValueSets("ESTADO_PEDIDO", mostarEstadoPedido);

                }
                    
                filaOp.setVariable("{fecha_compra}", cl.bbr.jumbocl.common.utils.Utils.cambiaFormatoFecha( op.getFingreso() ) );
                filaOp.setVariable("{fecha_entrega}", cl.bbr.jumbocl.common.utils.Utils.cambiaFormatoFecha( op.getFdespacho() ) );
                if (op.getId_estado() == 20) {
                    filaOp.setVariable("{img}", "" );
                    filaOp.setVariable("{color}", "red" );
                } else {
                    filaOp.setVariable("{img}", "<img src=\"/FO_IMGS/img/estructura/tracking_op/ver_off.gif\" name=\"track_" + op.getId_pedido() + op.getSecuenciaPago() + "\" id=\"track_" + op.getId_pedido() + op.getSecuenciaPago() + "\" border=\"0\"/>" );
                    filaOp.setVariable("{color}", "black" );
                }
                filaOp.setVariable("{estado}", op.getEstado() );
                theOps.add( filaOp );
            }
            	
            } else{
				
				List trackingVacioList = new ArrayList(); 

				IValueSet filatrackingVacio = new ValueSet();

				filatrackingVacio.setVariable("{tracking_vacio}", rb.getString("tracking.vacio")); 

				trackingVacioList.add(filatrackingVacio);				
				top.setDynamicValueSets("TRACKING_VACIO", trackingVacioList);
			}
            
            top.setDynamicValueSets("OP_TRACKING", theOps);
          
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException(e);
		}
	}

}