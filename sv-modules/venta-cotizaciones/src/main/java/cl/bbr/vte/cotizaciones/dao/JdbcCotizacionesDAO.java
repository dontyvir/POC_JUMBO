package cl.bbr.vte.cotizaciones.dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;


import cl.bbr.vte.cotizaciones.exception.CotizacionesDAOException;
import cl.bbr.vte.cotizaciones.dao.JdbcDAOFactory;
import cl.bbr.vte.cotizaciones.dto.AlertaDTO;
import cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.CategoriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesCriteriaDTO;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.EstadoDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.MarcaDTO;
import cl.bbr.vte.cotizaciones.dto.ModCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.MonitorCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.PedidosCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProcInsDetCotizacionDTO;
import cl.bbr.vte.cotizaciones.dto.ProductoDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO;
import cl.bbr.jumbocl.common.model.CodBarraSapEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.jumbocl.common.model.LocalEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.ProductoSapEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar, ingresar datos en el repositorio.</p>
 * 
 * @author BBR ecommerce & retail
 * 
 */
public class JdbcCotizacionesDAO implements CotizacionesDAO {

	/**
	 * Comentario para <code>logger</code> instancia del log de la aplicación para la clase.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Permite la conexión con la base de datos.
	 */
	Connection 	conn 		= null;

	/**
	 * Permite el manejo de la transaccionalidad, para procesos de multiples operaciones en la base de datos
	 */
	JdbcTransaccion trx		= null;
	

	// ************ Métodos Privados *************** //
	
	/**
	 * Obtiene la conexión
	 * 
	 * @return Connection
	 * @throws SQLException 
	 */
	private Connection getConnection() throws SQLException{
		
		if ( conn == null ){
			conn = JdbcDAOFactory.getConexion();
		}
		
		logger.debug("Conexion usada por el dao: " + conn);
		
		return this.conn;

	}
	
	// ************ Métodos Publicos *************** //
	/**
	 * Libera la conexión. Sólo si no es una conexión única, en cuyo caso
	 * no la cierra.
	 * 
	 * 
	 */
	private void releaseConnection(){
		if ( trx == null ){
            try {
            	if (conn != null){
            		conn.close();
            		conn = null;
            	}
            } catch (SQLException e) {
            	logger.error(e.getMessage(), e);

            }
		}
			
	}
	// ******************************************** //	
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws EmpresasDAOException
	 */
	public void setTrx(JdbcTransaccion trx)	throws CotizacionesDAOException {
		
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new CotizacionesDAOException(e);
		}
	}
	
	/**
	 * Retorna las categorias activas
	 * @param cliente_id
	 * @return
	 * @throws CotizacionesDAOException
	 */

	public List getListCategoria( long cliente_id ) throws CotizacionesDAOException {

		List lista = new ArrayList();
		CategoriaDTO categoria = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {
			conn = this.getConnection();
			String sql = "SELECT fo_categorias.cat_id, fo_catsubcat.cat_id cat_id_padre, cat_nombre, cat_descripcion, " +
						"cat_orden, cat_porc_ranking, cat_banner, cat_tipo " + 
						"FROM fo_categorias left join fo_catsubcat on fo_categorias.cat_id = fo_catsubcat.subcat_id " +
						"where cat_estado = 'A' " +
						"order by cat_orden, cat_nombre ";							
			logger.debug("SQL: " + sql);
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {

				categoria = null;
				categoria = new CategoriaDTO();
				categoria.setId(rs.getLong("cat_id"));
				categoria.setId_padre(rs.getLong("cat_id_padre"));
				categoria.setNombre(rs.getString("cat_nombre"));
				categoria.setTipo(rs.getString("cat_tipo"));
				categoria.setBanner(rs.getString("cat_banner"));
				categoria.setRanking(rs.getLong("cat_porc_ranking"));
				lista.add(categoria);
			}
			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListCategoria - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return lista;
	}

	
	/**
	 * Retorna las cotizaciones de acuerdo a un criterio determinado
	 * @param criterio
	 * @author BBRI
	 * @return lista MonitorCotizacionesDTO
	 * @throws CotizacionesDAOException
	 */
	public List getCotizacionesByCriteria(CotizacionesCriteriaDTO criterio) throws CotizacionesDAOException {
		List lista = new ArrayList();
		MonitorCotizacionesDTO cotizacion = null;
		PreparedStatement stm=null;
		ResultSet rs=null;
		//Revisamos los criterios de búsqueda
		long id_cot      = criterio.getId_cot();
		String rut_emp   = criterio.getRut_emp();
		String razon_soc = criterio.getRazon_social();
		String nom_emp 	 = criterio.getNom_emp();
		String rut_compr = criterio.getRut_comprador();
		long id_emp      = criterio.getId_empresa();
		long id_suc		 = criterio.getId_sucursal();
		long id_local    = criterio.getId_local();
		long id_est      = criterio.getId_estado();
		String tipo_fec  = criterio.getTipo_fec();
		String fec_ini   = criterio.getFec_ini();
		String fec_fin   = criterio.getFec_fin();
		String orden     = criterio.getOrdena_por();
		String nom_suc   = criterio.getNom_sucursal();
		String dir_desp  = criterio.getAlias_direccion();
		long id_comprador= criterio.getId_comprador();
		long tipo_comprador = criterio.getTipo_comprador(); //0:Comprador  1:Administrador
		long id_usuario = criterio.getId_Usuario();
 		String sucursales = criterio.getListSucursales();  //Lista de sucursales a las que tiene acceso el usuario logueado
 		String compradores = criterio.getListCompradores();  //Lista de compradores a los que tiene acceso el usuario logueado
 		
		
		String sqlWhere = " WHERE 1=1 ";
		String sqlOrden = "";
		if (id_cot > 0){
			sqlWhere += " AND cot_id = " + id_cot;
		}
		if (rut_emp != null && !rut_emp.equals("")){
			sqlWhere += " AND emp_rut = " + rut_emp ;
		}
		if (razon_soc != null && !razon_soc.equals("")){
			sqlWhere += " AND ucase(emp_rzsocial) LIKE ucase('%" + razon_soc + "%') ";
		}
		if (nom_emp != null && !nom_emp.equals("")){
			sqlWhere += " AND ucase(emp_nom) LIKE ucase('%" + nom_emp + "%') ";
		}
		if (rut_compr != null && !rut_compr.equals("")){
			sqlWhere += " AND cpr_rut = " + rut_compr ;
		}
		if (id_emp > 0 ){
			sqlWhere += " AND emp_id = " + id_emp;
		}
		if (id_suc > 0){
			sqlWhere += " AND cli_id = " + id_suc;
		}
		if (id_local > 0){
			sqlWhere += " AND id_local = " + id_local;
		}

		if (tipo_comprador == 1){// 1: Administrador
			if ( id_emp > 0 && id_suc > 0 && id_comprador > 0 ){
				sqlWhere += " AND cpr_id = " + id_comprador;
			}
		}else{ // 0: Comprador
			if (id_comprador > 0){
				sqlWhere += " AND cpr_id = " + id_comprador;
			}
		}
		
		if(sucursales != null && !sucursales.equals("") && id_suc == 0) {
	        sqlWhere += " and cli_id in (" + sucursales + " ) ";
		}

		if (compradores != null && !compradores.equals("") && id_comprador == 0){
		    sqlWhere += " and cpr_id in (" + compradores + ") ";
		}
		
		if (id_est > 0){
			sqlWhere += " AND cot_estado = " + id_est;
		}
		if (nom_suc != null && !nom_suc.equals("")){
			sqlWhere += " AND ucase(cli_nombre) LIKE ucase('%" + nom_suc + "%')";
		}
		if (dir_desp != null && !dir_desp.equals("")){
			sqlWhere += " AND ucase(dir_alias) LIKE ucase('%" + dir_desp + "%')";
		}
		logger.debug("tipo de fecha: " + tipo_fec);
		if (tipo_fec != null && !tipo_fec.equals("")){
			if ((tipo_fec.toUpperCase().equals("ING")) && (fec_ini != null)  && (fec_fin!= null)){
				sqlWhere += " AND cot_fing BETWEEN '" + fec_ini +"' AND '"+ fec_fin+"' ";
			}
			if ((tipo_fec.toUpperCase().equals("VENC")) && (fec_ini != null)  && (fec_fin!= null)){
				sqlWhere += " AND cot_fvenc BETWEEN '" + fec_ini +"' AND '" +fec_fin+"' ";
			}
			if ((tipo_fec.toUpperCase().equals("DESP")) && (fec_ini != null)  && (fec_fin!= null)){
				sqlWhere += " AND cot_facordada BETWEEN '" + fec_ini +"' AND '"+ fec_fin + "' ";
			}
		}
		if (orden != null && !orden.equals("")){
			sqlOrden = " ORDER BY " + orden;
		}
		//paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpag();
		logger.debug("pagina:"+pag + " regxpag:"+ regXpag);
		if(pag<=0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;	
		
/**
 * 02-05-2012
 * Query Modificada para obtener Nombre de Local ya que el campo DIR_LOC_ID no existe
 * se reemplaza
 * JOIN FO_DIRECCIONES ON COT_DIR_ID = DIR_ID
 * JOIN BO_LOCALES ON DIR_LOC_ID = ID_LOCAL 
 * por
 * JOIN FODBA.FO_DIRECCIONES       D   ON D.DIR_ID       = CZ.COT_DIR_ID 
 * JOIN BODBA.BO_POLIGONO          P   ON P.ID_POLIGONO  = D.ID_POLIGONO 
 * JOIN BODBA.BO_ZONAS             Z   ON Z.ID_ZONA      = P.ID_ZONA  
 * JOIN BODBA.BO_LOCALES           L   ON L.ID_LOCAL     = Z.ID_LOCAL 
 */		
		try {
			conn = this.getConnection();
			String sql =    " SELECT * FROM ( " 	
                + "     SELECT ROW_NUMBER() OVER(ORDER BY CZ.COT_ID DESC) AS ROW, "
                + "            CZ.COT_ID, E.EMP_RUT, E.EMP_NOM, C.CLI_NOMBRE, C.CLI_APELLIDO_PAT, "
                + "            CZ.COT_FACORDADA, CZ.COT_FVENC, CZ.COT_MONTO_TOT, EST.NOMBRE, CZ.COT_FING, " 
                + "            D.DIR_ALIAS, D.DIR_CALLE, D.DIR_NUMERO, CZ.COT_USER_ID, CZ.COT_USER_FONO_ID, " 
                + "            CZ.COT_ESTADO, CPR.CPR_ID, CPR.CPR_NOMBRES, CPR.CPR_APE_PAT, " 
                + "            SUM(DC.DCOT_QSOLIC) CANT_PROD_SOLIC "
                + "     FROM FODBA.VE_COTIZACION CZ "
                + "          JOIN FODBA.VE_EMPRESA           E   ON E.EMP_ID       = CZ.COT_EMP_ID "
                + "          JOIN FODBA.FO_CLIENTES          C   ON C.CLI_ID       = CZ.COT_CLI_ID "
                + "          JOIN FODBA.VE_COMPRADOR         CPR ON CPR.CPR_ID     = CZ.COT_CPR_ID "
                + "          JOIN FODBA.FO_DIRECCIONES       D   ON D.DIR_ID       = CZ.COT_DIR_ID "
                + "          JOIN BODBA.BO_POLIGONO          P   ON P.ID_POLIGONO  = D.ID_POLIGONO "
                + "          JOIN BODBA.BO_ZONAS             Z   ON Z.ID_ZONA      = P.ID_ZONA " 
                + "          JOIN BODBA.BO_LOCALES           L   ON L.ID_LOCAL     = Z.ID_LOCAL "
                + "          LEFT JOIN FODBA.VE_DETALLE_COTZ DC  ON DC.DCOT_COT_ID = CZ.COT_ID "
                + "          JOIN BODBA.BO_ESTADOS           EST ON EST.ID_ESTADO  = CZ.COT_ESTADO "
                + sqlWhere
                + "     GROUP BY CZ.COT_ID, E.EMP_RUT, E.EMP_NOM, C.CLI_NOMBRE, C.CLI_APELLIDO_PAT, " 
                + "              CZ.COT_FACORDADA, CZ.COT_FVENC, CZ.COT_MONTO_TOT, EST.NOMBRE, CZ.COT_FING, " 
                + "              D.DIR_ALIAS, D.DIR_CALLE, D.DIR_NUMERO, CZ.COT_USER_ID, CZ.COT_USER_FONO_ID, " 
                + "              CZ.COT_ESTADO, CPR.CPR_ID, CPR.CPR_NOMBRES, CPR.CPR_APE_PAT " 
                + sqlOrden  
                + "     ) AS TEMP "
                + "WHERE ROW BETWEEN "+ iniReg +" AND "+ finReg;

            logger.debug("SQL: " + sql);
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();

			while (rs.next()) {
				cotizacion = new MonitorCotizacionesDTO();
				cotizacion.setId_cot(rs.getLong("cot_id"));
				cotizacion.setRut_empresa(rs.getString("emp_rut"));
				cotizacion.setNom_empresa(rs.getString("emp_nom"));
				cotizacion.setSucursal(rs.getString("cli_apellido_pat"));
				cotizacion.setFec_despacho(rs.getString("cot_facordada"));
				cotizacion.setFec_vencimiento(rs.getString("cot_fvenc"));
				cotizacion.setMonto_total(rs.getDouble("cot_monto_tot"));
				cotizacion.setEstado(rs.getString("nombre"));
				cotizacion.setAlias_dir(rs.getString("dir_alias"));
				cotizacion.setCalle(rs.getString("dir_calle"));
				cotizacion.setNumero(rs.getString("dir_numero"));
				cotizacion.setFec_ing(rs.getString("cot_fing"));
				if (rs.getString("cot_user_id") != null){
				    cotizacion.setId_usuario(rs.getLong("cot_user_id"));
				}
				if (rs.getString("cot_user_fono_id") != null){
				    cotizacion.setId_usuario_fono(rs.getLong("cot_user_fono_id"));
				}
				cotizacion.setCot_estado(rs.getLong("cot_estado"));
				cotizacion.setNombre_comprador(rs.getString("cpr_nombres")+" "+rs.getString("cpr_ape_pat"));
				cotizacion.setId_comprador(rs.getLong("cpr_id"));
				cotizacion.setCant_prod(rs.getDouble("cant_prod_solic"));
				cotizacion.setNom_sucursal(rs.getString("cli_nombre"));
				lista.add(cotizacion);

			}

			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCotizacionesByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return lista;
	}

	
	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List de LocalEntity's
	 * @throws CotizacionesDAOException
	 */	
	public List getLocales() throws CotizacionesDAOException {
		List lista_locales = new ArrayList();
		LocalEntity loc = null;
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {
			conn = this.getConnection();
			
			String sql = "SELECT id_local, cod_local, nom_local, orden, fecha_carga_precios "
					   + "FROM bo_locales";
			logger.debug("SQL: " + sql);
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				loc = new LocalEntity();
				loc.setId_local(new Long(rs.getString("id_local")));
				loc.setCod_local(rs.getString("cod_local"));
				loc.setNom_local(rs.getString("nom_local"));
				loc.setOrden(new Integer(rs.getString("orden")));
				loc.setFec_carga_prec(rs.getTimestamp("fecha_carga_precios"));
				lista_locales.add(loc);
			}
			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLocales - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return lista_locales;
	}

	
	/**
	 * Obtiene un listado con los estados de las cotizaciones
	 */
	public List getEstadosCotizacion() throws CotizacionesDAOException {
		List result = new ArrayList();
		PreparedStatement stm =null;
		ResultSet rs = null;
		
		try {

			String cadQuery="SELECT id_estado,nombre, tipo_estado " +
							"FROM bo_estados " +
							"WHERE tipo_estado= '" + Constantes.TIPO_ESTADO_COTIZACION +"' " +
							"ORDER BY id_estado ASC";
			conn = this.getConnection();
			stm = conn.prepareStatement(cadQuery + " WITH UR");
			

			logger.debug("SQL query: " + stm.toString());

			rs = stm.executeQuery();
			while (rs.next()) {
				EstadoDTO est = new EstadoDTO();
				est.setId_estado(rs.getLong("id_estado"));
				est.setNombre(rs.getString("nombre"));
				est.setTipo_estado(rs.getString("tipo_estado"));
				result.add(est);
			}

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEstadosCotizacion - Problema SQL (close)", e);
			}
		}
		logger.debug("ok");
		return result;
	}
	
	
	/**
	 * Obtiene la cantidad de registros de una consulta por criterio
	 */
	public long getCountCotizacionesByCriteria(CotizacionesCriteriaDTO criterio) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		ResultSet rs=null;
		int numCot = 0;
		//Revisamos los criterios de búsqueda
		long id_cot      = criterio.getId_cot();
		String rut_emp   = criterio.getRut_emp();
		String razon_soc = criterio.getRazon_social();
		String nom_emp 	 = criterio.getNom_emp();
		String rut_compr = criterio.getRut_comprador();
		long id_emp      = criterio.getId_empresa();
		long id_suc		 = criterio.getId_sucursal();
		long id_local    = criterio.getId_local();
		long id_est      = criterio.getId_estado();
		String tipo_fec  = criterio.getTipo_fec();
		String fec_ini   = criterio.getFec_ini();
		String fec_fin   = criterio.getFec_fin();
 		
		String sqlWhere = " WHERE 1=1 ";
		if (id_cot > 0){
			sqlWhere += " AND cot_id = " + id_cot;
		}
		if (rut_emp != null && !rut_emp.equals("")){
			sqlWhere += " AND emp_rut = " + rut_emp ;
		}
		if (razon_soc != null && !razon_soc.equals("")){
			sqlWhere += " AND ucase(emp_rzsocial) LIKE ucase('%" + razon_soc + "%') ";
		}
		if (nom_emp != null && !nom_emp.equals("")){
			sqlWhere += " AND ucase(emp_nom) LIKE ucase('%" + nom_emp + "%') ";
		}
		if (rut_compr != null && !rut_compr.equals("")){
			sqlWhere += " AND cpr_rut = " + rut_compr ;
		}
		if (id_emp > 0 ){
			sqlWhere += " AND emp_id = " + id_emp;
		}
		if (id_suc > 0){
			sqlWhere += " AND cli_id = " + id_suc;
		}
		if (id_local > 0){
			sqlWhere += " AND id_local = " + id_local;
		}
		
		if (id_est > 0){
			sqlWhere += " AND cot_estado = " + id_est;
		}
		logger.debug("tipo de fecha: " + tipo_fec);
		if (tipo_fec != null && !tipo_fec.equals("")){
			if ((tipo_fec.toUpperCase().equals("ING")) && (fec_ini != null)  && (fec_fin!= null)){
				sqlWhere += " AND cot_fing BETWEEN '" + fec_ini +"' AND '"+ fec_fin+"' ";
			}
			if ((tipo_fec.toUpperCase().equals("VENC")) && (fec_ini != null)  && (fec_fin!= null)){
				sqlWhere += " AND cot_fvenc BETWEEN '" + fec_ini +"' AND '" +fec_fin+"' ";
			}
			if ((tipo_fec.toUpperCase().equals("DESP")) && (fec_ini != null)  && (fec_fin!= null)){
				sqlWhere += " AND cot_facordada BETWEEN '" + fec_ini +"' AND '"+ fec_fin + "' ";
			}
		}
        /**
         * 02-05-2012
         * Query Modificada para obtener Nombre de Local ya que el campo DIR_LOC_ID no existe
         * se reemplaza
         * JOIN FO_DIRECCIONES ON COT_DIR_ID = DIR_ID
         * JOIN BO_LOCALES ON DIR_LOC_ID = ID_LOCAL 
         * por
         * JOIN FODBA.FO_DIRECCIONES       D   ON D.DIR_ID       = CZ.COT_DIR_ID 
         * JOIN BODBA.BO_POLIGONO          P   ON P.ID_POLIGONO  = D.ID_POLIGONO 
         * JOIN BODBA.BO_ZONAS             Z   ON Z.ID_ZONA      = P.ID_ZONA  
         * JOIN BODBA.BO_LOCALES           L   ON L.ID_LOCAL     = Z.ID_LOCAL 
         */         
		try {
			conn = this.getConnection();
			String sql = "SELECT COUNT(*) CANTIDAD "
                + "FROM FODBA.VE_COTIZACION CZ "
                + "     JOIN FODBA.VE_EMPRESA           E   ON E.EMP_ID       = CZ.COT_EMP_ID "
                + "     JOIN FODBA.FO_CLIENTES          C   ON C.CLI_ID       = CZ.COT_CLI_ID "
                + "     JOIN FODBA.VE_COMPRADOR         CPR ON CPR.CPR_ID     = CZ.COT_CPR_ID "
                + "     JOIN FODBA.FO_DIRECCIONES       D   ON D.DIR_ID       = CZ.COT_DIR_ID "
                + "     JOIN BODBA.BO_POLIGONO          P   ON P.ID_POLIGONO  = D.ID_POLIGONO "
                + "     JOIN BODBA.BO_ZONAS             Z   ON Z.ID_ZONA      = P.ID_ZONA " 
                + "     JOIN BODBA.BO_LOCALES           L   ON L.ID_LOCAL     = Z.ID_LOCAL "
                //+ "     LEFT JOIN FODBA.VE_DETALLE_COTZ DC  ON DC.DCOT_COT_ID = CZ.COT_ID "
                + "     JOIN BODBA.BO_ESTADOS           EST ON EST.ID_ESTADO  = CZ.COT_ESTADO "
                + sqlWhere;

			logger.debug("SQL: " + sql);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			if (rs.next()) {
				numCot = rs.getInt("cantidad");
			}
			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCountCotizacionesByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("getCountCotizacionesByCriteria: ok");
		return numCot;

	}

	/**
	 * Obtiene el detalle de la cotización según su Id
	 */
	public CotizacionesDTO getCotizacionById(long cot_id) throws CotizacionesDAOException {
		CotizacionesDTO cotizacion = null;
		PreparedStatement stm=null;
		ResultSet rs=null;
	
		logger.debug("Cot id: " + cot_id);
        /**
         * 02-05-2012
         * Query Modificada para obtener Nombre de Local ya que el campo DIR_LOC_ID no existe
         * se reemplaza
         * JOIN FO_DIRECCIONES ON COT_DIR_ID = DIR_ID
         * JOIN BO_LOCALES ON DIR_LOC_ID = ID_LOCAL 
         * por
         * JOIN FODBA.FO_DIRECCIONES       D   ON D.DIR_ID       = CZ.COT_DIR_ID 
         * JOIN BODBA.BO_POLIGONO          P   ON P.ID_POLIGONO  = D.ID_POLIGONO 
         * JOIN BODBA.BO_ZONAS             Z   ON Z.ID_ZONA      = P.ID_ZONA  
         * JOIN BODBA.BO_LOCALES           L   ON L.ID_LOCAL     = Z.ID_LOCAL 
         */  
		try {
			conn = this.getConnection();
			String sql =  "SELECT CZ.COT_ID, CZ.COT_EMP_ID, CZ.COT_CLI_ID, E.EMP_RUT, E.EMP_NOM, C.CLI_NOMBRE, CZ.COT_ESTADO, "
                + "       (CPR.CPR_NOMBRES||' '||CPR.CPR_APE_PAT||' '||CPR.CPR_APE_MAT) NOMBRE_COMP, "
                + "       CZ.COT_CPR_ID, CZ.COT_FING, CZ.COT_FACORDADA, CZ.COT_FVENC, CZ.COT_MONTO_TOT, BE.NOMBRE, "
                + "       CZ.COT_COSTO_DESP, CZ.COT_MEDIO_PAGO, CZ.COT_NUM_MP, CZ.COT_FVENC, CZ.COT_OBS, CZ.COT_NOMTBANK, " 
                + "       CZ.COT_NCUOTAS, CZ.COT_FUERAMIX, CZ.COT_TIPO_DOC, CZ.COT_DFAC_ID, CZ.COT_DIR_ID, L.ID_LOCAL, " 
                + "       L.NOM_LOCAL, CZ.COT_SGENTE_TXT, CZ.COT_POL_SUST, CZ.COT_FEXP_MP, CZ.COT_TB_BANCO, E.EMP_RUT, " 
                + "       E.EMP_DV, D.DIR_ALIAS, D.DIR_CALLE, D.DIR_NUMERO, CZ.COT_USER_ID, CZ.COT_USER_FONO_ID, " 
                + "       E.EMP_ESTADO, E.EMP_SALDO, TC.TIP_NOMBRE, DF.DFAC_CALLE, DF.DFAC_NUMERO, DF.DFAC_DEPTO, " 
                + "       DF.DFAC_CIUDAD, BC.NOMBRE AS NOMCOMFAC, BCD.NOMBRE AS NOMCOMDES, CZ.COT_AUT_MARGEN, " 
                + "       CZ.COT_AUT_DSCTO, CZ.COT_POL_ID, CZ.COT_IDJORN_ESP, CPR.CPR_EMAIL COMP_EMAIL "
                + "FROM FODBA.VE_COTIZACION CZ "
                + "     JOIN FODBA.VE_EMPRESA         E   ON E.EMP_ID      = CZ.COT_EMP_ID "
                + "     JOIN FODBA.FO_CLIENTES        C   ON C.CLI_ID      = CZ.COT_CLI_ID "
                + "     JOIN FODBA.VE_COMPRADOR       CPR ON CPR.CPR_ID    = CZ.COT_CPR_ID "
                + "     JOIN FODBA.FO_DIRECCIONES     D   ON D.DIR_ID      = CZ.COT_DIR_ID "
                + "     JOIN BODBA.BO_POLIGONO        P   ON P.ID_POLIGONO = D.ID_POLIGONO "
                + "     JOIN BODBA.BO_ZONAS           Z   ON Z.ID_ZONA     = P.ID_ZONA "
                + "     JOIN BODBA.BO_LOCALES         L   ON L.ID_LOCAL    = Z.ID_LOCAL "
                + "     JOIN BODBA.BO_ESTADOS         BE  ON BE.ID_ESTADO  = CZ.COT_ESTADO "
                + "     LEFT JOIN FODBA.VE_DIRFACT    DF  ON DF.DFAC_ID    = CZ.COT_DFAC_ID "
                + "     LEFT JOIN FODBA.FO_TIPO_CALLE TC  ON TC.TIP_ID     = DF.DFAC_TIP_ID "
                + "     LEFT JOIN BODBA.BO_COMUNAS    BC  ON BC.ID_COMUNA  = DF.DFAC_COM_ID "
                + "     LEFT JOIN BODBA.BO_COMUNAS    BCD ON BCD.ID_COMUNA = D.DIR_COM_ID "
                + "WHERE CZ.COT_ID = " + cot_id;

			logger.debug("SQL (getCotizacionById): " + sql);
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();

			if (rs.next()){
				cotizacion = new CotizacionesDTO();
				cotizacion.setCot_id(rs.getLong("cot_id"));
				cotizacion.setCot_nom_emp(rs.getString("emp_nom"));
				cotizacion.setCot_nom_suc(rs.getString("cli_nombre"));
				cotizacion.setCot_nom_comp(rs.getString("nombre_comp"));
				cotizacion.setCot_fec_ingreso(rs.getString("cot_fing"));
				cotizacion.setCot_fec_acordada(rs.getString("cot_facordada"));
				cotizacion.setCot_fec_vencimiento(rs.getString("cot_fvenc"));
				cotizacion.setCot_estado(rs.getString("nombre"));
				cotizacion.setCot_monto_total(rs.getDouble("cot_monto_tot"));
				cotizacion.setCot_costo_desp(rs.getDouble("cot_costo_desp"));
				cotizacion.setCot_mpago(rs.getString("cot_medio_pago"));
				cotizacion.setCot_num_mpago(rs.getString("cot_num_mp"));
				cotizacion.setCot_obs(rs.getString("cot_obs"));
				cotizacion.setCot_nomtbank(rs.getString("cot_nomtbank"));
				cotizacion.setCot_ncuotas(rs.getInt("cot_ncuotas"));
				cotizacion.setCot_fueramix(rs.getString("cot_fueramix"));
				cotizacion.setCot_cli_id(rs.getLong("cot_cli_id"));				
				cotizacion.setCot_emp_id(rs.getLong("cot_emp_id"));
				cotizacion.setCot_tipo_doc(rs.getString("cot_tipo_doc"));
				cotizacion.setCot_dirfac_id(rs.getLong("cot_dfac_id"));
				cotizacion.setCot_dir_id(rs.getLong("cot_dir_id"));
				cotizacion.setCot_loc_id(rs.getLong("ID_LOCAL"));
				cotizacion.setCot_nom_local(rs.getString("NOM_LOCAL"));
				cotizacion.setCot_rut_emp(rs.getString("emp_rut"));
				cotizacion.setCot_dv_emp(rs.getString("emp_dv"));
				cotizacion.setCot_alias_dir(rs.getString("dir_alias"));
				cotizacion.setCot_calle_dir(rs.getString("dir_calle"));
				cotizacion.setCot_numero_dir(rs.getString("dir_numero"));
				cotizacion.setCot_tipo_nombre(rs.getString("tip_nombre"));
				cotizacion.setCot_dfac_calle(rs.getString("dfac_calle"));
				cotizacion.setCot_dfac_numero(rs.getString("dfac_numero"));
				cotizacion.setCot_dfac_depto(rs.getString("dfac_depto"));
				cotizacion.setCot_dfac_ciudad(rs.getString("dfac_ciudad"));
				if (rs.getString("cot_user_id") != null){
					cotizacion.setCot_id_usuario(rs.getLong("cot_user_id"));
				}
				if (rs.getString("cot_user_fono_id") != null){
					cotizacion.setCot_id_usuario_fono(rs.getLong("cot_user_fono_id"));
				}
				cotizacion.setCot_estado_id(rs.getLong("cot_estado"));
				cotizacion.setCot_comprador_id(rs.getLong("cot_cpr_id"));
				cotizacion.setNomcomunades(rs.getString("nomcomdes"));
				cotizacion.setNomcomunafac(rs.getString("nomcomfac"));
				cotizacion.setCot_aut_margen(rs.getString("cot_aut_margen"));
				cotizacion.setCot_aut_dscto(rs.getString("cot_aut_dscto"));				
				cotizacion.setPersona_auto(rs.getString("cot_sgente_txt"));
				cotizacion.setCot_pol_id(rs.getLong("cot_pol_id"));				
				cotizacion.setSustitucion(rs.getString("cot_pol_sust"));
				cotizacion.setFecha_expira(rs.getString("cot_fexp_mp"));
				cotizacion.setNombre_banco(rs.getString("cot_tb_banco"));
				cotizacion.setCot_emp_estado(rs.getString("emp_estado"));
				cotizacion.setCot_emp_saldo(rs.getDouble("emp_saldo"));
				cotizacion.setCot_id_jor_desp_ref(rs.getLong("cot_idjorn_esp"));
				
			}
			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
		logger.debug("getCotizacionById : ok");
		return cotizacion;		
	}

	/**
	 * Obtiene el Estado de la Empresa de una Cotización
	 */

	public String getEstadoSaldoEmpresaByCotizacion(long cot_id) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		ResultSet rs=null;
		String resp = "";

		logger.debug("Cot id: " + cot_id);
		try {
			conn = this.getConnection();
			String SQL = "";
			SQL = "SELECT E.EMP_ESTADO, E.EMP_SALDO "
                + "FROM FODBA.VE_COTIZACION CZ "
                + "     JOIN FODBA.VE_EMPRESA E ON E.EMP_ID = CZ.COT_EMP_ID "
                + "WHERE CZ.COT_ID = " + cot_id;
			logger.debug("SQL (getEstadoEmpresaByCotizacion): " + SQL);
			stm = conn.prepareStatement(SQL);
			rs = stm.executeQuery();

			if (rs.next()){
				resp = rs.getString("EMP_ESTADO") + "~" + rs.getString("EMP_SALDO");
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (rs != null)  rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("ok");
		return resp;
	}


	/**
	 * Obtiene el Estado de la Empresa de una Cotización
	 */

	public long getDireccionByCotizacion(long cot_id) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		ResultSet rs=null;
		long id_dir = 0L;

		logger.debug("Cot id: " + cot_id);
		try {
			conn = this.getConnection();
			String SQL = "";
			SQL = "SELECT CZ.COT_DIR_ID "
                + "FROM FODBA.VE_COTIZACION CZ "
                + "WHERE CZ.COT_ID = " + cot_id;
			logger.debug("SQL (getDireccionByCotizacion): " + SQL);
			stm = conn.prepareStatement(SQL);
			rs = stm.executeQuery();

			if (rs.next()){
				id_dir = rs.getLong("COT_DIR_ID");
			}
			rs.close();
			stm.close();
			releaseConnection();
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
            	if (rs != null)  rs.close();
                if (stm != null) stm.close();
                releaseConnection();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("ok");
		return id_dir;
	}


	/**
	 * Retorna la cantidad de productos de una cotización de acuerdo a id de cotización.
	 */
	public long getCountProductosEnCotizacionById(long cot_id) throws CotizacionesDAOException {

		PreparedStatement stm=null;
		ResultSet rs=null;
		long numProd = 0;
		logger.debug("Cot id: " + cot_id);
		try {
			conn = this.getConnection();
			String sql =    " SELECT count(*) cantidad " +
							" FROM ve_detalle_cotz " +
							" WHERE dcot_cot_id = " + cot_id ;
			logger.debug("SQL: " + sql);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			
			if (rs.next()) {
				numProd = rs.getInt("cantidad");
			}
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCountProductosEnCotizacionById - Problema SQL (close)", e);
			}
		}

		return numProd;

	}

	
	/**
	 * Retorna una lista con las alertas para cotizaciones
	 */
	public List getAlertasCotizacion(long id_cot) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_aler = new ArrayList();
		
		logger.debug("id cot:" + id_cot);
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();	
			String Sql =" SELECT a.ac_id id, a.ac_nombre nom , a.ac_descr desc, " +
				" a.ac_tipo tipo, a.ac_orden orden, a.ac_activo activo "+
				" FROM ve_cotz_alert a , ve_alertas_ct ac "  +
				" WHERE a.ac_id = ac.ac_id AND ac.cot_id = " + id_cot;
			
			logger.debug("sql:"+Sql);
		

			stm = conn.prepareStatement(Sql + " WITH UR");
			rs = stm.executeQuery();
			
			while (rs.next()) {
				AlertaDTO aler =new AlertaDTO();
				aler.setAle_id(rs.getLong("id"));
				aler.setAle_nom(rs.getString("nom"));
				aler.setAle_descr(rs.getString("desc"));
				aler.setAle_tipo(rs.getString("tipo"));
				aler.setAle_orden(rs.getInt("orden"));
				aler.setAle_activo(rs.getString("activo"));
				list_aler.add(aler);
			}
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getAlertasCotizacion - Problema SQL (close)", e);
			}
		}
		return list_aler;
	}
	
	
	/**
	 * Retorna una lista con los productos asociados a una cotización
	 */
	public List getProductosCotiz(long id_cot) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_prod = new ArrayList();
		
		logger.debug("Parametros getProductosCotiz:");
		logger.debug("numero_cot:"+id_cot);
        /**
         * 02-05-2012
         * Query Modificada para obtener Nombre de Local ya que el campo DIR_LOC_ID no existe
         * se reemplaza
         * JOIN FO_DIRECCIONES ON COT_DIR_ID = DIR_ID
         * JOIN BO_LOCALES ON DIR_LOC_ID = ID_LOCAL 
         * por
         * JOIN FODBA.FO_DIRECCIONES       D   ON D.DIR_ID       = CZ.COT_DIR_ID 
         * JOIN BODBA.BO_POLIGONO          P   ON P.ID_POLIGONO  = D.ID_POLIGONO 
         * JOIN BODBA.BO_ZONAS             Z   ON Z.ID_ZONA      = P.ID_ZONA  
         * LEFT JOIN FODBA.FO_PRECIOS_LOCALES PL ON PL.PRE_PRO_ID = P.PRO_ID AND
         * PL.PRE_LOC_ID = Z.ID_LOCAL
         */  	
		
		try {
		String Sql = "SELECT DC.DCOT_ID, DC.DCOT_PRO_ID_BO, DC.DCOT_PRO_ID, DC.DCOT_COD_PROD1, "
                + "       DC.DCOT_UNI_MED, DC.DCOT_DESCRIPCION, DC.DCOT_QSOLIC, DC.DCOT_PRECIO, "
                + "       DC.DCOT_OBS, DC.DCOT_DSCTO_ITEM, DC.DCOT_PRECIO_LISTA, PL.PRE_COSTO "
                + "FROM FODBA.VE_DETALLE_COTZ DC "
                + "     JOIN FODBA.VE_COTIZACION           C  ON C.COT_ID      = DC.DCOT_COT_ID "
                + "     JOIN FODBA.FO_DIRECCIONES          D  ON D.DIR_ID      = C.COT_DIR_ID "
                + "     JOIN BODBA.BO_POLIGONO             P  ON P.ID_POLIGONO = D.ID_POLIGONO "
                + "     JOIN BODBA.BO_ZONAS                Z  ON Z.ID_ZONA     = P.ID_ZONA "
                + "     JOIN FODBA.FO_PRODUCTOS            P  ON P.PRO_ID_BO   = DC.DCOT_PRO_ID_BO "
                + "     LEFT JOIN FODBA.FO_PRECIOS_LOCALES PL ON PL.PRE_PRO_ID = P.PRO_ID AND "
                + "                                              PL.PRE_LOC_ID = Z.ID_LOCAL "
                + "WHERE DC.DCOT_COT_ID = " + id_cot;
						 			
			logger.debug("SQL :"+Sql);			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				
				ProductosCotizacionesDTO prod = new ProductosCotizacionesDTO();
				prod.setDetcot_id(rs.getLong("dcot_id"));
				prod.setDetcot_proId(rs.getLong("dcot_pro_id_bo"));
				prod.setDetcot_id_fo(rs.getLong("dcot_pro_id"));
				prod.setDetcot_codSap(rs.getString("dcot_cod_prod1"));
				prod.setDetcot_umed(rs.getString("dcot_uni_med"));
				prod.setDetcot_desc(rs.getString("dcot_descripcion"));
				prod.setDetcot_cantidad(rs.getDouble("dcot_qsolic"));
				prod.setDetcot_precio(rs.getDouble("dcot_precio"));
				prod.setDetcot_obs(rs.getString("dcot_obs"));
				prod.setDetcot_dscto_item(rs.getDouble("dcot_dscto_item"));
				prod.setDetcot_precio_lista(rs.getDouble("dcot_precio_lista"));
				prod.setPre_costo(rs.getDouble("pre_costo"));
				list_prod.add(prod);
				}
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductosCotiz - Problema SQL (close)", e);
			}
		}
		return list_prod;
	}

	
	
	public List getPedidosCotiz(long id_cot) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_ped = new ArrayList();
		
		logger.debug("Parametros getPedidosCotiz:");
		logger.debug("numero_cot:"+id_cot);
	
		try {
			
			String Sql = " SELECT distinct p.id_pedido id, p.fcreacion fcrea, j.fecha fdesp, " +
					" l.nom_local local, p.costo_despacho cost_desp, " +
					" dp.cant_faltan cant_falt, e.nombre estado," +
					" LF.nom_local local_fact, p.id_estado id_estado " +
					" FROM bo_pedidos p " +
					" JOIN bo_detalle_pedido dp ON p.id_pedido =  dp.id_pedido " +
					" JOIN bo_locales l ON p.id_local = l.id_local " +
					" JOIN bo_jornada_desp j ON p.id_jdespacho = j.id_jdespacho " +
					" JOIN bo_estados e ON p.id_estado = e.id_estado " +
					" JOIN bo_locales LF ON LF.id_local = p.id_local_fact " +
					" WHERE p.id_cotizacion = " + id_cot +
					" ORDER BY p.id_pedido " ;

			logger.debug("SQL :"+Sql);			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				
				PedidosCotizacionesDTO ped = new PedidosCotizacionesDTO();
				ped.setPed_id(rs.getLong("id"));
				ped.setFec_pedido(rs.getString("fcrea"));
				ped.setFec_despacho(rs.getString("fdesp"));
				ped.setLocal(rs.getString("local"));
				ped.setCosto_desp(rs.getLong("cost_desp"));
				ped.setCant_falt(rs.getDouble("cant_falt"));
				ped.setEstado(rs.getString("estado"));
				ped.setLocal_fact(rs.getString("local_fact"));
				ped.setId_estado(rs.getLong("id_estado"));
				
				list_ped.add(ped);
				}
			
			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPedidosCotiz - Problema SQL (close)", e);
			}
		}
		return list_ped;
	}

	
	/**
	 * Retorna una lista con los logs asociados a una cotización, a partir de su ID.
	 */
	public List getLogCotiz(long id_cot) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_log = new ArrayList();
		
		logger.debug("Parametros getLogCotiz:");
		logger.debug("numero_cot:"+id_cot);
	
		try {
			
			String Sql = " SELECT cot_id, usuario, descripcion, fing " +
					     " FROM ve_log_cotizacion WHERE cot_id =" + id_cot +
					     " ORDER BY fing desc ";

			logger.debug("SQL :"+Sql);			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				
				LogsCotizacionesDTO log = new LogsCotizacionesDTO();
				log.setCot_id(rs.getLong("cot_id"));
				log.setUsuario(rs.getString("usuario"));
				log.setDescripcion(rs.getString("descripcion"));
				log.setFec_ing(rs.getString("fing"));
				
				list_log.add(log);
				}
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLogCotiz - Problema SQL (close)", e);
			}
		}
		return list_log;
	}


/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#doInsCotizacion(cl.bbr.vte.cotizaciones.dto.ProcInsCotizacionDTO)
	 */
	public long doInsCotizacion(ProcInsCotizacionDTO dto) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		long id = -1;
		
		logger.debug("En doInsCotizacion...");
		
		try {
			
			String Sql = " INSERT INTO ve_cotizacion (cot_emp_id, cot_cli_id, cot_dir_id, cot_dfac_id, " +
					" cot_cpr_id, cot_cpr_tipo, cot_fing, cot_facordada, cot_monto_tot, cot_costo_desp, " +
					" cot_medio_pago, cot_num_mp, cot_clave_mp, cot_obs, cot_estado, " +
					" cot_fvenc, cot_nomtbank, cot_ncuotas, cot_fueramix, cot_tipo_doc, cot_user_fono_id) " +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			logger.debug("SQL :"+Sql);			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql,Statement.RETURN_GENERATED_KEYS);
			stm.setLong(1, dto.getId_empresa());
			stm.setLong(2, dto.getId_sucursal());
			stm.setLong(3, dto.getId_dir_desp());
			stm.setLong(4, dto.getId_dir_fac());
			stm.setLong(5, dto.getId_comprador());
			stm.setString(6, dto.getTipo_cpr());
			stm.setString(7, dto.getFec_ingreso());
			stm.setString(8, dto.getFec_acordada());
			stm.setDouble(9, dto.getMonto_total());
			stm.setDouble(10, dto.getCosto_desp());
			stm.setString(11, dto.getMedio_pago());
			stm.setString(12, dto.getNumero_tarjeta());
			stm.setString(13, dto.getMedio_pago_clave());
			stm.setString(14, dto.getObs());
			stm.setInt(15, dto.getEstado());
			stm.setString(16, dto.getFec_vencimiento());
			stm.setString(17, dto.getTbk_nombre_tarjeta());
			stm.setInt(18, dto.getNumero_cuotas());
			stm.setString(19, dto.getFueramix());
			stm.setString(20, dto.getTipo_doc());
			stm.setInt(21, dto.getCot_user_fono_id());
			
			stm.executeUpdate();
			rs = stm.getGeneratedKeys();
			if (rs.next()) {
	            id = rs.getInt(1);
	            logger.debug("id:"+id);
	        }
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : doInsCotizacion - Problema SQL (close)", e);
			}
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#doInsDetCotizacion(cl.bbr.vte.cotizaciones.dto.ProcInsDetCotizacionDTO)
	 */
	public boolean doInsDetCotizacion(ProcInsDetCotizacionDTO dto) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		boolean result = false;
		
		logger.debug("En InsDetCotizacion...");
		
		try {
			
			String Sql = " INSERT INTO ve_detalle_cotz (dcot_cot_id, dcot_pro_id, dcot_pro_id_bo, dcot_cod_prod1, " +
					" dcot_uni_med, dcot_precio, dcot_descripcion, dcot_qsolic, dcot_obs, dcot_preparable, " +
					" dcot_pesable, dcot_con_nota, dcot_dscto_item, dcot_precio_lista) " +
					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			logger.debug("SQL :"+Sql);	
			logger.debug( "DTO:" + dto.toString() );
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			stm.setLong(1, dto.getCot_id());
			stm.setLong(2, dto.getPro_id());
			stm.setLong(3, dto.getPro_id_bo());
			stm.setString(4, dto.getCod_prod1());
			stm.setString(5, dto.getUni_med());
			stm.setDouble(6, dto.getPrecio());
			stm.setString(7, dto.getDescripcion());
			stm.setDouble(8, dto.getQsolic());
			stm.setString(9, dto.getObs());
			stm.setString(10, dto.getPreparable());
			stm.setString(11, dto.getPesable());
			stm.setString(12, dto.getCon_nota());
			stm.setDouble(13, dto.getDscto_item());
			stm.setDouble(14, dto.getPrecio_lista());
						
			int i = stm.executeUpdate();
			if (i>0) {
	            result = true;
	        }
		}catch (SQLException e) {
			e.printStackTrace();
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new CotizacionesDAOException(e);			
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : doInsDetCotizacion - Problema SQL (close)", e);
			}
		}
		logger.debug("result : "+result);
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getLstProductosByCotizacion(long)
	 */
	public List getLstProductosByCotizacion(long id_cotizacion) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List lst_dto = new ArrayList();
		
		logger.debug("En getLstProductosByCotizacion()...");
		logger.debug("id_cotizacion	:"+id_cotizacion);
	
		try {
			
			String Sql = 
				" SELECT dcot_pro_id_bo, dcot_cod_prod1, dcot_uni_med, dcot_descripcion, dcot_qsolic," +
				" P.id_pedido id_pedido, DP.cant_solic cant_pedido, dcot_pro_id, dcot_dscto_item, dcot_precio_lista " +
				" FROM VE_DETALLE_COTZ " +
				"   JOIN bo_pedidos P ON P.id_cotizacion = dcot_cot_id AND P.id_estado <> "+Constantes.ID_ESTAD_PEDIDO_ANULADO +
				"   JOIN bo_detalle_pedido DP ON DP.id_pedido = P.id_pedido AND DP.id_producto = dcot_pro_id_bo " +
				"  WHERE dcot_cot_id = ? " +
				" UNION " +
				" SELECT dcot_pro_id_bo, dcot_cod_prod1, dcot_uni_med, dcot_descripcion, dcot_qsolic, " +
				" 0 as id_pedido, 0 as cant_pedido, dcot_pro_id, dcot_dscto_item, dcot_precio_lista " +
				"  FROM VE_DETALLE_COTZ " +
				"  WHERE dcot_cot_id = ?  ORDER BY dcot_pro_id_bo ASC, id_pedido DESC ";
				/*
				" SELECT dcot_pro_id_bo, dcot_cod_prod1, dcot_uni_med, dcot_descripcion, dcot_qsolic, " +
				" P.id_pedido id_pedido, DP.cant_solic cant_pedido, dcot_pro_id, dcot_dscto_item, dcot_precio_lista " +
				" FROM VE_DETALLE_COTZ " +
				" LEFT JOIN bo_pedidos P ON P.id_cotizacion = dcot_cot_id AND P.id_estado <> "+Constantes.ID_ESTAD_PEDIDO_ANULADO +
				" LEFT JOIN bo_detalle_pedido DP ON DP.id_pedido = P.id_pedido AND DP.id_producto = dcot_pro_id_bo " +
				" WHERE dcot_cot_id = ? " +
				" ORDER BY dcot_pro_id_bo ASC, P.id_pedido ASC ";*/ 

			logger.debug("SQL :"+Sql);			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql + " WITH UR");
			stm.setLong(1, id_cotizacion);
			stm.setLong(2, id_cotizacion);
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosCotizacionesDTO dto = new ProductosCotizacionesDTO();
				dto.setDetcot_proId(rs.getLong("dcot_pro_id_bo"));
				dto.setDetcot_id_fo(rs.getLong("dcot_pro_id"));
				dto.setDetcot_codSap(rs.getString("dcot_cod_prod1"));
				dto.setDetcot_umed(rs.getString("dcot_uni_med"));
				dto.setDetcot_desc(rs.getString("dcot_descripcion"));
				dto.setDetcot_cantidad(rs.getLong("dcot_qsolic"));
				dto.setId_pedido(rs.getString("id_pedido"));
				dto.setCant_pedido(rs.getDouble("cant_pedido"));
				dto.setDetcot_dscto_item(rs.getDouble("dcot_dscto_item"));
				dto.setDetcot_precio_lista(rs.getDouble("dcot_precio_lista"));
				lst_dto.add(dto);
				}
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getLstProductosByCotizacion - Problema SQL (close)", e);
			}
		}
		return lst_dto;
	}

	/**
	 * Permite modificar una cotización
	 */
	public boolean doUpdCotizacion(ModCotizacionDTO cot)
	throws CotizacionesDAOException {		
	PreparedStatement stm=null;
	boolean result=false;
	long fec_venc;
	long fec_desp;
	
	
	String sql_aut ="";
	if (!cot.getAut_margen().equals("")){
		sql_aut +=", cot_aut_margen = '"+cot.getAut_margen()+"' ";
		}
	
	if (!cot.getAut_dscto().equals("")){
		sql_aut +=", cot_aut_dscto = '"+cot.getAut_dscto()+"' ";
		}
	
	String SQLStmt = 
			" UPDATE ve_cotizacion " +
			" SET cot_costo_desp  = ? , " +
			" cot_obs = ? , " +
			" cot_fueramix = ?, " +
			" cot_tipo_doc = ?, " +
			" cot_dfac_id = ?, " +
			" cot_dir_id = ?, " +
			" cot_fvenc = ?, " +
			" cot_idjorn_esp = ?, " +
			" cot_facordada = ? " +
			sql_aut +
			" WHERE cot_id = ?";
	
	logger.debug("Ejecución DAO doUpdCotizacion");
	logger.debug("SQL: " 			+ SQLStmt);
	logger.debug("id_cot: " 		+ cot.getId_cot());
	logger.debug("obs: " 	        + cot.getObs());
	logger.debug("fueramix: " 	    + cot.getFueraMix());
	logger.debug("cot_tipo_doc: " 	+ cot.getTipo_doc());
	logger.debug("cot_dfac_id: " 	+ cot.getId_dirfact());
	logger.debug("costo_despacho: " + cot.getCosto_despacho());
	logger.debug("direc_despacho: " + cot.getId_dirdesp());
	logger.debug("Fecha_venc: " 	+ cot.getFecha_venc()+ 
			" ano:"+cot.getFecha_venc().substring(6,10) +
			" mes:"+cot.getFecha_venc().substring(3,5)+
			" dia:"+cot.getFecha_venc().substring(0,2));
	logger.debug("Aut margen: " 	+ cot.getAut_margen());
	logger.debug("Aut dscto: " 		+ cot.getAut_dscto());
	
	try {

		//con = JdbcDAOFactory.getConexion();
		conn = this.getConnection();
		stm = conn.prepareStatement( SQLStmt );
		
		stm.setDouble(1, cot.getCosto_despacho());
		stm.setString(2, cot.getObs());
		stm.setString(3, cot.getFueraMix());
		stm.setString(4, cot.getTipo_doc());
		if (cot.getId_dirfact() != -1){
			stm.setLong(5, cot.getId_dirfact());
		}else{
			stm.setNull(5, Types.INTEGER);
		}
		stm.setLong(6, cot.getId_dirdesp());
		//fech vencimiento
		int ano = Integer.parseInt(cot.getFecha_venc().substring(6,10)); //ano
		int mes = Integer.parseInt(cot.getFecha_venc().substring(3,5)) - 1; //mes
		int dia = Integer.parseInt(cot.getFecha_venc().substring(0,2)); //dia
		//		 Para fecha de vencimiento
		Calendar cal = new GregorianCalendar(ano, mes, dia);
		fec_venc = cal.getTimeInMillis();

		stm.setTimestamp(7, new Timestamp(fec_venc));
		
		stm.setLong(8,cot.getId_jorn_ref());
//		fech depacho
		logger.debug("Fecha desp:"+cot.getFec_desp());
		int ano_d = Integer.parseInt(cot.getFec_desp().substring(6,10)); //ano
		int mes_d = Integer.parseInt(cot.getFec_desp().substring(3,5)) - 1; //mes
		int dia_d = Integer.parseInt(cot.getFec_desp().substring(0,2)); //dia
		int hora_d = Integer.parseInt(cot.getFec_desp().substring(11,13)); //dia
		logger.debug("hora " +  hora_d);
		int min_d = Integer.parseInt(cot.getFec_desp().substring(14,16)); //dia
		logger.debug("minuto " +  min_d);
		int seg_d = Integer.parseInt(cot.getFec_desp().substring(17,19)); //dia
		logger.debug("segundo " +  seg_d);
		//		 Para fecha de despacho
		Calendar cal_d = new GregorianCalendar(ano_d, mes_d, dia_d,hora_d,min_d,seg_d);
		fec_desp = cal_d.getTimeInMillis();
		stm.setTimestamp(9,new Timestamp(fec_desp));
		stm.setLong(10, cot.getId_cot());

		int i = stm.executeUpdate();
		logger.debug("Resultado Ejecución: " + i);
		if(i>0){
			result= true;
		}

	}catch (SQLException e) {
		throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
	} finally {
		try {
			if (stm != null)
				stm.close();
				releaseConnection();
		} catch (SQLException e) {
			logger.error("[Metodo] : doUpdCotizacion - Problema SQL (close)", e);
		}
	}	
	logger.debug("Resultado: "+result);
	return result;
	}
	
	
	/**
	 * Agrega un registro al log de cotizaciones
	 */
	public boolean addLogCotizacion(LogsCotizacionesDTO log) throws CotizacionesDAOException {  
		PreparedStatement stm	= null;
		boolean result=false;
		try{
			
			String SQLStmt = 
				" INSERT INTO ve_log_cotizacion (cot_id, usuario, descripcion, fing) " +
				" VALUES (?, ?, ?, ?) ";

			logger.debug("Ejecución DAO addLogCotizacion");
			logger.debug("SQL: " + SQLStmt);
			logger.debug("id_cot:" + log.getCot_id());
			logger.debug("usuario: " + log.getUsuario());
			logger.debug("desc: " + log.getDescripcion());
			logger.debug("fing: " + new Date(System.currentTimeMillis()));
			
			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt );
			stm.setLong(1, log.getCot_id());
			stm.setString(2, log.getUsuario());
			stm.setString(3, log.getDescripcion());
			stm.setDate(4, new Date(System.currentTimeMillis()));


			int i = stm.executeUpdate();
			logger.debug("Resultado Ejecución: " + i);
			if(i>0){
				result= true;
			}
	
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addLogCotizacion - Problema SQL (close)", e);
			}
		}
		logger.debug("addLogCotizacion : fin log");
		return result;
	}

	
	
	/**
	 * Cambia el estado a una cotización
	 * @param id_cot
	 * @param id_estado
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean setModEstadoCotizacion(long id_cot, long id_estado) throws CotizacionesDAOException {
		
		PreparedStatement stm=null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en setModEstadoCotizacion");
			String sql = "UPDATE  ve_cotizacion SET cot_estado = ? WHERE cot_id = ? ";
			logger.debug(sql);
			logger.debug("id_cot:"+id_cot);
			logger.debug("id_estado:"+id_estado);
			
			stm = conn.prepareStatement(sql);

			stm.setLong(1, id_estado);
			stm.setLong(2, id_cot);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setModEstadoCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	public boolean addProductoCotizacion(ProductosCotizacionesDTO prod) throws CotizacionesDAOException {
		
		PreparedStatement 	stm 	= null;
		boolean 			result 	= false;
		
		String sql =
			"INSERT INTO ve_detalle_cotz (dcot_cot_id, dcot_pro_id, dcot_pro_id_bo, " +
			" dcot_cod_prod1, dcot_uni_med, dcot_precio, dcot_descripcion, dcot_qsolic, " +
			" dcot_obs, dcot_preparable, dcot_pesable, dcot_con_nota, dcot_dscto_item, " +
			" dcot_precio_lista )"+
			" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		
		logger.debug("en addProductoCotizacion");
		logger.debug("SQL: " + sql);
		logger.debug("vals"+prod.getDetcot_cot_id()+","+ prod.getDetcot_proId()+","+prod.getDetcot_pro_id_bo()+","+
				prod.getDetcot_codSap()+","+prod.getDetcot_umed()+","+prod.getDetcot_precio()+","+
				prod.getDetcot_desc()+","+prod.getDetcot_cantidad()+","+prod.getDetcot_obs()+","+
				prod.getDetcot_preparable()+","+prod.getDetcot_pesable()+","+prod.getDetcot_con_nota()+","+
				prod.getDetcot_dscto_item()+","+prod.getDetcot_precio_lista());
	
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, prod.getDetcot_cot_id());
			stm.setLong(2, prod.getDetcot_proId());
			stm.setLong(3, prod.getDetcot_pro_id_bo());
			stm.setString(4, prod.getDetcot_codSap());
			stm.setString(5, prod.getDetcot_umed());
			stm.setDouble(6, prod.getDetcot_precio());
			stm.setString(7, prod.getDetcot_desc());
			stm.setDouble(8, prod.getDetcot_cantidad());
			stm.setString(9, prod.getDetcot_obs());
			stm.setString(10, prod.getDetcot_preparable());
			stm.setString(11, prod.getDetcot_pesable());
			stm.setString(12, prod.getDetcot_con_nota());
			stm.setDouble(13, prod.getDetcot_dscto_item());
			stm.setDouble(14, prod.getDetcot_precio_lista());
			
			
		
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addProductoCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/**
	 * Permite eliminar un producto de una cotización
	 */
	public boolean delProductoCotizacion(long detcot_id) throws CotizacionesDAOException {
		PreparedStatement 	stm 	= null;
		boolean 			result 	= false;
		
		String sql =
			" DELETE FROM ve_detalle_cotz " +
			" WHERE dcot_id = ? ";
		
		logger.debug("en delProductoCotizacion");
		logger.debug("SQL: " + sql + " dcot_id "+ detcot_id );
		
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, detcot_id);
	
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : delProductoCotizacion - Problema SQL (close)", e);
			}
		}
		return result;

	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#CaducarCotizaciones()
	 */
	public boolean CaducarCotizaciones( long id_comprador ) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en CaducarCotizaciones");
			String sql = " UPDATE ve_cotizacion" +
					" SET cot_estado = " + Constantes.ID_EST_COTIZACION_CADUCADA +
					" WHERE cot_id in ( " +
					" select distinct cot_id " +
					" from ve_comprxsuc as cosu " +
					" join fo_clientes as suc on cosu.cli_id = suc.cli_id " +
					" join ve_cotizacion as cot on cot.cot_emp_id = suc.cli_emp_id " +
					" where cpr_id = ? " +
					" and cot_fvenc < CURRENT TIMESTAMP ) ";
			logger.debug(sql);
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, id_comprador );
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : CaducarCotizaciones - Problema SQL (close)", e);
			}
		}
		return result;
		
	}

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#updProductoCotizacion(double, double)
	 */
	public boolean updProductoCotizacion(ModProdCotizacionesDTO prod) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en updProductoCotizacion");
			String sql = " UPDATE  ve_detalle_cotz " +
					     " SET dcot_qsolic = ?," +
					     " dcot_dscto_item = ?," +
					     " dcot_precio = ? " +
					     " WHERE dcot_id = ? ";
			logger.debug(sql);
			logger.debug("id_cot:"+prod.getDetcot_id());
			logger.debug("cantidad:"+prod.getDetcot_cantidad());
			logger.debug("descuento:"+prod.getDetcot_dscto_item());
			logger.debug("precio:"+prod.getDetcot_precio());
			stm = conn.prepareStatement(sql);

			stm.setDouble(1, prod.getDetcot_cantidad());
			stm.setDouble(2, prod.getDetcot_dscto_item());
			stm.setDouble(3, prod.getDetcot_precio());
			stm.setLong(4, prod.getDetcot_id());
			

			int i = stm.executeUpdate();
			if(i>0)
				result = true;
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updProductoCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#addAlertaCotizacion(long, int)
	 */
	public boolean addAlertaCotizacion(long id_cotizacion, int id_alerta) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		
		try {
			conn = this.getConnection();
			logger.debug("en addAlertaCotizacion...");
			String sql = " INSERT INTO VE_ALERTAS_CT (cot_id, ac_id) " +
					     " VALUES (?,?) ";
			logger.debug(sql);
			logger.debug("id_cotizacion:"+id_cotizacion);
			logger.debug("id_alerta:"+id_alerta);
			stm = conn.prepareStatement(sql);

			stm.setLong(1, id_cotizacion);
			stm.setLong(2, id_alerta);
			

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : addAlertaCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#elimAlertaByCotizacion(long)
	 */
	public boolean elimAlertaByCotizacion(long id_cotizacion)  throws CotizacionesDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		
		try {
			conn = this.getConnection();
			logger.debug("en elimAlertaByCotizacion");
			String sql = " DELETE FROM ve_alertas_ct " +
					     " WHERE cot_id = ? ";
			logger.debug(sql);
			logger.debug("id_cotizacion:"+id_cotizacion);
			stm = conn.prepareStatement(sql);

			stm.setLong(1, id_cotizacion);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : elimAlertaByCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getEmpresaById(long)
	 */
	public EmpresasEntity getEmpresaById(long id_empresa) throws CotizacionesDAOException {
		EmpresasEntity ent = null;
		
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {

			logger.debug("en getEmpresaById...");
			conn = this.getConnection();
			String sql = 
				" SELECT emp_id, emp_rut, emp_dv, emp_nom, emp_descr, emp_rzsocial, emp_rubro, emp_tamano, emp_qtyemp, " +
				" emp_nom_contacto, emp_fono1_contacto, emp_fono2_contacto, emp_fono3_contacto, emp_mail_contacto, emp_cargo_contacto, " +
				" emp_saldo, emp_fact_saldo,  emp_fmod, emp_estado, emp_mg_min, emp_fec_crea, " +
				" emp_dscto_max, emp_mod_rzsoc " +
				" FROM ve_empresa WHERE emp_id = ? ";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_empresa);
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				ent = new EmpresasEntity();
				ent.setId(new Long(rs.getLong("emp_id")));
				ent.setRut(new Long(rs.getLong("emp_rut")));
				ent.setDv(new Character(rs.getString("emp_dv").charAt(0)));
				ent.setNombre(rs.getString("emp_nom"));
				ent.setDescripcion(rs.getString("emp_descr"));
				ent.setRsocial(rs.getString("emp_rzsocial"));
				ent.setRubro(rs.getString("emp_rubro"));
				ent.setTamano(rs.getString("emp_tamano"));
				ent.setQtyemp(new Integer(rs.getInt("emp_qtyemp")));
				ent.setNom_contacto(rs.getString("emp_nom_contacto"));
				ent.setFono1_contacto(rs.getString("emp_fono1_contacto"));
				ent.setFono2_contacto(rs.getString("emp_fono2_contacto"));
				ent.setFono3_contacto(rs.getString("emp_fono3_contacto"));
				ent.setMail_contacto(rs.getString("emp_mail_contacto"));
				ent.setCargo_contacto(rs.getString("emp_cargo_contacto"));
				ent.setSaldo(new Double(rs.getDouble("emp_saldo")));
				ent.setFact_saldo(rs.getTimestamp("emp_fact_saldo"));
				ent.setFmod(rs.getTimestamp("emp_fmod"));
				ent.setEstado(rs.getString("emp_estado"));
				ent.setMrg_minimo(new Double(rs.getDouble("emp_mg_min")));
				ent.setFec_crea(rs.getTimestamp("emp_fec_crea"));
				ent.setDscto_max(new Double(rs.getDouble("emp_dscto_max")));
				ent.setMod_rzsoc(new Integer(rs.getInt("emp_mod_rzsoc")));
			}

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEmpresaById - Problema SQL (close)", e);
			}
		}
		logger.debug("getEmpresaById : ok");
		return ent;
	}


	public DireccionEntity getDireccionDespById(long id_dir_desp)  throws CotizacionesDAOException {
		DireccionEntity dir = null;
		PreparedStatement stm=null;
		ResultSet rs=null;
        String fnueva = "";
		try {

			conn = this.getConnection();
			String sql = "SELECT D.DIR_ID, D.DIR_FNUEVA FROM FODBA.FO_DIRECCIONES D WHERE D.DIR_ID = ? ";
			stm = conn.prepareStatement(sql + " With UR");
			stm.setLong(1,id_dir_desp);
			logger.debug("SQL (getDireccionDespById): " + sql);
			
			rs = stm.executeQuery();
			if (rs.next()) {
                dir = new DireccionEntity();
                dir.setFnueva(rs.getString("DIR_FNUEVA"));
			}
             
		}catch (SQLException e) {
			throw new  CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getDireccionDespById - Problema SQL (close)", e);
			}
		}
         return dir;

	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getSumaCostosByCotizacion(cl.bbr.vte.cotizaciones.dto.CotizacionesDTO)
	 */
	public double getSumaCostosByCotizacion(CotizacionesDTO dto) throws CotizacionesDAOException {
		double result = 0; 
		PreparedStatement stm=null;
		ResultSet rs=null;
		
		try {
			logger.debug("en getSumaCostosByCotizacion...");
			conn = this.getConnection();
			String sql = " SELECT dcot_pro_id, dcot_qsolic * pre_costo subtotal " +
					" FROM   ve_detalle_cotz " +
					" LEFT JOIN fo_precios_locales ON pre_pro_id = dcot_pro_id " +
					" WHERE  dcot_cot_id = ? and pre_loc_id=? ";

			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,dto.getCot_id());
			stm.setLong(2,dto.getCot_loc_id());
			logger.debug("SQL query: " + sql);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				//realiza una sumatoria de costos de cada prod * cantidad solicitada en la cotizacion
				result += rs.getDouble("subtotal");
			}

		}catch (SQLException e) {
			throw new  CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSumaCostosByCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getExisteAlertaActiva(long)
	 */
	public boolean getExisteAlertaActiva(long id_cotizacion) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		ResultSet rs =null;
		boolean result = false;
		
		try {
			conn = this.getConnection();
			logger.debug("En getExisteAlertaActiva...");
			String sql = 
				" SELECT cot_id " +
				" FROM   ve_cotz_alert A, ve_alertas_ct CA " +
				" WHERE  CA.ac_id = A.ac_id AND A.ac_tipo = '"+Constantes.ESTADO_ACTIVADO+"' AND CA.cot_id = ? "; 
			logger.debug(sql);
			logger.debug("id_cotizacion:"+id_cotizacion);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_cotizacion);
			
			rs = stm.executeQuery();
			if (rs.next()) {
				result = true;
			}

		}catch (SQLException e) {
			throw new  CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getExisteAlertaActiva - Problema SQL (close)", e);
			}
		}
	 return result;
			
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#setModEmpresaById(cl.bbr.jumbocl.common.model.EmpresasEntity)
	 */
	public boolean setModEmpresaById(EmpresasEntity emp) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		
		try {
			conn = this.getConnection();
			logger.debug("en setModEmpresaById...");
			String sql = " UPDATE ve_empresa SET emp_mod_rzsoc = ? " +
					     " WHERE emp_id = ? ";
			logger.debug(sql);
			logger.debug("id_empresa:"+emp.getId().longValue());
			stm = conn.prepareStatement(sql);

			stm.setInt(1, emp.getMod_rzsoc().intValue());
			stm.setLong(2, emp.getId().longValue());

			int i = stm.executeUpdate();
			if(i>0)
				result = true;
			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setModEmpresaById - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	
	/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getCategoriasList(long)
	 */
	public List getCategoriasList() throws CotizacionesDAOException {

		List lista = new ArrayList();
		CategoriaDTO categoria = null;
		PreparedStatement stm=null;
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			
			logger.debug("en getCategoriasList...");
			
			String sql = "SELECT fo_categorias.cat_id, fo_catsubcat.cat_id cat_id_padre, cat_nombre, cat_descripcion, "
					+ "cat_orden, cat_porc_ranking, cat_banner, cat_tipo "
					+ "FROM fo_categorias left join fo_catsubcat on fo_categorias.cat_id = fo_catsubcat.subcat_id "
					+ "where cat_estado = 'A' "
					+ "order by cat_orden, cat_nombre ";

			stm = conn.prepareStatement(sql + " WITH UR");							
			logger.debug("SQL: " + sql );
			rs = stm.executeQuery();
			while (rs.next()) {
				categoria = null;
				categoria = new CategoriaDTO();
				categoria.setId(rs.getLong("cat_id"));
				categoria.setId_padre(rs.getLong("cat_id_padre"));
				categoria.setNombre(rs.getString("cat_nombre"));
				categoria.setTipo(rs.getString("cat_tipo"));
				categoria.setBanner(rs.getString("cat_banner"));
				if( rs.getString("cat_porc_ranking") != null )
					categoria.setRanking(rs.getLong("cat_porc_ranking"));
				else
					categoria.setRanking(0);
				lista.add(categoria);
			}
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCategoriasList - Problema SQL (close)", e);
			}
		}
		return lista;
	}	

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#setAsignaCotizacion(cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO)
	 */
	public boolean setAsignaCotizacion(AsignaCotizacionDTO col) throws CotizacionesDAOException {
		boolean result = false;
		PreparedStatement stm0=null;
		PreparedStatement stm=null;
		PreparedStatement stm2=null;
		ResultSet rs =null;
		
		long id_cot_usuario = -1L;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			//debe consultar si el usuario ya tiene una op tomada
			String sql0 = "SELECT COALESCE(id_cotizacion,0) cotizacion " +
					" FROM BO_USUARIOS " +
					" WHERE id_usuario = ? ";
			
			logger.debug("Sql0:"+sql0);
			logger.debug("id_usuario:"+col.getId_usuario());
			
			stm0 = conn.prepareStatement(sql0 + " WITH UR");
			stm0.setLong(1, col.getId_usuario());
			
			rs = stm0.executeQuery();
			
			if(rs.next()){
				id_cot_usuario = rs.getLong("cotizacion");
			}
			if (id_cot_usuario>0){
				logger.debug("El usuario ya tiene tomada la Cotización:"+id_cot_usuario+" solo puede tomar una Cotización a la vez");
				return false;
			}
			
			// si no la tiene se puede asignar
			
			logger.debug("en setAsignaCotizacion");
			String sql = "UPDATE VE_COTIZACION SET cot_user_id = ? " +
						 " WHERE cot_id = ?";
			logger.debug(sql);
			logger.debug("id_usuario:"+col.getId_usuario());
			logger.debug("id_cotizacion:"+col.getId_cotizacion());
			
			stm = conn.prepareStatement(sql);
			stm.setLong(1, col.getId_usuario());
			stm.setLong(2, col.getId_cotizacion());

			int i = stm.executeUpdate();
			if(i<=0) { //nro lineas actualizadas 0 o menos
				return false;
			}
			String sql2 = "UPDATE BO_USUARIOS SET id_cotizacion = ? WHERE id_usuario = ? ";
			
			logger.debug(sql2);
			logger.debug("id_usuario:"+col.getId_usuario());
			logger.debug("id_cotizacion:"+col.getId_cotizacion());
			
			stm2 = conn.prepareStatement(sql2);
			stm2.setLong(1, col.getId_cotizacion());
			stm2.setLong(2, col.getId_usuario());
			
			int j = stm2.executeUpdate();
			if (j>0)  result = true;
			else	  result = false;

			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
                if (stm0 != null) 
                	stm0.close();
                if (stm != null) 
                	stm.close();
                if (stm2 != null) 
                	stm2.close();

				if (rs != null)
					rs.close();

				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setAsignaCotizacion - Problema SQL (close)", e);
			}
    	}
		return result;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getCategoriaById(long)
	 */
	public CategoriaDTO getCategoriaById(long id_categoria) throws CotizacionesDAOException {

		CategoriaDTO categoria = new CategoriaDTO();
		PreparedStatement stm=null;
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			
			logger.debug("en getCategoriaById...");
			
			String sql = "SELECT fo_categorias.cat_id, fo_catsubcat.cat_id cat_id_padre, cat_nombre, cat_descripcion, " +
							"cat_orden, cat_porc_ranking, cat_banner, cat_totem, cat_tipo " + 
							"FROM fo_categorias left join fo_catsubcat on fo_categorias.cat_id = fo_catsubcat.subcat_id " +
							"where fo_categorias.cat_id = ? ";
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_categoria );
			logger.debug("SQL: " + sql );
			logger.debug("id_categoria: " + id_categoria);
			rs = stm.executeQuery();
			while (rs.next()) {
				categoria.setId(rs.getLong("cat_id"));
				categoria.setId_padre(rs.getLong("cat_id_padre"));
				categoria.setNombre(rs.getString("cat_nombre"));
				categoria.setTipo(rs.getString("cat_tipo"));
				categoria.setBanner(rs.getString("cat_banner"));
				categoria.setRanking(rs.getLong("cat_porc_ranking"));
			}
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCategoriaById - Problema SQL (close)", e);
			}
		}
		return categoria;	
	
	
	}

	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#setLiberaCotizacion(cl.bbr.vte.cotizaciones.dto.AsignaCotizacionDTO)
	 */
	public boolean setLiberaCotizacion(AsignaCotizacionDTO col) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		PreparedStatement stm2 =null;
		boolean result = false;
		
		try {
			conn = this.getConnection();
			logger.debug("en setLiberaCotizacion");
			String sql = "UPDATE VE_COTIZACION SET cot_user_id = NULL " +
						 " WHERE cot_id = ? ";
			logger.debug(sql);
			
			logger.debug("id_cot:"+col.getId_cotizacion());
			
			stm = conn.prepareStatement(sql);

			stm.setLong(1, col.getId_cotizacion());
		
			int i = stm.executeUpdate();
			if(i<=0) {	
				return false;
			}
			String sql2 = "UPDATE BO_USUARIOS SET id_cotizacion = NULL WHERE id_usuario = ? ";
			stm2 = conn.prepareStatement(sql2);
			stm2.setLong(1, col.getId_usuario());
			int j = stm2.executeUpdate();
			if (j>0)  result = true;
			else	  result = false;
			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		}finally {
            try {
                if (stm != null) 
                	stm.close();
                if (stm2 != null) 
                	stm2.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setLiberaCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}

	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getTotalProductosCot(long)
	 */
	public double getTotalProductosCot(long id_cot) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		ResultSet rs=null;
		double total = 0;
		logger.debug("Cot id: " + id_cot);
		try {
			conn = this.getConnection();
			String sql =    " SELECT sum(dcot_qsolic) cantidad " +
							" FROM ve_detalle_cotz " +
							" WHERE dcot_cot_id = " + id_cot ;
			logger.debug("SQL: " + sql);
			
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			
			if (rs.next()) {
				total = rs.getInt("cantidad");
			}
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getTotalProductosCot - Problema SQL (close)", e);
			}
		}

		return total;
	}
	
	
	

	/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getListMarcasByCategoria(long)
	 */
	public List getListMarcasByCategoria(long categoria_id) throws CotizacionesDAOException {
		List lista = new ArrayList();
		MarcaDTO marca = new MarcaDTO();
		PreparedStatement stm=null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			logger.debug("en getCategoriasList...");
			String sql ="SELECT distinct mar_id, mar_nombre "+
						"FROM fo_productos inner join fo_productos_categorias on pro_id = prca_pro_id "+
						"inner join fo_categorias on cat_id = prca_cat_id "+
						"inner join fo_marcas on mar_id = pro_mar_id "+
						"where pro_estado = 'A' "+
						"and cat_estado = 'A' "+
						"and prca_estado = 'A' "+
						"and mar_estado = 'A' "+
						"and cat_id = ? "+
						"order by mar_nombre";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, categoria_id );
			logger.debug("SQL: " + sql );
			logger.debug("categoria_id:"+categoria_id);
			rs = stm.executeQuery();
			
			while (rs.next()) {
				marca = null;
				marca = new MarcaDTO();
				marca.setMar_id(rs.getLong("mar_id"));
				marca.setMar_nombre(rs.getString("mar_nombre"));
				lista.add(marca);
			}
			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListMarcasByCategoria - Problema SQL (close)", e);
			}
		}
		return lista;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getListProductosByCriteria(cl.bbr.vte.cotizaciones.dto.ProductosCriteriaDTO)
	 */
	public List getListProductosByCriteria(ProductosCriteriaDTO criterio) throws CotizacionesDAOException {

		List lista = new ArrayList();
		PreparedStatement stm=null;
		String filtro_patronaux = " ";
		ResultSet rs = null;
		try {

			conn = this.getConnection();
			
			logger.debug("en getCategoriasList...");		

			logger.debug("id_categoria:"+criterio.getId_categoria());
			logger.debug("id_local:"+criterio.getId_local());
			logger.debug("id_marca:"+criterio.getId_marca());
			logger.debug("ordenamiento:"+criterio.getOrdenamiento());
			logger.debug("id_producto:"+criterio.getId_producto());
			
			String filtro_orden = " ";
			if (criterio.getOrdenamiento() == 0)
				filtro_orden = " order by pre_valor desc ";
			else if (criterio.getOrdenamiento() == 1)
				filtro_orden = " order by pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
			else if (criterio.getOrdenamiento() == 3)
				filtro_orden = " order by ppum asc ";
			
			String filtro_marca = " ";
			if( criterio.getId_marca() != 0 )
				filtro_marca = " and mar_id="+criterio.getId_marca()+" ";			
			
			String filtro_id_producto = " ";
			if( criterio.getId_producto() != 0 ) {
				filtro_id_producto = " and pro_id = " + criterio.getId_producto();
			}
			
			String filtro_categoria = " ";
			if( criterio.getId_categoria() != 0 )
				filtro_categoria = " and prca_cat_id = " + criterio.getId_categoria();
			
			String filtro_local = " ";
			if( criterio.getId_local() != 0 )
				filtro_local = " and pre_loc_id = " + criterio.getId_local();
			
			String filtro_cot = " ";
			filtro_cot = " and dcot_cot_id = " + criterio.getId_cotizacion();
			
			if (criterio.getPatrones() != null ){
				filtro_patronaux = " and ( " + Formatos.formatPatron(criterio.getPatrones())+ " ) "; 
			}
			
			
			String sql = 	"SELECT DISTINCT pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, pro_tipo_producto, pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, pro_fcrea, pro_fmod, pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, pro_id_bo, pro_tipo_sel, " +
							" mar_nombre, mar_id, " +
							" pre_valor, pre_stock, pre_estado, "+
							" uni_desc, uni_cantidad, uni_estado," +
							" dcot_id, dcot_qsolic, dcot_obs " +
							" FROM fo_productos join fo_productos_categorias on pro_id = prca_pro_id "+
							" join fo_marcas on mar_id = pro_mar_id "+
							" left join fo_precios_locales on pre_pro_id = pro_id "+ filtro_local + 
							" left join fo_unidades_medida on uni_id = pro_uni_id " +
							" left join ve_detalle_cotz on pro_id = dcot_pro_id " + filtro_cot + 
							" where  "+
							" prca_estado = 'A' "+
							" and pro_estado = 'A' "+
							" and mar_estado = 'A' "+
							filtro_patronaux +
							filtro_categoria +
							filtro_marca +
							filtro_id_producto + 
							filtro_orden;
			
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL: " + sql );
			
			ProductoDTO producto = null;
			
			rs = stm.executeQuery();
			while (rs.next()) {
			
				producto = null;
				producto = new ProductoDTO();
				
				producto.setPro_id(rs.getLong("pro_id"));
				
				if (rs.getString("pro_id_bo") != null)
					producto.setPro_id_bo(rs.getLong("pro_id_bo"));
				
				producto.setTipo_producto(rs.getString("pro_tipo_producto"));
				producto.setDescripcion(rs.getString("pro_des_corta"));
				producto.setImg_chica(rs.getString("pro_imagen_minificha"));
				producto.setMarca(rs.getString("mar_nombre"));
				producto.setMarca_id(rs.getLong("mar_id"));
				producto.setGenerico(rs.getString("pro_generico"));
				producto.setImg_grande(rs.getString("pro_imagen_ficha"));
				
				if( rs.getString("pro_nota") != null && rs.getString("pro_nota").charAt(0) == 'S' )
					producto.setCon_nota(true);
				else
					producto.setCon_nota(false);
				if( rs.getString("dcot_id") != null ) {
					// Producto existe como detalle
					producto.setDcot_id(rs.getLong("dcot_id"));
				    producto.setEn_carro(true);
					producto.setCantidad(rs.getDouble("dcot_qsolic"));
					
					if(rs.getString("dcot_obs") != null)
						producto.setNota( rs.getString("dcot_obs") );
					else
						producto.setNota("");
					
				} else {
					producto.setEn_carro(false);
				}
				// No es un producto genérico
				if( rs.getString("pro_generico") == null || rs.getString("pro_generico").compareTo("G") != 0 ) {
					
					if( rs.getString( "pre_estado" ) == null || rs.getString( "pre_estado" ).compareTo("A") != 0 )
						continue;
					if( rs.getString( "uni_estado" ) == null || rs.getString( "uni_estado" ).compareTo("A") != 0 )
						continue;
				
					producto.setPrecio(rs.getDouble("pre_valor"));
					if( rs.getString("uni_cantidad") != null && rs.getString("pro_unidad_medida") != null )
						producto.setPpum(rs.getDouble("pre_valor")/(rs.getDouble("uni_cantidad")*rs.getDouble("pro_unidad_medida")));
					producto.setUnidad_nombre(rs.getString("uni_desc"));
					producto.setUnidad_tipo( rs.getString("pro_tipo_sel") );
					producto.setInter_maximo(rs.getDouble("pro_inter_max"));
					if( rs.getString("pro_inter_valor") != null )
						producto.setInter_valor(rs.getDouble("pro_inter_valor"));
					else
						producto.setInter_valor(1.0);					
					
					if (rs.getString("pro_tipre") != null)
					producto.setTipre(Formatos.formatoUnidad(rs.getString("pro_tipre")));
				}

				lista.add(producto);
								
			} // While	
		}catch (SQLException e) {
			e.printStackTrace();
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListProductosByCriteria - Problema SQL (close)", e);
			}
		}
		return lista;			
			
	}		
		
	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getListItems(java.lang.String, long, long)
	 */
	public List getListItems(String local_id, long pro_padre, long coti_id) throws CotizacionesDAOException {

		PreparedStatement stm=null;
		ProductoDTO item = null;
		List lista_item = new ArrayList();		
		ResultSet rs_item = null;
		try {

			conn = this.getConnection();
			
			logger.debug("en getListItems...");		

			logger.debug("local_id:"+ local_id);
			logger.debug("pro_padre:"+pro_padre);
			logger.debug("coti_id:"+coti_id);
			
			
			
			String sql = "select pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, pro_tipo_producto, pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, pro_fcrea, pro_fmod, pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, pro_id_bo, pro_tipo_sel, " +
						"mar_nombre, " +
						"pre_valor, pre_stock, "+
						"dcot_id, dcot_qsolic, dcot_id as en_carro, " +
						"uni_desc, uni_cantidad " +
						"from fo_productos " +
						"join fo_marcas on mar_id = pro_mar_id " +
						"join fo_precios_locales on pro_id = pre_pro_id and pre_loc_id = ? " +
						"join fo_unidades_medida on uni_id = pro_uni_id  " +
						"left join ve_detalle_cotz on pro_id = dcot_pro_id and dcot_cot_id = ? " +							
						"where pro_id_padre = ? " +
						"and pre_estado = 'A' " +
						"and uni_estado = 'A' " +
						"and mar_estado = 'A' " +
						"and pro_estado = 'A'";
			
			stm = conn.prepareStatement(sql + " WITH UR");
			logger.debug("SQL: " + sql );
			stm.setString(1, local_id );
			stm.setLong(2, coti_id );
			stm.setLong(3, pro_padre );			
			
			rs_item = stm.executeQuery();
			while (rs_item.next()) {
				
				//this.logger.logData( "getListItems", rs_item);
	
				item = null;
				item = new ProductoDTO();
				item.setPro_id(rs_item.getLong("pro_id"));
				item.setTipo_producto(rs_item.getString("pro_tipo_producto"));
				item.setDescripcion(rs_item.getString("pro_des_corta"));
				item.setImg_chica(rs_item.getString("pro_imagen_minificha"));
				item.setMarca(rs_item.getString("mar_nombre"));
				item.setPrecio(rs_item.getDouble("pre_valor"));
				if( rs_item.getString("uni_cantidad") != null && rs_item.getString("pro_unidad_medida") != null )
					item.setPpum(rs_item.getDouble("pre_valor")/(rs_item.getDouble("uni_cantidad")*(rs_item.getDouble("pro_unidad_medida"))));
				item.setUnidad_nombre(rs_item.getString("uni_desc")); 
				item.setUnidad_tipo( rs_item.getString("pro_tipo_sel") );
				item.setInter_maximo(rs_item.getDouble("pro_inter_max"));
				if( rs_item.getString("pro_inter_valor") != null )
					item.setInter_valor(rs_item.getDouble("pro_inter_valor"));
				else
					item.setInter_valor(1.0);
				
				if (rs_item.getString("dcot_qsolic") != null)
					item.setCantidad(rs_item.getDouble("dcot_qsolic"));
				
				item.setValor_diferenciador(rs_item.getString("pro_valor_difer"));
				item.setTipre(Formatos.formatoUnidad(rs_item.getString("pro_tipre")));
				if( rs_item.getString("en_carro") == null )
					item.setEn_carro( false );
				else
					item.setEn_carro( true );
				lista_item.add(item);						
			} // While
		}catch (SQLException e) {
			e.printStackTrace();
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs_item != null)
					rs_item.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getListItems - Problema SQL (close)", e);
			}
		}
		return lista_item;			
			
	}		
/*
 *  (non-Javadoc)
 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getProductSapById(long)
 */
	public ProductoSapEntity getProductSapById(long id_prod) throws CotizacionesDAOException{
		ProductoSapEntity prod = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getProductSapById:");
		
		try {
			String sql = " SELECT id_producto, P.id_catprod as id_cat, uni_med, cod_prod1, cod_prod2, des_corta, " + 
				" des_larga, estado, marca, cod_proppal, origen, un_base, ean13,  " +
				" un_empaque, num_conv, den_conv, atrib9, atrib10, fcarga, mix_web, P.estadoactivo as est, descat, " +
				" substr(P.id_catprod,1,2) seccion, substr(P.id_catprod,3,2) rubro, " +
				" substr(P.id_catprod,5,2) subrubro, substr(P.id_catprod,7) grupo " +
				" FROM bo_productos P, bo_catprod C " +
				" WHERE C.id_catprod = P.id_catprod AND id_producto = ? ";

			logger.debug(sql);
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_prod);
			rs = stm.executeQuery();
			
			int i=0;
			if (rs.next()) {
				prod = new ProductoSapEntity();
				prod.setId( new Long(rs.getString("id_producto")));
				prod.setId_cat(rs.getString("id_cat"));
				prod.setUni_med(rs.getString("uni_med"));
				prod.setCod_prod_1(rs.getString("cod_prod1"));
				prod.setCod_prod_2(rs.getString("cod_prod2"));
				prod.setDes_corta(rs.getString("des_corta"));
				prod.setDes_larga(rs.getString("des_larga"));
				prod.setEstado(rs.getString("estado"));
				if(rs.getString("marca")!=null)
					prod.setMarca(rs.getString("marca"));
				else
					prod.setMarca("");
				prod.setCod_propal(rs.getString("cod_proppal"));
				if(rs.getString("origen")!=null)
					prod.setOrigen(rs.getString("origen"));
				else
					prod.setOrigen("");
				prod.setUn_base(rs.getString("un_base"));
				prod.setEan13(rs.getString("ean13"));
				if(rs.getString("un_empaque")!=null)
					prod.setUn_empaque(rs.getString("un_empaque"));
				else
					prod.setUn_empaque("");
				if(rs.getString("num_conv")!=null)
					prod.setNum_conv(new Integer(rs.getString("num_conv")));
				else
					prod.setNum_conv(new Integer(0));
				if(rs.getString("den_conv")!=null)
					prod.setDen_conv(new Integer(rs.getString("den_conv")));
				else
					prod.setDen_conv(new Integer(0));
				prod.setAtrib9(rs.getString("atrib9"));
				prod.setAtrib10(rs.getString("atrib10"));
				if(rs.getString("fcarga")!=null)
					prod.setFecCarga(rs.getTimestamp("fcarga"));
				else
					prod.setFecCarga(null);
				prod.setMixWeb(rs.getString("mix_web"));
				prod.setEstActivo(rs.getString("est"));
				prod.setNom_cat_sap(rs.getString("descat"));				

				prod.setSeccion(rs.getString("seccion"));
				prod.setRubro(rs.getString("rubro"));
				prod.setSubrubro(rs.getString("subrubro"));
				prod.setGrupo(rs.getString("grupo"));
				
				i++;
			}

			if(i==0) 
				throw new  CotizacionesDAOException(DbSQLCode.SQL_ID_KEY_NO_EXIST);
		} catch (Exception e) {
			logger.debug("Problema getProductSapById:"+ e);
			throw new CotizacionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductSapById - Problema SQL (close)", e);
			}
		}
		return prod;
	}
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getProductoPedidoByIdProdFO(long)
	 */
	public ProductoEntity getProductoPedidoByIdProdFO(long id_prod_fo) throws CotizacionesDAOException {

		Connection 			con 	= null;
		PreparedStatement 	stm 	= null;
		ResultSet 			rs 		= null;

		ProductoEntity 		prod 	= null;
		
		logger.debug("en getProductoPedidoByIdProdFO:");

		String SQLStmt = 
			" SELECT pro_id, pro_id_padre, pro_tipre, pro_cod_sap, pro_uni_id, pro_mar_id, pro_estado, " +
				" pro_tipo_producto, pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, " +
				" pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, pro_fcrea, pro_fmod, " +
				" pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, pro_id_bo, " +
				" mar_nombre, pro_pesable " +
			" FROM fo_productos p " +
			" LEFT JOIN fo_marcas m ON mar_id = pro_mar_id " +
			" WHERE pro_id = ? ";
		
		logger.debug("SQL: " + SQLStmt+ ", id: "+id_prod_fo);
		
		try {

			con = this.getConnection();
			stm = con.prepareStatement( SQLStmt  + " WITH UR");
			stm.setLong(1, id_prod_fo);
			rs = stm.executeQuery();
			
			if (rs.next()) {
				
				prod = new ProductoEntity();
				prod.setId(new Long(rs.getString("pro_id")));
				
				if(rs.getString("pro_id_padre")!=null)
					prod.setId_padre(new Long(rs.getString("pro_id_padre")));
				else
					prod.setId_padre(new Long(0));
				
				prod.setTipre(rs.getString("pro_tipre"));
				prod.setCod_sap(rs.getString("pro_cod_sap"));
				
				if(rs.getString("pro_uni_id")!=null)
					prod.setUni_id(new Long(rs.getString("pro_uni_id")));
				else
					prod.setUni_id(new Long(0));
				if(rs.getString("pro_mar_id")!=null)
					prod.setMar_id(new Long(rs.getString("pro_mar_id")));
				else
					prod.setMar_id(new Long(0));
				
				prod.setEstado(rs.getString("pro_estado"));
				prod.setTipo(rs.getString("pro_tipo_producto"));
				prod.setDesc_corta(rs.getString("pro_des_corta"));
				prod.setDesc_larga(rs.getString("pro_des_larga"));
				prod.setImg_mini_ficha(rs.getString("pro_imagen_minificha"));
				prod.setImg_ficha(rs.getString("pro_imagen_ficha"));
				
				if(rs.getString("pro_unidad_medida")!=null)
					prod.setUnidad_medidad(new Double(rs.getString("pro_unidad_medida")));
				else
					prod.setUnidad_medidad(new Double(0));
				
				prod.setValor_difer(rs.getString("pro_valor_difer"));
				
				if(rs.getString("pro_ranking_ventas")!=null)
					prod.setRank_ventas(new Integer(rs.getString("pro_ranking_ventas")));
				else
					prod.setRank_ventas(new Integer(0));
				
				prod.setFec_crea(rs.getTimestamp("pro_fcrea"));
				prod.setFec_mod(rs.getTimestamp("pro_fmod"));
				
				if(rs.getString("pro_user_mod")!=null)
					prod.setUser_mod(new Integer(rs.getString("pro_user_mod")));
				else
					prod.setUser_mod(new Integer(0));
				
				prod.setGenerico(rs.getString("pro_generico"));
				
				if(rs.getString("pro_inter_valor")!=null)
					prod.setInter_valor(new Double(rs.getString("pro_inter_valor")));
				else
					prod.setInter_valor(new Double("0"));
				
				if(rs.getString("pro_inter_max")!=null)
					prod.setInter_max(new Double(rs.getString("pro_inter_max")));
				else
					prod.setInter_max(new Double("0"));
				
				if(rs.getString("pro_preparable")!=null)
					prod.setEs_prep(rs.getString("pro_preparable"));
				else
					prod.setEs_prep("N");
				
				logger.debug("preparable: " + rs.getString("pro_preparable"));
				
				if(rs.getString("pro_nota")!=null)
					prod.setAdm_coment(rs.getString("pro_nota"));
				else
					prod.setAdm_coment("");
				
				prod.setNom_marca( rs.getString("mar_nombre") );
				prod.setId_bo( new Long(rs.getString("pro_id_bo")) );
				prod.setEs_pesable( rs.getString("pro_pesable") );
				
				
			}

		} catch (SQLException e) {
			logger.debug("Problema getProductoPedidoByIdProdFO:"+ e.getMessage());			
			throw new CotizacionesDAOException(e.getErrorCode()+"");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductoPedidoByIdProdFO - Problema SQL (close)", e);
			}
		}
		return prod;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getCodBarrasByProdId(long)
	 */
	public List getCodBarrasByProdId(long id_prod) throws CotizacionesDAOException {
		
		List lista_codBarr = new ArrayList();
		CodBarraSapEntity codBarra = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getCodBarrasByProdId:");
		
		try {

			String sql = " SELECT cod_prod1, cod_barra, tip_codbar, cod_ppal, unid_med, id_producto, estadoactivo " + 
				" FROM bo_codbarra WHERE id_producto = ? AND estadoactivo = '1' " ;
			logger.debug(sql);
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1,id_prod);
			rs = stm.executeQuery();
			while (rs.next()) {
				codBarra = new CodBarraSapEntity();
				codBarra.setCod_prod_1(rs.getString("cod_prod1"));
				codBarra.setCod_barra(rs.getString("cod_barra"));
				codBarra.setTip_cod_barra(rs.getString("tip_codbar"));
				codBarra.setCod_ppal(rs.getString("cod_ppal"));
				codBarra.setUni_med(rs.getString("unid_med"));
				codBarra.setId_prod(new Long(rs.getString("id_producto")));
				codBarra.setEstado(rs.getString("estadoactivo"));
				lista_codBarr.add(codBarra);
			}

		} catch (Exception e) {
			logger.debug("Problema getCodBarrasByProdId :"+ e);
			throw new CotizacionesDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getCodBarrasByProdId - Problema SQL (close)", e);
			}
		}
		logger.debug("cant en lista:"+lista_codBarr.size());
		return lista_codBarr;
	}

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#updCantProductoCotizacion(cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO)
	 */
	public boolean updCantProductoCotizacion(ModProdCotizacionesDTO prod) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		
		try {
			logger.debug("en updCantProductoCotizacion");
			String sql = " UPDATE  ve_detalle_cotz SET dcot_qsolic = ? WHERE dcot_id = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			logger.debug(sql);
			logger.debug("id_cot:"+prod.getDetcot_id());
			logger.debug("cantidad:"+prod.getDetcot_cantidad());
			
			stm.setDouble(1, prod.getDetcot_cantidad());
			stm.setLong(2, prod.getDetcot_id());

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updCantProductoCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}	


	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getProductosDetCotiz(long)
	 */
	public List getProductosDetCotiz(long id_cot) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_prod = new ArrayList();
		
		logger.debug("Parametros getProductosDetCotiz:");
		logger.debug("numero_cot:"+id_cot);
        /**
         * 02-05-2012
         * Query Modificada para obtener Nombre de Local ya que el campo DIR_LOC_ID no existe
         * se reemplaza
         * JOIN FO_DIRECCIONES ON COT_DIR_ID = DIR_ID
         * JOIN BO_LOCALES ON DIR_LOC_ID = ID_LOCAL 
         * por
         * JOIN FODBA.FO_DIRECCIONES       D   ON D.DIR_ID       = CZ.COT_DIR_ID 
         * JOIN BODBA.BO_POLIGONO          P   ON P.ID_POLIGONO  = D.ID_POLIGONO 
         * JOIN BODBA.BO_ZONAS             Z   ON Z.ID_ZONA      = P.ID_ZONA  
         * JOIN FODBA.FO_PRECIOS_LOCALES PL ON PL.PRE_PRO_ID = PR.PRO_ID AND
         *  PL.PRE_LOC_ID = Z.ID_LOCAL
         */ 	
		try {
			conn = this.getConnection();

            String Sql = "SELECT DC.DCOT_PRO_ID, DC.DCOT_ID, DC.DCOT_QSOLIC, DC.DCOT_UNI_MED, M.MAR_NOMBRE, " 
                + "       PR.PRO_TIPO_PRODUCTO, PR.PRO_DES_CORTA, PR.PRO_UNIDAD_MEDIDA, PR.PRO_TIPRE, " 
                + "       PR.PRO_ID_BO, PR.PRO_INTER_VALOR, PR.PRO_COD_SAP, UM.UNI_DESC, PL.PRE_VALOR "
                + "FROM FODBA.VE_DETALLE_COTZ DC "
                + "     JOIN FODBA.VE_COTIZACION      CZ ON CZ.COT_ID     = DC.DCOT_COT_ID "
                + "     JOIN FODBA.FO_PRODUCTOS       PR ON PR.PRO_ID     = DC.DCOT_PRO_ID AND " 
                + "                                         PR.PRO_ESTADO = 'A' "
                + "     JOIN FODBA.FO_UNIDADES_MEDIDA UM ON UM.UNI_ID     = PR.PRO_UNI_ID "
                + "     JOIN FODBA.FO_MARCAS          M  ON M.MAR_ID      = PR.PRO_MAR_ID "
                + "     JOIN FODBA.FO_DIRECCIONES     D  ON CZ.COT_DIR_ID = D.DIR_ID "
                + "     JOIN BODBA.BO_POLIGONO        P  ON P.ID_POLIGONO = D.ID_POLIGONO "
                + "     JOIN BODBA.BO_ZONAS           Z  ON Z.ID_ZONA     = P.ID_ZONA "
                + "     JOIN FODBA.FO_PRECIOS_LOCALES PL ON PL.PRE_PRO_ID = PR.PRO_ID AND " 
                + "                                         PL.PRE_LOC_ID = Z.ID_LOCAL AND " 
                + "                                         PL.PRE_ESTADO = 'A' "
                + "WHERE DC.DCOT_COT_ID = ? " 
                + "ORDER BY DC.DCOT_ID DESC ";
			
			stm = conn.prepareStatement(Sql + " WITH UR");
			logger.debug(Sql);
			logger.debug("id_cot:"+ id_cot);
			
			stm.setLong(1, id_cot);
			rs = stm.executeQuery();
			while (rs.next()) {
				ProductosCotizacionesDTO prod = new ProductosCotizacionesDTO();
				prod.setDetcot_id(rs.getLong("dcot_id"));
				prod.setDetcot_cantidad(rs.getDouble("dcot_qsolic"));
				prod.setDetcot_umed(rs.getString("dcot_uni_med"));
				prod.setDetcot_precio(rs.getDouble("pre_valor"));
				prod.setDetcot_proId(rs.getLong("dcot_pro_id"));
				prod.setDetcot_desc(rs.getString("pro_des_corta"));
				prod.setPro_tipre(rs.getString("pro_tipre"));
				prod.setPro_tipo_producto(rs.getString("pro_tipo_producto"));
				prod.setUni_desc(rs.getString("uni_desc"));
				prod.setMar_nombre(rs.getString("mar_nombre"));
				prod.setDetcot_pro_id_bo(rs.getLong("pro_id_bo"));
				if( rs.getString("pro_inter_valor") != null && !rs.getString("pro_inter_valor").equals(""))
					prod.setIntervalo(rs.getDouble("pro_inter_valor"));
				else
					prod.setIntervalo(1.0);
				prod.setDetcot_codSap(rs.getString("pro_cod_sap"));
				
				
				list_prod.add(prod);
				}
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getProductosDetCotiz - Problema SQL (close)", e);
			}
		}
		return list_prod;
	}	
	

	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#updProductoCotizacion(cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO)
	 */
	public boolean updProductoCotizacion(ProductosCotizacionesDTO prod) throws CotizacionesDAOException {
		PreparedStatement stm=null;
		boolean result = false;
		try {
			conn = this.getConnection();
			logger.debug("en updProductoCotizacion");
			String sql = " UPDATE  ve_detalle_cotz " +
					     " SET dcot_descripcion = ?," +
					     " dcot_uni_med = ?," +
					     " dcot_precio = ?, " +
					     " dcot_precio_lista = ?, " +
					     " dcot_pro_id_bo = ?," +
					     " dcot_cod_prod1 = ? " +
					     " WHERE dcot_cot_id = ? and dcot_pro_id = ? ";
			logger.debug(sql);
			logger.debug("descripcion:"+prod.getPro_tipo_producto()+" "+prod.getMar_nombre()+" "+prod.getDetcot_desc());
			logger.debug("unidad:"+prod.getPro_tipre());
			logger.debug("precio:"+prod.getDetcot_precio());
			logger.debug("precio_lista:"+prod.getDetcot_precio_lista());
			logger.debug("pro_id_bo:"+prod.getDetcot_pro_id_bo());
			logger.debug("cod_prod1:"+prod.getDetcot_codSap());
			logger.debug("cot_id:"+prod.getDetcot_cot_id());
			logger.debug("id_producto:"+prod.getDetcot_proId());
			stm = conn.prepareStatement(sql);

			stm.setString(1, prod.getPro_tipo_producto()+" "+prod.getMar_nombre()+" "+prod.getDetcot_desc());
			stm.setString(2, prod.getPro_tipre());
			stm.setDouble(3, prod.getDetcot_precio());
			stm.setDouble(4, prod.getDetcot_precio_lista());
			stm.setDouble(5, prod.getDetcot_pro_id_bo());
			stm.setString(6, prod.getDetcot_codSap());
			stm.setLong(7, prod.getDetcot_cot_id());
			stm.setLong(8, prod.getDetcot_proId());
			

			int i = stm.executeUpdate();
			if(i>0)
				result = true;
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updProductoCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	/**
	 * Modifica el comentario de la cotizacion
	 * @param id_cot
	 * @param comentario
	 * @return boolean
	 * @throws CotizacionesDAOException
	 */
	public boolean setModComentarioCotizacion(long id_cot, String comentario, String fuera_mix) throws CotizacionesDAOException {
		
		PreparedStatement stm=null;
		boolean result = false;
		try {
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			logger.debug("en setModComentarioCotizacion");
			String sql = "UPDATE  ve_cotizacion SET cot_obs = ?, cot_fueramix = ? WHERE cot_id = ? ";
			logger.debug(sql);
			logger.debug("id_cot:"+id_cot);
			logger.debug("comentario:"+comentario);
			logger.debug("fuera_mix:"+fuera_mix);
			
			stm = conn.prepareStatement(sql);

			stm.setString(1, comentario);
			stm.setString(2, fuera_mix);
			stm.setLong(3, id_cot);

			int i = stm.executeUpdate();
			if(i>0)
				result = true;

		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : setModComentarioCotizacion - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see cl.bbr.vte.cotizaciones.dao.CotizacionesDAO#getSearch(java.util.List, long, long)
	 */
	public List getSearch( List patron, long local_id, long criterio ) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List list_prod = new ArrayList();
		
		logger.debug("Parametros getProductosDetCotiz:");
		logger.debug("patron:"+patron);
		logger.debug("local_id:"+local_id);
		logger.debug("criterio:"+criterio);
	
		try {
		
			logger.debug( "Búsqueda por marca: local_id=" + local_id );
			logger.debug( "Búsqueda por marca: patron=" + Formatos.formatPatron(patron) );
			
			conn = this.getConnection();
			String Sql =  "select cat1.cat_id, fo_catsubcat.cat_id cat_id_padre, cat1.cat_nombre, cat1.cat_tipo, pro_mar_id, mar_nombre, cat2.cat_nombre as subcat, count(*) as cantidad  " +
						"from fo_productos " +
						"join fo_marcas on pro_mar_id = mar_id " +
						"join fo_unidades_medida on uni_id = pro_uni_id " +
						"join fo_productos_categorias on pro_id = prca_pro_id " +
						"join fo_precios_locales on pro_id = pre_pro_id " +
						"join fo_categorias as cat1 on cat1.cat_id = prca_cat_id " +
						"left join fo_catsubcat on cat1.cat_id = fo_catsubcat.subcat_id " +
						"left join fo_categorias as cat2 on cat2.cat_id = fo_catsubcat.cat_id " +
						"where pre_estado = 'A' " +
						"and pre_loc_id = ? " +
						"and mar_estado = 'A' " +
						"and pro_estado = 'A' " +
						"and uni_estado = 'A' " +
						"and prca_estado = 'A' " +
						"and cat1.cat_estado = 'A' " +
						"and cat2.cat_estado = 'A' " +
						"and (" + Formatos.formatPatron(patron) + ") " +
						"group by cat1.cat_id, fo_catsubcat.cat_id, cat1.cat_nombre, cat1.cat_tipo, pro_mar_id, mar_nombre, cat2.cat_nombre " +
						"order by cat2.cat_nombre, cat1.cat_nombre";
			
			logger.debug(Sql);
			stm = conn.prepareStatement(Sql + " WITH UR");
			
			stm.setLong(1, local_id);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				CategoriaDTO categoria = new CategoriaDTO();
				
				categoria.setId(rs.getLong("cat_id"));
				categoria.setId_padre(rs.getLong("cat_id_padre")+0);
				categoria.setNombre(rs.getString("cat_nombre") );
				categoria.setId_marca(rs.getLong("pro_mar_id"));
				categoria.setNombre_marca(rs.getString("mar_nombre") );
				categoria.setTipo(rs.getString("cat_tipo") );
				categoria.setSubcat(rs.getString("subcat") );
				categoria.setCant_productos(rs.getLong("cantidad"));
				
				list_prod.add(categoria);
				}
			
		}catch (SQLException e) {
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getSearch - Problema SQL (close)", e);
			}
		}
		return list_prod;
	}	
	
	
	public boolean updCotizacion(ProcInsCotizacionDTO dto) throws CotizacionesDAOException {
		PreparedStatement stm = null;
		boolean result = false;
		
		logger.debug("En updCotizacion...");
		
		try {
			
			String Sql = "UPDATE ve_cotizacion set " +
					    " cot_medio_pago=?, " +
					    " cot_num_mp=?, " +
					    " cot_obs=?, " +
					    " cot_nomtbank=?, " +
						" cot_ncuotas=?, " +
						" cot_pol_id=?, " +
						" cot_pol_sust=?, " +
						" cot_sgente_op=?, " +
						" cot_sgente_txt=?, " +
						" cot_tb_banco=?, " +
						" cot_fexp_mp=? " +
						" where cot_id=? ";

			logger.debug("SQL :"+Sql);	
			logger.debug( "DTO:" + dto.toString() );
			logger.debug("medio pago : " + dto.getMedio_pago());
			logger.debug("numero tarjeta : " + dto.getNumero_tarjeta());
			logger.debug("Observacion : " + dto.getObs());
			logger.debug("Nombre tarjeta : " + dto.getTbk_nombre_tarjeta());
			logger.debug("Numero de cuotas : "  +dto.getNumero_cuotas());
			logger.debug("Cot id : " + dto.getCot_id());
			logger.debug("sustitucion : " + dto.getSustitucion());
			logger.debug("persona autor : "  + dto.getSgente_txt());
			logger.debug("nombre banco : " + dto.getNombre_banco());
			logger.debug("fecha expira : " + dto.getTbk_fec_expira());
			
			conn = this.getConnection();
			stm = conn.prepareStatement(Sql);
			stm.setString(1, dto.getMedio_pago());
			stm.setString(2, dto.getNumero_tarjeta());
			stm.setString(3, dto.getObs());
			stm.setString(4, dto.getTbk_nombre_tarjeta());
			stm.setLong(5, dto.getNumero_cuotas());
			stm.setLong(6, dto.getId_sustitucion());
			stm.setString(7, dto.getSustitucion());
			stm.setLong(8, dto.getSgente_op());
			stm.setString(9, dto.getSgente_txt());
			stm.setString(10, dto.getNombre_banco());
			stm.setString(11, dto.getTbk_fec_expira());
			stm.setLong(12, dto.getCot_id());
						
			int i = stm.executeUpdate();
			if (i>0) {
	            result = true;
	        }
		}catch (SQLException e) {
			e.printStackTrace();
			throw new CotizacionesDAOException(String.valueOf(e.getErrorCode()),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new CotizacionesDAOException(e);			
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : updCotizacion - Problema SQL (close)", e);
			}
		}
		logger.debug("updCotizacion - result : "+result);
		return result;
	}	
	
}
