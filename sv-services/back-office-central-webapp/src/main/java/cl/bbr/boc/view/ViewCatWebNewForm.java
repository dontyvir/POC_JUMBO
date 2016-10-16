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
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
	/**
	 * formulario de ingreso de datos para una nueva categoria
	 * @author BBRI
	 */
	public class ViewCatWebNewForm extends Command{
		
		protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
			View salida = new View(res);
			
			logger.debug("User: " + usr.getLogin());
					
			// Recupera pagina desde web.xml
			String html = getServletConfig().getInitParameter("TplFile");
			// le aasignamos el prefijo con la ruta
			html = path_html + html;
			logger.debug("Template: " + html);
			
			//2. Procesa parámetros del request
			
//			3. Template	
			TemplateLoader load = new TemplateLoader(html);
			ITemplate tem = load.getTemplate();
			IValueSet top = new ValueSet();
			
			

			// 4.  Rutinas Dinámicas 
			// 4.0 Bizdelegator 
			
			BizDelegate bizDelegate = new BizDelegate();
			
			String cat_tipo = "";
			String cat_estado = "";
			
//			 4.1 Listado de Categorias
						
			//ver tipos de categorias
			ArrayList tipos = new ArrayList();
			List listTipos = bizDelegate.getTiposCategorias();
			logger.debug("tipos de categorias");
			for (int i = 0; i< listTipos.size(); i++){
				IValueSet fila_tip = new ValueSet();
				EstadoDTO tip1 = (EstadoDTO)listTipos.get(i);
				fila_tip.setVariable("{id_tipo}", String.valueOf(tip1.getId_estado()));
				fila_tip.setVariable("{nom_tipo}"	, String.valueOf(tip1.getNombre()));
				
				if (cat_tipo.equals(String.valueOf(tip1.getId_estado()))){
					fila_tip.setVariable("{sel_tip}","selected");
				}
				else
					fila_tip.setVariable("{sel_tip}","");		
				tipos.add(fila_tip);
			}
			logger.debug("size:"+listTipos.size());

			
//			ver estados
			ArrayList estados = new ArrayList();
			List listEstados = bizDelegate.getEstadosByVis("CW","S");
			logger.debug("listEstados");
			
			for (int i = 0; i< listEstados.size(); i++){
				IValueSet fila_est = new ValueSet();
				EstadoDTO est1 = (EstadoDTO)listEstados.get(i);
				fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
				fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
				
				if (cat_estado.equals(String.valueOf(est1.getId_estado()))){
					fila_est.setVariable("{sel_est}","selected");
				}
				else
					fila_est.setVariable("{sel_est}","");		
				estados.add(fila_est);
			}
			logger.debug("size:"+listEstados.size());
						
			
			
			//		 5 Paginador		
			
			// 6. Setea variables bloques	
		    top.setDynamicValueSets("TIPO", tipos);
		    top.setDynamicValueSets("ESTADO", estados);
		    
			top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
			Date now = new Date();
			top.setVariable("{hdr_fecha}"	,now.toString());			    

		    String result = tem.toString(top);
		 //   res.sendRedirect(url);
			salida.setHtmlOut(result);
			salida.Output();	
		}

}
