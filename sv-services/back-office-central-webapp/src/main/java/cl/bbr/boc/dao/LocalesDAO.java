/*
 * Created on 15-abr-2010
 */
package cl.bbr.boc.dao;

import java.util.List;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author jdroguett
 */
public interface LocalesDAO {
   
   public List getLocales() throws DAOException;

}
