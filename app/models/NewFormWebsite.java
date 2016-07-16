package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class NewFormWebsite extends Model {

	@Id
	public Long id;
	public String title;
	public String form_type;
	public String lead_name;
	public String outcome;
	
	@ManyToOne
	public Location locations;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	

	public String getOutcome() {
		return outcome;
	}


	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}


	public String getLead_name() {
		return lead_name;
	}


	public void setLead_name(String lead_name) {
		this.lead_name = lead_name;
	}


	public String getForm_type() {
		return form_type;
	}


	public void setForm_type(String form_type) {
		this.form_type = form_type;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Location getLocations() {
		return locations;
	}


	public void setLocations(Location locations) {
		this.locations = locations;
	}
	
public static Finder<Long,NewFormWebsite> find = new Finder<>(Long.class,NewFormWebsite.class);
	
	public static NewFormWebsite findById(Long id) {
		return find.byId(id);
	}
	
	public static List<NewFormWebsite> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	public static NewFormWebsite findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	public static List<NewFormWebsite> getAllData() {
		return find.all();
	}
}
