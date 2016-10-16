package cl.bbr.boc.components;

import cl.bbr.boc.factory.ServicioAbstractFactory;

public class ServicioFactory {

	private static class SingletonHelper{
		private static final ServicioFactory INSTANCE = new ServicioFactory();
	}
	
	public static Servicios getService(ServicioAbstractFactory factory){
        return factory.createService();
    }
	
	public static ServicioFactory getInstance(){
		return SingletonHelper.INSTANCE;
	}
}

