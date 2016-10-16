package cl.bbr.boc.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class CategoriasInMobile extends Command {

	/**/private static final long serialVersionUID = -4322615027591366762L;

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
				
			if("getSeccionInGRB".equals(action)){//obtiene categorias padres que  estan en mobile 
				obj.putAll(bizDelegate.getCategoriasInGRB(1,2,"0"));	
			}
			
			else if("getRubroInGRB".equals(action)){//obtiene rubros que estan en mobile 
				String seccion = (request.getParameter("seccion") != null)? request.getParameter("seccion"):"0";
				if(!"0".equals(seccion))
					obj.putAll(bizDelegate.getCategoriasInGRB(1,4,seccion));
			}
			
			else if("getSubRubroInGRB".equals(action)){//obtiene sub rubros que estan en mobile 
				String rubro = (request.getParameter("rubro") != null)? request.getParameter("rubro"):"0";
				if(!"0".equals(rubro))
					obj.putAll(bizDelegate.getCategoriasInGRB(1,6,rubro));				
			}
			
			else if("getGrupoInGRB".equals(action)){//obtiene los grupos que estan en mobile 				
				String subRubro = (request.getParameter("subRubro") != null)? request.getParameter("subRubro"):"0";
				if(!"0".equals(subRubro))
					obj.putAll(bizDelegate.getCategoriasInGRB(1,9,subRubro));
			}
			
			else if("remove_cmbSeccionIn".equals(action)){
				String seccion = (request.getParameter("catId") != null)? request.getParameter("catId"):"0";
				if(!"0".equals(seccion))
					obj.put("registros",String.valueOf(bizDelegate.deleteCategoriaById(seccion,2)));
			}
			
			else if("remove_cmbRubroIn".equals(action)){
				String rubro = (request.getParameter("catId") != null)? request.getParameter("catId"):"0";
				if(!"0".equals(rubro))
					obj.put("registros",String.valueOf(bizDelegate.deleteCategoriaById(rubro,4)));
			}
			
			else if("remove_cmbSubRubroIn".equals(action)){
				String subrubro = (request.getParameter("catId") != null)? request.getParameter("catId"):"0";
				if(!"0".equals(subrubro))
					obj.put("registros",String.valueOf(bizDelegate.deleteCategoriaById(subrubro,6)));
			}
			
			else if("remove_checkGRUPOinGRB".equals(action)){
				String grupo = (request.getParameter("catId") != null)? request.getParameter("catId"):"0";
				if(!"0".equals(grupo))
					obj.put("registros",String.valueOf(bizDelegate.deleteCategoriaById(grupo,9)));
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
