package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class AddCollection extends Model {
	
	@Id
	public Long id;
	public String title;
	
	@ManyToOne
	public Location locations;
	
	
	
	public static Finder<Long,AddCollection> find = new Finder<>(Long.class,AddCollection.class);




	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	
		public static List<AddCollection> getAllReadyMadeCollection() {
		return find.where().eq("section", "readymade").orderBy("collectionOrder asc").findList();
	}
	
	public static AddCollection findById(Long id) {
		return find.byId(id);
	}
	
	public static List<AddCollection> findByLocation(Long locations_id) {
		return find.where().eq("locations_id", locations_id).findList();
	}
	public static AddCollection findByLocations(Long locations_id) {
		return find.where().eq("locations_id", locations_id).findUnique();
	}
	
	public static AddCollection findBytitle(String title) {
		return find.where().eq("title", title).findUnique();
	}
	
	public static List<AddCollection> findByLocationsAndSelected(Long locations_id) {
		return find.where().eq("shows", 1).eq("locations_id", locations_id).findList();
	}
	
}
