package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.sql.Time;

public class AdmHorarioDespachoDTO implements Serializable {

	private Time 	h_ini;
	private Time	h_fin;
	private long	id_jpicking_lu;
	private long	id_jpicking_ma;
	private long	id_jpicking_mi;
	private long	id_jpicking_ju;
	private long	id_jpicking_vi;
	private long	id_jpicking_sa;
	private long	id_jpicking_do;
	private int		capac_lu;
	private int		capac_ma;
	private int		capac_mi;
	private int		capac_ju;
	private int		capac_vi;
	private int		capac_sa;
	private int		capac_do;
	
	private int		tarifa_normal_lu;
	private int		tarifa_normal_ma;	
	private int		tarifa_normal_mi;
	private int		tarifa_normal_ju;
	private int		tarifa_normal_vi;
	private int		tarifa_normal_sa;
	private int		tarifa_normal_do;

	private int		tarifa_economica_lu;
	private int		tarifa_economica_ma;	
	private int		tarifa_economica_mi;
	private int		tarifa_economica_ju;
	private int		tarifa_economica_vi;
	private int		tarifa_economica_sa;
	private int		tarifa_economica_do;

	private int		tarifa_express_lu;
	private int		tarifa_express_ma;	
	private int		tarifa_express_mi;
	private int		tarifa_express_ju;
	private int		tarifa_express_vi;
	private int		tarifa_express_sa;
	private int		tarifa_express_do;
	
	private int		tarifa_umbral_lu;
	private int		tarifa_umbral_ma;
	private int		tarifa_umbral_mi;
	private int		tarifa_umbral_ju;
	private int		tarifa_umbral_vi;
	private int		tarifa_umbral_sa;
	private int		tarifa_umbral_do;

	
	public AdmHorarioDespachoDTO(){
		
	}

	/**
	 * @return Returns the capac_do.
	 */
	public int getCapac_do() {
		return capac_do;
	}

	/**
	 * @param capac_do The capac_do to set.
	 */
	public void setCapac_do(int capac_do) {
		this.capac_do = capac_do;
	}

	/**
	 * @return Returns the capac_ju.
	 */
	public int getCapac_ju() {
		return capac_ju;
	}

	/**
	 * @param capac_ju The capac_ju to set.
	 */
	public void setCapac_ju(int capac_ju) {
		this.capac_ju = capac_ju;
	}

	/**
	 * @return Returns the capac_lu.
	 */
	public int getCapac_lu() {
		return capac_lu;
	}

	/**
	 * @param capac_lu The capac_lu to set.
	 */
	public void setCapac_lu(int capac_lu) {
		this.capac_lu = capac_lu;
	}

	/**
	 * @return Returns the capac_ma.
	 */
	public int getCapac_ma() {
		return capac_ma;
	}

	/**
	 * @param capac_ma The capac_ma to set.
	 */
	public void setCapac_ma(int capac_ma) {
		this.capac_ma = capac_ma;
	}

	/**
	 * @return Returns the capac_mi.
	 */
	public int getCapac_mi() {
		return capac_mi;
	}

	/**
	 * @param capac_mi The capac_mi to set.
	 */
	public void setCapac_mi(int capac_mi) {
		this.capac_mi = capac_mi;
	}

	/**
	 * @return Returns the capac_sa.
	 */
	public int getCapac_sa() {
		return capac_sa;
	}

	/**
	 * @param capac_sa The capac_sa to set.
	 */
	public void setCapac_sa(int capac_sa) {
		this.capac_sa = capac_sa;
	}

	/**
	 * @return Returns the capac_vi.
	 */
	public int getCapac_vi() {
		return capac_vi;
	}

	/**
	 * @param capac_vi The capac_vi to set.
	 */
	public void setCapac_vi(int capac_vi) {
		this.capac_vi = capac_vi;
	}

	public Time getH_fin() {
		return h_fin;
	}


	public void setH_fin(Time h_fin) {
		this.h_fin = h_fin;
	}


	public Time getH_ini() {
		return h_ini;
	}


	public void setH_ini(Time h_ini) {
		this.h_ini = h_ini;
	}


	public long getId_jpicking_do() {
		return id_jpicking_do;
	}


	public void setId_jpicking_do(long id_jpicking_do) {
		this.id_jpicking_do = id_jpicking_do;
	}


	public long getId_jpicking_ju() {
		return id_jpicking_ju;
	}


	public void setId_jpicking_ju(long id_jpicking_ju) {
		this.id_jpicking_ju = id_jpicking_ju;
	}


	public long getId_jpicking_lu() {
		return id_jpicking_lu;
	}


	public void setId_jpicking_lu(long id_jpicking_lu) {
		this.id_jpicking_lu = id_jpicking_lu;
	}


	public long getId_jpicking_ma() {
		return id_jpicking_ma;
	}


	public void setId_jpicking_ma(long id_jpicking_ma) {
		this.id_jpicking_ma = id_jpicking_ma;
	}


	public long getId_jpicking_mi() {
		return id_jpicking_mi;
	}


	public void setId_jpicking_mi(long id_jpicking_mi) {
		this.id_jpicking_mi = id_jpicking_mi;
	}


	public long getId_jpicking_sa() {
		return id_jpicking_sa;
	}


	public void setId_jpicking_sa(long id_jpicking_sa) {
		this.id_jpicking_sa = id_jpicking_sa;
	}


	public long getId_jpicking_vi() {
		return id_jpicking_vi;
	}


	public void setId_jpicking_vi(long id_jpicking_vi) {
		this.id_jpicking_vi = id_jpicking_vi;
	}


	
    /**
     * @return Devuelve tarifa_economica_do.
     */
    public int getTarifa_economica_do() {
        return tarifa_economica_do;
    }
    /**
     * @param tarifa_economica_do El tarifa_economica_do a establecer.
     */
    public void setTarifa_economica_do(int tarifa_economica_do) {
        this.tarifa_economica_do = tarifa_economica_do;
    }
    /**
     * @return Devuelve tarifa_economica_ju.
     */
    public int getTarifa_economica_ju() {
        return tarifa_economica_ju;
    }
    /**
     * @param tarifa_economica_ju El tarifa_economica_ju a establecer.
     */
    public void setTarifa_economica_ju(int tarifa_economica_ju) {
        this.tarifa_economica_ju = tarifa_economica_ju;
    }
    /**
     * @return Devuelve tarifa_economica_lu.
     */
    public int getTarifa_economica_lu() {
        return tarifa_economica_lu;
    }
    /**
     * @param tarifa_economica_lu El tarifa_economica_lu a establecer.
     */
    public void setTarifa_economica_lu(int tarifa_economica_lu) {
        this.tarifa_economica_lu = tarifa_economica_lu;
    }
    /**
     * @return Devuelve tarifa_economica_ma.
     */
    public int getTarifa_economica_ma() {
        return tarifa_economica_ma;
    }
    /**
     * @param tarifa_economica_ma El tarifa_economica_ma a establecer.
     */
    public void setTarifa_economica_ma(int tarifa_economica_ma) {
        this.tarifa_economica_ma = tarifa_economica_ma;
    }
    /**
     * @return Devuelve tarifa_economica_mi.
     */
    public int getTarifa_economica_mi() {
        return tarifa_economica_mi;
    }
    /**
     * @param tarifa_economica_mi El tarifa_economica_mi a establecer.
     */
    public void setTarifa_economica_mi(int tarifa_economica_mi) {
        this.tarifa_economica_mi = tarifa_economica_mi;
    }
    /**
     * @return Devuelve tarifa_economica_sa.
     */
    public int getTarifa_economica_sa() {
        return tarifa_economica_sa;
    }
    /**
     * @param tarifa_economica_sa El tarifa_economica_sa a establecer.
     */
    public void setTarifa_economica_sa(int tarifa_economica_sa) {
        this.tarifa_economica_sa = tarifa_economica_sa;
    }
    /**
     * @return Devuelve tarifa_economica_vi.
     */
    public int getTarifa_economica_vi() {
        return tarifa_economica_vi;
    }
    /**
     * @param tarifa_economica_vi El tarifa_economica_vi a establecer.
     */
    public void setTarifa_economica_vi(int tarifa_economica_vi) {
        this.tarifa_economica_vi = tarifa_economica_vi;
    }
    /**
     * @return Devuelve tarifa_express_do.
     */
    public int getTarifa_express_do() {
        return tarifa_express_do;
    }
    /**
     * @param tarifa_express_do El tarifa_express_do a establecer.
     */
    public void setTarifa_express_do(int tarifa_express_do) {
        this.tarifa_express_do = tarifa_express_do;
    }
    /**
     * @return Devuelve tarifa_express_ju.
     */
    public int getTarifa_express_ju() {
        return tarifa_express_ju;
    }
    /**
     * @param tarifa_express_ju El tarifa_express_ju a establecer.
     */
    public void setTarifa_express_ju(int tarifa_express_ju) {
        this.tarifa_express_ju = tarifa_express_ju;
    }
    /**
     * @return Devuelve tarifa_express_lu.
     */
    public int getTarifa_express_lu() {
        return tarifa_express_lu;
    }
    /**
     * @param tarifa_express_lu El tarifa_express_lu a establecer.
     */
    public void setTarifa_express_lu(int tarifa_express_lu) {
        this.tarifa_express_lu = tarifa_express_lu;
    }
    /**
     * @return Devuelve tarifa_express_ma.
     */
    public int getTarifa_express_ma() {
        return tarifa_express_ma;
    }
    /**
     * @param tarifa_express_ma El tarifa_express_ma a establecer.
     */
    public void setTarifa_express_ma(int tarifa_express_ma) {
        this.tarifa_express_ma = tarifa_express_ma;
    }
    /**
     * @return Devuelve tarifa_express_mi.
     */
    public int getTarifa_express_mi() {
        return tarifa_express_mi;
    }
    /**
     * @param tarifa_express_mi El tarifa_express_mi a establecer.
     */
    public void setTarifa_express_mi(int tarifa_express_mi) {
        this.tarifa_express_mi = tarifa_express_mi;
    }
    /**
     * @return Devuelve tarifa_express_sa.
     */
    public int getTarifa_express_sa() {
        return tarifa_express_sa;
    }
    /**
     * @param tarifa_express_sa El tarifa_express_sa a establecer.
     */
    public void setTarifa_express_sa(int tarifa_express_sa) {
        this.tarifa_express_sa = tarifa_express_sa;
    }
    /**
     * @return Devuelve tarifa_express_vi.
     */
    public int getTarifa_express_vi() {
        return tarifa_express_vi;
    }
    /**
     * @param tarifa_express_vi El tarifa_express_vi a establecer.
     */
    public void setTarifa_express_vi(int tarifa_express_vi) {
        this.tarifa_express_vi = tarifa_express_vi;
    }
    /**
     * @return Devuelve tarifa_normal_do.
     */
    public int getTarifa_normal_do() {
        return tarifa_normal_do;
    }
    /**
     * @param tarifa_normal_do El tarifa_normal_do a establecer.
     */
    public void setTarifa_normal_do(int tarifa_normal_do) {
        this.tarifa_normal_do = tarifa_normal_do;
    }
    /**
     * @return Devuelve tarifa_normal_ju.
     */
    public int getTarifa_normal_ju() {
        return tarifa_normal_ju;
    }
    /**
     * @param tarifa_normal_ju El tarifa_normal_ju a establecer.
     */
    public void setTarifa_normal_ju(int tarifa_normal_ju) {
        this.tarifa_normal_ju = tarifa_normal_ju;
    }
    /**
     * @return Devuelve tarifa_normal_lu.
     */
    public int getTarifa_normal_lu() {
        return tarifa_normal_lu;
    }
    /**
     * @param tarifa_normal_lu El tarifa_normal_lu a establecer.
     */
    public void setTarifa_normal_lu(int tarifa_normal_lu) {
        this.tarifa_normal_lu = tarifa_normal_lu;
    }
    /**
     * @return Devuelve tarifa_normal_ma.
     */
    public int getTarifa_normal_ma() {
        return tarifa_normal_ma;
    }
    /**
     * @param tarifa_normal_ma El tarifa_normal_ma a establecer.
     */
    public void setTarifa_normal_ma(int tarifa_normal_ma) {
        this.tarifa_normal_ma = tarifa_normal_ma;
    }
    /**
     * @return Devuelve tarifa_normal_mi.
     */
    public int getTarifa_normal_mi() {
        return tarifa_normal_mi;
    }
    /**
     * @param tarifa_normal_mi El tarifa_normal_mi a establecer.
     */
    public void setTarifa_normal_mi(int tarifa_normal_mi) {
        this.tarifa_normal_mi = tarifa_normal_mi;
    }
    /**
     * @return Devuelve tarifa_normal_sa.
     */
    public int getTarifa_normal_sa() {
        return tarifa_normal_sa;
    }
    /**
     * @param tarifa_normal_sa El tarifa_normal_sa a establecer.
     */
    public void setTarifa_normal_sa(int tarifa_normal_sa) {
        this.tarifa_normal_sa = tarifa_normal_sa;
    }
    /**
     * @return Devuelve tarifa_normal_vi.
     */
    public int getTarifa_normal_vi() {
        return tarifa_normal_vi;
    }
    /**
     * @param tarifa_normal_vi El tarifa_normal_vi a establecer.
     */
    public void setTarifa_normal_vi(int tarifa_normal_vi) {
        this.tarifa_normal_vi = tarifa_normal_vi;
    }

	public int getTarifa_umbral_lu() {
		return tarifa_umbral_lu;
	}

	public void setTarifa_umbral_lu(int tarifa_umbral_lu) {
		this.tarifa_umbral_lu = tarifa_umbral_lu;
	}

	public int getTarifa_umbral_ma() {
		return tarifa_umbral_ma;
	}

	public void setTarifa_umbral_ma(int tarifa_umbral_ma) {
		this.tarifa_umbral_ma = tarifa_umbral_ma;
	}

	public int getTarifa_umbral_mi() {
		return tarifa_umbral_mi;
	}

	public void setTarifa_umbral_mi(int tarifa_umbral_mi) {
		this.tarifa_umbral_mi = tarifa_umbral_mi;
	}

	public int getTarifa_umbral_ju() {
		return tarifa_umbral_ju;
	}

	public void setTarifa_umbral_ju(int tarifa_umbral_ju) {
		this.tarifa_umbral_ju = tarifa_umbral_ju;
	}

	public int getTarifa_umbral_vi() {
		return tarifa_umbral_vi;
	}

	public void setTarifa_umbral_vi(int tarifa_umbral_vi) {
		this.tarifa_umbral_vi = tarifa_umbral_vi;
	}

	public int getTarifa_umbral_sa() {
		return tarifa_umbral_sa;
	}

	public void setTarifa_umbral_sa(int tarifa_umbral_sa) {
		this.tarifa_umbral_sa = tarifa_umbral_sa;
	}

	public int getTarifa_umbral_do() {
		return tarifa_umbral_do;
	}

	public void setTarifa_umbral_do(int tarifa_umbral_do) {
		this.tarifa_umbral_do = tarifa_umbral_do;
	}
    
    
}
