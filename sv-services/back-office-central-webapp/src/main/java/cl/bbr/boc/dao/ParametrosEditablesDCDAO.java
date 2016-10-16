package cl.bbr.boc.dao;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

public interface ParametrosEditablesDCDAO {

   /**
    * @return
    * @throws DAOException
    */
	public String getMontoLimite() throws DAOException;
	
   /**
    * @return
    * @throws DAOException
    */
	public String getDescuentoMayor() throws DAOException;
	
   /**
    * @return
    * @throws DAOException
    */
	public String getDescuentoMenor() throws DAOException;
	
   /**
    * @param monto
    * @throws DAOException
    */
	public void setMontoLimite(String monto) throws DAOException;
	
	/**
    * @param valor
    * @throws DAOException
    */
	public void setDescuentoMayor(String valor) throws DAOException;
	
	
	/**
    * @param valor
    * @throws DAOException
    */
	public void setDescuentoMenor(String valor) throws DAOException;
	
}
