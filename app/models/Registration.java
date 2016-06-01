package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class Registration extends Model {
	 

	@Id
	public Long id;
	
	public String location;
	public String options;
	public String name;
	public String email;
	public String phone;
	public String businessName;
	public String businessAdd;
	public String tokanNo;
	public String status;
	public Date registrationDate;
	public Date startDate;
	public Date expiryDate;
	public Integer sendDemoFlag;

	public Date activity;
	public Date clientsince;
	
	
	
	
	public static Finder<Long,Registration> find = new Finder<>(Long.class,Registration.class);
	
	
	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	

	public String getTokanNo() {
		return tokanNo;
	}

	public void setTokanNo(String tokanNo) {
		this.tokanNo = tokanNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessAdd() {
		return businessAdd;
	}

	public void setBusinessAdd(String businessAdd) {
		this.businessAdd = businessAdd;
	}
	
	public Integer getSendDemoFlag() {
		return sendDemoFlag;
	}

	public void setSendDemoFlag(Integer sendDemoFlag) {
		this.sendDemoFlag = sendDemoFlag;
	}
	

	


	public Date getClientsince() {
		return clientsince;
	}

	public void setClientsince(Date clientsince) {
		this.clientsince = clientsince;
	}

	public Date getActivity() {
		return activity;
	}

	public void setActivity(Date activity) {
		this.activity = activity;
	}

	public static Registration findById(Long id) {
		return find.byId(id);
	}

	public static List<Registration> getPending() {
		return find.where().eq("status", "pending").findList();
	}
	
	public static Registration getTokanNo(String tokanNo) {
		return find.where().eq("tokanNo", tokanNo).findUnique();
	}
	
	public static List<Registration> findByLocation(Long location) {
		return find.where().eq("locations.id", location).findList();
	}
	public static List<Registration> findByCarAndPending( ){
		return find.where().eq("options","Cars" ).eq("status" , "Pending").findList();
	}
	
	public static List<Registration> findByMotorcyclesAndPending( ){
		return find.where().eq("options","Motorcycles" ).eq("status" , "Pending").findList();
	}
	
	public static List<Registration> findByBoatAndPending( ){
		return find.where().eq("options","Boat" ).eq("status" , "Pending").findList();
	}
	
	public static List<Registration> findByDesignerFurnitureAndPending( ){
		return find.where().eq("options","DesignerFurniture" ).eq("status" , "Pending").findList();
	}
	
	public static List<Registration> findByRealStateAndPending( ){
		return find.where().eq("options","RealEstate" ).eq("status" , "Pending").findList();
	}
	
	public static List<Registration> findByAirplanesAndPending( ){
		return find.where().eq("options","Airplanes" ).eq("status" , "Pending").findList();
	}

	public static List<Registration> findByServiceProviderAndPending( ){
		return find.where().eq("options","ServiceProvider" ).eq("status" , "Pending").findList();
	}
	
	public static List<Registration> findByLuxuryProductsAndPending( ){
		return find.where().eq("options","LuxuryProducts" ).eq("status" , "Pending").findList();
	}
	
	public static List<Registration> findByCarAndStatus( ){
		return find.where().eq("options","Cars" ).eq("status" , "Live").findList();
	}
	
	public static List<Registration> findByBoatAndStatus( ){
		return find.where().eq("options","Boat" ).eq("status" , "Live").findList();
	}
	
	public static List<Registration> findByMotorcyclesAndStatus( ){
		return find.where().eq("options","Motorcycles" ).eq("status" , "Live").findList();
	}
	
	public static List<Registration> findByDesignerFurnitureAndStatus( ){
		return find.where().eq("options","DesignerFurniture" ).eq("status" , "Live").findList();
	}
	
	public static List<Registration> findByAirplanesAndStatus( ){
		return find.where().eq("options","Airplanes" ).eq("status" , "Live").findList();
	}
	
	public static List<Registration> findByRealStateAndStatus( ){
		return find.where().eq("options","RealEstate" ).eq("status" , "Live").findList();
	}
	
	public static List<Registration> findByServiceProviderAndStatus( ){
		return find.where().eq("options","ServiceProvider" ).eq("status" , "Live").findList();
	}
	
	public static List<Registration> findByLuxuryProductsAndStatus( ){
		return find.where().eq("options","LuxuryProducts" ).eq("status" , "Live").findList();
	}
}
