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
import cl.bbr.fo.util.Cod_error;
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;

/**
 * Despliega resumen del pedido
 *  
 */
public class OrderDisplay extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
 	
		try {
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("Pedido", arg0);
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
			
			String horasDespachoEconomico = "";
			if ( arg0.getParameter("horas_desp_eco") != null ) {
			    horasDespachoEconomico = arg0.getParameter("horas_desp_eco");
			}
            
            int prodSustitutosAntiguos = 0;
            if ( arg0.getParameter("psa") != null ) {
                prodSustitutosAntiguos = Integer.parseInt( arg0.getParameter("psa") );
            }
            int prodSustitutosNuevos = 0;
            if ( arg0.getParameter("psn") != null ) {
                prodSustitutosNuevos = Integer.parseInt( arg0.getParameter("psn") );
            }
			
			// Recupera pagina desde web.xml
			String pag_form_logueado = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form_logueado");
            String pag_form_invitado = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form_invitado");
            
			TemplateLoader load;
            System.out.println("ses_invitado_id = " + session.getAttribute("ses_invitado_id"));
            if ((session.getAttribute("ses_invitado_id") != null) && (!session.getAttribute("ses_invitado_id").toString().equals("0")))
                load = new TemplateLoader(pag_form_invitado);
            else
                load = new TemplateLoader(pag_form_logueado);
			ITemplate tem = load.getTemplate();
			
			IValueSet top = new ValueSet();
			IValueSet fila = null;
			IValueSet ch_prod = null;
			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
            long idPedido = Long.parseLong(session.getAttribute("ses_id_pedido").toString());
            
            //Seteo datos cliente para chaordic_meta
    		top.setVariable("{ses_cli_id}", (null == session.getAttribute("ses_cli_id") ? "1" : session.getAttribute("ses_cli_id")));
    		top.setVariable("{ses_cli_nombre}", (null == session.getAttribute("ses_cli_nombre") ? "Invitado" : session.getAttribute("ses_cli_nombre")));
    		top.setVariable("{ses_cli_email}", (null == session.getAttribute("ses_cli_email") || "".equals(session.getAttribute("ses_cli_email")) ? "invitado@invitado.cl" : session.getAttribute("ses_cli_email")));
			
            if (session.getAttribute("ses_cli_nombre").toString().equalsIgnoreCase("Invitado")){
				top.setVariable("{nombre_cliente}", session.getAttribute("nombre_inv")+" "+session.getAttribute("ape_pat_inv"));
			}else{
				top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			}
			if(session.getAttribute("ses_cli_nombre").toString().indexOf(" ") != -1) {
        		top.setVariable("{nombre_cliente_header}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
        		top.setVariable("{nombre_cliente_gracias}", session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
        	} else {
        		top.setVariable("{nombre_cliente_header}", session.getAttribute("ses_cli_nombre").toString());
        		top.setVariable("{nombre_cliente_gracias}", session.getAttribute("ses_cli_nombre").toString());
        	}     
			//top.setVariable("{nombre_cliente_header}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
			//top.setVariable("{nombre_cliente_gracias}", session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
            top.setVariable("{email}", session.getAttribute("ses_cli_email").toString());
            top.setVariable("{telefono}", session.getAttribute("ses_telefono").toString());
            top.setVariable("{tel_despacho}", session.getAttribute("tel_despacho").toString());
            
            
            String[] listCod = {
                    "5", "6", "7", "8", "9",
                    "02", "32", "33", "34", "35", "41", 
                    "42", "43", "45", "51", "52", "53", 
                    "55", "57", "58", "61", "63", "64", 
                    "65", "67", "71", "72", "73", "75"};
            ArrayList listadoCodigos = new ArrayList();
            String cod_telefono = session.getAttribute("tel_despacho").toString();
            for(int i=0; i<listCod.length; i++){
                IValueSet listCodFono = new ValueSet();
                listCodFono.setVariable("{cod_fono}" , listCod[i]);

            	if(cod_telefono.equals(listCod[i])){
                    listCodFono.setVariable("{cod_selected}" , "selected");
                }else{
                    listCodFono.setVariable("{cod_selected}" ,"");
                }
                listadoCodigos.add(listCodFono);
            }
            top.setDynamicValueSets("CODIGOS_TELEFONOS", listadoCodigos);
            
            
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			PedidoDTO pedido = biz.getPedidoById( idPedido );
			//inicio recuperacion datos cliente invitado para su registro--------------------------
            if(session.getAttribute("rut1_inv")!=null){
            	top.setVariable("{rut1_inv}", session.getAttribute("rut1_inv"));
            	top.setVariable("{nombre_inv}", session.getAttribute("nombre_inv"));
            	top.setVariable("{ape_pat_inv}", session.getAttribute("ape_pat_inv"));
            	top.setVariable("{email_inv}", session.getAttribute("email_inv"));
            	top.setVariable("{fono_num_1_inv}", session.getAttribute("fono_num_1_inv"));
            	top.setVariable("{fono_cod_1_inv}", session.getAttribute("fono_cod_1_inv"));
            	top.setVariable("{fono_num_2_inv}", session.getAttribute("fono_num_2_inv"));
            	top.setVariable("{fono_cod_2_inv}", session.getAttribute("fono_cod_2_inv"));
            	String rut_s = session.getAttribute("rut1_inv").toString();
                this.getLogger().info("Intento conexion ( RUT:" + rut_s + " )");
                
                rut_s = rut_s.replaceAll("\\.","").replaceAll("\\-","");
                rut_s = rut_s.substring(0, rut_s.length()-1);
                
    			long rut = Long.parseLong(rut_s);
    			
                ClienteDTO clienteinv = null;
                clienteinv = (ClienteDTO) biz.clienteGetByRut(rut);
                String mostrarLightboxRegistro=null;
                if (!(clienteinv == null)) {
                	mostrarLightboxRegistro="false";
                } else { 
                	mostrarLightboxRegistro="true";
                }
                top.setVariable("{mostrarLightboxRegistro}", mostrarLightboxRegistro);
                
                String[] listCod1 = {
                        "5", "6", "7", "8", "9",
                        "02", "32", "33", "34", "35", "41", 
                        "42", "43", "45", "51", "52", "53", 
                        "55", "57", "58", "61", "63", "64", 
                        "65", "67", "71", "72", "73", "75"};
                ArrayList listadoCodigos1 = new ArrayList();
                String cod_telefono1 = session.getAttribute("fono_cod_1_inv").toString();
                for(int i=0; i<listCod1.length; i++){
                    IValueSet listCodFono1 = new ValueSet();
                    listCodFono1.setVariable("{cod_fono}" , listCod1[i]);

                	if(cod_telefono1.equals(listCod1[i])){
                        listCodFono1.setVariable("{cod_selected}" , "selected");
                    }else{
                        listCodFono1.setVariable("{cod_selected}" ,"");
                    }
                    listadoCodigos1.add(listCodFono1);
                }
                top.setDynamicValueSets("CODIGOS_TELEFONOS1", listadoCodigos1);
            	if (!(pedido.getTipo_despacho().equalsIgnoreCase("R"))) {
            		top.setVariable("{tipo_calle_inv}", session.getAttribute("tipo_calle_inv"));
            		top.setVariable("{calle_inv}", session.getAttribute("calle_inv"));
            		top.setVariable("{numero_inv}", session.getAttribute("numero_inv"));
            		top.setVariable("{departamento_inv}", session.getAttribute("departamento_inv"));
            		top.setVariable("{region_inv}", session.getAttribute("region_inv"));
            		top.setVariable("{comuna_inv}", session.getAttribute("comuna_inv"));
            		top.setVariable("{alias_inv}", session.getAttribute("alias_inv"));
            		
            		ArrayList arr_regiones = new ArrayList();
        			List regiones = biz.regionesConCobertura();
                    for (int i = 0; i < regiones.size(); i++) {
        				RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
        				IValueSet fila1 = new ValueSet();
        				fila1.setVariable("{option_reg_id}", dbregion.getId()+"");
        				fila1.setVariable("{option_reg_nombre}", dbregion.getNombre());
        				if (Long.toString(dbregion.getId()).equals(session.getAttribute("region_inv").toString())){
        					fila1.setVariable("{cod_reg_select}", "selected");					
        				} else{
        					fila1.setVariable("{cod_reg_select}", "");	
        				}
        				arr_regiones.add(fila1);
        			}
        			top.setDynamicValueSets("select_regiones", arr_regiones);
        			
            	}
            }
            //fin recuperacion datos cliente invitado para su registro---------------------------------
                        
            if ( pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO ) {
                //Error no controlado, cuando llaman a ver el resumen de una OP que este preingresado
                throw new CommandException();
            }
            
			if ( pedido != null ) {
                
				RegionesDTO region = new RegionesDTO();
                if ( pedido.getId_comuna() > 0 ) {
                    region = biz.regionesGetRegion(pedido.getId_comuna());
                }
                if (session.getAttribute("region_inv")==null){
                	ArrayList arr_regiones = new ArrayList();
        			List regiones = biz.regionesConCobertura();
                    for (int i = 0; i < regiones.size(); i++) {
        				RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
        				IValueSet fila1 = new ValueSet();
        				fila1.setVariable("{option_reg_id}", dbregion.getId()+"");
        				fila1.setVariable("{option_reg_nombre}", dbregion.getNombre());
        				if (Long.toString(dbregion.getId()).equals(Long.toString(region.getId()))){
        					fila1.setVariable("{cod_reg_select}", "selected");					
        				} else{
        					fila1.setVariable("{cod_reg_select}", "");	
        				}
        				arr_regiones.add(fila1);
        			}
        			top.setDynamicValueSets("select_regiones", arr_regiones);
                	String[] loc = session.getAttribute("ses_comuna_cliente").toString().split("-=-");                	
            		top.setVariable("{comuna_inv}", loc[1]);
                }
				session.setAttribute("sesspedido", pedido.getId_pedido()+"");
				//top.setVariable("{idped}", Utils.numOP(Long.parseLong(rb.getString("op.funcion.transformacion.a")),Long.parseLong(rb.getString("op.funcion.transformacion.b")),pedido.getId_pedido())+"");
                top.setVariable("{idped}", pedido.getId_pedido()+pedido.getSecuenciaPago());
				
                if (pedido.getTipo_despacho().equalsIgnoreCase("R")){
                	top.setVariable("{mx_despacho}",  "Retiro");
                }
                else{
                	top.setVariable("{mx_despacho}",  "Domicilio");	
                }
                List textoDespacho = new ArrayList();
                IValueSet filaDespacho = new ValueSet();
				if (pedido.getTipo_despacho().equalsIgnoreCase("R")) {
                    top.setVariable("{fc_hr_despacho}","Fecha y hora de retiro");
                    top.setVariable("{forma_despacho}", "Dirección de Retiro");
                    top.setVariable("{lugar_despacho}",    pedido.getIndicacion());
                    LocalDTO loc = biz.getLocalRetiro(pedido.getId_local());
                    top.setVariable("{dir_retiro_pedido}", "("+loc.getDireccion()+")");
                    top.setVariable("{ver_mas}", "Ver Más");
                    filaDespacho.setVariable("{id_local}", loc.getId_local()+"");
                    
                    //textoDespacho.add(filaDespacho);
                    //top.setDynamicValueSets("LUGAR_RETIRO", textoDespacho);
                    
                    textoDespacho = new ArrayList();                    
                    filaDespacho = new ValueSet();                    
                    filaDespacho = new ValueSet();
                    String nombreRetira= new String(pedido.getSin_gente_txt());
                    String signoMas = "\\+";
                    nombreRetira = nombreRetira.replaceAll(signoMas, " ");
                    filaDespacho.setVariable("{sin_gente_txt}", nombreRetira+"");
                    filaDespacho.setVariable("{sin_gente_rut}", cl.bbr.jumbocl.common.utils.Formatos.formatoRut( pedido.getSin_gente_rut() ) +"-"+pedido.getSin_gente_dv());
                    textoDespacho.add(filaDespacho);
                    top.setDynamicValueSets("SIN_GENTE_RETIRO", textoDespacho);
                } else {
                    top.setVariable("{fc_hr_despacho}","Fecha y hora de despacho");
                    top.setVariable("{forma_despacho}", "Dirección de Despacho");
                    if ( pedido.getDir_depto().length() > 0 ) {
                    	top.setVariable("{lugar_despacho}", pedido.getDir_tipo_calle()+" "+pedido.getDir_calle()+" "+pedido.getDir_numero()+", "+pedido.getDir_depto()+", "+pedido.getNom_comuna());
                    } else {
                    	top.setVariable("{lugar_despacho}", pedido.getDir_tipo_calle()+" "+pedido.getDir_calle()+" "+pedido.getDir_numero()+", "+pedido.getNom_comuna());
                    }
                    top.setVariable("{dir_retiro_pedido}", "");
                    top.setVariable("{ver_mas}", "");
                    //textoDespacho.add(filaDespacho);
                    //top.setDynamicValueSets("LUGAR_DESPACHO", textoDespacho);

                    
                    textoDespacho = new ArrayList();
                    filaDespacho = new ValueSet();                    
                    filaDespacho = new ValueSet();
                    String nombreRetira= new String(pedido.getSin_gente_txt());
                    String signoMas = "\\+";
                    nombreRetira = nombreRetira.replaceAll(signoMas, " ");
                    filaDespacho.setVariable("{sin_gente_txt}", nombreRetira+"");
                    filaDespacho.setVariable("{sin_gente_rut}", "");
                    textoDespacho.add(filaDespacho);
                    top.setDynamicValueSets("SIN_GENTE_DESPACHO", textoDespacho);
                }
				
				if ( pedido.getMedio_pago().compareTo("CAT") == 0 ) {
                    //******** PAGO CON TARJETAS MAS **********                    
                    top.setVariable("{forma_pago}", "Tarjeta Mas" );

                    // se obtiene info desde la BD para mostrar en resumen
                    BotonPagoDTO bp = biz.botonPagoGetByPedido(idPedido);
                    
                    /*List pagoConTmas = new ArrayList();
                    IValueSet filaTmas = new ValueSet();                    
                    filaTmas.setVariable("{4_ultimos}", (bp.getNroTarjeta()==null?"":"**** **** **** "+bp.getNroTarjeta().substring(bp.getNroTarjeta().length()-4)));
                    filaTmas.setVariable("{cod_aut_trx}", bp.getCodigoAutorizacion());
                    filaTmas.setVariable("{nro_cuotas}", Utils.secuenciaStr(bp.getNroCuotas().intValue()));
                    filaTmas.setVariable("{tipo_cuotas}", "----");
                    pagoConTmas.add(filaTmas);
                    top.setDynamicValueSets("PAGO_TRANSBANK", pagoConTmas);*/
                                       
                    top.setVariable("{4_ultimos}", (bp.getNroTarjeta()==null?"":"**** **** **** "+bp.getNroTarjeta().substring(bp.getNroTarjeta().length()-4)));
                    top.setVariable("{cod_aut_trx}", bp.getCodigoAutorizacion());
                    top.setVariable("{nro_cuotas}", Utils.secuenciaStr(bp.getNroCuotas().intValue()));
                    top.setVariable("{tipo_cuotas}", "----");

                    /*List tagTarjetasMasReMKT = new ArrayList();
                    IValueSet filaRMKT = new ValueSet();                    
                    filaRMKT.setVariable("{sin_gente_txt}", "");
                    tagTarjetasMasReMKT.add(filaRMKT);
                    top.setDynamicValueSets("TARJETAS_MAS", tagTarjetasMasReMKT);*/
				} else {
                    //*********** PAGO CON TRANSBANK *************
                    top.setVariable("{forma_pago}", "Tarjeta Bancaria" );
                    
                    WebpayDTO wp = biz.webpayGetPedido(idPedido);
                    
                    /*List pagoConTransbank = new ArrayList();
                    IValueSet filaTransbank = new ValueSet();                    
                    filaTransbank.setVariable("{4_ultimos}", "**** **** **** "+wp.getTBK_FINAL_NUMERO_TARJETA());
                    filaTransbank.setVariable("{cod_aut_trx}", wp.getTBK_CODIGO_AUTORIZACION());
                    filaTransbank.setVariable("{nro_cuotas}", Utils.secuenciaStr(wp.getTBK_NUMERO_CUOTAS()));
                    filaTransbank.setVariable("{tipo_cuotas}", Utils.webpayTipoCuotas(wp.getTBK_TIPO_PAGO()));
                    pagoConTransbank.add(filaTransbank);
                    top.setDynamicValueSets("PAGO_TRANSBANK", pagoConTransbank);*/
                    top.setVariable("{4_ultimos}", "**** **** **** "+wp.getTBK_FINAL_NUMERO_TARJETA());
                    top.setVariable("{cod_aut_trx}", wp.getTBK_CODIGO_AUTORIZACION());
                    top.setVariable("{nro_cuotas}", Utils.secuenciaStr(wp.getTBK_NUMERO_CUOTAS()));
                    top.setVariable("{tipo_cuotas}", Utils.webpayTipoCuotas(wp.getTBK_TIPO_PAGO()));
				}
                top.setVariable("{fecha_lista}", Utils.getFechaActualByPatron("dd-MM-yyyy"));
                top.setVariable("{fecha_hoy}", Utils.getFechaActualByPatron("dd/MM/yyyy"));
                top.setVariable("{monto_op}", Formatos.formatoPrecioFO(pedido.getMonto()) +"" );
                top.setVariable("{monto_desp}", Formatos.formatoPrecioFO(pedido.getCosto_despacho()) +"" );
                top.setVariable("{monto_res}", Formatos.formatoPrecioFO(pedido.getMonto_reservado()) +"" );
				
				String fecha = cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido.getFdespacho());
				
				// Cuando es despacho Economico se debe mostrar la Hora de Inicio
				// y Fin de la Jornada completa del día. Ej.: 9:00 - 23:00
				// E: Express, N: Normal, C: Económico
				
				boolean flag = false;
				String DespHorarioEconomico = rb.getString("DespliegueHorarioEconomico");
				//'C': Completa (09:00 - 23:00), 'P': Parcial (14:00 - 19:00)
				if (DespHorarioEconomico.equalsIgnoreCase("C") && pedido.getTipo_despacho().equalsIgnoreCase("C") && !horasDespachoEconomico.equalsIgnoreCase("")){
				    top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + horasDespachoEconomico );
				    flag = true;
				}
				if (!flag){
				    int posIni = pedido.getHdespacho().indexOf(":", 3);
				    int posFin = pedido.getHfindespacho().indexOf(":", 3);
				    top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + pedido.getHdespacho().substring(0, posIni) + " - " + pedido.getHfindespacho().substring(0, posFin) );
				}
				top.setVariable("{cantidad}", Formatos.formatoCantidadFO(pedido.getCant_prods())+"" );
				
				if (pedido.getTipo_doc().compareTo("B") == 0){
					top.setVariable("{tip_doc}","Boleta");
				}else if(pedido.getTipo_doc().compareTo("F") == 0){
					top.setVariable("{tip_doc}","Factura");
				}
				
				//Código para Google Analytics
				top.setVariable("{order-id}",pedido.getId_pedido() + "");
				top.setVariable("{affiliation}",pedido.getNom_local());
				top.setVariable("{total}",pedido.getMonto() + "");
				double montoSinIva = pedido.getMonto()*0.81; //Calculamos el valor de compra sin iva.
				top.setVariable("{total_sin_iva}",montoSinIva + "");
				double montoIva = pedido.getMonto()*0.19; //Calculamos el valor iva de la compra.
				top.setVariable("{tax}",montoIva + "");
				top.setVariable("{shipping}",pedido.getCosto_despacho() + "");
				top.setVariable("{city}",pedido.getNom_comuna());
				top.setVariable("{state}",region.getNombre());
				top.setVariable("{country}","Chile");
				
				//Detalle de Productos para Google Analytics			

	            
	            List lproductos = biz.getProductosPedido(pedido.getId_pedido()); 
				List lista_prod = new ArrayList();
				List chaordic_prd = new ArrayList();
				// 05/10/2012 : INICIO COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 				
				ClienteDTO cliente=biz.clienteGetById(cliente_id);
                top.setVariable("{rutcliente}",String.valueOf(cliente.getId()));
                top.setVariable("{emailcliente}", cliente.getEmail());
                // 05/10/2012 : FIN COREMETRICS
				
                String criterio = "";
                //Trae las Categorìas
                
                List categoriaCompletaProd = (List) session.getAttribute("listProdCategComplet");
                String productDescr ="";
				for( int i = 0; i < lproductos.size(); i++ ) {
					fila = new ValueSet();
					ch_prod = new ValueSet();
					ProductosPedidoDTO producto = (ProductosPedidoDTO)lproductos.get(i);
					fila.setVariable("{order-id}", pedido.getId_pedido() + "");
					fila.setVariable("{sku/code}", producto.getCod_producto() + " " + producto.getUnid_medida());
					fila.setVariable("{productname}", producto.getDescripcion() + "");
					for( int j = 0; j < categoriaCompletaProd.size(); j++ ){
						productDescr = categoriaCompletaProd.get(j).toString().substring(categoriaCompletaProd.get(j).toString().lastIndexOf("/")+1,categoriaCompletaProd.get(j).toString().length());
						if(productDescr.equals(producto.getDescripcion())){
							fila.setVariable("{categoryComplete}", categoriaCompletaProd.get(j).toString().substring(0,categoriaCompletaProd.get(j).toString().lastIndexOf("/")));

						}
					}
					if (producto.getTipoSel() != null){
						if (producto.getTipoSel().equals("U")) {
							fila.setVariable("{category}", "Ultimas Compras");
						} else if (producto.getTipoSel().equals("G")) {
							fila.setVariable("{category}", "Góndola");
						} else if (producto.getTipoSel().equals("W")) {
							fila.setVariable("{category}", "Ficha Producto");
						} else {
							fila.setVariable("{category}", "category");
						}
					} else {
						fila.setVariable("{category}", "category");
					}
					
					
					
					//Tags Analytics - Obtener Criterio del Carro
	                if(criterio!="Hibrido"){
	                	if((producto.getDescCriterio().equalsIgnoreCase("No Sustituir"))&&(criterio.equalsIgnoreCase("Criterio Jumbo"))||(producto.getDescCriterio().equalsIgnoreCase("Criterio Jumbo"))&&(criterio.equalsIgnoreCase("No Sustituir"))){
	                		criterio="Hibrido";
	                	}
	                	else{
	                		criterio=producto.getDescCriterio();
	                	}
	 
	                }
	                top.setVariable("{mx_criterio}", criterio); //Tags Analytics - Criterio del Carro
					fila.setVariable("{price}", producto.getPrecio() + "");
					fila.setVariable("{quantity}", producto.getCant_solic() + "");
					fila.setVariable("{rutcliente}",String.valueOf(cliente.getId())); // 05/10/2012 : INICIO COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 	

					lista_prod.add(fila);
					
					//seteo producto para generar chaordic_meta
					ch_prod.setVariable("{id_prod}", producto.getId_producto() + "");
					ch_prod.setVariable("{precio}", producto.getPrecio() + "");
					ch_prod.setVariable("{cantidad}", producto.getCant_solic() + "");
					chaordic_prd.add(ch_prod);
					
				}
				top.setDynamicValueSets("LISTA_PROD", lista_prod);
				top.setDynamicValueSets("CHAORDIC_PROD", chaordic_prd);
			}
			else {
				session.setAttribute("cod_error", Cod_error.CHECKOUT_NO_PEDIDO);
			}
			List lista_base = new ArrayList();
			List reg_lista_base = biz.compraHistoryGetList( cliente_id );
			UltimasComprasDTO data = null;
			for( int i = 0; i < reg_lista_base.size() ; i++ ) {
				data = (UltimasComprasDTO) reg_lista_base.get(i);
				fila = new ValueSet();
				fila.setVariable("{id}",  data.getId()+"" );
				fila.setVariable("{nombre}", data.getNombre());
				lista_base.add(fila);
			}
			top.setDynamicValueSets("LISTA", lista_base);
            
            List msgSustitutos = new ArrayList();
            IValueSet filaSust = new ValueSet();
            filaSust.setVariable("{cant_productos_nuevos}",  prodSustitutosNuevos+"" );
            msgSustitutos.add(filaSust);
            if (prodSustitutosAntiguos == 0) {
                //Cliente que nunca ha definido sustitutos
                top.setDynamicValueSets("MSG_SUSTITUTOS_NEW", msgSustitutos);                
            } else {
                if ( prodSustitutosNuevos > 0 ) {
                    top.setDynamicValueSets("MSG_SUSTITUTOS_OLD_CON", msgSustitutos);
                } else {
                    top.setDynamicValueSets("MSG_SUSTITUTOS_OLD_SIN", msgSustitutos);
                }
            }            
			
			// --- INI - LAYER EVENTOS PERSONALIZADOS - RESUMEN ---
			/*String codLayerFlash = "";
			EventoDTO evento = new EventoDTO(); 
			if ( session.getAttribute("codLayerFlash") != null ) {
			    codLayerFlash = session.getAttribute("codLayerFlash").toString();    
			    if ( session.getAttribute("eventoLayerFlash") != null ) {
				    evento = (EventoDTO) session.getAttribute("eventoLayerFlash");    
				}
			}
			if ( codLayerFlash.length() > 0 ) {
			    top.setVariable("{titulo_evento}",	UtilsEventos.tituloMostrar( evento.getTitulo(), session.getAttribute("ses_cli_nombre_pila").toString() ));
				top.setVariable("{mostrar_evento}", "SI");
		    } else {
		        top.setVariable("{titulo_evento}",	"");
		        top.setVariable("{mostrar_evento}", "NO");
		    }
		    top.setVariable("{src_flash}", codLayerFlash);*/
	        // --- FIN - LAYER EVENTOS PERSONALIZADOS - RESUMEN ---
	        if ( session.getAttribute("codLayerFlash") != null ) {
	            session.removeAttribute("codLayerFlash");
	        }
	        if ( session.getAttribute("eventoLayerFlash") != null ) {
	            session.removeAttribute("eventoLayerFlash");
	        }
			// ----
			
            if(top.getVariable("{nombre_cliente}").equals("Invitado")){
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
            
          //agregar lógica para capturar cupón de descuento.
            if (session.getAttribute("ses_cupon_descuento_object") != null) {
	   	         CuponDsctoDTO cddto = null;    
	   			 cddto = (CuponDsctoDTO) session.getAttribute("ses_cupon_descuento_object") ;
	   			 top.setVariable("{ta_cupon_descuento}", cddto.getCodigo());
	   			 session.setAttribute("ses_cupon_descuento_object", null);
				 logger.debug("Se ha destruido Cupón de Descuento en esta sesión");
	   		}
            else{
            	  top.setVariable("{ta_cupon_descuento}", "Sin Cupón");
        	}
            
            List promocionesAll= new ArrayList();
            if((top.getVariable("{forma_pago}").toString()).equalsIgnoreCase("Tarjeta Mas"))
                promocionesAll = (List) session.getAttribute("promocionesTMAS");
            else
                promocionesAll = (List) session.getAttribute("promocionesWEBPAY");

			List lista_desc = new ArrayList();
			String promoDcto = "";
			String promoDescr = "";
			for( int i = 0; i < promocionesAll.size(); i++ ) {
				IValueSet filaDesc = new ValueSet();
				IValueSet promocion = new ValueSet();
				promocion = (IValueSet) promocionesAll.get(i);
	            filaDesc.setVariable("{cod_promo}", promocion.getVariable("{cod_promo}"));
	            promoDescr = promocion.getVariable("{promo_descripcion}").toString();
	            promoDescr = promoDescr.replaceAll("<br>", "");
	            promoDescr = promoDescr.replaceAll("</br>", "");
	            filaDesc.setVariable("{promo_descripcion}", promoDescr);
	            promoDcto = promocion.getVariable("{promo_descuento}").toString();
	            for (int x =0; x<promoDcto.length(); x++){ 
	            	if (promoDcto.charAt(x)=='$'){ 
	            		promoDcto = promoDcto.replace('$',' '); 

	            	}
	            	if (promoDcto.charAt(x)=='.'){ 
	            		promoDcto = promoDcto.replace('.',' '); 

	            	}

	            }
	            promoDcto = promoDcto.replaceAll(" ", "");
	            filaDesc.setVariable("{promo_descuento}", "-"+promoDcto);
	            filaDesc.setVariable("{cant_prod_desc}", promocion.getVariable("{cant_prod_desc}"));

	            lista_desc.add(filaDesc);
			}
			top.setDynamicValueSets("LISTA_DESC", lista_desc);
			

                    
			String result = tem.toString(top);
			out.print(result);
		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException( e );
		}
	}
	
}