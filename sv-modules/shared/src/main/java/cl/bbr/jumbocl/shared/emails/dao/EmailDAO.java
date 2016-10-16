/*
 * Creado el Mar 23, 2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.shared.emails.dao;

import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.shared.emails.exceptions.EmailDAOException;

/**
 * @author jvillalobos
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public interface EmailDAO {

	/**
	 * Ingresa un registro para el envío de mail.
	 * 
	 * @param mail	DTO con datos del mail a enviar.
	 * @throws EmailDAOException
	 */
	public void addMail(MailDTO mail) throws EmailDAOException;

}
