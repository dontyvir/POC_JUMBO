package cl.bbr.jumbocl.pedidos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.HorarioPickingEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar los calendarios que se encuentran en la base de datos.
 * @author BBR
 *
 */
public class JdbcCalendarioDAO implements  CalendarioDAO{

	/**
	 * Permite generar los eventos en un archivo log.
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
		return this.conn;

	}
	
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
            	logger.error(e.getMessage(),e);
            }
		}
			
	}

	
	
	// ************ Métodos Publicos *************** //
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws CalendarioDAOException 
	 */
	public void setTrx(JdbcTransaccion trx)
			throws CalendarioDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new CalendarioDAOException(e);
		}
	}
	
	/**
	 * Obtiene entidad Semana
	 * 
	 * @param  id_semana long 
	 * @return SemanasEntity
	 * @throws CalendarioDAOException 
	 */
	public SemanasEntity getSemana(long id_semana) throws CalendarioDAOException {
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		SemanasEntity sem = new SemanasEntity();
		
		try {
			String cadQuery=" SELECT id_semana, n_semana, ano, fini, ffin " +
							" FROM bo_semanas " +
							" WHERE id_semana = ? ";
			conn = this.getConnection();
			
			stm = conn.prepareStatement(cadQuery + " WITH UR");			
			
			stm.setLong(1,id_semana);
			
			rs = stm.executeQuery();
			while (rs.next()) {

				sem.setId_semana(rs.getLong("id_semana"));
				sem.setN_semana(rs.getInt("n_semana"));
				sem.setAno(rs.getInt("ano"));
				sem.setF_ini(rs.getDate("fini"));
				sem.setF_fin(rs.getDate("ffin"));

			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}	
		return sem;

	}
	
	/**
	 * Obtiene entidad Semana
	 * 
	 * @param  n_semana int , número de la semana en el año
	 * @param  ano int , año
	 * @return SemanasEntity
	 * @throws CalendarioDAOException 
	 */
	public SemanasEntity getSemana(int n_semana, int ano) throws CalendarioDAOException {
		PreparedStatement stm=null;
		ResultSet rs = null;
		SemanasEntity sem = null;
		
		try {
			logger.debug("Ejecuta getSemana(n_semana, ano)");
			conn = this.getConnection();
			String sql = 	" SELECT id_semana, n_semana, ano, fini, ffin " +
							" FROM bo_semanas " +
							" WHERE n_semana = ? AND ano = ? "; 
			stm = conn.prepareStatement(sql + " WITH UR");	
			
			logger.debug("sql:"+sql);
			logger.debug("n_semana:"+n_semana);
			logger.debug("ano:"+ano);
			stm.setInt(1,n_semana);
			stm.setInt(2,ano);
			
			rs = stm.executeQuery();
			while (rs.next()) {
				sem = new SemanasEntity();
				sem.setId_semana(rs.getLong("id_semana"));
				sem.setN_semana(rs.getInt("n_semana"));
				sem.setAno(rs.getInt("ano"));
				sem.setF_ini(rs.getDate("fini"));
				sem.setF_fin(rs.getDate("ffin"));

			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}		
		return sem;
	}

	/**
	 * Obtiene un horario de picking definido para una semana en un calendario de picking de un local
	 * 
	 * @param  id_hor_pick long 
	 * @return HorarioPickingEntity
	 * @throws CalendarioDAOException 
	 */
	public HorarioPickingEntity getHorarioPicking(long id_hor_pick)
		throws CalendarioDAOException {
		
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		HorarioPickingEntity hor = new HorarioPickingEntity();
		
		try {
			String cadQuery=" SELECT id_hor_pick, id_local, id_semana, hini, hfin, n_horas " +
							" FROM bo_horario_pick " +
							" WHERE id_hor_pick = ? ";
			conn = this.getConnection();
			
			stm = conn.prepareStatement(cadQuery + " WITH UR");			
			
			stm.setLong(1,id_hor_pick);
			
			rs = stm.executeQuery();
			while (rs.next()) {

				hor.setId_hor_pick(rs.getLong("id_hor_pick"));
				hor.setId_local(rs.getLong("id_local"));
				hor.setId_semana(rs.getLong("id_semana"));
				hor.setH_ini(rs.getTime("hini"));
				hor.setH_fin(rs.getTime("hfin"));
				hor.setN_horas(rs.getInt("n_horas"));

			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}	
		return hor;
	}
	
	
	/**
	 * Obtiene listado de horarios definidos para una semana en un calendario de despacho para
	 * una zona de despacho de un local
	 * 
	 * @param  id_semana long 
	 * @param  id_local long 
	 * @return List of HorarioPickingEntity's
	 * @throws CalendarioDAOException
	 */
	public List getHorariosPicking(long id_semana, long id_local) throws CalendarioDAOException {
		
		List result = new ArrayList();
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		try {
			String cadQuery=" SELECT id_hor_pick, id_semana, id_local, hini, hfin " +
							" FROM bo_horario_pick " +
							" WHERE id_semana = ? AND id_local = ? " +
							" ORDER BY hini";
			conn = this.getConnection();
			
			stm = conn.prepareStatement(cadQuery + " WITH UR");
			
			stm.setLong(1,id_semana);
			stm.setLong(2,id_local);
			
			rs = stm.executeQuery(); 
			while (rs.next()) {
				HorarioPickingEntity hor = new HorarioPickingEntity();
				hor.setId_hor_pick(rs.getLong("id_hor_pick"));
				hor.setId_semana(rs.getLong("id_semana"));
				hor.setId_local(rs.getLong("id_local"));
				hor.setH_ini(rs.getTime("hini"));
				hor.setH_fin(rs.getTime("hfin"));
				result.add(hor);
				
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}	
		
		return result;
		
		
	}
	

	/**
	 * Inserta registro a la tabla BO_SEMANAS
	 * 
	 * @param  n_semana int 
	 * @param  ano int 
	 * @return long, nuevo id
	 * @throws CalendarioDAOException
	 */
	public long InsSemana(int n_semana, int ano) throws CalendarioDAOException {
		
		
		PreparedStatement stm = null;	
		ResultSet rs = null;
		int	rc;
		long id = -1;
		try {

			
			java.util.Date lunes	= new Date();
			java.util.Date domingo 	= new Date();

			Calendar cal =  GregorianCalendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.MONDAY);

			cal.set(Calendar.WEEK_OF_YEAR,n_semana);
			cal.set(Calendar.YEAR,ano);
			
			cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
			domingo = cal.getTime();
			
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			lunes 	= cal.getTime();
			
			conn = this.getConnection();
			stm = conn.prepareStatement(
					" INSERT INTO bo_semanas (n_semana, ano, fini, ffin) " +
					" VALUES (?,?,?,?)"		
					,Statement.RETURN_GENERATED_KEYS);

			stm.setInt(1, n_semana);
			stm.setInt(2, ano);
			stm.setObject(3, new java.sql.Date(lunes.getTime()));
			stm.setObject(4, new java.sql.Date(domingo.getTime()));
			
			rc = stm.executeUpdate();
			
			logger.debug("rc: " + rc);
			
	        rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }
	       

		} catch (SQLException e) {
			logger.debug("Problema en InsSemana:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}
		return id;
	}

	/**
	 * Inserta un registro en la tabla bo_horario_pick
	 * 
	 * @param  id_local long 
	 * @param  id_semana long 
	 * @param  hini Time 
	 * @param  hfin Time 
	 * @return long, nuevo id
	 * @throws CalendarioDAOException
	 */
	public long InsHorarioPicking(long id_local, long id_semana, Time hini, Time hfin) throws CalendarioDAOException {
		
		
		PreparedStatement stm = null;	
		ResultSet rs = null;
		int	rc;
		int id = -1;

		try {
			
			conn = this.getConnection();

			stm = conn.prepareStatement(
					" INSERT INTO bo_horario_pick (id_local, id_semana, hini, hfin, n_horas) " +
					" VALUES (?,?,?,?,?)"		
					,Statement.RETURN_GENERATED_KEYS);
			
			stm.setLong(1, id_local);
			stm.setLong(2, id_semana);
			stm.setTime(3, hini);
			stm.setTime(4, hfin);
			stm.setInt(5, 4);
			
			rc = stm.executeUpdate();
			
			logger.debug("rc: " + rc);
			
	        rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }


		} catch (SQLException e) {
			logger.debug("Problema en InsHorarioPicking:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}	
		return id;		
	}

	/**
	 * Elimina registro en la tabla bo_horarios_picking
	 * 
	 * @param  id_hor_pick long 
	 * 
	 * @throws CalendarioDAOException
	 */
	public void DelHorarioPicking(long id_hor_pick) throws CalendarioDAOException {

		
		PreparedStatement stm = null;
		int	rc;

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(" DELETE FROM bo_horario_pick WHERE id_hor_pick = ?");
			stm.setLong(1, id_hor_pick);
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);

		} catch (SQLException e) {
			logger.error(e.toString());
			throw new CalendarioDAOException(String.valueOf(e.getErrorCode()));
		} finally {
			try {
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
			}
		}
	}

	//****************** Despacho **************************/
	
	/**
	 * Obtiene un horario de despacho definido para una semana en un calendario de despacho para
	 * una zona de despacho de un local
	 * 
	 * @param  id_hor_desp long 
	 * @return HorarioDespachoEntity
	 * @throws CalendarioDAOException
	 */
	public HorarioDespachoEntity getHorarioDespacho(long id_hor_desp) throws CalendarioDAOException {
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		HorarioDespachoEntity hor = new HorarioDespachoEntity();
		
		try {
			String cadQuery=" SELECT id_hor_desp, id_zona, id_semana, hini, hfin, n_horas " +
							" FROM bo_horario_desp " +
							" WHERE id_hor_desp = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(cadQuery + " WITH UR");			
			stm.setLong(1,id_hor_desp);
			rs = stm.executeQuery();
			while (rs.next()) {

				hor.setId_hor_desp(rs.getLong("id_hor_desp"));
				hor.setId_zona(rs.getLong("id_zona"));
				hor.setId_semana(rs.getLong("id_semana"));
				hor.setH_ini(rs.getTime("hini"));
				hor.setH_fin(rs.getTime("hfin"));
				hor.setN_horas(rs.getInt("n_horas"));

			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}
		
		return hor;
	}

	/**
	 * Inserta un registro en la tabla bo_horario_desp
	 * 
	 * @param  id_zona long 
	 * @param  id_semana long 
	 * @param  hini Time 
	 * @param  hfin Time 
	 * @return long, nuevo id
	 * @throws CalendarioDAOException
	 */
	public long InsHorarioDespacho(long id_zona, long id_semana, Time hini, Time hfin) throws CalendarioDAOException {

		
		PreparedStatement stm = null;
		ResultSet rs = null;
		int	rc;
		int id = -1;
		
		try {
			
			conn = this.getConnection();
			stm = conn.prepareStatement(
					" INSERT INTO bo_horario_desp (id_zona, id_semana, hini, hfin, n_horas) " +
					" VALUES (?,?,?,?,?)"		
					,Statement.RETURN_GENERATED_KEYS);
			
			stm.setLong(1, id_zona);
			stm.setLong(2, id_semana);
			stm.setTime(3, hini);
			stm.setTime(4, hfin);
			stm.setInt(5, 4);
			
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
			
	        rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }

		} catch (SQLException e) {
			logger.debug("Problema en InsHorarioDespacho:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}
		return id;
	}

	/**
	 * Borra registro en tabla bo_horario_desp
	 * 
	 * @param  id_hor_desp long 
	 * 
	 * @throws CalendarioDAOException
	 */
	public void DelHorarioDespacho(long id_hor_desp) throws CalendarioDAOException {
		
		PreparedStatement stm = null;
		int	rc;

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(
					" DELETE FROM bo_horario_desp " +
					" WHERE id_hor_desp = ?"		
					);
			stm.setLong(1, id_hor_desp);
			rc = stm.executeUpdate();
			logger.debug("rc: " + rc);
		} catch (SQLException e) {
			logger.error(e.toString());
			throw new CalendarioDAOException(String.valueOf(e.getErrorCode()));
		} finally {
			try {
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				stm=null;
			}
		}
		
	}

	
	/**
	 * Obtiene listado con horarios de despacho para una semana y una zona
	 * 
	 * @param  id_semana long 
	 * @param  id_zona long 
	 * @return List of HorarioDespachoEntity
	 * @throws CalendarioDAOException
	 */
	public List getHorariosDespacho(long id_semana, long id_zona) throws CalendarioDAOException {

		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String SQLStmt = 
				" SELECT id_hor_desp, id_semana, id_zona, hini, hfin " +
				" FROM bo_horario_desp " +
				" WHERE id_semana = ? AND id_zona = ? " +
				" ORDER BY hini"
				;
		
		logger.debug("Ejecución DAO getHorariosDespacho()");
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_semana: " + id_semana);
		logger.debug("id_zona: " + id_zona);
		
		
		try {

			conn = this.getConnection();
			stm = conn.prepareStatement( SQLStmt  + " WITH UR");			
			stm.setLong(1,id_semana);
			stm.setLong(2,id_zona);
			rs = stm.executeQuery(); 
			while (rs.next()) {
				HorarioDespachoEntity hor = new HorarioDespachoEntity();
				hor.setId_hor_desp(rs.getLong("id_hor_desp"));
				hor.setId_semana(rs.getLong("id_semana"));
				hor.setId_zona(rs.getLong("id_zona"));
				hor.setH_ini(rs.getTime("hini"));
				hor.setH_fin(rs.getTime("hfin"));
				result.add(hor);
			}
		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}
		return result;
		
		
	}

	/**
	 * Obtiene el listado de semanas, segun semana de inicio y semana de fin
	 * 
	 * @param  id_semana_ini
	 * @param  id_semana_fin
	 * @return List SemanasEntity
	 * @throws CalendarioDAOException
	 */
	public List getLstSemanas(long id_semana_ini, long id_semana_fin) throws CalendarioDAOException {
		List result = new ArrayList();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String SQLStmt = 
				" SELECT id_semana, n_semana, ano, fini, ffin " +
				" FROM bo_semanas " +
				" WHERE id_semana BETWEEN ? and ? "
				;
		
		logger.debug("Ejecución DAO getLstSemanas()");
		logger.debug("SQL: " + SQLStmt);
		logger.debug("id_semana_ini: " + id_semana_ini);
		logger.debug("id_semana_fin: " + id_semana_fin);
		
		
		try {

			conn = this.getConnection();
			
			stm = conn.prepareStatement( SQLStmt  + " WITH UR");			
			
			stm.setLong(1,id_semana_ini);
			stm.setLong(2,id_semana_fin);
			
			rs = stm.executeQuery(); 
			while (rs.next()) {
				SemanasEntity sem = new SemanasEntity();
				sem.setId_semana(rs.getLong("id_semana"));
				sem.setN_semana(rs.getInt("n_semana"));
				sem.setAno(rs.getInt("ano"));
				sem.setF_ini(rs.getDate("fini"));
				sem.setF_fin(rs.getDate("ffin"));
				result.add(sem);
				
			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}
		return result;
	}

	/**
	 * Obtiene el horario de picking relacionado a la jornada de picking
	 * 
	 * @param  id_jpicking
	 * @return HorarioPickingEntity
	 * @throws CalendarioDAOException
	 */
	public HorarioPickingEntity getHorarioPickingByIdJorPick(long id_jpicking) throws CalendarioDAOException {
		
		PreparedStatement stm=null;
		ResultSet rs = null;
		HorarioPickingEntity hor = new HorarioPickingEntity();
		
		try {
			String cadQuery=" SELECT H.id_hor_pick id_hor_pick, H.id_local id_local, H.id_semana id_semana, hini, hfin, n_horas " +
							" FROM bo_horario_pick H " +
							" JOIN bo_jornadas_pick J ON H.id_hor_pick = J.id_hor_pick AND J.id_jpicking = ? ";
			conn = this.getConnection();
			
			stm = conn.prepareStatement(cadQuery + " WITH UR");			
			
			stm.setLong(1,id_jpicking);
			
			rs = stm.executeQuery();
			while (rs.next()) {

				hor.setId_hor_pick(rs.getLong("id_hor_pick"));
				hor.setId_local(rs.getLong("id_local"));
				hor.setId_semana(rs.getLong("id_semana"));
				hor.setH_ini(rs.getTime("hini"));
				hor.setH_fin(rs.getTime("hfin"));
				hor.setN_horas(rs.getInt("n_horas"));

			}

		} catch (SQLException e) {
			logger.debug("Problema:"+ e);
			throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}	
		return hor;
	}

    /**
     * @param diasCalendario
     * @param idZona
     * @return
     */
    public List getCalendarioDespachoByDias(int diasCalendario, long idZona) throws CalendarioDAOException {
        List horarios = new ArrayList();  
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select jd.ID_JDESPACHO, jd.ID_JPICKING, jd.CAPAC_DESPACHO, jd.CAPAC_OCUPADA CAPAC_OCUPADA_DESP, " +
                     "jd.TARIFA_ECONOMICA, jd.TARIFA_EXPRESS, jd.TARIFA_NORMAL, jd.TARIFA_UMBRAL, jd.FECHA, " +
                     "hd.ID_HOR_DESP, hd.HINI ini_desp, hd.HFIN fin_desp, " +
                     "jp.CAPAC_PICKING, jp.CAPAC_OCUPADA CAPAC_OCUPADA_PICK, jp.HRS_VALIDACION, jp.HRS_OFRECIDO_WEB, " +
                     "hp.HINI ini_pick, hp.HFIN fin_pick, jp.FECHA FECHA_PICK " +
                     "from bodba.bo_jornada_desp jd " +
                     "inner join bodba.bo_horario_desp hd on jd.ID_HOR_DESP = hd.ID_HOR_DESP " +
                     "inner join bodba.bo_jornadas_pick jp on jp.ID_JPICKING = jd.ID_JPICKING " +
                     "inner join bodba.bo_horario_pick hp on hp.ID_HOR_PICK = jp.ID_HOR_PICK " +
                     "where jd.FECHA >= current date and jd.FECHA < ( current date + ? days ) " +
                     "and jd.ID_ZONA = ? " +
                     "and jd.CAPAC_DESPACHO > jd.CAPAC_OCUPADA " +
                     "order by hd.HINI, jd.FECHA";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            stm.setInt(1, diasCalendario);
            stm.setLong(2, idZona);
            
            rs = stm.executeQuery();
            
            String keyVentana = "";
            HorarioDespachoEntity ventana = new HorarioDespachoEntity();
            while (rs.next()) {
                if ( ( !keyVentana.equalsIgnoreCase("") ) && ( !keyVentana.equalsIgnoreCase(rs.getString("ini_desp")+"-"+rs.getString("fin_desp")) ) ) {
                    horarios.add(ventana);
                }
                if ( ( keyVentana.equalsIgnoreCase("") ) || ( !keyVentana.equalsIgnoreCase(rs.getString("ini_desp")+"-"+rs.getString("fin_desp")) ) ) {
                    ventana = new HorarioDespachoEntity();
                    keyVentana = rs.getString("ini_desp")+"-"+rs.getString("fin_desp");
                    ventana.setId_hor_desp( rs.getLong("ID_HOR_DESP") );
                    ventana.setH_ini( rs.getTime("ini_desp") );
                    ventana.setH_fin( rs.getTime("fin_desp") );
                }                
                JornadaDespachoEntity jor1 = new JornadaDespachoEntity();
                jor1.setId_jdespacho(rs.getLong("ID_JDESPACHO"));
                jor1.setId_hor_desp(rs.getLong("ID_HOR_DESP"));
                jor1.setId_jpicking(rs.getLong("ID_JPICKING"));
                jor1.setFecha(rs.getDate("FECHA"));
                jor1.setCapac_despacho(rs.getLong("CAPAC_DESPACHO"));
                jor1.setCapac_despacho_ocupada(rs.getLong("CAPAC_OCUPADA_DESP"));
                jor1.setCapac_picking(rs.getLong("CAPAC_PICKING"));
                jor1.setCapac_picking_ocupada(rs.getLong("CAPAC_OCUPADA_PICK"));
                jor1.setTarifa_express(rs.getInt("TARIFA_EXPRESS"));
                jor1.setTarifa_normal(rs.getInt("TARIFA_NORMAL"));
                jor1.setTarifa_economica(rs.getInt("TARIFA_ECONOMICA"));
                jor1.setTarifa_umbral(rs.getInt("TARIFA_UMBRAL"));
                jor1.setHrs_validacion(rs.getInt("HRS_VALIDACION"));
                jor1.setHrs_ofrecido_web(rs.getInt("HRS_OFRECIDO_WEB"));
                jor1.setHoraIniPicking(rs.getTime("ini_pick"));
                jor1.setHoraFinPicking(rs.getTime("fin_pick"));
                jor1.setFecha_picking(rs.getDate("FECHA_PICK"));
                ventana.getJornadas().add(jor1);
            }
            if ( !keyVentana.equalsIgnoreCase("") ) {
                horarios.add(ventana);
            }

        } catch (SQLException e) {
            logger.debug("Problema:"+ e);
            throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
				throw new CalendarioDAOException(e);
			}finally{
				rs=null;
				stm=null;
			}
		}
        return horarios;
    }
	
    /**
     * @param diasCalendario
     * @param idZona
     * @return
     */
    public List getCalendarioDespachoByDias(int diasCalendario, long idZona, long capacidad_ocupada_pick) throws CalendarioDAOException {
        List horarios = new ArrayList();  
        //JdbcJornadasDAO jornadas = new 
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select jd.ID_JDESPACHO, jd.ID_JPICKING, jd.CAPAC_DESPACHO, jd.CAPAC_OCUPADA CAPAC_OCUPADA_DESP, " +
                     "jd.TARIFA_ECONOMICA, jd.TARIFA_EXPRESS, jd.TARIFA_NORMAL, jd.TARIFA_UMBRAL, jd.FECHA, " +
                     "hd.ID_HOR_DESP, hd.HINI ini_desp, hd.HFIN fin_desp, " +
                     "jp.CAPAC_PICKING, jp.CAPAC_OCUPADA CAPAC_OCUPADA_PICK, jp.HRS_VALIDACION, jp.HRS_OFRECIDO_WEB, " +
                     "hp.HINI ini_pick, hp.HFIN fin_pick, jp.FECHA FECHA_PICK " +
                     "from bodba.bo_jornada_desp jd " +
                     "inner join bodba.bo_horario_desp hd on jd.ID_HOR_DESP = hd.ID_HOR_DESP " +
                     "inner join bodba.bo_jornadas_pick jp on jp.ID_JPICKING = jd.ID_JPICKING " +
                     "inner join bodba.bo_horario_pick hp on hp.ID_HOR_PICK = jp.ID_HOR_PICK " +
                     "where jd.FECHA >= current date and jd.FECHA < ( current date + ? days ) " +
                     "and jd.ID_ZONA = ? " +
                     "and jd.CAPAC_DESPACHO > jd.CAPAC_OCUPADA " +
                     "order by hd.HINI, jd.FECHA";
        
        try {
            conn = this.getConnection();
            stm = conn.prepareStatement( sql  + " WITH UR");
            stm.setInt(1, diasCalendario);
            stm.setLong(2, idZona);
            
            rs = stm.executeQuery();
            
            String keyVentana = "";
            HorarioDespachoEntity ventana = new HorarioDespachoEntity();
            while (rs.next()) {
                if ( ( !keyVentana.equalsIgnoreCase("") ) && ( !keyVentana.equalsIgnoreCase(rs.getString("ini_desp")+"-"+rs.getString("fin_desp")) ) ) {
                    horarios.add(ventana);
                }
                if ( ( keyVentana.equalsIgnoreCase("") ) || ( !keyVentana.equalsIgnoreCase(rs.getString("ini_desp")+"-"+rs.getString("fin_desp")) ) ) {
                    ventana = new HorarioDespachoEntity();
                    keyVentana = rs.getString("ini_desp")+"-"+rs.getString("fin_desp");
                    ventana.setId_hor_desp( rs.getLong("ID_HOR_DESP") );
                    ventana.setH_ini( rs.getTime("ini_desp") );
                    ventana.setH_fin( rs.getTime("fin_desp") );
                }                
                JornadaDespachoEntity jor1 = new JornadaDespachoEntity();
                jor1.setId_jdespacho(rs.getLong("ID_JDESPACHO"));
                jor1.setId_hor_desp(rs.getLong("ID_HOR_DESP"));
                jor1.setId_jpicking(rs.getLong("ID_JPICKING"));
                jor1.setFecha(rs.getDate("FECHA"));
                jor1.setCapac_despacho(rs.getLong("CAPAC_DESPACHO"));
                jor1.setCapac_despacho_ocupada(rs.getLong("CAPAC_OCUPADA_DESP"));
                jor1.setCapac_picking(rs.getLong("CAPAC_PICKING"));
                jor1.setCapac_picking_ocupada(capacidad_ocupada_pick);
                //jor1.setCapac_picking_ocupada(rs.getLong("CAPAC_OCUPADA_PICK"));
                jor1.setTarifa_express(rs.getInt("TARIFA_EXPRESS"));
                jor1.setTarifa_normal(rs.getInt("TARIFA_NORMAL"));
                jor1.setTarifa_economica(rs.getInt("TARIFA_ECONOMICA"));
                jor1.setTarifa_umbral(rs.getInt("TARIFA_UMBRAL"));
                jor1.setHrs_validacion(rs.getInt("HRS_VALIDACION"));
                jor1.setHrs_ofrecido_web(rs.getInt("HRS_OFRECIDO_WEB"));
                jor1.setHoraIniPicking(rs.getTime("ini_pick"));
                jor1.setHoraFinPicking(rs.getTime("fin_pick"));
                jor1.setFecha_picking(rs.getDate("FECHA_PICK"));
                ventana.getJornadas().add(jor1);
            }
            if ( !keyVentana.equalsIgnoreCase("") ) {
                horarios.add(ventana);
            }

        } catch (SQLException e) {
            logger.debug("Problema:"+ e);
            throw new CalendarioDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			releaseConnection();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}finally{
				rs=null;
				stm=null;
			}
		}
        return horarios;
    }
	
}
