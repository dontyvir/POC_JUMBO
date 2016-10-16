// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov Date: 28/09/2009
// 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: packimports(3)
// Source File Name: Libreria.java

package cl.bbr.promo.lib.helpers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import cl.bbr.irs.promolib.ctrl.IrsPromosDaoCtrl;
import cl.bbr.irs.promolib.entity.MatrizSeccionEntity;
import cl.bbr.irs.promolib.entity.PromoSeccionEntity;
import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.irs.promolib.exceptions.IrsPromocionException;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dao.DAOFactory;
import cl.bbr.promo.lib.dao.PromoDAO;
import cl.bbr.promo.lib.dto.BeneficioDTO;
import cl.bbr.promo.lib.dto.CondicionDTO;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.bbr.promo.lib.dto.ProductoBenConDTO;
import cl.bbr.promo.lib.dto.ProductoDTO;
import cl.bbr.promo.lib.dto.PromoMedioPagoDTO;
import cl.bbr.promo.lib.dto.ProrrateoProductoDTO;
import cl.bbr.promo.lib.dto.ProrrateoProductoSeccionDTO;
import cl.bbr.promo.lib.dto.ProrrateoPromocionProductoDTO;
import cl.bbr.promo.lib.dto.ProrrateoPromocionSeccionDTO;
import cl.bbr.promo.lib.dto.ProrrateoSeccionDTO;
import cl.bbr.promo.lib.dto.SeccionDTO;
import cl.bbr.promo.lib.dto.TcpClienteDTO;
import cl.bbr.promo.lib.dto.TcpPromoSeccion;
import cl.bbr.promo.lib.exception.PromoDAOException;

public class Libreria {

    //[20121114avc
    private double descTramo1 = 12;

    private double descTramo2 = 5;

    private long limMensual = 500000;

    private String formaPago = "CAT";

    //]20121114avc
    
    //ccp dscto
    private static String CUP_PRODUCTO = "P"; 
    private static String CUP_RUBRO = "R";
    private static String CUP_SECCION = "S";
    private static String CUP_TODAS_LAS_SECCIONES = "TS";

    public Libreria() throws NumberFormatException, Exception {
        logger = new Logging(this);
        PRODUCTOS = null;
        CONDICION = null;
        BENEFICIO = null;
        PROMOCION = null;
        SECCION = null;
        PRO_PRODUCTO = null;
        PRO_SECCION = null;
        PRO_PRODSEC = null;
        PRO_TCP = null;
        PRODUCTOS = new ArrayList();

        //[20121114avc
        ParametrosService parametro = new ParametrosService();
        descTramo1 = Double.parseDouble(parametro.getParametroByName("DESC_COL_1").getValor());
        descTramo2 = Double.parseDouble(parametro.getParametroByName("DESC_COL_2").getValor());
        limMensual = Long.parseLong(parametro.getParametroByName("DESC_COL_LIM").getValor());
        formaPago = parametro.getParametroByName("DESC_COL_FPAGO").getValor();
        //]20121114avc
    }

    public void inserta(ProductoDTO producto) {
        validaPromoAsociadas(producto);
    }

    //[20121108avc

    public long calcula(String fpago, int cuotas, List tcp, int canal, int local, Long rut, CuponDsctoDTO cddto, List cuponProds) {

    	long desc = 0;
    	boolean formaPagoValida = false;
    	
    	
        String[] formaPagoList = formaPago.split(",");
        for (int i = 0; i < formaPagoList.length; i++) {
            if (formaPagoList[i].equals(fpago)) {
                formaPagoValida = true;
                break;
            }
        }
        
        desc = calcula(fpago, cuotas, tcp, canal, local);
        
// inicio cdd
        boolean isMPCupon = false;
    	if (cddto != null && cuponProds != null) {
    		if ( cddto.getMedio_pago() == 0 ){
    			isMPCupon = true;
    		} else if ("CAT".equals(fpago) && cddto.getMedio_pago() == 1) {
    			isMPCupon = true;
    		} else if ("TBK".equals(fpago) && cddto.getMedio_pago() == 2) {
    			isMPCupon = true;
    		} else {
    			isMPCupon = false;
    		}
    	}
    	if (isMPCupon && (!"D".equals(cddto.getTipo())) && formaPagoValida && rut != null) {
    		//Inicio (17-12-2014): Contingencia_gramaje NSepulveda
    		desc = calculaCuponDsctoTMASTBK(cddto,cuponProds, local, true, fpago);
    		desc += calculaDescColaborador(rut.longValue(), local, cddto, cuponProds);
    		//Fin (17-12-2014): Contingencia_gramaje NSepulveda
    		
    		//Original:
    		/*desc = calculaCuponDscto(cddto,cuponProds, local, true);
    		 desc += calculaDescColaborador(rut.longValue(), local, cddto, cuponProds);*/
    	} else if (isMPCupon && (!"D".equals(cddto.getTipo()))){
    		//Inicio (17-12-2014): Contingencia_gramaje NSepulveda
    		desc = calculaCuponDsctoTMASTBK(cddto,cuponProds, local, false, fpago);
    		//Fin (17-12-2014): Contingencia_gramaje NSepulveda
    		
    		//Original:
    		/*desc = calculaCuponDscto(cddto,cuponProds, local, false);*/
    	} else if (formaPagoValida && rut != null) { 
            desc = calculaDescColaborador(rut.longValue(), local, cddto, cuponProds);
        } 
// fin cdd

    	
    	
    	return desc;
    }
    
    //Inicio (17-12-2014): Contingencia_gramaje NSepulveda
    private long calculaCuponDsctoTMASTBK(CuponDsctoDTO cddto, List cuponProds, int local, boolean colaborador, String fpago) {
    	
    	long dscto = 0;
    	long dsctoCupon = 0;
    	long dsctoPromo = 0;
    	List prorrateoProds = new ArrayList();
    	
    	Iterator iter = PRODUCTOS.iterator();
    	while (iter.hasNext()) {
    		ProductoDTO prod = (ProductoDTO) iter.next();
    		if (prod.getPromocion().size() < 1) {
	    		if (isAplicaCupon(prod, cddto, cuponProds, colaborador)) {
	    			dscto = (long) ((prod.getPrecio() * cddto.getDescuento()) / 100D);
	    			prorrateoProds.add(setProrrateoProducto(prod, dscto));
	    			dsctoCupon +=  dscto;
	    		}
    		} else {
    			//Inicio (17-12-2014): Contingencia_gramaje NSepulveda
    			//Para medio de pago TBK:
    			/*if ("TBK".equals(fpago)){
    				if (isAplicaCupon(prod, cddto, cuponProds, colaborador)) {
    	    			dscto = (long) ((prod.getPrecio() * cddto.getDescuento()) / 100D);
    	    			prorrateoProds.add(setProrrateoProducto(prod, dscto));
    	    			dsctoCupon +=  dscto;
    	    		}
    			}//Fin (17-12-2014): Contingencia_gramaje NSepulveda
    			else{*/
    				if (isAplicaCupon(prod, cddto, cuponProds, colaborador) && isCuponBetter(prod, cddto, local)) {
        				dscto = CuponVsPromo(prod, cddto, local, false);
        				prorrateoProds.add(setProrrateoProducto(prod, dscto));
        				dsctoCupon += dscto;
    	    		} else if ( !colaborador ) {
    	    			dsctoPromo += CuponVsPromo(prod, cddto, local, true);
    	    		}
    			//}
    		}
    	}
		
		if (prorrateoProds.size() > 0) {
			PRO_PRODUCTO.add(setProrrateoPromocionProducto(prorrateoProds, cddto, dsctoCupon));
		}
    	
    	return dsctoCupon + dsctoPromo;
    }
    //Fin (17-12-2014): Contingencia_gramaje NSepulveda
    
// inicio cdd
    private long calculaCuponDscto(CuponDsctoDTO cddto, List cuponProds, int local, boolean colaborador) {
    	
    	long dscto = 0;
    	long dsctoCupon = 0;
    	long dsctoPromo = 0;
    	List prorrateoProds = new ArrayList();
    	
    	Iterator iter = PRODUCTOS.iterator();
    	while (iter.hasNext()) {
    		ProductoDTO prod = (ProductoDTO) iter.next();
    		if (prod.getPromocion().size() < 1) {
	    		if (isAplicaCupon(prod, cddto, cuponProds, colaborador)) {
	    			dscto = (long) ((prod.getPrecio() * cddto.getDescuento()) / 100D);
	    			prorrateoProds.add(setProrrateoProducto(prod, dscto));
	    			dsctoCupon +=  dscto;
	    		}
    		} else {
    			if (isAplicaCupon(prod, cddto, cuponProds, colaborador) && isCuponBetter(prod, cddto, local)) {
    				dscto = CuponVsPromo(prod, cddto, local, false);
    				prorrateoProds.add(setProrrateoProducto(prod, dscto));
    				dsctoCupon += dscto;
	    		} else if ( !colaborador ) {
	    			dsctoPromo += CuponVsPromo(prod, cddto, local, true);
	    		}
    		}
    	}
		
		if (prorrateoProds.size() > 0) {
			PRO_PRODUCTO.add(setProrrateoPromocionProducto(prorrateoProds, cddto, dsctoCupon));
		}
    	
    	return dsctoCupon + dsctoPromo;
    }
    
    private boolean isCuponBetter(ProductoDTO prod, CuponDsctoDTO cddto, int local) {
    	
    	boolean result = false;
    	boolean isPromo = false;
    	long dscto = 0;
    	long precioUnitario = prod.getPrecio() / prod.getCantidad();
    	int codigoPromocion = 0;
    	int tipoPromo = 0;
    	boolean encontrado=false;
		Iterator itPromo = prod.getPromocion().iterator();
		while (itPromo.hasNext()) {
			codigoPromocion = Integer.parseInt(itPromo.next().toString());
			PromocionEntity promoActual = new PromocionEntity();
			promoActual = getPromoCond(codigoPromocion, local);
			tipoPromo = promoActual.getTipo();
			if(tipoPromo != 100 && tipoPromo != 104 && tipoPromo != 105) {
				isPromo = true;
			}
			ProrrateoPromocionProductoDTO prorrateoPromocion = null;
			Iterator itListPromo = PRO_PRODUCTO.iterator();
			while (itListPromo.hasNext()) {
				prorrateoPromocion = (ProrrateoPromocionProductoDTO) itListPromo.next();
				if (prorrateoPromocion.getCodigo() == codigoPromocion) {
					Iterator itProdList = prorrateoPromocion.getListadoProductos().iterator();
					while (itProdList.hasNext()) {
						ProrrateoProductoDTO prorrateoProducto = (ProrrateoProductoDTO) itProdList.next();
						if(prorrateoProducto.getCodigo() == prod.getCodigo()) {
							encontrado =true;
							if(tipoPromo==11 && prorrateoProducto.isDesc()){
								dscto= calculaDescuentoAgrupacion(prorrateoPromocion.getListadoProductos(),prorrateoProducto.getIndiceAgrup(),cddto.getDescuento());
								long sumaP = sumaProrrateo(prorrateoPromocion.getListadoProductos(),prorrateoProducto.getIndiceAgrup());
								if (isPromo && dscto > sumaP) {
									result = true;
								}
							} else {
								dscto = (long) ((precioUnitario * cddto.getDescuento()) / 100D);
							
								if (isPromo && dscto > prorrateoProducto.getProrrateo()) {
									result = true;
								}
							}	
							break;
						} 
					}
					break;
				}
			}	
			break;
		}
		if(!encontrado && isPromo){
			result = true;
		}
    	return result;
    }
    
    private long sumaProrrateo(List listadoProductos, int indiceAgrup) {
		long prorrateo=0;
		for(int i=0;i<listadoProductos.size();i++){
			if(indiceAgrup == ((ProrrateoProductoDTO)listadoProductos.get(i)).getIndiceAgrup()){
				prorrateo+=((ProrrateoProductoDTO)listadoProductos.get(i)).getProrrateo();
			}
		}
		return prorrateo;
	}

	private long calculaDescuentoAgrupacion(List listadoProductos,
			int indiceAgrup, int descuento) {
		long dcto = 0;
		long sumaPrecios=0;
		for(int i=0;i<listadoProductos.size();i++){
			if(indiceAgrup == ((ProrrateoProductoDTO)listadoProductos.get(i)).getIndiceAgrup()){
				sumaPrecios+=((ProrrateoProductoDTO)listadoProductos.get(i)).getPrecio();
			}
		}
		dcto= (long) ((sumaPrecios * descuento) / 100D);
		return dcto;
	}

	private long CuponVsPromo(ProductoDTO prod, CuponDsctoDTO cddto, int local, boolean switchPromo) {
    	
    	long result = 0;
    	long precioUnitario = prod.getPrecio() / prod.getCantidad();
    	long dscto = 0;
    	if (!switchPromo)
    		dscto = (long) ((precioUnitario * cddto.getDescuento()) / 100D);
    	long valorProrrateo = 0;
    	int countProd = 0;
    	int countPromo = 0;
    	int codigoPromocion = 0;
    	boolean removeProd = false;
    	boolean removePromo = false;
    	boolean encontrado = false;
    	List prodsToRemove = new ArrayList();
    	
		Iterator itPromo = prod.getPromocion().iterator();
		while (itPromo.hasNext()) {
			codigoPromocion = Integer.parseInt(itPromo.next().toString());
			ProrrateoPromocionProductoDTO prorrateoPromocion = null;
			Iterator itListPromo = PRO_PRODUCTO.iterator();
			while (itListPromo.hasNext()) {
				prorrateoPromocion = (ProrrateoPromocionProductoDTO) itListPromo.next();
				if (prorrateoPromocion.getCodigo() == codigoPromocion) {
					int cantInPromo = prorrateoPromocion.getListadoProductos().size();
					Iterator itProdList = prorrateoPromocion.getListadoProductos().iterator();
					while (itProdList.hasNext()) {
						ProrrateoProductoDTO prorrateoProducto = (ProrrateoProductoDTO) itProdList.next();
						if(prorrateoProducto.getCodigo() == prod.getCodigo()) {
							encontrado=true;
							if(prorrateoProducto.isDesc()){
								long dsctoAgrupa= calculaDescuentoAgrupacion(prorrateoPromocion.getListadoProductos(),prorrateoProducto.getIndiceAgrup(),cddto.getDescuento());
								long sumaP = sumaProrrateo(prorrateoPromocion.getListadoProductos(), prorrateoProducto.getIndiceAgrup());
								if(dsctoAgrupa>sumaP){
									if (cantInPromo == 1 || cantInPromo == prod.getCantidad()) {
										removePromo = true;
										result = dscto*prod.getCantidad();
									}  else {
										valorProrrateo += prorrateoProducto.getProrrateo();
										for(int i=0;i<prorrateoPromocion.getListadoProductos().size();i++){
											if(prorrateoProducto.getIndiceAgrup() == ((ProrrateoProductoDTO)prorrateoPromocion.getListadoProductos().get(i)).getIndiceAgrup()){
												prodsToRemove.add(String.valueOf(i));
											}
										}	
										int value = 0;
										for(int i=0;i<prodsToRemove.size();i++){
											value = Integer.parseInt(prodsToRemove.get(i).toString());
											prorrateoPromocion.getListadoProductos().remove(value - i);
										}
										if(prorrateoPromocion.getListadoProductos().size() == 0){
											removePromo = true;
										}
										result += dscto*prod.getCantidad();
										prorrateoPromocion.setDescuento(prorrateoPromocion.getDescuento() - valorProrrateo);
										break;
									}
									
								} else {
									result =prorrateoProducto.getProrrateo()* prod.getCantidad();
									break;
								}
							} else {
								if (dscto > prorrateoProducto.getProrrateo()) {
									if (cantInPromo == 1 || cantInPromo == prod.getCantidad()) {
										removePromo = true;
										result = dscto * prod.getCantidad();
										break;
									} else {
										valorProrrateo += prorrateoProducto.getProrrateo();
										prodsToRemove.add(String.valueOf(countProd));
										removeProd = true;
										result += dscto;
									}
								} else {
									result = prorrateoProducto.getProrrateo() * prod.getCantidad();
									break;
								}
							}	
						} 
						countProd ++;
					}
					if (removeProd) {
						int value = 0;
						for (int i = 0; i < prodsToRemove.size(); i++) {
							value = Integer.parseInt(prodsToRemove.get(i).toString());
							prorrateoPromocion.getListadoProductos().remove(value - i);
						}
						prorrateoPromocion.setDescuento(prorrateoPromocion.getDescuento() - valorProrrateo);
						removePromo = true;
					}
					break;
				}
				countPromo ++;
			}	
			if (removePromo)
				PRO_PRODUCTO.remove(countPromo);
			if (removeProd)
				PRO_PRODUCTO.add(prorrateoPromocion);
			break;
		}
		if(!encontrado){
			result = dscto * prod.getCantidad();
		}
    	return result;
    }
    
    private ProrrateoProductoDTO setProrrateoProducto(ProductoDTO prod, long dscto) {
    	
    	ProrrateoProductoDTO prorrateoProducto = new ProrrateoProductoDTO();
		prorrateoProducto.setCantPeso(prod.getCantidad());
		prorrateoProducto.setCodigo(prod.getCodigo());
		prorrateoProducto.setDepto(prod.getDepto());
		prorrateoProducto.setFlagVenta(prod.getFlagVenta());
		prorrateoProducto.setPrecio(prod.getPrecio());
		prorrateoProducto.setProrrateo(dscto);
		
		return prorrateoProducto;
    } 
    
    private ProrrateoPromocionProductoDTO setProrrateoPromocionProducto(List prods, CuponDsctoDTO cddto, long dsctoTotal) {
    	
    	DecimalFormat formato = new DecimalFormat("###,###");
		ProrrateoPromocionProductoDTO prorrateoPromocion = new ProrrateoPromocionProductoDTO();
		prorrateoPromocion.setCodigo(-2);
		prorrateoPromocion.setDescripcion("Cupón de descuento "	+ formato.format(cddto.getDescuento()) + "%, código "+cddto.getCodigo());	
		prorrateoPromocion.setDescuento(dsctoTotal);
		prorrateoPromocion.setListadoProductos(prods);
		prorrateoPromocion.setTipo("C");
		
    	return prorrateoPromocion;
    }

    private boolean isAplicaCupon(ProductoDTO prod, CuponDsctoDTO cddto, List cuponProds, boolean isColaborador) {
    	
    	boolean isAplicaDscto = false;
    	
    	if ( !isColaborador) {
    	
    		if ("P".equals(cddto.getTipo())) {
    			Iterator iterProd = cuponProds.iterator();
        		while (iterProd.hasNext() && !isAplicaDscto) {
        			long productoCupon = Long.parseLong(iterProd.next().toString());
        			if (prod.getIdProducto() == productoCupon) {
        				isAplicaDscto = true;
        			}
    			}
    		} else if ("R".equals(cddto.getTipo())) {
    			Iterator iterProd = cuponProds.iterator();
        		while (iterProd.hasNext() && !isAplicaDscto) {
        			long productoCupon = Long.parseLong(iterProd.next().toString());
        			int seccionRubro = Integer.parseInt(prod.getDepto() + "" + prod.getRubro()); 
        			if (seccionRubro == productoCupon) {
        				isAplicaDscto = true;
        			}
    			}
    		} else if ("S".equals(cddto.getTipo()) || "TS".equals(cddto.getTipo())) {
    			Iterator iterProd = cuponProds.iterator();
        		while (iterProd.hasNext() && !isAplicaDscto) {
        			long productoCupon = Long.parseLong(iterProd.next().toString());
        			if (prod.getDepto() == productoCupon) {
        				isAplicaDscto = true;
        			}
    			}
    		}
    		
    	}else if( prod.isAfectoDescColaborador() && isColaborador) {
    		
    		if (CUP_PRODUCTO.equals(cddto.getTipo())) {
    			Iterator iterProd = cuponProds.iterator();
        		while (iterProd.hasNext() && !isAplicaDscto) {
        			long productoCupon = Long.parseLong(iterProd.next().toString());
        			if (prod.getIdProducto() == productoCupon && cddto.getDescuento() > descTramo1 ) {
        				isAplicaDscto = true;
        			}
    			}
    		} else if (CUP_RUBRO.equals(cddto.getTipo())) {
    			Iterator iterProd = cuponProds.iterator();
        		while (iterProd.hasNext() && !isAplicaDscto) {
        			long productoCupon = Long.parseLong(iterProd.next().toString());
        			int seccionRubro = Integer.parseInt(prod.getDepto() + "" + prod.getRubro()); 
        			if (seccionRubro == productoCupon && cddto.getDescuento() > descTramo1 ) {
        				isAplicaDscto = true;
        			}
    			}
    		} else if (CUP_SECCION.equals(cddto.getTipo()) || CUP_TODAS_LAS_SECCIONES.equals(cddto.getTipo())) {
    			Iterator iterProd = cuponProds.iterator();
        		while (iterProd.hasNext() && !isAplicaDscto) {
        			long productoCupon = Long.parseLong(iterProd.next().toString());
        			if (prod.getDepto() == productoCupon && cddto.getDescuento() > descTramo1 ) {
        				isAplicaDscto = true;
        			}
    			}
    		}
    		
    	}
    	
    	return isAplicaDscto;
    
    }
// fin cdd
    
    
	private long calculaDescColaborador(long rut, int local, CuponDsctoDTO cddto, List cuponProds) {
		IrsPromosDaoCtrl irs = new IrsPromosDaoCtrl();

		long comprasAcumuladas = 0;
		long compraActual = 0;

		try {
			comprasAcumuladas = irs.getComprasAcumuladas(rut);
		} catch (IrsPromocionException e) {
			logger.error("Error al calcular compras acumuladas: ", e);
		}

		List productosTramo1 = new ArrayList();
		List productosTramo2 = new ArrayList();

		long dctoTotal = 0;
		long dctoTramo1 = 0;
		long dctoTramo2 = 0;
		int tramo = 1;
		
//		mejora
		if (comprasAcumuladas >= limMensual){
			tramo = 2;
		}

		//fvasquez 03092013
		int codPromoAnterior;
		boolean aplicoPromo = false;
		boolean aplicoProrrateo;
		//fvasquez 03092013
		
		for (int i = 0; i < PRODUCTOS.size(); i++) {
			
			aplicoProrrateo=false; //fvr oct
			
			ProductoDTO producto = (ProductoDTO) PRODUCTOS.get(i);
	
// inicio cdd
			if (cddto != null && cuponProds != null) {
				
				if (producto.getPromocion().size() < 1) {
		    		if (isAplicaCupon(producto, cddto, cuponProds, true)) {
		    			continue;
		    		}
	    		} else {
	    			if (isAplicaCupon(producto, cddto, cuponProds, true) && isCuponBetter(producto, cddto, local)) {
	    				continue;
	    			} 
	    		}
	    	}
// fin cdd
			
			long descuentoActual = 0;
			double dcto = 0;
//			String tipoDcto = null;
			// [20121130avc
			int cantRestante = producto.getCantidad(); 
			int cantAplica = 0;
			codPromoAnterior=0; //fvr oct
			long precioUnitario = producto.getPrecio() / producto.getCantidad();
			
			if (producto.getPromocion().size() > 0) {
					for (int j = 0; j < producto.getPromocion().size(); j++) {
						int codigoPromocion = Integer.parseInt(String.valueOf(producto.getPromocion().get(j)));
						
						int cantPromocion = 0;
						
						PromocionEntity promoActual = new PromocionEntity();
						promoActual = getPromoCond(codigoPromocion, local);
						int tipoPromo = promoActual.getTipo();
						boolean isPromo = false;
						
						if(tipoPromo != 100 && tipoPromo != 104 && tipoPromo != 105) {
							isPromo = true;
						}
									
						for (int k = 0; k < PRO_PRODUCTO.size(); k++) {
							
							ProrrateoPromocionProductoDTO prorrateoProducto = (ProrrateoPromocionProductoDTO) PRO_PRODUCTO.get(k);
							if ((prorrateoProducto.getCodigo() == codigoPromocion) && (PROMO_VALOR_COLABORADOR.get(String.valueOf(codigoPromocion)) == null)) {
								PROMO_VALOR_COLABORADOR.put(String.valueOf(codigoPromocion), new Object());
								descuentoActual += prorrateoProducto.getDescuento();
								cantRestante -= prorrateoProducto.getListadoProductos().size(); 
								cantPromocion += prorrateoProducto.getListadoProductos().size();
								
								aplicoProrrateo=true;
								aplicoPromo = (codigoPromocion == codPromoAnterior)? true:false; //fvasquez 03092013
								codPromoAnterior = codigoPromocion; //fvasquez 03092013
							}
						}
						
						if (PRO_PRODUCTO.size()== 0) {//fvr oct 
							aplicoProrrateo=true;
						}
						
						// se revisa la parte promocional 
						if ((producto.isAfectoDescColaborador() && cantPromocion > 0) && (aplicoProrrateo)) {
							dcto = calculaDesc(tramo, precioUnitario, cantPromocion, comprasAcumuladas + compraActual);
						}
						
						if ((isPromo && descuentoActual < (long)dcto))  {
							cantAplica += cantPromocion;
							
							for (int k = 0; k < PRO_PRODUCTO.size(); k++) {
								ProrrateoPromocionProductoDTO prorrateoProducto = (ProrrateoPromocionProductoDTO) PRO_PRODUCTO.get(k);
								
								if (prorrateoProducto.getCodigo() == codigoPromocion) {
									PRO_PRODUCTO.remove(k);
									break;
								}
							}
							producto.getPromocion().remove(j);
							dctoTotal += (long)dcto; 
							
						} else {
							//fvasquez 03092013 Evalúa si ya se aplicó promo al mismo producto, para que no sume dsctos por todos los prdctos
							if ((!aplicoPromo) && (descuentoActual > dcto) && (dcto != 0) && (aplicoProrrateo)){
								dctoTotal += descuentoActual; 
								aplicoPromo=true; //fvr oct 7 10 2013
							}//fvasquez 03092013
							
							// se revisa el resto
							if ((isPromo && producto.isAfectoDescColaborador() && cantRestante > 0) && (aplicoProrrateo)) { 
								dcto = calculaDesc(tramo, precioUnitario, cantRestante, comprasAcumuladas + compraActual);
								dctoTotal += dcto;
								cantAplica += cantRestante;
							}
							
							//fvr oct// Suma los descuentos para productos NO afectos a benf. colaborador
							if ((descuentoActual > 0 && !producto.isAfectoDescColaborador())) { 
								dctoTotal += descuentoActual; 
							}
						}
						
						if (cantAplica > 0) {
							// agrega promocion colaborador
							ProrrateoProductoDTO prorrateoProducto = new ProrrateoProductoDTO();
							prorrateoProducto.setCantPeso(producto.getCantidad());
							prorrateoProducto.setCodigo(producto.getCodigo());
							prorrateoProducto.setDepto(producto.getDepto());
							prorrateoProducto.setFlagVenta(producto.getFlagVenta());
							prorrateoProducto.setPrecio(producto.getPrecio());
							prorrateoProducto.setProrrateo((long) dcto);

							if (tramo == 1) {
								productosTramo1.add(prorrateoProducto);
								dctoTramo1 += (long) dcto;
								
							} else {
								productosTramo2.add(prorrateoProducto);
								dctoTramo2 += (long) dcto;
							}
							compraActual += producto.getPrecio();
						}
					}
					
				} else if ((producto.isAfectoDescColaborador() && cantRestante > 0)) {
					dcto = calculaDesc(tramo, precioUnitario, cantRestante,	comprasAcumuladas + compraActual);
					dctoTotal += dcto;
					cantAplica += cantRestante;
					
					if (cantAplica > 0) {
						ProrrateoProductoDTO prorrateoProducto = new ProrrateoProductoDTO();
						prorrateoProducto.setCantPeso(producto.getCantidad());
						prorrateoProducto.setCodigo(producto.getCodigo());
						prorrateoProducto.setDepto(producto.getDepto());
						prorrateoProducto.setFlagVenta(producto.getFlagVenta());
						prorrateoProducto.setPrecio(producto.getPrecio());
						prorrateoProducto.setProrrateo((long) dcto);

						if (tramo == 1) {
							productosTramo1.add(prorrateoProducto);
							dctoTramo1 += (long) dcto;
						} else {
							productosTramo2.add(prorrateoProducto);
							dctoTramo2 += (long) dcto;
						}
						compraActual += producto.getPrecio();
					}
				
			} else if ((producto.isAfectoDescColaborador() && cantRestante > 0)) {
				dcto = calculaDesc(tramo, precioUnitario, cantRestante,	comprasAcumuladas + compraActual);
				dctoTotal += dcto;
				cantAplica += cantRestante;
				
				if (cantAplica > 0) {
					ProrrateoProductoDTO prorrateoProducto = new ProrrateoProductoDTO();
					prorrateoProducto.setCantPeso(producto.getCantidad());
					prorrateoProducto.setCodigo(producto.getCodigo());
					prorrateoProducto.setDepto(producto.getDepto());
					prorrateoProducto.setFlagVenta(producto.getFlagVenta());
					prorrateoProducto.setPrecio(producto.getPrecio());
					prorrateoProducto.setProrrateo((long) dcto);

					if (tramo == 1) {
						productosTramo1.add(prorrateoProducto);
						dctoTramo1 += (long) dcto;
					} else {
						productosTramo2.add(prorrateoProducto);
						dctoTramo2 += (long) dcto;
					}
					compraActual += producto.getPrecio();
				}
			}
		}
		
		
		DecimalFormat formato = new DecimalFormat("###,###");

		if (productosTramo1.size() > 0) {
			ProrrateoPromocionProductoDTO prorrateoPromocion = new ProrrateoPromocionProductoDTO();
			prorrateoPromocion.setCodigo(-1);
			prorrateoPromocion.setDescripcion("Descuento colaborador Cencosud "	
					+ formato.format(descTramo1) + "%");	
			prorrateoPromocion.setDescuento(dctoTramo1);
			prorrateoPromocion.setListadoProductos(productosTramo1);
			prorrateoPromocion.setTipo("C");
			PRO_PRODUCTO.add(prorrateoPromocion);
		}
		
		if (productosTramo2.size() > 0) {
			ProrrateoPromocionProductoDTO prorrateoPromocion = new ProrrateoPromocionProductoDTO();
			prorrateoPromocion.setCodigo(-1);
			prorrateoPromocion.setDescripcion("Descuento colaborador Cencosud "
					+ formato.format(descTramo2)
					+ "% aplicado al monto que excede $"
					+ formato.format(limMensual) + " de compra mensual");
			prorrateoPromocion.setDescuento(dctoTramo2);
			prorrateoPromocion.setListadoProductos(productosTramo2);
			prorrateoPromocion.setTipo("C");
			PRO_PRODUCTO.add(prorrateoPromocion);
		}
		
		return dctoTotal;
	}
    //]20121130avc

	/**
     * @return
     */
    private double calculaDesc(int tramo, long precioUnitario, long cant, long comprasAcumuladas) {
        double dcto;
//        long precioDescuento;
        if (tramo == 1) {
            dcto = ((double) precioUnitario * cant * descTramo1) / 100D;
            dcto = (dcto + 0.5D) / 1.0D;
//            precioDescuento = precioUnitario * cant - (long) dcto;

            if (comprasAcumuladas + precioUnitario * cant > limMensual) {
                double remanente = limMensual - comprasAcumuladas;
                if (remanente > 0) {
                    dcto = ((double) remanente * descTramo1) / 100D;
                    dcto += ((double) (precioUnitario * cant - remanente) * descTramo2) / 100D;
                } else
                	dcto = ((double) precioUnitario * cant * descTramo2) / 100D;
                dcto = (dcto + 0.5D) / 1.0D;
//                precioDescuento = precioUnitario * cant - (long) dcto;
//                tramo = 2;
            }
        } else {
            dcto = ((double) precioUnitario * cant * descTramo2) / 100D;
            dcto = (dcto + 0.5D) / 1.0D;
//            precioDescuento = precioUnitario * cant - (long) dcto;
        }
        return dcto;
    }

    //]20121108avc

    private long calcula(String fpago, int cuotas, List tcp, int canal, int local) {
        long dcto = 0L;
        long total_dcto = 0L;
        int promoMedioPago = 0;

        PromoDAO dao = (PromoDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPromoDAO();

        //logger.debug("Medio de pago del pedido: " + fpago + " , cuotas" + cuotas);
        // Rescatamos el medio de pago de la promocion
        PromoMedioPagoDTO pr_mp;
        try {
            pr_mp = dao.getPromoMedioPagoByMPJmcl(fpago, cuotas);
            if (pr_mp.getMp_promo() != null) {
                promoMedioPago = Integer.parseInt(pr_mp.getMp_promo());
            }
        } catch (PromoDAOException e) {
            logger.error("Error: ",e);
        }

        /*logger.debug("INICIA LIBRERIA | FECHA=06/JUN/2008 | VERSION=1.10");
        logger.debug("fpago=" + fpago);
        logger.debug("cuotas=" + cuotas);
        logger.debug("canal=" + canal);
        logger.debug("local=" + local);*/
        if (promoMedioPago < 0 || cuotas < 0 || canal <= 0 || local <= 0) {
            //logger.debug("Error parametros ingresados");
            return total_dcto;
        } else {
            CONDICION = new ArrayList();
            BENEFICIO = new ArrayList();
            PROMOCION = new ArrayList();
            PRO_PRODUCTO = new ArrayList();
            PRO_SECCION = new ArrayList();
            validaPromoActivas(local);
            quitaProductosAnulados();
            generarMatrices(local);
            PRO_TCP = tcp;
            dcto = calculaPromoProducto(promoMedioPago, cuotas, tcp, canal, local);
            //logger.debug("dcto producto=" + dcto);
            total_dcto += dcto;
            tcp = PRO_TCP;
            generaMatrizProrrateoProducto();
            dcto = calculaPromoSeccion(promoMedioPago, cuotas, tcp, canal, local);
            //logger.debug("dcto seccion=" + dcto);
            total_dcto += dcto;

            //logger.debug("FIN LIBRERIA");
            return total_dcto;
        }
    }

    private long calculaPromoSeccion(int fpago, int cuotas, List TCP, int canal, int local) {
        List listaTcp = new ArrayList();
        int dia = 0;
        long total_dcto;
        long dcto = total_dcto = 0L;
        dia = Util.getDayOfWeek();
        if (dia == 1)
            dia = 906;
        else
            dia = 900 + (dia - 2);
        logger.debug("(seccion) dia=" + dia);
        IrsPromosDaoCtrl dao = new IrsPromosDaoCtrl();
        SECCION = new ArrayList();
        for (int i = 0; i < PRODUCTOS.size(); i++)
            acumulaPorSeccion((ProductoDTO) PRODUCTOS.get(i));

        logger.debug("VECTOR PRO_PRODUCTO=" + PRO_PRODUCTO.size());
        for (int i = 0; i < PRO_PRODUCTO.size(); i++) {
            ProrrateoPromocionProductoDTO promo = (ProrrateoPromocionProductoDTO) PRO_PRODUCTO.get(i);
            descuentaProductosProrrateados(promo.getListadoProductos());
        }

        logger.debug("VECTOR SECCION = " + SECCION.size());
        for (int i = 0; i < SECCION.size(); i++) {
            SeccionDTO seccion = (SeccionDTO) SECCION.get(i);
            logger.debug("[" + i + "] SECCION = " + seccion.getSeccion() + " | MONTO = " + seccion.getMonto());
            MatrizSeccionEntity matsec;
            try {
                matsec = dao.getMatrizSeccion(seccion.getSeccion(), local);
            } catch (IrsPromocionException e) {
                matsec = null;
            }
            if (matsec != null) {
                logger.debug("(matsec) seccion  = " + matsec.getSeccion());
                PromocionEntity promoBenef = null;
                if (matsec.isEsp1()) {
                    promoBenef = getPromoSeccion(907, local);
                    logger.debug("(seccion) ESPECIAL 1");
                } else if (matsec.isEsp2()) {
                    promoBenef = getPromoSeccion(908, local);
                    logger.debug("(seccion) ESPECIAL 2");
                } else if (matsec.isEsp3()) {
                    promoBenef = getPromoSeccion(909, local);
                    logger.debug("(seccion) ESPECIAL 3");
                } else if (matsec.isEsp4()) {
                    promoBenef = getPromoSeccion(910, local);
                    logger.debug("(seccion) ESPECIAL 4");
                } else if (matsec.isEsp5()) {
                    promoBenef = getPromoSeccion(911, local);
                    logger.debug("(seccion) ESPECIAL 5");
                } else if (matsec.isEsp6()) {
                    promoBenef = getPromoSeccion(912, local);
                    logger.debug("(seccion) ESPECIAL 6");
                } else if (matsec.isEsp7()) {
                    promoBenef = getPromoSeccion(913, local);
                    logger.debug("(seccion) ESPECIAL 7");
                }else if (matsec.isEsp8()) {
                    promoBenef = getPromoSeccion(914, local);
                    logger.debug("(seccion) ESPECIAL 8");
                }
                
                boolean seccionProducto=false; 
                
                if (promoBenef != null) {                	
                	Iterator it = (Iterator) promoBenef.getSecciones().iterator();
                	while(it.hasNext()){
                		String secc = (String)it.next();
                	 	if(secc != null && secc.equals(String.valueOf(seccion.getSeccion()))){
                	 		seccionProducto=true;
                	 		break;
                	 	}
                	}
                } 
                
                dcto = 0L;
                if (promoBenef != null && seccionProducto) {
                    logger.debug("(especial) -----------------------------------------------");
                    logger.debug("(especial) PROMO = " + promoBenef.getCodigo());
                    logger.debug("(semana) seccion = " + seccion.getSeccion());
                    ReglaDescuentoSeccion reglaSeccion = new ReglaDescuentoSeccion();
                    reglaSeccion.initReglaPromocion(promoBenef, seccion);
                    ProrrateoPromocionSeccionDTO prorrateoPromocionDTO = reglaSeccion.calculaDcto(fpago, cuotas, TCP, canal, local);
                    if (prorrateoPromocionDTO != null) {
                        dcto = prorrateoPromocionDTO.getDescuento();
                        logger.debug("(especial) dcto = " + promoBenef.getCodigo());
                        ProrrateoSeccionDTO secdto = (ProrrateoSeccionDTO) prorrateoPromocionDTO.getListadoSeccion().get(0);
                        secdto.setListadoProductos(obtieneProrrateoProducto(seccion, dcto));
                        List listsec = new ArrayList();
                        listsec.add(secdto);
                        prorrateoPromocionDTO.setListadoSeccion(listsec);
                        addProrrateoSeccion(prorrateoPromocionDTO);
                        int tcp = reglaSeccion.getTcp();
                        int codigo = promoBenef.getCodigo();
                        if (tcp > 0) {
                            boolean existe = false;
                            for (int w = 0; w < listaTcp.size(); w++) {
                                TcpPromoSeccion tcp_promo = (TcpPromoSeccion) listaTcp.get(w);
                                if (tcp_promo.getPromo() != codigo || tcp_promo.getTcp() != tcp)
                                    continue;
                                existe = true;
                                break;
                            }

                            if (!existe) {
                                TcpPromoSeccion tcp_promo = new TcpPromoSeccion();
                                tcp_promo.setPromo(codigo);
                                tcp_promo.setTcp(tcp);
                                listaTcp.add(tcp_promo);
                            }
                        }
                    }
                }
                total_dcto += dcto;
                dcto = 0L;
                if (promoBenef == null) {
                    promoBenef = getPromoSeccion(dia, local);
                } else {
                    logger.debug("(especial) acumulable = " + (promoBenef.getVersion() != 4 ? "no" : "si"));
                    if (promoBenef.getVersion() == 4)
                        promoBenef = getPromoSeccion(dia, local);
                    else
                        promoBenef = null;
                }
                
                seccionProducto=false; 
                
                if (promoBenef != null) {                	
                	Iterator it = (Iterator) promoBenef.getSecciones().iterator();
                	while(it.hasNext()){
                		String secc = (String)it.next();
                	 	if(secc != null && secc.equals(String.valueOf(seccion.getSeccion()))){
                	 		seccionProducto=true;
                	 		break;
                	 	}
                	}
                }                
                
                if (promoBenef != null && seccionProducto) {
                    logger.debug("(semana) -----------------------------------------------");
                    logger.debug("(semana) PROMO = " + promoBenef.getCodigo());
                    logger.debug("(semana) seccion = " + seccion.getSeccion());
                    ReglaDescuentoSeccion reglaSeccion = new ReglaDescuentoSeccion();
                    reglaSeccion.initReglaPromocion(promoBenef, seccion);
                    ProrrateoPromocionSeccionDTO prorrateoPromocionDTO = reglaSeccion.calculaDcto(fpago, cuotas, TCP, canal, local);
                    if (prorrateoPromocionDTO != null) {
                        dcto = prorrateoPromocionDTO.getDescuento();
                        logger.debug("(semana) dcto = " + dcto);
                        ProrrateoSeccionDTO secdto = (ProrrateoSeccionDTO) prorrateoPromocionDTO.getListadoSeccion().get(0);
                        secdto.setListadoProductos(obtieneProrrateoProducto(seccion, dcto));
                        List listsec = new ArrayList();
                        listsec.add(secdto);
                        prorrateoPromocionDTO.setListadoSeccion(listsec);
                        addProrrateoSeccion(prorrateoPromocionDTO);
                        int tcp = reglaSeccion.getTcp();
                        int codigo = promoBenef.getCodigo();
                        if (tcp > 0) {
                            boolean existe = false;
                            for (int w = 0; w < listaTcp.size(); w++) {
                                TcpPromoSeccion tcp_promo = (TcpPromoSeccion) listaTcp.get(w);
                                if (tcp_promo.getPromo() != codigo || tcp_promo.getTcp() != tcp)
                                    continue;
                                existe = true;
                                break;
                            }

                            if (!existe) {
                                TcpPromoSeccion tcp_promo = new TcpPromoSeccion();
                                tcp_promo.setPromo(codigo);
                                tcp_promo.setTcp(tcp);
                                listaTcp.add(tcp_promo);
                            }
                        }
                    }
                }
                total_dcto += dcto;
            }
        }

        try {
            for (int w = 0; w < listaTcp.size(); w++) {
                TcpPromoSeccion tcp_promo = (TcpPromoSeccion) listaTcp.get(w);
                int tcp = tcp_promo.getTcp();
                for (int z = 0; z < TCP.size(); z++) {
                    TcpClienteDTO tcpDto = (TcpClienteDTO) TCP.get(z);
                    if (tcpDto.getTcp() != tcp)
                        continue;
                    int cant = tcpDto.getCant() + 1;
                    tcpDto.setCant(cant);
                    TCP.set(z, tcpDto);
                    break;
                }

            }

        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        PRO_TCP = TCP;
        return total_dcto;
    }

    private List obtieneProrrateoProducto(SeccionDTO seccion, long dcto) {
        long acum_sec;
        long acum_prod = acum_sec = 0L;
        int cont_prod = 0;
        List lprod = new ArrayList();
        int sec = seccion.getSeccion();
        acum_sec = seccion.getMonto();
        for (int w = 0; w < PRO_PRODSEC.size(); w++) {
            ProrrateoProductoSeccionDTO pps = (ProrrateoProductoSeccionDTO) PRO_PRODSEC.get(w);
            if (sec == pps.getDepto()) {
                cont_prod++;
                acum_prod += pps.getPrecio() - pps.getProrrateoProducto();
                lprod.add(pps);
            }
        }

        long dcto_p;
        long acum_dcto_p;
        long diff = acum_dcto_p = dcto_p = 0L;
        for (int w = 0; w < lprod.size(); w++) {
            ProrrateoProductoSeccionDTO pps = (ProrrateoProductoSeccionDTO) lprod.get(w);
            long precio = pps.getPrecio() - pps.getProrrateoProducto();
            dcto_p = Util.calculaProrrateo(precio, acum_prod, dcto);
            acum_dcto_p += dcto_p;
            diff = dcto - acum_dcto_p;
            if (w == cont_prod && diff > 0L)
                dcto_p += diff;
            logger.debug("(prorrateo) codigo=" + pps.getCodigo() + "|precio=" + precio + "|dcto_p=" + dcto_p);
            pps.setProrrateoSeccion(dcto_p);
        }

        return lprod;
    }

    private void generaMatrizProrrateoProducto() {
        PRO_PRODSEC = new ArrayList();
        List lista = new ArrayList();
        for (int i = 0; i < PRODUCTOS.size(); i++) {
            ProrrateoProductoSeccionDTO pps = new ProrrateoProductoSeccionDTO();
            ProductoDTO prod = (ProductoDTO) PRODUCTOS.get(i);
            pps.setCodigo(prod.getCodigo());
            pps.setDepto(prod.getDepto());
            pps.setPrecio(prod.getPrecio());
            PRO_PRODSEC.add(pps);
        }

        for (int i = 0; i < PRO_PRODUCTO.size(); i++) {
            ProrrateoPromocionProductoDTO promo = (ProrrateoPromocionProductoDTO) PRO_PRODUCTO.get(i);
            lista = promo.getListadoProductos();
            for (int w = 0; w < lista.size(); w++) {
                ProrrateoProductoDTO p_prod = (ProrrateoProductoDTO) lista.get(w);
                for (int k = 0; k < PRO_PRODSEC.size(); k++) {
                    ProrrateoProductoSeccionDTO pps = (ProrrateoProductoSeccionDTO) PRO_PRODSEC.get(k);
                    if (pps.getCodigo() == p_prod.getCodigo()) {
                        long monto = pps.getProrrateoProducto() + p_prod.getProrrateo();
                        pps.setProrrateoProducto(monto);
                    }
                }

            }

        }

        for (int k = 0; k < PRO_PRODSEC.size(); k++) {
            ProrrateoProductoSeccionDTO pps = (ProrrateoProductoSeccionDTO) PRO_PRODSEC.get(k);
            logger.debug("(prorrateo) " + pps.toString());
        }

    }

    private void addProrrateoSeccion(ProrrateoPromocionSeccionDTO promo2) {
        boolean existe = false;
        List listado1 = new ArrayList();
        int cod2 = promo2.getCodigo();
        logger.debug("(ADD) agregando promocion..." + cod2);
        for (int i = 0; i < PRO_SECCION.size(); i++) {
            ProrrateoPromocionSeccionDTO promo1 = (ProrrateoPromocionSeccionDTO) PRO_SECCION.get(i);
            int cod1 = promo1.getCodigo();
            if (cod1 != cod2)
                continue;
            existe = true;
            long monto = promo1.getDescuento() + promo2.getDescuento();
            promo1.setDescuento(monto);
            listado1 = promo1.getListadoSeccion();
            ProrrateoSeccionDTO sec = (ProrrateoSeccionDTO) promo2.getListadoSeccion().get(0);
            logger.debug("(SECCION) sec=" + sec.getDepto());
            listado1.add(sec);
            promo1.setListadoSeccion(listado1);
            break;
        }

        if (!existe)
            PRO_SECCION.add(promo2);
    }

    private PromocionEntity getPromoSeccion(int tipo, int local) {
        int codigo = 0;
        IrsPromosDaoCtrl dao = new IrsPromosDaoCtrl();
        PromoSeccionEntity promoSec;
        try {
            promoSec = dao.getPromoSeccion(tipo, local, true);
        } catch (IrsPromocionException e) {
            return null;
        }
        PromocionEntity promo = null;
        try {
            
            if(promoSec != null){
        		codigo = promoSec.getCodigo();
        		promo = dao.getPromocion(codigo, local);
        		if(promo != null)
        			promo.setSecciones(promoSec.getSecciones());
        	}          
            
        } catch (IrsPromocionException e) {
            return null;
        }
        if (promo == null)
            return null;
        if (isPromocionVigente(promo, new Date()))
            return promo;
        else
            return null;
    }

    private void descuentaProductosProrrateados(List productos) {
        for (int i = 0; i < productos.size(); i++) {
            ProrrateoProductoDTO prod = (ProrrateoProductoDTO) productos.get(i);
            long monto = prod.getProrrateo();
            int depto = prod.getDepto();
            descuentaPorSeccion(depto, monto);
        }

    }

    private void descuentaPorSeccion(int depto, long monto) {
        for (int i = 0; i < SECCION.size(); i++) {
            SeccionDTO seccion = (SeccionDTO) SECCION.get(i);
            int cod = seccion.getSeccion();
            if (depto != cod)
                continue;
            long dcto = seccion.getMonto() - monto;
            seccion.setMonto(dcto);
            SECCION.set(i, seccion);
            break;
        }

    }

    private void acumulaPorSeccion(ProductoDTO producto) {
        boolean existe = false;
        //[20121119avc
        IrsPromosDaoCtrl irs = new IrsPromosDaoCtrl();
        //]20121119avc

        //        ProductoDTO prod = producto;
        int cod1 = producto.getDepto();
        long monto = producto.getPrecio();
        for (int i = 0; i < SECCION.size(); i++) {
            SeccionDTO seccion = (SeccionDTO) SECCION.get(i);
            int cod2 = seccion.getSeccion();
            if (cod1 != cod2)
                continue;
            monto += seccion.getMonto();
            seccion.setMonto(monto);
            SECCION.set(i, seccion);
            existe = true;
            //[20121119avc
            producto.setAfectoDescColaborador(seccion.isAfectoDescColaborador());
            //]20121119avc
            break;
        }

        if (!existe) {
            SeccionDTO seccion = new SeccionDTO();
            seccion.setSeccion(cod1);
            seccion.setMonto(monto);
            //[20121119avc
            try {
                seccion.setAfectoDescColaborador(irs.isAfectoDescColaborador(cod1));
            } catch (IrsPromocionException e) {
                seccion.setAfectoDescColaborador(false);
            }
            producto.setAfectoDescColaborador(seccion.isAfectoDescColaborador());
            //]20121119avc
            SECCION.add(seccion);
        }

    }

    private long calculaPromoProducto(int fpago, int cuotas, List TCP, int canal, int local) {
        long dcto = 0L;
        List listaTcp = TCP;
        List prorrateo = new ArrayList();
        IrsPromosDaoCtrl irs = new IrsPromosDaoCtrl();
        PromocionEntity promo = null;
        PromocionEntity promoCond1 = null;
        PromocionEntity promoCond2 = null;
        long total_dcto = 0L;
        for (int i = 0; i < BENEFICIO.size(); i++) {
            BeneficioDTO b = (BeneficioDTO) BENEFICIO.get(i);
            int codigo = b.getCodigo();
            int tipo = b.getTipo();
            List productosBeneficio = b.getProductos();
            //logger.debug("(calcula) codigo=" + codigo);
            try {
            	
            	promo = irs.getPromocion(codigo, local);
            
                
            } catch (IrsPromocionException e) {
                promo = null;
            }
            dcto = 0L;
            if (promo != null) {
                if (tipo == 1 || tipo == 2) {
                    ReglaDescuentoSinCondicion reglaDctoSinCondic = new ReglaDescuentoSinCondicion();
                    reglaDctoSinCondic.initReglaPromocion(promo, productosBeneficio);
                    ProrrateoPromocionProductoDTO prorrateoPromocionDTO = reglaDctoSinCondic.calculaDcto(fpago, cuotas, listaTcp, canal, local);
                    if (prorrateoPromocionDTO != null) {
                        dcto = prorrateoPromocionDTO.getDescuento();
                        prorrateo.add(prorrateoPromocionDTO);
                        listaTcp = reglaDctoSinCondic.getListTcp();
                    }
                } else if (tipo == 3 || tipo == 4) {
                    ReglaDescuentoConCondicion reglaDctoConCondic = new ReglaDescuentoConCondicion();
                    reglaDctoConCondic.initReglaPromocion(promo, productosBeneficio);
                    ProrrateoPromocionProductoDTO prorrateoPromocionDTO = reglaDctoConCondic.calculaDcto(fpago, cuotas, listaTcp, canal, local);
                    if (prorrateoPromocionDTO != null) {
                        dcto = prorrateoPromocionDTO.getDescuento();
                        prorrateo.add(prorrateoPromocionDTO);
                        listaTcp = reglaDctoConCondic.getListTcp();
                    }
                } else if (tipo == 11) {
                    ReglaPackMxN reglaPackMxN = new ReglaPackMxN();
                    reglaPackMxN.initReglaPromocion(promo, productosBeneficio);
                    ProrrateoPromocionProductoDTO prorrateoPromocionDTO = reglaPackMxN.calculaDcto(fpago, cuotas, listaTcp, canal, local);
                    if (prorrateoPromocionDTO != null) {
                        dcto = prorrateoPromocionDTO.getDescuento();
                        prorrateo.add(prorrateoPromocionDTO);
                        listaTcp = reglaPackMxN.getListTcp();
                    }
                } else if (tipo == 12) {
                    ReglaMultipack1 reglaMultipack1 = new ReglaMultipack1();
                    reglaMultipack1.initReglaPromocion(promo, productosBeneficio);
                    ProrrateoPromocionProductoDTO prorrateoPromocionDTO = reglaMultipack1.calculaDcto(fpago, cuotas, listaTcp, canal, local);
                    if (prorrateoPromocionDTO != null) {
                        dcto = prorrateoPromocionDTO.getDescuento();
                        prorrateo.add(prorrateoPromocionDTO);
                        listaTcp = reglaMultipack1.getListTcp();
                    }
                } else if (tipo == 102 || tipo == 103 || tipo == 104) {
                    int codCond1 = promo.getCondicion1();
                    if (codCond1 > 0) {
                        ReglaPromocionSimple reglaPromocionSimple = new ReglaPromocionSimple();
                        promoCond1 = getPromoCond(codCond1, local);
                        List productosCondicion1 = getProdCond(codCond1);
                        reglaPromocionSimple.initReglaPromocion(promo, promoCond1, productosBeneficio, productosCondicion1);
                        ProrrateoPromocionProductoDTO prorrateoPromocionDTO = reglaPromocionSimple.calculaDcto(fpago, cuotas, listaTcp, canal, local);
                        if (prorrateoPromocionDTO != null) {
                            dcto = prorrateoPromocionDTO.getDescuento();
                            prorrateo.add(prorrateoPromocionDTO);
                            listaTcp = reglaPromocionSimple.getListTcp();
                        }
                    }
                } else if (tipo == 105) {
                    int codCond1 = promo.getCondicion1();
                    if (codCond1 > 0) {
                        ReglaMultipack2 reglaMultipack2 = new ReglaMultipack2();
                        promoCond1 = getPromoCond(codCond1, local);
                        List productosCondicion1 = getProdCond(codCond1);
                        reglaMultipack2.initReglaPromocion(promo, promoCond1, productosBeneficio, productosCondicion1);
                        ProrrateoPromocionProductoDTO prorrateoPromocionDTO = reglaMultipack2.calculaDcto(fpago, cuotas, listaTcp, canal, local);
                        if (prorrateoPromocionDTO != null) {
                            dcto = prorrateoPromocionDTO.getDescuento();
                            prorrateo.add(prorrateoPromocionDTO);
                            listaTcp = reglaMultipack2.getListTcp();
                        }
                    }
                } else if (tipo == 205) {
                    int codCond1 = promo.getCondicion1();
                    if (codCond1 > 0) {
                        promoCond1 = getPromoCond(codCond1, local);
                        int codCond2 = promoCond1.getCondicion1();
                        if (codCond2 > 0) {
                            promoCond2 = getPromoCond(codCond2, local);
                            ReglaMultipack3 reglaMultipack3 = new ReglaMultipack3();
                            List productosCondicion1 = getProdCond(codCond1);
                            List productosCondicion2 = getProdCond(codCond2);
                            reglaMultipack3.initReglaPromocion(promo, promoCond1, promoCond2, productosBeneficio, productosCondicion1, productosCondicion2);
                            ProrrateoPromocionProductoDTO prorrateoPromocionDTO = reglaMultipack3.calculaDcto(fpago, cuotas, listaTcp, canal, local);
                            if (prorrateoPromocionDTO != null) {
                                dcto = prorrateoPromocionDTO.getDescuento();
                                prorrateo.add(prorrateoPromocionDTO);
                                listaTcp = reglaMultipack3.getListTcp();
                            }
                        }
                    }
                }
                //logger.debug("PROMO=" + promo.getDescripcion() + "|dcto=" + dcto);               
                total_dcto += dcto;
            }
        }

        PRO_TCP = listaTcp;
        if (prorrateo.size() > 0){
            PRO_PRODUCTO = prorrateo;
        }
        return total_dcto;
    }

    private List getProdCond(int codigo) {
        CondicionDTO con = new CondicionDTO();
        List productos = null;
        for (int i = 0; i < CONDICION.size(); i++) {
            con = (CondicionDTO) CONDICION.get(i);
            if (con.getCodigo() != codigo)
                continue;
            productos = con.getProductos();
            break;
        }

        return productos;
    }

    private PromocionEntity getPromoCond(int codigo, int local) {
        PromocionEntity promo = null;
        IrsPromosDaoCtrl irs = new IrsPromosDaoCtrl();
        try {
            promo = irs.getPromocion(codigo, local);
        } catch (IrsPromocionException e) {
            promo = null;
        }
        return promo;
    }

    private void generarMatrices(int local) {
        for (int i = 0; i < PRODUCTOS.size(); i++) {
            ProductoDTO item = (ProductoDTO) PRODUCTOS.get(i);
            List promos_asoc = item.getPromocion();
            for (int j = 0; j < item.getPromocion().size(); j++) {
                int cod_promo = Integer.parseInt(item.getPromocion().get(j).toString());
                //logger.debug("(matriz) promo=" + cod_promo);
                if (cod_promo != 0) {
                    List result = obtieneTipo(cod_promo, local);
                    String tipo_prod = String.valueOf(result.get(0));
                    int tipo_promocion = Integer.parseInt(result.get(1).toString());
                    if (tipo_prod == "C") {
                        if (item.getFlagCantidad() == 'C') {
                            int cant = item.getCantidad();
                            for (int k = 0; k < cant; k++) {
                                ProductoBenConDTO p = new ProductoBenConDTO(item.getCodigo(), item.getDepto(), item.getFlagCantidad(), 1, item.getPrecio() / (long) item.getCantidad());
                                //logger.debug("(cond) prod=" + p.getCodigo() + "|" + p.getCant() + "|" + p.getPrecio());
                                insertaCondicion(cod_promo, tipo_promocion, p);
                            }

                        } else {
                            ProductoBenConDTO p = new ProductoBenConDTO(item.getCodigo(), item.getDepto(), item.getFlagCantidad(), item.getCantidad(), item.getPrecio());
                            insertaCondicion(cod_promo, tipo_promocion, p);
                        }
                        j = promos_asoc.size();
                    }
                    if (tipo_prod == "B") {
                        if (item.getFlagCantidad() == 'C') {
                            int cant = item.getCantidad();
                            for (int k = 0; k < cant; k++) {
                                ProductoBenConDTO p = new ProductoBenConDTO(item.getCodigo(), item.getDepto(), item.getFlagCantidad(), 1, item.getPrecio() / (long) item.getCantidad());
                                //logger.debug("(benef) prod=" + p.getCodigo() + "|" + p.getCant() + "|" + p.getPrecio());
                                insertaBeneficio(cod_promo, tipo_promocion, p);
                            }

                        } else {
                            ProductoBenConDTO p = new ProductoBenConDTO(item.getCodigo(), item.getDepto(), item.getFlagCantidad(), item.getCantidad(), item.getPrecio());
                            insertaBeneficio(cod_promo, tipo_promocion, p);
                        }
                        j = promos_asoc.size();
                    }
                    if (tipo_prod == "E")
                        logger.debug("Promocion asociada: " + cod_promo + " invalida");
                }
            }

        }

    }

    private void insertaCondicion(int cod_promo, int tipo_promo, ProductoBenConDTO p) {
        boolean result = false;
        for (int i = 0; i < CONDICION.size(); i++) {
            CondicionDTO con = (CondicionDTO) CONDICION.get(i);
            if (con.getCodigo() == cod_promo) {
                con.getProductos().add(p);
                CONDICION.set(i, con);
                i = CONDICION.size();
                result = true;
            }
        }

        if (!result) {
            List productos = new ArrayList();
            productos.add(p);
            CondicionDTO con = new CondicionDTO(cod_promo, tipo_promo, productos);
            CONDICION.add(con);
        }
    }

    private void insertaBeneficio(int cod_promo, int tipo_promo, ProductoBenConDTO p) {
        boolean result = false;
        for (int i = 0; i < BENEFICIO.size(); i++) {
            BeneficioDTO ben = (BeneficioDTO) BENEFICIO.get(i);
            if (ben.getCodigo() == cod_promo) {
                ben.getProductos().add(p);
                BENEFICIO.set(i, ben);
                i = BENEFICIO.size();
                result = true;
            }
        }

        if (!result) {
            List productos = new ArrayList();
            productos.add(p);
            BeneficioDTO ben = new BeneficioDTO(cod_promo, tipo_promo, productos);
            BENEFICIO.add(ben);
        }
    }

    private List obtieneTipo(int codigo, int local) {
        List result = new Vector();
        PromocionEntity promo = new PromocionEntity();
        IrsPromosDaoCtrl irs = new IrsPromosDaoCtrl();
        logger.debug("codigo=" + codigo + " local=" + local);
        int tipo;
        try {
            promo = irs.getPromocion(codigo, local);
            tipo = promo.getTipo();
        } catch (IrsPromocionException e) {
            promo = null;
            tipo = 999;
        }
        logger.debug("codigo=" + promo.getCodigo() + " tipo=" + promo.getTipo());
        if (tipo < 100 || tipo == 102 || tipo == 103 || tipo == 104 || tipo == 105 || tipo == 205) {
            guardarRegistroPromocion(promo);
            result.add("B");
        } else if (tipo == 100 || tipo == 101 || tipo == 100 || tipo == 200) {
            guardarRegistroPromocion(promo);
            result.add("C");
        } else {
            result.add("E");
        }
        result.add(String.valueOf(promo.getTipo()));
        return result;
    }

    private void guardarRegistroPromocion(PromocionEntity reg) {
        boolean result = false;
        for (int i = 0; i < PROMOCION.size(); i++) {
            PromocionEntity reg2 = (PromocionEntity) PROMOCION.get(i);
            if (reg2 == reg)
                result = true;
        }

        if (!result)
            PROMOCION.add(reg);
    }

    private void quitaProductosAnulados() {
        Collections.reverse(PRODUCTOS);
        for (int i = 0; i < PRODUCTOS.size(); i++) {
            ProductoDTO item1 = (ProductoDTO) PRODUCTOS.get(i);
            if (item1.getFlagVenta() == 'A') {
                for (int j = i + 1; j < PRODUCTOS.size(); j++) {
                    ProductoDTO item2 = (ProductoDTO) PRODUCTOS.get(j);
                    if (item1.getCodigo() == item2.getCodigo() && item2.getFlagVenta() == 'V')
                        if (item1.getCantidad() == item2.getCantidad()) {
                            PRODUCTOS.remove(i);
                            PRODUCTOS.remove(j - 1);
                        } else if (item2.getCantidad() > item1.getCantidad()) {
                            item2.setCantidad(item2.getCantidad() - item1.getCantidad());
                            item2.setPrecio(item2.getPrecio() - item1.getPrecio());
                            PRODUCTOS.set(j, item2);
                            PRODUCTOS.remove(i);
                        } else if (item2.getCantidad() < item1.getCantidad()) {
                            item1.setCantidad(item1.getCantidad() - item2.getCantidad());
                            item1.setPrecio(item1.getPrecio() - item2.getPrecio());
                            PRODUCTOS.set(i, item1);
                            PRODUCTOS.remove(j);
                            i--;
                        } else {
                            PRODUCTOS.remove(i);
                        }
                }

            }
        }

        Collections.reverse(PRODUCTOS);
    }

    private void validaPromoActivas(int local) {
        boolean vigente = false;
        boolean formato = false;
        //logger.debug("size=" + PRODUCTOS.size());
        for (int cont = 0; cont < PRODUCTOS.size(); cont++) {
            ProductoDTO prod = (ProductoDTO) PRODUCTOS.get(cont);
            vigente = validaVigencia(prod, local);
            formato = validaFormato(prod);
            //logger.debug("[" + cont + "] valida? " + vigente + "  formato? " + formato);
            if (!vigente || !formato) {
                //logger.debug("Producto con datos invalido, se remueve de matriz de trabajo");
                PRODUCTOS.remove(cont);
                cont--;
            }
        }

    }

    private boolean validaFormato(ProductoDTO producto) {
        boolean result = true;
        if (producto.getCodigo() <= 0L) {
            logger.debug("Datos productos invalido!!, codigo=" + producto.getCodigo());
            result = false;
        }
        if (producto.getDepto() <= 0) {
            logger.debug("Datos productos invalido!!, depto=" + producto.getDepto());
            result = false;
        }
        if (producto.getFlagVenta() != 'V' && producto.getFlagVenta() != 'A') {
            logger.debug("Datos productos invalido!!, flag venta=" + producto.getFlagVenta());
            result = false;
        }
        if (producto.getFlagCantidad() != 'C' && producto.getFlagCantidad() != 'P') {
            logger.debug("Datos productos invalido!!, flag cantidad=" + producto.getFlagCantidad());
            result = false;
        }
        if (producto.getCantidad() <= 0) {
            logger.debug("Datos productos invalido!!, cantidad=" + producto.getCantidad());
            result = false;
        }
        if (producto.getPrecio() <= 0L) {
            logger.debug("Datos productos invalido!!, precio=" + producto.getPrecio());
            result = false;
        }
        return result;
    }

    private boolean validaVigencia(ProductoDTO producto, int local) {
        IrsPromosDaoCtrl irs = new IrsPromosDaoCtrl();
        List listado = producto.getPromocion();
        if (listado == null)
            return false;
        for (int i = 0; i < listado.size(); i++) {
            int codigo = Integer.parseInt(String.valueOf(listado.get(i)));
            logger.debug("(vigencia) Validando promocion=" + codigo);
            PromocionEntity promo;
            try {
                promo = irs.getPromocion(codigo, local);
            } catch (IrsPromocionException e) {
                e.printStackTrace();
                return false;
            }
            if (promo != null)
                return isPromocionVigente(promo, new Date());
        }

        return true;
    }

    private boolean isPromocionVigente(PromocionEntity promo, Date fecha) {
        boolean vigente = false;
        long inicio;
        long fin;
        long actual = inicio = fin = 0L;
        actual = Long.parseLong(Util.getDate(fecha).substring(0, 14));
        logger.debug("(vigencia) actual=" + actual);
        inicio = Long.parseLong(Util.getDate(promo.getFechaInicio()));
        logger.debug("(vigencia) inicio=" + inicio);
        fin = Long.parseLong(Util.getDate(promo.getFechaTermino()));
        logger.debug("(vigencia) termino=" + fin);
        if (actual >= inicio && actual <= fin)
            vigente = true;
        logger.debug("(vigencia) promo " + promo.getCodigo() + " vigente? " + vigente);
        return vigente;
    }

    private void validaPromoAsociadas(ProductoDTO producto) {
        List listado = new ArrayList();
        boolean flag = false;
        logger.debug("INSERTANDO PRODUCTO = " + producto.getCodigo());
        listado = producto.getPromocion();
        try {
            for (int i = 0; i < listado.size(); i++) {
                int codigo = 0;
                try {
                    codigo = Integer.parseInt(String.valueOf(listado.get(i)));
                } catch (Exception e) {
                    logger.debug("Error en codigo de promocion asociada al producto, promo=" + String.valueOf(listado.get(i)));
                    break;
                }
                logger.debug("PROMO = " + codigo);
                if (codigo < 0)
                    continue;
                insertaProducto(producto);
                flag = true;
                i = listado.size();
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Error al insertar producto, en ciclo de busqueda por promo");
        }
        if (!flag)
            try {
                if (listado.size() == 0)
                    insertaProducto(producto);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("Error al insertar producto a matriz de productos");
            }
    }

    private void insertaProducto(ProductoDTO producto) throws Exception {
        try {
            PRODUCTOS.add(producto);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List getProrrateoProducto() {
        return PRO_PRODUCTO;
    }

    public List getProrrateoSeccion() {
        return PRO_SECCION;
    }

    public List getListadoTcp() {
        return PRO_TCP;
    }

    Logging logger;
    
    private HashMap PROMO_VALOR_COLABORADOR=new HashMap();

    private List PRODUCTOS;

    private List CONDICION;

    private List BENEFICIO;

    private List PROMOCION;

    private List SECCION;

    private List PRO_PRODUCTO;

    private List PRO_SECCION;

    private List PRO_PRODSEC;

    private List PRO_TCP;
}