package models;

import java.sql.Date;

import javax.persistence.Entity;

import play.db.ebean.Model;

@Entity
public class SchedularDate extends Model{

	public Long id;
	public String start_date;
	public String end_date;
	public String curr_time;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCurr_time() {
		return curr_time;
	}
	public void setCurr_time(String curr_time) {
		this.curr_time = curr_time;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}


	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	
	
	
}
