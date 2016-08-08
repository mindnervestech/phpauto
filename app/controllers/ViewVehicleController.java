package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.json.JSONArray;

import au.com.bytecode.opencsv.CSVWriter;

import com.avaje.ebean.SqlRow;

import models.AuthUser;
import models.ClickyVisitorsList;
import models.Contacts;
import models.EmailDetails;
import models.FollowBrand;
import models.Location;
import models.MyProfile;
import models.PriceAlert;
import models.PriceChange;
import models.RequestMoreInfo;
import models.ScheduleTest;
import models.SiteLogo;
import models.SoldContact;
import models.TradeIn;
import models.UserNotes;
import models.Vehicle;
import models.VehicleImage;
import models.VirtualTour;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import securesocial.core.Identity;

import viewmodel.SoldContactVM;
import viewmodel.SpecificationVM;
import viewmodel.UserNoteVM;
import viewmodel.VehicleVM;
import views.html.home;

public class ViewVehicleController extends Controller{

	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	
	final static String pdfRootDir = Play.application().configuration()
			.getString("pdfRootDir");
	
	final static String userRegistration = Play.application().configuration()
			.getString("userRegistration");
	
	final static String imageUrlPath = Play.application().configuration()
			.getString("image.url.path");
	
	final static String vehicleUrlPath = Play.application().configuration()
			.getString("vehicle.url.path");
	
	final static String mashapeKey = Play.application().configuration()
			.getString("mashapeKey");
			
	final static String emailUsername = Play.application().configuration()
			.getString("mail.username");
	
	final static String emailPassword = Play.application().configuration()
			.getString("mail.password");
	
	private static boolean simulate = false;
	
	
	public static Result updateVehicle(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		int flag=0;
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		Date currDate = new Date();
    		AuthUser user = getLocalUser();
	    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
	    	SpecificationVM vm = form.get();
	    	Vehicle vehicle = Vehicle.findById(vm.id);
	    	if(vehicle != null) {
	    		
	    		String databasevalue=vehicle.price.toString();
	    		String vmvalue= vm.price.toString();
	    		if(!vmvalue.equals(databasevalue)) {
	    			
	    				List<PriceAlert> alertList = PriceAlert.getEmailsByVin(vehicle.vin, Long.valueOf(session("USER_LOCATION")));
	    				for(PriceAlert priceAlert: alertList) {
	    					priceAlert.setSendEmail("Y");
	    					priceAlert.setOldPrice(vehicle.price);
	    					priceAlert.setCurrDate(currDate);
	    					priceAlert.update();
	    				}
	    				Date crDate = new Date();
	    				PriceChange change = new PriceChange();
	    				change.dateTime = crDate;
	    				change.price = vm.price.toString();
	    				change.person = user.firstName +" "+user.lastName;
	    				change.vin = vm.vin;
	    				change.save();
	    				flag=1;
	    			//	sendPriceAlertMail(vehicle.vin);
	    		}
	    		vehicle.setTitle(vm.title);
	    		vehicle.setMake(vm.make);
	    		vehicle.setModel(vm.model);
	    		vehicle.setExteriorColor(vm.extColor);
	    		vehicle.setCityMileage(vm.city_mileage);
	    		vehicle.setHighwayMileage(vm.highway_mileage);
		    	vehicle.setStock(vm.stock);
		    	vehicle.setPrice(vm.price);
		    	vehicle.setBodyStyle(vm.bodyStyle);
		    	vehicle.setMileage(vm.mileage);
		    	
		    	vehicle.update();
		    	if(flag==1){
		    		sendPriceAlertMail(vehicle.vin);
		    	}
		    //	sendPriceAlertMail(vehicle.vin);
	    	}
	    	return ok();
    	}	
    }
	
public static void sendPriceAlertMail(String vin) {
		
		AuthUser user = getLocalUser();
		//AuthUser user = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
		//MyProfile profile = MyProfile.findByLocation(Long.valueOf(session("USER_LOCATION")));  //findByUser(user);
		
		AuthUser aUser = AuthUser.getlocationAndManagerByType(Location.findById(Long.valueOf(session("USER_LOCATION"))), "Manager");
    	MyProfile profile = MyProfile.findByUser(aUser);
		
		AuthUser logoUser = getLocalUser();
		//AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
 	    SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); //findByUser(logoUser);
		
		List<PriceAlert> priceAlertList = PriceAlert.getEmailsByStatus(user);
		List<PriceAlert> priceAlerts = PriceAlert.getEmailsByStatusVin(vin);
		for(PriceAlert alert: priceAlerts) {
			
			Vehicle vehicle = Vehicle.findByVinAndStatus(alert.vin);
			List<Vehicle> sameBodyList = Vehicle.getRandom(vehicle.vin);
			
			Vehicle sameBodyStyle = sameBodyList.get(0);
			VehicleImage sameBodyStyleDefault = VehicleImage.getDefaultImage(sameBodyStyle.vin);
			
			Vehicle sameEngine = sameBodyList.get(1);
			VehicleImage sameEngineDefault = VehicleImage.getDefaultImage(sameEngine.vin);
			
			Vehicle sameMake =  sameBodyList.get(2);
			VehicleImage sameMakeDefault = VehicleImage.getDefaultImage(sameMake.vin);
			
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
				Message message = new MimeMessage(session);
	    		try{
				message.setFrom(new InternetAddress(emailUser,emailName));
	    		}
	    		catch(UnsupportedEncodingException e){
	    			e.printStackTrace();
	    		}
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(alert.email));
				message.setSubject("VEHICLE PRICE CHANGE ALERT");
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart = new MimeBodyPart();
				
				VelocityEngine ve = new VelocityEngine();
				ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
				ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
				ve.init();
			
				
		        Template t = ve.getTemplate("/public/emailTemplate/priceAlert_HTML.vm"); 
		        VelocityContext context = new VelocityContext();
		        
		        context.put("hostnameUrl", imageUrlPath);
		        context.put("siteLogo", logo.logoImagePath);
		        
		        context.put("year", vehicle.year);
		        context.put("make", vehicle.make);
		        context.put("model", vehicle.model);
		        context.put("oldPrice", "$"+alert.oldPrice);
		        context.put("newPrice", "$"+vehicle.price);
		        context.put("bodyStyle", vehicle.bodyStyle);
		        context.put("mileage", vehicle.mileage);
		        
		        if(vehicle.doors != null) {
		        	context.put("doors", vehicle.doors);
		        	} else {
		        		context.put("doors", "");
		        	}
		        
		        
		        if(vehicle.standardSeating != null) {
		        	context.put("seats", vehicle.standardSeating);
		        	} else {
		        		context.put("seats", "" );
		        	}
		        
		        if(vehicle.drivetrain != null) {
		        	context.put("driveTrain", vehicle.drivetrain);
		        	} else {
		        		context.put("driveTrain", "");
		        	}
		        
		        if(vehicle.engine != null) {
		        	context.put("engine", vehicle.engine);
		        	} else {
		        		context.put("engine", "");
		        	}
		        
		        
		        if(vehicle.transmission != null) {
		        	 context.put("transmission", vehicle.transmission);
		        	} else {
		        		 context.put("transmission", "");
		        	}
		        
		        if(vehicle.brakes != null) {
		        	context.put("brakes", vehicle.brakes);
		        	} else {
		        		context.put("brakes", "");
		        	}
		        
		        
		        if(vehicle.horsePower != null) {
		        	context.put("horsePower", vehicle.horsePower);
		        	} else {
		        		context.put("horsePower", "");
		        	}
		        
		        context.put("email", profile.email);
		        String firstThreeDigit=profile.phone;
		        firstThreeDigit=firstThreeDigit.substring(0, 3);
		        String secondThreeDigit=profile.phone;
		        secondThreeDigit=secondThreeDigit.substring(3, 6);
		        String thirdThreeDigit=profile.phone;
		        thirdThreeDigit=thirdThreeDigit.substring(6, 10);
		        context.put("firstThreeDigit", firstThreeDigit);
		        context.put("secondThreeDigit", secondThreeDigit);
		        context.put("thirdThreeDigit", thirdThreeDigit);
		        
		        context.put("phone", profile.phone);
		        if(sameBodyStyle != null) {
		        	if(sameBodyStyle.price != null) {
		        		context.put("bodyStylePrice", "$"+sameBodyStyle.price.toString());
		        	} else {
		        		context.put("bodyStylePrice", "");
		        	}
		        	context.put("bodyStyleVin", sameBodyStyle.vin);
		        	context.put("bodyStyleYear", sameBodyStyle.year);
		        	context.put("bodyStyleMake", sameBodyStyle.make);
		        	context.put("bodyStyleModel", sameBodyStyle.model);
		        } else {
		        	context.put("bodyStylePrice", "");
		        	context.put("bodyStyleVin", "");
		        }
		        if(sameEngine != null) {
		        	if(sameEngine.price != null) {
		        		context.put("enginePrice", "$"+sameEngine.price.toString());
		        	} else {
		        		context.put("enginePrice", "");
		        	}
		        	context.put("engineVin", sameEngine.vin);
		        	context.put("engineMake", sameEngine.make);
		        	context.put("engineYear", sameEngine.year);
		        	context.put("engineModel", sameEngine.model);
		        } else {
		        	context.put("enginePrice","");
		        	context.put("engineVin", "");
		        }
		        if(sameMake != null) {
		        	if(sameMake.price != null) {
		        		context.put("makePrice", "$"+sameMake.price.toString());
		        	} else {
		        		context.put("makePrice", "");
		        	}
		        	context.put("makeVin", sameMake.vin);
		        	context.put("sameMake", sameMake.make);
		        	context.put("sameModel", sameMake.model);
		        	context.put("sameYear", sameMake.year);
		        } else {
		        	context.put("makePrice", "");
		        	context.put("makeVin", "");
		        }
		        
		        if(sameBodyStyleDefault != null) {
		        	context.put("sameBodyStyleDefault", sameBodyStyleDefault.thumbPath);
		        } else {
		        	context.put("sameBodyStyleDefault", "/no-image.jpg");
		        }
		        if(sameEngineDefault != null) {
		        	context.put("sameEngineDefault", sameEngineDefault.thumbPath);
		        } else {
		        	context.put("sameEngineDefault", "/no-image.jpg");
		        }
		        if(sameMakeDefault != null) {
		        	context.put("sameMakeDefault", sameMakeDefault.thumbPath);
		        } else {
		        	context.put("sameMakeDefault", "/no-image.jpg");
		        }
		        VehicleImage image = VehicleImage.getDefaultImage(vehicle.vin);
		        if(image != null) {
		        	context.put("defaultImage", image.path);
		        } else {
		        	context.put("defaultImage", "/no-image.jpg");
		        }
		        StringWriter writer = new StringWriter();
		        t.merge( context, writer );
		        String content = writer.toString();
				
				messageBodyPart.setContent(content, "text/html");
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
				Transport.send(message);
				alert.setSendEmail("N");
				alert.update();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			} 
		}	
	}

		public static AuthUser getLocalUser() {
			String id = session("USER_KEY");
			AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
			//AuthUser user = getLocalUser();
			return user;
		}
		
		public static Result getVehicleHistory(String vin){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    		List<UserNoteVM> userNote = new ArrayList<>();
	    		List<RequestMoreInfo> infoList = RequestMoreInfo.findByVin(vin);
	    		List<ScheduleTest>  scheduleList = ScheduleTest.findByVin(vin);
	    		for (ScheduleTest scheduleTest : scheduleList) {
	    			List<UserNotes> notes = UserNotes.findScheduleTest(scheduleTest);
					for (UserNotes note : notes) {
						UserNoteVM vm = new UserNoteVM();
						vm.id = note.id;
						vm.action = note.action;
						vm.note = note.note;
						vm.createdDate = df.format(note.createdDate);
						vm.createdTime = time.format(note.createdTime);
						userNote.add(vm);
					}
				}
	    		List<TradeIn> traid = TradeIn.findByVin(vin);
	    		for (TradeIn tradeIn : traid) {
	    			List<UserNotes> notes = UserNotes.findTradeIn(tradeIn);
					for (UserNotes note : notes) {
						UserNoteVM vm = new UserNoteVM();
						vm.id = note.id;
						vm.action = note.action;
						vm.note = note.note;
						vm.createdDate = df.format(note.createdDate);
						vm.createdTime = time.format(note.createdTime);
						userNote.add(vm);
					}
				}
	    		for (RequestMoreInfo info : infoList) {
					List<UserNotes> notes = UserNotes.findRequestMore(info);
					for (UserNotes note : notes) {
						UserNoteVM vm = new UserNoteVM();
						vm.id = note.id;
						vm.action = note.action;
						vm.note = note.note;
						vm.createdDate = df.format(note.createdDate);
						vm.createdTime = time.format(note.createdTime);
						userNote.add(vm);
					}
				}
	    		return ok(Json.toJson(userNote));
	    	}
		}
		
		public static Result getGoTodraft(Long id){
			
			Vehicle vehicle= Vehicle.findById(id);
			if(vehicle != null){
				vehicle.setPublicStatus("draft");
				vehicle.update();
			}
			return ok();
			
		}
		
		public static Result getAllVehiclesByType(String type) {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		int visitorCount = 0;
	    		List <Vehicle> vehicleObjList;
		    	/*List <Vehicle> vehicleObjList = Vehicle.getVehiclesByStatus("Newly Arrived");*/
	    		if(type.equalsIgnoreCase("All"))
	    			vehicleObjList = Vehicle.findByNewArrAndLocation(Long.valueOf(session("USER_LOCATION")));
	    		else{
	    			vehicleObjList = Vehicle.findByNewArrAndLocationType(Long.valueOf(session("USER_LOCATION")), type);
	    		}
	    		
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	ArrayList<SpecificationVM> NewVMs = new ArrayList<>();
		    	String params = "&date=last-28-days&type=visitors-list&limit=all";
		     	for(Vehicle vm : vehicleObjList){
		     		
		     		VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
		     		
		     		SpecificationVM vehicle = new SpecificationVM();
		     		vehicle.id = vm.id;
		     	    vehicle.title=vm.getTitle();
			    	vehicle.category = vm.category;
			    	vehicle.vin = vm.vin;
			    	vehicle.year = vm.year;
			    	vehicle.make = vm.make;
			    	vehicle.model = vm.model;
			    	vehicle.trim_level = vm.trim;
			    	vehicle.label = vm.label;
			    	vehicle.stock = vm.stock;
			    	vehicle.mileage = vm.mileage;
			    	vehicle.cost = vm.cost;
			    	vehicle.price = vm.price;
			    	vehicle.extColor = vm.exteriorColor;
			    	vehicle.intColor = vm.interiorColor;
			    	vehicle.colorDesc = vm.colorDescription;
			    	vehicle.doors = vm.doors;
			    	vehicle.stereo = vm.stereo;
			    	vehicle.engine = vm.engine;
			    	vehicle.fuel = vm.fuel;
			    	vehicle.city_mileage = vm.cityMileage;
			    	vehicle.highway_mileage = vm.highwayMileage;
			    	vehicle.bodyStyle = vm.bodyStyle;
			    	vehicle.drivetrain = vm.drivetrain;
			    	vehicle.transmission = vm.transmission;
			    	vehicle.location = vm.location;
			    	vehicle.status  =  vm.status;
			    	if(vehicleImg != null){
			    		vehicle.imagePath = vehicleImg.thumbPath;
			    		vehicle.imgId = vehicleImg.id;
			    	}	
			    	
			    	vehicle.sold = false;
			    	visitorCount = 0;
			    	
	        		try {
	    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
	    				for(int j=0;j<jsonArray.length();j++){
	    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
	    	    			String arr[] = data.split("/");
	    	    			if(arr.length > 5){
	    	    			  if(arr[5] != null){
	    	    				  if(arr[5].equals(vm.vin)){
	    	    					  visitorCount = visitorCount + 1;
	    	    				  }
	    	    			  }
	    	    			}
	    				}	
	    				
	    			} catch (Exception e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
			    	
	        		vehicle.pageViewCount = visitorCount;
			    	
			    	List<SqlRow> rows = Vehicle.getDriveTimeAndName(vehicle.vin);
			    	for(SqlRow row : rows) {
			    		Date date = (Date) row.get("confirm_date");
			    		Date timeObj = (Date) row.get("confirm_time");
			    		vehicle.testDrive = df.format(date) +" ";
			    		Calendar time = Calendar.getInstance();
			    		if(timeObj != null){
			    			time.setTime(timeObj);
			    		}
			    		
		    			String ampm = "";
		    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
		    				ampm = "PM";
		    			} else {
		    				ampm = "AM";
		    			}
		    			vehicle.testDrive = vehicle.testDrive + time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;
			    		Integer userId = (Integer) row.get("assigned_to_id");
			    		AuthUser userData = AuthUser.findById(userId);
			    		vehicle.salesRep = userData.firstName +" "+userData.lastName;
			    		break;
			    	}
			    	vehicle.vehicleCnt = VehicleImage.getVehicleImageCountByVIN(vm.vin);
			    	NewVMs.add(vehicle);
		    	}
		     	
		     	return ok(Json.toJson(NewVMs));
	    	}	
	    }
		
		private static String callClickAPI(String params) {
	    	StringBuffer response = new StringBuffer();
	    	try {
	    		String url = "https://api.clicky.com/api/stats/4?output=json&site_id=100875513&sitekey=d6e7550038b4a34c"+params;
			
	    		URL obj = new URL(url);
	    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    		con.setRequestMethod("GET");
	    		con.setRequestProperty("User-Agent", USER_AGENT);
	    		BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
	    		String inputLine;
	    		while ((inputLine = in.readLine()) != null) {
	    			response.append(inputLine);
	    		}
	    		in.close();
	    	} catch(Exception e) {}
	    	return response.toString();
	    }
		
		public static Result getAllSoldVehiclesByType(String type) {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	List <Vehicle> soldVehicleObjList;
		    	if(type.equalsIgnoreCase("All"))
		    		soldVehicleObjList = Vehicle.getVehiclesByStatusAndLocation("Sold",Long.valueOf(session("USER_LOCATION")));
		    	else{
		    		soldVehicleObjList = Vehicle.getVehiclesByStatusAndLocationType("Sold",Long.valueOf(session("USER_LOCATION")),type);
		    	}
		    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		    	ArrayList<SpecificationVM> soldVMs = new ArrayList<>(); 
		     	for(Vehicle vm : soldVehicleObjList){
		     		VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
		     		SpecificationVM vehicle = new SpecificationVM();
		     		vehicle.id = vm.id;
			    	vehicle.category = vm.category;
			    	vehicle.vin = vm.vin;
			    	vehicle.year = vm.year;
			    	//vehicle.make = vm.make+" "+vm.model;
			    	vehicle.make = vm.make;
			    	vehicle.model = vm.model;
			    	vehicle.trim_level = vm.trim;
			    	vehicle.label = vm.label;
			    	vehicle.stock = vm.stock;
			    	vehicle.mileage = vm.mileage;
			    	vehicle.cost = vm.cost;
			    	vehicle.price = vm.price;
			    	int len=vehicle.vin.length();
			    	vehicle.last4Vin=vehicle.vin.substring(Math.max(len - 4, 0));
			    	vehicle.extColor = vm.exteriorColor;
			    	vehicle.intColor = vm.interiorColor;
			    	vehicle.colorDesc = vm.colorDescription;
			    	vehicle.doors = vm.doors;
			    	vehicle.stereo = vm.stereo;
			    	vehicle.engine = vm.engine;
			    	vehicle.fuel = vm.fuel;
			    	vehicle.city_mileage = vm.cityMileage;
			    	vehicle.highway_mileage = vm.highwayMileage;
			    	vehicle.bodyStyle = vm.bodyStyle;
			    	vehicle.drivetrain = vm.drivetrain;
			    	vehicle.transmission = vm.transmission;
			    	vehicle.location = vm.location;
			    	vehicle.status  =  vm.status;
			    	vehicle.vehicleCnt = VehicleImage.getVehicleImageCountByVIN(vm.vin);
			    	vehicle.sold = true;
			    	vehicle.imagePath = vehicleImg.thumbPath;
			    	vehicle.imgId = vehicleImg.id;
			    	vehicle.testDrive = df.format(vm.soldDate);
			    	vehicle.title = vm.title;
			    	soldVMs.add(vehicle);
		    	}
		     	
		     	return ok(Json.toJson(soldVMs));
	    	}	
	    }
		
        public static Result getAllVehicles(Long locationId) {
        	
		int visitorCount = 0;
		
		List <Vehicle> vehicleObjList = Vehicle.findByNewArrAndLocationNoDraft(locationId);
		
    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    	ArrayList<SpecificationVM> NewVMs = new ArrayList<>();
    	//String params = "&date=last-28-days&type=visitors-list&limit=all";
     	for(Vehicle vm : vehicleObjList){
     		int len=0;
     		VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
     		
     		SpecificationVM vehicle = new SpecificationVM();
     		vehicle.id = vm.id;
     	    vehicle.title=vm.getTitle();
	    	vehicle.category = vm.category;
	    	vehicle.vin = vm.vin;
	    	len=vehicle.vin.length();
	    	//vehicle.last4Vin=vehicle.vin.charAt(len-3)+""+vehicle.vin.charAt(len-2)+""+vehicle.vin.charAt(len-1)+""+vehicle.vin.charAt(len);
	    	vehicle.last4Vin=vehicle.vin.substring(Math.max(len - 4, 0));
	    	vehicle.year = vm.year;
	    	vehicle.make = vm.make;
	    	vehicle.model = vm.model;
	    	vehicle.trim_level = vm.trim;
	    	vehicle.label = vm.label;
	    	vehicle.stock = vm.stock;
	    	vehicle.mileage = vm.mileage;
	    	vehicle.cost = vm.cost;
	    	vehicle.price = vm.price;
	    	vehicle.extColor = vm.exteriorColor;
	    	vehicle.intColor = vm.interiorColor;
	    	vehicle.colorDesc = vm.colorDescription;
	    	vehicle.doors = vm.doors;
	    	vehicle.stereo = vm.stereo;
	    	vehicle.engine = vm.engine;
	    	vehicle.fuel = vm.fuel;
	    	vehicle.city_mileage = vm.cityMileage;
	    	vehicle.highway_mileage = vm.highwayMileage;
	    	vehicle.bodyStyle = vm.bodyStyle;
	    	vehicle.drivetrain = vm.drivetrain;
	    	vehicle.transmission = vm.transmission;
	    	vehicle.location = vm.location;
	    	vehicle.status  =  vm.status;
	    	if(vehicleImg != null){
	    		vehicle.imagePath = vehicleImg.thumbPath;
	    		vehicle.imgId = vehicleImg.id;
	    	}	
	    	
	    	vehicle.sold = false;
	    	visitorCount = 0;
	    	
	    	List<ClickyVisitorsList> cList = ClickyVisitorsList.getfindAll();
	    	for(ClickyVisitorsList clickData:cList){
	    		String data = clickData.landingPage;
	    		String arr[] = data.split("/");
    			if(arr.length > 5){
    			  if(arr[5] != null){
    				  if(arr[5].equals(vm.vin)){
    					  visitorCount = visitorCount + 1;
    				  }
    			  }
    			}
	    	}
    		/*try {
				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
				for(int j=0;j<jsonArray.length();j++){
	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
	    			String arr[] = data.split("/");
	    			if(arr.length > 5){
	    			  if(arr[5] != null){
	    				  if(arr[5].equals(vm.vin)){
	    					  visitorCount = visitorCount + 1;
	    				  }
	    			  }
	    			}
				}	
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	    	
    		vehicle.pageViewCount = visitorCount;
	    	
	    	List<SqlRow> rows = Vehicle.getDriveTimeAndName(vehicle.vin);
	    	for(SqlRow row : rows) {
	    		Date date = (Date) row.get("confirm_date");
	    		Date timeObj = (Date) row.get("confirm_time");
	    		vehicle.testDrive = df.format(date) +" ";
	    		Calendar time = Calendar.getInstance();
	    		if(timeObj != null){
	    			time.setTime(timeObj);
	    		}
	    		
    			String ampm = "";
    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
    				ampm = "PM";
    			} else {
    				ampm = "AM";
    			}
    			vehicle.testDrive = vehicle.testDrive + time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;
	    		Integer userId = (Integer) row.get("assigned_to_id");
	    		AuthUser userData = AuthUser.findById(userId);
	    		vehicle.salesRep = userData.firstName +" "+userData.lastName;
	    		break;
	    	}
	    	vehicle.vehicleCnt = VehicleImage.getVehicleImageCountByVIN(vm.vin);
	    	NewVMs.add(vehicle);
     	}
     	
    	return ok(Json.toJson(NewVMs));
}
        
        public static Result getAllSoldVehicles() {
    		if(session("USER_KEY") == null || session("USER_KEY") == "") {
        		return ok(home.render("",userRegistration));
        	} else {
    	    	List <Vehicle> soldVehicleObjList = Vehicle.getVehiclesByStatusAndLocation("Sold",Long.valueOf(session("USER_LOCATION")));
    	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    	    	ArrayList<SpecificationVM> soldVMs = new ArrayList<>(); 
    	     	for(Vehicle vm : soldVehicleObjList){
    	     		VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
    	     		SpecificationVM vehicle = new SpecificationVM();
    	     		vehicle.id = vm.id;
    		    	vehicle.category = vm.category;
    		    	vehicle.vin = vm.vin;
    		    	int len=vehicle.vin.length();
    		    	vehicle.last4Vin=vehicle.vin.substring(Math.max(len - 4, 0));
    		    	vehicle.year = vm.year;
    		    	//vehicle.make = vm.make+" "+vm.model;
    		    	vehicle.make = vm.make;
    		    	vehicle.model = vm.model;
    		    	vehicle.trim_level = vm.trim;
    		    	vehicle.label = vm.label;
    		    	vehicle.stock = vm.stock;
    		    	vehicle.mileage = vm.mileage;
    		    	vehicle.cost = vm.cost;
    		    	vehicle.price = vm.price;
    		    	vehicle.extColor = vm.exteriorColor;
    		    	vehicle.intColor = vm.interiorColor;
    		    	vehicle.colorDesc = vm.colorDescription;
    		    	vehicle.doors = vm.doors;
    		    	vehicle.stereo = vm.stereo;
    		    	vehicle.engine = vm.engine;
    		    	vehicle.fuel = vm.fuel;
    		    	vehicle.city_mileage = vm.cityMileage;
    		    	vehicle.highway_mileage = vm.highwayMileage;
    		    	vehicle.bodyStyle = vm.bodyStyle;
    		    	vehicle.drivetrain = vm.drivetrain;
    		    	vehicle.transmission = vm.transmission;
    		    	vehicle.location = vm.location;
    		    	vehicle.status  =  vm.status;
    		    	vehicle.vehicleCnt = VehicleImage.getVehicleImageCountByVIN(vm.vin);
    		    	vehicle.sold = true;
    		    	if(vehicleImg != null){
    		    		vehicle.imagePath = vehicleImg.thumbPath;
    			    	vehicle.imgId = vehicleImg.id;
    		    	}
    		    	
    		    	vehicle.testDrive = df.format(vm.soldDate);
    		    	vehicle.title = vm.title;
    		    	soldVMs.add(vehicle);
    	    	}
    	     	
    	     	return ok(Json.toJson(soldVMs));
        	}	
        }
        
    	public static Result getAllDraftVehicles(Long locationId){
    		
	    	List <Vehicle> draftVehicleObjList = Vehicle.getVehiclesByDraftStatusAndLocation(locationId);
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	ArrayList<SpecificationVM> draftVMs = new ArrayList<>(); 
	     	for(Vehicle vm : draftVehicleObjList){
	     		VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
	     		SpecificationVM vehicle = new SpecificationVM();
	     		vehicle.id = vm.id;
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
		    	vehicle.year = vm.year;
		    	//vehicle.make = vm.make+" "+vm.model;
		    	vehicle.make = vm.make;
		    	vehicle.model = vm.model;
		    	vehicle.trim_level = vm.trim;
		    	vehicle.label = vm.label;
		    	vehicle.stock = vm.stock;
		    	vehicle.mileage = vm.mileage;
		    	vehicle.cost = vm.cost;
		    	vehicle.price = vm.price;
		    	int len=vehicle.vin.length();
		    	vehicle.last4Vin=vehicle.vin.substring(Math.max(len - 4, 0));
		    	vehicle.extColor = vm.exteriorColor;
		    	vehicle.intColor = vm.interiorColor;
		    	vehicle.colorDesc = vm.colorDescription;
		    	vehicle.doors = vm.doors;
		    	vehicle.stereo = vm.stereo;
		    	vehicle.engine = vm.engine;
		    	vehicle.fuel = vm.fuel;
		    	vehicle.city_mileage = vm.cityMileage;
		    	vehicle.highway_mileage = vm.highwayMileage;
		    	vehicle.bodyStyle = vm.bodyStyle;
		    	vehicle.drivetrain = vm.drivetrain;
		    	vehicle.transmission = vm.transmission;
		    	vehicle.location = vm.location;
		    	vehicle.status  =  vm.status;
		    	vehicle.vehicleCnt = VehicleImage.getVehicleImageCountByVIN(vm.vin);
		    	vehicle.sold = true;
		    	if(vehicleImg != null){
		    		vehicle.imagePath = vehicleImg.thumbPath;
		    		vehicle.imgId = vehicleImg.id;
		    	}
		    	vehicle.sold = false;
		    	 vehicle.title=vm.getTitle();
		    	 draftVMs.add(vehicle);
	    	}
	     	
	     	return ok(Json.toJson(draftVMs));
	}
    	
    	public static Result addSameNewCar(Long vinNo){
    		Vehicle vm = Vehicle.findById(vinNo);
    		Vehicle vObj = Vehicle.findByVinAndStatus(vm.vin);
    		
    		if(vObj == null){
    			Identity user = getLocalUser();
    	    	//Vehicle vehicleObj = Vehicle.findByVidAndUser(vm.vin);
    	    	Vehicle vehicle = new Vehicle();
    		    	vehicle.title = vm.title;
    		    	vehicle.category = vm.category;
    		    	vehicle.vin = vm.vin;
    		    	vehicle.year = vm.year;
    		    	vehicle.make = vm.make;
    		    	vehicle.model = vm.model;
    		    	vehicle.trim = vm.trim;
    		    	vehicle.label = vm.label;
    		    	vehicle.stock = vm.stock;
    		    	vehicle.cityMileage = vm.cityMileage;
    		        vehicle.mileage=vm.mileage;
    		    	vehicle.highwayMileage = vm.highwayMileage;
    		    	vehicle.cost = vm.cost;
    		    	vehicle.price = vm.price;
    		    	vehicle.madeIn = vm.madeIn;
    		    	vehicle.optionalSeating = vm.optionalSeating;
    		    	vehicle.exteriorColor = vm.exteriorColor;
    		    	vehicle.colorDescription = vm.colorDescription;
    		    	vehicle.doors = vm.doors;
    		    	vehicle.stereo = vm.stereo;
    		    	vehicle.engine = vm.engine;
    		    	vehicle.bodyStyle = vm.bodyStyle;
    		    	vehicle.location = vm.location;
    		    	vehicle.description = vm.description;
    				
    		    	vehicle.drivetrain = vm.drivetrain;
    		    	vehicle.fuelType = vm.fuelType;
    		    	vehicle.fuelTank = vm.fuelTank;
    		    	vehicle.headlights = vm.headlights;
    		    	vehicle.mirrors = vm.mirrors;
    		    	vehicle.groundClearance = vm.groundClearance;
    		    	vehicle.roof = vm.roof;
    		    	vehicle.height = vm.height;
    		    	vehicle.length = vm.length;
    		    	vehicle.width = vm.width;
    		    	vehicle.acceleration = vm.acceleration;
    		    	vehicle.standardSeating = vm.standardSeating;
    		    	vehicle.engineType = vm.engineType;
    		    	vehicle.cylinders = vm.cylinders;
    		    	vehicle.displacement = vm.displacement;
    		    	vehicle.camType = vm.camType;
    		    	vehicle.valves = vm.valves;
    		    	vehicle.fuelQuality = vm.fuelQuality;
    		    	vehicle.horsePower = vm.horsePower;
    		    	vehicle.transmission = vm.transmission;
    		    	vehicle.gears = vm.gears;
    		    	vehicle.brakes = vm.brakes;
    		    	vehicle.frontBrakeDiameter = vm.frontBrakeDiameter;
    		    	vehicle.frontBrakeType = vm.frontBrakeType;
    		    	vehicle.rearBrakeDiameter = vm.rearBrakeDiameter;
    		    	vehicle.rearBrakeType = vm.rearBrakeType;
    		    	vehicle.activeHeadRestrains = vm.activeHeadRestrains;
    		    	vehicle.bodySideReinforcements = vm.bodySideReinforcements;
    		    	vehicle.crumpleZones = vm.crumpleZones;
    		    	vehicle.impactAbsorbingBumpers = vm.impactAbsorbingBumpers;
    		    	vehicle.impactSensor = vm.impactSensor;
    		    	vehicle.parkingSensors = vm.parkingSensors;
    		    	vehicle.seatbelts = vm.seatbelts;
    		    	vehicle.audiSideAssist = vm.audiSideAssist;
    		    	vehicle.interiorColor = vm.interiorColor;
    		    	vehicle.comfortFeatures = vm.comfortFeatures;
    		    	vehicle.powerOutlet = vm.powerOutlet;
    		    	vehicle.powerSteering = vm.powerSteering;
    		    	vehicle.rearViewCamera = vm.rearViewCamera;
    		    	vehicle.rearViewMonitor = vm.rearViewMonitor;
    		    	vehicle.remoteTrunkRelease = vm.remoteTrunkRelease;
    		    	vehicle.steeringWheel = vm.steeringWheel;
    		    	vehicle.steeringWheelControls = vm.steeringWheelControls;
    				
    		    	vehicle.standardSeating = vm.standardSeating;
    				
    		    	vehicle.mileage = vm.mileage;
    				vehicle.user = (AuthUser)user;
    				vehicle.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    				
    		    	vehicle.madeIn = vm.madeIn;
    		    	vehicle.optionalSeating = vm.optionalSeating;
    		    	vehicle.status = "Newly Arrived";
    		    	vehicle.postedDate = new Date();
    		    	/*List<Site> siteList = new ArrayList<>();
    		    	if(vm.siteIds != null) {
    			    	for(Long obj: vm.siteIds) {
    			    		Site siteObj = Site.findById(obj);
    			    		siteList.add(siteObj);
    			    	}
    			    	vehicle.site = siteList;
    		    	}*/
    		    	vehicle.save();
    	    	sendEmailToBrandFollowers(vehicle.make);
    	    	return ok("success");
    		}else{
    			return ok("error");
    		}
    	}
    	
    	 public static void sendEmailToBrandFollowers(String brand) {
 	    	
 	    	AuthUser user = (AuthUser) getLocalUser();
 	    	List<FollowBrand> brandFollowers = FollowBrand.getAllBrandFollowersName(brand);
 	    	for(FollowBrand row: brandFollowers) {
 	    		AuthUser logoUser = getLocalUser();	
 	    		//AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
 		    	    SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));  //findByUser(logoUser);
 			    	String email = row.email;
 			    	/*List<FollowBrand> brandList = FollowBrand.getBrandsByEmail(email);
 			    	for(FollowBrand brandObj: brandList) {*/
 			    		
 			    		List<Vehicle> vehicleList = Vehicle.getVehiclesByMake(row.brand, Location.findById(Long.valueOf(session("USER_LOCATION"))));
 			    		List<VehicleVM> vehicleVMList = new ArrayList<>();
 			    		
 			    		for(Vehicle vehicle: vehicleList) {
 			    			VehicleImage defaultImage = VehicleImage.getDefaultImage(vehicle.vin);
 			    			VehicleVM vm = new VehicleVM();
 			    			vm.vin = vehicle.vin;
 			    			vm.make = vehicle.make;
 			    			vm.model = vehicle.model;
 			    			vm.price = "$"+vehicle.price;
 			    			if(defaultImage != null) {
 			    				vm.imageUrl = defaultImage.thumbPath;
 			    			}
 			    			vehicleVMList.add(vm);
 			    		}
 			    		
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
 							Message message = new MimeMessage(session);
 				    		try{
 							message.setFrom(new InternetAddress(emailUser,emailName));
 				    		}catch(UnsupportedEncodingException e){
 				    			e.printStackTrace();
 				    		}
 							message.setRecipients(Message.RecipientType.TO,
 							InternetAddress.parse(email));
 							message.setSubject("CAR BRAND INVENTORY UPDATE");
 							Multipart multipart = new MimeMultipart();
 							BodyPart messageBodyPart = new MimeBodyPart();
 							messageBodyPart = new MimeBodyPart();
 							
 							VelocityEngine ve = new VelocityEngine();
 							ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
 							ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
 							ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
 							ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
 							ve.init();
 						
 							
 					        Template t = ve.getTemplate("/public/emailTemplate/brandFollowersTemplate.vm"); 
 					        VelocityContext context = new VelocityContext();
 					        
 					        context.put("hostnameUrl", imageUrlPath);
 					        context.put("siteLogo", logo.logoImagePath);
 					        
 					        context.put("name", row.name);
 					        context.put("brand", row.brand);
 					        context.put("vehicleList", vehicleVMList);
 					       
 					        
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
 			    	//}
 	    	}
 	    }
    	 
    	 public static Result setVehicleStatus() {
    	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    	    		return ok(home.render("",userRegistration));
    	    	} else {
    	    		Date currDate = new Date();
    	    		AuthUser user = (AuthUser) getLocalUser();
    	    		List<String> emailList = new ArrayList<>();
    	    		Form<SoldContactVM> form = DynamicForm.form(SoldContactVM.class).bindFromRequest();
    	    		SoldContactVM vm = form.get();
    	    		SoldContact contact = new SoldContact();
    	    		contact.name = vm.name;
    	    		contact.email = vm.email;
    	    		contact.phone = vm.phone;
    	    		contact.gender = vm.gender;
    	    		contact.age = vm.age;
    	    		contact.buyingFor = vm.buyingFor;
    	    		contact.howContactedUs = vm.howContactedUs;
    	    		contact.howFoundUs = vm.howFoundUs;
    	    		contact.make = vm.make;
    	    		contact.year = vm.year;
    	    		contact.mileage = vm.mileage;
    	    		contact.price = vm.price;
    	    		contact.custZipCode = vm.custZipCode;
    	    		contact.enthicity = vm.enthicity;
    	    		
    	    		contact.save();
    	    		Contacts contactsObj = new Contacts();
    	    		String arr[] = vm.name.split(" ");
    	    		if(arr.length >= 1) {
    	    			contactsObj.firstName = arr[0];
    	    		} else {
    	    			contactsObj.firstName = vm.name;
    	    		}
    	    		if(arr.length >= 2) {
    	    			contactsObj.middleName = arr[1];
    	    		}
    	    		if(arr.length >= 3) {
    	    			contactsObj.lastName = arr[2];
    	    		} 
    	    		contactsObj.email = vm.email;
    	    		contactsObj.phone = vm.phone;
    	    		contactsObj.custZipCode = vm.custZipCode;
    	    		contactsObj.enthicity = vm.enthicity;
    	    		contactsObj.newsLetter = 0;
    	    		contactsObj.user = user.id;
    	    		contactsObj.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	    		contactsObj.save();
    	    		
    	    		
    	    		Vehicle vehicle = Vehicle.findByVinAndStatus(vm.vin);
    	    		
    	    		if(vehicle != null){
    		    		vehicle.setStatus("Sold");
    		    		Date date = new Date();
    		    		vehicle.setSoldDate(date);
    		    		vehicle.setSoldUser(user);
    		    		vehicle.setPrice(Integer.parseInt(vm.price));
    		    		vehicle.update();
    	    		}
    	    		
    	    		if(vm.statusVal.equals("Sold")){
    	    			List<TradeIn> tIn = TradeIn.findByVinAndLocation(vm.vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
    	        		for(TradeIn tradeIn:tIn){
    	        			if(tradeIn.status == null){
    	        				tradeIn.setStatus("LOST");
    	        				tradeIn.setStatusDate(currDate);
    	        				tradeIn.setStatusTime(currDate);
    	        				tradeIn.update();
    	        				
    	        				UserNotes uNotes1 = new UserNotes();
    	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
    	        	    		uNotes1.setAction("Other");
    	        	    		uNotes1.createdDate = currDate;
    	        	    		uNotes1.createdTime = currDate;
    	        	    		//uNotes1.user = user;
    	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	        	    		uNotes1.tradeIn = TradeIn.findById(tradeIn.id);
    	        	    		uNotes1.save();
    	        			}else if(!tradeIn.status.equals("COMPLETE")){
    	        				tradeIn.setStatus("LOST");
    	        				tradeIn.setStatusDate(currDate);
    	        				tradeIn.setStatusTime(currDate);
    	        				tradeIn.update();
    	        				
    	        				UserNotes uNotes1 = new UserNotes();
    	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
    	        	    		uNotes1.setAction("Other");
    	        	    		uNotes1.createdDate = currDate;
    	        	    		uNotes1.createdTime = currDate;
    	        	    		//uNotes1.user = user;
    	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	        	    		uNotes1.tradeIn = TradeIn.findById(tradeIn.id);
    	        	    		uNotes1.save();
    	        			}
    	        			
    	        			if(tradeIn.assignedTo !=null){
    	        				AuthUser userObj = AuthUser.findById(tradeIn.assignedTo.id);
    	        				if(userObj !=null){
    	        					emailList.add(userObj.email);
    	        				}
    	        			}
    	        		}
    	        		
    	        		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocation(vm.vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
    	        		for(RequestMoreInfo rMoreInfo:rInfos){
    	        			if(rMoreInfo.status == null){
    	        				rMoreInfo.setStatus("LOST");
    	        				rMoreInfo.setStatusDate(currDate);
    	        				rMoreInfo.setStatusTime(currDate);
    	        				rMoreInfo.update();
    	        				
    	        				UserNotes uNotes1 = new UserNotes();
    	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
    	        	    		uNotes1.setAction("Other");
    	        	    		uNotes1.createdDate = currDate;
    	        	    		uNotes1.createdTime = currDate;
    	        	    		//uNotes1.user = user;
    	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	        	    		uNotes1.tradeIn = TradeIn.findById(rMoreInfo.id);
    	        	    		uNotes1.save();
    	        			}else if(!rMoreInfo.status.equals("COMPLETE")){
    	        				rMoreInfo.setStatus("LOST");
    	        				rMoreInfo.setStatusDate(currDate);
    	        				rMoreInfo.setStatusTime(currDate);
    	        				rMoreInfo.update();
    	        				
    	        				UserNotes uNotes1 = new UserNotes();
    	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
    	        	    		uNotes1.setAction("Other");
    	        	    		uNotes1.createdDate = currDate;
    	        	    		uNotes1.createdTime = currDate;
    	        	    		//uNotes1.user = user;
    	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	        	    		uNotes1.tradeIn = TradeIn.findById(rMoreInfo.id);
    	        	    		uNotes1.save();
    	        			}
    	        			if(rMoreInfo.assignedTo !=null){
    	        				AuthUser userObj = AuthUser.findById(rMoreInfo.assignedTo.id);
    	        				if(userObj !=null){
    	        					emailList.add(userObj.email);
    	        				}
    	        			}
    	        		}
    	        		
    	        		
    	        		List<ScheduleTest> sTests = ScheduleTest.findByVinAndLocation(vm.vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
    	        		for(ScheduleTest scheduleTest:sTests){
    	        			if(scheduleTest.leadStatus == null){
    	        				scheduleTest.setLeadStatus("LOST");
    	        				scheduleTest.setStatusDate(currDate);
    	        				scheduleTest.setStatusTime(currDate);
    	        				scheduleTest.update();
    	        				
    	        				UserNotes uNotes1 = new UserNotes();
    	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
    	        	    		uNotes1.setAction("Other");
    	        	    		uNotes1.createdDate = currDate;
    	        	    		uNotes1.createdTime = currDate;
    	        	    		//uNotes1.user = user;
    	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	        	    		uNotes1.tradeIn = TradeIn.findById(scheduleTest.id);
    	        	    		uNotes1.save();
    	        			}else if(!scheduleTest.leadStatus.equals("COMPLETE")){
    	        				scheduleTest.setLeadStatus("LOST");
    	        				scheduleTest.setStatusDate(currDate);
    	        				scheduleTest.setStatusTime(currDate);
    	        				scheduleTest.update();
    	        				
    	        				UserNotes uNotes1 = new UserNotes();
    	        	    		uNotes1.setNote("Vehicle has been sold by another Sales Person");
    	        	    		uNotes1.setAction("Other");
    	        	    		uNotes1.createdDate = currDate;
    	        	    		uNotes1.createdTime = currDate;
    	        	    		//uNotes1.user = user;
    	        	    		uNotes1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	        	    		uNotes1.tradeIn = TradeIn.findById(scheduleTest.id);
    	        	    		uNotes1.save();
    	        			}
    	        			if(scheduleTest.assignedTo !=null){
    	        				AuthUser userObj = AuthUser.findById(scheduleTest.assignedTo.id);
    	        				if(userObj !=null){
    	        					emailList.add(userObj.email);
    	        				}
    	        			}
    	        		}
    	    		}
    	    		
    	    		if(emailList.size()>0){
    	    			vehicleSoldEmail(emailList);
    	    		}
    	    		
    	    		return ok();
    	    	}
    	    }
    	 
    	 public static Result vehicleSoldEmail(List<String> emailList) {
    			
 			for(String email : emailList) {
 				EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
 				String emailName=details.name;
 				String port=details.port;
 				String gmail=details.host;
 				final	String emailUser=details.username;
 				final	String emailPass=details.passward;
 				Properties props = new Properties();
 		 		props.put("mail.smtp.auth", "true");
 		 		props.put("mail.smtp.starttls.enable", "true");
 		 		props.put("mail.smtp.host", gmail);
 		 		props.put("mail.smtp.port",port);
 		  
 		 		Session session = Session.getInstance(props,
 		 		  new javax.mail.Authenticator() {
 		 			protected PasswordAuthentication getPasswordAuthentication() {
 		 				return new PasswordAuthentication(emailUser, emailPass);
 		 			}
 		 		  });
 		  
 		 		try{
 		 			
 		 			Message feedback = new MimeMessage(session);
 		  			feedback.setFrom(new InternetAddress(emailUser));
 		  			feedback.setRecipients(Message.RecipientType.TO,
 		  			InternetAddress.parse(email));
 		  			 feedback.setSubject("Vehicle Sold notificatio");	  			
 		  			 BodyPart messageBodyPart = new MimeBodyPart();	
 		  	         messageBodyPart.setText("Vehicle Sold notificatio");	 	    
 		  	         Multipart multipart = new MimeMultipart();	  	    
 		  	         multipart.addBodyPart(messageBodyPart);	            
 		  	         feedback.setContent(multipart);
 		  		     Transport.send(feedback);
 	    			System.out.println("email send");
 		       		} catch (MessagingException e) {
 		  			  throw new RuntimeException(e);
 		  		}
 		}
 		return ok();
 	}
    	 
    	 public static Result exportDataAsCSV() throws IOException {
    	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    	    		return ok(home.render("",userRegistration));
    	    	} else {
    		    	AuthUser user = (AuthUser) getLocalUser();
    		    	CSVWriter writer = new CSVWriter(new FileWriter("vehicleInfo.csv"));
    		    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(user);
    		    	
    		    	for(Vehicle vehicle: vehicleList) {
    		    		String []row = new String[22];
    		    		row[0] = "ms5421";
    		    		row[1] = vehicle.stock;
    		    		row[2] = vehicle.year;
    		    		row[3] = vehicle.make;
    		    		row[4] = vehicle.model;
    		    		row[5] = vehicle.trim;
    		    		row[6] = vehicle.vin;
    		    		row[7] = vehicle.mileage;
    		    		row[8] = vehicle.price.toString();
    		    		row[9] = vehicle.exteriorColor;
    		    		row[10] = vehicle.interiorColor;
    		    		row[11] = vehicle.transmission;
    		    		row[12] = "";
    		    		row[13] = vehicle.description;  //description
    		    		row[14] = vehicle.bodyStyle;
    		    		row[15] = vehicle.engine;
    		    		row[16] = vehicle.drivetrain;
    		    		row[17] = vehicle.fuel;
    		    		String standardOptions = "";
    		    		if(vehicle.activeHeadRestrains != null) {
    		    			standardOptions = standardOptions + vehicle.activeHeadRestrains+",";
    		    		}
    		    		if(vehicle.bodySideReinforcements != null) {
    		    			standardOptions = standardOptions + vehicle.bodySideReinforcements+",";
    		    		}
    		    		if(vehicle.crumpleZones != null) {
    		    			standardOptions = standardOptions + vehicle.crumpleZones+",";
    		    		}
    		    		if(vehicle.impactAbsorbingBumpers != null) {
    		    			standardOptions = standardOptions + vehicle.impactAbsorbingBumpers+",";
    		    		}
    		    		if(vehicle.impactSensor != null) {
    		    			standardOptions = standardOptions + vehicle.impactSensor+",";
    		    		}
    		    		if(vehicle.parkingSensors != null) {
    		    			standardOptions = standardOptions + vehicle.parkingSensors+",";
    		    		}
    		    		if(vehicle.seatbelts != null) {
    		    			standardOptions = standardOptions + vehicle.seatbelts+",";
    		    		}
    		    		if(vehicle.interiorColor != null) {
    		    			standardOptions = standardOptions + vehicle.interiorColor+",";
    		    		}
    		    		if(vehicle.powerOutlet != null) {
    		    			standardOptions = standardOptions + vehicle.powerOutlet+",";
    		    		}
    		    		if(vehicle.powerSteering != null) {
    		    			standardOptions = standardOptions + vehicle.powerSteering+",";
    		    		}
    		    		if(vehicle.rearViewCamera != null) {
    		    			standardOptions = standardOptions + vehicle.rearViewCamera+",";
    		    		}
    		    		if(vehicle.rearViewMonitor != null) {
    		    			standardOptions = standardOptions + vehicle.rearViewMonitor+",";
    		    		}
    		    		if(vehicle.remoteTrunkRelease != null) {
    		    			standardOptions = standardOptions + vehicle.remoteTrunkRelease+",";
    		    		}
    		    		if(vehicle.steeringWheel != null) {
    		    			standardOptions = standardOptions + vehicle.steeringWheel+",";
    		    		}
    		    		if(vehicle.steeringWheelControls != null) {
    		    			standardOptions = standardOptions + vehicle.steeringWheelControls;
    		    		}
    		    		
    		    		row[18] = standardOptions;
    		    		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicle.vin);
    		    		String str = "";
    		    		for(VehicleImage img : vImageList) {
    		    			str = str +imageUrlPath+img.path+",";
    		    		}
    		    		row[19] = str;
    		    		VirtualTour vt = VirtualTour.findByUserAndVin(user,vehicle.vin);
    		    		if(vt != null) {
    		    			row[20] = vt.desktopUrl;
    		    			row[21] = vt.mobileUrl;
    		    		} else {
    		    			row[20] = "";
    		    			row[21] = "";
    		    		}
    		    		writer.writeNext(row);
    		    	}
    		    	
    		    	 writer.close();
    		    	 
    		    	 File file = new File("vehicleInfo.csv");
    		    	response().setContentType("application/csv");
    		     	response().setHeader("Content-Disposition", "inline; filename=autoTrader.csv");
    		 		return ok(file);
    	    	}	
    	    }
    	 
    	 public static Result exportCarfaxCSV() throws IOException {
    	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    	    		return ok(home.render("",userRegistration));
    	    	} else {
    		    	AuthUser user = (AuthUser) getLocalUser();
    		    	CSVWriter writer = new CSVWriter(new FileWriter("D:\\vehicleCarfaxInfo.csv"));
    		    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(user);
    		    	String []rowHeaders = new String[48];
    		    	rowHeaders[0] = "Record Id";
    		    	rowHeaders[1] = "Vin";
    		    	rowHeaders[2] = "Stock Number";
    		    	rowHeaders[3] = "Title";
    		    	rowHeaders[4] = "Url";
    		    	rowHeaders[5] = "Category";
    		    	rowHeaders[6] = "Images";
    		    	rowHeaders[7] = "Address";
    		    	rowHeaders[8] = "City";
    		    	rowHeaders[9] = "State";
    		    	rowHeaders[10] = "Zip";
    		    	rowHeaders[11] = "Country";
    		    	rowHeaders[12] = "Seller Type";
    		    	rowHeaders[13] = "Dealer Name";
    		    	rowHeaders[14] = "Dealer Id";
    		    	rowHeaders[15] = "Dealer Email";
    		    	rowHeaders[16] = "Dealer Phone";
    		    	rowHeaders[17] = "Delaer Website";
    		    	rowHeaders[18] = "Dealer Fee";
    		    	rowHeaders[19] = "Make";
    		    	rowHeaders[20] = "Model";
    		    	rowHeaders[21] = "Trim";
    		    	rowHeaders[22] = "Body";
    		    	rowHeaders[23] = "Mileage";
    		    	rowHeaders[24] = "Year";
    		    	rowHeaders[25] = "Currency";
    		    	rowHeaders[26] = "Price";
    		    	rowHeaders[27] = "MSRP";
    		    	rowHeaders[28] = "Internet Price";
    		    	rowHeaders[29] = "Selling Price";
    		    	rowHeaders[30] = "Retail Price";
    		    	rowHeaders[31] = "Invoice Price";
    		    	rowHeaders[32] = "Exterior Color";
    		    	rowHeaders[33] = "Interior Color";
    		    	rowHeaders[34] = "Interior Material";
    		    	rowHeaders[35] = "Doors";
    		    	rowHeaders[36] = "Cylinders";
    		    	rowHeaders[37] = "Engine Size";
    		    	rowHeaders[38] = "Drive Type";
    		    	rowHeaders[39] = "Transmission";
    		    	rowHeaders[40] = "Cpo";
    		    	rowHeaders[41] = "Description";
    		    	rowHeaders[42] = "Standard Features";
    		    	rowHeaders[43] = "Optional Features";
    		    	rowHeaders[44] = "Seller Comments";
    		    	rowHeaders[45] = "Vehicle Condition";
    		    	rowHeaders[46] = "Listing Time";
    		    	rowHeaders[47] = "Expire Time";
    		    	writer.writeNext(rowHeaders);
    				
    		    	for(Vehicle vehicle: vehicleList) {
    		    		String []row = new String[48];
    		    		row[0] = "12345678";
    		    		row[1] = vehicle.vin;
    		    		row[2] = vehicle.stock;
    		    		row[3] = vehicle.year+" "+vehicle.make+" "+vehicle.model;
    		    		row[4] = "http://www.domain.com/cars/12345679.html";
    		    		row[5] = vehicle.category;
    		    		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicle.vin);
    		    		String str = "";
    		    		for(VehicleImage img : vImageList) {
    		    			str = str +rootDir+img.path+"|";
    		    		}
    		    		row[6] = str;
    		    		row[7] = "1234 Main Street";
    		    		row[8] = "San Francisco";
    		    		row[9] = "CA";
    		    		row[10] = "94105";
    		    		row[11] = "United States";
    		    		row[12] = "Dealer";
    		    		row[13] = "Dealer";
    		    		row[14] = "4567";
    		    		row[15] = "someone@dealerwebsite.com";
    		    		row[16] = "800-123-4567";
    		    		row[17] = "dealerwebsite.com";
    		    		row[18] = "";
    		    		row[19] = vehicle.make;
    		    		row[20] = vehicle.model;
    		    		row[21] = vehicle.trim;
    		    		row[22] = vehicle.bodyStyle;
    		    		row[23] = vehicle.mileage;
    		    		row[24] = vehicle.year;
    		    		row[25] = "USD";
    		    		row[26] = vehicle.price.toString();
    		    		row[27] = vehicle.getPrice().toString();
    		    		row[28] = "";
    		    		row[29] = "";
    		    		row[30] = "";
    		    		row[31] = "";
    		    		row[32] = vehicle.exteriorColor;
    		    		row[33] = vehicle.interiorColor;
    		    		row[34] = "fabric";
    		    		row[35] = vehicle.doors;
    		    		row[36] = vehicle.cylinders;
    		    		row[37] = vehicle.engine;
    		    		row[38] = vehicle.drivetrain;
    		    		row[39] = vehicle.transmission;
    		    		row[40] = "YES";
    		    		row[41] = vehicle.description; //description
    		    		
    		    		String standardFeatures = "";
    		    		if(vehicle.drivetrain != null) {
    		    			standardFeatures = standardFeatures + vehicle.drivetrain+",";
    		    		}
    		    		if(vehicle.fuelType != null) {
    		    			standardFeatures = standardFeatures + vehicle.fuelType+",";
    		    		}
    		    		if(vehicle.fuelTank != null) {
    		    			standardFeatures = standardFeatures + vehicle.fuelTank+",";
    		    		}
    		    		if(vehicle.headlights != null) {
    		    			standardFeatures = standardFeatures + vehicle.headlights+",";
    		    		}
    		    		if(vehicle.mirrors != null) {
    		    			standardFeatures = standardFeatures + vehicle.mirrors+",";
    		    		}
    		    		if(vehicle.roof != null) {
    		    			standardFeatures = standardFeatures + vehicle.roof+",";
    		    		}
    		    		if(vehicle.acceleration != null) {
    		    			standardFeatures = standardFeatures + vehicle.acceleration+",";
    		    		}
    		    		if(vehicle.standardSeating != null) {
    		    			standardFeatures = standardFeatures + vehicle.standardSeating+",";
    		    		}
    		    		if(vehicle.engine != null) {
    		    			standardFeatures = standardFeatures + vehicle.engine+",";
    		    		}
    		    		if(vehicle.camType != null) {
    		    			standardFeatures = standardFeatures + vehicle.camType+",";
    		    		}
    		    		if(vehicle.valves != null) {
    		    			standardFeatures = standardFeatures + vehicle.valves+",";
    		    		}
    		    		if(vehicle.cylinders != null) {
    		    			standardFeatures = standardFeatures + vehicle.cylinders+",";
    		    		}
    		    		if(vehicle.fuelQuality != null) {
    		    			standardFeatures = standardFeatures + vehicle.fuelQuality+",";
    		    		}
    		    		if(vehicle.horsePower != null) {
    		    			standardFeatures = standardFeatures + vehicle.horsePower+",";
    		    		}
    		    		if(vehicle.transmission != null) {
    		    			standardFeatures = standardFeatures + vehicle.transmission+",";
    		    		}
    		    		if(vehicle.gears != null) {
    		    			standardFeatures = standardFeatures + vehicle.gears+",";
    		    		}
    		    		if(vehicle.brakes != null) {
    		    			standardFeatures = standardFeatures + vehicle.brakes+",";
    		    		}
    		    		if(vehicle.frontBrakeDiameter != null) {
    		    			standardFeatures = standardFeatures + vehicle.frontBrakeDiameter+",";
    		    		}
    		    		if(vehicle.frontBrakeType != null) {
    		    			standardFeatures = standardFeatures + vehicle.frontBrakeType+",";
    		    		}
    		    		if(vehicle.rearBrakeDiameter != null) {
    		    			standardFeatures = standardFeatures + vehicle.rearBrakeDiameter+",";
    		    		}
    		    		if(vehicle.rearBrakeType != null) {
    		    			standardFeatures = standardFeatures + vehicle.rearBrakeType;
    		    		}
    		    		row[42] = standardFeatures;
    		    		
    		    		
    		    		String standardOptions = "";
    		    		
    		    		if(vehicle.activeHeadRestrains != null) {
    		    			standardOptions = standardOptions + vehicle.activeHeadRestrains+",";
    		    		}
    		    		if(vehicle.bodySideReinforcements != null) {
    		    			standardOptions = standardOptions + vehicle.bodySideReinforcements+",";
    		    		}
    		    		if(vehicle.crumpleZones != null) {
    		    			standardOptions = standardOptions + vehicle.crumpleZones+",";
    		    		}
    		    		if(vehicle.impactAbsorbingBumpers != null) {
    		    			standardOptions = standardOptions + vehicle.impactAbsorbingBumpers+",";
    		    		}
    		    		if(vehicle.impactSensor != null) {
    		    			standardOptions = standardOptions + vehicle.impactSensor+",";
    		    		}
    		    		if(vehicle.parkingSensors != null) {
    		    			standardOptions = standardOptions + vehicle.parkingSensors+",";
    		    		}
    		    		if(vehicle.seatbelts != null) {
    		    			standardOptions = standardOptions + vehicle.seatbelts+",";
    		    		}
    		    		if(vehicle.interiorColor != null) {
    		    			standardOptions = standardOptions + vehicle.interiorColor+",";
    		    		}
    		    		if(vehicle.powerOutlet != null) {
    		    			standardOptions = standardOptions + vehicle.powerOutlet+",";
    		    		}
    		    		if(vehicle.powerSteering != null) {
    		    			standardOptions = standardOptions + vehicle.powerSteering+",";
    		    		}
    		    		if(vehicle.rearViewCamera != null) {
    		    			standardOptions = standardOptions + vehicle.rearViewCamera+",";
    		    		}
    		    		if(vehicle.rearViewMonitor != null) {
    		    			standardOptions = standardOptions + vehicle.rearViewMonitor+",";
    		    		}
    		    		if(vehicle.remoteTrunkRelease != null) {
    		    			standardOptions = standardOptions + vehicle.remoteTrunkRelease+",";
    		    		}
    		    		if(vehicle.steeringWheel != null) {
    		    			standardOptions = standardOptions + vehicle.steeringWheel+",";
    		    		}
    		    		if(vehicle.steeringWheelControls != null) {
    		    			standardOptions = standardOptions + vehicle.steeringWheelControls;
    		    		}
    		    		
    		    		row[43] = standardOptions;
    		    		row[44] = "";
    		    		row[45] = "Used";
    		    		row[46] = "2012-09-16-11:00:00";
    		    		row[47] = "2012-10-16-11:00:00";
    		    		
    		    		writer.writeNext(row);
    		    	}
    		    	
    		    	 writer.close();
    		    	return ok();
    	    	}	
    	    }
    	 
    	 public static Result exportCarGurusCSV() throws IOException {
    	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    	    		return ok(home.render("",userRegistration));
    	    	} else {
    		    	AuthUser user = (AuthUser) getLocalUser();
    		    	CSVWriter writer = new CSVWriter(new FileWriter("D:\\vehicleCarGurusInfo.csv"));
    		    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(user);
    		    	
    		    	String []rowHeaders = new String[29];
    		    	rowHeaders[0] = "VIN";
    		    	rowHeaders[1] = "Make";
    		    	rowHeaders[2] = "Model";
    		    	rowHeaders[3] = "Year";
    		    	rowHeaders[4] = "Trim";
    		    	rowHeaders[5] = "Price";
    		    	rowHeaders[6] = "Mileage";
    		    	rowHeaders[7] = "Picture Urls";
    		    	rowHeaders[8] = "Exterior Color";
    		    	rowHeaders[9] = "Dealer Comments";
    		    	rowHeaders[10] = "Stock Number";
    		    	rowHeaders[11] = "Transmission Type";
    		    	rowHeaders[12] = "Installed Options";
    		    	rowHeaders[13] = "Dealer Id";
    		    	rowHeaders[14] = "Dealer Name";
    		    	rowHeaders[15] = "Dealer Street Address";
    		    	rowHeaders[16] = "Dealer City";
    		    	rowHeaders[17] = "Delaer State";
    		    	rowHeaders[18] = "Dealer Zip";
    		    	rowHeaders[19] = "Dealer CRM Email";
    		    	rowHeaders[20] = "MSRP";
    		    	rowHeaders[21] = "Interior Color";
    		    	rowHeaders[22] = "Certified";
    		    	rowHeaders[23] = "Is New";
    		    	rowHeaders[24] = "Engine";
    		    	rowHeaders[25] = "Dealer Latitude and Longitude";
    		    	rowHeaders[26] = "Dealer Radius";
    		    	rowHeaders[27] = "Dealer Phone Number";
    		    	rowHeaders[28] = "Dealer Website Url";
    		    	
    		    	writer.writeNext(rowHeaders);
    		    	
    		    	for(Vehicle vehicle: vehicleList) {
    		    		String []row = new String[29];
    		    		row[0] = vehicle.vin;
    		    		row[1] = vehicle.make;
    		    		row[2] = vehicle.model;
    		    		row[3] = vehicle.year;
    		    		row[4] = vehicle.trim;
    		    		row[5] = vehicle.price.toString();
    		    		row[6] = vehicle.mileage;
    		    		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicle.vin);
    		    		String str = "";
    		    		for(VehicleImage img : vImageList) {
    		    			str = str +rootDir+img.path+",";
    		    		}
    		    		row[7] = str;
    		    		row[8] = vehicle.exteriorColor;
    		    		row[9] = "";
    		    		row[10] = vehicle.stock;
    		    		row[11] = vehicle.transmission;
    		    		
    		    		String standardFeatures = "";
    		    		if(vehicle.drivetrain != null) {
    		    			standardFeatures = standardFeatures + vehicle.drivetrain+",";
    		    		}
    		    		if(vehicle.fuelType != null) {
    		    			standardFeatures = standardFeatures + vehicle.fuelType+",";
    		    		}
    		    		if(vehicle.fuelTank != null) {
    		    			standardFeatures = standardFeatures + vehicle.fuelTank+",";
    		    		}
    		    		if(vehicle.headlights != null) {
    		    			standardFeatures = standardFeatures + vehicle.headlights+",";
    		    		}
    		    		if(vehicle.mirrors != null) {
    		    			standardFeatures = standardFeatures + vehicle.mirrors+",";
    		    		}
    		    		if(vehicle.roof != null) {
    		    			standardFeatures = standardFeatures + vehicle.roof+",";
    		    		}
    		    		if(vehicle.acceleration != null) {
    		    			standardFeatures = standardFeatures + vehicle.acceleration+",";
    		    		}
    		    		if(vehicle.standardSeating != null) {
    		    			standardFeatures = standardFeatures + vehicle.standardSeating+",";
    		    		}
    		    		if(vehicle.engine != null) {
    		    			standardFeatures = standardFeatures + vehicle.engine+",";
    		    		}
    		    		if(vehicle.camType != null) {
    		    			standardFeatures = standardFeatures + vehicle.camType+",";
    		    		}
    		    		if(vehicle.valves != null) {
    		    			standardFeatures = standardFeatures + vehicle.valves+",";
    		    		}
    		    		if(vehicle.cylinders != null) {
    		    			standardFeatures = standardFeatures + vehicle.cylinders+",";
    		    		}
    		    		if(vehicle.fuelQuality != null) {
    		    			standardFeatures = standardFeatures + vehicle.fuelQuality+",";
    		    		}
    		    		if(vehicle.horsePower != null) {
    		    			standardFeatures = standardFeatures + vehicle.horsePower+",";
    		    		}
    		    		if(vehicle.transmission != null) {
    		    			standardFeatures = standardFeatures + vehicle.transmission+",";
    		    		}
    		    		if(vehicle.gears != null) {
    		    			standardFeatures = standardFeatures + vehicle.gears+",";
    		    		}
    		    		if(vehicle.brakes != null) {
    		    			standardFeatures = standardFeatures + vehicle.brakes+",";
    		    		}
    		    		if(vehicle.frontBrakeDiameter != null) {
    		    			standardFeatures = standardFeatures + vehicle.frontBrakeDiameter+",";
    		    		}
    		    		if(vehicle.frontBrakeType != null) {
    		    			standardFeatures = standardFeatures + vehicle.frontBrakeType+",";
    		    		}
    		    		if(vehicle.rearBrakeDiameter != null) {
    		    			standardFeatures = standardFeatures + vehicle.rearBrakeDiameter+",";
    		    		}
    		    		if(vehicle.rearBrakeType != null) {
    		    			standardFeatures = standardFeatures + vehicle.rearBrakeType;
    		    		}
    		    		
    		    		row[12] = standardFeatures;
    		    		row[13] = "1234";
    		    		row[14] = "Autolinx Inc";
    		    		row[15] = "3300 Sonoma Blvd";
    		    		row[16] = "Vallejo";
    		    		row[17] = "California";
    		    		row[18] = "94590";
    		    		row[19] = "info@autolinxinc.com";
    		    		row[20] = vehicle.getPrice().toString();
    		    		row[21] = vehicle.interiorColor;
    		    		row[22] = "";
    		    		row[23] = "N";
    		    		row[24] = vehicle.engine;
    		    		row[25] = "38.120301  -122.254508";
    		    		row[26] = "1000";
    		    		row[27] = "(707)552-5469";
    		    		row[28] = "www.autolinxinc.com/";
    		    		
    		    		writer.writeNext(row);
    		    	}
    		    	
    		    	 writer.close();
    		    	return ok();
    	    	}	
    	    }
}