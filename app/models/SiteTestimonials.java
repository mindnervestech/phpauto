package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class SiteTestimonials extends Model {

	@Id
	public Long id;
	public String testimonial;
	public String signature;
	
	
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
	
	public String getTestimonial() {
		return testimonial;
	}

	public void setTestimonial(String testimonial) {
		this.testimonial = testimonial;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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
	
	public static Finder<Long,SiteTestimonials> find = new Finder<>(Long.class,SiteTestimonials.class);
	
	public static SiteTestimonials findById(Long id) {
		return find.byId(id);
	}
	
	public static SiteTestimonials findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	public static List<SiteTestimonials> findAllByLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).findList();
	}
	
}
