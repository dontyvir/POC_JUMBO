package cl.bbr.boc.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * formulario que permite ingresar datos para modificar un documento de pago de un pedido
 * @author BBRI
 */
public class ViewModCantidadProductos extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		String html = path_html + getServletConfig().getInitParameter("TplFile");
		
		if ( req.getParameter("id_pedido") == null ){
			logger.error("Parámetro id_pedido es obligatorio");
			throw new ParametroObligatorioException("id_pedido es null");
		}	
		if ( req.getParameter("id_detalle") == null ){
			logger.error("Parámetro id_detalle es obligatorio");
			throw new ParametroObligatorioException("id_detalle es null");
		}
        if ( req.getParameter("id_prod") == null ){
            logger.error("Parámetro id_prod es obligatorio");
            throw new ParametroObligatorioException("id_prod es null");
        }
        if ( req.getParameter("cant_ped") == null ){
            logger.error("Parámetro cant_ped es obligatorio");
            throw new ParametroObligatorioException("cant_ped es null");
        }
        
		long idPedido = Long.parseLong(req.getParameter("id_pedido"));
        long idDetallePedido = Long.parseLong(req.getParameter("id_detalle"));
        long idProducto = Long.parseLong(req.getParameter("id_prod"));
        double cantidad = Double.parseDouble(req.getParameter("cant_ped"));
        
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//datos asociados al Medio de Pago
		BizDelegate biz = new BizDelegate();
        
        ProductosDTO prod = biz.getProductosById(idProducto);
        
		top.setVariable("{id_producto}", ""+idProducto);
        top.setVariable("{descripcion}", prod.getTipo() + ". " + prod.getDesc_corta());
        
        Double sCantidad = new Double(cantidad);
        if ( sCantidad.intValue() == cantidad ) {
            top.setVariable("{cant_original}", "" + sCantidad.intValue());
        } else {
            top.setVariable("{cant_original}", "" + cantidad);
        }
        
        top.setVariable("{id_pedido}", ""+idPedido);
        top.setVariable("{id_detalle_pedido}", ""+idDetallePedido);
        
        IValueSet fila_lista_sel = new ValueSet();

        List aux_lista = new ArrayList();

        for (double h = 0; h <= cantidad; h += prod.getInter_valor()) {
            IValueSet aux_fila = new ValueSet();
            aux_fila.setVariable("{valor}", Formatos.formatoIntervalo(h) + "");
            aux_fila.setVariable("{opcion}", Formatos.formatoIntervalo(h) + "");
            if( Formatos.formatoIntervalo(h).compareTo(Formatos.formatoIntervalo(cantidad)) == 0 ){
                aux_fila.setVariable("{selected}", "selected");
            }else
                aux_fila.setVariable("{selected}", "");
            aux_lista.add(aux_fila);
        }

        fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);
        
        List aux_blanco = new ArrayList();
        aux_blanco.add(fila_lista_sel);
        top.setDynamicValueSets("LISTA_SEL", aux_blanco);
        
        if ( req.getParameter("msj") == null ) {
            top.setVariable("{msj}", "");
        } else {
            top.setVariable("{msj}", req.getParameter("msj"));    
        }
		
		String result = tem.toString(top);	
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}