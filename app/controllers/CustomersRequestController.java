package controllers;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import models.AuthUser;
import models.CustomizationDataValue;
import models.EmailDetails;
import models.LeadType;
import models.Location;
import models.MyProfile;
import models.RequestMoreInfo;
import models.ScheduleTest;
import models.SiteLogo;
import models.ToDo;
import models.TradeIn;
import models.UserNotes;
import models.Vehicle;
import models.VehicleImage;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.KeyValueDataVM;
import viewmodel.NoteVM;
import viewmodel.RequestInfoVM;
import views.html.home;

public class CustomersRequestController extends Controller {

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

	  public static Result getAllOtherLeadInfo(String leadId) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		//Long.valueOf(session("USER_LOCATION"))
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<RequestMoreInfo> listData = new ArrayList<>();
		    	if(user.role == null || user.role.equals("General Manager")) {
	    			listData = RequestMoreInfo.findAllOtherLeadIdWise(leadId);
	    		} else {
	    			if(user.role.equals("Manager")) {
	    				listData = RequestMoreInfo.findAllLocationAndOtherLeadDataManager(Long.valueOf(session("USER_LOCATION")),leadId);
	    			} else {
	    				//listData = RequestMoreInfo.findAllByDate();
	    				listData = RequestMoreInfo.findAllLocationAndOtherLeadData(Long.valueOf(session("USER_LOCATION")),leadId);
	    			}
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(RequestMoreInfo info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Vehicle productInfo = Vehicle.findByVinAndStatusForGM(info.vin,Location.findById(Long.valueOf(session("USER_LOCATION"))));
		    		vm.productId = info.productId;
		    		vm.vin = info.vin;
		    		if(productInfo != null) {
		    			vm.make = productInfo.make;
		    			vm.model = productInfo.model;
		    			vm.price = (int) productInfo.price;
		    			vm.year = productInfo.year;
		    			VehicleImage pImage = VehicleImage.getDefaultImage(productInfo.vin);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.requestDate = df.format(info.requestDate);
		    		vm.userRole = user.role;
		    		vm.premiumFlagForSale = user.premiumFlag;
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		Application.findCustomeData(info.id,vm,Long.parseLong(leadId));
		    		infoVMList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }	
	  
	  
	  public static Result getAllSalesPersonOtherLead(Integer id){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser user;
	    		if(id == 0){
	    			user = getLocalUser();
	    		}else{
	    			user = AuthUser.findById(id);
	    		}
	    			
		    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllOtherLead(user);
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
		    	for(RequestMoreInfo info: listData) {
		    		if(!info.isContactusType.equals("contactUs")){
		    			
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Vehicle productInfo = Vehicle.findByVinAndStatusForGM(info.vin,Location.findById(Long.valueOf(session("USER_LOCATION"))));
		    		vm.productId = info.productId;
		    		vm.vin = info.vin;
		    		if(productInfo != null) {
		    			vm.make = productInfo.make;
		    			vm.model = productInfo.model;
		    			vm.price = (int) productInfo.price;
		    			vm.year = productInfo.year;
		    			VehicleImage pImage = VehicleImage.getDefaultImage(productInfo.vin);
		        		if(pImage!=null) {
		        			vm.imgId = pImage.getId().toString();
		        		}
		        		else {
		        			vm.imgId = "/assets/images/no-image.jpg";
		        		}
		    		}
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.isContactusType = info.isContactusType;
		    		
		    		vm.requestDate = df.format(info.requestDate);
		    		vm.productId = info.productId;
		    		
		    		LeadType lType = LeadType.findById(Long.parseLong(info.isContactusType));
		    		if(lType != null){
		    			vm.typeOfLead = lType.leadName;
		    			vm.leadId = lType.id;
		    		}
		    			
		    		List<UserNotes> notesList = UserNotes.findRequestMore(info);
		    		Integer nFlag = 0;
		    		List<NoteVM> list = new ArrayList<>();
		    		for(UserNotes noteObj :notesList) {
		    			NoteVM obj = new NoteVM();
		    			obj.id = noteObj.id;
		    			obj.note = noteObj.note;
		    			obj.action = noteObj.action;
		    			obj.date = df.format(noteObj.createdDate);
		    			obj.time = timedf.format(noteObj.createdTime);
		    			if(noteObj.saveHistory != null){
		    				if(noteObj.saveHistory.equals(1)){
		        				nFlag = 1;
		        			}
		    			}
		    			list.add(obj);
		    		}
		    		vm.note = list;
		    		vm.noteFlag = nFlag;
		    		vm.requestDate = df.format(info.requestDate);
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		
		    		Application.findCustomeData(info.id,vm,lType.id);
		    		Application.findRequestParentChildAndBro(infoVMList, info, df, vm);
		    	}
		    	
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    
	    	
	    }
	  

	  public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}
	  
		public static Result getAllContactInfo() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		//Long.valueOf(session("USER_LOCATION"))
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<RequestMoreInfo> listData = new ArrayList<>();
		    	if(user.role == null || user.role.equals("General Manager")) {
	    			listData = RequestMoreInfo.findAllContactData();
	    		} else {
	    			if(user.role.equals("Manager")) {
	    				listData = RequestMoreInfo.findAllLocationDataManagerContactUs(Long.valueOf(session("USER_LOCATION")));
	    			} else {
	    				//listData = RequestMoreInfo.findAllByDate();
	    				listData = RequestMoreInfo.findAllLocationDataContactUs(Long.valueOf(session("USER_LOCATION")));
	    			}
	    		}
		    	List<RequestInfoVM> infoVMList = new ArrayList<>();
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	for(RequestMoreInfo info: listData) {
		    		RequestInfoVM vm = new RequestInfoVM();
		    		vm.id = info.id;
		    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
		    		vm.vin = info.vin;
		    		if(vehicle != null) {
		    			vm.model = vehicle.model;
		    			vm.make = vehicle.make;
		    			vm.stock = vehicle.stock;
		    			vm.year = vehicle.year;
		    			vm.mileage = vehicle.mileage;
		    			vm.price = vehicle.price;
		    		}
		    		vm.message=info.message;
		    		vm.name = info.name;
		    		vm.phone = info.phone;
		    		vm.email = info.email;
		    		vm.howContactedUs = info.contactedFrom;
		    		vm.howFoundUs = info.hearedFrom;
		    		vm.custZipCode = info.custZipCode;
		    		vm.enthicity = info.enthicity;
		    		vm.requestDate = df.format(info.requestDate);
		    		vm.userRole = user.role;
		    		vm.premiumFlagForSale = user.premiumFlag;
		    		if(info.assignedTo == null) {
		    			vm.status = "Unclaimed";
		    		} else {
			    		if(info.assignedTo != null && info.status == null) {
			    			vm.status = "In Progress";
			    		} else {
			    			vm.status = info.status;
			    		}
		    		}
		    		if(info.assignedTo != null) {
		    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
		    		}
		    		
		    		if(info.isRead == 0) {
		    			vm.isRead = false;
		    		}
		    		
		    		if(info.isRead == 1) {
		    			vm.isRead = true;
		    		}
		    		
		    		infoVMList.add(vm);
		    	}
		    	
		    	return ok(Json.toJson(infoVMList));
	    	}	
	    }
		
		 
		 public static Result requestInfoMarkRead(String flag,Long id) {
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
		    		Date currDate = new Date();
		    		AuthUser user = (AuthUser) getLocalUser();
			    	RequestMoreInfo infoObj = RequestMoreInfo.findById(id);
			    		if(flag.equals("true")) {
			    			infoObj.setIsRead(1);
			    			infoObj.setAssignedTo(user);
			    			
			    			UserNotes uNotes = new UserNotes();
				    		uNotes.setNote("Lead has been claimed");
				    		uNotes.setAction("Other");
				    		uNotes.createdDate = currDate;
				    		uNotes.createdTime = currDate;
				    		uNotes.user = user;
			        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
				    		uNotes.requestMoreInfo = RequestMoreInfo.findById(infoObj.id);
				    		uNotes.save();
			    		}
			    		if(flag.equals("false")) {
			    			infoObj.setIsRead(0);
			    			infoObj.setAssignedTo(null);
			    		}
			    		infoObj.update();
			    		List<RequestMoreInfo> listData = new ArrayList<>();
			    		if(user.role == null) {
			    			listData = RequestMoreInfo.findAllData();
			    		} else {
			    			if(user.role.equals("General Manager")) {
			    				listData = RequestMoreInfo.findAllData();
			    			} else {
			    				listData = RequestMoreInfo.findAllByDate();
			    			}
			    		}
			        	List<RequestInfoVM> infoVMList = new ArrayList<>();
			        	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			        	for(RequestMoreInfo info: listData) {
			        		RequestInfoVM vm = new RequestInfoVM();
			        		vm.id = info.id;
			        		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
			        		vm.vin = info.vin;
			        		if(vehicle != null) {
			        			vm.model = vehicle.model;
			        			vm.make = vehicle.make;
			        			vm.stock = vehicle.stock;
			        		}
			        		vm.name = info.name;
			        		vm.phone = info.phone;
			        		vm.email = info.email;
			        		vm.requestDate = df.format(info.requestDate);
			        		
			        		if(info.assignedTo == null) {
				    			vm.status = "Unclaimed";
				    		} else {
					    		if(info.assignedTo != null && info.status == null) {
					    			vm.status = "In Progress";
					    		} else {
					    			vm.status = info.status;
					    		}
				    		}
				    		if(info.assignedTo != null) {
				    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
				    		}
			        		
			        		if(info.isRead == 0) {
			        			vm.isRead = false;
			        		}
			        		
			        		if(info.isRead == 1) {
			        			vm.isRead = true;
			        		}
			        		
			        		infoVMList.add(vm);
			        	}
			        	
			        	return ok(Json.toJson(infoVMList));
		    	}	
		    }
		 
		 public static Result changeAssignedUser(Long id,Integer user,String leadType) {
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
		    		AuthUser userObj = AuthUser.findById(user);
		    		if(leadType.equals("Schedule Test") || leadType.equals("Schedule Test Drive")) {
		    			ScheduleTest schedule = ScheduleTest.findById(id);
		    			schedule.setAssignedTo(userObj);
		    			schedule.setIsRead(1);
		    			schedule.setPremiumFlag(0);
		    			schedule.setLeadStatus(null);
		    			schedule.setIsReassigned(true);
		    			schedule.update();
		    		}
					if(leadType.equals("Request More Info")) {
					    RequestMoreInfo info = RequestMoreInfo.findById(id);
					    info.setAssignedTo(userObj);
					    info.setPremiumFlag(0);
					    info.setIsRead(1);
					    info.setStatus(null);
					    info.setLeadStatus(null);
					    info.setIsReassigned(true);
					    info.update();
					}
					if(leadType.equals("Trade In") || leadType.equals("Trade-In Appraisal")) {
						TradeIn tradeIn = TradeIn.findById(id);
						tradeIn.setAssignedTo(userObj);
						tradeIn.setPremiumFlag(0);
						tradeIn.setIsRead(1);
						tradeIn.setStatus(null);
						tradeIn.setLeadStatus(null);
						tradeIn.setIsReassigned(true);
						tradeIn.update();
					}
		    		return ok();
		    	}
		    }
		 
		 public static Result getSalesUserValue(){
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
		    		List<AuthUser> SalesUserList = AuthUser.getAllUserByLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
		    		return ok(Json.toJson(SalesUserList));
		    	}
		    }
		 
		 public static Result deletePremiumLead(Long id,String leadType) {
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
		    		if(leadType.equals("Schedule Test")) {
		    			ScheduleTest schedule = ScheduleTest.findById(id);
		    			List<UserNotes> userNote = UserNotes.findScheduleTest(schedule);
		    			for (UserNotes userNotes : userNote) {
		    				userNotes.delete();
						}
		    			schedule.delete();
		    		}
					if(leadType.equals("Request More Info")) {
					    RequestMoreInfo info = RequestMoreInfo.findById(id);
					    List<UserNotes> userNote = UserNotes.findRequestMore(info);
					    for (UserNotes userNotes : userNote) {
		    				userNotes.delete();
						}
					    info.delete();
					}
					if(leadType.equals("Trade In")) {
						TradeIn tradeIn = TradeIn.findById(id);
						List<UserNotes> userNote = UserNotes.findTradeIn(tradeIn);
								for (UserNotes userNotes : userNote) {
				    				userNotes.delete();
								}
						tradeIn.delete();
					}
		    		return ok();
		    	}
		    }
		 
		 public static Result getAllPremiumIn(){
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	AuthUser user = (AuthUser) getLocalUser();
			    	List<TradeIn> listData = new ArrayList<>();
			    	List<RequestMoreInfo> lInfos = new ArrayList<>();
			    	List<ScheduleTest> sList = new ArrayList<>();
			    	if(user.role == null || user.role.equals("General Manager")) {
		    			listData = TradeIn.findAllData();
		    		} else {
		    			int flag = 0;
		    			if(user.role.equals("Sales Person")) {
		    				if(user.premiumFlag.equals("1")){
		    					flag = 1;
		    				}
		    			}
		    			if(user.role.equals("Manager") || flag == 1) {
		    				listData = TradeIn.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
		    				sList = ScheduleTest.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
		    				lInfos = RequestMoreInfo.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
		    			} /*else {
		    				listData = TradeIn.findAllByLocationDate(Long.valueOf(session("USER_LOCATION")));
		    			}*/
		    		}
			    	List<RequestInfoVM> infoVMList = new ArrayList<>();
			    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			    	for(TradeIn info: listData) {
			    		RequestInfoVM vm = new RequestInfoVM();
			    		vm.id = info.id;
			    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
			    		vm.vin = info.vin;
			    		if(vehicle != null) {
			    			vm.model = vehicle.model;
			    			vm.make = vehicle.make;
			    			vm.stock = vehicle.stock;
			    			vm.year = vehicle.year;
			    			vm.mileage = vehicle.mileage;
			    			vm.price = vehicle.price;
			    			
			    			vm.bodyStyle =vehicle.bodyStyle;
			    			vm.drivetrain = vehicle.drivetrain;
			    			vm.engine = vehicle.engine;
			    			vm.transmission = vehicle.transmission;
			    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
			        		if(vehicleImage!=null) {
			        			vm.imgId = vehicleImage.getId().toString();
			        		}
			        		else {
			        			vm.imgId = "/assets/images/no-image.jpg";
			        		}
			    		}
			    		if(info.lastName != null){
			    			vm.name = info.firstName+" "+info.lastName;
			    		}else{
			    			vm.name = info.firstName;
			    		}
			    			
			    		
			    		vm.phone = info.phone;
			    		vm.email = info.email;
			    		vm.howContactedUs = info.contactedFrom;
			    		vm.howFoundUs = info.hearedFrom;
			    		vm.custZipCode = info.custZipCode;
			    		vm.enthicity = info.enthicity;
			    		vm.pdfPath = info.pdfPath;
			    		vm.userRole = user.role;
			    		vm.leadType = "Trade In";
			    		
			    		
			    		if(info.assignedTo == null) {
			    			vm.status = "Unclaimed";
			    		} else {
				    		if(info.assignedTo != null && info.status == null) {
				    			vm.status = "In Progress";
				    		} else {
				    			vm.status = info.status;
				    		}
			    		}
			    		if(info.assignedTo != null) {
			    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
			    		}
			    		vm.requestDate = df.format(info.tradeDate);
			    		if(info.isRead == 0) {
			    			vm.isRead = false;
			    		}
			    		
			    		if(info.isRead == 1) {
			    			vm.isRead = true;
			    		}
			    		infoVMList.add(vm);
			    	}
			    	
			    	for(RequestMoreInfo info: lInfos) {
			    		RequestInfoVM vm = new RequestInfoVM();
			    		vm.id = info.id;
			    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
			    		vm.vin = info.vin;
			    		if(vehicle != null) {
			    			vm.model = vehicle.model;
			    			vm.make = vehicle.make;
			    			vm.stock = vehicle.stock;
			    			vm.year = vehicle.year;
			    			vm.mileage = vehicle.mileage;
			    			vm.price = vehicle.price;
			    			
			    			vm.bodyStyle =vehicle.bodyStyle;
			    			vm.drivetrain = vehicle.drivetrain;
			    			vm.engine = vehicle.engine;
			    			vm.transmission = vehicle.transmission;
			    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
			        		if(vehicleImage!=null) {
			        			vm.imgId = vehicleImage.getId().toString();
			        		}
			        		else {
			        			vm.imgId = "/assets/images/no-image.jpg";
			        		}
			    		}
			    		vm.name = info.name;
			    		vm.phone = info.phone;
			    		vm.email = info.email;
			    		vm.howContactedUs = info.contactedFrom;
			    		vm.howFoundUs = info.hearedFrom;
			    		vm.custZipCode = info.custZipCode;
			    		vm.enthicity = info.enthicity;
			    		vm.requestDate = df.format(info.requestDate);
			    		//vm.leadType = "Request More Info";
			    		
			    		LeadType lType = null;
			    		if(info.isContactusType != null){
				    		if(!info.isContactusType.equals("contactUs")){
				    			lType = LeadType.findById(Long.parseLong(info.isContactusType));
				    		}else{
				    			lType = LeadType.findByName(info.isContactusType);
				    		}
				    		vm.typeOfLead = lType.leadName;
				    		findCustomeData(info.id,vm,lType.id);
			    		}else{
			    			vm.typeOfLead = "Request More Info";
			    			findCustomeData(info.id,vm,1L);
			    		}
			    		
			    		if(info.assignedTo == null) {
			    			vm.status = "Unclaimed";
			    		} else {
				    		if(info.assignedTo != null && info.status == null) {
				    			vm.status = "In Progress";
				    		} else {
				    			vm.status = info.status;
				    		}
			    		}
			    		if(info.assignedTo != null) {
			    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
			    		}
			    		
			    		if(info.isRead == 0) {
			    			vm.isRead = false;
			    		}
			    		
			    		if(info.isRead == 1) {
			    			vm.isRead = true;
			    		}
			    		
			    		infoVMList.add(vm);
			    	}
			    	
			    	Calendar time = Calendar.getInstance();
			    	for(ScheduleTest info: sList) {
			    		RequestInfoVM vm = new RequestInfoVM();
			    		vm.id = info.id;
			    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
			    		vm.vin = info.vin;
			    		if(vehicle != null) {
			    			vm.model = vehicle.model;
			    			vm.make = vehicle.make;
			    			vm.stock = vehicle.stock;
			    			vm.year = vehicle.year;
			    			vm.mileage = vehicle.mileage;
			    			vm.price = vehicle.price;
			    			
			    			vm.bodyStyle =vehicle.bodyStyle;
			    			vm.drivetrain = vehicle.drivetrain;
			    			vm.engine = vehicle.engine;
			    			vm.transmission = vehicle.transmission;
			    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
			        		if(vehicleImage!=null) {
			        			vm.imgId = vehicleImage.getId().toString();
			        		}
			        		else {
			        			vm.imgId = "/assets/images/no-image.jpg";
			        		}
			    		}
			    		vm.name = info.name;
			    		vm.phone = info.phone;
			    		vm.email = info.email;
			    		if(info.bestDay != null){
			    			/*String chaArr[] = info.bestDay.split("-");
			    			vm.bestDay = chaArr[1]+"/"+chaArr[2]+"/"+chaArr[0];*/
			    			vm.bestDay = info.bestDay;
			    		}
			    		vm.bestTime = info.bestTime;
			    		vm.howContactedUs = info.contactedFrom;
			    		vm.howFoundUs = info.hearedFrom;
			    		vm.custZipCode = info.custZipCode;
			    		vm.enthicity = info.enthicity;
			    		vm.leadType = "Schedule Test";
			    		
			    		
			    		if(info.assignedTo == null) {
			    			vm.status = "Unclaimed";
			    		} else {
				    		if(info.assignedTo != null && info.leadStatus == null) {
				    			vm.status = "In Progress";
				    		} else {
				    			vm.status = info.leadStatus;
				    		}
			    		}
			    		if(info.assignedTo != null) {
			    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
			    		}
			    		
			    		if(info.getConfirmDate() != null) {
			    			vm.confirmDate = df.format(info.getConfirmDate());
			    		}
			    		
			    		if(info.getConfirmTime() != null) {
			    			time.setTime(info.getConfirmTime());
			    			String ampm = "";
			    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
			    				ampm = "PM";
			    			} else {
			    				ampm = "AM";
			    			}
			    			vm.confirmTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;
			    		}
			    		if(info.scheduleDate != null){
			    			vm.requestDate = df.format(info.scheduleDate);
			    		}
			    		
			    		if(info.isRead == 0) {
			    			vm.isRead = false;
			    		}
			    		
			    		if(info.isRead == 1) {
			    			vm.isRead = true;
			    		}
			    		infoVMList.add(vm);
			    	}
			    	return ok(Json.toJson(infoVMList));
		    	}	
		    }
		 
		 public static Result releaseLeads(Long id,String leadType) {
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
		    		if(leadType.equals("Schedule Test")) {
		    			ScheduleTest schedule = ScheduleTest.findById(id);
		    			schedule.setPremiumFlag(0);
		    			
		    			schedule.update();
		    			String vin=schedule.vin;
		    			
		    			Location loc=schedule.locations;
		    			String confirmDate=schedule.bestDay;
		    			String confirmTime=schedule.bestTime;
		    			String pref=schedule.preferredContact;
		    			String pdffilePath=null;
		    			scheduleTestReleaseMail(vin,loc,confirmDate,confirmTime,pref,leadType,pdffilePath);
		    		}
					if(leadType.equals("Request More Info")) {
					    RequestMoreInfo info = RequestMoreInfo.findById(id);
					    info.setPremiumFlag(0);
					    info.update();
					    
		                String vin=info.vin;
		    			String pdffilePath=null;
		    			Location loc=info.locations;
		    			String confirmDate=info.bestDay;
		    			String confirmTime=info.bestTime;
		    			String pref=info.preferredContact;
		    			scheduleTestReleaseMail(vin,loc,confirmDate,confirmTime,pref,leadType,pdffilePath);
					}
					if(leadType.equals("Trade In")) {
						TradeIn tradeIn = TradeIn.findById(id);
						tradeIn.setPremiumFlag(0);
						tradeIn.update();
						String pdffilePath=tradeIn.pdfPath;
						String vin=tradeIn.vin;
		    			Location loc=tradeIn.locations;
		    			String confirmDate=tradeIn.bestDay;
		    			String confirmTime=tradeIn.bestTime;
		    			String pref=tradeIn.preferredContact;
		    			scheduleTestReleaseMail(vin,loc,confirmDate,confirmTime,pref,leadType,pdffilePath);
					
					}
		    		return ok();
		    	}
		    }
		 
		 public static void scheduleTestReleaseMail(String vin,Location loc,String confirmDate,String confirmTime,String preferred,String leadType,String pdffilePath){
		    	AuthUser locUser=getLocalUser();
		    	List <AuthUser> userList=AuthUser.findByLocatio(loc);
				InternetAddress[] usersArray = new InternetAddress[userList.size()];
				int index = 0;
				for (AuthUser assi : userList) {
					try {
						
						usersArray[index] = new InternetAddress(assi.getCommunicationemail());
						index++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				MyProfile  user1=MyProfile.findByUser(locUser);
				String email=user1.email;
				EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
				String emailName=details.name;
				String port=details.port;
				String gmail=details.host;
				final	String emailUser=details.username;
				final	String emailPass=details.passward;
				
				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.host", gmail);
				props.put("mail.smtp.port", 587);
				props.put("mail.smtp.starttls.enable", "true");
				Session session = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailUser, emailPass);
					}
				});
				
				try
				{
					Message message = new MimeMessage(session);
		    		
		    		try{
		    			
					message.setFrom(new InternetAddress(emailUser,emailName));
		    		}catch(UnsupportedEncodingException e){
		    			e.printStackTrace();
		    		}
		    		
					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(email));
					message.addRecipients(Message.RecipientType.BCC,usersArray);
					if(leadType.equals("Request More Info")){
						message.setSubject("Request More Info");
					}else if(leadType.equals("Trade In")){
						message.setSubject("Trade In");
					}else{
						message.setSubject("Schedule Test Drive");
					}
					Multipart multipart = new MimeMultipart();
					BodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart = new MimeBodyPart();
					
					VelocityEngine ve = new VelocityEngine();
					ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
					ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
					ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
					ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
					ve.init();
					Template t=null;
					
					if(leadType.equals("Request More Info")){
						 t = ve.getTemplate("/public/emailTemplate/requestMoreInfo_HTML.vm");
						
					}else if(leadType.equals("Trade In")){
						t = ve.getTemplate("/public/emailTemplate/tRADEINAPPRAISAL_HTML.vm");
						
					}
					else{
						 t = ve.getTemplate("/public/emailTemplate/ScheduleTestDrive_HTML.vm");
					}
					 
			        VelocityContext context = new VelocityContext();
			        
			        //context.put("title", vm.name);
			       // context.put("location", loc.getName());
			       // context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
			        Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
			        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
				       
			        int dayOfmonth=1;
			        int month=0;
			        if(confirmDate != null){
			        try {
			        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			        	String dateInString = confirmDate;
			        	String arr[] = dateInString.toString().split("-");
				        if(arr.length >=2){
				        	dayOfmonth = Integer.parseInt(arr[0]);
					        month = Integer.parseInt(arr[1]);
				        }else{
				        	Date date =formatter.parse(confirmDate);
				        	Calendar cal = Calendar.getInstance();
					         cal.setTime((Date)date);
					         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
					         month = cal.get(Calendar.MONTH)+1;
				        }
					} catch (Exception e) {
						e.printStackTrace();
					}
			        String monthName = months[month-1];
			        context.put("part1Date",  dayOfmonth);
			        context.put("part2Date",  monthName);
			        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
			        String time = confirmTime;
			        
			        context.put("bestTime", time);
			        }
//			String dateInString = vm.getBestDay();
			        if(pdffilePath != null){
			        	context.put("pdffilePath", pdffilePath);
			        }
			        
			        context.put("hostnameimg", imageUrlPath);
			        context.put("hostnameUrl", imageUrlPath);
		         if(vehicle.mileage!= null){
			        	
			        	context.put("mileage",vehicle.mileage);
			        	 
			        }
			        else{
			        	context.put("mileage","");
			        }
		         context.put("preferred",  preferred.toUpperCase());
			        context.put("year", vehicle.year);
			        context.put("make", vehicle.make);
			        context.put("model", vehicle.model);
			        context.put("price", "$"+vehicle.price);
			        context.put("stock", vehicle.stock);
			        context.put("vin", vehicle.vin);
			        context.put("make", vehicle.make);
			        context.put("typeofVehicle", vehicle.typeofVehicle);
			        
			        if(leadType.equals("Trade In")){
			        	 context.put("first_name", locUser.firstName);
			        	 context.put("last_name", locUser.lastName);
				        
			        }else{
			        	 context.put("name", locUser.fullName());
			        }
			       
			        context.put("email",locUser.email);
			        if(leadType.equals("Trade In")){
			        	context.put("work_phone",  locUser.phone);
				        
			        }else{
			        	context.put("phone",  locUser.phone);
				        
			        }
			        
			        VehicleImage image = VehicleImage.getDefaultImage(vehicle.vin);
			        if(image!=null) {
			        	context.put("path", image.path);
			        } else {
			        	context.put("path", "");
			        }
			        StringWriter writer = new StringWriter();
			        t.merge( context, writer );
			        String content = writer.toString(); 
					
					messageBodyPart.setContent(content, "text/html");
					multipart.addBodyPart(messageBodyPart);
					message.setContent(multipart);
					Transport.send(message);
					}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		 
		 public static void findCustomeData(Long id,RequestInfoVM vm,Long leadType){
		    	List<CustomizationDataValue> custData = CustomizationDataValue.findByCustomeLead(leadType, id);
		    	List<KeyValueDataVM> keyValueList = new ArrayList<>();
		    	Map<String, String> mapCar = new HashMap<String, String>();
		    	for(CustomizationDataValue custD:custData){
		    		mapCar.put(custD.keyValue, custD.value);
		    		if(custD.displayGrid.equals("true")){
		    			//if(keyValueList.size() == 0){
		    				KeyValueDataVM keyValue = new KeyValueDataVM();
		            		keyValue.key = custD.keyValue;
		            		keyValue.value = custD.value;
		            		keyValue.displayGrid = custD.displayGrid;
		            		keyValueList.add(keyValue);
		    		}
		    		
		    	}
		    	vm.customData = keyValueList;
		    	vm.customMapData = mapCar;
		    }
		 
		 public static Result getAllRequestInfo() {
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
		    		//Long.valueOf(session("USER_LOCATION"))
			    	AuthUser user = (AuthUser) getLocalUser();
			    	List<RequestMoreInfo> listData = new ArrayList<>();
			    	if(user.role == null || user.role.equals("General Manager")) {
		    			listData = RequestMoreInfo.findAllData();
		    		} else {
		    			if(user.role.equals("Manager")) {
		    				listData = RequestMoreInfo.findAllLocationDataManager(Long.valueOf(session("USER_LOCATION")));
		    			} else {
		    				//listData = RequestMoreInfo.findAllByDate();
		    				listData = RequestMoreInfo.findAllLocationData(Long.valueOf(session("USER_LOCATION")));
		    			}
		    		}
			    	List<RequestInfoVM> infoVMList = new ArrayList<>();
			    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			    	for(RequestMoreInfo info: listData) {
			    		RequestInfoVM vm = new RequestInfoVM();
			    		vm.id = info.id;
			    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
			    		vm.vin = info.vin;
			    		if(vehicle != null) {
			    			vm.model = vehicle.model;
			    			vm.make = vehicle.make;
			    			vm.stock = vehicle.stock;
			    			vm.year = vehicle.year;
			    			vm.mileage = vehicle.mileage;
			    			vm.price = vehicle.price;
			    		}
			    		vm.name = info.name;
			    		vm.phone = info.phone;
			    		vm.email = info.email;
			    		vm.howContactedUs = info.contactedFrom;
			    		vm.howFoundUs = info.hearedFrom;
			    		vm.custZipCode = info.custZipCode;
			    		vm.enthicity = info.enthicity;
			    		vm.requestDate = df.format(info.requestDate);
			    		vm.userRole = user.role;
			    		vm.premiumFlagForSale = user.premiumFlag;
			    		if(info.assignedTo == null) {
			    			vm.status = "Unclaimed";
			    		} else {
				    		if(info.assignedTo != null && info.status == null) {
				    			vm.status = "In Progress";
				    		} else {
				    			vm.status = info.status;
				    		}
			    		}
			    		if(info.assignedTo != null) {
			    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
			    		}
			    		
			    		if(info.isRead == 0) {
			    			vm.isRead = false;
			    		}
			    		
			    		if(info.isRead == 1) {
			    			vm.isRead = true;
			    		}
			    		
			    		LeadType lType = null;
			    		if(!info.isContactusType.equals("contactUs")){
			    			lType = LeadType.findById(Long.parseLong(info.isContactusType));
			    		}else{
			    			lType = LeadType.findByName(info.isContactusType);
			    		}
			    		
			    		findCustomeData(info.id,vm,lType.id);
			    		
			    		infoVMList.add(vm);
			    	}
			    	
			    	return ok(Json.toJson(infoVMList));
		    	}	
		    }
		 
		 public static Result getAllScheduleTest() {
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	AuthUser user = (AuthUser) getLocalUser();
			    	List<ScheduleTest> listData = new ArrayList<>();
		    		if(user.role == null || user.role.equals("General Manager")) {
		    			listData = ScheduleTest.findAllData();
		    		} else {
		    			if(user.role.equals("Manager")) {
		    				listData = ScheduleTest.findAllLocationDataManager(Long.valueOf(session("USER_LOCATION")));
		    			} else {
		    				listData = ScheduleTest.findAllLocationData(Long.valueOf(session("USER_LOCATION")));
		    				//listData = ScheduleTest.findAllByLocationDate(Long.valueOf(session("USER_LOCATION")));
		    			}
		    		}
			    	List<RequestInfoVM> infoVMList = new ArrayList<>();
			    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			    	Calendar time = Calendar.getInstance();
			    	for(ScheduleTest info: listData) {
			    		RequestInfoVM vm = new RequestInfoVM();
			    		vm.id = info.id;
			    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
			    		vm.vin = info.vin;
			    		if(vehicle != null) {
			    			vm.model = vehicle.model;
			    			vm.make = vehicle.make;
			    			vm.stock = vehicle.stock;
			    			vm.year = vehicle.year;
			    			vm.mileage = vehicle.mileage;
			    			vm.price = vehicle.price;
			    			vm.bodyStyle =vehicle.bodyStyle;
			    			vm.drivetrain = vehicle.drivetrain;
			    			vm.engine = vehicle.engine;
			    			vm.transmission = vehicle.transmission;
			    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
			        		if(vehicleImage!=null) {
			        			vm.imgId = vehicleImage.getId().toString();
			        		}
			        		else {
			        			vm.imgId = "/assets/images/no-image.jpg";
			        		}
			    		}
			    		vm.name = info.name;
			    		vm.phone = info.phone;
			    		vm.email = info.email;
			    		if(info.bestDay != null){
			    			String chaArr[] = info.bestDay.split("-");
			    			vm.bestDay = chaArr[2]+"-"+chaArr[1]+"-"+chaArr[0];
			    			//vm.bestDay = info.bestDay;
			    		}
			    		vm.bestTime = info.bestTime;
			    		vm.howContactedUs = info.contactedFrom;
			    		vm.howFoundUs = info.hearedFrom;
			    		vm.custZipCode = info.custZipCode;
			    		vm.enthicity = info.enthicity;
			    		vm.userRole = user.role;
			    		vm.premiumFlagForSale = user.premiumFlag;
			    		
			    		if(info.assignedTo == null) {
			    			vm.status = "Unclaimed";
			    		} else {
				    		if(info.assignedTo != null && info.leadStatus == null) {
				    			vm.status = "In Progress";
				    		} else {
				    			vm.status = info.leadStatus;
				    		}
			    		}
			    		if(info.assignedTo != null) {
			    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
			    		}
			    		
			    		if(info.getConfirmDate() != null) {
			    			vm.confirmDate = df.format(info.getConfirmDate());
			    		}
			    		
			    		if(info.getConfirmTime() != null) {
			    			time.setTime(info.getConfirmTime());
			    			String ampm = "";
			    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
			    				ampm = "PM";
			    			} else {
			    				ampm = "AM";
			    			}
			    			vm.confirmTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;
			    		}
			    		if(info.scheduleDate != null){
			    			vm.requestDate = df.format(info.scheduleDate);
			    		}
			    		
			    		if(info.isRead == 0) {
			    			vm.isRead = false;
			    		}
			    		
			    		if(info.isRead == 1) {
			    			vm.isRead = true;
			    		}
			    		
			    		findCustomeData(info.id,vm,2L);
			    		infoVMList.add(vm);
			    	}
			    	
			    	return ok(Json.toJson(infoVMList));
		    	}	
		    }
		 
		 public static Result scheduleTestMarkRead(String flag,Long id) {
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
		    		Date currDate = new Date();
			    	ScheduleTest scheduleObj = ScheduleTest.findById(id);
			    	AuthUser user = (AuthUser) getLocalUser();
			    	if(flag.equals("true")) {
			    		scheduleObj.setIsRead(1);
			    		scheduleObj.setAssignedTo(user);
			    		
			    		UserNotes uNotes = new UserNotes();
			    		uNotes.setNote("Lead has been claimed");
			    		uNotes.setAction("Other");
			    		uNotes.createdDate = currDate;
			    		uNotes.createdTime = currDate;
			    		uNotes.user = user;
		        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    		uNotes.scheduleTest = ScheduleTest.findById(scheduleObj.id);
			    		uNotes.save();
					}
					if(flag.equals("false")) {
						scheduleObj.setIsRead(0);
						scheduleObj.setAssignedTo(null);
					}
					
					scheduleObj.update();
					
					ToDo todo = new ToDo();
					Vehicle vobj = Vehicle.findByVinAndStatus(scheduleObj.vin);
					todo.task = "Follow up with the client about test Drive for "+vobj.make+" "+vobj.model+" ("+vobj.vin+")";
					todo.assignedTo = user;
					todo.assignedBy = user;
					todo.priority = "High";
					todo.status = "Assigned";
					Calendar cal = Calendar.getInstance();
					Date date = new Date();
					cal.setTime(date);
					cal.add(Calendar.DATE, 1);
					todo.dueDate = cal.getTime();
					todo.saveas = 0;
					//todo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					todo.save();
					
					List<ScheduleTest> listData = new ArrayList<>();
					if(user.role == null) {
		    			listData = ScheduleTest.findAllData();
		    		} else {
		    			if(user.role.equals("General Manager")) {
		    				listData = ScheduleTest.findAllData();
		    			} else {
		    				listData = ScheduleTest.findAllByDate();
		    			}
		    		}
			    	List<RequestInfoVM> infoVMList = new ArrayList<>();
			    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			    	for(ScheduleTest info: listData) {
			    		RequestInfoVM vm = new RequestInfoVM();
			    		vm.id = info.id;
			    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
			    		vm.vin = info.vin;
			    		if(vehicle != null) {
			    			vm.model = vehicle.model;
			    			vm.make = vehicle.make;
			    			vm.stock = vehicle.stock;
			    		}
			    		vm.name = info.name;
			    		vm.phone = info.phone;
			    		vm.email = info.email;
			    		if(info.bestDay != null){
			    			String chaArr[] = info.bestDay.split("-");
			    			vm.bestDay = chaArr[2]+"-"+chaArr[1]+"-"+chaArr[0];
			    			//vm.bestDay = info.bestDay;
			    		}
			    		vm.bestTime = info.bestTime;
			    		if(info.scheduleDate != null){
			    			vm.requestDate = df.format(info.scheduleDate);
			    		}
			    		
			    		if(info.assignedTo == null) {
			    			vm.status = "Unclaimed";
			    		} else {
				    		if(info.assignedTo != null && info.leadStatus == null) {
				    			vm.status = "In Progress";
				    		} else {
				    			vm.status = info.leadStatus;
				    		}
			    		}
			    		if(info.assignedTo != null) {
			    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
			    		}
			    		
			    		if(info.isRead == 0) {
			    			vm.isRead = false;
			    		}
			    		
			    		if(info.isRead == 1) {
			    			vm.isRead = true;
			    		}
			    		infoVMList.add(vm);
			    	}
			    	
			    	return ok(Json.toJson(infoVMList));
		    	}	
		    }
		 
		 public static Result saveConfirmData() {
		    	
		    	Date currDate = new Date();
		    	AuthUser user = (AuthUser) getLocalUser();
		    	Form<RequestInfoVM> form = DynamicForm.form(RequestInfoVM.class).bindFromRequest();
		    	RequestInfoVM vm = form.get();
		    	boolean flag = false;
		    	String vin = null;
		    	Date confirmDate = null;
		    	RequestInfoVM listOfString=new RequestInfoVM();
		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
				Date time = null;
				String msg = "success";
				
				try {
					 confirmDate = df.parse(vm.confirmDate);
					 time = parseTime.parse(vm.confirmTime);
					 
						if(vm.option==0) {
							ScheduleTest  scheduleTest = ScheduleTest.findById(vm.id);
							if(scheduleTest != null) {
								
								scheduleTest.setConfirmDate(confirmDate);
								scheduleTest.setConfirmTime(time);
								scheduleTest.setEmail(vm.email);
								vin = scheduleTest.vin;
								listOfString.name=scheduleTest.name;
								
								
								List<ScheduleTest> list = ScheduleTest.findByVin(vm.vin);
			    				for (ScheduleTest info2 : list) {
			    					if(info2.confirmDate != null && info2.confirmTime != null){
			    						if(info2.confirmDate.equals(confirmDate)){
			    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
			    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
			    								msg = "error";
			    							}
			    						}
			    					}
								}
			    				List<RequestMoreInfo> list1 = RequestMoreInfo.findByVin(vm.vin);
			    				for (RequestMoreInfo info2 : list1) {
			    					if(info2.confirmDate != null && info2.confirmTime != null){
			    						if(info2.confirmDate.equals(confirmDate)){
			    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
			    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
			    								msg = "error";
			    							}
			    						}
			    					}
								}
			    				List<TradeIn> list2 = TradeIn.findByVin(vm.vin);
			    				for (TradeIn info2 : list2) {
			    					if(info2.confirmDate != null && info2.confirmTime != null){
			    						if(info2.confirmDate.equals(confirmDate)){
			    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
			    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
			    								msg = "error";
			    							}
			    						}
			    					}
								}
								if(msg.equals("success")){
									flag = true;
									scheduleTest.update();
									
									UserNotes uNotes = new UserNotes();
				    	    		uNotes.setNote("Test Drive Re-scheduled");
				    	    		uNotes.setAction("Other");
				    	    		uNotes.createdDate = currDate;
				    	    		uNotes.createdTime = currDate;
				    	    		uNotes.user = user;
					        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
				    	    		uNotes.scheduleTest = ScheduleTest.findById(scheduleTest.id);
				    	    		uNotes.save();
								}
							}	
						}else if(vm.option == 1) {
							RequestMoreInfo info = RequestMoreInfo.findById(vm.id);
							if(info!=null) {
								
								info.setConfirmDate(confirmDate);
								info.setConfirmTime(time);
								info.setEmail(vm.email);
								vin = info.vin;
								
								List<ScheduleTest> list = ScheduleTest.findByVin(vm.vin);
			    				for (ScheduleTest info2 : list) {
			    					if(info2.confirmDate != null && info2.confirmTime != null){
			    						if(info2.confirmDate.equals(confirmDate)){
			    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
			    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
			    								msg = "error";
			    							}
			    						}
			    					}
								}
			    				List<RequestMoreInfo> list1 = RequestMoreInfo.findByVin(vm.vin);
			    				for (RequestMoreInfo info2 : list1) {
			    					if(info2.confirmDate != null && info2.confirmTime != null){
			    						if(info2.confirmDate.equals(confirmDate)){
			    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
			    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
			    								msg = "error";
			    							}
			    						}
			    					}
								}
			    				List<TradeIn> list2 = TradeIn.findByVin(vm.vin);
			    				for (TradeIn info2 : list2) {
			    					if(info2.confirmDate != null && info2.confirmTime != null){
			    						if(info2.confirmDate.equals(confirmDate)){
			    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
			    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
			    								msg = "error";
			    							}
			    						}
			    					}
								}
								if(msg.equals("success")){
									flag = true;
									info.update();
									
									UserNotes uNotes = new UserNotes();
				    	    		uNotes.setNote("Test Drive Re-scheduled");
				    	    		uNotes.setAction("Other");
				    	    		uNotes.createdDate = currDate;
				    	    		uNotes.createdTime = currDate;
				    	    		uNotes.user = user;
					        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
				    	    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
				    	    		uNotes.save();
								}
								
							}
						}else if(vm.option == 2) {
							TradeIn info = TradeIn.findById(vm.id);
							if(info!=null) {
								
								info.setConfirmDate(confirmDate);
								info.setConfirmTime(time);
								info.setEmail(vm.email);
								vin = info.vin;
								
								List<ScheduleTest> list = ScheduleTest.findByVin(vm.vin);
			    				for (ScheduleTest info2 : list) {
			    					if(info2.confirmDate != null && info2.confirmTime != null){
			    						if(info2.confirmDate.equals(confirmDate)){
			    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
			    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
			    								msg = "error";
			    							}
			    						}
			    					}
								}
			    				List<RequestMoreInfo> list1 = RequestMoreInfo.findByVin(vm.vin);
			    				for (RequestMoreInfo info2 : list1) {
			    					if(info2.confirmDate != null && info2.confirmTime != null){
			    						if(info2.confirmDate.equals(confirmDate)){
			    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
			    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
			    								msg = "error";
			    							}
			    						}
			    					}
								}
			    				List<TradeIn> list2 = TradeIn.findByVin(vm.vin);
			    				for (TradeIn info2 : list2) {
			    					if(info2.confirmDate != null && info2.confirmTime != null){
			    						if(info2.confirmDate.equals(confirmDate)){
			    							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
			    							if((time.after(info2.confirmTime) && time.before(newDate)) || time.equals(info2.confirmTime)){
			    								msg = "error";
			    							}
			    						}
			    					}
								}
								if(msg.equals("success")){
									flag = true;
									info.update();
									
									UserNotes uNotes = new UserNotes();
				    	    		uNotes.setNote("Test Drive Re-scheduled");
				    	    		uNotes.setAction("Other");
				    	    		uNotes.createdDate = currDate;
				    	    		uNotes.createdTime = currDate;
				    	    		uNotes.user = user;
					        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
				    	    		uNotes.tradeIn = TradeIn.findById(info.id);
				    	    		uNotes.save();
								}
								
							}
						}
					
						String userName=null;
						if(flag) {
				    		Map map = new HashMap();
				    		map.put("email",vm.email);
				    		map.put("email",vm.email);
				    		map.put("confirmDate", confirmDate);
				    		map.put("confirmTime", vm.confirmTime);
				    		map.put("CnfDateNature",vm.cnfDateNature);
				    		map.put("vin", vin);
				    		userName=user.firstName+" "+user.lastName;
				    		map.put("uname", user.firstName+" "+user.lastName);
				    		map.put("uphone", user.phone);
				    		map.put("uemail", user.email);
				    		map.put("clientName", listOfString.name);
				    	//	makeToDo(vm.vin);
				    		sendMailForReschedule(map);
				    	}
				} catch (Exception e) {
					e.printStackTrace();
				}
				/*if(vm.option==0) {
					ScheduleTest  scheduleTest = ScheduleTest.findById(vm.id);
					if(scheduleTest != null) {
						flag = true;
						try {
							confirmDate = df.parse(vm.confirmDate);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						scheduleTest.setConfirmDate(confirmDate);
						try {
							scheduleTest.setConfirmTime(parseTime.parse(vm.confirmTime));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						scheduleTest.setEmail(vm.email);
						vin = scheduleTest.vin;
						scheduleTest.update(); 
					}
				} else if(vm.option == 1) {
					RequestMoreInfo info = RequestMoreInfo.findById(vm.id);
					if(info!=null) {
						flag = true;
						try {
							confirmDate = df.parse(vm.confirmDate);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						info.setConfirmDate(confirmDate);
						try {
							info.setConfirmTime(parseTime.parse(vm.confirmTime));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						info.setEmail(vm.email);
						vin = info.vin;
						info.update();
					}
				} else if(vm.option == 2) {
					TradeIn info = TradeIn.findById(vm.id);
					if(info!=null) {
						flag = true;
						try {
							confirmDate = df.parse(vm.confirmDate);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						info.setConfirmDate(confirmDate);
						try {
							info.setConfirmTime(parseTime.parse(vm.confirmTime));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						info.setEmail(vm.email);
						vin = info.vin;
						info.update();
					}
				}
		    	if(flag) {
		    		Map map = new HashMap();
		    		map.put("email",vm.email);
		    		map.put("email",vm.email);
		    		map.put("confirmDate", confirmDate);
		    		map.put("confirmTime", vm.confirmTime);
		    		map.put("vin", vin);
		    		map.put("uname", user.firstName+" "+user.lastName);
		    		map.put("uphone", user.phone);
		    		map.put("uemail", user.email);
		    		makeToDo(vm.vin);
		    		sendMail(map);
		    	}*/
				listOfString.mesg=msg; 
		    	return ok(Json.toJson(listOfString));
		    }
		 
		 public static Result getAllTradeIn() {
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	AuthUser user = (AuthUser) getLocalUser();
			    	List<TradeIn> listData = new ArrayList<>();
			    	if(user.role == null || user.role.equals("General Manager")) {
		    			listData = TradeIn.findAllData();
		    		} else {
		    			if(user.role.equals("Manager")) {
		    				listData = TradeIn.findAllLocationDataManager(Long.valueOf(session("USER_LOCATION")));
		    			} else {
		    				listData = TradeIn.findAllByLocationDate(Long.valueOf(session("USER_LOCATION")));
		    			}
		    		}
			    	List<RequestInfoVM> infoVMList = new ArrayList<>();
			    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			    	for(TradeIn info: listData) {
			    		RequestInfoVM vm = new RequestInfoVM();
			    		vm.id = info.id;
			    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
			    		vm.vin = info.vin;
			    		if(vehicle != null) {
			    			vm.model = vehicle.model;
			    			vm.make = vehicle.make;
			    			vm.stock = vehicle.stock;
			    			vm.year = vehicle.year;
			    			vm.mileage = vehicle.mileage;
			    			vm.price = vehicle.price;
			    			vm.bodyStyle =vehicle.bodyStyle;
			    			vm.drivetrain = vehicle.drivetrain;
			    			vm.engine = vehicle.engine;
			    			vm.transmission = vehicle.transmission;
			    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
			        		if(vehicleImage!=null) {
			        			vm.imgId = vehicleImage.getId().toString();
			        		}
			        		else {
			        			vm.imgId = "/assets/images/no-image.jpg";
			        		}
			    		}
			    		
			    		if(info.lastName != null){
			    			vm.name = info.firstName+" "+info.lastName;
			    		}else{
			    			vm.name = info.firstName;
			    		}
			    		
			    		vm.phone = info.phone;
			    		vm.email = info.email;
			    		vm.howContactedUs = info.contactedFrom;
			    		vm.howFoundUs = info.hearedFrom;
			    		vm.custZipCode = info.custZipCode;
			    		vm.enthicity = info.enthicity;
			    		vm.pdfPath = info.pdfPath;
			    		vm.userRole = user.role;
			    		vm.premiumFlagForSale = user.premiumFlag;
			    		
			    		if(info.assignedTo == null) {
			    			vm.status = "Unclaimed";
			    		} else {
				    		if(info.assignedTo != null && info.status == null) {
				    			vm.status = "In Progress";
				    		} else {
				    			vm.status = info.status;
				    		}
			    		}
			    		if(info.assignedTo != null) {
			    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
			    		}
			    		vm.requestDate = df.format(info.tradeDate);
			    		if(info.isRead == 0) {
			    			vm.isRead = false;
			    		}
			    		
			    		if(info.isRead == 1) {
			    			vm.isRead = true;
			    		}
			    		
			    		findCustomeData(info.id,vm,3L);
			    		infoVMList.add(vm);
			    	}
			    	
			    	return ok(Json.toJson(infoVMList));
		    	}	
		    }
		 
		    private static void sendMailForReschedule(Map map) {
		    	
		    	AuthUser logoUser = getLocalUser();
		    //AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
		    	SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); // findByUser(logoUser);
				
		    	 EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
					String emailName=details.name;
					String port=details.port;
					String gmail=details.host;
					final	String emailUser=details.username;
					final	String emailPass=details.passward;
		    	Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.host", gmail);
				props.put("mail.smtp.port", port);
				props.put("mail.smtp.starttls.enable", "true");
				Session session = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailUser, emailPass);
					}
				});
		    	try
				{
		    		/*InternetAddress[] usersArray = new InternetAddress[2];
		    		int index = 0;
		    		usersArray[0] = new InternetAddress(map.get("email").toString());
		    		usersArray[1] = new InternetAddress(map.get("custEmail").toString());*/
		    		
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress(emailUser));
					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(map.get("email").toString()));
					message.setSubject("Test Drive's Information has been changed");
					Multipart multipart = new MimeMultipart();
					BodyPart messageBodyPart = new MimeBodyPart();
					messageBodyPart = new MimeBodyPart();
					
					VelocityEngine ve = new VelocityEngine();
					ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
					ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
					ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
					ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
					ve.init();
				
					
			        Template t = ve.getTemplate("/public/emailTemplate/testDriveInfoChanged_HTML.vm"); 
			        VelocityContext context = new VelocityContext();
			        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
			       
			        int dayOfmonth=1;
			        int month=0;
			        try {
			        	String arr[] = map.get("confirmDate").toString().split("-");
				        if(arr.length >=2){
				        	dayOfmonth = Integer.parseInt(arr[2]);
					        month = Integer.parseInt(arr[1]);
				        }else{
				        	Calendar cal = Calendar.getInstance();
					         cal.setTime((Date)map.get("confirmDate"));
					         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
					         month = cal.get(Calendar.MONTH)+1;
				        }
					} catch (Exception e) {
						e.printStackTrace();
					}
			        
			        String monthName = months[month-1];
			        context.put("hostnameUrl", imageUrlPath);
			        context.put("siteLogo", logo.logoImagePath);
			        context.put("dayOfmonth", dayOfmonth);
			        context.put("monthName", monthName);
			        context.put("confirmTime", map.get("confirmTime"));
			        
			        Vehicle vehicle = Vehicle.findByVinAndStatus(map.get("vin").toString());
			        context.put("year", vehicle.year);
			        context.put("make", vehicle.make);
			        context.put("model", vehicle.model);
			        context.put("price", "$"+vehicle.price);
			        context.put("stock", vehicle.stock);
			        context.put("vin", vehicle.vin);
			        context.put("make", vehicle.make);
		              if(vehicle.mileage!= null){
			        	
			        	context.put("mileage",vehicle.mileage);
			        	 
			        }
			        else{
			        	context.put("mileage","");
			        }
		              if( map.get("clientName")!= null){
		  	        	
		            	  context.put("clientName", map.get("clientName"));
		  	        	 
		  	        }
		  	        else{
		  	        	 context.put("clientName","");
		  	        }
		             context.put("typeofVehicle", vehicle.typeofVehicle);
			        context.put("name", map.get("uname"));
			        context.put("email", map.get("uemail"));
			        context.put("phone",  map.get("uphone"));
			        String weather= map.get("CnfDateNature").toString();
			        String arr1[] = weather.split("&");
			        String nature=arr1[0];
			        String temp=arr1[1];
			        context.put("nature",nature);
			        context.put("temp", temp);
			        VehicleImage image = VehicleImage.getDefaultImage(vehicle.vin);
			        if(image!=null) {
			        	context.put("defaultImage", image.path);
			        } else {
			        	context.put("defaultImage", "");
			        }
			        StringWriter writer = new StringWriter();
			        t.merge( context, writer );
			        String content = writer.toString(); 
					
					messageBodyPart.setContent(content, "text/html");
					multipart.addBodyPart(messageBodyPart);
					message.setContent(multipart);
					Transport.send(message);
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		    }
		    
		    public static Result tradeInMarkRead(String flag,Long id) {
		    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
		    		Date currDate = new Date();
		    		AuthUser user = (AuthUser) getLocalUser();
			    	TradeIn tradeObjV = TradeIn.findById(id);
			    	if(flag.equals("true")) {
			    		tradeObjV.setIsRead(1);
			    		tradeObjV.setAssignedTo(user);
			    		
			    		UserNotes uNotes = new UserNotes();
			    		uNotes.setNote("Lead has been claimed");
			    		uNotes.setAction("Other");
			    		uNotes.createdDate = currDate;
			    		uNotes.createdTime = currDate;
			    		uNotes.user = user;
		        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			    		uNotes.tradeIn = TradeIn.findById(tradeObjV.id);
			    		uNotes.save();
					}
					if(flag.equals("false")) {
						tradeObjV.setIsRead(0);
						tradeObjV.setAssignedTo(null);
					}
					tradeObjV.update();
					
					List<TradeIn> listData = new ArrayList<>();
		    		if(user.role == null) {
		    			listData = TradeIn.findAllData();
		    		} else {
		    			if(user.role.equals("General Manager")) {
		    				listData = TradeIn.findAllData();
		    			} else {
		    				listData = TradeIn.findAllByDate();
		    			}
		    		}
			    	List<RequestInfoVM> infoVMList = new ArrayList<>();
			    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			    	for(TradeIn info: listData) {
			    		RequestInfoVM vm = new RequestInfoVM();
			    		vm.id = info.id;
			    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
			    		vm.vin = info.vin;
			    		if(vehicle != null) {
			    			vm.model = vehicle.model;
			    			vm.make = vehicle.make;
			    			vm.stock = vehicle.stock;
			    		}
			    		if(info.lastName != null){
			    			vm.name = info.firstName+" "+info.lastName;
			    		}else{
			    			vm.name = info.firstName;
			    		}

			    		vm.phone = info.phone;
			    		vm.email = info.email;
			    		vm.pdfPath = info.pdfPath;
			    		vm.requestDate = df.format(info.tradeDate);
			    		
			    		if(info.assignedTo == null) {
			    			vm.status = "Unclaimed";
			    		} else {
				    		if(info.assignedTo != null && info.status == null) {
				    			vm.status = "In Progress";
				    		} else {
				    			vm.status = info.status;
				    		}
			    		}
			    		if(info.assignedTo != null) {
			    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
			    		}
			    		
			    		if(info.isRead == 0) {
			    			vm.isRead = false;
			    		}
			    		
			    		if(info.isRead == 1) {
			    			vm.isRead = true;
			    		}
			    		infoVMList.add(vm);
			    	}
			    	
			    	return ok(Json.toJson(infoVMList));
		    	}	
		    	
		    }
}