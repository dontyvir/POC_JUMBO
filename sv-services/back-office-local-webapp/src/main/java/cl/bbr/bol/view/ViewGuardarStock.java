package cl.bbr.bol.view;

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.IValueSet;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewGuardarStock extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String param_fecha       = "";
		String fecha             = "";
		Date   hoy               = new Date();
		String anterior          = "";
		String siguiente         = "";
		String fecha_jor         = "";
		String direccion         = "";
		String detalleLog 		 = new String();
		
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		String MsjeError = getServletConfig().getInitParameter("MsjeError");
		logger.debug("MsjeError: " + MsjeError);
		
		// 2. Procesa parámetros del request
		logger.debug("req fecha: "+req.getParameter("fecha"));
		if ( req.getParameter("fecha") != null ){
			param_fecha = req.getParameter("fecha");
			logger.debug("param_fecha: " + param_fecha);
			//fecha = new SimpleDateFormat("yyyy-MM-dd").parse(param_fecha);
			logger.debug("Fecha: "+param_fecha);
		}
		
		logger.debug("param_fecha: "+param_fecha);
		
		// 3. Template
		View salida = new View(res);
		
		//TemplateLoader load = new TemplateLoader(html);
		//ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		
		BizDelegate bizDelegate = new BizDelegate();
		
		// Guardar el Stock
		for(Enumeration e = req.getParameterNames(); e.hasMoreElements(); ){
			String element = (String)e.nextElement();
			String cod_bolsa = new String();
			String cod_sucursal = new String();
			int stock = 0;
			if( element.indexOf("stock_") != -1){
				String[] bolsa = element.split("_");
				cod_bolsa = bolsa[1];
				cod_sucursal = bolsa[2];
				stock = Integer.parseInt(req.getParameter(element));
				detalleLog += "- Cod Bolsa:" + cod_bolsa + "; Stock:"+stock;
				
				//Actualizar stock
				bizDelegate.actualizarStockBolsa(cod_bolsa, cod_sucursal, stock);
			}
		}
			
		//insertar log bolsas
		bizDelegate.insertarRegistroBitacoraBolsas("Actualización stock bolsas sucursal " 
				+ usr.getLocal() + ", detalle:" + detalleLog, 
				usr.getLogin(), usr.getId_local()+"");
		
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		
		// 8. Salida Final
		//String result = tem.toString(top);
		//salida.setHtmlOut(result);
		//salida.Output();
		
		String paramUrl = "ViewMonitorBolsas?id_sucursal="+usr.getId_local()+"&respuesta_ok=Se actualizó stock exitosamente";
		
		res.sendRedirect(paramUrl);
	}
}
