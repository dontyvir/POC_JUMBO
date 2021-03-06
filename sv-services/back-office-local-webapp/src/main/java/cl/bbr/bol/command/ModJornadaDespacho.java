package cl.bbr.bol.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaPickingEntity;
import cl.bbr.jumbocl.pedidos.dto.AdmHorarioDespachoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que modifica una jornada de despacho
 * 
 * @author mleiva
  */
public class ModJornadaDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res,
			UserDTO usr) throws Exception {
        logger.info("--------------------------------------------------------------------------------------------");
		logger.info("Modificar la jornada de DESPACHO: " + usr.getLogin()
				+ " (" + usr.getNombre() + " " + usr.getApe_paterno() + ")");

		String paramUrl			= "";		
		String paramId_semana	= "";
		String paramId_hora_desp= "";
		String paramH_inicio 	= "";
		String paramH_fin		= "";
		String paramCapac_lu	= "";
		String paramCapac_ma	= "";
		String paramCapac_mi	= "";
		String paramCapac_ju	= "";
		String paramCapac_vi	= "";
		String paramCapac_sa	= "";
		String paramCapac_do	= "";
        String id_zona= "";
        
		int capac_lu			=-1;
		int capac_ma			=-1;
		int capac_mi			=-1;
		int capac_ju			=-1;
		int capac_vi			=-1;
		int capac_sa			=-1;
		int capac_do			=-1;
		
		long jor_lu				=-1;
		long jor_ma				=-1;
		long jor_mi				=-1;
		long jor_ju				=-1;
		long jor_vi				=-1;
		long jor_sa				=-1;
		long jor_do				=-1;		
		
		int tarifa_normal_lu = -1;
		int tarifa_normal_ma = -1;
		int tarifa_normal_mi = -1;
		int tarifa_normal_ju = -1;
		int tarifa_normal_vi = -1;
		int tarifa_normal_sa = -1;
		int tarifa_normal_do = -1;
		
		int tarifa_economica_lu = -1;
		int tarifa_economica_ma = -1;
		int tarifa_economica_mi = -1;
		int tarifa_economica_ju = -1;
		int tarifa_economica_vi = -1;
		int tarifa_economica_sa = -1;
		int tarifa_economica_do = -1;
		
		int tarifa_express_lu = -1;
		int tarifa_express_ma = -1;
		int tarifa_express_mi = -1;
		int tarifa_express_ju = -1;
		int tarifa_express_vi = -1;
		int tarifa_express_sa = -1;
		int tarifa_express_do = -1;
		
		int tarifa_umbral_lu = -1;
		int tarifa_umbral_ma = -1;
		int tarifa_umbral_mi = -1;
		int tarifa_umbral_ju = -1;
		int tarifa_umbral_vi = -1;
		int tarifa_umbral_sa = -1;
		int tarifa_umbral_do = -1;
		
		String UrlError = getServletConfig().getInitParameter("UrlError");
		
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		paramUrl = req.getParameter("url");
		
		if ( req.getParameter("id_semana") == null){
			throw new ParametroObligatorioException("id_semana es null");			
		}
		paramId_semana = req.getParameter("id_semana");
		
		if ( req.getParameter("id_zona") == null){
			throw new ParametroObligatorioException("id_zona es null");			
		}
		id_zona = req.getParameter("id_zona");
		
		if (req.getParameter("id_hor_desp") == null) {
			throw new ParametroObligatorioException("id_hor_desp es null");
		}
		paramId_hora_desp = req.getParameter("id_hor_desp");
        
		if ( req.getParameter("h_ini") == null){
			throw new ParametroObligatorioException("Hora de inicio es null");			
		}
		paramH_inicio = req.getParameter("h_ini");		
		
		if ( req.getParameter("h_fin") == null){
			throw new ParametroObligatorioException("Hora de fin es null");			
		}
		paramH_fin = req.getParameter("h_fin");		
		
		if ( req.getParameter("capac_desp_lu") == null){
			throw new ParametroObligatorioException(
					"Capacidad del Lunes es null");
		}
		paramCapac_lu = req.getParameter("capac_desp_lu");
		if(paramCapac_lu.indexOf(",")<0){
			capac_lu = Integer.parseInt(paramCapac_lu);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
		
		if ( req.getParameter("capac_desp_ma") == null){
			throw new ParametroObligatorioException(
					"Capacidad del Martes es null");
		}
		paramCapac_ma = req.getParameter("capac_desp_ma");
		if(paramCapac_ma.indexOf(",")<0){
			capac_ma = Integer.parseInt(paramCapac_ma);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
		
		if ( req.getParameter("capac_desp_mi") == null){
			throw new ParametroObligatorioException(
					"Capacidad del Miercoles es null");
		}
		paramCapac_mi = req.getParameter("capac_desp_mi");
		if(paramCapac_mi.indexOf(",")<0){
			capac_mi = Integer.parseInt(paramCapac_mi);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
		
		if ( req.getParameter("capac_desp_ju") == null){
			throw new ParametroObligatorioException(
					"Capacidad del Jueves es null");
		}
		paramCapac_ju = req.getParameter("capac_desp_ju");
		if(paramCapac_ju.indexOf(",")<0){
			capac_ju = Integer.parseInt(paramCapac_ju);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
		
		if ( req.getParameter("capac_desp_vi") == null){
			throw new ParametroObligatorioException(
					"Capacidad del Viernes es null");
		}
		paramCapac_vi = req.getParameter("capac_desp_vi");
		if(paramCapac_vi.indexOf(",")<0){
			capac_vi = Integer.parseInt(paramCapac_vi);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
        
		if ( req.getParameter("capac_desp_sa") == null){
			throw new ParametroObligatorioException(
					"Capacidad del Sabado es null");
		}
		paramCapac_sa = req.getParameter("capac_desp_sa");
		if(paramCapac_sa.indexOf(",")<0){
			capac_sa = Integer.parseInt(paramCapac_sa);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
		
		if ( req.getParameter("capac_desp_do") == null){
			throw new ParametroObligatorioException(
					"Capacidad del Domingo es null");
		}
		paramCapac_do = req.getParameter("capac_desp_do");
		if(paramCapac_do.indexOf(",")<0){
			capac_do = Integer.parseInt(paramCapac_do);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
        
		if ( req.getParameter("id_jor_lu") == null){
			throw new ParametroObligatorioException("Jornada del Lunes es null");			
		}
		jor_lu = Integer.parseInt(req.getParameter("id_jor_lu"));
		
		if ( req.getParameter("id_jor_ma") == null){
			throw new ParametroObligatorioException(
					"Jornada del Martes es null");
		}
		jor_ma = Integer.parseInt(req.getParameter("id_jor_ma"));
        
		if ( req.getParameter("id_jor_mi") == null){
			throw new ParametroObligatorioException(
					"Jornada del Miercoles es null");
		}
		jor_mi = Integer.parseInt(req.getParameter("id_jor_mi"));
        
		if ( req.getParameter("id_jor_ju") == null){
			throw new ParametroObligatorioException(
					"Jornada del Jueves es null");
		}
		jor_ju = Integer.parseInt(req.getParameter("id_jor_ju"));
        
		if ( req.getParameter("id_jor_vi") == null){
			throw new ParametroObligatorioException(
					"Jornada del Viernes es null");
		}
		jor_vi = Integer.parseInt(req.getParameter("id_jor_vi"));
        
		if ( req.getParameter("id_jor_sa") == null){
			throw new ParametroObligatorioException(
					"Jornada del Sabado es null");
		}
		jor_sa = Integer.parseInt(req.getParameter("id_jor_sa"));
        
		if ( req.getParameter("id_jor_do") == null){
			throw new ParametroObligatorioException(
					"Jornada del Domingo es null");
		}
		jor_do = Integer.parseInt(req.getParameter("id_jor_do"));
        
		//*****************************************************************************
		// *********************** T A R I F A N O R M A L
		// ***********************
		//*****************************************************************************
		if ( req.getParameter("tarifa_normal_lu") == null){
			throw new ParametroObligatorioException("Precio del Lunes es null");			
		}
		tarifa_normal_lu = Integer.parseInt(req
				.getParameter("tarifa_normal_lu"));
		
		if ( req.getParameter("tarifa_normal_ma") == null){
			throw new ParametroObligatorioException("Precio del Martes es null");			
		}
		tarifa_normal_ma = Integer.parseInt(req
				.getParameter("tarifa_normal_ma"));
		
		if ( req.getParameter("tarifa_normal_mi") == null){
			throw new ParametroObligatorioException(
					"Precio del Miercoles es null");
		}
		tarifa_normal_mi = Integer.parseInt(req
				.getParameter("tarifa_normal_mi"));
		
		if ( req.getParameter("tarifa_normal_ju") == null){
			throw new ParametroObligatorioException("Precio del Jueves es null");			
		}
		tarifa_normal_ju = Integer.parseInt(req
				.getParameter("tarifa_normal_ju"));
		
        if ( req.getParameter("tarifa_normal_vi") == null){
			throw new ParametroObligatorioException(
					"Precio del Viernes es null");
		}
		tarifa_normal_vi = Integer.parseInt(req
				.getParameter("tarifa_normal_vi"));
		
		if ( req.getParameter("tarifa_normal_sa") == null){
			throw new ParametroObligatorioException("Precio del Sabado es null");			
		}
		tarifa_normal_sa = Integer.parseInt(req
				.getParameter("tarifa_normal_sa"));
		
		if ( req.getParameter("tarifa_normal_do") == null){
			throw new ParametroObligatorioException(
					"Precio del Domingo es null");
		}
		tarifa_normal_do = Integer.parseInt(req
				.getParameter("tarifa_normal_do"));
				
		//*****************************************************************************
		// ******************** T A R I F A E C O N O M I C A
		// ********************
		//*****************************************************************************
		if ( req.getParameter("tarifa_economica_lu") == null){
			throw new ParametroObligatorioException("Precio del Lunes es null");			
		}
		tarifa_economica_lu = Integer.parseInt(req
				.getParameter("tarifa_economica_lu"));
		
		if ( req.getParameter("tarifa_economica_ma") == null){
			throw new ParametroObligatorioException("Precio del Martes es null");			
		}
		tarifa_economica_ma = Integer.parseInt(req
				.getParameter("tarifa_economica_ma"));
		
		if ( req.getParameter("tarifa_economica_mi") == null){
			throw new ParametroObligatorioException(
					"Precio del Miercoles es null");
		}
		tarifa_economica_mi = Integer.parseInt(req
				.getParameter("tarifa_economica_mi"));
		
		if ( req.getParameter("tarifa_economica_ju") == null){
			throw new ParametroObligatorioException("Precio del Jueves es null");			
		}
		tarifa_economica_ju = Integer.parseInt(req
				.getParameter("tarifa_economica_ju"));
		
		if ( req.getParameter("tarifa_economica_vi") == null){
			throw new ParametroObligatorioException(
					"Precio del Viernes es null");
		}
		tarifa_economica_vi = Integer.parseInt(req
				.getParameter("tarifa_economica_vi"));
		
		if ( req.getParameter("tarifa_economica_sa") == null){
			throw new ParametroObligatorioException("Precio del Sabado es null");			
		}
		tarifa_economica_sa = Integer.parseInt(req
				.getParameter("tarifa_economica_sa"));
		
		if ( req.getParameter("tarifa_economica_do") == null){
			throw new ParametroObligatorioException(
					"Precio del Domingo es null");
		}
		tarifa_economica_do = Integer.parseInt(req
				.getParameter("tarifa_economica_do"));
		
        //****************************************************************************
		// ********************** T A R I F A E X P R E S S
		// *********************
		//****************************************************************************
		if ( req.getParameter("tarifa_express_lu") == null){
			throw new ParametroObligatorioException("Precio del Lunes es null");			
		}
		tarifa_express_lu = Integer.parseInt(req
				.getParameter("tarifa_express_lu"));
		
		if ( req.getParameter("tarifa_express_ma") == null){
			throw new ParametroObligatorioException("Precio del Martes es null");			
		}
		tarifa_express_ma = Integer.parseInt(req
				.getParameter("tarifa_express_ma"));
		
		if ( req.getParameter("tarifa_express_mi") == null){
			throw new ParametroObligatorioException(
					"Precio del Miercoles es null");
		}
		tarifa_express_mi = Integer.parseInt(req
				.getParameter("tarifa_express_mi"));
		
		if ( req.getParameter("tarifa_express_ju") == null){
			throw new ParametroObligatorioException("Precio del Jueves es null");			
		}
		tarifa_express_ju = Integer.parseInt(req
				.getParameter("tarifa_express_ju"));
		
		if ( req.getParameter("tarifa_express_vi") == null){
			throw new ParametroObligatorioException(
					"Precio del Viernes es null");
		}
		tarifa_express_vi = Integer.parseInt(req
				.getParameter("tarifa_express_vi"));
		
		if ( req.getParameter("tarifa_express_sa") == null){
			throw new ParametroObligatorioException("Precio del Sabado es null");			
		}
		tarifa_express_sa = Integer.parseInt(req
				.getParameter("tarifa_express_sa"));
		
		if ( req.getParameter("tarifa_express_do") == null){
			throw new ParametroObligatorioException(
					"Precio del Domingo es null");
		}
		tarifa_express_do = Integer.parseInt(req
				.getParameter("tarifa_express_do"));
		
		
		//****************************************************************************
		//**********************   T A R I F A   U M B R A L  	 *********************
		//****************************************************************************
		if ( req.getParameter("tarifa_umbral_lu") == null){
			throw new ParametroObligatorioException("Precio del Lunes es null");			
		}
		tarifa_umbral_lu = Integer.parseInt(req.getParameter("tarifa_umbral_lu"));
		
		if ( req.getParameter("tarifa_umbral_ma") == null){
			throw new ParametroObligatorioException("Precio del Martes es null");			
		}
		tarifa_umbral_ma = Integer.parseInt(req.getParameter("tarifa_umbral_ma"));
		
		if ( req.getParameter("tarifa_umbral_mi") == null){
			throw new ParametroObligatorioException("Precio del Miercoles es null");			
		}
		tarifa_umbral_mi = Integer.parseInt(req.getParameter("tarifa_umbral_mi"));
		
		if ( req.getParameter("tarifa_umbral_ju") == null){
			throw new ParametroObligatorioException("Precio del Jueves es null");			
		}
		tarifa_umbral_ju = Integer.parseInt(req.getParameter("tarifa_umbral_ju"));
		
		if ( req.getParameter("tarifa_umbral_vi") == null){
			throw new ParametroObligatorioException("Precio del Viernes es null");			
		}
		tarifa_umbral_vi = Integer.parseInt(req.getParameter("tarifa_umbral_vi"));
		
		if ( req.getParameter("tarifa_umbral_sa") == null){
			throw new ParametroObligatorioException("Precio del Sabado es null");			
		}
		tarifa_umbral_sa = Integer.parseInt(req.getParameter("tarifa_umbral_sa"));
		
		if ( req.getParameter("tarifa_umbral_do") == null){
			throw new ParametroObligatorioException("Precio del Domingo es null");			
		}
		tarifa_umbral_do = Integer.parseInt(req.getParameter("tarifa_umbral_do"));
		
		//___________________________________________________________________________
				
		// si falla el parseLong debiese levantar una excepci�n
		/*
		 * long id_semana = Long.parseLong(paramId_semana); long id_zona =
		 * Long.parseLong(paramId_zona); logger.debug("id_zona: "+id_zona);
		 */
		long id_hora_desp = Long.parseLong(paramId_hora_desp);
		
		logger.info("id_hor_desp:" + paramId_hora_desp + ", id_semana:"
				+ paramId_semana + ", id_zona:" + id_zona + ", hora inicio:"
				+ paramH_inicio + ", hora fin:" + paramH_fin);
		logger.info("Capacidad LU:" + paramCapac_lu + ", MA:" + paramCapac_ma
				+ ", MI:" + paramCapac_mi + ", JU:" + paramCapac_ju + ", VI:"
				+ paramCapac_vi + ", SA:" + paramCapac_sa + ", DO:"
				+ paramCapac_do);
		logger.info("Jornada LU:" + jor_lu + ", MA:" + jor_ma + ", MI:"
				+ jor_mi + ", JU:" + jor_ju + ", VI:" + jor_vi + ", SA:"
				+ jor_sa + ", DO:" + jor_do);
		logger.info("Precio LU:" + tarifa_normal_lu + ", MA:"
				+ tarifa_normal_ma + ", MI:" + tarifa_normal_mi + ", JU:"
				+ tarifa_normal_ju + ", VI:" + tarifa_normal_vi + ", SA:"
				+ tarifa_normal_sa + ", DO:" + tarifa_normal_do);
		logger.info("Precio Econ�mico LU:" + tarifa_economica_lu + ", MA:"
				+ tarifa_economica_ma + ", MI:" + tarifa_economica_mi + ", JU:"
				+ tarifa_economica_ju + ", VI:" + tarifa_economica_vi + ", SA:"
				+ tarifa_economica_sa + ", DO:" + tarifa_economica_do);
		logger.info("Precio Express LU:" + tarifa_express_lu + ", MA:"
				+ tarifa_express_ma + ", MI:" + tarifa_express_mi + ", JU:"
				+ tarifa_express_ju + ", VI:" + tarifa_express_vi + ", SA:"
				+ tarifa_express_sa + ", DO:" + tarifa_express_do);
        
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		
		AdmHorarioDespachoDTO hdespacho = new AdmHorarioDespachoDTO();
		hdespacho.setCapac_lu(capac_lu);
		hdespacho.setCapac_ma(capac_ma);
		hdespacho.setCapac_mi(capac_mi);
		hdespacho.setCapac_ju(capac_ju);
		hdespacho.setCapac_vi(capac_vi);
		hdespacho.setCapac_sa(capac_sa);
		hdespacho.setCapac_do(capac_do);
		hdespacho.setH_ini(java.sql.Time.valueOf(paramH_inicio));
		hdespacho.setH_fin(java.sql.Time.valueOf(paramH_fin));
		hdespacho.setId_jpicking_lu(jor_lu);
		hdespacho.setId_jpicking_ma(jor_ma);
		hdespacho.setId_jpicking_mi(jor_mi);
		hdespacho.setId_jpicking_ju(jor_ju);
		hdespacho.setId_jpicking_vi(jor_vi);
		hdespacho.setId_jpicking_sa(jor_sa);
		hdespacho.setId_jpicking_do(jor_do);
		
		hdespacho.setTarifa_normal_lu(tarifa_normal_lu);
		hdespacho.setTarifa_normal_ma(tarifa_normal_ma);
		hdespacho.setTarifa_normal_mi(tarifa_normal_mi);
		hdespacho.setTarifa_normal_ju(tarifa_normal_ju);
		hdespacho.setTarifa_normal_vi(tarifa_normal_vi);
		hdespacho.setTarifa_normal_sa(tarifa_normal_sa);
		hdespacho.setTarifa_normal_do(tarifa_normal_do);
		hdespacho.setTarifa_economica_lu(tarifa_economica_lu);
		hdespacho.setTarifa_economica_ma(tarifa_economica_ma);
		hdespacho.setTarifa_economica_mi(tarifa_economica_mi);
		hdespacho.setTarifa_economica_ju(tarifa_economica_ju);
		hdespacho.setTarifa_economica_vi(tarifa_economica_vi);
		hdespacho.setTarifa_economica_sa(tarifa_economica_sa);
		hdespacho.setTarifa_economica_do(tarifa_economica_do);
		hdespacho.setTarifa_express_lu(tarifa_express_lu);
		hdespacho.setTarifa_express_ma(tarifa_express_ma);
		hdespacho.setTarifa_express_mi(tarifa_express_mi);
		hdespacho.setTarifa_express_ju(tarifa_express_ju);
		hdespacho.setTarifa_express_vi(tarifa_express_vi);
		hdespacho.setTarifa_express_sa(tarifa_express_sa);
		hdespacho.setTarifa_express_do(tarifa_express_do);
		
		hdespacho.setTarifa_umbral_lu(tarifa_umbral_lu);
		hdespacho.setTarifa_umbral_ma(tarifa_umbral_ma);
		hdespacho.setTarifa_umbral_mi(tarifa_umbral_mi);
		hdespacho.setTarifa_umbral_ju(tarifa_umbral_ju);
		hdespacho.setTarifa_umbral_vi(tarifa_umbral_vi);
		hdespacho.setTarifa_umbral_sa(tarifa_umbral_sa);
		hdespacho.setTarifa_umbral_do(tarifa_umbral_do);
		
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try {
			String mensajeLog = "Se intento bajar la capacidad maxima a un valor menor que la capacidad ocupada para el dia ";
			String mensajeLogExito = "Se modifico la capacidad maxima para el dia ";

			ArrayList jornadasValidar = (ArrayList) biz.getJornadasDespachoByIdHorario(id_hora_desp);

			for (int i = 0; i < jornadasValidar.size(); i++) {
				JornadaDespachoEntity jor1 = (JornadaDespachoEntity) jornadasValidar.get(i);
				switch (jor1.getDay_of_week()) {
				case 1:
					if (capac_lu != jor1.getCapac_despacho()) {
						if (capac_lu < jor1.getCapac_despacho_ocupada()) {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(),usr.getLogin(), mensajeLog + "Lunes");
							throw new BolException(Constantes._EX_JOR_CAPAC_LU);
						} else {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(),usr.getLogin(), mensajeLogExito + "Lunes");
						}
					}
					break;
				case 2:
					if (capac_ma != jor1.getCapac_despacho()) {
						if (capac_ma < jor1.getCapac_despacho_ocupada()) {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLog + "Martes");
							throw new BolException(Constantes._EX_JOR_CAPAC_MA);
						} else {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLogExito + "Martes");
						}
					}
					break;
				case 3:
					if (capac_mi != jor1.getCapac_despacho()) {
						if (capac_mi < jor1.getCapac_despacho_ocupada()) {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLog + "Miercoles");
							throw new BolException(Constantes._EX_JOR_CAPAC_MI);
						} else {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLogExito + "Miercoles");
						}
					}
					break;
				case 4:
					if (capac_ju != jor1.getCapac_despacho()) {
						if (capac_ju < jor1.getCapac_despacho_ocupada()) {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLog + "Jueves");
							throw new BolException(Constantes._EX_JOR_CAPAC_JU);
						} else {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLogExito + "Jueves");
						}
					}
					break;
				case 5:
					if (capac_vi != jor1.getCapac_despacho()) {
						if (capac_vi < jor1.getCapac_despacho_ocupada()) {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLog + "Viernes");
							throw new BolException(Constantes._EX_JOR_CAPAC_VI);
						} else {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLogExito + "Viernes");
						}
					}
					break;
				case 6:
					if (capac_sa != jor1.getCapac_despacho()) {
						if (capac_sa < jor1.getCapac_despacho_ocupada()) {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLog + "Sabado");
							throw new BolException(Constantes._EX_JOR_CAPAC_SA);
						} else {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLogExito + "Sabado");
						}
					}
					break;
				case 7:
					if (capac_do != jor1.getCapac_despacho()) {
						if (capac_do < jor1.getCapac_despacho_ocupada()) {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLog + "Domingo");
							throw new BolException(Constantes._EX_JOR_CAPAC_DO);
						} else {
							biz.addLogJornadaDesp(jor1.getId_jdespacho(), usr.getLogin(), mensajeLogExito + "Domingo");
						}
					}
					break;
				}
			}

			biz.ModJornadaDespacho(id_hora_desp, hdespacho, Integer.parseInt(id_zona));
			logger.debug("Se modific� correctamente la jornada de despacho"); 
		} catch(BolException ex){
			logger.error("Controlando excepci�n: " + ex.getMessage());
			if ( ex.getMessage().equals(Constantes._EX_ID_H_DESP_NO_EXISTE) ){
				logger.error("El horario no existe en el sistema");
				fp.add( "rc" , Constantes._EX_ID_H_DESP_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			}	
			if (ex.getMessage().equals(Constantes._EX_JDESP_FALTAN_DATOS)) { // Controlamos
																				// error
																				// faltan
																				// parametros
				logger.error("faltan datos");
				fp.add( "rc" , Constantes._EX_JDESP_FALTAN_DATOS );
				paramUrl = UrlError + fp.forward();
			}
			if (ex.getMessage().equals(Constantes._EX_ZONA_ID_INVALIDA)) { // Controlamos
																			// error
																			// faltan
																			// parametros
				logger.error("el id de la zona es inv�lido");
				fp.add( "rc" , Constantes._EX_ZONA_ID_INVALIDA );
				paramUrl = UrlError + fp.forward();
			}
			if (ex.getMessage().equals(Constantes._EX_JOR_CAPAC_LU)) {
				logger.error("capacidad de lunes es invalido para la jornada de despacho");
				fp.add("rc", Constantes._EX_JOR_CAPAC_LU);
				fp.add("mns_rc",
						"La capacidad de lunes es invalido para la jornada de despacho");
				paramUrl = UrlError + fp.forward();
			}
			if (ex.getMessage().equals(Constantes._EX_JOR_CAPAC_MA)) {
				logger.error("capacidad de martes es invalido para la jornada de despacho");
				fp.add("rc", Constantes._EX_JOR_CAPAC_MA);
				fp.add("mns_rc",
						"La capacidad de martes es invalido para la jornada de despacho");
				paramUrl = UrlError + fp.forward();
			}
			if (ex.getMessage().equals(Constantes._EX_JOR_CAPAC_MI)) {
				logger.error("capacidad de miercoles es invalido para la jornada de despacho");
				fp.add("rc", Constantes._EX_JOR_CAPAC_MI);
				fp.add("mns_rc",
						"La capacidad de miercoles es invalido para la jornada de despacho");
				paramUrl = UrlError + fp.forward();
			}
			if (ex.getMessage().equals(Constantes._EX_JOR_CAPAC_JU)) {
				logger.error("capacidad del jueves es invalido para la jornada de despacho");
				fp.add("rc", Constantes._EX_JOR_CAPAC_JU);
				fp.add("mns_rc",
						"La capacidad del jueves es invalido para la jornada de despacho");
				paramUrl = UrlError + fp.forward();
			}
			if (ex.getMessage().equals(Constantes._EX_JOR_CAPAC_VI)) {
				logger.error("capacidad del viernes es invalido para la jornada de despacho");
				fp.add("rc", Constantes._EX_JOR_CAPAC_VI);
				fp.add("mns_rc",
						"La capacidad del viernes es invalido para la jornada de despacho");
				paramUrl = UrlError + fp.forward();
			}
			if (ex.getMessage().equals(Constantes._EX_JOR_CAPAC_SA)) {
				logger.error("capacidad del sabado es invalido para la jornada de despacho");
				fp.add("rc", Constantes._EX_JOR_CAPAC_SA);
				fp.add("mns_rc",
						"La capacidad del sabado es invalido para la jornada de despacho");
				paramUrl = UrlError + fp.forward();
			}
			if (ex.getMessage().equals(Constantes._EX_JOR_CAPAC_DO)) {
				logger.error("capacidad de domingo es invalido para la jornada de despacho");
				fp.add("rc", Constantes._EX_JOR_CAPAC_DO);
				fp.add("mns_rc",
						"La capacidad de domingo es invalido para la jornada de despacho");
				paramUrl = UrlError + fp.forward();
			}
		}
		res.sendRedirect(paramUrl);	
	}
}
