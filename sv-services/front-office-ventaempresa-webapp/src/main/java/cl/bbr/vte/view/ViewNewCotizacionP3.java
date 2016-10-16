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
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.vte.utils.vteFormatos;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra los datos de la cotizacion y el detalle de sus productos.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewNewCotizacionP3 extends Command {

	/**
	 * Despliega el detalle de una cotización PASO3
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


			// Nombre del comprador para header
			if( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );		

			long cot_id = 0;
			if (arg0.getParameter("cot_id") != null && Long.parseLong(arg0.getParameter("cot_id")) > 0) {
				cot_id = Long.parseLong(arg0.getParameter("cot_id"));
				session.setAttribute("ses_cot_id", Long.parseLong(arg0.getParameter("cot_id")) + "" );
			}
			else
				cot_id = Long.parseLong(session.getAttribute("ses_cot_id").toString());
			
					
			
			// Datos de los productos fuera de mix
			String prod_mix = "";
			if( arg0.getParameter("prod_mix") != null ) {
				prod_mix = arg0.getParameter("prod_mix");
				session.setAttribute("ses_prod_mix", prod_mix);
			} else if( session.getAttribute("ses_prod_mix") != null ) {
				prod_mix = session.getAttribute("ses_prod_mix").toString();
			}
			top.setVariable("{prod_mix}", prod_mix);
			
			// Presentar los productos fuera de mix
			List l_mix = new ArrayList();
			String[] arr = prod_mix.split("##");
			for( int i = 0; i < arr.length; i++ ) {
				logger.debug(arr.length+"-"+arr[i]+"-");
				String[] datos = arr[i].split("--");
				if( datos.length > 1 ) {
					IValueSet fila_mix = new ValueSet();
					fila_mix.setVariable("{descrip}", datos[0]);
					fila_mix.setVariable("{precio}", vteFormatos.formatoPrecio(Double.valueOf(datos[1]).doubleValue()));
					fila_mix.setVariable("{cantidad}", vteFormatos.formatoCantidad2(Double.valueOf(datos[2]).doubleValue())+"");
					l_mix.add(fila_mix);
				}
			}
			top.setDynamicValueSets("ListaProductosMix",l_mix);
			
			if(cot_id > 0){
				// Instacia del bizdelegate
				BizDelegate biz = new BizDelegate();
				//Recupera el encabezado de de la cotizacion
				CotizacionesDTO detcoti = biz.getCotizacionesById(cot_id);
				
				// TODO: Se debe solucionar para el caso cuando se llega desde la creacion de una nueva cotizacion
				
				//Si el estado de la cotizacion es distinto de ingresada = 55 se debe ir al monitor
				//Obs:  1.- Al llegar desde el monitor al p3 luego al resumen y hacer back esto funciona
				//		2.- Si se crea una nueva cotizacion y se sigue el flujo normal hasta llegar al resumen y hacer back aparece una pagina en blanco
				if(detcoti.getCot_estado_id() != Constantes.ID_EST_COTIZACION_INGRESADA){
					getServletConfig().getServletContext().getRequestDispatcher("/ViewSearchCotizaciones").forward(arg0, arg1);
					return;
				}
				
				top.setVariable("{cot_id}", detcoti.getCot_id()+"");
				top.setVariable("{nom_suc}", detcoti.getCot_nom_suc()+"");
				top.setVariable("{nom_emp}", detcoti.getCot_nom_emp());
				top.setVariable("{nom_com}", detcoti.getCot_nom_comp());
				top.setVariable("{dir_des}", detcoti.getCot_alias_dir()+" "+detcoti.getCot_calle_dir()+", "+detcoti.getCot_numero_dir()+", "+detcoti.getNomcomunades());
				top.setVariable("{dir_fac}", detcoti.getCot_dfac_calle()+" "+detcoti.getCot_dfac_numero()+", "+detcoti.getNomcomunafac());
				top.setVariable("{fecha_des}", Formatos.frmFecha(detcoti.getCot_fec_acordada()));
				
				String cot_num_mpago = "";
				if( detcoti.getCot_num_mpago() != null && !detcoti.getCot_num_mpago().equals("") && detcoti.getCot_num_mpago().length() > 0 ) {
					cot_num_mpago = Cifrador.desencriptar(rb.getString("conf.bo.key"),detcoti.getCot_num_mpago());
				}
				
				if( detcoti.getCot_mpago().equals("CAT") && cot_num_mpago.equals("1111111111111111") == false ){
					top.setVariable("{medio_pago}", "Jumbo Más" );
				}else if( detcoti.getCot_mpago().equals("TBK")){
					top.setVariable("{medio_pago}", "Tarjeta Bancaria" );
				}else if( detcoti.getCot_mpago().equals("CRE")) {
					top.setVariable("{medio_pago}", "Línea de Crédito" );
				} else if( detcoti.getCot_mpago().equals("CAT") && cot_num_mpago.equals("1111111111111111") == true ) {
					top.setVariable("{medio_pago}", "Cheque" );
				}
			
				top.setVariable("{comentario}", detcoti.getCot_obs()+"");
				
				
				//Recupera el listado de detalles de la cotizacio
				List lista = biz.getProductosDetCotiz(cot_id);
				List datos = new ArrayList();	
				double subtotalGen = 0;
				double descGen = 0;
				double cantTot = 0;
				double subtotalXdet;
				for (int i = 0; i < lista.size(); i++) {
					ProductosCotizacionesDTO proXcoti = (ProductosCotizacionesDTO) lista.get(i);  
					IValueSet fila = new ValueSet();
					fila.setVariable("{codigo}", proXcoti.getDetcot_proId() + "");
					fila.setVariable("{descrip}", proXcoti.getPro_tipo_producto() + " " + proXcoti.getMar_nombre() + " " + proXcoti.getDetcot_desc());
					fila.setVariable("{precio}", vteFormatos.formatoPrecio(Utils.redondear(proXcoti.getDetcot_precio())) + "");
					fila.setVariable("{cantidad}", proXcoti.getDetcot_cantidad() + "");
					subtotalXdet = Utils.redondear(proXcoti.getDetcot_precio() * proXcoti.getDetcot_cantidad());
					fila.setVariable("{subtotalXdet}", vteFormatos.formatoPrecio(subtotalXdet)+"");
					subtotalGen += subtotalXdet; //Suma los sobtotales por detalle
					descGen += proXcoti.getDetcot_dscto_item();//Suma los descuentos por detalle
					cantTot += proXcoti.getDetcot_cantidad();//Suma las cantidades por detalle
					
					datos.add(fila);				
				}
				top.setVariable("{subtotalGen}", vteFormatos.formatoPrecio(subtotalGen)+"");
				top.setVariable("{cantTot}", vteFormatos.formatoCantidad(cantTot)+"");
				top.setVariable("{cant_reg_prod}", lista.size()+"");
			
				top.setDynamicValueSets("ListaProductos", datos);
				
			}else{
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
			
			String result = tem.toString(top);

			out.print(result);

	}

}