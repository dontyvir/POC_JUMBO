package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * DelSucComprador Comando Process Elimina la relacion entre comprador y
 * sucursal
 * 
 * @author BBRI
 */

public class DelPoligono extends Command {
    private final static long serialVersionUID = 1;

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

        // 1. seteo de Variables del método
        String Url = "";
        long id_poligono = -1;
        long id_comuna = -1;
        String mensaje = "";

        // 2. Procesa parámetros del request
        logger.debug("Procesando parámetros...");

        // 2.1 revision de parametros obligatorios
        if (req.getParameter("id_poligono") == null) {
            throw new ParametroObligatorioException("id_poligono es null");
        }
        if (req.getParameter("id_comuna") == null) {
            throw new ParametroObligatorioException("id_comuna es null");
        }

        // 2.2 obtiene parametros desde el request
        Url = getServletConfig().getInitParameter("UrlResp");
        id_poligono = Long.parseLong(req.getParameter("id_poligono"));
        id_comuna   = Long.parseLong(req.getParameter("id_comuna"));

        // 2.3 log de parametros y valores
        logger.debug("url: " + Url);
        logger.debug("id_poligono: " + id_poligono);
        logger.debug("id_comuna: " + id_comuna);

        /*
         * 3. Procesamiento Principal
         */
        BizDelegate biz = new BizDelegate();

        try {
            int numDirAsoc = biz.VerificaPoligonoEnDirecciones(id_poligono);
            if (numDirAsoc > 0){
                mensaje ="No es posible Eliminar el Poligono ID: " + id_poligono + "<br>* Se encontraron " + numDirAsoc + " Direcciones enlazadas"; 
            }else{
                if (biz.DelPoligono(id_poligono)){
                    mensaje = "Poligono Eliminado en forma Satisfactoria.";
                }
            }
            Url += "?id_comuna=" + id_comuna + "&mensaje=" + mensaje;

        } catch (BocException e) {
            ForwardParameters fp = new ForwardParameters();
            fp.add(req.getParameterMap());

            logger.debug("Controlando excepción: " + e.getMessage());
            String UrlError = getServletConfig().getInitParameter("UrlResp");
            if (e.getMessage() != null && !e.getMessage().equals("")) {
                logger.debug("mensaje:" + e.getMessage());
                fp.add("id_comuna" , id_comuna + "");
                fp.add("mensaje" , e.getMessage());
                Url = UrlError + fp.forward();
            } else {
                logger.debug("Controlando excepción: " + e.getMessage());
            }
        }

        // 4. Redirecciona salida
        res.sendRedirect(Url);

    }//execute

}
