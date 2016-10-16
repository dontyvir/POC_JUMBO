package cl.bbr.jumbocl.pedidos.promos.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.promos.dto.CuponPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PedidoDatosSocketDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.SafDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.TcpPedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.ConsR2DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.CuponDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.DataConsR2DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.HeaderDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.TcpDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

public class ClienteTcpPromosCuponesSaf{
	private String host;
	private int puerto;
	JdbcTransaccion trx;

	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);
	
	/* 
	 * CONSULTAS TCP IP
	 */

	/**
	 * Inicializa ambiente de cliente para quema de tcps SAF
	 * @param host
	 * @param puerto
	 * @param trx
	 */
	public ClienteTcpPromosCuponesSaf(String host, int puerto, JdbcTransaccion trx) {
		this.host = host;
		this.puerto = puerto;
		this.trx = trx;
	}

	/**
	 * Genera el mensaje de quema de cupones en la tabla SAF
	 * @param id_pedido
	 * @param id_user
	 */
	public void setGeneraMsgSaf(long id_pedido, long id_user){
		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
		try {
			logger.debug("en setGeneraMsgSaf");
			dao.setTrx(this.trx);		
			
			String fyh_actual ="";
			String fecha_actual="";
			String hora_actual="";
			String[] split;		
			ConsR2DTO cons_r2 = new ConsR2DTO();
			
			PedidoDatosSocketDTO pedido = dao.getPedidoDatos(id_pedido);
			
			//busca datos base		
			List lst_tcp_dao = new ArrayList();
			List lst_cupon_dao = new ArrayList();
			
			List lst_tcp_r2 = new ArrayList();
			List lst_cupon_r2 = new ArrayList();
			
			//entrega listado TcpPedidoDTO
			lst_tcp_dao = dao.getTcpPedidosByIdPedido(id_pedido);
			//entrega listado CuponPedidoDTO
			lst_cupon_dao= dao.getCuponesPedidoByIdPedido(id_pedido);
			
			//Si no tiene tcps deja el logger
			if(lst_tcp_dao.size()<=0){
				logger.debug("El pedido no tiene TCPs, no envia SAFs");
				}
			else{
				logger.debug("El pedido tiene TCPs, genera el mensaje SAF");
				for (int i=0;i<lst_tcp_dao.size();i++){
					TcpPedidoDTO tcp_dao = (TcpPedidoDTO) lst_tcp_dao.get(i);
					/* 
					 * Consultar cupon y verificar el id_tcp corresponde
					 * se asume que la cantidad utilizada del tcp es la 
					 * cantidad	de veces que se canjea el cupon.
					 */
					String cupon ="";
					for(int j=0;j<lst_cupon_dao.size();j++){
						CuponPedidoDTO cup_dao =(CuponPedidoDTO) lst_cupon_dao.get(j);
						if ((tcp_dao.getCant_util()>0)&&
								(cup_dao.getId_tcp()==tcp_dao.getId_tcp())){
							cupon = cup_dao.getNro_cupon();
							CuponDTO cup_r2 = new CuponDTO();
							cup_r2.setCupon(cup_dao.getNro_cupon());
							cup_r2.setEstado(Constantes.TCP_R2_CUPON_CANJEADO);
							cup_r2.setCantidad_usada(tcp_dao.getCant_util());
							logger.debug("CUPON r2"+i
									+": (numero="+cup_r2.getCupon()
									+", numero="+cup_r2.getEstado()
									+", used:"+cup_r2.getCantidad_usada()+")");
							lst_cupon_r2.add(cup_r2);
						}
					}
					//tcpdto = new TcpClienteDTO(tcp_dao.getNro_tcp(),cupon,	tcp_dao.getCant_max(),0);
					TcpDTO tcp_r2 = new TcpDTO();
					tcp_r2.setNro_tcp(""+tcp_dao.getNro_tcp());
					tcp_r2.setCantidad(tcp_dao.getCant_util());
									
					//solo los tcp que tengan cantidad mayor a 0
					if (tcp_r2.getCantidad()>0){
						logger.debug("TCP r2"+i+": (numero="+tcp_r2.getNro_tcp()
								+", used:"+tcp_r2.getCantidad()+")");
						lst_tcp_r2.add(tcp_r2);
					}
				}			
				
				//HEADER
				HeaderDTO head = new HeaderDTO();
				
				head.setCanal(Constantes.TCP_TIPO_HEADER_CANAL);
				head.setCadena(Constantes.TCP_TIPO_HEADER_CADENA);
				head.setLocal(""+pedido.getCod_local_pos());		
				head.setPos(""+pedido.getCod_local_pos());
				
				/* fecha y hora actual */
				fyh_actual = Formatos.getFecHoraActualStr();
				split= fyh_actual.split(" ");	
				fecha_actual = split[0];
				hora_actual = split[1];
				
				head.setFecha(fecha_actual);
				head.setHora(hora_actual);
				head.setDocumento(
						Formatos.formatField(""+id_pedido,HeaderDTO.DOCUMENTO_LEN, Formatos.ALIGN_RIGHT, "0")
						); //boleta.. ? = pedido
				head.setOperador(
						Formatos.formatField(""+id_user,HeaderDTO.OPERADOR_LEN, Formatos.ALIGN_RIGHT, "0")
						); // operador ..? = id_usuario
				head.setJournal("000000");  // al inicio se marca en  6 ceros, mas adelante se actualiza el real
				head.setTipo_trx(Constantes.TCP_TIPO_CONS_R2);
				head.setFiller("00");
				
				//DATA
				DataConsR2DTO data= new DataConsR2DTO();
				data.setRut(
						Formatos.formatField(""+pedido.getRut(),DataConsR2DTO.RUT_LEN, Formatos.ALIGN_RIGHT, "0"));//rut cliente sin dv
				data.setCant_tcp(lst_tcp_r2.size());
				data.setList_tcp(lst_tcp_r2);
				data.setCant_cupon(lst_cupon_r2.size());
				data.setList_cupon(lst_cupon_r2);
				cons_r2.setHeader(head);
				cons_r2.setData(data);
				
				logger.debug("mensaje r2:"+cons_r2.toMsg());
				//almacena cualquier cosa con estado por defecto
				SafDTO saf_dto = new SafDTO();
				saf_dto.setEstado(Constantes.TCP_SAF_ESTADO_NO_ENVIADO);
				saf_dto.setMsg("temp");
				
				int id_saf = dao.insSaf(saf_dto);
				logger.debug("despues de insertar con mensaje temporal...id:"+id_saf);
				
				//obtiene el id_saf y actualiza el journal con el mensaje final
				saf_dto.setId_saf(id_saf);
				head.setJournal(
						Formatos.formatField(""+id_saf,HeaderDTO.JOURNAL_LEN, Formatos.ALIGN_RIGHT, "0"));
				saf_dto.setMsg(cons_r2.toMsg());			
				//actualiza el mensaje usando el id_saf como numero de journal			
				dao.updSaf(saf_dto);
				logger.debug("despues de actualizar mensaje con el journal:"+id_saf);
				logger.debug("fin setGeneraMsgSaf");
				
				// genera el envio 
				EnviarSaf();
				
			}//solo si tiene TCPs
		} catch (PedidosDAOException e) {
			e.printStackTrace();
		}		
	}
	
	public void EnviarSaf(){
		JdbcPedidosDAO dao = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		
		Socket skCliente=null;
		OutputStream os=null;
		InputStream is=null;
		try {
			logger.debug("en EnviarSaf");
			dao.setTrx(this.trx);
			
			logger.debug("genera conexion socket a "+host+":"+puerto);
			skCliente = new Socket( host , puerto );
			
			//lee tabla de almacenados y envia cada registro que no este marcado como enviado
			String estados = "'"+Constantes.TCP_SAF_ESTADO_PENDIENTE+"','"+Constantes.TCP_SAF_ESTADO_NO_ENVIADO+"'";			
			List lst_saf = dao.getSafByEstado(estados);
			logger.debug("total lista tabla saf:"+lst_saf.size());
			
			for (int i=0;i<lst_saf.size();i++){
				SafDTO saf_dao = (SafDTO) lst_saf.get(i);
				logger.debug("saf id:"+saf_dao.getId_saf()
						+", estado="+saf_dao.getEstado()
						+", msg=["+saf_dao.getMsg()+"]");
				
				//cambia el estado a pendiente
				saf_dao.setEstado(Constantes.TCP_SAF_ESTADO_PENDIENTE);
				dao.updSaf(saf_dao);				
				
				// inicio socket				
				if (skCliente.isConnected()){
					os = skCliente.getOutputStream();
					DataOutputStream dos = new DataOutputStream(os);
					
					// rellenar con 2098 ceros
					String conv = saf_dao.getMsg();			
					String cons_msg  = Formatos.formatField(conv, 		Constantes.LARGO_TOTAL_CONS_CUPONES, 		Formatos.ALIGN_LEFT,"0");					
					logger.debug("mensaje saf["+cons_msg+"]");
					//dos.writeBytes(saf_dao.getMsg());
					dos.writeBytes(cons_msg);
					logger.debug("quema de cupon enviada");
					dos.flush();			
					
					skCliente.setReuseAddress(true);
					skCliente.setSoTimeout(5000);
					is = skCliente.getInputStream();
					DataInputStream dis = new DataInputStream( is );
					// bytes a leer
					int a_leer = (HeaderDTO.CUPONES_HEADER_LEN);
					byte[] salida = new byte[a_leer];
					//lee header + cod_retorno
					int leidos = dis.read(salida,0,a_leer);//lee primeros 50					
					logger.debug("lee ["+a_leer+"]bytes AK");
									
					String header_msg_saf = saf_dao.getMsg().substring(0,HeaderDTO.CUPONES_HEADER_LEN);
					String header_recvsaf = new String(salida, 0,leidos);
					
					logger.debug("header enviado ["+header_msg_saf+"]:"+HeaderDTO.CUPONES_HEADER_LEN);
					logger.debug("header recibido["+header_recvsaf+"]:"+leidos);
					
					if (header_msg_saf.equals(header_recvsaf)){						
						logger.debug("AK recibido!!");
						//si no deberia esperar un momento y volver a enviar						
						//marcar con una E tabla de saf
						saf_dao.setEstado(Constantes.TCP_SAF_ESTADO_ENVIADO);
						dao.updSaf(saf_dao);
						logger.debug("Saf["+saf_dao.getId_saf()+"] se marca como E=enviado");
					}		
				}
			}
			logger.debug("termino de procesamiento tabla SAF:"+lst_saf.size());
			//fin socket
			skCliente.close();
			logger.debug("conexion socket finalizada!!");
			
		} catch (UnknownHostException e) {
			//devuelve null
			logger.debug("No encuentra host/puerto ="+host+":"+puerto);
			System.err.println("No encuentra host/puerto ="+host+":"+puerto);			
		} catch (IOException e) {			
			logger.debug("problema de comunicaciones "+host+":"+puerto+" error"+e.getMessage());
			System.err.println("problema de comunicaciones "+host+":"+puerto+" error"+e.getMessage());
			e.printStackTrace();
		} catch (Exception e){			
			logger.debug("error CanjearGenerarCupon:"+e.getMessage());
			System.err.println("error CanjearGenerarCupon:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	
	/**
	 * @return Returns the host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return Returns the puerto.
	 */
	public int getPuerto() {
		return puerto;
	}

	/**
	 * @param puerto The puerto to set.
	 */
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}


	/**
	 * @return Returns the trx.
	 */
	public JdbcTransaccion getTrx() {
		return trx;
	}

	/**
	 * @param trx The trx to set.
	 */
	public void setTrx(JdbcTransaccion trx) {
		this.trx = trx;
	}
	
	
}
