package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.vte.cotizaciones.dto.CategoriaDTO;
import cl.bbr.vte.cotizaciones.dto.MarcaDTO;
import cl.bbr.vte.cotizaciones.dto.ProductoDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO;
import cl.bbr.vte.utils.vteFormatos;
import cl.bbr.vte.bizdelegate.BizDelegate;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página para presentar los productos de la categoría seleccionada para el paso 2.
 * 
 * Presenta el listado de productos.
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewListCatProductos extends Command {

	/**
	 * Presenta el listado de productos.
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

		try {

			// Revisión parámetros obligatorios
			ArrayList campos = new ArrayList();
			campos.add("id_cat");
			campos.add("id_cat_padre");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}
			
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("vte");

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();

			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();

			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.path.html") + ""
					+ getServletConfig().getInitParameter("pag_form");
			logger.debug("Template:" + pag_form);
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();

	        //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
			top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());

			// Nombre del comprador para header
			if (session.getAttribute("ses_com_nombre") != null)
				top.setVariable("{nombre_comprador}", session
						.getAttribute("ses_com_nombre"));
			else
				top.setVariable("{nombre_comprador}", "");

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			long id_categoria = Long.parseLong(arg0.getParameter("id_cat"));
			top.setVariable("{id_cat}", id_categoria+"");
			long id_categoria_padre = Long.parseLong(arg0.getParameter("id_cat_padre"));
			top.setVariable("{id_cat_padre}", id_categoria_padre+"");			
			
			// Obtener datos de la categoría
			CategoriaDTO categoria = biz.getCategoriaById( id_categoria );
			// Obtener datos de la subcategoría
			CategoriaDTO categoria_padre = biz.getCategoriaById( id_categoria_padre );
			// Setear el nombre de la categoría
			top.setVariable("{cat_nombre}", categoria_padre.getNombre()+"/"+categoria.getNombre());
			
			// Obtener datos de las marcas para los productos de la categoría
			List l_marcas = biz.getListMarcasByCategoria(id_categoria);
			// Despliegue de marcas
			List arr_marcas = new ArrayList();
			MarcaDTO marca = null;
			for( int i = 0; i < l_marcas.size(); i++ ) {
				marca = (MarcaDTO)l_marcas.get(i);
				IValueSet aux_fila = new ValueSet();
				aux_fila.setVariable("{nombre}", marca.getMar_nombre()+"");
				aux_fila.setVariable("{valor}", marca.getMar_id()+"");
				if( arg0.getParameter("marcas") != null && Long.parseLong(arg0.getParameter("marcas").toString()) == marca.getMar_id() )
					aux_fila.setVariable("{seleccion}", "1");
				else
					aux_fila.setVariable("{seleccion}", "0");
				arr_marcas.add(aux_fila);				
			}
			top.setDynamicValueSets("FIL_MARCAS", arr_marcas);			
			
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
		
			// Obtener datos de los productos
			ProductosCriteriaDTO criterio = new ProductosCriteriaDTO();
			criterio.setId_categoria( id_categoria );
			criterio.setId_local( local_id );
			if( arg0.getParameter("order") != null )
				criterio.setOrdenamiento( Long.parseLong(arg0.getParameter("order").toString()) );
			if( arg0.getParameter("marcas") != null )
				criterio.setId_marca( Long.parseLong(arg0.getParameter("marcas").toString()) );
			if( session.getAttribute("ses_cot_id") != null )
				criterio.setId_cotizacion( Long.parseLong(session.getAttribute("ses_cot_id").toString()) );
			
			List l_productos = biz.getListProductosByCriteria( criterio );
			// Presentar los productos de la categoría
			ProductoDTO producto = null;
			List list_productos = new ArrayList();
			
			for( int i = 0; i < l_productos.size(); i++ ) {
				producto = (ProductoDTO)l_productos.get(i);
				IValueSet ivs_productos = new ValueSet();				
				IValueSet ivs_producto_datos = new ValueSet();

				List list_datos = new ArrayList();
				
				if( producto.getGenerico().equals("G") == false ) { // Datos de productos sin items
					//long temp_id = Long.parseLong(session.getAttribute("ses_cot_id").toString());
				    if (producto.getDcot_id() > 0){
				        ivs_producto_datos.setVariable("{id}", producto.getDcot_id()+"");
				    }else{
				        ivs_producto_datos.setVariable("{id}", "0");
				    }
				    ivs_producto_datos.setVariable("{pro_imagen}", producto.getImg_chica()+"");
					ivs_producto_datos.setVariable("{pro_id}", producto.getPro_id()+"");
					ivs_producto_datos.setVariable("{pro_tipo}", producto.getTipo_producto()+"");
					ivs_producto_datos.setVariable("{pro_marca}", producto.getMarca()+"");
					ivs_producto_datos.setVariable("{pro_desc}", producto.getDescripcion()+"");				
					ivs_producto_datos.setVariable("{ppum}", vteFormatos.formatoPrecio(producto.getPpum()) );
					ivs_producto_datos.setVariable("{ppum_med}", producto.getUnidad_nombre()+"");
					ivs_producto_datos.setVariable("{unidad}", vteFormatos.formatoUnidad(producto.getTipre()) );
					ivs_producto_datos.setVariable("{precio}", vteFormatos.formatoPrecio(producto.getPrecio()) + "" );
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
				} else { // Datos de productos con items
					
					if( producto.getProductosDTO() == null || producto.getProductosDTO().size() < 1 )
						continue;
					
					ivs_producto_datos.setVariable("{pro_imagen}", producto.getImg_chica()+"");
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
						if( item.isEn_carro() == true ) {
							producto.setEn_carro(true);
						}
					}

					if( producto.isEn_carro() == false ) 
						ivs_producto_datos.setVariable("{img_fila}", "agregar.gif" );
					else
						ivs_producto_datos.setVariable("{img_fila}", "modificar.gif" );					
					
					ivs_producto_datos.setVariable("{tot_reg}", contador+"");
					list_datos.add(ivs_producto_datos);
					ivs_productos.setDynamicValueSets("CON_ITEMS", list_datos );
					
				}
				
				list_productos.add(ivs_productos);
				
			}
			
			top.setDynamicValueSets("L_PRODUCTOS", list_productos);
			
			String result = tem.toString(top);

			out.print(result);

		} catch (ParametroObligatorioException e) {
			logger.debug( "Captura error pero no presenta error. " + e.getMessage());
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

	}

}