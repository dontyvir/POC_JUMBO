package cl.cencosud.jumbo.output.dto.DeliveryWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantDeliveryWindow;
import cl.cencosud.jumbo.output.dto.OutputDTO;

public class GetOutputDeliveryWindowDTO extends OutputDTO implements Serializable{

	private static final long serialVersionUID = -7284241374040694109L;

	private String shipping_type;//"alphanumeric value representing delivery or click and collect",
	private ArrayList day_of_week_for_delivery = new ArrayList();
	   /* "day_of_week_for_delivery": 
	    				[
						    {
						      "day_of_week": "number that represents the day of the week, the windows usually have 7 days (0..6)",
						      "delivery_ranges":
						      [
						        {
						          "enabled": "0/1 represents if the window is enabled or not (enabled if there is capacity to deliver)",
						          "start_time": "start time for this window, ie 08:00:00",
						          "end_time": "end time for this window, ie 13:00:00",
						          "journey_price": "price of delivery for this 	window, ie 4990",
						          "delivery_journey_id": "delivery journey unique identifier, ie 123",
						          "picking_journey_id": "picking journey unique identifier, ie 111",
						          "delivery_type":"represents the delivery type, ie (n)ormal/(e)xpress/e(c)onomico/(u)mbral"
						        }
						      ]
						    }
						]
						*/
	        		       
	public GetOutputDeliveryWindowDTO(String status, String error_message, String delivery_type, ArrayList day_of_week_for_delivery) {
		
		setStatus(status);
		setError_message(error_message);
		this.shipping_type = shipping_type;
		this.day_of_week_for_delivery = day_of_week_for_delivery;
	}

	public GetOutputDeliveryWindowDTO() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public String getShipping_type() {
		return shipping_type;
	}

	public void setDelivery_type(String delivery_type) {
		this.shipping_type = delivery_type;
	}

	public ArrayList getDay_of_week_for_delivery() {
		return day_of_week_for_delivery;
	}

	public void setDay_of_week_for_delivery(ArrayList dayOfWeekDelivery) {
		this.day_of_week_for_delivery = dayOfWeekDelivery;
	}

	public String toJson() {
		JSONObject obj	= new JSONObject();			
		
		obj.put(Constant.STATUS , String.valueOf(this.getStatus()));
		obj.put(Constant.ERROR_MESSAGE , String.valueOf(this.getError_message()));
		obj.put(ConstantDeliveryWindow.SHIPPING_TYPE , this.getShipping_type());				

		
		
		JSONArray jsArrayDayOfWeekForDelivery = new JSONArray();
		jsArrayDayOfWeekForDelivery.add(this.getDay_of_week_for_delivery());
		obj.put(ConstantDeliveryWindow.DAY_OF_WEEK_FOR_DELIVERY, this.getDay_of_week_for_delivery());
		
		/*JSONArray jsArraDeliveryRanges= new JSONArray();
		jsArraDeliveryRanges.addAll(this.getDay_of_week_for_delivery());
		obj.put(ConstantDeliveryWindow.DELIVERY_RANGES, jsArraDeliveryRanges.toString());*/
		
		
		return obj.toString();
		// TODO Apéndice de método generado automáticamente
		
	}

}
