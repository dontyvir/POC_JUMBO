/*
 * Created on 13-may-2009
 */
package cl.bbr.boc.dao;

import java.util.List;

import cl.bbr.boc.dto.SustitutoDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author jdroguett
 */
public interface SustitutosDAO {

   /**
    * @param pedidoId
    * @return
    * @throws DAOException
    */
   public List detallePedido(int pedidoId) throws DAOException;

   /**
    * @param localId
    * @param barra
    * @return
    * @throws DAOException
    */
   public SustitutoDTO sustituto(String localId, String barra) throws DAOException;

   /**
    * @param sustituto
    * @throws DAOException
    */
   public int updateSustituto(SustitutoDTO sustituto) throws DAOException;

}
