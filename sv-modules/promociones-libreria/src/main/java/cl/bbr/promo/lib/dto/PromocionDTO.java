package cl.bbr.promo.lib.dto;

import java.io.Serializable;

/**
 * DTO para datos de las promociones. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class PromocionDTO implements Serializable {

	private long id_promocion;
	private long cod_promo;
	private long id_local;
	private long version;
	private long tipo_promo;
	private long fini;
	private long ffin;
	private String descr;
	private long cant_min;
	private long monto_min;
	private long monto1;
	private double descuento1;
	private long monto2;
	private double descuento2;
	private long monto3;
	private double descuento3;
	private long monto4;
	private double descuento4;
	private long monto5;
	private double descuento5;
	private long fp1;
	private long num_cuota1;
	private long tcp1;
	private double beneficio1;
	private long fp2;
	private long num_cuota2;
	private long tcp2;	
	private double beneficio2;
	private long fp3;
	private long num_cuota3;
	private long tcp3;	
	private double beneficio3;	
	private long condicion1;
	private long condicion2;
	private long condicion3;
	private double prorrateo;
	private long recuperable;
	private long canal;
	private String sustituible;
	private String faltante;
	private String banner;
	private int cantProdPromo;
	private String colorBann;
	
	
	/**
	 * @return Devuelve banner.
	 */
	public String getBanner() {
		return banner;
	}
	/**
	 * @param banner El banner a establecer.
	 */
	public void setBanner(String banner) {
		this.banner = banner;
	}
	/**
	 * @return Devuelve beneficio1.
	 */
	public double getBeneficio1() {
		return beneficio1;
	}
	/**
	 * @param beneficio1 El beneficio1 a establecer.
	 */
	public void setBeneficio1(double beneficio1) {
		this.beneficio1 = beneficio1;
	}
	/**
	 * @return Devuelve beneficio2.
	 */
	public double getBeneficio2() {
		return beneficio2;
	}
	/**
	 * @param beneficio2 El beneficio2 a establecer.
	 */
	public void setBeneficio2(double beneficio2) {
		this.beneficio2 = beneficio2;
	}
	/**
	 * @return Devuelve beneficio3.
	 */
	public double getBeneficio3() {
		return beneficio3;
	}
	/**
	 * @param beneficio3 El beneficio3 a establecer.
	 */
	public void setBeneficio3(double beneficio3) {
		this.beneficio3 = beneficio3;
	}
	/**
	 * @return Devuelve canal.
	 */
	public long getCanal() {
		return canal;
	}
	/**
	 * @param canal El canal a establecer.
	 */
	public void setCanal(long canal) {
		this.canal = canal;
	}
	/**
	 * @return Devuelve cant_min.
	 */
	public long getCant_min() {
		return cant_min;
	}
	/**
	 * @param cant_min El cant_min a establecer.
	 */
	public void setCant_min(long cant_min) {
		this.cant_min = cant_min;
	}
	/**
	 * @return Devuelve cantProdPromo.
	 */
	public int getCantProdPromo() {
		return cantProdPromo;
	}
	/**
	 * @param cantProdPromo El cantProdPromo a establecer.
	 */
	public void setCantProdPromo(int cantProdPromo) {
		this.cantProdPromo = cantProdPromo;
	}	
	/**
	 * @return Devuelve cod_promo.
	 */
	public long getCod_promo() {
		return cod_promo;
	}
	/**
	 * @param cod_promo El cod_promo a establecer.
	 */
	public void setCod_promo(long cod_promo) {
		this.cod_promo = cod_promo;
	}
	/**
	 * @return Devuelve condicion1.
	 */
	public long getCondicion1() {
		return condicion1;
	}
	/**
	 * @param condicion1 El condicion1 a establecer.
	 */
	public void setCondicion1(long condicion1) {
		this.condicion1 = condicion1;
	}
	/**
	 * @return Devuelve condicion2.
	 */
	public long getCondicion2() {
		return condicion2;
	}
	/**
	 * @param condicion2 El condicion2 a establecer.
	 */
	public void setCondicion2(long condicion2) {
		this.condicion2 = condicion2;
	}
	/**
	 * @return Devuelve condicion3.
	 */
	public long getCondicion3() {
		return condicion3;
	}
	/**
	 * @param condicion3 El condicion3 a establecer.
	 */
	public void setCondicion3(long condicion3) {
		this.condicion3 = condicion3;
	}
	/**
	 * @return Devuelve descr.
	 */
	public String getDescr() {
		return descr;
	}
	/**
	 * @param descr El descr a establecer.
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}
	/**
	 * @return Devuelve descuento1.
	 */
	public double getDescuento1() {
		return descuento1;
	}
	/**
	 * @param descuento1 El descuento1 a establecer.
	 */
	public void setDescuento1(double descuento1) {
		this.descuento1 = descuento1;
	}
	/**
	 * @return Devuelve descuento2.
	 */
	public double getDescuento2() {
		return descuento2;
	}
	/**
	 * @param descuento2 El descuento2 a establecer.
	 */
	public void setDescuento2(double descuento2) {
		this.descuento2 = descuento2;
	}
	/**
	 * @return Devuelve descuento3.
	 */
	public double getDescuento3() {
		return descuento3;
	}
	/**
	 * @param descuento3 El descuento3 a establecer.
	 */
	public void setDescuento3(double descuento3) {
		this.descuento3 = descuento3;
	}
	/**
	 * @return Devuelve descuento4.
	 */
	public double getDescuento4() {
		return descuento4;
	}
	/**
	 * @param descuento4 El descuento4 a establecer.
	 */
	public void setDescuento4(double descuento4) {
		this.descuento4 = descuento4;
	}
	/**
	 * @return Devuelve descuento5.
	 */
	public double getDescuento5() {
		return descuento5;
	}
	/**
	 * @param descuento5 El descuento5 a establecer.
	 */
	public void setDescuento5(double descuento5) {
		this.descuento5 = descuento5;
	}
	/**
	 * @return Devuelve ffin.
	 */
	public long getFfin() {
		return ffin;
	}
	/**
	 * @param ffin El ffin a establecer.
	 */
	public void setFfin(long ffin) {
		this.ffin = ffin;
	}
	/**
	 * @return Devuelve fini.
	 */
	public long getFini() {
		return fini;
	}
	/**
	 * @param fini El fini a establecer.
	 */
	public void setFini(long fini) {
		this.fini = fini;
	}
	/**
	 * @return Devuelve fp1.
	 */
	public long getFp1() {
		return fp1;
	}
	/**
	 * @param fp1 El fp1 a establecer.
	 */
	public void setFp1(long fp1) {
		this.fp1 = fp1;
	}
	/**
	 * @return Devuelve fp2.
	 */
	public long getFp2() {
		return fp2;
	}
	/**
	 * @param fp2 El fp2 a establecer.
	 */
	public void setFp2(long fp2) {
		this.fp2 = fp2;
	}
	/**
	 * @return Devuelve fp3.
	 */
	public long getFp3() {
		return fp3;
	}
	/**
	 * @param fp3 El fp3 a establecer.
	 */
	public void setFp3(long fp3) {
		this.fp3 = fp3;
	}
	/**
	 * @return Devuelve id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local El id_local a establecer.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Devuelve id_promocion.
	 */
	public long getId_promocion() {
		return id_promocion;
	}
	/**
	 * @param id_promocion El id_promocion a establecer.
	 */
	public void setId_promocion(long id_promocion) {
		this.id_promocion = id_promocion;
	}
	/**
	 * @return Devuelve monto_min.
	 */
	public long getMonto_min() {
		return monto_min;
	}
	/**
	 * @param monto_min El monto_min a establecer.
	 */
	public void setMonto_min(long monto_min) {
		this.monto_min = monto_min;
	}
	/**
	 * @return Devuelve monto1.
	 */
	public long getMonto1() {
		return monto1;
	}
	/**
	 * @param monto1 El monto1 a establecer.
	 */
	public void setMonto1(long monto1) {
		this.monto1 = monto1;
	}
	/**
	 * @return Devuelve monto2.
	 */
	public long getMonto2() {
		return monto2;
	}
	/**
	 * @param monto2 El monto2 a establecer.
	 */
	public void setMonto2(long monto2) {
		this.monto2 = monto2;
	}
	/**
	 * @return Devuelve monto3.
	 */
	public long getMonto3() {
		return monto3;
	}
	/**
	 * @param monto3 El monto3 a establecer.
	 */
	public void setMonto3(long monto3) {
		this.monto3 = monto3;
	}
	/**
	 * @return Devuelve monto4.
	 */
	public long getMonto4() {
		return monto4;
	}
	/**
	 * @param monto4 El monto4 a establecer.
	 */
	public void setMonto4(long monto4) {
		this.monto4 = monto4;
	}
	/**
	 * @return Devuelve monto5.
	 */
	public long getMonto5() {
		return monto5;
	}
	/**
	 * @param monto5 El monto5 a establecer.
	 */
	public void setMonto5(long monto5) {
		this.monto5 = monto5;
	}
	/**
	 * @return Devuelve num_cuota1.
	 */
	public long getNum_cuota1() {
		return num_cuota1;
	}
	/**
	 * @param num_cuota1 El num_cuota1 a establecer.
	 */
	public void setNum_cuota1(long num_cuota1) {
		this.num_cuota1 = num_cuota1;
	}
	/**
	 * @return Devuelve num_cuota2.
	 */
	public long getNum_cuota2() {
		return num_cuota2;
	}
	/**
	 * @param num_cuota2 El num_cuota2 a establecer.
	 */
	public void setNum_cuota2(long num_cuota2) {
		this.num_cuota2 = num_cuota2;
	}
	/**
	 * @return Devuelve num_cuota3.
	 */
	public long getNum_cuota3() {
		return num_cuota3;
	}
	/**
	 * @param num_cuota3 El num_cuota3 a establecer.
	 */
	public void setNum_cuota3(long num_cuota3) {
		this.num_cuota3 = num_cuota3;
	}
	/**
	 * @return Devuelve prorrateo.
	 */
	public double getProrrateo() {
		return prorrateo;
	}
	/**
	 * @param prorrateo El prorrateo a establecer.
	 */
	public void setProrrateo(double prorrateo) {
		this.prorrateo = prorrateo;
	}
	/**
	 * @return Devuelve recuperable.
	 */
	public long getRecuperable() {
		return recuperable;
	}
	/**
	 * @param recuperable El recuperable a establecer.
	 */
	public void setRecuperable(long recuperable) {
		this.recuperable = recuperable;
	}
	/**
	 * @return Devuelve sustituible.
	 */
	public String getSustituible() {
		return sustituible;
	}
	/**
	 * @param sustituible El sustituible a establecer.
	 */
	public void setSustituible(String sustituible) {
		this.sustituible = sustituible;
	}
	/**
	 * @return Devuelve tcp1.
	 */
	public long getTcp1() {
		return tcp1;
	}
	/**
	 * @param tcp1 El tcp1 a establecer.
	 */
	public void setTcp1(long tcp1) {
		this.tcp1 = tcp1;
	}
	/**
	 * @return Devuelve tcp2.
	 */
	public long getTcp2() {
		return tcp2;
	}
	/**
	 * @param tcp2 El tcp2 a establecer.
	 */
	public void setTcp2(long tcp2) {
		this.tcp2 = tcp2;
	}
	/**
	 * @return Devuelve tcp3.
	 */
	public long getTcp3() {
		return tcp3;
	}
	/**
	 * @param tcp3 El tcp3 a establecer.
	 */
	public void setTcp3(long tcp3) {
		this.tcp3 = tcp3;
	}
	/**
	 * @return Devuelve tipo_promo.
	 */
	public long getTipo_promo() {
		return tipo_promo;
	}
	/**
	 * @param tipo_promo El tipo_promo a establecer.
	 */
	public void setTipo_promo(long tipo_promo) {
		this.tipo_promo = tipo_promo;
	}
	/**
	 * @return Devuelve version.
	 */
	public long getVersion() {
		return version;
	}
	/**
	 * @param version El version a establecer.
	 */
	public void setVersion(long version) {
		this.version = version;
	}
	
	public String getColorBann() {
		return colorBann;
	}
	/**
	 * @param version El version a establecer.
	 */
	public void setColorBann(String colorBann) {
		this.colorBann = colorBann;
	}
}