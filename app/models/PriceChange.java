package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class PriceChange extends Model {

	@Id
	public Long id;
	public String price;
	public String person;
	public String vin;
	public Date dateTime;
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public static Finder<Long,PriceChange> find = new Finder<>(Long.class,PriceChange.class);
	public static List<PriceChange> findByVin(String vin) {
		return find.where().eq("vin", vin).findList();
	}
	
	
	
}
