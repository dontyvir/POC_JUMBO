package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
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
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;


/**
 * Página que entrega un resumen de los items a pagar por el cliente
 * @author carriagada-IT4B
 * 
 */
public class Pago extends Command {
    
    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
        String emailError = "";
        HttpSession session = arg0.getSession();
        long total = 0;
        
        ResourceBundle rb = ResourceBundle.getBundle("fo");
        String errormailTo = rb.getString("mailerror.to");
        String errormailFrom = rb.getString("mailerror.from");
        String errormailCc = rb.getString("mailerror.cc");
        String errormailSmtp = rb.getString("mailerror.smtp");
        
		try {
            String carro = "";
            String carro2 = "";
            
			arg1.setHeader("Cache-Control", "no-cache");
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=iso-8859-1");
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();
            
			IValueSet top = new ValueSet();		
			
            if( session.getAttribute("cod_error") != null || arg0.getParameter("err") != null ) {
                top.setVariable("{error}", "1");
                
                if ( arg0.getParameter("err") != null ) {
                    if ( session.getAttribute("ses_id_pedido") != null ) {
                        top.setVariable("{mensaje_error}", "Transacción rechazada.\\nSu transacción no ha podido ser procesada, favor vuelva a intentarlo.\\nCorrobore números y fechas solicitadas, si el problema persiste comunique a su banco emisor o llámanos al 600 400 3000.");
                    }else{
                        top.setVariable("{mensaje_error}", "Transacción rechazada.\\ ....");
                    }
                }
            }else{
                top.setVariable("{error}", "0");
            }
            
			//Se setea el rut del cliente en la página para posibles validaciones con tarjeta paris
            top.setVariable("{rut_cli}", (String)session.getAttribute("ses_cli_rut"));
            top.setVariable("{dv_cli}", (String)session.getAttribute("ses_cli_dv"));
            
            Calendar ahora = new GregorianCalendar();
            String StrFecha1 = new SimpleDateFormat( Formatos.DATE_TIME).format(ahora.getTime());
            top.setVariable("{fechaactual}", StrFecha1 );
            
            top.setVariable("{nom_cliente}", session.getAttribute("ses_cli_nombre_pila").toString());
            top.setVariable("{ape_cliente}", session.getAttribute("ses_cli_apellido_pat").toString());
            if(session.getAttribute("ses_cli_nombre").toString().indexOf(" ") != -1) {
        		top.setVariable("{nombre_cliente}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
        	} else {
            top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
        	}     
            //top.setVariable("{nombre_cliente}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
            top.setVariable("{lug_desp}", session.getAttribute("ses_dir_alias").toString());
            
            top.setVariable("{cli_id}", session.getAttribute("ses_cli_id").toString());
            
            //Seteo datos cliente para chaordic_meta
    		top.setVariable("{ses_cli_id}", (null == session.getAttribute("ses_cli_id") ? "1" : session.getAttribute("ses_cli_id")));
    		top.setVariable("{ses_cli_nombre}", (null == session.getAttribute("ses_cli_nombre") ? "Invitado" : session.getAttribute("ses_cli_nombre")));
    		top.setVariable("{ses_cli_email}", (null == session.getAttribute("ses_cli_email") || "".equals(session.getAttribute("ses_cli_email")) ? "invitado@invitado.cl" : session.getAttribute("ses_cli_email")));
            
            //Coloca los datos de despacho en la página para procesar el pedido
            top.setVariable("{jpicking}", session.getAttribute("jpicking").toString());
            top.setVariable("{jdespacho}", session.getAttribute("jdespacho").toString());
            top.setVariable("{jprecio}", session.getAttribute("jprecio").toString());
            top.setVariable("{jfecha}", session.getAttribute("jfecha").toString());
            top.setVariable("{tdespacho}", session.getAttribute("tdespacho").toString());
            top.setVariable("{hh_economico}", session.getAttribute("ses_horas_economico").toString());
            top.setVariable("{zona_despacho}", session.getAttribute("ses_zona_id").toString());
            if (session.getAttribute("ses_forma_despacho").toString().equals("R")){
            	top.setVariable("{mx_despacho}",  "Retiro");
                top.setVariable("{sin_gente_rut}", session.getAttribute("rut_autorizado").toString());
                top.setVariable("{sin_gente_dv}", session.getAttribute("dv_autorizado").toString());
                top.setVariable("{retira_txt}", session.getAttribute("autorizacion").toString());
            } else if (session.getAttribute("ses_forma_despacho").toString().equals("D")){
            	top.setVariable("{mx_despacho}",  "Despacho");
                top.setVariable("{sin_gente_rut}", "");
                top.setVariable("{sin_gente_dv}", "");
                top.setVariable("{sin_gente_txt}", session.getAttribute("autorizacion").toString());
            }
            if (session.getAttribute("observacion") != null && !session.getAttribute("observacion").toString().equals("")){
                top.setVariable("{observacion}", session.getAttribute("observacion").toString());
            }else{
                top.setVariable("{observacion}", "");
            }
            
            top.setVariable("{webpay_url}", rb.getString("webpay.url") );
            top.setVariable("{boton_url}", rb.getString("boton.url") );
            
            if ( session.getAttribute("ses_eje_id") != null) {
                
            	// Si es fono compra, el kit webpay es distinto                
            	top.setVariable("{webpay_url_kit}", rb.getString("webpay.kit.fonocompra") );
                top.setVariable("{boton_url_kit}", rb.getString("boton.kit.ruta.fonocompra") );
                top.setVariable("{numero_empresa}", rb.getString("boton.numero.fono") );
            } else {
            	
            	String overrideWebPay = super.getWebPayOverride();
            	
            	if (ENABLE_OVERRIDE_WEBPAY.equals( overrideWebPay )) {
            		
            		top.setVariable("{webpay_url_kit}", rb.getString("webpay.kit.fonocompra") );
            	
            	} else {
            		
            		top.setVariable("{webpay_url_kit}", rb.getString("webpay.kit.cliente") );
            		
            	}
                
                top.setVariable("{boton_url_kit}", rb.getString("boton.kit.ruta.cliente") );
                top.setVariable("{numero_empresa}", rb.getString("boton.numero.cliente") );
            }
            

            List viewLogin = new ArrayList(); 
            IValueSet fila = new ValueSet();
            
            if ( session.getAttribute("ses_comuna_cliente") != null &&
                    !session.getAttribute("ses_comuna_cliente").toString().trim().equals("")) {
                String[] loc = session.getAttribute("ses_comuna_cliente").toString().split("-=-");
                fila.setVariable("{comuna_usuario}", "" + loc[2]);
                fila.setVariable("{comuna_usuario_id}", "" + loc[1]);
            }
            viewLogin.add(fila);
            
            if ( "1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
                top.setDynamicValueSets("MOSTRAR_NO_LOGUEADO", viewLogin); 
            } else {
                if ((session.getAttribute("ses_cli_rut") != null)  && (session.getAttribute("ses_cli_rut").equals("123123")))
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO_INVITADO", viewLogin);
                else
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO", viewLogin);  
            }
            
            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();
            long cli_id = 0L;
            String idSession = null;
            cli_id = Long.parseLong((String)session.getAttribute("ses_cli_id"));
            if (session.getAttribute("ses_invitado_id") != null)
                idSession = session.getAttribute("ses_invitado_id").toString();
            List listaCarro = biz.carroComprasPorCategorias(cli_id, session.getAttribute("ses_loc_id").toString(), idSession);
            
            long precio_total = 0;
            MiCarroDTO car;
            
            //Recuperar los cupones y los tcp de la sesión
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
            
            // Inicio cdd
            boolean aplicaCuponDscto = false;
            boolean isCuponDespacho = false;
            boolean isCuponProducto = false;
            boolean isCuponRubro = false;
            boolean isCuponSeccion = false;
            boolean isCuponCAT = false;
            boolean isCuponTBK = false;
            
            CuponDsctoDTO cddto = null;            
            List cuponProds = null;
            
            if (session.getAttribute("ses_cupon_descuento_object") != null) {
            	cuponProds = new ArrayList();
            	cddto = (CuponDsctoDTO) session.getAttribute("ses_cupon_descuento_object") ;
            	if (cddto.getTipo().equals("D")) {
            		isCuponDespacho = true;
            	} else if (cddto.getTipo().equals("P")){
            		isCuponProducto = true;
            		cuponProds = biz.getProdsCupon(cddto.getId_cup_dto(), "P");
            	} else if (cddto.getTipo().equals("R")){
            		isCuponRubro = true;
            		cuponProds = biz.getProdsCupon(cddto.getId_cup_dto(), "R");
            	} else if (cddto.getTipo().equals("S") || cddto.getTipo().equals("TS")){
            		isCuponSeccion = true;
            		cuponProds = biz.getProdsCupon(cddto.getId_cup_dto(), "S");
            	}
            	if (cddto.getMedio_pago() == 0) {
            		isCuponCAT = true;
            		isCuponTBK = true;
            	} else if (cddto.getMedio_pago() == 1) {
            		isCuponCAT = true;
            	} else {
            		isCuponTBK = true;
            	}
            }
            // Fin cdd
            
            if (listaCarro != null && listaCarro.size() > 0) {
                listaCarro = biz.cargarPromocionesMiCarro(listaCarro, l_torec, Integer.parseInt(session.getAttribute("ses_loc_id").toString()));
            }
            
            String criterio = "";
            //armamos carro aca tb
            for (int i = 0; i < listaCarro.size(); i++) {
                car = (MiCarroDTO) listaCarro.get(i);
                //Información del producto
                if (car.tieneStock()) {
                    precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
                    total += precio_total;
                    
                    carro2+="[cant. "+car.getCantidad()+"]"+car.getPro_id()+"/"+car.getNom_marca()+"/"+car.getTipo_producto()+"/"+car.getNombre()+"/"+car.getDescripcion()+"/Precio unit-total: "+car.getPrecio()+"-"+precio_total+"<br>";
                    
                }
              //Tags Analytics - Obtener Criterio del Carro
                if(criterio!="Hibrido"){
                	if((car.getDescripcion().equalsIgnoreCase("No Sustituir"))&&(criterio.equalsIgnoreCase("Criterio Jumbo"))||(car.getDescripcion().equalsIgnoreCase("Criterio Jumbo"))&&(criterio.equalsIgnoreCase("No Sustituir"))){
                		criterio="Hibrido";
                	}
                	else{
                		criterio=car.getDescripcion();
                	}
 
                }
                
                // Inicio cdd
                if (!aplicaCuponDscto) {
	                if (isCuponProducto){
	                	Iterator itProds = cuponProds.iterator();
	                	while (itProds.hasNext()) {
	                		long productoCupon = Long.parseLong(itProds.next().toString());
	                		if(productoCupon == car.getId_bo()){
								aplicaCuponDscto = true;
								if(cddto.getDespacho() == 1)
									isCuponDespacho = true;
								break;
							}
	                	}
	                } else if (isCuponRubro) {
	                	Iterator itProds = cuponProds.iterator();
	                	while (itProds.hasNext()) {
	                		int productoCupon = Integer.parseInt(itProds.next().toString());
	                		int seccionRubro = Integer.parseInt(car.getCatsap() + car.getId_rubro());
	                		if(productoCupon == seccionRubro) {
								aplicaCuponDscto = true;
								if(cddto.getDespacho() == 1)
									isCuponDespacho = true;
								break;
							}
	                	}             	
	                } else if (isCuponSeccion) {
	                	Iterator itProds = cuponProds.iterator();
	                	while (itProds.hasNext()) {
	                		int productoCupon = Integer.parseInt(itProds.next().toString());
							if(productoCupon == Integer.parseInt(car.getCatsap())) {
								aplicaCuponDscto = true;
								if(cddto.getDespacho() == 1)
									isCuponDespacho = true;
								break;
							}
	                	}
	                }
                }
                // Fin cdd
                
            }    
            
            if(aplicaCuponDscto || isCuponDespacho) {
            	
            	DecimalFormat formato = new DecimalFormat("###,###");
            	top.setVariable("{check_1}", "none");
	            top.setVariable("{check_2}", "block");
	            top.setVariable("{msg_cupon}", "");
	            
	            if(cddto.getTipo().equals("D"))
	            	top.setVariable("{tipoCupon}", "Se aplica cupón código "+cddto.getCodigo()+" para descuento solo despacho.");
	            else
	            	top.setVariable("{tipoCupon}", ""+cddto.getCodigo()+" con " + formato.format(cddto.getDescuento()) + "% descuento ha sido aplicado");
	            //TODO ingreso codugo cupon
            }else {
            	
            	top.setVariable("{check_1}", "block");
		        top.setVariable("{check_2}", "none");
		        
		        if (session.getAttribute("ses_cupon_descuento_object") != null) 
		        	top.setVariable("{msg_cupon}", "El cupon no esta asociado");
		        else
		        	top.setVariable("{msg_cupon}", "");
                
		        session.setAttribute("ses_cupon_descuento_object", null);
		    }
            
            top.setVariable("{mx_criterio}", criterio); //Tags Analytics - Criterio del Carro
            session.setAttribute("carro", carro2);
            session.setAttribute("totalcompra", total+"");
            
            
            //double descuento_promo = total;
            double descuento_promo_tmas = 0;
            String desc_promo_tmas = "";
            double descuento_promo_webpay = 0;
            String desc_promo_webpay = "";
            int cantProd = 0,cantidadProductosCarro = 0;

            // Obtener datos de la promomocion
            try {
                doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
                recalculoDTO.setCuotas( Integer.parseInt(rb.getString("promociones.jumbomas.cuotas")) );
                recalculoDTO.setF_pago( rb.getString("promociones.jumbomas.formapago") );
                recalculoDTO.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
                recalculoDTO.setGrupos_tcp(l_torec);
//              [20121108avc
                if(Boolean.valueOf(String.valueOf(session.getAttribute("ses_colaborador"))).booleanValue())
                    recalculoDTO.setRutColaborador(new Long(session.getAttribute("ses_cli_rut").toString()));
				//]20121108avc


                List l_prod = new ArrayList();
                for (int i = 0; i < listaCarro.size(); i++) {
                    car = (MiCarroDTO) listaCarro.get(i);
                    if (car.tieneStock()) {
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
                        
// inicio cdd                    
                        pro.setRubro(car.getId_rubro());
// fin cdd             
                        
                        l_prod.add(pro);
                        cantidadProductosCarro = cantidadProductosCarro + (int)pro.getCant_solicitada();
                    }
                }
                
                recalculoDTO.setProductos( l_prod );

                if (l_prod != null && l_prod.size() > 0 ){
                    /////////////////////////////////////////////////////
                    //CALCULO
                	
                	// inicio cdd
                	doRecalculoResultado resultadoTMAS = null;
                	if((!aplicaCuponDscto) && (isCuponCAT)) {
                		cuponProds = null;
                		cddto = null;
                	}
                    resultadoTMAS = biz.doRecalculoPromocionNew( recalculoDTO, cddto, cuponProds);
                    // fin cdd
                    
                    /////////////////////////////////////////////////////
                    List promocionesTMAS = new ArrayList();
                    List l_promo = resultadoTMAS.getPromociones();
                    int cantProdPromo = 0;
                    int cantProductos = 0;

                    
                    for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
                        PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
                        cantProductos = cantProductos + promocion.getCantProdPromo();
                    }
                    for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
                        PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
                        cantProd = cantProd + promocion.getCantProdPromo();
                        this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
                        desc_promo_tmas += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
                        
                        IValueSet fila_promo_tmas = new ValueSet();
                        fila_promo_tmas.setVariable("{cod_promo}", promocion.getCod_promo()+"");
                        fila_promo_tmas.setVariable("{promo_descripcion}", promocion.getDescr());
                        fila_promo_tmas.setVariable("{promo_descuento}", Formatos.formatoPrecioFO(promocion.getDescuento1()));
                        if((promocion.getDescr().equals("Descuento colaborador Cencosud 12%")) && (cantProductos != cantidadProductosCarro)){
                        	cantProdPromo = promocion.getCantProdPromo()+(cantidadProductosCarro-cantProd);
                            fila_promo_tmas.setVariable("{cant_prod_desc}", cantProdPromo+"");
                        }
                        else{
                            fila_promo_tmas.setVariable("{cant_prod_desc}", promocion.getCantProdPromo()+"");

                        }
                        promocionesTMAS.add(fila_promo_tmas);
                    }
                    
                    
                 
                   
                    top.setDynamicValueSets("PROMOCIONES_TMAS",promocionesTMAS);
                    session.setAttribute("promocionesTMAS", promocionesTMAS);

                    descuento_promo_tmas = resultadoTMAS.getDescuento_pedido();
                    if ( total < resultadoTMAS.getDescuento_pedido() ) {
                        descuento_promo_tmas = total;
                    }
                }
            } catch( SystemException e ) {
                this.getLogger().error("Error en libreria de promociones, no presenta promociones: "+e.getMessage());
            }
            
            //Obtener datos de la promomocion
            try {
                doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
                recalculoDTO.setCuotas( Integer.parseInt(rb.getString("promociones.tjacredito.cuotas")) );
                recalculoDTO.setF_pago( rb.getString("promociones.tjacredito.formapago") );
                recalculoDTO.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
                recalculoDTO.setGrupos_tcp(l_torec);
//				[20121108avc
                if(Boolean.valueOf(String.valueOf(session.getAttribute("ses_colaborador"))).booleanValue())
                    recalculoDTO.setRutColaborador(new Long(session.getAttribute("ses_cli_rut").toString()));
				//]20121108avc

                List l_prod = new ArrayList();
                
                for (int i = 0; i < listaCarro.size(); i++) {
                    car = (MiCarroDTO) listaCarro.get(i);
                    if (car.tieneStock()) {
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
// inicio cdd
                        pro.setRubro(car.getId_rubro());
// fin cdd             
                        l_prod.add(pro);
                    }
                }
                
                recalculoDTO.setProductos( l_prod );

                if (l_prod != null && l_prod.size() > 0 ){
                    /////////////////////////////////////////////////////
                    //CALCULO
                	
                	// inicio cdd
                    doRecalculoResultado resultado = null;
                    if ((!aplicaCuponDscto) && (isCuponTBK)) {
                    	cddto = null;
                    	cuponProds = null;
                    }
                    resultado = biz.doRecalculoPromocionNew( recalculoDTO, cddto, cuponProds);
                    // fin cdd
                    
                    /////////////////////////////////////////////////////

                    List promocionesWEBPAY = new ArrayList();
                    List l_promo = resultado.getPromociones();
                    int cantProductosWP = 0;
                    int cantProdWP = 0;
                    int cantProdPromoWP = 0;
                    for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
                        PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
                        cantProductosWP = cantProductosWP + promocion.getCantProdPromo();
                    }
                    
                    for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
                        PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
                        cantProdWP = cantProdWP + promocion.getCantProdPromo();
                        this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
                        desc_promo_webpay += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
                    
                        IValueSet fila_promo_webpay = new ValueSet();
                        fila_promo_webpay.setVariable("{cod_promo}", promocion.getCod_promo()+"");
                        fila_promo_webpay.setVariable("{promo_descripcion}", promocion.getDescr());
                        fila_promo_webpay.setVariable("{promo_descuento}", Formatos.formatoPrecioFO(promocion.getDescuento1()));
                        if((promocion.getDescr().equals("Descuento colaborador Cencosud 12%")) && (cantProductosWP != cantidadProductosCarro)){
                        	cantProdPromoWP = promocion.getCantProdPromo()+(cantidadProductosCarro-cantProd);
                        	fila_promo_webpay.setVariable("{cant_prod_desc}", cantProdPromoWP+"");
                        }
                        else{
                        	fila_promo_webpay.setVariable("{cant_prod_desc}", promocion.getCantProdPromo()+"");

                        }
                        promocionesWEBPAY.add(fila_promo_webpay);
                    }
                    top.setDynamicValueSets("PROMOCIONES_WEBPAY",promocionesWEBPAY);
                    session.setAttribute("promocionesWEBPAY", promocionesWEBPAY);

                    descuento_promo_webpay = resultado.getDescuento_pedido();
                    if ( total < resultado.getDescuento_pedido() ) {
                        descuento_promo_webpay = total;
                    }
                }
            } catch( SystemException e ) {
                this.getLogger().error("Error en libreria de promociones, no presenta promociones: "+e.getMessage());
            }
            
            //int costo_despacho = Integer.parseInt(session.getAttribute("jprecio") + "");
            
            long id_zona = Long.parseLong(session.getAttribute("ses_zona_id").toString());
            int costo_despacho_tmas = Integer.parseInt(session.getAttribute("jprecio") + "");
            int costo_despacho_webpay = Integer.parseInt(session.getAttribute("jprecio") + "");
            
            // inicio cdd
            if(isCuponDespacho){
            	if (isCuponCAT)
            		costo_despacho_tmas = 1;
            	if (isCuponTBK)
            		costo_despacho_webpay = 1;
            }
            // fin cdd
            
            /*****   C O S T O   D E   D E S P A C H O   ****/
            ZonaDTO zona = biz.getZonaDespachoById(id_zona);
            //double costo_despacho_old = ped2.getCosto_despacho();
            //01: siempre, 10: primera compra, 11: ambos
            
            if (session.getAttribute("descuento_despacho") != null && session.getAttribute("descuento_despacho").toString().equals("true")) {
            	if(( (zona.getEstado_descuento_cat() & 1) == 1 && total >= zona.getMonto_descuento_cat() ) || 
                        ( (zona.getEstado_descuento_cat() & 2) == 2 && total >= zona.getMonto_descuento_pc_cat() && biz.esPrimeraCompra(cli_id)) ){
                       costo_despacho_tmas = 1;
                }
            	
            	if(( (zona.getEstado_descuento_tbk() & 1) == 1 && total >= zona.getMonto_descuento_tbk() ) || 
                        ( (zona.getEstado_descuento_tbk() & 2) == 2 && total >= zona.getMonto_descuento_pc_tbk() && biz.esPrimeraCompra(cli_id))){
                        costo_despacho_webpay = 1;
                }
            }
            
           /************************************************/
            
            if (listaCarro != null && listaCarro.size()>0) {
                List viewTotalizadorTMAS = new ArrayList(); 
                IValueSet filatotal = new ValueSet();
                
                filatotal.setVariable("{total_desc}", Formatos.formatoPrecioFO(total-descuento_promo_tmas));
                filatotal.setVariable("{total_desc_sf}", Formatos.formatoPrecioFO(descuento_promo_tmas));
                filatotal.setVariable("{promo_desc}", desc_promo_tmas );
                filatotal.setVariable("{subtotal}", Formatos.formatoPrecioFO(total));
                filatotal.setVariable("{total}", Formatos.formatoPrecioFO(total + costo_despacho_tmas - descuento_promo_tmas) +"" );
                filatotal.setVariable("{costo_despacho}", Formatos.formatoPrecioFO(costo_despacho_tmas));
                viewTotalizadorTMAS.add(filatotal);
                top.setDynamicValueSets("TOTALIZADOR_TMAS",viewTotalizadorTMAS);
            }  
            
            if (listaCarro != null && listaCarro.size()>0) {
                List viewTotalizadorTMAS = new ArrayList(); 
                IValueSet filatotal = new ValueSet();
                filatotal.setVariable("{total_desc}", Formatos.formatoPrecioFO(total-descuento_promo_webpay));
                filatotal.setVariable("{total_desc_sf}", Formatos.formatoPrecioFO(descuento_promo_webpay));
                filatotal.setVariable("{promo_desc}", desc_promo_webpay );
                filatotal.setVariable("{subtotal}", Formatos.formatoPrecioFO(total));
                filatotal.setVariable("{total}", Formatos.formatoPrecioFO(total + costo_despacho_webpay - descuento_promo_webpay) +"" );
                filatotal.setVariable("{costo_despacho}", Formatos.formatoPrecioFO(costo_despacho_webpay));
                viewTotalizadorTMAS.add(filatotal);
                top.setDynamicValueSets("TOTALIZADOR_WEBPAY",viewTotalizadorTMAS);
            }  
            
            emailError+="El carro es:<br>"+carro2+"<br>Total: "+total+"<br>";
            emailError+="Datos Cliente:<br>";

            if(session.getAttribute("ses_cli_rut")!="" && session.getAttribute("ses_cli_rut")!= null){
                emailError+="Nombre: "+session.getAttribute("ses_cli_nombre")+"<br>";
                emailError+="Rut: "+session.getAttribute("ses_cli_rut")+"<br>";
                emailError+="ID Cliente: "+session.getAttribute("ses_cli_id")+"<br>";
            }else{
                emailError+="Cliente Invitado<br>";
            }
            
            emailError+="Email: "+session.getAttribute("ses_cli_email")+"<br>";
            emailError+="Telefono: "+session.getAttribute("ses_telefono")+"<br>";
            emailError+="Comuna: "+session.getAttribute("ses_comuna_cliente")+"<br>";
            
            session.setAttribute("emailError", emailError+"");
            
            
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
          
          
          top.setVariable("{ta_mx_content}", "Pago");
         
          
            
            String result = tem.toString(top);
			out.print(result);

		} catch (Exception e) {
            //recuperamos carro desde sesion
            BizDelegate biz = new BizDelegate();
            long cli_id = 0L;
            String idSession = null;
            cli_id = Long.parseLong((String)session.getAttribute("ses_cli_id"));
            List listaCarro =null;
            if (session.getAttribute("ses_invitado_id") != null)
                idSession = session.getAttribute("ses_invitado_id").toString();
            try {
                listaCarro = biz.carroComprasPorCategorias(cli_id, session.getAttribute("ses_loc_id").toString(), idSession);
            } catch (SystemException e2) {
                this.getLogger().error("Fallo llenado de carro (Error: AjaxPago)", e2);
            }
            
            MiCarroDTO car;
            long precio_total = 0;
            String carro2 = "";
            for (int i = 0; i < listaCarro.size(); i++) {
                car = (MiCarroDTO) listaCarro.get(i);
                //Información del producto
                if (car.tieneStock()) {
                    precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
                    total += precio_total;                    
                    carro2+="[cant. "+car.getCantidad()+"] "+car.getPro_id()+" / "+car.getNom_marca()+" / "+car.getTipo_producto()+" / "+car.getNombre()+" / "+car.getDescripcion()+"/Precio unit-total: "+car.getPrecio()+"-"+precio_total+"<br>";
                    
                }
            }
            
            emailError+="El carro es:<br>"+carro2+"<br>Total: "+total+"<br>";
            emailError+="Datos Cliente:<br>";

            if(session.getAttribute("ses_cli_rut")!="" && session.getAttribute("ses_cli_rut")!= null && session.getAttribute("ses_cli_rut")!="123123"){
                emailError+="Nombre: "+session.getAttribute("ses_cli_nombre")+"<br>";
                emailError+="Rut: "+session.getAttribute("ses_cli_rut")+"<br>";
                emailError+="ID Cliente: "+session.getAttribute("ses_cli_id")+"<br>";
            }else{
                emailError+="Cliente Invitado<br>";
            }
            
            
            emailError+="Email: "+session.getAttribute("ses_cli_email")+"<br>";
            emailError+="Telefono: "+session.getAttribute("ses_telefono")+"<br>";
            emailError+="Comuna: "+session.getAttribute("ses_comuna_cliente")+"<br>";           

            session.setAttribute("carro", carro2);
            session.setAttribute("totalcompra", total+"");  
            session.setAttribute("emailError", emailError+"");
            
            SendMail mail = new SendMail(errormailSmtp, "contacto@jumboweb.cl", emailError, null);
            
            
            try {
                mail.enviar(errormailTo, errormailCc, "Error: AjaxPago. Total: "+total);
            } catch (Exception e1) {
                this.getLogger().error("Fallo envio de email (Error: AjaxPago)", e1);               
            }
            
            this.getLogger().error(e);
            throw new CommandException( e );
		}
	}
}