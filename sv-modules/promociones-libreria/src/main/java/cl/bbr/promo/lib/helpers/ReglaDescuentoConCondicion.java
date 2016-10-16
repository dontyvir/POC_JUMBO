// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReglaDescuentoConCondicion.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            Tcp, TipoPromocionSimple, FormaPago

public class ReglaDescuentoConCondicion extends Tcp
    implements TipoPromocionSimple
{

    public ReglaDescuentoConCondicion()
    {
        logger = new Logging(this);
    }

    public void initReglaPromocion(PromocionEntity promo, List productos)
    {
        regPromo = promo;
        this.productos = productos;
        listTcp = new ArrayList();
    }

    public ProrrateoPromocionProductoDTO calculaDcto(int fpago, int cuotas, List TCP, int canal, int local)
    {
        ProductoBenConDTO prod = new ProductoBenConDTO();
        ProrrateoPromocionProductoDTO prorrateoPorPromocion = new ProrrateoPromocionProductoDTO();
        List prorrateoProducto = new ArrayList();
        ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
        listTcp = TCP;
        prorrateoPorPromocion.setTipo("P");
        prorrateoPorPromocion.setCodigo(regPromo.getCodigo());
        prorrateoPorPromocion.setDescripcion(regPromo.getDescripcion());
        prorrateoPorPromocion.setDescuento(0L);
        long total_dcto = 0L;
        long min_monto = 0L;
        int min_cant;
        int tipo = min_cant = 0;
        double dcto;
        double beneficio = dcto = 0.0D;
        tipo = regPromo.getTipo();
        min_cant = regPromo.getMinCantidad();
        min_monto = regPromo.getMinMonto();
        int acum_cant = 0;
        long acum_monto = 0L;
        if(tipo == 3)
        {
            for(int i = 0; i < productos.size(); i++)
            {
                prod = (ProductoBenConDTO)productos.get(i);
                acum_cant += prod.getCant();
            }

        } else
        if(tipo == 4)
        {
            for(int i = 0; i < productos.size(); i++)
            {
                prod = (ProductoBenConDTO)productos.get(i);
                acum_monto += prod.getPrecio();
            }

        } else
        {
            return null;
        }
        if(tipo == 3 && acum_cant < min_cant || tipo == 4 && acum_monto < min_monto)
            return null;
        if(canal != regPromo.getCanal())
            return null;
        FormaPago fp = new FormaPago();
        int tcp;
        if(fp.obtieneBeneficio(regPromo, fpago, cuotas, listTcp))
        {
            beneficio = fp.getBeneficio();
            tcp = fp.getTcp();
        } else
        {
            return null;
        }
        total_dcto = 0L;
        for(int i = 0; i < productos.size(); i++)
        {
            prod = (ProductoBenConDTO)productos.get(i);
            pp = new ProrrateoProductoDTO();
            pp.setCodigo(prod.getCodigo());
            pp.setDepto(prod.getDepto());
            pp.setCantPeso(prod.getFlag());
            pp.setCantPeso(prod.getCant());
            pp.setPrecio(prod.getPrecio());
            tipo = regPromo.getTipo();
            int cantTcp = getCantidadTCP(listTcp, tcp);
            //logger.debug("tcp=" + tcp + " | cantTcp=" + cantTcp);
            if(cantTcp <= 0 && tcp != 0)
                break;
            if(beneficio == 0.0D)
                return null;
            dcto = ((double)prod.getPrecio() * beneficio) / 100D;
            dcto = (dcto + 0.5D) / 1.0D;
            pp.setProrrateo((long)dcto);
            prorrateoProducto.add(pp);
            total_dcto += (long)dcto;
            listTcp = quemaTCP(listTcp, tcp);
        }

        if(total_dcto > 0L)
        {
            prorrateoPorPromocion.setDescuento(total_dcto);
            prorrateoPorPromocion.setListadoProductos(prorrateoProducto);
            return prorrateoPorPromocion;
        } else
        {
            return null;
        }
    }

    public List getListTcp()
    {
        return listTcp;
    }

    Logging logger;
    private PromocionEntity regPromo;
    private List productos;
    private List listTcp;
}