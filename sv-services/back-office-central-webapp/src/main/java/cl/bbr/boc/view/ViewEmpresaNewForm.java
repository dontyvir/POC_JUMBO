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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el formulario que permite crear una nueva empresa
 * 
 * @author BBRI
 */
public class ViewEmpresaNewForm extends Command {
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
		List listEst = bizDelegate.getEstadosByVis("EMP","S");

		for (int i = 0; i< listEst.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEst.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			fila_est.setVariable("{sel_est}","");		
			estados.add(fila_est);
		}
		
		
//		 Verificamos si el usuario conectado es supervisor venta empresa
		List perfiles = usr.getPerfiles();
		long perfil =0;
		boolean esAdmVE = false; 
		for (int p=0; p < perfiles.size();p++){
			PerfilesEntity per =	(PerfilesEntity)perfiles.get(p);
			
			perfil = per.getIdPerfil().longValue();
			logger.debug("Perfil: " + perfil);
			if (perfil == Constantes.ID_PERFIL_SUPERVISOR_VE){
				esAdmVE = true;
				break;
			}
		}
		top.setVariable("{margen_inf}", Constantes.VE_EMPRESAS_MARGEN_MIN_RANGO_INF);
		top.setVariable("{margen_sup}", Constantes.VE_EMPRESAS_MARGEN_MIN_RANGO_SUP);
		top.setVariable("{descuento_inf}", Constantes.VE_EMPRESAS_DESCUENTO_MAX_RANGO_INF);
		top.setVariable("{descuento_sup}", Constantes.VE_EMPRESAS_DESCUENTO_MAX_RANGO_SUP);
		if (esAdmVE){
			top.setVariable("{sup_ve}", "1");
			top.setVariable("{rango_margen}", "");
			top.setVariable("{rango_descuento}","");
		}else{
			top.setVariable("{sup_ve}", "0");
			top.setVariable("{rango_margen}", "(Entre: "+Constantes.VE_EMPRESAS_MARGEN_MIN_RANGO_INF+"% - "+Constantes.VE_EMPRESAS_MARGEN_MIN_RANGO_SUP+"%)");
			top.setVariable("{rango_descuento}",  "(Entre: "+Constantes.VE_EMPRESAS_DESCUENTO_MAX_RANGO_INF+"% - "+Constantes.VE_EMPRESAS_DESCUENTO_MAX_RANGO_SUP+"%)");
		}
		
		top.setDynamicValueSets("ESTADOS", estados);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		top.setVariable("{fecha}"	,Formatos.getFechaActual());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}