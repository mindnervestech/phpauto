package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class LeadType extends Model {

	@Id
	public Long id;
	public String leadName;
	public Integer shows;
	public String profile;
		
	@ManyToOne
	public Location locations;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getLeadName() {
		return leadName;
	}


	public Integer getShows() {
		return shows;
	}

	public void setShows(Integer shows) {
		this.shows = shows;
	}

	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}
	public static Finder<Long,LeadType> find = new Finder<>(Long.class,LeadType.class);
	
	public static LeadType findById(Long id) {
		return find.byId(id);
	}
	
	public static List<LeadType> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	public static LeadType findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	public static LeadType findByName(String name) {
		return find.where().eq("lead_name", name).findUnique();
	}
	
	public static List<LeadType> findByLocationsAndSelected(Long location) {
		return find.where().eq("shows", 1).eq("locations.id", location).findList();
	}
	
	public static List<LeadType> getLeadData() {
		return find.all();
	}
	
	
}
