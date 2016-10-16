/*
 * Creado el 11-sep-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
package cl.cencosud.jumbocl.umbrales.dao;

import java.util.List;

import cl.bbr.jumbocl.shared.exceptions.DAOException;

/**
 * @author RMI - DNT
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
public interface UmbralDAO {

	public List getUmbralesAll() throws DAOException;
	
	public List getParametrosByCriteria() throws DAOException;

	
	
}
