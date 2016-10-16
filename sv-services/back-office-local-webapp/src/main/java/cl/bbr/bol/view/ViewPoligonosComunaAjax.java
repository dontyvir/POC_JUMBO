package cl.bbr.bol.view;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * con un ajax permite cambiar datos de la comuna y la zona sin cargar la pagina 
 * @author BBRI
 */
public class ViewPoligonosComunaAjax extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_comuna = 0;
		
		BizDelegate bizDelegate = new BizDelegate();
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=iso-8859-1");
		
		PrintWriter salida = res.getWriter();
		
		if (req.getParameter("comuna") != null){
			id_comuna= Integer.parseInt(req.getParameter("comuna"));
        }else{
			id_comuna= 0;
		}
		
        List listaPoligonoXComuna = bizDelegate.getPoligonosXComunaSinZona(id_comuna);
		String result = "";
		
		if ( listaPoligonoXComuna.size() > 0 ){
			for (int i = 0; i < listaPoligonoXComuna.size(); i++) {
			    PoligonoxComunaDTO pol = (PoligonoxComunaDTO) listaPoligonoXComuna.get(i);
				//ZonaDTO listLoc = bizDelegate.getZonaById(pol.getId_zona());
				logger.debug("Esto es lo que devuelve listLoc " + pol.getNom_local() );
				//result += pol.getId_poligono() + "|" + pol.getNom_comuna() + " - " + pol.getNum_poligono() + "#"; //|" + pol.getNom_zona() + "
				result += pol.getId_poligono() + "|" + pol.getNom_comuna() + " " + pol.getNum_poligono() + "#";
				logger.debug(result);
			}
			result = result.substring(0,result.length()-1);
		}
		salida.print(result);
	}
}
