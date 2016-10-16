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
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CampanaDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementoDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el formulario de con  datos de campania
 * @author BBRI
 */
public class ViewCampanaForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_campana=0;
		
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
		
		//parametro
		if(req.getParameter("id_campana")!=null)
			id_campana = Long.parseLong(req.getParameter("id_campana"));
		logger.debug("id_campana:"+id_campana);
		

		BizDelegate bizDelegate = new BizDelegate();
		
		CampanaDTO camp = bizDelegate.getCampanaById(id_campana);
		
		List lst_est = bizDelegate.getEstadosByVis("ALL","S");
		
		top.setVariable("{id_campana}"		,String.valueOf(camp.getId_campana()));
		top.setVariable("{camp_nom}"		,String.valueOf(camp.getNombre()));
		top.setVariable("{camp_des}"		,String.valueOf(camp.getDescripcion()));
		if( (camp.getFec_creacion()!=null) && !camp.getFec_creacion().equals("")){
			top.setVariable("{camp_fec}"		,Formatos.frmFechaHora(String.valueOf(camp.getFec_creacion())));
		}else{
			top.setVariable("{camp_fec}"		,Constantes.SIN_DATO);
		}
		
		top.setVariable("{camp_estado}"		,FormatoEstados.frmEstado(lst_est,camp.getEstado()));
		
		// se obtiene el local actual y el estado del usuario
		//long usrlocal = user.getId_local();
		String campEstado = camp.getEstado();
		//logger.debug("usrlocal:"+usrlocal);
		logger.debug("campEstado:"+campEstado);

		//estados
		ArrayList estados = new ArrayList();
		List listEst = bizDelegate.getEstadosByVis("ALL","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			
			if (campEstado.equals(String.valueOf(est1.getId_estado()))){
				fila_est.setVariable("{sel_est}","selected");
			}
			else
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
		if (lst_elem.size()==0){			
			top.setVariable("{disabled_eltos}"	,"disabled");
		}
		else{
			top.setVariable("{disabled_eltos}"	,"enabled");
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