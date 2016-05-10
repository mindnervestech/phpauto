package models;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class HoursOfOperation extends Model {
	
	/*public Date sunOpenTime;
	public Date monOpenTime;
	public Date tueOpenTime;
	public Date wedOpenTime;
	public Date thuOpenTime;
	public Date friOpenTime;
	public Date satOpenTime;
	public Date sunCloseTime;
	public Date monCloseTime;
	public Date tueCloseTime;
	public Date wedCloseTime;
	public Date thuCloseTime;
	public Date friCloseTime;
	public Date satCloseTime;
	public Date sunOpen;
	public Date monOpen;
	public Date tueOpen;
	public Date wedOpen;
	public Date thuOpen;
	public Date friOpen;
	public Date satOpen;*/
	@Id
	public Long id;
	public Date closeTime;
	public Date openTime;
	public String day;
	public Integer dayFlag;
	public String typeOfOperation;
	
	
	public String getTypeOfOperation() {
		return typeOfOperation;
	}
	public void setTypeOfOperation(String typeOfOperation) {
		this.typeOfOperation = typeOfOperation;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	public Long getId() {
		return id;
	}
	public Integer getDayFlag() {
		return dayFlag;
	}
	public void setDayFlag(Integer dayFlag) {
		this.dayFlag = dayFlag;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}


	


	public static Finder<Long,HoursOfOperation> find = new Finder<>(Long.class,HoursOfOperation.class);
	
	public static HoursOfOperation findByDay(String day) {
		return find.where().eq("day", day).eq("typeOfOperation", "sales").findUnique();
	}
	
	public static HoursOfOperation findByDayForService(String day) {
		return find.where().eq("day", day).eq("typeOfOperation", "service").findUnique();
	}
	
	
}
