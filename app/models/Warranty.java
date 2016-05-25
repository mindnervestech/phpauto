package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class Warranty extends Model {

	@Id
	public Long id;
	public String path;
	public String thumbPath;
	public String mainTitle;
	public String subtitle;
	public Integer headerFlag;
	public String coverImageName;
	public Integer hideMenu;
	public Long findNewId;
	
	
	@ManyToOne
	public Location locations;
	


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Integer getHideMenu() {
		return hideMenu;
	}


	public void setHideMenu(Integer hideMenu) {
		this.hideMenu = hideMenu;
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


	public Long getFindNewId() {
		return findNewId;
	}


	public void setFindNewId(Long findNewId) {
		this.findNewId = findNewId;
	}


	public Integer getHeaderFlag() {
		return headerFlag;
	}


	public void setHeaderFlag(Integer headerFlag) {
		this.headerFlag = headerFlag;
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
	
	
public static Finder<Long,Warranty> find = new Finder<>(Long.class,Warranty.class);
	
	public static Warranty findById(Long id) {
		return find.byId(id);
	}
	
	public static Warranty findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	public static Warranty findByOtherId(Long id,Long location) {
		return find.where().eq("findNewId", id).eq("locations.id", location).findUnique();
	}
	
	
	
	
	
}
