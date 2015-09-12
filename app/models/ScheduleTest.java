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

	public static Finder<Long,ScheduleTest> find = new Finder<>(Long.class,ScheduleTest.class);
	
	public static ScheduleTest findById(Long id) {
		return find.byId(id);
	}
	
	public static List<ScheduleTest> findAllByDate(AuthUser user) {
		return find.where().eq("assignedTo", null).eq("isRead", 0).orderBy("scheduleDate desc").findList();
	}
	
	public static List<ScheduleTest> findAllAssigned(AuthUser user) {
		return find.where().eq("assignedTo", user).eq("leadStatus", null).orderBy("scheduleDate desc").findList();
	}
	
	public static int findAll() {
		return find.where().eq("isRead", 0).findRowCount();
	}
	
	public static List<ScheduleTest> findAllByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<ScheduleTest> findByVinAndAssignedUser(AuthUser user,String vin) {
		return find.where().eq("assignedTo", user).eq("vin", vin).findList();
	}
	
	public static List<SqlRow> getScheduleDates(AuthUser user) {
		SqlQuery q = Ebean.createSqlQuery("select schedule_test.confirm_date from schedule_test where schedule_test.confirm_date >= CURDATE() and schedule_test.assigned_to_id = '"+user.id+"'");
		List<SqlRow> rows = q.findList();
		return rows;
	}
	
}
