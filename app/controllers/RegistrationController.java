package controllers;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

import models.AuthUser;
import models.Registration;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import viewmodel.RegisterVM;
import views.html.home;
import akka.actor.ActorSystem;

public class RegistrationController extends Controller {
  
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
			
	
	
	public static Result registerUser() {
		Form<RegisterVM> form = DynamicForm.form(RegisterVM.class).bindFromRequest();
		RegisterVM vm=form.get();
		//AuthUser user=new AuthUser();
		Date date = new Date();
		
		Registration regi = new Registration();
		    	 
    	   regi.status = "pending";
    	   regi.name=vm.name;
    	   regi.email=vm.email;
    	   regi.phone=vm.phone;
    	   regi.businessAdd = vm.businessAddress;
    	   regi.businessName =vm.businessName;
    	   regi.location =vm.oneLocation;
    	   regi.options = vm.options;
    	   regi.registrationDate = date;
    	   regi.sendDemoFlag = 0;
    	   
    	   regi.save();
   		
    	   String subject= "Successfully registered";
          String comments="Successfully registered";
   		//sendEmail(user.email,comment);
    	 sendEmail(vm.email,subject,comments);
    	   
		
	/*	MyProfile profile=new MyProfile();
		List<Permission> permissionList = Permission.getAllPermission();
		 List<Permission> permissionData = new ArrayList<>();
		if(vm.oneLocation.equalsIgnoreCase("one")){
	    	 Location location=new Location();
	    	 location.name=vm.businessName;
	    	 location.address=vm.businessAddress;
	    	 location.save();
	    	 user.location=location;
	    	 profile.locations=location;
	    	 user.role="Manager";
	      }*/
		/*Generate passward*/
/*		if(vm.oneLocation.equalsIgnoreCase("multi")){
			user.role="General Manager";
		}*/
		
		
		/*generate permisssioons*/
		/*if(user.role.equals("Manager")) {
 		   for(Permission obj: permissionList) {
 			   if(!obj.name.equals("Show Location")) {
 				   permissionData.add(obj);
 			   }
 		   }
 		   user.permission = permissionData;
 	   } 
*/
/* if(user.role.equals("General Manager")) {
	   for(Permission obj: permissionList) {
		   if(obj.name.equals("CRM") || obj.name.equals("My Profile") || obj.name.equals("Dashboard") || obj.name.equals("Dealer's Profile")) {
			   permissionData.add(obj);
		   }
	   }
	   user.permission = permissionData;
	   
 }*/
		
		/*
		user.firstName=vm.name;
		user.email=vm.email;
		user.phone=vm.phone;
		user.save();
		profile.address=vm.businessAddress;
		profile.myname=vm.businessName;
		profile.email=vm.email;
		profile.phone=vm.phone;
		profile.businessOption=vm.options;
		profile.save();
       String comment="Successfully registered";
		sendEmail(user.email,comment);*/
		
		return ok();
	}
	

	public static Result updateRegisterUser() {
		Form<RegisterVM> form = DynamicForm.form(RegisterVM.class).bindFromRequest();
		RegisterVM vm=form.get();
		Date date = new Date();
		
		Registration regi = Registration.findById(vm.id);
		    regi.setLastTimeLoggedIn(vm.lastTimeLoggedIn);
		    regi.setWebsite(vm.website);
		    regi.setActiveUser(vm.activeUser);
    	   regi.setName(vm.name);
    	   regi.setEmail(vm.email);
    	   regi.setPhone(vm.phone);
    	   regi.setBusinessAdd(vm.businessAdd);
    	   regi.setBusinessName(vm.businessName);
    	   regi.setLocation(vm.oneLocation);
    	   regi.setOptions(vm.options);
    	   
    	   regi.setRegistrationDate(date);
    	   //regi.sendDemoFlag = 0;
    	   
    	   regi.update();
   		
    	   //String subject= "Successfully registered";
          //String comments="Successfully registered";
    	 //sendEmail(vm.email,subject,comments);
    	   
		
		return ok();
	}	
	
	public static Result updateClientUser() {
		Form<RegisterVM> form = DynamicForm.form(RegisterVM.class).bindFromRequest();
		RegisterVM vm=form.get();
		Date date = new Date();
		
		Registration regi = Registration.findById(vm.id);
		    regi.setLastTimeLoggedIn(vm.lastTimeLoggedIn);
		    regi.setWebsite(vm.website);
		    regi.setActiveUser(vm.activeUser);
    	   regi.setName(vm.name);
    	   regi.setEmail(vm.email);
    	   regi.setPhone(vm.phone);
    	   regi.setBusinessAdd(vm.businessAdd);
    	   regi.setBusinessName(vm.businessName);
    	   regi.setLocation(vm.oneLocation);
    	   regi.setOptions(vm.options);
    	   
    	   regi.setRegistrationDate(date);
    	   //regi.sendDemoFlag = 0;
    	   
    	   regi.update();
   		
    	   //String subject= "Successfully registered";
          //String comments="Successfully registered";
    	 //sendEmail(vm.email,subject,comments);
    	   
		
		return ok();
	}	
	
	public static Result getRegistrList() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<Registration> regi = Registration.getPending();
	    	
	    	return ok(Json.toJson(regi));
    	}	
    }
	
	public static Result getClientList() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<Registration> regi = Registration.getPending();
	    	
	    	return ok(Json.toJson(regi));
    	}	
    }
	
	public static Result getSendDemoLink(Long userId){
		
		Registration regi = Registration.findById(userId);
		
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	Random rnd = new Random();

    	   StringBuilder sb = new StringBuilder( 10 );
    	   for( int i = 0; i < 10; i++ ) 
    	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
    	   	  regi.setTokanNo(sb.toString());
    	   	  
    	  Date curDate = new Date(); 
    	  Calendar calendar = Calendar.getInstance();
    	  
    	  regi.setStartDate(curDate);
    	  calendar.setTime(curDate);
    	  calendar.add(Calendar.DATE, +3);
    	  regi.setExpiryDate(calendar.getTime());
    	  regi.setSendDemoFlag(1);
    	  
		  regi.update();
		  
		  String subject = "Demo site credentials";
		  String comments = "User Demo Key := "+sb.toString()+"\n\n Site URL \n http://www.glider-autos.com:7071/login \n http://www.glider-autos.com/glivr-test/";
			String demoKeyValue=sb.toString();
			//sendEmail(regi.email,subject,comments);
		sendDemoLinkMail(regi.email,demoKeyValue);
		return ok();
	}
	
	public static Result getActiveRegistrList() {
		
		List<AuthUser> regi = AuthUser.getOnlyActiveUser();
    	
    	return ok(Json.toJson(regi));
	}
	
	public static Result getRemoveUser(Long userId) {
		
		Registration regi = Registration.findById(userId);
    	
    	regi.setStatus("Deactive");
    	regi.update();
    	
		return ok();
	}
	
public static Result getStatus(Long userId) {
		
		Registration regi = Registration.findById(userId);
    	Date date = new Date();
    	

/*		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date curDates = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
			Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
			df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
			String date1=df2.format(date);
			try {
				curDates = formatter.parse(date1);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
*/		
    	
    	regi.setClientsince(date);
    	
    	regi.setStatus("Live");
    	regi.update();
    	
		return ok();
	}
	
	/*public static Result getSetActiveUser(Long userId) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = new AuthUser();
	    	
	    	Registration regi = Registration.findById(userId);
	    	
	    	regi.setStatus("Active");
	    	regi.update();
	    	

	    	MyProfile profile=new MyProfile();
	    	List<Permission> permissionList = Permission.getAllPermission();
	    	List<Permission> permissionData = new ArrayList<>();
	    	if(regi.location.equalsIgnoreCase("one")){
	    	    	 Location location=new Location();
	    	    	 location.name=regi.businessName;
	    	    	 location.address=regi.businessAdd;
	    	    	 location.save();
	    	    	 user.location=location;
	    	    	 profile.locations=location;
	    	    	 user.role="Manager";
	    	}
	    		Generate passward
	    		if(regi.location.equalsIgnoreCase("multi")){
	    			user.role="General Manager";
	    		}
	    		
	    		
	    		generate permisssioons
	    		if(user.role.equals("Manager")) {
	     		   for(Permission obj: permissionList) {
	     			   if(!obj.name.equals("Show Location") && !obj.name.equals("Registration")) {
	     				   permissionData.add(obj);
	     			   }
	     		   }
	     		   user.permission = permissionData;
	     	   } 
	    
	     if(user.role.equals("General Manager")) {
	    	   for(Permission obj: permissionList) {
	    		   if(obj.name.equals("CRM") || obj.name.equals("My Profile") || obj.name.equals("Dashboard") || obj.name.equals("Dealer's Profile")) {
	    			   permissionData.add(obj);
	    		   }
	    	   }
	    	   user.permission = permissionData;
	    	   
	     }
	    		
	    		
	     		user.registId = regi.id;
	    		user.firstName=regi.name;
	    		user.email=regi.email;
	    		user.communicationemail = regi.email; 
	    		user.phone=regi.phone;
	    		user.account = "active";
	    		user.save();
	    		profile.address=regi.businessAdd;
	    		profile.myname=regi.businessName;
	    		profile.email=regi.email;
	    		profile.phone=regi.phone;
	    		profile.businessOption=regi.options;
	    		profile.save();
	    		String subject = "confirmation mail";
	           String comment="your registration is confirmed \n Unser Name:"+regi.email+"\n Password:"+regi.password+".";
	    		sendEmail(user.email,subject,comment);
	    	
	    	
	    	
	    	
	    	return ok(Json.toJson(regi));
    	}	
    }*/
	
	public static Result sendEmail(final String email, final String subject ,final String comment) {
		ActorSystem newsLetter = Akka.system();
		newsLetter.scheduler().scheduleOnce(Duration.create(0, TimeUnit.MILLISECONDS), 
				new Runnable() {
			public void run() {
				Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", "smtp.gmail.com");
		 		props.put("mail.smtp.port", "587");
		  
		 		
		 		System.out.println(email);
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUsername, emailPassword);
		 			}
		 		  });
		  
		 		try{
		 			
		 			Message feedback = new MimeMessage(session);
		  			feedback.setFrom(new InternetAddress("glider.autos@gmail.com"));
		  			feedback.setRecipients(Message.RecipientType.TO,
		  			InternetAddress.parse(email));
		  			 feedback.setSubject(subject);	  			
		  			 BodyPart messageBodyPart = new MimeBodyPart();	
		  	         messageBodyPart.setText(comment);	 	    
		  	         Multipart multipart = new MimeMultipart();	  	    
		  	         multipart.addBodyPart(messageBodyPart);	            
		  	         feedback.setContent(multipart);
		  		     Transport.send(feedback);
	    			System.out.println("email send");
		       		} catch (MessagingException e) {
		  			  throw new RuntimeException(e);
		  		}
			}}, newsLetter.dispatcher());
				
		return ok();
	}
	
	
	 private static void sendDemoLinkMail(String email,String demoKey) {
	    	
	    	AuthUser logoUser = getLocalUser();
	    //AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.starttls.enable", "true");
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailUsername, emailPassword);
				}
			});
	    	try
			{
	    		/*InternetAddress[] usersArray = new InternetAddress[2];
	    		int index = 0;
	    		usersArray[0] = new InternetAddress(map.get("email").toString());
	    		usersArray[1] = new InternetAddress(map.get("custEmail").toString());*/
	    		
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(emailUsername));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(email.toString()));
				message.setSubject("Demo site credentials");
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart = new MimeBodyPart();
				
				VelocityEngine ve = new VelocityEngine();
				ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
				ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
				ve.init();
			
				
		        Template t = ve.getTemplate("/public/emailTemplate/demoVersionLinks_html.html"); 
		        VelocityContext context = new VelocityContext();
		         context.put("demoKey",demoKey);
		         context.put("hostnameUrl", imageUrlPath);
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
	    
	 public static Result getCarDetails(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	List <Registration> registrationObjList = Registration.findByCarAndPending();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
		    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
		     	for(Registration vm : registrationObjList){
		     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
		     		RegisterVM vehicle = new RegisterVM();
		     		vehicle.id = vm.id;
		     		vehicle.name=vm.name;
		     		vehicle.email=vm.email;
		     		vehicle.phone=vm.phone;
		     		vehicle.businessName=vm.businessName;
		     		vehicle.businessAdd=vm.businessAdd;
		     		vehicle.options=vm.options;
		     		vehicle.oneLocation = vm.location;
		     		if(vm.activity != null){
			     		vehicle.activity=df.format(vm.activity);
			     		}
		     		else{vehicle.activity="N/A";
		     		}
			    	  	 regVMs.add(vehicle);
		  	}
		     	
		     	return ok(Json.toJson(regVMs));
	    	}	
		}
	
	
	 
	 public static Result getMotorcyclesDetails(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	List <Registration> registrationObjList = Registration.findByMotorcyclesAndPending();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
		    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
		     	for(Registration vm : registrationObjList){
		     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
		     		RegisterVM vehicle = new RegisterVM();
		     		vehicle.id = vm.id;
		     		vehicle.name=vm.name;
		     		vehicle.email=vm.email;
		     		vehicle.phone=vm.phone;
		     		vehicle.businessName=vm.businessName;
		     		vehicle.businessAdd=vm.businessAdd;
		     		vehicle.options=vm.options;
		     		vehicle.oneLocation = vm.location;
		     		if(vm.activity != null){
			     		vehicle.activity=df.format(vm.activity);
			     		}
		     		else{vehicle.activity="N/A";
		     		}
			    	  	 regVMs.add(vehicle);
		  	}
		     	
		     	return ok(Json.toJson(regVMs));
	    	}	
		}
		 public static Result getBoatDetails(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	List <Registration> registrationObjList = Registration.findByBoatAndPending();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
		    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
		     	for(Registration vm : registrationObjList){
		     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
		     		RegisterVM vehicle = new RegisterVM();
		     		vehicle.id = vm.id;
		     		vehicle.name=vm.name;
		     		vehicle.email=vm.email;
		     		vehicle.phone=vm.phone;
		     		vehicle.businessName=vm.businessName;
		     		vehicle.businessAdd=vm.businessAdd;
		     		vehicle.options=vm.options;
		     		vehicle.oneLocation = vm.location;
		     		if(vm.activity != null){
			     		vehicle.activity=df.format(vm.activity);
			     		}
		     		else{vehicle.activity="N/A";
		     		}
			    	  	 regVMs.add(vehicle);
		  	}
		     	
		     	return ok(Json.toJson(regVMs));
	    	}	
		}
		 
		 public static Result getDesignerFurnitureDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByDesignerFurnitureAndPending();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		vehicle.name=vm.name;
			     		vehicle.email=vm.email;
			     		vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		vehicle.businessAdd=vm.businessAdd;
			     		vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     		}
			     		else{vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}
	
		 public static Result getRealStateDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByRealStateAndPending();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		vehicle.name=vm.name;
			     		vehicle.email=vm.email;
			     		vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		vehicle.businessAdd=vm.businessAdd;
			     		vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		//vehicle.activity=vm.activity;
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     		}
			     		else{vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}	
	
		 public static Result getAllAirplanesDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByAirplanesAndPending();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		vehicle.name=vm.name;
			     		vehicle.email=vm.email;
			     		vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		vehicle.businessAdd=vm.businessAdd;
			     		vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.activity != null){
			     		vehicle.activity=df.format(vm.activity);
			     		}
			     		else{vehicle.activity="N/A";
			     		}
			     		regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}	
		 
		 public static Result getAllServiceProviderDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByServiceProviderAndPending();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		vehicle.name=vm.name;
			     		vehicle.email=vm.email;
			     		vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		vehicle.businessAdd=vm.businessAdd;
			     		vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     		}
			     		else{vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}	
		 
		 public static Result getAllLuxuryProductsDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByLuxuryProductsAndPending();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		vehicle.name=vm.name;
			     		vehicle.email=vm.email;
			     		vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		vehicle.businessAdd=vm.businessAdd;
			     		vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     		}
			     		else{vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}
		 
		 public static Result CarsDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByCarAndStatus();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		//vehicle.name=vm.name;
			     		//vehicle.email=vm.email;
			     		//vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		//vehicle.businessAdd=vm.businessAdd;
			     		//vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.clientsince != null){
			     			vehicle.clientsince=df.format(vm.clientsince);
			     		}
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     		}
			     		else{vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}
		 
		 public static Result BoatDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByBoatAndStatus();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		//vehicle.name=vm.name;
			     		//vehicle.email=vm.email;
			     		//vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		//vehicle.businessAdd=vm.businessAdd;
			     		//vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.clientsince != null){
			     			vehicle.clientsince=df.format(vm.clientsince);
			     		}
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     		}
			     		else{vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}
		 
		 public static Result MotorcyclesDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByMotorcyclesAndStatus();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		//vehicle.name=vm.name;
			     		//vehicle.email=vm.email;
			     		//vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		//vehicle.businessAdd=vm.businessAdd;
			     		//vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.clientsince != null){
			     			vehicle.clientsince=df.format(vm.clientsince);
			     		}
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     		}
			     		else{vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}
		 
		 public static Result DesignerFurnitureDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByDesignerFurnitureAndStatus();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		//vehicle.name=vm.name;
			     		//vehicle.email=vm.email;
			     		//vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		//vehicle.businessAdd=vm.businessAdd;
			     		//vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.clientsince != null){
			     			vehicle.clientsince=df.format(vm.clientsince);
			     		}
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     		}
			     		else{vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}
		 
		 public static Result AirplanesDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByAirplanesAndStatus();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		//vehicle.name=vm.name;
			     		//vehicle.email=vm.email;
			     		//vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		//vehicle.businessAdd=vm.businessAdd;
			     		//vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.clientsince != null){
			     			vehicle.clientsince=df.format(vm.clientsince);
			     		}
			     		if(vm.activity != null){
			     		vehicle.activity=df.format(vm.activity);
			     		}
			     		else{vehicle.activity="N/A";
			     		}
			     		regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}	
		 
		 public static Result RealStateDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByRealStateAndStatus();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		//vehicle.name=vm.name;
			     		//vehicle.email=vm.email;
			     		//vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		//vehicle.businessAdd=vm.businessAdd;
			     		//vehicle.options=vm.options;
			     		//vehicle.activity=vm.activity;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.clientsince != null){
			     			vehicle.clientsince=df.format(vm.clientsince);
			     		}
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     		}
			     		else{vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}	
		 
		 public static Result ServiceProviderDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByServiceProviderAndStatus();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		//vehicle.name=vm.name;
			     		//vehicle.email=vm.email;
			     		//vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		//vehicle.businessAdd=vm.businessAdd;
			     		//vehicle.options=vm.options;
			     		vehicle.oneLocation = vm.location;
			     		if(vm.clientsince != null){
			     			vehicle.clientsince=df.format(vm.clientsince);
			     		}
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     	}
			     		else{
			     			vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}	
		 
		 public static Result LuxuryProductsDetails(){
				if(session("USER_KEY") == null || session("USER_KEY") == "") {
		    		return ok(home.render("",userRegistration));
		    	} else {
			    	List <Registration> registrationObjList = Registration.findByLuxuryProductsAndStatus();//getVehiclesByDraftStatusAndLocation(Long.valueOf(session("USER_LOCATION")));
			    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
			    	ArrayList<RegisterVM> regVMs = new ArrayList<>(); 
			     	for(Registration vm : registrationObjList){
			     		//VehicleImage vehicleImg = VehicleImage.getDefaultImage(vm.vin);
			     		RegisterVM vehicle = new RegisterVM();
			     		vehicle.id = vm.id;
			     		//vehicle.name=vm.name;
			     		//vehicle.email=vm.email;
			     		//vehicle.phone=vm.phone;
			     		vehicle.businessName=vm.businessName;
			     		//vehicle.businessAdd=vm.businessAdd;
			     		//vehicle.options=vm.options;
			     		//vehicle.clientsince=df.format(vm.clientsince);
			     		vehicle.oneLocation = vm.location;
			     		if(vm.clientsince != null){
			     			vehicle.clientsince=df.format(vm.clientsince);
			     		}
			     		if(vm.activity != null){
				     		vehicle.activity=df.format(vm.activity);
				     	}
			     		else{
			     			vehicle.activity="N/A";
			     		}
				    	  	 regVMs.add(vehicle);
			  	}
			     	
			     	return ok(Json.toJson(regVMs));
		    	}	
			}
		 
		 
	 public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}

}