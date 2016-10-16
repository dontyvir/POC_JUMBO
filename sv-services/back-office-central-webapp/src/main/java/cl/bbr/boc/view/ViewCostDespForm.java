package cl.bbr.boc.view;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.clientes.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega el calendario de despacho para una zona determinada
 * @author jsepulveda
 */
public class ViewCostDespForm extends Command {
//probando
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		//String 	paramFecha 		= "";
		//String 	param_id_pedido	= "";
		String  msg				= "";
		long 	id_pedido		= -1;
		long	id_zona			= -1;
		double 	costo_despacho = 0;
		String rc = "";
		
		logger.debug("User: " + usr.getLogin());
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		logger.debug("Template: " + html);
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		// 2.0 parámetro msg
		if ( req.getParameter("msg") != null )
			msg = req.getParameter("msg");
		if (req.getParameter("rc") != null ) 
			rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		
		// 2.1 parámetro id_pedido
		if ( req.getParameter("id_pedido") == null ){
			throw new ParametroObligatorioException("id_pedido es null");
		}
		
		id_pedido = Long.parseLong(req.getParameter("id_pedido"));
		logger.debug("id_pedido: " + id_pedido);
		
		
		logger.debug("Parámetros procesados");

		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
	
		
		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();

		// 4.1 Obtenemos información del pedido
		PedidoDTO pedido = new PedidoDTO();
		pedido = bizDelegate.getPedidosById(id_pedido); 
		
		//obtener el costo despacho del pedido
		costo_despacho = pedido.getCosto_despacho();
		long cant_prods = pedido.getCant_prods();
		id_zona = pedido.getId_zona();
		
		Date fecha = new Date();		
		
		//Formateo de fecha y calendario
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		logger.debug("cal1: " + cal1.toString());
		
		cal1.setTime(fecha);
		cal1.getTime();
		
		int year = cal1.get(Calendar.YEAR);
		int woy	 = cal1.get(Calendar.WEEK_OF_YEAR);
		
		logger.debug("Week of year: " + woy);
		logger.debug("Year: " + year);
		
		ZonaDTO zonadto = bizDelegate.getZonaById(id_zona);
	
		
		//	4.2 Listado de jornadas (horario)
		CalendarioDespachoDTO cal = new CalendarioDespachoDTO();
		cal = bizDelegate.getCalendarioDespacho(woy,year,id_zona);

		
		// Obtenemos los objetos
		SemanasEntity sem = new SemanasEntity();
		sem = cal.getSemana();
		
		
		top.setVariable("{f_ini}"			,sem.getF_ini().toString());
		top.setVariable("{f_fin}"			,sem.getF_fin().toString());
		top.setVariable("{ano}"				,String.valueOf(sem.getAno()));
		top.setVariable("{id_semana}"		,String.valueOf(sem.getId_semana()));
		top.setVariable("{n_semana}"		,String.valueOf(sem.getN_semana()));
		top.setVariable("{fecha}"			,pedido.getFdespacho());
		top.setVariable("{capac_picking}"	,String.valueOf(cant_prods));
		top.setVariable("{id_pedido}"		,String.valueOf(id_pedido));
		top.setVariable("{zona}"			,String.valueOf(id_zona));
		top.setVariable("{msg}"				,msg);
		
		// Setea variables del header de la tabla (fechas)

		top.setVariable("{id_zona}"		,String.valueOf(id_zona));
		top.setVariable("{nom_zona}", zonadto.getNombre());
		
		top.setVariable( "{mns}"	, "");
		if(costo_despacho!=0)
			top.setVariable("{precio}",Math.round(costo_despacho)+"");
		else
			top.setVariable("{precio}","");

		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

	
	
}