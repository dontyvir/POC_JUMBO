package cl.bbr.promo.lib.dto;

public class PrioridadPromosDTO {
	private long codPromoEvento;
	private long codPromoPeriodica;
	private long codPromoNormal;

	public PrioridadPromosDTO() {
		
	}

	/**
	 * @return Returns the codPromoEvento.
	 */
	public long getCodPromoEvento() {
		return codPromoEvento;
	}

	/**
	 * @param codPromoEvento The codPromoEvento to set.
	 */
	public void setCodPromoEvento(long codPromoEvento) {
		this.codPromoEvento = codPromoEvento;
	}

	/**
	 * @return Returns the codPromoNormal.
	 */
	public long getCodPromoNormal() {
		return codPromoNormal;
	}

	/**
	 * @param codPromoNormal The codPromoNormal to set.
	 */
	public void setCodPromoNormal(long codPromoNormal) {
		this.codPromoNormal = codPromoNormal;
	}

	/**
	 * @return Returns the codPromoPeriodica.
	 */
	public long getCodPromoPeriodica() {
		return codPromoPeriodica;
	}

	/**
	 * @param codPromoPeriodica The codPromoPeriodica to set.
	 */
	public void setCodPromoPeriodica(long codPromoPeriodica) {
		this.codPromoPeriodica = codPromoPeriodica;
	}
	
	

}
