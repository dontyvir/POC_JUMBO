	package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.utils.FormatoEstados;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
/**
 * Despliega el monitor de productos
 * @author BBRI
 */
public class ViewMonProducts extends Command {
	private final static long serialVersionUID = 1;
	

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int pag;
		int regsperpage;
		View salida = new View(res);
		//int cat_id = -1;
		String action = "";
		String tipo_sap = "";
		char tipo;
		char estado;
		String cod_prod="";
		String cod_prod_sap="";
		String descrip = "";
		String sel_cat = "";
		long id_cat_padre = -1; 
		int id_cat=0;
		String html;
		logger.debug("User: " + usr.getLogin());
        regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
        logger.debug("RegsPerPage: " + regsperpage);		
		// Recupera pagina desde web.xml
       
		html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		//parámetros
		if ( req.getParameter("action") != null )
			action =  req.getParameter("action");
		if ( req.getParameter("tipo") != null )
			tipo =  req.getParameter("tipo").charAt(0);
		else {
			tipo = '0';
		}
		
		logger.debug("la accion es:" + action);
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
		if ( req.getParameter("categoria_id") != null ){
			id_cat =  Integer.parseInt(req.getParameter("categoria_id"));
        }else if(req.getParameter("id_cat") != null){
            id_cat =  Integer.parseInt(req.getParameter("id_cat"));
        }
		if ( req.getParameter("tipo_sap")!=null){
			tipo_sap = 	req.getParameter("tipo_sap");
			if(req.getParameter("tipo_sap").equals("prod"))
				cod_prod = req.getParameter("cod_sap") ;
			else if(req.getParameter("tipo_sap").equals("sap"))
				cod_prod_sap = req.getParameter("cod_sap") ;
		}
		logger.debug("action: "+req.getParameter("action"));
		logger.debug("Tipo: "+req.getParameter("tipo"));
		logger.debug("Estado: "+req.getParameter("estado"));
		logger.debug("pagina: "+req.getParameter("pagina"));
		logger.debug("categoria_id: "+req.getParameter("categoria_id"));
		logger.debug("tipo sap "+  req.getParameter("tipo_sap"));
		logger.debug("cod sap "+  req.getParameter("cod_sap"));
		logger.debug("descrip "+  req.getParameter("descrip"));
		logger.debug("sel_cat "+ req.getParameter("sel_cat"));
	
		if (req.getParameter("descrip") != null)
			descrip = req.getParameter("descrip");
	
		if ( req.getParameter("sel_cat") != null && !req.getParameter("sel_cat").equals("")){
			sel_cat = req.getParameter("sel_cat");
			id_cat = new Integer(sel_cat).intValue();
		}
		if(sel_cat!=null && !sel_cat.equals(""))
			id_cat_padre = new Long(sel_cat).longValue();

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();

		//setear atributos y checks
		
    	
		if (tipo_sap != null && tipo_sap != ""){  
			logger.debug("Entra aki");
			if (tipo_sap.equals("prod")) {
		    	top.setVariable("{check_1}","checked");
		    	top.setVariable("{check_2}","");
			} else {
		    	top.setVariable("{check_1}","");
		    	top.setVariable("{check_2}","checked");
			}    	
		}else{
			top.setVariable("{check_1}","checked");
	    	top.setVariable("{check_2}","");
		}
		if (action.equals("bus_prod")){
			if (tipo_sap.equals("prod")){
				top.setVariable("{cod_sap}"   , cod_prod);
				top.setVariable("{tipo_sap}"   ,tipo_sap);
			}
			if (tipo_sap.equals("sap")){
				top.setVariable("{cod_sap}"   , cod_prod_sap);
				top.setVariable("{tipo_sap}"  , tipo_sap);
			}
			//logger.debug("Hace la comparacion y manda los datos requeridos");
			//descrip = "";

		}else{
			top.setVariable("{cod_sap}"   , "");
			top.setVariable("{tipo_sap}"   ,"");
			/*cod_prod = "";
			cod_prod_sap = "";
			tipo_sap = "";*/
		}
		if (action.equals("bus_desc")){
			top.setVariable("{descrip}"   , descrip);
			/*cod_prod = "";
			cod_prod_sap = "";
			tipo_sap = "";*/
		}else{
			top.setVariable("{descrip}"   , "");
			//descrip = "";
		} 
		if (action.equals("bus_cat")){
			top.setVariable("{sel_cat}"   ,sel_cat);
		}else{
			top.setVariable("{sel_cat}"   ,"");
		}
	
		//ver tipos de productos
		ArrayList tipos = new ArrayList();
		List listTipos = bizDelegate.getTiposProductos();

		logger.debug("4.2.1");
		
		
		
		for (int i = 0; i< listTipos.size(); i++){
			IValueSet fila_tip = new ValueSet();
			EstadoDTO tip1 = (EstadoDTO)listTipos.get(i);
			fila_tip.setVariable("{tip_id}", String.valueOf(tip1.getId_estado()));
			fila_tip.setVariable("{tip_nombre}"	, String.valueOf(tip1.getNombre()));
			
			if (tipo != 0 && String.valueOf(tipo).equals(String.valueOf(tip1.getId_estado()))){
				fila_tip.setVariable("{sel_tip}","selected");
			}
			else
				fila_tip.setVariable("{sel_tip}","");		
			tipos.add(fila_tip);
			
		}
		
		//ver estados
		ArrayList estados = new ArrayList();
		List listEstados = bizDelegate.getEstadosByVis("PR","S");

		logger.debug("4.2.1 est");
		
		for (int i = 0; i< listEstados.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEstados.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			if (estado != 0 && String.valueOf(estado).equals(String.valueOf(est1.getId_estado()))){
				fila_est.setVariable("{sel_est}","selected");
			}
			else
				fila_est.setVariable("{sel_est}","");		
			estados.add(fila_est);
		}
		
//		 4.1 Listado de Productos
		ProductosCriteriaDTO criterio = new ProductosCriteriaDTO(pag, cod_prod, cod_prod_sap, regsperpage, true,
				estado, tipo, descrip, id_cat);
		
	
		
		ArrayList productos = new ArrayList();
		
		try{
			List listProd = bizDelegate.getProductosByCriteria(criterio);
	
			logger.debug("listProd:"+listProd.size());
	
			if (listProd.size() < 1 ){
				top.setVariable("{mje1}","La consulta no arrojo resultados");
			}else{
				top.setVariable("{mje1}","");
			}	
			String prod_descr;
			int largo=0;;
			for (int i = 0; i< listProd.size(); i++){
				IValueSet fila_prod = new ValueSet();
				ProductosDTO prod1 = (ProductosDTO)listProd.get(i);
				
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
				if(prod1.getUni_med_desc()!=null && !prod1.getUni_med_desc().equals("null")){
					fila_prod.setVariable("{uni_med}"	, String.valueOf(prod1.getUni_med_desc()));
				}else { fila_prod.setVariable("{uni_med}"	, Constantes.SIN_DATO); }
				if(prod1.getCod_sap()!=null && !prod1.getCod_sap().equals("null")){
					fila_prod.setVariable("{cod_sap}"	, String.valueOf(prod1.getCod_sap()));
				}else { fila_prod.setVariable("{cod_sap}"	, Constantes.SIN_DATO); }
								
				fila_prod.setVariable("{tipo}"		, FormatoEstados.frmEstado(listTipos,prod1.getGenerico()));
				fila_prod.setVariable("{est_prod}"	, FormatoEstados.frmEstado(listEstados,prod1.getEstado()));
				fila_prod.setVariable("{cod_cat}"	, String.valueOf(id_cat));
				fila_prod.setVariable("{tipo_prod}"	, prod1.getGenerico());
				productos.add(fila_prod);
				//id_cat_padre = cat1.getId_cat_padre();
			}
		}
		catch(Exception ex){
			logger.debug("cate 4.2.1 "+ex.getMessage());//ex.printStackTrace();
		}

		top.setVariable("{id_cat_padre}", id_cat_padre+"");
		//top.setVariable("{cod_cat}",id_cat+"");
	
		//arbol de categorias
		char est = 'A';
		ArrayList arbCategorias = new ArrayList();
		CategoriasCriteriaDTO criterioArbolCat = new CategoriasCriteriaDTO(pag, est, ' ', regsperpage, true,
				"", "", sel_cat);
		try{
			List listArbCate = bizDelegate.getCategoriasNavegacion(criterioArbolCat, id_cat_padre);
	
			logger.debug("arbCategorias -> "+listArbCate.size());
	
			for (int i = 0; i< listArbCate.size(); i++){
				IValueSet fila_cat = new ValueSet();
				CategoriasDTO cat1 = (CategoriasDTO)listArbCate.get(i);
				fila_cat.setVariable("{cat_id}", String.valueOf(cat1.getId_cat()));
				fila_cat.setVariable("{cat_nombre}"	, String.valueOf(cat1.getNombre()));
				if (!sel_cat.equals("-") && !sel_cat.equals("-1") && sel_cat.equals(String.valueOf(cat1.getId_cat()))){
					fila_cat.setVariable("{sel_cat}","selected");
				}else if(id_cat!=0 && id_cat==(new Integer(String.valueOf(cat1.getId_cat()))).intValue())
					fila_cat.setVariable("{sel_cat}","selected");
				else
					fila_cat.setVariable("{sel_cat}","");		

				arbCategorias.add(fila_cat);
				
			}
		}catch(Exception ex){
			logger.debug("cate 4.2.1"+ex.getMessage());
		}
		
		
		
		//		 5 Paginador
		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getProductosCountByCriteria(criterio);
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug("tot_reg:"+tot_reg);
		logger.debug("total_pag:"+total_pag);
		
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
	    top.setVariable("{action}",action);
	    top.setVariable("{tipo_sap}",tipo_sap);
	    top.setVariable("{cod_sap}",cod_prod+cod_prod_sap);
	    top.setVariable("{descrip}"   , descrip);
	    top.setVariable("{sel_cat}"   , sel_cat);
	    top.setVariable("{tipo}", String.valueOf(tipo));
	    top.setVariable("{estado}", String.valueOf(estado)); 
        //RR
        top.setVariable("{id_cat}", String.valueOf(id_cat));
        
		
		// 6. Setea variables del template
		// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
	    
		//7. Setea variables main del template 
	    top.setDynamicValueSets("PAGINAS", pags);
	    top.setDynamicValueSets("PRODUCTOS", productos);
	    top.setDynamicValueSets("TIPOS", tipos);
	    top.setDynamicValueSets("EST_PROD", estados);
	    top.setDynamicValueSets("CATEGORIAS", arbCategorias);
		
		// 8. Setea variables bloques		
		String result = tem.toString(top);

		salida.setHtmlOut(result);
		salida.Output();		
	}
}

