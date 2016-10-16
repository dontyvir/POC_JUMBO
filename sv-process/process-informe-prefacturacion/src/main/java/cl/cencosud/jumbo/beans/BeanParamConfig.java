package cl.cencosud.jumbo.beans;

import java.util.Map;

public class BeanParamConfig {

    private String nombreArchivo;
    private String tipo;
    private Map horarios;
    private String numReintentos;
    private String intervalo;
    private String puerto;
    private String server;
    private String from;
    private Map to;
    private Map cc;
    private Map cco;
    private String subject;
    private String mensaje;
    private String pathImagen1;
    private String pathImagen2;
    private String EliminarArchivosTemporales;

    public String getNombreArchivo() {

        return this.nombreArchivo;
    }

    public Map getTo() {

        return this.to;
    }

    public Map getCc() {

        return this.cc;
    }

    public Map getCco() {

        return this.cco;
    }

    public String getFrom() {

        return this.from;
    }

    public Map getHorarios() {

        return this.horarios;
    }

    public String getIntervalo() {

        return this.intervalo;
    }

    public String getMensaje() {

        return this.mensaje;
    }

    public String getNumReintentos() {

        return this.numReintentos;
    }

    public String getPuerto() {

        return this.puerto;
    }

    public String getServer() {

        return this.server;
    }

    public String getSubject() {

        return this.subject;
    }

    public String getTipo() {

        return this.tipo;
    }

    public String getPathImagen1() {

        return this.pathImagen1;
    }

    public String getPathImagen2() {

        return this.pathImagen2;
    }

    public void setNombreArchivo( String nombreArchivo ) {

        this.nombreArchivo = nombreArchivo;
    }

    public void setTo( Map to ) {

        this.to = to;
    }

    public void setCc( Map cc ) {

        this.cc = cc;
    }

    public void setCco( Map cco ) {

        this.cco = cco;
    }

    public void setFrom( String from ) {

        this.from = from;
    }

    public void setHorarios( Map horarios ) {

        this.horarios = horarios;
    }

    public void setIntervalo( String intervalo ) {

        this.intervalo = intervalo;
    }

    public void setMensaje( String mensaje ) {

        this.mensaje = mensaje;
    }

    public void setNumReintentos( String numReintentos ) {

        this.numReintentos = numReintentos;
    }

    public void setPuerto( String puerto ) {

        this.puerto = puerto;
    }

    public void setServer( String server ) {

        this.server = server;
    }

    public void setSubject( String subject ) {

        this.subject = subject;
    }

    public void setTipo( String tipo ) {

        this.tipo = tipo;
    }

    public void setPathImagen1( String pathImagen1 ) {

        this.pathImagen1 = pathImagen1;
    }

    public void setPathImagen2( String pathImagen2 ) {

        this.pathImagen2 = pathImagen2;
    }

    public String getEliminarArchivosTemporales() {

        return this.EliminarArchivosTemporales;
    }

    public void setEliminarArchivosTemporales( String eliminarArchivosTemporales ) {

        this.EliminarArchivosTemporales = eliminarArchivosTemporales;
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.beans.BeanParamConfig JD-Core Version: 0.6.0
 */