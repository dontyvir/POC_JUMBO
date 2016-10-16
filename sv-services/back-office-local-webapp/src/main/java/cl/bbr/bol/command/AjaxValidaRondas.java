package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.bol.bizdelegate.BizDelegate;


/**
 *  Comando que agrega un registro al log del pedido
 *  @author mleiva
 */
public class AjaxValidaRondas extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		String verDetalle       = (req.getParameter("VerDetalle")==null?"NO":req.getParameter("VerDetalle"));
		String EtiqImpresa      = (req.getParameter("EtiqImpresa")==null?"NO":req.getParameter("EtiqImpresa"));
		String IniciaJornadaPKL = (req.getParameter("IniciaJornadaPKL")==null?"NO":req.getParameter("IniciaJornadaPKL"));
		long id_ronda    = Long.parseLong(req.getParameter("id_ronda")==null?"0":req.getParameter("id_ronda"));
		
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		
		/******************************************************************/
		logger.debug("Comienzo Test [AJAX]");
        String respuestaAjax = "";

        try {
            if (verDetalle.equalsIgnoreCase("SI")){
                if (biz.setEstadoVerDetalle(id_ronda)){
                    respuestaAjax = "OK";
                }else{
                    respuestaAjax = "Ocurrió un error";
                }
            }

            if (EtiqImpresa.equalsIgnoreCase("SI")){
                if (biz.setEstadoImpEtiqueta(id_ronda)){
                    respuestaAjax = "OK";
                }else{
                    respuestaAjax = "Ocurrió un error";
                }
            }
            if (IniciaJornadaPKL.equalsIgnoreCase("SI")){
                if(!biz.ExisteFechaIniciaJornadaPKL(id_ronda)){
                    if (biz.setFechaIniciaJornadaPKL(id_ronda)){
                        respuestaAjax = "OK";
                    }else{
                        respuestaAjax = "Ocurrió un error";
                    }
                }else{
                    respuestaAjax = "La Ronda ya se inicio";
                }
            }

        	/*if (biz.getRondaConFaltantes(id_ronda)){
        	    respuestaAjax = "OK";
        	}else{
        	    respuestaAjax = "Ocurrió un error";
        	}*/
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
