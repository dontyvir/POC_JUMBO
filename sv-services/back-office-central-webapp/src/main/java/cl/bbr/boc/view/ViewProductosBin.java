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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.ProductosBinDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega los productos que están en los bins
 * @author BBRI
 */
public class ViewProductosBin extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String	param_id_bp = "";
		long	id_bp		=-1;
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		if ( req.getParameter("id_bp") == null ){
			throw new ParametroObligatorioException("id_bp es null");
		}		
		param_id_bp	= req.getParameter("id_bp");
		id_bp = Long.parseLong(param_id_bp);
		logger.debug("id_bp: " + param_id_bp);

		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		
		BizDelegate bizDelegate = new BizDelegate();
		
		//4.1 Detalle Bin
		List prodsBin = new ArrayList();
		prodsBin = bizDelegate.getProductosBin( id_bp );

		ArrayList bin = new ArrayList();
		for (int i=0; i<prodsBin.size(); i++){
			ProductosBinDTO bin1 = new ProductosBinDTO();
			bin1 = (ProductosBinDTO)prodsBin.get(i);
			IValueSet fila = new ValueSet();
			logger.debug("idprod:"+bin1.getCod_producto());
			if(bin1.getCod_producto()!=null){
				fila.setVariable("{cod_prod}", bin1.getCod_producto());
				fila.setVariable("{cod_sap}" , bin1.getCod_prod_sap());
				fila.setVariable("{uni_med}" , bin1.getUni_med());
			}else{
				fila.setVariable("{cod_prod}", Constantes.SIN_DATO);
				fila.setVariable("{cod_sap}" , Constantes.SIN_DATO);
				fila.setVariable("{uni_med}" , Constantes.SIN_DATO);
			}
			fila.setVariable("{cbarra}"     , bin1.getCbarra());			
			fila.setVariable("{descripcion}", bin1.getDescripcion());
			fila.setVariable("{cant}"       , String.valueOf(bin1.getCantidad()));
			fila.setVariable("{auditado}"   , bin1.getAuditado()+"");
			bin.add(fila);
		}
		
		// 5. Setea variables del template
		//top.setVariable("{id_pedido}", param_id_pedido);
		top.setVariable("{id_pedido}", "");
		top.setVariable("{cod_bin}"  , "");
		
		
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_contenido_bodega", bin);
		
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
		
		
	
	}	
	
}
