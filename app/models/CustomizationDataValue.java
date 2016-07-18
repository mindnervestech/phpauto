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
public class CustomizationDataValue extends Model {

	@Id
	public Long id;
	public String value;
	public String keyValue;
	public Long leadId;
	public Long leadType;
	public String saveCrm;
	public String displayGrid;
	
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

	public String getSaveCrm() {
		return saveCrm;
	}

	public void setSaveCrm(String saveCrm) {
		this.saveCrm = saveCrm;
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

	public Long getLeadId() {
		return leadId;
	}

	public void setLeadId(Long leadId) {
		this.leadId = leadId;
	}


	public Long getLeadType() {
		return leadType;
	}

	public void setLeadType(Long leadType) {
		this.leadType = leadType;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public static Finder<Long,CustomizationDataValue> find = new Finder<>(Long.class,CustomizationDataValue.class);
	
	public static CustomizationDataValue findById(Long id) {
		return find.byId(id);
	}
	
	public static CustomizationDataValue findByIdAndParent(Long id) {
		return find.where().eq("id", id).eq("status", null).findUnique();
	}
	
	public static CustomizationDataValue findByKeyAndLeadId(String key,Long leadId) {
		return find.where().eq("keyValue", key).eq("leadId", leadId).findUnique();
	}
	
		
		public static List<CustomizationDataValue> findByScheduler() {
		return find.where().eq("schedule_email", 0).findList();
	}
		
		public static List<CustomizationDataValue> findByCustomeSaveCRMLead(Long leadType,Long leadId) {
			return find.where().eq("leadType", leadType).eq("leadId", leadId).eq("saveCrm", "true").findList();
		}
		
		public static List<CustomizationDataValue> findByCustomeLead(Long leadType,Long leadId) {
			return find.where().eq("leadType", leadType).eq("leadId", leadId).findList();
		}
		
		public static List<CustomizationDataValue> findByLeadIdWise(Long leadId) {
			return find.where().eq("leadId", leadId).findList();
		}
	
}
