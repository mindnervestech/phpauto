package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SiteComparison extends Model {
	
	@Id
	public Long id;
	public String path;
	public String thumbPath;
	public String mainTitle;
	public String subTitle;
	public Integer compareFlag;
	public String coverImageName;
	public Long findNewId;
	
	
	public Long getFindNewId() {
		return findNewId;
	}


	public void setFindNewId(Long findNewId) {
		this.findNewId = findNewId;
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



	public String getSubTitle() {
		return subTitle;
	}


	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}


	public Integer getCompareFlag() {
		return compareFlag;
	}


	public void setCompareFlag(Integer compareFlag) {
		this.compareFlag = compareFlag;
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
	
public static Finder<Long,SiteComparison> find = new Finder<>(Long.class,SiteComparison.class);
	
	public static SiteComparison findById(Long id) {
		return find.byId(id);
	}
	
	public static SiteComparison findByOtherId(Long id,Long location) {
		return find.where().eq("findNewId", id).eq("locations.id", location).findUnique();
	}
	
	public static SiteComparison findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	
	
	
}
