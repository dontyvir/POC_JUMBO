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
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CampanasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CampanaDTO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Despliega el monitor de campanas web
 * @author BBRI
 */
public class ViewMonCampanas extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int pag;
		int regsperpage;
		String action = "";
		char tipo;
		char activo;
		long id_cat_padre = -1; 
		String mns = "";
		
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
		
        regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
        logger.debug("RegsPerPage: " + regsperpage);		
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		
		logger.debug("Template: " + html);

// 		2. Procesa parámetros del request
		if ( req.getParameter("action") != null )
			action =  req.getParameter("action");
		
		if ( req.getParameter("mns") != null ){
			mns = "<script language='JavaScript'>alert('"+req.getParameter("mns")+"');</script>";
		}
		logger.debug("mns:"+mns);		
		if ( req.getParameter("tipo") != null )
			tipo =  req.getParameter("tipo").charAt(0);
		else {
			tipo = '0';
		}
		if ( req.getParameter("est_act") != null )
			activo =  req.getParameter("est_act").charAt(0) ;
		else {
			activo = '0';
		}
		if ( req.getParameter("pagina") != null )
			pag = Integer.parseInt( req.getParameter("pagina") );
		else {
			pag = 1;
		}
		logger.debug("req tipo:"+req.getParameter("tipo")+" y tipo:"+tipo);
		logger.debug("req est_act:"+req.getParameter("est_act")+" y activo:"+activo);
		
		//obtener número de categoria, nombre de categoria y navegar arbol de campanas
		String num_cam="";
		if ( req.getParameter("busq_num_cat") != null )
			num_cam =  req.getParameter("busq_num_cat");
		String nom_cam="";
		if ( req.getParameter("busq_nom_cat") != null )
			nom_cam =  req.getParameter("busq_nom_cat");
		String sel_cat = "";
		if ( req.getParameter("sel_cat") != null )
			sel_cat = req.getParameter("sel_cat");
		logger.debug("num_cam:"+num_cam);
		logger.debug("nom_cam:"+nom_cam);
		logger.debug("sel_cat:"+sel_cat);
		if(sel_cat!=null && !sel_cat.equals(""))
			id_cat_padre = new Long(sel_cat).longValue();
		
//		3. Template
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();

		//ver estados
		ArrayList estados = new ArrayList();
		List listEstados = bizDelegate.getEstadosByVis("ALL","S");

		for (int i = 0; i< listEstados.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEstados.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			logger.debug("activo:"+String.valueOf(activo));
			logger.debug("id_estado:"+String.valueOf(est1.getId_estado()));
			if (activo != 0 && String.valueOf(activo).equals(String.valueOf(est1.getId_estado()))){
				fila_est.setVariable("{sel_est}","selected");
			}
			else
				fila_est.setVariable("{sel_est}","");		
			estados.add(fila_est);
		}
		
//		 4.1 Listado de Campanias
		CampanasCriteriaDTO criterio = new CampanasCriteriaDTO();
		//pag, activo, tipo, regsperpage, true,	nom_cam, num_cam, sel_cat);
		criterio.setPag(pag);
		criterio.setActivo(activo);
		criterio.setNombre(nom_cam);
		criterio.setNumero(num_cam);
		criterio.setRegsperpage(regsperpage);
		
		ArrayList campanas = new ArrayList();
		
		//try{
			List lst_camp = bizDelegate.getCampanasByCriteria(criterio);
	
			List lst_est = bizDelegate.getEstadosByVis("ALL","S");
			
			logger.debug("cate 4.2.1-"+lst_camp.size());
			if (lst_camp.size() < 1 ){
				top.setVariable("{mje1}","La consulta no arrojo resultados");
			}else{
				top.setVariable("{mje1}","");
			}
			
			for (int i = 0; i< lst_camp.size(); i++){
				IValueSet fila_cam = new ValueSet();
				CampanaDTO camp1 = (CampanaDTO)lst_camp.get(i);
				fila_cam.setVariable("{id_camp}", String.valueOf(camp1.getId_campana()));
				fila_cam.setVariable("{nom_cam}"	, String.valueOf(camp1.getNombre()));
				fila_cam.setVariable("{est_cat}"	, FormatoEstados.frmEstado(lst_est, camp1.getEstado()));
				fila_cam.setVariable("{fec_crea}"	, Formatos.frmFecha(String.valueOf(camp1.getFec_creacion())));
				fila_cam.setVariable("{ver}"	, "Ver");
			
				campanas.add(fila_cam);
				//id_cat_padre = camp1.getId_cat_padre();
			}
		//}catch(Exception ex){
			//logger.debug("Error:"+ex.getMessage());
			//ex.printStackTrace();
		//}

		top.setVariable("{id_cat_padre}", id_cat_padre+"");
	
		//		 5 Paginador
		logger.debug(" cate 5");
		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getCountCampanasByCriteria(criterio);
		logger.debug ("total de registros: " + tot_reg);
		logger.debug ("registros por pagina: " + regsperpage);

		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug ("round: " + total_pag);
		if (total_pag == 0){
			total_pag = 1;
		}
		
			
	    for (int i = 1; i <= total_pag; i++) {
			IValueSet fila_pag = new ValueSet();
			fila_pag.setVariable("{pag}",String.valueOf(i));
			if (i == pag){
				fila_pag.setVariable("{sel_pag}","selected");
			}
			else
				fila_pag.setVariable("{sel_pag}","");				
			pags.add(fila_pag);
		}		
		
	    top.setVariable("{num_pag}"		,String.valueOf(pag));
	    top.setVariable("{action}" ,action);
	    top.setVariable("{busq_num_cat}" ,num_cam);
	    top.setVariable("{busq_nom_cat}" ,nom_cam);
	    top.setVariable("{est_act}" ,String.valueOf(activo));
	    top.setVariable("{tipo}" ,String.valueOf(tipo));	    
		
		
		//Setea variables main del template 
	    top.setDynamicValueSets("PAGINAS", pags);
	    top.setDynamicValueSets("CAMPANAS", campanas);
	    top.setDynamicValueSets("EST_CATEG", estados);
	    top.setVariable("{mns}", mns );
	    
		// 6. Setea variables del template
		// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
	    
		// 7. Setea variables bloques		
		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}
}
