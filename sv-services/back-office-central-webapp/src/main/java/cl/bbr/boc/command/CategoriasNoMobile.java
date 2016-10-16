package cl.bbr.boc.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class CategoriasNoMobile extends Command{
	
	/**/private static final long serialVersionUID = 3155978911086049573L;

	protected void Execute(HttpServletRequest request, HttpServletResponse response,UserDTO usr) throws Exception {
		 		 
		try {
			//X-Requested-With:XMLHttpRequest jquery header
			if(request.getHeader("X-Requested-With") == null || !"XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
				return;
			}
			 
			response.setContentType("application/json; charset=UTF-8");
			String action = request.getParameter("action"); 
			BizDelegate bizDelegate = new BizDelegate();
			JSONObject obj	= new JSONObject();
			PrintWriter output = response.getWriter();
			
			//EL campo ID_CATPROD tiene 9 digitos 
			//[1-2] SECCION
			//[1-4] RUBRO
			//[1-6] SUBRUBRO
			//[1-9] GRUPO (Aqui es donde se asocian los productos)
			//A la funcion getCategoriasInGRB le digo que voy a rescatar especificando la posicion [inicio-fin]
				
			if("getSeccionNoGRB".equals(action)){//obtiene categorias padres que no estan en mobile 
				obj.putAll(bizDelegate.getCategoriasNoGRB("0"));			
			}
			else if("getRubroNoGRB".equals(action)){//obtiene rubros que estan en mobile				
				String seccion = (request.getParameter("seccion") != null)? request.getParameter("seccion"):"0";
				if(!"0".equals(seccion))
					obj.putAll(bizDelegate.getCategoriasNoGRB(seccion));				
			}
			else if("getSubRubroNoGRB".equals(action)){//obtiene sub rubros que estan en mobile 				
				String rubro = (request.getParameter("rubro") != null)? request.getParameter("rubro"):"0";
				if(!"0".equals(rubro))
					obj.putAll(bizDelegate.getCategoriasNoGRB(rubro));				
			}
			else if("getGrupoNoGRB".equals(action)){//obtiene grupso que estan en mobile 
				String subRubro = (request.getParameter("subRubro") != null)? request.getParameter("subRubro"):"0";
				if(!"0".equals(subRubro))
					obj.putAll(bizDelegate.getCategoriasNoGRB(subRubro));		
			}
			
			else if("add_cmbSeccionNo".equals(action)){//Agrega la seccion completa XX
				String seccion = (request.getParameter("catId") != null)? request.getParameter("catId"):"0";
				if(!"0".equals(seccion))
					obj.put("registros",String.valueOf(bizDelegate.addCategoriaById(seccion,2)));
			}
			
			else if("add_cmbRubroNo".equals(action)){//Agrega el rubro completo XXXX
				String rubro = (request.getParameter("catId") != null)? request.getParameter("catId"):"0";	
				if(!"0".equals(rubro))
					obj.put("registros",String.valueOf(bizDelegate.addCategoriaById(rubro,4)));
			}
			
			else if("add_cmbSubRubroNo".equals(action)){//Agrega el sub rubro completo XXXXXX
				String subrubro = (request.getParameter("catId") != null)? request.getParameter("catId"):"0";
				if(!"0".equals(subrubro))
					obj.put("registros",String.valueOf(bizDelegate.addCategoriaById(subrubro,6)));
			}
			
			else if("add_checkGRUPOnoGRB".equals(action)){//Agrega el grupo completo XXXXXXGGG
				String grupo = (request.getParameter("catId") != null)? request.getParameter("catId"):"0";
				if(!"0".equals(grupo))
					obj.put("registros",String.valueOf(bizDelegate.addCategoriaById(grupo,9)));
			}
			
			String outJson = obj.toString();
			output.println(outJson.trim());	
			output.close(); 
			response.flushBuffer();
		
		} catch (Exception ex) {
			logger.error(ex);
		} 
	}
}
