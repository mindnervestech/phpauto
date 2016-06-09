package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class ClickyContentDownLoad extends Model {

	@Id
	public Long id;
	public String value;
	public String valuePercent;
	public String title;
	public String statsUrl;
	public String url;
	public String editedUrl;
	public Date saveDate;
	
	
	public String getEditedUrl() {
		return editedUrl;
	}

	public void setEditedUrl(String editedUrl) {
		this.editedUrl = editedUrl;
	}

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

	public static Finder<Long,ClickyContentDownLoad> find = new Finder<>(Long.class,ClickyContentDownLoad.class);
	
	public static List<ClickyContentDownLoad> getAll(Date sDate,Date eDate) {
		return find.where().between("saveDate", sDate, eDate).findList();
	}
	
	public static List<ClickyContentDownLoad> getfindAll() {
		return find.all();
	}
	
	
	public static ClickyContentDownLoad findById(Long id) {
		return find.byId(id);
	}
	
	
	
}

