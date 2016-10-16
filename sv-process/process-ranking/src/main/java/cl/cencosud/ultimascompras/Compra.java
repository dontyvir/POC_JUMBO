/*
 * Created on 20-may-2008
 *
 */
package cl.cencosud.ultimascompras;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author jdroguett
 * 
 * Compras de cada cliente
 */
public class Compra {

    private Hashtable detalle; //<codigoBarra, Cantidad>

    /**
     * @param nuevo
     *            False: indica si son compras antiguas desde la base de datos
     *            (ie, un rut que ya existe en tabla fo_compras_locales). True:
     *            indica si son compras nuevas desde los archivos SAP (ie, un
     *            rut que no existe en tabla fo_compras_locales).
     */
    public Compra() {
        this.detalle = new Hashtable();
    }

    public void add(long codigoBarra, float cantidad, Date fecha) {
        /*
         * Por ahora dejo la ultima cantidad comprada
         */
        Long codigo = new Long(codigoBarra);
        DetalleCompra detalleCompra = (DetalleCompra) detalle.get(codigo);
        if (detalleCompra == null) {
            detalleCompra = new DetalleCompra();
        }
        detalleCompra.setCodigoBarra(codigoBarra);
        detalleCompra.setFecha(fecha);
        detalleCompra.setCantidad(cantidad);
        detalle.put(codigo, detalleCompra);
    }

    public Enumeration getDetalle() {
        return detalle.elements();
    }
}
