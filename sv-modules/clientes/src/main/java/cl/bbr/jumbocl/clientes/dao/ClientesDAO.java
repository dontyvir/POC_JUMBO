package cl.bbr.jumbocl.clientes.dao;

import java.util.HashMap;
import java.util.List;

import cl.bbr.jumbocl.clientes.dto.ClienteCriteriaDTO;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.ProcModAddrBookDTO;
import cl.bbr.jumbocl.clientes.exceptions.ClientesDAOException;
import cl.bbr.jumbocl.clientes.model.ClienteEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.ZonaEntity;


/**
 * Permite las operaciones en base de datos sobre las Categorias Web.
 * @author BBR
 *
 */
public interface ClientesDAO {
	
	/**
	 * Obtiene listado de clientes de acuerdo a criterio de búsqueda
	 * 
	 * @param  criteria
	 * @return List ClienteEntity
	 * @throws ClientesDAOException
	 */
	public List listadoClientesByCriteria( ClienteCriteriaDTO criteria ) throws ClientesDAOException;
	
	/**
	 * Obtiene número de registros de una query por criterio
	 * 
	 * @param  criteria
	 * @return int
	 * @throws ClientesDAOException
	 */
	public int listadoClientesCountByCriteria(ClienteCriteriaDTO criteria) throws ClientesDAOException ;
	
	/**
	 * Obtiene el listado de clientes
	 * 
	 * @return List ClienteEntity
	 * @throws ClientesDAOException
	 */
	public List listadoClientes( ) throws ClientesDAOException;
	
	/**
	 * Obtiene el listado de Direcciones
	 * 
	 * @param  cliente_id
	 * @return List DireccionEntity
	 * @throws ClientesDAOException
	 */
	public List listadoDirecciones( long cliente_id ) throws ClientesDAOException;
	
	/**
	 * Obtiene cliente a partir de su id
	 * 
	 * @param  cliente_id
	 * @return ClienteEntity
	 * @throws ClientesDAOException
	 */
	public ClientesDTO getClienteById( long cliente_id ) throws ClientesDAOException;
	
	/**
	 * Obtiene Direccion a partir de su id
	 * 
	 * @param  direccion_id
	 * @return DireccionEntity
	 * @throws ClientesDAOException
	 */
	public DireccionEntity getDireccionById( long direccion_id ) throws ClientesDAOException;
	
	/**
	 * Actualiza una direccion de acuerdo a su id
	 * 
	 * @param  params
	 * @return boolean
	 * @throws ClientesDAOException
	 */
	public boolean actualizaDireccionById(ProcModAddrBookDTO params) throws ClientesDAOException;
	
	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List LocalEntity
	 * @throws ClientesDAOException
	 */
	public List listadoLocales() throws ClientesDAOException;
	
	/**
	 * Obtiene un listado de comunas
	 * 
	 * @return List ComunaEntity
	 * @throws ClientesDAOException
	 */
	public List listadoComunas() throws ClientesDAOException;
	
	/**
	 * Obtiene un listado de zonas
	 * 
	 * @return List ZonaEntity
	 * @throws ClientesDAOException
	 */
	public List listadoZonas() throws ClientesDAOException;
	
	/**
	 * Obtiene un listado de Tipo de Calles
	 * 
	 * @return List TipoCalleEntity
	 * @throws ClientesDAOException
	 */
	public List listadoTiposCalle() throws ClientesDAOException;
	
	/**
	 * Obtiene un listado de estados de acuerdo al tipo de estado y si es visible o no en la web
	 * 
	 * @param  tip_estado
	 * @param  visible
	 * @return List EstadoEntity
	 * @throws ClientesDAOException
	 */
	public List getEstados(String tip_estado,String visible)throws ClientesDAOException;
	
	/**
	 * Cambia el estado de bloqueo del cliente
	 * 
	 * @param  id_cliente
	 * @param  estado
	 * @throws ClientesDAOException
	 */
	public void updBloqueoCliente(long id_cliente, String estado) throws ClientesDAOException;
	
	/**
	 * Obtiene los id de cliente de acuerdo al rut o apellido
	 * 
	 * @param  rut
	 * @param  apellido
	 * @return long
	 * @throws ClientesDAOException
	 */
	public long getClienteByTips(String rut, String apellido) throws ClientesDAOException;
	
	/**
	 * Obtiene un listado de comunas de acuerdo a un id de zona
	 * 
	 * @param  id_zona
	 * @return List ComunaEntity
	 * @throws ClientesDAOException
	 */
	//public List getComunasByZona(long id_zona) throws ClientesDAOException;
	
	/**
	 * Obtiene un listado de Zonas de acuerdo a un id de comuna
	 * 
	 * @param  id_comuna
	 * @return List ZonaEntity
	 * @throws ClientesDAOException
	 */
	public List getZonasByComuna(long id_comuna) throws ClientesDAOException;
	
	/**
	 * Obtiene un listado de locales de acuedo a un id de zona
	 * 
	 * @param  id_zona
	 * @return List LocalEntity
	 * @throws ClientesDAOException
	 */
	public List getLocalesByZona(long id_zona) throws ClientesDAOException;
	
	/**
	 * Obtiene informacion de la zona de acuerdo a su id
	 * 
	 * @param  id_zona
	 * @return ZonaEntity
	 * @throws ClientesDAOException
	 */
	public ZonaEntity getZonaById(long id_zona) throws ClientesDAOException;
	
	/**
	 * Obtiene un local segun su id
	 * @param id_local
	 * @return
	 * @throws LocalDAOException
	 */
	public LocalDTO getLocalById(long id_local) throws ClientesDAOException;
	
	/**
	 * Modifica los campos del Local
	 * @param dto
	 * @return boolean resultado
	 * @throws ClientesDAOException
	 */
	public boolean doModLocal(LocalDTO dto) throws ClientesDAOException;
	
	
	/*
	 * DESDE EL FO
	 * */
	
	/**
	 * Recupera la lista de dirección para un cliente que la retorna como una lista de DTO (DireccionesDTO).
	 * 
	 * @param cliente_id	identificador único del cliente
	 * @return				Lista de DTO (DireccionesDTO)
	 * @throws ClientesDAOException
	 */
	public List listadoDireccionesFO( long cliente_id ) throws ClientesDAOException;

	/**
	 * Recupera la dirección para un cliente que la retorna un DTO (DireccionesDTO).
	 * 
	 * @param dir_id	identificador único de la direccion
	 * @return				DTO (DireccionesDTO)
	 * @throws ClientesDAOException
	 */
	public DireccionesDTO getDireccionFO( long dir_id ) throws ClientesDAOException;	
	
	/**
	 * Ingresa un nuevo cliente.
	 * 
	 * @param cliente	DTO con datos del cliente
	 * @throws ClientesDAOException
	 */	
	public long insertClienteFO(ClienteEntity cliente) throws ClientesDAOException;
	
	/**
	 * Modifica los datos de un cliente existente.
	 * 
	 * @param cliente	DTO con datos del cliente
	 * @throws ClientesDAOException
	 */	
	public void updateClienteFO(ClienteEntity cliente) throws ClientesDAOException;
	
	/**
	 * Modifica el estado de un cliente existente. Esta acción no cambia la clave, ya que en el 
	 * comando anterior se le permite cambiar la clave en el formulario dispuesto para ello.
	 * El estado que se ingresa como parámetro determina el estado que queda el cliente 
	 * luego de modificar su clave.
	 * 
	 * @param rut		RUT del cliente
	 * @param estado	Estado del cliente 
	 * @throws ClientesDAOException
	 */
	public void passupdClienteFO(long rut, String estado) throws ClientesDAOException;
	
	/**
	 * Modifica la clave de un cliente existente.
	 * El estado que se ingresa como parámetro determina el estado que queda el cliente 
	 * luego de modificar su clave.
	 * 
	 * @param rut		RUT del cliente
	 * @param estado	Estado del cliente
	 * @param clave		Clave nueva para el cliente 
	 * @throws ClientesDAOException
	 */		
	public void passupdClienteFO(long rut, String clave, String estado) throws ClientesDAOException;

	/**
	 * 
	 * Ingresa una nueva dirección de despacho para el cliente.
	 * 
	 * @param direccion	DTO con datos de la dirección a ingresar
	 * 
	 * @throws ClientesDAOException
	 * 
	 */
	public long insertDireccionFO(DireccionesDTO direccion) throws ClientesDAOException;
	
	/**
	 * 
	 * Elimina una dirección de despacho para el cliente.
	 * 
	 * @param direccion_id	Identificador único de la dirección a eliminar
	 * 
	 * @throws ClientesDAOException
	 * 
	 */	
	public void deleteDireccionFO(long direccion_id) throws ClientesDAOException;
	
	/**
	 * 
	 * Modifica una dirección de despacho para el cliente.
	 * 
	 * @param direccion		DTO con datos de la dirección a ingresar
	 * 
	 * @throws ClientesDAOException
	 * 
	 */	
	public void updateDireccionFO(DireccionesDTO direccion) throws ClientesDAOException;
	
	/**
	 * 
	 * Retorna la lista de productos por categoría del carro de compras del cliente.
	 * 
	 * @param local			Identificador del local asociado a la dirección de despachos
	 * @param cliente_id	Identificador único del cliente
	 * @param orden			Forma de ordenamiento de los productos
	 * 
	 * @throws ClientesDAOException
	 * 
	 */	
	public List getCarroComprasCatProFO(String local, long cliente_id ) throws ClientesDAOException;
	
	/**
	 * Retorna los datos del cliente en una entidad.
	 * 
	 * @param cliente_id	Identificador único del cliente a consultar
	 * @return				ClienteEntity con información del cliente
	 * 
	 * @throws ClientesDAOException
	 */
	public ClienteEntity getClienteByIdFO(long cliente_id) throws ClientesDAOException;
	
	/**
	 * Retorna los datos del cliente en una entidad.
	 * 
	 * @param rut	RUT del cliente a consultar
	 * @return		ClienteEntity con información del cliente
	 * 
	 * @throws ClientesDAOException
	 */
	public ClienteEntity getClienteByRutFO(long rut) throws ClientesDAOException;
	
	/**
	 * Retorna la lista de tipos de calle del sistema. 
	 * 
	 * @return Lista de tipos de calle
	 *  
	 * @throws ClientesDAOException
	 */
	public List getTiposCalleFO() throws ClientesDAOException;
	
	/**
	 * 
	 * Retorna la lista de productos del carro de compras del cliente. 
	 * Si el local es -1 sólo recupera los productos para consultar por datos de productos sin precios. 
	 * 
	 * @param local			Identificador del local asociado a la dirección de despachos
	 * @param cliente_id	Identificador único del cliente
	 * @return 				Lista de DTO con datos del los productos
	 * 
	 * @throws ClientesDAOException
	 * 
	 */		
	public List getCarroCompraFO(long cliente_id, String local, String idSession ) throws ClientesDAOException;
	
	public List getCarroCompraVentaMasiva(long cliente_id, String local, String idSession ) throws ClientesDAOException;
	
	/**
	 * Ingresa un nuevo cliente.
	 * 
	 * @param cliente 		Identificador único del cliente
	 * @param despachos		Lista con direcciones de despacho del cliente
	 * 
	 * @throws ClientesDAOException
	 */
	public String newClienteFO(ClienteDTO cliente, DireccionesDTO direccion) throws ClientesDAOException;
	
	/**
	 * Retorna si tiene o no productos en el carro de compras.
	 * 
	 * @param cliente	Identificador único del cliente
	 * @return			True: existe, False: no existe
	 * 
	 * @throws ClientesDAOException
	 */
	public boolean clienteExisteCarroComprasFO(long cliente) throws ClientesDAOException;
	
	/**
	 * Actualiza la cantidad del producto del carro de compras del cliente.
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @param carro_id		Identificador único del registro del carro de compras
	 * @param cantidad		Valor para actualizar
	 * @param nota			Valor para actualizar
	 * @throws ClientesDAOException
	 */
	public void updateCarroCompraFO(long cliente_id, long carro_id, double cantidad, String nota, String idSession, String tipoSel) throws ClientesDAOException;
	
	/**
	 * Eliminar un producto desde el carro de compras del cliente.
	 * 
	 * @param cliente_id	Identificador único del cliento
	 * @param carro_id		Identificador único del registro del carro de compras
	 * @throws ClientesDAOException
	 */
	public void deleteCarroCompraFO(long cliente_id, long carro_id, String idSession ) throws ClientesDAOException;
	
	/**
	 * Recupera últimas compras del cliente. 
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @return				Lista de DTO
	 * @throws ClientesDAOException
	 */
	public List getUltimasComprasFO(long cliente_id)  throws ClientesDAOException;
	
	/**
	 * Recupera los productos de las listas de las últimas compras seleccionadas.
	 * 
	 * @param listas		Identificador de las listas
	 * @param local         Identificador único del local
	 * @param orden			Forma de ordenamiento de los productos
	 * @param cliente_id	Identificador único del cliente
	 * @return list			Lista de DTO
	 * @throws ClientesDAOException
	 */
	public List getUltimasComprasCatProFO(String listas, String local, long cliente_id, long rut ) throws ClientesDAOException;
	
	/**
	 * Inserta un producto al carro de compras.
	 * 
	 * @param listcarro		Lista de DTO
	 * @param cliente		Identificador único del cliente
	 * @throws ClientesDAOException
	 */
	public void insertCarroCompraFO(List listcarro, long cliente, String idSession) throws ClientesDAOException;
	
	/**
	 * Retorna las políticas de sustitución del sistema.
	 * 
	 * @return Lista de DTO
	 * @throws ClientesDAOException
	 */
	public List getPoliticaSustitucionFO() throws ClientesDAOException;
	
	/**
	 * Retorna las formas de pago del sistema.
	 * 
	 * @return Lista de DTO
	 * @throws ClientesDAOException
	 */
	public List getFormaPagoFO() throws ClientesDAOException;
	
	/**
	 * Elimina todos los productos del carro de compras.
	 * 
	 * @param cliente_id	Identificador único del cliente
	 * @throws ClientesDAOException
	 */
	public void deleteCarroCompraAllFO( long cliente_id, String ses_id ) throws ClientesDAOException;
	
	/**
	 * Retorna el Local y el Poligono para la comuna..
	 * 
	 * @param comuna	Identificador único de la comuna
	 * @return			Identificador único de la zona y el identificador único del local en formato id_zona-id_local
	 * @throws ClientesDAOException
	 */
	public String getLocalPoligonoFO( long comuna ) throws ClientesDAOException;
	
	/**
	 * Ingresa un registro en el tracking para el web.
	 * 
	 * @param seccion	Sección de la página
	 * @param rut		RUT del cliente que deja el registro
	 * @param arg0		Información del navegador y url
	 * @throws ClientesDAOException
	 */
	public void addTrakingFO(String seccion, Long rut, HashMap arg0) throws ClientesDAOException;
	
	/**
	 * Modifica la cantidad de intentos de login para un cliente.
	 * 
	 * @param cliente_id	Identificador del cliente.
	 * @param accion		1: Aumenta 0: Reset
	 * @throws ClientesDAOException
	 */
	public void updateIntentosFO(long cliente_id, long accion) throws ClientesDAOException;

}
