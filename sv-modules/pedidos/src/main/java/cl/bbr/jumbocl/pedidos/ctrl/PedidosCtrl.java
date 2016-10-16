package cl.bbr.jumbocl.pedidos.ctrl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.jumbocl.bolsas.dao.JdbcBolsasDAO;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.common.dto.CarroCompraProductosDTO;
import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.dto.RegionDTO;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.common.model.CategoriaSapEntity;
import cl.bbr.jumbocl.common.model.ClienteEntity;
import cl.bbr.jumbocl.common.model.DetallePickingEntity;
import cl.bbr.jumbocl.common.model.DireccionEntity;
import cl.bbr.jumbocl.common.model.EmpresasEntity;
import cl.bbr.jumbocl.common.model.FacturaEntity;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.ProductoSapEntity;
import cl.bbr.jumbocl.common.model.ProductosPedidoEntity;
import cl.bbr.jumbocl.common.model.TrxMpDetalleEntity;
import cl.bbr.jumbocl.common.model.TrxMpEncabezadoEntity;
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.NumericUtils;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.contenidos.dao.JdbcProductosDAO;
import cl.bbr.jumbocl.eventos.dao.JdbcEventosDAO;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.exceptions.EventosDAOException;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.cat.CapturarCAT;
import cl.bbr.jumbocl.pedidos.cat.RptaCapturaCAT;
import cl.bbr.jumbocl.pedidos.cat.SolicCapturaCAT;
import cl.bbr.jumbocl.pedidos.cat.TarjetaCAT;
import cl.bbr.jumbocl.pedidos.cat.Validar;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDetalleFODTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModFacturaDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoIndicDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoPolSustDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModPedidoProdDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModTrxMPDetalleDTO;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcCalendarioDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcDespachosDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcJornadasDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcRondasDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcTrxMedioPagoDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcZonasDespachoDAO;
import cl.bbr.jumbocl.pedidos.dao.PedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.AlertaDTO;
import cl.bbr.jumbocl.pedidos.dto.AsignaOPDTO;
import cl.bbr.jumbocl.pedidos.dto.AvanceDTO;
import cl.bbr.jumbocl.pedidos.dto.BinCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.CambEnPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.ClientesDTO;
import cl.bbr.jumbocl.pedidos.dto.ComunaDTO;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.CriterioCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.CriterioSustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.CuponPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DatosMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.DireccionMixDTO;
import cl.bbr.jumbocl.pedidos.dto.EmpresaTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.ExtPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ExtProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.FacturaDTO;
import cl.bbr.jumbocl.pedidos.dto.FacturasDTO;
import cl.bbr.jumbocl.pedidos.dto.FaltanteDTO;
import cl.bbr.jumbocl.pedidos.dto.FonoTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.POSFeedbackProcPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.POSProductoDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoGrabilityDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoVentaMasivaDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoExtDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCotizacionesDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.PickingCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaProductoDTO;
import cl.bbr.jumbocl.pedidos.dto.PilaUnidadDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.pedidos.dto.PoliticaSustitucionDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductoCarruselDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PromoDetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RechPagoOPDTO;
import cl.bbr.jumbocl.pedidos.dto.SustitutoDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpDetalleDTO;
import cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.CalendarioDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.ClientesDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.ClientesException;
import cl.bbr.jumbocl.pedidos.exceptions.DespachosException;
import cl.bbr.jumbocl.pedidos.exceptions.JornadasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosException;
import cl.bbr.jumbocl.pedidos.exceptions.PoligonosException;
import cl.bbr.jumbocl.pedidos.exceptions.ProductosSapDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.RondasDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.TrxMedioPagoDAOException;
import cl.bbr.jumbocl.pedidos.promos.dto.CuponPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.DetPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PrioridadPromosDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ProductoPromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ProductosRelacionadosPromoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromoMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionesCriteriaDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ResumenPedidoPromocionDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.TcpPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.ClienteTcpPromosCuponesSaf;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.jumbocl.pedidos.util.FormatUtils;
import cl.bbr.jumbocl.pedidos.util.Parametros;
import cl.bbr.jumbocl.shared.emails.dao.JdbcEmailDAO;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.promo.lib.ctrl.PromoCtrl;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.ProductoCatalogoExternoDTO;
import cl.bbr.promo.lib.dto.ProductoDTO;
import cl.bbr.promo.lib.dto.ProductoStockDTO;
import cl.bbr.promo.lib.dto.ProrrateoProductoDTO;
import cl.bbr.promo.lib.dto.ProrrateoProductoSeccionDTO;
import cl.bbr.promo.lib.dto.ProrrateoPromocionProductoDTO;
import cl.bbr.promo.lib.dto.ProrrateoPromocionSeccionDTO;
import cl.bbr.promo.lib.dto.ProrrateoSeccionDTO;
import cl.bbr.promo.lib.dto.TcpClienteDTO;
import cl.bbr.promo.lib.exception.PromocionesException;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;

/**
 * Entrega metodos de navegacion por pedidos y busqueda en base a criterios. Los
 * resultados son listados de pedidos.
 * 
 * @author BBR
 *  
 */
public class PedidosCtrl {

    /**
     * Permite generar los eventos en un archivo log.
     */
    Logging logger = new Logging(this); 

    /**
     * Obtiene los datos del pedido, segun el <b>id </b> del pedido.
     * 
     * @param idPedido
     *            long
     * @return PedidoDTO, que contiene la información del pedido.
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error del sistema.
     *  
     */
	public PedidoDTO getPedidoById(long idPedido) throws PedidosException, SystemException {

        PedidoDTO ped = new PedidoDTO();
		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {

            //retorna EstadosDTO
            ped = dao.getPedidoById(idPedido);

            if (ped.getNum_mp() != null
                    && !"".equalsIgnoreCase(ped.getNum_mp())
                    && !"X".equalsIgnoreCase(ped.getNum_mp())
                    && !"null".equalsIgnoreCase(ped.getNum_mp())) {

                // Desencriptamos los 4 ult digitos del número de la tarjeta
                ResourceBundle rb = ResourceBundle.getBundle("bo");
                String key = rb.getString("conf.bo.key");
                String mp = Cifrador.desencriptar(key, ped.getNum_mp());
                ped.setNum_mp_unmask(mp);

                if (mp.length() > 4)
                    mp = "************" + mp.substring(mp.length() - 4);

                ped.setNum_mp(mp);
            }

        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }

        return ped;

    }

    /**
     * Obtiene listado del detalle del pedido, segun el <b>id </b> del pedido.
     * 
     * @param id_pedido
     *            long
     * @return List ProductosPedidoDTO, retorna la lista de detalle
     * @throws PedidosException,
     *             en el caso que no exista el pedido
     *  
     */
    public List getProductosPedido(long id_pedido) throws PedidosException {

        List lista_prod_entity = new ArrayList();

		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            DetallePedidoCriteriaDTO criteria = new DetallePedidoCriteriaDTO();
            criteria.setId_pedido(id_pedido);

            lista_prod_entity = dao.getProductosPedido(criteria);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        //return result;
        return lista_prod_entity;
    }

    /**
     * Obtiene el id del sector, segun el <b>id_prod </b> código del producto y
     * el <b>id_local </b> del local.
     * 
     * @param id_prod long
     * @return long, id del sector
     * @throws PedidosException,
     *             en el caso que no exista el sector.
     *  
     */
    public long getSectorByProdId(long id_prod) throws PedidosException {

        long res = -1;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {

            res = dao.getSectorByProdId(id_prod);

        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        //return result;
        return res;
    }

    /**
     * Permite agregar el producto en el pedido, segun la información del
     * detalle a agregar en el pedido. <br>
     * Se utiliza desde Jumbo BOC, en la vista del detalle del pedido.
     * 
     * @param prod
     *            ProductosPedidoDTO
     * @return boolean, devuelve <i>true </i> si no hay error, caso contrario,
     *         devuelve <i>false </i>.
     * @throws PedidosException,
     *             en el caso que exista error al insertar la información del
     *             nuevo detalle en la base de datos.
     *  
     */
    public boolean agregaProductoPedido(ProductosPedidoDTO prod) throws PedidosException {

        boolean res = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {

            res = (dao.agregaProductoPedido(prod) > 0);

        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        //return result;
        return res;
    }

    /**
     * Permite eliminar el detalle de un pedido. Se utiliza en el Jumbo BOC, en
     * la vista del detalle del pedido.
     * 
     * @param prod
     *            ProductosPedidoDTO
     * @return boolean, devuelve <i>true </i> si no hay error, caso contrario,
     *         devuelve <i>false </i>.
     * @throws PedidosException,
     *             en el caso que exista error al eliminar el detalle en la base
     *             de datos.
     *  
     */
    public boolean elimProductoPedido(ProductosPedidoDTO prod) throws PedidosException {

        boolean res = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {

            res = dao.elimProductoPedido(prod);

        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        //return result;
        return res;
    }

    /**
     * Obtiene listado de alertas del pedido, segun el <b>id </b> del pedido.
     * 
     * @param id_pedido
     *            long
     * @return List AlertaDTO
     * @throws PedidosException,
     *             en el caso que exista error al consultar las alertas.
     *  
     */
    public List getAlertasPedido(long id_pedido) throws PedidosException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            result = dao.getAlertasPedido(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Obtiene el listado de sustitutos de un pedido, segun el <b>id </b> del
     * pedido.
     * 
     * @param id_pedido
     *            long
     * @return List SustitutoDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getSustitutosByPedidoId(long id_pedido) throws PedidosException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            result = dao.getSustitutosByPedidoId(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Obtiene el listado de faltantes de un pedido, segun el <b>id </b> del
     * pedido.
     * 
     * @param id_pedido
     *            long
     * @return List FaltanteDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     */
    public List getFaltantesByPedidoId(long id_pedido) throws PedidosException {

        List faltantes = new ArrayList();
        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            faltantes = dao.getFaltantesByPedidoId(id_pedido);

            ProductosPedidoEntity prod = null;
            for (int i = 0; i < faltantes.size(); i++) {
                prod = null;
                prod = (ProductosPedidoEntity) faltantes.get(i);
                FaltanteDTO ft = new FaltanteDTO();
                ft.setCod_producto(prod.getCod_prod1());
                ft.setDescripcion(prod.getDescripcion());
                //ft.setCant_faltante((int)prod.getCant_faltan());
                ft.setCant_faltante(prod.getCant_faltan());
                ft.setId_pedido(id_pedido);
                ft.setUni_med(prod.getUni_med());
                ft.setPrecio(prod.getPrecio());
                ft.setIdCriterio(prod.getIdCriterio());
                ft.setDescCriterio(prod.getDescCriterio());
                //FaltanteDTO ft = new
                // FaltanteDTO(prod.getCod_prod1(),prod.getDescripcion(),(int)prod.getCant_solic(),
                // id_pedido);
                /*
                 * String cod_producto, String descripcion, int cant_faltante,
                 * long id_pedido ClientesDTO clidto = new
                 * ClientesDTO(cli.getId().longValue(),cli.getRut().longValue(),
                 * cli.getNombre(),cli.getApellido_pat(),cli.getApellido_mat(),
                 * FrmClientes.frmFecha(cli.getFec_nac().toString()),FrmClientes.frmCliEstado(cli.getEstado()));
                 */
                result.add(ft);
            }
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Retorna el listado de bins de un pedido, segun el <b>id </b> del pedido.
     * 
     * @param id_pedido
     *            long
     * @return List BinDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     */
    public List getBinsPedido(long id_pedido) throws PedidosException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        BinCriteriaDTO criterio = new BinCriteriaDTO();
        criterio.setId_pedido(id_pedido);

        try {
            return dao.getBinsByCriteria(criterio);
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }

    }

    /**
     * Retorna el listado de bins de un pedido, segun el <b>id </b> del pedido.
     * 
     * @param id_pedido
     *            long
     * @return List BinDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     */
    public List getBinsPedidoPKL(long id_pedido) throws PedidosException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        BinCriteriaDTO criterio = new BinCriteriaDTO();
        criterio.setId_pedido(id_pedido);

        try {
            return dao.getBinsByCriteriaPKL(criterio);
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }

    }

    /**
     * Obtiene el listado de log de eventos del pedido, segun el <b>id </b> del
     * pedido.
     * 
     * @param id_pedido
     *            long
     * @return List LogPedidoDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getLogPedido(long id_pedido) throws PedidosException {
        //List result = new ArrayList();

        try {
            JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
            return dao.getLogPedido(id_pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema PedidosCtrl:" + ex.getMessage());
            throw new PedidosException(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
    }

    
    public HashMap getListadoOP(Calendar cal, int id_local, String hora_desp) throws PedidosException {
        try {
            JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
            return dao.getListadoOP(cal, id_local, hora_desp);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema PedidosCtrl:" + ex.getMessage());
            throw new PedidosException(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
    }

    /**
     * Obtiene la cantidad de pedidos, segun el <b>PedidosCriteriaDTO </b> el
     * cual contiene los criterios de consulta <br>
     * (id del pedido, rut o apellido del cliente, fechas de inicio y fin de
     * picking, fechas de inicio y fin de despacho, <br>
     * estado, subestado)
     * 
     * @param pedido
     *            PedidosCriteriaDTO
     * @return int cantidad
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public int getCountPedidosByCriteria(PedidosCriteriaDTO pedido) throws PedidosException {
        int res = -1;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            //retorna EstadosDTO
            res = (int) dao.getCountPedidosByCriteria(pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
        return res;
    }

    /**
     * Obtiene la lista de Pedidos, segun el <b>PedidosCriteriaDTO </b> el cual
     * contiene los criterios de consulta <br>
     * (id del pedido, rut o apellido del cliente, fechas de inicio y fin de
     * picking, fechas de inicio y fin de despacho, <br>
     * estado, subestado)
     * 
     * @param pedido
     *            PedidosCriteriaDTO
     * @return List MonitorPedidoDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getPedidosByCriteria(PedidosCriteriaDTO pedido) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPedidosByCriteria(pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
    }
    
    /**
     * Obtiene la lista de Pedidos, segun el <b>PedidosCriteriaDTO </b> el cual
     * contiene los criterios de consulta <br>
     * (id del pedido, rut o apellido del cliente, fechas de inicio y fin de
     * picking, fechas de inicio y fin de despacho, <br>
     * estado, subestado)
     * 
     * @param pedido
     *            PedidosCriteriaDTO
     * @return List MonitorPedidoDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getPedidosByCriteriaHomologacion(PedidosCriteriaDTO pedido) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPedidosByCriteriaHomologacion(pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
    }

    /**
     * Obtiene la lista de Pedidos, segun el <b>PedidosCriteriaDTO </b> el cual
     * contiene los criterios de consulta <br>
     * (id del pedido, rut o apellido del cliente, fechas de inicio y fin de
     * picking, fechas de inicio y fin de despacho, <br>
     * estado, subestado)
     * 
     * @param pedido
     *            PedidosCriteriaDTO
     * @return List MonitorPedidoDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getPedidosXJornada(PedidosCriteriaDTO criterio) throws PedidosException {

        List pedidos = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {

            pedidos = dao.getPedidosXJornada(criterio);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
        return pedidos;

    }

    /**
     * Obtiene la lista de Pedidos, segun el <b>PedidosCriteriaDTO </b> el cual
     * contiene los criterios de consulta <br>
     * (id del pedido, rut o apellido del cliente, fechas de inicio y fin de
     * picking, fechas de inicio y fin de despacho)
     * 
     * @param pedido
     *            PedidosCriteriaDTO
     * @return List MonitorPedidoDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getPedidosByFecha(PedidosCriteriaDTO pedido) throws PedidosException {

        List pedidos = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            pedidos = dao.getPedidosByFecha(pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
        return pedidos;

    }

    /**
     * Obtiene la lista de Pedidos, segun la <b>Jornada de Picking </b> <br>
     * 
     * @param jpicking
     * @return List Pedidos
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getPedidosByJornadaPicking(long jpicking) throws PedidosException {

        List pedidos = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            pedidos = dao.getPedidoByJornadaPicking(jpicking);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
        return pedidos;

    }

    /**
     * Obtiene la cantidad de Pedidos, segun el <b>PedidosCriteriaDTO </b> el
     * cual contiene los criterios de consulta <br>
     * (id del pedido, rut o apellido del cliente, fechas de inicio y fin de
     * picking, fechas de inicio y fin de despacho)
     * 
     * @param pedido
     *            PedidosCriteriaDTO
     * @return int cantidad de pedidos
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public int getCountPedidosByFecha(PedidosCriteriaDTO pedido) throws PedidosException {
        int res = -1;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            res = (int) dao.getCountPedidosByFecha(pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
        return res;
    }

    /**
     * Obtiene el listado de los estados del pedido, que se muestran en el
     * filtro de estados, en la vista de busqueda del pedido.
     * 
     * @return List EstadosDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getEstadosPedidoBOC() throws PedidosException {

        List lista = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            //retorna EstadosDTO
            lista = dao.getEstadosPedidoBOC();
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }
        return lista;
    }

    /**
     * Obtiene el listado de motivos del pedido.
     * 
     * @return List MotivoDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getMotivosPedidoBOC() throws PedidosException {

        List lista = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            lista = dao.getMotivosPedidoBOC();
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }
        return lista;
    }

    /**
     * Obtiene listado de estados de pedido
     * 
     * @return List EstadosDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getEstadosPedido() throws PedidosException {

        List lista = new ArrayList();

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            //retorna EstadosDTO
            lista = dao.getEstadosPedido();

        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }

        return lista;
    }

    /**
     * Obtiene el estados de un pedido
     * 
     * @return EstadosDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public EstadoDTO getEstadoPedido(long IdPedido) throws PedidosException {

        EstadoDTO estado = new EstadoDTO();

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            //retorna EstadosDTO
            estado = dao.getEstadoPedido(IdPedido);

        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }

        return estado;
    }

    /**
     * Obtiene el estados de una TrxMp
     * 
     * @return EstadosDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public EstadoDTO getEstadoTrxMp(long IdTrxMp) throws PedidosException {

        EstadoDTO estado = new EstadoDTO();

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            //retorna EstadosDTO
            estado = dao.getEstadoTrxMp(IdTrxMp);

        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }

        return estado;
    }

    /**
     * Agrega un registro al log del Pedido, segun información contenida en
     * <b>LogPedidoDTO </b>.
     * 
     * @param log
     *            LogPedidoDTO
     * 
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     *  
     */
    public void addLogPedido(LogPedidoDTO log) throws PedidosException, SystemException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            dao.addLogPedido(log);
        } catch (PedidosDAOException ex) {
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                throw new PedidosException(Constantes._EX_PED_ID_INVALIDO);
            }
            throw new SystemException("Error no controlado al insertar log pedido", ex);
        }

    }

    /**
     * Obtiene el listado de productos de un bin, segun <b>id </b> del bin.
     * 
     * @param id_bp
     *            long
     * @return List ProductosBinDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     */
    public List getProductosBin(long id_bp) throws PedidosException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        PickingCriteriaDTO criterio = new PickingCriteriaDTO();
        criterio.setId_bp(id_bp);

        try {
            return dao.getPickingPedidoByCriteria(criterio);
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }

        /*
         * List lista = new ArrayList();
         * 
         * ProductosBinDTO prod1 = new ProductosBinDTO(); prod1.setCantidad(1);
         * prod1.setCod_producto("123432"); prod1.setDescripcion("Galletas");
         * prod1.setId_bin(22233); lista.add(prod1);
         * 
         * return lista;
         *  
         */
    }

    /**
     * Revisa la op si esta asignada a un ejecutivo <br>
     * Si la OP no esta asignada :<br>- asigna la op al usuario que hace la
     * edición <br>- anota en el log la asignacion <br>- redirecciona la
     * pagina al formulario de ordenes <br>
     * Si la OP ya está asignada :<br>- si ya está asignada despliega el
     * mensaje en el monitor de op <br>- "La OP XXXXX esta asignada al usuario
     * YYYY" <br>
     * 
     * Nota: Este método tiene <b>Transaccionalidad </b>.
     * 
     * @param dto
     *            AsignaOPDTO
     * @return boolean, devuelve <i>true </i> en el caso que la asignación fue
     *         satisfactoria, caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en el caso q no se cumplan ciertas condiciones: <br>- El
     *             usuario tiene OP asignada, <br>- La OP tiene otro usuario,
     *             <br>- No existe la OP.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public boolean setAsignaOP(AsignaOPDTO dto) throws PedidosException, SystemException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        boolean result = false;

        // Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();

        // Iniciamos trx
        try {
            trx1.begin();
        } catch (Exception e1) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        }

        // Marcamos los dao's con la transacción
        try {
            dao.setTrx(trx1);
        } catch (PedidosDAOException e2) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos");
        }

        try {
            //return dao.setAsignaOP(col);
            /*
             * 
             * setAsignaOP : metodo que asigna el usuario a la orden de pedido.
             */

            //Si el usuario ya tiene un pedido asignado no puede tomar otro
            if ((dto.getId_pedido_usr_act() > 0) && (dto.getId_pedido_usr_act() != dto.getId_pedido())) {
                trx1.rollback();
                throw new PedidosException(Constantes._EX_OPE_USR_TIENE_OTRO_PED);
                //logger.debug("El usuario "+usr.getLogin()+" debe primero
                // liberar la OP:"+usr.getId_pedido());
                //url =
                // paramUrlfracaso+"?mensaje_error="+mensaje_fracaso2+usr.getId_pedido();
            }
            //si el usuario es identico al que esta asignado, no hace nada, lo
            // pasa
            // si son distintos evia el mensaje de error
            long id_usuario_pedido = dao.getPedidoById(dto.getId_pedido()).getId_usuario();
            if ((id_usuario_pedido > 0) && (dto.getId_usuario() != id_usuario_pedido)) {
                trx1.rollback();
                throw new PedidosException(Constantes._EX_OPE_TIENE_OTRO_USR);
                //logger.debug("La OP :"+paramId_op+" pertenece al usuario
                // :"+paramId_usuario_ped);
                //url =
                // paramUrlfracaso+"?mensaje_error="+mensaje_fracaso+usr.getLogin();
            }
            if (id_usuario_pedido <= 0) {

                //si no esta asignado lo asigna si ya esta asignado es a el
                // mismo, salta a la url
                result = dao.setAsignaOP(dto);
                if (result) {
                    //usr.setId_pedido(paramId_op);
                    logger.debug("Se ha asignado la orden " + dto.getId_pedido() + " al usuario:" + dto.getUsr_login());
                    //El log pasa por abrir un popup al usuario para que
                    // anote la observacion y el motivo
                    LogPedidoDTO log = new LogPedidoDTO();
                    log.setId_pedido(dto.getId_pedido());
                    log.setUsuario(dto.getUsr_login());
                    log.setLog(dto.getLog());
                    dao.addLogPedido(log);
                    //url= paramUrl+"&mod=1";
                } else {
                    logger.debug("No se ha logrado asignar la OP:" + dto.getId_pedido() + " al usuario:"
                            + dto.getUsr_login());
                    //url =
                    // paramUrlfracaso+"?mensaje_error="+mensaje_fracaso+usr.getLogin();
                }
            } else {
                logger.debug("La OP ya esta asignada al usuario");
                //url= paramUrl+"&mod=1";
            }

        } catch (PedidosDAOException ex) {
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
            }
            throw new SystemException("Error no controlado ", ex);
        } catch (DAOException e) {
            logger.error("Error al hacer rollback");
            throw new SystemException("Error al hacer rollback");
        }
        //		 cerramos trx
        try {
            trx1.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }
        return result;
    }

    /**
     * Permite liberar una OP. <br>
     * Actualiza la informacion en base de datos, las tablas de pedidos y
     * usuarios. <br>
     * Nota: Este método tiene <b>Transaccionalidad </b>.
     * 
     * @param col
     *            AsignaOPDTO
     * @return boolean, devuelve <i>true </i> en el caso que la liberación fue
     *         satisfactoria, caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en el caso q exista error en la actualización en base de
     *             datos.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public boolean setLiberaOP(AsignaOPDTO col) throws PedidosException, SystemException {
        boolean result = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        //		 Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();

        //		 Iniciamos trx
        try {
            trx1.begin();
        } catch (Exception e1) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        }

        try {
            dao.setTrx(trx1);
        } catch (PedidosDAOException e2) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos");
        }

        try {
            result = dao.setLiberaOP(col);
        } catch (PedidosDAOException e) {
            //				rollback trx
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }
            e.printStackTrace();
            throw new PedidosException(e);
        }
        //			 cerramos trx
        try {
            trx1.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }
        return result;

    }

    /**
     * Realiza el proceso de validación de la OP. Las definiciones de alertas se
     * encuentran en la tabla BO_ALERTAS <br>
     * 
     * Reglas de validación: <br>
     * 
     * 1. Productos sin sector de picking asociado <br>
     * 2. Primera Compra Web <br>
     * 3. Cliente cambia de Apellido <br>
     * 4. Nueva dirección de despacho <br>
     * 5. Medio de pago sin cupo (no implementado) <br>
     * 6. Medio de pago bloqueado (no implementado) <br>
     * 7. Monto máximo sobrepasado <br>
     * 8. Cliente Bloqueado <br>
     * 9. Fecha y Hora de Despacho no razonable <br>
     * 10. Fecha y Hora de Picking atrasada <br>
     * 11. Compra con factura <br>
     * 12. Dirección Nueva y Dirección con más de un local de cobertura <br>
     * 13. Reagendar Despacho <br>
     * 14. Pago con tarjetas CAT <br>
     * 15. Medio de pago alternativo (CAT DUMMY) <br>
     * Alertas para pedidos Venta Empresa 16. Cantidades de productos excedidas
     * 17. Forma de pago, con linea de credito 18. Zona SPOT. Nota: Este Método
     * tiene <b>Transaccionalidad </b>.
     * 
     * 
     * @param id_pedido
     *            long
     * @return boolean devuelve <i>true </i> en caso que el proceso de
     *         validación fue satisactoria, caso contrario devuelve <i>false
     *         </i>
     * @throws PedidosException,
     *             en uno de los siguientes casos: <br>- La OP no existe. <br>-
     *             La relación entre el pedido y la alerta ya existia y no puede
     *             insertarse nuevamente <br>
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
	public boolean setValidaOP(long id_pedido) throws PedidosException,
			SystemException {

		PedidoDTO pedido = null;
		// obtiene datos del pedido
		pedido = this.getPedidoById(id_pedido);

		if (pedido == null) {
			logger.info("Pedido no existe");
			throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
		}

		JdbcPedidosDAO daoPedidos = (JdbcPedidosDAO) DAOFactory.getDAOFactory(
				DAOFactory.JDBC).getPedidosDAO();
		JdbcJornadasDAO daoJornadas = (JdbcJornadasDAO) DAOFactory
				.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
		JdbcEventosDAO daoEventos = (JdbcEventosDAO) DAOFactory.getDAOFactory(
				DAOFactory.JDBC).getEventosDAO();

		boolean res = false;

		logger.debug("Inicio Proceso de Validación de OP...");

		// Creamos trx
		JdbcTransaccion trx1 = new JdbcTransaccion();
		// Iniciamos trx
		try {
			trx1.begin();
			daoPedidos.setTrx(trx1);
			daoJornadas.setTrx(trx1);
			daoEventos.setTrx(trx1);
		} catch (Exception e) {
			logger.error("Error al iniciar transacción");
			throw new SystemException("Error al iniciar transacción", e);
		} /*catch (PedidosDAOException e) {
			logger.error("Error al asignar transacción al dao Pedidos");
			throw new SystemException(
					"Error al asignar transacción al dao Pedidos", e);
		} catch (JornadasDAOException e) {
			logger.error("Error al asignar transacción al dao Jornadas");
			throw new SystemException(
					"Error al asignar transacción al dao Jornadas", e);
		} catch (EventosDAOException e) {
			logger.error("Error al asignar transacción al dao Eventos");
			throw new SystemException(
					"Error al asignar transacción al dao Jornadas", e);
		}*/

		try {
			// eliminar alertas del pedido
			boolean eliminoAlertas = daoPedidos.elimAlertaByPedido(id_pedido);

			if (eliminoAlertas) {
				// variables utilizadas en este método
				long id_alerta = 0;
				// PedidoDTO pedido = dao.getPedidoById(id_pedido);
				// long id_local = pedido.getId_local();

				List lst_det = daoPedidos.getDetallesPedido(id_pedido);
				// boolean existePrimCompra = false;

				logger.debug("Inicia revision de alertas");

				// Obtiene validaciones activas..
				// 20121005_AG
				List lst_alertsActivas = daoPedidos.getAlertasActivas();
				// _20121005_AG

				/*
				 * Evento Personalizado Revisa si el pedido esta relacionado a
				 * un evento personalizado (alerta 30)
				 */
				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_EVENTO_PERSONALIZADO,
						lst_alertsActivas)) {// alerta activa
					EventoDTO evento = daoEventos.getEventoByPedido(
							pedido.getRut_cliente(), pedido.getId_pedido());
					logger.info("Validador de Eventos... evento.getIdEvento(): "
							+ evento.getIdEvento());
					if (evento.getIdEvento() != 0
							&& evento.getValidacionManual().equalsIgnoreCase(
									"S")) {
						id_alerta = Constantes.ALE_EVENTO_PERSONALIZADO;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta de Eventos Personalizados");
					}
					// _20121005_AG
				}

				/*
				 * 1. Productos sin sector de picking asociado (alerta 11 BD)
				 * 
				 * Revisa si cada producto tiene sector de picking asociado, en
				 * caso que alguno no tenga sector asociado, lo actualiza
				 */
				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_PEDIDO_SIN_SECTOR,
						lst_alertsActivas)) {// alerta activa
					boolean encontro = false;
					boolean sin_sector = false;
					int cantProds = 0;
					String productos_sin_sector = "";
					for (int i = 0; i < lst_det.size(); i++) {
						encontro = false;
						ProductosPedidoDTO prodped = (ProductosPedidoDTO) lst_det
								.get(i);
						if (prodped.getId_sector() == 0) {
							long id_sector = daoPedidos
									.getSectorByProdId(prodped.getId_producto());
							if (id_sector != 0) {
								encontro = daoPedidos.actSectorDetalle(
										prodped.getId_detalle(), id_sector);
							}
							if (!encontro) {
								if (!productos_sin_sector.equals("")) {
									productos_sin_sector += " -";
								}
								productos_sin_sector += " "
										+ prodped.getId_producto();
								sin_sector = true;
								cantProds++;
								if (cantProds == 20) {
									// registrar en el log los productos que no
									// tiene sector
									LogPedidoDTO log = new LogPedidoDTO();
									log.setId_pedido(id_pedido);
									log.setUsuario("SYSTEM");
									log.setLog("Productos sin sector:"
											+ productos_sin_sector);
									daoPedidos.addLogPedido(log);
									// inicializar las variables
									cantProds = 0;
									productos_sin_sector = "";
								}
							}
						}
					}
					if (cantProds > 0) {
						// registrar en el log los productos que no tiene sector
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(id_pedido);
						log.setUsuario("SYSTEM");
						log.setLog("Productos sin sector:"
								+ productos_sin_sector);
						daoPedidos.addLogPedido(log);
					}
					// registrar la alerta
					if (sin_sector) {
						id_alerta = Constantes.ALE_PEDIDO_SIN_SECTOR;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta Pedido sin Sector");
					}

					if (cantProds > 0) {
						// registrar en el log los productos que no tiene sector
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(id_pedido);
						log.setUsuario("SYSTEM");
						log.setLog("Productos sin sector:"
								+ productos_sin_sector);
						daoPedidos.addLogPedido(log);
					}
					// registrar la alerta
					if (sin_sector) {
						id_alerta = Constantes.ALE_PEDIDO_SIN_SECTOR;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta Pedido sin Sector");
					}
					// _20121005_AG
				}
				/*
				 * 2. Primera Compra (alerta 1 BD)
				 * 
				 * Revisa si es el primer pedido del cliente (segun los estados
				 * de los pedidos considerados en compra web)
				 */
				/*
				 * PedidosCriteriaDTO criterio = new PedidosCriteriaDTO();
				 * criterio.setId_cliente(pedido.getId_cliente());
				 * criterio.setFiltro_estados
				 * (Constantes.ESTADOS_PEDIDO_COMPRA_WEB); List pedidos =
				 * daoPedidos
				 * .getPedidosByCriteria(criterio);//dao.getPedidosByEstados
				 * (pedido.getId_cliente()); if (pedidos.size() == 0) {
				 * id_alerta = Constantes.ALE_PRIMERA_COMPRA_WEB;
				 * daoPedidos.addAlertToPedido(id_pedido, id_alerta);
				 * logger.info("Alerta Primera Compra"); //existePrimCompra =
				 * true; }
				 */
				try {
					if (daoPedidos.enValidacionManual(pedido.getId_pedido(),
							Constantes.KEY_VALIDACION_MANUAL_OP)) {
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(pedido.getId_pedido());
						log.setUsuario("SYSTEM");
						daoPedidos.addAlertToPedido(pedido.getId_pedido(),
								Constantes.ALE_PRODUCTO_CATEGORIA_VALIDACION);
					}
				} catch (Exception e) {
					;// si falla no importa, que se pasen la alerta por el .....
				}

				try {
					if (daoPedidos.esEscolares(pedido.getId_pedido())) {
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(pedido.getId_pedido());
						log.setUsuario("SYSTEM");
						daoPedidos.addAlertToPedido(pedido.getId_pedido(),
								Constantes.ALE_ARTICULO_ESCOLAR);
					}
				} catch (Exception e) {
					;// si falla no importa, que se pasen la alerta por el .....
				}

				/*
				 * J: Se elimina alerta primera compra
				 */
				// 20121008_AG
				if (isAlertaActiva(Constantes.ALE_PRIMERA_COMPRA_WEB,
						lst_alertsActivas)) {// alerta activa
					if (daoPedidos.esPrimeraCompra(pedido.getId_cliente())) {
						Double montoPrimeraCompra = new Double(Parametros
								.getInstance().getValor(
										"MONTO_VALIDA_PRIMERA_COMPRA"));
						if (pedido.getMonto() >= montoPrimeraCompra
								.doubleValue()) {
							LogPedidoDTO log = new LogPedidoDTO();
							log.setId_pedido(pedido.getId_pedido());
							log.setUsuario("SYSTEM");
							/*
							 * Si es Invitado -no es Primera Compra -
							 * Validalacion Manual
							 */
							if (pedido.getRut_cliente() == Long
									.parseLong(Constantes.CLIENTE_INVITADO)
									|| pedido.getNom_cliente().equals(
											"Invitado")) {
								daoPedidos.addAlertToPedido(
										pedido.getId_pedido(),
										Constantes.ALE_CLIENTE_INVITADO);
							} else {
								daoPedidos.addAlertToPedido(
										pedido.getId_pedido(),
										Constantes.ALE_PRIMERA_COMPRA_WEB);
							}

							if (daoPedidos.tieneProductosDeListaNegra(pedido
									.getId_pedido())) {
								daoPedidos
										.addAlertToPedido(
												pedido.getId_pedido(),
												Constantes.ALE_PROD_SUSCEPTIBLE_A_FRAUDE);
							}
						}
					}
					// _20121008_AG
				}

				/*
				 * 3. Cliente cambia de apellido (alerta 2 BD)
				 * 
				 * Verifica si el cliente ha modificado su apellido.
				 */
				ClienteEntity cliente = daoPedidos.getClienteById(pedido
						.getId_cliente());
				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_CAMBIA_APELLIDOS,
						lst_alertsActivas)) {// alerta activa
					logger.debug("cliente.getMod_dato():"
							+ cliente.getMod_dato());
					if (cliente.getMod_dato().equals(
							Constantes.CLI_MOD_APELLIDOS)) {
						id_alerta = Constantes.ALE_CAMBIA_APELLIDOS;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta Cliente cambia de apellido");
					}
					// _20121005_AG
				}

				/*
				 * 4. Nueva dirección (alerta 3 BD)
				 * 
				 * Verifica si la compra es con una dirección nueva
				 */
				/*
				 * Alerta de dirección nueva, sólo si el pedido es para despacho
				 * y no es una tarjeta cencosud. En validación tarjeta se valida
				 * dirección para tarjetas cencosud (mas abajo).
				 */
				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_DIRECC_NUEVA,
						lst_alertsActivas)) {// alerta activa
					if (!pedido.getTipo_despacho().equalsIgnoreCase("R")) {
						DireccionEntity dirPedido = daoPedidos
								.getDireccionById(pedido.getDir_id());
						/** Se elimina validación de medio de pago **
						if (!pedido.getMedio_pago().equals(
								Constantes.MEDIO_PAGO_CAT)
								&& !pedido.getMedio_pago().equals(
										Constantes.MEDIO_PAGO_PARIS)) {
						**/				
							if (Constantes.ESTADO_NUEVO
									.equalsIgnoreCase(dirPedido.getFnueva())) {
								id_alerta = Constantes.ALE_DIRECC_NUEVA;
								daoPedidos.addAlertToPedido(id_pedido,
										id_alerta);
								logger.info("Alerta Dirección nueva");
							}
						/** Se elimina validación de medio de pago **
						}
						**/
						/*
						 * if
						 * (Constantes.ESTADO_NUEVO.equalsIgnoreCase(dirPedido
						 * .getFnueva()) &&
						 * ComunaSusceptibleFraude.getInstance()
						 * .estaEnListaNegra(dirPedido.getCom_id().intValue())){
						 * daoPedidos.addAlertToPedido(id_pedido,
						 * Constantes.ALE_COMUNA_SUSCEPTIBLE_A_FRAUDE);
						 * logger.info("Alerta comuna susceptible a fraude"); }
						 */
					}
					// _20121005_AG
				}

				/*
				 * 7. Monto límite sobrepasado (aleta 6 BD)
				 * 
				 * Cuando se sobrepasa el monto límite de una compra
				 */
				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_SOBRE_MONTO_LIMITE,
						lst_alertsActivas)) {// alerta activa
					Double mon_limite = new Double(Parametros.getInstance()
							.getValor(Constantes.MONTO_LIMITE_OP));
					if (pedido.getMonto() > mon_limite.doubleValue()) {
						id_alerta = Constantes.ALE_SOBRE_MONTO_LIMITE;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta Monto límite sobrepasado");
					}
					// _20121005_AG
				}

				/*
				 * 8. Cliente Bloqueado por Jumbo CL (alerta 7 BD)
				 * 
				 * Si el cliente tiene el estado B:Bloqueado
				 */
				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_BLOQUEADO_X_JUMBOCL,
						lst_alertsActivas)) {// alerta activa
					logger.debug("cliente.getBloqueo():" + cliente.getBloqueo());
					if (cliente.getBloqueo().equals(Constantes.CLI_BLOQUEADO)) {
						id_alerta = Constantes.ALE_BLOQUEADO_X_JUMBOCL;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta Cliente Bloqueado");
					}
					// _20121005_AG
				}

				/*
				 * 9. Hora y Fecha despacho no razonable (alerta 8 BD)
				 * 
				 * alerta 8: Fecha y hora de despacho no es razonable alerta 12:
				 * fecha y hora de despacho atrasada
				 */
				// revisar que horas y fecha se comparan
				// Si el pedido posee despacho Express no muestra esta alerta
				long id_jornada = pedido.getId_jpicking();
				JornadaDTO jornada = daoJornadas.getJornadaById(id_jornada);
				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_FEC_HORA_DESPACHO,
						lst_alertsActivas)) {// alerta activa
					if (!pedido.getTipo_despacho().equals("E")) {
						try {
							// String fecHoraDespacho = pedido.getFdespacho()+"
							// "+pedido.getHfindespacho();
							String fecHoraDespacho = pedido.getFdespacho()
									+ " " + pedido.getHdespacho();
							logger.debug("fecha y hora despacho:"
									+ fecHoraDespacho);

							DateFormat formatter = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date dateDespacho = (Date) formatter
									.parse(fecHoraDespacho);
							// Date dateActual =
							// formatter.parse(formatter.format(new Date()));
							Calendar ahora = new GregorianCalendar();

							long tiempoDespacho = dateDespacho.getTime();
							// long tiempoActual = dateActual.getTime();
							long tiempoActual = ahora.getTimeInMillis();

							logger.debug("tiempoActual:" + tiempoActual);
							logger.debug("tiempoDespacho:" + tiempoDespacho);
							logger.debug("diferencia  :"
									+ (tiempoDespacho - tiempoActual));

							// el tiempo limite será la constante de diferencia
							// de
							// horas mas las horas de validación
							// que tiene la jornada de picking relacionada al
							// pedido.
							long tiempoLimite = Constantes.MAX_DIFERENCIA_HORAS_NORMAL
									+ jornada.getHrs_validacion()
									* Constantes.HORA_EN_MILI_SEG;
							logger.debug("tiempoLimite:" + tiempoLimite);

							if (tiempoDespacho - tiempoActual < tiempoLimite) {
								id_alerta = Constantes.ALE_FEC_HORA_DESPACHO;
								daoPedidos.addAlertToPedido(id_pedido,
										id_alerta);
								logger.info("Alerta Fecha y Hora de despacho no razonable");
							}

							/*
							 * // revision de ambas alertas, donde solo se
							 * comparaba con una constante igual para todos long
							 * tiempoLimite = Constantes.MIN_DIFERENCIA_HORAS;
							 * logger.debug("tiempoLimite:"+tiempoLimite);
							 * if(tiempoDespacho-tiempoActual <tiempoLimite){
							 * id_alerta = Constantes.ALE_FEC_HOR_DESP_ATRAS;
							 * dao.addAlertToPedido(id_pedido,id_alerta); }else{
							 * tiempoLimite = Constantes.MAX_DIFERENCIA_HORAS;
							 * logger.debug("tiempoLimite:"+tiempoLimite);
							 * if(tiempoDespacho-tiempoActual <tiempoLimite){
							 * id_alerta = Constantes.ALE_FEC_HORA_DESPACHO;
							 * dao.addAlertToPedido(id_pedido,id_alerta); } }
							 */

						} catch (Exception e) {
							logger.error("exc:" + e.getMessage());
							try {
								trx1.rollback();
							} catch (DAOException e1) {
								logger.error("Error al hacer rollback");
								throw new SystemException(
										"Error al hacer rollback");
							}
							throw new SystemException("Error no controlado", e);
						}
					}
					// _20121005_AG
				}

				/*
				 * 10. Fecha y Hora de Picking atrasada (alerta 12 BD)
				 * 
				 * Revision de la fecha hora de picking atrasada, segun el
				 * id_jornada de picking del pedido se obtiene las horas de
				 * validacion.
				 */
				// revision
				// obtener las horas de validacion
				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_FEC_HOR_PICK_ATRAS,
						lst_alertsActivas)) {// alerta activa
					long tiempoLimite = 0;
					if (pedido.getTipo_despacho().equals("E")) {
						tiempoLimite = Constantes.HORAS_VALIDACION_EXPRESS
								* Constantes.HORA_EN_MILI_SEG;
					} else {
						tiempoLimite = jornada.getHrs_validacion()
								* Constantes.HORA_EN_MILI_SEG;
					}
					try {
						// String fecHoraPicking = pedido.getFpicking()+"
						// "+pedido.getHfinpicking();
						String fecHoraPicking = pedido.getFpicking() + " "
								+ pedido.getHpicking();

						logger.debug("fecha y hora picking:" + fecHoraPicking);
						logger.debug("hrs_validacion:"
								+ jornada.getHrs_validacion());

						DateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date datePicking = (Date) formatter
								.parse(fecHoraPicking);
						// Date dateActual =
						// formatter.parse(formatter.format(new
						// Date()));
						Calendar ahora = new GregorianCalendar();

						long tiempoPicking = datePicking.getTime();
						// long tiempoActual = dateActual.getTime();
						long tiempoActual = ahora.getTimeInMillis();

						logger.debug("tiempoActual:" + tiempoActual);
						logger.debug("tiempoDespacho:" + tiempoPicking);
						logger.debug("diferencia  :"
								+ (tiempoPicking - tiempoActual));
						logger.debug("tiempoLimite:" + tiempoLimite);

						if ((tiempoPicking - tiempoActual) < tiempoLimite) {
							id_alerta = Constantes.ALE_FEC_HOR_PICK_ATRAS;
							daoPedidos.addAlertToPedido(id_pedido, id_alerta);
							logger.info("Alerta Fecha y Hora de validación atrasada");
						}
					} catch (Exception e) {
						logger.error("exc:" + e.getMessage());
						try {
							trx1.rollback();
						} catch (DAOException e1) {
							logger.error("Error al hacer rollback");
							throw new SystemException("Error al hacer rollback");
						}
						throw new SystemException("Error no controlado", e);
					}

					try {
						// String fecHoraPicking = pedido.getFpicking()+"
						// "+pedido.getHfinpicking();
						String fecHoraPicking = pedido.getFpicking() + " "
								+ pedido.getHpicking();

						logger.debug("fecha y hora picking:" + fecHoraPicking);
						logger.debug("hrs_validacion:"
								+ jornada.getHrs_validacion());

						DateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date datePicking = (Date) formatter
								.parse(fecHoraPicking);
						// Date dateActual =
						// formatter.parse(formatter.format(new
						// Date()));
						Calendar ahora = new GregorianCalendar();

						long tiempoPicking = datePicking.getTime();
						// long tiempoActual = dateActual.getTime();
						long tiempoActual = ahora.getTimeInMillis();

						logger.debug("tiempoActual:" + tiempoActual);
						logger.debug("tiempoDespacho:" + tiempoPicking);
						logger.debug("diferencia  :"
								+ (tiempoPicking - tiempoActual));
						logger.debug("tiempoLimite:" + tiempoLimite);

						if ((tiempoPicking - tiempoActual) < tiempoLimite) {
							id_alerta = Constantes.ALE_FEC_HOR_PICK_ATRAS;
							daoPedidos.addAlertToPedido(id_pedido, id_alerta);
							logger.info("Alerta Fecha y Hora de validación atrasada");
						}
					} catch (Exception e) {
						logger.error("exc:" + e.getMessage());
						try {
							trx1.rollback();
						} catch (DAOException e1) {
							logger.error("Error al hacer rollback");
							throw new SystemException("Error al hacer rollback");
						}
						throw new SystemException("Error no controlado", e);
					}

				}
				/*
				 * 11. Compra con factura (alerta 10 BD)
				 * 
				 * Si se compra con factura, entonces genera una alerta
				 */
				// revisa el tipo de documento es Factura o Boleta

				if (isAlertaActiva(Constantes.ALE_COMPRA_CON_FACTURA,
						lst_alertsActivas)) {// alerta activa
					if (pedido.getTipo_doc()
							.equals(Constantes.TIPO_DOC_FACTURA)) {
						id_alerta = Constantes.ALE_COMPRA_CON_FACTURA;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta compra con factura");
					}

				}

				/*
				 * 12. Alerta por Dirección Nueva y Dirección con más de un
				 * local de cobertura (alerta 15)
				 */
				/*
				 * if (!pedido.getTipo_despacho().equalsIgnoreCase("R")) {
				 * DireccionEntity dirPedido =
				 * daoPedidos.getDireccionById(pedido.getDir_id()); long
				 * id_comuna = dirPedido.getCom_id().longValue(); if
				 * (((daoPedidos.getLocalesByIdComuna(id_comuna)) > 1) &&
				 * existeDirNueva) { id_alerta =
				 * Constantes.ALE_DIR_NVA_VARIAS_ZONAS;
				 * daoPedidos.addAlertToPedido(id_pedido, id_alerta);
				 * logger.info
				 * ("Alerta dirección nueva con más de un local de cobertura");
				 * } }
				 */

				/*
				 * 13. Reprogramar Despacho por cambio de Zona (alerta XX)
				 */

				if (isAlertaActiva(Constantes.ALE_CAMBIO_ZONA_DESPACHO,
						lst_alertsActivas)) {// alerta activa
					// rescatamos la jornada de despacho que referencia el
					// pedido
					JornadaDespachoEntity jor = daoJornadas
							.getJornadaDespachoById(pedido.getId_jdespacho());

					if (jor.getId_zona() != pedido.getId_zona()) {
						id_alerta = Constantes.ALE_CAMBIO_ZONA_DESPACHO;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta Reprogramar Despacho por cambio de Zona");
					}

				}

				/*
				 * Alerta blanda indicando cuando cliente pago con CAT y medio
				 * no autenticado
				 */

				if (isAlertaActiva(Constantes.ALE_PAGO_NO_AUTENTICADO,
						lst_alertsActivas)) {// alerta activa
					boolean alert = false;
					if (Constantes.TIPO_TARJETA_CAT.equalsIgnoreCase(pedido
							.getMedio_pago())) {
						BotonPagoDTO bp = daoPedidos
								.botonPagoGetByPedidoAprobado(id_pedido);
						if ("N".equalsIgnoreCase(bp.getClienteValidado())) {
							id_alerta = Constantes.ALE_PAGO_NO_AUTENTICADO;
							daoPedidos.addAlertToPedido(id_pedido, id_alerta);
							logger.info("Alerta Medio de pago no autenticado");
							alert = true;
						}
					}
					if (!alert) {
						if (Constantes.ORIGEN_VE_CTE.equalsIgnoreCase(pedido
								.getOrigen())
								|| pedido.getId_usuario_fono() != 0) {
							id_alerta = Constantes.ALE_PAGO_NO_AUTENTICADO;
							daoPedidos.addAlertToPedido(id_pedido, id_alerta);
							logger.info("Alerta Medio de pago no autenticado");
						}
					}

					if (!alert) {
						if (Constantes.ORIGEN_VE_CTE.equalsIgnoreCase(pedido
								.getOrigen())
								|| pedido.getId_usuario_fono() != 0) {
							id_alerta = Constantes.ALE_PAGO_NO_AUTENTICADO;
							daoPedidos.addAlertToPedido(id_pedido, id_alerta);
							logger.info("Alerta Medio de pago no autenticado");
						}
					}

				}
				/*
				 * 15. Pago con tarjeta CAT DUMMY (1111-1111-1111-1111) La
				 * identificamos como "Medio de Pago Alternativo" (alerta 15)
				 */
				/*
				 * CTA: se comenta por cambio a boton de pago if
				 * (pedido.getMedio_pago().equals(Constantes.TIPO_TARJETA_CAT)
				 * &&
				 * pedido.getNum_mp_unmask().equals(Constantes.TARJETA_CAT_DUMMY
				 * )) { id_alerta = Constantes.ALE_MED_PAGO_ALTERNATIVO;
				 * daoPedidos.addAlertToPedido(id_pedido, id_alerta);
				 * logger.info("Alerta Medio de pago Alternativo"); }
				 */

				/** ************************************************ */
				/*
				 * 25. Pago con TARJETA PARIS (alerta 25)
				 */
				/*
				 * CTA: se comenta por cambio a boton de pago if
				 * (pedido.getMedio_pago() != null &&
				 * pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_PARIS)) {
				 * id_alerta = Constantes.ALE_MEDIO_PAGO_PARIS;
				 * daoPedidos.addAlertToPedido(id_pedido, id_alerta);
				 * logger.info("Alerta Medio de Pago PARIS"); }
				 */

				// Validación tarjetas cencosud y direccion nueva
				/*
				 * CTA: se comenta por cambio a boton de pago if
				 * (Constantes.MEDIO_PAGO_CAT.equals(pedido.getMedio_pago()) ||
				 * Constantes.MEDIO_PAGO_PARIS.equals(pedido.getMedio_pago())) {
				 * validaSaldoYBloqueo(pedido, daoPedidos); }
				 */
				// FIN VALIDACIÓN MEDIOS DE PAGO
				// ////////////////////////////////////////////////////////////////////

				/*
				 * 26. Poligono 0 (alerta 35 BD)
				 * 
				 * Esta Alerta Verifica:
				 * 
				 * - Que la dirección este asociada al Poligono Cero de su
				 * Comuna - Que la Cantidad de Poligonos, en la Comuna a la que
				 * pertenece la direción, sea mayor a cero, es decir, que la
				 * comuna tenga asociado a alguna zona de despacho más de 1
				 * poligono y que estos sean distintos al Poligono 0 (base) y al
				 * Poligono 999 (Retiro en local). NOTA: esta alerta no es
				 * realizada para los pedidos con Tipo de Despacho igual a "R"
				 * (Retiro en Local)
				 */

				if (isAlertaActiva(Constantes.ALE_POLIGONO_CERO,
						lst_alertsActivas)) {// alerta activa
					if (!pedido.getTipo_despacho().equals("R")) {
						PoligonosCtrl pol = new PoligonosCtrl();
						PoligonoDTO bean_pol = pol
								.getPoligonoByIdDireccion(pedido.getDir_id());
						long cant_pol = pol
								.getCantidadPoligonoDifBaseyRetiroLocal(pedido
										.getId_comuna());

						if (bean_pol.getNum_poligono() == 0 && cant_pol > 0) {
							id_alerta = Constantes.ALE_POLIGONO_CERO;
							daoPedidos.addAlertToPedido(id_pedido, id_alerta);
							logger.info("Alerta Polígono Cero");
						}
					}
					// _20121005_AG
				}

				/*
				 * Revision de Alertas solo para pedidos de origen Venta Empresa
				 */
				if (pedido.getOrigen().equals(Constantes.ORIGEN_VE_CTE)) {
					validarVentaEmpresa(id_pedido, pedido, daoPedidos, lst_det,
							cliente);
				}

				/*
				 * Revision de alertas para promociones
				 */
				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_RECALC_PROMO_ELIM_PROD,
						lst_alertsActivas)) {// alerta activa
					if (pedido.isFlg_recalc_prod()) {
						id_alerta = Constantes.ALE_RECALC_PROMO_ELIM_PROD;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta Recalculo de Promociones por eliminacion de producto");
					}
					// _20121005_AG
				}

				// 20121005_AG
				if (isAlertaActiva(Constantes.ALE_RECALC_PROMO_CAMBIO_MPAGO,
						lst_alertsActivas)) {// alerta activa
					if (pedido.isFlg_recalc_mp()) {
						id_alerta = Constantes.ALE_RECALC_PROMO_CAMBIO_MPAGO;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta Recalculo de Promociones por cambio de medio de pago");
					}
				}
				// _20121005_AG

				if (isAlertaActiva(Constantes.ALE_PRIMERA_COMPRA_RETIRO_EN_LOCAL,
						lst_alertsActivas)) {// alerta activa
					if (verificaPrimeraCompraRetiroEnLocal(id_pedido)) {
						id_alerta = Constantes.ALE_PRIMERA_COMPRA_RETIRO_EN_LOCAL;
						daoPedidos.addAlertToPedido(id_pedido, id_alerta);
						logger.info("Alerta primera compra con retiro en local");
					}
				}
				
				
				
				
				
				logger.debug("Finaliza revision de alertas");

				/*
				 * La consulta de las alertas activas se realizará en
				 * setConfirmarOP if(!dao.getExisteAlertaActiva(id_pedido)){ res
				 * = true; }
				 */
				res = true;
			}

		} catch (PedidosDAOException ex) {
			// rollback trx
			ex.printStackTrace();
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}

			logger.debug("Problema :" + ex);
			if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
				logger.debug("no existe cod del pedido");
				throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
			}
			if (ex.getMessage().equals(DbSQLCode.SQL_DUP_KEY_CODE)) {
				logger.debug("existe el alerta en el pedido");
				throw new PedidosException(
						Constantes._EX_OPE_REL_ALERTA_EXISTE, ex);
			}
			throw new SystemException("Error no controlado", ex);
		} catch (ClientesDAOException ex) {
			// rollback trx
			ex.printStackTrace();
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("Problema :" + ex);
			if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
				logger.debug("no existe cod del cliente");
				throw new PedidosException(Constantes._EX_CLI_ID_NO_EXISTE, ex);
			}
			throw new SystemException("Error no controlado", ex);
		} catch (JornadasDAOException ex) {
			// rollback trx
			ex.printStackTrace();
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("Problema :" + ex);
			throw new SystemException("Error no controlado", ex);
		} catch (EventosDAOException ex) {
			// rollback trx
			ex.printStackTrace();
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("Problema :" + ex);
			throw new SystemException("Error no controlado", ex);
		} catch (PoligonosException ex) {
			// rollback trx
			ex.printStackTrace();
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("Problema :" + ex);
			throw new SystemException("Error no controlado", ex);
		}/*
		 * catch (EmpresasDAOException ex) { //rollback trx try {
		 * trx1.rollback(); } catch (DAOException e1) { logger.error("Error al
		 * hacer rollback"); throw new SystemException("Error al hacer
		 * rollback"); } logger.debug("Problema :" + ex); throw new
		 * SystemException("Error no controlado",ex); }
		 */
        catch (ServiceException ex) {
			// TODO Bloque catch generado automáticamente
			try {
				trx1.rollback();
			} catch (DAOException e1) {
				logger.error("Error al hacer rollback");
				throw new SystemException("Error al hacer rollback");
			}
			logger.debug("Problema :" + ex);
			throw new SystemException("Error no controlado", ex);
		}
		// cerramos trx
		try {
			trx1.end();
		} catch (DAOException e) {
			logger.error("Error al finalizar transacción");
			e.printStackTrace();
			throw new SystemException("Error al finalizar transacción");
		}
		return res;
	}

    /**
     * Verifica si el id alerta existe en la lista de validaciones activas
     * Alvaro Gutierrez 05-10-2012
     * 
     * @param id_alerta
     * @param lst_alertsActivas
     * @return true si id_alerta esta en la list
     */
//20121005_AG    
    private boolean isAlertaActiva(long id_alerta, List lst_alertsActivas) {
        boolean isValid = false;
        Iterator it = lst_alertsActivas.iterator();
        while (it.hasNext()) {
            AlertaDTO oAlertaDTO = (AlertaDTO) it.next();
            if (oAlertaDTO.getId_alerta() == id_alerta) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }
//_20121005_AG     

    public boolean setValidacionClientesConfiables(long id_pedido) throws PedidosException, SystemException {

        PedidoDTO pedido = null;
        //obtiene datos del pedido
        pedido = this.getPedidoById(id_pedido);

        if (pedido == null) {
            logger.info("Pedido no existe");
            throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
        }

        JdbcPedidosDAO daoPedidos = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        JdbcJornadasDAO daoJornadas = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();

        boolean res = false;

        logger.debug("Inicio Proceso de Validación de Sectores de Picking para Productos");

        // Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();
        // Iniciamos trx
        try {
            trx1.begin();
            daoPedidos.setTrx(trx1);
            daoJornadas.setTrx(trx1);
        } catch (Exception e) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción", e);
        } /*catch (PedidosDAOException e) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos", e);
        } catch (JornadasDAOException e) {
            logger.error("Error al asignar transacción al dao Jornadas");
            throw new SystemException("Error al asignar transacción al dao Jornadas", e);
        }*/

        try {
            // eliminar alertas del pedido
            boolean eliminoAlertas = daoPedidos.elimAlertaByPedido(id_pedido);

            if (eliminoAlertas) {
                //variables utilizadas en este método
                long id_alerta = 0;
                //PedidoDTO pedido = dao.getPedidoById(id_pedido);
                //long id_local = pedido.getId_local();

                List lst_det = daoPedidos.getDetallesPedido(id_pedido);
                //boolean existePrimCompra = false;
                //boolean existeDirNueva = false;

                logger.debug("Inicia de Productos con Sector de Picking");

                /*
                 * 1. Productos sin sector de picking asociado (alerta 11 BD)
                 * 
                 * Revisa si cada producto tiene sector de picking asociado, en
                 * caso que alguno no tenga sector asociado, lo actualiza
                 *  
                 */
                //List lst_secXdet_pedido = daoPedidos.getSectorByDetPedido(id_pedido);
                boolean encontro = false;
                boolean sin_sector = false;
                int cantProds = 0;
                String productos_sin_sector = "";
                for (int i = 0; i < lst_det.size(); i++) {
                    encontro = false;
                    ProductosPedidoDTO prodped = (ProductosPedidoDTO) lst_det.get(i);
                    if (prodped.getId_sector() == 0) {
                        long id_sector = daoPedidos.getSectorByProdId(prodped.getId_producto());
                        if (id_sector != 0) {
                            encontro = daoPedidos.actSectorDetalle(prodped.getId_detalle(), id_sector);
                        }
                        /*for (int j = 0; j < lst_secXdet_pedido.size(); j++) {
                            ProductosPedidoDTO secXdet = (ProductosPedidoDTO) lst_secXdet_pedido.get(j);
                            if (prodped.getId_detalle() == secXdet.getId_detalle()) {
                                //actualiza el sector
                                encontro = daoPedidos.actSectorDetalle(prodped.getId_detalle(), secXdet.getId_sector());
                            }
                        }*/
                        if (!encontro) {
                            if (!productos_sin_sector.equals("")) {
                                productos_sin_sector += " -";
                            }
                            productos_sin_sector += " " + prodped.getId_producto();
                            sin_sector = true;
                            cantProds++;
                            if (cantProds == 20) {
                                //registrar en el log los productos que no
                                // tiene sector
                                LogPedidoDTO log = new LogPedidoDTO();
                                log.setId_pedido(id_pedido);
                                log.setUsuario("SYSTEM");
                                log.setLog("Productos sin sector:" + productos_sin_sector);
                                daoPedidos.addLogPedido(log);
                                //inicializar las variables
                                cantProds = 0;
                                productos_sin_sector = "";
                            }
                        }
                    }

                }
                if (cantProds > 0) {
                    //registrar en el log los productos que no tiene sector
                    LogPedidoDTO log = new LogPedidoDTO();
                    log.setId_pedido(id_pedido);
                    log.setUsuario("SYSTEM");
                    log.setLog("Productos sin sector:" + productos_sin_sector);
                    daoPedidos.addLogPedido(log);
                }
                //registrar la alerta
                if (sin_sector) {
                    id_alerta = Constantes.ALE_PEDIDO_SIN_SECTOR;
                    daoPedidos.addAlertToPedido(id_pedido, id_alerta);
                    logger.info("Alerta Pedido sin Sector");
                }

                /*
                 * 13. Reprogramar Despacho por cambio de Zona (alerta XX)
                 * 
                 *  
                 */
                // rescatamos la jornada de despacho que referencia el pedido
                JornadaDespachoEntity jor = daoJornadas.getJornadaDespachoById(pedido.getId_jdespacho());

                if (jor.getId_zona() != pedido.getId_zona()) {
                    id_alerta = Constantes.ALE_CAMBIO_ZONA_DESPACHO;
                    daoPedidos.addAlertToPedido(id_pedido, id_alerta);
                    logger.info("Alerta Reprogramar Despacho por cambio de Zona");
                }
            }
        } catch (PedidosDAOException ex) {
            //  rollback trx
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }

            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            if (ex.getMessage().equals(DbSQLCode.SQL_DUP_KEY_CODE)) {
                logger.debug("existe el alerta en el pedido");
                throw new PedidosException(Constantes._EX_OPE_REL_ALERTA_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        } catch (JornadasDAOException ex) {
            //rollback trx
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
        try {
            trx1.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }
        return res;

    }

    public void validaSaldoYBloqueo(PedidoDTO pedido, JdbcPedidosDAO daoPedidos) throws PedidosDAOException {
        if (seDebeValidarMedioDePago()) {
            Validar validar = new Validar();
            TarjetaCAT tarjeta = validar.infoCat(pedido.getNum_mp_unmask());
            if (tarjeta == null) {
                daoPedidos.addAlertToPedido(pedido.getId_pedido(),Constantes.ALE_VALIDADOR_MPAGO_NO_EJECUTADO);
                return;
            }

            if (tarjeta.getError() > 0) {
                daoPedidos.addAlertToPedido(pedido.getId_pedido(),Constantes.ALE_VALIDADOR_MPAGO_NO_EJECUTADO);
                LogPedidoDTO log = new LogPedidoDTO();
                log.setId_pedido(pedido.getId_pedido());
                log.setUsuario("SYSTEM");
                log.setLog("Nº Error: " + tarjeta.getError() + "  msg: " + tarjeta.getErrorMsg());
                daoPedidos.addLogPedido(log);
                return;
            }
            if (tarjeta.getDisponibleCompraPesos() < (pedido.getMonto() * 1.1)) {
                daoPedidos.addAlertToPedido(pedido.getId_pedido(), Constantes.ALE_MED_PAGO_SIN_CUPO);
            }
            if (tarjeta.getBloqueo() != null && !"".equals(tarjeta.getBloqueo().trim())) {
                daoPedidos.addAlertToPedido(pedido.getId_pedido(), Constantes.ALE_VALIDADOR_MPAGO_CON_BLOQUEOS);
            }

            //si el pedido no es para entrega en local
            if (!pedido.getTipo_despacho().equalsIgnoreCase("R")) {
                DireccionEntity dirPedido = daoPedidos.getDireccionById(pedido.getDir_id());
                if (dirPedido.getFnueva() != null && dirPedido.getFnueva().equals(Constantes.ESTADO_NUEVO)) {
                    String direccion = tarjeta.getDireccion().toLowerCase();
                    if (direccion.indexOf(dirPedido.getCalle().toLowerCase()) < 0 
                    || direccion.indexOf(dirPedido.getNumero().toLowerCase()) < 0) {
                        daoPedidos.addAlertToPedido(pedido.getId_pedido(), Constantes.ALE_DIRECC_NUEVA);
                    }
                }
            }

            if (validar.isMigrada(pedido.getNum_mp_unmask())) {
                ResourceBundle rb = ResourceBundle.getBundle("bo");
                String key = rb.getString("conf.bo.key");
                String mp;
                try {
                    mp = Cifrador.encriptar(key, (new Long(tarjeta.getNumeroTarjeta())).toString());
                    logger.debug("MP:" + mp);
                    daoPedidos.updNumeroTarjeta(pedido.getId_pedido(), mp);
                    LogPedidoDTO log = new LogPedidoDTO();
                    log.setId_pedido(pedido.getId_pedido());
                    log.setUsuario("SYSTEM");
                    log.setLog("Cambio automático de numero antiguo de tarjeta paris a numero nuevo");
                    daoPedidos.addLogPedido(log);
                } catch (Exception e) {
                    logger.error("Error al modificar numero de tarjeta paris migrada");
                    logger.error(e);
                }
            }
        }
    }

    /**
     * Indica si se debe validar el medio de pago. El método obtiene de la base
     * de datos de la tabla bo_parametros si se validar o no
     * 
     * @return True si se debe validar el medio de pago.
     */
    private boolean seDebeValidarMedioDePago() {
        /*
         * Es más razonable sacar esta consulta de las clases Process que
         * validan el medio de pago por: 1.- Se escribe una sola vez y se
         * diminuye el código escrito. 2.- Si a una clase process se llama su
         * método que valida un medio de pago, pero en la bd está configurado
         * para que no valide los medios de pago ¿Que rotorna entonces el
         * método? Cada método que valida el medio de pago va ha tener que
         * retornar que no debe validar, mejor es preguntar antes si se deben
         * validar los medios de pagos una sola vez. Así queda programado en el
         * método validarMedioDePago
         */
        try {
            ParametrosService ps = new ParametrosService();
            ParametroDTO par = ps.getParametroByName("MQ_VALIDA_MEDIOPAGO");
            return Integer.parseInt(par.getValor()) != 0;
        } catch (Exception se) {
            logger.info("No se pudo leer el valor del servicio MQ Valida Cuenta");
            return false;
        }
    }

    /**
     * Verifica el numero de la tarjeta para saber si es tarjeta MAS
     * 
     * @param numero
     * @return true si es tarjetaCAT
     */
    private boolean esTarjetaCAT(long numero) {
        return (numero / 10000000000000L) == 615;
    }

    /**
     * Busca alertas relacionadas con una venta a empresa
     * 
     * @param id_pedido
     * @param pedido
     * @param daoPedidos
     * @param lst_det
     * @param cliente
     * @throws PedidosDAOException
     */
    private void validarVentaEmpresa(long id_pedido, PedidoDTO pedido, JdbcPedidosDAO daoPedidos, List lst_det, 
    		ClienteEntity cliente) throws PedidosDAOException {
        long id_alerta;
        logger.debug("Inicio - Revision de Alertas pedidos Venta Empresa");

        /*
         * 16. Cantidades de productos excedidas Obtener la cantidad de
         * productos y comparar con la constante (alerta 16)
         */
        double cant_total = 0;
        for (int i = 0; i < lst_det.size(); i++) {
            ProductosPedidoDTO prodped = (ProductosPedidoDTO) lst_det.get(i);
            cant_total += prodped.getCant_solic();
        }
        logger.debug("cant_total: " + cant_total);
        if (cant_total > Constantes.MAX_CANTIDAD_PRODUCTOS) {
            id_alerta = Constantes.ALE_CANT_PROD_EXCEDIDAS;
            daoPedidos.addAlertToPedido(id_pedido, id_alerta);
            logger.info("Alerta Cantidades de productos excedidas");
        }

        /*
         * 17. Forma de pago con Linea de credito Obtiene el monto del ultimo
         * cargo de la empresa (alerta 17)
         */

        System.out.println("*** validarVentaEmpresa ***");

        if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_LINEA_CREDITO)) {
            //obtener el monto disponible de la empresa
            EmpresasEntity emp = daoPedidos.getEmpresaById(cliente.getId_empresa().longValue());
            System.out.println("id_empresa : " + emp.getId().longValue());
            double saldo_pendiente = daoPedidos.getSaldoActualPendiente(emp.getId().longValue());
            double disp_actual = emp.getSaldo().doubleValue() - saldo_pendiente;
            System.out.println("saldo pendiente: " + saldo_pendiente);
            System.out.println("Saldo: " + emp.getSaldo());
            System.out.println("monto pedido: " + pedido.getMonto());
            System.out.println("disp_actual: " + disp_actual);
            if (disp_actual < 0.0) {
                id_alerta = Constantes.ALE_MED_PAGO_LINEA_CRED;
                daoPedidos.addAlertToPedido(id_pedido, id_alerta);
                logger.info("Alerta Forma de pago con Linea de credito Insuficiente");
            }
        }

        /*
         * 18. Pedido especial (ex-SPOT) Revisa si el pedido esta relacionado a
         * una zona SPOT si la comuna esta marcada con el tipo = 'V' (alerta 18)
         */
        logger.debug("Tipo de comuna:" + pedido.getTipo_comuna());
        if (pedido.getTipo_comuna().equals(Constantes.ORIGEN_VE_CTE)) {
            id_alerta = Constantes.ALE_ZONA_SPOT;
            daoPedidos.addAlertToPedido(id_pedido, id_alerta);
            logger.info("Alerta Pedido Especial de Venta a Empresas");
        }

        /*
         * if(pedido.getId_local() == Constantes.LOCAL_SPOT){ id_alerta =
         * Constantes.ALE_ZONA_SPOT; dao.addAlertToPedido(id_pedido,id_alerta);
         * logger.info("Zona SPOT"); }
         */

        logger.debug("Fin - Revision de Alertas pedidos Venta Empresa");
    }

    /**
     * 
     * @param num_mp
     * @return
     */
    public boolean isMPagoAlternativo(String num_mp) {
        return num_mp.equals(Constantes.TARJETA_CAT_DUMMY);
    }

    /**
     * Permite eliminar las alertas de una OP. <br>
     * 
     * @param id_pedido
     *            long
     * @return boolean, devuelve <i>true </i> en el caso que la eliminación fue
     *         satisfactoria, caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en caso que exista error en base de datos.
     * @throws SystemException,
     *             en caso que exista error de sistema.
     *  
     */
    public boolean elimAlertaByPedido(long id_pedido) throws PedidosException, SystemException {
        boolean result = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            result = dao.elimAlertaByPedido(id_pedido);
        } catch (PedidosDAOException e) {
            throw new PedidosException(e.getMessage());
        }

        return result;
    }

    /**
     * Permite eliminar las alertas GENERADAS POR EL mEDIO DE pAGO. <br>
     * 
     * @param id_pedido
     *            long
     * @return boolean, devuelve <i>true </i> en el caso que la eliminación fue
     *         satisfactoria, caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en caso que exista error en base de datos.
     * @throws SystemException,
     *             en caso que exista error de sistema.
     *  
     */
    public boolean elimAlertaByPedidoFromMPago(long id_pedido) throws PedidosException, SystemException {
        boolean result = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory( DAOFactory.JDBC).getPedidosDAO();

        try {
            result = dao.elimAlertaByPedidoFromMPago(id_pedido);
        } catch (PedidosDAOException e) {
            throw new PedidosException(e.getMessage());
        }

        return result;
    }

    /**
     * Permite agregar una alerta a una OP, segun <b>id </b> del pedido y <b>id
     * </b> de alerta. <br>
     * 
     * @param id_pedido
     *            long
     * @param id_alerta
     *            long
     * @return boolean, devuelve <i>true </i> en el caso que la inserción fue
     *         satisfactoria, caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en caso que exista error en base de datos.
     *  
     */
    public boolean addAlertToPedido(long id_pedido, long id_alerta) throws PedidosException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory( DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.addAlertToPedido(id_pedido, id_alerta);
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }
    }

    /**
     * Permite modificar el estado de una OP, segun <b>id </b> del pedido y
     * <b>id </b> del nuevo estado. <br>
     * 
     * @param id_pedido
     *            long
     * @param id_estado
     *            long
     * @return boolean, devuelve <i>true </i> en el caso que la actualización
     *         fue satisfactoria, caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en caso que exista error en base de datos.
     *  
     */
    public boolean setModEstadoPedido(long id_pedido, long id_estado) throws PedidosException, SystemException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.setModEstadoPedido(id_pedido, id_estado);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * Permite modificar el estado de una TrxMp, segun <b>id </b> de la TrxMp y
     * <b>id </b> del nuevo estado. <br>
     * 
     * @param id_trx_mp
     *            long
     * @param id_estado
     *            long
     * @return boolean, devuelve <i>true </i> en el caso que la actualización
     *         fue satisfactoria, caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en caso que exista error en base de datos.
     *  
     */
    public boolean setModEstadoTrxMp(long id_trx_mp, long id_estado) throws PedidosException, SystemException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.setModEstadoTrxMp(id_trx_mp, id_estado);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * Determina si existe alerta activa relacionada a una OP, segun <b>id </b>
     * del pedido. <br>
     * 
     * @param id_pedido
     *            long
     * @return boolean, devuelve <i>true </i> en el caso que exista una o mas
     *         alertas activas, caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en caso que exista error en base de datos.
     *  
     */
    public boolean getExisteAlertaActiva(long id_pedido) throws PedidosException {
        //trae listado de alertas para el pedido
        //si alguna alerta es de tipo ACTIVA entonces devuelve TRUE
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getExisteAlertaActiva(id_pedido);
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }
    }

    /**
     * Modifica información del medio de pago en la OP. <br>
     * Nota: Este método tiene <b>Transaccionalidad </b>.
     * 
     * @param mp
     *            DatosMedioPagoDTO
     * 
     * @throws PedidosException,
     *             en caso que no exista el pedido.
     * @throws SystemException,
     *             en caso que exista error de sistema.
     *  
     */
    public void setCambiarMedio_pago(DatosMedioPagoDTO mp) throws PedidosException, SystemException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        //		 Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();
        //		 Iniciamos trx
        try {
            trx1.begin();
        } catch (Exception e1) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        }
        try {
            dao.setTrx(trx1);
        } catch (PedidosDAOException e2) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos");
        }

        try {
            // Validamos que vengan los datos

            // Encriptamos el número de tarjeta
            if (!mp.getNum_mp().equals("")) {
                ResourceBundle rb = ResourceBundle.getBundle("bo");
                String key = rb.getString("conf.bo.key");
                String num_tc = Cifrador.encriptar(key, mp.getNum_mp());
                mp.setNum_mp(num_tc);
            }

            dao.setCambiarMedio_pago(mp);

            //registrar el cambio en el log
            LogPedidoDTO log = new LogPedidoDTO();
            log.setId_pedido(mp.getId_pedido());
            log.setLog(Constantes.CAMBIO_MEDIO_PAGO + mp.getMedio_pago());
            log.setId_motivo(8);
            log.setUsuario(mp.getUsr_login());
            dao.addLogPedido(log);

            PedidoDTO ped = dao.getPedidoById(mp.getId_pedido());
            //Si tiene promociones y no es Venta empresa
            if ((dao.getResumenPromocionPedidos(mp.getId_pedido()).size() > 0)
                    && (!ped.getTipo_ve().equals("S"))) {
                //Setea el flag de recalculo en el pedido si no existe
                List lst_alerts = dao.getAlertasPedido(mp.getId_pedido());
                boolean enc_alerta = false;
                for (int i = 0; i < lst_alerts.size(); i++) {
                    AlertaDTO alerta = (AlertaDTO) lst_alerts.get(i);
                    if (alerta.getId_alerta() == Constantes.ALE_RECALC_PROMO_CAMBIO_MPAGO) {
                        enc_alerta = true;
                    }
                }
                //si la alerta no estaba la pone
                if (!enc_alerta) {
                    ped.setFlg_recalc_mp(true);
                    dao.updFlagRecalculoPedido(ped);
                    //Debe dejar la alerta dura de recalculo de promocion por cambio de medio de pago
                    dao.addAlertToPedido(mp.getId_pedido(), Constantes.ALE_RECALC_PROMO_CAMBIO_MPAGO);
                }
            }

        } catch (PedidosDAOException e) {
            //			rollback trx
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }
            logger.error("PedidosDAOException");
            if (e.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, e);
            }
            throw new SystemException(e);
        } catch (Exception e) {
            throw new SystemException(e);
        }
        //		 cerramos trx
        try {
            trx1.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }
    }

    /**
     * Permite anular una OP, segun el <b>id </b> del pedido. <br>
     * 
     * @param id_pedido
     *            long
     * @return boolean, devuelve <i>true </i> si la anulación fue satisfactoria,
     *         caso contrario, devuelve <i>false </i>.
     * @throws PedidosException,
     *             en caso que exista error en base de datos.
     * @throws SystemException,
     *             en caso que exista error de sistema.
     * 
     *  
     */
    public boolean setAnularOP(long id_pedido) throws PedidosException, SystemException {
        boolean res = false;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        JdbcJornadasDAO daoJ = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();

        //		 Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();
        //		 Iniciamos trx
        try {
            trx1.begin();
        } catch (Exception e1) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        }
        try {
            dao.setTrx(trx1);
            daoJ.setTrx(trx1);
        } catch (PedidosDAOException e2) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos");
        } catch (JornadasDAOException e) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Jornadas");
        }

        try {
            int id_estado = Constantes.ID_ESTAD_PEDIDO_ANULADO; //estado
            // anulado
            res = dao.setModEstadoPedido(id_pedido, id_estado);
            if (res) {
                PedidoDTO pedido = dao.getPedidoById(id_pedido);

                //disminuir la capacidad de picking de la jornada
                // correspondiente al pedido
                //Por VE, los pedidos Especiales (S) NO TIENEN capacidad
                // ocupada
                if (!pedido.getTipo_ve().equals("S")) {
                    daoJ.doOcupaCapacidadPicking(pedido.getId_jpicking(),((int) pedido.getCant_prods()) * (-1));
                }

                // 2. liberar capacidad de despacho de jornada despacho en que
                // se encuentra el pedido
                //Por VE, los pedidos Especiales (S) NO TIENEN capacidad
                // ocupada
                if (!pedido.getTipo_ve().equals("S")) {
                    daoJ.doOcupaCapacidadDespacho(pedido.getId_jdespacho(), -1);
                }

                //eliminar la relacion entre id_jpicking y el pedido
                //dao.elimJPickByPedido(id_pedido);

                //liberar OP
                /*
                 * AsignaOPDTO prm = new AsignaOPDTO();
                 * prm.setId_pedido(id_pedido);
                 * prm.setId_usuario(pedido.getId_usuario());
                 * prm.setId_motivo(Constantes.ID_MOTIVO_MODIF_DATOS);
                 * dao.setLiberaOP(prm);
                 */

                //Por VE, si anula el pedido debe revisar si es necesario
                // Terminar la cotizacion:
                //	1. obtener el id de cotizacion
                logger.debug("obtener id cotizacion del pedido");
                long id_cotizacion = 0;
                //PedidoDTO ped = dao.getPedidoById(id_pedido);
                id_cotizacion = pedido.getId_cotizacion();
                logger.debug("id cotizacion del pedido:" + pedido.getId_cotizacion());
                if (id_cotizacion > 0) {
                    //2. loopear revisando los estados de los pedidos de la
                    // cotizacion
                    List lst_ped = dao.getPedidosCotiz(id_cotizacion);
                    int cuenta_ped_no_terminados = 0;
                    for (int i = 0; i < lst_ped.size(); i++) {
                        PedidosCotizacionesDTO pedcotz = (PedidosCotizacionesDTO) lst_ped.get(i);
                        if ((pedcotz.getId_estado() != Constantes.ID_ESTAD_PEDIDO_ANULADO)
                                && (pedcotz.getId_estado() != Constantes.ID_ESTAD_PEDIDO_FINALIZADO)) {
                            cuenta_ped_no_terminados++;
                        }
                    }
                    logger.debug("numero de pedidos de la cotizacion " + id_cotizacion + ", no finalizados:"
                            + cuenta_ped_no_terminados);

                    /*
                     * 3. si solo existen pedidos finalizados, y anulados
                     * entonces cambiar el estado de la cotizacion a terminada.
                     */
                    if (cuenta_ped_no_terminados == 0) {
                        logger.debug("La cotizacion debe cambiar a Terminada");
                        boolean res_mod_cotz = false;
                        res_mod_cotz = dao
                        		.setModEstadoCotizacion(id_cotizacion, Constantes.ID_EST_COTIZACION_TERMINADA);
                        if (res_mod_cotz == false)
                            throw new PedidosDAOException(Constantes._EX_COT_ID_NO_EXISTE);
                    }
                }//fin de revision si el pedido era de una cotizacion
            }
        } catch (PedidosDAOException ex) {
            //	rollback trx
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }

            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de factura");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            } else if (ex.getMessage().equals(Constantes._EX_COT_ID_NO_EXISTE)) {
                logger.error("no puede buscar datos de la cotizacion");
                throw new PedidosException(Constantes._EX_COT_ID_NO_EXISTE, ex);
            } else
                throw new SystemException("Error no controlado", ex);
        } catch (JornadasDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de jornada");
                throw new PedidosException(Constantes._EX_JPICK_ID_INVALIDO, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        //	cerramos trx
        try {
            trx1.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }
        return res;
    }

    /**
     * Modifica dirección de despacho de un pedido, segun el <b>id </b> del
     * pedido y el <b>id </b> de dirección.
     * 
     * @param dto
     *            datos de la dirección nueva que se seteará en el pedido
     * @return boolean, devuelve <i>true </i> si la actualización fue
     *         satisfactoria, caso contrario, devuelve <i>false </i>.
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en caso que exista error de sistema.
     *  
     */
    public boolean setModPedidoDir(String user,cl.bbr.jumbocl.clientes.dto.DireccionesDTO nuevaDir, PedidoDTO ped) throws PedidosException, SystemException {

        // Creamos los dao's
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        boolean actualizo = false;
        LogPedidoDTO log = new LogPedidoDTO();

        log.setId_pedido(ped.getId_pedido());
        log.setUsuario(user);

        try {
            //obtiene local actual del pedido
            //PedidoDTO pedido = dao.getPedidoById(dto.getId_pedido());

            // Obtiene la dirección nueva
            //DireccionEntity nuevaDir = dao.getDireccionById(dto.getId_dir());

            //actualizar la direccion en el pedido
            actualizo = dao.setModPedidoDir(nuevaDir, ped);

            if (actualizo) {
                // revisar si el local es distinto para cambiar los sectores de
                // picking
                logger.debug("local actual:" + ped.getId_local() + " local nuevo" 
                		+ nuevaDir.getId_loc());
                if (ped.getId_local() != nuevaDir.getId_loc()) {
                    //es necesario cambiar los sectores de picking del detalle
                    // pedido por sus equivalentes en el otro local
                    logger.debug("el local de la nueva direccion es diferente al del pedido");
                    //se nulifican los sectores picking del detalle_pedido
                    if ((dao.setNullSectoresDetallePedido(ped.getId_pedido()))
                            && (dao.setModEstadoPedido(ped.getId_pedido(),Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION))) {
                        // y se cambia el estado del pedido a "en validacion"
                        logger.debug("sectores nulificados, se debe revalidar el pedido " + ped.getId_pedido());
                        log.setLog(Constantes.CAMBIO_LOCAL_PED_SECTOR);
                        //logear el cambio
                        dao.addLogPedido(log);
                    } else {
                        logger.debug("los sector no pueden nulificarse o no puede cambiar el estado  al pedido");
                    }
                }
                // Registra en Log
                log.setLog(Constantes.CAMBIO_DIR_DESP);
                dao.addLogPedido(log);

            }
            /*
             * boolean act=dao2.doActualizaPedidoJornadas( id_pedido,
             * jor.getId_jpicking(), id_jdespacho, (int)var_precio );
             * 
             * if(act){ LogPedidoDTO log = new LogPedidoDTO();
             * log.setId_pedido(id_pedido);
             * log.setLog(Constantes.REAGENDA_JORNADA_DESP);
             * log.setUsuario(usr_login); dao2.addLogPedido(log); }
             *  
             */
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }

        return actualizo;

    }

    /**
     * Modifica las indicaciones de un pedido, segun la información contenida en
     * <b>ProcModPedidoIndicDTO </b>.
     * 
     * @param prm
     *            ProcModPedidoIndicDTO
     * @return boolean, devuelve <i>true </i> si la actualización fue
     *         satisfactoria, caso contrario, devuelve <i>false </i>.
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en caso que exista error de sistema.
     *  
     */
    public boolean setModPedidoIndic(ProcModPedidoIndicDTO prm) throws PedidosException, SystemException {
        boolean actualiza = false;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            actualiza = dao.setModPedidoIndic(prm);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return actualiza;
    }

    /**
     * Obtiene la información del cliente, segun el <b>id </b> del cliente.
     * 
     * @param id_cliente
     *            long
     * @return ClientesDTO contiene la información del cliente
     * @throws ClientesException,
     *             en el caso que no exista el cliente.
     * @throws SystemException,
     *             en caso que exista error de sistema.
     *  
     */
    public ClientesDTO getClienteById(long id_cliente) throws ClientesException, SystemException {
        ClientesDTO dto = new ClientesDTO();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            ClienteEntity cli = dao.getClienteById(id_cliente);
            String fec_crea = "";
            if (cli.getFec_crea() != null)
                fec_crea = cli.getFec_crea().toString();
            String fec_act = "";
            if (cli.getFec_act() != null)
                fec_act = cli.getFec_act().toString();
            String fec_nac = "";
            if (cli.getFec_nac() != null)
                fec_nac = cli.getFec_nac().toString();

            dto = new ClientesDTO(cli.getId().longValue(), cli.getRut().longValue(), cli.getDv(), cli.getNombre(), cli
                    .getApellido_pat(), cli.getApellido_mat(), cli.getClave(), cli.getEmail(), cli.getFon_cod_1(), cli
                    .getFon_num_1(), cli.getFon_cod_2(), cli.getFon_num_2(), cli.getRec_info().intValue(), fec_crea, 
                    fec_act, cli.getEstado(), fec_nac, cli.getGenero());
        } catch (ClientesDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new ClientesException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return dto;
    }

    /**
     * Obtiene el listado de direcciones, segun el <b>id </b> del cliente.
     * 
     * @param id_cliente
     *            long
     * @return List DireccionesDTO contiene la información de la dirección
     * @throws PedidosException,
     *             en el caso que no exista el cliente.
     *  
     */
    /*public List listadoDirecciones(long id_cliente) throws PedidosException {
        List dirs = new ArrayList();
        try {
            JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

            List listaDir = new ArrayList();
            listaDir = (List) dao.listadoDirecciones(id_cliente);
            DireccionEntity dir = null;
            String fecha = "";
            for (int i = 0; i < listaDir.size(); i++) {
                fecha = "";
                dir = null;
                dir = (DireccionEntity) listaDir.get(i);
                TipoCalleDTO tipCllDto = new TipoCalleDTO(dir.getTipo_calle().getId().longValue(), dir.getTipo_calle()
                        .getNombre(), dir.getTipo_calle().getEstado());
                if (dir.getFec_crea() != null)
                    fecha = dir.getFec_crea().toString();
                DireccionesDTO dirdto = new DireccionesDTO(dir.getId().intValue(), dir.getCli_id().longValue(), dir
                        .getAlias(), dir.getCalle(), dir.getNom_comuna(), dir.getDepto(), dir.getComentarios(), dir
                        .getEstado(), fecha, dir.getNumero(), dir.getNom_local(), dir.getNom_tip_calle(), dir
                        .getFnueva(), dir.getCuadrante(), dir.getLoc_cod().longValue(), dir.getCom_id().longValue(),
                        dir.getZon_id().longValue(), tipCllDto);
                dirs.add(dirdto);
                logger.debug("se coloca el dir:" + i);
            }

        } catch (PedidosDAOException ex) {
            logger.debug("Problema DireccionesCTRL:" + ex);
            throw new PedidosException(ex);
        }
        return dirs;

    }*/

    /**
     * Determina si el pedido existe o no, segun el <b>id </b> del pedido.
     * 
     * @param id_pedido
     *            long
     * @return boolean, devuelve <i>true </i> si el pedido existe, caso
     *         contrartio devuelve <i>false </i>.
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public boolean isPedidoById(long id_pedido) throws PedidosException, SystemException {

        boolean existe = false;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            existe = dao.isPedidoById(id_pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return existe;
    }

    /**
     * Permite actualizar el monto total y la cantidad de productos del pedido,
     * segun el <b>id </b> del pedido.
     * 
     * @param id_pedido
     *            long
     * @return boolean, devuelve <i>true </i> si la actualización fue exitosa,
     *         caso contrartio devuelve <i>false </i>.
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    /*
    public boolean recalcPedido(long id_pedido) throws PedidosException, SystemException {

        boolean recalcula = false;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            recalcula = dao.recalcPedido(id_pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return recalcula;
    }
     */

    /**
     * Obtiene el precio de un producto, segun el <b>id </b> del producto y el
     * <b>id </b> del local.
     * 
     * @param id_local
     *            long
     * @param id_producto
     *            long
     * @return double, devuelve el precio del producto.
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public double getPrecioByLocalProd(long id_local, long id_producto) throws PedidosException, SystemException {

        double precio = 0;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            precio = dao.getPrecioByLocalProd(id_local, id_producto);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del local");
                throw new PedidosException(Constantes._EX_PROD_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return precio;
    }

    /**
     * Permite confirmar el pedido, segun el <b>id </b> del pedido. <br>
     * Luego de la confirmación del pedido, actualiza información en el cliente.
     * Nota: Este método tiene <b>Transaccionalidad </b>.
     * 
     * @param id_pedido
     *            long
     * @return boolean, devuelve <i>true </i> si la actualización fue exitosa,
     *         caso contrartio devuelve <i>false </i>.
     * @throws PedidosException,
     *             en uno de los siguientes casos: <br>- No existe el pedido,
     *             <br>- Tiene alertas activas, <br>- No existe el cliente.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public boolean setConfirmarOP(long idPedido, UserDTO usr) throws PedidosException, SystemException {

        // Llama a método que Valida la OP
        boolean isValidada = this.setValidaOP(idPedido);
        logger.debug("isValidada?" + isValidada);

        boolean result = false;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        logger.debug("Inicio proceso de confirmación de OP...");
        //		 Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();

        try {
            // Iniciamos trx
            trx1.begin();
            // Asigna trx al dao
            dao.setTrx(trx1);
        } catch (PedidosDAOException e2) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos");
        } catch (Exception e) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        }

        try {
            // Si la OP está validada sigue con el proceso de confirmacion
            if (isValidada && !dao.getExisteAlertaActiva(idPedido)) {

                result = dao.setModEstadoPedido(idPedido, Constantes.ID_ESTAD_PEDIDO_VALIDADO);
                dao.setDireccion(idPedido);

                // generar la consulta SAF de quema de cupones si falla queda en la tabla saf para su envio posterior
                logger.debug("****** inicio quema cupones saf");
                logger.debug("PROMO_SERVER_HOST : " + Constantes.PROMO_SERVER_HOST + " - PROMO_SERVER_TCP_PORT : " + Constantes.PROMO_SERVER_TCP_PORT);

                ClienteTcpPromosCuponesSaf socketsaf = new ClienteTcpPromosCuponesSaf(
                        Constantes.PROMO_SERVER_HOST,
                        Constantes.PROMO_SERVER_TCP_PORT, 
                        trx1);
                socketsaf.setGeneraMsgSaf(idPedido, usr.getId_usuario());

                logger.debug("****** fin quema cupones saf");

                //lgging de pedidos
                LogPedidoDTO log = new LogPedidoDTO();
                log.setId_pedido(idPedido);

                //log.setUsuario("SYSTEM");
                log.setUsuario(usr.getLogin());
                String mnsLog = "El Pedido se encuentra en estado : Validado";
                log.setLog(mnsLog);
                dao.addLogPedido(log);

                //actualizar el indicador de modificacion de datos
                PedidoDTO pedidoC = dao.getPedidoById(idPedido);
                ClienteEntity cliente = dao.getClienteById(pedidoC.getId_cliente());
                if (cliente.getMod_dato().equals(Constantes.CLI_MOD_APELLIDOS)) {
                    cliente.setMod_dato(Constantes.CLI_NO_MOD_DATOS);
                    boolean actCliente = dao.actClienteById(cliente);
                    logger.debug("actCliente:" + actCliente);
                }

            } else {
                trx1.end();
                throw new PedidosException(Constantes._EX_OPE_TIENE_ALERTA_ACT);
            }

        } catch (PedidosDAOException ex) {

            // rollback trx
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }

            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);

        } catch (ClientesDAOException ex) {
            // rollback trx
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }

            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del cliente");
                throw new PedidosException(Constantes._EX_CLI_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);

        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }

        //***********************************************
        //Logica para agregar bolsa de regalo

        try {
            JdbcBolsasDAO bolsasDao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsaDAO();
            JdbcProductosDAO productosDao = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();

            if (bolsasDao.estaActivoServicioBolsas()) {

                System.out.println("****** SERVICIO DE ASIGNACION DE BOLSA ACTIVO *****");

                System.out.println("***************************************");
                System.out.println("********VALIDACIÓN ASIGNA BOLSA********");
                System.out.println("***************************************");

                PedidoDTO pedido = dao.getPedidoById(idPedido);

                //long id_local = pedido.getId_local();
                String rut_cliente_pedido = pedido.getRut_cliente() + "-" + pedido.getDv_cliente();
                BolsaDTO bolsa = new BolsaDTO();
                ProductoEntity producto = new ProductoEntity();
                int id_prod = -1;
                String pedidosNum = "";


                if (pedido.getOrigen().equalsIgnoreCase("W")) { // si no es venta empresa
                    System.out.println("---NO ES VENTA EMPRESA!");
                    boolean flagFueAsignado = false;
                    boolean flagEL = bolsasDao.existeEnListaBase(rut_cliente_pedido);

                    if (flagEL) {
                        flagFueAsignado = true;
                    }

                    //Validacion existencia de Bolsa de Regalo dentro del mismo periodo
                    //de tener bolsa 1 o bolsa 2 ya asignada, no asigna.
                    boolean flagTieneDespachosEnPeriodo = false;
                    //boolean flagTieneBolsaAsignadaEnPeriodo = false;
                    boolean flagNoTieneBolsa = false;
                    List listPedidosR = dao.getPedidosPorEstadoR(pedido.getId_cliente());
                    for (int i = 0; i < listPedidosR.size(); i++) {
                        PedidoDTO ped1 = (PedidoDTO) listPedidosR.get(i);
                        // validaciones:
                        // mes despacho != mes op que se generó
                        // año despacho != año op que se generó
                        // estado != pedido anulado
                        if (ped1.getFdespacho() != null) {
                            if (Integer.parseInt(ped1.getFdespacho().split("-")[1]) == Integer.parseInt(pedido.getFdespacho().split("-")[1])
                                && 
                                Integer.parseInt(ped1.getFdespacho().split("-")[0]) == Integer.parseInt(pedido.getFdespacho().split("-")[0])
                                ) {
                                pedidosNum = ped1.getId_pedido() + "," + pedidosNum;
                            }
					}/*else{
						break;
					}*/
                    }

                    flagNoTieneBolsa = dao.tieneBolsaRegalo(pedidosNum.substring(0, pedidosNum.length() - 1));

                    if (flagNoTieneBolsa) { //SI PEDIDO NO TIENE BOLSA DE REGALO ASIGNADA DENTRO DEL MISMO PERIODO
                        System.out.println("--- CLIENTE NO TIENE MAS DE 2 BOLSAS DENTRO DEL PERIODO ! ");
                        //consulta si está en lista base;
                        // true = cliente que ya tiene asignación de bolsa
                        // false = cliente nunca asignado
                        if (flagFueAsignado) {
                            System.out.println("---CLIENTE SE ENCUENTRA EN LISTA BASE!");
                            flagTieneDespachosEnPeriodo = false;
                            List listPedidos = dao.getPedidosPorEstado(pedido.getId_cliente(), idPedido);
                            List productosPedidoComparar = new ArrayList();
                            BolsaDTO bolsaComparar = new BolsaDTO();

                            for (int i = 0; i < listPedidos.size(); i++) {
                                PedidoDTO ped = (PedidoDTO) listPedidos.get(i);
                                // validaciones:
                                // mes despacho != mes op que se generó
                                // año despacho != año op que se generó
                                // estado != pedido anulado
                                if (ped.getFdespacho() != null) {
                                    if (Integer.parseInt(ped.getFdespacho().split("-")[1]) == Integer.parseInt(pedido.getFdespacho().split("-")[1])
                                        && 
                                        Integer.parseInt(ped.getFdespacho().split("-")[0]) == Integer.parseInt(pedido.getFdespacho().split("-")[0])
                                        ) {
                                        flagTieneDespachosEnPeriodo = true;
                                        productosPedidoComparar = dao.getDetallesPedido(ped.getId_pedido());
	    							//System.out.println("DETALLE DEL PEDIDO : "+ped.getId_pedido());
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }

                            List bolsas = bolsasDao.getBolsasRegalo();

                            for (int i = 0; i < productosPedidoComparar.size(); i++) {
                                ProductosPedidoDTO prodComp = (ProductosPedidoDTO) productosPedidoComparar.get(i);
                                for (int y = 0; y < bolsas.size(); y++) {
                                    BolsaDTO bolsaComp = (BolsaDTO) bolsas.get(y);
							//System.out.println("COMPARO BOLSA DE LISTA EN BASE CON LA DEL ULTIMO PERIODO");
						    //System.out.println("COMPARO BOLSA "+prodComp.getCod_producto()+" == "+bolsaComp.getCod_sap());
                                    if (prodComp.getCod_producto().equals(bolsaComp.getCod_sap())) {
                                        bolsaComparar = bolsaComp;
								//System.out.println("CODIGO DE BOLSA ASIGNADO : "+bolsaComparar.getCod_bolsa());  
                                        break;
                                    }
                                }
                            }

                            Hashtable hAsignacion = bolsasDao.getAsignacionBolsaCliente(rut_cliente_pedido);

                            if (flagTieneDespachosEnPeriodo) {
                                System.out.println("---CLIENTE TIENE DESPACHOS EN PERIODO!");

						// bolsa anterior == bolsa actual (ultimo pedido)
						//List productosPedido = dao.getDetallesPedido(0);

						//if( ((String)hAsignacion.get("cod_bolsa")).equalsIgnoreCase(bolsaComparar.getCod_bolsa()) ){
						//System.out.println("BOLSA ANTERIOR = BOLSA ACTUAL -> "+(String)hAsignacion.get("cod_bolsa")+" = "+bolsaComparar.getCod_bolsa());
						if( ((String)hAsignacion.get("cod_bolsa")).equals(bolsaComparar.getCod_bolsa()) ){	
							System.out.println("---BOLSA LISTA BASE ES IGUAL A BOLSA ACTUAL!");

                                    if (bolsaComparar.getCod_bolsa().equals("2")) { // bolsa actual igual a bolsa N°2
                                        System.out.println("---BOLSA LISTA BASE ES IGUAL A BOLSA 2!");
                                    } else {
                                        System.out.println("---BOLSA LISTA BASE NO IGUAL A BOLSA 2!");
                                        bolsa = bolsasDao.getBolsaRegalo("2");
                                        producto = productosDao.getProductoById(bolsa.getId_producto());

        						//System.out.println("ID PRODUCTO DE LA BOLSA : "+bolsa.getId_producto()+" ID PRODUCTO : "+producto.getId_bo());

                                        //asigno bolsa al pedido
                                        ProductosPedidoDTO prod = new ProductosPedidoDTO();
                                        prod.setId_pedido(idPedido);
                                        prod.setId_producto(producto.getId_bo().longValue());
                                        prod.setId_sector(2);

                                        prod.setCod_producto(producto.getCod_sap());//LPC bolsa.getId_producto()+"");
                                        prod.setUnid_medida("ST");
                                        prod.setDescripcion(producto.getDesc_larga());
                                        prod.setCant_solic(1);
                                        prod.setObservacion(producto.getDesc_larga());
                                        prod.setPrecio(1);
                                        prod.setCant_spick(1);
                                        prod.setPesable(producto.getEs_pesable());
                                        prod.setPreparable(producto.getEs_prep());
                                        prod.setCon_nota("");
                                        prod.setTipoSel(producto.getTipo());
                                        prod.setPrecio_lista(1);
                                        prod.setDscto_item(0);

                                        System.out.println("ID_PRODUCTO=" + id_prod);
                                        System.out.println("---SE LE ASIGNA LA BOLSA 2!");

                                        if (bolsasDao.existeStockBolsaSucursal((pedido.getId_local() + ""), bolsa.getCod_bolsa())) {
                                            dao.agregaProductoPedido(prod);
                                            dao.descuentaStockBolsa(bolsa.getCod_bolsa(), (pedido.getId_local() + ""));
                                        }

                                    }
                                } else {
                                    System.out.println("---BOLSA LISTA BASE ES DIFERENTE A BOLSA ACTUAL!");
                                    bolsa = bolsasDao.getBolsaRegalo((String) hAsignacion.get("cod_bolsa"));
                                    producto = productosDao.getProductoById(bolsa.getId_producto());

                                    //asigno bolsa al pedido
                                    ProductosPedidoDTO prod = new ProductosPedidoDTO();
                                    prod.setId_pedido(idPedido);
                                    prod.setId_producto(producto.getId_bo().longValue());
                                    prod.setId_sector(2);

                                    prod.setCod_producto(producto.getCod_sap()); //LPC bolsa.getId_producto()+"");
                                    prod.setUnid_medida("ST");
                                    prod.setDescripcion(producto.getDesc_larga());
                                    prod.setCant_solic(1);
                                    prod.setObservacion(producto.getDesc_larga());
                                    prod.setPrecio(1);
                                    prod.setCant_spick(1);
                                    prod.setPesable(producto.getEs_pesable());
                                    prod.setPreparable(producto.getEs_prep());
                                    prod.setCon_nota("");
                                    prod.setTipoSel(producto.getTipo());
                                    prod.setPrecio_lista(1);
                                    prod.setDscto_item(0);

                                    System.out.println("ID_PRODUCTO=" + id_prod);
                                    System.out.println("---SE LE ASIGNA LA BOLSA EN LISTA BASE!");
                                    if (bolsasDao.existeStockBolsaSucursal((pedido.getId_local() + ""), bolsa.getCod_bolsa())) {
                                        dao.agregaProductoPedido(prod);
                                        dao.descuentaStockBolsa(bolsa.getCod_bolsa(), (pedido.getId_local() + ""));
                                    }
                                }
                            } else {
                                System.out.println("---CLIENTE NO TIENE DESPACHOS EN PERIODO!");
                                bolsa = bolsasDao.getBolsaRegalo((String) hAsignacion.get("cod_bolsa"));
                                producto = productosDao.getProductoById(bolsa.getId_producto());

                                //asigno bolsa al pedido
                                ProductosPedidoDTO prod = new ProductosPedidoDTO();
                                prod.setId_pedido(idPedido);
                                prod.setId_producto(producto.getId_bo().longValue());
                                prod.setId_sector(2);

                                prod.setCod_producto(producto.getCod_sap()); //LPC bolsa.getId_producto()+"");
                                prod.setUnid_medida("ST");
                                prod.setDescripcion(producto.getDesc_larga());
                                prod.setCant_solic(1);
                                prod.setObservacion(producto.getDesc_larga());
                                prod.setPrecio(1);
                                prod.setCant_spick(1);
                                prod.setPesable(producto.getEs_pesable());
                                prod.setPreparable(producto.getEs_prep());
                                prod.setCon_nota("");
                                prod.setTipoSel(producto.getTipo());
                                prod.setPrecio_lista(1);
                                prod.setDscto_item(0);

                                System.out.println("ID_PRODUCTO=" + id_prod);
                                System.out.println("---SE LE ASIGNA LA BOLSA EN LISTA BASE!");
                                if (bolsasDao.existeStockBolsaSucursal((pedido.getId_local() + ""), bolsa.getCod_bolsa())) {
                                    dao.agregaProductoPedido(prod);
                                    dao.descuentaStockBolsa(bolsa.getCod_bolsa(), (pedido.getId_local() + ""));
                                }
                            }

                        } else {
                            System.out.println("---CLIENTE NO SE ENCUENTRA EN LISTA BASE!");
                            boolean flagEsClienteNuevo = dao.esPrimeraCompra(pedido.getId_cliente());

                            if (flagEsClienteNuevo) {// si es nuevo, asignar bolsa N°1
                                System.out.println("---CLIENTE ES NUEVO!");
                                System.out.println("---SE ASIGNA BOLSA 1!");
                                bolsa = bolsasDao.getBolsaRegalo("1");
                                producto = productosDao.getProductoById(bolsa.getId_producto());
                                //agrego a lista base
                                bolsasDao.asignacionBolsaCliente(rut_cliente_pedido, "1"); // rut_cliente_pedido, ID bolsa N°1
                            } else {// si NO es nuevo, asignar bolsa N°2
                                System.out.println("---CLIENTE NO ES NUEVO!");
                                System.out.println("---SE ASIGNA BOLSA 2!");
                                bolsa = bolsasDao.getBolsaRegalo("2");
                                producto = productosDao.getProductoById(bolsa.getId_producto());
                                //agrego a lista base
                                bolsasDao.asignacionBolsaCliente(rut_cliente_pedido, "2"); // rut_cliente_pedido, ID bolsa N°2
                            }

                            //asigno bolsa al pedido
                            ProductosPedidoDTO prod = new ProductosPedidoDTO();
                            prod.setId_pedido(idPedido);
                            prod.setId_producto(producto.getId_bo().longValue());
                            prod.setId_sector(2);

                            //prod.setCod_producto(bolsa.getId_producto()+"");
                            prod.setCod_producto(producto.getCod_sap());
                            prod.setUnid_medida("ST");
                            prod.setDescripcion(producto.getDesc_larga());
                            prod.setCant_solic(1);
                            prod.setObservacion(producto.getDesc_larga());
                            prod.setPrecio(1);
                            prod.setCant_spick(1);
                            prod.setPesable(producto.getEs_pesable());
                            prod.setPreparable(producto.getEs_prep());
                            prod.setCon_nota("");
                            prod.setTipoSel(producto.getTipo());
                            prod.setPrecio_lista(1);
                            prod.setDscto_item(0);

                            System.out.println("ID_PRODUCTO=" + id_prod);

                            System.out.println("---SE AGREGA CLIENTE A LISTA BASE!");

                            if (bolsasDao.existeStockBolsaSucursal((pedido.getId_local() + ""), bolsa.getCod_bolsa())) {
                                dao.agregaProductoPedido(prod);
                                dao.descuentaStockBolsa(bolsa.getCod_bolsa(), (pedido.getId_local() + ""));
                            }

                        }
                    } else {
                        System.out.println("---EL PEDIDO YA TIENE BOLSA ASIGNADA EN MISMO PERIODO!");
                    }

                } else {
                    System.out.println("---NO ES VENTA EMPRESA!");
                }

                System.out.println("***************************************");
                System.out.println("******FIN VALIDACIÓN ASIGNA BOLSA******");
                System.out.println("***************************************");
            } else {
                System.out.println("****** SERVICIO DE ASIGNACION DE BOLSA NO-ACTIVO *****");
            }
        } catch (Exception e) {
            logger.error("EL PROCESO DE ASIGNACIÓN DE BOLSA FALLÓ", e);
            System.out.println("EL PROCESO DE ASIGNACIÓN DE BOLSA FALLÓ =" + e.getLocalizedMessage());
            e.printStackTrace();
        }

        //FIN Logica para agregar bolsas de regalo
        //***********************************************

        //		 cerramos trx
        try {
            trx1.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }

        return result;
    }

    /**
     * Obtiene los detalles de picking de un pedido, segun el <b>id </b> del
     * pedido. <br>
     * 
     * @param id_pedido
     *            long
     * @return List DetallePickingDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */

    public List getDetPickingByIdPedido(long id_pedido) throws PedidosException, SystemException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            /*List lst_det_pick = dao.getDetPickingByIdPedido(id_pedido);
            DetallePickingEntity dpick = null;
            for (int i = 0; i < lst_det_pick.size(); i++) {
                dpick = null;
                dpick = (DetallePickingEntity) lst_det_pick.get(i);
                DetallePickingDTO dto = new DetallePickingDTO();
                dto.setId_dpicking(dpick.getId_dpicking().longValue());
                dto.setId_detalle(dpick.getId_detalle().longValue());
                //dto.setId_bp(dpick.getId_bp().longValue());
                dto.setId_producto(dpick.getId_producto().longValue());
                dto.setId_pedido(dpick.getId_pedido().longValue());
                dto.setCBarra(dpick.getCBarra());
                dto.setDescripcion(dpick.getDescripcion());
                dto.setPrecio(dpick.getPrecio().doubleValue());
                dto.setCant_pick(dpick.getCant_pick().doubleValue());
                dto.setSustituto(dpick.getSustituto());
                dto.setCod_bin(dpick.getCod_bin());
                dto.setUni_med(dpick.getUni_med());
                dto.setCod_prod(dpick.getCod_prod());
                result.add(dto);
            }*/

            HashMap lst_det_pick = dao.getDetPickingByIdPedido(id_pedido);
            DetallePickingEntity dpick = null;

            Iterator it = lst_det_pick.keySet().iterator();
            while (it.hasNext()) {
                // Get key (ID_DETALLE)
                String key = it.next().toString();
                dpick = null;
                dpick = (DetallePickingEntity) lst_det_pick.get(key);
                DetallePickingDTO dto = new DetallePickingDTO();
                dto.setId_dpicking(dpick.getId_dpicking());
                dto.setId_detalle(dpick.getId_detalle());
                dto.setId_bp(dpick.getId_bp());
                dto.setId_producto(dpick.getId_producto());
                dto.setId_pedido(dpick.getId_pedido());
                dto.setCBarra(dpick.getCBarra());
                dto.setDescripcion(dpick.getDescripcion());
                dto.setPrecio(dpick.getPrecio().doubleValue());
                dto.setCant_pick(dpick.getCant_pick().doubleValue());
                dto.setSustituto(dpick.getSustituto());
                dto.setCod_bin(dpick.getCod_bin());
                dto.setUni_med(dpick.getUni_med());
                dto.setCod_prod(dpick.getCod_prod());
                result.add(dto);
            }
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return result;
    }

    /**
     * Obtiene los detalles de picking de un pedido, segun el <b>id </b> del
     * pedido. <br>
     * 
     * @param id_pedido
     *            long
     * @return List DetallePickingDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public List getDetPickingHojaDesp2(long id_pedido) throws PedidosException, SystemException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getDetPickingHojaDesp2(id_pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * Obtiene las facturas de un pedido, segun el <b>id </b> del pedido. <br>
     * 
     * @param id_pedido
     *            long
     * @return List FacturaDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public List getFacturasByIdPedido(long id_pedido) throws PedidosException, SystemException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            List lst_fact = dao.getFacturasByIdPedido(id_pedido);
            FacturaEntity fact = null;
            for (int i = 0; i < lst_fact.size(); i++) {
                fact = null;
                fact = (FacturaEntity) lst_fact.get(i);
                FacturaDTO dto = new FacturaDTO();
                dto.setId_factura(fact.getId_factura().longValue());
                dto.setId_pedido(fact.getId_pedido().longValue());
                dto.setRazon(fact.getRazon());
                dto.setRut(fact.getRut().intValue());
                dto.setDv(fact.getDv());
                dto.setDireccion(fact.getDireccion());
                dto.setFono(fact.getFono());
                dto.setGiro(fact.getGiro());
                dto.setNum_doc(fact.getNum_doc().longValue());
                dto.setTipo_doc(fact.getTipo_doc());
                dto.setCiudad(fact.getCiudad());
                dto.setComuna(fact.getComuna());
                /*
                 * FacturaDTO dto = new
                 * FacturaDTO(fact.getId_factura().longValue(),
                 * fact.getId_pedido().longValue(), fact.getRazon(),
                 * fact.getRut().intValue(), fact.getDv(), fact.getDireccion(),
                 * fact.getFono(), fact.getGiro());
                 */
                result.add(dto);
            }

        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return result;
    }

    /**
     * Procesa Feedback del POS en el Proceso de Pago. <br>
     * Nota: Este método tiene <b>Transaccionalidad </b>.
     * 
     * @param fback
     *            POSFeedbackProcPagoDTO
     * 
     * @throws PedidosException,
     *             en uno de los siguientes casos: <br>- El estado de la
     *             transacción es inadecuado. <br>- El estado del pedido es
     *             inadecuado. <br>
     * @throws SystemException,
     *             en caso exista error del sistema. <br>
     *  
     */
    public void doProcesaFeedbackPOS(POSFeedbackProcPagoDTO fback) throws PedidosException, SystemException {

        JdbcPedidosDAO dao1 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        JdbcTrxMedioPagoDAO dao2 = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();

        // Se agrega flag para condicionar generación de Email
        boolean sendEmail = false;

        LogPedidoDTO logex = new LogPedidoDTO();

        // Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();

        try {
            // Iniciamos trx
            trx1.begin();
            // Marcamos los dao's con la transacción
            dao1.setTrx(trx1);
            dao2.setTrx(trx1);
        }  catch (PedidosDAOException e2) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos");
        } catch (TrxMedioPagoDAOException e) {
            logger.error("Error al asignar transacción al dao TrxMP");
            throw new SystemException("Error al asignar transacción al dao TrxMP");
        } catch (Exception e1) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        }

        String rc = fback.getCod_feedback();
        long id_trxmp = fback.getId_trxmp();
        long id_pedido = 0;

        TrxMpEncabezadoEntity encmp = null;

        try {
            // Obtiene encabezado de la trx
            encmp = dao2.getTrxMpEnc(id_trxmp);
        } catch (TrxMedioPagoDAOException e2) {
            logger.error("Error al obtener encabezado trx");
            try {
                trx1.end();
            } catch (DAOException e) {
                throw new SystemException("Error al finalizar la trx");
            }
            throw new SystemException("Error al obtener encabezado trx");
        }

        // obtenemos el id_pedido de la transaccion de pago
        id_pedido = encmp.getId_pedido();
        logger.debug("id_pedido: " + id_pedido);

        /*
         * Realizamos validaciones sobre la transaccion de medio de pago
         *  
         */

        if ((encmp.getId_estado() != Constantes.ID_ESTAD_TRXMP_CREADA)
                && (encmp.getId_estado() != Constantes.ID_ESTAD_TRXMP_RECHAZADA)) {

            logger.info("Trx no está en estado adecuado para realizar la operación");

            // agregamos al log del pedido
            logex.setId_pedido(id_pedido);
            logex.setUsuario("SYSTEM");
            logex
                    .setLog("Se cursó en el POS una trx de pago que estaba en un estado distinto a Creada o Rechazada. Verifique dicho pago, pues el BOL no lo ha considerado.");

            try {
                dao1.addLogPedido(logex);
            } catch (PedidosDAOException e1) {
                logger.error("Error al insertar en el log del pedido");
            }

            // finalizamos transacción
            try {
                trx1.end();
            } catch (DAOException e) {
                throw new SystemException("Error al finalizar la trx");
            }

            // lanzamos excepción
            throw new PedidosException(Constantes._EX_TRX_ESTADO_INADECUADO);

        }

        /*
         * Realizamos validaciones sobre el pedido relacionado
         *  
         */

        PedidoDTO pedido;
        try {
            pedido = dao1.getPedidoById(id_pedido);
        } catch (PedidosDAOException e2) {
            logger.error("Error al obtener pedido");
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                throw new SystemException("Error al hacer rollback");
            }
            throw new SystemException(e2);
        }

        // validamos que el estado del pedido sea el correcto
        if ((pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_EN_PAGO)
                && (pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO)) {

            logger.info("Pedido no está en estado adecuado para realizar la operación");

            // agregamos al log del pedido
            logex.setId_pedido(id_pedido);
            logex.setUsuario("SYSTEM");
            logex
                    .setLog("Se cursó en el POS una trx de pago en un pedido con estado distinto a En Pago o Pago Rechazado. Verifique dicho pago, pues el BOL no lo ha considerado.");

            try {
                dao1.addLogPedido(logex);
            } catch (PedidosDAOException e1) {
                logger.error("Error al insertar en el log del pedido");
            }

            // finalizamos transacción db
            try {
                trx1.end();
            } catch (DAOException e) {
                throw new SystemException("Error al finalizar trx");
            }

            // lanzamos excepción
            throw new PedidosException(Constantes._EX_OPE_ESTADO_INADECUADO);

        }

        /*
         * Procesamos feedback
         *  
         */

        logger.debug("Cod feedback: " + rc);

        // 000: OK
        if (rc.equals("000")) {
            // actualizamos pedido
            try {

                // Actualiza trx con datos de la trx del pos
                dao2.doProcesaPagoOK(fback);
                if (pedido.getTipo_doc().equalsIgnoreCase("B")) {
                    //INGRESA LA BOLETA EN LA NUEVA TABLA "BO_NUM_DOC"
                    FacturasDTO fact = new FacturasDTO();
                    fact.setId_pedido(pedido.getId_pedido());
                    fact.setId_trxmp(fback.getId_trxmp());
                    fact.setNum_doc(fback.getNum_boleta());
                    fact.setLogin("SYSTEM");
                    dao1.ingFactura(fact);
                }

                logger.info("Trx de pago: " + id_trxmp + " realizada OK");

                // Agrega al log del pedido
                LogPedidoDTO log = new LogPedidoDTO();
                log.setId_pedido(id_pedido);
                log.setUsuario("SYSTEM");
                log.setLog("Trx de pago: " + id_trxmp + " realizada OK");

                try {
                    dao1.addLogPedido(log);
                } catch (PedidosDAOException e) {
                    logger.info("No se pudo agregar información al log del pedido");
                }

                // verificar que todas las trx han sido realizadas para cambiar
                // estado al pedido
                List lst_trx = new ArrayList();
                lst_trx = dao2.getTrxMpByIdPedido(id_pedido);

                boolean ok_trxs = true;
                for (int i = 0; i < lst_trx.size(); i++) {
                    MonitorTrxMpDTO trxmp1 = new MonitorTrxMpDTO();
                    trxmp1 = (MonitorTrxMpDTO) lst_trx.get(i);
                    if (trxmp1.getId_estado() != Constantes.ID_ESTAD_TRXMP_PAGADA) {
                        ok_trxs = false;
                    }
                }

                // en caso que todas sus transacciones esten pagadas
                if (ok_trxs == true) {

                    long monto_trx = dao1.getMontoTotalTrxByIdPedido(id_pedido);
                    if (monto_trx > pedido.getMonto_reservado()&& !Constantes.MEDIO_PAGO_LINEA_CREDITO.equalsIgnoreCase(pedido.getMedio_pago())) {
                    	try {
                        	ExcesoCtrl oExceso = new ExcesoCtrl();
                        	oExceso.modPedidoExcedido(id_pedido, true);
                        	oExceso = null;
                        	LogPedidoDTO logExceso = new LogPedidoDTO();
                        	logExceso.setId_pedido(id_pedido);
                        	logExceso.setUsuario("SYSTEM");
                        	logExceso.setLog("OP con exceso al generar la boleta. POS_MONTO_FP > Monto_reservado");
                            dao1.addLogPedido(logExceso);
                        } catch (SystemException e) {
							logger.error("modPedidoExcedido ServiceException::",e);
						}catch (PedidosDAOException e) {
                            logger.info("No se pudo agregar información al log del pedido");                           
						}
                    	/*try {
							modPedidoExcedido(id_pedido, true);
						} catch (ServiceException e) {
							logger.error("modPedidoExcedido ServiceException::",e);
						}*/
                    } else {
                        if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_LINEA_CREDITO)) {
                            // cambiamos el estado de la OP a Pagado
                            dao1.setModEstadoPedido(id_pedido,Constantes.ID_ESTAD_PEDIDO_PAGADO);

                            logger.info("Cambio estado pedido a Pagado");

                            // agregamos al log
                            LogPedidoDTO log2 = new LogPedidoDTO();
                            log2.setId_pedido(id_pedido);
                            log2.setUsuario("SYSTEM");
                            log2.setLog("OP cambia estado a Pagada");

                            try {
                                dao1.addLogPedido(log2);
                            } catch (PedidosDAOException e) {
                                logger.info("No se pudo agregar información al log del pedido");
                            }
                        } else if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_TBK)) {
                            //se realiza la captura WebPay para el pedido
                            //ANTES DE CAMBIAR EL ESTADO DEL PEDIDO SE DEBE
                            //  0.- ACTUALIZAR FLAG EN OP

                            if (!pedido.getNum_mp().equals("X") && pedido.getMonto_reservado() == 0D) {
                                logger.info("El estado de la OP " + pedido.getId_pedido() + " debe ser cambiado en forma manual desde el BOC");
                                // cambiamos el estado de la OP a Pagado
                                /*dao1.setModEstadoPedido(id_pedido, Constantes.ID_ESTAD_PEDIDO_PAGADO);

                                logger.info("Cambio estado pedido a Pagado");

                                // agregamos al log
                                LogPedidoDTO log2 = new LogPedidoDTO();
                                log2.setId_pedido(id_pedido);
                                log2.setUsuario("SYSTEM");
                                log2.setLog("OP cambia estado a Pagada");

                                try {
                                    dao1.addLogPedido(log2);
                                } catch (PedidosDAOException e) {
                                    logger.info("No se pudo agregar información al log del pedido");
                                }*/
                    	        
                            } else {
                                dao1.setActivarFlagCaptura(id_pedido);
                            }



                            //  1.- REALIZAR LA CAPTURA DEL MONTO DEL PEDIDO EN TBK O CAT
                        	
                            //  2.- EN CASO DE PAGAR CON TBK, SE DEBE INSERTAR TRX EN EL SWITCH BBR
                            
                            /**
                        	 * Se comenta el paso 2, esto se realizara mediante un servlet, desde Proyecto CapturarPagoWP
                        	 * */

                            //******* 2.- INSERTAR TRX EN EL SWITCH BBR *******
                            //************** SOLO PARA TRANSBANK **************
                            /**
                            if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_TBK)){
                                ClienteSwitch cliSwitch = new ClienteSwitch();
                                if (cliSwitch.insertaTrxSwitch(id_pedido)){
                                    logger.info("Registro Insertado Correctamente");
                                }else{
                                    logger.info("Error en la Inserción del Registro");
                                }
                            }

                            //***************************************************
                            // cambiamos el estado de la OP a Pagado
                            dao1.setModEstadoPedido(id_pedido, Constantes.ID_ESTAD_PEDIDO_PAGADO);

                            logger.info("Cambio estado pedido a Pagado");

                            // agregamos al log
                            LogPedidoDTO log2 = new LogPedidoDTO();
                            log2.setId_pedido(id_pedido);
                            log2.setUsuario("SYSTEM");
                            log2.setLog("OP cambia estado a Pagada");

                            try {
                                dao1.addLogPedido(log2);
                            } catch (PedidosDAOException e) {
                                logger.info("No se pudo agregar información al log del pedido");
                            }
                                 */

                    	} else if( pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_CAT) ) {
                    	    
                    	    if (!"X".equals(pedido.getNum_mp()) && pedido.getMonto_reservado() == 0D){
                    	        logger.info("El estado de la OP " + pedido.getId_pedido()+ " debe ser cambiado en forma manual desde el BOC");
                    	        /*dao1.setModEstadoPedido(id_pedido, Constantes.ID_ESTAD_PEDIDO_PAGADO);
                                // agregamos al log
                                LogPedidoDTO log2 = new LogPedidoDTO();
                                log2.setId_pedido(id_pedido);
                                log2.setUsuario("SYSTEM");
                                log2.setLog("OP cambia estado a Pagada");

                                try {
                                    dao1.addLogPedido(log2);
                                } catch (PedidosDAOException e) {
                                    logger.info("No se pudo agregar información al log del pedido");
                                }*/

                            } else {
                                //1- Obtener informacion para confirmar - boton de pago
                        		//long monto_trx = dao1.getMontoTotalTrxByIdPedido(id_pedido);
                        		BotonPagoDTO bp = dao1.botonPagoGetByPedidoAprobado(id_pedido);
                        		//String cod_autoriz = dao1.getCodAutorizByOP(id_pedido);
                                //obtener datos para solicitar la captura
                                String numComercio = "";
                                ResourceBundle rb = ResourceBundle.getBundle("bo");
                        		/*if (pedido.getId_usuario_fono() != 0 && pedido.getOrigen().equals("V")){
                        			numComercio = rb.getString("CAT.numComercioNoAutenticado");
                        		}else{
                        			numComercio = rb.getString("CAT.numComercioAutenticado");
                        		}*/
                                if (pedido.getId_usuario_fono() != 0 && pedido.getOrigen().equals("W")) {
                                    numComercio = rb .getString("CAT.numComercioFOCliFono");
                                    //Venta Empresas
                                } else if (pedido.getOrigen().equals("V")) {
                                    numComercio = rb.getString("CAT.numComercioFOVE");
                                    //Compra realizada por el Cliente
                                } else if (pedido.getId_usuario_fono() == 0 && pedido.getOrigen().equals("W")) {
                                    numComercio = rb.getString("CAT.numComercioFOCliente");
                                }

                                SolicCapturaCAT sol = new SolicCapturaCAT();

                                //El codigo de DPC de captura solo es para la confirmación de la reserva de cupo. 
                                //En el caso de la anulación de la reserva enviar ese dato con ceros.
                                LocalDTO loc = dao1.getLocalById(pedido.getId_local());
                                sol.setDpc(FormatUtils.addCharToString("" + loc.getDpc(), "0", 12, FormatUtils.ALIGN_RIGHT));
                                //System.out.println("*** DPC LOCAL ("+loc.getId_local()+"): " + sol.getDpc());

                                sol.setNumempre(numComercio);
                        		//TODO: verificar si la fecha de transaccion es la fecha de ingreso de OP
                        		//String ftrx = pedido.getFingreso().substring(6) + pedido.getFingreso().substring(3,5) + pedido.getFingreso().substring(0,2);
                        		//String ftrx = pedido.getFingreso().substring(0,4) + pedido.getFingreso().substring(5,7) + pedido.getFingreso().substring(8) ;
                        		
                                String ftrx = Formatos.calendarToString(bp.getFechaAutorizacion(), "yyyyMMdd");
                                sol.setFectrans(ftrx);
                                sol.setCodtrans(Constantes.BTN_PAGO_COD_TRANSACCION);
                                //sol.setIdtrn(Formatos.completaCadena(String.valueOf(id_pedido), ' ', 16-(String.valueOf(id_pedido).length()),'D'));
                                sol.setIdtrn(FormatUtils.addCharToString(bp.getIdCatPedido()+ "", " ", 16, FormatUtils.ALIGN_LEFT));
                                sol.setTipoauto(" ");
                                sol.setCodautor("        "); //Espacios en blanco
                                //sol.setMonconta(Formatos.completaCadena(String.valueOf(monto_trx), '0', 9-(String.valueOf(monto_trx).length()), 'I'));
                                sol.setMonconta(FormatUtils.addCharToString(monto_trx + "", "0", 9, FormatUtils.ALIGN_RIGHT));
                                logger.debug("Fecha/Hora Ingreso OP (" + id_pedido + "): " + pedido.getFingreso() + " " + Formatos.getHoraActualBtnPago());
                                sol.setHorenvio(Formatos.getHoraActualBtnPago());
                                //2. SE REALIZA CAPTURA HACIA CAT - boton de pago
                                CapturarCAT capturar = new CapturarCAT();
                                RptaCapturaCAT rpta = new RptaCapturaCAT();
                                try {
                                    rpta = capturar.confirmar_reserva(sol);
                                } catch (Exception e) {
                                    logger.error("Error al capturar: " + e.getMessage());
                                    logger.error(getStackTrace(e));
                                }
                        		//3. Segun respuesta, se aprueba o se rechaza...
                        		//if (rpta.getTipoauto().equals(Constantes.BTN_PAGO_APROBADO)) {
                                if ("010".equals(rpta.getCodrespu())) {
                                    //cambiamos el estado de la OP a Pagado
                                    dao1.setModEstadoPedido(id_pedido,Constantes.ID_ESTAD_PEDIDO_PAGADO);
                                    logger.info("Cambio estado pedido a Pagado");
                                    // agregamos al log
                                    LogPedidoDTO log2 = new LogPedidoDTO();
                                    log2.setId_pedido(id_pedido);
                                    log2.setUsuario("SYSTEM");
                                    log2.setLog("OP cambia estado a Pagada");
                                    try {
                                        dao1.addLogPedido(log2);
                                    } catch (PedidosDAOException e) {
                                        logger.info("No se pudo agregar información al log del pedido");
                                    }
                                } else if (Constantes.BTN_PAGO_RECHAZADO.equals(rpta.getTipoauto())) {
                                    // Cambia estado de la transaccion a Rechazada
                                    try {
                                        dao2.doRechazaPago(fback);
                                    } catch (TrxMedioPagoDAOException e) {
                                        logger.error("Error al rechazar pago");
                                        try {
                                            trx1.rollback();
                                        } catch (DAOException e1) {
                                            throw new SystemException("Error al hacer rollback");
                                        }
                                        throw new SystemException(e);
                                    }
                                    // Cambia estado del pedido a Pago Rechazado
                                    try {
                                        dao1.setModEstadoPedido(id_pedido, Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO);
                                    } catch (PedidosDAOException e) {
                                        logger.error("Error al modificar estado pedido");
                                        try {
                                            trx1.rollback();
                                        } catch (DAOException e1) {
                                            throw new SystemException("Error al hacer rollback");
                                        }
                                        throw new SystemException(e);
                                    }
                                    //agregamos al log
                                    LogPedidoDTO log2 = new LogPedidoDTO();
                                    log2.setId_pedido(id_pedido);
                                    log2.setUsuario("SYSTEM");
                                    log2.setLog("OP fue rechazada desde Boton de pago.");
                                    try {
                                        dao1.addLogPedido(log2);
                                        log2.setLog("Rpta de CAT:" + rpta.getCodrespu() + "," + rpta.getDatrespu());
                                        dao1.addLogPedido(log2);
                                    } catch (PedidosDAOException e) {
                                        logger.info("No se pudo agregar información al log del pedido");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (TrxMedioPagoDAOException e) {
                logger.error("Error al utilzar dao doProcesaPagoOK");
                try {
                    trx1.rollback();
                } catch (DAOException e1) {
                    throw new SystemException("Error al hacer rollback");
                }
                throw new SystemException(e);
            } catch (PedidosDAOException e) {
                logger.error("Error al utilzar dao pedidos");
                try {
                    trx1.rollback();
                } catch (DAOException e1) {
                    throw new SystemException("Error al hacer rollback");
                }
                throw new SystemException(e);
            }

        }
        // En el resto de los casos es Rechazo
        else {

            // Cambia estado de la transaccion a Rechazada
            try {
                dao2.doRechazaPago(fback);
            } catch (TrxMedioPagoDAOException e) {
                logger.error("Error al rechazar pago");
                try {
                    trx1.rollback();
                } catch (DAOException e1) {
                    throw new SystemException("Error al hacer rollback");
                }
                throw new SystemException(e);
            }

            // Cambia estado del pedido a Pago Rechazado
            try {
                dao1.setModEstadoPedido(id_pedido,Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO);
            } catch (PedidosDAOException e) {
                logger.error("Error al modificar estado pedido");
                try {
                    trx1.rollback();
                } catch (DAOException e1) {
                    throw new SystemException("Error al hacer rollback");
                }
                throw new SystemException(e);

            }

            // 001: Error en validación de productos
            if (rc.equals("001")) {

                // Agrega al log del pedido
                LogPedidoDTO log = new LogPedidoDTO();
                log.setId_pedido(id_pedido);
                log.setUsuario("SYSTEM");
                log.setLog("Trx pago N° " + id_trxmp + " - Error en validación de producto:");

                try {
                    dao1.addLogPedido(log);
                } catch (PedidosDAOException e) {
                    logger.info("No se pudo agregar información al log del pedido");
                }

                List prods = fback.getProds();
                for (int i = 0; i < prods.size(); i++) {
                    POSProductoDTO p1 = (POSProductoDTO) prods.get(i);

                    log.setLog("Prod: " + p1.getCbarra() + " , err: " + p1.getCod_ret());

                    try {
                        dao1.addLogPedido(log);
                    } catch (PedidosDAOException e) {
                        logger.info("No se pudo agregar información al log del pedido");
                    }

                }

                logger.info("Trx N°: " + id_trxmp + " - Error en validación de productos");

            }
            // 002: Error en validación datos forma de pago
            else if (rc.equals("002")) {

                // Agrega al log del pedido
                LogPedidoDTO log = new LogPedidoDTO();
                log.setId_pedido(id_pedido);
                log.setUsuario("SYSTEM");
                log.setLog("Trx N°: " + id_trxmp + " - Error en datos forma de pago. Cód: " + fback.getFp_error() + " " 
                	+ fback.getFp_glosa());

                try {
                    dao1.addLogPedido(log);
                } catch (PedidosDAOException e) {
                    logger.info("No se pudo agregar información al log del pedido");
                }

                logger.info("Trx N°: " + id_trxmp + "Error en validación de formas de pago. Cód: "
                        + fback.getFp_error() + " " + fback.getFp_glosa());

            }
            // 003: Error excede límite de productos
            else if (rc.equals("003")) {

                // Agrega al log del pedido
                LogPedidoDTO log = new LogPedidoDTO();
                log.setId_pedido(id_pedido);
                log.setUsuario("SYSTEM");
                log.setLog("Trx N°: " + id_trxmp + " - Error, se excede el límite de productos");

                try {
                    dao1.addLogPedido(log);
                } catch (PedidosDAOException e) {
                    logger.info("No se pudo agregar información al log del pedido");
                }

                logger.info("Trx N°: " + id_trxmp + " - Error, excede límite de productos");

            }
            // 004: Error al procesar pago
            else if (rc.equals("004")) {

                // Agrega al log del pedido
                LogPedidoDTO log = new LogPedidoDTO();
                log.setId_pedido(id_pedido);
                log.setUsuario("SYSTEM");
                log.setLog("Trx N°: " + id_trxmp + " - Error al procesar pago. Cód: " + fback.getFp_error() + " - " 
                		+ fback.getFp_glosa());

                try {
                    dao1.addLogPedido(log);
                } catch (PedidosDAOException e) {
                    logger.info("No se pudo agregar información al log del pedido");
                }

                logger.info("Trx N°: " + id_trxmp + " - Error al procesar pago. Cód: " + fback.getFp_error() + " - " 
                		+ fback.getFp_glosa());

            }
            // si no concuerda ningun codigo de respuesta
            else {
                // Agrega al log del pedido
                LogPedidoDTO log = new LogPedidoDTO();
                log.setId_pedido(id_pedido);
                log.setUsuario("SYSTEM");
                log.setLog("Trx N°: " + id_trxmp + " - Error al procesar pago. Cód: " + fback.getFp_error() + " - " 
                		+ fback.getFp_glosa());

                try {
                    dao1.addLogPedido(log);
                } catch (PedidosDAOException e) {
                    logger.info("No se pudo agregar información al log del pedido");
                }

                logger.error("Trx N°: " + id_trxmp + " - Código de Feedback erróneo");

            }

        } // end if rc=000

        // cerramos trx
        try {
            trx1.end();
            sendEmail = true;
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }

        //Envío de Email
        if (sendEmail) {
            try {
                sendEmailByPedido(id_pedido);
            } catch (PedidosException e) {
                logger.error("No se envió email pedido: " + id_pedido
						+ " / causa: Pedido sin todas sus transacciones pagadas");
            } catch (SystemException e) {
                logger.error("No se envió email pedido: " + id_pedido + " / causa: " + e.getMessage());
            }
        } else {
            logger.error("No se envió email pedido: " + id_pedido + " / causa: Error al recibir el Feedback del POS");
        }

    }

    /**
     * Actualiza los datos de una factura, segun la información contenida en
     * <b>ProcModFacturaDTO </b>. <br>
     * 
     * @param prm
     *            ProcModFacturaDTO
     * @return boolean, devuelve <i>true </i> si la actualización fue exitosa,
     *         caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en el caso que no exista la factura.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public boolean setModFactura(ProcModFacturaDTO prm) throws PedidosException, SystemException {
        boolean result = false;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory( DAOFactory.JDBC).getPedidosDAO();
        try {
            /*
             * if(!dao.isPedidoById(prm.getId_pedido())){ throw new
             * PedidosException(Constantes._EX_OPE_ID_NO_EXISTE); }
             */
            result = dao.setModFactura(prm);

        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de factura");
                throw new PedidosException(Constantes._EX_OPE_ID_FACT_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return result;
    }

    /**
     * Permite agregar o eliminar un producto de un pedido, segun la información
     * contenida en <b>ProcModPedidoProdDTO </b>. <br>
     * Nota: Este método tiene <b>Transaccionalidad </b>.
     * 
     * @param prm
     *            ProcModPedidoProdDTO
     * @return boolean, devuelve <i>true </i> si la actualización fue exitosa,
     *         caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en uno de los siguientes casos: <br>- No existe el pedido.
     *             <br>- No existe el producto. <br>- El producto no tiene
     *             sector. <br>- El precio no existe. <br>- El código de barra
     *             del producto no existe. <br>- La jornada de picking
     *             relacionada al pedido no existe
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public boolean setModPedidoProd(ProcModPedidoProdDTO prm) throws PedidosException, SystemException {

        boolean result = false;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        JdbcJornadasDAO daoJ = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
        //JdbcProductosSapDAO daoS = (JdbcProductosSapDAO)
        // cl.bbr.jumbocl.contenidos.dao.DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosSapDAO();

        try {
            PedidoDTO pedido = dao.getPedidoById(prm.getId_pedido());

            if (pedido == null) {
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
            }
            long id_local = pedido.getId_local();

            //result = dao.setModPedidoProd(prm);
            if (prm.getAccion().equals("agregar")) {
                logger.debug("Accion es agregar");
                //Obtenemos información del producto

                ProductoEntity prod1 = null;
                prod1 = dao.getProductoPedidoByIdProdFO(prm.getId_producto());//Id_Producto en el FO

                if (prod1 == null) {
                    logger.debug("El producto no existe");
                    throw new PedidosException(Constantes._EX_OPE_ID_PROD_NO_EXISTE);
                }
                //si accion es agregar producto al pedido
                long id_prod_bo = prod1.getId_bo().longValue();
                ProductoSapEntity dto = dao.getProductSapById(id_prod_bo);

                //verificar el estado del producto web
                if (!prod1.getEstado().equals(Constantes.ESTADO_PUBLICADO)) {
                    throw new PedidosException(Constantes._EX_PROD_DESPUBLICADO);
                }
                //revisar si tiene sector asociado
                long id_sec = dao.getSectorByProdId(id_prod_bo);
                logger.debug("id_sec:" + id_sec);
                if (id_sec < 0) {
                    throw new PedidosException(Constantes._EX_PSAP_SECTOR_NO_EXISTE);
                }
                //revisar si tiene precio
                int cant_precios = dao.getPreciosByProdId(id_prod_bo).size();
                logger.debug("cant_precios:" + cant_precios);
                if (cant_precios == 0) {
                    throw new PedidosException(Constantes._EX_OPE_PRECIO_NO_EXISTE);
                }
                //revisar si tiene cod_barras
                int cant_cbarra = dao.getCodBarrasByProdId(id_prod_bo).size();
                logger.debug("cant_cbarra:" + cant_cbarra);
                if (cant_cbarra == 0) {
                    throw new PedidosException(Constantes._EX_OPE_CODBARRA_NO_EXISTE);
                }

                ProductosPedidoDTO prod = new ProductosPedidoDTO();
                prod.setId_pedido(prm.getId_pedido());
                prod.setId_producto(id_prod_bo);
                prod.setId_sector(id_sec);
                prod.setCod_producto(dto.getCod_prod_1());
                prod.setUnid_medida(dto.getUni_med());
                prod.setDescripcion(prod1.getTipo() + " " + prod1.getNom_marca() + " " + prod1.getDesc_corta());
                prod.setCant_solic(prm.getCantidad());
                prod.setCant_spick(prm.getCantidad());
                prod.setObservacion(prm.getObservacion());
                if (!prod.getObservacion().equals("") || prod.getObservacion() == null)
                    prod.setCon_nota("N");
                else
                    prod.setCon_nota("S");
                prod.setPesable(prod1.getEs_pesable());
                prod.setPreparable(prod1.getEs_prep());
                //obtener precio
                logger.debug("id_local:" + id_local);
                double precio = dao.getPrecioByLocalProd(id_local, id_prod_bo);
                logger.debug("precio:" + precio);
                prod.setPrecio(precio);
                prod.setPrecio_lista(precio);

                if (prm.getIdCriterio() == 0) {
                    prod.setIdCriterio(1);
                } else {
                    prod.setIdCriterio(prm.getIdCriterio());
                }
                if (prod.getIdCriterio() == 4) {
                    prod.setDescCriterio(prm.getDescCriterio());
                } else {
                    prod.setDescCriterio("");
                }

                boolean existeProd = dao.verificaProductoEnPedido(prod);
                boolean resb = false;
                if (existeProd) {
                    resb = dao.actualizaCantidadProductoPedido(prod);
                    logger.debug("agregar:" + resb);
                } else {
                    resb = (dao.agregaProductoPedido(prod) > 0);
                    logger.debug("agregar:" + resb);
                }

                if (resb) {
                    //agregar la accion en el log del pedido
                    LogPedidoDTO ldto = new LogPedidoDTO();
                    ldto.setId_pedido(prm.getId_pedido());
                    ldto.setUsuario(prm.getUsr_login());
                    ldto.setLog(prm.getMnsAgreg() + ":" + dto.getDes_corta());
                    dao.addLogPedido(ldto);
                    //recalcular pedido
                    boolean resRec = dao.recalcPedido(prm.getId_pedido(), pedido.getTipo_doc());
                    logger.debug("resRec=" + resRec);
                    //aumentar la capacidad de picking de la jornada
                    // correspondiente al pedido
                    if (!pedido.getTipo_ve().equals("S")) {
                        daoJ.doOcupaCapacidadPicking(pedido.getId_jpicking(), (int) prod.getCant_solic());
                    }
                }
            }

            //busca promos antes de eliminar
            List lst_promos_resum = dao.getResumenPromocionPedidos(prm.getId_pedido());
            boolean encontrado = false;

            //si va a eliminar el producto busca si tiene promociones y lo marca con un flag de recalculo
            for (int k = 0; k < lst_promos_resum.size(); k++) {
                ResumenPedidoPromocionDTO res = (ResumenPedidoPromocionDTO) lst_promos_resum.get(k);
                List lst_productos = dao.getProductosByPromocionPedido(res.getId_promocion(), prm.getId_pedido());
                for (int l = 0; l < lst_productos.size(); l++) {
                    ProductosRelacionadosPromoDTO prodrel = (ProductosRelacionadosPromoDTO) lst_productos.get(l);
                    if (prodrel.getId_detalle() == prm.getId_detalle()) {
                        logger.debug("el producto id_detalle:" + prm.getId_detalle() + " esta en promocion:" + res.getPromo_codigo());
                        encontrado = true;
            		}
            		else{
                        logger.debug("el producto id_detalle:" + prm.getId_detalle() + " NO esta en promocion. es distinto a :" + prodrel.getId_detalle());
                    }
                }
            }

            //si accion = eliminar: eliminar un prod
            if (prm.getAccion().equals("eliminar")) {
                ProductoSapEntity dto = dao.getProductSapById(prm.getId_producto());
                ProductosPedidoDTO prod = new ProductosPedidoDTO();
                prod.setId_detalle(prm.getId_detalle());
                prod.setId_pedido(prm.getId_pedido());
                boolean resb = dao.elimProductoPedido(prod);
                logger.debug("desasociar:" + resb);
                if (resb) {
                    //agregar la accion en el log del pedido
                    LogPedidoDTO ldto = new LogPedidoDTO();
                    ldto.setId_pedido(prm.getId_pedido());
                    ldto.setUsuario(prm.getUsr_login());
                    ldto.setLog(prm.getMnsElim() + ":" + dto.getDes_corta());
                    dao.addLogPedido(ldto);
                    //recalcular pedido
                    boolean resRec = dao.recalcPedido(prm.getId_pedido(), pedido.getTipo_doc());
                    logger.debug("resRec=" + resRec);
                    //disminuir la capacidad de picking de la jornada
                    // correspondiente al pedido
                    if (!pedido.getTipo_ve().equals("S")) {
                        daoJ.doOcupaCapacidadPicking(pedido.getId_jpicking(), ((int) prm.getCantidad()) * (-1));

                        //  genera alerta dura si el producto tenia promociones promociones y si no es de venta empresas                        
                        if (encontrado) {
                            //existian promociones para el producto, setea el flag de recalculo por eliminacion de producto si no existe
                            List lst_alertas = dao.getAlertasPedido(prm.getId_pedido());
                            boolean alerta_encontrada = false;
                            for (int ia = 0; ia < lst_alertas.size(); ia++) {
                                AlertaDTO alerta = (AlertaDTO) lst_alertas.get(ia);
                                if (alerta.getId_alerta() == Constantes.ALE_RECALC_PROMO_ELIM_PROD) {
                                    alerta_encontrada = true;
                                }
                            }
                            if (!alerta_encontrada) {
                                pedido.setFlg_recalc_prod(true);
                                dao.updFlagRecalculoPedido(pedido);
                                //se pone la alerta dura por eliminacion de producto
                                dao.addAlertToPedido(prm.getId_pedido(), Constantes.ALE_RECALC_PROMO_ELIM_PROD);
                            }
                        }

                    }
                }
            }

        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de factura");
                throw new PedidosException(Constantes._EX_OPE_ID_FACT_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        } catch (ProductosSapDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de producto sap");
                throw new PedidosException(Constantes._EX_PSAP_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        } catch (JornadasDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de jornada");
                throw new PedidosException(Constantes._EX_JPICK_ID_INVALIDO, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return result;
    }

    /**
     * Obtiene la cantidad de productos del pedido, según el <b>id </b> del
     * pedido. <br>
     * 
     * @param id_pedido
     *            long
     * @return int, cantidad de productos
     * @throws PedidosException,
     *             en caso que no existe el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public int valMaxNumProductosByPedido(long id_pedido) throws PedidosException, SystemException {
        int result = 0;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        logger.debug("En valMaxNumProductosByPedido");

        try {

            if (!dao.isPedidoById(id_pedido)) {
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
            }
            List lst_det_pedido = dao.getDetallesPedido(id_pedido);
            for (int i = 0; i < lst_det_pedido.size(); i++) {
                ProductosPedidoDTO prodPed = (ProductosPedidoDTO) lst_det_pedido.get(i);
                if (prodPed.getUnid_medida().equals(Constantes.UMEDSAP_ST) && prodPed.getCant_solic() == 1) {
                    result += 1; //prodPed.getCant_solic();
                } else {
                    result += 2; //(2*prodPed.getCant_solic());
                }
            }
            logger.debug("result:" + result);

        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);

        }
        return result;
    }

    /**
     * Obtiene la cantidad de productos del pedido, según el <b>id </b> del
     * pedido. <br>
     * 
     * @param id_pedido
     *            long
     * @return int, cantidad de productos
     * @throws PedidosException,
     *             en caso que no existe el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public double getMontoTotalDetPickingByIdPedido(long id_pedido) throws PedidosException, SystemException {
        double MontoTotal = 0D;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        logger.debug("En getMontoTotalDetPickingByIdPedido");

        try {

            MontoTotal = dao.getMontoTotalDetPickingByIdPedido(id_pedido);
            logger.debug("MontoTotal:" + MontoTotal);

        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return MontoTotal;
    }

    /**
     * Obtiene la cantidad de productos del pedido, según el <b>id </b> del
     * pedido. <br>
     * 
     * @param id_pedido
     *            long
     * @return int, cantidad de productos
     * @throws PedidosException,
     *             en caso que no existe el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public long getMontoTotalTrxByIdPedido(long id_pedido) throws PedidosException, SystemException {
        long MontoTotal = 0L;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        logger.debug("En getMontoTotalDetPickingByIdPedido");

        try {

            MontoTotal = dao.getMontoTotalTrxByIdPedido(id_pedido);
            logger.debug("MontoTotal:" + MontoTotal);

        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return MontoTotal;
    }

    /**
     * Obtiene la cantidad de productos del pedido, según el <b>id </b> del
     * pedido. <br>
     * 
     * @param id_pedido
     *            long
     * @return int, cantidad de productos
     * @throws PedidosException,
     *             en caso que no existe el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public double getMontoTotalDetPedidoByIdPedido(long id_pedido) throws PedidosException, SystemException {
        double MontoTotal = 0D;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        logger.debug("En getMontoTotalDetPedidoByIdPedido");

        try {

            MontoTotal = dao.getMontoTotalDetPedidoByIdPedido(id_pedido);
            logger.debug("MontoTotal:" + MontoTotal);

        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod de pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return MontoTotal;
    }

    /**
     * Permite generar transacciones de un pedido, segun el <b>id </b> del
     * pedido. <br>
     * Nota: Este método tiene <b>Transaccionalidad </b>.
     * 
     * @param id_pedido
     *            long
     * 
     * @throws PedidosException,
     *             en uno de los siguientes casos: <br>- No existe el pedido.
     *             <br>- Existe productos con información inválida. <br>-
     *             Duplicación en las transacciones. <br>
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public void doGeneraTrxMp(long id_pedido) throws PedidosException, SystemException, ServiceException {
        int peso_prod = 0;
        int num_encabezados = 0;
        int suma_ponderada = 0;
        int num_productos = 0;
        double monto_detalles = 0.0;
        String clave = "";

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        JdbcTrxMedioPagoDAO daoT = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();

        // Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();

        // Iniciamos trx
        try {
            trx1.begin();
        } catch (Exception e1) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        }

        // Marcamos los dao's con la transacción
        try {
            daoT.setTrx(trx1);
        } catch (TrxMedioPagoDAOException e2) {
            logger.error("Error al asignar transacción al dao TrxMp");
            throw new SystemException("Error al asignar transacción al dao TrxMp");
        }
        try {
            dao.setTrx(trx1);
        } catch (PedidosDAOException e2) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos");
        }

        try {
            //agregar metodos a utilizar daoT.doGeneraTrxMp(id_pedido);
            //invoca al método de validacion del pedido
            if (!dao.isPedidoById(id_pedido)) {
                trx1.rollback();
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
            }
            logger.debug("doGeneraTrxMp con id_pedido:" + id_pedido);
            //recuperar pedido
            PedidoDTO pedido = new PedidoDTO();
            pedido = dao.getPedidoById(id_pedido);
            
            
            
            boolean generar_trx_pago = false;
            boolean generar_trx_pago_mxn = true;
            ExcesoCtrl oExceso = new ExcesoCtrl();
            //_____________________________________________________
            //FIXME::Valida exceso (Correccion automatica)
            if (pedido.getMonto_reservado() == 0D) {
                generar_trx_pago = true;
            } else if (Constantes.MEDIO_PAGO_LINEA_CREDITO.equalsIgnoreCase(pedido.getMedio_pago())) {
                generar_trx_pago = true;
            } else {
            	if ( productoEnOPConSistitutosMxN(pedido.getId_pedido()) && !Constantes.MEDIO_PAGO_LINEA_CREDITO.equalsIgnoreCase( pedido.getMedio_pago() ) ) {
            		generar_trx_pago_mxn = false;
            		
            	}else if(oExceso.isActivaCorreccionAutomatica()){
            		if (!oExceso.isOpConExceso(id_pedido)) {
                        generar_trx_pago = true;
                    }   
            	}else{
            		if (!oExceso.isOpConExceso(id_pedido, false)) {
                        generar_trx_pago = true;
                    }            		
            	}            	
            }
            
            /*else if (!isOpConExceso(id_pedido)) {
                generar_trx_pago = true;
            }*/
            //________________________________________________________

            if (!generar_trx_pago) {
            	if(!generar_trx_pago_mxn){            		
            		 throw new PedidosException(Constantes._EX_OPE_MONTO_MXN);
            	}else{
	            	logger.info("No es posible generar TRX por exceso en OP" +id_pedido);
	            	
	                try {
						//modPedidoExcedido(id_pedido, true);
						oExceso.modPedidoExcedido(id_pedido, true);
					} catch (SystemException e) {
						logger.error("modPedidoExcedido ServiceException:",e);
					}
	                logger.info("Se activa flag monto_excedido BO_PEDIDOS en 1 OP" +id_pedido);
	                throw new PedidosException(Constantes._EX_OPE_MONTO_PICK_MAYOR);
            	}
            }
            
            //Generar las transacciones de pago necesarias, flujo normal
            //generar un entity encabezado trxmp copiado los datos
            TrxMpEncabezadoEntity trxmpenc = new TrxMpEncabezadoEntity();
            trxmpenc.setId_pedido(pedido.getId_pedido());
            trxmpenc.setId_estado(Constantes.ID_ESTAD_TRXMP_CREADA);
            trxmpenc.setId_local(pedido.getId_local());
            trxmpenc.setId_cliente(pedido.getId_cliente());

            //el costo de despacho solo se transmite a la primera transaccion
            // generada
            trxmpenc.setCosto_despacho(pedido.getCosto_despacho());
            // trxmpenc.setFcreacion() current date
            logger.debug("---Encabezado---");
            logger.debug("id_pedido:" + trxmpenc.getId_pedido());
            logger.debug("id_local:" + trxmpenc.getId_local());
            logger.debug("id_cliente:" + trxmpenc.getId_cliente());
            logger.debug("costo_despacho:" + trxmpenc.getCosto_despacho());

            logger.debug("CBARRA Despacho: " + Constantes.DESPACHO_COD_BARRA);

            //recuperar detalles
            HashMap detalles_pedido = dao.getDetPickingByIdPedido(id_pedido);
            //List detalles_pedido = dao.getDetPickingByIdPedido(id_pedido);
            List detalle_trx = new ArrayList();

            logger.debug("registros de productos pickeados:" + detalles_pedido.size());

            //revisa si existen productos con id_productos null o cantidad 0
                /*for (int x = 0; x < detalles_pedido.size(); x++) {
                    DetallePickingEntity prod = (DetallePickingEntity) detalles_pedido.get(x);
                    if (prod.getPrecio().doubleValue() == 0.0) {
                        logger.debug("el registro " + x + " tiene datos inválidos en su id_produto / cantidad");
                        logger.debug("reg num    :" + x);
                        logger.debug("id_detalle :" + prod.getId_detalle());
                        logger.debug("id bp      :" + prod.getId_bp());
                        logger.debug("id dpicking:" + prod.getId_dpicking());
                        logger.debug("cant pick  :" + prod.getCant_pick());
                        logger.debug("precio     :" + prod.getPrecio());
                        logger.debug("descripcion:" + prod.getDescripcion());
                        logger.debug("sustituto  :" + prod.getSustituto());
                        logger.debug("cod_bin    :" + prod.getCod_bin());
                        trx1.rollback();
                        throw new PedidosException(Constantes._EX_TRX_PRODS_INVALIDOS);
                    }
                }*/
            
            
       	 //+JMCE Llamar variable parametrica 
        	int TRXMP_MAX_POND_DETAIL =0;
        	String tipoDoc=pedido.getTipo_doc().toUpperCase();
           	logger.info("OP con tipo de Documento = "+tipoDoc);
        	if ("F".equals(tipoDoc)){
                try {
                    ParametrosService ps = new ParametrosService();
                    ParametroDTO par = ps.getParametroByName("TRXMP_MAX_POND_DETAIL");
                     TRXMP_MAX_POND_DETAIL =  Integer.parseInt(par.getValor());
                } catch (Exception se) {
                    logger.info("No se pudo leer el valor del servicio TRXMP_MAX_POND_DETAIL");
                }
			} else{
                try {
                    ParametrosService ps = new ParametrosService();
                    ParametroDTO par = ps.getParametroByName("TRXMP_MAX_POND_DETAIL_OTROS");
                     TRXMP_MAX_POND_DETAIL =  Integer.parseInt(par.getValor());
                } catch (Exception se) {
                    logger.info("No se pudo leer el valor del servicio TRXMP_MAX_POND_DETAIL_OTROS");
                }
			}
             
          //-JMCE Llamar variable parametrica   
            
            Iterator it = detalles_pedido.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next().toString();
                DetallePickingEntity prod = (DetallePickingEntity) detalles_pedido.get(key);
                if (prod.getPrecio().doubleValue() == 0.0) {
                    logger.debug("el registro " + key + " tiene datos inválidos en su id_produto / cantidad");
                    logger.debug("reg num    :" + key);
                    logger.debug("id_detalle :" + prod.getId_detalle());
                    logger.debug("id bp      :" + prod.getId_bp());
                    logger.debug("id dpicking:" + prod.getId_dpicking());
                    logger.debug("cant pick  :" + prod.getCant_pick());
                    logger.debug("precio     :" + prod.getPrecio());
                    logger.debug("descripcion:" + prod.getDescripcion());
                    logger.debug("sustituto  :" + prod.getSustituto());
                    logger.debug("cod_bin    :" + prod.getCod_bin());
                    trx1.rollback();
                    throw new PedidosException(Constantes._EX_TRX_PRODS_INVALIDOS);
                }
            }

            //recorrer en el detalle pedido x cada producto :
            suma_ponderada = 0;
            num_encabezados = 1;
            monto_detalles = 0.0;

            int i = 0;
            Iterator it2 = detalles_pedido.keySet().iterator();
            while (it2.hasNext()) {
                String key = it2.next().toString();
                i++;
                /*for (int i = 0; i < detalles_pedido.size(); i++) {
                    logger.debug("--- ciclo " + i + " num_encabezados " + num_encabezados + " num_detalles:" + (i + 1)
                            + " ---");*/

                DetallePickingEntity prodped = (DetallePickingEntity) detalles_pedido.get(key);
                //DetallePickingEntity prodped = (DetallePickingEntity) detalles_pedido.get(i);

                //genera nuevo entity detalles
                TrxMpDetalleEntity trxmpdet = new TrxMpDetalleEntity();
                trxmpdet.setId_detalle(prodped.getId_detalle());
                trxmpdet.setId_producto(prodped.getId_producto());
                trxmpdet.setId_pedido(id_pedido);
                trxmpdet.setCantidad(prodped.getCant_pick().doubleValue());
                trxmpdet.setDescripcion(prodped.getDescripcion());
                trxmpdet.setPrecio((int)prodped.getPrecio().doubleValue());
                trxmpdet.setCod_barra(prodped.getCBarra());
                logger.debug(i + ".- detalle_trx recibidos");

                //revisa el peso ponderado del producto - con el producto
                // detalle entity
                if (prodped.getId_producto() == 0) {
                    peso_prod = 2;
                } else {
                    ProductoSapEntity sap = (ProductoSapEntity) dao.getProductSapById(trxmpdet.getId_producto());
                    if ((sap.getUni_med().equals(Constantes.UMEDSAP_ST)) && (trxmpdet.getCantidad() == 1))
                        peso_prod = 1;
                    else
                        peso_prod = 2;
                }
                 
                num_productos++;
                suma_ponderada = suma_ponderada + peso_prod;
                monto_detalles = monto_detalles + Formatos.redondear((trxmpdet.getPrecio() * prodped.getCant_pick().doubleValue()));
                logger.debug(i + ".- precio producto :" + trxmpdet.getPrecio());
                
                //+JMCE Llamar variable parametrica 
                logger.debug(i + ".- MAX PONDERACION :" +TRXMP_MAX_POND_DETAIL);
                //-JMCE Llamar variable parametrica 
                
                logger.debug(i + ".- suma_ponderada:" + suma_ponderada);
                logger.debug(i + ".- monto_detalles:" + monto_detalles);

                detalle_trx.add(trxmpdet);
               
    //+JMCE Llamar variable parametrica 
                //if (suma_ponderada >= Constantes.TRXMP_MAX_POND_DETALLE) {
                if (suma_ponderada >= TRXMP_MAX_POND_DETAIL) {
   //-JMCE Llamar variable parametrica  	
                	
                   logger.debug(i + ".- ENCABEZADO :" + num_encabezados);
                    //genera clave : idpedido+00 <--serial num trxmp
                    if (num_encabezados > 9)
                        clave = id_pedido + "" + num_encabezados;
                    else
                        clave = id_pedido + "0" + num_encabezados;
                    trxmpenc.setId_trxmp(Long.parseLong(clave));
                    logger.debug(i + ".- encabezado clave:" + clave);

                    //agrega costo de despacho como producto
                    if (num_encabezados > 1) {
                        trxmpenc.setCosto_despacho(0L);
                        logger.debug(i + ".- encabezado costo_despacho:0");
                    } else {
                        TrxMpDetalleEntity costo_despacho = new TrxMpDetalleEntity();

                        costo_despacho.setId_pedido(id_pedido);
                        costo_despacho.setId_trxmp(Long.parseLong(clave));
                        //obtiene producto codigo_despacho
                        costo_despacho.setId_producto(Constantes.DESPACHO_ID_PRODUCTO_SAP);
                        costo_despacho.setCod_barra(Constantes.DESPACHO_COD_BARRA);
                        costo_despacho.setPrecio(pedido.getCosto_despacho());
                        costo_despacho.setCantidad(1);
                        costo_despacho.setDescripcion(Constantes.DESPACHO_GLOSA_DESCR);
                        monto_detalles = monto_detalles + pedido.getCosto_despacho();
                        detalle_trx.add(costo_despacho);
                        num_productos++;
                        logger.debug(i + ".- encabezado costo_despacho:" + costo_despacho.getPrecio());
                        logger.debug(i + ".- PRIMER encabezado genera producto despacho");
                        logger.debug("cba: " + costo_despacho.getCod_barra());
                    }

                    trxmpenc.setMonto_trxmp(monto_detalles);
                    trxmpenc.setCant_productos(num_productos);
                    logger.debug(i + ".- encabezado monto_detalles=" + monto_detalles);
                    logger.debug(i + ".- encabezado cantidad_prods=" + num_productos);

                    logger.debug(i + ".- encabezado antes setCreaTrxMpEnc");

                    if (!daoT.setCreaTrxMpEnc(trxmpenc)) {
                        trx1.rollback();
                        throw new TrxMedioPagoDAOException(Constantes._EX_TRX_NO_INSERTA_ENC);
                    }
                    logger.debug(i + ".- encabezado despues nro detalles:" + detalle_trx.size());

                    //inserta detalles
                    for (int j = 0; j < detalle_trx.size(); j++) {
                        logger.debug(i + ".- encabezado detalle " + j);
                        TrxMpDetalleEntity prod_traspaso = (TrxMpDetalleEntity) detalle_trx.get(j);
                        prod_traspaso.setId_trxmp(Long.parseLong(clave));

                        if (!daoT.setCreaTrxMpDet(prod_traspaso)) {
                            trx1.rollback();
                            throw new TrxMedioPagoDAOException(Constantes._EX_TRX_NO_INSERTA_ENC);
                        }
                        logger.debug(i + ".- encabezado detalle " + j + " insertado id_trxmp:" + clave);
                    }
                    //vacia detalles
                    detalle_trx.clear();
                    suma_ponderada = 0;
                    num_productos = 0;
                    monto_detalles = 0.0;
                    logger.debug(i + ".- detalles_trx vaciados, fin inserts");
                    num_encabezados++;
                }//if
            }//While

            logger.debug("Termina el ciclo de productos restantes:" + detalle_trx.size());
            logger.debug("Suma Ponderada =" + suma_ponderada);
            
       //+JMCE Llamar variable parametrica 
            logger.debug("Max Ponderado  =" + TRXMP_MAX_POND_DETAIL);
       //-JMCE Llamar variable parametrica 
            //detalles restantes
            if (detalle_trx.size() > 0) {
                logger.debug("Genera ultimo encabezado por que existen detalles restantes");
                logger.debug("F.- ENCABEZADO :" + num_encabezados);
                //genera clave : idpedido+00 <--serial num trxmp
                if (num_encabezados > 9)
                    clave = id_pedido + "" + num_encabezados;
                else
                    clave = id_pedido + "0" + num_encabezados;
                trxmpenc.setId_trxmp(Long.parseLong(clave));
                logger.debug("F.- encabezado clave:" + clave);

                //agrega costo de despacho como producto
                if (num_encabezados > 1) {
                    trxmpenc.setCosto_despacho(0L);
                    logger.debug("F.- encabezado costo_despacho:0");
                } else {
                    TrxMpDetalleEntity costo_despacho = new TrxMpDetalleEntity();

                    costo_despacho.setId_pedido(id_pedido);
                    costo_despacho.setId_trxmp(Long.parseLong(clave));
                    //obtiene producto codigo_despacho
                    costo_despacho.setId_producto(Constantes.DESPACHO_ID_PRODUCTO_SAP);
                    costo_despacho.setCod_barra(Constantes.DESPACHO_COD_BARRA);
                    costo_despacho.setPrecio(pedido.getCosto_despacho());
                    costo_despacho.setCantidad(1);
                    costo_despacho.setDescripcion(Constantes.DESPACHO_GLOSA_DESCR);
                    detalle_trx.add(costo_despacho);
                    monto_detalles = monto_detalles + pedido.getCosto_despacho();
                    num_productos++;
                    logger.debug("F.- encabezado costo_despacho:" + costo_despacho.getPrecio());
                    logger.debug("F.- PRIMER encabezado genera producto despacho");
                    logger.debug("cba2:" + costo_despacho.getCod_barra());
                }

                trxmpenc.setMonto_trxmp(monto_detalles);
                trxmpenc.setCant_productos(num_productos);
                logger.debug("F.- encabezado monto_detalles=" + monto_detalles);
                logger.debug("F.- encabezado cantidad_prods=" + num_productos);

                logger.debug("F.- encabezado antes setCreaTrxMpEnc");

                if (!daoT.setCreaTrxMpEnc(trxmpenc)) {
                    trx1.rollback();
                    throw new TrxMedioPagoDAOException(Constantes._EX_TRX_NO_INSERTA_ENC);
                }
                logger.debug("F.- encabezado despues nro detalles:" + detalle_trx.size());

                //inserta detalles
                for (int j = 0; j < detalle_trx.size(); j++) {
                    logger.debug("F.- encabezado detalle " + j);
                    TrxMpDetalleEntity prod_traspaso = (TrxMpDetalleEntity) detalle_trx.get(j);
                    prod_traspaso.setId_trxmp(Long.parseLong(clave));

                    if (!daoT.setCreaTrxMpDet(prod_traspaso)) {
                        trx1.rollback();
                        throw new TrxMedioPagoDAOException(Constantes._EX_TRX_NO_INSERTA_ENC);
                    }
                    logger.debug("F.- encabezado detalle " + j + " insertado id_trxmp:" + clave);
                }
                //vacia detalles
                detalle_trx.clear();
                suma_ponderada = 0;
                num_productos = 0;
                monto_detalles = 0.0;
                logger.debug("F.- detalles_trx vaciados, fin inserts");
                num_encabezados++;
            }//if detalles restantes
            else {
                logger.debug("F.- No existen detalles restantes");
            }

            //cambia estado al pedido
            if (!dao.setModEstadoPedido(id_pedido, Constantes.ID_ESTAD_PEDIDO_EN_PAGO)) {
                logger.debug("Error al intentar modificar el pedido al estado EN PAGO");
            }

        } catch (TrxMedioPagoDAOException ex) {
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }

            if (ex.getMessage().equals(DbSQLCode.SQL_DUP_KEY_CODE)) {
                logger.debug("clave duplicada");
                throw new PedidosException(Constantes._EX_TRX_DUPLICADA, ex);
            } else if (ex.getMessage().equals(DbSQLCode.SQL_EXIST_DEP_TBLS)) {
                logger.debug("Problemas de claves foraneas");
                throw new PedidosException(Constantes._EX_TRX_NO_INSERTA_ENC, ex);
            } else if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("Clave no existe");
                throw new PedidosException(Constantes._EX_TRX_NO_INSERTA_ENC, ex);
            } else if (ex.getMessage().equals(Constantes._EX_TRX_NO_INSERTA_ENC)) {
                logger.debug("no puede insertar encabezado de trx. de pago");
                throw new PedidosException(Constantes._EX_TRX_NO_INSERTA_ENC, ex);
            } else if (ex.getMessage().equals(Constantes._EX_TRX_NO_INSERTA_DET)) {
                logger.debug("no puede insertar detalle de trx. de pago");
                throw new PedidosException(Constantes._EX_TRX_NO_INSERTA_ENC, ex);
            } else
                throw new SystemException("Error no controlado", ex);

        } catch (PedidosDAOException ex) {
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }

            logger.debug("Problema :" + ex.getMessage());
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no encuentra el pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado al insertar TRX de PAGO", ex);
        } catch (ProductosSapDAOException ex) {
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }

            logger.debug("No encuentra la unidad de medida sap al producto " + ex.getMessage());
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_TRX_PROD_SIN_UM, ex);
            }
            throw new SystemException("Error no controlado", ex);
        } catch (DAOException e) {
            logger.error("Error al hacer rollback");
            throw new SystemException("Error al hacer rollback");
        }

        // cerramos trx
        try {
            trx1.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }
    }

    /**
     * Obtiene las transacciones de un pedido, segun el <b>id </b> del pedido.
     * <br>
     * 
     * @param id_pedido
     *            long
     * @return List MonitorTrxMpDTO
     * @throws PedidosException,
     *             en el caso que no existe el pedido. <br>
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public List getTrxMpByIdPedido(long id_pedido) throws PedidosException {

        List result = new ArrayList();
        JdbcTrxMedioPagoDAO dao = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        try {
            result = dao.getTrxMpByIdPedido(id_pedido);
        } catch (TrxMedioPagoDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Obtiene los datos del cupon de pago, a imprimir, segun el <b>id </b> del
     * pedido y el <b>id </b> de la transacción. <br>
     * 
     * @param id_pedido
     *            long
     * @param id_trxmp
     *            long
     * @return CuponPagoDTO
     * @throws PedidosException,
     *             en uno de los siguientes casos: <br>- No existe el pedido.
     *             <br>- No existe la transacción. <br>
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public CuponPagoDTO getCuponPago(long id_pedido, long id_trxmp) throws PedidosException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        JdbcTrxMedioPagoDAO daoT = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        JdbcJornadasDAO daoJ = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
        JdbcCalendarioDAO daoC = (JdbcCalendarioDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getCalendarioDAO();

        CuponPagoDTO cupon = new CuponPagoDTO();
        try {
            PedidoDTO pedido = dao.getPedidoById(id_pedido);
            TrxMpEncabezadoEntity enc = daoT.getTrxMpEnc(id_trxmp);
            logger.debug(" id jornada despacho:" + pedido.getId_jdespacho());
            JornadaDespachoEntity jornada = daoJ.getJornadaDespachoById(pedido.getId_jdespacho());
            logger.debug(" Id_hor_desp" + jornada.getId_hor_desp());
            HorarioDespachoEntity hor = daoC.getHorarioDespacho(jornada.getId_hor_desp());
            logger.debug("fin daos");
            //Genera el cuponDTO
            cupon.setId_pedido(id_pedido);
            cupon.setId_trxmp(id_trxmp);
            cupon.setCant_prods(enc.getCant_productos());
            cupon.setMonto_trxmp(enc.getMonto_trxmp());
            cupon.setFdespacho(pedido.getFdespacho());
            cupon.setTipoDocPago(pedido.getTipo_doc());
            cupon.setNom_cliente(pedido.getNom_cliente());
            cupon.setTipo_despacho(pedido.getTipo_despacho());
            logger.debug(" setVentanaDespacho antes");
            cupon.setVentanaDespacho(hor.getH_ini().toString() + " - " + hor.getH_fin().toString());
            logger.debug(" setVentanaDespacho despues");

        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        } catch (TrxMedioPagoDAOException e) {
            logger.debug("Problema TrxMedioPago:" + e.getMessage());
            throw new PedidosException(e);
        } catch (JornadasDAOException e) {
            logger.debug("Problema Jornadas:" + e.getMessage());
            throw new PedidosException(e);
        } catch (CalendarioDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }

        return cupon;
    }

    public HashMap getFacturasIngresadas(long id_pedido) throws PedidosException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getFacturasIngresadas(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    public int getCantFacturasIngresadas(long id_pedido) throws PedidosException {

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getCantFacturasIngresadas(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    public boolean ActListFacturas(FacturasDTO fact) throws PedidosException {
        boolean retorno = false;
        String mensage = "";
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            if (fact.getAccion().equalsIgnoreCase("eliminar")) {
                retorno = dao.elimFactura(fact);
                if (retorno) {
                    mensage = "Se Elimino la Factura Nº " + fact.getNum_doc() + " del Pedido Nº " + fact.getId_pedido();
                }
            } else if (fact.getAccion().equalsIgnoreCase("agregar")) {
                retorno = dao.ingFactura(fact);
                if (retorno) {
                    mensage = "Se Agrego la Factura Nº " + fact.getNum_doc() + " al Pedido Nº " + fact.getId_pedido();
                }
            }
            if (retorno) {
                LogPedidoDTO log = new LogPedidoDTO();
                log.setId_pedido(fact.getId_pedido());
                log.setUsuario(fact.getLogin());
                log.setLog(mensage);
                dao.addLogPedido(log);
            }
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            if (e.getMessage().equals(DbSQLCode.SQL_DUP_KEY_CODE)) {
                //logger.info("La Factura Nº " + fact.getNum_doc() + " ya se encuentra asociada al Pedido Nº " + fact.getId_pedido());
                throw new PedidosException(e.getMessage(), e);
            }
            throw new PedidosException(e);
        }
        return retorno;
    }

    /**
     * Obtiene transacción de medio de pago, segun el <b>id </b> de la
     * transacción. <br>
     * 
     * @param id_trxmp
     *            long
     * @return TrxMpDTO
     * @throws PedidosException,
     *             en caso que no existe la transacción. <br>
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public TrxMpDTO getTrxPagoById(long id_trxmp) throws PedidosException, SystemException {

        JdbcTrxMedioPagoDAO daoT = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        TrxMpDTO trx = new TrxMpDTO();
        TrxMpEncabezadoEntity ent = null;

        try {
            ent = daoT.getTrxMpEnc(id_trxmp);
        } catch (TrxMedioPagoDAOException e) {
            logger.debug("Problema getTrxPagoById:" + e.getMessage());
            throw new SystemException(e);
        }

        if (ent == null) {
            logger.info("Transacción " + id_trxmp + " no existe");
            throw new PedidosException(Constantes._EX_TRX_NO_EXISTE);
        }

        trx.setId_trxmp(ent.getId_trxmp());
        trx.setId_pedido(ent.getId_pedido());
        trx.setId_estado(ent.getId_estado());
        trx.setId_local(ent.getId_local());
        trx.setId_cliente(ent.getId_cliente());
        trx.setCosto_despacho(ent.getCosto_despacho());
        trx.setFcreacion(ent.getFcreacion());
        trx.setMonto_trxmp(ent.getMonto_trxmp());
        trx.setPos_monto_fp(ent.getPos_monto_fp());
        trx.setPos_num_caja(ent.getPos_num_caja());
        trx.setPos_boleta(ent.getPos_boleta());
        trx.setPos_trx_sma(ent.getPos_trx_sma());
        trx.setPos_fecha(ent.getPos_fecha());
        trx.setPos_hora(ent.getPos_hora());
        trx.setPos_fp(ent.getPos_fp());
        trx.setCant_productos(ent.getCant_productos());

        return trx;
    }

    /**
     * Obtiene el listado de detalles de trx de medio de pago, segun el <b>id
     * </b> de la transacción. <br>
     * 
     * @param id_trxmp
     *            long
     * @return List TrxMpDetalleDTO
     * @throws PedidosException,
     *             en caso que no existe la transacción. <br>
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public List getTrxPagoDetalleByIdTrxMp(long id_trxmp) throws PedidosException, SystemException {

        JdbcTrxMedioPagoDAO daoT = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();

        List dets = new ArrayList();
        List res = new ArrayList();
        try {

            dets = daoT.getTrxMpDetalles(id_trxmp);
            if (dets.size() == 0) {
                logger.debug("No existen detalles para la transaccion de pago");
                return null;
            }

            for (int i = 0; i < dets.size(); i++) {
                TrxMpDetalleEntity d = (TrxMpDetalleEntity) dets.get(i);
                TrxMpDetalleDTO trx = new TrxMpDetalleDTO();
                trx.setId_trxdet(d.getId_trxdet());
                trx.setId_trxmp(d.getId_trxmp());
                trx.setId_pedido(d.getId_pedido());
                trx.setId_producto(d.getId_producto());
                trx.setCod_barra(d.getCod_barra());
                trx.setPrecio(d.getPrecio());
                trx.setDescripcion(d.getDescripcion());
                trx.setCantidad(d.getCantidad());
                res.add(trx);
            }
        } catch (TrxMedioPagoDAOException e) {
            logger.debug("Problema getTrxPagoById:" + e.getMessage());
            throw new PedidosException(e);
        }
        return res;

    }

    /**
     * Actualiza Detalle de transaccion de medio de pagos
     * 
     * @param trx
     *            ProcModTrxMPDetalleDTO
     * @return boolean, devuelve <i>true </i> si la actualización fue exitosa,
     *         caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en caso que no existe la transacción detalle. <br>
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public boolean setModTrxPagoDet(ProcModTrxMPDetalleDTO trx) throws PedidosException, SystemException {
        JdbcTrxMedioPagoDAO daoT = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        try {

            if (trx.getCod_barra() == null) {
                logger.debug("El código de barra viene vacio.");
                return false;
            }

            boolean res = false;
            for (int i = 0; i < trx.getId_trx_mp_det().length; i++) {
                TrxMpDetalleEntity trxent = new TrxMpDetalleEntity();
                trxent.setCod_barra(trx.getCod_barra()[i]);
                trxent.setId_trxdet(trx.getId_trx_mp_det()[i]);
                res = daoT.updTrxMpDet(trxent);
                if (!res) {
                    break;
                }
            }
            return res;

        } catch (TrxMedioPagoDAOException e) {
            logger.debug("Problema setModTrxPagoDet:" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * Retorna el último pedido cursado por el cliente, en cualquier estado.
     * 
     * @param id_cliente
     *            long
     * @return PedidoDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public PedidoDTO getUltimoIdPedidoCliente(long id_cliente) throws PedidosException, SystemException {

        PedidoDTO pedido = null;
        long id_pedido = 0;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            id_pedido = dao.getUltimoIdPedidoCliente(id_cliente);
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new SystemException(e);
        }

        try {
            pedido = this.getPedidoById(id_pedido);
        } catch (PedidosException e) {
            if (e.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE)) {
                logger.info("El cliente no tiene pedidos anteriores");
                pedido = null;
            }
        }
        return pedido;
    }

    /**
     * Retorna los productos de un pedido, segun id_pedido y id_sector relacionado
     * 
     * @param id_pedido  long
     * @param id_sector  long
     * @param id_local   long
     * @return List ProductosPedidoDTO
     * @throws PedidosException, en el caso que no exista el pedido.
     * @throws SystemException, en el caso que exista error de sistema.
     */
    public List getProdPedidoXSector(long id_pedido, long id_sector, long id_local) throws PedidosException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            result = dao.getProdPedidoXSector(id_pedido, id_sector, id_local);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Retorna los productos de un pedido, segun id_pedido y id_sector relacionado
     * 
     * @param id_pedido  long
     * @param id_sector  long
     * @param id_local   long
     * @return List ProductosPedidoDTO
     * @throws PedidosException, en el caso que no exista el pedido.
     * @throws SystemException, en el caso que exista error de sistema.
     */
    public List getProdSinPickearXPedidoXSector(long id_pedido, long id_sector,long id_local) throws PedidosException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            result = dao.getProdSinPickearXPedidoXSector(id_pedido, id_sector,id_local);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Retorna los productos de un pedido, segun id_pedido y id_sector
     * relacionado
     * 
     * @param id_pedido
     *            long
     * @param id_sector
     *            long
     * @param id_local
     *            long
     * @return List ProductosPedidoDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public AvanceDTO getAvanceJornada(long id_jornada_picking) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getAvanceJornada(id_jornada_picking);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    //Maxbell Arreglo homologacion
    public int getCantidadOpPasadosPorBodega(long id_jornada) {
    	JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getCantidadOpPasadosPorBodega(id_jornada);
        } catch (Exception e) {
            logger.debug("Problema :" + e.getMessage());
        }
        return 0;
    }

    /**
     * Retorna los productos de un pedido, segun id_pedido y id_sector
     * relacionado
     * 
     * @param id_pedido
     *            long
     * @param id_sector
     *            long
     * @param id_local
     *            long
     * @return List ProductosPedidoDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public AvanceDTO getAvancePedido(long id_pedido) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getAvancePedido(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**  /**(+) INDRA 2012-12-12 (+)
     * Retorna los productos de un pedido, segun id_pedido y id_sector
     * relacionado
     * 
     * @param id_pedido
     *            long
     * @param id_sector
     *            long
     * @param id_local
     *            long
     * @return List ProductosPedidoDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public AvanceDTO getAvanceUmbralPedido(long id_pedido) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getAvanceUmbralPedido(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }
    /**
     * Retorna los productos de un pedido, segun id_pedido y id_sector
     * relacionado
     * 
     * @param id_pedido
     *            long
     * @param id_sector
     *            long
     * @param id_local
     *            long
     * @return List ProductosPedidoDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public AvanceDTO getAvanceUmbralMonto(long id_pedido) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getAvanceUmbralMonto(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }
    /**
     * Retorna los umbrales de un local
     * relacionado
     * 
     * @param id_pedido
     *            long
     * @param id_sector
     *            long
     * @param id_local
     *            long
     * @return List ProductosPedidoDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public AvanceDTO getAvanceUmbralParametros(long id_pedido) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getAvanceUmbralParametros(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**(-) INDRA 2012-12-12 (-)
     * Obtiene la lista de productos sueltos de un pedido, segun el <i>id </i>
     * del pedido.
     * 
     * @param id_pedido
     *            long
     * @return List ProductosPedidoDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public List getProductosSueltosByPedidoId(long id_pedido) throws PedidosException {
        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            result = dao.getProductosSueltosByPedidoId(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Se obtiene la lista de todas las secciones sap
     * 
     * @return List CategoriaSapDTO
     * @throws PedidosException,
     *             en el caso que no exista el pedido.
     * @throws SystemException,
     *             en el caso que exista error de sistema.
     *  
     */
    public List getSeccionesSap() throws PedidosException, SystemException {

        List result = new ArrayList();
        List lista_cat = null;

        logger.debug("en getCategoriasSapById");

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {

            lista_cat = dao.getSeccionesSap();

            CategoriaSapEntity cat = null;
            for (int i = 0; i < lista_cat.size(); i++) {
                cat = (CategoriaSapEntity) lista_cat.get(i);
                CategoriaSapDTO catdto = new CategoriaSapDTO();
                catdto.setId_cat(cat.getId_cat());
                catdto.setDescrip(cat.getDescrip());
                result.add(catdto);
            }

        } catch (PedidosDAOException ex) {
            logger.debug("Problema getSeccionesSap:" + ex);
            throw new SystemException(ex);
        }
        return result;
    }

    /**
     * Actualizar detalle del picking, en caso que el detalle tiene sustituto y
     * no tiene informacion completa.
     * 
     * @param prod
     *            DetallePickingDTO
     * @return boolean, devuelve <i>true </i> si la actualización fue exitosa,
     *         caso contrario devuelve <i>false </i>.
     * @throws PedidosException,
     *             en caso que no existe el id del detalle de picking. <br>
     * @throws SystemException,
     *             en caso que exista error de sistema.
     *  
     */
    public boolean setDetallePickingSustituto(DetallePickingDTO prod) throws PedidosException, SystemException {
        boolean result = false;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        logger.debug("en setDetallePickingSustituto");

        try {
            //verificar si existe el detalle del pedido
            if (dao.getDetPickingById(prod.getId_dpicking()) == null) {
                throw new PedidosException(Constantes._EX_OPE_ID_DETPCK_NO_EXISTE);
            }

            result = dao.setDetallePickingSustituto(prod);
        } catch (PedidosDAOException ex) {
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                throw new PedidosException(Constantes._EX_OPE_ID_DETPCK_NO_EXISTE);
            }
            throw new SystemException("Error no controlada", ex);
        }
        return result;
    }

    /**
     * Obtener detalle picking del pedido, segun el <b>id </b> del detalle de
     * picking.
     * 
     * @param id_dpicking
     *            long
     * @return DetallePickingDTO
     * @throws PedidosException,
     *             en caso que no existe el id del detalle de picking. <br>
     * @throws SystemException,
     *             en caso que exista error de sistema.
     *  
     */
    public DetallePickingDTO getDetallePickingById(long id_dpicking) throws PedidosException, SystemException {
        DetallePickingDTO result = null;
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        logger.debug("en getDetallePickingById");

        try {
            //verificar si existe el detalle del pedido
            DetallePickingEntity det = dao.getDetPickingById(id_dpicking);
            if (det == null) {
                throw new PedidosException(Constantes._EX_OPE_ID_DETPCK_NO_EXISTE);
            }
            result = new DetallePickingDTO();
            result.setId_dpicking(det.getId_dpicking());
            result.setId_detalle(det.getId_detalle());
            result.setId_bp(det.getId_bp());
            result.setId_producto(det.getId_producto());
            result.setId_pedido(det.getId_pedido());
            result.setCBarra(det.getCBarra());
            result.setDescripcion(det.getDescripcion());
            result.setPrecio(det.getPrecio().doubleValue());
            result.setCant_pick(det.getCant_pick().doubleValue());
            result.setSustituto(det.getSustituto());

        } catch (PedidosDAOException ex) {
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                throw new PedidosException(Constantes._EX_OPE_ID_DETPCK_NO_EXISTE);
            }
            throw new SystemException("Error no controlada", ex);
        }
        return result;
    }

    /**
     * Obtiene listado de los locales
     * 
     * @param cod_prod
     *            long
     * @return List LocalDTO, retorna la lista de locales
     * @throws PedidosException
     *  
     */
    public List getLocalesByProducto(long cod_prod) throws PedidosException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            result = dao.getLocalesByProducto(cod_prod);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Elimina la relación entre productos y sector, segun producto y local
     * Nota: Este método tiene transaccionalidad.
     * 
     * @param id_producto
     * @return boolean
     * @throws PedidosException
     * @throws SystemException
     */
    public boolean setDelProductoXSector(long id_producto) throws PedidosException, SystemException {
        boolean result = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        //Creamos la transacción
        JdbcTransaccion trx = new JdbcTransaccion();

        try {

            // Iniciamos la transacción
            trx.begin();

            // Asignamos la transacción a los dao's
            dao.setTrx(trx);

        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new SystemException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(e);
        }

        try {
            //verifica si existe id_producto sap
            if (dao.getProductSapById(id_producto) == null) {
                throw new PedidosException(Constantes._EX_PSAP_ID_NO_EXISTE);
            }

            //obtiene los sectores , segun producto y local
            //long id_sector = dao.getProdXSector(id_producto);
/*            if (id_sector == 0) {
                logger.debug("No hay sectores");
                result = true;
            } else {*/
            result = dao.setDelProdXSector(id_producto);
            //}

            //Finaliza la transacción
            trx.end();

        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        } catch (ProductosSapDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(Constantes._EX_PSAP_ID_NO_EXISTE);
        } catch (DAOException e) {
            try {
                trx.rollback();
            } catch (DAOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new SystemException(e);
        }
        return result;
    }

    /**
     * Agrega relación entre producto y sector
     * 
     * @param id_producto
     * @param id_sector
     * @return boolean
     * @throws PedidosException
     */
    public boolean setAddProductoXSector(long id_producto, long id_sector) throws PedidosException {
        boolean result = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            //verifica si id_producto sap existe
            if (dao.getProductSapById(id_producto) == null) {
                throw new PedidosException(Constantes._EX_PSAP_ID_NO_EXISTE);
            }
            //verifica si is_sector existe
            if (!dao.isSectorById(id_sector)) {
                throw new PedidosException(Constantes._EX_ID_SECTOR_INVALIDO);
            }

            result = dao.setAddProductoXSector(id_producto, id_sector);

        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        } catch (ProductosSapDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(Constantes._EX_PSAP_ID_NO_EXISTE);
        }
        return result;
    }

    /**
     * Obtiene el id del sector , segun producto y local
     * 
     * @param id_producto
     * @return boolean
     * @throws PedidosException
     */
    public long getSectorByProd(long id_producto) throws PedidosException {
        long result = -1;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            //verifica si id_producto sap existe
            if (dao.getProductSapById(id_producto) == null) {
                throw new PedidosException(Constantes._EX_PSAP_ID_NO_EXISTE);
            }
        } catch (ProductosSapDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(Constantes._EX_PSAP_ID_NO_EXISTE);
        }
        return result;
    }

    /**
     * Obtiene el monto total de la hoja de despacho, segun el id_pedido
     * 
     * @param id_pedido
     * @return monto total
     * @throws PedidosException
     * @throws SystemException
     */
    public double getMontoTotalHojaDespachoByIdPedido(long id_pedido) throws PedidosException, SystemException {
        double result = 0;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        //Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();

        // Iniciamos trx
        try {
            trx1.begin();
        } catch (Exception e1) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        }

        // Marcamos los dao's con la transacción
        try {
            dao.setTrx(trx1);
        } catch (PedidosDAOException e2) {
            logger.error("Error al asignar transacción al dao TrxMp");
            throw new SystemException("Error al asignar transacción al dao TrxMp");
        }

        double suma_ponderada = 0;
        double monto_detalles = 0.0;
        int peso_prod = 0;
        int num_productos = 0;
        int num_encabezados = 1;

        try {
            logger.debug("En getMontoTotalHojaDespachoByIdPedido.");
            //verifica si el pedido existe
            if (!dao.isPedidoById(id_pedido)) {
                trx1.rollback();
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
            }
            
            //recuperar detalles
            HashMap detalles_pedido = dao.getDetPickingByIdPedido(id_pedido);
            //List detalles_pedido = dao.getDetPickingByIdPedido(id_pedido);

            PedidoDTO pedido = new PedidoDTO();
            pedido = dao.getPedidoById(id_pedido);

            //costo de despacho
            //monto_detalles = pedido.getCosto_despacho();

            //lista de detalle, q permiten recorrer los detalles encontrados
            List detalle_trx = new ArrayList();
            
          	 //+JMCE Llamar variable parametrica 
           	int TRXMP_MAX_POND_DETAIL =0;
           	String tipoDoc=pedido.getTipo_doc().toUpperCase();
           	logger.info("OP con tipo de Documento = "+tipoDoc);
           	if ("F".equals(tipoDoc)){
                   try {
                       ParametrosService ps = new ParametrosService();
                       ParametroDTO par = ps.getParametroByName("TRXMP_MAX_POND_DETAIL");
                        TRXMP_MAX_POND_DETAIL =  Integer.parseInt(par.getValor());
                   } catch (Exception se) {
                       logger.info("No se pudo leer el valor del servicio TRXMP_MAX_POND_DETAIL");
                   }
   			} else{
                   try {
                       ParametrosService ps = new ParametrosService();
                       ParametroDTO par = ps.getParametroByName("TRXMP_MAX_POND_DETAIL_OTROS");
                        TRXMP_MAX_POND_DETAIL =  Integer.parseInt(par.getValor());
                   } catch (Exception se) {
                       logger.info("No se pudo leer el valor del servicio TRXMP_MAX_POND_DETAIL_OTROS");
                   }
   			}
                
             //-JMCE Llamar variable parametrica   
               
            int i = 0;
            Iterator it = detalles_pedido.keySet().iterator();
            while (it.hasNext()) {
                i++;
                String key = it.next().toString();
                DetallePickingEntity prodped = (DetallePickingEntity) detalles_pedido.get(key);

            /*for (int i = 0; i < detalles_pedido.size(); i++) {

                DetallePickingEntity prodped = (DetallePickingEntity) detalles_pedido.get(i);*/

                //revisa el peso ponderado del producto - con el producto
                // detalle entity
                if (prodped.getId_producto() == 0) {
                    peso_prod = 2;
                } else {
                    ProductoSapEntity sap = (ProductoSapEntity) dao.getProductSapById(prodped.getId_producto());
                    if ((sap.getUni_med().equals(Constantes.UMEDSAP_ST)) && (prodped.getCant_pick().doubleValue() == 1))
                        peso_prod = 1;
                    else
                        peso_prod = 2;
                }

                num_productos++;
                suma_ponderada = suma_ponderada + peso_prod;
                monto_detalles = monto_detalles
                        + Formatos
                                .redondear((prodped.getPrecio().doubleValue())* prodped.getCant_pick().doubleValue());
                logger.debug(i + ".- suma_ponderada:" + suma_ponderada);
                logger.debug(i + ".- monto_detalles:" + monto_detalles);

                detalle_trx.add("1");
      //+JMCE Llamar variable parametrica 
               // if (suma_ponderada >= Constantes.TRXMP_MAX_POND_DETALLE) {
                if (suma_ponderada >= TRXMP_MAX_POND_DETAIL) {
       //-JMCE Llamar variable parametrica 	
                	
                    logger.debug(i + ".- ENCABEZADO :" + num_encabezados);
                    //genera clave : idpedido+00 <--serial num trxmp

                    //agrega costo de despacho como producto
                    if (num_encabezados > 1) {
                        logger.debug(i + ".- encabezado costo_despacho:0");
                    } else {
                        monto_detalles = monto_detalles + pedido.getCosto_despacho();
                        detalle_trx.add("2");
                        num_productos++;
                    }

                    logger.debug(i + ".- encabezado despues nro detalles:" + detalle_trx.size());

                    logger.debug(i + ".- encabezado monto_detalles=" + monto_detalles);
                    logger.debug(i + ".- encabezado cantidad_prods=" + num_productos);

                    result += monto_detalles;
                    logger.debug(i + ".- encabezado result=" + result);

                    //vacia detalles
                    detalle_trx.clear();
                    suma_ponderada = 0;
                    num_productos = 0;
                    monto_detalles = 0.0;
                    logger.debug(i + ".- detalles_trx vaciados, fin inserts");
                    num_encabezados++;
                }//if

            }//while

            logger.debug("Termina el ciclo de productos restantes:" + detalle_trx.size());
            logger.debug("Suma Ponderada =" + suma_ponderada);

            //detalles restantes
            if (detalle_trx.size() > 0) {
                logger.debug("Genera ultimo encabezado por que existen detalles restantes");
                logger.debug("F.- ENCABEZADO :" + num_encabezados);

                //agrega costo de despacho como producto
                if (num_encabezados > 1) {
                    logger.debug("F.- encabezado costo_despacho:0");
                } else {
                    detalle_trx.add("3");
                    monto_detalles = monto_detalles + pedido.getCosto_despacho();
                    num_productos++;
                }

                logger.debug("F.- encabezado monto_detalles=" + monto_detalles);
                logger.debug("F.- encabezado cantidad_prods=" + num_productos);

                logger.debug("F.- encabezado despues nro detalles:" + detalle_trx.size());

                result += monto_detalles;
                logger.debug("F.- encabezado result=" + result);

                //vacia detalles
                detalle_trx.clear();
                suma_ponderada = 0;
                num_productos = 0;
                monto_detalles = 0.0;
                logger.debug("F.- detalles_trx vaciados, fin inserts");
                num_encabezados++;
            }//if detalles restantes
            else {
                logger.debug("F.- No existen detalles restantes");
            }

        } catch (PedidosDAOException ex) {
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }

            logger.debug("Problema :" + ex.getMessage());
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no encuentra el pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado al insertar TRX de PAGO", ex);
        } catch (ProductosSapDAOException ex) {
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }

            logger.debug("No encuentra la unidad de medida sap al producto " + ex.getMessage());
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_TRX_PROD_SIN_UM, ex);
            }
            throw new SystemException("Error no controlado", ex);
        } catch (DAOException e) {
            logger.error("Error al hacer rollback");
            throw new SystemException("Error al hacer rollback");
        }

        //cerramos trx
        try {
            trx1.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }

        return result;
    }

    /**
     * Rechaza el pago de una OP, coloca las observaciones en el log.
     * 
     * @param dto
     * @return boolean
     * @throws PedidosException
     * @throws SystemException
     */
    public boolean setRechazaPagoOP(RechPagoOPDTO dto) throws PedidosException, SystemException {

        boolean result = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {

            PedidoDTO pedido = dao.getPedidoById(dto.getId_pedido());
            if (pedido == null) {
                logger.debug("El pedido no existe");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
            }
            if (pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_EN_PAGO) {
                logger.debug("El pedido en estado inadecuado");
                throw new PedidosException(Constantes._EX_OPE_ESTADO_INADECUADO);
            }
            result = dao.setRechazaPagoOP(dto);

            //agregar la observacion al Log
            LogPedidoDTO log = new LogPedidoDTO();
            log.setId_pedido(dto.getId_pedido());
            log.setLog(Constantes.MNS_PAGO_RECHAZADO + " : " + dto.getObservacion());
            log.setUsuario(dto.getUsr_login());
            dao.addLogPedido(log);

        } catch (PedidosDAOException ex) {
            logger.debug(ex.getMessage());
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }

        return result;
    }

    /**
     * Obtiene el listado de las politicas de sustitución
     * 
     * @return List
     * @throws PedidosException
     * @throws SystemException
     */
    public List getPolitSustitucionAll() throws PedidosException, SystemException {
        List lst_pol_sust = new ArrayList();

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            lst_pol_sust = dao.getPolitSustitucionAll();
        } catch (PedidosDAOException ex) {
            logger.debug(ex.getMessage());
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return lst_pol_sust;
    }

    /**
     * modifica la politica de sustitucion de un pedido
     * 
     * @param dto
     *            ProcModPedidoPolSustDTO
     * @return boolean
     * @throws PedidosException
     * @throws SystemException
     */
    public boolean setModPedidoPolSust(ProcModPedidoPolSustDTO dto) throws PedidosException, SystemException {

        boolean result = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {

            PedidoDTO pedido = dao.getPedidoById(dto.getId_pedido());
            if (pedido == null) {
                logger.debug("El pedido no existe");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
            }
            logger.debug("estado:" + pedido.getId_estado());
            if ((pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_INGRESADO)
                    && (pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION)
                    && (pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_VALIDADO)) {
                logger.debug("El pedido en estado inadecuado");
                throw new PedidosException(Constantes._EX_OPE_ESTADO_INADECUADO);
            }

            PoliticaSustitucionDTO polSustDto = dao.getPolSustitById(dto.getId_pol_sust());
            dto.setDesc_pol_sust(polSustDto.getDescripcion());
            result = dao.setModPedidoPolSust(dto);

            //agregar la observacion al Log
            LogPedidoDTO log = new LogPedidoDTO();
            log.setId_pedido(dto.getId_pedido());
            log.setLog(Constantes.MNS_POL_SUSTITUCION + ": " + dto.getDesc_pol_sust());
            log.setUsuario(dto.getLogin());
            dao.addLogPedido(log);

        } catch (PedidosDAOException ex) {
            logger.debug(ex.getMessage());
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);

        }

        return result;
    }

    /**
     * Cambia el estado del pedido a 'En Pago'
     * 
     * @param dto
     * @return boolean
     * @throws PedidosException
     * @throws SystemException
     */
    public boolean setCambiarEnPagoOP(CambEnPagoOPDTO dto) throws PedidosException, SystemException {
        boolean result = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {

            PedidoDTO pedido = dao.getPedidoById(dto.getId_pedido());
            if (pedido == null) {
                logger.debug("El pedido no existe");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
            }
            logger.debug("estado:" + pedido.getId_estado());
            if (pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO) {
                logger.debug("El pedido tiene estado inadecuado");
                throw new PedidosException(Constantes._EX_OPE_ESTADO_INADECUADO);
            }

            result = dao.setCambiarEnPagoOP(dto);

            //agregar la observacion al Log
            LogPedidoDTO log = new LogPedidoDTO();
            log.setId_pedido(dto.getId_pedido());
            log.setLog(Constantes.MNS_CAMBIAR_EN_PAGO);
            log.setUsuario(dto.getUsr_login());
            dao.addLogPedido(log);

        } catch (PedidosDAOException ex) {
            logger.debug(ex.getMessage());
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
        return result;
    }

    /**
     * Envía email a cliente con detalle del pedido pickeado y pagado
     * 
     * @param idPedido
     *            long
     * @throws PedidosException,
     *             en el caso que hayan transacciones del pedido pendientes
     * @throws SystemException,
     *             en el caso que exista otro error no esperado.
     *  
     */
	public void sendEmailByPedido(long id_pedido) throws PedidosException, SystemException {
        JdbcEmailDAO emailDAO = (JdbcEmailDAO) cl.bbr.jumbocl.shared.emails.dao.DAOFactory.getDAOFactory(
        	DAOFactory.JDBC).getEmailDAO();

        JdbcPedidosDAO dao1 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        JdbcTrxMedioPagoDAO dao2 = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        JdbcDespachosDAO dao3 = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();

        String versionListener ="V.2014.09.1R1";
        try {

            // Verifica que todas las transacciones estén pagadas
        	if (dao2.isAllTrxMPbyPedidoAndState(id_pedido,Constantes.ID_ESTAD_TRXMP_PAGADA)) {
                // Obtiene template para Email, agrega datos y envía email
                ResourceBundle rb = ResourceBundle.getBundle("bo");
                //Se lee el archivo para el mail
                // Recupera pagina desde web.xml

                // Codigo se uso para probar local
                //String pag_form = rb.getString("conf.path.html") + rb.getString("mail.pedidos.pathTemplate.html");
                
                PedidoDTO ped = dao1.getPedidoById(id_pedido);
                String pag_form=rb.getString("mail.pedidos.pathTemplate.html");
                String pag_form_aux=rb.getString("mail.pedidos.pathTemplate.html");
                if (ped.getTipo_despacho().equalsIgnoreCase("R")){
                	try{
                		pag_form = rb.getString("mail.pedidos.pathTemplate.html.retiro");
                	}catch(MissingResourceException e){                	
                		//si no encuentra la ruta de la planilla en el template le agrega "_retiro" al nombre del archivo en base a la ruta del original               	
                		pag_form_aux=pag_form_aux.replaceAll(".html", "");
                		pag_form=pag_form_aux+"_retiro.html";
                	}

                }

                // Carga el template html
                TemplateLoader load = new TemplateLoader(pag_form);
                ITemplate tem = load.getTemplate();
                
                ClienteEntity cliente = dao1.getClienteById(ped.getId_cliente());

                // TObtener Clientes y datos del Cliente
                String emailAddress = cliente.getEmail();

                long montoTotal = 0;
                long montoFlujo = 0;
                String horaUltimaTrx = "";
                long cantidadProdEnv = 0;
                long cantidadProdSus = 0;
                long cantidadProdFal = 0;
                DespachoDTO despacho = dao3.getDespachoById(id_pedido);
                String fecha = despacho.getF_despacho();

                // Obtener Día y Jornada de Despacho
                String fecha_tramo = "";
                if (ped.getTipo_despacho().equalsIgnoreCase(Constantes.TIPO_DESPACHO_ECONOMICO_CTE)) {
                    fecha_tramo = fecha.substring(8, 10) + "/" + fecha.substring(5, 7) + "/" + fecha.substring(0, 4);
                } else {
                    fecha_tramo = fecha.substring(8, 10) + "/" + fecha.substring(5, 7) + "/" + fecha.substring(0, 4) + ", entre " + despacho.getH_ini() + " y " + despacho.getH_fin() + " hrs.";
                }
                // Obtener Número de Pedido, Monto Total, Productos Enviados
                List trxMP = dao2.getTrxMpByIdPedido(id_pedido);
                //ArrayList listaProductosEnv = new ArrayList();

                for (Iterator iter = trxMP.iterator(); iter.hasNext();) {
                    MonitorTrxMpDTO trxmps = (MonitorTrxMpDTO) iter.next();
                    //montoTotal += trxmps.getMonto_trxmp();
                    montoTotal += trxmps.getPos_monto_fp();
                    horaUltimaTrx = trxmps.getPos_hora();
                }

                List datos_cat = dao1.productosEnviadosPedidoForEmail(id_pedido);

                List fm_cate = new ArrayList();
                double precio_total = 0;
                int contador = 0;
                long totalizador = 0;
                IValueSet fila_lista_sel = null;
                List aux_blanco = null;
                long total_producto_pedido = 0;
//20121012VMatheu se elimino la logica para organizar los productos por categoria.
                //los productos no se organizan por categoria
                List fm_prod = new ArrayList();
                //Llenamos la tabla con los productos ordenados por categorias
                for (int i = 0; i < datos_cat.size(); i++) {

                    CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) datos_cat.get(i);
                    List prods = cat.getCarroCompraProductosDTO();
                    for (int j = 0; j < prods.size(); j++) {

                        CarroCompraProductosDTO producto = (CarroCompraProductosDTO) prods.get(j);
                        total_producto_pedido += Math.ceil(producto.getCantidad());
                        IValueSet fila_pro = new ValueSet();
                        fila_pro.setVariable("{descripcion}", producto.getNombre());
                        fila_pro.setVariable("{marca}", producto.getMarca());
                        fila_pro.setVariable("{cod_sap}", producto.getCodigo());
                        fila_pro.setVariable("{valor}", Formatos.formatoIntervalo(producto.getCantidad()) + "");

                        if (producto.getUnidadMedida().length() == 0) {
                            if (!NumericUtils.tieneDecimalesSignificativos(producto.getCantidad(), 3)) {
                                cantidadProdEnv += new Double(producto.getCantidad()).longValue();
                            } else {
                                cantidadProdEnv += 1;
                            }
                        } else if (producto.getUnidadMedida().matches("ST|BAG|PAK|CS")) {
                            cantidadProdEnv += new Double(producto.getCantidad()).longValue();
                        } else {
                            cantidadProdEnv += 1;
                        }

                        fila_pro.setVariable("{carr_id}", producto.getCar_id() + "");
                        fila_pro.setVariable("{contador}", contador + "");

                        precio_total = 0;

                        // Si el producto es con seleccion
                        if (producto.getUnidad_tipo().charAt(0) == 'S') {

                            fila_lista_sel = new ValueSet();

                            List aux_lista = new ArrayList();
                            for (double v = 0; v <= producto.getInter_maximo(); v += producto.getInter_valor()) {
                                IValueSet aux_fila = new ValueSet();
                                aux_fila.setVariable("{valor}", Formatos.formatoIntervalo(v)+ "");
                                aux_fila.setVariable("{opcion}", Formatos.formatoIntervalo(v)+ "");
                                if (Formatos.formatoIntervalo(v).compareTo(Formatos.formatoIntervalo(producto.getCantidad())) == 0)
                                    aux_fila.setVariable("{selected}","selected");
                                else
                                    aux_fila.setVariable("{selected}", "");
                                aux_lista.add(aux_fila);
                            }
                            fila_lista_sel.setVariable("{contador}", contador+ "");
                            fila_lista_sel.setDynamicValueSets("CANTIDADES",aux_lista);

                            aux_blanco = new ArrayList();
                            aux_blanco.add(fila_lista_sel);
                            fila_pro.setDynamicValueSets("LISTA_SEL",aux_blanco);

                        } else {
                            fila_lista_sel = new ValueSet();
                            fila_lista_sel.setVariable("{contador}", contador+ "");
                            fila_lista_sel.setVariable("{valor}", producto.getCantidad()+ "");
                            fila_lista_sel.setVariable("{maximo}", producto.getInter_maximo()+ "");
                            fila_lista_sel.setVariable("{intervalo}", producto.getInter_valor()+ "");
                            aux_blanco = new ArrayList();
                            aux_blanco.add(fila_lista_sel);
                            fila_pro.setDynamicValueSets("INPUT_SEL",aux_blanco);
                        }

                        if (producto.isCon_nota() == true) {
                            IValueSet set_nota = new ValueSet();
                            set_nota.setVariable("{nota}", producto.getNota()+ "");
                            set_nota.setVariable("{contador}", contador + "");
                            aux_blanco = new ArrayList();
                            aux_blanco.add(set_nota);
                            fila_pro.setDynamicValueSets("NOTA", aux_blanco);
                        }

                        precio_total = Utils.redondear(producto.getPrecio());
                        fila_pro.setVariable("{unidad}", producto.getTipre());
                        fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecio(producto.getPpum()));
                        fila_pro.setVariable("{precio_total}", Formatos.formatoPrecio(precio_total));
                        fila_pro.setVariable("{CLASE_TABLA}","TablaDiponiblePaso1");

                        if (contador % 2 != 0) {
                            fila_pro.setVariable("{CLASE_CELDA}","tdProductoPar");

                        } else {
                            fila_pro.setVariable("{CLASE_CELDA}", "celda1");

                        }

                        fila_pro.setVariable("{NO_DISPONIBLE}", "");
                        fila_pro.setVariable("{OPCION_COMPRA}", "1");
                        totalizador += precio_total;

                        contador++;
                        fm_prod.add(fila_pro);

                    }

                }

                IValueSet top = new ValueSet();
                top.setDynamicValueSets("PRODUCTOS", fm_prod);

                // Obtener Productos Sustituidos
                List productosSustituidos = dao1.productosSustitutosPedidoForEmail(id_pedido);
                ArrayList listaProductosSus = new ArrayList();
                List sustheads = new ArrayList();
                for (Iterator iterator = productosSustituidos.iterator(); iterator.hasNext();) {
                    SustitutoDTO prod = (SustitutoDTO) iterator.next();
                    IValueSet fila = new ValueSet();
                    if ((prod.getCod_prod1() != null) && (prod.getUni_med1() != null) 
                    		&& !sustheads.contains(prod.getCod_prod1() + prod.getUni_med1())) {
                        sustheads.add(prod.getCod_prod1() + prod.getUni_med1());
                        fila.setVariable("{can_pro_sus1}", prod.getCant1() + "");
                        fila.setVariable("{can_pro_sus2}", prod.getCant2() + "");
                        if (prod.getDescr1().length() > 48)
                            fila.setVariable("{descripcion_sus1}", prod.getDescr1().substring(0, 48)+ "");
                        else
                            fila.setVariable("{descripcion_sus1}", prod.getDescr1()+ "");
                        if (prod.getDescr2().length() > 48)
                            fila.setVariable("{descripcion_sus2}", prod.getDescr2().substring(0, 48)+ "");
                        else
                            fila.setVariable("{descripcion_sus2}", prod.getDescr2()+ "");

                        fila.setVariable("{unitario_sus1}", Formatos.formatoPrecio(prod.getPrecio1())+ "");
                        fila.setVariable("{unitario_sus1_tot}", Formatos.formatoPrecio(Utils.redondear(prod.getPrecio1() * prod.getCant1()))+ "");

                        fila.setVariable("{unitario_sus2}", Formatos.formatoPrecio(prod.getPrecio2()) + "");
                        fila.setVariable("{unitario_sus2_tot}", Formatos.formatoPrecio(Utils.redondear(prod.getPrecio2() * prod.getCant2())) + "");

                        if (prod.getUni_med1().length() == 0) {
                            double cantidad = prod.getCant1();
                            if (!NumericUtils.tieneDecimalesSignificativos(cantidad, 3)) {
                                cantidadProdSus += prod.getCant1();
                            } else {
                                cantidadProdSus += 1;
                            }
                        } else if (prod.getUni_med1().matches("ST|BAG|PAK|CS")) {
                            cantidadProdSus += prod.getCant1();
                        } else {
                            cantidadProdSus += 1;
                        }
                    } else {
                        fila.setVariable("{can_pro_sus1}", "&nbsp;");
                        fila.setVariable("{can_pro_sus2}", prod.getCant2() + "");
                        fila.setVariable("{descripcion_sus1}", "&nbsp;");
                        if (prod.getDescr2().length() > 48) {
                            fila.setVariable("{descripcion_sus2}", prod.getDescr2().substring(0, 48) + "");
                        } else {
                            fila.setVariable("{descripcion_sus2}", prod.getDescr2() + "");
                        }
                        fila.setVariable("{unitario_sus1}", "&nbsp;");
                        fila.setVariable("{unitario_sus1_tot}", "&nbsp;");

                        fila.setVariable("{unitario_sus2}", Formatos.formatoPrecio(prod.getPrecio2())+ "");
                        fila.setVariable("{unitario_sus2_tot}", Formatos.formatoPrecio(Utils.redondear(prod.getPrecio2() * prod.getCant2())) + "");
                    }
                    fila.setVariable("{cri_sus}", prod.getDescCriterio() + "");
                    listaProductosSus.add(fila);
                }

                // Obtener Productos Faltantes
                List productosFaltantes = dao1.getFaltantesByPedidoId(id_pedido);
                ArrayList listaProductosFal = new ArrayList();
                for (Iterator iterator = productosFaltantes.iterator(); iterator.hasNext();) {
                    ProductosPedidoEntity prod = (ProductosPedidoEntity) iterator.next();
                    IValueSet fila = new ValueSet();
                    if (prod.getCant_faltan() > 0.1) {
                        fila.setVariable("{can_pro_fal}", prod.getCant_faltan()+ "");
                        if (prod.getDescripcion().length() > 48)
                            fila.setVariable("{descripcion_fal}", prod.getDescripcion().substring(0, 48) + "");
                        else
                            fila.setVariable("{descripcion_fal}", prod.getDescripcion() + "");
                        fila.setVariable("{unitario_fal}", Formatos.formatoPrecio(prod.getPrecio()) + "");
                        fila.setVariable("{total_fal}", Formatos.formatoPrecio(Utils.redondear(prod.getPrecio()* prod.getCant_faltan())) + "");
                        if (prod.getUni_med().length() == 0) {
                            double cantidad = prod.getCant_faltan();
                            if (!NumericUtils.tieneDecimalesSignificativos(cantidad, 3))
                                cantidadProdFal += prod.getCant_faltan();
                            else
                                cantidadProdFal += 1;
                        } else if (prod.getUni_med().matches("ST|BAG|PAK|CS"))
                            cantidadProdFal += prod.getCant_faltan();
                        else
                            cantidadProdFal += 1;

                        fila.setVariable("{cri_sus}", prod.getDescCriterio() + "");
                        listaProductosFal.add(fila);
                    }
                }
                if (horaUltimaTrx != null && horaUltimaTrx.length() > 3) {
                    top.setVariable("{hora_caja}", horaUltimaTrx.substring(0, 2) + ":" + horaUltimaTrx.substring(2, 4));
                } else {
                    top.setVariable("{hora_caja}", " ");
                }
                if (cliente.getNombre().equalsIgnoreCase("Invitado")){//si es invitado sacamos el nombre que se ingreso en el pedido
                	cliente.setNombre(ped.getNom_cliente());                	
                }
                top.setVariable("{nombre_cliente}", cliente.getNombre());
                top.setVariable("{fecha_tramo}", fecha_tramo);
                if (ped.getTipo_despacho().equalsIgnoreCase("R")) {
                    top.setVariable("{accion_a_realizar}", "retiro");
                } else {
                    top.setVariable("{accion_a_realizar}", "despacho");
                }
                top.setVariable("{can_pro_env}", cantidadProdEnv + "");
                top.setVariable("{can_pro_sus}", cantidadProdSus + "");
                top.setVariable("{can_pro_fal}", cantidadProdFal + "");
                top.setVariable("{idped}", ped.getId_pedido() + ped.getSecuenciaPago() + "");
                top.setVariable("{monto}", Formatos.formatoPrecio(montoTotal) + "");
                if (ped.getTipo_despacho().equalsIgnoreCase("R")){
	                top.setVariable("{local_retiro}",ped.getIndicacion());
	                top.setVariable("{dir_retiro}",ped.getDir_calle());
	                top.setVariable("{persona_autorizada}", ped.getSin_gente_txt() + "");
                }
                top.setDynamicValueSets("LISTA_PROD_SUS", listaProductosSus);
                top.setDynamicValueSets("LISTA_PROD_FAL", listaProductosFal);

                if (cantidadProdSus == 0) {
                    top.setVariable("{oculta_prod_sus_ini}", "<!--");
                    top.setVariable("{oculta_prod_sus_fin}", "-->");
                } else {
                    top.setVariable("{oculta_prod_sus_ini}", "");
                    top.setVariable("{oculta_prod_sus_fin}", "");
                }

                if (cantidadProdFal == 0) {
                    top.setVariable("{oculta_prod_falt_ini}", "<!--");
                    top.setVariable("{oculta_prod_falt_fin}", "-->");
                } else {
                    top.setVariable("{oculta_prod_falt_ini}", "");
                    top.setVariable("{oculta_prod_falt_fin}", "");
                }

                if (cantidadProdSus == 0 && cantidadProdFal == 0) {
                    top.setVariable("{sin_sus_ni_falt}", "Tu pedido no tiene ni sustitutos ni faltantes");
                } else {
                    top.setVariable("{sin_sus_ni_falt}", "");
                }

                // Inicio generación del nuevo mail Tu compra está lista
                // fvasquez
                double cantidadSolicitada = 0;
                long cantSolicU = 0;
                double precioSolicitado = 0;
                long montoSolicitado = 0;

                List producSolicitados = dao1.productosSolicitados(id_pedido);

                for (int i = 0; i < producSolicitados.size(); ++i) {

                    HashMap productos = (HashMap) producSolicitados.get(i);

                    cantidadSolicitada = Double.parseDouble((String) productos
                            .get("CANT_SOLIC"));

                    if ((productos.get("UNI_MED") != null)
                            && !(productos.get("UNI_MED").toString()
                            .matches("ST|BAG|PAK|CS"))) {
                        cantSolicU += 1;
                    } else {
                        cantSolicU += (long) cantidadSolicitada;
                    }

                    precioSolicitado = Double.parseDouble((String) productos
                            .get("PRECIO"));
                    montoSolicitado += Utils
                            .redondear((precioSolicitado * cantidadSolicitada));
                }

                top.setVariable("{pod_sol_sku}",
                        Formatos.formatoUnidad(String.valueOf(producSolicitados
                                .size())));
                top.setVariable("{pod_sol_unds}",
                        Formatos.formatoUnidad(String.valueOf(cantSolicU)));
                top.setVariable("{monto_prod_sol}",
                        String.valueOf(Formatos.formatoPrecio(montoSolicitado)));
                // --------------------------------------------------------------------------------------------------

                double cantidadFaltante = 0;
                long cantSolicFaltU = 0;
                double precioFaltante = 0;
                long montoFaltantes = 0;
                ArrayList totalProdFalt = new ArrayList();
                List producFaltantes = dao1.productosFaltantes(id_pedido);

                for (int i = 0; i < producFaltantes.size(); ++i) {

                    HashMap productos = (HashMap) producFaltantes.get(i);

                    cantidadFaltante = Double.parseDouble((String) productos
                            .get("CANT_FALTAN"));

                    if ((productos.get("UNI_MED") != null)
                            && !(productos.get("UNI_MED").toString()
                            .matches("ST|BAG|PAK|CS"))) {
                        cantSolicFaltU += 1;
                    } else {
                        cantSolicFaltU += (long) cantidadFaltante;
                    }

                    precioFaltante = Double.parseDouble((String) productos
                            .get("PRECIO"));
                    montoFaltantes += Utils
                            .redondear((precioFaltante * cantidadFaltante));
                    // Muestra el detalle de los productos faltantes
                    ValueSet varProdSFalta = new ValueSet();
                    if (productos.get("DESCRIPCION").toString().length() > 48) {
                        varProdSFalta.setVariable("{prodSolicFaltante}",
                                productos.get("DESCRIPCION").toString()
                                        .substring(0, 48));
                    } else {
                        varProdSFalta.setVariable("{prodSolicFaltante}",
                                productos.get("DESCRIPCION").toString());
                    }
                    varProdSFalta.setVariable("{cantProdSolFaltante}", Formatos
                            .formatoUnidad((String.valueOf(cantidadFaltante))));
                    varProdSFalta.setVariable("{precioSolicFaltante}", String
                            .valueOf(Formatos.formatoPrecio(precioFaltante)));
                    varProdSFalta.setVariable("{difPrecioFaltante}", String
                            .valueOf(Formatos.formatoPrecio(Double
                                    .parseDouble(productos.get("TOTAL_PRECIO")
                                            .toString()))));
                    totalProdFalt.add(varProdSFalta);
                }
                top.setDynamicValueSets("PRODUC_FALTANTES", totalProdFalt);
                top.setVariable("{can_pro_fal_sku}", Formatos
                        .formatoUnidad(String.valueOf(producFaltantes.size())));
                top.setVariable("{can_pro_fal_uni}",
                        Formatos.formatoUnidad(String.valueOf(cantSolicFaltU)));
                top.setVariable("{monto_faltan}",
                        String.valueOf(Formatos.formatoPrecio(montoFaltantes)));
                // --------------------------------------------------------------------------------------------------

                ArrayList producEnvia = new ArrayList();
                List productosEnviados = dao1.productosEnviadosXmail(id_pedido);
                String descripcion = "";

                for (int i = 0; i < productosEnviados.size(); ++i) {

                    HashMap productos = (HashMap) productosEnviados.get(i);
                    // Muestra el detalle de los productos enviados
                    ValueSet varProdsEnviados = new ValueSet();
                    varProdsEnviados.setVariable("{valor_env}", Formatos
                            .formatoIntervalo(Double.parseDouble(productos.get(
                                    "CANT_ENVIADA").toString())));

                    descripcion = productos.get("DESCRIP_ENV").toString();

                    if (productos.get("SUST").toString().equalsIgnoreCase("S")) {
                        descripcion = cambiaPrimeraLetraAMayucula(descripcion);
                    }

                    if (descripcion.length() > 65) {
                        varProdsEnviados.setVariable("{descripcion_env}",
                                descripcion.substring(0, 65));
                    } else {
                        varProdsEnviados.setVariable("{descripcion_env}",
                                descripcion.toString());
                    }

                    varProdsEnviados.setVariable("{precio_unitario_env}",
                            Formatos.formatoPrecio(Double.parseDouble(productos
                                    .get("PRECIO_ENV").toString())));
                    producEnvia.add(varProdsEnviados);
                }
                top.setDynamicValueSets("PRODUC_ENVI", producEnvia);

                // --------------------------------------------------------------------------------------------------

                double cantidadSoli = 0;
                double cantidadPick = 0;
                double precioSoli = 0;
                double precioPick = 0;
                double sumaMontoSolic = 0;
                double sumaMontoPick = 0;
                long cantProdReemplU = 0;
                long sumaTotalProdReemplazados = 0;
                ArrayList totalProdSus = new ArrayList();
                
                ExcesoCtrl oExceso = new ExcesoCtrl();
                boolean  isActiva=oExceso.isActivaCorreccionAutomatica();
                
                List producReemplaMenorValor = dao1.productosReemplazadosMenorValor(id_pedido,isActiva);

                for (int i = 0; i < producReemplaMenorValor.size(); ++i) {

                    // Muestra resumen de productos reemplazados por productos
                    // de menor valor
                    HashMap productos = (HashMap) producReemplaMenorValor.get(i);

                    cantidadSoli = Double.parseDouble((String) productos.get("CANT_SOLIC"));
                    cantidadPick = Double.parseDouble((String) productos.get("CANT_PICK"));
                    precioSoli = Double.parseDouble((String) productos.get("PRECIO_SOLIC"));
                    precioPick = Double.parseDouble((String) productos.get("PRECIO_PICK"));

                    if ((productos.get("UNI_MED") != null) && !(productos.get("UNI_MED").toString().matches("ST|BAG|PAK|CS"))) {
                        cantProdReemplU += 1;
                    } else {
                        // cantProdReemplU += (long) cantidadPick;
                        cantProdReemplU += (long) cantidadSoli;
                    }
                    sumaMontoSolic += Utils.redondear(cantidadSoli * precioSoli);
                    sumaMontoPick += Utils.redondear(cantidadPick * precioPick);

                    // Muestra resumen de productos reemplazados por productos
                    // de menor valor, se incluyen productos con diferencia
                    // igual 0 (cero)
                    ValueSet varProdSus = new ValueSet();
                    if (productos.get("DESCRIP1").toString().length() > 48) {
                        varProdSus.setVariable("{descrpSolicitado}", productos.get("DESCRIP1").toString().substring(0, 48));
                    } else {
                        varProdSus.setVariable("{descrpSolicitado}", productos.get("DESCRIP1").toString());
                    }

                    varProdSus.setVariable("{cantidadSolicitada}", Formatos.formatoUnidad((String.valueOf(cantidadSoli))));
                    varProdSus.setVariable("{precioSolitado}", String.valueOf(Formatos.formatoPrecio(precioSoli)));

                    descripcion = productos.get("DESCRIP2").toString();
                    descripcion = cambiaPrimeraLetraAMayucula(descripcion);

                    if (descripcion.length() > 48) {
                        varProdSus.setVariable("{productoSustituto}", descripcion.substring(0, 48));
                    } else {
                        varProdSus.setVariable("{productoSustituto}", descripcion.toString());
                    }
                    varProdSus.setVariable("{cantidadProdSustituto}", Formatos.formatoUnidad((String.valueOf(cantidadPick))));
                    varProdSus.setVariable("{precioSustituto}",  String.valueOf(Formatos.formatoPrecio(precioPick)));
                    varProdSus.setVariable("{diferenciaPrecioMenor}", String.valueOf(Formatos.formatoPrecio(Double.parseDouble(productos.get("DIF_PRECIO").toString()))));
                    totalProdSus.add(varProdSus);
                }
                top.setDynamicValueSets("PROD_MENOR_VALOR", totalProdSus);
                sumaTotalProdReemplazados += producReemplaMenorValor.size();
                top.setVariable("{can_pro_reem_sku}", Formatos.formatoUnidad(String.valueOf(producReemplaMenorValor.size())));
                top.setVariable("{can_pro_reemp}", Formatos.formatoUnidad((String.valueOf(cantProdReemplU))));
                String montoReem = String.valueOf(Formatos.formatoPrecio(sumaMontoSolic- sumaMontoPick));
                top.setVariable("{monto_reemp}",montoReem);
                top.setVariable("{costo_despacho}", String.valueOf(Formatos.formatoPrecio(ped.getCosto_despacho())));
                // --------------------------------------------------------------------------------------------------

                cantidadSoli = 0;
                cantidadPick = 0;
                precioSoli = 0;
                precioPick = 0;
                sumaMontoSolic = 0;
                sumaMontoPick = 0;
                cantProdReemplU = 0;
                ArrayList totalProdSusBenef = new ArrayList();
                List producReemplaMayorValor = dao1
                        .productoosReemplazadosMayorValor(id_pedido);

                for (int i = 0; i < producReemplaMayorValor.size(); ++i) {

                    HashMap productos = (HashMap) producReemplaMayorValor
                            .get(i);

                    cantidadSoli = Double.parseDouble((String) productos
                            .get("CANT_SOLIC"));
                    cantidadPick = Double.parseDouble((String) productos
                            .get("CANT_PICK"));
                    precioSoli = Double.parseDouble((String) productos
                            .get("PRECIO_SOLIC"));
                    precioPick = Double.parseDouble((String) productos
                            .get("PRECIO_PICK"));

                    if ((productos.get("UNI_MED") != null)
                            && !(productos.get("UNI_MED").toString()
                            .matches("ST|BAG|PAK|CS"))) {
                        cantProdReemplU += 1;
                    } else {
                        cantProdReemplU += (long) cantidadPick;
                    }
                    sumaMontoSolic += Utils
                            .redondear(cantidadSoli * precioSoli);
                    sumaMontoPick += Utils.redondear(cantidadPick * precioPick);

                    // Muestra resumen de productos reemplazados por productos
                    // de MAYOR valor
                    ValueSet varProdSusBenef = new ValueSet();
                    if (productos.get("DESCRIP1").toString().length() > 48) {
                        varProdSusBenef.setVariable(
                                "{descrpSolicBenef}",
                                productos.get("DESCRIP1").toString()
                                        .substring(0, 48));
                    } else {
                        varProdSusBenef.setVariable("{descrpSolicBenef}",
                                productos.get("DESCRIP1").toString());
                    }

                    varProdSusBenef.setVariable("{cantidadSolicBenef}",
                            Formatos.formatoUnidad((String
                                    .valueOf(cantidadSoli))));
                    varProdSusBenef.setVariable("{precioSoliciBenef}",
                            String.valueOf(Formatos.formatoPrecio(precioSoli)));

                    descripcion = productos.get("DESCRIP2").toString();
                    descripcion = cambiaPrimeraLetraAMayucula(descripcion);

                    if (descripcion.length() > 48) {
                        varProdSusBenef.setVariable("{productoSustitBenef}",
                                descripcion.substring(0, 48));
                    } else {
                        varProdSusBenef.setVariable("{productoSustitBenef}",
                                descripcion);
                    }
                    varProdSusBenef.setVariable("{cantidadProdSustitBenef}",
                            Formatos.formatoUnidad((String
                                    .valueOf(cantidadPick))));
                    varProdSusBenef.setVariable("{precioSustitBenef}",
                            String.valueOf(Formatos.formatoPrecio(precioPick)));
                    varProdSusBenef
                            .setVariable("{difePrecioMenorBenef}", String
                                    .valueOf(Formatos.formatoPrecio(Double
                                            .parseDouble(productos.get(
                                                    "DIF_PREC_BENEF")
                                                    .toString()))));
                    totalProdSusBenef.add(varProdSusBenef);
                }
                top.setDynamicValueSets("PROD_MAYOR_VALOR", totalProdSusBenef);
                sumaTotalProdReemplazados += producReemplaMayorValor.size();
                top.setVariable("{sumaTotalProdReempl}", Formatos
                        .formatoUnidad(String
                                .valueOf(sumaTotalProdReemplazados)));
                top.setVariable("{prodBenefReemp}", Formatos
                        .formatoUnidad(String.valueOf(producReemplaMayorValor
                                .size())));
                top.setVariable(
                        "{montoBenfReempl}",
                        String.valueOf(Formatos.formatoPrecio(sumaMontoPick
                                - sumaMontoSolic)));

                top.setVariable("{versionListener}",versionListener);
                 /** modificacion despliegue prodbenefreemplazo**/
                ValueSet benefReemplazoPanel =  new ValueSet();
                List benefReemplazo = new ArrayList();
                if(null != Formatos.formatoUnidad(String.valueOf(producReemplaMayorValor
                                .size())) && !"0".equals(Formatos.formatoUnidad(String.valueOf(producReemplaMayorValor.size())))){
               	 benefReemplazoPanel.setVariable("{sumaTotalProdReempl}", Formatos
                            .formatoUnidad(String
                                    .valueOf(sumaTotalProdReemplazados)));
               	benefReemplazoPanel.setVariable(
                        "{montoBenfReempl}",
                        String.valueOf(Formatos.formatoPrecio(sumaMontoPick
                                - sumaMontoSolic)));
               	benefReemplazoPanel.setVariable("{prodBenefReemp}", Formatos
                        .formatoUnidad(String.valueOf(producReemplaMayorValor
                                .size())));
             	benefReemplazo.add(benefReemplazoPanel);
                	top.setDynamicValueSets("BENEFICIO_REEMPLAZO", benefReemplazo);
                } 
                if(null != totalProdSus && !totalProdSus.isEmpty()){
                	 ValueSet existeProdSus =  new ValueSet();
                     List existeProdSusList= new ArrayList();
                     existeProdSusList.add(existeProdSus);
                     existeProdSus.setVariable(
                             "{monto_reemp}",
                             montoReem);
                     existeProdSus.setVariable("{sumaTotalProdReempl}", Formatos
                             .formatoUnidad(String
                                     .valueOf(sumaTotalProdReemplazados)));
                     top.setDynamicValueSets("EXISTE_PRODUCTOS_SUSTITUTOS", existeProdSusList);
                }
                if(null != totalProdSusBenef && !totalProdSusBenef.isEmpty()){
               	 	ValueSet existeProdSusBenef =  new ValueSet();
               	 	existeProdSusBenef.setVariable("{prodBenefReemp}", Formatos
                         .formatoUnidad(String.valueOf(producReemplaMayorValor
                                 .size())));
               	 	existeProdSusBenef.setVariable(
                         "{montoBenfReempl}",
                         String.valueOf(Formatos.formatoPrecio(sumaMontoPick
                                 - sumaMontoSolic)));
                    List existeProdSusBenefList= new ArrayList();
                    existeProdSusBenefList.add(existeProdSusBenef);
                    top.setDynamicValueSets("EXISTE_PROD_MAYOR_VALOR", existeProdSusBenefList);
               }
                if(null != totalProdFalt && !totalProdFalt.isEmpty()){
               	 	ValueSet existeProdFaltantes =  new ValueSet();
               	 	existeProdFaltantes.setVariable("{can_pro_fal_sku}", Formatos
                         .formatoUnidad(String.valueOf(producFaltantes.size())));
               	 	existeProdFaltantes.setVariable("{monto_faltan}",
                         String.valueOf(Formatos.formatoPrecio(montoFaltantes)));
                    List existeProdFaltantesList= new ArrayList();
                    existeProdFaltantesList.add(existeProdFaltantes);
                    top.setDynamicValueSets("EXISTE_PROD_FALTANTES", existeProdFaltantesList);
               }
                
                
                
                /** fin MODIFICACION **/
                
                // Fin generación del nuevo mail Tu compra está lista
                String result = tem.toString(top);

                // Código comentado. Se utilizó para probar localmente
                //              try {
                //                  BufferedWriter out = new BufferedWriter(new
                // FileWriter("test.html"));
                //                  out.write(result);
                //                  out.close();
                //              } catch (IOException e) {
                //                  e.printStackTrace();
                //              }

                //Registro en el log del pedido diferencia de monto entre lo calculado por POS y los valores entregados por mail tu compra esta lista.
                try{
            		montoFlujo  = (montoSolicitado - montoFaltantes - (long)(sumaMontoSolic - sumaMontoPick) + (long)ped.getCosto_despacho() );
                    if(montoFlujo != montoTotal){
                		//agregar la observacion al Log
                        LogPedidoDTO log = new LogPedidoDTO();
                        log.setId_pedido(ped.getId_pedido());
                        log.setLog("[MAIL-COMPRA-LISTA] Monto POS distinto a monto mail. POS:"+montoTotal+" Mail:"+montoFlujo);
                        log.setUsuario("SYSTEM");
                        dao1.addLogPedido(log);
                    }
            	} catch (Exception e) {}
                
                // Se graba mail al cliente para posteriormente ser enviado
                MailDTO mail = new MailDTO();
                mail.setFsm_subject(cliente.getNombre() + ", " + rb.getString("mail.pedidos.subject"));
                mail.setFsm_data(result);
                mail.setFsm_destina(emailAddress);
                mail.setFsm_remite(rb.getString("mail.pedidos.remite"));
                emailDAO.addMail(mail);
                //-20121012VMatheu
                if (dao1.actEstadoEnvioEMail(id_pedido)) {
                    logger.info("Se envió el EMail para el pedido N° " + id_pedido);
                }

            } else {
                throw new PedidosException("Envío de Email en Pedidos: No todas las transacciones para pedido: " 
                        + id_pedido + " están pagadas o \n" + "El EMail ya fue enviado con anterioridad.");
            }
        } catch (PedidosException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(e);
        }
    }

    private String cambiaPrimeraLetraAMayucula(String descrip) {
        char[] descripAux = null;
        descrip = descrip.toLowerCase();
        descripAux = descrip.toCharArray();
        descripAux[0] = Character.toUpperCase(descripAux[0]);
        for (int s = 0; s < descrip.length() - 2; s++) {
            if (descripAux[s] == ' ' || descripAux[s] == '.'
                    || descripAux[s] == ',') {
                descripAux[s + 1] = Character.toUpperCase(descripAux[s + 1]);
            }
        }
        descrip = String.valueOf(descripAux);
        return descrip;
    }


    public void sendEmailByPedidoTest(long id_pedido, String emailAddress) throws PedidosException, SystemException {
        JdbcEmailDAO emailDAO = (JdbcEmailDAO) cl.bbr.jumbocl.shared.emails.dao.DAOFactory.getDAOFactory(
        		DAOFactory.JDBC).getEmailDAO();

        JdbcPedidosDAO dao1 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        JdbcTrxMedioPagoDAO dao2 = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        JdbcDespachosDAO dao3 = (JdbcDespachosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getDespachosDAO();

        try {

            // Verifica que todas las transacciones estén pagadas
            //if (dao2.isAllTrxMPbyPedidoAndState(id_pedido, Constantes.ID_ESTAD_TRXMP_PAGADA)) {
            if (true) {
                // Obtiene template para Email, agrega datos y envía email
                ResourceBundle rb = ResourceBundle.getBundle("bo");
                //Se lee el archivo para el mail
                // Recupera pagina desde web.xml

                // Codigo se uso para probar local
                //String pag_form = rb.getString("conf.path.html") + rb.getString("mail.pedidos.pathTemplate.html");
                PedidoDTO ped = dao1.getPedidoById(id_pedido);
                String pag_form=rb.getString("mail.pedidos.pathTemplate.html");
                if (ped.getTipo_despacho().equalsIgnoreCase(Constantes.TIPO_DESPACHO_RETIRO_CTE)){
                	pag_form = rb.getString("mail.pedidos.pathTemplate.html.retiro");
                }

                // Carga el template html
                TemplateLoader load = new TemplateLoader(pag_form);
                ITemplate tem = load.getTemplate();
                
                ClienteEntity cliente = dao1.getClienteById(ped.getId_cliente());

                // TObtener Clientes y datos del Cliente
                //String emailAddress = cliente.getEmail();

                long montoTotal = 0;
                String horaUltimaTrx = "";
                long cantidadProdEnv = 0;
                long cantidadProdSus = 0;
                long cantidadProdFal = 0;
                DespachoDTO despacho = dao3.getDespachoById(id_pedido);
                String fecha = despacho.getF_despacho();

                // Obtener Día y Jornada de Despacho
                String fecha_tramo = "";
                if (ped.getTipo_despacho().equalsIgnoreCase(Constantes.TIPO_DESPACHO_ECONOMICO_CTE)) {
                    fecha_tramo = fecha.substring(8, 10) + "/" + fecha.substring(5, 7) + "/" + fecha.substring(0, 4);
                } else {
                    fecha_tramo = fecha.substring(8, 10) + "/" + fecha.substring(5, 7) + "/" + fecha.substring(0, 4) + ", entre " + despacho.getH_ini() + " y " + despacho.getH_fin() + " hrs.";
                }

                // Obtener Número de Pedido, Monto Total, Productos Enviados
                List trxMP = dao2.getTrxMpByIdPedido(id_pedido);
                //ArrayList listaProductosEnv = new ArrayList();

                for (Iterator iter = trxMP.iterator(); iter.hasNext();) {
                    MonitorTrxMpDTO trxmps = (MonitorTrxMpDTO) iter.next();
                    //montoTotal += trxmps.getMonto_trxmp();
                    montoTotal += trxmps.getPos_monto_fp();
                    horaUltimaTrx = trxmps.getPos_hora();
                }

                List datos_cat = dao1.productosEnviadosPedidoForEmail(id_pedido);

                List fm_cate = new ArrayList();
                double precio_total = 0;
                int contador = 0;
                long totalizador = 0;
                IValueSet fila_lista_sel = null;
                List aux_blanco = null;
                long total_producto_pedido = 0;

                //Llenamos la tabla con los productos ordenados por categorias
                for (int i = 0; i < datos_cat.size(); i++) {

                    CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) datos_cat.get(i);

                    IValueSet fila_cat = new ValueSet();
                    fila_cat.setVariable("{categoria}", cat.getCategoria());

                    List prods = cat.getCarroCompraProductosDTO();

                    List fm_prod = new ArrayList();
                    for (int j = 0; j < prods.size(); j++) {

                        CarroCompraProductosDTO producto = (CarroCompraProductosDTO) prods.get(j);

                        total_producto_pedido += Math.ceil(producto.getCantidad());

                        IValueSet fila_pro = new ValueSet();
                        fila_pro.setVariable("{descripcion}", producto.getNombre());
                        fila_pro.setVariable("{marca}", producto.getMarca());
                        fila_pro.setVariable("{cod_sap}", producto.getCodigo());
                        fila_pro.setVariable("{valor}", Formatos.formatoIntervalo(producto.getCantidad()) + "");

                        if (producto.getUnidadMedida().length() == 0) {
                            if (!NumericUtils.tieneDecimalesSignificativos(producto.getCantidad(), 3)) {
                                cantidadProdEnv += new Double(producto.getCantidad()).longValue();
                            } else {
                                cantidadProdEnv += 1;
                            }
                        } else if (producto.getUnidadMedida().matches("ST|BAG|PAK|CS")) {
                            cantidadProdEnv += new Double(producto.getCantidad()).longValue();
                        } else {
                            cantidadProdEnv += 1;
                        }

                        fila_pro.setVariable("{carr_id}", producto.getCar_id() + "");
                        fila_pro.setVariable("{contador}", contador + "");

                        precio_total = 0;

                        // Si el producto es con seleccion
                        if (producto.getUnidad_tipo().charAt(0) == 'S') {

                            fila_lista_sel = new ValueSet();

                            List aux_lista = new ArrayList();
                            for (double v = 0; v <= producto.getInter_maximo(); v += producto.getInter_valor()) {
                                IValueSet aux_fila = new ValueSet();
                                aux_fila.setVariable("{valor}", Formatos.formatoIntervalo(v) + "");
                                aux_fila.setVariable("{opcion}", Formatos.formatoIntervalo(v) + "");
                                if (Formatos.formatoIntervalo(v).compareTo(Formatos.formatoIntervalo(producto.getCantidad())) == 0)
                                    aux_fila.setVariable("{selected}","selected");
                                else
                                    aux_fila.setVariable("{selected}", "");
                                aux_lista.add(aux_fila);
                            }
                            fila_lista_sel.setVariable("{contador}", contador + "");
                            fila_lista_sel.setDynamicValueSets("CANTIDADES", aux_lista);

                            aux_blanco = new ArrayList();
                            aux_blanco.add(fila_lista_sel);
                            fila_pro.setDynamicValueSets("LISTA_SEL", aux_blanco);

                        } else {
                            fila_lista_sel = new ValueSet();
                            fila_lista_sel.setVariable("{contador}", contador + "");
                            fila_lista_sel.setVariable("{valor}", producto.getCantidad() + "");
                            fila_lista_sel.setVariable("{maximo}", producto.getInter_maximo() + "");
                            fila_lista_sel.setVariable("{intervalo}", producto.getInter_valor() + "");
                            aux_blanco = new ArrayList();
                            aux_blanco.add(fila_lista_sel);
                            fila_pro.setDynamicValueSets("INPUT_SEL", aux_blanco);
                        }

                        if (producto.isCon_nota() == true) {
                            IValueSet set_nota = new ValueSet();
                            set_nota.setVariable("{nota}", producto.getNota() + "");
                            set_nota.setVariable("{contador}", contador + "");
                            aux_blanco = new ArrayList();
                            aux_blanco.add(set_nota);
                            fila_pro.setDynamicValueSets("NOTA", aux_blanco);
                        }

                        precio_total = Utils.redondear(producto.getPrecio());
                        fila_pro.setVariable("{unidad}", producto.getTipre());
                        fila_pro.setVariable("{precio_unitario}", Formatos.formatoPrecio(producto.getPpum()));
                        fila_pro.setVariable("{precio_total}", Formatos.formatoPrecio(precio_total));
                        fila_pro.setVariable("{CLASE_TABLA}", "TablaDiponiblePaso1");
                        fila_pro.setVariable("{CLASE_CELDA}", "celda1");
                        fila_pro.setVariable("{NO_DISPONIBLE}", "");
                        fila_pro.setVariable("{OPCION_COMPRA}", "1");
                        totalizador += precio_total;

                        contador++;
                        fm_prod.add(fila_pro);

                    }
                    fila_cat.setDynamicValueSets("PRODUCTOS", fm_prod);
                    fm_cate.add(fila_cat);

                }

                IValueSet top = new ValueSet();
                top.setDynamicValueSets("CATEGORIAS", fm_cate);
                //top.setVariable("{total}", Formatos.formatoPrecio(totalizador)+"");

                // Obtener Productos Sustituidos
                List productosSustituidos = dao1.productosSustitutosPedidoForEmail(id_pedido);
                ArrayList listaProductosSus = new ArrayList();
                List sustheads = new ArrayList();
                for (Iterator iterator = productosSustituidos.iterator(); iterator.hasNext();) {
                    SustitutoDTO prod = (SustitutoDTO) iterator.next();
                    IValueSet fila = new ValueSet();
                    if ((prod.getCod_prod1() != null) && (prod.getUni_med1() != null)
                            && !sustheads.contains(prod.getCod_prod1() + prod.getUni_med1())) {
                        sustheads.add(prod.getCod_prod1() + prod.getUni_med1());
                        fila.setVariable("{can_pro_sus1}", prod.getCant1() + "");
                        fila.setVariable("{can_pro_sus2}", prod.getCant2() + "");
                        if (prod.getDescr1().length() > 48)
                            fila.setVariable("{descripcion_sus1}", prod.getDescr1().substring(0, 48) + "");
                        else
                            fila.setVariable("{descripcion_sus1}", prod.getDescr1() + "");
                        if (prod.getDescr2().length() > 48)
                            fila.setVariable("{descripcion_sus2}", prod.getDescr2().substring(0, 48) + "");
                        else
                            fila.setVariable("{descripcion_sus2}", prod.getDescr2() + "");

                        fila.setVariable("{unitario_sus1}", Formatos.formatoPrecio(prod.getPrecio1()) + "");
                        fila.setVariable("{unitario_sus1_tot}", Formatos.formatoPrecio(Utils.redondear(prod .getPrecio1() * prod.getCant1())) + "");

                        fila.setVariable("{unitario_sus2}", Formatos.formatoPrecio(prod.getPrecio2()) + "");
                        fila.setVariable("{unitario_sus2_tot}", Formatos.formatoPrecio(Utils.redondear(prod.getPrecio2() * prod.getCant2())) + "");

                        if (prod.getUni_med1().length() == 0) {
                            double cantidad = prod.getCant1();
                            if (!NumericUtils.tieneDecimalesSignificativos(cantidad, 3)) {
                                cantidadProdSus += prod.getCant1();
                            } else {
                                cantidadProdSus += 1;
                            }
                        } else if (prod.getUni_med1().matches("ST|BAG|PAK|CS")) {
                            cantidadProdSus += prod.getCant1();
                        } else {
                            cantidadProdSus += 1;
                        }
                    } else {
                        fila.setVariable("{can_pro_sus1}", "&nbsp;");
                        fila.setVariable("{can_pro_sus2}", prod.getCant2() + "");
                        fila.setVariable("{descripcion_sus1}", "&nbsp;");
                        if (prod.getDescr2().length() > 48) {
                            fila.setVariable("{descripcion_sus2}", prod.getDescr2().substring(0, 48) + "");
                        } else {
                            fila.setVariable("{descripcion_sus2}", prod.getDescr2() + "");
                        }
                        fila.setVariable("{unitario_sus1}", "&nbsp;");
                        fila.setVariable("{unitario_sus1_tot}", "&nbsp;");

                        fila.setVariable("{unitario_sus2}", Formatos.formatoPrecio(prod.getPrecio2()) + "");
                        fila.setVariable("{unitario_sus2_tot}", Formatos.formatoPrecio(Utils.redondear(prod.getPrecio2() * prod.getCant2())) + "");
                    }
                    fila.setVariable("{cri_sus}", prod.getDescCriterio() + "");
                    listaProductosSus.add(fila);
                }

                // Obtener Productos Faltantes
                List productosFaltantes = dao1.getFaltantesByPedidoId(id_pedido);
                ArrayList listaProductosFal = new ArrayList();
                for (Iterator iterator = productosFaltantes.iterator(); iterator.hasNext();) {
                    ProductosPedidoEntity prod = (ProductosPedidoEntity) iterator.next();
                    IValueSet fila = new ValueSet();
                    if (prod.getCant_faltan() > 0.1) {
                        fila.setVariable("{can_pro_fal}", prod.getCant_faltan() + "");
                        if (prod.getDescripcion().length() > 48)
                            fila.setVariable("{descripcion_fal}", prod.getDescripcion().substring(0, 48) + "");
                        else
                            fila.setVariable("{descripcion_fal}", prod.getDescripcion() + "");
                        fila.setVariable("{unitario_fal}", Formatos.formatoPrecio(prod.getPrecio()) + "");
                        fila.setVariable("{total_fal}", Formatos.formatoPrecio(Utils.redondear(prod.getPrecio() * prod.getCant_faltan())) + "");
                        if (prod.getUni_med().length() == 0) {
                            double cantidad = prod.getCant_faltan();
                            if (!NumericUtils.tieneDecimalesSignificativos(cantidad, 3))
                                cantidadProdFal += prod.getCant_faltan();
                            else
                                cantidadProdFal += 1;
                        } else if (prod.getUni_med().matches("ST|BAG|PAK|CS"))
                            cantidadProdFal += prod.getCant_faltan();
                        else
                            cantidadProdFal += 1;

                        fila.setVariable("{cri_sus}", prod.getDescCriterio() + "");
                        listaProductosFal.add(fila);
                    }
                }
                if (horaUltimaTrx != null && horaUltimaTrx.length() > 3) {
                    top.setVariable("{hora_caja}", horaUltimaTrx.substring(0, 2) + ":" + horaUltimaTrx.substring(2, 4));
                } else {
                    top.setVariable("{hora_caja}", " ");
                }
                top.setVariable("{nombre_cliente}", cliente.getNombre() + " " + cliente.getApellido_pat());
                top.setVariable("{fecha_tramo}", fecha_tramo);
                if (ped.getTipo_despacho().equalsIgnoreCase("R")) {
                    top.setVariable("{accion_a_realizar}", "retiro");
                } else {
                    top.setVariable("{accion_a_realizar}", "despacho");
                }
                top.setVariable("{can_pro_env}", cantidadProdEnv + "");
                top.setVariable("{can_pro_sus}", cantidadProdSus + "");
                top.setVariable("{can_pro_fal}", cantidadProdFal + "");
                //top.setVariable("{idped}", (id_pedido * 2 + 1) + "");
                top.setVariable("{idped}", ped.getId_pedido() + ped.getSecuenciaPago() + "");
                top.setVariable("{monto}", Formatos.formatoPrecio(montoTotal) + "");
                //top.setDynamicValueSets("LISTA_PROD_ENV", listaProductosEnv);
                top.setDynamicValueSets("LISTA_PROD_SUS", listaProductosSus);
                top.setDynamicValueSets("LISTA_PROD_FAL", listaProductosFal);

                if (cantidadProdSus == 0) {
                    top.setVariable("{oculta_prod_sus_ini}", "<!--");
                    top.setVariable("{oculta_prod_sus_fin}", "-->");
                } else {
                    top.setVariable("{oculta_prod_sus_ini}", "");
                    top.setVariable("{oculta_prod_sus_fin}", "");
                }

                if (cantidadProdFal == 0) {
                    top.setVariable("{oculta_prod_falt_ini}", "<!--");
                    top.setVariable("{oculta_prod_falt_fin}", "-->");
                } else {
                    top.setVariable("{oculta_prod_falt_ini}", "");
                    top.setVariable("{oculta_prod_falt_fin}", "");
                }

                if (cantidadProdSus == 0 && cantidadProdFal == 0) {
                    top.setVariable("{sin_sus_ni_falt}", "Tu pedido no tiene ni sustitutos ni faltantes");
                } else {
                    top.setVariable("{sin_sus_ni_falt}", "");
                }

                String result = tem.toString(top);

                // Código comentado. Se utilizó para probar localmente
                //			    try {
                //			        BufferedWriter out = new BufferedWriter(new
                // FileWriter("test.html"));
                //			        out.write(result);
                //			        out.close();
                //			    } catch (IOException e) {
                //			    	e.printStackTrace();
                //			    }

                // Se graba mail al cliente para posteriormente ser enviado
                MailDTO mail = new MailDTO();
                mail.setFsm_subject(rb.getString("mail.pedidos.subject"));
                mail.setFsm_data(result);
                mail.setFsm_destina(emailAddress);
                mail.setFsm_remite(rb.getString("mail.pedidos.remite"));
                emailDAO.addMail(mail);

                /*if (dao1.actEstadoEnvioEMail(id_pedido)) {
                    logger.info("Se envió el EMail para el pedido N° " + id_pedido);
                }*/
            } else {
                throw new PedidosException("Envío de Email en Pedidos: No todas las transacciones para pedido: "
                                + id_pedido + " están pagadas o \n" + "El EMail ya fue enviado con anterioridad.");
            }
        } catch (PedidosException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(e);
        }
    }

    /**
     * retorna los sustitutos de un pedido en particular, de la forma como se
     * envían en el email (Modificación según req. 500)
     * 
     * @param id_pedido
     *            long
     * @return List SustitutoDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List productosSustitutosPedidoForEmail(long id_pedido) throws PedidosException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            result = dao.productosSustitutosPedidoForEmail(id_pedido);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Recupera el Listado de Horas de Inicio e Jornadas de Despacho
     * (Modificación según req. 567)
     * 
     * @return List String
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getHorasInicioJDespacho() throws PedidosException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            result = dao.getHorasInicioJDespacho();
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Recupera el Listado de Horas de Inicio e Jornadas de Despacho
     * (Modificación según req. 567)
     * 
     * @return List String
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getJornadasDespachoByFecha(String fecha, int local) throws PedidosException {

        List result = new ArrayList();
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            result = dao.getJornadasDespachoByFecha(fecha, local);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Genera el pedido, a partir de productos seleccionados desde la cotizacion
     * 
     * @param pedido
     * @return id del nuevo pedido generado
     * @throws SystemException
     * @throws PedidosException
     */
    public long doGeneraPedido(ProcInsPedidoDTO pedido) throws SystemException, PedidosException {

        logger.debug("En doGeneraPedido() ...");

        long id_pedido = 0;

        // 1. Valida campos obligatorios del pedido
        if (pedido.getId_cliente() == 0) {
            //throw new PedidosException("Campo id_cliente es incorrecto: " +
            // pedido.getId_cliente());
            logger.error("Campo id_cliente es incorrecto: " + pedido.getId_cliente());
            throw new PedidosException(Constantes._EX_VE_GP_CAMPOS_INC);
        }
        if (pedido.getDir_id() == 0) {
            //throw new PedidosException("Campo dir_id es incorrecto:" +
            // pedido.getDir_id());
            logger.error("Campo dir_id es incorrecto:" + pedido.getDir_id());
            throw new PedidosException(Constantes._EX_VE_GP_CAMPOS_INC);
        }
        if (pedido.getId_jdespacho() == 0) {
            //throw new PedidosException("Campo id_jdespacho es incorrecto: " +
            // pedido.getId_jdespacho());
            logger.error("Campo id_jdespacho es incorrecto: " + pedido.getId_jdespacho());
            throw new PedidosException(Constantes._EX_VE_GP_CAMPOS_INC);
        }
        if (pedido.getMedio_pago().equals("")) {
            //throw new PedidosException("Campo medio pago es incorrecto: " +
            // pedido.getMedio_pago());
            logger.error("Campo medio pago es incorrecto: " + pedido.getMedio_pago());
            throw new PedidosException(Constantes._EX_VE_GP_CAMPOS_INC);
        }

        // Iteramos los productos
        double cant_prods = 0.0;
        double monto_total = 0.0;
        List lst_prods = pedido.getProductos();

        for (int i = 0; i < lst_prods.size(); i++) {
            ProcInsPedidoDetalleFODTO det1 = new ProcInsPedidoDetalleFODTO();
            det1 = (ProcInsPedidoDetalleFODTO) lst_prods.get(i);
            cant_prods += det1.getCant_solic();
            monto_total += (det1.getCant_solic() * det1.getPrecio_unitario());
        }

        // 2. Valida capacidades de despacho y de picking solo si es un pedido
        // normal
        CalendariosCtrl ctrl1 = new CalendariosCtrl();

        try {
            if ((pedido.getTipo_ve().equals(Constantes.TIPO_VE_NORMAL_CTE))
                    && (!ctrl1.doVerificaCapacidadDespacho((int) cant_prods, pedido.getId_jdespacho()))) {
                //throw new PedidosException("Excede capacidad de pedido y/o
                // despacho");
                logger.error("Excede capacidad de pedido y/o despacho");
                throw new PedidosException(Constantes._EX_VE_GP_EXCEDE_CAPAC);
            }

        } catch (DespachosException e) {
            logger.error("Error al verificar las capacidades de despacho");
            e.printStackTrace();
            throw new SystemException(e);
        }

        // Creamos los dao's
        JdbcPedidosDAO dao1 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        JdbcJornadasDAO dao2 = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();

        // Creamos la transacción
        JdbcTransaccion trx = new JdbcTransaccion();

        try {

            // Iniciamos la transacción
            trx.begin();

            // Asignamos la transacción a los dao's
            dao1.setTrx(trx);
            dao2.setTrx(trx);

        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new SystemException(e);
        } catch (JornadasDAOException e) {
            e.printStackTrace();
            throw new SystemException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(e);
        }

        // Creamos el objeto pedido
        PedidoDTO ped2 = new PedidoDTO();

        try {
            // 3. Recupera datos del cliente
            ClienteEntity cliente = dao1.getClienteById(pedido.getId_cliente());

            // 4. Recupera datos de la dirección de despacho
            DireccionEntity dir1 = dao1.getDireccionById(pedido.getDir_id());

            // 5. Obtiene la jornada de picking asociada
            JornadaDespachoEntity jor1 = dao2.getJornadaDespachoById(pedido.getId_jdespacho());

            // Llenamos dto pedido
            // si es pagado con TBK o CAT el pedido queda preingresado
            if (Constantes.MEDIO_PAGO_TBK.equalsIgnoreCase(pedido.getMedio_pago()) || Constantes.MEDIO_PAGO_CAT.equalsIgnoreCase(pedido.getMedio_pago()))
                ped2.setId_estado(Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO);
            else
                ped2.setId_estado(Constantes.ID_ESTAD_PEDIDO_INGRESADO);
            ped2.setId_jdespacho(pedido.getId_jdespacho());
            ped2.setId_jpicking(jor1.getId_jpicking());

            ped2.setId_local(pedido.getId_local_desp());
            LocalDTO loc = dao1.getLocalById(pedido.getId_local_desp());
            ped2.setTipo_picking(loc.getTipo_picking());

            ped2.setId_comuna(dir1.getCom_id().longValue());
            ped2.setId_zona(dir1.getZon_id().longValue());
            ped2.setId_cliente(pedido.getId_cliente());

            ped2.setId_usuario_fono(pedido.getId_usuario_fono());

            ped2.setGenero(cliente.getGenero());
            ped2.setFnac(cliente.getFec_nac());
            ped2.setRut_cliente(cliente.getRut().longValue());
            ped2.setDv_cliente(cliente.getDv());
            ped2.setNom_cliente(cliente.getNombre() + " " + cliente.getApellido_pat() + " " + cliente.getApellido_mat());
            String fono1 = "";
            if (cliente.getFon_cod_1() != null)
                fono1 = cliente.getFon_cod_1() + " ";
            if (cliente.getFon_num_1() != null)
                fono1 += cliente.getFon_num_1();
            ped2.setTelefono(fono1);
            String fono2 = "";
            if (cliente.getFon_cod_2() != null)
                fono2 = cliente.getFon_cod_2() + " ";
            if (cliente.getFon_num_2() != null)
                fono2 += cliente.getFon_num_2();
            ped2.setTelefono2(fono2);

            ped2.setCosto_despacho(pedido.getCosto_desp());

            //ped2.setFingreso(); ponerla en el dao
            ped2.setMonto(monto_total);
            ped2.setIndicacion(dir1.getComentarios());

            ped2.setMedio_pago(pedido.getMedio_pago());

            // Encriptamos el número de tarjeta
            /*
             * Ya viene encriptada desde el FOVE ResourceBundle rb =
             * ResourceBundle.getBundle("bo"); String key =
             * rb.getString("conf.bo.key");
             * 
             * if(pedido.getNum_mp()!=null){ num_tc = Cifrador.encriptar( key,
             * pedido.getNum_mp() ); }
             */

            //            ped2.setNum_mp(pedido.getNum_mp());
            //
            //            ped2.setFecha_exp(pedido.getFecha_exp());
            //            ped2.setN_cuotas((int) pedido.getN_cuotas());
            //
            //            ped2.setNom_tbancaria(pedido.getNom_tbancaria());
            //            if (pedido.getTb_banco() != null) {
            //                ped2.setTb_banco(pedido.getTb_banco());
            //            } else {
            //                ped2.setTb_banco("");
            //            }
            ped2.setTipo_doc(pedido.getTipo_doc());

            ped2.setCant_prods((long) cant_prods);

            ped2.setSin_gente_op((int) pedido.getSin_gente_op());
            ped2.setSin_gente_txt(pedido.getSin_gente_txt());

            ped2.setDir_id(pedido.getDir_id());
            ped2.setDir_tipo_calle(dir1.getNom_tip_calle());
            ped2.setDir_calle(dir1.getCalle());
            ped2.setDir_numero(dir1.getNumero());
            ped2.setDir_depto(dir1.getDepto());

            ped2.setPol_id(pedido.getPol_id());
            ped2.setPol_sustitucion(pedido.getPol_sustitucion());

            ped2.setObservacion(pedido.getObservacion());

            //campos propios VE
            ped2.setOrigen(pedido.getOrigen());

            ped2.setTipo_ve(pedido.getTipo_ve());

            ped2.setId_cotizacion(pedido.getId_cotizacion());

            ped2.setId_local_fact(pedido.getId_local_fact());

            // 5. Inserta encabezado del pedido
            id_pedido = dao1.doGeneraPedido(ped2);

            // 6. Preparamos dto detalle de pedido
            for (int i = 0; i < lst_prods.size(); i++) {

                // Dto generado en el FO
                ProcInsPedidoDetalleFODTO det1 = new ProcInsPedidoDetalleFODTO();
                det1 = (ProcInsPedidoDetalleFODTO) lst_prods.get(i);

                // Dto que se pasará al dao
                ProductosPedidoDTO det2 = new ProductosPedidoDTO();

                // Obtenemos información del producto
                ProductoEntity prod1 = dao1.getProductoPedidoByIdProdFO(det1.getId_producto_fo());

                det2.setId_pedido(id_pedido);
                det2.setId_producto(prod1.getId_bo().longValue());
                //det2.setId_sector ( ); /* Este campo se llena al Validar la
                // OP*/
                det2.setCod_producto(prod1.getCod_sap());
                det2.setUnid_medida(prod1.getTipre());

                det2.setDescripcion(prod1.getTipo() + " " + prod1.getNom_marca() + " " + prod1.getDesc_corta());

                det2.setCant_solic(det1.getCant_solic());
                det2.setPrecio(det1.getPrecio_unitario());

                det2.setCant_spick(det1.getCant_solic()); // Inicialmente, es igual a la cant_solic

                det2.setPesable(prod1.getEs_pesable());
                det2.setPreparable(prod1.getEs_prep());

                if (det1.getObservacion() == null || !det1.getObservacion().equals(""))
                    det2.setCon_nota("N");
                else
                    det2.setCon_nota("S");

                if (det1.getObservacion() != null)
                    det2.setObservacion(det1.getObservacion());
                else
                    det2.setObservacion(Constantes.CADENA_VACIA);

                det2.setDscto_item(det1.getDscto_item());

                det2.setPrecio_lista(det1.getPrecio_lista());

                // 7. Agrega detalle
                dao1.generaProductoPedido(det2);

            }

            // 8. Actualiza pedido con totales, por seguridad... y el monto reservado
            dao1.recalcPedido(id_pedido, pedido.getTipo_doc());

            // 9. Reserva capacidad de despacho solo si no es un pedido especial
            // (ex - spot)
            logger.debug("Tipo ve:" + pedido.getTipo_ve());
            if (pedido.getTipo_ve().equals(Constantes.TIPO_VE_SPECIAL_CTE)) {
                logger.debug("El pedido es ESPECIAL y NO RESERVA capacidades de despacho y picking");
            } else {
                logger.debug("El pedido es NORMAL y SI reserva capacidades de despacho y picking");
                /*
                 * se incrementa en 1, pues se mide el número de pedidos de la
                 * jornada de despacho
                 */
                dao2.doOcupaCapacidadDespacho(pedido.getId_jdespacho(), 1);
                // 10. toma capacidad de picking de la jornada de picking
                // relacionada
                dao2.doOcupaCapacidadPicking(jor1.getId_jpicking(), (int) cant_prods);
                                                
                //Agregamos datos al log del pedido creado	            
                try{
    	            LogPedidoDTO logRetiro = new LogPedidoDTO();
    	            logRetiro.setUsuario("SYSTEM");
    	            logRetiro.setId_pedido(id_pedido);	   
    	            
    	            String tDesp="N";
    	            
    	            if(ped2.getTipo_despacho() != null ){
    	            	tDesp=ped2.getTipo_despacho();
    	            }
    	            
    	            logRetiro.setLog("[TIPO_DESPACHO] Tipo despacho creado por BOC es "+ tDesp +" valor "+ped2.getCosto_despacho());                
    	            dao1.addLogPedido(logRetiro);
    	            
    	            LogPedidoDTO logDespacho = new LogPedidoDTO();
    	            logDespacho.setUsuario("SYSTEM");
    	            logDespacho.setId_pedido(id_pedido);	                                   
    	            logDespacho.setLog("[ID_DESPACHO] Jornada despacho asigna por BOC es "+pedido.getId_jdespacho());                
    	            dao1.addLogPedido(logDespacho);
    	            
    	            LogPedidoDTO logPicking = new LogPedidoDTO();
    	            logPicking.setUsuario("SYSTEM");
    	            logPicking.setId_pedido(id_pedido);	                                   
    	            logPicking.setLog("[ID_JPICKING] Jornada picking asigna por BOC es "+jor1.getId_jpicking()+" capacida ocupada:"+cant_prods);                
    	            dao1.addLogPedido(logPicking);
                } catch (Exception e) {logger.info("ERROR AL LOGEAR DATOS DEL PEDIDO CREADO OP:"+id_pedido);}
                //FIn datos log del pedido                
                
            }

            // 11. Inserta datos de la factura

            logger.debug("Tipo Doc: ->" + pedido.getTipo_doc() + "<-");

            if (pedido.getTipo_doc().equals("F")) {

                ProcModFacturaDTO fac1 = new ProcModFacturaDTO();

                fac1.setTipo_doc("F");
                fac1.setId_pedido(id_pedido);
                fac1.setRazon(pedido.getFac_razon());
                fac1.setRut(pedido.getFac_rut());
                fac1.setDv(pedido.getFac_dv());
                fac1.setDireccion(pedido.getFac_direccion());
                fac1.setTelefono(pedido.getFac_fono());
                fac1.setGiro(pedido.getFac_giro());
                fac1.setComuna(pedido.getFac_comuna());
                fac1.setCiudad(pedido.getFac_ciudad());

                dao1.setModFactura(fac1);

            }

            //cambiar el estado de la cotizacion
            boolean cambio = dao1.setCambiarCotizacion(pedido.getId_cotizacion(),
                    Constantes.ID_EST_COTIZACION_EN_REALIZACION);
            logger.debug("cambio estado en cotizacion?" + cambio);

            // Finaliza la transacción
            trx.end();

        } catch (PedidosDAOException e) {
            try {
                trx.rollback();
            } catch (DAOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new SystemException(e);
        } catch (JornadasDAOException e) {
            try {
                trx.rollback();
            } catch (DAOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new SystemException(e);
        } catch (ClientesDAOException e) {
            try {
                trx.rollback();
            } catch (DAOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new SystemException(e);
        } catch (DAOException e) {
            try {
                trx.rollback();
            } catch (DAOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new SystemException(e);
        } catch (Exception e) {
            try {
                trx.rollback();
            } catch (DAOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new SystemException(e);
        }

        // 11. Invoca procedimiento Valida la OP (este procedimiento debe quedar fuera de la transacción)
        try {
            //se cambio a ValidarOP, la cual verifica si existen alertas o no (sean activas o informativas)
            this.setValidaOP(id_pedido);

        } catch (Exception e) {
            logger.info("El proceso de validación falló... OP queda en estado Ingresada");
        }

        if (!Constantes.MEDIO_PAGO_TBK.equalsIgnoreCase(pedido.getMedio_pago()) && !Constantes.MEDIO_PAGO_CAT.equalsIgnoreCase(pedido.getMedio_pago())) {
            ingresarPedidoVteASistema(id_pedido);
        }

        /* Finalmente, Retornamos id_pedido */
        return id_pedido;
    }

    /**
     *  
     */
    public void ingresarPedidoVteASistema(long idPedido) throws SystemException, PedidosException {
        try {
            /*
             * Cambiar estado
             */
            JdbcPedidosDAO dao3 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

            // Creamos la transacción
            JdbcTransaccion trx2 = new JdbcTransaccion();

            // Iniciamos la transacción
            trx2.begin();

            // Asignamos la transacción a los dao's
            dao3.setTrx(trx2);

            List lst_ale_final = dao3.getAlertasPedido(idPedido);
            long nvo_estado = 0;
            String mnsLog = "";
            if (lst_ale_final.size() == 0) {
                nvo_estado = Constantes.ID_ESTAD_PEDIDO_VALIDADO; //Estado
                // Validado
                mnsLog = "El Pedido se encuentra en estado : Validado";
            } else {
                nvo_estado = Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION; //Estado
                // En
                // Validación
                mnsLog = "El Pedido se encuentra en estado : En Validación";
            }
            dao3.setModEstadoPedido(idPedido, nvo_estado);
            logger.debug("mnsLog:" + mnsLog);

            LogPedidoDTO log = new LogPedidoDTO();
            log.setId_pedido(idPedido);
            log.setUsuario("SYSTEM");
            log.setLog(mnsLog);
            dao3.addLogPedido(log);

            trx2.end();

        } catch (Exception e) {
            logger.info("El proceso de validación falló... OP queda en estado Ingresada");
        }
    }

    /**
     * Obtiene un listado de transacciones por criterio: - local de facturacion
     * (obligatorio) - id_pedido (opcional)
     * 
     * @param criterios
     * @return List
     * @throws PedidosException
     */
    public List getTrxMpByCriteria(TrxMpCriteriaDTO criterios) throws PedidosException {

        List result = new ArrayList();
        JdbcTrxMedioPagoDAO dao = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        try {
            result = dao.getTrxMpByCriteria(criterios);
        } catch (TrxMedioPagoDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return result;
    }

    /**
     * Obtiene el total de registros de transacciones de pago segun local
     * facturador. En base a criterios de busqueda y paginacion.
     * 
     * @param TrxMpCriteriaDTO
     *            criterios
     * @return List MonitorTrxMpDTO
     * @throws ServiceException
     * @see cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO
     */
    public long getCountTrxMpByCriteria(TrxMpCriteriaDTO criterio) throws PedidosException {

        JdbcTrxMedioPagoDAO dao = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        try {
            return dao.getCountTrxMpByCriteria(criterio);
        } catch (TrxMedioPagoDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * Obtiene listado de estados de pedido
     * 
     * @return List EstadosDTO
     * @throws PedidosException,
     *             en el caso que exista error en la consulta en base de datos.
     *  
     */
    public List getEstadosTrxMp() throws PedidosException {

        List lista = new ArrayList();

        JdbcTrxMedioPagoDAO dao = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();

        try {
            //retorna EstadosDTO
            lista = dao.getEstadosTrxMp();

        } catch (TrxMedioPagoDAOException e) {
            e.printStackTrace();
            throw new PedidosException(e);
        }

        return lista;
    }

    /**
     * Permite aplicar recalculo de promociones
     * @param lst_promo listado de promociones
     * @param lst_dp listado de detalle pedido
     * @param id_pedido
     * @throws PedidosException
     * @throws SystemException
     */
    public void setAplicaRecalculo(List lst_promo, List lst_dp, long id_pedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        // Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();
        // Iniciamos trx
        try {
            trx1.begin();
        } catch (Exception e1) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        }

        // Marcamos los dao's con la transacción
        try {
            dao.setTrx(trx1);
        } catch (PedidosDAOException e2) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos");
        }
        try {
            if (id_pedido > 0) {
                PedidoDTO ped = dao.getPedidoById(id_pedido);
                // eliminamos todas las promociones del pedido de la tabla promos_detped
                dao.doEliminaPromoDPPedido(id_pedido);
                logger.debug("El id_pedido es : " + id_pedido);

                // Recorremos el listado de promociones y las insertamos en la tabla promos_detped
                for (int i = 0; lst_promo != null && i < lst_promo.size(); i++) {
                    PromoDetallePedidoDTO promodp = (PromoDetallePedidoDTO) lst_promo.get(i);
                    logger.debug("ID DETALLE: " + promodp.getId_detalle());
                    logger.debug("ID PROMO: " + promodp.getId_promocion());
                    dao.addPromoDetallePedido(promodp);
                }
                logger.debug("Tamaño lista promociones: " + lst_promo.size());

                //sumarizacion de descuentos
                String lst_idet = "";
                String id_det = "";
                ArrayList lst_detped = new ArrayList();
                for (int i = 0; lst_dp != null && i < lst_dp.size(); i++) {
                    DetallePedidoDTO dp1 = (DetallePedidoDTO) lst_dp.get(i);
                    DetallePedidoDTO detPed = new DetallePedidoDTO();
                    double precio = 0;
                    double tasa_descto = 0;
                    double descto = 0.0;
                    for (int j = 0; lst_dp != null && j < lst_dp.size(); j++) {
                        DetallePedidoDTO dp2 = (DetallePedidoDTO) lst_dp.get(j);
                        if (dp1.getId_detalle() == dp2.getId_detalle()) {
                            descto += dp2.getDesc_pesos_item();
                            tasa_descto += dp2.getDscto_item();
                        }

                    }
                    precio = dp1.getPrecio_lista() - descto;
                    if (precio < 0)
                        precio = 0;
                    detPed.setId_detalle(dp1.getId_detalle());
                    detPed.setPrecio(precio);
                    detPed.setDscto_item(tasa_descto);
                    logger.debug("DESCUENTO FINAL: " + descto);
                    logger.debug("TASA DESCUENTO FINAL" + tasa_descto);
                    logger.debug("PRECIO FINAL: " + precio);
                    logger.debug("ID DETALLE: " + dp1.getId_detalle());

                    id_det = String.valueOf(dp1.getId_detalle());
                    logger.debug("LISTA DE ID DETALLE: " + lst_idet);
                    logger.debug("ID DET: " + id_det);
                    logger.debug("lst_idet.indexOf(" + id_det + ") " + lst_idet.indexOf(id_det));
                    if (lst_idet.indexOf(id_det) == -1) {
                        lst_idet += " " + dp1.getId_detalle();
                        lst_detped.add(detPed);
                        logger.debug(" AGREGAMOS DETALLE DE PEDIDO ID_DETALLE " + id_det);
                    } else {
                        logger.debug("YA EXISTE ID_DETALLE " + id_det + " EN LA LISTA ");
                    }

                }

                // Recorremos el listado de detalle de pedido para modificar el desucento por producto

                for (int i = 0; lst_detped != null && i < lst_detped.size(); i++) {
                    DetallePedidoDTO dp = (DetallePedidoDTO) lst_detped.get(i);

                    dao.doModificaDescuentoDetallePedido(dp);
                }
                logger.debug("Tamaño lista detalle pedido: " + lst_dp.size());

                //TODO: probar la aplicacion de recalculo sobre productos que antes tenian promocion y ahora no, deben quedar con precio_lista
                //TODO: probar la aplicacion de recalculo sobre productos que antes tenian promocion y ahora no, deben quedar con precio_lista
                //TODO: probar la aplicacion de recalculo sobre productos que antes tenian promocion y ahora no, deben quedar con precio_lista

                //condicion de borde
                //se deben revisar los productos que tengan precio_lista!=precio y que no tienen promocion asociada
                //y deben quedar los precios=precio_lista

                //ProductosPedidoDTO
                logger.debug("Inicio de limpieza precios de productos sin promocion");
                List lst_detalles = dao.getDetallesPedido(id_pedido);
                for (int i = 0; i < lst_detalles.size(); i++) {
                    ProductosPedidoDTO prod = (ProductosPedidoDTO) lst_detalles.get(i);
                    logger.debug("id_detalle:" + prod.getId_detalle() 
                    		+ " precio:" + prod.getPrecio() 
                    		+ " precio lista:" + prod.getPrecio_lista());
                    boolean det_encontrado = false;
                    for (int j = 0; j < lst_promo.size(); j++) {
                        PromoDetallePedidoDTO promodp = (PromoDetallePedidoDTO) lst_promo.get(j);
                        if (promodp.getId_detalle() == prod.getId_detalle()) {
                            //logger.debug("id_detalle = promo_dp.id_detalle =>encontrado! el producto es parte de una promocion");
                            det_encontrado = true;
                            break;
                        }
                    }

                    if (!det_encontrado) {
                        if (prod.getPrecio_lista() != prod.getPrecio()) {
                            logger.debug("Se restituye el precio del producto id_detalle:" + prod.getId_detalle());
                            logger.debug("precio lista:" + prod.getPrecio_lista());
                            logger.debug("precio actual:" + prod.getPrecio());
                            DetallePedidoDTO mod_prod = new DetallePedidoDTO();
                            mod_prod.setId_detalle(prod.getId_detalle());
                            mod_prod.setDscto_item(0.0);
                            mod_prod.setPrecio(prod.getPrecio_lista());
                            logger.debug("precio final:" + mod_prod.getPrecio());
                            logger.debug("descuento:0.0:" + mod_prod.getDscto_item());
                            dao.doModificaDescuentoDetallePedido(mod_prod);
                		}
                		else{
                            logger.debug("Los precios del producto son identicos.");
                        }

                    }

                }
                logger.debug("Fin de limpieza precios de productos sin promocion");

                
                // Se deben limpiar los flags de recalculo por eliminacion o mod. de medio de pago
                //saca los flags de recalculo del pedido
                ped.setFlg_recalc_prod(false);
                ped.setFlg_recalc_mp(false);
                dao.updFlagRecalculoPedido(ped);
                logger.debug("Flags de recalculo eliminados del pedido");

            }

        } catch (PedidosDAOException ex) {
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE);
            }
            throw new SystemException("Error no controlado ", ex);
        }
        //         cerramos trx
        try {
            trx1.end();
        } catch (DAOException e) {
            logger.error("Error al finalizar transacción");
            throw new SystemException("Error al finalizar transacción");
        }

        //    return result;
    }

    /**
     * Recalcula las promociones para un pedido en un local.
     * Entrega un DTO de resumen con el listado de productos relacionados agrupados por id_detalle con el descuento 
     * @param id_pedido
     * @param id_local_bo
     * @return List ResumenPedidoPromocionDTO
     * @throws PromocionesException
     * @throws SystemException
     */
    //20121114avc
    public List doRecalculoPromocion(long id_pedido, long id_local_bo, JdbcTransaccion trx1, Long colaborador, CuponDsctoDTO cddto, List cuponProds) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        List result = new ArrayList();
        //OJO hay que definir el canal en las constantes
        int id_canal;
        char flag_cant = Constantes.PROMO_RECALCULO_FLAG_PROD_CANTIDAD; //Flag producto cantidad 'C', o producto 'P' Pesable
        char flag_venta = Constantes.PROMO_RECALCULO_FLAG_VENTA;// Venta o anulacion
        int fpago = 0;
        int cuotas = 0;

        logger.debug("inicia doRecalculoPromocion");

        if (trx1 != null) {
            logger.debug("necesita transaccionalidad");
            //         Marcamos los dao's con la transacción
            try {
                dao.setTrx(trx1);
            } catch (PedidosDAOException e2) {
                logger.error("Error al asignar transacción al dao Pedidos");
                throw new SystemException("Error al asignar transacción al dao Pedidos");
            }
        } else {
            logger.debug("no necesita transaccionalidad");
        }

        try {
            //define el canal segun constantes
            id_canal = Constantes.CANAL_PROMOCIONES;

            //    se inicializa ambiente de libreria promociones
            
            //20121114avc
            PromoCtrl lib = new PromoCtrl((int) id_local_bo, id_canal, colaborador);

            //    se setea el listado de tcp (al menos 1 debe ser seteado)
            List TCP = new ArrayList();
            TcpClienteDTO tcpdto = null;
            /** el primer tcp vacio, se usan 2000 para que siempre entregue todos los productos
            * la cantidad es el numero que limita la aplicacion de prorrateos a n productos siendo
            * n el numero - ESTO ES TEMPORAL, A FUTURO NO DEBERIA EXISTIR
             */

            //rescatar tcp y cupones de las tablas
            logger.debug("Antes de obtener cupones");
            List lst_cupon_dao = dao.getCuponesPedidoByIdPedido(id_pedido);
            logger.debug("despues de obtener cupones y antes de obterne TCPs");
            List lst_tcp_dao = dao.getTcpPedidosByIdPedido(id_pedido);
            logger.debug("despues de obtener TCPs");
            //genera listados de tcps, y busca cupones en caso de que existan revisa cual es su tcp y le inserta con cupon
            if ((lst_tcp_dao == null) || (lst_tcp_dao.size() == 0)) {
                tcpdto = new TcpClienteDTO(0, "", 2000, 0);
                TCP.add(tcpdto);
            }
            else{
                for (int i = 0; i < lst_tcp_dao.size(); i++) {
                    TcpPedidoDTO tcp_dao = (TcpPedidoDTO) lst_tcp_dao.get(i);
                    //consultar cupon y verificar el id_tcp si es identico se debe incluir el numero de cupon
                    String cupon = "";
                    for (int j = 0; j < lst_cupon_dao.size(); j++) {
                        CuponPedidoDTO cup_dao = (CuponPedidoDTO) lst_cupon_dao.get(j);
                        if (cup_dao.getId_tcp() == tcp_dao.getId_tcp()) {
                            cupon = cup_dao.getNro_cupon();
                        }
                    }
                    tcpdto = new TcpClienteDTO(tcp_dao.getNro_tcp(), cupon, tcp_dao.getCant_max(), 0);
                    logger.debug("TCP " + i + ": (numero=" + tcpdto.getTcp() + ", cupon" + tcpdto.getCupon() + ", max:" + tcpdto.getMax() + ", used:" + tcpdto.getCant() + ")");
                    TCP.add(tcpdto);
                }
            }

            /*
            try{
                ClienteTcpPromosCupones servidor = new ClienteTcpPromosCupones();
                servidor.setHost(Constantes.PROMO_SERVER_HOST);
                servidor.setPuerto(Constantes.PROMO_SERVER_PORT);    
                
                // Consultar tcps según el rut sin dv del cliente                
                RespR1DTO cons_tcp=servidor.ConsultaCuponesPorRut(123782763,56);
                if (Integer.parseInt(cons_tcp.getCod_ret())!=0){
                    logger.warn("No hay respuesta o TCPS");
                    logger.warn("cod_ret="+cons_tcp.getCod_ret());
                    logger.warn("glosa1="+cons_tcp.getGlosa1());
                    logger.warn("glosa2="+cons_tcp.getGlosa2());
                }
                else{
                    List socket_tcps = new ArrayList();
                    socket_tcps = cons_tcp.getTcps();
                    for (int i=0;i<socket_tcps.size();i++){
                        TcpDTO socktcp = (TcpDTO) socket_tcps.get(i);
                        tcpdto = new TcpClienteDTO(Integer.parseInt(socktcp.getNro_tcp()),
                                "",
                                socktcp.getCantidad(),
                                0);
                        TCP.add(tcpdto);            
                    }
                }
                // Consultar tcps según el rut sin dv del cliente y resolver tcps
                
                
            }catch(Exception ex){
                logger.warn("No puede contactar al servidor de promociones:"+ex.getMessage());                
            }
            
             */

            //recoge listado de productos del pedido
            List lst_detped = dao.getProductosByPedido(id_pedido);
            logger.debug("obtiene detalle del pedido:" + lst_detped.size());

            for (int i = 0; i < lst_detped.size(); i++) {
                DetPedidoDTO prod1 = (DetPedidoDTO) lst_detped.get(i);

                PrioridadPromosDTO codigos_promo = (PrioridadPromosDTO) dao.getPromosPrioridadProducto(prod1.getId_producto(), (int) id_local_bo);

                //    genera un nuevo reigstro para el calculo
                ProductoDTO pro = new ProductoDTO();

                //busca las promociones asociadas por prioridad promocional a cada producto
                List promo = new ArrayList();

                
                
                /* Esto no aplica , ya que si la promocion es de tipo seccion debe continuar igual
                 * 
                 * si no hay promociones o son todas 0 sigue el ciclo ya que no hay promociones a calcular
                if ((codigos_promo==null)
                || ((codigos_promo.getCodPromoEvento()+codigos_promo.getCodPromoNormal()+codigos_promo.getCodPromoPeriodica())==0)){
                    logger.debug(i+".-detalle del pedido : NO tiene promociones");
                    continue;
                }*/                

                logger.debug(i + ".-detalle del pedido : tiene promociones "+ 
                		codigos_promo.getCodPromoEvento() + " ," + codigos_promo.getCodPromoPeriodica() + " ," + codigos_promo.getCodPromoNormal());

                //si existen promociones las adjunta al registro de productos
                if (codigos_promo.getCodPromoEvento() > 0) {
                    promo.add(String.valueOf(codigos_promo.getCodPromoEvento()));
                    logger.debug("Promocion:" + codigos_promo.getCodPromoEvento());
                }
                if (codigos_promo.getCodPromoPeriodica() > 0) {
                    promo.add(String.valueOf(codigos_promo.getCodPromoPeriodica()));
                    logger.debug("Promocion:" + codigos_promo.getCodPromoPeriodica());
                }
                if (codigos_promo.getCodPromoNormal() > 0) {
                    promo.add(String.valueOf(codigos_promo.getCodPromoNormal()));
                    logger.debug("Promocion:" + codigos_promo.getCodPromoNormal());
                }

                //ingresa promociones del producto
                //logger.debug("total promos:"+promo.size());
                pro.setPromocion(promo);

                //codigo de barra
                long ean13long = Long.parseLong(prod1.getCod_barra());
                pro.setCodigo(ean13long);

                // Que va en departamento la seccion de la categoria SAP
                pro.setDepto(Integer.parseInt(prod1.getSeccion_sap()));
                
// inicio cdd
                pro.setRubro(prod1.getRubro());
// fin cdd
                
                //pro.setDepto(99);
                // La cantidad va multiplicada por 1000 si es pesable
                int cant_solicitada = 0;

                // flag pesable si es SI => 'P'=Pesable o 'C'=Cantidad
                if ((prod1.getPesable() != null) && (prod1.getPesable().equals(Constantes.INDICADOR_SI))) {
                    cant_solicitada = (int) Math.round(prod1.getCant_solicitada() * 1000);
                    flag_cant = Constantes.PROMO_RECALCULO_FLAG_PROD_PESABLE;
                } else {
                    cant_solicitada = (int) Math.round(prod1.getCant_solicitada());
                    flag_cant = Constantes.PROMO_RECALCULO_FLAG_PROD_CANTIDAD;
                }
                //logger.debug("prod1.getCant_solicitada() -> : " +cant_solicitada);
                pro.setCantidad(cant_solicitada);
                //pro.setCantidad(6);

                //Flag producto cantidad = C o pesable = P
                pro.setFlagCantidad(flag_cant);

                //Flag Venta=V Anulacion=A
                pro.setFlagVenta(flag_venta);

                //Precio del Producto
                pro.setPrecio(Math.round(prod1.getCant_solicitada() * prod1.getPrecio_lista()));

                logger.debug("inserta producto :  " + prod1.getId_producto() + 
                		" barra=" + pro.getCodigo() + 
                		" depto=" + pro.getDepto() + 
                		" cantidad=" + pro.getCantidad() + 
                		" flag_cantidad=" + pro.getFlagCantidad() + 
                		" flag_venta=" + pro.getFlagVenta() + 
                		" precio_lista=" + pro.getPrecio() + 
                		" monto p(" + prod1.getPrecio_lista() + ")x q(" + prod1.getCant_solicitada() + ")=" 
                			+ Math.round(prod1.getCant_solicitada() * prod1.getPrecio_lista()) + 
               			" promo=" + pro.getPromocion());

                //se insertan productos a la libreria
                lib.insertaProducto(pro);
            }
            logger.debug("productos insertados");

            /*
             *  p r u e b a   e  n  d u r o INICIO 
             *  
            - despues calculo descuentos
            - -----------------
            - DESCUENTO = 5825
            - PRORR PRO= 8
            - PRORR SEC= 0
            - -----------------
            - PROMO=1    BBR PROMCN   1    dcto=845  
            - PROMO=1    BBR PROMCN   1    dcto=845      || ean13:780460346037-precio:990-prorrateo:495|| ean13:208200017968-precio:700-prorrateo:350
            - PROMO=2    BBR PROMCN   2    dcto=1200  
            - PROMO=2    BBR PROMCN   2    dcto=1200      || ean13:780490500133-precio:1500-prorrateo:1200
            - PROMO=3    BBR PROMCN   3    dcto=750  
            - PROMO=3    BBR PROMCN   3    dcto=750      || ean13:780583610733-precio:750-prorrateo:375|| ean13:780583610733-precio:750-prorrateo:375
            - PROMO=11    BBR PROMCN  11    dcto=500  
            - PROMO=11    BBR PROMCN  11    dcto=500      || ean13:780495000194-precio:500-prorrateo:250|| ean13:780495000194-precio:500-prorrateo:250
            - PROMO=12    BBR PROMCN  12    dcto=510  
            - PROMO=12    BBR PROMCN  12    dcto=510      || ean13:780650022201-precio:750-prorrateo:255|| ean13:780650022201-precio:750-prorrateo:255
            - PROMO=1020    BBR PROMCN 102    dcto=400  
            - PROMO=1020    BBR PROMCN 102    dcto=400  || ean13:780614704992-precio:900-prorrateo:211|| ean13:780614704997-precio:800-prorrateo:189
            - PROMO=1050    BBR PROMCN 105    dcto=510  
            - PROMO=1050    BBR PROMCN 105    dcto=510  || ean13:780650599159-precio:700-prorrateo:238|| ean13:780650597749-precio:800-prorrateo:272
            - PROMO=2050    BBR PROMCN 205    dcto=1110  
            - PROMO=2050    BBR PROMCN 205    dcto=1110 || ean13:780161022304-precio:700-prorrateo:370|| ean13:780161002941-precio:700-prorrateo:370|| ean13:780161002296-precio:700-prorrateo:370
            - termina debug resultados
             *  
             */

            //    se calcula promociones
            //Rescatamos el medio de pago del pedido
            PedidoDTO ped = dao.getPedidoById(id_pedido);

            //** PROMOCIONES ** Esto hay que hacerlo de nuevo y sacar el dato de las nuevas tablas... ademas ver donde mas lo hace asi. 
            String ped_mp = "";
            ped_mp = ped.getMedio_pago();
            cuotas = ped.getN_cuotas();

			//[20121114avc            
            logger.debug("antes calculo descuentos fpago=" + fpago + " cuotas=" + cuotas);
            
//inicio cdd
            long dcto = lib.calculaDescuentosNew(ped_mp, cuotas, TCP, cddto, cuponProds);
// fin cdd
            
            //]20121114avc
            logger.debug("despues calculo descuentos");
            // se obtiene el listado de productos prorrateados
            List p_prod = lib.getProrrateoProducto();
            // se obtiene el listaod de secciones prorrateadas
            List p_sec = lib.getProrrateoSeccion();
            // se obtiene el listado de tcps utilizados
            List p_tcps = lib.getListadoTcp();
            logger.debug("-----------------");
            logger.debug("DESCUENTO = " + dcto);
            logger.debug("PRORR PRO= " + p_prod.size());
            logger.debug("PRORR SEC= " + p_sec.size());
            logger.debug("PRORR TCP= " + p_tcps.size());
            logger.debug("-----------------");

            logger.debug("RESULTADO PRODUCTOS=" + p_prod.size());
            String linea_debug;
            for (int i = 0; i < p_prod.size(); i++) {
                ProrrateoPromocionProductoDTO promoproducto = (ProrrateoPromocionProductoDTO) p_prod.get(i);

                linea_debug = "PROMO=" + promoproducto.getCodigo() 
                		+ "\t" + promoproducto.getDescripcion() 
                		+ "\tdcto=" + promoproducto.getDescuento() + "  ";
                List lst_prod_prorrat = promoproducto.getListadoProductos();
                List prod_relacionados = new ArrayList();
                //double suma_precio_lista=0.0;
                String str_prod_relacionados = "";
                logger.debug("lst_prod_prorrat= " + lst_prod_prorrat.size());
                for (int w = 0; w < lst_prod_prorrat.size(); w++) {
                    ProrrateoProductoDTO p = (ProrrateoProductoDTO) lst_prod_prorrat.get(w);
                    linea_debug += "|| ean13:" + p.getCodigo() + "-precio:" + p.getPrecio() + "-prorrateo:" + p.getProrrateo();

                    //busca el producto con prorrateo en el detalle pedido
                    long id_producto_promo = -1;
                    long id_detalle_pedido = -1;
                    String descr_prod_promo = "";
                    String ean13_promo = "";
                    double precio_lista = 0.0;
                    double cantidad = 0;
                    long prorrateo = 0;
                    for (int z = 0; z < lst_detped.size(); z++) {
                        //verifica informacion con los detalles del pedido
                        DetPedidoDTO prodped = (DetPedidoDTO) lst_detped.get(z);
                        //compara el codigo de barras con lo entregado al calculo
                        if (prodped.getCod_barra().equals(String.valueOf(p.getCodigo()))) {
                            id_producto_promo = prodped.getId_producto();
                            id_detalle_pedido = prodped.getId_detalle();
                            descr_prod_promo = prodped.getDesc_prod();
                            ean13_promo = prodped.getCod_barra();
                            // ** PROMOCIONES ** aca tb usa el precio lista sacado del detalle_pedido
                            precio_lista = prodped.getPrecio_lista();
                            cantidad = prodped.getCant_solicitada();
                            prorrateo = p.getProrrateo();
                            break;
                        }
                    }

                    //agrega el producto encontrado como producto relacionado
                    if (id_producto_promo > 0) {
                        //ojo que va el listado detallado, es posible que se necesite la agrupación
                        //por id_detalle al mostrar resultados

                        boolean det_found = false;
                        if (prod_relacionados.size() > 0) {
                            //logger.debug("Revisa en los relacionados existentes si existe "+id_detalle_pedido);
                            Iterator x;
                            x = prod_relacionados.iterator();
                            while (x.hasNext()) {
                                ProductosRelacionadosPromoDTO mco = (ProductosRelacionadosPromoDTO) x.next();
                                //si el producto existe lo sumariza
                                if (mco.getId_detalle() == id_detalle_pedido) {
                                    //agrega los prorrateos
                                    logger.debug("detalle:" + id_detalle_pedido + " prorrateo antes=" + mco.getProrrateo());
                                    mco.setProrrateo(mco.getProrrateo() + prorrateo);
                                    logger.debug("detalle:" + id_detalle_pedido + " suma prorrateo=" + mco.getProrrateo());
                                    det_found = true;
                                }
                            }
                        }
                        //si no lo encuentra lo agrega
                        if (!det_found) {
                            //A la espera de la sumarizacion con el resultado de secciones
                            ProductosRelacionadosPromoDTO prod = new ProductosRelacionadosPromoDTO();
                            prod.setId_producto(id_producto_promo);
                            prod.setId_detalle(id_detalle_pedido);
                            prod.setDescripcion(descr_prod_promo);
                            prod.setEan13(ean13_promo);
                            prod.setProrrateo(prorrateo);
                            prod.setPrecio_lista(precio_lista);
                            prod.setCantidad(cantidad);
                            logger.debug("producto relacionado nuevo =" + id_detalle_pedido + " " + descr_prod_promo);
                            prod_relacionados.add(prod);
                            //tambien sumariza las descripciones de productos para el resumen
                            if (w > 0)
                                str_prod_relacionados += ",<br>";
                            str_prod_relacionados += descr_prod_promo;
                        }
                    } else {
                        logger.warn("el producto ingresado al calculo no ha sido encontrado barra:" + p.getCodigo());
                    }
                }
                logger.debug(linea_debug);
                //busca datos de la proimocion
                
                //[20121114avc
                ResumenPedidoPromocionDTO res = new ResumenPedidoPromocionDTO();

                if (promoproducto.getTipo().equals("C")) {
                    //                    res.setId_promocion(promocion.getId_promocion());
                    res.setPromo_codigo(promoproducto.getCodigo());
                    res.setDesc_promo(promoproducto.getDescripcion());
                    //                    res.setFec_ini(promocion.getFini());
                    //                    res.setFec_fin(promocion.getFfin());
                    res.setMonto_descuento(promoproducto.getDescuento());
                    res.setDesc_prod(str_prod_relacionados);
                    res.setProd_relacionados(prod_relacionados);
                    //                    res.setTipo_promo(promocion.getTipo_promo());
                } else {
                    PromocionDTO promocion = dao
                            .getPromocionByCodigo(promoproducto.getCodigo());

                    //entrega resultados

                    res.setId_promocion(promocion.getId_promocion());
                    res.setPromo_codigo(promoproducto.getCodigo());
                    res.setDesc_promo(promoproducto.getDescripcion());
                    res.setFec_ini(promocion.getFini());
                    res.setFec_fin(promocion.getFfin());
                    res.setMonto_descuento(promoproducto.getDescuento());
                    res.setDesc_prod(str_prod_relacionados);
                    res.setProd_relacionados(prod_relacionados);
                    res.setTipo_promo(promocion.getTipo_promo());
                }
                result.add(res);
				//]20121114avc
            }
            logger.debug("RESULTADO SECCIONES=" + p_sec.size());
            for (int i = 0; i < p_sec.size(); i++) {
                ProrrateoPromocionSeccionDTO promosecc = (ProrrateoPromocionSeccionDTO) p_sec.get(i);
                linea_debug = "";
                linea_debug = "PROMO=" + promosecc.getCodigo() 
                	+ "|" + promosecc.getDescripcion() 
                	+ "|dcto=" + promosecc.getDescuento() + "  ";

                //listado productos relacionados
                List prod_relacionados = new ArrayList();
                String str_prod_relacionados = "";

                List lst_secciones = promosecc.getListadoSeccion();

                for (int w = 0; w < lst_secciones.size(); w++) {
                    ProrrateoSeccionDTO seccion = (ProrrateoSeccionDTO) lst_secciones.get(w);
                    linea_debug += "||" + seccion.getDepto() 
                    	+ "-" + seccion.getPrecio() 
                    	+ "-" + seccion.getProrrateo();

                    for (int k = 0; k < seccion.getListadoProductos().size(); k++) {
                        ProrrateoProductoSeccionDTO pps = 
                        	(ProrrateoProductoSeccionDTO) seccion.getListadoProductos().get(k);

                        linea_debug += "|| ean13=" + pps.getCodigo()
                                + "-depto:" + pps.getDepto() 
                                + "-precio:" + pps.getPrecio() 
                                + "-pr_producto:" + pps.getProrrateoProducto()
                                + "-(*)pr_seccion:" + pps.getProrrateoSeccion();

                        //                        busca el producto con prorrateo en el detalle pedido
                        long id_producto_promo = -1;
                        long id_detalle_pedido = -1;
                        String descr_prod_promo = "";
                        String ean13_promo = "";
                        double precio_lista = 0.0;
                        double cantidad = 0;
                        long prorrateo = 0;
                        for (int z = 0; z < lst_detped.size(); z++) {
                            //verifica informacion con los detalles del pedido
                            DetPedidoDTO prodped = (DetPedidoDTO) lst_detped.get(z);
                            //compara el codigo de barras con lo entregado al calculo
                            if (prodped.getCod_barra().equals(String.valueOf(pps.getCodigo()))) {
                                id_producto_promo = prodped.getId_producto();
                                id_detalle_pedido = prodped.getId_detalle();
                                descr_prod_promo = prodped.getDesc_prod();
                                ean13_promo = prodped.getCod_barra();
                                precio_lista = prodped.getPrecio_lista();
                                cantidad = prodped.getCant_solicitada();
                                prorrateo = pps.getProrrateoSeccion();
                                break;
                            }
                        }

                        //agrega el producto encontrado como producto relacionado
                        if (id_producto_promo > 0) {
                            //ojo que va el listado detallado, es posible que se necesite la agrupación
                            //por id_detalle al mostrar resultados

                            boolean det_found = false;
                            if (prod_relacionados.size() > 0) {
                                //logger.debug("Revisa en los relacionados existentes si existe "+id_detalle_pedido);
                                Iterator x;
                                x = prod_relacionados.iterator();
                                while (x.hasNext()) {
                                    ProductosRelacionadosPromoDTO mco = (ProductosRelacionadosPromoDTO) x.next();
                                    //si el producto existe lo sumariza
                                    if (mco.getId_detalle() == id_detalle_pedido) {
                                        //agrega los prorrateos
                                        logger.debug("detalle:" + id_detalle_pedido + " prorrateo antes=" + mco.getProrrateo());
                                        mco.setProrrateo(mco.getProrrateo()+ prorrateo);
                                        logger.debug("detalle:" + id_detalle_pedido + " suma prorrateo=" + mco.getProrrateo());
                                        det_found = true;
                                    }
                                }
                            }
                            //si no lo encuentra lo agrega
                            //al agregar el producto de la seccion por primera vez
                            //se le agrega el prorrateo de promociones al producto
                            // ** PROMOCIONES ** verificar para que se usa 'prod_relacionados' y ademas ver si precio_lista puede venir con 0.0
                            if (!det_found) {
                                //TODO: A la espera de la sumarizacion con el resultado de secciones
                                ProductosRelacionadosPromoDTO prod = new ProductosRelacionadosPromoDTO();
                                prod.setId_producto(id_producto_promo);
                                prod.setId_detalle(id_detalle_pedido);
                                prod.setDescripcion(descr_prod_promo);
                                prod.setEan13(ean13_promo);
                                // el prorrateo al producto solo se agrega si el producto aparece la primera vez en la 
                                // lista para esta promocion
                                prod.setProrrateo(prorrateo);
                                prod.setPrecio_lista(precio_lista);
                                prod.setCantidad(cantidad);
                                logger.debug("producto relacionado nuevo =" + id_detalle_pedido + " " + descr_prod_promo);
                                prod_relacionados.add(prod);
                                //tambien sumariza las descripciones de productos para el resumen
                                if (w > 0)
                                    str_prod_relacionados += ",<br>";
                                str_prod_relacionados += descr_prod_promo;
                            }
                        } else {
                            logger.warn("el producto ingresado al calculo no ha sido encontrado barra:" + pps.getCodigo());
                        }
                    }
                }

                logger.debug(linea_debug);

                //busca datos de la promocion
                //                entrega resultados
                
                //[2012114avc
                ResumenPedidoPromocionDTO res = new ResumenPedidoPromocionDTO();
                if (promosecc.getTipo().equals("C")) {
                    res.setPromo_codigo(promosecc.getCodigo());
                    res.setDesc_promo(promosecc.getDescripcion());
                    res.setMonto_descuento(promosecc.getDescuento());
                    res.setDesc_prod(str_prod_relacionados);
                    res.setProd_relacionados(prod_relacionados);
                } else {
                    PromocionDTO promocion = dao.getPromocionByCodigo(promosecc.getCodigo());

                    res.setId_promocion(promocion.getId_promocion());
                    res.setPromo_codigo(promosecc.getCodigo());
                    res.setDesc_promo(promosecc.getDescripcion());
                    res.setFec_ini(promocion.getFini());
                    res.setFec_fin(promocion.getFfin());
                    res.setMonto_descuento(promosecc.getDescuento());

                    res.setDesc_prod(str_prod_relacionados);
                    res.setProd_relacionados(prod_relacionados);
                }
                result.add(res);
            }
			//]20121114avc
			
            logger.debug("RESULTADO TCPS=" + p_tcps.size());
            for (int i = 0; i < p_tcps.size(); i++) {
                TcpClienteDTO tcp = (TcpClienteDTO) p_tcps.get(i);
                logger.debug(i + " tcp:" + tcp.getTcp());
                logger.debug(i + " cupon:" + tcp.getCupon());
                logger.debug(i + " max:" + tcp.getMax());
                logger.debug(i + " cant:" + tcp.getCant());
                //realizar update x id_pedido y numero de tcp
                dao.updTcpCantUtil(id_pedido, tcp.getTcp(), tcp.getCant());
            }
            logger.debug("termina debug resultados");
            return result;
        } catch (PedidosDAOException ex) {
            logger.error("No puede obtener datos de promociones :" + ex);
            ex.printStackTrace();
            throw new PedidosException("Error en PedidosDAOException", ex);
        } catch (Exception e) {
            logger.error("Error en libreria de promociones:" + e.getMessage());
            e.printStackTrace();
            throw new PedidosException(e);
        }
    }

    /**
     * Obtiene los productos CON promoción de un pedido especifico
     * @param id_pedido
     * @return List PedidoPromocionDTO
     * @throws PedidosException
     * @throws SystemException
     */
    public List getPromocionPedidos(long id_pedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        List resultado = new ArrayList();
        try {
            List lst_resumen = dao.getResumenPromocionPedidos(id_pedido);
            for (int i = 0; i < lst_resumen.size(); i++) {
                ResumenPedidoPromocionDTO antiguo = (ResumenPedidoPromocionDTO) lst_resumen.get(i);
                ResumenPedidoPromocionDTO nuevo = antiguo;
                //concatena descripciones de productos
                String str_prod_relacionados = "";
                List lst_productos = dao.getProductosByPromocionPedido(antiguo.getId_promocion(), id_pedido);
                for (int j = 0; j < lst_productos.size(); j++) {
                    ProductosRelacionadosPromoDTO prod = (ProductosRelacionadosPromoDTO) lst_productos.get(j);
                    if (j > 0)
                        str_prod_relacionados += ",<br>";
                    str_prod_relacionados += prod.getDescripcion();
                }
                logger.debug("Productos relacionados promo:" + antiguo.getId_promocion() + " pedido:" + id_pedido + " = " + str_prod_relacionados);
                nuevo.setDesc_prod(str_prod_relacionados);

                nuevo.setProd_relacionados(lst_productos);

                //arma el nuevo resumen agregandole los productos por cada promocion
                resultado.add(nuevo);
            }
            return resultado;
        } catch (PedidosDAOException ex) {
            logger.warn("Problema :" + ex);
            throw new PedidosException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Obtiene un listado de promociones según el criterio de busqueda
     * Criterio puede ser el local
     * @param criteria
     * @return
     * @throws PedidosException
     * @throws SystemException
     */
    //FIXME Eliminar
    public List getPromocionesByCriteria(PromocionesCriteriaDTO criteria) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            return dao.getPromocionesByCriteria(criteria);
        } catch (PedidosDAOException ex) {
            logger.warn("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public List getPromociones() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPromociones();
        } catch (PedidosDAOException ex) {
            logger.warn("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public PromocionDTO getPromocion(int codigo) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPromocion(codigo);
        } catch (PedidosDAOException ex) {
            throw new SystemException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Obtiene el numero de registros para la paginación del listado del monitor de promociones  
     * @param criteria
     * @return
     * @throws PedidosException
     * @throws SystemException
     */
    public long getPromocionesByCriteriaCount(PromocionesCriteriaDTO criteria) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            return dao.getPromocionesByCriteriaCount(criteria);
        } catch (PedidosDAOException ex) {
            logger.warn("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Obtiene los datos de una promoción específica
     * @param id_promocion
     * @return
     * @throws PedidosException
     * @throws SystemException
     */
    public PromocionDTO getPromocionById(long id_promocion) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            return dao.getPromocionById(id_promocion);
        } catch (PedidosDAOException ex) {
            logger.warn("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }

    }

    /**
     * Obtiene un listado de Categorias SAP asociados al tipo de promocion seccion y
     * al local de la promocion. 
     * @param id_local
     * @param tipo
     * @return List CategoriaSapDTO
     * @throws PedidosException
     * @throws SystemException
     */
    public List getCategoriasSapByPromocionSeccion(long id_local, int tipo) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            return dao.getCategoriasSapByPromocionSeccion(id_local, tipo);
        } catch (PedidosDAOException ex) {
            logger.warn("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }

    }

    /**
     * Obtiene los productos de una promoción.
     * @param id_promocion
     * @return List ProductoPromocionDTO
     * @throws PedidosException
     * @throws SystemException
     */
    public List getPromocionProductos(int codigo) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        List result = new ArrayList();
        List lst_prod_promo_eventos = null;
        List lst_promo_periodico = null;
        List lst_promo_normal = null;
        try {
            //            obtiene productos promocion evento para el local de la promocion
            lst_prod_promo_eventos = dao.getPromocionProductosByTipo(codigo, Constantes.PROMO_TIPO_EVENTO);
            for (int i = 0; i < lst_prod_promo_eventos.size(); i++) {
                ProductoPromocionDTO prod = (ProductoPromocionDTO) lst_prod_promo_eventos.get(i);
                result.add(prod);
            }

            //            obtiene productos promocion periodica para el local de la promocion
            lst_promo_periodico = dao.getPromocionProductosByTipo(codigo, Constantes.PROMO_TIPO_PERIODICA);
            for (int i = 0; i < lst_promo_periodico.size(); i++) {
                ProductoPromocionDTO prod = (ProductoPromocionDTO) lst_promo_periodico.get(i);
                result.add(prod);
            }
            //            obtiene productos promocion normal para el local de la promocion
            lst_promo_normal = dao.getPromocionProductosByTipo(codigo, Constantes.PROMO_TIPO_NORMAL);
            for (int i = 0; i < lst_promo_normal.size(); i++) {
                ProductoPromocionDTO prod = (ProductoPromocionDTO) lst_promo_normal.get(i);
                result.add(prod);
            }

        } catch (PedidosDAOException ex) {
            logger.warn("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }
        return result;
    }

    /**
     * Permite modificar una promoción
     * @param dto
     * @return
     * @throws PedidosException
     * @throws SystemException
     */
    public boolean updPromocion(PromocionDTO dto) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            return dao.updPromocion(dto);
        } catch (PedidosDAOException ex) {
            logger.warn("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * Obtiene los medios de pago para promociones segun el medio de pado de jumbocl
     * @param mp_jmcl
     * @return
     * @throws PedidosException
     * @throws SystemException
     */
    public PromoMedioPagoDTO getMPPromoByMpJmcl(String mp_jmcl, int ncuotas) throws PedidosException, SystemException {
        PromoMedioPagoDTO mp_promo = null;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {
            mp_promo = dao.getPromoMedioPagoByMPJmcl(mp_jmcl, ncuotas);
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new SystemException(e);
        }

        return mp_promo;
    }

    /**
     * @param idPedido
     * @param sustitutosPorCategorias
     */
    public void updateCriterioSustitucionEnPedido(long idPedido, List sustitutosPorCategorias) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        System.out.println("\n\n*******************************************");
        try {
            for (int i = 0; i < sustitutosPorCategorias.size(); i++) {
                CriterioSustitutoDTO criterio = (CriterioSustitutoDTO) sustitutosPorCategorias.get(i);
                long idProdBo = dao.getIdProductoBOByIdProductoFO(criterio.getIdProducto());
                System.out.println("criterio.getIdProducto():" + criterio.getIdProducto());
                System.out.println("dao.updateCriterioSustitucionEnPedido(" + idPedido + ", " + idProdBo + ", " + criterio + ");");
                dao.updateCriterioSustitucionEnPedido(idPedido, idProdBo, criterio);
            }
        } catch (PedidosDAOException e) {
            e.printStackTrace();
            throw new SystemException(e);
        }
    }

    /**
     * @param idCliente
     * @param idProducto
     * @return
     */
    public SustitutoDTO getCriterioClientePorProducto(long idCliente, long idProducto) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getCriterioClientePorProducto(idCliente, idProducto);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idProducto
     * @param idCliente
     * @param idCriterio
     * @param descCriterio
     */
    public void addModCriterioCliente(long idProducto, long idCliente, long idCriterio, String descCriterio) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            if (dao.existeCriterio(idProducto, idCliente)) {
                dao.modCriterioCliente(idProducto, idCliente, idCriterio, descCriterio);
            } else {
                dao.addCriterioCliente(idProducto, idCliente, idCriterio, descCriterio);
            }
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idPedido
     * @param idRonda
     * @return
     */
    public List getProductosPedidoRonda(long idPedido, long idRonda) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getProductosPedidoRonda(idPedido, idRonda);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @return
     */
    public List getGruposListado() throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getGruposListado();
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @return
     */
    public List getTiposGruposListado() throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getTiposGruposListado();
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param lg
     * @return
     */
    public long addGrupoLista(ListaGrupoDTO lg) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.addGrupoLista(lg);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param lg
     */
    public void modGrupoLista(ListaGrupoDTO lg) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modGrupoLista(lg);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idObjeto
     * @return
     */
    public ListaGrupoDTO getGrupoListadoById(long idGrupoListado) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getGrupoListadoById(idGrupoListado);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idGrupoLista
     */
    public void delGrupoLista(long idGrupoLista) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delGrupoLista(idGrupoLista);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idGrupoLista
     * @return
     */
    public List clienteListasEspeciales(long idGrupoLista) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.clienteListasEspeciales(idGrupoLista);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param hash
     * @return
     */
    public String addListasEspeciales(Hashtable hash) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.addListasEspeciales(hash);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idLista
     * @return
     */
    public List getGruposAsociadosLista(long idLista) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getGruposAsociadosLista(idLista);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idLista
     * @return
     */
    public List getGruposNoAsociadosLista(long idLista) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getGruposNoAsociadosLista(idLista);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idLista
     * @return
     */
    public UltimasComprasDTO getListaById(long idLista) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getListaById(idLista);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param lista
     * @param grupos
     */
    public void modLista(UltimasComprasDTO lista, String[] grupos) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modLista(lista, grupos);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idLista
     * @param idGrupo
     */
    public void delListaEspecial(long idLista, long idGrupo) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delListaEspecial(idLista, idGrupo);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idLista
     * @return
     */
    public List listaDeProductosByLista(long idLista) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.listaDeProductosByLista(idLista);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idTipoGrupo
     * @return
     */
    public ListaTipoGrupoDTO getTipoGrupoById(int idTipoGrupo) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getTipoGrupoById(idTipoGrupo);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idTipoGrupo
     * @return
     */
    public List getGruposDeListasByTipo(int idTipoGrupo) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getGruposDeListasByTipo(idTipoGrupo);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @return
     */
    public List localesRetiro() throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.localesRetiro();
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public LocalDTO getLocalRetiro(long idLocal) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getLocalRetiro(idLocal);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idZona
     * @return
     */
    public boolean zonaEsRetiroLocal(long idZona) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.zonaEsRetiroLocal(idZona);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idPedido
     * @param tipoDespacho
     */
    public void modTipoDespachoDePedido(long idPedido, String tipoDespacho) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modTipoDespachoDePedido(idPedido, tipoDespacho);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idPedido
     * @param tipoPicking
     */
    public void modTipoPickingPedido(long idPedido, String tipoPicking) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modTipoPickingPedido(idPedido, tipoPicking);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @return
     */
    public Date fechaActualBD() throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.fechaActualBD();
        } catch (Exception e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param cliente_id
     * @return
     */
    public PedidoDTO getUltimaCompraClienteConDespacho(long cliente_id) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getUltimaCompraClienteConDespacho(cliente_id);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param cliente_id
     * @return
     */
    public PedidoDTO getUltimaCompraClienteConRetiro(long cliente_id) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getUltimaCompraClienteConRetiro(cliente_id);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public List getProductosSolicitadosById(long id_pedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getProductosSolicitadosById(id_pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idMarco
     * @return
     */
    public ListaTipoGrupoDTO getTipoGrupoListadoById(long idMarco) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getTipoGrupoListadoById(idMarco);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param marco
     * @return
     */
    public void addMarco(ListaTipoGrupoDTO marco) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addMarco(marco);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param marco
     * @return
     */
    public void modMarco(ListaTipoGrupoDTO marco) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modMarco(marco);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param marco
     * @return
     */
    public void delMarco(ListaTipoGrupoDTO marco) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delMarco(marco);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idMarco
     * @return
     */
    public List getGruposByMarco(int idMarco) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getGruposByMarco(idMarco);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getTiposGruposListadoActivos() throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getTiposGruposListadoActivos();
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public List getProductosCarruselPorCriterio(CriterioCarruselDTO criterio) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getProductosCarruselPorCriterio(criterio);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param prod
     */
    public long addEditProductoCarrusel(ProductoCarruselDTO prod) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.addEditProductoCarrusel(prod);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idProductoCarrusel
     * @return
     */
    public ProductoCarruselDTO getProductoCarruselById(long idProductoCarrusel) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getProductoCarruselById(idProductoCarrusel);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idProductoCarrusel
     */
    public void deleteProductoCarruselById(long idProductoCarrusel) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.deleteProductoCarruselById(idProductoCarrusel);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param usr
     * @param strLog
     */
    public void addLogCarrusel(UserDTO usr, String strLog) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addLogCarrusel(usr, strLog);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @return
     */
    public List getProductosCarruselActivos() throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getProductosCarruselActivos();
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountProductosCarruselPorCriterio(CriterioCarruselDTO criterio) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getCountProductosCarruselPorCriterio(criterio);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param fecha
     * @return
     */
    public List getLogCarruselByFecha(String fecha) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getLogCarruselByFecha(fecha);
        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
    }

    /**
     * @param idComuna
     * @return
     */
    public RegionDTO getRegionByComuna(long idComuna) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getRegionByComuna(idComuna);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getRegiones() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getRegiones();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idRegion
     * @return
     */
    public List getComunasByRegion(int idRegion) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getComunasByRegion(idRegion);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idComuna
     * @return
     */
    public List getZonasByComuna(int idComuna) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getZonasByComuna(idComuna);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param ped
     */
    public long addPedidoExt(PedidoDTO ped) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.addPedidoExt(ped);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param rut
     * @return
     */
    public PedidoDTO getUltimoPedidoJumboVAByRut(long rut) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getUltimoPedidoJumboVAByRut(rut);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idFono
     * @return
     */
    public FonoTransporteDTO getFonoTransporteById(long idFono) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getFonoTransporteById(idFono);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idChofer
     * @param idLocal
     * @return
     */
    public ChoferTransporteDTO getChoferTransporteById(long idChofer) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getChoferTransporteById(idChofer);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPatente
     * @param idLocal
     * @return
     */
    public PatenteTransporteDTO getPatenteTransporteById(long idPatente) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPatenteTransporteById(idPatente);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idEmpresaTransporte
     * @param idLocal
     * @return
     */
    public List getFonosDeTransporte(long idEmpresaTransporte, long idLocal) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getFonosDeTransporte(idEmpresaTransporte, idLocal);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idEmpresaTransporte
     * @param idLocal
     * @return
     */
    public List getChoferesDeTransporte(long idEmpresaTransporte, long idLocal) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getChoferesDeTransporte(idEmpresaTransporte, idLocal);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idEmpresaTransporte
     * @param idLocal
     * @return
     */
    public List getPatentesDeTransporte(long idEmpresaTransporte, long idLocal) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPatentesDeTransporte(idEmpresaTransporte, idLocal);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public List getDetPickingToHojaDespacho(long id_pedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getDetPickingToHojaDespacho(id_pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idRuta
     * @param cantBins
     */
    public void actualizaCantBinsRuta(long idRuta) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.actualizaCantBinsRuta(idRuta);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @param operacion
     */
    public void modificaVecesEnRutaDePedido(long idPedido, String operacion) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modificaVecesEnRutaDePedido(idPedido, operacion);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List getDocumentosByPedido(long idPedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getDocumentosByPedido(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getResponsablesDespachoNC() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getResponsablesDespachoNC();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getMotivosDespachoNC() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getMotivosDespachoNC();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param pedExt
     */
    public void updPedidoFinalizado(PedidoExtDTO pedExt) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.updPedidoFinalizado(pedExt);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idRuta
     * @return
     */
    public int getCountPedidoNoFinalizadosByRuta(long idRuta) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getCountPedidoNoFinalizadosByRuta(idRuta);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     */
    public void updPedidoReagendado(long idPedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.updPedidoReagendado(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public List getPedidosPendientesByCriterio(DespachoCriteriaDTO criterio) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPedidosPendientesByCriterio(criterio);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
    }

    /**
     * @param criterio
     * @return
     */
    public double getCountPedidosPendientesByCriterio(DespachoCriteriaDTO criterio) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getCountPedidosPendientesByCriterio(criterio);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
    }

    /**
     * @param idPedido
     * @param idMotivo
     * @param idResponsable
     */
    public void addMotivoResponsableReprogramacion(long idPedido, long idMotivo, long idResponsable, long idJornadaDespachoAnterior, long idUsuario) throws PedidosException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addMotivoResponsableReprogramacion(idPedido, idMotivo, idResponsable, idJornadaDespachoAnterior, idUsuario);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
    }

    /**
     * @return
     */
    public List getMotivosDespachoNCAll() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getMotivosDespachoNCAll();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getResponsablesDespachoNCAll() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getResponsablesDespachoNCAll();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            if (ex.getMessage().equals(DbSQLCode.SQL_ID_KEY_NO_EXIST)) {
                logger.debug("no existe cod del pedido");
                throw new PedidosException(Constantes._EX_OPE_ID_NO_EXISTE, ex);
            }
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idMotivo
     */
    public void delMotivoNCById(long idMotivo) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delMotivoNCById(idMotivo);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param motivo
     */
    public void addMotivoNC(ObjetoDTO motivo) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addMotivoNC(motivo);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param motivo
     */
    public void modMotivoNC(ObjetoDTO motivo) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modMotivoNC(motivo);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idResponsable
     */
    public void delResponsableNCById(long idResponsable) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delResponsableNCById(idResponsable);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param motivo
     */
    public void addResponsableNC(ObjetoDTO responsable) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addResponsableNC(responsable);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param motivo
     */
    public void modResponsableNC(ObjetoDTO responsable) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modResponsableNC(responsable);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idEmpresa
     */
    public void delEmpresaTransporteById(long idEmpresa) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delEmpresaTransporteById(idEmpresa);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param empresa
     */
    public void addEmpresaTransporte(EmpresaTransporteDTO empresa) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addEmpresaTransporte(empresa);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param empresa
     */
    public void modEmpresaTransporte(EmpresaTransporteDTO empresa) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modEmpresaTransporte(empresa);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getEmpresasTransporteAll() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getEmpresasTransporteAll();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getPatentesDeTransporteByLocal(long idLocal) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPatentesDeTransporteByLocal(idLocal);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getFonosDeTransporteByLocal(long idLocal) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getFonosDeTransporteByLocal(idLocal);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idLocal
     * @return
     */
    public List getChoferesDeTransporteByLocal(long idLocal)throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getChoferesDeTransporteByLocal(idLocal);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPatente
     */
    public void delPatenteTransporteById(long idPatente) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delPatenteTransporteById(idPatente);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param patente
     */
    public long addPatenteTransporte(PatenteTransporteDTO patente) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.addPatenteTransporte(patente);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param patente
     */
    public void modPatenteTransporte(PatenteTransporteDTO patente) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modPatenteTransporte(patente);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idFono
     */
    public void delFonoTransporteById(long idFono) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delFonoTransporteById(idFono);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param fono
     */
    public long addFonoTransporte(FonoTransporteDTO fono) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.addFonoTransporte(fono);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param fono
     */
    public void modFonoTransporte(FonoTransporteDTO fono) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modFonoTransporte(fono);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idChofer
     */
    public void delChoferTransporteById(long idChofer) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delChoferTransporteById(idChofer);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param chofer
     */
    public long addChoferTransporte(ChoferTransporteDTO chofer) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.addChoferTransporte(chofer);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param chofer
     */
    public void modChoferTransporte(ChoferTransporteDTO chofer) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modChoferTransporte(chofer);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getEmpresasTransporteActivas() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getEmpresasTransporteActivas();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idChofer
     * @param descripcion
     * @param idUsuario
     */
    public void addLogChoferTransporte(long idChofer, String descripcion, long idUsuario) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addLogChoferTransporte(idChofer, descripcion, idUsuario);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idFono
     * @param descripcion
     * @param idUsuario
     */
    public void addLogFonoTransporte(long idFono, String descripcion, long idUsuario) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addLogFonoTransporte(idFono, descripcion, idUsuario);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPatente
     * @param descripcion
     * @param idUsuario
     */
    public void addLogPatenteTransporte(long idPatente, String descripcion, long idUsuario) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addLogPatenteTransporte(idPatente, descripcion, idUsuario);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List getProductosTodasTrxByPedido(long idPedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getProductosTodasTrxByPedido(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idProducto
     * @param porcion
     * @param unidadPorcion
     */
    public void modPorcionProducto(long idProducto, double porcion, long unidadPorcion) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modPorcionProducto(idProducto, porcion, unidadPorcion);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idProducto
     * @param idPila
     */
    public void delPilaProducto(long idProducto, long idPila) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.delPilaProducto(idProducto, idPila);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param pilaProd
     */
    public void addPilaProducto(PilaProductoDTO pilaProd) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.addPilaProducto(pilaProd);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getUnidadesNutricionales() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getUnidadesNutricionales();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idProducto
     * @return
     */
    public List getPilasNutricionalesByProductoFO(long idProducto) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPilasNutricionalesByProductoFO(idProducto);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getPilasNutricionales() throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPilasNutricionales();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idUnidadPila
     * @return
     */
    public PilaUnidadDTO getPilaUnidadById(long idUnidadPila) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPilaUnidadById(idUnidadPila);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    public boolean DelTrxMP(long id_pedido) throws ServiceException, SystemException {
        JdbcTrxMedioPagoDAO dao = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        try {
            return dao.DelTrxMP(id_pedido);
        } catch (TrxMedioPagoDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    public boolean VerificaHoraCompra(long id_jdespacho, String TipoDespacho) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.VerificaHoraCompra(id_jdespacho, TipoDespacho);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param id_pedido
     * @param key_Validation
     * @return
     */
    public List getProductosXAlerta(long id_pedido, String key_Validation) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getProductosXAlerta(id_pedido, key_Validation);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    public boolean verificaAlertaValidacion(long id_pedido, String key_validacion) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.enValidacionManual(id_pedido, key_validacion);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }
    
    public boolean verificaPrimeraCompraRetiroEnLocal(long id_pedido) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.verificaPrimeraCompraRetiroEnLocal(id_pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }
    
    /**
     * @param idPedido
     * @param esClienteConfiable
     */
    public void ingresarPedidoASistema(long idPedido, boolean esClienteConfiable, long rutCliente) throws PedidosException, SystemException {
    	boolean esCategoriaValidacion = false;
    	boolean esCategoriaAlerta = false;
        try {
            JdbcEventosDAO dao4 = (JdbcEventosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getEventosDAO();

            logger.debug("INI - AUMENTAR OCURRENCIA DE EVENTOS PARA CLIENTE DE OP");
            //subimos ocurrencia de eventos, no importa si no tenemos ningun layer q mostrar.
            logger.debug("Tenemos que aumentar la ocurrencia de los eventos asociados al rut.");
            if (!dao4.subirOcurrenciaEvento(rutCliente, idPedido)) {
                logger.debug("Existió un problema al subir la ocurrencia de los eventos del Rut " + rutCliente + " , id_pedido: " + idPedido);
            }
            logger.debug("FIN - AUMENTAR OCURRENCIA DE EVENTOS PARA CLIENTE DE OP");
        } catch (EventosDAOException e1) {
            logger.error("Error en aumentar ocurrencia rut", e1);
        }

        // Invoca procedimiento Valida la OP (este procedimiento debe quedar fuera de la transacción)
        try {
        	JdbcPedidosDAO daoVal = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        	esCategoriaValidacion	= verificaAlertaValidacion(idPedido, Constantes.KEY_VALIDACION_MANUAL_OP);
        	esCategoriaAlerta		= verificaAlertaValidacion(idPedido, Constantes.KEY_ALERTA_COMPRA_OP);
        	System.out.println(">>>>>>>entrando a PedidosCtrl().<<<<<<<<, esClienteConfiable: "+esClienteConfiable +",esCategoriaValidacion:"+esCategoriaValidacion);
        	
        	if (esCategoriaValidacion) {
        		logger.debug("Categoria validacion: forzando validacion manual de OP.");
        		this.setValidaOP(idPedido);
        	}else{
	            // Si el cliente es confiable, no validamos el pedido (por que confiamos en el... ja!)
	            if (!esClienteConfiable) {
	                // Se verifica si existen alertas o no (sean activas o informativas)
	                this.setValidaOP(idPedido);
	            } else {
	                this.setValidacionClientesConfiables(idPedido);
	            }
        	}
        	
        	// guarda log de alertas de validacion
        	if (esCategoriaValidacion){
        		List allProds = getProductosXAlerta(idPedido, Constantes.KEY_VALIDACION_MANUAL_OP);
        		for (int i = 0; i < allProds.size(); i++) {
	                LogPedidoDTO logVal1 = new LogPedidoDTO();
	                ProductosPedidoDTO prod1 = (ProductosPedidoDTO) allProds.get(i);
	                logVal1.setId_pedido(idPedido);
	                logVal1.setUsuario("SYSTEM");
	                logVal1.setLog("Pedido "+idPedido+" entra a validación manual por producto "+prod1.getCod_sap()+". Por estar en categoría "+prod1.getId_catprod());
	                daoVal.addLogPedido(logVal1);
        		}
        	}
        	if (esCategoriaAlerta){
        		List allProds2 = getProductosXAlerta(idPedido, Constantes.KEY_ALERTA_COMPRA_OP);
                for (int j = 0; j < allProds2.size(); j++) {
                	LogPedidoDTO logVal2 = new LogPedidoDTO();
                	ProductosPedidoDTO prod2 = (ProductosPedidoDTO) allProds2.get(j);
	                logVal2.setId_pedido(idPedido);
	                logVal2.setUsuario("SYSTEM");
	                logVal2.setLog("Pedido "+idPedido+" entra a alerta automática por producto "+prod2.getCod_sap()+". Por estar en categoría "+prod2.getId_catprod());
	                daoVal.addLogPedido(logVal2);
                }
        	}

        } catch (Exception e) {
            //El flujo continua a pesar de problemas en la validacion, ya que el medio de pago hizo reserva al cliente
            e.printStackTrace();
            logger.error("El proceso de validación de pedido falló", e);
        }

        try {
            JdbcPedidosDAO dao3 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

            // Creamos la transacción
            JdbcTransaccion trx2 = new JdbcTransaccion();

            // Iniciamos la transacción
            trx2.begin();

            // Asignamos la transacción a los dao's
            dao3.setTrx(trx2);

            List lst_ale_final = dao3.getAlertasPedido(idPedido);
            long nvo_estado = 0;
            String mnsLog = "";
            
            if (lst_ale_final.size() == 0) {
            	Map hash_process_ale = ProcesoAlertaFinal(idPedido, dao3, trx2);
            	nvo_estado = Long.parseLong((String)hash_process_ale.get("nvo_estado"));
            	mnsLog = (String)hash_process_ale.get("mnsLog");
            } else {
                nvo_estado = Constantes.ID_ESTAD_PEDIDO_EN_VALIDACION; //Estado En Validación
                mnsLog = "El Pedido se encuentra en estado : En Validación";
            }
            boolean stateModEstadoPedido = dao3.setModEstadoPedido(idPedido, nvo_estado);
            logger.debug("mnsLog:" + mnsLog);
            System.out.println(">>>>>>>>>>>>>>>>hb>rpta stateModEstadoPedido: " + stateModEstadoPedido);

            LogPedidoDTO log = new LogPedidoDTO();
            log.setId_pedido(idPedido);
            log.setUsuario("SYSTEM");
            log.setLog(mnsLog);
            dao3.addLogPedido(log);
            trx2.end();

        } catch (Exception e) {
            logger.error("OP queda en estado PRE-Ingresada", e);
        }
    }

    /**
     * @param webpayDTO
     */
    public void webpaySave(WebpayDTO webpayDTO) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.webpaySave(webpayDTO);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public int webpayMonto(int idPedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.webpayMonto(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public WebpayDTO webpayGetPedido(long idPedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.webpayGetPedido(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public int webpayGetEstado(int idPedido) throws PedidosException,SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.webpayGetEstado(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    
    /** Entregala informacion de un pedido para enviar por boton de pago
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetPedido(long idPedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.botonPagoGetPedido(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * Inserta un registro del resultado del pago con Boton de Pago CAT
     * 
     * @param BotonPagoDTO
     */
    public void botonPagoSave(BotonPagoDTO bp) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.botonPagoSave(bp);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /** Entregala informacion de un registro de boton de pago segun el id del pedido
     * @param idPedido
     * @return
     */
    public BotonPagoDTO botonPagoGetByPedido(long idPedido) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.botonPagoGetByPedido(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public HashMap obtenerLstOPByTBK() throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.obtenerLstOPByTBK();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param id_pedido
     * @param id_estado
     */
    public void setModEstadoTrxOP(long id_pedido, int id_estado)throws PedidosException, ServiceException, SystemException {
        JdbcTrxMedioPagoDAO daoT = (JdbcTrxMedioPagoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getTrxMedioPagoDAO();
        try {
            daoT.setModEstadoTrxOP(id_pedido, id_estado);
        } catch (TrxMedioPagoDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }

    }

    /**
     * @param id_pedido
     */
    public WebpayDTO getWebpayByOP(long id_pedido) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getWebpayByOP(id_pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * Actualiza registro de Boton de pago CAT
     */
    public boolean updateNotificacionBotonPago(BotonPagoDTO bp) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.updateNotificacionBotonPago(bp);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idComprador
     * @return
     */
    public List getPedidosPorPagar(long idComprador) throws PedidosException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPedidosPorPagar(idComprador);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     */
    public void actualizaSecuenciaPago(long idPedido) throws PedidosException,SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.actualizaSecuenciaPago(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public boolean pedidoEsFonoCompra(int idPedido) throws PedidosException,SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.pedidoEsFonoCompra(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public boolean setModFlagWebpayByOP(long id_pedido, String flag) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.setModFlagWebpayByOP(id_pedido, flag);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List getProductosPickeadosByIdPedido(long idPedido) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getProductosPickeadosByIdPedido(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @return
     */
    public List getSustitutosYPesablesByPedidoId(long idPedido) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getSustitutosYPesablesByPedidoId(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idPedido
     * @param datosCambiados
     */
    public void cambiarPreciosPickeados(long idPedido, String datosCambiados) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.cambiarPreciosPickeados(idPedido, datosCambiados);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getPedidosEnTransicionTEMP(long idLocal) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPedidosEnTransicionTEMP(idLocal);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param pedido
     */
    public void purgaPedidoPreIngresado(PedidoDTO pedido, long idCliente) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.purgaPedidoPreIngresado(pedido, idCliente);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idProductoFO
     * @param idPedido
     * @param idDetallePedido
     * @param cantidad
     */
    public void modProductoDePedido(long idPedido, long idDetallePedido, double cantidad) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.modProductoDePedido(idPedido, idDetallePedido, cantidad);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param fcIni
     * @param fcFin
     * @param local
     * @param usuario
     * @return
     */
    public List getInformeModificacionDePrecios(String fcIni, String fcFin, long local, String usuario) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getInformeModificacionDePrecios(fcIni, fcFin, local, usuario);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @return
     */
    public List getUsuariosInformeModPrecios() throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getUsuariosInformeModPrecios();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param usuario
     * @return
     */
    public List getInformeProductosSinStock() throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getInformeProductosSinStock();
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param pedido
     */
    public boolean anulacionAceleradaCAT(PedidoDTO pedido) throws PedidosException, ServiceException, SystemException {

        JdbcPedidosDAO dao1 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {

            //            System.out.println("** anulacionAceleradaCAT (V3) **");

            //long monto_trx = dao1.getMontoTotalTrxByIdPedido(pedido.getId_pedido());
            //Validar que monto hay que enviar, ya q no tenemos transacciones
            long monto_trx = (new Double(pedido.getMonto_reservado())).longValue();

            //1- Obtener informacion para confirmar - boton de pago
            BotonPagoDTO bp = dao1.botonPagoGetByPedidoAprobado(pedido.getId_pedido());
            //obtener datos para solicitar la captura
            String numComercio = "";
            ResourceBundle rb = ResourceBundle.getBundle("bo");
            if (pedido.getId_usuario_fono() != 0 && pedido.getOrigen().equals("W")) {
                numComercio = rb.getString("CAT.numComercioFOCliFono");
            } else if (pedido.getOrigen().equals("V")) {
                //Venta Empresas
                numComercio = rb.getString("CAT.numComercioFOVE");
            } else if (pedido.getId_usuario_fono() == 0 && pedido.getOrigen().equals("W")) {
                //Compra realizada por el Cliente
                numComercio = rb.getString("CAT.numComercioFOCliente");
            }
            //            System.out.println("** numComercio:"+numComercio);
            SolicCapturaCAT sol = new SolicCapturaCAT();

            //El codigo de DPC de captura solo es para la confirmación de la reserva de cupo. 
            //En el caso de la anulación de la reserva enviar ese dato con ceros.
            sol.setDpc("000000000000");
            //System.out.println("*** DPC LOCAL : " + sol.getDpc());

            sol.setNumempre(numComercio);
            String ftrx = Formatos.calendarToString(bp.getFechaAutorizacion(), "yyyyMMdd");
            sol.setFectrans(ftrx);
            sol.setCodtrans(Constantes.BTN_PAGO_COD_DEVOLUCION_RESERVA);
            sol.setIdtrn(FormatUtils.addCharToString(bp.getIdCatPedido() + "", " ", 16, FormatUtils.ALIGN_LEFT));
            sol.setTipoauto(" ");
            //Enviar 8 espacios en blanco
            //sol.setCodautor(FormatUtils.addCharToString(bp.getCodigoAutorizacion(), "0", 8, FormatUtils.ALIGN_LEFT));
            sol.setCodautor("        ");
            //            System.out.println("** Codautor: " + sol.getCodautor());
            sol.setMonconta(FormatUtils.addCharToString(monto_trx + "", "0", 9, FormatUtils.ALIGN_RIGHT));
            sol.setHorenvio(Formatos.getHoraActualBtnPago());
            //2. SE REALIZA CAPTURA HACIA CAT - boton de pago
            CapturarCAT capturar = new CapturarCAT();
//            System.out.println("** a devolver cupo con sol.getCodtrans -> " + sol.getCodtrans());
            RptaCapturaCAT rpta = capturar.confirmar_reserva(sol);
            //            System.out.println("** respuesta:" + rpta.getCodrespu());
            if ("010".equalsIgnoreCase(rpta.getCodrespu())) {
                LogPedidoDTO log2 = new LogPedidoDTO();
                log2.setId_pedido(pedido.getId_pedido());
                log2.setUsuario("SYSTEM");
                log2.setLog("Devolución Reserva de Cupo");
                try {
                    dao1.addLogPedido(log2);
                } catch (PedidosDAOException e) {
                    logger.info("No se pudo agregar información al log del pedido");
                }
                return true;
            }
            return false;

        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }

    }

    /**
     * @param idPedido
     * @param marcar
     */
    public void marcaAnulacionBoletaEnLocal(long idPedido, boolean marcar) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.marcaAnulacionBoletaEnLocal(idPedido, marcar);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

    /**
     * @return
     */
    public List getPedidosRechazadosErroneamenteTBK(int dias, int minutos) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPedidosRechazadosErroneamenteTBK(dias, minutos);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    public List getPedidosRechazadosErroneamenteCAT(int dias, int minutos) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getPedidosRechazadosErroneamenteCAT(dias, minutos);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * @param idProducto
     * @param idLocal
     * @return
     */
    public boolean existeProductoEnLocal(long idProducto, long idLocal) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.existeProductoEnLocal(idProducto, idLocal);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    
    //Maxbell - Mejoras al catalogo interno 2014/06/30
    public ProductoStockDTO productoTieneStockEnLocal(long idProducto, long idLocal) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.productoTieneStockEnLocal(idProducto, idLocal);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * Metodo que recupera del dato el listado de productos sustitutos que superan el margen de sustitución.
     * 
     * @return <code>List</code> Listado de <code>ProductoSobreMargenDTO</code> con los productos sustitutos sobre umbral
     * @throws PedidosException
     * @throws ServiceException
     * @throws SystemException
     */
    public List getProductosSustitutosSobreMargen(long id_local) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getProductosSustitutosSobreMargen(id_local);
        } catch (PedidosDAOException ex) {
            logger.error("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }

    /**
     * Metodo que se encarga de eliminar el detalle del reporte de productos sustitutos sobre margen
     * @param idEliminar id del detalle a eliminar
     * @return boolean true en caso de exito false en caso de falla
     * @throws PedidosException
     */
    public boolean eliminarProductosSustitutosSobreMargen(long idEliminar) throws PedidosException {

        boolean res = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {

            res = dao.eliminarProductoSustitutoSobreMargen(idEliminar);

        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return res;
    }

    /**
     * @param idPedido
     */
    public void cambiaEstadoWebPays(long idPedido) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            dao.cambiaEstadoWebPays(idPedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }
    /** (
     * @return
     */
    public List getListadoProductosxPedido(long id_pedido) throws PedidosException, ServiceException, SystemException {
        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
            return dao.getListadoProductosxPedido(id_pedido);
        } catch (PedidosDAOException ex) {
            logger.debug("Problema :" + ex);
            throw new SystemException("Error no controlado", ex);
        }
    }
    /**
     * Metodo que se encarga de eliminar el detalle del reporte de productos sustitutos sobre margen
     * @param idEliminar id del detalle a eliminar
     * @return boolean true en caso de exito false en caso de falla
     * @throws PedidosException
     */
    public boolean setModDetallePedido(long id_pedido, double cantidad, long id_producto) throws PedidosException {

        boolean res = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {

            res = dao.setModDetallePedido(id_pedido, cantidad, id_producto);

        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
}
        return res;
    }

	public boolean modFechaOP(long id_pedido, String paramFecha, String fechaOld,String userLogin) throws PedidosException, DAOException {

        boolean res = false;

        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
           res = dao.modFechaOP(id_pedido, paramFecha);
            String mensajeLog="SE MODIFICA FECHA DE INGRESO OP "+fechaOld +" a "+Formatos.frmFecha(paramFecha);
            if (res) {
            	dao.registrarTracking(id_pedido, userLogin,mensajeLog);
			}

        } catch (PedidosDAOException e) {
            logger.debug("Problema :" + e.getMessage());
            throw new PedidosException(e);
        }
        return res;
    }
	public void insertaTrackingOP(long id_pedido, String user, String mensajeLog) throws DAOException, PedidosException, PedidosDAOException {
		try{
			JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
            dao.registrarTracking(id_pedido, user,mensajeLog);
		}catch (Exception e){
			logger.debug("Error al insertar Tracking OP");
			throw new PedidosDAOException();
		}
        
    }
	
	
	private Map ProcesoAlertaFinal(long idPedido,JdbcPedidosDAO dao3, JdbcTransaccion trx2){
		System.out.println(">>>>>>>>>>>>>>>>ProcesoAlertaFinal() - init");
		Map hash_process_ale = new HashMap();		
		
        long nvo_estado = Constantes.ID_ESTAD_PEDIDO_VALIDADO; //Estado Validado
        hash_process_ale.put("nvo_estado", Long.toString(nvo_estado));
        
        String mnsLog = "El Pedido se encuentra en estado : Validado";
        hash_process_ale.put("mnsLog", mnsLog);
        
        // generar la consulta SAF de quema de cupones si falla queda en la tabla saf para su envio posterior
        // porque se elimino del doIns la confirmacion de la OP
        logger.debug("inicio quema cupones saf");
        ClienteTcpPromosCuponesSaf socketsaf = new ClienteTcpPromosCuponesSaf(Constantes.PROMO_SERVER_HOST, Constantes.PROMO_SERVER_TCP_PORT, trx2);
        //se deja en cero dado que el FO fue quien lo envio
        socketsaf.setGeneraMsgSaf(idPedido, 0);
        logger.debug("fin quema cupones saf");

        //***********************************************
        //Logica para agregar bolsa de regalo

        try {
            JdbcBolsasDAO bolsasDao = (JdbcBolsasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getBolsaDAO();
            JdbcProductosDAO productosDao = (JdbcProductosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getProductosDAO();

            if (bolsasDao.estaActivoServicioBolsas()) {
                System.out.println("****** SERVICIO DE ASIGNACION DE BOLSA ACTIVO *****");

                System.out.println("***************************************");
                System.out.println("********VALIDACIÓN ASIGNA BOLSA********");
                System.out.println("***************************************");

                PedidoDTO pedido = dao3.getPedidoById(idPedido);

                //long id_local = pedido.getId_local();
                String rut_cliente_pedido = pedido.getRut_cliente() + "-" + pedido.getDv_cliente();
                BolsaDTO bolsa = new BolsaDTO();
                ProductoEntity producto = new ProductoEntity();
                int id_prod = -1;
                String pedidosNum = "";

                if (pedido.getOrigen().equalsIgnoreCase("W")) { // si no es venta empresa
                    System.out.println("---NO ES VENTA EMPRESA!");
                    boolean flagFueAsignado = false;
                    boolean flagEL = bolsasDao.existeEnListaBase(rut_cliente_pedido);

                    if (flagEL) {
                        flagFueAsignado = true;
                    }

                    //Validacion existencia de Bolsa de Regalo dentro del mismo periodo
                    //de tener bolsa 1 o bolsa 2 ya asignada, no asigna.
                    boolean flagTieneDespachosEnPeriodo = false;
                    //boolean flagTieneBolsaAsignadaEnPeriodo = false;
                    boolean flagNoTieneBolsa = false;
                    List listPedidosR = dao3.getPedidosPorEstadoR(pedido.getId_cliente());
                    for (int i = 0; i < listPedidosR.size(); i++) {
                        PedidoDTO ped1 = (PedidoDTO) listPedidosR.get(i);
                        // validaciones:
                        // mes despacho != mes op que se generó
                        // año despacho != año op que se generó
                        // estado != pedido anulado
                        if (ped1.getFdespacho() != null) {
                            if (Integer.parseInt(ped1.getFdespacho().split("-")[1]) == Integer.parseInt(pedido.getFdespacho().split("-")[1])
							&& 
                            Integer.parseInt(ped1.getFdespacho().split("-")[0]) == Integer.parseInt(pedido.getFdespacho().split("-")[0])
                            ) {
                                pedidosNum = ped1.getId_pedido() + ","+ pedidosNum;
                            }
					}/*else{
						break;
					}*/
                    }

                    flagNoTieneBolsa = dao3.tieneBolsaRegalo(pedidosNum.substring(0, pedidosNum.length() - 1));

                    if (flagNoTieneBolsa) { //SI PEDIDO NO TIENE BOLSA DE REGALO ASIGNADA DENTRO DEL MISMO PERIODO
                        System.out.println("---EL PEDIDO NO TIENE BOLSA ASIGNADA EN MISMO PERIODO!");
                        //consulta si está en lista base;
                        // true = cliente que ya tiene asignación de bolsa
                        // false = cliente nunca asignado
                        if (flagFueAsignado) {
                            System.out.println("---CLIENTE SE ENCUENTRA EN LISTA BASE!");
                            flagTieneDespachosEnPeriodo = false;
                            List listPedidos = dao3.getPedidosPorEstado(pedido.getId_cliente(), idPedido);
                            List productosPedidoComparar = new ArrayList();
                            BolsaDTO bolsaComparar = new BolsaDTO();

                            for (int i = 0; i < listPedidos.size(); i++) {
                                PedidoDTO ped = (PedidoDTO) listPedidos.get(i);
                                // validaciones:
                                // mes despacho != mes op que se generó
                                // año despacho != año op que se generó
                                // estado != pedido anulado
                                if (ped.getFdespacho() != null) {
                                    if (Integer.parseInt(ped.getFdespacho().split("-")[1]) == Integer.parseInt(pedido.getFdespacho().split("-")[1])
										&& 
										Integer.parseInt(ped.getFdespacho().split("-")[0]) == Integer.parseInt(pedido.getFdespacho().split("-")[0])
										) {
                                        flagTieneDespachosEnPeriodo = true;
                                        productosPedidoComparar = dao3.getDetallesPedido(ped.getId_pedido());
                                        //System.out.println("DETALLE DEL PEDIDO : "+ped.getId_pedido());
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }

                            List bolsas = bolsasDao.getBolsasRegalo();

                            for (int i = 0; i < productosPedidoComparar.size(); i++) {
                                ProductosPedidoDTO prodComp = (ProductosPedidoDTO) productosPedidoComparar.get(i);
                                for (int y = 0; y < bolsas.size(); y++) {
                                    BolsaDTO bolsaComp = (BolsaDTO) bolsas.get(y);
                                    //System.out.println("COMPARO BOLSA DE LISTA EN BASE CON LA DEL ULTIMO PERIODO");
                                    //System.out.println("COMPARO BOLSA "+prodComp.getCod_producto()+" == "+bolsaComp.getCod_sap());
                                    if (prodComp.getCod_producto().equals(bolsaComp.getCod_sap())) {
                                        bolsaComparar = bolsaComp;
                                        System.out.println("CODIGO DE BOLSA ASIGNADO : "+ bolsaComparar.getCod_bolsa());
                                        break;
                                    }
                                }
                            }

                            Hashtable hAsignacion = bolsasDao.getAsignacionBolsaCliente(rut_cliente_pedido);

                            if (flagTieneDespachosEnPeriodo) {
                                System.out.println("---CLIENTE TIENE DESPACHOS EN PERIODO!");

						// bolsa anterior == bolsa actual (ultimo pedido)
						//List productosPedido = dao3.getDetallesPedido(0);

						//if( ((String)hAsignacion.get("cod_bolsa")).equalsIgnoreCase(bolsaComparar.getCod_bolsa()) ){
						//System.out.println("BOLSA ANTERIOR = BOLSA ACTUAL -> "+(String)hAsignacion.get("cod_bolsa")+" = "+bolsaComparar.getCod_bolsa());
                                if (((String) hAsignacion.get("cod_bolsa")).equals(bolsaComparar.getCod_bolsa())) {
                                    System.out.println("---BOLSA LISTA BASE ES IGUAL A BOLSA ACTUAL!");

                                    if (bolsaComparar.getCod_bolsa().equals("2")) { // bolsa actual igual a bolsa N°2
                                        System.out.println("---BOLSA LISTA BASE ES IGUAL A BOLSA 2!");
                                    } else {
                                        System.out.println("---BOLSA LISTA BASE NO IGUAL A BOLSA 2!");
                                        bolsa = bolsasDao.getBolsaRegalo("2");
                                        producto = productosDao.getProductoById(bolsa.getId_producto());

                                        //System.out.println("ID PRODUCTO DE LA BOLSA : "+bolsa.getId_producto()+" ID PRODUCTO : "+producto.getId_bo());

                                        //asigno bolsa al pedido
                                        ProductosPedidoDTO prod = new ProductosPedidoDTO();
                                        prod.setId_pedido(idPedido);
                                        prod.setId_producto(producto.getId_bo().longValue());
                                        prod.setId_sector(2);

                                        prod.setCod_producto(producto.getCod_sap());//LPC bolsa.getId_producto()+"");
                                        prod.setUnid_medida("ST");
                                        prod.setDescripcion(producto.getDesc_larga());
                                        prod.setCant_solic(1);
                                        prod.setObservacion(producto.getDesc_larga());
                                        prod.setPrecio(1);
                                        prod.setCant_spick(1);
                                        prod.setPesable(producto.getEs_pesable());
                                        prod.setPreparable(producto.getEs_prep());
                                        prod.setCon_nota("");
                                        prod.setTipoSel(producto.getTipo());
                                        prod.setPrecio_lista(1);
                                        prod.setDscto_item(0);

                                        System.out.println("ID_PRODUCTO="+ id_prod);
                                        System.out.println("---SE LE ASIGNA LA BOLSA 2!");

                                        if (bolsasDao.existeStockBolsaSucursal((pedido.getId_local() + ""),bolsa.getCod_bolsa())) {
                                            dao3.agregaProductoPedido(prod);
                                            dao3.descuentaStockBolsa(bolsa.getCod_bolsa(),(pedido.getId_local() + ""));
                                        }

                                    }
                                } else {
                                    System.out.println("---BOLSA LISTA BASE ES DIFERENTE A BOLSA ACTUAL!");
                                    bolsa = bolsasDao.getBolsaRegalo((String) hAsignacion.get("cod_bolsa"));
                                    producto = productosDao.getProductoById(bolsa.getId_producto());

                                    //asigno bolsa al pedido
                                    ProductosPedidoDTO prod = new ProductosPedidoDTO();
                                    prod.setId_pedido(idPedido);
                                    prod.setId_producto(producto.getId_bo().longValue());
                                    prod.setId_sector(2);

                                    prod.setCod_producto(producto.getCod_sap()); //LPC bolsa.getId_producto()+"");
                                    prod.setUnid_medida("ST");
                                    prod.setDescripcion(producto.getDesc_larga());
                                    prod.setCant_solic(1);
                                    prod.setObservacion(producto.getDesc_larga());
                                    prod.setPrecio(1);
                                    prod.setCant_spick(1);
                                    prod.setPesable(producto.getEs_pesable());
                                    prod.setPreparable(producto.getEs_prep());
                                    prod.setCon_nota("");
                                    prod.setTipoSel(producto.getTipo());
                                    prod.setPrecio_lista(1);
                                    prod.setDscto_item(0);

                                    System.out.println("ID_PRODUCTO=" + id_prod);
                                    System.out.println("---SE LE ASIGNA LA BOLSA EN LISTA BASE!");
                                    if (bolsasDao.existeStockBolsaSucursal((pedido.getId_local() + ""),bolsa.getCod_bolsa())) {
                                        dao3.agregaProductoPedido(prod);
                                        dao3.descuentaStockBolsa(bolsa.getCod_bolsa(),(pedido.getId_local() + ""));
                                    }
                                }
                            } else {
                                System.out.println("---CLIENTE NO TIENE DESPACHOS EN PERIODO!");
                                bolsa = bolsasDao.getBolsaRegalo((String) hAsignacion.get("cod_bolsa"));
                                producto = productosDao.getProductoById(bolsa.getId_producto());

                                //asigno bolsa al pedido
                                ProductosPedidoDTO prod = new ProductosPedidoDTO();
                                prod.setId_pedido(idPedido);
                                prod.setId_producto(producto.getId_bo().longValue());
                                prod.setId_sector(2);

                                prod.setCod_producto(producto.getCod_sap()); //LPC bolsa.getId_producto()+"");
                                prod.setUnid_medida("ST");
                                prod.setDescripcion(producto.getDesc_larga());
                                prod.setCant_solic(1);
                                prod.setObservacion(producto.getDesc_larga());
                                prod.setPrecio(1);
                                prod.setCant_spick(1);
                                prod.setPesable(producto.getEs_pesable());
                                prod.setPreparable(producto.getEs_prep());
                                prod.setCon_nota("");
                                prod.setTipoSel(producto.getTipo());
                                prod.setPrecio_lista(1);
                                prod.setDscto_item(0);

                                System.out.println("ID_PRODUCTO=" + id_prod);
                                System.out.println("---SE LE ASIGNA LA BOLSA EN LISTA BASE!");
                                if (bolsasDao.existeStockBolsaSucursal((pedido.getId_local() + ""), bolsa.getCod_bolsa())) {
                                    dao3.agregaProductoPedido(prod);
                                    dao3.descuentaStockBolsa(bolsa.getCod_bolsa(), (pedido.getId_local() + ""));
                                }
                            }

                        } else {
                            System.out.println("---CLIENTE NO SE ENCUENTRA EN LISTA BASE!");
                            boolean flagEsClienteNuevo = dao3.esPrimeraCompra(pedido.getId_cliente());

                            if (flagEsClienteNuevo) {// si es nuevo, asignar bolsa N°1
                                System.out.println("---CLIENTE ES NUEVO!");
                                System.out.println("---SE ASIGNA BOLSA 1!");
                                bolsa = bolsasDao.getBolsaRegalo("1");
                                producto = productosDao.getProductoById(bolsa.getId_producto());
                                //agrego a lista base
                                bolsasDao.asignacionBolsaCliente(rut_cliente_pedido, "1"); // rut_cliente_pedido, ID bolsa N°1
                            } else {// si NO es nuevo, asignar bolsa N°2
                                System.out.println("---CLIENTE NO ES NUEVO!");
                                System.out.println("---SE ASIGNA BOLSA 2!");
                                bolsa = bolsasDao.getBolsaRegalo("2");
                                producto = productosDao.getProductoById(bolsa.getId_producto());
                                //agrego a lista base
                                bolsasDao.asignacionBolsaCliente(rut_cliente_pedido, "2"); // rut_cliente_pedido, ID bolsa N°2
                            }

                            //asigno bolsa al pedido
                            ProductosPedidoDTO prod = new ProductosPedidoDTO();
                            prod.setId_pedido(idPedido);
                            prod.setId_producto(producto.getId_bo().longValue());
                            prod.setId_sector(2);

                            //prod.setCod_producto(bolsa.getId_producto()+"");
                            prod.setCod_producto(producto.getCod_sap());
                            prod.setUnid_medida("ST");
                            prod.setDescripcion(producto.getDesc_larga());
                            prod.setCant_solic(1);
                            prod.setObservacion(producto.getDesc_larga());
                            prod.setPrecio(1);
                            prod.setCant_spick(1);
                            prod.setPesable(producto.getEs_pesable());
                            prod.setPreparable(producto.getEs_prep());
                            prod.setCon_nota("");
                            prod.setTipoSel(producto.getTipo());
                            prod.setPrecio_lista(1);
                            prod.setDscto_item(0);

                            System.out.println("ID_PRODUCTO=" + id_prod);

                            System.out.println("---SE AGREGA CLIENTE A LISTA BASE!");

                            if (bolsasDao.existeStockBolsaSucursal((pedido.getId_local() + ""), bolsa.getCod_bolsa())) {
                                dao3.agregaProductoPedido(prod);
                                dao3.descuentaStockBolsa(bolsa.getCod_bolsa(), (pedido.getId_local() + ""));
                            }

                        }
                    } else {
                        System.out.println("---EL PEDIDO YA TIENE BOLSA ASIGNADA EN MISMO PERIODO!");
                    }

                } else {
                    System.out.println("---NO ES VENTA EMPRESA!");
                }

                System.out.println("***************************************");
                System.out.println("******FIN VALIDACIÓN ASIGNA BOLSA******");
                System.out.println("***************************************");
            } else {
                System.out.println("****** SERVICIO DE ASIGNACION DE BOLSA NO-ACTIVO *****");
            }
        } catch (Exception e) {
            logger.error("EL PROCESO DE ASIGNACIÓN DE BOLSA FALLÓ", e);
            System.out.println("EL PROCESO DE ASIGNACIÓN DE BOLSA FALLÓ =" + e.getLocalizedMessage());
            e.printStackTrace();
        }

        //FIN Logica para agregar bolsas de regalo
        //***********************************************
        
        System.out.println(">>>>>>>>>>>>>>>>ProcesoAlertaFinal() - fin");
        return hash_process_ale;
	}


	public List getMailPM() throws PedidosException, PedidosDAOException {
        //List result = new ArrayList();

        try {
            JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
            return dao.getMailPM();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.debug("Problema :" + ex.getMessage());
            throw new PedidosException(ex);
        }
    }
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	//public class NewPedidosCtrl extends PedidosCtrl {
	
	/**
	 * Transcción que inserta pedido. Es llamada desde el FO. <br>
     * Nota: Este método tiene <b>Transaccionalidad </b>.
	 * Se agregan parametros para aplicar cupon de descuento (cddto, cuponProds) cdd
	 * @param pedido
	 * @param cddto
	 * @param cuponProds
	 * @return
	 * @throws PedidosException
	 *             en uno de los siguientes casos: <br>- Campo id_cliente es
     *             incorrecto. <br>- Campo dir_id es incorrecto. <br>- Campo
     *             id_jdespacho es incorrecto. <br>- Campo medio_pago es
     *             incorrecto. <br>
	 * @throws SystemException
	 *              en el caso que exista error de sistema.
	 */
	    public long doInsPedido(ProcInsPedidoDTO pedido, CuponDsctoDTO cddto, List cuponProds) throws PedidosException, SystemException {
	    	String rutInv="";
	    	String nomInv="";
	    	String apeInv="";
	        validate(pedido);
	        if (!(pedido.getRut_tit()==null)){
	        	rutInv=pedido.getRut_tit();
	        	nomInv=pedido.getNom_tit();
	        	apeInv=pedido.getApat_tit();
	        	pedido.setApat_tit(null);
	        	pedido.setNom_tit(null);
	        	pedido.setRut_tit(null);
	        }
	        

	        long idJDespacho = 0;
	        long idLocal = 0;

	        // Creamos los dao's
	        PedidosDAO dao1 = (PedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
	        JdbcJornadasDAO dao2 = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
	        JdbcZonasDespachoDAO dao5 = (JdbcZonasDespachoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getZonasDespachoDAO();

	        // Creamos la transacción
	        JdbcTransaccion trx = new JdbcTransaccion();

	        try {
	            // Asignamos la transacción a los dao's
	            dao1.setTrx(trx);
	            dao2.setTrx(trx);
	            dao5.setTrx(trx);

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new SystemException(e);
	        }

	        // Creamos el objeto pedido
	        ExtPedidoDTO ped2 = new ExtPedidoDTO();

	        long idPedido = 0;
	        try {
	            // 3. Recupera datos del cliente
	            logger.info("doInsPedido(New): Recupera datos del cliente");
	            ClienteEntity cliente = dao1.getClienteById(pedido.getId_cliente());

	            if (pedido.getTipo_despacho().equalsIgnoreCase("R")) {
	                logger.info("doInsPedido(New): Recupera datos de la direccion local retiro");
	                idLocal = pedido.getId_local_desp();
	                LocalDTO loc = dao1.getLocalRetiro(idLocal);
	                List comuna = dao5.getComunasByIdZonaDespacho(pedido.getId_zona()); //<ComunaDTO>

	                ped2.setId_local(loc.getId_local());
	                ped2.setId_local_fact(loc.getId_local());
	                ped2.setTipo_picking(loc.getTipo_picking());
	                ped2.setId_comuna(((ComunaDTO) comuna.get(0)).getId_comuna());
	                ped2.setId_zona(pedido.getId_zona());
	                ped2.setIndicacion("Retira en Local " + loc.getNom_local());
	                ped2.setDir_id(0);
	                ped2.setDir_tipo_calle("");
	                ped2.setDir_calle(loc.getDireccion());
	                ped2.setDir_numero("");
	                ped2.setDir_depto("");

	            } else {
	                // 4. Recupera datos de la dirección de despacho
	                logger.info("doInsPedido(New): Recupera datos de la direccion de despacho");
	                DireccionEntity dir1 = dao1.getDireccionById(pedido.getDir_id());
	                idLocal = dir1.getLoc_cod().longValue();
	                LocalDTO loc = dao1.getLocalById(idLocal);

	                ped2.setId_local(dir1.getLoc_cod().longValue());
	                ped2.setId_local_fact(dir1.getLoc_cod().longValue());
	                ped2.setId_comuna(dir1.getCom_id().longValue());
	                ped2.setId_zona(dir1.getZon_id().longValue());
	                ped2.setIndicacion(dir1.getComentarios());
	                ped2.setDir_id(pedido.getDir_id());
	                ped2.setDir_tipo_calle(dir1.getNom_tip_calle());
	                ped2.setDir_calle(dir1.getCalle());
	                ped2.setDir_numero(dir1.getNumero());
	                ped2.setDir_depto(dir1.getDepto());
	                ped2.setTipo_picking(loc.getTipo_picking());
	            }
	            if (pedido.getDispositivo().equalsIgnoreCase("M")){
	            	cliente.setColaborador(false);//no existe cliente colaborador para venta masiva
	            }

	            // 4.1 Obtiene jornada de despacho con Mayor Capacidad de Picking,
	            // sólo en caso de que el despacho sea Económico.

	            if (pedido.getTipo_despacho().equalsIgnoreCase("C")) {
	                logger.info("doInsPedido(New): Obtiene jornada de despacho con mayor capacidad de picking");
	                idJDespacho = dao2.getJornadaDespachoMayorCapacidad(pedido.getId_zona(), Utils.fechaJornadaDespacho(String.valueOf(pedido.getFecha_despacho())),
	                        pedido.getCantidadProductos());
	                if(idJDespacho == 0){
		                throw new PedidosException("TIPO_DESPACHO_ERROR");
		            }
	                pedido.setId_jdespacho(idJDespacho);
	            }
	            // 5. Obtiene la jornada de picking asociada
	            logger.info("doInsPedido(New): Obtiene la jornada de picking asociada");
	            JornadaDespachoEntity jor1 = dao2.getJornadaDespachoById(pedido.getId_jdespacho());

	            // Llenamos dto pedido
	            ped2.setId_estado(Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO);
	            ped2.setId_jdespacho(pedido.getId_jdespacho());
	            ped2.setId_jpicking(jor1.getId_jpicking());
	            ped2.setId_cliente(pedido.getId_cliente());

	            ped2.setId_usuario_fono(pedido.getId_usuario_fono());

	            ped2.setGenero(cliente.getGenero());
	            ped2.setFnac(cliente.getFec_nac());
	            if (cliente.getRut() != null)
	                ped2.setRut_cliente(cliente.getRut().longValue());
	            else
	                ped2.setRut_cliente(0);
	            ped2.setDv_cliente(cliente.getDv());
	            ped2.setNom_cliente(cliente.getNombre() + " " + cliente.getApellido_pat() + " " + cliente.getApellido_mat());
	            ped2.setTelefono(cliente.getFon_cod_1() + " " + cliente.getFon_num_1());
	            ped2.setTelefono2(cliente.getFon_cod_2() + " " + cliente.getFon_num_2());
	            if (cliente.getNombre().equalsIgnoreCase("Invitado")){
	            	ped2.setNom_cliente(nomInv+" "+apeInv);
	            	ped2.setRut_cliente(Long.parseLong(rutInv));
	            	int M=0,S=1,T=Integer.parseInt(rutInv);for(;T!=0;T/=10)S=(S+T%10*(9-M++%6))%11;
	            	ped2.setDv_cliente(Character.toString((char)(S!=0?S+47:75)));
	            }
	            // //E: Express, N: Normal, C: Económico
	            if (pedido.getTipo_despacho() == null) {
	                ped2.setTipo_despacho("N");
	            } else {
	                ped2.setTipo_despacho(pedido.getTipo_despacho());
	            }

	            if (pedido.getTipo_despacho().equals("E")) {
	                ped2.setCosto_despacho(jor1.getTarifa_express());
	            } else if (pedido.getTipo_despacho().equals("N") || pedido.getTipo_despacho().equals("R")) {
	                // La tarifa normal se aplicara a los pedidos de retiro en local
	            	if(pedido.getTipo_despacho().equals("R") || cliente.isColaborador()){
	            		ped2.setCosto_despacho(pedido.getCosto_desp());
	            	}else if (pedido.getDispositivo().equalsIgnoreCase("M")){//Venta Masiva
	            		ped2.setCosto_despacho(pedido.getCosto_desp());
	            		ped2.setTelefono2(cliente.getFon_num_2());//se deja el telefono sin espacios por si no valida
	            	}else{
	            		ped2.setCosto_despacho(jor1.getTarifa_normal());
	            	}
	                
	            } else if (pedido.getTipo_despacho().equals("C")) {
	                ped2.setCosto_despacho(jor1.getTarifa_economica());
	            } else if (pedido.getTipo_despacho().equals("U")) {
	                ped2.setCosto_despacho(jor1.getTarifa_umbral());
	            }
	            
	            if (pedido.getCosto_desp() == 1){
	            	ped2.setCosto_despacho(1);
	            }

	            ped2.setMonto(pedido.getMontoTotal());
	            ped2.setMedio_pago(pedido.getMedio_pago());
	            ped2.setTipo_doc(pedido.getTipo_doc());
	            ped2.setCant_prods((long) pedido.getCantidadProductos());
	            ped2.setSin_gente_op((int) pedido.getSin_gente_op());
	            ped2.setSin_gente_txt(pedido.getSin_gente_txt());
	            ped2.setSin_gente_rut(pedido.getSin_gente_rut());
	            ped2.setSin_gente_dv(pedido.getSin_gente_dv());
	            ped2.setPol_id(pedido.getPol_id());
	            ped2.setPol_sustitucion(pedido.getPol_sustitucion());
	            ped2.setObservacion(pedido.getObservacion());

	            // INGRESA DATOS DEL ADICIONAL
	            ped2.setRut_tit(pedido.getRut_tit());
	            ped2.setDv_tit(pedido.getDv_tit());
	            ped2.setNom_tit(pedido.getNom_tit());
	            ped2.setApat_tit(pedido.getApat_tit());
	            ped2.setAmat_tit(pedido.getAmat_tit());
	            ped2.setDir_tit(pedido.getDir_tit());
	            ped2.setDir_num_tit(pedido.getDir_num_tit());
	            ped2.setDispositivo(pedido.getDispositivo());

	            // Descuenta en forma Automática el Costo del Despacho
	            // dependiendo del Medio de Pago y Monto Limite
	            // Definido para la Zona de Despacho del Pedido
	            ZonaDTO zona = dao5.getZonaDespachoById(ped2.getId_zona());
	            boolean descuento = false;
	            double costo_despacho_old = ped2.getCosto_despacho();
	            // 01: siempre, 10: primera compra, 11: ambos
	            
	            if (pedido.isDescuentoDespacho()) {
	            	if (ped2.getMedio_pago().equalsIgnoreCase("CAT")) {
		                if (((zona.getEstado_descuento_cat() & 1) == 1 && pedido.getMontoTotal() >= zona.getMonto_descuento_cat())
		                        || ((zona.getEstado_descuento_cat() & 2) == 2 && pedido.getMontoTotal() >= zona.getMonto_descuento_pc_cat() && dao1
		                                .esPrimeraCompra(pedido.getId_cliente()))) {
		                    ped2.setCosto_despacho(1);
		                    descuento = true;
		                }
		            }
		            if (ped2.getMedio_pago().equalsIgnoreCase("TBK")) {
		                if (((zona.getEstado_descuento_tbk() & 1) == 1 && pedido.getMontoTotal() >= zona.getMonto_descuento_tbk())
		                        || ((zona.getEstado_descuento_tbk() & 2) == 2 && pedido.getMontoTotal() >= zona.getMonto_descuento_pc_tbk() && dao1
		                                .esPrimeraCompra(pedido.getId_cliente()))) {
		                    ped2.setCosto_despacho(1);
		                    descuento = true;
		                }
		            }
		            if (ped2.getMedio_pago().equalsIgnoreCase("PAR")) {
		                if (((zona.getEstado_descuento_par() & 1) == 1 && pedido.getMontoTotal() >= zona.getMonto_descuento_par())
		                        || ((zona.getEstado_descuento_par() & 2) == 2 && pedido.getMontoTotal() >= zona.getMonto_descuento_pc_par() && dao1
		                                .esPrimeraCompra(pedido.getId_cliente()))) {
		                    ped2.setCosto_despacho(1);
		                    descuento = true;
		                }
		            }
	            }

	            // Inserta LOG por Descuento Automático
	            if (descuento) {
	                LogPedidoDTO log = new LogPedidoDTO();
	                log.setUsuario("SYSTEM");
	                log.setLog("El costo de despacho fue cambiado en forma autom&aacute;tica de <font color='#0000FF'>$" + costo_despacho_old
	                        + "</font> a <font color='#FF0000'>$1</font>");
	                ped2.setLogPedido(log);
	            }

	            // 6. Preparamos dto detalle de pedido
	            logger.info("doInsPedido(New): Preparamos detalle de pedido");
	            for (int i = 0; i < pedido.getProductos().size(); i++) {
	                // Dto generado en el FO
	                ProcInsPedidoDetalleFODTO det1 = (ProcInsPedidoDetalleFODTO) pedido.getProductos().get(i);

	                // Dto que se pasará al dao
	                ExtProductosPedidoDTO productosPedido = new ExtProductosPedidoDTO();

	                // Obtenemos información del producto
	                ProductoEntity productoEntity = (ProductoEntity) dao1.getProductoPedidoByIdProdFO(det1.getId_producto_fo());

	                // avc -->det2.setId_pedido(idPedido);
	                productosPedido.setId_producto(productoEntity.getId_bo().longValue());
	                productosPedido.setId_sector(productoEntity.getId_sector());
	                productosPedido.setCod_producto(productoEntity.getCod_sap());
	                productosPedido.setUnid_medida(productoEntity.getTipre());

	                String descripcion = productoEntity.getTipo() + " " + productoEntity.getNom_marca() + " " + productoEntity.getDesc_corta();
	                if (descripcion.length() > 255) {
	                    descripcion = descripcion.substring(0, 255);
	                }
	                productosPedido.setDescripcion(descripcion);

	                productosPedido.setCant_solic(det1.getCant_solic());
	                productosPedido.setPrecio(det1.getPrecio_unitario());
	                productosPedido.setPrecio_lista(det1.getPrecio_lista());

	                productosPedido.setCant_spick(det1.getCant_solic());
	                productosPedido.setPesable(productoEntity.getEs_pesable());
	                productosPedido.setPreparable(productoEntity.getEs_prep());

	                if (!det1.getObservacion().equals("") || det1.getObservacion() == null) {
	                    productosPedido.setCon_nota("N");
	                } else if (det1.getObservacion().equalsIgnoreCase("N") || det1.getObservacion().equals("")) {
	                    productosPedido.setCon_nota("N");
	                } else {
	                    productosPedido.setCon_nota("S");
	                }

	                productosPedido.setObservacion(det1.getObservacion());
	                productosPedido.setTipoSel(det1.getTipoSel());

	                // --- INI - Sustitutos ---
	                logger.info("doInsPedido(New): Sustitutos");
	                CriterioSustitutoDTO criterioCliente = dao1.getCriterioByClienteAndProducto(pedido.getId_cliente(), det1.getId_producto_fo());
	                productosPedido.setIdCriterio(criterioCliente.getIdCriterio());
	                if (criterioCliente.getIdCriterio() == 4) {
	                    productosPedido.setDescCriterio(criterioCliente.getDescCriterio());
	                } else {
	                    productosPedido.setDescCriterio("");
	                }
	                // --- FIN - Sustitutos ---

	                productosPedido.setImpuesto(productoEntity.getImpuesto());
	                productosPedido.setCod_barra(productoEntity.getCodBarra());
	                productosPedido.setId_seccion(productoEntity.getIdSeccion());
	                productosPedido.setSeccion(String.valueOf(productoEntity.getIdSeccion()));
	                
// inicio cdd          
	                productosPedido.setRubro(productoEntity.getRubro());
// fin cdd

	                ped2.getProductos().add(productosPedido);
	            } //productos - detalle de pedido

	            // Agregamos los tcp
	            //[2012113avc
	            if (pedido.getLst_tcp().size() > 0) {
	                logger.info("doInsPedido(New): Agregamos los TCP");
	                for (int i = 0; pedido.getLst_tcp() != null && i < pedido.getLst_tcp().size(); i++) {
	                    TcpPedidoDTO tcp = (TcpPedidoDTO) pedido.getLst_tcp().get(i);
	                    tcp.setCant_util(0);
	                    ped2.getTcpPedido().add(tcp);
	                }
	            }
	            //]2012113avc

	            // Agregamos los Cupones
	            //[2012113avc
	            if (pedido.getLst_cupones().size() > 0) {
	                logger.info("doInsPedido(New): Agregamos los Cupones");
	                for (int i = 0; pedido.getLst_cupones() != null && i < pedido.getLst_cupones().size(); i++) {
	                    long id = -1;

	                    CuponPedidoDTO cupon = (CuponPedidoDTO) pedido.getLst_cupones().get(i);
	                    TcpPedidoDTO tcp_ex = null;
	                    for (int j = 0; j < ped2.getTcpPedido().size(); j++) {
	                        TcpPedidoDTO tcp = (TcpPedidoDTO) ped2.getTcpPedido().get(j);
	                        if (tcp.getNro_tcp() == cupon.getNro_tcp())
	                            tcp_ex = tcp;
	                    }

	                    // logger.debug("id_pedido: " + idPedido);
	                    logger.debug("nro_tcp: " + cupon.getNro_tcp());
	                    logger.debug("tcp_ex.getId_tcp(): " + tcp_ex.getId_tcp());
	                // Si no existe el tcp se debe ingresar, sino se debe rescatar
	                    // su id
	                    if (tcp_ex.getId_tcp() <= 0) {
	                        // Insertamos los tcp de los cupones
	                        logger.debug("tcp_ex es null, NO existe el nro_tcp para el id_pedido");
	                        TcpPedidoDTO tcp_cupon = new TcpPedidoDTO();
	                        // avc --> tcp_cupon.setId_pedido(idPedido);
	                        tcp_cupon.setCant_max(cupon.getCant_max());
	                        tcp_cupon.setCant_util(0);
	                        tcp_cupon.setNro_tcp(cupon.getNro_tcp());

	                        ped2.getTcpPedido().add(tcp_cupon);// id =
	                        // dao1.setTcpPedido(tcp_cupon);

	                    } else {
	                        logger.debug("Ya existe el tcp, rescatamos su id");
	                        id = tcp_ex.getId_tcp();
	                    }
	                    logger.debug("id_tcp: " + id);
	                    logger.debug("nro_cupon" + cupon.getNro_cupon());

	                    ped2.getCuponPedido().add(cupon); // dao1.setCuponPedido(cupon);
	                } //cupone
	            }
	            //]2012113avc

	            // Aplica los descuentos generados por las promociones al detalle de
	            // pedido
	            logger.info("doInsPedido(New): Aplica los descuentos generados por las promociones al detalle de pedido");
	            //[2012113avc
	            
// inicio cdd
	            List lst_prod_promo = this.doRecalculoPromocion(ped2, idLocal, dao1, cliente.isColaborador() ? cliente.getRut() : null, cddto, cuponProds);
// fin cdd

	            ArrayList detped = new ArrayList();
	            for (int i = 0; i < lst_prod_promo.size(); i++) {
	                ResumenPedidoPromocionDTO resumenPedidoPromocion = (ResumenPedidoPromocionDTO) lst_prod_promo.get(i);

	                //Obtenemos el listado de productos relacionados a la promocion
	                //[2012113avc
	                logger.info("doInsPedido(New): Obtenemos el listado de productos relacionados a la promocion");
	                for (int j = 0; j < resumenPedidoPromocion.getProd_relacionados().size(); j++) {
	                    ProductosRelacionadosPromoDTO productosRelacionadosPromo = (ProductosRelacionadosPromoDTO) resumenPedidoPromocion.getProd_relacionados().get(j);

	                    //Agregamos datos de promociones
	                    PromoDetallePedidoDTO promoDetallePedido = new PromoDetallePedidoDTO();
	                    promoDetallePedido.setId_promocion(resumenPedidoPromocion.getId_promocion());
	                    promoDetallePedido.setPromo_codigo(resumenPedidoPromocion.getPromo_codigo());
	                    promoDetallePedido.setPromo_desc(resumenPedidoPromocion.getDesc_promo());
	                    
	                    //preguntar si existe cupon descto.
	                    
	                    if(resumenPedidoPromocion.getPromo_codigo() == -2) {
	                    	promoDetallePedido.setPromo_fini(cddto.getFecha_ini()+" 00:00:00");
		    	            promoDetallePedido.setPromo_ffin(cddto.getFecha_fin()+" 00:00:00");
	                    } else {
	                    	promoDetallePedido.setPromo_fini(resumenPedidoPromocion.getFec_ini());
	                    promoDetallePedido.setPromo_ffin(resumenPedidoPromocion.getFec_fin());
	                    }
	                    
	                    promoDetallePedido.setPromo_tipo(resumenPedidoPromocion.getTipo_promo());

	                    
	                    double descuento_unitario = productosRelacionadosPromo.getProrrateo() / productosRelacionadosPromo.getCantidad();

	                    DetallePedidoDTO detallePedido = new DetallePedidoDTO();
	                    detallePedido.setId_producto(productosRelacionadosPromo.getId_producto());
	                    detallePedido.setPrecio_lista(productosRelacionadosPromo.getPrecio_lista());

	                    double precio = Math.floor(productosRelacionadosPromo.getPrecio_lista() - descuento_unitario);
	                   if(precio==0){
	                	   detallePedido.setPrecio(1);
	                   } else {
	                    detallePedido.setPrecio(precio);
	                   }

	                    double descto = 0.0;
	                    if (productosRelacionadosPromo.getPrecio_lista() > 0) {
	                        descto = (1 - (precio / productosRelacionadosPromo.getPrecio_lista())) * 100;
	                    }
	                    detallePedido.setDscto_item(descto);
	                    detallePedido.setDesc_pesos_item(descuento_unitario);

	                    logger.debug("ID PRODUCTO: " +productosRelacionadosPromo.getId_producto());
	                    logger.debug("CANTIDAD : " + productosRelacionadosPromo.getCantidad());
	                    logger.debug("PRECIO LISTA: " + productosRelacionadosPromo.getPrecio_lista());
	                    logger.debug("PRORRATEO: " + productosRelacionadosPromo.getProrrateo());
	                    logger.debug("DESCUENTO POR PROD: " + descuento_unitario);
	                    logger.debug("TASA DESCUENTO: " + descto);
	                    logger.debug("PRECIO: " + (precio));

	                    //Agrega las promociones a la tabla bo_promos_detped
	                    promoDetallePedido.setId_producto(productosRelacionadosPromo.getId_producto());
	                    promoDetallePedido.setPromo_dscto_porc(descto);
	                    ped2.getPromoDetallePedido().add(promoDetallePedido); //dao1.addPromoDetallePedido(promoDetallePedido);

	                    detped.add(detallePedido);
	                }
	            }
	            //]2012113avc

	            // sumarizacion de descuentos
	            logger.info("doInsPedido(New): Sumarizacion de descuentos");
	            Hashtable lst_detped = new Hashtable(); //<DetallePedidoDTO>
	            for (int i = 0; detped != null && i < detped.size(); i++) {
	                DetallePedidoDTO dp1 = (DetallePedidoDTO) detped.get(i);
	                DetallePedidoDTO detPed1 = null;
	                Long key = new Long(dp1.getId_producto());
	                if (lst_detped.containsKey(key)) {
	                    detPed1 = (DetallePedidoDTO) lst_detped.get(key);
	                    detPed1.setPrecio(detPed1.getPrecio() - dp1.getDesc_pesos_item());
	                    detPed1.setDscto_item(detPed1.getDscto_item() + dp1.getDscto_item());
	                } else {
	                    detPed1 = new DetallePedidoDTO();
	                    detPed1.setId_producto(key.longValue());
	                    double precio = dp1.getPrecio_lista() - dp1.getDesc_pesos_item();
	                    if(precio ==0){
	                    	detPed1.setPrecio(1);
	                    } else {
	                    detPed1.setPrecio(dp1.getPrecio_lista() - dp1.getDesc_pesos_item());
	                    }
	                    detPed1.setDscto_item(dp1.getDscto_item());
	                }
	                if (detPed1.getPrecio() < 0) {
	                    detPed1.setPrecio(0);
	                }

	                lst_detped.put(new Long(detPed1.getId_producto()), detPed1);
	            }

	            // Recorremos el listado de detalle de pedido para modificar el
	            // descuento por producto
	            logger.info("doInsPedido(New): Recorremos el listado de detalle de pedido para modifiar el descuento por producto");

	            Iterator iterator = lst_detped.values().iterator();
	            while (iterator.hasNext()) {
	                DetallePedidoDTO dp = (DetallePedidoDTO) iterator.next();
	                for (int k = 0; k < ped2.getProductos().size(); k++) {
	                    ExtProductosPedidoDTO producto = (ExtProductosPedidoDTO) ped2.getProductos().get(k);
	                    if (dp.getId_producto() == producto.getId_producto()) {
	                        producto.setPrecio(dp.getPrecio());
	                        producto.setDscto_item(dp.getDscto_item());
							//[2012113avc
	                        for (int l = 0; l < ped2.getPromoDetallePedido().size(); l++) {
	                            PromoDetallePedidoDTO promoDetallePedido = (PromoDetallePedidoDTO) ped2.getPromoDetallePedido().get(l);
	                            if (promoDetallePedido.getId_producto() == producto.getId_producto()) {
	                                producto.getPromoDetallePedido().add(promoDetallePedido);
	                            }
	                        }
	                        //]2012113avc
	                    }
	                }

	            }
							
	            logger.debug("Tamaño lista detalle pedido: " + lst_detped.size());

	            logger.info("doInsPedido(New): Actualiza pedido con totales");
	            recalcPedido(ped2, pedido.getTipo_doc());

	            /////INICIO TRANSACCION/////
	            //FIXME: Valida si la jornada de picking y de despacho tienen aun capacidad validad para soportar el nuevo pedido en la operacion.-
	            if(!dao2.isCapacidadDespachoValida(pedido.getId_jdespacho()) || !dao2.isCapacidadPickingValida(jor1.getId_jpicking(), (int) pedido.getCantidadProductos())){
	                throw new PedidosException("ERR_CAPACIDAD");
	            }
	            
	            logger.info("doInsPedido(New): Iniciamos la transaccion");
	            trx.begin();

	            logger.info("doInsPedido(New): Inserta encabezado del pedido");
	            idPedido = dao1.doInsEncPedido(ped2);
	            logger.info("doInsPedido(New): idPedido=" + idPedido);
	            if (ped2.getLogPedido() != null) {
	                logger.info("doInsPedido(New): Inserta log por descuento automatico");
	                ped2.getLogPedido().setId_pedido(idPedido);
	                dao1.addLogPedido(ped2.getLogPedido());
	            }

	            logger.info("doInsPedido(New): Agrega detalle");
	            for (int i = 0; i < ped2.getProductos().size(); i++) {
	                ExtProductosPedidoDTO producto = (ExtProductosPedidoDTO) ped2.getProductos().get(i);
	                producto.setId_pedido(idPedido);

	                //Obtiene el idDetalle
	                long idDetalle = dao1.agregaProductoPedido(producto);
	                producto.setId_detalle(idDetalle);
	                
	                for (int j = 0; j < producto.getPromoDetallePedido().size(); j++) {
	                //+20131023_JMCE correccion recorrido errone de lista para promocion ( ped2 --> producto)
	                    PromoDetallePedidoDTO promoDetallePedido = (PromoDetallePedidoDTO) producto.getPromoDetallePedido().get(j);
	               //-20131023_JMCE correccion recorrido errone de lista para promocion ( ped2 --> producto)     

	                    promoDetallePedido.setId_pedido(idPedido);
	                    promoDetallePedido.setId_detalle(idDetalle);
	                    dao1.addPromoDetallePedido(promoDetallePedido);
	                }
	            }

	            for (int i = 0; i < ped2.getTcpPedido().size(); i++) {
	                TcpPedidoDTO tcp = (TcpPedidoDTO) ped2.getTcpPedido().get(i);
	                tcp.setId_pedido(idPedido);
	                tcp.setId_tcp(dao1.setTcpPedido(tcp));

	                logger.info("doInsPedido(New): Agrega Tcp " + tcp.getId_tcp());
	                for (int j = 0; j < ped2.getCuponPedido().size(); j++) {
	                    CuponPedidoDTO cupon = (CuponPedidoDTO) ped2.getCuponPedido().get(j);

	                    if (cupon.getNro_tcp() == tcp.getNro_tcp()) {
	                        cupon.setId_tcp(tcp.getId_tcp());
	                        cupon.setId_pedido(idPedido);
	                        logger.info("doInsPedido(New): Agrega cupon " + tcp.getId_tcp());
	                        dao1.setCuponPedido(cupon);
	                    }
	                }
	            }

	            // 9. Reserva capacidad de despacho
	            /*
	             * se incrementa en 1, pues se mide el número de pedidos de la
	             * jornada de despacho
	             */
	            logger.info("doInsPedido(New): Reserva capacidad de despacho");
	            dao2.doOcupaCapacidadDespacho(pedido.getId_jdespacho(), 1);

	            // 10. toma capacidad de picking de la jornada de picking
	            // relacionada
	            logger.info("doInsPedido(New): Toma capacidad de picking de la jornada");
	            dao2.doOcupaCapacidadPicking(jor1.getId_jpicking(), (int) pedido.getCantidadProductos());
	            
	          //Agregamos datos al log del pedido creado	            
	            try{
		            LogPedidoDTO logRetiro = new LogPedidoDTO();
		            logRetiro.setUsuario("SYSTEM");
		            logRetiro.setId_pedido(idPedido);	                                   
		            logRetiro.setLog("[TIPO_DESPACHO] Tipo despacho creado por FO es "+ped2.getTipo_despacho()+" valor "+ped2.getCosto_despacho());                
		            dao1.addLogPedido(logRetiro);
		            
		            LogPedidoDTO logDespacho = new LogPedidoDTO();
		            logDespacho.setUsuario("SYSTEM");
		            logDespacho.setId_pedido(idPedido);	                                   
		            logDespacho.setLog("[ID_DESPACHO] Jornada despacho asigna por FO es "+pedido.getId_jdespacho());                
		            dao1.addLogPedido(logDespacho);
		            
		            LogPedidoDTO logPicking = new LogPedidoDTO();
		            logPicking.setUsuario("SYSTEM");
		            logPicking.setId_pedido(idPedido);	                                   
		            logPicking.setLog("[ID_JPICKING] Jornada picking asigna por FO es "+jor1.getId_jpicking()+" capacida:"+pedido.getCantidadProductos());                
		            dao1.addLogPedido(logPicking);
	            } catch (Exception e) {logger.info("ERROR AL LOGEAR DATOS DEL PEDIDO CREADO OP:"+idPedido);}
	            //FIn datos log del pedido

	            // 11. Inserta datos de la factura
	            if (pedido.getTipo_doc().equals("F")) {
	                logger.info("doInsPedido(New): Inserta datos factura");
	                ProcModFacturaDTO fac1 = new ProcModFacturaDTO();
	                fac1.setTipo_doc("F");
	                fac1.setId_pedido(idPedido);
	                fac1.setRazon(pedido.getFac_razon());
	                fac1.setRut(pedido.getFac_rut());
	                fac1.setDv(pedido.getFac_dv());
	                fac1.setDireccion(pedido.getFac_direccion());
	                fac1.setTelefono(pedido.getFac_fono());
	                fac1.setGiro(pedido.getFac_giro());
	                fac1.setComuna(pedido.getFac_comuna());
	                fac1.setCiudad(pedido.getFac_ciudad());

	                dao1.setModFactura(fac1);
	            }
	            // Finaliza la transacción
	            logger.info("doInsPedido(New): Finaliza la transaccion");
	            trx.end();

	        } catch (PedidosDAOException e) {
	            try {
	                trx.rollback();
	            } catch (DAOException e1) {
	                e1.printStackTrace();
	            }
	            e.printStackTrace();
	            throw new SystemException(e);
	        } catch (JornadasDAOException e) {
	            try {
	                trx.rollback();
	            } catch (DAOException e1) {
	                e1.printStackTrace();
	            }
	            e.printStackTrace();
	            throw new SystemException(e);
	        } catch (ClientesDAOException e) {
	            try {
	                trx.rollback();
	            } catch (DAOException e1) {
	                e1.printStackTrace();
	            }
	            e.printStackTrace();
	            throw new SystemException(e);
	        } catch (DAOException e) {
	            try {
	                trx.rollback();
	            } catch (DAOException e1) {
	                e1.printStackTrace();
	            }
	            e.printStackTrace();
	            throw new SystemException(e);
	        } catch (Exception e) {
	            try {
	                trx.rollback();
	            } catch (DAOException e1) {
	                e1.printStackTrace();
	            }
	            e.printStackTrace();
	            throw new SystemException(e);
	        }

	        return idPedido;
	    }

	    /**
	     *  
	     */
	    private void recalcPedido(ExtPedidoDTO ped2, String tipoDoc) {
	        logger.info("recalcPedido(New): Actualiza pedido con totales");

	        long monto = 0;
	        long cant_prods = 0;
	        long totConImpuesto = 0;
	        for (int i = 0; i < ped2.getProductos().size(); i++) {
	            ExtProductosPedidoDTO producto = (ExtProductosPedidoDTO) ped2.getProductos().get(i);
	            monto += Math.round(producto.getPrecio() * producto.getCant_solic());
	            cant_prods += producto.getCant_solic();
	            if (producto.getImpuesto() != null)
	                totConImpuesto += Math.round(producto.getPrecio() * producto.getCant_solic() * producto.getImpuesto().doubleValue());
	            else
	                totConImpuesto += Math.round(producto.getPrecio() * producto.getCant_solic());
	        }
	        ped2.setMonto(monto);
	        ped2.setCant_prods(cant_prods);
	        if (tipoDoc.equals("F"))
	            ped2.setMonto_reservado(ped2.getCosto_despacho() + totConImpuesto);
	        else
	            ped2.setMonto_reservado(ped2.getCosto_despacho() + monto);

	    }

	    /**
	     * @param pedido
	     * @throws PedidosException
	     */
	    private void validate(ProcInsPedidoDTO pedido) throws PedidosException {
	        //      1. Valida campos obligatorios del pedido
	        if (pedido.getId_cliente() == 0)
	            throw new PedidosException("Campo id_cliente es incorrecto: " + pedido.getId_cliente());

	        /*
	         * if (pedido.getDir_id() == 0) throw new PedidosException("Campo dir_id
	         * es incorrecto:" + pedido.getDir_id());
	         */
	        if (pedido.getMedio_pago().equals(""))
	            throw new PedidosException("Campo id_cliente es incorrecto: " + pedido.getMedio_pago());

	    }

	    /**
	     * Se agregan parametros para aplicar cupon de descuento (cddto, cuponProds) cdd
	     * @param ped2
	     * @param idLocal
	     * @param dao1
	     * @param colaborador
	     * @param cddto
	     * @param cuponProds
	     * @return
	     * @throws PedidosException
	     * @throws SystemException
	     */
	    public List doRecalculoPromocion(ExtPedidoDTO ped2, long idLocal, PedidosDAO dao1, Long colaborador, CuponDsctoDTO cddto, List cuponProds) throws PedidosException, SystemException { //<ResumenPedidoPromocionDTO>
	        logger.info("doRecalculoPromocion(New): Aplica los descuentos generados por las promociones al detalle de pedido");
	        int id_canal = Constantes.CANAL_PROMOCIONES;
	        char flag_cant = Constantes.PROMO_RECALCULO_FLAG_PROD_CANTIDAD; //Flag
	        // producto cantidad 'C' o producto 'P' Pesable
	        char flag_venta = Constantes.PROMO_RECALCULO_FLAG_VENTA;// Venta o
	        // anulacion
	        int fpago = 0;
	        int cuotas = 0;
	        logger.debug("inicia doRecalculoPromocion");

	        try {
	            //Se inicializa ambiente de libreria promociones
	            //[2012113avc
	            PromoCtrl lib = new PromoCtrl((int) idLocal, id_canal, colaborador);
				//]2012113avc
	            //Se setea el listado de tcp (al menos 1 debe ser seteado)
	            List tcpClienteList = new ArrayList();
	            TcpClienteDTO tpcCliente = null;
	            /**
	             * el primer tcp vacio, se usan 2000 para que siempre entregue todos
	             * los productos la cantidad es el numero que limita la aplicacion
	             * de prorrateos a n productos siendo n el numero - ESTO ES
	             * TEMPORAL, A FUTURO NO DEBERIA EXISTIR
	             */

	            //genera listados de tcps, y busca cupones en caso de que existan
	            // revisa cual es su tcp y le inserta con cupon
	            if ((ped2.getTcpPedido() == null) || (ped2.getTcpPedido().size() == 0)) {
	                tpcCliente = new TcpClienteDTO(0, "", 2000, 0);
	                tcpClienteList.add(tpcCliente);
	            } else {
	                for (int i = 0; i < ped2.getTcpPedido().size(); i++) {
	                    TcpPedidoDTO tcpPedido = (TcpPedidoDTO) ped2.getTcpPedido().get(i);
	                    //consultar cupon y verificar el id_tcp si es identico se
	                    // debe incluir el numero de cupon
	                    String cupon = "";
	                    for (int j = 0; j < ped2.getCuponPedido().size(); j++) {
	                        CuponPedidoDTO cuponPedido = (CuponPedidoDTO) ped2.getCuponPedido().get(j);
	                        if (cuponPedido.getId_tcp() == tcpPedido.getId_tcp()) {
	                            cupon = cuponPedido.getNro_cupon();
	                        }
	                    }
	                    tpcCliente = new TcpClienteDTO(tcpPedido.getNro_tcp(), cupon, tcpPedido.getCant_max(), 0);
	                    logger.debug("TCP " + i + ": (numero=" + tpcCliente.getTcp() + ", cupon" + tpcCliente.getCupon() + ", max:" + tpcCliente.getMax()
	                            + ", used:" + tpcCliente.getCant() + ")");
	                    tcpClienteList.add(tpcCliente);
	                }
	            }

	            //recoge listado de productos del pedido
	            logger.debug("obtiene detalle del pedido:" + ped2.getProductos().size());

	            for (int i = 0; i < ped2.getProductos().size(); i++) {
	                ExtProductosPedidoDTO productosPedido = (ExtProductosPedidoDTO) ped2.getProductos().get(i);
	                PrioridadPromosDTO prioridadPromos = (PrioridadPromosDTO) dao1.getPromosPrioridadProducto(productosPedido.getId_producto(), (int) idLocal);

	                //genera un nuevo reigstro para el calculo
	                ProductoDTO producto = new ProductoDTO();

	                //busca las promociones asociadas por prioridad promocional
	                // cada producto
	                List promocion = new ArrayList();

	                logger.debug(i + ".-detalle del pedido : tiene promociones " + prioridadPromos.getCodPromoEvento() + " ,"
	                        + prioridadPromos.getCodPromoPeriodica() + " ," + prioridadPromos.getCodPromoNormal());

	                //si existen promociones las adjunta al registro de productos
	                if (prioridadPromos.getCodPromoEvento() > 0) {
	                    promocion.add(String.valueOf(prioridadPromos.getCodPromoEvento()));
	                    logger.debug("Promocion:" + prioridadPromos.getCodPromoEvento());
	                }
	                if (prioridadPromos.getCodPromoPeriodica() > 0) {
	                    promocion.add(String.valueOf(prioridadPromos.getCodPromoPeriodica()));
	                    logger.debug("Promocion:" + prioridadPromos.getCodPromoPeriodica());
	                }
	                if (prioridadPromos.getCodPromoNormal() > 0) {
	                    promocion.add(String.valueOf(prioridadPromos.getCodPromoNormal()));
	                    logger.debug("Promocion:" + prioridadPromos.getCodPromoNormal());
	                }

	                //ingresa promociones del producto
	                producto.setPromocion(promocion);

	                //codigo de barra
	                long ean13long = Long.parseLong(productosPedido.getCod_barra());
	                producto.setCodigo(ean13long);
	                
// inicio cdd	                
	                producto.setIdProducto(productosPedido.getId_producto());
	                producto.setRubro(productosPedido.getRubro());
// fin cdd       

	                // Que va en departamento la seccion de la categoria SAP
	                producto.setDepto(Integer.parseInt(productosPedido.getSeccion())); //Integer.parseInt(detPedido.getSeccion_sap()));

	                // La cantidad va multiplicada por 1000 si es pesable
	                int cant_solicitada = 0;
	                // flag pesable si es SI => 'P'=Pesable o 'C'=Cantidad
	                if ((productosPedido.getPesable() != null) && (productosPedido.getPesable().equals(Constantes.INDICADOR_SI))) {
	                    cant_solicitada = (int) Math.round(productosPedido.getCant_solic() * 1000);
	                    flag_cant = Constantes.PROMO_RECALCULO_FLAG_PROD_PESABLE;
	                } else {
	                    cant_solicitada = (int) Math.round(productosPedido.getCant_solic());
	                    flag_cant = Constantes.PROMO_RECALCULO_FLAG_PROD_CANTIDAD;
	                }
	                producto.setCantidad(cant_solicitada);

	                //Flag producto cantidad = C o pesable = P
	                producto.setFlagCantidad(flag_cant);

	                //Flag Venta=V Anulacion=A
	                producto.setFlagVenta(flag_venta);

	                //Precio del Producto
	                producto.setPrecio(Math.round(productosPedido.getCant_solic() * productosPedido.getPrecio_lista()));

	                logger.debug("inserta producto :  " + productosPedido.getId_producto() + " barra=" + producto.getCodigo() + " depto=" + producto.getDepto()
	                        + " cantidad=" + producto.getCantidad() + " flag_cantidad=" + producto.getFlagCantidad() + " flag_venta=" + producto.getFlagVenta()
	                        + " precio_lista=" + producto.getPrecio() + " monto p(" + productosPedido.getPrecio_lista() + ")x q(" + productosPedido.getCant_solic()
	                        + ")=" + Math.round(productosPedido.getCant_solic() * productosPedido.getPrecio_lista()) + " promo=" + producto.getPromocion());

	                //se insertan productos a la libreria
	                lib.insertaProducto(producto);
	            }
	            logger.debug("productos insertados");

	            //////////////////////////////////////////////////////////////////////

	            //** PROMOCIONES ** Esto hay que hacerlo de nuevo y sacar el dato
	            // de las nuevas tablas... ademas ver donde mas lo hace asi.

	            String ped_mp = "";
	            ped_mp = ped2.getMedio_pago();
	            cuotas = ped2.getN_cuotas();
	            logger.debug("Medio de pago del pedido: " + ped_mp + " , cuotas" + ped2.getN_cuotas());

	            // Rescatamos el medio de pago de la promocion
	            PromoMedioPagoDTO pr_mp = dao1.getPromoMedioPagoByMPJmcl(ped_mp, ped2.getN_cuotas());
	            if (pr_mp.getMp_promo() != null) {
	                fpago = Integer.parseInt(pr_mp.getMp_promo());
	            }
	            // fpago hay que traducirlos a codificacion POS
	            // usar una tabla de normalizacion de medios de pago

	            logger.debug("antes calculo descuentos fpago=" + fpago + " cuotas=" + cuotas);

// inicio cdd            
	            long dcto = lib.calculaDescuentosNew(ped_mp, cuotas, tcpClienteList, cddto, cuponProds);
// fin cdd
	            
	            logger.debug("despues calculo descuentos");

	            List p_prod = lib.getProrrateoProducto(); //se obtiene el listado
	            // de productos
	            // prorrateados
	            List p_sec = lib.getProrrateoSeccion(); //se obtiene el listaod de
	            // secciones prorrateadas
	            List p_tcps = lib.getListadoTcp(); //se obtiene el listado de tcps
	            // utilizados
	            logger.debug("-----------------");
	            logger.debug("DESCUENTO = " + dcto);
	            logger.debug("PRORR PRO= " + p_prod.size());
	            logger.debug("PRORR SEC= " + p_sec.size());
	            logger.debug("PRORR TCP= " + p_tcps.size());
	            logger.debug("-----------------");

	            List lst_prod_promo = new ArrayList();
	            lst_prod_promo.addAll(resultado_productos(p_prod, ped2.getProductos(), dao1));
	            lst_prod_promo.addAll(resultado_seccion(p_sec, ped2.getProductos(), dao1));

	            logger.debug("RESULTADO TCPS=" + p_tcps.size());
	            for (int i = 0; i < p_tcps.size(); i++) {
	                TcpClienteDTO tcp = (TcpClienteDTO) p_tcps.get(i);
	                for (int j = 0; j < ped2.getTcpPedido().size(); j++) {
	                    TcpClienteDTO tcpPedido = (TcpClienteDTO) ped2.getTcpPedido().get(j);
	                    if (tcp.getCupon() == tcpPedido.getCupon()) {
	                        tcpPedido.setCant(tcp.getCant());
	                    }
	                }

	                logger.debug(i + " tcp:" + tcp.getTcp());
	                logger.debug(i + " cupon:" + tcp.getCupon());
	                logger.debug(i + " max:" + tcp.getMax());
	                logger.debug(i + " cant:" + tcp.getCant());

	            }
	            logger.debug("termina debug resultados");
	            return lst_prod_promo;
	        } catch (PedidosDAOException ex) {
	            logger.error("No puede obtener datos de promociones :" + ex);
	            ex.printStackTrace();
	            throw new PedidosException("Error en PedidosDAOException", ex);
	        } catch (Exception e) {
	            logger.error("Error en libreria de promociones:" + e.getMessage());
	            e.printStackTrace();
	            throw new PedidosException(e);
	        }
	    }

	    /**
	     * @param lst_detped
	     * @param dao
	     * @throws PedidosDAOException
	     *  
	     */
	    private List resultado_seccion(List prorrateoPromocionProductoList, List productosPedidoList, PedidosDAO dao) throws PedidosDAOException {
	        List result = new ArrayList();
	        for (int i = 0; i < prorrateoPromocionProductoList.size(); i++) {
	            ProrrateoPromocionSeccionDTO prorrateoPromocion = (ProrrateoPromocionSeccionDTO) prorrateoPromocionProductoList.get(i);
	            PromocionDTO promocion = dao.getPromocionByCodigo(prorrateoPromocion.getCodigo());
	            List lst_prod_prorrat = prorrateoPromocion.getListadoSeccion();

	            ResumenPedidoPromocionDTO resumenPedidoPromocion = new ResumenPedidoPromocionDTO();
	            resumenPedidoPromocion.setId_promocion(promocion.getId_promocion());
	            resumenPedidoPromocion.setPromo_codigo(prorrateoPromocion.getCodigo());
	            resumenPedidoPromocion.setDesc_promo(prorrateoPromocion.getDescripcion());
	            resumenPedidoPromocion.setFec_ini(promocion.getFini());
	            resumenPedidoPromocion.setFec_fin(promocion.getFfin());
	            resumenPedidoPromocion.setMonto_descuento(prorrateoPromocion.getDescuento());
	            resumenPedidoPromocion.setTipo_promo(promocion.getTipo_promo());

	            String str_prod_relacionados = "";
	            List productosRelacionadosPromoList = new ArrayList();
	            for (int w = 0; w < lst_prod_prorrat.size(); w++) {
	                ProrrateoSeccionDTO prorrateoSeccion = (ProrrateoSeccionDTO) lst_prod_prorrat.get(w);

	                for (int k = 0; k < prorrateoSeccion.getListadoProductos().size(); k++) {
	                    ProrrateoProductoSeccionDTO pps = (ProrrateoProductoSeccionDTO) prorrateoSeccion.getListadoProductos().get(k);

	                    long id_producto_promo = -1;
	                    String descr_prod_promo = "";
	                    String ean13_promo = "";
	                    double precio_lista = 0.0;
	                    double cantidad = 0;
	                    long prorrateo = 0;

	                    for (int z = 0; z < productosPedidoList.size(); z++) {
	                        ExtProductosPedidoDTO productosPedido = (ExtProductosPedidoDTO) productosPedidoList.get(z);
	                        if (productosPedido.getCod_barra().equals(String.valueOf(pps.getCodigo()))) {
	                            id_producto_promo = productosPedido.getId_producto();
	                            descr_prod_promo = productosPedido.getDescripcion();
	                            ean13_promo = productosPedido.getCod_barra();
	                            precio_lista = productosPedido.getPrecio_lista();
	                            cantidad = productosPedido.getCant_solic();
	                            prorrateo = pps.getProrrateoSeccion();
	                        }

	                        //agrega el producto encontrado como producto
	                        // relacionado
	                        if (id_producto_promo > 0) {
	                            //ojo que va el listado detallado, es posible que
	                            // se necesite la agrupación
	                            //por id_detalle al mostrar resultados

	                            boolean det_found = false;
	                            if (productosRelacionadosPromoList.size() > 0) {
	                                Iterator x = productosRelacionadosPromoList.iterator();
	                                while (x.hasNext()) {
	                                    ProductosRelacionadosPromoDTO mco = (ProductosRelacionadosPromoDTO) x.next();
	                                    //si el producto existe lo sumariza
	                                    if (mco.getId_producto() == id_producto_promo) {
	                                        //agrega los prorrateos
	                                        logger.debug("producto:" + id_producto_promo + " prorrateo antes=" + mco.getProrrateo());
	                                        mco.setProrrateo(prorrateo);
	                                        logger.debug("producto:" + id_producto_promo + " suma prorrateo=" + mco.getProrrateo());
	                                        det_found = true;
	                                    }
	                                }
	                            }
	                            //si no lo encuentra lo agrega al agregar el
	                            // producto de la seccion por primera vez
	                            //se le agrega el prorrateo de promociones al
	                            // producto
	                            // ** PROMOCIONES ** verificar para que se usa
	                            // 'prod_relacionados' y ademas ver si precio_lista
	                            // puede venir con 0.0
	                            if (!det_found) {
	                                //TODO: A la espera de la sumarizacion con el
	                                // resultado de secciones
	                                ProductosRelacionadosPromoDTO prod = new ProductosRelacionadosPromoDTO();
	                                prod.setId_producto(id_producto_promo);
	                                prod.setDescripcion(descr_prod_promo);
	                                prod.setEan13(ean13_promo);
	                                // el prorrateo al producto solo se agrega si el
	                                // producto aparece la primera vez en la
	                                // lista para esta promocion
	                                prod.setProrrateo(prorrateo);
	                                prod.setPrecio_lista(precio_lista);
	                                prod.setCantidad(cantidad);
	                                logger.debug("producto relacionado nuevo =" + id_producto_promo + " " + descr_prod_promo);
	                                productosRelacionadosPromoList.add(prod);
	                                //tambien sumariza las descripciones de
	                                // productos para el resumen
	                                if (w > 0)
	                                    str_prod_relacionados += ",<br>";
	                                str_prod_relacionados += descr_prod_promo;
	                            }
	                        } else {
	                            logger.warn("el producto ingresado al calculo no ha sido encontrado barra:" + pps.getCodigo());
	                        }
	                    }
	                }
	            }
	            resumenPedidoPromocion.setDesc_prod(str_prod_relacionados);
	            resumenPedidoPromocion.setProd_relacionados(productosRelacionadosPromoList);
	            result.add(resumenPedidoPromocion);
	        }
	        return result;
	    }

	    /**
	     * @param p_prod
	     * @param productosPedidoList
	     * @param dao
	     * @return
	     * @throws PedidosDAOException
	     * @throws PedidosDAOException
	     *  
	     */
	    private List resultado_productos(List prorrateoPromocionProductoList, List productosPedidoList, PedidosDAO dao) throws PedidosDAOException {
	        List result = new ArrayList();
	        for (int i = 0; i < prorrateoPromocionProductoList.size(); i++) {
	            ProrrateoPromocionProductoDTO prorrateoPromocion = (ProrrateoPromocionProductoDTO) prorrateoPromocionProductoList.get(i);
	            //[avc20121109
	            ResumenPedidoPromocionDTO resumenPedidoPromocion = new ResumenPedidoPromocionDTO();

	            if (prorrateoPromocion.getTipo().equals("C")) {
	                resumenPedidoPromocion.setPromo_codigo(prorrateoPromocion.getCodigo());
	                resumenPedidoPromocion.setDesc_promo(prorrateoPromocion.getDescripcion());
	                resumenPedidoPromocion.setMonto_descuento(prorrateoPromocion.getDescuento());
	                resumenPedidoPromocion.setId_promocion(-1);
	            } else {
	                PromocionDTO promocion = dao.getPromocionByCodigo(prorrateoPromocion.getCodigo());

	                resumenPedidoPromocion.setId_promocion(promocion.getId_promocion());
	                resumenPedidoPromocion.setPromo_codigo(prorrateoPromocion.getCodigo());
	                resumenPedidoPromocion.setDesc_promo(prorrateoPromocion.getDescripcion());
	                resumenPedidoPromocion.setFec_ini(promocion.getFini());
	                resumenPedidoPromocion.setFec_fin(promocion.getFfin());
	                resumenPedidoPromocion.setMonto_descuento(prorrateoPromocion.getDescuento());
	                resumenPedidoPromocion.setTipo_promo(promocion.getTipo_promo());
	            }
	            //]20121109avc

	            String str_prod_relacionados = "";
	            List productosRelacionadosPromoList = new ArrayList();
	            for (int w = 0; w < prorrateoPromocion.getListadoProductos().size(); w++) {
	                ProrrateoProductoDTO prorrateoProducto = (ProrrateoProductoDTO) prorrateoPromocion.getListadoProductos().get(w);
	                long id_producto_promo = -1;
	                String descr_prod_promo = "";
	                String ean13_promo = "";
	                double precio_lista = 0.0;
	                double cantidad = 0;
	                long prorrateo = 0;
	                for (int z = 0; z < productosPedidoList.size(); z++) {
	                    ExtProductosPedidoDTO productosPedido = (ExtProductosPedidoDTO) productosPedidoList.get(z);
	                    if (productosPedido.getCod_barra().equals(String.valueOf(prorrateoProducto.getCodigo()))) {
	                        id_producto_promo = productosPedido.getId_producto();
	                        descr_prod_promo = productosPedido.getDescripcion();
	                        ean13_promo = productosPedido.getCod_barra();
	                        precio_lista = productosPedido.getPrecio_lista();
	                        cantidad = productosPedido.getCant_solic();
	                        prorrateo = prorrateoProducto.getProrrateo();
	                        break;
	                    }
	                }

	                boolean found = false;
	                if (id_producto_promo > 0) {
	                    Iterator x = productosRelacionadosPromoList.iterator();
	                    while (x.hasNext()) {
	                        ProductosRelacionadosPromoDTO mco = (ProductosRelacionadosPromoDTO) x.next();
	                        //si el producto existe lo sumariza
	                        if (mco.getId_producto() == id_producto_promo) {
	                            //agrega los prorrateos
	                            logger.debug("producto:" + id_producto_promo + " prorrateo antes=" + mco.getProrrateo());
	                            mco.setProrrateo(mco.getProrrateo() + prorrateo);
	                            logger.debug("producto:" + id_producto_promo + " suma prorrateo=" + mco.getProrrateo());
	                            found = true;
	                        }
	                    }

	                    if (!found) {
	                        ProductosRelacionadosPromoDTO productosRelacionadosPromo = new ProductosRelacionadosPromoDTO();
	                        productosRelacionadosPromo.setCantidad(cantidad);
	                        productosRelacionadosPromo.setDescripcion(descr_prod_promo);
	                        productosRelacionadosPromo.setEan13(ean13_promo);
	                        //productosRelacionadosPromo.setId_detalle();
	                        productosRelacionadosPromo.setId_producto(id_producto_promo);
	                        productosRelacionadosPromo.setPrecio_lista(precio_lista);
	                        productosRelacionadosPromo.setProrrateo(prorrateo);
	                        productosRelacionadosPromoList.add(productosRelacionadosPromo);

	                        if (w > 0)
	                            str_prod_relacionados += ",<br>";
	                        str_prod_relacionados += descr_prod_promo;
	                    }
	                }
	            }
	            resumenPedidoPromocion.setProd_relacionados(productosRelacionadosPromoList);
	            resumenPedidoPromocion.setDesc_prod(str_prod_relacionados);
	            result.add(resumenPedidoPromocion);

	        }
	        return result;
	    }
	    
	    
	    /**
	     * 
	     * @param idPedido
	     * @return
	     * @throws PedidosException
	     */
	    public PedidoDTO getValidaCuponYPromocionPorIdPedido( long idPedido ) throws PedidosException {

	    	PedidoDTO pedido = null;

			JdbcPedidosDAO dao = ( JdbcPedidosDAO ) DAOFactory.getDAOFactory( DAOFactory.JDBC ).getValidaCuponYPromocionPorIdPedido();
		    
			try {
				
				pedido = dao.getValidaCuponYPromocionPorIdPedido( idPedido );
				
				    
		        } catch ( PedidosDAOException e ) {
		            
		        	logger.debug( "Problema :" + e.getMessage() );
		            throw new PedidosException( e );
		        
		        }
			
		    
			return pedido;
		    
	    }
	    public int getIdCuponByIdPedido( long idPedido ) throws PedidosException {
			int idCupon = 0;
	    	JdbcPedidosDAO dao = ( JdbcPedidosDAO ) DAOFactory.getDAOFactory( DAOFactory.JDBC ).getValidaCuponYPromocionPorIdPedido();
			try {
				idCupon = dao.getIdCuponByIdPedido(idPedido);
		        } catch ( PedidosDAOException e ) {
		        	logger.debug( "Problema :" + e.getMessage() );
		            throw new PedidosException( e );
		        }
			return idCupon;
	    }
    
	    /**
	     * 
	     * @param id_pedido
	     * @return
	     * @throws PedidosException
	     */
	    public List getDescuentosAplicados( long id_pedido ) throws PedidosException {

	        JdbcPedidosDAO dao = ( JdbcPedidosDAO ) DAOFactory.getDAOFactory( DAOFactory.JDBC ).getDescuentosAplicados();
	        
			try {
				
		        return dao.getTiposDescuentosAplicados( id_pedido );
				
	        } catch ( PedidosDAOException e ) {
	        	
	            logger.debug("Problema :" + e.getMessage());
	            throw new PedidosException(e);
	        
	        }
	        
		} 
	    
	    
	    /**(Catalogo Externo) - NSepulveda
	     * Metodo que se encarga de recuperar la categoria padre, intermedia y terminal del ultimo producto 
	     * agregado al carro de compras.
	     * @param idProducto <code>long<code> Identificador del producto.
	     * @return <code>ProductoCatalogoExternoDTO</code>
	     * @throws PedidosException
	     * @throws ServiceException
	     * @throws SystemException
	     */
	    public ProductoCatalogoExternoDTO getCatUltimoProductoCatalogoExterno(long idProducto) throws PedidosException, 
				ServiceException, SystemException {
	    	
			JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			
			try{
				return dao.getCatUltimoProductoCatalogoExterno(idProducto);
			}catch (PedidosDAOException ex) {
			    logger.debug("Problema :" + ex);
			    throw new SystemException("Error no controlado", ex);
			}
		}
	    
	    /**(Catalogo Externo) - NSepulveda
	     * Metodo que se encarga de validar los productos solicitados desde el catalogo externo 
	     * para posteriormente agregarlos al carro de compras. 
	     * @param listCatalogoExt <code>List</code> Listado de productos a validar.
	     * @return <code>List</code> Listado de <code>ProductoCatalogoExternoDTO</code> con los productos validados.
	     * @throws PedidosException
	     * @throws ServiceException
	     * @throws SystemException
	     */
	    public List getValidacionProductosCatalogoExterno(List listCatalogoExt) throws PedidosException, ServiceException, SystemException {
	    	JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
	    	
	    	try{
	    		return dao.getValidacionProductosCatalogoExterno(listCatalogoExt);
	    	} catch (PedidosDAOException ex) {
	            logger.debug("Problema :" + ex);
	            throw new SystemException("Error no controlado", ex);
	        }
	    }
	    
	    public int actualizarCapacidadOcupadaPicking(long id_jpicking) throws JornadasDAOException {
	    	int registrosActualizados = 0;
	    	JdbcJornadasDAO daoJ = (JdbcJornadasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getJornadasDAO();
	    	try {
				daoJ.actualizarCapacidadOcupadaPicking(id_jpicking);
			} catch (JornadasDAOException e) {
				throw new JornadasDAOException(e);
			}
	    	return registrosActualizados;
	    }
	    
	    
	    //Para traer la ultima direccion valida para asocias al cliente al iniciar sesion, en funcion del ultimo pedido 
	    public DireccionMixDTO getDireccionIniciaSessionCliente(long idCliente)  throws PedidosException{	    	

	    	DireccionMixDTO oDireccionMixDTO = new DireccionMixDTO();

	    	try {

	    		PedidosService oPedidosService = new PedidosService();
	    		ClientesService cli_service = new ClientesService();

	    		PedidoDTO ultPedido = oPedidosService.getUltimoIdPedidoCliente(idCliente);	

	    		if ( ultPedido != null && ultPedido.getId_pedido() != 0 ) {

	    			List dirPed = cli_service.clienteAllDireccionesConCoberturaFO(idCliente);

	    			for( int k = 0; k < dirPed.size(); k++ ) {
	    				DireccionesDTO dir2 = (DireccionesDTO) dirPed.get(k);
	    				if(ultPedido.getId_zona() == dir2.getZona_id()){
	    					oDireccionMixDTO.setSes_zona_id(ultPedido.getId_zona());
	    					oDireccionMixDTO.setSes_loc_id(ultPedido.getId_local());
	    					oDireccionMixDTO.setSes_dir_id(ultPedido.getDir_id());
	    					oDireccionMixDTO.setSes_dir_alias(ultPedido.getDir_calle());                       
	    					oDireccionMixDTO.setSes_forma_despacho(ultPedido.getTipo_despacho());
	    					oDireccionMixDTO.setDireccionMix(true);
	    					break;
	    				}
	    			}	

	    			if(!oDireccionMixDTO.isDireccionMix()){ 
	    				DireccionesDTO dir3 = (DireccionesDTO) cli_service.clienteAllDireccionesConCoberturaFO(idCliente).get(0);
	    				oDireccionMixDTO.setSes_zona_id( dir3.getZona_id());
	    				oDireccionMixDTO.setSes_loc_id( dir3.getLoc_cod());
	    				oDireccionMixDTO.setSes_dir_id( dir3.getId());
	    				oDireccionMixDTO.setSes_dir_alias( dir3.getAlias());                        
	    				oDireccionMixDTO.setSes_forma_despacho("N"); 
	    				oDireccionMixDTO.setDireccionMix(true);
	    			}

	    		} else {

	    			List listDir = cli_service.clienteAllDireccionesConCoberturaFO(idCliente);

	    			if ( listDir.size() >0) {
	    				DireccionesDTO dir = (DireccionesDTO) listDir.get(0);                     	

	    				oDireccionMixDTO.setSes_zona_id(dir.getZona_id());
	    				oDireccionMixDTO.setSes_loc_id(dir.getLoc_cod());
	    				oDireccionMixDTO.setSes_dir_id(dir.getId());
	    				oDireccionMixDTO.setSes_dir_alias(dir.getAlias());                    
	    				oDireccionMixDTO.setSes_forma_despacho("N");
	    				oDireccionMixDTO.setDireccionMix(true);

	    			} else {
	    				oDireccionMixDTO.setSes_destination("UltComprasForm?opcion=1");
	    				oDireccionMixDTO.setDireccionMix(false);
	    			}
	    		}

	    	} catch (Exception e) {
	    		throw new PedidosException("Error no controlado", e);
	    	}

	    	return oDireccionMixDTO;
	    }
	    
	    /**
		 * Ingresa una nueva compra histórica WEB.
		 * 
		 * @param nombre		Nombre de la compra histórica
		 * @param cliente_id	Identificador único del cliente
		 * @param unidades		Cantidad de productos de la compra
		 * @param pedido_id		Identificador único del pedido
		 * @throws PedidosException
		 */
		public void setCompraHistorica(String nombre, long cliente_id, long unidades, long pedido_id) throws PedidosException {

			try {
				JdbcPedidosDAO pedidosDAO = (JdbcPedidosDAO) DAOFactory
				 .getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

				pedidosDAO.setCompraHistorica(nombre, cliente_id, unidades, pedido_id);

			} catch ( NullPointerException ex ) {
				logger.error("Problema con null en los datos (setCompraHistorica)" );
				throw new PedidosException(ex);
			} catch (PedidosDAOException ex) {
				logger.error("Problema (setCompraHistorica)", ex);
				throw new PedidosException(ex);
			}
		}
		public long setCompraHistoricaForMobile(String nombre, long cliente_id, ArrayList productos) throws PedidosException {

			try {
				JdbcPedidosDAO pedidosDAO = (JdbcPedidosDAO) DAOFactory
				 .getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

				return pedidosDAO.setCompraHistoricaForMobile(nombre, cliente_id, productos);

			} catch ( NullPointerException ex ) {
				logger.error("Problema con null en los datos (setCompraHistorica)" );
				throw new PedidosException(ex);
			} catch (PedidosDAOException ex) {
				logger.error("Problema (setCompraHistorica)", ex);
				throw new PedidosException(ex);
			}
		}

		/**
		 * Actualiza datos de una compra histórica WEB.
		 * 
		 * @param nombre		Nombre de la compra histórica
		 * @param id_cliente	Identificador único del cliente
		 * @param unidades		Cantidad de productos de la compra
		 * @param pedido_id		Identificador único del pedido
		 * @param id_lista		Identificador único de la compra histórica a modificar
		 * @throws PedidosException
		 */
		public void updateCompraHistorica(String nombre, long id_cliente, long unidades, long pedido_id, long id_lista) throws PedidosException {

			try {
				JdbcPedidosDAO pedidosDAO = (JdbcPedidosDAO) DAOFactory
				 .getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

				pedidosDAO.updateCompraHistorica(nombre, id_cliente, unidades, pedido_id, id_lista);

			} catch ( NullPointerException ex ) {
				logger.error("Problema con null en los datos (updateCompraHistorica)" );
				throw new PedidosException(ex);
			} catch (PedidosDAOException ex) {
				logger.error("Problema (updateCompraHistorica)", ex);
				throw new PedidosException(ex);
			}
		}

		/**
		 * Actualiza el ranking de ventas de los productos de un pedido 
		 * 
		 * @param carro_compras			Lista de DTO con datos del carro de compras
		 * @throws PedidosException
		 */	
		public void updateRankingVentas( long id_cliente ) throws PedidosException {

			try {
				JdbcPedidosDAO pedidosDAO = (JdbcPedidosDAO) DAOFactory
				 .getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

				pedidosDAO.updateRankingVentas( id_cliente );

			} catch ( NullPointerException ex ) {
				logger.error("Problema con null en los datos (updateCompraHistorica)" );
				throw new PedidosException(ex);
			} catch (PedidosDAOException ex) {
				logger.error("Problema (updateCompraHistorica)", ex);
				throw new PedidosException(ex);
			}
		}	

		/**
		 * Actualiza el nombre en una compra histórica web.
		 * 
		 * @param nombre		Nombre de la compra histórica
		 * @param id_cliente	Identificador único del cliente
		 * @throws PedidosException
		 */
		public void updateNombreCompraHistorica(String nombre, long id_cliente) throws PedidosException {

			try {
				JdbcPedidosDAO pedidosDAO = (JdbcPedidosDAO) DAOFactory
				 .getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

				pedidosDAO.updateNombreCompraHistorica(nombre, id_cliente);

			} catch ( NullPointerException ex ) {
				logger.error("Problema con null en los datos (updateNombreCompraHistorica)" );
				throw new PedidosException(ex);
			} catch (PedidosDAOException ex) {
				logger.error("Problema (updateNombreCompraHistorica)", ex);
				throw new PedidosException(ex);
			}
		}
	    
	    
	    public boolean esPrimeraCompra(long id_cliente) throws cl.bbr.jumbocl.pedidos.exceptions.PedidosException {

	        try {
	            cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO pedidosDAO = (cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO) cl.bbr.jumbocl.pedidos.dao.DAOFactory
	             .getDAOFactory(cl.bbr.jumbocl.pedidos.dao.DAOFactory.JDBC).getPedidosDAO();

	            return pedidosDAO.esPrimeraCompra(id_cliente);

	        } catch ( NullPointerException ex ) {
	            logger.error("Problema con null en los datos (updateNombreCompraHistorica)" );
	            throw new cl.bbr.jumbocl.pedidos.exceptions.PedidosException(ex);
	        } catch (cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException ex) {
	            logger.error("Problema (updateNombreCompraHistorica)", ex);
	            throw new cl.bbr.jumbocl.pedidos.exceptions.PedidosException(ex);
	        }
	    }
	    
	    public List getAlertaPedidoByKey(long id_pedido, int keyAlerta) throws cl.bbr.jumbocl.pedidos.exceptions.PedidosException {
	        try {
	        	cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO daoPedidos = (cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO) cl.bbr.jumbocl.pedidos.dao.DAOFactory
	        			.getDAOFactory(cl.bbr.jumbocl.pedidos.dao.DAOFactory.JDBC).getPedidosDAO();
	        	         
	             return daoPedidos.getAlertaPedidoByKey(id_pedido, keyAlerta);

	        } catch ( NullPointerException ex ) {
	            logger.error("Problema con null en los datos (getAlertaPedidoByKey)" );
	            throw new cl.bbr.jumbocl.pedidos.exceptions.PedidosException(ex);
	        } catch (cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException ex) {
	            logger.error("Problema (getAlertaPedidoByKey)", ex);
	            throw new cl.bbr.jumbocl.pedidos.exceptions.PedidosException(ex);
	        }
	    }	    	    	  
	    
	    /**
	     *  Cristian Valdebenito
	     * @return 
	     */
	    public  void updateDatosPedidoCliente(int idpedido, long rut, String dv , String nombre , String apellido) throws cl.bbr.jumbocl.pedidos.exceptions.PedidosException {
			//Llamada al DAO para generar el Update del Pedido
	    	
	    	JdbcPedidosDAO pedidoDAO = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
	        try {

	        	pedidoDAO.updateDatosPedidoInvitado(idpedido, rut, dv, nombre, apellido);
	        	
	    } catch (PedidosDAOException  ex) {
	        logger.error("Error Update Pedido:" + ex.getMessage());
	        //ex.printStackTrace();
	         }
	    }
	    public PagoGrabilityDTO getPagoByOP(long id_pedido) throws PedidosException {

	       PagoGrabilityDTO pago = null;

	        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
	        try {
	        	pago = dao.getPagoByOP(id_pedido);
	            
	        } catch (PedidosDAOException e) {
	            logger.debug("Problema :" + e.getMessage());
	            throw new PedidosException(e);
	        }
	        //return result;
	        return pago;
	    }
	    
	    public PagoGrabilityDTO getPagoByToken(String token) throws PedidosException {

		       PagoGrabilityDTO pago = null;

		        JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		        try {
		        	pago = dao.getPagoByToken(token);
		            
		        } catch (PedidosDAOException e) {
		            logger.debug("Problema :" + e.getMessage());
		            throw new PedidosException(e);
		        }
		        //return result;
		        return pago;
		    }

		public void insertRegistroPago(PagoGrabilityDTO oPagoGrabilityDTO) throws PedidosException {
			JdbcPedidosDAO pedidoDAO = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				pedidoDAO.insertRegistroPago(oPagoGrabilityDTO);
			} catch (PedidosDAOException  e) {
				throw new PedidosException(e);
			}
		}
		public void actualizaPagoGrabilityByOP(PagoGrabilityDTO pago) throws PedidosException {
			JdbcPedidosDAO pedidoDAO = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				pedidoDAO.actualizaPagoGrabilityByOP(pago);
			} catch (PedidosDAOException  e) {
				throw new PedidosException(e);
			}
		}

		public List listaDeProductosPreferidos(int client_id) throws PedidosException{
			try {
				JdbcPedidosDAO pedidosDAO = (JdbcPedidosDAO) DAOFactory
				 .getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

				return pedidosDAO.listaDeProductosPreferidos(client_id);

			} catch ( NullPointerException ex ) {
				logger.error("Problema con null en los datos (setCompraHistorica)" );
				throw new PedidosException(ex);
			} catch (PedidosDAOException ex) {
				logger.error("Problema (setCompraHistorica)", ex);
				throw new PedidosException(ex);
			}
		}

		public long updateList(long idClient, ArrayList productsArray,String listType, long listId, String listName) throws PedidosException{
			try {
				JdbcPedidosDAO pedidosDAO = (JdbcPedidosDAO) DAOFactory
				 .getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

				return pedidosDAO.updateList(idClient,productsArray,listType, listId,listName);

			} catch ( NullPointerException ex ) {
				logger.error("Problema con null en los datos (setCompraHistorica)" );
				throw new PedidosException(ex);
			} catch (PedidosDAOException ex) {
				logger.error("Problema (setCompraHistorica)", ex);
				throw new PedidosException(ex);
			}
		}
		
		public boolean productoEnOPConSistitutosMxN(long idPedido)throws PedidosException, ServiceException, SystemException {
			  JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		        try {
		        	return  dao.isOPConProductosFaltantesEnPromoMxN(idPedido);
		        } catch (PedidosDAOException ex) {
		            logger.debug("Problema :" + ex);
		            throw new SystemException("Error no controlado", ex);
		        }
		}
		
		public Map getOPConProductosFaltantesEnPromoMxN(long idPedido)throws PedidosException, ServiceException, SystemException {
			  JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		        try {
		        	return  dao.getOPConProductosFaltantesEnPromoMxN(idPedido);
		        } catch (PedidosDAOException ex) {
		            logger.debug("Problema :" + ex);
		            throw new SystemException("Error no controlado", ex);
		        }
		}


		/* 
		 * INICIO VENTA MASIVA 
		 * */	
		public void actualizaPagoVentaMasivaByOP(PagoVentaMasivaDTO pago) throws PedidosException {
			JdbcPedidosDAO pedidoDAO = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				pedidoDAO.actualizaPagoVentaMasivaByOP(pago);
			} catch (PedidosDAOException  e) {
				throw new PedidosException(e);
			}
		}
		public void addPagoVentaMasiva(PagoVentaMasivaDTO dto) throws PedidosException {
			JdbcPedidosDAO pedidoDAO = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				pedidoDAO.addPagoVentaMasiva(dto);
			} catch (PedidosDAOException  e) {
				throw new PedidosException(e);
			}
		}
		public PagoVentaMasivaDTO getPagoVentaMasivaByToken(String token) throws PedidosException {
			PagoVentaMasivaDTO pago = null;
			JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				pago = dao.getPagoVentaMasivaByToken(token);				
			} catch (PedidosDAOException e) {
				logger.debug("Problema :" + e.getMessage());
				throw new PedidosException(e);
			}		        
			return pago;
		}
		public PagoVentaMasivaDTO getPagoVentaMasivaByIdPedido(long idpedido) throws PedidosException {
			PagoVentaMasivaDTO pago = null;
			JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				pago = dao.getPagoVentaMasivaByIdPedido(idpedido);				
			} catch (PedidosDAOException e) {
				logger.debug("Problema :" + e.getMessage());
				throw new PedidosException(e);
			}		        
			return pago;
		}
		public JornadaDTO getDatosJornada(String hora, String fecha, long idLocal) throws PedidosException, ServiceException, SystemException, ParseException{
			JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				return dao.getDatosJornada(hora,fecha, idLocal);
			} catch (PedidosDAOException ex) {
	            logger.debug("Problema :" + ex);
	            throw new SystemException("Error no controlado", ex);
			}
		}

		public ProductoEntity getProductoPedidoByIdProdFO(String pro_id) throws SystemException {
			JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				long prod_id_fo = Long.parseLong(pro_id);
				return dao.getProductoPedidoByIdProdFO(prod_id_fo);
			} catch (PedidosDAOException ex) {
	            logger.debug("Problema :" + ex);
	            throw new SystemException("Error no controlado", ex);
			}
		}
		public void addDetallePickingVentaMasiva(List lista, CreaRondaDTO dtoRonda, BinDTO dtoBin, long idPedido, long pedidoValidado) throws PedidosException, SystemException {
			
			JdbcPedidosDAO pedidoDAO = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			//JdbcRondasDAO daoRondas = (JdbcRondasDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getRondasDAO();
			boolean result = false;

			// Creamos trx
			JdbcTransaccion trx1 = new JdbcTransaccion();

			// Iniciamos trx
			try {
				trx1.begin();
			} catch (Exception e1) {
				logger.error("Error al iniciar transacción");
				throw new SystemException("Error al iniciar transacción");
			}
		        
			try {
				//List binsConDetalle = new ArrayList();
				//binsConDetalle.add(dtoBin);
				//daoRondas.grabarRonda(0, binsConDetalle, resultadoPedidos, detallePicking, regPick, -1);
				pedidoDAO.setTrx(trx1);
				//daoRondas.setTrx(trx1);
			} catch (PedidosDAOException e2) {
				logger.error("Error al asignar transacción al dao Pedidos");
				throw new SystemException("Error al asignar transacción al dao Pedidos");
			} 
			try {
				
				//daoRondas.setCreaRonda()				
				pedidoDAO.addDetallePickingVentaMasiva(lista,  dtoRonda, dtoBin);
				pedidoDAO.setModEstadoPedido(idPedido, pedidoValidado);
			} catch (PedidosDAOException  pde) {
				try {
					trx1.rollback();
				} catch (DAOException e1) {
					logger.error("Error al hacer rollback");
					throw new SystemException("Error al hacer rollback");
				}
				throw new SystemException("Error no controlado ", pde);
			}
			
			try {
	            trx1.end();
	        } catch (DAOException e) {
	            logger.error("Error al finalizar transacción");
	            throw new SystemException("Error al finalizar transacción");
	        }
			//return null;
		}
		
		public List getProductosSolicitadosVMById(long id_pedido) throws PedidosException, SystemException {
			JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				return dao.getProductosSolicitadosVMById(id_pedido);
			} catch (PedidosDAOException ex) {
				logger.debug("Problema :" + ex);
				throw new SystemException("Error no controlado", ex);
			}
		}
		/* 
		 * FIN INICIO VENTA MASIVA 
		 * */

		public JornadaDTO getDatosJornadaDespachoSegunComuna(String hora,String fecha, long comuna_id, long idLocal) throws PedidosException, ServiceException, SystemException, ParseException{
			JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				return dao.getDatosJornadaDespachoSegunComuna(hora,fecha,comuna_id, idLocal);
			} catch (PedidosDAOException ex) {
	            logger.debug("Problema :" + ex);
	            throw new SystemException("Error no controlado", ex);
			}
		}

		public int getPoligonoVentaMasivaPorComuna(long comuna_id)  throws PedidosException, ServiceException, SystemException, ParseException{
			JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			try {
				return dao.getPoligonoVentaMasivaPorComuna(comuna_id);
			} catch (PedidosDAOException ex) {
	            logger.debug("Problema :" + ex);
	            throw new SystemException("Error no controlado", ex);
			}
		}
}
