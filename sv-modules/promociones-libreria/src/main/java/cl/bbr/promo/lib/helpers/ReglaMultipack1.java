// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReglaMultipack1.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            Tcp, TipoPromocionSimple, FormaPago, Util

public class ReglaMultipack1 extends Tcp
    implements TipoPromocionSimple
{

    public ReglaMultipack1()
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
        ProductoBenConDTO prod1 = new ProductoBenConDTO();
        ProductoBenConDTO prod2 = new ProductoBenConDTO();
        ProrrateoPromocionProductoDTO prorrateoPorPromocion = new ProrrateoPromocionProductoDTO();
        List prorrateoProducto = new ArrayList();
        ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
        prorrateoPorPromocion.setTipo("P");
        prorrateoPorPromocion.setCodigo(regPromo.getCodigo());
        prorrateoPorPromocion.setDescripcion(regPromo.getDescripcion());
        prorrateoPorPromocion.setDescuento(0L);
        listTcp = TCP;
        if(canal != regPromo.getCanal())
            return null;
        FormaPago fp = new FormaPago();
        long beneficio;
        int tcp;
        if(fp.obtieneBeneficio(regPromo, fpago, cuotas, listTcp))
        {
            beneficio = (long)fp.getBeneficio();
            tcp = fp.getTcp();
        } else
        {
            return null;
        }
        long dcto;
        long acum_monto;
        long total_dcto = dcto = acum_monto = 0L;
        int acum_cant;
        int puntero;
        int min_cant = puntero = acum_cant = 0;
        min_cant = regPromo.getMinCantidad();
        if(productos == null)
            return null;
        for(int i = puntero; i < productos.size(); i++)
        {
            int cantTcp = getCantidadTCP(listTcp, tcp);
            logger.debug("tcp=" + tcp + " | cantTcp=" + cantTcp);
            if(cantTcp <= 0 && tcp != 0)
                break;
            prod1 = (ProductoBenConDTO)productos.get(i);
            acum_monto += prod1.getPrecio();
            if(++acum_cant != min_cant)
                continue;
            
            if(regPromo.getPague() != 0 &&  regPromo.getLleve() > regPromo.getPague())
            	dcto = acum_monto - (prod1.getPrecio() *  regPromo.getPague());//beneficio;
            else
            	dcto = acum_monto - beneficio;
            
            if(dcto <= 0L)
                break;
            if(dcto > 0L)
                listTcp = quemaTCP(listTcp, tcp);
            long acum_dcto_p = 0L;
            long diff = 0L;
            for(int j = puntero; j <= i; j++)
            {
                pp = new ProrrateoProductoDTO();
                prod2 = (ProductoBenConDTO)productos.get(j);
                pp.setCodigo(prod2.getCodigo());
                pp.setDepto(prod2.getDepto());
                pp.setCantPeso(prod2.getCant());
                pp.setPrecio(prod2.getPrecio());
                long dcto_p = Util.calculaProrrateo(prod2.getPrecio(), acum_monto, dcto);
                acum_dcto_p += dcto_p;
                diff = dcto - acum_dcto_p;
                if(j == i && diff > 0L)
                    dcto_p += diff;
                pp.setProrrateo(dcto_p);
                pp.setCantPeso(prod2.getFlag());
                prorrateoProducto.add(pp);
            }

            total_dcto += dcto;
            acum_cant = 0;
            acum_monto = 0L;
            puntero = i+1;
        }

        if(total_dcto == 0L)
        {
            return null;
        } else
        {
            prorrateoPorPromocion.setDescuento(total_dcto);
            prorrateoPorPromocion.setListadoProductos(prorrateoProducto);
            return prorrateoPorPromocion;
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