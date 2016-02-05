package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Comments extends Model {

	@Id
	public Long id;
	public String comment;
	@ManyToOne
	public AuthUser user;
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}

	public static Finder<Long,Comments> find = new Finder<>(Long.class,Comments.class);
	
	public static List<Comments> getAll() {
		return find.all();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static Comments getByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
		//return null;
	}
	
	
}