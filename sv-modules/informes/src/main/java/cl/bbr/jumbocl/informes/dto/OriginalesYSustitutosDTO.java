package cl.bbr.jumbocl.informes.dto;

import java.io.Serializable;

/**
 * DTO para datos de los productos del carro de compras y sus categorías. 
 * 
 * @author imoyano
 *
 */
public class OriginalesYSustitutosDTO implements Serializable {

	private String codProdOriginal;
	private String uniMedProdOriginal;
	private String codProdSustituto;
	private String uniMedProdSustituto;
	
	
    /**
     * @return Devuelve codProdOriginal.
     */
    public String getCodProdOriginal() {
        return codProdOriginal;
    }
    /**
     * @return Devuelve codProdSustituto.
     */
    public String getCodProdSustituto() {
        return codProdSustituto;
    }
    /**
     * @return Devuelve uniMedProdOriginal.
     */
    public String getUniMedProdOriginal() {
        return uniMedProdOriginal;
    }
    /**
     * @return Devuelve uniMedProdSustituto.
     */
    public String getUniMedProdSustituto() {
        return uniMedProdSustituto;
    }
    /**
     * @param codProdOriginal El codProdOriginal a establecer.
     */
    public void setCodProdOriginal(String codProdOriginal) {
        this.codProdOriginal = codProdOriginal;
    }
    /**
     * @param codProdSustituto El codProdSustituto a establecer.
     */
    public void setCodProdSustituto(String codProdSustituto) {
        this.codProdSustituto = codProdSustituto;
    }
    /**
     * @param uniMedProdOriginal El uniMedProdOriginal a establecer.
     */
    public void setUniMedProdOriginal(String uniMedProdOriginal) {
        this.uniMedProdOriginal = uniMedProdOriginal;
    }
    /**
     * @param uniMedProdSustituto El uniMedProdSustituto a establecer.
     */
    public void setUniMedProdSustituto(String uniMedProdSustituto) {
        this.uniMedProdSustituto = uniMedProdSustituto;
    }
}