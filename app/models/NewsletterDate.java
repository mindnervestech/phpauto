package models;

import java.util.Date;
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
	public Date newsletterTime;
	public String timeZone;
	public Date gmtTime;
	
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
	public Date getNewsletterTime() {
		return newsletterTime;
	}
	public void setNewsletterTime(Date newsletterTime) {
		this.newsletterTime = newsletterTime;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public Date getGmtTime() {
		return gmtTime;
	}
	public void setGmtTime(Date gmtTime) {
		this.gmtTime = gmtTime;
	}

	public static Finder<Long,NewsletterDate> find = new Finder<>(Long.class,NewsletterDate.class);
	
	public static List<NewsletterDate> findAll() {
		return find.all();
	}
	
	public static NewsletterDate findById(Long id) {
		return find.byId(id);
	}
	
}
