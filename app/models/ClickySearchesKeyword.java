package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class ClickySearchesKeyword extends Model {

	@Id
	public Long id;
	public String value;
	public String valuePercent;
	public String title;
	public String statsUrl;
	public String url;
	public Date saveDate;
	

	
	
	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}



	public String getValuePercent() {
		return valuePercent;
	}

	public void setValuePercent(String valuePercent) {
		this.valuePercent = valuePercent;
	}

	public String getStatsUrl() {
		return statsUrl;
	}

	public void setStatsUrl(String statsUrl) {
		this.statsUrl = statsUrl;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static Finder<Long,ClickySearchesKeyword> find = new Finder<>(Long.class,ClickySearchesKeyword.class);
	
	public static List<ClickySearchesKeyword> getAll(Date sDate,Date eDate) {
		return find.where().between("saveDate", sDate, eDate).findList();
	}
	
	
	public static List<ClickySearchesKeyword> getAllData(Date sDate) {
		return find.where().eq("saveDate", sDate).findList();
	}
	
	
	public static List<ClickySearchesKeyword> getfindAll() {
		return find.all();
	}
	
	
	public static ClickySearchesKeyword findById(Long id) {
		return find.byId(id);
	}
	
	
	public static List<ClickySearchesKeyword> findByTitleAndDate(String title, Date startdate, Date enddate) {
		return find.where().eq("title", title).between("saveDate", startdate, enddate).findList();
	}
	
	
	
	
}

