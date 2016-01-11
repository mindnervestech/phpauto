package models;

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
	public String vin;
	public String sendEmail;
	public Integer oldPrice;
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


	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}


	public static Finder<Long,PriceAlert> find = new Finder<>(Long.class,PriceAlert.class);
	
	public static PriceAlert findById(Long id) {
		return find.byId(id);
	}
	
	public static List<PriceAlert> getEmailsByStatus(AuthUser user) {
		return find.where().eq("user", user).eq("sendEmail","Y").findList();
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
	
	
	
}
