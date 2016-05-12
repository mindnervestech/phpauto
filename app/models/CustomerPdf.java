package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class CustomerPdf extends Model {
	
	@Id
	public Long id;
	public String pdf_name;
	public String pdf_path;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
public static Finder<Long,CustomerPdf> find = new Finder<>(Long.class,CustomerPdf.class);
	
	
	public static List<CustomerPdf> getAllPdfData() {
		return find.all();
	}
	
	
	public static CustomerPdf findPdfById(Long id) {
		return find.byId(id);
	}
	
	
	
}
