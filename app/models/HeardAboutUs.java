package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class HeardAboutUs extends Model {

	@Id
	public Long id;
	public String value;
	
	public static Finder<Long,HeardAboutUs> find = new Finder<>(Long.class,HeardAboutUs.class);
	
	public static List<HeardAboutUs> getAll() {
		return find.all();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static List<HeardAboutUs> getByValue(String name) {
		return find.where().eq("value", name).findList();
		//return null;
	}
	
	
}
