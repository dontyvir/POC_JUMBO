package cl.bbr.fo.command;

import java.io.PrintWriter;
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
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.clientes.dto.ResumenCompraProductosDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;

/**
 * Impresión del pedido final
 * 
 * @author BBRI
 * 
 */
public class OrderPrint extends Command {

	/**
	 * Impresión del pedido final
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
				
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
			
			// Recupera pagina desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			
			//long pedido_id = Long.parseLong(arg0.getParameter("idped").toString());
			long pedido_id = 0;
			if( session.getAttribute("sesspedido") != null )
				pedido_id = Long.parseLong(session.getAttribute("sesspedido")+"");
			else
				throw new CommandException( "No tiene id del pedido" );
			
			IValueSet top = new ValueSet();
						
			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			Calendar cal = new GregorianCalendar();
			long minutos = (cal.getTimeInMillis()-session.getCreationTime())/1000/60;
			String  str_minuto = "";
			if (minutos < 2){
				str_minuto = "minuto";
			}else{
				str_minuto = "minutos";
			}
			
			if (minutos <= Integer.parseInt(rb.getString("pedido.minutos"))){
				top.setVariable("{msg_time_compra}", rb.getString("orderprint.mensaje1") + minutos +" "+str_minuto+ rb.getString("orderprint.mensaje2") );
			}else{
				top.setVariable("{msg_time_compra}", "");
			}
            
            if ( arg0.getParameter("print") != null ) {
                top.setVariable("{print_page}", "window.print();");    
            } else {
                top.setVariable("{print_page}", "");
            }
            
            String horasDespachoEconomico = "";
            if ( arg0.getParameter("horas_desp_eco") != null ) {
                horasDespachoEconomico = arg0.getParameter("horas_desp_eco");
            }

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			PedidoDTO pedido = biz.getPedidoById(pedido_id);
            
            if ( pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO ) {
                //Error no controlado, cuando llaman a ver el resumen de una OP que este preingresado
                throw new CommandException();
            }

//			String num_tarjeta = pedido.getNum_mp();
			//SOLO PARA EL CASO DEL BLOQUE SEL_CUOTA
			IValueSet lista_boton = new ValueSet();
			lista_boton.setVariable("{contador_form}", "");	
			List list_boton = new ArrayList();
			list_boton.add(lista_boton);
			
			
			top.setVariable("{idped}", pedido_id + pedido.getSecuenciaPago() );
			top.setVariable("{cantidad}", Formatos.formatoCantidadFO(pedido.getCant_prods())+"" );
			
            top.setVariable("{fecha_hoy}", cl.bbr.jumbocl.common.utils.Utils.getFechaActualByPatron("dd/MM/yyyy"));
            top.setVariable("{monto_op}", Formatos.formatoPrecioFO(pedido.getMonto()) +"" );
            top.setVariable("{monto_desp}", Formatos.formatoPrecioFO(pedido.getCosto_despacho()) +"" );
            top.setVariable("{monto_res}", Formatos.formatoPrecioFO(pedido.getMonto_reservado()) +"" );
            
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
            
			
            if ( pedido.getMedio_pago().compareTo("CAT") == 0 ) {
                //******** PAGO CON TARJETAS MAS **********                    
                top.setVariable("{forma_pago}", "Tarjeta Mas" );
                List tagTarjetasMasReMKT = new ArrayList();
                IValueSet filaRMKT = new ValueSet();                    
                filaRMKT.setVariable("{sin_gente_txt}", "");
                tagTarjetasMasReMKT.add(filaRMKT);
                top.setDynamicValueSets("TARJETAS_MAS", tagTarjetasMasReMKT);

                BotonPagoDTO bp = biz.botonPagoGetByPedido(pedido_id);                    
                List pagoConTmas = new ArrayList();
                IValueSet filaTmas = new ValueSet();                    
                filaTmas.setVariable("{4_ultimos}", (bp.getNroTarjeta()==null?"":"**** **** **** "+bp.getNroTarjeta().substring(bp.getNroTarjeta().length()-4)));
                filaTmas.setVariable("{cod_aut_trx}", bp.getCodigoAutorizacion());
                filaTmas.setVariable("{nro_cuotas}", cl.bbr.jumbocl.common.utils.Utils.secuenciaStr(bp.getNroCuotas().intValue()));
                filaTmas.setVariable("{tipo_cuotas}", "----");
                pagoConTmas.add(filaTmas);
                top.setDynamicValueSets("PAGO_TRANSBANK", pagoConTmas);                    
                
            } else {
                //*********** PAGO CON TRANSBANK *************
                top.setVariable("{forma_pago}", "Tarjeta Bancaria" );
                
                WebpayDTO wp = biz.webpayGetPedido(pedido_id);
                
                List pagoConTransbank = new ArrayList();
                IValueSet filaTransbank = new ValueSet();                    
                filaTransbank.setVariable("{4_ultimos}", "**** **** **** "+wp.getTBK_FINAL_NUMERO_TARJETA());
                filaTransbank.setVariable("{cod_aut_trx}", wp.getTBK_CODIGO_AUTORIZACION());
                filaTransbank.setVariable("{nro_cuotas}", cl.bbr.jumbocl.common.utils.Utils.secuenciaStr(wp.getTBK_NUMERO_CUOTAS()));
                filaTransbank.setVariable("{tipo_cuotas}", cl.bbr.jumbocl.common.utils.Utils.webpayTipoCuotas(wp.getTBK_TIPO_PAGO()));
                pagoConTransbank.add(filaTransbank);
                top.setDynamicValueSets("PAGO_TRANSBANK", pagoConTransbank);                    
            }
          
			String fecha = cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido.getFdespacho());
			top.setVariable("{fecha_tramo}", fecha + " " + pedido.getHdespacho() + "-" + pedido.getHfindespacho() );
            
            
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
			
			if(pedido.getN_cuotas() == 0){
				lista_boton.setVariable("{num_cuotas}", "Sin cuotas");
			}else{
				lista_boton.setVariable("{num_cuotas}", pedido.getN_cuotas() + "");
			}
			
//			if( !(pedido.getMedio_pago().compareTo("CAT") == 0 && num_tarjeta.compareTo("************1111") == 0)){
//				top.setDynamicValueSets("SEL_CUOTA", list_boton);//No agrega el bloque
//			}
			
			
			if (pedido.getSin_gente_txt() != null)
				top.setVariable("{sin_gente_txt}", pedido.getSin_gente_txt()+"");
			else
				top.setVariable("{sin_gente_txt}", "");
			
			if (pedido.getTipo_doc().compareTo("B") == 0){
				top.setVariable("{tip_doc}","Boleta");
			}else if(pedido.getTipo_doc().compareTo("F") == 0){
				top.setVariable("{tip_doc}","Factura");
			}

			
			//Categorías y Productos
			List fm_cate = new ArrayList();
			List fm_prod = null;
			ResumenCompraProductosDTO producto = null;
			int contador=0;
			long totalizador = 0;
			
			List datos_cat = biz.resumenCompraGetCategoriasProductos(pedido_id);
			
			long total_producto_pedido = 0;
			double precio_total = 0;
			CarroCompraCategoriasDTO cat = null;
			IValueSet fila_cat = null;
			List prod = null;
			IValueSet fila_pro = null;
			IValueSet fila_lista_sel = null;
			IValueSet set_nota = null;
			List aux_blanco = null;
			for( int i = 0; i < datos_cat.size(); i++ ) {
				
				cat = (CarroCompraCategoriasDTO) datos_cat.get(i);
								
				fila_cat = new ValueSet();
				fila_cat.setVariable("{categoria}", cat.getCategoria());
				
				prod = cat.getCarroCompraProductosDTO();
								
				fm_prod = null;
				fm_prod = new ArrayList();				
				for (int j = 0; j < prod.size(); j++) {
					
					producto = (ResumenCompraProductosDTO) prod.get(j);
					
					total_producto_pedido += Math.ceil(producto.getCantidad());
					
					fila_pro = new ValueSet();
					fila_pro.setVariable("{descripcion}", producto.getNombre());
					fila_pro.setVariable("{marca}", producto.getMarca());
					fila_pro.setVariable("{cod_sap}", producto.getCodigo());
					fila_pro.setVariable("{valor}", Formatos.formatoIntervaloFO(producto.getCantidad())+"");
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
						
					precio_total = Utils.redondearFO( producto.getPrecio()*producto.getCantidad() );
					fila_pro.setVariable("{unidad}", producto.getCantidad() + "");
					fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecioFO(producto.getPrecio()) );
					fila_pro.setVariable("{precio_total}", Formatos.formatoPrecioFO(precio_total) );				
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
				
				this.getLogger().info( "Categoria: "+cat.getCategoria()+ "("+cat.getId()+") Productos:"+fm_prod.size() );
				
			}
			top.setDynamicValueSets("CATEGORIAS", fm_cate);
			
			
			//PRODUCTOS			
			//top.setVariable("{total}", Formatos.formatoPrecio(totalizador)+"");
			
			String result = tem.toString(top);
			
			out.print(result);

		}
		catch (Exception e) {
			this.getLogger().error(e);
			this.getLogger().error( "(OrderPrint)", e );
			throw new CommandException( "(OrderPrint) Problema", e );
		}
		
	}

}
