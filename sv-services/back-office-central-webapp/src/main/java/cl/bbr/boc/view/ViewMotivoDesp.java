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
import cl.bbr.jumbocl.contenidos.dto.MotivosDespDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega formulario para el motivo de despublicación del despacho
 * @author BBRI
 */
public class ViewMotivoDesp extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String mensaje_retorno="";
		long id_producto = 0;
		String generico="";
		
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("id_producto") != null ){id_producto =  Long.parseLong(req.getParameter("id_producto"));}
		logger.debug("Este es el id_producto que viene:" + id_producto);
		
	//	if ( req.getParameter("mensaje") != null ){mensaje_retorno = req.getParameter("mensaje");}
		logger.debug("mensaje_retorno:" + mensaje_retorno );
		
		if ( req.getParameter("tipo_prod") != null ){generico= req.getParameter("tipo_prod");}
		logger.debug("tipo_prod:" + generico);
		
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();	
		
		/*ProductoLogDTO log1 = new ProductoLogDTO();
		log1.setCod_prod(id_producto);
		log1.setFec_crea(Formato.getFechaActual());
		log1.setUsuario(usr.getLogin());
		log1.setTexto("El motivo de Despublicación es: "+ motivo);
		
		bizDelegate.setLogProduct(log1);*/
		
		
		//4.1 Listar Motivos
		List motivos_list = new ArrayList();
		motivos_list = bizDelegate.getMotivosDesp();
		ArrayList motivos = new ArrayList();		
		for (int i = 0; i < motivos_list.size(); i++) {			
			IValueSet fila = new ValueSet();	
			MotivosDespDTO mot = new MotivosDespDTO();
			mot = (MotivosDespDTO)motivos_list.get(i);
			fila.setVariable("{id_desp}", String.valueOf(mot.getId()));
			fila.setVariable("{motivo}"	, mot.getMotivo());
			logger.debug("id:"+String.valueOf(mot.getId())+" motivo:"+mot.getMotivo());
			motivos.add(fila);
		}	
		
		// 5. variables globales
		top.setVariable("{id_producto}", String.valueOf(id_producto));
		top.setVariable("{mensaje}", mensaje_retorno);
		top.setVariable("{gen}", generico);
		//top.setVariable("{motivo}", motivo);
		
		// 6. listas dinamicas
		top.setDynamicValueSets("listado_motivos_desp" , motivos);	
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}