// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProcesaArchivo.java

package cl.bbr.promo.carga.helper;

import cl.bbr.irs.promolib.ctrl.IrsPromosDaoCtrl;
import cl.bbr.irs.promolib.entity.LocalBoEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionException;
import cl.bbr.log.Logging;
import cl.bbr.promo.carga.dto.*;
import java.io.*;
import java.util.*;

// Referenced classes of package cl.bbr.promo.carga.helper:
//            ProcesaReg1Prod, ProcesaReg2, ProcesaReg3, ProcesaReg4, 
//            ProcesaReg1Sec, ProcesaReg5, ProcesaReg6, Registro0Wrap, 
//            Registro1Wrap, Registro2Wrap, Registro3Wrap, Registro4Wrap, 
//            Registro5Wrap, Registro6Wrap, Registro7Wrap

public class ProcesaArchivo
{

    public ProcesaArchivo(File archivo, String idLocalSAP)
    {
        logger = new Logging(this);
        this.archivo = archivo;
        this.idLocalSAP = idLocalSAP;
    }

    public ProcesaArchivo(String archivo, String idLocalSAP)
    {
        logger = new Logging(this);
        try
        {
            this.archivo = new File(archivo);
            this.idLocalSAP = idLocalSAP;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            this.archivo = null;
            idLocalBO = 0;
        }
    }

    public FeedbackDTO procesa()
    {
        List excep = new ArrayList();
        promoOk = promoError = skuOk = skuError = 0;
        FeedbackDTO feedback = new FeedbackDTO();
        finicio = new Date();
        logger.debug("INICIA CARGA | FECHA=17/JUN/2008 | VERSION=1.5");
        IrsPromosDaoCtrl irs = new IrsPromosDaoCtrl();
        LocalBoEntity locales;
        try
        {
            locales = irs.getLocalBySap(idLocalSAP);
        }
        catch(IrsPromocionException e1)
        {
            return generaFeedbackError(2);
        }
        setIdLocalSAP(idLocalSAP);
        setIdLocalBO((int)locales.getId_local());
        setIdLocalPOS((int)locales.getId_local_bop());
        logger.debug("local sap = " + idLocalSAP);
        logger.debug("local bo = " + locales.getId_local());
        logger.debug("local pos = " + locales.getId_local_bop());
        try
        {
            if(!archivo.exists())
                return generaFeedbackError(10);
            if(!archivo.isFile())
                return generaFeedbackError(10);
            if(!archivo.canRead())
                return generaFeedbackError(10);
        }
        catch(Exception e)
        {
            return generaFeedbackError(10);
        }
        if(!checkSizeFile())
            return generaFeedbackError(17);
        List carga = wrapArchivoCarga(archivo);
        logger.debug("WRAPPER=" + carga.size());
        int id_local = getIdLocal(carga, getIdLocalPOS());
        if(id_local == 0)
            return generaFeedbackError(16);
        logger.debug("id_local = " + id_local);
        int tipo_promo = getTipoPromo(carga);
        logger.debug("tipo_promo = " + tipo_promo);
        if(tipo_promo == 1)
            excep = procesaProducto(carga, getIdLocalBO());
        else
        if(tipo_promo == 2)
            excep = procesaSeccion(carga, getIdLocalBO());
        feedback = generaFeedback(tipo_promo, excep);
        logger.debug("EXCEP=" + excep.size());
        return feedback;
    }

    private FeedbackDTO generaFeedback(int tipo_promo, List excep)
    {
        FeedbackDTO fb = new FeedbackDTO();
        ftermino = new Date();
        logger.debug("TERMINO CARGA = " + ftermino);
        String id_tarea;
        try
        {
            id_tarea = archivo.getName().substring(2, 8);
        }
        catch(Exception e)
        {
            logger.error("id_tarea=" + archivo.getName(), e);
            id_tarea = "0";
        }
        logger.debug("(fb) id_tarea=" + id_tarea);
        fb.setTipo(tipo_promo);
        fb.setLocal(getIdLocalSAP());
        try
        {
            fb.setIdtarea(Integer.parseInt(id_tarea));
        }
        catch(Exception e)
        {
            logger.error("id_tarea=" + id_tarea, e);
            fb.setIdtarea(0);
        }
        if(excep.size() == 0)
        {
            fb.setEstado(0);
            fb.setCodret(0);
        } else
        {
            fb.setEstado(1);
            fb.setCodret(0);
        }
        fb.setProdProc(skuOk);
        fb.setProdNoProc(skuError);
        fb.setPromoProc(promoOk);
        fb.setPromoNoProc(promoError);
        fb.setFinicio(finicio);
        fb.setFtermino(ftermino);
        fb.setRegExcep(excep.size());
        fb.setExcep(excep);
        return fb;
    }

    private FeedbackDTO generaFeedbackError(int error)
    {
        FeedbackDTO fb = new FeedbackDTO();
        ftermino = new Date();
        String id_tarea;
        try
        {
            id_tarea = archivo.getName().substring(2, 8);
        }
        catch(Exception e)
        {
            logger.error("id_tarea=" + archivo.getName(), e);
            id_tarea = "0";
        }
        logger.debug("id_tarea=" + id_tarea);
        logger.debug("FEEDBACK ERROR");
        logger.debug("local sap = " + getIdLocalSAP());
        logger.debug("error = " + error);
        fb.setLocal(getIdLocalSAP());
        try
        {
            fb.setIdtarea(Integer.parseInt(id_tarea));
        }
        catch(Exception e)
        {
            logger.error("id_tarea=" + id_tarea, e);
            fb.setIdtarea(0);
        }
        fb.setEstado(2);
        fb.setCodret(error);
        fb.setFinicio(finicio);
        fb.setFtermino(ftermino);
        return fb;
    }

    private int getIdLocal(List carga, int local)
    {
        List locales = new ArrayList();
        Registro0DTO reg0 = (Registro0DTO)carga.get(0);
        locales = reg0.getLocales();
        for(int i = 0; i < locales.size(); i++)
        {
            int cod = Integer.parseInt(String.valueOf(locales.get(i)));
            if(local == cod)
                return local;
        }

        return 0;
    }

    private int getTipoPromo(List carga)
    {
        Registro0DTO reg0 = (Registro0DTO)carga.get(0);
        return reg0.getTipoPromo();
    }

    private List procesaProducto(List carga, int id_local)
    {
        Registro1DTO reg1 = new Registro1DTO();
        Registro2DTO reg2 = new Registro2DTO();
        Registro3DTO reg3 = new Registro3DTO();
        Registro4DTO reg4 = new Registro4DTO();
        FeedbackProductoDTO fb = null;
        List excep = new ArrayList();
        List res = new ArrayList();
        logger.debug("carga=" + carga.size());
        for(int i = 0; i < carga.size(); i++)
        {
            Object reg = carga.get(i);
            fb = null;
            if(reg.getClass().getName().indexOf(reg1.getClass().getName()) != -1)
            {
                fb = ProcesaReg1Prod.procesaReg1((Registro1DTO)reg, id_local);
                if(fb != null)
                {
                    excep.add(fb);
                    addPromoError();
                } else
                {
                    addPromoOk();
                }
            } else
            if(reg.getClass().getName().indexOf(reg2.getClass().getName()) != -1)
            {
                res = ProcesaReg2.procesaReg2((Registro2DTO)reg, id_local);
                if(res.size() > 0)
                {
                    for(int w = 0; w < res.size(); w++)
                    {
                        fb = (FeedbackProductoDTO)res.get(w);
                        if(fb != null)
                        {
                            excep.add(fb);
                            addSkuError();
                        } else
                        {
                            addSkuOk();
                        }
                    }

                }
            } else
            if(reg.getClass().getName().indexOf(reg3.getClass().getName()) != -1)
            {
                res = ProcesaReg3.procesaReg3((Registro3DTO)reg, id_local);
                if(res.size() > 0)
                {
                    for(int w = 0; w < res.size(); w++)
                    {
                        fb = (FeedbackProductoDTO)res.get(w);
                        if(fb != null)
                        {
                            excep.add(fb);
                            addPromoError();
                        } else
                        {
                            addPromoOk();
                        }
                    }

                }
            } else
            if(reg.getClass().getName().indexOf(reg4.getClass().getName()) != -1)
            {
                res = ProcesaReg4.procesaReg4((Registro4DTO)reg, id_local);
                if(res.size() > 0)
                {
                    for(int w = 0; w < res.size(); w++)
                    {
                        fb = (FeedbackProductoDTO)res.get(w);
                        if(fb != null)
                        {
                            excep.add(fb);
                            addSkuError();
                        } else
                        {
                            addSkuOk();
                        }
                    }

                }
            }
        }

        return excep;
    }

    private List procesaSeccion(List carga, int id_local)
    {
        Registro1DTO reg1 = new Registro1DTO();
        Registro5DTO reg5 = new Registro5DTO();
        Registro6DTO reg6 = new Registro6DTO();
        FeedbackSeccionDTO fb = null;
        List res = new ArrayList();
        List excep = new ArrayList();
        for(int i = 0; i < carga.size(); i++)
        {
            fb = null;
            Object reg = carga.get(i);
            if(reg.getClass().getName().indexOf(reg1.getClass().getName()) != -1)
            {
                fb = ProcesaReg1Sec.procesaReg1((Registro1DTO)reg, id_local);
                if(fb != null)
                {
                    excep.add(fb);
                    addPromoError();
                } else
                {
                    addPromoOk();
                }
            } else
            if(reg.getClass().getName().indexOf(reg5.getClass().getName()) != -1)
            {
                res = ProcesaReg5.procesaReg5((Registro5DTO)reg, id_local);
                if(res.size() > 0)
                {
                    for(int w = 0; w < res.size(); w++)
                    {
                        fb = (FeedbackSeccionDTO)res.get(w);
                        if(fb != null)
                        {
                            excep.add(fb);
                            addSkuError();
                        } else
                        {
                            addSkuOk();
                        }
                    }

                }
            } else
            if(reg.getClass().getName().indexOf(reg6.getClass().getName()) != -1)
            {
                res = ProcesaReg6.procesaReg6((Registro6DTO)reg, id_local);
                if(res.size() > 0)
                {
                    for(int w = 0; w < res.size(); w++)
                    {
                        fb = (FeedbackSeccionDTO)res.get(w);
                        if(fb != null)
                        {
                            excep.add(fb);
                            addSkuError();
                        } else
                        {
                            addSkuOk();
                        }
                    }

                }
            }
        }

        return excep;
    }

    private List wrapArchivoCarga(File archivo)
    {
        List carga = new ArrayList();
        try
        {
            int cont = 0;
            BufferedReader reg = new BufferedReader(new FileReader(archivo));
            String buffer;
            while((buffer = reg.readLine()) != null) 
            {
                cont++;
                int tipo_reg = Integer.parseInt(buffer.substring(0, 1));
                if(tipo_reg == 0)
                {
                    Registro0DTO reg0 = Registro0Wrap.wrap(buffer);
                    if(reg0 != null)
                        carga.add(reg0);
                } else
                if(tipo_reg == 1)
                {
                    Registro1DTO reg1 = Registro1Wrap.wrap(buffer);
                    if(reg1 != null)
                        carga.add(reg1);
                } else
                if(tipo_reg == 2)
                {
                    Registro2DTO reg2 = Registro2Wrap.wrap(buffer);
                    if(reg2 != null)
                        carga.add(reg2);
                } else
                if(tipo_reg == 3)
                {
                    Registro3DTO reg3 = Registro3Wrap.wrap(buffer);
                    if(reg3 != null)
                        carga.add(reg3);
                } else
                if(tipo_reg == 4)
                {
                    Registro4DTO reg4 = Registro4Wrap.wrap(buffer);
                    if(reg4 != null)
                        carga.add(reg4);
                } else
                if(tipo_reg == 5)
                {
                    Registro5DTO reg5 = Registro5Wrap.wrap(buffer);
                    if(reg5 != null)
                        carga.add(reg5);
                } else
                if(tipo_reg == 6)
                {
                    Registro6DTO reg6 = Registro6Wrap.wrap(buffer);
                    if(reg6 != null)
                        carga.add(reg6);
                } else
                if(tipo_reg == 7)
                {
                    cl.bbr.promo.carga.dto.Registro7DTO reg7 = Registro7Wrap.wrap(buffer);
                    if(reg7 != null)
                        carga.add(reg7);
                }
            }
            reg.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return carga;
    }

    private boolean checkSizeFile()
    {
        int tot_reg;
        int pos;
        int cant_reg = pos = tot_reg = tot_reg = 0;
        try
        {
            long len = archivo.length();
            if(len <= 0L)
                return false;
            cant_reg = (int)(len / (long)(SIZE_REG + 2));
            pos = (int)len - SIZE_REG - 2;
            if(pos < SIZE_REG)
                return false;
            BufferedReader buf = new BufferedReader(new FileReader(archivo));
            buf.skip(pos);
            String reg = buf.readLine();
            tot_reg = Integer.parseInt(reg.substring(1, 7));
            if(cant_reg != tot_reg)
            {
                return false;
            } else
            {
                buf.close();
                return true;
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException io)
        {
            io.printStackTrace();
        }
        return false;
    }

    private void addPromoOk()
    {
        promoOk++;
    }

    private void addPromoError()
    {
        promoError++;
    }

    private void addSkuOk()
    {
        skuOk++;
    }

    private void addSkuError()
    {
        skuError++;
    }

    public int getIdLocalBO()
    {
        return idLocalBO;
    }

    public void setIdLocalBO(int idLocalBO)
    {
        this.idLocalBO = idLocalBO;
    }

    public int getIdLocalPOS()
    {
        return idLocalPOS;
    }

    public void setIdLocalPOS(int idLocalPOS)
    {
        this.idLocalPOS = idLocalPOS;
    }

    public String getIdLocalSAP()
    {
        return idLocalSAP;
    }

    public void setIdLocalSAP(String idLocalSAP)
    {
        this.idLocalSAP = idLocalSAP;
    }

    Logging logger;
    private static int SIZE_REG = 248;
    private File archivo;
    private int idLocalBO;
    private int idLocalPOS;
    private String idLocalSAP;
    private Date finicio;
    private Date ftermino;
    private int promoOk;
    private int promoError;
    private int skuOk;
    private int skuError;

}