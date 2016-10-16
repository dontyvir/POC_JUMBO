/*
 * Created on 09-nov-2009
 *
 */
package cl.bbr.bol.dao;

import java.util.Date;
import java.util.List;

import cl.bbr.bol.dto.AsistenciaPickeadorDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author jdroguett
 *
 */
public interface PickeadorDAO {

   /**
    * @param localId
    * @return
    * @throws DAOException
    */
   public List getEncargados(int localId) throws DAOException;

   /**
    * @param localId
    * @param patron
    * @return
    * @throws DAOException
    */
   public List getEncargados(int localId, String patron) throws DAOException;

   /**
    * @param localId
    * @param encargadoId
    * @return
    * @throws DAOException
    */
   public List getPickeadoresPorEncargado(int localId, int encargadoId) throws DAOException;

   /**
    * @param pickeadoresId
    * @param encargadoId
    * @param localId
    * @throws DAOException
    */
   public void updAsociacion(int[] pickeadoresId, int encargadoId, int localId) throws DAOException;

   /**
    * @return
    */
   public List getAsistencias() throws DAOException;

   /**
    * @param localId
    * @param encargadoId
    * @param fecha
    * @return
    */
   public List getAsistenciasPickeadores(int localId, int encargadoId, Date fecha)throws DAOException;

   /**
    * @param asistenciaPick
    */
   public void updAsistencia(AsistenciaPickeadorDTO asistenciaPick)throws DAOException;

}
