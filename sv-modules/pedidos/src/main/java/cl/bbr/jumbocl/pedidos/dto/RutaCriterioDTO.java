package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class RutaCriterioDTO implements Serializable {
    
    String fechaDespacho;
    long idZona;
    long idChofer;
    long idPatente;
    long idEstado;
    long idRuta;
    long idPedido;
    String clienteRut;
    String clienteApellido;
    int pag;
    int regsperpage;
    long idLocal;
    String hIni;
    String hFin;    

    public RutaCriterioDTO(String fechaDespacho, long idZona, long idChofer, long idPatente, long idEstado, long idRuta, long idPedido, String clienteRut, String clienteApellido, int pag, int regsperpage, long idLocal, String hIni, String hFin) {
        this.fechaDespacho  = fechaDespacho;
        this.idZona         = idZona;
        this.idChofer       = idChofer;
        this.idPatente      = idPatente;
        this.idEstado       = idEstado;
        this.idRuta         = idRuta;
        this.idPedido       = idPedido;
        this.clienteRut     = clienteRut;
        this.clienteApellido = clienteApellido;
        this.pag            = pag;
        this.regsperpage    = regsperpage;
        this.idLocal        = idLocal;
        this.hIni           = hIni;
        this.hFin           = hFin;
    }
    
    
    /**
     * @return Devuelve clienteApellido.
     */
    public String getClienteApellido() {
        return clienteApellido;
    }
    /**
     * @return Devuelve clienteRut.
     */
    public String getClienteRut() {
        return clienteRut;
    }
    /**
     * @return Devuelve fechaDespacho.
     */
    public String getFechaDespacho() {
        return fechaDespacho;
    }
    /**
     * @return Devuelve idChofer.
     */
    public long getIdChofer() {
        return idChofer;
    }
    /**
     * @return Devuelve idEstado.
     */
    public long getIdEstado() {
        return idEstado;
    }

    /**
     * @return Devuelve idPatente.
     */
    public long getIdPatente() {
        return idPatente;
    }
    /**
     * @return Devuelve idPedido.
     */
    public long getIdPedido() {
        return idPedido;
    }
    /**
     * @return Devuelve idRuta.
     */
    public long getIdRuta() {
        return idRuta;
    }
    /**
     * @return Devuelve idZona.
     */
    public long getIdZona() {
        return idZona;
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
     * @param clienteApellido El clienteApellido a establecer.
     */
    public void setClienteApellido(String clienteApellido) {
        this.clienteApellido = clienteApellido;
    }
    /**
     * @param clienteRut El clienteRut a establecer.
     */
    public void setClienteRut(String clienteRut) {
        this.clienteRut = clienteRut;
    }
    /**
     * @param fechaDespacho El fechaDespacho a establecer.
     */
    public void setFechaDespacho(String fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }
    /**
     * @param idChofer El idChofer a establecer.
     */
    public void setIdChofer(long idChofer) {
        this.idChofer = idChofer;
    }
    /**
     * @param idEstado El idEstado a establecer.
     */
    public void setIdEstado(long idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * @param idPatente El idPatente a establecer.
     */
    public void setIdPatente(long idPatente) {
        this.idPatente = idPatente;
    }
    /**
     * @param idPedido El idPedido a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }
    /**
     * @param idRuta El idRuta a establecer.
     */
    public void setIdRuta(long idRuta) {
        this.idRuta = idRuta;
    }
    /**
     * @param idZona El idZona a establecer.
     */
    public void setIdZona(long idZona) {
        this.idZona = idZona;
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
     * @return Devuelve idLocal.
     */
    public long getIdLocal() {
        return idLocal;
    }
    /**
     * @param idLocal El idLocal a establecer.
     */
    public void setIdLocal(long idLocal) {
        this.idLocal = idLocal;
    }
    /**
     * @return Devuelve hFin.
     */
    public String getHFin() {
        return hFin;
    }
    /**
     * @return Devuelve hIni.
     */
    public String getHIni() {
        return hIni;
    }
    /**
     * @param fin El hFin a establecer.
     */
    public void setHFin(String fin) {
        hFin = fin;
    }
    /**
     * @param ini El hIni a establecer.
     */
    public void setHIni(String ini) {
        hIni = ini;
    }
}
