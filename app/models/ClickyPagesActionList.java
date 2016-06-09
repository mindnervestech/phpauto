package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ClickyPagesActionList extends Model {
	
	@Id
	public Long id;
	public Long clickyPagesId;
	public String visitors;
	public String uniqueVisitor;
	public String action;
	public String averageAction;
	public String totalTime;
	public String averageTime;
	public String bounceRate;
	public Date saveDate;
	public String url;
	
	
	
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
	public Long getClickyPagesId() {
		return clickyPagesId;
	}
	public void setClickyPagesId(Long clickyPagesId) {
		this.clickyPagesId = clickyPagesId;
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
	
	public static Finder<Long,ClickyPagesActionList> find = new Finder<>(Long.class,ClickyPagesActionList.class);
	
	public static List<ClickyPagesActionList> getAll(Date sDate,Date eDate) {
		return find.where().between("saveDate", sDate, eDate).findList();
	}
	
	
	
	public static List<ClickyPagesActionList> getfindAll() {
		return find.all();
		
		
	}
	
	
	public static ClickyPagesActionList findById(Long id) {
		return find.byId(id);
	}
	
	
	
	
	

}
