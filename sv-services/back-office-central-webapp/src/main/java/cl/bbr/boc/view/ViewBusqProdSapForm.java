package cl.bbr.boc.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * formulario de busqueda para productos Sap
 * se puede buscar a través de filtros o navegar por las categorias web
 * @author BBRI
 */
public class ViewBusqProdSapForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		long id_cat_padre = -1;
		int pag;
		int regsperpage;
		int id_cat=0;
		String codSapCat = "";
		String codSapProd = "";
		String sel_cat = "";
		String cod_cat_abuelo = "";
		String accion="";
		String mix_opcion ="";

		//logger.debug("User: " + usr.getLogin());
        regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
        logger.debug("RegsPerPage: " + regsperpage);		

		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parámetros
		if ( req.getParameter("pagina") != null )
			pag =  Integer.parseInt(req.getParameter("pagina")) ;
		else {
			pag = 1;
		}	
		if ( req.getParameter("accion") != null ){
			accion = req.getParameter("accion");
		}
		if ( req.getParameter("categoria") != null && !req.getParameter("categoria").equals("") )
			id_cat =  Integer.parseInt(req.getParameter("categoria"));
		String tipo_sap="";
		if ( req.getParameter("tipo_sap") != null ){
			tipo_sap = req.getParameter("tipo_sap");
			if( req.getParameter("tipo_sap").equals("cat") && req.getParameter("sap")!=null 
					&& !req.getParameter("sap").equals(""))
				codSapCat =  req.getParameter("sap");
			if( req.getParameter("tipo_sap").equals("prod") && req.getParameter("sap")!=null 
					&& !req.getParameter("sap").equals(""))
				codSapProd =  req.getParameter("sap");
		}

		
		if ( req.getParameter("categoria") != null )
			sel_cat = req.getParameter("categoria");
		if(sel_cat!=null && !sel_cat.equals(""))
			id_cat_padre = new Long(sel_cat).longValue();
		if ( req.getParameter("cod_cat_abuelo") != null ){
			cod_cat_abuelo = req.getParameter("cod_cat_abuelo");
		}
	
		
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();
		
		top.setVariable("{check2}", "checked");
		top.setVariable("{sap}", "");
		
		if( tipo_sap != null ){
			top.setVariable("{accion}"  ,"bus_sap");
			if(tipo_sap.equals("cat")){
				top.setVariable("{nom_hid1}","tipo_sap");
			    top.setVariable("{nom_hid2}","sap");
		    	top.setVariable("{tipo_sap}"   , "cat");
		    	top.setVariable("{sap}"   , codSapCat );
		    	top.setVariable("{sap}"   , codSapCat );
				top.setVariable("{check1}", "checked");
				top.setVariable("{check2}", "");
			} else if(tipo_sap.equals("prod")){
				top.setVariable("{nom_hid1}","tipo_sap");
			    top.setVariable("{nom_hid2}","prod");
		    	top.setVariable("{tipo_sap}"   , "prod");
		    	top.setVariable("{sap}"   , codSapProd );
		    	top.setVariable("{sap}"   , codSapProd);
		    	top.setVariable("{check2}","checked");
		    	top.setVariable("{check1}","");
			}
		}else{
			 top.setVariable("{accion}"  ,"bus_nav");
			 top.setVariable("{nom_hid1}","sel_cat");
			 top.setVariable("{tipo_sap}"   , sel_cat);
			 top.setVariable("{nom_hid2}","sap");
			 top.setVariable("{sap}"   , codSapCat );
		}
		//arbol de categorias
		ArrayList arbCategorias = new ArrayList();
			if(cod_cat_abuelo.equals("") && !sel_cat.equals("") && !sel_cat.equals("-1") ){
				cod_cat_abuelo = bizDelegate.getCodCatPadre(sel_cat);

			}
			logger.debug("** cod_cat_abuelo:"+cod_cat_abuelo);
			logger.debug("** sel_cat:"+sel_cat);
			
			List listArbCate = bizDelegate.getCategoriasSapById(sel_cat, cod_cat_abuelo);
			/*//opcion1
			if(listArbCate.size()==0){
				cod_cat_abuelo = bizDelegate.getCodCatPadre(cod_cat_abuelo);
				listArbCate = bizDelegate.getCategoriasSapById("-1", cod_cat_abuelo);
			}*/
			for (int i = 0; i< listArbCate.size(); i++){
				IValueSet fila_cat = new ValueSet();
				CategoriaSapDTO cat1 = (CategoriaSapDTO)listArbCate.get(i);
				
				
				fila_cat.setVariable("{cat_id}", String.valueOf(cat1.getId_cat())+"|"+String.valueOf(cat1.getTipo()));
				fila_cat.setVariable("{tipo_cat}", String.valueOf(cat1.getTipo()));
				int largo = cat1.getDescrip().length();
				if (largo > 31){
					largo = 31;
				}
				fila_cat.setVariable("{cat_nombre}"	,String.valueOf(cat1.getDescrip()).substring(0,largo));
				
				if (!sel_cat.equals("-") && !sel_cat.equals("-1") && sel_cat.equals(String.valueOf(cat1.getId_cat()))){
					fila_cat.setVariable("{sel_cat}","selected");
				}else
					fila_cat.setVariable("{sel_cat}","");		

				arbCategorias.add(fila_cat);
			}
			logger.debug("accion:" + accion);
			logger.debug("tipo_sap:"+tipo_sap);
			top.setVariable("{cod_cat_abuelo}",String.valueOf(cod_cat_abuelo));
			
			logger.debug("PAGINA: " + pag);
			logger.debug("MIX OPCION: " + mix_opcion);
			logger.debug("REGSPERPAGE: " + regsperpage);
			logger.debug("CODSAPCAT: "+codSapCat);
			logger.debug("CODSAPPROD: "+codSapProd);			
			logger.debug("SEL_CAT: "+sel_cat);

			
		
		
		//obtener los productos
		ProductosSapCriteriaDTO criterio = new ProductosSapCriteriaDTO(pag, mix_opcion, regsperpage, true, 
				codSapProd, codSapCat, sel_cat);

		ArrayList productos = new ArrayList();
			List listProd = null;
			if( accion.equals("") || accion.equals("navega")){
				listProd = new ArrayList();
			}else{
				listProd = bizDelegate.getProductosSapByCriteria(criterio);
				cod_cat_abuelo = bizDelegate.getCodCatPadre(cod_cat_abuelo);//Se obtiene la categoria padre
				top.setVariable("{cod_cat_abuelo}",String.valueOf(cod_cat_abuelo)); // Se parsea para que pueda subir al nivel correspondiente				
			}
			
			if (listProd.size() < 1  ){
				top.setVariable("{mje1}", "La consulta no arrojo resultados");
			}else{
				top.setVariable("{mje1}","");
			}		
			logger.debug("listProd:"+listProd.size());
			
			for (int i = 0; i< listProd.size(); i++){
				IValueSet fila_prod = new ValueSet();
				ProductosSapDTO prod1 = (ProductosSapDTO)listProd.get(i);
				
				fila_prod.setVariable("{valor}", String.valueOf(prod1.getId())+ "|"+ String.valueOf(prod1.getDes_corta()));
				fila_prod.setVariable("{id_prod}", String.valueOf(prod1.getId()));
				fila_prod.setVariable("{cod_prod}", String.valueOf(prod1.getCod_prod_1()));
				fila_prod.setVariable("{desc_prod}"	, String.valueOf(prod1.getDes_corta()));
				fila_prod.setVariable("{cat_sap}"	, String.valueOf(prod1.getNom_cat_sap()));
				fila_prod.setVariable("{cod_cat}", String.valueOf(id_cat));
				productos.add(fila_prod);
			}	
		
		// 5.Paginador
		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getCountProdSapByCriteria(criterio);
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		
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
	    top.setVariable("{pagina}"		,String.valueOf(pag));
	    top.setVariable("{categoria}"		,sel_cat);
		
		
		// 6. Setea variables bloques
	    top.setDynamicValueSets("PAGINA", pags);
	    top.setDynamicValueSets("CATEGORIA", arbCategorias);
	    top.setDynamicValueSets("INF_PROD", productos);

		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
