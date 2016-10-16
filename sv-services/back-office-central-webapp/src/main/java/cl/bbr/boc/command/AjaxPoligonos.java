package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.boc.bizdelegate.BizDelegate;


/**
 *  Comando que agrega un registro al log del pedido
 *  @author mleiva
 */
public class AjaxPoligonos extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		String VerificaNumPoligono = (req.getParameter("VerificaNumPoligono")==null?"NO":req.getParameter("VerificaNumPoligono"));
		int id_comuna = Integer.parseInt(req.getParameter("id_comuna")==null?"0":req.getParameter("id_comuna"));
		int num_pol   = -1;
		
		if (req.getParameter("num_pol") != null && !req.getParameter("num_pol").equalsIgnoreCase("")){
		    num_pol = Integer.parseInt(req.getParameter("num_pol"));
		}
		
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		
		/******************************************************************/
		logger.debug("Comienzo Test [AJAX]");
        String respuestaAjax = "";

        try {
            if (VerificaNumPoligono.equalsIgnoreCase("SI")){
                if (num_pol > -1){
                    if(!biz.verificaNumPoligono(id_comuna, num_pol)){
                        respuestaAjax = "OK";
                    }else{
                        respuestaAjax = "El Número de Polígono Existe";
                    }
                }else{
                    respuestaAjax = "Ingrese un Número de Polígono Valido";
                }
            }
        }catch(Exception e) {
            respuestaAjax = "Ocurrió un error";
            e.printStackTrace();
		}

        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<xml_respuesta>");
        res.getWriter().write("<mensaje>" + respuestaAjax + "</mensaje>");
        res.getWriter().write("</xml_respuesta>");

        logger.debug("Fin Test [AJAX]");
	}
}
