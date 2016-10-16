package cl.bbr.fo.command.mobi;

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
import cl.bbr.fo.command.Command;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;


/**
 * Página que entrega los productos que tiene el cliente en el carro
 * <p>
 * Muestra un listado de los productos que se encuentran guardados en el carro
 * <p>
 * Pagina para mobile
 * 
 * @author imoyano
 * 
 */
public class OrderItemDisplay extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
		    arg1.setContentType("text/html; charset=iso-8859-1");
		    arg1.setCharacterEncoding("UTF-8");
		    
		    long idProductoAgregado = 0;
		    boolean primerProdNuevo = true;
            int regPorPagina = 5;
            int pagina = 1;
            boolean mostrarFoto = true;

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
			
			List idsProductos = new ArrayList();
			if ( session.getAttribute("ids_prod_add") != null ) {
			    idsProductos = (ArrayList) session.getAttribute("ids_prod_add");
			    session.removeAttribute("ids_prod_add");
			}
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			this.getLogger().debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();	
            
            
			// Paginacion
            if (arg0.getParameter("pagina") != null) {
                pagina = Integer.parseInt(arg0.getParameter("pagina"));
            }
            if (arg0.getParameter("paginado") != null) {
                regPorPagina = Integer.parseInt(arg0.getParameter("paginado"));
            }
            if (arg0.getParameter("ver_foto_txt") != null) {
                if ( arg0.getParameter("ver_foto_txt").equalsIgnoreCase("0")) {
                    mostrarFoto = false;
                }
            }

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
			
			double totalRegistros = biz.getCountCarroCompras( cliente_id.longValue(), session.getAttribute("ses_loc_id").toString() );
            if ( ( ( totalRegistros / regPorPagina ) + 1 ) < pagina ) {
                pagina = ( new Double ( totalRegistros / regPorPagina ) ).intValue();
                if (pagina <= 0) {
                    pagina = 1;
                }
            }
            List listaCarro = biz.carroComprasMobi( cliente_id.longValue(), session.getAttribute("ses_loc_id").toString(), regPorPagina, pagina );
            
			List datos_e = new ArrayList();
			
			long total = 0;
			
			long  valorid = 0;
			long precio_total = 0;
			for (int i = 0; i < listaCarro.size(); i++) {
				 CarroCompraDTO car = (CarroCompraDTO) listaCarro.get(i); 
				 IValueSet fila = new ValueSet();
				 fila.setVariable("{id}", car.getId()+"");
				 fila.setVariable("{pro_id}",car.getPro_id()+""); 
				 valorid = car.getId();
				 precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
				 total += precio_total;
				 String nombre_pro = (car.getTipo_producto()+" "+car.getNom_marca()).trim();
				 //se separan los '/' de los productos q contengan este caracter 
				 nombre_pro = cl.bbr.jumbocl.common.utils.Utils.separarDescripcionesLargas(nombre_pro);
				 
				 int largo_pro = Integer.parseInt(rb.getString("orderitemdisplay.largonombreproducto")); 
				 if( nombre_pro.length() < largo_pro )
				 	largo_pro = nombre_pro.length();
				 fila.setVariable("{nombrepro}", nombre_pro.substring(0,largo_pro));
				 fila.setVariable("{nommarca}", car.getTipo_producto()+"\n"+car.getNombre()+"\n"+car.getNom_marca()+"");
				 fila.setVariable("{subtotal}", Formatos.formatoPrecioFO(precio_total) + "");
				 
				 fila.setVariable("{i}", i +"");
			 
				// Si el producto es con seleccion
                car.setUnidad_tipo("S"); //Seteamos para mobile
				if (car.getUnidad_tipo().charAt(0) == 'S') {
					IValueSet fila_lista_sel = new ValueSet();
					fila_lista_sel.setVariable("{id}", valorid+"" );
					fila_lista_sel.setVariable("{pro_id}",car.getPro_id()+""); 
					fila_lista_sel.setVariable("{contador}", i+"" );
					fila_lista_sel.setVariable("{maximo}", car.getInter_maximo() + "");
					fila_lista_sel.setVariable("{intervalo}", car.getInter_valor() + "");
					fila_lista_sel.setVariable("{unidad}", car.getTipre() + "" );
					fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(car.getCantidad()) + "" );
					if (car.getStock() > 0) {
					    fila_lista_sel.setVariable("{disabled}", "");
                    } else {
                        fila_lista_sel.setVariable("{disabled}", "disabled");
                    }
                    
                    List aux_blanco = new ArrayList();
					aux_blanco.add(fila_lista_sel);
                    fila.setDynamicValueSets("LISTA_SEL", aux_blanco);
				} else {
					IValueSet fila_lista_sel = new ValueSet();
					fila_lista_sel.setVariable("{id}", valorid+"" );
					fila_lista_sel.setVariable("{pro_id}",car.getPro_id()+""); 
					fila_lista_sel.setVariable("{contador}", i+"" );
					fila_lista_sel.setVariable("{maximo}", car.getInter_maximo() + "");
					fila_lista_sel.setVariable("{intervalo}", car.getInter_valor() + "");
					fila_lista_sel.setVariable("{unidad}", car.getTipre() + "" );
					fila_lista_sel.setVariable("{valor}", Formatos.formatoIntervaloFO(car.getCantidad()) + "");						
					List aux_blanco = new ArrayList();
					aux_blanco.add(fila_lista_sel);
					fila.setDynamicValueSets("INPUT_SEL",aux_blanco);
				}

				fila.setVariable("{id}", valorid+"" );
				fila.setVariable("{contador}", i+"" );
				
				if (esNuevoProducto(idsProductos, car.getPro_id())) {
				    fila.setVariable("{style_fila}", "productoAgregado" );
				    if (primerProdNuevo) {
				        idProductoAgregado = Long.parseLong( car.getPro_id() );
				        primerProdNuevo = false;
				    }
				} else {
				    fila.setVariable("{style_fila}", " " );
				}
                
                if ( mostrarFoto ) {
                    ArrayList mostrarImagen = new ArrayList();
                    IValueSet filaImg = new ValueSet();
                    filaImg.setVariable("{pro_imagen}", car.getImagen() );
                    mostrarImagen.add(filaImg);
                    fila.setDynamicValueSets("MUESTRA_IMG", mostrarImagen);
                } else {
                    fila.setDynamicValueSets("MUESTRA_IMG", null);
                }
                
                if (car.getStock() > 0) {                
                    fila.setVariable("{NO_DISPONIBLE}", "");
                    fila.setVariable("{estilo_div}", "despliegue_prod_carro");                    
                    
                    if ( car.isTieneNota() ) {
                        IValueSet aux_fila = new ValueSet();
                        List aux_lista = new ArrayList();
                        aux_fila.setVariable("{id_car}", car.getId()+"");
                        aux_fila.setVariable("{idprod}", car.getPro_id() + "");
                        aux_fila.setVariable("{contador}", i + "");
                        boolean tieneNotaEscrita = false;
                        if (car.getNota() != null) {
                            if (car.getNota().length() > 0) {
                                aux_fila.setVariable("{nota}", car.getNota());
                                aux_fila.setVariable("{comentario_img}", "notasb.gif");
                                tieneNotaEscrita = true;
                            }
                        } 
                        if (!tieneNotaEscrita){
                            aux_fila.setVariable("{nota}", "");
                            aux_fila.setVariable("{comentario_img}", "notas.gif");
                        }
                        aux_lista.add(aux_fila);
                        fila.setDynamicValueSets("COMENTARIO", aux_lista);
                    }
                    
                } else {
                    fila.setVariable("{NO_DISPONIBLE}", "(NO DISPONIBLE)");
                    fila.setVariable("{estilo_div}", "despliegue_prod_carro_no_disponible");
                }
                
				datos_e.add(fila);
			}
			top.setDynamicValueSets("lista_carro",datos_e);
			
			if( datos_e.size() == 0 ) {
				IValueSet fila_lista_sel = new ValueSet();
				fila_lista_sel.setVariable("p","");
				List datos_p = new ArrayList();
				datos_p.add(fila_lista_sel);
				top.setDynamicValueSets("lista_carro_img",datos_p);
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
			
			//double descuento_promo = total;
			double descuento_promo = 0;
			String desc_promo = "";
			// Obtener datos de la promomocion
			try {
				doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
				recalculoDTO.setCuotas( Integer.parseInt(rb.getString("promociones.jumbomas.cuotas")) );
				recalculoDTO.setF_pago( rb.getString("promociones.jumbomas.formapago") );
				recalculoDTO.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
				recalculoDTO.setGrupos_tcp(l_torec);
//				[20121108avc
                if(Boolean.valueOf(String.valueOf(session.getAttribute("ses_colaborador"))).booleanValue())
                    recalculoDTO.setRutColaborador(new Long(session.getAttribute("ses_cli_rut").toString()));
				//]20121108avc


				List l_prod = new ArrayList();
				
				for (int i = 0; i < listaCarro.size(); i++) {
					CarroCompraDTO car = (CarroCompraDTO) listaCarro.get(i);
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
				
				recalculoDTO.setProductos( l_prod );

				if (l_prod != null && l_prod.size() > 0 ){
					doRecalculoResultado resultado = biz.doRecalculoPromocion( recalculoDTO );
					
					List l_promo = resultado.getPromociones();
					for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
						PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
						this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
						desc_promo += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
					}
					
					// Se cambia a descuento de la orden
					/*
					descuento_promo = total - resultado.getDescuento_pedido();
					if(descuento_promo < 0)
						descuento_promo = 0;
					*/
					descuento_promo = resultado.getDescuento_pedido();
					if(total < resultado.getDescuento_pedido()){
					    descuento_promo = total;
					}
				}
			} catch( SystemException e ) {
				this.getLogger().error("Error en libreria de promociones, no presenta promociones: "+e.getMessage());
			}
			
			top.setVariable("{total_desc}", Formatos.formatoPrecioFO(descuento_promo));
			top.setVariable("{promo_desc}", desc_promo );
			
			top.setVariable("{total}", Formatos.formatoPrecioFO(total) +"" );
			top.setVariable("{cant_reg}", listaCarro.size() +"" );
			top.setVariable("{id_prod_add}", idProductoAgregado +"" );
            
            if (!mostrarFoto) {
                top.setVariable("{ver_foto_txt}", "0" );
                top.setVariable("{foto_checked}", "" );
            } else {
                top.setVariable("{ver_foto_txt}", "1" );
                top.setVariable("{foto_checked}", "checked" );
            }
            
            
            // -- INI Paginacion del carro            
            int total_pag = (int) Math.ceil( totalRegistros / regPorPagina );
            if (total_pag == 0) {
                total_pag = 1;
            } 
            //anterior y siguiente
            if ( pagina > 1 ) {
                int anterior = pagina - 1;
                top.setVariable("{anterior_label}","<img src=\"/FO_IMGS/images/mobi/fl_retr_pg.gif\" alt=\"ver pagina anterior\" width=\"20\" height=\"25\" border=\"0\" />");
                top.setVariable("{anterior}",String.valueOf(anterior));
                top.setVariable("{anterior_label_vacia}","");
            } else if ( pagina == 1) {
                top.setVariable("{anterior_label}","");
                top.setVariable("{anterior_label_vacia}","<img src=\"/FO_IMGS/images/mobi/fl_retr_pg_b.gif\" alt=\"\" width=\"20\" height=\"25\" border=\"0\" />");
            }       
            if (pagina < total_pag) {
                int siguiente = pagina + 1;
                top.setVariable("{siguiente_label}","<img src=\"/FO_IMGS/images/mobi/fl_av_pg.gif\" alt=\"ver pagina siguiente\" width=\"20\" height=\"25\" border=\"0\" />");
                top.setVariable("{siguiente}",String.valueOf(siguiente));
                top.setVariable("{siguiente_label_vacia}","");
            } else {
                top.setVariable("{siguiente_label}","");
                top.setVariable("{siguiente_label_vacia}","<img src=\"/FO_IMGS/images/mobi/fl_av_pg_b.gif\" alt=\"\" width=\"20\" height=\"25\" border=\"0\" />");
            }
            ArrayList paginaciones = new ArrayList();
            for ( int i = 5; i <= totalRegistros; i+=5 ) {
                IValueSet fila = new ValueSet();
                fila.setVariable("{pag}", String.valueOf(i));
                if (i == regPorPagina) {
                    fila.setVariable("{sel}", "selected");
                } else {
                    fila.setVariable("{sel}", "");
                }
                paginaciones.add(fila);
            }            
            
            top.setVariable("{pag_actual}",""+pagina);
            top.setVariable("{total_pag}",""+total_pag);
            top.setVariable("{reg_x_pag}",""+regPorPagina);
            
            if (paginaciones.size() > 0) {
                ArrayList mostrarPaginacion = new ArrayList();
                IValueSet fila = new ValueSet();
                fila.setVariable("{blanco}", "");
                fila.setDynamicValueSets("PAGINACIONES", paginaciones);
                mostrarPaginacion.add(fila);
                top.setDynamicValueSets("MOSTRAR_PAGINACIONES", mostrarPaginacion);
            } else {
                top.setDynamicValueSets("MOSTRAR_PAGINACIONES", null);
            }
            
            
            
            
            // -- FIN Paginacion del carro            
			
            
			this.getLogger().debug( "Total:"+total );
			this.getLogger().debug( "Productos:"+listaCarro.size() );
			this.getLogger().debug( "Total con descuento:"+descuento_promo );
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}


	}

    /**
     * No indica ID de producto es un producto nuevo
     * @param idsProductos Lista de Id's de productos agregados al carro
     * @param pro_id Id de Producto del carro
     * @return 
     */
    private boolean esNuevoProducto(List idsProductos, String pro_id) {
        for (int i = 0; i < idsProductos.size(); i++) {
            if ( idsProductos.get(i).equals(pro_id) ) {
                return true;
            }
        }
        return false;
    }

}