/*
 * Created on 22-abr-2009
 *
 */
package cl.bbr.boc.dao.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import cl.bbr.boc.dao.ProductosDAO;
import cl.bbr.boc.dto.BOPrecioDTO;
import cl.bbr.boc.dto.BOProductoDTO;
import cl.bbr.boc.dto.FOProductoBannerDTO;
import cl.bbr.boc.dto.FOProductoDTO;
import cl.bbr.boc.dto.LocalDTO;
import cl.bbr.boc.dto.MarcaDTO;
import cl.bbr.boc.dto.MotivoDespDTO;
import cl.bbr.boc.dto.SubrubroDTO;
import cl.bbr.jumbocl.common.utils.DString;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.common.dao.jdbc.JdbcDAO;
import cl.jumbo.common.dto.CategoriaMasvDTO;

/**
 * @author jdroguett
 *  
 */
public class JdbcProductos extends JdbcDAO implements ProductosDAO {
   private static int RANGO = 1000;
   private static ConexionUtil conexionUtil = new ConexionUtil();

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#publicar(java.lang.String)
    */
   public int publicar(String locales, List productos, String usuario) throws DAOException {
      return cambiaEstado("A", "El producto ha sido publicado (método masivo)", locales, productos, usuario, -1, "");
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#despublicar(java.lang.String, java.lang.String)
    */
   public int despublicar(String locales, List productos, String usuario, int motivoId, String obs) throws DAOException {
      return cambiaEstado("D", "El producto ha sido despublicado (método masivo)", locales, productos, usuario,
            motivoId, obs);
   }
   
   public int conStock(String locales, List productos, String usuario) throws DAOException {
      return cambiaStock(1, "El producto ha sido seteado con stock (método masivo)", locales, productos, usuario);
   }
   
   public int sinStock(String locales, List productos, String usuario) throws DAOException {
       return cambiaStock(0, "El producto ha sido seteado sin stock (método masivo)", locales, productos, usuario);
    }
   

   private int cambiaEstado(String estado, String msg, String locales, List productos, String usuario, int motivoId,
         String obs) throws DAOException {
      int fromIndex = 0;
      int toIndex = (productos.size() < RANGO ? productos.size() : RANGO);
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      int cant = 0;
      try {
         con = conexionUtil.getConexion();
         con.setAutoCommit(false);
        
         while (fromIndex < productos.size()) {
            List lista = productos.subList(fromIndex, toIndex);
            String prods = DString.join(lista);
            String sql = "";
            if ("A".equals(estado))
                sql = "update fodba.fo_precios_locales set pre_estado = '" + estado + "' , pre_tienestock = 1 where pre_loc_id in "
                  + locales + " and pre_pro_id in " + prods;
            else
                sql = "update fodba.fo_precios_locales set pre_estado = '" + estado + "' where pre_loc_id in "
                + locales + " and pre_pro_id in " + prods;
            logger.debug(sql);
            ps = con.prepareStatement(sql);
            cant += ps.executeUpdate();

            if ("A".equals(estado)) { //ya que por lo menos en un local está publicado
               sql = "update fodba.fo_productos set pro_estado = 'A' where pro_id in " + prods;
            } else {
               //los que estan despublicados en todos los locales
               String motivo = (motivoId >= 0 ? motivoId + "" : null);
               sql = "update fodba.fo_productos set pro_estado = 'D', PRO_ID_DESP = " + motivo
                     + " where pro_id in ( select L from (values " + DString.joinJoin(lista) + ") as X(L) "
                     + " except (" + " select pre_pro_id from fodba.fo_precios_locales "
                     + " where pre_estado = 'A' and pre_pro_id in " + prods + "))";
            }
            ps = con.prepareStatement(sql);
            ps.executeUpdate();

            String sqlLoc = "select cod_local from bodba.bo_locales where id_local in " + locales;
            ps = con.prepareStatement(sqlLoc + " WITH UR");
            rs = ps.executeQuery();
            
            StringBuffer sbLoc = new StringBuffer();
            while (rs.next()) {
               sbLoc.append(rs.getString("cod_local") + " ");
            }
            close(rs);
            msg = "".equals(obs) ? msg + " en " + sbLoc : msg + " en " + sbLoc + "(" + obs + ")";
            String sqlIns = "insert into fodba.fo_pro_tracking "
                  + "(tra_pro_id, tra_bo_pro_id, tra_fec_crea, tra_usuario, tra_texto) "
                  + "select pro_id, pro_id_bo, current_timestamp, '" + usuario + "','" + msg
                  + "' from fodba.fo_productos " + "where pro_id in " + prods;
            
            ps = con.prepareStatement(sqlIns);
            ps.executeUpdate();

            fromIndex = toIndex;
            toIndex = (productos.size() < toIndex + RANGO ? productos.size() : toIndex + RANGO);
         }
        
      } catch (Exception e) {
         rollback(con);
         throw new DAOException(e);
      } finally {
      	 try {
      	 	con.commit();
	         autoCommitTrue(con);
	         close(rs, ps, con);
		} catch (SQLException e1) {
			logger.error("[Metodo] : cambiaEstado - Problema SQL (close)", e1);
		}

      }
      return cant;
   }

   private int cambiaStock(int estado, String msg, String locales, List productos, String usuario) throws DAOException {
        int fromIndex = 0;
        int toIndex = (productos.size() < RANGO ? productos.size() : RANGO);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cant = 0;
        try {
           con = conexionUtil.getConexion();
           con.setAutoCommit(false);
          
           
           while (fromIndex < productos.size()) {
              List lista = productos.subList(fromIndex, toIndex);
              String prods = DString.join(lista);
              String sql = "update fodba.fo_precios_locales set pre_tienestock = " + estado + " where pre_loc_id in "
                    + locales + " and pre_pro_id in " + prods;

              logger.debug(sql);
              ps = con.prepareStatement(sql);
              cant += ps.executeUpdate();

              String sqlLoc = "select cod_local from bodba.bo_locales where id_local in " + locales;
              ps = con.prepareStatement(sqlLoc + " WITH UR");
              rs = ps.executeQuery();
              
              
              StringBuffer sbLoc = new StringBuffer();
              while (rs.next()) {
                 sbLoc.append(rs.getString("cod_local") + " ");
              }
              close(rs);
              msg = msg + " en " + sbLoc;
              String sqlIns = "insert into fodba.fo_pro_tracking "
                    + "(tra_pro_id, tra_bo_pro_id, tra_fec_crea, tra_usuario, tra_texto) "
                    + "select pro_id, pro_id_bo, current_timestamp, '" + usuario + "','" + msg
                    + "' from fodba.fo_productos " + "where pro_id in " + prods;
              ps = con.prepareStatement(sqlIns);
              ps.executeUpdate();

              fromIndex = toIndex;
              toIndex = (productos.size() < toIndex + RANGO ? productos.size() : toIndex + RANGO);
           }
          
        } catch (Exception e) {
           rollback(con);
           throw new DAOException(e);
        } finally {
        	try {
				con.commit();
	        	autoCommitTrue(con);
	        	close(rs, ps, con);
			} catch (SQLException e1) {
				logger.error("[Metodo] : cambiaStock - Problema SQL (close)", e1);
			}
        }
        return cant;
     }
   
   
   public List conSectorPicking(List productos) throws DAOException {
      List prodsConSector = new ArrayList();
      int fromIndex = 0;
      int toIndex = (productos.size() < RANGO ? productos.size() : RANGO);
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         
         while (fromIndex < productos.size()) {
            List lista = productos.subList(fromIndex, toIndex);
            String prods = DString.join(lista);
            String sql = "select pro_id "
                  + " from fodba.fo_productos pro inner join bodba.bo_prod_sector sec on pro.pro_id_bo = sec.id_producto"
                  + " where pro_id in " + prods;
            logger.debug(sql);
            ps = con.prepareStatement(sql + " WITH UR");
            rs = ps.executeQuery();
            
            while (rs.next()) {
               prodsConSector.add(rs.getInt(1) + "");
            }
            fromIndex = toIndex;
            toIndex = (productos.size() < toIndex + RANGO ? productos.size() : toIndex + RANGO);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return prodsConSector;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#idsQueNoExisten(java.lang.String, java.util.List)
    */
   public List idsQueNoExisten(String localId, List productos) throws DAOException {
      List noExisten = new ArrayList();
      int rango = 1000;
      int fromIndex = 0;
      int toIndex = (productos.size() < rango ? productos.size() : rango);
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
        
         while (fromIndex < productos.size()) {
            List lista = productos.subList(fromIndex, toIndex);
            String prods = DString.joinJoin(lista);
            String sql = "select L from (values " + prods + ") as X(L) "
                  + "except (select pre_pro_id from fodba.fo_precios_locales where pre_loc_id = " + localId + ")";
            logger.debug(sql);
            ps = con.prepareStatement(sql + " WITH UR");
            rs = ps.executeQuery();
            fromIndex = toIndex;
            toIndex = (productos.size() < toIndex + rango ? productos.size() : toIndex + rango);

            while (rs.next()) {
               noExisten.add(rs.getInt(1) + "");
            }
         }
      } catch (Exception e) {
         throw new DAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				//cierra coneccion
				if (con != null && !con.isClosed())
					con.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : idsQueNoExisten - Problema SQL (close)", e);
			}
		}
      return noExisten;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#getPublicacion(int)
    */
   public List getPublicacion(int productoId) throws DAOException {
      List lista = new ArrayList();
      String sql = "select id_local, nom_local, pre_estado, pre_tienestock from fodba.fo_productos "
            + "inner join fodba.fo_precios_locales on pro_id = pre_pro_id "
            + "inner join bodba.bo_locales on id_local = pre_loc_id where pro_id = " + productoId;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         rs = ps.executeQuery();
         while (rs.next()) {
            LocalDTO localDTO = new LocalDTO();
            localDTO.setId(rs.getInt("id_local"));
            localDTO.setNombre(rs.getString("nom_local"));
            localDTO.setPublicado("A".equals(rs.getString("pre_estado")));
            localDTO.setTieneStock(rs.getBoolean("pre_tienestock"));
            lista.add(localDTO);
         }
      } catch (Exception e) {
         throw new DAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				//cierra coneccion
				if (con != null && !con.isClosed())
					con.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPublicacion - Problema SQL (close)", e);
			}
		}
      return lista;
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#publicar(long, java.lang.String[])
    */
   public void publicar(long productoId, String[] locales, String motivoDes, String obs, String usuario)
         throws DAOException {
      //supone despublicación de todos los locales
      String sql = "update fodba.fo_productos set pro_estado = 'D', pro_id_desp = " + motivoDes + " where pro_id = "
            + productoId;
      //despublica todos los locales
      String sqlDes = "update fodba.fo_precios_locales set pre_estado = 'D' where pre_pro_id = " + productoId;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         con.setAutoCommit(false);

         ps = con.prepareStatement(sqlDes);
         ps.executeUpdate();

         if (locales.length > 0) {
            //publica algunos locales
            String sqlPub = "update fodba.fo_precios_locales set pre_estado = 'A' where pre_pro_id = " + productoId
                  + " and pre_loc_id in " + DString.join(locales);
            ps = con.prepareStatement(sqlPub);
            ps.executeUpdate();
            //hay por lo menos un local publicado
            sql = "update fodba.fo_productos set pro_estado = 'A', pro_id_desp = null where pro_id = " + productoId;
         }
         ps = con.prepareStatement(sql);
         ps.executeUpdate();

         String sqlLoc = "select id_local, cod_local from bodba.bo_locales";
         ps = con.prepareStatement(sqlLoc + " WITH UR");
         rs = ps.executeQuery();
         StringBuffer locPub = new StringBuffer();
         StringBuffer locDes = new StringBuffer();

         Arrays.sort(locales); //para buscar debe estar ordenado
         while (rs.next()) {
            int localId = rs.getInt("id_local");
            if (Arrays.binarySearch(locales, localId + "") >= 0)
               locPub.append(rs.getString("cod_local") + " ");
            else
               locDes.append(rs.getString("cod_local") + " ");
         }

         if (locPub.length() > 0) {
            String msg = "El producto ha sido publicado en " + locPub;
            String sqlIns = "insert into fodba.fo_pro_tracking "
                  + "(tra_pro_id, tra_bo_pro_id, tra_fec_crea, tra_usuario, tra_texto) "
                  + "select pro_id, pro_id_bo, current_timestamp, '" + usuario + "','" + msg
                  + "' from fodba.fo_productos where pro_id = " + productoId;
            logger.debug(sqlIns);
            ps = con.prepareStatement(sqlIns);
            ps.executeUpdate();
         }

         if (locDes.length() > 0) {
            String msg = "El producto ha sido despublicado en " + locDes + "(" + obs + ")";
            String sqlIns = "insert into fodba.fo_pro_tracking "
                  + "(tra_pro_id, tra_bo_pro_id, tra_fec_crea, tra_usuario, tra_texto) "
                  + "select pro_id, pro_id_bo, current_timestamp, '" + usuario + "','" + msg
                  + "' from fodba.fo_productos where pro_id = " + productoId;
            logger.debug(sqlIns);
            ps = con.prepareStatement(sqlIns);
            ps.executeUpdate();
         }

        
      } catch (Exception e) {
         rollback(con);
         throw new DAOException(e);
      } finally {
      	 try {
			con.commit();
	         autoCommitTrue(con);
	         close(rs, ps, con);
		} catch (SQLException e1) {
			logger.error("[Metodo] : publicar - Problema SQL (close)", e1);
		}

      }
   }

   
   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#publicar(long, java.lang.String[])
    */
   public void setTieneStock(long productoId, String[] localestienestock, String usuario)
         throws DAOException {
      //cambia estado tiene stock para todos los locales
      String sqlDes = "update fodba.fo_precios_locales set pre_tienestock = 0 where pre_pro_id = " + productoId;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         con.setAutoCommit(false);

         ps = con.prepareStatement(sqlDes);
         ps.executeUpdate();

         if (localestienestock.length > 0) {
            //publica algunos locales
            String sqlPub = "update fodba.fo_precios_locales set pre_tienestock = 1 where pre_pro_id = " + productoId
                  + " and pre_loc_id in " + DString.join(localestienestock);
            ps = con.prepareStatement(sqlPub);
            ps.executeUpdate();
            
         }
         String sqlLoc = "select id_local, cod_local from bodba.bo_locales";
         ps = con.prepareStatement(sqlLoc + " WITH UR");
         rs = ps.executeQuery();
         StringBuffer locPub = new StringBuffer();
         StringBuffer locDes = new StringBuffer();

         Arrays.sort(localestienestock); //para buscar debe estar ordenado
         while (rs.next()) {
            int localId = rs.getInt("id_local");
            if (Arrays.binarySearch(localestienestock, localId + "") >= 0)
               locPub.append(rs.getString("cod_local") + " ");
            else
               locDes.append(rs.getString("cod_local") + " ");
         }
          if (locPub.length() > 0) {
            String msg = "El producto ha sido marcado con stock en " + locPub;
            String sqlIns = "insert into fodba.fo_pro_tracking "
                  + "(tra_pro_id, tra_bo_pro_id, tra_fec_crea, tra_usuario, tra_texto) "
                  + "select pro_id, pro_id_bo, current_timestamp, '" + usuario + "','" + msg
                  + "' from fodba.fo_productos where pro_id = " + productoId;
            logger.debug(sqlIns);
            ps = con.prepareStatement(sqlIns);
            ps.executeUpdate();
         }

         if (locDes.length() > 0) {
            String msg = "El producto ha sido marcado sin stock en " + locDes ;
            String sqlIns = "insert into fodba.fo_pro_tracking "
                  + "(tra_pro_id, tra_bo_pro_id, tra_fec_crea, tra_usuario, tra_texto) "
                  + "select pro_id, pro_id_bo, current_timestamp, '" + usuario + "','" + msg
                  + "' from fodba.fo_productos where pro_id = " + productoId;
            logger.debug(sqlIns);
            ps = con.prepareStatement(sqlIns);
            ps.executeUpdate();
         }

         
      } catch (Exception e) {
         rollback(con);
         throw new DAOException(e);
      } finally {
      	 try {
      	 	con.commit();
	         autoCommitTrue(con);
	         close(rs, ps, con);
		} catch (SQLException e1) {
			logger.error("[Metodo] : setTieneStock - Problema SQL (close)", e1);
		}

      }
   }

   
   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#getMotivosDespublicacion()
    */
   public List getMotivosDespublicacion() throws DAOException {
      List lista = new ArrayList();
      String sql = "SELECT ID_DESP, MOTIVO FROM FODBA.FO_PROD_DESP where estado = 'A'";
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         rs = ps.executeQuery();
         
         while (rs.next()) {
            MotivoDespDTO dto = new MotivoDespDTO();
            dto.setId(rs.getInt("id_desp"));
            dto.setMotivo(rs.getString("motivo"));
            lista.add(dto);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#evitarPubDes(java.util.List, boolean)
    */
   public void evitarPubDes(List productos, boolean evitarPubDes) throws DAOException {
      int fromIndex = 0;
      int toIndex = (productos.size() < RANGO ? productos.size() : RANGO);
      Connection con = null;
      PreparedStatement ps = null;
      int cant = 0;
      try {
         con = conexionUtil.getConexion();
         con.setAutoCommit(false);
         
         while (fromIndex < productos.size()) {
            List lista = productos.subList(fromIndex, toIndex);
            String prods = DString.join(lista);
            String sql = "update fodba.fo_productos set evitar_pub_des = '" + (evitarPubDes ? "S" : "N")
                  + "' where pro_id in " + prods;

            logger.debug(sql);
            ps = con.prepareStatement(sql);
            cant += ps.executeUpdate();
            fromIndex = toIndex;
            toIndex = (productos.size() < toIndex + RANGO ? productos.size() : toIndex + RANGO);
         }
        
      } catch (Exception e) {
         rollback(con);
         throw new DAOException(e);
      } finally {
      	 try {
			con.commit();
	         autoCommitTrue(con);
	         close(ps, con);
		} catch (SQLException e1) {
			logger.error("[Metodo] : evitarPubDes - Problema SQL (close)", e1);
		}

      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#tieneSectorPicking(int)
    */
   public boolean tieneSectorPicking(int productoId) throws DAOException {
      String sql = "select count(*) from bodba.bo_prod_sector "
            + "where id_producto = (select pro_id_bo from fo_productos where pro_id = " + productoId + ")";
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         rs = ps.executeQuery();
         if (rs.next()) {
            int n = rs.getInt(1);
            if (n > 0)
               return true;
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#listaNegraProductos()
    */
   public List listaNegraProductos() throws DAOException {
      List lista = new ArrayList();
      String sql = "select subrubro, precio_total from bodba.lista_negra_subrubro";
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         rs = ps.executeQuery();
         while (rs.next()) {
            SubrubroDTO subrubro = new SubrubroDTO();
            subrubro.setSubrubro(rs.getString("subrubro"));
            subrubro.setPrecioTotal(rs.getBigDecimal("precio_total"));
            lista.add(subrubro);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#updRubrubro(java.util.List)
    */
   public void updSubrubro(List lista) throws DAOException {
      String sqlDel = "delete from bodba.lista_negra_subrubro";
      String sqlIns = "insert into bodba.lista_negra_subrubro(subrubro, precio_total) values(?, ?)";
      String sqlInsP = "insert into bodba.lista_negra_productos(subrubro_id, producto_id) "
            + "(select rub.id, bpro.id_producto "
            + "from bodba.lista_negra_subrubro rub inner join bodba.bo_catprod cat on rub.subrubro = SUBSTR(cat.id_catprod,1,6) "
            + "inner join bodba.bo_productos bpro on bpro.id_catprod = cat.id_catprod "
            + "inner join fodba.fo_productos fpro on fpro.pro_id_bo = bpro.id_producto)";
      Connection con = null;
      PreparedStatement ps = null;
      try {
         con = conexionUtil.getConexion();
         con.setAutoCommit(false);
         //borra lista_negra_subrubro y lista_negra_productos
         ps = con.prepareStatement(sqlDel);
         ps.executeUpdate();
         //inserta nuevos subrubros
         ps = con.prepareStatement(sqlIns);
         for (int i = 0; i < lista.size(); i++) {
            SubrubroDTO subrubro = (SubrubroDTO) lista.get(i);
            ps.setString(1, subrubro.getSubrubro());
            ps.setBigDecimal(2, subrubro.getPrecioTotal());
            ps.executeUpdate();
         }
         //insert nuevos productos
         ps = con.prepareStatement(sqlInsP);
         ps.executeUpdate();
        
      } catch (Exception e) {
         rollback(con);
         throw new DAOException(e);
      } finally {
      	 try {
			con.commit();
	         autoCommitTrue(con);
	         close(ps, con);
		} catch (SQLException e1) {
			logger.error("[Metodo] : updSubrubro - Problema SQL (close)", e1);
		}

      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#getMasvProductos(int)
    */
   public List getMasvProductos(int categoriaId) throws DAOException {
      List lista = new ArrayList();
      String sql = "select mp.local_id, l.nom_local, cat_id, cat_nombre, m.mar_id, m.mar_nombre, "
            + " pro_tipo_producto, pro_des_corta, pro_cod_sap, mp.cantidad "
            + " from  fodba.masv_productos mp inner join bodba.bo_locales l on l.id_local = mp.local_id "
            + " inner join fodba.fo_productos fp on fp.pro_id = mp.producto_id "
            + " inner join fodba.fo_marcas m on m.mar_id = fp.pro_mar_id "
            + " inner join fodba.fo_productos_categorias on prca_pro_id = fp.pro_id "
            + " inner join fodba.fo_categorias cat  on cat.cat_id = prca_cat_id         "
            + "where mp.masv_categoria_id = " + categoriaId
            + " and cat_estado = 'A' order by mp.local_id, mp.cantidad desc ";
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         rs = ps.executeQuery();
         
         while (rs.next()) {
            FOProductoDTO pro = new FOProductoDTO();
            LocalDTO local = new LocalDTO(rs.getInt("local_id"), rs.getString("nom_local"));
            pro.setLocal(local);
            CategoriaMasvDTO categoria = new CategoriaMasvDTO(rs.getInt("cat_id"), rs.getString("cat_nombre"));
            pro.setCategoria(categoria);
            MarcaDTO marca = new MarcaDTO(rs.getInt("mar_id"), rs.getString("mar_nombre"));
            pro.setMarca(marca);
            pro.setDescripcion(rs.getString("pro_tipo_producto") + rs.getString("pro_des_corta"));
            pro.setCodSap(rs.getString("pro_cod_sap"));
            pro.setCantidad(rs.getInt("cantidad"));
            lista.add(pro);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#getProductosBO(java.lang.String)
    */
   public List getProductosBO(String codsap) throws DAOException {
      List lista = new ArrayList();
      String sql = "select id_producto, des_corta, des_larga, uni_med from bodba.bo_productos where cod_prod1 = ?";
      String sqlPrecios = "select l.id_local, l.nom_local, p.prec_valor, p.bloq_compra "
            + " from bodba.bo_precios p inner join bodba.bo_locales l on p.id_local = l.id_local "
            + " where id_producto = ? order by id_local";
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      PreparedStatement pspre = null;
      ResultSet rspre = null;
      try {
         con = conexionUtil.getConexion();
         con.setAutoCommit(false);
         ps = con.prepareStatement(sql + " WITH UR");
         ps.setString(1, codsap);
         rs = ps.executeQuery();
         pspre = con.prepareStatement(sqlPrecios + " WITH UR");
         while (rs.next()) {
            BOProductoDTO pro = new BOProductoDTO();
            pro.setId(rs.getInt("id_producto"));
            pro.setCodSap(codsap);
            pro.setDescripcion(rs.getString("des_corta") + " " + rs.getString("des_larga"));
            pro.setUnidad(rs.getString("uni_med"));
            pspre.setInt(1, pro.getId());
            rspre = pspre.executeQuery();
            while (rspre.next()) {
               BOPrecioDTO pre = new BOPrecioDTO();
               LocalDTO loc = new LocalDTO();
               loc.setId(rspre.getInt("id_local"));
               loc.setNombre(rspre.getString("nom_local"));
               pre.setLocal(loc);
               pre.setPrecio(rspre.getBigDecimal("prec_valor"));
               pre.setBloqueado("SI".equals(rspre.getString("bloq_compra")));
               pro.addPrecio(pre);
            }
            lista.add(pro);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.ProductosDAO#updatePrecio(int, int, boolean)
    */
   public void updatePrecio(UserDTO usr, int id, int localId, BigDecimal precio, boolean b) throws DAOException {
      //modifica precio en bo_precios
      String sqlBO = "update bodba.bo_precios set prec_valor = ?, bloq_compra = ? where id_producto = ? and id_local = ?";
      //actualiza cantidades en bo_productos
      String sqlBOpro = "update bodba.bo_productos p set "
            + " tot_precios = (select count(*) from bodba.bo_precios pr where pr.id_producto = p.id_producto), "
            + " tot_precios_bloq = (select count(*) from bodba.bo_precios pr where pr.id_producto = p.id_producto and bloq_compra ='SI') "
            + " where id_producto = ?";
      //actualiza estado en bo_productos
      String sqlBOproEst = "update bodba.bo_productos "
            + " set con_precio = (case when tot_precios_bloq < tot_precios then 'S' else 'N' end) "
            + " where id_producto = ?";
      //actualiza/inserta precios en fo_precios_locales
      String sqlFOpre = "merge into fodba.fo_precios_locales fprecios "
            + " using (select fproductos.pro_id as pro_id, bprecios.id_local as id_local, bprecios.prec_valor as valor, bprecios.bloq_compra "
            + "       from  fodba.fo_productos fproductos "
            + "       inner join bodba.bo_precios bprecios on bprecios.id_producto = ? and bprecios.id_producto  = fproductos.pro_id_bo "
            + "      ) precios "
            + " on (fprecios.pre_pro_id = precios.pro_id and fprecios.pre_loc_id = precios.id_local) "
            + " WHEN MATCHED THEN                                "
            + "   UPDATE SET fprecios.pre_valor = precios.valor, fprecios.pre_estado = (case when precios.bloq_compra = 'SI' then 'D' else 'A' end)  "
            + "WHEN NOT MATCHED THEN                             "
            + "   INSERT (PRE_PRO_ID, PRE_LOC_ID, PRE_VALOR, PRE_COSTO, PRE_STOCK, PRE_ESTADO) "
            + "   VALUES  (precios.pro_id, precios.id_local, precios.valor, 0, 0, (case when precios.bloq_compra = 'SI' then 'D' else 'A' end)) ";

      String sqlLog = "insert into fodba.fo_pro_tracking "
            + "(TRA_BO_PRO_ID, TRA_PRO_ID, TRA_FEC_CREA, TRA_USUARIO, TRA_TEXTO) "
            + " select pro_id_bo, pro_id, CURRENT TIMESTAMP, '" + usr.getLogin()
            + "', 'Cambio de precio y/o desbloqueo: precio(" + precio.toString() + ")-bloqueado(" + b
            + ")' from fodba.fo_productos where pro_id_bo = ? ";

      Connection con = null;
      PreparedStatement ps = null;
      try {
         con = conexionUtil.getConexion();
         con.setAutoCommit(false);
         //
         ps = con.prepareStatement(sqlBO);
         ps.setBigDecimal(1, precio);
         ps.setString(2, b ? "SI" : "NO");
         ps.setInt(3, id);
         ps.setInt(4, localId);
         ps.executeUpdate();
         //
         ps = con.prepareStatement(sqlBOpro);
         ps.setInt(1, id);
         ps.executeUpdate();
         //
         ps = con.prepareStatement(sqlBOpro);
         ps.setInt(1, id);
         ps.executeUpdate();
         //
         ps = con.prepareStatement(sqlBOproEst);
         ps.setInt(1, id);
         ps.executeUpdate();
         //
         ps = con.prepareStatement(sqlFOpre);
         ps.setInt(1, id);
         ps.executeUpdate();
         //
         ps = con.prepareStatement(sqlLog);
         ps.setInt(1, id);
         ps.executeUpdate();
         
        
      } catch (Exception e) {
         rollback(con);
         throw new DAOException(e);
      } finally {
      	 try {
			con.commit();
	         autoCommitTrue(con);
	         close(ps, con);
		} catch (SQLException e1) {
			logger.error("[Metodo] : updatePrecio - Problema SQL (close)", e1);
		}

      }
   }

public String getCategCompletaProducto(int codprod) throws DAOException {
    String catCompletaProd = "";
    String catproducto = "";
    String sql = "";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    String sql2 = "select id_catprod from BO_PRODUCTOS where cod_prod1 = '"+codprod+"'";

    try {
       con = conexionUtil.getConexion();
       ps = con.prepareStatement(sql2 + " WITH UR");
       rs = ps.executeQuery();
       while (rs.next()) {
    	   catproducto = rs.getString("id_catprod");  	   
       }
    } catch (Exception e) {
       throw new DAOException(e);
    } finally {
       close(rs, ps, con);
    }
    
    
    for(int i=0; i<4;i++){
    	if(i==1){
    		catproducto = catproducto.substring(0,catproducto.length()-3);
    	}
    	else{
    		if(i>1){
    			catproducto = catproducto.substring(0,catproducto.length()-2);
    		}
    	}
    	sql = "select descat from bo_catprod where id_catprod ='"+catproducto+"'";
    	try {
    	       con = conexionUtil.getConexion();
    	       ps = con.prepareStatement(sql + " WITH UR");
    	       rs = ps.executeQuery();
    	       while (rs.next()) {
    	          catCompletaProd = "/"+rs.getString("descat")+catCompletaProd;
    	       }
    	    } catch (Exception e) {
    	       throw new DAOException(e);
    	    } finally {
    	       close(rs, ps, con);
    	    }
	    
    }
    
    return catCompletaProd;
 }

/*
 * (updateBannerProductoOnLy)
 * pkg @cl.bbr.boc.dao.ProductosDAO#updateBannerProductoOnLy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, cl.bbr.jumbocl.usuarios.dto.UserDTO)
 * metodo que realiza la publicacion y despublicacion de los banner de productos
 * este metodo es para cargar y eliminar banner de productos.
 * la tabla es FODBA.FO_PRODUCTOS_PROMO
 * Param int
 */
  public int updateBannerProductoOnLy(FOProductoBannerDTO pro, String obs, UserDTO usr) throws DAOException { 
	    int respuestaSQL =0;

	    //actualiza banner en fo_productos
	     String sqlSelectPro = " SELECT PRO_ID FROM FODBA.FO_PRODUCTOS WHERE PRO_ID = ? ";
	     String sqlSelect= " SELECT PRM_PROD_ID FROM  FODBA.FO_PRODUCTOS_PROMO WHERE PRM_PROD_ID = ?";
	     // se elimina condicion del sqlSelect ya que el campo cod_sap no existe en tabla fo_productos_promo PRM_COD_SAP = ?  AND 
	     String sqlUpdate = " UPDATE FODBA.FO_PRODUCTOS_PROMO SET " +
	      		"  PRM_NOM_BANNER = ? " +
	            ", PRM_DESC_BANNER = ? " +
	            ", PRM_OBS_BANNER = ?"  +
	            ", PRM_FCREACION = CURRENT_TIMESTAMP"+
	            ", PRM_USR_BANNER = ?"+  
	            ", PRM_COLOR_BANNER = ?"+
	            "  WHERE PRM_PROD_ID = ? "   ;
	     
	     String sqlInsert = " INSERT INTO FODBA.FO_PRODUCTOS_PROMO (PRM_PROD_ID,PRM_NOM_BANNER,PRM_DESC_BANNER,PRM_USR_BANNER,PRM_OBS_BANNER,PRM_FCREACION,PRM_COLOR_BANNER) " +
		            " VALUES (?,?,?,?,?,CURRENT_TIMESTAMP,?) ";
	     
	      Connection con = null;
	      
	      PreparedStatement psSelectPro  = null;	     
	      ResultSet rsSelectPro  = null;
	      
	      PreparedStatement psSelect = null;	     
	      ResultSet rsSelect = null;
	      
	      PreparedStatement psUpdate = null;
	      PreparedStatement psInsert = null;

	      try {
	         con = conexionUtil.getConexion();
	         psSelectPro = con.prepareStatement(sqlSelectPro);
	         psSelectPro.setLong(1, pro.getIdProducto());
	         rsSelectPro = psSelectPro.executeQuery();

       	 //si existen registros
       	 if(rsSelectPro.next()){
       	     	 
	        	 //se ejecuta la query de la tabla FODBA.FO_PRODUCTOS_PROMO
	        	 psSelect = con.prepareStatement(sqlSelect);
	        	 psSelect.setLong(1, pro.getIdProducto());
	        	 rsSelect = psSelect.executeQuery();
	
	        	 //si existen registros
	        	 if(rsSelect.next()){
	        		 //ejecucion del update
	        		 psUpdate = con.prepareStatement(sqlUpdate);
	        		 //parametros de entrada al update
	        		 psUpdate.setString(1, pro.getNombreBanner());
	        		 psUpdate.setString(2, pro.getDescripcionBanner());
	        		 psUpdate.setString(3, obs);
	        		 psUpdate.setLong(4, usr.getId_usuario());
	        		 psUpdate.setString(5, pro.getColorBanner());
	        		 psUpdate.setLong(6, pro.getIdProducto());
	        		 respuestaSQL = psUpdate.executeUpdate();
	        	 }else{
	        		 //sino se ejecuta el insert
	        		 psInsert = con.prepareStatement(sqlInsert);
	        		 psInsert.setLong(1, pro.getIdProducto());
	        		 psInsert.setString(2, pro.getNombreBanner());
	        		 psInsert.setString(3, pro.getDescripcionBanner());
	        		 psInsert.setLong(4, usr.getId_usuario());
	        		 psInsert.setString(5, obs);
	        		 psInsert.setString(6, pro.getColorBanner());
	        		 respuestaSQL = psInsert.executeUpdate();
	        	 }
       	 }
	          
	      } catch (Exception e) {
	    	  logger.error(e);
	         throw new DAOException(e);
	      } finally {
	      	 try {	      		
				if (rsSelectPro != null)
					rsSelectPro.close();
				if (psSelectPro != null)
					psSelectPro.close();
				if (rsSelect != null)
					rsSelect.close();
				if (psSelect != null)
					psSelect.close();
				if (psUpdate != null)
					psUpdate.close();
				if (psInsert != null)
					psInsert.close();
				if (con != null)
					con.close();	      		 
			} catch (SQLException e1) {
				logger.error(e1);
			}

	      }
	      return respuestaSQL;
  }
  
  /*
	* (revertBannerProductoOnLy)
	 * pkg @cl.bbr.boc.dao.ProductosDAO#updateBannerProductoOnLy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, cl.bbr.jumbocl.usuarios.dto.UserDTO)
	 * metodo que realiza la despublicacion de los banner de productos
	 * este metodo es para eliminar banner de productos.
	 * la tabla es FODBA.FO_PRODUCTOS_PROMO
	 * Param int
  */
  public int revertBannerProductoOnLy(FOProductoBannerDTO pro, String obs,
			UserDTO usr) throws DAOException {

		int respuestaSQL = 0;
		String sqlDelete = " DELETE FROM FODBA.FO_PRODUCTOS_PROMO WHERE PRM_PROD_ID = ? ";

		Connection con = null;
		PreparedStatement psDelete = null;

		try {
			con = conexionUtil.getConexion();

			// se ejecuta el script de borrado de registros FODBA.FO_PRODUCTOS_PROMO
			psDelete = con.prepareStatement(sqlDelete);
			psDelete.setLong(1, pro.getIdProducto());
			respuestaSQL = psDelete.executeUpdate();

		} catch (Exception e) {
			logger.error(e);
			throw new DAOException(e);
		} finally {
			try {
				if(psDelete != null)
					psDelete.close();
				if(con != null)
	      			con.close();
			} catch (SQLException e1) {
				logger.error(e1);
			}
		}
		return respuestaSQL;
	}

}