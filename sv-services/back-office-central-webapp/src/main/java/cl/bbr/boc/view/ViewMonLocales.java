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
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega el listado de comunas
 * @author BBRI
 * 
 */
public class ViewMonLocales extends Command {

	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		 	String 	paramUrl 	= "";	 	
		 	String 	mensaje 	= "";	 	

	     
		 	//	 1. Parámetros de inicialización servlet
			String html = getServletConfig().getInitParameter("TplFile");
			paramUrl = getServletConfig().getInitParameter("url");
			// le asignamos el prefijo con la ruta
			html = path_html + html;
			logger.debug("Template: " + html);		

			// 2. Procesa parámetros del request
			logger.debug("Procesando parámetros...");
			if(req.getParameter("msje")!=null){
				mensaje	= req.getParameter("msje");
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
			
			// 4.1 Listado de Todas las Comunas 
			List listalocales = new ArrayList();
			listalocales = biz.getLocales();
			ArrayList comunas = new ArrayList();
			for(int i =0;i<listalocales.size();i++){
				IValueSet fila = new ValueSet();
				LocalDTO local1 = (LocalDTO) listalocales.get(i);
					fila.setVariable("{id_local}", String.valueOf(local1.getId_local()));
					fila.setVariable("{cod_local}", String.valueOf(local1.getCod_local()));
					fila.setVariable("{nombre}", String.valueOf(local1.getNom_local()));
					fila.setVariable("{orden}", String.valueOf(local1.getOrden()));
					fila.setVariable("{fecha_carga_precios}", String.valueOf(local1.getFec_carga_prec()));
					fila.setVariable("{cod_local_pos}", String.valueOf(local1.getCod_local_pos()));
					
					if (local1.getTipo_flujo().equals(Constantes.LOCAL_TIPO_FLUJO_NORMAL_CTE)){
						fila.setVariable("{tipo_flujo}", Constantes.LOCAL_TIPO_FLUJO_NORMAL_TXT);
					}else if (local1.getTipo_flujo().equals(Constantes.LOCAL_TIPO_FLUJO_PARCIAL_CTE)){
						fila.setVariable("{tipo_flujo}", Constantes.LOCAL_TIPO_FLUJO_PARCIAL_TXT);
					}
					
					if ((local1.getCod_local_promocion()!=null) && (!local1.getCod_local_promocion().equals("")))
						fila.setVariable("{cod_local_promo}", local1.getCod_local_promocion());
					else
						fila.setVariable("{cod_local_promo}", "");
					
					if (local1.getTipo_picking().equals(Constantes.TIPO_PICKING_NORMAL_CTE)){
					    fila.setVariable("{tipo_picking}", Constantes.TIPO_PICKING_NORMAL_TXT);
					}else if (local1.getTipo_picking().equals(Constantes.TIPO_PICKING_LIGHT_CTE)){
					    fila.setVariable("{tipo_picking}", Constantes.TIPO_PICKING_LIGHT_TXT);
					}
					
					comunas.add(fila);
				}	
			
			// 5. Setea variables del template
				top.setVariable("{url}"		,paramUrl);
				if(!mensaje.equals("") || mensaje!=null){
					top.setVariable("{mensaje}"		,mensaje);
				}else{
					top.setVariable("{mensaje}"		,"");	
				}				
			// 6. Setea variables bloques
				top.setDynamicValueSets("LST_LOCALES",comunas);				
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
