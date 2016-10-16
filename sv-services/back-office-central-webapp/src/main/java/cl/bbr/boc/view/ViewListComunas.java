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
import cl.bbr.jumbocl.pedidos.dto.ComunaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega el listado de comunas
 * @author BBRI
 * 
 */
public class ViewListComunas extends Command {

	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		 	String 	paramUrl 	= "";	 	
		 	String 	mensaje 	= "";	 	

	     
		 	//	 1. Parámetros de inicialización servlet
			String html = getServletConfig().getInitParameter("TplFile");
			paramUrl = getServletConfig().getInitParameter("url");
			// le asignamos el prefijo con la ruta
			html = path_html + html;
			logger.debug("Template: " + html);		

			// 2. Procesa parámetros del request
			logger.debug("Procesando parámetros...");
			if(req.getParameter("mensaje")!=null){
				mensaje	= req.getParameter("mensaje");
				logger.debug("mensaje: "+mensaje);			
			}					
			// 3. Template (dejar tal cual)
			View salida = new View(res);

			TemplateLoader load = new TemplateLoader(html);

			ITemplate tem = load.getTemplate();
			IValueSet top = new ValueSet();

			// 4. Rutinas Dinámicas
			// 4.0 Creamos al BizDelegator	
			BizDelegate biz = new BizDelegate();
			
			// 4.1 Listado de Todas las Comunas 
			List listacomuna = new ArrayList();
			listacomuna = biz.getComunasAll();
			ArrayList comunas = new ArrayList();
			for(int i =0;i<listacomuna.size();i++){
				IValueSet fila = new ValueSet();			
				ComunaDTO comuna1 = (ComunaDTO) listacomuna.get(i);
					fila.setVariable("{id_comuna}"		,String.valueOf(comuna1.getId_comuna()));
					fila.setVariable("{nombre}"			,comuna1.getNombre());
					comunas.add(fila);
				}	
			
			// 5. Setea variables del template
				top.setVariable("{url}"		,paramUrl);
				if(!mensaje.equals("") || mensaje!=null){
					top.setVariable("{mensaje}"		,mensaje);
				}else{
					top.setVariable("{mensaje}"		,"");	
				}				
			// 6. Setea variables bloques
				top.setDynamicValueSets("LST_COMUNAS",comunas);				
			// 7. variables del header
				top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
				top.setVariable("{hdr_local}"	,usr.getLocal());
				Date now = new Date();
				top.setVariable("{hdr_fecha}"	,now.toString());	
					
				// 8. Salida Final
				String result = tem.toString(top);
				salida.setHtmlOut(result);
				salida.Output();
			

	 }//execute

	}
