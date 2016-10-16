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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.promos.dto.Promocion;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionesCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra el monitor de Empresas
 * despliega los datos de la empresa, se puede utilizar filtros de búsqueda.
 * 
 * @author BBR
 *
 */
public class ViewMonPromos extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		int regsperpage 	= 10;
		int pag 			= 1;
		String msje			= "";
		long cod_promocion   = -1;
		long id_local		= -1;
		String sel_loc = "";
		
		// 1. Parámetros de inicialización servlet
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);
		
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		if ( req.getParameter("pagina") != null ){
			pag = Integer.parseInt( req.getParameter("pagina"));
		}
		logger.debug("pagina:"+pag);

		if( req.getParameter("msje") != null ){	
			msje = req.getParameter("msje");
			}
		logger.debug("msje:"+msje);
		
		if ( (req.getParameter("num_prom") != null) && (!req.getParameter("num_prom").equals("")) ){
			cod_promocion = Long.parseLong(req.getParameter("num_prom"));
			}
		logger.debug("num_prom:"+cod_promocion);
		
		
		if ( req.getParameter("sel_loc") != null && (!req.getParameter("sel_loc").equals("")) ) {
			sel_loc = req.getParameter("sel_loc");
			logger.debug("sel_local:"+sel_loc);
			if(!sel_loc.equals("T")){
				id_local = Long.parseLong(sel_loc);
				logger.debug("id_local:"+id_local);
			}
		}
		
		
		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();

		

		// 4.1 Listado de Promociones		
		PromocionesCriteriaDTO criterio  =new PromocionesCriteriaDTO();
		if (cod_promocion>0){
			criterio.setCod_promo(cod_promocion);
		}
		if(id_local>0){
			criterio.setId_local(id_local);
		}
		criterio.setPag(pag);
		
		// Listado de Promociones
		List lst_promociones = null;
		
		//lst_promociones=  bizDelegate.getPromocionesByCriteria(criterio);
		lst_promociones=  bizDelegate.getPromociones();
			
		//List lst_est = bizDelegate.getEstadosByVis("EMP","S");
	
		ArrayList promociones = new ArrayList();
		if (lst_promociones.size() < 1 ){
			top.setVariable("{mje1}","La consulta no arrojo resultados");
		}else{
			top.setVariable("{mje1}","");
		}

		for (int i = 0; i < lst_promociones.size(); i++) {			
				IValueSet fila = new ValueSet();
				Promocion promocion = (Promocion)lst_promociones.get(i);
				
				fila.setVariable("{id_promo}", String.valueOf(promocion.getCodigo()));
				fila.setVariable("{cod_promo}",	String.valueOf(promocion.getCodigo()));
				fila.setVariable("{nom_local}",	promocion.getCodLocalesString());
				fila.setVariable("{tipo_promo}", String.valueOf(promocion.getTipo()));
				fila.setVariable("{descr_promo}", promocion.getDescripcion());
				fila.setVariable("{fini_promo}", Formatos.fechaHora(promocion.getFechaIni()));
				fila.setVariable("{ffin_promo}", Formatos.fechaHora(promocion.getFechaFin()));
				promociones.add(fila);
			}		
		
	
		//4.2 Listado de Locales
		List lst_loc = null;	
		lst_loc = bizDelegate.getLocales();
		ArrayList locs = new ArrayList();
		
		for (int i = 0; i < lst_loc.size(); i++) {			
			IValueSet fila = new ValueSet();
			LocalDTO loc1 = (LocalDTO)lst_loc.get(i);
			fila.setVariable("{id_local}",String.valueOf(loc1.getId_local()));
			fila.setVariable("{nom_local}",loc1.getNom_local());
			//local de despacho
			if (id_local==loc1.getId_local())
				fila.setVariable("{sel_loc}", "selected");
			else
				fila.setVariable("{sel_loc}", "");			
			locs.add(fila);
			}
		
		//		 5 Paginador
		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getPromocionesByCriteriaCount(criterio);	
		logger.debug("tot_reg: " + tot_reg + "");
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug ("round: " + total_pag);
		if (total_pag == 0){
			total_pag = 1;
		}

		for (int i = 1; i <= total_pag; i++) {
			IValueSet fila = new ValueSet();
			fila.setVariable("{pag}", String.valueOf(i));
			if (i == pag)
				fila.setVariable("{sel_pag}", "selected");
			else
				fila.setVariable("{sel_pag}", "");
			pags.add(fila);
		}	
		//anterior y siguiente
		if( pag >1){
	    	int anterior = pag-1;
	    	top.setVariable("{anterior_label}","<< anterior");
	    	top.setVariable("{anterior}",String.valueOf(anterior));
	    }else if (pag==1){
	    	top.setVariable("{anterior_label}","");
	    }	    
	    if (pag <total_pag){
	    	int siguiente = pag+1;
	    	top.setVariable("{siguiente_label}","siguiente >>");
	    	top.setVariable("{siguiente}",String.valueOf(siguiente));
	    }else{
	    	top.setVariable("{siguiente_label}","");
	    }
	    
	    
		//Setea variables main del template

	    if(cod_promocion>0){
	    	top.setVariable("{cod_promo}"	,String.valueOf(cod_promocion));
	    }
	    else{
	    	top.setVariable("{cod_promo}"	,"");	
	    }
	    top.setVariable("{sel_loc}"	,sel_loc);
	    top.setVariable("{num_pag}"	,String.valueOf(pag));
		top.setVariable("{msje}"  , msje);

		// 6. Setea variables bloques
	    top.setDynamicValueSets("PROMOS", promociones);
	    top.setDynamicValueSets("LOCALES", locs);
		top.setDynamicValueSets("PAGINAS", pags);

		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
		
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
	
	}
	
}
