package models;

import javax.persistence.Entity;
import javax.persistence.Id;

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
	public String avatarUrl;
	
	@Id
	public Integer id;
	public String provider;

	



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

	public static AuthUser findById(Integer id) {
		return find.byId(id);
	}

	
}
