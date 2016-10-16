package cl.bbr.bol.view;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/*
 * Posibles parámetros que puede recibir en el get/post:
 * id_pedido	: n° de pedido
 * id_estado	: estado
 * pag 			: página del paginador
 * tipo_fecha	: radio button indicando el tipo del parametro "fecha" (1: Picking; 2: Despacho)
 * fecha		: parametro fecha (de despacho, de picking)
 */
/**
 * Despliega el monitor de pedidos
 * @author mleiva
 */
public class ViewMonitorPedidos extends Command {

	/*
	 * (non-Javadoc)
	 * 
	 * @see cl.bbr.common.framework.Command#Execute()
	 */
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		int pag;
		long id_pedido 	= -1;
		long id_estado	= -1;
		long id_local_fact = -1;
		long id_zona	= -1;
		int regsperpage = 10;

		String param_id_pedido = "-1";
		String param_id_estado = "";
		String param_id_local_fact = "";
		String param_id_zona = "";
		String param_fecha = "";
		String param_fechafin = "";
		String param_tipo_fecha = "";
		String local_tipo_picking = "";

		// 1. Parámetros de inicialización servlet
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter(
				"RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);

		String MsjeError = getServletConfig().getInitParameter("MsjeError");
		logger.debug("MsjeError: "+MsjeError);
		
		String mensaje = "";
		if (req.getParameter("mensaje") != null) {
			mensaje = req.getParameter("mensaje");
		}
		
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		if (req.getParameter("pag") != null) {
			pag = Integer.parseInt(req.getParameter("pag"));
			logger.debug("Pag: " + req.getParameter("pag"));
		} else
			pag = 1; // por defecto mostramos la página 1

		if (req.getParameter("id_pedido") != null) {
			// try{
				param_id_pedido = req.getParameter("id_pedido");
				id_pedido = Long.parseLong(param_id_pedido);
				logger.debug("id_pedido: " + param_id_pedido);
			// }catch (Exception e){
			//	 logger.debug("se cae todo mal!!!");
			// }
		}

		if ((req.getParameter("id_estado") != null) && (!req.getParameter("id_estado").equals(""))) {
			param_id_estado = req.getParameter("id_estado");
			id_estado = Long.parseLong(param_id_estado);			
			logger.debug("id_estado: " + param_id_estado);
		}
		
		if ((req.getParameter("id_local_fact") != null) && (!req.getParameter("id_local_fact").equals(""))) {
			param_id_local_fact = req.getParameter("id_local_fact");
			id_local_fact = Long.parseLong(param_id_local_fact);			
			logger.debug("id_local_fact: " + param_id_local_fact);
		}		

		if ((req.getParameter("id_zona") != null) && (!req.getParameter("id_zona").equals(""))) {
			param_id_zona = req.getParameter("id_zona");
			logger.debug("id_zona param: " + req.getParameter("id_zona"));
			id_zona = Long.parseLong(param_id_zona);			
			logger.debug("id_zona: " + param_id_zona);
		}		
		
		logger.debug("Prueba de contenido de fecha: "+req.getParameter("fecha"));
		if (req.getParameter("fecha") != null) {
			param_fecha = req.getParameter("fecha");
			logger.debug("Fecha: " + param_fecha);
		}
		if (req.getParameter("fechafin") != null) {
			param_fechafin = req.getParameter("fechafin");
			logger.debug("Fecha: " + param_fechafin);
		}else{
			param_fechafin = "";
		}
		if (req.getParameter("tipo_fecha") != null) {
			//try{
				param_tipo_fecha = req.getParameter("tipo_fecha");
				logger.debug("Tipo Fecha: " + param_tipo_fecha);
			//}catch(Exception E) {
			//	logger.debug("se cae, todo mal" );
			//}
		}

		local_tipo_picking = usr.getLocal_tipo_picking();

		logger.debug("Parámetros procesados");

		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();

		// 4.1 Listado de Pedidos

		// creamos los criterios
		PedidosCriteriaDTO criterio = new PedidosCriteriaDTO();

		if (id_pedido != -1)
			criterio.setId_pedido(id_pedido);

		if (id_estado != -1)
			criterio.setId_estado(id_estado);

		if (!param_fecha.equals("") && param_tipo_fecha.equals("picking")){
			criterio.setTip_fec(param_tipo_fecha);
			logger.debug(param_tipo_fecha);
			criterio.setFec_ini(param_fecha);
			criterio.setFec_fin(param_fechafin);
			logger.debug("Parametro Con Fecha de Picking");			
		}	
		else if (!param_fecha.equals("") && param_tipo_fecha.equals("despacho")){
			criterio.setTip_fec(param_tipo_fecha);
			criterio.setFec_ini(param_fecha);
			criterio.setFec_fin(param_fechafin);			
			logger.debug("param_fecha" +param_fecha);
			logger.debug("param_fechafin" +param_fechafin);
			
		}
		logger.debug("Parametro Con Fecha de Picking");
		
		
		criterio.setFiltro_estados( Constantes.ESTADOS_PEDIDO_PICKING ); 
		criterio.setId_local(usr.getId_local());
		criterio.setPag(pag);
		criterio.setRegsperpag(regsperpage);
		criterio.setId_local_fact(id_local_fact);
		criterio.setId_zona(id_zona);
		

		
		//	orden usando criterios

		List ordenarpor = new ArrayList();

		ordenarpor.add(PedidosCriteriaDTO.ORDEN_ZONA_ORDEN+" "+PedidosCriteriaDTO.ORDEN_ASCENDENTE);
		ordenarpor.add(PedidosCriteriaDTO.ORDEN_FECHA_PICK+" "+PedidosCriteriaDTO.ORDEN_ASCENDENTE);
		ordenarpor.add(PedidosCriteriaDTO.ORDEN_HORA_INI_PICK+" "+PedidosCriteriaDTO.ORDEN_ASCENDENTE);
		ordenarpor.add(PedidosCriteriaDTO.ORDEN_CANT_PROD+" "+PedidosCriteriaDTO.ORDEN_ASCENDENTE);
		
		
		criterio.setOrden_columnas(ordenarpor);
		criterio.setTipo_picking(local_tipo_picking);
		
		// Llama al BizDelegator
		logger.debug("ID PEDIDO: " + id_pedido);
		logger.debug("ID ESTADO: " + id_pedido);
		logger.debug("PARAMFECHA: " + param_fecha);
		List listapedidos = new ArrayList();
		if ( id_pedido != -1 || id_estado != -1 || !param_fecha.equals("") ) {
			 listapedidos = bizDelegate.getPedidosByCriteria(criterio);
		} else {
			MsjeError = "Debe ingresar criterio de búsqueda";
		}

		ArrayList datos = new ArrayList();
		String zona_anterior="";
		for (int i = 0; i < listapedidos.size(); i++) {
			IValueSet fila = new ValueSet();
			MonitorPedidosDTO ped1 = (MonitorPedidosDTO) listapedidos.get(i);
			
            String flag = "";
            if (ped1.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE)) {
                flag =" * ";
            }
            
            fila.setVariable("{id_pedido}"		, String.valueOf(ped1.getId_pedido()));
            fila.setVariable("{fl}"      , flag);
            
            /** (+) INDRA 2012-12-12 (+)
             * Alerta Revision Faltante
             */
            if((ped1.getId_estado() == 70)){
 			   fila.setVariable("{imgEstado}","<img src='img/icon41.gif' width='9' height='9'>");
 			   
 		   }else{
 			   fila.setVariable("{imgEstado}","");
 			  
 		   }
            
            //**(-) INDRA 2012-12-12 (-)
            
            
            
            
            if ( ped1.getId_jpicking() > 0 ) {
                fila.setVariable("{n_jornada}"		, String.valueOf(ped1.getId_jpicking()));
            } else {
                fila.setVariable("{n_jornada}"      , "");
            }
            if ( ped1.getFpicking() != null ) {
                fila.setVariable("{f_picking}"		, Formatos.frmFecha( ped1.getFpicking() ));
            } else {
                fila.setVariable("{f_picking}"      , "");    
            }
			if ( ped1.getHini_jpicking() != null && ped1.getHfin_jpicking() != null ) {
			    fila.setVariable("{horario_jor}"	, Formatos.frmHoraSola( ped1.getHini_jpicking() )+" - "+ Formatos.frmHoraSola( ped1.getHfin_jpicking() ));
            } else {
                fila.setVariable("{horario_jor}"    , "");   
            }            
			fila.setVariable("{n_productos}"	, String.valueOf(Formatos.formatoNum3Dec(ped1.getCant_prods())));
			fila.setVariable("{f_despacho}"		, Formatos.frmFecha( ped1.getFdespacho() ));
			fila.setVariable("{h_despacho}"		, Formatos.frmHoraSola( ped1.getHdespacho() ));
			fila.setVariable("{estado}"			, ped1.getEstado());
			fila.setVariable("{local_fact}"		, ped1.getLocal_facturacion());
			fila.setVariable("{zona_des}"		, ped1.getZona_nombre());
			//grosor del borde			
			String zona_actual=ped1.getZona_nombre();
			logger.debug(i+".- zona anterior="+zona_anterior+" zona actual="+zona_actual);
			fila.setVariable("{borderstyle}","");
			if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
				fila.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
			}
			zona_anterior =zona_actual;
			   
			if ((ped1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_INGRESADO  ||
			    ped1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION ||
			    ped1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_VALIDADO   ||
			    ped1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PICKING) && enTiempo(ped1)  ){
					fila.setVariable("{cam_color1}"	, " <FONT color=red> ");
					fila.setVariable("{cam_color2}"	, " </FONT> ");
			}else{
				fila.setVariable("{cam_color1}"	, "");
				fila.setVariable("{cam_color2}"	, "");
			}
			
			if (ped1.getTipo_despacho() == null){
			    ped1.setTipo_despacho("N");
			}
			if (ped1.getTipo_despacho().equals("E")){//Express
			    fila.setVariable("{color_fondo_op}","#F7BB8B");
			}else if (ped1.getTipo_despacho().equals("C")){//Económico
			    fila.setVariable("{color_fondo_op}","#C4FFC4");
			}else if (ped1.getTipo_despacho().equals("R")){//Retiro en Local
                fila.setVariable("{color_fondo_op}","#FFFF99");
            }else{
			    fila.setVariable("{color_fondo_op}","#FFFFFF");
			}

			//mostrar la acción de eliminar el pedido solo para pedidos con estado en pago. 
			if(ped1.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO){
				fila.setVariable("{accion}"		, " | <a href=\"javascript:popUpWindowModal('ViewRechPagoPedido?id_pedido="+ped1.getId_pedido()+"', 100, 100, 530, 250);\" >Rech.Pago</a>");
			}else{
				fila.setVariable("{accion}"		, "");
			}
			
			datos.add(fila);
		}

		// 4.2 Paginador		
		ArrayList pags = new ArrayList();
		double tot_reg = 0;
		if (id_pedido != -1 || id_estado != -1 || !param_fecha.equals("")  ){
			tot_reg = bizDelegate.getCountPedidosByCriteria(criterio);
		}
		if (tot_reg == 0){
			top.setVariable("{dis1}", "disabled");
		}else{
			top.setVariable("{dis1}", "");
		}
		logger.debug("tot_reg: " + tot_reg + "");
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug ("round: " + total_pag);
		if (total_pag == 0){
			total_pag = 1;
		}
		logger.debug("round: " + (double) Math.ceil(tot_reg / regsperpage));

		for (int i = 1; i <= total_pag; i++) {
			IValueSet fila = new ValueSet();
			fila.setVariable("{pag}", String.valueOf(i));
			if (i == pag)
				fila.setVariable("{sel}", "selected");
			else
				fila.setVariable("{sel}", "");
			pags.add(fila);
		}
		//anterior y siguiente
		if( pag >1){
	    	int anterior = pag-1;
	    	top.setVariable("{anterior_label}","<< anterior");
	    	top.setVariable("{anterior}",String.valueOf(anterior));
	    }else if (pag==1){
	    	top.setVariable("{anterior_label}","");
	    }	    
	    if (pag <total_pag){
	    	int siguiente = pag+1;
	    	top.setVariable("{siguiente_label}","siguiente >>");
	    	top.setVariable("{siguiente}",String.valueOf(siguiente));
	    }else{
	    	top.setVariable("{siguiente_label}","");
	    }
		logger.debug("antes de estado");

		// 4.3 Estados
		List listaestados = bizDelegate.getEstadosPedido();

		ArrayList estados = new ArrayList();
		for (int i = 0; i < listaestados.size(); i++) {
			IValueSet fila = new ValueSet();
			EstadoDTO estado1 = new EstadoDTO();
			estado1 = (EstadoDTO) listaestados.get(i);
			//logger.debug("id_estado=" + estado1.getId_estado());
			//logger.debug("estado=" + estado1.getNombre());
			fila.setVariable("{id_estado}", estado1.getId_estado() + "");
			fila.setVariable("{estado}", estado1.getNombre());

			if (Long.toString(id_estado) != null
					&& Long.toString(id_estado).equals(
							String.valueOf(estado1.getId_estado())))
				fila.setVariable("{sel2}", "selected");
			else
				fila.setVariable("{sel2}", "");

			estados.add(fila);
		}
		logger.debug("despues de estado");
		
		// 4.4 Local de facturacion
		List lista_locales_fact = bizDelegate.getLocales();
		ArrayList locales = new ArrayList();
		for (int j = 0; j < lista_locales_fact.size(); j++) {
			IValueSet fila = new ValueSet();
			LocalDTO local1 = new LocalDTO();
			local1 = (LocalDTO) lista_locales_fact.get(j);
			//logger.debug("id_local_fact="+local1.getId_local());
			//logger.debug("nombre local fact="+local1.getNom_local());
			fila.setVariable("{id_local_fact}", local1.getId_local() + "");
			fila.setVariable("{local_fact_nom}", local1.getNom_local());

			if (Long.toString(id_local_fact) != null
			&& Long.toString(id_local_fact).equals(String.valueOf(local1.getId_local())))
				fila.setVariable("{sel_loc_fact}", "selected");
			else
				fila.setVariable("{sel_loc_fact}", "");

			locales.add(fila);			
		}
		
		// 4.5 Zonas de despacho
		List lista_zonas = bizDelegate.getZonasLocal(usr.getId_local());
		ArrayList zonas = new ArrayList();
		for( int i = 0; i < lista_zonas.size(); i++ ) {
			IValueSet fila = new ValueSet();
			ZonaDTO zona = new ZonaDTO();
			zona = (ZonaDTO) lista_zonas.get(i);
			//logger.debug( "id_zona:" + zona.getId_zona() );
			//logger.debug("Nombre zona:" + zona.getNombre());
			fila.setVariable("{option_id_zona}", zona.getId_zona()+"");
			fila.setVariable("{option_nombre}", zona.getNombre());
			
			if (Long.toString(id_zona) != null
					&& Long.toString(id_zona).equals(String.valueOf(zona.getId_zona())))
						fila.setVariable("{sel_zona}", "selected");
					else
						fila.setVariable("{sel_zona}", "");			
			
			zonas.add(fila);
		}

		// 5. Setea variables del template
		if(!param_id_pedido.equals("-1")){
			top.setVariable("{id_pedido}", param_id_pedido);
		}else{
			top.setVariable("{id_pedido}", "");
		}	
		top.setVariable("{id_estado}", param_id_estado);
		top.setVariable("{id_zona}", param_id_zona);
		top.setVariable("{fecha}", param_fecha);
		top.setVariable("{fecha_fin}", param_fechafin);
		top.setVariable("{tipo_fecha}", param_tipo_fecha);
		top.setVariable("{h_despacho}", "");

		if (param_tipo_fecha.equals("despacho")){
			top.setVariable("{chk2}", "checked");			
			top.setVariable("{chk1}", "");			
			top.setVariable("{f_picking}", "");
			top.setVariable("{f_despacho}", param_fecha);
			
			}
		else{
			top.setVariable("{chk1}", "checked");
			top.setVariable("{chk2}", "");
			top.setVariable("{f_picking}", param_fecha);
			top.setVariable("{f_despacho}", "");
			}
		
		if (listapedidos.size()<=0){
			top.setVariable("{mensaje}",MsjeError);
		}else{
			top.setVariable("{mensaje}",mensaje);
		}

		// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());		
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());		

		
		// 6. Setea variables bloques
		top.setDynamicValueSets("listado", datos);
		top.setDynamicValueSets("paginador", pags);
		top.setDynamicValueSets("estados", estados);
		top.setDynamicValueSets("locales", locales);
		top.setDynamicValueSets("zonas", zonas);

		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();

	}

    /**
     * @param ped1
     * @return
     */
    private boolean enTiempo(MonitorPedidosDTO pedido) {
        if ( Constantes.ORIGEN_WEB_CTE.equalsIgnoreCase(pedido.getOrigen()) || Constantes.ORIGEN_VE_CTE.equalsIgnoreCase(pedido.getOrigen()) ) {        
            try {
                String fecHoraPick = pedido.getFpicking()+" "+pedido.getHfin_jpicking();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datePick = (Date)formatter.parse(fecHoraPick);
            
                Date dateActual = formatter.parse(formatter.format(new Date()));
                long tiempoPicking = datePick.getTime();
                long tiempoActual = dateActual.getTime();
                
                if (tiempoPicking < tiempoActual) {
                    return true;
                }
                return false;
                
            } catch (ParseException e) {
                return false;
            }
        }        
        return true;
    }

}
