package cl.bbr.boc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewVerBolsaRegalo extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		
		String MsjeError = getServletConfig().getInitParameter("MsjeError");
		
		BizDelegate bizDelegate = new BizDelegate();
		
		
		// 2. template
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 3. Trae bolsa de regalo
		String cod_bolsa = new String();
		String desc_bolsa = new String();
		String cod_barra_bolsa = new String();
		String cod_sap = new String();
		int id_producto = -1;
		
		cod_bolsa = req.getParameter("cod_bolsa");
		
		BolsaDTO bolsa = bizDelegate.getBolsaRegalo(cod_bolsa);
		
		desc_bolsa = bolsa.getDesc_bolsa();
		cod_barra_bolsa = bolsa.getCod_barra_bolsa();
		id_producto = bolsa.getId_producto();
		cod_sap = bizDelegate.getProductoByIdProd(bolsa.getId_producto()).getCod_sap();
		
		top.setVariable("{cod_bolsa}", cod_bolsa);
		top.setVariable("{desc_bolsa}", desc_bolsa);
		top.setVariable("{cod_barra_bolsa}", cod_barra_bolsa);
		top.setVariable("{id_producto}", id_producto+"");
		top.setVariable("{cod_sap}", cod_sap+"");
		
		logger.debug("desc_bolsa: " + desc_bolsa);
		logger.debug("cod_barra_bolsa: " + cod_barra_bolsa);
		logger.debug("id_producto: " + id_producto);
		logger.debug("cod_sap: " + cod_sap);
		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
	
	
	
}
