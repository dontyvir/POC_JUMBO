/*
 * Creado el 03/08/2012
 *
 */
package cl.jumbo.fo.servlet.services;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.command.Command;

/**
 * @author EAvendanoA
 *
 */
public class CheckOverrideWebPay extends Command {

	/* (sin Javadoc)
	 * @see cl.bbr.fo.command.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		
		String overrideWebPay = super.getWebPayOverride();

        PrintWriter out = arg1.getWriter();
        out.print(overrideWebPay);
        out.flush();
        out.close();

	}

}
