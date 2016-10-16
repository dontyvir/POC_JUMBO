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
public class AjaxModificaDireccionRegSencillo extends Command {

    
    /**
     * Agrega una dirección de despacho para un cliente
     * 
     * @param arg0  Request recibido desde el navegador
     * @param arg1  Response recibido desde el navegador
     * @throws Exception
     */
    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
        String respuesta = "OK";
        try {
        
        HttpSession session = arg0.getSession();
                
        // Instacia del bizdelegate
        BizDelegate biz = new BizDelegate();
        
        long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
        long idDirecion = Long.parseLong(session.getAttribute("ses_dir_id").toString());
        
        long tipo_calle = Long.parseLong(arg0.getParameter("tipo_calle").toString());
        String calle = arg0.getParameter("calle").toString();
        String numero = arg0.getParameter("numero").toString();
        String departamento = arg0.getParameter("departamento").toString();
        long id_comuna = Long.parseLong(arg0.getParameter("id_comuna").toString());
        long id_region = Long.parseLong(arg0.getParameter("id_region").toString());
        String alias = arg0.getParameter("alias").toString();
        String comentario = arg0.getParameter("comentario").toString();
        
        DireccionesDTO direccion = new DireccionesDTO();
        direccion.setId(idDirecion);
        direccion.setAlias(alias);
        direccion.setCalle(calle);
        direccion.setId_cliente(cliente_id);
        direccion.setCom_id(id_comuna);
        direccion.setReg_id(id_region);
        direccion.setDepto(departamento);
        direccion.setEstado("A");
        direccion.setTipo_calle(tipo_calle);
        direccion.setNumero(numero);
        direccion.setComentarios(comentario);
        
        biz.clienteUpdateDireccion(direccion);
        
      } catch (Exception e) {
          this.getLogger().error(e);
          respuesta = "Ocurrió un error al Almacenar la Direccion.";
          
          throw new CommandException( e );
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
      }
      
    }

}
