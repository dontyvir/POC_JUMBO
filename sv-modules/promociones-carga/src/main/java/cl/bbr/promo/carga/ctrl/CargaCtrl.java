// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:21:54
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CargaCtrl.java

package cl.bbr.promo.carga.ctrl;

import cl.bbr.promo.carga.dto.FeedbackDTO;
import cl.bbr.promo.carga.helper.ProcesaArchivo;
import java.io.File;

public class CargaCtrl
{

    public CargaCtrl(File archivo, String idLocalSAP)
    {
        this.archivo = new ProcesaArchivo(archivo, idLocalSAP);
    }

    public CargaCtrl(String archivo, String idLocalSAP)
    {
        this.archivo = new ProcesaArchivo(archivo, idLocalSAP);
    }

    public FeedbackDTO procesa()
    {
        return archivo.procesa();
    }

    private ProcesaArchivo archivo;
}