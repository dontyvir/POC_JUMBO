package cl.bbr.boc.factory;

import cl.bbr.boc.components.Servicios;
import cl.bbr.boc.components.ServiciosFactoryImpl;

public class ProductosFactory implements ServicioAbstractFactory{

	public Servicios createService() {		
		return new ServiciosFactoryImpl();
	}
}
