package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.AuthUser;
import models.PhotographerHoursOfOperation;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.HoursOperation;
import viewmodel.PortalNameVM;

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
						 hOperation.id = pOperation.id+"_sun";
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.sunOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.sunCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 hOperation.locationId = pOperation.locations.id;
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
						 hOperation.id = pOperation.id+"_mon";
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.monOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.monCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 hOperation.locationId = pOperation.locations.id;
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
						 hOperation.id = pOperation.id+"_tue";
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.tueOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.tueCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 hOperation.locationId = pOperation.locations.id;
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
						 hOperation.id = pOperation.id+"_wed";
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.wedOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.wedCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.locationId = pOperation.locations.id;
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
						 hOperation.id = pOperation.id+"_thu";
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.thuOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.thuCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.contractDurStartDate = df.format(dt);
						 hOperation.locationId = pOperation.locations.id;
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
						 hOperation.id = pOperation.id+"_fri";
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.friOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.friCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.locationId = pOperation.locations.id;
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
						 hOperation.id = pOperation.id+"_sat";
						 hOperation.openTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.satOpenTime);
						 hOperation.closeTime = new SimpleDateFormat("hh:mm:ss").format(pOperation.satCloseTime);
						 hOperation.portalName = pOperation.portalName;
						 hOperation.contractDurEndDate = df.format(dt);
						 hOperation.locationId = pOperation.locations.id;
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
			 pVm.locationId = 16L;
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
	 
	/* public static Result getEditEventResize(Integer milliseconds ,Integer days,Integer months,String id,String dates){
		
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 DateFormat df1 = new SimpleDateFormat("hh:mm:ss");
		 Date currDate = null;
		 try {
			currDate = df.parse(dates);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		// int milliSc = milliseconds + milliseconds;
		 
		 String arr[] = id.split("_");
		 PhotographerHoursOfOperation pHoursOfOperation = PhotographerHoursOfOperation.findById(Long.parseLong(arr[0]));
		 if(days != 0){
			 Calendar c = Calendar.getInstance(); 
				c.setTime(currDate); 
				c.add(Calendar.DATE, days);
				//Date editDadte = c.getTime();
				Calendar c1 = Calendar.getInstance(); 
				 c1.setTime(c.getTime()); 
				 int dayOfWeek = c1.get(Calendar.DAY_OF_WEEK);
				 
				 if(dayOfWeek == 1){
					 Calendar c2 = Calendar.getInstance(); 
					 Date varDate = null;
					 try {
						varDate = df1.parse("00:00:00");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 c2.add(Calendar.MILLISECOND,milliseconds);
					 pHoursOfOperation.setSatOpenTime(c2.getTime());
				 }
				 
				 if(dayOfWeek == 2){
					 Calendar c2 = Calendar.getInstance();
					 Date varDate = null;
						 try {
							varDate = df1.parse("00:00:00");
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
					 c2.setTime(varDate); 
					 c2.add(Calendar.MILLISECOND,milliseconds);
					 pHoursOfOperation.setMonOpenTime(c2.getTime());
				 }
				pHoursOfOperation.update();
		 }
		if(months != 0){
					 
		}
		if(milliseconds != 0){
			 
		}
		 return ok();
	 }*/
	 
	 public static Result deleteEvent(){
		 Form<HoursOperation> form = DynamicForm.form(HoursOperation.class).bindFromRequest();
 		 HoursOperation vm = form.get();
 		String arr[] = vm.contractDurStartDate.split("T");
 		Date ajDate = null;
		Date setTimes = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance(); 
		System.out.println(vm.portalName);
		PhotographerHoursOfOperation hOperation = PhotographerHoursOfOperation.findByPortalNameAndLocation(vm.portalName, 16L);
		try {
			ajDate = df.parse(arr[0]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 c.setTime(ajDate); 
			 int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			 if(dayOfWeek == 1){
					hOperation.setSunOpenTime(null);
					hOperation.setSunCloseTime(null);
					hOperation.setSunOpen(0);
				 
			 }
			 if(dayOfWeek == 2){
					hOperation.setMonOpenTime(null);
					hOperation.setMonCloseTime(null);
					hOperation.setMonOpen(0);
			 }
			 if(dayOfWeek == 3){
					hOperation.setTueOpenTime(null);
					hOperation.setTueCloseTime(null);
					hOperation.setTueOpen(0);
			 }
			 if(dayOfWeek == 4){
					hOperation.setWedOpenTime(null);
					hOperation.setWedCloseTime(null);
					hOperation.setWedOpen(0);
			 }
			 if(dayOfWeek == 5){
					hOperation.setThuOpenTime(null);
					hOperation.setThuCloseTime(null);
					hOperation.setThuOpen(0);
			 }
			 if(dayOfWeek == 6){
					hOperation.setFriOpenTime(null);
					hOperation.setFriCloseTime(null);
					hOperation.setFriOpen(0);
			 }
			 if(dayOfWeek == 7){
					hOperation.setSatOpenTime(null);
					hOperation.setSatCloseTime(null);
					hOperation.setSatOpen(0);
			 }
			 hOperation.update();
 		 
 		 return ok();
	 }
	 
	 public static Result saveNewEvent(){
			
	    		Form<HoursOperation> form = DynamicForm.form(HoursOperation.class).bindFromRequest();
	    		HoursOperation vm = form.get();
	    		
	    		PhotographerHoursOfOperation hOperation = PhotographerHoursOfOperation.findByPortalNameAndLocation(vm.portalName, 16L);
	    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    		DateFormat dfTime = new SimpleDateFormat("hh:mm:ss");
	    		String arrEndDate[];
	    		String originArr[] = vm.originStartDate .split("T");
	    		String arr[] = vm.contractDurStartDate.split("T");
	    		Date closeTime = null;
	    		if(!vm.contractDurEndDate.equals("0")){
	    			arrEndDate = vm.contractDurEndDate.split("T");
	    			try {
						closeTime = dfTime.parse(arrEndDate[1]);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}else{
	    			try {
						closeTime = dfTime.parse(arr[1]);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    		
	    		
	    		Date ajDate = null;
	    		Date setTimes = null;
	    		try {
					ajDate = df.parse(arr[0]);
					setTimes = dfTime.parse(arr[1]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		Calendar c = Calendar.getInstance(); 
				 c.setTime(ajDate); 
				 int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				 if(dayOfWeek == 1){
						hOperation.setSunOpenTime(setTimes);
						hOperation.setSunCloseTime(closeTime);
						hOperation.setSunOpen(1);
					 
				 }
				 if(dayOfWeek == 2){
						hOperation.setMonOpenTime(setTimes);
						hOperation.setMonCloseTime(closeTime);
						hOperation.setMonOpen(1);
				 }
				 if(dayOfWeek == 3){
						hOperation.setTueOpenTime(setTimes);
						hOperation.setTueCloseTime(closeTime);
						hOperation.setTueOpen(1);
				 }
				 if(dayOfWeek == 4){
						hOperation.setWedOpenTime(setTimes);
						hOperation.setWedCloseTime(closeTime);
						hOperation.setWedOpen(1);
				 }
				 if(dayOfWeek == 5){
						hOperation.setThuOpenTime(setTimes);
						hOperation.setThuCloseTime(closeTime);
						hOperation.setThuOpen(1);
				 }
				 if(dayOfWeek == 6){
						hOperation.setFriOpenTime(setTimes);
						hOperation.setFriCloseTime(closeTime);
						hOperation.setFriOpen(1);
				 }
				 if(dayOfWeek == 7){
						hOperation.setSatOpenTime(setTimes);
						hOperation.setSatCloseTime(closeTime);
						hOperation.setSatOpen(1);
				 }
				 hOperation.update();
				 
				 
				 if(!originArr[0].equals("0")){
					 try {
							ajDate = df.parse(originArr[0]);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 c.setTime(ajDate); 
						 dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
						 if(dayOfWeek == 1){
								hOperation.setSunOpenTime(null);
								hOperation.setSunCloseTime(null);
								hOperation.setSunOpen(0);
							 
						 }
						 if(dayOfWeek == 2){
								hOperation.setMonOpenTime(null);
								hOperation.setMonCloseTime(null);
								hOperation.setMonOpen(0);
						 }
						 if(dayOfWeek == 3){
								hOperation.setTueOpenTime(null);
								hOperation.setTueCloseTime(null);
								hOperation.setTueOpen(0);
						 }
						 if(dayOfWeek == 4){
								hOperation.setWedOpenTime(null);
								hOperation.setWedCloseTime(null);
								hOperation.setWedOpen(0);
						 }
						 if(dayOfWeek == 5){
								hOperation.setThuOpenTime(null);
								hOperation.setThuCloseTime(null);
								hOperation.setThuOpen(0);
						 }
						 if(dayOfWeek == 6){
								hOperation.setFriOpenTime(null);
								hOperation.setFriCloseTime(null);
								hOperation.setFriOpen(0);
						 }
						 if(dayOfWeek == 7){
								hOperation.setSatOpenTime(null);
								hOperation.setSatCloseTime(null);
								hOperation.setSatOpen(0);
						 }
						 hOperation.update();
				 }
				 
		    		
	    		return ok();
	    		
	    		
	    		//String sDate = arr[2]+"-"+arr[1]+"-"+arr[3];
	    		//try {
					//System.out.println(arr[0]);
				/*} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	    		/*String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(Long.parseLong(vm.openTime)),
	    	            TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(vm.openTime)) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(Long.parseLong(vm.openTime))),
	    	            TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(vm.openTime)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(vm.openTime))));*/
	    		//System.out.println(hms);
	   	} 		
	 
	
	  public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}

	
}