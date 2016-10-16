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
import cl.bbr.jumbocl.pedidos.dto.ComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra Formulario para editar una zona de despacho
 * @author mleiva
 */

public class ViewComuna extends Command {



 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 	long	id_zona = -1;
	 	String 	param_id_zona = "";
	 	String 	paramUrl = "";
	 	String 	mensaje  = "";

     
	 	//	 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		paramUrl = getServletConfig().getInitParameter("url");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		if(req.getParameter("id_zona")!=null){
			param_id_zona = req.getParameter("id_zona");
			logger.debug("param_id_zona: "+param_id_zona);
			id_zona	= Long.parseLong(param_id_zona);
		}					
		if(req.getParameter("mensaje")!=null){
			mensaje	= req.getParameter("mensaje");
			logger.debug("mensaje: "+mensaje);			
		}
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate biz = new BizDelegate();
		
		ZonaDTO zona = biz.getZonaDespacho(id_zona);
		
		
		//4.1 Listado de Comunas por zona
		List listaComunas = new ArrayList();
		logger.debug("id_zona: "+id_zona);
		//listacomunaxzona = biz.getComunasByIdZonaDespacho(id_zona);
		listaComunas = biz.getComunasConPoligonosSinZona();
		ArrayList comunas = new ArrayList();
		logger.debug("listaComunas.size(): "+listaComunas.size());
		for (int i = 0; i<listaComunas.size();i++){
			IValueSet fila = new ValueSet();
			ComunaDTO com = (ComunaDTO) listaComunas.get(i);
			if(com.getId_comuna()>0){
				fila.setVariable("{id_comuna}", String.valueOf(com.getId_comuna()));
				fila.setVariable("{nombre}", com.getNombre());
				comunas.add(fila);
			}
		}

		// 4.2 Listado de Poligonos por Zona
		List listaPoligonosZona = biz.getPoligonosXZona(id_zona);
		List Poligonos = new ArrayList();
		for(int i =0;i<listaPoligonosZona.size();i++){
			IValueSet fila = new ValueSet();
			PoligonoxComunaDTO pol = (PoligonoxComunaDTO) listaPoligonosZona.get(i);
			fila.setVariable("{id_poligono}", pol.getId_poligono() + "");
			fila.setVariable("{num_poligono}", pol.getNum_poligono() + "");
			fila.setVariable("{desc_poligono}", pol.getDesc_poligono());
			fila.setVariable("{nom_comuna}", pol.getNom_comuna());
			Poligonos.add(fila);
		}
		
		// 4.2 Listado de Todas las Comunas
		/*List listacomuna = new ArrayList();
		listacomuna = biz.getComunasAll();
		ArrayList comunas = new ArrayList();
		for(int i =0;i<listacomuna.size();i++){
			IValueSet fila = new ValueSet();
			ComunaDTO comuna1 = (ComunaDTO) listacomuna.get(i);

			boolean pertenece = false;

			for (int j = 0; j<listacomunaxzona.size();j++){
				ComunaDTO comuna2 = (ComunaDTO) listacomunaxzona.get(j);
				if(comuna2.getId_comuna()==comuna1.getId_comuna()){
					pertenece = true;
				}
			}
			if(pertenece == false){
				fila.setVariable("{id_comuna}", String.valueOf(comuna1.getId_comuna()));
				fila.setVariable("{nombre}", comuna1.getNombre());
				comunas.add(fila);
			}
		}*/
		
		// 5. Setea variables del template
		top.setVariable("{id_zona}", String.valueOf(id_zona));
		top.setVariable("{nom_zona}", zona.getNombre());
		top.setVariable("{url}", paramUrl);
		if(!mensaje.equals("") || mensaje!=null){
			top.setVariable("{mensaje}", mensaje);
		}else{
			top.setVariable("{mensaje}", "");	
		}
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_comunas", comunas);
		top.setDynamicValueSets("select_poligonos", Poligonos);
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}", usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());	
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
    }//execute
}
