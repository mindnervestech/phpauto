package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class TakeMapClickyData extends Model {

	@Id
	public Long id;
	public Date dateStore;
	
	
	public static Finder<Long,TakeMapClickyData> find = new Finder<>(Long.class,TakeMapClickyData.class);
	
	
	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}




	public Date getDateStore() {
		return dateStore;
	}




	public void setDateStore(Date dateStore) {
		this.dateStore = dateStore;
	}





	public static TakeMapClickyData getallData(Long id) {
		return find.byId(id);
	}
	
}
