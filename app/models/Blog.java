package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Blog extends Model {

	@Id
	public Long id;
	public String title;
	public String description;
	public String postedBy;
	public Date postedDate;
	
	@ManyToOne
	public AuthUser user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
	
	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public static Finder<Long,Blog> find = new Finder<>(Long.class,Blog.class);
	
	public static Blog findById(Long id) {
		return find.byId(id);
	}
	
	public static List<Blog> findByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static Blog findByUserAndTitle(AuthUser user,String title) {
		return find.where().eq("user", user).eq("title", title).findUnique();
	}
	
}
