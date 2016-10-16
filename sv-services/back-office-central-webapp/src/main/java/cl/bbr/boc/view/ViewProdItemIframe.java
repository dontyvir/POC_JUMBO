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
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe con los items del producto
 * @author BBRI
 */
public class ViewProdItemIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_prod=0;
		String paramId="";
		String mns="";
		String rc = "";

		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("id_prod") != null ){
			id_prod =  Long.parseLong(req.getParameter("id_prod"));
			paramId = req.getParameter("id_prod");
		}
		if ( req.getParameter("id_prod_gen") != null ){
			id_prod =  Long.parseLong(req.getParameter("id_prod_gen"));
			paramId = req.getParameter("id_prod_gen");
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
		List lst_items = null;

		lst_items =  bizDelegate.getProductosById(id_prod).getLista_items();
			ArrayList items = new ArrayList();
			String prod_descr;
			int largo=0;
			for (int i = 0; i < lst_items.size(); i++) {			
				IValueSet fila_tip = new ValueSet();
				ProductoEntity tip1 = (ProductoEntity)lst_items.get(i);
				fila_tip.setVariable("{item_id}", String.valueOf(tip1.getId()));
				

				prod_descr ="";				
				if (tip1.getTipo()!=null)		 prod_descr +=" "+tip1.getTipo();
				logger.debug("Tipo items...: "+ tip1.getTipo());
				if (tip1.getNom_marca()!=null)	 prod_descr +=" "+tip1.getNom_marca();
				logger.debug("marca items...: "+ tip1.getNom_marca());
				if (tip1.getDesc_corta()!=null) prod_descr +=" "+tip1.getDesc_corta();
				logger.debug("desc corta items...: "+ tip1.getDesc_corta());
				if (prod_descr.length() > 40)
					largo = 40;
				else
					largo = prod_descr.length();
				
				
				fila_tip.setVariable("{item_des}"	, prod_descr.substring(0,largo));
				fila_tip.setVariable("{item_atr_ver}"	, String.valueOf(tip1.getValor_difer()));
				fila_tip.setVariable("{id_prod}"	, String.valueOf(paramId) );
				items.add(fila_tip);
			}		
			
			if (lst_items.size() == 0){
				top.setVariable("{mensaje}", "No existen Items asociados" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
			
		// 6. Setea variables bloques
		top.setVariable("{mns}", mns );
		if ( rc.equals(Constantes._EX_PROD_ITEM_IGUAL_PROD) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto item debe ser distinto al producto');</script>" );
		}
		if ( rc.equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de producto ingresado no existe');</script>" );
		}
		if ( rc.equals(Constantes._EX_PROD_ITEM_GENERICO) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('No se permite asociar un producto genérico');</script>" );
		}

	    top.setDynamicValueSets("ITEMS", items);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
