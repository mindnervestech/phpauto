package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class ClickySearchesRecent extends Model {

	@Id
	public Long id;
	public String time;
	public String timePretty;
	
	public String visitors;
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



	public String uniqueVisitor;
	public String action;
	public String averageAction;
	public String totalTime;
	public String averageTime;
	public String bounceRate;
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimePretty() {
		return timePretty;
	}

	public void setTimePretty(String timePretty) {
		this.timePretty = timePretty;
	}



	public String title;
	public String statsUrl;
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



	public static Finder<Long,ClickySearchesRecent> find = new Finder<>(Long.class,ClickySearchesRecent.class);
	
	public static List<ClickySearchesRecent> getAll(Date sDate,Date eDate) {
		return find.where().between("saveDate", sDate, eDate).findList();
	}
	
	
	public static List<ClickySearchesRecent> getAllData(Date sDate) {
		return find.where().eq("saveDate", sDate).findList();
	}
	
	
	public static List<ClickySearchesRecent> getfindAll() {
		return find.all();
	}
	
	
	public static ClickySearchesRecent findById(Long id) {
		return find.byId(id);
	}
	
	
	public static List<ClickySearchesRecent> findByTitleAndDate(String title, Date startdate, Date enddate) {
		return find.where().eq("title", title).between("saveDate", startdate, enddate).findList();
	}
	
	
	
	
}

