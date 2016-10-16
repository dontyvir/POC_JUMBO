package cl.bbr.jumbocl.pedidos.dto;

import java.util.Comparator;

/**
 * 
 * Comparador de rondas para el monitor.
 * 
 * @author BBR e-commerce & retail
 *
 */
public class RondasComparator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		MonitorRondasDTO ronda1 = (MonitorRondasDTO) arg0;
		MonitorRondasDTO ronda2 = (MonitorRondasDTO) arg1;
		
		return ronda1.getOrden() - ronda2.getOrden();
	}

}
