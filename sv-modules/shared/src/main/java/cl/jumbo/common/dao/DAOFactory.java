/*
 * Created on 30-ene-2009
 *
 */
package cl.jumbo.common.dao;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * @author jdroguett
 *  
 */
public class DAOFactory {
   private static Logging logger = new Logging(DAOFactory.class);
   private static Hashtable INSTANCES = new Hashtable();

   /**
    * @return
    * @throws DAOFactoryException
    */
   private static String implementacion(String interfaz) throws DAOFactoryException {
      java.io.InputStream impl = DAOFactory.class.getClassLoader().getResourceAsStream("/implements.properties");
      Properties prop = new Properties();
      try {
         prop.load(impl);
      } catch (IOException e) {
         throw new DAOFactoryException("Error fatal: no existe archivo /implements.properties");
      }
      return prop.getProperty(interfaz);
   }

   /**
    * Uso: ProductoDAO dao = (ProductoDAO)DAOFactory.getInstance(ProductoDAO.class.getName());
    * 
    * @param name
    * @return
    * @throws DAOFactoryException
    */
   public static Object getInstanceDAO(String name) throws DAOFactoryException {
      Object inst = INSTANCES.get(name);
      if (inst == null) {
         String implementacion = implementacion(name);
         try {
            Class c = Class.forName(implementacion);
            inst = c.newInstance();
            INSTANCES.put(name, inst);
         } catch (ClassNotFoundException e) {
            throw new DAOFactoryException("Error fatal: No existe implementacion para " + name
                  + " o nombre de implementación incorrecto");
         } catch (InstantiationException e) {
            throw new DAOFactoryException("Error fatal: Al instanciar la implementación de " + name);
         } catch (IllegalAccessException e) {
            throw new DAOFactoryException("Error fatal: Al instanciar la implementación de " + name);
         }
      }
      return inst;
   }
}
