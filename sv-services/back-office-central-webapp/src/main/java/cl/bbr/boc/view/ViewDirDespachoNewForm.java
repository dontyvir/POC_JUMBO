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
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el formulario que permite crear una nueva sucursal
 * 
 * @author BBRI
 */
public class ViewDirDespachoNewForm extends Command {
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
		List listComunas = bizDelegate.getComunas();

		for (int i = 0; i < listComunas.size(); i++) {
			IValueSet fila_com = new ValueSet();
			ComunasDTO com = (ComunasDTO) listComunas.get(i);
			fila_com.setVariable("{id_comuna}", String.valueOf(com.getId_comuna()));
			fila_com.setVariable("{nom_comuna}", String.valueOf(com.getNombre())+" - " +String.valueOf(com.getReg_nombre()));
			
			comunas.add(fila_com);
		}		
		
		// Listado de todos los poligonos
		List poligonos = new ArrayList();
		String nom_zona = "";
		boolean FlagNomZona = false;
		List listaPoligonoXComuna = new ArrayList();
		//listaPoligonoXComuna = bizDelegate.getPoligonosXComuna(id_comuna);
		//if (listaPoligonoXComuna.size() == 0){
		    listaPoligonoXComuna = bizDelegate.getPoligonosXComuna(((ComunasDTO) listComunas.get(0)).getId_comuna());
		//}
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
			/*if (id_poligono != -1 && id_poligono == pxc.getId_poligono()) {
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
		
		//top.setDynamicValueSets("ZONAS", zonas);
		top.setDynamicValueSets("COMUNAS", comunas);
		top.setDynamicValueSets("LST_POLIGONOS_COMUNA", poligonos);
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