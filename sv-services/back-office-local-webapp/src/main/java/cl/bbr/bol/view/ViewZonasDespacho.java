package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * ViewZonasDespacho Comando View
 * despliega las zonas de despacho
 * @author bbri
 * 
 */
public class ViewZonasDespacho extends Command {

 
 
  protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr)
         throws Exception {
     
	 	//	 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		

		String MsjeError = getServletConfig().getInitParameter("MsjeError");
		logger.debug("MsjeError: " + MsjeError);
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		// 4.1 genera Listados
		List listazonadesp = new ArrayList();
		listazonadesp = bizDelegate.getZonasLocal(usr.getId_local());
		logger.debug("listazonadesp: "+listazonadesp.size()); 
		ArrayList zonaDesp = new ArrayList();
		for(int i =0;i<listazonadesp.size();i++){
			IValueSet fila = new ValueSet();
			ZonaDTO zona1 = (ZonaDTO)listazonadesp.get(i);
			fila.setVariable("{id_zona}"            , String.valueOf(zona1.getId_zona()));
			fila.setVariable("{nombre}"             , zona1.getNombre());
			fila.setVariable("{descripcion}"        , zona1.getDescripcion());
			fila.setVariable("{tarifa_normal_alta}" , String.valueOf(zona1.getTarifa_normal_alta()));
			fila.setVariable("{tarifa_normal_media}", String.valueOf(zona1.getTarifa_normal_media()));
			fila.setVariable("{tarifa_normal_baja}" , String.valueOf(zona1.getTarifa_normal_baja()));
			fila.setVariable("{tarifa_economica}"   , String.valueOf(zona1.getTarifa_economica()));
			fila.setVariable("{tarifa_express}"     , String.valueOf(zona1.getTarifa_express()));
			fila.setVariable("{estado_tarifa_economica}", String.valueOf(zona1.getEstado_tarifa_economica()));
			fila.setVariable("{estado_tarifa_express}"  , String.valueOf(zona1.getEstado_tarifa_express()));
			fila.setVariable("{orden}"		,String.valueOf(zona1.getOrden()));
			
			zonaDesp.add(fila);
		}
		// 5. Setea variables del template
		if (listazonadesp.size()<=0)			
		{
			logger.debug("dentro del IF");
			top.setVariable("{mensaje}",MsjeError);
		}
		else
		{
			logger.debug("dentro del ELSE");
			top.setVariable("{mensaje}","");
		}

		// 6. Setea variables bloques
			top.setDynamicValueSets("select_zona_desp",zonaDesp);
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
			
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
		

 }

}
