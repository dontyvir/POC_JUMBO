package cl.bbr.fo.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Comando que revisa si los datos del medio de pago tarjeta paris normal son correctos.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ValidaCuentaCheck extends Command {

	/**
	 * Revisa si el medio de pago es correcto. Retorna True o False.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		//Recupera la sesión del usuario
		HttpSession session = arg0.getSession();
		
		boolean empleado = false;
		String nro_tarjeta = "";
		int clave = 0;
		int cuotas = 0;
		long total = 0;
		//Se recupera si el cliente es Empleado Paris (1:Empleado, 0:No Empleado)
		if (session.getAttribute("ses_empleado_paris") != null){
			if (((String)session.getAttribute("ses_empleado_paris")).equals("SI")){
				empleado = true;
				this.getLogger().info("Es Empleado");
			}	
			else {
				empleado = false;
				this.getLogger().info("NO Es Empleado");
			}	
		} else {
			empleado = false;
			this.getLogger().info("NO Es Empleado");
		}
		
		cuotas = Integer.parseInt(arg0.getParameter("cuotas"));
		total = Long.parseLong(arg0.getParameter("total"));
		
		if (empleado) {
			nro_tarjeta = "00" + ((String)session.getAttribute("ses_cta_empleado")).substring(2,((String)session.getAttribute("ses_cta_empleado")).length());
			clave = Integer.parseInt(arg0.getParameter("clave"));
		} else {
			nro_tarjeta = "00" + arg0.getParameter("nro_tarjeta").substring(2,arg0.getParameter("nro_tarjeta").length());
		}
		
		//ClienteDeCompra cliente = new ClienteDeCompra();
		//cliente.setRut(new Long((String)session.getAttribute("ses_cli_rut")));
		//cliente.setDv((String)session.getAttribute("ses_cli_dv"));
		//cliente.setNroTarjeta(new Long(nro_tarjeta));
		//cliente.setClaveTarjeta(new Integer(clave));
		//cliente.setNumCuotas(new Integer(cuotas));
		//if (empleado)
		//	cliente.setTipoTarjeta(new Integer(1));
		//else
		//	cliente.setTipoTarjeta(new Integer(arg0.getParameter("titular")));	
		//cliente.setMontoTotal(new Long(total));
		//CompraValida compra = new ValidaCuentaCompraProcess().processing(cliente);

		//if (compra.isValida()){
		//	out.print( "1" + "|" + compra.getCodigo() );
		//}else{
		//	out.print( "0" + "|" + compra.getCodigo() );
		//}
		
		//por defecto no es valida
		out.print( "0" + "|" + "0" );
	}

}