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
public class ActionAdd extends Model {

	@Id
	public Long id;
	public String value;
	
	public static Finder<Long,ActionAdd> find = new Finder<>(Long.class,ActionAdd.class);
	
	public static List<ActionAdd> getAll() {
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

	public static List<ActionAdd> getByValue(String name) {
		return find.where().eq("value", name).findList();
		//return null;
	}
	
	
}
