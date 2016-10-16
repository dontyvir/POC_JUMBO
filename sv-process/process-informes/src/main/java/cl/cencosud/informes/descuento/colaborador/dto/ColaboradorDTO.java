package cl.cencosud.informes.descuento.colaborador.dto;

public class ColaboradorDTO {

	private int colRut;
	private int codEmpresa;
	private String empresa;

	public ColaboradorDTO() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public ColaboradorDTO(int colRut, int codEmpresa, String empresa) {
		this.colRut = colRut;
		this.codEmpresa = codEmpresa;
		this.empresa = empresa;
	}

	public int getColRut() {
		return colRut;
	}

	public void setColRut(int colRut) {
		this.colRut = colRut;
	}

	public int getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(int codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String toString() {
		return colRut + " " + codEmpresa + " " + empresa.toString();
	}

}
