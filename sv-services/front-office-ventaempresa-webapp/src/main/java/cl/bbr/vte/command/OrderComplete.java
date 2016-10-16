package cl.bbr.vte.command;

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
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.common.dto.CarroCompraProductosDTO;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.CompradoresDTO;

/**
 * Ingresa el pedido (cambia el pre-ingresado) en el sistema
 *  
 * @author imoyano
 *  
 */
public class OrderComplete extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
    
        System.out.println("********** OrderComplete (VE) **************");
        HttpSession session = arg0.getSession();        
        long idPedido = Long.parseLong(session.getAttribute("ses_id_pedido").toString());
        long idComprador = Long.parseLong(session.getAttribute("ses_com_id").toString());
        String disOk = getServletConfig().getInitParameter("dis_ok");
        
        System.out.println("idComprador:"+idComprador);
        
        BizDelegate biz = new BizDelegate();
        PedidoDTO pedido = biz.getPedidoById(idPedido);
        
        //define Objeto para contener nueva info desde BotonPago
        BotonPagoDTO bp = new BotonPagoDTO(); 
        
        if ( pedido.getMedio_pago().equalsIgnoreCase(Constantes.MEDIO_PAGO_TBK) ) {
            if ( pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_VALIDADO || pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION ) {
                //Caso de excepcion, por alguna razon webpay llama aveces mas de una vez la pagina de exito, en esos casos enviamos a la pagina de resumen
                System.out.println("Caso de excepcion, por alguna razon webpay llama aveces mas de una vez la pagina de exito, en esos casos enviamos a la pagina de resumen");
                arg1.sendRedirect(disOk);
                return;
            }
            // validar el TBK_RESPUESTA = 0 si es webpay antes de ingresar el pedido al sistema solo por seguridad
            WebpayDTO wp = biz.webpayGetPedido(idPedido);
            if ( wp.getTBK_RESPUESTA() != 0 || pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO ) {
                //Error no controlado, cuando llaman a cerrar una OP que no tenga el OK de transbank o que no este preingresado
                System.out.println("Error no controlado, cuando llaman a cerrar una OP que no tenga el OK de transbank o que no este preingresado");
                throw new Exception();
            }
        } else if ( pedido.getMedio_pago().equalsIgnoreCase(Constantes.MEDIO_PAGO_CAT) ) {
            if ( pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO ) {
                //Error no controlado, cuando llaman a cerrar una OP que no tenga el OK de transbank o que no este preingresado
                throw new Exception();
            }
            
            //se recibe segunda parte de parte de parametros desde BotonPAgoTMAS  
            // CTA: 2010.12.06: Por cambio de parametros
            // si es 010 esta OK si no debo enviarlo al CheckOut
            String tipoAutorizacion = arg0.getParameter("tipoAutorizacion");
            
            if (!"A".equals(tipoAutorizacion)){
                logger.debug("BtnPagoTMAS_NOTIFICACION: Pago rechazado (tipoAutorizacion="+tipoAutorizacion+"), se reenvía a CheckOut" );
                arg1.sendRedirect("ViewDetallePedidoPorPagar");
                return;
            }
            
            //se recibe segunda parte de parte de parametros desde BotonPAgoTMAS            
            String numeroTarjeta = arg0.getParameter("numeroTarjeta");
            String rutCliente = arg0.getParameter("rutCliente");
            String usoClave = arg0.getParameter("usoClave");
            logger.debug("BtnPagoTMAS_NOTIFICACION: se recibe info del pago OK desde TMAS" );
            logger.debug("BtnPagoTMAS_NOTIFICACION: numeroTarjeta=****; rutCliente="+rutCliente+"; usoClave="+usoClave);
            
            // Se guarda la Información recibida en la BD
            bp = biz.botonPagoGetByPedido(idPedido);
            bp.setNroTarjeta(numeroTarjeta);
            bp.setRutCliente(rutCliente);
            bp.setClienteValidado(usoClave);
            biz.updateNotificacionBotonPago(bp);
            
        }
        
        biz.ingresarPedidoVteASistema( idPedido );
        
        System.out.println("Pedido ingresado al sistema");
        
        // Envia mail
        try {
            CompradoresDTO comprador = biz.getCompradoresById( idComprador );
            
            ResourceBundle rb = ResourceBundle.getBundle("vte");
            String mail_tpl = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("arc_mail");
            TemplateLoader mail_load = new TemplateLoader(mail_tpl);
            ITemplate mail_tem = mail_load.getTemplate();
            String mail_result = mail_tem.toString(contenidoMailResumen(bp, rb, comprador, pedido, idPedido, biz));
            // Se envía mail al cliente
            MailDTO mail = new MailDTO();
            mail.setFsm_subject(rb.getString("mail.checkout.subject"));
            mail.setFsm_data(mail_result);
            System.out.println("comprador.getCpr_email()->"+comprador.getCpr_email());
            mail.setFsm_destina(comprador.getCpr_email());
            mail.setFsm_remite(rb.getString("mail.checkout.remite"));
            biz.addMail(mail);
        } catch (Exception e) {
            e.printStackTrace();
            this.getLogger().error("Problemas con mail", e);
        }
        arg1.sendRedirect(disOk);
        
    }

    /**
     * @return
     * @throws FuncionalException
     * @throws SystemException
     */
    private IValueSet contenidoMailResumen(BotonPagoDTO bp, ResourceBundle rb, CompradoresDTO comprador, PedidoDTO pedido1, long idPedido, BizDelegate biz) throws  SystemException {
        IValueSet mail_top = new ValueSet();
        
        mail_top.setVariable("{nombre_cliente}", comprador.getCpr_nombres() + " " + comprador.getCpr_ape_pat());

        //SOLO PARA EL CASO DEL BLOQUE SEL_CUOTA
        IValueSet lista_boton = new ValueSet();
        lista_boton.setVariable("{contador_form}", "");
        List list_boton = new ArrayList();
        list_boton.add(lista_boton);

        mail_top.setVariable("{idped}", idPedido + pedido1.getSecuenciaPago());
        mail_top.setVariable("{cantidad}", Formatos.formatoCantidad(pedido1.getCant_prods()) + "");
       
        mail_top.setVariable("{fecha_hoy}", cl.bbr.jumbocl.common.utils.Utils.getFechaActualByPatron("dd/MM/yyyy"));
        mail_top.setVariable("{monto_op}", Formatos.formatoPrecio(pedido1.getMonto()) +"" );
        mail_top.setVariable("{monto_desp}", Formatos.formatoPrecio(pedido1.getCosto_despacho()) +"" );
        mail_top.setVariable("{monto_res}", Formatos.formatoPrecio(pedido1.getMonto_reservado()) +"" );
        
        List textoDespacho = new ArrayList();
        IValueSet filaDespacho = new ValueSet();
        if (pedido1.getTipo_despacho().equalsIgnoreCase("R")) {
            mail_top.setVariable("{fc_hr_despacho}","Fecha y hora de retiro");
            
            filaDespacho.setVariable("{lugar_despacho}",    pedido1.getIndicacion());
            LocalDTO loc = biz.getLocalRetiro(pedido1.getId_local());
            filaDespacho.setVariable("{dir_retiro_pedido}", "("+loc.getDireccion()+")");
            filaDespacho.setVariable("{id_local}", loc.getId_local()+"");
            
            textoDespacho.add(filaDespacho);
            mail_top.setDynamicValueSets("LUGAR_RETIRO", textoDespacho);
            
            textoDespacho = new ArrayList();
            filaDespacho = new ValueSet();                    
            filaDespacho.setVariable("{sin_gente_txt}", pedido1.getSin_gente_txt()+"");
            filaDespacho.setVariable("{sin_gente_rut}", cl.bbr.jumbocl.common.utils.Formatos.formatoRut( pedido1.getSin_gente_rut() ) +"-"+pedido1.getSin_gente_dv());
            textoDespacho.add(filaDespacho);
            mail_top.setDynamicValueSets("SIN_GENTE_RETIRO", textoDespacho);
        } else {
            mail_top.setVariable("{fc_hr_despacho}","Fecha y hora del despacho");
            
            if ( pedido1.getDir_depto().length() > 0 ) {
                filaDespacho.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle()+" "+pedido1.getDir_calle()+" "+pedido1.getDir_numero()+", "+pedido1.getDir_depto()+", "+pedido1.getNom_comuna());
            } else {
                filaDespacho.setVariable("{lugar_despacho}", pedido1.getDir_tipo_calle()+" "+pedido1.getDir_calle()+" "+pedido1.getDir_numero()+", "+pedido1.getNom_comuna());
            }
            textoDespacho.add(filaDespacho);
            mail_top.setDynamicValueSets("LUGAR_DESPACHO", textoDespacho);
            
            textoDespacho = new ArrayList();
            filaDespacho = new ValueSet();                    
            filaDespacho.setVariable("{sin_gente_txt}", pedido1.getSin_gente_txt()+"");
            filaDespacho.setVariable("{sin_gente_rut}", "");
            textoDespacho.add(filaDespacho);
            mail_top.setDynamicValueSets("SIN_GENTE_DESPACHO", textoDespacho);
        }
        
        
        if ( pedido1.getMedio_pago().compareTo("CAT") == 0 ) {
            //******** PAGO CON TARJETAS MAS **********                    
            mail_top.setVariable("{forma_pago}", "Tarjeta Mas" );
            List tagTarjetasMasReMKT = new ArrayList();
            IValueSet filaRMKT = new ValueSet();                    
            filaRMKT.setVariable("{sin_gente_txt}", "");
            tagTarjetasMasReMKT.add(filaRMKT);
            mail_top.setDynamicValueSets("TARJETAS_MAS", tagTarjetasMasReMKT);

            List pagoConTmas = new ArrayList();
            IValueSet filaTmas = new ValueSet();                    
            filaTmas.setVariable("{4_ultimos}", "**** **** **** " + bp.getNroTarjeta().substring(bp.getNroTarjeta().length() - 4));
            filaTmas.setVariable("{cod_aut_trx}", bp.getCodigoAutorizacion());
            filaTmas.setVariable("{nro_cuotas}", cl.bbr.jumbocl.common.utils.Utils.secuenciaStr(bp.getNroCuotas().intValue()));
            filaTmas.setVariable("{tipo_cuotas}", "----");
            pagoConTmas.add(filaTmas);
            mail_top.setDynamicValueSets("PAGO_TRANSBANK", pagoConTmas);                    
            
        } else {
            //*********** PAGO CON TRANSBANK *************
            mail_top.setVariable("{forma_pago}", "Tarjeta Bancaria" );
            
            WebpayDTO wp = biz.webpayGetPedido(idPedido);
            
            List pagoConTransbank = new ArrayList();
            IValueSet filaTransbank = new ValueSet();                    
            filaTransbank.setVariable("{4_ultimos}", "**** **** **** "+wp.getTBK_FINAL_NUMERO_TARJETA());
            filaTransbank.setVariable("{cod_aut_trx}", wp.getTBK_CODIGO_AUTORIZACION());
            filaTransbank.setVariable("{nro_cuotas}", Utils.secuenciaStr(wp.getTBK_NUMERO_CUOTAS()));
            filaTransbank.setVariable("{tipo_cuotas}", Utils.webpayTipoCuotas(wp.getTBK_TIPO_PAGO()));
            pagoConTransbank.add(filaTransbank);
            mail_top.setDynamicValueSets("PAGO_TRANSBANK", pagoConTransbank);                    
        }
        
        String fecha = cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido1.getFdespacho());

        // Cuando es despacho Economico se debe mostrar la Hora de Inicio y Fin de la Jornada completa del día. Ej.: 9:00 - 23:00
        // E: Express, N: Normal, C: Económico
        boolean flag = false;
        String DespHorarioEconomico = rb.getString("DespliegueHorarioEconomico");
        //'C': Completa (09:00 - 23:00), 'P': Parcial (14:00 - 19:00)
        if (DespHorarioEconomico.equalsIgnoreCase("C") && pedido1.getTipo_despacho().equalsIgnoreCase("C") ) {
            mail_top.setVariable("{fecha_tramo}", fecha );
            flag = true;
        }
        if (!flag) {
            int posIni = pedido1.getHdespacho().indexOf(":", 3);
            int posFin = pedido1.getHfindespacho().indexOf(":", 3);
            mail_top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + pedido1.getHdespacho().substring(0, posIni) + " - " + pedido1.getHfindespacho().substring(0, posFin));
        }
        
        if (pedido1.getTipo_doc().compareTo("B") == 0) {
            mail_top.setVariable("{tip_doc}", "Boleta");
        } else if (pedido1.getTipo_doc().compareTo("F") == 0) {
            mail_top.setVariable("{tip_doc}", "Factura");
        }

        //Categorías y Productos
        List fm_cate = new ArrayList();
        int contador = 0;
        long totalizador = 0;

        List productosPorCategoria = biz.getProductosSolicitadosById(idPedido);

        //total_producto_pedido = 0;
        double precio_total = 0;
        for (int i = 0; i < productosPorCategoria.size(); i++) {
            CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) productosPorCategoria.get(i);

            IValueSet fila_cat = new ValueSet();
            fila_cat.setVariable("{categoria}", cat.getCategoria());

            List prod = cat.getCarroCompraProductosDTO();

            List fm_prod = new ArrayList();
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

                    if (producto.isCon_nota() == true) {
                        IValueSet set_nota = new ValueSet();
                        set_nota.setVariable("{nota}", producto.getNota() + "");
                        set_nota.setVariable("{contador}",contador + "");
                        List aux_blanco = new ArrayList();
                        aux_blanco.add(set_nota);
                        fila_pro.setDynamicValueSets("NOTA",aux_blanco);
                    }

                    precio_total = Utils.redondear(producto.getPpum() * producto.getCantidad());
                    fila_pro.setVariable("{unidad}", producto.getTipre());
                    fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecio(producto.getPpum()));
                    fila_pro.setVariable("{precio_total}",Formatos.formatoPrecio(precio_total));
                    fila_pro.setVariable("{CLASE_TABLA}","TablaDiponiblePaso1");
                    fila_pro.setVariable("{CLASE_CELDA}","celda1");
                    fila_pro.setVariable("{NO_DISPONIBLE}", "");
                    fila_pro.setVariable("{OPCION_COMPRA}", "1");
                    totalizador += precio_total;

                    contador++;
                    fm_prod.add(fila_pro);
                }
            }
            fila_cat.setDynamicValueSets("PRODUCTOS", fm_prod);
            fm_cate.add(fila_cat);
        }
        mail_top.setDynamicValueSets("CATEGORIAS", fm_cate);
        
        return mail_top;
    }
	
}