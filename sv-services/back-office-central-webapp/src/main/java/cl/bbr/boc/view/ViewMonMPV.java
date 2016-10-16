package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
/**
 * despliega el monitor de productos mix
 * @author BBRI
 */	
public class ViewMonMPV extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		View salida = new View(res);
		int pag;
		int regsperpage;
		String codSapCat = "";
		String codSapProd = "";
		String sel_cat = "";
		String mix_opcion ="";
		String est_pre ="";
		String accion = "";
		String fec_ini = "";
		String fec_fin = "";
		double tot_reg = 0;
		String cod_cat_abuelo = "";
		String tipo_cat = "";
		String mensaje1 = "La consulta no arrojo resultados";
		
		String id_cat="";
		String mns="";
		String rc = "";

		//obtener id_categoria, de la cual se muestran los productos (sea de seccion, rubro, subrubro, etc)
		String id_cat_all = "";
		String selcat = "";
		
		logger.debug("User: " + usr.getLogin());
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
        regsperpage = Integer.parseInt(getServletConfig().getInitParameter("RegsPerPage"));
        logger.debug("RegsPerPage: " + regsperpage);		
        if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		
        //setear parte dinamica en html
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		top.setVariable("{check_1}","checked");
		
		
		//parametros
		if ( req.getParameter("pagina") != null )
			pag =  Integer.parseInt(req.getParameter("pagina")) ;
		else {
			pag = 1;
		}	
		
		if ( req.getParameter("est_mix") != null )
			mix_opcion = req.getParameter("est_mix");
		if ( req.getParameter("est_pre") != null )
			est_pre = req.getParameter("est_pre");
		
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
		
		if ( req.getParameter("cod_cat_abuelo") != null ){
			cod_cat_abuelo = req.getParameter("cod_cat_abuelo");
		}
		if ( req.getParameter("tipo") != null ){
			tipo_cat = req.getParameter("tipo");
		}
		if ( req.getParameter("sel_cat") != null ){
			sel_cat = req.getParameter("sel_cat");
			//id_cat = sel_cat;
		}
		logger.debug("ESTA ES LA CATEGORÍA SELECCIONADA: " + sel_cat);
		if ( req.getParameter("accion") != null ){
			accion = req.getParameter("accion");
		}
		logger.debug("accion:"+accion);
		
		if ( req.getParameter("fec_ini") != null ){
			fec_ini = req.getParameter("fec_ini");
		}
		
		if ( req.getParameter("fec_fin") != null ){
			fec_fin = req.getParameter("fec_fin");
		}
		
		//id_categoria de todos los productos
		if ( req.getParameter("id_cat_all") != null ){
			if(req.getParameter("id_cat_all").indexOf("|")>-1){
				id_cat_all = req.getParameter("id_cat_all").substring(0,req.getParameter("id_cat_all").indexOf("|"));
			}else{
				id_cat_all = req.getParameter("id_cat_all");
			}
		}
		logger.debug("id_cat_all:"+id_cat_all);
		//obtener selcat
		if ( req.getParameter("selcat") != null ){
			selcat = req.getParameter("selcat");
		}
		logger.debug("selcat:"+selcat);
		
		
		if ( req.getParameter("mns") != null ){
			mns = "<script language='JavaScript'>alert('"+req.getParameter("mns")+"');</script>";
		}
		
		logger.debug("mns:"+mns);
		
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();

		logger.debug("cod_cat_abuelo:"+cod_cat_abuelo);
		logger.debug("sel_cat:"+sel_cat);
		
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
				}//else if(!id_cat.equals("") && id_cat.equals(String.valueOf(cat1.getId_cat())))
					//fila_cat.setVariable("{sel_cat}","selected");
				else
					fila_cat.setVariable("{sel_cat}","");		

				arbCategorias.add(fila_cat);
			}


			top.setVariable("{cod_cat_abuelo}",String.valueOf(cod_cat_abuelo));
			
			
			
		
		if (mix_opcion.equals("S")){
			top.setVariable("{sel_si}"   , "selected");
			top.setVariable("{sel_no}"   , "");
		}else if(mix_opcion.equals("N")){
			top.setVariable("{sel_no}"   , "selected");
			top.setVariable("{sel_si}"   , "");
		}
		if (est_pre.equals("S")){
			top.setVariable("{sel_pre_si}"   , "selected");
			top.setVariable("{sel_pre_no}"   , "");
		}else if(est_pre.equals("N")){
			top.setVariable("{sel_pre_no}"   , "selected");
			top.setVariable("{sel_pre_si}"   , "");
		}
			
		//mostrar la ruta de navegacion de las categorias
		String rutaCat = "";
		if(!sel_cat.equals("") && !sel_cat.equals("0")){
			CategoriaSapDTO catAux = bizDelegate.getCategoriaSapById(sel_cat);
			rutaCat = "<a href='ViewMonMPV?accion=navega&tipo=T&sel_cat="+catAux.getId_cat()+"&selcat="+catAux.getId_cat()+"%7CI' color=#EC0035>"+catAux.getDescrip()+"</a>";
			String id_catPadre = catAux.getId_cat_padre();
			while(!id_catPadre.equals("0")){
				catAux = bizDelegate.getCategoriaSapById(id_catPadre);
				rutaCat = "<a href='ViewMonMPV?accion=navega&tipo=T&sel_cat="+catAux.getId_cat()+"&selcat="+catAux.getId_cat()+"%7CI' color=#EC0035>"+catAux.getDescrip()+"</a>" + " >> "+rutaCat ;
				id_catPadre = catAux.getId_cat_padre();
			}
			
		}
		logger.debug("rutaCat:"+rutaCat);
		top.setVariable("{ruta_cat}"   , rutaCat);
		if(!rutaCat.equals("")){
			top.setVariable("{btn_mostrar}"   , "");
		}else{
			top.setVariable("{btn_mostrar}"   , "disabled");
		}

		//listado de productos
		ArrayList productos = new ArrayList();
		
		logger.debug("PAGINA: " + pag);
		logger.debug("MIX OPCION: " + mix_opcion);
		logger.debug("REGSPERPAGE: " + regsperpage);
		logger.debug("CODSAPCAT: "+codSapCat);
		logger.debug("CODSAPPROD: "+codSapProd);			
		logger.debug("SEL_CAT: "+sel_cat);		
		
		
		ProductosSapCriteriaDTO criterio = new ProductosSapCriteriaDTO(pag, mix_opcion, regsperpage, true, 
				codSapProd, codSapCat, sel_cat);
		criterio.setCon_precio(est_pre);
		criterio.setId_cat_all(id_cat_all);
		if(!id_cat_all.equals("")){
			criterio.setCat_sel("");
		}
				
		List listProd = new ArrayList();
		
		//listado inicial y busqueda por codigo sap
		top.setVariable("{sap}"   , "");
		if (accion.equals("")){
			mensaje1 = "Ingrese un criterio de búsqueda";
			top.setVariable("{com_ini}"   , " <!-- ");
			top.setVariable("{com_fin}"   , " --> " );
    	}else{
    		top.setVariable("{com_ini}"   , "");
			top.setVariable("{com_fin}"   , "" );    		
    	}
		
		
		if( accion.equals("bus_sap")){
			top.setVariable("{nom_hid1}","tipo_sap");
		    top.setVariable("{nom_hid2}","sap");
		    if (tipo_sap.equals("cat")){
		    	top.setVariable("{sap}"   , codSapCat );
		    	top.setVariable("{check_1}","checked");
		    	top.setVariable("{check_2}","");
		    }else if (tipo_sap.equals("prod")){
		    	top.setVariable("{sap}"   , codSapProd);
		    	top.setVariable("{check_2}","checked");
		    	top.setVariable("{check_1}","");
		    }
		    
		    if (accion.equals("bus_sap")){
		    	top.setVariable("{accion}"  ,"bus_sap");	
		    }else{
		    	top.setVariable("{accion}"  ,"");
		    }
		    
			criterio.setCat_sel("");
			listProd = bizDelegate.getProductosSapByCriteria(criterio);
	
			//cantidad total de registros
			tot_reg = bizDelegate.getCountProdSapByCriteria(criterio);

		}
		
		//busqueda por arbol de categorias
		if(accion.equals("bus_nav")){
			 top.setVariable("{accion}"  ,"bus_nav");
  			 listProd = bizDelegate.getProductosSapByCriteria(criterio);
			 //cantidad total de registros
			 tot_reg = bizDelegate.getCountProdSapByCriteria(criterio);
		
			 //revisar lo sgte.
			 //cod_cat_abuelo = bizDelegate.getCodCatPadre(cod_cat_abuelo);//Se obtiene la categoria padre
				
			 //top.setVariable("{cod_cat_abuelo}",String.valueOf(cod_cat_abuelo)); // Se parsea para que pueda subir al nivel correspondiente
		}
			
		//Para busqueda de productos por fecha

		if(accion.equals("bus_fecha")){
			if (!fec_ini.equals("") && !fec_fin.equals("")){
			    top.setVariable("{accion}"  ,"bus_fecha");
			    
				listProd = bizDelegate.getProductosSapByCriteriaByDateRange(criterio, 
						Formatos.formatFecha(fec_ini), Formatos.formatFecha(fec_fin));
		
				//cantidad total de registros
				tot_reg = bizDelegate.getCountProdSapByCriteriaByDateRange(criterio,
						Formatos.formatFecha(fec_ini), Formatos.formatFecha(fec_fin));
			    /*
				listProd = bizDelegate.getProductosSapByCriteriaByDateRange(criterio, 
						Formatos.formatFechaHoraIni(fec_ini), Formatos.formatFechaHoraFin(fec_fin));
		
				//cantidad total de registros
				tot_reg = bizDelegate.getCountProdSapByCriteriaByDateRange(criterio,
						Formatos.formatFechaHoraIni(fec_ini), Formatos.formatFechaHoraFin(fec_fin));
				*/
			}else{
				top.setVariable("{fec_ini}"   ,"");
			    top.setVariable("{fec_fin}"   ,"");
			}
		}else{
			top.setVariable("{fec_ini}"   ,"");
		    top.setVariable("{fec_fin}"   ,"");
		}
		
		logger.debug("listProd:"+listProd.size());
		if (listProd.size() < 1  ){
			top.setVariable("{mje1}",mensaje1);
			top.setVariable("{dis}","disabled");
			top.setVariable("{dis_pre}","disabled");
		}else{
			top.setVariable("{mje1}","");
			top.setVariable("{dis}","enabled");
			top.setVariable("{dis_pre}","enabled");
		}		

		if(listProd.size()>0){
			
			for (int i = 0; i< listProd.size(); i++){
				IValueSet fila_prod = new ValueSet();
				ProductosSapDTO prod1 = (ProductosSapDTO)listProd.get(i);
				
				fila_prod.setVariable("{cod_sap}", String.valueOf(prod1.getCod_prod_1()));
				if (prod1.getDes_corta() == null)
					fila_prod.setVariable("{desc}"	, "");
				else
					fila_prod.setVariable("{desc}"	, String.valueOf(prod1.getDes_corta()));
				
				fila_prod.setVariable("{u_med}" 	, prod1.getUni_med());
				
				fila_prod.setVariable("{cat_sap}"	, String.valueOf(prod1.getNom_cat_sap()));
				fila_prod.setVariable("{fec_carga}"	, String.valueOf(Formatos.frmFechaHora(prod1.getFec_carga())));
				fila_prod.setVariable("{mix_web}"   , String.valueOf(Formatos.frmMix(prod1.getFlag_mix())));
				fila_prod.setVariable("{con_pre}"   , String.valueOf(Formatos.frmConPrecio(prod1.getCon_precio())));
				fila_prod.setVariable("{cod_prod}"  , String.valueOf(prod1.getId()));
				//las acciones se muestran segun valor de mix_web: 
				if(prod1.getEstado().equals("1")) {
					if(prod1.getFlag_mix().equals("S")){
						//mostrar enlaces "ver" y "sacar" 
						fila_prod.setVariable("{acciones}", "|<a href=\"javascript:validar_eliminar('¿Desea Sacar?'," +
							"'ModMPVProduct?action=Sacar&cod_prod=" +prod1.getId() +"&cod_prod_sap=" + prod1.getCod_prod_1()+
							"&pagina="+String.valueOf(pag)+"&sel_cat="+sel_cat+"&tipo_sap="+tipo_sap+"&sap="+codSapCat+codSapProd+
							"&fec_ini="+fec_ini+"&fec_fin="+fec_fin+"&accion="+accion+"&est_mix="+mix_opcion+							
							"&url='+escape('ViewMonMPV?pagina="+String.valueOf(pag)+"&sel_cat="+sel_cat+"&tipo_sap="+tipo_sap+
							"&sap="+codSapCat+codSapProd+"&fec_ini="+fec_ini+"&fec_fin="+fec_fin+"&accion="+accion+
							"&est_mix="+mix_opcion+"&est_pre="+est_pre+"'));\">Sacar</a>");
						
						
						
						//modMix(1,"+prod1.getId()+","+prod1.getCod_prod_1()+")
					/*	fila_prod.setVariable("{acciones}", "| <a href=\"javascript:validar_eliminar('¿Desea Sacar?','ModMPVProduct?action=Sacar&cod_prod=" +prod1.getId() +
											"&cod_prod_sap=" + prod1.getCod_prod_1()+"');modMix(1,"+prod1.getId()+
								","+prod1.getCod_prod_1()+");\">Sacar</a>");
						*///fila_prod.setVariable("{acciones}","| <a href=\"javascript:validar_eliminar('¿Desea Sacar?','ModMPVProduct?action=Sacar&cod_prod=" +prod1.getId() +
							//				"&cod_prod_sap=" + prod1.getCod_prod_1()+
											//"&url='+escape('ViewMonMPV?pagina=" +String.valueOf(pag)+"&sel_cat="+sel_cat+"&tipo_sap="+tipo_sap+"&sap="+codSapCat+codSapProd+"&fec_ini="+fec_ini+"&fec_fin="+fec_fin+"&accion="+accion+"&est_mix="+mix_opcion+
								//			"');\">Sacar</a>"); 

						
						
						
					} else if(prod1.getFlag_mix().equals("N")){
						//mostrar enlaces "agregar" y "ver" 
						fila_prod.setVariable("{acciones}","| <a href=\"javascript:validar_eliminar('¿Desea Agregar?'," +
							"'ModMPVProduct?action=Agregar&cod_prod="+prod1.getId()+"&cod_prod_sap="+prod1.getCod_prod_1()+
							"&pagina="+String.valueOf(pag)+"&sel_cat="+sel_cat+"&tipo_sap="+tipo_sap+"&sap="+codSapCat+codSapProd+
							"&fec_ini="+fec_ini+"&fec_fin="+fec_fin+"&accion="+accion+"&est_mix="+mix_opcion+
							"&url='+escape('ViewMonMPV?pagina="+String.valueOf(pag)+"&sel_cat="+sel_cat+"&tipo_sap="+tipo_sap+
							"&sap="+codSapCat+codSapProd+"&fec_ini="+fec_ini+"&fec_fin="+fec_fin+"&accion="+
							accion+"&est_mix="+mix_opcion+"&est_pre="+est_pre+"'));"+
							//"modMix(0,"+prod1.getId()+","+prod1.getCod_prod_1()+");" +
							"\">Agregar</a>"); 

						/*fila_prod.setVariable("{acciones}", " | <a href='javascript:modMix(0,"+prod1.getId()+
								","+prod1.getCod_prod_1()+")'>Agregar</a>");*/						
					}
					
				}else{
					fila_prod.setVariable("{acciones}", "");
				}
				productos.add(fila_prod);
			}
			
		}
				

		ArrayList pags = new ArrayList();
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
	    
	    top.setVariable("{tipo_sap}"	,tipo_sap);
	    top.setVariable("{sap}"		    ,codSapCat+codSapProd);
	    top.setVariable("{fec_ini}"		,fec_ini);
	    top.setVariable("{fec_fin}"		,fec_fin);
	    top.setVariable("{sel_cat}"		,sel_cat);
	    top.setVariable("{num_pag}"		,String.valueOf(pag));
	    top.setVariable("{est_pre}"		,est_pre);
	    top.setVariable("{est_mix}"		,mix_opcion);
	    top.setVariable("{mix_op}"		,mix_opcion);
	    top.setVariable("{pagina}"		,String.valueOf(pag));
		top.setVariable("{mns}", mns );
		top.setVariable("{id_cat_all}", id_cat_all );
		top.setVariable("{selcat}", selcat );
		top.setVariable("{tipo}", tipo_cat );
		
		//Setea variables main del template 
	    top.setDynamicValueSets("PAGINAS", pags);		
		
		top.setDynamicValueSets("MIX", productos);
	    top.setDynamicValueSets("CATEGORIAS", arbCategorias);

	    if ( rc.equals(Constantes._EX_PSAP_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de producto sap no existe');</script>" );
		}
	    if ( rc.equals(Constantes._EX_PSAP_COD_PROD_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de producto en producto sap no existe');</script>" );
		}
	    if ( rc.equals(Constantes._EX_PSAP_DESACTIVADO) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto sap se encuentra desactivado');</script>" );
		}
	    if ( rc.equals(Constantes._EX_OPE_DUP_COD_BARRA) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto tiene codigo de barra duplicado');</script>" );
		}
	    if ( rc.equals(Constantes._EX_OPE_DUP_COD_PRECIO) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto tiene precio duplicado');</script>" );
		}
	    if ( rc.equals(Constantes._EX_OPE_PRECIO_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto no tiene precio. No se Agregó');</script>" );
		}
	    if ( rc.equals(Constantes._EX_OPE_CODBARRA_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto tiene no tiene codigo de barra');</script>" );
		}
	    if ( rc.equals(Constantes.ESTADO_DESPUBLICADO) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto Web tiene estado Despublicado');</script>" );
		}
	    
	    	    
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());		    
	    
		String result = tem.toString(top);
	    
		salida.setHtmlOut(result);
		salida.Output();		
	}
}
