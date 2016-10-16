package cl.bbr.jumbocl.clientes.ctrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import sun.misc.BASE64Encoder;

import cl.bbr.jumbocl.clientes.dao.DAOFactory;
import cl.bbr.jumbocl.clientes.dao.JdbcClientesDAO;
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteCarroDescComparator;
import cl.bbr.jumbocl.clientes.dto.ClienteCriteriaDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.CriterioClienteSustitutoDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.EstadoDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.clientes.exceptions.ClientesDAOException;
import cl.bbr.jumbocl.clientes.exceptions.ClientesException;
import cl.bbr.jumbocl.clientes.model.TipoCalleEntity;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.DuplicateKeyException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.ClienteEntity;
import cl.bbr.jumbocl.common.model.EstadoEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * Entrega metodos de navegacion por clientes y busqueda en base a criterios. 
 * Los resultados son listados de clientes o datos de cliente.
 * 
 * @author BBR
 *
 */ 
public class ClientesCtrl {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/**
	 * Obtiene cliente a partir de su id
	 * 
	 * @param  idcliente long
	 * @return ClientesDTO
	 * @throws ClientesException
	 */
	public ClientesDTO getClienteById(long idcliente) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			
			return clientesDAO.getClienteById(idcliente);
		} catch(ClientesDAOException ex){
			logger.debug("Problema getClienteById:"+ex);
			throw new ClientesException(ex);
		}
	}
	

	/**
	 * Obtiene cliente a partir de su RUT
	 * 
	 * @param  idcliente long
	 * @return ClientesDTO
	 * @throws ClientesException
	 */
	public ClientesDTO getClienteByRut(long rut) throws ClientesException {
		ClientesDTO row1 = new ClientesDTO();
		try{
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
		
			ClienteEntity cli = clientesDAO.getClienteByRut(rut);
			if(cli != null){
				String fec_crea = "";
				if(cli.getFec_crea()!=null)
					fec_crea = cli.getFec_crea().toString();
				String fec_act = "";
				if(cli.getFec_act()!=null)
					fec_act = cli.getFec_act().toString();
				String fec_nac = "";
				if(cli.getFec_nac()!=null)
					fec_nac = cli.getFec_nac().toString();
				
				row1 = new ClientesDTO(cli.getId().longValue(), 
						cli.getRut().longValue(), cli.getDv(),cli.getNombre(),cli.getApellido_pat(),cli.getApellido_mat(),
						cli.getClave(), cli.getEmail(), cli.getFon_cod_1(),cli.getFon_num_1(),
						cli.getFon_cod_2(), cli.getFon_num_2(), cli.getFon_cod_3(), cli.getFon_num_3(),
						cli.getRec_info().intValue(),
						fec_crea,fec_act, cli.getEstado(), 
						fec_nac,cli.getGenero());
				row1.setEst_bloqueo(cli.getBloqueo());
				row1.setCli_envio_mail(cli.getCli_envio_mail());
			}
		}catch(ClientesDAOException ex){
			logger.debug("Problema getClienteByRut:"+ex);
			throw new ClientesException(ex);
		}
		return row1;
	}

	
	/**
	 * Obtiene la cantidad de clientes de acuerdo a un criterio
	 * 
	 * @param  criteria ClienteCriteriaDTO
	 * @return int 
	 * @throws ClientesException
	 */
	
	public int getClientesCountByCriteria(ClienteCriteriaDTO criteria) throws ClientesException {	
//		return 11;
		int numCli = 0;
		try{
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
			
			numCli = clientesDAO.listadoClientesCountByCriteria(criteria);
		}catch(ClientesDAOException ex){
			logger.debug("Problema getClientesCountByCriteria:"+ex);
			throw new ClientesException(ex);
		}
		
		return numCli;
		
	}
	
	
	/**
	 * Obtiene listado de clientes de acuerdo a criterio
	 * 
	 * @param  criteria ClienteCriteriaDTO
	 * @return List de ClientesDTO's
	 * @throws ClientesException
	 */
	public List getClientesByCriteria(ClienteCriteriaDTO criteria)  throws ClientesException {
		List result= new ArrayList();
		try{
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
			
			List listaCli = new ArrayList();
			listaCli = (List) clientesDAO.listadoClientesByCriteria(criteria);
			ClienteEntity cli = null;
			for (int i = 0; i < listaCli.size(); i++) {
				cli = null;
				cli = (ClienteEntity) listaCli.get(i);
				String fecha = "";
				if (cli.getFec_nac()!=null)
					fecha = cli.getFec_nac().toString();
				ClientesDTO clidto = new ClientesDTO(cli.getId().longValue(),cli.getRut().longValue(), cli.getDv(),
						cli.getNombre(),cli.getApellido_pat(),cli.getApellido_mat(),
						fecha,cli.getEstado(), cli.getBloqueo());
				clidto.setEst_bloqueo(cli.getBloqueo());
				clidto.setRzs_empresa(cli.getRzs_empresa());
				
				if (cli.getId_empresa()!=null){
					clidto.setId_empresa(cli.getId_empresa().longValue());
				}
				
				result.add(clidto);
			}
		}catch(ClientesDAOException ex){
			logger.debug("Problema ClientesCTRL:"+ex);
			throw new ClientesException(ex);
		}
		
		return result;
	}
	
	/**
	 * Obtiene listado de clientes
	 * 
	 * @return List de ClientesDTO's
	 * @throws ClientesException
	 */	
	
	public List getClientesAll()  throws ClientesException {
		List result= new ArrayList();
		try{
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
			
			List listaCli = new ArrayList();
			listaCli = (List) clientesDAO.listadoClientes();
			ClienteEntity cli = null;
			for (int i = 0; i < listaCli.size(); i++) {
				cli = null;
				cli = (ClienteEntity) listaCli.get(i);
				ClientesDTO clidto = new ClientesDTO(cli.getId().longValue(),cli.getRut().longValue(),cli.getDv(),
						cli.getNombre(),cli.getApellido_pat(),cli.getApellido_mat(),
						Formatos.frmFecha(cli.getFec_nac().toString()),cli.getEstado(),cli.getBloqueo());
				result.add(clidto);
			}
		}catch(ClientesDAOException ex){
			logger.debug("Problema ClientesCTRL:"+ex);
			throw new ClientesException(ex);
		}
		
		return result;
	}
	
	
	/**
	 * Obtiene listado de estados de Clientes
	 * 
	 * @return List de EstadoDTO's
	 * @throws ClientesException
	 */
	
	public  List getEstadosClientes() throws ClientesException {
		List lista = new ArrayList();
		
		try{
			logger.debug("en getEstadosClientes");
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			
			List listaEst = new ArrayList();
			listaEst = (List) clientesDAO.getEstados("CL","S");
			EstadoEntity est = null;
			for (int i = 0; i < listaEst.size(); i++) {
				est = null;
				est = (EstadoEntity) listaEst.get(i); 
				EstadoDTO estDto = new EstadoDTO(est.getId().charValue(), est.getEstado());
				lista.add(estDto);
			}
		}catch(ClientesDAOException ex){
			logger.debug("Problema ClientesCTRL:"+ex);
			throw new ClientesException(ex);
		}
		return lista;
	}
	
	/**
	 * Realiza proceso de bloqueo de cliente
	 * 
	 * @param  id_cliente long
	 * @throws ClientesException
	 * @throws SystemException 
	 */
	public void doBloqueaCliente(long id_cliente) throws ClientesException, SystemException {
		
		try{
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			
			// Actualización
			clientesDAO.updBloqueoCliente( id_cliente, Constantes.CLI_BLOQUEADO );
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema ClientesCTRL:"+ex);
			throw new SystemException(ex);
		}
	    
	}

	
	/**
	 * Realiza proceso de desbloqueo de cliente
	 * 
	 * @param  id_cliente long
	 * @throws ClientesException
	 * @throws SystemException 
	 */
	public void doDesbloqueaCliente(long id_cliente)
		throws ClientesException, SystemException {
		
		try{
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			
			// Actualización
			clientesDAO.updBloqueoCliente( id_cliente, Constantes.CLI_DESBLOQUEADO );
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema ClientesCTRL:"+ex);
			throw new SystemException(ex);
		}
	    
	}	
	
	/**
	 * Obtiene el id de cliente de acuerdo a una busqueda por rut, apellido o ambas
	 * 
	 * @param  rut String
	 * @param  apellido String
	 * @return long
	 * @throws ClientesException
	 */
	
	public long getClienteByTips(String rut, String apellido) throws ClientesException {
		long res=0;
		try{
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			// HibernateClientesDAO clientesDAO =
			// (HibernateClientesDAO)DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
			
			res = clientesDAO.getClienteByTips(rut, apellido);
			logger.debug("res?"+res);
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema ClientesCTRL:"+ex);
			throw new ClientesException(ex);
		}
	    return res;
	}
	
	/**
	 * Obtiene un local de acuerdo a su id
	 * @param id_local
	 * @return
	 * @throws ClientesException
	 * @throws SystemException
	 */
	public LocalDTO getLocalById(long id_local)
		throws ClientesException, SystemException {
		
		JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
	
		try {
			return dao.getLocalById(id_local);
		} catch (ClientesDAOException e) {
			throw new SystemException(e);
		}
		
		
	}

	public boolean doModLocal(LocalDTO dto) 
		throws ClientesException, SystemException {
		
		JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
	
		try {
			return dao.doModLocal(dto);
		} catch (ClientesDAOException e) {
			throw new SystemException(e);
		}
	}
	public boolean doModZonaLocal(LocalDTO dto) 
			throws ClientesException, SystemException {
			
			JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
		
			try {
				return dao.doModZonaLocal(dto);
			} catch (ClientesDAOException e) {
				throw new SystemException(e);
			}
		}

	public boolean doAddLocal(LocalDTO dto)
		throws ClientesException, SystemException {
		
		JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
	
		try {
			return dao.doAddLocal(dto);
		} catch (ClientesDAOException e) {
			throw new SystemException(e);
		}
	}
	public int doAddLocalWithZone(LocalDTO dto)
			throws ClientesException, SystemException {
			
			JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
		
			try {
				return dao.doAddLocalWithZone(dto);
			} catch (ClientesDAOException e) {
				throw new SystemException(e);
			}
		}

    public boolean clienteEsConfiable(long rut) throws ClientesException, SystemException {        
        JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
        try {
            return dao.clienteEsConfiable(rut);
        } catch (ClientesDAOException e) {
            throw new SystemException(e);
        }
    }


    /**
     * @return
     */
    public List getRutsConfiables() throws ClientesException, SystemException {        
        JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
        try {
            return dao.getRutsConfiables();
        } catch (ClientesDAOException e) {
            throw new SystemException(e);
        }
    }


    /**
     * @param ruts
     * @return
     */
    public void addRutsConfiables(Vector ruts) throws ClientesException, SystemException {        
        JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
        try {
            dao.addRutsConfiables(ruts);
        } catch (ClientesDAOException e) {
            throw new SystemException(e);
        }
    }


    /**
     * @return
     */
    public List getLogRutsConfiables() throws ClientesException, SystemException {        
        JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
        try {
            return dao.getLogRutsConfiables();
        } catch (ClientesDAOException e) {
            throw new SystemException(e);
        }
    }


    /**
     * @param user
     * @param nombre
     */
    public void addLogRutConfiables(String user, String nombre, String msjLog) throws ClientesException, SystemException {        
        JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
        try {
            dao.addLogRutConfiables(user, nombre, msjLog);
        } catch (ClientesDAOException e) {
            throw new SystemException(e);
        }
    }


    /**
     * @param idCliente
     */
    public void actualizaContadorEncuestaCliente(long idCliente, long idPedido, int nroCompras) throws ClientesException, SystemException {        
        JdbcClientesDAO dao = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
        try {
            dao.actualizaContadorEncuestaCliente(idCliente, idPedido, nroCompras);
        } catch (ClientesDAOException e) {
            throw new SystemException(e);
        }
    }
    
    /**
     * 
     * Retorna la lista de productos del pedido por categoría.
     * 
     * @param id_pedido     Identificador único del cliente
     * @return              Lista de DTO con datos
     * 
     * @throws ClientesException
     * 
     */ 
    public List resumenCompraGetCatPro(long id_pedido) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.getResumenCompraCatPro(id_pedido);

        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (resumenCompraGetCatPro)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (resumenCompraGetCatPro)", ex);
            throw new ClientesException(ex);
        }
    }
    
    /**
     * Ingresa un registro para el envío de mail.
     * 
     * @param mail  DTO con datos del mail a enviar.
     * @throws ClientesException
     */ 
    public void addMail(MailDTO mail) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.addMail( mail );    
        
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (deleteCarroCompraAll)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (deleteCarroCompraAll)", ex);
            throw new ClientesException(ex);
        }
    }


	public boolean doBlanqueoDireccion(long id_cliente)  throws ClientesException, SystemException {
		
		try{
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			return clientesDAO.doBlanqueoDireccion( id_cliente);
			
		}catch(ClientesDAOException ex){
			logger.debug("Problema ClientesCTRL:"+ex);
			throw new SystemException(ex);
		}
	}


	public boolean tieneDireccionesConCobertura(long idCliente) throws ClientesException, SystemException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.tieneDireccionesConCobertura(idCliente);
        } catch ( NullPointerException ex ) {
            logger.error("addSustitutoCliente - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("addSustitutoCliente - Problem", ex);
            throw new ClientesException(ex);
        }
    }
	
	/*
	 * DESDE FO
	 * */
	
	/**
	 * Recupera la lista de dirección para un cliente que la retorna como una lista de DTO (DireccionesDTO).
	 * 
	 * @param cliente_id	identificador único del cliente
	 * @return				Lista de DTO (DireccionesDTO)
	 * @throws ClientesException
	 */
	public List listadoDireccionesFO(long cliente_id) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			return clientesDAO.listadoDireccionesFO(cliente_id);

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (listadoDirecciones)" );
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (listadoDirecciones)", ex);
			throw new ClientesException(ex);
		}
	}

	/**
	 * Ingresa un nuevo cliente.
	 * 
	 * @param cliente	DTO con datos del cliente
	 * @throws ClientesException
	 */	
	public long clienteInsertFO(ClienteDTO cliente) throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
			 .getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			//HibernateClientesDAO clientesDAO = (HibernateClientesDAO) DAOFactory
			//		.getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();

			// transformar DTO a entity
			cl.bbr.jumbocl.clientes.model.ClienteEntity cli = new cl.bbr.jumbocl.clientes.model.ClienteEntity();
			cli = this.clienteDTOtoEntityFO(cliente);
			return clientesDAO.insertClienteFO(cli);

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (clienteInsert)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (clienteInsert)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (clienteInsert)", ex);
			throw new ClientesException(ex);
		}

	}

	/**
	 * Modifica los datos de un cliente existente.
	 * 
	 * @param cliente	DTO con datos del cliente
	 * @throws ClientesException
	 */		
	public void clienteUpdateFO(ClienteDTO cliente) throws ClientesException {

		try {

			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			/*
			 * HibernateClientesDAO clientesDAO = (HibernateClientesDAO)
			 * DAOFactory .getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
			 */

			// transformar DTO a entity
			cl.bbr.jumbocl.clientes.model.ClienteEntity cli = new cl.bbr.jumbocl.clientes.model.ClienteEntity();
			cli = this.clienteDTOtoEntityFO(cliente);
			clientesDAO.updateClienteFO(cli);

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (clienteUpdate)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (clienteUpdate)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (clienteUpdate)", ex);
			throw new ClientesException(ex);
		}

	}

    /**
     * actualiza los datos de contacto de un cliente existente.
     * 
     * @param cliente   DTO con datos del cliente
     * @throws ClientesException
     */     
    public void updateDatosContactoClienteFO(ClienteDTO cliente) throws ClientesException {

        try {

            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();

            // transformar DTO a entity
            clientesDAO.updateDatosContactoClienteFO(cliente);

        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (updateDatosContactoCliente)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema DAO (updateDatosContactoCliente)", ex);
            throw new ClientesException(ex);
        } catch (Exception ex) {
            logger.error("Problema general (updateDatosContactoCliente)", ex);
            throw new ClientesException(ex);
        }

    }
    
    
	/**
	 * Retorna los datos del cliente en una entidad.
	 * 
	 * @param cliente_id	Identificador único del cliente a consultar
	 * @return				ClienteEntity con información del cliente
	 * 
	 * @throws ClientesException
	 */
	public ClienteDTO ClienteGetByIdFO(long cliente_id) throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			return clienteEntitytoDTOFO(clientesDAO.getClienteByIdFO(cliente_id));

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (ClienteGetById)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (ClienteGetById)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (ClienteGetById)", ex);
			throw new ClientesException(ex);
		}

	}
	
	/**
	 * Retorna los datos del cliente en una entidad.
	 * 
	 * @param rut	RUT del cliente a consultar
	 * @return		ClienteEntity con información del cliente
	 * 
	 * @throws ClientesException
	 */
	public ClienteDTO ClienteGetByRutFO(long rut) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			return this.clienteEntitytoDTOFO(clientesDAO.getClienteByRutFO(rut));

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (ClienteGetByRut)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (ClienteGetByRut)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (ClienteGetByRut)", ex);
			throw new ClientesException(ex);
		}

	}
	
	/**
	 * Desinscribe mail cliente
	 * 
	 * @param cliente	ClinteDTO
	 * @return	boolean se realizo el update o no
	 * 
	 * @throws ClientesException
	 */
	public boolean ClienteDesinscribeMailFO(ClienteDTO cliente) throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			return clientesDAO.desinscribeMailFO(cliente);

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (ClienteDesinscribeMail)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (ClienteDesinscribeMail)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (ClienteGetByRut)", ex);
			throw new ClientesException(ex);
		}

	}	

	/**
	 * Modifica el estado de un cliente existente. Esta acción no cambia la clave, ya que en el 
	 * comando anterior se le permite cambiar la clave en el formulario dispuesto para ello.
	 * El estado que se ingresa como parámetro determina el estado que queda el cliente 
	 * luego de modificar su clave.
	 * 
	 * @param rut		RUT del cliente
	 * @param estado	Estado del cliente 
	 * @throws ClientesException
	 */
	public void clienteChangePassFO(long rut, String estado)
	throws ClientesException {

	try {
		JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
	
		// HibernateClientesDAO clientesDAO = (HibernateClientesDAO)
		// DAOFactory
		// .getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();
	
		clientesDAO.passupdClienteFO(rut, estado );
	
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (clienteChangePass)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (clienteChangePass)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (clienteChangePass)", ex);
			throw new ClientesException(ex);
		}
	
	}

	/**
	 * 
	 * 
	 * @param cli_id	Id del cliente
	 * @param email		email del cliente
	 * @param codigo	codigo del telefono del cliente
	 * @param telefono	telefono del cliente 
	 * @throws ClientesException
	 */
	public void clienteChangeDatosPaso3FO(long cli_id, String email, String codigo, String telefono)
	throws ClientesException {

	try {
		JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
	
		clientesDAO.clienteChangeDatosPaso3FO(cli_id, email, codigo, telefono );
	
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (clienteChangeDatosPaso3)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (clienteChangeDatosPaso3)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (clienteChangeDatosPaso3)", ex);
			throw new ClientesException(ex);
		}
	
	}
	
	
	/**
	 * Modifica la clave de un cliente existente.
	 * El estado que se ingresa como parámetro determina el estado que queda el cliente 
	 * luego de modificar su clave.
	 * 
	 * @param rut		RUT del cliente
	 * @param estado	Estado del cliente
	 * @param clave		Clave nueva para el cliente 
	 * @throws ClientesException
	 */	
	public void clienteChangePassFO(long rut, String clave, String estado)
			throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			// HibernateClientesDAO clientesDAO = (HibernateClientesDAO)
			// DAOFactory
			// .getDAOFactory(DAOFactory.HIBERNATE).getClientesDAO();

			clientesDAO.passupdClienteFO(rut, clave, estado );

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (clienteChangePass)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (clienteChangePass)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (clienteChangePass)", ex);
			throw new ClientesException(ex);
		}

	}

	/**
	 * 
	 * Ingresa una nueva dirección de despacho para el cliente.
	 * 
	 * @param direccion	DTO con datos de la dirección a ingresar
	 * 
	 * @throws ClientesException
	 * 
	 */
	public long insertDireccionFO(DireccionesDTO direccion) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			return clientesDAO.insertDireccionFO(direccion);
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (insertDireccion)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (insertDireccion)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (insertDireccion)", ex);
			throw new ClientesException(ex);
		}
	}

	/**
	 * 
	 * Elimina una dirección de despacho para el cliente.
	 * 
	 * @param direccion_id	Identificador único de la dirección a eliminar
	 * 
	 * @throws ClientesException
	 * 
	 */
	public void deleteDireccionFO(long direccion_id) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			clientesDAO.deleteDireccionFO(direccion_id);

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (deleteDireccion)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (deleteDireccion)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (deleteDireccion)", ex);
			throw new ClientesException(ex);
		}
	}

	/**
	 * 
	 * Modifica una dirección de despacho para el cliente.
	 * 
	 * @param direccion_id		DTO con datos de la dirección a ingresar
	 * 
	 * @throws ClientesException
	 * 
	 */	
	public void updateDireccionFO(DireccionesDTO direccion_id)
			throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			// Recuperar los datos de la dirección para revisar si hay cambios
			DireccionesDTO dir = clientesDAO.getDireccionFO(direccion_id.getId());
			
			direccion_id.setNueva( dir.getNueva() );
			
			if( dir.getCalle().compareTo(direccion_id.getCalle()) != 0 )
				direccion_id.setNueva("N");
			else if( dir.getCom_id() != direccion_id.getCom_id() )
				direccion_id.setNueva("N");
			else if( dir.getDepto().compareTo(direccion_id.getDepto()) != 0 )
				direccion_id.setNueva("N");
			else if( dir.getNumero().compareTo(direccion_id.getNumero()) != 0 )
				direccion_id.setNueva("N");
			else if( dir.getTipo_calle() != direccion_id.getTipo_calle() )
				direccion_id.setNueva("N");

			clientesDAO.updateDireccionFO(direccion_id);

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (updateDireccion)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema DAO (updateDireccion)", ex);
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Problema general (updateDireccion)", ex);
			throw new ClientesException(ex);
		}
	}

	/**
	 * Retorna la lista de tipos de calle del sistema. 
	 * 
	 * @return Lista de tipos de calle
	 *  
	 * @throws ClientesException
	 */	
	public List getTiposCalleFO() throws ClientesException {
		List result = new ArrayList();

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			List lista = new ArrayList();
			lista = (List) clientesDAO.getTiposCalleFO();
			TipoCalleEntity dir = null;
			for (int i = 0; i < lista.size(); i++) {
				dir = null;
				dir = (cl.bbr.jumbocl.clientes.model.TipoCalleEntity) lista.get(i);
				cl.bbr.jumbocl.clientes.dto.TipoCalleDTO ent = new cl.bbr.jumbocl.clientes.dto.TipoCalleDTO();

				ent.setId(dir.getId().longValue());
				ent.setNombre(dir.getNombre());

				result.add(ent);
			}

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getTiposCalle)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (getTiposCalle)", ex);
			throw new ClientesException(ex);
		}

		return result;
	}

	/**
	 * Trasforma de DTO a Entity los datos del cliente
	 * 
	 * @param cliente
	 * @return Entidad con datos delos clientes
	 * @throws ClientesException
	 */
	private cl.bbr.jumbocl.clientes.model.ClienteEntity clienteDTOtoEntityFO(ClienteDTO cliente)
			throws ClientesException {

		try {

			// transformar DTO a entity
			cl.bbr.jumbocl.clientes.model.ClienteEntity cli = new cl.bbr.jumbocl.clientes.model.ClienteEntity();
			cli.setApellido_mat(cliente.getApellido_mat());
			cli.setApellido_pat(cliente.getApellido_pat());
			cli.setClave(cliente.getClave());
			cli.setDv(new Character(cliente.getDv().charAt(0)));
			cli.setEmail(cliente.getEmail());
			cli.setEstado(new Character(cliente.getEstado().charAt(0)));
			cli.setFec_crea(new Timestamp(cliente.getFec_crea()));
			cli.setFec_nac(new Date(cliente.getFec_nac()));
			cli.setFon_cod_1(cliente.getFon_cod_1());
			cli.setFon_cod_2(cliente.getFon_cod_2());
			cli.setFon_cod_3(cliente.getFon_cod_3());
			cli.setFon_num_1(cliente.getFon_num_1());
			cli.setFon_num_2(cliente.getFon_num_2());
			cli.setFon_num_3(cliente.getFon_num_3());
			cli.setGenero(new Character(cliente.getGenero().charAt(0)));
			cli.setId(new Long(cliente.getId()));
			cli.setNombre(cliente.getNombre());
			cli.setRec_info(new Long(cliente.getRec_info()));
			cli.setRut(new Long(cliente.getRut()));
			cli.setFec_act(new Timestamp(cliente.getFec_act()));
			cli.setPregunta( cliente.getPregunta() );
			cli.setRespuesta( cliente.getRespuesta() );
			cli.setCli_mod_dato(cliente.getCli_mod_dato());
			cli.setFec_login( new Timestamp(cliente.getFec_login()) );
			cli.setIntento( new Long( cliente.getIntento() ) );
			cli.setRecibeSms(cliente.getRecibeSms());
            cli.setRecibeEMail(cliente.getRecibeEMail());
			return cli;

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (clienteDTOtoEntity)");
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Transformación de dto a entidad (clienteDTOtoEntity)", ex);
			throw new ClientesException(ex);
		}

	}

	/**
	 * Trasforma de DTO a Entity los datos del cliente
	 * 
	 * @param cliente Entidad con datos del cliente
	 * @return DTO con datos del cliente
	 * @throws ClientesException
	 */
	private ClienteDTO clienteEntitytoDTOFO(cl.bbr.jumbocl.clientes.model.ClienteEntity cliente)
			throws ClientesException {

		ClienteDTO cli = null;
		
		try {

			if (cliente != null) {

				// transformar Entity a DTO
				cli = new ClienteDTO();
				cli.setApellido_mat(cliente.getApellido_mat());
				cli.setApellido_pat(cliente.getApellido_pat());
				cli.setClave(cliente.getClave());
				cli.setDv(cliente.getDv().toString());
				cli.setEmail(cliente.getEmail());
				cli.setEstado(cliente.getEstado().toString());
				//cli.setFec_crea(cliente.getFec_crea().getTime());
				cli.setFec_nac(cliente.getFec_nac().getTime());
				cli.setFon_cod_1(cliente.getFon_cod_1());
				cli.setFon_cod_2(cliente.getFon_cod_2());
				cli.setFon_cod_3(cliente.getFon_cod_3());
				cli.setFon_num_1(cliente.getFon_num_1());
				cli.setFon_num_2(cliente.getFon_num_2());
				cli.setFon_num_3(cliente.getFon_num_3());
				cli.setGenero(cliente.getGenero().toString());
				cli.setId(cliente.getId().longValue());
				cli.setNombre(cliente.getNombre());
				cli.setRec_info(cliente.getRec_info().longValue());
				cli.setRut(cliente.getRut().longValue());
				//cli.setFec_act(cliente.getFec_act().getTime());
				cli.setPregunta( cliente.getPregunta() );
				cli.setRespuesta( cliente.getRespuesta() );
				cli.setCli_mod_dato(cliente.getCli_mod_dato());
				if( cliente.getFec_login() != null )
					cli.setFec_login( cliente.getFec_login().getTime() );
				else
					cli.setFec_login( 0 );
				if( cliente.getIntento() != null )
					cli.setIntento( cliente.getIntento().longValue() );
				else
					cli.setIntento( 0 );
                cli.setRecibeSms(cliente.getRecibeSms());
//+20120724coh
                cli.setRecibeEMail(cliente.getRecibeEMail());
//-20120724coh
                if( cliente.getKey_CambioClave() != null )
                    cli.setKey_CambioClave( cliente.getKey_CambioClave() );
                else
                    cli.setKey_CambioClave("");
//[20121107avc
                cli.setColaborador(cliente.isColaborador());
//]20121107avc                
			}

			return cli;

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (clienteEntitytoDTO)");
			throw new ClientesException(ex);
		} catch (Exception ex) {
			logger.error("Transformación de entidad a dto (clienteEntitytoDTO)", ex);
			throw new ClientesException(ex);
		}
	}

	/**
	 * 
	 * Retorna la lista de productos del carro de compras del cliente. 
	 * Si el local es -1 sólo recupera los productos para consultar por datos de productos sin precios. 
	 * 
	 * @param local			Identificador del local asociado a la dirección de despachos
	 * @param cliente_id	Identificador único del cliente
	 * @return 				Lista de DTO con datos del los productos
	 * 
	 * @throws ClientesException
	 * 
	 */
	public List getCarroComprasFO(long cliente_id, String local, String idSession ) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			List listaCarro =  clientesDAO.getCarroCompraFO(cliente_id, local, idSession);     
            CarroCompraDTO car1 = null;
            HashMap miMap = new HashMap();
                                
            for (int i = 0; i < listaCarro.size(); i++) {
                 car1 = (CarroCompraDTO) listaCarro.get(i);           
                 logger.debug("pro_id :'"+car1.getPro_id()+"'");
                 miMap.put(car1.getPro_id(),car1);  
                
                 
                } 
            ArrayList llavesOrdenadas = new ArrayList(miMap.values());  
             Collections.sort(llavesOrdenadas,new ClienteCarroDescComparator());
              return llavesOrdenadas;
             //return new ArrayList(miMap.values());
           
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getCarroCompras)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (getCarroCompras)", ex);
			throw new ClientesException(ex);
		}
	}
    
    //riffo
    
    public List getCarroComprasCheckOutFO(long cliente_id, String local, String idSession ) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();         
         return clientesDAO.getCarroCompraCheckOutFO(cliente_id, local, idSession);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (getCarroCompras)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (getCarroCompras)", ex);
            throw new ClientesException(ex);
        }
    }
    
    /*
     * Crea el cliente desde la sesión de invitado para poder asociar el pedido y guardar la información
     * Se asocia el carro de compras y se ingresan los criterios de sustitución
     */
    public String creaClienteDesdeInvitadoFO(ClienteDTO cliente, DireccionesDTO desp, String forma_despacho) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            long idCliente = 0;
            long idDir = 0;
            idCliente = clientesDAO.creaClienteDesdeInvitadoFO(cliente);
            if (forma_despacho.equals("D"))
                idDir = clientesDAO.creaDireccionDesdeInvitadoFO(idCliente, desp);
            return idCliente + "--" + idDir;
            

        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (creaClienteDesdeInvitado)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (creaClienteDesdeInvitado)", ex);
            throw new ClientesException(ex);
        }
    }
    
    
    /*
     * Se asocia el carro de compras al Nuevo ID del Cliente
     */
    public boolean reasignaCarroDelInvitadoFO(long idCliente, long idInvitado) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.reasignaCarroDelInvitadoFO(idCliente, idInvitado);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (creaClienteDesdeInvitado)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (creaClienteDesdeInvitado)", ex);
            throw new ClientesException(ex);
        }
    }
    
    
    /*
     * Se asocian los criterios de sustitución al Nuevo ID de Cliente
     */
    public boolean reasignaSustitutosDelInvitadoFO(long idCliente, long idInvitado) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.reasignaSustitutosDelInvitadoFO(idCliente, idInvitado);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (creaClienteDesdeInvitado)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (creaClienteDesdeInvitado)", ex);
            throw new ClientesException(ex);
        }
    }
    
    /**
     * Retorna true si el carro esta vacio, de lo contrario retorna false
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @param idSession
     *            Identificador de la sessión
     * @return si el carro esta vacio o no
     * 
     * @throws SystemException
     */
    public boolean isCarroComprasEmptyFO(long cliente_id, String idSession ) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();

            return clientesDAO.isCarroComprasEmptyFO(cliente_id, idSession);

        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (isCarroComprasEmpty)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (isCarroComprasEmpty)", ex);
            throw new ClientesException(ex);
        }
    }
    
    /**
     * Retorna la cantidad de productos en el carro
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @param idSession
     *            Identificador de la sessión
     * @return cantidad de productos
     * 
     * @throws SystemException
     */
    public long carroComprasGetCantidadProductosFO(long cliente_id, String idSession ) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();

            return clientesDAO.carroComprasGetCantidadProductosFO(cliente_id, idSession);

        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (carroComprasGetCantidadProductos)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (carroComprasGetCantidadProductos)", ex);
            throw new ClientesException(ex);
        }
    }    
    
    /**
     * 
     * Retorna la lista de productos del carro de compras del cliente ordenado por categorias. 
     * Si el local es -1 sólo recupera los productos para consultar por datos de productos sin precios. 
     * 
     * @param local         Identificador del local asociado a la dirección de despachos
     * @param cliente_id    Identificador único del cliente
     * @return              Lista de DTO con datos del los productos
     * 
     * @throws ClientesException
     * 
     */
    public List getCarroComprasPorCategoriasFO(long cliente_id, String local, String idSession ) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();

            return clientesDAO.getCarroComprasPorCategoriasFO(cliente_id, local, idSession);

        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (getCarroComprasPorCategorias)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (getCarroComprasPorCategorias)", ex);
            throw new ClientesException(ex);
        }
    } 
    
    /**
     * 
     * elimina los productos no disponibles del carro de un cliente
     * 
     * @param local         Identificador del local asociado a la dirección de despachos
     * @param cliente_id    Identificador único del cliente
     * 
     * @throws ClientesException
     * 
     */
    public void eliminaProdCarroNoDispFO(long cliente_id, String local, String idSession ) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();

            clientesDAO.eliminaProdCarroNoDispFO(cliente_id, local, idSession);

        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (eliminaProdCarroNoDisp)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (eliminaProdCarroNoDisp)", ex);
            throw new ClientesException(ex);
        }
    }     
    
    /**
     * 
     * Retorna la lista de productos del carro de compras del cliente ordenado por categorias. 
     * Si el local es -1 sólo recupera los productos para consultar por datos de productos sin precios. 
     * 
     * @param local         Identificador del local asociado a la dirección de despachos
     * @param cliente_id    Identificador único del cliente
     * @return              Lista de DTO con datos del los productos
     * 
     * @throws ClientesException
     * 
     */
    public List getCarroComprasPorCategoriasFO(long cliente_id, String local, String idSession, String filtro) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();

            return clientesDAO.getCarroComprasPorCategoriasFO(cliente_id, local, idSession,filtro);

        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (getCarroComprasPorCategorias)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (getCarroComprasPorCategorias)", ex);
            throw new ClientesException(ex);
        }
    }       

	/**
	 * Ingresa un nuevo cliente.
	 * 
	 * @param cliente 		Identificador único del cliente
	 * @param despachos		Lista con direcciones de despacho del cliente
	 * 
	 * @throws ClientesException
	 */
	public String newClienteFO(ClienteDTO cliente, DireccionesDTO direccion) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			return clientesDAO.newClienteFO(cliente, direccion);
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (newCliente)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (newCliente)", ex);
			throw new ClientesException(ex);
		}
	}

	/**
	 * Retorna si tiene o no productos en el carro de compras.
	 * 
	 * @param cliente	Identificador único del cliente
	 * @return			True: existe, False: no existe
	 * 
	 * @throws ClientesException
	 */
	public boolean clienteExisteCarroComprasFO(long cliente) throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			return clientesDAO.clienteExisteCarroComprasFO(cliente);

		} catch ( NullPointerException ex ) {
			logger.error("clienteExisteCarroCompras - Problema con null en los datos");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("clienteExisteCarroCompras - Problema", ex);
			throw new ClientesException(ex);
		}		

	}

	/**
	 * Eliminar un producto desde el carro de compras del cliente.
	 * 
	 * @param cliente		Identificador único del cliento
	 * @param producto		Identificador único del producto del carro de compras
	 * @throws ClientesException
	 */
	public void carroComprasDeleteProductoFO(long cliente, long producto, String idSession) throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			clientesDAO.deleteCarroCompraFO(cliente, producto, idSession);

		} catch ( NullPointerException ex ) {
			logger.error("carroComprasDeleteProducto - Problema con null en los datos");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("carroComprasDeleteProducto - Problema", ex);
			throw new ClientesException(ex);
		}			
		
	}
    
    /**
     * Modifica Cantidad Producto Mi Carro
     * 
     * @param cliente       Identificador único del cliento
     * @param producto      Identificador único del producto del carro de compras
     * @param cantidad cantidad del producto
     * @param idSession id de session del cliente
     * @throws ClientesException
     */
    public void modificarCantidadProductoMiCarroFO(long cliente, long producto, double cantidad, String idSession, String tipoSel) throws ClientesException {

        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.modificarCantidadProductoMiCarroFO(cliente, producto, cantidad, idSession, tipoSel);

        } catch ( NullPointerException ex ) {
            logger.error("modificarCantidadProductoMiCarro - Problema con null en los datos");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("modificarCantidadProductoMiCarro - Problema", ex);
            throw new ClientesException(ex);
        }           
        
    }

	/**
	 * Eliminar un producto desde el carro de compras del cliente.
	 * 
	 * @param cliente		Identificador único del cliento
	 * @param producto		Identificador único del producto del carro de compras (por id de producto)
	 * @throws ClientesException
	 */
	public void carroComprasDeleteProductoxIdFO(long cliente, long producto, String idSession) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			clientesDAO.deleteCarroCompraProductoxIdFO(cliente, producto, idSession);
		} catch ( NullPointerException ex ) {
			logger.error("carroComprasDeleteProductoxId - Problema con null en los datos");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("carroComprasDeleteProductoxId - Problema", ex);
			throw new ClientesException(ex);
		}			
	}
	
	
	/**
	 * Actualiza la cantidad del producto del carro de compras del cliente.
	 * 
	 * @param cliente		Identificador único del cliente
	 * @param producto		Identificador único del producto del carro de compras
	 * @param cantidad		Valor para actualizar
	 * @param nota			Valor para actualizar
	 * @throws ClientesException
	 */
	public void carroComprasUpdateProductoFO(long cliente, long producto, double cantidad, String nota, String idSession, String tipoSel) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			clientesDAO.updateCarroCompraFO(cliente, producto, cantidad, nota, idSession, tipoSel);

		} catch ( NullPointerException ex ) {
			logger.error("carroComprasUpdateProducto - Problema con null en los datos");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("carroComprasUpdateProducto - Problema", ex);
			throw new ClientesException(ex);
		}					
	}

	/**
	 * Recupera últimas compras del cliente. 
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @return				Lista de DTO
	 * @throws ClientesException
	 */
	public List ultComprasGetListFO(long cliente_id) throws ClientesException {
		List result = new ArrayList();

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			result = (List) clientesDAO.getUltimasComprasFO(cliente_id);

		} catch ( NullPointerException ex ) {
			logger.error("ultComprasGetList - Problema con null en los datos (getCarroCompras)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("ultComprasGetList - Problema (getCarroCompras)", ex);
			throw new ClientesException(ex);
		}

		return result;
	}

	/**
	 * Recupera los productos de las listas de las últimas compras seleccionadas.
	 * 
	 * @param listas		Identificador de las listas
	 * @param local         Identificador único del local
	 * @param orden			Forma de ordenamiento de los productos
	 * @param cliente_id	Identificador único del cliente
	 * @return list			Lista de DTO
	 * @throws ClientesException
	 */
	public List ultComprasGetCatListFO(String listas, String local, long cliente_id, long rut ) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.getUltimasComprasCatProFO(listas, local, cliente_id, rut );
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (ultComprasGetCatList)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (ultComprasGetCatList)", ex);
			throw new ClientesException(ex);
		}
	}

	/**
	 * Inserta un producto al carro de compras.
	 * 
	 * @param listcarro		Lista de DTO
	 * @param cliente		Identificador único del cliente
	 * @throws ClientesException
	 */
	public void carroComprasInsertProductoFO(List listcarro, long cliente, String idSession)
		throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
		
			clientesDAO.insertCarroCompraFO(listcarro, cliente, idSession);
		
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (insertCarro)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (insertCarro)", ex);
			throw new ClientesException(ex);
		}
	}

	/**
	 * Almacena una lista de compra a partir del carro.
	 * 
	 * @param nombre		nombre de la lista
	 * @param cliente		Identificador único del cliente
	 * @throws ClientesException
	 */
	public int carroComprasSaveListFO(String nombre, long cliente)
		throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
		
			return clientesDAO.guardaListaCompraFO(nombre, cliente);
		
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (carroComprasSaveList)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (carroComprasSaveList)", ex);
			throw new ClientesException(ex);
		}
	}
    
    /**
     * Crea una sesion invitado
     * 
     * @param idSesion
     * @throws ClientesException
     */
    public int crearSesionInvitadoFO(String idSesion)
        throws ClientesException {

        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();
        
            return clientesDAO.crearSesionInvitadoFO(idSesion);
        
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (crearSesionInvitado)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (crearSesionInvitado)", ex);
            throw new ClientesException(ex);
        }
    }

	/**
	 * Elimina una lista de compra.
	 * 
	 * @param id_lista		id de la lista
	 * @throws ClientesException
	 */
	public int carroComprasDeleteListFO(int id_lista)
		throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
		
			return clientesDAO.eliminaListaCompraFO(id_lista);
		
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (carroComprasDeleteList)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (carroComprasDeleteList)", ex);
			throw new ClientesException(ex);
		}
	}	
	
	
	/**
	 * Retorna las políticas de sustitución del sistema.
	 * 
	 * @return Lista de DTO
	 * @throws ClientesException
	 */	
	public List PoliticaSustitucionFO()
		throws ClientesException {
			List result = new ArrayList();
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			
			result = (List) clientesDAO.getPoliticaSustitucionFO();	
		
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (PoliticaSustitucion)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (PoliticaSustitucion)", ex);
			throw new ClientesException(ex);
		}
		return result;
	}	

	/**
	 * 
	 * Retorna la lista de productos por categoría del carro de compras del cliente.
	 * 
	 * @param local			Identificador del local asociado a la dirección de despachos
	 * @param cliente_id	Identificador único del cliente
	 * @param modo_orden	Forma de ordenamiento de los productos
	 * 
	 * @return				Lista de DTO con datos
	 * 
	 * @throws ClientesException
	 * 
	 */	
	public List carroComprasGetCatProFO( String local, long cliente_id ) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.getCarroComprasCatProFO( local, cliente_id );

		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (carroComprasGetCatPro)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (carroComprasGetCatPro)", ex);
			throw new ClientesException(ex);
		}
	}	
	
	/**
	 * Retorna las formas de pago del sistema.
	 * 
	 * @return Lista de DTO
	 * @throws ClientesException
	 */	
	public List FormaPagoFO() throws ClientesException {
		List result = new ArrayList();
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			
			result = (List) clientesDAO.getFormaPagoFO();	
		
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (FormaPago)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (FormaPago)", ex);
			throw new ClientesException(ex);
		}
		return result;
	}	

	/**
	 * Elimina todos los productos del carro de compras.
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @throws ClientesException
	 */	
	public void deleteCarroCompraAllFO(long cliente_id, String ses_invitado_id) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			
			clientesDAO.deleteCarroCompraAllFO( cliente_id, ses_invitado_id );	
		
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (deleteCarroCompraAll)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (deleteCarroCompraAll)", ex);
			throw new ClientesException(ex);
		}
	}	

    /**
     * Elimina todos los productos del carro de compras.
     * 
     * @param cliente_id Identificador único del cliente
     * @param id_session
     * @throws ClientesException
     */ 
    public void limpiarMiCarroFO(long cliente_id, String id_session) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            
            clientesDAO.limpiarMiCarroFO(cliente_id, id_session); 
        
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (limpiarMiCarro)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (limpiarMiCarro)", ex);
            throw new ClientesException(ex);
        }
    }   
    
    
    //riffo
    
    public void deleteCarroCompraFO(long cliente_id, long carro_id, String idSession ) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            
            clientesDAO.deleteCarroCompraFO(cliente_id,carro_id,idSession ); 
        
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (deleteCarroCompraAll)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (deleteCarroCompraAll)", ex);
            throw new ClientesException(ex);
        }
    }   
    
    
	/**
	 * Ingresa un registro en el tracking para el web.
	 * 
	 * @param seccion	Sección de la página
	 * @param rut		RUT del cliente que deja el registro
	 * @param arg0		Información del navegador y url
	 * @throws ClientesException
	 */	
	public void addTrakingFO(String seccion, Long rut, HashMap arg0) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			
			clientesDAO.addTrakingFO( seccion, rut, arg0 );	
		
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (addTraking)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (addTraking)", ex);
			throw new ClientesException(ex);
		}
	}		

	/**
	 * Modifica la cantidad de intentos de login para un cliente.
	 * 
	 * @param cliente_id	Identificador del cliente.
	 * @param accion		1: Aumenta 0: Reset
	 * @throws ClientesException
	 */	
	public void updateIntentosFO(long cliente_id, long accion) throws ClientesException {

		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			clientesDAO.updateIntentosFO(cliente_id, accion);

		} catch ( NullPointerException ex ) {
			logger.error("updateIntentos - Problema con null en los datos");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("updateIntentos - Problema", ex);
			throw new ClientesException(ex);
		}					
	}
    
    /**
     * Modifica la cantidad de intentos de login para un cliente.
     * 
     * @param cliente_id    Identificador del cliente.
     * @param accion        1: Aumenta 0: Reset
     * @throws ClientesException
     */ 
    public void updateDatosInvitadoFO(ClienteDTO cliente, String opcion) throws ClientesException {

        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();

            clientesDAO.updateDatosInvitadoFO(cliente, opcion);

        } catch ( NullPointerException ex ) {
            logger.error("updateDatosInvitado - Problema con null en los datos");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("updateDatosInvitado - Problema", ex);
            throw new ClientesException(ex);
        }                   
    }    

    /**
     * @param idCliente
     * @param local
     * @param paginado
     * @param pagina
     * @return
     */
    public List getCarroComprasMobiFO(long idCliente, String local, int paginado, int pagina) throws ClientesException {
        List result = new ArrayList();
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            result = (List) clientesDAO.getCarroCompraMobiFO(idCliente, local, paginado, pagina);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (getCarroCompras)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (getCarroCompras)", ex);
            throw new ClientesException(ex);
        }
        return result;
    }

    /**
     * @param idCliente
     * @param local
     * @return
     */
    public double getCountCarroComprasFO(long idCliente, String local) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.getCountCarroComprasFO(idCliente, local);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (getCarroCompras)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (getCarroCompras)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idCarro
     * @param idProducto
     * @param nota
     * @return
     */
    public void updateNotaCarroCompraFO(long idCliente, long idCarro, long idProducto, String nota) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.updateNotaCarroCompraFO( idCliente, idCarro, idProducto, nota );
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (getCarroCompras)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (getCarroCompras)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List ultComprasGetListInternetFO(long idCliente) throws ClientesException {
        List result = new ArrayList();
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            result = (List) clientesDAO.getUltimasComprasInternetFO(idCliente);
        } catch ( NullPointerException ex ) {
            logger.error("ultComprasGetListInternet - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("ultComprasGetListInternet - Problem", ex);
            throw new ClientesException(ex);
        }
        return result;
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteMisListasFO(long idCliente) throws ClientesException {
        List result = new ArrayList();
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            result = (List) clientesDAO.clienteMisListasFO(idCliente);
        } catch ( NullPointerException ex ) {
            logger.error("clienteMisListas - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("clienteMisListas - Problem", ex);
            throw new ClientesException(ex);
        }
        return result;
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteMisListasPredefinidasFO() throws ClientesException {
        List result = new ArrayList();
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            result = (List) clientesDAO.clienteMisListasPredefinidasFO();
        } catch ( NullPointerException ex ) {
            logger.error("clienteMisListasPredefinidas - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("clienteMisListasPredefinidas - Problem", ex);
            throw new ClientesException(ex);
        }
        return result;
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteTieneSustitutosFO(long idCliente) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.clienteTieneSustitutosFO( idCliente );
        } catch ( NullPointerException ex ) {
            logger.error("clienteTieneSustitutos - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("clienteTieneSustitutos - Problem", ex);
            throw new ClientesException(ex);
        }
    }


    /**
     * @param idCliente
     * @param sustitutosPorCategorias
     */
    public void updateSustitutosClienteFO(Long idCliente, List sustitutosPorCategorias) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.updateSustitutosClienteFO( idCliente, sustitutosPorCategorias );
        } catch ( NullPointerException ex ) {
            logger.error("updateSustitutosCliente - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("updateSustitutosCliente - Problem", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @param criteriosProductos
     * @param idSession
     * 
     */
    public void guardaCriteriosMiCarroFO(Long idCliente, List criteriosProductos, String idSession) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.guardaCriteriosMiCarroFO( idCliente, criteriosProductos, idSession );
        } catch ( NullPointerException ex ) {
            logger.error("guardaCriteriosMiCarro - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("guardaCriteriosMiCarro - Problem", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @param prodsNuevos
     */
    public void addSustitutosClienteFO(long idCliente, List prodsNuevos) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.addSustitutosClienteFO( idCliente, prodsNuevos );
        } catch ( NullPointerException ex ) {
            logger.error("addSustitutosCliente - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("addSustitutosCliente - Problem", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteHaCompradoFO(long idCliente) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.clienteHaCompradoFO( idCliente );
        } catch ( NullPointerException ex ) {
            logger.error("clienteHaComprado - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("clienteHaComprado - Problem", ex);
            throw new ClientesException(ex);
        }
    }
    /**
     * @param idGrupo
     * @return
     */
    public List listasEspecialesByGrupoFO(long idGrupo, int idLocal) throws ClientesException {
        List result = new ArrayList();
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            result = (List) clientesDAO.listasEspecialesByGrupoFO(idGrupo, idLocal);
        } catch ( NullPointerException ex ) {
            logger.error("listasEspecialesByGrupo - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("listasEspecialesByGrupo - Problem", ex);
            throw new ClientesException(ex);
        }
        return result;
    }

    /**
     * @param listas
     * @param local
     * @return
     */
    public List clientesGetProductosListasEspecialesFO(String listas, String local, long idCliente) throws ClientesException {
        List result = new ArrayList();
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            result = (List) clientesDAO.clientesGetProductosListasEspecialesFO(listas, local, idCliente);

        } catch ( NullPointerException ex ) {
            logger.error("clientesGetProductosListasEspeciales - Problema con null en los datos (getCarroCompras)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("clientesGetProductosListasEspeciales - Problema (getCarroCompras)", ex);
            throw new ClientesException(ex);
        }
        return result;
    }

    /**
     * @param idCliente
     * @param idDireccion
     * @return
     */
    public DireccionesDTO clienteGetDireccionFO(long idDireccion) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.getDireccionFO(idDireccion);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (clienteGetDireccion)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (clienteGetDireccion)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteTieneDisponibleRetirarEnLocalFO(long idCliente) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.clienteTieneDisponibleRetirarEnLocalFO(idCliente);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (clienteGetDireccion)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (clienteGetDireccion)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idProducto
     * @return
     */
    public CriterioClienteSustitutoDTO criterioSustitucionByClienteProductoFO(long idCliente, long idProducto) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.criterioSustitucionByClienteProductoFO( idCliente, idProducto );
        } catch ( NullPointerException ex ) {
            logger.error("criterioSustitucionByClienteProducto - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("criterioSustitucionByClienteProducto - Problem", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @param criterio
     */
    public void setCriterioClienteFO(long idCliente, CriterioClienteSustitutoDTO criterio) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.setCriterioClienteFO( idCliente, criterio );
        } catch ( NullPointerException ex ) {
            logger.error("setCriterioCliente - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("setCriterioCliente - Problem", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @param criterio
     */
    public void addSustitutoClienteFO(long idCliente, CriterioClienteSustitutoDTO criterio) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.addSustitutoClienteFO( idCliente, criterio );
        } catch ( NullPointerException ex ) {
            logger.error("addSustitutoCliente - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("addSustitutoCliente - Problem", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean tieneDireccionesConCoberturaFO(long idCliente) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.tieneDireccionesConCoberturaFO( idCliente );
        } catch ( NullPointerException ex ) {
            logger.error("addSustitutoCliente - Problema con null");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("addSustitutoCliente - Problem", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteAllDireccionesConCoberturaFO(long idCliente) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.clienteAllDireccionesConCoberturaFO(idCliente);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (clienteAllDireccionesConCobertura)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (clienteAllDireccionesConCobertura)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteConOPsTrackingFO(long idCliente) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.clienteConOPsTrackingFO(idCliente);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (clienteConOPsTracking)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (clienteConOPsTracking)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteListaOPsTrackingFO(long idCliente) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.clienteListaOPsTrackingFO(idCliente);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (clienteListaOPsTracking)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (clienteListaOPsTracking)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param rut
     * @return
     */
    public List clienteComprasEnLocalFO(long rut) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.clienteComprasEnLocalFO(rut);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (clienteComprasEnLocal)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (clienteComprasEnLocal)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param email
     */
    public void addMailHomeFO(String email) throws ClientesException, DuplicateKeyException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.addMailHomeFO( email );   
        
        } catch (DuplicateKeyException ex) {
            logger.error("Problema BizDelegate (addMailHome)", ex);
            throw new DuplicateKeyException(ex);            
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (addMailHome)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (addMailHome)", ex);
            throw new ClientesException(ex);
        }
    }
    

    /**
     * @param idCliente
     * @return
     */
    public boolean AlmacenaConfirmacionMapaFO(double lat, double lng, long dir_id) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.AlmacenaConfirmacionMapaFO(lat, lng, dir_id);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (AlmacenaConfirmacionMapa)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (AlmacenaConfirmacionMapa)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param rut
     * @return
     */
    public void eliminaSuscripcionEncuestaByRutFO(long rut) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.eliminaSuscripcionEncuestaByRutFO(rut);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (eliminaSuscripcionEncuestaByRut)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (eliminaSuscripcionEncuestaByRut)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idComuna
     * @return
     */
    public DireccionesDTO getDireccionClienteByComunaFO(long idCliente, long idComuna) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.getDireccionClienteByComunaFO(idCliente, idComuna);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (getDireccionClienteByComuna)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (getDireccionClienteByComuna)", ex);
            throw new ClientesException(ex);
        }
    }
    
    public void convierteCarroDonaldAfterPagoFO(long idCliente, String idSession, long idInvitado) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.convierteCarroDonaldAfterPagoFO(idCliente, idSession, idInvitado);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (conviertecarroDonald)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (conviertecarroDonald)", ex);
            throw new ClientesException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idSession
     */
    public void convierteCarroDonaldFO(long idCliente, String idSession) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.convierteCarroDonaldFO(idCliente, idSession);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (conviertecarroDonald)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (conviertecarroDonald)", ex);
            throw new ClientesException(ex);
        }
    }

    
    public void RecuperaClave_GuardaKeyClienteFO(long idCliente, String key) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.RecuperaClave_GuardaKeyClienteFO(idCliente, key);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (RecuperaClave_GuardaKeyCliente)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (RecuperaClave_GuardaKeyCliente)", ex);
            throw new ClientesException(ex);
        }
    }


    public String RecuperaClave_getKeyClienteFO(long idCliente) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            return clientesDAO.RecuperaClave_getKeyClienteFO(idCliente);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (RecuperaClave_GuardaKeyCliente)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (RecuperaClave_GuardaKeyCliente)", ex);
            throw new ClientesException(ex);
        }
    }
    
    public void setClienteFacebookFO(long id_cliente, String id_facebook)throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
            clientesDAO.setClienteFacebookFO(id_cliente, id_facebook);
        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (setClienteFace)" );
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (setClienteFace)", ex);
            throw new ClientesException(ex);
        }
    }
	
    
	public String getRutByIdFacebookFO(String idFacebook) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getClientesDAO();
			return clientesDAO.getRutByIdFacebookFO(idFacebook);
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (newClienteFace)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (newClienteFace)", ex);
			throw new ClientesException(ex);
		}
	}
	
	/**
     * se filtro por idProducto
     * @param cliente_id
     * @param local
     * @param idSession
     * @param filtro
     * @return
     * @throws ClientesException
     */
    public List getItemCarroComprasPorCategoriasFO(long cliente_id, String local, String idSession, String filtro) throws ClientesException {
        try {
            JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
                    .getDAOFactory(DAOFactory.JDBC).getClientesDAO();

            return clientesDAO.getItemCarroComprasPorCategoriasFO(cliente_id, local, idSession,filtro);

        } catch ( NullPointerException ex ) {
            logger.error("Problema con null en los datos (getItemCarroComprasPorCategorias)");
            throw new ClientesException(ex);
        } catch (ClientesDAOException ex) {
            logger.error("Problema (getItemCarroComprasPorCategorias)", ex);
            throw new ClientesException(ex);
        }
    }
        
    public void recuperaClaveFO(ClienteDTO cliente, String contextPath) throws ClientesException{
    	try {
    		BASE64Encoder encoder = new BASE64Encoder();

    		String passwd = Utils.genPasswordFO(20);
    		String passwd_crypt = Utils.encriptarFO(passwd);
    		RecuperaClave_GuardaKeyClienteFO(cliente.getId(), passwd_crypt);

    		Calendar cal = Calendar.getInstance();
    		cal.setTime(new java.util.Date());
    		cal.add(Calendar.DATE, 2);
    		String DATE_FORMAT = "yyyyMMdd HHmmss";
    		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    		String token = cliente.getRut() + "--" + sdf.format(cal.getTime()) + "--" + passwd_crypt;
    		String token_encode64 = encoder.encode(token.getBytes());

    		String token_URLEncode = URLEncoder.encode(token_encode64,"UTF-8");
    		String URL = contextPath+"/ChangeForgottenPasswordForm?token=" + token_URLEncode;

    		//lo siento lo del html aqui pero se necesita compartir esta funcionalidad entre 2 aplicaciones web :(    		
    		StringBuffer sf = new StringBuffer();
    		
			sf.append("<!DOCTYPE html>");
			sf.append("<html>"); 
			sf.append("<head> ");
			sf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />");
			sf.append("<META HTTP-EQUIV=\"CACHE-CONTROL\" CONTENT=\"NO-CACHE\">");
			sf.append("<META HTTP-EQUIV=\"PRAGMA\" CONTENT=\"NO-CACHE\">"); 
			sf.append("<META HTTP-EQUIV=\"EXPIRES\" CONTENT=\"Mon, 22 Jul 2002 11:12:01 GMT\">");
			sf.append("<title>Jumbo.cl</title>"); 
			sf.append("</head>"); 			
			sf.append("<body>"); 
				sf.append("<table width=\"670\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">");
				sf.append("<tr><td valign=\"top\" ><img src=\"http://www.jumbo.cl/FO_IMGS/img/estructura/imprimir/imprimir-arriba.jpg\"></td></tr>"); 
				sf.append("<tr>");
					sf.append("<td width=\"673\" valign=\"top\">");
						sf.append("<table width=\"670\" border=\"0\" cellpadding=\"11\" cellspacing=\"0\">");
						sf.append("<tr>");
						sf.append("<td width=\"645\" valign=\"top\">");
						sf.append("<font face=\"Trebuchet MS\" size=\"4\" color=\"#93BE2E\"><strong>__NOMBRE_CLIENTE__</strong></font>");
						sf.append("<br/><br/>");
						sf.append("<font face=\"Trebuchet MS\" size=\"3\" color=\"#333333\"> Estimad@ cliente<br/><br/>");
						sf.append("Para cambiar tu clave de acceso a Jumbo.cl haz click en este <a href=\"__URL__\">link</a> e ingresa tu nueva clave");
						sf.append("<br/>");
						sf.append("Si no puedes acceder a este link haciendo click en &eacute;l, c&oacute;pialo y p&eacute;galo en tu navegador."); 
						sf.append("<br/><br/>__URL__<br /><br />"); 
						sf.append("Si no solicitaste cambio de clave en jumbo.cl, por seguridad no tomes en cuenta este correo. ");
						sf.append("<br/><br/>");
						sf.append("</font>");
						sf.append("</td>");
						sf.append("</tr> ");
						sf.append("</table>");
					sf.append("<img src=\"http://www.jumbo.cl/FO_IMGS/img/estructura/imprimir/abajo2b.gif\"/>");
					sf.append("</td>");
				sf.append("</tr>");
				sf.append("</table>");
			sf.append("</body>");
			sf.append("</html>");
    		
    		/*String arc_mail = rb_fo.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("arc_mail");
			 // Carga el template html
			 TemplateLoader load_mail = new TemplateLoader(arc_mail);
			 ITemplate tem_mail = load_mail.getTemplate();
			 IValueSet top_mail = new ValueSet();*/
			
			    		/*  top_mail.setVariable("{nombre_cliente}", cliente.getNombre()+" " + cliente.getApellido_pat() );
			 top_mail.setVariable("{url}", URL);*/
    		String result = sf.toString();
    		result = result.replaceAll("__URL__", URL);
    		result = result.replaceAll("__NOMBRE_CLIENTE__", cliente.getNombre()+" "+cliente.getApellido_pat());//tem_mail.toString(top_mail);*/

    		// Se envía mail al cliente
    		MailDTO mail = new MailDTO();
    		mail.setFsm_subject("Recupera clave www.jumbo.cl");
    		mail.setFsm_data( result );
    		mail.setFsm_destina( cliente.getEmail() );
    		mail.setFsm_remite("contacto@jumbo.cl");
    		addMail( mail );

    	} catch (Exception ex) {
    		logger.error("Problema (recuperaClaveFO)", ex);
    		throw new ClientesException(ex);
    	}
    }
    
    
    // CREA DIRECCIÓN DUMMY
    //=======================
    public DireccionesDTO crearDireccionDummy(DireccionesDTO d) throws ClientesException{
    	
    	 DireccionesDTO desp = new DireccionesDTO();
    	try{
	            
	        desp.setAlias("Nombre");
	        desp.setTipo_calle(2); //1:Avenida, 2:Calle, 3:Pasaje
	        desp.setCalle("Ingresa tu calle");
	        desp.setNumero("000");
	        desp.setDepto("");
	        desp.setCom_id(d.getCom_id());
	        desp.setReg_id(d.getReg_id());
	        desp.setComentarios("");
    	}catch (Exception ex) {
    		logger.error("Problema (crearDireccionDummy)", ex);
    		throw new ClientesException(ex);
    	}
        
        return desp;
    }
    
    //es Direccion dummy
    public boolean isDireccionDummy(DireccionesDTO d) throws ClientesException{
    	
    	boolean rs = false;
	   	try{
	   		
		        if("Nombre".equals(d.getAlias()) && d.getTipo_calle() == 2 && "Ingresa tu calle".equals(d.getCalle()) &&
		        		"000".equals(d.getNumero()) && "".equals(d.getDepto()) && "".equals(d.getComentarios())){
		        	rs = true;
		        }

	   	}catch (Exception ex) {
	   		logger.error("Problema (isDireccionDummy)", ex);
	   		throw new ClientesException(ex);
	   	}
       
       return rs;
   }
    public List getCarroComprasVentaMasiva(long cliente_id, String local, String idSession ) throws ClientesException {
		try {
			JdbcClientesDAO clientesDAO = (JdbcClientesDAO) DAOFactory
					.getDAOFactory(DAOFactory.JDBC).getClientesDAO();

			List listaCarro =  clientesDAO.getCarroCompraVentaMasiva(cliente_id, local, idSession);     
            CarroCompraDTO car1 = null;
            HashMap miMap = new HashMap();
                                
            for (int i = 0; i < listaCarro.size(); i++) {
                 car1 = (CarroCompraDTO) listaCarro.get(i);           
                 logger.debug("pro_id :'"+car1.getPro_id()+"'");
                 miMap.put(car1.getPro_id(),car1);  
                
                 
                } 
            ArrayList llavesOrdenadas = new ArrayList(miMap.values());  
             Collections.sort(llavesOrdenadas,new ClienteCarroDescComparator());
              return llavesOrdenadas;
             //return new ArrayList(miMap.values());
           
		} catch ( NullPointerException ex ) {
			logger.error("Problema con null en los datos (getCarroCompras)");
			throw new ClientesException(ex);
		} catch (ClientesDAOException ex) {
			logger.error("Problema (getCarroCompras)", ex);
			throw new ClientesException(ex);
		}
	}
}
