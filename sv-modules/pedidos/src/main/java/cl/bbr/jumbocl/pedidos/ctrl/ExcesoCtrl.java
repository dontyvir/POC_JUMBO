package cl.bbr.jumbocl.pedidos.ctrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcExcesoDAO;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.shared.log.Logging;

public class ExcesoCtrl {

    /**
     * Permite generar los eventos en un archivo log.
     */
    Logging logger = new Logging(this); 
    
	
	/*Eliminar solo para realizar los test JUnit*/
	public static List getPedidosByEstado(long idEstado) {
		
		JdbcExcesoDAO daoExceso =  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();

        try {            
        	return daoExceso.getPedidosByEstado(idEstado);        	
        } catch (Exception e) {
        	e.printStackTrace();
        }	
        return null;
	}
	
	public boolean isOpConExcesoXTotal(long idPedido) throws SystemException {
		// TODO Apéndice de método generado automáticamente
		boolean isExceso = false;
		
		JdbcExcesoDAO daoExceso =  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		JdbcPedidosDAO daoPedido = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

        try {            
        	PedidoDTO oPedido = daoPedido.getPedidoById(idPedido);
        	//obtener suma de costo de despacho mas detalle picking
        	int montoTotalCompra = (int)oPedido.getCosto_despacho() + (int)daoExceso.getTotalDetPickingByOP(idPedido);
        	
        	if (montoTotalCompra > (int)oPedido.getMonto_reservado()) {
    			isExceso = true;
    		}
        	
        } catch (Exception e) {
        	logger.error(e);
            throw new SystemException(e);
        }
        return isExceso;		
	}
	
	public boolean isOpConExcesoXProducto(long idPedido) throws SystemException {
		// TODO Apéndice de método generado automáticamente
		JdbcExcesoDAO daoExceso =  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		
        try {            
        	List idsDetallePedidoPickeado	= daoExceso.getIdsDetallePedidoPickeado(idPedido);
        	Iterator it = idsDetallePedidoPickeado.iterator();		
        	
        	while(it.hasNext()){
        		long idDetalle = Long.parseLong((String) it.next());
        		long precioTotalSolicitado	= daoExceso.getPrecioTotalSolicitadoByIdDetalle(idDetalle, idPedido);
        		long precioTotalPickeado	= daoExceso.getPrecioTotalPickingByIdDetalle(idDetalle, idPedido);
        		
        		if (precioTotalPickeado > precioTotalSolicitado) {
        			return true;
        		}
        	}
        	
        } catch (Exception e) {
        	logger.error(e);
            throw new SystemException(e);
        }
        return false;		
	}
	
	public List getIdDetalleSolicitadoConExceso(long idPedido) throws SystemException {
		// TODO Apéndice de método generado automáticamente
		JdbcExcesoDAO daoExceso =  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		List idsDetalleSolicitadoConExceso	= new ArrayList();
        try {            
        	List idsDetallePedidoPickeado	= daoExceso.getIdsDetallePedidoPickeado(idPedido);
        	Iterator it = idsDetallePedidoPickeado.iterator();		
        	
        	while(it.hasNext()){
        		long idDetalle = Long.parseLong((String) it.next());
        		long precioTotalSolicitado	= daoExceso.getPrecioTotalSolicitadoByIdDetalle(idDetalle, idPedido);
        		long precioTotalPickeado	= daoExceso.getPrecioTotalPickingByIdDetalle(idDetalle, idPedido);
        		
        		if (precioTotalPickeado > precioTotalSolicitado) {
        			idsDetalleSolicitadoConExceso.add(String.valueOf(idDetalle));
        		}
        	}
        	
        } catch (Exception e) {
        	logger.error(e);
            throw new SystemException(e);
        }
        return idsDetalleSolicitadoConExceso;		
	}

	public boolean corrigeExcesoOP(long idPedido) throws SystemException {
					
		JdbcPedidosDAO daoPedido = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
		try {
			PedidoDTO oPedido = daoPedido.getPedidoById(idPedido);
			if (oPedido != null && oPedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_BODEGA) {
				
				JdbcExcesoDAO daoExceso 		=  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
				List idsDetallePedidoPickeado	= daoExceso.getIdsDetallePedidoPickeado(idPedido);
				
				//1.- Los excesos sobre el total de un producto pickeado solicitado.	    	
		    	this.corrigePreciosPickeadosConExcesoEnTotal(idsDetallePedidoPickeado,idPedido);
		    	
		    	//2.- Los excesos sobre el precio de un producto pickiado con y sin faltantes.
		    	this.corrigePreciosPickeadosConExcesoParcial(idsDetallePedidoPickeado,idPedido);
		    			    	
		    	//4.- Valida que no existan excesos para retornar true !!!!
		    	if( this.isExcesoCorregidoXProducto(idsDetallePedidoPickeado,idPedido)){		    		
		    		//3.- cambiar flag de exceso del pedido
			    	this.modPedidoExcedido(idPedido, false);
			    	return true;
		    	}else{
		    		this.modPedidoExcedido(idPedido, true);	
		    		return false;
		    	}
		    	
			} else {
				logger.error("Estado OP no valido para corregir exceso.");
				throw new SystemException("Estado OP no valido para corregir exceso.");
			}
		} catch (PedidosDAOException e) {
			logger.error(e);
			throw new SystemException(e);
		}
    }
	
	private boolean isExcesoCorregidoXProducto(List idsDetallePedidoPickeado, long idPedido) throws SystemException {
		// TODO Apéndice de método generado automáticamente
		JdbcExcesoDAO daoExceso =  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		
        try {            
        	
        	Iterator it = idsDetallePedidoPickeado.iterator();		
        	
        	while(it.hasNext()){
        		long idDetalle = Long.parseLong((String) it.next());
        		long precioTotalSolicitado	= daoExceso.getPrecioTotalSolicitadoByIdDetalle(idDetalle, idPedido);
        		long precioTotalPickeado	= daoExceso.getPrecioTotalPickingByIdDetalle(idDetalle, idPedido);
        		
        		if (precioTotalPickeado > precioTotalSolicitado) {
        			return false;
        		}
        	}
        	
        } catch (Exception e) {
        	logger.error(e);
            throw new SystemException(e);
        }
        return true;		
	}

	private void corrigePreciosPickeadosConExcesoEnTotal(List idsDetallePedidoPickeado, long idPedido) throws SystemException {

		JdbcExcesoDAO daoExceso = (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		JdbcPedidosDAO daoPedido= (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
        try {            
        	
        	Iterator it = idsDetallePedidoPickeado.iterator();		
        	
        	while(it.hasNext()){
        		long idDetalle = Long.parseLong((String) it.next());
        		
        		//Producto solicitado
        		ProductosPedidoDTO detalleProdSolic = daoExceso.getDetallePedidoByIdDetalle(idDetalle,idPedido);
        		        		        		
        		//Caso de exceso para este metodo 
        		//Cant_pick * Precio_pick > Cant_solic * Precio_solic
        		        		        		
        		long precioTotalSolicitado	= daoExceso.getPrecioTotalSolicitadoByIdDetalle(idDetalle,idPedido);
        		long precioTotalPickeado	= daoExceso.getPrecioTotalPickingByIdDetalle(idDetalle, idPedido);
        		double cantPickeada			= daoExceso.getCantPickingByIdDetalle(idDetalle, idPedido);
        		
        		//
				if (precioTotalPickeado > precioTotalSolicitado) {
					//Mover precio pickeado; copiar el campo PRECIO al campo PRECIO_PICK_ORI ambos de la tabla BO_DEATLLE_PICKING
					daoExceso.updatePrecioPickingOriginal(idDetalle, idPedido);	
					
					//Mantener precio solicitado
					if(cantPickeada == detalleProdSolic.getCant_solic() || cantPickeada < detalleProdSolic.getCant_solic()){								
						//Hacer update al precio a todo los id_detalle pickeados con el precio asolicitado 
						if(daoExceso.updatePrecioPickXPrecioSolicitado(idDetalle, idPedido)){
							try{
								LogPedidoDTO log = new LogPedidoDTO();
								log.setId_pedido(idPedido);
								log.setLog("[CORRECCION_EXCESO] Exceso corregido automaticamente (Mantiene precio solicitado) para el ID_DETALLE_PEDIDO: "+idDetalle);
								log.setUsuario("EXCESO");
								daoPedido.addLogPedido(log);
							}catch(Exception e){}
						}
						
					}
					//Bajar precio para corregir exceso
					else if(cantPickeada > detalleProdSolic.getCant_solic()){
						//Hacer update al precio a todo los id_detalle pickeados con el precio asolicitado 
						if(daoExceso.updatePrecioPickXPrecioSegunCantidad(idDetalle, idPedido)){
							try{
								LogPedidoDTO log = new LogPedidoDTO();
								log.setId_pedido(idPedido);
								log.setLog("[CORRECCION_EXCESO] Exceso corregido automaticamente (Baja precio para corregir exceso) para el ID_DETALLE_PEDIDO: "+idDetalle);
								log.setUsuario("EXCESO");
								daoPedido.addLogPedido(log);
							}catch(Exception e){}
						}
					}
				}				
        		
        	}
        	
        } catch (Exception e) {
        	logger.error(e);
            throw new SystemException(e);
        }
    }
	
	private void corrigePreciosPickeadosConExcesoParcial(List idsDetallePedidoPickeado, long idPedido) throws SystemException {

		JdbcExcesoDAO daoExceso =  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		JdbcPedidosDAO daoPedido= (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
        try {

			Iterator it = idsDetallePedidoPickeado.iterator();
			
			while (it.hasNext()) {
				long idDetalle = Long.parseLong((String) it.next());
				//long precioSolicitado = daoExceso.getPrecioSolicitadoByIdDetalle(idDetalle, idPedido);
				long precioSolicitado = daoExceso.getPrecioTotalSolicitadoByIdDetalle(idDetalle,idPedido);
				
				List idsDpicking  = daoExceso.getIdsDpickingByIdDetalle(idDetalle, idPedido);
				Iterator itDpicking = idsDpicking.iterator();
				
				while (itDpicking.hasNext()) {
					long idDpicking = Long.parseLong((String) itDpicking.next());
					DetallePickingDTO oDpick = daoExceso.getDetallePickingByIdDpicking(idDpicking);
					
					if(Math.round(oDpick.getPrecio() * oDpick.getCant_pick()) > precioSolicitado){
						//Mover precio pickeado; copiar el campo PRECIO al campo PRECIO_PICK_ORI ambos de la tabla BO_DEATLLE_PICKING
						daoExceso.updatePrecioPickingOriginal(idDetalle, idPedido);	
						//Mantiene el precio que el cliente pago en el FO. 
						if(daoExceso.updatePrecioPickXPrecioSolicitado(idDetalle, idPedido, idDpicking)){
							try{
								LogPedidoDTO log = new LogPedidoDTO();
								log.setId_pedido(idPedido);
								log.setLog("[CORRECCION_EXCESO] Exceso corregido automaticamente (Mantiene precio pagado en el FO) para el ID_DETALLE_PEDIDO: "+idDetalle);
								log.setUsuario("EXCESO");
								daoPedido.addLogPedido(log);
							}catch(Exception e){}
						}
					}
				}
			}

        } catch (Exception e) {
        	logger.error(e);
            throw new SystemException(e);
        }
    }
	
	/**
	 * Metodo que consulta si una OP tiene Exceso 
	 * Correccion : El exceso ahora se mide por producto y por el total pickeado.
	 * Deprecado: ([total pickiado(BO_DETALLE_PICKING) + costo despacho(BO_PEDIDOS)]  vs [monto reservado(BO_PEDIDOS)])
	 * 
	 * @param id_pedido:id del pedido a consultar
	 * @return boolean true en caso de exceso o false en caso que OP no tenga exceso
	 * @throws PedidosException
	 */
	public boolean isOpConExceso(long idPedido) throws SystemException {

		boolean isExceso = false;

		try {
			if(this.isOpConExcesoXTotal(idPedido) || this.isOpConExcesoXProducto(idPedido)){
				 isExceso = true;
			}
		} catch (Exception e) {
			logger.error(e);
            throw new SystemException(e);
        }
		
		return isExceso;
	}
	
	public boolean isOpConExceso(long idPedido, boolean isCorrecionActiva) throws SystemException {

		boolean isExceso = false;
		if(!isCorrecionActiva){
			try {
				JdbcPedidosDAO dao	= (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
				PedidoDTO pedido = dao.getPedidoById(idPedido);
				//obtener suma de costo de despacho mas detalle picking
				double monto_total_compra = pedido.getCosto_despacho() + dao.getTotalDetPickingByOP(idPedido);
				if (monto_total_compra > pedido.getMonto_reservado()) {
					isExceso = true;
				}
			} catch (PedidosDAOException e) {
				logger.error(e);
	            throw new SystemException(e);
	        }
		}else{
			isExceso = this.isOpConExceso(idPedido);
		}
		return isExceso;
	}
	
    /**
     * Actualiza campo monto_excedido de la tabla BO_PEDIDOS a 1 si la op tiene exceso.
     * @param excedido
     */
    public void modPedidoExcedido(long idPedido, boolean excedido) throws SystemException {
        JdbcPedidosDAO daoPedido = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
        try {
        	daoPedido.modPedidoExcedido(idPedido, excedido);
        } catch (PedidosDAOException e) {
        	logger.error(e);
            throw new SystemException("Error no controlado", e);
        }
    }

	public boolean isExcesoCorreccionAutomatico(long idPedido)
			throws SystemException {
		// TODO Apéndice de método generado automáticamente

		if (!this.isOpConExcesoXProducto(idPedido)
				&& !this.isOpConExcesoXTotal(idPedido))
			return false;

		JdbcExcesoDAO daoExceso = (JdbcExcesoDAO) DAOFactory.getDAOFactory(
				DAOFactory.JDBC).getExcesoDAO();

		int porcentajeCorreccionExceso = 50;// Por si no puede rescatar el valor
											// de BD se define en 50%

		try {
			ParametrosService ps = new ParametrosService();
			ParametroDTO par = ps
					.getParametroByName("PORCENTAJE_CORRECCION_EXCESO");
			porcentajeCorreccionExceso = Integer.parseInt(par.getValor());
		} catch (Exception e) {
			// TODO Bloque catch generado automáticamente
			logger.error(e);
		}

		List idsDetallePedidoPickeado = daoExceso
				.getIdsDetallePedidoPickeado(idPedido);
		Iterator it = idsDetallePedidoPickeado.iterator();

		if (idsDetallePedidoPickeado.isEmpty())
			return false;

		int cantProdConExcesoCorregible = 0;
		int cantProdConExcesoNoCorregible = 0;
		int totalProdConExcesos = 0;

		while (it.hasNext()) {
			long idDetalle = Long.parseLong((String) it.next());

			long precioTotalSolicitado = daoExceso
					.getPrecioTotalSolicitadoByIdDetalle(idDetalle, idPedido);
			long precioTotalPickeado = daoExceso
					.getPrecioTotalPickingByIdDetalle(idDetalle, idPedido);

			if (precioTotalPickeado > precioTotalSolicitado) {
				// el X% del precio pickeado
				float porcExceso = ((float) precioTotalPickeado * 100)
						/ (float) precioTotalSolicitado;
				int precioPorcentualExcedido = Math.round(porcExceso) - 100;
				if (porcentajeCorreccionExceso >= precioPorcentualExcedido) {
					cantProdConExcesoCorregible++;
				} else {
					cantProdConExcesoNoCorregible++;
				}
				totalProdConExcesos++;
			}
		}
		int porcProdConExcesoNoCorregible = 0;

		if (totalProdConExcesos > 0)
			porcProdConExcesoNoCorregible = (cantProdConExcesoNoCorregible * 100)
					/ totalProdConExcesos;

		/*
		 * Si la cant de excesos corregible automaticamente es mayor a
		 * la cantidad de productos con exceso no corregibles se reparan los
		 * excesos de manera automatica.
		 */
		if (cantProdConExcesoCorregible > cantProdConExcesoNoCorregible
				&& porcentajeCorreccionExceso > porcProdConExcesoNoCorregible) {
			return true;
		} else {// de lo contrario se dispara un mail a aplicaciones para que se
				// corriga de manera manual.
			return false;
		}
	}

	public boolean isActivaCorreccionAutomatica() throws SystemException {
		// TODO Apéndice de método generado automáticamente

		try {
			ParametrosService ps = new ParametrosService();
			ParametroDTO oParam = ps
					.getParametroByName("ACTIVA_CORRECCION_EXCESO");
			if ("1".equals(oParam.getValor())) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e);
			throw new SystemException(e);
		}
		return false;
	}
	
	//Formulario cambio de precio

    /**
     * @param idPedido, idDetallePedido
     * @return
     */
    public ProductosPedidoDTO getDetalleProductoSolicitado(long idPedido,long idDetalle) throws SystemException {
       	JdbcExcesoDAO daoExceso =  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO(); 
       	return daoExceso.getDetallePedidoByIdDetalle(idDetalle,idPedido);
    }	
    
    public List getDetallePickingByIdDetalle(long idDetalle, long idPedido) throws SystemException {
       	JdbcExcesoDAO daoExceso =  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO(); 
       	return daoExceso.getDetallePickingByIdDetalle(idDetalle,idPedido);    	
    }
    
	public List getTrackingExcesoByOP(long idPedido) throws SystemException {
		JdbcExcesoDAO daoExceso =  (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO(); 
       	return daoExceso.getTrackingExcesoByOP(idPedido); 
	}
    
    public boolean updatePrecioPickXPrecioSegunCantidad(long idDetalle, long idPedido) throws SystemException {

		JdbcExcesoDAO daoExceso = (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		JdbcPedidosDAO daoPedido= (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		boolean isUpdate = false;
        try {                 	
			//Mover precio pickeado; copiar el campo PRECIO al campo PRECIO_PICK_ORI ambos de la tabla BO_DEATLLE_PICKING
			daoExceso.updatePrecioPickingOriginal(idDetalle, idPedido);			
			
			//Hacer update al precio a todo los id_detalle pickeados con el precio asolicitado 
			isUpdate = daoExceso.updatePrecioPickXPrecioSegunCantidad(idDetalle, idPedido);
			if(isUpdate){
				try{
					LogPedidoDTO log = new LogPedidoDTO();
					log.setId_pedido(idPedido);
					log.setLog("[CORRECCION_EXCESO] Exceso corregido por formulario cambia precio BOC (Baja precio para corregir exceso) para el ID_DETALLE_PEDIDO: "+idDetalle);
					log.setUsuario("EXCESO");
					daoPedido.addLogPedido(log);
				}catch(Exception e){}
			}
        } catch (Exception e) {
        	logger.error(e);
            throw new SystemException(e);
        }
        return isUpdate;
    }

	public boolean deletePickingByIdDpicking(long idPedido, long idDpicking)  throws SystemException {
		JdbcExcesoDAO daoExceso = (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		JdbcPedidosDAO daoPedido= (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		boolean isUpdate = false;
        try {                 	
        	DetallePickingDTO oDetallePickingDTO = daoExceso.getDetallePickingByIdDpicking(idDpicking);
			//Hacer update al precio a todo los id_detalle pickeados con el precio asolicitado 
			isUpdate = daoExceso.deletePickingByIdDpicking(idDpicking);
			if(isUpdate){
				daoExceso.recalculoCantidad(0, idPedido, idDpicking);
				try{
					LogPedidoDTO log = new LogPedidoDTO();
					log.setId_pedido(idPedido);
					log.setLog("[PICKING_ELIMINADO] " +oDetallePickingDTO.toString());
					log.setUsuario("EXCESO");
					daoPedido.addLogPedido(log);
				}catch(Exception e){}
			}
        } catch (Exception e) {
        	logger.error(e);
            throw new SystemException(e);
        }
        return isUpdate;
	}

	public boolean CorregirPrecioSegúnPrecioSolicitado(long idDetalle, long idPedido)  throws SystemException {
		// TODO Apéndice de método generado automáticamente
		JdbcExcesoDAO daoExceso = (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		JdbcPedidosDAO daoPedido= (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
		//Mover precio pickeado; copiar el campo PRECIO al campo PRECIO_PICK_ORI ambos de la tabla BO_DEATLLE_PICKING
		daoExceso.updatePrecioPickingOriginal(idDetalle, idPedido);	
		
		boolean isUpdate = daoExceso.updatePrecioPickXPrecioSolicitado(idDetalle, idPedido);
		//Hacer update al precio a todo los id_detalle pickeados con el precio asolicitado 
		if(isUpdate){
			try{
				LogPedidoDTO log = new LogPedidoDTO();
				log.setId_pedido(idPedido);
				log.setLog("[CORRECCION_EXCESO] Exceso corregido por formulario cambia precio BOC (Mantiene precio solicitado) para el ID_DETALLE_PEDIDO: "+idDetalle);
				log.setUsuario("EXCESO");
				daoPedido.addLogPedido(log);
			}catch(Exception e){}
		}
		return isUpdate;
	}

	public boolean ajustarCantidadSolicitada(long idDetalle, long idPedido) throws SystemException {
		// TODO Apéndice de método generado automáticamente
		JdbcExcesoDAO daoExceso = (JdbcExcesoDAO)  DAOFactory.getDAOFactory(DAOFactory.JDBC).getExcesoDAO();
		JdbcPedidosDAO daoPedido= (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
		boolean isUpdate = daoExceso.ajustarCantidadSolicitada(idDetalle, idPedido);
		//Hacer update al precio a todo los id_detalle pickeados con el precio asolicitado 
		if(isUpdate){
			daoExceso.recalculoCantidad(idDetalle, idPedido, 0);
			try{
				LogPedidoDTO log = new LogPedidoDTO();
				log.setId_pedido(idPedido);
				log.setLog("[CORRECCION_EXCESO] Exceso corregido por formulario cambia cantidad pickeada (Mantiene cantidad solicitado) para el ID_DETALLE_PEDIDO: "+idDetalle);
				log.setUsuario("EXCESO");
				daoPedido.addLogPedido(log);
			}catch(Exception e){}
		}
		return isUpdate;
	}
	
}