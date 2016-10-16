package cl.bbr.boc.components;

import cl.bbr.boc.actions.ProductosDelegate;

public class ServiciosFactoryImpl extends Servicios{
	
	public ProductosDelegate getInstanceProductosDelegate() {
		return new ProductosDelegate(new cl.bbr.jumbocl.productos.service.ProductosService());
	}	
}
