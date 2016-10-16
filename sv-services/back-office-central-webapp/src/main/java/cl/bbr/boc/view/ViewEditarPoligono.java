package cl.bbr.boc.view;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega un formulario que permite editar una comuna
 * @author BBRI
 */
public class ViewEditarPoligono extends Command {

	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		 String Url = "";
		 String mensaje  = "";
		 long   Id_Poligono = 0L;
		 
	     
		 //	 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		Url = getServletConfig().getInitParameter("url");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		

		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		if(req.getParameter("mensaje")!=null){
			mensaje	= req.getParameter("mensaje");
			logger.debug("mensaje: "+mensaje);			
		}
		if ( req.getParameter("id_poligono") == null ){throw new ParametroObligatorioException("id_poligono es null");}
		
		Id_Poligono = Long.parseLong(req.getParameter("id_poligono"));

		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator	
		BizDelegate biz = new BizDelegate();
			
		
	
		// 4.1 Detalle de un Poligono
		PoligonoDTO pol = biz.getPoligonoById(Id_Poligono);
		top.setVariable("{id_poligono}", String.valueOf(pol.getId_poligono()));
		top.setVariable("{id_comuna}", String.valueOf(pol.getId_comuna()));
		top.setVariable("{num_poligono}", String.valueOf(pol.getNum_poligono()));
		top.setVariable("{desc_poligono}", String.valueOf(pol.getDescripcion()));
		
		//String comuna = biz.getComunaById(Id_Poligono);
		
		// 5. Setea variables del template
		top.setVariable("{url}", Url);
		//top.setVariable("{Id_Poligono}", Id_Poligono+"");
		top.setVariable("{msg}", "");
								
		// 6. Setea variables bloques
		
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
