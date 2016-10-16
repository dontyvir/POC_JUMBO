package cl.bbr.fo.command;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;

/**
 * busca cliente por rut
 *  
 * @author Francisco Ramos
 *  
 */
public class AjaxExisteCliente extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
		String respuesta = "NOK";
        try { 
        	String rut_s = arg0.getParameter("rutCliente").toString();
            this.getLogger().info("Intento conexion ( RUT:" + rut_s + " )");
            
            rut_s = rut_s.replaceAll("\\.","").replaceAll("\\-","");
            rut_s = rut_s.substring(0, rut_s.length()-1);
            
			long rut = Long.parseLong(rut_s);
			
            BizDelegate biz = new BizDelegate();            
            
            ClienteDTO cliente = null;
			if (rut < 100000) {
				respuesta = "rutcorto";
			} else {
				cliente = (ClienteDTO) biz.clienteGetByRut(rut);
				if (!(cliente == null)) {
					respuesta = "OK";
				} else {
					respuesta = "NOK";
				}
			}
        } catch (Exception e) {
            //e.printStackTrace();
        }
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        try {
            arg1.getWriter().write("<datos_objeto>");
            arg1.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
            arg1.getWriter().write("</datos_objeto>");
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (NumberFormatException e2){
        	
		} catch (StringIndexOutOfBoundsException e3){
			
		}
	}
}