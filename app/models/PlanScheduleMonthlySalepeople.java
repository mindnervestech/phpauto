package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class PlanScheduleMonthlySalepeople extends Model {

	@Id
	public Long id;
	public String month;
	public String totalBrought;
	public String vehicalesToSell;
	public String testDrives;
	public String returningCustomers;
	public String outofSale;
	public String newCustomers;
	public String leadsToGenerate;
	public String emails;
	public String cell;
	public Integer flagMsg;
	public String successRate;
	
	@ManyToOne
	public AuthUser user;
	
	@ManyToOne
	public Location locations;


	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTotalBrought() {
		return totalBrought;
	}

	public void setTotalBrought(String totalBrought) {
		this.totalBrought = totalBrought;
	}

	public String getVehicalesToSell() {
		return vehicalesToSell;
	}

	public void setVehicalesToSell(String vehicalesToSell) {
		this.vehicalesToSell = vehicalesToSell;
	}

	public String getTestDrives() {
		return testDrives;
	}

	public void setTestDrives(String testDrives) {
		this.testDrives = testDrives;
	}

	public String getReturningCustomers() {
		return returningCustomers;
	}

	public void setReturningCustomers(String returningCustomers) {
		this.returningCustomers = returningCustomers;
	}

	public String getOutofSale() {
		return outofSale;
	}

	public void setOutofSale(String outofSale) {
		this.outofSale = outofSale;
	}

	public String getNewCustomers() {
		return newCustomers;
	}

	public void setNewCustomers(String newCustomers) {
		this.newCustomers = newCustomers;
	}

	
	
	public Integer getFlagMsg() {
		return flagMsg;
	}

	public void setFlagMsg(Integer flagMsg) {
		this.flagMsg = flagMsg;
	}

	public String getLeadsToGenerate() {
		return leadsToGenerate;
	}

	public void setLeadsToGenerate(String leadsToGenerate) {
		this.leadsToGenerate = leadsToGenerate;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(String successRate) {
		this.successRate = successRate;
	}


	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}

	public Location getLocations() {
		return locations;
	}

	public void setLocations(Location locations) {
		this.locations = locations;
	}

	public static Finder<Long,PlanScheduleMonthlySalepeople> find = new Finder<>(Long.class,PlanScheduleMonthlySalepeople.class);
	
	public static PlanScheduleMonthlySalepeople findById(Long id) {
		return find.byId(id);
	}
	
	public static PlanScheduleMonthlySalepeople findByUser(AuthUser user) {
		return find.where().eq("user", user).findUnique();
	}
	
	public static List<PlanScheduleMonthlySalepeople> findByListUser(AuthUser user) {
		return find.where().eq("user", user).findList();
	}
	
	public static List<PlanScheduleMonthlySalepeople> findByListByMonth(String months) {
		return find.where().eq("month", months).findList();
	}
	
	public static List<PlanScheduleMonthlySalepeople> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	
	public static PlanScheduleMonthlySalepeople findByUserMonth(AuthUser user,String month) {
		return find.where().eq("user", user).eq("month", month).findUnique();
	}
	
	public static PlanScheduleMonthlySalepeople findByLocationAndMonth(Location location,String month) {
		return find.where().eq("locations", location).eq("month", month).findUnique();
	}
	
	public static List<PlanScheduleMonthlySalepeople> findByAllLocationAndMonth(Location location,String month) {
		return find.where().eq("locations", location).eq("month", month).findList();
	}
	
	public static List<PlanScheduleMonthlySalepeople> findByAllMsgPlan(AuthUser user) {
		return find.where().eq("flagMsg", 1).eq("user", user).findList();
	}
}
