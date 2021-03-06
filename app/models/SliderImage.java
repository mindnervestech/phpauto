package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SliderImage extends Model {

	@Id
	public Long id;
	public String path;
	public String imgName;
	public String thumbPath;
	public String description;
	public String link;
	public Integer row;
	public Integer col;
	public Integer sliderNumber;
	
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

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
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
	
	public Integer getSliderNumber() {
		return sliderNumber;
	}

	public void setSliderNumber(Integer sliderNumber) {
		this.sliderNumber = sliderNumber;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}



	public static Finder<Long,SliderImage> find = new Finder<>(Long.class,SliderImage.class);
	
	public static SliderImage getByImagePath(String path) {
		return find.where().eq("path", path).findUnique();
	}
	
	public static SliderImage findById(Long id) {
		return find.byId(id);
	}
	
	public static List<SliderImage> findByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<SliderImage> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	public static SliderImage findByRowCol(Integer row,Integer col) {
		return find.where().eq("row", row).eq("col", col).findUnique();
	}
	
}
