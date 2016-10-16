package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

/**
 * Clase que captura desde la Base de Datos los datos de direccion de una factura
 * 
 * @author BBR
 *
 */
public class DirFacturacionEntity {

	private Long 	dfac_id;
	private Long 	dfac_tip_id;
	private Long 	dfac_cli_id;
	private Long 	dfac_com_id;
	private String 	dfac_alias;
	private String 	dfac_calle;
	private String 	dfac_numero;
	private String 	dfac_depto;
	private String 	dfac_comentarios;
	private String 	dfac_estado;
	private String 	dfac_ciudad;
	private String 	dfac_fax;
	private String 	dfac_nom_contacto;
	private String 	dfac_cargo;
	private String 	dfac_email;
	private String 	dfac_fono1;
	private String 	dfac_fono2;
	private String 	dfac_fono3;
	private Timestamp dfac_fec_crea;
	//nombres
	private String  nom_tip_calle;
	private String  nom_comuna;
	private String  nom_region;
	private String  nom_local;
	
	
	public Timestamp getDfac_fec_crea() {
		return dfac_fec_crea;
	}
	public void setDfac_fec_crea(Timestamp dfac_fec_crea) {
		this.dfac_fec_crea = dfac_fec_crea;
	}
	public String getDfac_alias() {
		return dfac_alias;
	}
	public void setDfac_alias(String dfac_alias) {
		this.dfac_alias = dfac_alias;
	}
	public String getDfac_calle() {
		return dfac_calle;
	}
	public void setDfac_calle(String dfac_calle) {
		this.dfac_calle = dfac_calle;
	}
	public String getDfac_cargo() {
		return dfac_cargo;
	}
	public void setDfac_cargo(String dfac_cargo) {
		this.dfac_cargo = dfac_cargo;
	}
	public String getDfac_ciudad() {
		return dfac_ciudad;
	}
	public void setDfac_ciudad(String dfac_ciudad) {
		this.dfac_ciudad = dfac_ciudad;
	}
	public Long getDfac_cli_id() {
		return dfac_cli_id;
	}
	public void setDfac_cli_id(Long dfac_cli_id) {
		this.dfac_cli_id = dfac_cli_id;
	}
	public Long getDfac_com_id() {
		return dfac_com_id;
	}
	public void setDfac_com_id(Long dfac_com_id) {
		this.dfac_com_id = dfac_com_id;
	}
	public String getDfac_comentarios() {
		return dfac_comentarios;
	}
	public void setDfac_comentarios(String dfac_comentarios) {
		this.dfac_comentarios = dfac_comentarios;
	}
	public String getDfac_depto() {
		return dfac_depto;
	}
	public void setDfac_depto(String dfac_depto) {
		this.dfac_depto = dfac_depto;
	}
	public String getDfac_email() {
		return dfac_email;
	}
	public void setDfac_email(String dfac_email) {
		this.dfac_email = dfac_email;
	}
	public String getDfac_estado() {
		return dfac_estado;
	}
	public void setDfac_estado(String dfac_estado) {
		this.dfac_estado = dfac_estado;
	}
	public String getDfac_fax() {
		return dfac_fax;
	}
	public void setDfac_fax(String dfac_fax) {
		this.dfac_fax = dfac_fax;
	}
	public String getDfac_fono1() {
		return dfac_fono1;
	}
	public void setDfac_fono1(String dfac_fono1) {
		this.dfac_fono1 = dfac_fono1;
	}
	public String getDfac_fono2() {
		return dfac_fono2;
	}
	public void setDfac_fono2(String dfac_fono2) {
		this.dfac_fono2 = dfac_fono2;
	}
	public String getDfac_fono3() {
		return dfac_fono3;
	}
	public void setDfac_fono3(String dfac_fono3) {
		this.dfac_fono3 = dfac_fono3;
	}
	public Long getDfac_id() {
		return dfac_id;
	}
	public void setDfac_id(Long dfac_id) {
		this.dfac_id = dfac_id;
	}
	public String getDfac_nom_contacto() {
		return dfac_nom_contacto;
	}
	public void setDfac_nom_contacto(String dfac_nom_contacto) {
		this.dfac_nom_contacto = dfac_nom_contacto;
	}
	public String getDfac_numero() {
		return dfac_numero;
	}
	public void setDfac_numero(String dfac_numero) {
		this.dfac_numero = dfac_numero;
	}
	public Long getDfac_tip_id() {
		return dfac_tip_id;
	}
	public void setDfac_tip_id(Long dfac_tip_id) {
		this.dfac_tip_id = dfac_tip_id;
	}
	public String getNom_comuna() {
		return nom_comuna;
	}
	public void setNom_comuna(String nom_comuna) {
		this.nom_comuna = nom_comuna;
	}
	public String getNom_local() {
		return nom_local;
	}
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}
	public String getNom_region() {
		return nom_region;
	}
	public void setNom_region(String nom_region) {
		this.nom_region = nom_region;
	}
	public String getNom_tip_calle() {
		return nom_tip_calle;
	}
	public void setNom_tip_calle(String nom_tip_calle) {
		this.nom_tip_calle = nom_tip_calle;
	}
	
}
