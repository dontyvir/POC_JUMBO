// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcesaReg2.java

package cl.bbr.promo.carga.helper;

import cl.bbr.irs.promolib.ctrl.IrsPromosDaoCtrl;
import cl.bbr.irs.promolib.entity.ProductoPromosEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionException;
import cl.bbr.promo.carga.dto.Reg2ProdxPromoDTO;
import cl.bbr.promo.carga.dto.Registro2DTO;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.carga.helper:
//            Feedback, Util

public class ProcesaReg2 extends Feedback
{

    public ProcesaReg2()
    {
    }

    public static List procesaReg2(Registro2DTO reg, int id_local)
    {
        String ean13 = new String();
        List excep = new ArrayList();
        List listado = reg.getProductos();
        IrsPromosDaoCtrl dao = new IrsPromosDaoCtrl();
        for(int i = 0; i < listado.size(); i++)
        {
            Reg2ProdxPromoDTO prod = (Reg2ProdxPromoDTO)listado.get(i);
            int prioridad = prod.getPrioridad();
            try
            {
                ean13 = String.valueOf(prod.getProducto()) + Util.getEan13(String.valueOf(prod.getProducto()));
                ProductoPromosEntity producto = dao.getPromocionProducto(ean13, id_local);
                if(producto != null)
                {
                    if(prioridad == 1)
                        producto.setPromo1(prod.getCodigo());
                    if(prioridad == 2)
                        producto.setPromo2(prod.getCodigo());
                    if(prioridad == 3)
                        producto.setPromo3(prod.getCodigo());
                    dao.updPromocionProducto(producto);
                } else
                {
                    producto = new ProductoPromosEntity();
                    producto.setId_local(id_local);
                    producto.setEan13(ean13);
                    producto.setPromo1(0);
                    producto.setPromo2(0);
                    producto.setPromo3(0);
                    if(prioridad == 1)
                        producto.setPromo1(prod.getCodigo());
                    if(prioridad == 2)
                        producto.setPromo2(prod.getCodigo());
                    if(prioridad == 3)
                        producto.setPromo3(prod.getCodigo());
                    dao.insPromocionProducto(producto);
                }
                excep.add(null);
            }
            catch(IrsPromocionException e2)
            {
                excep.add(excepProducto(prod.getCodigo(), prod.getProducto(), 2));
            }
        }

        return excep;
    }
}