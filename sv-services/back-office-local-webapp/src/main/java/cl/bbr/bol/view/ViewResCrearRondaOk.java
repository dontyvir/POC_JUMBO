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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.RondaDetalleResumenDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class ViewResCrearRondaOk extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String	param_id_ronda = "";
		String  param_id_jornada ="";
		long id_ronda;
		long id_jornada;
		double suma=0.0;
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		
		if ( req.getParameter("id_ronda") == null ){ 
			throw new ParametroObligatorioException("id_ronda");
		}	
		param_id_ronda = req.getParameter("id_ronda");
		id_ronda = Long.parseLong(param_id_ronda);
		logger.debug("id_ronda = " + req.getParameter("id_ronda"));
		
		if ( req.getParameter("id_jornada") == null ){ 
			throw new ParametroObligatorioException("id_jornada");
		}	
		param_id_jornada = req.getParameter("id_jornada");
		id_jornada = Long.parseLong(param_id_jornada);
		logger.debug("id_jornada = " + req.getParameter("id_jornada"));
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator

		BizDelegate bizDelegate = new BizDelegate();
		
		List listasecciones = bizDelegate.getResumenRondaById(id_ronda);
		ArrayList seccion = new ArrayList();
		for (int i = 0; i < listasecciones.size(); i++) {
			IValueSet fila = new ValueSet();
			RondaDetalleResumenDTO resronda = new RondaDetalleResumenDTO();
			resronda = (RondaDetalleResumenDTO) listasecciones.get(i);
			fila.setVariable("{num_op}"	,  String.valueOf(resronda.getId_pedido()));
			fila.setVariable("{seccion}"	, resronda.getNom_sector());
			fila.setVariable("{cant_prod}"	, String.valueOf(resronda.getCant_asignada()));
			suma +=resronda.getCant_asignada();
			seccion.add(fila);
		}
		
		// 5. Setea variables del template
		top.setVariable("{id_ronda}", param_id_ronda);
		top.setVariable("{id_jornada}", param_id_jornada);
		top.setVariable("{mensaje}", "");
		top.setVariable("{total_prod}",suma+"");
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_ronda_picking", seccion);
		
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
