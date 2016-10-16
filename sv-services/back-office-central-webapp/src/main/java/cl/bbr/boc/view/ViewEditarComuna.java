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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaxComunaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega un formulario que permite editar una comuna
 * @author BBRI
 */
public class ViewEditarComuna extends Command {

	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		 String 	paramUrl = "";	 	
		 String 	mensaje  = "";	 	
		 long paramId_Comuna = 0L;
		 	
	     
		 //	 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		paramUrl = getServletConfig().getInitParameter("url");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);		

		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		if(req.getParameter("mensaje")!=null){
			mensaje	= req.getParameter("mensaje");
			logger.debug("mensaje: "+mensaje);			
		}
		if ( req.getParameter("id_comuna") == null ){throw new ParametroObligatorioException("id_comuna es null");}
		
		paramId_Comuna = Long.parseLong(req.getParameter("id_comuna"));
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator	
		BizDelegate biz = new BizDelegate();
			
		
		// 4.1 Listado de Todas las Comunas 
		List listazonaxcomuna = new ArrayList();
		listazonaxcomuna = biz.getZonasxComuna(paramId_Comuna);
		List zonas = new ArrayList();
		for(int i =0; i<listazonaxcomuna.size(); i++){
			IValueSet fila = new ValueSet();			
			ZonaxComunaDTO zonaxcomuna1 = (ZonaxComunaDTO) listazonaxcomuna.get(i);
			fila.setVariable("{nom_local}"	, String.valueOf(zonaxcomuna1.getNom_local()));
			fila.setVariable("{id_zona}"	, String.valueOf(zonaxcomuna1.getId_zona()));
			fila.setVariable("{descripcion}", zonaxcomuna1.getNom_zona());
			fila.setVariable("{orden}"		, zonaxcomuna1.getOrden() + "");					
			fila.setVariable("{i}"			, i + "");					
			zonas.add(fila);
		}	
		
		// 4.2 Listado de todos los poligonos
		List listaPoligonoXComuna = new ArrayList();
		listaPoligonoXComuna = biz.getPoligonosXComunaAll(paramId_Comuna);
		List poligonos = new ArrayList();
		for(int i =0; i<listaPoligonoXComuna.size(); i++){
			IValueSet fila = new ValueSet();			
			PoligonoxComunaDTO PoligonosXComuna = (PoligonoxComunaDTO) listaPoligonoXComuna.get(i);
			fila.setVariable("{id_poligono}", String.valueOf(PoligonosXComuna.getId_poligono()));
			fila.setVariable("{num_poligono}", String.valueOf(PoligonosXComuna.getNum_poligono()));
			fila.setVariable("{desc_poligono}", String.valueOf(PoligonosXComuna.getDesc_poligono()));
			fila.setVariable("{nom_local}", String.valueOf(PoligonosXComuna.getNom_local()));
			fila.setVariable("{nom_zona}", String.valueOf(PoligonosXComuna.getNom_zona()));
			fila.setVariable("{id_comuna}", String.valueOf(PoligonosXComuna.getId_comuna()));
			poligonos.add(fila);
		}
		
		String comuna = biz.getComunaById(paramId_Comuna);
		
		// 5. Setea variables del template
		top.setVariable("{url}", paramUrl);
		top.setVariable("{id_comuna}", paramId_Comuna+"");
		top.setVariable("{comuna}", comuna);
		top.setVariable("{mensaje1}", mensaje);
								
		// 6. Setea variables bloques
		top.setDynamicValueSets("LST_ZONAS_COMUNA", zonas);
		top.setDynamicValueSets("LST_POLIGONOS_COMUNA", poligonos);
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
				
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	 }//execute

	}
