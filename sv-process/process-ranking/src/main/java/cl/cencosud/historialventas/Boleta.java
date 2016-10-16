/*
 * Created on 07-may-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.cencosud.historialventas;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author jdroguett
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Boleta {
    private int id;

    private String local;

    private int caja;

    private int ticket;

    private Date fecha;

    private int rutCliente;

    private int medioPago;

    private int medioPagoSAP;

    private int montoTotal;

    private Hashtable lineas; //<codigo_barra, Linea>

    public Boleta() {
        lineas = new Hashtable();
        montoTotal = 0;
    }

    /**
     * @return Returns the caja.
     */
    public int getCaja() {
        return caja;
    }

    /**
     * @param caja
     *            The caja to set.
     */
    public void setCaja(int caja) {
        this.caja = caja;
    }

    /**
     * @return Returns the fecha.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha
     *            The fecha to set.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return Returns the local.
     */
    public String getLocal() {
        return local;
    }

    /**
     * @param local
     *            The local to set.
     */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
     * @return Returns the medioPago.
     */
    public int getMedioPago() {
        return medioPago;
    }

    /**
     * @param medioPago
     *            The medioPago to set.
     */
    public void setMedioPago(int medioPago) {
        this.medioPago = medioPago;
    }

    /**
     * @return Returns the medioPagoSAP.
     */
    public int getMedioPagoSAP() {
        return medioPagoSAP;
    }

    /**
     * @param medioPagoSAP
     *            The medioPagoSAP to set.
     */
    public void setMedioPagoSAP(int medioPagoSAP) {
        this.medioPagoSAP = medioPagoSAP;
    }

    /**
     * @return Returns the montoTotal.
     */
    public int getMontoTotal() {
        return montoTotal;
    }

    /**
     * @param montoTotal
     *            The montoTotal to set.
     */
    public void addMontoTotal(int montoTotal) {
        this.montoTotal += montoTotal;
    }

    /**
     * @return Returns the rutCliente.
     */
    public int getRutCliente() {
        return rutCliente;
    }

    /**
     * @param rutCliente
     *            The rutCliente to set.
     */
    public void setRutCliente(int rutCliente) {
        this.rutCliente = rutCliente;
    }

    /**
     * @return Returns the ticket.
     */
    public int getTicket() {
        return ticket;
    }

    /**
     * @param ticket
     *            The ticket to set.
     */
    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public void addLinea(Linea linea) {
        Long codigoBarra = new Long(linea.getCodigoBarra());
        Linea lineaOld = (Linea) lineas.get(codigoBarra);
        if (lineaOld != null)
            lineaOld.setCantidad(lineaOld.getCantidad() + linea.getCantidad());
        else
            lineas.put(codigoBarra, linea);

    }

    public Enumeration getLineas() {
        return this.lineas.elements();
    }
    
    public int getLineasSize(){
        return this.lineas.size();
    }

    public String toString() {
        String detalle = "";
        Enumeration lineas = getLineas(); 
        while (lineas.hasMoreElements()) {
            Linea linea = (Linea) lineas.nextElement();
            detalle += linea + "\n";
        }

        return "local: " + local + "\n" + "caja: " + caja + "\n" + "ticket: " + ticket + "\n" + "fecha: " + fecha
                + "\n" + "rut: " + rutCliente + "\n" + "medioPago: " + medioPago + "\n" + "medioPagoSAP: "
                + medioPagoSAP + "\n" + "montoTotal: " + montoTotal + "\n" + detalle;

    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
}
