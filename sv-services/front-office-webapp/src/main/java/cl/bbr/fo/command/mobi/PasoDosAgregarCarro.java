/*
 * Created on 27-ago-2008
 *
 */
package cl.bbr.fo.command.mobi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.command.AjaxCommand;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;

/**
 * @author jdroguett
 *  
 */

public class PasoDosAgregarCarro extends AjaxCommand {

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        try {
            String invitado_id = "";
            long cli_id = 0L;
            
            HttpSession session = arg0.getSession();
            BizDelegate biz = new BizDelegate();
            
            List lista_carro = new ArrayList();
            CarroCompraDTO carro = new CarroCompraDTO();
            carro.setPro_id(arg0.getParameter("productoId"));
            carro.setNota(arg0.getParameter("nota"));
            
            //getLogger().debug("nota : " + carro.getNota());

            String cantidad = arg0.getParameter("cantidad");
            cantidad = cantidad.replace(',', '.');
            carro.setCantidad(Double.parseDouble(cantidad));
            carro.setLugar_compra("");
            lista_carro.add(carro);
            cli_id = 0L;
            if(session.getAttribute("ses_cli_id")!= null)
            	cli_id= Long.parseLong((String)session.getAttribute("ses_cli_id"));
            if (session.getAttribute("ses_invitado_id") != null &&
                    !session.getAttribute("ses_invitado_id").toString().equals("")){
                invitado_id = session.getAttribute("ses_invitado_id").toString();
            }
            //biz.carroComprasInsertProducto(lista_carro, cli_id, invitado_id);MCA
            try{
                biz.carroComprasInsertProducto(lista_carro, cli_id, invitado_id);
            }catch (Exception e) {
				// TODO: handle exception MCA
            	logger.debug("Error PasoDosAgregarCarro.carroComprasInsertProducto."+e.getMessage());
            	logger.error("Error PasoDosAgregarCarro.carroComprasInsertProducto."+e.getMessage());
            	if(carro!= null)
            	 System.out.println("Error PasoDosAgregarCarro.carroComprasInsertProducto."+".cliente id.{"+cli_id +"}.invitado id.{"+ invitado_id +"}.carro.getPro_id.{"+carro.getPro_id()+"}");
			}

            /*
             * (J)Esto es para que marque el nuevo producto en el carro, se
             * mantiene el Arraylist para no cambiar mucho el código
             */
            List idsProductos = new ArrayList();
            idsProductos.add("" + carro.getPro_id());
            session.setAttribute("ids_prod_add", idsProductos);
            ///////////////////////////////////////

            arg1.setContentType("text/xml");
            arg1.getWriter().write("<datos_objeto>");
            arg1.getWriter().write("<respuesta>ok</respuesta>");
            arg1.getWriter().write("</datos_objeto>");
            arg1.getWriter().flush();
        } catch (Exception e) {
            this.getLogger().error(e);
            throw new CommandException(e);
        }
    }
}
