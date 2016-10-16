package cl.bbr.fo.command;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;

/**
 * Agrega una dirección de despacho para un cliente
 *  
 * @author carriagada it4b
 *  
 */
public class AjaxMensajeRegistrar extends Command {

    
    /**
     * Agrega una dirección de despacho para un cliente
     * 
     * @param arg0  Request recibido desde el navegador
     * @param arg1  Response recibido desde el navegador
     * @throws Exception
     */
    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
      String respuesta = "OK";
      HttpSession session = arg0.getSession();
      try {
        //Instacia del bizdelegate
        BizDelegate biz = new BizDelegate();
        long idDirecion = 0L;
        
        if (session.getAttribute("ses_dir_id") != null){
            idDirecion = Long.parseLong(session.getAttribute("ses_dir_id").toString());
        }

        //Si la variable "ses_flag_dir_nueva" no existe se muestra el mensaje
        if (session.getAttribute("ses_flag_dir_nueva") == null){
            
            if (idDirecion > 1){
                DireccionesDTO dir = biz.clienteGetDireccion(idDirecion);
                if (!dir.getCalle().equals("Ingresa tu calle")){
                    respuesta = "DIREC_MODIFICADA";
                }/*else{// dirección dummy sin modificación
                    respuesta = "DIRECCION_DUMMY";  ==> OK
                }*/
            }else{
                respuesta = "SIN_DIRECCION_NUEVA";
            }
        /*}else if (session.getAttribute("ses_flag_dir_nueva").toString().equals("1")){
            if (idDirecion > 1){
                DireccionesDTO dir = biz.clienteGetDireccion(idDirecion);
                if (!dir.getCalle().equals("Ingresa tu calle")){
                    respuesta = "DIREC_MODIFICADA";
                }
            }*/
        }else{//EL MENSAJE YA FUE MOSTRADO 1 VEZ
            respuesta = "ERROR";
        }
      } catch (Exception e) {
          this.getLogger().error(e);
          respuesta = "Ocurrió un error al Almacenar la Direccion.";
          e.printStackTrace();
          throw new CommandException( e );
      }
      arg1.setContentType("text/xml");
      arg1.setHeader("Cache-Control", "no-cache");
      arg1.setCharacterEncoding("UTF-8");
      try {
          if (session.getAttribute("ses_flag_dir_nueva") == null && respuesta.equals("OK")){
              session.setAttribute("ses_flag_dir_nueva", "1");
          }/*else if (session.getAttribute("ses_flag_dir_nueva") != null &&
                      session.getAttribute("ses_flag_dir_nueva").toString().equals("1") &&
                        respuesta.equals("DIREC_MODIFICADA")){
              session.setAttribute("ses_flag_dir_nueva", "2");
          }*/
          arg1.getWriter().write("<datos_objeto>");
          arg1.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
          arg1.getWriter().write("</datos_objeto>");
      } catch (IOException e1) {
          e1.printStackTrace();
      }
      
    }

}
