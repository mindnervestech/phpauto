package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SalesPlanSchedule extends Model {

	@Id
	public Long id;
	public Date startDate;
	public Date endDate;
	public String totalEarn;
	public String carsSold;
	public String contractsSign;
	public String dayContract;
	public String weekContract;
	public String monthContract	;
	public String quarterContract;
	public String sixMonthContract;
	public String totalMeetingAm;
	public String meetingSalesAm;
	public String workWithClient;
	
	@ManyToOne
	public AuthUser user;
	@OneToOne
	public Location locations;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

		public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTotalEarn() {
		return totalEarn;
	}

	public void setTotalEarn(String totalEarn) {
		this.totalEarn = totalEarn;
	}

	public String getCarsSold() {
		return carsSold;
	}

	public void setCarsSold(String carsSold) {
		this.carsSold = carsSold;
	}

	public String getContractsSign() {
		return contractsSign;
	}

	public void setContractsSign(String contractsSign) {
		this.contractsSign = contractsSign;
	}

	public String getDayContract() {
		return dayContract;
	}

	public void setDayContract(String dayContract) {
		this.dayContract = dayContract;
	}

	public String getWeekContract() {
		return weekContract;
	}

	public void setWeekContract(String weekContract) {
		this.weekContract = weekContract;
	}

	public String getMonthContract() {
		return monthContract;
	}

	public void setMonthContract(String monthContract) {
		this.monthContract = monthContract;
	}

	public String getQuarterContract() {
		return quarterContract;
	}

	public void setQuarterContract(String quarterContract) {
		this.quarterContract = quarterContract;
	}

	public String getSixMonthContract() {
		return sixMonthContract;
	}

	public void setSixMonthContract(String sixMonthContract) {
		this.sixMonthContract = sixMonthContract;
	}

	public String getTotalMeetingAm() {
		return totalMeetingAm;
	}

	public void setTotalMeetingAm(String totalMeetingAm) {
		this.totalMeetingAm = totalMeetingAm;
	}

	public String getMeetingSalesAm() {
		return meetingSalesAm;
	}

	public void setMeetingSalesAm(String meetingSalesAm) {
		this.meetingSalesAm = meetingSalesAm;
	}

	public String getWorkWithClient() {
		return workWithClient;
	}

	public void setWorkWithClient(String workWithClient) {
		this.workWithClient = workWithClient;
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

	public static Finder<Long,SalesPlanSchedule> find = new Finder<>(Long.class,SalesPlanSchedule.class);
	
	
	
	public static SalesPlanSchedule findById(Long id) {
		return find.byId(id);
	}
	
	public static List<SalesPlanSchedule> findAllData() {
		return find.all();
	}
	
	public static List<SalesPlanSchedule> findAllByUser(AuthUser user) {
		return find.where().eq("user", user).findList();
		
		
	}
	public static List<SalesPlanSchedule> findAllByLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).findList();
	}
	
	
}
