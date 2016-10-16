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
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * ViewEditarSector Comando Process
 * despliega el formulario que edita sectores 
 * @author bbr
 */

public class ViewEditarSector extends Command {

 

 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr)
         throws Exception {

	 long id_sector = 0L;
//	 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		if(req.getParameter("id_sector")==null){
			throw new ParametroObligatorioException("id de sector es null");
		}
		id_sector = Long.parseLong(req.getParameter("id_sector"));
		logger.debug("id_sector:"+id_sector);
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate biz = new BizDelegate();
		List listasector = new ArrayList();
		listasector = biz.getSectores();
		ArrayList datos = new ArrayList();
		for (int i = 0; i < listasector.size(); i++) {
			//IValueSet fila = new ValueSet();
			SectorLocalDTO sec1 = (SectorLocalDTO) listasector.get(i);				
			if(id_sector == sec1.getId_sector()){
				//top.setVariable("{nom_local}"	,usr.getLocal());
				top.setVariable("{id_sector}"	, String.valueOf(sec1.getId_sector()));
				top.setVariable("{sector}"		, sec1.getNombre());	
				top.setVariable("{max_prod}"	, String.valueOf(sec1.getMax_prod()));
				top.setVariable("{max_op}"		, String.valueOf(sec1.getMax_op()));
				top.setVariable("{min_op_fill}"	, String.valueOf(sec1.getMin_op_fill()));
				top.setVariable("{cant_min_prods}"	, String.valueOf(sec1.getCant_min_prods()));
			}
			
		}
		
		// 4.x genera Listados

		// 5. Setea variables del template

		// 6. Setea variables bloques
	
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
		

 }//execute

}
