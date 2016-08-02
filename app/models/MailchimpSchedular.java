package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class MailchimpSchedular extends Model {

	@Id
	public Long id;
	public String schedularTime;
	public Date currDate;
	
	@ManyToOne
	public Location locations;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSchedularTime() {
		return schedularTime;
	}


	public void setSchedularTime(String schedularTime) {
		this.schedularTime = schedularTime;
	}


	public Location getLocations() {
		return locations;
	}


	public void setLocations(Location locations) {
		this.locations = locations;
	}
	

	public Date getCurrDate() {
		return currDate;
	}


	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}




	public static Finder<Long,MailchimpSchedular> find = new Finder<>(Long.class,MailchimpSchedular.class);
	
	public static MailchimpSchedular findById(Long id) {
		return find.byId(id);
	}
	
	public static List<MailchimpSchedular> getAllContact() {
		return find.all();
	}

	
	public static MailchimpSchedular findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	
}
