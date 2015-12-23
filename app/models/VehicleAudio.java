package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class VehicleAudio extends Model {

	@Id
	public Long id;
	public String vin;
	public String path;
	public String fileName;
	
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

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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



	public static Finder<Long,VehicleAudio> find = new Finder<>(Long.class,VehicleAudio.class);
	
	public static VehicleAudio findById(Long id) {
		return find.byId(id);
	}
	
	public static List<VehicleAudio> getByUserAndVin(AuthUser user,String vin) {
		return find.where().eq("user", user).eq("vin", vin).findList();
	}
	
}
