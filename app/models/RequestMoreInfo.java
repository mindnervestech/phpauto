package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class RequestMoreInfo extends Model {

	@Id
	public Long id;
	public String name;
	public String preferredContact;
	public String email;
	public String phone;
	public Date requestDate;
	public Date requestTime;
	public String vin;
	public int isRead;
	public int notifFlag;
	public String status;
	public String reason;
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
	public String custZipCode;
	public int scheduleEmail;
	public String enthicity;
	public String testDriveCompletedComment;
	public String testDriveCompletedDuration;
	public Date statusDate;
	public Date statusTime;
	public Integer premiumFlag;
	public Long parentId;
	public String sessionId;
	public int onlineOrOfflineLeads;
public String testDriveStatus;
public String isContactusType;
public String message;	
	
	
	
	
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
	public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
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
	

	public Date getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}


	public String getIsContactusType() {
		return isContactusType;
	}
	public void setIsContactusType(String isContactusType) {
		this.isContactusType = isContactusType;
	}


	@ManyToOne
	public AuthUser assignedTo;
	@ManyToOne
	public AuthUser user;
	
	@ManyToOne
	public Location locations;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPreferredContact() {
		return preferredContact;
	}
	public void setPreferredContact(String preferredContact) {
		this.preferredContact = preferredContact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
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

	public String getCustZipCode() {
		return custZipCode;
	}
	public void setCustZipCode(String custZipCode) {
		this.custZipCode = custZipCode;
	}

	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}
	
	public Integer getPremiumFlag() {
		return premiumFlag;
	}
	public void setPremiumFlag(Integer premiumFlag) {
		this.premiumFlag = premiumFlag;
	}
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	

	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
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

	public static Finder<Long,RequestMoreInfo> find = new Finder<>(Long.class,RequestMoreInfo.class);
	
	public static RequestMoreInfo findById(Long id) {
		return find.byId(id);
	}
	
	public static RequestMoreInfo findByIdAndParent(Long id) {
		return find.where().eq("id", id).eq("status", null).findUnique();
	}
	
	public static List<RequestMoreInfo> findAllByDate() {
		return find.where().eq("isRead", 0).eq("status", null).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllByParentID(Long parentId) {
		return find.where().eq("parentId", parentId).eq("status", null).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	public static List<RequestMoreInfo> findByLocationNotOpenLead(Long location) {
		return find.where().eq("locations.id", location).ne("status", null).findList();
	}
	
	public static List<RequestMoreInfo> findByLocationNotCancel(Long location) {
		return find.where().eq("locations.id", location).ne("status", "CANCEL").findList();
	}
	
	public static List<RequestMoreInfo> findByVinAndLocation(String vin,Location location) {
		return find.where().eq("vin", vin).eq("status", null).eq("locations", location).findList();
	}
	
	public static List<RequestMoreInfo> findByVinSoldAndLocation(String vin,Location location) {
		return find.where().eq("vin", vin).ne("status", null).eq("locations", location).findList();
	}
	
	public static List<RequestMoreInfo> findAllData() {
		return find.where().eq("isContactusType", null).findList();
		
	}
	
	public static List<RequestMoreInfo> findAllContactData() {
		return find.where().ne("isContactusType", null).findList();
	}
	
	
	public static List<RequestMoreInfo> findAllLocationData(Long locationId) {
		return find.where().eq("status", null).eq("isRead", 0).eq("premiumFlag", 0).eq("assignedTo", null).eq("isContactusType", null).eq("locations.id", locationId).findList();
	}
	public static List<RequestMoreInfo> findAllLocationDataContactUs(Long locationId) {
		return find.where().eq("status", null).eq("isRead", 0).eq("premiumFlag", 0).eq("assignedTo", null).ne("isContactusType", null).eq("locations.id", locationId).findList();
	}
	
	public static List<RequestMoreInfo> findAllLocationDataManager(Long locationId) {
		return find.where().eq("status", null).eq("locations.id", locationId).eq("premiumFlag", 0).eq("isContactusType", null).findList();
	}
	
	public static List<RequestMoreInfo> findAllLocationDataManagerContactUs(Long locationId) {
		return find.where().eq("status", null).eq("locations.id", locationId).eq("premiumFlag", 0).ne("isContactusType", null).findList();
	}
	
	public static List<RequestMoreInfo> findAllLocationDataManagerPremium(Long locationId) {
		return find.where().eq("status", null).eq("locations.id", locationId).eq("premiumFlag", 1).findList();
	}
	
	public static List<RequestMoreInfo> findAllSeen(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("isRead", 1).eq("status", null).eq("isContactusType", null).eq("isScheduled", false).orderBy("requestDate desc").findList();
	}
	public static List<RequestMoreInfo> findAllSeenContactUs(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("isRead", 1).eq("status", null).ne("isContactusType", null).eq("isScheduled", false).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllSeenSch(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("isRead", 1).eq("status", null).orderBy("requestDate desc").findList();
	}	
	public static List<RequestMoreInfo> findAllSeenSchfoeSold(AuthUser user) {
		return find.where().add(Expr.or(Expr.eq("status", null),Expr.eq("status", "COMPLETE"))).eq("assignedTo", user).eq("isRead", 1).orderBy("requestDate desc").findList();
	}
	public static List<RequestMoreInfo> findAllAssignedOffine(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("onlineOrOfflineLeads",0).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllAssignedOnline(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("onlineOrOfflineLeads",1).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllLostSch(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("status", "LOST").orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllSeenLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).eq("isRead", 1).eq("status", null).eq("isScheduled", false).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllSeenLocationSch(Long locationId) {
		return find.where().eq("locations.id", locationId).eq("isRead", 1).eq("status", null).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllSeenComplete(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("status", "COMPLETE").orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllCancel(Long locationId) {
		return find.where().add(Expr.or(Expr.eq("status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).eq("locations.id", locationId).findList();
	}
	public static List<RequestMoreInfo> findAllCancelByUser(Long locationId, AuthUser user) {
		return find.where().add(Expr.or(Expr.eq("status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).eq("locations.id", locationId).eq("assignedTo", user).findList();
	}
	
	public static int findAll(Long locationId) {
		return find.where().eq("isRead", 0).eq("premiumFlag", 0).eq("status", null).eq("locations.id", locationId).findRowCount();
	}
	public static List<RequestMoreInfo> findAllLeads(Long locationId) {
		return find.where().eq("isRead", 0).eq("premiumFlag", 0).eq("status", null).eq("locations.id", locationId).findList();
	}
	public static List<RequestMoreInfo> findAllByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<RequestMoreInfo> findAllCancelLeadByUser(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("status", "CANCEL").findList();
	}
	
	public static List<RequestMoreInfo> findAllByAssignedUser(AuthUser user) {
		return find.where().eq("assignedTo", user).findList();
	}
	
	public static List<RequestMoreInfo> findAllByNotOpenLead(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("status", null).findList();
	}
	
	public static List<RequestMoreInfo> findByAssigUserNotCancel(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("status", "CANCEL").findList();
	}
	
	public static List<RequestMoreInfo> findByLocationNotCancel(Location location) {
		return find.where().eq("locations", location).ne("status", "CANCEL").findList();
	}
	
	public static List<RequestMoreInfo> findAllScheduledUser(AuthUser user) {
		return find.where().eq("isScheduled", true).eq("isRead",true).eq("assignedTo", user).eq("status", null).findList();
	}
	
	public static List<RequestMoreInfo> findAllAssignedLeadsToUser(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("status", null).findList();
	}
	public static List<RequestMoreInfo> findAllAssignedLeadsToUser1(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("status", null).findList();
	}
	
	public static List<RequestMoreInfo> findAllCompletedToUser(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("status", null).eq("testDriveStatus", "TestDriveCompleted").findList();
	}
	
	public static List<RequestMoreInfo> findAllNullStatusLeads(Date timeForThreeDays) {
		return find.where().eq("status", null).ge("requestTime", timeForThreeDays).findList();
	}
	
	public static List<RequestMoreInfo> findByConfirmLeads(Long locationId, AuthUser user) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("status", null).eq("testDriveStatus", null).eq("locations.id", locationId).eq("assignedTo", user).findList();
	}
	
	public static List<RequestMoreInfo> testDriveForSalePerson(AuthUser user) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("assignedTo", user).findList();
	}
	
	public static List<RequestMoreInfo> findByConfirmGraLeadsToEmail(Date lessDate) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("status", null).eq("testDriveStatus", null).ge("confirmDate", lessDate).findList();
	}
	
	public static List<RequestMoreInfo> findByConfirmGraLeadsToPopUp(AuthUser user,Date curDate) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("assignedTo", user).eq("status", null).eq("testDriveStatus", null).ge("confirmDate", curDate).findList();
	}
	
	public static List<RequestMoreInfo> findByConfirmGraLeads(Long locationId, AuthUser user, Date cDate) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("status", null).eq("testDriveStatus", null).eq("locations.id", locationId).ge("confirmDate", cDate).eq("assignedTo", user).findList();
	}
	public static List<RequestMoreInfo> findByDateAndAssignedUser(AuthUser user,Date date) {
		return find.where().eq("assignedTo", user).eq("confirmDate", date).eq("status", null).findList();
	}
	public static List<RequestMoreInfo> findAllByUserServiceTest(AuthUser user,Date date) {
		return find.where().eq("assignedTo", user).eq("confirmDate", date).ne("confirmTime", null).eq("status", null).findList();
	}
	public static List<RequestMoreInfo> findByVinAndAssignedUser(String vin,AuthUser user) {
		return find.where().eq("vin", vin).eq("user", user).findList();
	}
	
	public static List<RequestMoreInfo> findAllReassigned(AuthUser user) {
		return find.where().eq("isReassigned", true).eq("assignedTo",user).findList();
	}
	
	public static List<SqlRow> getRequestedDates(AuthUser user, String cD) {
		SqlQuery q = Ebean.createSqlQuery("select distinct request_more_info.confirm_date from request_more_info where (request_more_info.assigned_to_id = '"+user.id+"') and request_more_info.confirm_date is not null");
		List<SqlRow> rows = q.findList();
		return rows;
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
	public String getLeadStatus() {
		return leadStatus;
	}
	public void setLeadStatus(String leadStatus) {
		this.leadStatus = leadStatus;
	}
	public static List<RequestMoreInfo> findByVin(String vin) {
		// TODO Auto-generated method stub
		return find.where().eq("vin", vin).findList();
	}
	public static List<RequestMoreInfo> findByVinStatus(String vin) {
		return find.where().eq("vin", vin).eq("status", null).findList();
	}
	public static List<RequestMoreInfo> findByVinDate(String vin, Date date) {
		return find.where().eq("vin", vin).eq("confirm_date", date).findList();
	}
	public static List<RequestMoreInfo> findByScheduler() {
		return find.where().eq("schedule_email", 0).findList();
	}
	
}
