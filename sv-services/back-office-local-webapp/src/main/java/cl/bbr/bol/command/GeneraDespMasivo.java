package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcGeneraDespMasivoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * comando que genera una transacción de pago y agrega un log al pedido
 * @author BBR Ingenieria
 */
public class GeneraDespMasivo extends Command{

	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl	= "";
		String paramIdZona ="";
		String paramIdSemana ="";
		String paramFecIni = "";
		String paramFecFin = "";
		long id_zona = -1;
		long id_semana = -1;
		
		// 1. Parámetros de inicialización servlet
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);		
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");
		logger.debug("mensaje_exito: " + mensaje_exito);
		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
		logger.debug("mensaje_fracaso: " + mensaje_fracaso);
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros GeneraTrxPago...");

		//URL		
		if ( req.getParameter("url") == null ){	throw new ParametroObligatorioException("url es null");}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		if ( req.getParameter("id_zona") == null ){throw new ParametroObligatorioException("id_zona es null");}
		if ( req.getParameter("id_semana") == null ){throw new ParametroObligatorioException("id_semana es null");}
		if ( req.getParameter("txt_fecha") == null ){throw new ParametroObligatorioException("txt_fecha es null");}
		if ( req.getParameter("txt_fechafin") == null ){throw new ParametroObligatorioException("txt_fechafin es null");}
		
		paramIdZona	= req.getParameter("id_zona");
		id_zona = Long.parseLong(paramIdZona);
		paramIdSemana	= req.getParameter("id_semana");
		id_semana = Long.parseLong(paramIdSemana);
		paramFecIni	= req.getParameter("txt_fecha");
		paramFecFin	= req.getParameter("txt_fechafin");
		
		logger.debug("id_zona: " + id_zona);
		logger.debug("id_semana: " + id_semana);
		logger.debug("txt_fecha: " + paramFecIni);
		logger.debug("txt_fechafin: " + paramFecFin);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		
		// Inicia la jornada
		try{
			ProcGeneraDespMasivoDTO dto = new ProcGeneraDespMasivoDTO();
			dto.setId_local(usr.getId_local());
			dto.setId_semana(id_semana);
			dto.setId_zona(id_zona);
			dto.setFecha_ini(paramFecIni);
			dto.setFecha_fin(paramFecFin);
			boolean result = biz.doGeneraDespMasivo(dto);
			logger.debug("result:"+result);
			paramUrl = paramUrl + "&fecha="+paramFecIni + "&mensaje="+mensaje_exito;

		}catch(BolException ex){
			logger.debug("ex:"+ex.getMessage());
			fp.add( "id_zona" , String.valueOf(id_zona));
			if(ex.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE)){
				logger.debug("El id del pedido es Inválido");
				fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE);
				fp.add( "mensaje_rc" , mensaje_fracaso);
				paramUrl = UrlError + fp.forward();
			}else if( !ex.getMessage().equals("")){
				logger.debug("excepcion");
				fp.add( "rc" , "1");
				fp.add( "mensaje" , ex.getMessage());
				paramUrl = UrlError + fp.forward();
			}else{
				throw new SystemException(ex);
			}
		}
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
