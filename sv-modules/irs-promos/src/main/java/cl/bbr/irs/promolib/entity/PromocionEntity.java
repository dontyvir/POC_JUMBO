/**
 * PromocionEntity.java
 * Creado   : 26-feb-2007
 * Historia : 26-feb-2007 Version 1.0
 * Historia : 17-jun-2007 version 1.1
 * 			: Se generó un constructor a partir de campos
 * 			: Se cambió Timestamp por Date para manejar fechas
 * Version  : 1.1
 * BBR
 */
package cl.bbr.irs.promolib.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author JORGE SILVA
 *
 */
public class PromocionEntity implements Serializable {

	private int id_promocion;
	private int codigo;
	private int local;
	private int version;
	private int tipo;
	private Date fechaInicio;
	private Date fechaTermino;
	private String descripcion;
	private int minCantidad;
	private long minMonto;
	private long tramo1Monto;
	private double tramo1Dcto;
	private long tramo2Monto;
	private double tramo2Dcto;
	private long tramo3Monto;
	private double tramo3Dcto;
	private long tramo4Monto;
	private double tramo4Dcto;
	private long tramo5Monto;
	private double tramo5Dcto;
	private int benef1FormaPago;
	private int benef1NroCuotas;
	private int benef1TCP;
	private double benef1Monto;
	private int benef2FormaPago;
	private int benef2NroCuotas;
	private int benef2TCP;
	private double benef2Monto;
	private int benef3FormaPago;
	private int benef3NroCuotas;
	private int benef3TCP;
	private double benef3Monto;
	private int condicion1;
	private int condicion2;
	private int condicion3;
	private int flagProrrateo;
	private int flagRecuperable;
	private int canal;
	
	private int lleve;
	private int pague;
	
	private ArrayList secciones;
	
	/**
	 * <b>Description: </b>
	 */
	public PromocionEntity() {
		this.id_promocion = 0;
		this.codigo = 0;
		this.local = 0;
		this.version = 0;
		this.tipo = 0;
		this.fechaInicio = null;
		this.fechaTermino = null;
		this.descripcion = null;
		this.minCantidad = 0;
		this.minMonto = 0;
		this.tramo1Monto = 0;
		this.tramo1Dcto = 0;
		this.tramo2Monto = 0;
		this.tramo2Dcto = 0;
		this.tramo3Monto = 0;
		this.tramo3Dcto = 0;
		this.tramo4Monto = 0;
		this.tramo4Dcto = 0;
		this.tramo5Monto = 0;
		this.tramo5Dcto = 0;
		this.benef1FormaPago = 0;
		this.benef1NroCuotas = 0;
		this.benef1TCP = 0;
		this.benef1Monto = 0;
		this.benef2FormaPago = 0;
		this.benef2NroCuotas = 0;
		this.benef2TCP = 0;
		this.benef2Monto = 0;
		this.benef3FormaPago = 0;
		this.benef3NroCuotas = 0;
		this.benef3TCP = 0;
		this.benef3Monto = 0;
		this.condicion1 = 0;
		this.condicion2 = 0;
		this.condicion3 = 0;
		this.flagProrrateo = 0;
		this.flagRecuperable = 0;
		this.canal = 0;
		this.lleve=0;
		this.pague=0;
		this.secciones = new ArrayList();
	}

	/**
	 * @return Returns the benef1FormaPago.
	 */
	public int getBenef1FormaPago() {
		return benef1FormaPago;
	}

	/**
	 * @param benef1FormaPago The benef1FormaPago to set.
	 */
	public void setBenef1FormaPago(int benef1FormaPago) {
		this.benef1FormaPago = benef1FormaPago;
	}

	/**
	 * @return Returns the benef1Monto.
	 */
	public double getBenef1Monto() {
		return benef1Monto;
	}

	/**
	 * @param benef1Monto The benef1Monto to set.
	 */
	public void setBenef1Monto(double benef1Monto) {
		this.benef1Monto = benef1Monto;
	}

	/**
	 * @return Returns the benef1NroCuotas.
	 */
	public int getBenef1NroCuotas() {
		return benef1NroCuotas;
	}

	/**
	 * @param benef1NroCuotas The benef1NroCuotas to set.
	 */
	public void setBenef1NroCuotas(int benef1NroCuotas) {
		this.benef1NroCuotas = benef1NroCuotas;
	}

	/**
	 * @return Returns the benef1TCP.
	 */
	public int getBenef1TCP() {
		return benef1TCP;
	}

	/**
	 * @param benef1TCP The benef1TCP to set.
	 */
	public void setBenef1TCP(int benef1TCP) {
		this.benef1TCP = benef1TCP;
	}

	/**
	 * @return Returns the benef2FormaPago.
	 */
	public int getBenef2FormaPago() {
		return benef2FormaPago;
	}

	/**
	 * @param benef2FormaPago The benef2FormaPago to set.
	 */
	public void setBenef2FormaPago(int benef2FormaPago) {
		this.benef2FormaPago = benef2FormaPago;
	}

	/**
	 * @return Returns the benef2Monto.
	 */
	public double getBenef2Monto() {
		return benef2Monto;
	}

	/**
	 * @param benef2Monto The benef2Monto to set.
	 */
	public void setBenef2Monto(double benef2Monto) {
		this.benef2Monto = benef2Monto;
	}

	/**
	 * @return Returns the benef2NroCuotas.
	 */
	public int getBenef2NroCuotas() {
		return benef2NroCuotas;
	}

	/**
	 * @param benef2NroCuotas The benef2NroCuotas to set.
	 */
	public void setBenef2NroCuotas(int benef2NroCuotas) {
		this.benef2NroCuotas = benef2NroCuotas;
	}

	/**
	 * @return Returns the benef2TCP.
	 */
	public int getBenef2TCP() {
		return benef2TCP;
	}

	/**
	 * @param benef2TCP The benef2TCP to set.
	 */
	public void setBenef2TCP(int benef2TCP) {
		this.benef2TCP = benef2TCP;
	}

	/**
	 * @return Returns the benef3FormaPago.
	 */
	public int getBenef3FormaPago() {
		return benef3FormaPago;
	}

	/**
	 * @param benef3FormaPago The benef3FormaPago to set.
	 */
	public void setBenef3FormaPago(int benef3FormaPago) {
		this.benef3FormaPago = benef3FormaPago;
	}

	/**
	 * @return Returns the benef3Monto.
	 */
	public double getBenef3Monto() {
		return benef3Monto;
	}

	/**
	 * @param benef3Monto The benef3Monto to set.
	 */
	public void setBenef3Monto(double benef3Monto) {
		this.benef3Monto = benef3Monto;
	}

	/**
	 * @return Returns the benef3NroCuotas.
	 */
	public int getBenef3NroCuotas() {
		return benef3NroCuotas;
	}

	/**
	 * @param benef3NroCuotas The benef3NroCuotas to set.
	 */
	public void setBenef3NroCuotas(int benef3NroCuotas) {
		this.benef3NroCuotas = benef3NroCuotas;
	}

	/**
	 * @return Returns the benef3TCP.
	 */
	public int getBenef3TCP() {
		return benef3TCP;
	}

	/**
	 * @param benef3TCP The benef3TCP to set.
	 */
	public void setBenef3TCP(int benef3TCP) {
		this.benef3TCP = benef3TCP;
	}

	/**
	 * @return Returns the canal.
	 */
	public int getCanal() {
		return canal;
	}

	/**
	 * @param canal The canal to set.
	 */
	public void setCanal(int canal) {
		this.canal = canal;
	}

	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return Returns the condicion1.
	 */
	public int getCondicion1() {
		return condicion1;
	}

	/**
	 * @param condicion1 The condicion1 to set.
	 */
	public void setCondicion1(int condicion1) {
		this.condicion1 = condicion1;
	}

	/**
	 * @return Returns the condicion2.
	 */
	public int getCondicion2() {
		return condicion2;
	}

	/**
	 * @param condicion2 The condicion2 to set.
	 */
	public void setCondicion2(int condicion2) {
		this.condicion2 = condicion2;
	}

	/**
	 * @return Returns the condicion3.
	 */
	public int getCondicion3() {
		return condicion3;
	}

	/**
	 * @param condicion3 The condicion3 to set.
	 */
	public void setCondicion3(int condicion3) {
		this.condicion3 = condicion3;
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return Returns the fechaInicio.
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio The fechaInicio to set.
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return Returns the fechaTermino.
	 */
	public Date getFechaTermino() {
		return fechaTermino;
	}

	/**
	 * @param fechaTermino The fechaTermino to set.
	 */
	public void setFechaTermino(Date fechaTermino) {
		this.fechaTermino = fechaTermino;
	}

	/**
	 * @return Returns the flagProrrateo.
	 */
	public int getFlagProrrateo() {
		return flagProrrateo;
	}

	/**
	 * @param flagProrrateo The flagProrrateo to set.
	 */
	public void setFlagProrrateo(int flagProrrateo) {
		this.flagProrrateo = flagProrrateo;
	}

	/**
	 * @return Returns the flagRecuperable.
	 */
	public int getFlagRecuperable() {
		return flagRecuperable;
	}

	/**
	 * @param flagRecuperable The flagRecuperable to set.
	 */
	public void setFlagRecuperable(int flagRecuperable) {
		this.flagRecuperable = flagRecuperable;
	}

	/**
	 * @return Returns the id_promocion.
	 */
	public int getId_promocion() {
		return id_promocion;
	}

	/**
	 * @param id_promocion The id_promocion to set.
	 */
	public void setId_promocion(int id_promocion) {
		this.id_promocion = id_promocion;
	}

	/**
	 * @return Returns the local.
	 */
	public int getLocal() {
		return local;
	}

	/**
	 * @param local The local to set.
	 */
	public void setLocal(int local) {
		this.local = local;
	}

	/**
	 * @return Returns the minCantidad.
	 */
	public int getMinCantidad() {
		return minCantidad;
	}

	/**
	 * @param minCantidad The minCantidad to set.
	 */
	public void setMinCantidad(int minCantidad) {
		this.minCantidad = minCantidad;
	}

	/**
	 * @return Returns the minMonto.
	 */
	public long getMinMonto() {
		return minMonto;
	}

	/**
	 * @param minMonto The minMonto to set.
	 */
	public void setMinMonto(long minMonto) {
		this.minMonto = minMonto;
	}

	/**
	 * @return Returns the tipo.
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return Returns the tramo1Dcto.
	 */
	public double getTramo1Dcto() {
		return tramo1Dcto;
	}

	/**
	 * @param tramo1Dcto The tramo1Dcto to set.
	 */
	public void setTramo1Dcto(double tramo1Dcto) {
		this.tramo1Dcto = tramo1Dcto;
	}

	/**
	 * @return Returns the tramo1Monto.
	 */
	public long getTramo1Monto() {
		return tramo1Monto;
	}

	/**
	 * @param tramo1Monto The tramo1Monto to set.
	 */
	public void setTramo1Monto(long tramo1Monto) {
		this.tramo1Monto = tramo1Monto;
	}

	/**
	 * @return Returns the tramo2Dcto.
	 */
	public double getTramo2Dcto() {
		return tramo2Dcto;
	}

	/**
	 * @param tramo2Dcto The tramo2Dcto to set.
	 */
	public void setTramo2Dcto(double tramo2Dcto) {
		this.tramo2Dcto = tramo2Dcto;
	}

	/**
	 * @return Returns the tramo2Monto.
	 */
	public long getTramo2Monto() {
		return tramo2Monto;
	}

	/**
	 * @param tramo2Monto The tramo2Monto to set.
	 */
	public void setTramo2Monto(long tramo2Monto) {
		this.tramo2Monto = tramo2Monto;
	}

	/**
	 * @return Returns the tramo3Dcto.
	 */
	public double getTramo3Dcto() {
		return tramo3Dcto;
	}

	/**
	 * @param tramo3Dcto The tramo3Dcto to set.
	 */
	public void setTramo3Dcto(double tramo3Dcto) {
		this.tramo3Dcto = tramo3Dcto;
	}

	/**
	 * @return Returns the tramo3Monto.
	 */
	public long getTramo3Monto() {
		return tramo3Monto;
	}

	/**
	 * @param tramo3Monto The tramo3Monto to set.
	 */
	public void setTramo3Monto(long tramo3Monto) {
		this.tramo3Monto = tramo3Monto;
	}

	/**
	 * @return Returns the tramo4Dcto.
	 */
	public double getTramo4Dcto() {
		return tramo4Dcto;
	}

	/**
	 * @param tramo4Dcto The tramo4Dcto to set.
	 */
	public void setTramo4Dcto(double tramo4Dcto) {
		this.tramo4Dcto = tramo4Dcto;
	}

	/**
	 * @return Returns the tramo4Monto.
	 */
	public long getTramo4Monto() {
		return tramo4Monto;
	}

	/**
	 * @param tramo4Monto The tramo4Monto to set.
	 */
	public void setTramo4Monto(long tramo4Monto) {
		this.tramo4Monto = tramo4Monto;
	}

	/**
	 * @return Returns the tramo5Dcto.
	 */
	public double getTramo5Dcto() {
		return tramo5Dcto;
	}

	/**
	 * @param tramo5Dcto The tramo5Dcto to set.
	 */
	public void setTramo5Dcto(double tramo5Dcto) {
		this.tramo5Dcto = tramo5Dcto;
	}

	/**
	 * @return Returns the tramo5Monto.
	 */
	public long getTramo5Monto() {
		return tramo5Monto;
	}

	/**
	 * @param tramo5Monto The tramo5Monto to set.
	 */
	public void setTramo5Monto(long tramo5Monto) {
		this.tramo5Monto = tramo5Monto;
	}

	/**
	 * @return Returns the version.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	public int getLleve() {
		return lleve;
	}

	public void setLleve(int lleve) {
		this.lleve = lleve;
	}

	public int getPague() {
		return pague;
	}

	public void setPague(int pague) {
		this.pague = pague;
	}
	
	public ArrayList getSecciones() {
		return secciones;
	}

	public void setSecciones(ArrayList secciones) {
		this.secciones = secciones;
	}
	
}
