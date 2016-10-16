package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.pedidos.dto.ListaGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;

/**
 * Página de listas especiales
 * 
 * @author imoyano
 * 
 */
public class ListasForm extends Command {
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");
            
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("Listas", arg0);
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
            
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
            int idTipoGrupo = 0;
            String from = "";
            if ( arg0.getParameter("itg") != null ) {
                idTipoGrupo = Integer.parseInt(arg0.getParameter("itg"));
            }
            if ( arg0.getParameter("from") != null ) {
                from = arg0.getParameter("from");
            }
			ListaTipoGrupoDTO marco = biz.getTipoGrupoById(idTipoGrupo);
            if ( !"S".equalsIgnoreCase(marco.getActivado()) ) {
                if ( !from.equalsIgnoreCase("") ) {
                    arg1.sendRedirect(from);
                    return;
                }
                throw new CommandException("Marco no existe o no está activo.");
            }
            
            top.setVariable("{titulo_marco}", marco.getDescripcion());
            top.setVariable("{texto}", marco.getTexto());
            top.setVariable("{banner}", marco.getNombreArchivo());            
            
            List listaGrupos = new ArrayList();
            List grupos = biz.getGruposDeListasByTipo(idTipoGrupo);
            for ( int i = 0; i < grupos.size(); i++ ) {
                ListaGrupoDTO grupo = (ListaGrupoDTO) grupos.get(i);
                IValueSet fila = new ValueSet();
                fila.setVariable("{id_grupo}", grupo.getIdListaGrupo() + "");
                fila.setVariable("{nombre}", grupo.getNombre());
                listaGrupos.add(fila);
            }
            
            top.setVariable("{from}", from);
            top.setDynamicValueSets("GRUPOS", listaGrupos);
            
			String result = tem.toString(top);
			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException(e);
		}
	}
}