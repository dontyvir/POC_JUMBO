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
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Permite mostrar el formulario con el detalle de la direccion de despacho de una sucursal
 * 
 * @author BBRI
 */
public class ViewDirDespachoForm extends Command {
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
		DireccionEntity dir = bizDelegate.getDireccionesDespByIdDesp(id_dir);
		top.setVariable("{dir_id}"			,String.valueOf(dir.getId()));
		top.setVariable("{dir_com_id}"		,String.valueOf(dir.getCom_id()));
		top.setVariable("{id_poligono}"		,String.valueOf(dir.getId_poligono()));
		top.setVariable("{dir_tip_id}"		,String.valueOf(dir.getTip_id()));
		top.setVariable("{dir_alias}"		,String.valueOf(dir.getAlias()));
		top.setVariable("{dir_calle}"		,String.valueOf(dir.getCalle()));
		top.setVariable("{dir_numero}"		,String.valueOf(dir.getNumero()));
		top.setVariable("{dir_depto}"		,String.valueOf(dir.getDepto()));
		top.setVariable("{dir_comentarios}"	,String.valueOf(dir.getComentarios()));
		top.setVariable("{dir_fecha}"		,Formatos.formatFecha(Formatos.frmFechaHora(dir.getFec_crea())));
		
		long id_comuna 	= dir.getCom_id().longValue();
		long id_poligono= dir.getId_poligono();
		long id_tip_cal = dir.getTip_id().longValue();
		if(id_sucursal>0)
			id_sucursal = dir.getCli_id().longValue();
		
		//listado de zona
		/*ArrayList zonas = new ArrayList();
		List listZonas = bizDelegate.getZonas();

		if ( listZonas.size() > 0 ){
			for (int i = 0; i < listZonas.size(); i++) {
				IValueSet fila_zon = new ValueSet();
				ZonaDTO zon = (ZonaDTO) listZonas.get(i);
				ZonaDTO listLoc = bizDelegate.getZonaById(zon.getId_zona());
				fila_zon.setVariable("{id_zona}", String.valueOf(zon.getId_zona()));
				fila_zon.setVariable("{nom_zona}", String.valueOf(zon.getNombre()) + " ("+ listLoc.getNom_local()+")");
				if(id_zona == zon.getId_zona())
					fila_zon.setVariable("{sel_zon}", "selected");
				else
					fila_zon.setVariable("{sel_zon}", "");
				zonas.add(fila_zon);				
		
				logger.debug("Esto es lo que devuelve listLoc " + listLoc.getNom_local() );
			}
		}*/
		
		//listado de comuna
		ArrayList comunas = new ArrayList();
		List listComunas = bizDelegate.getComunasConPoligonos();

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
		
		// Listado de todos los poligonos
		List poligonos = new ArrayList();
		String nom_zona = "";
		boolean FlagNomZona = false;
		List listaPoligonoXComuna = new ArrayList();
		listaPoligonoXComuna = bizDelegate.getPoligonosXComuna(id_comuna);
		if (listaPoligonoXComuna.size() == 0){
		    listaPoligonoXComuna = bizDelegate.getPoligonosXComuna(((ComunasDTO) listComunas.get(0)).getId_comuna());
		}
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
			if (id_poligono != -1 && id_poligono == pxc.getId_poligono()) {
			    fila_pol.setVariable("{sel_pol}", "selected");
			    nom_zona = String.valueOf(pxc.getNom_zona());
			    FlagNomZona = true;
			} else {
			    fila_pol.setVariable("{sel_pol}", "");
			}
			if (!FlagNomZona){
			    nom_zona = String.valueOf(pxc.getNom_zona());
			    FlagNomZona = true;
			}
			poligonos.add(fila_pol);
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
		
		//top.setDynamicValueSets("ZONAS", zonas);
		top.setDynamicValueSets("COMUNAS", comunas);
		top.setDynamicValueSets("LST_POLIGONOS_COMUNA", poligonos);
		top.setDynamicValueSets("T_CALLE", tipos_calle);
		top.setVariable("{id_sucursal}"	,String.valueOf(id_sucursal));
		top.setVariable("{nom_zona}", nom_zona);
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