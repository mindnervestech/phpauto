package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class Registration extends Model {
	 

	@Id
	public Long id;
	
	public String location;
	public String options;
	public String name;
	public String email;
	public String phone;
	public String businessName;
	public String businessAdd;
	public String password;
	public String status;
	
	
	
	public static Finder<Long,Registration> find = new Finder<>(Long.class,Registration.class);
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
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

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessAdd() {
		return businessAdd;
	}

	public void setBusinessAdd(String businessAdd) {
		this.businessAdd = businessAdd;
	}

	public static Registration findByUser(AuthUser user) {
		return find.where().eq("status", "pending").findUnique();
	}

	public static List<Registration> getPending() {
		return find.all();
	}
	
	public static List<Registration> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}

	
	
}
