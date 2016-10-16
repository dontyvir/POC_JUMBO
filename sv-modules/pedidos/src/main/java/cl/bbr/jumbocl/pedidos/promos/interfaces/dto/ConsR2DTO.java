package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

import java.io.Serializable;

public class ConsR2DTO  implements Serializable{
	private HeaderDTO header;
	private DataConsR2DTO data;
	
	public ConsR2DTO() {
	}
	
	public String toMsg(){
		String out="";
		out = header.toMsg()+data.toMsg();
		return out;
	}

	/**
	 * @return Returns the data.
	 */
	public DataConsR2DTO getData() {
		return data;
	}

	/**
	 * @param data The data to set.
	 */
	public void setData(DataConsR2DTO data) {
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
