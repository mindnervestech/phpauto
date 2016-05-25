package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class ContactHeader extends Model {

	@Id
	public Long id;
	public String path;
	public String thumbPath;
	public String mainTitle;
	public String subtitle;
	public Integer row;
	public Integer col;
	public Integer headerFlag;
	public String coverImageName;
	public Long findNewId;
	
	
	public Long getFindNewId() {
		return findNewId;
	}


	public void setFindNewId(Long findNewId) {
		this.findNewId = findNewId;
	}


	public String getCoverImageName() {
		return coverImageName;
	}


	public void setCoverImageName(String coverImageName) {
		this.coverImageName = coverImageName;
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


	public Location getLocations() {
		return locations;
	}


	public void setLocations(Location locations) {
		this.locations = locations;
	}

	
public static Finder<Long,ContactHeader> find = new Finder<>(Long.class,ContactHeader.class);
	
	public static ContactHeader findById(Long id) {
		return find.byId(id);
	}
	
	public static ContactHeader findByOtherId(Long id,Long location) {
		return find.where().eq("findNewId", id).eq("locations.id", location).findUnique();
	}
	
	public static ContactHeader findByLocations(Long location) {
		return find.where().eq("locations.id", location).findUnique();
	}
	
	
	
}
