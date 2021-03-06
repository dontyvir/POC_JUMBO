/*
 * Created on 03-07-2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import cl.jumbo.interfaces.exceptions.ProcesoInterfazException;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MonitorVentaCDP extends Thread implements ProcesoInterfaz {

	private boolean stopFlag = false;
	private boolean sleepFlag = false;
	private boolean pauseFlag = false;
	private long timeSleep = 0;
	private Status status = null;
	private Vector statusHistory = new Vector();
	private String dirSource = "";
	private String dirTarget = "";
	private String dirBackup = "";
	private int offsetDay = 0;
	private Date nominalDate = null;
	private ControladorProcesos controlador = null;
	private FileMonitorUtil fileUtil = new FileMonitorUtil();
	private LocalesJumbo[] locales = null;


	public MonitorVentaCDP(
			LocalesJumbo[] locales, 
			String dirSource, 
			String dirTarget, 
			String dirBackup, 
			int offsetDay,
			Date nominalDate,
			ControladorProcesos controlador) {
		super();
		this.locales = locales;
		this.dirSource = dirSource;
		this.dirTarget = dirTarget;
		this.offsetDay = offsetDay;
		this.nominalDate = nominalDate;
		this.controlador = controlador;
		this.dirBackup = dirBackup;
	}

	/**
	 * @param target
	 */
	public MonitorVentaCDP(Runnable target) {
		super(target);
		// Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 */
	public MonitorVentaCDP(ThreadGroup group, Runnable target) {
		super(group, target);
		// Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public MonitorVentaCDP(String name) {
		super(name);
		// Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param name
	 */
	public MonitorVentaCDP(ThreadGroup group, String name) {
		super(group, name);
		// Auto-generated constructor stub
	}

	/**
	 * @param target
	 * @param name
	 */
	public MonitorVentaCDP(Runnable target, String name) {
		super(target, name);
		// Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 */
	public MonitorVentaCDP(ThreadGroup group, Runnable target,
			String name) {
		super(group, target, name);
		// Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 * @param stackSize
	 */
	public MonitorVentaCDP(ThreadGroup group, Runnable target,
			String name, long stackSize) {
		super(group, target, name, stackSize);
		// Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#runProcess()
	 */
	public void runProcess() {
		start();
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toStop()
	 */
	public void toStop() {
		stopFlag = true;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#getStatusHistory()
	 */
	public Vector getStatusHistory() {
		return statusHistory;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#getStatus()
	 */
	public Status getStatus() {
		return this.status;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toPause()
	 */
	public void toPause() {
		pauseFlag = true;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toResume()
	 */
	public void toResume() {
		pauseFlag = false;
		notify();
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toSleep(long)
	 */
	public void toSleep(long time) {
		timeSleep = time;
		sleepFlag = true;
		if (time == 0)
			sleepFlag = false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		setStatus(new Status(1, "Iniciando Monitor de Ventas CDP"));
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		long offset = 24 * 60 * 60 * 1000 * (long)offsetDay;
		long timeWithOffset = nominalDate.getTime() - offset;
		String dateToFind = sdf.format(new Date(timeWithOffset));
		Vector filesToMove = new Vector();
		Vector filesToBackup = new Vector();
		try {
			if ((locales != null) && (locales.length > 0)) {
				setStatus(new Status(1, "Buscando archivos necesarios"));
				for (int i = 0; i < locales.length; i++) {
					try {
						filesToMove.addAll(getFileToProcess(SufijosInterfaces.I_VENTAS_HISTORICAS + locales[i].getCodLocal() + dateToFind + ".*", 2));
					} catch (FileNotFoundException e) {
						setStatus(new Status(1, "Archivo de Ventas para Local " + locales[i].getCodLocal() + " no encontrado."));
					}
				}
				setStatus(new Status(1, "Respaldando archivos necesarios"));
				for (Iterator iter = filesToMove.iterator(); iter.hasNext();) {
					File element = (File) iter.next();
					File source = new File(this.dirSource + File.separator + element.getName());
					File target = new File(this.dirBackup + File.separator + element.getName());
					fileUtil.copyFile(source, target);
					setStatus(new Status(1, "Archivo '" + source.getName() + "' respaldado"));
				}
				setStatus(new Status(1, "Descomprimiendo y copiando archivos necesarios a directorio de Proceso"));
				for (Iterator iter = filesToMove.iterator(); iter.hasNext();) {
					File element = (File) iter.next();
					File source = new File(this.dirSource + File.separator + element.getName());
					if (element.getName().substring(element.getName().length() - 3).matches("\\.gz")) {
						fileUtil.uncompressFiles(source, null, new File(this.dirTarget));
						setStatus(new Status(1, "Archivo '" + source.getName() + "' copiado"));
					} else {
						source.delete();
					}
				}
				try {
					setStatus(new Status(1, "Buscando archivos innecesarios"));
					filesToBackup.addAll(getFileToProcess(SufijosInterfaces.I_VENTAS_HISTORICAS + "...." + dateToFind + ".*", -1));
					setStatus(new Status(1, "Moviendo archivos innecesarios"));
					for (Iterator iter = filesToBackup.iterator(); iter.hasNext();) {
						File element = (File) iter.next();
						File source = new File(this.dirSource + File.separator + element.getName());
						File target = new File(this.dirBackup + File.separator + element.getName());
						fileUtil.moveFile(source, target);
						setStatus(new Status(1, "Archivo '" + source.getName() + "' movido"));
					}
				} catch (FileNotFoundException e) {
					setStatus(new Status(1, "No hay archivos innecesarios"));
				}
				setStatus(new Status(0, "Proceso finalizado"));
			} else {
				setStatus(new Status(-1, "No hay locales definidos para cargar"));
				setStatus(new Status(-1, "Proceso finalizado"));
			}
		} catch (FileNotFoundException e) {
			setStatus(new Status(-1, e.getMessage()));
			setStatus(new Status(-1, "Proceso finalizado"));
		} catch (ProcesoInterfazException e) {
			setStatus(new Status(-1, e.getMessage()));
			setStatus(new Status(-1, "Proceso finalizado"));
		} catch (Exception e) {
			setStatus(new Status(-1, e.getMessage()));
			setStatus(new Status(-1, "Proceso finalizado"));
			e.printStackTrace();
		}
		controlador.setStatusHistory(statusHistory);
	}
	
	private Vector getFileToProcess(String sufix, int totalFilesToFind) throws FileNotFoundException {
		boolean cantidadIndeterminada = false;
		if (totalFilesToFind == -1)
			cantidadIndeterminada = true;
		Vector result = new Vector();
		File[] files = fileUtil.findFiles(new File(this.dirSource), sufix, null);
		if ((files == null) || (files.length == 0) || (!cantidadIndeterminada && (files.length != totalFilesToFind))) {
			throw new FileNotFoundException("Archivo '" + sufix + "', GZ o TRG no encontrado");
		} else {
			result.addAll(Arrays.asList(files));
			setStatus(new Status(1, "Archivo '" + sufix + "', GZ y TRG encontrados"));
		}
		return result;
	}
	
	private void setStatus(Status status) {
		this.status = status;
		statusHistory.add(0, status);
	}
	
}
