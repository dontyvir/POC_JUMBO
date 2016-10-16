package cl.bbr.jumbocl.common.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Clase que captura desde la base de datos los datos de una semana
 * @author bbr
 *
 */
public class SemanasEntity implements Serializable {

	long	id_semana;
	int		n_semana;
	int		ano;
	Date	f_ini;
	Date	f_fin;
	
	/**
	 * Constructor 
	 */
	public SemanasEntity(){
		
	}

	/**
	 * @return ano
	 */
	public int getAno() {
		return ano;
	}

	/**
	 * @param ano
	 */
	public void setAno(int ano) {
		this.ano = ano;
	}

	/**
	 * @return f_fin
	 */
	public Date getF_fin() {
		return f_fin;
	}

	/**
	 * @param f_fin
	 */
	public void setF_fin(Date f_fin) {
		this.f_fin = f_fin;
	}

	/**
	 * @return f_ini
	 */
	public Date getF_ini() {
		return f_ini;
	}

	/**
	 * @param f_ini
	 */
	public void setF_ini(Date f_ini) {
		this.f_ini = f_ini;
	}

	/**
	 * @return id_semana
	 */
	public long getId_semana() {
		return id_semana;
	}

	/**
	 * @param id_semana
	 */
	public void setId_semana(long id_semana) {
		this.id_semana = id_semana;
	}

	/**
	 * @return n_semana
	 */
	public int getN_semana() {
		return n_semana;
	}

	/**
	 * @param n_semana
	 */
	public void setN_semana(int n_semana) {
		this.n_semana = n_semana;
	}
	
}
