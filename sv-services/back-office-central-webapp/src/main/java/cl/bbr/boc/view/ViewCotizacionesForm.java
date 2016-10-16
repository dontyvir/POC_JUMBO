package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.ZonaDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;
/**
 * muestra el detalle de la cotización
 * @author BBRI
 */
public class ViewCotizacionesForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long id_cot=0;
		String rc = "";
		String msje = "";
		long id_jornada = -1;
		
		View salida = new View(res);
//		 Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

//		2. Procesa parámetros del request
		
		if (req.getParameter("cot_id")!= null && !req.getParameter("cot_id").equals("")){
			id_cot =  Long.parseLong(req.getParameter("cot_id"));
			logger.debug("id_cot: " + id_cot);
		}
		if (req.getParameter("rc") != null ) rc = req.getParameter("rc");
		logger.debug("rc:"+rc);
		int mod = 0;
		if (req.getParameter("mod") != null && !req.getParameter("mod").equals("")){
			mod = Integer.parseInt(req.getParameter("mod"));
		}
		if (req.getParameter("msje") != null ) msje = req.getParameter("msje");
		logger.debug("msje:"+msje);
		
		if (req.getParameter("id_jornada") != null &&  !req.getParameter("id_jornada").equals(""))
			id_jornada = Long.parseLong(req.getParameter("id_jornada") );
		
		logger.debug("ID JORNADA: " + id_jornada);
//		3. Template	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();		
		
		
		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate bizDelegate = new BizDelegate();	
		
		//4.1 Detalle de Cotización
		
		CotizacionesDTO cot = bizDelegate.getCotizacionesById(id_cot);
		double totProd = bizDelegate.getTotalProductosCot(id_cot);
		
		// Tab General
		
		// Datos de la Cotización
		
		top.setVariable("{num_cot}"		  ,String.valueOf(cot.getCot_id()));
		top.setVariable("{nom_emp}"		  ,cot.getCot_nom_emp());
		top.setVariable("{suc_id}"		  ,String.valueOf(cot.getCot_cli_id()));
		top.setVariable("{nom_suc}"		  ,cot.getCot_nom_suc());
		top.setVariable("{nom_comp}"	  ,cot.getCot_nom_comp());
		top.setVariable("{fec_ing}"		  ,Formatos.frmFecha(cot.getCot_fec_ingreso()));
		top.setVariable("{fec_venc}"	  ,Formatos.frmFecha(cot.getCot_fec_vencimiento()));	
		top.setVariable("{estado}"		  ,cot.getCot_estado());
		top.setVariable("{monto}"		  ,String.valueOf(cot.getCot_monto_total()));
		top.setVariable("{costo_desp}"	  ,String.valueOf(cot.getCot_costo_desp()));
		top.setVariable("{total}"		  ,String.valueOf(cot.getCot_monto_total() + cot.getCot_costo_desp()));
		top.setVariable("{tot_productos}" ,String.valueOf(totProd));
		top.setVariable("{loc_id}"        ,String.valueOf(cot.getCot_loc_id()));
		top.setVariable("{loc_desp}"    ,cot.getCot_nom_local());
		if (cot.getCot_tipo_doc().equals(Constantes.TIPO_DOC_FACTURA)){
			top.setVariable("{check_F}"	  ,"checked");	
			top.setVariable("{hab_sel_dirfact}"	  ,"");
		}else if (cot.getCot_tipo_doc().equals(Constantes.TIPO_DOC_BOLETA)){
			top.setVariable("{check_B}"	  ,"checked");
			top.setVariable("{hab_sel_dirfact}"	  ,"disabled");
		}else{
			top.setVariable("{check_F}"	  ,"");
			top.setVariable("{check_B}"	  ,"");
			top.setVariable("{hab_sel_dirfact}"	  ,"disabled");
		}
		
		if (cot.getCot_id_usuario_fono() > 0){
			UserDTO userFono = bizDelegate.getUserById(cot.getCot_id_usuario_fono());
			String UsuarioFonoCompra = "<tr>"
                                     + "   <td>Operador Fono Compras</td>"
                                     + "   <td>" + userFono.getNombre() + " " + userFono.getApe_paterno() + " " + userFono.getApe_materno() + "</td>"
                                     + "</tr>";
			top.setVariable("{oper_fono}", UsuarioFonoCompra);
		}else{
			top.setVariable("{oper_fono}", "");
		}
		
		if (id_jornada == -1){
			top.setVariable("{fec_desp}"	  ,Formatos.frmFechaHora(cot.getCot_fec_acordada()));
			top.setVariable("{id_jornada}"    ,String.valueOf(cot.getCot_id_jor_desp_ref()));
		}else{
			top.setVariable("{id_jornada}"    ,String.valueOf(id_jornada));
			//Obtiene la Fecha de despacho segun el id de jornada
			JorDespachoCalDTO jdesp = bizDelegate.getJornadaDespachoById(id_jornada);
			
			top.setVariable("{fec_desp}" ,Formatos.frmFecha(jdesp.getFecha()) + " " +jdesp.getH_ini());
			
			
		}

		
		
		
		// Obtiene la lista de direcciones de facturación 
		List lst_dirfact =  bizDelegate.getListDireccionFactBySucursal(cot.getCot_cli_id());
		logger.debug("lst_dirfact.size(): "+lst_dirfact.size());
		ArrayList dirfact = new ArrayList();
		for(int i=0; i<lst_dirfact.size(); i++){
			IValueSet fila = new ValueSet();
			DirFacturacionDTO df= (DirFacturacionDTO)lst_dirfact.get(i);
			//Revisamos si existe alguna seleccionada
			logger.debug("dfac sel: " +cot.getCot_dirfac_id());
			logger.debug("dfac : " +df.getDfac_id());
			if (cot.getCot_dirfac_id() == df.getDfac_id()){
				fila.setVariable("{sel_df}"	   ,"selected");
			}else{
				fila.setVariable("{sel_df}"	   ,"");
			}
			fila.setVariable("{df_id}"	   ,String.valueOf(df.getDfac_id()));
			String direc = "";
			if (df.getNom_tip_calle()!= null){
				direc += df.getNom_tip_calle()+" ";
			}
			if (df.getDfac_calle() != null){
				direc += df.getDfac_calle()+" ";
			}
			if (df.getDfac_numero()!= null){
				direc += df.getDfac_numero()+" ";
			}
			if (df.getDfac_depto() != null ){
				direc += df.getDfac_depto()+" ";
			}
			if (df.getNom_comuna()!= null){
				direc += df.getNom_comuna()+" ";
			}
			if (df.getDfac_ciudad() != null){
				direc += df.getDfac_ciudad();
			}
			
			fila.setVariable("{direccion}" , direc);
			dirfact.add(fila);
		}	
//		 Obtiene la lista de direcciones de despacho 
		List lst_dirdesp =  bizDelegate.getListDireccionDespBySucursal(cot.getCot_cli_id());
		logger.debug("lst_dirfact.size(): "+lst_dirdesp.size());
		ArrayList dirdesp = new ArrayList();
		for(int i=0; i<lst_dirdesp.size(); i++){
			IValueSet fila = new ValueSet();
			DireccionesDTO dd= (DireccionesDTO)lst_dirdesp.get(i);
			//Revisamos si existe alguna seleccionada
			logger.debug("ddesp sel: " +cot.getCot_dir_id());
			logger.debug("ddesp : " +dd.getId());
			fila.setVariable("{i}"	   ,String.valueOf(i));
			if (cot.getCot_dir_id() == dd.getId()){
				fila.setVariable("{sel_dd}"	   ,"selected");
			}else{
				fila.setVariable("{sel_dd}"	   ,"");
			}
			
			fila.setVariable("{dd_id}"	   ,String.valueOf(dd.getId()));
			fila.setVariable("{nom_local}" ,dd.getNom_local() );
			String direc_desp = "";
			if (dd.getCalle()!= null){
				direc_desp += dd.getCalle()+" ";
			}
			if (dd.getNumero() != null){
				direc_desp += dd.getNumero()+" ";
			}
			if (dd.getNom_comuna()!= null){
				direc_desp += dd.getNom_comuna()+" ";
			}
			logger.debug("dir desp zone:"+dd.getZona_id());
			if (dd.getZona_id()>0){
				//obtiene el nombre de la zona
				ZonaDTO zona = bizDelegate.getZonaById(dd.getZona_id());
				if (zona!=null)
					direc_desp += " Zona=["+zona.getNombre()+"] ";
			}
			logger.debug("direcc_desp + zona :"+direc_desp);
			fila.setVariable("{dir_desp}" , direc_desp);
			dirdesp.add(fila);
			logger.debug("id dir : " + dd.getId() + " nombre local: " + dd.getNom_local() );
		}
		UserDTO usuario = bizDelegate.getUserById(cot.getCot_id_usuario());
		String nom_usu = "";
		if (usuario.getNombre()!= null)
			nom_usu += usuario.getNombre();
		if (usuario.getApe_paterno() != null)
			nom_usu += " " +usuario.getApe_paterno();
		top.setVariable("{usuario_edita}", nom_usu);
		
		logger.debug("aut_margen:"+cot.getCot_aut_margen());
		if (cot.getCot_aut_margen().equals(Constantes.INDICADOR_SI)){
			top.setVariable("{aut_margen_on}"   ,"selected");
			top.setVariable("{aut_margen_off}"   ,"");
			}
		else{
			top.setVariable("{aut_margen_on}"   ,"");
			top.setVariable("{aut_margen_off}"   ,"selected");
			}
		
		logger.debug("aut_dscto:"+cot.getCot_aut_dscto());
		if (cot.getCot_aut_dscto().equals(Constantes.INDICADOR_SI)){
			top.setVariable("{aut_dscto_on}"   ,"selected");
			top.setVariable("{aut_dscto_off}"   ,"");
			}
		else{			
			top.setVariable("{aut_dscto_on}"   ,"");
			top.setVariable("{aut_dscto_off}"   ,"selected");			
		}
		
		// Datos del medio de pago 
		top.setVariable("{medio_pago}"    ,cot.getCot_mpago());
		String tipo_tarjeta = "";
		if (cot.getCot_nomtbank()!= null){
			tipo_tarjeta =cot.getCot_nomtbank(); 
		}
		top.setVariable("{tipo_tarjeta}"  ,tipo_tarjeta);
		String mp = "";
		ResourceBundle rb = ResourceBundle.getBundle("bo");
		String key = rb.getString("conf.bo.key");
		if (cot.getCot_num_mpago()!= null){
            mp= Cifrador.desencriptar( key, cot.getCot_num_mpago() );
			//mp = cot.getCot_num_mpago();
		}
		if (mp.length() > 4)
			mp = "************"+mp.substring(mp.length()-4);
		top.setVariable("{num_tarjeta}"   ,mp);
		if (cot.getFecha_expira() != null && !cot.getFecha_expira().equals("")){
		    top.setVariable("{fec_expira}", cot.getFecha_expira());
		}else{
		    top.setVariable("{fec_expira}", "");
		}
		top.setVariable("{num_cuotas}"    ,String.valueOf(cot.getCot_ncuotas()));
		if (cot.getCot_obs()!=null)
			top.setVariable("{obs}"       ,cot.getCot_obs());
		else
			top.setVariable("{obs}"       ,"");
		if (cot.getCot_fueramix()!= null)
			top.setVariable("{prod_fuera_mix}",String.valueOf(cot.getCot_fueramix()));
		else
			top.setVariable("{prod_fuera_mix}","");
		
		String nom_boton = "";
		String enlace = "";
		if (cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_EN_REVISION){
			nom_boton = "Cambiar a Estado Gestión Fuera Mix";
			enlace = "CambiaCotGestionFueraMix?cot_id="+cot.getCot_id();
		}else{ 
			nom_boton = "Cambiar a Estado En Revisión";
			enlace = "CambiaCotEnRevision?cot_id="+cot.getCot_id();
		}
		
		top.setVariable("{nom_btn}", nom_boton );
		// Permisos de modificacion segun variable MOD
		if (mod == 0){			
			top.setVariable("{habgc}", "disabled" );
			top.setVariable("{habgp}", "disabled" );
			//top.setVariable("{habac}", "disabled" );
			top.setVariable("{hablc}", "disabled" );
			top.setVariable("{habemp}", "disabled" );
			top.setVariable("{habsuc}", "disabled" );
			top.setVariable("{habvc}", "disabled" );
			top.setVariable("{habcc}", "disabled" );
			top.setVariable("{habap}", "disabled" );
			top.setVariable("{habad}", "disabled" );
			top.setVariable("{habgd}", "disabled" );
			top.setVariable("{habal}", "disabled" );
			top.setVariable("{hab_btn}", "disabled" );
			top.setVariable("{mod}", "0" );
			top.setVariable("{hab_input_costo}", "disabled" );
			top.setVariable("{hab_select_tipo_doc}", "disabled" );
			top.setVariable("{hab_sel_dirfact}", "disabled" );
			top.setVariable("{hab_sel_dirdesp}", "disabled" );
			top.setVariable("{hab_obs1}", "disabled" );
			top.setVariable("{hab_ped_fuera_mix}", "disabled" );
			top.setVariable("{hab_fecha_venc}", "disabled" );
			top.setVariable("{hab_aut_margen}", "disabled" );
			top.setVariable("{hab_aut_dscto}", "disabled" );
			top.setVariable("{enlace}", enlace+"&mod=0");
		}else{
			top.setVariable("{hablc}", "enabled" );
			top.setVariable("{habemp}", "enabled" );
			top.setVariable("{habsuc}", "enabled" );
			top.setVariable("{habap}", "enabled" );
			top.setVariable("{habad}", "enabled" );
			top.setVariable("{habgd}", "enabled" );
			top.setVariable("{habal}", "enabled" );
			top.setVariable("{mod}", "1" );
			top.setVariable("{habgc}", "enabled" );
			top.setVariable("{hab_input_costo}", "enabled" );
			top.setVariable("{hab_select_tipo_doc}", "enabled" );
			top.setVariable("{hab_sel_dirfact}", "enabled" );
			top.setVariable("{hab_sel_dirdesp}", "enabled" );
			top.setVariable("{hab_obs1}", "enabled" );
			top.setVariable("{hab_ped_fuera_mix}", "enabled" );
			top.setVariable("{hab_fecha_venc}", "enabled" );
			top.setVariable("{enlace}", enlace+"&mod=1");
			List perfiles_usr = usr.getPerfiles();
			boolean es_supervisor = false;
			for (int i=0; i<perfiles_usr.size();i++){
				PerfilesEntity per = (PerfilesEntity) perfiles_usr.get(i);
				if (per.getIdPerfil().intValue()==Constantes.ID_PERFIL_SUPERVISOR_VE){
					es_supervisor = true;
				}
			}
			
			if(es_supervisor){
				logger.debug("Es supervisor");
				top.setVariable("{hab_aut_margen}", "enabled" );
				top.setVariable("{hab_aut_dscto}", "enabled" );
				}
			else{
				/* DEBUG ONLY 
				top.setVariable("{hab_aut_margen}", "enabled" );
				top.setVariable("{hab_aut_dscto}", "enabled" );
				*/
				/* finalmente debe quedar con esto*/  
				top.setVariable("{hab_aut_margen}", "disabled" );
				top.setVariable("{hab_aut_dscto}", "disabled" );
				
			}
			
			//Boton Generar Cotizaciones
			if (cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_ACEPTADA
				|| 	cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_EN_REALIZACION){
					top.setVariable("{habgp}", "enabled" );
			}else{	top.setVariable("{habgp}", "disabled" );	}
			
			//Boton Anular Cotizacion
			/*if (cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_INGRESADA
					|| 	cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_COTIZADA){
					top.setVariable("{habac}", "enabled" );
			}else{	top.setVariable("{habac}", "disabled" );}*/
			
			//Boton Validar Cotizacion
			if (cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_EN_REVISION
				|| cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_EN_VALIDACION	){
					top.setVariable("{habvc}", "enabled" );
			}else{	top.setVariable("{habvc}", "disabled" );}
			
			//Boton Confirmar Cotizacion
			if (cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_EN_VALIDACION){
					top.setVariable("{habcc}", "enabled" );
			}else{	top.setVariable("{habcc}", "disabled" );}
			
			// Boton Cambiar a Gestion Fuera Mix / Revision
			if (cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_EN_REVISION ||
			    cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_GEST_FUERA_MIX){
				top.setVariable("{hab_btn}", "enabled" );
			}else{
				top.setVariable("{hab_btn}", "disabled" );
			}
			
			//Segun el estado se deshabilitan botones a granel
			if (cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_INGRESADA){
				top.setVariable("{habgc}", "disabled" );
				top.setVariable("{habgp}", "disabled" );
				//top.setVariable("{habac}", "disabled" );
				//top.setVariable("{hablc}", "disabled" );
				top.setVariable("{habemp}", "disabled" );
				top.setVariable("{habsuc}", "disabled" );
				top.setVariable("{habvc}", "disabled" );
				top.setVariable("{habcc}", "disabled" );
				top.setVariable("{habap}", "disabled" );
				top.setVariable("{habad}", "disabled" );
				top.setVariable("{habgd}", "disabled" );
				top.setVariable("{hab_btn}", "disabled" );
				top.setVariable("{mod}", "0" );
				top.setVariable("{nom_btn}","Cambiar a Estado En Revisión");
				top.setVariable("{hab_input_costo}", "disabled" );
				top.setVariable("{hab_select_tipo_doc}", "disabled" );
				top.setVariable("{hab_sel_dirfact}", "disabled" );
				top.setVariable("{hab_sel_dirdesp}", "disabled" );
				top.setVariable("{hab_obs1}", "disabled" );
				top.setVariable("{hab_ped_fuera_mix}", "disabled" );
				top.setVariable("{hab_fecha_venc}", "disabled" );				
			}

		}
			
		
		
		// Tab Empresas
		
		// Datos de la empresa
		logger.debug("Empresa id: " + cot.getCot_emp_id());
		EmpresasDTO emp = bizDelegate.getEmpresaById(cot.getCot_emp_id());
		top.setVariable("{emp_id}"     ,String.valueOf(emp.getEmp_id()));
		top.setVariable("{rut_emp}"    ,emp.getEmp_rut() + "-" + emp.getEmp_dv());
		top.setVariable("{raz_soc}"    ,emp.getEmp_rzsocial());
		top.setVariable("{emp_nombre}" ,emp.getEmp_nom());
		top.setVariable("{desc}"       ,emp.getEmp_descr());
		top.setVariable("{mail}"       ,emp.getEmp_mail_contacto());
		top.setVariable("{fono1}"      ,emp.getEmp_fono1_contacto());
		top.setVariable("{fono2}"      ,emp.getEmp_fono2_contacto());
		top.setVariable("{fec_crea}"   ,Formatos.frmFecha(emp.getEmp_fec_crea()));
		
		double margen_minimo_emp = emp.getEmp_mrg_minimo();
		double dscto_max_emp = emp.getEmp_dscto_max();
		
		/* datos de LINEA CREDITO
		 * Debe extraer el monto total disponible y contrastarlo con los pedidos pendientes, la resta entrega el disponible real.
		 * Ademas debe permitir que si se le agrega un nuevo producto, debe realizar la comprobacion con el nuevo monto total
		 * Si hay disponible puede permitir la generacion de pedidos si no lanza un mensaje
		 */
		
		double saldo_reportado_if = emp.getEmp_saldo();
		double saldo_pendiente  = bizDelegate.getSaldoActualPendiente(cot.getCot_emp_id());
		double disponible_actual = saldo_reportado_if - saldo_pendiente;
		
		//obtiene monto total en variable suma_precios_con_descto 
		double suma_precios_con_descto=0.0;
		List listaProductos = null;
		listaProductos =  bizDelegate.getProductosCotiz(id_cot);
		for (int i=0; i<listaProductos.size(); i++){			
			IValueSet fila = new ValueSet();
			ProductosCotizacionesDTO prod = new ProductosCotizacionesDTO();
			prod = (ProductosCotizacionesDTO)listaProductos.get(i);
			/*
			 if (document.form.descuento_{i}.value==""){	
				suma_precios_con_descto+= document.form.cantidad_{i}.value *{precio_lista};}
			else{	
				suma_precios_con_descto+= document.form.cantidad_{i}.value *{precio_lista} * (1 - (document.form.descuento_{i}.value/100) );	}					
			 */
			
			if (prod.getDetcot_dscto_item()<=0.0){
				suma_precios_con_descto+=  prod.getDetcot_cantidad() * prod.getDetcot_precio_lista();
			}
			else{
				suma_precios_con_descto+=  prod.getDetcot_cantidad() * prod.getDetcot_precio_lista() *(1-prod.getDetcot_dscto_item()/100);						
			}
		}
		logger.debug("monto total cotizacion con descuentos:"+suma_precios_con_descto);
		top.setVariable("{res_monto_total}",String.valueOf(suma_precios_con_descto));		
		//top.setVariable("{genera_pedidos_onlcick}", "location.href='ViewGeneraPedidos?id_cotizacion="+id_cot+"'" );
		
		if ((cot.getCot_mpago().equals(Constantes.MEDIO_PAGO_LINEA_CREDITO))
				&& ((cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_ACEPTADA) 
						|| (cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_EN_REALIZACION))  )
			top.setVariable("{estado_a_o_r}", "S"); //Si genera la validacion, al generar pedidos
		else
			top.setVariable("{estado_a_o_r}", "N"); //No realiza la validacion, al generar pedidos
		
		if ((cot.getCot_mpago().equals(Constantes.MEDIO_PAGO_LINEA_CREDITO))
				&& ((cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_ACEPTADA) 
						|| (cot.getCot_estado_id() == Constantes.ID_EST_COTIZACION_EN_REALIZACION))  ) {
			top.setVariable("{hab_datos_lcredito_ini}","");
			top.setVariable("{hab_datos_lcredito_fin}","");
			
			top.setVariable("{lc_saldo_reportado}", Formatos.formatoPrecio(saldo_reportado_if));
			top.setVariable("{lc_saldo_pendientes}", Formatos.formatoPrecio(saldo_pendiente));
			top.setVariable("{lc_disponible_actual}", Formatos.formatoPrecio(disponible_actual));
			
			//debe generar mensaje que impida la generacion de pedidos si el saldo no es disponible
			
			
			double saldo_cotizacion = disponible_actual - (suma_precios_con_descto + cot.getCot_costo_desp());			
			top.setVariable("{saldo_empresa}", String.valueOf(disponible_actual));
			// TODO dejar los onlick con if javascript donde analice el  res_monto_total para decidir si procede o no
			if(saldo_cotizacion <= 0.0){
				logger.debug("No puede generar pedidos en estado ACEPTADO/EN REALIZACION");				
			}
			else{
				logger.debug("puede generar pedidos en estado ACEPTADO/EN REALIZACION");
				//mostrar alert al presionar generar pedidos
			}
		}
		else{
			top.setVariable("{hab_datos_lcredito_ini}","<!--");
			top.setVariable("{hab_datos_lcredito_fin}","-->");
			
			top.setVariable("{lc_saldo_reportado}"       ,"");
			top.setVariable("{lc_saldo_pendientes}"       ,"");
			top.setVariable("{lc_disponible_actual}"       ,"");
			
			top.setVariable("{saldo_empresa}", "0");
		}
		
		// Datos de la Sucursal
		logger.debug("Sucursal Id: " + cot.getCot_cli_id());
		SucursalesDTO suc = bizDelegate.getSucursalById(cot.getCot_cli_id());
		top.setVariable("{suc_id}"         ,String.valueOf(suc.getSuc_id()));
		top.setVariable("{rut_suc}"        ,suc.getSuc_rut() + "-" + suc.getSuc_dv());
		top.setVariable("{raz_soc_suc}"    ,suc.getSuc_razon());
		top.setVariable("{suc_nombre}"     ,suc.getSuc_nombre());
		top.setVariable("{desc_suc}"       ,suc.getSuc_descr());
		top.setVariable("{mail_suc}"       ,suc.getSuc_email());
		
		String fono1 = "";
		if (suc.getSuc_fono_cod1()!= null)
			fono1 = "("+suc.getSuc_fono_cod1()+") ";
		if (suc.getSuc_fono_num1()!= null)
			fono1 +=  suc.getSuc_fono_num1();
		
		String fono2 = "";
		if (suc.getSuc_fono_cod1()!= null)
			fono2 = "("+suc.getSuc_fono_cod2()+") ";
		if (suc.getSuc_fono_num2()!= null)
			fono2 +=  suc.getSuc_fono_num2();
		
		top.setVariable("{fono1_suc}"      ,fono1);
		top.setVariable("{fono2_suc}"      ,fono2);
		top.setVariable("{fec_crea_suc}"   ,Formatos.frmFecha(suc.getSuc_fec_crea()));
		
		// Tab Validaciones
		
		// Tab Productos
		top.setVariable("{mar_min_emp}"      ,String.valueOf(margen_minimo_emp));
		top.setVariable("{dscto_max_emp}"      ,String.valueOf(dscto_max_emp));
		
		//manejo de rc
		if ( rc.equals(Constantes._EX_COT_ID_NO_EXISTE) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('El código de cotizacion no existe');</script>" );
		}else if ( rc.equals(Constantes._EX_COT_TIENE_ALERTA_ACT) ){
			top.setVariable( "{mns}"	, "<script language='JavaScript'>alert('La cotizacion tiene alertas activas');</script>" );
		} else {
			top.setVariable( "{mns}"	, "");
		}
		
		top.setVariable("{id_cot}"   ,String.valueOf(id_cot));
		top.setVariable("{id_est_cot}"   ,String.valueOf(cot.getCot_estado_id()));
		//sirve para llenar las variables del header
		
		// 6. Setea Variables de bloque
		
		top.setDynamicValueSets("DIR_FACT", dirfact);
		top.setDynamicValueSets("DIR_DESP", dirdesp);

//		sirve para llenar las variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		
		if(!msje.equals("")){
			top.setVariable( "{msje}"	, msje);
		}else{
			top.setVariable( "{msje}"	, "");
		}
		
		String result = tem.toString(top);
		
		salida.setHtmlOut(result);
		salida.Output();	
	}

}