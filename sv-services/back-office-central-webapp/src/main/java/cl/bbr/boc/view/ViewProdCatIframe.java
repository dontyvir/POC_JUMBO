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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CategoriasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que muestra los productos por categoria
 * @author BBRI
 */
public class ViewProdCatIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_prod=0;
		String mns = "";
		String rc = "";
		String tipo_prod="";

		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
		logger.debug("1");
		if ( req.getParameter("id_prod") != null ){
			logger.debug("2 : "+req.getParameter("id_prod"));
			id_prod =  Long.parseLong(req.getParameter("id_prod"));
			logger.debug("3");
		}
		if ( req.getParameter("tipo_prod") != null ){
			logger.debug("2 : "+req.getParameter("tipo_prod"));
			tipo_prod =  req.getParameter("tipo_prod");
			logger.debug("3");
		}		
		logger.debug("Este es el id_prod que viene" + id_prod);
		if ( req.getParameter("mns") != null ){
			mns = "<script language='JavaScript'>alert('"+req.getParameter("mns")+"');</script>";
		}
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("mns:"+mns);
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();	
		List lstCateg = null;
		
		lstCateg = bizDelegate.getCategoriasByProductId(id_prod);
		ArrayList categs = new ArrayList();
			
		for (int i = 0; i< lstCateg.size(); i++){
			IValueSet fila_log = new ValueSet();
			CategoriasDTO cat1 = (CategoriasDTO)lstCateg.get(i);
			
			fila_log.setVariable("{procat_id}", String.valueOf(cat1.getId_procat()));
			fila_log.setVariable("{id_categ}", String.valueOf(cat1.getId_cat()));
			fila_log.setVariable("{nom_categ}"	, String.valueOf(cat1.getNombre()));
			fila_log.setVariable("{nom_categ_padre}"	, bizDelegate.getNombresCategoriasPadreByIdCat( cat1.getId_cat() ));
			fila_log.setVariable("{orden}"	, String.valueOf(cat1.getOrden()));
			if(!cat1.getCon_pago().equals(""))
				fila_log.setVariable("{con_pago}"	  ,String.valueOf(Formatos.frmConPrecio(cat1.getCon_pago())));
			else
				fila_log.setVariable("{con_pago}"	  ,Constantes.SIN_DATO);
			fila_log.setVariable("{id_prod}"	, String.valueOf(id_prod));
			if (lstCateg.size() <= 1){
				fila_log.setVariable("{direc}","ViewProductForm?cod_prod="+id_prod+"&tipo_prod="+tipo_prod);
				fila_log.setVariable("{targ}","window.parent.location.reload();");
				fila_log.setVariable("{confirmacion}","Atención!! Es la ultima categoria, el producto se Despublicara\n ¿Esta seguro que Desea Eliminar?");
				//fila_log.setVariable("{direc}","ViewProdCatIframe?id_prod"+id_prod);
			}
			else{
				fila_log.setVariable("{direc}","ViewProdCatIframe?id_prod="+id_prod);
				fila_log.setVariable("{targ}","");
				fila_log.setVariable("{confirmacion}"," ¿Desea Eliminar?");				
			}
			fila_log.setVariable("{action}" ,"ModProdCatWeb");
			categs.add(fila_log);
		}
			
		if (lstCateg.size() == 0){
			top.setVariable("{mensaje}", "No existen Categorias asociadas" );
		}else{
			top.setVariable("{mensaje}", "" );
		}
		top.setVariable("{mns}", mns );
		
		if ( rc.equals(Constantes._EX_CAT_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de la categoría no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de producto ingresado no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAT_PROD_REL_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La relacion entre categoria y producto ya existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAT_NO_ES_TERMINAL) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La categoria no es terminal y no se puede asociar al producto.');</script>" );
		}
		if ( rc.equals(Constantes._EX_CAT_PROD_REL_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La relación entre categoría y producto no existen.No se puede eliminar.');</script>" );
		}
		 if ( rc.equals(Constantes._EX_PROD_CAT_NO_TIENE) ){
				logger.debug("Se eliminaron todas las categorias.");
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('Atención!! Se eliminaron todas las Categorías. El Producto se ha Despublicado');</script>" );
				
				
		 }
		 
		 
		
		//		 5 Paginador
		// 6. Setea variables bloques
	    
	    top.setDynamicValueSets("CATEG", categs);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
