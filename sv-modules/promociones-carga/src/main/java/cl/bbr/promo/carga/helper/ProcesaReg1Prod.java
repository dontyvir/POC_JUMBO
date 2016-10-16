// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcesaReg1Prod.java

package cl.bbr.promo.carga.helper;

import cl.bbr.irs.promolib.ctrl.IrsPromosDaoCtrl;
import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionException;
import cl.bbr.log.Logging;
import cl.bbr.promo.carga.dto.FeedbackProductoDTO;
import cl.bbr.promo.carga.dto.Registro1DTO;
//import java.io.PrintStream;

// Referenced classes of package cl.bbr.promo.carga.helper:
//            Feedback

public class ProcesaReg1Prod extends Feedback
{

    public ProcesaReg1Prod()
    {
        logger = new Logging(this);
    }

    public static FeedbackProductoDTO procesaReg1(Registro1DTO reg, int id_local)
    {
        PromocionEntity promo = new PromocionEntity();
        promo.setLocal(id_local);
        promo.setCodigo(reg.getCodigo());
        promo.setTipo(reg.getTipoPromo());
        promo.setVersion(reg.getVersion());
        promo.setFechaInicio(reg.getInicio());
        promo.setFechaTermino(reg.getTermino());
        promo.setDescripcion(reg.getDescriptor());
        promo.setMinCantidad(reg.getMinCantidad());
        promo.setMinMonto(reg.getMinMonto());
        promo.setTramo1Monto(reg.getTramo1Monto());
        promo.setTramo1Dcto(reg.getTramo1Dcto() / 10D);
        promo.setTramo2Monto(reg.getTramo2Monto());
        promo.setTramo2Dcto(reg.getTramo2Dcto() / 10D);
        promo.setTramo3Monto(reg.getTramo3Monto());
        promo.setTramo3Dcto(reg.getTramo3Dcto() / 10D);
        promo.setTramo4Monto(reg.getTramo4Monto());
        promo.setTramo4Dcto(reg.getTramo4Dcto() / 10D);
        promo.setTramo5Monto(reg.getTramo5Monto());
        promo.setTramo5Dcto(reg.getTramo5Dcto() / 10D);
        double dcto1 = reg.getBenef1Monto();
        double dcto2 = reg.getBenef2Monto();
        double dcto3 = reg.getBenef3Monto();
        if(reg.getTipoPromo() == 1 || reg.getTipoPromo() == 3 || reg.getTipoPromo() == 4 || reg.getTipoPromo() == 102)
        {
            dcto1 = reg.getBenef1Monto() / 10D;
            dcto2 = reg.getBenef2Monto() / 10D;
            dcto3 = reg.getBenef3Monto() / 10D;
        }
        promo.setBenef1FormaPago(reg.getBenef1Fpago());
        promo.setBenef1NroCuotas(reg.getBenef1Cuotas());
        promo.setBenef1TCP(reg.getBenef1TCP());
        promo.setBenef1Monto(dcto1);
        promo.setBenef2FormaPago(reg.getBenef2Fpago());
        promo.setBenef2NroCuotas(reg.getBenef2Cuotas());
        promo.setBenef2TCP(reg.getBenef2TCP());
        promo.setBenef2Monto(dcto2);
        promo.setBenef3FormaPago(reg.getBenef3Fpago());
        promo.setBenef3NroCuotas(reg.getBenef3Cuotas());
        promo.setBenef3TCP(reg.getBenef3TCP());
        promo.setBenef3Monto(dcto3);
        promo.setCondicion1(reg.getCondicion1());
        promo.setCondicion2(reg.getCondicion2());
        promo.setCondicion3(reg.getCondicion3());
        promo.setFlagProrrateo(reg.getFlagProrrateo());
        promo.setFlagRecuperable(reg.getFlagRecuperable());
        promo.setCanal(reg.getCanal());
        IrsPromosDaoCtrl dao = new IrsPromosDaoCtrl();
        try
        {
            System.out.println("PROMO = " + promo.getCodigo());
            int cont = dao.updPromocion(promo);
            if(cont <= 0)
                dao.insPromocion(promo);
        }
        catch(IrsPromocionException e)
        {
            e.printStackTrace();
            return excepProducto(promo.getCodigo(), 0L, 5);
        }
        return null;
    }

    Logging logger;
}