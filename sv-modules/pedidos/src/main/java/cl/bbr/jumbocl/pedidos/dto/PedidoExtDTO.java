package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.jumbocl.common.dto.ObjetoDTO;

public class PedidoExtDTO implements Serializable {
    
    private long idPedido;    
    private long nroGuiaCaso;
    private String fcHoraLlegadaDomicilio;
    private String fcHoraSalidaDomicilio;
    private String cumplimiento;
    private ObjetoDTO motivoNoCumplimiento = new ObjetoDTO();
    private ObjetoDTO responsableNoCumplimiento = new ObjetoDTO();
    private int reprogramada;
    private long idRuta;
    private String mail;
    private String binsDescripcion;
    private String tipoJumboVA;
    private int vecesEnRuta;
    private long idJornadaOriginal;
    private String conDevolucion;
    private List documentos = new ArrayList();
    
    /**
     * @return Devuelve fcHoraLlegadaDomicilio.
     */
    public String getFcHoraLlegadaDomicilio() {
        return fcHoraLlegadaDomicilio;
    }
    /**
     * @return Devuelve fcHoraSalidaDomicilio.
     */
    public String getFcHoraSalidaDomicilio() {
        return fcHoraSalidaDomicilio;
    }

    /**
     * @return Devuelve nroGuiaCaso.
     */
    public long getNroGuiaCaso() {
        return nroGuiaCaso;
    }

    /**
     * @param fcHoraLlegadaDomicilio El fcHoraLlegadaDomicilio a establecer.
     */
    public void setFcHoraLlegadaDomicilio(String fcHoraLlegadaDomicilio) {
        this.fcHoraLlegadaDomicilio = fcHoraLlegadaDomicilio;
    }
    /**
     * @param fcHoraSalidaDomicilio El fcHoraSalidaDomicilio a establecer.
     */
    public void setFcHoraSalidaDomicilio(String fcHoraSalidaDomicilio) {
        this.fcHoraSalidaDomicilio = fcHoraSalidaDomicilio;
    }

    /**
     * @param nroGuiaCaso El nroGuiaCaso a establecer.
     */
    public void setNroGuiaCaso(long nroGuiaCaso) {
        this.nroGuiaCaso = nroGuiaCaso;
    }
    /**
     * @return Devuelve binsDescripcion.
     */
    public String getBinsDescripcion() {
        return binsDescripcion;
    }
    /**
     * @param binsDescripcion El binsDescripcion a establecer.
     */
    public void setBinsDescripcion(String binsDescripcion) {
        this.binsDescripcion = binsDescripcion;
    }
    /**
     * @return Devuelve tipoJumboVA.
     */
    public String getTipoJumboVA() {
        return tipoJumboVA;
    }
    /**
     * @param tipoJumboVA El tipoJumboVA a establecer.
     */
    public void setTipoJumboVA(String tipoJumboVA) {
        this.tipoJumboVA = tipoJumboVA;
    }
    /**
     * @return Devuelve mail.
     */
    public String getMail() {
        return mail;
    }
    /**
     * @param mail El mail a establecer.
     */
    public void setMail(String mail) {
        this.mail = mail;
    }
    /**
     * @return Devuelve idRuta.
     */
    public long getIdRuta() {
        return idRuta;
    }
    /**
     * @param idRuta El idRuta a establecer.
     */
    public void setIdRuta(long idRuta) {
        this.idRuta = idRuta;
    }
    /**
     * @return Devuelve documentos.
     */
    public List getDocumentos() {
        return documentos;
    }
    /**
     * @param documentos El documentos a establecer.
     */
    public void setDocumentos(List documentos) {
        this.documentos = documentos;
    }
    /**
     * @return Devuelve conDevolucion.
     */
    public String getConDevolucion() {
        return conDevolucion;
    }
    /**
     * @return Devuelve cumplimiento.
     */
    public String getCumplimiento() {
        return cumplimiento;
    }
    /**
     * @return Devuelve idJornadaOriginal.
     */
    public long getIdJornadaOriginal() {
        return idJornadaOriginal;
    }
    /**
     * @return Devuelve motivoNoCumplimiento.
     */
    public ObjetoDTO getMotivoNoCumplimiento() {
        return motivoNoCumplimiento;
    }
    /**
     * @return Devuelve reprogramada.
     */
    public int getReprogramada() {
        return reprogramada;
    }
    /**
     * @return Devuelve responsableNoCumplimiento.
     */
    public ObjetoDTO getResponsableNoCumplimiento() {
        return responsableNoCumplimiento;
    }
    /**
     * @return Devuelve vecesEnRuta.
     */
    public int getVecesEnRuta() {
        return vecesEnRuta;
    }
    /**
     * @param conDevolucion El conDevolucion a establecer.
     */
    public void setConDevolucion(String conDevolucion) {
        this.conDevolucion = conDevolucion;
    }
    /**
     * @param cumplimiento El cumplimiento a establecer.
     */
    public void setCumplimiento(String cumplimiento) {
        this.cumplimiento = cumplimiento;
    }
    /**
     * @param idJornadaOriginal El idJornadaOriginal a establecer.
     */
    public void setIdJornadaOriginal(long idJornadaOriginal) {
        this.idJornadaOriginal = idJornadaOriginal;
    }
    /**
     * @param motivoNoCumplimiento El motivoNoCumplimiento a establecer.
     */
    public void setMotivoNoCumplimiento(ObjetoDTO motivoNoCumplimiento) {
        this.motivoNoCumplimiento = motivoNoCumplimiento;
    }
    /**
     * @param reprogramada El reprogramada a establecer.
     */
    public void setReprogramada(int reprogramada) {
        this.reprogramada = reprogramada;
    }
    /**
     * @param responsableNoCumplimiento El responsableNoCumplimiento a establecer.
     */
    public void setResponsableNoCumplimiento(ObjetoDTO responsableNoCumplimiento) {
        this.responsableNoCumplimiento = responsableNoCumplimiento;
    }
    /**
     * @param vecesEnRuta El vecesEnRuta a establecer.
     */
    public void setVecesEnRuta(int vecesEnRuta) {
        this.vecesEnRuta = vecesEnRuta;
    }
    /**
     * @return Devuelve idPedido.
     */
    public long getIdPedido() {
        return idPedido;
    }
    /**
     * @param idPedido El idPedido a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }
}
