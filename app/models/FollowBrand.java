package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class FollowBrand extends Model {

	@Id
	public Long id;
	public String name;
	public String email;
	public String brand;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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




	public static Finder<Long,FollowBrand> find = new Finder<>(Long.class,FollowBrand.class);
	
	public static FollowBrand findById(Long id) {
		return find.byId(id);
	}
	
	public static List<SqlRow> getAllBrandFollowers(AuthUser user) {
		SqlQuery q = Ebean.createSqlQuery("select distinct email from follow_brand where user_id='"+user.id+"'");
		List<SqlRow> rows = q.findList();
		return rows;
	}
	
	public static List<FollowBrand> getBrands(AuthUser user, String email) {
		return find.where().eq("email", email).eq("user", user).findList();
	}
	public static List<FollowBrand> getBrandsByEmail(String email) {
		return find.where().eq("email", email).findList();
	}
	public static List<FollowBrand> getAllBrandFollowersName(String brand) {
		return find.where().eq("brand", brand).findList();
	}
}
