/*
 * Creado el 05-11-2010
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.jumbo.capturar.bizdelegate;

import java.util.HashMap;
import java.util.List;

import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.clientes.service.ClientesService;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.log.Logging;
import cl.jumbo.capturar.exceptions.FuncionalException;




/**
 * @author rsuarezr
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class BizDelegate {
	private static PedidosService pedidoService;
    private static ClientesService clientesService = null;
    Logging logger = new Logging(this);
	
	public BizDelegate(){
		if (pedidoService == null)
			pedidoService = new PedidosService();
        if (clientesService == null)
            clientesService = new ClientesService();
	}
	
	public HashMap obtenerLstOPByTBK() throws  ServiceException{
		try {
			return pedidoService.obtenerLstOPByTBK();			
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}
		
	}

	/**
	 * @param id_pedido
	 * @param id_estad_pedido_pagado
	 */
	public boolean setModEstadoPedido(long id_pedido, long id_estado) throws  ServiceException{
		try {
			return pedidoService.setModEstadoPedido(id_pedido, id_estado);			
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}
	}

	/**
	 * @param log2
	 */
	public void addLogPedido(LogPedidoDTO log) throws  ServiceException{
		try {
			pedidoService.addLogPedido(log);			
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @param id_estad_trxmp_rechazada
	 */
	public void setModEstadoTrxOP(long id_pedido, int id_estado) throws  ServiceException{
		try {
			pedidoService.setModEstadoTrxOP(id_pedido, id_estado);			
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}
	}

	/**
	 * @param id_pedido
	 * @return
	 */
	public WebpayDTO getWebpayByOP(long id_pedido) throws  ServiceException{
		try {
			  return pedidoService.getWebpayByOP(id_pedido);			
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}
	}

	/**
	 * @param id_pedido
	 */
	public boolean setModFlagWebpayByOP(long id_pedido, String flag) throws  ServiceException{
		try {
			  return pedidoService.setModFlagWebpayByOP(id_pedido, flag);			
		} catch (Exception ex) {
			throw new ServiceException(ex);
		}
	}

    /**
     * @return
     */
    public List getPedidosRechazadosErroneamenteTBK(int dias, int minutos) throws  ServiceException{
        try {
              return pedidoService.getPedidosRechazadosErroneamenteTBK(dias, minutos);           
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }
    
    public List getPedidosRechazadosErroneamenteCAT(int dias, int minutos) throws  ServiceException{
        try {
              return pedidoService.getPedidosRechazadosErroneamenteCAT(dias, minutos);           
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param id_pedido
     * @param b
     * @param i
     */
    public void ingresarPedidoASistema(long idPedido, boolean esClienteConfiable, long cliente) throws ServiceException {
        try {
            pedidoService.ingresarPedidoASistema(idPedido, esClienteConfiable, cliente);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param id_local
     * @return
     */
    public LocalDTO getLocalRetiro(long idLocal) throws ServiceException {
        try {
            return pedidoService.getLocalRetiro(idLocal);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public List getProductosSolicitadosById(long idPedido) throws ServiceException {
        try {
            return pedidoService.getProductosSolicitadosById(idPedido);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public WebpayDTO webpayGetPedido(long idPedido) throws ServiceException {
        try {
            return pedidoService.webpayGetPedido(idPedido);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public BotonPagoDTO botonPagoGetByPedido(long idPedido) throws ServiceException {
        try {
            return pedidoService.botonPagoGetByPedido(idPedido);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param mail
     */
    public void addMail(MailDTO mail) throws ServiceException {
        try {
            clientesService.addMail(mail);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param rut_cliente
     * @return
     */
    public ClientesDTO getClienteByRut(long rutCliente) throws ServiceException {
        try {
            return clientesService.getClienteByRut(rutCliente);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param rut_cliente
     * @return
     */
    public boolean clienteEsConfiable(long rutCliente) throws ServiceException {
        try {
            return clientesService.clienteEsConfiable(rutCliente);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param id_pedido
     * @return
     */
    public PedidoDTO getPedidoById(long idPedido) throws ServiceException {
        try {
            return pedidoService.getPedido(idPedido);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * @param pedidosConError
     */
    public void cambiaEstadoWebPays(long idPedido) throws ServiceException {
        try {
            pedidoService.cambiaEstadoWebPays(idPedido);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }
    /**
     * 
     * @param pedido_id
     * @return
     * @throws FuncionalException
     */
    public List getDescuentosAplicados( long pedido_id ) throws FuncionalException {
        
   	 try {
        
   		 return pedidoService.getDescuentosAplicados( pedido_id );
        
   	 } catch ( cl.bbr.jumbocl.common.exceptions.ServiceException e ) {
         
   		 logger.error( "Problema BizDelegate (doInsPedido)", e );
            throw new FuncionalException( e );
        
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
         
         	pedido = pedidoService.getValidaCuponYPromocionPorIdPedido( idPedido );
         
         } catch ( cl.bbr.jumbocl.common.exceptions.ServiceException e ) {
        
        	 logger.error( "Problema BizDelegate (CapturaWP)", e );
             throw new FuncionalException( e );
         
         }
         
         return pedido;
     }
     
     
     public ParametroDTO getParametroByName(String name) throws FuncionalException{
    	 try {
    		 return pedidoService.getParametroByName(name);	
    		 //return parametroService.getParametroByName(name);
    	 } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e ) {
    		 logger.error( "Problema BizDelegate (getParametroByName)", e );
             throw new FuncionalException( e );
    	 }
     }
     
     public boolean actualizaParametroByName(String name, String valor) throws FuncionalException{
    	 try {
    		 return pedidoService.actualizaParametroByName(name, valor);	
    		 //return parametroService.getParametroByName(name);
    	 } catch (cl.bbr.jumbocl.common.exceptions.ServiceException e ) {
    		 logger.error( "Problema BizDelegate (actualizaParametroByName)", e );
             throw new FuncionalException( e );
    	 }
     }
}
