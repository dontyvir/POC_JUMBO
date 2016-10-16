/*
 * Created on 21-06-2007
 *
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
 */
public class MonitorInterfacesProductos extends Thread implements
		ProcesoInterfaz {

	private boolean stopFlag = false;
	private boolean sleepFlag = false;
	private boolean pauseFlag = false;
	private long timeSleep = 0;
	private LocalesJumbo[] locales = null;
	private Status status = null;
	private Vector statusHistory = new Vector();
	private String dirSource = "";
	private String dirTarget = "";
	private String dirBackup = "";
	private int offsetDayProductos = 0;
	private int offsetDayPrecios = 0;
	private Date nominalDate = null;
	private ControladorProcesos controlador = null;
	private FileMonitorUtil fileUtil = new FileMonitorUtil();
	
	public MonitorInterfacesProductos(
			LocalesJumbo[] locales, 
			String dirSource, 
			String dirTarget, 
			String dirBackup, 
			int offsetDayProductos,
			int offsetDayPrecios,
			Date nominalDate,
			ControladorProcesos controlador) {
		super();
		this.locales = locales;
		this.dirSource = dirSource;
		this.dirTarget = dirTarget;
		this.offsetDayProductos = offsetDayProductos;
		this.offsetDayPrecios = offsetDayPrecios;
		this.nominalDate = nominalDate;
		this.controlador = controlador;
		this.dirBackup = dirBackup;
	}

	/**
	 * @param target
	 */
	public MonitorInterfacesProductos(Runnable target) {
		super(target);
	}

	/**
	 * @param group
	 * @param target
	 */
	public MonitorInterfacesProductos(ThreadGroup group, Runnable target) {
		super(group, target);
	}

	/**
	 * @param name
	 */
	public MonitorInterfacesProductos(String name) {
		super(name);
	}

	/**
	 * @param group
	 * @param name
	 */
	public MonitorInterfacesProductos(ThreadGroup group, String name) {
		super(group, name);
	}

	/**
	 * @param target
	 * @param name
	 */
	public MonitorInterfacesProductos(Runnable target, String name) {
		super(target, name);
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 */
	public MonitorInterfacesProductos(ThreadGroup group, Runnable target,
			String name) {
		super(group, target, name);
	}

	/**
	 * @param group
	 * @param target
	 * @param name
	 * @param stackSize
	 */
	public MonitorInterfacesProductos(ThreadGroup group, Runnable target,
			String name, long stackSize) {
		super(group, target, name, stackSize);
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
		setStatus(new Status(1, "Iniciando Monitor de Interfaces"));
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		SimpleDateFormat sdfPrecios = new SimpleDateFormat("MMdd");
		long offsetProds = 24 * 60 * 60 * 1000 * (long)offsetDayProductos;
		long offsetPrecios = 24 * 60 * 60 * 1000 * (long)offsetDayPrecios;
		long timeWithOffset = nominalDate.getTime() - offsetProds;
		long timePreciosWithOffset = nominalDate.getTime() - offsetPrecios;
		String dateToFind = sdf.format(new Date(timeWithOffset));
		String dateToFindPrecios = sdfPrecios.format(new Date(timePreciosWithOffset));
		Vector filesToMove = new Vector();
		Vector filesPreciosToMove = new Vector();
		Vector filesToBackup = new Vector();
		try {
			if ((locales != null) && (locales.length > 0)) {
				setStatus(new Status(1, "Buscando archivos necesarios"));
				filesToMove.addAll(getFileToProcess(SufijosInterfaces.I_PRODUCTOS + dateToFind + "\\.CSV", 2));
				filesToMove.addAll(getFileToProcess(SufijosInterfaces.I_CATEGORIAS + dateToFind + "\\.CSV", 2));
				filesToMove.addAll(getFileToProcess(SufijosInterfaces.I_CODBARRAS + dateToFind + "\\.CSV", 2));
				for (int i = 0; i < locales.length; i++)
					filesToMove.addAll(getFileToProcess(SufijosInterfaces.I_INVENTARIO + locales[i].getCodLocal() + dateToFind + "\\.CSV", 2));
				try {
					for (int i = 0; i < locales.length; i++)
						filesPreciosToMove.addAll(getFileToProcess(SufijosInterfaces.I_MAESTRO_PRECIOS + locales[i].getCodLocal() + dateToFindPrecios + "......", 2));
				} catch (FileNotFoundException e) {
				}
				setStatus(new Status(1, "Respaldando archivos necesarios"));
				for (Iterator iter = filesToMove.iterator(); iter.hasNext();) {
					File element = (File) iter.next();
					File source = new File(this.dirSource + File.separator + element.getName());
					File target = new File(this.dirBackup + File.separator + element.getName());
					fileUtil.copyFile(source, target);
					setStatus(new Status(1, "Archivo '" + source.getName() + "' respaldado"));
				}
				for (Iterator iter = filesPreciosToMove.iterator(); iter.hasNext();) {
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
				for (Iterator iter = filesPreciosToMove.iterator(); iter.hasNext();) {
					File element = (File) iter.next();
					File source = new File(this.dirSource + File.separator + element.getName());
					if (element.getName().substring(element.getName().length() - 3).matches("\\.gz")) {
						fileUtil.uncompressFiles(source, element.getName().substring(0, element.getName().length() - 3) + ".txt", new File(this.dirTarget));
						setStatus(new Status(1, "Archivo '" + source.getName() + "' copiado"));
					} else {
						source.delete();
					}
				}
				setStatus(new Status(1, "Buscando archivos innecesarios"));
				try {
					filesToBackup.addAll(getFileToProcess(SufijosInterfaces.I_INVENTARIO + "...." + dateToFind + "\\.CSV", -1));
				} catch (FileNotFoundException e) {
				}
				try {
					filesToBackup.addAll(getFileToProcess(SufijosInterfaces.I_LOCALES + "...." + dateToFind + "\\.CSV", -1));
				} catch (FileNotFoundException e) {
				}
				try {
					filesToBackup.addAll(getFileToProcess(SufijosInterfaces.I_MAESTRO_PRECIOS + "...." + dateToFindPrecios + "......", -1));
				} catch (FileNotFoundException e) {
				}
				for (Iterator iter = filesToBackup.iterator(); iter.hasNext();) {
					File element = (File) iter.next();
					File source = new File(this.dirSource + File.separator + element.getName());
					File target = new File(this.dirBackup + File.separator + element.getName());
					fileUtil.moveFile(source, target);
					setStatus(new Status(1, "Archivo '" + source.getName() + "' movido"));
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
