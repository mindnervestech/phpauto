package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Inventory extends Model {
	
	@Id
	public Long id;
	public String title;
	public String description;
	public double price;
	public double cost;
	public String productId;
	
	
	@ManyToOne
	public Location locations;
	
	
	@ManyToOne
	public AddCollection collection;
	
	
	
	
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public AddCollection getCollection() {
		return collection;
	}

	public void setCollection(AddCollection collection) {
		this.collection = collection;
	}

	public static Finder<Long,Inventory> find = new Finder<>(Long.class,Inventory.class);
	
	public static Inventory findById(Long id) {
		return find.byId(id);
	}
	
	public static Inventory getByProductId(String productId) {
		return find.where().eq("productId", productId).findUnique();
	}
	
	public static List<Inventory> getAllAccessories(AddCollection collection) {
		return find.where().eq("collection", collection).findList();
	}
	
	public static List<Inventory> getByLocation(Location locations) {
		return find.where().eq("locations", locations).findList();
	}
	
	
}
