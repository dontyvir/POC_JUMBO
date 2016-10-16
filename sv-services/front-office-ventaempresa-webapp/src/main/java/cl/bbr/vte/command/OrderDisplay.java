package cl.bbr.vte.command;

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

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.clientes.dto.ResumenCompraProductosDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.vte.bizdelegate.BizDelegate;

/**
 * Despliega resumen del pedido
 *  
 */
public class OrderDisplay extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
 	
		System.out.println("**** OrderDisplay (VE) ****");
        
        try {
            ResourceBundle rb = ResourceBundle.getBundle("vte");
            HttpSession session = arg0.getSession();
            PrintWriter out = arg1.getWriter();

            String pag_form = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("pag_form");
            TemplateLoader load = new TemplateLoader(pag_form);
            ITemplate tem = load.getTemplate();
            IValueSet top = new ValueSet();

            long idPedido = Long.parseLong(session.getAttribute("ses_id_pedido").toString());

            //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
            top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());
            //Se setea la variable tipo usuario
            if (session.getAttribute("ses_tipo_usuario") != null) {
                top.setVariable("{tipo_usuario}", session.getAttribute("ses_tipo_usuario").toString());
            } else {
                top.setVariable("{tipo_usuario}", "0");
            }

            // Nombre del comprador para header
            if (session.getAttribute("ses_com_nombre") != null)
                top.setVariable("{nombre_comprador}", session.getAttribute("ses_com_nombre"));
            else
                top.setVariable("{nombre_comprador}", "");

            //Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();

            PedidoDTO pedido = biz.getPedidoById(idPedido);

            top.setVariable("{idped}", pedido.getId_pedido() + pedido.getSecuenciaPago());
            List textoDespacho = new ArrayList();
            IValueSet filaDespacho = new ValueSet();
            if (pedido.getTipo_despacho().equalsIgnoreCase("R")) {
                top.setVariable("{fc_hr_despacho}","Fecha y hora de retiro");
                
                filaDespacho.setVariable("{lugar_despacho}",    pedido.getIndicacion());
                LocalDTO loc = biz.getLocalRetiro(pedido.getId_local());
                filaDespacho.setVariable("{dir_retiro_pedido}", "("+loc.getDireccion()+")");
                filaDespacho.setVariable("{id_local}", loc.getId_local()+"");
                
                textoDespacho.add(filaDespacho);
                top.setDynamicValueSets("LUGAR_RETIRO", textoDespacho);
                
                textoDespacho = new ArrayList();
                filaDespacho = new ValueSet();                    
                filaDespacho.setVariable("{sin_gente_txt}", pedido.getSin_gente_txt()+"");
                filaDespacho.setVariable("{sin_gente_rut}", cl.bbr.jumbocl.common.utils.Formatos.formatoRut( pedido.getSin_gente_rut() ) +"-"+pedido.getSin_gente_dv());
                textoDespacho.add(filaDespacho);
                top.setDynamicValueSets("SIN_GENTE_RETIRO", textoDespacho);
            } else {
                top.setVariable("{fc_hr_despacho}","Fecha y hora del despacho");
                
                if ( pedido.getDir_depto().length() > 0 ) {
                    filaDespacho.setVariable("{lugar_despacho}", pedido.getDir_tipo_calle()+" "+pedido.getDir_calle()+" "+pedido.getDir_numero()+", "+pedido.getDir_depto()+", "+pedido.getNom_comuna());
                } else {
                    filaDespacho.setVariable("{lugar_despacho}", pedido.getDir_tipo_calle()+" "+pedido.getDir_calle()+" "+pedido.getDir_numero()+", "+pedido.getNom_comuna());
                }
                textoDespacho.add(filaDespacho);
                top.setDynamicValueSets("LUGAR_DESPACHO", textoDespacho);
                
                textoDespacho = new ArrayList();
                filaDespacho = new ValueSet();                    
                filaDespacho.setVariable("{sin_gente_txt}", pedido.getSin_gente_txt()+"");
                filaDespacho.setVariable("{sin_gente_rut}", "");
                textoDespacho.add(filaDespacho);
                top.setDynamicValueSets("SIN_GENTE_DESPACHO", textoDespacho);
            }

            top.setVariable("{forma_pago}", Formatos.getDescripcionMedioPago(pedido.getMedio_pago()));
            
            
            if ( pedido.getMedio_pago().compareTo("CAT") == 0 ) {
                //******** PAGO CON TARJETAS MAS **********                    
                // se obtiene info desde la BD para mostrar en resumen
                BotonPagoDTO bp = biz.botonPagoGetByPedido(idPedido);
                
                List pagoConTmas = new ArrayList();
                IValueSet filaTmas = new ValueSet();                    
                filaTmas.setVariable("{4_ultimos}", "**** **** **** " + bp.getNroTarjeta().substring(bp.getNroTarjeta().length() - 4));
                filaTmas.setVariable("{cod_aut_trx}", bp.getCodigoAutorizacion());
                filaTmas.setVariable("{nro_cuotas}", Utils.secuenciaStr(bp.getNroCuotas().intValue()));
                filaTmas.setVariable("{tipo_cuotas}", "----");
                pagoConTmas.add(filaTmas);
                top.setDynamicValueSets("PAGO_TRANSBANK", pagoConTmas);                    
                
                
                List tagTarjetasMasReMKT = new ArrayList();
                IValueSet filaRMKT = new ValueSet();                    
                filaRMKT.setVariable("{sin_gente_txt}", "");
                tagTarjetasMasReMKT.add(filaRMKT);
                top.setDynamicValueSets("TARJETAS_MAS", tagTarjetasMasReMKT);

            } else {
                //*********** PAGO CON TRANSBANK *************
                WebpayDTO wp = biz.webpayGetPedido(idPedido);
                
                List pagoConTransbank = new ArrayList();
                IValueSet filaTransbank = new ValueSet();                    
                filaTransbank.setVariable("{4_ultimos}", "**** **** **** "+wp.getTBK_FINAL_NUMERO_TARJETA());
                filaTransbank.setVariable("{cod_aut_trx}", wp.getTBK_CODIGO_AUTORIZACION());
                filaTransbank.setVariable("{nro_cuotas}", Utils.secuenciaStr(wp.getTBK_NUMERO_CUOTAS()));
                filaTransbank.setVariable("{tipo_cuotas}", Utils.webpayTipoCuotas(wp.getTBK_TIPO_PAGO()));
                pagoConTransbank.add(filaTransbank);
                top.setDynamicValueSets("PAGO_TRANSBANK", pagoConTransbank);                    
            }
            
            
            top.setVariable("{fecha_compra}", Formatos.frmFecha(pedido.getFingreso()));
            top.setVariable("{monto_op}", Formatos.formatoPrecio(pedido.getMonto()) + "");
            top.setVariable("{monto_desp}", Formatos.formatoPrecio(pedido.getCosto_despacho()) + "");
            top.setVariable("{monto_res}", Formatos.formatoPrecio(pedido.getMonto_reservado()) + "");

            String fecha = Formatos.frmFecha(pedido.getFdespacho());

            // E: Express, N: Normal, C: Económico
            //'C': Completa (09:00 - 23:00), 'P': Parcial (14:00 - 19:00)
            if ( Constantes.TIPO_DESPACHO_ECONOMICO_CTE.equalsIgnoreCase(pedido.getTipo_despacho()) ) {
                top.setVariable("{fecha_tramo}", fecha);
            } else {
                int posIni = pedido.getHdespacho().indexOf(":", 3);
                int posFin = pedido.getHfindespacho().indexOf(":", 3);
                top.setVariable("{fecha_tramo}", fecha + "&nbsp;&nbsp;" + pedido.getHdespacho().substring(0, posIni) + " - " + pedido.getHfindespacho().substring(0, posFin));
            }
            top.setVariable("{cantidad}", Formatos.formatoNumeroSinDecimales(pedido.getCant_prods()) + "");

            top.setVariable("{tip_doc}", Formatos.getDescripcionTipoDocumento(pedido.getTipo_doc()));
            
            top.setVariable("{webpay_url}", rb.getString("webpay.url") + "");
            top.setVariable("{webpay_url_kit}", rb.getString("webpay.kit.pago") + "");
            
            top = showProductosPedido(top, biz, idPedido);
            
            String result = tem.toString(top);
            out.print(result);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
            //throw new CommandException(e);
        }
        
	}
    
    
    private IValueSet showProductosPedido(IValueSet top, BizDelegate biz, long idPedido) throws SystemException {
//      Categorías y Productos
        List fm_cate = new ArrayList();
        List fm_prod = null;
        int contador=0;
        long totalizador = 0;
        
        List datos_cat = biz.resumenCompraGetCategoriasProductos(idPedido);
        
        long total_producto_pedido = 0;
        double precio_total = 0;
        IValueSet fila_cat = null;
        List prod = null;
        IValueSet fila_pro = null;
        IValueSet fila_lista_sel = null;
        IValueSet set_nota = null;
        List aux_blanco = null;
        for( int i = 0; i < datos_cat.size(); i++ ) {
            
            CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) datos_cat.get(i);
                            
            fila_cat = new ValueSet();
            fila_cat.setVariable("{categoria}", cat.getCategoria());
            
            prod = cat.getCarroCompraProductosDTO();
                            
            fm_prod = null;
            fm_prod = new ArrayList();              
            for (int j = 0; j < prod.size(); j++) {
                
                ResumenCompraProductosDTO producto = (ResumenCompraProductosDTO) prod.get(j);
                
                total_producto_pedido += Math.ceil(producto.getCantidad());
                
                fila_pro = new ValueSet();
                fila_pro.setVariable("{descripcion}", producto.getNombre());
                fila_pro.setVariable("{marca}", producto.getMarca());
                fila_pro.setVariable("{cod_sap}", producto.getCodigo());
                fila_pro.setVariable("{valor}", Formatos.formatoIntervalo(producto.getCantidad())+"");
                fila_pro.setVariable("{contador}", contador+"");
                                    
                precio_total = 0;
                
                fila_lista_sel = new ValueSet();
                fila_lista_sel.setVariable("{contador}", contador+"");
                fila_lista_sel.setVariable("{valor}", producto.getCantidad() + "");
                aux_blanco = new ArrayList();
                aux_blanco.add(fila_lista_sel);
                fila_pro.setDynamicValueSets("INPUT_SEL",aux_blanco);
                
                set_nota = new ValueSet();
                set_nota.setVariable("{nota}", producto.getNota()+"");
                set_nota.setVariable("{contador}", contador+"");
                aux_blanco = new ArrayList();
                aux_blanco.add(set_nota);
                fila_pro.setDynamicValueSets("NOTA", aux_blanco);                   
                    
                precio_total = Utils.redondear( producto.getPrecio()*producto.getCantidad() );
                fila_pro.setVariable("{unidad}", producto.getCantidad() + "");
                fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecio(producto.getPrecio()) );
                fila_pro.setVariable("{precio_total}", Formatos.formatoPrecio(precio_total) );              
                fila_pro.setVariable("{CLASE_TABLA}", "TablaDiponiblePaso1");
                fila_pro.setVariable("{CLASE_CELDA}", "celda1");
                fila_pro.setVariable("{NO_DISPONIBLE}", "");
                fila_pro.setVariable("{OPCION_COMPRA}", "1");
                totalizador += precio_total;                        
                
                contador++;
                fm_prod.add(fila_pro);
            }
            fila_cat.setDynamicValueSets("PRODUCTOS", fm_prod);
            fm_cate.add(fila_cat);
        }
        top.setDynamicValueSets("CATEGORIAS", fm_cate);
        return top;
    }
	
}