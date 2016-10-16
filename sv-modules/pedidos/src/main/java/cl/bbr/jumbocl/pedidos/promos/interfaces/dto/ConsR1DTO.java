package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

import java.io.Serializable;

public class ConsR1DTO  implements Serializable{
	private HeaderDTO header;
	private DataConsR1DTO data;
	
	public ConsR1DTO() {
	}
	
	public String toMsg(){
		String out="";
		out = header.toMsg()+data.toMsg();
		return out;
	}	
	
	/**
	 * @return Returns the data.
	 */
	public DataConsR1DTO getData() {
		return data;
	}
	/**
	 * @param data The data to set.
	 */
	public void setData(DataConsR1DTO data) {
		this.data = data;
	}
	/**
	 * @return Returns the header.
	 */
	public HeaderDTO getHeader() {
		return header;
	}
	/**
	 * @param header The header to set.
	 */
	public void setHeader(HeaderDTO header) {
		this.header = header;
	}
	
	

}
