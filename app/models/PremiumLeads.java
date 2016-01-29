package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class PremiumLeads extends Model {

	@Id
	public Long id;
	public String premium_amount;
	public Integer premium_flag;
	
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

	public String getPremium_amount() {
		return premium_amount;
	}

	public void setPremium_amount(String premium_amount) {
		this.premium_amount = premium_amount;
	}

	public Integer getPremium_flag() {
		return premium_flag;
	}

	public void setPremium_flag(Integer premium_flag) {
		this.premium_flag = premium_flag;
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

	public static Finder<Long,PremiumLeads> find = new Finder<>(Long.class,PremiumLeads.class);
	
	public static PremiumLeads findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
	public static PremiumLeads findByLocation(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	
}
