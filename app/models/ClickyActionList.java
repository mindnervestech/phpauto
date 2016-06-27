package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ClickyActionList extends Model {

	@Id
	
	public Long id;
	public String time;
	public String time_pretty;
	public String ip_address;
	public String uid;
	public String session_id;
	public String action_type;
	public String action_title;
	public String action_url;
	public String stats_url;
	public Date currDate;
	
	
	public static Finder<Long,ClickyActionList> find = new Finder<>(Long.class,ClickyActionList.class);


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime_pretty() {
		return time_pretty;
	}

	public void setTime_pretty(String time_pretty) {
		this.time_pretty = time_pretty;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public String getAction_title() {
		return action_title;
	}

	public void setAction_title(String action_title) {
		this.action_title = action_title;
	}

	public String getAction_url() {
		return action_url;
	}

	public void setAction_url(String action_url) {
		this.action_url = action_url;
	}

	public String getStats_url() {
		return stats_url;
	}

	public void setStats_url(String stats_url) {
		this.stats_url = stats_url;
	}

	public Date getCurrDate() {
		return currDate;
	}

	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}

	public static List<ClickyActionList> getAll(Date sDate,Date eDate) {
		return find.where().between("currDate", sDate, eDate).findList();
	}
	
	public static List<ClickyActionList> getfindAll() {
		return find.all();
	}
	public static List<ClickyActionList> getClickyUnikue(String uid,String sessionId) {
		return find.where().eq("uid", uid).eq("session_id", sessionId).findList();
	}
	public static List<ClickyActionList> getcurr_date(Date curr_date){
		return find.where().eq("curr_date" , curr_date).findList();
	}
}
