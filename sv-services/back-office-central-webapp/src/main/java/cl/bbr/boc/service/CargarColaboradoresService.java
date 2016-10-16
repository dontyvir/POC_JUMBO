package cl.bbr.boc.service;

import java.util.List;

import cl.bbr.boc.ctrl.CargarColaboradoresCtrl;
import cl.bbr.jumbocl.common.exceptions.ServiceException;

public class CargarColaboradoresService {
	
	public boolean truncateTableColaborador() throws ServiceException{
		CargarColaboradoresCtrl ccCtrl = new CargarColaboradoresCtrl();
		try {
			return ccCtrl.truncateTableColaboradores();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	public boolean cargarColaboradores(List colaboradores) throws ServiceException {
		CargarColaboradoresCtrl ccCtrl = new CargarColaboradoresCtrl();
		try {
			return ccCtrl.cargarColaboradores(colaboradores);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	public String cantidadColaboradores() throws ServiceException {
		CargarColaboradoresCtrl ccCtrl = new CargarColaboradoresCtrl();
		try {
			return ccCtrl.cantidadColaboradores();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
}
