package cl.bbr.boc.ctrl;

import java.sql.SQLException;

import cl.bbr.boc.dao.CambiaEstadoValidadoDAO;
import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

public class CambiaEstadoValidadoCtrl {
	
	Logging logger = new Logging(this);
	
	public boolean retrocederOPEstadoValidado(long idPedido, String user)throws DAOException, SystemException, BocException{
		CambiaEstadoValidadoDAO cev = (CambiaEstadoValidadoDAO)DAOFactoryParametros.getDAOFactory(DAOFactoryParametros.JDBC).getCambiaEstadoValidadoDAO();
        boolean result = false;
        logger.debug("Inicio Proceso de cambio a estado Validado...");

        //		 Creamos trx
        JdbcTransaccion trx1 = new JdbcTransaccion();

        //		 Iniciamos trx
        try {
            trx1.begin();
        } catch (DAOException e1) {
            logger.error("Error al iniciar transacción");
            throw new SystemException("Error al iniciar transacción");
        } catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			 logger.error("Error al iniciar transacción");
	            throw new SystemException("Error al iniciar transacción");
		}

        try {
        	cev.setTrx(trx1);
        } catch (DAOException e2) {
            logger.error("Error al asignar transacción al dao Pedidos");
            throw new SystemException("Error al asignar transacción al dao Pedidos");
        }

        try {
        	//Si existen TRX, se eliminan
			int resultTRX = cev.getCountTrxMpByIdPedido(idPedido);
			if (resultTRX > 0) {
				
				cev.DelTrxMP(idPedido);
				cev.registrarTracking(idPedido, user ,"Cambio Estado a Validado : Se elimina transaccion MP");
				logger.debug("Cambio Estado a Validado : Registros TrxMp por Id_Pedido, han sido Borrados.");
			}
			//Si existen datos en detalle de Picking, se eliminan.
			int	resultDP = cev.getCountDetallePicking(idPedido);
			if (resultDP > 0) {
				
				cev.borrarDetallePicking(idPedido);
				cev.registrarTracking(idPedido, user,"Cambio Estado a Validado : Se elimina Detalle de Picking");
				logger.debug("Cambio Estado a Validado : Registros DetallePicking por Id_Pedido, han sido Borrados.");
			}	
			
			boolean resultadoDP	= cev.modificarCantidadesDP(idPedido);
			if(resultadoDP){
				cev.registrarTracking(idPedido, user,"Cambio Estado a Validado : Se modifican cantidades faltantes y sin pick en Detalle de Pedidos");
				logger.debug("Cambio Estado a Validado : Se modifican cantidades faltantes y sin pick en Detalle de Pedidos.");
			}
			boolean resultadoCE	= cev.cambioEstadoOP(idPedido);        	
            if (resultadoCE) {
				cev.registrarTracking(idPedido, user,"Cambio Estado a Validado : OP Retrocedida");
				result=true;
				logger.debug("Cambio Estado a Validado : OP Retrocedida");
			}
        } catch (DAOException e) {
            //				rollback trx
            try {
                trx1.rollback();
            } catch (DAOException e1) {
                logger.error("Error al hacer rollback");
                throw new SystemException("Error al hacer rollback");
            }
            e.printStackTrace();
            throw new DAOException(e);
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


}
