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
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;

/**
 * Página que entrega los totales para cada medio de pago
 * @author carriagada-IT4B
 * 
 */
public class AjaxPago extends Command {
    
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
		    // Carga properties
			
			// Recupera la sesión del usuario
            arg1.setHeader("Cache-Control", "no-cache");
            arg0.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/html; charset=utf-8");
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();
            
			IValueSet top = new ValueSet();		

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
            
            if (listaCarro != null && listaCarro.size() > 0) {
                listaCarro = biz.cargarPromocionesMiCarro(listaCarro, l_torec, Integer.parseInt(session.getAttribute("ses_loc_id").toString()));
            }
            
            //armamos carro aca tb
            for (int i = 0; i < listaCarro.size(); i++) {
                car = (MiCarroDTO) listaCarro.get(i);
                //Información del producto
                if (car.tieneStock()) {
                    precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
                    total += precio_total;
                    
                    carro2+="[cant. "+car.getCantidad()+"]"+car.getPro_id()+"/"+car.getNom_marca()+"/"+car.getTipo_producto()+"/"+car.getNombre()+"/"+car.getDescripcion()+"/Precio unit-total: "+car.getPrecio()+"-"+precio_total+"<br>";
                    
                }
            }    
            
            session.setAttribute("carro", carro2);
            session.setAttribute("totalcompra", total+"");
            
            
            //double descuento_promo = total;
            double descuento_promo_tmas = 0;
            String desc_promo_tmas = "";
            double descuento_promo_webpay = 0;
            String desc_promo_webpay = "";
            // Obtener datos de la promomocion
            try {
                doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
                recalculoDTO.setCuotas( Integer.parseInt(rb.getString("promociones.jumbomas.cuotas")) );
                recalculoDTO.setF_pago( rb.getString("promociones.jumbomas.formapago") );
                recalculoDTO.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
                recalculoDTO.setGrupos_tcp(l_torec);
				//[20121108avc
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
                        l_prod.add(pro);
                    }
                }
                
                recalculoDTO.setProductos( l_prod );

                if (l_prod != null && l_prod.size() > 0 ){
                    /////////////////////////////////////////////////////
                    //CALCULO
                    doRecalculoResultado resultadoTMAS = biz.doRecalculoPromocion( recalculoDTO );
                    /////////////////////////////////////////////////////
                    List promocionesTMAS = new ArrayList();
                    List l_promo = resultadoTMAS.getPromociones();
                    for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
                        PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
                        this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
                        desc_promo_tmas += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
                        
                        IValueSet fila_promo_tmas = new ValueSet();
                        fila_promo_tmas.setVariable("{promo_descripcion}", promocion.getDescr());
                        fila_promo_tmas.setVariable("{promo_descuento}", Formatos.formatoPrecioFO(promocion.getDescuento1()));
                        promocionesTMAS.add(fila_promo_tmas);
                    }
                    top.setDynamicValueSets("PROMOCIONES_TMAS",promocionesTMAS);
                    
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
                        l_prod.add(pro);
                    }
                }
                
                recalculoDTO.setProductos( l_prod );

                if (l_prod != null && l_prod.size() > 0 ){
                    /////////////////////////////////////////////////////
                    //CALCULO
                    doRecalculoResultado resultado = biz.doRecalculoPromocion( recalculoDTO );
                    /////////////////////////////////////////////////////

                    List promocionesWEBPAY = new ArrayList();
                    List l_promo = resultado.getPromociones();
                    for( int i = 0; l_promo != null && i < l_promo.size(); i++ ) {
                        PromocionDTO promocion = (PromocionDTO)l_promo.get(i);
                        this.getLogger().debug("Promocion: " + promocion.getCod_promo() +"-"+ promocion.getDescuento1() +"-"+ promocion.getDescr());
                        desc_promo_webpay += promocion.getDescr()+"--"+Formatos.formatoPrecioFO(promocion.getDescuento1())+"#";
                        
                        IValueSet fila_promo_webpay = new ValueSet();
                        fila_promo_webpay.setVariable("{promo_descripcion}", promocion.getDescr());
                        fila_promo_webpay.setVariable("{promo_descuento}", Formatos.formatoPrecioFO(promocion.getDescuento1()));
                        promocionesWEBPAY.add(fila_promo_webpay);
                    }
                    top.setDynamicValueSets("PROMOCIONES_WEBPAY",promocionesWEBPAY);
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
            
            /*****   C O S T O   D E   D E S P A C H O   ****/
            ZonaDTO zona = biz.getZonaDespachoById(id_zona);
            //double costo_despacho_old = ped2.getCosto_despacho();
            //01: siempre, 10: primera compra, 11: ambos
            if(( (zona.getEstado_descuento_cat() & 1) == 1 && total >= zona.getMonto_descuento_cat() ) || 
                 ( (zona.getEstado_descuento_cat() & 2) == 2 && total >= zona.getMonto_descuento_pc_cat() && biz.esPrimeraCompra(cli_id)) ){
                costo_despacho_tmas = 1;
            }

            if(( (zona.getEstado_descuento_tbk() & 1) == 1 && total >= zona.getMonto_descuento_tbk() ) || 
                   ( (zona.getEstado_descuento_tbk() & 2) == 2 && total >= zona.getMonto_descuento_pc_tbk() && biz.esPrimeraCompra(cli_id))){
                   costo_despacho_webpay = 1;
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
            
            List viewLogin = new ArrayList(); 
            IValueSet fila = new ValueSet();
            fila.setVariable("1","1");
            viewLogin.add(fila);
            if ( "1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
                top.setDynamicValueSets("MOSTRAR_NO_LOGUEADO", viewLogin); 
            } else {
                top.setDynamicValueSets("MOSTRAR_LOGUEADO", viewLogin); 
            }
            String result = tem.toString(top);
            
            
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
            
			out.print(result);
		} catch (Exception e){
                        
			this.getLogger().error("Fallo (Error: AjaxPago)", e);

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