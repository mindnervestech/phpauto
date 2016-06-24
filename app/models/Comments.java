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
	public Date likeDate;
	public Date newTime;
	@ManyToOne
	public AuthUser user;
	@ManyToOne
	public AuthUser commentUser;
	public Integer commentFlag;
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

	public Integer getCommentFlag() {
		return commentFlag;
	}

	public void setCommentFlag(Integer commentFlag) {
		this.commentFlag = commentFlag;
	}

	public AuthUser getCommentUser() {
		return commentUser;
	}

	public void setCommentUser(AuthUser commentUser) {
		this.commentUser = commentUser;
	}

	public static Comments getByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
		//return null;
	}
	
	public static List<Comments> getByListUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<Comments> getByListUserWithFlag(AuthUser user) {
		return find.where().eq("user", user).eq("commentFlag", 1).findList();
	}
	
	
}
