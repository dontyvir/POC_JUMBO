package cl.bbr.jumbocl.casos.dto;

import java.io.Serializable;

public class CasosDocBolDTO implements Serializable {
    
	private long	idDocBol;
	private long 	idCaso;
	private String	usuario;
	private String	fecha;
	
	private String  comentario;
	
	private String  cooler1;
	private String  cooler2;
	private String  cooler3;
	private String  cooler4;
	private String  cooler5;
	private String  cooler6;
	
	private String  bin1;
	private String  bin2;
	private String  bin3;
	private String  bin4;
	private String  bin5;
	private String  bin6;
	
	public CasosDocBolDTO() {	    
	    this.idDocBol	= 0;
	    this.idCaso 	= 0;
	    this.usuario 	= "";
	    this.fecha 		= "";		
	    this.comentario	= "";		
	    this.cooler1 	= "";
	    this.cooler2 	= "";
	    this.cooler3 	= "";
	    this.cooler4 	= "";
	    this.cooler5 	= "";
	    this.cooler6 	= "";		
	    this.bin1 		= "";
	    this.bin2 		= "";
	    this.bin3 		= "";
	    this.bin4 		= "";
	    this.bin5		= "";
	    this.bin6 		= "";
	    
	}	
	
	public CasosDocBolDTO(long idCaso, String usuario, String comentario, String cooler1, String cooler2, String cooler3, String cooler4, String cooler5, String cooler6, String bin1, String bin2, String bin3, String bin4, String bin5, String bin6) {
	    this.idCaso = idCaso;
	    this.usuario = usuario;
	    
	    this.comentario = comentario;
		
	    this.cooler1 = cooler1;
	    this.cooler2 = cooler2;
	    this.cooler3 = cooler3;
	    this.cooler4 = cooler4;
	    this.cooler5 = cooler5;
	    this.cooler6 = cooler6;
		
	    this.bin1 = bin1;
	    this.bin2 = bin2;
	    this.bin3 = bin3;
	    this.bin4 = bin4;
	    this.bin5 = bin5;
	    this.bin6 = bin6;
	}

    /**
     * @return Devuelve bin1.
     */
    public String getBin1() {
        return bin1;
    }
    /**
     * @return Devuelve bin2.
     */
    public String getBin2() {
        return bin2;
    }
    /**
     * @return Devuelve bin3.
     */
    public String getBin3() {
        return bin3;
    }
    /**
     * @return Devuelve bin4.
     */
    public String getBin4() {
        return bin4;
    }
    /**
     * @return Devuelve bin5.
     */
    public String getBin5() {
        return bin5;
    }
    /**
     * @return Devuelve bin6.
     */
    public String getBin6() {
        return bin6;
    }
    /**
     * @return Devuelve comentario.
     */
    public String getComentario() {
        return comentario;
    }
    /**
     * @return Devuelve cooler1.
     */
    public String getCooler1() {
        return cooler1;
    }
    /**
     * @return Devuelve cooler2.
     */
    public String getCooler2() {
        return cooler2;
    }
    /**
     * @return Devuelve cooler3.
     */
    public String getCooler3() {
        return cooler3;
    }
    /**
     * @return Devuelve cooler4.
     */
    public String getCooler4() {
        return cooler4;
    }
    /**
     * @return Devuelve cooler5.
     */
    public String getCooler5() {
        return cooler5;
    }
    /**
     * @return Devuelve cooler6.
     */
    public String getCooler6() {
        return cooler6;
    }
    /**
     * @return Devuelve fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @return Devuelve idCaso.
     */
    public long getIdCaso() {
        return idCaso;
    }
    /**
     * @return Devuelve idDocBol.
     */
    public long getIdDocBol() {
        return idDocBol;
    }
    /**
     * @return Devuelve usuario.
     */
    public String getUsuario() {
        return usuario;
    }
    /**
     * @param bin1 El bin1 a establecer.
     */
    public void setBin1(String bin1) {
        this.bin1 = bin1;
    }
    /**
     * @param bin2 El bin2 a establecer.
     */
    public void setBin2(String bin2) {
        this.bin2 = bin2;
    }
    /**
     * @param bin3 El bin3 a establecer.
     */
    public void setBin3(String bin3) {
        this.bin3 = bin3;
    }
    /**
     * @param bin4 El bin4 a establecer.
     */
    public void setBin4(String bin4) {
        this.bin4 = bin4;
    }
    /**
     * @param bin5 El bin5 a establecer.
     */
    public void setBin5(String bin5) {
        this.bin5 = bin5;
    }
    /**
     * @param bin6 El bin6 a establecer.
     */
    public void setBin6(String bin6) {
        this.bin6 = bin6;
    }
    /**
     * @param comentario El comentario a establecer.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    /**
     * @param cooler1 El cooler1 a establecer.
     */
    public void setCooler1(String cooler1) {
        this.cooler1 = cooler1;
    }
    /**
     * @param cooler2 El cooler2 a establecer.
     */
    public void setCooler2(String cooler2) {
        this.cooler2 = cooler2;
    }
    /**
     * @param cooler3 El cooler3 a establecer.
     */
    public void setCooler3(String cooler3) {
        this.cooler3 = cooler3;
    }
    /**
     * @param cooler4 El cooler4 a establecer.
     */
    public void setCooler4(String cooler4) {
        this.cooler4 = cooler4;
    }
    /**
     * @param cooler5 El cooler5 a establecer.
     */
    public void setCooler5(String cooler5) {
        this.cooler5 = cooler5;
    }
    /**
     * @param cooler6 El cooler6 a establecer.
     */
    public void setCooler6(String cooler6) {
        this.cooler6 = cooler6;
    }
    /**
     * @param fecha El fecha a establecer.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @param idCaso El idCaso a establecer.
     */
    public void setIdCaso(long idCaso) {
        this.idCaso = idCaso;
    }
    /**
     * @param idDocBol El idDocBol a establecer.
     */
    public void setIdDocBol(long idDocBol) {
        this.idDocBol = idDocBol;
    }
    /**
     * @param usuario El usuario a establecer.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
