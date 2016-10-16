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
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.contenidos.dto.ElementoDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el formulario de con  datos de campania
 * @author BBRI
 */
public class ViewCampanaNewForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		

		BizDelegate bizDelegate = new BizDelegate();
		
		
		//estados
		ArrayList estados = new ArrayList();
		List listEst = bizDelegate.getEstadosByVis("ALL","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			fila_est.setVariable("{sel_est}","");		
			estados.add(fila_est);
		}
		
		
		//elementos
		ArrayList elementos = new ArrayList();
		ElementosCriteriaDTO crit = new ElementosCriteriaDTO ();
		crit.setNombre("");
		crit.setNumero("");
		crit.setActivo('0');
		crit.setPag(1);
		crit.setRegsperpage(1000);
		
		List lst_elem = bizDelegate.getElementosByCriteria(crit);//camp.getLst_elem();

		for (int i = 0; i < lst_elem.size(); i++) {
			IValueSet fila_elem = new ValueSet();
			ElementoDTO elem = (ElementoDTO) lst_elem.get(i);
			fila_elem.setVariable("{elem_id}", String.valueOf(elem.getId_elemento()));
			fila_elem.setVariable("{elem_nom}", String.valueOf(elem.getNombre()));
			elementos.add(fila_elem);
		}

		top.setDynamicValueSets("ELEMENTOS", elementos);
		top.setDynamicValueSets("ESTADOS", estados);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}