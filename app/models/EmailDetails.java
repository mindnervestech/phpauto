package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class EmailDetails extends Model {


	@Id
	public Long id;
	public String username;
	public String name;
	public String passward;
	public String host;
	public String port;

	@ManyToOne
		public Location locations;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassward() {
		return passward;
	}
	public void setPassward(String passward) {
		this.passward = passward;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	public static Finder<Long,EmailDetails> find = new Finder<>(Long.class,EmailDetails.class);
	
	
	public static EmailDetails findByLocation(Long location) {
		return find.where().eq("locations_id", location).findUnique();
	}
	public static List<EmailDetails> findAllData() {
		return find.all();
	}
	
}
