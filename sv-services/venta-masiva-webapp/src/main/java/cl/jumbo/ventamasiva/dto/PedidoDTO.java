package cl.jumbo.ventamasiva.dto;

import java.io.Serializable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PedidoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String rut;
	private String name;
	private String last_name;
	private String email;
	private String phone_number;
	private long type_street;
	private String street_name;
	private String number_street;
	private String apartment;
	private long region_id;
	private long comuna_id;
	private String observation;
	private JSONArray products;
	private String date;
	private String window;
	private String delivery_price;
	private String payment_method;
	
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public long getType_street() {
		return type_street;
	}
	public void setType_street(long type_street) {
		this.type_street = type_street;
	}
	public String getStreet_name() {
		return street_name;
	}
	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}
	public String getNumber_street() {
		return number_street;
	}
	public void setNumber_street(String number_street) {
		this.number_street = number_street;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	public long getRegion_id() {
		return region_id;
	}
	public void setRegion_id(long region_id) {
		this.region_id = region_id;
	}
	public long getComuna_id() {
		return comuna_id;
	}
	public void setComuna_id(long comuna_id) {
		this.comuna_id = comuna_id;
	}
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWindow() {
		return window;
	}
	public void setWindow(String window) {
		this.window = window;
	}
	public String getDelivery_price() {
		return delivery_price;
	}
	public void setDelivery_price(String delivery_price) {
		this.delivery_price = delivery_price;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	public JSONArray getProducts() {
		return products;
	}
	public void setProducts(JSONArray products) {
		this.products = products;
	}
	public String toString() {
		return "PedidoDTO [rut=" + rut + ", name=" + name + ", last_name="
				+ last_name + ", email=" + email + ", phone_number="
				+ phone_number + ", type_street=" + type_street + ", " 
				+ " street_name=" + street_name + ", number_street=" + number_street  
				+ " apartment=" + apartment + ", comuna_id=" + comuna_id + "," 
				+ " region_id=" + region_id + ",observation="
				+ observation + ", products=" + products.toString() + ", date=" + date
				+ ", window=" + window + ", delivery_price=" + delivery_price
				+ ", payment_method=" + payment_method + "]";
	}
	
}
