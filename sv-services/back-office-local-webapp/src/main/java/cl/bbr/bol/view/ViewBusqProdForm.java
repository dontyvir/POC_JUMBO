package cl.bbr.bol.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.contenidos.dto.ProductosCbarraDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el detalle del pedido
 * @author BBR
 */
public class ViewBusqProdForm extends Command {
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		int pag;
		int regsperpage;
		char tipo;
		char estado;
		String cod_prod="";
		String cod_prod_sap="";
		String action = "";
		String descrip = "";
		

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
		
		
		//descripcion
		if (action.equals("bus_desc")){
			top.setVariable("{descrip}"   , descrip);
			top.setVariable("{action}"   , action);
		}else{
			top.setVariable("{descrip}"   , "");
		} 
		
	
		//obtener los productos
		ProductosCriteriaDTO criterio = new ProductosCriteriaDTO();
		criterio.setPag(pag);
		criterio.setCodprod(cod_prod);
		criterio.setProdsap(cod_prod_sap);
		criterio.setRegsperpage(regsperpage);
		criterio.setPag_activa(true);
		criterio.setEstado(estado);
		criterio.setTipo(tipo);
		criterio.setDescrip(descrip);
		logger.debug("pag, cod_prod, cod_prod_sap, regsperpage, true, estado, tipo, descrip:"+
				pag+","+ cod_prod+","+cod_prod_sap+","+regsperpage+","+true+","+estado+","+tipo+","+descrip);
		ArrayList productos = new ArrayList();
		
		try{
			List listProd = bizDelegate.getProdCbarraByCriteria(criterio);
	
			logger.debug("listProd:"+listProd.size());
			
			
			String prod_descr;
			int largo=0;
			for (int i = 0; i< listProd.size(); i++){
				logger.debug("entra "+ i + " veces");
				IValueSet fila_prod = new ValueSet();
				ProductosCbarraDTO prod1 = (ProductosCbarraDTO)listProd.get(i);
				
				//la descripcion corta no debe contener comillas simples ni comillas dobles 
				String valor = prod1.getPro_desc_corta().replaceAll("'","").replaceAll("\"","");
				
				fila_prod.setVariable("{valor}"		, prod1.getPro_cod_barra());
				fila_prod.setVariable("{cod_barra}"	, String.valueOf(prod1.getPro_cod_barra()));

				prod_descr ="";				
				if (prod1.getPro_tipo_prod()!=null)		 prod_descr +=" "+prod1.getPro_tipo_prod();
				if (prod1.getPro_desc_corta()!=null) prod_descr +=" "+prod1.getPro_desc_corta();
				if (prod_descr.length() > 100)
					largo = 100;
				else
					largo = prod_descr.length();
				
				fila_prod.setVariable("{prod_desc}"	, prod_descr.substring(0,largo));
				

				//fila_prod.setVariable("{cod_cat}"	, String.valueOf(id_cat));
				productos.add(fila_prod);
				logger.debug("valor = " + valor);
				//id_cat_padre = cat1.getId_cat_padre();
			}	
		}catch(Exception ex){
			logger.debug("err "+ex.getMessage());//ex.printStackTrace();
		}
		// 5.Paginador
		ArrayList pags = new ArrayList();
		double tot_reg = bizDelegate.getCountProdCbarraByCriteria(criterio);
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

		
		
		
		// 6. Setea variables bloques
	    top.setDynamicValueSets("PAGINA", pags);
	    top.setDynamicValueSets("INF_PROD", productos);

		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
	}


}
