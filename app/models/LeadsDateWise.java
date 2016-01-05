package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class LeadsDateWise extends Model {

	@Id
	public Long id;
	public String leads;
	public Date leadsDate;
	public String goalSetTime;
	
	
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

	public String getLeads() {
		return leads;
	}

	public void setLeads(String leads) {
		this.leads = leads;
	}

	public Date getLeadsDate() {
		return leadsDate;
	}

	public void setLeadsDate(Date leadsDate) {
		this.leadsDate = leadsDate;
	}

	public String getGoalSetTime() {
		return goalSetTime;
	}

	public void setGoalSetTime(String goalSetTime) {
		this.goalSetTime = goalSetTime;
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


	public static Finder<Long,LeadsDateWise> find = new Finder<>(Long.class,LeadsDateWise.class);
	
	public static LeadsDateWise findById(Long id) {
		return find.byId(id);
	}
	
	public static List<LeadsDateWise> getAllVehicles() {
		return find.all();
	}

	public static List<LeadsDateWise> getAllVehicles(AuthUser user) {
		return find.where().eq("user", user).findList();
		
	}
	
	public static List<LeadsDateWise> findByLocation(Location location) {
		return find.where().eq("locations", location).findList();
	}
	
	
}
