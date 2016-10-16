package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcModFichaTecnicaDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
      private long pftProId; // obligatorio      
      private long pftPfiItem;      
      private long pftPfiSecuencia;      
      private String pftDescripcionItem;      
	  private int pftEstadoItem;	 
      private String mensaje;
      private String usr_login;
      
	/**
	 * 
	 */
	public ProcModFichaTecnicaDTO() {
	}

	public ProcModFichaTecnicaDTO(long pftProId, long pftPfiItem,
			long pftPfiSecuencia, String pftDescripcionItem, int pftEstadoItem,
			String mensaje, String usr_login) {
		super();
		this.pftProId = pftProId;
		this.pftPfiItem = pftPfiItem;
		this.pftPfiSecuencia = pftPfiSecuencia;
		this.pftDescripcionItem = pftDescripcionItem;
		this.pftEstadoItem = pftEstadoItem;
		this.mensaje = mensaje;
		this.usr_login = usr_login;
	}

	public long getPftProId() {
		return pftProId;
	}
	public void setPftProId(long pftProId) {
		this.pftProId = pftProId;
	}
	public long getPftPfiItem() {
		return pftPfiItem;
	}
	public void setPftPfiItem(long pftPfiItem) {
		this.pftPfiItem = pftPfiItem;
	}
	public long getPftPfiSecuencia() {
		return pftPfiSecuencia;
	}
	public void setPftPfiSecuencia(long pftPfiSecuencia) {
		this.pftPfiSecuencia = pftPfiSecuencia;
	}
	public String getPftDescripcionItem() {
		return pftDescripcionItem;
	}
	public void setPftDescripcionItem(String pftDescripcionItem) {
		this.pftDescripcionItem = pftDescripcionItem;
	}
	public int getPftEstadoItem() {
		return pftEstadoItem;
	}
	public void setPftEstadoItem(int pftEstadoItem) {
		this.pftEstadoItem = pftEstadoItem;
	}
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getUsr_login() {
		return usr_login;
	}

	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}
		
	 public String toString() {
	        return "[pftProId : " + pftProId + "]\n"+
	        	   "[pftPfiItem :" + pftPfiItem +"]\n"+
	        	   "[pftPfiSecuencia :" + pftPfiSecuencia +"]\n"+
	        	   "[pftDescripcionItem :" + pftDescripcionItem +"]\n"+
	        	   "[pftEstadoItem :" + pftEstadoItem +"]\n"+
	        	   "[mensaje :" + mensaje +"]\n"+
	        	   "[usr_login :" + usr_login +"]";	               
	 }	
}