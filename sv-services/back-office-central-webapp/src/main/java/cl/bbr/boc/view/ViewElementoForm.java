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
import cl.bbr.jumbocl.contenidos.dto.CampanasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ElementoDTO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.contenidos.dto.TipoElementoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el formulario con  datos de elemento
 * @author BBRI
 */
public class ViewElementoForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_elemento=0;
		
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
		if(req.getParameter("id_elemento")!=null)
			id_elemento = Long.parseLong(req.getParameter("id_elemento"));
		logger.debug("id_elemento:"+id_elemento);
		

		BizDelegate bizDelegate = new BizDelegate();
		
		ElementoDTO elem = bizDelegate.getElementoById(id_elemento);
		
		List lst_est = bizDelegate.getEstadosByVis("ALL","S");
		
		top.setVariable("{id_elemento}"		,String.valueOf(elem.getId_elemento()));
		top.setVariable("{elem_nom}"		,String.valueOf(elem.getNombre()));
		top.setVariable("{elem_des}"		,String.valueOf(elem.getDescripcion()));
		if( (elem.getFec_creacion()!=null) && !elem.getFec_creacion().equals("")){
			top.setVariable("{elem_fec}"	,Formatos.frmFechaHora(String.valueOf(elem.getFec_creacion())));
		}else{
			top.setVariable("{elem_fec}"	,Constantes.SIN_DATO);
		}
		
		top.setVariable("{elem_estado}"		,FormatoEstados.frmEstado(lst_est,elem.getEstado()));
		top.setVariable("{elem_url}"		,elem.getUrl_destino());
		
		logger.debug("url:"+elem.getUrl_destino());
		
		// se obtiene el local actual y el estado del usuario
		//long usrlocal = user.getId_local();
		String campEstado = elem.getEstado();
		long tipo_elem = elem.getId_tipo_elem();
		
		logger.debug("campEstado:"+campEstado);
		logger.debug("tipo_elem:"+tipo_elem);

		//tipos
		ArrayList tipos = new ArrayList();
		List listTipos = bizDelegate.getLstTipoElementos();

		for (int i = 0; i< listTipos.size(); i++){
			IValueSet fila_tip = new ValueSet();
			TipoElementoDTO tip1 = (TipoElementoDTO)listTipos.get(i);
			fila_tip.setVariable("{tip_id}", String.valueOf(tip1.getId_tipo()));
			fila_tip.setVariable("{tip_nom}"	, String.valueOf(tip1.getNombre()));
			
			if (tipo_elem==tip1.getId_tipo()){
				fila_tip.setVariable("{sel_tip}","selected");
			}
			else
				fila_tip.setVariable("{sel_tip}","");		
			tipos.add(fila_tip);
		}
		
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
		
		
		//campanas
		ArrayList campanas = new ArrayList();
		CampanasCriteriaDTO crit = new CampanasCriteriaDTO ();
		crit.setNombre("");
		crit.setNumero("");
		crit.setActivo('0');
		crit.setPag(1);
		crit.setRegsperpage(1000);
		
		List lst_elem = bizDelegate.getCampanasByCriteria(crit);//elem.getLst_elem();

		for (int i = 0; i < lst_elem.size(); i++) {
			IValueSet fila_camp = new ValueSet();
			CampanaDTO camp = (CampanaDTO) lst_elem.get(i);
			fila_camp.setVariable("{camp_id}", String.valueOf(camp.getId_campana()));
			fila_camp.setVariable("{camp_nom}", String.valueOf(camp.getNombre()));
			campanas.add(fila_camp);
		}
		if (lst_elem.size()==0){
			top.setVariable("{disabled_camp}"	,"disabled");			
		}
		else{
			top.setVariable("{disabled_camp}"	,"enabled");
		}

		top.setDynamicValueSets("CAMPANAS", campanas);
		top.setDynamicValueSets("TIPOS", tipos);
		top.setDynamicValueSets("ESTADOS", estados);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}