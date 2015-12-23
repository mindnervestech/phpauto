package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SiteContent extends Model {

	@Id
	public Long id;
	public String heading;
	public String descHeading;
	public String description;
	
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

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getDescHeading() {
		return descHeading;
	}

	public void setDescHeading(String descHeading) {
		this.descHeading = descHeading;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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



	public static Finder<Long,SiteContent> find = new Finder<>(Long.class,SiteContent.class);
	
	public static SiteContent findById(Long id) {
		return find.byId(id);
	}
	
	public static SiteContent findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
	public static SiteContent findByLocation(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
}
