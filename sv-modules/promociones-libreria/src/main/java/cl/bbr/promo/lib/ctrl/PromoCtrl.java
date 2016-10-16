// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:16:58
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PromoCtrl.java

package cl.bbr.promo.lib.ctrl;

import cl.bbr.irs.promolib.exceptions.IrsPromocionDAOException;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.ProductoDTO;
import cl.bbr.promo.lib.helpers.Libreria;
import java.util.List;

public class PromoCtrl
{
	//[20121114avc
    private Long rutColaborador;

    public PromoCtrl(int local, int canal, Long rut_colaborador)
    {
        logger = new Logging(this);
        this.local = local;
        this.canal = canal;
        try {
        lib = new Libreria();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
    }
        this.rutColaborador = rut_colaborador; //new Long(rut_colaborador);
    }
	//[20121114avc

    public void insertaProducto(ProductoDTO p)
    {
        lib.inserta(p);
    }

    public void insertaListadoProductos(List l)
    {
        try
        {
            for(int i = 0; i < l.size(); i++)
            {
                ProductoDTO p = (ProductoDTO)l.get(i);
                lib.inserta(p);
            }

        }
        catch(Exception e)
        {
            logger.debug("Error al insertar listado de productos a la libreria");
            e.printStackTrace();
        }
    }

//[20121114avc
    public long calculaDescuentos(String fpago, int cuotas, List tcp) throws IrsPromocionDAOException
    {
        return calculaDescuentosNew(fpago, cuotas, tcp, null, null);
    }
//]20121114avc
    
    /**
     * Se agregan parametros para aplicar cupon de descuento
     * @param fpago
     * @param cuotas
     * @param tcp
     * @param cddto
     * @param cuponProds
     * @return
     */
    public long calculaDescuentosNew(String fpago, int cuotas, List tcp, CuponDsctoDTO cddto, List cuponProds)
    {
        return lib.calcula(fpago, cuotas, tcp, getCanal(), getLocal(), rutColaborador, cddto, cuponProds);
    }

    public List getProrrateoProducto()
    {
        return lib.getProrrateoProducto();
    }

    public List getProrrateoSeccion()
    {
        return lib.getProrrateoSeccion();
    }

    public int getLocal()
    {
        return local;
    }

    public int getCanal()
    {
        return canal;
    }

    public List getListadoTcp()
    {
        return lib.getListadoTcp();
    }

    Logging logger;
    private Libreria lib;
    private int local;
    private int canal;
}