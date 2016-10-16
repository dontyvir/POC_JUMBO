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
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Despliega el monitor de categorias web
 * @author BBRI
 */
public class ViewMonCatWeb extends Command {

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
		
		//obtener número de categoria, nombre de categoria y navegar arbol de categorias
		String num_cat="";
		if ( req.getParameter("busq_num_cat") != null )
			num_cat =  req.getParameter("busq_num_cat");
		String nom_cat="";
		if ( req.getParameter("busq_nom_cat") != null )
			nom_cat =  req.getParameter("busq_nom_cat");
		String sel_cat = "";
		if ( req.getParameter("sel_cat") != null )
			sel_cat = req.getParameter("sel_cat");
		logger.debug("num_cat:"+num_cat);
		logger.debug("nom_cat:"+nom_cat);
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

		//ver tipos de categorias
		ArrayList tipos = new ArrayList();
		List listTipos = bizDelegate.getTiposCategorias();

		logger.debug("4.2.1");
		
		for (int i = 0; i< listTipos.size(); i++){
			IValueSet fila_tip = new ValueSet();
			EstadoDTO tip1 = (EstadoDTO)listTipos.get(i);
			fila_tip.setVariable("{id_tipo}", String.valueOf(tip1.getId_estado()));
			fila_tip.setVariable("{nom_tipo}"	, String.valueOf(tip1.getNombre()));
			
			if (tipo != 0 && String.valueOf(tipo).equals(String.valueOf(tip1.getId_estado()))){
				fila_tip.setVariable("{sel_tip}","selected");
			}
			else
				fila_tip.setVariable("{sel_tip}","");		
			tipos.add(fila_tip);
			
		}
		
		//ver estados
		ArrayList estados = new ArrayList();
		List listEstados = bizDelegate.getEstadosByVis("CW","S");

		logger.debug("4.2.1 est");
		
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
		
//		 4.1 Listado de Categorias
		CategoriasCriteriaDTO criterio = new CategoriasCriteriaDTO(pag, activo, tipo, regsperpage, true,
				nom_cat, num_cat, sel_cat);
		ArrayList categorias = new ArrayList();
		
		try{
			List listCate = bizDelegate.getCategoriasByCriteria(criterio);
	
			List lst_tip = bizDelegate.getEstadosByVis("CAT","S");
			List lst_est = bizDelegate.getEstadosByVis("CW","S");
			
			logger.debug("cate 4.2.1-"+listCate.size());
			if (listCate.size() < 1 ){
				top.setVariable("{mje1}","La consulta no arrojo resultados");
			}else{
				top.setVariable("{mje1}","");
			}
			
			for (int i = 0; i< listCate.size(); i++){
				IValueSet fila_cat = new ValueSet();
				CategoriasDTO cat1 = (CategoriasDTO)listCate.get(i);
				fila_cat.setVariable("{id_cat}", String.valueOf(cat1.getId_cat()));
				fila_cat.setVariable("{nom_cat}"	, String.valueOf(cat1.getNombre()));
				fila_cat.setVariable("{nom_cat_padre}"	,  bizDelegate.getNombresCategoriasPadreByIdCat( cat1.getId_cat() ) );
				fila_cat.setVariable("{est_cat}"	, FormatoEstados.frmEstado(lst_est, cat1.getEstado()));
				fila_cat.setVariable("{tipo}"	, FormatoEstados.frmEstado(lst_tip,cat1.getTipo()));
				fila_cat.setVariable("{ver}"	, "Ver");
				if(cat1.getTipo().equals("I")){
					fila_cat.setVariable("{accion1}", "| Subcategorías");
					fila_cat.setVariable("{url}", "ViewMonCatWeb?sel_cat="+cat1.getId_cat()+"&Ir=Enviar");
				}
				else if (cat1.getTipo().equals("T")){
					fila_cat.setVariable("{accion1}", "| Productos");
					fila_cat.setVariable("{url}", "ViewMonProducts?categoria_id="+cat1.getId_cat());
				}
                else if (cat1.getTipo().equals("C")){
                    fila_cat.setVariable("{accion1}", "| Subcategorías");
                    fila_cat.setVariable("{url}", "ViewMonCatWeb?sel_cat="+cat1.getId_cat()+"&Ir=Enviar");
                }
				else{
					fila_cat.setVariable("{accion1}"	, "");
					fila_cat.setVariable("{url}"	, "");					
				}
					
			
				categorias.add(fila_cat);
				//id_cat_padre = cat1.getId_cat_padre();
			}
	}catch(Exception ex){
		logger.debug("cate 4.2.1"+ex.getMessage());
	}

		top.setVariable("{id_cat_padre}", id_cat_padre+"");
	
		//arbol de categorias
		ArrayList arbCategorias = new ArrayList();
		CategoriasCriteriaDTO criterioArbolCat = new CategoriasCriteriaDTO(pag, activo, tipo, regsperpage, true,
				"", "", sel_cat);
		try{
			//List listArbCate = bizDelegate.getCategoriasByCriteria(criterioArbolCat);
			List listArbCate = bizDelegate.getCategoriasNavegacion(criterioArbolCat, id_cat_padre);
	
			logger.debug("arbCategorias -> "+listArbCate.size());
	
			for (int i = 0; i< listArbCate.size(); i++){
				IValueSet fila_cat = new ValueSet();
				CategoriasDTO cat1 = (CategoriasDTO)listArbCate.get(i);
				fila_cat.setVariable("{nav_id_cat}", String.valueOf(cat1.getId_cat()));
				fila_cat.setVariable("{nav_nom_cat}"	, String.valueOf(cat1.getNombre()));
				if (!sel_cat.equals("-") && !sel_cat.equals("-1") && sel_cat.equals(String.valueOf(cat1.getId_cat()))){
					fila_cat.setVariable("{sel_cat}","selected");
				}
				else
					fila_cat.setVariable("{sel_cat}","");		

				arbCategorias.add(fila_cat);
				
			}
	}catch(Exception ex){
		logger.debug("cate 4.2.1"+ex.getMessage());
	}
		
		
		
		//		 5 Paginador
		logger.debug(" cate 5");
		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getCategoriasCountByCriteria(criterio);
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
	    top.setVariable("{busq_num_cat}" ,num_cat);
	    top.setVariable("{busq_nom_cat}" ,nom_cat);
	    top.setVariable("{est_act}" ,String.valueOf(activo));
	    top.setVariable("{tipo}" ,String.valueOf(tipo));	    
		
		
		//Setea variables main del template 
	    top.setDynamicValueSets("PAGINAS", pags);
	    top.setDynamicValueSets("CATEGORIAS", categorias);
	    top.setDynamicValueSets("TIPOS", tipos);
	    top.setDynamicValueSets("EST_CATEG", estados);
	    top.setDynamicValueSets("SEL_CATEGORIA_NAV", arbCategorias);
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
