package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class AutoPortal extends Model{

	
	@Id
	public Long id;
	
	public String login;
	public String passward;
	public String path;
	public String port;
	public String sitename;
	
	public String getSitename() {
		return sitename;
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassward() {
		return passward;
	}
	public void setPassward(String passward) {
		this.passward = passward;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	
public static Finder<Long,AutoPortal> find = new Finder<>(Long.class,AutoPortal.class);
	
	public static AutoPortal findByType(String sitename) {
		return find.where().eq("sitename", sitename).findUnique();
	}
	
	
	
}
