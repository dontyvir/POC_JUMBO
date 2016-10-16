package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;

/**
 * Agrega una dirección de despacho para un cliente
 *  
 * @author carriagada it4b
 *  
 */
public class AjaxAgregaDireccion extends Command {

    /**
     * Agrega una dirección de despacho para un cliente
     * 
     * @param arg0  Request recibido desde el navegador
     * @param arg1  Response recibido desde el navegador
     * @throws Exception
     */
    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
        try {
        //Carga properties
        ResourceBundle rb = ResourceBundle.getBundle("fo");
        
        HttpSession session = arg0.getSession();
                
        //Se recupera la salida para el servlet
        PrintWriter out = arg1.getWriter();
        
        // Recupera pagina desde web.xml y se inicia parser
        String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
        this.getLogger().debug( "[AjaxAgregaDireccion - Template]:" + pag_form );
        TemplateLoader load = new TemplateLoader(pag_form);
        ITemplate tem = load.getTemplate();
        
        IValueSet top = new ValueSet(); 
            
        // Instacia del bizdelegate
        BizDelegate biz = new BizDelegate();
        
        Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
        
        long tipo_calle = Long.parseLong(arg0.getParameter("tipo_calle").toString());
        String calle = arg0.getParameter("calle").toString();
        String numero = arg0.getParameter("numero").toString();
        String departamento = arg0.getParameter("departamento").toString();
        long comuna = Long.parseLong(arg0.getParameter("comuna").toString());
        String alias = arg0.getParameter("alias").toString();
        
        DireccionesDTO direccion = new DireccionesDTO();
        direccion.setAlias(alias);
        direccion.setCalle(calle);
        direccion.setId_cliente(cliente_id.longValue());
        direccion.setCom_id(comuna);
        direccion.setDepto(departamento);
        direccion.setEstado("A");
        direccion.setTipo_calle(tipo_calle);
        direccion.setNumero(numero);
        
        long id = biz.clienteInsertDireccion(direccion);
        
        List lista = biz.clientegetAllDirecciones(cliente_id.longValue());
        List direcciones = new ArrayList();
        DireccionesDTO dir;
        for (int i = 0; i < lista.size(); i++) {
            dir = (DireccionesDTO) lista.get(i);
            IValueSet fila = new ValueSet();
            fila.setVariable("{dir_opcion}", dir.getAlias() + "");
            fila.setVariable("{dir_calle}", dir.getCalle() + "");
            fila.setVariable("{dir_numero}", dir.getNumero() + "");
            fila.setVariable("{dir_dpto}", dir.getDepto() + "");
            fila.setVariable("{dir_valor}", dir.getId() + "");
            fila.setVariable("{dir_comentario}", dir.getComentarios() + "");
            int largo = 12;
            if( dir.getComentarios().length() < 12  )
                largo = dir.getComentarios().length(); 
            fila.setVariable("{dir_comentario_s}", dir.getComentarios().substring(0,largo) + "");
            fila.setVariable("{dir_tipid}", dir.getTipo_calle() + "");
            fila.setVariable("{dir_id_comuna}", dir.getCom_id() + "");
            fila.setVariable("{dir_comuna}", dir.getCom_nombre() + "");
            fila.setVariable("{dir_region}", dir.getReg_nombre() + "");
            fila.setVariable("{dir_id_region}", dir.getReg_id() + "");
            fila.setVariable("{id_local}", dir.getLoc_cod() + "");
            fila.setVariable("{id_zona}", dir.getZona_id() + "");
            if (id == dir.getId())
                fila.setVariable("{selected}", "selected");
            else
                fila.setVariable("{selected}", "");
            direcciones.add(fila);
        }       
        
        DireccionesDTO direc = biz.clienteGetDireccion(id);
        /*Setea direccion a la session del cliente.*/
        session.setAttribute("ses_comuna_cliente", ""+direc.getReg_id()+"-=-"+direc.getCom_id()+"-=-"+direc.getCom_nombre());
		
		session.setAttribute("ses_zona_id", String.valueOf(direc.getZona_id()));
		session.setAttribute("ses_loc_id", String.valueOf(direc.getLoc_cod()));
		session.setAttribute("ses_dir_id", String.valueOf(direc.getId()));
		session.setAttribute("ses_dir_alias", direc.getAlias());
		
		top.setDynamicValueSets("direcciones", direcciones);
        String result = tem.toString(top);
        out.print(result);
      } catch (Exception e) {
          this.getLogger().error(e);
          e.printStackTrace();
          throw new CommandException( e );
      }  
    }

}
