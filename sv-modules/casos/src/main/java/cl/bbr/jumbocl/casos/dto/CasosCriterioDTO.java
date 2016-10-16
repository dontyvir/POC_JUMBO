package cl.bbr.jumbocl.casos.dto;

import java.io.Serializable;

public class CasosCriterioDTO implements Serializable {
	
    private String estado;
    private String alarma;
    private String local;
    private String pedido;
    private String cliRut;
    private String cliApellido;
    private String nroCaso;
    private int pag;
    private int regsperpage;
    private String sistema;
    private String fcIniCreacion;
    private String fcFinCreacion;
    private String fcIniCopromiso;
    private String fcFinCopromiso;
    
	public CasosCriterioDTO() {
		super();
		
	}
	
	public CasosCriterioDTO(String estado, String alarma, String local, String pedido, String cliRut, 
	        String cliApellido, String nroCaso, int pag, int regsperpage, String sistema,
	        String fcIniCreacion, String fcFinCreacion, String fcIniCopromiso, String fcFinCopromiso) {
		super();
		this.estado = estado;
		this.alarma = alarma;
		this.local = local;
		this.pedido = pedido;
		this.cliRut = cliRut;
		this.cliApellido = cliApellido;
		this.nroCaso = nroCaso;
		this.pag = pag;
	    this.regsperpage = regsperpage;	
	    this.sistema = sistema;
	    this.fcIniCreacion = fcIniCreacion;
	    this.fcFinCreacion = fcFinCreacion;
	    this.fcIniCopromiso = fcIniCopromiso;
	    this.fcFinCopromiso = fcFinCopromiso;
	}

    /**
     * @return Devuelve alarma.
     */
    public String getAlarma() {
        return alarma;
    }
    /**
     * @return Devuelve cliApellido.
     */
    public String getCliApellido() {
        return cliApellido;
    }
    /**
     * @return Devuelve cliRut.
     */
    public String getCliRut() {
        return cliRut;
    }
    /**
     * @return Devuelve estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @return Devuelve local.
     */
    public String getLocal() {
        return local;
    }
    /**
     * @return Devuelve nroCaso.
     */
    public String getNroCaso() {
        return nroCaso;
    }
    /**
     * @return Devuelve pedido.
     */
    public String getPedido() {
        return pedido;
    }
    /**
     * @param alarma El alarma a establecer.
     */
    public void setAlarma(String alarma) {
        this.alarma = alarma;
    }
    /**
     * @param cliApellido El cliApellido a establecer.
     */
    public void setCliApellido(String cliApellido) {
        this.cliApellido = cliApellido;
    }
    /**
     * @param cliRut El cliRut a establecer.
     */
    public void setCliRut(String cliRut) {
        this.cliRut = cliRut;
    }
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @param local El local a establecer.
     */
    public void setLocal(String local) {
        this.local = local;
    }
    /**
     * @param nroCaso El nroCaso a establecer.
     */
    public void setNroCaso(String nroCaso) {
        this.nroCaso = nroCaso;
    }
    /**
     * @param pedido El pedido a establecer.
     */
    public void setPedido(String pedido) {
        this.pedido = pedido;
    }
    /**
     * @return Devuelve pag.
     */
    public int getPag() {
        return pag;
    }
    /**
     * @return Devuelve regsperpage.
     */
    public int getRegsperpage() {
        return regsperpage;
    }
    /**
     * @param pag El pag a establecer.
     */
    public void setPag(int pag) {
        this.pag = pag;
    }
    /**
     * @param regsperpage El regsperpage a establecer.
     */
    public void setRegsperpage(int regsperpage) {
        this.regsperpage = regsperpage;
    }
    /**
     * @return Devuelve sistema.
     */
    public String getSistema() {
        return sistema;
    }
    /**
     * @param sistema El sistema a establecer.
     */
    public void setSistema(String sistema) {
        this.sistema = sistema;
    }
    /**
     * @return Devuelve fcFinCopromiso.
     */
    public String getFcFinCopromiso() {
        return fcFinCopromiso;
    }
    /**
     * @return Devuelve fcFinCreacion.
     */
    public String getFcFinCreacion() {
        return fcFinCreacion;
    }
    /**
     * @return Devuelve fcIniCopromiso.
     */
    public String getFcIniCopromiso() {
        return fcIniCopromiso;
    }
    /**
     * @return Devuelve fcIniCreacion.
     */
    public String getFcIniCreacion() {
        return fcIniCreacion;
    }
    /**
     * @param fcFinCopromiso El fcFinCopromiso a establecer.
     */
    public void setFcFinCopromiso(String fcFinCopromiso) {
        this.fcFinCopromiso = fcFinCopromiso;
    }
    /**
     * @param fcFinCreacion El fcFinCreacion a establecer.
     */
    public void setFcFinCreacion(String fcFinCreacion) {
        this.fcFinCreacion = fcFinCreacion;
    }
    /**
     * @param fcIniCopromiso El fcIniCopromiso a establecer.
     */
    public void setFcIniCopromiso(String fcIniCopromiso) {
        this.fcIniCopromiso = fcIniCopromiso;
    }
    /**
     * @param fcIniCreacion El fcIniCreacion a establecer.
     */
    public void setFcIniCreacion(String fcIniCreacion) {
        this.fcIniCreacion = fcIniCreacion;
    }
}
