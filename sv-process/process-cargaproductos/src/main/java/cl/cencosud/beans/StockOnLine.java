//package cl.cencosud.beans;
//
//public class StockOnLine {
//   private String  idStockOnline;
//   private int     idLocal;
//   private String  idCatProd;   
//   private String  idGerencia;
//   private String  idSeccion;   
//   private String  idRubro;
//   private String  idSubRubro;
//   private String  idCatProdPadre;
//   private int     idProducto;
//   private String  skuProducto;
//   private String  nombreSeccion;
//   private String  nombreRubro;
//   private String  nombreSubRubro;
//   private String  nombreProducto;
//   private int     catNivel;
//   private int     estado;
//   private int     stockMinimo;
//   private int     modo;
//   private String  nombreGerencia;
//   private String  nombreLocal;
//   private String publicadosConStock;
//   private String publicadosSinStock;
//   private String despublicados;
//   private String cambioConStock;
//   private String cambioSinStock;
//   private String maestra;
//   private int stockReal;
//   private String estadoInicial;
//   private String estadoFinal;
//   private String resultadoTransaccion;
//   private boolean publicarConStock; 
//   private boolean publicarSinStock; 
//
//   
//	   
//public boolean isPublicarSinStock() {
//	return publicarSinStock;
//}
//public void setPublicarSinStock(boolean publicarSinStock) {
//	this.publicarSinStock = publicarSinStock;
//}
//public boolean isPublicarConStock() {
//	return publicarConStock;
//}
//public void setPublicarConStock(boolean publicarConStock) {
//	this.publicarConStock = publicarConStock;
//}
//public String getEstadoInicial() {
//	return estadoInicial;
//}
//public void setEstadoInicial(String estadoInicial) {
//	this.estadoInicial = estadoInicial;
//}
//public String getEstadoFinal() {
//	return estadoFinal;
//}
//public void setEstadoFinal(String estadoFinal) {
//	this.estadoFinal = estadoFinal;
//}
//public String getResultadoTransaccion() {
//	return resultadoTransaccion;
//}
//public void setResultadoTransaccion(String resultadoTransaccion) {
//	this.resultadoTransaccion = resultadoTransaccion;
//}
//public int getStockReal() {
//	return stockReal;
//}
//public void setStockReal(int stockReal) {
//	this.stockReal = stockReal;
//}
//public String getPublicadosConStock() {
//	return publicadosConStock;
//}
//public void setPublicadosConStock(String publicadosConStock) {
//	this.publicadosConStock = publicadosConStock;
//}
//public String getPublicadosSinStock() {
//	return publicadosSinStock;
//}
//public void setPublicadosSinStock(String publicadosSinStock) {
//	this.publicadosSinStock = publicadosSinStock;
//}
//public String getDespublicados() {
//	return despublicados;
//}
//public void setDespublicados(String despublicados) {
//	this.despublicados = despublicados;
//}
//public String getCambioConStock() {
//	return cambioConStock;
//}
//public void setCambioConStock(String cambioConStock) {
//	this.cambioConStock = cambioConStock;
//}
//public String getCambioSinStock() {
//	return cambioSinStock;
//}
//public void setCambioSinStock(String cambioSinStock) {
//	this.cambioSinStock = cambioSinStock;
//}
//
//public String getMaestra() {
//	return maestra;
//}
//public void setMaestra(String maestra) {
//	this.maestra = maestra;
//}
//	public String getIdCatProd() {
//		return idCatProd;
//	}
//	public void setIdCatProd(String idCatProd) {
//		this.idCatProd = idCatProd;
//	}
//	   
//	public int getCatNivel() {
//		return catNivel;
//	}
//	public void setCatNivel(int catNivel) {
//		this.catNivel = catNivel;
//	}   
//	public String getIdCatProdPadre() {
//		return idCatProdPadre;
//	}
//	public void setIdCatProdPadre(String idCatProdPadre) {
//		this.idCatProdPadre = idCatProdPadre;
//	}   
//	public String getIdSeccion() {
//		return idSeccion;
//	}
//	public void setIdSeccion(String idSeccion) {
//		this.idSeccion = idSeccion;
//	}
//	public String getIdRubro() {
//		return idRubro;
//	}
//	public void setIdRubro(String idRubro) {
//		this.idRubro = idRubro;
//	}
//	public String getIdSubRubro() {
//		return idSubRubro;
//	}
//	public void setIdSubRubro(String idSubRubro) {
//		this.idSubRubro = idSubRubro;
//	}
//	public String getSkuProducto() {
//		return skuProducto;
//	}
//	public void setSkuProducto(String skuProducto) {
//		this.skuProducto = skuProducto;
//	}
//	public String getNombreSeccion() {
//		return nombreSeccion;
//	}
//	public void setNombreSeccion(String nombreSeccion) {
//		this.nombreSeccion = nombreSeccion;
//	}
//	public String getNombreRubro() {
//		return nombreRubro;
//	}
//	public void setNombreRubro(String nombreRubro) {
//		this.nombreRubro = nombreRubro;
//	}
//	public String getNombreSubRubro() {
//		return nombreSubRubro;
//	}
//	public void setNombreSubRubro(String nombreSubRubro) {
//		this.nombreSubRubro = nombreSubRubro;
//	}
//	public String getNombreProducto() {
//		return nombreProducto;
//	}
//	public void setNombreProducto(String nombreProducto) {
//		this.nombreProducto = nombreProducto;
//	}
//	   
//	public String getIdGerencia() {
//		return idGerencia;
//	}
//	public void setIdGerencia(String idGerencia) {
//		this.idGerencia = idGerencia;
//	}
//	public String getNombreGerencia() {
//		return nombreGerencia;
//	}
//	public void setNombreGerencia(String nombreGerencia) {
//		this.nombreGerencia = nombreGerencia;
//	}
//	public int getEstado() {
//		return estado;
//	}
//	public void setEstado(int estado) {
//		this.estado = estado;
//	}
//	public int getStockMinimo() {
//		return stockMinimo;
//	}
//	public void setStockMinimo(int stockMinimo) {
//		this.stockMinimo = stockMinimo;
//	}
//	public int getModo() {
//		return modo;
//	}
//	public void setModo(int modo) {
//		this.modo = modo;
//	}
//	public String getNombreLocal() {
//		return nombreLocal;
//	}
//	public void setNombreLocal(String nombreLocal) {
//		this.nombreLocal = nombreLocal;
//	}
//	public String getIdStockOnline() {
//		return idStockOnline;
//	}
//	public void setIdStockOnline(String idStockOnline) {
//		this.idStockOnline = idStockOnline;
//	}
//	public int getIdLocal() {
//		return idLocal;
//	}
//	public void setIdLocal(int idLocal) {
//		this.idLocal = idLocal;
//	}
//	public int getIdProducto() {
//		return idProducto;
//	}
//	public void setIdProducto(int idProducto) {
//		this.idProducto = idProducto;
//	}
//	
//	
//
//
//}
