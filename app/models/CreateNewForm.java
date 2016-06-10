package models;

import java.util.List;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity


public class CreateNewForm extends Model {

	@Id
	public Long id;
	public String name;
	
		
	@ManyToOne
	public Location locations;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Location getLocations() {
		return locations;
	}


	public void setLocations(Location locations) {
		this.locations = locations;
	}
	
	public static Finder<Long,CreateNewForm> find = new Finder<>(Long.class,CreateNewForm.class);
	
	public static CreateNewForm findById(Long id) {
		return find.byId(id);
	}
	
	public static List<CreateNewForm> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	public static CreateNewForm findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	public static List<CreateNewForm> getAllData() {
		return find.all();
	}

}
