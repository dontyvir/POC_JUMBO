package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega el formulario de productos del pedido por sector
 * @author mleiva
 */
public class ViewProdPedidoXSectorForm extends Command{
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		long id_pedido=0;
		long id_sector=0;
		long id_local=0;
		String sector = "";

		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parámetros
		if ( req.getParameter("id_pedido") != null )
			id_pedido =  new Long(req.getParameter("id_pedido")).longValue();
		logger.debug("id_pedido: " + id_pedido);
		if ( req.getParameter("id_sector") != null )
			id_sector =  new Long(req.getParameter("id_sector")).longValue();
		logger.debug("id_sector: " + id_sector);
		id_local =  usr.getId_local();
		logger.debug("id_local: " + id_local);
		
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();

		//obtener el producto
			//List lst_prod= bizDelegate.getProdPedidoXSector(id_pedido, id_sector, id_local);
			List lst_prod= bizDelegate.getProdSinPickearXPedidoXSector(id_pedido, id_sector, id_local);
			
			List productos = new ArrayList();
			logger.debug("listado en:"+lst_prod.size());
			double tot = 0;
			for (int i = 0; i < lst_prod.size(); i++) {
				IValueSet fila = new ValueSet();
				ProductosPedidoDTO prod = (ProductosPedidoDTO) lst_prod.get(i);
				//if(prod.getCant_spick()!=0){
				fila.setVariable("{id_producto}"	, String.valueOf(prod.getId_producto()));
				fila.setVariable("{cod_prod}"		, prod.getCod_producto());
				fila.setVariable("{uni_med}"		, prod.getUnid_medida());
				fila.setVariable("{descripcion}"	, prod.getDescripcion());
				fila.setVariable("{cant_spick}"		, String.valueOf(prod.getCant_spick()));
				fila.setVariable("{NombreUsuario}", prod.getNombreUsuario());
				tot = Formatos.formatoNum3Dec(tot + prod.getCant_spick());
				fila.setVariable("{cant_tot}"		, String.valueOf(tot));
				fila.setVariable("{i}"				, String.valueOf(i));
				sector = prod.getSector();				
				productos.add(fila);
				//}
			}
			top.setDynamicValueSets("productos", productos);	

		top.setVariable("{id_pedido}", String.valueOf(id_pedido));
		top.setVariable("{id_sector}", String.valueOf(id_sector));
		top.setVariable("{sector}", String.valueOf(sector));
		top.setVariable("{id_local}", String.valueOf(id_local));
			
		// 6. Setea variables bloques	

		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
