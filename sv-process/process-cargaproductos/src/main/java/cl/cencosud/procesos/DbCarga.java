package cl.cencosud.procesos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Costo;
import cl.cencosud.beans.Precio;
import cl.cencosud.beans.Producto;
import cl.cencosud.util.Db;
import cl.cencosud.util.LogUtil;

public class DbCarga extends Db {

	static {        
		LogUtil.initLog4J();        
    }	
	static Logger logger = Logger.getLogger(DbCarga.class);
	
	public DbCarga() {
	}

   /**
    * Carga en memoria la lista de productos existentes en la base de datos, para una busqueda más rápida Formato
    * [codigo, [unidad, Producto]]
    * 
    * @param con
    * @return
    * @throws SQLException
    */
    public static Hashtable productos( Connection con ) throws SQLException {
       long inix = System.currentTimeMillis();
      String sql = "select id_producto, ID_CATPROD, COD_PROD1, COD_PROD2, "
            + "DES_CORTA, DES_LARGA, ESTADO, MARCA, COD_PROPPAL, ORIGEN, UN_BASE, EAN13, UN_EMPAQUE, "
            + "ATRIB9, ATRIB10, FCARGA, UNI_MED from bodba.bo_productos";
        PreparedStatement ps = con.prepareStatement(sql + " with ur");
      ResultSet rs = ps.executeQuery();

        Hashtable productos = new Hashtable();

      while (rs.next()) {
         //un producto es único con su codigo y unidad de medida, por lo
         //tanto el par codigo, unidad produce un nuevo id
         String codigo = rs.getString("COD_PROD1");
         Hashtable unidades = (Hashtable) productos.get(codigo);
            if ( unidades == null ) {
                unidades = new Hashtable();
            }

            Producto producto = new Producto();
            producto.setId(rs.getInt("id_producto"));
            producto.setCodigoCategoria(rs.getString("ID_CATPROD"));
            producto.setCodigo(codigo);
            producto.setCodigo2(rs.getString("COD_PROD2"));
            producto.setNombre(rs.getString("DES_CORTA"));
            producto.setDescripcion(rs.getString("DES_LARGA"));
            producto.setEstado(rs.getInt("ESTADO"));
            producto.setMarca(rs.getString("MARCA"));
            producto.setCodigoProPpal(rs.getString("COD_PROPPAL"));
            producto.setOrigen(rs.getString("ORIGEN"));
            producto.setUnidadBase(rs.getString("UN_BASE"));
            producto.setEan13(rs.getString("EAN13"));
            producto.setUnidadEmpaque(rs.getString("UN_EMPAQUE"));
            producto.setAtributo9(rs.getString("ATRIB9"));
            producto.setAtributo10(rs.getString("ATRIB10"));
            producto.setUnidadMedida(rs.getString("UNI_MED"));
            unidades.put(producto.getUnidadMedida(), producto);
            productos.put(codigo, unidades);
        }
        if ( rs != null ) {
            rs.close();
        }
        if ( ps != null ) {
            ps.close();
        }
        logger.info("Productos cargados desde BD: " + productos.size() + ", tiempo:" + Cargar.calculaTiempo(inix));
      return productos;
   }

   /**
    * Carga codigos de barra en memoria para que su búsqueda sea más rápida. clave hash
    * codigo_producto+unidad_medida+codigo_barra, dato objeto barra
    * 
    * @param con
    * @return
    * @throws SQLException
    */
    public static Hashtable barras( Connection con ) throws SQLException {
       long inix = System.currentTimeMillis();
      String sql = "select cod_prod1, cod_barra, tip_codbar, cod_ppal, unid_med from bodba.bo_codbarra";
      PreparedStatement ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

        Hashtable barras = new Hashtable();

      while (rs.next()) {
         Barra barra = new Barra();
         barra.setCodigo(rs.getString("cod_barra"));
         barra.setTipo(rs.getString("tip_codbar"));
         barra.setPpal(rs.getString("cod_ppal"));
         barra.setUnidad(rs.getString("unid_med"));
         barras.put(rs.getString("cod_prod1") + rs.getString("unid_med") + rs.getString("cod_barra"), barra);
      }
      
        if ( rs != null ) {
            rs.close();
        }
        if ( ps != null ) {
            ps.close();
        }

        logger.info("Codigos de barra cargados desde BD: " + barras.size() + ", tiempo:" + Cargar.calculaTiempo(inix));
      return barras;
   }

   /**
    * Clave de la hash (codigoProducto + unidadMedida)
    * 
    * @param con
    * @param localId
    * @return
    * @throws SQLException
    */
    public static Hashtable precios( Connection con, int localId ) throws SQLException {
       long inix = System.currentTimeMillis();
        Hashtable precios = new Hashtable();
      String sql = "select ID_PRODUCTO, COD_PROD1, COD_LOCAL, PREC_VALOR, UMEDIDA, COD_BARRA, "
            + "PRECIO_ANTIGUO, FECHA_PRECIO_ANTIGUO, PRECIO_NUEVO, FECHA_PRECIO_NUEVO, BLOQ_COMPRA, COSTO_PROMEDIO, "
            + "pro.pro_estado                                                                 "
            + "from bodba.bo_precios pre left outer join fodba.fo_productos pro on pre.id_producto = pro.pro_id_bo "
            + "where id_local = ? ";

      PreparedStatement ps = con.prepareStatement(sql);
      ps.setInt(1, localId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
         Precio precio = new Precio();
         precio.setCodigoProducto(rs.getString("COD_PROD1").trim());
         precio.setPrecio(rs.getInt("PREC_VALOR"));
         precio.setUnidadMedida(rs.getString("UMEDIDA").trim());
         precio.setCodigoBarra(rs.getString("COD_BARRA"));
         precio.setIdLocal(localId);
         precio.setIdProducto(rs.getInt("ID_PRODUCTO"));
         precio.setBloqueoCompra(rs.getString("BLOQ_COMPRA"));
         precio.setFechaPrecioNuevo(rs.getDate("FECHA_PRECIO_NUEVO"));
         precio.setCostoPromedio(rs.getInt("COSTO_PROMEDIO"));
         precio.setEstado(rs.getString("pro_estado"));

         /*
          * El precio es unico para cada producto (codigoProducto, unidadMedida) => producto_id, no importa el codigo de
          * barra
          */
         precios.put(precio.getCodigoProducto() + precio.getUnidadMedida(), precio);
      }
        if ( rs != null ) {
            rs.close();
        }
        if ( ps != null ) {
            ps.close();
        }

        logger.info("Precios cargados desde BD: " + precios.size() + ", local:" + localId + " tiempo:" + Cargar.calculaTiempo(inix));
      return precios;
   }

   /**
    * Los costos estan en la tabla bo_precios
    * 
    * @param con
    * @param localId
    * @return
    * @throws SQLException
    */
    public static List costos( Connection con, int localId ) throws SQLException {
        List costos = new ArrayList();
      String sql = "select pre_pro_id, pro_cod_sap, pre_costo "
            + "from fodba.fo_precios_locales  inner join fodba.fo_productos on pre_pro_id = pro_id "
            + "where pre_loc_id = ? ";
        PreparedStatement ps = con.prepareStatement(sql + " with ur");
      ps.setInt(1, localId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
         Costo costo = new Costo();
         costo.setCodigoProd(rs.getString("pro_cod_sap"));
         costo.setId(rs.getInt("pre_pro_id"));
         costo.setCosto(rs.getBigDecimal("pre_costo"));
         costos.add(costo);
      }
      return costos;
   }

   public static Precio getPrecio(PreparedStatement ps, int localId, String codigoProducto, String unidadMedida, String archivo)
         throws SQLException {
	   ResultSet rs = null;
	   try {    	    			   
		   //logger.debug("Obtener Precio DB, Entrada [localId="+localId+", codigoProducto="+codigoProducto+", unidadMedida="+unidadMedida+"]");
		   ps.setInt(1, localId);
		   ps.setString(2, codigoProducto);
		   ps.setString(3, unidadMedida);
		   rs = ps.executeQuery();
         
		   if (rs.next()) {
			   Precio precio = new Precio();
			   precio.setIdProducto(rs.getInt("ID_PRODUCTO"));
			   precio.setPrecio(rs.getInt("PREC_VALOR"));
			   precio.setIdFoProducto(rs.getInt("PRO_ID"));
			   precio.setFechaPrecioNuevo(rs.getDate("FECHA_PRECIO_NUEVO"));
			   precio.setNombreArchivo(archivo);
			   precio.setIdLocal(localId);
			   return precio;
		   }
	   } finally {
		   close(rs);
	   }
	   return null;
   }
}