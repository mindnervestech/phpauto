package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
 @Entity
public class Domain extends Model {
	@Id
	public Long id;
	 
	public String domain;
	
	 @ManyToOne
		public Location locations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	 
	
public static Finder<Long,Domain> find = new Finder<>(Long.class,Domain.class);
	
	
	public static Domain findByLocation(Long location) {
		return find.where().eq("locations_id", location).findUnique();
	}
	 
	 

}
