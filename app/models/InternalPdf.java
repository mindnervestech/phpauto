package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.List;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import scalaz.std.string;
@Entity
public class InternalPdf extends Model {
	@Id
	public Long id;
	public String pdf_name;
	public String pdf_path;
	
	 @ManyToOne
		public Location locations;
	
	
	
	public String getPdf_name() {
		return pdf_name;
	}
	public void setPdf_name(String pdf_name) {
		this.pdf_name = pdf_name;
	}
	public String getPdf_path() {
		return pdf_path;
	}
	public void setPdf_path(String pdf_path) {
		this.pdf_path = pdf_path;
	}
	
	
	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}


	public static Finder<Long,InternalPdf> find = new Finder<>(Long.class,InternalPdf.class);
	
	
	public static List<InternalPdf> getAllPdfData() {
		return find.all();
	}
	
	public static List<InternalPdf> findByLocation(Long location) {
		return find.where().eq("locations_id", location).findList();
	}
	
	public static InternalPdf findPdfById(Long id) {
		return find.byId(id);
	}

}
