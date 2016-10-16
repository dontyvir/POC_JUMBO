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
import cl.bbr.jumbocl.common.model.ElementoEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CampanaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que contiene las los elementos de una campania
 * @author BBRI
 */
public class ViewCampElemIframe extends Command{
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int id_campana=0;
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
			
		if ( req.getParameter("id_campana") != null ){
			id_campana =  Integer.parseInt(req.getParameter("id_campana"));
		} 
		logger.debug("Este es el id de campana que viene" + id_campana);
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
		CampanaDTO camp = bizDelegate.getCampanaById(id_campana);
		List lst_elem = camp.getLst_elem();
		
		ArrayList datos = new ArrayList();
		for (int i = 0; i < lst_elem.size(); i++) {			
				IValueSet fila = new ValueSet();
				ElementoEntity elem = (ElementoEntity)lst_elem.get(i);
				fila.setVariable("{elem_id}"	, String.valueOf(elem.getId_elemento()));
				fila.setVariable("{elem_tip_id}"	,String.valueOf(elem.getId_tipo_elem()));	
				fila.setVariable("{elem_nombre}"	,String.valueOf(elem.getNombre()));
				fila.setVariable("{elem_desc}"	,String.valueOf(elem.getDescripcion()));
				if((elem.getFec_creacion()!=null) && !elem.getFec_creacion().equals("")){
					fila.setVariable("{elem_fec_crea}"	,Formatos.frmFechaHora(String.valueOf(elem.getFec_creacion())));
				}else{
					fila.setVariable("{elem_fec_crea}"	,Constantes.SIN_DATO);
				}
				
				fila.setVariable("{elem_url}"	,String.valueOf(elem.getUrl_destino()));
				fila.setVariable("{id_campana}"	,String.valueOf(id_campana));
				fila.setVariable("{elem_clicks}"	,String.valueOf(elem.getClicks()));
				
				fila.setVariable("{action}" ,"ModCampElemento");
				datos.add(fila);
			}		
			
			if (lst_elem.size() == 0){
				top.setVariable("{mensaje}", "No existen Elementos asociados" );
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
	    top.setDynamicValueSets("ELEMENTOS", datos);
	    		

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
