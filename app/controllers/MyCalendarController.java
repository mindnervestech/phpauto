package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.AddProduct;
import models.AuthUser;
import models.CustomizationForm;
import models.LeadType;
import models.Location;
import models.PhotographerHoursOfOperation;
import models.ProductImages;
import models.RequestMoreInfo;
import models.UserNotes;
import models.Vehicle;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.CreateFormVM;
import viewmodel.HoursOperation;
import viewmodel.NoteVM;
import viewmodel.PortalNameVM;
import viewmodel.RequestInfoVM;
import views.html.home;

import com.fasterxml.jackson.databind.JsonNode;

public class MyCalendarController extends Controller {

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

	
	 public static Result getTimeTableOfPhotos(){
		 AuthUser user = getLocalUser();
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 Map<String,Object> map = new HashMap<>();
		 Map<String, String> mapPortalName = new HashMap<String, String>();
		 List<PhotographerHoursOfOperation> pOfOperation = PhotographerHoursOfOperation.findByListUser(user);
		 List<HoursOperation> hList =  new ArrayList<>();
		 for(PhotographerHoursOfOperation pOperation:pOfOperation){
			 
			 String langValue = mapPortalName.get(pOperation.portalName); 
				if (langValue == null) {
					mapPortalName.put(pOperation.portalName, "red");
				}
			 
			 int stopFlag = 0;
			 Date dt = pOperation.contractDurStartDate;
			 while(stopFlag == 0){
				 
				 if(dt.equals(pOperation.contractDurEndDate)){
					 stopFlag = 1;
				 }
				 Calendar c = Calendar.getInstance(); 
				 c.setTime(dt); 
				 
				 int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				 if(pOperation.sunOpen == 1){
					 if(dayOfWeek == 1){
						 HoursOperation hOperation = new HoursOperation();
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.sunOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.sunCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 if(pOperation.portalName.equals("MavenFurniture")){
							 hOperation.setColor = "red";
						 }else if(pOperation.portalName.equals("AutoDealer")){
							 hOperation.setColor = "green";
						 }else{
							 hOperation.setColor = "red";
						 }
						 hList.add(hOperation);
					 }
				 }
				 if(pOperation.monOpen == 1){
					 if(dayOfWeek == 2){
						 HoursOperation hOperation = new HoursOperation();
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.monOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.monCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 if(pOperation.portalName.equals("MavenFurniture")){
							 hOperation.setColor = "red";
						 }else if(pOperation.portalName.equals("AutoDealer")){
							 hOperation.setColor = "green";
						 }else{
							 hOperation.setColor = "red";
						 }
						 hList.add(hOperation);
					 }
				 }
				 if(pOperation.tueOpen == 1){
					 if(dayOfWeek == 3){
						 HoursOperation hOperation = new HoursOperation();
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.tueOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.tueCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 if(pOperation.portalName.equals("MavenFurniture")){
							 hOperation.setColor = "red";
						 }else if(pOperation.portalName.equals("AutoDealer")){
							 hOperation.setColor = "green";
						 }else{
							 hOperation.setColor = "red";
						 }
						 hList.add(hOperation);
					 }
				 }
				 
				 if(pOperation.wedOpen == 1){
					 if(dayOfWeek == 4){
						 HoursOperation hOperation = new HoursOperation();
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.wedOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.wedCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 if(pOperation.portalName.equals("MavenFurniture")){
							 hOperation.setColor = "red";
						 }else if(pOperation.portalName.equals("AutoDealer")){
							 hOperation.setColor = "green";
						 }else{
							 hOperation.setColor = "red";
						 }
						 hList.add(hOperation);
					 }
				 }
				 
				 if(pOperation.thuOpen == 1){
					 if(dayOfWeek == 5){
						 HoursOperation hOperation = new HoursOperation();
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.thuOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.thuCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 if(pOperation.portalName.equals("MavenFurniture")){
							 hOperation.setColor = "red";
						 }else if(pOperation.portalName.equals("AutoDealer")){
							 hOperation.setColor = "green";
						 }else{
							 hOperation.setColor = "red";
						 }
						 hList.add(hOperation);
					 }
				 }
				 if(pOperation.friOpen == 1){
					 if(dayOfWeek == 6){
						 HoursOperation hOperation = new HoursOperation();
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.friOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.friCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 if(pOperation.portalName.equals("MavenFurniture")){
							 hOperation.setColor = "red";
						 }else if(pOperation.portalName.equals("AutoDealer")){
							 hOperation.setColor = "green";
						 }else{
							 hOperation.setColor = "red";
						 }
						 hList.add(hOperation);
					 }
				 }
				 if(pOperation.satOpen == 1){
					 if(dayOfWeek == 7){
						 HoursOperation hOperation = new HoursOperation();
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.satOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.satCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 if(pOperation.portalName.equals("MavenFurniture")){
							 hOperation.setColor = "red";
						 }else if(pOperation.portalName.equals("AutoDealer")){
							 hOperation.setColor = "green";
						 }else{
							 hOperation.setColor = "red";
						 }
						 hList.add(hOperation);
					 }
				 }
				 
				 
				 c.add(Calendar.DATE, 1);
				 dt = c.getTime();
				 
				 
			 }
			
			 
		 }
		 
		 List<PortalNameVM> pList = new ArrayList<>();
		 for (Map.Entry<String, String> entry : mapPortalName.entrySet())
		 {
			 PortalNameVM pVm = new PortalNameVM();
			 pVm.title = entry.getKey();
			 if(pVm.title.equals("MavenFurniture")){
				 pVm.color = "red";
			 }else if(pVm.title.equals("AutoDealer")){
				 pVm.color = "green";
			 }else{
				 pVm.color = "red";
			 }
			 pList.add(pVm);	 
		 }
		 
		 map.put("calenderTimeTable", hList);
		 map.put("postalNameList", pList);
		 
		 return ok(Json.toJson(map));
	 }
	
	  public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}

	
}