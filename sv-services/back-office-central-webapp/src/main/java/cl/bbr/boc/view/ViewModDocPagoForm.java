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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

/**
 * formulario que permite ingresar datos para modificar un documento de pago de un pedido
 * @author BBRI
 */
public class ViewModDocPagoForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_cot = -1;
		long id_suc = -1;

		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
		
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		
		if ( req.getParameter("cot_id") == null ){
			logger.error("Parámetro cot_id es obligatorio");
			throw new ParametroObligatorioException("cot_id es null");
		}	
		if ( req.getParameter("suc_id") == null ){
			logger.error("Parámetro suc_id es obligatorio");
			throw new ParametroObligatorioException("suc_id es null");
		}
		
		id_cot = Long.parseLong(req.getParameter("cot_id"));
		id_suc = Long.parseLong(req.getParameter("suc_id"));
		
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//datos asociados al Medio de Pago
		BizDelegate bizDelegate = new BizDelegate();
		List lst_dirfact =  bizDelegate.getListDireccionFactBySucursal(id_suc);
		logger.debug("lst_dirfact.size(): "+lst_dirfact.size());
		//Rescatamos los datos de la sucursal
		SucursalesDTO suc = bizDelegate.getSucursalById(id_suc);
		String rut_suc = suc.getSuc_rut()+"-"+suc.getSuc_dv();
		String raz_soc = suc.getSuc_razon();
		String direc;
		ArrayList dirfact = new ArrayList();
		for(int i=0; i<lst_dirfact.size(); i++){
			IValueSet fila = new ValueSet();
			DirFacturacionDTO df= (DirFacturacionDTO)lst_dirfact.get(i);	
			fila.setVariable("{df_id}"	   ,String.valueOf(df.getDfac_id()));
			direc = "";
			if (df.getNom_tip_calle()!= null){
				direc += df.getNom_tip_calle()+" ";
			}
			if (df.getDfac_calle() != null){
				direc += df.getDfac_calle()+" ";
			}
			if (df.getDfac_numero()!= null){
				direc += df.getDfac_numero()+" ";
			}
			if (df.getDfac_depto() != null ){
				direc += df.getDfac_depto()+" ";
			}
			if (df.getNom_comuna()!= null){
				direc += df.getNom_comuna()+" ";
			}
			if (df.getDfac_ciudad() != null){
				direc += df.getDfac_ciudad();
			}
			fila.setVariable("{direccion}" , direc);
			dirfact.add(fila);
		}	
		// 5. Setea variables del template
		
		// 6. Setea variables bloques
		top.setVariable("{rut_suc}",rut_suc);
		top.setVariable("{raz_soc}",raz_soc);
		top.setDynamicValueSets("DIR_FACT", dirfact);			

				
		
		String result = tem.toString(top);	
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}