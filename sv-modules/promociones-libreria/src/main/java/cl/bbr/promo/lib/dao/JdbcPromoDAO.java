package cl.bbr.promo.lib.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.irs.promolib.entity.LocalBoEntity;
import cl.bbr.irs.promolib.entity.MatrizSeccionEntity;
import cl.bbr.irs.promolib.entity.ProductoPromosEntity;
import cl.bbr.irs.promolib.entity.PromoSeccionEntity;
import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionDAOException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.PromoMedioPagoDTO;
import cl.bbr.promo.lib.exception.PromoDAOException;

//import cl.bbr.transactions.JdbcTransaccion;

public class JdbcPromoDAO implements PromoDAO {

    /**
     * Permite generar los eventos en un archivo log.
     */
    Logging logger = new Logging(this);

    /**
     * Permite la conexión con la base de datos.
     */
    Connection conn = null;

    /**
     * Permite el manejo de la transaccionalidad, para procesos de multiples
     * operaciones en la base de datos
     */
    JdbcTransaccion trx = null;

    int btoint(boolean valor) {
        if (valor == true) {
            return 1;
        } else {
            return 0;
        }

    }

    // ************ Métodos Privados *************** //

    /**
     * Obtiene la conexión
     * 
     * @return Connection
     * @throws SQLException 
     */
    private Connection getConnection() throws SQLException {

        if (conn == null) {
            conn = JdbcDAOFactory.getConexion();
        }
        return this.conn;

    }

    // ************ Métodos Publicos *************** //
    /**
     * Libera la conexión. Sólo si no es una conexión única, en cuyo caso no la
     * cierra.
     * 
     *  
     */
    private void releaseConnection() {
        if (trx == null) {
            try {
                if (conn != null) {
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
     * @param trx
     *            JdbcTransaccion
     * 
     * @throws EmpresasDAOException
     */
    public void setTrx(JdbcTransaccion trx) throws IrsPromocionDAOException {

        this.trx = trx;
        try {
            conn = trx.getConnection();
        } catch (DAOException e) {
            throw new IrsPromocionDAOException(e);
        }
    }

    /**
     * constructor
     */
    public JdbcPromoDAO() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#getAllPromoSeccion(int)
     */
    public List getAllPromoSeccion(int local) throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List result = new ArrayList();

        logger.debug("en getAllPromoSeccion:");
        try {

            String sql = " SELECT "
                    + " ID_PROMOSECCION, ID_LOCAL, ID_TIPO, COD_PROMO "
                    + " FROM BODBA.PR_PROMO_SECCION " + " WHERE ID_LOCAL = ? ";

            logger.debug("SQL: " + sql);
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, local);
            rs = stm.executeQuery();
            while (rs.next()) {
                PromoSeccionEntity ps = new PromoSeccionEntity();
                ps.setId_promoseccion(rs.getInt("ID_PROMOSECCION"));
                ps.setLocal(rs.getInt("ID_LOCAL"));
                ps.setTipo(rs.getInt("ID_TIPO"));
                ps.setCodigo(rs.getInt("COD_PROMO"));
                result.add(ps);
            }

        } catch (Exception e) {
            logger.debug("Problema:" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error(
                        "[Metodo] : getAllPromoSeccion - Problema SQL (close)",
                        e);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#getPromoByTipo(int, int)
     */
    public PromoSeccionEntity getPromoSeccion(int tipo, int local)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PromoSeccionEntity result = null;

        logger.debug("en getPromoSeccion:");
        try {

            String sql = " SELECT "
                    + " ID_PROMOSECCION, ID_LOCAL, ID_TIPO, COD_PROMO "
                    + " FROM BODBA.PR_PROMO_SECCION "
                    + " WHERE ID_LOCAL = ? AND ID_TIPO = ?";

            logger.debug("SQL: " + sql);
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setInt(1, local);
            stm.setInt(2, tipo);
            logger.debug("id_local:" + local);
            logger.debug("tipo:" + tipo);
            rs = stm.executeQuery();
            if (rs.next()) {
                result = new PromoSeccionEntity();
                result.setId_promoseccion(rs.getInt("ID_PROMOSECCION"));
                result.setLocal(rs.getInt("ID_LOCAL"));
                result.setTipo(rs.getInt("ID_TIPO"));
                result.setCodigo(rs.getInt("COD_PROMO"));
            }

        } catch (Exception e) {
            logger.debug("Problema:" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error(
                        "[Metodo] : getPromoSeccion - Problema SQL (close)", e);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#insPromoSeccion(cl.bbr.irs.promolib.entity.PromoSeccionEntity)
     */
    public void insPromoSeccion(PromoSeccionEntity promo)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        //long id_dir = param.getDir_id();
        try {
            String sql = "INSERT INTO " + " PR_PROMO_SECCION "
                    + " (id_local, id_tipo, cod_promo) " + " VALUES (?,?,?)";
            logger.debug("sql:" + sql);
            conn = this.getConnection();
            logger.debug("en insPromoSeccion(PromoSeccionEntity)");
            logger.debug("id_local:" + promo.getLocal());
            logger.debug("id_tipo:" + promo.getTipo());
            logger.debug("cod_promo:" + promo.getCodigo());

            stm = conn.prepareStatement(sql);

            stm.setInt(1, promo.getLocal());
            stm.setInt(2, promo.getTipo());
            stm.setInt(3, promo.getCodigo());

            int i = stm.executeUpdate();
            if (i > 0)
                logger.debug("PromoSeccion insertada!");

        } catch (Exception e) {
            logger.debug("Problema :" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {

                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error(
                        "[Metodo] : insPromoSeccion - Problema SQL (close)", e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#updPromoSeccion(cl.bbr.irs.promolib.entity.PromoSeccionEntity)
     */
    public int updPromoSeccion(PromoSeccionEntity promo)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        int i = 0;
        try {
            String sql = " UPDATE PR_PROMO_SECCION " + " SET cod_promo = ? "
                    + " WHERE id_tipo = ? " + " AND id_local = ? ";
            logger.debug("sql:" + sql);
            conn = this.getConnection();
            logger.debug("en updPromoSeccion");
            logger.debug("id_local:" + promo.getLocal());
            logger.debug("id_tipo:" + promo.getTipo());
            logger.debug("cod_promo:" + promo.getCodigo());

            stm = conn.prepareStatement(sql);
            stm.setInt(1, promo.getCodigo());
            stm.setInt(2, promo.getTipo());
            stm.setInt(3, promo.getLocal());

            i = stm.executeUpdate();
            if (i > 0) {
                logger.debug("PromoSeccion actualizada!");
            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.error("Error exception:" + e.getMessage());
            e.printStackTrace();
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error(
                        "[Metodo] : updPromoSeccion - Problema SQL (close)", e);
            }
        }
        return i;

    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#delPromoSeccion(cl.bbr.irs.promolib.entity.PromoSeccionEntity)
     */
    public void delPromoSeccion(PromoSeccionEntity promo)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        try {
            String sql = "DELETE FROM PR_PROMO_SECCION "
                    + " WHERE id_tipo = ? " + " AND id_local = ? ";
            logger.debug("sql:" + sql);
            conn = this.getConnection();
            logger.debug("en delPromoSeccion");
            logger.debug("id_tipo:" + promo.getTipo());
            logger.debug("id_local:" + promo.getLocal());

            stm = conn.prepareStatement(sql);
            stm.setInt(1, promo.getTipo());
            stm.setInt(2, promo.getLocal());

            int i = stm.executeUpdate();
            if (i > 0)
                logger.debug("PromoSeccion eliminada!");

        } catch (Exception e) {
            logger.debug("Problema :" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {

                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error(
                        "[Metodo] : delPromoSeccion - Problema SQL (close)", e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#getAllMatrizSeccion(int)
     */
    public List getAllMatrizSeccion(int local) throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List result = new ArrayList();

        logger.debug("en getAllMatrizSeccion:");
        try {

            String sql = " SELECT " + " ID_LOCAL, " + " ID_SECCION, "
                    + " LUNES, " + " MARTES, " + " MIERCOLES, " + " JUEVES, "
                    + " VIERNES, " + " SABADO, " + " DOMINGO, " + " ESP1, "
                    + " ESP2, " + " ESP3, " + " ESP4, " + " ESP5, " + " ESP6, "
                    + " ESP7, " + " ESP8 " + " FROM BODBA.PR_MATRIZ_SECCION "
                    + " WHERE ID_LOCAL = ? " + " ORDER BY ID_SECCION ASC ";

            logger.debug("SQL: " + sql);
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, local);
            rs = stm.executeQuery();
            while (rs.next()) {
                MatrizSeccionEntity mat = new MatrizSeccionEntity();

                mat.setLocal(rs.getInt("ID_LOCAL"));
                mat.setSeccion(rs.getInt("ID_SECCION"));
                mat.setLunes(rs.getInt("LUNES") == 1);
                mat.setMartes(rs.getInt("MARTES") == 1);
                mat.setMiercoles(rs.getInt("MIERCOLES") == 1);
                mat.setJueves(rs.getInt("JUEVES") == 1);
                mat.setViernes(rs.getInt("VIERNES") == 1);
                mat.setSabado(rs.getInt("SABADO") == 1);
                mat.setDomingo(rs.getInt("DOMINGO") == 1);

                mat.setEsp1(rs.getInt("ESP1") == 1);
                mat.setEsp2(rs.getInt("ESP2") == 1);
                mat.setEsp3(rs.getInt("ESP3") == 1);
                mat.setEsp4(rs.getInt("ESP4") == 1);
                mat.setEsp5(rs.getInt("ESP5") == 1);
                mat.setEsp6(rs.getInt("ESP6") == 1);
                mat.setEsp7(rs.getInt("ESP7") == 1);
                mat.setEsp8(rs.getInt("ESP8") == 1);

                result.add(mat);
            }

        } catch (Exception e) {
            logger.debug("Problema:" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger
                        .error(
                                "[Metodo] : getAllMatrizSeccion - Problema SQL (close)",
                                e);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#getMatrizBySeccion(int,
     *      int)
     */
    public MatrizSeccionEntity getMatrizSeccion(int seccion, int local)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        MatrizSeccionEntity mat = null;
        logger.debug("en getMatrizSeccion:");
        try {

            String sql = " SELECT " + " ID_LOCAL, " + " ID_SECCION, "
                    + " LUNES, " + " MARTES, " + " MIERCOLES, " + " JUEVES, "
                    + " VIERNES, " + " SABADO, " + " DOMINGO, " + " ESP1, "
                    + " ESP2, " + " ESP3, " + " ESP4, " + " ESP5, " + " ESP6, "
                    + " ESP7, " + " ESP8 " + " FROM BODBA.PR_MATRIZ_SECCION "
                    + " WHERE ID_LOCAL = ? AND ID_SECCION = ? "
                    + " ORDER BY ID_SECCION ASC ";

            logger.debug("SQL: " + sql);
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, local);
            stm.setLong(2, seccion);
            rs = stm.executeQuery();
            if (rs.next()) {
                mat = new MatrizSeccionEntity();

                mat.setLocal(rs.getInt("ID_LOCAL"));
                mat.setSeccion(rs.getInt("ID_SECCION"));
                mat.setLunes(rs.getInt("LUNES") == 1);
                mat.setMartes(rs.getInt("MARTES") == 1);
                mat.setMiercoles(rs.getInt("MIERCOLES") == 1);
                mat.setJueves(rs.getInt("JUEVES") == 1);
                mat.setViernes(rs.getInt("VIERNES") == 1);
                mat.setSabado(rs.getInt("SABADO") == 1);
                mat.setDomingo(rs.getInt("DOMINGO") == 1);

                mat.setEsp1(rs.getInt("ESP1") == 1);
                mat.setEsp2(rs.getInt("ESP2") == 1);
                mat.setEsp3(rs.getInt("ESP3") == 1);
                mat.setEsp4(rs.getInt("ESP4") == 1);
                mat.setEsp5(rs.getInt("ESP5") == 1);
                mat.setEsp6(rs.getInt("ESP6") == 1);
                mat.setEsp7(rs.getInt("ESP7") == 1);
                mat.setEsp8(rs.getInt("ESP8") == 1);

            }

        } catch (Exception e) {
            logger.debug("Problema:" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger
                        .error(
                                "[Metodo] : getMatrizSeccion - Problema SQL (close)",
                                e);
            }
        }
        return mat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#addMatrizSeccion(cl.bbr.irs.promolib.entity.MatrizSeccionEntity)
     */
    public void insMatrizSeccion(MatrizSeccionEntity matriz)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        //long id_dir = param.getDir_id();
        try {
            String sql = "INSERT INTO PR_MATRIZ_SECCION ( " + " ID_LOCAL, "
                    + " ID_SECCION, " + " LUNES, " + " MARTES, "
                    + " MIERCOLES, " + " JUEVES, " + " VIERNES, " + " SABADO, "
                    + " DOMINGO, " + " ESP1, " + " ESP2, " + " ESP3, "
                    + " ESP4, " + " ESP5, " + " ESP6, " + " ESP7, " + " ESP8 "
                    + " ) VALUES ("
                    + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
                    + ")";
            logger.debug("sql:" + sql);
            conn = this.getConnection();
            logger.debug("en insMatrizSeccion");
            //logger.debug(sql+"id_dir:"+id_dir);
            stm = conn.prepareStatement(sql);

            //stm.setLong(1, id_dir);
            stm.setInt(1, matriz.getLocal());
            stm.setInt(2, matriz.getSeccion());
            stm.setInt(3, btoint(matriz.isLunes()));
            stm.setInt(4, btoint(matriz.isMartes()));
            stm.setInt(5, btoint(matriz.isMiercoles()));
            stm.setInt(6, btoint(matriz.isJueves()));
            stm.setInt(7, btoint(matriz.isViernes()));
            stm.setInt(8, btoint(matriz.isSabado()));
            stm.setInt(9, btoint(matriz.isDomingo()));
            stm.setInt(10, btoint(matriz.isEsp1()));
            stm.setInt(11, btoint(matriz.isEsp2()));
            stm.setInt(12, btoint(matriz.isEsp3()));
            stm.setInt(13, btoint(matriz.isEsp4()));
            stm.setInt(14, btoint(matriz.isEsp5()));
            stm.setInt(15, btoint(matriz.isEsp6()));
            stm.setInt(16, btoint(matriz.isEsp7()));
            stm.setInt(17, btoint(matriz.isEsp8()));

            int i = stm.executeUpdate();
            if (i > 0)
                logger.debug("MatrizSeccion insertada!");

        } catch (Exception e) {
            logger.debug("Problema :" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger
                        .error(
                                "[Metodo] : insMatrizSeccion - Problema SQL (close)",
                                e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#updMatrizSeccion(cl.bbr.irs.promolib.entity.MatrizSeccionEntity)
     */
    public int updMatrizSeccion(MatrizSeccionEntity matriz)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        int i = 0;
        try {
            String sql = " UPDATE PR_MATRIZ_SECCION " + " SET  ID_LOCAL 		="
                    + matriz.getLocal() + " , ID_SECCION 	="
                    + matriz.getSeccion() + " , LUNES 		="
                    + btoint(matriz.isLunes()) + " , MARTES 		="
                    + btoint(matriz.isMartes()) + " , MIERCOLES 	="
                    + btoint(matriz.isMiercoles()) + " , JUEVES 		="
                    + btoint(matriz.isJueves()) + " , VIERNES 	="
                    + btoint(matriz.isViernes()) + " , SABADO 		="
                    + btoint(matriz.isSabado()) + " , DOMINGO 	="
                    + btoint(matriz.isDomingo()) + " , ESP1 		="
                    + btoint(matriz.isEsp1()) + " , ESP2 		="
                    + btoint(matriz.isEsp2()) + " , ESP3 		="
                    + btoint(matriz.isEsp3()) + " , ESP4 		="
                    + btoint(matriz.isEsp4()) + " , ESP5 		="
                    + btoint(matriz.isEsp5()) + " , ESP6 		="
                    + btoint(matriz.isEsp6()) + " , ESP7 		="
                    + btoint(matriz.isEsp7()) + " , ESP8 		="
                    + btoint(matriz.isEsp8()) + " WHERE ID_LOCAL 	="
                    + matriz.getLocal() + " AND ID_SECCION 	="
                    + matriz.getSeccion();
            logger.debug("sql:" + sql);
            conn = this.getConnection();
            logger.debug("en updMatrizSeccion");
            //logger.debug(sql+"id_dir:"+id_dir);
            stm = conn.prepareStatement(sql);
            //stm.setLong(1, id_dir);
            i = stm.executeUpdate();
            if (i > 0) {
                logger.debug("MatrizSeccion actualizada!");
            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.debug("Problema :" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger
                        .error(
                                "[Metodo] : updMatrizSeccion - Problema SQL (close)",
                                e);
            }
        }
        return i;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#delMatrizSeccion(cl.bbr.irs.promolib.entity.MatrizSeccionEntity)
     */
    public void delMatrizSeccion(MatrizSeccionEntity matriz)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        //long id_dir = param.getDir_id();
        try {
            String sql = "DELETE FROM PR_MATRIZ_SECCION "
                    + " WHERE ID_LOCAL 	= ? " + " AND ID_SECCION 	= ? ";
            logger.debug("sql:" + sql);
            conn = this.getConnection();
            logger.debug("en delMatrizSeccion");
            logger.debug("id_local:" + matriz.getLocal());
            logger.debug("id_seccion:" + matriz.getSeccion());

            stm = conn.prepareStatement(sql);

            //stm.setLong(1, id_dir);
            stm.setInt(1, matriz.getLocal());
            stm.setInt(2, matriz.getSeccion());

            int i = stm.executeUpdate();
            if (i > 0)
                logger.debug("MatrizSeccion eliminada!");

        } catch (Exception e) {
            logger.debug("Problema :" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {

                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger
                        .error(
                                "[Metodo] : delMatrizSeccion - Problema SQL (close)",
                                e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#getPromoById(int, int)
     */
    public PromocionEntity getPromocion(int codigo, int local)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PromocionEntity result = null;
        logger.debug("en getPromocion:");
        try {

            String sql = "SELECT COD_PROMO       ," + " ID_LOCAL             ,"
                    + " VERSION              ," + " TIPO_PROMO           ,"
                    + " FINI                 ," + " FFIN                 ,"
                    + " DESCR                ," + " CANT_MIN             ,"
                    + " MONTO_MIN            ," + " MONTO1               ,"
                    + " DESCUENTO1           ," + " MONTO2               ,"
                    + " DESCUENTO2           ," + " MONTO3               ,"
                    + " DESCUENTO3           ," + " MONTO4               ,"
                    + " DESCUENTO4           ," + " MONTO5               ,"
                    + " DESCUENTO5           ," + " FP1                  ,"
                    + " NUM_CUOTA1           ," + " TCP1                 ,"
                    + " BENEFICIO1           ," + " FP2                  ,"
                    + " NUM_CUOTA2           ," + " TCP2                 ,"
                    + " BENEFICIO2           ," + " FP3                  ,"
                    + " NUM_CUOTA3           ," + " TCP3                 ,"
                    + " BENEFICIO3           ," + " CONDICION1           ,"
                    + " CONDICION2           ," + " CONDICION3           ,"
                    + " PRORRATEO            ," + " RECUPERABLE          ,"
                    + " CANAL                ," + " SUSTITUIBLE          ,"
                    + " BANNER				  " + " FROM PR_PROMOCION "
                    + " WHERE COD_PROMO = ? AND ID_LOCAL = ? ";

            logger.debug("SQL: " + sql);
            logger.debug("codigo: " + codigo);
            logger.debug("local: " + local);
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, codigo);
            stm.setLong(2, local);
            rs = stm.executeQuery();
            if (rs.next()) {
                result = new PromocionEntity();

                result.setCodigo(rs.getInt("COD_PROMO"));
                result.setLocal(rs.getInt("ID_LOCAL"));
                result.setVersion(rs.getInt("VERSION"));
                result.setTipo(rs.getInt("TIPO_PROMO"));
                result.setFechaInicio(rs.getTimestamp("FINI"));
                result.setFechaTermino(rs.getTimestamp("FFIN"));
                result.setDescripcion(rs.getString("DESCR"));
                result.setMinCantidad(rs.getInt("CANT_MIN"));
                result.setMinMonto(rs.getLong("MONTO_MIN"));

                result.setTramo1Monto(rs.getLong("MONTO1"));
                result.setTramo1Dcto(rs.getInt("DESCUENTO1"));
                result.setTramo2Monto(rs.getLong("MONTO2"));
                result.setTramo2Dcto(rs.getInt("DESCUENTO2"));
                result.setTramo3Monto(rs.getLong("MONTO3"));
                result.setTramo3Dcto(rs.getInt("DESCUENTO3"));
                result.setTramo4Monto(rs.getLong("MONTO4"));
                result.setTramo4Dcto(rs.getInt("DESCUENTO4"));
                result.setTramo5Monto(rs.getLong("MONTO5"));
                result.setTramo5Dcto(rs.getInt("DESCUENTO5"));

                result.setBenef1FormaPago(rs.getInt("FP1"));
                result.setBenef1NroCuotas(rs.getInt("NUM_CUOTA1"));
                result.setBenef1TCP(rs.getInt("TCP1"));
                result.setBenef1Monto(rs.getLong("BENEFICIO1"));

                result.setBenef2FormaPago(rs.getInt("FP2"));
                result.setBenef2NroCuotas(rs.getInt("NUM_CUOTA2"));
                result.setBenef2TCP(rs.getInt("TCP2"));
                result.setBenef2Monto(rs.getLong("BENEFICIO2"));

                result.setBenef3FormaPago(rs.getInt("FP3"));
                result.setBenef3NroCuotas(rs.getInt("NUM_CUOTA3"));
                result.setBenef3TCP(rs.getInt("TCP3"));
                result.setBenef3Monto(rs.getLong("BENEFICIO3"));

                result.setCondicion1(rs.getInt("CONDICION1"));
                result.setCondicion2(rs.getInt("CONDICION2"));
                result.setCondicion3(rs.getInt("CONDICION3"));

                result.setFlagProrrateo(rs.getInt("PRORRATEO"));
                result.setFlagRecuperable(rs.getInt("RECUPERABLE"));
                result.setCanal(rs.getInt("CANAL"));
            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.debug("Problema:" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : getPromocion - Problema SQL (close)",
                        e);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#addPromocion(cl.bbr.irs.promolib.entity.PromocionEntity)
     */
    public void insPromocion(PromocionEntity promo)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;

        try {
            String sql = "INSERT INTO PR_PROMOCION ( " + " COD_PROMO, "
                    + " ID_LOCAL, " + " VERSION, " + " TIPO_PROMO, "
                    + " FINI, " + " FFIN, " + " DESCR, " + " CANT_MIN, "
                    + " MONTO_MIN, " + " MONTO1, DESCUENTO1, "
                    + " MONTO2, DESCUENTO2, " + " MONTO3, DESCUENTO3, "
                    + " MONTO4, DESCUENTO4, " + " MONTO5, DESCUENTO5, "
                    + " FP1, NUM_CUOTA1, TCP1, BENEFICIO1, "
                    + " FP2, NUM_CUOTA2, TCP2, BENEFICIO2, "
                    + " FP3, NUM_CUOTA3, TCP3, BENEFICIO3, "
                    + " CONDICION1, CONDICION2, CONDICION3, "
                    + " PRORRATEO, RECUPERABLE, " + " CANAL ) VALUES ( "
                    + promo.getCodigo() + " , " + promo.getLocal() + " , "
                    + promo.getVersion() + " , " + promo.getTipo() + " , "
                    + " '"
                    + Formatos.frmFechaHoraByDate(promo.getFechaInicio())
                    + ".000000', " + " '"
                    + Formatos.frmFechaHoraByDate(promo.getFechaTermino())
                    + ".000000', " + " '" + promo.getDescripcion() + "' , "
                    + promo.getMinCantidad() + " , " + promo.getMinMonto()
                    + " , " + promo.getTramo1Monto() + ", "
                    + promo.getTramo1Dcto() + ", " + promo.getTramo2Monto()
                    + ", " + promo.getTramo2Dcto() + ", "
                    + promo.getTramo3Monto() + ", " + promo.getTramo3Dcto()
                    + ", " + promo.getTramo4Monto() + ", "
                    + promo.getTramo4Dcto() + ", " + promo.getTramo5Monto()
                    + ", " + promo.getTramo5Dcto() + ", "
                    + promo.getBenef1FormaPago() + ", "
                    + promo.getBenef1NroCuotas() + ", " + promo.getBenef1TCP()
                    + ", " + promo.getBenef1Monto() + ", "
                    + promo.getBenef2FormaPago() + ", "
                    + promo.getBenef2NroCuotas() + ", " + promo.getBenef2TCP()
                    + ", " + promo.getBenef2Monto() + ", "
                    + promo.getBenef3FormaPago() + ", "
                    + promo.getBenef3NroCuotas() + ", " + promo.getBenef3TCP()
                    + ", " + promo.getBenef3Monto() + ", "
                    + promo.getCondicion1() + ", " + promo.getCondicion2()
                    + ", " + promo.getCondicion3() + ", "
                    + promo.getFlagProrrateo() + " ,"
                    + promo.getFlagRecuperable() + ", " + promo.getCanal()
                    + " ) ";

            conn = this.getConnection();
            logger.debug("en insPromocion");
            logger.debug("SQL:" + sql);
            stm = conn.prepareStatement(sql);

            int i = stm.executeUpdate();
            if (i > 0)
                logger.debug("Promocion insertada!");

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.debug("Problema :" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : insPromocion - Problema SQL (close)",
                        e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#updPromocion(cl.bbr.irs.promolib.entity.PromocionEntity)
     */
    public int updPromocion(PromocionEntity promo)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        int i = 0;
        try {

            String sql = "UPDATE PR_PROMOCION " + " SET COD_PROMO 	= "
                    + promo.getCodigo() + " , ID_LOCAL 		= " + promo.getLocal()
                    + " , VERSION 		= " + promo.getVersion()
                    + " , TIPO_PROMO 		= " + promo.getTipo() + " , FINI 			= '"
                    + Formatos.frmFechaHoraByDate(promo.getFechaInicio())
                    + ".000000' " + " , FFIN 			= '"
                    + Formatos.frmFechaHoraByDate(promo.getFechaTermino())
                    + ".000000' " + " , DESCR 			= '" + promo.getDescripcion()
                    + "' " + " , CANT_MIN 		= " + promo.getMinCantidad()
                    + " , MONTO_MIN 		= " + promo.getMinMonto()
                    + " , MONTO1 			= " + promo.getTramo1Monto()
                    + " , DESCUENTO1 		= " + promo.getTramo1Dcto()
                    + " , MONTO2 			= " + promo.getTramo2Monto()
                    + " , DESCUENTO2 		= " + promo.getTramo2Dcto()
                    + " , MONTO3 			= " + promo.getTramo3Monto()
                    + " , DESCUENTO3 		= " + promo.getTramo3Dcto()
                    + " , MONTO4 			= " + promo.getTramo4Monto()
                    + " , DESCUENTO4 		= " + promo.getTramo4Dcto()
                    + " , MONTO5 			= " + promo.getTramo5Monto()
                    + " , DESCUENTO5 		= " + promo.getTramo5Dcto()
                    + " , FP1 			= " + promo.getBenef1FormaPago()
                    + " , NUM_CUOTA1 		= " + promo.getBenef1NroCuotas()
                    + " , TCP1 			= " + promo.getBenef1TCP()
                    + " , BENEFICIO1 		= " + promo.getBenef1Monto()
                    + " , FP2 			= " + promo.getBenef2FormaPago()
                    + " , NUM_CUOTA2 		= " + promo.getBenef2NroCuotas()
                    + " , TCP2 			= " + promo.getBenef2TCP()
                    + " , BENEFICIO2 		= " + promo.getBenef2Monto()
                    + " , FP3 			= " + promo.getBenef3FormaPago()
                    + " , NUM_CUOTA3 		= " + promo.getBenef3NroCuotas()
                    + " , TCP3 			= " + promo.getBenef3TCP()
                    + " , BENEFICIO3 		= " + promo.getBenef3Monto()
                    + " , CONDICION1 		= " + promo.getCondicion1()
                    + " , CONDICION2 		= " + promo.getCondicion2()
                    + " , CONDICION3 		= " + promo.getCondicion3()
                    + " , PRORRATEO 		= " + promo.getFlagProrrateo()
                    + " , RECUPERABLE 	= " + promo.getFlagRecuperable()
                    + " , CANAL 			= " + promo.getCanal()
                    + " WHERE cod_promo = " + promo.getCodigo()
                    + " AND id_local = " + promo.getLocal();

            conn = this.getConnection();
            logger.debug("en updPromocion");
            logger.debug("SQL:" + sql);
            stm = conn.prepareStatement(sql);
            i = stm.executeUpdate();
            if (i > 0) {
                logger.debug("Promocion actualizada!");
            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : updPromocion - Problema SQL (close)",
                        e);
            }
        }
        return i;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#delPromocion(cl.bbr.irs.promolib.entity.PromocionEntity)
     */
    public void delPromocion(PromocionEntity promo)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        try {
            String sql = "DELETE FROM PR_PROMOCION "
                    + " WHERE cod_promo = ? AND id_local = ? ";

            conn = this.getConnection();
            logger.debug("en delPromocion");
            logger.debug("SQL:" + sql);
            stm = conn.prepareStatement(sql);
            logger.debug("cod_promo:" + promo.getCodigo());
            logger.debug("id_local:" + promo.getLocal());

            stm.setInt(1, promo.getCodigo());
            stm.setInt(2, promo.getLocal());

            int i = stm.executeUpdate();
            if (i > 0)
                logger.debug("Promocion eliminada!");

        } catch (Exception e) {
            logger.debug("Problema :" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : delPromocion - Problema SQL (close)",
                        e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#getPromocionProducto(int,
     *      int)
     */
    public ProductoPromosEntity getPromocionProducto(int id_producto,
            int id_local) throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        logger.debug("en getPromocionProducto:");
        ProductoPromosEntity producto_promo = null;
        try {

            String sql = " SELECT pp.id_prodpromos id_prodpromos, "
                    + " pp.id_producto id_producto, "
                    + " pp.id_local id_local, " + " pp.cod_promo1 cod_promo1, "
                    + " pp.cod_promo2 cod_promo2, "
                    + " pp.cod_promo3 cod_promo3, " + " b.cod_barra ean13 "
                    + " FROM PR_PRODUCTO_PROMOS  pp "
                    + " JOIN BO_CODBARRA b ON b.id_producto = pp.id_producto  "
                    + " WHERE pp.id_local = ? " + " AND pp.id_producto = ? ";

            logger.debug("SQL: " + sql);
            logger.debug("id_local: " + id_local);
            logger.debug("id_producto: " + id_producto);

            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setInt(1, id_local);
            stm.setInt(2, id_producto);
            rs = stm.executeQuery();

            if (rs.next()) {
                producto_promo = new ProductoPromosEntity();
                producto_promo.setId_prodpromos(rs.getInt("id_prodpromos"));
                producto_promo.setId_producto(rs.getInt("id_producto"));
                producto_promo.setId_local(rs.getInt("id_local"));
                producto_promo.setPromo1(rs.getInt("cod_promo1"));
                producto_promo.setPromo2(rs.getInt("cod_promo2"));
                producto_promo.setPromo3(rs.getInt("cod_promo3"));
                producto_promo.setEan13(rs.getString("ean13"));

            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.debug("Problema:" + e.getMessage());
            e.printStackTrace();
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger
                        .error(
                                "[Metodo] : getPromocionProducto - Problema SQL (close)",
                                e);
            }
        }
        return producto_promo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#addPromocionProducto(cl.bbr.irs.promolib.entity.ProductoPromosEntity)
     */
    public void insPromocionProducto(ProductoPromosEntity producto)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        try {
            String sql = "INSERT INTO PR_PRODUCTO_PROMOS "
                    + " (id_producto, id_local, cod_promo1, cod_promo2, cod_promo3)"
                    + " VALUES (?,?,?,?,?) ";
            conn = this.getConnection();
            logger.debug("en insPromocionProducto");
            logger.debug("SQL:" + sql);
            logger.debug("id_producto:" + producto.getId_producto());
            logger.debug("id_local:" + producto.getId_local());
            logger.debug("cod_promo1:" + producto.getPromo1());
            logger.debug("cod_promo2:" + producto.getPromo2());
            logger.debug("cod_promo3:" + producto.getPromo3());

            stm = conn.prepareStatement(sql);
            stm.setInt(1, producto.getId_producto());
            stm.setInt(2, producto.getId_local());
            stm.setInt(3, producto.getPromo1());
            stm.setInt(4, producto.getPromo2());
            stm.setInt(5, producto.getPromo3());

            int i = stm.executeUpdate();
            if (i > 0)
                logger.debug("ProductosPromos insertada!");

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.debug("Problema :" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger
                        .error(
                                "[Metodo] : insPromocionProducto - Problema SQL (close)",
                                e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#updPromocionProducto(cl.bbr.irs.promolib.entity.ProductoPromosEntity)
     */
    public int updPromocionProducto(ProductoPromosEntity producto)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        int i = 0;
        try {
            String sql = "UPDATE PR_PRODUCTO_PROMOS " + " SET id_producto = "
                    + producto.getId_producto() + " , id_local = "
                    + producto.getId_local();
            if (producto.getPromo1() >= 0) {
                sql += " , cod_promo1 = " + producto.getPromo1();
            }
            if (producto.getPromo2() >= 0) {
                sql += " , cod_promo2 = " + producto.getPromo2();
            }
            if (producto.getPromo3() >= 0) {
                sql += " , cod_promo3 = " + producto.getPromo3();
            }
            sql += " WHERE id_producto = " + producto.getId_producto()
                    + " AND id_local = " + producto.getId_local();
            conn = this.getConnection();
            logger.debug("en updPromocionProducto");
            logger.debug("SQL:" + sql);

            stm = conn.prepareStatement(sql);
            i = stm.executeUpdate();
            if (i > 0) {
                logger.debug("ProductoPromos actualizada!");
            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger
                        .error(
                                "[Metodo] : updPromocionProducto - Problema SQL (close)",
                                e);
            }
        }
        return i;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#delPromocionProducto(cl.bbr.irs.promolib.entity.ProductoPromosEntity)
     */
    public void delPromocionProducto(ProductoPromosEntity producto)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;

        try {
            String sql = "DELETE FROM PR_PRODUCTO_PROMOS "
                    + " WHERE id_producto = ? " + " AND id_local = ? ";
            conn = this.getConnection();
            logger.debug("en delPromocionProducto");
            logger.debug("SQL:" + sql);

            stm = conn.prepareStatement(sql);
            stm.setInt(1, producto.getId_producto());
            stm.setInt(2, producto.getId_local());

            int i = stm.executeUpdate();
            if (i > 0)
                logger.debug("ProductoPromo eliminada!");

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.debug("Problema :" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger
                        .error(
                                "[Metodo] : delPromocionProducto - Problema SQL (close)",
                                e);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#getLocalBO(int)
     */
    public LocalBoEntity getLocalBySap(String local_sap)
            throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        logger.debug("en getLocalBySap:");
        LocalBoEntity local_bo = null;
        try {

            String sql = " SELECT id_local, " + " cod_local, " + " nom_local, "
                    + " cod_local_pos id_local_bop," + " id_local_sap "
                    + " FROM BO_LOCALES " + " WHERE cod_local = ? ";

            logger.debug("SQL: " + sql);
            logger.debug("local sap: " + local_sap);
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setString(1, local_sap);
            rs = stm.executeQuery();

            if (rs.next()) {
                local_bo = new LocalBoEntity();
                local_bo.setId_local(rs.getInt("id_local"));
                local_bo.setCod_local(rs.getString("cod_local"));
                local_bo.setNom_local(rs.getString("nom_local"));
                local_bo.setId_local_bop(rs.getInt("id_local_bop"));
                local_bo.setId_local_sap(rs.getInt("id_local_sap"));
            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.debug("Problema:" + e.getMessage());
            e.printStackTrace();
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : getLocalBySap - Problema SQL (close)",
                        e);
            }
        }
        return local_bo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.irs.promolib.dao.IrsPromocionesDAO#getProductoBO(String)
     */
    public int getProductoBO(String ean13) throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        logger.debug("en getProductoBO:");
        int id_producto = -1;
        try {

            String sql = " SELECT id_producto " + " FROM BO_CODBARRA "
                    + " WHERE cod_barra = '" + ean13 + "' ";

            logger.debug("SQL: " + sql);
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            rs = stm.executeQuery();
            if (rs.next()) {
                id_producto = rs.getInt("id_producto");
            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.debug("Problema:" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : getProductoBO - Problema SQL (close)",
                        e);
            }
        }
        return id_producto;
    }
    
    /**
     * @param seccion
     * @return
     * @throws IrsPromocionDAOException
     */
    public boolean isAfectoDescColaborador(int seccion) throws IrsPromocionDAOException {
        
        ResultSet rs = null;
        PreparedStatement stm = null;

        int count = 0;

        try {
        	conn = this.getConnection();
            stm = conn
                    .prepareStatement("SELECT COUNT(*) FROM FO_SECCION_EXCLUIDA_DESCUENTO WHERE SED_ID=? WITH UR");
            stm.setInt(1, seccion);
            rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e1) {
                logger.error("[Metodo] : isAfectoDescColaborador - Problema SQL (close)",
                        e1);
            }
        }

        boolean result = true;
        if (count > 0)
            result = false;

        return result;
    }

    /**
     * @param rut
     * @return
     * @throws IrsPromocionDAOException
     */
    public long getComprasAcumuladas(long rut) throws IrsPromocionDAOException {
        
        ResultSet rs = null;
        PreparedStatement stm = null;
        
        long monto = 0;
        try {
        	conn = this.getConnection();
            stm = conn
                    .prepareStatement("SELECT sum(POS_MONTO_FP) FROM BODBA.BO_PEDIDOS JOIN BODBA.bo_trx_mp ON BODBA.BO_PEDIDOS.ID_PEDIDO = BODBA.BO_TRX_MP.ID_PEDIDO WHERE MONTH(BODBA.BO_TRX_MP.FCREACION)= MONTH(CURRENT TIMESTAMP) AND YEAR(BODBA.BO_TRX_MP.FCREACION)= YEAR(CURRENT TIMESTAMP) AND BODBA.BO_PEDIDOS.ID_ESTADO IN (8,9,10) AND RUT_CLIENTE=? WITH UR");
            stm.setLong(1, rut);
            rs = stm.executeQuery();
            if (rs.next()) {
                monto = rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e1) {
                logger.error("[Metodo] : getComprasAcumuladas - Problema SQL (close)",
                        e1);
            }
        }

        return monto;
    }

    /**
     * @return
     * @throws IrsPromocionDAOException
     */
    public double getDescTramo1() throws IrsPromocionDAOException {
        
        ResultSet rs = null;
        PreparedStatement stm = null;

        double monto=0;
        try {
        	conn = this.getConnection();
            stm = conn
                    .prepareStatement("SELECT  WITH UR");
            rs = stm.executeQuery();
            if (rs.next()) {
                monto = rs.getDouble(1);
            }
        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e1) {
                logger.error("[Metodo] : getDescTramo1 - Problema SQL (close)",
                        e1);
            }
        }

        return monto;
    }

    /* (non-Javadoc)
     * @see cl.bbr.promo.lib.dao.PromoDAO#getPromoMedioPagoByMPJmcl(java.lang.String, int)
     */
    public PromoMedioPagoDTO getPromoMedioPagoByMPJmcl(String fpago, int cuotas) throws PromoDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        PromoMedioPagoDTO result = new PromoMedioPagoDTO();

        
        //logger.debug("en getPromoMedioPagoByJmp:");
        try {
        	conn = this.getConnection();
            String sql = "SELECT mp_promo, mp_jmcl , mp_jmcl_ncuotas " +
            		"FROM pr_medio_pago " +
            		"WHERE mp_jmcl= ? " +
            		"AND mp_jmcl_ncuotas = ? " +
            		"WITH UR";

            stm = conn.prepareStatement(sql);
            stm.setString(1, fpago);
            stm.setInt(2, cuotas);
            rs = stm.executeQuery();
            if (rs.next()) {
                result.setMp_jmcl(rs.getString("mp_jmcl"));
                result.setMp_promo(rs.getString("mp_promo"));
                result.setMp_jmcl_ncuotas(rs.getInt("mp_jmcl_ncuotas"));
            }

        } catch (SQLException e) {
            throw new PromoDAOException(String.valueOf(e.getErrorCode()), e);
        }
        finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : getProductoBO - Problema SQL (close)",
                        e);
            }
        }
        return result;
    }

    public String getRubroBO(long idProd) throws IrsPromocionDAOException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        logger.debug("en getRubroBO:");
        String id_rubro = "";
        try {

            String sql = " select  substr(BP.ID_CATPROD, 3, 2) as ID_RUBRO from    FO_PRODUCTOS FP inner join BO_PRODUCTOS BP on BP.ID_PRODUCTO=FP.PRO_ID_BO INNER JOIN BO_CATPROD BC ON BC.ID_CATPROD=BP.ID_CATPROD WHERE bp.id_producto = ? ";
            logger.debug("SQL: " + sql);
            conn = this.getConnection();
            stm = conn.prepareStatement(sql + " WITH UR");
            stm.setLong(1, idProd);
            rs = stm.executeQuery();
            if (rs.next()) {
            	id_rubro = rs.getString("ID_RUBRO");
            }

        } catch (SQLException e) {
            logger.error("SQLException!! ErrorCode: " + e.getErrorCode()
                    + " , SQLState: " + e.getSQLState() + " getMessage:"
                    + e.getMessage());
            throw new IrsPromocionDAOException(e);
        } catch (Exception e) {
            logger.debug("Problema:" + e);
            throw new IrsPromocionDAOException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                releaseConnection();
            } catch (SQLException e) {
                logger.error("[Metodo] : getProductoBO - Problema SQL (close)",
                        e);
            }
        }
        return id_rubro;
    }
    
}
