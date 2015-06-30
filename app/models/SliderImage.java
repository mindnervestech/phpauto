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
	
	@ManyToOne
	public AuthUser user;

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
}
