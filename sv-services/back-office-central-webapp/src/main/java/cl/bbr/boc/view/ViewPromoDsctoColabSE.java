//Cargar Template de Secciones Excluidas

package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.dto.BOSeccionDTO;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author Cenco
 * @version 1
 */
public class ViewPromoDsctoColabSE extends Command {

	private static final long serialVersionUID = 1L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res,
			UserDTO usr) throws Exception {

		View salida = new View(res);
		int regsperpage = 10;

		logger.debug("User: " + usr.getLogin());

		// 1. Parámetros de inicialización servlet
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter(
				"RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);

		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		top.setVariable("{hdr_nombre}",
				usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());

		BizDelegate biz = new BizDelegate();

		List seccMarcExc = new ArrayList();
		seccMarcExc = biz.getSecciones();

		ArrayList binM = new ArrayList();
		for (int i = 0; i < seccMarcExc.size(); i++) {
			BOSeccionDTO bin1 = new BOSeccionDTO();
			bin1 = (BOSeccionDTO) seccMarcExc.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{mrc_id}", String.valueOf(bin1.getId_seccion()));
			fila.setVariable("{mrc_nombre}", bin1.getNombre());
			binM.add(fila);
		}

		List seccPromExc = new ArrayList();
		seccPromExc = biz.getSeccionesExcluidas();

		ArrayList binP = new ArrayList();
		for (int i = 0; i < seccPromExc.size(); i++) {
			BOSeccionDTO bin1 = new BOSeccionDTO();
			bin1 = (BOSeccionDTO) seccPromExc.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{id_seccion}",
					String.valueOf(bin1.getId_seccion()));
			fila.setVariable("{nombre}", bin1.getNombre());
			binP.add(fila);
		}

		top.setDynamicValueSets("MARCAS", binM);
		top.setDynamicValueSets("PROMOS", binP);

		String result = tem.toString(top);

		salida.setHtmlOut(result);
		salida.Output();

	}
}
