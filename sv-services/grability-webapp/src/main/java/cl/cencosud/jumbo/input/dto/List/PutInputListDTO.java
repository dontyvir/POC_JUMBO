package cl.cencosud.jumbo.input.dto.List;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantClient;
import cl.cencosud.jumbo.Constant.ConstantList;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class PutInputListDTO extends InputDTO implements Serializable {

	private static final long serialVersionUID = 5321749896825322803L;

	private int client_id;//"user identifier",
	private int list_id;//"unique identifier for the list",
	private String type;//"alphanumeric value representing the origin of the request",
	private String list_type;//"should always be w",
	private String list_name;//"name of the list",

	private String products; //{"quantity": "how many of this product is in this list","product_id": "product id (FO id)"}
    private JSONArray jsProducts; //Products
    
	public PutInputListDTO(HttpServletRequest request) throws ExceptionInParam, GrabilityException{
		super(request,ConstantList.INTEGRATION_CODE_AUTH_PUT_LIST,null);
		this.client_id = getParamInt(request, ConstantClient.CLIENT_ID);
		this.list_id = getParamInt(request, ConstantList.LIST_ID);
		this.type = getParamString(request, Constant.TYPE);
		this.list_type = getParamString(request, ConstantList.LIST_TYPE);
		this.list_name = getParamString(request, ConstantList.LIST_NAME);
		this.products = getParamString(request, ConstantPurchaseOrder.PRODUCTS);
		try{
			this.jsProducts = (JSONArray) JSONSerializer.toJSON(this.products);
		}catch(Exception e){
			this.jsProducts = null;
		}
	}
	
	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}

	public int getList_id() {
		return list_id;
	}

	public void setList_id(int list_id) {
		this.list_id = list_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getList_type() {
		return list_type;
	}

	public void setList_type(String list_type) {
		this.list_type = list_type;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getList_name() {
		return list_name;
	}

	public void setList_name(String list_name) {
		this.list_name = list_name;
	}

	public JSONArray getJsProducts() {
		return jsProducts;
	}

	public void setJsProducts(JSONArray jsProducts) {
		this.jsProducts = jsProducts;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void isValid() throws ExceptionInParam, GrabilityException {
		
		if (!(getType().equalsIgnoreCase(Constant.SOURCE))){
			throw new ExceptionInParam(ConstantList.SC_INVALID_SOURCE, ConstantList.MSG_INVALID_SOURCE);
		}
		String [] tiposPermitidosLists = ConstantList.LIST_TYPE_PUT_PERMITED.split("-");
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
		
		if (("".equalsIgnoreCase(getList_name())) || (getList_name()==null)){
			throw new ExceptionInParam(ConstantList.SC_INVALID_LIST_NAME, ConstantList.MSG_INVALID_LIST_NAME);
		}
		isValidClientById((long)getClient_id());

	}
	
}
