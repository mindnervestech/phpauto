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
	
	public String visitors1;
	public String uniqueVisitor1;
	public String action1;
	public String averageAction1;
	public String totalTime1;
	public String averageTime1;
	public String bounceRate1;
	
	public String visitors2;
	public String uniqueVisitor2;
	public String action2;
	public String averageAction2;
	public String totalTime2;
	public String averageTime2;
	public String bounceRate2;
	
	public String visitorsos;
	public String uniqueVisitoros;
	public String actionos;
	public String averageActionos;
	public String totalTimeos;
	public String averageTimeos;
	public String bounceRateos;
	
	public String visitorsbrowser;
	public String uniqueVisitorbrowser;
	public String actionbrowser;
	public String averageActionbrowser;
	public String totalTimebrowser;
	public String averageTimebrowser;
	public String bounceRatebrowser;
	
	public String visitorsorg;
	public String uniqueVisitororg;
	public String actionorg;
	public String averageActionorg;
	public String totalTimeorg;
	public String averageTimeorg;
	public String bounceRateorg;
	
	public static Finder<Long,ClickyVisitorsList> find = new Finder<>(Long.class,ClickyVisitorsList.class);
	
	
	
	


	public String getVisitorsorg() {
		return visitorsorg;
	}



	public void setVisitorsorg(String visitorsorg) {
		this.visitorsorg = visitorsorg;
	}



	public String getUniqueVisitororg() {
		return uniqueVisitororg;
	}



	public void setUniqueVisitororg(String uniqueVisitororg) {
		this.uniqueVisitororg = uniqueVisitororg;
	}



	public String getActionorg() {
		return actionorg;
	}



	public void setActionorg(String actionorg) {
		this.actionorg = actionorg;
	}



	public String getAverageActionorg() {
		return averageActionorg;
	}



	public void setAverageActionorg(String averageActionorg) {
		this.averageActionorg = averageActionorg;
	}



	public String getTotalTimeorg() {
		return totalTimeorg;
	}



	public void setTotalTimeorg(String totalTimeorg) {
		this.totalTimeorg = totalTimeorg;
	}



	public String getAverageTimeorg() {
		return averageTimeorg;
	}



	public void setAverageTimeorg(String averageTimeorg) {
		this.averageTimeorg = averageTimeorg;
	}



	public String getBounceRateorg() {
		return bounceRateorg;
	}



	public void setBounceRateorg(String bounceRateorg) {
		this.bounceRateorg = bounceRateorg;
	}



	public String getVisitorsbrowser() {
		return visitorsbrowser;
	}



	public void setVisitorsbrowser(String visitorsbrowser) {
		this.visitorsbrowser = visitorsbrowser;
	}



	public String getUniqueVisitorbrowser() {
		return uniqueVisitorbrowser;
	}



	public void setUniqueVisitorbrowser(String uniqueVisitorbrowser) {
		this.uniqueVisitorbrowser = uniqueVisitorbrowser;
	}



	public String getActionbrowser() {
		return actionbrowser;
	}



	public void setActionbrowser(String actionbrowser) {
		this.actionbrowser = actionbrowser;
	}



	public String getAverageActionbrowser() {
		return averageActionbrowser;
	}



	public void setAverageActionbrowser(String averageActionbrowser) {
		this.averageActionbrowser = averageActionbrowser;
	}



	public String getTotalTimebrowser() {
		return totalTimebrowser;
	}



	public void setTotalTimebrowser(String totalTimebrowser) {
		this.totalTimebrowser = totalTimebrowser;
	}



	public String getAverageTimebrowser() {
		return averageTimebrowser;
	}



	public void setAverageTimebrowser(String averageTimebrowser) {
		this.averageTimebrowser = averageTimebrowser;
	}



	public String getBounceRatebrowser() {
		return bounceRatebrowser;
	}



	public void setBounceRatebrowser(String bounceRatebrowser) {
		this.bounceRatebrowser = bounceRatebrowser;
	}



	public String getVisitorsos() {
		return visitorsos;
	}



	public void setVisitorsos(String visitorsos) {
		this.visitorsos = visitorsos;
	}



	public String getUniqueVisitoros() {
		return uniqueVisitoros;
	}



	public void setUniqueVisitoros(String uniqueVisitoros) {
		this.uniqueVisitoros = uniqueVisitoros;
	}



	public String getActionos() {
		return actionos;
	}



	public void setActionos(String actionos) {
		this.actionos = actionos;
	}



	public String getAverageActionos() {
		return averageActionos;
	}



	public void setAverageActionos(String averageActionos) {
		this.averageActionos = averageActionos;
	}



	public String getTotalTimeos() {
		return totalTimeos;
	}



	public void setTotalTimeos(String totalTimeos) {
		this.totalTimeos = totalTimeos;
	}



	public String getAverageTimeos() {
		return averageTimeos;
	}



	public void setAverageTimeos(String averageTimeos) {
		this.averageTimeos = averageTimeos;
	}



	public String getBounceRateos() {
		return bounceRateos;
	}



	public void setBounceRateos(String bounceRateos) {
		this.bounceRateos = bounceRateos;
	}



	public String getVisitors2() {
		return visitors2;
	}



	public void setVisitors2(String visitors2) {
		this.visitors2 = visitors2;
	}



	public String getUniqueVisitor2() {
		return uniqueVisitor2;
	}



	public void setUniqueVisitor2(String uniqueVisitor2) {
		this.uniqueVisitor2 = uniqueVisitor2;
	}



	public String getAction2() {
		return action2;
	}



	public void setAction2(String action2) {
		this.action2 = action2;
	}



	public String getAverageAction2() {
		return averageAction2;
	}



	public void setAverageAction2(String averageAction2) {
		this.averageAction2 = averageAction2;
	}



	public String getTotalTime2() {
		return totalTime2;
	}



	public void setTotalTime2(String totalTime2) {
		this.totalTime2 = totalTime2;
	}



	public String getAverageTime2() {
		return averageTime2;
	}



	public void setAverageTime2(String averageTime2) {
		this.averageTime2 = averageTime2;
	}



	public String getBounceRate2() {
		return bounceRate2;
	}



	public void setBounceRate2(String bounceRate2) {
		this.bounceRate2 = bounceRate2;
	}



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
	public static List<ClickyVisitorsList> findByOsAndDate(String operatingSystem, Date startdate, Date enddate) {
		return find.where().eq("operatingSystem", operatingSystem).between("DateClick", startdate, enddate).findList();
	}
	public static List<ClickyVisitorsList> findBybrowserAndDate(String webBrowser, Date startdate, Date enddate) {
		return find.where().eq("webBrowser", webBrowser).between("DateClick", startdate, enddate).findList();
	}
	public static List<ClickyVisitorsList> findByOrgAndDate(String organization, Date startdate, Date enddate) {
		return find.where().eq("organization", organization).between("DateClick", startdate, enddate).findList();
	}
	public static List<ClickyVisitorsList> findByIdAndDate(Long id, Date startdate, Date enddate) {
		return find.where().eq("id", id).between("DateClick", startdate, enddate).findList();
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
