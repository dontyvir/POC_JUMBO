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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;

/**
 * despliega el formulario que permite crear una nueva direccion de facturacion de una sucursal
 * 
 * @author BBRI
 */
public class ViewWizardEmpDirFact2_1 extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long suc_id = -1;
		long emp_id = -1;
		String msje ="";
		
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
		if ( req.getParameter("id_sucursal") == null ){throw new ParametroObligatorioException("id_sucursal  es null");}
			suc_id = Long.parseLong(req.getParameter("id_sucursal"));
		if ( req.getParameter("emp_id") == null ){throw new ParametroObligatorioException("emp_id  es null");}
			emp_id = Long.parseLong(req.getParameter("emp_id"));
		if ( req.getParameter("msje") != null )
			msje=req.getParameter("msje") ;
		BizDelegate biz = new BizDelegate();
		
		
		// Listado de Direcciones de Facturacion creadas para la sucursal
		logger.debug("Obtiene listado de direcciones de facturacion");
		ArrayList dir_facturacion = new ArrayList();
		List listDirFact = biz.getListDireccionFactBySucursal(suc_id);
		int cant_reg = listDirFact.size();
		for (int i = 0; i< listDirFact.size(); i++){
			IValueSet fila_est = new ValueSet();
			DirFacturacionDTO dir = (DirFacturacionDTO)listDirFact.get(i);
			fila_est.setVariable("{dir_id}", String.valueOf(dir.getDfac_id()));
			fila_est.setVariable("{dir_nom_tip_calle}", String.valueOf(dir.getNom_tip_calle()));
			fila_est.setVariable("{dir_calle}", String.valueOf(dir.getDfac_calle()));
			fila_est.setVariable("{dir_numero}", String.valueOf(dir.getDfac_numero()));
			fila_est.setVariable("{dir_depto}", String.valueOf(dir.getDfac_depto()));
			fila_est.setVariable("{dir_nom_comuna}", String.valueOf(dir.getNom_comuna()));
			fila_est.setVariable("{dir_nom_region}", String.valueOf(dir.getNom_region()));
			fila_est.setVariable("{id_sucursal}", String.valueOf(suc_id));
			//fila_est.setVariable("{dir_nom_local}", String.valueOf(dir.getNom_local()));
			fila_est.setVariable("{emp_id}"	,String.valueOf(emp_id));
			dir_facturacion.add(fila_est);
		}		
		
		
		
		//listado de comuna
		ArrayList comunas = new ArrayList();
		List listComunas = biz.getComunasFact();

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
		List listTip = biz.getTiposCalle();

		for (int i = 0; i< listTip.size(); i++){
			IValueSet fila_est = new ValueSet();
			TipoCalleDTO tip = (TipoCalleDTO)listTip.get(i);
			fila_est.setVariable("{id_tip_call}", String.valueOf(tip.getId()));
			fila_est.setVariable("{nom_tip_call}"	, String.valueOf(tip.getNombre()));
			fila_est.setVariable("{sel_tca}","");		
			tipos_calle.add(fila_est);
		}
		
		// Si tiene al menos una direccion habilita el boton siguiente
		if (cant_reg > 0){
			top.setVariable("{hab}"	,"Enabled");
		}else{
			top.setVariable("{hab}"	,"Disabled");
		}
		
		top.setDynamicValueSets("COMUNAS", comunas);
		top.setDynamicValueSets("T_CALLE", tipos_calle);
		top.setDynamicValueSets("DIR_FACTURACION", dir_facturacion);
		top.setVariable("{id_sucursal}"	,String.valueOf(suc_id));
		top.setVariable("{emp_id}"	,String.valueOf(emp_id));
		top.setVariable("{fec_actual}"	,Formatos.getFechaActual());
		top.setVariable("{msje}"	,msje);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		top.setVariable("{fecha}"	,Formatos.getFechaActual());
			
		String result = tem.toString(top);
				
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}