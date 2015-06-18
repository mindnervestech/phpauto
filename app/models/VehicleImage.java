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
	public String orderNum;
	public String imgName;
	
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
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
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
	
}
