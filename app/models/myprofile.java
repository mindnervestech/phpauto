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
	public String address;
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
		
	public static MyProfile findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}

	
	
}
