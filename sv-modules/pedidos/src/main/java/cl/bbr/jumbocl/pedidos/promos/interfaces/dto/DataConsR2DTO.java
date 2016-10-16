package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

import java.io.Serializable;
import java.util.List;

import cl.bbr.jumbocl.common.utils.Formatos;

public class DataConsR2DTO implements Serializable{

	public static final int RUT_LEN          = 8;
	public static final int CANT_TCP_LEN       = 2;
	public static final int CANT_CUPON_LEN       = 2;
	
	private String rut;
	private int cant_tcp;
	private List list_tcp;
	private int cant_cupon;
	private List list_cupon;
	 
	
	public DataConsR2DTO() {
	
	}
	
	public String toMsg(){
		String out = "";
		out += Formatos.formatField(rut, 			RUT_LEN, 			Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(""+cant_tcp, 	CANT_TCP_LEN, 		Formatos.ALIGN_RIGHT,"0");
		//agregar listado tcp
		if (list_tcp!=null){
			for(int i=0; i<list_tcp.size();i++){
				TcpDTO tcp = (TcpDTO)list_tcp.get(i);
				out += tcp.toMsg();		
			}
		}
		out += Formatos.formatField(""+cant_cupon, 	CANT_CUPON_LEN, 		Formatos.ALIGN_RIGHT,"0");		
		//agregar listado cupones
		if (list_cupon!=null){
			for(int i=0; i<list_cupon.size();i++){
				CuponDTO cupon = (CuponDTO)list_cupon.get(i);
				out += cupon.toMsg();		
			}
		}
		return out;
	}

	/**
	 * @return Returns the cant_cupon.
	 */
	public int getCant_cupon() {
		return cant_cupon;
	}

	/**
	 * @param cant_cupon The cant_cupon to set.
	 */
	public void setCant_cupon(int cant_cupon) {
		this.cant_cupon = cant_cupon;
	}

	/**
	 * @return Returns the cant_tcp.
	 */
	public int getCant_tcp() {
		return cant_tcp;
	}

	/**
	 * @param cant_tcp The cant_tcp to set.
	 */
	public void setCant_tcp(int cant_tcp) {
		this.cant_tcp = cant_tcp;
	}

	/**
	 * @return Returns the list_cupon.
	 */
	public List getList_cupon() {
		return list_cupon;
	}

	/**
	 * @param list_cupon The list_cupon to set.
	 */
	public void setList_cupon(List list_cupon) {
		this.list_cupon = list_cupon;
	}

	/**
	 * @return Returns the list_tcp.
	 */
	public List getList_tcp() {
		return list_tcp;
	}

	/**
	 * @param list_tcp The list_tcp to set.
	 */
	public void setList_tcp(List list_tcp) {
		this.list_tcp = list_tcp;
	}

	/**
	 * @return Returns the rut.
	 */
	public String getRut() {
		return rut;
	}

	/**
	 * @param rut The rut to set.
	 */
	public void setRut(String rut) {
		this.rut = rut;
	}

	
}
