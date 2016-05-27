
 
 package models;

 import java.util.Date;
 import java.util.List;

 import javax.persistence.Entity;
 import javax.persistence.Id;
 import javax.persistence.ManyToMany;
 import javax.persistence.ManyToOne;
 import javax.persistence.OneToOne;
 import javax.persistence.Version;

 import com.avaje.ebean.Expr;

 import play.db.ebean.Model;
 import play.db.ebean.Model.Finder;

 @Entity
 public class CampaignsVM extends Model {

	 @Id
		public Long id;
		 
		public String name;
		
		public String matchType;
		public String matchString;
		
		 @ManyToOne
			public Location locations;
 	
 	
 	public static Finder<Long,CampaignsVM> find = new Finder<>(Long.class,CampaignsVM.class);
 	
 	
 	public static List<CampaignsVM> findByLocation(Long location) {
 		return find.where().eq("locations.id", location).findList();
 	}
 	
 }

 
 
 
 
 
