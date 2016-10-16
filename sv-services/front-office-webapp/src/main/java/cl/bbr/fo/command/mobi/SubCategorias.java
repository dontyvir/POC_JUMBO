package cl.bbr.fo.command.mobi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.command.Command;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;

/**
 * Entrega el total en pesos de los productos que tiene el cliente en el carro
 * 
 * Pagina para mobile
 * 
 * @author imoyano
 * 
 */
public class SubCategorias extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
		    // Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
            
            int idCategoria = 0;
            if ( arg0.getParameter("categorias") != null ) {
                idCategoria = Integer.parseInt(arg0.getParameter("categorias"));
            }
            arg1.setContentType("text/html");
            arg1.setHeader("Cache-Control", "no-cache");
            arg1.setCharacterEncoding("UTF-8");
            arg1.getWriter().write(listaCategorias(biz.subCategorias(idCategoria), 0));

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}


	}
    
    /**
     * Entrega las opciones de las subcategorias
     * 
     * @param lista
     * @return
     */
    private String listaCategorias(List lista, int selected) {
        String out = "";
        if (selected == 0)
            out = "<option value=\"0\" selected>Todas</option>";
        else
            out = "<option value=\"0\" >Todas</option>";
        
        for (int i = 0; i < lista.size(); i++) {
            CategoriaDTO categoriaDTO = (CategoriaDTO) lista.get(i);
            if (selected == categoriaDTO.getId()) {
                out += "<option value=\"" + categoriaDTO.getId() + "\" selected>" + categoriaDTO.getNombre() + "</option>\n";
            } else {
                out += "<option value=\"" + categoriaDTO.getId() + "\">" + categoriaDTO.getNombre() + "</option>\n";
            }
        }
        return out;
    }

}