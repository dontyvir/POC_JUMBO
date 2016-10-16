package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos del log de las empresas 
 * 
 * @author imoyano
 *
 */
public class EmpresaLogDTO implements Serializable {

	private long   	idEmpresa;
	private long   	idUsuario;
	private double 	saldoOld;
    private double  saldoNew;
	
	/**
	 * Constructor
	 */
	public EmpresaLogDTO() {
	}
	

	
    /**
     * @return Devuelve idEmpresa.
     */
    public long getIdEmpresa() {
        return idEmpresa;
    }
    /**
     * @return Devuelve idUsuario.
     */
    public long getIdUsuario() {
        return idUsuario;
    }
    /**
     * @return Devuelve saldoNew.
     */
    public double getSaldoNew() {
        return saldoNew;
    }
    /**
     * @return Devuelve saldoOld.
     */
    public double getSaldoOld() {
        return saldoOld;
    }
    /**
     * @param idEmpresa El idEmpresa a establecer.
     */
    public void setIdEmpresa(long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    /**
     * @param idUsuario El idUsuario a establecer.
     */
    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }
    /**
     * @param saldoNew El saldoNew a establecer.
     */
    public void setSaldoNew(double saldoNew) {
        this.saldoNew = saldoNew;
    }
    /**
     * @param saldoOld El saldoOld a establecer.
     */
    public void setSaldoOld(double saldoOld) {
        this.saldoOld = saldoOld;
    }
}