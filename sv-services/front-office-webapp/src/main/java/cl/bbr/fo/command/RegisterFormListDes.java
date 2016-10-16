package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;


import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Recupera desde la sesión del usuario la lista de direcciones de despacho que tiene.
 * De este modo se pueden administrar sin ir al repositorio de datos.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class RegisterFormListDes extends Command {

	/**
	 * Presenta información de las direcciones de despacho.
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
		ResourceBundle rb = ResourceBundle.getBundle("fo");		
		
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		// Recupera parámetro desde web.xml
		String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");

		// Template de despliegue
		TemplateLoader load = new TemplateLoader(pag_form);
		ITemplate tem = load.getTemplate();

		IValueSet top = new ValueSet();

			
		
		// Recupera la sesión del usuario sólo si existe
		HttpSession session = arg0.getSession();

		List lista = (List) session.getAttribute("despachos");
		
		List datos = new ArrayList();

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				DireccionesDTO dir = (DireccionesDTO) lista.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{lista}", i + "");
				fila.setVariable("{dir_opcion}", dir.getAlias() + "");
				fila.setVariable("{dir_calle}", dir.getCalle() + "");
				fila.setVariable("{dir_numero}", dir.getNumero() + "");
				fila.setVariable("{dir_dpto}", dir.getDepto() + "");
				fila.setVariable("{dir_valor}", dir.getId() + "");
				datos.add(fila);
			}
			top.setDynamicValueSets("dir_despachos", datos);
		}

		String result = tem.toString(top);

		out.print(result);

	}

}