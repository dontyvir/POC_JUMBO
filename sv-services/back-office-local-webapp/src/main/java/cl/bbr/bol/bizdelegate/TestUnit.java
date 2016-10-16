package cl.bbr.bol.bizdelegate;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.bol.exceptions.BolException;
import cl.bbr.jumbocl.common.exceptions.*;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.pedidos.exceptions.LocalException;

public class TestUnit {

	private static void prt(String msg){
		System.out.println(msg);
	}
	
	public static void main(String[] args) throws LocalException, BolException, SystemException, ServiceException {
		
		prt("-----------------------------------------------");
		prt("        Test Zona Despacho                     ");
		prt("-----------------------------------------------");
		
		getZonaDespacho();
		
		prt("-----------------------------------------------");
		prt("        Test getCalendarioDespacho                     ");
		prt("-----------------------------------------------");
		
		getCalendarioDespacho(25,2006,1);
		
		prt("-----------------------------------------------");
		prt("        Test Inserta/Modifica Zona Despacho             ");
		prt("-----------------------------------------------");
		
		//doAgregaZonaDespacho();
		doModificaZonaDespacho();
		
		
	}
	
	private static void getZonaDespacho() throws BolException{
		
		ZonaDTO zona = new ZonaDTO();
		BizDelegate biz = new BizDelegate();
		
		zona = biz.getZonaDespacho(1);
		
		prt("id_zona: " + zona.getId_zona());
		prt("nombre: " + zona.getNombre());
			
	}
	
	
	public static void getCalendarioDespacho(int n_semana, int ano, long id_zona)
		throws BolException {
	
		List horarios = new ArrayList();
		List jornadas = new ArrayList();
		
		BizDelegate biz = new BizDelegate();

		CalendarioDespachoDTO cal = biz.getCalendarioDespacho(n_semana, ano, id_zona);
		
		SemanasEntity sem = cal.getSemana();
		
		horarios = cal.getHorarios();
		
		prt("id_Semana: " + sem.getId_semana() + " - " + sem.getF_ini() + " - " + sem.getF_fin());
		
		
		for (int i=0; i< horarios.size(); i++){
			
			HorarioDespachoEntity hor = new HorarioDespachoEntity();
			hor = (HorarioDespachoEntity) horarios.get(i);
			
			prt("id_hor_desp: " + hor.getId_hor_desp() + " - " 
				+ hor.getId_semana() + " - " + hor.getH_ini() 
				+ " - " + hor.getH_fin());
			
		}
		
		jornadas = cal.getJornadas();
		
		for (int j=0; j< jornadas.size(); j++){
			
			JornadaDespachoEntity jor = new JornadaDespachoEntity();
			jor = (JornadaDespachoEntity) jornadas.get(j);
			
			prt("id_jdespacho: " + jor.getId_jdespacho() + " - " 
				+ jor.getCapac_picking() + " - " + jor.getId_semana()
				+ " - " + jor.getDay_of_week());
			
		}	
		
		
	
		
	}	
	
	
	public static void doAgregaZonaDespacho() throws BolException, SystemException, ServiceException{
		long id_zona = -1;
		ZonaDTO zona = new ZonaDTO();
		zona.setId_local(1);
		zona.setNombre("Zona Periferica");
		zona.setDescripcion("Zona que queda muy lejos");
		zona.setTarifa_normal_alta(3950);
		zona.setTarifa_normal_media(2950);
		zona.setTarifa_normal_baja(1950);
		
		BizDelegate biz = new BizDelegate();
		
		id_zona = biz.doAgregaZonaDespacho(zona);
		
		prt("id_zona insertado: " +  id_zona);
		
	}
	
	public static void doModificaZonaDespacho() throws BolException, SystemException, ServiceException{

		ZonaDTO zona = new ZonaDTO();
		zona.setId_local(1);
		zona.setId_zona(21);
		zona.setNombre("Zona Periferica");
		zona.setDescripcion("");
		zona.setTarifa_normal_alta(3950);
		zona.setTarifa_normal_media(2950);
		zona.setTarifa_normal_baja(1950);
		
		BizDelegate biz = new BizDelegate();
		
		biz.doModZonaDespacho(zona);
		
		prt("Fin");
		
	}
	
	
	
}
