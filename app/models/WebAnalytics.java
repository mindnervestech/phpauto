package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class WebAnalytics extends Model{
	@Id
	public Long id;
	public String tracking_code;
	
	
	@ManyToOne
	public Location locations;
	
	public static Finder<Long,WebAnalytics> find = new Finder<>(Long.class,WebAnalytics.class);
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTracking_code() {
		return tracking_code;
	}
	public void setTracking_code(String tracking_code) {
		this.tracking_code = tracking_code;
	}
	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}
	
	public static WebAnalytics findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	public static WebAnalytics findById(Long id) {
		return find.byId(id);
	}
	
}
