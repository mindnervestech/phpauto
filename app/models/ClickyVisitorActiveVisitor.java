package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ClickyVisitorActiveVisitor extends Model {
	
	@Id
	public Long id;
	public String value;
	public String valuePercent;
	public String title;
	public String statsUrl;
	public Date saveDate;
	
	public Date getSaveDate() {
		return saveDate;
	}
	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValuePercent() {
		return valuePercent;
	}
	public void setValuePercent(String valuePercent) {
		this.valuePercent = valuePercent;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatsUrl() {
		return statsUrl;
	}
	public void setStatsUrl(String statsUrl) {
		this.statsUrl = statsUrl;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public static Finder<Long,ClickyVisitorActiveVisitor> find = new Finder<>(Long.class,ClickyVisitorActiveVisitor.class);
	
	public static List<ClickyVisitorActiveVisitor> getAll(Date sDate,Date eDate) {
		return find.where().between("saveDate", sDate, eDate).findList();
	}
	
	
	public static List<ClickyVisitorActiveVisitor> getAllData(Date sDate) {
		return find.where().eq("saveDate", sDate).findList();
	}
	
	public static List<ClickyVisitorActiveVisitor> getfindAll() {
		return find.all();
		
		
	}
	
	
	public static ClickyVisitorActiveVisitor findById(Long id) {
		return find.byId(id);
	}
	
	
	
	
	

}

