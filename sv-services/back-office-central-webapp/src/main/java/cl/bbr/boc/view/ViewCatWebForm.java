package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CategoriasCriteriaDTO;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega la ficha de la categoria
 * @author BBRI
 */
public class ViewCatWebForm extends Command{
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int id_categoria=0;
		String mns = "";
		String rc = "";
		String action = "";
		long id_cat = -1;
		long id_prod = -1; 
		View salida = new View(res);
		
		//Recupera los paths para las imagenes de los productos
		ResourceBundle rb = ResourceBundle.getBundle("bo");
		String path_img_me = rb.getString("conf.path_prod_img.banner");
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		String html_prod = getServletConfig().getInitParameter("PopUpProd");
		
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
		if ( req.getParameter("categoria_id") != null )
			id_categoria =  Integer.parseInt(req.getParameter("categoria_id"));
		logger.debug("id_categoria: "+id_categoria);
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		if(req.getParameter("action")!=null){
			action = req.getParameter("action");
			logger.debug("action: "+action);
		}
		if(req.getParameter("id_cat")!=null){
			id_cat = Long.parseLong(req.getParameter("id_cat"));
			id_categoria = Integer.parseInt(req.getParameter("id_cat")); 
			logger.debug("id_cat: "+id_cat);
		}
		if(req.getParameter("id_prod")!=null){
			id_prod = Long.parseLong(req.getParameter("id_prod"));
			logger.debug("id_prod: "+id_prod);
		}
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		top.setVariable("{pag_url}", html_prod);
		top.setVariable("{path_banner_intermedia}","");
		BizDelegate bizDelegate = new BizDelegate();	
		
		String cat_tipo = "";
		String cat_estado = "";
		String tipo_cat = "";
//		 4.1 Listado de Categorias

			CategoriasDTO cate = bizDelegate.getCategoriasById(id_categoria);
						
			top.setVariable("{id_cat}"		,String.valueOf(cate.getId_cat()));
			top.setVariable("{desc_corta}"	,String.valueOf(cate.getNombre()));
			top.setVariable("{desc}",(cate.getDescripcion()!= null && !"null".equalsIgnoreCase(cate.getDescripcion()))? String.valueOf(cate.getDescripcion()):"");
			top.setVariable("{tipo}"		,String.valueOf(cate.getTipo()));
			top.setVariable("{estado}"		,String.valueOf(cate.getEstado()));
			top.setVariable("{orden}"		,String.valueOf(cate.getOrden()));
			top.setVariable("{fec_crea}"	,Formatos.frmFecha(String.valueOf(cate.getFec_crea())));
			top.setVariable("{fec_act}"		,Formatos.frmFecha(String.valueOf(cate.getFec_mod())));
			top.setVariable("{url_banner}"	,cate.getUrl_banner());
			tipo_cat=String.valueOf(cate.getTipo());
			if(tipo_cat.equals("I")){
				top.setVariable("{tipocat}"	,"Intermedio");
				path_img_me = rb.getString("conf.path_prod_img.intermedias");				
				if(cate.getBanner()!=null && !cate.getBanner().equals("") && !cate.getBanner().toUpperCase().equals("NULL")){
					top.setVariable("{path_banner_intermedia}"	,rb.getString("conf.path_prod_img.banner")+cate.getBanner());
				}
			}else if(tipo_cat.equals("C")){
                top.setVariable("{tipocat}" ,"Cabecera");
            } else
			{
				top.setVariable("{tipocat}"	,"Terminal");
			}
			
			
			
			//UserDTO usuario = bizDelegate.getUserById(cate.getUser_mod());
			if (cate.getTipo().equals("I")){
				top.setVariable("{icom_i}", " <!-- ");
				top.setVariable("{fcom_i}", " --> ");
				top.setVariable("{icom_t}", "");
				top.setVariable("{fcom_t}", "");
                top.setVariable("{tipo_sub}", "T");
				List subcat = bizDelegate.getCategoriasByCategId(id_categoria);
				if (subcat.size()> 0){
					top.setVariable("{ex_catpro}", "1");								
				}
			}
            if (cate.getTipo().equals("C")){
                top.setVariable("{icom_i}", " <!-- ");
                top.setVariable("{fcom_i}", " --> ");
                top.setVariable("{icom_t}", "");
                top.setVariable("{fcom_t}", "");
                top.setVariable("{tipo_sub}", "I");
                List subcat = bizDelegate.getCategoriasByCategId(id_categoria);
                if (subcat.size()> 0){
                    top.setVariable("{ex_catpro}", "1");                                
                }
            }
			if (cate.getTipo().equals("T")){
				top.setVariable("{icom_t}", " <!-- ");
				top.setVariable("{fcom_t}", " --> ");
				top.setVariable("{icom_i}", "");
				top.setVariable("{fcom_i}", "");
				List prodcat = bizDelegate.getProductosByCategId(id_categoria);
				if (prodcat.size()> 0){
					top.setVariable("{ex_catpro}", "2");								
				}			
			}
			
			if ( req.getParameter("mns") != null ){
				mns = "<script language='JavaScript'>alert('"+req.getParameter("mns")+"');</script>";
			}
			logger.debug("mns:"+mns);
			if (String.valueOf(usr.getNombre()) != null){
				top.setVariable("{usu_act}"		,String.valueOf(usr.getNombre() +" "+ usr.getApe_paterno()+" "+ usr.getApe_materno()));
			}
			else{
				top.setVariable("{usu_act}"	,"");
			}
			
			logger.debug("Baner....:" + cate.getBanner());
			if(cate.getBanner()!=null && !cate.getBanner().equals("") && !cate.getBanner().toUpperCase().equals("NULL")){
				top.setVariable("{imagen}"		, String.valueOf(cate.getBanner()));
				top.setVariable("{img}"		, String.valueOf(cate.getBanner()));
				logger.debug("Traer imagen de BD");
			}else{
				top.setVariable("{imagen}"		, "");
				top.setVariable("{img}"		, String.valueOf("logo2.jpg"));
				logger.debug("Pone imagen por defecto");
			}
			
			if(tipo_cat.equals("I")){
				top.setVariable("{img}", String.valueOf(cate.getImagen()));
			}
			//totem
			logger.debug("totem:"+cate.getTotem());
			if(cate.getTotem()!=null && !cate.getTotem().equals("") && !cate.getTotem().toUpperCase().equals("NULL")){
				top.setVariable("{totem}",cate.getTotem());
			}else{
				top.setVariable("{totem}","");
			}
			
			
			//img/logo2.jpg			
			cat_tipo = cate.getTipo();
			cat_estado = cate.getEstado();
			
            
            //Imagen
            logger.debug("imagen:"+cate.getImagen());
            if(cate.getImagen()!=null && !cate.getImagen().equals("") && !cate.getImagen().toUpperCase().equals("NULL")){
                top.setVariable("{cat_imagen}",cate.getImagen());
            }else{
                top.setVariable("{cat_imagen}","");
            }

		
		//ver tipos de categorias
		ArrayList tipos = new ArrayList();
		List listTipos = bizDelegate.getTiposCategorias();
		logger.debug("tipos de categorias");
		for (int i = 0; i< listTipos.size(); i++){
			IValueSet fila_tip = new ValueSet();
			EstadoDTO tip1 = (EstadoDTO)listTipos.get(i);
			fila_tip.setVariable("{id_tipo}", String.valueOf(tip1.getId_estado()));
			fila_tip.setVariable("{nom_tipo}"	, String.valueOf(tip1.getNombre()));
			
			if (cat_tipo.equals(String.valueOf(tip1.getId_estado()))){
				fila_tip.setVariable("{sel_tip}","selected");
			}
			else
				fila_tip.setVariable("{sel_tip}","");		
			tipos.add(fila_tip);
		}
		logger.debug("size:"+listTipos.size());
		
		//ver estados
		ArrayList estados = new ArrayList();
		List listEstados = bizDelegate.getEstadosByVis("CW","S");
		logger.debug("listEstados");
		
		for (int i = 0; i< listEstados.size(); i++){
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO)listEstados.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}"	, String.valueOf(est1.getNombre()));
			
			if (cat_estado.equals(String.valueOf(est1.getId_estado()))){
				fila_est.setVariable("{sel_est}","selected");
			}
			else
				fila_est.setVariable("{sel_est}","");		
			estados.add(fila_est);
		}
		logger.debug("size:"+listEstados.size());
		
		//arbol de categorias
		ArrayList arbCategorias = new ArrayList();
		CategoriasCriteriaDTO criterioArbolCat = new CategoriasCriteriaDTO(1, 'A', 'T', 10, true,
				"", "", "");

			//List listArbCate = bizDelegate.getCategoriasByCriteria(criterioArbolCat);
			List listArbCate = bizDelegate.getCategoriasNavegacion(criterioArbolCat, -1);
	
			logger.debug("arbCategorias -> "+listArbCate.size());
			for (int i = 0; i< listArbCate.size(); i++){
				IValueSet fila_cat = new ValueSet();
				CategoriasDTO cat1 = (CategoriasDTO)listArbCate.get(i);
				fila_cat.setVariable("{nav_id_cat}", String.valueOf(cat1.getId_cat()));
				fila_cat.setVariable("{nav_nom_cat}"	, String.valueOf(cat1.getNombre()));
				fila_cat.setVariable("{sel_cat}","");		
				arbCategorias.add(fila_cat);
				
			}

		top.setVariable("{mns}", mns );
		if ( rc.equals(Constantes._EX_CAT_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La categoría no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAT_PROD_REL_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La categoria tiene productos relacionados.No puede ser eliminada.');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAT_SUBCAT_REL_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La categoria tiene subcategorias relacionadas.No puede ser eliminada');</script>" );
		}
		
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		//Productos
			top.setVariable("{action}" ,"AddProdCatWeb");
			
		//setea la ruta de imagenes
			top.setVariable("{path_me}"	,path_img_me);
		//5. Paginador		
		
		// 6. Setea variables bloques	
	    top.setDynamicValueSets("TIPO", tipos);
	    top.setDynamicValueSets("ESTADO", estados);
	    top.setDynamicValueSets("SEL_CATEGORIA_NAV", arbCategorias);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
