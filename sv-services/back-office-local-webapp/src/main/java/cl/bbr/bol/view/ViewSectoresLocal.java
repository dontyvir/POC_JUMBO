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
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * ViewSectoresLocal Comando Process
 * despliega los sectores del local
 * @author bbri 
 */

public class ViewSectoresLocal extends Command {

 

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
		List listasector = new ArrayList();
		listasector = bizDelegate.getSectores();
		ArrayList datos = new ArrayList();
		for (int i = 0; i < listasector.size(); i++) {
			IValueSet fila = new ValueSet();
			SectorLocalDTO sec1 = (SectorLocalDTO) listasector.get(i);
			fila.setVariable("{id_sector}"		, String.valueOf(sec1.getId_sector()));
			fila.setVariable("{descripcion}"	, sec1.getNombre());
			fila.setVariable("{max_prod}"	, String.valueOf(sec1.getMax_prod()));
			fila.setVariable("{max_op}"	, String.valueOf(sec1.getMax_op()));
			fila.setVariable("{min_op_fill}"	, String.valueOf(sec1.getMin_op_fill()));
			fila.setVariable("{cant_min_prods}"	, String.valueOf(sec1.getCant_min_prods()));
			
			datos.add(fila);
		}
		// 5. Setea variables del template
		if (listasector.size()<=0)
		{
			top.setVariable("{mensaje}",MsjeError);
		}
		else
		{
			top.setVariable("{mensaje}","");
		}

		// 6. Setea variables bloques
		top.setDynamicValueSets("select_adm_secciones", datos);
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
		

 }//execute

}
