package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

public class ConsC1DTO {
	HeaderDTO header;
	DataConsC1DTO data;
	
	public ConsC1DTO() {	
	}

	public String toMsg(){
		String out="";
		out = header.toMsg()+data.toMsg();
		return out;
	}	
	
	/**
	 * @return Returns the data.
	 */
	public DataConsC1DTO getData() {
		return data;
	}

	/**
	 * @param data The data to set.
	 */
	public void setData(DataConsC1DTO data) {
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
