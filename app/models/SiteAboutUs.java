package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class SiteAboutUs extends Model {

	@Id
	public Long id;
	public String mainTitle;
	public String text;
	public String text1;
	public String imageurl;
	public String themImageurl;
	public String path;
	public String imgName;
	public String thumbPath;
	public String description;
	public String link;
	public String headerTitle;
	public String subtitle;
	public Integer headerFlag;
	public Integer row;
	public Integer col;
	
	
	
	
	@ManyToOne
	public AuthUser user;
	
	@ManyToOne
	public Location locations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
	
	
	public String getThemImageurl() {
		return themImageurl;
	}

	public void setThemImageurl(String themImageurl) {
		this.themImageurl = themImageurl;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getCol() {
		return col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}


	public String getHeaderTitle() {
		return headerTitle;
	}

	public void setHeaderTitle(String headerTitle) {
		this.headerTitle = headerTitle;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Integer getHeaderFlag() {
		return headerFlag;
	}

	public void setHeaderFlag(Integer headerFlag) {
		this.headerFlag = headerFlag;
	}




	public static Finder<Long,SiteAboutUs> find = new Finder<>(Long.class,SiteAboutUs.class);
	
	public static SiteAboutUs findById(Long id) {
		return find.byId(id);
	}
	public static SiteAboutUs  getByImagePath(String path) {
		return find.where().eq("path", path).findUnique();
	}
	
	public static SiteAboutUs findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	public static SiteAboutUs findAllByLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).findUnique();
	}
	
	public static SiteAboutUs findByLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).findUnique();
	}
	
	
}
