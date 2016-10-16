package cl.bbr.jumbocl.pedidos.promos.dto;



public class PromocionDTO {
	private long	id_promocion;
	private long	cod_promo;
	private long	id_local;
	private long	version;
	private long	tipo_promo;
	private String	fini;
	private String	ffin;
	private String	descr;
	private long	cant_min;
	private long	monto_min;
	private long	monto1;
	private double	descuento1;
	private long	monto2;
	private double	descuento2;
	private long	monto3;
	private double	descuento3;
	private long	monto4;
	private double	descuento4;
	private long	monto5;
	private double	descuento5;
	private long	fp1;
	private long	num_cuota1;
	private long	tcp1;
	private double	beneficio1;
	private long	fp2;
	private long	num_cuota2;
	private long	tcp2;
	private double	beneficio2;
	private long	fp3;
	private double	num_cuota3;
	private long	tcp3;
	private double	beneficio3;
	private long	condicion1;
	private long	condicion2;
	private long	condicion3;
	private double	prorrateo;
	private long	recuperable;
	private long	canal;
	private String	sustituible;
	private String	banner;
	private String	faltante;
	
	private String nom_local;
	
	public PromocionDTO() {
		
	}

	/**
	 * @return Returns the banner.
	 */
	public String getBanner() {
		return banner;
	}

	/**
	 * @param banner The banner to set.
	 */
	public void setBanner(String banner) {
		this.banner = banner;
	}

	/**
	 * @return Returns the beneficio1.
	 */
	public double getBeneficio1() {
		return beneficio1;
	}

	/**
	 * @param beneficio1 The beneficio1 to set.
	 */
	public void setBeneficio1(double beneficio1) {
		this.beneficio1 = beneficio1;
	}

	/**
	 * @return Returns the beneficio2.
	 */
	public double getBeneficio2() {
		return beneficio2;
	}

	/**
	 * @param beneficio2 The beneficio2 to set.
	 */
	public void setBeneficio2(double beneficio2) {
		this.beneficio2 = beneficio2;
	}

	/**
	 * @return Returns the beneficio3.
	 */
	public double getBeneficio3() {
		return beneficio3;
	}

	/**
	 * @param beneficio3 The beneficio3 to set.
	 */
	public void setBeneficio3(double beneficio3) {
		this.beneficio3 = beneficio3;
	}

	/**
	 * @return Returns the canal.
	 */
	public long getCanal() {
		return canal;
	}

	/**
	 * @param canal The canal to set.
	 */
	public void setCanal(long canal) {
		this.canal = canal;
	}

	/**
	 * @return Returns the cant_min.
	 */
	public long getCant_min() {
		return cant_min;
	}

	/**
	 * @param cant_min The cant_min to set.
	 */
	public void setCant_min(long cant_min) {
		this.cant_min = cant_min;
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
	 * @return Returns the condicion1.
	 */
	public long getCondicion1() {
		return condicion1;
	}

	/**
	 * @param condicion1 The condicion1 to set.
	 */
	public void setCondicion1(long condicion1) {
		this.condicion1 = condicion1;
	}

	/**
	 * @return Returns the condicion2.
	 */
	public long getCondicion2() {
		return condicion2;
	}

	/**
	 * @param condicion2 The condicion2 to set.
	 */
	public void setCondicion2(long condicion2) {
		this.condicion2 = condicion2;
	}

	/**
	 * @return Returns the condicion3.
	 */
	public long getCondicion3() {
		return condicion3;
	}

	/**
	 * @param condicion3 The condicion3 to set.
	 */
	public void setCondicion3(long condicion3) {
		this.condicion3 = condicion3;
	}

	/**
	 * @return Returns the descr.
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * @param descr The descr to set.
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * @return Returns the descuento1.
	 */
	public double getDescuento1() {
		return descuento1;
	}

	/**
	 * @param descuento1 The descuento1 to set.
	 */
	public void setDescuento1(double descuento1) {
		this.descuento1 = descuento1;
	}

	/**
	 * @return Returns the descuento2.
	 */
	public double getDescuento2() {
		return descuento2;
	}

	/**
	 * @param descuento2 The descuento2 to set.
	 */
	public void setDescuento2(double descuento2) {
		this.descuento2 = descuento2;
	}

	/**
	 * @return Returns the descuento3.
	 */
	public double getDescuento3() {
		return descuento3;
	}

	/**
	 * @param descuento3 The descuento3 to set.
	 */
	public void setDescuento3(double descuento3) {
		this.descuento3 = descuento3;
	}

	/**
	 * @return Returns the descuento4.
	 */
	public double getDescuento4() {
		return descuento4;
	}

	/**
	 * @param descuento4 The descuento4 to set.
	 */
	public void setDescuento4(double descuento4) {
		this.descuento4 = descuento4;
	}

	/**
	 * @return Returns the descuento5.
	 */
	public double getDescuento5() {
		return descuento5;
	}

	/**
	 * @param descuento5 The descuento5 to set.
	 */
	public void setDescuento5(double descuento5) {
		this.descuento5 = descuento5;
	}

	/**
	 * @return Returns the ffin.
	 */
	public String getFfin() {
		return ffin;
	}

	/**
	 * @param ffin The ffin to set.
	 */
	public void setFfin(String ffin) {
		this.ffin = ffin;
	}

	/**
	 * @return Returns the fini.
	 */
	public String getFini() {
		return fini;
	}

	/**
	 * @param fini The fini to set.
	 */
	public void setFini(String fini) {
		this.fini = fini;
	}

	/**
	 * @return Returns the fp1.
	 */
	public long getFp1() {
		return fp1;
	}

	/**
	 * @param fp1 The fp1 to set.
	 */
	public void setFp1(long fp1) {
		this.fp1 = fp1;
	}

	/**
	 * @return Returns the fp2.
	 */
	public long getFp2() {
		return fp2;
	}

	/**
	 * @param fp2 The fp2 to set.
	 */
	public void setFp2(long fp2) {
		this.fp2 = fp2;
	}

	/**
	 * @return Returns the fp3.
	 */
	public long getFp3() {
		return fp3;
	}

	/**
	 * @param fp3 The fp3 to set.
	 */
	public void setFp3(long fp3) {
		this.fp3 = fp3;
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
	 * @return Returns the id_promocion.
	 */
	public long getId_promocion() {
		return id_promocion;
	}

	/**
	 * @param id_promocion The id_promocion to set.
	 */
	public void setId_promocion(long id_promocion) {
		this.id_promocion = id_promocion;
	}

	/**
	 * @return Returns the monto_min.
	 */
	public long getMonto_min() {
		return monto_min;
	}

	/**
	 * @param monto_min The monto_min to set.
	 */
	public void setMonto_min(long monto_min) {
		this.monto_min = monto_min;
	}

	/**
	 * @return Returns the monto1.
	 */
	public long getMonto1() {
		return monto1;
	}

	/**
	 * @param monto1 The monto1 to set.
	 */
	public void setMonto1(long monto1) {
		this.monto1 = monto1;
	}

	/**
	 * @return Returns the monto2.
	 */
	public long getMonto2() {
		return monto2;
	}

	/**
	 * @param monto2 The monto2 to set.
	 */
	public void setMonto2(long monto2) {
		this.monto2 = monto2;
	}

	/**
	 * @return Returns the monto3.
	 */
	public long getMonto3() {
		return monto3;
	}

	/**
	 * @param monto3 The monto3 to set.
	 */
	public void setMonto3(long monto3) {
		this.monto3 = monto3;
	}

	/**
	 * @return Returns the monto4.
	 */
	public long getMonto4() {
		return monto4;
	}

	/**
	 * @param monto4 The monto4 to set.
	 */
	public void setMonto4(long monto4) {
		this.monto4 = monto4;
	}

	/**
	 * @return Returns the monto5.
	 */
	public long getMonto5() {
		return monto5;
	}

	/**
	 * @param monto5 The monto5 to set.
	 */
	public void setMonto5(long monto5) {
		this.monto5 = monto5;
	}

	/**
	 * @return Returns the nom_local.
	 */
	public String getNom_local() {
		return nom_local;
	}

	/**
	 * @param nom_local The nom_local to set.
	 */
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}

	/**
	 * @return Returns the num_cuota1.
	 */
	public long getNum_cuota1() {
		return num_cuota1;
	}

	/**
	 * @param num_cuota1 The num_cuota1 to set.
	 */
	public void setNum_cuota1(long num_cuota1) {
		this.num_cuota1 = num_cuota1;
	}

	/**
	 * @return Returns the num_cuota2.
	 */
	public long getNum_cuota2() {
		return num_cuota2;
	}

	/**
	 * @param num_cuota2 The num_cuota2 to set.
	 */
	public void setNum_cuota2(long num_cuota2) {
		this.num_cuota2 = num_cuota2;
	}

	/**
	 * @return Returns the num_cuota3.
	 */
	public double getNum_cuota3() {
		return num_cuota3;
	}

	/**
	 * @param num_cuota3 The num_cuota3 to set.
	 */
	public void setNum_cuota3(double num_cuota3) {
		this.num_cuota3 = num_cuota3;
	}

	/**
	 * @return Returns the prorrateo.
	 */
	public double getProrrateo() {
		return prorrateo;
	}

	/**
	 * @param prorrateo The prorrateo to set.
	 */
	public void setProrrateo(double prorrateo) {
		this.prorrateo = prorrateo;
	}

	/**
	 * @return Returns the recuperable.
	 */
	public long getRecuperable() {
		return recuperable;
	}

	/**
	 * @param recuperable The recuperable to set.
	 */
	public void setRecuperable(long recuperable) {
		this.recuperable = recuperable;
	}

	/**
	 * @return Returns the sustituible.
	 */
	public String getSustituible() {
		return sustituible;
	}

	/**
	 * @param sustituible The sustituible to set.
	 */
	public void setSustituible(String sustituible) {
		this.sustituible = sustituible;
	}

	/**
	 * @return Returns the tcp1.
	 */
	public long getTcp1() {
		return tcp1;
	}

	/**
	 * @param tcp1 The tcp1 to set.
	 */
	public void setTcp1(long tcp1) {
		this.tcp1 = tcp1;
	}

	/**
	 * @return Returns the tcp2.
	 */
	public long getTcp2() {
		return tcp2;
	}

	/**
	 * @param tcp2 The tcp2 to set.
	 */
	public void setTcp2(long tcp2) {
		this.tcp2 = tcp2;
	}

	/**
	 * @return Returns the tcp3.
	 */
	public long getTcp3() {
		return tcp3;
	}

	/**
	 * @param tcp3 The tcp3 to set.
	 */
	public void setTcp3(long tcp3) {
		this.tcp3 = tcp3;
	}

	/**
	 * @return Returns the tipo_promo.
	 */
	public long getTipo_promo() {
		return tipo_promo;
	}

	/**
	 * @param tipo_promo The tipo_promo to set.
	 */
	public void setTipo_promo(long tipo_promo) {
		this.tipo_promo = tipo_promo;
	}

	

	/**
	 * @return Returns the version.
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	/**
	 * @return Returns the faltante.
	 */
	public String getFaltante() {
		return faltante;
	}

	/**
	 * @param faltante The faltante to set.
	 */
	public void setFaltante(String faltante) {
		this.faltante = faltante;
	}

}
