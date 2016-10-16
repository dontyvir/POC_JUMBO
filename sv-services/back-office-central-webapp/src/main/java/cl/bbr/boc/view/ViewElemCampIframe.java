package cl.bbr.boc.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.CampanaEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.ElementoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que contiene las campanas relacionados a un elemento
 * @author BBRI
 */
public class ViewElemCampIframe extends Command{
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int id_elemento=0;
		String mns = "";
		String rc = "";

		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("id_elemento") != null ){
			id_elemento =  Integer.parseInt(req.getParameter("id_elemento"));
		} 
		logger.debug("Este es el id_elemento que viene" + id_elemento);
		if ( req.getParameter("mns") != null ){
			mns = "<script language='JavaScript'>alert('"+req.getParameter("mns")+"');</script>";
		}
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		logger.debug("mns:"+mns);
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();	
		IValueSet top = new ValueSet();
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();	
		ElementoDTO elem = bizDelegate.getElementoById(id_elemento);
		List lst_camp = elem.getLst_campanas();
		
		ArrayList datos = new ArrayList();
		for (int i = 0; i < lst_camp.size(); i++) {			
				IValueSet fila = new ValueSet();
				CampanaEntity camp = (CampanaEntity)lst_camp.get(i);
				fila.setVariable("{camp_id}"	, String.valueOf(camp.getId_campana()));
				fila.setVariable("{camp_nombre}"	,String.valueOf(camp.getNombre()));
				fila.setVariable("{camp_desc}"	,String.valueOf(camp.getDescripcion()));
				if((camp.getFec_creacion()!=null) && !camp.getFec_creacion().equals("")){
					fila.setVariable("{camp_fec_crea}"	,Formatos.frmFechaHora(String.valueOf(camp.getFec_creacion())));
				}else{
					fila.setVariable("{camp_fec_crea}"	,Constantes.SIN_DATO);
				}
				
				fila.setVariable("{id_elemento}"	,String.valueOf(id_elemento));
				
				fila.setVariable("{action}" ,"ModCampElemento");
				datos.add(fila);
			}		
			
			if (lst_camp.size() == 0){
				top.setVariable("{mensaje}", "No existen Campañas asociadas" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
			
	
		//		 5 Paginador
			
		// 6. Setea variables bloques
	    
	    top.setVariable("{mns}", mns );
		if ( rc.equals(Constantes._EX_CAM_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de la campaña no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_ELM_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de elemento ingresado no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAM_ELEM_REL_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La relacion entre campaña y elemento ya existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAM_ELEM_REL_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La relación entre campaña y elemento no existen.No se puede eliminar.');</script>" );
		}
	    top.setDynamicValueSets("CAMPANAS", datos);
	    		

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
