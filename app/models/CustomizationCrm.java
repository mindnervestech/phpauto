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
public class CustomizationCrm extends Model {

	@Id
	public Long id;
	public String value;
	public String keyValue;
	public String displayGrid;
	public Long crmId;
	
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

	
	public Long getcrmId() {
		return crmId;
	}

	public void setcrmId(Long crmId) {
		crmId = crmId;
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

	public static Finder<Long,CustomizationCrm> find = new Finder<>(Long.class,CustomizationCrm.class);
	
	public static CustomizationCrm findById(Long id) {
		return find.byId(id);
	}
	
	public static CustomizationCrm findByIdAndParent(Long id) {
		return find.where().eq("id", id).eq("status", null).findUnique();
	}
	
	public static List<CustomizationCrm> findByIdList(Long id) {
		return find.where().eq("crmId", id).findList();
	}
	
	public static CustomizationCrm findByKeyAndLeadId(String key,Long crmId) {
		return find.where().eq("keyValue", key).eq("crmId", crmId).findUnique();
	}
	
	
}
