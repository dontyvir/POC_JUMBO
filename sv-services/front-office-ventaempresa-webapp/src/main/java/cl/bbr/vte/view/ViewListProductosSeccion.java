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
import cl.bbr.vte.cotizaciones.dto.ProductoDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO;
import cl.bbr.vte.utils.vteFormatos;
import cl.bbr.vte.bizdelegate.BizDelegate;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página para presentar los productos de la categoría seleccionando la seccion
 * 
 * Presenta el listado de productos.
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewListProductosSeccion extends Command {

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
			/*
			ArrayList campos = new ArrayList();
			campos.add("id_cat");
			campos.add("id_cat_padre");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}
			*/
			
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

			// Nombre del comprador para header
			if (session.getAttribute("ses_com_nombre") != null)
				top.setVariable("{nombre_comprador}", session
						.getAttribute("ses_com_nombre"));
			else
				top.setVariable("{nombre_comprador}", "");

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			// setear el nombre en blanco
			top.setVariable("{cat_nombre}", "");
			
			// Si es una búsqueda asociada a una categoría
			if( arg0.getParameter("id_cat") != null ) {
				long id_categoria = Long.parseLong(arg0.getParameter("id_cat"));
				top.setVariable("{id_cat}", id_categoria+"");
				long id_categoria_padre = Long.parseLong(arg0.getParameter("id_cat_padre"));
				top.setVariable("{id_cat_padre}", id_categoria_padre+"");
				// Obtener datos de la categoría
				CategoriaDTO categoria = biz.getCategoriaById( id_categoria );
				// Obtener datos de la subcategoría
				CategoriaDTO categoria_padre = biz.getCategoriaById( id_categoria_padre );
				top.setVariable("{cat_nombre}", categoria_padre.getNombre()+"/"+categoria.getNombre());
			}
			
			// Recupera identificador único para el local de la dirección de despacho de la cotización
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
			criterio.setId_local( local_id );
			//criterio.setId_categoria( id_categoria );
			if( arg0.getParameter("order") != null )
				criterio.setOrdenamiento( Long.parseLong(arg0.getParameter("order").toString()) );
			
			if( arg0.getParameter("marcas") != null )
				criterio.setId_marca( Long.parseLong(arg0.getParameter("marcas").toString()) );
			
			if( arg0.getParameter("id_cat") != null ) {
				criterio.setId_categoria( Long.parseLong(arg0.getParameter("id_cat")) );
			}
				
			if( session.getAttribute("ses_cot_id") != null )
				criterio.setId_cotizacion( Long.parseLong(session.getAttribute("ses_cot_id").toString()) );

			// Inicio de búsqueda
			List list_patrones = new ArrayList();
			int largo_patron = Integer.parseInt(rb.getString("searchitemdisplay.largopatron"));
			
			String[] patrones = vteFormatos.stringToSearch(arg0.getParameter("patron").toString()).split("[\\s]+");
			
			for (int k=0; k < (patrones.length ); k++){
				if( patrones[k].trim().compareTo("") != 0 && patrones[k].length() > 1){
					//System.out.print("largoBD = "  +patrones[k].length());
					if(patrones[k].trim().toUpperCase().length() > largo_patron){
						list_patrones.add( patrones[k].trim().toUpperCase().substring(0,largo_patron) );
					}else{
						list_patrones.add( patrones[k].trim().toUpperCase() );
					}
				}
			}			
			
			criterio.setPatrones(list_patrones);
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
					
					if( producto.isEn_carro() == false ) 
						ivs_producto_datos.setVariable("{img_fila}", "agregar.gif" );
					else
						ivs_producto_datos.setVariable("{img_fila}", "modificar.gif" );
					
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
					}
					
					ivs_producto_datos.setVariable("{tot_reg}", contador+"");
					list_datos.add(ivs_producto_datos);
					ivs_productos.setDynamicValueSets("CON_ITEMS", list_datos );
					
				}
				
				list_productos.add(ivs_productos);
				
			}
			
			top.setDynamicValueSets("L_PRODUCTOS", list_productos);
			
			// Presenta el listado de marcas de los productos
			List marcas_ok = new ArrayList(); 
			List arr_marcas = new ArrayList();
			for (int i = 0; i < l_productos.size(); i++) {
				ProductoDTO marca = (ProductoDTO) l_productos.get(i);
				if( marcas_ok.contains( marca.getMarca_id()+"" ) )
					continue;
				marcas_ok.add( marca.getMarca_id()+"" );
				IValueSet aux_fila = new ValueSet();
				aux_fila.setVariable("{nombre}", marca.getMarca()+"");
				aux_fila.setVariable("{valor}", marca.getMarca_id()+"");
				if( arg0.getParameter("marcas") != null && Long.parseLong(arg0.getParameter("marcas").toString()) == marca.getMarca_id() )
					aux_fila.setVariable("{seleccion}", "1");
				else
					aux_fila.setVariable("{seleccion}", "0");
				arr_marcas.add(aux_fila);
			}
			top.setDynamicValueSets("FIL_MARCAS", arr_marcas);
			
			String result = tem.toString(top);

			out.print(result);

		} catch (ParametroObligatorioException e) {
			logger.debug( "Captura error pero no presenta error. " + e.getMessage());
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new Exception(e);
		}

	}
}