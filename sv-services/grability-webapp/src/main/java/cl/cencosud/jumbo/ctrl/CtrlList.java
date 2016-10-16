package cl.cencosud.jumbo.ctrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.clientes.dto.UltimasComprasDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantList;
import cl.cencosud.jumbo.bizdelegate.BizDelegateList;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.List.GetInputListDTO;
import cl.cencosud.jumbo.input.dto.List.PostInputListDTO;
import cl.cencosud.jumbo.input.dto.List.PutInputListDTO;
import cl.cencosud.jumbo.output.dto.List.GetOutputListDTO;
import cl.cencosud.jumbo.output.dto.List.PostOutputListDTO;
import cl.cencosud.jumbo.output.dto.List.PutOutputListDTO;
import cl.cencosud.jumbo.util.Util;




public class CtrlList extends Ctrl{
	private GetInputListDTO getListDTO;
	private PostInputListDTO postListDTO;
	private PutInputListDTO putListDTO;
	
	public CtrlList(GetInputListDTO oGetListDTO) {
		super();
		this.getListDTO = oGetListDTO;
	}
	
	/**
	* Constructor para asignar el objeto del tipo PostInputListDTO.
	*/
	public CtrlList(PostInputListDTO oPostListDTO) {
		super();
		this.postListDTO = oPostListDTO;
	}
	
	/**
	* Constructor para asignar el objeto del tipo PutInputListDTO.
	*/	
	public CtrlList(PutInputListDTO oPutListDTO) {
		super();
		this.putListDTO = oPutListDTO;
	}

	public GetOutputListDTO getList() throws GrabilityException{
		GetOutputListDTO outputDTO = new GetOutputListDTO();
		if(ConstantList.ACTION_LISTS.equals(getListDTO.getAction())){
			ArrayList listaFiltradaPorTipo = new ArrayList();
			ArrayList lista = new ArrayList();
			lista = getListas(getListDTO.getClient_id());
			String tipoListaFiltro = "";
			if (ConstantList.LIST_TYPE_MANUAL.equals(getListDTO.getList_type())){
				tipoListaFiltro = "list_type=M";
			} else if (ConstantList.LIST_TYPE_WEB.equals(getListDTO.getList_type())){
				tipoListaFiltro = "list_type=W";
			} else if (ConstantList.LIST_TYPE_ALL.equals(getListDTO.getList_type())){
				listaFiltradaPorTipo=lista;
			} else {
				throw new GrabilityException(ConstantList.SC_ERROR_LIST_TYPE,ConstantList.MSG_ERROR_LIST_TYPE);
			}
			if (!(ConstantList.LIST_TYPE_ALL.equals(getListDTO.getList_type()))){
				for(int i=0; i<lista.size(); i++){
					if (StringUtils.contains(lista.get(i).toString(),tipoListaFiltro)){
						listaFiltradaPorTipo.add(lista.get(i));
					}
				}
			}			
			LinkedHashMap listaSolicitada = new LinkedHashMap();
			listaSolicitada.put(ConstantList.LISTS,listaFiltradaPorTipo);
			ArrayList listaFinal = new ArrayList();
			listaFinal.add(listaSolicitada);
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);
			outputDTO.setTotal(listaFiltradaPorTipo.size());
			outputDTO.setLists(listaFiltradaPorTipo);
		}else if (ConstantList.ACTION_DETAIL.equals(getListDTO.getAction())){
			BizDelegateList biz = new BizDelegateList();
			List productos = new ArrayList();
			String listType = getListDTO.getList_type();
			if ((listType.equals(ConstantList.LIST_TYPE_MANUAL))||(listType.equals(ConstantList.LIST_TYPE_WEB))){
				productos = biz.listaDeProductosByLista(getListDTO.getList_id());
			}else if (listType.equals(ConstantList.LIST_TYPE_PREFERED)){
				productos = biz.listaDeProductosPreferidos(getListDTO.getClient_id());
			}else {
				throw new GrabilityException(ConstantList.SC_ERROR_LIST_TYPE,ConstantList.MSG_ERROR_LIST_TYPE);
			}
			if (productos.isEmpty()){
				throw new GrabilityException(ConstantList.SC_ERROR_LIST_EMPTY,ConstantList.MSG_ERROR_LIST_EMPTY);
			}
			ArrayList prds = new ArrayList();
            for (int i=0; i < productos.size(); i++) {
            	LinkedHashMap detalleProductos = new LinkedHashMap();
            	if(!(listType.equals(ConstantList.LIST_TYPE_PREFERED))){
	            	cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO producto = (cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO) productos.get(i);
	                detalleProductos.put(ConstantList.QUANTITY, ""+producto.getUnidades());
	                detalleProductos.put(ConstantList.PRODUCT_ID, ""+producto.getId());
                }else {
                	ProductoDTO producto = (ProductoDTO) productos.get(i);
                	detalleProductos.put(ConstantList.QUANTITY, ""+producto.getCantidad());
                	detalleProductos.put(ConstantList.PRODUCT_ID, ""+producto.getPro_id());
                }
            	prds.add(detalleProductos);
            }            
			LinkedHashMap prods = new LinkedHashMap();
			prods.put(ConstantList.PRODUCTS, prds);
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);
			outputDTO.setList_type(getListDTO.getList_type());
			outputDTO.setProducts(prds);

		}
		return outputDTO;
			
	
			//LinkedHashMap listaCliente
		
		
	}
	public PostOutputListDTO postList() throws GrabilityException{
		PostOutputListDTO outputDTO = new PostOutputListDTO();
		BizDelegateList biz = new BizDelegateList();
		long idClient = postListDTO.getClient_id();
		String type = postListDTO.getType();
		String listType = postListDTO.getList_type();
		String listName = postListDTO.getList_name();
		JSONArray products = postListDTO.getJsProducts();
		ArrayList productsArray = new ArrayList();
		for (int i=0;i<products.size();i++){ 
			JSONObject prod = products.optJSONObject(i);
			ProductoDTO pro = new ProductoDTO();
			pro.setCantidad(prod.optDouble(ConstantList.QUANTITY));
			pro.setPro_id(prod.optInt(ConstantList.PRODUCT_ID));
		    productsArray.add(pro);
		} 
		long idLista = biz.setCompraHistoricaForMobile(listName, idClient, productsArray);
		List productos = new ArrayList();
		productos = biz.listaDeProductosByLista(idLista);
		ArrayList prds = new ArrayList();
        for (int i=0; i < productos.size(); i++) {
        	LinkedHashMap detalleProductos = new LinkedHashMap();
        	cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO producto = (cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO) productos.get(i);
            detalleProductos.put(ConstantList.QUANTITY, ""+producto.getUnidades());
            detalleProductos.put(ConstantList.PRODUCT_ID, ""+producto.getId());
        	prds.add(detalleProductos);
        }            
		LinkedHashMap prods = new LinkedHashMap();
		prods.put(ConstantList.PRODUCTS, prds);
		ArrayList productosDetalle = new ArrayList();
		productosDetalle.add(prods);
		outputDTO.setStatus(Constant.SC_OK);
		outputDTO.setError_message(Constant.MSG_OK);
		outputDTO.setList_type(ConstantList.LIST_TYPE_MANUAL);
		outputDTO.setList_name(listName);
		outputDTO.setProducts(prds);
		
		return outputDTO;
	}
	
	public PutOutputListDTO putList() throws GrabilityException{
		PutOutputListDTO outputDTO = new PutOutputListDTO();
		
		BizDelegateList biz = new BizDelegateList();
		long idClient = putListDTO.getClient_id();
		String type = putListDTO.getType();
		String listType = putListDTO.getList_type();
		String listName = putListDTO.getList_name();
		long listId = putListDTO.getList_id();
		JSONArray products = putListDTO.getJsProducts();
		ArrayList productsArray = new ArrayList();
		for (int i=0;i<products.size();i++){ 
			JSONObject prod = products.optJSONObject(i);
			ProductoDTO pro = new ProductoDTO();
			pro.setCantidad(prod.optDouble(ConstantList.QUANTITY));
			pro.setPro_id(prod.optInt(ConstantList.PRODUCT_ID));
		    productsArray.add(pro);
		} 
		////////////////////
		long idLista = biz.updateList(idClient, productsArray, listType, listId, listName);
		///////
		List productos = new ArrayList();
		productos = biz.listaDeProductosByLista(idLista);
		ArrayList prds = new ArrayList();
        for (int i=0; i < productos.size(); i++) {
        	LinkedHashMap detalleProductos = new LinkedHashMap();
        	cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO producto = (cl.bbr.jumbocl.pedidos.dto.UltimasComprasDTO) productos.get(i);
            detalleProductos.put(ConstantList.QUANTITY, ""+producto.getUnidades());
            detalleProductos.put(ConstantList.PRODUCT_ID, ""+producto.getId());
        	prds.add(detalleProductos);
        }            
		LinkedHashMap prods = new LinkedHashMap();
		prods.put(ConstantList.PRODUCTS, prds);
		ArrayList productosDetalle = new ArrayList();
		productosDetalle.add(prods);
		outputDTO.setStatus(Constant.SC_OK);
		outputDTO.setError_message(Constant.MSG_OK);
		outputDTO.setList_type(ConstantList.LIST_TYPE_MANUAL);
		outputDTO.setList_name(listName);
		outputDTO.setProducts(prds);
		
		
		return outputDTO;
	}
	
	
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
