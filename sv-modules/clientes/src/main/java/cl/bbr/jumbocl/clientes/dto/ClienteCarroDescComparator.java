package cl.bbr.jumbocl.clientes.dto;

import java.util.Comparator;

public class ClienteCarroDescComparator implements Comparator {
	
    public int compare(Object arg0, Object arg1) {
        CarroCompraDTO carro1 = (CarroCompraDTO) arg0;
        CarroCompraDTO carro2 = (CarroCompraDTO) arg1;
        return carro1.getTipo_producto().compareTo(carro2.getTipo_producto());
    }

}
