package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class PlanScheduleMonthlyLocation extends Model {

	@Id
	public Long id;
	public String month;
	public String totalEarning;
	public String minEarning;
	public String vehiclesSell;
	public String avgCheck;
	
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTotalEarning() {
		return totalEarning;
	}

	public void setTotalEarning(String totalEarning) {
		this.totalEarning = totalEarning;
	}

	public String getMinEarning() {
		return minEarning;
	}

	public void setMinEarning(String minEarning) {
		this.minEarning = minEarning;
	}

	public String getVehiclesSell() {
		return vehiclesSell;
	}

	public void setVehiclesSell(String vehiclesSell) {
		this.vehiclesSell = vehiclesSell;
	}

	public String getAvgCheck() {
		return avgCheck;
	}

	public void setAvgCheck(String avgCheck) {
		this.avgCheck = avgCheck;
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

	public static Finder<Long,PlanScheduleMonthlyLocation> find = new Finder<>(Long.class,PlanScheduleMonthlyLocation.class);
	
	public static PlanScheduleMonthlyLocation findById(Long id) {
		return find.byId(id);
	}
	
	public static PlanScheduleMonthlyLocation findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
	public static List<PlanScheduleMonthlyLocation> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	public static PlanScheduleMonthlyLocation findByLocationAndMonth(Location location,String month) {
		return find.where().eq("locations", location).eq("month", month).findUnique();
	}
	
	public static PlanScheduleMonthlyLocation findByUserMonth(AuthUser user,String month) {
		return find.where().eq("user", user).eq("month", month).findUnique();
	}
	
}
