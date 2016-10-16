package cl.cencosud.jumbo.ctrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.DireccionMixDTO;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.bizdelegate.BizDelegateAddress;
import cl.cencosud.jumbo.bizdelegate.BizDelegateClient;
import cl.cencosud.jumbo.bizdelegate.BizDelegateList;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.Client.GetInputClientDTO;
import cl.cencosud.jumbo.input.dto.Client.PostInputClientDTO;
import cl.cencosud.jumbo.input.dto.Client.PutInputClientDTO;
import cl.cencosud.jumbo.output.dto.Client.GetOutputClientDTO;
import cl.cencosud.jumbo.output.dto.Client.PostOutputClientDTO;
import cl.cencosud.jumbo.output.dto.Client.PutOutputClientDTO;
import cl.cencosud.jumbo.util.RequestUtils;
import cl.cencosud.jumbo.util.Util;

public class CtrlClient extends Ctrl {
	
	private GetInputClientDTO getClientDTO;
	private PostInputClientDTO postClientDTO;
	private PutInputClientDTO putClientDTO;
	
	/**
	* Constructor para asignar el objeto del tipo GetInputClientDTO.
	*/	
	public CtrlClient(GetInputClientDTO oGetClientDTO) {
		super();
		this.getClientDTO = oGetClientDTO;
	}
	
	/**
	* Constructor para asignar el objeto del tipo PostInputClientDTO.
	*/
	public CtrlClient(PostInputClientDTO oPostClientDTO) {
		super();
		this.postClientDTO = oPostClientDTO;
	}
	
	/**
	* Constructor para asignar el objeto del tipo PutInputClientDTO.
	*/	
	public CtrlClient(PutInputClientDTO oPutClientDTO) {
		super();
		this.putClientDTO = oPutClientDTO;
	}
	
	/**
	 * Valida cliente y genera un objeto de salida del tipo GetOutputClientDTO. 
	 * <p>
	 * Este metodo valida al cliente rut y contraseña a traves del objeto getClientDTO,
	 * setea el objeto de salida outputDTO con los datos del cliente si existe y el status correspondiente 
	 * a lo procesado en el metodo.
	 * Retorna un objeto del tipo GetOutputClientDTO. 
	 *
	 * @return  GetOutputClientDTO, objeto de salida que implementa el metodo toJson.
	 * @see     GetOutputClientDTO
	 * @throws	GrabilityException
	 */
	public GetOutputClientDTO getClient() throws GrabilityException{
				
		GetOutputClientDTO outputDTO = new GetOutputClientDTO();
		
		if(!StringUtils.isBlank(getClientDTO.get_pwd())){	
			
			String pwd = "";
			if(StringUtils.equals("des",getClientDTO.get_act())){
				pwd = RequestUtils.decoderPass(getClientDTO.get_pwd());
			}else{
				pwd = RequestUtils.encoderPass(getClientDTO.get_pwd());
			}
				
			if(!StringUtils.isBlank(pwd)){
				outputDTO.setStatus(Constant.SC_OK);//Correcto
				outputDTO.setError_message(Constant.MSG_OK);
				outputDTO.set_pwd(pwd);
			}else{
				outputDTO.setStatus(ConstantClient.SC_ENC_PASS);//Incorrecto
				outputDTO.setError_message(ConstantClient.MSG_ENC_PASS);
			}
				 
		}else{
			
			//BizDelegateClient biz = new BizDelegateClient();
			ClientesDTO oClientesDTO = this.getClientDTO.getCliente();			
		
			outputDTO.setStatus(Constant.SC_OK);//Correcto
			outputDTO.setError_message("");
			outputDTO.setCliente_id(oClientesDTO.getId_cliente());
			outputDTO.setRut((int)oClientesDTO.getRut());
			outputDTO.setDv(oClientesDTO.getDv());			
			outputDTO.setName(oClientesDTO.getNombre());
			outputDTO.setLast_name(oClientesDTO.getPaterno());
			outputDTO.setEmail(oClientesDTO.getEmail());			
			//outputDTO.setCod_phone_number(oClientesDTO.getCodfono1());
			outputDTO.setCod_phone_number((StringUtils.isBlank(oClientesDTO.getCodfono1()))? "0":oClientesDTO.getCodfono1());
			outputDTO.setPhone_number((StringUtils.isBlank(oClientesDTO.getFono1()))? "0":oClientesDTO.getFono1());
			//outputDTO.setCod_phone_number2(oClientesDTO.getCodfono2());
			outputDTO.setCod_phone_number2((StringUtils.isBlank(oClientesDTO.getCodfono2()))? "0":oClientesDTO.getCodfono2());
			outputDTO.setPhone_number2((StringUtils.isBlank(oClientesDTO.getFono2()))? "0":oClientesDTO.getFono2());
			outputDTO.setNewsletter(oClientesDTO.getCli_envio_mail());
			
			//DIRECCIONES
			outputDTO.setAddresses(getDireccionesCliente(oClientesDTO.getId_cliente()));
			
			//LISTAS
			outputDTO.setLists(getListas(oClientesDTO.getId_cliente())); 
			
		}
		return outputDTO;
	}
	
	/**
	 * Crea cliente y recupera contraseña, genera un objeto de salida del tipo PostOutputClientDTO. 
	 * <p>
	 * Este metodo crea un nuevo cliente jumbo.cl y permite recuperar la contraseña de un cliente antiguo, a traves del objeto postClientDTO,
	 * setea el objeto de salida outputDTO con los datos del cliente creado y el status correspondiente 
	 * a lo procesado en el metodo. Retorna un objeto del tipo PostOutputClientDTO. 
	 *
	 * @return  PostOutputClientDTO, objeto de salida que implementa el metodo toJson.
	 * @see     PostOutputClientDTO
	 * @throws	GrabilityException
	 */
	public PostOutputClientDTO postClient() throws GrabilityException{
		
		PostOutputClientDTO outputDTO = new PostOutputClientDTO();		
		BizDelegateClient biz = new BizDelegateClient();
			
		if(ConstantClient.INSERT.equals(postClientDTO.getAction())){	
			
			outputDTO.setAction(ConstantClient.INSERT);
			String pass="";
			try{
				//String passDC = URLDecoder.decode(postClientDTO.getPassword(), Constant.ENCODING);
				pass=Utils.encriptarFO(postClientDTO.getPassword());
			} catch (Exception e) {
				throw new GrabilityException(ConstantClient.SC_ERROR_AL_CREAR_PASS_CLIENTE, ConstantClient.MSG_ERROR_AL_CREAR_PASS_CLIENTE, e);
			}			

			ClienteDTO cliente = new ClienteDTO();
			cliente.setRut(postClientDTO.getRut());
			cliente.setDv(postClientDTO.getDv());
			cliente.setNombre(postClientDTO.getName());
			cliente.setApellido_pat(postClientDTO.getLast_name());
			cliente.setClave(pass);
			cliente.setEmail(postClientDTO.getEmail());
			cliente.setRecibeEMail(postClientDTO.getNewsletter());

			cliente.setFon_cod_1(String.valueOf(postClientDTO.getCod()));
			cliente.setFon_num_1(String.valueOf(postClientDTO.getNumber()));

			cliente.setFon_cod_2(String.valueOf(postClientDTO.getCod2())); // cli_fon_cod_2
			cliente.setFon_num_2(String.valueOf(postClientDTO.getNumber2())); // cli_fon_num_2

			//VALORES DUMMY
			cliente.setApellido_mat(""); // cli_apellido_mat
			cliente.setRecibeSms(0);
			cliente.setEstado("A"); // cli_estado
			Calendar cal = Calendar.getInstance();
			cliente.setFec_nac(cal.getTimeInMillis()); // cli_fec_nac
			cliente.setGenero(" "); // cli_genero
			cliente.setPregunta("--"); // cli_pregunta
			cliente.setRespuesta("--"); // cli_respuesta  
			

			// CREA DIRECCIÓN DUMMY
			//=======================				
			DireccionesDTO d = new DireccionesDTO();
			d.setCom_id(postClientDTO.getMunicipality_id());
			d.setReg_id(postClientDTO.getRegion_id());
            DireccionesDTO desp = biz.crearDireccionDummy(d); 

			long idCliente = 0;
			//long idDireccion = 0;

			String idCliDir = biz.clienteNewRegistro(cliente, desp);
			String ids[] = idCliDir.split("-");
			idCliente    = Long.parseLong(ids[0]);
			//idDireccion  = Long.parseLong(ids[1]);

			ClientesDTO oCliente = biz.getClienteById(idCliente);

			outputDTO.setStatus(Constant.SC_OK);//Correcto
			outputDTO.setError_message("");
			outputDTO.setCliente_id(oCliente.getId_cliente());
			outputDTO.setName(oCliente.getNombre());
			outputDTO.setLast_name(oCliente.getPaterno());
			outputDTO.setEmail(oCliente.getEmail());
			//outputDTO.setCod_phone_number(oCliente.getCodfono1());
			outputDTO.setCod_phone_number((StringUtils.isBlank(oCliente.getCodfono1()))? "0":oCliente.getCodfono1());
			outputDTO.setPhone_number((StringUtils.isBlank(oCliente.getFono1()))? "0":oCliente.getFono1());
			//outputDTO.setCod_phone_number2(oCliente.getCodfono2());
			outputDTO.setCod_phone_number2((StringUtils.isBlank(oCliente.getCodfono2()))? "0":oCliente.getCodfono2());
			outputDTO.setPhone_number2((StringUtils.isBlank(oCliente.getFono2()))? "0":oCliente.getFono2());
			outputDTO.setNewsletter(oCliente.getCli_envio_mail());
	
			//DIRECCIONES
			outputDTO.setAddresses(getDireccionesCliente(idCliente));

			//LISTAS
			outputDTO.setLists(getListas(idCliente)); 			

		}else if(ConstantClient.RECOVER.equals(postClientDTO.getAction())){
			
			ClientesDTO oClientesDTO = postClientDTO.getCliente();
			
			ClienteDTO cliente =new ClienteDTO();
			cliente.setId(oClientesDTO.getId_cliente());
			cliente.setRut(oClientesDTO.getRut());
			cliente.setDv(oClientesDTO.getDv());
			cliente.setNombre(oClientesDTO.getNombre());
			cliente.setApellido_pat(oClientesDTO.getPaterno());
			cliente.setEmail(oClientesDTO.getEmail());
			
			String contextPath=postClientDTO.getContextPath();
			biz.recuperaClaveFO(cliente,contextPath);
			
			outputDTO.setAction(ConstantClient.RECOVER);
			outputDTO.setEmail(cliente.getEmail());
			outputDTO.setStatus(Constant.SC_OK);
            outputDTO.setError_message("");

			
		}else if(ConstantClient.GUEST.equals(postClientDTO.getAction())){				
				
			Date d = new Date();
			String key = "GRB_"+d.getTime();
			
        	if(key.length() > 60 )
        		key = ("GRB_"+d.getTime()).substring(0,60).trim();
        	
			int idClienteInvitado = biz.crearSesionInvitado(key);

			outputDTO.setAction(ConstantClient.GUEST);
			outputDTO.setStatus(Constant.SC_OK);//Correcto
			outputDTO.setError_message("");
			outputDTO.setCliente_id(idClienteInvitado);
			outputDTO.setName("Invitado");
			outputDTO.setLast_name("");
			outputDTO.setEmail("");
			outputDTO.setPhone_number("");
			outputDTO.setPhone_number2("");

		}else if(ConstantClient.LOGIN.equals(postClientDTO.getAction())){
			
			ClientesDTO oClientesDTO = this.postClientDTO.getCliente();	
			String pass="";
			try {
				//Clave invalida.
				pass=Utils.encriptarFO(postClientDTO.getPassword());				
			} catch (Exception e) {
				throw new GrabilityException(ConstantClient.SC_ERROR_AUTH_CLIENTE, ConstantClient.MSG_ERROR_AUTH_CLIENTE, e);
			}
			
			outputDTO.setAction(ConstantClient.LOGIN);
				
			if( oClientesDTO.getClave().compareTo(pass) != 0 ){
				outputDTO.setStatus(ConstantClient.SC_CLAVE_INVALIDA);
				outputDTO.setError_message(ConstantClient.MSG_CLAVE_INVALIDA);					
			}else{
				
				outputDTO.setStatus(Constant.SC_OK);//Correcto
				outputDTO.setError_message("");
				outputDTO.setCliente_id(oClientesDTO.getId_cliente());
				outputDTO.setName(oClientesDTO.getNombre());
				outputDTO.setLast_name(oClientesDTO.getPaterno());
				outputDTO.setEmail(oClientesDTO.getEmail());
				outputDTO.setPhone_number(oClientesDTO.getFono1());
				outputDTO.setPhone_number2(oClientesDTO.getFono2());
				outputDTO.setNewsletter(oClientesDTO.getCli_envio_mail());
				
				//DIRECCIONES
				outputDTO.setAddresses(getDireccionesCliente(oClientesDTO.getId_cliente()));
				
				//LISTAS
				outputDTO.setLists(getListas(oClientesDTO.getId_cliente())); 				
			}
			
		}
		
		return outputDTO;
	}
	
	/**
	 * Actualiza cliente, genera un objeto de salida del tipo PutOutputClientDTO. 
	 * <p>
	 * Este metodo actualiza los datos de un antiguo cliente jumbo.cl, a traves del objeto postClientDTO,
	 * setea el objeto de salida outputDTO con los datos del cliente creado y el status correspondiente 
	 * a lo procesado en el metodo. Retorna un objeto del tipo PutOutputClientDTO. 
	 *
	 * @return  PutOutputClientDTO, objeto de salida que implementa el metodo toJson.
	 * @see     PutOutputClientDTO
	 * @throws	GrabilityException
	 */
	public PutOutputClientDTO putClient() throws GrabilityException{
		
		PutOutputClientDTO outputDTO = new PutOutputClientDTO();		
		BizDelegateClient biz = new BizDelegateClient();
		
		String password="";
		String newPassword="";
		try{
			password=Utils.encriptarFO(putClientDTO.getPassword());
			newPassword=Utils.encriptarFO(putClientDTO.getNew_password());
		} catch (Exception e) {
			throw new GrabilityException(ConstantClient.SC_ERROR_AL_CREAR_PASS_CLIENTE, ConstantClient.MSG_ERROR_AL_CREAR_PASS_CLIENTE, e);
		}
							
		ClientesDTO oClientes = putClientDTO.getCliente();//Datos Actuales
		
		ClienteDTO cliente = new ClienteDTO();			 
		
		//cliente.setRut(putClientDTO.getRut());
		//cliente.setDv(putClientDTO.getDv());
		
		if(!StringUtils.isBlank(putClientDTO.getName()) && !StringUtils.equals(oClientes.getNombre(), putClientDTO.getName())){
			cliente.setNombre(putClientDTO.getName());
		}else{//Se mantiene
			cliente.setNombre(oClientes.getNombre());
		}
		
		if(!StringUtils.isBlank(putClientDTO.getLast_name()) && !StringUtils.equals(oClientes.getPaterno(),putClientDTO.getLast_name())){
			cliente.setApellido_pat(putClientDTO.getLast_name());
		}else{//Se mantiene
			cliente.setApellido_pat(oClientes.getPaterno());
		}

		cliente.setApellido_mat(oClientes.getMaterno());
		
		if(StringUtils.equals(oClientes.getClave(), password) && !StringUtils.equals(password, newPassword)){
			cliente.setClave(newPassword);
		}else{
			cliente.setClave(oClientes.getClave());
		}
		
		if(!StringUtils.isBlank(putClientDTO.getEmail()) && !StringUtils.equals(oClientes.getEmail(),putClientDTO.getEmail())){
			cliente.setEmail(putClientDTO.getEmail());
		}else{//Se mantiene
			cliente.setEmail(oClientes.getEmail());
		}
		
		if(oClientes.getCli_envio_mail() != putClientDTO.getNewsletter()){
			cliente.setRecibeEMail(putClientDTO.getNewsletter());
		}else{
			cliente.setRecibeEMail(oClientes.getCli_envio_mail());
		}
		
		
		//Telefono 1
		if(!StringUtils.equals(oClientes.getCodfono1(), String.valueOf(putClientDTO.getCod()))){
			cliente.setFon_cod_1(String.valueOf(putClientDTO.getCod()));
		}else{
			cliente.setFon_cod_1(oClientes.getCodfono1());
		}
		
		if(!StringUtils.equals(oClientes.getFono1(),String.valueOf(putClientDTO.getNumber()))){
			cliente.setFon_num_1(String.valueOf(putClientDTO.getNumber()));
		}else{
			cliente.setFon_num_1(oClientes.getFono1());
		}
		
		//Telefono 2
		if(!StringUtils.equals(oClientes.getCodfono2(), String.valueOf(putClientDTO.getCod2()))){
			cliente.setFon_cod_2(String.valueOf(putClientDTO.getCod2()));
		}else{
			cliente.setFon_cod_2(oClientes.getCodfono2());
		}
		
		if(!StringUtils.equals(oClientes.getFono2(), String.valueOf(putClientDTO.getNumber2()))){
			cliente.setFon_num_2(String.valueOf(putClientDTO.getNumber2()));
		}else{
			cliente.setFon_num_2(oClientes.getFono2());
		}
		
		cliente.setFon_cod_3(oClientes.getCodfono3());
		cliente.setFon_num_3(oClientes.getFono3());
		oClientes.setFnac(oClientes.getFnac());
		oClientes.setGenero(oClientes.getGenero());
		
		
		cliente.setPregunta(oClientes.getPregunta());
		cliente.setRespuesta(oClientes.getRespuesta());
		cliente.setCli_mod_dato(oClientes.getCli_mod_dato());
		cliente.setRecibeSms(oClientes.getRecibeSms());
		
		cliente.setRut(oClientes.getRut());
		cliente.setDv(oClientes.getDv());
		cliente.setEstado(oClientes.getEstado());
		cliente.setGenero(oClientes.getGenero());
		
		cliente.setId(oClientes.getId_cliente());
		
		//Actualiza clientes.
		biz.clienteUpdate(cliente);
		
		//Rescata datos actualizados.
		ClientesDTO oCliente = biz.getClienteById(oClientes.getId_cliente());
		outputDTO.setStatus(Constant.SC_OK);//Correcto
		outputDTO.setError_message("");
		outputDTO.setCliente_id(oCliente.getId_cliente());
		outputDTO.setName(oCliente.getNombre());
		outputDTO.setLast_name(oCliente.getPaterno());
		outputDTO.setEmail(oCliente.getEmail());
		outputDTO.setCod_phone_number((StringUtils.isBlank(oCliente.getCodfono1()))? "0":oCliente.getCodfono1());
		//outputDTO.setPhone_number(oCliente.getFono1());
		outputDTO.setPhone_number((StringUtils.isBlank(oCliente.getFono1()))? "0":oCliente.getFono1());
		outputDTO.setCod_phone_number2((StringUtils.isBlank(oCliente.getCodfono2()))? "0":oCliente.getCodfono2());
		//outputDTO.setPhone_number2(oCliente.getFono2());
		outputDTO.setPhone_number2((StringUtils.isBlank(oCliente.getFono2()))? "0":oCliente.getFono2());
		outputDTO.setNewsletter(oCliente.getCli_envio_mail());
		
		//DIRECCIONES
		outputDTO.setAddresses(getDireccionesCliente(oClientes.getId_cliente()));

		//LISTAS
		outputDTO.setLists(getListas(oClientes.getId_cliente())); 

		
		return outputDTO;
	}
	
	//recupera direcciones del cliente
	private ArrayList getDireccionesCliente(long idCliente) throws GrabilityException{
		BizDelegateAddress biz = new BizDelegateAddress();
		ArrayList ListDirecciones =  new ArrayList();
		
		List direcciones = biz.getDireccionesByIdCliente(idCliente);
		DireccionMixDTO oDireccionMixDTO = biz.getDireccionIniciaSessionCliente(idCliente);
	
		Iterator it = direcciones.iterator();
		while(it.hasNext()){
			DireccionesDTO oDireccionesDTO = (DireccionesDTO) it.next();
			HashMap Address = new HashMap();
			Address.put("address_id", String.valueOf(oDireccionesDTO.getId()));
			Address.put("name", oDireccionesDTO.getAlias());
			Address.put("street", oDireccionesDTO.getCalle());
			Address.put("number", oDireccionesDTO.getNumero());
			Address.put("house_apt", oDireccionesDTO.getDepto());
			Address.put("municipality_id", String.valueOf(oDireccionesDTO.getCom_id()));
			Address.put("region_id",  String.valueOf(oDireccionesDTO.getReg_id()));
			Address.put("observation", oDireccionesDTO.getComentarios());
			if(oDireccionMixDTO.getSes_dir_id() == oDireccionesDTO.getId()){
				Address.put("default", Constant.BOOLEAN_TRUE);
			}else{
				Address.put("default", Constant.BOOLEAN_FALSE);
			}
			Address.put("local_id", String.valueOf(oDireccionesDTO.getLoc_cod()));
			if(biz.isDireccionDummy(oDireccionesDTO)){
				Address.put("dummy", Constant.BOOLEAN_TRUE);
			}else{				
				Address.put("dummy", Constant.BOOLEAN_FALSE);
			}			
			Address.put("street_type", String.valueOf(oDireccionesDTO.getTipo_calle()));
			
			ListDirecciones.add(Address);
		}
		
		return ListDirecciones;
	}
	
	//obtiene listas del cliente
	private ArrayList getListas(long idCliente) throws GrabilityException{
		
		BizDelegateList biz = new BizDelegateList();
		ArrayList Listlist =  new ArrayList();
		
		
		Date date = new Date();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		
		//Obtiene listas tipo W
		List comprasInternet = biz.clienteGetUltComprasInternet(idCliente);//W						
		Iterator itci = comprasInternet.iterator();
		while(itci.hasNext()){
			UltimasComprasDTO oUltimasComprasDTO = (UltimasComprasDTO) itci.next();
			HashMap list = new HashMap();
			list.put("list_id", String.valueOf(oUltimasComprasDTO.getId()));
			list.put("list_name", oUltimasComprasDTO.getNombre());
			
			if(oUltimasComprasDTO.getFecha() > 0){
				date.setTime(oUltimasComprasDTO.getFecha());
				list.put("creation_date", Util.formatoFechaGrability(dateTimeFormat.format(date)));
			}else{
				list.put("creation_date", "");
			}
						
			list.put("list_type", "W");
			Listlist.add(list);

		}

		//Obtiene listas tipo M
		List misListas = biz.clienteMisListas(idCliente);//M						
		Iterator itml = misListas.iterator();
		while(itml.hasNext()){
			UltimasComprasDTO oUltimasComprasDTO = (UltimasComprasDTO) itml.next();
			HashMap list = new HashMap();
			list.put("list_id", String.valueOf(oUltimasComprasDTO.getId()));
			list.put("list_name", oUltimasComprasDTO.getNombre());
			//list.put("creation_date", String.valueOf(oUltimasComprasDTO.getFecha()));		
			
			if(oUltimasComprasDTO.getFecha() > 0){
				date.setTime(oUltimasComprasDTO.getFecha());
				list.put("creation_date", Util.formatoFechaGrability(dateTimeFormat.format(date)));
			}else{
				list.put("creation_date", "");
			}
			
			list.put("list_type", "M");
			Listlist.add(list);						
		}		
		
		return Listlist;

	}

}
