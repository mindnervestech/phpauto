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

	
	
	/*public static List<AddCollection> findAllCollection() {
		
		return find.all();
	}
	public static AddCollection findByUser(AuthUser userObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public static AddCollection findById(Long id) {
		return find.byId(id);
	}
	
	public static List<AddCollection> findAllcollection() {
		return find.all();
	}
	public static List<AddCollection> getAllBySection(String sec) {
		return find.where().eq("section", sec).orderBy("collectionOrder asc").findList();
	}
	
	public static List<AddCollection> getAllReadyMade() {
		return find.where().eq("section", "readymade").findList();
	}
	
	public static List<AddCollection> getAllAccessories() {
		return find.where().eq("section", "accessories").findList();
	}
	public static List<AddCollection> getAllReadyMadeCollection() {
		return find.where().eq("section", "readymade").orderBy("collectionOrder asc").findList();
	}
	public static List<AddCollection> getAllProductCollection() {
		return find.where().eq("section", "product").orderBy("collectionOrder asc").findList();
	}
	public static List<AddCollection> getAllSection() {
		return find.where().ne("section", "product").ne("section", "readymade").ne("section", "accessories").ne("section", "disable").findList();
		//return find.where().eq("section", "product").findList();
	}
	public static List<AddCollection> getAllProducts() {
		return find.where().eq("section", "product").findList();
	}
	public static AddCollection getCollectionName(Long id) {
		return find.where().eq("id", id).findUnique();
	}
	public static List<AddCollection> getAllByName( String name) {
		return find.where().eq("section", name).findList();
	}*/
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
