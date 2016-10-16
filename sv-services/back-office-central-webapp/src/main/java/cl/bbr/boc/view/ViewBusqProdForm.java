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
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * formulario para búsqueda de productos
 * @author BBRI
 */
public class ViewBusqProdForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		long id_cat_padre = -1;
		int pag;
		int regsperpage;
		char tipo;
		char estado;
		int id_cat=0;
		String cod_prod="";
		String cod_prod_sap="";
		String action = "";
		String descrip = "";
		String sel_cat = "";

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
		if ( req.getParameter("action") != null )
			action =  req.getParameter("action");
		if ( req.getParameter("tipo") != null )
			tipo =  req.getParameter("tipo").charAt(0);
		else {
			tipo = '0';
		}
		
		if (req.getParameter("descrip") != null)
			descrip = req.getParameter("descrip");
		
		if ( req.getParameter("estado") != null )
			estado =  req.getParameter("estado").charAt(0) ;
		else {
			estado = '0';
		}
		if ( req.getParameter("pagina") != null )
			pag =  Integer.parseInt(req.getParameter("pagina")) ;
		else {
			pag = 1;
		}	
		if ( req.getParameter("categoria") != null && !req.getParameter("categoria").equals("") )
			id_cat =  Integer.parseInt(req.getParameter("categoria"));
		
		
		if ( req.getParameter("categoria") != null )
			sel_cat = req.getParameter("categoria");
		if(sel_cat!=null && !sel_cat.equals(""))
			id_cat_padre = new Long(sel_cat).longValue();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();
		
		top.setVariable("{check1}", "checked");
		String buscapor = req.getParameter("buscapor");
		logger.debug("Esta marcardo el buscar por:" + buscapor);
		if( buscapor != null ){
			if(buscapor.equals("prod")){
				logger.debug("Cod_prod Antes: "+req.getParameter("buscar"));				
				try{
					cod_prod = String.valueOf(Long.parseLong(req.getParameter("buscar")));
				}catch(NumberFormatException ex){
					cod_prod ="0";
				}
				logger.debug("Cod_prod Despues: "+ cod_prod);
				top.setVariable("{buscar}", cod_prod);
				top.setVariable("{check1}", "checked");
				top.setVariable("{check2}", "");
			}
		else if(buscapor.equals("sap")){
				cod_prod_sap = (String) req.getParameter("buscar");
				top.setVariable("{buscar}", cod_prod_sap);
				top.setVariable("{check2}", "checked");
				top.setVariable("{check1}", "");
			}
		}else{
			top.setVariable("{buscar}", " ");
		}
		
		if ( req.getParameter("categoria") != null )
			sel_cat = req.getParameter("categoria");
		
		logger.debug("sel_cat:"+sel_cat);
		if(sel_cat!=null && !sel_cat.equals(""))
			id_cat_padre = new Long(sel_cat).longValue();
		
		//descripcion
		if (action.equals("bus_desc")){
			top.setVariable("{descrip}"   , descrip);
			top.setVariable("{action}"   , action);
		}else{
			top.setVariable("{descrip}"   , "");
		} 
		
		//arbol de categorias
		char est = 'A';
		char tip = 'T';
		ArrayList arbCategorias = new ArrayList();
		CategoriasCriteriaDTO criterioArbolCat = new CategoriasCriteriaDTO(1, est, tip, 10, true,
				"", "", sel_cat);
		try{
			List listArbCate = bizDelegate.getCategoriasNavegacion(criterioArbolCat, id_cat_padre);
	
			for (int i = 0; i< listArbCate.size(); i++){
				IValueSet fila_cat = new ValueSet();
				CategoriasDTO cat1 = (CategoriasDTO)listArbCate.get(i);
				fila_cat.setVariable("{cat_id}", String.valueOf(cat1.getId_cat()));
				fila_cat.setVariable("{cat_nombre}"	, String.valueOf(cat1.getNombre()));
				if (!sel_cat.equals("-") && !sel_cat.equals("-1") && sel_cat.equals(String.valueOf(cat1.getId_cat()))){
					fila_cat.setVariable("{sel_cat}","selected");
				}
				else
					fila_cat.setVariable("{sel_cat}","");		

				arbCategorias.add(fila_cat);
				
			}
		}catch(Exception ex){
			logger.debug("err:"+ex.getMessage());
		}
		top.setVariable("{id_cat_padre}", id_cat_padre+"");
		
		//obtener los productos
		ProductosCriteriaDTO criterio = new ProductosCriteriaDTO(pag, cod_prod, cod_prod_sap, regsperpage, true,
				estado, tipo, descrip, id_cat);
		logger.debug("pag, cod_prod, cod_prod_sap, regsperpage, true, estado, tipo, id_cat, descrip:"+
				pag+","+ cod_prod+","+cod_prod_sap+","+regsperpage+","+true+","+estado+","+tipo+","+id_cat+","+descrip);
		ArrayList productos = new ArrayList();
		
		try{
			List listProd = bizDelegate.getProductosByCriteria(criterio);
	
			logger.debug("listProd:"+listProd.size());
			
			List lst_est = bizDelegate.getEstadosByVis("PR","S");
			List lst_tip = bizDelegate.getEstadosByVis("TPR","S");
			String prod_descr;
			int largo=0;
			for (int i = 0; i< listProd.size(); i++){
				logger.debug("entra "+ i + " veces");
				IValueSet fila_prod = new ValueSet();
				ProductosDTO prod1 = (ProductosDTO)listProd.get(i);
				
				//la descripcion corta no debe contener comillas simples ni comillas dobles 
				String valor = prod1.getDesc_corta().replaceAll("'","").replaceAll("\"","");
				
				fila_prod.setVariable("{valor}"		, String.valueOf(prod1.getId())+ "|"+ valor + "|" + prod1.getInter_valor() + "|" + prod1.getInter_max());
				fila_prod.setVariable("{cod_prod}"	, String.valueOf(prod1.getId()));

				prod_descr ="";				
				if (prod1.getTipo()!=null)		 prod_descr +=" "+prod1.getTipo();
				if (prod1.getNom_marca()!=null)	 prod_descr +=" "+prod1.getNom_marca();
				if (prod1.getDesc_corta()!=null) prod_descr +=" "+prod1.getDesc_corta();
				if (prod_descr.length() > 100)
					largo = 100;
				else
					largo = prod_descr.length();
				
				fila_prod.setVariable("{prod_desc}"	, prod_descr.substring(0,largo));
				
				fila_prod.setVariable("{tipo}"		, FormatoEstados.frmEstado(lst_tip, prod1.getGenerico()));
				fila_prod.setVariable("{est_prod}"	, FormatoEstados.frmEstado(lst_est, prod1.getEstado()));
				fila_prod.setVariable("{cod_cat}"	, String.valueOf(id_cat));
				productos.add(fila_prod);
				logger.debug("valor = " +String.valueOf(prod1.getId()) + "|"+ valor + "|" + prod1.getInter_valor() + "|" + prod1.getInter_max());
				//id_cat_padre = cat1.getId_cat_padre();
			}	
		}catch(Exception ex){
			logger.debug("err "+ex.getMessage());//ex.printStackTrace();
		}
		// 5.Paginador
		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getProductosCountByCriteria(criterio);
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
	    top.setVariable("{categoria}"	,String.valueOf(id_cat));
		
		
		
		// 6. Setea variables bloques
	    top.setDynamicValueSets("PAGINA", pags);
	    top.setDynamicValueSets("CATEGORIA", arbCategorias);
	    top.setDynamicValueSets("INF_PROD", productos);

		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
