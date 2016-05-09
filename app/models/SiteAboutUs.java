package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class SiteAboutUs extends Model {

	@Id
	public Long id;
	public String mainTitle;
	public String text;
	
	
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
	

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
	
	public static Finder<Long,SiteAboutUs> find = new Finder<>(Long.class,SiteAboutUs.class);
	
	public static SiteAboutUs findById(Long id) {
		return find.byId(id);
	}
	
	public static SiteAboutUs findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	public static SiteAboutUs findAllByLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).findUnique();
	}
	
}
