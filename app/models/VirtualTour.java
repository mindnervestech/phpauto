package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class VirtualTour extends Model {

	@Id
	public Long id;
	public String vin;
	public String desktopUrl;
	public String mobileUrl;
	
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

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getDesktopUrl() {
		return desktopUrl;
	}

	public void setDesktopUrl(String desktopUrl) {
		this.desktopUrl = desktopUrl;
	}

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
	
	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public static Finder<Long,VirtualTour> find = new Finder<>(Long.class,VirtualTour.class);
	
	public static VirtualTour findById(Long id) {
		return find.byId(id);
	}
	
	public static VirtualTour findByUserAndVin(AuthUser user,String vin) {
		return find.where().eq("user", user).eq("vin", vin).findUnique();
	}
	
}
