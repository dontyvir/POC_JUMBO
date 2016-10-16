package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;


/**
 * cambia en session la comuna
 *  
 * @author imoyano
 *  
 */
public class AjaxLlenaFormDireccionRegSencillo extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {

        DireccionesDTO dir = null;
        try {
            HttpSession session = arg0.getSession();
            
            BizDelegate biz = new BizDelegate();

            // recuperar identificador de la región de consulta
            long idDirecion = Long.parseLong(session.getAttribute("ses_dir_id").toString());
            
            dir = biz.clienteGetDireccion(idDirecion);

            arg1.setContentType("text/xml");
            arg1.setHeader("Cache-Control", "no-cache");
            arg1.setCharacterEncoding("UTF-8");

            /*************************************************/
            /*********   T I P O   D E   C A L L E   *********/
            /*************************************************/

            //vemos cual es el mensaje a desplegar
            arg1.getWriter().write("<direccion>");
            arg1.getWriter().write("<id>" + dir.getId() + "</id>");
            arg1.getWriter().write("<alias>" + dir.getAlias() + "</alias>");
            arg1.getWriter().write("<tipo_calle>" + dir.getTipo_calle() + "</tipo_calle>");
            arg1.getWriter().write("<calle>" + dir.getCalle() + "</calle>");
            arg1.getWriter().write("<id_cliente>" + dir.getId_cliente() + "</id_cliente>");
            arg1.getWriter().write("<id_comuna>" + dir.getCom_id() + "</id_comuna>");
            arg1.getWriter().write("<id_region>" + dir.getReg_id() + "</id_region>");
            arg1.getWriter().write("<estado>" + dir.getEstado() + "</estado>");
            arg1.getWriter().write("<numero>" + dir.getNumero() + "</numero>");
            arg1.getWriter().write("<depto>" + dir.getDepto() + "</depto>");
            arg1.getWriter().write("<comentario>" + dir.getComentarios() + "</comentario>");
            arg1.getWriter().write("</direccion>");

        } catch (Exception e) {
            e.printStackTrace();
        }

	}
}