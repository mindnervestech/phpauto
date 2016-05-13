package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class PhotographerHoursOfOperation extends Model {
	
	@Id
	public Long id;
	public Date sunOpenTime;
	public Date monOpenTime;
	public Date tueOpenTime;
	public Date wedOpenTime;
	public Date thuOpenTime;
	public Date friOpenTime;
	public Date satOpenTime;
	public Integer sunOpen;
	public Integer monOpen;
	public Integer tueOpen;
	public Integer wedOpen;
	public Integer thuOpen;
	public Integer friOpen;
	public Integer satOpen;
	
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
	
	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}
	
	public Date getSunOpenTime() {
		return sunOpenTime;
	}
	public void setSunOpenTime(Date sunOpenTime) {
		this.sunOpenTime = sunOpenTime;
	}
	public Date getMonOpenTime() {
		return monOpenTime;
	}
	public void setMonOpenTime(Date monOpenTime) {
		this.monOpenTime = monOpenTime;
	}
	public Date getTueOpenTime() {
		return tueOpenTime;
	}
	public void setTueOpenTime(Date tueOpenTime) {
		this.tueOpenTime = tueOpenTime;
	}
	public Date getWedOpenTime() {
		return wedOpenTime;
	}
	public void setWedOpenTime(Date wedOpenTime) {
		this.wedOpenTime = wedOpenTime;
	}
	public Date getThuOpenTime() {
		return thuOpenTime;
	}
	public void setThuOpenTime(Date thuOpenTime) {
		this.thuOpenTime = thuOpenTime;
	}
	public Date getFriOpenTime() {
		return friOpenTime;
	}
	public void setFriOpenTime(Date friOpenTime) {
		this.friOpenTime = friOpenTime;
	}
	public Date getSatOpenTime() {
		return satOpenTime;
	}
	public void setSatOpenTime(Date satOpenTime) {
		this.satOpenTime = satOpenTime;
	}
	

	public Integer getSunOpen() {
		return sunOpen;
	}
	public void setSunOpen(Integer sunOpen) {
		this.sunOpen = sunOpen;
	}
	public Integer getMonOpen() {
		return monOpen;
	}
	public void setMonOpen(Integer monOpen) {
		this.monOpen = monOpen;
	}
	public Integer getTueOpen() {
		return tueOpen;
	}
	public void setTueOpen(Integer tueOpen) {
		this.tueOpen = tueOpen;
	}
	public Integer getWedOpen() {
		return wedOpen;
	}
	public void setWedOpen(Integer wedOpen) {
		this.wedOpen = wedOpen;
	}
	public Integer getThuOpen() {
		return thuOpen;
	}
	public void setThuOpen(Integer thuOpen) {
		this.thuOpen = thuOpen;
	}
	public Integer getFriOpen() {
		return friOpen;
	}
	public void setFriOpen(Integer friOpen) {
		this.friOpen = friOpen;
	}
	public Integer getSatOpen() {
		return satOpen;
	}
	public void setSatOpen(Integer satOpen) {
		this.satOpen = satOpen;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}


	public static Finder<Long,PhotographerHoursOfOperation> find = new Finder<>(Long.class,PhotographerHoursOfOperation.class);
	
	public static List<PhotographerHoursOfOperation> findByListUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	public static PhotographerHoursOfOperation findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
	
	
}
