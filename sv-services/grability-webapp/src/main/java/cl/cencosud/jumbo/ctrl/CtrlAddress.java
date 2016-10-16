package cl.cencosud.jumbo.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;
import cl.bbr.jumbocl.pedidos.dto.DireccionMixDTO;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantAddress;
import cl.cencosud.jumbo.bizdelegate.BizDelegateAddress;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.Address.DeleteInputAddressDTO;
import cl.cencosud.jumbo.input.dto.Address.GetInputAddressDTO;
import cl.cencosud.jumbo.input.dto.Address.PostInputAddressDTO;
import cl.cencosud.jumbo.input.dto.Address.PutInputAddressDTO;
import cl.cencosud.jumbo.output.dto.Address.DeleteOutputAddressDTO;
import cl.cencosud.jumbo.output.dto.Address.GetOutputAddressDTO;
import cl.cencosud.jumbo.output.dto.Address.PostOutputAddressDTO;
import cl.cencosud.jumbo.output.dto.Address.PutOutputAddressDTO;

public class CtrlAddress extends Ctrl {
	
	private GetInputAddressDTO getAddressDTO;
	private PostInputAddressDTO postAddressDTO;
	private PutInputAddressDTO putAddressDTO;
	private DeleteInputAddressDTO deleteAddressDTO;
	
	/**
	* Constructor para asignar el objeto del tipo GetInputAddressDTO.
	*/	
	public CtrlAddress(GetInputAddressDTO oGetInputAddressDTO) {
		super();
		this.getAddressDTO = oGetInputAddressDTO;
	}
	
	/**
	* Constructor para asignar el objeto del tipo PostInputAddressDTO.
	*/	
	public CtrlAddress(PostInputAddressDTO oPostInputAddressDTO) {
		super();
		this.postAddressDTO = oPostInputAddressDTO;
	}
	
	/**
	* Constructor para asignar el objeto del tipo PostInputAddressDTO.
	*/	
	public CtrlAddress(PutInputAddressDTO oPutInputAddressDTO) {
		super();
		this.putAddressDTO = oPutInputAddressDTO;
	}
	
	/**
	* Constructor para asignar el objeto del tipo DeleteInputAddressDTO.
	*/	
	public CtrlAddress(DeleteInputAddressDTO oDeleteInputAddressDTO) {
		super();
		this.deleteAddressDTO = oDeleteInputAddressDTO;
	}
	
	/**
	 * Recupera las direcciones de un cliente. 
	 * <p>
	 * Este metodo recupera todas las direcciones de un cliente a traves del objeto getAddressDTO,
	 * setea el objeto de salida outputDTO con las direcciones del cliente.
	 * Retorna un objeto del tipo GetOutputAddressDTO. 
	 *
	 * @return  GetOutputAddressDTO, objeto de salida que implementa el metodo toJson.
	 * @see     GetOutputAddressDTO
	 * @throws	GrabilityException
	 */
	public GetOutputAddressDTO getAddress() throws GrabilityException{
				
		GetOutputAddressDTO outputDTO = new GetOutputAddressDTO();
		ArrayList ListDirecciones = getDireccionesCliente(getAddressDTO.getClient_id(),"GET");
		
		if(ListDirecciones.size() > 0){
		
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);
			outputDTO.setAddresses(ListDirecciones);
			
		}else{
			outputDTO.setStatus(ConstantAddress.SC_CLIENTE_SIN_DIR);
			outputDTO.setError_message(ConstantAddress.MSG_CLIENTE_SIN_DIR);
		}
					
		return outputDTO;		
	}
	
	/**
	 * Ingresa nueva direccion. 
	 * <p>
	 * Este metodo inserta una nueva direccion de un cliente a traves del objeto postAddressDTO,
	 * setea el objeto de salida outputDTO con las direcciones del cliente.
	 * Retorna un objeto del tipo PostOutputAddressDTO. 
	 *
	 * @return  PostOutputAddressDTO, objeto de salida que implementa el metodo toJson.
	 * @see     PostOutputAddressDTO
	 * @throws	GrabilityException
	 */
	public PostOutputAddressDTO postAddress() throws GrabilityException{
				
		PostOutputAddressDTO outputDTO = new PostOutputAddressDTO();
		BizDelegateAddress biz = new BizDelegateAddress();
		
		long com_id = postAddressDTO.getComuna().getId_comuna();
		long tipo_calle = postAddressDTO.getStreet_type();
		
		long cli_id =  postAddressDTO.getClient_id();
		String alias = postAddressDTO.getName();
		String calle = postAddressDTO.getStreet();
		String numero = postAddressDTO.getNumber();
		String depto = postAddressDTO.getHouse_apt();	
		String comentarios = postAddressDTO.getObservation();
		String estado = "A";

		DireccionesDTO dir = new DireccionesDTO( -1, com_id, tipo_calle, cli_id, alias, calle, numero, depto, comentarios,-1, estado );
		biz.clienteInsertDireccion(dir);
		
		ArrayList ListDirecciones = getDireccionesCliente(postAddressDTO.getClient_id(),"POST");
		
		if(ListDirecciones.size() > 0){
		
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);
			outputDTO.setAddresses(ListDirecciones);
			
		}else{
			outputDTO.setStatus(ConstantAddress.SC_CLIENTE_SIN_DIR);
			outputDTO.setError_message(ConstantAddress.MSG_CLIENTE_SIN_DIR);
		}
		
		return outputDTO;		
	}	
	
	/**
	 * Actualiza direccion. 
	 * <p>
	 * Este metodo actualiza la direccion de un cliente a traves del objeto putAddressDTO,
	 * setea el objeto de salida outputDTO con las direcciones del cliente.
	 * Retorna un objeto del tipo PutOutputAddressDTO. 
	 *
	 * @return  PutOutputAddressDTO, objeto de salida que implementa el metodo toJson.
	 * @see     PutOutputAddressDTO
	 * @throws	GrabilityException
	 */
	public PutOutputAddressDTO putAddress() throws GrabilityException{
				
		PutOutputAddressDTO outputDTO = new PutOutputAddressDTO();
		BizDelegateAddress biz = new BizDelegateAddress();
		
		DireccionesDTO oDireccionesDTO = putAddressDTO.getDireccion();
		
		DireccionesDTO dir = new DireccionesDTO();
		
		dir.setId_cliente(putAddressDTO.getClient_id());
		dir.setId(putAddressDTO.getAddress_id());
		TipoCalleDTO otipoCalle = oDireccionesDTO.getTipoCalle();		
		
		if(otipoCalle.getId() != putAddressDTO.getStreet_type()){
			dir.setTipo_calle(putAddressDTO.getStreet_type());	
		}else{
			dir.setTipo_calle(otipoCalle.getId());
		}
				
		if(oDireccionesDTO.getId_com() != putAddressDTO.getComuna().getId_comuna()){
			dir.setCom_id(putAddressDTO.getComuna().getId_comuna());	
		}else{
			dir.setCom_id(oDireccionesDTO.getId_com());
		}				
		
		if(!StringUtils.equals(oDireccionesDTO.getAlias(), putAddressDTO.getName())){
			dir.setAlias(putAddressDTO.getName());
		}else{
			dir.setAlias(oDireccionesDTO.getAlias());
		}
		
		if(!StringUtils.equals(oDireccionesDTO.getCalle(), putAddressDTO.getStreet())){
			dir.setCalle(putAddressDTO.getStreet());
		}else{
			dir.setCalle(oDireccionesDTO.getCalle());
		}
		
		if(!StringUtils.equals(oDireccionesDTO.getNumero(),putAddressDTO.getNumber() )){
			dir.setNumero(putAddressDTO.getNumber());
		}else{
			dir.setNumero(oDireccionesDTO.getNumero());
		}
		
		if(!StringUtils.equals(oDireccionesDTO.getDepto(), putAddressDTO.getHouse_apt())){
			dir.setDepto(putAddressDTO.getHouse_apt());
		}else{
			dir.setDepto(oDireccionesDTO.getDepto());
		}
		
		if(!StringUtils.equals(oDireccionesDTO.getComentarios(),putAddressDTO.getObservation())){
			dir.setComentarios(putAddressDTO.getObservation());
		}else{
			dir.setComentarios(oDireccionesDTO.getComentarios());
		}
		
		biz.clienteUpdateDireccion(dir);		
		ArrayList ListDirecciones = getDireccionesCliente(putAddressDTO.getClient_id(),"PUT");
		
		if(ListDirecciones.size() > 0){
		
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);
			outputDTO.setAddresses(ListDirecciones);
			
		}else{
			outputDTO.setStatus(ConstantAddress.SC_CLIENTE_SIN_DIR);
			outputDTO.setError_message(ConstantAddress.MSG_CLIENTE_SIN_DIR);
		}
		
		return outputDTO;		
	}
	
	/**
	 * Elimina direccion. 
	 * <p>
	 * Este metodo elimina la direccion de un cliente a traves del objeto putAddressDTO,
	 * setea el objeto de salida outputDTO con las direcciones del cliente.
	 * Retorna un objeto del tipo DeleteOutputAddressDTO. 
	 *
	 * @return  DeleteOutputAddressDTO, objeto de salida que implementa el metodo toJson.
	 * @see     DeleteOutputAddressDTO
	 * @throws	GrabilityException
	 */
	public DeleteOutputAddressDTO deleteAddress() throws GrabilityException{
				
		DeleteOutputAddressDTO outputDTO = new DeleteOutputAddressDTO();
		BizDelegateAddress biz = new BizDelegateAddress();		

		DireccionMixDTO oDireccionMixDTO = biz.getDireccionIniciaSessionCliente(deleteAddressDTO.getClient_id());
		if(oDireccionMixDTO.getSes_dir_id() != deleteAddressDTO.getAddress_id()){
			biz.clienteDeleteDireccion(deleteAddressDTO.getAddress_id());		
		}
		
		DireccionesDTO oDireccionesDTO = biz.getDireccionByIdDir(deleteAddressDTO.getAddress_id());
		
		if(oDireccionesDTO != null && StringUtils.equals("E", oDireccionesDTO.getEstado())){
		
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);
			
		}else{
			outputDTO.setStatus(ConstantAddress.SC_ERROR_DELETE_DIR);
			outputDTO.setError_message(ConstantAddress.MSG_ERROR_DELETE_DIR);
		}
		
		return outputDTO;		
	}
	
	//recupera direcciones del cliente
	private ArrayList getDireccionesCliente(long idCliente, String method) throws GrabilityException{
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
			LocalDTO oLocal = biz.getLocalById(oDireccionesDTO.getLoc_cod());
			
			if(StringUtils.equals("GET", method)){
				Address.put("local_code", String.valueOf(oLocal.getCod_local()));
			}
			
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

}
