package cl.bbr.cupondscto.dto;

public class CarroAbandonadoDTO {
	
	private String car_cli_id;
	private String car_fec_mail;
	private String car_tipo_dcto;
	private String car_cli_nombres;
	private String car_fecha_fin;
	private String car_monto_dcto;
	private String car_cupon;
	private String car_nom_seccion;
	private int car_estado_mail;

	public CarroAbandonadoDTO() {

	}

	public String getCar_cli_id() {
		return car_cli_id;
	}

	public void setCar_cli_id(String car_cli_id) {
		this.car_cli_id = car_cli_id;
	}

	public String getCar_fec_mail() {
		return car_fec_mail;
	}

	public void setCar_fec_mail(String car_fec_mail) {
		this.car_fec_mail = car_fec_mail;
	}

	public String getCar_tipo_dcto() {
		return car_tipo_dcto;
	}

	public void setCar_tipo_dcto(String car_tipo_dcto) {
		this.car_tipo_dcto = car_tipo_dcto;
	}

	public String getCar_cli_nombres() {
		return car_cli_nombres;
	}

	public void setCar_cli_nombres(String car_cli_nombres) {
		this.car_cli_nombres = car_cli_nombres;
	}

	public String getCar_fecha_fin() {
		return car_fecha_fin;
	}

	public void setCar_fecha_fin(String car_fecha_fin) {
		this.car_fecha_fin = car_fecha_fin;
	}

	public String getCar_monto_dcto() {
		return car_monto_dcto;
	}

	public void setCar_monto_dcto(String car_monto_dcto) {
		this.car_monto_dcto = car_monto_dcto;
	}

	public String getCar_nom_seccion() {
		return car_nom_seccion;
	}

	public void setCar_nom_seccion(String car_nom_seccion) {
		this.car_nom_seccion = car_nom_seccion;
	}

	public int getCar_estado_mail() {
		return car_estado_mail;
	}

	public void setCar_estado_mail(int car_estado_mail) {
		this.car_estado_mail = car_estado_mail;
	}

	public String getCar_cupon() {
		return car_cupon;
	}

	public void setCar_cupon(String car_cupon) {
		this.car_cupon = car_cupon;
	}
	
}
