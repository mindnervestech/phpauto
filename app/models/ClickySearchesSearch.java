	package models;

	import java.util.Date;
	import java.util.List;

	import javax.persistence.Entity;
	import javax.persistence.Id;

	import play.db.ebean.Model;
	import play.db.ebean.Model.Finder;

	@Entity
	public class ClickySearchesSearch extends Model {
		
		@Id
		public Long id;
		public String value;
		public String valuePercent;
		public String title;
		public String statsUrl;
		public String visitors;
		public String uniqueVisitor;
		public String action;
		public String averageAction;
		public String totalTime;
		public String averageTime;
		public String bounceRate;
		
		public String visitors1;
		public String uniqueVisitor1;
		public String action1;
		public String averageAction1;
		public String totalTime1;
		public String averageTime1;
		public String bounceRate1;
		
		
		public String getVisitors1() {
			return visitors1;
		}
		public void setVisitors1(String visitors1) {
			this.visitors1 = visitors1;
		}
		public String getUniqueVisitor1() {
			return uniqueVisitor1;
		}
		public void setUniqueVisitor1(String uniqueVisitor1) {
			this.uniqueVisitor1 = uniqueVisitor1;
		}
		public String getAction1() {
			return action1;
		}
		public void setAction1(String action1) {
			this.action1 = action1;
		}
		public String getAverageAction1() {
			return averageAction1;
		}
		public void setAverageAction1(String averageAction1) {
			this.averageAction1 = averageAction1;
		}
		public String getTotalTime1() {
			return totalTime1;
		}
		public void setTotalTime1(String totalTime1) {
			this.totalTime1 = totalTime1;
		}
		public String getAverageTime1() {
			return averageTime1;
		}
		public void setAverageTime1(String averageTime1) {
			this.averageTime1 = averageTime1;
		}
		public String getBounceRate1() {
			return bounceRate1;
		}
		public void setBounceRate1(String bounceRate1) {
			this.bounceRate1 = bounceRate1;
		}
		public Date saveDate;
		
		
		
		
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
		public static Finder<Long,ClickySearchesSearch > find = new Finder<>(Long.class,ClickySearchesSearch.class);
		
		public static List<ClickySearchesSearch > getAll(Date sDate,Date eDate) {
			return find.where().between("saveDate", sDate, eDate).findList();
		}
		
		
		public static List<ClickySearchesSearch> getAllData(Date sDate) {
			return find.where().eq("saveDate", sDate).findList();
		}
		
		public static List<ClickySearchesSearch > getfindAll() {
			return find.all();
			
			
		}
		
		
		public static ClickySearchesSearch  findById(Long id) {
			return find.byId(id);
		}
		
		public static List<ClickySearchesSearch> findByTitleAndDate(String title, Date startdate, Date enddate) {
			return find.where().eq("title", title).between("saveDate", startdate, enddate).findList();
		}
		
		
		

	}


	

