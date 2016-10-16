package cl.cencosud.procesos.chaordic.dto;

import java.util.List;

/**
 * @author
 *
 */

public class ConfigDTO {

    private String ip = null;
    private String puerto;
    private List tablasHijas = null;
    private String meses = null;
    private String fechaInicio = null;
    private String usuario = null;
    private String pass = null;
    private String baseDatos = null;
    private String directorioRespaldo = null;
    private String remitente = null;
    private String destinatario = null;
    private String copiaMensaje = null;
    private String asunto = null;
    private String driver = null;
    private String asuntoResumen = null;
    private boolean fechaCalculada ;
    private String  rutArchivo=null;
    
	/**
	 * @return Devuelve fechaCalculada.
	 */
	public boolean isFechaCalculada() {
		return fechaCalculada;
	}
	/**
	 * @param fechaCalculada El fechaCalculada a establecer.
	 */
	public void setFechaCalculada(boolean fechaCalculada) {
		this.fechaCalculada = fechaCalculada;
	}

    /**
     * @return Devuelve copiaMensaje.
     */
    public String getCopiaMensaje() {
        return copiaMensaje;
    }

    /**
     * @param copiaMensaje El copiaMensaje a establecer.
     */
    public void setCopiaMensaje(String copiaMensaje) {
        this.copiaMensaje = copiaMensaje;
    }

    /**
     * @return Devuelve destinatario.
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * @param destinatario El destinatario a establecer.
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * @return Devuelve remitente.
     */
    public String getRemitente() {
        return remitente;
    }

    /**
     * @param remitente El remitente a establecer.
     */
    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    /**
     * @return Devuelve directorioRespaldo.
     */
    public String getDirectorioRespaldo() {
        return directorioRespaldo;
    }

    /**
     * @param directorioRespaldo El directorioRespaldo a establecer.
     */
    public void setDirectorioRespaldo(String directorioRespaldo) {
        this.directorioRespaldo = directorioRespaldo;
    }

    /**
     * @return Devuelve ip.
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip El ip a establecer.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param llavePadre El llavePadre a establecer.
     */
 
    /**
     * @return Devuelve meses.
     */
    public String getMeses() {
        return meses;
    }

    /**
     * @return Devuelve baseDatos.
     */
    public String getBaseDatos() {
        return baseDatos;
    }

    /**
     * @param baseDatos El baseDatos a establecer.
     */
    public void setBaseDatos(String baseDatos) {
        this.baseDatos = baseDatos;
    }

    /**
     * @return Devuelve pass.
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass El pass a establecer.
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return Devuelve usuario.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario El usuario a establecer.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @param meses El meses a establecer.
     */
    public void setMeses(String meses) {
        this.meses = meses;
    }

    /**
     * @return Devuelve puerto.
     */
    public String getPuerto() {
        return puerto;
    }

    /**
     * @param puerto El puerto a establecer.
     */
    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

   

    /**
     * @return Devuelve tablasHijas.
     */
    public List getTablasHijas() {
        return tablasHijas;
    }

    /**
     * @param tablasHijas El tablasHijas a establecer.
     */
    public void setTablasHijas(List tablasHijas) {
        this.tablasHijas = tablasHijas;
    }

    /**
     * @return Devuelve fechaInicio.
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio El fechaInicio a establecer.
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return Devuelve asunto.
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * @param asunto El asunto a establecer.
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /* (sin Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("asunto: ");
        sb.append(this.asunto);
        sb.append(",baseDatos: ");
        sb.append(this.baseDatos);
        sb.append(",copiaMensaje: ");
        sb.append(this.copiaMensaje);
        sb.append(",destinatario: ");
        sb.append(this.destinatario);
        sb.append(",directorioRespaldo: ");
        sb.append(this.directorioRespaldo);
        sb.append(",fechaInicio: ");
        sb.append(this.fechaInicio);
        sb.append(",ip: ");
        sb.append(this.ip);
        sb.append(",meses: ");
        sb.append(this.meses);
        sb.append(",pass: ");
        sb.append(this.pass);
        sb.append(",remitente: ");
        sb.append(this.remitente);
        sb.append(",usuario: ");
        sb.append(this.usuario);

        return sb.toString();
    }

    /**
     * @return Devuelve driver.
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @param driver El driver a establecer.
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }
	/**
	 * @return Devuelve asuntoResumen.
	 */
	public String getAsuntoResumen() {
		return asuntoResumen;
	}
	/**
	 * @param asuntoResumen El asuntoResumen a establecer.
	 */
	public void setAsuntoResumen(String asuntoResumen) {
		this.asuntoResumen = asuntoResumen;
	}
	public String getRutArchivo() {
		return rutArchivo;
	}
	public void setRutArchivo(String rutArchivo) {
		this.rutArchivo = rutArchivo;
	}
	
	
}
