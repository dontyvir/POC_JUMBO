package cl.bbr.fo.bizdelegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cl.bbr.cupondscto.dto.CarroAbandonadoDTO;
import cl.bbr.cupondscto.service.CuponDsctoService;
import cl.bbr.fo.exception.FuncionalException;
import cl.bbr.fo.exception.ServiceException;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.fo.fonocompras.dto.UsuarioDTO;
import cl.bbr.fo.marketing.dto.MarketingElementoDTO;
import cl.bbr.fo.service.CategoriasService;
import cl.bbr.fo.service.FaqService;
import cl.bbr.fo.service.FonoComprasService;
import cl.bbr.fo.service.MarketingService;
import cl.bbr.jumbocl.bolsas.service.BolsasService;
import cl.bbr.jumbocl.clientes.ctrl.ClientesCtrl;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;
import cl.bbr.jumbocl.clientes.dto.CriterioClienteSustitutoDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasCategoriasDTO;
import cl.bbr.jumbocl.clientes.exceptions.ClientesException;
import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.clientes.service.RegionesService;
import cl.bbr.jumbocl.common.exceptions.DuplicateKeyException;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.service.EventosService;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;
import cl.bbr.jumbocl.pedidos.ctrl.PedidosCtrl;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.DireccionMixDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoGrabilityDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaUnidadDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosException;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.jumbocl.productos.service.ProductosService;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.ClienteKccDTO;
import cl.bbr.promo.lib.dto.ClientePRDTO;
import cl.bbr.promo.lib.dto.ClienteSG6DTO;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.ProductoCatalogoExternoDTO;
import cl.bbr.promo.lib.dto.ProductoStockDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;
import cl.bbr.promo.lib.exception.PromocionesException;
import cl.bbr.promo.lib.service.PromocionesService;
import cl.jumbo.common.dto.CategoriaBannerDTO;
import cl.jumbo.common.dto.CategoriaMasvDTO;
//Catalogo Externo - NSepulveda
//Catalogo Externo - NSepulveda

/**
 * <p>
 * BizDelegate para Front Office.
 * </p>
 * <p>
 * Es la capa responsable de disponibilizar los servicios de la capa de negocios
 * y datos del sistema.
 * </p>
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class BizDelegate {

    /**
     * Instancia para log
     */
    Logging logger = new Logging(this);

    /**
     * Instancia para el service de cliente.
     */
    private static ClientesService cli_service = null;

    /**
     * Instancia para el service de regiones.
     */
    private static RegionesService reg_service = null;

    /**
     * Instancia para el service de pedidos.
     */
    private static PedidosService ped_service = null;

    /**
     * Instancia para el service de productos.
     */
    private static ProductosService pro_service = null;
    
    private static CategoriasService cat_service = null;

    /**
     * Instancia para el service de fonocompras.
     */
    private static FonoComprasService fono_service = null;

    /**
     * Instancia para el service de FAQ.
     */
    private static FaqService faq_service = null;

    /**
     * Instancia para el service de marketing.
     */
    private static MarketingService marketing_service = null;

    /**
     * Instancia para el service de promociones.
     */
    private static PromocionesService promociones_service = null;

    /**
     * Instancia para el service de pedidos BO.
     */
    private static cl.bbr.jumbocl.pedidos.service.PedidosService bolped_service = null;

    /**
     * Instancia para el service de eventos personalizados.
     */
    private static EventosService eventoService = null;
    
//  14112012 VMatheu 
    /**
     * Instancia para el service de eventos personalizados.
     */
    private static ParametrosService parametroService= null;
// -- 14112012 VMatheu 
    
    /**
     * Instancia para el service de Clientes (proyecto Clientes).
     */
    private static cl.bbr.jumbocl.clientes.service.ClientesService clientesService = null;
    
    
    private static BolsasService bolsasService = null;
    
    private static CuponDsctoService cuponDsctoService = null;

    /**
     * Constructor, se instancian los servicios.
     */
    public BizDelegate() {
        if (cli_service == null)
            cli_service = new ClientesService();
        if (reg_service == null)
            reg_service = new RegionesService();
        if (ped_service == null)
            ped_service = new PedidosService();
        if (pro_service == null)
            pro_service = new ProductosService();
        if (cat_service == null)
           cat_service = new CategoriasService();
        if (fono_service == null)
            fono_service = new FonoComprasService();
        if (faq_service == null)
            faq_service = new FaqService();
        if (marketing_service == null)
            marketing_service = new MarketingService();
        if (promociones_service == null)
            promociones_service = new PromocionesService();
        if (bolped_service == null)
            bolped_service = new cl.bbr.jumbocl.pedidos.service.PedidosService();
        if (eventoService == null)
            eventoService = new EventosService();
        if (clientesService == null)
            clientesService = new cl.bbr.jumbocl.clientes.service.ClientesService();
        if (bolsasService == null)
        	bolsasService = new BolsasService();
//14112012 VMatheu 
        if(parametroService == null )
            parametroService = new ParametrosService();
//-14112012 VMatheu 
        if(cuponDsctoService == null)
        	cuponDsctoService = new CuponDsctoService();        	
    }

    /**
     * Direcciones de despacho para un cliente.
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @throws SystemException
     *  
     */
    public List clientegetAllDirecciones(long cliente_id) throws SystemException {
        try {
            return cli_service.clienteGetAllDireccionesFO(cliente_id);
        } catch (Exception ex) {
            logger.error("Problemas con controles de clientes", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera los datos del cliente
     * 
     * @param rut
     *            RUT del cliente a consultar
     * @return Cliente DTO
     * @throws SystemException
     * @see ClienteDTO
     */
    public ClienteDTO clienteGetByRut(long rut) throws SystemException {

        try {
            return cli_service.clienteGetByRutFO(rut);
        } catch (Exception ex) {
            logger.error("Problemas con controles de clientes", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Datos del cliente
     * 
     * @param cliente_id
     *            Identificador único del cliente a consultar
     * @return Lista de DTO
     * @throws SystemException
     * @see ClienteDTO
     */
    public ClienteDTO clienteGetById(long cliente_id) throws SystemException {

    		try {
				return cli_service.clienteGetByIdFO(cliente_id);
			} catch (Exception e) {
				logger.error("Problemas con controles de clientes", e);
	    		throw new SystemException(e);
			}
    	/*} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
    		logger.error("Problemas con controles de clientes", e);
    		throw new SystemException(e);
    	}*/

    }

    /**
     * Desinscribe mail cliente
     * 
     * @param cliente
     *            ClinteDTO
     * @return boolean se realizo el update o no
     * @throws SystemException
     * @see ClienteDTO
     */
    public boolean clienteDesinscribeMail(ClienteDTO cliente) throws SystemException {

        try {
            return cli_service.clienteDesinscribeMailFO(cliente);
        } catch (Exception ex) {
            logger.error("Problemas con controles de clientes", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera las últimas compras del cliente.
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @return Lista de UltimasComprasDTO
     * @throws SystemException
     */
    public List clienteGetUltCompras(long cliente_id) throws SystemException {
        List result = new ArrayList();

        try {

            result = cli_service.ultComprasGetListFO(cliente_id);

        } catch (Exception ex) {
            logger.error("Problemas con ultimas compras", ex);
            throw new SystemException(ex);
        }

        return result;

    }

    /**
     * Listado de productos de las últimas compras.
     * 
     * @param listas
     *            Identificadores únicos de las últimas compras a consultar
     * @param local
     *            Identificador único del local
     * @param orden
     *            Orden de despligue de los productos
     * @return Lista de UltimasComprasCategoriasDTO
     * @throws SystemException
     * @see UltimasComprasCategoriasDTO
     */
    public List clientesGetUltComprasCategoriasProductos(String listas, String local, long cliente_id, long rut)
            throws SystemException {
        try {
            return cli_service.ultComprasProductosGetListFO(listas, local, cliente_id, rut);
        } catch (Exception ex) {
            logger.error("Problemas con la lista de ultimas productos Listas: " + listas + " Local: " + local, ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Listado de tipos de calle activos
     * 
     * @return Listado con tipos de calles DTO
     * @throws SystemException
     */
    public List tiposCalleGetAll() throws SystemException {
        List result = null;

        try {

            result = cli_service.tiposCallesGetListFO();

        } catch (Exception ex) {
            logger.error("Problemas con servicio de clientes", ex);
            throw new SystemException(ex);
        }

        return result;

    }

    /**
     * Listado de regiones
     * 
     * @return Lista con RegionesDTO
     * @throws SystemException
     */
    public List regionesGetAll() throws SystemException {
        List result = null;

        try {

            result = reg_service.getAllRegiones();

        } catch (Exception ex) {
            logger.error("Problemas con servicios de regiones", ex);
            throw new SystemException(ex);
        }

        return result;

    }

    /**
     * Region de una comuna
     * 
     * @param id_comuna
     *            identificador único de la comuna
     * @return Lista con RegionesDTO
     * @throws SystemException
     */
    public RegionesDTO regionesGetRegion(long id_comuna) throws SystemException {
        RegionesDTO region = null;

        try {

            region = reg_service.getRegion(id_comuna);

        } catch (Exception ex) {
            logger.error("Problemas con servicios de regiones", ex);
            throw new SystemException(ex);
        }

        return region;

    }

    /**
     * Listado de comunas
     * 
     * @param reg_id
     *            Identificador único de la región
     * @return Lista de ComunasDTO
     * @throws SystemException
     */
    public List regionesGetAllComunas(long reg_id) throws SystemException {
        List result = null;

        try {

            result = reg_service.getAllComunas(reg_id);

        } catch (Exception ex) {
            logger.error("Problemas con servicios de regiones", ex);
            throw new SystemException(ex);
        }

        return result;

    }

    /**
     * Inserta un cliente en el repositorio
     * 
     * @param cliente
     *            ClienteDTO con datos del cliente
     * @throws SystemException
     */
    public long clienteInsert(ClienteDTO cliente) throws SystemException {

        try {

            return cli_service.clienteInsertFO(cliente);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Modifica los datos de un cliente
     * 
     * @param cliente
     *            ClienteDTO con datos del cliente
     * @throws SystemException
     */
    public void clienteUpdate(ClienteDTO cliente) throws SystemException {

        try {

            cli_service.clienteUpdateFO(cliente);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Actualiza los datos de contacto de un cliente
     * 
     * @param cliente
     *            ClienteDTO con datos del cliente
     * @throws SystemException
     */
    public void updateDatosContactoCliente(ClienteDTO cliente) throws SystemException {

        try {

            cli_service.updateDatosContactoClienteFO(cliente);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }
    
    
    /**
     * Cambio de clave de un cliente
     * 
     * @param rut
     *            RUT del cliente
     * @param estado
     *            Estado final para el cliente luego de modificar clave
     * @throws SystemException
     */
    public void clienteChangePass(long rut, String estado) throws SystemException {

        try {

            cli_service.clienteChangePassFO(rut, estado);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Cambio de clave de un cliente
     * 
     * @param rut
     *            RUT del cliente
     * @param clave
     *            Clave de reemplazo
     * @param estado
     *            Estado final para el cliente luego de modificar clave
     * @throws SystemException
     */
    public void clienteChangePass(long rut, String clave, String estado) throws SystemException {

        try {

            cli_service.clienteChangePassFO(rut, clave, estado);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * actualiza datos cliente paso3
     * 
     * @param cli_id
     *            Id del cliente
     * @param email
     *            Email del cliente
     * @param codigo
     *            codigo telefono 2
     * @param telefono
     *            telefono 2 del cliente
     * @throws SystemException
     */
    public void clienteChangeDatosPaso3(long cli_id, String email, String codigo, String telefono)
            throws SystemException {

        try {

            cli_service.clienteChangeDatosPaso3FO(cli_id, email, codigo, telefono);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Agrega una dirección de despacho al cliente
     * 
     * @param direccion
     *            DTO con datos de la dirección de despacho
     * @throws SystemException
     * @see DireccionesDTO
     */
    public long clienteInsertDireccion(DireccionesDTO direccion) throws SystemException {
        try {
            return cli_service.clienteInsertDireccionFO(direccion);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Elimina dirección de despacho del cliente
     * 
     * @param direccion_id
     *            Identificador único de la dirección a eliminar
     * @throws SystemException
     */
    public void clienteDeleteDireccion(long direccion_id) throws SystemException {

        try {

            cli_service.clienteDeleteDireccionFO(direccion_id);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Modifica dirección de despacho del cliente
     * 
     * @param direccion_id
     *            DTO con datos de la dirección a modificar
     * @throws SystemException
     * @see DireccionesDTO
     */
    public void clienteUpdateDireccion(DireccionesDTO direccion_id) throws SystemException {

        try {

            cli_service.clienteUpdateDireccionFO(direccion_id);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Crea un nuevo cliente
     * 
     * @param cliente
     *            DTO con datos del cliente
     * @param despachos
     *            DTO con datos de las direcciones de despacho
     * @throws SystemException
     */
    public String clienteNewRegistro(ClienteDTO cliente, DireccionesDTO direccion) throws SystemException {
        try {
            return cli_service.clienteNewRegistroFO(cliente, direccion);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Revisa si existen productos en el carro de compras del cliente
     * 
     * @param cliente
     *            Identificador único del cliente
     * @return True: existe False: no existe
     * @throws SystemException
     */
    public boolean clienteExisteCarroCompras(long cliente) throws SystemException {
        try {

            return cli_service.clienteExisteCarroComprasFO(cliente);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Vacía carro de compras para el cliente
     * 
     * @param cliente
     *            Identificador único del cliente
     * @param producto
     *            Identificador único del producto
     * @throws SystemException
     */
    public void carroComprasDeleteProducto(long cliente, long producto, String idSession) throws SystemException {
        try {
            cli_service.carroComprasDeleteProductoFO(cliente, producto, idSession);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }
    
    /**
     * Modifica Cantidad Producto Mi Carro
     * 
     * @param cliente Identificador único del cliente
     * @param producto Identificador único del producto en el carro
     * @param cantidad cantidad del producto
     * @param idSession id de session del cliente
     * @throws SystemException
     */
    public void modificarCantidadProductoMiCarro(long cliente, long producto, double cantidad, String idSession, String tipoSel) throws SystemException {
        try {
            cli_service.modificarCantidadProductoMiCarroFO(cliente, producto, cantidad, idSession, tipoSel);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Borra prodcuto del carro del cliente
     * 
     * @param cliente
     *            Identificador único del cliente
     * @param producto
     *            Identificador único del producto
     * @throws SystemException
     */
    public void carroComprasDeleteProductoxId(long cliente, long producto, String idSession) throws SystemException {
        try {
            cli_service.carroComprasDeleteProductoxIdFO(cliente, producto, idSession);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Actualiza los datos del productos en el carro de compras
     * 
     * @param cliente
     *            Identificador único del cliente
     * @param producto
     *            Identificador único del producto
     * @param cantidad
     *            Cantidad a modificar
     * @throws SystemException
     */
    public void carroComprasUpdateProducto(long cliente, long producto, double cantidad, String nota, String idSession, String tipoSel) throws SystemException {
        try {
            cli_service.carroComprasUpdateProductoFO(cliente, producto, cantidad, nota, idSession, tipoSel);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Guarda una lista de compra a través del carro
     * 
     * @param nombre
     *            nombre de la lista
     * @param cliente
     *            Identificador único del cliente
     * @throws SystemException
     */
    public int carroComprasSaveList(String nombre, long cliente) throws SystemException {
        try {

            return cli_service.carroComprasSaveListFO(nombre, cliente);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Elimina una lista de compra
     * 
     * @param id_lista
     *            id de la lista
     * @throws SystemException
     */
    public int carroComprasDeleteList(int id_lista) throws SystemException {
        try {

            return cli_service.carroComprasDeleteListFO(id_lista);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Agrega una lista de producto al carro de compras
     * 
     * @param listcarro
     *            Lista de DTO con datos de los productos
     * @param cliente
     *            Identificador único del cliente
     * @throws SystemException
     */
    public void carroComprasInsertProducto(List listcarro, long cliente, String idSession) throws SystemException {
        try {

            cli_service.carroComprasInsertProductoFO(listcarro, cliente, idSession);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Retorna la lista de productos del carro de compras del cliente. Si el
     * local es -1 sólo recupera los productos para consultar por datos de
     * productos sin precios.
     * 
     * @param local
     *            Identificador del local asociado a la dirección de despachos
     * @param cliente_id
     *            Identificador único del cliente
     * @return Lista de DTO con datos del los productos
     * 
     * @throws SystemException
     */
    public List carroComprasGetProductos(long cliente_id, String local, String idSession) throws SystemException {
        try {
            return cli_service.carroComprasGetFO(cliente_id, local, idSession);
        } catch (Exception ex) {
            logger.error("Problemas clienteGetCarroCompras", ex);
            throw new SystemException(ex);
        }
    }
    
    /*
     * Crea el cliente desde la sesión de invitado para poder asociar el pedido y guardar la información
     * Se asocia el carro de compras y se ingresan los criterios de sustitución
     */
    public String creaClienteDesdeInvitado(ClienteDTO cliente, DireccionesDTO desp, String forma_despacho) throws SystemException {
        try {
            return cli_service.creaClienteDesdeInvitadoFO(cliente, desp, forma_despacho);
        } catch (Exception ex) {
            logger.error("Problemas creaClienteDesdeInvitado", ex);
            throw new SystemException(ex);
        }
    }
    
    
    /*
     * Se asocia el carro de compras al Nuevo ID del Cliente
     */
    public boolean reasignaCarroDelInvitado(long idCliente, long idInvitado) throws SystemException {
        try {
            return cli_service.reasignaCarroDelInvitadoFO(idCliente, idInvitado);
        } catch (Exception ex) {
            logger.error("Problemas creaClienteDesdeInvitado", ex);
            throw new SystemException(ex);
        }
    }
    
    //riffo
    
    public List carroComprasGetProductosCheckOut(long cliente_id, String local, String idSession) throws SystemException {
        try {
            return cli_service.carroComprasGetCheckOutFO(cliente_id, local, idSession);
        } catch (Exception ex) {
            logger.error("Problemas clienteGetCarroCompras", ex);
            throw new SystemException(ex);
        }
    }
    
    
    /*
     * Se asocian los criterios de sustitución al Nuevo ID de Cliente
     */
    public boolean reasignaSustitutosDelInvitado(long idCliente, long idInvitado) throws SystemException {
        try {
            return cli_service.reasignaSustitutosDelInvitadoFO(idCliente, idInvitado);
        } catch (Exception ex) {
            logger.error("Problemas creaClienteDesdeInvitado", ex);
            throw new SystemException(ex);
        }
    }
    
    /**
     * Retorna true si el carro de compras esta vacio, de lo contrario retorna false
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @param idSession
     *            Identificador de la sessión
     * @return true si carro esta vacio, sino false
     * 
     * @throws SystemException
     */
    public boolean isCarroComprasEmpty(long cliente_id, String idSession) throws SystemException {
        try {
            return cli_service.isCarroComprasEmptyFO(cliente_id, idSession);
        } catch (Exception ex) {
            logger.error("Problemas isCarroComprasEmpty", ex);
            throw new SystemException(ex);
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
    public long carroComprasGetCantidadProductos(long cliente_id, String idSession) throws SystemException {
        try {
            return cli_service.carroComprasGetCantidadProductosFO(cliente_id, idSession);
        } catch (Exception ex) {
            logger.error("Problemas carroComprasGetCantidadProductos", ex);
            throw new SystemException(ex);
        }
    }    
    
    /**
     * Retorna la lista de productos del carro de compras del cliente ordenado por categorias.
     * 
     * @param local
     *            Identificador del local asociado a la dirección de despachos
     * @param cliente_id
     *            Identificador único del cliente
     * @return Lista de DTO con datos del los productos
     * 
     * @throws SystemException
     */
    public List carroComprasPorCategorias(long cliente_id, String local, String idSession) throws SystemException {
        try {
            return cli_service.carroComprasPorCategoriasFO(cliente_id, local, idSession);
        } catch (Exception ex) {
            logger.error("Problemas carroComprasPorCategorias", ex);
            throw new SystemException(ex);
        }
    }
    
    /**
     * elimina los productos no disponibles del carro de un cliente
     * 
     * @param local
     *            Identificador del local asociado a la dirección de despachos
     * @param cliente_id
     *            Identificador único del cliente
     * 
     * @throws SystemException
     */
    public void eliminaProdCarroNoDisp(long cliente_id, String local, String idSession) throws SystemException {
        try {
            System.out.println("Entra a eliminar el carro");
            cli_service.eliminaProdCarroNoDispFO(cliente_id, local, idSession);
        } catch (Exception ex) {
            logger.error("Problemas eliminaProdCarroNoDisp", ex);
            throw new SystemException(ex);
        }
    }    
    
    /**
     * Retorna la lista de productos del carro de compras del cliente ordenado por categorias.
     * 
     * @param local
     *            Identificador del local asociado a la dirección de despachos
     * @param cliente_id
     *            Identificador único del cliente
     * @return Lista de DTO con datos del los productos
     * 
     * @throws SystemException
     */
    public List carroComprasPorCategorias(long cliente_id, String local, String idSession, String filtro) throws SystemException {
        try {
            return cli_service.carroComprasPorCategoriasFO(cliente_id, local, idSession, filtro);
        } catch (Exception ex) {
            logger.error("Problemas carroComprasPorCategorias", ex);
            throw new SystemException(ex);
        }
    }    
    

    /**
     * Recupera los productos del carro de compras con sus categorías
     * 
     * @param local
     *            Identificador único del local
     * @param cliente_id
     *            Identificador único del cliente
     * @param modo_orden
     *            Forma de ordenamiento de los productos
     * @return Lista de DTO con datos de las categorías y sus productos
     * @throws SystemException
     */
    public List carroComprasGetCategoriasProductos(String local, long cliente_id) throws SystemException {
        try {
            return cli_service.carroComprasGetCategoriasProductosFO(local, cliente_id);
        } catch (Exception ex) {
            logger.error("Problemas carroComprasGetCategoriasProductos", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera los productos del pedido con sus categorías
     * 
     * @param id_pedido
     *            Identificador único del pedido
     * @return Lista de DTO con datos de las categorías y sus productos
     * @throws SystemException
     */
    public List resumenCompraGetCategoriasProductos(long id_pedido) throws SystemException {
        try {
            return clientesService.resumenCompraGetCategoriasProductos(id_pedido);

        } catch (Exception ex) {
            logger.error("Problemas resumenCompraGetCategoriasProductos", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera las politicas de sustitución
     * 
     * @return Lista de DTO
     * @throws SystemException
     */
    public List PoliticaSustitucion() throws SystemException {
        List result = null;
        try {

            result = cli_service.PoliticaSustitucionFO();

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Recupera las formas de pago permitidas
     * 
     * @return Lista de DTO
     * @throws SystemException
     */
    public List FormaPago() throws SystemException {
        List result = null;
        try {

            result = cli_service.FormaPagoFO();

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Recupera las categorías que tiene productos para el cliente
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @return Lista de DTO
     * @throws SystemException
     */
    public List productosGetCategorias(long cliente_id) throws SystemException {
        try {
            return pro_service.Categoria(cliente_id);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }
    
    /**
     * Recupera las categorías intermedias y Terminales que tienen productos para el cliente
     * 
     * @param cliente_id Identificador único del cliente
     * @param cabecera_id Identificador cabecera
     * @return Lista de DTO
     * @throws SystemException
     */
    public List categoriasGetInteryTerm(long cliente_id, long cabecera_id) throws SystemException {
        try {
            return pro_service.categoriasGetInteryTerm(cliente_id, cabecera_id);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }    
    
    /**
     * Entrega una categoría
     * 
     * @param categoria_id
     *            Identificador único de la categoría
     * @return DTO
     * @throws SystemException
     * @see CategoriaDTO
     */
    public CategoriaDTO getCategoria(long categoria_id) throws SystemException {
        try {

            return pro_service.getCategoria(categoria_id);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera los datos del producto
     * 
     * @param local_id
     *            Identificador único del local
     * @param categoria_id
     *            Identificador único de la categoría
     * @param cliente_id
     *            Identificador único del cliente
     * @param marca
     *            Marca para filtrar
     * @param orden
     *            Forma de ordenamiento de los productos
     * @param lista_tcp
     *            Listado de TCP para el cliente
     * @return Lista de DTO con datos de los productos
     * @throws SystemException
     */
    public List productosGetProductos(String local_id, long categoria_id, long cliente_id, String marca, String orden,
            List lista_tcp) throws SystemException {
        List result = null;
        try {

            result = pro_service.getProductosList(local_id, categoria_id, cliente_id, marca, orden, lista_tcp);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getProductosList)", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Recupera las marcas de los productos de la categoría indicada.
     * 
     * @param categoria_id
     *            Identificador único de la categoría
     * @return Lista de DTO con datos de las marcas
     * @throws SystemException
     */
    public List productosGetMarcas(long categoria_id) throws SystemException {
        try {

            return pro_service.getMarcas(categoria_id);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getProductosList)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera la lista de compras históricoas
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @return Lista de DTO con datos de las compras históricas (web-local)
     * @throws SystemException
     */
    public List compraHistoryGetList(long cliente_id) throws SystemException {
        List result = null;
        try {

            result = cli_service.compraHistoryGetListFO(cliente_id);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Agrega una nueva compra histórica web.
     * 
     * @param nombre
     *            Nombre de la compra histórica
     * @param cliente_id
     *            Identificador único del cliente
     * @param unidades
     *            Cantidad total de unidades de la compra
     * @param pedido_id
     *            Identificador único del pedido
     * @throws SystemException
     */
    public void setCompraHistorica(String nombre, long cliente_id, long unidades, long pedido_id)
            throws SystemException {
        try {

            ped_service.setCompraHistorica(nombre, cliente_id, unidades, pedido_id);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Actualiza una compra histórica web.
     * 
     * @param nombre
     *            Nombre de la compra histórica
     * @param id_cliente
     *            Identificador único del cliente
     * @param unidades
     *            Cantidad total de unidades de la compra
     * @param pedido_id
     *            Identificador único del pedido
     * @param id_lista
     *            Identificador único de la lista
     * @throws SystemException
     */
    public void updateCompraHistorica(String nombre, long id_cliente, long unidades, long pedido_id, long id_lista)
            throws SystemException {
        try {

            ped_service.updateCompraHistorica(nombre, id_cliente, unidades, pedido_id, id_lista);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Elimina los productos del carro de compras
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @throws SystemException
     */
    public void deleteCarroCompraAll(long cliente_id, String ses_invitado_id) throws SystemException {
        try {

            cli_service.deleteCarroCompraAllFO(cliente_id, ses_invitado_id);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Limpia el carro de compra
     * 
     * @param cliente_id Identificador único del cliente
     * @param ses_invitado_id identificador de la session
     * @throws SystemException
     */
    public void limpiarMiCarro(long cliente_id, String id_session) throws SystemException {
        try {

            cli_service.limpiarMiCarroFO(cliente_id, id_session);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    
    //riffo
    public void deleteCarroCompra(long cliente_id, long carro_id, String idSession) throws SystemException {
        try {

            cli_service.deleteCarroCompraFO( cliente_id,  carro_id,  idSession);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Listado con UN SOLO producto que corresponde a ProductoDTO para el
     * producto consultado
     * 
     * @param producto_id
     *            Identificador único del producto
     * @param cliente_id
     *            Identificador único del cliente
     * @param lista_tcp
     *            Listado de TCP para el cliente
     * @return Lista con DTO del producto consultado
     * @throws SystemException
     */
    public List getProducto(long producto_id, long cliente_id, long local_id, List lista_tcp, String idSession ) throws SystemException {
        List result = null;
        try {

            result = pro_service.getProducto(producto_id, cliente_id, local_id, lista_tcp, idSession);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Obtiene los sugeridos para el producto
     * 
     * @param producto_id
     *            Identificador único del producto
     * @param cliente_id
     *            Identificador único del cliente
     * @param local_id
     *            Identificador único del local
     * @return Lista de DTO con datos de los productos sugeridos
     * @throws SystemException
     */
    public List getSugerido(long producto_id, long cliente_id, long local_id, String idSession) throws SystemException {
        try {
            return pro_service.getSugerido(producto_id, cliente_id, local_id, idSession);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera categorías y marcas para la búsqueda
     * 
     * @param patron
     *            Patrón de búsqueda
     * @param local_id
     *            Identificador único del local
     * @return Lista de DTO con datos de las categorías
     * @throws SystemException
     */
    public List getSearch(List patron, long local_id) throws SystemException {
        List result = null;
        try {

            result = pro_service.getSearch(patron, local_id);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Recupera los productos búscados por marca
     * 
     * @param local_id
     *            Identificador único del local
     * @param marca_id
     *            Identificador único de la marca
     * @param cliente_id
     *            Identificador único del cliente
     * @param orden
     *            Forma de ordenamiento de los productos
     * @param patron
     *            Patrón de búsqueda
     * @param lista_tcp
     *            Listado de TCP para el cliente
     * @return Lista de DTO con datos de los productos
     * @throws SystemException
     */
    public List getSearchMarca(String local_id, long marca_id, long cliente_id, String orden, List patron,
            List lista_tcp) throws SystemException {
        List result = null;
        try {

            result = pro_service.getProductoMarca(local_id, marca_id, cliente_id, orden, patron, lista_tcp);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Recupera los productos búscados por categoría
     * 
     * @param local_id
     *            Identificador único del local
     * @param categoria_id
     *            Identificador único de la categoría
     * @param cliente_id
     *            Identificador único del cliente
     * @param marca
     *            Marca a buscar
     * @param orden
     *            Forma de ordenamiento de los productos
     * @param patron
     *            Patrón de búsqueda
     * @param lista_tcp
     *            Listado de TCP para el cliente
     * @return Lista de DTO con datos de los productos
     * @throws SystemException
     */
    public List getSearchListSeccion(String local_id, long categoria_id, long cliente_id, String marca, String orden,
            List patron, List lista_tcp) throws SystemException {
        List result = null;
        try {

            result = pro_service.getProductosListSeccion(local_id, categoria_id, cliente_id, marca, orden, patron,
                    lista_tcp);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate (productosGetProductosSeccion)", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * 
     * Agrega registro de mail para ser enviado
     * 
     * @param mail
     *            DTO con información del mail
     * @throws SystemException
     */
    public void addMail(MailDTO mail) throws SystemException {
        try {
            clientesService.addMail(mail);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (addMail)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * 
     * Recupera los datos del ejecutivo de call center
     * 
     * @param login
     *            Login de ingreso del ejecutivo
     * @return Usuario DTO con datos del ejecutivo
     * @throws SystemException
     */
    public UsuarioDTO ejecutivoGetByRut(String login) throws SystemException {
        try {
            return fono_service.ejecutivoGetByRut(login);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (ejecutivoGetByRut)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera el calendario de despachos. Utiliza libreria externa de BOL.
     * 
     * @param n_semana
     *            Número de semana en el año
     * @param ano
     *            Año de consulta
     * @param id_zona
     *            Identificador único de la zona de consulta
     * @return CalendarioDTO
     * @throws SystemException
     */
    public CalendarioDespachoDTO getCalendarioDespacho(int n_semana, int ano, long id_zona) throws SystemException {
        try {
            return bolped_service.getCalendarioDespacho(n_semana, ano, id_zona);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getCalendarioDespacho)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Verifica capacidad de despacho de una jornada
     * 
     * @param cant_productos
     *            Cantidad de productos
     * @param id_jdespacho
     *            Identificador único de la jornada de despacho
     * @return True: existe, False: no existe
     * @throws SystemException
     */
    public boolean doVerificaCapacidadDespacho(long cant_productos, long id_jdespacho) throws SystemException {
        try {
            return bolped_service.doVerificaCapacidadDespacho(cant_productos, id_jdespacho);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (doVerificaCapacidadDespacho)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Agenda capacidad de despacho en una jornada
     * 
     * @param cant_productos
     *            Cantidad de productos
     * @param id_jdespacho
     *            Identificador único de la jornada de despacho
     * @throws SystemException
     * @throws FuncionalException
     */
    /*
    public void doAgendaDespacho(long cant_productos, long id_jdespacho) throws SystemException, FuncionalException {
        try {
            bolped_service.doAgendaDespacho(cant_productos, id_jdespacho);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (doAgendaDespacho)", e);
            throw new SystemException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (doAgendaDespacho)", e);
            throw new FuncionalException(e);
        }
    }*/


    /**
     * Inserta un pedido en el BO
     * 
     * @param pedido
     *            DTO con datos del pedido
     * @return Identificador único del pedido ingresado
     * @throws FuncionalException
     */
    public long doInsPedido(ProcInsPedidoDTO pedido) throws FuncionalException {
    	return doInsPedidoNew(pedido, null, null);
    }
    
    /**
     * Se agregan parametros para aplicar cupon de descuento (cddto, cuponProds) cdd
     * @param pedido
     * @param cddto
     * @param cuponProds
     * @return
     * @throws FuncionalException
     */
    public long doInsPedidoNew(ProcInsPedidoDTO pedido, CuponDsctoDTO cddto, List cuponProds) throws FuncionalException {

        try {
            return bolped_service.doInsPedido(pedido, cddto, cuponProds);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (doInsPedido)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (doInsPedido)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * 
     * Retorna datos del último pedido del cliente
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @return DTO con datos del pedido
     * @throws FuncionalException
     */
    public PedidoDTO getUltimoIdPedidoCliente(long cliente_id) throws FuncionalException {

        try {
            return bolped_service.getUltimoIdPedidoCliente(cliente_id);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (doInsPedido)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (doInsPedido)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * 
     * Retorna datos del pedido indicado
     * 
     * @param pedido_id
     *            Identificador único del pedido
     * @return DTO con datos del pedido
     * @throws FuncionalException
     */
    public PedidoDTO getPedidoById(long pedido_id) throws FuncionalException {

        try {
            return bolped_service.getPedido(pedido_id);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (doInsPedido)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (doInsPedido)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * Recupera los productos del pedido
     * 
     * @param pedido_id
     *            Identificador único del pedido
     * @return DTO con datos del pedido
     * @throws FuncionalException
     */
    public List getProductosPedido(long pedido_id) throws FuncionalException {
        try {
            return bolped_service.getProductosPedido(pedido_id);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (doInsPedido)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * Tracking de clientes en el web
     * 
     * @param seccion
     *            Sección de track
     * @param rut
     *            RUT del cliente
     * @param arg0
     *            Datos del navegador y URL
     * @throws SystemException
     */
    public void addTraking(String seccion, Long rut, HashMap arg0) throws SystemException {
        try {
            cli_service.addTrakingFO(seccion, rut, arg0);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (addTraking)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera Categorias de FAQ
     * 
     * @return Lista de DTO con datos de las categorías de FAQ
     * 
     * @throws SystemException
     */
    public List getFaqCategoria() throws SystemException {
        List result = null;
        try {
            result = faq_service.getFaqCategoria();
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getFaqCategoria)", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Recupera las Preguntas de FAQ
     * 
     * @param idcat
     *            Identificador único de la categoría
     * @return Lista de DTO con datos de las preguntas
     * @throws SystemException
     */
    public List getFaqPregunta(long idcat) throws SystemException {
        List result = null;
        try {
            result = faq_service.getFaqPregunta(idcat);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getFaqPregunta)", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Datos para marketing
     * 
     * @param ele_id
     *            Identificador único del elemento de marketing
     * @return DTO con datos del elemento
     * @throws SystemException
     */
    public MarketingElementoDTO getMarkElemento(long ele_id, long mar_id) throws SystemException {
        try {
            return marketing_service.getMarkElemento(ele_id, mar_id);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getMarkElemento)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Actualiza el ranking de ventas luego de realizar un pedido
     * 
     * @param id_cliente
     *            id de cliente para el cual actualizar el ranking
     * @throws SystemException
     */
    public void updateRankingVentas(long id_cliente) throws SystemException {
        try {
            ped_service.updateRankingVentas(id_cliente);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (updateRankingVentas)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Modifica la cantidad de intentos de logeo de un cliente
     * 
     * @param cliente_id
     *            Identificador único del cliente
     * @param accion
     *            Acción a gatillar 1: Aumentar, 0: Reset
     * @throws SystemException
     */
    public void updateIntentos(long cliente_id, long accion) throws SystemException {

        try {
            cli_service.updateIntentosFO(cliente_id, accion);
        } catch (Exception ex) {
            logger.error("Problemas con controles de clientes", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Actualiza el nombre en una compra histórica web.
     * 
     * @param nombre
     *            Nombre de la compra histórica
     * @param id_cliente
     *            Identificador único del cliente
     * @throws SystemException
     */
    public void updateNombreCompraHistorica(String nombre, long id_cliente) throws SystemException {
        try {

            ped_service.updateNombreCompraHistorica(nombre, id_cliente);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Guarda los datos del invitado.
     * 
     * @param cliente
     *            Datos del invitado
     * @throws SystemException
     */
    public void updateDatosInvitado(ClienteDTO cliente, String opcion) throws SystemException {
        try {

            cli_service.updateDatosInvitadoFO(cliente, opcion);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }    
    
    /**
     * Para obtener el evento que debemos mostrar para el Rut indicado
     * 
     * @param clienteRut
     *            Rut del cliente
     * @return Evento a mostrar, si no tenemos que mostrar ningún evento
     *         devuleve un "new EventoDTO" (con idEvento = 0)
     */
    public EventoDTO getEventoMostrarByRut(long clienteRut) throws SystemException {
        try {
            return eventoService.getEventoMostrarByRut(clienteRut);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getEventoMostrarByRut)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Para obtener el evento que debemos mostrar de acuerdo a una fecha y al
     * Rut de un cliente
     * 
     * @param rutCliente
     *            Rut del cliente
     * @param fingreso
     *            Fecha del evento
     * @return Evento a mostrar, si no tenemos que mostrar ningún evento
     *         devuleve un "new EventoDTO" (con idEvento = 0)
     */
    public EventoDTO getEventoMostrarByFecha(long rutCliente, String fingreso) throws SystemException {
        try {
            return eventoService.getEventoMostrarByRutYFecha(rutCliente, fingreso);
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }

    /**
     * Modificamos la ocurrencia del evento.
     * 
     * @param rut_cliente
     * @param idPedido
     */
    public boolean subirOcurrenciaEvento(long rutCliente, long idPedido) throws SystemException {
        try {
            return eventoService.subirOcurrenciaEvento(rutCliente, idPedido);
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }

    /**
     * Obtiene los datos de una zona por su id
     * 
     * @param id_zona
     * @return
     * @throws SystemException
     */
    public ZonaDTO getZonaDespachoById(long id_zona) throws SystemException {
        try {

            return bolped_service.getZonaDespacho(id_zona);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Obtiene listado de promociones para un producto.
     * 
     * @param id_producto
     *            Identificador único del producto
     * @param lista_tcp
     *            Listado de TCP para el cliente
     * @return Listado de promociones
     * @throws SystemException
     */
    public List promocionesGetPromocionesByProductoId(long idProductoFO, long idProductoBO, String id_local, List lista_tcp)
            throws SystemException {
        try {

            return promociones_service.getPromocionesByProductoId(idProductoFO, idProductoBO, id_local, lista_tcp);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }

    }

    /**
     * Entrega el total considerando las promociones según parámetros.
     * 
     * @param total
     *            Total sin promociones
     * @return Total con promociones
     * @throws SystemException
     */
    public long getTotalPromocion(long total) throws SystemException {
        try {

            return promociones_service.getTotalPromocion(total);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recuperar promociones por TCP
     * 
     * @param tcp
     *            Listado de TCP
     * @return Listado de promociones
     * @throws SystemException
     */
    public List getPromocionesByTCP(List tcp) throws SystemException {
        try {
            return promociones_service.getPromocionesByTCP(tcp);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recuperar promociones por TCP
     * 
     * @param tcp      Listado de TCP
     * @param id_local Identificador del Local
     * @return Listado de promociones
     * @throws SystemException
     */
    public List getPromocionesByTCP(List tcp, String id_local) throws SystemException {
        try {
            return promociones_service.getPromocionesByTCP(tcp, id_local);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recalcula las promociones para un pedido en un local.
     * 
     * @param recalculodtp
     * @return doRecalculoResultado
     * @throws SystemException
     */
    public doRecalculoResultado doRecalculoPromocion(doRecalculoCriterio recalculodto) throws SystemException {
    	return doRecalculoPromocionNew(recalculodto, null, null);
    }
    
    /**
     * Se agregan parametros para aplicar cupon de descuento (cddto, cuponProds) cdd
     * @param recalculodto
     * @param cddto
     * @param cuponProds
     * @return
     * @throws SystemException
     */
    public doRecalculoResultado doRecalculoPromocionNew(doRecalculoCriterio recalculodto, CuponDsctoDTO cddto, List cuponProds) throws SystemException {
        try {
            return promociones_service.doRecalculoPromocion(recalculodto, cddto, cuponProds);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Recupera el id de local POS
     * 
     * @param id_local
     * @return
     * @throws SystemException
     */
    public int getCodigoLocalPos(long id_local) throws SystemException {
        try {
            return promociones_service.getCodigoLocalPos(id_local);

        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @param string
     * @param paginado
     * @param pagina
     * @return
     */
    public List carroComprasMobi(long idCliente, String local, int paginado, int pagina) throws SystemException {
        List result = new ArrayList();
        try {
            result = cli_service.carroComprasMobiFO(idCliente, local, paginado, pagina);
        } catch (Exception ex) {
            logger.error("Problemas clienteGetCarroCompras", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * @param idCliente
     * @param local
     * @return
     */
    public double getCountCarroCompras(long idCliente, String local) throws SystemException {
        try {
            return cli_service.getCountCarroComprasFO(idCliente, local);
        } catch (Exception ex) {
            logger.error("Problemas clienteGetCarroCompras", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idCarro
     * @param idProducto
     * @param nota
     */
    public void updateNotaCarroCompra(long idCliente, long idCarro, long idProducto, String nota)
            throws SystemException {
        try {
            cli_service.updateNotaCarroCompraFO(idCliente, idCarro, idProducto, nota);
        } catch (Exception ex) {
            logger.error("Problemas clienteGetCarroCompras", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * Lista de categorias del primer nivel
     * 
     * @return
     * @throws SystemException
     */
    public List categorias() throws SystemException {
        try {
            return pro_service.categorias();
        } catch (Exception ex) {
            logger.error("Problemas categorias", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idLocal
     * @param idSubCategoria
     * @return
     * @throws SystemException
     */
    public int cantidadProductosDeSubCategoria(int idLocal, int idSubCategoria, int idMarca) throws SystemException {
        try {
            return pro_service.cantidadProductosDeSubCategoria(idLocal, idSubCategoria, idMarca);
        } catch (Exception ex) {
            logger.error("Problemas subCategorias", ex);
            throw new SystemException(ex);
        }
    }
    
    /**
     * @param idSesion
     * @return id
     * @throws SystemException
     */
    public int crearSesionInvitado(String idSesion) throws SystemException {
        try {
            return cli_service.crearSesionInvitadoFO(idSesion);
        } catch (Exception ex) {
            logger.error("Problemas crearSesionInvitado", ex);
            throw new SystemException(ex);
        }
    }
    
    
    /**
     * @param idsProductos
     * @param idLocal
     * @param idCategoria
     * @param idSubCategoria
     * @return
     * @throws SystemException
     */
    public int cantidadProductosPorIds(List idsProductos, int idLocal, int idMarca, int idCategoria, int idSubCategoria) throws SystemException {
        try {
            return pro_service.cantidadProductosPorIds(idsProductos, idLocal, idMarca, idCategoria, idSubCategoria);
        } catch (Exception ex) {
            logger.error("Problemas subCategorias", ex);
            throw new SystemException(ex);
        }
    }
    

    /**
     * @param idCategoria
     * @return
     * @throws SystemException
     */
    public List subCategorias(int idCategoria) throws SystemException {
        try {
            return pro_service.subCategorias(idCategoria);
        } catch (Exception ex) {
            logger.error("Problemas subCategorias", ex);
            throw new SystemException(ex);
        }
    }

    public List productosPorSubCategoria(int idCliente, int idLocal, int idSubCategoria, int idMarca, String ordenarPor, int filaNumero, int filaCantidad) throws SystemException{
        try {
            return pro_service.productosPorSubCategoria(idCliente, idLocal, idSubCategoria, idMarca, ordenarPor, filaNumero, filaCantidad);
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }
    
    /**
     * @param idCliente
     * @param idLocal
     * @param idSubCategoria
    * @param idCategoria
     * @return
     */
    public List productosPorSubCategoria(int idCliente, int idLocal, int idSubCategoria, int idCategoria, String idSession) throws SystemException{
       try {
          return pro_service.productosPorSubCategoria(idCliente, idLocal, idSubCategoria, idCategoria, idSession);
      } catch (Exception ex) {
          throw new SystemException(ex);
      }
    }

    /**
     * @param diasCalendario
     * @param id_zona
     * @return
     */
    public List getCalendarioDespachoByDias(int diasCalendario, long idZona) throws SystemException {
        try {
            return bolped_service.getCalendarioDespachoByDias(diasCalendario, idZona);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getCalendarioDespachoByDias)", ex);
            throw new SystemException(ex);
        }
    }
    
    /**
     * @param diasCalendario
     * @param id_zona
     * @return
     */
    public List getCalendarioDespachoByDias(int diasCalendario, long idZona, long id_jpicking, int cantProductos) throws SystemException {
        try {
            return bolped_service.getCalendarioDespachoByDias(diasCalendario, idZona, id_jpicking, cantProductos);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getCalendarioDespachoByDias)", ex);
            throw new SystemException(ex);
        }
    }
    
   
    public List cargarPromociones(List productos, List lista_tcp, int idLocal) throws PromocionesException{
        return pro_service.cargarPromociones(productos, lista_tcp, idLocal);
    }
    
    public List cargarPromocionesMiCarro(List productos, List lista_tcp, int idLocal) throws PromocionesException{
        return pro_service.cargarPromocionesMiCarro(productos, lista_tcp, idLocal);
    }
    
    /**
     * @param idLocal
     * @param idSubCategoria
     * @return
     * @throws SystemException
     */
    public List marcasPorSubCategoria(int idLocal, int idSubCategoria) throws SystemException {
        try {
            return pro_service.marcasPorSubCategoria(idLocal, idSubCategoria);
        } catch (Exception ex) {
            logger.error("Problemas marcas", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idsProductos
     * @param idCategoria
     * @param idSubCategoria
     * @param idSubCategoria
     * @param ordenarPor
     * @param filaCantidad
     * @param filaNumero
     * @return
     * @throws SystemException
     */
    public List productosPorIds(int idCliente, List idsProductos, int idLocal, int idMarca, int idCategoria, int idSubCategoria, String ordenarPor, int filaNumero, int filaCantidad) throws SystemException {
        try {
            return pro_service.productosPorIds(idCliente, idsProductos, idLocal, idMarca, idCategoria, idSubCategoria, ordenarPor, filaNumero, filaCantidad);
        } catch (Exception ex) {
            logger.error("Problemas productos", ex);
            throw new SystemException(ex);
        }
    }
    
    /**
     * FIXME NO se usa: se debe borrar
     * @param idCliente
     * @param idsProductos
     * @param idLocal
     * @return
     */
    public List productosPorIds(int idCliente, List idsProductos, int idLocal) throws SystemException {
       try {
          return pro_service.productosPorIds(idCliente, idsProductos, idLocal);
      } catch (Exception ex) {
          logger.error("Problemas productos", ex);
          throw new SystemException(ex);
      }
    }
    
    public ProductoDTO productosById(int idCliente, int idProducto, int idLocal) throws SystemException {
        try {
           return pro_service.productoById(idCliente, idProducto, idLocal);
       } catch (Exception ex) {
           logger.error("Problemas productos", ex);
           throw new SystemException(ex);
       }
     }
    
    public List getProductosDespubOrSinStock(long idCliente, List idProductos, int idLocal) throws SystemException{
    	try {
    		return pro_service.getProductosDespubOrSinStock(idCliente, idProductos, idLocal);
    	} catch (Exception ex) {
    		logger.error("Problema en obtener productos despublicados o sin Stock");
    		throw new SystemException(ex);
    	}
    }

    public List getProductosSinStockDespublicadosPorLista(long idCliente, int idLista, int idLocal) throws SystemException{
    	try {
    		return pro_service.getProductosSinStockDespublicadosPorLista(idCliente, idLista, idLocal);
    	} catch (Exception ex) {
    		logger.error("Problema en obtener productos despublicados o sin Stock");
    		throw new SystemException(ex);
    	}
    }

    
    /**
     * @param marcasIds
     * @param idLocal
     * @return
     * @throws SystemException
     */
    public List marcasPorIds(List marcasIds, int idLocal) throws SystemException {
        try {
            return pro_service.marcasPorIds(marcasIds, idLocal);
        } catch (Exception ex) {
            logger.error("Problemas marcasPorIdsProductos", ex);
            throw new SystemException(ex);
        }
    }


    /**
     * @param clienteId
     * @return
     * @throws SystemException
     */
    public Hashtable productosCarro(int clienteId, String idSession) throws SystemException {
        try {
            return pro_service.productosCarro(clienteId, idSession);
        } catch (Exception ex) {
            logger.error("Problemas productosCarro", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param time
     * @param semana
     * @param anio
     * @param id_zona
     * @return
     */
    public CalendarioDespachoDTO getCalendarioDespachoByFecha(Date fecha, int semana, int anio, long idZona) throws SystemException {
        try {
            return bolped_service.getCalendarioDespachoByFecha(fecha, semana, anio, idZona);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getCalendarioDespacho)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param l
     * @return
     */
    public List clienteGetUltComprasInternet(long idCliente) throws SystemException {
        List result = new ArrayList();
        try {
            result = cli_service.ultComprasGetListInternetFO(idCliente);

        } catch (Exception ex) {
            logger.error("Problemas con ultimas compras internet", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * @param l
     * @return
     */
    public List clienteMisListas(long idCliente) throws SystemException {
        List result = new ArrayList();
        try {
            result = cli_service.clienteMisListasFO(idCliente);

        } catch (Exception ex) {
            logger.error("Problemas con Mis listas", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * @param l
     * @return
     */
    public List clienteMisListasPredefinidas() throws SystemException {
        List result = new ArrayList();
        try {
            result = cli_service.clienteMisListasPredefinidasFO();

        } catch (Exception ex) {
            logger.error("Problemas con Mis listas predefinidas", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * @param l
     * @return
     */
    public boolean clienteTieneSustitutos(long idCliente) throws SystemException {
        try {
            return cli_service.clienteTieneSustitutosFO(idCliente);
        } catch (Exception ex) {
            logger.error("Problemas con clienteTieneSustitutos", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param esResumen
     * @param l
     * @return
     */
    public List productosSustitutosPorCategoria(long idCliente, boolean esResumen) throws SystemException {
        try {
            return pro_service.productosSustitutosPorCategoriaFO(idCliente, esResumen);
        } catch (Exception ex) {
            logger.error("Problemas con productosSustitutosPorCategoria", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @param sustitutosPorCategorias
     */
    public void updateSustitutosCliente(Long idCliente, List sustitutosPorCategorias) throws SystemException {
        try {
            cli_service.updateSustitutosClienteFO(idCliente, sustitutosPorCategorias);
        } catch (Exception ex) {
            logger.error("Problemas con updateSustitutosCliente", ex);
            throw new SystemException(ex);
        }        
    }

    /**
     * @param idCliente
     * @param criteriosProductos
     * @param idSession
     */
    public void guardaCriteriosMiCarro(Long idCliente, List criteriosProductos, String idSession) throws SystemException {
        try {
            cli_service.guardaCriteriosMiCarroFO(idCliente, criteriosProductos, idSession);
        } catch (Exception ex) {
            logger.error("Problemas con guardaCriteriosMiCarro", ex);
            throw new SystemException(ex);
        }        
    }
    
    
    /**
     * @param id_cliente
     * @return
     */
    public List productosSustitutosByCliente(long idCliente) throws SystemException {
        try {
            return pro_service.productosSustitutosByClienteFO(idCliente);
        } catch (Exception ex) {
            logger.error("Problemas con productosSustitutosByCliente", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param id_cliente
     * @param prodsNuevos
     */
    public void addSustitutosCliente(long idCliente, List prodsNuevos) throws SystemException {
        try {
            cli_service.addSustitutosClienteFO(idCliente, prodsNuevos);
        } catch (Exception ex) {
            logger.error("Problemas con addSustitutosCliente", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param l
     * @return
     */
    public boolean clienteHaComprado(long idCliente) throws SystemException {
        try {
            return cli_service.clienteHaCompradoFO(idCliente);
        } catch (Exception ex) {
            logger.error("Problemas con clienteHaComprado", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param l
     * @param sustitutosPorCategorias
     */
    public void updateCriterioSustitucionEnPedido(long idPedido, List sustitutosPorCategorias) throws SystemException {
        try {
            bolped_service.updateCriterioSustitucionEnPedido(idPedido, sustitutosPorCategorias);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (updateCriterioSustitucionEnPedido)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idTipoGrupo
     * @return
     */
    public ListaTipoGrupoDTO getTipoGrupoById(int idTipoGrupo) throws SystemException {
        try {
            return bolped_service.getTipoGrupoById(idTipoGrupo);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (updateCriterioSustitucionEnPedido)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idTipoGrupo
     * @return
     */
    public List getGruposDeListasByTipo(int idTipoGrupo) throws SystemException {
        try {
            return bolped_service.getGruposDeListasByTipo(idTipoGrupo);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (updateCriterioSustitucionEnPedido)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idGrupo
     * @return
     */
    public List listasEspecialesByGrupo(long idGrupo, int idLocal) throws SystemException {
        List result = new ArrayList();
        try {
            result = cli_service.listasEspecialesByGrupoFO(idGrupo, idLocal);

        } catch (Exception ex) {
            logger.error("Problemas con listasEspecialesByGrupo", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * @param tupla
     * @param string
     * @return
     */
    public List clientesGetProductosListasEspeciales(String listas, String local, long idCliente) throws SystemException {
        List result = new ArrayList();
        try {
            result = cli_service.clientesGetProductosListasEspecialesFO(listas, local, idCliente);
        } catch (Exception ex) {
            logger.error("Problemas con clientesGetProductosListasEspeciales ", ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * @return
     */
    public List localesRetiro() throws SystemException {
        try {
            return bolped_service.localesRetiro();
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (localesRetiro)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param l
     * @return
     */
    public LocalDTO getLocalRetiro(long idLocal) throws SystemException {
        try {
            return bolped_service.getLocalRetiro(idLocal);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getLocalRetiro)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param l
     * @param m
     * @return
     */
    public DireccionesDTO clienteGetDireccion(long idDireccion) throws SystemException {
            try {
                return cli_service.clienteGetDireccionFO(idDireccion);
            } catch (Exception ex) {
                logger.error("Problemas con controles de clientes", ex);
                throw new SystemException(ex);
            }
    }

    /**
     * @param idZona
     * @return
     */
    public boolean zonaEsRetiroLocal(long idZona) throws SystemException {
        try {
            return bolped_service.zonaEsRetiroLocal(idZona);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (zonaEsRetiroLocal)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param l
     * @return
     */
    public boolean clienteTieneDisponibleRetirarEnLocal(long idCliente) throws SystemException {
        try {
            return cli_service.clienteTieneDisponibleRetirarEnLocalFO(idCliente);
        } catch (Exception ex) {
            logger.error("Problemas con controles de clientes", ex);
            throw new SystemException(ex);
        }
    }
    
    /**
     * @param listaProductos
     * @param idLocal
     * @return
     */
    public Hashtable getProductosDestacadosDeHoy(int idLocal) throws SystemException {
        try{
            return pro_service.getProductosDestacadosDeHoy(idLocal);
        } catch (Exception ex) {
            logger.error("Error en getProductosDestacadosDeHoy " + ex.getMessage());
            throw new SystemException(ex);
        }
    }

    /**
     * @param rut
     * @return
     */
    public boolean clienteEsConfiable(long rut) throws SystemException {
        try{
            return clientesService.clienteEsConfiable(rut);
        } catch (Exception ex) {
            logger.error("Error en clienteEsConfiable " + ex.getMessage());
            throw new SystemException(ex);
        }
    }

    /**
     * @return
     */
    public Date fechaActualBD() throws SystemException {
        try {
            return bolped_service.fechaActualBD();
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (zonaEsRetiroLocal)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @param idProducto
     * @return
     */
    public CriterioClienteSustitutoDTO criterioSustitucionByClienteProducto(long idCliente, long idProducto) throws SystemException {
        try {
            return cli_service.criterioSustitucionByClienteProductoFO(idCliente, idProducto);
        } catch (Exception ex) {
            logger.error("Problemas con criterioSustitucionByClienteProducto", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @param criterio
     */
    public void setCriterioCliente(long idCliente, CriterioClienteSustitutoDTO criterio) throws SystemException {
        try {
            cli_service.setCriterioClienteFO(idCliente, criterio);
        } catch (Exception ex) {
            logger.error("Problemas con setCriterioCliente", ex);
            throw new SystemException(ex);
        }        
    }

    /**
     * @param idCliente
     * @param criterio
     */
    public void addSustitutoCliente(long idCliente, CriterioClienteSustitutoDTO criterio) throws SystemException {
        try {
            cli_service.addSustitutoClienteFO(idCliente, criterio);
        } catch (Exception ex) {
            logger.error("Problemas con addSustitutoCliente", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param cliente_id
     * @return
     */
    public PedidoDTO getUltimaCompraClienteConDespacho(long cliente_id) throws FuncionalException {
        try {
            return bolped_service.getUltimaCompraClienteConDespacho(cliente_id);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (getUltimaCompraClienteConDespacho)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (getUltimaCompraClienteConDespacho)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param cliente_id
     * @return
     */
    public PedidoDTO getUltimaCompraClienteConRetiro(long cliente_id) throws FuncionalException {
        try {
            return bolped_service.getUltimaCompraClienteConRetiro(cliente_id);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (getUltimaCompraClienteConRetiro)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (getUltimaCompraClienteConRetiro)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param rut
     * @return
     */
    public boolean tieneEventosActivosConValidacionManual(long clienteRut) throws SystemException {
        try {
            return eventoService.tieneEventosActivosConValidacionManual(clienteRut);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (tieneEventosActivosConValidacionManual)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean tieneDireccionesConCobertura(long idCliente) throws SystemException {
       try {
            return cli_service.tieneDireccionesConCoberturaFO( idCliente );
        } catch (Exception ex) {
            logger.error("Problemas con controles de clientes", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteAllDireccionesConCobertura(long idCliente) throws SystemException {
        try {
            return cli_service.clienteAllDireccionesConCoberturaFO(idCliente);
        } catch (Exception ex) {
            logger.error("Problemas con controles de clientes", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public boolean clienteConOPsTracking(long idCliente) throws SystemException {
        try {
            return cli_service.clienteConOPsTrackingFO(idCliente);
        } catch (Exception ex) {
            logger.error("Problemas con clienteConOPsTracking", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idCliente
     * @return
     */
    public List clienteListaOPsTracking(long idCliente) throws SystemException {
        try {
            return cli_service.clienteListaOPsTrackingFO(idCliente);
        } catch (Exception ex) {
            logger.error("Problemas con clienteListaOPsTracking", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param l
     * @return
     */
    public List clienteComprasEnLocal(long rut) throws SystemException {
        try {
            return cli_service.clienteComprasEnLocalFO(rut);
        } catch (Exception ex) {
            logger.error("Problemas con clienteComprasEnLocal", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public List getProductosSolicitadosById(long id_pedido) throws FuncionalException {
        try {
            return bolped_service.getProductosSolicitadosById( id_pedido );
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (getProductosSolicitadosById)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (getProductosSolicitadosById)", e);
            throw new FuncionalException(e);
        } 
    }

    /**
     * @param i
     * @return
     */
    public RegionesDTO getRegionById(int idRegion) throws SystemException {
        try {
            return reg_service.getRegionById(idRegion);
        } catch (Exception ex) {
            logger.error("Problemas con servicios de regiones", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param email
     */
    public void addMailHome(String email) throws SystemException, DuplicateKeyException {
        try {
            cli_service.addMailHomeFO(email);
        } catch (DuplicateKeyException ex) {
            logger.info("Problema BizDelegate (addMailHome)"+ex.getMessage());
            throw new DuplicateKeyException(ex);
            
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (addMailHome)"+ex.getMessage());
            throw new SystemException(ex);
            
        }
    }

    /**
     * @return
     */
    public List getProductosCarruselActivos() throws FuncionalException {
        try {
            return bolped_service.getProductosCarruselActivos( );
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (getProductosCarruselActivos)", e);
            throw new FuncionalException(e);
        }
    }


    /**
     * @return
     */
    public boolean AlmacenaConfirmacionMapa(double lat, double lng, long dir_id) throws FuncionalException {
        try {
            return cli_service.AlmacenaConfirmacionMapaFO(lat, lng, dir_id);
        } catch (Exception e) {
            logger.error("Problema BizDelegate (getProductosCarruselActivos)", e);
            throw new FuncionalException(e);
        }
    }

    public void eliminaSuscripcionEncuestaByRut(long rut) throws FuncionalException {
        try {
            cli_service.eliminaSuscripcionEncuestaByRutFO(rut);
        } catch (Exception e) {
            logger.error("Problema BizDelegate (eliminaSuscripcionEncuestaByRut)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param prod_id
     * @return
     */
    public List getPilasNutricionalesByProductoFO(long prod_id) throws FuncionalException {
        try {
            return bolped_service.getPilasNutricionalesByProductoFO(prod_id);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (getPilasNutricionalesByProductoFO)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (getPilasNutricionalesByProductoFO)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param idUnidadPila
     * @return
     */
    public PilaUnidadDTO getPilaUnidadById(long idUnidadPila) throws FuncionalException {
        try {
            return bolped_service.getPilaUnidadById(idUnidadPila);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (getPilaUnidadById)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (getPilaUnidadById)", e);
            throw new FuncionalException(e);
        }
    }


   /**
    * @param localId
    * @param categoriaId
    * @param clienteId
    * @return
    * @throws SystemException
    */
   public List productosMasVendidos(int localId, int categoriaId, int clienteId, String idSession) throws SystemException {
      try {
         return pro_service.productosMasVendidos(localId, categoriaId, clienteId, idSession);
     } catch (Exception ex) {
         throw new SystemException(ex);
     }
   }

   /**
    * @param categoriaId
    * @return
    * @throws SystemException
    */
   public CategoriaMasvDTO categoriaMasv(int categoriaId) throws SystemException {
      try {
         return cat_service.getMasvCategoria(categoriaId);
     } catch (cl.bbr.jumbocl.common.exceptions.ServiceException ex) {
         throw new SystemException(ex);
     }
   }
   
   /**
    * @param categoriaId
    * @return
    * @throws SystemException
    */
   public CategoriaBannerDTO categoriaBanner(int categoriaId) throws SystemException {
      try {
         return cat_service.getBannersCategoria(categoriaId);
     } catch (cl.bbr.jumbocl.common.exceptions.ServiceException ex) {
         throw new SystemException(ex);
     }
   }

    /**
     * @return
     */
    public boolean verificaHoraCompra(long idJdespacho, String tipoDespacho) throws FuncionalException {
        try {
            return bolped_service.verificaHoraCompra(idJdespacho, tipoDespacho);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (verificaHoraCompra)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (verificaHoraCompra)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param idPedido
     * @param esClienteConfiable
     */
    public void ingresarPedidoASistema(long idPedido, boolean esClienteConfiable, long cliente) throws FuncionalException {
        try {
            bolped_service.ingresarPedidoASistema(idPedido, esClienteConfiable, cliente);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (ingresarPedidoASistema)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (ingresarPedidoASistema)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param webpayDTO
     */
    public void webpaySave(WebpayDTO webpayDTO) throws FuncionalException {
        try {
            bolped_service.webpaySave(webpayDTO);            
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (webpaySave)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (webpaySave)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param tbk_orden_compra
     * @return
     */
    public int webpayMonto(int idPedido) throws FuncionalException {
        try {
            return bolped_service.webpayMonto(idPedido);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (webpayMonto)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (webpayMonto)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public WebpayDTO webpayGetPedido(long idPedido) throws FuncionalException {
        try {
            return bolped_service.webpayGetPedido(idPedido);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (webpayGetPedido)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (webpayGetPedido)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param tbk_orden_compra
     * @return
     */
    public int webpayGetEstado(int idPedido) throws FuncionalException {
        try {
            return bolped_service.webpayGetEstado(idPedido);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (webpayGetEstado)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (webpayGetEstado)", e);
            throw new FuncionalException(e);
        }
    }
    
    /**
     * @param idZona
     * @param string
     * @param cantProductos
     * @return
     */
    public long getJornadaDespachoMayorCapacidad(long idZona, String fechaDespacho, long cantProductos) throws SystemException {
        try {
            return bolped_service.getJornadaDespachoMayorCapacidad(idZona, fechaDespacho, cantProductos);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate (getCalendarioDespachoByDias)", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetByPedido(long idPedido) throws FuncionalException {
        try {
            return bolped_service.botonPagoGetByPedido(idPedido);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (botonPagoGetByPedido)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (botonPagoGetByPedido)", e);
            throw new FuncionalException(e);
        }
    }

	/**
	 * Actualiza registro de Boton de pago CAT
	 */
	public boolean updateNotificacionBotonPago(BotonPagoDTO bp) throws FuncionalException {
		try {
			return bolped_service.updateNotificacionBotonPago(bp);
		} catch (Exception e) {
			logger.error("Problema BizDelegate (updateNotificacionBotonPago)", e);
			throw new FuncionalException(e);
		}
	}

    /**
     * @param idPedido
     * @return
     */
    public boolean pedidoEsFonoCompra(int idPedido) throws FuncionalException {
        try {
            return bolped_service.pedidoEsFonoCompra(idPedido);
        } catch (Exception e) {
            logger.error("Problema BizDelegate (pedidoEsFonoCompra)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param pedidoOld
     */
    public void purgaPedidoPreIngresado(PedidoDTO pedido, long idCliente) throws FuncionalException {
        try {
            bolped_service.purgaPedidoPreIngresado(pedido, idCliente);
        } catch (Exception e) {
            logger.error("Problema BizDelegate (purgaPedidoPreIngresado)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @return
     */
    public List regionesConCobertura() throws SystemException {
        try {
            return reg_service.regionesConCobertura();
        } catch (Exception ex) {
            logger.error("Problemas en regionesConCobertura", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idRegion
     * @return
     */
    public List comunasConCoberturaByRegion(long idRegion) throws SystemException {
        try {
            return reg_service.comunasConCoberturaByRegion(idRegion);
        } catch (Exception ex) {
            logger.error("Problemas en comunasConCoberturaByRegion", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idComuna
     * @return
     */
    public ComunaDTO getComunaConLocal(long idComuna) throws SystemException {
        try {
            return reg_service.getComunaConLocal(idComuna);
        } catch (Exception ex) {
            logger.error("Problemas en getComunaConLocal", ex);
            throw new SystemException(ex);
        }
    }
    
    /**
     * @param idComuna
     * @return
     */
    public ComunaDTO getZonaxComuna(long idComuna) throws SystemException {
        try {
            return reg_service.getZonaxComuna(idComuna);
        } catch (Exception ex) {
            logger.error("Problemas en getZonaxComuna", ex);
            throw new SystemException(ex);
        }
    }

    /**
     * @param idProducto
     * @param idLocal
     * @return
     */
    public boolean existeProductoEnLocal(long idProducto, long idLocal) throws FuncionalException {
        try {
            return bolped_service.existeProductoEnLocal(idProducto, idLocal);
        } catch (Exception e) {
            logger.error("Problema BizDelegate (existeProductoEnLocal)", e);
            throw new FuncionalException(e);
        }
    }

    //Maxbell - Mejoras al catalogo interno 2014/06/30
    public ProductoStockDTO productoTieneStockEnLocal(long idProducto, long idLocal) throws FuncionalException {
        try {
            return bolped_service.productoTieneStockEnLocal(idProducto, idLocal);
        } catch (Exception e) {
            logger.error("Problema BizDelegate (productoTieneStockEnLocal)", e);
            throw new FuncionalException(e);
        }
    }

    /**
     * @param id
     * @param l
     * @return
     */
    public DireccionesDTO getDireccionClienteByComuna(long idCliente, long idComuna) throws SystemException {
        try {
            return cli_service.getDireccionClienteByComunaFO(idCliente, idComuna);
        } catch (Exception ex) {
            logger.error("Problemas con controles de clientes", ex);
            throw new SystemException(ex);
        }
    }

    public void convierteCarroDonaldAfterPago(long idCliente, String idSession, long idInvitado) throws SystemException {
        try {
            cli_service.convierteCarroDonaldAfterPagoFO(idCliente, idSession, idInvitado);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }
    
    public void convierteCarroDonald(long idCliente, String idSession) throws SystemException {
        try {
            cli_service.convierteCarroDonaldFO(idCliente, idSession);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }
    
    
    public boolean esPrimeraCompra(long idCliente) throws SystemException {
        try {
            return ped_service.esPrimeraCompra(idCliente);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }
    
    public void RecuperaClave_GuardaKeyCliente(long idCliente, String key) throws SystemException {
        try {
            cli_service.RecuperaClave_GuardaKeyClienteFO(idCliente, key);
        } catch (Exception ex) {
            logger.error("Problema RecuperaClave_GuardaKeyCliente ", ex);
            throw new SystemException(ex);
        }
    }
    

    public String RecuperaClave_getKeyCliente(long idCliente) throws SystemException {
        try {
            return cli_service.RecuperaClave_getKeyClienteFO(idCliente);
        } catch (Exception ex) {
            logger.error("Problema RecuperaClave_getKeyCliente ", ex);
            throw new SystemException(ex);
        }
    }
//  14112012 VMatheu  
    public ParametroFoDTO getParametroByKey(String key)  throws SystemException {
        try {
            return parametroService.getParametroByKey(key);
        } catch (Exception ex) {
            logger.error("Problema getParametroByKey ", ex);
            throw new SystemException(ex);
}
    }
  
    public ParametroFoDTO getParametroByID(int id)  throws SystemException {
        try {
            return parametroService.getParametroByID(id);
        } catch (Exception ex) {
            logger.error("Problema getParametroByID ", ex);
            throw new SystemException(ex);
        }
    }
// -- 14112012 VMatheu 
    
    public void setClienteFacebook(long id_cliente, String id_facebook) throws SystemException {
        try {
            cli_service.setClienteFacebookFO(id_cliente, id_facebook);
        } catch (Exception ex) {
            logger.error("Problema setClienteFace ", ex);
            throw new SystemException(ex);
        }
    }          
    
    public String clienteGetRutByIdFacebook(String idFacebook) throws SystemException {
        try {
            return cli_service.clienteGetRutByIdFacebookFO(idFacebook);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }
    
    public List getAlertaPedidoByKey(long id_pedido, int keyAlerta) throws FuncionalException {
        try {
            return ped_service.getAlertaPedidoByKey(id_pedido, keyAlerta);
        } catch (Exception ex) {
            logger.error("Problemas en getAlertaPedidoByKey", ex);
            throw new FuncionalException(ex);
        }
    }

    /**
     * Busca cupon de descuento en base al codigo
     * @param codigo
     * @return
     * @throws SystemException
     */
    public CuponDsctoDTO getCuponDscto(String codigo) throws SystemException{
    	try{
    		return cuponDsctoService.getCuponDscto(codigo);
    	} catch (Exception e) {
    		logger.error("Error (Biz) getCuponDscto:", e);
    		throw new SystemException(e);
    	}
    }
    
    public CuponDsctoDTO getCuponDsctoById(int idCupon) throws SystemException{
    	try{
    		return cuponDsctoService.getCuponDsctoById(idCupon);
    	} catch (Exception e) {
    		logger.error("Error (Biz) getCuponDscto:", e);
    		throw new SystemException(e);
    	}
    }
    

    /**
     * Verifica que cupon es para el cliente
     * @param rut
     * @param id_cupon
     * @return
     * @throws SystemException
     */
    public boolean isCuponForRut(long rut, long id_cupon) throws SystemException{
	    try{
	    	return cuponDsctoService.isCuponForRut(rut, id_cupon);
	    } catch (Exception ex) {
	        logger.error("Error (Biz) isCuponForRut: ", ex);
	        throw new SystemException(ex);
	    }
    	
    }
    
    /**
     * 
     * @param id_cupon
     * @param tipo
     * @return
     * @throws SystemException
     */
    public List getProdsCupon (long id_cupon, String tipo) throws SystemException {
	    try{
	    	return cuponDsctoService.getProdsCupon(id_cupon, tipo);
	    } catch (Exception ex) {
	        logger.error("Error (Biz) getProdsCupon: ", ex);
	        throw new SystemException(ex);
	    }
    }
    /**
     * 
     * @param idCupon
     * @return
     * @throws SystemException
     */
	public boolean descuentaStockCuponDescto(long idCupon) throws SystemException {
		try{
	    	return cuponDsctoService.dsctaStockCupon(idCupon);
	    } catch (Exception ex) {
	        logger.error("Error (Biz) descuentaStockCuponDescto: ", ex);
	        throw new SystemException(ex);
	    }
		
	}
	 /**
	 * Retorna item carro de compras del cliente filtro idProducto
     * @param cliente_id
     * @param local
     * @param idSession
     * @param idProducto
     * @return
     * @throws SystemException
     */
  
    public List getItemCarroComprasPorCategorias(long cliente_id, String local, String idSession, String idProducto) throws SystemException {
        try {
            return cli_service.getItemCarroComprasPorCategoriasFO(cliente_id, local, idSession, idProducto);
        } catch (Exception ex) {
            logger.error("Problemas getItemCarroComprasPorCategorias", ex);
            throw new SystemException(ex);
        }
    }
    
    /**
     * 
     * @param idCupon
     * @param idPedido
     * @return
     * @throws SystemException
     */
    public boolean setIdCuponIdPedido( long idCupon, long idPedido ) throws SystemException {
    	
		try {
	    
			return cuponDsctoService.setIdCuponIdPedido( idCupon, idPedido );
	    
		} catch ( Exception ex ) {
	     
			logger.error( "Error (Biz) setIdCuponIdPedido: ", ex );
	        throw new SystemException( ex );
	    
		}
		
	}
    
    
    /**
     * 
     * @param idPedido
     * @return
     * @throws FuncionalException
     */
     public PedidoDTO getValidaCuponYPromocionPorIdPedido( long idPedido ) throws FuncionalException {
    	 
     	PedidoDTO pedido = null;
         
     	try {
         
         	pedido = bolped_service.getValidaCuponYPromocionPorIdPedido( idPedido );
         
         } catch ( cl.bbr.jumbocl.common.exceptions.ServiceException e ) {
        
        	 logger.error( "Problema BizDelegate (doInsPedido)", e );
             throw new FuncionalException( e );
         
         }
         
         return pedido;
     }
     
     public int getIdCuponByIdPedido( long idPedido ) throws FuncionalException {
      	int idCupon = 0;  
      	try {
          	idCupon = bolped_service.getIdCuponByIdPedido(idPedido);
          } catch ( cl.bbr.jumbocl.common.exceptions.ServiceException e ) {
         	 logger.error( "Problema BizDelegate (doInsPedido)", e );
              throw new FuncionalException( e );          
          }
          return idCupon;
      }
     
     /**
      * 
      * @param pedido_id
      * @return
      * @throws FuncionalException
      */
     public List getDescuentosAplicados( long pedido_id ) throws FuncionalException {
         
    	 try {
         
    		 return bolped_service.getDescuentosAplicados( pedido_id );
         
    	 } catch ( cl.bbr.jumbocl.common.exceptions.ServiceException e ) {
          
    		 logger.error( "Problema BizDelegate (doInsPedido)", e );
             throw new FuncionalException( e );
         
    	 }
     
     }
     
     
     /**
      * 
      * @param log
      * @throws ServiceException
      * @throws SystemException
      */
     public void addLogPedido ( LogPedidoDTO log ) throws ServiceException, SystemException {

    	 PedidosCtrl pedidos = new PedidosCtrl();
     	
    	 try {
     	
    		 try {
     		
    			 pedidos.addLogPedido( log );
     			
    		 } catch ( cl.bbr.jumbocl.common.exceptions.SystemException e ) {
     		
    			 logger.debug( "Problemas con controles de pedidos" );
     			
    		 }
     		
    	 } catch ( PedidosException ex ) {
     	
    		 logger.debug( "Problemas con controles de pedidos" );
     		 throw new ServiceException( ex.getMessage() );
     		
    	 }
     
     }
     
    /**
     * @param id_pedido
     * @param key_validacion
     * @return
     */
    public boolean verificaAlertaValidacion(long id_pedido, String key_validacion) throws SystemException {
        try {
            return bolped_service.verificaAlertaValidacion(id_pedido, key_validacion);
        } catch (Exception e) {
            logger.error("Problema BizDelegate (verificaAlertaValidacion)", e);
            throw new SystemException(e);
        }
    }
    
    public boolean verificaPrimeraCompraRetiroEnLocal(long id_pedido) throws SystemException {
        try {
            return bolped_service.verificaPrimeraCompraRetiroEnLocal(id_pedido);
        } catch (Exception e) {
            logger.error("Problema BizDelegate (verificaAlertaValidacion)", e);
            throw new SystemException(e);
        }
    }
    
    public void updatePedidoInvitado(int idpedido, long rut, String dv, String nombre,
			String apellido_pat) throws SystemException {
		
		
		 try {
	            ped_service.updatePedidoInvitado(idpedido, rut, dv, nombre, apellido_pat);
	        } catch (Exception ex) {
	            logger.error("Problemas ", ex);
	            throw new SystemException(ex);
	        }
		
		
		
	}
     
    //Maxbell - Inconsistencias v3
    public int actualizarCapacidadOcupadaPicking(long id_jpicking) throws SystemException {
    	try {
			return bolped_service.actualizarCapacidadOcupadaPicking(id_jpicking);
		} catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
			// TODO Bloque catch generado automáticamente
			throw new SystemException(e);
		}
    }
     
    
    //-- Carro Abandonado
    

    public void updateFechaMiCarro(int clienteId) throws SystemException {
        try{
            pro_service.updateFechaMiCarro(clienteId);
        } catch (Exception ex) {
            logger.error("Error en updateFechaMiCarro " + ex.getMessage());
            throw new SystemException(ex);
        }
    }
    
    /**
     * Busca email con cupon de descuento en base al id de mail
     * @param codigo
     * @return
     * @throws SystemException
     */
    public CarroAbandonadoDTO getCuponCarroAbandonado(int codigo) throws SystemException{
    	try{
    		return cuponDsctoService.getCuponCarroAbandonado(codigo);
    	} catch (Exception e) {
    		logger.error("Error (Biz) getCuponCarroAbandonado:", e);
    		throw new SystemException(e);
    	}
    }
    
    //-- Fin Carro Abandonado
    
    /**
     * Registrar datos Cliente formulario kcc
     * 
     * @param dataClienteKcc
     * @return
     * @throws SystemException
     */
    public boolean addDataClienteKcc(ClienteKccDTO dataClienteKcc) throws SystemException {
 
    	try {
            return promociones_service.addDataClienteKcc(dataClienteKcc);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }
    
    public boolean addDataClientePR(ClientePRDTO dataClientePR) throws SystemException {
   	 
    	try {
            return promociones_service.addDataClientePR(dataClientePR);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }
    
  public boolean getClientePRByRut(String rut, String dv) throws SystemException {
    	
    	try {
            return promociones_service.getClientePRByRut(rut, dv);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }
     
    /**
     * Valida Cliente kcc existente
     * 
     * @param rut
     * @param dv
     * @return
     * @throws SystemException
     */
    public boolean getClienteKccByRut(String rut, String dv) throws SystemException {
    	
    	try {
            return promociones_service.getClienteKccByRut(rut, dv);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
    }
    
    /**(Catalogo Externo) - NSepulveda
     * Metodo que se encarga de recuperar la categoria padre, intermedia y terminal del ultimo producto 
     * agregado al carro de compras. (Aligare - Nelson Sepulveda 07/08/2014).  
     * @param idProducto <code>long<code> Identificador del producto.
     * @return <code>ProductoCatalogoExternoDTO</code>
     * @throws FuncionalException
     */
    public ProductoCatalogoExternoDTO getCatUltimoProductoCatalogoExterno(long idProducto) throws FuncionalException{
    	try{
    		return bolped_service.getCatUltimoProductoCatalogoExterno(idProducto);
    	}catch (Exception e) {
            logger.error("Problema BizDelegate (getCatUltimoProductoCatalogoExterno)", e);
            throw new FuncionalException(e);
        }
    }
    
    /**(Catalogo Externo) - NSepulveda
     * Metodo que se encarga de validar los productos solicitados desde el catalogo externo 
     * para posteriormente agregarlos al carro de compras. 
     * @param listCatalogoExt <code>List</code> Listado de productos a validar.
     * @return <code>List</code> Listado de <code>ProductoCatalogoExternoDTO</code> con los productos validados.
     * @throws FuncionalException
     */
    public List getValidacionProductosCatalogoExterno(List listCatalogoExt) throws FuncionalException{
    	try{
    		return bolped_service.getValidacionProductosCatalogoExterno(listCatalogoExt);
    	}catch(Exception e){
    		logger.error("Problema BizDelegate (getValidacionProductosCatalogoExterno)", e);
            throw new FuncionalException(e);
    	}
    }
	
		/**
	 * Obtiene los datos de la ficha del producto según codigo producto
	 * @param codProd
	 * @return
	 * @throws BocException
	 */
	public List getFichaProductoPorId(long codProd) throws SystemException {
		try {
			return pro_service.getFichaProductoById(codProd);
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new SystemException(ex);
		}
	}
	
	/**
	 * Obtiene todos los item de la ficha tecnica
	 * @return
	 * @throws BocException
	 */
	public List getItemFichaProductoAll() throws SystemException {
		try {
			return pro_service.getListItemFichaProductoAll();
		} catch (Exception ex) { // RemoteException ex
			// Translate the service exception into
			// application exception
			throw new SystemException(ex);
		}
	}
	
	/**
	 * Permite saber si el producto tiene ficha.
	 * 
	 * @param cod_prod
	 *            long
	 * @return boolean
	 * @throws BocException
	 */
	public boolean tieneFichaProductoById(long cod_prod) throws SystemException {
		try {
			return pro_service.tieneFichaProductoById(cod_prod);
		} catch (Exception ex) { // RemoteException ex
			throw new SystemException(ex);
		}
	}
	
	/**
	 * 
	 * @param cliente
	 * @return
	 * @throws SystemException
	 */
	public ClienteSG6DTO getClienteByRut(ClienteSG6DTO cliente) throws SystemException {
		try {
            return promociones_service.getClienteByRut(cliente);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
	}
	/**
	 * 
	 * @param llave
	 * @return
	 * @throws SystemException
	 */
	public ArrayList getModelosSamsung(String llave) throws SystemException {
		try {
            return promociones_service.getModelosSamsung(llave);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
	}
	
	/**
	 * 
	 * @param cliente
	 * @return
	 * @throws SystemException
	 */
	public int getReservasSamsungCliente(ClienteSG6DTO cliente)  throws SystemException {
		try {
            return promociones_service.getReservasSamsungCliente(cliente);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
	}
	/**
	 * 
	 * @param cliente
	 * @return
	 * @throws SystemException
	 */
	public boolean registrarReservaSamsung(ClienteSG6DTO cliente) throws SystemException {
			try {
	            return promociones_service.registrarReservaSamsung(cliente);
	        } catch (Exception ex) {
	            logger.error("Problema BizDelegate", ex);
	            throw new SystemException(ex);
	        }
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public ClienteSG6DTO getDireccionCliente(ClienteSG6DTO cliente) throws SystemException {
		try {
            return promociones_service.getDireccionCliente(cliente);
        } catch (Exception ex) {
            logger.error("Problema BizDelegate", ex);
            throw new SystemException(ex);
        }
	}
	
	 public DireccionMixDTO getDireccionIniciaSessionCliente(long idCliente)  throws SystemException{
		 
	        try {
	            return bolped_service.getDireccionIniciaSessionCliente(idCliente);
	        } catch (Exception e) {
	        	throw new SystemException(e.getMessage());
	        } 
	 }
	 
	 //Recupera clave
	 public void recuperaClaveFO(ClienteDTO cliente, String contextPath) throws SystemException{
		 try {
			 cli_service.recuperaClaveFO(cliente, contextPath);
		 } catch (Exception ex) {
			 logger.error( "Problemas recuperaClaveFO", ex);
			 throw new SystemException(ex);
		 }
	 }

	public DireccionesDTO crearDireccionDummy(DireccionesDTO d) throws SystemException {
		 try {
			 return cli_service.crearDireccionDummy(d);
		 } catch (Exception ex) {
			 logger.error( "Problemas recuperaClaveFO", ex);
			 throw new SystemException(ex);
		 }
	}
	public PagoGrabilityDTO getPagoByOP(long pedido_id) throws FuncionalException {

        try {
            return bolped_service.getPagoByOP(pedido_id);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (getPagoByOP)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (getPagoByOP)", e);
            throw new FuncionalException(e);
        }
    }
	public PagoGrabilityDTO getPagoByToken(String token) throws FuncionalException {

        try {
            return bolped_service.getPagoByToken(token);
        } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e) {
            logger.error("Problema BizDelegate (getPagoByOP)", e);
            throw new FuncionalException(e);
        } catch (cl.bbr.jumbocl.common.exceptions.SystemException e) {
            logger.error("Problema BizDelegate (getPagoByOP)", e);
            throw new FuncionalException(e);
        }
    }
	
	public void actualizaPagoGrabilityByOP (PagoGrabilityDTO pago) throws FuncionalException {
		try{
			bolped_service.actualizaPagoGrabilityByOP (pago);
		}catch (Exception e){
			logger.error("Problema BizDelegate (getPagoByOP)", e);
            throw new FuncionalException(e);
		}
	}
	
	public void insertaTrackingOP(long id_pedido, String user, String mensajeLog) throws FuncionalException {
		try {
			bolped_service.insertaTrackingOP(id_pedido, user, mensajeLog);
		} catch (Exception e) {
			logger.error("Problema BizDelegate (insertaTrackingOP)", e);
            throw new FuncionalException(e);
		}
	}
	
	public Map getParametroByNameIn(String name) throws Exception {
	        try {
	            return parametroService.getParametroByNameIn(name);
	        } catch (Exception ex) { // RemoteException ex
	            // Translate the service exception into
	            // application exception
	            throw new Exception(ex);
	        }
	    }
}
