package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que modifica la zona de despacho
 * @author mleiva
 */


public class ModZonaDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.info("--------------------------------------------------------------------------------------------");
        logger.info("Modificar la zona de DESPACHO: " + usr.getLogin() + " (" + usr.getNombre() + " " + usr.getApe_paterno() + ")");

		String Url              = "";
		String Nombre           = "";
		String Descripcion      = "";
		int tarifa_normal_alta  = -1;
		int tarifa_normal_media = -1;
		int tarifa_normal_baja  = -1;
		int tarifa_economica    = -1;
		int tarifa_express      = -1;
		int estado_tarifa_economica = 0;
		int estado_tarifa_express   = 0;
		int regalo_clientes 	 = 0;
		String msg_cal_despacho  = "";
		int orden                = -1;
		long id_zona             = -1;
		int estado_descuento_cat = 0;
		int estado_descuento_tbk = 0;
		int estado_descuento_par = 0;
		int monto_descuento_cat = -1;
		int monto_descuento_tbk = -1;
		int monto_descuento_par = -1;
		int monto_descuento_pc_cat = -1;
		int monto_descuento_pc_tbk = -1;
		int monto_descuento_pc_par = -1;
		
		String UrlError = getServletConfig().getInitParameter("UrlError");
		
        if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		Url = req.getParameter("url");
		
		if ( req.getParameter("nombre") == null){
			throw new ParametroObligatorioException("nombre es null");			
		}
		Nombre = req.getParameter("nombre");
		
		if ( req.getParameter("desc") == null){
			throw new ParametroObligatorioException("descripcion es null");
		}
		Descripcion = req.getParameter("desc");
		
		if ( req.getParameter("tarifa_normal_alta") == null){
			throw new ParametroObligatorioException("tarifa_normal_alta es null");
		}
		tarifa_normal_alta = Integer.parseInt(req.getParameter("tarifa_normal_alta"));
		
        if ( req.getParameter("tarifa_normal_media") == null){
			throw new ParametroObligatorioException("tarifa_normal_media es null");
		}
		tarifa_normal_media = Integer.parseInt(req.getParameter("tarifa_normal_media"));
		
		if ( req.getParameter("tarifa_normal_baja") == null){
			throw new ParametroObligatorioException("tarifa_normal_baja es null");
		}
		tarifa_normal_baja = Integer.parseInt(req.getParameter("tarifa_normal_baja"));
		
		if ( req.getParameter("tarifa_economica") == null){
			throw new ParametroObligatorioException("tarifa_economica es null");
		}
		tarifa_economica = Integer.parseInt(req.getParameter("tarifa_economica"));
		
		if ( req.getParameter("tarifa_express") == null){
			throw new ParametroObligatorioException("tarifa_express es null");
		}
		tarifa_express = Integer.parseInt(req.getParameter("tarifa_express"));
		
		if ( req.getParameter("estado_tarifa_economica") != null){
			estado_tarifa_economica = Integer.parseInt(req.getParameter("estado_tarifa_economica"));
		}
		
		if ( req.getParameter("estado_tarifa_express") != null){
			estado_tarifa_express = Integer.parseInt(req.getParameter("estado_tarifa_express"));
		}
				
		if ( req.getParameter("msg_cal_despacho") == null){
			throw new ParametroObligatorioException("msg_cal_despacho es null");
		}
		msg_cal_despacho = req.getParameter("msg_cal_despacho");
				
		if ( req.getParameter("id_zona") == null){
			throw new ParametroObligatorioException("id zona es null");
		}
		id_zona = Long.parseLong(req.getParameter("id_zona"));
		
		if ( req.getParameter("orden") == null){
			throw new ParametroObligatorioException("orden es null");
		}
		orden = Integer.parseInt(req.getParameter("orden"));
		
		if ( req.getParameter("regalo_clientes") != null){
		    regalo_clientes = Integer.parseInt(req.getParameter("regalo_clientes"));			
		}
        
        //2.15 Parametro Estado Descuento CAT
		//2.16 Parametro Estado Descuento TBK
		//2.17 Parametro Estado Descuento PAR
		
		estado_descuento_cat = req.getParameter("estado_descuento_pc_cat") == null ? 0 : Integer.parseInt(req.getParameter("estado_descuento_pc_cat")); 
		estado_descuento_tbk = req.getParameter("estado_descuento_pc_tbk") == null ? 0 : Integer.parseInt(req.getParameter("estado_descuento_pc_tbk"));
		estado_descuento_par = req.getParameter("estado_descuento_pc_par") == null ? 0 : Integer.parseInt(req.getParameter("estado_descuento_pc_par"));
		
		estado_descuento_cat = req.getParameter("estado_descuento_cat") == null ? estado_descuento_cat : estado_descuento_cat + Integer.parseInt(req.getParameter("estado_descuento_cat")); 
		estado_descuento_tbk = req.getParameter("estado_descuento_tbk") == null ? estado_descuento_tbk : estado_descuento_tbk + Integer.parseInt(req.getParameter("estado_descuento_tbk"));
		estado_descuento_par = req.getParameter("estado_descuento_par") == null ? estado_descuento_par : estado_descuento_par + Integer.parseInt(req.getParameter("estado_descuento_par"));
				
		monto_descuento_cat = req.getParameter("monto_descuento_cat") == null ? 50000 : Integer.parseInt(req.getParameter("monto_descuento_cat")); 
		monto_descuento_pc_cat = req.getParameter("monto_descuento_pc_cat") == null ? 50000 : Integer.parseInt(req.getParameter("monto_descuento_pc_cat"));
		
		monto_descuento_tbk = req.getParameter("monto_descuento_tbk") == null ? 50000 : Integer.parseInt(req.getParameter("monto_descuento_tbk")); 
		monto_descuento_pc_tbk = req.getParameter("monto_descuento_pc_tbk") == null ? 50000 : Integer.parseInt(req.getParameter("monto_descuento_pc_tbk"));
		
		monto_descuento_par = req.getParameter("monto_descuento_par") == null ? 50000 : Integer.parseInt(req.getParameter("monto_descuento_par")); 
		monto_descuento_pc_par = req.getParameter("monto_descuento_pc_par") == null ? 50000 : Integer.parseInt(req.getParameter("monto_descuento_pc_par"));
		
        BizDelegate biz = new BizDelegate();
		
		ZonaDTO zona = new ZonaDTO();
		zona.setDescripcion(Descripcion);
		zona.setId_local(usr.getId_local());
		zona.setNombre(Nombre);
        zona.setTarifa_normal_alta(tarifa_normal_alta);
        zona.setTarifa_normal_media(tarifa_normal_media);
        zona.setTarifa_normal_baja(tarifa_normal_baja);
        zona.setTarifa_economica(tarifa_economica);
        zona.setTarifa_express(tarifa_express);
        zona.setEstado_tarifa_economica(estado_tarifa_economica);
        zona.setEstado_tarifa_express(estado_tarifa_express);
        zona.setMensaje_cal_despacho(msg_cal_despacho);
        zona.setId_zona(id_zona);
        zona.setOrden(orden);
        zona.setRegalo_clientes(regalo_clientes);
        zona.setEstado_descuento_cat(estado_descuento_cat);
        zona.setEstado_descuento_tbk(estado_descuento_tbk);
        zona.setEstado_descuento_par(estado_descuento_par);
        zona.setMonto_descuento_cat(monto_descuento_cat);
        zona.setMonto_descuento_tbk(monto_descuento_tbk);
        zona.setMonto_descuento_par(monto_descuento_par);
        zona.setMonto_descuento_pc_cat(monto_descuento_pc_cat);
        zona.setMonto_descuento_pc_tbk(monto_descuento_pc_tbk);
        zona.setMonto_descuento_pc_par(monto_descuento_pc_par);
        ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
        
        logger.info("id_zona:" + id_zona + ", Nombre:" + Nombre + ", Descripcion:" + Descripcion + ", LOCAL:" + usr.getId_local());
        logger.info("Tarifa Normal AL:" + tarifa_normal_alta + ", ME:" + tarifa_normal_media + ", BA:" + tarifa_normal_baja);
        logger.info("Tarifa Economica:" + tarifa_economica + ", Express:" + tarifa_express);
        logger.info("Estado Tarifa Economica:" + estado_tarifa_economica + ", Express:" + estado_tarifa_express);
        logger.info("Orden:" + orden + ", Mensaje Cal Despacho:" + msg_cal_despacho + ", Regalo Clientes:" + regalo_clientes);
        logger.info("Estado descuento CAT:" + estado_descuento_cat + ", TBK:" + estado_descuento_tbk + ", PAR:" + estado_descuento_par);
        logger.info("Monto descuento CAT:" + monto_descuento_cat + ", TBK:" + monto_descuento_tbk + ", PAR:" + monto_descuento_par);
        logger.info("Monto descuento PC CAT:" + monto_descuento_pc_cat + ", PC TBK:" + monto_descuento_pc_tbk + ", PC PAR:" + monto_descuento_pc_par);
        
		try {
            biz.doModZonaDespacho(zona);
            logger.debug("Se modificó correctamente la jornada de despacho"); 
		} catch(BolException ex) {
			if(ex.getMessage().equals(Constantes.PARAMETRO_OBLIGATORIO)){
				logger.error("Faltan Parametros que son Obligatorios");
				fp.add( "rc" , Constantes.PARAMETRO_OBLIGATORIO );
				Url = UrlError + fp.forward();
			}
			if(ex.getMessage().equals(Constantes._EX_ZONA_ID_INVALIDA)){
				logger.error("El Id de la Zona es Inválido");
				fp.add( "rc" , Constantes._EX_ZONA_ID_INVALIDA );
				Url = UrlError + fp.forward();
			}
			if(ex.getMessage().equals(Constantes._EX_LOCAL_ID_INVALIDO)){
				logger.error("El Id del Local es inválido");
				fp.add( "rc" , Constantes._EX_LOCAL_ID_INVALIDO );
				Url = UrlError + fp.forward();
			}
        }
		res.sendRedirect(Url);
	}
}
