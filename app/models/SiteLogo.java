package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SiteLogo extends Model {

	@Id
	public Long id;
	public String logoImagePath;
	public String logoImageName;
	public String faviconImagePath;
	public String faviconImageName;
	public String tabText;
	
	@ManyToOne
	public AuthUser user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogoImagePath() {
		return logoImagePath;
	}

	public void setLogoImagePath(String logoImagePath) {
		this.logoImagePath = logoImagePath;
	}

	public String getLogoImageName() {
		return logoImageName;
	}

	public void setLogoImageName(String logoImageName) {
		this.logoImageName = logoImageName;
	}

	public String getFaviconImagePath() {
		return faviconImagePath;
	}

	public void setFaviconImagePath(String faviconImagePath) {
		this.faviconImagePath = faviconImagePath;
	}

	public String getFaviconImageName() {
		return faviconImageName;
	}

	public void setFaviconImageName(String faviconImageName) {
		this.faviconImageName = faviconImageName;
	}

	public String getTabText() {
		return tabText;
	}

	public void setTabText(String tabText) {
		this.tabText = tabText;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
	
	public static Finder<Long,SiteLogo> find = new Finder<>(Long.class,SiteLogo.class);
	
	public static SiteLogo findById(Long id) {
		return find.byId(id);
	}
	
	public static SiteLogo findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
}
