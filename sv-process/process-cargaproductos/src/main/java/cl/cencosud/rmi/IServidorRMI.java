package cl.cencosud.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServidorRMI extends Remote {
    public void modificarPrecios( List pro ) throws RemoteException;
}
