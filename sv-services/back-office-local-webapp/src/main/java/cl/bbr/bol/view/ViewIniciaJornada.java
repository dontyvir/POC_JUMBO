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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.SeccionSapDTO;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * muestra el formulario que sirve para iniciar una jornada
 * @author mleiva
 */
public class ViewIniciaJornada extends Command {
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String param_id_jornada	= "";
		long id_jornada			= -1;
		
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		if ( req.getParameter("id_jornada") == null ){ 
			throw new ParametroObligatorioException("id_jornada");
		}	
		param_id_jornada = req.getParameter("id_jornada");
		id_jornada = Long.parseLong(param_id_jornada);
		
		logger.debug("id_jornada = " + req.getParameter("id_jornada"));

		if ( id_jornada == -1)
			throw new ParametroObligatorioException("id_jornada viene vacío");
		

		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		
		
		//4.1 obtiene la lista de sectores SAP para comanda
		List lst_secc_sap = new ArrayList();
		lst_secc_sap = bizDelegate.getSeccionesSAPPreparadosByIdJornada(id_jornada);
		
		logger.debug("Total de secciones: "+lst_secc_sap.size());
		ArrayList sector_fila = new ArrayList();
		ArrayList sector_col = new ArrayList();
		
		int j=0;
		
		for (int i=0; i<lst_secc_sap.size(); i++){			
			IValueSet col = new ValueSet();
			SeccionSapDTO seccion1 = new SeccionSapDTO();
			seccion1 = (SeccionSapDTO)lst_secc_sap.get(i);
			col.setVariable("{seccion}"		, seccion1.getDescrip());
			col.setVariable("{id_seccion}"	, String.valueOf(seccion1.getId_cat()));			
			col.setVariable("{id_jornada}"	, param_id_jornada);
			logger.debug(" columna : id_seccion :"+String.valueOf(seccion1.getId_cat())+ " nombre:"+seccion1.getDescrip());
			sector_col.add(col);	

			if ((i%5) == 0.0){
				IValueSet fila = new ValueSet();
				fila.setVariable("{indice_fila}"	, String.valueOf(j));				
				sector_fila.add(fila);
				logger.debug("cambia fila :"+j);
				j++;
				}
		}		
		//4.2 sectores local de verificacion de stock
		List lst_secc_loc = new ArrayList();
		lst_secc_loc = bizDelegate.getSeccionesLocalVerifStockByIdJornada(id_jornada);
		
		logger.debug("Total de secciones local: "+lst_secc_loc.size());
		ArrayList sector_loc_fila = new ArrayList();
		ArrayList sector_loc_col = new ArrayList();
		
		j=0;
		
		for (int i=0; i<lst_secc_loc.size(); i++){			
			IValueSet col = new ValueSet();
			SectorLocalDTO seccion1 = new SectorLocalDTO();
			seccion1 = (SectorLocalDTO)lst_secc_loc.get(i);
			col.setVariable("{seccion}"		, seccion1.getNombre());
			col.setVariable("{id_seccion}"	, String.valueOf(seccion1.getId_sector()));			
			col.setVariable("{id_jornada}"	, param_id_jornada);
			logger.debug(" columna : id_seccion :"+String.valueOf(seccion1.getId_sector())
						+ " nombre:"+seccion1.getNombre());
			sector_loc_col.add(col);	

			if ((i%5) == 0.0){
				IValueSet fila = new ValueSet();
				fila.setVariable("{indice_fila}"	, String.valueOf(j));				
				sector_loc_fila.add(fila);
				logger.debug("cambia fila :"+j);
				j++;
				}
		}		
		
		
				
		//4.3 ver estado de la jornada
		JornadaDTO jornada = bizDelegate.getJornadaById(id_jornada);
		if(jornada.getId_estado()==Constantes.ID_ESTADO_JORNADA_NO_INICIADA){
			top.setVariable("{hab_btn}", "enabled");
		}else{
			top.setVariable("{hab_btn}", "disabled");
		}
			
		// 5. Setea variables del template
		top.setVariable("{id_jornada}", param_id_jornada);
		
		// 6. Setea variables bloques
		
		//top.setDynamicValueSets("select_jornada_picking", sector_fila);
		top.setDynamicValueSets("sector_printer", sector_col);
		top.setDynamicValueSets("sector_titulo", sector_col);
		
		top.setDynamicValueSets("sector_printer_stock", sector_loc_col);
		top.setDynamicValueSets("sector_titulo_stock", sector_loc_col);
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		// 8. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();
		
		
	
	}
	
}
