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
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que despliega productos sugeridos
 * @author BBRI
 */
public class ViewProdSugIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_prod=0;
		String paramId="";
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
			
		logger.debug("1");
		if ( req.getParameter("id_prod") != null ){
			logger.debug("2 id_prod:"+req.getParameter("id_prod"));
			id_prod =  Long.parseLong(req.getParameter("id_prod"));
			logger.debug("3");
			paramId = req.getParameter("id_prod");
		}
		
		if ( req.getParameter("id_producto") != null ){
			id_prod =  Long.parseLong(req.getParameter("id_producto"));
			paramId = req.getParameter("id_producto");
		}
		
		
		logger.debug("Este es el id_prod que viene:" + id_prod);
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
		List lst_sug = null;

		lst_sug =  bizDelegate.getProductosById(id_prod).getLista_sugeridos();
			ArrayList items = new ArrayList();
			String prod_descr;
			int largo = 0;
			for (int i = 0; i < lst_sug.size(); i++) {			
				IValueSet fila_tip = new ValueSet();
				ProductoEntity tip1 = (ProductoEntity)lst_sug.get(i);
				fila_tip.setVariable("{sug_sug}", String.valueOf(tip1.getId()));
				prod_descr ="";				
				if (tip1.getTipo()!=null)		 prod_descr +=" "+tip1.getTipo();
				logger.debug("Tipo suge...: "+ tip1.getTipo());
				if (tip1.getNom_marca()!=null)	 prod_descr +=" "+tip1.getNom_marca();
				logger.debug("marca suge...: "+ tip1.getNom_marca());
				if (tip1.getDesc_corta()!=null) prod_descr +=" "+tip1.getDesc_corta();
				logger.debug("desc corta suge...: "+ tip1.getDesc_corta());
				if (prod_descr.length() > 40)
					largo = 40;
				else
					largo = prod_descr.length();
				
				fila_tip.setVariable("{sug_sug}"	, String.valueOf(tip1.getId()));				
				fila_tip.setVariable("{sug_des}"	, prod_descr.substring(0,largo));
				fila_tip.setVariable("{sug_id}", String.valueOf(tip1.getId_padre()));
				fila_tip.setVariable("{sug_dir}", Formatos.frmSugDir(String.valueOf(tip1.getDesc_larga())));
				fila_tip.setVariable("{id_prod}"	, String.valueOf(paramId) );
				items.add(fila_tip);
			}		
			
			if (lst_sug.size() == 0){
				top.setVariable("{mensaje}", "No existen Productos sugeridos asociados" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
			
		top.setVariable("{mns}", mns );
		// 6. Setea variables bloques
		if ( rc.equals(Constantes._EX_PROD_SUG_IGUAL_PROD) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto sugerido debe ser distinto al producto');</script>" );
		}
		if ( rc.equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código del producto no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_PROD_SUG_GENERICO) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('No se permite sugerir un producto genérico');</script>" );
		}
		if ( rc.equals(Constantes._EX_PROD_SUG_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código del producto sugerido ingresado no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_PROD_SUG_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto seleccionado ya existe como sugerido');</script>" );
		}
	    top.setDynamicValueSets("SUGS", items);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
