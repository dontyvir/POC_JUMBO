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
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;
/**
 * iframe que despliega los productos del pedido
 * @author BBRI
 */
public class ViewCotizProdIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_cot=0;
		int mod = 0;
		String rc = "";
		long estado = 0;
		String origen="0";
		double margen_min_emp = 0;
		double dscto_max_emp = 0;

		
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
				
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
			
		if ( req.getParameter("cot_id") != null ){
			id_cot =  Long.parseLong(req.getParameter("cot_id"));
		}
		logger.debug("mod: " + req.getParameter("mod"));
		if ( req.getParameter("mod") != null ){
			mod =  Integer.parseInt(req.getParameter("mod"));
		}
		if ( req.getParameter("rc") != null ){
			rc = req.getParameter("rc");
		}
		if ( req.getParameter("estado") != null ){
			estado = Long.parseLong(req.getParameter("estado"));
		}
		if ( req.getParameter("origen") != null ){
			origen = req.getParameter("origen");
		}
		if ( req.getParameter("mar_min_emp") != null ){
			margen_min_emp = Double.parseDouble(req.getParameter("mar_min_emp"));
		}
		if ( req.getParameter("dscto_max_emp") != null ){
			dscto_max_emp = Double.parseDouble(req.getParameter("dscto_max_emp"));
		}
		logger.debug("Este es el id_cot que viene:" + id_cot);
		logger.debug("Este es el mod que viene:" + mod);
		logger.debug("Este es el estado que viene:" + estado);
		logger.debug("Este es el origen que viene:" + origen);
		logger.debug("Este es el margen minimo de la empresa que viene:" + margen_min_emp);
		logger.debug("Este es el descuento máximo de la empresa que viene:" + dscto_max_emp);

//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		
		// Modo de edición
		//if ( usr.getId_pedido() == id_pedido ){
			//edicion = true;
		//}
		
		
		// 4.0 Bizdelegator 
		
		
		BizDelegate bizDelegate = new BizDelegate();	
		
		List listaProductos = null;
		//Si el estado es ingresada no debe mostrar la lista de  productos
		if(estado != Constantes.ID_EST_COTIZACION_INGRESADA){
			listaProductos =  bizDelegate.getProductosCotiz(id_cot);
		

			ArrayList producto = new ArrayList();
			
			for (int i=0; i<listaProductos.size(); i++){			
				IValueSet fila = new ValueSet();
				ProductosCotizacionesDTO prod = new ProductosCotizacionesDTO();
				prod = (ProductosCotizacionesDTO)listaProductos.get(i);
				fila.setVariable("{i}"	         ,String.valueOf(i));
				if (mod == 1)
					fila.setVariable("{img_basurero}","<img src='img/trash.gif' border=0 >");
				else
					fila.setVariable("{img_basurero}","");
				fila.setVariable("{mod}", String.valueOf(mod) );
				//logger.debug("reemplaza ");
				fila.setVariable("{num_cot}"	 ,String.valueOf(id_cot));
				fila.setVariable("{cot_detid}"	 ,String.valueOf(prod.getDetcot_id()));
				fila.setVariable("{id_prod}"	 ,String.valueOf(prod.getDetcot_proId()));
				fila.setVariable("{cod_sap}"	 ,prod.getDetcot_codSap());
				fila.setVariable("{uni_med}"	 ,prod.getDetcot_umed());
				fila.setVariable("{desc}"		 ,prod.getDetcot_desc());
				fila.setVariable("{cant_prod}"	 ,String.valueOf(prod.getDetcot_cantidad()));
				fila.setVariable("{precio_unit}" ,String.valueOf(prod.getDetcot_precio()));
				fila.setVariable("{dcto}"		 ,String.valueOf(prod.getDetcot_dscto_item()));
				fila.setVariable("{precio_lista}",String.valueOf(prod.getDetcot_precio_lista()));
				fila.setVariable("{obs}"		 ,prod.getDetcot_obs());
				fila.setVariable("{costo_unit}"		,String.valueOf(prod.getPre_costo()));
				
				producto.add(fila);
			}
			
			if (listaProductos.size() == 0){
				top.setVariable("{mensaje}", "No existen Productos asociados" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
			top.setDynamicValueSets("PROD_COT", producto);	
		}
		
			if ( rc.equals(Constantes._EX_COT_PROD_PRECIO_CERO) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto ingresado tiene precio 0.');</script>" );
			}else if( rc.equals(Constantes._EX_COT_CODBARRA_NO_EXISTE) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de barras no existe.');</script>" );
			}else if ( rc.equals(Constantes._EX_COT_PROD_DESPUBLICADO) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto esta despublicado.');</script>" );
			}else if ( rc.equals(Constantes._EX_COT_PROD_ID_NO_EXISTE) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto no existe.');</script>" );
			}
			else{
				top.setVariable("{mns}", "" );
			}
		// 5. variables globales
		top.setVariable( "{num_cot}"	 ,String.valueOf(id_cot));
		top.setVariable( "{mar_min_emp}"	 ,String.valueOf(margen_min_emp));
		top.setVariable( "{dscto_max_emp}"	 ,String.valueOf(dscto_max_emp));
		top.setVariable( "{origen}"	 ,String.valueOf(origen));
		// 6. Setea variables bloques
		
			

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
