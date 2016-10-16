package cl.bbr.boc.service;

import java.util.List;

import cl.bbr.boc.ctrl.CuponesDCCtrl;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.model.CuponEntity;
import cl.bbr.jumbocl.common.model.CuponPorProducto;
import cl.bbr.jumbocl.common.model.CuponPorRut;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * @author JoLazoGu
 * @version 1
 */
public class CuponesDCService {

	Logging logger = new Logging(this);

	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List getListaTiposCupones() throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();
		List result = null;
		
		try {
			result = cupCtrl.getListaTiposCupones();
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
		return result;
	}

	/**
	 * 
	 * @param cuponId
	 * @return
	 * @throws ServiceException
	 */
	public CuponEntity getDatoCuponPorId(CuponEntity cuponId) throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();

		try {
			return cupCtrl.getDatoCuponPorId(cuponId);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}

	}

	/**
	 * 
	 * @param cupon
	 * @return
	 * @throws ServiceException
	 */
	public CuponEntity getDatoCuponPorCodigo(CuponEntity cupon) throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();

		try {
			return cupCtrl.getDatoCuponPorCodigo(cupon);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}

	}

	/**
	 * 
	 * @param cupon
	 * @return
	 * @throws ServiceException
	 */
	public boolean setGuardarCupon(CuponEntity cupon) throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();

		try {
			return cupCtrl.setGuardarCupon(cupon);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}

	}

	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List getListaRubros() throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();
		List result = null;
		
		try {
			result = cupCtrl.getListaRubros();
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
		return result;
	}

	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List getListaSecciones() throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();
		List result = null;
		
		try {
			result = cupCtrl.getListaSecciones();
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
		return result;
	}

	/**
	 * 
	 * @param cpr
	 * @param id_usuario
	 * @return
	 * @throws ServiceException
	 */
	public boolean setCargaRutMasiva(CuponPorRut cpr, long id_usuario) throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();

		try {
			return cupCtrl.setCargaRutMasiva(cpr, id_usuario);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}

	}

	/**
	 * 
	 * @param ce
	 * @throws ServiceException
	 */
	public void setTodasLasSeccionesAsociado(CuponEntity ce) throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();

		try {
			cupCtrl.setTodasLasSeccionesAsociado(ce);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}

	}

	/**
	 * 
	 * @param id_cup_dto
	 * @return
	 * @throws ServiceException
	 */
	public int getCantidadRutAsociado(int id_cup_dto) throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();

		try {
			return cupCtrl.getCantidadRutAsociado(id_cup_dto);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}

	}

	/**
	 * 
	 * @param cpp
	 * @param rad_tipo
	 * @param id_usuario
	 * @throws ServiceException
	 */
	public void setCuponAsociarTipo(CuponPorProducto cpp, String rad_tipo, long id_usuario, String rad_despacho) throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();

		try {
			cupCtrl.setCuponAsociarTipo(cpp, rad_tipo, id_usuario, rad_despacho);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}

	}

	/**
	 * 
	 * @param id_cup_dto
	 * @return
	 * @throws ServiceException
	 */
	public List getListaCuponAsociado(int id_cup_dto) throws ServiceException {

		CuponesDCCtrl cupCtrl = new CuponesDCCtrl();
		List result = null;
		
		try {
			result = cupCtrl.getListaCuponAsociado(id_cup_dto);
		} catch (Exception e) {
			logger.error("Error", e);
			throw new ServiceException(e);
		}
		return result;
	}

}
