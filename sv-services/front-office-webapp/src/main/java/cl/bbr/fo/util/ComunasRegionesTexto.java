package cl.bbr.fo.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.fastm.IValueSet;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;

public class ComunasRegionesTexto {
	public static String ComunaRegionTexto(HttpSession session){
		
		String comReg = "";
		
        ArrayList arr_regiones = new ArrayList();
        long id_region = 0, id_comuna=0;
        BizDelegate biz = new BizDelegate();
        IValueSet fila = null;
        
        
        if (session.getAttribute("ses_comuna_cliente") != null && 
                !session.getAttribute("ses_comuna_cliente").toString().trim().equals("")){
            String tmp = session.getAttribute("ses_comuna_cliente").toString();
            String[] com = tmp.split("-=-"); //id_region-id_comuna-nom_comuna
            id_region = Long.parseLong(com[0]);
            id_comuna = Long.parseLong(com[1]);
        }
        

		List regiones = null;
		try {
			regiones = biz.regionesConCobertura();
		} catch (SystemException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		}
        

        for (int i = 0; i < regiones.size(); i++) {
            RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
            IValueSet filaReg = new ValueSet();
            filaReg.setVariable("{option_reg_id}", dbregion.getId()+"");
            filaReg.setVariable("{option_reg_nombre}", dbregion.getNombre());
            
            if(dbregion.getId()==id_region)
            	comReg=dbregion.getNombre().toString();

        }
        
        //top.setDynamicValueSets("REGIONES", arr_regiones);

        /*************   LISTADO DE COMUNAS   *****************/
        ArrayList arr_comunas = new ArrayList();
        List comunas = null;
		try {
			comunas = biz.comunasConCoberturaByRegion( id_region );
		} catch (SystemException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		}



        for (int i = 0; i < comunas.size(); i++) {
            ComunaDTO dbcomuna = (ComunaDTO) comunas.get(i);
            IValueSet filaCom = new ValueSet();
            filaCom.setVariable("{option_com_id}", dbcomuna.getId()+"");
            filaCom.setVariable("{option_com_nombre}", dbcomuna.getNombre());
            if (dbcomuna.getId() == id_comuna)
            	comReg=comReg+"-"+dbcomuna.getNombre();
           
        }

		return comReg;
		
	}
}
