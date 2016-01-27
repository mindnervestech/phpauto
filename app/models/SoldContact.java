package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SoldContact extends Model {

	@Id
	public Long id;
	public String name;
	public String email;
	public String phone;
	public String gender;
	public String age;
	public String buyingFor;
	public String howContactedUs;
	public String howFoundUs;
	public String make;
	public String year;
	public String mileage;
	public String price;
	public String custZipCode;
	public String enthicity;
	
	
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBuyingFor() {
		return buyingFor;
	}
	public void setBuyingFor(String buyingFor) {
		this.buyingFor = buyingFor;
	}
	public String getHowContactedUs() {
		return howContactedUs;
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
	public void setHowContactedUs(String howContactedUs) {
		this.howContactedUs = howContactedUs;
	}
	public String getHowFoundUs() {
		return howFoundUs;
	}
	public void setHowFoundUs(String howFoundUs) {
		this.howFoundUs = howFoundUs;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getCustZipCode() {
		return custZipCode;
	}
	public void setCustZipCode(String custZipCode) {
		this.custZipCode = custZipCode;
	}
	public String getEnthicity() {
		return enthicity;
	}
	public void setEnthicity(String enthicity) {
		this.enthicity = enthicity;
	}



	public static Finder<Long,SoldContact> find = new Finder<>(Long.class,SoldContact.class);
	
	public static List<SoldContact> getAllSoldContacts() {
		return find.all();
	}
	
	public static SoldContact findById(Long id) {
		return find.byId(id);
	}
	
}
