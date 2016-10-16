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
import cl.bbr.jumbocl.pedidos.exceptions.InterfacesException;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.ConsC1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.ConsR1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.ConsultaCuponPorIdDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.ConsultaCuponesPorRutDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.DataConsC1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.DataConsR1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.HeaderDTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.RespC1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.RespR1DTO;
import cl.bbr.jumbocl.pedidos.promos.interfaces.dto.TcpDTO;
import cl.bbr.jumbocl.shared.log.Logging;

public class ClienteTcpPromosCupones {
	private String host;
	private int puerto;
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/* 
	 * CONSULTAS TCP IP
	 */
	public ClienteTcpPromosCupones() {
	
	}
	public ClienteTcpPromosCupones(String host, int puerto) {
		this.host = host;
		this.puerto = puerto;
	}

	/**
	 * Consulta TCPs por Rut de cliente
	 * @param ConsultaCuponesPorRutDTO datos
	 * @return RespR1DTO respuesta
	 */
	public RespR1DTO ConsultaCuponesPorRut(ConsultaCuponesPorRutDTO datos) throws InterfacesException{
		String fyh_actual ="";
		String fecha_actual="";
		String hora_actual="";
		String[] split;
		RespR1DTO reg_resp = new RespR1DTO();		
		Socket skCliente=null;
		OutputStream os=null;
		InputStream is=null;
		
		ConsR1DTO cons_r1 = new ConsR1DTO();
				
		//HEADER : los rellnos los genera ToMsg Automaticamente
		HeaderDTO head = new HeaderDTO();
		
		head.setCanal(Constantes.TCP_TIPO_HEADER_CANAL);
		head.setCadena(Constantes.TCP_TIPO_HEADER_CADENA);
		head.setLocal(""+datos.getCod_local_pos());		
		head.setPos(""+datos.getNum_pos());		
		
		logger.debug("CANAL:"+head.getCanal());
		logger.debug("CADENA:"+head.getCadena());
		
		/* fecha y hora actual */
		fyh_actual = Formatos.getFecHoraActualStr();
		split= fyh_actual.split(" ");	
		fecha_actual = split[0];
		hora_actual = split[1];
		
		head.setFecha(fecha_actual);
		head.setHora(hora_actual);		
		
		// TODO: resolver los datos reales del header
		head.setDocumento(""+datos.getDocumento());
		head.setOperador(""+datos.getOperador());
		head.setJournal(""+datos.getJournal());
		
		head.setTipo_trx(Constantes.TCP_TIPO_CONS_R1);
		head.setFiller("0");
		
		
		//DATA
		DataConsR1DTO data= new DataConsR1DTO();
		data.setRut(""+datos.getRut());
		data.setTipo_cliente(Constantes.TCP_TIPO_CONS_R1_CTE_TIPO_CLIENTE);
		data.setFiller("0");
				
		cons_r1.setHeader(head);
		cons_r1.setData(data);
		// consultar
		try {
			skCliente = new Socket( host , puerto );
			if (skCliente.isConnected()){
				
				//ENVIO
				os = skCliente.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				
				// rellenar con 2098 ceros
				String conv = cons_r1.toMsg();			
				String cons_msg  = Formatos.formatField(conv, 		Constantes.LARGO_TOTAL_CONS_CUPONES, 		Formatos.ALIGN_LEFT,"0");				
				//String cons_msg = cons_r1.toMsg();
				
				logger.debug("consulta r1:["+cons_msg+"]");
				dos.writeBytes(cons_msg);
				logger.debug("consulta r1 enviada!!");
				dos.flush();
				
				//RECEPCION				
				skCliente.setReuseAddress(true);
				skCliente.setSoTimeout(Constantes.CARGAS_PROMO_ARCH_FB_TIMEOUT_MS);
				is = skCliente.getInputStream();
				DataInputStream dis = new DataInputStream( is );
				// bytes a leer
				int a_leer = (HeaderDTO.CUPONES_HEADER_LEN+2);
				byte[] salida = new byte[a_leer];
				//lee header + cod_retorno
				logger.debug("antes de leer resp r1 "+a_leer+" bytes");
				int leidos = dis.read(salida,0,a_leer);//lee primeros 52
				logger.debug("lectura 1:["+new String(salida, 0,leidos)+"] leidos:"+leidos);
				
				String header_msg = conv.substring(0,HeaderDTO.CUPONES_HEADER_LEN);
				String header_recv = new String(salida, 0,HeaderDTO.CUPONES_HEADER_LEN);
				
				logger.debug("header enviado ["+header_msg+"]:"+HeaderDTO.CUPONES_HEADER_LEN);
				logger.debug("header recibido["+header_recv+"]:"+HeaderDTO.CUPONES_HEADER_LEN);
				
				//compara el header enviado con el recibido
				if (header_msg.equals(header_recv)){
					int n=Constantes.TCP_TIPO_CONS_R1_CTE_N;
					int nro_tcps_leidos=0;
					List tcps = new ArrayList();
					String codigo_retorno = new String(salida, HeaderDTO.CUPONES_HEADER_LEN,2);
					
					//revisa si la respuesta viene con error
					if ("00".equals(codigo_retorno)){						
						a_leer=TcpDTO.TCP_LEN+TcpDTO.CANTIDAD_LEN; //4 tcp y 4 cantidad
						byte[] reg = new byte[a_leer];
						for(int i=0;i<n;i++){
							logger.debug("antes de leer "+a_leer+" bytes");
							leidos = dis.read(reg,0,a_leer);//registros de 8
							
							TcpDTO dto = new TcpDTO();
							dto.setNro_tcp(new String(reg, 0,TcpDTO.TCP_LEN));
							dto.setCantidad(Integer.parseInt(new String(reg, TcpDTO.TCP_LEN,TcpDTO.CANTIDAD_LEN)));
							if (!dto.getNro_tcp().equals("0000")){
								logger.debug("TCP:"+dto.getNro_tcp()+" qty:"+dto.getCantidad());
								tcps.add(dto);								
								nro_tcps_leidos++;
							}							
						}
					}
					else{ //viene con error
						logger.debug("Codigo de retorno <>00 = "+codigo_retorno);
						
						a_leer=RespR1DTO.GLOSA1_LEN+RespR1DTO.GLOSA2_LEN; //las 2 glosas 
						byte[] reg = new byte[a_leer];
						leidos = dis.read(reg,0,a_leer);
						
						logger.debug("glosa de Error1 = "+new String(reg, 0,RespR1DTO.GLOSA1_LEN));
						logger.debug("glosa de Error2 = "+new String(reg, RespR1DTO.GLOSA1_LEN, RespR1DTO.GLOSA2_LEN));
						
						reg_resp.setGlosa1(new String(reg, 0,RespR1DTO.GLOSA1_LEN).trim());
						reg_resp.setGlosa2(new String(reg, RespR1DTO.GLOSA1_LEN, RespR1DTO.GLOSA2_LEN).trim());
					}
					//genera la respuesta
					reg_resp.setCod_ret(codigo_retorno);
					reg_resp.setCant_tcps(nro_tcps_leidos);
					reg_resp.setTcps(tcps);					
					logger.debug("Numero de tcps leidos:"+n+" validos:"+nro_tcps_leidos);
				}
				else{
					logger.error("La respuesta de R1 recibida del rut["+datos.getRut()+"] no viene con el header enviado");
				}
				skCliente.close();//cierro el socket				
			}
		} catch (UnknownHostException e) {
			//devuelve null
			logger.error("No encuentra host/puerto ="+host+":"+puerto);	
			throw new InterfacesException("No encuentra host/puerto ="+host+":"+puerto);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("problema de comunicaciones "+host+":"+puerto+" error:"+e.getMessage());		
			throw new InterfacesException("problema de comunicaciones "+host+":"+puerto+" error:"+e.getMessage());
		} catch (Exception e){
			//e.printStackTrace();
			logger.error("error ConsultaCuponesPorRut"+e.getMessage());
			throw new InterfacesException("error ConsultaCuponesPorRut"+e.getMessage());
		}
		//si no hay problema arma respuesta		
		return reg_resp;
	}
	
	/* ** 2. consultas Cupon ***/
	public RespC1DTO ConsultaCuponPorId(ConsultaCuponPorIdDTO datos) throws InterfacesException{
		String fyh_actual ="";
		String fecha_actual="";
		String hora_actual="";
		String[] split;

		Socket skCliente=null;
		OutputStream os=null;
		InputStream is=null;
		
		ConsC1DTO cons_c1 = new ConsC1DTO();
		RespC1DTO reg_resp = new RespC1DTO();
		//HEADER
		HeaderDTO head = new HeaderDTO();
		
		head.setCanal(Constantes.TCP_TIPO_HEADER_CANAL);
		head.setCadena(Constantes.TCP_TIPO_HEADER_CADENA);
		head.setLocal(""+datos.getCod_local_pos());		
		head.setPos(""+datos.getNum_pos());
		
		
		/* fecha y hora actual */
		fyh_actual = Formatos.getFecHoraActualStr();
		split= fyh_actual.split(" ");	
		fecha_actual = split[0];
		hora_actual = split[1];		
				
		head.setFecha(fecha_actual);
		head.setHora(hora_actual);
		// TODO: resolver los datos reales del header
		head.setDocumento(""+datos.getDocumento());
		head.setOperador(""+datos.getOperador());
		head.setJournal(""+datos.getJournal());
		head.setTipo_trx(Constantes.TCP_TIPO_CONS_C1);
		head.setFiller("0");
		
		
		//DATA
		DataConsC1DTO data= new DataConsC1DTO();
		data.setN_cupon(datos.getCupon());
				
		cons_c1.setHeader(head);
		cons_c1.setData(data);
		// consultar
		try {
			skCliente = new Socket( host , puerto );
			if (skCliente.isConnected()){
				
				//envio consulta
				os = skCliente.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				String conv = cons_c1.toMsg();		
				
				//rellenar con 2098 ceros
				String cons_msg  = Formatos.formatField(conv, 		Constantes.LARGO_TOTAL_CONS_CUPONES, 		Formatos.ALIGN_LEFT,"0");
				
				logger.debug("consulta c1:["+cons_msg+"]");
				dos.writeBytes(cons_msg);
				logger.debug("consulta c1 enviada!!");
				dos.flush();
				
				// Recepcion respuesta
				skCliente.setReuseAddress(true);
				skCliente.setSoTimeout(5000);
				is = skCliente.getInputStream();
				DataInputStream dis = new DataInputStream( is );
				// bytes a leer
				int a_leer = (HeaderDTO.CUPONES_HEADER_LEN+2);
				byte[] salida = new byte[a_leer];
				//lee header + cod_retorno
				logger.debug("antes de leer resp c1 de "+a_leer+" bytes");
				int leidos = dis.read(salida,0,a_leer);//lee primeros 52
				logger.debug("lectura 1:["+new String(salida, 0,leidos)+"] leidos:"+leidos);
												
				String header_msg = conv.substring(0,HeaderDTO.CUPONES_HEADER_LEN);
				String header_recv = new String(salida, 0,HeaderDTO.CUPONES_HEADER_LEN);
				
				logger.debug("header enviado ["+header_msg+"]:"+HeaderDTO.CUPONES_HEADER_LEN);
				logger.debug("header recibido["+header_recv+"]:"+HeaderDTO.CUPONES_HEADER_LEN);
				
				if (header_msg.equals(header_recv)){
					String codigo_retorno = new String(salida, HeaderDTO.CUPONES_HEADER_LEN,2);
					logger.debug("codigo retorno:"+codigo_retorno);
					reg_resp.setCod_ret(codigo_retorno);
					if (codigo_retorno.equals("00")){
						// solo se lee un tcp						
						a_leer=TcpDTO.TCP_LEN+TcpDTO.CANTIDAD_LEN; //4 tcp y 4 cantidad=8
						byte[] reg = new byte[a_leer];
						
						//lee la data de la respuesta restante (ya se leyo el header +cod_ret = 52 bytes)
						logger.debug("lee tcp de "+a_leer+" bytes");
						leidos = dis.read(reg,0,a_leer);//registros de 8
						logger.debug(leidos+" bytes");
						
						String tcp = new String(reg, 0,TcpDTO.TCP_LEN);
						String qty = new String(reg, TcpDTO.TCP_LEN,TcpDTO.CANTIDAD_LEN);
						logger.debug("TCP:"+tcp+" CANTIDAD:"+qty);
						TcpDTO dto = new TcpDTO();						
						dto.setNro_tcp(tcp);
						dto.setCantidad(Integer.parseInt(qty));
						
						reg_resp.setTcp(dto);
					}
					else{ //viene con error
						logger.debug("Codigo de retorno <>00 = "+codigo_retorno);					

						a_leer=RespC1DTO.GLOSA1_LEN+RespC1DTO.GLOSA2_LEN; //las 2 glosas 
						byte[] reg = new byte[a_leer];
						leidos = dis.read(reg,0,a_leer);
						
						logger.debug("glosa de Error1 = "+new String(reg, 0,RespC1DTO.GLOSA1_LEN));
						logger.debug("glosa de Error2 = "+new String(reg, RespC1DTO.GLOSA1_LEN,RespC1DTO.GLOSA2_LEN));
						reg_resp.setGlosa1(new String(reg, 0,RespC1DTO.GLOSA1_LEN).trim());
						reg_resp.setGlosa2(new String(reg, RespC1DTO.GLOSA1_LEN,RespC1DTO.GLOSA2_LEN).trim());
					}					
				}
				else{
					logger.error("La respuesta de C1 recibida del cupon["+datos.getCupon()+"] no viene con el header enviado");
				}
				skCliente.close();//cierro el socket				
			}
		} catch (UnknownHostException e) {
			//devuelve null
			logger.error("No encuentra host/puerto ="+host+":"+puerto);	
			throw new InterfacesException("No encuentra host/puerto ="+host+":"+puerto);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("problema de comunicaciones "+host+":"+puerto+" error:"+e.getMessage());		
			throw new InterfacesException("problema de comunicaciones "+host+":"+puerto+" error:"+e.getMessage());
		} catch (Exception e){
			e.printStackTrace();
			logger.error("error ConsultaCuponesPorRut"+e.getMessage());
			throw new InterfacesException("error ConsultaCuponPorId"+e.getMessage());
		}
		//si no hay problema arma respuesta
		return reg_resp;
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
	
	
}
