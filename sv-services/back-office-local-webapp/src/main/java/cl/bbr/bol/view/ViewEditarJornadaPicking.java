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
import cl.bbr.jumbocl.common.model.HorarioPickingEntity;
import cl.bbr.jumbocl.common.model.JornadaPickingEntity;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega formulario para edición de las jornadas de picking de un horario semanal
 * @author jsepulveda
 */
public class ViewEditarJornadaPicking extends Command{

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String	param_id_ronda = "";

		// 1. Variables del método
		String paramUrl			= "";
		String paramId_hor_pick = "";
		String paramFecha		= "";
		String rc 				= "";
		String mns_rc			= "";
		
		// 2. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
	
		// 3. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		
		// 3.1 Parámetro id_jor_pick (Obligatorio)
		if ( req.getParameter("id_hor_pick") == null){
			throw new ParametroObligatorioException("id_hor_pick es null");			
		}
		paramId_hor_pick = req.getParameter("id_hor_pick");
		logger.debug("id_hor_pick: " + paramId_hor_pick);
		
		if ( req.getParameter("fecha") == null){
			paramFecha = "";			
		}
		paramFecha = req.getParameter("fecha");
		logger.debug("paramFecha: " + paramFecha);
		
		// si falla el parseLong debiese levantar una excepción
		long id_hor_pick = Long.parseLong(paramId_hor_pick);
	
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		if (req.getParameter("mns_rc") != null ) mns_rc = req.getParameter("mns_rc");
		logger.debug("mns_rc:"+mns_rc);

		// 4. Template (dejar tal cual)
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
	
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
	
		// 5. Rutinas Dinámicas
		// 5.0 Creamos al BizDelegator
		BizDelegate biz = new BizDelegate();
		
		HorarioPickingEntity hor = new HorarioPickingEntity();
		hor = biz.getHorarioPicking(id_hor_pick);
		
		
		List jornadas = new ArrayList();
		jornadas = biz.getJornadasByIdHorario(id_hor_pick);
		
		// 5.1 Iteramos las jornadas
		for(int i=0; i<jornadas.size(); i++){
			JornadaPickingEntity jor = new JornadaPickingEntity();
			jor = (JornadaPickingEntity)jornadas.get(i);
			
			switch(jor.getDay_of_week()){
			case 1:
				top.setVariable("{f_lu}"	, jor.getFecha().toString());
				top.setVariable("{capac_lu}", String.valueOf(jor.getCapac_picking()));
				top.setVariable("{hras_val_lu}", String.valueOf(jor.getHrs_validacion()));
				top.setVariable("{hras_web_lu}", String.valueOf(jor.getHrs_ofrecido_web()));
			case 2:
				top.setVariable("{f_ma}"	, jor.getFecha().toString());
				top.setVariable("{capac_ma}", String.valueOf(jor.getCapac_picking()));
				top.setVariable("{hras_val_ma}", String.valueOf(jor.getHrs_validacion()));
				top.setVariable("{hras_web_ma}", String.valueOf(jor.getHrs_ofrecido_web()));
			case 3:
				top.setVariable("{f_mi}"	, jor.getFecha().toString());
				top.setVariable("{capac_mi}", String.valueOf(jor.getCapac_picking()));
				top.setVariable("{hras_val_mi}", String.valueOf(jor.getHrs_validacion()));
				top.setVariable("{hras_web_mi}", String.valueOf(jor.getHrs_ofrecido_web()));
			case 4:
				top.setVariable("{f_ju}"	, jor.getFecha().toString());
				top.setVariable("{capac_ju}", String.valueOf(jor.getCapac_picking()));
				top.setVariable("{hras_val_ju}", String.valueOf(jor.getHrs_validacion()));
				top.setVariable("{hras_web_ju}", String.valueOf(jor.getHrs_ofrecido_web()));
			case 5:
				top.setVariable("{f_vi}"	, jor.getFecha().toString());
				top.setVariable("{capac_vi}", String.valueOf(jor.getCapac_picking()));
				top.setVariable("{hras_val_vi}", String.valueOf(jor.getHrs_validacion()));
				top.setVariable("{hras_web_vi}", String.valueOf(jor.getHrs_ofrecido_web()));
			case 6:
				top.setVariable("{f_sa}"	, jor.getFecha().toString());
				top.setVariable("{capac_sa}", String.valueOf(jor.getCapac_picking()));
				top.setVariable("{hras_val_sa}", String.valueOf(jor.getHrs_validacion()));
				top.setVariable("{hras_web_sa}", String.valueOf(jor.getHrs_ofrecido_web()));
			case 7:
				top.setVariable("{f_do}"	, jor.getFecha().toString());
				top.setVariable("{capac_do}", String.valueOf(jor.getCapac_picking()));
				top.setVariable("{hras_val_do}", String.valueOf(jor.getHrs_validacion()));
				top.setVariable("{hras_web_do}", String.valueOf(jor.getHrs_ofrecido_web()));
				
			}
			
			
		}
		
		
		// 6. Setea variables del template
		top.setVariable("{id_hor_pick}", paramId_hor_pick);
		top.setVariable("{h_ini}", hor.getH_ini());
		top.setVariable("{h_fin}", hor.getH_fin());
		top.setVariable("{fecha_param}" ,paramFecha);
		
		// 7. Setea variables bloques
	
		// 8. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		logger.debug(" **** rc:"+rc);
		if ( !rc.equals("") ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('"+mns_rc+"');</script>" );
		}else {
			top.setVariable( "{mns}", "");
		}
		
		
		// 9. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
