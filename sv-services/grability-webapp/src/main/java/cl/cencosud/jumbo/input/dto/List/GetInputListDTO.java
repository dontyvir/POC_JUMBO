package cl.cencosud.jumbo.input.dto.List;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.Constant.ConstantList;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class GetInputListDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = -8992761449156405584L;

	private int client_id;//"user identifier",
	private String type; //"alphanumeric value representing the origin of the request",
	private int offset;//"optional, used for paging purposes",
	private int limit; //"optional, used for paging purposes",
	private String list_type;//"defines the type of lists, ie (m)amual, (w)eb or (a)ll"
	private int list_id;//"unique identifier for the list",
	private String action;//2 diferent get, 1.-get an array of the user's lists - 2.-get a details of a specic list
	
	public GetInputListDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException{
		super(request,ConstantList.INTEGRATION_CODE_AUTH_GET_LIST,null);
		this.client_id = getParamInt(request, ConstantClient.CLIENT_ID);
		this.type = getParamString(request, Constant.TYPE);
		this.offset = getParamInt(request, ConstantList.OFFSET);
		this.limit = getParamInt(request, ConstantList.LIMIT);
		this.list_type = getParamString(request, ConstantList.LIST_TYPE);
		this.list_id = getParamInt(request, ConstantList.LIST_ID);
		this.action = getParamString(request, Constant.ACTION);
	}
	
	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getList_type() {
		return list_type;
	}

	public void setList_type(String list_type) {
		this.list_type = list_type;
	}

	public int getList_id() {
		return list_id;
	}

	public void setList_id(int list_id) {
		this.list_id = list_id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void isValid() throws ExceptionInParam, GrabilityException {
		if (!(getType().equalsIgnoreCase(Constant.SOURCE))){
			throw new ExceptionInParam(ConstantList.SC_INVALID_SOURCE, ConstantList.MSG_INVALID_SOURCE);
		}
		if (getAction().equals(ConstantList.ACTION_DETAIL)){
			String [] tiposPermitidosDetail = ConstantList.LIST_TYPE_ACTION_DETAIL_PERMITED.split("-");
			boolean isTipoDetallePermitida = false;
			for (int i=0;i<tiposPermitidosDetail.length;i++){
				if (tiposPermitidosDetail[i].equals(getList_type())){
					isTipoDetallePermitida = true;
					break;
				}
			}	
			if (!isTipoDetallePermitida){
				throw new ExceptionInParam(ConstantList.SC_TIPO_LISTA_INVALIDA, ConstantList.MSG_TIPO_LISTA_INVALIDA);
			}
		}else if (getAction().equals(ConstantList.ACTION_LISTS)){
			String [] tiposPermitidosLists = ConstantList.LIST_TYPE_ACTION_LISTS_PERMITED.split("-");
			boolean isTipoListaPermitida = false;
			for (int i=0;i<tiposPermitidosLists.length;i++){
				if (tiposPermitidosLists[i].equals(getList_type())){
					isTipoListaPermitida = true;
					break;
				}
			}
			if (!isTipoListaPermitida){
				throw new ExceptionInParam(ConstantList.SC_TIPO_LISTA_INVALIDA, ConstantList.MSG_TIPO_LISTA_INVALIDA);
			}
		}else {
			throw new ExceptionInParam(ConstantList.SC_INVALID_ACTION, ConstantList.MSG_INVALID_ACTION);
		}
		isValidClientById((long)getClient_id());
		
	}
	
}
