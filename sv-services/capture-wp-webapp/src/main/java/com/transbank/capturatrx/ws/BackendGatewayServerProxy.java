package com.transbank.capturatrx.ws;

public class BackendGatewayServerProxy implements com.transbank.capturatrx.ws.BackendGatewayServer {
  private boolean _useJNDI = true;
  private String _endpoint = null;
  private com.transbank.capturatrx.ws.BackendGatewayServer backendGatewayServer = null;
  
  public BackendGatewayServerProxy() {
    _initBackendGatewayServerProxy();
  }
  
  private void _initBackendGatewayServerProxy() {
  
  if (_useJNDI) {
    try{
      javax.naming.InitialContext ctx = new javax.naming.InitialContext();
      backendGatewayServer = ((com.transbank.capturatrx.ws.BackendGatewayServerImplService)ctx.lookup("java:comp/env/service/BackendGatewayServerImplService")).getBackendGatewayServerImplPort();
      }
    catch (javax.naming.NamingException namingException) {}
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  if (backendGatewayServer == null) {
    try{
      backendGatewayServer = (new com.transbank.capturatrx.ws.BackendGatewayServerImplServiceLocator()).getBackendGatewayServerImplPort();
      }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  if (backendGatewayServer != null) {
    if (_endpoint != null)
      ((javax.xml.rpc.Stub)backendGatewayServer)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    else
      _endpoint = (String)((javax.xml.rpc.Stub)backendGatewayServer)._getProperty("javax.xml.rpc.service.endpoint.address");
  }
  
}


public void useJNDI(boolean useJNDI) {
  _useJNDI = useJNDI;
  backendGatewayServer = null;
  
}

public String getEndpoint() {
  return _endpoint;
}

public void setEndpoint(String endpoint) {
  _endpoint = endpoint;
  if (backendGatewayServer != null)
    ((javax.xml.rpc.Stub)backendGatewayServer)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
  
}

public com.transbank.capturatrx.ws.BackendGatewayServer getBackendGatewayServer() {
  if (backendGatewayServer == null)
    _initBackendGatewayServerProxy();
  return backendGatewayServer;
}

public com.transbank.capturatrx.ws.TrxReturn capturaDiferida(com.transbank.capturatrx.ws.TrxInput arg0) throws java.rmi.RemoteException{
  if (backendGatewayServer == null)
    _initBackendGatewayServerProxy();
  return backendGatewayServer.capturaDiferida(arg0);
}


}