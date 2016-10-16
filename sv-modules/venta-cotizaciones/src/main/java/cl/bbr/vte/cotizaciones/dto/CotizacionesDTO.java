package cl.bbr.vte.cotizaciones.dto;

import java.io.Serializable;

/**
 * DTO para datos de las cotizaciones. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CotizacionesDTO implements Serializable {

	private long cot_id;
	private long cot_emp_id;
	private long cot_cli_id;
	private long cot_dir_id;
	private long cot_dirfac_id;
	private long cot_comprador_id;
	private char cot_tipo_comprador;
	private String cot_fec_ingreso;
	private String cot_fec_acordada;
	private double cot_monto_total;
	private double cot_costo_desp;
	private String cot_mpago;
	private String cot_num_mpago;
	private String cot_clave_mpago;
	private double cot_descuento;
	private String cot_obs;
	private String cot_estado;
	private String cot_fec_vencimiento;
	private String cot_nom_emp;
	private String cot_nom_suc;
	private String cot_nom_comp;
	private String cot_nomtbank;
	private int cot_ncuotas;
	private String cot_fueramix;
	private String cot_tipo_doc;
	private long cot_loc_id;
	private String cot_nom_local;
	private String cot_rut_emp;
	private String cot_dv_emp;
	private String cot_alias_dir;
	private String cot_calle_dir;
	private String cot_numero_dir;
	private String cot_tipo_nombre;
	private String cot_dfac_calle;
	private String cot_dfac_numero;
	private String cot_dfac_depto;
	private String cot_dfac_ciudad;
	private long cot_id_usuario;
	private long cot_id_usuario_fono;
	private long  cot_estado_id;
	private String nomcomunades;
	private String nomcomunafac;
	private String cot_aut_margen;
	private String cot_aut_dscto;
	private String 	sustitucion;
	private String 	persona_auto;
	private String 	nombre_banco;
	private String 	fecha_expira;
	private String  cot_emp_estado;
	private double cot_emp_saldo;
	private long cot_pol_id;
	private long cot_id_jor_desp_ref;
    private String mailComprador;
	
	
    /**
     * @return Devuelve cot_id_usuario_fono.
     */
    public long getCot_id_usuario_fono() {
        return cot_id_usuario_fono;
    }
    /**
     * @param cot_id_usuario_fono El cot_id_usuario_fono a establecer.
     */
    public void setCot_id_usuario_fono(long cot_id_usuario_fono) {
        this.cot_id_usuario_fono = cot_id_usuario_fono;
    }
	/**
	 * @return Returns the cot_pol_id.
	 */
	public long getCot_pol_id() {
		return cot_pol_id;
	}
	/**
	 * @param cot_pol_id The cot_pol_id to set.
	 */
	public void setCot_pol_id(long cot_pol_id) {
		this.cot_pol_id = cot_pol_id;
	}
	/**
	 * @return Returns the cot_aut_dscto.
	 */
	public String getCot_aut_dscto() {
		return cot_aut_dscto;
	}
	/**
	 * @param cot_aut_dscto The cot_aut_dscto to set.
	 */
	public void setCot_aut_dscto(String cot_aut_dscto) {
		this.cot_aut_dscto = cot_aut_dscto;
	}
	/**
	 * @return Returns the cot_aut_margen.
	 */
	public String getCot_aut_margen() {
		return cot_aut_margen;
	}
	/**
	 * @param cot_aut_margen The cot_aut_margen to set.
	 */
	public void setCot_aut_margen(String cot_aut_margen) {
		this.cot_aut_margen = cot_aut_margen;
	}
	/**
	 * @return Returns the cot_clave_mpago.
	 */
	public String getCot_clave_mpago() {
		return cot_clave_mpago;
	}
	/**
	 * @return Returns the cot_cli_id.
	 */
	public long getCot_cli_id() {
		return cot_cli_id;
	}
	/**
	 * @return Returns the cot_comprador_id.
	 */
	public long getCot_comprador_id() {
		return cot_comprador_id;
	}
	/**
	 * @return Returns the cot_costo_desp.
	 */
	public double getCot_costo_desp() {
		return cot_costo_desp;
	}
	/**
	 * @return Returns the cot_descuento.
	 */
	public double getCot_descuento() {
		return cot_descuento;
	}
	/**
	 * @return Returns the cot_diafac_id.
	 */
	public long getCot_dirfac_id() {
		return cot_dirfac_id;
	}
	/**
	 * @return Returns the cot_dir_id.
	 */
	public long getCot_dir_id() {
		return cot_dir_id;
	}
	/**
	 * @return Returns the cot_emp_id.
	 */
	public long getCot_emp_id() {
		return cot_emp_id;
	}
	/**
	 * @return Returns the cot_estado.
	 */
	public String getCot_estado() {
		return cot_estado;
	}
	/**
	 * @return Returns the cot_fec_acordada.
	 */
	public String getCot_fec_acordada() {
		return cot_fec_acordada;
	}
	/**
	 * @return Returns the cot_fec_ingreso.
	 */
	public String getCot_fec_ingreso() {
		return cot_fec_ingreso;
	}
	/**
	 * @return Returns the cot_fec_vencimiento.
	 */
	public String getCot_fec_vencimiento() {
		return cot_fec_vencimiento;
	}
	/**
	 * @return Returns the cot_fueramix.
	 */
	public String getCot_fueramix() {
		return cot_fueramix;
	}
	/**
	 * @return Returns the cot_id.
	 */
	public long getCot_id() {
		return cot_id;
	}
	/**
	 * @return Returns the cot_monto_total.
	 */
	public double getCot_monto_total() {
		return cot_monto_total;
	}
	/**
	 * @return Returns the cot_mpago.
	 */
	public String getCot_mpago() {
		return cot_mpago;
	}
	/**
	 * @return Returns the cot_ncuotas.
	 */
	public int getCot_ncuotas() {
		return cot_ncuotas;
	}
	/**
	 * @return Returns the cot_nom_comp.
	 */
	public String getCot_nom_comp() {
		return cot_nom_comp;
	}
	/**
	 * @return Returns the cot_nom_emp.
	 */
	public String getCot_nom_emp() {
		return cot_nom_emp;
	}
	/**
	 * @return Returns the cot_nom_suc.
	 */
	public String getCot_nom_suc() {
		return cot_nom_suc;
	}
	/**
	 * @return Returns the cot_nomtbank.
	 */
	public String getCot_nomtbank() {
		return cot_nomtbank;
	}
	/**
	 * @return Returns the cot_num_mpago.
	 */
	public String getCot_num_mpago() {
		return cot_num_mpago;
	}
	/**
	 * @return Returns the cot_obs.
	 */
	public String getCot_obs() {
		return cot_obs;
	}
	/**
	 * @return Returns the cot_tipo_comprador.
	 */
	public char getCot_tipo_comprador() {
		return cot_tipo_comprador;
	}
	/**
	 * @param cot_clave_mpago The cot_clave_mpago to set.
	 */
	public void setCot_clave_mpago(String cot_clave_mpago) {
		this.cot_clave_mpago = cot_clave_mpago;
	}
	/**
	 * @param cot_cli_id The cot_cli_id to set.
	 */
	public void setCot_cli_id(long cot_cli_id) {
		this.cot_cli_id = cot_cli_id;
	}
	/**
	 * @param cot_comprador_id The cot_comprador_id to set.
	 */
	public void setCot_comprador_id(long cot_comprador_id) {
		this.cot_comprador_id = cot_comprador_id;
	}
	/**
	 * @param cot_costo_desp The cot_costo_desp to set.
	 */
	public void setCot_costo_desp(double cot_costo_desp) {
		this.cot_costo_desp = cot_costo_desp;
	}
	/**
	 * @param cot_descuento The cot_descuento to set.
	 */
	public void setCot_descuento(double cot_descuento) {
		this.cot_descuento = cot_descuento;
	}
	/**
	 * @param cot_diafac_id The cot_diafac_id to set.
	 */
	public void setCot_dirfac_id(long cot_diafac_id) {
		this.cot_dirfac_id = cot_diafac_id;
	}
	/**
	 * @param cot_dir_id The cot_dir_id to set.
	 */
	public void setCot_dir_id(long cot_dir_id) {
		this.cot_dir_id = cot_dir_id;
	}
	/**
	 * @param cot_emp_id The cot_emp_id to set.
	 */
	public void setCot_emp_id(long cot_emp_id) {
		this.cot_emp_id = cot_emp_id;
	}
	/**
	 * @param cot_estado The cot_estado to set.
	 */
	public void setCot_estado(String cot_estado) {
		this.cot_estado = cot_estado;
	}
	/**
	 * @param cot_fec_acordada The cot_fec_acordada to set.
	 */
	public void setCot_fec_acordada(String cot_fec_acordada) {
		this.cot_fec_acordada = cot_fec_acordada;
	}
	/**
	 * @param cot_fec_ingreso The cot_fec_ingreso to set.
	 */
	public void setCot_fec_ingreso(String cot_fec_ingreso) {
		this.cot_fec_ingreso = cot_fec_ingreso;
	}
	/**
	 * @param cot_fec_vencimiento The cot_fec_vencimiento to set.
	 */
	public void setCot_fec_vencimiento(String cot_fec_vencimiento) {
		this.cot_fec_vencimiento = cot_fec_vencimiento;
	}
	/**
	 * @param cot_fueramix The cot_fueramix to set.
	 */
	public void setCot_fueramix(String cot_fueramix) {
		this.cot_fueramix = cot_fueramix;
	}
	/**
	 * @param cot_id The cot_id to set.
	 */
	public void setCot_id(long cot_id) {
		this.cot_id = cot_id;
	}
	/**
	 * @param cot_monto_total The cot_monto_total to set.
	 */
	public void setCot_monto_total(double cot_monto_total) {
		this.cot_monto_total = cot_monto_total;
	}
	/**
	 * @param cot_mpago The cot_mpago to set.
	 */
	public void setCot_mpago(String cot_mpago) {
		this.cot_mpago = cot_mpago;
	}
	/**
	 * @param cot_ncuotas The cot_ncuotas to set.
	 */
	public void setCot_ncuotas(int cot_ncuotas) {
		this.cot_ncuotas = cot_ncuotas;
	}
	/**
	 * @param cot_nom_comp The cot_nom_comp to set.
	 */
	public void setCot_nom_comp(String cot_nom_comp) {
		this.cot_nom_comp = cot_nom_comp;
	}
	/**
	 * @param cot_nom_emp The cot_nom_emp to set.
	 */
	public void setCot_nom_emp(String cot_nom_emp) {
		this.cot_nom_emp = cot_nom_emp;
	}
	/**
	 * @param cot_nom_suc The cot_nom_suc to set.
	 */
	public void setCot_nom_suc(String cot_nom_suc) {
		this.cot_nom_suc = cot_nom_suc;
	}
	/**
	 * @param cot_nomtbank The cot_nomtbank to set.
	 */
	public void setCot_nomtbank(String cot_nomtbank) {
		this.cot_nomtbank = cot_nomtbank;
	}
	/**
	 * @param cot_num_mpago The cot_num_mpago to set.
	 */
	public void setCot_num_mpago(String cot_num_mpago) {
		this.cot_num_mpago = cot_num_mpago;
	}
	/**
	 * @param cot_obs The cot_obs to set.
	 */
	public void setCot_obs(String cot_obs) {
		this.cot_obs = cot_obs;
	}
	/**
	 * @param cot_tipo_comprador The cot_tipo_comprador to set.
	 */
	public void setCot_tipo_comprador(char cot_tipo_comprador) {
		this.cot_tipo_comprador = cot_tipo_comprador;
	}
	/**
	 * @return Returns the cot_tipo_doc.
	 */
	public String getCot_tipo_doc() {
		return cot_tipo_doc;
	}
	/**
	 * @param cot_tipo_doc The cot_tipo_doc to set.
	 */
	public void setCot_tipo_doc(String cot_tipo_doc) {
		this.cot_tipo_doc = cot_tipo_doc;
	}
	/**
	 * @return Returns the cot_loc_id.
	 */
	public long getCot_loc_id() {
		return cot_loc_id;
	}
	/**
	 * @return Returns the cot_nom_local.
	 */
	public String getCot_nom_local() {
		return cot_nom_local;
	}
	/**
	 * @param cot_loc_id The cot_loc_id to set.
	 */
	public void setCot_loc_id(long cot_loc_id) {
		this.cot_loc_id = cot_loc_id;
	}
	/**
	 * @param cot_nom_local The cot_nom_local to set.
	 */
	public void setCot_nom_local(String cot_nom_local) {
		this.cot_nom_local = cot_nom_local;
	}
	/**
	 * @return Returns the cot_alias_dir.
	 */
	public String getCot_alias_dir() {
		return cot_alias_dir;
	}
	/**
	 * @return Returns the cot_calle_dir.
	 */
	public String getCot_calle_dir() {
		return cot_calle_dir;
	}
	/**
	 * @return Returns the cot_dv_emp.
	 */
	public String getCot_dv_emp() {
		return cot_dv_emp;
	}
	/**
	 * @return Returns the cot_numero_dir.
	 */
	public String getCot_numero_dir() {
		return cot_numero_dir;
	}
	/**
	 * @return Returns the cot_rut_emp.
	 */
	public String getCot_rut_emp() {
		return cot_rut_emp;
	}
	/**
	 * @param cot_alias_dir The cot_alias_dir to set.
	 */
	public void setCot_alias_dir(String cot_alias_dir) {
		this.cot_alias_dir = cot_alias_dir;
	}
	/**
	 * @param cot_calle_dir The cot_calle_dir to set.
	 */
	public void setCot_calle_dir(String cot_calle_dir) {
		this.cot_calle_dir = cot_calle_dir;
	}
	/**
	 * @param cot_dv_emp The cot_dv_emp to set.
	 */
	public void setCot_dv_emp(String cot_dv_emp) {
		this.cot_dv_emp = cot_dv_emp;
	}
	/**
	 * @param cot_numero_dir The cot_numero_dir to set.
	 */
	public void setCot_numero_dir(String cot_numero_dir) {
		this.cot_numero_dir = cot_numero_dir;
	}
	/**
	 * @param cot_rut_emp The cot_rut_emp to set.
	 */
	public void setCot_rut_emp(String cot_rut_emp) {
		this.cot_rut_emp = cot_rut_emp;
	}
	/**
	 * @return Returns the cot_dfac_calle.
	 */
	public String getCot_dfac_calle() {
		return cot_dfac_calle;
	}
	/**
	 * @param cot_dfac_calle The cot_dfac_calle to set.
	 */
	public void setCot_dfac_calle(String cot_dfac_calle) {
		this.cot_dfac_calle = cot_dfac_calle;
	}
	/**
	 * @return Returns the cot_dfac_ciudad.
	 */
	public String getCot_dfac_ciudad() {
		return cot_dfac_ciudad;
	}
	/**
	 * @param cot_dfac_ciudad The cot_dfac_ciudad to set.
	 */
	public void setCot_dfac_ciudad(String cot_dfac_ciudad) {
		this.cot_dfac_ciudad = cot_dfac_ciudad;
	}
	/**
	 * @return Returns the cot_dfac_depto.
	 */
	public String getCot_dfac_depto() {
		return cot_dfac_depto;
	}
	/**
	 * @param cot_dfac_depto The cot_dfac_depto to set.
	 */
	public void setCot_dfac_depto(String cot_dfac_depto) {
		this.cot_dfac_depto = cot_dfac_depto;
	}
	/**
	 * @return Returns the cot_dfac_numero.
	 */
	public String getCot_dfac_numero() {
		return cot_dfac_numero;
	}
	/**
	 * @param cot_dfac_numero The cot_dfac_numero to set.
	 */
	public void setCot_dfac_numero(String cot_dfac_numero) {
		this.cot_dfac_numero = cot_dfac_numero;
	}
	/**
	 * @return Returns the cot_tipo_nombre.
	 */
	public String getCot_tipo_nombre() {
		return cot_tipo_nombre;
	}
	/**
	 * @param cot_tipo_nombre The cot_tipo_nombre to set.
	 */
	public void setCot_tipo_nombre(String cot_tipo_nombre) {
		this.cot_tipo_nombre = cot_tipo_nombre;
	}

	/**
	 * @return Returns the cot_id_usuario.
	 */
	public long getCot_id_usuario() {
		return cot_id_usuario;
	}
	/**
	 * @param cot_id_usuario The cot_id_usuario to set.
	 */
	public void setCot_id_usuario(long cot_id_usuario) {
		this.cot_id_usuario = cot_id_usuario;
	}
	/**
	 * @return Returns the cot_estado_id.
	 */
	public long getCot_estado_id() {
		return cot_estado_id;
	}
	/**
	 * @param cot_estado_id The cot_estado_id to set.
	 */
	public void setCot_estado_id(long cot_estado_id) {
		this.cot_estado_id = cot_estado_id;
	}
	/**
	 * @return Returns the nomcomunades.
	 */
	public String getNomcomunades() {
		return nomcomunades;
	}
	/**
	 * @param nomcomunades The nomcomunades to set.
	 */
	public void setNomcomunades(String nomcomunades) {
		this.nomcomunades = nomcomunades;
	}
	/**
	 * @return Returns the nomcomunafac.
	 */
	public String getNomcomunafac() {
		return nomcomunafac;
	}
	/**
	 * @param nomcomunafac The nomcomunafac to set.
	 */
	public void setNomcomunafac(String nomcomunafac) {
		this.nomcomunafac = nomcomunafac;
	}
	/**
	 * @return Returns the nombre_banco.
	 */
	public String getNombre_banco() {
		return nombre_banco;
	}
	/**
	 * @param nombre_banco The nombre_banco to set.
	 */
	public void setNombre_banco(String nombre_banco) {
		this.nombre_banco = nombre_banco;
	}
	/**
	 * @return Returns the persona_auto.
	 */
	public String getPersona_auto() {
		return persona_auto;
	}
	/**
	 * @param persona_auto The persona_auto to set.
	 */
	public void setPersona_auto(String persona_auto) {
		this.persona_auto = persona_auto;
	}
	/**
	 * @return Returns the sustitucion.
	 */
	public String getSustitucion() {
		return sustitucion;
	}
	/**
	 * @param sustitucion The sustitucion to set.
	 */
	public void setSustitucion(String sustitucion) {
		this.sustitucion = sustitucion;
	}
	/**
	 * @return Returns the fecha_expira.
	 */
	public String getFecha_expira() {
		return fecha_expira;
	}
	/**
	 * @param fecha_expira The fecha_expira to set.
	 */
	public void setFecha_expira(String fecha_expira) {
		this.fecha_expira = fecha_expira;
	}
	/**
	 * @return Returns the cot_emp_estado.
	 */
	public String getCot_emp_estado() {
		return cot_emp_estado;
	}
	/**
	 * @param cot_emp_estado The cot_emp_estado to set.
	 */
	public void setCot_emp_estado(String cot_emp_estado) {
		this.cot_emp_estado = cot_emp_estado;
	}
	/**
	 * @return Returns the cot_emp_saldo.
	 */
	public double getCot_emp_saldo() {
		return cot_emp_saldo;
	}
	/**
	 * @param cot_emp_saldo The cot_emp_saldo to set.
	 */
	public void setCot_emp_saldo(double cot_emp_saldo) {
		this.cot_emp_saldo = cot_emp_saldo;
	}
	/**
	 * @return Returns the cot_id_jor_desp_ref.
	 */
	public long getCot_id_jor_desp_ref() {
		return cot_id_jor_desp_ref;
	}
	/**
	 * @param cot_id_jor_desp_ref The cot_id_jor_desp_ref to set.
	 */
	public void setCot_id_jor_desp_ref(long cot_id_jor_desp_ref) {
		this.cot_id_jor_desp_ref = cot_id_jor_desp_ref;
	}


	
    /**
     * @return Devuelve mailComprador.
     */
    public String getMailComprador() {
        return mailComprador;
    }
    /**
     * @param mailComprador El mailComprador a establecer.
     */
    public void setMailComprador(String mailComprador) {
        this.mailComprador = mailComprador;
    }
}


