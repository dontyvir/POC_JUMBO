/*
 * Creado el 17-jun-2011
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.jumbo.capturar.command;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.jumbocl.clientes.dao.JdbcClientesDAO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.common.dto.CarroCompraProductosDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.capturar.bizdelegate.BizDelegate;
import cl.jumbo.capturar.exceptions.FuncionalException;
import cl.jumbo.capturar.utils.Formatos;

/**
 * @author imoyano
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class TerminaCompra {
    
    protected Logging logger = new Logging(this);
    private static final String POR = " por ";

    public void terminaComprasIncompletas(BizDelegate biz, ResourceBundle rb) {
        try {        
//            logger.info("\n comenzamos!");
            List pedidosCambiados = new ArrayList();
            List pedidosConError = new ArrayList();
            BufferedReader br = null;
            
            int diasDesde = 1;
            int minutosHaciaAtras = 3;
            
            //pedidos desde hace 2 dias hasta hace 10 minutos atras
            List pedidosTBK = biz.getPedidosRechazadosErroneamenteTBK(diasDesde, minutosHaciaAtras);
            
//            logger.info("pedidosTBK:"+pedidosTBK.size());
            
            //*** Ingresamos los pedidos de TBK ***
            for (int i=0; i < pedidosTBK.size(); i++) {
                boolean cambiar = false;
                //traemos el log de un pedido por la fecha
                PedidoDTO ped = (PedidoDTO) pedidosTBK.get(i);
                
                logger.debug("Pedido TBK ("+i+") "+ped.getId_pedido());
                
//                logger.debug("Fecha:"+ped.getFingreso());
                
                String diaIni = ped.getFingreso().substring(8, 10);
                String mesIni = ped.getFingreso().substring(5, 7);
                
                String archivo = "tbk_bitacora_TR_NORMAL_"+ mesIni + diaIni + ".log";
                
//                logger.debug("archivo:"+archivo);
                
                try {                
                    if (ped.getId_usuario_fono() != 0 && ped.getOrigen().equals("W")) {
//                        logger.debug("ruta:"+rb.getString("path.fonoWebpay"));
                        br = new BufferedReader(new FileReader(rb.getString("path.fonoWebpay")+ archivo));
                    } else if (ped.getOrigen().equals("V")) {
//                        logger.debug("ruta:"+rb.getString("path.ve"));
                        br = new BufferedReader(new FileReader(rb.getString("path.ve")+ archivo));
                    } else if (ped.getId_usuario_fono() == 0 && ped.getOrigen().equals("W")) {
//                        logger.debug("ruta:"+rb.getString("path.webpay"));
                        br = new BufferedReader(new FileReader(rb.getString("path.webpay")+ archivo));
                    }                    
                    String linea = null;
                    
//                    logger.debug("a recorrer...");
                    while ((linea = br.readLine()) != null) {
                        String col[] = linea.split(";");
                        if ( col.length >= 5 ) {
                            //col[0].trim(); => ACK  col[1].trim(); => TBK_ORDEN_COMPRA   col[4].trim(); => TBK_RESPUESTA=0
                            if ( col[1].trim().equalsIgnoreCase( "TBK_ORDEN_COMPRA="+ped.getId_pedido()+ped.getSecuenciaPago() ) ) { //Aca el id_pedido debe tener el correlativo
                                if ( col[4].trim().equalsIgnoreCase( "TBK_RESPUESTA=0" ) ) {
                                    if ( col[0].trim().equalsIgnoreCase( "ACK" ) ) {
                                        cambiar = true;
                                        break;
                                    }   
                                }                        
                            }
                        }                
                    }
//                    logger.debug("cambiar?"+cambiar);
                
                } catch (Exception e) { 
                	e.printStackTrace();
                	}
                
                //Si tiene el ACK le cambiamos el estado a en validacion y guardamos 
                if ( cambiar ) {
                   //Agregamos el pedido a pedidosCambiados
                    ingresarPedidoAlSistema(biz,ped,rb);
                    pedidosCambiados.add(String.valueOf(ped.getId_pedido()));
                } else {
                    //Si tiene Err, agregamos el pedido a pedidosConError
                    pedidosConError.add(String.valueOf(ped.getId_pedido()));
                }
            }
            if ( br != null ) {
                br.close();
            }
            
            List pedidosCAT = biz.getPedidosRechazadosErroneamenteCAT(diasDesde, minutosHaciaAtras);
            
            // *** Ingresamos los pedidos de CAT ***
            for (int j = 0; j < pedidosCAT.size(); j++) {
                PedidoDTO ped = (PedidoDTO) pedidosCAT.get(j);
//                logger.debug("Pedido CAT ("+j+") "+ped.getId_pedido());
                ingresarPedidoAlSistema(biz,ped,rb);
                pedidosCambiados.add(String.valueOf(ped.getId_pedido()));
            }
            
            if ( pedidosCambiados.size() > 0  ) {
                //Generamos un mail indicando los cambios, por ej para que BOC revisen que cliente puede tener compra duplicada
                envioDeMailBOCCambiados(pedidosCambiados,biz,rb);            
            }
            
            if ( pedidosConError.size() > 0 ) {
                //Generamos un mail para q sistemas deje los pedidosConError con código -1 en la tabla webpays para que no sean considerados nuevamente
                envioDeMailBOCError(pedidosConError,biz,rb);
                cambiaEstadoWebPays(pedidosConError,biz);
            }

        } catch (Exception e) {
            logger.error(e);
            logger.error(getStackTrace(e));
        }

    }
    
    /**
     * @param pedidosConError
     * @param biz
     */
    private void cambiaEstadoWebPays(List pedidosConError, BizDelegate biz) {
        try {
            for (int i=0; i < pedidosConError.size(); i++) {
                biz.cambiaEstadoWebPays( Long.parseLong( pedidosConError.get(i).toString() ));
            } 
        } catch (ServiceException e) {
            logger.error("Error cambiaEstadoWebPays:"+e.getMessage());
            logger.error(getStackTrace(e));            
        }
        
    }

    public void ingresarPedidoAlSistema(BizDelegate biz, PedidoDTO ped, ResourceBundle rb) {
        logger.info("* ingresarPedidoAlSistema");
        try {
            biz.ingresarPedidoASistema( ped.getId_pedido(), biz.clienteEsConfiable(ped.getRut_cliente()), ped.getRut_cliente() );
        } catch (ServiceException e) {
            logger.error("* ingresarPedidoAlSistema Err:"+e.getMessage());
        }        
        logger.info("* envio el mail *");
        try {
        	String mail_tpl=null;
        	if (ped.getTipo_despacho().equals("R")){
        		mail_tpl = rb.getString("mail.checkout.retiro");
        	}else{
        		mail_tpl = rb.getString("mail.checkout");
        	}
//            logger.info("mail_tpl:"+mail_tpl);
            TemplateLoader mail_load = new TemplateLoader(mail_tpl);
//            logger.info("mail_load:"+mail_load);
            ITemplate mail_tem = mail_load.getTemplate();
//            logger.info("mail_tem:"+mail_tem);
            String mail_result = mail_tem.toString(contenidoMailResumen(ped, biz));
//            logger.info("mail_result:"+mail_result);
            // Se envía mail al cliente
            MailDTO mail = new MailDTO();
            mail.setFsm_subject(ped.getNom_cliente()+rb.getString("mail.checkout.subject"));
            mail.setFsm_data(mail_result);
            JdbcClientesDAO dao = new JdbcClientesDAO();
            ClientesDTO cliente = new ClientesDTO();
            try{
            	cliente = biz.getClienteByRut( ped.getRut_cliente() );
            }catch (Exception e){
            	cliente = dao.getClienteById(ped.getId_cliente());
            }	
            logger.info("PEDIDO :" + ped.getId_pedido() + " mail > " + cliente.getEmail());
            mail.setFsm_destina(cliente.getEmail());
            mail.setFsm_remite(rb.getString("mail.checkout.remite"));
            biz.addMail(mail);
        } catch (Exception e) {
            logger.error("* envio el mail Err:"+e.getMessage());
//            logger.error("* Error:"+getStackTrace(e));            
        }        
    }
    
    public static String getStackTrace(Throwable t)    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
    
    private IValueSet contenidoMailResumen(PedidoDTO ped, BizDelegate biz) throws FuncionalException, ServiceException {
        PedidoDTO pedido1 = biz.getPedidoById(ped.getId_pedido());
        
        IValueSet mail_top = new ValueSet();
        mail_top.setVariable("{msg_time_compra}", "");
        mail_top.setVariable("{nombre_cliente}", pedido1.getNom_cliente());

        //SOLO PARA EL CASO DEL BLOQUE SEL_CUOTA
        IValueSet lista_boton = new ValueSet();
        lista_boton.setVariable("{contador_form}", "");
        List list_boton = new ArrayList();
        list_boton.add(lista_boton);

        mail_top.setVariable("{idped}", pedido1.getId_pedido() + pedido1.getSecuenciaPago());
        mail_top.setVariable("{cantidad}", formatoCantidad(pedido1.getCant_prods()) + "");
       
        mail_top.setVariable("{fecha_ingreso}", cl.bbr.jumbocl.common.utils.Utils.getFechaActualByPatron("dd/MM/yyyy"));
        mail_top.setVariable("{monto_op}", formatoPrecio(pedido1.getMonto()) +"" );
        mail_top.setVariable("{monto_despacho}", formatoPrecio(pedido1.getCosto_despacho()) +"" );
        mail_top.setVariable("{monto_reservado}", formatoPrecio(pedido1.getMonto_reservado()) +"" );
        mail_top.setVariable("{sin_gente_txt}", pedido1.getSin_gente_txt().replaceAll("\\+"," ")+"");
        List textoDespacho = new ArrayList();
        IValueSet filaDespacho = new ValueSet();
if (pedido1.getTipo_despacho().equalsIgnoreCase("R")) {
		    
			LocalDTO loc = biz.getLocalRetiro(pedido1.getId_local());
			
		    String direc= loc.getDireccion()!=null?loc.getDireccion():"";
		    String indic= pedido1.getIndicacion()!=null?pedido1.getIndicacion():"";
		    
			mail_top.setVariable("{lugar_despacho}", indic +" "+direc);
			mail_top.setVariable("{id_local}", loc.getId_local()+"");
			mail_top.setVariable("{sin_gente_rut}", cl.bbr.jumbocl.common.utils.Formatos.formatoRut( pedido1.getSin_gente_rut() ) +"-"+pedido1.getSin_gente_dv());
		
		}else{
			
		    if ( pedido1.getDir_depto().length() > 0 ) {
				
				mail_top.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle()+" "+pedido1.getDir_calle()
									+" "+pedido1.getDir_numero()+", "+pedido1.getDir_depto()+", "+pedido1.getNom_comuna());
			} else {
				
				mail_top.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle()+" "+pedido1.getDir_calle()
									+" "+pedido1.getDir_numero()+", "+pedido1.getNom_comuna());
				
			}
		}
        
        
        if ( pedido1.getMedio_pago().compareTo("CAT") == 0 ) {
        	BotonPagoDTO bp = biz.botonPagoGetByPedido(pedido1.getId_pedido());

            //******** PAGO CON TARJETAS MAS **********                    
        	mail_top.setVariable("{forma_pago}", "Tarjeta Mas" );
		    mail_top.setVariable("{4_ultimos}", (bp.getNroTarjeta()==null?"":"**** **** **** "+bp.getNroTarjeta().substring(bp.getNroTarjeta().length()-4)));
			mail_top.setVariable("{cod_aut_trx}", bp.getCodigoAutorizacion());
			mail_top.setVariable("{nro_cuotas}", cl.bbr.jumbocl.common.utils.Utils.secuenciaStr(bp.getNroCuotas().intValue()));
            
        } else {
            //*********** PAGO CON TRANSBANK *************
			WebpayDTO wp = biz.webpayGetPedido(ped.getId_pedido());
			
			mail_top.setVariable("{forma_pago}", "Tarjeta Bancaria" );
			mail_top.setVariable("{4_ultimos}", "**** **** **** "+wp.getTBK_FINAL_NUMERO_TARJETA());
			mail_top.setVariable("{cod_aut_trx}", wp.getTBK_CODIGO_AUTORIZACION());
			mail_top.setVariable("{nro_cuotas}", cl.bbr.jumbocl.common.utils.Utils.secuenciaStr(wp.getTBK_NUMERO_CUOTAS()));
              
        }
        
        String fecha = cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido1.getFdespacho());

        // Cuando es despacho Economico se debe mostrar la Hora de Inicio y Fin de la Jornada completa del día. Ej.: 9:00 - 23:00
        // E: Express, N: Normal, C: Económico
        //'C': Completa (09:00 - 23:00), 'P': Parcial (14:00 - 19:00)
        if (pedido1.getTipo_despacho().equalsIgnoreCase("C")) {
            mail_top.setVariable("{fecha_tramo}", fecha + "");
        } else {
            int posIni = pedido1.getHdespacho().indexOf(":", 3);
            int posFin = pedido1.getHfindespacho().indexOf(":", 3);
            mail_top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + pedido1.getHdespacho().substring(0, posIni) + " - " + pedido1.getHfindespacho().substring(0, posFin));
        }
        
        if (pedido1.getTipo_doc().compareTo("B") == 0) {
            mail_top.setVariable("{tip_doc}", "Boleta");
        } else if (pedido1.getTipo_doc().compareTo("F") == 0) {
            mail_top.setVariable("{tip_doc}", "Factura");
        }
        
      //inicio cupon de descuento
    	
    	PedidoDTO pedido = new PedidoDTO();
    	long idPedido = pedido1.getId_pedido();
    	pedido = biz.getValidaCuponYPromocionPorIdPedido( idPedido );
    	
    	ArrayList listDescuento = new ArrayList();
    	
    	if( pedido != null ) {
    		
    		if( pedido.isCupon() || pedido.isPromocion() ) {
    			
    			mail_top.setVariable("{switch}", "block");
    		
    			List descuentos = biz.getDescuentosAplicados( idPedido );
    		
    			boolean textoCupon = true, textoPromocion = true, textoColaborador = true, isDespacho = true, isDescuento = true;
    		
    			int contador = 0;
    			
    			for ( int i = 0; i < descuentos.size(); i++ ) {
    		
    				DetallePedidoDTO dpd = ( DetallePedidoDTO ) descuentos.get( i );
    			
    				IValueSet fila = new ValueSet();
    			
    				if( !textoCupon || !textoPromocion || !textoColaborador ) {
    					fila.setVariable( Constantes.NOMBRE_DESCTO, "" );
    					fila.setVariable("{CELDA_TITULO}", "td_borderSinTitulo");	
    				}
    				
    				if( dpd.getCodPromo() == -2 && textoCupon ) {
    		
    					fila.setVariable( Constantes.NOMBRE_DESCTO, Constantes.TEXT_CUPON_DESCUENTO );
    					fila.setVariable("{CELDA_TITULO}", "td_borderTitulo");
    					textoCupon = false;
    	
    				}else if( dpd.getCodPromo() > 0 && textoPromocion ) {
    		
    					fila.setVariable( Constantes.NOMBRE_DESCTO, Constantes.TEXT_PROMOCION );
    					fila.setVariable("{CELDA_TITULO}", "td_borderTitulo");
    					textoPromocion = false;
    	
    				}else if( dpd.getCodPromo() == -1 && textoColaborador ) {
    		
    					fila.setVariable( Constantes.NOMBRE_DESCTO, Constantes.TEXT_COLABORADOR );
    					fila.setVariable("{CELDA_TITULO}", "td_borderTitulo");
    					textoColaborador = false;
    		
    				}
    	
    				if( dpd.getDescripcion().equals( Constantes.TEXT_DESCTO_DESPACHO ) ) {
    		//TODO creacion vista cupon por email
    					fila.setVariable( Constantes.DETALLE_DESCTO, dpd.getDescripcion());
    					fila.setVariable( Constantes.MONTO_DESCTO, "" );
    				
    				}
    				else {
    		
    					fila.setVariable( Constantes.DETALLE_DESCTO, dpd.getDescripcion() );
    					fila.setVariable( Constantes.MONTO_DESCTO, Formatos.formatoPrecio( dpd.getPrecio() ) );
    					
    				}
    	
    				if( dpd.getCodPromo() == -2 && !dpd.getDescripcion().equals( Constantes.TEXT_DESCTO_DESPACHO ) && isDescuento ) {
    		
    					LogPedidoDTO log = new LogPedidoDTO();
    					
    					log.setId_pedido( idPedido );
    					log.setUsuario( Constantes.SYSTEM );
    					log.setLog( Constantes.TEXT_DESCTO_APLICADO + pedido.getCodigoCupon() + POR + Formatos.formatoPrecio( dpd.getPrecio() ) );
    	     
    					try { 
    						
    						biz.addLogPedido( log );
    					
    					} catch ( Exception e ) {
    	            
    						logger.info( "(" +idPedido+ ") No se pudo agregar información al log del pedido" );
    	        
    					}
    	     
    					isDespacho = false;
    	
    				}else if( dpd.getCodPromo() == -2 && dpd.getDescripcion().equals( Constantes.TEXT_DESCTO_DESPACHO ) && isDespacho ) {
    	
    					LogPedidoDTO log = new LogPedidoDTO();
    	 	
    					log.setId_pedido( idPedido );
    					log.setUsuario( Constantes.SYSTEM );
    					log.setLog( Constantes.TEXT_DESCTO_APLICADO + pedido.getCodigoCupon() + Constantes.TEXT_COSTO_DESPSCHO );
    	     
    					try { 
    						
    						biz.addLogPedido( log );
    					
    					} catch ( Exception e ) {
    						
    						logger.info( "(" +idPedido+ ") No se pudo agregar información al log del pedido" );
    	            
    					}
    	         
    	         		isDescuento = false;
    		
    				}
    				
    				if(contador%2!=0){
    				    fila.setVariable("{CLASE_CELDA}","tdProductoPar");
    					
    				}else{
    					fila.setVariable("{CLASE_CELDA}","celda1");
    					
    				}
    				
    				contador++;
    				
    				listDescuento.add( fila );
    				
    			}
    	
    			mail_top.setDynamicValueSets( "DESCUENTOS", listDescuento );
    		
    		
    		}else {
    			
    			mail_top.setVariable("{switch}", "none");
    				
    		}
    	
    	}

        //Categorías y Productos
        List fm_cate = new ArrayList();
        int contador = 0;
        long totalizador = 0;

        List productosPorCategoria = biz.getProductosSolicitadosById(pedido1.getId_pedido());

        //total_producto_pedido = 0;
        double precio_total = 0;
        
		//total_producto_pedido = 0;
		
		List fm_prod = new ArrayList();
		for (int i = 0; i < productosPorCategoria.size(); i++) {
			
			CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) productosPorCategoria.get(i);
			List prod = cat.getCarroCompraProductosDTO();
			
			for (int j = 0; j < prod.size(); j++) {
			
			   
				CarroCompraProductosDTO producto = (CarroCompraProductosDTO) prod.get(j);
				//  total_producto_pedido += Math.ceil(producto.getCantidad());
				IValueSet fila_pro = new ValueSet();
				fila_pro.setVariable("{descripcion}", producto.getNombre());
				fila_pro.setVariable("{marca}", producto.getMarca());
				fila_pro.setVariable("{cod_sap}", producto.getCodigo());
				fila_pro.setVariable("{valor}", Formatos.formatoIntervalo(producto.getCantidad()) + "");
				fila_pro.setVariable("{carr_id}", producto.getCar_id() + "");
				fila_pro.setVariable("{contador}", contador + "");
				precio_total = 0;
				// Existe STOCK para el producto
				if (producto.getStock() != 0) {
					// Si el producto es con seleccion
					if (producto.getUnidad_tipo().charAt(0) == 'S') {
						
						IValueSet fila_lista_sel = new ValueSet();
						List aux_lista = new ArrayList();
						for (double v = 0; v <= producto.getInter_maximo(); v += producto.getInter_valor()) {
							IValueSet aux_fila = new ValueSet();
							aux_fila.setVariable("{valor}",Formatos.formatoIntervalo(v)+ "");
							aux_fila.setVariable("{opcion}",Formatos.formatoIntervalo(v)+ "");
							if (Formatos.formatoIntervalo(v).compareTo(Formatos.formatoIntervalo(producto.getCantidad())) == 0)
								aux_fila.setVariable("{selected}","selected");
							else
								aux_fila.setVariable("{selected}", "");
							aux_lista.add(aux_fila);
						}
						fila_lista_sel.setVariable("{contador}", contador + "");
						fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);
						
						List aux_blanco = new ArrayList();
						aux_blanco.add(fila_lista_sel);
						fila_pro.setDynamicValueSets("LISTA_SEL", aux_blanco);
						
					} else {
						
						IValueSet fila_lista_sel = new ValueSet();
						fila_lista_sel.setVariable("{contador}", contador + "");
						fila_lista_sel.setVariable("{valor}",producto.getCantidad() + "");
						fila_lista_sel.setVariable("{maximo}",producto.getInter_maximo() + "");
						fila_lista_sel.setVariable("{intervalo}", producto.getInter_valor()+ "");
						List aux_blanco = new ArrayList();
						aux_blanco.add(fila_lista_sel);
						fila_pro.setDynamicValueSets("INPUT_SEL", aux_blanco);
						
					}
					
				
					precio_total = Math.round(producto.getPpum() * producto.getCantidad());
					fila_pro.setVariable("{unidad}", producto.getTipre());
					fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecio(producto.getPpum()));
					fila_pro.setVariable("{precio_total}",Formatos.formatoPrecio(precio_total));
					fila_pro.setVariable("{CLASE_TABLA}","TablaDiponiblePaso1");
					
					if(contador%2!=0){
					    fila_pro.setVariable("{CLASE_CELDA}","tdProductoPar");
						
					}else{
						fila_pro.setVariable("{CLASE_CELDA}","celda1");
						
					}
					
					fila_pro.setVariable("{NO_DISPONIBLE}", "");
					fila_pro.setVariable("{OPCION_COMPRA}", "1");
					totalizador += precio_total;
					
					contador++;
					fm_prod.add(fila_pro);
					
				}
				
			}
		
		}
		mail_top.setDynamicValueSets("PRODUCTOS", fm_prod);
        return mail_top;
    }
    
    
    public String formatoPrecio(double num) {

        String numero = Math.round(num) + "";
        String numero_out = "";
        int j = 1;
        for (int i = numero.length(); i > 0; i--) {
            numero_out += numero.charAt(i - 1) + "";
            if (j != numero.length() && j % 3 == 0)
                numero_out += ".";
            j++;
        }
        numero = "$";
        for (int i = numero_out.length() - 1; i >= 0; i--)
            numero += numero_out.charAt(i);
        return numero;

    }

    public long formatoCantidad(double num) {
        return Math.round(Math.ceil(num));
    }

    public long redondear( double valor ) {        
        return Math.round(valor);        
    }

    public String formatoIntervalo(double num) {
        double num_aux = Math.rint(num * 1000) / 1000;
        if (Math.floor(num_aux) == num_aux)
            return Math.round(Math.ceil(num_aux)) + "";
        return Math.rint(num_aux * 1000) / 1000 + "";

    }

    public void envioDeMailBOCCambiados(List pedidosCambiados, BizDelegate biz, ResourceBundle rb) {
        logger.info("* envioDeMailBOC - Cambiados *");
        
        MailDTO mail = new MailDTO();
        mail.setFsm_subject( rb.getString("reingreso.titulo") );
        mail.setFsm_destina( rb.getString("reingreso.destinatario") );
        mail.setFsm_copia( rb.getString("reingreso.copia") );
        mail.setFsm_remite( rb.getString("reingreso.remitente") );
        
        String body = rb.getString("reingreso.texto");
        
        String msg1 = "";
        if ( pedidosCambiados.size() > 0 ) {
            msg1 = rb.getString("reingreso.pedidos.cambiados").toString().replaceAll("@pedidos_cambiados", listaPedidos(pedidosCambiados));    
        }
        body = body.replaceAll("@mensaje_1",msg1);
        body = body.replaceAll("@mensaje_2","");
        
        mail.setFsm_data( body );
        try {
            biz.addMail(mail);
        } catch (ServiceException e) {
            logger.error("Error envioDeMailBOCCambiados:"+e.getMessage());
            logger.error(getStackTrace(e));            
        }
        
    }
    
    public void envioDeMailBOCError(List pedidosConError, BizDelegate biz, ResourceBundle rb) {
        logger.info("* envioDeMailBOC - Error *");
        
        MailDTO mail = new MailDTO();
        mail.setFsm_subject( "Pedidos ERROR re-ingresados" );
        mail.setFsm_destina( rb.getString("reingreso.dest_sist") );
        mail.setFsm_remite( rb.getString("reingreso.remitente") );
        
        String body = rb.getString("reingreso.texto");
        
        String msg2 = "";
        if ( pedidosConError.size() > 0 ) {
            msg2 = rb.getString("reingreso.pedidos.error").toString().replaceAll("@pedidos_error", listaPedidos(pedidosConError));    
        }
        body = body.replaceAll("@mensaje_1","");
        body = body.replaceAll("@mensaje_2",msg2);
        
        mail.setFsm_data( body );
        try {
            biz.addMail(mail);
        } catch (ServiceException e) {
            logger.error("Error envioDeMailBOCError:"+e.getMessage());
            logger.error(getStackTrace(e));            
        }
        
    }

    /**
     * @param pedidosCambiados
     * @return
     */
    private String listaPedidos(List pedidosProcesados) {
        String pedidos = "";
        String coma = "";
        for (int i=0; i < pedidosProcesados.size(); i++) {
            pedidos += ( coma + pedidosProcesados.get(i).toString() );
            coma = ",";
        }        
        return pedidos;
    }

    
 
}
