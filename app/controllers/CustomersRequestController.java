package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import models.AuthUser;
import models.LeadType;
import models.Location;
import models.RequestMoreInfo;
import models.UserNotes;
import models.Vehicle;
import models.VehicleImage;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
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
		    			vm.typeOfLead = lType.leadName;
		    			vm.leadId = lType.id;
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

	
}