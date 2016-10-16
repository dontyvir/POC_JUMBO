package cl.cencosud.informes.descuento.colaborador.dto;

public class InformeColaboradorDTO {

	private ColaboradorDTO colaborador;
	private int compraAcumulada;
	private int descuentoAcumlado;

	public InformeColaboradorDTO() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public ColaboradorDTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(ColaboradorDTO colaborador) {
		this.colaborador = colaborador;
	}

	public int getCompraAcumulada() {
		return compraAcumulada;
	}

	public void setCompraAcumulada(int compraAcumulada) {
		this.compraAcumulada = compraAcumulada;
	}

	public int getDescuentoAcumlado() {
		return descuentoAcumlado;
	}

	public void setDescuentoAcumlado(int descuentoAcumlado) {
		this.descuentoAcumlado = descuentoAcumlado;
	}

	public String toString() {
		return colaborador.toString() + " " + compraAcumulada + " "
				+ descuentoAcumlado;
	}

}
