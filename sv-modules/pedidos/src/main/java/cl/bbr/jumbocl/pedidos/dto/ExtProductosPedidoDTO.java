package cl.bbr.jumbocl.pedidos.dto;

import java.util.ArrayList;
import java.util.List;

public class ExtProductosPedidoDTO extends ProductosPedidoDTO {

    /**
     *  
     */
    private static final long serialVersionUID = 1L;

    private Double impuesto;

    private List promoDetallePedido = new ArrayList();

    public Double getImpuesto() {
        return impuesto;
    }

    public List getPromoDetallePedido() {
        return promoDetallePedido;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    public void setPromoDetallePedido(List promoDetallePedido) {
        this.promoDetallePedido = promoDetallePedido;
    }
}
