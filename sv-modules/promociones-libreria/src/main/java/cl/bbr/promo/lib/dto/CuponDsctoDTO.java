package cl.bbr.promo.lib.dto;

public class CuponDsctoDTO {
	
	private long id_cup_dto;
    private long id_usuario;
    private String codigo;
    private String tipo;
    private int descuento;
    private int cantidad;
    private int despacho;
    private int publico;
    private int medio_pago;
    private String fecha_ini;
    private String fecha_fin;
    private boolean utilizado;
    
	public long getId_cup_dto() {
		return id_cup_dto;
	}
	public void setId_cup_dto(long id_cup_dto) {
		this.id_cup_dto = id_cup_dto;
	}
	public long getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getDescuento() {
		return descuento;
	}
	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public int getDespacho() {
		return despacho;
	}
	public void setDespacho(int despacho) {
		this.despacho = despacho;
	}
	public int getPublico() {
		return publico;
	}
	public void setPublico(int publico) {
		this.publico = publico;
	}
	public int getMedio_pago() {
		return medio_pago;
	}
	public void setMedio_pago(int medio_pago) {
		this.medio_pago = medio_pago;
	}
	public String getFecha_ini() {
		return fecha_ini;
	}
	public void setFecha_ini(String fecha_ini) {
		this.fecha_ini = fecha_ini;
	}
	public String getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public String toString(){
		String result = "";
		
		result += this.getId_cup_dto();
		result += " | " + this.getCodigo();
		result += " | " + this.getTipo();
		result += " | " + this.getDescuento();
		result += " | " + this.getCantidad();
		result += " | " + this.getDespacho();
		result += " | " + this.getPublico();
		result += " | " + this.getMedio_pago();
		result += " | " + this.getFecha_ini();
		result += " | " + this.getFecha_fin();
				
		return result;
	}
	public boolean isUtilizado() {
		return utilizado;
	}
	public void setUtilizado(boolean utilizado) {
		this.utilizado = utilizado;
	}
	
}
