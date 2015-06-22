package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import securesocial.core.Identity;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

@Entity
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Vehicle extends Model {

	@Id
	public Long id;
	public String category;
	public String vin;
	public String year;
	public String make;
	public String model;
	public String trim;
	public String label;
	public String stock;
	public String mileage;
	public Integer cost;
	public Integer price;
	public String exteriorColor;
	public String interiorColor;
	public String colorDescription;
	public String doors;
	public String stereo;
	public String engine;
	public String fuel;
	public String cityMileage;
	public String highwayMileage;
	public String bodyStyle;
	public String drivetrain;
	public String transmission;
	public String standardFeatures1;
	public String standardFeatures2;
	public String description;
	public String madeIn;
	public String steeringType;
	public String antiBrakeSystem;
	public String height;
	public String length;
	public String width;
	public String standardSeating;
	public String optionalSeating;
	public String status;
	
	@ManyToMany
	public List<Site> site;
	
	@ManyToOne
	public AuthUser user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTrim() {
		return trim;
	}

	public void setTrim(String trim) {
		this.trim = trim;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}

	public String getExteriorColor() {
		return exteriorColor;
	}

	public void setExteriorColor(String exteriorColor) {
		this.exteriorColor = exteriorColor;
	}

	public String getInteriorColor() {
		return interiorColor;
	}

	public void setInteriorColor(String interiorColor) {
		this.interiorColor = interiorColor;
	}

	public String getColorDescription() {
		return colorDescription;
	}

	public void setColorDescription(String colorDescription) {
		this.colorDescription = colorDescription;
	}

	public String getDoors() {
		return doors;
	}

	public void setDoors(String doors) {
		this.doors = doors;
	}

	public String getStereo() {
		return stereo;
	}

	public void setStereo(String stereo) {
		this.stereo = stereo;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getCityMileage() {
		return cityMileage;
	}

	public void setCityMileage(String cityMileage) {
		this.cityMileage = cityMileage;
	}

	public String getHighwayMileage() {
		return highwayMileage;
	}

	public void setHighwayMileage(String highwayMileage) {
		this.highwayMileage = highwayMileage;
	}

	public String getBodyStyle() {
		return bodyStyle;
	}

	public void setBodyStyle(String bodyStyle) {
		this.bodyStyle = bodyStyle;
	}

	public String getDrivetrain() {
		return drivetrain;
	}

	public void setDrivetrain(String drivetrain) {
		this.drivetrain = drivetrain;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getStandardFeatures1() {
		return standardFeatures1;
	}

	public void setStandardFeatures1(String standardFeatures1) {
		this.standardFeatures1 = standardFeatures1;
	}

	public String getStandardFeatures2() {
		return standardFeatures2;
	}

	public void setStandardFeatures2(String standardFeatures2) {
		this.standardFeatures2 = standardFeatures2;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Site> getSite() {
		return site;
	}

	public void setSite(List<Site> site) {
		this.site = site;
	}
	
	public String getMadeIn() {
		return madeIn;
	}

	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}

	public String getSteeringType() {
		return steeringType;
	}

	public void setSteeringType(String steeringType) {
		this.steeringType = steeringType;
	}

	public String getAntiBrakeSystem() {
		return antiBrakeSystem;
	}

	public void setAntiBrakeSystem(String antiBrakeSystem) {
		this.antiBrakeSystem = antiBrakeSystem;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getStandardSeating() {
		return standardSeating;
	}

	public void setStandardSeating(String standardSeating) {
		this.standardSeating = standardSeating;
	}

	public String getOptionalSeating() {
		return optionalSeating;
	}

	public void setOptionalSeating(String optionalSeating) {
		this.optionalSeating = optionalSeating;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public static Finder<Long,Vehicle> find = new Finder<>(Long.class,Vehicle.class);
	
	public static Vehicle findById(Long id) {
		return find.byId(id);
	}
	
	public static Vehicle findByVidAndUser(String vid,AuthUser user) {
		return find.where().eq("vin", vid).eq("user", user).findUnique();
	}
	
	public static List<Vehicle> getAllVehicles() {
		return find.all();
	}

	public static List<Vehicle> getAllVehicles(AuthUser user) {
		return find.where().eq("user", user).findList();
		
	}
	
}
