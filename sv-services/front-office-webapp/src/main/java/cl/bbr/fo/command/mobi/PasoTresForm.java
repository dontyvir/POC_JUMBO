package cl.bbr.fo.command.mobi;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import cl.bbr.fo.util.Cod_error;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.PoliticaSustitucionDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;

/**
 * Despliega página del paso 3 para mobile
 * 
 * @author imoyano
 * 
 */
public class PasoTresForm extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		try {			
			// Carga properties
            ResourceBundle rb = ResourceBundle.getBundle("fo");

            // Se almacena tracking en este sector
            //Tracking_web.saveTracking("Checkout", arg0);

            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();

            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();

            // Recupera pagina desde web.xml
            String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
            this.getLogger().debug( "Template:"+pag_form );
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();
            
            IValueSet top = new ValueSet();
            
            //Se recupera si el cliente es Empleado Paris (1:Empleado, 0:No Empleado)
            if (session.getAttribute("ses_empleado_paris") != null){
                if (((String)session.getAttribute("ses_empleado_paris")).equals("SI"))
                    top.setVariable("{empleado}", "1");
                else 
                    top.setVariable("{empleado}", "0"); 
            } else {
                top.setVariable("{empleado}", "0");
            }
            
            //Se setea el rut del cliente en la página para posibles validaciones con tarjeta paris
            top.setVariable("{rut_cli}", (String)session.getAttribute("ses_cli_rut"));
            top.setVariable("{dv_cli}", (String)session.getAttribute("ses_cli_dv"));
            
            Calendar ahora = new GregorianCalendar();
            String StrFecha1 = new SimpleDateFormat( Formatos.DATE_TIME).format(ahora.getTime());
            top.setVariable("{fechaactual}", StrFecha1 );
            
            //SOLO PARA EL CASO QUE EXISTA EL ID DEL EJECUTIVO
            IValueSet lista_boton = new ValueSet();
            lista_boton.setVariable("{contador_form}", ""); 
            
            List list_boton = new ArrayList();
            list_boton.add(lista_boton);
            
            if(session.getAttribute("ses_eje_id") != null ){
                top.setDynamicValueSets("SEL_MPA", list_boton);
                top.setDynamicValueSets("DIV_MPA", list_boton);
            }
            
            //NUMERO DE TARJETA JUMBO
            if (arg0.getParameter("j_numtja1") == null) top.setVariable("{j_numtja1}", "" );
            if (arg0.getParameter("j_numtja2") == null) top.setVariable("{j_numtja2}", "" );
            if (arg0.getParameter("j_numtja3") == null) top.setVariable("{j_numtja3}", "" );
            if (arg0.getParameter("j_numtja4") == null) top.setVariable("{j_numtja4}", "" );

            //NUMERO DE CUOTAS
            if (arg0.getParameter("j_cuotas") == null) {
                top.setVariable("{selcuota1}", "" );
                top.setVariable("{selcuota2}", "" );
                top.setVariable("{selcuota3}", "" );
                top.setVariable("{selcuota4}", "" );
            }
            
            //DATOS DE FACTURACION
            if (arg0.getParameter("chk_tipo_doc") == null) top.setVariable("{chk_tipo_doc}", "" ); 
            
            
            if (arg0.getParameter("fac_rut") == null) top.setVariable("{fac_rut}", "" );
            if (arg0.getParameter("fac_dv") == null) top.setVariable("{fac_dv}", "" );
            if (arg0.getParameter("fac_razon") == null) top.setVariable("{fac_razon}", "" );
            if (arg0.getParameter("fac_giro") == null) top.setVariable("{fac_giro}", "" );
            if (arg0.getParameter("fac_direccion") == null) top.setVariable("{fac_direccion}", "" );
            if (arg0.getParameter("fac_fono") == null) top.setVariable("{fac_fono}", "" );
            if (arg0.getParameter("fac_comuna") == null) top.setVariable("{fac_comuna}", "" );
            if (arg0.getParameter("fac_ciudad") == null) top.setVariable("{fac_ciudad}", "" );
            
            //TARJETA BANCARIA
            if (arg0.getParameter("nom_tban") == null){
                top.setVariable("{chktbank1}", "" );            
                top.setVariable("{chktbank2}", "" );
                top.setVariable("{chktbank3}", "" );
                top.setVariable("{chktbank4}", "" );
            }
            if (arg0.getParameter("t_numero") == null) top.setVariable("{t_numero}", "" );
            if (arg0.getParameter("t_auxmes") == null) top.setVariable("{t_auxmes}", "" );
            if (arg0.getParameter("t_auxano") == null) top.setVariable("{t_auxano}", "" );
            if (arg0.getParameter("t_auxcuotas") == null) top.setVariable("{t_auxcuotas}", "" );
            if (arg0.getParameter("t_banco") == null) top.setVariable("{t_banco}", "" );
            
            //TARJETA PARIS EMPLEADO
            if (arg0.getParameter("pe_clave") == null) top.setVariable("{pe_clave}", "" );
            
            //TARJETA PARIS
            if (arg0.getParameter("p_numtja") == null) top.setVariable("{p_numtja}", "" );
            else top.setVariable("{p_numtja}", arg0.getParameter("j_numtja1")); 
            
            //ADICIONAL
            if (arg0.getParameter("rut_titular") == null) top.setVariable("{rut_titular}", "" );
            if (arg0.getParameter("dv_titular") == null) top.setVariable("{dv_titular}", "" );
            if (arg0.getParameter("nom_titular") == null) top.setVariable("{nom_titular}", "" );
            if (arg0.getParameter("pat_titular") == null) top.setVariable("{pat_titular}", "" );
            if (arg0.getParameter("mat_titular") == null) top.setVariable("{mat_titular}", "" );
            if (arg0.getParameter("dir_titular") == null) top.setVariable("{dir_titular}", "" );
            if (arg0.getParameter("num_titular") == null) top.setVariable("{num_titular}", "" );
            
            //FORMA DE PAGO
            if (arg0.getParameter("forma_pago") == null) top.setVariable("{select_fpago}", "" );
            
            // Revisión de errores
            if( session.getAttribute("cod_error") != null ) {
                top.setVariable("{error}", "1");
                this.getLogger().debug("Se ha detectado un error: " + session.getAttribute("cod_error") );
                
                if( session.getAttribute("cod_error").toString().compareTo( Cod_error.CHECKOUT_PICK ) == 0 ) {
                    top.setVariable("{mensaje_error}", rb.getString("checkout.mensaje.errorpick"));
                }else if( session.getAttribute("cod_error").toString().compareTo( Cod_error.CHECKOUT_SIN_PROD ) == 0 ) {
                    top.setVariable("{mensaje_error}", rb.getString("checkout.mensaje.errorproductos"));
                    this.getLogger().debug("Se ha detectado un error en orderprocess: " + rb.getString("checkout.mensaje.errorproductos") );
                }else if( session.getAttribute("cod_error").toString().compareTo( Cod_error.CHECKOUT_SIN_CAPACIDAD ) == 0 ) {
                    top.setVariable("{mensaje_error}", rb.getString("checkout.mensaje.sincapacidad"));
                    this.getLogger().debug("Se ha detectado un error en orderprocess: " + rb.getString("checkout.mensaje.sincapacidad") );
                }else
                    top.setVariable("{mensaje_error}", rb.getString("checkout.mensaje.error"));
                //session.removeAttribute("cod_error");
                
                //NUMERO DE TARJETA
                if (arg0.getParameter("j_numtja1") != null) top.setVariable("{j_numtja1}", arg0.getParameter("j_numtja1")+"" );
                if (arg0.getParameter("j_numtja2") != null) top.setVariable("{j_numtja2}", arg0.getParameter("j_numtja2")+"" );
                if (arg0.getParameter("j_numtja3") != null) top.setVariable("{j_numtja3}", arg0.getParameter("j_numtja3")+"" );
                if (arg0.getParameter("j_numtja4") != null) top.setVariable("{j_numtja4}", arg0.getParameter("j_numtja4")+"" );

                    
                //NUMERO DE CUOTAS
                if( arg0.getParameter("j_cuotas") != null ) {
                    if (arg0.getParameter("j_cuotas").compareTo("1") == 0) top.setVariable("{selcuota1}", "selected=\"selected\"" );
                    if (arg0.getParameter("j_cuotas").compareTo("2") == 0) top.setVariable("{selcuota2}", "selected=\"selected\"" );
                    if (arg0.getParameter("j_cuotas").compareTo("3") == 0) top.setVariable("{selcuota3}", "selected=\"selected\"" );
                    if (arg0.getParameter("j_cuotas").compareTo("4") == 0) top.setVariable("{selcuota4}", "selected=\"selected\"" );
                }
                
                
                //DATOS DE FACTURACION
                if (arg0.getParameter("fac_rut") != null) top.setVariable("{fac_rut}", arg0.getParameter("fac_rut"));
                if (arg0.getParameter("fac_dv") != null) top.setVariable("{fac_dv}",  arg0.getParameter("fac_dv"));
                if (arg0.getParameter("fac_razon") != null) top.setVariable("{fac_razon}", arg0.getParameter("fac_razon"));
                if (arg0.getParameter("fac_giro") != null) top.setVariable("{fac_giro}", arg0.getParameter("fac_giro"));
                if (arg0.getParameter("fac_direccion") != null) top.setVariable("{fac_direccion}", arg0.getParameter("fac_direccion"));
                if (arg0.getParameter("fac_fono") != null) top.setVariable("{fac_fono}", arg0.getParameter("fac_fono"));
                if (arg0.getParameter("fac_ciudad") != null) top.setVariable("{fac_ciudad}", arg0.getParameter("fac_ciudad"));
                if (arg0.getParameter("fac_comuna") != null) top.setVariable("{fac_comuna}", arg0.getParameter("fac_comuna"));
                
                //TARJETA BANCARIA
                if (arg0.getParameter("nom_tban") != null) {
                    if (arg0.getParameter("nom_tban").compareTo("Visa") == 0)
                        top.setVariable("{chktbank1}", "checked" );
                    else if (arg0.getParameter("nom_tban").compareTo("Dinners") == 0)
                        top.setVariable("{chktbank2}", "checked" );
                    else if (arg0.getParameter("nom_tban").compareTo("MasterCard") == 0)
                        top.setVariable("{chktbank3}", "checked" );
                    else if (arg0.getParameter("nom_tban").compareTo("American Express") == 0)
                        top.setVariable("{chktbank4}", "checked" ); 
                }
                
                /*if (arg0.getParameter("nom_tban") != null && arg0.getParameter("nom_tban").compareTo("Visa") == 0) top.setVariable("{chktbank1}", "checked" );            
                if (arg0.getParameter("nom_tban") != null && arg0.getParameter("nom_tban").compareTo("Dinners") == 0) top.setVariable("{chktbank2}", "checked" );
                if (arg0.getParameter("nom_tban") != null && arg0.getParameter("nom_tban").compareTo("MasterCard") == 0) top.setVariable("{chktbank3}", "checked" );
                if (arg0.getParameter("nom_tban") != null && arg0.getParameter("nom_tban").compareTo("American Express") == 0) top.setVariable("{chktbank4}", "checked" );*/
                
                if (arg0.getParameter("t_numero") != null) top.setVariable("{t_numero}", arg0.getParameter("t_numero"));
                if (arg0.getParameter("t_auxmes") != null) top.setVariable("{t_auxmes}", arg0.getParameter("t_mes"));
                if (arg0.getParameter("t_auxano") != null) top.setVariable("{t_auxano}", arg0.getParameter("t_ano"));
                if (arg0.getParameter("t_auxcuotas") != null) top.setVariable("{t_auxcuotas}", arg0.getParameter("t_cuotas"));
                if (arg0.getParameter("t_banco") != null) top.setVariable("{t_banco}", arg0.getParameter("t_banco"));       

                //FORMA DE PAGO
                if (arg0.getParameter("forma_pago") != null) {
                    if (arg0.getParameter("forma_pago").compareTo("4") == 0) {
                        top.setVariable("{select_fpago}", "selected=\"selected\"" );
                        top.setVariable("{formaDePagoLayer}", "formaDePagoLayer(4);" );
                    }else{
                        top.setVariable("{formaDePagoLayer}", "" );
                    }
                }   
                
                if (arg0.getParameter("tipo_documento") != null) {
                    if (arg0.getParameter("tipo_documento").compareTo("B") == 0) {
                        top.setVariable("{datosFactura}", "datosFactura('hidden')" );
                    }
                } else
                    top.setVariable("{chk_tipo_doc}", "" ); 
                
                if (arg0.getParameter("tipo_documento") != null) {
                    if (arg0.getParameter("tipo_documento").compareTo("F") == 0) {
                        top.setVariable("{chk_tipo_doc}", "checked=\"checked\"" );
                        top.setVariable("{datosFactura}", "datosFactura('visible')" );
                    } else
                        top.setVariable("{chk_tipo_doc}", "" );
                }   
                
                if (arg0.getParameter("sin_gente_txt") != null)
                    top.setVariable("{sin_gente_txt}", arg0.getParameter("sin_gente_txt") );
                
                if (arg0.getParameter("sin_gente_op") != null) {
                    if (arg0.getParameter("sin_gente_op").compareTo("0") == 0) top.setVariable("{chknadie1}", "checked=\"checked\"");
                    if (arg0.getParameter("sin_gente_op").compareTo("1") == 0) top.setVariable("{chknadie2}", "checked=\"checked\"");
                }   
                
            }
            else {
                top.setVariable("{error}", "0");
            }
            
            if ( arg0.getParameter("err") != null ) {
                if ( session.getAttribute("ses_id_pedido") != null ) {
                    top.setVariable("{error}", "1");
                    top.setVariable("{mensaje_error}", "Transacción rechazada.\\nSu transacción no ha podido ser procesada, favor vuelva a intentarlo.\\nCorrobore números y fechas solicitadas, si el problema persiste comunique a su banco emisor o llámanos al 600 400 3000.");
                }
            }

            top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
            top.setVariable("{email_cliente}", session.getAttribute("ses_cli_email").toString());
            
            // Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();
            
            
            // Recupera el cliente desde la sesión
            long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
            long direcc_id = Long.parseLong(session.getAttribute("ses_dir_id").toString());
        
            ClienteDTO cli = biz.clienteGetById(cliente_id);
            top.setVariable("{mail_nombre}", nombreDeMail(cli.getEmail()));
            top.setVariable("{mail_dominio}", dominioDeMail(cli.getEmail()));
            
            top.setVariable("{num_fono}", cli.getFon_num_2());
            List codCelulares = new ArrayList();
            for( int i = 6; i <= 9 ; i++ ) {                
                IValueSet f = new ValueSet();
                try {
                    if ( cli.getFon_cod_2() != null && !("").equalsIgnoreCase(cli.getFon_cod_2())) {
                        if (i == Integer.parseInt( cli.getFon_cod_2())) {
                            f.setVariable("{sel_cod_cel}","selected");
                        } else {
                            f.setVariable("{sel_cod_cel}","");
                        }
                    }
                } catch (Exception e) {
                    f.setVariable("{sel_cod_cel}","");
                }
                f.setVariable("{cod_cel}",""+i);
                codCelulares.add(f);                
            }
            top.setDynamicValueSets("SEL_COD_CEL", codCelulares);
            
            
            // Recuperar direcciones de despacho del cliente
            List lista = biz.clienteAllDireccionesConCobertura(cliente_id);
            this.getLogger().debug("Direcciones despachos: " + lista.size() );
            String direccion = "";
            DireccionesDTO dir = null;
            for (int i = 0; i < lista.size(); i++) {
                dir = (DireccionesDTO) lista.get(i);
                if (dir.getId() == direcc_id){
                    top.setVariable("{txtTipoCalle}", dir.getTipo_calle()+"");
                    top.setVariable("{txtDirCalle}", dir.getCalle());
                    top.setVariable("{txtDirNumero}", dir.getNumero());
                    top.setVariable("{txtDirDepto}", dir.getDepto());
                    direccion = dir.getCalle()+" "+dir.getNumero()+" "+dir.getDepto()+", "+dir.getCom_nombre()+", "+dir.getReg_nombre();
                    break;
                }
            }
            // Validar si tiene dirección de despacho válida
            if( direccion.compareTo("") == 0 ) {
                this.getLogger().error( "No existe dirección de despacho id=" + direcc_id );
                top.setVariable("{error}", "2");
                top.setVariable("{mensaje_error}", rb.getString("checkout.mensaje.sindirdespacho"));
            }
            top.setVariable("{direccion_despacho}", direccion);
            this.getLogger().debug( "Dir despacho ID: " + direcc_id + " - " + direccion );
            
            PedidoDTO pedido = biz.getUltimoIdPedidoCliente( cliente_id );
            
            if( pedido != null ) {
                if (pedido.getSin_gente_op() == 0){
                    top.setVariable("{chknadie1}", "checked=\"checked\"");
                    top.setVariable("{chknadie2}", "");
                }else if(pedido.getSin_gente_op() == 1){
                    top.setVariable("{chknadie2}", "checked=\"checked\"");
                    top.setVariable("{chknadie1}", "");
                }
                top.setVariable("{sin_gente_txt}", pedido.getSin_gente_txt()+"");
            }else{
                top.setVariable("{chknadie1}", "checked=\"checked\"");
                top.setVariable("{sin_gente_txt}", "");
            }
            
            // Lista de politicas de sustitución
            List arr_politicas = new ArrayList();           
            List pol_sustitucion = biz.PoliticaSustitucion();
            this.getLogger().debug( "Politicas sustitucion:"+pol_sustitucion.size() );
            PoliticaSustitucionDTO data = null;
            IValueSet fila = null;
            for( int i = 0; i < pol_sustitucion.size() ; i++ ) {
                data = (PoliticaSustitucionDTO) pol_sustitucion.get(i);
                fila = new ValueSet();
                
                fila.setVariable("{sus_id}",  data.getId()+""); 
                fila.setVariable("{sus_nombre}", data.getNombre()+"");
                fila.setVariable("{sus_descripcion}", data.getDescripcion()+"");
                fila.setVariable("{sus_estado}", data.getEstado()+"");
                fila.setVariable("{sus_seleccion}", data.getSeleccion()+"");
                
                if ( pedido!= null && pedido.getPol_id() != 0 && data.getId() == pedido.getPol_id() ){
                    fila.setVariable("{checked}", "checked=\"checked\"");
                }else{
                    if ( data.getSeleccion() != null && data.getSeleccion().toString().compareTo("S") == 0 ) {
                        fila.setVariable("{checked}", "");
                    }
                    else{
                        fila.setVariable("{checked}", "");
                    }
                }
                
                if( session.getAttribute("cod_error") != null ) {
                    if (arg0.getParameter("pol_sustitucion") != null){
                        if (data.getId() == Long.parseLong(arg0.getParameter("pol_sustitucion").split("--")[0].toString())){
                            fila.setVariable("{checked}", "checked=\"checked\"");
                        }else{
                            fila.setVariable("{checked}", "");
                        }
                    } else {
                        fila.setVariable("{checked}", "");
                    }
                }
                
                
                
                arr_politicas.add(fila);            
            }
            top.setDynamicValueSets("POL_SUSTITUCION", arr_politicas); 
            
            top.setVariable("{selcuota1}", "selected=\"selected\"" );

            long idZona = 0;
            if (session.getAttribute("ses_zona_id") != null) {
                idZona = Long.parseLong(session.getAttribute("ses_zona_id").toString());
            }
            top.setVariable("{zona_despacho}", "" + idZona );
            
            
            
            top.setVariable("{webpay_url}", rb.getString("webpay.url") );
            top.setVariable("{boton_url}", rb.getString("boton.url") );            
            if ( session.getAttribute("ses_eje_id") != null ) {
                //Si es fono compra, el kit webpay es distinto
                top.setVariable("{webpay_url_kit}", rb.getString("webpay.kit.fonocompra") );
                top.setVariable("{boton_url_kit}", rb.getString("boton.kit.ruta.fonocompra") );
                top.setVariable("{numero_empresa}", rb.getString("boton.numero.fono") );
            } else {
                top.setVariable("{webpay_url_kit}", rb.getString("webpay.kit.cliente") );
                top.setVariable("{boton_url_kit}", rb.getString("boton.kit.ruta.cliente") );
                top.setVariable("{numero_empresa}", rb.getString("boton.numero.cliente") );
            }
            
            
            
            String result = tem.toString(top);
            out.print(result);
		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
		}
	}

    /**
     * @param email
     * @return
     */
    private String dominioDeMail(String email) {
        String dom = "";
        if (email != null) {
            if (email.split("@").length == 2) {
                dom = email.split("@")[1];
            }            
        }
        return dom;
    }

    /**
     * @param email
     * @return
     */
    private String nombreDeMail(String email) {
        String nom = "";
        if (email != null) {
            if (email.split("@").length == 2) {
                nom = email.split("@")[0];
            }            
        }
        return nom;
    }
}