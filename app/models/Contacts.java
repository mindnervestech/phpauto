package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import com.avaje.ebean.Expr;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Contacts extends Model {

	@Id
	public Long contactId;
	public String type;
	public String salutation;
	public String firstName;
	public String middleName;
	public String lastName;
	public String suffix;
	public String companyName;
	public String email;
	public String phone;
	public String street;
	public String city;
	public String state;
	public String zip;
	public String country;
	public String allEmail;
	public String allPhone;
	public String website;
	public String allAddresses;
	public String title;
	public String birthday;
	public String backgroundInfo;
	public String industry;
	public String numberOfEmployees;
	public String creationDate;
	public String lastEditedDate;
	public String assignedTo;
	public String campaignSource;
	public String priority;
	public String groups;
	public String relationships;
	public String notes;
	public String workEmail;
	public String workEmail1;
	public String workPhone;
	public String workPhone1;
	public String email1;
	public String phone1;
	public Integer newsLetter;
	public String custZipCode;
	public String enthicity;
	
	 public Integer user;
	 
	 @ManyToOne
		public Location locations;
	
	public Integer getUser() {
		return user;
	}
	public void setUser(Integer user) {
		this.user = user;
	}



	@Version
	public java.util.Date version; 
	
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAllEmail() {
		return allEmail;
	}
	public void setAllEmail(String allEmail) {
		this.allEmail = allEmail;
	}
	public String getAllPhone() {
		return allPhone;
	}
	public void setAllPhone(String allPhone) {
		this.allPhone = allPhone;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getAllAddresses() {
		return allAddresses;
	}
	public void setAllAddresses(String allAddresses) {
		this.allAddresses = allAddresses;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBackgroundInfo() {
		return backgroundInfo;
	}
	public void setBackgroundInfo(String backgroundInfo) {
		this.backgroundInfo = backgroundInfo;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getCampaignSource() {
		return campaignSource;
	}
	public void setCampaignSource(String campaignSource) {
		this.campaignSource = campaignSource;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getGroups() {
		return groups;
	}
	public void setGroups(String groups) {
		this.groups = groups;
	}
	public String getRelationships() {
		return relationships;
	}
	public void setRelationships(String relationships) {
		this.relationships = relationships;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getNumberOfEmployees() {
		return numberOfEmployees;
	}
	public void setNumberOfEmployees(String numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getLastEditedDate() {
		return lastEditedDate;
	}
	public void setLastEditedDate(String lastEditedDate) {
		this.lastEditedDate = lastEditedDate;
	}
	public Integer getNewsLetter() {
		return newsLetter;
	}
	public void setNewsLetter(Integer newsLetter) {
		this.newsLetter = newsLetter;
	}
	
	public String getCustZipCode() {
		return custZipCode;
	}
	public void setCustZipCode(String custZipCode) {
		this.custZipCode = custZipCode;
	}
	public String getEnthicity() {
		return enthicity;
	}
	public void setEnthicity(String enthicity) {
		this.enthicity = enthicity;
	}
	public Location getLocations() {
		return locations;
	}
	public void setLocations(Location locations) {
		this.locations = locations;
	}
	public String getWorkEmail() {
		return workEmail;
	}
	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}
	public String getWorkEmail1() {
		return workEmail1;
	}
	public void setWorkEmail1(String workEmail1) {
		this.workEmail1 = workEmail1;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	public String getWorkPhone1() {
		return workPhone1;
	}
	public void setWorkPhone1(String workPhone1) {
		this.workPhone1 = workPhone1;
	}



	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}



	public static Finder<Long,Contacts> find = new Finder<>(Long.class,Contacts.class);
	
	public static List<Contacts> getAllContacts() {
		return find.all();
	}
	public static List<Contacts> getAllContactsByUser(int id) {
		return find.where().eq("user", id).findList();
	}
	
	public static List<Contacts> getAllContactsByLocation(Long locationId) {
		return find.where().eq("locations.id", locationId).findList();
	}
	
	public static Contacts findByEmail(String email) {
		return find.where().eq("email", email).findUnique();
	}
	
	public static List<Contacts> findByUser(Integer user) {
		return find.where().eq("user", user).findList();
	}
	
	public static Contacts findById(Long id) {
		return find.byId(id);
	}
	public static List<Contacts> getAllByName(String name) {
		System.out.println(name);
		return find.where().or(Expr.like("firstName", "%"+name+"%"),Expr.like("lastName", "%"+name+"%")).findList();
		//return find.where("firstName like '"+name+"'").findList();
	}
	public static List<Contacts> getAllNewsletter() {
		return find.where().eq("newsLetter", 1).findList();
	}
	public static List<Contacts> findAll() {
		return find.all();
	}
	
}
