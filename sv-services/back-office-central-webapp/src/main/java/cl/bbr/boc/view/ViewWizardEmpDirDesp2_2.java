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
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;

/**
 * despliega el formulario que permite crear una nueva sucursal
 * 
 * @author BBRI
 */
public class ViewWizardEmpDirDesp2_2 extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long id_sucursal = -1;
		long emp_id = -1;
		
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
		id_sucursal = Long.parseLong(req.getParameter("id_sucursal"));
		if ( req.getParameter("emp_id") == null ){throw new ParametroObligatorioException("emp_id  es null");}
		emp_id = Long.parseLong(req.getParameter("emp_id"));
		BizDelegate biz = new BizDelegate();
		
		
		//Listado de direcciones de despacho asociadas
		

		logger.debug("Obtiene listado de direcciones de despacho");
		ArrayList dir_despachos = new ArrayList();
		List listDirDesp = biz.getListDireccionDespBySucursal(id_sucursal);
		int cant_reg = listDirDesp.size();
		for (int i = 0; i< listDirDesp.size(); i++){
			IValueSet fila_est = new ValueSet();
			DireccionesDTO dir = (DireccionesDTO)listDirDesp.get(i);
			fila_est.setVariable("{dir_id}", String.valueOf(dir.getId()));
			fila_est.setVariable("{dir_nom_tip_calle}", String.valueOf(dir.getNom_tip_calle()));
			fila_est.setVariable("{dir_calle}", String.valueOf(dir.getCalle()));
			fila_est.setVariable("{dir_numero}", String.valueOf(dir.getNumero()));
			fila_est.setVariable("{dir_depto}", String.valueOf(dir.getDepto()));
			fila_est.setVariable("{dir_nom_comuna}", String.valueOf(dir.getNom_comuna()));
			fila_est.setVariable("{dir_nom_region}", String.valueOf(dir.getReg_nombre()));
			fila_est.setVariable("{dir_nom_local}", String.valueOf(dir.getNom_local()));
			fila_est.setVariable("{id_sucursal}", String.valueOf(id_sucursal));
			fila_est.setVariable("{emp_id}"	,String.valueOf(emp_id));
			dir_despachos.add(fila_est);
		}
		
		
		
		//listado de zona
		/*ArrayList zonas = new ArrayList();
		List listZonas = biz.getZonas();

		if ( listZonas.size() > 0 ){
			for (int i = 0; i < listZonas.size(); i++) {
				IValueSet fila_zon = new ValueSet();
				ZonaDTO zon = (ZonaDTO) listZonas.get(i);
				
				ZonaDTO listLoc = biz.getZonaById(zon.getId_zona());
				fila_zon.setVariable("{id_zona}", String.valueOf(zon.getId_zona()));
				fila_zon.setVariable("{nom_zona}", String.valueOf(zon.getNombre()) + " ("+ listLoc.getNom_local()+")");
				zonas.add(fila_zon);				
				
				logger.debug("Esto es lo que devuelve listLoc " + listLoc.getNom_local() );
			}
		}*/
		
		//listado de comuna
		//ArrayList comunas = new ArrayList();
		//List listComunas = biz.getComunasConZona();

		/*for (int i = 0; i < listComunas.size(); i++) {
			IValueSet fila_com = new ValueSet();
			ComunaDTO com = (ComunaDTO) listComunas.get(i);
			fila_com.setVariable("{id_comuna}", String.valueOf(com.getId_comuna()));
			fila_com.setVariable("{nom_comuna}", String.valueOf(com.getNombre())+" - " +String.valueOf(com.getReg_nombre()));
			
			comunas.add(fila_com);

		}*/
		/*******************************************/
		ArrayList comunas = new ArrayList();
		//List listComunas = bizDelegate.getComunas();
		List listComunas = biz.getComunasConPoligonos();

		for (int i = 0; i < listComunas.size(); i++) {
			IValueSet fila_com = new ValueSet();
			ComunasDTO com = (ComunasDTO) listComunas.get(i);
			fila_com.setVariable("{id_comuna}", String.valueOf(com.getId_comuna()));
			fila_com.setVariable("{nom_comuna}", String.valueOf(com.getNombre())+" - " +String.valueOf(com.getReg_nombre()));
			
			if (i == 0 ) {
				fila_com.setVariable("{sel_com}", "selected");
			} else {
				fila_com.setVariable("{sel_com}", "");
			}
			comunas.add(i, fila_com);
		}		

		// Listado de todos los poligonos
		String nom_zona = "";
		boolean FlagNomZona = false;
		List listaPoligonoXComuna = new ArrayList();
		long id_comuna = Long.parseLong(((IValueSet)comunas.get(0)).getVariable("{id_comuna}").toString());
		listaPoligonoXComuna = biz.getPoligonosXComuna(id_comuna);
		if (listaPoligonoXComuna.size() == 0){
		    listaPoligonoXComuna = biz.getPoligonosXComuna(((ComunasDTO) listComunas.get(0)).getId_comuna());
		}
		List poligonos = new ArrayList();
		for(int i =0; i<listaPoligonoXComuna.size(); i++){
			IValueSet fila_pol = new ValueSet();			
			PoligonoxComunaDTO pxc = (PoligonoxComunaDTO) listaPoligonoXComuna.get(i);
			
			fila_pol.setVariable("{id_poligono}", String.valueOf(pxc.getId_poligono()));
			fila_pol.setVariable("{num_poligono}", String.valueOf(pxc.getNum_poligono()));
			fila_pol.setVariable("{desc_poligono}", String.valueOf(pxc.getDesc_poligono()));
			fila_pol.setVariable("{id_local}", String.valueOf(pxc.getId_local()));
			fila_pol.setVariable("{nom_local}", String.valueOf(pxc.getNom_local()));
			fila_pol.setVariable("{id_zona}", String.valueOf(pxc.getId_zona()));
			fila_pol.setVariable("{nom_zona}", String.valueOf(pxc.getNom_zona()));
			fila_pol.setVariable("{id_comuna}", String.valueOf(pxc.getId_comuna()));
			fila_pol.setVariable("{nom_comuna}", String.valueOf(pxc.getNom_comuna()));
			/*if (dirpoligono != -1 && dirpoligono == pxc.getId_poligono()) {
			    fila_pol.setVariable("{sel_pol}", "selected");
			    nom_zona = String.valueOf(pxc.getNom_zona());
			    FlagNomZona = true;
			} else {*/
			    fila_pol.setVariable("{sel_pol}", "");
			//}
			if (!FlagNomZona){
			    nom_zona = String.valueOf(pxc.getNom_zona());
			    FlagNomZona = true;
			}

			poligonos.add(fila_pol);
		}

		/*******************************************/
		
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
		
		//top.setDynamicValueSets("ZONAS", zonas);
		top.setDynamicValueSets("COMUNAS", comunas);
		top.setDynamicValueSets("LST_POLIGONOS_COMUNA", poligonos);
		top.setDynamicValueSets("T_CALLE", tipos_calle);
		top.setDynamicValueSets("DIR_DESPACHO",dir_despachos);
		
		top.setVariable("{emp_id}"	,String.valueOf(emp_id));
		top.setVariable("{id_sucursal}"	,String.valueOf(id_sucursal));
		top.setVariable("{nom_zona}", nom_zona);
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