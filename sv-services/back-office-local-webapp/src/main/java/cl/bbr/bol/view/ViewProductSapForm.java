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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.CodigosBarraSapDTO;
import cl.bbr.jumbocl.contenidos.dto.PreciosSapDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega productos sap
 * @author mleiva
 */
public class ViewProductSapForm extends Command{
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		View salida = new View(res);
		int cod_prod=0;

		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//parámetros
		if ( req.getParameter("cod_prod") != null )
			cod_prod =  new Integer(req.getParameter("cod_prod")).intValue();
		logger.debug("Id de Producto: " + cod_prod);
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		
		BizDelegate bizDelegate = new BizDelegate();
		
		//obtener el producto
		//try{
		logger.debug("1");
			ProductosSapDTO prod = bizDelegate.getProductosSapById(cod_prod);
			logger.debug("2");
				
			top.setVariable("{cod_prod_1}"		,String.valueOf(prod.getCod_prod_1()));
			top.setVariable("{estado}"			,String.valueOf(prod.getEstado()));
			top.setVariable("{seccion}"			,String.valueOf(prod.getCat_seccion().toUpperCase()));
			top.setVariable("{rubro}"			,String.valueOf(prod.getCat_rubro().toUpperCase()));
			top.setVariable("{subrubro}"		,String.valueOf(prod.getCat_subrubro().toUpperCase()));
			top.setVariable("{grupo}"			,String.valueOf(prod.getCat_grupo().toUpperCase()));
			top.setVariable("{categ}"			,String.valueOf(prod.getNom_cat_sap()));
			top.setVariable("{marca}"			,String.valueOf(prod.getMarca()));
			top.setVariable("{desc_corta}"		,String.valueOf(prod.getDes_corta()));
			top.setVariable("{desc_larga}"		,String.valueOf(prod.getDes_larga()));
			top.setVariable("{uni_med}"			,String.valueOf(prod.getUni_med()));
			top.setVariable("{origen}"			,String.valueOf(prod.getOrigen()));
			top.setVariable("{uni_bas}"			,String.valueOf(prod.getUn_base()));
			top.setVariable("{uni_emp}"			,String.valueOf(prod.getUn_empaque()));
			top.setVariable("{fec_car}"			,String.valueOf(Formatos.frmFechaHora(prod.getFec_carga())));
			top.setVariable("{mix_web}"			,String.valueOf(Formatos.frmMix(prod.getFlag_mix().toUpperCase())));
			
			
			// BARRAS SAP con estadoactivo = 1			
			ArrayList arrBarras = new ArrayList();
			List listabarras = bizDelegate.getCodBarrasByProdId(cod_prod);
			//usa CodigosBarraSapDTO
			for (int i = 0; i< listabarras.size(); i++){
				IValueSet fila_barra= new ValueSet();
				CodigosBarraSapDTO barra = new CodigosBarraSapDTO();
				barra = (CodigosBarraSapDTO) listabarras.get(i);
				
				fila_barra.setVariable("{cb_codigo}",barra.getCod_barra());
				fila_barra.setVariable("{cb_tipo}",barra.getTip_cod_barra());		
				
				arrBarras.add(fila_barra);
			}			
			
			// PRECIOS SAP con estadoactivo = 1 y bloqcompra ='NO'
			ArrayList arrPrecios = new ArrayList();
			List listaprecios = bizDelegate.getPreciosByProdId(cod_prod);
			//usa PreciosSapDTO
			for (int i = 0; i< listaprecios.size(); i++){
				IValueSet fila_precios = new ValueSet();
				PreciosSapDTO precio = new PreciosSapDTO();
				precio = (PreciosSapDTO) listaprecios.get(i);
				
				fila_precios.setVariable("{pr_codlocal}",precio.getCod_local());
				fila_precios.setVariable("{pr_precio}",String.valueOf(precio.getPrec_valor()));						
				
				arrPrecios.add(fila_precios);
			}
		
			top.setDynamicValueSets("listado_precios", arrPrecios);	
			top.setDynamicValueSets("listado_cbarras", arrBarras);	
			
		/*}catch(Exception ex){
			logger.debug("Excepcion:"+ ex);
		}*/

		
		// 6. Setea variables bloques	

		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}
