/*
 * Created on 30-ene-2009
 */
package cl.bbr.fo.dao;

import java.util.List;

import cl.bbr.jumbocl.shared.exceptions.DAOException;


/**
 * @author jdroguett
 *  
 */
public interface DatosClienteDAO {

   /**
    * @return
    * @throws DAOException
    */
   public List getPreguntas(int clienteId) throws DAOException;

   /**
    * @param respuestas
    * @param clienteId
    * @throws DAOException
    */
   public void updateRespuestas(List respuestas, int clienteId) throws DAOException;

   /**
    * @return
    * @throws DAOException
    */
   public List getDependencia() throws DAOException;

}
