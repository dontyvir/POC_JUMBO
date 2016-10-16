package cl.bbr.boc.dao;

import java.util.List;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

public interface CargarColaboradoresDAO {

   /**
    * @return
    * @throws DAOException
    */
	public boolean truncateTableColaboradres() throws DAOException;
	
   /**
    * @param colaboradores
    * @return
    * @throws DAOException
    */
	public boolean cargarColaboradores(List colaboradores) throws DAOException;
	
   /**
    * @return
    * @throws DAOException
    */
	public String cantidadColaboradores() throws DAOException;

}
