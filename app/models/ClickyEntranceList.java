package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ClickyEntranceList extends Model {
	
	@Id
	public Long id;
	public String visitors;
	public String uniqueVisitor;
	public String action;
	public String averageAction;
	public String totalTime;
	public String averageTime;
	public String bounceRate;
	public Date saveDate;
	public String url;
	
	public String value;
	public String valuePercent;
	public String title;
	public String statsUrl;
	public String mainUrl;
	
	
	
	
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
	public String getMainUrl() {
		return mainUrl;
	}
	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}
	public Date getSaveDate() {
		return saveDate;
	}
	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVisitors() {
		return visitors;
	}
	public void setVisitors(String visitors) {
		this.visitors = visitors;
	}
	public String getUniqueVisitor() {
		return uniqueVisitor;
	}
	public void setUniqueVisitor(String uniqueVisitor) {
		this.uniqueVisitor = uniqueVisitor;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAverageAction() {
		return averageAction;
	}
	public void setAverageAction(String averageAction) {
		this.averageAction = averageAction;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getAverageTime() {
		return averageTime;
	}
	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}
	public String getBounceRate() {
		return bounceRate;
	}
	public void setBounceRate(String bounceRate) {
		this.bounceRate = bounceRate;
	}
	
	public static Finder<Long,ClickyEntranceList> find = new Finder<>(Long.class,ClickyEntranceList.class);
	
	public static List<ClickyEntranceList> getAll(Date sDate,Date eDate) {
		return find.where().between("saveDate", sDate, eDate).findList();
	}
	
	
	public static List<ClickyEntranceList> getAllData(Date sDate) {
		return find.where().eq("saveDate", sDate).findList();
	}
	
	public static List<ClickyEntranceList> getfindAll() {
		return find.all();
		
		
	}
	
	
	public static ClickyEntranceList findById(Long id) {
		return find.byId(id);
	}
	
	public static List<ClickyEntranceList> findByTitleAndDate(String title, Date startdate, Date enddate) {
		return find.where().eq("title", title).between("saveDate", startdate, enddate).findList();
	}
	
	
	

}

