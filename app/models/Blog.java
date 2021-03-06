package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Blog extends Model {

	@Id
	public Long id;
	public String title;
	public String description;
	public String imageUrl;
	public String imageName;
	public String videoUrl;
	public String postedBy;
	public String coverImageName;
	public String path;
	public String thumbPath;
	public String mainTitle;
	public String subtitle;
	public Integer row;
	public Integer col;
	public Integer headerFlag;
	public Date postedDate;
	public Long findNewId;
	
	@ManyToOne
	public AuthUser user;
	
	@ManyToOne
	public Location locations;

	
	
	public Long getFindNewId() {
		return findNewId;
	}

	public void setFindNewId(Long findNewId) {
		this.findNewId = findNewId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
	
	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
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

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	
	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}
	
	

	
	


	public String getCoverImageName() {
		return coverImageName;
	}

	public void setCoverImageName(String coverImageName) {
		this.coverImageName = coverImageName;
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


	public Integer getHeaderFlag() {
		return headerFlag;
	}

	public void setHeaderFlag(Integer headerFlag) {
		this.headerFlag = headerFlag;
	}







	public static Finder<Long,Blog> find = new Finder<>(Long.class,Blog.class);
	
	public static Blog findById(Long id) {
		return find.byId(id);
	}
	
	public static Blog findByOtherId(Long id,Long location) {
		return find.where().eq("findNewId", id).eq("locations.id", location).findUnique();
	}
	
	public static List<Blog> findByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static Blog findByUserAndTitle(AuthUser user,String title) {
		return find.where().eq("user", user).eq("title", title).findUnique();
	}
	
	public static List<Blog> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	public static Blog findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	
	public static List<Blog> getBlogsByDate(Date start,Date end) {
		return find.where().between("postedDate", start, end).orderBy("postedDate desc").findList();
	}
	
}
