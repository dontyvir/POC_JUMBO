/*
 * Created on 01-oct-2009
 */
package cl.bbr.jumbocl.pedidos.util;

import java.util.Date;
import java.util.Hashtable;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * @author jdroguett
 */
public class Parametros {
   private long DESFASE = 3600000; //1 hora
   private Hashtable fecha; //una fecha por parametro
   private Hashtable hash;
   private Logging logger = new Logging(this);

   private Parametros() {
      fecha = new Hashtable();
      hash = new Hashtable();
   }

   /**
    *  
    */
   private void cargar(String nombre) {
      ParametrosService ps = new ParametrosService();
      try {
         ParametroDTO par = ps.getParametroByName(nombre);
         hash.put(nombre, par.getValor());
         fecha.put(nombre, new Date());
      } catch (ServiceException e) {
         e.printStackTrace();
      }
   }

   private synchronized void recargar(String nombre) {
      if (fecha.get(nombre) == null || ((Date) fecha.get(nombre)).getTime() + DESFASE <= System.currentTimeMillis()
            || hash.get(nombre) == null) {
         cargar(nombre);
      }
   }

   public static Parametros getInstance() {
      return SingletonHolder.INSTANCE;
   }

   public String getValor(String nombre) {
      recargar(nombre);
      return (String)hash.get(nombre);
   }

   /**
    * Para asegurar una única instancia. Que se carga la primera vez que se necesita y no cuando parte el servidor.
    */
   private static class SingletonHolder {
      private final static Parametros INSTANCE = new Parametros();
   }
}
