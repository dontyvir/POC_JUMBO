package cl.bbr.vte.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.ProcInsDetCotizacionDTO;

/**
 * 
 * <p>Este comando permite agregar detalles (productos) a una cotización.</p>
 * <P>Se valida que exista el identificador único de la cotización.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class AddNewDetalle extends Command {

	/**
	 * Inserta una lista de detalles a la cotización
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

		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("dis_er");
		logger.debug("UrlError: " + UrlError);

		ForwardParameters fp = new ForwardParameters();
		fp.add( arg0.getParameterMap() );

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");		

		try {

			// 2.1 revision de parametros obligatorios
			/*
			ArrayList campos = new ArrayList();
			campos.add("id_cat");
			campos.add("id_cat_padre");
			if (ValidateParameters(arg0, arg1, campos) == false) {
				throw new ParametroObligatorioException( "Falta parámetro obligatorio." );
			}
			*/	

			if( session.getAttribute("ses_cot_id") == null )
				throw new ParametroObligatorioException( "Falta parámetro obligatorio. (ses_cot_id)" );
			
		    //2.2 obtiene parametros desde el request

			// Llenamos la lista con los productos
			List l_productos = new ArrayList();
			ProcInsDetCotizacionDTO detalle = null;
			long tot_reg = Long.parseLong(arg0.getParameter( "tot_reg" ));
			long cot_id = Long.parseLong(session.getAttribute("ses_cot_id").toString());
			for( int i = 0; i < tot_reg; i++ ) {
				if(arg0.getParameter("pro_id"+i) != null 
						&& !arg0.getParameter("valor"+i).equals("") 
						&& !arg0.getParameter("valor"+i).equals("0") ){
					detalle = new ProcInsDetCotizacionDTO();
					detalle.setCot_id( cot_id );
					detalle.setObs( "" );
					detalle.setPro_id( Long.parseLong(arg0.getParameter("pro_id"+i)) );
					detalle.setQsolic( Double.parseDouble(arg0.getParameter("valor"+i)) );
					l_productos.add( detalle );
				}
			}
			
			//Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			biz.doInsCotizacionDetail( l_productos );
			logger.info("Se han insertado los productos.");
			
		} catch (ParametroObligatorioException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			logger.info("URL: " + UrlError);
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;
		/*} catch (VteException e) {
			logger.warn("Controlando excepción de negocios: " + e.getMessage());
			fp.add("cod_error", e.getMessage());
			UrlError = UrlError + fp.forward();
			getServletConfig().getServletContext().getRequestDispatcher(UrlError).forward(arg0, arg1);
			return;*/
		} catch (Exception e) {
			logger.warn("Controlando excepción de sistema: " + e.getMessage());
			e.printStackTrace();
			throw new SystemException(e);
		}
			
		// 4. Redirecciona salida
		// Recupera pagina desde web.xml
		String dis_ok = getServletConfig().getInitParameter("dis_ok");
		logger.debug("Redireccionando a: " + dis_ok + fp.forward());
		//arg1.sendRedirect(dis_ok + fp.forward() );			
		arg1.sendRedirect(dis_ok);

	}

}