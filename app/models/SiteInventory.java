package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class SiteInventory extends Model {

	@Id
	public Long id;
	public String mainTitle;
	public String subTitle;
	public String defaultView;
	public String sortBy;
	public String sortType;
	public String vType;
	public String imageUrl;
	public String thumbPath;
	public String imageName;
	public Integer applyAll;
	
	
	@ManyToOne
	public Location locations;
	
	
	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
		
	

	public Integer getApplyAll() {
		return applyAll;
	}

	public void setApplyAll(Integer applyAll) {
		this.applyAll = applyAll;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getvType() {
		return vType;
	}

	public void setvType(String vType) {
		this.vType = vType;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}



	public static Finder<Long,SiteInventory> find = new Finder<>(Long.class,SiteInventory.class);
	
	public static SiteInventory findById(Long id) {
		return find.byId(id);
	}
	
	public static List<SiteInventory> findByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<SiteInventory> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	public static SiteInventory findByLocationAndType(Long location,String vType) {
		return find.where().eq("locations.id", location).eq("vType", vType).findUnique();
	}
	
	
	
}
