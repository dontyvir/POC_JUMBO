package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.bizdelegate.ProductosDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.FichaNutricionalDTO;
import cl.bbr.jumbocl.contenidos.dto.FichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.ItemFichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.NutricionalLeySupeDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaProductoDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaUnidadDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;


/**
 * Ficha del producto
 *  
 * @author BBRI
 * 
 */
public class ProductDisplay extends Command {

	/**
	 * Ficha del producto
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
			
			// Recupera pagina desde web.xml y se inicia parser, ficha.html
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();		
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			long prod_id       = Long.parseLong(arg0.getParameter("idprod").toString());
			String prod_id_str = arg0.getParameter("idprod").toString();
			long cliente_id    = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			long local_id      = Long.parseLong(session.getAttribute("ses_loc_id").toString());
			
		 // Seteo datos cliente para chaordic_meta
			top.setVariable("{id_prod}", arg0.getParameter("idprod").toString());
			top.setVariable("{ses_cli_id}", (null == session.getAttribute("ses_cli_id") ? "1" : session.getAttribute("ses_cli_id")));
			top.setVariable("{ses_cli_nombre}", (null == session.getAttribute("ses_cli_nombre") ? "Invitado" : session.getAttribute("ses_cli_nombre")));
			top.setVariable("{ses_cli_email}", (null == session.getAttribute("ses_cli_email") || "".equals(session.getAttribute("ses_cli_email")) ? "invitado@invitado.cl" : session.getAttribute("ses_cli_email")));
			
            String invitado_id = "";
            if (session.getAttribute("ses_invitado_id") != null &&
                    !session.getAttribute("ses_invitado_id").toString().equals("")){
                invitado_id = session.getAttribute("ses_invitado_id").toString();
            }

			List arr_sugerido = new ArrayList();	
			List list_sugerido = biz.getSugerido( prod_id , cliente_id, local_id, invitado_id);
			
			long contador0 = 0;
			boolean isEn_carro0 = false;

			/**
			 * Sugeridos
			 */
			for( int i = 0; i < list_sugerido.size() ; i++ ) {			
				isEn_carro0 = false;
				contador0 = 0;
				ProductoDTO sugerido = (ProductoDTO) list_sugerido.get(i);
				IValueSet fila = new ValueSet();
								
				fila.setVariable("{contador_formSug}",  i+"");
				fila.setVariable("{contadorSug}",  contador0+"");
				fila.setVariable("{pro_imagenSug}",  sugerido.getImg_chica()+"");
				fila.setVariable("{pro_idSug}", sugerido.getPro_id()+"");
				fila.setVariable("{pro_tipoSug}", sugerido.getTipo_producto()+"");
				fila.setVariable("{pro_marcaSug}", sugerido.getMarca()+"");
				fila.setVariable("{pro_descSug}", sugerido.getDescripcion()+"");
				fila.setVariable("{notaSug}", sugerido.getNota()+"");
				
				// Producto genérico
				if(sugerido.getGenerico().compareTo("G") == 0 ) {
					List data_items = sugerido.getProductosDTO();					
					List lista_items = new ArrayList();
					List lista_items_sel = new ArrayList();					
					
					for(int v = 0; v < data_items.size(); v++ ) {
						ProductoDTO item = (ProductoDTO) data_items.get(v);
						IValueSet valueset_items = new ValueSet();
						valueset_items.setVariable("{precioSug}", Formatos.formatoPrecioFO(item.getPrecio()) + "" );
						valueset_items.setVariable("{pro_ppumSug}", Formatos.formatoPrecioFO(item.getPpum()) + "" );
						valueset_items.setVariable("{pro_uni_med_ppumSug}", item.getUnidad_nombre()+"");
						valueset_items.setVariable("{unidadSug}", item.getTipre()+"");
						lista_items.add(valueset_items);
						
						IValueSet valueset_items_sel = new ValueSet();
						// Si el producto es con seleccion
						if (item.getUnidad_tipo().compareTo("S") == 0) {

							IValueSet fila_lista_sel = new ValueSet();

							List aux_lista = new ArrayList();
							for (double h = 0; h <= item.getInter_maximo(); h += item.getInter_valor()) {
								IValueSet aux_fila = new ValueSet();
								aux_fila.setVariable("{valorSug}", Formatos.formatoIntervaloFO(h) + "");
								aux_fila.setVariable("{opcionSug}", Formatos.formatoIntervaloFO(h) + "");
								if( Formatos.formatoIntervaloFO(h).compareTo(Formatos.formatoIntervaloFO(item.getCantidad())) == 0 ){
									aux_fila.setVariable("{selectedSug}", "selected");
								}else
									aux_fila.setVariable("{selectedSug}", "");
								aux_lista.add(aux_fila);
							}

							fila_lista_sel.setDynamicValueSets("CANTIDADESSUG", aux_lista);
							fila_lista_sel.setVariable("{contadorSug}", contador0+"" );
							
							List aux_blanco = new ArrayList();
							aux_blanco.add(fila_lista_sel);
							
							valueset_items_sel.setDynamicValueSets("LISTA_SELSUG", aux_blanco);
						} else {
							IValueSet fila_lista_sel = new ValueSet();
							if (Formatos.formatoCantidadFO(item.getCantidad()) == 0)
								fila_lista_sel.setVariable("{valorSug}", Formatos.formatoIntervaloFO(item.getInter_valor())+"");
							else
								fila_lista_sel.setVariable("{valorSug}", Formatos.formatoIntervaloFO(item.getCantidad()) + "");
							
							fila_lista_sel.setVariable("{contadorSug}", contador0+"" );
							fila_lista_sel.setVariable("{contador_form_auxSug}",  i+"");
							fila_lista_sel.setVariable("{maximoSug}", item.getInter_maximo() + "");
							fila_lista_sel.setVariable("{intervaloSug}", item.getInter_valor() + "");
							List aux_blanco = new ArrayList();
							aux_blanco.add(fila_lista_sel);
							valueset_items_sel.setDynamicValueSets("INPUT_SELSUG",aux_blanco);
						}
						valueset_items_sel.setVariable("{pro_idSug}", item.getPro_id()+"");
						valueset_items_sel.setVariable("{unidadSug}", item.getTipre() );						
						valueset_items_sel.setVariable("{contadorSug}", contador0+"" );
						valueset_items_sel.setVariable("{difSug}", item.getValor_diferenciador()+"" );						
						lista_items_sel.add(valueset_items_sel);
						
						if( item.isEn_carro() == true )
							isEn_carro0 = true;
						
						contador0++;
						
					}					
					
					fila.setDynamicValueSets("ITEMSSUG", lista_items);
					fila.setDynamicValueSets("ITEMS_SELSUG", lista_items_sel);
					
				} else { // Producto sin item
					List lista_items = new ArrayList();
					IValueSet valueset_items = new ValueSet();
					valueset_items.setVariable("{precioSug}", Formatos.formatoPrecioFO(sugerido.getPrecio()) + "" );
					valueset_items.setVariable("{pro_ppumSug}", Formatos.formatoPrecioFO(sugerido.getPpum()) + "" );
					valueset_items.setVariable("{pro_uni_med_ppumSug}", sugerido.getUnidad_nombre()+"");
					valueset_items.setVariable("{unidadSug}", sugerido.getTipre()+"");
					lista_items.add(valueset_items);					
					fila.setDynamicValueSets("SIN_ITEMS_UNISUG", lista_items);					
					fila.setDynamicValueSets("SIN_ITEMSSUG", lista_items);
					
					/** INI Selección de cantidad de productos **/
					// Valueset para la lista de selección
					IValueSet valueset_items_sel = new ValueSet();				
					
					// Si el producto es con seleccion
					if (sugerido.getUnidad_tipo().compareTo("S") == 0) {

						IValueSet fila_lista_sel = new ValueSet();

						List aux_lista = new ArrayList();
						for (double v = 0; v <= sugerido.getInter_maximo(); v += sugerido.getInter_valor()) {
							IValueSet aux_fila = new ValueSet();
							aux_fila.setVariable("{valorSug}", Formatos.formatoIntervaloFO(v) + "");
							aux_fila.setVariable("{opcionSug}", Formatos.formatoIntervaloFO(v) + "");
							if( Formatos.formatoIntervaloFO(v).compareTo(Formatos.formatoIntervaloFO(sugerido.getCantidad())) == 0 ){
								aux_fila.setVariable("{selectedSug}", "selected");
							}else
								aux_fila.setVariable("{selectedSug}", "");
							aux_lista.add(aux_fila);
						}

						fila_lista_sel.setDynamicValueSets("CANTIDADESSUG", aux_lista);
						fila_lista_sel.setVariable("{contadorSug}", contador0+"" );
						
						List aux_blanco = new ArrayList();
						aux_blanco.add(fila_lista_sel);
						
						valueset_items_sel.setDynamicValueSets("LISTA_SELSUG", aux_blanco);
					} else {
						IValueSet fila_lista_sel = new ValueSet();
						if (Formatos.formatoCantidadFO(sugerido.getCantidad()) == 0)
							fila_lista_sel.setVariable("{valorSug}", Formatos.formatoIntervaloFO(sugerido.getInter_valor())+"");
						else
							fila_lista_sel.setVariable("{valorSug}", Formatos.formatoIntervaloFO(sugerido.getCantidad()) + "");
						
						fila_lista_sel.setVariable("{contadorSug}", contador0+"" );
						fila_lista_sel.setVariable("{contador_form_auxSug}",  i+"");
						fila_lista_sel.setVariable("{maximoSug}", sugerido.getInter_maximo() + "");
						fila_lista_sel.setVariable("{intervaloSug}", sugerido.getInter_valor() + "");
						List aux_blanco = new ArrayList();
						aux_blanco.add(fila_lista_sel);
						valueset_items_sel.setDynamicValueSets("INPUT_SELSUG",aux_blanco);
					}
				
					List lista_items_sel = new ArrayList();
					valueset_items_sel.setVariable("{pro_idSug}", sugerido.getPro_id()+"");
					valueset_items_sel.setVariable("{unidadSug}", sugerido.getTipre() );					
					valueset_items_sel.setVariable("{contadorSug}", contador0+"" );
					valueset_items_sel.setVariable("{difSug}", "" );
					lista_items_sel.add(valueset_items_sel);
					fila.setDynamicValueSets("ITEMS_SELSUG", lista_items_sel);
					/** FIN Selección de cantidad de productos **/
					
					if( sugerido.isEn_carro() == true )
						isEn_carro0 = true;
					
					contador0++;
					
				}
				
				IValueSet lista_boton = new ValueSet();
				lista_boton.setVariable("{contador_formSug}",  i+"");	
				lista_boton.setVariable("{notaSug}", sugerido.getNota()+"");
				
				List list_boton = new ArrayList();
				list_boton.add(lista_boton);
				
				if( isEn_carro0 == true )
					fila.setDynamicValueSets("IMG_MODIFICARSUG", list_boton );
				else
					fila.setDynamicValueSets("IMG_AGREGARSUG", list_boton );				

				if (sugerido.getNota() == null)
					lista_boton.setVariable("{notaSug}", "");
				else
					lista_boton.setVariable("{notaSug}", sugerido.getNota()+"");
				
				if( sugerido.isCon_nota() == true ){
					fila.setDynamicValueSets("IMG_COMENSUG", list_boton );
				}
				
				fila.setVariable("{total_productosSug}", contador0+"");
				
				arr_sugerido.add(fila);
			}
            if ( cliente_id != 1 ) {
                top.setDynamicValueSets("PRODUCTOSSUG", arr_sugerido);    
            }		
			
			//MUETRA EL TITULO SUGERIDO EN LA FICHA SI ES QUE EXISTEN PROD SUGERIDOS 
			if ( list_sugerido.size() > 0 && cliente_id != 1 ) {
				 List list_sug = new ArrayList(); 
				 IValueSet fila = new ValueSet();
				 fila.setVariable("{sugerido}", "Sugerencias"); 
				 list_sug.add(fila);
				 top.setDynamicValueSets("TITULOSUG", list_sug); 
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
			
			/**
			 * Producto
			 */
			List arr_productos = new ArrayList();	
			List list_prod = biz.getProducto( prod_id , cliente_id, local_id, l_torec, invitado_id);
			long contador = 0;
            long idUnidadPila=0;
            double porcionNutrienteProducto=0;
			String saux = "";
			boolean isEn_carro = false;
			for( int i = 0; i < list_prod.size() ; i++ ) {
				isEn_carro = false;
				contador = 0;
				ProductoDTO data = (ProductoDTO) list_prod.get(i);
				IValueSet fila = new ValueSet();
                idUnidadPila = data.getIdPilaUnidad();                
                porcionNutrienteProducto = data.getPilaPorcion();	
				fila.setVariable("{contador_form}",  i+"");
				fila.setVariable("{contador}",  contador+"");
				fila.setVariable("{pro_imagen}",  data.getImg_chica()+"");
				fila.setVariable("{pro_id}", data.getPro_id()+"");
				fila.setVariable("{pro_tipo}", data.getTipo_producto()+"");
				top.setVariable("{pro_tipo}", data.getTipo_producto()+"");
				saux = data.getTipo_producto().replaceAll("'"," ");
				fila.setVariable("{pro_tipo_analytics}", saux + "");
				fila.setVariable("{pro_marca}", data.getMarca()+"");
				top.setVariable("{pro_marca}", data.getMarca()+"");
				saux = data.getMarca().replaceAll("'"," ");
				fila.setVariable("{pro_marca_analytics}", saux + "");
				fila.setVariable("{pro_desc}", data.getDescripcion()+"");
				top.setVariable("{pro_desc}", data.getDescripcion()+"");
				fila.setVariable("{pro_imagen}", data.getImg_grande()+"");

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
						ivs_promo.setVariable( "{promo_color_bann}", promocion.getColorBann());
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
				
				if (data.getNota() == null)
					fila.setVariable("{nota}", "");
				else
					fila.setVariable("{nota}", data.getNota()+"");

				
				// Producto genérico
				if(data.getGenerico().compareTo("G") == 0 ) {
					List data_items = data.getProductosDTO();					
					List lista_items = new ArrayList();
					List lista_items_sel = new ArrayList();					
					
					for(int v = 0; v < data_items.size(); v++ ) {
						ProductoDTO item = (ProductoDTO) data_items.get(v);
						IValueSet valueset_items = new ValueSet();
						valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(item.getPrecio()) + "" );
						valueset_items.setVariable("{pro_ppum}", Formatos.formatoPrecioFO(item.getPpum()) + "" );
						valueset_items.setVariable("{pro_uni_med_ppum}", item.getUnidad_nombre()+"");
						valueset_items.setVariable("{unidad}", item.getTipre()+"");
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
						valueset_items_sel.setVariable("{unidad}", item.getTipre()+"" );						
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
					valueset_items.setVariable("{precio}", Formatos.formatoPrecioFO(data.getPrecio()) + "" );
					valueset_items.setVariable("{pro_ppum}", Formatos.formatoPrecioFO(data.getPpum()) + "" );
					valueset_items.setVariable("{pro_uni_med_ppum}", data.getUnidad_nombre()+"");
					valueset_items.setVariable("{unidad}", data.getTipre()+"");
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
				
				List list_boton = new ArrayList();
				list_boton.add(lista_boton);
				if (data.tieneStock()) {
				    if( isEn_carro == true )
				        fila.setDynamicValueSets("IMG_MODIFICAR", list_boton );
				    else
				        fila.setDynamicValueSets("IMG_AGREGAR", list_boton );				
				
				    if( data.isCon_nota() == true ){
				        fila.setDynamicValueSets("IMG_COMEN", list_boton );
				    }else{
				        fila.setDynamicValueSets("IMG_SINCOMEN", list_boton );
				    }
                } else {
                    List bloqueados = new ArrayList();
                    IValueSet bloq = new ValueSet();
                    bloq.setVariable("{bloqueado}","");
                    bloqueados.add(bloq);
                    fila.setDynamicValueSets("IMG_SINSTOCK", bloqueados);
                }
				
				fila.setVariable("{total_productos}", contador+"");
				
				arr_productos.add(fila);
			}
			top.setDynamicValueSets("PRODUCTOS", arr_productos);
            
			
            List mostrar_pila = new ArrayList();   
            List lPilasDelProducto = biz.getPilasNutricionalesByProductoFO(prod_id);
            
            if ( idUnidadPila != 0 && porcionNutrienteProducto != 0 && lPilasDelProducto.size() > 0) {
                
                PilaUnidadDTO unidad = biz.getPilaUnidadById(idUnidadPila);
                
                IValueSet top_pila = new ValueSet();
                
                Double sPorc = new Double(porcionNutrienteProducto);
                if ( sPorc.intValue() == porcionNutrienteProducto ) {
                    top_pila.setVariable("{porcion_nutriente}", ""+sPorc.intValue());
                } else {
                    top_pila.setVariable("{porcion_nutriente}", ""+porcionNutrienteProducto);
                }
                top_pila.setVariable("{unidad}", unidad.getUnidad());
                
                List arr_pilas = new ArrayList();       
                for (int i = 0; i < lPilasDelProducto.size(); i++) {
                    PilaProductoDTO pilaProducto = (PilaProductoDTO) lPilasDelProducto.get(i);
                    IValueSet fila = new ValueSet();
                    fila.setVariable("{nutriente}", pilaProducto.getPila().getNutriente());
                    Double sNutri = new Double(pilaProducto.getNutrientePorPorcion());
                    if ( sNutri.intValue() == pilaProducto.getNutrientePorPorcion() ) {
                        fila.setVariable("{nutriente_porcion}", ""+sNutri.intValue());
                    } else {
                        fila.setVariable("{nutriente_porcion}", ""+pilaProducto.getNutrientePorPorcion());
                    }                
                    fila.setVariable("{unidad}", pilaProducto.getPila().getUnidad().getUnidad());
                    Double sPorcen = new Double(pilaProducto.getPorcentaje());
                    if ( sPorcen.intValue() == pilaProducto.getPorcentaje() ) {
                        fila.setVariable("{porcentaje}", ""+sPorcen.intValue());
                    } else {
                        fila.setVariable("{porcentaje}", ""+pilaProducto.getPorcentaje());
                    }
                    arr_pilas.add(fila);
                }                
                top_pila.setDynamicValueSets("PILAS", arr_pilas);
                mostrar_pila.add(top_pila);
            }                        
            top.setDynamicValueSets("MOSTRAR_PILA", mostrar_pila);
            
            /***************** MOSTRAR TIPO FICHA ****************/
            ProductosDelegate prodDelegate = new ProductosDelegate();
            String tipoFicha = "";
            boolean show_ficha_nut = false;
            boolean show_alto_en   = false;
            
            List listTipoFicha = prodDelegate.obtenerTipoFicha(prod_id);
            Iterator itList = listTipoFicha.iterator();
            while(itList.hasNext()){
            	tipoFicha = (String) itList.next();

            	if(tipoFicha.equals("FNUTRICIONAL")){
            		show_ficha_nut = true;
            		top.setVariable("{mostrarFichaNutri}", "display:inline;");
            	}
            	if(tipoFicha.equals("ALTOEN")){
            		show_alto_en = true;
            		top.setVariable("{mostrarAltoEn}", "display:inline;");
            	}
            }
            
            /**
             * Ficha tecnica
             */
            List camposFichaList = new ArrayList();
            ProductoDTO producto = list_prod != null ? (ProductoDTO) list_prod.get(0) : null;
            if (producto != null) {
            	/**
    			* Valida si tiene habilitado ficha tecnica
    			*/
            	boolean productoTieneFichaTecnica = biz.tieneFichaProductoById(prod_id);

            	if (productoTieneFichaTecnica) {
            		List listFichaProductos = null;
        			listFichaProductos = biz.getFichaProductoPorId(prod_id);

        			if (listFichaProductos != null && listFichaProductos.size() > 0) {
        				List listItems = biz.getItemFichaProductoAll();
        				Map mapItem = new HashMap();
        		    	//Lleno map items
        		    	llenaMapItems(listItems, mapItem);

        		    	ItemFichaProductoDTO itemDto = null;
        				for (int i = 0; i < listFichaProductos.size(); i++) {
        					FichaProductoDTO fichaDto = (FichaProductoDTO) listFichaProductos.get(i);
        					if (fichaDto != null) {
        						String itemDescripcion = "Sin descripci&oacute;n";
        						if (mapItem.get(String.valueOf(fichaDto.getPftPfiItem())) != null) {
        							itemDto = (ItemFichaProductoDTO) mapItem.get(String.valueOf(fichaDto.getPftPfiItem()));
        							itemDescripcion = itemDto.getPfiDescripcion();
        						}
        						logger.debug("ProductDisplay->Ficha-> valor item[" +i+ "]: " + itemDescripcion);
        						logger.debug("ProductDisplay->Ficha-> valor item descripcion:["+i+"]" + fichaDto.getPftDescripcionItem());
        						IValueSet detalleFicha = new ValueSet();
        						detalleFicha.setVariable("{item}", itemDescripcion);
        						detalleFicha.setVariable("{itemDescripcion}", fichaDto.getPftDescripcionItem() != null && fichaDto.getPftDescripcionItem().length() > 0 ? fichaDto.getPftDescripcionItem() : "");
        						camposFichaList.add(detalleFicha);
        					}
        				}
        			}
            	}
            }

            if(camposFichaList.size() == 0){
            	top.setVariable("{ocultarFichaTecFO}", "display:none;");
            }else{
            	top.setVariable("{ocultarFichaTecFO}", "display:inline;");
            }
            
			top.setDynamicValueSets("MOSTRAR_FICHA", camposFichaList);
			
			
            /**
             * Ficha Nutricional
             */
            /****** Ley Super Ocho ******/
			List listaNutriImg = new ArrayList();
			List camposFichaNut = new ArrayList();
			
			if(show_alto_en){
			
				listaNutriImg = prodDelegate.listaLeySuperOcho(Integer.parseInt(prod_id_str));
				Iterator ite = listaNutriImg.iterator();
				
				NutricionalLeySupeDTO dto = null;
				String statusEA = "off";
				String statusEC= "off";
				String statusEGS = "off";
				String statusES = "off";
				
				if(ite.hasNext()){ // sólo tiene una fila
					dto = (NutricionalLeySupeDTO)ite.next();
					if(dto.getExcesoAzuraces()=='P'){
						statusEA = "on";
					}
					
					if(dto.getExcesoCalorias()=='P'){
						statusEC = "on";
					}
					if(dto.getExcesoGrasasSaturadas()=='P'){
						statusEGS = "on";
					}
					if(dto.getExcesoSodio()=='P'){
						statusES = "on";
					}
				}
				
				top.setVariable("{imagen_aa}" , statusEA);
				top.setVariable("{imagen_ac}" , statusEC);
				top.setVariable("{imagen_ags}", statusEGS);
				top.setVariable("{imagen_as}" , statusES);
			}
			
			String cabecera = "";
			if(show_ficha_nut){
				/********** DATOS FICHA NUTRICIONAL **********/
				
				List listaFichaNutri = new ArrayList();
				listaFichaNutri = prodDelegate.listaFichaNutricional(Integer.parseInt(prod_id_str));
				FichaNutricionalDTO dtoFN = null;
				int i = 0;
				
				Iterator itFicNut = listaFichaNutri.iterator();
				while(itFicNut.hasNext()){
					dtoFN = (FichaNutricionalDTO)itFicNut.next();
					if(i == 0){
						cabecera = dtoFN.getCabecera();
					}
					
					IValueSet datosFicha = new ValueSet();
					datosFicha.setVariable("{item}", dtoFN.getItem() != null && dtoFN.getItem().length() > 0 ? dtoFN.getItem() : "");
					datosFicha.setVariable("{itemDescripcion}", dtoFN.getDescripcion() != null && dtoFN.getDescripcion().length() > 0 ? dtoFN.getDescripcion() : "");
					datosFicha.setVariable("{itemDescripcion2}", dtoFN.getDescripcion2() != null && dtoFN.getDescripcion2().length() > 0 ? dtoFN.getDescripcion2() : "");
					camposFichaNut.add(datosFicha);
					i++;
					
				}
				if(camposFichaNut.size() > 0){
					top.setDynamicValueSets("MOSTRAR_FICHA_NUTRICIONAL", camposFichaNut);
					
				}else{
					top.setVariable("{mostrarFichaNutri}", "display:none");
				}
			}else{
				top.setVariable("{mostrarFichaNutri}", "display:none");
				cabecera = "";
			}
			top.setVariable("{cabecera}", cabecera);
			
            //Tags Analytics - Captura Categorias, Sub Cat, etc.
         /*   if (idSubCategoria > 0) {
                CategoriaDTO subcat = biz.getCategoria(idSubCategoria);
                nombreSubCategoria = subcat.getNombre();
                banner = subcat.getBanner();
                url_banner = subcat.getUrl_banner();
                if (idCategoria == 0)
                    idCategoria = (int) subcat.getId_padre();
            }
            if (idCategoria != 0) {
                CategoriaDTO cat = biz.getCategoria(idCategoria);
                nombreCategoria = cat.getNombre();
            }
            if (idCabecera != 0) {
                CategoriaDTO cab = biz.getCategoria(idCabecera);
                nombreCabecera = cab.getNombre();
            }*/
			
            String cabecera_nombre = (String) arg0.getParameter("cabecera_nombre");
            String categoria_nombre = (String) arg0.getParameter("categoria_nombre");
            String subcategoria_nombre = (String) arg0.getParameter("subcategoria_nombre");
            
            top.setVariable("{cabecera_nombre}",cabecera_nombre);
            top.setVariable("{categoria_nombre}",categoria_nombre);
            top.setVariable("{subcategoria_nombre}",subcategoria_nombre);
            
            //Tags Analytics - Captura de estado de user
            if(session.getAttribute("ses_cli_nombre_pila").toString().equals("Invitado")){
                top.setVariable("{ta_mx_user}", "invitado");
            }
            else{
                top.setVariable("{ta_mx_user}", "registrado");
            }
			//Tags Analytics - Captura de Comuna y Región en Texto
			/************   LISTADO DE REGIONES   ****************/
			  
			String ta_mx_loc = ComunasRegionesTexto.ComunaRegionTexto(session);
			if(ta_mx_loc.equals(""))
			ta_mx_loc="none-none";
			top.setVariable("{ta_mx_loc}", ta_mx_loc);
			  
			
			
			String mx_content = "/"+cabecera_nombre+"/"+categoria_nombre+"/"+subcategoria_nombre+"/"+top.getVariable("{pro_marca}")+" - "+top.getVariable("{pro_desc}");
			
			top.setVariable("{mx_content}", mx_content);
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}
	}
	
	/**
	* 
	* @param listItem
	* @param mapItem
	*/
	private void llenaMapItems(List listItem, Map mapItem) {
		if (listItem != null && !listItem.isEmpty()) {
			for (int i = 0; i < listItem.size(); i++) {
				ItemFichaProductoDTO dto = (ItemFichaProductoDTO) listItem.get(i);
					if(!mapItem.containsKey(String.valueOf(dto.getPfiItem()))  ){
						mapItem.put(String.valueOf(dto.getPfiItem()), dto);
					}
			}
		}
	}
}