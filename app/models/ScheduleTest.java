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
	public Date scheduleTime;
	public String declineReason;
	public String vin;
	public int isRead;
	public int notifFlag;
	public Date confirmDate;
	public Date confirmTime;
	public Date confirmEndTime;
	public Date meetingActionTime;
	public Date getConfirmEndTime() {
		return confirmEndTime;
	}
	public void setConfirmEndTime(Date confirmEndTime) {
		this.confirmEndTime = confirmEndTime;
	}
	public String sessionId;
	public String leadStatus;
	public String reason;
	public String declineUser;
	public int getNotifFlag() {
		return notifFlag;
	}
	public void setNotifFlag(int notifFlag) {
		this.notifFlag = notifFlag;
	}
	public Integer meeting;
	//public Integer noteFlag;
	@ManyToOne
	public AuthUser assignedTo;
	public Integer getMeeting() {
		return meeting;
	}
	public void setMeeting(Integer meeting) {
		this.meeting = meeting;
	}

	
	public Date getMeetingActionTime() {
		return meetingActionTime;
	}
	public void setMeetingActionTime(Date meetingActionTime) {
		this.meetingActionTime = meetingActionTime;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}




	@ManyToOne
	public AuthUser user;
	@ManyToOne
	public Location locations;
	public Boolean isReassigned = true;
	public String contactedFrom;
	public String hearedFrom;
	public String meetingStatus;
	public String location;
	public Long groupId;
	public String google_id;
	public String custZipCode;
	public Boolean is_google_data;
	public int scheduleEmail;
	public String enthicity;
	public Date statusDate;
	public Date statusTime;
	public String testDriveCompletedComment;
	public String testDriveCompletedDuration;
	public int onlineOrOfflineLeads;
	public Integer premiumFlag;
	public Long parentId;
	public String testDriveStatus;
	public Integer sendInvitation;
	public Integer acceptMeeting;
	public Integer declineMeeting;
	public Integer declineUpdate;
	public Integer deleteMsgFlag;
	
	
	
	
	
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
	public Integer getDeclineUpdate() {
		return declineUpdate;
	}
	public void setDeclineUpdate(Integer declineUpdate) {
		this.declineUpdate = declineUpdate;
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
	
	public Date getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public Date getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
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
	
	public Integer getDeleteMsgFlag() {
		return deleteMsgFlag;
	}
	public void setDeleteMsgFlag(Integer deleteMsgFlag) {
		this.deleteMsgFlag = deleteMsgFlag;
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

	
	public String getMeetingStatus() {
		return meetingStatus;
	}
	public void setMeetingStatus(String meetingStatus) {
		this.meetingStatus = meetingStatus;
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


	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getDeclineUser() {
		return declineUser;
	}
	public void setDeclineUser(String declineUser) {
		this.declineUser = declineUser;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getDeclineReason() {
		return declineReason;
	}
	public void setDeclineReason(String declineReason) {
		this.declineReason = declineReason;
	}
	public Integer getSendInvitation() {
		return sendInvitation;
	}
	public void setSendInvitation(Integer sendInvitation) {
		this.sendInvitation = sendInvitation;
	}
	public Integer getAcceptMeeting() {
		return acceptMeeting;
	}
	public void setAcceptMeeting(Integer acceptMeeting) {
		this.acceptMeeting = acceptMeeting;
	}
	public Integer getDeclineMeeting() {
		return declineMeeting;
	}
	public void setDeclineMeeting(Integer declineMeeting) {
		this.declineMeeting = declineMeeting;
	}





	public static Finder<Long,ScheduleTest> find = new Finder<>(Long.class,ScheduleTest.class);
	
	public static List<ScheduleTest> findAllReassigned(AuthUser user) {
		return find.where().eq("isReassigned", true).eq("assignedTo",user).findList();
	}
	
	public static ScheduleTest findById(Long id) {
		return find.byId(id);
	}
	
	public static ScheduleTest findByIdAndParent(Long id) {
		return find.where().eq("id", id).eq("leadStatus", null).findUnique();
	}
	
	public static List<ScheduleTest> findAllByDate() {
		return find.where().eq("assignedTo", null).eq("isRead", 0).ne("vin", "no").orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllGroupMeeting(Long groupId) {
		return find.where().eq("groupId", groupId).findList();
	}
	
	public static List<ScheduleTest> findAllGroupMeetingCheckMeeting(Long groupId) {
		return find.where().eq("groupId", groupId).ne("meeting", 2).findList();
	}
	
	public static ScheduleTest findAllGroupUserMeeting(Long groupId,AuthUser user) {
		return find.where().eq("groupId", groupId).eq("assignedTo", user).findUnique();
	}
	
	public static List<ScheduleTest> findAllByLocationDate(Long locationId) {
		return find.where().eq("assignedTo", null).eq("locations.id",locationId).ne("vin", "no").eq("isRead", 0).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllData() {
		return find.all();
	}
	
	public static List<ScheduleTest> findAllLocationData(Long locationId) {
		return find.where().eq("leadStatus", null).eq("premiumFlag", 0).eq("assignedTo", null).eq("locations.id",locationId).ne("vin", "no").eq("isRead",false).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllLocationDataManager(Long locationId) {
		return find.where().eq("leadStatus", null).eq("locations.id",locationId).ne("vin", "no").eq("premiumFlag", 0).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllLocationDataManagerPremium(Long locationId) {
		return find.where().eq("leadStatus", null).eq("locations.id",locationId).ne("vin", "no").eq("premiumFlag", 1).orderBy("scheduleDate desc").findList();
	}
	
	
	public static List<ScheduleTest> findAllAssigned(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("leadStatus", null).ne("vin", "no").eq("isRead",true).orderBy("scheduleDate desc").findList();
	}
	public static List<ScheduleTest> findAllNullStatusLeads(Date beforeSevenDays) {
		return find.where().eq("leadStatus", null).ge("scheduleTime", beforeSevenDays).findList();
	}
	
	public static List<ScheduleTest> findAllAssignedforSold(AuthUser user) {
		return find.where().add(Expr.or(Expr.eq("leadStatus", null),Expr.eq("leadStatus", "COMPLETE"))).eq("assignedTo", user).ne("vin", "no").eq("isRead",true).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllAssignedOffine(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("onlineOrOfflineLeads",0).ne("vin", "no").orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllAssignedOnline(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("onlineOrOfflineLeads",1).ne("vin", "no").orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> getdecline(AuthUser user) {
		return find.where().eq("user", user).eq("declineMeeting", 2).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> getdeleteMsg(AuthUser user) {
		return find.where().add(Expr.or(Expr.eq("assignedTo", user),Expr.eq("user", user))).eq("deleteMsgFlag", 1).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> getaccepted(AuthUser user) {
		return find.where().eq("user", user).eq("acceptMeeting", 2).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> getUpdateMeeting(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("declineUpdate", 1).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllLostSch(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("leadStatus", "LOST").findList();
	}
	
	public static List<ScheduleTest> findAllByParentID(Long parentId) {
		return find.where().eq("parentId", parentId).eq("leadStatus", null).findList();
	}
	
	public static List<ScheduleTest> findAllAssignedLeadsToUser(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("leadStatus", null).orderBy("scheduleDate desc").findList();
	}
	public static List<ScheduleTest> findAllAssignedLeadsToUser1(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("leadStatus", null).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllCompletedToUser(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("leadStatus", null).eq("testDriveStatus","TestDriveCompleted").orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllSeenComplete(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("leadStatus", "COMPLETE").orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllAssignedLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).eq("leadStatus", null).eq("isRead",true).ne("vin", "no").orderBy("scheduleDate desc").findList();
	}
	
	public static int findAll(Long locationId) {
		return find.where().eq("isRead", 0).ne("vin", "no").eq("premiumFlag", 0).eq("locations.id", locationId).eq("leadStatus", null).findRowCount();
	}
	public static List<ScheduleTest> findAllLeads(Long locationId) {
		return find.where().eq("isRead", 0).ne("vin", "no").eq("premiumFlag", 0).eq("locations.id", locationId).eq("leadStatus", null).findList();
	}
	public static List<ScheduleTest> findAllByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<ScheduleTest> findAllByUserAssigned(AuthUser user) {
		return find.where().eq("assignedTo", user).findList();
	}
	
	public static List<ScheduleTest> findAllByServiceTestEmail(Date lessDate) {
		
		return find.where().add(Expr.or(Expr.ne("acceptMeeting", 1),Expr.eq("acceptMeeting", null))).ne("confirmDate",null).eq("deleteMsgFlag", null).eq("lead_status", null).ge("confirmDate", lessDate).orderBy("confirmDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllByServiceTestPopup(AuthUser user,Date currDate) {
		
		return find.where().ne("acceptMeeting", 1).ne("confirmDate",null).eq("assignedTo", user).ge("confirmDate", currDate).eq("lead_status", null).orderBy("confirmDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllByServiceTest(Date currDate) {
		
		return find.where().ne("acceptMeeting", 1).ne("confirmDate",null).eq("lead_status", null).ge("confirmDate", currDate).orderBy("confirmDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllByUserServiceTest(AuthUser user, Date currDate) {
		
		return find.where().add(Expr.or(Expr.eq("assignedTo", user),Expr.eq("user", user))).add(Expr.or(Expr.eq("acceptMeeting", null),Expr.eq("acceptMeeting", 0))).ne("confirmDate",null).eq("lead_status", null).ge("confirmDate", currDate).orderBy("confirmDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllByUserService(AuthUser user, Date currDate) {
		
		return find.where().eq("assignedTo", user).ne("acceptMeeting", 1).eq("deleteMsgFlag",null).eq("lead_status", null).ge("confirmDate", currDate).orderBy("confirmDate desc").findList();
	}
	
	public static List<ScheduleTest> findForUser(AuthUser user,Date currDate) {
		
		return find.where().eq("user", user).eq("deleteMsgFlag",null).eq("lead_status", null).ge("confirmDate", currDate).orderBy("confirmDate desc").findList();
	}
	
	
	
	public static List<ScheduleTest> findAllByInvitationTest(AuthUser user, Date currDate) {
		return find.where().eq("assignedTo", user).ne("confirmDate",null).eq("lead_status", null).eq("sendInvitation", 1).ge("confirmDate", currDate).orderBy("confirmDate desc").findList();
	}
	
	public static List<ScheduleTest> findByVinAndAssignedUser(AuthUser user,String vin) {
		return find.where().eq("assignedTo", user).eq("vin", vin).findList();
	}
	
	/*public static List<ScheduleTest> findByDateAndAssignedUser(AuthUser user,Date date) {
		return find.where().add(Expr.or(Expr.eq("assignedTo", user),Expr.eq("user", user))).eq("declineUser", null).eq("confirmDate", date).ne("acceptMeeting", 1).eq("lead_status", null).findList();
	}*/
	public static List<ScheduleTest> findByDateAndAssignedUser(AuthUser user,Date date) {
		return find.where().add(Expr.or(Expr.eq("assignedTo", user),Expr.eq("user", user))).eq("declineUser", null).eq("confirmDate", date).eq("lead_status", null).findList();
	}
	
	public static List<ScheduleTest> findByConfirmLeads(Long locationId, AuthUser user) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("locations.id", locationId).eq("lead_status", null).eq("testDriveStatus", null).eq("assignedTo", user).findList();
	}
	
	public static List<ScheduleTest> testDriveForSalePerson(AuthUser user) {
		return find.where().ne("confirmTime", null).ne("confirmDate", null).eq("assignedTo", user).findList();
	}
	public static List<ScheduleTest> getAllFailed(Long locationId) {
		return find.where().add(Expr.or(Expr.eq("lead_status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).eq("locations.id", locationId).findList();		
		//return find.where().add(Expr.or(Expr.eq("status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).findList();
	}
	
	public static List<ScheduleTest> getAllFailedById(Long locationId, AuthUser user) {
		return find.where().add(Expr.or(Expr.eq("lead_status", "CANCEL"),Expr.eq("lead_status", "FAILED"))).eq("locations.id", locationId).eq("assignedTo", user).findList();
	}
	
	public static List<SqlRow> getScheduleDates(AuthUser user, String cd) {
		SqlQuery q = Ebean.createSqlQuery("select distinct schedule_test.confirm_date ,schedule_test.id  from schedule_test where (schedule_test.assigned_to_id = '"+user.id+"' or schedule_test.user_id = '"+user.id+"') and schedule_test.confirm_date >= '"+cd+"' and schedule_test.delete_msg_flag is null and schedule_test.lead_status is null");
		List<SqlRow> rows = q.findList();
		return rows;
	}
	public static List<ScheduleTest> getScheduleDatesCal(AuthUser user, Date cd) {
		return find.where().add(Expr.or(Expr.eq("assignedTo", user),Expr.eq("user", user))).ne("confirmTime", null).ge("confirmDate", cd).ne("confirmDate", null).findList();
	}
	
	
	public static List<ScheduleTest> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	public static List<ScheduleTest> findByLocationNotOpenLead(Long location) {
		return find.where().eq("locations.id", location).ne("lead_status", null).findList();
	}
	
	public static List<ScheduleTest> findAllCancelLeadsForUser(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("lead_status", "CANCEL").findList();
	}
	
	
	public static List<ScheduleTest> findByLocationNotCancel(Long location) {
		return find.where().eq("locations.id", location).ne("leadStatus", "CANCEL").findList();
	}
	
	public static List<ScheduleTest>  findByVinAndLocation(String vin,Location location) {
		return find.where().eq("vin", vin).eq("leadStatus", null).eq("locations", location).findList();
	}
	
	public static List<ScheduleTest>  findByVinSoldAndLocation(String vin,Location location) {
		return find.where().eq("vin", vin).ne("leadStatus", null).eq("locations", location).findList();
	}
	
	
	
	public static List<ScheduleTest> findAllByAssignedUser(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("vin", "no").findList();
	}
	public static List<ScheduleTest> findAllByNotOpenLead(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("leadStatus", null).findList();
	}
	
	public static List<ScheduleTest> findByAssigUserNotCancel(AuthUser user) {
		return find.where().eq("assignedTo", user).ne("vin", "no").ne("leadStatus", "CANCEL").findList();
	}
	
	public static List<ScheduleTest> findByLocationNotCancel(Location location) {
		return find.where().eq("locations", location).ne("vin", "no").ne("leadStatus", "CANCEL").findList();
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
	public static List<ScheduleTest> findByVinStatus(String vin) {
		return find.where().eq("vin", vin).eq("leadStatus", null).findList();
	}
	public static List<ScheduleTest> findByUserDate(AuthUser user, Date date) {
		return find.where().add(Expr.or(Expr.ne("acceptMeeting", 1),Expr.eq("acceptMeeting", null))).add(Expr.or(Expr.eq("assignedTo", user),Expr.eq("user", user))).eq("confirm_date", date).eq("leadStatus", null).findList();
	}
	public static List<ScheduleTest> findByVinDate(String vin, Date date) {
		return find.where().eq("vin", vin).eq("confirm_date", date).findList();
	}
	public static List<ScheduleTest> findByScheduler() {
		return find.where().eq("schedule_email", 0).findList();
	}
}
