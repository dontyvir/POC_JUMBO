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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;

/**
 * Página que entrega los productos de la categoría de búsqueda
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class SearchProList extends Command {

	/**
	 * Despliega productos
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
			
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
		    TemplateLoader load = new TemplateLoader(pag_form);
		    
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();		

			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());

			long categoria_id = Long.parseLong(arg0.getParameter("idcategoria"));

			String marca = "";
			String orden = "";
			if( arg0.getParameter("mar_id")!=null )
				marca = arg0.getParameter("mar_id").toString(); 
			if( arg0.getParameter("orden")!=null )
				orden = arg0.getParameter("orden").toString();			
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			List list_patrones = new ArrayList();			
			if( arg0.getParameter("patron")!=null  ) {
				String[] patrones = arg0.getParameter("patron").split("[\\s]+");
				for (int j=0; j < (patrones.length ); j++){
					list_patrones.add(patrones[j].trim().toUpperCase());
				}
			}
			
			// Recuperar los cupones y los tcp de la sesión
			List l_torec = new ArrayList();
			List l_tcp = null;
			if( session.getAttribute("ses_promo_tcp") != null ) {
				l_tcp = (List)session.getAttribute("ses_promo_tcp");
				l_torec.addAll(l_tcp);
			}
			if( session.getAttribute("ses_cupones") != null ) {
				List l_cupones = (List)session.getAttribute("ses_cupones");
				l_torec.addAll(l_cupones);
			}
			
			List list_prod = biz.getSearchListSeccion( session.getAttribute("ses_loc_id").toString(), categoria_id, cliente_id, marca, orden, list_patrones, l_torec );
			List arr_productos = new ArrayList();
			long contador = 0;
			boolean isEn_carro = false;
			for( int i = 0; i < list_prod.size() ; i++ ) {
				isEn_carro = false;
				contador = 0;
				ProductoDTO data = (ProductoDTO) list_prod.get(i);
				IValueSet fila = new ValueSet();
								
				fila.setVariable("{contador_form}",  i+"");
				fila.setVariable("{contador}",  contador+"");
				fila.setVariable("{pro_imagen}",  data.getImg_chica()+"");
				fila.setVariable("{pro_id}", data.getPro_id()+"");
				fila.setVariable("{pro_tipo}", data.getTipo_producto()+"");
				fila.setVariable("{pro_marca}", data.getMarca()+"");
				fila.setVariable("{pro_desc}", data.getDescripcion()+"");				
				
				// Revisar si existen promociones para el producto
				if( data.getListaPromociones() != null && data.getListaPromociones().size() > 0 ) {
					this.getLogger().info( "Existen Promociones:" + data.getListaPromociones().size() );
					// Si sólo tiene una promoción se presenta el banner asociado.
					List lst_promo = data.getListaPromociones();
					if( lst_promo.size() == 1 ) {
						PromocionDTO promocion = (PromocionDTO) lst_promo.get(0);
						List show_lst_promo = new ArrayList();
						IValueSet ivs_promo = new ValueSet();
						ivs_promo.setVariable( "{promo_img}", promocion.getBanner() );
						ivs_promo.setVariable( "{promo_desc}", promocion.getDescr() );
						show_lst_promo.add(ivs_promo);
						fila.setDynamicValueSets("CON_PROMOCION", show_lst_promo);
					}
					else if( lst_promo.size() > 1 ) { // Tiene más de una promoción se presenta enlace con popup
						List show_lst_promo = new ArrayList();
						IValueSet ivs_promo = new ValueSet();
						ivs_promo.setVariable( "{promo_img}", "" );
						ivs_promo.setVariable("{pro_id}", data.getPro_id()+"");
						show_lst_promo.add(ivs_promo);
						fila.setDynamicValueSets("CON_N_PROMOCION", show_lst_promo);
					}
				}				
				
				if(data.getGenerico().compareTo("G") == 0 ) {
					List data_items = data.getProductosDTO();					
					List lista_items = new ArrayList();
					List lista_items_sel = new ArrayList();					
					
					for(int v = 0; v < data_items.size(); v++ ) {
						ProductoDTO item = (ProductoDTO) data_items.get(v);
						IValueSet valueset_items = new ValueSet();
						
						if (item.getEsParticionable().equalsIgnoreCase("S")) {
						    valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(item.getPrecio() / item.getParticion()) + "" );
							valueset_items.setVariable("{unidad}", " 1/" + item.getParticion() + " " + item.getTipre()+"");
						} else {
						    valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(item.getPrecio()) + "" );
							valueset_items.setVariable("{unidad}", item.getTipre()+"");
						}
						
						valueset_items.setVariable("{pro_ppum}", Formatos.formatoPrecioFO(item.getPpum()) + "" );
						valueset_items.setVariable("{pro_uni_med_ppum}", item.getUnidad_nombre()+"");
						lista_items.add(valueset_items);
						
						IValueSet valueset_items_sel = new ValueSet();
						// Si el producto es con seleccion
						if (item.getUnidad_tipo().compareTo("S") == 0) {

							IValueSet fila_lista_sel = new ValueSet();

							List aux_lista = new ArrayList();
							for (double h = 0; h <= item.getInter_maximo(); h += item.getInter_valor()) {
								IValueSet aux_fila = new ValueSet();
								aux_fila.setVariable("{valor}", Formatos.formatoIntervaloFO(h) + "");
								aux_fila.setVariable("{opcion}", Formatos.formatoIntervaloFO(h) + "");
								if( Formatos.formatoIntervaloFO(h).compareTo(Formatos.formatoIntervaloFO(item.getCantidad())) == 0 ){
									aux_fila.setVariable("{selected}", "selected");
								}else
									aux_fila.setVariable("{selected}", "");
								aux_lista.add(aux_fila);
							}

							fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);
							fila_lista_sel.setVariable("{contador}", contador+"" );
							
							List aux_blanco = new ArrayList();
							aux_blanco.add(fila_lista_sel);
							
							valueset_items_sel.setDynamicValueSets("LISTA_SEL", aux_blanco);
						} else {
							IValueSet fila_lista_sel = new ValueSet();
							if (Formatos.formatoCantidadFO(item.getCantidad()) == 0)
								fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(item.getInter_valor() - item.getInter_valor())+"");
							else
								fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(item.getCantidad()) + "");
							
							fila_lista_sel.setVariable("{contador}", contador+"" );
							fila_lista_sel.setVariable("{contador_form_aux}",  i+"");
							fila_lista_sel.setVariable("{maximo}", item.getInter_maximo() + "");
							fila_lista_sel.setVariable("{intervalo}", item.getInter_valor() + "");
							List aux_blanco = new ArrayList();
							aux_blanco.add(fila_lista_sel);
							valueset_items_sel.setDynamicValueSets("INPUT_SEL",aux_blanco);
						}
						valueset_items_sel.setVariable("{pro_id}", item.getPro_id()+"");
						valueset_items_sel.setVariable("{unidad}", item.getTipre()+"");
						valueset_items_sel.setVariable("{contador}", contador+"" );
						valueset_items_sel.setVariable("{dif}", item.getValor_diferenciador()+"" );						
						lista_items_sel.add(valueset_items_sel);
						
						if( item.isEn_carro() == true )
							isEn_carro = true;
						
						contador++;
						
					}					
					
					fila.setDynamicValueSets("ITEMS", lista_items);
					fila.setDynamicValueSets("ITEMS_SEL", lista_items_sel);
					
				} else { // Producto sin item
					List lista_items = new ArrayList();
					IValueSet valueset_items = new ValueSet();
					
					if (data.getEsParticionable().equalsIgnoreCase("S")) {
					    valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(data.getPrecio() / data.getParticion()) + "" );
						valueset_items.setVariable("{unidad}", " 1/" + data.getParticion() + " " + data.getTipre()+"");
					} else {
					    valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(data.getPrecio()) + "" );
						valueset_items.setVariable("{unidad}", data.getTipre()+"");
					}
					
					valueset_items.setVariable("{pro_ppum}", Formatos.formatoPrecioFO(data.getPpum()) + "" );
					valueset_items.setVariable("{pro_uni_med_ppum}", data.getUnidad_nombre()+"");
					lista_items.add(valueset_items);					
					fila.setDynamicValueSets("SIN_ITEMS_UNI", lista_items);					
					fila.setDynamicValueSets("SIN_ITEMS", lista_items);
					
					/** INI Selección de cantidad de productos **/
					// Valueset para la lista de selección
					IValueSet valueset_items_sel = new ValueSet();				
					
					// Si el producto es con seleccion
					long cont = 0;
					if (data.getUnidad_tipo().compareTo("S") == 0) {

						IValueSet fila_lista_sel = new ValueSet();

						List aux_lista = new ArrayList();
						for (double v = 0; v <= data.getInter_maximo(); v += data.getInter_valor()) {
							IValueSet aux_fila = new ValueSet();
							aux_fila.setVariable("{valor}", Formatos.formatoIntervaloFO(v) + "");
							aux_fila.setVariable("{opcion}", Formatos.formatoIntervaloFO(v) + "");
							if( Formatos.formatoIntervaloFO(v).compareTo(Formatos.formatoIntervaloFO(data.getCantidad())) == 0 ){
								aux_fila.setVariable("{selected}", "selected");
							}else if (cont == 1 && (data.getCantidad() == 0.0))
								aux_fila.setVariable("{selected}", "selected");
							else	
								aux_fila.setVariable("{selected}", "");
							
							aux_lista.add(aux_fila);
							cont++;
						}

						fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);
						fila_lista_sel.setVariable("{contador}", contador+"" );
						
						List aux_blanco = new ArrayList();
						aux_blanco.add(fila_lista_sel);
						
						valueset_items_sel.setDynamicValueSets("LISTA_SEL", aux_blanco);
					} else {
						IValueSet fila_lista_sel = new ValueSet();
						if (Formatos.formatoCantidadFO(data.getCantidad()) == 0)
							fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(data.getInter_valor())+"");
						else
							fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(data.getCantidad()) + "");
						
						fila_lista_sel.setVariable("{contador}", contador+"" );
						fila_lista_sel.setVariable("{contador_form_aux}",  i+"");
						fila_lista_sel.setVariable("{maximo}", data.getInter_maximo() + "");
						fila_lista_sel.setVariable("{intervalo}", data.getInter_valor() + "");
						List aux_blanco = new ArrayList();
						aux_blanco.add(fila_lista_sel);
						valueset_items_sel.setDynamicValueSets("INPUT_SEL",aux_blanco);
					}
				
					List lista_items_sel = new ArrayList();
					valueset_items_sel.setVariable("{pro_id}", data.getPro_id()+"");
					valueset_items_sel.setVariable("{unidad}", data.getTipre()+"" );
					valueset_items_sel.setVariable("{contador}", contador+"" );
					valueset_items_sel.setVariable("{dif}", "" );
					lista_items_sel.add(valueset_items_sel);
					fila.setDynamicValueSets("ITEMS_SEL", lista_items_sel);
					/** FIN Selección de cantidad de productos **/
					
					if( data.isEn_carro() == true )
						isEn_carro = true;
					
					contador++;
					
				}
				
				IValueSet lista_boton = new ValueSet();
				lista_boton.setVariable("{contador_form}",  i+"");	
				lista_boton.setVariable("{nota}", data.getNota()+"");
				
				List list_boton = new ArrayList();
				list_boton.add(lista_boton);
				
				if( isEn_carro == true )
					fila.setDynamicValueSets("IMG_MODIFICAR", list_boton );
				else
					fila.setDynamicValueSets("IMG_AGREGAR", list_boton );				

				if (data.getNota() == null)
					lista_boton.setVariable("{nota}", "");
				else
					lista_boton.setVariable("{nota}", data.getNota()+"");
				
				if( data.isCon_nota() == true ){
					lista_boton.setVariable("{pro_id}", data.getPro_id()+"");
					fila.setDynamicValueSets("IMG_COMEN", list_boton );
				}
				
				fila.setVariable("{total_productos}", contador+"");
				
				arr_productos.add(fila);
			}
			top.setDynamicValueSets("PRODUCTOS", arr_productos);
			
			String result = tem.toString(top);

			out.print(result);
			    
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}


	}

}