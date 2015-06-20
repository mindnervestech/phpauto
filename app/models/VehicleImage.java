package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class VehicleImage extends Model {

	@Id
	public Long id;
	public String vin;
	public String path;
	public boolean defaultImage;
	public Integer row;
	public Integer col;
	public String imgName;
	public String thumbPath;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isDefaultImage() {
		return defaultImage;
	}
	public void setDefaultImage(boolean defaultImage) {
		this.defaultImage = defaultImage;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
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
	public String getThumbPath() {
		return thumbPath;
	}
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}


	public static Finder<Long,VehicleImage> find = new Finder<>(Long.class,VehicleImage.class);
	
	public static VehicleImage getByImagePath(String path) {
		return find.where().eq("path", path).findUnique();
	}
	
	public static List<VehicleImage> getByVin(String vin) {
		return find.where().eq("vin", vin).findList();
	}
	
	public static VehicleImage findById(Long id) {
		return find.byId(id);
	}
	
	public static VehicleImage findByRowCol(Integer row,Integer col,String vin) {
		return find.where().eq("vin", vin).eq("row", row).eq("col", col).findUnique();
	}
	
}
