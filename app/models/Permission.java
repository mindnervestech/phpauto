package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Permission extends Model {

	@Id
	public Integer id;
	public String name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static Finder<Long,Permission> find = new Finder<>(Long.class,Permission.class);
	
	public static List<Permission> getAllPermission() {
		return find.all();
	}
	
}
