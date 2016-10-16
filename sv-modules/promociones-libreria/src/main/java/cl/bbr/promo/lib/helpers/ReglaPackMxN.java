package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.ProductoBenConDTO;
import cl.bbr.promo.lib.dto.ProrrateoProductoDTO;
import cl.bbr.promo.lib.dto.ProrrateoPromocionProductoDTO;
//import cl.bbr.promo.lib.dto.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            Tcp, TipoPromocionSimple, FormaPago

public class ReglaPackMxN extends Tcp implements TipoPromocionSimple
{

    Logging logger;
    private PromocionEntity regPromo;
    private List productos;
    private List listTcp;
    private int contador;
    
    public ReglaPackMxN()
    {
        logger = new Logging(this);
    }

    public void initReglaPromocion(PromocionEntity promo, List productos)
    {
        regPromo = promo;
        this.productos = productos;
        listTcp = new ArrayList();
        contador =0;
    }

    //refactoring Rodrigo Madrid 10-05-2013
    public ProrrateoPromocionProductoDTO calculaDcto(int fpago, int cuotas, List TCP, int canal, int local){
        ProrrateoPromocionProductoDTO prorrateoPorPromocion = new ProrrateoPromocionProductoDTO();
        List prorrateoProducto = new ArrayList();
        prorrateoPorPromocion.setTipo("P");
        prorrateoPorPromocion.setCodigo(regPromo.getCodigo());
        prorrateoPorPromocion.setDescripcion(regPromo.getDescripcion());
        prorrateoPorPromocion.setDescuento(0L);
       //fvasquez 03092013 Mejora el cálculo evitando que se pierdan decimales
        double total_dcto = 0L; 
        double dcto = 0L;
        //fvasquez 03092013 Mejora el cálculo evitando que se pierdan decimales
        
        listTcp = TCP;
        if(canal != regPromo.getCanal())
            return null;
        FormaPago fp = new FormaPago();
        int beneficio;
        int tcp;
        if(fp.obtieneBeneficio(regPromo, fpago, cuotas, listTcp))
        {
            beneficio = (int)fp.getBeneficio();
            tcp = fp.getTcp();
        } else{
            return null;
        }
        String factor = new String();
        if(beneficio < 1000)
            factor = "0";
        factor = factor + String.valueOf(beneficio);
        int M = Integer.parseInt(factor.substring(0, 2));
        int N = Integer.parseInt(factor.substring(2, 4));
        Map prodCantidad = new HashMap();
        //no vale la pena ordenar, es mejor contar y aplicar descuento
        for(int j = 0; j < productos.size(); j++){
            ProductoBenConDTO prod1 = (ProductoBenConDTO)productos.get(j);
            int cant = 0 ;
            String val = (String)prodCantidad.get(Long.toString(prod1.getCodigo()));
            if (val !=null){
            	cant = prod1.getCant() + Integer.parseInt(val);
            }else{
            	cant = prod1.getCant() ;
            }
            //variables de tipo int, solo pueden retornar valores de tipo int
            prodCantidad.put(Long.toString(prod1.getCodigo()), Integer.toString(cant));
        }
        long codigo = 0;
        //calculo de prorrateo por producto
        for(int i = 0; i < productos.size(); i++){
            int cantTcp = getCantidadTCP(listTcp, tcp);
            //logger.debug("tcp=" + tcp + " | cantTcp=" + cantTcp);
            ProductoBenConDTO prod1 = (ProductoBenConDTO)productos.get(i);
            long cantidad = 0;
            if (prodCantidad.get(Long.toString(prod1.getCodigo()))!=null){
            	cantidad = Long.parseLong(prodCantidad.get(Long.toString(prod1.getCodigo())).toString());
//            	System.out.println("cantidad de prods "+cantidad);
            }
//            System.out.println("prod1.getPrecio() "+prod1.getPrecio());
            long maximoAfectados = 1000000;
            //usando la cantidad podemos saber cuantos productos deben ser afectados por la promocion y calcular un porcentaje que definira el precio
            if ((cantidad/M)>cantTcp && cantTcp!=0 )  
            	maximoAfectados  = (cantTcp*M);//de este modo no calcula si no quedan unidades en la promo, solo aplica a cupones
            //si llegasen a usar cupones y no quedaran unidades para la promocion debe detenerse el el descuento por este concepto            
            if(cantTcp <= 0 && tcp != 0)
                break;
            double cantidadD = cantidad;
            double ND = N;
            double MD = M;
			double afectadosPromoD = (cantidadD / MD) ;
//			System.out.println("afectadosPromoD "+afectadosPromoD);
//			System.out.println("maximoAfectados "+maximoAfectados);
			if (afectadosPromoD>maximoAfectados){
				afectadosPromoD=maximoAfectados;//se restringe el maximo de productos afectados
			}
			long afectadosPromo = (long)afectadosPromoD;
/*            System.out.println("N:"+N+" M:"+M+" N/M "+(N / M));
            System.out.println("ND:"+ND+" MD:"+MD+" ND/MD "+(ND / MD));*/
            //se calcula los descuentos
            long totalNoAfectados = (long)(cantidad-afectadosPromo)*prod1.getPrecio();//total de los descuentos
            long totalAfectados = (long)(prod1.getPrecio()*afectadosPromo);//total sin considerar descuentos
//            System.out.println("totalNoAfectados: "+totalNoAfectados+"; totalAfectados: "+totalAfectados);
            
          //fvasquez 03092013 Mejora el cálculo evitando que se pierdan decimales
              dcto  = ((prod1.getPrecio() * ((MD-ND)* afectadosPromo))/cantidadD);
          //fvasquez 03092013 Mejora el cálculo evitando que se pierdan decimales
              
/*            System.out.println("(MD-ND) "+(MD-ND));
            System.out.println("afectadosPromo "+afectadosPromo);
            System.out.println("(MD-ND)* afectadosPromoD "+(MD-ND)* afectadosPromo);
            System.out.println("(prod1.getPrecio() * ((MD-ND)* afectadosPromo) "+(prod1.getPrecio() * ((MD-ND)* afectadosPromo)));
            System.out.println("(prod1.getPrecio() * ((MD-ND)* afectadosPromo))/cantidad "+(prod1.getPrecio() * ((MD-ND)* afectadosPromo))/cantidad);
*/            
            //esto debe calcular el descuento para una sola unidad, no es muy preciso ya que todas las variables son enteros
            dcto = (dcto>0)?dcto:0;//evita precios negativos 
            //es tan mal sistema como el actual, no diferencia productos, pero sera suficiente
            
//            System.out.println("descuento "+dcto);
        	ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
        	total_dcto =total_dcto + dcto;
            pp = new ProrrateoProductoDTO();
            pp.setCodigo(prod1.getCodigo());
            pp.setCantPeso(prod1.getCant());
            pp.setDepto(prod1.getDepto());
            pp.setFlagVenta(prod1.getFlag());
            pp.setPrecio(prod1.getPrecio());
            pp.setProrrateo((long)dcto); //fvasquez 03092013 Mejora el cálculo evitando que se pierdan decimales
            prorrateoProducto.add(pp);
//            System.out.println("**********************fin de aplicacion para producto "+prod1.getCodigo()+"***********************");
            boolean agrupado=false;
            if(dcto == 0){
            	 agrupado = agrupa(productos,i,prorrateoProducto,M,N,total_dcto,pp.getDepto());
            	 if(agrupado){
            		 dcto = 1;
            	 }
            }
            if(dcto > 0L&&(i-(i/M))==0){
            	listTcp = quemaTCP(listTcp, tcp);
            } 
        }
        for(int i=0;i<prorrateoProducto.size();i++){
        	if(((ProrrateoProductoDTO)prorrateoProducto.get(i)).isDesc()){
        		total_dcto+= ((ProrrateoProductoDTO)prorrateoProducto.get(i)).getProrrateo();
        	}
        }
//        System.out.println("total_dcto "+total_dcto);
        if(total_dcto > 0L){
            prorrateoPorPromocion.setDescuento((long)total_dcto); //fvasquez 03092013 Mejora el cálculo evitando que se pierdan decimales
            prorrateoPorPromocion.setListadoProductos(prorrateoProducto);
            return prorrateoPorPromocion;
        } else{
            return null;
        }
    }

    private boolean agrupa(List productos2, int indice, List prorrateoProducto,int M,int N,double tl_dcto, int seccion) {
		// TODO Apéndice de método generado automáticamente
		int cantidad = 0;
		String indicesDescuentos = "";
		int descontar = M-N;
    	boolean agrupado=false;
		for(int i=0; i < prorrateoProducto.size(); i ++){
			ProrrateoProductoDTO prod = (ProrrateoProductoDTO)prorrateoProducto.get(i);
			if(prod.getProrrateo() == 0 && !prod.isDesc()){
				if(prod.getDepto() == seccion){
					cantidad += prod.getCantPeso();
					if(indicesDescuentos.equals("")){
						indicesDescuentos +=i;
					}else{
						indicesDescuentos += ","+i;
					}	
					if(cantidad >= M){
						prorrateoProducto = prorratear(prorrateoProducto,M,N,indicesDescuentos,tl_dcto);
						cantidad = 0;
						agrupado = true;
					}
				}	
			}
		}
    	return agrupado;
	}

	private List prorratear(List prorrateoProducto, int m, int n,
			String indicesDescuentos,double total_dcto) {
		String indices[] = indicesDescuentos.split(",");
		long precios[]= new long[indices.length];
		int contadorMenores=m-n;
		contador++;
		int temp;
		for(int i =0;i<indices.length;i++){
			((ProrrateoProductoDTO)prorrateoProducto.get(Integer.valueOf(indices[i]).intValue())).setDesc(true);
			((ProrrateoProductoDTO)prorrateoProducto.get(Integer.valueOf(indices[i]).intValue())).setIndiceAgrup(contador);
			precios[i]=((ProrrateoProductoDTO)prorrateoProducto.get(Integer.valueOf(indices[i]).intValue())).getPrecio();
		}	
		for(int z=1;z<precios.length;z++){
			for(int x=0;x<precios.length-1;x++){
				if(precios[x] > precios[x+1]){
					temp = Integer.valueOf(indices[x]).intValue();
					indices[x]= indices[x+1];
					indices[x+1]=String.valueOf(temp);	
					long tempL = precios[x];
					precios[x] = precios[x+1];
					precios[x+1]= tempL;
				}
			}
		}
		for(int i=0;i<contadorMenores;i++){
			((ProrrateoProductoDTO)prorrateoProducto.get(Integer.valueOf(indices[i]).intValue())).setProrrateo(((ProrrateoProductoDTO)prorrateoProducto.get(Integer.valueOf(indices[i]).intValue())).getPrecio());
			total_dcto+=((ProrrateoProductoDTO)prorrateoProducto.get(Integer.valueOf(indices[i]).intValue())).getProrrateo();
		}
		return prorrateoProducto;
	}



	public List getListTcp()
    {
        return listTcp;
    }

}
    
