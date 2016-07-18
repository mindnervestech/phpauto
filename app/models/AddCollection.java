package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class AddCollection extends Model {
	
	@Id
	public Long id;
	public String title;
	public String description;
	public String path;
	public String thumbnailPath;
	public String imageName;
	public String thumbPath;
	public String thumbnailImageName;
	public String thumbnailThumbPath;
	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getThumbnailImageName() {
		return thumbnailImageName;
	}

	public void setThumbnailImageName(String thumbnailImageName) {
		this.thumbnailImageName = thumbnailImageName;
	}

	public String getThumbnailThumbPath() {
		return thumbnailThumbPath;
	}

	public void setThumbnailThumbPath(String thumbnailThumbPath) {
		this.thumbnailThumbPath = thumbnailThumbPath;
	}

	public String section;
	public String website;
	public String newFlag;
	public Integer collectionOrder;
	
	public Integer getCollectionOrder() {
		return collectionOrder;
	}

	public void setCollectionOrder(Integer collectionOrder) {
		this.collectionOrder = collectionOrder;
	}

	public String getNewFlag() {
		return newFlag;
	}

	public void setNewFlag(String newFlag) {
		this.newFlag = newFlag;
	}
	@ManyToOne
	public AuthUser user;
	
	public static Finder<Long,AddCollection> find = new Finder<>(Long.class,AddCollection.class);
	public String getTitle() {
		return title;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
	}
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	public static List<AddCollection> findAllCollection() {
		
		return find.all();
	}
	public static AddCollection findByUser(AuthUser userObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public static AddCollection findById(Long id) {
		return find.byId(id);
	}
	
	public static List<AddCollection> findAllcollection() {
		return find.all();
	}
	public static List<AddCollection> getAllBySection(String sec) {
		return find.where().eq("section", sec).orderBy("collectionOrder asc").findList();
	}
	
	public static List<AddCollection> getAllReadyMade() {
		return find.where().eq("section", "readymade").findList();
	}
	
	public static List<AddCollection> getAllAccessories() {
		return find.where().eq("section", "accessories").findList();
	}
	public static List<AddCollection> getAllReadyMadeCollection() {
		return find.where().eq("section", "readymade").orderBy("collectionOrder asc").findList();
	}
	public static List<AddCollection> getAllProductCollection() {
		return find.where().eq("section", "product").orderBy("collectionOrder asc").findList();
	}
	public static List<AddCollection> getAllSection() {
		return find.where().ne("section", "product").ne("section", "readymade").ne("section", "accessories").ne("section", "disable").findList();
		//return find.where().eq("section", "product").findList();
	}
	public static List<AddCollection> getAllProducts() {
		return find.where().eq("section", "product").findList();
	}
	public static AddCollection getCollectionName(Long id) {
		return find.where().eq("id", id).findUnique();
	}
	public static List<AddCollection> getAllByName( String name) {
		return find.where().eq("section", name).findList();
	}
	
}
