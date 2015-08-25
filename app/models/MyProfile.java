package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class MyProfile extends Model {

	
	 
	public String myname;
	public String dealer_id;
	public String latlong;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String country;
	public String phone;
	public String email;
	public String web;
	public String facebook;
	public String twitter;
	public String pinterest;
	public String instagram;
	public String googleplus;
	

	@Id
	public Integer id;
	
	 @OneToOne
	 public AuthUser user;
	
	public String getWeb() {
		return web;
	}


	public void setWeb(String web) {
		this.web = web;
	}

	public String getFacebook() {
		return facebook;
	}


	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}


	public String getPinterest() {
		return pinterest;
	}


	public void setPinterest(String pinterest) {
		this.pinterest = pinterest;
	}


	public String getInstagram() {
		return instagram;
	}


	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}


	
	public void setTwitter(String twitter) {
		twitter = twitter;
	}


	public String getGoogleplus() {
		return googleplus;
	}


	public void setGoogleplus(String googleplus) {
		this.googleplus = googleplus;
	}



	public static Finder<Long,MyProfile> find = new Finder<>(Long.class,MyProfile.class);
	
	public String getMyname() {
		return myname;
	}


	public void setMyname(String myname) {
		this.myname = myname;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public AuthUser getUser() {
		return user;
	}


	public void setUser(AuthUser user) {
		this.user = user;
	}
		
	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getZip() {
		return zip;
	}


	public void setZip(String zip) {
		this.zip = zip;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getTwitter() {
		return twitter;
	}


	public static MyProfile findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}


	public String getDealer_id() {
		return dealer_id;
	}


	public void setDealer_id(String dealer_id) {
		this.dealer_id = dealer_id;
	}


	public String getLatlong() {
		return latlong;
	}


	public void setLatlong(String latlong) {
		this.latlong = latlong;
	}

	
	
}
