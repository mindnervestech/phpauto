package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;

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
	public String vin;
	public int isRead;
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

	public static Finder<Long,RequestMoreInfo> find = new Finder<>(Long.class,RequestMoreInfo.class);
	
	public static RequestMoreInfo findById(Long id) {
		return find.byId(id);
	}
	
	public static List<RequestMoreInfo> findAllByDate() {
		return find.where().eq("isRead", 0).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllData() {
		return find.all();
	}
	
	public static List<RequestMoreInfo> findAllSeen(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("isRead", 1).eq("status", null).eq("isScheduled", false).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllCancel() {
		return find.where().add(Expr.or(Expr.eq("status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).findList();
	}
	
	public static int findAll() {
		return find.where().eq("isRead", 0).findRowCount();
	}
	
	public static List<RequestMoreInfo> findAllByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<RequestMoreInfo> findAllScheduledUser(AuthUser user) {
		return find.where().eq("isScheduled", true).eq("user", user).eq("leadStatus", null).findList();
	}
	
	public static List<RequestMoreInfo> findByVinAndAssignedUser(String vin,AuthUser user) {
		return find.where().eq("vin", vin).eq("user", user).findList();
	}
	
	public static List<RequestMoreInfo> findAllReassigned(AuthUser user) {
		return find.where().eq("isReassigned", true).eq("assignedTo",user).findList();
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
	
}
