package cl.bbr.vte.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.ProcInsCotizacionDTO;

/**
 * 
 * <p>Permite que un comprador acepte la cotizacion enviada por un ejecutivo venta empresas.</p>
 *  
 * <p>Se deben validar que envíen los datos de los medios de pago correspondiente y otros datos.</p> 
 * <p>Debe validar cupo del medio de pago para Línea de Crédito.</p>
 * <p>Debe validar que la empresa no este bloqueda.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AceptaCotizacion extends Command {

	/**
	 * Acepta una cotizacion
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	public void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		// Carga properties
//		ResourceBundle rb = ResourceBundle.getBundle("vte");
		String comentarios = "";
		
		// Obtiene sessión
		HttpSession session = arg0.getSession();

		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		String comentario = arg0.getParameter("ped_obs_tmp");
		logger.debug("UrlOK: " + dis_ok);
		logger.debug("UrlError: " + UrlError);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");

		ForwardParameters fp = new ForwardParameters();
		fp.add( arg0.getParameterMap() );		

		logger.debug( "Parametros cot_id: " + session.getAttribute("ses_cot_id").toString() + " --> " + fp.forward());
		
		try {
			/*
			// Revision de parametros obligatorios
			ArrayList campos = new ArrayList();
			if (arg0.getParameter("valor_medio_pago") != null && Long.parseLong(arg0.getParameter("valor_medio_pago")) == 1){// Tarjeta jumbo mas
				campos.add("num_tja");
				campos.add("num_cuotas");
			}else if(arg0.getParameter("valor_medio_pago") != null && Long.parseLong(arg0.getParameter("valor_medio_pago")) == 4){// Tarjeta Bancaria
				campos.add("nom_tban");
				campos.add("num_tja");
				campos.add("t_mes");
				campos.add("t_ano");
				campos.add("num_cuotas");
				campos.add("t_banco");
			}else if(arg0.getParameter("valor_medio_pago") != null && Long.parseLong(arg0.getParameter("valor_medio_pago")) == 3){//Cheque
				campos.add("ped_obs");
			}else if(arg0.getParameter("valor_medio_pago") != null && Long.parseLong(arg0.getParameter("valor_medio_pago")) == 5){//Credito
				campos.add("total_coti");
			}
			campos.add("valor_medio_pago");
			if (ValidateParameters(arg0, arg1, campos) == false ) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}
			*/
			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			
			// Validación de estado de la empresa
			//CotizacionesDTO cot = biz.getCotizacionesById( Long.parseLong(session.getAttribute("ses_cot_id").toString()) );
			String emp = biz.getEstadoSaldoEmpresaByCotizacion( Long.parseLong(session.getAttribute("ses_cot_id").toString()) );
			String []est_saldo = emp.split("~");
			String empEstado = String.valueOf(est_saldo[0]);
			Double empSaldo  = Double.valueOf(est_saldo[1]);
			
			// Si esta bloqueada error
			if(!empEstado.equals(Constantes.ESTADO_ACTIVADO)){
				throw new ParametroObligatorioException( "El estado de la Empresa no es ACTIVO." );
			}
			
			// Carga los datos en el objeto
			ProcInsCotizacionDTO cotizacion = new ProcInsCotizacionDTO();
			
			cotizacion.setCot_id(Long.parseLong(session.getAttribute("ses_cot_id").toString()));// ID cotizacion
			cotizacion.setObs(comentario);
			if( arg0.getParameter("pol_sustitucion").toString().equals("Si") )
				cotizacion.setId_sustitucion(1);
			else
				cotizacion.setId_sustitucion(2);
			cotizacion.setSustitucion(arg0.getParameter("pol_sustitucion"));//Política de Sustitucion
			cotizacion.setSgente_op(Long.parseLong(arg0.getParameter("sin_gente_op")));//Opción Persona autorizada
			cotizacion.setSgente_txt(arg0.getParameter("sin_gente_txt"));//Persona autorizada

			if ( Long.parseLong(arg0.getParameter("valor_medio_pago")) == 1 ) {
				// Tarjeta jumbo mas
				cotizacion.setMedio_pago(Constantes.MEDIO_PAGO_CAT);//Medio de pago
//				cotizacion.setNumero_tarjeta( Cifrador.encriptar(rb.getString("conf.bo.key"),arg0.getParameter("num_tja").toString()));//NUmero de tarjeta
//				cotizacion.setNumero_cuotas(Integer.parseInt(arg0.getParameter("num_cuotas")));//Numero de cuotas
				
			} else if ( Long.parseLong(arg0.getParameter("valor_medio_pago")) == 4 ) {
				// Tarjeta Bancaria
				cotizacion.setMedio_pago(Constantes.MEDIO_PAGO_TBK);//Medio de pago
//				cotizacion.setTbk_nombre_tarjeta(arg0.getParameter("nom_tban"));//Nombre tarjeta bancaria
//				cotizacion.setNumero_tarjeta( Cifrador.encriptar(rb.getString("conf.bo.key"),arg0.getParameter("num_tja").toString()));//NUmero de tarjeta
//				cotizacion.setTbk_fec_expira(arg0.getParameter("t_mes") + "" + arg0.getParameter("t_ano"));//Fecha de expiracion
//				cotizacion.setNumero_cuotas(Integer.parseInt(arg0.getParameter("num_cuotas")));//Numero de cuotas
//				cotizacion.setNombre_banco(arg0.getParameter("t_banco").toString());//Nombre del banco
				
			} else if ( Long.parseLong(arg0.getParameter("valor_medio_pago")) == 3 ) {
				//Cheque				
				cotizacion.setMedio_pago(Constantes.MEDIO_PAGO_CAT);//Medio de pago
//				cotizacion.setNumero_tarjeta( Cifrador.encriptar(rb.getString("conf.bo.key"),"1111111111111111"));//NUmero de tarjeta
				if (comentario.trim().equals("")){
				    comentarios = arg0.getParameter("ped_obs");
				}else{
				    comentarios = "1) " + comentario + "\n2) " + arg0.getParameter("ped_obs");
				}
				cotizacion.setObs(comentarios);
				
			} else if ( Long.parseLong(arg0.getParameter("valor_medio_pago")) == 5 ) {
				//Credito
				cotizacion.setMedio_pago(Constantes.MEDIO_PAGO_LINEA_CREDITO);//Medio de pago
				// Validación al medio de pago cuendo el comprador acepta la cotización
				if ( Double.parseDouble(arg0.getParameter("total_coti")) > empSaldo.doubleValue() ) {
					logger.info("El valor de la cotizacion es mayor al saldo de la empresa ");
				} else {
					logger.info("El valor de la cotizacion es menor al saldo de la empresa ");
				}
			}

			biz.setAceptarCotizacion(cotizacion);
			logger.info("Se ha cambiado el estado de la cotizacion a Aceptada y se ha grabado informacion del medio de pago.");
			
		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (Exception e) {
			logger.warn("Controlando excepción de sistema: " + e.getMessage());
			e.printStackTrace();
			throw new SystemException(e);
		}
			
		// 3. Redirecciona salida
		// Recupera pagina desde web.xml
		logger.debug("Redireccionando a: " + dis_ok + fp.forward());
		//arg1.sendRedirect(dis_ok + fp.forward() );			
		arg1.sendRedirect(dis_ok);

	}

}