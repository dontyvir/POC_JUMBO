/*
 * Creado el 09-may-2012
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.productos.dto;

import java.util.Comparator;

import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;



/**
 * @author rriffog
 *
 *  Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class CarroDescComparator implements Comparator  {

    public int compare(Object arg0, Object arg1) {
        CarroCompraDTO carro1 = (CarroCompraDTO) arg0;
        CarroCompraDTO carro2 = (CarroCompraDTO) arg1;
        return carro1.getTipo_producto().compareTo(carro2.getTipo_producto());
    }
    
}
