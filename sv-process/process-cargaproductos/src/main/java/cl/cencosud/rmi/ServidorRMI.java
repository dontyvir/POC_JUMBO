package cl.cencosud.rmi;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import cl.cencosud.util.Parametros;

public class ServidorRMI extends UnicastRemoteObject implements IServidorRMI {
    private static final long serialVersionUID = -2147501264295553926L;

   protected ServidorRMI() throws RemoteException {
      super();
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.cencosud.socket.rmi.IServidor#prueba(int, int)
    */
    public void modificarPrecios( List precios ) throws RemoteException {
      try {
         ModificarPrecios modificar = new ModificarPrecios();
         modificar.preparaPreciosActualizar(precios);
      } catch (Exception e) {
         e.printStackTrace();
         throw new RemoteException(e.getMessage());
      }
   }

   public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException {
      IServidorRMI remota = new ServidorRMI();
      String port = Parametros.getString("PUERTO_RMI");
      String ip = Parametros.getString("IP_RMI");
      //java.net.InetAddress.getLocalHost().getHostAddress()
      java.rmi.Naming.rebind("//" + ip + ":" + port + "/Servidor", remota);
   }
}
