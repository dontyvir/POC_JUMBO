package cl.bbr.fo.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.util.Cod_error;


/**
 * Clase abstracta de la cual heredan todos los comandos.
 * 
 * @author BBR e-commerce & retail
 *  
 */
public abstract class AjaxCommand extends Command {
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)  throws ServletException, IOException {
        try {
            HttpSession session = arg0.getSession();
            setSession(arg0, session);
            logger.inicio_comando(this);
            
            boolean bol_res = true;
            if (!VerifyAccessControl(arg0, arg1)) {
				this.cod_error = Cod_error.GEN_ERR_PERMISOS;
				bol_res = false;
			} else if (this.ValidateParameters(arg0, arg1) == false) {
				logger.error("Faltan parámetros mínimos (Command): " + Cod_error.GEN_FALTAN_PARA);
				this.cod_error = Cod_error.GEN_FALTAN_PARA;
				bol_res = false;
			} else if (!this.validateDonaldAccess(arg0, session)) {
				logger.error("No tiene permiso para esta sección: " + Cod_error.GEN_ERR_PERMISOS);
				this.cod_error = Cod_error.GEN_ERR_PERMISOS;
				bol_res = false;
			} else {	
				arg1.setContentType("text/html");
				arg1.setCharacterEncoding("UTF-8");
			    execute(arg0, arg1);
			} 
            
            if(!bol_res) {
				arg1.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
    		
    		logger.fin_comando(this);
            
    		
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }
    
    /**
     * @param session
     * @return
     */
    private boolean checkSession(HttpSession session) {
        boolean result=true;
        if(session.getAttribute("ses_cli_id") == null) 
            result = false;
        return result;
    }

    /**
     * @param session
     * @return
     */
    private void setSession(HttpServletRequest arg0, HttpSession session) {
        logger.setSession(session.getId().substring(session.getId().length()-5));
        logger.setUsrAgent(arg0.getHeader("User-Agent"));
        if (arg0.getHeader("X-Forwarded-For") != null)
            logger.setUsrIP(arg0.getHeader("X-Forwarded-For"));
        else if (arg0.getHeader("x-forwarded-for") != null) 
            logger.setUsrIP(arg0.getHeader("x-forwarded-for"));
        else if (arg0.getHeader("X_Forwarded_For") != null)
            logger.setUsrIP(arg0.getHeader("X_Forwarded_For"));
        else if (arg0.getHeader("HTTP_X_FORWARDED_FOR") != null)
            logger.setUsrIP(arg0.getHeader("HTTP_X_FORWARDED_FOR"));
        else
            logger.setUsrIP(arg0.getRemoteAddr());

		if (session.getAttribute("ses_cli_id") != null && session.getAttribute("ses_cli_nombre") != null)
			logger.setUsuario((String) session.getAttribute("ses_cli_id"), (String) session.getAttribute("ses_cli_nombre"));
		else 
			logger.setUsuario("GUEST", "GUEST");
    }
}