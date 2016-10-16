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
import cl.bbr.fo.exception.SystemException;
import cl.bbr.jumbocl.clientes.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.clientes.dto.CarroCompraProductosDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.promo.lib.dto.FOTcpDTO;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;

/**
 * Listado de productos en el checkout
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class CheckoutProList extends Command {

	/**
	 * Listado de productos en el checkout
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		try {

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=iso-8859-1");

			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();			
			
			// Recupera pagina desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			this.getLogger().debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();

			List listaCarro = new ArrayList();
			List fm_cate = new ArrayList();
			int contador=0;
			long totalizador = 0;
			
			long cli_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
            String invitado_id = "";
            if (session.getAttribute("ses_invitado_id") != null &&
                    !session.getAttribute("ses_invitado_id").toString().equals("")){
                invitado_id = session.getAttribute("ses_invitado_id").toString();
            }

            //riffo
            List repetidos =  biz.carroComprasGetProductosCheckOut( cli_id, session.getAttribute("ses_loc_id").toString(), invitado_id);
            CarroCompraDTO carRepetidos = null;
            for (int r = 0; r < repetidos.size(); r++) {
                carRepetidos = (CarroCompraDTO) repetidos.get(r);
                biz.deleteCarroCompra(cli_id, carRepetidos.getId(), invitado_id);                               
            }			
            List datos_cat = biz.carroComprasGetCategoriasProductos( session.getAttribute("ses_loc_id").toString(), cli_id);
			
			long total_producto_pedido = 0;
			double precio_total = 0;
            
            if ( datos_cat.size() == 0 ) {
                List vacia = new ArrayList();
                IValueSet fila = new ValueSet();
                fila.setVariable("{vacio}", "");
                vacia.add(fila);
                top.setDynamicValueSets("SIN_CATEGORIAS", vacia);
            }

			for( int i = 0; i < datos_cat.size(); i++ ) {				
                CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) datos_cat.get(i);
								
                IValueSet fila_cat = new ValueSet();
				fila_cat.setVariable("{categoria}", cat.getCategoria());
				
				List prod = cat.getCarroCompraProductosDTO();
								
				List fm_prod = new ArrayList();				
				for (int j = 0; j < prod.size(); j++) {
					
                    CarroCompraProductosDTO producto = (CarroCompraProductosDTO) prod.get(j);
					
					total_producto_pedido += Math.ceil(producto.getCantidad());
					
					IValueSet fila_pro = new ValueSet();
					fila_pro.setVariable("{descripcion}", producto.getNombre());
					fila_pro.setVariable("{marca}", producto.getMarca());
					fila_pro.setVariable("{cod_sap}", producto.getCodigo());
					fila_pro.setVariable("{valor}", Formatos.formatoIntervaloFO(producto.getCantidad())+"");
					fila_pro.setVariable("{carr_id}", producto.getCar_id()+"");
					fila_pro.setVariable("{contador}", contador+"");
										
					precio_total = 0;
					// Existe STOCK para el producto
					if ( producto.getStock() != 0 ) {
					    if (producto.tieneStock()) {
						// Si el producto es con seleccion
                        if (producto.getUnidad_tipo().charAt(0) == 'S') {

                            IValueSet fila_lista_sel = new ValueSet();

                            List aux_lista = new ArrayList();
							for (double v = 0; v <= producto.getInter_maximo(); v += producto.getInter_valor()) {
                                IValueSet aux_fila = new ValueSet();
								aux_fila.setVariable("{valor}", Formatos.formatoIntervaloFO(v) + "");
								aux_fila.setVariable("{opcion}", Formatos.formatoIntervaloFO(v) + "");
								if( Formatos.formatoIntervaloFO(v).compareTo(Formatos.formatoIntervaloFO(producto.getCantidad())) == 0 )
									aux_fila.setVariable("{selected}", "selected");
								else
									aux_fila.setVariable("{selected}", "");
								aux_lista.add(aux_fila);
							}
							fila_lista_sel.setVariable("{contador}", contador+"");
							fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);
							
							List aux_blanco = new ArrayList();
							aux_blanco.add(fila_lista_sel);
							fila_pro.setDynamicValueSets("LISTA_SEL", aux_blanco);							
							
						} else {
                            IValueSet fila_lista_sel = new ValueSet();
							fila_lista_sel.setVariable("{contador}", contador+"");
							fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(producto.getCantidad()) + "");
							fila_lista_sel.setVariable("{maximo}", producto.getInter_maximo() + "");
							fila_lista_sel.setVariable("{intervalo}", producto.getInter_valor() + "");
							List aux_blanco = new ArrayList();
							aux_blanco.add(fila_lista_sel);
							fila_pro.setDynamicValueSets("INPUT_SEL",aux_blanco);
						}
                        
						
						if( producto.isCon_nota() == true ) {
                            IValueSet set_nota = new ValueSet();
							set_nota.setVariable("{nota}", producto.getNota()+"");
							set_nota.setVariable("{contador}", contador+"");
							List aux_blanco = new ArrayList();
							aux_blanco.add(set_nota);
							fila_pro.setDynamicValueSets("NOTA", aux_blanco);					
						}
                        
						
                            precio_total = Utils.redondearFO( producto.getPrecio()*producto.getCantidad() );
                            fila_pro.setVariable("{unidad}", producto.getTipre());
                            fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecioFO(producto.getPrecio()) );
                            fila_pro.setVariable("{precio_total}", Formatos.formatoPrecioFO(precio_total) );				
                            fila_pro.setVariable("{CLASE_TABLA}", "TablaDiponiblePaso1");
                            fila_pro.setVariable("{CLASE_CELDA}", "celda1");
                            fila_pro.setVariable("{NO_DISPONIBLE}", "");
                            fila_pro.setVariable("{OPCION_COMPRA}", "1");
                            fila_pro.setVariable("{btn_eliminar}", "<img src=\"/FO_IMGS/img/estructura/paso3/eliminar.gif\" alt=\"Eliminar este producto del carro de compras\" width=\"14\" height=\"10\" border=\"0\">");
                            fila_pro.setVariable("{input_no_disponible}", "");
                            totalizador += precio_total;
                        } else {
                            fila_pro.setVariable("{precio_unitario}", "" );
                            fila_pro.setVariable("{precio_total}", "" );
                            fila_pro.setVariable("{unidad}", "");
                            fila_pro.setVariable("{CLASE_TABLA}", "TablaNoDiponiblePaso1");
                            fila_pro.setVariable("{CLASE_CELDA}", "celda1NoDisponible");
                            fila_pro.setVariable("{NO_DISPONIBLE}", "(No disponible)");
                            fila_pro.setVariable("{OPCION_COMPRA}", "0");
                            fila_pro.setVariable("{btn_eliminar}", "<img src=\"/FO_IMGS/img/estructura/paso3/eliminar.gif\" alt=\"Eliminar este producto del carro de compras\" width=\"14\" height=\"10\" border=\"0\">");
                            fila_pro.setVariable("{input_no_disponible}", "<input type=\"hidden\" name=\"cantidad"+contador+"\" value=\"0\"/>");
                            IValueSet aux_fila = new ValueSet();
                            List aux_lista = new ArrayList();
                            aux_lista.add(aux_fila);
                        }
						
						// Agrega a la lista para calcular sus promociones
						listaCarro.add( producto );
						
					} else {
						fila_pro.setVariable("{precio_unitario}", "" );
						fila_pro.setVariable("{precio_total}", "" );
						fila_pro.setVariable("{unidad}", "");
						fila_pro.setVariable("{CLASE_TABLA}", "TablaNoDiponiblePaso1");
						fila_pro.setVariable("{CLASE_CELDA}", "celda1NoDisponible");
						fila_pro.setVariable("{NO_DISPONIBLE}", "(No disponible)");
						fila_pro.setVariable("{OPCION_COMPRA}", "0");
						fila_pro.setVariable("{btn_eliminar}", "<img src=\"/FO_IMGS/img/estructura/paso3/eliminar.gif\" alt=\"Eliminar este producto del carro de compras\" width=\"14\" height=\"10\" border=\"0\">");
                        fila_pro.setVariable("{input_no_disponible}", "<input type=\"hidden\" name=\"cantidad"+contador+"\" value=\"0\"/>");
                        IValueSet aux_fila = new ValueSet();
						List aux_lista = new ArrayList();
						aux_lista.add(aux_fila);
					}
					contador++;
					fm_prod.add(fila_pro);
				}
				fila_cat.setDynamicValueSets("PRODUCTOS", fm_prod);
				
				fm_cate.add(fila_cat);
				
				this.getLogger().debug( "Categoria: "+cat.getCategoria()+ "("+cat.getId()+") Productos:"+fm_prod.size() );
				
			}
			top.setDynamicValueSets("CATEGORIAS", fm_cate);

			// Recuperar los cupones y los tcp de la sesión
			List l_torec = new ArrayList();
			List l_tcp = null;
			if( session.getAttribute("ses_promo_tcp") != null ) {
				l_tcp = (List)session.getAttribute("ses_promo_tcp");
				l_torec.addAll(l_tcp);
			}
			
			String cupones = "";
			if( session.getAttribute("ses_cupones") != null ) {
				List l_cupones = (List)session.getAttribute("ses_cupones");
				l_torec.addAll(l_cupones);
				for( int f = 0; l_cupones != null && f < l_cupones.size(); f++ ) {
					FOTcpDTO cupon = (FOTcpDTO)l_cupones.get(f);
					cupones += cupon.getCupon() + "-=-";
				}
				top.setVariable("{cupones}", cupones );
			} else {
				top.setVariable("{cupones}", "" );
			}
			if( session.getAttribute("ses_msg_cupon") != null )
				top.setVariable("{msg_cupon}", session.getAttribute("ses_msg_cupon") );
			else
				top.setVariable("{msg_cupon}", "" );
			session.setAttribute("ses_msg_cupon", "");

			String desc_promo_j = "";
			//double descuento_promo_j = totalizador;
			double descuento_promo_j = 0;
			String desc_promo_tc = "";
			//double descuento_promo_tc = totalizador;
			double descuento_promo_tc = 0;
			// Obtener datos de la promomocion
			try {

				List l_prod = new ArrayList();

				List listaCarro_all = biz.carroComprasGetProductos(cli_id, session.getAttribute("ses_loc_id").toString(), invitado_id);
				CarroCompraDTO car = null;

				for (int i = 0; i < listaCarro.size(); i++) {
                    CarroCompraProductosDTO producto = (CarroCompraProductosDTO) listaCarro.get(i);
					
					for (int f = 0; f < listaCarro_all.size(); f++) {
						 car = (CarroCompraDTO) listaCarro_all.get(f);
						 if( producto.getCar_id() == car.getId() ) {
						 	break;
						 }						 
					}
					
					ProductoPromosDTO pro = new ProductoPromosDTO();
					pro.setId_producto(car.getId_bo());
					pro.setCod_barra(car.getCodbarra());
					pro.setSeccion_sap(car.getCatsap());
					pro.setCant_solicitada(car.getCantidad());
					if(car.getPesable()!=null && car.getPesable().equals("S") )
						pro.setPesable("P");
					else
						pro.setPesable("C");
					pro.setPrecio_lista(car.getPrecio());
					l_prod.add(pro);
				}
				
				if (l_prod != null && l_prod.size() > 0 ){
					// descuento para jumbomas
					doRecalculoCriterio recalculoDTO_j = new doRecalculoCriterio();
					recalculoDTO_j.setCuotas( Integer.parseInt(rb.getString("promociones.jumbomas.cuotas")) );
					recalculoDTO_j.setF_pago( rb.getString("promociones.jumbomas.formapago") );
					recalculoDTO_j.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
					recalculoDTO_j.setProductos( l_prod );
					recalculoDTO_j.setGrupos_tcp(l_torec);
					//[20121108avc
	                if(Boolean.valueOf(String.valueOf(session.getAttribute("ses_colaborador"))).booleanValue())
	                    recalculoDTO_j.setRutColaborador(new Long(session.getAttribute("ses_cli_rut").toString()));
					//]20121108avc

					
					doRecalculoResultado resultado_j = biz.doRecalculoPromocion( recalculoDTO_j );
					List l_promo = resultado_j.getPromociones();
					for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
						PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
						this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
						desc_promo_j += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
					}
					// Se cambia a descuento de la orden
					
					/*descuento_promo_j = totalizador - resultado.getDescuento_pedido();
					if(descuento_promo_j < 0)
						descuento_promo_j = 0;*/
					
					descuento_promo_j = resultado_j.getDescuento_pedido();
					if(totalizador < resultado_j.getDescuento_pedido())
						descuento_promo_j = totalizador;
					
					// descuento para tarjeta de credito
					doRecalculoCriterio recalculoDTO_tc = new doRecalculoCriterio();
					recalculoDTO_tc.setCuotas( Integer.parseInt(rb.getString("promociones.tjacredito.cuotas")) );
					recalculoDTO_tc.setF_pago( rb.getString("promociones.tjacredito.formapago") );
					recalculoDTO_tc.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
					recalculoDTO_tc.setProductos( l_prod );
					recalculoDTO_tc.setGrupos_tcp(l_torec);
					
//					[20121108avc
	                if(Boolean.valueOf(String.valueOf(session.getAttribute("ses_colaborador"))).booleanValue())
	                    recalculoDTO_tc.setRutColaborador(new Long(session.getAttribute("ses_cli_rut").toString()));
					//]20121108avc
					
					doRecalculoResultado resultado_tc = biz.doRecalculoPromocion( recalculoDTO_tc );
					l_promo = resultado_tc.getPromociones();
					for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
						PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
						this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
						desc_promo_tc += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
					}
					// Se cambia a descuento de la orden
					/*
					descuento_promo_tc = totalizador - resultado.getDescuento_pedido();
					if(descuento_promo_tc < 0)
						descuento_promo_tc = 0;
					*/
					descuento_promo_tc = resultado_tc.getDescuento_pedido();
					if(totalizador < resultado_tc.getDescuento_pedido())
						descuento_promo_tc = totalizador;
				}
			} catch( SystemException e ) {
				this.getLogger().error("Error en libreria de promociones, no presenta promociones: "+e.getMessage());
			}

			top.setVariable("{total_desc_j_sf}", Formatos.formatoPrecioFO(descuento_promo_j));
            top.setVariable("{total_desc_j}", Formatos.formatoPrecioFO(totalizador-descuento_promo_j));
			top.setVariable("{promo_desc_j}", desc_promo_j );
			top.setVariable("{total_desc_tc}", Formatos.formatoPrecioFO(totalizador-descuento_promo_tc));
			top.setVariable("{promo_desc_tc}", desc_promo_tc );
			
			top.setVariable("{total_actual}", Formatos.formatoPrecioFO(totalizador) );
			top.setVariable("{total_actual_sf}", ""+totalizador );
			top.setVariable("{total_registros}", contador+"" );
			top.setVariable("{total_producto_pedido}", total_producto_pedido+"" );
			
			this.getLogger().debug("Total:"+totalizador+" Total productos:"+total_producto_pedido);
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			
			throw new CommandException( e );
		}

	}

}