package cl.bbr.vte.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.fastm.IValueSet;
import net.sf.fastm.ValueSet;

import cl.bbr.vte.empresas.dto.ComprXSucDTO;

/**
 * Calse que permite presentar el filtro para empresas en las vistas
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FiltroEmpresas {

	/**
	 * Listado de empresas
	 */
	private List datos_empresas = new ArrayList();
	/**
	 * Listado de sucursales
	 */
	private List datos_sucursales = new ArrayList();	
	
	/**
	 * Constructor
	 */
	public FiltroEmpresas() {
		
	}
	
	/**
	 * Genera el listado de empresas y susursales para el despliegue en el filtro
	 * 
	 * @param l_comp		Listado de empresas y sucursales para el comprador
	 * @param ses_emp_id	Identificador único de la empresa seleccionada
	 */
	public void setFiltroEmpresas( List l_comp, Object ses_emp_id ) {
		// Recupera lista de empresas y sucursales para el comprador		
		// para el filtro de empresa-sucursal
		ComprXSucDTO dto = null;
		List suc_aux = new ArrayList();
		for( int i = 0; i < l_comp.size(); i++ ) {
			dto = (ComprXSucDTO)l_comp.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{emp_nombre}", dto.getNom_empresa() );			
			fila.setVariable("{emp_id}", dto.getId_empresa() + "" );
			fila.setVariable("{suc_nombre}", dto.getNom_sucursal() );
			fila.setVariable("{suc_id}", dto.getId_sucursal() + "" );
			datos_sucursales.add(fila);
			if( suc_aux.contains( dto.getId_empresa() + "" ) == false ) {
				datos_empresas.add(fila);				
				suc_aux.add( dto.getId_empresa() + "" );
			}
			if( ses_emp_id != null && dto.getId_empresa() == Long.parseLong(ses_emp_id.toString()) ) {
				fila.setVariable("{emp_selected}","selected");
			}
			else {
				fila.setVariable("{emp_selected}","");
			}
		}

	}
	
	/**
	 * Entrega el listado con los datos de las empresas para el template
	 * 
	 * @return Listado empresas
	 */
	public List getDatos_empresas() {
		return datos_empresas;
	}
	
	/**
	 * Entrega el listado con los datos de las sucursales para el template
	 * 
	 * @return Listado sucursales
	 */
	public List getDatos_sucursales() {
		return datos_sucursales;
	}
}
