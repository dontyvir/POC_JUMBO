package cl.bbr.jumbocl.pedidos.promos.cargas.dto;

import cl.bbr.jumbocl.common.utils.Formatos;

public class HeaderFeedBackDTO {
	/* constantes de largo header */
	public static final int TIPO_LEN = 2;
	public static final int LOCAL_LEN = 4;
	public static final int TAREA_LEN = 6;
	public static final int ESTADO_LEN = 2;
	public static final int COD_RET_LEN = 2;
	public static final int NRO_PROD_PROC_LEN = 6;
	public static final int NRO_PROD_NPROC_LEN = 6;
	public static final int NRO_PROM_PROC_LEN = 6;
	public static final int NRO_PROM_NPROC_LEN = 6;
	public static final int FECHA_INI_LEN = 14;
	public static final int FECHA_FIN_LEN = 14;
	public static final int NRO_BLOQ = 6;
	public static final int TOT_BLOQ = 6;
	public static final int REG_EXCEP = 6;
	
	public static final int HEADER_FB_LEN   = 86;
	
	public String tipo;
	public String local;
	public String tarea;
	public String estado;
	public String cod_ret;
	public String nro_prod_proc;
	public String nro_prod_nproc;
	public String nro_prom_proc;
	public String nro_prom_nproc;
	public String fecha_ini;
	public String fecha_fin;
	public String nro_bloq;
	public String tot_bloq;
	public String reg_excep;

	public HeaderFeedBackDTO() {
	}

	public String toMsg(){
		String out = "";
		out += Formatos.formatField(tipo, 				TIPO_LEN, 				Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(local, 				LOCAL_LEN, 				Formatos.ALIGN_RIGHT," ");
		out += Formatos.formatField(tarea, 				TAREA_LEN, 				Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(estado, 			ESTADO_LEN, 			Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(cod_ret, 			COD_RET_LEN, 			Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(nro_prod_proc, 		NRO_PROD_PROC_LEN, 		Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(nro_prod_nproc, 	NRO_PROD_NPROC_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(nro_prom_proc, 		NRO_PROM_PROC_LEN, 		Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(nro_prom_nproc, 	NRO_PROM_NPROC_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(fecha_ini, 			FECHA_INI_LEN, 			Formatos.ALIGN_LEFT,"0");
		out += Formatos.formatField(fecha_fin, 			FECHA_FIN_LEN, 			Formatos.ALIGN_LEFT,"0");		
		out += Formatos.formatField(nro_bloq, 			NRO_BLOQ, 				Formatos.ALIGN_RIGHT,"0");	
		out += Formatos.formatField(tot_bloq, 			TOT_BLOQ, 				Formatos.ALIGN_RIGHT,"0");	
		out += Formatos.formatField(reg_excep, 			REG_EXCEP, 				Formatos.ALIGN_RIGHT,"0");	
		return out;
	}
	
	/**
	 * @return Returns the cod_ret.
	 */
	public String getCod_ret() {
		return cod_ret;
	}

	/**
	 * @param cod_ret The cod_ret to set.
	 */
	public void setCod_ret(String cod_ret) {
		this.cod_ret = cod_ret;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the fecha_fin.
	 */
	public String getFecha_fin() {
		return fecha_fin;
	}

	/**
	 * @param fecha_fin The fecha_fin to set.
	 */
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	/**
	 * @return Returns the fecha_ini.
	 */
	public String getFecha_ini() {
		return fecha_ini;
	}

	/**
	 * @param fecha_ini The fecha_ini to set.
	 */
	public void setFecha_ini(String fecha_ini) {
		this.fecha_ini = fecha_ini;
	}

	/**
	 * @return Returns the local.
	 */
	public String getLocal() {
		return local;
	}

	/**
	 * @param local The local to set.
	 */
	public void setLocal(String local) {
		this.local = local;
	}

	/**
	 * @return Returns the nro_bloq.
	 */
	public String getNro_bloq() {
		return nro_bloq;
	}

	/**
	 * @param nro_bloq The nro_bloq to set.
	 */
	public void setNro_bloq(String nro_bloq) {
		this.nro_bloq = nro_bloq;
	}

	/**
	 * @return Returns the nro_prod_nproc.
	 */
	public String getNro_prod_nproc() {
		return nro_prod_nproc;
	}

	/**
	 * @param nro_prod_nproc The nro_prod_nproc to set.
	 */
	public void setNro_prod_nproc(String nro_prod_nproc) {
		this.nro_prod_nproc = nro_prod_nproc;
	}

	/**
	 * @return Returns the nro_prod_proc.
	 */
	public String getNro_prod_proc() {
		return nro_prod_proc;
	}

	/**
	 * @param nro_prod_proc The nro_prod_proc to set.
	 */
	public void setNro_prod_proc(String nro_prod_proc) {
		this.nro_prod_proc = nro_prod_proc;
	}

	/**
	 * @return Returns the nro_prom_nproc.
	 */
	public String getNro_prom_nproc() {
		return nro_prom_nproc;
	}

	/**
	 * @param nro_prom_nproc The nro_prom_nproc to set.
	 */
	public void setNro_prom_nproc(String nro_prom_nproc) {
		this.nro_prom_nproc = nro_prom_nproc;
	}

	/**
	 * @return Returns the nro_prom_proc.
	 */
	public String getNro_prom_proc() {
		return nro_prom_proc;
	}

	/**
	 * @param nro_prom_proc The nro_prom_proc to set.
	 */
	public void setNro_prom_proc(String nro_prom_proc) {
		this.nro_prom_proc = nro_prom_proc;
	}

	/**
	 * @return Returns the reg_excep.
	 */
	public String getReg_excep() {
		return reg_excep;
	}

	/**
	 * @param reg_excep The reg_excep to set.
	 */
	public void setReg_excep(String reg_excep) {
		this.reg_excep = reg_excep;
	}

	/**
	 * @return Returns the tarea.
	 */
	public String getTarea() {
		return tarea;
	}

	/**
	 * @param tarea The tarea to set.
	 */
	public void setTarea(String tarea) {
		this.tarea = tarea;
	}

	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return Returns the tot_bloq.
	 */
	public String getTot_bloq() {
		return tot_bloq;
	}

	/**
	 * @param tot_bloq The tot_bloq to set.
	 */
	public void setTot_bloq(String tot_bloq) {
		this.tot_bloq = tot_bloq;
	}

}
