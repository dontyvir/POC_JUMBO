package cl.bbr.irsmonitor.dto;

import cl.bbr.irsmonitor.utils.FormatUtils;


public class HeaderPosDTO {
	public static final int EMPRESA_LEN  = 3;
	public static final int LOCAL_LEN    = 3;
	public static final int POS_LEN      = 3;
	public static final int BOLETA_LEN   = 10;
	public static final int TRXSMA_LEN   = 4;
	public static final int FECHA_LEN    = 6;
	public static final int HORA_LEN     = 6;
	public static final int OPERADOR_LEN = 8;
	public static final int TIPOTRX_LEN  = 2;
	public static final int VERSION_LEN  = 1;
	public static final int JOURNAL_LEN  = 6;
	public static final int LARGO_LEN    = 4;

	private String empresa;    	//3 bytes
	private String local;		//3 bytes
	private String pos;			//3 bytes
	private String boleta;		//10 bytes
	private String trxSma;		//4 bytes
	private String fecha;		//6 bytes
	private String hora;		//6 bytes
	private String operador;	//8 bytes
	private String tipoTrx;		//2 bytes
	private String version;		//1 bytes
	private String journal;		//6 bytes
	private String largo;		//4 bytes
	
	public String getBoleta() {
		return boleta;
	}
	public void setBoleta(String boleta) {
		this.boleta = boleta;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	public String getLargo() {
		return largo;
	}
	public void setLargo(String largo) {
		this.largo = largo;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getOperador() {
		return operador;
	}
	public void setOperador(String operador) {
		this.operador = operador;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getTipoTrx() {
		return tipoTrx;
	}
	public void setTipoTrx(String tipoTrx) {
		this.tipoTrx = tipoTrx;
	}
	public String getTrxSma() {
		return trxSma;
	}
	public void setTrxSma(String trxSma) {
		this.trxSma = trxSma;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String toMsg(){
		String out = "";

		out += this.getEmpresa();
		out += this.getLocal();
		out += this.getPos();
		out += this.getBoleta();
		out += this.getTrxSma();
		out += this.getFecha();
		out += this.getHora();
		out += this.getOperador();
		out += this.getTipoTrx();
		out += this.getVersion();
		out += this.getJournal();
		out += FormatUtils.formatField(this.getLargo(), LARGO_LEN, FormatUtils.ALIGN_RIGHT,"0"); // formateamos el largo como número
	
		return out;
		
	}
	
}
