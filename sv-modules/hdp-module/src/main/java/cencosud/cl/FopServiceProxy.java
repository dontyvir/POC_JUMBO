package cencosud.cl;

public class FopServiceProxy implements cencosud.cl.FopService {
  private boolean _useJNDI = true;
  private boolean _useJNDIOnly = false;
  private String _endpoint = null;
  private cencosud.cl.FopService __fopService = null;
  
  public FopServiceProxy() {
    _initFopServiceProxy();
  }
  
  private void _initFopServiceProxy() {
  
    if (_useJNDI || _useJNDIOnly) {
      try {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        __fopService = ((cencosud.cl.Fop)ctx.lookup("java:comp/env/service/Fop")).getFopServicePort();
      }
      catch (javax.naming.NamingException namingException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("JNDI lookup failure: javax.naming.NamingException: " + namingException.getMessage());
          namingException.printStackTrace(System.out);
        }
      }
      catch (javax.xml.rpc.ServiceException serviceException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("Unable to obtain port: javax.xml.rpc.ServiceException: " + serviceException.getMessage());
          serviceException.printStackTrace(System.out);
        }
      }
    }
    if (__fopService == null && !_useJNDIOnly) {
      try {
        __fopService = (new cencosud.cl.FopLocator()).getFopServicePort();
        
      }
      catch (javax.xml.rpc.ServiceException serviceException) {
        if ("true".equalsIgnoreCase(System.getProperty("DEBUG_PROXY"))) {
          System.out.println("Unable to obtain port: javax.xml.rpc.ServiceException: " + serviceException.getMessage());
          serviceException.printStackTrace(System.out);
        }
      }
    }
    if (__fopService != null) {
      if (_endpoint != null)
        ((javax.xml.rpc.Stub)__fopService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
      else
        _endpoint = (String)((javax.xml.rpc.Stub)__fopService)._getProperty("javax.xml.rpc.service.endpoint.address");
    }
    
  }
  
  
  public void useJNDI(boolean useJNDI) {
    _useJNDI = useJNDI;
    __fopService = null;
    
  }
  
  public void useJNDIOnly(boolean useJNDIOnly) {
    _useJNDIOnly = useJNDIOnly;
    __fopService = null;
    
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (__fopService == null)
      _initFopServiceProxy();
    if (__fopService != null)
      ((javax.xml.rpc.Stub)__fopService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public java.lang.String get(java.lang.String request) throws java.rmi.RemoteException, cencosud.cl.WebServiceFaultWrapper{
    if (__fopService == null)
      _initFopServiceProxy();
    return __fopService.get(request);
  }
  
  
  public cencosud.cl.FopService getFopService() {
    if (__fopService == null)
      _initFopServiceProxy();
    return __fopService;
  }
  
}