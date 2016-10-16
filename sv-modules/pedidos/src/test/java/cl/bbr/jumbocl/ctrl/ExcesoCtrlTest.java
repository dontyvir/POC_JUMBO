package cl.bbr.jumbocl.ctrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.ctrl.ExcesoCtrl;

/*
Statement	|	Description
1.- fail(String)	 Let the method fail. Might be used to check that a certain part of the code is not reached or to have a failing test 
before the test code is implemented. The String parameter is optional.

2.- assertTrue([message], boolean condition)	Checks that the boolean condition is true.
3.- assertFalse([message], boolean condition)	Checks that the boolean condition is false.
4.- assertEquals([String message], expected, actual)	 Tests that two values are the same.
 Note: for arrays the reference is checked not the content of the arrays.
 
5.-assertEquals([String message], expected, actual, tolerance)	 Test that float or double values match. The tolerance is the number of decimals which must be the same.
6.-assertNull([message], object)	Checks that the object is null.
7.-assertNotNull([message], object)	Checks that the object is not null.
8.-assertSame([String], expected, actual)	 Checks that both variables refer to the same object.
9.-assertNotSame([String], expected, actual)
*/

public class ExcesoCtrlTest extends ExcesoTest {
		
	public void testOP() throws SystemException{
				
		/*List ops = ExcesoCtrl.getPedidosByEstado(7);
		Iterator it = ops.iterator();
		
		while(it.hasNext()){
			long op = Long.parseLong((String)it.next());
			if(excesoCtrl.isOpConExceso(op))
				excesoCtrl.isExcesoCorreccionAutomatico(op);
		}*/
		
		
		/*if(excesoCtrl.isOpConExcesoXTotal(idPedido)){
			System.out.println("isOpConExcesoXTotal");
		}
		if(excesoCtrl.isOpConExcesoXProducto(idPedido)){
			System.out.println("isOpConExcesoXProducto");
		}*/
	}
	
	/*
	 * public void testIsExcesoXTotal() throws SystemException {
		assertFalse("OP:excesoCtrl "+idPedido+" tiene exceso XTota", excesoCtrl.isOpConExcesoXTotal(idPedido));     
	}
	
	public void testIsExcesoXProducto() throws SystemException {
		assertFalse("OP:excesoCtrl "+idPedido+" tiene exceso XProducto", excesoCtrl.isOpConExcesoXProducto(idPedido));     
	}
	*/

}
