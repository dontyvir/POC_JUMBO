package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
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
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO;
import cl.bbr.jumbocl.pedidos.dto.MotivoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega el monitor de pedidos
 * 
 * @author BBRI
 */
public class ViewMonOP extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res,
			UserDTO usr) throws Exception {
		logger.debug("comienzo ViewMonOP Execute");
		View salida = new View(res);
		String param_id_pedido="";
		String param_id_estado="";
		String param_mensaje_error="";
		int pag;
		long id_pedido 	= -1;
		long id_estado	= -1;
		int regsperpage = 10;
		String rad_cli = "";
		String cli_rut = "";
		//String cli_dv  = "";
		String cli_ape = "";
		String num_ped ="";
		String rad_fec = "";
		String fec_ini = "";
		String fec_fin = "";
		String action = "";
		String sel_mot = "";
		long id_motivo = -1;
		long id_local = -1;
		long id_local_fact=-1;
		String sel_est = "";
		String sel_loc = "";
		String sel_jde = "";
		String rc = "";
		String mensaje = "";
		boolean sin_gestion = false;
		String sel_loc_fact="";
		String rad_origen="";
		String sel_tip_ped="";
		String sel_tip_desp="";
		String tipo_ve="";
		String tipo_desp="";
		String origen="";
		String cli_email ="";
		String cli_ape_ori = "";
		String checkEstado69 = "";
		
		//logger.debug("User: " + usr.getLogin());
		
		// 		 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");		
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		regsperpage = Integer.parseInt(getServletConfig().getInitParameter(
				"RegsPerPage"));
		logger.debug("RegsPerPage: " + regsperpage);
		if (req.getParameter("rc") != null)
			rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		
		//		 2. Procesa parámetros del request
		if (req.getParameter("pagina") != null) {
			pag = Integer.parseInt(req.getParameter("pagina"));
			logger.debug("pagina: " + req.getParameter("pagina"));
		} else
			pag = 1; // por defecto mostramos la página 1

		if (req.getParameter("id_pedido") != null) {
			param_id_pedido = req.getParameter("id_pedido");
			id_pedido = Long.parseLong(param_id_pedido);
			logger.debug("id_pedido: " + param_id_pedido);
		}
		
		if ((req.getParameter("id_estado") != null)
				&& (!req.getParameter("id_estado").equals("T"))) {
			param_id_estado = req.getParameter("id_estado");
			id_estado = Long.parseLong(param_id_estado);			
			logger.debug("id_estado: " + param_id_estado);
		}
		
		if ((req.getParameter("action") != null)){
			action = req.getParameter("action");
			logger.debug("action:"+action);
		} 
		//parametros de busqueda:
		if ((req.getParameter("num_ped") != null && !req
				.getParameter("num_ped").equals(""))) {
			num_ped = req.getParameter("num_ped");
			logger.debug("num_ped:"+num_ped);
		}
		if ((req.getParameter("rad_cli") != null)){
			rad_cli = req.getParameter("rad_cli"); 
			if (rad_cli.equals("rut")) {
				cli_rut = req.getParameter("cliente");
				//cli_dv = req.getParameter("dv");
				logger.debug("cli_rut:"+cli_rut);
			} else if (rad_cli.equals("ape")) {
				cli_ape_ori = req.getParameter("cliente");
				cli_ape = req.getParameter("cliente").toLowerCase().replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u');				
				logger.debug("cli_ape:"+cli_ape);
				logger.debug("cli_ape_ori:"+cli_ape_ori);
			}
			else if (rad_cli.equals("email")){
				cli_email = req.getParameter("cliente");
				logger.debug("cli_email:"+cli_email);
			}
			
		}
		if ( req.getParameter("rad_fec") != null ){
			rad_fec = req.getParameter("rad_fec");
			logger.debug("rad_fec:"+rad_fec);
		}
		
		if ( req.getParameter("rad_origen") != null ){
			rad_origen = req.getParameter("rad_origen");
			logger.debug("rad_origen:"+rad_origen);
			if(!rad_origen.equals("T")){
				origen = rad_origen;
				logger.debug("origen:"+origen);
			}						
		}
		
		
		if ( req.getParameter("fec_ini") != null ){
			fec_ini = req.getParameter("fec_ini");
			logger.debug("fec_ini:"+fec_ini);
		}
		if ( req.getParameter("fec_fin") != null ){
			fec_fin = req.getParameter("fec_fin");
			logger.debug("fec_fin:"+fec_fin);
		}
		if ( req.getParameter("sel_mot") != null) {
			sel_mot = req.getParameter("sel_mot");
			if(!sel_mot.equals("T") && !sel_mot.equals("SG")){
				id_motivo = Long.parseLong(sel_mot);
				logger.debug("id_motivo:"+id_motivo);
			} else if(sel_mot.equals("SG")){
				sin_gestion = true;
			}
		}else{
			sel_mot = "-1";
		}
		if ( req.getParameter("sel_est") != null) {
			sel_est = req.getParameter("sel_est");
			if(!sel_est.equals("T")){
				id_estado = Long.parseLong(sel_est);
				logger.debug("id_estado:"+id_estado);
			}
		}else{
			sel_est = "-1";
		}
		if ( req.getParameter("sel_loc") != null) {
			sel_loc = req.getParameter("sel_loc");
			if(!sel_loc.equals("T")){
				id_local = Long.parseLong(sel_loc);
				logger.debug("id_local:"+id_local);
			}
		}else{
			sel_loc = "-1";
		}
		
		if ( req.getParameter("sel_jde") != null) {
			sel_jde = req.getParameter("sel_jde");
		}else{
			sel_jde = "-1";
		}
		
		if ( req.getParameter("sel_tip_ped") != null) {
			sel_tip_ped = req.getParameter("sel_tip_ped");
			if(!sel_tip_ped.equals("T")){
				tipo_ve = sel_tip_ped;
				logger.debug("tipo_ve:"+tipo_ve);
			}			
		}else{
			sel_tip_ped = "-1";
		}
		
		if ( req.getParameter("sel_tip_desp") != null) {
		    sel_tip_desp = req.getParameter("sel_tip_desp");
			if(!sel_tip_desp.equals("T")){
				tipo_desp = sel_tip_desp;
				logger.debug("tipo_desp:"+tipo_desp);
			}			
		}else{
		    sel_tip_desp = "-1";
		}

		
		if (req.getParameter("mensaje_error")!=null){
			param_mensaje_error = req.getParameter("mensaje_error");
		}
		if (req.getParameter("mensaje")!=null){
			mensaje = req.getParameter("mensaje");
		}
		
		if ( req.getParameter("sel_loc_fact") != null) {
			sel_loc_fact = req.getParameter("sel_loc_fact");
			if(!sel_loc_fact.equals("T")){
				id_local_fact = Long.parseLong(sel_loc_fact);
				logger.debug("id_local_fact:"+id_local_fact);
			}
		}else{
			sel_loc_fact = "-1";
		}		
		
			
		//		 3. Template		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		
		if(sel_est.equals("T") || sel_est.equals("-1")) {
		
			top.setVariable("{checkEstado69}", "style='display:block;'");
			top.setVariable("{colorTexto}", "style='color:red;'");
			top.setVariable("{estado69}", "S");
			top.setVariable("{mostrar_ocultar}", "Mostrar todos los estados");
			checkEstado69 = "S";
		}else 
			top.setVariable("{checkEstado69}", "style='display:none;'");
		
		String isFiltro = "";
		
		if ( req.getParameter("estado69") != null) {
			
			checkEstado69 = req.getParameter("estado69");
			
			if(checkEstado69.equals("S")){
				
				if (  req.getParameter("filtrar") != null ){
					
					isFiltro = req.getParameter("filtrar");
					
					if(isFiltro.equals("true")) {
						
						top.setVariable("{estado69}", "N");
						top.setVariable("{mostrar_ocultar}", "Ocultar estado Abortado Cliente");
						checkEstado69 = "N";
					
					}else {
						
						top.setVariable("{estado69}", "S");
						top.setVariable("{mostrar_ocultar}", "Mostrar todos los estados");
						
					
					}
				
				}
			
			}else if(checkEstado69.equals("N")){
				
				if (  req.getParameter("filtrar") != null ){
					
					isFiltro = req.getParameter("filtrar");
					
					if(isFiltro.equals("true")) {
						
						top.setVariable("{estado69}", "S");
						top.setVariable("{mostrar_ocultar}", "Mostrar todos los estados");
						checkEstado69 = "S";
					
					}else {
						
						top.setVariable("{estado69}", "N");
						top.setVariable("{mostrar_ocultar}", "Ocultar estado Abortado Cliente");
						
					
					}
				
				}
				
			}
			
		}
		
		
		// 		 4.  Rutinas Dinámicas 
		// 		 4.0 Bizdelegator
		BizDelegate bizDelegate = new BizDelegate();
		//		 4.1 Listado de Pedidos
		
		if(!num_ped.equals(""))	{
			id_pedido = Long.parseLong(num_ped);
			top.setVariable("{checkEstado69}", "style='display:none;'");
			checkEstado69 = "N";
		}
		
		logger.debug("fec_ini:"+fec_ini);
		logger.debug("fec_fin:"+fec_fin);
		/*
		 * PedidosCriteriaDTO criterio = new PedidosCriteriaDTO(id_estado, pag,
		 * regsperpage, id_pedido, cli_rut, cli_ape, rad_fec,
		 * Formatos.formatFecha(fec_ini), Formatos.formatFecha(fec_fin),
		 * id_motivo, false, false, false, true);
		*/
		PedidosCriteriaDTO criterio = new PedidosCriteriaDTO();
		criterio.setCheckEstado69(checkEstado69);
		criterio.setId_estado(id_estado);
		criterio.setPag(pag);
		criterio.setRegsperpag(regsperpage);
		criterio.setId_pedido(id_pedido);
		criterio.setCli_rut(cli_rut);
		criterio.setCli_ape(cli_ape);
		// verifica que las fechas vengan con datos
		if (!fec_ini.equals("") && !fec_fin.equals("")){
			criterio.setTip_fec(rad_fec);
			criterio.setFec_ini(Formatos.formatFecha(fec_ini));
			criterio.setFec_fin(Formatos.formatFecha(fec_fin));
		}
		criterio.setId_motivo(id_motivo);
		//nuevo criterio de búsqueda
		criterio.setId_local(id_local);
		criterio.setId_local_fact(id_local_fact);
		//nuevo criterio de busqueda
		criterio.setJdesp_dia(sel_jde);
		criterio.setOrigen(origen);
		criterio.setTipo_ve(tipo_ve);		
		criterio.setTipo_desp(tipo_desp);		
		criterio.setCli_email(cli_email);
		
		//	orden usando criterios

		List ordenarpor = new ArrayList();		
		ordenarpor.add(PedidosCriteriaDTO.ORDEN_FECHA_COMPRA + " "
				+ PedidosCriteriaDTO.ORDEN_DESCENDENTE);
				
		criterio.setOrden_columnas(ordenarpor);
				
		
		//Motivo sin gestion
		criterio.setSin_gestion(sin_gestion);
		//estados ingresado y en validacion
		if(id_estado==34){
			criterio.setFiltro_estados(Constantes.ESTADOS_PEDIDO_INGR_EN_VAL);
			criterio.setId_estado(-1);
		}
		long id_cliente = 0;
		criterio.setId_cliente(id_cliente);
		//setear atributos y checks
		top.setVariable("{texto_origen_web}",Constantes.ORIGEN_WEB_TXT);
    	top.setVariable("{texto_origen_ve}",Constantes.ORIGEN_VE_TXT);
        top.setVariable("{texto_origen_jv}",Constantes.ORIGEN_JV_TXT);
		
    	top.setVariable("{cte_origen_web}",Constantes.ORIGEN_WEB_CTE);
    	top.setVariable("{cte_origen_ve}",Constantes.ORIGEN_VE_CTE);
        top.setVariable("{cte_origen_jv}",Constantes.ORIGEN_JV_CTE);
    	
    	top.setVariable("{check_1}","checked");
    	top.setVariable("{check_3}","checked");
    	top.setVariable("{sel_jde_en}","disabled");
    	top.setVariable("{check_5}","checked");    	
    	top.setVariable("{sel_tip_ped_en}","disabled");    	
    	
    	if (action.equals("bus_cli")){
    		if (rad_cli.equals("rut")) {
		    	top.setVariable("{check_1}","checked");
		    	top.setVariable("{check_2}","");
		    	top.setVariable("{check_9}","");
    		} else if (rad_cli.equals("ape")) {
		    	top.setVariable("{check_1}","");
		    	top.setVariable("{check_2}","checked");
		    	top.setVariable("{check_9}","");
			}
    		else if (rad_cli.equals("email")){
				top.setVariable("{check_1}","");
				top.setVariable("{check_2}","");
				top.setVariable("{check_9}","checked");
			}
		} else if (action.equals("bus_fec_mot")){
			if (rad_fec.equals("picking")) {
		    	top.setVariable("{check_3}","checked");
		    	top.setVariable("{check_4}","");
			} else {
		    	top.setVariable("{check_3}","");
		    	top.setVariable("{check_4}","checked");
		    	top.setVariable("{sel_jde_en}","enabled");
			}
			if (rad_origen.equals(Constantes.ORIGEN_WEB_CTE)){
				top.setVariable("{check_6}","checked");
		    	top.setVariable("{check_7}","");
                top.setVariable("{check_8}","");
			} else if (rad_origen.equals(Constantes.ORIGEN_JV_CTE)){
                top.setVariable("{check_8}","checked");
                top.setVariable("{check_7}","");
                top.setVariable("{check_6}","");
            } else if (rad_origen.equals(Constantes.ORIGEN_VE_CTE)){
				top.setVariable("{check_6}","");
		    	top.setVariable("{check_7}","checked");
                top.setVariable("{check_8}","");
		    	top.setVariable("{sel_tip_ped_en}","enabled");
			} 
			
		}
		
		/*
		 * if(action.equals("bus_fec_mot")) /* listped =
		 * bizDelegate.getPedidosByFecha(criterio); else
		 */
		criterio.setTipo_picking("");
		List listped;
		 if (req.getParameter("opcion")!= null){
			 listped = bizDelegate.getPedidosByCriteria(criterio);
		 }else{
		   listped = null;
		   top.setVariable("{checkEstado69}", "style='display:none;'");
		 }
		 
		if (req.getParameter("opcion") != null){ 
		if (listped.size() < 1 ){
			top.setVariable("{mje1}","La consulta no arrojo resultados");
			top.setVariable("{dis}","disabled");
		}else{
			top.setVariable("{mje1}","");
			top.setVariable("{dis}","");
		}		
		}else
		{
			top.setVariable("{mje1}","Debe ingresar criterio de búsqueda");
			top.setVariable("{dis}","disabled");
			pag = 1;
		}
		
		int cont=0;
		
		ArrayList datos = new ArrayList();
		if (req.getParameter("opcion") != null){ 
		for (int i = 0; i < listped.size(); i++) {			
			IValueSet fila = new ValueSet();
			ArrayList ls_imgAvisos = new ArrayList();
			MonitorPedidosDTO ped1 = (MonitorPedidosDTO)listped.get(i);
			if (ped1.isConAlertaMPago()){
			    IValueSet imgAvisos = new ValueSet();
				imgAvisos.setVariable("{ale_img}", String
						.valueOf("img/icon4.gif"));
				imgAvisos.setVariable("{ale_msg}", String
						.valueOf("Problemas con el Medio de Pago"));
			    ls_imgAvisos.add(imgAvisos);
			}
			
			String ColorPedido_der = "";
			String ColorPedido_izq = "";
			if (ped1.getMedioPago().equals("PAR")){
			    ColorPedido_der = "<FONT COLOR='#0000FF'>";
			    ColorPedido_izq = "</FONT>";
			}
            
			fila.setVariable("{TrId}", String.valueOf(cont++));
			
			fila.setVariable("{num_op}", ColorPedido_der
					+ String.valueOf(ped1.getId_pedido()) + ColorPedido_izq);
			fila.setVariable("{rut_cliente}", ColorPedido_der
					+ String.valueOf(ped1.getRut_cliente()) + ColorPedido_izq);
			fila.setVariable("{dv_cliente}", ColorPedido_der
					+ ped1.getDv_cliente() + ColorPedido_izq);
			fila.setVariable("{cliente}", ColorPedido_der
					+ ped1.getNom_cliente() + ColorPedido_izq);
			
			if ( ped1.getOrigen().equals(Constantes.ORIGEN_WEB_CTE) ){
				fila.setVariable("{origen_pedido}", ColorPedido_der
						+ Constantes.ORIGEN_WEB_TXT + ColorPedido_izq);
			} else if ( ped1.getOrigen().equals(Constantes.ORIGEN_JV_CTE) ){
				fila.setVariable("{origen_pedido}", ColorPedido_der
						+ Constantes.ORIGEN_JV_TXT + ColorPedido_izq);
            } else if ( ped1.getOrigen().equals(Constantes.ORIGEN_CASOS_CTE) ){
				fila.setVariable("{origen_pedido}", ColorPedido_der
						+ Constantes.ORIGEN_CASOS_CTE + ColorPedido_izq);
            } else{
				fila.setVariable("{origen_pedido}", ColorPedido_der
						+ Constantes.ORIGEN_VE_TXT + ColorPedido_izq);
			}
			
			if ( ped1.getTipo_ve().equals(Constantes.TIPO_VE_NORMAL_CTE) ){
				fila.setVariable("{tipo_pedido}", ColorPedido_der
						+ Constantes.TIPO_VE_NORMAL_TXT + ColorPedido_izq);
			}else{
				fila.setVariable("{tipo_pedido}", ColorPedido_der
						+ Constantes.TIPO_VE_SPECIAL_TXT + ColorPedido_izq);
			}
			//Indra
			if (ped1.getId_estado() == 70)
			{
			 fila.setVariable("{img}","<img src='img/icon41.gif' width='8' height='8'>");
			}
			else 
			{
				fila.setVariable("{img}","");
			}
			
				//indra
			if (ped1.getTipo_despacho() == null){
			    ped1.setTipo_despacho("N");
			}
			if (ped1.getTipo_despacho().equals("E")){//Express
			    fila.setVariable("{color_fondo_op}","#F7BB8B");
			} else if (ped1.getTipo_despacho().equals("C")){//Económico
			    fila.setVariable("{color_fondo_op}","#C4FFC4");
			} else if (ped1.getTipo_despacho().equals("R")){//Retiro en Local
                fila.setVariable("{color_fondo_op}","#FFFF99");
            } else{
			    fila.setVariable("{color_fondo_op}","#FFFFFF");
			}

			fila.setVariable("{fec_pedido}", ColorPedido_der
					+ Formatos.frmFecha(ped1.getFingreso()) + ColorPedido_izq);
			fila.setVariable("{fec_desp}", ColorPedido_der
					+ Formatos.frmFecha(ped1.getFdespacho()) + ColorPedido_izq);
			//fila.setVariable("{estado}", ColorPedido_der + ped1.getEstado()
			if(ped1.getEstado().equals ("ID_ESTAD_PEDIDO_REVISION_FALTANTE")){
			
				fila.setVariable("{estado}", ColorPedido_der + ped1.getEstado()
						+ ColorPedido_izq);

			}else{
				fila.setVariable("{estado}", ColorPedido_der + ped1.getEstado()
						+ ColorPedido_izq);


			}
			
			fila.setVariable("{id_pedido}",String.valueOf(ped1.getId_pedido()));
			fila.setVariable("{pagina}",String.valueOf(pag));
			fila.setVariable("{accion1}","ver");
			fila.setVariable("{direccion1}","ViewOPFormPedido");
            
            if ( bizDelegate.isOpConExceso(ped1.getId_pedido()) || ped1.isMontoExcedido() ) {
                fila.setVariable("{excedido}","$+");
            } else {
                fila.setVariable("{excedido}","");
            }
            
            if (ped1.isAnularBoleta()) {
                fila.setVariable("{anular_boleta}","&szlig;");
            } else {
                fila.setVariable("{anular_boleta}","");
            }
			
			//el usuario tiene un pedido asignado : solo puede ver ese pedido
			// para editar
			if (usr.getId_pedido()>0){
				if(ped1.getId_pedido()==usr.getId_pedido()){
					//el pedido es suyo
					fila.setVariable("{palito}","|");
					fila.setVariable("{accion2}","editar");
					fila.setVariable("{direccion2}","AsignaOP");
					fila.setVariable("{user}", String.valueOf(ped1
							.getId_usuario()));
				}else{
					//no muestra nada para los pedidos que no son suyos
					fila.setVariable("{palito}","");
					fila.setVariable("{accion2}","");
					fila.setVariable("{direccion2}","");
					fila.setVariable("{user}","");
				}				
			}else{
				//el usuario no tiene un pedido asignado : puede ver solo los
				// pedidos sin asignaciones
				if (ped1.getId_usuario()<=0){
					fila.setVariable("{palito}","|");
					fila.setVariable("{accion2}","editar");
					fila.setVariable("{direccion2}","AsignaOP");
					fila.setVariable("{user}", String.valueOf(ped1
							.getId_usuario()));
					
				}else{
					fila.setVariable("{palito}","");
					fila.setVariable("{accion2}","");
					fila.setVariable("{direccion2}","");
					fila.setVariable("{user}","");	
				}
			}
			//Verificamos si el cliente tiene evento y lo marcamos con un color
			// de fondo
			EventoDTO eve = bizDelegate.getEventoByPedido(
					ped1.getRut_cliente(), ped1.getId_pedido());
			/*
			 * if (eve.getIdEvento() != 0) {
			 * fila.setVariable("{color_fondo_op}","#FFFF99"); }
			 *//*
			    * else { fila.setVariable("{color_fondo_op}","#FFFFFF"); }
			    */
			if (eve.getIdEvento() != 0){
			    IValueSet imgAvisos = new ValueSet();
				imgAvisos.setVariable("{ale_img}", String
						.valueOf("img/flag-16.gif"));
				imgAvisos.setVariable("{ale_msg}", String
						.valueOf("Pedido con Evento Asociado"));
			    ls_imgAvisos.add(imgAvisos);
			}
			if (ls_imgAvisos.size() <= 0){
			    IValueSet imgAvisos = new ValueSet();
				imgAvisos.setVariable("{ale_img}", String
						.valueOf("img/1x1trans.gif"));
			    imgAvisos.setVariable("{ale_msg}", String.valueOf(""));
			    ls_imgAvisos.add(imgAvisos);
			}
			fila.setDynamicValueSets("LISTA_AVISOS", ls_imgAvisos);
			datos.add(fila);
		}
		
		top.setVariable("{totalTR}", String.valueOf(cont));
		
		}
//		 4.1 Listado de Estados de pedidos
		List listestados=null;	
		listestados=bizDelegate.getEstadosPedidoBOC();
		ArrayList edos = new ArrayList();
		
		for (int i = 0; i < listestados.size(); i++) {			
			IValueSet fila = new ValueSet();
			EstadoDTO edos1 = (EstadoDTO)listestados.get(i);
			fila.setVariable("{id_estado}", String
					.valueOf(edos1.getId_estado()));
			fila.setVariable("{estado}",edos1.getNombre());
			if (id_estado==edos1.getId_estado())
				fila.setVariable("{sel_est}", "selected");
			else
				fila.setVariable("{sel_est}", "");
			edos.add(fila);
		
		}
		
//		 4.2 Listado de motivos de pedidos
		List lst_mot = null;	
		lst_mot=bizDelegate.getMotivosPedidoBOC();
		ArrayList mots = new ArrayList();
		
		for (int i = 0; i < lst_mot.size(); i++) {			
			IValueSet fila = new ValueSet();
			MotivoDTO mot1 = (MotivoDTO)lst_mot.get(i);
			fila.setVariable("{id_mot}",String.valueOf(mot1.getId_mot()));
			fila.setVariable("{motivo}",mot1.getNombre());
			if (id_motivo==mot1.getId_mot())
				fila.setVariable("{sel_mot}", "selected");
			else
				fila.setVariable("{sel_mot}", "");
				
			mots.add(fila);
			}

		//4.3 Listado de locales
		List lst_loc = null;	
		lst_loc = bizDelegate.getLocales();
		ArrayList locs = new ArrayList();
		
		for (int i = 0; i < lst_loc.size(); i++) {			
			IValueSet fila = new ValueSet();
			LocalDTO loc1 = (LocalDTO)lst_loc.get(i);
			fila.setVariable("{id_loc}",String.valueOf(loc1.getId_local()));
			fila.setVariable("{local}",loc1.getNom_local());
			//local de despacho
			if (id_local==loc1.getId_local())
				fila.setVariable("{sel_loc}", "selected");
			else
				fila.setVariable("{sel_loc}", "");
				
			//local de facturacion
			if (id_local_fact==loc1.getId_local())
				fila.setVariable("{sel_loc_fact}", "selected");
			else
				fila.setVariable("{sel_loc_fact}", "");
			
			locs.add(fila);
			}
		
		//4.4 Listado de jdesp AM/PM
		/*
		 * List lst_jde = new ArrayList();
		 * lst_jde.add(Constantes.PARAMETRO_DIA);
		 * lst_jde.add(Constantes.PARAMETRO_NOCHE); ArrayList jdes = new
		 * ArrayList();
		 * 
		 * for (int i = 0; i < lst_jde.size(); i++) { IValueSet fila = new
		 * ValueSet(); String jde = (String)lst_jde.get(i);
		 * fila.setVariable("{id_jde}",jde); fila.setVariable("{jde}",jde); if
		 * (sel_jde.equals(jde)) fila.setVariable("{sel_jde}", "selected"); else
		 * fila.setVariable("{sel_jde}", ""); jdes.add(fila); }
		 */
		
		//4.4 Jistado de Horas de Inicio para Jornadas de Despacho
		List lst_jde = null;	
		lst_jde = bizDelegate.getHorasInicioJDespacho();
		ArrayList jdes = new ArrayList();
		
		for (int i = 0; i < lst_jde.size(); i++) {			
			IValueSet fila = new ValueSet();
			String jde = (String)lst_jde.get(i);
			fila.setVariable("{id_jde}",jde);
			fila.setVariable("{jde}",jde);
			if (sel_jde.equals(jde))
				fila.setVariable("{sel_jde}", "selected");
			else
				fila.setVariable("{sel_jde}", "");
			jdes.add(fila);
		}
		
		//	4.5 Listado de Tipos de Pedido(N: Normal,  S: Especial)
		List lst_tipo_ve = new ArrayList();	
		lst_tipo_ve.add(Constantes.TIPO_VE_NORMAL_CTE);
		lst_tipo_ve.add(Constantes.TIPO_VE_SPECIAL_CTE);
		ArrayList tipoPedidos = new ArrayList();
		
		for (int i = 0; i < lst_tipo_ve.size(); i++) {			
			IValueSet fila = new ValueSet();
			String tip = (String)lst_tipo_ve.get(i);
			fila.setVariable("{id_tipove}",tip);
			if (tip.equals(Constantes.TIPO_VE_NORMAL_CTE)){
				fila.setVariable("{tipove}",Constantes.TIPO_VE_NORMAL_TXT);	
			}else{
				fila.setVariable("{tipove}",Constantes.TIPO_VE_SPECIAL_TXT);
			}
			
			if (sel_tip_ped.equals(tip))
				fila.setVariable("{sel_tipove}", "selected");
			else
				fila.setVariable("{sel_tipove}", "");
				
			tipoPedidos.add(fila);
		}
		
		
		//	4.6 Listado de Tipos de Despacho(N: Normal,  E: Express,  C: Económico)
		List lst_tipo_desp = new ArrayList();	
		lst_tipo_desp.add(Constantes.TIPO_DESPACHO_NORMAL_CTE);
		lst_tipo_desp.add(Constantes.TIPO_DESPACHO_EXPRESS_CTE);
		lst_tipo_desp.add(Constantes.TIPO_DESPACHO_ECONOMICO_CTE);
        lst_tipo_desp.add(Constantes.TIPO_DESPACHO_RETIRO_CTE);
		ArrayList tipoDespacho = new ArrayList();
		
		for (int i = 0; i < lst_tipo_desp.size(); i++) {			
			IValueSet fila = new ValueSet();
			String tip = (String)lst_tipo_desp.get(i);
			fila.setVariable("{id_tipodesp}",tip);
			if (tip.equals(Constantes.TIPO_DESPACHO_NORMAL_CTE)) {
				fila.setVariable("{tipodesp}",
						Constantes.TIPO_DESPACHO_NORMAL_TXT);
			} else if (tip.equals(Constantes.TIPO_DESPACHO_EXPRESS_CTE)) {
				fila.setVariable("{tipodesp}",
						Constantes.TIPO_DESPACHO_EXPRESS_TXT);
			} else if (tip.equals(Constantes.TIPO_DESPACHO_ECONOMICO_CTE)) {
				fila.setVariable("{tipodesp}",
						Constantes.TIPO_DESPACHO_ECONOMICO_TXT);
			} else if (tip.equals(Constantes.TIPO_DESPACHO_RETIRO_CTE)) {
				fila.setVariable("{tipodesp}",
						Constantes.TIPO_DESPACHO_RETIRO_TXT);
            }
			
			if (sel_tip_desp.equals(tip))
				fila.setVariable("{sel_tipodesp}", "selected");
			else
				fila.setVariable("{sel_tipodesp}", "");
				
			tipoDespacho.add(fila);
		}

		
		//5 Paginador
		ArrayList pags = new ArrayList();
		/*
		 * if(action.equals("bus_fec_mot")) tot_reg =
		 * bizDelegate.getCountPedidosByFecha(criterio); else
		 */
		double tot_reg = bizDelegate.getCountPedidosByCriteria(criterio);
		
		logger.debug("tot_reg: " + tot_reg + "");
		double total_pag = (double)Math.ceil(tot_reg/regsperpage);
		logger.debug ("round: " + total_pag);
		if (total_pag == 0){
			total_pag = 1;
		}
        if (req.getParameter("opcion") == null)
        	total_pag = 1;
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
		// 		 6. Setea variables bloques
		top.setVariable("{mensaje_error}",param_mensaje_error);
		top.setVariable("{num_ped}",num_ped);
		top.setVariable("{rad_cli}",rad_cli);
		if (rad_cli.equals("rut")) {
			//top.setVariable("{cliente1}",cli_rut+"-"+cli_dv);
			top.setVariable("{cliente}",cli_rut);
			//top.setVariable("{dv}",cli_dv);
		}else if(rad_cli.equals("ape")){
		//	top.setVariable("{cliente1}",cli_ape);
			top.setVariable("{cliente}",cli_ape_ori);
		//	top.setVariable("{dv}","");
		}
		else if(rad_cli.equals("email")){
			top.setVariable("{cliente}",cli_email);			
		}
		else{top.setVariable("{cliente}","");}
		top.setVariable("{rad_fec}",rad_fec);
		top.setVariable("{rad_origen}",rad_origen);
		top.setVariable("{fec_ini}",fec_ini);
		top.setVariable("{fec_fin}",fec_fin);
		top.setVariable("{sel_mot}",sel_mot);
		top.setVariable("{sel_tip_ped}",tipo_ve);
		top.setVariable("{sel_tip_desp}",tipo_desp);
		logger.debug("soy un estado     "  +id_estado);
		
		//para motivos sin gestion
		if(sel_mot.equals("SG"))
			top.setVariable("{sel_mot_sg}","selected");
		
		top.setVariable("{sel_est}",sel_est);
		top.setVariable("{sel_loc}",sel_loc);
		top.setVariable("{sel_loc_fact}",sel_loc_fact);		
		top.setVariable("{sel_jde}",sel_jde);
		top.setVariable("{action}",action);
		top.setVariable("{pagina}",String.valueOf(pag));
		top.setVariable("{id_estado}"	,id_estado+"");
		top.setVariable("{usr_pedido}","");
		logger.debug("usr.getId_pedido():"+usr.getId_pedido());
		String var_usrped = "";
		var_usrped = "Ud. está editando la OP: <a href='ViewOPFormPedido?id_pedido="
				+ usr.getId_pedido()
				+ "&mod=1'> "
				+ usr.getId_pedido()
				+ "</a> ("
				+ "<a href =\"javascript:popUpWindowModal('ViewLiberaMotivo?id_pedido="
				+ usr.getId_pedido()
				+ "&origen=2"
				+ "', 200, 200, 500, 180);\"> Liberar OP </a> )";
		
	//	top.setVariable("{url}","ViewMonOP?");
		if(usr.getId_pedido()>0)
			top.setVariable("{usr_pedido}", var_usrped);
		
		if (!mensaje.equals(""))		
			top.setVariable("{mensaje}", mensaje);
		else{
			top.setVariable("{mensaje}", "");						
		}
		
		
		top.setDynamicValueSets("MON_PEDIDOS", datos);
		top.setDynamicValueSets("PAGINAS", pags);
		top.setDynamicValueSets("ESTADOS_PEDIDOS", edos);
		top.setDynamicValueSets("MOTIVOS", mots);
		top.setDynamicValueSets("LOCALES", locs);
		top.setDynamicValueSets("JDESPACHO", jdes);
		top.setDynamicValueSets("LISTA_TIPOSVE", tipoPedidos);
		top.setDynamicValueSets("LISTA_TIPOSDESP", tipoDespacho);
		
		top.setVariable("{hdr_nombre}", usr.getNombre() + " "
				+ usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());	
		
		if ( rc.equals(Constantes._EX_OPE_ID_NO_EXISTE) ){
			top
					.setVariable(
							"{mns}",
							"<script language='JavaScript'>alert('El código del pedido no existe');</script>");
		} else if ( rc.equals(Constantes._EX_OPE_USR_TIENE_OTRO_PED) ){
			top
					.setVariable(
							"{mns}",
							"<script language='JavaScript'>alert('El usuario debe liberar el pedido antes de asignar otro pedido');</script>");
		} else if ( rc.equals(Constantes._EX_OPE_TIENE_OTRO_USR) ){
			top
					.setVariable(
							"{mns}",
							"<script language='JavaScript'>alert('El pedido tiene otro usuario asignado');</script>");
		} else {
			top.setVariable( "{mns}"	, "");
		}
		
		//		 7. Salida Final		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
		
	}

}

