/*
 * Created on 18-06-2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.interfaces;

import java.util.Vector;

/**
 * @author Javier Villalobos
 *
 */
public interface ProcesoInterfaz {
	
	public void runProcess();
	
	public void toStop();
	
	public Vector getStatusHistory();
	
	public Status getStatus();
	
	public void toPause();

	public void toResume();
	
	public void toSleep(long time);
}
