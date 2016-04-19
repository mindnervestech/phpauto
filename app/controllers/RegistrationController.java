package controllers;

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
	
	public static Result getRegistrList() {
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
			
		  regi.update();
		  
		  String subject = "Demo site credentials";
		  String comments = "User token No "+sb.toString()+"\n\n Site URL \n http://www.glider-autos.com:7071/login \n http://www.glider-autos.com/glivr-test/";
			
			sendEmail(regi.email,subject,comments);
		
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
	
	
	 public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}

}