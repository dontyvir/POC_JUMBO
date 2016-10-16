/*
 * Created on Jun 16, 2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.interfaces.ventaslocales;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CompraCDP extends CompraHistorica {
	
	public static int EN_PROCESO_DE_CARGA = 1;
	public static int CARGADO_EN_VTHJM = 2;
	public static int CARGADO_A_CLIENTE = 3;
	public static int NUEVA_ACTUALIZACION = 4;

	private TreeMap clientes;
	private HashMap productos;
	
	
	public CompraCDP() {
		super();
		clientes = new TreeMap();
		productos = new HashMap();
	}

	public CompraCDP(int idCompra) {
		super(idCompra);
		clientes = new TreeMap();
		productos = new HashMap();
	}

	public Vector getProductos() {
		Vector result = new Vector();
		if (productos != null)
			result.addAll(productos.values());
		return result;
	}
	
	public void setProductos(HashMap productos) {
		this.productos = productos;
	}
	public TreeMap getClientes() {
		return clientes;
	}
	public void setClientes(TreeMap clientes) {
		this.clientes = clientes;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if ((clientes != null) && (clientes.values().size() > 0)) {
			Iterator iter = clientes.values().iterator();
			ClienteCDP cliente = (ClienteCDP) iter.next();
			sb.append("RUT: " + cliente.getRut() + " / Medio de Pago " + cliente.getMedioDePago().getCodigo() + " / " + cliente.getMedioDePago().getImporte() + "\n")
				.append("Fecha: " + getFecha().toString() + " / Caja " + getCaja() + " / Ticket " + getTicket() + " / local " + getCodigoLocal() + "\n");
			if ((productos != null) && (productos.values().size() > 0)) {
				Iterator iter2 = productos.values().iterator();
				while (iter2.hasNext()) {
					sb.append("\t" + ((ProductoCompraHistorica)iter2.next()).getCodigoEAN() + " / " + ((ProductoCompraHistorica)iter2.next()).getCantidad() + "\n");
				}
			} else {
				sb.append("\tSin Productos\n");
			}
			sb.append("\n");
		} else {
			sb.append("No hay clientes...");
		}
		return sb.toString();
	}
}
