package cl.bbr.jumbocl.pedidos.dto;

import java.util.ArrayList;
import java.util.List;


public class ExtPedidoDTO extends PedidoDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private List cuponPedido = new ArrayList(); //<CuponPedidoDTO>
	private LogPedidoDTO logPedido;
	
	private List productos = new ArrayList(); //<ExtProductosPedidoDTO>
    private List promoDetallePedido = new ArrayList(); //<PromoDetallePedidoDTO>
    private List tcpPedido = new ArrayList(); //<TcpPedidoDTO>
	public List getCuponPedido() { //<CuponPedidoDTO>
		return cuponPedido;
	}
 
    public LogPedidoDTO getLogPedido() {
		return logPedido;
	}
	public List getProductos() { //<ExtProductosPedidoDTO>
		return productos;
	}
	public List getPromoDetallePedido() { //<PromoDetallePedidoDTO>
		return promoDetallePedido;
	}
	public List getTcpPedido() { //<TcpPedidoDTO>
		return tcpPedido;
	}
	public void setCuponPedido(List cuponPedido) { //<CuponPedidoDTO>
		this.cuponPedido = cuponPedido;
	}
	public void setLogPedido(LogPedidoDTO logPedido) {
		this.logPedido = logPedido;
	}
	public void setProductos(List productos) { //<ExtProductosPedidoDTO>
		this.productos = productos;
	}
	public void setPromoDetallePedido(List promoDetallePedido) { //<PromoDetallePedidoDTO>
		this.promoDetallePedido = promoDetallePedido;
	}
	public void setTcpPedido(List tcpPedido) { //<TcpPedidoDTO>
		this.tcpPedido = tcpPedido;
	}
	
}
