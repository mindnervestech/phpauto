package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class NewsletterDate extends Model {

	@Id
	public Long id;
	public String dateOfMonth;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDateOfMonth() {
		return dateOfMonth;
	}
	public void setDateOfMonth(String dateOfMonth) {
		this.dateOfMonth = dateOfMonth;
	}
	
	public static Finder<Long,NewsletterDate> find = new Finder<>(Long.class,NewsletterDate.class);
	
	public static List<NewsletterDate> findAll() {
		return find.all();
	}
	
	public static NewsletterDate findById(Long id) {
		return find.byId(id);
	}
	
}
