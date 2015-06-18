package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Site extends Model {

	@Id
	public Long id;
	public String name;
	
	
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
	
	public static Finder<Long,Site> find = new Finder<>(Long.class,Site.class);
	
	public static List<Site> getAllSites() {
		return find.all();
	}
	
	public static Site findById(Long id) {
		return find.byId(id);
	}
}
