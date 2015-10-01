package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
		return find.where().eq("assignedTo", user).eq("isRead", 1).eq("status", null).orderBy("requestDate desc").findList();
	}
	
	public static List<RequestMoreInfo> findAllCancel() {
		return find.where().eq("status", "CANCEL").findList();
	}
	
	public static int findAll() {
		return find.where().eq("isRead", 0).findRowCount();
	}
	
	public static List<RequestMoreInfo> findAllByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
}
