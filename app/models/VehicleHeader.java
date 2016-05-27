package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class VehicleHeader extends Model {

	
	
	@Id
	public Long id;
	public String path;
	public String thumbPath;
	public String mainTitle;
	public String subtitle;
	public String makeValue;
	public Integer makeFlag;
	public Integer financeFlag;
	public Integer socialFlag;
	public String coverImageName;
	public Long findNewId;
	
	
	
	
	
	public String getMakeValue() {
		return makeValue;
	}


	public void setMakeValue(String makeValue) {
		this.makeValue = makeValue;
	}


	public Long getFindNewId() {
		return findNewId;
	}


	public void setFindNewId(Long findNewId) {
		this.findNewId = findNewId;
	}


	public Integer getSocialFlag() {
		return socialFlag;
	}


	public void setSocialFlag(Integer socialFlag) {
		this.socialFlag = socialFlag;
	}



	@ManyToOne
	public Location locations;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getThumbPath() {
		return thumbPath;
	}


	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}


	public String getMainTitle() {
		return mainTitle;
	}


	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}


	public String getSubtitle() {
		return subtitle;
	}


	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}


	public Integer getMakeFlag() {
		return makeFlag;
	}


	public void setMakeFlag(Integer makeFlag) {
		this.makeFlag = makeFlag;
	}


	public Integer getFinanceFlag() {
		return financeFlag;
	}


	public void setFinanceFlag(Integer financeFlag) {
		this.financeFlag = financeFlag;
	}


	public String getCoverImageName() {
		return coverImageName;
	}


	public void setCoverImageName(String coverImageName) {
		this.coverImageName = coverImageName;
	}


	public Location getLocations() {
		return locations;
	}


	public void setLocations(Location locations) {
		this.locations = locations;
	}

	
	
public static Finder<Long,VehicleHeader> find = new Finder<>(Long.class,VehicleHeader.class);
	
	public static VehicleHeader findById(Long id) {
		return find.byId(id);
	}
	
	public static VehicleHeader findByLocationsAndMake(Long location,String makeValue) {
		return find.where().eq("locations.id", location).eq("makeValue",makeValue).findUnique();
	}
	
	public static VehicleHeader findByIdAndMake(Long id,String makeValue ) {
		return find.where().eq("id", id).eq("makeValue",makeValue).findUnique();
	}
	
	public static VehicleHeader findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	public static VehicleHeader findByOtherId(Long id,Long location) {
		return find.where().eq("id", id).eq("locations.id", location).findUnique();
	}
	
	public static List<VehicleHeader> findAllByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	
}
