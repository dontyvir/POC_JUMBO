package cl.cencosud.jumbo.beans;

import java.util.Map;

public class BeanParamConfig {

    private String nombreArchivo;
    private String tipo;
    private Map horarios;
    private String UltimaJornada;
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
    private int porcentaje;
    private String pathImagen1;
    private String pathImagen2;
    private String obtenerMails;

    public BeanParamConfig() {
    }

    public String getObtenerMails() {
        return obtenerMails;
    }

    public void setObtenerMails(String obtenerMails) {
        this.obtenerMails = obtenerMails;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public Map getTo() {
        return to;
    }

    public Map getCc() {
        return cc;
    }

    public Map getCco() {
        return cco;
    }

    public String getFrom() {
        return from;
    }

    public Map getHorarios() {
        return horarios;
    }

    public String getIntervalo() {
        return intervalo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getNumReintentos() {
        return numReintentos;
    }

    public String getPuerto() {
        return puerto;
    }

    public String getServer() {
        return server;
    }

    public String getSubject() {
        return subject;
    }

    public String getTipo() {
        return tipo;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public String getPathImagen1() {
        return pathImagen1;
    }

    public String getPathImagen2() {
        return pathImagen2;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void setTo(Map to) {
        this.to = to;
    }

    public void setCc(Map cc) {
        this.cc = cc;
    }

    public void setCco(Map cco) {
        this.cco = cco;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setHorarios(Map horarios) {
        this.horarios = horarios;
    }

    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setNumReintentos(String numReintentos) {
        this.numReintentos = numReintentos;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setSubject(String subject)  {
        this.subject = subject;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public void setPathImagen1(String pathImagen1) {
        this.pathImagen1 = pathImagen1;
    }

    public void setPathImagen2(String pathImagen2) {
        this.pathImagen2 = pathImagen2;
    }

    public String getUltimaJornada() {
        return UltimaJornada;
    }

    public void setUltimaJornada(String ultimaJornada) {
        UltimaJornada = ultimaJornada;
    }
}
