package cl.bbr.jumbocl.pedidos.promos.dto;

public class PromocionesCriteriaDTO {
	long id_local;
	long cod_promo;
	int		pag;
	int		regsperpag;
	
	/**
	 * Constructor
	 */
	public PromocionesCriteriaDTO() {
	
	}

	/**
	 * @return Returns the id_local.
	 */
	public long getId_local() {
		return id_local;
	}

	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	/**
	 * @return Returns the cod_promo.
	 */
	public long getCod_promo() {
		return cod_promo;
	}

	/**
	 * @param cod_promo The cod_promo to set.
	 */
	public void setCod_promo(long cod_promo) {
		this.cod_promo = cod_promo;
	}

	/**
	 * @return Returns the pag.
	 */
	public int getPag() {
		return pag;
	}

	/**
	 * @param pag The pag to set.
	 */
	public void setPag(int pag) {
		this.pag = pag;
	}

	/**
	 * @return Returns the regsperpag.
	 */
	public int getRegsperpag() {
		return regsperpag;
	}

	/**
	 * @param regsperpag The regsperpag to set.
	 */
	public void setRegsperpag(int regsperpag) {
		this.regsperpag = regsperpag;
	}
	
	
	
	
	

}
