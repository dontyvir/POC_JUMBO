package cl.bbr.vte.command;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.ProcInsCotizacionDTO;
import cl.bbr.vte.exception.VteException;

/**
 * 
 * <p>Este comando inserta un nuevo encabezado de cotización sin detalle de productos.</p>
 * <p>Se valida que ciertos parámetros sean de caracter obligatorio.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddNewCotizacion extends Command {

	/**
	 * Inserta un nuevo encabezado de cotización sin detalle de productos 
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	public void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		// Obtiene sessión
		HttpSession session = arg0.getSession();

		// Carga properties
		ResourceBundle rb = ResourceBundle.getBundle("vte");		
		
		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);

		ForwardParameters fp = new ForwardParameters();
		//fp.add("cpr_id", session.getAttribute("id_comprador_tmp")+"");
		//fp.add( arg0.getParameterMap() );

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");		

		try {

			// 2.1 revision de parametros obligatorios

			// Se revisan que los parámetros mínimos existan
			ArrayList campos = new ArrayList();
			campos.add("empresas");
			campos.add("sucursales");
			campos.add("dir_desp");
			campos.add("dir_fact");
			campos.add("fecha_desp");
			campos.add("f_pago");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}	

		     //2.2 obtiene parametros desde el request
			long emp_id  = Long.parseLong(arg0.getParameter("empresas"));
			long suc_id  = Long.parseLong(arg0.getParameter("sucursales"));
			long dirdesp_id  = Long.parseLong(arg0.getParameter("dir_desp"));
			long dirfact_id  = Long.parseLong(arg0.getParameter("dir_fact"));
			String fecha_desp  = Formatos.formatFechaHoraIni(arg0.getParameter("fecha_desp"));
			String f_pago = "";
			String num_tja = null;			

			if (arg0.getParameter("f_pago").equals("1") || arg0.getParameter("f_pago").equals("2") )//Tarjeta Jumbo mas, Tarjeta Paris 
				f_pago = "CAT";
			else if(arg0.getParameter("f_pago").equals("5")){//Linea de credito
				f_pago = "CRE";
			}else if(arg0.getParameter("f_pago").equals("3")){//Cheque
				f_pago = "CAT";
				num_tja = "1111111111111111";
			}else if(arg0.getParameter("f_pago").equals("4")){//Tarjeta de credito
				f_pago = "TBK";
			}
			
			long cpr_id = Long.parseLong(session.getAttribute("ses_com_id")+"");
			
			
			ProcInsCotizacionDTO insCoti = new ProcInsCotizacionDTO();
			
			insCoti.setId_empresa(emp_id);
			insCoti.setId_sucursal(suc_id);
			insCoti.setId_dir_desp(dirdesp_id);
			insCoti.setId_dir_fac(dirfact_id);
			insCoti.setFec_acordada(fecha_desp);
			insCoti.setMedio_pago(f_pago);
			insCoti.setId_comprador(cpr_id);
			if(  num_tja != null)
				insCoti.setNumero_tarjeta(Cifrador.encriptar(rb.getString("conf.bo.key"),num_tja));
			if(session.getAttribute("ses_fono_id") != null){
				insCoti.setCot_user_fono_id(Integer.parseInt(session.getAttribute("ses_fono_id").toString()));
			}
				
			
			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			long respins = biz.doInsCotizacionHeader(insCoti);
			if (respins > 0){
				logger.info("Se agrego una nueva cotizacion: " + respins);
				session.setAttribute("ses_cot_id", respins+"");
			}
	
			
		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			logger.info("URL: " + UrlError);
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		} catch (VteException e) {
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
			
		     // 4. Redirecciona salida
			// Recupera pagina desde web.xml
			
			String dis_ok = getServletConfig().getInitParameter("dis_ok");
			logger.debug("Redireccionando a: " + dis_ok);
			arg1.sendRedirect(dis_ok);			

	}

}