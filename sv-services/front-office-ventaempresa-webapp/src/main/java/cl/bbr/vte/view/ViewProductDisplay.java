package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.ProductoDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO;
import cl.bbr.vte.utils.vteFormatos;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * <p>Página que muestra la ficha del producto.</p>
 * <p>Si el producto es genérico despliega solamente el producto</p>  
 * <p>Si el producto no es genérico despliega los producto con items</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewProductDisplay extends Command {

	/**
	 * Despliega la ficha del producto
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
			//throws CommandException {


		// Carga properties
		ResourceBundle rb = ResourceBundle.getBundle("vte");			
		
		// Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();		
		
		
		// Recupera pagina desde web.xml y se inicia parser
		String pag_form = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("pag_form");
		logger.debug( "Template:"+pag_form );
		TemplateLoader load = new TemplateLoader(pag_form);
		ITemplate tem = load.getTemplate();
		
			
		IValueSet top = new ValueSet();

		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();
		
		// Recuperara identificador único para el local de la dirección de despacho de la cotización
		long local_id = 0;
		try {
			//CotizacionesDTO cotizacion = biz.getCotizacionesById( Long.parseLong(session.getAttribute("ses_cot_id").toString()) );
		    long id_dir = biz.getDireccionByCotizacion( Long.parseLong(session.getAttribute("ses_cot_id").toString()) );
			local_id = biz.getLocalDireccion( id_dir );
			//local_id = direccion.getLoc_cod().longValue();
		} catch (Exception e) {
			logger.error("Problemas con la recuperación del ID del local.");
			logger.debug(e.getMessage());
			throw new Exception(e);
		}		
		
		ProductosCriteriaDTO pro = new ProductosCriteriaDTO();
		
		pro.setId_cotizacion(Long.parseLong(session.getAttribute("ses_cot_id").toString()));
		pro.setId_producto(Long.parseLong(arg0.getParameter("pro_id").toString()));
		pro.setId_local(local_id);


		List L_producto = biz.getListProductosByCriteria( pro );
		
		ProductoDTO producto = null;
		List list_productos = new ArrayList();
		
		//List arr_productos = new ArrayList();
		for( int i = 0; i < L_producto.size(); i++ ) {
			producto = (ProductoDTO)L_producto.get(i);
			IValueSet ivs_productos = new ValueSet();				
			IValueSet ivs_producto_datos = new ValueSet();

			List list_datos = new ArrayList();
			
			if( producto.getGenerico().equals("G") == false ) { // Datos de productos sin items			
				ivs_producto_datos.setVariable("{pro_imagen}", producto.getImg_grande()+"");
				ivs_producto_datos.setVariable("{pro_id}", producto.getPro_id()+"");
				ivs_producto_datos.setVariable("{pro_tipo}", producto.getTipo_producto()+"");
				ivs_producto_datos.setVariable("{pro_marca}", producto.getMarca()+"");
				ivs_producto_datos.setVariable("{pro_desc}", producto.getDescripcion()+"");				
				ivs_producto_datos.setVariable("{ppum}", vteFormatos.formatoPrecio(producto.getPpum()) );
				ivs_producto_datos.setVariable("{ppum_med}", producto.getUnidad_nombre()+"");
				ivs_producto_datos.setVariable("{unidad}", vteFormatos.formatoUnidad(producto.getTipre()) );
				ivs_producto_datos.setVariable("{precio}", vteFormatos.formatoPrecio(producto.getPrecio()) + "" );
				if (producto.getCantidad() == 0)
					top.setVariable("{cantidad}", producto.getInter_valor()+"");
				else
					top.setVariable("{cantidad}", producto.getCantidad()+"");
				
				top.setVariable("{unidad}", vteFormatos.formatoUnidad(producto.getTipre()));
				top.setVariable("{marca}", producto.getMarca()+"");
				top.setVariable("{ppum_med}", producto.getUnidad_nombre()+"");
				top.setVariable("{ppum}", vteFormatos.formatoPrecio(producto.getPpum()) );		
				if( producto.isEn_carro() == false ) 
					ivs_producto_datos.setVariable("{img_fila}", "agregar.gif" );
				else
					ivs_producto_datos.setVariable("{img_fila}", "modificar.gif" );
				if (producto.getCantidad() == 0)
					ivs_producto_datos.setVariable("{valor}", vteFormatos.formatoIntervalo(producto.getInter_valor())+"");
				else
					ivs_producto_datos.setVariable("{valor}", vteFormatos.formatoIntervalo(producto.getCantidad()) + "");
				
				ivs_producto_datos.setVariable("{intervalo}", producto.getInter_valor() + "");
				
				list_datos.add(ivs_producto_datos);
				ivs_productos.setDynamicValueSets("SIN_ITEMS", list_datos );
				
				
			}else { // Datos de productos con items
				
				if( producto.getProductosDTO() == null || producto.getProductosDTO().size() < 1 )
					continue;
				
				ivs_producto_datos.setVariable("{pro_imagen}", producto.getImg_grande()+"");
				ivs_producto_datos.setVariable("{pro_id}", producto.getPro_id()+"");
				ivs_producto_datos.setVariable("{pro_tipo}", producto.getTipo_producto()+"");
				ivs_producto_datos.setVariable("{pro_marca}", producto.getMarca()+"");
				ivs_producto_datos.setVariable("{pro_desc}", producto.getDescripcion()+"");
				
				List listado_precios = new ArrayList();
				List listado_values = new ArrayList();
				long contador = 0;
				
				// Recuperar items de los productos
				List list_items = new ArrayList();
				list_items = producto.getProductosDTO();
				
				for( int f = 0; f < list_items.size(); f++ ) {
					ProductoDTO item = (ProductoDTO)list_items.get(f);

					IValueSet ivs_values = new ValueSet();
					ivs_values.setVariable("{pro_id_P}", producto.getPro_id()+"");
					ivs_values.setVariable("{pro_id}", item.getPro_id()+"");
					ivs_values.setVariable("{ppum}", vteFormatos.formatoPrecio(item.getPpum()) );
					ivs_values.setVariable("{ppum_med}", item.getUnidad_nombre()+"");
					ivs_values.setVariable("{unidad}", vteFormatos.formatoUnidad(item.getTipre()) );
					ivs_values.setVariable("{precio}", vteFormatos.formatoPrecio(item.getPrecio()) + "" );
					
					if( item.isEn_carro() == false ) 
						ivs_producto_datos.setVariable("{img_fila}", "agregar.gif" );
					else
						ivs_producto_datos.setVariable("{img_fila}", "modificar.gif" );
					
					
					if (item.getCantidad() == 0)
						ivs_values.setVariable("{valor}", vteFormatos.formatoIntervalo(item.getInter_valor())+"");
					else
						ivs_values.setVariable("{valor}", vteFormatos.formatoIntervalo(item.getCantidad()) + "");
					ivs_values.setVariable("{contador}", contador+"");
					ivs_values.setVariable("{dif}",item.getValor_diferenciador());
					ivs_values.setVariable("{intervalo}", item.getInter_valor() + "");
					contador++;
					listado_values.add(ivs_values);
					listado_precios.add(ivs_values);
					ivs_producto_datos.setDynamicValueSets("CON_ITEMS_PRECIOS", listado_precios );
				}
				
				ivs_producto_datos.setVariable("{tot_reg}", contador+"");
				list_datos.add(ivs_producto_datos);
				ivs_productos.setDynamicValueSets("CON_ITEMS", list_datos );
				
			}
			
			list_productos.add(ivs_productos);
			
		}
		
		top.setDynamicValueSets("L_PRODUCTOS", list_productos);
		
		String result = tem.toString(top);

		out.print(result);

	}

}