// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReglaDescuentoSeccion.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            Tcp, TipoPromocionSeccion, FormaPago

public class ReglaDescuentoSeccion extends Tcp
    implements TipoPromocionSeccion
{

    public ReglaDescuentoSeccion()
    {
        logger = new Logging(this);
    }

    public void initReglaPromocion(PromocionEntity promo, SeccionDTO seccion)
    {
        regPromo = promo;
        this.seccion = seccion;
        listTcp = new ArrayList();
        tcp = 0;
    }

    public ProrrateoPromocionSeccionDTO calculaDcto(int fpago, int cuotas, List TCP, int canal, int local)
    {
        List listado = new ArrayList();
        ProrrateoPromocionSeccionDTO prorrateoPorSeccion = new ProrrateoPromocionSeccionDTO();
        prorrateoPorSeccion.setTipo("S");
        prorrateoPorSeccion.setCodigo(regPromo.getCodigo());
        prorrateoPorSeccion.setDescripcion(regPromo.getDescripcion());
        listTcp = TCP;
        if(canal != regPromo.getCanal())
            return null;
        FormaPago fp = new FormaPago();
        double beneficio;
        int tcp;
        if(fp.obtieneBeneficio(regPromo, fpago, cuotas, listTcp))
        {
            beneficio = fp.getBeneficio();
            tcp = fp.getTcp();
        } else
        {
            return null;
        }
        int tipo = regPromo.getTipo();
        int cantTcp = getCantidadTCP(listTcp, tcp);
        if(cantTcp <= 0 && tcp != 0)
            return null;
        if(tipo >= 900 && tipo <= 914)
        {
            ProrrateoSeccionDTO sec = new ProrrateoSeccionDTO();
            if(beneficio == 0.0D)
                return null;
            double dcto = ((double)seccion.getMonto() * beneficio) / 100D;
            dcto = (dcto + 0.5D) / 1.0D;
            if(dcto <= 0.0D)
                return null;
            logger.debug("seccion=" + seccion.getSeccion() + " | monto=" + seccion.getMonto() + " | dcto=" + (long)dcto);
            sec.setDepto(seccion.getSeccion());
            sec.setPrecio(seccion.getMonto());
            sec.setProrrateo((long)dcto);
            listado.add(sec);
            prorrateoPorSeccion.setListadoSeccion(listado);
            prorrateoPorSeccion.setDescuento((long)dcto);
        }
        this.tcp = tcp;
        return prorrateoPorSeccion;
    }

    public int getTcp()
    {
        return tcp;
    }

    Logging logger;
    private PromocionEntity regPromo;
    private SeccionDTO seccion;
    private List listTcp;
    private int tcp;
}