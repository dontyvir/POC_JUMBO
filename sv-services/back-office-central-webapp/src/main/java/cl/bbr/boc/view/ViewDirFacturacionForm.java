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
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;

/**
 * Permite mostrar el formulario con el detalle de la direccion de facturacion de una sucursal
 * 
 * @author BBRI
 */
public class ViewDirFacturacionForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long id_sucursal = -1;
		long id_dir = -1;
		String msje = "";
		
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
		if(req.getParameter("id_dir")!=null)
			id_dir = Long.parseLong(req.getParameter("id_dir"));
		if(req.getParameter("msje")!=null)
			msje = req.getParameter("msje");
		

		BizDelegate bizDelegate = new BizDelegate();
		
		//obtener informacion de direccion de despacho
		DirFacturacionDTO dto = bizDelegate.getDireccionesFactByIdFact(id_dir);
		top.setVariable("{dir_id}"			,String.valueOf(dto.getDfac_id()));
		top.setVariable("{dir_com_id}"		,String.valueOf(dto.getDfac_com_id()));
		top.setVariable("{dir_tip_id}"		,String.valueOf(dto.getDfac_tip_id()));
		top.setVariable("{dir_alias}"		,String.valueOf(dto.getDfac_alias()));
		top.setVariable("{dir_calle}"		,String.valueOf(dto.getDfac_calle()));
		top.setVariable("{dir_numero}"		,String.valueOf(dto.getDfac_numero()));
		top.setVariable("{dir_depto}"		,String.valueOf(dto.getDfac_depto()));
		top.setVariable("{dir_comentarios}"	,String.valueOf(dto.getDfac_comentarios()));
		top.setVariable("{dir_ciudad}"		,String.valueOf(dto.getDfac_ciudad()));
		top.setVariable("{dir_fax}"			,String.valueOf(dto.getDfac_fax()));
		top.setVariable("{dir_nom_cont}"	,String.valueOf(dto.getDfac_nom_contacto()));
		top.setVariable("{dir_cargo}"		,String.valueOf(dto.getDfac_cargo()));
		top.setVariable("{dir_email}"		,String.valueOf(dto.getDfac_email()));
		top.setVariable("{dir_fono1}"		,String.valueOf(dto.getDfac_fono1()));
		top.setVariable("{dir_fono2}"		,String.valueOf(dto.getDfac_fono2()));
		top.setVariable("{dir_fono3}"		,String.valueOf(dto.getDfac_fono3()));
		top.setVariable("{dir_fono1}"		,String.valueOf(dto.getDfac_fono1()));
		top.setVariable("{dir_fec_crea}"	,String.valueOf(dto.getDfac_fec_crea()));
		
		long id_comuna 	= dto.getDfac_com_id();
		logger.debug("id_comuna:"+id_comuna);
		long id_tip_cal = dto.getDfac_tip_id();
		if(id_sucursal>0)
			id_sucursal = dto.getDfac_cli_id();
		
		//listado de comuna
		ArrayList comunas = new ArrayList();
		List listComunas = bizDelegate.getComunasFact();

		for (int i = 0; i < listComunas.size(); i++) {
			IValueSet fila_com = new ValueSet();
			ComunasDTO com = (ComunasDTO) listComunas.get(i);
			fila_com.setVariable("{id_comuna}", String.valueOf(com.getId_comuna()));
			fila_com.setVariable("{nom_comuna}", String.valueOf(com.getNombre())+" - " +String.valueOf(com.getReg_nombre()));
			
			if(id_comuna==com.getId_comuna())
				fila_com.setVariable("{sel_com}", "selected");
			else
				fila_com.setVariable("{sel_com}", "");
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
			if(id_tip_cal == tip.getId())
				fila_est.setVariable("{sel_tca}","selected");
			else
				fila_est.setVariable("{sel_tca}","");
			tipos_calle.add(fila_est);
		}
		
		top.setDynamicValueSets("COMUNAS", comunas);
		top.setDynamicValueSets("T_CALLE", tipos_calle);
		top.setVariable("{id_sucursal}"	,String.valueOf(id_sucursal));
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