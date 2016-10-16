package cl.bbr.jumbocl.productos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;


import cl.bbr.jumbocl.common.model.FichaProductoEntity;
import cl.bbr.jumbocl.common.utils.DString;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.ItemFichaProductoDTO;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.jumbocl.productos.dto.CriterioSustitutoDTO;
import cl.bbr.jumbocl.productos.dto.DestacadoDTO;
import cl.bbr.jumbocl.productos.dto.MarcaDTO;
import cl.bbr.jumbocl.productos.dto.ProductoCarroDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.jumbocl.productos.dto.ProductosSustitutosCategoriasDTO;
import cl.bbr.jumbocl.productos.exception.ProductosDAOException;
import cl.bbr.jumbocl.productos.exception.ProductosException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * <p>
 * Clase para la interacción con el repositorio de datos. Esta clase debe ser
 * instanciada sólo desde capa de servicios (service).
 * </p>
 * 
 * <p>
 * Esta clase contiene los métodos para consultar, modificar, ingresar datos en
 * el repositorio.
 * </p>
 * 
 * @author BBR ecommerce & retail
 *  
 */
public class JdbcProductosDAO implements ProductosDAO {

    /**
     * Comentario para <code>logger</code> instancia del log de la aplicación
     * para la clase.
     */
    static Logging logger = new Logging(JdbcProductosDAO.class);

    /*
     * (sin Javadoc)
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getCategoriasList(long)
     */
    public List getCategoriasList(long cliente_id) throws ProductosDAOException {
        List lista = new ArrayList();
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conexion = JdbcDAOFactory.getConexion();
            String sql = "SELECT fo_categorias.cat_id, fo_catsubcat.cat_id cat_id_padre, cat_nombre, cat_descripcion, " +
                         "cat_orden, cat_porc_ranking, cat_banner, cat_tipo " +
                         "FROM fo_categorias " +
                         "left join fo_catsubcat on fo_categorias.cat_id = fo_catsubcat.subcat_id " +
                         "where cat_estado = 'A' " +
                         "union " +
                         "select distinct 0 as cat_id, fcat.cat_id as cat_id_padre, 'Mis Preferidos' as cat_nombre, '' as cat_descripcion," +
                         " -1 as cat_orden, 0 as cat_porc_ranking, '' as cat_banner, 'T' as cat_tipo " +
                         "from fodba.fo_favoritos fa " +
                         "inner join fodba.fo_productos fpro on fpro.pro_id = fa.producto_id " +
                         "inner join fodba.fo_productos_categorias fpc on fpc.prca_pro_id = fpro.pro_id " +
                         "inner join fodba.fo_categorias fsubcat on fsubcat.cat_id = fpc.prca_cat_id " +
                         "inner join fodba.fo_catsubcat fsub on fsub.subcat_id = fsubcat.cat_id " +
                         "inner join fodba.fo_categorias fcat on fcat.cat_id = fsub.cat_id " +
                         "where fpro.pro_estado = 'A'  and fsubcat.cat_estado = 'A' and fcat.cat_estado = 'A' " +
                         "and fa.cliente_id = " + cliente_id + " " +
                         "order by cat_orden, cat_nombre";
            
            stm = conexion.prepareStatement(sql + " WITH UR"); 
            logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();
            while (rs.next()) {
                CategoriaDTO categoria = new CategoriaDTO();
                categoria.setId(rs.getLong("cat_id"));
                categoria.setId_padre(rs.getLong("cat_id_padre"));
                categoria.setNombre(rs.getString("cat_nombre"));
                categoria.setTipo(rs.getString("cat_tipo"));
                categoria.setBanner(rs.getString("cat_banner"));
                categoria.setRanking(rs.getLong("cat_porc_ranking"));
                categoria.setDescripcion(rs.getString("cat_descripcion"));
                lista.add(categoria);
            }
        } catch (SQLException ex) {
            logger.error("getCategoriasList - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCategoriasList - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCategoriasList - Problema SQL (close)", e);
            }
        }
        return lista;
    }
    
    /*
     * (sin Javadoc)
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getCategoriasList(long)
     */
    public List categoriasGetInteryTerm(long cliente_id, long cabecera_id) throws ProductosDAOException {
            List lista = new ArrayList();
            Connection conexion = null;
            PreparedStatement stm = null;
            ResultSet rs = null;
            try {
                conexion = JdbcDAOFactory.getConexion();
                String sql = "SELECT fo_categorias.cat_id, fo_categorias.cat_url_banner, fo_catsubcat.cat_id cat_id_padre, cat_nombre, cat_descripcion, " +
                             "cat_orden, cat_porc_ranking, cat_banner, cat_tipo, cat_imagen " + 
                             "FROM fo_categorias " + 
                             "LEFT JOIN fo_catsubcat on fo_categorias.cat_id = fo_catsubcat.subcat_id " +
                             "WHERE cat_estado = 'A' and fo_catsubcat.cat_id = " + cabecera_id + " " +
                             "UNION " +
                             
                             "SELECT fo_categorias.cat_id, fo_categorias.cat_url_banner, fo_catsubcat.cat_id cat_id_padre, cat_nombre, cat_descripcion, " +
                             "cat_orden, cat_porc_ranking, cat_banner, cat_tipo, cat_imagen " + 
                             "FROM fo_categorias " + 
                             "LEFT JOIN fo_catsubcat on fo_categorias.cat_id = fo_catsubcat.subcat_id " +
                             "WHERE cat_estado = 'A' and fo_catsubcat.cat_id in (SELECT fo_catsubcat.subcat_id FROM fodba.fo_catsubcat WHERE cat_id = " + cabecera_id +") " +
                             "UNION " +
                             
                             "SELECT distinct 0 as cat_id, fcat.cat_url_banner, fcat.cat_id as cat_id_padre, 'Mis Preferidos' as cat_nombre, '' as cat_descripcion," +
                             " -1 as cat_orden, 0 as cat_porc_ranking, '' as cat_banner, 'T' as cat_tipo, '' as cat_imagen " +
                             "FROM fodba.fo_favoritos fa " +
                             "INNER JOIN fodba.fo_productos fpro on fpro.pro_id = fa.producto_id " +
                             "INNER JOIN fodba.fo_productos_categorias fpc on fpc.prca_pro_id = fpro.pro_id " +
                             "INNER JOIN fodba.fo_categorias fsubcat on fsubcat.cat_id = fpc.prca_cat_id " +
                             "INNER JOIN fodba.fo_catsubcat fsub on fsub.subcat_id = fsubcat.cat_id " +
                             "INNER JOIN fodba.fo_categorias fcat on fcat.cat_id = fsub.cat_id " +
                             "WHERE fpro.pro_estado = 'A'  and fsubcat.cat_estado = 'A' and fcat.cat_estado = 'A' " +
                             "and fa.cliente_id = " + cliente_id + " " +
                             "ORDER BY cat_orden, cat_nombre ";            
                
                stm = conexion.prepareStatement(sql + " WITH UR"); 
                logger.debug("SQL: " + stm.toString());
                rs = stm.executeQuery();
                while (rs.next()) {
                    CategoriaDTO categoria = new CategoriaDTO();
                    categoria.setId(rs.getLong("cat_id"));
                    categoria.setId_padre(rs.getLong("cat_id_padre"));
                    categoria.setNombre(rs.getString("cat_nombre"));
                    categoria.setTipo(rs.getString("cat_tipo"));
                    categoria.setImagen(rs.getString("cat_imagen"));
                    categoria.setRanking(rs.getLong("cat_porc_ranking"));
                    categoria.setUrl_banner(rs.getString("cat_url_banner"));
                    
                    lista.add(categoria);
                }
            } catch (SQLException ex) {
                logger.error("categoriasGetInteryTerm - Problema SQL", ex);
                throw new ProductosDAOException(ex);
            } catch (Exception ex) {
                logger.error("categoriasGetInteryTerm - Problema General", ex);
                throw new ProductosDAOException(ex);
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (stm != null)
                        stm.close();
                    if (conexion != null && !conexion.isClosed())
                        conexion.close();
                } catch (SQLException e) {
                    logger.error("categoriasGetInteryTerm - Problema SQL (close)", e);
                }
            }
            return lista;
        }
    
    
    public List getProductosList(String local_id, long categoria_id, long cliente_id, String marca, String orden)
            throws ProductosDAOException {

        List lista = new ArrayList();
        ProductoDTO producto = null;
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        logger.debug("local_id: " + local_id);
        logger.debug("categoria_id: " + categoria_id);
        logger.debug("cliente_id: " + cliente_id);
        logger.debug("marca: " + marca);
        logger.debug("order: " + orden);

        String filtro_orden = "";
        if (orden.compareTo("precio") == 0)
            filtro_orden = " order by pre_valor desc, pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
        else if (orden.compareTo("nombre") == 0)
            filtro_orden = " order by pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
        else if (orden.compareTo("ppum") == 0)
            filtro_orden = " order by ppum asc, pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
        else if (orden.compareTo("jumbo") == 0)
            filtro_orden = " ";
        else
            filtro_orden = " ";

        String filtro_marca = "";
        if (marca.compareTo("") != 0)
            filtro_marca = " and mar_id=" + marca + " ";

        try {

            String query = "";

            if (filtro_orden.compareTo(" ") == 0) {
                query = "SELECT pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, pro_tipo_producto, pro_des_corta, "
                        + "		pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, "
                        + "		pro_fcrea, pro_fmod, pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, pro_id_bo, "
                        + "		pro_tipo_sel, mar_nombre, pre_valor, pre_stock, pre_estado, pre_tienestock, car_id, car_cantidad, car_id as en_carro, car_nota, "
                        + "		uni_desc, uni_cantidad, uni_estado, "
                        + "		CASE WHEN uni_cantidad <> 0 and pro_unidad_medida <> 0 and uni_cantidad is not null and pro_unidad_medida is not null THEN pre_valor/(uni_cantidad*pro_unidad_medida) ELSE 0 END as ppum,"
                        + "		prca_con_pago, prca_orden, pro_particionable, pro_particion "
                        + "FROM 	fo_productos inner join fo_productos_categorias on pro_id = prca_pro_id "
                        + "		inner join fo_marcas on mar_id = pro_mar_id "
                        + "		left join fo_precios_locales on pre_pro_id = pro_id and pre_loc_id = ? "
                        + "		left join fo_unidades_medida on uni_id = pro_uni_id "
                        + "		left join fo_carro_compras on car_pro_id = pro_id and car_cli_id = ? "
                        + "WHERE	prca_cat_id = ? "
                        + "		and prca_estado = 'A' "
                        + "		and pro_estado = 'A' "
                        + "		and mar_estado = 'A' "
                        + "		and prca_con_pago = 'S' "
                        + filtro_marca
                        + "UNION "
                        + "SELECT pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, pro_tipo_producto, pro_des_corta, "
                        + "		pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, "
                        + "		pro_fcrea, pro_fmod, pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, pro_id_bo, "
                        + "		pro_tipo_sel, mar_nombre, pre_valor, pre_stock, pre_estado, pre_tienestock, car_id, car_cantidad, car_id as en_carro, car_nota, "
                        + "		uni_desc, uni_cantidad, uni_estado, "
                        + "		CASE WHEN uni_cantidad <> 0 and pro_unidad_medida <> 0 and uni_cantidad is not null and pro_unidad_medida is not null THEN pre_valor/(uni_cantidad*pro_unidad_medida) ELSE 0 END as ppum,"
                        + "		'N' as prca_con_pago, 0 as prca_orden, pro_particionable, pro_particion "
                        + "FROM 	fo_productos inner join fo_productos_categorias on pro_id = prca_pro_id "
                        + "		inner join fo_marcas on mar_id = pro_mar_id "
                        + "		left join fo_precios_locales on pre_pro_id = pro_id and pre_loc_id = ? "
                        + "		left join fo_unidades_medida on uni_id = pro_uni_id "
                        + "		left join fo_carro_compras on car_pro_id = pro_id and car_cli_id = ? "
                        + "WHERE	prca_cat_id = ? "
                        + "		and prca_estado = 'A' "
                        + "		and pro_estado = 'A' "
                        + "		and ( prca_con_pago <> 'S' or prca_con_pago is null ) "
                        + filtro_marca
                        + "ORDER BY prca_con_pago desc, prca_orden asc, pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc";
                conexion = JdbcDAOFactory.getConexion();
                stm = conexion.prepareStatement(query + " WITH UR");
                stm.setString(1, local_id);
                stm.setLong(2, cliente_id);
                stm.setLong(3, categoria_id);
                stm.setString(4, local_id);
                stm.setLong(5, cliente_id);
                stm.setLong(6, categoria_id);

            } else {
                query = "SELECT	pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, pro_tipo_producto, pro_des_corta, "
                        + "		pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, "
                        + "		pro_fcrea, pro_fmod, pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, pro_id_bo, "
                        + "		pro_tipo_sel, mar_nombre, pre_valor, pre_stock, pre_estado, pre_tienestock, car_id, car_cantidad, car_id as en_carro, car_nota, "
                        + "		uni_desc, uni_cantidad, uni_estado, pro_particionable, pro_particion, "
                        + "		CASE WHEN uni_cantidad <> 0 and pro_unidad_medida <> 0 and uni_cantidad is not null and pro_unidad_medida is not null THEN pre_valor/(uni_cantidad*pro_unidad_medida) ELSE 0 END as ppum "
                        + "FROM 	fo_productos "
                        + "		inner join fo_productos_categorias on pro_id = prca_pro_id "
                        + "		inner join fo_marcas on mar_id = pro_mar_id "
                        + "		left join fo_precios_locales on pre_pro_id = pro_id and pre_loc_id = ? "
                        + "		left join fo_unidades_medida on uni_id = pro_uni_id "
                        + "		left join fo_carro_compras on car_pro_id = pro_id and car_cli_id = ? "
                        + "WHERE 	prca_cat_id = ? "
                        + "		and prca_estado = 'A' "
                        + "		and pro_estado = 'A' "
                        + "		and mar_estado = 'A' " + filtro_marca + filtro_orden;


                conexion = JdbcDAOFactory.getConexion();
                stm = conexion.prepareStatement(query + " WITH UR");
                stm.setString(1, local_id);
                stm.setLong(2, cliente_id);
                stm.setLong(3, categoria_id);
            }

            logger.debug("SQL (getProductosList): " + stm.toString());
            rs = stm.executeQuery();
            while (rs.next()) {

                logger.logData("getProductosList", rs);

                producto = null;
                producto = new ProductoDTO();
                producto.setPro_id(rs.getLong("pro_id"));
                producto.setTipo_producto(rs.getString("pro_tipo_producto"));
                producto.setDescripcion(rs.getString("pro_des_corta"));
                producto.setImg_chica(rs.getString("pro_imagen_minificha"));
                producto.setMarca(rs.getString("mar_nombre"));
                producto.setGenerico(rs.getString("pro_generico"));
                producto.setNota(rs.getString("car_nota"));
                if (rs.getString("pro_nota") != null && rs.getString("pro_nota").charAt(0) == 'S') {
                    producto.setCon_nota(true);
                } else {
                    producto.setCon_nota(false);
                }
                if (rs.getString("pro_particionable") != null) {
                    if (rs.getString("pro_particionable").toString().equalsIgnoreCase("S")) {
                        producto.setEsParticionable("S");
                        if (rs.getString("pro_particion") != null) {
                            producto.setParticion(Long.parseLong(rs.getString("pro_particion")));
                        } else {
                            producto.setParticion(1);
                        }
                    } else {
                        producto.setEsParticionable("N");
                        producto.setParticion(1);
                    }
                } else {
                    producto.setEsParticionable("N");
                    producto.setParticion(1);
                }

                // No es un producto genérico
                if (rs.getString("pro_generico") == null || rs.getString("pro_generico").compareTo("G") != 0) {

                    if (rs.getString("pre_estado") == null || rs.getString("pre_estado").compareTo("A") != 0)
                        continue;
                    if (rs.getString("uni_estado") == null || rs.getString("uni_estado").compareTo("A") != 0)
                        continue;

                    producto.setPrecio(rs.getDouble("pre_valor"));
                    if (rs.getInt("pre_tienestock") == 0)
                        producto.setTieneStock(false);
                    else
                        producto.setTieneStock(true);
                    if (rs.getString("uni_cantidad") != null && rs.getString("pro_unidad_medida") != null)
                        producto.setPpum(rs.getDouble("pre_valor")
                                / (rs.getDouble("uni_cantidad") * rs.getDouble("pro_unidad_medida")));
                    producto.setUnidad_nombre(rs.getString("uni_desc"));
                    producto.setUnidad_tipo(rs.getString("pro_tipo_sel"));
                    producto.setInter_maximo(rs.getDouble("pro_inter_max"));
                    if (rs.getString("pro_inter_valor") != null) {
                        producto.setInter_valor(rs.getDouble("pro_inter_valor"));
                    } else {
                        producto.setInter_valor(1.0);
                    }
                    producto.setCantidad(rs.getDouble("car_cantidad"));
                    producto.setTipre(Formatos.formatoUnidadFO(rs.getString("pro_tipre")));
                    if (rs.getString("en_carro") == null) {
                        producto.setEn_carro(false);
                    } else {
                        producto.setEn_carro(true);
                    }
                    if (orden.equalsIgnoreCase("precio")) {
                        //particionable
                        if (producto.getEsParticionable().equalsIgnoreCase("S")) {
                            producto.setPrecioOrden(rs.getDouble("pre_valor") / producto.getParticion());
                        } else {
                            producto.setPrecioOrden(rs.getDouble("pre_valor"));
                        }

                    } else if (orden.equalsIgnoreCase("ppum")) {
                        producto.setPrecioOrden(producto.getPpum());
                    }

                } else {
                    //Recorrer los productos genéricos para obtener sus item
                    List lista_item = this.getItems(local_id, producto.getPro_id(), cliente_id, orden);
                    if (lista_item.size() > 0) {
                        producto.setProductosDTO(lista_item);
                    }
                    if ((orden.equalsIgnoreCase("precio") || orden.equalsIgnoreCase("ppum"))
                            && producto.getProductosDTO() != null) {
                        if (producto.getProductosDTO().size() > 0) {
                            if (orden.equalsIgnoreCase("precio")) {
                                producto.setPrecioOrden(((ProductoDTO) producto.getProductosDTO().get(0)).getPrecio());
                            } else if (orden.equalsIgnoreCase("ppum")) {
                                producto.setPrecioOrden(promedioGenericos(producto.getProductosDTO()));
                            }
                        }
                    }
                }
                lista.add(producto);
            }

            if (orden.equalsIgnoreCase("precio") || orden.equalsIgnoreCase("ppum")) {
                Collections.sort(lista);
            }

        } catch (SQLException ex) {
            logger.error("getProductosList - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getProductosList - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getProductosList - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /**
     * @param productosDTO
     * @return
     */
    private double promedioGenericos(List productosDTO) {
        double suma = 0;
        double prom = 0;
        for (int i = 0; i < productosDTO.size(); i++) {
            ProductoDTO prod = (ProductoDTO) productosDTO.get(i);
            suma += prod.getPpum();
        }
        prom = (suma / productosDTO.size());
        return prom;
    }

    /*
     * (sin Javadoc)
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getItems(java.lang.String,
     *      long, long)
     */
    public List getItems(String local_id, long pro_padre, long cliente_id, String orden) throws ProductosDAOException {

        Connection conexion = null;
        PreparedStatement stm_item = null;
        ResultSet rs_item = null;
        List lista_item = new ArrayList();
        String orderBy = "";

        if (orden != null) {
            if (orden.equalsIgnoreCase("precio")) {
                orderBy = "ORDER BY pre_valor";
            } else if (orden.equalsIgnoreCase("nombre")) {
                orderBy = "ORDER BY pro_des_corta";
            }
        }

        try {
            String query = "select pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, "
                    + "pro_tipo_producto, pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, "
                    + "pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, pro_fcrea, pro_fmod, pro_user_mod, "
                    + "pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, pro_id_bo, pro_tipo_sel, "
                    + "mar_nombre, pre_valor, pre_stock, car_id, car_cantidad, car_id as en_carro, uni_desc, uni_cantidad, "
                    + "pro_particionable, pro_particion "
                    + "from fo_productos "
                    + "	join fo_marcas on mar_id = pro_mar_id "
                    + "	join fo_precios_locales on pro_id = pre_pro_id and pre_loc_id = ? "
                    + "	join fo_unidades_medida on uni_id = pro_uni_id "
                    + "	left join fo_carro_compras on pro_id = car_pro_id and car_cli_id = ? "
                    + "where pro_id_padre = ? " + "and pre_estado = 'A' " + "and uni_estado = 'A' "
                    + "and mar_estado = 'A' " + "and pro_estado = 'A' " + orderBy;

            conexion = JdbcDAOFactory.getConexion();
            stm_item = conexion.prepareStatement(query + " WITH UR");
            stm_item.setString(1, local_id);
            stm_item.setLong(2, cliente_id);
            stm_item.setLong(3, pro_padre);
            rs_item = stm_item.executeQuery();
            while (rs_item.next()) {

                logger.logData("getProductosList", rs_item);

                ProductoDTO item = new ProductoDTO();
                item.setPro_id(rs_item.getLong("pro_id"));
                item.setTipo_producto(rs_item.getString("pro_tipo_producto"));
                item.setDescripcion(rs_item.getString("pro_des_corta"));
                item.setImg_chica(rs_item.getString("pro_imagen_minificha"));
                item.setMarca(rs_item.getString("mar_nombre"));
                item.setPrecio(rs_item.getDouble("pre_valor"));
                if (rs_item.getString("uni_cantidad") != null && rs_item.getString("pro_unidad_medida") != null)
                    item.setPpum(rs_item.getDouble("pre_valor")
                            / (rs_item.getDouble("uni_cantidad") * (rs_item.getDouble("pro_unidad_medida"))));
                item.setUnidad_nombre(rs_item.getString("uni_desc"));
                item.setUnidad_tipo(rs_item.getString("pro_tipo_sel"));
                item.setInter_maximo(rs_item.getDouble("pro_inter_max"));
                if (rs_item.getString("pro_inter_valor") != null)
                    item.setInter_valor(rs_item.getDouble("pro_inter_valor"));
                else
                    item.setInter_valor(1.0);
                item.setCantidad(rs_item.getDouble("car_cantidad"));
                item.setValor_diferenciador(rs_item.getString("pro_valor_difer"));
                item.setTipre(Formatos.formatoUnidadFO(rs_item.getString("pro_tipre")));
                if (rs_item.getString("en_carro") == null)
                    item.setEn_carro(false);
                else
                    item.setEn_carro(true);

                if (rs_item.getString("pro_particionable") != null) {
                    if (rs_item.getString("pro_particionable").toString().equalsIgnoreCase("S")) {
                        item.setEsParticionable("S");
                        if (rs_item.getString("pro_particion") != null) {
                            item.setParticion(Long.parseLong(rs_item.getString("pro_particion")));
                        } else {
                            item.setParticion(1);
                        }
                    } else {
                        item.setEsParticionable("N");
                        item.setParticion(1);
                    }
                } else {
                    item.setEsParticionable("N");
                    item.setParticion(1);
                }

                lista_item.add(item);
            } // While

        } catch (SQLException ex) {
            logger.error("getItems - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getItems - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs_item != null)
                    rs_item.close();
                if (stm_item != null)
                    stm_item.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getItems - Problema SQL (close)", e);
            }
        }

        return lista_item;

    }

    /*
     * (sin Javadoc)
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getCategoria(long)
     */
    public CategoriaDTO getCategoria(long categoria_id) throws ProductosDAOException {

            CategoriaDTO categoria = null;
            Connection conexion = null;
            PreparedStatement stm = null;
            ResultSet rs = null;

            try {
            	String cadQuery="	SELECT FO_CATEGORIAS.CAT_ID," +
            			" FO_CATSUBCAT.CAT_ID CAT_ID_PADRE, FO_CATEGORIAS.CAT_URL_BANNER, CAT_NOMBRE," +
            			" CAT_DESCRIPCION, CAT_ORDEN, CAT_PORC_RANKING, " +
            			"CAT_BANNER, CAT_TOTEM, CAT_TIPO " +
            					"	FROM FO_CATEGORIAS LEFT JOIN FO_CATSUBCAT ON FO_CATEGORIAS.CAT_ID = FO_CATSUBCAT.SUBCAT_ID " +
            					"	WHERE FO_CATEGORIAS.CAT_ID = ? ";
            	
                conexion = JdbcDAOFactory.getConexion();
                stm = conexion.prepareStatement(cadQuery + " WITH UR");
                stm.setLong(1, categoria_id);
                logger.debug("SQL: " + stm.toString());
                rs = stm.executeQuery();
                while (rs.next()) {

                    logger.logData("getCategoria", rs);

                    categoria = null;
                    categoria = new CategoriaDTO();
                    categoria.setId(rs.getLong("cat_id"));
                    categoria.setId_padre(rs.getLong("cat_id_padre"));
                    categoria.setNombre(rs.getString("cat_nombre"));
                    categoria.setTipo(rs.getString("cat_tipo"));
                    categoria.setBanner(rs.getString("cat_banner"));
                    categoria.setTotem(rs.getString("cat_totem"));
                    categoria.setRanking(rs.getLong("cat_porc_ranking"));
                    categoria.setUrl_banner(rs.getString("cat_url_banner"));
                }
            } catch (SQLException ex) {
                logger.error("getCategoria - Problema SQL", ex);
                throw new ProductosDAOException(ex);
            } catch (Exception ex) {
                logger.error("getCategoria - Problema General", ex);
                throw new ProductosDAOException(ex);
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (stm != null)
                        stm.close();
                    if (conexion != null && !conexion.isClosed())
                        conexion.close();
                } catch (SQLException e) {
                    logger.error("getCategoria - Problema SQL (close)", e);
                }
            }
            return categoria;

        }

    /*
     * (J)Obtiene todas las marcas de la categoria
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getMarcas(long)
     */
    public List getMarcas(long categoria_id) throws ProductosDAOException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List lista = new ArrayList();

        try {
        	String cadQuery="SELECT DISTINCT MAR_ID, MAR_NOMBRE                            		 "
                + "FROM FODBA.FO_PRODUCTOS                                                       "
                + "		INNER JOIN FODBA.FO_PRODUCTOS_CATEGORIAS ON PRO_ID = PRCA_PRO_ID         "
                + "		INNER JOIN FODBA.FO_CATEGORIAS SUBCAT ON SUBCAT.CAT_ID = PRCA_CAT_ID     "
                + "		INNER JOIN FODBA.FO_CATSUBCAT RELCAT ON RELCAT.SUBCAT_ID = SUBCAT.CAT_ID "
                + "		INNER JOIN FODBA.FO_CATEGORIAS CAT ON CAT.CAT_ID = RELCAT.CAT_ID         "
                + "		INNER JOIN FODBA.FO_MARCAS ON MAR_ID = PRO_MAR_ID                        "
                + "WHERE PRO_ESTADO = 'A' AND SUBCAT.CAT_ESTADO = 'A' AND CAT.CAT_ESTADO = 'A'   "
                + "AND PRCA_ESTADO = 'A'  AND MAR_ESTADO = 'A' AND SUBCAT.CAT_ID = ?             "
                + "ORDER BY MAR_NOMBRE";
        	
            con = JdbcDAOFactory.getConexion();
            //deben estar activos: producto, categoria y subcategoria
            ps = con.prepareStatement(cadQuery + " WITH UR");
            
            ps.setLong(1, categoria_id);
            
            logger.debug("SQL: " + ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                MarcaDTO marca = new MarcaDTO();
                marca.setMar_id(rs.getLong("mar_id"));
                marca.setMar_nombre(rs.getString("mar_nombre"));
                lista.add(marca);
            }
        } catch (SQLException ex) {
            logger.error("getCategoriaMarcas - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCategoriaMarcas - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            close(rs, ps, con);
        }
        return lista;
    }

    /*
     * (sin Javadoc)
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getProducto(long, long, long)
     */
    public List getProducto(long producto_id, long cliente_id, long local_id, String idSession) throws ProductosDAOException {
        List lista = new ArrayList();
        ProductoDTO producto = null;
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, " +
                         "pro_tipo_producto, pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, " +
                         "pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, pro_fcrea, pro_fmod, pro_user_mod, " +
                         "pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, pro_id_bo, " +
                         "pro_tipo_sel, mar_nombre, pre_valor, pre_stock, pre_estado, pre_tienestock, uni_desc, uni_cantidad, " +
                         "uni_estado, car_id, car_cantidad, car_id as en_carro, car_nota, pila_porcion, id_pila_unidad " +
                         "FROM fo_productos " +
                         "left join fo_precios_locales on pre_pro_id = pro_id and pre_loc_id = ? " +
                         "left join fo_unidades_medida on uni_id = pro_uni_id " +
                         "inner join fo_marcas on mar_id = pro_mar_id " +
                         "left join fo_carro_compras on car_pro_id = pro_id and car_cli_id = ? ";
                         
                         if ( cliente_id == 1 ) {
                             sql += "AND CAR_INVITADO_ID=" + idSession + " ";
                         }
                         
                         sql += "where pro_id = ? ";

            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql + " WITH UR");
            stm.setLong(1, local_id);
            stm.setLong(2, cliente_id);
            stm.setLong(3, producto_id);
            logger.debug("SQL (getProducto): " + stm.toString());
            rs = stm.executeQuery();
            while (rs.next()) {

                logger.logData("getProducto", rs);

                producto = null;
                producto = new ProductoDTO();
                producto.setPro_id(rs.getLong("pro_id"));
                producto.setTipo_producto(rs.getString("pro_tipo_producto"));
                producto.setDescripcion(rs.getString("pro_des_corta"));
                producto.setImg_chica(rs.getString("pro_imagen_minificha"));
                producto.setImg_grande(rs.getString("pro_imagen_ficha"));
                producto.setMarca(rs.getString("mar_nombre"));
                producto.setGenerico(rs.getString("pro_generico"));
                producto.setNota(rs.getString("car_nota"));
                producto.setCantidad(rs.getDouble("car_cantidad"));
                if ( rs.getString("pila_porcion") != null )
                    producto.setPilaPorcion(rs.getDouble("pila_porcion"));
                if ( rs.getString("id_pila_unidad") != null )
                    producto.setIdPilaUnidad(rs.getLong("id_pila_unidad"));
                
                if (rs.getString("pro_nota") != null && rs.getString("pro_nota").charAt(0) == 'S')
                    producto.setCon_nota(true);
                else
                    producto.setCon_nota(false);

                // Si producto no es genérico
                if (rs.getString("pro_generico") == null || rs.getString("pro_generico").compareTo("G") != 0) {

                    if (rs.getString("pre_estado") == null || rs.getString("pre_estado").compareTo("A") != 0)
                        continue;
                    if (rs.getString("uni_estado") == null || rs.getString("uni_estado").compareTo("A") != 0)
                        continue;

                    producto.setPrecio(rs.getDouble("pre_valor"));
                    if (rs.getString("uni_cantidad") != null && rs.getString("pro_unidad_medida") != null)
                        producto.setPpum(rs.getDouble("pre_valor")
                                / (rs.getDouble("uni_cantidad") * rs.getDouble("pro_unidad_medida")));
                    producto.setUnidad_nombre(rs.getString("uni_desc"));
                    producto.setUnidad_tipo(rs.getString("pro_tipo_sel"));
                    producto.setInter_maximo(rs.getDouble("pro_inter_max"));
                    if (rs.getString("pro_inter_valor") != null)
                        producto.setInter_valor(rs.getDouble("pro_inter_valor"));
                    else
                        producto.setInter_valor(1.0);
                    producto.setTipre(Formatos.formatoUnidadFO(rs.getString("pro_tipre")));

                    if (rs.getString("en_carro") == null)
                        producto.setEn_carro(false);
                    else
                        producto.setEn_carro(true);
                    if (rs.getInt("pre_tienestock") == 0)
                        producto.setTieneStock(false);
                    else
                        producto.setTieneStock(true);
                }
                /**
                 * pro_id_bo
                 */
                producto.setPro_id_bo(rs.getInt("pro_id_bo"));

                lista.add(producto);

            }

            for (int i = 0; i < lista.size(); i++) {

                ProductoDTO pro_aux = (ProductoDTO) lista.get(i);

                // Si es producto genérico se deben obtener sus items
                if (pro_aux.getGenerico() != null && pro_aux.getGenerico().compareTo("G") == 0) {
                    List lista_item = new ArrayList();
                    lista_item = this.getItems(local_id + "", producto.getPro_id(), cliente_id, null);
                    if (lista_item.size() == 0)
                        continue;
                    producto.setProductosDTO(lista_item);
                }

            }
        } catch (SQLException ex) {
            logger.error("getProducto - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getProducto - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getProducto - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /*
     * (sin Javadoc)
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getSugerido(long, long, long)
     */
    public List getSugerido(long producto_id, long cliente_id, long local_id, String idSession) throws ProductosDAOException {

        List lista = new ArrayList();
        ProductoDTO producto = null;
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            conexion = JdbcDAOFactory.getConexion();
            
            String sql = "SELECT pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, " +
                         "pro_tipo_producto, pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, " +
                         "pro_unidad_medida, pro_valor_difer, pro_ranking_ventas, pro_fcrea, pro_fmod, pro_user_mod, " +
                         "pro_generico, pro_inter_valor, pro_inter_max, pro_preparable, pro_nota, pro_id_bo, pro_tipo_sel, " +
                         "mar_nombre, pre_valor, pre_stock, pre_estado, uni_desc, uni_cantidad, uni_estado, car_id, car_cantidad, " +
                         "car_id as en_carro, car_nota " +
                         "FROM fo_productos " +
                         "join fo_pro_sugerencias on pro_id = sug_sug " +
                         "left join fo_precios_locales on pre_pro_id = pro_id " +
                         "left join fo_unidades_medida on uni_id = pro_uni_id " +
                         "join fo_marcas on mar_id = pro_mar_id " +
                         "left join fo_carro_compras on car_pro_id = pro_id and car_cli_id = ? ";
            if ( cliente_id == 1 ) {
                sql += "AND CAR_INVITADO_ID="+idSession+" ";
            }
            sql += "where pre_loc_id = ? and pro_estado = 'A' and sug_base = ? and sug_formato in ('U', 'B') ";
            
            stm = conexion.prepareStatement(sql + " WITH UR");
            
            stm.setLong(1, cliente_id);
            stm.setLong(2, local_id);
            stm.setLong(3, producto_id);
            logger.debug("SQL: " + stm.toString());
            rs = stm.executeQuery();

            while (rs.next()) {

                logger.logData("getSugerido", rs);

                producto = null;
                producto = new ProductoDTO();
                producto.setPro_id(rs.getLong("pro_id"));
                producto.setTipo_producto(rs.getString("pro_tipo_producto"));
                producto.setDescripcion(rs.getString("pro_des_corta"));
                producto.setImg_chica(rs.getString("pro_imagen_minificha"));
                producto.setImg_grande(rs.getString("pro_imagen_ficha"));
                producto.setMarca(rs.getString("mar_nombre"));
                producto.setGenerico(rs.getString("pro_generico"));
                producto.setNota(rs.getString("car_nota"));
                producto.setCantidad(rs.getDouble("car_cantidad"));
                if (rs.getString("pro_nota") != null && rs.getString("pro_nota").charAt(0) == 'S')
                    producto.setCon_nota(true);
                else
                    producto.setCon_nota(false);

                // Es unr producto
                if (rs.getString("pro_generico") == null || rs.getString("pro_generico").compareTo("G") != 0) {

                    if (rs.getString("pre_estado") == null || rs.getString("pre_estado").compareTo("A") != 0)
                        continue;
                    if (rs.getString("uni_estado") == null || rs.getString("uni_estado").compareTo("A") != 0)
                        continue;

                    producto.setPrecio(rs.getDouble("pre_valor"));
                    if (rs.getString("uni_cantidad") != null && rs.getString("pro_unidad_medida") != null)
                        producto.setPpum(rs.getDouble("pre_valor")
                                / (rs.getDouble("uni_cantidad") * rs.getDouble("pro_unidad_medida")));
                    producto.setUnidad_nombre(rs.getString("uni_desc"));
                    producto.setUnidad_tipo(rs.getString("pro_tipo_sel"));
                    producto.setInter_maximo(rs.getDouble("pro_inter_max"));
                    if (rs.getString("pro_inter_valor") != null)
                        producto.setInter_valor(rs.getDouble("pro_inter_valor"));
                    else
                        producto.setInter_valor(1.0);
                    producto.setTipre(Formatos.formatoUnidadFO(rs.getString("pro_tipre")));

                    if (rs.getString("en_carro") == null)
                        producto.setEn_carro(false);
                    else
                        producto.setEn_carro(true);
                }

                lista.add(producto);

            }

            for (int i = 0; i < lista.size(); i++) {
                ProductoDTO producto_aux = (ProductoDTO) lista.get(i);
                // Si es producto genérico se deben obtener sus items
                if (producto_aux.getGenerico().compareTo("G") == 0) {
                    List lista_item = new ArrayList();
                    lista_item = this.getItems(local_id + "", producto.getPro_id(), cliente_id, null);
                    if (lista_item.size() == 0)
                        continue;
                    producto.setProductosDTO(lista_item);
                }

            }

        } catch (SQLException ex) {
            logger.error("getSugerido - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getSugerido - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getSugerido - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /*
     * (sin Javadoc)
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getSearch(java.util.List, long)
     */
    public List getSearch(List patron, long local_id) throws ProductosDAOException {

        List lista = new ArrayList();
        CategoriaDTO categoria = null;
        String patronaux = " ";

        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {

            // Formatear patron a buscar
            patronaux = this.formatPatron(patron);

            logger.debug("Búsqueda por marca: local_id=" + local_id);
            logger.debug("Búsqueda por marca: patron=" + patronaux);

            String query = "select cat1.cat_id, cat1.cat_nombre, cat1.cat_tipo, pro_mar_id, mar_nombre, cat2.cat_nombre as subcat, count(*) as cantidad  "
                    + "from fo_productos "
                    + "join fo_marcas on pro_mar_id = mar_id "
                    + "join fo_unidades_medida on uni_id = pro_uni_id "
                    + "join fo_productos_categorias on pro_id = prca_pro_id "
                    + "join fo_precios_locales on pro_id = pre_pro_id "
                    + "join fo_categorias as cat1 on cat1.cat_id = prca_cat_id "
                    + "left join fo_catsubcat on cat1.cat_id = fo_catsubcat.subcat_id "
                    + "left join fo_categorias as cat2 on cat2.cat_id = fo_catsubcat.cat_id "
                    + "where pre_estado = 'A' "
                    + "and pre_loc_id = ? "
                    + "and mar_estado = 'A' "
                    + "and pro_estado = 'A' "
                    + "and uni_estado = 'A' "
                    + "and prca_estado = 'A' "
                    + "and cat1.cat_estado = 'A' "
                    + "and cat2.cat_estado = 'A' "
                    + "and ("
                    + patronaux
                    + ") "
                    + "group by cat1.cat_id, cat1.cat_nombre, cat1.cat_tipo, pro_mar_id, mar_nombre, cat2.cat_nombre "
                    + "order by cat2.cat_nombre, cat1.cat_nombre";

            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query + " WITH UR");
            
            stm.setLong(1, local_id);
            logger.debug("SQL: " + stm.toString());
            
            rs = stm.executeQuery();
            while (rs.next()) {

                logger.logData("getSearch", rs);

                categoria = null;
                categoria = new CategoriaDTO();
                categoria.setId(rs.getLong("cat_id"));
                categoria.setNombre(rs.getString("cat_nombre"));
                categoria.setId_marca(rs.getLong("pro_mar_id"));
                categoria.setNombre_marca(rs.getString("mar_nombre"));
                categoria.setTipo(rs.getString("cat_tipo"));
                categoria.setSubcat(rs.getString("subcat"));
                categoria.setCant_productos(rs.getLong("cantidad"));

                lista.add(categoria);
            }

        } catch (SQLException ex) {
            logger.error("getSearch - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getSearch - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getSearch - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /*
     * (sin Javadoc)
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getSearchMarca(java.lang.String,
     *      long, long, java.lang.String, java.util.List)
     */
    public List getSearchMarca(String local_id, long marca_id, long cliente_id, String orden, List patron) throws ProductosDAOException {
        List lista = new ArrayList();
        ProductoDTO producto = null;
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String patronaux = " ";

        try {
            if (orden.equalsIgnoreCase("")) {
                orden = "precio";
            }
            String filtro_orden = "";
            if (orden.compareTo("precio") == 0)
                filtro_orden = " order by pre_valor desc, pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
            else if (orden.compareTo("nombre") == 0)
                filtro_orden = " order by pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
            else if (orden.compareTo("ppum") == 0)
                filtro_orden = " order by ppum asc, pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
            else
                filtro_orden = " order by pre_valor desc ";

            // Formatear patron a buscar
            patronaux = this.formatPatron(patron);

            String query = "SELECT distinct pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, pro_tipo_producto, "
                    + "		pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, pro_unidad_medida, pro_valor_difer, "
                    + "		pro_ranking_ventas, pro_fcrea, pro_fmod, pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, "
                    + "		pro_preparable, pro_nota, pro_id_bo, pro_tipo_sel, mar_nombre, pre_valor, pre_stock, pre_estado, car_id, "
                    + "		car_cantidad, car_id as en_carro, car_nota, uni_desc, uni_cantidad, uni_estado, pro_particionable, pro_particion, "
                    + "		CASE WHEN uni_cantidad <> 0 and pro_unidad_medida <> 0 and uni_cantidad is not null and pro_unidad_medida is not null THEN pre_valor/(uni_cantidad*pro_unidad_medida) ELSE 0 END as ppum "
                    + "FROM 	fo_productos "
                    + "		join fo_productos_categorias on pro_id = prca_pro_id "
                    + "		left join fo_precios_locales on pre_pro_id = pro_id and pre_loc_id = ? "
                    + "		left join fo_unidades_medida on uni_id = pro_uni_id "
                    + "		join fo_marcas on mar_id = pro_mar_id "
                    + "		left join fo_carro_compras on car_pro_id = pro_id  and car_cli_id = ? "
                    + "WHERE 	mar_id = ? "
                    + "		and prca_estado = 'A' "
                    + "		and pro_estado = 'A' "
                    + "		and mar_estado = 'A' "
                    + "		and "
                    + " ( " + patronaux + " ) " + filtro_orden;

            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query + " WITH UR");
            
            stm.setString(1, local_id);
            stm.setLong(2, cliente_id);
            stm.setLong(3, marca_id);
            logger.debug("SQL: " + stm.toString());

            rs = stm.executeQuery();
            while (rs.next()) {

                logger.logData("getSearchMarca", rs);

                producto = null;
                producto = new ProductoDTO();

                producto.setPro_id(rs.getLong("pro_id"));
                producto.setTipo_producto(rs.getString("pro_tipo_producto"));
                producto.setDescripcion(rs.getString("pro_des_corta"));
                producto.setImg_chica(rs.getString("pro_imagen_minificha"));
                producto.setImg_grande(rs.getString("pro_imagen_ficha"));
                producto.setMarca(rs.getString("mar_nombre"));
                producto.setMarca_id(rs.getLong("pro_mar_id"));
                producto.setGenerico(rs.getString("pro_generico"));
                producto.setNota(rs.getString("car_nota"));
                producto.setCantidad(rs.getLong("car_cantidad"));
                if (rs.getString("pro_nota") != null && rs.getString("pro_nota").charAt(0) == 'S') {
                    producto.setCon_nota(true);
                } else {
                    producto.setCon_nota(false);
                }
                if (rs.getString("pro_particionable") != null) {
                    if (rs.getString("pro_particionable").toString().equalsIgnoreCase("S")) {
                        producto.setEsParticionable("S");
                        if (rs.getString("pro_particion") != null) {
                            producto.setParticion(Long.parseLong(rs.getString("pro_particion")));
                        } else {
                            producto.setParticion(1);
                        }
                    } else {
                        producto.setEsParticionable("N");
                        producto.setParticion(1);
                    }
                } else {
                    producto.setEsParticionable("N");
                    producto.setParticion(1);
                }

                // No es un producto genérico
                if (rs.getString("pro_generico") == null || rs.getString("pro_generico").compareTo("G") != 0) {

                    if (rs.getString("pre_estado") == null || rs.getString("pre_estado").compareTo("A") != 0)
                        continue;
                    if (rs.getString("uni_estado") == null || rs.getString("uni_estado").compareTo("A") != 0)
                        continue;

                    producto.setPrecio(rs.getDouble("pre_valor"));
                    if (rs.getString("uni_cantidad") != null && rs.getString("pro_unidad_medida") != null)
                        producto.setPpum(rs.getDouble("pre_valor")
                                / (rs.getDouble("uni_cantidad") * rs.getDouble("pro_unidad_medida")));
                    producto.setUnidad_nombre(rs.getString("uni_desc"));
                    producto.setUnidad_tipo(rs.getString("pro_tipo_sel"));
                    producto.setInter_maximo(rs.getDouble("pro_inter_max"));
                    if (rs.getString("pro_inter_valor") != null)
                        producto.setInter_valor(rs.getDouble("pro_inter_valor"));
                    else
                        producto.setInter_valor(1.0);
                    producto.setTipre(Formatos.formatoUnidadFO(rs.getString("pro_tipre")));

                    if (rs.getString("en_carro") == null)
                        producto.setEn_carro(false);
                    else
                        producto.setEn_carro(true);

                    if (orden.equalsIgnoreCase("precio")) {
                        //particionable
                        if (producto.getEsParticionable().equalsIgnoreCase("S")) {
                            producto.setPrecioOrden(rs.getDouble("pre_valor") / producto.getParticion());
                        } else {
                            producto.setPrecioOrden(rs.getDouble("pre_valor"));
                        }
                    } else if (orden.equalsIgnoreCase("ppum")) {
                        producto.setPrecioOrden(producto.getPpum());
                    }

                } else {
                    //Si es producto genérico se deben obtener sus items
                    List lista_item = new ArrayList();
                    lista_item = this.getItems(local_id + "", producto.getPro_id(), cliente_id, orden);
                    if (lista_item.size() == 0)
                        continue;
                    producto.setProductosDTO(lista_item);

                    if ((orden.equalsIgnoreCase("precio") || orden.equalsIgnoreCase("ppum"))
                            && producto.getProductosDTO() != null) {
                        if (producto.getProductosDTO().size() > 0) {
                            if (orden.equalsIgnoreCase("precio")) {
                                producto.setPrecioOrden(((ProductoDTO) producto.getProductosDTO().get(0)).getPrecio());
                            } else if (orden.equalsIgnoreCase("ppum")) {
                                producto.setPrecioOrden(promedioGenericos(producto.getProductosDTO()));
                            }
                        }
                    }
                }
                lista.add(producto);
            }
            if (orden.equalsIgnoreCase("precio") || orden.equalsIgnoreCase("ppum")) {
                Collections.sort(lista);
            }

        } catch (SQLException ex) {
            logger.error("getSearchMarca - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getSearchMarca - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getSearchMarca - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /*
     * (sin Javadoc)
     * 
     * @see cl.bbr.fo.productos.dao.ProductosDAO#getSearchSeccion(java.lang.String,
     *      long, long, java.lang.String, java.lang.String, java.util.List)
     */
    public List getSearchSeccion(String local_id, long categoria_id, long cliente_id, String marca, String orden,
            List patron) throws ProductosDAOException {

        List lista = new ArrayList();
        ProductoDTO producto = null;
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        String patronaux = " ";

        try {
            // Formatear patron a buscar
            patronaux = this.formatPatron(patron);
            if (orden.equalsIgnoreCase("")) {
                orden = "precio";
            }

            String filtro_orden = "";
            if (orden.compareTo("precio") == 0)
                filtro_orden = " order by pre_valor desc, pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
            else if (orden.compareTo("nombre") == 0)
                filtro_orden = " order by pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
            else if (orden.compareTo("ppum") == 0)
                filtro_orden = " order by ppum asc, pro_tipo_producto asc, mar_nombre asc, pro_des_corta asc ";
            else
                filtro_orden = " order by pre_valor desc ";

            String filtro_marca = "";
            if (marca.compareTo("") != 0)
                filtro_marca = " and mar_id=" + marca + " ";

            String query = "SELECT distinct pro_id, pro_id_padre, pro_uni_id, pro_mar_id, pro_cod_sap, pro_tipre, pro_estado, pro_tipo_producto, "
                    + "		pro_des_corta, pro_des_larga, pro_imagen_minificha, pro_imagen_ficha, pro_unidad_medida, pro_valor_difer, "
                    + "		pro_ranking_ventas, pro_fcrea, pro_fmod, pro_user_mod, pro_generico, pro_inter_valor, pro_inter_max, "
                    + "		pro_preparable, pro_nota, pro_id_bo, pro_tipo_sel, mar_nombre, pre_valor, pre_stock, pre_estado, car_id, "
                    + "		car_cantidad, car_id as en_carro, car_nota, uni_desc, uni_cantidad, uni_estado, pro_particionable, pro_particion, "
                    + "		CASE WHEN uni_cantidad <> 0 and pro_unidad_medida <> 0 and uni_cantidad is not null and pro_unidad_medida is not null THEN pre_valor/(uni_cantidad*pro_unidad_medida) ELSE 0 END as ppum "
                    + "FROM 	fo_productos "
                    + "		join fo_productos_categorias on pro_id = prca_pro_id "
                    + "		left join fo_precios_locales on pre_pro_id = pro_id and pre_loc_id = ? "
                    + "		left join fo_unidades_medida on uni_id = pro_uni_id "
                    + "		join fo_marcas on mar_id = pro_mar_id "
                    + "		left join fo_carro_compras on car_pro_id = pro_id  and car_cli_id = ? "
                    + "WHERE	prca_cat_id = ? " + "		and prca_estado = 'A' " + "		and pro_estado = 'A' " +
                    //"and pre_estado = 'A' " +
                    //"and uni_estado = 'A' " +
                    "		and mar_estado = 'A' " + filtro_marca + "		and " + " ( " + patronaux + " ) " + filtro_orden;

            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(query + " WITH UR");
            stm.setString(1, local_id);
            stm.setLong(2, cliente_id);
            stm.setLong(3, categoria_id);
            logger.debug("SQL: " + stm.toString());

            rs = stm.executeQuery();

            while (rs.next()) {

                logger.logData("getSearchSeccion", rs);
                producto = null;
                producto = new ProductoDTO();
                producto.setPro_id(rs.getLong("pro_id"));
                producto.setTipo_producto(rs.getString("pro_tipo_producto"));
                producto.setDescripcion(rs.getString("pro_des_corta"));
                producto.setImg_chica(rs.getString("pro_imagen_minificha"));
                producto.setMarca(rs.getString("mar_nombre"));
                producto.setMarca_id(rs.getLong("pro_mar_id"));
                producto.setGenerico(rs.getString("pro_generico"));
                producto.setNota(rs.getString("car_nota"));
                if (rs.getString("pro_nota").charAt(0) == 'S') {
                    producto.setCon_nota(true);
                } else {
                    producto.setCon_nota(false);
                }
                if (rs.getString("pro_particionable") != null) {
                    if (rs.getString("pro_particionable").toString().equalsIgnoreCase("S")) {
                        producto.setEsParticionable("S");
                        if (rs.getString("pro_particion") != null) {
                            producto.setParticion(Long.parseLong(rs.getString("pro_particion")));
                        } else {
                            producto.setParticion(1);
                        }
                    } else {
                        producto.setEsParticionable("N");
                        producto.setParticion(1);
                    }
                } else {
                    producto.setEsParticionable("N");
                    producto.setParticion(1);
                }
                // No es un producto genérico
                if (rs.getString("pro_generico") == null || rs.getString("pro_generico").compareTo("G") != 0) {

                    if (rs.getString("pre_estado") == null || rs.getString("pre_estado").compareTo("A") != 0)
                        continue;
                    if (rs.getString("uni_estado") == null || rs.getString("uni_estado").compareTo("A") != 0)
                        continue;

                    producto.setPrecio(rs.getDouble("pre_valor"));
                    if (rs.getString("uni_cantidad") != null && rs.getString("pro_unidad_medida") != null)
                        producto.setPpum(rs.getDouble("pre_valor")
                                / (rs.getDouble("uni_cantidad") * rs.getDouble("pro_unidad_medida")));
                    producto.setUnidad_nombre(rs.getString("uni_desc"));
                    producto.setUnidad_tipo(rs.getString("pro_tipo_sel"));
                    producto.setInter_maximo(rs.getDouble("pro_inter_max"));
                    if (rs.getString("pro_inter_valor") != null)
                        producto.setInter_valor(rs.getDouble("pro_inter_valor"));
                    else
                        producto.setInter_valor(1.0);
                    producto.setCantidad(rs.getDouble("car_cantidad"));
                    producto.setTipre(Formatos.formatoUnidadFO(rs.getString("pro_tipre")));

                    if (rs.getString("en_carro") == null)
                        producto.setEn_carro(false);
                    else
                        producto.setEn_carro(true);

                    if (orden.equalsIgnoreCase("precio")) {
                        // particionable
                        if (producto.getEsParticionable().equalsIgnoreCase("S")) {
                            producto.setPrecioOrden(rs.getDouble("pre_valor") / producto.getParticion());
                        } else {
                            producto.setPrecioOrden(rs.getDouble("pre_valor"));
                        }
                    } else if (orden.equalsIgnoreCase("ppum")) {
                        producto.setPrecioOrden(producto.getPpum());
                    }

                } else {
                    //Si es producto genérico se deben obtener sus items
                    List lista_item = new ArrayList();
                    lista_item = this.getItems(local_id + "", producto.getPro_id(), cliente_id, orden);
                    if (lista_item.size() == 0)
                        continue;
                    producto.setProductosDTO(lista_item);

                    if ((orden.equalsIgnoreCase("precio") || orden.equalsIgnoreCase("ppum"))
                            && producto.getProductosDTO() != null) {
                        if (producto.getProductosDTO().size() > 0) {
                            if (orden.equalsIgnoreCase("precio")) {
                                producto.setPrecioOrden(((ProductoDTO) producto.getProductosDTO().get(0)).getPrecio());
                            } else if (orden.equalsIgnoreCase("ppum")) {
                                producto.setPrecioOrden(promedioGenericos(producto.getProductosDTO()));
                            }
                        }
                    }
                }
                lista.add(producto);
            }

            if (orden.equalsIgnoreCase("precio") || orden.equalsIgnoreCase("ppum")) {
                Collections.sort(lista);
            }

        } catch (SQLException ex) {
            logger.error("getSearchSeccion - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getSearchSeccion - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getSearchSeccion - Problema SQL (close)", e);
            }
        }
        return lista;
    }

    /**
     * Formatea el patrón de las búsquedas para las consultas
     * 
     * @param patron
     *            Patrón a buscar
     * @return Texto formateado
     */
    private String formatPatron(List patron) {

        String patronaux = "";
        String strOr = " ";

        for (int i = 0; i < patron.size(); ++i) {
            String pa_txt = (String) patron.get(i);
            // contiene tildes
            String pa_txt_sin_acentos = pa_txt.replaceAll("Á", "A");
            pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("É", "E");
            pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Í", "I");
            pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ó", "O");
            pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ú", "U");
            /*
             * patronaux += strOr + "
             * upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_des_corta),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U'))))
             * like '%"+pa_txt_sin_acentos+"%' " + "or
             * upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_des_larga),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U'))))
             * like '%"+pa_txt_sin_acentos+"%' " + "or
             * upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_tipo_producto),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U'))))
             * like '%"+pa_txt_sin_acentos+"%' " + "or
             * upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(mar_nombre),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U'))))
             * like '%"+ pa_txt_sin_acentos + "%' " + "or
             * upper(rtrim(ltrim(pro_cod_sap))) like '%"+ pa_txt_sin_acentos +
             * "%' " ;
             */
            patronaux += strOr
                    + "upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_tipo_producto),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"
                    + pa_txt_sin_acentos
                    + "%' "
                    + "or upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(mar_nombre),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"
                    + pa_txt_sin_acentos + "%' " + "or upper(rtrim(ltrim(pro_cod_sap))) like '%" + pa_txt_sin_acentos
                    + "%' ";
            strOr = " or ";
        }

        return patronaux;

    }

    /**
     * jean: entrega una lista con las categorias del primer nivel
     * 
     * @return
     * @throws ProductosDAOException
     */
    public List categorias() throws ProductosDAOException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            List lista = new ArrayList();
            String sql ="	SELECT CAT_ID, CAT_NOMBRE  " +
            			"	FROM FODBA.FO_CATEGORIAS " +
            			"	WHERE CAT_TIPO = 'I' " +
            			"	AND CAT_ESTADO = 'A' " +
            			"	ORDER BY CAT_NOMBRE ";
            con = JdbcDAOFactory.getConexion();

            ps = con.prepareStatement(sql + " WITH UR");
            rs = ps.executeQuery();
            while (rs.next()) {
                CategoriaDTO categoriaDTO = new CategoriaDTO();
                categoriaDTO.setId(rs.getInt("cat_id"));
                categoriaDTO.setNombre(rs.getString("cat_nombre"));
                lista.add(categoriaDTO);
            }
            return lista;
        } catch (Exception e) {
            logger.error("categorias", e);
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
    }

    /**
     * jean: entrega las subcategorias de la categoria
     * 
     * @param idCategoria
     * @return
     * @throws ProductosDAOException
     */
    public List subCategorias(int idCategoria) throws ProductosDAOException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            List lista = new ArrayList();
            String sql ="	SELECT C.CAT_ID, C.CAT_NOMBRE " +
            			"	FROM FODBA.FO_CATEGORIAS C INNER JOIN FODBA.FO_CATSUBCAT S ON C.CAT_ID = S.SUBCAT_ID " +
						"	WHERE S.CAT_ID = ? AND C.CAT_ESTADO = 'A' " +
						"	ORDER BY C.CAT_NOMBRE ";
            con = JdbcDAOFactory.getConexion();

            ps = con.prepareStatement(sql + " WITH UR");
            ps.setInt(1, idCategoria);
            rs = ps.executeQuery();
            while (rs.next()) {
                CategoriaDTO categoriaDTO = new CategoriaDTO();
                categoriaDTO.setId(rs.getInt("cat_id"));
                categoriaDTO.setNombre(rs.getString("cat_nombre"));
                lista.add(categoriaDTO);
            }
            return lista;
        } catch (Exception e) {
            logger.error("categorias", e);
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////// Los siguientes métodos se usan en jumbo mobi
    // /////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param idLocal
     * @param idSubCategoria
     * @return
     * @throws ProductosDAOException
     */
    public int cantidadProductosDeSubCategoria(int idLocal, int idSubCategoria, int idMarca)
            throws ProductosDAOException {
        int cantidad = 0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;

        try {
            String sql = "select count(*) "
                    + "from fodba.fo_productos pro                                                    "
                    + "inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id         "
                    + "inner join fodba.fo_productos_categorias cat on cat.prca_pro_id = pro.pro_id   "
                    + "inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id                  "
                    + "where pre.pre_estado = 'A' and pro.pro_estado = 'A' and pro_inter_valor > 0 and pre_loc_id = ? and prca_cat_id = ?              ";
            if (idMarca != 0)
                sql += " and mar.mar_id = " + idMarca;

            logger.debug("cantidadProductosDeSubCategoria: " + sql);

            con = JdbcDAOFactory.getConexion();
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setInt(1, idLocal);
            ps.setInt(2, idSubCategoria);
            rs = ps.executeQuery();
            if (rs.next()) {
                cantidad = rs.getInt(1);
            }
            return cantidad;
        } catch (Exception e) {
            logger.error("productos", e);
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
    }

    /**
     * @param idsProductos
     * @param idLocal
     * @param idCategoria
     * @param idSubCategoria
     * @return
     * @throws ProductosDAOException
     */
    public int cantidadProductosPorIds(List idsProductos, int idLocal, int idMarca, int idCategoria, int idSubCategoria)
            throws ProductosDAOException {
        if (idsProductos.size() == 0)
            return 0;
        int cantidad = 0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        //Todos, sin categorias ni subcategorias
        String sql = "select count(distinct pro.pro_id) from fodba.fo_productos pro                           "
                + "inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id                     "
                + "inner join fodba.fo_productos_categorias procat on procat.prca_pro_id = pro.pro_id         "
                + "inner join fodba.fo_catsubcat catsub on catsub.subcat_id = procat.prca_cat_id              "
                + "inner join fodba.fo_categorias sub on sub.cat_id = catsub.subcat_id                        "
                + "inner join fodba.fo_categorias cat on cat.cat_id = catsub.cat_id                           "
                + "where cat.cat_estado = 'A' and sub.cat_estado = 'A' and pro.pro_estado = 'A'               "
                + "and pre.pre_estado = 'A' and pro_inter_valor > 0 and pre.pre_loc_id = " + idLocal
                + " and pro.pro_id in " + DString.join(idsProductos);
        try {

            if (idSubCategoria != 0) {
                //Con subcategoria
                sql += " and sub.cat_id = " + idSubCategoria;
            } else if (idCategoria != 0) {
                //Con Categoria
                sql += " and cat.cat_id = " + idCategoria;
            }

            if (idMarca != 0)
                sql += " and pro_mar_id = " + idMarca;

            //logger.debug("Cantidad pro por ids: " + sql);
            con = JdbcDAOFactory.getConexion();
            ps = con.prepareStatement(sql + " WITH UR");
            rs = ps.executeQuery();
            if (rs.next()) {
                cantidad = rs.getInt(1);
            }
            return cantidad;
        } catch (Exception e) {
            logger.error("cantidadProductosPorIds", e);
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
    }

    /**
     * @param idLocal
     * @param idSubCategoria
     * @return
     * @throws ProductosDAOException
     */
    public List marcasPorSubCategoria(int idLocal, int idSubCategoria) throws ProductosDAOException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List lista = new ArrayList();

        try {
            con = JdbcDAOFactory.getConexion();
            //deben estar activos: producto, categoria y subcategoria
            //además se filtra por local
            String sql = "SELECT distinct mar_id, mar_nombre                            "
                    + "FROM fodba.fo_productos pro                                                   "
                    + "		inner join fodba.fo_productos_categorias on pro_id = prca_pro_id         "
                    + "		inner join fodba.fo_categorias subcat on subcat.cat_id = prca_cat_id     "
                    + "		inner join fodba.fo_catsubcat relcat on relcat.subcat_id = subcat.cat_id "
                    + "		inner join fodba.fo_categorias cat on cat.cat_id = relcat.cat_id         "
                    + "		inner join fodba.fo_marcas on mar_id = pro_mar_id                        "
                    + "     inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id   "
                    + "where pro_estado = 'A' and subcat.cat_estado = 'A' and cat.cat_estado = 'A'   "
                    + "and prca_estado = 'A'  and mar_estado = 'A' and pro_inter_valor > 0           "
                    + "and subcat.cat_id = ?  and pre.pre_loc_id = ?                                 "
                    + "order by mar_nombre ";
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setInt(1, idSubCategoria);
            ps.setInt(2, idLocal);
            logger.debug("marcasPorSubCategoria: " + sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                MarcaDTO marca = new MarcaDTO();
                marca.setMar_id(rs.getLong("mar_id"));
                marca.setMar_nombre(rs.getString("mar_nombre"));
                lista.add(marca);
            }
        } catch (SQLException ex) {
            logger.error("getCategoriaMarcas - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCategoriaMarcas - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            close(rs, ps, con);
        }
        return lista;
    }

    /**
     * 
     * @param idLocal
     * @param marcasIds
     * @return
     * @throws ProductosDAOException
     */
    public List marcasPorIds(List marcasIds, int idLocal) throws ProductosDAOException {
        List lista = new ArrayList();
        if (marcasIds.size() == 0)
            return lista;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	String cadQuery="SELECT distinct mar_id, mar_nombre                            "
                + "FROM fodba.fo_productos pro                                                   "
                + "		inner join fodba.fo_productos_categorias on pro_id = prca_pro_id         "
                + "		inner join fodba.fo_categorias subcat on subcat.cat_id = prca_cat_id     "
                + "		inner join fodba.fo_catsubcat relcat on relcat.subcat_id = subcat.cat_id "
                + "		inner join fodba.fo_categorias cat on cat.cat_id = relcat.cat_id         "
                + "		inner join fodba.fo_marcas on mar_id = pro_mar_id                        "
                + "     inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id   "
                + "where pro_estado = 'A' and subcat.cat_estado = 'A' and cat.cat_estado = 'A'   "
                + "and mar_estado = 'A' and pro_inter_valor > 0 and pre.pre_loc_id = ? and mar_id in "
                + DString.join(marcasIds) + "order by mar_nombre ";
        	
            con = JdbcDAOFactory.getConexion();
            //deben estar activos: producto, categoria y subcategoria
            //además se filtra por local
             
            ps = con.prepareStatement(cadQuery + " WITH UR ");
            ps.setInt(1, idLocal);
            rs = ps.executeQuery();
            while (rs.next()) {
                MarcaDTO marca = new MarcaDTO();
                marca.setMar_id(rs.getLong("mar_id"));
                marca.setMar_nombre(rs.getString("mar_nombre"));
                lista.add(marca);
            }
        } catch (SQLException ex) {
            logger.error("getCategoriaMarcas - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getCategoriaMarcas - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            close(rs, ps, con);
        }
        return lista;
    }

    /**
     * @param idCategoria
     * @param idSubCategoria
     * @param idMarca
     * @param ordenarPor
     * @return
     * @throws ProductosDAOException
     */
    public List productosPorSubCategoria(int idCliente, int idLocal, int idSubCategoria, int idMarca,
            String ordenarPor, int filaNumero, int filaCantidad) throws ProductosDAOException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;

        String orderBy = null;
        String rowNumber = null;
        //////// INI Tipo de orden ////////////////////////////
        //OVER() no soporta alias por eso el case when
        String caseWhenPrecioX = " case when pro_particionable = 'S' and pro_particion <> 0 then pre.pre_valor/pro_particion else pre.pre_valor end ";
        String caseWhenOrdenX = " case when prca_con_pago = 'N' then 100000 else prca_orden end ";
        String caseWhenPrecioUnidad = " CASE WHEN pro_unidad_medida is not null and pro_unidad_medida <> 0 THEN pre_valor/pro_unidad_medida ELSE 0 END ";
        if ("precio".equals(ordenarPor)) {
            orderBy = " order by preciox, ordenx asc, pro.pro_tipo_producto ";
            rowNumber = " order by " + caseWhenPrecioX + ", " + caseWhenOrdenX + " asc, pro.pro_tipo_producto ";
        } else if ("precio_por_unidad".equals(ordenarPor)) {
            orderBy = " order by precio_por_unidad, ordenx asc, pro.pro_tipo_producto ";
            rowNumber = " order by " + caseWhenPrecioUnidad + ", " + caseWhenOrdenX + " asc, pro.pro_tipo_producto ";
        } else if ("nombre".equals(ordenarPor)) {
            orderBy = " order by pro.pro_tipo_producto,  pro.pro_des_corta ";
            rowNumber = orderBy;
        } else {//prca_con_pago desc, prca_orden asc => ordenx
            //por defecto ordena por los proveedores que pagan
            orderBy = " order by ordenx asc, pro.pro_tipo_producto, pro.pro_des_corta ";
            rowNumber = " order by " + caseWhenOrdenX + " asc, pro.pro_tipo_producto, pro.pro_des_corta  ";
        }
        ////////FIN Tipo de orden ////////////////////////////
        try {
            List lista = new ArrayList();
            String sql = "select ROW_NUMBER() OVER (" + rowNumber + ") AS row_number,                            "
                    + "pro.pro_id, pro.pro_tipo_producto, pro.pro_des_corta, pro_tipre,                          "
                    + "pro.pro_imagen_minificha, mar.mar_nombre, mar.mar_id, PRO_INTER_VALOR, PRO_INTER_MAX,     "
                    + "uni.uni_desc, pro_nota, car_nota, pro_id_bo, pro_particionable, pro_particion, pre_valor, "
                    + caseWhenPrecioX + " as preciox, " + caseWhenPrecioUnidad + " as precio_por_unidad,         "
                    + caseWhenOrdenX + " as ordenx                                                               "
                    + "from fodba.fo_productos pro                                                               "
                    + "inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id                    "
                    + "inner join fodba.fo_productos_categorias cat on cat.prca_pro_id = pro.pro_id              "
                    + "inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id                             "
                    + "inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id                    "
                    + "left outer join fodba.fo_carro_compras car on car.car_pro_id = pro.pro_id and car_cli_id = ? "
                    + "where pre.pre_estado = 'A' and pro.pro_estado = 'A' and pro_inter_valor > 0               "
                    + "and pre_loc_id = ? and prca_cat_id = ?                                                    ";
            if (idMarca != 0)
                sql += " and mar.mar_id = " + idMarca;

            sql += orderBy;

            //esto es para el paginado
            sql = "select * from (" + sql + ") as x where row_number >= ? and row_number < ?";
            con = JdbcDAOFactory.getConexion();

            logger.debug(sql);
            logger.debug("desde: " + filaNumero);
            logger.debug("hasta: " + (filaNumero + filaCantidad));
            
            ps = con.prepareStatement(sql + " WITH UR");
            
            ps.setInt(1, idCliente);
            ps.setInt(2, idLocal);
            ps.setInt(3, idSubCategoria);
            ps.setInt(4, filaNumero);
            ps.setInt(5, filaNumero + filaCantidad);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(setearProducto(rs));
            }
            return lista;
        } catch (Exception e) {
            logger.error("productos", e);
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
    }
    
    
    /**
     * 
     * @param idCliente
     * @param idLocal
     * @param idSubCategoria
     * @return
     * @throws ProductosDAOException
     */
    public List productosPorSubCategoria(int idCliente, int idLocal, int idSubCategoria, int idCategoria, String idSession) throws ProductosDAOException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        String join = "";
// 05-10-2012 Mauricio Farias
        if(idSubCategoria == -1)
    	{
        	join = " inner join fodba.fo_productos_categorias cat on cat.prca_pro_id = pro.pro_id "
    			+ " inner join fodba.fo_catsubcat fsub on fsub.subcat_id = cat.prca_cat_id and fsub.cat_id = "+ idCategoria;
    	}
        else
    	{
        	if(idSubCategoria != 0)
                join = "inner join fodba.fo_productos_categorias cat on prca_cat_id = " + idSubCategoria + " and cat.prca_pro_id = pro.pro_id ";
             else //mis favoritos
                join = " inner join fodba.fo_favoritos fav on fav.cliente_id = " + idCliente + " and pro.pro_id = fav.producto_id "
                + " inner join fodba.fo_productos_categorias fpc on fpc.prca_pro_id = fav.producto_id "
                + " inner join fodba.fo_catsubcat fsub on fsub.subcat_id = fpc.prca_cat_id and fsub.cat_id = " + idCategoria 
                + " inner join fodba.fo_categorias subcat on subcat.cat_id = fsub.subcat_id and subcat.cat_estado = 'A'";
    	}
//- 05-10-2012 Mauricio Farias
        

        String caseWhenPrecioX = " case when pro_particionable = 'S' and pro_particion <> 0 then pre.pre_valor/pro_particion else pre.pre_valor end ";
        String caseWhenOrdenX = " case when prca_con_pago = 'N' then 100000 else prca_orden end ";
        String caseWhenPrecioUnidad = " CASE WHEN pro_unidad_medida is not null and pro_unidad_medida <> 0 THEN pre_valor/pro_unidad_medida ELSE 0 END ";
        try {
            List lista = new ArrayList();
            //por defecto ordena por los proveedores que pagan
            //prca_con_pago desc, prca_orden asc => ordenx
            String sql = "select pro.pro_id, pro.pro_tipo_producto, pro.pro_des_corta, pro_tipre,                "
                    + "pro.pro_imagen_minificha, mar.mar_nombre, mar.mar_id, PRO_INTER_VALOR, PRO_INTER_MAX,     "
                    + "uni.uni_desc, pro_nota, car_nota, pro_id_bo, pro_particionable, pro_particion, pre_valor, pre_tienestock, "
                    + caseWhenPrecioX + " as preciox, " + caseWhenPrecioUnidad + " as precio_por_unidad,         "
                    + caseWhenOrdenX + " as ordenx                                                               "
                    + "from fodba.fo_productos pro                                                               "
                    + "inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id                    "
                    
                    + "inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id                             "
                    + "inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id                    "
                    + join
                    + " left outer join fodba.fo_carro_compras car on car.car_pro_id = pro.pro_id and car_cli_id = ? ";
            
            if ( idCliente == 1 ) {
                sql += "AND CAR_INVITADO_ID="+idSession+" ";
            }
            
            //hans santibáñez agrego tabla de categorias para verificar que estado de la categoría sea activo
            if(idSubCategoria == -1)
                sql += " INNER JOIN fodba.FO_CATEGORIAS focat on focat.cat_id = cat.prca_cat_id and focat.cat_estado = 'A' ";
            //hans santibáñez agrego tabla de categorias para verificar que estado de la categoría sea activo
            
            sql += "where pre.pre_estado = 'A' and pro.pro_estado = 'A' and pro_inter_valor > 0               "
                    + "and pre_loc_id = ?                                                                        ";
            
            sql += "order by ordenx asc, pro.pro_tipo_producto, mar.mar_nombre, pro.pro_des_corta ";

            con = JdbcDAOFactory.getConexion();
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setInt(1, idCliente);
            ps.setInt(2, idLocal);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(setearProducto(rs));
            }
            return lista;
        } catch (Exception e) {
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
    }


    /**
     * @param localId
     * @param categoriaId
    * @param clienteId
     * @return
    * @throws ProductosDAOException
     */
    public List productosMasVendidos(int localId, int categoriaId, int clienteId, String idSession) throws ProductosDAOException {
       ResultSet rs = null;
       PreparedStatement ps = null;
       Connection con = null;
       String caseWhenPrecioUnidad = " CASE WHEN pro_unidad_medida is not null and pro_unidad_medida <> 0 THEN pre_valor/pro_unidad_medida ELSE 0 END ";
       
       try {
           List lista = new ArrayList();
           
           String sql = "select distinct pro.pro_id, pro.pro_tipo_producto, pro.pro_des_corta, pro_tipre, " +                
           	"pro.pro_imagen_minificha, mar.mar_nombre, mar.mar_id, PRO_INTER_VALOR, PRO_INTER_MAX,      " +
           	"uni.uni_desc, pro_nota, car_nota, pro_id_bo, pro_particionable, pro_particion, pre_valor, pre_tienestock,  " +
           	caseWhenPrecioUnidad + " as precio_por_unidad, mas.cantidad " + 
           	"from fodba.masv_productos mas " +
           	"inner join fodba.fo_productos pro on mas.producto_id = pro.pro_id " +
           	"inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id and mas.local_id = pre.pre_loc_id " + 
           	"inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id    " +
           	"inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id " +
           	"inner join fodba.fo_productos_categorias subcat on subcat.prca_pro_id = pro.pro_id " +
           	"inner join fodba.fo_catsubcat cat on cat.subcat_id = subcat.prca_cat_id " +
           	"left outer join fodba.fo_carro_compras car on car.car_pro_id = pro.pro_id and car_cli_id = " + clienteId;
           if ( clienteId == 1 ) {
               sql += " AND CAR_INVITADO_ID="+idSession+" ";
           }
           sql += " where pre.pre_estado = 'A' and pro.pro_estado = 'A' and pro_inter_valor > 0 " +            
           	"and pre_loc_id = ? and cat.cat_id = ? ";
           
           sql = "select ROW_NUMBER() OVER (order by cantidad desc) AS row_number, pro_id, pro_tipo_producto, pro_des_corta, pro_tipre, pro_imagen_minificha,  "
              + "pre_valor, pre_tienestock, mar_nombre, mar_id, PRO_INTER_VALOR, PRO_INTER_MAX, uni_desc, pro_nota, car_nota, pro_id_bo, "
              + "precio_por_unidad, pro_particionable, pro_particion                                    "
              + " from (" + sql + ") as x " ;
           
           sql = "select pro_id, pro_tipo_producto, pro_des_corta, pro_tipre, pro_imagen_minificha,  "
              + "pre_valor, pre_tienestock, mar_nombre, mar_id, PRO_INTER_VALOR, PRO_INTER_MAX, uni_desc, pro_nota, car_nota, pro_id_bo, "
              + "precio_por_unidad, pro_particionable, pro_particion                                    "
              + " from (" +sql +") as y where y.row_number <= 4";
           
           	con = JdbcDAOFactory.getConexion();
           	ps = con.prepareStatement(sql + " WITH UR");
           	ps.setInt(1, localId);
           	ps.setInt(2, categoriaId);
           	rs = ps.executeQuery();
           	
           	while (rs.next()) {
           	   lista.add(setearProducto(rs));
           	}
           	return lista;
       } catch (Exception e) {
           throw new ProductosDAOException(e);
       } finally {
           close(rs, ps, con);
       }
    }
    /**
     * 
     * @param idCliente
     * @param idsProductos
     * @param idLocal
     * @param idMarca
     * @param idCategoria
     * @param idSubCategoria
     * @param ordenarPor
     * @param filaNumero
     * @param filaCantidad
     * @return
     * @throws ProductosDAOException
     */
    public List productosPorIds(int idCliente, List idsProductos, int idLocal, int idMarca, int idCategoria,
            int idSubCategoria, String ordenarPor, int filaNumero, int filaCantidad) throws ProductosDAOException {
        List lista = new ArrayList();
        if (idsProductos.size() == 0)
            return lista;

        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {

            if (idsProductos.size() != 0) {
                String sql = getSqlProductosPorIds(idCliente, idsProductos, idLocal, idMarca, idCategoria,
                        idSubCategoria, ordenarPor, filaNumero, filaCantidad);

                //logger.debug("sql: " + sql);
                con = JdbcDAOFactory.getConexion();
                ps = con.prepareStatement(sql + " WITH UR");
                rs = ps.executeQuery();

                HashSet set = new HashSet();
                while (rs.next()) {
                    ProductoDTO pro = setearProducto(rs);
                    if (!set.contains("" + pro.getPro_id())) {
                        lista.add(pro);
                        set.add("" + pro.getPro_id());
                    }
                }
            }
            return lista;
        } catch (Exception e) {
            logger.error("productosPorIds: ", e);
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
    }
    
    /**
     * Entrega la lista de productos según lista de idsProductos y que esten publicados
     * @param idCliente
     * @param idsProductos
     * @param idLocal
     * @return
     */
    public List productosPorIds(int idCliente, List idsProductos, int idLocal) throws ProductosDAOException {
       if (idsProductos.size() == 0)
           return new ArrayList();

       ResultSet rs = null;
       PreparedStatement ps = null;
       Connection con = null;
       List lista = new ArrayList();
       try {
         String caseWhenPrecioUnidad = " CASE WHEN pro_unidad_medida is not null and pro_unidad_medida <> 0 THEN pre_valor/pro_unidad_medida ELSE 0 END ";
         String caseWhenPrecioX = " case when pro_particionable = 'S' and pro_particion <> 0 then pre.pre_valor/pro_particion else pre.pre_valor end ";
         String sql = "select distinct pro.pro_id, pro.pro_tipo_producto, pro.pro_des_corta, pro_tipre, "
         + "pro_imagen_minificha, pre.pre_valor, mar.mar_nombre, mar.mar_id, PRO_INTER_VALOR, PRO_INTER_MAX, "
         + "uni.uni_desc, pro_nota, car_nota, pro_id_bo, pro_particionable, pro_particion,             "
         + caseWhenPrecioUnidad + " as precio_por_unidad, " + caseWhenPrecioX + " as preciox           "
         + "from fodba.fo_productos pro                                                                "
         + "inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id                     "
         + "inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id                              "
         + "inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id                     "
         + "inner join fodba.fo_productos_categorias procat on procat.prca_pro_id = pro.pro_id         "
         + "inner join fodba.fo_catsubcat catsub on catsub.subcat_id = procat.prca_cat_id              "
         + "inner join fodba.fo_categorias sub on sub.cat_id = catsub.subcat_id                        "
         + "inner join fodba.fo_categorias cat on cat.cat_id = catsub.cat_id                           "
         + "left outer join fodba.fo_carro_compras car on car.car_pro_id = pro.pro_id and car_cli_id = "
         + idCliente + " where sub.cat_estado = 'A' and cat.cat_estado = 'A' and pro.pro_estado = 'A'  "
         + " and pre.pre_estado = 'A' and pro_inter_valor > 0 and pre.pre_loc_id = " + idLocal
         + " and pro.pro_id in " + DString.join(idsProductos);
         //logger.debug("sql: " + sql);
         con = JdbcDAOFactory.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         rs = ps.executeQuery();
         
         HashSet set = new HashSet();
         while (rs.next()) {
           ProductoDTO pro = setearProducto(rs);
           if (!set.contains("" + pro.getPro_id())) {
              lista.add(pro);
              set.add("" + pro.getPro_id());
           }
         }
       
       } catch (Exception e) {
           logger.error("productosPorIds: ", e);
           throw new ProductosDAOException(e);
       } finally {
           close(rs, ps, con);
       }
       
       return lista;
    }
    
    public ProductoDTO productoById(int idCliente, int idProducto, int idLocal) throws ProductosDAOException {
       
    	ProductoDTO pro = new ProductoDTO();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        List lista = new ArrayList();
        try {
          String caseWhenPrecioUnidad = " CASE WHEN pro_unidad_medida is not null and pro_unidad_medida <> 0 THEN pre_valor/pro_unidad_medida ELSE 0 END ";
          String caseWhenPrecioX = " case when pro_particionable = 'S' and pro_particion <> 0 then pre.pre_valor/pro_particion else pre.pre_valor end ";
          String sql = "select distinct pro.pro_id, pro.pro_tipo_producto, pro.pro_des_corta, pro_tipre, "
          + "pro_imagen_minificha, pre.pre_valor, mar.mar_nombre, mar.mar_id, PRO_INTER_VALOR, PRO_INTER_MAX, "
          + "uni.uni_desc, pro_nota, car_nota, pro_id_bo, pro_particionable, pro_particion,             "
          + caseWhenPrecioUnidad + " as precio_por_unidad, " + caseWhenPrecioX + " as preciox           "
          + "from fodba.fo_productos pro                                                                "
          + "inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id                     "
          + "inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id                              "
          + "inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id                     "
          + "inner join fodba.fo_productos_categorias procat on procat.prca_pro_id = pro.pro_id         "
          + "inner join fodba.fo_catsubcat catsub on catsub.subcat_id = procat.prca_cat_id              "
          + "inner join fodba.fo_categorias sub on sub.cat_id = catsub.subcat_id                        "
          + "inner join fodba.fo_categorias cat on cat.cat_id = catsub.cat_id                           "
          + "left outer join fodba.fo_carro_compras car on car.car_pro_id = pro.pro_id and car_cli_id = "
          + idCliente + " where sub.cat_estado = 'A' and cat.cat_estado = 'A' and pro.pro_estado = 'A'  "
          + " and pre.pre_estado = 'A' and pro_inter_valor > 0 and pre.pre_loc_id = " + idLocal
          + " and pro.pro_id = " + idProducto;
          //logger.debug("sql: " + sql);
          con = JdbcDAOFactory.getConexion();
          ps = con.prepareStatement(sql + " WITH UR");
          rs = ps.executeQuery();
          
          HashSet set = new HashSet();
          while (rs.next()) {
            pro = setearProducto(rs);
            if (!set.contains("" + pro.getPro_id())) {
               lista.add(pro);
               set.add("" + pro.getPro_id());
            }
          }
        
        } catch (Exception e) {
            logger.error("productosPorIds: ", e);
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
        return pro;
     }
    //INICIO INDRA 22-10-2012
    public List getProductosDespubOrSinStock(long idCliente, List idProductos, int idLocal) throws ProductosDAOException{
    	
    	List lista = new ArrayList();
    	ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		
    	try {
    		if (idProductos.size() == 0){
    	           return lista;
    		} else {
    			
    			String sql = "SELECT FO_PRODUCTOS.PRO_ID, FO_PRODUCTOS.PRO_TIPO_PRODUCTO, FO_MARCAS.MAR_ID, FO_MARCAS.MAR_NOMBRE" + " " +   
							 "FROM FO_PRODUCTOS" + " " +  
							 "INNER JOIN FO_PRECIOS_LOCALES ON FO_PRECIOS_LOCALES.PRE_PRO_ID = FO_PRODUCTOS.PRO_ID" + " " + 
							 "INNER JOIN FO_MARCAS ON FO_PRODUCTOS.PRO_MAR_ID = FO_MARCAS.MAR_ID" + " " + 
							 "WHERE FO_PRODUCTOS.PRO_ID in " + DString.join(idProductos) + " " + 
							 "AND FO_PRECIOS_LOCALES.PRE_LOC_ID = " + idLocal + " " +  
							 "AND (FO_PRODUCTOS.PRO_ESTADO = 'D' or FO_PRECIOS_LOCALES.PRE_ESTADO = 'D' OR FO_PRECIOS_LOCALES.PRE_TIENESTOCK = 0)";

    			System.out.println("SQL: "+ sql);
				 logger.debug("sql: " + sql);
				 con = JdbcDAOFactory.getConexion();
				 ps = con.prepareStatement(sql + " WITH UR");
				 rs = ps.executeQuery();

				 HashSet set = new HashSet();
				 while (rs.next()) {
				 	ProductoDTO pro = new ProductoDTO();
			        pro.setPro_id(rs.getInt("pro_id"));
			        pro.setTipo_producto(rs.getString("pro_tipo_producto"));
			        pro.setMarca(rs.getString("mar_nombre"));
			        pro.setMarca_id(rs.getLong("mar_id"));
			        
				 	lista.add(pro);
				 }
    		}
    	
    		return lista;
    		
    	} catch (Exception ex) {
    		logger.error("Problema en obtener productos despublicados o sin Stock");
    		throw new ProductosDAOException(ex);
    	}
    	finally {
            close(rs, ps, con);
        }
    	
    }
    
    public List getProductosSinStockDespublicadosPorLista(long idCliente, int idLista, int idLocal) throws ProductosDAOException {
    	
    	final String VALOR_DESPUBLICADO = "D";
    	
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	List productos = new ArrayList();
    	Connection con = null;
    	String sql = "SELECT fp.PRO_ID, fp.PRO_TIPO_PRODUCTO, fm.MAR_ID, fm.MAR_NOMBRE " +
					 "FROM FO_PRODUCTOS fp " +
					 "INNER JOIN fo_marcas fm ON fp.PRO_MAR_ID = fm.MAR_ID " +
					 "inner join fo_precios_locales fpl ON fpl.PRE_PRO_ID = fp.PRO_ID " +
					 "inner join FO_CH_PRODUCTOS fchp ON fchp.CHP_PRO_ID = fp.pro_id " +
					 "WHERE fchp.chp_ch_id = ?" + 
					 "AND fpl.PRE_LOC_ID = ?" +
					 "AND (fp.PRO_ESTADO = '" + VALOR_DESPUBLICADO + "' " +  
					 "OR fpl.PRE_ESTADO = '" + VALOR_DESPUBLICADO +  "' " +
					 "OR fpl.PRE_TIENESTOCK = 0)";
    	try {
    		con = JdbcDAOFactory.getConexion();
    		ps = con.prepareStatement(sql+ " WITH UR");
    		ps.setLong(1, idLista);
    		ps.setLong(2, idLocal);
    		rs = ps.executeQuery();
    		
    		while ( rs.next()) {
    			ProductoDTO producto = new ProductoDTO();
		        producto.setPro_id(rs.getInt("pro_id"));
		        producto.setTipo_producto(rs.getString("pro_tipo_producto"));
		        producto.setMarca(rs.getString("mar_nombre"));
		        producto.setMarca_id(rs.getLong("mar_id"));
		        
		        productos.add(producto);
    		}
    		
    	} catch ( SQLException e) {
    		logger.error("Problema en obtener productos despublicados o sin Stock");
    		e.printStackTrace();
    		throw new ProductosDAOException(e);
    	} finally {
            close(rs, ps, con);
        }
    	
    	return productos;

    }
    // FIN INDRA 22-10-2012

    /**
     * (J)Entrega el SQL correcto según los datos que vienen
     * 
     * @param idCliente
     * @param idsProductos
     * @param idLocal
     * @param idCategoria
     * @param idSubCategoria
     * @param ordenarPor
     * @param filaNumero
     * @param filaCantidad
     * @return
     */
    private String getSqlProductosPorIds(int idCliente, List idsProductos, int idLocal, int idMarca, int idCategoria,
            int idSubCategoria, String ordenarPor, int filaNumero, int filaCantidad) {
        String sql = null;
        String orderBy = null;
        String orderBy2 = "";
        String rowNumber = null;
        String caseWhenPrecioUnidad = " CASE WHEN pro_unidad_medida is not null and pro_unidad_medida <> 0 THEN pre_valor/pro_unidad_medida ELSE 0 END ";
        String caseWhenPrecioX = " case when pro_particionable = 'S' and pro_particion <> 0 then pre.pre_valor/pro_particion else pre.pre_valor end ";

        if ("precio".equals(ordenarPor)) {
            orderBy = " order by preciox ";
            rowNumber = " order by " + caseWhenPrecioX;
            orderBy2 = " order by preciox ";

        } else if ("precio_por_unidad".equals(ordenarPor)) {
            orderBy = " order by precio_por_unidad ";
            orderBy2 = " order by precio_por_unidad ";
            //OVER() no soporta alias, es este caso precio_por_unidad
            rowNumber = " order by " + caseWhenPrecioUnidad;
        } else if ("nombre".equals(ordenarPor)) {
            //por nombre
            orderBy = " order by pro.pro_tipo_producto ";
            orderBy2 = " order by pro_tipo_producto ";
            rowNumber = orderBy;
        } else {
            orderBy = "";
            rowNumber = orderBy;
            orderBy2 = "";
        }

        String select = "select distinct ROW_NUMBER() OVER (" + rowNumber + ") AS row_number, "
                + "pro.pro_id, pro.pro_tipo_producto as pro_tipo_producto, pro.pro_des_corta, pro_tipre, "
                + "pro_imagen_minificha, pre.pre_valor as pre_valor, mar.mar_nombre, mar.mar_id, PRO_INTER_VALOR, PRO_INTER_MAX, "
                + "uni.uni_desc, pro_nota, car_nota, pro_id_bo, pro_particionable, pro_particion, "
                + caseWhenPrecioUnidad + " as precio_por_unidad, " + caseWhenPrecioX + " as preciox ";
        //Todos, sin categorias ni subcategorias
        sql = select + "from fodba.fo_productos pro                                                           "
                + "inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id                     "
                + "inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id                              "
                + "inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id                     "
                + "inner join fodba.fo_productos_categorias procat on procat.prca_pro_id = pro.pro_id         "
                + "inner join fodba.fo_catsubcat catsub on catsub.subcat_id = procat.prca_cat_id              "
                + "inner join fodba.fo_categorias sub on sub.cat_id = catsub.subcat_id                        "
                + "inner join fodba.fo_categorias cat on cat.cat_id = catsub.cat_id                           "
                + "left outer join fodba.fo_carro_compras car on car.car_pro_id = pro.pro_id and car_cli_id = "
                + idCliente + " where sub.cat_estado = 'A' and cat.cat_estado = 'A' and pro.pro_estado = 'A'  "
                + " and pre.pre_estado = 'A' and pro_inter_valor > 0 and pre.pre_loc_id = " + idLocal
                + " and pro.pro_id in " + DString.join(idsProductos);

        if (idSubCategoria != 0) {
            //Con subcategoria
            sql += " and sub.cat_id = " + idSubCategoria;
        } else if (idCategoria != 0) {
            //Con Categoria
            sql += " and cat.cat_id = " + idCategoria;
        }

        if (idMarca != 0)
            sql += " and mar.mar_id = " + idMarca;
        sql += orderBy;

        sql = "select distinct pro_id, pro_tipo_producto, pro_des_corta, pro_tipre, pro_imagen_minificha,  "
                + "pre_valor, mar_nombre, mar_id, PRO_INTER_VALOR, PRO_INTER_MAX, uni_desc, pro_nota, car_nota, pro_id_bo, "
                + "precio_por_unidad, pro_particionable, pro_particion, preciox                                    "
                + " from (" + sql + ") as x where row_number >= " + filaNumero + " and row_number < "
                + (filaNumero + filaCantidad);
        sql += orderBy2;
        //logger.debug(sql);
        return sql;
    }

    /**
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    private ProductoDTO setearProducto(ResultSet rs) throws SQLException {
        ProductoDTO p = new ProductoDTO();
        p.setPro_id(rs.getInt("pro_id"));
        p.setTipo_producto(rs.getString("pro_tipo_producto"));
        p.setDescripcion(rs.getString("pro_des_corta"));
        p.setTipre(rs.getString("pro_tipre"));
        p.setImg_chica(rs.getString("pro_imagen_minificha"));
        p.setPrecio(rs.getDouble("pre_valor"));
        if (rs.getInt("pre_tienestock") == 0)
            p.setTieneStock(false);
        else
            p.setTieneStock(true);
        p.setMarca(rs.getString("mar_nombre"));
        p.setMarca_id(rs.getLong("mar_id"));
        p.setCantidadMinima(rs.getBigDecimal("PRO_INTER_VALOR"));
        p.setCantidadMaxima(rs.getBigDecimal("PRO_INTER_MAX"));
        p.setUnidad_nombre(rs.getString("uni_desc"));
        p.setPpum(rs.getDouble("precio_por_unidad"));
        p.setCon_nota("S".equals(rs.getString("pro_nota")));
        p.setNota(rs.getString("car_nota"));
        p.setPro_id_bo(rs.getInt("pro_id_bo"));
        p.setEsParticionable(rs.getString("pro_particionable"));
        p.setParticion(rs.getInt("pro_particion"));
        p.setDescripcionCompleta(p.getTipo_producto() + " " + p.getDescripcion() + " " + p.getMarca());
        return p;
    }

    /**
     * @param clienteId
     * @return
     * @throws ProductosDAOException
     */
    public Hashtable productosCarro(int clienteId, String idSession) throws ProductosDAOException {
        Hashtable productos = new Hashtable();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;

        try {
            String sql = "select car_pro_id, car_cantidad, car_nota from fodba.fo_carro_compras where car_cli_id = ? ";
            
            if ( clienteId == 1 ) {
                sql += "AND CAR_INVITADO_ID="+idSession+" ";
            }
            con = JdbcDAOFactory.getConexion();
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setInt(1, clienteId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer productoId = new Integer(rs.getInt("car_pro_id"));
                ProductoCarroDTO productoCarroDTO = new ProductoCarroDTO();
                productoCarroDTO.setId(rs.getInt("car_pro_id"));
                productoCarroDTO.setNota(rs.getString("car_nota"));
                productoCarroDTO.setCantidad(rs.getBigDecimal("car_cantidad"));
                productos.put(productoId, productoCarroDTO);
            }
            return productos;
        } catch (Exception e) {
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
    }

    /**
     * Entrega todos los productos destacados de hoy según el local
     * 
     * @return
     * @throws ProductosDAOException
     */
    public Hashtable getProductosDestacadosDeHoy(int localId) throws ProductosDAOException {
        /*
         * Se ordena por "id asc" ya que si un producto esta destacados más de
         * una vez se deja el último. Se reutiliza el objeto destacadoDTO para
         * ocupar menos memoria.
         *  
         */
        String sql = "select id, descripcion, imagen, producto_id "
                + " from bodba.destacados d inner join bodba.destacados_productos dp on d.id = dp.destacado_id "
                + " inner join bodba.destacados_locales dl on d.id = dl.destacado_id "
                + " where local_id = ? and current_timestamp >= fecha_hora_ini and current_timestamp <= fecha_hora_fin "
                + " order by id asc";
        Hashtable productosDestacados = new Hashtable();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = JdbcDAOFactory.getConexion();
            ps = con.prepareStatement(sql + " WITH UR");
            ps.setInt(1, localId);
            rs = ps.executeQuery();
            int id = -1;
            DestacadoDTO destacadoDTO = null;
            while (rs.next()) {
                if (rs.getInt("id") != id) {
                    id = rs.getInt("id");
                    destacadoDTO = new DestacadoDTO();
                    destacadoDTO.setId(rs.getInt("id"));
                    destacadoDTO.setDescripcion(rs.getString("descripcion"));
                    destacadoDTO.setImagen(rs.getString("imagen"));
                }
                productosDestacados.put(new Integer(rs.getInt("producto_id")), destacadoDTO);
            }
            return productosDestacados;
        } catch (Exception e) {
            throw new ProductosDAOException(e);
        } finally {
            close(rs, ps, con);
        }
    }

    // Los siguientes metodos intentan simplificar el manejo de excepciones al
    // cerrar los recursos
    /**
     * Cierra todo
     * 
     * @param rs
     * @param ps
     * @param con
     */
    private void close(ResultSet rs, PreparedStatement ps, Connection con) {
        close(rs);
        close(ps);
        close(con);
    }

    /**
     * Cierra el resulset
     * 
     * @param rs
     */
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Error al cerrar el resulset: ", e);
            }
        }
    }

    /**
     * Cierra el statement
     * 
     * @param st
     */
    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error("Error al cerrar el Statement: ", e);
            }
        }
    }

    /**
     * Cierra la conexión
     * 
     * @param con
     */
    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                logger.error("Error al cerrar la Connection: ", e);
            }
        }
    }
    /**
     * Carro Abandonado
     * @param clienteId
     * @throws ProductosDAOException
     */ 
	 public void updateFechaMiCarro(int clienteId) throws  ProductosDAOException {	
		Connection con = null;
		PreparedStatement st 	= null;
		boolean result = false;
		
		String sql = " UPDATE FODBA.FO_CARRO_COMPRAS " +
					 " SET CAR_FEC_MICARRO = CURRENT TIMESTAMP " +
					 " WHERE CAR_CLI_ID =  " + clienteId ;
				
		try {
			con = JdbcDAOFactory.getConexion();
			st = con.prepareStatement(sql);
			
			int i = st.executeUpdate();
			if(i>0)
				result = true;
	
		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			close(st, con);
		}
	}
	 
	 /**
	 * Cierra todo
	 * 
     * @param rs
	 * @param ps
	 * @param con
	 */

	    private void close(Statement st, Connection con) {
	        close(st);
	        close(con);
	    }

/**
	 * Obtiene información de la ficha del producto web
	 * @param idProd
	 * @return
	 * @throws ProductosDAOException
	 */	 
	public List getFichaProductoById(long idProd) throws ProductosDAOException {
		Connection conexion = null;
		List lstDatosFicha = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		FichaProductoEntity datosFichaEntity = new FichaProductoEntity();

		logger.debug("getFichaProductoPorId");
		String sql =
			"SELECT PFT_PRO_ID, PFT_PFI_ITEM, PFT_PFI_SECUENCIA, PFT_DESCRIPCION_ITEM, PFT_ESTADO_ITEM " +
			"FROM FODBA.FO_PRODUCTOS_FICHA_TEC " +
			"WHERE PFT_PRO_ID = " + idProd + " ORDER BY PFT_PFI_SECUENCIA";
		
		logger.debug("SQL: " + sql);
				
		try {
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				datosFichaEntity = new FichaProductoEntity();
				datosFichaEntity.setPftProId(rs.getLong("PFT_PRO_ID"));
				datosFichaEntity.setPftPfiItem(rs.getLong("PFT_PFI_ITEM"));
				datosFichaEntity.setPftPfiSecuencia(rs.getInt("PFT_PFI_SECUENCIA"));
				datosFichaEntity.setPftDescripcionItem(rs.getString("PFT_DESCRIPCION_ITEM"));
				lstDatosFicha.add(datosFichaEntity);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				close(rs, stm, conexion);
			} catch (SQLException e) {
				logger.error("[Metodo] : getFichaProductoById - Problema SQL (close)", e);
			}
		}
		//logger.debug("prod:"+prod.getDesc_corta());
		return lstDatosFicha;
	}
	
	/**
	 * Obtiene todos los item de la ficha tecnica
	 * @return
	 * @throws ProductosDAOException
	 */
	public List getItemFichaProductoAll() throws ProductosDAOException {
		Connection conexion = null;
		List lstItems = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		ItemFichaProductoDTO itemFichaDTO = new ItemFichaProductoDTO();

		logger.debug("getItemFichaProductoAll");
		String sql = "SELECT PFI_ITEM, PFI_DESCRIPCION " +
					 "FROM FODBA.FO_PRODUCTOS_ITEM_FICHA_TEC ORDER ORDER BY PFI_ITEM";
		
		logger.debug("SQL: " + sql);
				
		try {
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(sql + " WITH UR");

			rs = stm.executeQuery();
			while (rs.next()) {
				itemFichaDTO = new ItemFichaProductoDTO();
				itemFichaDTO.setPfiItem(rs.getLong("PFI_ITEM"));
				itemFichaDTO.setPfiDescripcion(rs.getString("PFI_DESCRIPCION"));
				lstItems.add(itemFichaDTO);
			}
			
		} catch (Exception e) {
			logger.debug("Problema getItemFichaProductoAll :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				close(rs, stm, conexion);
			} catch (SQLException e) {
				logger.error("[Metodo] : getItemFichaProductoAll - Problema SQL (close)", e);
			}
		}
		logger.debug("cantidad:"+lstItems.size());
		return lstItems;
	}
	
	/**
	 * Verifica si el producto tiene ficha
	 * 
	 * @param  cod_prod long 
	 * @return boolean, devuelve <i>true</i> si la ficha existe, caso contrario, devuelve <i>false</i>.
	 * @throws ProductosDAOException
	 * */
	public boolean tieneFichaProductoById(long cod_prod) throws ProductosDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		long estado = 0;
		try {

			conexion = JdbcDAOFactory.getConexion();
			logger.debug("en tieneFichaProductoById");
			String sql = "SELECT EPF_ESTADO_FICHA_TEC FROM FODBA.FO_ESTADO_PRODUCTOS_FICHA_TEC WHERE EPF_PRO_ID = ? ";
			logger.debug(sql);
			logger.debug("pft_pro_id="+cod_prod);
			stm = conexion.prepareStatement(sql  + " WITH UR");
			
			stm.setLong(1, cod_prod);
			rs = stm.executeQuery();		
			if(rs.next()) {
				estado = rs.getLong("EPF_ESTADO_FICHA_TEC");
			}
			if (estado == 1) {
				result = true;
			}	
		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ProductosDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				close(rs, stm, conexion);
			} catch (SQLException e) {
				logger.error("[Metodo] : tieneFichaProductoById - Problema SQL (close)", e);
			}
		}
		return result;
	}
	
    
    /**
     * @param idCliente
     * @return
     */
    public List productosSustitutosByClienteFO(long idCliente) throws ProductosDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List productos = new ArrayList();
        try {
            String sql = "select pro_id " +
                         "from fo_sustitutos_clientes sc " +
                         "where sc.cli_id = ?";
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");          
            stm.setLong(1,idCliente);
            logger.debug("SQL productosSustitutosByCliente: " + sql);
            rs = stm.executeQuery();
            while ( rs.next() ) {
                ProductoDTO producto = new ProductoDTO();
                producto.setPro_id(rs.getLong("pro_id"));
                productos.add(producto);                
            }
            
        } catch (SQLException ex) {
            logger.error("productosSustitutosByCliente - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("productosSustitutosByCliente - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("productosSustitutosByCliente - Problema SQL (close)", e);
            }
        }
        return productos;
    }
    

    /**
     * @param idCliente
     * @param esResumen
     * @return
     */
    public List productosSustitutosPorCategoriaFO(long idCliente, boolean esResumen) throws ProductosDAOException {
        Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List productosPorCategoria = new ArrayList();
        ProductosSustitutosCategoriasDTO categoria = new ProductosSustitutosCategoriasDTO();
        try {
            String sql = "";
            
            if (esResumen) {
                //Mostramos solo los productos de la actual compra del cliente
                sql =   "select cat.CAT_ID, cat.CAT_NOMBRE, pro.pro_id, pro.pro_des_corta, pro.pro_unidad_medida, " +
                        "pro.pro_tipre, pro.pro_tipo_producto, ma.MAR_NOMBRE, sc.ID_CRITERIO, sc.DESC_CRITERIO, " +
                        "scr.DESCRIPCION, sc.ASIGNO_CLIENTE " +
                        "from fo_ch_productos ch " +
                        "    join fo_sustitutos_clientes sc on (ch.CHP_PRO_ID = sc.PRO_ID) " +
                        "    join fo_sustitutos_criterio scr on (sc.ID_CRITERIO = scr.ID_CRITERIO) " +
                        "    join fo_productos pro on (sc.PRO_ID = pro.PRO_ID) " +
                        "    join " +
                        "       ( " +
                        "       select pro1.pro_id as pro_id, min(cat1.cat_id) as cat_id " +
                        "       from fo_ch_productos ch1 " +
                        "         join fo_sustitutos_clientes sc1 on (ch1.CHP_PRO_ID = sc1.PRO_ID) " +
                        "         join fo_sustitutos_criterio scr1 on (sc1.ID_CRITERIO = scr1.ID_CRITERIO) " +
                        "         join fo_productos pro1 on (sc1.PRO_ID = pro1.PRO_ID) " +
                        "         join fo_productos_categorias prca1 on pro1.PRO_ID = prca1.prca_pro_id " +
                        "         join fo_categorias sub1 on sub1.CAT_ID = prca1.prca_cat_id AND sub1.cat_estado = 'A' " +
                        "         join fo_catsubcat subcat1 on subcat1.subcat_id = sub1.cat_id " +
                        "         join fo_categorias cat1 on cat1.CAT_ID = subcat1.cat_id AND cat1.cat_estado = 'A' " +
                        "       where sc1.CLI_ID = " + idCliente + " and ch1.CHP_CH_ID in ( " +
                        "          select max(ch_id) as maxid from fo_ch_compra_historicas where ch_cli_id = " + idCliente + " " +
                        "          ) " +
                        "          group by pro1.pro_id " +
                        "       ) as x " +
                        "       on x.pro_id = pro.pro_id " +
                        "    join fo_categorias cat on cat.CAT_ID = x.cat_id " +
                        "    left join fo_marcas ma on (pro.PRO_MAR_ID = ma.MAR_ID) " +
                        "where sc.CLI_ID = " + idCliente + " and ch.CHP_CH_ID in ( " +
                        "   select max(ch_id) as maxid from fo_ch_compra_historicas where ch_cli_id = " + idCliente + " " +
                        "   ) " +
                        "order by cat.CAT_NOMBRE ";
            } else {
                sql =   "select distinct cat.CAT_ID, cat.CAT_NOMBRE, pro.pro_id, pro.pro_des_corta, " +
                        "pro.pro_unidad_medida, pro.pro_tipre, pro.pro_tipo_producto, ma.MAR_NOMBRE, " +
                        "sc.ID_CRITERIO, sc.DESC_CRITERIO, scr.DESCRIPCION, sc.ASIGNO_CLIENTE " +
                        "from fo_sustitutos_clientes sc " +
                        "  join fo_sustitutos_criterio scr on (sc.ID_CRITERIO = scr.ID_CRITERIO) " +
                        "  join fo_productos pro on sc.PRO_ID = pro.PRO_ID " +
                        "  join " +
                        "    ( " +
                        "     select sc1.pro_id as pro_id, min(cat1.cat_id) as cat_id " +
                        "     from fo_sustitutos_clientes sc1 " +
                        "       join fo_sustitutos_criterio scr1 on (sc1.ID_CRITERIO = scr1.ID_CRITERIO)" +
                        "       join fo_productos pro1 on sc1.PRO_ID = pro1.PRO_ID" +
                        "       JOIN fo_productos_categorias prca1 on pro1.PRO_ID = prca1.prca_pro_id " +
                        "       JOIN fo_categorias sub1 on sub1.CAT_ID = prca1.prca_cat_id AND sub1.cat_estado = 'A' " +
                        "       JOIN fo_catsubcat subcat1 on subcat1.subcat_id = sub1.cat_id " +
                        "       JOIN fo_categorias cat1 on cat1.CAT_ID = subcat1.cat_id AND cat1.cat_estado = 'A' " +
                        "    WHERE sc1.CLI_ID = " + idCliente + " " +
                        "    group by sc1.pro_id " +
                        "    ) as x " +
                        "    on x.pro_id = pro.pro_id " +
                        "JOIN fo_categorias cat on cat.CAT_ID = x.cat_id " +
                        "left join fo_marcas ma on (pro.PRO_MAR_ID = ma.MAR_ID) " +
                        "where sc.CLI_ID = " + idCliente + " " +
                        "order by cat.CAT_NOMBRE ";
            }
            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");          
            rs = stm.executeQuery();
            long ultIdCategoria = 0;
            List productos = new ArrayList();
            List prodsGuardados = new ArrayList();
            while ( rs.next() ) {
                boolean flag = true;
                //Revisamos que no se haya guardado el producto antes
                for ( int i = 0; i < prodsGuardados.size(); i++ ) {
                    if ( rs.getString("pro_id").compareTo( prodsGuardados.get(i).toString() ) == 0 ) {
                        flag = false;
                        break;
                    }
                }
                if ( !flag ) {
                    continue;
                }
                if ( ultIdCategoria != rs.getLong("cat_id") ) {
                    if ( ultIdCategoria != 0 ) {
                        categoria.setSustitutos(productos);
                        productosPorCategoria.add(categoria);
                    }
                    categoria = new ProductosSustitutosCategoriasDTO();
                    productos = new ArrayList();
                    categoria.setId(rs.getLong("cat_id"));
                    categoria.setCategoria(rs.getString("cat_nombre"));
                    ultIdCategoria = rs.getLong("cat_id");
                }
                
                ProductoDTO producto = new ProductoDTO();
                producto.setPro_id(rs.getLong("pro_id"));
                producto.setDescripcion(rs.getString("pro_tipo_producto") + " " + rs.getString("pro_des_corta") );
                producto.setMarca(rs.getString("mar_nombre"));
                
                CriterioSustitutoDTO sustituto = new CriterioSustitutoDTO();
                sustituto.setIdCriterio(rs.getLong("id_criterio"));
                sustituto.setDescripcion(rs.getString("descripcion"));
                sustituto.setSustitutoCliente(rs.getString("desc_criterio"));
                sustituto.setAsignoCliente(rs.getString("asigno_cliente"));
                producto.setCriterio(sustituto);
                
                prodsGuardados.add(""+producto.getPro_id());
                productos.add(producto);                
            }
            
            if ( ultIdCategoria != 0 ) {
                categoria.setSustitutos(productos);
                productosPorCategoria.add(categoria);
            }
            
        } catch (SQLException ex) {
            logger.error("productosSustitutosPorCategoria - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("productosSustitutosPorCategoria - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("productosSustitutosPorCategoria - Problema SQL (close)", e);
            }
        }
        return productosPorCategoria;
    }

	public ProductoDTO getProductoPrecioFO(String idProd, long idLocal) throws ProductosDAOException {
		Connection conexion = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        ProductoDTO producto = new ProductoDTO();
        try {
            String sql = "select pl.pre_valor from fodba.fo_productos p " +
            		"inner join  fodba.FO_PRECIOS_LOCALES pl " +
            		"on p.PRO_ID = pl.pre_pro_id " +
            		"where p.PRO_ID= ? and pl.PRE_LOC_ID=?";            
            conexion = JdbcDAOFactory.getConexion();
            stm = conexion.prepareStatement(sql  + " WITH UR");          
            stm.setLong(1,Integer.parseInt(idProd));
            stm.setLong(2, idLocal);
            logger.debug("SQL productosSustitutosByCliente: " + sql);
            rs = stm.executeQuery();
            while ( rs.next() ) {
                
                producto.setPrecio(rs.getDouble("pre_valor"));
                
             
            }
            
        } catch (SQLException ex) {
            logger.error("getProductoPrecioFO - Problema SQL", ex);
            throw new ProductosDAOException(ex);
        } catch (Exception ex) {
            logger.error("getProductoPrecioFO - Problema General", ex);
            throw new ProductosDAOException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close(); 
                if (stm != null)
                    stm.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getProductoPrecioFO - Problema SQL (close)", e);
            }
        }
        return producto;
	}


}
