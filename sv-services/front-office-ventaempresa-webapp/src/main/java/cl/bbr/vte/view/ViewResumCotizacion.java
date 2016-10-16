package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.vte.utils.vteFormatos;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;
import cl.bbr.vte.empresas.dto.ComprXSucDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra los datos de la cotización y el detalle de productos (resumen) de una cotización
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewResumCotizacion extends Command {

	/**
	 * Despliega el detalle de una cotización
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("vte");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			// Estado de la cotización
			long estado_cot = 0;
			
			// Detalle de cotización
			CotizacionesDTO detcoti = null; 
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("pag_form");
			logger.debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();
			
            //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
			top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());

			//Se setea la variable tipo usuario
			if(session.getAttribute("ses_tipo_usuario") != null ){
				top.setVariable("{tipo_usuario}", session.getAttribute("ses_tipo_usuario").toString());
			}else{
				top.setVariable("{tipo_usuario}", "0");
			}

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// Número de tarjeta
			String cot_num_mpago = "";
			
			// Validar si puede o no ver la cotización
			if (arg0.getParameter("cot_id") != null && Long.parseLong(arg0.getParameter("cot_id")) > 0){
				// Revisar si el comprador puede ver la cotización
				// 1.- Si la cotización es del comprador => OK
				CotizacionesDTO cot = biz.getCotizacionesById( Long.parseLong(arg0.getParameter("cot_id")) );
				if( cot.getCot_comprador_id() != Long.parseLong(session.getAttribute("ses_com_id").toString()) ) {
					// 2.- Si el comprador es administrador y la cotización es para una sucursal que administra
					if( true ) {
						// Recuperar las sucursales que tiene acceso como comprador
						List l_suc = biz.getListSucursalesByCompradorId( Long.parseLong(session.getAttribute("ses_com_id").toString()) );
						ComprXSucDTO dto = null;
						boolean existe_comuna = false;
						for( int i = 0; i < l_suc.size(); i++ ) {
							dto = (ComprXSucDTO)l_suc.get(i);
							if( dto.getId_sucursal() == cot.getCot_cli_id() )
								existe_comuna = true;
						}
						if( existe_comuna == false ) {
							// Enviar a Página de error si no cumple 1 y 2
							logger.warn( "No tiene permisos de acceso para la cotización. cot_id:"+arg0.getParameter("cot_id") + " com_id:"+session.getAttribute("ses_com_id").toString());
							throw new SystemException( "No tiene permisos de acceso para la cotización." );
						}
					}
				}
				// Se deja en una variable de session el cot_id si es que viene
				session.setAttribute("ses_cot_id", Long.parseLong(arg0.getParameter("cot_id")) + "" );
			}			
			// Nombre del comprador para header
			if( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );		

			// Se revisa si se ha llegado con error
			
			if (arg0.getParameter("cod_error") != null) {
				logger.debug("cod_error = " + arg0.getParameter("cod_error"));
				top.setVariable("{msg_error}", "1");
				top.setVariable("{desc_msg_error}", rb.getString("general.mensaje.error"));
				// Poner los datos en el formulario para reintentar
				top.setVariable("{num_cuotas}", arg0.getParameter("num_cuotas"));
				top.setVariable("{num_tja}", arg0.getParameter("num_tja"));
				top.setVariable("{valor_medio_pago}", arg0.getParameter("valor_medio_pago"));
				top.setVariable("{sin_gente_txt_tmp}", arg0.getParameter("sin_gente_txt"));
				top.setVariable("{j_numtja1_tmp}", arg0.getParameter("j_numtja1"));
				top.setVariable("{j_numtja2_tmp}", arg0.getParameter("j_numtja2"));
				top.setVariable("{j_numtja3_tmp}", arg0.getParameter("j_numtja3"));
				top.setVariable("{j_numtja4_tmp}", arg0.getParameter("j_numtja4"));
				top.setVariable("{sustitucion_tmp}", arg0.getParameter("sustitucion"));
				top.setVariable("{nom_tban_tmp}", arg0.getParameter("nom_tban"));
				top.setVariable("{t_numero_tmp}", arg0.getParameter("t_numero"));
				top.setVariable("{t_mes_tmp}", arg0.getParameter("t_mes"));
				top.setVariable("{t_ano_tmp}", arg0.getParameter("t_ano"));
				top.setVariable("{t_banco_tmp}", arg0.getParameter("t_banco"));
				top.setVariable("{ped_obs_tmp}", arg0.getParameter("ped_obs"));
			} else { // Cuando es una modificación
				top.setVariable("{num_cuotas}", "");
				top.setVariable("{num_tja}", "");
				top.setVariable("{valor_medio_pago}", "");
				top.setVariable("{sin_gente_txt_tmp}", "");
				top.setVariable("{j_numtja1_tmp}", "");
				top.setVariable("{j_numtja2_tmp}", "");
				top.setVariable("{j_numtja3_tmp}", "");
				top.setVariable("{j_numtja4_tmp}", "");
				top.setVariable("{sustitucion_tmp}", "");
				top.setVariable("{nom_tban_tmp}", "");
				top.setVariable("{t_numero_tmp}", "");
				top.setVariable("{t_mes_tmp}", "");
				top.setVariable("{t_ano_tmp}", "");
				top.setVariable("{t_banco_tmp}", "");				
				top.setVariable("{ped_obs_tmp}", "");
				top.setVariable("{cot_id}", "");
				top.setVariable("{nom_emp}", "");
				top.setVariable("{nom_com}", "");
				top.setVariable("{dir_des}", "");
				top.setVariable("{dir_fac}", "");
				top.setVariable("{fecha_des}", "");
				top.setVariable("{subtotalGen}", "");
				top.setVariable("{cantTot}", "");
				top.setVariable("{comentario", "");
			}
			
			// Recuperar datos de la cotización
			long cot_id = 0;
			if(session.getAttribute("ses_cot_id") != null && !session.getAttribute("ses_cot_id").equals("")){
				cot_id = Long.parseLong(session.getAttribute("ses_cot_id").toString());
				
				//Recupera el encabezado de la cotizacion
				detcoti = biz.getCotizacionesById(cot_id);
				top.setVariable("{cot_id}", detcoti.getCot_id()+"");
				top.setVariable("{estado}", detcoti.getCot_estado()+"");
				top.setVariable("{nom_suc}", detcoti.getCot_nom_suc()+"");
				top.setVariable("{nom_emp}", detcoti.getCot_nom_emp());
				top.setVariable("{nom_com}", detcoti.getCot_nom_comp());
				top.setVariable("{dir_des}", detcoti.getCot_alias_dir()+" "+detcoti.getCot_calle_dir()+", "+detcoti.getCot_numero_dir()+", "+detcoti.getNomcomunades());
				top.setVariable("{dir_fac}", detcoti.getCot_dfac_calle()+" "+detcoti.getCot_dfac_numero()+", "+detcoti.getNomcomunafac());
				top.setVariable("{fecha_des}", Formatos.frmFechaHora(detcoti.getCot_fec_acordada()));
				top.setVariable("{fecha_exp}", Formatos.frmFecha(detcoti.getCot_fec_vencimiento()));
				top.setVariable("{sin_gente_txt_tmp}", detcoti.getPersona_auto());
				if (detcoti.getPersona_auto() == null){
					top.setVariable("{sin_gente_txt_tmp}", "");
				}
			
				top.setVariable("{ped_obs_tmp}", detcoti.getCot_obs());
				
				top.setVariable("{sustitucion_tmp}", detcoti.getSustitucion());
				if(detcoti.getSustitucion() == null){
					top.setVariable("{sustitucion_tmp}", "");
				}
				
				//Revisa si tiene valor la variable de session ses_exito_envia_cot y envia un msg de exito en la pagina 
				if(session.getAttribute("ses_exito_envia_cot") != null && detcoti.getCot_estado_id() == Constantes.ID_EST_COTIZACION_EN_REVISION){
					top.setVariable("{msg_exito_envia_cot}", "1");
					top.setVariable("{desc_msg_exito_envia_cot}", rb.getString("resumen.mensaje.exito.envio.cotizacion"));
					session.removeAttribute("ses_exito_envia_cot");
				}				
				
				// Obtener número de tarjeta si es que existe
				if( detcoti.getCot_num_mpago() != null && detcoti.getCot_num_mpago().length() > 0 ) {
					cot_num_mpago = Cifrador.desencriptar(rb.getString("conf.bo.key"),detcoti.getCot_num_mpago());

					if (cot_num_mpago.length() > 4)
						cot_num_mpago = "************"+cot_num_mpago.substring(cot_num_mpago.length()-4);

					// Dividir para tarjeta jumbo mas
					if (cot_num_mpago.length() == 16){
						top.setVariable("{j_numtja1_tmp}", cot_num_mpago.substring(0,4));
						top.setVariable("{j_numtja2_tmp}", cot_num_mpago.substring(4,8));
						top.setVariable("{j_numtja3_tmp}", cot_num_mpago.substring(8,12));
						top.setVariable("{j_numtja4_tmp}", cot_num_mpago.substring(12,16));
					}
				}	
				
				top.setVariable("{comentario}", detcoti.getCot_obs()+"");

				// Datos del medio de pago
				//top.setVariable("{nom_tban_tmp}", "---");
				//top.setVariable("{t_numero_tmp}", "---");
				//top.setVariable("{t_banco_tmp}", "---");
				//top.setVariable("{t_mes_tmp}", "-");
				//top.setVariable("{t_ano_tmp}", "-");		
				//top.setVariable("{num_cuotas}", "---");

				logger.info("cot_num_mpago:"+cot_num_mpago);
				logger.info("getCot_mpago:"+detcoti.getCot_mpago());
				
				if ( Constantes.MEDIO_PAGO_CAT.equalsIgnoreCase( detcoti.getCot_mpago() ) ) {
					top.setVariable("{medio_pago}", Constantes.MEDIO_PAGO_CAT_DESC );
					top.setVariable("{valor_medio_pago}", "1" );					
				} else if ( Constantes.MEDIO_PAGO_TBK.equalsIgnoreCase( detcoti.getCot_mpago() ) ) {
					top.setVariable("{medio_pago}", Constantes.MEDIO_PAGO_TBK_DESC );
					top.setVariable("{valor_medio_pago}", "4" );						
				} else if ( Constantes.MEDIO_PAGO_LINEA_CREDITO.equalsIgnoreCase( detcoti.getCot_mpago() ) ) {
					top.setVariable("{valor_medio_pago}", "5" );
					top.setVariable("{medio_pago}", Constantes.MEDIO_PAGO_LINEA_CREDITO_DESC );
				} 
//                else if( detcoti.getCot_mpago().equals("CAT") && cot_num_mpago.equals("************1111") == true ) {
//					top.setVariable("{medio_pago}", "Cheque" );
//					top.setVariable("{valor_medio_pago}", "3" );
//				}
				
				//Si existe el valor de numero de coutas se muestra el bloque.
				List list_nc = new ArrayList();
				IValueSet fila_nc = new ValueSet();
				if( !( detcoti.getCot_mpago().equals("CAT") && cot_num_mpago.equals("************1111") == true ) && detcoti.getCot_ncuotas()+"" != null ){
					fila_nc.setVariable("{num_cuotas}", detcoti.getCot_ncuotas()+"");
					list_nc.add(fila_nc);
					top.setDynamicValueSets("BLOQUE_COUTAS", list_nc );
				}

				//Si existe el valor de la tarjeta bancaria se muestra el bloque.
				List list_tt = new ArrayList();
				IValueSet fila_tt = new ValueSet();
				if(detcoti.getCot_nomtbank() != null && !detcoti.getCot_nomtbank().equals("")){
					fila_tt.setVariable("{nom_tban_tmp}", detcoti.getCot_nomtbank()+"");
					list_tt.add(fila_tt);
					top.setDynamicValueSets("BLOQUE_TTARJ", list_tt );
				}
				
				//Si existe el valor del numero de la tarjeta se muestra el bloque.
				List list_nt = new ArrayList();
				IValueSet fila_nt = new ValueSet();
				if( !( detcoti.getCot_mpago().equals("CAT") && cot_num_mpago.equals("************1111") == true ) && cot_num_mpago+"" != null && !cot_num_mpago.equals("")){
					fila_nt.setVariable("{t_numero_tmp}", cot_num_mpago+"");
					list_nt.add(fila_nt);
					top.setDynamicValueSets("BLOQUE_NTARJ", list_nt );
				}		

				//Si existe el valor de la fecha de expiracion se muestra el bloque.
				List list_fe = new ArrayList();
				IValueSet fila_fe = new ValueSet();
				if(detcoti.getFecha_expira() != null && !detcoti.getFecha_expira().equals("")){
					fila_fe.setVariable("{t_mes_tmp}", detcoti.getFecha_expira().substring(0,2));
					fila_fe.setVariable("{t_ano_tmp}", detcoti.getFecha_expira().substring(2,4));
					list_fe.add(fila_fe);
					top.setDynamicValueSets("BLOQUE_FTARJ", list_fe );
				}					
				
				//Si existe el valor del nombre del banco se muestra el bloque.				
				List list_nb = new ArrayList();
				IValueSet fila_nb = new ValueSet();
				if(detcoti.getNombre_banco() != null && !detcoti.getNombre_banco().equals("")){
					fila_nb.setVariable("{t_banco_tmp}", detcoti.getNombre_banco()+"");
					list_nb.add(fila_nb);
					top.setDynamicValueSets("BLOQUE_NBANCO", list_nb );
				}	
				
				// Almacenar el estado de la cotización
				estado_cot = detcoti.getCot_estado_id();
				
				//Recupera el listado de detalles de la cotizacio
				List lista = new ArrayList(); 
				if( detcoti.getCot_estado_id() == Constantes.ID_EST_COTIZACION_INGRESADA )
					lista = biz.getProductosDetCotiz(cot_id);
				else
					lista = biz.getProductosCotiz(cot_id);
					
				List datos = new ArrayList();	
				double subtotalGen = 0;
				double subtotalGen_lista = 0;
				double descGen = 0;
				double cantTot = 0;
				double subtotalXdet;
				for (int i = 0; i < lista.size(); i++) {
					ProductosCotizacionesDTO proXcoti = (ProductosCotizacionesDTO) lista.get(i);  
					IValueSet fila = new ValueSet();
					
					if (detcoti.getCot_estado_id() == Constantes.ID_EST_COTIZACION_INGRESADA ){
						fila.setVariable("{codigo}", proXcoti.getDetcot_proId()+ "");
					}else{
						fila.setVariable("{codigo}", proXcoti.getDetcot_id_fo() + "");
					}
					
					fila.setVariable("{descrip}", proXcoti.getDetcot_desc());
					fila.setVariable("{precio}", vteFormatos.formatoPrecio(Utils.redondear(proXcoti.getDetcot_precio())));
					fila.setVariable("{cantidad}", proXcoti.getDetcot_cantidad() + "");
					//subtotalXdet = Utils.redondear(proXcoti.getDetcot_precio() * proXcoti.getDetcot_cantidad());
					subtotalXdet = proXcoti.getDetcot_precio() * proXcoti.getDetcot_cantidad();
					fila.setVariable("{subtotalXdet}", vteFormatos.formatoPrecio(subtotalXdet)+"");
					subtotalGen += subtotalXdet; //Suma los sobtotales por detalle
					//subtotalGen_lista += Utils.redondear(proXcoti.getDetcot_precio_lista() * proXcoti.getDetcot_cantidad()); //Suma los sobtotales por detalle al precio de lista
					subtotalGen_lista += proXcoti.getDetcot_precio_lista() * proXcoti.getDetcot_cantidad(); //Suma los sobtotales por detalle al precio de lista
					descGen += proXcoti.getDetcot_dscto_item();//Suma los descuentos por detalle
					cantTot += proXcoti.getDetcot_cantidad();//Suma las cantidades por detalle
					
					datos.add(fila);
				}
				
				// Presentar el descuento de la cotización
				double porc_descuento = 100-(subtotalGen*100)/subtotalGen_lista;
				if( porc_descuento > 0 ) {
					
					IValueSet fila = new ValueSet();
					fila.setVariable("{codigo}", "");
					fila.setVariable("{descrip}", "");
					fila.setVariable("{precio}", "Descuento Aplic.");
					fila.setVariable("{cantidad}", "");
					fila.setVariable("{subtotalXdet}", vteFormatos.formatoIntervalo1(porc_descuento)+" %");
					datos.add(fila);

				    
					/*List l_descuento = new ArrayList();
					IValueSet fila_descuento = new ValueSet();
					fila_descuento.setVariable("{porc_descuento}", vteFormatos.formatoIntervalo(porc_descuento)+"");
					//fila_descuento.setVariable("{porc_descuento}", porc_descuento+"");
					l_descuento.add(fila_descuento);
					top.setDynamicValueSets("BLOQUE_DESCUENTO",l_descuento);*/
					
				}

				
				
				// Incorporación del Costo de despacho
				
				if (detcoti.getCot_estado_id() != Constantes.ID_EST_COTIZACION_EN_REVISION &&
				        detcoti.getCot_estado_id() != Constantes.ID_EST_COTIZACION_EN_VALIDACION){
					IValueSet fila = new ValueSet();
					fila.setVariable("{codigo}", "");
					fila.setVariable("{descrip}", "");
					fila.setVariable("{precio}", "Costo Despacho");
					fila.setVariable("{cantidad}", "");
					fila.setVariable("{subtotalXdet}", vteFormatos.formatoPrecio(detcoti.getCot_costo_desp()));
					datos.add(fila);
					
					subtotalGen = subtotalGen + detcoti.getCot_costo_desp();
				}
				//************************************
				

				
				top.setDynamicValueSets("ListaProductos", datos);
				
				top.setVariable("{subtotalGen}", vteFormatos.formatoPrecio(subtotalGen)+"");
				top.setVariable("{totalcoti}", subtotalGen+"");
				top.setVariable("{cantTot}", vteFormatos.formatoCantidad(cantTot)+"");
				

				String prod_mix = detcoti.getCot_fueramix();
				// Presentar los productos fuera de mix
				List l_mix = new ArrayList();
				String[] arr = prod_mix.split("##");
				for( int i = 0; i < arr.length; i++ ) {
					String[] datos_fm = arr[i].split("--");
					if( datos_fm.length > 1 ) {
						IValueSet fila_mix = new ValueSet();
						fila_mix.setVariable("{descrip}", datos_fm[0]);
						fila_mix.setVariable("{precio}", vteFormatos.formatoPrecio(Double.valueOf(datos_fm[1]).doubleValue()));
						fila_mix.setVariable("{cantidad}", vteFormatos.formatoCantidad2(Double.valueOf(datos_fm[2]).doubleValue())+"");
						l_mix.add(fila_mix);
					}
				}
				top.setDynamicValueSets("ListaProductosMix",l_mix);			
				
			}
			
			// Acciones que puede realizar la cotización
			List l_vacio = new ArrayList();
			IValueSet fila_vacia = new ValueSet();
			l_vacio.add(fila_vacia);
			
			List l_mpa_form = new ArrayList();			

			// Solicitar datos correspondiente al medio de pago
			if( estado_cot == Constantes.ID_EST_COTIZACION_COTIZADA && detcoti != null ) {	
				
				if( detcoti.getCot_mpago().equals("CAT") && cot_num_mpago.equals("************1111") == false ) {
					IValueSet fila = new ValueSet();					
					fila.setDynamicValueSets("MEDIO_CAT", l_vacio );
					l_mpa_form.add(fila);					
				} else if( detcoti.getCot_mpago().equals("TBK")) {
					IValueSet fila = new ValueSet();
					fila.setDynamicValueSets("MEDIO_TBK", l_vacio );
					l_mpa_form.add(fila);
				} else if( detcoti.getCot_mpago().equals("CRE")) {
					IValueSet fila = new ValueSet();
					//l_mpa_form.setDynamicValueSets("MEDIO_LIN", l_vacio );
					l_mpa_form.add(fila);
				} else if( detcoti.getCot_mpago().equals("CAT") && cot_num_mpago.equals("************1111") == true ) {
					IValueSet fila = new ValueSet();
					fila.setDynamicValueSets("MEDIO_CHE", l_vacio );
					l_mpa_form.add(fila);
				}
				top.setDynamicValueSets("FORM_MPA", l_mpa_form );
				
			}
			
			// Si cotización esta en estado "Ingresada"
			if (estado_cot == Constantes.ID_EST_COTIZACION_INGRESADA ){
				top.setDynamicValueSets("ACCION_PRODUCTOS", l_vacio );
			}
			// Si cotización esta en estado "Cotizada"
			if (estado_cot == Constantes.ID_EST_COTIZACION_COTIZADA ){
				top.setDynamicValueSets("ACCION_ACEPTAR", l_vacio );		
			}
			// Si cotización esta en estado "Ingresada" o "Cotizada"
			if (estado_cot == Constantes.ID_EST_COTIZACION_INGRESADA || estado_cot == Constantes.ID_EST_COTIZACION_COTIZADA ){
				top.setDynamicValueSets("ACCION_ANULAR", l_vacio );		
			}
			
			String result = tem.toString(top);

			out.print(result);

	}

}