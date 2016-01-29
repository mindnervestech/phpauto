package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ScheduleTest extends Model {

	@Id
	public Long id;
	public String name;
	public String email;
	public String preferredContact;
	public String phone;
	public String bestDay;
	public String bestTime;
	public Date scheduleDate;
	public String vin;
	public int isRead;
	public Date confirmDate;
	public Date confirmTime;
	public String leadStatus;
	public String reason;
	//public Integer noteFlag;
	@ManyToOne
	public AuthUser assignedTo;
	@ManyToOne
	public AuthUser user;
	@ManyToOne
	public Location locations;
	public Boolean isReassigned = true;
	public String contactedFrom;
	public String hearedFrom;
	public String status;
	public String location;
	public String google_id;
	public String custZipCode;
	public Boolean is_google_data;
	public int scheduleEmail;
	public String enthicity;
	public Date statusDate;
	public Integer premiumFlag;
	
	
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public Date getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}
	public AuthUser getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(AuthUser assignedTo) {
		this.assignedTo = assignedTo;
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Integer getPremiumFlag() {
		return premiumFlag;
	}
	public void setPremiumFlag(Integer premiumFlag) {
		this.premiumFlag = premiumFlag;
	}
	/*public Integer getNoteFlag() {
		return noteFlag;
	}
	public void setNoteFlag(Integer noteFlag) {
		this.noteFlag = noteFlag;
	}*/
	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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



	public static Finder<Long,ScheduleTest> find = new Finder<>(Long.class,ScheduleTest.class);
	
	public static List<ScheduleTest> findAllReassigned(AuthUser user) {
		return find.where().eq("isReassigned", true).eq("assignedTo",user).findList();
	}
	
	public static ScheduleTest findById(Long id) {
		return find.byId(id);
	}
	
	public static List<ScheduleTest> findAllByDate() {
		return find.where().eq("assignedTo", null).eq("isRead", 0).ne("vin", "no").orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllByLocationDate(Long locationId) {
		return find.where().eq("assignedTo", null).eq("locations.id",locationId).ne("vin", "no").eq("isRead", 0).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllData() {
		return find.all();
	}
	
	public static List<ScheduleTest> findAllLocationData(Long locationId) {
		return find.where().eq("leadStatus", null).eq("locations.id",locationId).ne("vin", "no").eq("isRead",false).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllLocationDataManager(Long locationId) {
		return find.where().eq("leadStatus", null).eq("locations.id",locationId).ne("vin", "no").orderBy("scheduleDate desc").findList();
	}
	
	
	public static List<ScheduleTest> findAllAssigned(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("leadStatus", null).ne("vin", "no").eq("isRead",true).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllAssignedLeadsToUser(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("leadStatus", null).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllSeenComplete(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("leadStatus", "COMPLETE").orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllAssignedLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).eq("leadStatus", null).eq("isRead",true).ne("vin", "no").orderBy("scheduleDate desc").findList();
	}
	
	public static int findAll() {
		return find.where().eq("isRead", 0).ne("vin", "no").eq("leadStatus", null).findRowCount();
	}
	
	public static List<ScheduleTest> findAllByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<ScheduleTest> findAllByUserAssigned(AuthUser user) {
		return find.where().eq("assignedTo", user).findList();
	}
	
	
	public static List<ScheduleTest> findAllByUserServiceTest(AuthUser user) {
		return find.where().eq("assignedTo", user).orderBy("confirmDate desc").setMaxRows(3).findList();
	}
	
	public static List<ScheduleTest> findByVinAndAssignedUser(AuthUser user,String vin) {
		return find.where().eq("assignedTo", user).eq("vin", vin).findList();
	}
	
	public static List<ScheduleTest> findByDateAndAssignedUser(AuthUser user,Date date) {
		return find.where().eq("assignedTo", user).eq("confirmDate", date).findList();
	}
	
	public static List<ScheduleTest> getAllFailed() {
		return find.where().add(Expr.or(Expr.eq("lead_status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).findList();
		
		//return find.where().add(Expr.or(Expr.eq("status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).findList();
	}
	
	public static List<SqlRow> getScheduleDates(AuthUser user) {
		SqlQuery q = Ebean.createSqlQuery("select distinct schedule_test.confirm_date from schedule_test where schedule_test.assigned_to_id = '"+user.id+"' and schedule_test.confirm_date is not null");
		List<SqlRow> rows = q.findList();
		return rows;
	}
	
	public static List<ScheduleTest> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	public static List<ScheduleTest>  findByVinAndLocation(String vin,Location location) {
		return find.where().eq("vin", vin).eq("leadStatus", null).eq("locations", location).findList();
	}
	
	public static List<ScheduleTest> findAllByAssignedUser(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("vin", "no").findList();
	}
	
	public static SqlRow getTopPerformers(String start,String end,Integer id) {
		SqlQuery q = Ebean.createSqlQuery("select count(*) as total,(select count(*) from schedule_test where schedule_test.assigned_to_id = '"+id+"' and (schedule_test.confirm_date between '"+start+"' and '"+end+"') and schedule_test.lead_status = 'COMPLETE') as success,(select sum(vehicle.price) from vehicle where status = 'Sold' and vehicle.vin in (select schedule_test.vin from schedule_test where schedule_test.assigned_to_id = '"+id+"' and (schedule_test.confirm_date between '"+start+"' and '"+end+"') and schedule_test.lead_status = 'COMPLETE') ) as samount,(select sum(vehicle.price) as ramount from vehicle where vehicle.vin in (select request_more_info.vin from request_more_info where request_more_info.assigned_to_id = '"+id+"' and (request_more_info.confirm_date between '"+start+"' and '"+end+"') and request_more_info.status = 'COMPLETE')),(select sum(vehicle.price) from vehicle where vehicle.vin in (select trade_in.vin from trade_in where trade_in.assigned_to_id = '"+id+"' and (trade_in.confirm_date between '"+start+"' and '"+end+"') and trade_in.status = 'COMPLETE') ) as tramount,(select count(*) from schedule_test where schedule_test.assigned_to_id = '"+id+"' and schedule_test.lead_status is null and is_read = 1) as leads,(select count(*) from request_more_info where request_more_info.assigned_to_id = '"+id+"' and request_more_info.status is null and is_read = 1) as requestleads,(select count(*) from trade_in where trade_in.assigned_to_id = '"+id+"' and trade_in.status is null and is_read = 1) as tradeInleads from schedule_test where schedule_test.assigned_to_id = '"+id+"' and (schedule_test.confirm_date between '"+start+"' and '"+end+"') and (schedule_test.lead_status = 'COMPLETE' or schedule_test.lead_status = 'LOST')");
		SqlRow row = q.findUnique();
		return row;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getGoogle_id() {
		return google_id;
	}
	public void setGoogle_id(String google_id) {
		this.google_id = google_id;
	}
	public Boolean getIs_google_data() {
		return is_google_data;
	}
	public void setIs_google_data(Boolean is_google_data) {
		this.is_google_data = is_google_data;
	}
	public static List<ScheduleTest> findByVin(String vin) {
		return find.where().eq("vin", vin).findList();
	}
	public static List<ScheduleTest> findByVinDate(String vin, Date date) {
		return find.where().eq("vin", vin).eq("confirm_date", date).findList();
	}
	public static List<ScheduleTest> findByScheduler() {
		return find.where().eq("schedule_email", 0).findList();
	}
}
