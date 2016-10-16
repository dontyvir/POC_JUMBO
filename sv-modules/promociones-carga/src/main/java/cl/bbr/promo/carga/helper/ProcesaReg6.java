// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcesaReg6.java

package cl.bbr.promo.carga.helper;

import cl.bbr.irs.promolib.ctrl.IrsPromosDaoCtrl;
import cl.bbr.irs.promolib.entity.MatrizSeccionEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionDAOException;
import cl.bbr.irs.promolib.exceptions.IrsPromocionException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.promo.carga.dto.Reg6SeccionxPromo;
import cl.bbr.promo.carga.dto.Registro6DTO;
import java.sql.SQLException;
//import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.carga.helper:
//            Feedback

public class ProcesaReg6 extends Feedback
{

    public ProcesaReg6()
    {
    }

    public static List procesaReg6(Registro6DTO reg, int id_local)
    {
        List excep = new ArrayList();
        List listado = reg.getSeccion();
        IrsPromosDaoCtrl dao = new IrsPromosDaoCtrl();
        JdbcTransaccion trx = new JdbcTransaccion();
        for(int i = 0; i < listado.size(); i++)
        {
            Reg6SeccionxPromo seccion = (Reg6SeccionxPromo)listado.get(i);
            int sec = seccion.getSeccion();
            if(sec == 0)
                break;
            try
            {
                trx.begin();
                dao.setTrx(trx);
            }
            catch(DAOException e)
            {
                e.printStackTrace();
                excep.add(excepSeccion(seccion.getSeccion(), 0, 1));
                break;
            }
            catch(IrsPromocionDAOException e)
            {
                e.printStackTrace();
                excep.add(excepSeccion(seccion.getSeccion(), 0, 1));
                break;
            } catch (SQLException e) {
            	e.printStackTrace();
                excep.add(excepSeccion(seccion.getSeccion(), 0, 1));
                break;
			}
            try
            {
                System.out.println("seccion=" + sec);
                MatrizSeccionEntity matriz = dao.getMatrizSeccion(sec, id_local);
                if(matriz != null)
                {
                    matriz.setSeccion(seccion.getSeccion());
                    matriz.setLunes(seccion.isLunes());
                    matriz.setMartes(seccion.isMartes());
                    matriz.setMiercoles(seccion.isMiercoles());
                    matriz.setJueves(seccion.isJueves());
                    matriz.setViernes(seccion.isViernes());
                    matriz.setSabado(seccion.isSabado());
                    matriz.setDomingo(seccion.isDomingo());
                    matriz.setEsp1(seccion.isEsp1());
                    matriz.setEsp2(seccion.isEsp2());
                    matriz.setEsp3(seccion.isEsp3());
                    matriz.setEsp4(seccion.isEsp4());
                    matriz.setEsp5(seccion.isEsp5());
                    matriz.setEsp6(seccion.isEsp6());
                    matriz.setEsp7(seccion.isEsp7());
                    matriz.setEsp8(seccion.isEsp8());
                    dao.updMatrizSeccion(matriz);
                } else
                {
                    matriz = new MatrizSeccionEntity();
                    matriz.setLocal(id_local);
                    matriz.setSeccion(seccion.getSeccion());
                    matriz.setSeccion(seccion.getSeccion());
                    matriz.setLunes(seccion.isLunes());
                    matriz.setMartes(seccion.isMartes());
                    matriz.setMiercoles(seccion.isMiercoles());
                    matriz.setJueves(seccion.isJueves());
                    matriz.setViernes(seccion.isViernes());
                    matriz.setSabado(seccion.isSabado());
                    matriz.setDomingo(seccion.isDomingo());
                    matriz.setEsp1(seccion.isEsp1());
                    matriz.setEsp2(seccion.isEsp2());
                    matriz.setEsp3(seccion.isEsp3());
                    matriz.setEsp4(seccion.isEsp4());
                    matriz.setEsp5(seccion.isEsp5());
                    matriz.setEsp6(seccion.isEsp6());
                    matriz.setEsp7(seccion.isEsp7());
                    matriz.setEsp8(seccion.isEsp8());
                    dao.insMatrizSeccion(matriz);
                }
                excep.add(null);
                trx.end();
                continue;
            }
            catch(IrsPromocionException e)
            {
                excep.add(excepSeccion(seccion.getSeccion(), 0, 2));
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
                excep.add(excepSeccion(seccion.getSeccion(), 0, 2));
            }
            break;
        }

        return excep;
    }
}