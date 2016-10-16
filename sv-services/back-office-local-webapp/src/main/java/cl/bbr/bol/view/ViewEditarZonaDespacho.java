package cl.bbr.bol.view;

import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra Formulario para editar una zona de despacho
 * @author mleiva
 */

public class ViewEditarZonaDespacho extends Command {

 

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
	 	long	id_zona			=-1;
	 	String 	param_id_zona 	= "";

		ResourceBundle rb = ResourceBundle.getBundle("bo");
		String descuento = rb.getString("monto.descuento.despacho");
     
	 	// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		if(req.getParameter("id_zona")!=null){
			param_id_zona	= req.getParameter("id_zona");
			logger.debug("param_id_zona: "+param_id_zona);
			id_zona			= Long.parseLong(param_id_zona);
		}
			
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		// 4.1 genera el DTO de la Salida
		ZonaDTO zona1 = new ZonaDTO(); 
		zona1 = bizDelegate.getZonaDespacho(id_zona);
		
		// 5. Setea variables del template
		top.setVariable("{id_zona}"		,String.valueOf(zona1.getId_zona()));
		top.setVariable("{nombre}"		,zona1.getNombre());
		top.setVariable("{desc}"		,zona1.getDescripcion());
		top.setVariable("{tarifa_normal_alta}"  , String.valueOf(zona1.getTarifa_normal_alta()));
		top.setVariable("{tarifa_normal_media}" , String.valueOf(zona1.getTarifa_normal_media()));
		top.setVariable("{tarifa_normal_baja}"  , String.valueOf(zona1.getTarifa_normal_baja()));
		top.setVariable("{tarifa_economica}"    , String.valueOf(zona1.getTarifa_economica()));
		top.setVariable("{tarifa_express}"      , String.valueOf(zona1.getTarifa_express()));
		top.setVariable("{msg_cal_despacho}"    , zona1.getMensaje_cal_despacho());
		
		//Estados ==> 1: Activa, 0: Inactiva
		if (zona1.getEstado_tarifa_economica() > 0){
			top.setVariable("{estado_tarifa_economica}", "checked");
		}else{
			top.setVariable("{estado_tarifa_economica}", "");
		}
		
		//Estados ==> 1: Activa, 0: Inactiva
		if (zona1.getEstado_tarifa_express() > 0){
			top.setVariable("{estado_tarifa_express}", "checked");
		}else{
			top.setVariable("{estado_tarifa_express}", "");
		}
		
		// Regalos Clientes ==> 1: Activo, 0: Inactivo
		if (zona1.getRegalo_clientes() > 0){
			top.setVariable("{regalo_clientes}", "checked");
		}else{
			top.setVariable("{regalo_clientes}", "");
		}
		top.setVariable("{orden}", String.valueOf(zona1.getOrden()));
		
		// jean:Estado Descuento CAT ==> 01: siempre, 10: primera compra, 11: ambos
		top.setVariable("{estado_descuento_cat}", (zona1.getEstado_descuento_cat() & 1) == 1 ? "checked" : "");
		top.setVariable("{estado_descuento_pc_cat}", (zona1.getEstado_descuento_cat() & 2) == 2 ? "checked" : "");
		
		// jean:Estado Descuento TBK ==> 01: siempre, 10: primera compra, 11: ambos
		top.setVariable("{estado_descuento_tbk}", (zona1.getEstado_descuento_tbk() & 1) == 1 ? "checked" : "");
		top.setVariable("{estado_descuento_pc_tbk}", (zona1.getEstado_descuento_tbk() & 2) == 2 ? "checked" : "");
		
		// jean:Estado Descuento PAR ==> 01: siempre, 10: primera compra, 11: ambos
		top.setVariable("{estado_descuento_par}", (zona1.getEstado_descuento_par() & 1) == 1 ? "checked" : "");
		top.setVariable("{estado_descuento_pc_par}", (zona1.getEstado_descuento_par() & 2) == 2 ? "checked" : "");

		top.setVariable("{monto_descuento_cat}", zona1.getMonto_descuento_cat() + "");
		top.setVariable("{monto_descuento_tbk}", zona1.getMonto_descuento_tbk() + "");
		top.setVariable("{monto_descuento_par}", zona1.getMonto_descuento_par() + "");
		
		top.setVariable("{monto_descuento_pc_cat}", zona1.getMonto_descuento_pc_cat() + "");
		top.setVariable("{monto_descuento_pc_tbk}", zona1.getMonto_descuento_pc_tbk() + "");
		top.setVariable("{monto_descuento_pc_par}", zona1.getMonto_descuento_pc_par() + "");
		
		
		// 6. Setea variables bloques

		// 7. variables del header
		top.setVariable("{hdr_nombre}",usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}" ,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}",now.toString());
		top.setVariable("{MontoMinimo}", descuento);
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
    }//execute

}
