package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Expression;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class CustomizationInventory extends Model {

	@Id
	public Long id;
	public String value;
	public String keyValue;
	public String displayGrid;
	public Long InventoryId;
	
	@ManyToOne
	public Location locations;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKeyValue() {
		return keyValue;
	}

	
	public Long getInventoryId() {
		return InventoryId;
	}

	public void setInventoryId(Long inventoryId) {
		InventoryId = inventoryId;
	}

	public String getDisplayGrid() {
		return displayGrid;
	}

	public void setDisplayGrid(String displayGrid) {
		this.displayGrid = displayGrid;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public static Finder<Long,CustomizationInventory> find = new Finder<>(Long.class,CustomizationInventory.class);
	
	public static CustomizationInventory findById(Long id) {
		return find.byId(id);
	}
	
	public static CustomizationInventory findByIdAndParent(Long id) {
		return find.where().eq("id", id).eq("status", null).findUnique();
	}
	
	public static List<CustomizationInventory> findByIdList(Long id) {
		return find.where().eq("InventoryId", id).findList();
	}
	
	public static CustomizationInventory findByKeyAndLeadId(String key,Long InventoryId) {
		return find.where().eq("keyValue", key).eq("InventoryId", InventoryId).findUnique();
	}
	
	
}
