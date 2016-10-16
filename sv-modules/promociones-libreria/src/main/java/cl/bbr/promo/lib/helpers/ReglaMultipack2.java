// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReglaMultipack2.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            Tcp, TipoPromocionDoble, FormaPago, Util

public class ReglaMultipack2 extends Tcp
    implements TipoPromocionDoble
{

    public ReglaMultipack2()
    {
        logger = new Logging(this);
    }

    public void initReglaPromocion(PromocionEntity promoBenef, PromocionEntity promoCond, List prodBenef, List prodCond)
    {
        regPromoBenef = promoBenef;
        regPromoCond = promoCond;
        this.prodBenef = prodBenef;
        this.prodCond = prodCond;
        listTcp = new ArrayList();
    }

    public ProrrateoPromocionProductoDTO calculaDcto(int fpago, int cuotas, List TCP, int canal, int local)
    {
        ProductoBenConDTO prod = new ProductoBenConDTO();
        ProrrateoPromocionProductoDTO prorrateoPorPromocion = new ProrrateoPromocionProductoDTO();
        List prorrateoProducto = new ArrayList();
        ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
        prorrateoPorPromocion.setTipo("P");
        prorrateoPorPromocion.setCodigo(regPromoBenef.getCodigo());
        prorrateoPorPromocion.setDescripcion(regPromoBenef.getDescripcion());
        prorrateoPorPromocion.setDescuento(0L);
        listTcp = TCP;
        if(canal != regPromoBenef.getCanal())
            return null;
        FormaPago fp = new FormaPago();
        long beneficio;
        int tcp;
        if(fp.obtieneBeneficio(regPromoBenef, fpago, cuotas, listTcp))
        {
            beneficio = (long)fp.getBeneficio();
            tcp = fp.getTcp();
        } else
        {
            return null;
        }
        int acum_cant_cond;
        int acum_cant_benef = acum_cant_cond = 0;
        int min_cant_benef = regPromoBenef.getMinCantidad();
        int min_cant_cond = regPromoCond.getMinCantidad();
        boolean gc1;
        boolean gb = gc1 = false;
        boolean gc1gb = true;
        if(regPromoBenef.getFlagProrrateo() == 1)
            gb = true;
        if(regPromoCond.getFlagProrrateo() == 1)
            gc1 = true;
        if(regPromoBenef.getFlagProrrateo() == 0 || regPromoCond.getFlagProrrateo() == 0)
            gc1gb = false;
        else
            gb = gc1 = false;
        int TOPE_BENEFICIO = 0;
        int TOPE_CONDICION = 0;
        long tot_acum = 0L;
        long tot_acum_p = 0L;
        long monto_tot_desc = 0L;
        long monto_desc = 0L;
        if(prodBenef == null || prodCond == null)
            return null;
        for(int i = TOPE_BENEFICIO; i < prodBenef.size(); i++)
        {
            int cantTcp = getCantidadTCP(listTcp, tcp);
            if(cantTcp <= 0 && tcp != 0)
                break;
            acum_cant_benef++;
            prod = (ProductoBenConDTO)prodBenef.get(i);
            if(gb || gc1gb)
                tot_acum_p += prod.getPrecio();
            tot_acum += prod.getPrecio();
            if(acum_cant_benef == min_cant_benef)
            {
                for(int j = TOPE_CONDICION; j < prodCond.size(); j++)
                {
                    acum_cant_cond++;
                    prod = (ProductoBenConDTO)prodCond.get(j);
                    if(gc1 || gc1gb)
                        tot_acum_p += prod.getPrecio();
                    tot_acum += prod.getPrecio();
                    if(acum_cant_cond != min_cant_cond)
                        continue;
                    monto_desc = tot_acum - beneficio;
                    logger.debug("DCTO=" + monto_desc);
                    if(monto_desc > 0L)
                    {
                        if(monto_desc > 0L)
                            listTcp = quemaTCP(listTcp, tcp);
                        monto_tot_desc += monto_desc;
                        long acum_dcto_p = 0L;
                        long diff = 0L;
                        if(gb || gc1gb)
                        {
                            for(int z = TOPE_BENEFICIO; z <= i; z++)
                            {
                                prod = (ProductoBenConDTO)prodBenef.get(z);
                                pp = new ProrrateoProductoDTO();
                                pp.setCantPeso(prod.getCant());
                                pp.setCodigo(prod.getCodigo());
                                pp.setDepto(prod.getDepto());
                                pp.setFlagVenta(prod.getFlag());
                                pp.setPrecio(prod.getPrecio());
                                long dcto_p = Util.calculaProrrateo(prod.getPrecio(), tot_acum_p, monto_desc);
                                acum_dcto_p += dcto_p;
                                diff = monto_desc - acum_dcto_p;
                                if(z == i && diff > 0L && gb)
                                    dcto_p += diff;
                                pp.setProrrateo(dcto_p);
                                prorrateoProducto.add(pp);
                            }

                        }
                        if(gc1 || gc1gb)
                        {
                            for(int w = TOPE_CONDICION; w <= j; w++)
                            {
                                prod = (ProductoBenConDTO)prodCond.get(w);
                                pp = new ProrrateoProductoDTO();
                                pp.setCantPeso(prod.getCant());
                                pp.setCodigo(prod.getCodigo());
                                pp.setDepto(prod.getDepto());
                                pp.setFlagVenta(prod.getFlag());
                                pp.setPrecio(prod.getPrecio());
                                long dcto_p = Util.calculaProrrateo(prod.getPrecio(), tot_acum_p, monto_desc);
                                acum_dcto_p += dcto_p;
                                diff = monto_desc - acum_dcto_p;
                                if(w == j && diff > 0L && (gc1 || gc1gb))
                                    dcto_p += diff;
                                pp.setProrrateo(dcto_p);
                                prorrateoProducto.add(pp);
                            }

                        }
                        tot_acum = 0L;
                        monto_desc = 0L;
                        tot_acum_p = 0L;
                        TOPE_BENEFICIO = i + 1;
                        TOPE_CONDICION = j + 1;
                        acum_cant_cond = 0;
                        acum_cant_benef = 0;
                    }
                    break;
                }

            }
        }

        if(monto_tot_desc == 0L)
        {
            return null;
        } else
        {
            prorrateoPorPromocion.setDescuento(monto_tot_desc);
            prorrateoPorPromocion.setListadoProductos(prorrateoProducto);
            return prorrateoPorPromocion;
        }
    }

    public List getListTcp()
    {
        return listTcp;
    }

    Logging logger;
    private PromocionEntity regPromoBenef;
    private PromocionEntity regPromoCond;
    private List prodBenef;
    private List prodCond;
    private List listTcp;
}