package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.ComunasDTO;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el formulario que permite crear una nueva direccion de facturacion de una sucursal
 * 
 * @author BBRI
 */
public class ViewDirFacturacionNewForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long id_sucursal = -1;
		
		View salida = new View(res);
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parametro
		if(req.getParameter("id_sucursal")!=null)
			id_sucursal = Long.parseLong(req.getParameter("id_sucursal"));

		BizDelegate bizDelegate = new BizDelegate();
		
		//listado de comuna
		ArrayList comunas = new ArrayList();
		List listComunas = bizDelegate.getComunasFact();

		for (int i = 0; i < listComunas.size(); i++) {
			IValueSet fila_com = new ValueSet();
			ComunasDTO com = (ComunasDTO) listComunas.get(i);
			fila_com.setVariable("{id_comuna}", String.valueOf(com.getId_comuna()));
			fila_com.setVariable("{nom_comuna}", String.valueOf(com.getNombre())+" - " +String.valueOf(com.getReg_nombre()));
			fila_com.setVariable("{sel_com}","");	
			
			comunas.add(fila_com);

		}		
		
		//listado de tipo de calle
		ArrayList tipos_calle = new ArrayList();
		List listTip = bizDelegate.getTiposCalle();

		for (int i = 0; i< listTip.size(); i++){
			IValueSet fila_est = new ValueSet();
			TipoCalleDTO tip = (TipoCalleDTO)listTip.get(i);
			fila_est.setVariable("{id_tip_call}", String.valueOf(tip.getId()));
			fila_est.setVariable("{nom_tip_call}"	, String.valueOf(tip.getNombre()));
			fila_est.setVariable("{sel_tca}","");		
			tipos_calle.add(fila_est);
		}
		
		top.setDynamicValueSets("COMUNAS", comunas);
		top.setDynamicValueSets("T_CALLE", tipos_calle);
		top.setVariable("{id_sucursal}"	,String.valueOf(id_sucursal));
		top.setVariable("{fec_actual}"	,Formatos.getFechaActual());
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		top.setVariable("{fecha}"	,Formatos.getFechaActual());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}