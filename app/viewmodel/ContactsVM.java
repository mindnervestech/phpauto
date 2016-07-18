package viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactsVM {

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
	public String assignedToName;
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
	public String fullName;
	public String enthicity;
	
	public List<KeyValueDataVM> customData  = new ArrayList<KeyValueDataVM>();
	public Map<String, String> customMapData;
	
	public String getEnthicity() {
		return enthicity;
	}
	public void setEnthicity(String enthicity) {
		this.enthicity = enthicity;
	}
	public String getAssignedToName() {
		return assignedToName;
	}
	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public boolean newsletter;
	
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
	
}
