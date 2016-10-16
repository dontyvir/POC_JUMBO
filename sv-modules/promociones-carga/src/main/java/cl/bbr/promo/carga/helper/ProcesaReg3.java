// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcesaReg3.java

package cl.bbr.promo.carga.helper;

import cl.bbr.irs.promolib.ctrl.IrsPromosDaoCtrl;
import cl.bbr.irs.promolib.exceptions.IrsPromocionException;
import cl.bbr.promo.carga.dto.Registro3DTO;
//import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.carga.helper:
//            Feedback

public class ProcesaReg3 extends Feedback
{

    public ProcesaReg3()
    {
    }

    public static List procesaReg3(Registro3DTO reg, int id_local)
    {
        List excep = new ArrayList();
        List listado = reg.getPromociones();
        IrsPromosDaoCtrl dao = new IrsPromosDaoCtrl();
        for(int i = 0; i < listado.size(); i++)
        {
            int codigo;
            try
            {
                codigo = Integer.parseInt(String.valueOf(listado.get(i)));
            }
            catch(Exception e)
            {
                codigo = 0;
            }
            System.out.println("codigo = " + codigo);
            try
            {
                cl.bbr.irs.promolib.entity.PromocionEntity promo = dao.getPromocion(codigo, id_local);
                if(promo != null)
                {
                    dao.delPromocion(promo);
                    excep.add(null);
                } else
                {
                    excep.add(excepProducto(codigo, 0L, 6));
                }
            }
            catch(IrsPromocionException e)
            {
                excep.add(excepProducto(codigo, 0L, 6));
            }
        }

        return excep;
    }
}