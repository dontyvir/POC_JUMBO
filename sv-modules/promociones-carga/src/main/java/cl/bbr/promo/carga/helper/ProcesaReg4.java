// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcesaReg4.java

package cl.bbr.promo.carga.helper;

import cl.bbr.irs.promolib.ctrl.IrsPromosDaoCtrl;
import cl.bbr.irs.promolib.entity.ProductoPromosEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionException;
import cl.bbr.promo.carga.dto.Reg4ProdxEliminarDTO;
import cl.bbr.promo.carga.dto.Registro4DTO;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.carga.helper:
//            Feedback, Util

public class ProcesaReg4 extends Feedback
{

    public ProcesaReg4()
    {
    }

    public static List procesaReg4(Registro4DTO reg, int id_local)
    {
        String ean13 = new String();
        List excep = new ArrayList();
        List listado = reg.getProductos();
        IrsPromosDaoCtrl dao = new IrsPromosDaoCtrl();
        for(int i = 0; i < listado.size(); i++)
        {
            Reg4ProdxEliminarDTO prod = new Reg4ProdxEliminarDTO();
            try
            {
                prod = (Reg4ProdxEliminarDTO)listado.get(i);
                int prioridad = prod.getPrioridad();
                ean13 = String.valueOf(prod.getProducto()) + Util.getEan13(String.valueOf(prod.getProducto()));
                ProductoPromosEntity producto = dao.getPromocionProducto(ean13, id_local);
                if(producto != null)
                {
                    if(prioridad == 1)
                        producto.setPromo1(0);
                    if(prioridad == 2)
                        producto.setPromo2(0);
                    if(prioridad == 3)
                        producto.setPromo3(0);
                    if(producto.getPromo1() == 0 && producto.getPromo2() == 0 && producto.getPromo3() == 0)
                        dao.delPromocionProducto(producto);
                    else
                        dao.updPromocionProducto(producto);
                    excep.add(null);
                } else
                {
                    excep.add(excepProducto(0, prod.getProducto(), 3));
                }
            }
            catch(IrsPromocionException e)
            {
                excep.add(excepProducto(0, prod.getProducto(), 3));
            }
        }

        return excep;
    }
}