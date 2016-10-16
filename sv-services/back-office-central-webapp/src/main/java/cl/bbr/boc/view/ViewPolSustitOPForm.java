package cl.bbr.boc.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PoliticaSustitucionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega formulario para editar la politica de sustitución de un pedido
 * @author BBRI
 */

public class ViewPolSustitOPForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int id_pedido=0;
		long pol_sust_id = -1;
		String pol_sust_desc = "";
		String mensaje = "";
		
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
		
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		if ( req.getParameter("id_pedido") != null )
			id_pedido = Integer.parseInt( req.getParameter("id_pedido") );
		logger.debug("id_pedido:"+id_pedido);

		if ( req.getParameter("mensaje") != null ){
			mensaje = req.getParameter("mensaje");
		}
		
		BizDelegate biz = new BizDelegate();
		PedidoDTO pedido = biz.getPedidosById(id_pedido); 
		pol_sust_desc = pedido.getPol_sustitucion();
		pol_sust_id = pedido.getPol_id();
		
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//Listado de politicas de sustitucion
		List lst_pol_sust = biz.getPolitSustitucionAll();
		ArrayList pols = new ArrayList();
		
		for (int i=0; i<lst_pol_sust.size(); i++){
			IValueSet filamot = new ValueSet();
			PoliticaSustitucionDTO psu1 = new PoliticaSustitucionDTO();
			psu1 = (PoliticaSustitucionDTO)lst_pol_sust.get(i);
			if (psu1.getId()==pol_sust_id){
				filamot.setVariable("{sel_psu}", "selected");	
			}else{
			    filamot.setVariable("{sel_psu}", "");
			}
			filamot.setVariable("{id_psu}"	,String.valueOf(psu1.getId()));
			filamot.setVariable("{desc_psu}",String.valueOf(psu1.getDescripcion()));
			filamot.setVariable("{sel}"		,"");
			pols.add(filamot);
		}

		top.setVariable("{id_pedido}"	,	String.valueOf(id_pedido));
		top.setVariable("{desc}"		,	String.valueOf(pol_sust_desc));
		top.setVariable("{id_pol}"		,	String.valueOf(pol_sust_id));
		top.setVariable("{mod}"		,	"");
		top.setVariable("{mensaje}"		,	mensaje);
		
		top.setDynamicValueSets("listado_pol_sust",pols);
		
		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}