package cl.cencosud.procesos.carroabandonado.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import cl.cencosud.procesos.carroabandonado.dto.CarroAbandonadoDTO;
import cl.cencosud.procesos.carroabandonado.util.JFactory;
import cl.cencosud.procesos.carroabandonado.util.Util;

/**
 * Clase que se encarga de obtener el acceso a datos para 
 * recuperar los carro abandonados desde la base de datos
 */
public class CarroAbandonadoDAO {
	/**
	 *  logger de la clase
	 */
	protected static Logger logger = Logger.getLogger(CarroAbandonadoDAO.class.getName());
	
	private Connection conn;
	
	public CarroAbandonadoDAO() {
		this.conn = JFactory.getConnectionDB2();
	}
	
	/**
	 * Método que se encarga de recuperar los carros abandonados.
	 * @return <code>List</code> Listado de registros
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public List getCarrosAbandonados()throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getCarrosAbandonados] Ingreso al metodo");
		
		List carros  = null;
		
		try {
			logger.debug("[CarroAbandonadoDAO][getCarrosAbandonados] Recupero la query");
			StringBuffer sql = getQueryCarrosAbandonados();
			logger.info("[CarroAbandonadoDAO][getCarrosAbandonados] la query a ejecutar es " + sql);
			
			logger.debug("[CarroAbandonadoDAO][getCarrosAbandonados] Realizo llamado a obtener los datos");
			carros = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
		
			logger.debug("[CarroAbandonadoDAO][getCarrosAbandonados] Obtuve resultado.");
			
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getCarrosAbandonados] Error al recuperar datos", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getCarrosAbandonados] Finaliza el metodo.");
		return carros;

	}

	/**
	 * Método que se encarga de recuperar la suma del precio de todos los itemes del carro de compra.
	 * @return <code>CarroAbandonadoDTO</code> DTO con carro abandonado
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public CarroAbandonadoDTO getCategoriaMasCara(CarroAbandonadoDTO carrAband)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getCategoriaMasCara] Ingreso al metodo");
		CarroAbandonadoDTO carrAbandTemp = carrAband;
		
		List respuestas  = null;
		
		try {
			logger.debug("[CarroAbandonadoDAO][getCategoriaMasCara] Recupero la query");
			StringBuffer sql = getQueryCategoriaMasCara(carrAband.getCli_id());
			logger.info("[CarroAbandonadoDAO][getCategoriaMasCara] la query a ejecutar es " + sql);

			logger.debug("[CarroAbandonadoDAO][getCategoriaMasCara] Realizo llamado a obtener los datos");
			respuestas = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
			
			Iterator it =  respuestas.iterator();
			while (it.hasNext()) {
				Map respuesta = (Map) it.next();
				String cat_nombre = (String)respuesta.get("CAT_NOMBRE");
				int cat_id = ((Integer)respuesta.get("PRCA_CAT_ID")).intValue();
				int cat_suma = ((Integer)respuesta.get("SUMA2")).intValue();
				
				carrAbandTemp.setCat_nombre(cat_nombre);
				carrAbandTemp.setCat_id(cat_id);
				carrAbandTemp.setCat_suma(cat_suma);
				
				break;
			}
	
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getCategoriaMasCara] Error al recuperar datos", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getCategoriaMasCara] Finaliza el metodo.");

		return carrAbandTemp;
	}
	
	/**
	 * Método que se encarga de recuperar la suma del precio de todos los itemes del carro de compra.
	 * @return <code>CarroAbandonadoDTO</code> DTO con carro abandonado
	 * @throws SQLException Exception en caso de error en la consulta
	 * 
	 * 
	 * ---Actualizacion Se trabajar con Rubros.
	 */
	public List getRubroMasCaro(CarroAbandonadoDTO carrAband)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getSeccionMasCara] Ingreso al metodo");
		//CarroAbandonadoDTO carrAbandTemp = carrAband;
		
		List respuestas  = null;
		List listadoProductosRubros = new ArrayList();
		int sumaMasAlta = 0;
		
		try {
			logger.debug("[CarroAbandonadoDAO][getSeccionMasCara] Recupero la query");
			StringBuffer sql = getQueryRubroMasCaro(carrAband.getCli_id());
			logger.info("[CarroAbandonadoDAO][getSeccionMasCara] la query a ejecutar es " + sql);

			logger.debug("[CarroAbandonadoDAO][getSeccionMasCara] Realizo llamado a obtener los datos");
			respuestas = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
			
			Iterator it =  respuestas.iterator();
			while (it.hasNext()) {
				Map respuesta = (Map) it.next();
				String cat_nombre = (String)respuesta.get("NOMBRE_RUBRO");
				int cat_id = ((Integer)respuesta.get("ID_SECCION")).intValue();
				int rubro_id= ((Integer)respuesta.get("ID_RUBRO")).intValue();
				int cat_suma = ((Integer)respuesta.get("SUMA2")).intValue();
				
				CarroAbandonadoDTO carrAbandTemp = new CarroAbandonadoDTO();
				
				carrAbandTemp.setCat_nombre(cat_nombre);
				carrAbandTemp.setCat_id(cat_id);
				carrAbandTemp.setRubro_id(rubro_id);
				carrAbandTemp.setCat_suma(cat_suma);
				
				if(listadoProductosRubros.size() == 0)
				{
					sumaMasAlta = cat_suma;
				}
				
				if(sumaMasAlta == cat_suma)
					listadoProductosRubros.add(carrAbandTemp);
				else
					break;
				
			}
	
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getSeccionMasCara] Error al recuperar datos", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getSeccionMasCara] Finaliza el metodo.");

		return listadoProductosRubros;
	}
	/**
	 * Método que se encarga de recuperar la suma del precio de todos los itemes del carro de compra.
	 * @return <code>CarroAbandonadoDTO</code> DTO con carro abandonado
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public List getSeccionMasCara(CarroAbandonadoDTO carrAband)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getSeccionMasCara] Ingreso al metodo");
		//CarroAbandonadoDTO carrAbandTemp = carrAband;
		
		List respuestas  = null;
		List listaProductosSeccion = new ArrayList();
		int sumaMasAlta = 0;
		
		try {
			logger.debug("[CarroAbandonadoDAO][getSeccionMasCara] Recupero la query");
			StringBuffer sql = getQuerySeccionMasCara(carrAband.getCli_id());
			logger.info("[CarroAbandonadoDAO][getSeccionMasCara] la query a ejecutar es " + sql);

			logger.debug("[CarroAbandonadoDAO][getSeccionMasCara] Realizo llamado a obtener los datos");
			respuestas = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
			
			Iterator it =  respuestas.iterator();
			while (it.hasNext()) {
				Map respuesta = (Map) it.next();
				String cat_nombre = (String)respuesta.get("NOMBRE");
				int cat_id = ((Integer)respuesta.get("ID_SECCION")).intValue();
				int cat_suma = ((Integer)respuesta.get("SUMA2")).intValue();
				
				CarroAbandonadoDTO carrAbandTemp = new CarroAbandonadoDTO();
				carrAbandTemp.setCat_nombre(cat_nombre);
				carrAbandTemp.setCat_id(cat_id);
				carrAbandTemp.setCat_suma(cat_suma);
				
				if(listaProductosSeccion.size() == 0)
				{
					sumaMasAlta = cat_suma;
				}
				
				if(sumaMasAlta == cat_suma)
					listaProductosSeccion.add(carrAbandTemp);
				else
				break;
			}
	
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getSeccionMasCara] Error al recuperar datos", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getSeccionMasCara] Finaliza el metodo.");

		return listaProductosSeccion;
	}

	
	/**
	 * Método que se encarga de recuperar la categoria con la suma mas alta en costo.
	 * @return <code>CarroAbandonadoDTO</code> DTO con carro abandonado
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public CarroAbandonadoDTO getSumaCarroCompra(CarroAbandonadoDTO carrAband)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getCategoriaMasCara] Ingreso al metodo");
		CarroAbandonadoDTO carrAbandTemp = carrAband;
		
		List respuestas  = null;
		
		try {
			logger.debug("[CarroAbandonadoDAO][getCategoriaMasCara] Recupero la query");
			StringBuffer sql = getQuerySumaCarroCompra(carrAband.getCli_id());
			logger.info("[CarroAbandonadoDAO][getCategoriaMasCara] la query a ejecutar es " + sql);

			logger.debug("[CarroAbandonadoDAO][getCategoriaMasCara] Realizo llamado a obtener los datos");
			respuestas = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
			
			Iterator it =  respuestas.iterator();
			int sumaCarro = 0;
			while (it.hasNext()) {
				Map respuesta = (Map) it.next();
				int car_suma = ((Integer)respuesta.get("SUMA")).intValue();
				sumaCarro = sumaCarro + car_suma;
			}
	
			carrAbandTemp.setCar_suma(sumaCarro);
			
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getCategoriaMasCara] Error al recuperar datos", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getCategoriaMasCara] Finaliza el metodo.");

		return carrAbandTemp;
	}
	
	/**
	 * Método que se encarga de recuperar la suma del precio de todos los itemes del carro de compra.
	 * @return <code>CarroAbandonadoDTO</code> DTO con carro abandonado
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public CarroAbandonadoDTO getCuponRubro(CarroAbandonadoDTO carrAband)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Ingreso al metodo");
		CarroAbandonadoDTO carrAbandTemp = carrAband;
		
		List respuestas  = null;
		
		try {
			logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Recupero la query");
			StringBuffer sql = getQueryCuponRubro(carrAband.getCat_id(), carrAband.getRubro_id());
			logger.info("[CarroAbandonadoDAO][getCuponSeccion] la query a ejecutar es " + sql);
			carrAbandTemp.setCar_cupon_cantidad(0);
			logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Realizo llamado a obtener los datos");
			respuestas = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
			
			Iterator it =  respuestas.iterator();
			while (it.hasNext()) {
				Map respuesta = (Map) it.next();
				String car_cupon = (String)respuesta.get("CODIGO");
				String car_tipo_cupon = "R";
				int car_cupon_monto = ((Integer)respuesta.get("DESCUENTO")).intValue();
				int car_cupon_cantidad = ((Integer)respuesta.get("CANTIDAD")).intValue();
				String fecha = (Date)respuesta.get("FECHA_FIN")+"";
				
				carrAbandTemp.setCar_cupon(car_cupon);
				carrAbandTemp.setCar_tipo_cupon(car_tipo_cupon);
				carrAbandTemp.setCar_cupon_cantidad(car_cupon_cantidad);
				carrAbandTemp.setCar_cupon_monto_mail(car_cupon_monto+"%");
				carrAbandTemp.setCar_fecha_fin(fecha);
				
				break;
			}
	
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getCuponSeccion] Error al recuperar datos", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Finaliza el metodo.");

		return carrAbandTemp;
	}
	
	public CarroAbandonadoDTO getCuponSeccion(CarroAbandonadoDTO carrAband)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Ingreso al metodo");
		CarroAbandonadoDTO carrAbandTemp = carrAband;
		
		List respuestas  = null;
		
		try {
			logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Recupero la query");
			StringBuffer sql = getQueryCuponSeccion(carrAband.getCat_id());
			logger.info("[CarroAbandonadoDAO][getCuponSeccion] la query a ejecutar es " + sql);
			carrAbandTemp.setCar_cupon_cantidad(0);
			logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Realizo llamado a obtener los datos");
			respuestas = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
			
			Iterator it =  respuestas.iterator();
			while (it.hasNext()) {
				Map respuesta = (Map) it.next();
				String car_cupon = (String)respuesta.get("CODIGO");
				String car_tipo_cupon = "S";
				int car_cupon_monto = ((Integer)respuesta.get("DESCUENTO")).intValue();
				int car_cupon_cantidad = ((Integer)respuesta.get("CANTIDAD")).intValue();
				String fecha = (Date)respuesta.get("FECHA_FIN")+"";
				
				carrAbandTemp.setCar_cupon(car_cupon);
				carrAbandTemp.setCar_tipo_cupon(car_tipo_cupon);
				carrAbandTemp.setCar_cupon_cantidad(car_cupon_cantidad);
				carrAbandTemp.setCar_cupon_monto_mail(car_cupon_monto+"%");
				carrAbandTemp.setCar_fecha_fin(fecha);
				
				break;
			}
	
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getCuponSeccion] Error al recuperar datos", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Finaliza el metodo.");

		return carrAbandTemp;
	}

	/**
	 * Método que se encarga de recuperar la suma del precio de todos los itemes del carro de compra.
	 * @return <code>CarroAbandonadoDTO</code> DTO con carro abandonado
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public CarroAbandonadoDTO getCuponDespacho(CarroAbandonadoDTO carrAband)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Ingreso al metodo");
		CarroAbandonadoDTO carrAbandTemp = carrAband;
		
		List respuestas  = null;
		
		try {
			logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Recupero la query");
			StringBuffer sql = getQueryCuponDespacho();
			logger.info("[CarroAbandonadoDAO][getCuponSeccion] la query a ejecutar es " + sql);
			carrAbandTemp.setCar_cupon_cantidad(0);
			logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Realizo llamado a obtener los datos");
			respuestas = (List) JFactory.getQueryRunner().query(conn, sql.toString(), new MapListHandler());
			
			Iterator it =  respuestas.iterator();
			while (it.hasNext()) {
				Map respuesta = (Map) it.next();
				String car_cupon = (String)respuesta.get("CODIGO");
				String car_tipo_cupon = "D";
				int car_cupon_monto = ((Integer)respuesta.get("DESCUENTO")).intValue();
				int car_cupon_cantidad = ((Integer)respuesta.get("CANTIDAD")).intValue();
				String fecha = (Date)respuesta.get("FECHA_FIN")+"";
				
				carrAbandTemp.setCar_cupon(car_cupon);
				carrAbandTemp.setCar_tipo_cupon(car_tipo_cupon);
				carrAbandTemp.setCar_cupon_cantidad(car_cupon_cantidad);
				carrAbandTemp.setCar_cupon_monto_mail(car_cupon_monto+"%");
				carrAbandTemp.setCar_fecha_fin(fecha);
				
				break;
			}
	
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getCuponSeccion] Error al recuperar datos", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Finaliza el metodo.");

		return carrAbandTemp;
	}

	/**
	 * Método que se encarga de insertar en BD los carros abandonados con cupon asignado.
	 * @return <code>CarroAbandonadoDTO</code> DTO con carro abandonado
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public int getInsertCarroAbandonado(CarroAbandonadoDTO carrAband)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getCuponSeccion] Ingreso al metodo");
		CarroAbandonadoDTO carrAbandTemp = carrAband;
		
		List respuestas  = null;
		
		int respuesta = 0;
		int last_id = 0;
		
		int cli_id = carrAbandTemp.getCli_id();
		String car_tipo_cupon = carrAbandTemp.getCar_tipo_cupon();
		String cli_nombres_mail = carrAbandTemp.getCli_nombres_mail();
		String car_fecha_fin = carrAbandTemp.getCar_fecha_fin();
		String car_cupon_monto_mail = carrAbandTemp.getCar_cupon_monto_mail();
		String car_cupon = carrAbandTemp.getCar_cupon();
		String cli_nombre_seccion = carrAbandTemp.getCat_nombre();
		int estado_mail = 0;
		
		try {
			logger.debug("[CarroAbandonadoDAO][getInsertCarroAbandonado] Recupero la query");
			StringBuffer sql = getQueryInsertCarroAbandonado(cli_id, car_tipo_cupon, cli_nombres_mail, car_fecha_fin, car_cupon_monto_mail, car_cupon, cli_nombre_seccion, estado_mail);
			String sqlLastID = "SELECT IDENTITY_VAL_LOCAL() AS VAL FROM SYSIBM.SYSDUMMY1";
			logger.info("[CarroAbandonadoDAO][getInsertCarroAbandonado] la query a ejecutar es " + sql);
			logger.debug("[CarroAbandonadoDAO][getInsertCarroAbandonado] Realizo llamado a obtener los datos");
			
			respuesta = JFactory.getQueryRunner().update(conn, sql.toString());
			if (respuesta > 0){
				respuestas = (List) JFactory.getQueryRunner().query(conn, sqlLastID.toString(), new MapListHandler());
				Iterator it =  respuestas.iterator();
				while (it.hasNext()) {
					Map respuestaId = (Map) it.next();
					last_id = ((BigDecimal)respuestaId.get("VAL")).intValue();
					break;
				}

			}
			
	
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getInsertCarroAbandonado] Error al recuperar datos", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getInsertCarroAbandonado] Finaliza el metodo.");

		return last_id;
	}

	/**
	 * Método que se encarga de actualizar en BD los carros de compra marcandolos como email enviado.
	 * @return <code>boolean</code> estado de update de carro de compra
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public boolean getUpdateCarroCompra(int cli_id)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getUpdateCarroCompra] Ingreso al metodo");

		boolean respuesta = false;
		int respSql = 0;
		try {
			logger.debug("[CarroAbandonadoDAO][getUpdateCarroCompra] Recupero la query");
			StringBuffer sql = getQueryUpdateEstadoCarro(cli_id);
			logger.info("[CarroAbandonadoDAO][getUpdateCarroCompra] la query a ejecutar es " + sql);
			respSql = JFactory.getQueryRunner().update(conn, sql.toString());
			
			if (respSql>0) {
				respuesta = true;
			}
			else {
				respuesta = false;
			}
			
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getUpdateCarroCompra] Error al actualizar estado envio mail", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getUpdateCarroCompra] Finaliza el metodo.");

		return respuesta;
	}

	/**
	 * Método que se encarga de actualizar en BD los carros abandonados marcandolos como email enviado.
	 * @return <code>boolean</code> estado de update de carro de compra
	 * @throws SQLException Exception en caso de error en la consulta
	 */
	public boolean getUpdateCarroAbandonado(int car_id)throws SQLException{
		logger.debug("[CarroAbandonadoDAO][getUpdateCarroAbandonado] Ingreso al metodo");

		boolean respuesta = false;
		int respSql = 0;
		try {
			logger.debug("[CarroAbandonadoDAO][getUpdateCarroAbandonado] Recupero la query");
			StringBuffer sql = getQueryUpdateEstadoCarroAbandonado(car_id);
			logger.info("[CarroAbandonadoDAO][getUpdateCarroAbandonado] la query a ejecutar es " + sql);
			respSql = JFactory.getQueryRunner().update(conn, sql.toString());
			
			if (respSql>0) {
				respuesta = true;
			}
			else {
				respuesta = false;
			}
			
		} catch (SQLException e) {
			
			logger.error("[CarroAbandonadoDAO][getUpdateCarroAbandonado] Error al actualizar estado envio mail", e);
			throw e;
			
		}
		
		logger.debug("[CarroAbandonadoDAO][getUpdateCarroAbandonado] Finaliza el metodo.");

		return respuesta;
	}
	
	
	/**
	 * Método que se encarga de recuperar la query para carros abandonados.
	 * 
	 * @return <code>StringBuffer</code> con la query
	 */
	private StringBuffer getQueryCarrosAbandonados() {
		StringBuffer sql = JFactory.getStringBuffer();
	
		sql.append("SELECT C.CLI_ID, C.CLI_RUT, C.CLI_DV, C.CLI_NOMBRE, C.CLI_APELLIDO_PAT, C.CLI_APELLIDO_MAT, C.CLI_EMAIL ");
		sql.append("\n");
		sql.append("FROM FODBA.FO_CARRO_COMPRAS CC, FODBA.FO_CLIENTES C ");
		sql.append("\n");
		sql.append("WHERE CC.CAR_CLI_ID = C.CLI_ID ");
		sql.append("\n");
		sql.append("AND C.CLI_ESTADO = 'A' AND C.CLI_BLOQUEO = 'D' ");
		sql.append("\n");
		sql.append("AND CC.CAR_FEC_MICARRO IS NOT NULL ");
		sql.append("\n");
		sql.append("AND CC.CAR_EST_MAIL = 0 ");
		sql.append("\n");
		sql.append("AND C.CLI_RUT NOT IN ( ");
		sql.append("\n");
		sql.append("        SELECT COL.COL_RUT FROM FODBA.FO_COLABORADOR COL ");
		sql.append("\n");
		sql.append("    ) ");
		sql.append("\n");
		sql.append("AND (( DAYS(CURRENT TIMESTAMP) - DAYS(CC.CAR_FEC_MICARRO) ) >= "+Util.getPropertiesString("carro.dias.abandono.minimo")+" ) ");
		sql.append("\n");
		sql.append("AND CC.CAR_CLI_ID NOT IN ( ");
		sql.append("\n");
		sql.append("        SELECT CA.CAR_CLI_ID ");
		sql.append("\n");
		sql.append("        FROM FODBA.FO_CARRO_ABANDONADO CA ");
		sql.append("\n");
		sql.append("        WHERE (( DAYS(CURRENT TIMESTAMP) - DAYS(CA.CAR_FEC_MAIL) ) <= "+Util.getPropertiesString("carro.dias.abandono.maximo")+" ) ");
		sql.append("\n");
		sql.append("        GROUP BY CA.CAR_CLI_ID ");
		sql.append("\n");
		sql.append("        HAVING COUNT(CA.CAR_CLI_ID) >= "+Util.getPropertiesString("carro.maximo.correos.periodo")+" ");		// para que acepte hasta x correos en el periodo
		sql.append("\n");
		sql.append("    ) ");
		sql.append("\n");
		sql.append("GROUP BY C.CLI_ID, C.CLI_RUT, C.CLI_DV, C.CLI_NOMBRE, C.CLI_APELLIDO_PAT, C.CLI_APELLIDO_MAT, C.CLI_EMAIL ");
		sql.append("\n");
		sql.append("WITH UR ");
		
		return sql;
	}
	
	/**
	 * Método que se encarga de recuperar la query para la categoria con la suma mas alta en costo.
	 * 
	 * @return <code>StringBuffer</code> con la query
	 */
	private StringBuffer getQueryCategoriaMasCara(long cli_id) {
		StringBuffer sql = JFactory.getStringBuffer();
		
		sql.append("SELECT PRCA_CAT_ID, CAT_NOMBRE, SUM(SUMA) SUMA2 ");
		sql.append("\n");
		sql.append("FROM ");
		sql.append("\n");
		sql.append("( ");
		sql.append("\n");
		sql.append("    SELECT CC.CAR_PRO_ID, (INT(CC.CAR_CANTIDAD) * INT(PL.PRE_VALOR)) as SUMA, PA.PRCA_CAT_ID, CA.CAT_NOMBRE ");
		sql.append("\n");
		sql.append("    FROM FODBA.FO_CARRO_COMPRAS CC, FODBA.FO_PRECIOS_LOCALES PL, ");
		sql.append("\n");
		sql.append("         FODBA.FO_PRODUCTOS_CATEGORIAS PA, FODBA.FO_CATEGORIAS CA ");
		sql.append("\n");
		sql.append("    WHERE CC.CAR_PRO_ID = PL.PRE_PRO_ID ");
		sql.append("\n");
		sql.append("    AND PL.PRE_PRO_ID = PA.PRCA_PRO_ID ");
		sql.append("\n");
		sql.append("    AND PA.PRCA_CAT_ID = CA.CAT_ID ");
		sql.append("\n");
		sql.append("    AND PL.PRE_LOC_ID = 1 ");
		sql.append("\n");
		sql.append("    AND CC.CAR_CLI_ID = "+cli_id+" ");
		sql.append("\n");
		sql.append(") P ");
		sql.append("\n");
		sql.append("GROUP BY PRCA_CAT_ID, CAT_NOMBRE ");
		sql.append("\n");
		sql.append("ORDER BY SUMA2 DESC ");
		sql.append("\n");
		sql.append("WITH UR ");
		
		return sql;
	}

	/**
	 * Método que se encarga de recuperar la query para la seccion con la suma mas alta en costo.
	 * 
	 * @return <code>StringBuffer</code> con la query
	 */
	private StringBuffer getQueryRubroMasCaro(long cli_id) {
		StringBuffer sql = JFactory.getStringBuffer();
		
		sql.append("SELECT ID_SECCION, NOMBRE_RUBRO, ID_RUBRO, SUM(SUMA) SUMA2 "); // AGREGAR ID_RUBRO
		sql.append("\n");
		sql.append("FROM ");
		sql.append("\n");
		sql.append("( ");
		sql.append("\n");
		sql.append("    SELECT CC.CAR_PRO_ID, (INT(CC.CAR_CANTIDAD) * INT(PL.PRE_VALOR)) as SUMA, S.ID_SECCION, S.NOMBRE_RUBRO, S.ID_RUBRO ");// SE AGREGA S.ID_RUBRO
		sql.append("\n");
		sql.append("    FROM FODBA.FO_CARRO_COMPRAS CC, FODBA.FO_PRECIOS_LOCALES PL, ");
		sql.append("\n");
		sql.append("         FODBA.FO_PRODUCTOS FP, BODBA.BO_PRODUCTOS BP, BODBA.BO_RUBROXSECCION S "); // rubro por seccion SELECT * FROM BODBA.BO_RUBROXSECCION
		sql.append("\n");
		sql.append("    WHERE CC.CAR_PRO_ID = PL.PRE_PRO_ID ");
		sql.append("\n");
		sql.append("      AND CC.CAR_CLI_ID = "+cli_id+" ");
		sql.append("\n");
		sql.append("      AND PL.PRE_LOC_ID = 1 ");
		sql.append("\n");
		sql.append("      AND CC.CAR_PRO_ID = FP.PRO_ID ");
		sql.append("\n");
		sql.append("      AND FP.PRO_ID_BO = BP.ID_PRODUCTO ");
		sql.append("\n");
		sql.append("      AND S.ID_SECCION = CAST(SUBSTR(BP.ID_CATPROD, 1, 2) AS INT) "); // select * from BO_CATPROD where ID_CATPROD='0317' with ur -- rubro (1,4)
		sql.append("\n");
		sql.append("      AND S.ID_RUBRO = CAST(SUBSTR(BP.ID_CATPROD, 3, 2) AS INT) ");
		sql.append("\n");
		sql.append(") P ");
		sql.append("\n");
		sql.append("GROUP BY ID_SECCION, ID_RUBRO, NOMBRE_RUBRO");
		sql.append("\n");
		sql.append("ORDER BY SUMA2 DESC ");
		sql.append("\n");
		sql.append("WITH UR ");
		
		return sql;
	}
	
	/**
	 * Método que se encarga de recuperar la query para la seccion con la suma mas alta en costo.
	 * 
	 * @return <code>StringBuffer</code> con la query
	 */
	private StringBuffer getQuerySeccionMasCara(long cli_id) {
		StringBuffer sql = JFactory.getStringBuffer();
		
		sql.append("SELECT ID_SECCION, NOMBRE, SUM(SUMA) SUMA2 ");
		sql.append("\n");
		sql.append("FROM ");
		sql.append("\n");
		sql.append("( ");
		sql.append("\n");
		sql.append("    SELECT CC.CAR_PRO_ID, (INT(CC.CAR_CANTIDAD) * INT(PL.PRE_VALOR)) as SUMA, S.ID_SECCION, S.NOMBRE ");
		sql.append("\n");
		sql.append("    FROM FODBA.FO_CARRO_COMPRAS CC, FODBA.FO_PRECIOS_LOCALES PL, ");
		sql.append("\n");
		sql.append("         FODBA.FO_PRODUCTOS FP, BODBA.BO_PRODUCTOS BP, BODBA.BO_SECCION S ");
		sql.append("\n");
		sql.append("    WHERE CC.CAR_PRO_ID = PL.PRE_PRO_ID ");
		sql.append("\n");
		sql.append("      AND CC.CAR_CLI_ID = "+cli_id+" ");
		sql.append("\n");
		sql.append("      AND PL.PRE_LOC_ID = 1 ");
		sql.append("\n");
		sql.append("      AND CC.CAR_PRO_ID = FP.PRO_ID ");
		sql.append("\n");
		sql.append("      AND FP.PRO_ID_BO = BP.ID_PRODUCTO ");
		sql.append("\n");
		sql.append("      AND S.ID_SECCION = CAST(SUBSTR(BP.ID_CATPROD, 1, 2) AS INT) ");
		sql.append("\n");
		sql.append(") P ");
		sql.append("\n");
		sql.append("GROUP BY ID_SECCION, NOMBRE ");
		sql.append("\n");
		sql.append("ORDER BY SUMA2 DESC ");
		sql.append("\n");
		sql.append("WITH UR ");
		
		return sql;
	}
	
	/**
	 * Método que se encarga de recuperar el precio de todos los itemes del carro de compra.
	 * 
	 * @return <code>StringBuffer</code> con la query
	 */
	private StringBuffer getQuerySumaCarroCompra(long cli_id) {
		StringBuffer sql = JFactory.getStringBuffer();

		sql.append("SELECT CC.CAR_PRO_ID, (INT(CC.CAR_CANTIDAD) * INT(PL.PRE_VALOR)) as SUMA ");
		sql.append("\n");
		sql.append("FROM FODBA.FO_CARRO_COMPRAS CC, FODBA.FO_PRECIOS_LOCALES PL ");
		sql.append("\n");
		sql.append("WHERE CC.CAR_PRO_ID = PL.PRE_PRO_ID ");
		sql.append("\n");
		sql.append("AND PL.PRE_LOC_ID = 1 ");
		sql.append("\n");
		sql.append("AND CC.CAR_CLI_ID = "+cli_id+" ");
		sql.append("\n");
		sql.append("WITH UR ");
		
		return sql;
	}

	/**
	 * Método que se encarga de recuperar el cupon de seccion especifica
	 * 
	 * @return <code>StringBuffer</code> con la query
	 */
	private StringBuffer getQueryCuponRubro(int id_seccion , int id_rubro) {
		StringBuffer sql = JFactory.getStringBuffer();
		String cupon_prefijo = Util.getPropertiesString("carro.cupon.prefijo");

		sql.append("SELECT CD.ID_CUP_DTO, CD.CODIGO, CD.CANTIDAD, CD.DESCUENTO, CD.FECHA_FIN ");
		sql.append("\n");
		sql.append("FROM BODBA.BO_CUPON_DSCTO CD, BODBA.BO_CUPON_DSCTOXPRODS DP ");
		sql.append("\n");
		sql.append("WHERE CD.TIPO = 'R' ");  // CAMBIAR POR R - PARA QUE BUSQUE POR RUBRO
		sql.append("\n");
		sql.append("AND CD.ID_CUP_DTO = DP.ID_CUP_DTO ");
		sql.append("\n");
		sql.append("AND CD.CANTIDAD > 0 ");
		sql.append("\n");
		sql.append("AND DP.ID_SECCION = "+id_seccion+" "); 
		sql.append("\n");
		sql.append("AND DP.ID_RUBRO = "+id_rubro+" "); // SE AGREGA  EL RUBRO
		sql.append("\n");
		sql.append("AND ((CD.FECHA_FIN >= CURRENT DATE) AND (CD.FECHA_INI <= CURRENT DATE)) ");
		sql.append("\n");
		sql.append("AND CD.CODIGO LIKE '"+cupon_prefijo+"%' ");
		sql.append("\n");
		sql.append("WITH UR ");
		
		return sql;
	}
	
	private StringBuffer getQueryCuponSeccion(int id_seccion) {
		StringBuffer sql = JFactory.getStringBuffer();
		String cupon_prefijo = Util.getPropertiesString("carro.cupon.prefijo");

		sql.append("SELECT CD.ID_CUP_DTO, CD.CODIGO, CD.CANTIDAD, CD.DESCUENTO, CD.FECHA_FIN ");
		sql.append("\n");
		sql.append("FROM BODBA.BO_CUPON_DSCTO CD, BODBA.BO_CUPON_DSCTOXPRODS DP ");
		sql.append("\n");
		sql.append("WHERE CD.TIPO = 'S' ");
		sql.append("\n");
		sql.append("AND CD.ID_CUP_DTO = DP.ID_CUP_DTO ");
		sql.append("\n");
		sql.append("AND CD.CANTIDAD > 0 ");
		sql.append("\n");
		sql.append("AND DP.ID_SECCION = "+id_seccion+" ");
		sql.append("\n");
		sql.append("AND ((CD.FECHA_FIN >= CURRENT DATE) AND (CD.FECHA_INI <= CURRENT DATE)) ");
		sql.append("\n");
		sql.append("AND CD.CODIGO LIKE '"+cupon_prefijo+"%' ");
		sql.append("\n");
		sql.append("WITH UR ");
		
		return sql;
	}
	
	/**
	 * Método que se encarga de recuperar el cupon de despacho
	 * 
	 * @return <code>StringBuffer</code> con la query
	 */
	private StringBuffer getQueryCuponDespacho() {
		StringBuffer sql = JFactory.getStringBuffer();
		String cupon_prefijo = Util.getPropertiesString("carro.cupon.prefijo");
		
		sql.append("SELECT CD.ID_CUP_DTO, CD.CODIGO, CD.CANTIDAD, CD.DESCUENTO, CD.FECHA_FIN ");
		sql.append("\n");
		sql.append("FROM BODBA.BO_CUPON_DSCTO CD ");
		sql.append("\n");
		sql.append("WHERE CD.TIPO = 'D' ");
		sql.append("\n");
		sql.append("AND CD.CANTIDAD > 0 ");
		sql.append("\n");
		sql.append("AND ((CD.FECHA_FIN >= CURRENT DATE) AND (CD.FECHA_INI <= CURRENT DATE)) ");
		sql.append("\n");
		sql.append("AND CD.CODIGO LIKE '"+cupon_prefijo+"%' ");
		sql.append("\n");
		sql.append("WITH UR ");
		
		return sql;
	}

	/**
	 * Método que se encarga de insertar un mail de carro abandonado a la BD
	 * 
	 * @return <code>StringBuffer</code> con la query
	 */
	private StringBuffer getQueryInsertCarroAbandonado(int cli_id, String car_tipo_cupon, String cli_nombres_mail, String car_fecha_fin, String car_cupon_monto_mail, String car_cupon, String cat_nombre, int estado_mail) {
		StringBuffer sql = JFactory.getStringBuffer();

		sql.append("INSERT INTO FODBA.FO_CARRO_ABANDONADO ");
		sql.append("\n");
		sql.append("	(CAR_CLI_ID, CAR_FEC_MAIL, CAR_TIPO_DCTO, CAR_CLI_NOMBRES, CAR_FECHA_FIN, CAR_MONTO_DCTO, CAR_CUPON, CAR_NOM_SECCION, CAR_ESTADO_MAIL) ");
		sql.append("\n");
		sql.append("VALUES ");
		sql.append("\n");
		sql.append("	( "+cli_id+", CURRENT TIMESTAMP, '"+car_tipo_cupon+"', '"+cli_nombres_mail+"', '"+car_fecha_fin+"', '"+car_cupon_monto_mail+"', '"+car_cupon+"', '"+cat_nombre+"', "+estado_mail+") ");		
		return sql;
	}

	private StringBuffer getQueryUpdateEstadoCarro(int cli_id){
		StringBuffer sql = JFactory.getStringBuffer();
		
		sql.append("UPDATE FODBA.FO_CARRO_COMPRAS SET CAR_EST_MAIL=1 WHERE CAR_CLI_ID="+cli_id+" ");
		return sql;
	}

	private StringBuffer getQueryUpdateEstadoCarroAbandonado(int car_id){
		StringBuffer sql = JFactory.getStringBuffer();
		
		sql.append("UPDATE FODBA.FO_CARRO_ABANDONADO SET CAR_ESTADO_MAIL=1 WHERE CAR_ID="+car_id+" ");
		return sql;
	}
	
}
