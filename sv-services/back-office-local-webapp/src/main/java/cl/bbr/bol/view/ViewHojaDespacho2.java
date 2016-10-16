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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.DetallePickingEntity;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.FaltanteDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega la hoja de despacho 2
 * @author BBR
 */
public class ViewHojaDespacho2 extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {		
		String	param_id_pedido = "";
		long 	id_pedido		= -1;
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		param_id_pedido	= req.getParameter("id_pedido");
		id_pedido = Long.parseLong(param_id_pedido);	
		logger.debug("id_pedido: " + param_id_pedido);

        // 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
//20121008VMatheu Carga del parmetro nombre cliente en la hoja de despacho
		DespachoDTO despacho = bizDelegate.getDespachoById(id_pedido);
		ClientesDTO cliente = new ClientesDTO();
		try{
			cliente = bizDelegate.getClienteByCriterio(despacho.getCliente().getRut());
		}catch (Exception e){
			cliente.setNombre(despacho.getNom_cliente());
		}
		top.setVariable("{nombre_cliente}",cliente.getNombre());
//-20121008VMatheu	
		//4.1 Detalle de Productos
		//List listaproductos = bizDelegate.getDetPickingByIdPedido(id_pedido);

		List listaproductos = bizDelegate.getDetPickingHojaDesp2(id_pedido);
		DetallePickingEntity dpick = null;

        IValueSet fila_bin = null; //Contenido del BIN
        List productos = null;
        List bins = new ArrayList(); //Listado de BINS
        String cod_bin = "";
        for (int j=0; j < listaproductos.size(); j++) {
            dpick = null;
            dpick = (DetallePickingEntity) listaproductos.get(j);
            
			IValueSet fila_producto = new ValueSet();
			
		    if (cod_bin.equals("") || !cod_bin.equals(dpick.getCod_bin())){
		        cod_bin = dpick.getCod_bin();
		        fila_bin = new ValueSet();
		        fila_bin.setVariable("{cod_bin}", dpick.getCod_bin());
		        productos = new ArrayList();
		    }
			
			logger.debug("id_prod:"+dpick.getId_producto());
			ProductosSapDTO prodSap = null;
			if (dpick.getId_producto() > 0){
				prodSap = bizDelegate.getProductosSapById(dpick.getId_producto());
			}
		    
			fila_producto.setVariable("{cod_bin}"	,String.valueOf(dpick.getCod_bin()));
			//fila_producto.setVariable("{codigo}"	,String.valueOf(dpick.getId_detalle())+"-"+String.valueOf(dpick.getCod_prod()));
			fila_producto.setVariable("{codigo}"	,String.valueOf(dpick.getCod_prod())+"-"+String.valueOf(dpick.getUni_med()));
			fila_producto.setVariable("{descripcion}",dpick.getDescripcion());
			fila_producto.setVariable("{cant}"		,String.valueOf(dpick.getCant_pick()));			
			
			if(prodSap!=null && prodSap.getUn_base()!=null){
			    fila_producto.setVariable("{u_med}"	,prodSap.getUn_base());
			}else{
			    fila_producto.setVariable("{u_med}"	,Constantes.SIN_DATO);
			}
			fila_producto.setVariable("{prec_unit}"	,String.valueOf(dpick.getPrecio()));
			productos.add(fila_producto);
			
			
            //Si el BIN siguiente es diferente agrega el listado de Productos del BIN Actual
	        if ((j+1) < listaproductos.size() && !cod_bin.equals(((DetallePickingEntity) listaproductos.get(j+1)).getCod_bin())){
	            fila_bin.setDynamicValueSets("BIN", productos);
	            bins.add(fila_bin);
	        }else if((j+1) >= listaproductos.size()){
	            fila_bin.setDynamicValueSets("BIN", productos);
	            bins.add(fila_bin);
	        }
        }
		
		
        // 4.3.1 sustitutos
		List sustheads = new ArrayList();
		// SKP: Se cambia por la que viene de la versión de Paris
		// listaSustitutos = bizDelegate.getSustitutosByPedidoId(id_pedido); 
		List listaSustitutos = bizDelegate.productosEnviadosPedidoForEmail(id_pedido); 
		ArrayList susts = new ArrayList();
		logger.debug("numero de sustitutos:" + listaSustitutos.size());
		for (int i=0; i<listaSustitutos.size(); i++){			
			IValueSet fila = new ValueSet();
			SustitutoDTO s = (SustitutoDTO )listaSustitutos.get(i);
			
			if ((s.getCod_prod1()!=null) && (s.getUni_med1()!=null) && !sustheads.contains(s.getCod_prod1()+s.getUni_med1())) {
                sustheads.add(s.getCod_prod1()+s.getUni_med1());
				fila.setVariable("{cod_prod1}"		,s.getCod_prod1());
				fila.setVariable("{uni_med1}"		,s.getUni_med1());			
				fila.setVariable("{descr1}"	        ,s.getDescr1());
				fila.setVariable("{cant1}"			,String.valueOf(s.getCant1()));
				fila.setVariable("{obs1}"			,s.getObs1());	
			} else {
				fila.setVariable("{cod_prod1}"		,"");
				fila.setVariable("{uni_med1}"		,"");			
				fila.setVariable("{descr1}"	        ,"");
				fila.setVariable("{cant1}"			,"");
				fila.setVariable("{obs1}"			,"");
            }
			
			if (s.getCod_prod2()!=null) {
				fila.setVariable("{cod_prod2}", s.getCod_prod2());
			}else{
				fila.setVariable("{cod_prod2}", "");
			}
			if (s.getUni_med2() != null) {
				fila.setVariable("{uni_med2}", s.getUni_med2());
			}else{
				fila.setVariable("{uni_med2}", "");
			}
            fila.setVariable("{criterio_sustitucion}", s.getDescCriterio());
            
			fila.setVariable("{descr2}", s.getDescr2());
			fila.setVariable("{cant2}", String.valueOf(s.getCant2()));			
			susts.add(fila);
		}
		List prodSustit = null;
		if (susts != null && susts.size() > 0){
			IValueSet sustitutos = new ValueSet(); 
			sustitutos.setDynamicValueSets("SUSTITUTOS", susts);
			prodSustit = new ArrayList();
			prodSustit.add(sustitutos);
		}
		
		
		//4.3 Detalle Producto Faltantes
		List listafaltantes = bizDelegate.getFaltantesByPedidoId(id_pedido);
		ArrayList falt = new ArrayList();
		logger.debug("numero de faltantes:" + listafaltantes.size());
		for ( int i =0; i <listafaltantes.size();i++){
			IValueSet fila = new ValueSet();
			FaltanteDTO falt1 = (FaltanteDTO)listafaltantes.get(i);
			//solo se muestran los faltantes con cantidad > 0.1
			logger.debug("cod:"+falt1.getCod_producto()+", cant_falt:"+falt1.getCant_faltante());
			if(falt1.getCant_faltante() > 0.1){
				fila.setVariable("{codigo}"		, falt1.getCod_producto());
				fila.setVariable("{descripcion}", falt1.getDescripcion());
				fila.setVariable("{cant}"		, String.valueOf(falt1.getCant_faltante()));
				fila.setVariable("{u_med}"		, falt1.getUni_med());	
				fila.setVariable("{prec_unit}"	, String.valueOf(falt1.getPrecio()));
                fila.setVariable("{criterio_sustitucion}" , falt1.getDescCriterio());
				falt.add(fila);
			}
		}
		List prodFalt = null;
		if (falt != null && falt.size() > 0){
			IValueSet faltantes = new ValueSet(); 
			faltantes.setDynamicValueSets("FALTANTES", falt);
			prodFalt = new ArrayList();
			prodFalt.add(faltantes);
		}

		
		
		// 5. Setea variables del template
		//top.setVariable("{id_pedido}"	, String.valueOf(despacho.getId_pedido()));
        top.setVariable("{id_pedido}"   , String.valueOf(id_pedido));
        
		// 6. Setea variables bloques		
		top.setDynamicValueSets("BINS", bins);
		

		//prodFalt.
		if (falt.size() > 0){
		    top.setDynamicValueSets("productos_faltantes", prodFalt);
		}else{
		    top.setDynamicValueSets("productos_faltantes", null);
		}
		if(susts.size() > 0){
		    top.setDynamicValueSets("productos_sustitutos", prodSustit);
		}else{
		    top.setDynamicValueSets("productos_sustitutos", null);
		}
		
		
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
