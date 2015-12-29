package viewmodel;

import java.util.Date;
import java.util.List;

public class PlanScheduleVM {
	public Long id;
	public Long location;
	public Integer salePerson;
	public String startDate;
	public String endDate;
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
	public String scheduleBy;
	public List<Long> locationList;
	public List<Integer> salesList;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public List<Long> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<Long> locationList) {
		this.locationList = locationList;
	}
	public Long getLocation() {
		return location;
	}
	public void setLocation(Long location) {
		this.location = location;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getScheduleBy() {
		return scheduleBy;
	}
	public void setScheduleBy(String scheduleBy) {
		this.scheduleBy = scheduleBy;
	}
	
	
}
