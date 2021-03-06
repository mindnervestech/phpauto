package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class TradeIn extends Model {
	
	public String getLienholder() {
		return lienholder;
	}
	public void setLienholder(String lienholder) {
		this.lienholder = lienholder;
	}
	public String getPdfPath() {
		return pdfPath;
	}
	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}

	//@Version
	//public java.util.Date version; 

	@Id
	public Long id;
	public String firstName;
	public String lastName;
	public String workPhone;
	public String phone;
	public String email;
	public String preferredContact;
	public Date tradeDate;
	public Date tradeTime;
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
	public String reason;
	public String custZipCode;
	public int scheduleEmail;
	public String enthicity;
	public Date statusDate;
	public Date statusTime;
	public Integer premiumFlag;	
	public String testDriveCompletedComment;
	public String testDriveCompletedDuration;
	public int onlineOrOfflineLeads;
	public String testDriveStatus;
	public String sessionId;
	public int notifFlag;
	public Long ProductId;
	
	
	
	
	public Long getProductId() {
		return ProductId;
	}
	public void setProductId(Long productId) {
		ProductId = productId;
	}
	public int getNotifFlag() {
		return notifFlag;
	}
	public void setNotifFlag(int notifFlag) {
		this.notifFlag = notifFlag;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getTestDriveStatus() {
		return testDriveStatus;
	}
	public void setTestDriveStatus(String testDriveStatus) {
		this.testDriveStatus = testDriveStatus;
	}
	public String getEnthicity() {
		return enthicity;
	}
	public void setEnthicity(String enthicity) {
		this.enthicity = enthicity;
	}
	public int getScheduleEmail() {
		return scheduleEmail;
	}
	public void setScheduleEmail(int scheduleEmail) {
		this.scheduleEmail = scheduleEmail;
	}

	@ManyToOne
	public AuthUser assignedTo;
	@ManyToOne
	public AuthUser user;
	
	@ManyToOne
	public Location locations;
	public String bestDay;
	public String bestTime;
	public Date scheduleDate;
	public Boolean isScheduled = false;
	public Date confirmDate;
	public Date confirmTime;
	public String leadStatus;
	public Boolean isReassigned = false;
	public String contactedFrom;
	public String hearedFrom;
	public Long parentId;
	
	public String getContactedFrom() {
		return contactedFrom;
	}
	public void setContactedFrom(String contactedFrom) {
		this.contactedFrom = contactedFrom;
	}
	public String getHearedFrom() {
		return hearedFrom;
	}
	public void setHearedFrom(String hearedFrom) {
		this.hearedFrom = hearedFrom;
	}
	public Boolean getIsReassigned() {
		return isReassigned;
	}
	public void setIsReassigned(Boolean isReassigned) {
		this.isReassigned = isReassigned;
	}
	public String getLeadStatus() {
		return leadStatus;
	}
	public void setLeadStatus(String leadStatus) {
		this.leadStatus = leadStatus;
	}
	public String getBestDay() {
		return bestDay;
	}
	public void setBestDay(String bestDay) {
		this.bestDay = bestDay;
	}
	public String getBestTime() {
		return bestTime;
	}
	public void setBestTime(String bestTime) {
		this.bestTime = bestTime;
	}
	public Date getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public Boolean getIsScheduled() {
		return isScheduled;
	}
	public void setIsScheduled(Boolean isScheduled) {
		this.isScheduled = isScheduled;
	}
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public Date getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}
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
	
	public int getOnlineOrOfflineLeads() {
		return onlineOrOfflineLeads;
	}
	public void setOnlineOrOfflineLeads(int onlineOrOfflineLeads) {
		this.onlineOrOfflineLeads = onlineOrOfflineLeads;
	}
	public String getTestDriveCompletedComment() {
		return testDriveCompletedComment;
	}
	public void setTestDriveCompletedComment(String testDriveCompletedComment) {
		this.testDriveCompletedComment = testDriveCompletedComment;
	}
	public String getTestDriveCompletedDuration() {
		return testDriveCompletedDuration;
	}
	public void setTestDriveCompletedDuration(String testDriveCompletedDuration) {
		this.testDriveCompletedDuration = testDriveCompletedDuration;
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
	
	
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/*public String getLienHolder() {
		return lienholder;
	}
	public void setLienHolder(String lienHolder) {
		this.lienholder = lienHolder;
	}*/
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
	
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
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
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}

	
	public Integer getPremiumFlag() {
		return premiumFlag;
	}
	public void setPremiumFlag(Integer premiumFlag) {
		this.premiumFlag = premiumFlag;
	}
	public String getCustZipCode() {
		return custZipCode;
	}
	public void setCustZipCode(String custZipCode) {
		this.custZipCode = custZipCode;
	}
	public Date getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
	public Date getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}

	public static Finder<Long,TradeIn> find = new Finder<>(Long.class,TradeIn.class);
	
	public static TradeIn findById(Long id) {
		return find.byId(id);
	}
	
	public static TradeIn findByIdAndParent(Long id) {
		return find.where().eq("id", id).eq("status", null).findUnique();
	}
	
	public static List<TradeIn> findAllByDate() {
		return find.where().eq("isRead", 0).orderBy("tradeDate desc").orderBy("isRead").findList();
	}
	
	public static List<TradeIn> findAllByParentID(Long parentId) {
		return find.where().eq("parentId", parentId).eq("status", null).findList();
	}
	
	public static List<TradeIn> findAllByLocationDate(Long locationId) {
		return find.where().eq("isRead", 0).eq("locations.id", locationId).eq("assignedTo", null).eq("premiumFlag", 0).eq("status", null).orderBy("tradeDate desc").orderBy("isRead").findList();
	}
	
	public static List<TradeIn> findAllCanceled(Long locationId) {
		return find.where().add(Expr.or(Expr.eq("status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).eq("locations.id", locationId).findList();
	}
	public static List<TradeIn> findAllCanceledByUser(Long locationId, AuthUser user) {
		return find.where().add(Expr.or(Expr.eq("status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).eq("locations.id", locationId).eq("assignedTo", user).findList();
	}
	public static List<TradeIn> findAllData() {
		return find.all();
	}
	
	public static List<TradeIn> findAllLocationData(Long locationId) {
		return find.where().eq("locations.id", locationId).eq("isRead", 0).eq("status", null).findList();
	}
	
	public static List<TradeIn> findAllLocationDataManager(Long locationId) {
		return find.where().eq("locations.id", locationId).eq("status", null).eq("premiumFlag", 0).findList();
	}
	
	public static List<TradeIn> findAllLocationDataManagerPremium(Long locationId) {
		return find.where().eq("locations.id", locationId).eq("status", null).eq("premiumFlag", 1).findList();
	}
	
	public static List<TradeIn> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	public static List<TradeIn> findByLocationNotCancel(Long location) {
		return find.where().eq("locations.id", location).ne("status", "CANCEL").findList();
	}
	
	public static List<TradeIn> findAllByAssignedUser(AuthUser user) {
		return find.where().eq("assignedTo", user).findList();
	}
	
	public static List<TradeIn> findAllByNotOpenLead(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("status", null).findList();
	}
	
	public static List<TradeIn> findByLocationNotOpenLead(Long location) {
		return find.where().eq("locations.id", location).ne("status", null).findList();
	}
	
	public static List<TradeIn> findByAssigUserNotCancel(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("status", "CANCEL").findList();
	}
	
	public static List<TradeIn> findByLocationNotCancel(Location location) {
		return find.where().eq("locations", location).ne("status", "CANCEL").findList();
	}
	
	public static List<TradeIn> findAllSeen(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("isRead", 1).eq("status", null).eq("isScheduled", false).orderBy("tradeDate desc").findList();
	}
	
	public static List<TradeIn> findAllSeenSch(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("isRead", 1).eq("status", null).orderBy("tradeDate desc").findList();
	}
	public static List<TradeIn> findAllSeenSchforSold(AuthUser user) {
		return find.where().add(Expr.or(Expr.eq("status", null),Expr.eq("status", "COMPLETE"))).eq("assignedTo", user).eq("isRead", 1).orderBy("tradeDate desc").findList();
	}
	
	public static List<TradeIn> findAllAssignedOffine(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("onlineOrOfflineLeads",0).orderBy("tradeDate desc").findList();
	}
	
	public static List<TradeIn> findAllAssignedOnline(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("onlineOrOfflineLeads",1).orderBy("tradeDate desc").findList();
	}
	
	public static List<TradeIn> findAllLostSch(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("status", "LOST").findList();
	}
	
	public static List<TradeIn> findAllSeenComplete(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("status", "COMPLETE").orderBy("scheduleDate desc").findList();
	}
	
	public static List<TradeIn> findAllSeenLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).eq("isRead", 1).eq("status", null).eq("isScheduled", false).orderBy("tradeDate desc").findList();
	}
	
	public static List<TradeIn> findAllSeenLocationSch(Long locationId) {
		return find.where().eq("locations.id", locationId).eq("isRead", 1).eq("status", null).orderBy("tradeDate desc").findList();
	}
	
	public static int findAll(Long locationId) {
		return find.where().eq("isRead", 0).eq("premiumFlag", 0).eq("locations.id", locationId).eq("status", null).findRowCount();
	}
	
	public static List<TradeIn> findAllLeads(Long locationId) {
		return find.where().eq("isRead", 0).eq("premiumFlag", 0).eq("locations.id", locationId).eq("status", null).findList();
	}
	public static List<TradeIn> findByConfirmLeads(Long locationId, AuthUser user) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("status", null).eq("testDriveStatus", null).eq("locations.id", locationId).eq("assignedTo", user).findList();
	}
	
	public static List<TradeIn> testDriveForSalePerson(AuthUser user) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("assignedTo", user).findList();
	}
	
	public static List<TradeIn> findByConfirmGraLeadsToEmail(Date lessDate) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("status", null).eq("testDriveStatus", null).ge("confirmDate", lessDate).findList();
	}
	
	public static List<TradeIn> findByConfirmGraLeadsToPopup(AuthUser user,Date curDate) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("assignedTo", user).eq("status", null).eq("testDriveStatus", null).ge("confirmDate", curDate).findList();
	}
	
	public static List<TradeIn> findByConfirmGraLeads(Long locationId, AuthUser user, Date datec) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("status", null).eq("testDriveStatus", null).eq("locations.id", locationId).eq("assignedTo", user).ge("confirmDate", datec).findList();
	}
	
	public static List<TradeIn> findByDateAndAssignedUser(AuthUser user,Date date) {
		return find.where().eq("assignedTo", user).eq("confirmDate", date).eq("status", null).findList();
	}
	
	public static List<TradeIn> findAllByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<TradeIn> findAllScheduledUser(AuthUser user) {
		//return find.where().eq("isScheduled", true).eq("leadStatus", null).eq("user",user).findList();
		return find.where().eq("isScheduled", true).eq("status", null).eq("isRead",true).eq("assignedTo",user).findList();
	}
	
	public static List<TradeIn> findAllAssignedLeadsToUser(AuthUser user) {
		return find.where().ne("status", null).eq("assignedTo",user).findList();
	}
	
	public static List<TradeIn> findAllNullStatusLeads(Date timeForThreeDays) {
		return find.where().eq("status", null).ge("tradeTime", timeForThreeDays).findList();
	}
	
	public static List<TradeIn> findAllAssignedLeadsToUser1(AuthUser user) {
		return find.where().eq("status", null).eq("assignedTo",user).findList();
	}
	public static List<TradeIn> findAllCompletedToUser(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("status", null).eq("testDriveStatus", "TestDriveCompleted").findList();
	}
	
	public static List<TradeIn> findAllCancelLeadsForUser(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("status", "CANCEL").findList();
	}
	
	public static List<TradeIn>  findByVinAndLocation(String vin,Location location) {
		return find.where().eq("vin", vin).eq("status", null).eq("locations", location).findList();
	}
	
	public static List<TradeIn>  findByVinSoldAndLocation(String vin,Location location) {
		return find.where().eq("vin", vin).ne("status", null).eq("locations", location).findList();
	}
	
	
	public static List<TradeIn>  findByVinAndAssignedUser(String vin,AuthUser user) {
		return find.where().eq("vin", vin).eq("user", user).findList();
	}
	
	public static List<TradeIn>  findAllReassigned(AuthUser user) {
		return find.where().eq("isReassigned", true).eq("assignedTo",user).findList();
	}
	public static List<TradeIn> findByVinStatus(String vin) {
		return find.where().eq("vin", vin).eq("status", null).findList();
	}
	public static List<TradeIn> findByVin(String vin) {
		// TODO Auto-generated method stub
		return find.where().eq("vin", vin).findList();
	}
	public static List<TradeIn> findByVinDate(String vin, Date date) {
		return find.where().eq("vin", vin).eq("confirm_date", date).findList();
	}
	public static List<TradeIn> findByScheduler() {
		return find.where().eq("schedule_email", 0).findList();
	}
	public static List<SqlRow> getTradeDates(AuthUser user, String cD) {
		SqlQuery q = Ebean.createSqlQuery("select distinct trade_in.confirm_date from trade_in where (trade_in.assigned_to_id = '"+user.id+"') and trade_in.confirm_date >= '"+cD+"' and trade_in.confirm_date is not null");
		List<SqlRow> rows = q.findList();
		return rows;
	}
	public static List<TradeIn> findAllByUserServiceTest(AuthUser user,
			Date date) {
		return find.where().eq("assignedTo", user).eq("confirmDate", date).ne("confirmTime", null).eq("status", null).findList();
	}
}
