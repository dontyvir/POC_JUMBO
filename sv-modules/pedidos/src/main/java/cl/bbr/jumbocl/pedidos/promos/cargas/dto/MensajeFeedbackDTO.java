package cl.bbr.jumbocl.pedidos.promos.cargas.dto;

import java.util.List;

public class MensajeFeedbackDTO {
	HeaderFeedBackDTO head;
	List excep;

	public MensajeFeedbackDTO() {		
	}
	
	/**
	 * Devuelve una cadena de caracteres lista para ser enviada con 
	 * el mensaje de feedback.
	 * @return String
	 */
	public String toMsg(){
		String out="";		
		int tipo = Integer.parseInt(head.getTipo());
		out += head.toMsg();		
		if (excep.size()==0){
			return out;
		}
		for (int i=0;i<excep.size();i++){
			if (tipo==1){
				TipoFb01ProductoDTO dto1 = (TipoFb01ProductoDTO)excep.get(i); 
				out +=	dto1.toMsg();
			}
			else if (tipo==2){
				TipoFb02SeccionDTO dto2 = (TipoFb02SeccionDTO)excep.get(i); 
				out +=	dto2.toMsg();
			}					
		}
		return out;		
	}
	
	/**
	 * @return Returns the excep.
	 */
	public List getExcep() {
		return excep;
	}

	/**
	 * @param excep The excep to set.
	 */
	public void setExcep(List excep) {
		this.excep = excep;
	}

	/**
	 * @return Returns the head.
	 */
	public HeaderFeedBackDTO getHead() {
		return head;
	}

	/**
	 * @param head The head to set.
	 */
	public void setHead(HeaderFeedBackDTO head) {
		this.head = head;
	}
	
	

}
