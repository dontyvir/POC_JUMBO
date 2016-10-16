
package cl.bbr.bol.view;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.collaboration.ProcZonasJornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.AvanceDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorRondasDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RondasCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.TotProdZonJorDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * despliega el detalle de la jornada
 * @author mleiva
 */
public class ViewJornadaPKL extends Command {
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String param_id_jornada	= "";
		String param_id_zona    = "";
		long id_jornada			= -1;
		long id_zona 			= -1;
		Map zonas_lst = new LinkedHashMap();
		
		
		//double cant_prod_ronda_creada = 0;
		//double cant_prod_ronda_picking = 0;
		
		logger.debug("id jornada" +req.getParameter("id_jornada"));
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
	
		// 2. Procesa parámetros del request
		if ( req.getParameter("id_jornada") == null ){
			throw new ParametroObligatorioException("id_jornada");
		}	
		param_id_jornada = req.getParameter("id_jornada");
		id_jornada = Long.parseLong(param_id_jornada);
		logger.debug("id_jornada = " + req.getParameter("id_jornada"));

		if ( id_jornada == -1)
			throw new ParametroObligatorioException("id_jornada viene vacío");
		
		
		if (( req.getParameter("sel_zona_res") != null ) && ( !req.getParameter("sel_zona_res").equals(""))){
			param_id_zona = req.getParameter("sel_zona_res");
			id_zona = Long.parseLong(param_id_zona);
			logger.debug("id_zona = " + req.getParameter("sel_zona_res"));
		}
		
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		
		BizDelegate bizDelegate = new BizDelegate();
		JornadaDTO jornada1 = new JornadaDTO();
		jornada1 = bizDelegate.getJornadaById(id_jornada);
		
		//***********************************************************
		//****************   4 . 2   P e d i d o s   ****************
		//***********************************************************
		List listapedidos = new ArrayList();
//		 creamos los criterios
		PedidosCriteriaDTO criterio = new PedidosCriteriaDTO();
		criterio.setId_jpicking(id_jornada);
		criterio.setRegsperpag(9999);
		//criterio.setOrigen(Constantes.ORIGEN_WEB_CTE);
		criterio.setTipo_ve(Constantes.TIPO_VE_NORMAL_CTE);
		criterio.setTipo_picking(Constantes.TIPO_PICKING_LIGHT_CTE);
		
		List ordenarpor = new ArrayList();
		ordenarpor.add(PedidosCriteriaDTO.ORDEN_ZONA_ORDEN+" "+PedidosCriteriaDTO.ORDEN_ASCENDENTE);
		criterio.setOrden_columnas(ordenarpor);
		
		listapedidos = bizDelegate.getPedidosByCriteria(criterio);
		ArrayList ped = new ArrayList();
		ArrayList ped_restante = new ArrayList();

		//contabilizar ops, segun estado
		int ops_en_valid = 0;
		int ops_en_pick = 0;
		int ops_en_bodega =0;
		String zona_anterior="";
		for (int i=0; i<listapedidos.size(); i++){
			IValueSet fila = new ValueSet();
			IValueSet fila_rest = new ValueSet();
			MonitorPedidosDTO pedido1 = new MonitorPedidosDTO();
			pedido1 = (MonitorPedidosDTO)listapedidos.get(i);
			if((pedido1.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION)||
			  (pedido1.getId_estado()==Constantes.ID_ESTAD_PEDIDO_VALIDADO)){
				ops_en_valid += 1;
			}if(pedido1.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_PICKING){
				ops_en_pick += 1;
			}if(pedido1.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_BODEGA){
				ops_en_bodega += 1;
			}
			String num_op    = "";
			String cant_prod = "";
			String fec_desp  = "";
			String hora_desp = "";
			String p_estado  = "";
			logger.debug("pedido1.getId_estado()"+pedido1.getId_estado());
			logger.debug("pedido1.getEstado()"+pedido1.getEstado());
			
			//grosor del borde
			String zona_actual=pedido1.getZona_nombre();
			logger.debug(i+".- zona anterior="+zona_anterior+" zona actual="+zona_actual);
			fila.setVariable("{borderstyle}","");
			
			//llena zonas del combo
			String trace ="COMBO ZONAS con id:"+pedido1.getId_zona()+" "+pedido1.getZona_nombre();

			if (zonas_lst.get(""+pedido1.getId_zona()) == null){
			    zonas_lst.put(""+pedido1.getId_zona(), pedido1.getZona_nombre());
			    logger.debug("COMBO ZONAS - Agrega = id: "+pedido1.getId_zona()+"   nombre: "+pedido1.getZona_nombre());
			}
			
			if(pedido1.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION 
					&& pedido1.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_INGRESADO 
					&& pedido1.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_ANULADO ){
				num_op		= pedido1.getId_pedido()+"";
				cant_prod	= Formatos.formatoNum3Dec(pedido1.getCant_prods())+"";
				try {
					cant_prod = cant_prod.substring(0, cant_prod.indexOf("."));
				} catch(Exception ex) {}
				logger.debug("pedido1.getCant_prods(): " + pedido1.getCant_prods());
				fec_desp	= pedido1.getFdespacho();
				hora_desp	= pedido1.getHdespacho();
				p_estado	= pedido1.getEstado();
				fila.setVariable("{zona}"     , pedido1.getZona_nombre());
				fila.setVariable("{num_op}"   , num_op);
				fila.setVariable("{cant_prod}", cant_prod);
				fila.setVariable("{fec_desp}" , fec_desp);
				fila.setVariable("{hora_desp}", hora_desp);
				fila.setVariable("{p_estado}" , p_estado);
				/*if (pedido1.getId_estado()== Constantes.ID_ESTAD_PEDIDO_VALIDADO ||
				        pedido1.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_EN_PICKING){
					fila.setVariable("{p_accion}" , " / <a href='CreaRondaPKL?id_jornada="+id_jornada+"&id_pedido=" + num_op + "&cant_prod="+pedido1.getCant_prods()+"&tipo_ve="+pedido1.getTipo_ve()+"'>Crear Ronda</a>");				    
				}else{
				    fila.setVariable("{p_accion}" , "");				    
				}*/
				if ((pedido1.getId_estado()== Constantes.ID_ESTAD_PEDIDO_VALIDADO ||
				      pedido1.getId_estado()== Constantes.ID_ESTAD_PEDIDO_EN_PICKING) &&
				        pedido1.getCant_spick() > 0){
					fila.setVariable("{p_accion}" , " / <a href='CreaRondaPKL?id_jornada="+id_jornada+"&id_pedido=" + num_op + "&cant_prod="+pedido1.getCant_spick()+"&tipo_ve="+pedido1.getTipo_ve()+"'>Crear Ronda</a>");				    
				}else{
				    fila.setVariable("{p_accion}" , "");
				}
				
				
				if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
					fila.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
				}
				if (pedido1.getTipo_despacho() == null){
				    pedido1.setTipo_despacho("N");
				}
				if (pedido1.getTipo_despacho().equals("E")){//Express
				    fila.setVariable("{color_fondo_op}", "#F7BB8B");
				}else if (pedido1.getTipo_despacho().equals("C")){//Económico
				    fila.setVariable("{color_fondo_op}", "#C4FFC4");
				}else{
				    fila.setVariable("{color_fondo_op}", "#FFFFFF");
				}

				top.setVariable("{valor}", "Todos");
				
				AvanceDTO AvancePedido = bizDelegate.getAvancePedido(pedido1.getId_pedido());
				if (AvancePedido != null){
					fila.setVariable("{cant_prod_bodega}", Formatos.formatoNumero(AvancePedido.getCant_prod_en_bodega()));
					fila.setVariable("{porc_prod_bodega}", Formatos.formatoNumeroSinDecimales(AvancePedido.getPorc_prod_en_bodega()) + " %");
				}else{
					fila.setVariable("{cant_prod_bodega}", "0");
					fila.setVariable("{porc_prod_bodega}", "0 %");
				}

			    ped.add(fila);
			}else {
				//if(pedido1.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_ANULADO ){
				num_op		= pedido1.getId_pedido()+"";
				cant_prod	= Formatos.formatoNum3Dec(pedido1.getCant_prods())+"";
				fec_desp	= pedido1.getFdespacho();
				hora_desp	= pedido1.getHdespacho();
				p_estado	= pedido1.getEstado();
				fila_rest.setVariable("{zona}"     , pedido1.getZona_nombre());
				fila_rest.setVariable("{num_op}"   , num_op);
				fila_rest.setVariable("{cant_prod}", cant_prod);
				fila_rest.setVariable("{fec_desp}" , fec_desp);
				fila_rest.setVariable("{hora_desp}", hora_desp);
				fila_rest.setVariable("{p_estado}" , p_estado);
				fila_rest.setVariable("{p_accion}" , "");
				if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
					fila_rest.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
				}
				top.setVariable("{valor}", "sacar en validacion");
				fila_rest.setVariable("{cant_prod_bodega}", "0");
				fila_rest.setVariable("{porc_prod_bodega}", "0 %");
				ped_restante.add(fila_rest);
			}
			zona_anterior =zona_actual;
		}
		//colocar los contadores ops
		top.setVariable("{ops_en_valid}" , String.valueOf(ops_en_valid));
		top.setVariable("{ops_en_pick}"  , String.valueOf(ops_en_pick));
		top.setVariable("{ops_en_bodega}", String.valueOf(ops_en_bodega));
		
		
		
		//***********************************************************
		//*****************   4 . 3   R o n d a s   *****************
		//***********************************************************
		logger.debug("4.3 Rondas");
		RondasCriteriaDTO criterioRonda = new RondasCriteriaDTO();
		criterioRonda.setId_jornada(id_jornada);
		criterioRonda.setId_local(usr.getId_local());
		criterioRonda.setPag(-1);
		criterioRonda.setPagina_seleccionada(-1);
		criterioRonda.setRegsperpag(9999);
		criterioRonda.setTipo_ve(Constantes.TIPO_VE_NORMAL_CTE);
		
		
		List ordrondas = new ArrayList();
		ordrondas.add(RondasCriteriaDTO.ORDEN_NJPICK+" "+RondasCriteriaDTO.ORDEN_ASCENDENTE);
		ordrondas.add(RondasCriteriaDTO.ORDEN_ESTADO+" "+RondasCriteriaDTO.ORDEN_DESCENDENTE);
		ordrondas.add(RondasCriteriaDTO.ORDEN_FECHA_FIN_PICKING + " " + RondasCriteriaDTO.ORDEN_ASCENDENTE);
		//ordrondas.add(RondasCriteriaDTO.ORDEN_SECTOR+" "+RondasCriteriaDTO.ORDEN_ASCENDENTE);
		criterioRonda.setOrden_columnas(ordrondas);
		criterioRonda.setId_zona(id_zona);
		criterioRonda.setEsPickingLight("S");
		//r.tipo_ve = 'N'
		
		List listarondas = new ArrayList();
		listarondas = bizDelegate.getMonRondasByCriteriaCMO(criterioRonda);
		//listarondas = bizDelegate.getRondasByCriteria(criterioRonda);
		ArrayList ronda = new ArrayList();
		zona_anterior="";
		for (int i=0; i<listarondas.size(); i++){
			IValueSet fila = new ValueSet();
			MonitorRondasDTO ronda1 = new MonitorRondasDTO();
			ronda1 = (MonitorRondasDTO)listarondas.get(i);
			fila.setVariable("{id_ronda}"	, String.valueOf(ronda1.getId_ronda()));
			fila.setVariable("{id_jornada}"	, String.valueOf(ronda1.getId_jornada()));
			fila.setVariable("{zonas_r}"	, ronda1.getZonas());
			
			String[] split;
			split =ronda1.getZonas().split(",");
			String zona_actual=split[0];
			logger.debug(i+".- zona anterior="+zona_anterior+" zona actual="+zona_actual);
			fila.setVariable("{borderstyle}","");
			if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
				fila.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
			}
			zona_anterior =zona_actual;
			
			
			//fila.setVariable("{seccion}"		,ronda1.getSector());
			fila.setVariable("{id_pedido}"		,ronda1.getId_pedido()+"");
			fila.setVariable("{cant_prod}"		,String.valueOf(Formatos.formatoNum3Dec(ronda1.getCant_prods())));
			
            fila.setVariable("{EstadoAudSust}", ronda1.getEstadoAuditSustitucion());
            
			if(ronda1.getId_estado()==Constantes.ID_ESTADO_RONDA_CREADA){
				fila.setVariable("{tiempo_trans}"	,Formatos.getTiempo(ronda1.getDif_creacion()));
				//cant_prod_ronda_creada = Formatos.formatoNum3Dec(cant_prod_ronda_creada + ronda1.getCant_prods());
			}else if(ronda1.getId_estado()==Constantes.ID_ESTADO_RONDA_EN_PICKING){
				fila.setVariable("{tiempo_trans}"	,Formatos.getTiempo(ronda1.getDif_picking()));
				//cant_prod_ronda_picking = Formatos.formatoNum3Dec(cant_prod_ronda_picking + ronda1.getCant_prods());
			}else if(ronda1.getId_estado()==Constantes.ID_ESTADO_RONDA_FINALIZADA){
				fila.setVariable("{tiempo_trans}"	,Formatos.getTiempo(ronda1.getDif_termino()));
			}
			//fila.setVariable("{tiempo_trans}"	,"algun tiempo");
			fila.setVariable("{r_estado}"			,ronda1.getEstado());
			
			LocalDTO local = bizDelegate.getLocalByID(ronda1.getId_local());
			
			//P: Flujo Parcia o Nuevo Modelo Operacional y Ronda en estado "Terminada"
            if (local.getTipo_flujo().equals("P") && ronda1.getId_estado() == 13){
                if (ronda1.getEstadoImpEtiqueta().equals("N")){
                    fila.setVariable("{print_img}" , String.valueOf("img/imprimir2.gif"));
                    fila.setVariable("{print_link_ini}", "<a href=\"javascript: cambiaImgPrint("+ronda1.getId_ronda()+");\">");
                }else{
                    fila.setVariable("{print_img}" , String.valueOf("img/imprimir.gif"));
                    fila.setVariable("{print_link_ini}", "<a href=\"javascript: popUpWindow('ViewPrint?url='+escape('ViewPrintEtiquetas?id_ronda=" + ronda1.getId_ronda()+ "'), 100, 100, 700, 500);\">");
                }
				fila.setVariable("{print_msg}" , String.valueOf("Imprimir Etiquetas"));
				fila.setVariable("{print_link_fin}", "</a>");
			}else{
				fila.setVariable("{print_img}" , String.valueOf("img/1x1trans.gif"));
				fila.setVariable("{print_msg}" , String.valueOf(""));
				fila.setVariable("{print_link_ini}", String.valueOf("<!--"));
				fila.setVariable("{print_link_fin}", String.valueOf("-->"));
            }
            
            if (ronda1.getId_estado() == 13 && 
                  ronda1.getCant_spick() > 0 && 
                    ronda1.getEstadoVerDetalle().equals("N")){
                fila.setVariable("{ver_link}", String.valueOf("javascript:cambiaImgEdit("+ronda1.getId_ronda()+");"));
                fila.setVariable("{ver_img}" , String.valueOf("img/ver2.gif"));
            }else{
                fila.setVariable("{ver_link}", String.valueOf("ViewRonda?id_ronda=" + ronda1.getId_ronda() + "&urlBack=ViewJornadaPKL?id_jornada="+id_jornada));
                fila.setVariable("{ver_img}" , String.valueOf("img/ver.gif"));
            }
            if (ronda1.getId_estado() == 11 && ronda1.getFecha_inico_ronda_pkl() == null){
                  //fila.setVariable("{IniciaJornadaPKL_link}", String.valueOf("javascript:cambiaImgIniciaJornadaPKL("+ronda1.getId_ronda()+");"));
                  fila.setVariable("{IniciaJornadaPKL_link_ini}", "<a href='" + String.valueOf("javascript:cambiaImgIniciaJornadaPKL("+ronda1.getId_ronda()+");") + "' id='link_"+ronda1.getId_ronda()+"'>");
                  fila.setVariable("{IniciaJornadaPKL_link_fin}", "</a>");
                  fila.setVariable("{IniciaJornadaPKL_img}", String.valueOf("img/Time-cancel.gif"));
            }else if (ronda1.getFecha_inico_ronda_pkl() != null){
                  //fila.setVariable("{IniciaJornadaPKL_link}", String.valueOf("ViewRonda?id_ronda=" + ronda1.getId_ronda() + "&urlBack=ViewJornadaPKL?id_jornada="+id_jornada));
                fila.setVariable("{IniciaJornadaPKL_link_ini}", ""); //"<a href='" + String.valueOf("javascript:cambiaImgIniciaJornadaPKL("+ronda1.getId_ronda()+");") + "'>");
                  fila.setVariable("{IniciaJornadaPKL_link_fin}", ""); //"</a>");
                  fila.setVariable("{IniciaJornadaPKL_img}" , String.valueOf("img/Time-ok.gif"));
            }else if (ronda1.getId_estado() != 11){
                //<a href='{IniciaJornadaPKL_link}'>
                fila.setVariable("{IniciaJornadaPKL_link_ini}", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!--");
                fila.setVariable("{IniciaJornadaPKL_link_fin}", "-->");
                fila.setVariable("{IniciaJornadaPKL_img}" , "img/transp.gif");
            }
			ronda.add(fila);
		}
		
		
		// sacamos las estadisticas de rondas
		List stats = new ArrayList(); // Object[Long(id_sector), Double(cant_creada), Double(cant_enpick)]

		for(int i=0;i<listarondas.size(); i++){
			MonitorRondasDTO ronda1 = new MonitorRondasDTO();
			ronda1 = (MonitorRondasDTO)listarondas.get(i);
			logger.debug("i:" + i);
			// iteramos stats (el arreglo con las estadisticas) para ver si existe un registro con el id_sector
			boolean found = false;
			for(int j=0;j<stats.size();j++){
				
				logger.debug("j:" + j);
				Object[] reg = (Object[])stats.get(j);
				Long   id_sector 	= (Long)reg[0];
				Double cant_creada	= (Double)reg[1];
				Double cant_enpick	= (Double)reg[2];
				Double cant_enbodega= (Double)reg[3];
				
				
				if ( id_sector.longValue() == ronda1.getId_sector() ){
					found = true;
					// sumamos
					if(ronda1.getId_estado()== Constantes.ID_ESTADO_RONDA_CREADA){
						// sumamos en la columna cant_creada
						reg[1] =  new Double( Formatos.formatoNum3Dec(cant_creada.doubleValue() + ronda1.getCant_prods()) );
					}else if(ronda1.getId_estado()== Constantes.ID_ESTADO_RONDA_EN_PICKING){
						//sumamos en la columna cant_enpick
						reg[2] =  new Double( Formatos.formatoNum3Dec(cant_enpick.doubleValue() + ronda1.getCant_prods()) );						
					}else if(ronda1.getId_estado()== Constantes.ID_ESTADO_RONDA_FINALIZADA){
						//sumamos en la columna cant_enbodega
						reg[3] =  new Double( Formatos.formatoNum3Dec(cant_enbodega.doubleValue() + ronda1.getCant_prods() - ronda1.getCant_spick()) );						
					}
				}
			}
			//si no existe la llave en el arreglo, la agregamos
			if(found == false){
				// Agregamos el registro
				if(ronda1.getId_estado()== Constantes.ID_ESTADO_RONDA_CREADA){
					Object[] reg = new Object[4];
					reg[0] = new Long( ronda1.getId_sector() );
					reg[1] = new Double( ronda1.getCant_prods() );
					reg[2] = new Double(0);
					reg[3] = new Double(0);
					stats.add(reg);
				}else if(ronda1.getId_estado()== Constantes.ID_ESTADO_RONDA_EN_PICKING){
					Object[] reg = new Object[4];
					reg[0] = new Long(ronda1.getId_sector());
					reg[1] = new Double(0);
					reg[2] = new Double(ronda1.getCant_prods());
					reg[3] = new Double(0);
					stats.add(reg);
				}else if(ronda1.getId_estado()== Constantes.ID_ESTADO_RONDA_FINALIZADA){
					Object[] reg = new Object[4];
					reg[0] = new Long(ronda1.getId_sector());
					reg[1] = new Double(0);
					reg[2] = new Double(0);
					reg[3] = new Double(ronda1.getCant_prods() - ronda1.getCant_spick());
					stats.add(reg);
				}

			}
						
		}
		
		//***********************************************************
		//*****   4 . 5   P e d i d o s   E s p e c i a l e s   *****
		//***********************************************************
		logger.debug("4.5 Pedidos Especiales");
		List listapedidos_esp = new ArrayList();
//		 creamos los criterio_esps
		PedidosCriteriaDTO criterio_esp = new PedidosCriteriaDTO();		
		criterio_esp.setId_jpicking(id_jornada);
		criterio_esp.setRegsperpag(9999);
		criterio_esp.setOrigen(Constantes.ORIGEN_VE_CTE);
		criterio_esp.setTipo_ve(Constantes.TIPO_VE_SPECIAL_CTE);
		
		
		List ordenarpor_esp = new ArrayList();
		ordenarpor_esp.add(PedidosCriteriaDTO.ORDEN_ZONA_ORDEN+" "+PedidosCriteriaDTO.ORDEN_ASCENDENTE);
		criterio_esp.setOrden_columnas(ordenarpor_esp);
		
		
		listapedidos_esp = bizDelegate.getPedidosByCriteria(criterio_esp);
		ArrayList ped_esp = new ArrayList();	
		ArrayList ped_restante_esp = new ArrayList();	

		//contabilizar ops, segun estado
		int ops_en_valid_esp = 0;
		int ops_en_pick_esp = 0;
		int ops_en_bodega_esp =0;
		zona_anterior="";
		for (int i=0; i<listapedidos_esp.size(); i++){			
			IValueSet fila_esp = new ValueSet();
			IValueSet fila_rest_esp = new ValueSet();
			MonitorPedidosDTO pedido1_esp = new MonitorPedidosDTO();
			pedido1_esp = (MonitorPedidosDTO)listapedidos_esp.get(i);
			if((pedido1_esp.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION)||
			  (pedido1_esp.getId_estado()==Constantes.ID_ESTAD_PEDIDO_VALIDADO))	{
				ops_en_valid_esp += 1;
			}if(pedido1_esp.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_PICKING){
				ops_en_pick_esp += 1;
			}if(pedido1_esp.getId_estado()==Constantes.ID_ESTAD_PEDIDO_EN_BODEGA){
				ops_en_bodega_esp += 1;
			}
			String num_op_esp 	= "";
			String cant_prod_esp= "";
			String fec_desp_esp = "";
			String hora_desp_esp= "";
			String p_estado_esp = "";		
			logger.debug("pedido1_esp.getId_estado()"+pedido1_esp.getId_estado());
			logger.debug("pedido1_esp.getEstado()"+pedido1_esp.getEstado());
			
			//grosor del borde			
			String zona_actual=pedido1_esp.getZona_nombre();
			logger.debug(i+".- zona anterior="+zona_anterior+" zona actual="+zona_actual);
			fila_esp.setVariable("{borderstyle}","");
			
			//llena zonas del combo
			logger.debug("combo zona:"+pedido1_esp.getId_zona()+pedido1_esp.getZona_nombre());
			
			String trace ="COMBO ZONAS con id:"+pedido1_esp.getId_zona()+" "+pedido1_esp.getZona_nombre();		

			if (zonas_lst.get(""+pedido1_esp.getId_zona()) == null){
			    zonas_lst.put(""+pedido1_esp.getId_zona(), pedido1_esp.getZona_nombre());
			    logger.debug("COMBO ZONAS - agrega = id: "+pedido1_esp.getId_zona()+"   nombre: "+pedido1_esp.getZona_nombre());
			}

			
			if(pedido1_esp.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION && 
			     pedido1_esp.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_INGRESADO && 
			       pedido1_esp.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_ANULADO ){
				num_op_esp   = pedido1_esp.getId_pedido()+"";
				cant_prod_esp= pedido1_esp.getCant_prods()+"";
				fec_desp_esp = pedido1_esp.getFdespacho();
				hora_desp_esp= pedido1_esp.getHdespacho();
				p_estado_esp = pedido1_esp.getEstado();
				fila_esp.setVariable("{zona_esp}"     , pedido1_esp.getZona_nombre());
				fila_esp.setVariable("{num_op_esp}"   , num_op_esp);
				fila_esp.setVariable("{cant_prod_esp}", cant_prod_esp);
				fila_esp.setVariable("{fec_desp_esp}" , fec_desp_esp);
				fila_esp.setVariable("{hora_desp_esp}", hora_desp_esp);
				fila_esp.setVariable("{p_estado_esp}" , p_estado_esp);
				if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
					fila_esp.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
				}
				top.setVariable("{valor}", "Todos");
				AvanceDTO AvancePedidoEsp = bizDelegate.getAvancePedido(pedido1_esp.getId_pedido());
				if (AvancePedidoEsp != null){
				    fila_esp.setVariable("{cant_prod_bodega}", Formatos.formatoNumero(AvancePedidoEsp.getCant_prod_en_bodega()));
				    fila_esp.setVariable("{porc_prod_bodega}", Formatos.formatoNumeroSinDecimales(AvancePedidoEsp.getPorc_prod_en_bodega()) + " %");
				}else{
				    fila_esp.setVariable("{cant_prod_bodega}", "0");
				    fila_esp.setVariable("{porc_prod_bodega}", "0 %");
				}
				if ((pedido1_esp.getId_estado()== Constantes.ID_ESTAD_PEDIDO_VALIDADO ||
					      pedido1_esp.getId_estado()== Constantes.ID_ESTAD_PEDIDO_EN_PICKING) &&
					        pedido1_esp.getCant_spick() > 0){
				        fila_esp.setVariable("{p_accion}" , " / <a href='CreaRondaPKL?id_jornada="+id_jornada+"&id_pedido=" + num_op_esp + "&cant_prod="+pedido1_esp.getCant_spick()+"&tipo_ve="+pedido1_esp.getTipo_ve()+"'>Crear Ronda</a>");				    
					}else{
					    fila_esp.setVariable("{p_accion}" , "");				    
					}

			    ped_esp.add(fila_esp);
			}else{
				//if(pedido1_esp.getId_estado()!= Constantes.ID_ESTAD_PEDIDO_ANULADO ){
				num_op_esp   = pedido1_esp.getId_pedido()+"";
				cant_prod_esp= pedido1_esp.getCant_prods()+"";
				fec_desp_esp = pedido1_esp.getFdespacho();
				hora_desp_esp= pedido1_esp.getHdespacho();
				p_estado_esp = pedido1_esp.getEstado();
				fila_rest_esp.setVariable("{zona_esp}"     , pedido1_esp.getZona_nombre());
				fila_rest_esp.setVariable("{num_op_esp}"   , num_op_esp);
				fila_rest_esp.setVariable("{cant_prod_esp}", cant_prod_esp);
				fila_rest_esp.setVariable("{fec_desp_esp}" , fec_desp_esp);
				fila_rest_esp.setVariable("{hora_desp_esp}", hora_desp_esp);
				fila_rest_esp.setVariable("{p_estado_esp}" , p_estado_esp);
				if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
					fila_rest_esp.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
				}
				top.setVariable("{valor}", "sacar en validacion");
				fila_rest_esp.setVariable("{cant_prod_bodega}", "0");
				fila_rest_esp.setVariable("{porc_prod_bodega}", "0 %");
				ped_restante_esp.add(fila_rest_esp);
			}
			zona_anterior =zona_actual;
		}
		//colocar los contadores ops_esp
		top.setVariable("{ops_en_valid_esp}" , String.valueOf(ops_en_valid_esp));
		top.setVariable("{ops_en_pick_esp}"  , String.valueOf(ops_en_pick_esp));
		top.setVariable("{ops_en_bodega_esp}", String.valueOf(ops_en_bodega_esp));
		
		
		//***********************************************************
		//******   4 . 6   R o n d a s   E s p e c i a l e s   ******
		//***********************************************************
		logger.debug("4.6 Rondas Especiales");
		RondasCriteriaDTO criterioRonda_esp = new RondasCriteriaDTO(); 
		criterioRonda_esp.setId_jornada(id_jornada);
		criterioRonda_esp.setId_local(usr.getId_local());
		criterioRonda_esp.setPag(-1);
		criterioRonda_esp.setPagina_seleccionada(-1);
		criterioRonda_esp.setRegsperpag(9999);
		criterioRonda_esp.setTipo_ve(Constantes.TIPO_VE_SPECIAL_CTE);
		
		List ordrondas_esp = new ArrayList();
		ordrondas_esp.add(RondasCriteriaDTO.ORDEN_NJPICK+" "+RondasCriteriaDTO.ORDEN_ASCENDENTE);
		//ordrondas_esp.add(RondasCriteriaDTO.ORDEN_SECTOR+" "+RondasCriteriaDTO.ORDEN_ASCENDENTE);
		ordrondas_esp.add(RondasCriteriaDTO.ORDEN_ESTADO+" "+RondasCriteriaDTO.ORDEN_ASCENDENTE);
		criterioRonda_esp.setOrden_columnas(ordrondas_esp);
		criterioRonda_esp.setId_zona(id_zona);
		criterioRonda_esp.setEsPickingLight("S");
		
		List listarondas_esp = new ArrayList();
		listarondas_esp = bizDelegate.getMonRondasByCriteriaCMO(criterioRonda_esp);
		//listarondas_esp = bizDelegate.getRondasByCriteria(criterioRonda_esp);
		ArrayList ronda_esp = new ArrayList();	
		logger.debug("Rondas especiales :"+listarondas_esp.size());
		zona_anterior="";
		for (int i=0; i<listarondas_esp.size(); i++){			
			IValueSet fila = new ValueSet();
			MonitorRondasDTO ronda1_esp = new MonitorRondasDTO();
			ronda1_esp = (MonitorRondasDTO)listarondas_esp.get(i);
			fila.setVariable("{id_ronda}"  ,String.valueOf(ronda1_esp.getId_ronda()));
			fila.setVariable("{id_jornada}",String.valueOf(ronda1_esp.getId_jornada()));
			fila.setVariable("{zonas_r}"   ,ronda1_esp.getZonas());
			
			String[] split;
			split =ronda1_esp.getZonas().split(",");
			String zona_actual=split[0];
			logger.debug(i+".- zona anterior="+zona_anterior+" zona actual="+zona_actual);
			fila.setVariable("{borderstyle}", "");
			if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
				fila.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
			}
			zona_anterior =zona_actual;
			
			
			//fila.setVariable("{seccion}"  , ronda1_esp.getSector());
			fila.setVariable("{id_pedido}"	,ronda1_esp.getId_pedido()+"");
			fila.setVariable("{cant_prod}", String.valueOf(Formatos.formatoNum3Dec(ronda1_esp.getCant_prods())));
			
            fila.setVariable("{EstadoAudSust}", ronda1_esp.getEstadoAuditSustitucion());
            
			if(ronda1_esp.getId_estado()==Constantes.ID_ESTADO_RONDA_CREADA){
				fila.setVariable("{tiempo_trans}", Formatos.getTiempo(ronda1_esp.getDif_creacion()));
				//cant_prod_ronda_creada = Formatos.formatoNum3Dec(cant_prod_ronda_creada + ronda1_esp.getCant_prods());
			}else if(ronda1_esp.getId_estado()==Constantes.ID_ESTADO_RONDA_EN_PICKING){
				fila.setVariable("{tiempo_trans}", Formatos.getTiempo(ronda1_esp.getDif_picking()));
				//cant_prod_ronda_picking = Formatos.formatoNum3Dec(cant_prod_ronda_picking + ronda1_esp.getCant_prods());
			}else if(ronda1_esp.getId_estado()==Constantes.ID_ESTADO_RONDA_FINALIZADA){
				fila.setVariable("{tiempo_trans}", Formatos.getTiempo(ronda1_esp.getDif_termino()));
			}
			//fila.setVariable("{tiempo_trans}"	,"algun tiempo");			
			fila.setVariable("{r_estado}", ronda1_esp.getEstado());
			
			LocalDTO local = bizDelegate.getLocalByID(ronda1_esp.getId_local());
			//P: Flujo Parcia o Nuevo Modelo Operacional y Ronda en estado "Terminada"
            if (local.getTipo_flujo().equals("P") && ronda1_esp.getId_estado() == 13){
                if (ronda1_esp.getEstadoImpEtiqueta().equals("N")){
                    fila.setVariable("{print_img}" , String.valueOf("img/imprimir2.gif"));
                    fila.setVariable("{print_link_ini}", "<a href=\"javascript: cambiaImgPrint("+ronda1_esp.getId_ronda()+");\">");
                }else{
                    fila.setVariable("{print_img}" , String.valueOf("img/imprimir.gif"));
                    fila.setVariable("{print_link_ini}", "<a href=\"javascript: popUpWindow('ViewPrint?url='+escape('ViewPrintEtiquetas?id_ronda=" + ronda1_esp.getId_ronda()+ "'), 100, 100, 700, 500);\">");
                }
				fila.setVariable("{print_msg}" , String.valueOf("Imprimir Etiquetas"));
				fila.setVariable("{print_link_fin}", "</a>");
			}else{
				fila.setVariable("{print_img}" , String.valueOf("img/1x1trans.gif"));
				fila.setVariable("{print_msg}" , String.valueOf(""));
				fila.setVariable("{print_link_ini}", String.valueOf("<!--"));
				fila.setVariable("{print_link_fin}", String.valueOf("-->"));
            }
            
            if (ronda1_esp.getId_estado() == 13 && 
                    ronda1_esp.getCant_spick() > 0 && 
                        ronda1_esp.getEstadoVerDetalle().equals("N")){
                fila.setVariable("{ver_link}", String.valueOf("javascript:cambiaImgEdit("+ronda1_esp.getId_ronda()+");"));
                fila.setVariable("{ver_img}" , String.valueOf("img/ver2.gif"));
            }else{
                fila.setVariable("{ver_link}", String.valueOf("ViewRonda?id_ronda=" + ronda1_esp.getId_ronda() + "&urlBack=ViewJornadaPKL?id_jornada="+id_jornada));
                fila.setVariable("{ver_img}" , String.valueOf("img/ver.gif"));
            }
            if (ronda1_esp.getId_estado() == 11 && ronda1_esp.getFecha_inico_ronda_pkl() == null){
                //fila.setVariable("{IniciaJornadaPKL_link}", String.valueOf("javascript:cambiaImgIniciaJornadaPKL("+ronda1.getId_ronda()+");"));
                fila.setVariable("{IniciaJornadaPKL_link_ini}", "<a href='" + String.valueOf("javascript:cambiaImgIniciaJornadaPKL("+ronda1_esp.getId_ronda()+");") + "'>");
                fila.setVariable("{IniciaJornadaPKL_link_fin}", "</a>");
                fila.setVariable("{IniciaJornadaPKL_img}" , String.valueOf("img/Time-cancel.gif"));
            }else if (ronda1_esp.getFecha_inico_ronda_pkl() != null){
                //fila.setVariable("{IniciaJornadaPKL_link}", String.valueOf("ViewRonda?id_ronda=" + ronda1.getId_ronda() + "&urlBack=ViewJornadaPKL?id_jornada="+id_jornada));
                fila.setVariable("{IniciaJornadaPKL_link_ini}", "<a href='" + String.valueOf("javascript:cambiaImgIniciaJornadaPKL("+ronda1_esp.getId_ronda()+");") + "'>");
                fila.setVariable("{IniciaJornadaPKL_link_fin}", "</a>");
                fila.setVariable("{IniciaJornadaPKL_img}" , String.valueOf("img/Time-ok.gif"));
            }else if (ronda1_esp.getId_estado() != 11){
                //<a href='{IniciaJornadaPKL_link}'>
                fila.setVariable("{IniciaJornadaPKL_link_ini}", "<!--");
                fila.setVariable("{IniciaJornadaPKL_link_fin}", "-->");
                fila.setVariable("{IniciaJornadaPKL_img}" , "img/transp.gif");
            }
			ronda_esp.add(fila);
		}
		
		
		// sacamos las estadisticas de rondas especiales
		List stats_esp = new ArrayList(); // Object[Long(id_sector), Double(cant_creada), Double(cant_enpick)]

		for(int i=0;i<listarondas_esp.size(); i++){
			MonitorRondasDTO ronda1_esp = new MonitorRondasDTO();
			ronda1_esp = (MonitorRondasDTO)listarondas_esp.get(i);
			logger.debug("revisa estadisticas i:" + i);			
			// iteramos stats_esp (el arreglo con las estadisticas) para ver si existe un registro con el id_sector
			boolean found = false;
			for(int j=0;j<stats_esp.size();j++){
				
				logger.debug("j:" + j);
				Object[] reg = (Object[])stats_esp.get(j);
				Long   id_sector 	= (Long)reg[0];
				Double cant_creada	= (Double)reg[1];
				Double cant_enpick	= (Double)reg[2];
				Double cant_enbodega= (Double)reg[3];
				
				if ( id_sector.longValue() == ronda1_esp.getId_sector() ){
					found = true;
					// sumamos
					if(ronda1_esp.getId_estado()== Constantes.ID_ESTADO_RONDA_CREADA){
						// sumamos en la columna cant_creada
						reg[1] =  new Double( Formatos.formatoNum3Dec(cant_creada.doubleValue() + ronda1_esp.getCant_prods()) );
					}else if(ronda1_esp.getId_estado()== Constantes.ID_ESTADO_RONDA_EN_PICKING){
						//sumamos en la columna cant_enpick
						reg[2] =  new Double( Formatos.formatoNum3Dec(cant_enpick.doubleValue() + ronda1_esp.getCant_prods()) );						
					}else if(ronda1_esp.getId_estado()== Constantes.ID_ESTADO_RONDA_FINALIZADA){
						//sumamos en la columna cant_enpick
						reg[3] =  new Double( Formatos.formatoNum3Dec(cant_enbodega.doubleValue() + ronda1_esp.getCant_prods() - ronda1_esp.getCant_spick()) );						
					}
				}
			}
			//si no existe la llave en el arreglo, la agregamos
			if(found == false){
				// Agregamos el registro
				if(ronda1_esp.getId_estado()== Constantes.ID_ESTADO_RONDA_CREADA){
					Object[] reg = new Object[4];
					reg[0] = new Long(ronda1_esp.getId_sector());
					reg[1] = new Double(ronda1_esp.getCant_prods());
					reg[2] = new Double(0);
					reg[3] = new Double(0);
					logger.debug("Arreglo ronda creada [sector, qty prods creada,qty prods en picking, qty prods en bodega ]:["+reg[0]+","+reg[1]+","+reg[2]+","+reg[3]+"]");
					stats_esp.add(reg);
				}else if(ronda1_esp.getId_estado()== Constantes.ID_ESTADO_RONDA_EN_PICKING){
					Object[] reg = new Object[4];
					reg[0] = new Long(ronda1_esp.getId_sector());
					reg[1] = new Double(0);
					reg[2] = new Double(ronda1_esp.getCant_prods());
					reg[3] = new Double(0);
					logger.debug("Arreglo ronda en picking[sector, qty prods creada,qty prods en picking, qty prods en bodega ]:["+reg[0]+","+reg[1]+","+reg[2]+","+reg[3]+"]");
					stats_esp.add(reg);
				}else if(ronda1_esp.getId_estado()== Constantes.ID_ESTADO_RONDA_FINALIZADA){
					Object[] reg = new Object[4];//en bodega
					reg[0] = new Long(ronda1_esp.getId_sector());
					reg[1] = new Double(0);
					reg[2] = new Double(0);
					reg[3] = new Double(ronda1_esp.getCant_prods()-ronda1_esp.getCant_spick());
					logger.debug("Arreglo ronda en picking[sector, qty prods creada,qty prods en picking, qty prods en bodega ]:["+reg[0]+","+reg[1]+","+reg[2]+","+reg[3]+"]");
					stats_esp.add(reg);
				}
			}
		}
		
		
		
		//***********************************************************
		//******************   4 . 7   Z o n a s   ******************
		//***********************************************************
		ArrayList zonas = new ArrayList();
	    for (Iterator it=zonas_lst.keySet().iterator(); it.hasNext(); ) {
	        String key = it.next().toString();
	        String value = zonas_lst.get(key).toString();
	        
	        IValueSet fila = new ValueSet();
			fila.setVariable("{id_zona}", key);
			fila.setVariable("{zona_nom}", value);
			
			if (param_id_zona != null && param_id_zona.equals(key)){				
				fila.setVariable("{sel_zona1}", "selected");
			}else{
				fila.setVariable("{sel_zona1}", "");
			}
			zonas.add(fila);

	    }

		
		//***********************************************************************
		//*****   5 . 1   R e s u m e n   X   Z o n a s   N o r m a l e s   *****
		//***********************************************************************
		logger.debug("5.1 Resumen x Zonas Normales");
		ArrayList res_zonas = new ArrayList();
		double prods_no_asig2=0;
		double prods_tot2=0;
		double prods_en_bod2=0;
		double prods_ronda_crea2=0;
		zona_anterior="";
		List listazonas_prod = new ArrayList();
		ProcZonasJornadaDTO datos_zon = new ProcZonasJornadaDTO();
		datos_zon.setId_jornada(id_jornada);
		datos_zon.setFlag_especiales(0);
		listazonas_prod = bizDelegate.getTotalProductosZonasJornada(datos_zon);
		
		for (int i=0; i<listazonas_prod.size(); i++){
			logger.debug("i: "+i);
			IValueSet fila = new ValueSet();
			TotProdZonJorDTO zonas1 = (TotProdZonJorDTO)listazonas_prod.get(i);
			
			String zona_actual=zonas1.getZona();
			logger.debug(i+".- zona anterior="+zona_anterior+" zona actual="+zona_actual);
			fila.setVariable("{borderstyle}","");
			if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
				fila.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
			}
			zona_anterior =zona_actual;
			
			//totales
			prods_no_asig2 = Formatos.formatoNum3Dec(prods_no_asig2 + zonas1.getCant_prods_no_asig());			
			prods_tot2 = Formatos.formatoNum3Dec(prods_tot2 + zonas1.getCant_prods());
			
			fila.setVariable("{zonas}"		,zonas1.getZona());
			fila.setVariable("{cant_prod}"	,String.valueOf(Formatos.formatoNum3Dec(zonas1.getCant_prods())));	
			fila.setVariable("{cant_prod_no_asig}"	,String.valueOf(Formatos.formatoNum3Dec(zonas1.getCant_prods_no_asig())));
						
			//obtiene rondas de la jornada + zona
			double ron_creada_prods=0;
			double ron_pick_prods=0;
			double ron_term_prods=0;
			
			double gen_ron_creada_prods=0;
			double gen_ron_creada_prods_spick =0;
			double gen_ron_term_prods=0;
			
			
			for(int j=0;j<listarondas.size();j++){				
				MonitorRondasDTO ron1 = (MonitorRondasDTO)listarondas.get(j);	
				if(ron1.getId_estado()==Constantes.ID_ESTADO_RONDA_FINALIZADA){
					gen_ron_creada_prods_spick+=ron1.getCant_spick();
				}
				
				logger.debug(i+"."+j+"id_ronda = "+ron1.getId_ronda()+ " spik:"+ron1.getCant_spick()+ " acum:"+gen_ron_creada_prods_spick);
				
				List lst_det_r1 = new ArrayList();
				lst_det_r1 = bizDelegate.getProductosRonda(ron1.getId_ronda());
				for(int k=0;k<lst_det_r1.size();k++){					
					ProductosPedidoDTO prod = (ProductosPedidoDTO)lst_det_r1.get(k);
					
					logger.debug("producto:"+prod.getId_producto()
							+" pedido:"+prod.getId_pedido()
							+" zona:"+prod.getId_zona()
							+" cant:"+prod.getCant_solic());
					
					//if (prod.getId_zona()!=zonas1.getId_zona())
					gen_ron_creada_prods+=prod.getCant_solic();
					
					if(ron1.getId_estado()==Constantes.ID_ESTADO_RONDA_CREADA){
						
						if (prod.getId_zona()==zonas1.getId_zona()){
							ron_creada_prods +=prod.getCant_solic();
						}
						
					}else if(ron1.getId_estado()==Constantes.ID_ESTADO_RONDA_EN_PICKING){
						if (prod.getId_zona()==zonas1.getId_zona()){
							ron_pick_prods +=prod.getCant_solic();
							}
					}else if(ron1.getId_estado()==Constantes.ID_ESTADO_RONDA_FINALIZADA){
						gen_ron_term_prods+=prod.getCant_solic();
						if (prod.getId_zona()==zonas1.getId_zona()){
							ron_term_prods +=prod.getCant_solic() - prod.getCant_spick();
						}
					}
					logger.debug(i+"."+j+"."+k+".- rondas creadas:"+ron_creada_prods
							+" ron_pick_prods:"+ron_pick_prods
							+" ron_term_prods:"+ron_term_prods);
				}				
			}
			
			//cuenta los totales generales solo con la primera zona, si no se multiplican x cant zonas
			if (i==0){
				// se le restan las cantidades sin pickear
				prods_ronda_crea2 = Formatos.formatoNum3Dec(prods_ronda_crea2 + gen_ron_creada_prods-gen_ron_creada_prods_spick);
				prods_en_bod2 = Formatos.formatoNum3Dec(prods_en_bod2 + gen_ron_term_prods-gen_ron_creada_prods_spick);
			}
			
			fila.setVariable("{cant_creada}"	,String.valueOf(Formatos.formatoNum3Dec(ron_creada_prods)));	
			fila.setVariable("{cant_enpick}"	,String.valueOf(Formatos.formatoNum3Dec(ron_pick_prods)));
			fila.setVariable("{cant_enbod}"		,String.valueOf(Formatos.formatoNum3Dec(ron_term_prods)));	
			
			double avance = Formatos.formatoNum3Dec((ron_term_prods/zonas1.getCant_prods())*100);
			fila.setVariable("{avance}"		,Formatos.formatoNumero(avance)+" %");		
			res_zonas.add(fila);
		}

		double porc_no_asig2 = Formatos.formatoNum3Dec( 100 * (prods_no_asig2 /prods_tot2));
		double porc_en_bod2 = Formatos.formatoNum3Dec( 100 * (prods_en_bod2 /prods_tot2));
		double porc_ronda_crea2 = Formatos.formatoNum3Dec( 100 * (prods_ronda_crea2 /prods_tot2));
		
		
		//		setear contadores de productos y porcentajes
		top.setVariable("{prods_no_asig}",String.valueOf(prods_no_asig2));
		top.setVariable("{porc_no_asig}",Formatos.formatoNumeroSinDecimales(porc_no_asig2)+" %");
		top.setVariable("{prods_en_bod}",String.valueOf(prods_en_bod2));
		top.setVariable("{porc_en_bod}",Formatos.formatoNumeroSinDecimales(porc_en_bod2)+" %");
		top.setVariable("{prods_ronda_crea}",String.valueOf(prods_ronda_crea2));
		top.setVariable("{porc_ronda_crea}",Formatos.formatoNumeroSinDecimales(porc_ronda_crea2)+" %");
		top.setVariable("{prods_total_jor}",String.valueOf(prods_tot2));
		top.setVariable("{porc_total_jor}","100 %");
		
		
		//***************************************************************************
		//*****   5 . 2   R e s u m e n   X   Z o n a s   E s p e c i a l e s   *****
		//***************************************************************************
		logger.debug("5.2 Resumen x Zonas Especiales");
		ArrayList esp_res_zonas = new ArrayList();
		double esp_prods_no_asig2=0;
		double esp_prods_tot2=0;
		double esp_prods_en_bod2=0;
		double esp_prods_ronda_crea2=0;
		zona_anterior="";
		List esp_listazonas_prod = new ArrayList();
		ProcZonasJornadaDTO esp_datos_zon = new ProcZonasJornadaDTO();
		esp_datos_zon.setId_jornada(id_jornada);
		esp_datos_zon.setFlag_especiales(1);
		esp_listazonas_prod = bizDelegate.getTotalProductosZonasJornada(esp_datos_zon);
		
		for (int i=0; i<esp_listazonas_prod.size(); i++){
			logger.debug("i: "+i);
			IValueSet fila = new ValueSet();
			TotProdZonJorDTO zonas1 = (TotProdZonJorDTO)esp_listazonas_prod.get(i);
			
			String zona_actual=zonas1.getZona();
			logger.debug(i+".- zona anterior="+zona_anterior+" zona actual="+zona_actual);
			fila.setVariable("{borderstyle}","");
			if ((!zona_anterior.equals("")) && (!zona_anterior.equals(zona_actual))){ //borde grueso
				fila.setVariable("{borderstyle}","style='border-top: 2px solid #badb8a;'");
			}
			zona_anterior =zona_actual;
			
			//totales
			esp_prods_no_asig2 = Formatos.formatoNum3Dec(esp_prods_no_asig2 + zonas1.getCant_prods_no_asig());			
			esp_prods_tot2 = Formatos.formatoNum3Dec(esp_prods_tot2 + zonas1.getCant_prods());
			
			fila.setVariable("{zonas}"		,zonas1.getZona());
			fila.setVariable("{cant_prod}"	,String.valueOf(Formatos.formatoNum3Dec(zonas1.getCant_prods())));	
			fila.setVariable("{cant_prod_no_asig}"	,String.valueOf(Formatos.formatoNum3Dec(zonas1.getCant_prods_no_asig())));
						
			//obtiene rondas de la jornada + zona
			double ron_creada_prods=0;
			double ron_pick_prods=0;
			double ron_term_prods=0;
			
			double gen_ron_creada_prods=0;
			double gen_ron_creada_prods_spick =0;
			double gen_ron_term_prods=0;

			for(int j=0;j<listarondas_esp.size();j++){
				MonitorRondasDTO ron1 = (MonitorRondasDTO)listarondas_esp.get(j);
				logger.debug(i+"."+j+"id_ronda esp = "+ron1.getId_ronda()+ " spik:"+ron1.getCant_spick());
				if(ron1.getId_estado()==Constantes.ID_ESTADO_RONDA_FINALIZADA){
					gen_ron_creada_prods_spick+=ron1.getCant_spick();
					}
				List lst_det_r1 = new ArrayList();
				lst_det_r1 = bizDelegate.getProductosRonda(ron1.getId_ronda());
				for(int k=0;k<lst_det_r1.size();k++){
					ProductosPedidoDTO prod = (ProductosPedidoDTO)lst_det_r1.get(k);
					
					logger.debug("producto:"+prod.getId_producto()
							+" pedido:"+prod.getId_pedido()
							+" zona:"+prod.getId_zona()
							+" cant:"+prod.getCant_solic());
					
					//if (prod.getId_zona()!=zonas1.getId_zona())
					gen_ron_creada_prods+=prod.getCant_solic();
					if(ron1.getId_estado()==Constantes.ID_ESTADO_RONDA_CREADA){
						
						if (prod.getId_zona()==zonas1.getId_zona()){
							ron_creada_prods +=prod.getCant_solic();
						}
						
					}else if(ron1.getId_estado()==Constantes.ID_ESTADO_RONDA_EN_PICKING){
						if (prod.getId_zona()==zonas1.getId_zona()){
							ron_pick_prods +=prod.getCant_solic();
							}
					}else if(ron1.getId_estado()==Constantes.ID_ESTADO_RONDA_FINALIZADA){
						gen_ron_term_prods+=prod.getCant_solic();
						if (prod.getId_zona()==zonas1.getId_zona()){
							ron_term_prods +=prod.getCant_solic()- prod.getCant_spick();
						}
					}
					logger.debug(i+"."+j+"."+k+".- rondas creadas:"+ron_creada_prods
							+" ron_pick_prods:"+ron_pick_prods
							+" ron_term_prods:"+ron_term_prods);
					
				}				
			}
			// solo cuenta las rondas durante el rastreo de la primera zona se le resta los spik
			if (i==0){
				esp_prods_ronda_crea2 = 
						Formatos.formatoNum3Dec(
								esp_prods_ronda_crea2 + 
								gen_ron_creada_prods -
								gen_ron_creada_prods_spick);
				esp_prods_en_bod2 = Formatos.formatoNum3Dec(esp_prods_en_bod2 + gen_ron_term_prods-
						gen_ron_creada_prods_spick);
			}
			
			fila.setVariable("{cant_creada}", String.valueOf(Formatos.formatoNum3Dec(ron_creada_prods)));	
			fila.setVariable("{cant_enpick}", String.valueOf(Formatos.formatoNum3Dec(ron_pick_prods)));
			fila.setVariable("{cant_enbod}" , String.valueOf(Formatos.formatoNum3Dec(ron_term_prods)));	
			
			double avance = Formatos.formatoNum3Dec((ron_term_prods/zonas1.getCant_prods())*100);
			fila.setVariable("{avance}"		,Formatos.formatoNumero(avance)+" %");		
			esp_res_zonas.add(fila);
		}

		double esp_porc_no_asig2 = Formatos.formatoNum3Dec( 100 * (esp_prods_no_asig2 /esp_prods_tot2));
		double esp_porc_en_bod2 = Formatos.formatoNum3Dec( 100 * (esp_prods_en_bod2 /esp_prods_tot2));
		double esp_porc_ronda_crea2 = Formatos.formatoNum3Dec( 100 * (esp_prods_ronda_crea2 /esp_prods_tot2));
		
		//		setear contadores de productos y porcentajes para pedidos especiales
		top.setVariable("{prods_no_asig_esp}",String.valueOf(esp_prods_no_asig2));
		top.setVariable("{porc_no_asig_esp}",Formatos.formatoNumeroSinDecimales(esp_porc_no_asig2)+" %");
		top.setVariable("{prods_en_bod_esp}",String.valueOf(esp_prods_en_bod2));
		top.setVariable("{porc_en_bod_esp}",Formatos.formatoNumeroSinDecimales(esp_porc_en_bod2)+" %");
		top.setVariable("{prods_ronda_crea_esp}",String.valueOf(esp_prods_ronda_crea2));
		top.setVariable("{porc_ronda_crea_esp}",Formatos.formatoNumeroSinDecimales(esp_porc_ronda_crea2)+" %");
		top.setVariable("{prods_total_jor_esp}",String.valueOf(esp_prods_tot2));
		top.setVariable("{porc_total_jor_esp}","100 %");	
		

		//*******************************************************************************
		//*****   6 . 0   S e t e a   V a r i a b l e s   d e l   T e m p l a t e   *****
		//*******************************************************************************
		logger.debug("6.0 Setea Variables del Template");

		// mostrar el gif de impresion de comandas 
		top.setVariable("{vis_gif_ini}","");
		top.setVariable("{vis_gif_fin}","");
		/*		
		List lst_secc_sap = bizDelegate.getSeccionesSAPPreparadosByIdJornada(id_jornada);
		if(lst_secc_sap .size()==0){
			top.setVariable("{vis_gif_ini}","<!--");
			top.setVariable("{vis_gif_fin}","-->");
		}*/				
		
		
		top.setVariable("{id_jornada}"			,String.valueOf(jornada1.getId_jornada()));		
		top.setVariable("{fec_jornada}"			,jornada1.getF_jornada());
		top.setVariable("{hora_ini}"			,jornada1.getH_inicio());
		top.setVariable("{hora_term}"			,jornada1.getH_fin());
		top.setVariable("{jp_estado}"			,jornada1.getEstado());		
		top.setVariable("{n_pedidos}"			,String.valueOf(jornada1.getCant_pedidos()));
		top.setVariable("{cap_pick_uti}"		,String.valueOf(jornada1.getCapac_picking_ut()));
		top.setVariable("{cap_max_pick}"		,String.valueOf(jornada1.getCapac_picking_max()));
		top.setVariable("{utilizacion}"			,Formatos.formatoNumeroSinDecimales(jornada1.getPorc_utilizacion()));
		top.setVariable("{pedxpick}"			,String.valueOf(Formatos.formatoNum3Dec(jornada1.getCant_ped_por_pickear())));
		top.setVariable("{ped_picking}"			,String.valueOf(Formatos.formatoNum3Dec(jornada1.getCant_ped_en_picking())));
		top.setVariable("{ped_bode}"			,String.valueOf(Formatos.formatoNum3Dec(jornada1.getCant_ped_en_bodega())));
		top.setVariable("{prod_sin_asig}"		,String.valueOf(Formatos.formatoNum3Dec(jornada1.getCant_prod_sin_asignar())));
		top.setVariable("{porc_prod_sin_asig}"	,Formatos.formatoNumeroSinDecimales(jornada1.getPorc_prod_sin_asignar()));
		top.setVariable("{prod_bode}"			,String.valueOf(Formatos.formatoNum3Dec(jornada1.getCant_prod_en_bodega())));
		top.setVariable("{porc_prod_bode}"		,Formatos.formatoNumeroSinDecimales(jornada1.getPorc_prod_en_bodega()));
				
		// Condiciones de los botones
		top.setVariable("{jorn_enabled}", "disabled");
		top.setVariable("{ronda_enabled}", "disabled");
		
		// Si la jornada no ha sido iniciada, habilitamos botón Inicia Jornada 
		if( jornada1.getId_estado() == Constantes.ID_ESTADO_JORNADA_NO_INICIADA ){
			top.setVariable("{jorn_enabled}", "enabled");
		}
		
		// Si la jornada está en proceso, habilitamos botón Crear Ronda
		if ( jornada1.getId_estado() == Constantes.ID_ESTADO_JORNADA_EN_PROCESO){
			top.setVariable("{ronda_enabled}", "enabled");
		}
			
		// variables header
		Date hoy = new Date(); 
		top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}" , usr.getLocal());		
		top.setVariable("{hdr_fecha}" , hoy.toString());
		
		
		// 7. Setea variables bloques
		//top.setDynamicValueSets("select_sectores", sector);
		//top.setDynamicValueSets("select_sectores_esp", sector_esp);		
		top.setDynamicValueSets("select_res_zonas", res_zonas);
		top.setDynamicValueSets("select_res_zonas_esp", esp_res_zonas);
		
		top.setDynamicValueSets("select_pedidos", ped);
		top.setDynamicValueSets("select_pedidos_rest", ped_restante);
		top.setDynamicValueSets("select_pedidos_esp", ped_esp);
		top.setDynamicValueSets("select_pedidos_rest_esp", ped_restante_esp);
		top.setDynamicValueSets("select_rondas", ronda);
		top.setDynamicValueSets("select_rondas_esp", ronda_esp);

		
		top.setDynamicValueSets("select_zonas_res", zonas);
		
		// 8. variables del header
		/*top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());*/
		
		// 9. Salida Final (se deja tal cual)
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();		
	

	}
	
	
}
