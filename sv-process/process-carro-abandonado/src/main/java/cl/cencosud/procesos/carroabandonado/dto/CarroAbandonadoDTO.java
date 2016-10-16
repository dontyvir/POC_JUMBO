package cl.cencosud.procesos.carroabandonado.dto;

public class CarroAbandonadoDTO {

	private int cli_id;
	private int cli_rut;
	private String cli_dv;
	private String cli_nombre;
	private String cli_nombres_mail;
	private String cli_apellido_pat;
	private String cli_apellido_mat;
	private String cli_email;
	
	private String cat_nombre;
	private int cat_id;
	private int rubro_id;
	private int cat_suma;
	
	private int car_suma;
	
	private String car_cupon;
	private String car_tipo_cupon;
	private int car_cupon_cantidad;
	private String car_cupon_monto_mail;
	private String car_fecha_fin;
	
	private StringBuffer car_text_mail;
	
	public CarroAbandonadoDTO() {

	}


	public int getCli_id() {
		return cli_id;
	}


	public void setCli_id(int cli_id) {
		this.cli_id = cli_id;
	}


	public int getCli_rut() {
		return cli_rut;
	}


	public void setCli_rut(int cli_rut) {
		this.cli_rut = cli_rut;
	}


	public String getCli_dv() {
		return cli_dv;
	}


	public void setCli_dv(String cli_dv) {
		this.cli_dv = cli_dv;
	}


	public String getCli_nombre() {
		return cli_nombre;
	}


	public void setCli_nombre(String cli_nombre) {
		this.cli_nombre = cli_nombre;
	}


	public String getCli_apellido_pat() {
		return cli_apellido_pat;
	}


	public void setCli_apellido_pat(String cli_apellido_pat) {
		this.cli_apellido_pat = cli_apellido_pat;
	}


	public String getCli_apellido_mat() {
		return cli_apellido_mat;
	}


	public void setCli_apellido_mat(String cli_apellido_mat) {
		this.cli_apellido_mat = cli_apellido_mat;
	}


	public String getCli_email() {
		return cli_email;
	}


	public void setCli_email(String cli_email) {
		this.cli_email = cli_email;
	}


	public String getCat_nombre() {
		return cat_nombre;
	}


	public void setCat_nombre(String cat_nombre) {
		this.cat_nombre = cat_nombre;
	}


	public int getCat_id() {
		return cat_id;
	}


	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}


	public int getCat_suma() {
		return cat_suma;
	}


	public void setCat_suma(int cat_suma) {
		this.cat_suma = cat_suma;
	}


	public int getCar_suma() {
		return car_suma;
	}


	public void setCar_suma(int car_suma) {
		this.car_suma = car_suma;
	}


	public String getCar_cupon() {
		return car_cupon;
	}


	public void setCar_cupon(String car_cupon) {
		this.car_cupon = car_cupon;
	}


	public String getCar_tipo_cupon() {
		return car_tipo_cupon;
	}


	public void setCar_tipo_cupon(String car_tipo_cupon) {
		this.car_tipo_cupon = car_tipo_cupon;
	}


	public int getCar_cupon_cantidad() {
		return car_cupon_cantidad;
	}


	public void setCar_cupon_cantidad(int car_cupon_cantidad) {
		this.car_cupon_cantidad = car_cupon_cantidad;
	}


	public String getCar_fecha_fin() {
		return car_fecha_fin;
	}


	public void setCar_fecha_fin(String car_fecha_fin) {
		this.car_fecha_fin = car_fecha_fin;
	}


	public StringBuffer getCar_text_mail() {
		return car_text_mail;
	}


	public void setCar_text_mail(StringBuffer car_text_mail) {
		this.car_text_mail = car_text_mail;
	}


	public String getCar_cupon_monto_mail() {
		return car_cupon_monto_mail;
	}


	public void setCar_cupon_monto_mail(String car_cupon_monto_mail) {
		this.car_cupon_monto_mail = car_cupon_monto_mail;
	}


	public String getCli_nombres_mail() {
		return cli_nombres_mail;
	}


	public void setCli_nombres_mail(String cli_nombres_mail) {
		this.cli_nombres_mail = cli_nombres_mail;
	}


	public int getRubro_id() {
		return rubro_id;
	}


	public void setRubro_id(int rubro_id) {
		this.rubro_id = rubro_id;
	}
	
	
}
