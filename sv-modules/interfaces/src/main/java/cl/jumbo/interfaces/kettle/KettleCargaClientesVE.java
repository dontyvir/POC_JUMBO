/*
 * Created on 27-08-2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.interfaces.kettle;

import cl.jumbo.interfaces.ControladorProcesos;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class KettleCargaClientesVE extends KettleProcess {

	/**
	 * @param controlador
	 * @param kettleScript
	 */
	public KettleCargaClientesVE(ControladorProcesos controlador) {
		super(controlador, KettleScripts.CARGA_CLIENTES_VE);
	}

}
