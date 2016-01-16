package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class PlanSalesTotal extends Model {

	@Id
	public Long id;
	public String total;
	
	
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

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
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

	public static Finder<Long,PlanSalesTotal> find = new Finder<>(Long.class,PlanSalesTotal.class);
	
	public static PlanSalesTotal findById(Long id) {
		return find.byId(id);
	}
	
	public static PlanSalesTotal findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
	public static PlanSalesTotal findByLocation(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	
}
