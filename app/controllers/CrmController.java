package controllers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import controllers.MailchipControllers.MailIntegrationServices;

import models.AuthUser;
import models.Contacts;
import models.CustomizationCrm;
import models.Location;
import models.MailchimpSchedular;
import models.PhotographerHoursOfOperation;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import viewmodel.ContactsVM;
import viewmodel.HoursOperation;
import viewmodel.KeyValueDataVM;
import viewmodel.PortalNameVM;
import views.html.home;

public class CrmController extends Controller {

	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	final static String pdfRootDir = Play.application().configuration()
			.getString("pdfRootDir");

	final static String imageUrlPath = Play.application().configuration()
			.getString("image.url.path");

	final static String userRegistration = Play.application().configuration()
			.getString("userRegistration");

	final static String vehicleUrlPath = Play.application().configuration()
			.getString("vehicle.url.path");

	final static String mashapeKey = Play.application().configuration()
			.getString("mashapeKey");

	final static String emailUsername = Play.application().configuration()
			.getString("mail.username");

	final static String emailPassword = Play.application().configuration()
			.getString("mail.password");

	static String simulatevin = "{    'success': true,    'specification': {        'vin': 'WDDNG7KB7DA494890',        'year': '2013',        'make': 'Mercedes-Benz',        'model': 'S-Class',        'trim_level': 'S65 AMG',        'engine': '6.0L V12 SOHC 36V TURBO',        'style': 'SEDAN 4-DR',        'made_in': 'GERMANY',        'steering_type': 'R&P',        'anti_brake_system': '4-Wheel ABS',        'tank_size': '23.80 gallon',        'overall_height': '58.00 in.',        'overall_length': '206.50 in.',        'overall_width': '73.70 in.',        'standard_seating': '5',        'optional_seating': null,        'highway_mileage': '19 miles/gallon',        'city_mileage': '12 miles/gallon'    },    'vin': 'WDDNG7KB7DA494890'}";

	private static boolean simulate = false;

	
	
	
	public static Result saveContactsData() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		AuthUser userObj = (AuthUser) getLocalUser();
    		
    		Form<ContactsVM> form = DynamicForm.form(ContactsVM.class).bindFromRequest();
    		ContactsVM vm = form.get();
    		Contacts obj = Contacts.findByEmail(vm.email);
    		String msg = "";
    		if(obj == null) {
    		Contacts contacts = new Contacts();
    		 contacts.setFirstName(vm.firstName);
 		    contacts.setCompanyName(vm.companyName);
 		    contacts.setEmail(vm.email);
 		    contacts.setPhone(vm.phone);
 		    contacts.setWebsite(vm.website);
 		    contacts.setAllAddresses(vm.allAddresses);
 		   	contacts.setTitle(vm.title);
 		   	if(vm.assignedTo == null){
 		   		contacts.setAssignedTo(userObj.id.toString());
 		   	}else{
 		   		contacts.setAssignedTo(vm.assignedTo);
 		   	}
 		    contacts.setCampaignSource(vm.campaignSource);
 		    contacts.setPriority(vm.priority);
 		    contacts.setGroups(vm.groups);
 		    contacts.setWorkEmail(vm.workEmail);
 		    contacts.setWorkEmail1(vm.workEmail1);
 		    contacts.setWorkPhone(vm.workPhone);
 		    contacts.setWorkPhone1(vm.workPhone1);
 		    contacts.setEmail1(vm.email1);
 		    contacts.setPhone1(vm.phone1);
 		    //contacts.userId = userObj;
 		    contacts.setUser(userObj.id);
 		    contacts.setLocations(userObj.location);
 		    contacts.setType("Offline");
 		    contacts.setEnthicity(vm.enthicity);
 		    contacts.setCustZipCode(vm.zip);
 		   contacts.setLastName(vm.lastName);
 		   /*
 		     contacts.setAllEmail(vm.allEmail);
 		    contacts.setAllPhone(vm.allPhone);
 		    contacts.setType(vm.type);
 		    contacts.setSalutation(vm.salutation);
 		    contacts.setMiddleName(vm.middleName);
 		    contacts.setLastName(vm.lastName);
 		    contacts.setSuffix(vm.suffix);  
 		    contacts.setStreet(vm.street);
 		   	contacts.setCity(vm.city);
 		    contacts.setState(vm.state);
 		   	contacts.setZip(vm.zip);
 		    contacts.setCountry(vm.country);
 		    contacts.setBirthday(vm.birthday);
 		    contacts.setBackgroundInfo(vm.backgroundInfo);
 		    contacts.setIndustry(vm.industry);
 		    contacts.setNumberOfEmployees(vm.numberOfEmployees);
 		    contacts.setCreationDate(vm.creationDate);
 		    contacts.setLastEditedDate(vm.lastEditedDate);
 		    contacts.setRelationships(vm.relationships);
 		    contacts.setNotes(vm.notes);*/
    			/*if(vm.newsletter == true) {
    				contacts.setNewsLetter(1);
    			} else {
    				contacts.setNewsLetter(0);
    			}*/
 		   contacts.setNewsLetter(1);
    			contacts.save();
    			saveCustomCrmData(contacts.contactId,vm);
    			MailchimpSchedular mSchedular = MailchimpSchedular.findByLocations(16L); //Long.valueOf(session("USER_LOCATION"))
    			if(mSchedular != null){
    				if(mSchedular.schedularTime.equals("Immediately")){
    					MailIntegrationServices objMail = new MailIntegrationServices();
    	    			//obj.getLists(Long.valueOf(session("USER_LOCATION")));
    	    			objMail.addUser(vm.firstName, vm.lastName, vm.email);
    				}
    			}
    			
    			
    		} else {
    			msg = "Email already exists";
    		}
    		return ok(msg);
    	}
	}
	
	public static Result updateContactsData() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Form<ContactsVM> form = DynamicForm.form(ContactsVM.class).bindFromRequest();
    		ContactsVM vm = form.get();
    		Contacts contacts = Contacts.findById(vm.contactId);
    			contacts.setType(vm.type);
    			contacts.setSalutation(vm.salutation);
    			contacts.setFirstName(vm.firstName);
    			contacts.setMiddleName(vm.middleName);
    			contacts.setLastName(vm.lastName);
    			contacts.setSuffix(vm.suffix);
    			contacts.setCompanyName(vm.companyName);
    			contacts.setEmail(vm.email);
    			contacts.setPhone(vm.phone);
    			contacts.setStreet(vm.street);
    			contacts.setCity(vm.city);
    			contacts.setState(vm.state);
    			contacts.setZip(vm.zip);
    			contacts.setCustZipCode(vm.zip);
    			contacts.setEnthicity(vm.enthicity);
    			contacts.setCountry(vm.country);
    			contacts.setAllEmail(vm.allEmail);
    			contacts.setAllPhone(vm.allPhone);
    			contacts.setWebsite(vm.website);
    			contacts.setAllAddresses(vm.allAddresses);
    			contacts.setTitle(vm.title);
    			contacts.setBirthday(vm.birthday);
    			contacts.setBackgroundInfo(vm.backgroundInfo);
    			contacts.setIndustry(vm.industry);
    			contacts.setNumberOfEmployees(vm.numberOfEmployees);
    			contacts.setCreationDate(vm.creationDate);
    			contacts.setLastEditedDate(vm.lastEditedDate);
    			contacts.setAssignedTo(vm.assignedTo);
    			contacts.setCampaignSource(vm.campaignSource);
    			contacts.setPriority(vm.priority);
    			contacts.setGroups(vm.groups);
    			contacts.setRelationships(vm.relationships);
    			contacts.setNotes(vm.notes);
    			contacts.update();
    			saveCustomCrmData(contacts.contactId,vm);
    		return ok();
    	}
	}
	
	private static void saveCustomCrmData(Long InventoryId,ContactsVM vm) {
       	
       	for(KeyValueDataVM custom:vm.customData){
       		
       		CustomizationCrm cDataValue = CustomizationCrm.findByKeyAndLeadId(custom.key,InventoryId);
       		if(cDataValue == null){
       			CustomizationCrm cValue = new CustomizationCrm();
       			cValue.keyValue = custom.key;
       			cValue.value = custom.value;
       			cValue.crmId = InventoryId;
       			cValue.displayGrid = custom.displayGrid;
       			cValue.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
       			cValue.save();
       			
       		}else{
       			cDataValue.setKeyValue(custom.key);
       			cDataValue.setValue(custom.value);
       			cDataValue.setDisplayGrid(custom.displayGrid);
       			cDataValue.update();
       		}
   			
   		}
       }
	 
	public static Result callList() {
		
		MailIntegrationServices obj = new MailIntegrationServices();
		obj.getLists(Long.valueOf(session("USER_LOCATION")));
		//getLists();
		return ok();
	}
	
	 public static Result getMailChimpList(){

			Date curr = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String cDate = df.format(curr);
			Date cuDate = null;
			try {
				cuDate = df.parse(cDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(cuDate);
			MailchimpSchedular mScheduler = MailchimpSchedular.findByLocations(16L);
			if(mScheduler != null){
				if(mScheduler.schedularTime.equals("Daily")){
					cal1.add(Calendar.DATE, -1);
				}else if(mScheduler.schedularTime.equals("Weekly")){
					cal1.add(Calendar.DATE, -7);
				}else if(mScheduler.schedularTime.equals("Monthly")){
					cal1.add(Calendar.MONTH, -1);
				}
			}	
			
			cuDate = cal1.getTime();
			
			
			List<Contacts> contacts = Contacts.findByDateWise(cuDate);
			for(Contacts cont:contacts){
				 MailIntegrationServices objMail = new MailIntegrationServices();
					//obj.getLists(Long.valueOf(session("USER_LOCATION")));
					objMail.addUser(cont.firstName, cont.lastName, cont.email);
			}
		 
		
		 
		 return ok();
	 }
	
	 public static Result uploadContactsFile() throws Exception {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	MultipartFormData body = request().body().asMultipartFormData();
		    	
		    	AuthUser userObj = (AuthUser) getLocalUser();
		    	
		    	FilePart fileData = body.getFile("file0");
		    	  if (fileData != null) {
		    	    String fileName = fileData.getFilename();
		    	    File file = fileData.getFile();
		    	    com.opencsv.CSVReader reader = new com.opencsv.CSVReader(new FileReader(file), ',', '"','\0', 1);
		    	    
		    	    List<String[]> allRows = reader.readAll();
		    	       
		    	     for(String[] row : allRows){
		    	    	
			    	    	 Contacts contactObj = null;
			    	    	 if(row.length >= 9) {
				    	    	 if( row[8] != null && !row[8].isEmpty()) {
				    	    		 if(!row[8].trim().equals("")) {
				    	    			 contactObj = Contacts.findByEmail(row[8]);
				    	    		 }
				    	    	 }
			    	    	 }
			    	    	 if(contactObj == null) {
				    	    	 Contacts contact = new Contacts();
				    	    	 if(row.length >= 2) {
				    	    		 contact.type = row[1];
				    	    	 }
				    	    	 if(row.length >= 3) {
				    	    		 contact.salutation = row[2];
				    	    	 }
				    	    	 if(row.length >= 4) {
				    	    		 contact.firstName = row[3];
				    	    	 }
				    	    	 if(row.length >= 5) {
				    	    		 contact.middleName = row[4];
				    	    	 }
				    	    	 if(row.length >= 6) {
				    	    		 contact.lastName = row[5];
				    	    	 }
				    	    	 if(row.length >= 7) {
				    	    		 contact.suffix = row[6];
				    	    	 }
				    	    	 if(row.length >= 8) {
				    	    		 contact.companyName = row[7];
				    	    	 }
				    	    	 if(row.length >= 9) {
				    	    		 contact.email = row[8];
				    	    	 }
				    	    	 if(row.length >= 10) {
				    	    		 contact.phone = row[9];
				    	    	 }
				    	    	 if(row.length >= 11) {
				    	    		 contact.street = row[10];
				    	    	 }
				    	    	 if(row.length >= 12) {
				    	    		 contact.city = row[11];
				    	    	 }
				    	    	 if(row.length >= 13) {
				    	    		 contact.state = row[12];
				    	    	 }
				    	    	 if(row.length >= 14) {
				    	    		 contact.zip = row[13];
				    	    	 }
				    	    	 if(row.length >= 15) {
				    	    		 contact.country = row[14];
				    	    	 }
				    	    	 if(row.length >= 16) {
				    	    		 contact.allEmail = row[15];
				    	    	 }
				    	    	 if(row.length >= 17) {
				    	    		 contact.allPhone = row[16];
				    	    	 }
				    	    	 if(row.length >= 18) {
				    	    		 contact.website = row[17];
				    	    	 }
				    	    	 if(row.length >= 19) {
				    	    		 contact.allAddresses = row[18];
				    	    	 }
				    	    	 if(row.length >= 20) {
				    	    		 contact.title = row[19];
				    	    	 }
				    	    	 if(row.length >= 21) {
				    	    		 contact.birthday = row[20];
				    	    	 }
				    	    	 if(row.length >= 22) {
				    	    		 contact.backgroundInfo = row[21];
				    	    	 }
				    	    	 if(row.length >= 23) {
				    	    		 contact.industry = row[22];
				    	    	 }
				    	    	 if(row.length >= 24) {
				    	    		 contact.numberOfEmployees = row[23];
				    	    	 }
				    	    	 if(row.length >= 25) {
				    	    		 contact.creationDate = row[24];
				    	    	 }
				    	    	 if(row.length >= 26) {
				    	    		 contact.lastEditedDate = row[25];
				    	    	 }
				    	    	 if(row.length >= 27) {
				    	    		 contact.assignedTo = row[26];
				    	    	 }
				    	    	 if(row.length >= 28) {
				    	    		 contact.campaignSource = row[27];
				    	    	 }
				    	    	 if(row.length >= 29) {
				    	    		 contact.priority = row[28];
				    	    	 }
				    	    	 if(row.length >= 30) {
				    	    		 contact.groups = row[29];
				    	    	 }
				    	    	 if(row.length >= 31) {
				    	    		 contact.relationships = row[30];
				    	    	 }
				    	    	 if(row.length >= 32) {
				    	    		 contact.notes = row[31];
				    	    	 }
				    	    	 contact.newsLetter = 0;
				    	    	 contact.save();
				    	    	 
				    	    	 MailchimpSchedular mSchedular = MailchimpSchedular.findByLocations(16L); //Long.valueOf(session("USER_LOCATION"))
				     			if(mSchedular != null){
				     				if(mSchedular.schedularTime.equals("Immediately")){
				     					MailIntegrationServices objMail = new MailIntegrationServices();
				     	    			//obj.getLists(Long.valueOf(session("USER_LOCATION")));
				     	    			objMail.addUser(contact.firstName, contact.lastName, contact.email);
				     				}
				     			}
				    	    	 
			    	    	 } else {
			    	    		 if(row.length >= 2) {
			    	    			 contactObj.setType(row[1]);
			    	    		 }
			    	    		 if(row.length >= 3) {
			    	    			 contactObj.setSalutation(row[2]);
			    	    		 }
			    	    		 if(row.length >= 4) {
			    	    			 contactObj.setFirstName(row[3]);
			    	    		 }
			    	    		 if(row.length >= 5) {
			    	    			 contactObj.setMiddleName(row[4]);
			    	    		 }
			    	    		 if(row.length >= 6) {
			    	    			 contactObj.setLastName(row[5]);
			    	    		 }
			    	    		 if(row.length >= 7) {
			    	    			 contactObj.setSuffix(row[6]);
			    	    		 }
			    	    		 if(row.length >= 8) {
			    	    			 contactObj.setCompanyName(row[7]);
			    	    		 }
			    	    		 if(row.length >= 9) {
			    	    			 contactObj.setEmail(row[8]);
			    	    		 }
			    	    		 if(row.length >= 10) {
			    	    			 contactObj.setPhone(row[9]);
			    	    		 }
			    	    		 if(row.length >= 11) {
			    	    			 contactObj.setStreet(row[10]);
			    	    		 }
			    	    		 if(row.length >= 12) {
			    	    			 contactObj.setCity(row[11]);
			    	    		 }
			    	    		 if(row.length >= 13) {
			    	    			 contactObj.setState(row[12]);
			    	    		 }
			    	    		 if(row.length >= 14) {
			    	    			 contactObj.setZip(row[13]);
			    	    		 }
			    	    		 if(row.length >= 15) {
			    	    			 contactObj.setCountry(row[14]);
			    	    		 }
			    	    		 if(row.length >= 16) {
			    	    			 contactObj.setAllEmail(row[15]);
			    	    		 }
			    	    		 if(row.length >= 17) {
			    	    			 contactObj.setAllPhone(row[16]);
			    	    		 }
			    	    		 if(row.length >= 18) {
			    	    			 contactObj.setWebsite(row[17]);
			    	    		 }
			    	    		 if(row.length >= 19) {
			    	    			 contactObj.setAllAddresses(row[18]);
			    	    		 }
			    	    		 if(row.length >= 20) {
			    	    			 contactObj.setTitle(row[19]);
			    	    		 }
			    	    		 if(row.length >= 21) {
			    	    			 contactObj.setBirthday(row[20]);
			    	    		 }
			    	    		 if(row.length >= 22) {
			    	    			 contactObj.setBackgroundInfo(row[21]);
			    	    		 }
			    	    		 if(row.length >= 23) {
			    	    			 contactObj.setIndustry(row[22]);
			    	    		 }
			    	    		 if(row.length >= 24) {
			    	    			 contactObj.setNumberOfEmployees(row[23]);
			    	    		 }
			    	    		 if(row.length >= 25) {
			    	    			 contactObj.setCreationDate(row[24]);
			    	    		 }
			    	    		 if(row.length >= 26) {
			    	    			 contactObj.setLastEditedDate(row[25]);
			    	    		 }
			    	    		 if(row.length >= 27) {
			    	    			 contactObj.setAssignedTo(row[26]);
			    	    		 }
			    	    		 if(row.length >= 28) {
			    	    			 contactObj.setCampaignSource(row[27]);
			    	    		 }
			    	    		 if(row.length >= 29) {
			    	    			 contactObj.setPriority(row[28]);
			    	    		 }
			    	    		 if(row.length >= 30) {
			    	    			 contactObj.setGroups(row[29]);
			    	    		 }
			    	    		 if(row.length >= 31) {
			    	    			 contactObj.setRelationships(row[30]);
			    	    		 }
			    	    		 if(row.length >= 32) {
			    	    			 contactObj.setNotes(row[31]);
			    	    		 }
				    	    	 contactObj.update();
				    	    	 
				    	    	 MailchimpSchedular mSchedular = MailchimpSchedular.findByLocations(16L); //Long.valueOf(session("USER_LOCATION"))
					     			if(mSchedular != null){
					     				if(mSchedular.schedularTime.equals("Immediately")){
					     					MailIntegrationServices objMail = new MailIntegrationServices();
					     	    			//obj.getLists(Long.valueOf(session("USER_LOCATION")));
					     	    			objMail.addUser(contactObj.firstName, contactObj.lastName, contactObj.email);
					     				}
					     			}
			    	    	 }
		    	    	 
		    	     }
		    	  } 
		    	return ok();
	    	}	
	    }
	 
	 public static Result removeAllContactsData(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		AuthUser userObj = (AuthUser) getLocalUser();
	    		List<Contacts> contactsList;
	    		if(userObj.role.equalsIgnoreCase("Manager")){
	    			contactsList = Contacts.getAllContactsByLocation(Long.valueOf(session("USER_LOCATION")));
	    		}else{
	    			contactsList = Contacts.getAllContactsByUser(userObj.id);
	    		}
	    		
	    		for (Contacts con : contactsList) {
					con.delete();
				}
	    		return ok();
	    	}
		}
	 
	 public static Result exportContactsData(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		FileWriter fileWriter = null;
	    		String COMMA_DELIMITER = ",";
	    		String NEW_LINE_SEPARATOR = "\n";
	    		
	    		File fdir = new File(rootDir+File.separator+"CsvFile");
	       	    if(!fdir.exists()) {
	       	    	fdir.mkdir();
	       	    }
	       	    String filePath = rootDir+File.separator+"CsvFile/contacts.csv";
	    		
	    		
	    		try {
	       			Boolean sts = FileUtils.deleteQuietly(new File(filePath));
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	    		
	    		String FILE_HEADER = "ContactId,Type,Salutation,FirstName,MiddleName,LastName,Suffix,CompanyName,Email,WorkEmail,Email1,WorkEmail1,Phone,WorkPhone,Phone1,WorkPhone1,Street,City,State,Zip,Countrt,AllEmail,AllPhone,Website,AllAddress,Title,Birthday,BackgroundInfo,Industry,NumberOfEmployees,CreationDate,LastEditedDate,AssignnedTo,CampaignSource,Priority,Groups,Relationship,Notes,Version,Newsletter,AddedBy";
	    		try {

	    			fileWriter = new FileWriter(filePath);
	        		fileWriter.append(FILE_HEADER.toString());
	        		fileWriter.append(NEW_LINE_SEPARATOR);
	        		
	        		List<Contacts> list = Contacts.getAllContacts();
	        		
	        		for (Contacts contacts : list) {
	        			
	        			fileWriter.append(String.valueOf(contacts.contactId));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.type));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.salutation));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.firstName));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.middleName));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.lastName));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.suffix));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.companyName));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.email));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.workEmail));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.email1));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.workEmail1));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.phone));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.workPhone));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.phone1));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.workPhone1));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.street));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.city));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.state));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.zip));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.country));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.allEmail));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.allPhone));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.website));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.allAddresses));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.title));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.birthday));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.backgroundInfo));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.industry));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.numberOfEmployees));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.creationDate));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.lastEditedDate));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.assignedTo));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.campaignSource));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.priority));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.groups));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.relationships));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.notes));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.version));
	            		fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.newsLetter));
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(String.valueOf(contacts.user));
	        			fileWriter.append(NEW_LINE_SEPARATOR);
					}
	        		System.out.println("CSV file was created successfully !!!");
	        		
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					            try {
					                fileWriter.flush();
					                fileWriter.close();
					            } catch (IOException e) {
					                System.out.println("Error while flushing/closing fileWriter !!!");
					                e.printStackTrace();
					            }
					             
					        }
	    		
	    		return ok();
	    	}
		}
	 

		
		public static Result getAllContactsData() {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
	    		List<ContactsVM> contactsVMList = new ArrayList<>();
	    		List<Contacts> contactsList;
	    		if(userObj.role.equalsIgnoreCase("General Manager")){
	    			contactsList = Contacts.getAllContacts();
	    		}else if(userObj.role.equalsIgnoreCase("Manager")){
	    			contactsList = Contacts.getAllContactsByLocation(Long.valueOf(session("USER_LOCATION")));
	    		}else{
	    			contactsList = Contacts.getAllContactsByUser(userObj.id);
	    		}
	    		
	    		for(Contacts contact : contactsList) {
	    			ContactsVM vm = new ContactsVM();
	    			vm.contactId = contact.contactId;
	       			vm.type = contact.type;
	       			vm.salutation = contact.salutation;
	       			vm.firstName = contact.firstName;
	       			vm.middleName = contact.middleName;
	       			vm.lastName = contact.lastName;
	       			vm.suffix = contact.suffix;
	       			vm.companyName = contact.companyName;
	       			vm.email = contact.email;
	       			vm.phone = contact.phone;
	       			vm.street = contact.street;
	       			vm.city = contact.city;
	       			vm.state = contact.state;
	       			vm.zip = contact.custZipCode;
	       			vm.country = contact.country;
	       			vm.allEmail = contact.allEmail;
	       			vm.allPhone = contact.allPhone;
	       			vm.website = contact.website;
	       			vm.allAddresses = contact.allAddresses;
	       			vm.title = contact.title;
	       			vm.fullName = contact.firstName+" "+contact.lastName;
	       			vm.enthicity = contact.enthicity;
	       			/*vm.birthday = contact.birthday;
	       			vm.backgroundInfo = contact.backgroundInfo;
	       			vm.industry = contact.industry;
	       			vm.numberOfEmployees = contact.numberOfEmployees;
	       			vm.creationDate = contact.creationDate;
	       			vm.lastEditedDate = contact.lastEditedDate;*/
	       			if(contact.assignedTo !=null){
	       				AuthUser user = AuthUser.findById(Integer.parseInt(contact.assignedTo));
	           			vm.assignedToName = user.firstName+" "+user.lastName;
	       			}
	       			vm.assignedTo = contact.assignedTo;
	       			vm.campaignSource = contact.campaignSource;
	       			vm.priority = contact.priority;
	       			vm.groups = contact.groups;
	       			vm.relationships = contact.relationships;
	       			vm.notes = contact.notes;
	       			vm.workEmail = contact.workEmail;
	       			vm.workEmail1 = contact.workEmail1;
	       			vm.workPhone = contact.workPhone;
	       			vm.workPhone1 = contact.workPhone1;
	       			vm.email1 = contact.email1;
	       			vm.phone1 = contact.phone1;
	       			findCustomCrmData(contact.contactId,vm);
	    			if(contact.newsLetter == 0) {
	    				vm.newsletter = false;
	    			} else {
	    				vm.newsletter = true;
	    			}
	    			contactsVMList.add(vm);
	    		}
	    		return ok(Json.toJson(contactsVMList));
	    	}
		}
		
		public static void findCustomCrmData(Long id,ContactsVM inventoryvm){
	    	List<CustomizationCrm> custData = CustomizationCrm.findByIdList(id);
	    	List<KeyValueDataVM> keyValueList = new ArrayList<>();
	    	Map<String, String> mapCar = new HashMap<String, String>();
	    	for(CustomizationCrm custD:custData){
	    		mapCar.put(custD.keyValue, custD.value);
	    		//if(custD.displayGrid.equals("true")){
	    			//if(keyValueList.size() == 0){
	    				KeyValueDataVM keyValue = new KeyValueDataVM();
	            		keyValue.key = custD.keyValue;
	            		keyValue.value = custD.value;
	            		keyValue.displayGrid = custD.displayGrid;
	            		keyValueList.add(keyValue);
	    			//}else{
	            		/*for(KeyValueDataVM ks:keyValueList){
	    					if(!ks.equals(custD.keyValue)){
	    						KeyValueDataVM keyValue = new KeyValueDataVM();
	    	            		keyValue.key = custD.keyValue;
	    	            		keyValue.value = custD.value;
	    	            		keyValue.displayGrid = custD.displayGrid;
	    	            		keyValueList.add(keyValue);
	    					}
	    				}
	    			}*/
	    			
	    		//}
	    		
	    	}
	    	inventoryvm.customData = keyValueList;
	    	inventoryvm.customMapData = mapCar;
	    }
	 
		
		public static Result getAllContactsByLocation(Long id){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		AuthUser userObj = (AuthUser) getLocalUser();
	    		List<ContactsVM> contactsVMList = new ArrayList<>();
	    		List<Contacts> contactsList = Contacts.getAllContactsByLocation(id);
	    			
	    		for(Contacts contact : contactsList) {
	    			ContactsVM vm = new ContactsVM();
	    			vm.contactId = contact.contactId;
	       			vm.type = contact.type;
	       			vm.salutation = contact.salutation;
	       			vm.firstName = contact.firstName;
	       			vm.middleName = contact.middleName;
	       			vm.lastName = contact.lastName;
	       			vm.suffix = contact.suffix;
	       			vm.companyName = contact.companyName;
	       			vm.email = contact.email;
	       			vm.phone = contact.phone;
	       			vm.street = contact.street;
	       			vm.city = contact.city;
	       			vm.state = contact.state;
	       			vm.zip = contact.custZipCode;
	       			vm.country = contact.country;
	       			vm.allEmail = contact.allEmail;
	       			vm.allPhone = contact.allPhone;
	       			vm.website = contact.website;
	       			vm.allAddresses = contact.allAddresses;
	       			vm.title = contact.title;
	       			vm.fullName = contact.firstName+" "+contact.lastName;
	       			vm.enthicity = contact.enthicity;
	       			if(contact.assignedTo !=null){
	       				AuthUser user = AuthUser.findById(Integer.parseInt(contact.assignedTo));
	           			vm.assignedToName = user.firstName+" "+user.lastName;
	       			}
	       			vm.assignedTo = contact.assignedTo;
	       			vm.campaignSource = contact.campaignSource;
	       			vm.priority = contact.priority;
	       			vm.groups = contact.groups;
	       			vm.relationships = contact.relationships;
	       			vm.notes = contact.notes;
	       			vm.workEmail = contact.workEmail;
	       			vm.workEmail1 = contact.workEmail1;
	       			vm.workPhone = contact.workPhone;
	       			vm.workPhone1 = contact.workPhone1;
	       			vm.email1 = contact.email1;
	       			vm.phone1 = contact.phone1;
	       			
	    			if(contact.newsLetter == 0) {
	    				vm.newsletter = false;
	    			} else {
	    				vm.newsletter = true;
	    			}
	    			contactsVMList.add(vm);
	    		}
	    		return ok(Json.toJson(contactsVMList));
	    	}
		}
		

		public static Result addNewsLetter(String flag,Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Contacts infoObj = Contacts.findById(id);
		    		if(flag.equals("true")) {
		    			infoObj.setNewsLetter(1);
		    		}
		    		if(flag.equals("false")) {
		    			infoObj.setNewsLetter(0);
		    		}
		    		infoObj.update();
		    		return ok();
	    	}  		
		}

		public static Result deleteContactsById(Long id ){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
			   	return ok(home.render("",userRegistration));
			   	} else {
				   	String msg;
				   	Contacts contact = Contacts.findById(id);
				   	if(contact !=null){
					   	contact.delete();
					   	msg = "success";
					   	return ok(msg);
				   	}
				   	msg = "error";
				   	return ok(msg);
			   	}
			}
		
	 
	  public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}

	
}