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
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ProductosRelacionadosPromoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ResumenPedidoPromocionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * iframe que despliega los productos del pedido
 * @author BBRI
 */
public class ViewPedidoProdIframe extends Command{
	private final static long serialVersionUID = 1;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_pedido=0;
		long id_est=0;
		String rc = "";
		int largobs=0;
		boolean edicion = false;
		
		View salida = new View(res);
		
		logger.debug("User: " + usr.getLogin());
		
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		//2. Procesa parámetros del request
		
		if ( req.getParameter("id_pedido") != null ){
			id_pedido =  Long.parseLong(req.getParameter("id_pedido"));
		}
		
		logger.debug("Este es el id_pedido que viene:" + id_pedido);
		
				
		
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);

//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		
		// Modo de edición
		if ( usr.getId_pedido() == id_pedido ){
			edicion = true;
		}
		
		
		// 4.0 Bizdelegator 
		
		
		BizDelegate bizDelegate = new BizDelegate();	
		
		List pedpromo= bizDelegate.getPromocionPedidos(id_pedido);
		
		List listaProductos = bizDelegate.getProductosPedidosById(id_pedido);
		id_est = bizDelegate.getPedidosById(id_pedido).getId_estado();

			ArrayList prod = new ArrayList();
			String seccion = "";
			
			//contador de observaciones
			int cont_obs = 0;
            double total = 0;
			
			for (int i=0; i<listaProductos.size(); i++){			
				IValueSet fila = new ValueSet();
				ProductosPedidoDTO producto1 = (ProductosPedidoDTO)listaProductos.get(i);
				
				fila.setVariable("{id_prod}", String.valueOf(producto1.getId_producto()));
				fila.setVariable("{id_prod_fo}", String.valueOf(producto1.getId_prod_fo()));
				fila.setVariable("{id_pedido}", String.valueOf(producto1.getId_pedido()));
				fila.setVariable("{cod_prod}", producto1.getCod_producto());
				logger.debug("substr cat_sap: " + producto1.getId_catprod().substring(0,2));
				seccion = producto1.getId_catprod().substring(0,2);
				String nomCat = bizDelegate.getCatSapById(seccion);
				
				if (nomCat != null){
					fila.setVariable("{seccion}", nomCat.replaceAll("/", " / "));
				}else{
					fila.setVariable("{seccion}", seccion.replaceAll("/", " / "));
				}
				
				if(producto1.getSector()!=null && !producto1.getSector().equals("")){
					fila.setVariable("{sec_pick}", producto1.getSector());
				}else{
					fila.setVariable("{sec_pick}", "");
				}
				
				fila.setVariable("{descripcion}", producto1.getDescripcion() + "");
				fila.setVariable("{cant_ped}"   , String.valueOf(producto1.getCant_solic()));
				fila.setVariable("{cant_pick}"  , String.valueOf(producto1.getCant_pick()));
				fila.setVariable("{cant_faltan}", String.valueOf(producto1.getCant_faltan()));
				fila.setVariable("{cant_spick}" , String.valueOf(producto1.getCant_spick()));
				fila.setVariable("{u_med}"      , producto1.getUnid_medida());
				fila.setVariable("{precio}"     , Formatos.formatoPrecio(producto1.getPrecio()));
								
                total += (producto1.getCant_solic()*producto1.getPrecio());
                
				if (producto1.getObservacion().compareTo("null") != 0 ){	
								
					largobs = producto1.getObservacion().length();
					if(largobs > 0){
						cont_obs++;
						fila.setVariable("{obs}"	,"<A NAME='obs"+cont_obs+"'><a href='#obs"+cont_obs+"'><img src='img/book.gif' border='0' width='20' title='"+producto1.getObservacion()+"'></a>");
					}else{
						fila.setVariable("{obs}"	,Constantes.SIN_DATO);
						//fila.setVariable("{obs}"	,"<img src='img/book.gif' border='0' width='20' alt='"+producto1.getObservacion()+"'>");
					}
					
					logger.debug("Largo del campo observaciones _: " + largobs);
					if (largobs <= 100){
						fila.setVariable("{observaciones}"	,String.valueOf(producto1.getObservacion().substring(0, largobs )));
					}
					if (largobs > 100 && largobs <= 200){
						fila.setVariable("{observaciones}"	, String.valueOf(producto1.getObservacion().substring(0, 100 ))+"\n"+String.valueOf(producto1.getObservacion().substring(100, largobs )));
					}
					if (largobs > 200 && largobs <= 255){
						fila.setVariable("{observaciones}"	, String.valueOf(producto1.getObservacion().substring(0, 100 ))+
								"\n"+String.valueOf(producto1.getObservacion().substring(100, 200))+
								"<br>"+String.valueOf(producto1.getObservacion().substring(200, largobs)));
					}				
				}
				else{
					fila.setVariable("{obs}"	,"");
					fila.setVariable("{observaciones}"	,"");
				}
                String imgSustitutos = "sustitutos.gif";
                if ( producto1.getIdCriterio() != 1 ) {
                    imgSustitutos = "distinto_sustitutos.gif";
                }
                fila.setVariable("{sustituto}"    ,"<img src='img/" + imgSustitutos + "' border='0' title='Sustitución sugerida: "+ producto1.getDescCriterio() +"'>");
                
				fila.setVariable("{id_detalle}"		,String.valueOf(producto1.getId_detalle()));
				fila.setVariable("{id_prod}"		,String.valueOf(producto1.getId_producto()));
				fila.setVariable("{mod}"	    	,"");
				//logger.debug("Modo viene con valor: " + modo);
				//if ( edicion && id_est != 0 && (id_est <= Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION)) {
				if ( edicion && id_est != 0 && (id_est <= Constantes.ID_ESTAD_PEDIDO_VALIDADO)) {
					fila.setVariable("{com1}", "");
					fila.setVariable("{com2}", "");
					//logger.debug("MODO:EDICION");
				}
				else {
					fila.setVariable("{com1}", "<!-- ");
					fila.setVariable("{com2}", " -->");
					//logger.debug("MODO:VER");
				}
				
				boolean prod_en_promocion=false;
				//revisa si el producto esta en alguna promocion
				for (int j=0;j<pedpromo.size();j++){
					ResumenPedidoPromocionDTO promocion =(ResumenPedidoPromocionDTO) pedpromo.get(j);
					
					List lst_productos = promocion.getProd_relacionados();
					for(int k=0;k<lst_productos.size();k++){
						ProductosRelacionadosPromoDTO prod_prom=(ProductosRelacionadosPromoDTO)lst_productos.get(k); 
						if (prod_prom.getId_producto()==producto1.getId_producto()){
							logger.debug("El producto "+producto1.getId_producto()+" tiene promocion:"+promocion.getPromo_codigo());
							prod_en_promocion=true;
						}
					}
				}
					
				if(prod_en_promocion){	
					fila.setVariable("{msg_promociones}"
							,"El producto pertenece a una promoción, deberá RECALCULAR promociones obligatoriamente. ");
				}
				else {
					fila.setVariable("{msg_promociones}","");
				}
				
				prod.add(fila);
			}
			
			if (listaProductos.size() == 0){
				top.setVariable("{mensaje}", "No existen Productos asociados" );
			}
			else{
				top.setVariable("{mensaje}", "" );
			}
			
			if ( rc.equals(Constantes._EX_PSAP_ID_NO_EXISTE) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de producto ingresado no existe');</script>" );
			}else if ( rc.equals(Constantes._EX_PSAP_SECTOR_NO_EXISTE) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto no tiene sector asociado');</script>" );
			}else if ( rc.equals(Constantes._EX_OPE_PRECIO_NO_EXISTE) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto no tiene precio asociado');</script>" );
			}else if ( rc.equals(Constantes._EX_OPE_CODBARRA_NO_EXISTE) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto no tiene código de barra asociado');</script>" );
			}else if ( rc.equals(Constantes._EX_PROD_DESPUBLICADO) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El producto tiene estado Despublicado');</script>" );
			}else if ( rc.equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
				top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El id de producto web no existe');</script>" );
			}else {
				top.setVariable( "{mns}", "");
			}
			top.setVariable("{precio_tot}",""+Formatos.formatoPrecio(total));
	
			
		// 6. Setea variables bloques
		
			top.setDynamicValueSets("LST_PROD", prod);

	    String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}
}
