package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class InventoryImage extends Model {

	@Id
	public Long id;
	public String productId;
	public String path;
	public boolean defaultImage;
	public Integer row;
	public Integer col;
	public String imgName;
	public String thumbPath;
	
	@ManyToOne
	public Location locations;
	
	@ManyToOne
	public AuthUser user;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
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
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
	}


	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}



	public static Finder<Long,InventoryImage> find = new Finder<>(Long.class,InventoryImage.class);
	
	public static InventoryImage getByImagePath(String path) {
		return find.where().eq("path", path).findUnique();
	}
	
	public static List<InventoryImage> getByProductId(String productId) {
		return find.where().eq("productId", productId).findList();
	}
	
	public static InventoryImage findById(Long id) {
		return find.byId(id);
	}
	
	public static InventoryImage getDefaultImage(String vin) {
		return find.where().eq("productId", vin).eq("defaultImage", true).findUnique();
	}
	
	public static InventoryImage findByRowCol(Integer row,Integer col,String vin) {
		return find.where().eq("productId", vin).eq("row", row).eq("col", col).findUnique();
	}
	
		public static int getVehicleImageCountByVIN(String vin){
		return find.where().eq("productId", vin).findList().size();
	} 
}
