/*
 * Created on 18-07-2007
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
public class KettleCargaProductos extends KettleProcess {

	public KettleCargaProductos(ControladorProcesos controlador) {
		super(controlador, KettleScripts.CARGA_PRODUCTOS);
	}
}
