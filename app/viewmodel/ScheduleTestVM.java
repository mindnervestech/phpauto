package viewmodel;

import java.util.ArrayList;
import java.util.List;

public class ScheduleTestVM {
	
	public String disabled;
	public String firstName;
	public String lastName;
	public String fullName;
	public String isSelect;
	public String per;
	public String role;
	public String userStatus;
	public String meetingFlag;
	public String bodyStyle;
	public String custZipCode;
	public String drivetrain;
	public String engine;
	public String enthicity;
	public String howContactedUs;
	public String howFoundUs;
	public String imageUrl;
	public String imgId;
	public String isgoogle;
	public String leadType;
	public String leadsValue;
	public String make;
	public String meeting;
	public String mileage;
	public String model;
	public String note;
	public String noteFlag;
	public String parentChildLead;
	public String pdfPath;
	public String prefferedContact;
	public String price;
	public String requestDate;
	public String salesRep;
	public String status;
	public String statusDate;
	public String stock;
	public String testDriveCompletedComment;
	public String testDriveCompletedDuration;
	public String transmission;
	public String trim;
	public String typeofVehicle;
	public String userRole;
	public String declineUser;
	
	
	
	
	
	public Long id;
	public String name;
	public String email;
	public String preferredContact;
	public String phone;
	public String bestDay;
	public String bestTime;
	public String bestEndTime;
	public String scheduleDate;
	public String getBestEndTime() {
		return bestEndTime;
	}
	public void setBestEndTime(String bestEndTime) {
		this.bestEndTime = bestEndTime;
	}
	public String vin;
	public String year;
	public String confirmDateOrderBy;
	public int isRead;
	public String confDate;
	public String confTime;
	public String confirmDate;
	public String getConfirmEndTime() {
		return confirmEndTime;
	}
	public void setConfirmEndTime(String confirmEndTime) {
		this.confirmEndTime = confirmEndTime;
	}
	public String confirmTime;
	public String confirmEndTime;
	public Long groupId;
	public String leadStatus;
	public String reason;
	public String option;
	public String setFlagSameUser;
	public String typeOfLead;
	public Boolean isReassigned = true;
	public String contactedFrom;
	public String hearedFrom;
	public String location;
	public String google_id;
	public Boolean is_google;
	public Boolean is_google_data;
	public String assignedTo;
	public String Enthicity;
	public Boolean allStaff;
	public String meetingStatus;
	public List<UserVM> userdata;
	public List<UserVM> usersList = new ArrayList<UserVM>();
	public List<UserVM> getUsersList() {
		return usersList;
	}
	public String getConfDate() {
		return confDate;
	}
	public void setConfDate(String confDate) {
		this.confDate = confDate;
	}
	public String getConfTime() {
		return confTime;
	}
	public void setConfTime(String confTime) {
		this.confTime = confTime;
	}
	
	
	public List<UserVM> getUserdata() {
		return userdata;
	}
	public void setUserdata(List<UserVM> userdata) {
		this.userdata = userdata;
	}
	public void setUsersList(List<UserVM> usersList) {
		this.usersList = usersList;
	}
	public Boolean getAllStaff() {
		return allStaff;
	}
	public void setAllStaff(Boolean allStaff) {
		this.allStaff = allStaff;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
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
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(String confirmTime) {
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
	public Boolean getIsReassigned() {
		return isReassigned;
	}
	public void setIsReassigned(Boolean isReassigned) {
		this.isReassigned = isReassigned;
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
}
