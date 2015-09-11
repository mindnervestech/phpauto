package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class TradeIn extends Model {

	@Id
	public Long id;
	public String firstName;
	public String lastName;
	public String workPhone;
	public String phone;
	public String email;
	public String preferredContact;
	public Date tradeDate;
	public String comments;
	public String year;
	public String make;
	public String model;
	public String exteriorColour;
	public String kilometres;
	public String engine;
	public String doors;
	public String transmission;
	public String drivetrain;
	public String bodyRating;
	public String tireRating;
	public String engineRating;
	public String transmissionRating;
	public String glassRating;
	public String interiorRating;
	public String exhaustRating;
	public String leaseOrRental;
	public String operationalAndAccurate;
	public String serviceRecord;
	public String lienholder;
	public String holdsThisTitle;
	public String equipment;
	public String vehiclenew;
	public String accidents;
	public String damage;
	public String paint;
	public String salvage;
	public String optionValue;
	public String vin;
	public int isRead;
	public String pdfPath;
	public String status;
	@ManyToOne
	public AuthUser assignedTo;
	@ManyToOne
	public AuthUser user;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPreferredContact() {
		return preferredContact;
	}
	public void setPreferredContact(String preferredContact) {
		this.preferredContact = preferredContact;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public String getExteriorColour() {
		return exteriorColour;
	}
	public void setExteriorColour(String exteriorColour) {
		this.exteriorColour = exteriorColour;
	}
	public String getKilometres() {
		return kilometres;
	}
	public void setKilometres(String kilometres) {
		this.kilometres = kilometres;
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getDoors() {
		return doors;
	}
	public void setDoors(String doors) {
		this.doors = doors;
	}
	public String getTransmission() {
		return transmission;
	}
	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}
	public String getDrivetrain() {
		return drivetrain;
	}
	public void setDrivetrain(String drivetrain) {
		this.drivetrain = drivetrain;
	}
	public String getBodyRating() {
		return bodyRating;
	}
	public void setBodyRating(String bodyRating) {
		this.bodyRating = bodyRating;
	}
	public String getTireRating() {
		return tireRating;
	}
	public void setTireRating(String tireRating) {
		this.tireRating = tireRating;
	}
	public String getEngineRating() {
		return engineRating;
	}
	public void setEngineRating(String engineRating) {
		this.engineRating = engineRating;
	}
	public String getTransmissionRating() {
		return transmissionRating;
	}
	public void setTransmissionRating(String transmissionRating) {
		this.transmissionRating = transmissionRating;
	}
	public String getGlassRating() {
		return glassRating;
	}
	public void setGlassRating(String glassRating) {
		this.glassRating = glassRating;
	}
	public String getInteriorRating() {
		return interiorRating;
	}
	public void setInteriorRating(String interiorRating) {
		this.interiorRating = interiorRating;
	}
	public String getExhaustRating() {
		return exhaustRating;
	}
	public void setExhaustRating(String exhaustRating) {
		this.exhaustRating = exhaustRating;
	}
	public String getLeaseOrRental() {
		return leaseOrRental;
	}
	public void setLeaseOrRental(String leaseOrRental) {
		this.leaseOrRental = leaseOrRental;
	}
	public String getOperationalAndAccurate() {
		return operationalAndAccurate;
	}
	public void setOperationalAndAccurate(String operationalAndAccurate) {
		this.operationalAndAccurate = operationalAndAccurate;
	}
	public String getServiceRecord() {
		return serviceRecord;
	}
	public void setServiceRecord(String serviceRecord) {
		this.serviceRecord = serviceRecord;
	}
	public String getLienHolder() {
		return lienholder;
	}
	public void setLienHolder(String lienHolder) {
		this.lienholder = lienHolder;
	}
	public String getHoldsThisTitle() {
		return holdsThisTitle;
	}
	public void setHoldsThisTitle(String holdsThisTitle) {
		this.holdsThisTitle = holdsThisTitle;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public String getVehiclenew() {
		return vehiclenew;
	}
	public void setVehiclenew(String vehiclenew) {
		this.vehiclenew = vehiclenew;
	}
	public String getAccidents() {
		return accidents;
	}
	public void setAccidents(String accidents) {
		this.accidents = accidents;
	}
	public String getDamage() {
		return damage;
	}
	public void setDamage(String damage) {
		this.damage = damage;
	}
	public String getPaint() {
		return paint;
	}
	public void setPaint(String paint) {
		this.paint = paint;
	}
	public String getSalvage() {
		return salvage;
	}
	public void setSalvage(String salvage) {
		this.salvage = salvage;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public AuthUser getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(AuthUser assignedTo) {
		this.assignedTo = assignedTo;
	}

	public static Finder<Long,TradeIn> find = new Finder<>(Long.class,TradeIn.class);
	
	public static TradeIn findById(Long id) {
		return find.byId(id);
	}
	
	public static List<TradeIn> findAllByDate() {
		return find.where().eq("isRead", 0).orderBy("tradeDate desc").orderBy("isRead").findList();
	}
	
	public static List<TradeIn> findAllSeen(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("isRead", 1).eq("status", null).orderBy("tradeDate desc").findList();
	}
	
	public static int findAll() {
		return find.where().eq("isRead", 0).findRowCount();
	}
	
	public static List<TradeIn> findAllByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
}
