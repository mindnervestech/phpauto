package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Expr;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class PriceAlert extends Model {

	@Id
	public Long id;
	public String email;
	public String name;
	public String vin;
	public String sendEmail;
	public Integer oldPrice;
	public Integer popupFlag;
	public Date currDate;
	@ManyToOne
	public AuthUser user;
	
	@ManyToOne
	public Location locations;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	public Integer getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(Integer oldPrice) {
		this.oldPrice = oldPrice;
	}

	public Date getCurrDate() {
		return currDate;
	}
	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}
	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public Integer getPopupFlag() {
		return popupFlag;
	}
	public void setPopupFlag(Integer popupFlag) {
		this.popupFlag = popupFlag;
	}


	public static Finder<Long,PriceAlert> find = new Finder<>(Long.class,PriceAlert.class);
	
	public static PriceAlert findById(Long id) {
		return find.byId(id);
	}
	
	public static List<PriceAlert> getEmailsByStatus(AuthUser user) {
		return find.where().eq("user", user).eq("sendEmail","Y").findList();
	}
	
	public static List<PriceAlert> getAllRecord() {
		return find.all();
	}
	
	public static List<PriceAlert> getAllRecordPopUp() {
		return find.where().eq("popupFlag", 1).findList();
	}
	
	public static List<PriceAlert> getEmailsByStatusVin(String vin) {
		return find.where().eq("vin", vin).eq("sendEmail","Y").findList();
	}
	public static List<PriceAlert> getByStatus(AuthUser user) {
		return find.where().eq("user", user).findList();
	}

	public static List<PriceAlert> getByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	public static List<PriceAlert> getEmailsByVin(String vin,Long location) {
		return find.where().eq("vin", vin).eq("locations.id",location).findList();
	}
	public static List<PriceAlert> getByVin(String vin) {
		return find.where().eq("vin", vin).findList();
	}
	
	
	
}
