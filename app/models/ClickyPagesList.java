package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class ClickyPagesList extends Model {

	@Id
	public Long id;
	public String value;
	public String value_percent;
	public String title;
	public String stats_url;
	public String url;
	public String mainUrl;
	public Date saveDate;
	public String locationName;
	public Long locations;
	
	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	public static Finder<Long,ClickyPagesList> find = new Finder<>(Long.class,ClickyPagesList.class);

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

	public String getValue_percent() {
		return value_percent;
	}

	public void setValue_percent(String value_percent) {
		this.value_percent = value_percent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStats_url() {
		return stats_url;
	}

	public void setStats_url(String stats_url) {
		this.stats_url = stats_url;
	}

	public String getMainUrl() {
		return mainUrl;
	}

	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Long getLocations() {
		return locations;
	}

	public void setLocations(Long locations) {
		this.locations = locations;
	}

	public static List<ClickyPagesList> getAll(Date sDate,Date eDate) {
		return find.where().between("saveDate", sDate, eDate).findList();
	}
	
	public static List<ClickyPagesList> getAllData(Date sDate) {
		return find.where().eq("saveDate", sDate).findList();
	}
	
	public static List<ClickyPagesList> getHeatMapUrl(String url) {
		return find.where().eq("mainUrl", url).findList();
	}
	
	public static List<ClickyPagesList> getfindAll() {
		return find.all();
	}
	
	
	public static ClickyPagesList findById(Long id) {
		return find.byId(id);
	}
	
	
	
}
