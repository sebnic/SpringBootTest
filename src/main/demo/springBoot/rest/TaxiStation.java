package demo.springBoot.rest;

import demo.springBoot.tool.csv.CSVAttributAnnotation;

public class TaxiStation {

	private int id;
	
	private String name;
	
	private int number;
	
	private String address;
	
	private String addressPrecision;
	
	private int zipCode;
	
	private String city;
	
	private String geoCoordinates;

	public int getId() {
		return id;
	}

	@CSVAttributAnnotation(fieldName="station_id")
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@CSVAttributAnnotation(fieldName="station_name")
	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	@CSVAttributAnnotation(fieldName="station_number")
	public void setNumber(int number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	@CSVAttributAnnotation(fieldName="address")
	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressPrecision() {
		return addressPrecision;
	}

	@CSVAttributAnnotation(fieldName="addressPrecision")
	public void setAddressPrecision(String addressPrecision) {
		this.addressPrecision = addressPrecision;
	}

	public int getZipCode() {
		return zipCode;
	}

	@CSVAttributAnnotation(fieldName="zip_code")
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	@CSVAttributAnnotation(fieldName="city")
	public void setCity(String city) {
		this.city = city;
	}

	public String getGeoCoordinates() {
		return geoCoordinates;
	}

	@CSVAttributAnnotation(fieldName="geo_coordinates")
	public void setGeoCoordinates(String geoCoordinates) {
		this.geoCoordinates = geoCoordinates;
	}
	
	
}
