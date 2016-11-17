package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

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

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import models.AuthUser;
import models.EmailDetails;
import models.HoursOfOperation;
import models.InternalPdf;
import models.Location;
import models.MyProfile;
import models.Permission;
import models.PhotographerHoursOfOperation;
import models.RequestMoreInfo;
import models.ScheduleTest;
import models.SiteLogo;
import models.TradeIn;
import models.Vehicle;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import securesocial.core.Identity;
import viewmodel.AssignToVM;
import viewmodel.HoursOperation;
import viewmodel.LocationVM;
import viewmodel.UserLeadVM;
import viewmodel.UserVM;
import viewmodel.profileVM;


import views.html.home;

public class MyProfileController extends Controller{

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
	
	
	public static Result getLocationForGM() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		    		
    		Integer totalPrice = 0;
    		List<AuthUser> userList = AuthUser.getUserByType();
    		List<LocationVM> vmList = new ArrayList<>();
    		List<Location> locationList = Location.findAllActiveType();
    		for(Location location : locationList) {
    			totalPrice = 0;
    			LocationVM vm = new LocationVM();
    			vm.id = location.id;
    			vm.locationaddress = location.address;
    			vm.locationemail = location.email;
    			vm.locationName = location.name;
    			vm.locationphone = location.phone;
    			vm.imageName = location.imageName;
    			vm.imageUrl = location.imageUrl;
    			if(location.manager != null){
    				vm.mi = "true";
    			}
    			vm.type = location.type;
    			String roles = "Manager";
    			AuthUser users = AuthUser.getlocationAndManagerByType(location, roles);
    			if(users != null){
	    			vm.managerId = users.id;
	    			vm.email = users.email;
	    			vm.firstName = users.firstName;
	    			vm.lastName = users.lastName;
	    			vm.phone = users.phone;
	    			vm.managerFullName = users.firstName+""+users.lastName;
	    			vm.mImageName = users.imageName;
	    			vm.mImageUrl = users.imageUrl;
    			}
    			
    				vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
	
	public static Result getLocationValueForGM(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
    		int flag = 0;
    		Location location= Location.findManagerType(user);
    		
    		if(location != null){
    			flag = 1;
    		}
    		return ok(Json.toJson(flag));
    	}
    }
	
	 public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}
	 
	 public static Result deactiveLocationById(Long id){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Location loc = Location.findById(id);
	    		if(loc !=null){
	    			loc.setType("deactive");
	    			loc.update();
	    		}
	        	return ok();
	    	}
	    }
	 
	    public static Result uploadLocationImageFile(){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		Form<LocationVM> form = DynamicForm.form(LocationVM.class).bindFromRequest();
	    		
		    	AuthUser userObj = new AuthUser();
		    	LocationVM vm = form.get();

		    	Location loc = new Location();
		    	loc.setEmail(vm.locationemail);
		    	loc.setAddress(vm.locationaddress);
		    	loc.setName(vm.locationName);
		    	loc.setPhone(vm.locationphone);
		    	loc.setType("active");
		    	loc.setCreatedDate(new Date());
		    	loc.save();
		    	
		    	MyProfile mProfile = new MyProfile();
		    	mProfile.setAddress(vm.locationaddress);
		    	mProfile.setMyname(vm.locationName);
		    	mProfile.setEmail(vm.locationemail);
		    	mProfile.setPhone(vm.locationphone);
		    	mProfile.setLocations(Location.findById(loc.id));
		    	mProfile.save();
		    	
		    	 MultipartFormData body = request().body().asMultipartFormData();
		    	   if(body != null) {
		    		FilePart picture = body.getFile("file0");
			    	  if (picture != null) {
			    	    String fileName = picture.getFilename();
			    	    File fdir = new File(rootDir+File.separator+"LocationImg"+File.separator+loc.id);
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = rootDir+File.separator+"LocationImg"+File.separator+loc.id+File.separator+fileName;
			    	    File file = picture.getFile();
			    	    try {
			    	    		FileUtils.moveFile(file, new File(filePath));
			    	    		Location location = Location.findById(loc.id);
			    	    		location.setImageUrl("LocationImg"+"/"+location.id+"/"+fileName);
			    	    		location.setImageName(fileName);
			    	    		location.update();	
			    	    		
			    	  } catch (FileNotFoundException e) {
			  			e.printStackTrace();
				  		} catch (IOException e) {
				  			e.printStackTrace();
				  		} 
			    	  } 
		    	   } 
		    	
	    		return ok(Json.toJson(loc.id));
	    	}
	    }
	    
	    public static Result uploadManagerImageFile(){
	    	
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		 AuthUser users = (AuthUser) getLocalUser();
	    		Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
	    		
		    	AuthUser userObj = new AuthUser();
		    	UserVM vm = form.get();
		    	 List<Permission> permissionList = Permission.getAllPermission();
			     if(vm.mi.equals("true")){
			    	 
			    	   Location location = Location.findById(vm.locationId);
			    	   location.setManager(users);
			    	   location.update();
			    	   
			    	   
			    	   userObj.firstName = users.firstName;
				    	userObj.lastName = users.lastName;
				    	userObj.email = users.email;
				    	userObj.phone = users.phone;
				    	userObj.role = vm.userType;
				    	userObj.location = Location.findById(vm.locationId);
				    	userObj.communicationemail = users.email;
				    	userObj.imageName = users.imageName;
				    	userObj.imageUrl = users.imageUrl;
				    	userObj.account = "active";
				    	
				    	 userObj.password = "0";
				    	 
				    	 if(vm.userType.equals("Manager")) {
				    		   userObj.permission = permissionList;
				    	   } 
			     }else{
			    	  userObj.firstName = vm.firstName;
				    	userObj.lastName = vm.lastName;
				    	userObj.email = vm.email;
				    	userObj.phone = vm.phone;
				    	userObj.role = vm.userType;
				    	userObj.location = Location.findById(vm.locationId);
				    	userObj.communicationemail = vm.email;
				    	userObj.account = "active";
				    	
				    	final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				    	Random rnd = new Random();

				    	   StringBuilder sb = new StringBuilder( 6 );
				    	   for( int i = 0; i < 6; i++ ) 
				    	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
				    	
				    	   userObj.password = sb.toString();
				    	   List<Permission> permissionData = new ArrayList<>();
				    	   if(vm.userType.equals("Manager")) {
				    		   for(Permission obj: permissionList) {
				    			   if(!obj.name.equals("Show Location")) {
				    				   permissionData.add(obj);
				    			   }
				    		   }
				    		   userObj.permission = permissionData;
				    	   } 
				    	  
			     }
		    	
		    	
		    	  
		    	   if(vm.userType.equals("General Manager")) {
		    		   List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
		    			   if(obj.name.equals("CRM") || obj.name.equals("My Profile") || obj.name.equals("Dashboard") || obj.name.equals("Dealer's Profile") || obj.name.equals("My Locations") || obj.name.equals("Deactivate Locations")) {
		    				   permissionData.add(obj);
		    			   }
		    		   }
		    		   userObj.permission = permissionData;
		    	   }
		    	   
		    	   
		    	   if(vm.userType.equals("Sales Person")) {
		    		   List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
		    			   if(!obj.name.equals("Home Page Editing") && !obj.name.equals("Blogs") && !obj.name.equals("My Profile") && !obj.name.equals("Account Settings")) {
		    				   permissionData.add(obj);
		    			   }
		    		   }
		    		   userObj.permission = permissionData;
		    	   }
		    	   
		    	  // if(!vm.mi.equals("true")){
		    		   userObj.save();
		    	   //}
		    	   
		    	   MultipartFormData body = request().body().asMultipartFormData();
		    	   
		    	   if(!vm.mi.equals("true")){
		    	   if(body != null) {
		    		FilePart picture = body.getFile("file0");
			    	  if (picture != null) {
			    	    String fileName = picture.getFilename();
			    	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"userPhoto");
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"userPhoto"+File.separator+fileName;
			    	    File file = picture.getFile();
			    	    try {
			    	    		FileUtils.moveFile(file, new File(filePath));
			    	    		AuthUser user = AuthUser.findById(userObj.id);
			    	    		user.setImageUrl("/"+user.id+"/"+"userPhoto"+"/"+fileName);
			    	    		user.setImageName(fileName);
			    	    		user.update();	
			    	    		
			    	  } catch (FileNotFoundException e) {
			  			e.printStackTrace();
				  		} catch (IOException e) {
				  			e.printStackTrace();
				  		} 
			    	  } 
		    	   } 
		    	   }
		    	   //AuthUser logoUser = AuthUser.findById(userObj.id);
		    	   //SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); //findByUser(logoUser);
		    		
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
			 		props.put("mail.smtp.port", port);
			  
			 		Session session = Session.getInstance(props,
			 		  new javax.mail.Authenticator() {
			 			protected PasswordAuthentication getPasswordAuthentication() {
			 				return new PasswordAuthentication(emailUser, emailPass);
			 			}
			 		  });
			  
			 		try{
			 		   
			  			Message message = new MimeMessage(session);
			    		try{
			  			message.setFrom(new InternetAddress(emailUser,emailName));
			    		}
			    		catch(UnsupportedEncodingException e){
			    			e.printStackTrace();
			    		}
			  		  if(vm.mi.equals("true")){
			  			message.setRecipients(Message.RecipientType.TO,
					  			InternetAddress.parse(users.email));
			  		  }else{
			  			message.setRecipients(Message.RecipientType.TO,
					  			InternetAddress.parse(userObj.email));
			  		  }
			  			
			  			message.setSubject("Your username and password ");	  			
			  			Multipart multipart = new MimeMultipart();
		    			BodyPart messageBodyPart = new MimeBodyPart();
		    			messageBodyPart = new MimeBodyPart();
		    			
		    			VelocityEngine ve = new VelocityEngine();
		    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
		    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
		    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
		    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		    			ve.init();
		    			
		    			Template t = ve.getTemplate("/public/emailTemplate/newUserTemplate.vm"); 
		    	        VelocityContext context = new VelocityContext();
		    	        context.put("hostnameUrl", imageUrlPath);
		    	        //context.put("siteLogo", logo.logoImagePath);
		    	        context.put("username", userObj.email);
		    	        context.put("password", userObj.password);
		    	        StringWriter writer = new StringWriter();
		    	        t.merge( context, writer );
		    	        String content = writer.toString(); 
		    			
		    			messageBodyPart.setContent(content, "text/html");
		    			multipart.addBodyPart(messageBodyPart);
		    			message.setContent(multipart);
		    			Transport.send(message);
			       		} catch (MessagingException e) {
			  			  throw new RuntimeException(e);
			  		}
		    	   
		    	return ok();
	    	}
	    }
	    
	    public static Result updateUploadLocationImageFile() {
	 	   
	 	   if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	 	        		Form<LocationVM> form = DynamicForm.form(LocationVM.class).bindFromRequest();
	 	        		
	 	    	    	AuthUser userObj = new AuthUser();
	 	    	    	LocationVM vm = form.get();
	 	    	    	Location loc = Location.findById((long)vm.id);
	 	    	    	if(loc != null){
	 	    	    		loc.setEmail(vm.locationemail);
	 		    	    	loc.setAddress(vm.locationaddress);
	 		    	    	loc.setName(vm.locationName);
	 		    	    	loc.setPhone(vm.locationphone);
	 		    	    	
	 		    	    	loc.update();
	 		    	    	
	 		    	    	/*List<MyProfile> mProfile = MyProfile.findByLocation(Long.valueOf(vm.id));
	 		    	    	mProfile.setAddress(vm.locationemail);
	 		    	    	mProfile.setMyname(vm.locationName);
	 		    	    	mProfile.setEmail(vm.locationemail);
	 		    	    	mProfile.setPhone(vm.locationphone);
	 		    	    	mProfile.update();*/

	 		    	    	
	 		    	    	 MultipartFormData body = request().body().asMultipartFormData();
	 		  	    	   if(body != null) {
	 		  	    	   File file1 = new File(rootDir+loc.imageUrl);
	 		  	    	   file1.delete();
	 		  	    		FilePart picture = body.getFile("file0");
	 		  		    	  if (picture != null) {
	 		  		    	    String fileName = picture.getFilename();
	 		  		    	    File fdir = new File(rootDir+File.separator+"LocationImg"+File.separator+loc.id);
	 		  		    	    if(!fdir.exists()) {
	 		  		    	    	fdir.mkdir();
	 		  		    	    }
	 		  		    	    String filePath = rootDir+File.separator+"LocationImg"+File.separator+loc.id+File.separator+fileName;
	 		  		    	    File file = picture.getFile();
	 		  		    	    try {
	 		  		    	    	FileUtils.moveFile(file, new File(filePath));
	 			    	    		Location location = Location.findById(loc.id);
	 			    	    		location.setImageUrl("LocationImg"+"/"+location.id+"/"+fileName);
	 			    	    		location.setImageName(fileName);
	 			    	    		location.update();	
	 		  		    	    		
	 		  		    	  } catch (FileNotFoundException e) {
	 		  		  			e.printStackTrace();
	 		  			  		} catch (IOException e) {
	 		  			  			e.printStackTrace();
	 		  			  		} 
	 		  		    	  } 
	 		  	    	   }
	    					}
	 	        		return ok(Json.toJson(loc.id));
	    	} 	
	 	   
	 	 
	    }
	    
	    public static Result UpdateuploadManagerImageFile(){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
		    	String flag = "0";
		    	UserVM vm = form.get();
		    	String roles = "Manager";
		    	AuthUser userObj = null;
		    	if(vm.mi.equals("true")){
		    		
		    		AuthUser uAuthUser = null;
		    		if(vm.locationId != null){
		    			uAuthUser = AuthUser.getlocationAndManagerOne(Location.findById(vm.locationId));
		    		}
		    		 //AuthUser uAuthUser = AuthUser.getlocationAndManagerOne(Location.findById(vm.locationId));
			    	if(uAuthUser != null){
			    		 uAuthUser.setRole("Sales Person");
				    	 uAuthUser.update();
			    	}
		    		 AuthUser users = AuthUser.getOnlyGM();
		    		
		    		    userObj = new AuthUser();
			    	    userObj.firstName = users.firstName;
				    	userObj.lastName = users.lastName;
				    	userObj.email = users.email;
				    	userObj.phone = users.phone;
				    	userObj.role = "Manager";
				    	userObj.location = Location.findById(vm.locationId);
				    	userObj.communicationemail = users.email;
				    	userObj.imageName = users.imageName;
				    	userObj.imageUrl = users.imageUrl;
				    	userObj.account = "active";
				    	
				    	 userObj.password = "0";
				    	 
				    	 List<Permission> permissionList = Permission.getAllPermission();
				    	 userObj.permission = permissionList;
				    	 
				    	 userObj.save();
				    	 
				    	 Location location = Location.findById(vm.locationId);
				    	 location.setManager(AuthUser.findById(users.id));
				    	 location.update();
				    	 
				    	
				    	 
				    	 
		    	}else{
		    		AuthUser uAuthUser = null;
		    		if(vm.locationId != null){
		    			uAuthUser = AuthUser.getlocationAndManagerOne(Location.findById(vm.locationId));
		    		}
		    		 //AuthUser uAuthUser = AuthUser.getlocationAndManagerOne(Location.findById(vm.locationId));
		    		
		    		 if(uAuthUser != null){
		    			 List<RequestMoreInfo> rInfo = RequestMoreInfo.findAllAssignedLeadsToUser1(uAuthUser);
		    			 List<ScheduleTest> sList = ScheduleTest.findAllAssignedLeadsToUser1(uAuthUser);
		    			 List<TradeIn> tIns = TradeIn.findAllAssignedLeadsToUser1(uAuthUser);
		    			 
		    			 if(rInfo.size() != 0 || sList.size() != 0 || tIns.size() != 0){
		    				 flag = "1";
		    			 }else{
		    				 uAuthUser.setAccount("deactive");
					    	 uAuthUser.update();
					    	 
					    	 List<Permission> permissionList = Permission.getAllPermission();
					    	 userObj = new AuthUser();
					    	 userObj.firstName = vm.firstName;
						    	userObj.lastName = vm.lastName;
						    	userObj.email = vm.email;
						    	userObj.phone = vm.phone;
						    	userObj.role = vm.userType;
						    	userObj.location = Location.findById(vm.locationId);
						    	userObj.communicationemail = vm.email;
						    	userObj.account = "active";
						    	
						    	final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
						    	Random rnd = new Random();

						    	   StringBuilder sb = new StringBuilder( 6 );
						    	   for( int i = 0; i < 6; i++ ) 
						    	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
						    	
						    	   userObj.password = sb.toString();
						    	   /*List<Permission> permissionData = new ArrayList<>();
						    		   for(Permission obj: permissionList) {
						    			   if(obj.name.equals("Show Location")) {
						    				   permissionData.add(obj);
						    			   }
						    		   }
						    		   userObj.permission = permissionData;*/
						    	   userObj.permission = permissionList;
						    	   userObj.save();
						    	   
						    	   Location location = Location.findById(vm.locationId);
							       location.setManager(null);
							   location.update();
		    			 }
		    			 
				    	 
		    		 }else{
		    			 userObj = AuthUser.findById(vm.id);
		 		    	
		 		    	userObj.setFirstName(vm.firstName);
		 		    	userObj.setLastName(vm.lastName);
		 		    	userObj.setEmail(vm.email);
		 		    	userObj.setPhone(vm.phone);
		 		    	userObj.setCommunicationemail(vm.email);
		 		    	userObj.setAccount("active");
		 	    	//userObj.set = Location.findById(vm.locationId);
		 	    
		 	    	
		 	    	final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		 	    	Random rnd = new Random();

		 	    	   StringBuilder sb = new StringBuilder( 6 );
		 	    	   for( int i = 0; i < 6; i++ ) 
		 	    	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		 	    	
		 	    	  // userObj.password = sb.toString();
		 	    	
		 	    	   userObj.update();
		    		 }
		    	   
		    	} 
		    	   
		    	   MultipartFormData body = request().body().asMultipartFormData();
		    	   
		    	   if(flag.equals("0")){
		    		   if(body != null) {
		   	    		FilePart picture = body.getFile("file0");
		   		    	  if (picture != null) {
		   		    	    String fileName = picture.getFilename();
		   		    	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"userPhoto");
		   		    	    if(!fdir.exists()) {
		   		    	    	fdir.mkdir();
		   		    	    }
		   		    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"userPhoto"+File.separator+fileName;
		   		    	    File file = picture.getFile();
		   		    	    try {
		   		    	    		FileUtils.moveFile(file, new File(filePath));
		   		    	    		AuthUser user = AuthUser.findById(userObj.id);
		   		    	    		user.setImageUrl("/"+user.id+"/"+"userPhoto"+"/"+fileName);
		   		    	    		user.setImageName(fileName);
		   		    	    		user.update();	
		   		    	    		
		   		    	  } catch (FileNotFoundException e) {
		   		  			e.printStackTrace();
		   			  		} catch (IOException e) {
		   			  			e.printStackTrace();
		   			  		} 
		   		    	  } 
		   	    	   } 
		    	   }
		    	  
		    	   
		    	return ok(Json.toJson(flag));
	    	}
	    }
	    
	    public static Result checkEmailOfUser(String email ){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
			   	return ok(home.render("",userRegistration));
			   	} else {
				   	String msg;
				   	AuthUser userObj = AuthUser.findByEmail(email);
				   	if(userObj !=null){
					   	msg = "Email already exists";
					   	return ok(msg);
				   	}
				   	msg = "";
				   	return ok(msg);
			   	}
			}
	    
	    public static Result getDeactiveLocationForGM() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		List<AuthUser> userList = AuthUser.getUserByType();
	    		List<LocationVM> vmList = new ArrayList<>();
	    		List<Location> locationList = Location.findAllDeactiveType();
	    		for(Location location : locationList) {
	    			LocationVM vm = new LocationVM();
	    			vm.id = location.id;
	    			vm.locationaddress = location.address;
	    			vm.locationemail = location.email;
	    			vm.locationName = location.name;
	    			vm.locationphone = location.phone;
	    			vm.imageName = location.imageName;
	    			vm.imageUrl = location.imageUrl;
	    			vm.type = location.type;
	    			String roles = "Manager";
	    			AuthUser users = AuthUser.getlocationAndManagerByType(location, roles);
	    			if(users != null){
		    			vm.managerId = users.id;
		    			vm.email = users.email;
		    			vm.firstName = users.firstName;
		    			vm.lastName = users.lastName;
		    			vm.phone = users.phone;
		    			vm.managerFullName = users.firstName+""+users.lastName;
		    			vm.mImageName = users.imageName;
		    			vm.mImageUrl = users.imageUrl;
	    			}
	    			
	    				vmList.add(vm);
	    		}
	    		return ok(Json.toJson(vmList));
	    	}
	    }
	    
	    public static Result getUserRole(){
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	return ok(Json.toJson(userObj));
	    }
	    
	    public static Result getDealerProfile(){
			Map<String,Object> map = new HashMap<>();
			AuthUser user = getLocalUser();
			MyProfile mProfile = MyProfile.findByUser(user);
			map.put("user", user);
			map.put("dealer", mProfile);
			return ok(Json.toJson(map));
		}
	    
	    public static Result getMyProfile() {
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	//MyProfile mpObj = MyProfile.findByUser(userObj);
	    	List<MyProfile> mpObj = MyProfile.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	profileVM vm = new profileVM();
	    	if(mpObj.size() != 0){
	    		
	    	vm.yelp=mpObj.get(0).yelp;
	    	vm.address = mpObj.get(0).address;
	    	vm.myname = mpObj.get(0).myname;
	    	vm.city = mpObj.get(0).city;
	    	vm.country = mpObj.get(0).country;
	    	vm.dealer_id = mpObj.get(0).dealer_id;
	    	vm.email = mpObj.get(0).email;
	    	vm.facebook = mpObj.get(0).facebook;
	    	vm.googleplus = mpObj.get(0).googleplus;
	    	vm.instagram= mpObj.get(0).instagram;
	    	vm.phone = mpObj.get(0).phone;
	    	vm.pinterest = mpObj.get(0).pinterest;
	    	vm.state = mpObj.get(0).state;
	    	vm.twitter = mpObj.get(0).twitter;
	    	vm.web = mpObj.get(0).web;
	    	vm.zip = mpObj.get(0).zip;
	    	
	    	}
	    	return ok(Json.toJson(vm));
	    }
	    
	    public static Result getSaleHourData(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		List<HoursOfOperation>list=HoursOfOperation.findByType();
	    		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	    		List <HoursOperation> alist=new ArrayList<>();
	    		HoursOperation vm=new HoursOperation();
	    		for (HoursOfOperation op : list) {
	    			
	    			String day=(String)op.getDay();
	    			if(day != null){
	    				if(day.equalsIgnoreCase("Sunday")){
	    					if(op.getOpenTime() != null){
	    					vm.sunOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.sunCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.sunFlag=op.getDayFlag();
	    					if(vm.sunFlag == 1){
	    						vm.sunOpen=true;
	    					}
	    					System.out.println(">>>>");
	    					System.out.println(vm.satOpenTime);
	    					System.out.println(vm.satCloseTime);
	    					System.out.println(vm.sunFlag);
	    				}
	    				if(day.equalsIgnoreCase("Monday")){
	    					if(op.getOpenTime() != null){
	    					vm.monOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.monCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.monFlag=op.getDayFlag();
	    					if(vm.monFlag == 1){
	    						vm.monOpen=true;
	    					}
	    					
	    					
	    				}
	    				
	    				if(day.equalsIgnoreCase("Tuesday")){
	    					if(op.getOpenTime() != null){
	    					vm.tueOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.tueCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.tueFlag=op.getDayFlag();
	    					
	    					if(vm.tueFlag == 1){
	    						vm.tueOpen=true;
	    					}
	    					
	    				}
	    				if(day.equalsIgnoreCase("Wednesday")){
	    					if(op.getOpenTime() != null){
	    					vm.wedOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.wedCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.wedFlag=op.getDayFlag();
	    					if(vm.wedFlag == 1){
	    						vm.wedOpen=true;
	    					}
	    					
	    				}
	    				if(day.equalsIgnoreCase("Thursday")){
	    					if(op.getOpenTime() != null){
	    					vm.thuOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.thuCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.thuFlag=op.getDayFlag();
	    					if(vm.thuFlag == 1){
	    						vm.thuOpen=true;
	    					}
	    				}
	    				if(day.equalsIgnoreCase("Friday")){
	    					if(op.getOpenTime() != null){
	    					vm.friOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.friCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.friFlag=op.getDayFlag();
	    					if(vm.friFlag == 1){
	    						vm.friOpen=true;
	    					}
	    				}
	    				if(day.equalsIgnoreCase("Saturday")){
	    					if(op.getOpenTime() != null){
	    					vm.satOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.satCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.satFlag=op.getDayFlag();
	    					if(vm.satFlag == 1){
	    						vm.satOpen=true;
	    					}
	    				}
	    			}
	    		}
	    		alist.add(vm);
	    		return ok(Json.toJson(alist));
	    	}
		}
	    
	    public static Result getSaleHourDataForService(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		List<HoursOfOperation>list=HoursOfOperation.findByTypeForServices();
	    		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	    		List <HoursOperation> alist=new ArrayList<>();
	    		HoursOperation vm=new HoursOperation();
	    		for (HoursOfOperation op : list) {
	    			
	    			String day=(String)op.getDay();
	    			if(day != null){
	    				if(day.equalsIgnoreCase("Sunday")){
	    					if(op.getOpenTime() != null){
	    					vm.sunOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.sunCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.sunFlag=op.getDayFlag();
	    					if(vm.sunFlag == 1){
	    						vm.sunOpen=true;
	    					}
	    					vm.checkForSunday=op.getCheckValue();
	    					if(vm.checkForSunday == 1){
	    						vm.serviceCheck=true;
	    					}
	    					System.out.println(">>>>");
	    					System.out.println(vm.satOpenTime);
	    					System.out.println(vm.satCloseTime);
	    					System.out.println(vm.sunFlag);
	    				}
	    				if(day.equalsIgnoreCase("Monday")){
	    					if(op.getOpenTime() != null){
	    					vm.monOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.monCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.monFlag=op.getDayFlag();
	    					if(vm.monFlag == 1){
	    						vm.monOpen=true;
	    					}
	    					
	    					vm.checkForMonday=op.getCheckValue();
	    					if(vm.checkForSunday == 1){
	    						vm.serviceCheck=true;
	    					}
	    					
	    					
	    				}
	    				
	    				if(day.equalsIgnoreCase("Tuesday")){
	    					if(op.getOpenTime() != null){
	    					vm.tueOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.tueCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.tueFlag=op.getDayFlag();
	    					
	    					if(vm.tueFlag == 1){
	    						vm.tueOpen=true;
	    					}
	    					
	    				}
	    				if(day.equalsIgnoreCase("Wednesday")){
	    					if(op.getOpenTime() != null){
	    					vm.wedOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.wedCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.wedFlag=op.getDayFlag();
	    					if(vm.wedFlag == 1){
	    						vm.wedOpen=true;
	    					}
	    				}
	    				if(day.equalsIgnoreCase("Thursday")){
	    					if(op.getOpenTime() != null){
	    					vm.thuOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.thuCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.thuFlag=op.getDayFlag();
	    					if(vm.thuFlag == 1){
	    						vm.thuOpen=true;
	    					}
	    				}
	    				if(day.equalsIgnoreCase("Friday")){
	    					if(op.getOpenTime() != null){
	    					vm.friOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.friCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.friFlag=op.getDayFlag();
	    					if(vm.friFlag == 1){
	    						vm.friOpen=true;
	    					}
	    				}
	    				if(day.equalsIgnoreCase("Saturday")){
	    					if(op.getOpenTime() != null){
	    					vm.satOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.satCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.satFlag=op.getDayFlag();
	    					if(vm.satFlag == 1){
	    						vm.satOpen=true;
	    					}
	    				}
	    			}
	    		}
	    		alist.add(vm);
	    		return ok(Json.toJson(alist));
	    	}
		}
	    
	    public static Result getSaleHourDataForParts(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		List<HoursOfOperation>list=HoursOfOperation.findByTypeForParts();
	    		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	    		List <HoursOperation> alist=new ArrayList<>();
	    		HoursOperation vm=new HoursOperation();
	    		for (HoursOfOperation op : list) {
	    			
	    			String day=(String)op.getDay();
	    			if(day != null){
	    				if(day.equalsIgnoreCase("Sunday")){
	    					if(op.getOpenTime() != null){
	    					vm.sunOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.sunCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.sunFlag=op.getDayFlag();
	    					if(vm.sunFlag == 1){
	    						vm.sunOpen=true;
	    					}
	    					vm.checkForSunday=op.getCheckValue();
	    					if(vm.checkForSunday == 1){
	    						vm.serviceCheck=true;
	    					}
	    					System.out.println(">>>>");
	    					System.out.println(vm.satOpenTime);
	    					System.out.println(vm.satCloseTime);
	    					System.out.println(vm.sunFlag);
	    				}
	    				if(day.equalsIgnoreCase("Monday")){
	    					if(op.getOpenTime() != null){
	    					vm.monOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.monCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.monFlag=op.getDayFlag();
	    					if(vm.monFlag == 1){
	    						vm.monOpen=true;
	    					}
	    					
	    					vm.checkForMonday=op.getCheckValue();
	    					if(vm.checkForMonday == 1){
	    						vm.serviceCheck=true;
	    					}
	    					
	    					
	    				}
	    				
	    				if(day.equalsIgnoreCase("Tuesday")){
	    					if(op.getOpenTime() != null){
	    					vm.tueOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.tueCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					
	    					vm.tueFlag=op.getDayFlag();
	    					
	    					if(vm.tueFlag == 1){
	    						vm.tueOpen=true;
	    					}
	    					
	    				}
	    				if(day.equalsIgnoreCase("Wednesday")){
	    					if(op.getOpenTime() != null){
	    					vm.wedOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.wedCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					
	    					vm.wedFlag=op.getDayFlag();
	    					if(vm.wedFlag == 1){
	    						vm.wedOpen=true;
	    					}
	    				}
	    				if(day.equalsIgnoreCase("Thursday")){
	    					if(op.getOpenTime() != null){
	    					vm.thuOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.thuCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.thuFlag=op.getDayFlag();
	    					if(vm.thuFlag == 1){
	    						vm.thuOpen=true;
	    					}
	    				}
	    				if(day.equalsIgnoreCase("Friday")){
	    					if(op.getOpenTime() != null){
	    					vm.friOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.friCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.friFlag=op.getDayFlag();
	    					if(vm.friFlag == 1){
	    						vm.friOpen=true;
	    					}
	    				}
	    				if(day.equalsIgnoreCase("Saturday")){
	    					if(op.getOpenTime() != null){
	    					vm.satOpenTime=dateFormat.format((Date)op.getOpenTime());
	    					}
	    					if(op.getCloseTime() != null){
	    					vm.satCloseTime=dateFormat.format((Date)op.getCloseTime());
	    					}
	    					vm.satFlag=op.getDayFlag();
	    					if(vm.satFlag == 1){
	    						vm.satOpen=true;
	    					}
	    				}
	    			}
	    		}
	    		alist.add(vm);
	    		return ok(Json.toJson(alist));
	    	}
		}
	    
	    public static Result myprofile() {
	    	
	    	Form<profileVM> form = DynamicForm.form(profileVM.class).bindFromRequest();
	    	profileVM vm = form.get();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	MyProfile mpObj = MyProfile.findByUser(userObj);
	    	if(mpObj != null) {
	    		mpObj.setMyname(vm.myname);
	    		mpObj.setDealer_id(vm.dealer_id);
	    		mpObj.setLatlong(vm.latlong);
	    		mpObj.setAddress(vm.address);
	    		mpObj.setCity(vm.city);
	    		mpObj.setState(vm.state);
	    		mpObj.setZip(vm.zip);
	    		mpObj.setCountry(vm.country);
	    		mpObj.setPhone(vm.phone);
	    		mpObj.setEmail(vm.email);
	    		mpObj.setWeb(vm.web);
	    		mpObj.setFacebook(vm.facebook);
	    		mpObj.setTwitter(vm.twitter);
	    		mpObj.setYelp(vm.yelp);
	    		mpObj.setPinterest(vm.pinterest);
	    		if(session("USER_LOCATION") != null){
	    			mpObj.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    		}
	    		mpObj.setInstagram(vm.instagram);
	    		mpObj.setGoogleplus(vm.googleplus);
	    		mpObj.update();
	    	}else{
	    		mpObj = new MyProfile();
	    		mpObj.setMyname(vm.myname);
	    		mpObj.setDealer_id(vm.dealer_id);
	    		mpObj.setLatlong(vm.latlong);
	    		mpObj.setAddress(vm.address);
	    		mpObj.setCity(vm.city);
	    		mpObj.setState(vm.state);
	    		mpObj.setZip(vm.zip);
	    		mpObj.setYelp(vm.yelp);
	    		mpObj.setCountry(vm.country);
	    		mpObj.setPhone(vm.phone);
	    		mpObj.setEmail(vm.email);
	    		mpObj.setWeb(vm.web);
	    		mpObj.setFacebook(vm.facebook);
	    		mpObj.setTwitter(vm.twitter);
	    		if(session("USER_LOCATION") != null){
	    			mpObj.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    		}
	    		mpObj.setPinterest(vm.pinterest);
	    		mpObj.setInstagram(vm.instagram);
	    		mpObj.setGoogleplus(vm.googleplus);
	    		mpObj.user = (AuthUser) getLocalUser();
	    		
	    		mpObj.save();
	    	}
	    	
	    	
	    	if(session("USER_LOCATION") != null){
	    		Location loc = Location.findById(Long.parseLong(session("USER_LOCATION")));
	        	
	        	if(loc != null){
	        		loc.setEmail(vm.email);
	            	loc.setAddress(vm.address);
	            	loc.setName(vm.myname);
	            	loc.setPhone(vm.phone);
	            	loc.update();

	            	
	            	 MultipartFormData body = request().body().asMultipartFormData();
	          	   if(body != null) {
	          	   File file1 = new File(rootDir+loc.imageUrl);
	          	   file1.delete();
	          		FilePart picture = body.getFile("file0");
	        	    	  if (picture != null) {
	        	    	    String fileName = picture.getFilename();
	        	    	    File fdir = new File(rootDir+File.separator+"LocationImg"+File.separator+loc.id);
	        	    	    if(!fdir.exists()) {
	        	    	    	fdir.mkdir();
	        	    	    }
	        	    	    String filePath = rootDir+File.separator+"LocationImg"+File.separator+loc.id+File.separator+fileName;
	        	    	    File file = picture.getFile();
	        	    	    try {
	        	    	    	FileUtils.moveFile(file, new File(filePath));
	        	    		Location location = Location.findById(loc.id);
	        	    		location.setImageUrl("LocationImg"+"/"+location.id+"/"+fileName);
	        	    		location.setImageName(fileName);
	        	    		location.update();	
	        	    	    		
	        	    	  } catch (FileNotFoundException e) {
	        	  			e.printStackTrace();
	        		  		} catch (IOException e) {
	        		  			e.printStackTrace();
	        		  		} 
	        	    	 } 
	          	   	}
	        	}
	    	}
	    	userObj.communicationemail = vm.email;
	    	userObj.update();
			return ok();
	    }
	    
	    public static Result saveSalesHoursForParts(){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		MultipartFormData body = request().body().asMultipartFormData();
		    	Form<HoursOperation> form = DynamicForm.form(HoursOperation.class).bindFromRequest();
		    	HoursOperation vm = form.get();
	    		AuthUser user = getLocalUser();
		    	Date currDate = new Date();
		    	
		    	
		    		HoursOfOperation operation=HoursOfOperation.findByDayForParts("Sunday");
		    		if(operation == null){
		    			if(vm.sunOpen == null || vm.sunOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Sunday";
		    		try {
		    			if(vm.sunOpenTime!= "" ||vm.sunCloseTime!= ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.sunOpenTime);
					
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.sunCloseTime);
		    		
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				op.typeOfOperation=vm.typeOfOperation;
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.sunOpen == null || vm.sunOpen == false){
		    				
		    				try {
								operation.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.sunOpenTime));
		    				operation.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.sunCloseTime));
		    				operation.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation.update();
		    			}else{
		    				operation.setDayFlag(1);
		    				operation.update();
		    			}
		    		}
		    		HoursOfOperation operation1=HoursOfOperation.findByDayForParts("Monday");
		    		if(operation1 == null){
		    			if(vm.monOpen == null || vm.monOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Monday";
		    		try {
		    			
		    			if(vm.monOpenTime != "" || vm.monCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.monOpenTime);
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.monCloseTime);
		    		}
		    		op.dayFlag = 0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(); 
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.monOpen == null || vm.monOpen == false){
		    				
		    				try {
								operation1.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.monOpenTime));
		    				operation1.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.monCloseTime));
		    				operation1.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation1.update();
		    			}else{
		    				operation1.setDayFlag(1);
		    				operation1.update();
		    			}
		    		}
		    		HoursOfOperation operation2=HoursOfOperation.findByDayForParts("Tuesday");
		    		if(operation2 == null){
		    			if(vm.tueOpen == null || vm.tueOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Tuesday";
		    		try {
		    			if(vm.tueOpenTime != "" ||   vm.tueCloseTime!= ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.tueOpenTime);
					
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.tueCloseTime);
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.tueOpen == null || vm.tueOpen == false){
		    				
		    				try {
								operation2.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.tueOpenTime));
		    				operation2.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.tueCloseTime));
		    				operation2.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation2.update();
		    			}else{
		    				operation2.setDayFlag(1);
		    				operation2.update();
		    			}
		    		}
		    		HoursOfOperation operation3=HoursOfOperation.findByDayForParts("Wednesday");
		    		if(operation3 == null){
		    			if(vm.wedOpen == null || vm.wedOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Wednesday";
		    		try {
		    			
		    			if(vm.wedOpenTime != ""  || vm.wedCloseTime!= ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.wedOpenTime);
					
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.wedCloseTime);
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.wedOpen == null || vm.wedOpen == false){
		    				
		    				try {
								operation3.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.wedOpenTime));
		    				operation3.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.wedCloseTime));
		    				operation3.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation3.update();
		    			}else{
		    				operation3.setDayFlag(1);
		    				operation3.update();
		    			}
		    		}
		    		HoursOfOperation operation4=HoursOfOperation.findByDayForParts("Thursday");
		    		if(operation4 == null){
		    			if(vm.thuOpen == null || vm.thuOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Thursday";
		    		try {
		    			
		    			if(vm.thuOpenTime != "" ||  vm.thuCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.thuOpenTime);
					
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.thuCloseTime);
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;  	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.thuOpen == null || vm.thuOpen == false){
		    				
		    				try {
								operation4.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.thuOpenTime));
		    				operation4.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.thuCloseTime));
		    				operation4.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation4.update();
		    			}else{
		    				operation4.setDayFlag(1);
		    				operation4.update();
		    			}
		    		}
		    		HoursOfOperation operation5=HoursOfOperation.findByDayForParts("Friday");
		    		if(operation5 == null){
		    			if(vm.friOpen == null || vm.friOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Friday";
		    		try {
		    			if(vm.friOpenTime != "" ||  vm.friCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.friOpenTime);
					
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.friCloseTime);
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;  	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.friOpen == null || vm.friOpen == false){
		    				
		    				try {
								operation5.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.friOpenTime));
		    				operation5.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.friCloseTime));
		    				operation5.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation5.update();
		    			}else{
		    				operation5.setDayFlag(1);
		    				operation5.update();
		    			}
		    		}
		    		HoursOfOperation operation6=HoursOfOperation.findByDayForParts("Saturday");
		    		if(operation6 == null){
		    			if(vm.satOpen == null || vm.satOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Saturday";
		    		try {
		    			
		    			if(vm.satOpenTime != "" ||  vm.satCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.satOpenTime);
					
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.satCloseTime);
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.satOpen == null  ||vm.satOpen == false ){
		    				
		    				try {
								operation6.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.satOpenTime));
		    				operation6.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.satCloseTime));
		    				operation6.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation6.update();
		    			}
		    			else{
		    				operation6.setDayFlag(1);
		    				operation6.update();
		    			}
		    		}
		    	return ok();
	    	}	
		}
	    
	    public static Result saveHours(){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		MultipartFormData body = request().body().asMultipartFormData();
		    	Form<HoursOperation> form = DynamicForm.form(HoursOperation.class).bindFromRequest();
		    	HoursOperation vm = form.get();
	    		AuthUser user = getLocalUser();
		    	Date currDate = new Date();
			    	HoursOfOperation operation=HoursOfOperation.findByDay("Sunday");
		    		if(operation == null){
		    			if(vm.sunOpen == null || vm.sunOpen == false){
		    				HoursOfOperation op=new HoursOfOperation();
		    				op.day="Sunday";
		    		try {
		    			if(vm.sunOpenTime != "" ||  vm.sunCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.sunOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.sunCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				op.typeOfOperation=vm.typeOfOperation;
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    	}
		    		else{
		    			if(vm.sunOpen == null || vm.sunOpen == false){
		    				
		    				try {
								operation.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.sunOpenTime));
		    				operation.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.sunCloseTime));
		    				operation.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation.update();
		    			}else{
		    				operation.setDayFlag(1);
		    				operation.update();
		    			}
		    		}
		    		HoursOfOperation operation1=HoursOfOperation.findByDay("Monday");
		    		if(operation1 == null){
		    			if(vm.monOpen == null || vm.monOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.day="Monday";
		    		try {
		    			if(vm.monOpenTime != "" ||  vm.monCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.monOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.monCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.monOpen == null || vm.monOpen == false){
		    				
		    				try {
								operation1.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.monOpenTime));
		    				operation1.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.monCloseTime));
		    				operation1.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation1.update();
		    			}else{
		    				operation1.setDayFlag(1);
		    				operation1.update();
		    			}
		    		}
		    		HoursOfOperation operation2=HoursOfOperation.findByDay("Tuesday");
		    		if(operation2 == null){
		    			if(vm.tueOpen == null || vm.tueOpen == false){
		    				HoursOfOperation op=new HoursOfOperation();
		    				op.day="Tuesday";
		    		try {
		    			if(vm.tueOpenTime != "" ||  vm.tueCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.tueOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.tueCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    			op.typeOfOperation=vm.typeOfOperation;	
		    			op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.tueOpen == null || vm.tueOpen == false){
		    				
		    				try {
								operation2.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.tueOpenTime));
		    				operation2.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.tueCloseTime));
		    				operation2.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation2.update();
		    			}else{
		    				operation2.setDayFlag(1);
		    				operation2.update();
		    			}
		    			
		    			
		    			
		    		}
		    	
		    		HoursOfOperation operation3=HoursOfOperation.findByDay("Wednesday");
		    		if(operation3 == null){
		    			if(vm.wedOpen == null || vm.wedOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Wednesday";
		    		try {
		    			if(vm.wedOpenTime != "" ||  vm.wedCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.wedOpenTime);
					
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.wedCloseTime);
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.wedOpen == null || vm.wedOpen == false){
		    				
		    				try {
								operation3.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.wedOpenTime));
		    				operation3.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.wedCloseTime));
		    				operation3.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation3.update();
		    			}else{
		    				operation3.setDayFlag(1);
		    				operation3.update();
		    			}
			    	}
		    		HoursOfOperation operation4=HoursOfOperation.findByDay("Thursday");
		    		if(operation4 == null){
		    			if(vm.thuOpen == null || vm.thuOpen == false){
		    				HoursOfOperation op=new HoursOfOperation();
		    				op.day="Thursday";
		    		try {
		    			if(vm.thuOpenTime != "" ||  vm.thuCloseTime != ""){
		    				op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.thuOpenTime);
					
		    				op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.thuCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;  	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.thuOpen == null || vm.thuOpen == false){
		    				
		    				try {
								operation4.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.thuOpenTime));
		    				operation4.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.thuCloseTime));
		    				operation4.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation4.update();
		    			}else{
		    				operation4.setDayFlag(1);
		    				operation4.update();
		    			}
		    		}
		    		HoursOfOperation operation5=HoursOfOperation.findByDay("Friday");
		    		if(operation5 == null){
		    			if(vm.friOpen == null || vm.friOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Friday";
		    		try {
		    			if(vm.friOpenTime != "" ||  vm.friCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.friOpenTime);
					
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.friCloseTime);
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;  	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    	}
		    		else{
		    			if(vm.friOpen == null || vm.friOpen == false){
		    				
		    				try {
								operation5.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.friOpenTime));
		    				operation5.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.friCloseTime));
		    				operation5.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation5.update();
		    			}else{
		    				operation5.setDayFlag(1);
		    				operation5.update();
		    			}
		    		}
		    		HoursOfOperation operation6=HoursOfOperation.findByDay("Saturday");
		    		if(operation6 == null){
		    			if(vm.satOpen == null || vm.satOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Saturday";
		    		try {
		    			if(vm.satOpenTime != "" ||  vm.satCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.satOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.satCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.satOpen == null  ||vm.satOpen == false ){
		    				
		    				try {
								operation6.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.satOpenTime));
		    				operation6.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.satCloseTime));
		    				operation6.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation6.update();
		    			}else{
		    				operation6.setDayFlag(1);
		    				operation6.update();
		    			}
		    		}
		    	return ok();
	    	}	
		}
	    
	    public static Result checkForServiceForPart(int checkForServiceValue){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		System.out.println(">>>>>>>>>>>>>>>>>>>"+checkForServiceValue);
	    		List<HoursOfOperation>list=HoursOfOperation.findByTypeForParts();
	    		for (HoursOfOperation op : list) {
	    			if(checkForServiceValue == 0){
	    			op.setCheckValue(0);
	    			
	    			op.update();
	    			}
	    			else{
	    				op.setCheckValue(1);
	        			
	        			op.update();
	    			}
	    		}
	    	}
			return ok();
		}
	    
	    public static Result checkForService(int checkForServiceValue){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		System.out.println(">>>>>>>>>>>>>>>>>>>"+checkForServiceValue);
	    		List<HoursOfOperation>list=HoursOfOperation.findByTypeForServices();
	    		for (HoursOfOperation op : list) {
	    			if(checkForServiceValue == 0){
	    			op.setCheckValue(0);
	    			
	    			op.update();
	    			}
	    			else{
	    				op.setCheckValue(1);
	        			
	        			op.update();
	    			}
	    		}
	    	}
			return ok();
		}
	    
	    public static Result saveSalesHoursForService(){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		MultipartFormData body = request().body().asMultipartFormData();
		    	Form<HoursOperation> form = DynamicForm.form(HoursOperation.class).bindFromRequest();
		    	HoursOperation vm = form.get();
	    		AuthUser user = getLocalUser();
		    	Date currDate = new Date();
		    	
		    	
		    		HoursOfOperation operation=HoursOfOperation.findByDayForService("Sunday");
		    		if(operation == null){
		    			if(vm.sunOpen == null || vm.sunOpen == false){
		    				HoursOfOperation op=new HoursOfOperation();
		    				op.day="Sunday";
		    		try {
		    			if(vm.sunOpenTime != "" ||  vm.sunCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.sunOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.sunCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.sunOpen == null || vm.sunOpen == false){
		    				
		    				try {
								operation.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.sunOpenTime));
		    				operation.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.sunCloseTime));
		    				operation.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation.update();
		    			}else{
		    				operation.setDayFlag(1);
		    				operation.update();
		    			}
		    		}
		    		HoursOfOperation operation1=HoursOfOperation.findByDayForService("Monday");
		    		if(operation1 == null){
		    			if(vm.monOpen == null || vm.monOpen == false){
		    				HoursOfOperation op=new HoursOfOperation();
		    				op.day="Monday";
		    		try {
		    			if(vm.monOpenTime != "" ||  vm.monCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.monOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.monCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.monOpen == null || vm.monOpen == false){
		    				
		    				try {
								operation1.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.monOpenTime));
		    				operation1.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.monCloseTime));
		    				operation1.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation1.update();
		    			}else{
		    				operation1.setDayFlag(1);
		    				operation1.update();
		    			}
		    		}
		    		HoursOfOperation operation2=HoursOfOperation.findByDayForService("Tuesday");
		    		if(operation2 == null){
		    			if(vm.tueOpen == null || vm.tueOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.day="Tuesday";
		    		try {
		    			if(vm.tueOpenTime != "" ||  vm.tueCloseTime != ""){
		    			
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.tueOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.tueCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.tueOpen == null || vm.tueOpen == false){
		    				
		    				try {
								operation2.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.tueOpenTime));
		    				operation2.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.tueCloseTime));
		    				operation2.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation2.update();
		    			}else{
		    				operation2.setDayFlag(1);
		    				operation2.update();
		    			}
		    		}
		    		HoursOfOperation operation3=HoursOfOperation.findByDayForService("Wednesday");
		    		if(operation3 == null){
		    			if(vm.wedOpen == null || vm.wedOpen == false){
		    		HoursOfOperation op=new HoursOfOperation();
		    	 op.day="Wednesday";
		    		try {
		    			if(vm.wedOpenTime != "" ||  vm.wedCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.wedOpenTime);
					
		    		op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.wedCloseTime);
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    	op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.wedOpen == null || vm.wedOpen == false){
		    				
		    				try {
								operation3.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.wedOpenTime));
		    				operation3.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.wedCloseTime));
		    				operation3.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation3.update();
		    			}else{
		    				operation3.setDayFlag(1);
		    				operation3.update();
		    			}
		    		}
		    		HoursOfOperation operation4=HoursOfOperation.findByDayForService("Thursday");
		    		if(operation4 == null){
		    			if(vm.thuOpen == null || vm.thuOpen == false){
		    				HoursOfOperation op=new HoursOfOperation();
		    				op.day="Thursday";
		    		try {
		    			if(vm.thuOpenTime != "" ||  vm.thuCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.thuOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.thuCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.thuOpen == null || vm.thuOpen == false){
		    				
		    				try {
								operation4.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.thuOpenTime));
		    				operation4.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.thuCloseTime));
		    				operation4.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				
		    				operation4.update();
		    			}else{
		    				operation4.setDayFlag(1);
		    				operation4.update();
		    			}
		    		}
		    		HoursOfOperation operation5=HoursOfOperation.findByDayForService("Friday");
		    		if(operation5 == null){
		    			if(vm.friOpen == null || vm.friOpen == false){
		    				HoursOfOperation op=new HoursOfOperation();
		    				op.day="Friday";
		    		try {
		    			if(vm.friOpenTime != "" ||  vm.friCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.friOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.friCloseTime);
		    			}
		    		op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}else{	
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    		
		    	}
		    		else{
		    			if(vm.friOpen == null || vm.friOpen == false){
		    				try {
								operation5.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.friOpenTime));
		    				operation5.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.friCloseTime));
		    				operation5.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				operation5.update();
		    			}else{
		    				operation5.setDayFlag(1);
		    				operation5.update();
		    			}
		    		}
		    		HoursOfOperation operation6=HoursOfOperation.findByDayForService("Saturday");
		    		if(operation6 == null){
		    			if(vm.satOpen == null || vm.satOpen == false){
		    				HoursOfOperation op=new HoursOfOperation();
		    				op.day="Saturday";
		    		try {
		    			if(vm.satOpenTime != "" ||  vm.satCloseTime != ""){
						op.openTime =  new SimpleDateFormat("hh:mm a").parse(vm.satOpenTime);
					
						op.closeTime = new SimpleDateFormat("hh:mm a").parse(vm.satCloseTime);
		    			}
		    			op.dayFlag=0;
		    		} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		op.typeOfOperation=vm.typeOfOperation;	
		    		op.save();
		    	}else{
		    		HoursOfOperation op=new HoursOfOperation();
		    		op.dayFlag=1;
		    		op.typeOfOperation=vm.typeOfOperation;
		    		op.save();
		    	}
		    	}
		    		else{
		    			if(vm.satOpen == null  ||vm.satOpen == false ){
		    				try {
								operation6.setOpenTime( new SimpleDateFormat("hh:mm a").parse(vm.satOpenTime));
		    				operation6.setCloseTime(new SimpleDateFormat("hh:mm a").parse(vm.satCloseTime));
		    				operation6.setDayFlag(0);
		    				} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    				operation6.update();
		    			}else{
		    				operation6.setDayFlag(1);
		    				operation6.update();
		    			}
		    		}
		    	return ok();
	    	}	
		}
	    
	    public static Result updateUserMaven() {
	    		boolean isNew = true;
	    		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	    		MultipartFormData body = request().body().asMultipartFormData();
		    	Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
		    	UserVM vm = form.get();
		    	AuthUser userObj = AuthUser.findById(vm.id);
		    	System.out.println("hiiiiiii");
		    	if(body != null) {
		    		
			    	   //File file1 = new File(rootDir+userObj.imageUrl);
			    	  // System.out.println(userObj.imageUrl);
			    	   //file1.delete();
			    		FilePart picture = body.getFile("file0");
				    	  if (picture != null) {
				    	    String fileName = picture.getFilename();
				    	    File fdir = new File(rootDir+File.separator+"contractor"+File.separator+"Photographer"+File.separator+userObj.id+File.separator+"userPhoto");
				    	    if(!fdir.exists()) {
				    	    	fdir.mkdir();
				    	    }
				    	    String filePath = rootDir+File.separator+"contractor"+File.separator+"Photographer"+File.separator+userObj.id+File.separator+"userPhoto"+File.separator+fileName;
				    	    System.out.println(filePath);
				    	    File file = picture.getFile();
				    	    try {
					    	    	if(new File(filePath).exists()) {
					    	    		new File(filePath).delete();
						    	    }
				    	    		FileUtils.moveFile(file, new File(filePath));
				    	    		userObj.setImageUrl("/"+"contractor"+"/"+"Photographer"+"/"+userObj.id+"/"+"userPhoto"+"/"+fileName);
				    	    		userObj.setImageName(fileName);
				    	    		userObj.update();	
				    	    		
				    	  } catch (FileNotFoundException e) {
				  			e.printStackTrace();
					  		} catch (IOException e) {
					  			e.printStackTrace();
					  		} 
				    	  } 
				    	  return ok();
			    	   }else{
			    		   if("null".equals(vm.imageName)){
			    			   userObj.setImageName(null);
					   	       userObj.setImageUrl(vm.imageUrl);
			    		   }
			    	   }
		    	
			 		return ok();
	    }    	
	    
	    public static Result updateUser() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	
	    		MultipartFormData body = request().body().asMultipartFormData();
		    	Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
		    	UserVM vm = form.get();
		    	
		    	AuthUser userObj = AuthUser.findById(vm.id);
		    	userObj.setFirstName(vm.firstName);
		    	userObj.setLastName(vm.lastName);
		    	userObj.setEmail(vm.email);
		    	userObj.setPhone(vm.phone);
		    	userObj.setRole(vm.userType);
		    	//userObj.setPassword(vm.password);
		    	userObj.setAge(vm.age);
		    	userObj.setCommission(vm.commission);
		    	userObj.setContractDur(vm.contractDur);
		    	userObj.setExperience(vm.experience);
		    	userObj.setTrainingPro(vm.trainingPro);
		    	userObj.setTrialPeriod(vm.trialPeriod);
		    	userObj.setTrial(vm.trial);
		    	userObj.setUserGender(vm.userGender);
		    	userObj.setSalary(vm.salary);
		    	userObj.setTrainingCost(vm.trainingCost);
		    	userObj.setTrainingHours(vm.trainingHours);
		    	userObj.setQuota(vm.quota);
		    	
		    	String arr2[] = null;
		    	
		    	
		    	
		    	if(vm.userType.equals("General Manager")  || vm.userType.equals("Manager")){
		    		session("USER_ROLE", vm.userType+"");
		    	}else{
		    		if(body != null) {
			    		 String abcd= vm.permissions.get(0);
			 	    	abcd = abcd.replace("]", "");
			 	    	abcd = abcd.replace("[", "");
			 	    	abcd = abcd.replace("\"", "");
			 	    	arr2 = abcd.split(",");
			    	 }
		    	}
		    	userObj.deleteManyToManyAssociations("permission");
		    	List<Permission> permissionList = Permission.getAllPermission();
		    	
		    	if(vm.userType.equals("General Manager")) {
		    		  //userObj.permission = permissionList;
		    		List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
		    			   if(obj.name.equals("CRM") || obj.name.equals("My Profile") || obj.name.equals("Dashboard") || obj.name.equals("Show Location")) {
		    				   permissionData.add(obj);
		    			   }
		    		   }
		    		   userObj.permission = permissionData;
		    	   }
		    	   if(vm.userType.equals("Manager")) {
		    		   List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
						   if(!obj.name.equals("Show Location")) {
							   permissionData.add(obj);
						   }
		    		   }
		    		   userObj.permission = permissionData;
		    	   }
		    	   
		    	   if(vm.userType.equals("Sales Person")) {
		    		   /*List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
		    			   for(String role:vm.permissions){
		    				   if(obj.name.equals(role)) {
			    				   permissionData.add(obj);
			    			   }
		    	    	   }
		    			   if(!obj.name.equals("Home Page Editing") && !obj.name.equals("Blogs") && !obj.name.equals("My Profile") && !obj.name.equals("Account Settings")) {
		    				   permissionData.add(obj);
		    			   }
		    		   }*/
		    		   List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
		    			   if(body != null) {
		    				   for(String role:arr2){
	    						   if(obj.name.equals(role)) {
				    				   permissionData.add(obj);
		    					   }
	    				   
		    				   }
		    			   }else{
		    				   for(String role:vm.permissions){
	    						   if(obj.name.equals(role)) {
				    				   permissionData.add(obj);
		    					   }
	    				   
		    				   }
		    			   }
		    		   }
		    		   userObj.permission.addAll(permissionData);
		    	   }
		    	  
		    	  
		    	   if(body != null) {
		    	   File file1 = new File(rootDir+userObj.imageUrl);
		    	   file1.delete();
		    		FilePart picture = body.getFile("file0");
			    	  if (picture != null) {
			    	    String fileName = picture.getFilename();
			    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"userPhoto");
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"userPhoto"+File.separator+fileName;
			    	    File file = picture.getFile();
			    	    try {
			    	    		FileUtils.moveFile(file, new File(filePath));
			    	    		AuthUser user = AuthUser.findById(userObj.id);
			    	    	
			    	    		userObj.setImageUrl("/"+session("USER_LOCATION")+"/"+user.id+"/"+"userPhoto"+"/"+fileName);
			    	    		userObj.setImageName(fileName);
			    	    		//user.update();	
			    	    		
			    	  } catch (FileNotFoundException e) {
			  			e.printStackTrace();
				  		} catch (IOException e) {
				  			e.printStackTrace();
				  		} 
			    	  } 
		    	   }else{
		   	    	userObj.setImageName(null);
		   	    	userObj.setImageUrl(vm.imageUrl);
		    	   }
		    	   
		    	   if(vm.userType.equals("Photographer")){
		    		   PhotographerHoursOfOperation pOperation = PhotographerHoursOfOperation.findByUser(userObj);
				    	
				    	
				    	try {
				    			
				    			if(vm.hOperation.sunOpen == true){
				    				pOperation.sunOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.sunOpenTime);
				    				pOperation.sunOpen = 1;
				    			}else{
				    				pOperation.sunOpen = 0;
				    			}
				    			
				    			if(vm.hOperation.monOpen == true){
				    				pOperation.monOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.monOpenTime);
				    				pOperation.monOpen = 1;
				    			}else{
				    				pOperation.monOpen = 0;
				    			}
				    			
				    			if(vm.hOperation.thuOpen == true){
				    				pOperation.thuOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.thuOpenTime);
				    				pOperation.thuOpen = 1;
				    			}else{
				    				pOperation.thuOpen = 0;
				    			}
				    			
				    			if(vm.hOperation.tueOpen == true){
				    				pOperation.tueOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.tueOpenTime);
				    				pOperation.tueOpen = 1;
				    			}else{
				    				pOperation.tueOpen = 0;
				    			}
				    			
				    			if(vm.hOperation.wedOpen == true){
				    				pOperation.wedOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.wedOpenTime);
				    				pOperation.wedOpen = 1;
				    			}else{
				    				pOperation.wedOpen = 0;
				    			}
				    			
				    			if(vm.hOperation.friOpen == true){
				    				pOperation.friOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.friOpenTime);
				    				pOperation.friOpen = 1;
				    			}else{
				    				pOperation.friOpen = 0;
				    			}
				    			
				    			if(vm.hOperation.satOpen == true){
				    				pOperation.satOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.satOpenTime);
				    				pOperation.satOpen = 1;
				    			}else{
				    				pOperation.satOpen = 0;
				    			}
					    	
				    			pOperation.user = AuthUser.findById(userObj.id);
				    			pOperation.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
				    			
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				    	
				    	pOperation.update();
		    	   }
		    	   
		    	   
		    	   
		    	   userObj.update();
		    	   

		    	   InternalPdf iPdf = null;
		    	   
		    	   EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    		String emailName=details.name;
		    		String port=details.port;
		    		String gmail=details.host;
		    	final	String emailUser=details.username;
		    	final	String emailPass=details.passward;
		    	   AuthUser logoUser = AuthUser.findById(userObj.id);//Integer.getInteger(session("USER_KEY")));
		    	   SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));  //findByUser(logoUser);
		    		Properties props = new Properties();
			 		props.put("mail.smtp.auth", "true");
			 		props.put("mail.smtp.starttls.enable", "true");
			 		props.put("mail.smtp.host", gmail);
			 		props.put("mail.smtp.port", port);
			  
			 		Session session = Session.getInstance(props,
			 		  new javax.mail.Authenticator() {
			 			protected PasswordAuthentication getPasswordAuthentication() {
			 				return new PasswordAuthentication(emailUser, emailPass);
			 			}
			 		  });
			  
			 		try{
			 			
			  			Message message = new MimeMessage(session);
			  			try {
							message.setFrom(new InternetAddress("glider.autos@gmail.com",emailName));
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			  			message.setRecipients(Message.RecipientType.TO,
			  			InternetAddress.parse(userObj.email));
			  			message.setSubject("Your username and password ");	  			
			  			Multipart multipart = new MimeMultipart();
		    			BodyPart messageBodyPart = new MimeBodyPart();
		    			messageBodyPart = new MimeBodyPart();
		    			
		    			VelocityEngine ve = new VelocityEngine();
		    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
		    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
		    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
		    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		    			ve.init();
		    			
		    			Template t = ve.getTemplate("/public/emailTemplate/newUserTemplate.vm"); 
		    	        VelocityContext context = new VelocityContext();
		    	        context.put("hostnameUrl", imageUrlPath);
		    	        context.put("siteLogo", logo.logoImagePath);
		    	        context.put("username", userObj.email);
		    	        context.put("password", userObj.password);
		    	        StringWriter writer = new StringWriter();
		    	        t.merge( context, writer );
		    	        String content = writer.toString(); 
		    			
		    			messageBodyPart.setContent(content, "text/html");
		    			
		    			for(Long ls:vm.pdfIds){
		 	    		   iPdf = InternalPdf.findPdfById(ls);  
		 	    		   String PdfFile = rootDir + File.separator + iPdf.pdf_path;
		 	    		  File f = new File(PdfFile);
		 	    		 MimeBodyPart attachPart = new MimeBodyPart();
		 	    		 try {
		    					attachPart.attachFile(f);
		    		  	      } catch (IOException e) {
		    		  	       	// TODO Auto-generated catch block
		    		  	       		e.printStackTrace();
		    		  	    }
		    			 multipart.addBodyPart(attachPart);
		 	    	   }
		 	    	   
		 	    		
		    			
		    			
		    			multipart.addBodyPart(messageBodyPart);
		    			message.setContent(multipart);
		    			Transport.send(message);
			       		} catch (MessagingException e) {
			  			 throw new RuntimeException(e);
			  		}
			 		return ok();
	    	}    	
	    }
	    
	    public static Result getMangerAndLocation() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser users = (AuthUser) getLocalUser();
	    	//AuthUser users =AuthUser.findById(Integer.getInteger(session("USER_KEY")));
	    	LocationVM vm = new LocationVM();
	    	vm.email = users.email;
	    	vm.managerId = users.id;
			vm.firstName = users.firstName;
			vm.lastName = users.lastName;
			vm.phone = users.phone;
			vm.managerFullName = users.firstName+""+users.lastName;
			vm.mImageName = users.imageName;
			vm.mImageUrl = users.imageUrl;
			
			if(users.location == null){
				Location location1 = Location.findManagerType(users);
				if(location1 != null){
					session("USER_LOCATION", location1.id+"");
				}
			}
			Location location = Location.findById(Long.parseLong(session("USER_LOCATION")));
			if(location != null){
				vm.id = location.id;
				vm.locationaddress = location.address;
				vm.locationemail = location.email;
				vm.locationName = location.name;
				vm.locationphone = location.phone;
				vm.imageName = location.imageName;
				vm.imageUrl = location.imageUrl;
			}
	    		return ok(Json.toJson(vm));
	    	}
	    }
	    
	    public static Result getAllUsersToAssign() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		AuthUser users = getLocalUser();
	    		/*List<AuthUser> userList = AuthUser.getUserByType();*/
	    		List<AuthUser> userList = AuthUser.findByLocatio(users.location);
	    		List<UserVM> vmList = new ArrayList<>();
	    	
	    		for(AuthUser user : userList) {
	    			List<String> parmi = new ArrayList<>();
	    			UserVM vm = new UserVM();
	    			vm.fullName = user.firstName + " "+ user.lastName;
	    			vm.firstName = user.firstName;
	    			vm.lastName = user.lastName;
	    			vm.email = user.email;
	    			vm.phone = user.phone;
	    			vm.userType = user.role;
	    			vm.commission =user.commission;
	    			vm.contractDur = user.contractDur;
	    			vm.age = user.age;
	    			vm.userGender = user.userGender;
	    			vm.experience = user.experience;
	    			vm.trainingPro = user.trainingPro;
	    			vm.salary = user.salary;
	    			vm.trialPeriod = user.trialPeriod;
	    			vm.trainingCost = user.trainingCost;
	    			vm.trainingHours = user.trainingHours;
	    			vm.quota = user.quota;
	    			vm.imageName = user.imageName;
	    			vm.imageUrl = user.imageUrl;
	    			vm.trial = user.trial;
	    			vm.id = user.id;
	    			for(Permission permission:user.permission){
	    				parmi.add(permission.name);
	    			}
	    			vm.permissions = parmi;
	    			
	    				vmList.add(vm);
	    		}
	    		return ok(Json.toJson(vmList));
	    	}
	    }
	    
	    public static Result assignToUser(){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Identity user = getLocalUser();
		    	Form<AssignToVM> form = DynamicForm.form(AssignToVM.class).bindFromRequest();
		    	AssignToVM vm = form.get();
		    	AuthUser assUser = AuthUser.findById(Integer.parseInt(vm.id));
		    	for(UserLeadVM userLead : vm.leads) {
		    		if(userLead.leadType.equalsIgnoreCase("Request More Info")){
		    			RequestMoreInfo info = RequestMoreInfo.findById(userLead.id);
		    			info.setAssignedTo(assUser);
		    			info.update();
		    		}else if(userLead.leadType.equalsIgnoreCase("Schedule Test Drive")){
		    			ScheduleTest info = ScheduleTest.findById(userLead.id);
		    			info.setAssignedTo(assUser);
		    			info.update();
		    		}else if(userLead.leadType.equalsIgnoreCase("Trade In Info")){
		    			TradeIn info = TradeIn.findById(userLead.id);
		    			info.setAssignedTo(assUser);
		    			info.update();
		    		}
		    	}
		    	return ok();
	    	}
	    }	
	    
	    public static Result getAllLeadsByUser(Integer id){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser user = AuthUser.findById(id);
	    		List<UserLeadVM> list = new ArrayList<>();
	    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    		if(user !=null){
	    			List<RequestMoreInfo> listData = RequestMoreInfo.findAllAssignedLeadsToUser1(user);
	    			List<ScheduleTest> scheduleData = ScheduleTest.findAllAssignedLeadsToUser1(user);
	    			List<TradeIn> tradeData = TradeIn.findAllAssignedLeadsToUser1(user);
	    			for (RequestMoreInfo info : listData) {
	    				UserLeadVM vm = new UserLeadVM();
	    				Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
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
	    	    		}
	    					vm.leadType = "Request More Info";
	    					vm.name = info.name;
	    					vm.phone = info.phone;
	    					vm.email = info.email;
	    					vm.vin = info.vin;
	    					vm.check = false;
	    					vm.id = info.id;
	    					vm.requestDate = df.format(info.requestDate);
	    					list.add(vm);
					}
	    			for (ScheduleTest info : scheduleData) {
	    				UserLeadVM vm = new UserLeadVM();
	    				Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
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
	    	    		}
	    					vm.leadType = "Schedule Test Drive";
	    					vm.name = info.name;
	    					vm.phone = info.phone;
	    					vm.email = info.email;
	    					vm.vin = info.vin;
	    					vm.check = false;
	    					vm.id = info.id;
	    					vm.requestDate = df.format(info.scheduleDate);
	    					list.add(vm);
					}
	    			for (TradeIn info : tradeData) {
	    				UserLeadVM vm = new UserLeadVM();
	    				Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
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
	    	    		}
	    					vm.leadType = "Trade In Info";
	    					vm.name = info.firstName+" "+info.lastName;
	    					vm.phone = info.phone;
	    					vm.email = info.email;
	    					vm.vin = info.vin;
	    					vm.check = false;
	    					vm.id = info.id;
	    					vm.requestDate = df.format(info.tradeDate);
	    					list.add(vm);
					}
	    		}
	    		return ok(Json.toJson(list));
	    	}
		}
	    
	    public static Result deactivateAccount(Integer id){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser user = AuthUser.findById(id);
	    		if(user !=null){
	    			user.setAccount("deactive");
	        		user.update();
	    		}
	        	return ok();
	    	}
	    }
	    
	    public static Result getAllUsers() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		//2813
	    		//2714
	    		//2583
	    		
	    		AuthUser users = getLocalUser();
	    		/*List<AuthUser> userList = AuthUser.getUserByType();*/
	    		List<AuthUser> userList = AuthUser.findByLocatio(users.location);
	    		List<UserVM> vmList = new ArrayList<>();
	    	
	    		for(AuthUser user : userList) {
	    			List<String> parmi = new ArrayList<>();
	    			UserVM vm = new UserVM();
	    			vm.fullName = user.firstName + " "+ user.lastName;
	    			vm.firstName = user.firstName;
	    			vm.lastName = user.lastName;
	    			vm.email = user.email;
	    			vm.phone = user.phone;
	    			vm.userType = user.role;
	    			vm.commission =user.commission;
	    			vm.contractDur = user.contractDur;
	    			vm.age = user.age;
	    			vm.userGender = user.userGender;
	    			vm.experience = user.experience;
	    			vm.trainingPro = user.trainingPro;
	    			vm.salary = user.salary;
	    			vm.trialPeriod = user.trialPeriod;
	    			vm.trainingCost = user.trainingCost;
	    			vm.trainingHours = user.trainingHours;
	    			vm.quota = user.quota;
	    			vm.premiumFlag = user.premiumFlag;
	    			vm.imageName = user.imageName;
	    			vm.imageUrl = user.imageUrl;
	    			vm.trial = user.trial;
	    			vm.id = user.id;
	    			for(Permission permission:user.permission){
	    				parmi.add(permission.name);
	    			}
	    			vm.permissions = parmi;
	    			if(user.role.equals("Photographer")){
	    				SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
	    				
	    				PhotographerHoursOfOperation pOperation = PhotographerHoursOfOperation.findByUser(user);
	    				if(pOperation != null){
	    					if(pOperation.friOpen != null){
	    						if(pOperation.friOpen == 0){
	    							vm.hOperation.friOpen = false;
	    						}else{
	    							vm.hOperation.friOpen = true;
	    						}
	        				}
	        				if(pOperation.tueOpen != null){
	        					if(pOperation.tueOpen == 0){
	    							vm.hOperation.tueOpen = false;
	    						}else{
	    							vm.hOperation.tueOpen = true;
	    						}
	        				}
	        				if(pOperation.thuOpen != null){
	        					if(pOperation.thuOpen == 0){
	    							vm.hOperation.thuOpen = false;
	    						}else{
	    							vm.hOperation.thuOpen = true;
	    						}
	        				}
	        				if(pOperation.wedOpen != null){
	        					if(pOperation.wedOpen == 0){
	    							vm.hOperation.wedOpen = false;
	    						}else{
	    							vm.hOperation.wedOpen = true;
	    						}
	        				}
	        				if(pOperation.monOpen != null){
	        					if(pOperation.monOpen == 0){
	    							vm.hOperation.monOpen = false;
	    						}else{
	    							vm.hOperation.monOpen = true;
	    						}
	        				}
	        				if(pOperation.satOpen != null){
	        					if(pOperation.satOpen == 0){
	    							vm.hOperation.satOpen = false;
	    						}else{
	    							vm.hOperation.satOpen = true;
	    						}
	        				}
	        				if(pOperation.sunOpen != null){
	        					if(pOperation.sunOpen == 0){
	    							vm.hOperation.sunOpen = false;
	    						}else{
	    							vm.hOperation.sunOpen = true;
	    						}
	        				}
	        				
	        				
	        				
	        				
	        				if(pOperation.friOpenTime != null){
	        					vm.hOperation.friOpenTime = parseTime.format(pOperation.friOpenTime);
	        				}
	        				if(pOperation.tueOpenTime != null){
	        					vm.hOperation.tueOpenTime = parseTime.format(pOperation.tueOpenTime);
	        				}
	        				if(pOperation.thuOpenTime != null){
	        					vm.hOperation.thuOpenTime = parseTime.format(pOperation.thuOpenTime);
	        				}
	        				if(pOperation.wedOpenTime != null){
	        					vm.hOperation.wedOpenTime = parseTime.format(pOperation.wedOpenTime);
	        				}
	        				if(pOperation.monOpenTime != null){
	        					vm.hOperation.monOpenTime = parseTime.format(pOperation.monOpenTime);
	        				}
	        				if(pOperation.satOpenTime != null){
	        					vm.hOperation.satOpenTime = parseTime.format(pOperation.satOpenTime);
	        				}
	        				if(pOperation.sunOpenTime != null){
	        					vm.hOperation.sunOpenTime = parseTime.format(pOperation.sunOpenTime);
	        				}
	    				}
	    				
	    				
	    			}
	    			
	    			if(!vm.userType.equals("Manager")){
	    				vmList.add(vm);
	    			}
	    		}
	    		return ok(Json.toJson(vmList));
	    	}
	    }
	    
	    public static Result findLocation(){
			   return ok(Json.toJson(Long.valueOf(session("USER_LOCATION"))));
		   }
	    
public static Result saveUser() {
	    	
    		Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
    		MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	UserVM vm = form.get();
	    	
	    	AuthUser uAuthUser = AuthUser.findByEmail(vm.email);
	    	AuthUser userObj = new AuthUser();
	    	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    	if(uAuthUser == null){
	    		
		    	userObj.firstName = vm.firstName;
		    	userObj.lastName = vm.lastName;
		    	userObj.email = vm.email;
		    	userObj.account = "active";
		    	userObj.communicationemail = vm.email;
		    	userObj.phone = vm.phone;
		    	userObj.role = vm.userType;
		    	userObj.location = Location.findById(vm.locationId);
		    	userObj.age = vm.age;
		    	userObj.commission =vm.commission;
		    	userObj.contractDur = vm.contractDur;
		    	userObj.experience = vm.experience;
		    	userObj.trainingPro = vm.trainingPro;
		    	userObj.trialPeriod = vm.trialPeriod;
		    	userObj.trial = vm.trial;
		    	userObj.userGender = vm.userGender;
		    	userObj.salary = vm.salary;
		    	userObj.trainingCost = vm.trainingCost;
		    	userObj.trainingHours = vm.trainingHours;
		    	userObj.quota = vm.quota;
		    	if(!vm.userType.equals("Photographer")){
		    		userObj.imageUrl = vm.imageUrl;
		    	}
		    	
		    	if(vm.premiumFlag.equals("true")){
		    		userObj.premiumFlag = "1";
		    	}else{
		    		userObj.premiumFlag = "0";
		    	}
		    	
		    	try {
		    		if(vm.contractDurEndDate != null)
		    			userObj.contractDurEndDate = dateFormat.parse(vm.contractDurEndDate);
		    		if(vm.contractDurStartDate != null)
		    			userObj.contractDurStartDate = dateFormat.parse(vm.contractDurStartDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	
		    	String arr2[] = null;
		    	 if(body != null) {
		    		 String abcd= vm.permissions.get(0);
		 	    	abcd = abcd.replace("]", "");
		 	    	abcd = abcd.replace("[", "");
		 	    	abcd = abcd.replace("\"", "");
		 	    	arr2 = abcd.split(",");
		    	 }
		    	 
		    	 if(body != null){
		    		String[] roles = vm.permissions.get(0).replaceAll("[\\[\\](){}\"]","").split(",");
		    		vm.permissions = new ArrayList<>();
		    		for (int i = 0; i < roles.length; i++) {
		    			vm.permissions.add(roles[i]);
					}
		    	}
		    	
		    	final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		    	Random rnd = new Random();

		    	
		    	
		    	   StringBuilder sb = new StringBuilder( 6 );
		    	   for( int i = 0; i < 6; i++ ) 
		    	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		    	
		    	   userObj.setPassword(sb.toString());
		    	   List<Permission> permissionList = Permission.getAllPermission();
		    	   if(vm.userType.equals("General Manager")) {
		    		  //userObj.permission = permissionList;
		    		   List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
    					   for(String per: vm.permissions) {//obj.name.equals("CRM") || obj.name.equals("My Profile") || obj.name.equals("Dashboard") || obj.name.equals("Show Location")
    						   if(obj.name.equals(per)) {
    		    				   permissionData.add(obj);
    		    			   }
    					   }
		    		   }
		    		   userObj.permission = permissionData;
		    	   }
		    	   
		    	   if(vm.userType.equals("Photographer")) {
		    		   List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
		    			   if(obj.name.equals("My Calendar") || obj.name.equals("Dashboard") || obj.name.equals("Inventory Photographer")) {
		    				   permissionData.add(obj);
		    			   }
		    		   }
		    		   userObj.permission = permissionData;
		    	   }
		    	   if(vm.userType.equals("Front Desk")) {
		    		   List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
		    			   for(String per: vm.permissions) {
		    				   if(obj.name.equals(per)) {
    		    				   permissionData.add(obj);
    		    			   }
		    			   }
		    			   /*if(obj.name.equals("Calendar")) {
		    				   permissionData.add(obj);
		    			   }*/
		    		   }
		    		   userObj.permission = permissionData;
		    	   }
		    	   
		    	   if(vm.userType.equals("Sales Person")) {
		    		   
		    		  // String aa = vm.permissions.get(0);
		    		   List<Permission> permissionData = new ArrayList<>();
		    		   for(Permission obj: permissionList) {
		    			   /*if(body != null) {
		    				   for(String role:arr2){
	    						   if(obj.name.equals(role)) {
				    				   permissionData.add(obj);
		    					   }
	    				   
		    				   }
		    			   }else{*/
		    				   for(String role:vm.permissions){
	    						   if(obj.name.equals(role)) {
				    				   permissionData.add(obj);
		    					   }
	    				   
		    				   }
		    			   //}
		    			   
		    			   /*if(!obj.name.equals("Home Page Editing") && !obj.name.equals("Blogs") && !obj.name.equals("My Profile") && !obj.name.equals("Account Settings")) {
		    				   permissionData.add(obj);
		    			   }*/
		    			   
		    		   }
		    		   userObj.setPermission(permissionData);
		    	   }
		    	   
		    	   userObj.save();
	    		
	    	}
	    		    	
	    	   DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	    	   if(vm.userType.equals("Photographer")){
	    		   
	    		   AuthUser user = null;
	   	    	if(uAuthUser == null){
	   				user = AuthUser.findById(userObj.id);
	   			}else{
	   				user = AuthUser.findById(uAuthUser.id);
	   			}
	   	    	
	   	    	PhotographerHoursOfOperation php = PhotographerHoursOfOperation.findByUserAndLocation(user, Location.findById(vm.locationId),"AutoDealer");
	   	    	if(php == null){
	   	    		
	    		   PhotographerHoursOfOperation pOperation = new PhotographerHoursOfOperation();
			    	
			    	try {
			    			
			    			if(vm.hOperation.sunOpen == true){
			    				pOperation.sunOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.sunOpenTime);
			    				pOperation.sunOpen = 1;
			    			}else{
			    				pOperation.sunOpen = 0;
			    			}
			    			
			    			if(vm.hOperation.sunOpen == true){
			    				pOperation.sunCloseTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.sunCloseTime);
			    				pOperation.sunOpen = 1;
			    			}else{
			    				pOperation.sunOpen = 0;
			    			}
			    			
			    			
			    			if(vm.hOperation.monOpen == true){
			    				pOperation.monOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.monOpenTime);
			    				pOperation.monOpen = 1;
			    			}else{
			    				pOperation.monOpen = 0;
			    			}
			    			if(vm.hOperation.monOpen == true){
			    				pOperation.monCloseTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.monCloseTime);
			    				pOperation.monOpen = 1;
			    			}else{
			    				pOperation.monOpen = 0;
			    			}
			    			
			    			if(vm.hOperation.thuOpen == true){
			    				pOperation.thuOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.thuOpenTime);
			    				pOperation.thuOpen = 1;
			    			}else{
			    				pOperation.thuOpen = 0;
			    			}
			    			
			    			if(vm.hOperation.thuOpen == true){
			    				pOperation.thuCloseTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.thuCloseTime);
			    				pOperation.thuOpen = 1;
			    			}else{
			    				pOperation.thuOpen = 0;
			    			}
			    			
			    			
			    			if(vm.hOperation.tueOpen == true){
			    				pOperation.tueOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.tueOpenTime);
			    				pOperation.tueOpen = 1;
			    			}else{
			    				pOperation.tueOpen = 0;
			    			}
			    			if(vm.hOperation.tueOpen == true){
			    				pOperation.tueCloseTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.tueCloseTime);
			    				pOperation.tueOpen = 1;
			    			}else{
			    				pOperation.tueOpen = 0;
			    			}
			    			
			    			if(vm.hOperation.wedOpen == true){
			    				pOperation.wedOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.wedOpenTime);
			    				pOperation.wedOpen = 1;
			    			}else{
			    				pOperation.wedOpen = 0;
			    			}
			    			if(vm.hOperation.wedOpen == true){
			    				pOperation.wedCloseTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.wedCloseTime);
			    				pOperation.wedOpen = 1;
			    			}else{
			    				pOperation.wedOpen = 0;
			    			}
			    			if(vm.hOperation.friOpen == true){
			    				pOperation.friOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.friOpenTime);
			    				pOperation.friOpen = 1;
			    			}else{
			    				pOperation.friOpen = 0;
			    			}
			    			if(vm.hOperation.friOpen == true){
			    				pOperation.friCloseTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.friCloseTime);
			    				pOperation.friOpen = 1;
			    			}else{
			    				pOperation.friOpen = 0;
			    			}
			    			
			    			if(vm.hOperation.satOpen == true){
			    				pOperation.satOpenTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.satOpenTime);
			    				pOperation.satOpen = 1;
			    			}else{
			    				pOperation.satOpen = 0;
			    			}
			    			if(vm.hOperation.satOpen == true){
			    				pOperation.satCloseTime = new SimpleDateFormat("hh:mm a").parse(vm.hOperation.satCloseTime);
			    				pOperation.satOpen = 1;
			    			}else{
			    				pOperation.satOpen = 0;
			    			}
			    		pOperation.portalName = vm.portalName;
			    		if(vm.contractDurEndDate != null){
			    			pOperation.contractDurEndDate = df.parse(vm.contractDurEndDate);
			    		}
			    			pOperation.contractDurStartDate = df.parse(vm.contractDurStartDate);
			    			if(uAuthUser == null){
			    				pOperation.user = AuthUser.findById(userObj.id);
			    			}else{
			    				pOperation.user = AuthUser.findById(uAuthUser.id);
			    			}
			    			
			    			pOperation.locations = Location.findById(vm.locationId);
			    			
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	
			    	pOperation.save();
	    	    }	
	    	   }
	    	  
		    	
		    	    	
	    	   
	    	   if(body != null) {
	    		FilePart picture = body.getFile("file0");
		    	  if (picture != null) {
		    	    String fileName = picture.getFilename();
		    	    File fdir = new File(rootDir+File.separator+vm.locationId+File.separator+userObj.id+File.separator+"userPhoto");
		    	    if(!fdir.exists()) {
		    	    	fdir.mkdir();
		    	    }
		    	    String filePath = rootDir+File.separator+vm.locationId+File.separator+userObj.id+File.separator+"userPhoto"+File.separator+fileName;
		    	    File file = picture.getFile();
		    	    try {
		    	    		FileUtils.moveFile(file, new File(filePath));
		    	    		if(userObj != null){
			    	    		AuthUser user = AuthUser.findById(userObj.id);
			    	    		if(user != null){
			    	    			user.setImageUrl("/"+vm.locationId+"/"+user.id+"/"+"userPhoto"+"/"+fileName);
				    	    		user.setImageName(fileName);
				    	    		user.update();	
			    	    		}
		    	    		}
		    	    		
		    	    		
		    	  } catch (FileNotFoundException e) {
		  			e.printStackTrace();
			  		} catch (IOException e) {
			  			e.printStackTrace();
			  		} 
		    	  } 
	    	   } 
	    	   InternalPdf iPdf = null;
	    	   
	    	   EmailDetails details=EmailDetails.findByLocation(vm.locationId);
	    		String emailName=details.name;
	    		String port=details.port;
	    		String gmail=details.host;
	    	final	String emailUser=details.username;
	    	final	String emailPass=details.passward;
	    	
	    	if(uAuthUser == null){
	    		AuthUser logoUser = AuthUser.findById(userObj.id);
			}else{
				AuthUser logoUser = AuthUser.findById(uAuthUser.id);
			}
	    	
	    	   
	    	   SiteLogo logo = SiteLogo.findByLocation(vm.locationId);
	    		Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host",gmail);
		 		props.put("mail.smtp.port", port);
		  
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUser, emailPass);
		 			}
		 		  });
		  
		 		try{
		 			
		  			Message message = new MimeMessage(session);
		  			try {
						message.setFrom(new InternetAddress("glider.autos@gmail.com",emailName));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		  			if(userObj != null){
		  				if(userObj.email != null){
		  					message.setRecipients(Message.RecipientType.TO,
		  				  			InternetAddress.parse(userObj.email));
		  				}else{
		  					message.setRecipients(Message.RecipientType.TO,
		  				  			InternetAddress.parse("abc@gmail.com"));
		  				}
		  			}else{
		  				message.setRecipients(Message.RecipientType.TO,
	  				  			InternetAddress.parse("abc@gmail.com"));
		  			}
		  			
		  			message.setSubject("Your username and password ");	  			
		  			Multipart multipart = new MimeMultipart();
	    			BodyPart messageBodyPart = new MimeBodyPart();
	    			messageBodyPart = new MimeBodyPart();
	    			
	    			VelocityEngine ve = new VelocityEngine();
	    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
	    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
	    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
	    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	    			ve.init();
	    			
	    			Template t = ve.getTemplate("/public/emailTemplate/newUserTemplate.vm"); 
	    	        VelocityContext context = new VelocityContext();
	    	        context.put("hostnameUrl", imageUrlPath);
	    	        //context.put("siteLogo", logo.logoImagePath);
	    	        context.put("username", userObj.email);
	    	        context.put("password", userObj.password);
	    	        StringWriter writer = new StringWriter();
	    	        t.merge( context, writer );
	    	        String content = writer.toString(); 
	    			
	    			messageBodyPart.setContent(content, "text/html");
	    			if(vm.pdfIds != null){
	    			for(Long ls:vm.pdfIds){
	 	    		   iPdf = InternalPdf.findPdfById(ls);  
	 	    		   if(iPdf != null){
	 	    			   if(iPdf.pdf_path != null){
	 	    			   
		 	    			  String PdfFile = rootDir + File.separator + iPdf.pdf_path;
			 	    		  File f = new File(PdfFile);
			 	    		 MimeBodyPart attachPart = new MimeBodyPart();
			 	    		 try {
			    					attachPart.attachFile(f);
			    		  	      } catch (IOException e) {
			    		  	       	// TODO Auto-generated catch block
			    		  	       		e.printStackTrace();
			    		  	    }
			    			 multipart.addBodyPart(attachPart);
		 	    		   }
	 	    		   }
	 	    	   	   }
	    			}
	 	    		
	    			
	    			
	    			multipart.addBodyPart(messageBodyPart);
	    			message.setContent(multipart);
	    			Transport.send(message);
		       		} catch (MessagingException e) {
		       			e.printStackTrace();
		  			 //throw new RuntimeException(e);
		  		}
		 		System.out.println("save user END ??????????????????????????????????????");
	    	return ok(Json.toJson(userObj));
    }
	    
	    public static Result getAllDeactivateUsers(){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		AuthUser users = getLocalUser();
	    		/*List<AuthUser> userList = AuthUser.getUserByType();*/
	    		List<AuthUser> userList = AuthUser.findByLocationDeactive(users.location);
	    		List<UserVM> vmList = new ArrayList<>();
	    	
	    		for(AuthUser user : userList) {
	    			List<String> parmi = new ArrayList<>();
	    			UserVM vm = new UserVM();
	    			vm.fullName = user.firstName + " "+ user.lastName;
	    			vm.firstName = user.firstName;
	    			vm.lastName = user.lastName;
	    			vm.email = user.email;
	    			vm.phone = user.phone;
	    			vm.userType = user.role;
	    			vm.commission =user.commission;
	    			vm.contractDur = user.contractDur;
	    			vm.age = user.age;
	    			vm.userGender = user.userGender;
	    			vm.experience = user.experience;
	    			vm.trainingPro = user.trainingPro;
	    			vm.salary = user.salary;
	    			vm.trialPeriod = user.trialPeriod;
	    			vm.trainingCost = user.trainingCost;
	    			vm.trainingHours = user.trainingHours;
	    			vm.quota = user.quota;
	    			vm.imageName = user.imageName;
	    			vm.imageUrl = user.imageUrl;
	    			vm.trial = user.trial;
	    			vm.id = user.id;
	    			for(Permission permission:user.permission){
	    				parmi.add(permission.name);
	    			}
	    			vm.permissions = parmi;
	    			
	    			if(!vm.userType.equals("Manager")){
	    				vmList.add(vm);
	    			}
	    		}
	    		return ok(Json.toJson(vmList));
	    	}
	    }
	    
	    public static Result activeAccount(Integer id){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser user = AuthUser.findById(id);
	    		if(user !=null){
	    			user.setAccount("active");
	        		user.update();
	    		}
	        	return ok();
	    	}
	    }
	    
	    public static Result activeLocationById(Long id){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Location loc = Location.findById(id);
	    		if(loc !=null){
	    			loc.setType("active");
	    			loc.update();
	    		}
	        	return ok();
	    	}
	    }
}