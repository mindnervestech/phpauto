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
	public Date sunCloseTime;
	public Date monCloseTime;
	public Date tueCloseTime;
	public Date wedCloseTime;
	public Date thuCloseTime;
	public Date friCloseTime;
	public Date satCloseTime;
	public Integer sunOpen;
	public Integer monOpen;
	public Integer tueOpen;
	public Integer wedOpen;
	public Integer thuOpen;
	public Integer friOpen;
	public Integer satOpen;
	public String portalName;
	public Date contractDurStartDate;
	public Date contractDurEndDate;
	
	@ManyToOne
	public AuthUser user;
	
	@ManyToOne
	public Location locations;



	public Date getSunCloseTime() {
		return sunCloseTime;
	}

	public void setSunCloseTime(Date sunCloseTime) {
		this.sunCloseTime = sunCloseTime;
	}

	public Date getMonCloseTime() {
		return monCloseTime;
	}

	public void setMonCloseTime(Date monCloseTime) {
		this.monCloseTime = monCloseTime;
	}

	public Date getTueCloseTime() {
		return tueCloseTime;
	}

	public void setTueCloseTime(Date tueCloseTime) {
		this.tueCloseTime = tueCloseTime;
	}

	public Date getWedCloseTime() {
		return wedCloseTime;
	}

	public void setWedCloseTime(Date wedCloseTime) {
		this.wedCloseTime = wedCloseTime;
	}

	public Date getThuCloseTime() {
		return thuCloseTime;
	}

	public void setThuCloseTime(Date thuCloseTime) {
		this.thuCloseTime = thuCloseTime;
	}

	public Date getFriCloseTime() {
		return friCloseTime;
	}

	public void setFriCloseTime(Date friCloseTime) {
		this.friCloseTime = friCloseTime;
	}

	public Date getSatCloseTime() {
		return satCloseTime;
	}

	public void setSatCloseTime(Date satCloseTime) {
		this.satCloseTime = satCloseTime;
	}

	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPortalName() {
		return portalName;
	}

	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}

	public Date getContractDurStartDate() {
		return contractDurStartDate;
	}

	public void setContractDurStartDate(Date contractDurStartDate) {
		this.contractDurStartDate = contractDurStartDate;
	}

	public Date getContractDurEndDate() {
		return contractDurEndDate;
	}

	public void setContractDurEndDate(Date contractDurEndDate) {
		this.contractDurEndDate = contractDurEndDate;
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
	public static PhotographerHoursOfOperation findById(Long id) {
		return find.where().eq("id", id).findUnique();
	}
	
	public static PhotographerHoursOfOperation findByPortalNameAndLocation(String name, AuthUser user,Long id) {
		return find.where().eq("portalName", name).eq("user", user).findUnique();
	}
	
	
	
}
