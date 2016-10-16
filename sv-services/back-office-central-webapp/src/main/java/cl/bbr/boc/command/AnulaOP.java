package cl.bbr.boc.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * AnulaOP Comando Process comando que anula una Op
 * 
 * @author BBRI
 */

public class AnulaOP extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

        String url;
        String paramUrl = "";
        long paramId_op = 0L;
        long paramId_motivo = 0L;
        String paramObs = "";

        String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
        String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");
        String paramHeader = getServletConfig().getInitParameter("headerlog");
        
        if (req.getParameter("url") == null) {
            throw new ParametroObligatorioException("url es null");
        }
        if (req.getParameter("id_pedido") == null) {
            throw new ParametroObligatorioException("id_pedido es nulo");
        }
        if (req.getParameter("id_motivo") == null) {
            throw new ParametroObligatorioException("id_motivo es nulo");
        }
        if (req.getParameter("obs") == null) {
            throw new ParametroObligatorioException("obs es nulo");
        }

        paramUrl = req.getParameter("url");
        paramId_op = Long.parseLong(req.getParameter("id_pedido"));
        paramId_motivo = Long.parseLong(req.getParameter("id_motivo"));
        paramObs = req.getParameter("obs");

        BizDelegate bizDelegate = new BizDelegate();

        ForwardParameters fp = new ForwardParameters();
        fp.add(req.getParameterMap());
        
        try {
            PedidoDTO pedido = bizDelegate.getPedidosById(paramId_op);
            logger.info("*** ANULAR OP ***");
            if ( bizDelegate.setAnularOP(paramId_op) ) {
                
                //INI - El pedido ya esta anulado en la BD, ahora vamos a anular la reserva
                if (pedido.getId_estado() <= Constantes.ID_ESTAD_PEDIDO_EN_BODEGA || pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO) {
                    if (Constantes.MEDIO_PAGO_CAT.equalsIgnoreCase(pedido.getMedio_pago())) {
                        try {
                        	logger.info("*** VAMOS A ANULAR RESERVA CAT ***");
                            if ( bizDelegate.anulacionAceleradaCAT(pedido) ) {
                                fp.add("msj_anulacion_acelerada", "");
                            } else {
                                fp.add("msj_anulacion_acelerada", "Ocurrió un error al liberar el monto reservado en la tarjeta Mas del cliente");
                            }
                            
                        } catch (BocException e) {
                            fp.add("msj_anulacion_acelerada", "Ocurrió un error al liberar el monto reservado en la tarjeta Mas del cliente");
                        }
                    } else if (Constantes.MEDIO_PAGO_TBK.equalsIgnoreCase(pedido.getMedio_pago())) {
                        //TODO: falta para TBK
                        
                    }

                    if (!Constantes.MEDIO_PAGO_LINEA_CREDITO.equalsIgnoreCase(pedido.getMedio_pago())
                            && pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO) {
                        List trxs = bizDelegate.getTrxMpByIdPedido( pedido.getId_pedido() );
                        boolean alertar = false;
                        for ( int i = 0; i < trxs.size(); i++ ) {
                            MonitorTrxMpDTO trx = (MonitorTrxMpDTO) trxs.get(i);
                            if ( trx.getId_estado() == Constantes.ID_ESTAD_TRXMP_PAGADA || trx.getId_estado() == Constantes.ID_ESTAD_TRXMP_CREADA ) {
                                alertar = true;                                
                            }
                        }
                        if ( alertar ) {
                            fp.add("msj_anulacion_acelerada", "Para este caso deberá informar al Local para que realice además la anulación de la boleta emitida. La OP permanecerá marcada hasta que se confirme esta operación.");                                
                            //marcamos el pedido
                            bizDelegate.marcaAnulacionBoletaEnLocal(pedido.getId_pedido(),true);                                                       
                            
                            //dejamos en el log
                            LogPedidoDTO log = new LogPedidoDTO();
                            log.setUsuario(usr.getLogin());
                            log.setId_pedido(pedido.getId_pedido());
                            log.setId_motivo(paramId_motivo);
                            log.setLog("El usuario es informado de avisar a local y confirmar anulación de boleta");
                            bizDelegate.addLogPedido(log);                            
                        }                        
                    }
                }
                //FIN - 
                
                //INI - REBAJAR OCURRENCIA DE EVENTO PARA CLIENTE DE OP
                boolean resultOcurrencia = true;
                if ( !bizDelegate.bajarOcurrenciaEvento(paramId_op, pedido.getRut_cliente())) {
                    logger.debug("No se pudo rebajar la ocurrencia de los eventos del Rut "+ pedido.getRut_cliente()+ ", ID Pedido "+ paramId_op);
                    resultOcurrencia = false;
                }
                //FIN - REBAJAR OCURRENCIA DE EVENTO PARA CLIENTE DE OP
                usr.setId_pedido(0);
                LogPedidoDTO log = new LogPedidoDTO();
                log.setUsuario(usr.getLogin());
                log.setId_pedido(paramId_op);
                log.setId_motivo(paramId_motivo);
                log.setLog(paramHeader + " : " + paramObs);
                bizDelegate.addLogPedido(log);
                if (resultOcurrencia) {
                    fp.add("mensaje", mensaje_exito);
                } else {
                    fp.add("mensaje",mensaje_exito+ " [Existió un problema al rebajar la ocurrencia de eventos] ");
                }
                fp.add("ret", "1");
                url = paramUrl + fp.forward();
                usr.setId_pedido(paramId_op);

                // **** Monitor de despacho ****
                RutaDTO ruta = bizDelegate.getRutaByPedido(paramId_op);
                if (ruta.getIdRuta() > 0) {
                    // Sacamos el pedido de la ruta
                    if (bizDelegate.delPedidoRuta(paramId_op) == 1) {
                        LogPedidoDTO log2 = new LogPedidoDTO(paramId_op, usr.getLogin(), "[BOC] Pedido " + paramId_op+ " eliminado de la Ruta: " + ruta.getIdRuta());
                        bizDelegate.addLogPedido(log2);
                    }
                    //Actualizamos la cantidad de bins
                    bizDelegate.actualizaCantBinsRuta(ruta.getIdRuta());

                    //Si es el ultimo pedido de la ruta, dejamos la ruta anulada
                    List peds = bizDelegate.getPedidosByRuta(ruta.getIdRuta());
                    if (peds.size() == 0) {
                        bizDelegate.setEstadoRuta(Constantes.ESTADO_RUTA_ANULADA, ruta.getIdRuta());
                        LogRutaDTO log3 = new LogRutaDTO(ruta.getIdRuta(), ruta.getEstado(), usr, "[BOC] La ruta queda anulada ");
                        bizDelegate.addLogRuta(log3);
                        //Pedido anulado y ruta anulada
                    } else {
                        boolean todosFinalizados = true;
                        for (int i = 0; i < peds.size(); i++) {
                            PedidoDTO p = (PedidoDTO) peds.get(i);
                            if (p.getId_estado() != Constantes.ID_ESTAD_PEDIDO_FINALIZADO) {
                                todosFinalizados = false;
                            }
                        }
                        if (todosFinalizados) {
                            bizDelegate.setEstadoRuta(Constantes.ESTADO_RUTA_FINALIZADA, ruta.getIdRuta());
                            LogRutaDTO log3 = new LogRutaDTO(ruta.getIdRuta(),ruta.getEstado(), usr,"[BOC] La ruta queda finalizada ");
                            bizDelegate.addLogRuta(log3);
                            //Pedido Anulado y ruta finalizada
                        }
                    }
                }
            } else {
                fp.add("mensaje", mensaje_fracaso);
                fp.add("ret", "0");
                url = paramUrl + fp.forward() + "&mensaje=" + mensaje_fracaso + "&ret=0";
            }

        } catch (Exception ex) {
            String UrlError = paramUrl;
            if (ex.getMessage().equals(Constantes._EX_PED_ID_INVALIDO)) {
                logger.error("Id del pedido es inválido");
                fp.add("rc", Constantes._EX_PED_ID_INVALIDO);
            }
            if (ex.getMessage().equals(Constantes._EX_ESTADO_INVALIDO)) {
                logger.error("Id del estado es inválido");
                fp.add("rc", Constantes._EX_ESTADO_INVALIDO);
            }
            if (ex.getMessage().equals(Constantes._EX_COT_ID_NO_EXISTE)) {
                logger
                        .error("No puede encontrar datos de la cotizacion a la que el pedido pertenece");
                fp.add("rc", Constantes._EX_COT_ID_NO_EXISTE);
            }
            fp.add("mensaje", mensaje_fracaso);
            fp.add("ret", "0");
            url = UrlError + fp.forward();
        }

        res.sendRedirect(url);

    }

}
