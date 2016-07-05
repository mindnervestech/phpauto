package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;

@Entity
public class ClickyVisitorsList extends Model {

	@Id
	public Long id;
	public String time;
	public String timePretty;
	public String timeTotal;
	public String ipAddress;
	public String uid;
	public String sessionId;
	public String customSessionId;
	public String actions;
	public String totalVisits;
	public String landingPage;
	public String webBrowser;
	public String operatingSystem;
	public String screenResolution;
	public String javascript;
	public String language;
	public String referrerUrl;
	public String referrerDomain;
	public String referrerType;
	public String geolocation;
	public String countryCode;
	public String latitude;
	public String longitude;
	public String hostname;
	public String organization;
	public String statsUrl;
	public Date DateClick;
	public String visitors;
	public String uniqueVisitor;
	public String action;
	public String averageAction;
	public String totalTime;
	public String averageTime;
	public String bounceRate;
	
	public static Finder<Long,ClickyVisitorsList> find = new Finder<>(Long.class,ClickyVisitorsList.class);
	
	
	
	public String getCustomSessionId() {
		return customSessionId;
	}



	public void setCustomSessionId(String customSessionId) {
		this.customSessionId = customSessionId;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getTime() {
		return time;
	}



	public String getActions() {
		return actions;
	}



	public Date getDateClick() {
		return DateClick;
	}



	public void setDateClick(Date dateClick) {
		DateClick = dateClick;
	}



	public void setActions(String actions) {
		this.actions = actions;
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



	public String getTimeTotal() {
		return timeTotal;
	}



	public void setTimeTotal(String timeTotal) {
		this.timeTotal = timeTotal;
	}



	public String getIpAddress() {
		return ipAddress;
	}



	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	public String getUid() {
		return uid;
	}



	public void setUid(String uid) {
		this.uid = uid;
	}



	public String getSessionId() {
		return sessionId;
	}



	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}



	public String getTotalVisits() {
		return totalVisits;
	}



	public void setTotalVisits(String totalVisits) {
		this.totalVisits = totalVisits;
	}



	public String getLandingPage() {
		return landingPage;
	}



	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}



	public String getWebBrowser() {
		return webBrowser;
	}



	public void setWebBrowser(String webBrowser) {
		this.webBrowser = webBrowser;
	}



	public String getOperatingSystem() {
		return operatingSystem;
	}



	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}



	public String getScreenResolution() {
		return screenResolution;
	}



	public void setScreenResolution(String screenResolution) {
		this.screenResolution = screenResolution;
	}



	public String getJavascript() {
		return javascript;
	}



	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}



	public String getVisitors() {
		return visitors;
	}



	public void setVisitors(String visitors) {
		this.visitors = visitors;
	}



	



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}



	



	public String getUniqueVisitor() {
		return uniqueVisitor;
	}



	public void setUniqueVisitor(String uniqueVisitor) {
		this.uniqueVisitor = uniqueVisitor;
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



	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
	}



	public String getReferrerUrl() {
		return referrerUrl;
	}



	public void setReferrerUrl(String referrerUrl) {
		this.referrerUrl = referrerUrl;
	}



	public String getReferrerDomain() {
		return referrerDomain;
	}



	public void setReferrerDomain(String referrerDomain) {
		this.referrerDomain = referrerDomain;
	}



	public String getReferrerType() {
		return referrerType;
	}



	public void setReferrerType(String referrerType) {
		this.referrerType = referrerType;
	}



	public String getGeolocation() {
		return geolocation;
	}



	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}



	public String getCountryCode() {
		return countryCode;
	}



	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}



	public String getLatitude() {
		return latitude;
	}



	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}



	public String getLongitude() {
		return longitude;
	}



	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}



	public String getHostname() {
		return hostname;
	}



	public void setHostname(String hostname) {
		this.hostname = hostname;
	}



	public String getOrganization() {
		return organization;
	}



	public void setOrganization(String organization) {
		this.organization = organization;
	}



	public String getStatsUrl() {
		return statsUrl;
	}



	public void setStatsUrl(String statsUrl) {
		this.statsUrl = statsUrl;
	}

	public static ClickyVisitorsList findById(Long id) {
		return find.byId(id);
	}

	public static List<ClickyVisitorsList> getAll(Date sDate,Date eDate) {
		return find.where().between("DateClick", sDate, eDate).findList();
	}
	public static List<ClickyVisitorsList> findByTitleAndDate(String geolocation, Date startdate, Date enddate) {
		return find.where().eq("geolocation", geolocation).between("DateClick", startdate, enddate).findList();
	}
	
	public static List<ClickyVisitorsList> getClickyUnikue(String uid,String sessionId) {
		return find.where().eq("uid", uid).eq("sessionId", sessionId).findList();
	}
	public static List<ClickyVisitorsList> getClickySessionData(String sessionId) {
		return find.where().eq("sessionId", sessionId).findList();
	}
	public static List<ClickyVisitorsList> getClickyCustomSessionData(String sessionId) {
		return find.where().eq("customSessionId", sessionId).findList();
	}
	public static List<ClickyVisitorsList> getfindAll() {
		return find.all();
	}
	
	/*public Date getMaxDate(){
		return (Date)entityManager.createQuery("select max(date_click) from clicky_visitors_list").getSingleResult();
	}*/
	
	public static SqlRow getMaxDate() {
		
		SqlQuery q = Ebean.createSqlQuery("select max(date_click) as maxDate from clicky_visitors_list");
		SqlRow rows = q.findUnique();
		return rows;
	}
	
	public static List<ClickyVisitorsList> findByTitle(String title) {
		return find.where().eq("ipAddress", title).findList();
	}
	public static List<ClickyVisitorsList> getIPAddress(String ipAddress) {
		return find.where().eq("ipAddress", ipAddress).findList();
	}
	
}
