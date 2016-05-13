package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class MarketingAcounts extends Model {
	
	@Id
	public Long id;
	public String username;
	public String passward;
	public String sitename;
	
	@ManyToOne
	public Location locations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassward() {
		return passward;
	}

	public void setPassward(String passward) {
		this.passward = passward;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}
	
	
	public static Finder<Long,MarketingAcounts> find = new Finder<>(Long.class,MarketingAcounts.class);
	
	public static MarketingAcounts findByType(String sitename) {
		return find.where().eq("sitename", sitename).findUnique();
	}
	
	
	
	

}
