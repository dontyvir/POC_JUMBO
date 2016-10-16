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
import cl.bbr.jumbocl.contenidos.dto.ProductoCategDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que contiene las categorias de los productos
 * @author BBRI
 */
public class ViewCatProdIframe extends Command{
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int id_categoria=0;
		String mns = "";
		String rc = "";

		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("categoria_id") != null ){
			id_categoria =  Integer.parseInt(req.getParameter("categoria_id"));
		} else if ( req.getParameter("id_cat") != null ){
			id_categoria =  Integer.parseInt(req.getParameter("id_cat"));
		}
		logger.debug("Este es el id de categoria que viene" + id_categoria);
		if ( req.getParameter("mns") != null ){
			mns = "<script language='JavaScript'>alert('"+req.getParameter("mns")+"');</script>";
		}
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		logger.debug("mns:"+mns);
		
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();	
		IValueSet top = new ValueSet();
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();	
		List listprod = null;
		logger.debug("4.3.0");
			listprod =  bizDelegate.getProductosByCategId(id_categoria);
			logger.debug("4.3.1");
		
			ArrayList datos = new ArrayList();
			String prod_descr;
			int largo = 0;			
		for (int i = 0; i < listprod.size(); i++) {			
				IValueSet fila = new ValueSet();
				ProductoCategDTO prod = (ProductoCategDTO)listprod.get(i);
				fila.setVariable("{procat_id}"	, String.valueOf(prod.getId()));
				fila.setVariable("{cod_prod}"	,String.valueOf(prod.getId_prod()));	
				
				prod_descr ="";	
				
				if (prod.getTipo_prod()!=null)		 prod_descr +=" "+ prod.getTipo_prod();
				if (prod.getNom_marca()!=null)	 prod_descr +=" "+prod.getNom_marca();
				if (prod.getNom_prod()!=null) prod_descr +=" "+prod.getNom_prod();
				if (prod_descr.length() > 40)
					largo = 40;
				else
					largo = prod_descr.length();				
				
				
				fila.setVariable("{nom_prod}"	  ,prod_descr.substring(0,largo));
				fila.setVariable("{orden}"	  ,String.valueOf(prod.getOrden()));
				if(!prod.getCon_pago().equals(""))
					fila.setVariable("{con_pago}"	  ,String.valueOf(Formatos.frmConPrecio(prod.getCon_pago())));
				else
					fila.setVariable("{con_pago}"	  ,Constantes.SIN_DATO);
				fila.setVariable("{cate}"	  ,String.valueOf(id_categoria));
				fila.setVariable("{action}" ,"ModProdCatWeb");
				datos.add(fila);
			}		
			
			if (listprod.size() == 0){
				top.setVariable("{mensaje}", "No existen Productos asociados" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
			
	
		//		 5 Paginador
			
		// 6. Setea variables bloques
	    
	    logger.debug("6");
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
	    top.setDynamicValueSets("PRODUCTOS", datos);
	    		

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
