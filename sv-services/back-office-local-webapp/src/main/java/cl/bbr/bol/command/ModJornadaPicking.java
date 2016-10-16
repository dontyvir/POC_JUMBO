package cl.bbr.bol.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.JornadaPickingEntity;
import cl.bbr.jumbocl.pedidos.dto.AdmHorarioPickingDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Modifica horario de picking y jornadas relacionadas
 * @author jsepulveda
 */
public class ModJornadaPicking extends Command {
    
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.info("--------------------------------------------------------------------------------------------");
        logger.info("Modificar la jornada de PICKING: " + usr.getLogin() + " (" + usr.getNombre() + " " + usr.getApe_paterno() + ")");

		String paramUrl	= "";
		String paramId_hora = "";
		String paramH_inicio 	= "";
		String paramH_fin		= "";
		String paramCapac_lu	= "";
		String paramCapac_ma	= "";
		String paramCapac_mi	= "";
		String paramCapac_ju	= "";
		String paramCapac_vi	= "";
		String paramCapac_sa	= "";
		String paramCapac_do	= "";
		String paramHras_val_lu	= "";
		String paramHras_val_ma	= "";
		String paramHras_val_mi	= "";
		String paramHras_val_ju	= "";
		String paramHras_val_vi	= "";
		String paramHras_val_sa	= "";
		String paramHras_val_do	= "";
		String paramHras_web_lu	= "";
		String paramHras_web_ma	= "";
		String paramHras_web_mi	= "";
		String paramHras_web_ju	= "";
		String paramHras_web_vi	= "";
		String paramHras_web_sa	= "";
		String paramHras_web_do	= "";
		
		int capac_lu			=-1;
		int capac_ma			=-1;
		int capac_mi			=-1;
		int capac_ju			=-1;
		int capac_vi			=-1;
		int capac_sa			=-1;
		int capac_do			=-1;
		int hras_val_lu			=-1;
		int hras_val_ma			=-1;
		int hras_val_mi			=-1;
		int hras_val_ju			=-1;
		int hras_val_vi			=-1;
		int hras_val_sa			=-1;
		int hras_val_do			=-1;
		int hras_web_lu			=-1;
		int hras_web_ma			=-1;
		int hras_web_mi			=-1;
		int hras_web_ju			=-1;
		int hras_web_vi			=-1;
		int hras_web_sa			=-1;
		int hras_web_do			=-1;

		String UrlError = getServletConfig().getInitParameter("UrlError");
		
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		paramUrl = req.getParameter("url");
		
		if ( req.getParameter("id_hor_pick") == null){
			throw new ParametroObligatorioException("id_hor_pick es null");			
		}
		paramId_hora = req.getParameter("id_hor_pick");
		
		if ( req.getParameter("h_ini") == null){
			throw new ParametroObligatorioException("Hora de inicio es null");			
		}
		paramH_inicio = req.getParameter("h_ini");		
		
		if ( req.getParameter("h_fin") == null){
			throw new ParametroObligatorioException("Hora de fin es null");			
		}
		paramH_fin = req.getParameter("h_fin");
        
		//revision de capacidades por cada dia
		if ( req.getParameter("capac_lu") == null){
			throw new ParametroObligatorioException("Capacidad del Lunes es null");			
		}
		paramCapac_lu = req.getParameter("capac_lu");
		if(paramCapac_lu.indexOf(",")<0){	
			capac_lu = Integer.parseInt(paramCapac_lu);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
		
		if ( req.getParameter("capac_ma") == null){
			throw new ParametroObligatorioException("Capacidad del Martes es null");			
		}
		
        paramCapac_ma = req.getParameter("capac_ma");
		if(paramCapac_ma.indexOf(",")<0){	
			capac_ma = Integer.parseInt(paramCapac_ma);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
		
		if ( req.getParameter("capac_mi") == null){
			throw new ParametroObligatorioException("Capacidad del Miercoles es null");			
		}
		paramCapac_mi = req.getParameter("capac_mi");
		if(paramCapac_mi.indexOf(",")<0){
			capac_mi = Integer.parseInt(paramCapac_mi);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
        
		if ( req.getParameter("capac_ju") == null){
			throw new ParametroObligatorioException("Capacidad del Jueves es null");			
		}
		paramCapac_ju = req.getParameter("capac_ju");
		if(paramCapac_ju.indexOf(",")<0){
			capac_ju = Integer.parseInt(paramCapac_ju);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
        
		if ( req.getParameter("capac_vi") == null){
			throw new ParametroObligatorioException("Capacidad del Viernes es null");			
		}
		paramCapac_vi = req.getParameter("capac_vi");
		if(paramCapac_vi.indexOf(",")<0){
			capac_vi = Integer.parseInt(paramCapac_vi);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
        
		if ( req.getParameter("capac_sa") == null){
			throw new ParametroObligatorioException("Capacidad del Sabado es null");			
		}
		paramCapac_sa = req.getParameter("capac_sa");
		if(paramCapac_sa.indexOf(",")<0){
			capac_sa = Integer.parseInt(paramCapac_sa);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
        
		if ( req.getParameter("capac_do") == null){
			throw new ParametroObligatorioException("Capacidad del Domingo es null");			
		}
		paramCapac_do = req.getParameter("capac_do");
		if(paramCapac_sa.indexOf(",")<0){
			capac_do = Integer.parseInt(paramCapac_do);
		}else{
			throw new SystemException("las capacidades deben ser enteros");
		}
        
        // revision de horas validación por dia
		if ( req.getParameter("hras_val_lu") == null){
			throw new ParametroObligatorioException("Hras. val. del Lunes es null");			
		}
		paramHras_val_lu = req.getParameter("hras_val_lu");
		if(paramHras_val_lu.indexOf(",")<0){	
			hras_val_lu = Integer.parseInt(paramHras_val_lu);
		}else{
			throw new SystemException("las hras. val. deben ser enteros");
		}
        
		if ( req.getParameter("hras_val_ma") == null){
			throw new ParametroObligatorioException("Hras. val. del Martes es null");			
		}
		paramHras_val_ma = req.getParameter("hras_val_ma");
		if(paramHras_val_ma.indexOf(",")<0){	
			hras_val_ma = Integer.parseInt(paramHras_val_ma);
		}else{
			throw new SystemException("las hras. val. deben ser enteros");
		}
        
		if ( req.getParameter("hras_val_mi") == null){
			throw new ParametroObligatorioException("Hras. val. del Miercoles es null");			
		}
		paramHras_val_mi = req.getParameter("hras_val_mi");
		if(paramHras_val_mi.indexOf(",")<0){	
			hras_val_mi = Integer.parseInt(paramHras_val_mi);
		}else{
			throw new SystemException("las hras. val. deben ser enteros");
		}
        
		if ( req.getParameter("hras_val_ju") == null){
			throw new ParametroObligatorioException("Hras. val. del Jueves es null");			
		}
		paramHras_val_ju = req.getParameter("hras_val_ju");
		if(paramHras_val_ju.indexOf(",")<0){	
			hras_val_ju = Integer.parseInt(paramHras_val_ju);
		}else{
			throw new SystemException("las hras. val. deben ser enteros");
		}
        
		if ( req.getParameter("hras_val_vi") == null){
			throw new ParametroObligatorioException("Hras. val. del Viernes es null");			
		}
		paramHras_val_vi = req.getParameter("hras_val_vi");
		if(paramHras_val_vi.indexOf(",")<0){	
			hras_val_vi = Integer.parseInt(paramHras_val_vi);
		}else{
			throw new SystemException("las hras. val. deben ser enteros");
		}
        
		if ( req.getParameter("hras_val_sa") == null){
			throw new ParametroObligatorioException("Hras. val. del Sabado es null");			
		}
		paramHras_val_sa = req.getParameter("hras_val_sa");
		if(paramHras_val_sa.indexOf(",")<0){	
			hras_val_sa = Integer.parseInt(paramHras_val_sa);
		}else{
			throw new SystemException("las hras. val. deben ser enteros");
		}
        
		if ( req.getParameter("hras_val_do") == null){
			throw new ParametroObligatorioException("Hras. val. del Domingo es null");			
		}
		paramHras_val_do = req.getParameter("hras_val_do");
		if(paramHras_val_do.indexOf(",")<0){	
			hras_val_do = Integer.parseInt(paramHras_val_do);
		}else{
			throw new SystemException("las hras. val. deben ser enteros");
		}
        
		// revision de horas web por dia
		if ( req.getParameter("hras_web_lu") == null){
			throw new ParametroObligatorioException("Hras. web. del Lunes es null");			
		}
		paramHras_web_lu = req.getParameter("hras_web_lu");
		if(paramHras_web_lu.indexOf(",")<0){	
			hras_web_lu = Integer.parseInt(paramHras_web_lu);
		}else{
			throw new SystemException("las hras. web. deben ser enteros");
		}
        
		if ( req.getParameter("hras_web_ma") == null){
			throw new ParametroObligatorioException("Hras. web. del Martes es null");			
		}
		paramHras_web_ma = req.getParameter("hras_web_ma");
		if(paramHras_web_ma.indexOf(",")<0){	
			hras_web_ma = Integer.parseInt(paramHras_web_ma);
		}else{
			throw new SystemException("las hras. web. deben ser enteros");
		}
        
		if ( req.getParameter("hras_web_mi") == null){
			throw new ParametroObligatorioException("Hras. web. del Miercoles es null");			
		}
		paramHras_web_mi = req.getParameter("hras_web_mi");
		if(paramHras_web_mi.indexOf(",")<0){	
			hras_web_mi = Integer.parseInt(paramHras_web_mi);
		}else{
			throw new SystemException("las hras. web. deben ser enteros");
		}
        
		if ( req.getParameter("hras_web_ju") == null){
			throw new ParametroObligatorioException("Hras. web. del Jueves es null");			
		}
		paramHras_web_ju = req.getParameter("hras_web_ju");
		if(paramHras_web_ju.indexOf(",")<0){	
			hras_web_ju = Integer.parseInt(paramHras_web_ju);
		}else{
			throw new SystemException("las hras. web. deben ser enteros");
		}
        
		if ( req.getParameter("hras_web_vi") == null){
			throw new ParametroObligatorioException("Hras. web. del Viernes es null");			
		}
		paramHras_web_vi = req.getParameter("hras_web_vi");
		if(paramHras_web_vi.indexOf(",")<0){	
			hras_web_vi = Integer.parseInt(paramHras_web_vi);
		}else{
			throw new SystemException("las hras. web. deben ser enteros");
		}
        
		if ( req.getParameter("hras_web_sa") == null){
			throw new ParametroObligatorioException("Hras. web. del Sabado es null");			
		}
		paramHras_web_sa = req.getParameter("hras_web_sa");
		if(paramHras_web_sa.indexOf(",")<0){	
			hras_web_sa = Integer.parseInt(paramHras_web_sa);
		}else{
			throw new SystemException("las hras. web. deben ser enteros");
		}
        
		if ( req.getParameter("hras_web_do") == null){
			throw new ParametroObligatorioException("Hras. web. del Domingo es null");			
		}
		paramHras_web_do = req.getParameter("hras_web_do");
		if(paramHras_web_do.indexOf(",")<0){	
			hras_web_do = Integer.parseInt(paramHras_web_do);
		}else{
			throw new SystemException("las hras. web. deben ser enteros");
		}
        
		// si falla el parseLong debiese levantar una excepción
		long id_hora_pick = Long.parseLong(paramId_hora);
        
        logger.info("id_hor_pick:" + id_hora_pick + ", hora de inicio:" + paramH_inicio + ", hora de fin:" + paramH_fin);
        logger.info("Capacidad LU:" + paramCapac_lu + ", MA:" + paramCapac_ma + "MI:" + paramCapac_mi + ", JU:" + paramCapac_ju + ", VI:" + paramCapac_vi + ", SA:" + paramCapac_sa + ", DO:" + paramCapac_do);
        logger.info("Hras. validacion LU:" + paramHras_val_lu + ", MA:" + paramHras_val_ma + ", MI:" + paramHras_val_mi + ", JU:" + paramHras_val_ju + ", VI:" + paramHras_val_vi + ", SA:" + paramHras_val_sa + ", DO:" + paramHras_val_do);
        logger.info("Hras. web LU:" + paramHras_web_lu + ", MA:" + paramHras_web_ma + ", MI:" + paramHras_web_mi + ", JU:" + paramHras_web_ju + ", VI:" + paramHras_web_vi + ", SA:" + paramHras_web_sa + ", DO:" + paramHras_web_do);
        
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		
		AdmHorarioPickingDTO hpicking = new AdmHorarioPickingDTO();
		hpicking.setCapac_lu(capac_lu);
		hpicking.setCapac_ma(capac_ma);
		hpicking.setCapac_mi(capac_mi);
		hpicking.setCapac_ju(capac_ju);
		hpicking.setCapac_vi(capac_vi);
		hpicking.setCapac_sa(capac_sa);
		hpicking.setCapac_do(capac_do);
		hpicking.setHras_val_lu(hras_val_lu);
		hpicking.setHras_val_ma(hras_val_ma);
		hpicking.setHras_val_mi(hras_val_mi);
		hpicking.setHras_val_ju(hras_val_ju);
		hpicking.setHras_val_vi(hras_val_vi);
		hpicking.setHras_val_sa(hras_val_sa);
		hpicking.setHras_val_do(hras_val_do);
		hpicking.setHras_web_lu(hras_web_lu);
		hpicking.setHras_web_ma(hras_web_ma);
		hpicking.setHras_web_mi(hras_web_mi);
		hpicking.setHras_web_ju(hras_web_ju);
		hpicking.setHras_web_vi(hras_web_vi);
		hpicking.setHras_web_sa(hras_web_sa);
		hpicking.setHras_web_do(hras_web_do);
		hpicking.setH_ini(java.sql.Time.valueOf(paramH_inicio));
		hpicking.setH_fin(java.sql.Time.valueOf(paramH_fin));
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		
		try {		
		
			String mensajeLog = "Se intento bajar la capacidad maxima a un valor menor que la capacidad ocupada para el dia ";
			String mensajeLogExito = "Se modifico la capacidad maxima para el dia ";

			ArrayList jornadasValidar = (ArrayList) biz
					.getJornadasByIdHorario(id_hora_pick);
			for (int i = 0; i < jornadasValidar.size(); i++) {
				JornadaPickingEntity jor1 = (JornadaPickingEntity) jornadasValidar
						.get(i);
				switch (jor1.getDay_of_week()) {
				case 1:					
					if (capac_lu != jor1.getCapac_picking()) {
						if (capac_lu < jor1.getCapac_ocupada()) {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLog + "Lunes");
							throw new BolException(Constantes._EX_JOR_CAPAC_LU);
						} else {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLogExito + "Lunes");
						}
					}
					break;
				case 2:
					if (capac_ma != jor1.getCapac_picking()) {
						if (capac_ma < jor1.getCapac_ocupada()) {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLog + "Martes");
							throw new BolException(Constantes._EX_JOR_CAPAC_MA);
						} else {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLogExito + "Martes");
						}
					}
					break;
				case 3:
					if (capac_mi != jor1.getCapac_picking()) {
						if (capac_mi < jor1.getCapac_ocupada()) {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLog + "Miercoles");
							throw new BolException(Constantes._EX_JOR_CAPAC_MI);
						} else {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLogExito + "Miercoles");
						}
					}
					break;
				case 4:
					if (capac_ju != jor1.getCapac_picking()) {
						if (capac_ju < jor1.getCapac_ocupada()) {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLog + "Jueves");
							throw new BolException(Constantes._EX_JOR_CAPAC_JU);
						} else {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLogExito + "Jueves");
						}
					}
					break;
				case 5:
					if (capac_vi != jor1.getCapac_picking()) {
						if (capac_vi < jor1.getCapac_ocupada()) {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLog + "Viernes");
							throw new BolException(Constantes._EX_JOR_CAPAC_VI);
						} else {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLogExito + "Viernes");
						}
					}
					break;
				case 6:
					if (capac_sa != jor1.getCapac_picking()) {
						if (capac_sa < jor1.getCapac_ocupada()) {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLog + "Sabado");
							throw new BolException(Constantes._EX_JOR_CAPAC_SA);
						} else {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLogExito + "Sabado");
						}
					}
					break;
				case 7:
					if (capac_do != jor1.getCapac_picking()) {
						if (capac_do < jor1.getCapac_ocupada()) {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLog + "Domingo");
							throw new BolException(Constantes._EX_JOR_CAPAC_DO);
						} else {
							biz.addLogJornadaPick(jor1.getId_jpicking(),
									usr.getLogin(), mensajeLogExito + "Domingo");
						}
					}
					break;
				}
			}

			biz.ModJornadaPicking(id_hora_pick,hpicking);
            logger.info("Se modificó correctamente la jornada de picking"); 
		}catch(BolException ex){
			logger.error("No se pueden modificar jornadas de picking ");
			logger.error("ex.getMessage():"+ex.getMessage());
			fp.add( "id_hor_pick" , paramId_hora);
			
			//fp.add( "fecha" , );
			if ( ex.getMessage().equals(Constantes._EX_ID_H_PICK_NO_EXISTE) ){
				logger.error("la jornada no existe en el sistema");
				fp.add( "rc" , Constantes._EX_ID_H_PICK_NO_EXISTE);
				fp.add( "mns_rc" , "La jornada no existe en el sistema");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JPICK_FALTAN_DATOS) ){
				logger.error("faltan datos para modificar una nueva Jornada de picking");
				fp.add( "rc" , Constantes._EX_JPICK_FALTAN_DATOS );
				fp.add( "mns_rc" , "Faltan datos para modificar una nueva Jornada de picking");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_LOCAL_ID_INVALIDO) ){
				logger.error("el id de local es invalido para la jornada de picking");
				fp.add( "rc" , Constantes._EX_LOCAL_ID_INVALIDO );
				fp.add( "mns_rc" , "El local es invalido para la jornada de picking");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_SEMANA_ID) ){
				logger.error("el id de la semana es invalido para la jornada de picking");
				fp.add( "rc" , Constantes._EX_SEMANA_ID );
				fp.add( "mns_rc" , "El id de la semana es invalido para la jornada de picking");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_CAPAC_LU) ){
				logger.error("capacidad de lunes es invalido para la jornada de picking");
				fp.add( "rc" , Constantes._EX_JOR_CAPAC_LU );
				fp.add( "mns_rc" , "La capacidad de lunes es invalido para la jornada de picking");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_CAPAC_MA) ){
				logger.error("capacidad de martes es invalido para la jornada de picking");
				fp.add( "rc" , Constantes._EX_JOR_CAPAC_MA );
				fp.add( "mns_rc" , "La capacidad de martes es invalido para la jornada de picking");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_CAPAC_MI) ){
				logger.error("capacidad de miercoles es invalido para la jornada de picking");
				fp.add( "rc" , Constantes._EX_JOR_CAPAC_MI );
				fp.add( "mns_rc" , "La capacidad de miercoles es invalido para la jornada de picking");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_CAPAC_JU) ){
				logger.error("capacidad del jueves es invalido para la jornada de picking");
				fp.add( "rc" , Constantes._EX_JOR_CAPAC_JU );
				fp.add( "mns_rc" , "La capacidad del jueves es invalido para la jornada de picking");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_CAPAC_VI) ){
				logger.error("capacidad del viernes es invalido para la jornada de picking");
				fp.add( "rc" , Constantes._EX_JOR_CAPAC_VI );
				fp.add( "mns_rc" , "La capacidad del viernes es invalido para la jornada de picking");
				paramUrl = UrlError + fp.forward();
			}
			if ( ex.getMessage().equals(Constantes._EX_JOR_CAPAC_SA) ){
				logger.error("capacidad del sabado es invalido para la jornada de picking");
				fp.add( "rc" , Constantes._EX_JOR_CAPAC_SA );
				fp.add( "mns_rc" , "La capacidad del sabado es invalido para la jornada de picking");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_CAPAC_DO) ){
				logger.error("capacidad de domingo es invalido para la jornada de picking");
				fp.add( "rc" , Constantes._EX_JOR_CAPAC_DO );
				fp.add( "mns_rc" , "La capacidad de domingo es invalido para la jornada de picking");
				paramUrl = UrlError + fp.forward();
			}
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_VAL_LU) ){
				logger.error("horas mínimas de validación del lunes es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_VAL_LU );
				fp.add( "mns_rc" , "Las horas mínimas de validación del lunes es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_VAL_MA) ){
				logger.error("horas mínimas de validación del martes es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_VAL_MA );
				fp.add( "mns_rc" , "Las horas mínimas de validación del martes es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_VAL_MI) ){
				logger.error("horas mínimas de validación del miercoles es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_VAL_MI );
				fp.add( "mns_rc" , "Las horas mínimas de validación del miercoles es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_VAL_JU) ){
				logger.error("horas mínimas de validación del jueves es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_VAL_JU );
				fp.add( "mns_rc" , "Las horas mínimas de validación del jueves es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_VAL_VI) ){
				logger.error("horas mínimas de validación del viernes es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_VAL_VI );
				fp.add( "mns_rc" , "Las horas mínimas de validación del viernes es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_VAL_SA) ){
				logger.error("horas mínimas de validación del sabado es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_VAL_SA );
				fp.add( "mns_rc" , "Las horas mínimas de validación del sabado es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_VAL_DO) ){
				logger.error("horas mínimas de validación del domingo es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_VAL_DO );
				fp.add( "mns_rc" , "Las horas mínimas de validación del domingo es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_WEB_LU) ){
				logger.error("horas mínimas web del lunes es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_WEB_LU );
				fp.add( "mns_rc" , "Las horas mínimas web del lunes es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_WEB_MA) ){
				logger.error("horas mínimas web del martes es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_WEB_MA );
				fp.add( "mns_rc" , "Las horas mínimas web del martes es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_WEB_MI) ){
				logger.error("horas mínimas web del miercoles es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_WEB_MI );
				fp.add( "mns_rc" , "Las horas mínimas web del miercoles es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_WEB_JU) ){
				logger.error("horas mínimas web del jueves es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_WEB_JU );
				fp.add( "mns_rc" , "Las horas mínimas web del jueves es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_WEB_VI) ){
				logger.error("horas mínimas web del viernes es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_WEB_VI );
				fp.add( "mns_rc" , "Las horas mínimas web del viernes es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_WEB_SA) ){
				logger.error("horas mínimas web del sabado es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_WEB_SA );
				fp.add( "mns_rc" , "Las horas mínimas web del sabado es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
			if ( ex.getMessage().equals(Constantes._EX_JOR_HRAS_WEB_DO) ){
				logger.error("horas mínimas web del domingo es menor a cero");
				fp.add( "rc" , Constantes._EX_JOR_HRAS_WEB_DO );
				fp.add( "mns_rc" , "Las horas mínimas web del domingo es menor a cero");
				paramUrl = UrlError + fp.forward();
			}	
	
		}
		
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
