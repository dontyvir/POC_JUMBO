package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.dto.RegionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Muestra el formulario para agregar un pedido de tipo Jumbo VA
 * @author imoyano
 */
public class ViewAgregaPedidoJumboVA extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewAgregaPedidoJumboVA Execute");
        
        // Variables
        //Objetos para pintar la pantalla
        View salida = new View(res);
        String html = getServletConfig().getInitParameter("TplFile");       
        html = path_html + html;
        logger.debug("Template: " + html);      
        
        TemplateLoader load = new TemplateLoader(html);
        ITemplate tem = load.getTemplate();
        IValueSet top = new ValueSet();
        
        //Verificamos si el usuario esta editando un caso
        BizDelegate biz = new BizDelegate();
        
        List regiones = biz.getRegiones();
        
        ArrayList regs = new ArrayList();
        for (int i = 0; i < regiones.size(); i++) {
            IValueSet fila = new ValueSet();
            RegionDTO reg = (RegionDTO) regiones.get(i);
            fila.setVariable("{id_region}"    , String.valueOf(reg.getIdRegion()));
            fila.setVariable("{nombre}"    , reg.getNombre());
            regs.add(fila);
        }
       
        top.setDynamicValueSets("REGIONES", regs);        
        
        top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
        top.setVariable("{hdr_local}"   ,usr.getLocal());
        Date now = new Date();
        top.setVariable("{hdr_fecha}", now.toString());     
        
        logger.debug("Fin ViewAgregaPedidoJumboVA Execute");
        String result = tem.toString(top);
        salida.setHtmlOut(result);
        salida.Output();	
	}
}
