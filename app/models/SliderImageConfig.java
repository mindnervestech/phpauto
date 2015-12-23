package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SliderImageConfig extends Model {

	@Id
	public Long id;
	public Integer cropWidth;
	public Integer cropHeight;
	
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

	public Integer getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(Integer cropWidth) {
		this.cropWidth = cropWidth;
	}

	public Integer getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(Integer cropHeight) {
		this.cropHeight = cropHeight;
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



	public static Finder<Long,SliderImageConfig> find = new Finder<>(Long.class,SliderImageConfig.class);
	
	public static SliderImageConfig findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
}
