package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class GroupTable extends Model {

	@Id
	public Long id;
	public String name;
	

	public static Finder<Long,GroupTable> find = new Finder<>(Long.class,GroupTable.class);
	
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

	public static GroupTable findById(Long id) {
		return find.byId(id);
	}
	
	public static List<GroupTable> findAllGroup() {
		return find.all();
	}
	
}
