// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReglaDescuentoSinCondicion.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            Tcp, TipoPromocionSimple, FormaPago

public class ReglaDescuentoSinCondicion extends Tcp
    implements TipoPromocionSimple
{

    public ReglaDescuentoSinCondicion()
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
        ProrrateoPromocionProductoDTO prorrateoPorPromocion = new ProrrateoPromocionProductoDTO();
        List prorrateoProducto = new ArrayList();
        ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
        listTcp = TCP;
        prorrateoPorPromocion.setTipo("P");
        prorrateoPorPromocion.setCodigo(regPromo.getCodigo());
        prorrateoPorPromocion.setDescripcion(regPromo.getDescripcion());
        prorrateoPorPromocion.setDescuento(0L);
        if(canal != regPromo.getCanal())
            return null;
        FormaPago fp = new FormaPago();
        boolean flag_benef = fp.obtieneBeneficio(regPromo, fpago, cuotas, listTcp);
        //logger.debug("flag_benef=" + flag_benef);
        double beneficio;
        int tcp;
        if(flag_benef)
        {
            beneficio = fp.getBeneficio();
            tcp = fp.getTcp();
        } else
        {
            return null;
        }
        long total_dcto = 0L;
        for(int i = 0; i < productos.size(); i++)
        {
            ProductoBenConDTO prod = (ProductoBenConDTO)productos.get(i);
            pp = new ProrrateoProductoDTO();
            pp.setCodigo(prod.getCodigo());
            pp.setDepto(prod.getDepto());
            pp.setCantPeso(prod.getFlag());
            pp.setCantPeso(prod.getCant());
            pp.setPrecio(prod.getPrecio());
            int tipo = regPromo.getTipo();
            int cantTcp = getCantidadTCP(listTcp, tcp);
            //logger.debug("tcp=" + tcp + " | cantTcp=" + cantTcp);
            if(cantTcp <= 0 && tcp != 0)
                break;
            double dcto = 0.0D;
            if(tipo == 1)
            {
                if(beneficio == 0.0D)
                    return null;
                dcto = ((double)prod.getPrecio() * beneficio) / 100D;
                dcto = (dcto + 0.5D) / 1.0D;
                pp.setProrrateo((long)dcto);
                prorrateoProducto.add(pp);
                total_dcto += (long)dcto;
            }
            if(tipo == 2)
            {
                dcto = prod.getPrecio() - (long)beneficio;
                if(dcto > 0.0D)
                {
                    pp.setProrrateo((long)dcto);
                    prorrateoProducto.add(pp);
                    total_dcto += (long)dcto;
                }
            }
            if(dcto > 0.0D)
                listTcp = quemaTCP(listTcp, tcp);
        }

        if(total_dcto <= 0L)
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