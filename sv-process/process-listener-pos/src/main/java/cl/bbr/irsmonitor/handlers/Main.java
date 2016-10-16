package cl.bbr.irsmonitor.handlers;



import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.WrapperListener;

                    
public class Main implements WrapperListener{
    private Server m_app;

    private Main(){
    }

    public Integer start( String[] args ){
        try{
        	m_app = new Server();
			boolean ret = m_app.setConfiguration(ResourceBundle.getBundle("configuracion"));
        	if (ret == false)
        		return new Integer( -1 );
			m_app.start();
        	System.out.println("Comienzo servicio");
        }
		catch(MissingResourceException e){
			System.err.println("Error Obteniendo Configuracion");
			e.printStackTrace();
			return new Integer( -1 );
		}
        catch(Exception e){
        	e.printStackTrace();
        	System.err.println("Error en ejecucion servicio");
        	System.err.println(e.getMessage());        	
			return new Integer( -1 );
        }
        return null;
    }

    public int stop( int exitCode ){
    	m_app.stop();
    	return exitCode;
    }
    
    public void controlEvent( int event ){
        if (WrapperManager.isControlledByNativeWrapper()) {

        } else {
            if ((event == WrapperManager.WRAPPER_CTRL_C_EVENT) ||
                (event == WrapperManager.WRAPPER_CTRL_CLOSE_EVENT) ||
                (event == WrapperManager.WRAPPER_CTRL_SHUTDOWN_EVENT)){
                WrapperManager.stop(0);
            }
        }
    }
    
    public static void main( String[] args ){
        WrapperManager.start( new Main(), args );
    }
}
