package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Expr;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import scala.Option;
import securesocial.core.AuthenticationMethod;
import securesocial.core.Identity;
import securesocial.core.IdentityId;
import securesocial.core.OAuth1Info;
import securesocial.core.OAuth2Info;
import securesocial.core.PasswordInfo;

@Entity
public class AuthUser extends Model implements Identity {

	 public static Finder<Integer,AuthUser> find = new Finder<Integer,AuthUser>(
			 Integer.class, AuthUser.class
			  ); 
	public String email;
	public String communicationemail;
	public String firstName;
	public String lastName;
	public String password;
	public String role;
	public String phone;
	public String avatarUrl;
	public String imageName;
	public String imageUrl;
	
	@Id
	public Integer id;
	public String provider;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Permission> permission;
	


	@Override
	public AuthenticationMethod authMethod() {
		return null;
	}

	@Override
	public Option<String> avatarUrl() {
		return null;
	}

	@Override
	public Option<String> email() {
		return null;
	}

	@Override
	public String firstName() {
		return firstName;
	}

	@Override
	public String fullName() {
		return firstName + " " +lastName;
	}

	@Override
	public IdentityId identityId() {
		return null;
	}

	@Override
	public String lastName() {
		return lastName;
	}

	@Override
	public Option<OAuth1Info> oAuth1Info() {
		return null;
	}

	@Override
	public Option<OAuth2Info> oAuth2Info() {
		return null;
	}

	@Override
	public Option<PasswordInfo> passwordInfo() {
		return null;
	}

	public List<Permission> getPermission() {
		return permission;
	}

	public void setPermission(List<Permission> permission) {
		this.permission = permission;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public static List<AuthUser> getUserByType() {
		return find.where().or(Expr.eq("role", "General Manager"), Expr.eq("role", "Sales Person")).findList();
	}

	public static AuthUser findById(Integer id) {
		return find.byId(id);
	}

	public static List<AuthUser> getAllSalesUser() {
	
		return find.where().eq("role", "Sales Person").findList();
	}
	
	public static List<AuthUser> getAllUsers() {
		
		return find.all();
	}
	
}
