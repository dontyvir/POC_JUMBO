package cl.bbr.irs.promolib.dao;

import java.util.List;

import cl.bbr.irs.promolib.entity.LocalBoEntity;
import cl.bbr.irs.promolib.entity.MatrizSeccionEntity;
import cl.bbr.irs.promolib.entity.ProductoPromosEntity;
import cl.bbr.irs.promolib.entity.PromoSeccionEntity;
import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionDAOException;

public interface IrsPromocionesDAO {
	/**
	 * Metodo para obtener todos las promociones (tipos y codigos)
	 * de seccion activas para un determinado local
	 * @param local
	 * @return List PromoSeccionEntity
	 * @throws IrsPromocionDAOException
	 */
	public List getAllPromoSeccion(int local) throws IrsPromocionDAOException;
	
	/**
	 * Metdo para obtener una promocion de seccion
	 * activa para un determinado local
	 * @param tipo Tipo de promocion seccion (lunes, martes...)
	 * @param local Codigo de local
	 * @return PromoSeccionEntity
	 * @throws IrsPromocionDAOException
	 */
	public PromoSeccionEntity getPromoSeccion(int tipo, int local, boolean getPromoSeccion) throws IrsPromocionDAOException;
	/**
	 * Agregar la referencia de la promocion para una determinada seccion
	 * @param promo
	 * @throws IrsPromocionDAOException
	 */
	public void insPromoSeccion(PromoSeccionEntity promo) throws IrsPromocionDAOException;

	/**
	 * Actualiza la referencia de la promocion para una determinada seccion
	 * @param promo
	 * @return registros actualizados
	 * @throws IrsPromocionDAOException
	 */
	public int updPromoSeccion(PromoSeccionEntity promo) throws IrsPromocionDAOException;
	
	
	/**
	 * Elimina la promocion para una determinada seccion
	 * @param promo
	 * @throws IrsPromocionDAOException
	 */
	public void delPromoSeccion(PromoSeccionEntity promo) throws IrsPromocionDAOException;
	/**
	 * Metodo para obtener toda la matriz de seccion
	 * y promociones para un determinado local
	 * @param local 
	 * @return List MatrizSeccionEntity
	 * @throws IrsPromocionDAOException
	 */
	public List getAllMatrizSeccion(int local) throws IrsPromocionDAOException;
	
	/**
	 * Metodo para obtener todas las promociones de una seccion para un determinado local
	 * @param seccion
	 * @param local
	 * @return MatrizSeccionEntity
	 * @throws IrsPromocionDAOException
	 */
	public MatrizSeccionEntity getMatrizSeccion(int seccion, int local) throws IrsPromocionDAOException;
	
	/**
	 * Agrega matriz de promociones activas por promocion
	 * @param matriz
	 * @throws IrsPromocionDAOException
	 */
	public void insMatrizSeccion(MatrizSeccionEntity matriz) throws IrsPromocionDAOException;
	/**
	 * actualiza matriz de promociones activas por promocion
	 * @param matriz
	 * @return numero de registros actualizados
	 * @throws IrsPromocionDAOException
	 */
	public int updMatrizSeccion(MatrizSeccionEntity matriz) throws IrsPromocionDAOException;
	
	/**
	 * Elimina matriz de promociones activas por promocion
	 * @param matriz
	 * @throws IrsPromocionDAOException
	 */
	public void delMatrizSeccion(MatrizSeccionEntity matriz) throws IrsPromocionDAOException;	
	/**
	 * Metodo para obtener una promocion a partir del codigo
	 * para un determinado local
	 * @param codigo
	 * @param local
	 * @return PromocionEntity
	 * @throws IrsPromocionDAOException
	 */
	public PromocionEntity getPromocion(int codigo, int local) throws IrsPromocionDAOException;
	
	/**
	 * Metodo para agregar una promocion
	 * @param promo
	 * @throws IrsPromocionDAOException
	 */
	public void insPromocion(PromocionEntity promo) throws IrsPromocionDAOException;
	/**
	 * Metodo para actualizar una promocion
	 * @param promo
	 * @return numero de registros actualizados 
	 * @throws IrsPromocionDAOException
	 */
	public int updPromocion(PromocionEntity promo) throws IrsPromocionDAOException;
	/**
	 * Metodo para borrar una promocion
	 * @param promo
	 * @throws IrsPromocionDAOException
	 */
	public void delPromocion(PromocionEntity promo) throws IrsPromocionDAOException;
	
	/**
	 * * Obtiene las promociones de un producto 
	 * @param id_producto
	 * @param id_local
	 * @return ProductoPromosEntity
	 * @throws IrsPromocionDAOException
	 */
	public ProductoPromosEntity getPromocionProducto(int id_producto, int id_local )throws IrsPromocionDAOException;
	
	/**
	 * Agrega  la referencia de la promocion para el producto
	 * @param producto
	 * @throws IrsPromocionDAOException
	 */
	public void insPromocionProducto(ProductoPromosEntity producto) throws IrsPromocionDAOException;	
	/**
	 * Actualiza la referencia de la promocion para el producto
	 * @param producto
	 * @return numero de registros actualizados
	 * @throws IrsPromocionDAOException
	 */
	public int updPromocionProducto(ProductoPromosEntity producto) throws IrsPromocionDAOException;
	/**
	 * Quita la referencia de la promoción para el producto 
	 * @param producto
	 * @throws IrsPromocionDAOException
	*/	 
	public void delPromocionProducto(ProductoPromosEntity producto) throws IrsPromocionDAOException;	

	

	/**
	 * Obtiene la información del local BO de jumbo.cl con un id_local del BackOffice de Promociones 
	 * @param local_sap
	 * @return LocalBoEntity Local BO jumbo.cl
	 * @throws IrsPromocionDAOException
	 */
	public LocalBoEntity getLocalBySap(String local_sap) throws IrsPromocionDAOException;	
	
	/**
	 * Obtiene id_producto del BO de jumbo.cl con un codigo SAP y Unidad de Medida 
	 * @param ean13
	 * @return id_producto
	 * @throws IrsPromocionDAOException
	 */
	public int getProductoBO(String ean13) throws IrsPromocionDAOException;	
}
