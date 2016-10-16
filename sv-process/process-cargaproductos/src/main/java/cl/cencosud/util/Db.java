
package cl.cencosud.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Local;
import cl.cencosud.lucene.util.Producto;
import cl.cencosud.procesos.Cargar;

/**
 * @author jdroguett
 * 
 * Preferences - Java - Code Style - Code Templates
 */
public class Db {

	static {
		LogUtil.initLog4J();
	}
	private static Logger logger = Logger.getLogger(Db.class);
    public static Connection conexion(String user, String password, String driver, String url) throws Exception {

        Connection con = null;
        try {

            try {
                Class.forName(driver).newInstance();
            } catch (ClassNotFoundException e) {
                throw new Exception("Error: El driver no se encuentra");
            }
            Properties p = new Properties();
            p.setProperty("user", user);
            p.setProperty("password", password);
            
            if ((con = DriverManager.getConnection(url, p)) == null) {
                throw new Exception("Error: la conexion es nula");
            }
            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        } catch (Exception e) {
            System.out.println("Error al obtener nueva conexión: " + e.getMessage());
            throw new Exception("Error al obtener nueva conexión: " + e.getMessage());
        }
        return con;
    }

    /**
     * 
     * @param con
     * @return
     * @throws SQLException
     */
    public static List locales(Connection con) throws SQLException {

        List locales = new ArrayList();
        String sql = "select ID_LOCAL, COD_LOCAL, NOM_LOCAL from bodba.bo_locales";
        PreparedStatement ps = con.prepareStatement(sql + " with ur ");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Local local = new Local();
            local.setId(rs.getInt("ID_LOCAL"));
            local.setCodigo(rs.getString("COD_LOCAL"));
            local.setNombre(rs.getString("NOM_LOCAL"));
            locales.add(local);
        }
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        System.out.println("Locales cargado : " + locales.size());
        return locales;
    }

    /**
     * 
     * @param con
     * @param codigo
     * @return Cero si no encuentra codigo
     * @throws SQLException
     */
    public static int getLocalId(Connection con, String codigoLocal) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select ID_LOCAL from bodba.bo_locales where COD_LOCAL = ?";
            ps = con.prepareStatement(sql + " with ur ");                     
            
            ps.setString(1, codigoLocal);
            rs = ps.executeQuery();
            int localId = 0;
            if (rs.next()) {
                localId = rs.getInt("ID_LOCAL");
            }
            return localId;
        } finally {
            close(rs, ps);
        }
    }

    public static PreparedStatement preparedStatement(Connection con, String sql) throws SQLException {

        return con.prepareStatement(sql);
    }

    // /////////lucene
    /**
     * Entrega s�lo productos publicados por local
     * 
     * @param con
     * @return
     * @throws SQLException
     */
    public static List productosPub(Connection con) throws SQLException {

        long ini = System.currentTimeMillis();
        String sql = "select distinct pro.pro_id, pro_id_bo, pro.pro_cod_sap, pro.pro_tipo_producto, pro.pro_des_corta, "
                + "sub.cat_id, sub.cat_nombre, pro_tipre, pro_imagen_minificha, pre.pre_valor, pro_unidad_medida, pre_loc_id, mar.mar_nombre, mar.mar_id, "
                + "PRO_INTER_VALOR, PRO_INTER_MAX, uni.uni_desc, pro_nota, pro_particionable, pro_particion   "
                + "from fodba.fo_productos pro                                                                "
                + "inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id                     "
                + "inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id                              "
                + "inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id                     "
                + "inner join fodba.fo_productos_categorias procat on procat.prca_pro_id = pro.pro_id         "
                + "inner join fodba.fo_catsubcat catsub on catsub.subcat_id = procat.prca_cat_id              "
                + "inner join fodba.fo_categorias sub on sub.cat_id = catsub.subcat_id                        "
                + "inner join fodba.fo_categorias cat on cat.cat_id = catsub.cat_id                           "
                + " where sub.cat_estado = 'A' and cat.cat_estado = 'A' and pro.pro_estado = 'A'              "
                + " and pre.pre_estado = 'A' and pro_inter_valor > 0 ";

        PreparedStatement ps = con.prepareStatement(sql + " with ur ");
        ResultSet rs = ps.executeQuery();

        List productos = new ArrayList();

        while (rs.next()) {
            Producto pro = new Producto();
            pro.setIdFo(rs.getInt("pro_id"));
            pro.setIdBo(rs.getInt("pro_id_bo"));
            pro.setCodigo(rs.getString("pro_cod_sap"));
            pro.setTipo(rs.getString("pro_tipo_producto"));
            pro.setDescripcion(rs.getString("pro_des_corta"));
            pro.setCategoriaId(rs.getInt("cat_id"));
            pro.setCategoria(rs.getString("cat_nombre"));
            pro.setUnidadMedida(rs.getString("pro_tipre"));
            pro.setImagenMiniFicha(rs.getString("pro_imagen_minificha"));
            pro.setCantidadEnEmpaque(rs.getBigDecimal("pro_unidad_medida"));
            pro.setMarca(rs.getString("mar_nombre"));
            pro.setMarcaId(rs.getInt("mar_id"));
            pro.setCantidadMinima(rs.getBigDecimal("PRO_INTER_VALOR"));
            pro.setCantidadMaxima(rs.getBigDecimal("PRO_INTER_MAX"));
            pro.setUnidadNombre(rs.getString("uni_desc"));
            pro.setConNota("S".equals(rs.getString("pro_nota")));
            pro.setEsParticionable(rs.getString("pro_particionable"));
            pro.setParticion(rs.getInt("pro_particion"));
            productos.add(pro);
        }
        System.out.println("[productosPub - Productos cargados desde la base de datos ] : " + productos.size() + ". Tiempo lectura BD productos: "
                + Cargar.calculaTiempo(ini));
        return productos;
    }

    /**
     * Entrega todos los productos por local
     * 
     * @param con
     * @return
     * @throws SQLException
     */
    public static List productosConEstado(Connection con) throws SQLException {

        long ini = System.currentTimeMillis();
        List productoList = new ArrayList();
        System.out.println("-- inicia obtencion de productos");

        String sqlProducto = "select distinct pro.pro_id, pro_id_bo, pro.pro_cod_sap, pro.pro_tipo_producto, pro.pro_des_corta, pro.pro_des_larga, pro_tipre, pro_imagen_minificha, pro_unidad_medida, PRO_INTER_VALOR, PRO_INTER_MAX, pro_estado, pro_nota, pro_particionable, pro_particion, "
                + "bop.des_corta, mar.mar_nombre, mar.mar_id, bop.des_larga, uni.uni_desc "
                + "from fodba.fo_productos pro "
                + "inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id "
                + "inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id "
                + "join BODBA.BO_PRODUCTOS bop on pro.PRO_ID_BO = bop.ID_PRODUCTO where pro_inter_valor > 0 AND pro_estado='A' WITH UR";
        PreparedStatement productoStatement = con.prepareStatement(sqlProducto);

        String sqlProductoLocal = "SELECT pre.pre_valor, pre.pre_tienestock, pre_loc_id, pre_estado " + "FROM fodba.fo_precios_locales pre "
                + "WHERE pre_pro_id = ? " + "AND pre_estado='A' " + "WITH UR";
        PreparedStatement productoLocalStatement = con.prepareStatement(sqlProductoLocal);

        String sqlCategoriaProducto = "select cat1.cat_nombre cat_nombre1, cat2.cat_nombre cat_nombre2, cat3.cat_nombre cat_nombre3 "
                + "from FO_PRODUCTOS prod " + "join FO_PRODUCTOS_CATEGORIAS procat ON procat.PRCA_PRO_ID = prod.PRO_ID "
                + "join fo_categorias cat1 on procat.PRCA_CAT_ID = cat1.CAT_ID " + "join FO_CATSUBCAT subcat1 ON subcat1.SUBCAT_ID = cat1.CAT_ID "
                + "join fo_categorias cat2 on subcat1.CAT_ID = cat2.CAT_ID " + "join FO_CATSUBCAT subcat2 ON subcat2.SUBCAT_ID = cat2.CAT_ID "
                + "join fo_categorias cat3 on subcat2.CAT_ID = cat3.CAT_ID " + "where cat1.cat_estado='A' " + "and cat2.cat_estado='A' "
                + "and cat3.cat_estado='A' " + "and prca_estado='A' " + "and pro_id = ? " + "WITH UR";
        PreparedStatement categoriaProductoStatement = con.prepareStatement(sqlCategoriaProducto);

        ResultSet productoResultSet = productoStatement.executeQuery();
        while (productoResultSet.next()) {
            categoriaProductoStatement.setInt(1, productoResultSet.getInt("pro_id"));
            ResultSet categoriaProductoResultSet = categoriaProductoStatement.executeQuery();

            productoLocalStatement.setInt(1, productoResultSet.getInt("pro_id"));
            ResultSet productoLocalResultSet = productoLocalStatement.executeQuery();

            Producto producto = new Producto();
            producto.setIdFo(productoResultSet.getInt("pro_id"));
            producto.setIdBo(productoResultSet.getInt("pro_id_bo"));
            producto.setCodigo(productoResultSet.getString("pro_cod_sap"));
            producto.setTipo(productoResultSet.getString("pro_tipo_producto"));
            if (productoResultSet.getString("des_larga") == null)
                producto.setDescripcionBOLarga("");
            else
                producto.setDescripcionBOLarga(productoResultSet.getString("des_larga"));

            if (productoResultSet.getString("des_corta") == null)
                producto.setDescripcionBOCorta("");
            else
                producto.setDescripcionBOCorta(productoResultSet.getString("des_corta"));
            if (productoResultSet.getString("pro_des_corta") == null) {
                producto.setDescripcion("");
            } else {
                producto.setDescripcion(productoResultSet.getString("pro_des_corta"));
            }
            if(productoResultSet.getString("pro_des_larga") == null) {
                producto.setDescripcionLarga("");
            } else {
                producto.setDescripcionLarga(productoResultSet.getString("pro_des_larga"));
            }
            
            producto.setDescripcionCompleta(productoResultSet.getString("pro_tipo_producto") + " " + productoResultSet.getString("pro_des_corta") + " " + productoResultSet.getString("mar_nombre"));
            while (categoriaProductoResultSet.next()) {
                //logger.debug("cat_id:"+categoriaResult.getInt("cat_id"));
                producto.addCategoria(categoriaProductoResultSet.getString("cat_nombre1"));
                producto.addCategoria(categoriaProductoResultSet.getString("cat_nombre2"));
                producto.addCategoria(categoriaProductoResultSet.getString("cat_nombre3"));
            }

            producto.setUnidadMedida(productoResultSet.getString("pro_tipre"));
            producto.setImagenMiniFicha(productoResultSet.getString("pro_imagen_minificha"));

            producto.setCantidadEnEmpaque(productoResultSet.getBigDecimal("pro_unidad_medida"));

            producto.setMarca(productoResultSet.getString("mar_nombre"));
            producto.setMarcaId(productoResultSet.getInt("mar_id"));
            producto.setCantidadMinima(productoResultSet.getBigDecimal("PRO_INTER_VALOR"));
            producto.setCantidadMaxima(productoResultSet.getBigDecimal("PRO_INTER_MAX"));
            producto.setUnidadNombre(productoResultSet.getString("uni_desc"));
            producto.setConNota("S".equals(productoResultSet.getString("pro_nota")));
            producto.setEsParticionable(productoResultSet.getString("pro_particionable"));
            producto.setParticion(productoResultSet.getInt("pro_particion"));

            HashMap tieneStock = new HashMap();
            while (productoLocalResultSet.next()) {
                producto.addPrecio(productoLocalResultSet.getInt("pre_loc_id"), productoLocalResultSet.getBigDecimal("pre_valor"));
                producto.addPublicado(productoLocalResultSet.getInt("pre_loc_id"), true);
                Boolean stock = Boolean.FALSE;
                if (productoLocalResultSet.getInt("pre_tienestock") > 0){
                    stock = Boolean.TRUE;
                }
                tieneStock.put(new Integer(productoLocalResultSet.getInt("pre_loc_id")), stock);
                producto.getLocales().add(String.valueOf(productoLocalResultSet.getInt("pre_loc_id")));
            }
            producto.setTieneStock(tieneStock);
            productoList.add(producto);

        }
        return productoList;

    }

    /**
     * @param con
     * @return
     * @throws SQLException
     */
    public static List productosAutocompletar(Connection con) throws SQLException {

        String sqlProducto = "select distinct pro.pro_tipo_producto, pro.pro_des_corta, " + "bop.des_corta, mar.mar_nombre, bop.des_larga "
                + "from fodba.fo_productos pro " + "inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id "
                + "inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id "
                + "join BODBA.BO_PRODUCTOS bop on pro.PRO_ID_BO = bop.ID_PRODUCTO where pro_inter_valor > 0 AND pro_estado='A' WITH UR";
        PreparedStatement productoStatement = con.prepareStatement(sqlProducto);

        ResultSet productoResultSet = productoStatement.executeQuery();

        List productos = new ArrayList();

        while (productoResultSet.next()) {
            Producto producto = new Producto();
            producto.setCodigo(productoResultSet.getString("pro_cod_sap"));
            producto.setTipo(productoResultSet.getString("pro_tipo_producto"));
            producto.setDescripcion(productoResultSet.getString("pro_des_corta"));
            producto.setMarca(productoResultSet.getString("mar_nombre"));

            productos.add(producto);
        }
        System.out.println("productosAutocompletar - Productos cargados desde la base de datos: " + productos.size());

        try {
            productoResultSet.close();
        } catch (Exception e) {
            System.out.println("Problema SQL close " + e.getMessage());
        }
        return productos;

    }

    /**
     * 
     * @param con
     * @return
     * @throws SQLException
     */
    public static List categorias(Connection con) throws SQLException {
        List lista = new ArrayList();
        String sql = "select distinct sub.cat_nombre                                                  "
                + "from fodba.fo_categorias sub                                                         "
                + "inner join fodba.fo_catsubcat cs on cs.subcat_id = sub.cat_id                        "
                + "inner join fodba.fo_categorias cat  on cs.cat_id = cat.cat_id                        "
                + "where sub.cat_estado = 'A' and cat.cat_estado ='A'                                   " + "order by sub.cat_nombre";
        PreparedStatement ps = con.prepareStatement(sql + " with ur ");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(rs.getString("cat_nombre"));
        }
        return lista;
    }

    // ///////// fin lucene

    public static void close(Connection con) {

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Problema SQL close " + e.getMessage());
            }
        }
    }

    public static void close(Statement st) {

        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                System.out.println("Problema SQL close " + e.getMessage());
            }
        }
    }

    public static void close(ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                System.out.println("Problema SQL close " + e.getMessage());
            }
        }
    }

    public static void close(ResultSet rs, Statement st) {
        close(rs);
        close(st);
    }

   

    public static String calculaTiempo(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        return hours + ":" + minutes + ":" + seconds;
    }

}
