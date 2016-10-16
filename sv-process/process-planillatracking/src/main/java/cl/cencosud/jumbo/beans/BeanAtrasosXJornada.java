/*
 * Creado el 26-01-2006
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
package cl.cencosud.jumbo.beans;

/**
 * @author rbelmar
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
public class BeanAtrasosXJornada {

    private String jornada;
    private double cantOP = 0D;
    private double atrasoTotal = 0D;
    private double atrasoMaximo = 0D;
    private double atrasoPromedio = 0D;
    
    
    /**
     * @return Devuelve atrasoMaximo en Minutos.
     */
    public double getAtrasoMaximo() {
        return atrasoMaximo;
    }
    /**
     * @return Devuelve atrasoPromedio en Minutos.
     */
    public double getAtrasoPromedio() {
        return atrasoTotal/cantOP;
    }
    
    /**
     * @return Devuelve atrasoMaximo en formato HH:MM.
     */
    public String getAtrasoMaximoHHMM() {
        long horas = ((long)atrasoMaximo)/60;
        long minutos = ((long)atrasoMaximo) - (horas*60);
        
        return (Long.toString(horas) + ":" + Long.toString(minutos));
    }
    /**
     * @return Devuelve atrasoPromedio.
     */
    public String getAtrasoPromedioHHMM() {
        long AtPromMin = (((long)atrasoTotal)/((long)cantOP));
        long horas = AtPromMin /60;
        long minutos = AtPromMin - (horas*60);
        
        return (Long.toString(horas) + ":" + Long.toString(minutos));
    }

    
    /**
     * @return Devuelve atrasoTotal.
     */
    public double getAtrasoTotal() {
        return atrasoTotal;
    }
    /**
     * @return Devuelve cantOP.
     */
    public double getCantOP() {
        return cantOP;
    }
    /**
     * @return Devuelve jornada.
     */
    public String getJornada() {
        return jornada;
    }
    /**
     * @param atrasoMaximo El atrasoMaximo a establecer.
     */
    public void setAtrasoMaximo(double atrasoMaximo) {
        this.atrasoMaximo = atrasoMaximo;
    }
    /**
     * @param atrasoTotal El atrasoTotal a establecer.
     */
    public void setAtrasoTotal(double atrasoTotal) {
        this.atrasoTotal = atrasoTotal;
    }
    /**
     * @param cantOP El cantOP a establecer.
     */
    public void setCantOP(double cantOP) {
        this.cantOP = cantOP;
    }
    /**
     * @param jornada El jornada a establecer.
     */
    public void setJornada(String jornada) {
        this.jornada = jornada;
    }
}
