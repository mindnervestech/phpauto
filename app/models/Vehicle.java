package models;

import java.util.Date;
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
	public String brakes;
	public String horsePower;
	public String acceleration;
	public String location;
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
	public String fuelType;
	public String fuelTank;
	public String headlights;
	public String mirrors;
	public String groundClearance;
	public String roof;
	public String engineType;
	public String cylinders;
	public String displacement;
	public String camType;
	public String valves;
	public String fuelQuality;
	public String gears;
	public String frontBrakeDiameter;
	public String frontBrakeType;
	public String rearBrakeDiameter;
	public String rearBrakeType;
	public String activeHeadRestrains;
	public String bodySideReinforcements;
	public String crumpleZones;
	public String impactAbsorbingBumpers;
	public String impactSensor;
	public String parkingSensors;
	public String seatbelts;
	public String audiSideAssist;
	public String comfortFeatures;
	public String powerOutlet;
	public String powerSteering;
	public String rearViewCamera;
	public String rearViewMonitor;
	public String remoteTrunkRelease;
	public String steeringWheel;
	public String steeringWheelControls;
	public Date postedDate;
	
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getBrakes() {
		return brakes;
	}

	public void setBrakes(String brakes) {
		this.brakes = brakes;
	}

	public String getHorsePower() {
		return horsePower;
	}

	public void setHorsePower(String horsePower) {
		this.horsePower = horsePower;
	}

	public String getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(String acceleration) {
		this.acceleration = acceleration;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getFuelTank() {
		return fuelTank;
	}

	public void setFuelTank(String fuelTank) {
		this.fuelTank = fuelTank;
	}

	public String getHeadlights() {
		return headlights;
	}

	public void setHeadlights(String headlights) {
		this.headlights = headlights;
	}

	public String getMirrors() {
		return mirrors;
	}

	public void setMirrors(String mirrors) {
		this.mirrors = mirrors;
	}

	public String getGroundClearance() {
		return groundClearance;
	}

	public void setGroundClearance(String groundClearance) {
		this.groundClearance = groundClearance;
	}

	public String getRoof() {
		return roof;
	}

	public void setRoof(String roof) {
		this.roof = roof;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getCylinders() {
		return cylinders;
	}

	public void setCylinders(String cylinders) {
		this.cylinders = cylinders;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	public String getCamType() {
		return camType;
	}

	public void setCamType(String camType) {
		this.camType = camType;
	}

	public String getValves() {
		return valves;
	}

	public void setValves(String valves) {
		this.valves = valves;
	}

	public String getFuelQuality() {
		return fuelQuality;
	}

	public void setFuelQuality(String fuelQuality) {
		this.fuelQuality = fuelQuality;
	}

	public String getGears() {
		return gears;
	}

	public void setGears(String gears) {
		this.gears = gears;
	}

	public String getFrontBrakeDiameter() {
		return frontBrakeDiameter;
	}

	public void setFrontBrakeDiameter(String frontBrakeDiameter) {
		this.frontBrakeDiameter = frontBrakeDiameter;
	}

	public String getFrontBrakeType() {
		return frontBrakeType;
	}

	public void setFrontBrakeType(String frontBrakeType) {
		this.frontBrakeType = frontBrakeType;
	}

	public String getRearBrakeDiameter() {
		return rearBrakeDiameter;
	}

	public void setRearBrakeDiameter(String rearBrakeDiameter) {
		this.rearBrakeDiameter = rearBrakeDiameter;
	}

	public String getRearBrakeType() {
		return rearBrakeType;
	}

	public void setRearBrakeType(String rearBrakeType) {
		this.rearBrakeType = rearBrakeType;
	}

	public String getActiveHeadRestrains() {
		return activeHeadRestrains;
	}

	public void setActiveHeadRestrains(String activeHeadRestrains) {
		this.activeHeadRestrains = activeHeadRestrains;
	}

	public String getBodySideReinforcements() {
		return bodySideReinforcements;
	}

	public void setBodySideReinforcements(String bodySideReinforcements) {
		this.bodySideReinforcements = bodySideReinforcements;
	}

	public String getCrumpleZones() {
		return crumpleZones;
	}

	public void setCrumpleZones(String crumpleZones) {
		this.crumpleZones = crumpleZones;
	}

	public String getImpactAbsorbingBumpers() {
		return impactAbsorbingBumpers;
	}

	public void setImpactAbsorbingBumpers(String impactAbsorbingBumpers) {
		this.impactAbsorbingBumpers = impactAbsorbingBumpers;
	}

	public String getImpactSensor() {
		return impactSensor;
	}

	public void setImpactSensor(String impactSensor) {
		this.impactSensor = impactSensor;
	}

	public String getParkingSensors() {
		return parkingSensors;
	}

	public void setParkingSensors(String parkingSensors) {
		this.parkingSensors = parkingSensors;
	}

	public String getSeatbelts() {
		return seatbelts;
	}

	public void setSeatbelts(String seatbelts) {
		this.seatbelts = seatbelts;
	}

	public String getAudiSideAssist() {
		return audiSideAssist;
	}

	public void setAudiSideAssist(String audiSideAssist) {
		this.audiSideAssist = audiSideAssist;
	}

	public String getComfortFeatures() {
		return comfortFeatures;
	}

	public void setComfortFeatures(String comfortFeatures) {
		this.comfortFeatures = comfortFeatures;
	}

	public String getPowerOutlet() {
		return powerOutlet;
	}

	public void setPowerOutlet(String powerOutlet) {
		this.powerOutlet = powerOutlet;
	}

	public String getPowerSteering() {
		return powerSteering;
	}

	public void setPowerSteering(String powerSteering) {
		this.powerSteering = powerSteering;
	}

	public String getRearViewCamera() {
		return rearViewCamera;
	}

	public void setRearViewCamera(String rearViewCamera) {
		this.rearViewCamera = rearViewCamera;
	}

	public String getRearViewMonitor() {
		return rearViewMonitor;
	}

	public void setRearViewMonitor(String rearViewMonitor) {
		this.rearViewMonitor = rearViewMonitor;
	}

	public String getRemoteTrunkRelease() {
		return remoteTrunkRelease;
	}

	public void setRemoteTrunkRelease(String remoteTrunkRelease) {
		this.remoteTrunkRelease = remoteTrunkRelease;
	}

	public String getSteeringWheel() {
		return steeringWheel;
	}

	public void setSteeringWheel(String steeringWheel) {
		this.steeringWheel = steeringWheel;
	}

	public String getSteeringWheelControls() {
		return steeringWheelControls;
	}

	public void setSteeringWheelControls(String steeringWheelControls) {
		this.steeringWheelControls = steeringWheelControls;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public static Finder<Long,Vehicle> find = new Finder<>(Long.class,Vehicle.class);
	
	public static Vehicle findById(Long id) {
		return find.byId(id);
	}
	
	public static Vehicle findByVidAndUser(String vid,AuthUser user) {
		return find.where().eq("vin", vid).eq("user", user).findUnique();
	}
	
	public static Vehicle findByVin(String vin) {
		return find.where().eq("vin", vin).findUnique();
	}
	
	public static List<Vehicle> getAllVehicles() {
		return find.all();
	}

	public static List<Vehicle> getAllVehicles(AuthUser user) {
		return find.where().eq("user", user).findList();
		
	}
	
	public static List<Vehicle> getVehiclesByStatus(AuthUser user,String status) {
		return find.where().eq("user", user).eq("status", status).findList();
		
	}
	
}
