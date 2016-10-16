// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcesaReg5.java

package cl.bbr.promo.carga.helper;

import cl.bbr.irs.promolib.ctrl.IrsPromosDaoCtrl;
import cl.bbr.irs.promolib.entity.PromoSeccionEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionDAOException;
import cl.bbr.irs.promolib.exceptions.IrsPromocionException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.promo.carga.dto.Reg5PromoSeccionActivasDTO;
import cl.bbr.promo.carga.dto.Registro5DTO;
import java.sql.SQLException;
//import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.carga.helper:
//            Feedback

public class ProcesaReg5 extends Feedback
{

    public ProcesaReg5()
    {
    }

    public static List procesaReg5(Registro5DTO reg, int id_local)
    {
        List excep = new ArrayList();
        List listado = reg.getPromociones();
        IrsPromosDaoCtrl dao = new IrsPromosDaoCtrl();
        JdbcTransaccion trx = new JdbcTransaccion();
        for(int i = 0; i < listado.size(); i++)
        {
            Reg5PromoSeccionActivasDTO promo = (Reg5PromoSeccionActivasDTO)listado.get(i);
            int tipo = promo.getTipo();
            if(tipo == 0)
                break;
            try
            {
                trx.begin();
                dao.setTrx(trx);
            }
            catch(DAOException e1)
            {
                e1.printStackTrace();
                excep.add(excepSeccion(promo.getTipo(), promo.getCodigo(), 1));
                break;
            }
            catch(IrsPromocionDAOException e)
            {
                e.printStackTrace();
                excep.add(excepSeccion(promo.getTipo(), promo.getCodigo(), 1));
                break;
            } catch (SQLException e2) {
            	 e2.printStackTrace();
                 excep.add(excepSeccion(promo.getTipo(), promo.getCodigo(), 1));
                 break;
			}
            try
            {
                System.out.println("tipo=" + tipo);
                PromoSeccionEntity p_seccion = dao.getPromoSeccion(tipo, id_local, false);
                if(p_seccion != null)
                {
                    p_seccion.setCodigo(promo.getCodigo());
                    dao.updPromoSeccion(p_seccion);
                } else
                {
                    p_seccion = new PromoSeccionEntity();
                    p_seccion.setLocal(id_local);
                    p_seccion.setTipo(promo.getTipo());
                    p_seccion.setCodigo(promo.getCodigo());
                    dao.insPromoSeccion(p_seccion);
                }
                excep.add(null);
                trx.end();
                continue;
            }
            catch(IrsPromocionException e)
            {
                excep.add(excepSeccion(promo.getTipo(), promo.getCodigo(), 1));
                try
                {
                    trx.rollback();
                    continue;
                }
                catch(DAOException e1)
                {
                    e1.printStackTrace();
                }
            }
            catch(DAOException e)
            {
                e.printStackTrace();
                excep.add(excepSeccion(promo.getTipo(), promo.getCodigo(), 1));
            }
            break;
        }

        return excep;
    }
}