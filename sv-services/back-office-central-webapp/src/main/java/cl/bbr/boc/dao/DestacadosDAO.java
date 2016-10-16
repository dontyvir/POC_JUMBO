/*
 * Created on 30-ene-2009
 */
package cl.bbr.boc.dao;

import java.util.List;

import cl.bbr.boc.dto.DestacadoDTO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author jdroguett
 *  
 */
public interface DestacadosDAO {

    /**
     * Retorna la lista de destacados
     * 
     * @return
     * @throws DAOException
     */
    public List getDestacados() throws DAOException;

    /**
     * @param destacadoDTO
     */
    public void addDestacado(DestacadoDTO destacadoDTO) throws DAOException;

    /**
     * @param id
     */
    public void delDestacado(int id) throws DAOException;

    /**
     * @param id
     * @return
     */
    public DestacadoDTO getDestacado(int id) throws DAOException;

    /**
     * @param id
     * @return
     */
    public List getProductosDestacados(int id) throws DAOException;

    /**
     * @param id
     * @return
     */
    public List getDestacadoLocales(int id) throws DAOException;

    /**
     * Si la lista de locales o de productos vienen vacias no se modifican en la base de datos.
     * @param destacadoDTO
     */
    public void updDestacado(DestacadoDTO destacadoDTO)  throws DAOException;

}
