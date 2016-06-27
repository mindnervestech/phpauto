package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
	
	
	public static List<ClickyVisitorsList> findByTitle(String title) {
		return find.where().eq("ipAddress", title).findList();
	}
	public static List<ClickyVisitorsList> getIPAddress(String ipAddress) {
		return find.where().eq("ipAddress", ipAddress).findList();
	}
	
}
