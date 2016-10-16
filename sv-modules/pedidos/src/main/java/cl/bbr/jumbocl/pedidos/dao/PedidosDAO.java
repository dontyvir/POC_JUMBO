package cl.bbr.jumbocl.pedidos.dao;

//import java.util.HashMap;
import java.util.HashMap;
import java.util.List;

import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.model.ClienteEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.ProductoSapEntity;
import cl.bbr.jumbocl.common.model.UsuariosEntity;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModFacturaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoPolSustDTO;
import cl.bbr.jumbocl.pedidos.dto.BinCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.CambEnPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.CriterioSustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.DatosMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DetalleRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.POSFeedbackProcPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.PickingCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.PoliticaSustitucionDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoPromoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PromoDetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RechPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.exceptions.ClientesDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.ProductosSapDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.UsuariosDAOException;
import cl.bbr.jumbocl.pedidos.promos.dto.CuponPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PedidoDatosSocketDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PrioridadPromosDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromoMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionesCriteriaDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.SafDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.TcpPedidoDTO;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Permite las operaciones en base de datos sobre los Pedidos.
 * 
 * @author BBR
 *
 */
public interface PedidosDAO { 

	/**
	 * Agrega registro al log del pedido
	 * 
	 * @param  log
	 * @throws PedidosDAOException
	 */
	public void addLogPedido( LogPedidoDTO log )throws PedidosDAOException;
	
	/**
	 * Obtiene listado del log de un pedido
	 * 
	 * @param  id_pedido
	 * @return List LogPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getLogPedido(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Obtiene el ultimo log de un pedido
	 * 
	 * @param  id_pedido
	 * @return LogPedidoDTO
	 * @throws PedidosDAOException
	 */
	public LogPedidoDTO getUltLogPedido(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Obtiene Listado de estados de un pedido 
	 * 
	 * @return List EstadoDTO
	 * @throws PedidosDAOException
	 */
	public List getEstadosPedido() throws PedidosDAOException;
	
	/**
	 * Obtiene Listado de motivos de un pedido
	 * 
	 * @return List MotivoDTO
	 * @throws PedidosDAOException
	 */
	public List getMotivosPedidoBOC() throws PedidosDAOException;
	
	/**
	 * Obtiene datos del pedido
	 * 
	 * @param  idPedido
	 * @return PedidoDTO
	 * @throws PedidosDAOException
	 */
	public PedidoDTO getPedidoById( long idPedido ) throws PedidosDAOException;
	
	/**
	 * Lista pedidos por criterio
	 * 
	 * @param  criterio
	 * @return List MonitorPedidosDTO
	 * @throws PedidosDAOException
	 */
	public List getPedidosByCriteria (PedidosCriteriaDTO criterio)throws PedidosDAOException;
	
	/**
	 * Retorna el número de registros de una consulta por criterio
	 * 
	 * @param  criterio
	 * @return long
	 * @throws PedidosDAOException
	 */
	public long getCountPedidosByCriteria(PedidosCriteriaDTO criterio)throws PedidosDAOException;
	
	/**
	 * Retorna el número de registros de una consulta por fecha
	 * 
	 * @param  criterio
	 * @return List MonitorPedidosDTO
	 * @throws PedidosDAOException
	 */
	public List getPedidosByFecha( PedidosCriteriaDTO criterio)throws PedidosDAOException;
	
	/**
	 * Retorna el número de registros de una consulta por fecha
	 * 
	 * @param  criterio
	 * @return long
	 * @throws PedidosDAOException
	 */
	public long getCountPedidosByFecha(PedidosCriteriaDTO criterio)throws PedidosDAOException;
	
	/**
	 * Obtiene listado de productos de un pedido
	 * 
	 * @param  criterio
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getProductosPedido(DetallePedidoCriteriaDTO criterio)throws PedidosDAOException;
	
	/**
	 * Entrega listado de bins de acuerdo a un criterio
	 * 
	 * @param  criterio
	 * @return List BinDTO
	 * @throws PedidosDAOException
	 */
	public List getBinsByCriteria(BinCriteriaDTO criterio)throws PedidosDAOException;
	
	/**
	 * Retorna la diferencia entre lo pickeado y lo pedido
	 * 
	 * @param  id_pedido
	 * @return List ProductosPedidoEntity
	 * @throws PedidosDAOException
	 */
	public List getFaltantesByPedidoId(long id_pedido)throws PedidosDAOException;
	
	/**
	 * Rescate de productos de un pedido especifico en base a un bin
	 * 
	 * @param  id_pedido
	 * @param  cod_bin
	 * @return List ProductosPedidoEntity
	 * @throws PedidosDAOException
	 */
	public List getProductosBin(long id_pedido, long cod_bin)throws PedidosDAOException;
	
	/**
	 * Obtiene listado de alertas de un pedido
	 * 
	 * @param  id_pedido
	 * @return List AlertaDTO
	 * @throws PedidosDAOException
	 */
	public List getAlertasPedido(long id_pedido) throws PedidosDAOException ;
	
	/**
	 * Obtiene listado de sustitutos de un pedido
	 * 
	 * @param  id_pedido
	 * @return List SustitutoDTO
	 * @throws PedidosDAOException
	 */
	public List getSustitutosByPedidoId(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Obtiene listado de productos pickeados de un pedido de acuerdo a un criterio
	 * 
	 * @param  criterio
	 * @return List ProductosBinDTO
	 * @throws PedidosDAOException
	 */
	public List getPickingPedidoByCriteria(PickingCriteriaDTO criterio) throws PedidosDAOException;
	
	/**
	 * Obtiene id del sector, segun producto y local.
	 * 
	 * @param  id_prod
	 * @return long
	 * @throws PedidosDAOException
	 */
	public long getSectorByProdId(long id_prod) throws PedidosDAOException;
	
	/**
	 * Agrega un producto al detalle de un pedido
	 * 
	 * @param  prod
	 * @return long
	 * @throws PedidosDAOException
	 */
	public long agregaProductoPedido(ProductosPedidoDTO prod) throws PedidosDAOException;
	
	/**
	 * Elimina el detalle de un pedido
	 * 
	 * @param  prod
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean elimProductoPedido(ProductosPedidoDTO prod) throws PedidosDAOException;
	
	/**
	 * Actualiza las jornadas de picking y de despacho al pedido
	 * 
	 * @param  id_pedido
	 * @param  id_jpicking
	 * @param  id_jdespacho
	 * @param  costo_despacho
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean doActualizaPedidoJornadas(long id_pedido, long id_jpicking, long id_jdespacho, int costo_despacho, boolean modificarJPicking, boolean modificarPrecio) throws PedidosDAOException;
	
	/**
	 * @param id_pedido
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean elimAlertaByPedido(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Borra las alertas de un pedido
	 * 
	 * @param  id_pedido
	 * @param  id_alerta
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean addAlertToPedido(long id_pedido, long id_alerta) throws PedidosDAOException;
	
	/**
	 * Modifica el estado de un pedido
	 * 
	 * @param  id_pedido
	 * @param  id_estado
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setModEstadoPedido(long id_pedido, long id_estado) throws PedidosDAOException;
	
	/**
	 * Verifica si un pedido tiene alerta activa
	 * 
	 * @param  id_pedido
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean getExisteAlertaActiva(long id_pedido)  throws PedidosDAOException;
	
	/**
	 * Actualiza la dirección de un pedido
	 * 
	 * @param  id_pedido
	 * @param  dir
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setModPedidoDir(DireccionesDTO dir, PedidoDTO pedido) throws PedidosDAOException;
	
	/**
	 * Elimina la alerta de un pedido
	 * 
	 * @param  id_pedido
	 * @param  id_alerta
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean elimAlertaPedidoById(long id_pedido,int id_alerta) throws PedidosDAOException;
	
	/**
	 * Modifica notas del pedido (indicación y observación)
	 * 
	 * @param  prm
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	/*public boolean PedidoIndic(ProcModPedidoIndicDTO prm) throws PedidosDAOException;*/
	
	/**
	 * Elimina y agrega los datos de una nueva factura.
	 * 
	 * @param  prm
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setModFactura(ProcModFacturaDTO prm) throws PedidosDAOException;
	
	/**
	 * Actualiza medio de pago de un  pedido
	 * 
	 * @param  mp
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setCambiarMedio_pago(DatosMedioPagoDTO mp) throws PedidosDAOException;
	
	//métodos replicados
	/**
	 * Obtiene datos del cliente
	 * 
	 * @param  id_cliente
	 * @return ClienteEntity
	 * @throws ClientesDAOException
	 */
	public ClienteEntity getClienteById(long id_cliente) throws ClientesDAOException;
	
	/**
	 * Obtiene lista de direcciones del cliente
	 * 
	 * @param  cliente_id
	 * @return List DireccionEntity
	 * @throws PedidosDAOException
	 */
	//public List listadoDirecciones(long cliente_id) throws PedidosDAOException;
	
	/**
	 * Obtiene datos de direccion
	 * 
	 * @param  dir_id
	 * @return DireccionEntity
	 * @throws PedidosDAOException
	 */
	public DireccionEntity getDireccionById(long dir_id) throws PedidosDAOException;
	
	/**
	 * Obtiene un producto del catálogo del FO
	 * 
	 * @param  id_prod_fo
	 * @return ProductoEntity
	 * @throws PedidosDAOException
	 */
	public ProductoEntity getProductoPedidoByIdProdFO(long id_prod_fo) throws PedidosDAOException;
	
	//boc
	/**
	 * Obtiene detalles del pedido
	 * 
	 * @param  id_pedido
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getDetallesPedido(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Recalcular el monto total del pedido
	 * 
	 * @param  id_pedido
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean recalcPedido(long id_pedido, String tipoDoc) throws PedidosDAOException;
	
	/**
	 * Obtiene precio de un producto segun el local.
	 * 
	 * @param  id_local
	 * @param  id_producto
	 * @return double
	 * @throws PedidosDAOException
	 */
	public double getPrecioByLocalProd(long id_local, long id_producto) throws PedidosDAOException;
	
	/**
	 * Actualiza dirección de un pedido
	 * 
	 * @param  id_pedido
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setDireccion(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Actualiza el sector en el detalle del pedido
	 * 
	 * @param id_detalle
	 * @param id_sector
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean actSectorDetalle(long id_detalle, long id_sector) throws PedidosDAOException;
	
	/**
	 * Obtiene los detalles de picking de un pedido
	 * 
	 * @param  id_pedido
	 * @return List DetallePickingEntity
	 * @throws PedidosDAOException
	 */
	public HashMap getDetPickingByIdPedido(long id_pedido) throws PedidosDAOException;
	//public List getDetPickingByIdPedido(long id_pedido) throws PedidosDAOException;
	/**
	 * Obtiene las facturas de un pedido
	 * 
	 * @param  id_pedido
	 * @return List FacturaEntity
	 * @throws PedidosDAOException
	 */
	public List getFacturasByIdPedido(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Agrega detalle de picking. 
	 * 
	 * @param  dto
	 * @return long
	 * @throws PedidosDAOException
	 */
	public long addDetallePicking(DetallePickingDTO dto) throws PedidosDAOException;
	
	/**
	 * Obtiene la cantidad de zonas, segun la comuna.
	 * 
	 * @param  id_comuna
	 * @return int
	 * @throws PedidosDAOException
	 */
	/*public int getLocalesByIdComuna(long id_comuna) throws PedidosDAOException;*/
	
	//calcula el nro maximo de registros
	/**
	 * Obtiene la cantidad de productos de un pedido, segun la lógica de unidad de medida.
	 * 
	 * @param  id_pedido
	 * @return int
	 * @throws PedidosDAOException
	 */
	public int  valMaxNumProductosByPedido(long id_pedido) throws PedidosDAOException;
	
	//metodos que provienen del JdbcProductosSapDAO
	/**
	 * Obtiene datos del producto sap.
	 * 
	 * @param  id_prod
	 * @return ProductoSapEntity
	 * @throws ProductosSapDAOException
	 */
	public ProductoSapEntity getProductSapById(long id_prod) throws ProductosSapDAOException;
	
	/**
	 * Obtiene los precios de un producto 
	 * 
	 * @param  id_prod
	 * @return List PrecioSapEntity 
	 * @throws ProductosSapDAOException
	 */
	public List getPreciosByProdId(long id_prod) throws ProductosSapDAOException;
	
	/**
	 * Obtiene los códigos de barra de un producto
	 * 
	 * @param  id_prod
	 * @return List CodBarraSapEntity 
	 * @throws ProductosSapDAOException
	 */
	public List getCodBarrasByProdId(long id_prod) throws ProductosSapDAOException;
	

	//metodos para el TP- descarga de ronda
	/**
	 * Agrega bins que pertenecen a una ronda. 
	 * 
	 * @param  lst_bin
	 * @param  id_ronda
	 * @return List TPBinOpDTO
	 * @throws PedidosDAOException
	 */
	public List addBinsPedidoPOS(List lst_bin, long id_ronda) throws PedidosDAOException;
	
	/**
	 * Obtiene el detalle de pedido, segun id del detalle. 
	 * 
	 * @param  id_detalle
	 * @return ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public ProductosPedidoDTO getDetallePedidoById(long id_detalle) throws PedidosDAOException;
	
	/**
	 * Obtiene el producto sustituto, segun código de barra y id del local 
	 * 
	 * @param  cbarra
	 * @param  id_local
	 * @return ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public ProductosPedidoDTO getDetSustituto(String cbarra,long id_local) throws PedidosDAOException;
	
	/**
	 * Actualiza cantidades en el detalle de la ronda 
	 * 
	 * @param  detRndDto
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean actDetalleRonda(DetalleRondaDTO detRndDto) throws PedidosDAOException;
	
	/**
	 * Actualiza cantidades en el detalle de pedido 
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean actCantDetallePedido(ProductosPedidoDTO dto) throws PedidosDAOException;
	
	/**
	 * Actualiza datos de un pedido que ha sido pickeado 
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean verificaPedidoPickeado(PedidoDTO dto) throws PedidosDAOException;
	
	/**
	 * Obtiene cantidad de productos sin pickear de un pedido 
	 * 
	 * @param  id_pedido
	 * @return double
	 * @throws PedidosDAOException
	 */
	public double getCantSinPickearByPedido(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Obtiene id_detalle de pedido, segun id de detalle de ronda.
	 * 
	 * @param  id_dronda
	 * @return long
	 * @throws PedidosDAOException
	 */
	public long getIdDetPedidoByIdDRonda(long id_dronda) throws PedidosDAOException;
	
	/**
	 * Verifica si existe la ronda
	 * 
	 * @param  id_ronda
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean isRondaById(long id_ronda) throws PedidosDAOException;
	
	/**
	 * Verifica si existe relación entre el pedido y la ronda
	 * 
	 * @param id_pedido
	 * @param id_ronda
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean isPedidoRelRondaByIds(long id_pedido, long id_ronda) throws PedidosDAOException;
	
	/**
	 * Obtener los datos del perfil.
	 * 
	 * @param  id_perfil
	 * @return PerfilesEntity
	 * @throws PedidosDAOException
	 */
	public PerfilesEntity getPerfilById(int id_perfil) throws PedidosDAOException; 
	
	// metodos para pago POS
	/**
	 * Procesa transacción de pago Exitosa en POS
	 * 
	 * @param  fback
	 * @throws PedidosDAOException
	 */
	public void doProcesaPagoOK(POSFeedbackProcPagoDTO fback) throws PedidosDAOException;
	
	/**
	 * Obtiene datos del usuario. 
	 * 
	 * @param  id_usuario
	 * @return UsuariosEntity
	 * @throws UsuariosDAOException
	 */
	public UsuariosEntity getUsuarioById(long id_usuario) throws UsuariosDAOException;
	
	// Métodos para insertar un pedido
	/**
	 * Inserta Encabezado de Pedido
	 * 
	 * @param  pedido
	 * @return long
	 * @throws PedidosDAOException
	 */
	public long doInsEncPedido(PedidoDTO pedido) throws PedidosDAOException;
	
	/**
	 * Retorna el id_pedido del último pedido cursado por un cliente. Si no encuentra
	 * ningún pedido, entonces retorna 0
	 * 
	 * @param  id_cliente
	 * @return long
	 * @throws PedidosDAOException
	 */
	public long getUltimoIdPedidoCliente(long id_cliente)  throws PedidosDAOException;


	//otros
	/**
	 * Actualiza datos del cliente
	 * 
	 * @param cliente
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean actClienteById(ClienteEntity cliente) throws PedidosDAOException;
	
	/**
	 * Obtiene los productos del pedido por sector
	 * 
	 * @param  id_pedido
	 * @param  id_sector
	 * @param  id_local
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getProdPedidoXSector(long id_pedido, long id_sector, long id_local) throws PedidosDAOException;
	
	
	/**
	 * Obtiene el listado de los productos sueltos de un pedido, q se encuentran contenidos en el bin de tipo Virtual
	 * 
	 * @param  id_pedido
	 * @return List ProductosPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getProductosSueltosByPedidoId(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Entrega un Listado de Todas las Secciones (nivel 2)
	 * 
	 * @return List CategoriaSapEntity
	 * @throws PedidosDAOException
	 */
	public List getSeccionesSap() throws PedidosDAOException;
	
	/**
	 * Actualiza las cantidades del detalle de pedido
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean actCantSinPickearDetallePedido(ProductosPedidoDTO dto) throws PedidosDAOException;
	
	//actualizar informacion de detalle de picking que fue sustituido y no tiene información
	/**
	 * Actualiza el detalle de picking de un producto sustituto. 
	 * 
	 * @param dto
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setDetallePickingSustituto(DetallePickingDTO dto) throws PedidosDAOException;
	
	/**
	 * Obtiene listado de sectores, segun producto y local 
	 * 
	 * @param id_producto
	 * @return List SectorLocalDTO
	 * @throws PedidosDAOException
	 */
	public long getProdXSector(long id_producto) throws PedidosDAOException ;
	
	/**
	 * Elimina la relación entre producto y sector 
	 * 
	 * @param  id_producto
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setDelProdXSector(long id_producto) throws PedidosDAOException ;
	
	/**
	 * Agrega relación de producto y sector.
	 * 
	 * @param  id_producto
	 * @param  id_sector
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setAddProductoXSector(long id_producto, long id_sector) throws PedidosDAOException ;
	
	/**
	 * Verifica si existe id_sector
	 * 
	 * @param  id_sector
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean isSectorById(long id_sector) throws PedidosDAOException;
	
	/**
	 * Nulifica los sectores de picking para todo el detalle de un pedido
	 * 
	 * @param  id_pedido long 
	 * @return boolean, devuelve <i>true</i> si la actualización fue exitosa, caso contrario devuelve <i>false</i>. 
	 * @throws PedidosDAOException
	 * 
	 */
	public boolean setNullSectoresDetallePedido(long id_pedido)	throws PedidosDAOException;
	
	/**
	 * Permite eliminar la relacion entre el pedido y la jornada de picking
	 * 
	 * @param id_pedido
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean elimJPickByPedido(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Cambia el estado de OP a pago rechazado
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setRechazaPagoOP(RechPagoOPDTO dto) throws PedidosDAOException;
	
	/**
	 * Obtiene el listado de politicas de sustitucion
	 * 
	 * @return List PoliticaSustitucionDTO
	 * @throws PedidosDAOException
	 */
	public List getPolitSustitucionAll() throws PedidosDAOException;
	
	/**
	 * Obtiene los datos de la politica de sustitucion, segun el codigo
	 * 
	 * @param  id_pol_sust
	 * @return PoliticaSustitucionDTO
	 * @throws PedidosDAOException
	 */
	public PoliticaSustitucionDTO getPolSustitById(long id_pol_sust) throws PedidosDAOException;
	
	/**
	 * Modifica la politica de sustitucion de un pedido
	 * 
	 * @param dto
	 * @return boolean
	 * @throws PedidosDAOException 
	 */
	public boolean setModPedidoPolSust(ProcModPedidoPolSustDTO dto) throws PedidosDAOException;
	
	/**
	 * Cambia el estado del pedido a 'En Pago'
	 * 
	 * @param  dto
	 * @return boolean
	 * @throws PedidosDAOException
	 */
	public boolean setCambiarEnPagoOP(CambEnPagoOPDTO dto) throws PedidosDAOException;

	/**
	 * Lista de productos que se enviarán al cliente. Incluye sustitutos.
	 * 
	 * @param  id_pedido
	 * @return List
	 * @throws PedidosDAOException
	 */
	public List productosEnviadosPedidoForEmail(long id_pedido) throws PedidosDAOException;

	/**
	 * Lista de productos sustitutos que se enviarán al cliente, 
	 * estos sustitutos corresponden a cambios en el Código SAP
	 * 
	 * @param  id_pedido
	 * @return List
	 * @throws PedidosDAOException
	 */
	public List productosSustitutosPedidoForEmail(long id_pedido) throws PedidosDAOException;

	/**
	 * Genera el pedido desde una cotizacion
	 * 
	 * @param pedido
	 * @return id del nuevo pedido
	 * @throws PedidosDAOException
	 */
	public long doGeneraPedido(PedidoDTO pedido) throws PedidosDAOException;
	
	/**
	 * Genera el detalle del pedido, desde una cotizacion
	 * 
	 * @param prod
	 */
	public boolean generaProductoPedido(ProductosPedidoDTO prod) throws PedidosDAOException;
	
	/**
	 * Actualiza el estado de una cotizacion
	 * 
	 * @param id_cotizacion
	 * @param estado
	 * @return true, si se actualizo con exito, false en caso contrario
	 * @throws PedidosDAOException
	 */
	public boolean setCambiarCotizacion(long id_cotizacion, int estado) throws PedidosDAOException;
	
	/**
	 * Obtiene el ultimo monto de cargo de una empresa y un pedido
	 * 
	 * @param dto
	 * @return monto de cargo
	 * @throws PedidosDAOException
	 */
	public double getUltimoCargoByEmpresaId(PedidoDTO dto) throws PedidosDAOException;
	
	/**
	 * Obtiene informacion de la empresa, segun id de la empresa
	 * 
	 * @param id_empresa
	 * @return EmpresasEntity
	 * @throws PedidosDAOException
	 */
	public EmpresasEntity getEmpresaById(long id_empresa)  throws PedidosDAOException;
	
	
	
	/**
	 * Aplica el prorrateo. Cambia el precio al detalle de picking correspondiente.
	 * @param id_detpick
	 * @param precio
	 * @return
	 * @throws PedidosDAOException
	 */
	public boolean setAplicaProrrateo(long id_detpick, long precio) throws PedidosDAOException;
	
	/**
	 * Agrega promoción al detalle de pedido.
	 * @param dto
	 * @return
	 * @throws PedidosDAOException
	 */
	public boolean addPromoDetallePedido(PromoDetallePedidoDTO dto) throws PedidosDAOException;
	
	
	/**
	 * Obtiene un listado con las promociones que estan asignadas a un producto de un local especificado.
	 * @param id_producto
	 * @param id_local
	 * @return
	 * @throws PedidosDAOException
	 */
	public ProductoPromoDTO getProductoPromos(long id_producto, long id_local) throws PedidosDAOException;
	
	
	/**
	 * Permite eliminar las promociones de un detalle de pedido
	 * @param id_pedido
	 * @return
	 * @throws PedidosDAOException
	 */
	public boolean doEliminaPromoDPPedido(long id_pedido) throws PedidosDAOException;
	
	
	/**
	 * Permite modificar el descuento de un detalle pedido segun su id.
	 * @param dp DetallePedidoDTO
	 * @return
	 * @throws PedidosDAOException
	 */
	public boolean doModificaDescuentoDetallePedido(DetallePedidoDTO dp) throws PedidosDAOException;
	
	
	/************* PROMOCIONES *********************************/
	
	/**
	 * Obtiene el listado de cupones de un pedido
	 * @param id_pedido
	 * @return List CuponPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getCuponesPedidoByIdPedido(long id_pedido)throws PedidosDAOException;

	/**
	 * Obtiene el listado de TCPs del pedido
	 * @param id_pedido
	 * @return List TcpPedidoDTO
	 * @throws PedidosDAOException
	 */
	public List getTcpPedidosByIdPedido(long id_pedido) throws PedidosDAOException;	

	/**
	 * Obtiene TODOS los productos del pedido con el codigo de barras asociados que tenga el malor MAXIMO
	 * entre el conjunto de codigos de barras que tenga asociado el producto
	 * (un producto jumbo.cl puede tener más de un código de barras)
	 * @param id_pedido
	 * @return List PedidoPromocionDTO
	 * @throws PromocionesDAOException
	 */
	public List getProductosByPedido(long id_pedido) throws PedidosDAOException;

	/**
	 * Trae los códigos de promociones tipo evento , periodica , normal de un producto en un local
	 * @param id_producto
	 * @param id_local
	 * @return PrioridadPromosDTO
	 * @throws PedidosDAOException
	 */
	public PrioridadPromosDTO getPromosPrioridadProducto(long id_producto, long id_local) throws PedidosDAOException;

	/**
	 * Actualiza la cantidad utilizada del TCP
	 * @param id_pedido
	 * @param nro_tcp
	 * @param cant_util
	 * @return num reg actualizados 
	 * @throws PedidosDAOException
	 */
	public int updTcpCantUtil(long id_pedido, long nro_tcp, long cant_util) throws PedidosDAOException;
	

	/**
	 * Obtiene los productos CON promoción de un pedido especifico
	 * @param id_pedido
	 * @return List PedidoPromocionDTO
	 * @throws PedidosDAOException
	 */
	public List getResumenPromocionPedidos(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Obtiene los productos que estan en la promocion de un pedido en particular.
	 * @param id_promocion
	 * @param id_pedido
	 * @return
	 * @throws PedidosDAOException
	 */
	public List getProductosByPromocionPedido(long id_promocion, long id_pedido) throws PedidosDAOException;
		
	

	/**
	 * Entrega un listado de promociones utilizando un criterio de busqueda opcional
	 * dado por un id_promocion o un id_local
	 * @param criteria
	 * @return List MonitorPromocionesDTO
	 * @throws PedidosDAOException
	 */
	public List getPromocionesByCriteria(PromocionesCriteriaDTO criteria) throws PedidosDAOException;
	
	/**
	 * Entrega el numero de registros del  listado de promociones utilizando un criterio de busqueda opcional
	 * dado por un id_promocion o un id_local
	 * @param criteria
	 * @return total registros
	 * @throws PedidosDAOException
	 */
	public long getPromocionesByCriteriaCount(PromocionesCriteriaDTO criteria) throws PedidosDAOException;
	
	/**
	 * Obtiene los datos de una promoción específica por su ID
	 * @param id_promocion
	 * @return PromocionDTO
	 * @throws PromocionesDAOException
	 */
	public PromocionDTO getPromocionById(long id_promocion) throws PedidosDAOException;
		
	/**
	 * Obtiene los datos de una promoción específica por el COD_PROMO
	 * @param cod_promo
	 * @return
	 * @throws PedidosDAOException
	 */
	public PromocionDTO getPromocionByCodigo(long cod_promo) throws PedidosDAOException;
	
	/**
	 * Obtiene un listado de Categorias SAP asociados al tipo de promocion seccion y
	 * al local de la promocion. 
	 * @param id_local
	 * @param tipo
	 * @return List CategoriaSapDTO
	 * @throws PedidosDAOException
	 */
	public List getCategoriasSapByPromocionSeccion(long id_local, int tipo) throws PedidosDAOException;
	
	/**
	 * Obtiene los productos x locales de cada promocion
	 * @param codigo
	 * @return
	 * @throws PedidosDAOException
	 */
	public List getPromocionProductosByTipo(int codigo, String tipo) throws PedidosDAOException;
	
	/**
	 * Permite modificar una promoción
	 * @param dto
	 * @return
	 * @throws PedidosDAOException
	 */
	public boolean updPromocion(PromocionDTO dto) throws PedidosDAOException;
	
	
	/**
	 * Obtiene un TCP con el id_pedido y el id_tcp
	 * @param id_pedido
	 * @param id_tcp
	 * @return
	 * @throws PedidosDAOException
	 */
	public TcpPedidoDTO getTcpPedidosByIdPedidoIdTcp(long id_pedido, long id_tcp)throws PedidosDAOException;
		
	/**
	 * Inserta un nuevo TCP al pedido, devuelve el id_tcp generado
	 * @param tcp
	 * @return id_tcp
	 * @throws PedidosDAOException
	 */
	public int setTcpPedido(TcpPedidoDTO tcp)throws PedidosDAOException;
	
	
	/**
	 * Elimina todos los tcp del pedido
	 * @param id_pedido
	 * @throws PedidosDAOException
	 */
	public void delTcpPedido(long id_pedido) throws PedidosDAOException;

	
	/**
	 * Obtiene un cupon con el id_cupon
	 * @param id_cupon
	 * @return CuponPedidoDTO
	 * @throws PedidosDAOException
	 */
	public CuponPedidoDTO getCuponesPedidoByIdCupon(long id_cupon)throws PedidosDAOException;
	
	/**
	 * Inserta un cupon al pedido
	 * @param cupon
	 * @return
	 * @throws PedidosDAOException
	 */
	public int setCuponPedido(CuponPedidoDTO cupon)throws PedidosDAOException;
		
	/**
	 * Elimina todos los cupones de un pedido
	 * @param id_pedido
	 * @throws PedidosDAOException
	 */
	public void delCuponPedido(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Obtiene datos del pedido para los clientes soccket
	 * @param id_pedido
	 * @return
	 * @throws PedidosDAOException
	 */
	public PedidoDatosSocketDTO getPedidoDatos(long id_pedido) throws PedidosDAOException;
	
	/**
	 * inserta trx SAF y devuelve el id_saf generado
	 * @param dto
	 * @return
	 * @throws PedidosDAOException
	 */
	public int insSaf(SafDTO dto) throws PedidosDAOException;
	
	
	/**
	 * Actualiza el registro SAF utilizando el id_saf como busqueda
	 * @param dto
	 * @return
	 * @throws PedidosDAOException
	 */
	public void updSaf(SafDTO dto) throws PedidosDAOException;
	
	
	/**
	 * Devuelve un listado de SafDTO con los saf encontrados por id_saf
	 * @param  id_saf
	 * @return List SafDTO
	 * @throws PedidosDAOException
	 */
	public SafDTO getSafById(long id_saf) throws PedidosDAOException;
	
	/**
	 * Devuelve el listado de SafDTO de los registros saf con el estado buscado
	 * @param estado
	 * @return
	 * @throws PedidosDAOException
	 */
	public List getSafByEstado(String estado) throws PedidosDAOException;
	
	/**
	 * Obtiene los datos de un tcp segun id pedido y numero de tcp
	 * @param id_pedido
	 * @param nro_tcp
	 * @return
	 * @throws PedidosDAOException
	 */
	public TcpPedidoDTO getTcpPedidosByIdPedidoNroTcp(long id_pedido, int nro_tcp) throws PedidosDAOException;
	
	/**
	 * Obtiene los datos de los medios de pago para promociones segun el medio pago jumbocl
	 * @param jm_mp
	 * @return
	 * @throws PedidosDAOException
	 */
	public PromoMedioPagoDTO getPromoMedioPagoByMPJmcl(String jm_mp, int jm_mp_cuotas) throws PedidosDAOException;
	
	/**
	 * Actualiza flags de recalculo de la promocion para el pedido
	 * @param dto PedidoDTO
	 * @throws PedidosDAOException
	 */
	public void updFlagRecalculoPedido(PedidoDTO dto) throws PedidosDAOException;
	
	/**
	 * Verifica la hora de compra de una OP
	 * @param dto PedidoDTO
	 * @throws PedidosDAOException
	 */
	public boolean VerificaHoraCompra(long id_jdespacho, String TipoDespacho) throws PedidosDAOException;
	
	
	/**
	 * Obtiene total de detalle de picking de un pedido
	 * @param id_pedido
	 * @return
	 * @throws PedidosDAOException
	 */
	public double getTotalDetPickingByOP(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Obtener listado de op para ser enviadas a WebPay
	 * @return
	 * @throws PedidosDAOException
	 */
	public HashMap obtenerLstOPByTBK() throws PedidosDAOException;
	
	/**
	 * Activa el flag de captura del pedido
	 * @param id_pedido
	 * @return
	 * @throws PedidosDAOException
	 */
	public boolean setActivarFlagCaptura(long id_pedido) throws PedidosDAOException;
	
    /** Entregala informacion de un pedido para enviar por boton de pago
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetPedido(long idPedido) throws PedidosDAOException;
	
    /**
     * Inserta un registro del resultado del pago con Boton de Pago CAT
     * 
     * @param BotonPagoDTO
     */
    public void botonPagoSave(BotonPagoDTO bp) throws PedidosDAOException;

    /** Entregala informacion de un registro de boton de pago segun el id del pedido
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetByPedido(long idPedido) throws PedidosDAOException;
    

	/**
	 * Obtener datos del webpay
	 * 
	 * @param id_pedido
	 * @return
	 * @throws PedidosDAOException
	 */
	public WebpayDTO getWebpayByOP(long id_pedido) throws PedidosDAOException;
	
	/**
	 * Obtener codigo de autorizacion de pago segun OP
	 * @param id_pedido
	 * @return
	 * @throws PedidosDAOException
	 */
	public String getCodAutorizByOP(long id_pedido) throws PedidosDAOException;

	/**
	 * Actualiza registro de Boton de pago CAT
	 */
	public boolean updateNotificacionBotonPago(BotonPagoDTO bp) throws PedidosDAOException;

    /**
     * @param trx
     * @throws PedidosDAOException
     */
    public void setTrx(JdbcTransaccion trx) throws PedidosDAOException;

    /**
     * @param idLocal
     * @return
     * @throws PedidosDAOException
     */
    public LocalDTO getLocalRetiro(long idLocal) throws PedidosDAOException;

    /**
     * @param id_cliente
     * @return
     * @throws PedidosDAOException
     */
    public boolean esPrimeraCompra(long id_cliente) throws PedidosDAOException;

    /**
     * @param idLocal
     * @return
     * @throws PedidosDAOException
     */
    public LocalDTO getLocalById(long idLocal) throws PedidosDAOException;

    /**
     * @param id_cliente
     * @param id_producto_fo
     * @return
     * @throws PedidosDAOException
     */
    public CriterioSustitutoDTO getCriterioByClienteAndProducto(long id_cliente, long id_producto_fo) throws PedidosDAOException;
    
    //DESDE FO
    
    /**
	 * Ingresa una nueva compra histórica WEB.
	 * 
	 * @param nombre		Nombre de la compra histórica
	 * @param id_cliente	Identificador único del cliente
	 * @param unidades		Cantidad de productos de la compra
	 * @param pedido_id		Identificador único del pedido
	 * @throws PedidosDAOException
	 */
	public void setCompraHistorica(String nombre, long id_cliente, long unidades, long pedido_id) throws PedidosDAOException;
	
	/**
	 * Actualiza datos de una compra histórica WEB.
	 * 
	 * @param nombre		Nombre de la compra histórica
	 * @param id_cliente	Identificador único del cliente
	 * @param unidades		Cantidad de productos de la compra
	 * @param pedido_id		Identificador único del pedido
	 * @param id_lista		Identificador único de la compra histórica a modificar
	 * @throws PedidosDAOException
	 */
	public void updateCompraHistorica(String nombre, long id_cliente, long unidades, long pedido_id, long id_lista) throws PedidosDAOException;
	
	/**
	 * Actualiza el ranking de ventas de los productos de un pedido 
	 * 
	 * @param id_cliente	id de cliente al cual se le actualizan el ranking
	 * @throws PedidosDAOException
	 */
	public void updateRankingVentas( long id_cliente ) throws PedidosDAOException;
	
	/**
	 * Actualiza datos de una compra histórica WEB.
	 * 
	 * @param nombre		Nombre de la compra histórica
	 * @param id_cliente	Identificador único del cliente
	 * @throws PedidosDAOException
	 */
	public void updateNombreCompraHistorica( String nombre, long id_cliente ) throws PedidosDAOException;

	
	public void updateDatosPedidoInvitado( int idpedido,long rut , String dv, String nombre, String apellido ) throws PedidosDAOException;
	
	
}
