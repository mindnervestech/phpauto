package controllers;

import java.awt.TextField;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
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
import javax.net.ssl.HttpsURLConnection;

import models.ActionAdd;
import models.AuthUser;
import models.Blog;
import models.Comments;
import models.Contacts;
import models.FeaturedImage;
import models.FeaturedImageConfig;
import models.FollowBrand;
import models.GroupTable;
import models.HeardAboutUs;
import models.LeadsDateWise;
import models.Location;
import models.MyProfile;
import models.NewsletterDate;
import models.Permission;
import models.PlanLocationTotal;
import models.PlanSalesTotal;
import models.PlanSchedule;
import models.PlanScheduleMonthlyLocation;
import models.PlanScheduleMonthlySalepeople;
import models.PremiumLeads;
import models.PriceAlert;
import models.PriceChange;
import models.RequestMoreInfo;
import models.SalesPlanSchedule;
import models.ScheduleTest;
import models.Site;
import models.SiteContent;
import models.SiteLogo;
import models.SliderImage;
import models.SliderImageConfig;
import models.SoldContact;
import models.ToDo;
import models.TradeIn;
import models.UserNotes;
import models.Vehicle;
import models.VehicleAudio;
import models.VehicleImage;
import models.VirtualTour;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.Days;
import org.json.JSONArray;
import org.json.JSONException;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import scheduler.NewsLetter;
import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;
import viewmodel.AssignToVM;
import viewmodel.AudioVM;
import viewmodel.BarChartVM;
import viewmodel.BlogVM;
import viewmodel.ContactsVM;
import viewmodel.DateAndValueVM;
import viewmodel.EditImageVM;
import viewmodel.HeardAboutUsVm;
import viewmodel.ImageVM;
import viewmodel.InfoCountVM;
import viewmodel.LeadDateWiseVM;
import viewmodel.LeadVM;
import viewmodel.LocationMonthPlanVM;
import viewmodel.LocationVM;
import viewmodel.LocationWiseDataVM;
import viewmodel.NoteVM;
import viewmodel.PageVM;
import viewmodel.PinVM;
import viewmodel.PlanScheduleVM;
import viewmodel.PriceChangeVM;
import viewmodel.PriceFormatDate;
import viewmodel.RequestInfoVM;
import viewmodel.SalepeopleMonthPlanVM;
import viewmodel.ScheduleTestVM;
import viewmodel.SessionUseVM;
import viewmodel.SetPriceChangeFlag;
import viewmodel.SiteContentVM;
import viewmodel.SiteLogoVM;
import viewmodel.SiteVM;
import viewmodel.SoldContactVM;
import viewmodel.SpecificationVM;
import viewmodel.ToDoVM;
import viewmodel.TradeInVM;
import viewmodel.UserLeadVM;
import viewmodel.UserNoteVM;
import viewmodel.UserVM;
import viewmodel.VehicleVM;
import viewmodel.VirtualTourVM;
import viewmodel.bodyStyleSetVM;
import viewmodel.profileVM;
import viewmodel.sendDateAndValue;
import views.html.agreement;
import views.html.home;
import views.html.index;
import au.com.bytecode.opencsv.CSVWriter;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.google.api.services.tasks.model.Tasks;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mnt.dataone.Equipment;
import com.mnt.dataone.InstalledEquipment;
import com.mnt.dataone.Option;
import com.mnt.dataone.OptionalEquipment;
import com.mnt.dataone.ResponseData;
import com.mnt.dataone.Specification;
import com.mnt.dataone.Specification_;
import com.mnt.dataone.Value;

public class Application extends Controller {
  
	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	final static String pdfRootDir = Play.application().configuration()
			.getString("pdfRootDir");
	
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
			
			//public static int userId = -1361609913;
			
	static String simulatevin = "{    'success': true,    'specification': {        'vin': 'WDDNG7KB7DA494890',        'year': '2013',        'make': 'Mercedes-Benz',        'model': 'S-Class',        'trim_level': 'S65 AMG',        'engine': '6.0L V12 SOHC 36V TURBO',        'style': 'SEDAN 4-DR',        'made_in': 'GERMANY',        'steering_type': 'R&P',        'anti_brake_system': '4-Wheel ABS',        'tank_size': '23.80 gallon',        'overall_height': '58.00 in.',        'overall_length': '206.50 in.',        'overall_width': '73.70 in.',        'standard_seating': '5',        'optional_seating': null,        'highway_mileage': '19 miles/gallon',        'city_mileage': '12 miles/gallon'    },    'vin': 'WDDNG7KB7DA494890'}";

	private static boolean simulate = false;
    /*public static Result index() {
        return ok(index.render("Your new application is ready."));
    }*/
	
	
	//private final static Log logger = LogFactory.getLog(GoogleConnectController.class);
		private static final String APPLICATION_NAME = "Web client 1";
		private static HttpTransport httpTransport;
		private static HttpTransport httpTransporttask;
		private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		private static com.google.api.services.calendar.Calendar client;
		private static com.google.api.services.tasks.Tasks service;
		private static int flagValue = 0;
		
		static GoogleClientSecrets clientSecrets;
		static GoogleClientSecrets clientSecretstask;
		static GoogleAuthorizationCodeFlow flow;
		static GoogleAuthorizationCodeFlow flowtask;
		static com.google.api.client.auth.oauth2.Credential credential;
		static com.google.api.client.auth.oauth2.Credential credentialtask;

		private static String clientId="657059082204-1uh3d2dt5cik1269s55bc80tlpd52gsb.apps.googleusercontent.com";
		private static String clientSecret="Xx2gAJ4ucJ-rmcYdO3wwB5_D";
		private static String redirectURI="http://www.glider-autos.com/oauth2Callback";
		private static String redirectURIUpdate="http://www.glider-autos.com/updatecalenderdata";
		private Set<Event> events=new HashSet<Event>();
		static List<Tasks> tasksList = new ArrayList<>();
		static List<Event> events1 = new ArrayList<>();
		
		private static Oauth2 oauth2;
		
		
		public void setEvents(Set<Event> events) {
			this.events = events;
		}
		
		public static Result locationWise(Long locationId){
			AuthUser user = AuthUser.getOnlyGM();
			
			if(user != null) {
				System.out.println(user.role);

						session("USER_KEY", user.id+"");
						session("USER_ROLE", user.role+""	);
						
						if(user.location != null){
							session("USER_LOCATION", user.location.id+"");
						}else if(user.location == null){
							Location location = Location.findManagerType(user);
							if(location != null){
								session("USER_LOCATION", location.id+"");
							}
						}
			    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
			    		List<Permission> userPermissions = user.getPermission();
			    		for(Permission per: userPermissions) {
			    			permission.put(per.name, true);
			    		}
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),locationId.toString()));
			    	
			} else {
				return ok(home.render("Invalid Credentials"));
			}
			
			
		}
		
		public static Result gmIsManager(Long locationId) {
			
			AuthUser user = AuthUser.getlocationAndManagerOne(Location.findById(locationId));
			
			if(user != null) {
				System.out.println(user.role);

						session("USER_KEY", user.id+"");
						session("USER_ROLE", user.role+""	);
						
						if(user.location != null){
							session("USER_LOCATION", user.location.id+"");
						}else if(user.location == null){
							Location location = Location.findManagerType(user);
							if(location != null){
								session("USER_LOCATION", location.id+"");
							}
						}
			    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
			    		List<Permission> userPermissions = user.getPermission();
			    		for(Permission per: userPermissions) {
			    			permission.put(per.name, true);
			    		}
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)), "0"));
			    	
			} else {
				return ok(home.render("Invalid Credentials"));
			}
			
		}
	
	public static Result login() {
		String email = Form.form().bindFromRequest().get("email");
		String password= Form.form().bindFromRequest().get("password");
		AuthUser user = AuthUser.find.where().eq("email", email).eq("password", password).eq("account", "active").findUnique();
		if(user != null) {
			System.out.println(user.role);
			if(user.role.equalsIgnoreCase("General Manager")){
				if(user.getNewUser()== 1){

					session("USER_KEY", user.id+"");
					session("USER_ROLE", user.role+""	);
					
					if(user.location != null){
						session("USER_LOCATION", user.location.id+"");
					}else if(user.location == null){
						Location location = Location.findManagerType(user);
						if(location != null){
							session("USER_LOCATION", location.id+"");
						}
					}
					//return  redirect("/dealer/index.html#/");
		    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
		    		List<Permission> userPermissions = user.getPermission();
		    		for(Permission per: userPermissions) {
		    			permission.put(per.name, true);
		    		}
		    		
		    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0"));
		    		//return redirect("/googleConnectionStatus");
				}else{
					return ok(home.render(user.getEmail()));
				}
			}else{
				Location loc = Location.findById(user.location.id);
				if(loc.getType().equalsIgnoreCase("active")){
					if(user.getNewUser()== 1){

						session("USER_KEY", user.id+"");
						session("USER_ROLE", user.role+"");
						
						if(user.location != null){
							session("USER_LOCATION", user.location.id+"");
						}else if(user.location == null){
							Location location = Location.findManagerType(user);
							if(location != null){
								session("USER_LOCATION", location.id+"");
							}
						}
						
						
						//return  redirect("/dealer/index.html#/");
			    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
			    		List<Permission> userPermissions = user.getPermission();
			    		for(Permission per: userPermissions) {
			    			permission.put(per.name, true);
			    		}
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0"));
			    		//return redirect("/googleConnectionStatus");
					}else{
						return ok(home.render(user.getEmail()));
					}
				}else{
					return ok(home.render("Your account has been suspended, please contact your management for further questions"));
				}
			}
    		//return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList))));
		} else {
			return ok(home.render("Invalid Credentials"));
		}
	}
	
	public static Result getfindGmIsManager(){
		AuthUser user = getLocalUser();
		Location location = Location.findById(user.location.id);
		int flag = 0;
		if(location != null){
			if(location.manager != null){
				flag = 1;
			}
		}
		//Location location = Location.findManagerType(user);
		return ok(Json.toJson(flag));
	}
	
	public static Result acceptAgreement() {
		System.out.println("acceptAgreement");
		String email = Form.form().bindFromRequest().get("email");
		String userName = Form.form().bindFromRequest().get("name");
		String userDate = Form.form().bindFromRequest().get("date");
		String userPhone = Form.form().bindFromRequest().get("phone");
		AuthUser user = AuthUser.findByEmail(email);
		if(user != null) {
			user.setNewUser(1);
			user.update();
			session("USER_KEY", user.id+"");
			session("USER_ROLE", user.role+"");
			
			if(user.location != null){
				session("USER_LOCATION", user.location.id+"");
			}else if(user.location == null){
				Location location = Location.findManagerType(user);
				if(location != null){
					session("USER_LOCATION", location.id+"");
				}
			}
			
			
			HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
    		List<Permission> userPermissions = user.getPermission();
    		for(Permission per: userPermissions) {
    			permission.put(per.name, true);
    		}
    		
    		
    		 String domain = Play.application().configuration().getString("domain");
    	        String wkpath = Play.application().configuration().getString("wkpath");
    	        String folderPath = Play.application().path().getAbsolutePath() + "/pdf";
    	        File folder = new File(folderPath);
    	        if (!folder.exists()) {
    	            folder.mkdir();
    	        }
    	        String pdfFilePath = Play.application().path().getAbsolutePath() + "/pdf/"+ "agreement.pdf";
    	        try {
    	            Process p = Runtime.getRuntime().exec(wkpath + " --viewport-size 1280x800 " + domain + "getAgreement/" +userName + "/"+userDate +"/"+userPhone +" "+pdfFilePath);
    	            BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
    	            String line = inStreamReader.readLine();
    	            while (line != null) {
    	                line = inStreamReader.readLine();
    	            }
    	        } catch (Exception e) {
    	            System.out.println("coming-->" + e.getMessage());
    	        }
    	        File f = new File(pdfFilePath);

	        agreementEmail();
    		
    		//agreementEmail(userName,userDate,userPhone);
    		//return ok(agreement.render(userName,userDate,userPhone));
    		//return redirect("/googleConnectionStatus");
    		//return ok();
	        	return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0"));
		}else {
			return ok(home.render("Invalid Credentials"));
		}
	}
	
	public static Result agreementEmail(){
		
	        String to = "info@gliderllc.com";
	        String from = "glider.autos@gmail.com";
	        String host = "mail.smtp.host";

	        Properties props = new Properties();
	 		props.put("mail.smtp.auth", "true");
	 		props.put("mail.smtp.starttls.enable", "true");
	 		props.put("mail.smtp.host", "smtp.gmail.com");
	 		props.put("mail.smtp.port", "587");

	        // Get the Session object.
	        Session session = Session.getInstance(props,
	           new javax.mail.Authenticator() {
	              protected PasswordAuthentication getPasswordAuthentication() {
	                 return new PasswordAuthentication(emailUsername, emailPassword);
	              }
	           });

	        try {
	           Message message = new MimeMessage(session);

	           message.setFrom(new InternetAddress(from));

	           message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

	           message.setSubject("User Agreement");

	           BodyPart messageBodyPart = new MimeBodyPart();

	           messageBodyPart.setText("This is message body");

	           Multipart multipart = new MimeMultipart();

	           multipart.addBodyPart(messageBodyPart);

	           messageBodyPart = new MimeBodyPart();
	           String pdfFilePath = Play.application().path().getAbsolutePath() + "/pdf/"+ "agreement.pdf";
	           DataSource source = new FileDataSource(pdfFilePath);
	           messageBodyPart.setDataHandler(new DataHandler(source));
	           messageBodyPart.setFileName(pdfFilePath);
	           multipart.addBodyPart(messageBodyPart);

	           message.setContent(multipart);

	           Transport.send(message);

	           System.out.println("Sent message successfully....");
	    
	        } catch (MessagingException e) {
	           throw new RuntimeException(e);
	        }
		return ok();
	}
	
	public static Result getAgreement(String userName,String userDate, String userPhone) {
		return ok(agreement.render(userName,userDate,userPhone));
	}
	
	public static Result agreementEmail(String userName,String userDate, String userPhone) {
			
		Properties props = new Properties();
 		props.put("mail.smtp.auth", "true");
 		props.put("mail.smtp.starttls.enable", "true");
 		props.put("mail.smtp.host", "smtp.gmail.com");
 		props.put("mail.smtp.port", "587");
  
 		Session session = Session.getInstance(props,
 		  new javax.mail.Authenticator() {
 			protected PasswordAuthentication getPasswordAuthentication() {
 				return new PasswordAuthentication(emailUsername, emailPassword);
 			}
 		  });
  
 		try{
 		   
  			Message message = new MimeMessage(session);
  			message.setFrom(new InternetAddress("dineshkudale2@gmail.com"));
  			message.setRecipients(Message.RecipientType.TO,
  			InternetAddress.parse("dineshkudale2@gmail.com"));
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
			
			Template t = ve.getTemplate("/public/emailTemplate/agreementTemplate.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("userName", userName);
	        context.put("userDate", userDate);
	        context.put("userPhone", userPhone);
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("Agree mail");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ok();
	}
	
	
	public static Result logout() {
		session().clear();
		return ok(home.render(""));
	}
	
	public static Result home() {
		return ok(home.render(""));
	}
	public static Result test() {
		return ok(agreement.render("fdfdsf","fdsf","1"));
	}
	
	
    public static Result index() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return redirect("/login");
    	} else {
    		//return redirect("/dealer/index.html");
    		AuthUser user = getLocalUser();
    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
    		List<Permission> userPermissions = user.getPermission();
    		for(Permission per: userPermissions) {
    			permission.put(per.name, true);
    		}
    		return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0"));
    	}
    }
    
    public static Result changePermission(Long locationId,Integer managerId,String gmIsManager){
    	AuthUser user = AuthUser.findById(managerId);
    	user.deleteManyToManyAssociations("permission");
    	List<Permission> permissionList = Permission.getAllPermission();
    	 List<Permission> permissionData = new ArrayList<>();
    	if(gmIsManager.equals("1")) {
    		  
    		 for(Permission obj: permissionList) {
				   if(!obj.name.equals("Dealer's Profile") && !obj.name.equals("My Locations") && !obj.name.equals("Deactivate Locations")) {
					   permissionData.add(obj);
				   }
  		   }
  		   user.permission = permissionData;
    		   //user.permission.addAll(permissionList);
    		   
    	   }else{
    		   
    		   if(user.role.equals("General Manager")){
    			   for(Permission obj: permissionList) {
    				   if(obj.name.equals("CRM") || obj.name.equals("My Profile") || obj.name.equals("Dashboard") || obj.name.equals("Dealer's Profile") || obj.name.equals("My Locations") || obj.name.equals("Deactivate Locations")) {
    					   permissionData.add(obj);
    				   }
        		   }
        		   user.permission = permissionData;
    		   }
    		  
    	   }
    	user.update();
    	return ok();
    }
	
    public static Result getUserPermissions() {
    	AuthUser user = getLocalUser();
		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
		List<Permission> userPermissions = user.getPermission();
		for(Permission per: userPermissions) {
			permission.put(per.name, true);
		}
		return ok(Json.toJson(permission));
    }
    
    public static Result getUserInfo() {
    	AuthUser user = getLocalUser();
    	if(user.imageUrl== null){
    		user.imageUrl ="/profile-pic.jpg";
    	}
		return ok(Json.toJson(user));
	}
    
    public static AuthUser getLocalUser() {
    	String id = session("USER_KEY");
    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
    	//AuthUser user = getLocalUser();
		return user;
	}

    @SecureSocial.UserAwareAction
    public static Result userAware() {
        Identity user = getLocalUser();
        final String userName = user != null ? user.fullName() : "guest";
        return ok("Hello " + userName);
    }

    public static Result ajaxCall() {
        // return some json
    	return null;
    }
  
    public static Result getAllSites() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    	List<Site> siteList = Site.getAllSites();
    	List<SiteVM> vmList = new ArrayList<>();
    	for(Site site: siteList) {
    		SiteVM siteVM = new SiteVM();
    		siteVM.id = site.getId();
    		siteVM.name = site.getName();
    		vmList.add(siteVM);
    	}
    	
    	return ok(Json.toJson(vmList));
    	}
    }
    
    
    public static Result getVehicleInfo(String vin) throws IOException,Exception {
    	
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Location location=Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	Vehicle vehicle = Vehicle.findByVinAndStatusForGM(vin,location); 
	    	if(vehicle == null) {
	    		PinVM pinObj = new PinVM();
	    		if(!simulate ) {
	    		/*URL url;
					url = new URL("https://vindecoder.p.mashape.com/decode_vin?vin="+vin);
				URLConnection conn = url.openConnection();
				conn.setRequestProperty("X-Mashape-Key", mashapeKey);
				conn.setRequestProperty("Accept", "application/json");
				
				BufferedReader br = new BufferedReader(
		                           new InputStreamReader(conn.getInputStream()));
				
				StringBuilder sb = new StringBuilder();
				String line;
			    while ((line = br.readLine()) != null) {
			        sb.append(line);
			    }
			 
				ObjectMapper mapper = new ObjectMapper();
				
				pinObj = new ObjectMapper().readValue(sb.toString(), PinVM.class);*/
	    			
	    			pinObj.vin = vin;
	    			SpecificationVM specificationVM = new SpecificationVM();
	    			String postData = "client_id=11178&authorization_code=c382533644b1c8e3a0607671ea0caf1742961b84&decoder_query=";
	    		      //String vinNum = "SCFFDCCDXBGE12780";
	    		      String decoderQuery = URLEncoder.encode(
	    		    		  "{	\"decoder_settings\" : {	    \"version\" : \"7.0.1\",		\"display\" : \"full\",		\"styles\" : \"on\",		\"style_data_packs\" : {			\"basic_data\" : \"on\",			\"pricing\" : \"on\",			\"engines\" : \"on\",			\"transmissions\" : \"on\",			\"specifications\" : \"on\",			\"installed_equipment\" : \"on\",			\"optional_equipment\" : \"on\",			\"colors\" : \"on\",			\"safety_equipment\" : \"on\",			\"warranties\" : \"on\",			\"fuel_efficiency\" : \"on\"			},		\"common_data\" : \"on\",		\"common_data_packs\" : {			\"basic_data\" : \"on\",			\"pricing\" : \"on\",			\"engines\" : \"on\",			\"transmissions\" : \"on\",			\"specifications\" : \"on\",			\"installed_equipment\" : \"on\"		}	},	\"query_requests\" : {		\"Request-Sample\" : {			\"vin\" : \""+vin+"\",			\"year\" : \"on\",			\"make\" : \"on\",			\"model\" : \"on\",			\"trim\" : \"on\",			\"model_number\" : \"on\",			\"package_code\" : \"on\",			\"drive_type\" : \"on\",			\"vehicle_type\" : \"on\",			\"body_type\" : \"on\",			\"doors\" : \"on\",			\"bedlength\" : \"on\",			\"wheelbase\" : \"on\",			\"msrp\" : \"on\",			\"invoice_price\" : \"on\",			\"engine\" : {				\"description\" : \"on\",				\"block_type\" : \"on\",				\"cylinders\" : \"on\",				\"displacement\" : \"on\",				\"fuel_type\" : \"on\"			},			\"transmission\" : {				\"description\" : \"on\",				\"trans_type\" : \"on\",				\"trans_speeds\" : \"on\"			},			\"optional_equipment_codes\" : \"on\",			\"installed_equipment_descriptions\" : \"on\",			\"interior_color\" : {				\"description\" : \"on\",				\"color_code\" : \"on\"			},			\"exterior_color\" : {				\"description\" : \"on\",				\"color_code\" : \"on\"			}		}	}}"
	    		    		  ,"UTF-8");
	    	    	
	    		      
	    		      URL decoder_url = new URL("https://api.dataonesoftware.com/webservices/vindecoder/decode");
	    		       HttpsURLConnection connection = (HttpsURLConnection) decoder_url.openConnection();
	    		       connection.setRequestMethod("POST");
	    		       connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    		       connection.setDoInput(true);
	    		       connection.setDoOutput(true);
	    					
	    		       // Post the request data
	    		       DataOutputStream output = new DataOutputStream(connection.getOutputStream());
	    		       output.writeBytes(postData + decoderQuery);
	    		       output.flush();
	    		       output.close();
	    					
	    		       // Retrieve the response data
	    		       DataInputStream input = new DataInputStream(connection.getInputStream()); 
	    		       // read in each character until end-of-stream is detected 
	    		       StringBuilder json = new StringBuilder();
	    		       for (int c = input.read(); c != -1; c = input.read()) {
	    		    	   json.append((char)c);
	    		    	    
	    		       }
	    		       input.close();
	    		       //System.out.print( json.toString() );
	    		       ResponseData mapperObj = new com.fasterxml.jackson.databind.ObjectMapper().readValue(json.toString(), ResponseData.class);
	    		       
	    		       if(mapperObj.getQueryResponses().getRequestSample().getQueryError().getErrorMessage().equals("")) {
	    		    	   pinObj.success = true;
	    		       } else {
	    		    	   pinObj.success = false;
	    		    	   return ok(Json.toJson(pinObj));
	    		       }
	    		       
	    		       specificationVM.year = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getYear();
	    		       specificationVM.make = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getMake();
	    		       specificationVM.model = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getModel();
	    		       specificationVM.mileage = "";
	    		       specificationVM.bodyStyle = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getBodyType();
	    		       specificationVM.doors = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getDoors();
	    		       
	    		       for(Specification seating: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getSpecifications()) {
	    		    	   if(seating.getCategory().equals("Seating")) {
	    			    	   for(Specification_ spec: seating.getSpecifications()) {
	    			    		   if(spec.getName().equals("Max Seating")) {
	    			    			   specificationVM.standardSeating = spec.getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       
	    		       specificationVM.drivetrain = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getDriveType();
	    		       specificationVM.engine = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getDisplacement()+" L";
	    		       specificationVM.transmission = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getTransmissions().get(0).getDetailType();
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Brakes")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("ABS")) {
	    			    			   for(Value vl: spec.getValues()){
	    			    				   if(vl.getInstalledEquipmentId().equals("5000000168"))
	    			    					   specificationVM.brakes = vl.getValue();
	    			    			   }
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       specificationVM.horsePower = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getMaxHp();
	    		       
	    		       for(Specification seating: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getSpecifications()) {
	    		    	   if(seating.getCategory().equals("Performance Specifications")) {
	    			    	   for(Specification_ spec: seating.getSpecifications()) {
	    			    		   if(spec.getName().equals("Acceleration (0-60MPH)")) {
	    			    			   specificationVM.acceleration = spec.getValue()+" sec";
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       
	    		       specificationVM.extColor = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getColors().getExteriorColors().get(0).getMfrColorName();
	    		       specificationVM.intColor = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getColors().getInteriorColors().get(0).getMfrColorName();
	    		       specificationVM.vin = vin;
	    		       specificationVM.city_mileage = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getEpaFuelEfficiency().get(0).getCity();
	    		       specificationVM.highway_mileage = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getEpaFuelEfficiency().get(0).getHighway();
	    		       specificationVM.fuelType = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getFuelType();
	    		       
	    		       for(Specification seating: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getSpecifications()) {
	    		    	   if(seating.getCategory().equals("Fuel Tanks")) {
	    			    	   for(Specification_ spec: seating.getSpecifications()) {
	    			    		   if(spec.getName().equals("Fuel Tank 1 Capacity (Gallons)")) {
	    			    			   specificationVM.fuelTank = spec.getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Lights")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("Headlights")) {
	    			    			   for(Value vl: spec.getValues()){
	    			    				   if(vl.getInstalledEquipmentId().equals("5000001530"))
	    			    					   specificationVM.headlights = vl.getValue();
	    			    			   }
	    			    		   }
	    			    	   }
	    		    	   }
	    		       } 
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Mirrors")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("Exterior mirrors")) {
	    			    			   specificationVM.mirrors = spec.getValues().get(0).getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       } 
	    		       
	    		       for(Specification seating: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getSpecifications()) {
	    		    	   if(seating.getCategory().equals("Measurements of Size and Shape")) {
	    			    	   for(Specification_ spec: seating.getSpecifications()) {
	    			    		   if(spec.getName().equals("Ground Clearance")) {
	    			    			   specificationVM.groundClearance = spec.getValue();
	    			    		   }
	    			    		   if(spec.getName().equals("Height")) {
	    			    			   specificationVM.height = spec.getValue();
	    			    		   }
	    			    		   if(spec.getName().equals("Length")) {
	    			    			   specificationVM.length = spec.getValue();
	    			    		   }
	    			    		   if(spec.getName().equals("Width")) {
	    			    			   specificationVM.width = spec.getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       }
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Roof")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("Sunroof")) {
	    			    			   specificationVM.roof = spec.getValues().get(0).getValue();
	    			    		   }
	    			    	   }
	    		    	   }
	    		       } 
	    		      
	    		       specificationVM.engineType = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getBlockType();
	    		       specificationVM.cylinders = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getCylinders();
	    		       specificationVM.displacement = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getDisplacement()+" L";
	    		       specificationVM.camType = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getCamType();
	    		       specificationVM.valves = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getValves();
	    		       specificationVM.fuelQuality = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getEngines().get(0).getFuelQuality();
	    		       specificationVM.gears = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getTransmissions().get(0).getGears();
	    		       
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Brakes")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    				    		   if(spec.getName().equals("Front brake diameter")) {
	    				    			   specificationVM.frontBrakeDiameter = spec.getValues().get(0).getValue()+" inches";
	    				    			   }
	    				    		   
	    				    		   if(spec.getName().equals("Front brakes")) {
	    				    			   specificationVM.frontBrakeType = spec.getValues().get(0).getValue();
	    		    				   }
	    			    		   
	    				    		   if(spec.getName().equals("Rear brakes")) {
	    				    			   specificationVM.rearBrakeType = spec.getValues().get(0).getValue();
	    		    				   }
	    				    		   
	    				    		   if(spec.getName().equals("Rear brake diameter")) {
	    				    			   specificationVM.rearBrakeDiameter = spec.getValues().get(0).getValue()+" inches";
	    		    				   }
	    			    		   }
	    		    	   }
	    		       }
	    		       
	    		       specificationVM.comfortFeatures = "";
	    		       specificationVM.steeringWheelControls = "";
	    		       for(InstalledEquipment equip: mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getInstalledEquipment()) {
	    		    	   if(equip.getCategory().equals("Safety")) {
	    			    	   for(Equipment spec: equip.getEquipment()) {
	    			    		   if(spec.getName().equals("Active head restraints")) {
	    			    			   specificationVM.activeHeadRestraints = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Body side reinforcements")) {
	    			    			   specificationVM.bodySideReinforcements = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Crumple zones")) {
	    			    			   specificationVM.crumpleZones = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Impact absorbing bumpers")) {
	    			    			   specificationVM.impactAbsorbingBumpers = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Impact sensor")) {
	    			    			   specificationVM.impactSensors = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    		   if(spec.getName().equals("Parking sensors")) {
	    			    			   specificationVM.parkingSensors = spec.getValues().get(0).getValue();
	    			    		   }
	    			    		   
	    			    	   }
	    		    	   }
	    		    	   
	    		    	   if(equip.getCategory().equals("Seatbelts")) {
	    		    		   specificationVM.seatbelts = equip.getEquipment().get(0).getValues().get(0).getValue();
	    		    	   }
	    		    	   
	    		    	   
	    		    	   if(equip.getCategory().equals("Comfort Features")) {
	    		    		   for(Equipment spec: equip.getEquipment()) {
	    		    			   specificationVM.comfortFeatures = specificationVM.comfortFeatures+spec.getValues().get(0).getValue()+",";
	    		    		   }
	    		    	   }
	    		    	   
	    		    	   if(equip.getCategory().equals("Convenience Features")) {
	    		    		   for(Equipment spec: equip.getEquipment()) {
	    		    			   if(spec.getName().equals("Power outlet(s)")) {
	    		    				   specificationVM.powerOutlets = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Power steering")) {
	    		    				   specificationVM.powerSteering = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Rear view camera")) {
	    		    				   specificationVM.rearViewCamera = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Rear view monitor")) {
	    		    				   specificationVM.rearViewMonitor = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Remote trunk release")) {
	    		    				   specificationVM.remoteTrunkRelease = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Steering wheel")) {
	    		    				   specificationVM.steeringWheel = spec.getValues().get(0).getValue();
	    		    			   }
	    		    			   if(spec.getName().equals("Steering wheel mounted controls")) {
	    		    				   for(Value val: spec.getValues()) {
	    		    					   specificationVM.steeringWheelControls = specificationVM.steeringWheelControls+val.getValue()+",";
	    		    				   }
	    		    			   }
	    		    		   }
	    		    	   }
	    		    	   
	    		       } 
	    		       
	    		      for(OptionalEquipment opts:mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getUsStyles().get(0).getOptionalEquipment()) {
	    		    	  if(opts.getCategory().equals("Safety")) {
	    		    		  for(Option option : opts.getOptions()) {
	    		    			  if(option.getName().equals("Audi Side Assist")) {
	    		    				  specificationVM.audiSideAssist = option.getDescription();
	    		    			  }
	    		    		  }
	    		    		  
	    		    	  }
	    		      }
	    		       
	    		      specificationVM.trim_level = mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getBasicData().getTrim();
	    		      specificationVM.label = "";
	    		      specificationVM.stock = "";
	    		       
	    		      specificationVM.cost = Integer.parseInt(mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getPricing().getInvoicePrice());
	    		      specificationVM.price = Integer.parseInt(mapperObj.getQueryResponses().getRequestSample().getUsMarketData().getCommonUsData().getPricing().getMsrp());
	    		      specificationVM.colorDesc = "";
	    		      specificationVM.category = "";
	    		      specificationVM.stereo = "";
	    		       
	    		      specificationVM.fuel = "";
	    		      specificationVM.location = "";
	    		      specificationVM.made_in = "";
	    		      specificationVM.optional_seating = "";
	    		      specificationVM.status = "Newly Listed";
	    		      pinObj.specification = specificationVM;
	    		      
	    			
		    	} else {
		    		pinObj.success = true;
		    		pinObj.vin = "WDDNG7KB7DA494890";
					
					SpecificationVM specificationVM = new SpecificationVM();
					specificationVM.vin = "WDDNG7KB7DA494890";
					specificationVM.year = "2013";
					specificationVM.make = "Mercedes-Benz";
					specificationVM.model = "S-Class";
					specificationVM.trim_level = "S65 AMG";
					specificationVM.engine = "6.0L V12 SOHC 36V TURBO";
					specificationVM.style = "SEDAN 4-DR";
					specificationVM.made_in = "GERMANY";
					specificationVM.optional_seating = "";
					specificationVM.highway_mileage = "19 miles/gallon";
					specificationVM.city_mileage = "12 miles/gallon";
					pinObj.specification = specificationVM;
		    	}
		    	
				return ok(Json.toJson(pinObj));
				
	    	} else {
				PinVM pinVM = new PinVM();
				pinVM.success = true;
				pinVM.vin = vehicle.getVin();
				
				SpecificationVM specificationVM = new SpecificationVM();
				specificationVM.category = vehicle.getCategory();
				specificationVM.vin = vehicle.getVin();
				specificationVM.year = vehicle.getYear();
				specificationVM.make = vehicle.getMake();
				specificationVM.model = vehicle.getModel();
				specificationVM.trim_level = vehicle.getTrim();
				specificationVM.label = vehicle.getLabel();
				specificationVM.stock = vehicle.getStock();
				specificationVM.city_mileage = vehicle.getCityMileage();
				specificationVM.highway_mileage = vehicle.getHighwayMileage();
				specificationVM.cost = vehicle.getCost();
				specificationVM.price = vehicle.getPrice();
				specificationVM.made_in = vehicle.getMadeIn();
				specificationVM.optional_seating = vehicle.getOptionalSeating();
				specificationVM.extColor = vehicle.getExteriorColor();
				specificationVM.colorDesc = vehicle.getColorDescription();
				specificationVM.doors = vehicle.getDoors();
				specificationVM.stereo = vehicle.getStereo();
				specificationVM.engine = vehicle.getEngine();
				specificationVM.style = vehicle.getBodyStyle();
				specificationVM.location = vehicle.getLocation();
				specificationVM.description = vehicle.getDescription();
				
				
				specificationVM.drivetrain = vehicle.getDrivetrain();
				specificationVM.fuelType = vehicle.getFuelType();
				specificationVM.fuelTank = vehicle.getFuelTank();
				specificationVM.headlights = vehicle.getHeadlights();
				specificationVM.mirrors = vehicle.getMirrors();
				specificationVM.groundClearance = vehicle.getGroundClearance();
				specificationVM.roof = vehicle.getRoof();
				specificationVM.height = vehicle.getHeight();
				specificationVM.length = vehicle.getLength();
				specificationVM.width = vehicle.getWidth();
				specificationVM.acceleration = vehicle.getAcceleration();
				specificationVM.standardSeating = vehicle.getStandardSeating();
				specificationVM.engineType = vehicle.getEngineType();
				specificationVM.cylinders = vehicle.getCylinders();
				specificationVM.displacement = vehicle.getDisplacement();
				specificationVM.camType = vehicle.getCamType();
				specificationVM.valves = vehicle.getValves();
				specificationVM.fuelQuality = vehicle.getFuelQuality();
				specificationVM.horsePower = vehicle.getHorsePower();
				specificationVM.transmission = vehicle.getTransmission();
				specificationVM.gears = vehicle.getGears();
				specificationVM.brakes = vehicle.getBrakes();
				specificationVM.frontBrakeDiameter = vehicle.getFrontBrakeDiameter();
				specificationVM.frontBrakeType = vehicle.getFrontBrakeType();
				specificationVM.rearBrakeDiameter = vehicle.getRearBrakeDiameter();
				specificationVM.rearBrakeType = vehicle.getRearBrakeType();
				specificationVM.activeHeadRestraints = vehicle.getActiveHeadRestrains();
				specificationVM.bodySideReinforcements = vehicle.getBodySideReinforcements();
				specificationVM.crumpleZones = vehicle.getCrumpleZones();
				specificationVM.impactAbsorbingBumpers = vehicle.getImpactAbsorbingBumpers();
				specificationVM.impactSensors = vehicle.getImpactSensor();
				specificationVM.parkingSensors = vehicle.getParkingSensors();
				specificationVM.seatbelts = vehicle.getSeatbelts();
				specificationVM.audiSideAssist = vehicle.getAudiSideAssist();
				specificationVM.intColor = vehicle.getInteriorColor();
				specificationVM.comfortFeatures = vehicle.getComfortFeatures();
				specificationVM.powerOutlets = vehicle.getPowerOutlet();
				specificationVM.powerSteering = vehicle.getPowerSteering();
				specificationVM.rearViewCamera = vehicle.getRearViewCamera();
				specificationVM.rearViewMonitor = vehicle.getRearViewMonitor();
				specificationVM.remoteTrunkRelease = vehicle.getRemoteTrunkRelease();
				specificationVM.steeringWheel = vehicle.getSteeringWheel();
				specificationVM.steeringWheelControls = vehicle.getSteeringWheelControls();
				
				specificationVM.standardSeating = vehicle.getStandardSeating();
				
				specificationVM.mileage = vehicle.getMileage();
				
				List<Long> siteIds = new ArrayList<>();
				for(Site site: vehicle.getSite()) {
					siteIds.add(site.id);
				}
				specificationVM.siteIds = siteIds;
				
				pinVM.specification = specificationVM;
				
		    	return ok(Json.toJson(pinVM));
	    	}
    	}	
    }
    
    
    public static Result getUserRole(){
    	Identity user = getLocalUser();
    	AuthUser userObj = (AuthUser)user;
    	return ok(Json.toJson(userObj));
    }
    
    public static Result saveVehicle() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	Identity user = getLocalUser();
	    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
	    	SpecificationVM vm = form.get();
	    	AuthUser userObj = (AuthUser)user;
	    	Vehicle vehicleObj = Vehicle.findByVinAndStatus(vm.vin);
	    	Vehicle vehicle = new Vehicle();
	    	if(vehicleObj == null) {
		    	
	    		vehicle.setTitle(vm.make+" "+vm.model+" "+vm.year);
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
		    	vehicle.typeofVehicle=vm.typeofVehicle;
		    	
		    	vehicle.year = vm.year;
		    	vehicle.make = vm.make;
		    	vehicle.model = vm.model;
		    	vehicle.trim = vm.trim_level;
		    	vehicle.label = vm.label;
		    	vehicle.stock = vm.stock;
		    	vehicle.cityMileage = vm.city_mileage;
		        vehicle.mileage=vm.mileage;
		    	vehicle.highwayMileage = vm.highway_mileage;
		    	vehicle.cost = vm.cost;
		    	vehicle.price = vm.price;
		    	vehicle.madeIn = vm.made_in;
		    	vehicle.optionalSeating = vm.optional_seating;
		    	vehicle.exteriorColor = vm.extColor;
		    	vehicle.colorDescription = vm.colorDesc;
		    	vehicle.doors = vm.doors;
		    	vehicle.stereo = vm.stereo;
		    	vehicle.engine = vm.engine;
		    	vehicle.bodyStyle = vm.style;
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
		    	vehicle.activeHeadRestrains = vm.activeHeadRestraints;
		    	vehicle.bodySideReinforcements = vm.bodySideReinforcements;
		    	vehicle.crumpleZones = vm.crumpleZones;
		    	vehicle.impactAbsorbingBumpers = vm.impactAbsorbingBumpers;
		    	vehicle.impactSensor = vm.impactSensors;
		    	vehicle.parkingSensors = vm.parkingSensors;
		    	vehicle.seatbelts = vm.seatbelts;
		    	vehicle.audiSideAssist = vm.audiSideAssist;
		    	vehicle.interiorColor = vm.intColor;
		    	vehicle.comfortFeatures = vm.comfortFeatures;
		    	vehicle.powerOutlet = vm.powerOutlets;
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
				
		    	vehicle.madeIn = vm.made_in;
		    	vehicle.optionalSeating = vm.optional_seating;
		    	vehicle.status = "Newly Arrived";
		    	vehicle.postedDate = new Date();
		    	List<Site> siteList = new ArrayList<>();
		    	if(vm.siteIds != null) {
			    	for(Long obj: vm.siteIds) {
			    		Site siteObj = Site.findById(obj);
			    		siteList.add(siteObj);
			    	}
			    	vehicle.site = siteList;
		    	}
		    	vehicle.save();
	    	}
	    	sendEmailToBrandFollowers(vehicle.make);
	    	Vehicle vehicleObj2 = Vehicle.findByVinAndStatus(vm.vin);
	    	List<Site> siteList = vehicleObj2.getSite();
	    	//AuthUser aUser = AuthUser.getlocationAndManagerByType(Location.findById(Long.valueOf(session("USER_LOCATION"))), "Manager");
	    	//MyProfile profile = MyProfile.findByUser(aUser);
	    	List<MyProfile> profile = MyProfile.findByLocation(Long.valueOf(session("USER_LOCATION"))); //findByUser(userObj);
	    	
	    	if(!siteList.isEmpty()) {
		    	for(Site siteObj: siteList) {
		    		
		    		if(siteObj.getName().equals("CarsGuru")) {
		    			FTPClient client = new FTPClient();
		    	        FileInputStream fis = null;
		    	        client.connect("ftp.cargurus.com");
		                client.login("glider", "GLF8yP");
		                String filename = vm.vin+".csv";
		                CSVWriter writer = new CSVWriter(new FileWriter(filename));
		            	List<Vehicle> vehicleList = Vehicle.getAllVehicles(userObj);
		            	
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
		            	
		            	for(Vehicle vehicleData: vehicleList) {
		            		String []row = new String[29];
		            		row[0] = vehicleData.vin;
		            		row[1] = vehicleData.make;
		            		row[2] = vehicleData.model;
		            		row[3] = vehicleData.year;
		            		row[4] = vehicleData.trim;
		            		row[5] = vehicleData.price.toString();
		            		row[6] = vehicleData.mileage;
		            		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicleData.vin);
		            		String str = "";
		            		for(VehicleImage img : vImageList) {
		            			str = str +imageUrlPath+img.path+",";
		            		}
		            		row[7] = str;
		            		row[8] = vehicleData.exteriorColor;
		            		row[9] = "";
		            		row[10] = vehicleData.stock;
		            		row[11] = vehicleData.transmission;
		            		
		            		String standardFeatures = "";
		            		if(vehicleData.drivetrain != null) {
		            			standardFeatures = standardFeatures + vehicleData.drivetrain+",";
		            		}
		            		if(vehicleData.fuelType != null) {
		            			standardFeatures = standardFeatures + vehicleData.fuelType+",";
		            		}
		            		if(vehicleData.fuelTank != null) {
		            			standardFeatures = standardFeatures + vehicleData.fuelTank+",";
		            		}
		            		if(vehicleData.headlights != null) {
		            			standardFeatures = standardFeatures + vehicleData.headlights+",";
		            		}
		            		if(vehicleData.mirrors != null) {
		            			standardFeatures = standardFeatures + vehicleData.mirrors+",";
		            		}
		            		if(vehicleData.roof != null) {
		            			standardFeatures = standardFeatures + vehicleData.roof+",";
		            		}
		            		if(vehicleData.acceleration != null) {
		            			standardFeatures = standardFeatures + vehicleData.acceleration+",";
		            		}
		            		if(vehicleData.standardSeating != null) {
		            			standardFeatures = standardFeatures + vehicleData.standardSeating+",";
		            		}
		            		if(vehicleData.engine != null) {
		            			standardFeatures = standardFeatures + vehicleData.engine+",";
		            		}
		            		if(vehicleData.camType != null) {
		            			standardFeatures = standardFeatures + vehicleData.camType+",";
		            		}
		            		if(vehicleData.valves != null) {
		            			standardFeatures = standardFeatures + vehicleData.valves+",";
		            		}
		            		if(vehicleData.cylinders != null) {
		            			standardFeatures = standardFeatures + vehicleData.cylinders+",";
		            		}
		            		if(vehicleData.fuelQuality != null) {
		            			standardFeatures = standardFeatures + vehicleData.fuelQuality+",";
		            		}
		            		if(vehicleData.horsePower != null) {
		            			standardFeatures = standardFeatures + vehicleData.horsePower+",";
		            		}
		            		if(vehicleData.transmission != null) {
		            			standardFeatures = standardFeatures + vehicleData.transmission+",";
		            		}
		            		if(vehicleData.gears != null) {
		            			standardFeatures = standardFeatures + vehicleData.gears+",";
		            		}
		            		if(vehicleData.brakes != null) {
		            			standardFeatures = standardFeatures + vehicleData.brakes+",";
		            		}
		            		if(vehicleData.frontBrakeDiameter != null) {
		            			standardFeatures = standardFeatures + vehicleData.frontBrakeDiameter+",";
		            		}
		            		if(vehicleData.frontBrakeType != null) {
		            			standardFeatures = standardFeatures + vehicleData.frontBrakeType+",";
		            		}
		            		if(vehicleData.rearBrakeDiameter != null) {
		            			standardFeatures = standardFeatures + vehicleData.rearBrakeDiameter+",";
		            		}
		            		if(vehicleData.rearBrakeType != null) {
		            			standardFeatures = standardFeatures + vehicleData.rearBrakeType;
		            		}
		            		
		            		row[12] = standardFeatures;
		            		
		            		if(profile != null) {
		            			row[13] = profile.get(0).dealer_id;
		            			row[14] = profile.get(0).myname;
		            			row[15] = profile.get(0).address;
		            			row[16] = profile.get(0).city;
			            		row[17] = profile.get(0).state;
			            		row[18] = profile.get(0).zip;
			            		row[19] = profile.get(0).email;
			            		row[25] = profile.get(0).latlong;
			            		row[27] = profile.get(0).phone;
			            		row[28] = profile.get(0).web;
		            		} else {
		            			row[13] = "";
		            			row[14] = "";
		            			row[15] = "";
		            			row[16] = "";
			            		row[17] = "";
			            		row[18] = "";
			            		row[19] = "";
			            		row[25] = "";
			            		row[27] = "";
			            		row[28] = "";
		            		}
		            		
		            		row[20] = vehicleData.getPrice().toString();
		            		row[21] = vehicleData.interiorColor;
		            		row[22] = "";
		            		row[23] = "N";
		            		row[24] = vehicleData.engine;
		            		row[26] = "1000";
		            		
		            		writer.writeNext(row);
		            	}
		            	
		            	 writer.close();
		                fis = new FileInputStream(filename);
		                client.storeFile(filename, fis);
		                client.logout();
		                if (fis != null) {
		                    fis.close();
		                }
		    		}   
		    		
		    		
		    		if(siteObj.getName().equals("Carfax")) {
		    			FTPClient client = new FTPClient();
		    	        FileInputStream fis = null;
		    	        client.connect("ftp.vast.com");
		                client.login("gliderllc.com", "73f1vvRw4a");
		                String filename = vm.vin+".csv";
		                CSVWriter writer = new CSVWriter(new FileWriter(filename));
		    	    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(userObj);
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
		    			
		    	    	for(Vehicle vehicleCarfax: vehicleList) {
		    	    		String []row = new String[48];
		    	    		row[0] = "12345678";
		    	    		row[1] = vehicleCarfax.vin;
		    	    		row[2] = vehicleCarfax.stock;
		    	    		row[3] = vehicleCarfax.year+" "+vehicleCarfax.make+" "+vehicleCarfax.model;
		    	    		row[4] = vehicleUrlPath+vehicleCarfax.vin;
		    	    		row[5] = vehicleCarfax.category;
		    	    		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicleCarfax.vin);
		    	    		String str = "";
		    	    		for(VehicleImage img : vImageList) {
		    	    			str = str +imageUrlPath+img.path+"|";
		    	    		}
		    	    		row[6] = str;
		    	    		if(profile != null) {
		    	    			row[7] = profile.get(0).address;
		    	    		} else {
		    	    			row[7] = "";
		    	    		}
		    	    		row[8] = "San Francisco";
		    	    		row[9] = "CA";
		    	    		row[10] = "94105";
		    	    		row[11] = "United States";
		    	    		row[12] = "Dealer";
		    	    		if(profile != null) {
		    	    			row[13] = profile.get(0).myname;
		    	    			row[14] = profile.get(0).dealer_id;
			    	    		row[15] = profile.get(0).email;
			    	    		row[16] = profile.get(0).phone;
			    	    		row[17] = profile.get(0).web;
		    	    		} else {
		    	    			row[13] = "";
		    	    			row[14] = "";
			    	    		row[15] = "";
			    	    		row[16] = "";
			    	    		row[17] = "";
		    	    		}
		    	    		
		    	    		row[18] = "";
		    	    		row[19] = vehicleCarfax.make;
		    	    		row[20] = vehicleCarfax.model;
		    	    		row[21] = vehicleCarfax.trim;
		    	    		row[22] = vehicleCarfax.bodyStyle;
		    	    		row[23] = vehicleCarfax.mileage;
		    	    		row[24] = vehicleCarfax.year;
		    	    		row[25] = "USD";
		    	    		row[26] = vehicleCarfax.price.toString();
		    	    		row[27] = vehicleCarfax.getPrice().toString();
		    	    		row[28] = "";
		    	    		row[29] = "";
		    	    		row[30] = "";
		    	    		row[31] = "";
		    	    		row[32] = vehicleCarfax.exteriorColor;
		    	    		row[33] = vehicleCarfax.interiorColor;
		    	    		row[34] = "fabric";
		    	    		row[35] = vehicleCarfax.doors;
		    	    		row[36] = vehicleCarfax.cylinders;
		    	    		row[37] = vehicleCarfax.engine;
		    	    		row[38] = vehicleCarfax.drivetrain;
		    	    		row[39] = vehicleCarfax.transmission;
		    	    		row[40] = "YES";
		    	    		row[41] = vehicleCarfax.description; //description
		    	    		
		    	    		String standardFeatures = "";
		    	    		if(vehicleCarfax.drivetrain != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.drivetrain+",";
		    	    		}
		    	    		if(vehicleCarfax.fuelType != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.fuelType+",";
		    	    		}
		    	    		if(vehicleCarfax.fuelTank != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.fuelTank+",";
		    	    		}
		    	    		if(vehicleCarfax.headlights != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.headlights+",";
		    	    		}
		    	    		if(vehicleCarfax.mirrors != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.mirrors+",";
		    	    		}
		    	    		if(vehicleCarfax.roof != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.roof+",";
		    	    		}
		    	    		if(vehicleCarfax.acceleration != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.acceleration+",";
		    	    		}
		    	    		if(vehicleCarfax.standardSeating != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.standardSeating+",";
		    	    		}
		    	    		if(vehicleCarfax.engine != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.engine+",";
		    	    		}
		    	    		if(vehicleCarfax.camType != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.camType+",";
		    	    		}
		    	    		if(vehicleCarfax.valves != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.valves+",";
		    	    		}
		    	    		if(vehicleCarfax.cylinders != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.cylinders+",";
		    	    		}
		    	    		if(vehicleCarfax.fuelQuality != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.fuelQuality+",";
		    	    		}
		    	    		if(vehicleCarfax.horsePower != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.horsePower+",";
		    	    		}
		    	    		if(vehicleCarfax.transmission != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.transmission+",";
		    	    		}
		    	    		if(vehicleCarfax.gears != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.gears+",";
		    	    		}
		    	    		if(vehicleCarfax.brakes != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.brakes+",";
		    	    		}
		    	    		if(vehicleCarfax.frontBrakeDiameter != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.frontBrakeDiameter+",";
		    	    		}
		    	    		if(vehicleCarfax.frontBrakeType != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.frontBrakeType+",";
		    	    		}
		    	    		if(vehicleCarfax.rearBrakeDiameter != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.rearBrakeDiameter+",";
		    	    		}
		    	    		if(vehicleCarfax.rearBrakeType != null) {
		    	    			standardFeatures = standardFeatures + vehicleCarfax.rearBrakeType;
		    	    		}
		    	    		row[42] = standardFeatures;
		    	    		
		    	    		
		    	    		String standardOptions = "";
		    	    		
		    	    		if(vehicleCarfax.activeHeadRestrains != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.activeHeadRestrains+",";
		    	    		}
		    	    		if(vehicleCarfax.bodySideReinforcements != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.bodySideReinforcements+",";
		    	    		}
		    	    		if(vehicleCarfax.crumpleZones != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.crumpleZones+",";
		    	    		}
		    	    		if(vehicleCarfax.impactAbsorbingBumpers != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.impactAbsorbingBumpers+",";
		    	    		}
		    	    		if(vehicleCarfax.impactSensor != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.impactSensor+",";
		    	    		}
		    	    		if(vehicleCarfax.parkingSensors != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.parkingSensors+",";
		    	    		}
		    	    		if(vehicleCarfax.seatbelts != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.seatbelts+",";
		    	    		}
		    	    		if(vehicleCarfax.interiorColor != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.interiorColor+",";
		    	    		}
		    	    		if(vehicleCarfax.powerOutlet != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.powerOutlet+",";
		    	    		}
		    	    		if(vehicleCarfax.powerSteering != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.powerSteering+",";
		    	    		}
		    	    		if(vehicleCarfax.rearViewCamera != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.rearViewCamera+",";
		    	    		}
		    	    		if(vehicleCarfax.rearViewMonitor != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.rearViewMonitor+",";
		    	    		}
		    	    		if(vehicleCarfax.remoteTrunkRelease != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.remoteTrunkRelease+",";
		    	    		}
		    	    		if(vehicleCarfax.steeringWheel != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.steeringWheel+",";
		    	    		}
		    	    		if(vehicleCarfax.steeringWheelControls != null) {
		    	    			standardOptions = standardOptions + vehicleCarfax.steeringWheelControls;
		    	    		}
		    	    		
		    	    		row[43] = standardOptions;
		    	    		row[44] = "";
		    	    		row[45] = "Used";
		    	    		row[46] = "2012-09-16-11:00:00";
		    	    		row[47] = "2012-10-16-11:00:00";
		    	    		
		    	    		writer.writeNext(row);
		    	    	}
		    	    	
		    	    	 writer.close();
		    	    	 fis = new FileInputStream(filename);
			                client.storeFile(filename, fis);
			                client.logout();
			                if (fis != null) {
			                    fis.close();
			                }
		    		}
		    		
		    		
		    	}
	    	}
	    	
	    	return ok();
    	}	
    }
    public static Result getLocationDays(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser userObj = (AuthUser) getLocalUser();
    		Location loc= Location.findById(userObj.location.id);
    		Date date = new Date();
    		long diff;
    		long days = 0;
    		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
    		String modifiedDate= myFormat.format(new Date());
    		try {
    			date = myFormat.parse(modifiedDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		if(loc.createdDate !=null){
    			diff = date.getTime() - loc.createdDate.getTime();
    			days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    		}
    	
    		return ok(Json.toJson(days));
    	}
    }
    
    public static Result uploadLocationImageFile(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    
    public static Result UpdateuploadManagerImageFile(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
	    	String flag = "0";
	    	UserVM vm = form.get();
	    	String roles = "Manager";
	    	AuthUser userObj = null;
	    	if(vm.mi.equals("true")){
	    		
	    		 AuthUser uAuthUser = AuthUser.getlocationAndManagerOne(Location.findById(vm.locationId));
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
	    		
	    		 AuthUser uAuthUser = AuthUser.getlocationAndManagerOne(Location.findById(vm.locationId));
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
    
    public static Result uploadManagerImageFile(){
    	
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	    		Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", "smtp.gmail.com");
		 		props.put("mail.smtp.port", "587");
		  
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUsername, emailPassword);
		 			}
		 		  });
		  
		 		try{
		 		   
		  			Message message = new MimeMessage(session);
		  			message.setFrom(new InternetAddress("glider.autos@gmail.com"));
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
    
    public static Result uploadPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	String vin = request().getHeader("vinNum");
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+vin+"-"+userObj.id);
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+vin+"-"+userObj.id+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+vin+"-"+userObj.id+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				VehicleImage imageObj = VehicleImage.getByImagePath("/"+session("USER_LOCATION")+"/"+vin+"-"+userObj.id+"/"+fileName);
				if(imageObj == null) {
					VehicleImage vImage = new VehicleImage();
					vImage.vin = vin;
					vImage.imgName = fileName;
					vImage.path = "/"+session("USER_LOCATION")+"/"+vin+"-"+userObj.id+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+vin+"-"+userObj.id+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.save();
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	return ok();
    	}	
    }
    
    
    public static Result getImagesByVin(String vin) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	List<VehicleImage> imageList = VehicleImage.getByVin(vin);
	    	reorderImagesForFirstTime(imageList);
	    	List<ImageVM> vmList = new ArrayList<>();
	    	for(VehicleImage image : imageList) {
	    		ImageVM vm = new ImageVM();
	    		vm.id = image.id;
	    		vm.imgName = image.imgName;
	    		vm.defaultImage = image.defaultImage;
	    		vm.row = image.row;
	    		vm.col = image.col;
	    		vm.path = image.path;
	    		vmList.add(vm);
	    	}
	    	return ok(Json.toJson(vmList));
    	}	
    }
    
    
    private static void reorderImagesForFirstTime(List<VehicleImage> imageList) {
    	if(imageList.size() > 0) {
    		if(imageList.get(0).row == null) {
    			for(int i = 0, col = 0 ; i < imageList.size() ; i++) {
    				imageList.get(i).setRow(  col / 6);
    				imageList.get(i).setCol( col % 6);
    				col++;
    				imageList.get(i).update();
    			}
    			
    		}
    		
    	}
		
    }
    
	
    public static Result getImageById(Long id, String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	File file = null;
	    	VehicleImage image = VehicleImage.findById(id);
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	return ok(file);
    	}	
    }
    
	
    public static Result getImageDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	VehicleImage image = VehicleImage.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.imgName;
			vm.defaultImage = image.defaultImage;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.vin = image.vin;
	    	return ok(Json.toJson(vm));
    	}	
    }
	
    
    public static Result deleteImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	VehicleImage image = VehicleImage.findById(id);
	    	File file = new File(rootDir+File.separator+image.vin+"-"+user.id+File.separator+image.imgName);
	    	File thumbFile = new File(rootDir+File.separator+image.vin+"-"+user.id+File.separator+"thumbnail_"+image.imgName);
	    	file.delete();
	    	thumbFile.delete();
	    	image.delete();
	    	return ok();
    	}
    }
    
    
    public static Result setDefaultImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	VehicleImage image = VehicleImage.findById(id);
	    	image.defaultImage = (true);
	    	image.update();
	    	return ok();
    	}
    }
    
    
    
    public static Result removeDefault(Long old,Long newId) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	VehicleImage image = VehicleImage.findById(old);
	    	image.defaultImage = (false);
	    	image.update();
	    	
	    	VehicleImage newImage = VehicleImage.findById(newId);
	    	newImage.defaultImage = (true);
	    	newImage.update();
	    	return ok();
    	}	
    }
    
    public static Result editLeads(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
    		Date currDate = new Date();
	    	Form<LeadVM> form = DynamicForm.form(LeadVM.class).bindFromRequest();
	    	LeadVM vm = form.get();
	    	int parentFlag = 0;
	    	Long parentLeadId = 0L;
	    	Date reqDate = null;
	    	for(VehicleVM vehicleVM:vm.stockWiseData){
	    		if(parentFlag == 0){
	    			
		    	if(vm.leadType.equals("Request More Info")){
		    		RequestMoreInfo rInfo = RequestMoreInfo.findById(Long.parseLong(vm.id));
		    		if(rInfo != null){
		    			rInfo.setVin(vehicleVM.vin);
		    			rInfo.setName(vm.custName);
		    			rInfo.setEmail(vm.custEmail);
		    			rInfo.setPhone(vm.custNumber);
		    			rInfo.setCustZipCode(vm.custZipCode);
		    			rInfo.setEnthicity(vm.enthicity);
		    			rInfo.update();
		    			
		    			reqDate = rInfo.requestDate;
		    			
			    		if(parentFlag == 0){
			    			parentFlag = 1;
			    			parentLeadId = rInfo.getId();
			    		}
		    			
		    			UserNotes uNotes = new UserNotes();
		        		uNotes.setNote("Client interested in another vehicle");
		        		uNotes.setAction("Other");
		        		uNotes.createdDate = currDate;
		        		uNotes.createdTime = currDate;
		        		uNotes.user = user;
		        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		        		uNotes.requestMoreInfo = RequestMoreInfo.findById(rInfo.id);
		        		uNotes.save();
		    		}
		    	}else if(vm.leadType.equals("Schedule Test Drive")){
		    		ScheduleTest sInfo = ScheduleTest.findById(Long.parseLong(vm.id));
		    		if(sInfo != null){
		    			sInfo.setVin(vehicleVM.vin);
		    			sInfo.setName(vm.custName);
		    			sInfo.setEmail(vm.custEmail);
		    			sInfo.setPhone(vm.custNumber);
		    			sInfo.setCustZipCode(vm.custZipCode);
		    			sInfo.setEnthicity(vm.enthicity);
		    			sInfo.update();
		    			
		    			reqDate = sInfo.scheduleDate;
		    			
		    			if(parentFlag == 0){
			    			parentFlag = 1;
			    			parentLeadId = sInfo.getId();
			    		}
		    			
		    			UserNotes uNotes = new UserNotes();
		        		uNotes.setNote("Client interested in another vehicle");
		        		uNotes.setAction("Other");
		        		uNotes.createdDate = currDate;
		        		uNotes.createdTime = currDate;
		        		uNotes.user = user;
		        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		        		uNotes.scheduleTest = ScheduleTest.findById(sInfo.id);
		        		uNotes.save();
		    		}
		    	}else if(vm.leadType.equals("Trade-In Appraisal")){
		    		TradeIn tInfo = TradeIn.findById(Long.parseLong(vm.id));
		    		if(tInfo != null){
		    			tInfo.setVin(vehicleVM.vin);
		    			tInfo.setFirstName(vm.custName);
		    			tInfo.setEmail(vm.custEmail);
		    			tInfo.setPhone(vm.custNumber);
		    			tInfo.setCustZipCode(vm.custZipCode);
		    			tInfo.setEnthicity(vm.enthicity);
		    			tInfo.update();
		    			
		    			reqDate = tInfo.tradeDate;
		    			
		    			if(parentFlag == 0){
			    			parentFlag = 1;
			    			parentLeadId = tInfo.getId();
			    		}
		    			
		    			UserNotes uNotes = new UserNotes();
		        		uNotes.setNote("Client interested in another vehicle");
		        		uNotes.setAction("Other");
		        		uNotes.createdDate = currDate;
		        		uNotes.createdTime = currDate;
		        		uNotes.user = user;
		        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		        		uNotes.tradeIn = TradeIn.findById(tInfo.id);
		        		uNotes.save();
		    		}
		    	}
	    	}else{
	    		if(vm.leadType.equals("Request More Info")){
	    			
	    			RequestMoreInfo rInfo = RequestMoreInfo.findById(Long.parseLong(vm.id));
	    			
	    	    		RequestMoreInfo info = new RequestMoreInfo();
	    	    		info.setIsReassigned(true);
	    	    		info.setLeadStatus(null);
	    	    		info.setEmail(vm.custEmail);
	    	    		info.setName(vm.custName);
	    	    		info.setPhone(vm.custNumber);
	    	    		info.setCustZipCode(vm.custZipCode);
	    	    		info.setEnthicity(vm.enthicity);
	    	    		
	    	    		Vehicle vehicle = Vehicle.findByStockAndNew(vehicleVM.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    	    		info.setVin(vehicle.getVin());
	    	    		info.setUser(user);
	    				info.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	    		info.setIsScheduled(false);
	    	    		info.setIsRead(1);
	    	    		info.setHearedFrom(rInfo.hearedFrom);
	    	    		info.setContactedFrom(rInfo.contactedFrom);
	    	    		info.setAssignedTo(user);
	    	    		info.setOnlineOrOfflineLeads(1);
	    	    		info.setRequestDate(reqDate);
	    	    		/*PremiumLeads pLeads = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	    		if(pLeads != null){
	        				if(Integer.parseInt(pLeads.premium_amount) <= vehicle.price){
	        					info.setPremiumFlag(1);
	        				}else{
	        					info.setPremiumFlag(0);
	        					if(pLeads.premium_flag == 0){
	        						info.setAssignedTo(user);
	        					}
	        				}
	        				if(pLeads.premium_flag == 1){
	        					AuthUser aUser = AuthUser.getlocationAndManagerOne(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	        					info.setAssignedTo(aUser);
	        				}
	        			
	    	    		}else{
	    	    			info.setPremiumFlag(0);
	    	    			info.setAssignedTo(user);
	    	    		}*/
	    	    		
	    	    		if(parentFlag == 1){
	    	    			info.setParentId(parentLeadId);
	    	    		}
	    	    		
	    	    		info.save();
	    	    		
	    	    		if(parentFlag == 0){
	    	    			parentFlag = 1;
	    	    			parentLeadId = info.getId();
	    	    		}
	    	    		
	    	    		UserNotes uNotes = new UserNotes();
	    	    		uNotes.setNote("Lead has been created");
	    	    		uNotes.setAction("Other");
	    	    		uNotes.createdDate = currDate;
	    	    		uNotes.createdTime = currDate;
	    	    		uNotes.saveHistory = 1;
	    	    		uNotes.user = user;
	    	    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
	    	    		uNotes.save();
	    	    		
	    	    		/*if(info.premiumFlag == 1){
	    	    			sendMailpremium();
	    	    		}*/
	    	    		
	        	}else if(vm.leadType.equals("Schedule Test Drive")){
	        		
	        		ScheduleTest sInfo = ScheduleTest.findById(Long.parseLong(vm.id));
	        		Date confirmDate = null;
	        		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	        		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
	        		
	    	    		ScheduleTest test = new ScheduleTest();
	    	    		
	    	    		test.setIsReassigned(true);
	    	    		test.setLeadStatus(null);
	    	    		test.setBestDay(sInfo.bestDay);
	    	    		test.setBestTime(sInfo.bestTime);
	    	    		/*try {
	    	    			confirmDate = df.parse(sInfo.bestDay);
	    	    			test.setConfirmDate(confirmDate);
	    	    		} catch(Exception e) {}
	    	    		try {
	    	    			test.setConfirmTime(parseTime.parse(sInfo.bestTime));
	    	    		} catch(Exception e) {}*/
	    	    		test.setEmail(vm.custEmail);
	    	    		test.setName(vm.custName);
	    	    		test.setPhone(vm.custNumber);
	    	    		test.setCustZipCode(vm.custZipCode);
	    	    		test.setEnthicity(vm.enthicity);
	    	    		test.setIsRead(1);
	    	    		test.setUser(user);
	    				test.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	    		test.setHearedFrom(sInfo.hearedFrom);
	    	    		test.setContactedFrom(sInfo.contactedFrom);
	    	    		//test.setScheduleDate(new Date());
	    	    		test.setScheduleDate(reqDate);
	    	    		test.setPreferredContact(sInfo.preferredContact);
	    	    		Vehicle vehicle = Vehicle.findByStockAndNew(vehicleVM.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    	    		test.setVin(vehicle.getVin());
	    	    		test.setAssignedTo(user);
	    	    		test.setOnlineOrOfflineLeads(1);
	    	    		//test.setVin(vehicles.get(0).getVin());
	    	    		
	    	    	/*	PremiumLeads pLeads = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	    		if(pLeads != null){
	        				if(Integer.parseInt(pLeads.premium_amount) <= vehicle.price){
	        					test.setPremiumFlag(1);
	        				}else{
	        					test.setPremiumFlag(0);
	        					if(pLeads.premium_flag == 0){
	        						test.setAssignedTo(user);
	        					}
	        				}
	        				if(pLeads.premium_flag == 1){
	        					AuthUser aUser = AuthUser.getlocationAndManagerOne(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	        					test.setAssignedTo(aUser);
	        				}
	        			
	    	    		}else{
	    	    			test.setPremiumFlag(0);
	    	    			test.setAssignedTo(user);
	    	    		}*/
	    	    		
	    	    		if(parentFlag == 1){
	    	    			test.setParentId(parentLeadId);
	    	    		}
	    	    		
	    	    		test.save();
	    	    		
	    	    		if(parentFlag == 0){
	    	    			parentFlag = 1;
	    	    			parentLeadId = test.getId();
	    	    		}
	    	    		
	    	    		UserNotes uNotes = new UserNotes();
	    	    		uNotes.setNote("Lead has been created");
	    	    		uNotes.setAction("Other");
	    	    		uNotes.createdDate = currDate;
	    	    		uNotes.createdTime = currDate;
	    	    		uNotes.saveHistory = 1;
	    	    		uNotes.user = user;
	    	    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	    		uNotes.scheduleTest = ScheduleTest.findById(test.id);
	    	    		uNotes.save();
	    	    		
	    	    		Map map = new HashMap();
	    	    		map.put("email",user.getEmail());
	    	    		map.put("confirmDate", confirmDate);
	    	    		map.put("confirmTime",sInfo.bestTime);
	    	    		map.put("vin", vehicle.getVin());
	    	    		map.put("uname", user.firstName+" "+user.lastName);
	    	    		map.put("uphone", user.phone);
	    	    		map.put("uemail", user.email);
	    	    		makeToDo(vehicle.vin);
	    	    		/*if(test.premiumFlag == 1){
	    	    			sendMailpremium();
	    	    		}*/
	    	    		
	    	    		
	    	    		
	        		
	        	}else if(vm.leadType.equals("Trade-In Appraisal")){
		    		TradeIn leadVM = TradeIn.findById(Long.parseLong(vm.id));
	        		
	        			TradeIn tradeIn = new TradeIn();
	            		tradeIn.setAccidents(leadVM.accidents);
	            		tradeIn.setEnthicity(leadVM.enthicity);
	            		
	            		tradeIn.setIsReassigned(true);
	            		tradeIn.setLeadStatus(null);
	            		tradeIn.setBodyRating(leadVM.bodyRating);
	            		tradeIn.setComments(leadVM.comments);
	            		tradeIn.setContactedFrom(leadVM.contactedFrom);
	            		tradeIn.setDamage(leadVM.damage);
	            		tradeIn.setDoors(leadVM.doors);
	            		tradeIn.setDrivetrain(leadVM.drivetrain);
	            		tradeIn.setEmail(vm.custEmail);
	            		tradeIn.setCustZipCode(leadVM.custZipCode);
	            		tradeIn.setEngine(leadVM.engine);
	            		tradeIn.setEngineRating(leadVM.engineRating);
	            		tradeIn.setEquipment(leadVM.equipment);
	            		tradeIn.setExhaustRating(leadVM.exhaustRating);
	            		tradeIn.setExteriorColour(leadVM.exteriorColour);
	            		tradeIn.setFirstName(vm.custName);
	            		tradeIn.setGlassRating(leadVM.glassRating);
	            		tradeIn.setHearedFrom(leadVM.hearedFrom);
	            		tradeIn.setInteriorRating(leadVM.interiorRating);
	            		tradeIn.setIsRead(1);
	            		tradeIn.setKilometres(leadVM.kilometres);
	            		tradeIn.setLeaseOrRental(leadVM.leaseOrRental);
	            		tradeIn.setLienholder(leadVM.lienholder);
	            		tradeIn.setMake(vm.make);
	            		tradeIn.setModel(vm.model);
	            		tradeIn.setOperationalAndAccurate(leadVM.operationalAndAccurate);
	            		tradeIn.setOptionValue(leadVM.optionValue);
	            		tradeIn.setPaint(leadVM.paint);
	            		tradeIn.setPhone(vm.custNumber);
	            		tradeIn.setPreferredContact(leadVM.preferredContact);
	            		tradeIn.setSalvage(leadVM.salvage);
	            		tradeIn.setScheduleDate(new Date());
	            		tradeIn.setTireRating(leadVM.tireRating);
	            		tradeIn.setTradeDate(reqDate);
	            		tradeIn.setTransmission(leadVM.transmission);
	            		tradeIn.setUser(user);
	        			tradeIn.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	            		tradeIn.setVehiclenew(leadVM.vehiclenew);
	            		Vehicle vehicle = Vehicle.findByStockAndNew(vm.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
	            		tradeIn.setVin(vm.vin);
	            		tradeIn.setAssignedTo(user);
	            		tradeIn.setOnlineOrOfflineLeads(1);
	            		//tradeIn.setVin(vehicles.get(0).getVin());
	            		tradeIn.setYear(vm.year);
	            		
	            	/*	PremiumLeads pLeads = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
	            		if(pLeads != null){
	        				if(Integer.parseInt(pLeads.premium_amount) <= vehicle.price){
	        					tradeIn.setPremiumFlag(1);
	        				}else{
	        					tradeIn.setPremiumFlag(0);
	        					if(pLeads.premium_flag == 0){
	        						tradeIn.setAssignedTo(user);
	        					}
	        				}
	        				if(pLeads.premium_flag == 1){
	        					AuthUser aUser = AuthUser.getlocationAndManagerOne(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	        					tradeIn.setAssignedTo(aUser);
	        				}
	        			
	    	    		}else{
	    	    			tradeIn.setPremiumFlag(0);
	    	    			tradeIn.setAssignedTo(user);
	    	    		}*/
	            		
	            		if(parentFlag == 1){
	            			tradeIn.setParentId(parentLeadId);
	    	    		}
	    	    		
	            		tradeIn.save();
	    	    		
	    	    		if(parentFlag == 0){
	    	    			parentFlag = 1;
	    	    			parentLeadId = tradeIn.getId();
	    	    		}
	            		
	            		UserNotes uNotes = new UserNotes();
	            		uNotes.setNote("Lead has been created");
	            		uNotes.setAction("Other");
	            		uNotes.createdDate = currDate;
	            		uNotes.createdTime = currDate;
	            		uNotes.saveHistory = 1;
	            		uNotes.user = user;
	            		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	            		uNotes.tradeIn = tradeIn.findById(tradeIn.id);
	            		uNotes.save();
	            		
	            		/*if(tradeIn.premiumFlag == 1){
	    	    			sendMailpremium();
	    	    		}*/
	        		
	        		
	        		VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.getVin());
	        		
	        		AuthUser defaultUser = getLocalUser();
	        		//AuthUser defaultUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
	        		SiteLogo siteLogo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
	        		SiteContent siteContent = SiteContent.findByLocation(Long.valueOf(session("USER_LOCATION")));
	        		String heading1 = "",heading2 = "";
	        		if(siteContent.getHeading()!=null) {
	        		    int index= siteContent.getHeading().lastIndexOf(" ");
	        		    heading1 = siteContent.getHeading().substring(0, index);
	        		    heading2 = siteContent.getHeading().substring(index+1);
	        		}
	        		String filepath = null,findpath = null;

	    			try {
	    				Document document = new Document();
	    				createDir(pdfRootDir, Long.parseLong(session("USER_LOCATION")), tradeIn.getId());
	    				filepath = pdfRootDir + File.separator + Long.parseLong(session("USER_LOCATION"))
	    						+ File.separator + "trade_in_pdf" + File.separator
	    						+ tradeIn.getId() + File.separator + "Trade_In.pdf";
	    				findpath = File.separator + Long.parseLong(session("USER_LOCATION")) + File.separator + "trade_in_pdf" + File.separator
	    						+ tradeIn.getId() + File.separator + "Trade_In.pdf";
	    				// UPDATE table_name
	    				// SET column1=value1,column2=value2,...
	    				// WHERE some_column=some_value;
	    				TradeIn tIn2 = TradeIn.findById(tradeIn.getId());
	    				tIn2.setPdfPath(findpath);
	    				tIn2.update();
	    				PdfWriter pdfWriter = PdfWriter.getInstance(document,
	    						new FileOutputStream(filepath));

	    				// Properties
	    				document.addAuthor("Celinio");
	    				document.addCreator("Celinio");
	    				document.addSubject("iText with Maven");
	    				document.addTitle("Trade-In Appraisal");
	    				document.addKeywords("iText, Maven, Java");

	    				document.open();

	    				/* Chunk chunk = new Chunk("Fourth tutorial"); */
	    				Font font = new Font();
	    				font.setStyle(Font.UNDERLINE);
	    				font.setStyle(Font.ITALIC);
	    				/* chunk.setFont(font); */
	    				// chunk.setBackground(Color.BLACK);
	    				/* document.add(chunk); */

	    				Font font1 = new Font(FontFamily.HELVETICA, 8, Font.NORMAL,
	    						BaseColor.BLACK);
	    				Font font2 = new Font(FontFamily.HELVETICA, 8, Font.BOLD,
	    						BaseColor.BLACK);

	    				PdfPTable Titlemain = new PdfPTable(1);
	    				Titlemain.setWidthPercentage(100);
	    				float[] TitlemainWidth = { 2f };
	    				Titlemain.setWidths(TitlemainWidth);

	    				PdfPCell title = new PdfPCell(new Phrase("Trade-IN Appraisal"));
	    				title.setBorderColor(BaseColor.WHITE);
	    				title.setBackgroundColor(new BaseColor(255, 255, 255));
	    				Titlemain.addCell(title);

	    				PdfPTable contactInfo = new PdfPTable(4);
	    				contactInfo.setWidthPercentage(100);
	    				float[] contactInfoWidth = { 2f, 2f, 2f, 2f };
	    				contactInfo.setWidths(contactInfoWidth);

	    				PdfPCell firstname = new PdfPCell(new Phrase("First Name:",
	    						font1));
	    				firstname.setBorderColor(BaseColor.WHITE);
	    				firstname.setBackgroundColor(new BaseColor(255, 255, 255));
	    				contactInfo.addCell(firstname);

	    				PdfPCell firstnameValue = new PdfPCell(new Paragraph(
	    						tradeIn.getFirstName(), font2));
	    				firstnameValue.setBorderColor(BaseColor.WHITE);
	    				firstnameValue.setBorderWidth(1f);
	    				contactInfo.addCell(firstnameValue);

	    				PdfPCell lastname = new PdfPCell(
	    						new Phrase("Last Name:", font1));
	    				lastname.setBorderColor(BaseColor.WHITE);
	    				contactInfo.addCell(lastname);

	    				PdfPCell lastnameValue = new PdfPCell(new Paragraph("", font2));
	    				lastnameValue.setBorderColor(BaseColor.WHITE);
	    				lastnameValue.setBorderWidth(1f);
	    				// lastnameValue.setHorizontalAlignment(Element.ALIGN_LEFT);
	    				contactInfo.addCell(lastnameValue);

	    				PdfPCell workPhone = new PdfPCell(new Phrase("Work Phone:",
	    						font1));
	    				workPhone.setBorderColor(BaseColor.WHITE);
	    				contactInfo.addCell(workPhone);

	    				PdfPCell workPhoneValue = new PdfPCell(new Paragraph("", font2));
	    				workPhoneValue.setBorderColor(BaseColor.WHITE);
	    				workPhoneValue.setBorderWidth(1f);
	    				contactInfo.addCell(workPhoneValue);

	    				PdfPCell phone = new PdfPCell(new Phrase("Phone:", font1));
	    				phone.setBorderColor(BaseColor.WHITE);
	    				contactInfo.addCell(phone);

	    				PdfPCell phoneValue = new PdfPCell(new Paragraph(
	    						tradeIn.getPhone(), font2));
	    				phoneValue.setBorderColor(BaseColor.WHITE);
	    				phoneValue.setBorderWidth(1f);
	    				contactInfo.addCell(phoneValue);

	    				PdfPCell email = new PdfPCell(new Phrase("Email", font1));
	    				email.setBorderColor(BaseColor.WHITE);
	    				contactInfo.addCell(email);

	    				PdfPCell emailValue = new PdfPCell(new Paragraph(
	    						tradeIn.getEmail(), font2));
	    				emailValue.setBorderColor(BaseColor.WHITE);
	    				emailValue.setBorderWidth(1f);
	    				contactInfo.addCell(emailValue);

	    				PdfPCell options = new PdfPCell(new Phrase("Options:", font1));
	    				options.setBorderColor(BaseColor.WHITE);
	    				contactInfo.addCell(options);

	    				PdfPCell optionsValue = new PdfPCell(new Paragraph(
	    						leadVM.optionValue, font2));
	    				optionsValue.setBorderColor(BaseColor.WHITE);
	    				optionsValue.setBorderWidth(1f);
	    				contactInfo.addCell(optionsValue);

	    				// --------------Vehicle Information

	    				PdfPTable vehicleInformationTitle = new PdfPTable(1);
	    				vehicleInformationTitle.setWidthPercentage(100);
	    				float[] vehicleInformationTitleWidth = { 2f };
	    				vehicleInformationTitle.setWidths(vehicleInformationTitleWidth);

	    				PdfPCell vehicleInformationTitleValue = new PdfPCell(
	    						new Phrase("Vehicle Information"));
	    				vehicleInformationTitleValue.setBorderColor(BaseColor.WHITE);
	    				vehicleInformationTitleValue.setBackgroundColor(new BaseColor(
	    						255, 255, 255));
	    				vehicleInformationTitle.addCell(vehicleInformationTitleValue);

	    				// ------------vehicle info data

	    				PdfPTable vehicleInformation = new PdfPTable(4);
	    				vehicleInformation.setWidthPercentage(100);
	    				float[] vehicleInformationWidth = { 2f, 2f, 2f, 2f };
	    				vehicleInformation.setWidths(vehicleInformationWidth);

	    				PdfPCell year = new PdfPCell(new Phrase("Year:", font1));
	    				year.setBorderColor(BaseColor.WHITE);
	    				year.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleInformation.addCell(year);

	    				PdfPCell yearValue = new PdfPCell(new Paragraph(
	    						tradeIn.getYear(), font2));
	    				yearValue.setBorderColor(BaseColor.WHITE);
	    				yearValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(yearValue);

	    				PdfPCell make = new PdfPCell(new Phrase("Make:", font1));
	    				make.setBorderColor(BaseColor.WHITE);
	    				vehicleInformation.addCell(make);

	    				PdfPCell makeValue = new PdfPCell(new Paragraph(
	    						tradeIn.getMake(), font2));
	    				makeValue.setBorderColor(BaseColor.WHITE);
	    				makeValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(makeValue);

	    				PdfPCell Model = new PdfPCell(new Phrase("Model:", font1));
	    				Model.setBorderColor(BaseColor.WHITE);
	    				vehicleInformation.addCell(Model);

	    				PdfPCell modelValue = new PdfPCell(new Paragraph(
	    						tradeIn.getModel(), font2));
	    				modelValue.setBorderColor(BaseColor.WHITE);
	    				modelValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(modelValue);

	    				PdfPCell exteriorColour = new PdfPCell(new Phrase(
	    						"exteriorColour:", font1));
	    				exteriorColour.setBorderColor(BaseColor.WHITE);
	    				vehicleInformation.addCell(exteriorColour);

	    				PdfPCell exteriorColourValue = new PdfPCell(new Paragraph(
	    						tradeIn.getExteriorColour(), font2));
	    				exteriorColourValue.setBorderColor(BaseColor.WHITE);
	    				exteriorColourValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(exteriorColourValue);

	    				PdfPCell vin = new PdfPCell(new Phrase("VIN:", font1));
	    				vin.setBorderColor(BaseColor.WHITE);
	    				vehicleInformation.addCell(vin);

	    				PdfPCell vinValue = new PdfPCell(new Paragraph(
	    						tradeIn.getVin(), font2));
	    				vinValue.setBorderColor(BaseColor.WHITE);
	    				vinValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(vinValue);

	    				PdfPCell kilometres = new PdfPCell(new Phrase("Kilometres:",
	    						font1));
	    				kilometres.setBorderColor(BaseColor.WHITE);
	    				vehicleInformation.addCell(kilometres);

	    				PdfPCell kilometresValue = new PdfPCell(new Paragraph(
	    						tradeIn.getKilometres(), font2));
	    				kilometresValue.setBorderColor(BaseColor.WHITE);
	    				kilometresValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(kilometresValue);

	    				PdfPCell engine = new PdfPCell(new Phrase("Engine:", font1));
	    				engine.setBorderColor(BaseColor.WHITE);
	    				vehicleInformation.addCell(engine);

	    				PdfPCell engineValue = new PdfPCell(new Paragraph(
	    						tradeIn.getEngine(), font2));
	    				engineValue.setBorderColor(BaseColor.WHITE);
	    				engineValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(engineValue);

	    				PdfPCell doors = new PdfPCell(new Phrase("Doors:", font1));
	    				doors.setBorderColor(BaseColor.WHITE);
	    				vehicleInformation.addCell(doors);

	    				PdfPCell doorsValue = new PdfPCell(new Paragraph(
	    						tradeIn.getDoors(), font2));
	    				doorsValue.setBorderColor(BaseColor.WHITE);
	    				doorsValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(doorsValue);

	    				PdfPCell transmission = new PdfPCell(new Phrase(
	    						"Transmission:", font1));
	    				transmission.setBorderColor(BaseColor.WHITE);
	    				vehicleInformation.addCell(transmission);

	    				PdfPCell transmissionValue = new PdfPCell(new Paragraph(
	    						tradeIn.getTransmission(), font2));
	    				transmissionValue.setBorderColor(BaseColor.WHITE);
	    				transmissionValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(transmissionValue);

	    				PdfPCell drivetrain = new PdfPCell(new Phrase("Drivetrain:",
	    						font1));
	    				drivetrain.setBorderColor(BaseColor.WHITE);
	    				vehicleInformation.addCell(drivetrain);

	    				PdfPCell drivetrainValue = new PdfPCell(new Paragraph(
	    						tradeIn.getDrivetrain(), font2));
	    				drivetrainValue.setBorderColor(BaseColor.WHITE);
	    				drivetrainValue.setBorderWidth(1f);
	    				vehicleInformation.addCell(drivetrainValue);

	    				// ----------------Vehicle Rating title----

	    				PdfPTable vehicleRatingTitle = new PdfPTable(1);
	    				vehicleRatingTitle.setWidthPercentage(100);
	    				float[] vehicleRatingTitleWidth = { 2f };
	    				vehicleRatingTitle.setWidths(vehicleRatingTitleWidth);

	    				PdfPCell vehicleRatingTitleValue = new PdfPCell(new Phrase(
	    						"Vehicle Rating"));
	    				vehicleRatingTitleValue.setBorderColor(BaseColor.WHITE);
	    				vehicleRatingTitleValue.setBackgroundColor(new BaseColor(255,
	    						255, 255));
	    				vehicleRatingTitle.addCell(vehicleRatingTitleValue);

	    				// ------------Vehicle Rating data

	    				PdfPTable vehicleRatingData = new PdfPTable(4);
	    				vehicleRatingData.setWidthPercentage(100);
	    				float[] vehicleRatingDataWidth = { 2f, 2f, 2f, 2f };
	    				vehicleRatingData.setWidths(vehicleRatingDataWidth);

	    				PdfPCell body = new PdfPCell(new Phrase("Body :", font1));
	    				body.setBorderColor(BaseColor.WHITE);
	    				body.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleRatingData.addCell(body);

	    				PdfPCell bodyValue = new PdfPCell(new Paragraph(
	    						tradeIn.getBodyRating(), font2));
	    				bodyValue.setBorderColor(BaseColor.WHITE);
	    				bodyValue.setBorderWidth(1f);
	    				vehicleRatingData.addCell(bodyValue);

	    				PdfPCell tires = new PdfPCell(new Phrase("Tires :", font1));
	    				tires.setBorderColor(BaseColor.WHITE);
	    				tires.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleRatingData.addCell(tires);

	    				PdfPCell tiresValue = new PdfPCell(new Paragraph(
	    						tradeIn.getTireRating(), font2));
	    				tiresValue.setBorderColor(BaseColor.WHITE);
	    				tiresValue.setBorderWidth(1f);
	    				vehicleRatingData.addCell(tiresValue);

	    				PdfPCell engineRate = new PdfPCell(
	    						new Phrase("Engine :", font1));
	    				engineRate.setBorderColor(BaseColor.WHITE);
	    				engineRate.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleRatingData.addCell(engineRate);

	    				PdfPCell engineRateValue = new PdfPCell(new Paragraph(
	    						tradeIn.getEngineRating(), font2));
	    				engineRateValue.setBorderColor(BaseColor.WHITE);
	    				engineRateValue.setBorderWidth(1f);
	    				vehicleRatingData.addCell(engineRateValue);

	    				PdfPCell transmissionRate = new PdfPCell(new Phrase(
	    						"Transmission :", font1));
	    				transmissionRate.setBorderColor(BaseColor.WHITE);
	    				transmissionRate
	    						.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleRatingData.addCell(transmissionRate);

	    				PdfPCell transmissionRateValue = new PdfPCell(new Paragraph(
	    						tradeIn.getTransmissionRating(), font2));
	    				transmissionRateValue.setBorderColor(BaseColor.WHITE);
	    				transmissionRateValue.setBorderWidth(1f);
	    				vehicleRatingData.addCell(transmissionRateValue);

	    				PdfPCell glass = new PdfPCell(new Phrase("Glass :", font1));
	    				glass.setBorderColor(BaseColor.WHITE);
	    				glass.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleRatingData.addCell(glass);

	    				PdfPCell glassValue = new PdfPCell(new Paragraph(
	    						tradeIn.getGlassRating(), font2));
	    				glassValue.setBorderColor(BaseColor.WHITE);
	    				glassValue.setBorderWidth(1f);
	    				vehicleRatingData.addCell(glassValue);

	    				PdfPCell interior = new PdfPCell(
	    						new Phrase("Interior :", font1));
	    				interior.setBorderColor(BaseColor.WHITE);
	    				interior.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleRatingData.addCell(interior);

	    				PdfPCell interiorValue = new PdfPCell(new Paragraph(
	    						tradeIn.getInteriorRating(), font2));
	    				interiorValue.setBorderColor(BaseColor.WHITE);
	    				interiorValue.setBorderWidth(1f);
	    				vehicleRatingData.addCell(interiorValue);

	    				PdfPCell exhaust = new PdfPCell(new Phrase("Exhaust :", font1));
	    				exhaust.setBorderColor(BaseColor.WHITE);
	    				exhaust.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleRatingData.addCell(exhaust);

	    				PdfPCell exhaustValue = new PdfPCell(new Paragraph(
	    						tradeIn.getExhaustRating(), font2));
	    				exhaustValue.setBorderColor(BaseColor.WHITE);
	    				exhaustValue.setBorderWidth(1f);
	    				vehicleRatingData.addCell(exhaustValue);

	    				// ----------------Vehicle History title----

	    				PdfPTable vehiclehistoryTitle = new PdfPTable(1);
	    				vehiclehistoryTitle.setWidthPercentage(100);
	    				float[] vehiclehistoryTitleWidth = { 2f };
	    				vehiclehistoryTitle.setWidths(vehiclehistoryTitleWidth);

	    				PdfPCell vehiclehistoryValue = new PdfPCell(new Phrase(
	    						"Vehicle History"));
	    				vehiclehistoryValue.setBorderColor(BaseColor.WHITE);
	    				vehiclehistoryValue.setBackgroundColor(new BaseColor(255, 255,
	    						255));
	    				vehiclehistoryTitle.addCell(vehiclehistoryValue);

	    				// ------------Vehicle History data

	    				PdfPTable vehicleHistoryData = new PdfPTable(4);
	    				vehicleHistoryData.setWidthPercentage(100);
	    				float[] vehicleHistoryDataWidth = { 2f, 2f, 2f, 2f };
	    				vehicleHistoryData.setWidths(vehicleHistoryDataWidth);

	    				PdfPCell rentalReturn = new PdfPCell(new Phrase(
	    						"Was it ever a lease or rental return?  :", font1));
	    				rentalReturn.setBorderColor(BaseColor.WHITE);
	    				rentalReturn.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleHistoryData.addCell(rentalReturn);

	    				PdfPCell rentalReturnValue = new PdfPCell(new Paragraph(
	    						tradeIn.getLeaseOrRental(), font2));
	    				rentalReturnValue.setBorderColor(BaseColor.WHITE);
	    				rentalReturnValue.setBorderWidth(1f);
	    				vehicleHistoryData.addCell(rentalReturnValue);

	    				PdfPCell operationalAndaccu = new PdfPCell(new Phrase(
	    						"Is the odometer operational and accurate?  :", font1));
	    				operationalAndaccu.setBorderColor(BaseColor.WHITE);
	    				operationalAndaccu.setBackgroundColor(new BaseColor(255, 255,
	    						255));
	    				vehicleHistoryData.addCell(operationalAndaccu);

	    				PdfPCell operationalAndaccuValue = new PdfPCell(new Paragraph(
	    						tradeIn.getOperationalAndAccurate(), font2));
	    				operationalAndaccuValue.setBorderColor(BaseColor.WHITE);
	    				operationalAndaccuValue.setBorderWidth(1f);
	    				vehicleHistoryData.addCell(operationalAndaccuValue);

	    				PdfPCell serviceRecodes = new PdfPCell(new Phrase(
	    						"Detailed service records available?   :", font1));
	    				serviceRecodes.setBorderColor(BaseColor.WHITE);
	    				serviceRecodes.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleHistoryData.addCell(serviceRecodes);

	    				PdfPCell serviceRecodesValue = new PdfPCell(new Paragraph(
	    						tradeIn.getServiceRecord(), font2));
	    				serviceRecodesValue.setBorderColor(BaseColor.WHITE);
	    				serviceRecodesValue.setBorderWidth(1f);
	    				vehicleHistoryData.addCell(serviceRecodesValue);

	    				// ----------------Title History title----

	    				PdfPTable historyTitle = new PdfPTable(1);
	    				historyTitle.setWidthPercentage(100);
	    				float[] historyTitleWidth = { 2f };
	    				historyTitle.setWidths(historyTitleWidth);

	    				PdfPCell historyTitleValue = new PdfPCell(new Phrase(
	    						"Title History"));
	    				historyTitleValue.setBorderColor(BaseColor.WHITE);
	    				historyTitleValue.setBackgroundColor(new BaseColor(255, 255,
	    						255));
	    				historyTitle.addCell(historyTitleValue);

	    				// ------------Title History data

	    				PdfPTable historyTitleData = new PdfPTable(2);
	    				historyTitleData.setWidthPercentage(100);
	    				float[] historyTitleDataWidth = { 2f, 2f };
	    				historyTitleData.setWidths(historyTitleDataWidth);

	    				PdfPCell lineholder = new PdfPCell(new Phrase(
	    						"Is there a lineholder? :", font1));
	    				lineholder.setBorderColor(BaseColor.WHITE);
	    				lineholder.setBackgroundColor(new BaseColor(255, 255, 255));
	    				historyTitleData.addCell(lineholder);

	    				PdfPCell lineholderValue = new PdfPCell(new Paragraph(
	    						tradeIn.getLienholder(), font2));
	    				lineholderValue.setBorderColor(BaseColor.WHITE);
	    				lineholderValue.setBorderWidth(1f);
	    				historyTitleData.addCell(lineholderValue);

	    				PdfPCell titleholder = new PdfPCell(new Phrase(
	    						"Who holds this title?  :", font1));
	    				titleholder.setBorderColor(BaseColor.WHITE);
	    				titleholder.setBackgroundColor(new BaseColor(255, 255, 255));
	    				historyTitleData.addCell(titleholder);

	    				PdfPCell titleholderValue = new PdfPCell(new Paragraph("",
	    						font2));
	    				titleholderValue.setBorderColor(BaseColor.WHITE);
	    				titleholderValue.setBorderWidth(1f);
	    				historyTitleData.addCell(titleholderValue);

	    				// ----------------Vehicle Assessment title----

	    				PdfPTable vehicleAssessmentTitle = new PdfPTable(1);
	    				vehicleAssessmentTitle.setWidthPercentage(100);
	    				float[] vehicleAssessmentTitleWidth = { 2f };
	    				vehicleAssessmentTitle.setWidths(vehiclehistoryTitleWidth);

	    				PdfPCell vehiclTitle = new PdfPCell(new Phrase(
	    						"Vehicle Assessment"));
	    				vehiclTitle.setBorderColor(BaseColor.WHITE);
	    				vehiclTitle.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleAssessmentTitle.addCell(vehiclTitle);

	    				// ------------Vehicle Assessment data

	    				PdfPTable vehicleAssessmentData = new PdfPTable(1);
	    				vehicleAssessmentData.setWidthPercentage(100);
	    				float[] vehicleAssessmentDataWidth = { 2f };
	    				vehicleAssessmentData.setWidths(vehicleAssessmentDataWidth);

	    				PdfPCell equipment = new PdfPCell(new Phrase(
	    						"Does all equipment and accessories work correctly? :",
	    						font1));
	    				equipment.setBorderColor(BaseColor.WHITE);
	    				equipment.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleAssessmentData.addCell(rentalReturn);

	    				PdfPCell equipmentValue = new PdfPCell(new Paragraph(
	    						tradeIn.getEquipment(), font2));
	    				equipmentValue.setBorderColor(BaseColor.WHITE);
	    				equipmentValue.setBorderWidth(1f);
	    				vehicleAssessmentData.addCell(equipmentValue);

	    				PdfPCell buyVehicle = new PdfPCell(new Phrase(
	    						"Did you buy the vehicle new? :", font1));
	    				buyVehicle.setBorderColor(BaseColor.WHITE);
	    				buyVehicle.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleAssessmentData.addCell(buyVehicle);

	    				PdfPCell buyVehicleValue = new PdfPCell(new Paragraph(
	    						tradeIn.getVehiclenew(), font2));
	    				buyVehicleValue.setBorderColor(BaseColor.WHITE);
	    				buyVehicleValue.setBorderWidth(1f);
	    				vehicleAssessmentData.addCell(buyVehicleValue);

	    				PdfPCell accidents = new PdfPCell(
	    						new Phrase(
	    								"Has the vehicle ever been in any accidents? Cost of repairs? :",
	    								font1));
	    				accidents.setBorderColor(BaseColor.WHITE);
	    				accidents.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleAssessmentData.addCell(accidents);

	    				PdfPCell accidentsValue = new PdfPCell(new Paragraph(
	    						tradeIn.getAccidents(), font2));
	    				accidentsValue.setBorderColor(BaseColor.WHITE);
	    				accidentsValue.setBorderWidth(1f);
	    				vehicleAssessmentData.addCell(accidentsValue);

	    				PdfPCell damage = new PdfPCell(new Phrase(
	    						"Is there existing damage on the vehicle? Where? :",
	    						font1));
	    				damage.setBorderColor(BaseColor.WHITE);
	    				damage.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleAssessmentData.addCell(damage);

	    				PdfPCell damageValue = new PdfPCell(new Paragraph(
	    						tradeIn.getDamage(), font2));
	    				damageValue.setBorderColor(BaseColor.WHITE);
	    				damageValue.setBorderWidth(1f);
	    				vehicleAssessmentData.addCell(damageValue);

	    				PdfPCell paintWork = new PdfPCell(new Phrase(
	    						"Has the vehicle ever had paint work performed? :",
	    						font1));
	    				paintWork.setBorderColor(BaseColor.WHITE);
	    				paintWork.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleAssessmentData.addCell(paintWork);

	    				PdfPCell paintWorkValue = new PdfPCell(new Paragraph(
	    						tradeIn.getPaint(), font2));
	    				paintWorkValue.setBorderColor(BaseColor.WHITE);
	    				paintWorkValue.setBorderWidth(1f);
	    				vehicleAssessmentData.addCell(paintWorkValue);

	    				PdfPCell salvage = new PdfPCell(
	    						new Phrase(
	    								"Is the title designated 'Salvage' or 'Reconstructed'? Any other? :",
	    								font1));
	    				salvage.setBorderColor(BaseColor.WHITE);
	    				salvage.setBackgroundColor(new BaseColor(255, 255, 255));
	    				vehicleAssessmentData.addCell(salvage);

	    				PdfPCell salvageValue = new PdfPCell(new Paragraph(
	    						tradeIn.getSalvage(), font2));
	    				salvageValue.setBorderColor(BaseColor.WHITE);
	    				salvageValue.setBorderWidth(1f);
	    				vehicleAssessmentData.addCell(salvageValue);

	    				// ----------sub main Table----------

	    				PdfPTable AddAllTableInMainTable = new PdfPTable(1);
	    				AddAllTableInMainTable.setWidthPercentage(100);
	    				float[] AddAllTableInMainTableWidth = { 2f };
	    				AddAllTableInMainTable.setWidths(AddAllTableInMainTableWidth);

	    				PdfPCell hotelVoucherTitlemain1 = new PdfPCell(Titlemain);
	    				hotelVoucherTitlemain1.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(hotelVoucherTitlemain1);

	    				PdfPCell contactInfoData = new PdfPCell(contactInfo);
	    				contactInfoData.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(contactInfoData);

	    				PdfPCell vehicaleInfoTitle = new PdfPCell(
	    						vehicleInformationTitle);
	    				vehicaleInfoTitle.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(vehicaleInfoTitle);

	    				PdfPCell vehicaleInfoData = new PdfPCell(vehicleInformation);
	    				vehicaleInfoData.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(vehicaleInfoData);

	    				PdfPCell vehicleRatingTitles = new PdfPCell(vehicleRatingTitle);
	    				vehicleRatingTitles.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(vehicleRatingTitles);

	    				PdfPCell vehicleRatinginfo = new PdfPCell(vehicleRatingData);
	    				vehicleRatinginfo.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(vehicleRatinginfo);

	    				PdfPCell vehicleHisTitle = new PdfPCell(vehiclehistoryTitle);
	    				vehicleHisTitle.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(vehicleHisTitle);

	    				PdfPCell vehicleHistoryInfo = new PdfPCell(vehicleHistoryData);
	    				vehicleHistoryInfo.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(vehicleHistoryInfo);

	    				PdfPCell historyTitles = new PdfPCell(historyTitle);
	    				historyTitles.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(historyTitles);

	    				PdfPCell historyTitleinfo = new PdfPCell(historyTitleData);
	    				historyTitleinfo.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(historyTitleinfo);

	    				PdfPCell vehicleTitleAssessment = new PdfPCell(
	    						vehicleAssessmentTitle);
	    				vehicleTitleAssessment.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(vehicleTitleAssessment);

	    				PdfPCell vehicleAssessmentDataTitle = new PdfPCell(
	    						vehicleAssessmentData);
	    				vehicleAssessmentDataTitle.setBorder(Rectangle.NO_BORDER);
	    				AddAllTableInMainTable.addCell(vehicleAssessmentDataTitle);

	    				// ----------main Table----------

	    				PdfPTable AddMainTable = new PdfPTable(1);
	    				AddMainTable.setWidthPercentage(100);
	    				float[] AddMainTableWidth = { 2f };
	    				AddMainTable.setWidths(AddMainTableWidth);

	    				PdfPCell AddAllTableInMainTable1 = new PdfPCell(
	    						AddAllTableInMainTable);
	    				AddAllTableInMainTable1.setPadding(10);
	    				AddAllTableInMainTable1.setBorderWidth(1f);
	    				AddMainTable.addCell(AddAllTableInMainTable1);

	    				document.add(AddMainTable);

	    				document.close();

	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}

	    			final String username = emailUsername;
	    			final String password = emailPassword;
	    			Properties props = new Properties();
	    			props.put("mail.smtp.auth", "true");
	    			props.put("mail.smtp.host", "smtp.gmail.com");
	    			props.put("mail.smtp.port", "587");
	    			props.put("mail.smtp.starttls.enable", "true");
	    			Session session = Session.getInstance(props,
	    					new javax.mail.Authenticator() {
	    						protected PasswordAuthentication getPasswordAuthentication() {
	    							return new PasswordAuthentication(username,
	    									password);
	    						}
	    					});
	    			try {
	    				List<AuthUser> users = AuthUser.getAllUsers();

	    				InternetAddress[] usersArray = new InternetAddress[users.size() + 1];
	    				int index = 0;
	    				usersArray[index] = new InternetAddress(user.getEmail());
	    				
	    				Message message = new MimeMessage(session);
	    				message.setFrom(new InternetAddress(emailUsername));
	    				message.setRecipients(Message.RecipientType.TO, usersArray);
	    				message.setSubject("Trade-In Appraisal");
	    				Multipart multipart = new MimeMultipart();
	    				BodyPart messageBodyPart = new MimeBodyPart();
	    				messageBodyPart = new MimeBodyPart();

	    				VelocityEngine ve = new VelocityEngine();
	    				ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
	    						"org.apache.velocity.runtime.log.Log4JLogChute");
	    				ve.setProperty("runtime.log.logsystem.log4j.logger",
	    						"clientService");
	    				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
	    				ve.setProperty("classpath.resource.loader.class",
	    						ClasspathResourceLoader.class.getName());
	    				ve.init();

	    				String urlfind = "http://www.glider-autos.com/dealer/index.html#/tradeIn";

	    				Template t = ve.getTemplate("/public/emailTemplate/trade_in_app.vm");
	    				VelocityContext context = new VelocityContext();

	    				// ---------Trad in info---------------

	    				// contact info
	    				context.put("first_name", tradeIn.getFirstName());
	    				context.put("last_name", "");
	    				context.put("work_phone", "");
	    				context.put("email", tradeIn.getEmail());

	    				context.put("year", tradeIn.getYear());
	    				context.put("make", tradeIn.getMake());
	    				context.put("model", tradeIn.getModel());
	    				context.put("price", "$" + vehicle.getPrice());
	    				context.put("vin", vehicle.getVin());
	    				context.put("stock", vehicle.getStock());
	    				context.put("mileage", vehicle.getMileage());
	    				context.put("pdffilePath", findpath);

	    				if (tradeIn.getPreferredContact() != null) {
	    					context.put("preferred", tradeIn.getPreferredContact());
	    				} else {
	    					context.put("preferred", "");
	    				}

	    				context.put("optionValue", leadVM.optionValue);

	    				// vehicale info

	    				if (tradeIn.getYear() != null) {
	    					context.put("yearValue", tradeIn.getYear());
	    				} else {
	    					context.put("yearValue", "");
	    				}
	    				if (tradeIn.getMake() != null) {
	    					context.put("makeValue", tradeIn.getMake());
	    				} else {
	    					context.put("makeValue", "");
	    				}
	    				if (tradeIn.getModel() != null) {
	    					context.put("modelValue", tradeIn.getModel());
	    				} else {
	    					context.put("modelValue", "");
	    				}
	    				if (tradeIn.getExteriorColour() != null) {
	    					context.put("exterior_colour", tradeIn.getExteriorColour());
	    				} else {
	    					context.put("exterior_colour", "");
	    				}

	    				if (tradeIn.getKilometres() != null) {
	    					context.put("kilometres", tradeIn.getKilometres());
	    				} else {
	    					context.put("kilometres", "");
	    				}

	    				if (tradeIn.getEngine() != null) {
	    					context.put("engine", tradeIn.getEngine());
	    				} else {
	    					context.put("engine", "");
	    				}

	    				if (tradeIn.getModel() != null) {
	    					context.put("doors", tradeIn.getModel());
	    				} else {
	    					context.put("doors", "");
	    				}

	    				if (tradeIn.getTransmission() != null) {
	    					context.put("transmission", tradeIn.getTransmission());
	    				} else {
	    					context.put("transmission", "");
	    				}

	    				if (tradeIn.getDrivetrain() != null) {
	    					context.put("drivetrain", tradeIn.getDrivetrain());
	    				} else {
	    					context.put("drivetrain", "");
	    				}

	    				// vehicale rating

	    				if (tradeIn.getBodyRating() != null) {
	    					context.put("body_rating", tradeIn.getBodyRating());
	    				} else {
	    					context.put("body_rating", "");
	    				}

	    				if (tradeIn.getTireRating() != null) {
	    					context.put("tire_rating", tradeIn.getTireRating());
	    				} else {
	    					context.put("tire_rating", "");
	    				}

	    				if (tradeIn.getEngineRating() != null) {
	    					context.put("engine_rating", tradeIn.getEngineRating());
	    				} else {
	    					context.put("engine_rating", "");
	    				}

	    				if (tradeIn.getTransmissionRating() != null) {
	    					context.put("transmission_rating",
	    							tradeIn.getTransmissionRating());
	    				} else {
	    					context.put("transmission_rating", "");
	    				}

	    				if (tradeIn.getGlassRating() != null) {
	    					context.put("glass_rating", tradeIn.getGlassRating());
	    				} else {
	    					context.put("glass_rating", "");
	    				}

	    				if (tradeIn.getInteriorRating() != null) {
	    					context.put("interior_rating", tradeIn.getInteriorRating());
	    				} else {
	    					context.put("interior_rating", "");
	    				}

	    				if (tradeIn.getExhaustRating() != null) {
	    					context.put("exhaust_rating", tradeIn.getExhaustRating());
	    				} else {
	    					context.put("exhaust_rating", "");
	    				}

	    				// vehicale History

	    				if (tradeIn.getLeaseOrRental() != null) {
	    					context.put("lease_or_rental", tradeIn.getLeaseOrRental());
	    				} else {
	    					context.put("lease_or_rental", "");
	    				}

	    				if (tradeIn.getOperationalAndAccurate() != null) {
	    					context.put("operational_and_accurate",
	    							tradeIn.getOperationalAndAccurate());
	    				} else {
	    					context.put("operational_and_accurate", "");
	    				}

	    				if (tradeIn.getServiceRecord() != null) {
	    					context.put("service_record", tradeIn.getServiceRecord());
	    				} else {
	    					context.put("service_record", "");
	    				}

	    				// title History

	    				if (tradeIn.getLienholder() != null) {
	    					context.put("lienholder", tradeIn.getLienholder());
	    				} else {
	    					context.put("lienholder", "");
	    				}

	    				if (tradeIn.getHoldsThisTitle() != null) {
	    					context.put("holds_this_title", tradeIn.getHoldsThisTitle());
	    				} else {
	    					context.put("holds_this_title", "");
	    				}

	    				// Vehicle Assessment

	    				if (tradeIn.getEquipment() != null) {
	    					context.put("equipment", tradeIn.getEquipment());
	    				} else {
	    					context.put("equipment", "");
	    				}

	    				if (tradeIn.getVehiclenew() != null) {
	    					context.put("vehiclenew", tradeIn.getVehiclenew());
	    				} else {
	    					context.put("vehiclenew", "");
	    				}

	    				if (tradeIn.getAccidents() != null) {
	    					context.put("accidents", tradeIn.getAccidents());
	    				} else {
	    					context.put("accidents", "");
	    				}

	    				if (tradeIn.getDamage() != null) {
	    					context.put("damage", tradeIn.getDamage());
	    				} else {
	    					context.put("damage", "");
	    				}

	    				if (tradeIn.getPaint() != null) {
	    					context.put("paint", tradeIn.getPaint());
	    				} else {
	    					context.put("paint", "");
	    				}

	    				if (tradeIn.getSalvage() != null) {
	    					context.put("salvage", tradeIn.getSalvage());
	    				} else {
	    					context.put("salvage", "");
	    				}

	    				context.put("sitelogo", siteLogo.getLogoImageName());
	    				context.put("path", siteLogo.getLogoImagePath());
	    				context.put("heading1", heading1);
	    				context.put("heading2", heading2);
	    				context.put("urlLink", vehicleUrlPath);
	    				context.put("urlfind", urlfind);
	    				context.put("hostnameimg", imageUrlPath);

	    				StringWriter writer = new StringWriter();
	    				t.merge(context, writer);
	    				String content = writer.toString();
	    				// attachPart.attachFile(file);
	    				messageBodyPart.setContent(content, "text/html");
	    				multipart.addBodyPart(messageBodyPart);

	    				message.setContent(multipart);
	    				Transport.send(message);
	    				System.out.println("Sent test message successfully....");
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	        	}
	    		
	    	 }
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result savePosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<VehicleImage> images = mapper.readValue(nodes.toString(), new TypeReference<List<VehicleImage>>() {});
				
		    	for(VehicleImage image : images) {
		    		image.update();
		    	}
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	return ok();
    	}
    }
    
    
    public static Result saveSliderPosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<SliderImage> images = mapper.readValue(nodes.toString(), new TypeReference<List<SliderImage>>() {});
				
		    	for(SliderImage image : images) {
		    		image.update();
		    	}
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result saveFeaturedPosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<FeaturedImage> images = mapper.readValue(nodes.toString(), new TypeReference<List<FeaturedImage>>() {});
				
		    	for(FeaturedImage image : images) {
		    		image.update();
		    	}
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	return ok();
    	}	
    }
    
	public static Result getAllVehiclesByType(String type) {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    				
    			} catch (JSONException e) {
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
    
	public static Result getAllVehicles() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		int visitorCount = 0;
	    	/*List <Vehicle> vehicleObjList = Vehicle.getVehiclesByStatus("Newly Arrived");*/
    		
    		List <Vehicle> vehicleObjList = Vehicle.findByNewArrAndLocation(Long.valueOf(session("USER_LOCATION")));
    		
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
    				
    			} catch (JSONException e) {
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
    
	public static Result getVehicleHistory(String vin){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
	public static Result getPriceHistory(String vin){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
    		List<PriceChangeVM> priceList = new ArrayList<>();
    		List<PriceChange> list = PriceChange.findByVin(vin);
    		for (PriceChange obj : list) {
				PriceChangeVM vm =new PriceChangeVM();
				vm.dateTime = obj.dateTime.toString();
				vm.person = obj.person;
				vm.price = obj.price;
				priceList.add(vm);
			}
    		return ok(Json.toJson(list));
    	}
	}
	
	public static Result getAllSoldVehiclesByType(String type) {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	public static Result getAllSoldVehicles() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
		    	vehicle.imagePath = vehicleImg.thumbPath;
		    	vehicle.imgId = vehicleImg.id;
		    	vehicle.testDrive = df.format(vm.soldDate);
		    	vehicle.title = vm.title;
		    	soldVMs.add(vehicle);
	    	}
	     	
	     	return ok(Json.toJson(soldVMs));
    	}	
    }
    
    
    public static Result deleteVehicleById(Long id ){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	Vehicle vm = Vehicle.findById(id);
	    	AuthUser user = (AuthUser) getLocalUser();
	    	if(vm != null){
	    		List<VehicleImage> v = VehicleImage.getByVin(vm.vin);
	    		if(v.size() != 0){
	    			Ebean.delete(v);
	    		}
	    		vm.deleteManyToManyAssociations("site");
	    		vm.delete();
	    		File file = new File(rootDir+File.separator+vm.vin+"-"+user.id);
	    		file.delete();
	    	}
	    	return ok();
    	}	
    }
    
    
    public static Result updateVehicleStatus(Long id,String status){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	Vehicle vm = Vehicle.findById(id);
	    	Date currDate = new Date();
	    	if(vm != null){
	    		vm.setStatus(status);
	    		Date date = new Date();
	    		vm.setSoldDate(date);
	    		vm.update();
	    		
	    		if(status.equals("Sold")){
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
	        		}
	    		}
	    	}
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result getVehicleById(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	Vehicle vehicle = Vehicle.findById(id);
	    	PinVM pinVM = new PinVM();
			pinVM.success = true;
			pinVM.vin = vehicle.getVin();
			
			SpecificationVM specificationVM = new SpecificationVM();
			specificationVM.id = vehicle.getId();
			specificationVM.category = vehicle.getCategory();
			specificationVM.vin = vehicle.getVin();
			specificationVM.typeofVehicle=vehicle.getTypeofVehicle();
			specificationVM.year = vehicle.getYear();
			specificationVM.make = vehicle.getMake();
			specificationVM.model = vehicle.getModel();
			specificationVM.trim_level = vehicle.getTrim();
			specificationVM.label = vehicle.getLabel();
			specificationVM.stock = vehicle.getStock();
			specificationVM.city_mileage = vehicle.getCityMileage();
			specificationVM.highway_mileage = vehicle.getHighwayMileage();
			specificationVM.cost = vehicle.getCost();
			specificationVM.price = vehicle.getPrice();
			specificationVM.made_in = vehicle.getMadeIn();
			specificationVM.optional_seating = vehicle.getOptionalSeating();
			specificationVM.extColor = vehicle.getExteriorColor();
			specificationVM.colorDesc = vehicle.getColorDescription();
			specificationVM.doors = vehicle.getDoors();
			specificationVM.stereo = vehicle.getStereo();
			specificationVM.engine = vehicle.getEngine();
			specificationVM.style = vehicle.getBodyStyle();
			specificationVM.location = vehicle.getLocation();
			specificationVM.description = vehicle.getDescription();
			specificationVM.title = vehicle.getTitle();
			
			specificationVM.drivetrain = vehicle.getDrivetrain();
			specificationVM.fuelType = vehicle.getFuelType();
			specificationVM.fuelTank = vehicle.getFuelTank();
			specificationVM.headlights = vehicle.getHeadlights();
			specificationVM.mirrors = vehicle.getMirrors();
			specificationVM.groundClearance = vehicle.getGroundClearance();
			specificationVM.roof = vehicle.getRoof();
			specificationVM.height = vehicle.getHeight();
			specificationVM.length = vehicle.getLength();
			specificationVM.width = vehicle.getWidth();
			specificationVM.acceleration = vehicle.getAcceleration();
			specificationVM.standardSeating = vehicle.getStandardSeating();
			specificationVM.engineType = vehicle.getEngineType();
			specificationVM.cylinders = vehicle.getCylinders();
			specificationVM.displacement = vehicle.getDisplacement();
			specificationVM.camType = vehicle.getCamType();
			specificationVM.valves = vehicle.getValves();
			specificationVM.fuelQuality = vehicle.getFuelQuality();
			specificationVM.horsePower = vehicle.getHorsePower();
			specificationVM.transmission = vehicle.getTransmission();
			specificationVM.gears = vehicle.getGears();
			specificationVM.brakes = vehicle.getBrakes();
			specificationVM.frontBrakeDiameter = vehicle.getFrontBrakeDiameter();
			specificationVM.frontBrakeType = vehicle.getFrontBrakeType();
			specificationVM.rearBrakeDiameter = vehicle.getRearBrakeDiameter();
			specificationVM.rearBrakeType = vehicle.getRearBrakeType();
			specificationVM.activeHeadRestraints = vehicle.getActiveHeadRestrains();
			specificationVM.bodySideReinforcements = vehicle.getBodySideReinforcements();
			specificationVM.crumpleZones = vehicle.getCrumpleZones();
			specificationVM.impactAbsorbingBumpers = vehicle.getImpactAbsorbingBumpers();
			specificationVM.impactSensors = vehicle.getImpactSensor();
			specificationVM.parkingSensors = vehicle.getParkingSensors();
			specificationVM.seatbelts = vehicle.getSeatbelts();
			specificationVM.audiSideAssist = vehicle.getAudiSideAssist();
			specificationVM.intColor = vehicle.getInteriorColor();
			specificationVM.comfortFeatures = vehicle.getComfortFeatures();
			specificationVM.powerOutlets = vehicle.getPowerOutlet();
			specificationVM.powerSteering = vehicle.getPowerSteering();
			specificationVM.rearViewCamera = vehicle.getRearViewCamera();
			specificationVM.rearViewMonitor = vehicle.getRearViewMonitor();
			specificationVM.remoteTrunkRelease = vehicle.getRemoteTrunkRelease();
			specificationVM.steeringWheel = vehicle.getSteeringWheel();
			specificationVM.steeringWheelControls = vehicle.getSteeringWheelControls();
			
			specificationVM.standardSeating = vehicle.getStandardSeating();
			
			specificationVM.mileage = vehicle.getMileage();
			List<Long> siteIds = new ArrayList<>();
			for(Site site: vehicle.getSite()) {
				siteIds.add(site.id);
			}
			specificationVM.siteIds = siteIds;
			
			pinVM.specification = specificationVM;
			
	    	return ok(Json.toJson(pinVM));
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
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(emailUsername));
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
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(emailUsername));
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
			
				
		        Template t = ve.getTemplate("/public/emailTemplate/priceAlertTemplate.vm"); 
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
		        context.put("doors", vehicle.doors);
		        context.put("seats", vehicle.standardSeating);
		        context.put("driveTrain", vehicle.drivetrain);
		        context.put("engine", vehicle.engine);
		        context.put("transmission", vehicle.transmission);
		        context.put("brakes", vehicle.brakes);
		        context.put("horsePower", vehicle.horsePower);
		        context.put("email", profile.email);
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
    
    public static Result updateVehicle(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    
    public static Result updateVehicleById() throws SocketException, IOException{
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		int flag=0;
    		Date currDate = new Date();
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
	    	SpecificationVM vm = form.get();
	    	Vehicle vehicle = Vehicle.findById(vm.id);
	    	if(vehicle != null) {
	    		
	    	
	    		String databasevalue=vehicle.price.toString();
	    		String vmvalue= vm.price.toString();
	    		if(!vmvalue.equals(databasevalue)) {
	    			List<PriceAlert> alertList = PriceAlert.getEmailsByVin(vehicle.vin,Long.valueOf(session("USER_LOCATION")));
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
    				change.person = userObj.firstName +" "+userObj.lastName;
    				change.vin = vm.vin;
    				change.save();
    				flag=1;
    		//		sendPriceAlertMail(vehicle.vin);
	    		}
	    		vehicle.setTypeofVehicle(vm.typeofVehicle);
	    		System.out.println(vm.typeofVehicle);
	    		vehicle.setCategory(vm.category);
	    		vehicle.setTitle(vm.title);
		    	vehicle.setYear(vm.year);
		    	vehicle.setMake(vm.make);
		    	vehicle.setModel(vm.model);
		    	vehicle.setTrim(vm.trim_level);
		    	vehicle.setLabel(vm.label);
		    	vehicle.setStock(vm.stock);
		    	vehicle.setCityMileage(vm.city_mileage);
		    	vehicle.setHighwayMileage(vm.highway_mileage);
		    	vehicle.setCost(vm.cost);
		    	vehicle.setPrice(vm.price);
		    	vehicle.setMadeIn(vm.made_in);
		    	vehicle.setOptionalSeating(vm.optional_seating);
		    	vehicle.setExteriorColor(vm.extColor);
		    	vehicle.setColorDescription(vm.colorDesc);
		    	vehicle.setDoors(vm.doors);
		    	vehicle.setStereo(vm.stereo);
		    	vehicle.setEngine(vm.engine);
		    	vehicle.setBodyStyle(vm.style);
		    	vehicle.setLocation(vm.location);
		    	vehicle.setDescription(vm.description);
				
		    	vehicle.setDrivetrain(vm.drivetrain);
		    	vehicle.setFuelType(vm.fuelType);
		    	vehicle.setFuelTank(vm.fuelTank);
		    	vehicle.setHeadlights(vm.headlights);
		    	vehicle.setMirrors(vm.mirrors);
		    	vehicle.setGroundClearance(vm.groundClearance);
		    	vehicle.setRoof(vm.roof);
		    	vehicle.setHeight(vm.height);
		    	vehicle.setLength(vm.length);
		    	vehicle.setWidth(vm.width);
		    	vehicle.setAcceleration(vm.acceleration);
		    	vehicle.setStandardSeating(vm.standardSeating);
		    	vehicle.setEngineType(vm.engineType);
		    	vehicle.setCylinders(vm.cylinders);
		    	vehicle.setDisplacement(vm.displacement);
		    	vehicle.setCamType(vm.camType);
		    	vehicle.setValves(vm.valves);
		    	vehicle.setFuelQuality(vm.fuelQuality);
		    	vehicle.setHorsePower(vm.horsePower);
		    	vehicle.setTransmission(vm.transmission);
		    	vehicle.setGears(vm.gears);
		    	vehicle.setBrakes(vm.brakes);
		    	vehicle.setFrontBrakeDiameter(vm.frontBrakeDiameter);
		    	vehicle.setFrontBrakeType(vm.frontBrakeType);
		    	vehicle.setRearBrakeDiameter(vm.rearBrakeDiameter);
		    	vehicle.setRearBrakeType(vm.rearBrakeType);
		    	vehicle.setActiveHeadRestrains(vm.activeHeadRestraints);
		    	vehicle.setBodySideReinforcements(vm.bodySideReinforcements);
		    	vehicle.setCrumpleZones(vm.crumpleZones);
		    	vehicle.setImpactAbsorbingBumpers(vm.impactAbsorbingBumpers);
		    	vehicle.setImpactSensor(vm.impactSensors);
		    	vehicle.setParkingSensors(vm.parkingSensors);
		    	vehicle.setSeatbelts(vm.seatbelts);
		    	vehicle.setAudiSideAssist(vm.audiSideAssist);
		    	vehicle.setInteriorColor(vm.intColor);
		    	vehicle.setComfortFeatures(vm.comfortFeatures);
		    	vehicle.setPowerOutlet(vm.powerOutlets);
		    	vehicle.setPowerSteering(vm.powerSteering);
		    	vehicle.setRearViewCamera(vm.rearViewCamera);
		    	vehicle.setRearViewMonitor(vm.rearViewMonitor);
		    	vehicle.setRemoteTrunkRelease(vm.remoteTrunkRelease);
		    	vehicle.setSteeringWheel(vm.steeringWheel);
		    	vehicle.setSteeringWheelControls(vm.steeringWheelControls);
				
		    	vehicle.setStandardSeating(vm.standardSeating);
				
		    	vehicle.setMileage(vm.mileage);
		    	vehicle.setMadeIn(vm.made_in);
		    	vehicle.setOptionalSeating(vm.optional_seating);
		    	List<Site> siteList = new ArrayList<>();
		    	if(vm.siteIds != null) {
			    	for(Long obj: vm.siteIds) {
			    		Site siteObj = Site.findById(obj);
			    		siteList.add(siteObj);
			    	}
			    	vehicle.setSite(siteList);
		    	}
		    	vehicle.update();
		    	
		    	if(flag==1){
		    		sendPriceAlertMail(vehicle.vin);
		    	}
		//    	sendPriceAlertMail(vehicle.vin);
		    	
		    	Vehicle vehicleObj2 = Vehicle.findByVinAndStatus(vm.vin);
		    	List<Site> siteList2 = vehicleObj2.getSite();
		    	//MyProfile profile = MyProfile.findByLocation(Long.valueOf(session("USER_LOCATION")));  //findByUser(userObj);
		    	AuthUser aUser = AuthUser.getlocationAndManagerByType(Location.findById(Long.valueOf(session("USER_LOCATION"))), "Manager");
		    	MyProfile profile = MyProfile.findByUser(aUser);
		    	
		    	if(!siteList2.isEmpty()) {
			    	for(Site siteObj: siteList2) {
			    		if(siteObj.getName().equals("CarsGuru")) {
			    			FTPClient client = new FTPClient();
			    	        FileInputStream fis = null;
			    	        client.connect("ftp.cargurus.com");
			                client.login("glider", "GLF8yP");
			                String filename = vm.vin+"-CarsGuru"+".csv";
			                CSVWriter writer = new CSVWriter(new FileWriter(filename));
			            	List<Vehicle> vehicleList = Vehicle.getAllVehicles(userObj);
			            	
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
			            	
			            	for(Vehicle vehicleData: vehicleList) {
			            		String []row = new String[29];
			            		row[0] = vehicleData.vin;
			            		row[1] = vehicleData.make;
			            		row[2] = vehicleData.model;
			            		row[3] = vehicleData.year;
			            		row[4] = vehicleData.trim;
			            		row[5] = vehicleData.price.toString();
			            		row[6] = vehicleData.mileage;
			            		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicleData.vin);
			            		String str = "";
			            		for(VehicleImage img : vImageList) {
			            			str = str +imageUrlPath+img.path+",";
			            		}
			            		row[7] = str;
			            		row[8] = vehicleData.exteriorColor;
			            		row[9] = "";
			            		row[10] = vehicleData.stock;
			            		row[11] = vehicleData.transmission;
			            		
			            		String standardFeatures = "";
			            		if(vehicleData.drivetrain != null) {
			            			standardFeatures = standardFeatures + vehicleData.drivetrain+",";
			            		}
			            		if(vehicleData.fuelType != null) {
			            			standardFeatures = standardFeatures + vehicleData.fuelType+",";
			            		}
			            		if(vehicleData.fuelTank != null) {
			            			standardFeatures = standardFeatures + vehicleData.fuelTank+",";
			            		}
			            		if(vehicleData.headlights != null) {
			            			standardFeatures = standardFeatures + vehicleData.headlights+",";
			            		}
			            		if(vehicleData.mirrors != null) {
			            			standardFeatures = standardFeatures + vehicleData.mirrors+",";
			            		}
			            		if(vehicleData.roof != null) {
			            			standardFeatures = standardFeatures + vehicleData.roof+",";
			            		}
			            		if(vehicleData.acceleration != null) {
			            			standardFeatures = standardFeatures + vehicleData.acceleration+",";
			            		}
			            		if(vehicleData.standardSeating != null) {
			            			standardFeatures = standardFeatures + vehicleData.standardSeating+",";
			            		}
			            		if(vehicleData.engine != null) {
			            			standardFeatures = standardFeatures + vehicleData.engine+",";
			            		}
			            		if(vehicleData.camType != null) {
			            			standardFeatures = standardFeatures + vehicleData.camType+",";
			            		}
			            		if(vehicleData.valves != null) {
			            			standardFeatures = standardFeatures + vehicleData.valves+",";
			            		}
			            		if(vehicleData.cylinders != null) {
			            			standardFeatures = standardFeatures + vehicleData.cylinders+",";
			            		}
			            		if(vehicleData.fuelQuality != null) {
			            			standardFeatures = standardFeatures + vehicleData.fuelQuality+",";
			            		}
			            		if(vehicleData.horsePower != null) {
			            			standardFeatures = standardFeatures + vehicleData.horsePower+",";
			            		}
			            		if(vehicleData.transmission != null) {
			            			standardFeatures = standardFeatures + vehicleData.transmission+",";
			            		}
			            		if(vehicleData.gears != null) {
			            			standardFeatures = standardFeatures + vehicleData.gears+",";
			            		}
			            		if(vehicleData.brakes != null) {
			            			standardFeatures = standardFeatures + vehicleData.brakes+",";
			            		}
			            		if(vehicleData.frontBrakeDiameter != null) {
			            			standardFeatures = standardFeatures + vehicleData.frontBrakeDiameter+",";
			            		}
			            		if(vehicleData.frontBrakeType != null) {
			            			standardFeatures = standardFeatures + vehicleData.frontBrakeType+",";
			            		}
			            		if(vehicleData.rearBrakeDiameter != null) {
			            			standardFeatures = standardFeatures + vehicleData.rearBrakeDiameter+",";
			            		}
			            		if(vehicleData.rearBrakeType != null) {
			            			standardFeatures = standardFeatures + vehicleData.rearBrakeType;
			            		}
			            		
			            		row[12] = standardFeatures;
			            		
			            		if(profile != null) {
			            			row[13] = profile.dealer_id;
			            			row[14] = profile.myname;
			            			row[15] = profile.address;
			            			row[16] = profile.city;
				            		row[17] = profile.state;
				            		row[18] = profile.zip;
				            		row[19] = profile.email;
				            		row[25] = profile.latlong;
				            		row[27] = profile.phone;
				            		row[28] = profile.web;
			            		} else {
			            			row[13] = "";
			            			row[14] = "";
			            			row[15] = "";
			            			row[16] = "";
				            		row[17] = "";
				            		row[18] = "";
				            		row[19] = "";
				            		row[25] = "";
				            		row[27] = "";
				            		row[28] = "";
			            		}
			            		
			            		row[20] = vehicleData.getPrice().toString();
			            		row[21] = vehicleData.interiorColor;
			            		row[22] = "";
			            		row[23] = "N";
			            		row[24] = vehicleData.engine;
			            		
			            		row[26] = "1000";
			            		
			            		
			            		writer.writeNext(row);
			            	}
			            	
			            	 writer.close();
			                fis = new FileInputStream(filename);
			                client.storeFile(filename, fis);
			                client.logout();
			                if (fis != null) {
			                    fis.close();
			                }
			    		}   
			    		
			    		
			    		if(siteObj.getName().equals("Carfax")) {
			    			FTPClient client = new FTPClient();
			    	        FileInputStream fis = null;
			    	        client.connect("ftp.vast.com");
			                client.login("gliderllc.com", "73f1vvRw4a");
			                String filename = vm.vin+"-Carfax"+".csv";
			                CSVWriter writer = new CSVWriter(new FileWriter(filename));
			    	    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(userObj);
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
			    			
			    	    	for(Vehicle vehicleCarfax: vehicleList) {
			    	    		String []row = new String[48];
			    	    		row[0] = "12345678";
			    	    		row[1] = vehicleCarfax.vin;
			    	    		row[2] = vehicleCarfax.stock;
			    	    		row[3] = vehicleCarfax.year+" "+vehicleCarfax.make+" "+vehicleCarfax.model;
			    	    		row[4] = vehicleUrlPath+vehicleCarfax.vin;
			    	    		row[5] = vehicleCarfax.category;
			    	    		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicleCarfax.vin);
			    	    		String str = "";
			    	    		for(VehicleImage img : vImageList) {
			    	    			str = str +imageUrlPath+img.path+"|";
			    	    		}
			    	    		row[6] = str;
			    	    		if(profile != null) {
			    	    			row[7] = profile.address;
			    	    		} else {
			    	    			row[7] = "";
			    	    		}
			    	    		row[8] = "San Francisco";
			    	    		row[9] = "CA";
			    	    		row[10] = "94105";
			    	    		row[11] = "United States";
			    	    		row[12] = "Dealer";
			    	    		if(profile != null) {
			    	    			row[13] = profile.myname;
			    	    			row[14] = profile.dealer_id;
				    	    		row[15] = profile.email;
				    	    		row[16] = profile.phone;
				    	    		row[17] = profile.web;
			    	    		} else {
			    	    			row[13] = "";
			    	    			row[14] = "";
				    	    		row[15] = "";
				    	    		row[16] = "";
				    	    		row[17] = "";
			    	    		}
			    	    		
			    	    		row[18] = "";
			    	    		row[19] = vehicleCarfax.make;
			    	    		row[20] = vehicleCarfax.model;
			    	    		row[21] = vehicleCarfax.trim;
			    	    		row[22] = vehicleCarfax.bodyStyle;
			    	    		row[23] = vehicleCarfax.mileage;
			    	    		row[24] = vehicleCarfax.year;
			    	    		row[25] = "USD";
			    	    		row[26] = vehicleCarfax.price.toString();
			    	    		row[27] = vehicleCarfax.getPrice().toString();
			    	    		row[28] = "";
			    	    		row[29] = "";
			    	    		row[30] = "";
			    	    		row[31] = "";
			    	    		row[32] = vehicleCarfax.exteriorColor;
			    	    		row[33] = vehicleCarfax.interiorColor;
			    	    		row[34] = "fabric";
			    	    		row[35] = vehicleCarfax.doors;
			    	    		row[36] = vehicleCarfax.cylinders;
			    	    		row[37] = vehicleCarfax.engine;
			    	    		row[38] = vehicleCarfax.drivetrain;
			    	    		row[39] = vehicleCarfax.transmission;
			    	    		row[40] = "YES";
			    	    		row[41] = vehicleCarfax.description; //description
			    	    		
			    	    		String standardFeatures = "";
			    	    		if(vehicleCarfax.drivetrain != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.drivetrain+",";
			    	    		}
			    	    		if(vehicleCarfax.fuelType != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.fuelType+",";
			    	    		}
			    	    		if(vehicleCarfax.fuelTank != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.fuelTank+",";
			    	    		}
			    	    		if(vehicleCarfax.headlights != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.headlights+",";
			    	    		}
			    	    		if(vehicleCarfax.mirrors != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.mirrors+",";
			    	    		}
			    	    		if(vehicleCarfax.roof != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.roof+",";
			    	    		}
			    	    		if(vehicleCarfax.acceleration != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.acceleration+",";
			    	    		}
			    	    		if(vehicleCarfax.standardSeating != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.standardSeating+",";
			    	    		}
			    	    		if(vehicleCarfax.engine != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.engine+",";
			    	    		}
			    	    		if(vehicleCarfax.camType != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.camType+",";
			    	    		}
			    	    		if(vehicleCarfax.valves != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.valves+",";
			    	    		}
			    	    		if(vehicleCarfax.cylinders != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.cylinders+",";
			    	    		}
			    	    		if(vehicleCarfax.fuelQuality != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.fuelQuality+",";
			    	    		}
			    	    		if(vehicleCarfax.horsePower != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.horsePower+",";
			    	    		}
			    	    		if(vehicleCarfax.transmission != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.transmission+",";
			    	    		}
			    	    		if(vehicleCarfax.gears != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.gears+",";
			    	    		}
			    	    		if(vehicleCarfax.brakes != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.brakes+",";
			    	    		}
			    	    		if(vehicleCarfax.frontBrakeDiameter != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.frontBrakeDiameter+",";
			    	    		}
			    	    		if(vehicleCarfax.frontBrakeType != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.frontBrakeType+",";
			    	    		}
			    	    		if(vehicleCarfax.rearBrakeDiameter != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.rearBrakeDiameter+",";
			    	    		}
			    	    		if(vehicleCarfax.rearBrakeType != null) {
			    	    			standardFeatures = standardFeatures + vehicleCarfax.rearBrakeType;
			    	    		}
			    	    		row[42] = standardFeatures;
			    	    		
			    	    		
			    	    		String standardOptions = "";
			    	    		
			    	    		if(vehicleCarfax.activeHeadRestrains != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.activeHeadRestrains+",";
			    	    		}
			    	    		if(vehicleCarfax.bodySideReinforcements != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.bodySideReinforcements+",";
			    	    		}
			    	    		if(vehicleCarfax.crumpleZones != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.crumpleZones+",";
			    	    		}
			    	    		if(vehicleCarfax.impactAbsorbingBumpers != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.impactAbsorbingBumpers+",";
			    	    		}
			    	    		if(vehicleCarfax.impactSensor != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.impactSensor+",";
			    	    		}
			    	    		if(vehicleCarfax.parkingSensors != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.parkingSensors+",";
			    	    		}
			    	    		if(vehicleCarfax.seatbelts != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.seatbelts+",";
			    	    		}
			    	    		if(vehicleCarfax.interiorColor != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.interiorColor+",";
			    	    		}
			    	    		if(vehicleCarfax.powerOutlet != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.powerOutlet+",";
			    	    		}
			    	    		if(vehicleCarfax.powerSteering != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.powerSteering+",";
			    	    		}
			    	    		if(vehicleCarfax.rearViewCamera != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.rearViewCamera+",";
			    	    		}
			    	    		if(vehicleCarfax.rearViewMonitor != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.rearViewMonitor+",";
			    	    		}
			    	    		if(vehicleCarfax.remoteTrunkRelease != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.remoteTrunkRelease+",";
			    	    		}
			    	    		if(vehicleCarfax.steeringWheel != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.steeringWheel+",";
			    	    		}
			    	    		if(vehicleCarfax.steeringWheelControls != null) {
			    	    			standardOptions = standardOptions + vehicleCarfax.steeringWheelControls;
			    	    		}
			    	    		
			    	    		row[43] = standardOptions;
			    	    		row[44] = "";
			    	    		row[45] = "Used";
			    	    		row[46] = "2012-09-16-11:00:00";
			    	    		row[47] = "2012-10-16-11:00:00";
			    	    		
			    	    		writer.writeNext(row);
			    	    	}
			    	    	
			    	    	 writer.close();
			    	    	 fis = new FileInputStream(filename);
				                client.storeFile(filename, fis);
				                client.logout();
				                if (fis != null) {
				                    fis.close();
				                }
			    		}
			    		
			    		
			    	}
		    	}
		    	
	    	}	
	    	return ok();
    	}	
    }
    
    
    public static Result uplaodSoundFile() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	DynamicForm dynamicForm = Form.form().bindFromRequest();
			String vin = dynamicForm.get("vinNum");
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file0");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+vin+"-"+userObj.id+File.separator+"audio");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+vin+"-"+userObj.id+File.separator+"audio"+File.separator+fileName;
	    	    File file = picture.getFile();
	    	    try {
	    	    		FileUtils.moveFile(file, new File(filePath));
	    	    		VehicleAudio audio = new VehicleAudio();
	    	    		audio.vin = vin;
	    	    		audio.path = File.separator+session("USER_LOCATION")+File.separator+vin+"-"+userObj.id+File.separator+"audio"+File.separator+fileName;
	    	    		audio.fileName = fileName;
	    	    		audio.user = userObj;
	    	    		audio.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	    		audio.save();
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	return ok();
    	}	
    }
	
    
    public static Result getAllAudio(String vin) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<VehicleAudio> audioList = VehicleAudio.getByUserAndVin(user, vin);
	    	List<AudioVM> vmList = new ArrayList<>();
	    	
	    	for(VehicleAudio audio: audioList) {
	    		AudioVM vm = new AudioVM();
	    		vm.id = audio.id;
	    		vm.path = audio.path;
	    		vm.fileName = audio.fileName;
	    		vmList.add(vm);
	    	}
	    	return ok(Json.toJson(vmList));
    	}
    }
    
    
    public static Result getVirtualTour(String vin) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	VirtualTour virtualTour = VirtualTour.findByUserAndVin(user, vin);
	    	VirtualTourVM vm = new VirtualTourVM();
	    	if(virtualTour != null) {
	    		vm.desktopUrl = virtualTour.desktopUrl;
	    		vm.mobileUrl = virtualTour.mobileUrl;
	    	}
	    	return ok(Json.toJson(vm));
    	}	
    }
    
    
    public static Result saveVData() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<VirtualTourVM> form = DynamicForm.form(VirtualTourVM.class).bindFromRequest();
	    	VirtualTourVM vm = form.get();
	    	VirtualTour virtualTour = VirtualTour.findByUserAndVin(user, vm.vin);
	    	if(virtualTour == null) {
		    	VirtualTour vt = new VirtualTour();
		    	vt.desktopUrl = vm.desktopUrl;
		    	vt.mobileUrl = vm.mobileUrl;
		    	vt.vin = vm.vin;
		    	vt.user = user;
		    	vt.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	vt.save();
	    	} else {
	    		virtualTour.setDesktopUrl(vm.desktopUrl);
	    		virtualTour.setMobileUrl(vm.mobileUrl);
	    		virtualTour.update();
	    	}
	    	return ok();
    	}	
    }
    
    
    public static Result editImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	    	VehicleImage image = VehicleImage.findById(vm.imageId);
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	    	Thumbnails.of(croppedImage).scale(1.0).toFile(file);
	    	
	    	Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result uploadSliderPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"SliderImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"SliderImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"SliderImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SliderImage imageObj = SliderImage.getByImagePath("/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"SliderImages"+"/"+fileName);
				if(imageObj == null) {
					List<SliderImage> sliderList = SliderImage.find.all();
					SliderImage vImage = new SliderImage();
					vImage.imgName = fileName;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"SliderImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"SliderImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					if(sliderList.size() == 0) {
						vImage.sliderNumber = 1;
					}
					if(sliderList.size() == 1) {
						if(sliderList.get(0).sliderNumber == 1) {
							vImage.sliderNumber = 2;
						}
						if(sliderList.get(0).sliderNumber == 2) {
							vImage.sliderNumber = 3;
						}
						if(sliderList.get(0).sliderNumber == 3) {
							vImage.sliderNumber = 1;
						}
					}
					if(sliderList.size() == 2) {
						if((sliderList.get(0).sliderNumber == 1 && sliderList.get(1).sliderNumber == 2)||(sliderList.get(0).sliderNumber == 2 && sliderList.get(1).sliderNumber == 1)) {
							vImage.sliderNumber = 3;
						}
						if((sliderList.get(0).sliderNumber == 1 && sliderList.get(1).sliderNumber == 3)||(sliderList.get(0).sliderNumber == 3 && sliderList.get(1).sliderNumber == 1)) {
							vImage.sliderNumber = 2;
						}
						if((sliderList.get(0).sliderNumber == 2 && sliderList.get(1).sliderNumber == 3)||(sliderList.get(0).sliderNumber == 3 && sliderList.get(1).sliderNumber == 2)) {
							vImage.sliderNumber = 1;
						}
					}
					vImage.save();
					
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result uploadFeaturedPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"FeaturedImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+File.separator+userObj.id+File.separator+"FeaturedImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+File.separator+userObj.id+File.separator+"FeaturedImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				FeaturedImage imageObj = FeaturedImage.getByImagePath("/"+userObj.id+"/"+"FeaturedImages"+"/"+fileName);
				if(imageObj == null) {
					FeaturedImage vImage = new FeaturedImage();
					vImage.imgName = fileName;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"FeaturedImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"FeaturedImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.save();
					
				}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result getSliderImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	File file = null;
	    	SliderImage image = SliderImage.findById(id);
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	return ok(file);
    	}	
    }
    
    
    public static Result getFeaturedImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	File file = null;
	    	FeaturedImage image = FeaturedImage.findById(id);
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	return ok(file);
    	}
    }
    
    
    public static Result deleteSliderImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SliderImage image = SliderImage.findById(id);
	    	File file = new File(rootDir+File.separator+user.id+File.separator+"SliderImages"+File.separator+image.imgName);
	    	file.delete();
	    	File thumbfile = new File(rootDir+File.separator+user.id+File.separator+"SliderImages"+File.separator+"thumbnail_"+image.imgName);
	    	thumbfile.delete();
	    	image.delete();
	    	return ok();
    	}
    }
    
    
    public static Result deleteFeaturedImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	FeaturedImage image = FeaturedImage.findById(id);
	    	File file = new File(rootDir+File.separator+user.id+File.separator+"FeaturedImages"+File.separator+image.imgName);
	    	file.delete();
	    	File thumbfile = new File(rootDir+File.separator+user.id+File.separator+"FeaturedImages"+File.separator+"thumbnail_"+image.imgName);
	    	thumbfile.delete();
	    	image.delete();
	    	return ok();
    	}
    }
    
    
    public static Result deleteAudioFile(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	VehicleAudio audio = VehicleAudio.findById(id);
	    	File file = new File(rootDir+audio.path);
	    	file.delete();
	    	audio.delete();
	    	return ok();
    	}	
    }
    
    
    public static Result getSliderAndFeaturedImages() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	
	    	Map<String,List> map = new HashMap<>();
	    	//slconfig.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	List<SliderImage> sliderList = SliderImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	List<ImageVM> sliderVMList = new ArrayList<>();
	    	List<ImageVM> configList = new ArrayList<>();
	    	
	    	reorderSliderImagesForFirstTime(sliderList);
	    	
	    	for(SliderImage slider: sliderList) {
	    		ImageVM vm = new ImageVM();
	    		vm.id = slider.id;
	    		vm.path = slider.path;
	    		vm.imgName = slider.imgName;
	    		vm.row = slider.row;
	    		vm.col = slider.col;
	    		sliderVMList.add(vm);
	    	}
	    	
	    	map.put("sliderList", sliderVMList);
	    	/*List<FeaturedImage> featuredList = FeaturedImage.findByUser(user);*/
	    	List<FeaturedImage> featuredList = FeaturedImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	List<ImageVM> featuredVMList = new ArrayList<>();
	    	
	    	reorderFeaturedImagesForFirstTime(featuredList);
	    	
	    	for(FeaturedImage featured: featuredList) {
	    		ImageVM vm = new ImageVM();
	    		vm.id = featured.id;
	    		vm.path = featured.path;
	    		vm.imgName = featured.imgName;
	    		vm.row = featured.row;
	    		vm.col = featured.col;
	    		featuredVMList.add(vm);
	    	}
	    	
	    	map.put("featuredList", featuredVMList);
	    	
	    	SliderImageConfig sliderConfig = SliderImageConfig.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	ImageVM slider = new ImageVM();
	    	if(sliderConfig != null) {
	    		slider.height = sliderConfig.cropHeight;
	    		slider.width = sliderConfig.cropWidth;
	    	} else {
	    		SliderImageConfig slconfig = new SliderImageConfig();
	    		slconfig.cropHeight = 840;
	    		slconfig.cropWidth = 1400;
	    		slconfig.user = user;
				slconfig.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		slconfig.save();
	    		slider.height = slconfig.cropHeight;
	    		slider.width = slconfig.cropWidth;
	    	}
	    	configList.add(slider);
	    	
	    	FeaturedImageConfig featuredConfig = FeaturedImageConfig.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	ImageVM featured = new ImageVM();
	    	if(featuredConfig != null) {
	    		featured.height = featuredConfig.cropHeight;
	    		featured.width = featuredConfig.cropWidth;
	    	} else {
	    		FeaturedImageConfig ftdconfig = new FeaturedImageConfig();
	    		ftdconfig.cropHeight = 95;
	    		ftdconfig.cropWidth = 160;
	    		ftdconfig.user = user;
				ftdconfig.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		ftdconfig.save();
	    		featured.height = ftdconfig.cropHeight;
	    		featured.width = ftdconfig.cropWidth;
	    	}
	    	configList.add(featured);
	    	map.put("configList", configList);
	    	
	    	SiteContent content = SiteContent.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	List<SiteContentVM> siteContentList = new ArrayList<>();
	    	SiteContentVM contentVM = new SiteContentVM();
	    	if(content != null) {
	    		contentVM.heading = content.heading;
	    		contentVM.descHeading = content.descHeading;
	    		contentVM.description = content.description;
	    	} else {
	    		contentVM.heading = "";
	    		contentVM.descHeading = "";
	    		contentVM.description = "";
	    	}
	    	siteContentList.add(contentVM);
	    	map.put("contentVM", siteContentList);
	    	
	    	return ok(Json.toJson(map));
    	}	
    }
    
    
    private static void reorderSliderImagesForFirstTime(List<SliderImage> imageList) {
    	if(imageList.size() > 0) {
    			for(int i = 0, col = 0 ; i < imageList.size() ; i++) {
    				if(imageList.get(i).row == null) {
	    				imageList.get(i).setRow(  col / 6);
	    				imageList.get(i).setCol( col % 6);
	    				imageList.get(i).update();
    				}
    				col++;
    			}
    			
    		
    	}
		
    }
    
    
    private static void reorderFeaturedImagesForFirstTime(List<FeaturedImage> imageList) {
    	if(imageList.size() > 0) {
    			for(int i = 0, col = 0 ; i < imageList.size() ; i++) {
    				if(imageList.get(i).row == null) {
	    				imageList.get(i).setRow(  col / 6);
	    				imageList.get(i).setCol( col % 6);
	    				imageList.get(i).update();
    				}
    				col++;
    			}
    			
    	}
		
    }
    
    
    public static Result saveSiteHeading(String heading) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SiteContent content = SiteContent.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(content != null) {
	    		content.setHeading(heading);
	    		content.update();
	    	} else {
	    		SiteContent contentObj = new SiteContent();
	    		contentObj.heading = heading;
	    		contentObj.user = user;
				contentObj.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		contentObj.save();
	    	}
	    	return ok();
    	}
    }
    
    
    public static Result saveSiteDescription() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<SiteContentVM> form = DynamicForm.form(SiteContentVM.class).bindFromRequest();
	    	SiteContentVM vm = form.get();
	    	SiteContent content = SiteContent.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(content != null) {
	    		content.setDescHeading(vm.descHeading);
	    		content.setDescription(vm.description);
	    		content.update();
	    	} else {
	    		SiteContent contentObj = new SiteContent();
	    		contentObj.descHeading = vm.descHeading;
	    		contentObj.description = vm.description;
	    		contentObj.user = user;
				contentObj.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		contentObj.save();
	    	}
	    	return ok();
    	}	
    }
    
    
    
    public static Result getSliderImageDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SliderImage image = SliderImage.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.imgName;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.description = image.description;
			vm.link = image.link;
			SliderImageConfig config = SliderImageConfig.findByUser(user);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
    
    public static Result getFeaturedImageDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	FeaturedImage image = FeaturedImage.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.imgName;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.description = image.description;
			vm.link = image.link;
			FeaturedImageConfig config = FeaturedImageConfig.findByUser(user);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
    
    public static Result editSliderImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	    	SliderImage image = SliderImage.findById(vm.imageId);
	    	image.setImgName(vm.imgName);
	    	image.setDescription(vm.description);
	    	image.setLink(vm.link);
	    	image.update();
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	    	SliderImageConfig config = SliderImageConfig.findByUser(user);
	    	//Thumbnails.of(croppedImage).scale(1.0).toFile(file);
	    	Thumbnails.of(croppedImage).size(config.cropWidth,config.cropHeight).toFile(file);
	    	Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result editFeaturedImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	    	FeaturedImage image = FeaturedImage.findById(vm.imageId);
	    	image.setImgName(vm.imgName);
	    	image.setDescription(vm.description);
	    	image.setLink(vm.link);
	    	image.update();
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	    	FeaturedImageConfig config = FeaturedImageConfig.findByUser(user);
	    	//Thumbnails.of(croppedImage).size(150, 150).toFile(file);
	    	Thumbnails.of(croppedImage).size(config.cropWidth,config.cropHeight).toFile(file);
	    	
	    	Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result getImageConfig() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Map<String,Object> map = new HashMap<>();
	    	SliderImageConfig config = SliderImageConfig.findByUser(user);
	    	if(config != null) {
		    	ImageVM vm1 = new ImageVM();
		    	vm1.width = config.cropWidth;
		    	vm1.height = config.cropHeight;
		    	map.put("slider", vm1);
	    	}
	    	FeaturedImageConfig config2 = FeaturedImageConfig.findByUser(user);
	    	if(config2 != null) {
		    	ImageVM vm2 = new ImageVM();
		    	vm2.width = config2.cropWidth;
		    	vm2.height = config2.cropHeight;
		    	map.put("featured", vm2);
	    	}
	    	List<NewsletterDate> objList = NewsletterDate.findAll();
	    	SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
	    	if(objList.size() > 0) {
	    		map.put("NewsletterDate", objList.get(0).dateOfMonth);
	    		if(objList.get(0).newsletterTime != null) {
	    			map.put("newsletterTime", df.format(objList.get(0).newsletterTime));
	    		}
	    		map.put("NewsletterId", objList.get(0).id);
	    		map.put("NewsletterTimeZone", objList.get(0).timeZone);
	    	} else {
	    		map.put("NewsletterId", 0);
	    		map.put("NewsletterTimeZone", "");
	    	}
	    	
	       PremiumLeads pLeads = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
	       if(pLeads != null){	
	    	   map.put("premiumLeads", pLeads);
	       }  
	    	
	    	return ok(Json.toJson(map));
    	}
    }
    
    
    public static Result saveSliderConfig(Integer width,Integer height) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SliderImageConfig config = SliderImageConfig.findByUser(user);
	    	
	    	if(config != null) {
	    		config.setCropHeight(height);
	    		config.setCropWidth(width);
	    		config.update();
	    	} else {
	           config = new SliderImageConfig();
	           config.setCropHeight(height);
	           config.setCropWidth(width);
	           config.user = user;
				config.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	           config.save();
	        }
	    	return ok();
    	}	
    }
    
    public static Result savePremiumConfig(String priceVehical,String premiumFlag){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	PremiumLeads preMim = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(preMim != null){
	    		preMim.setPremium_amount(priceVehical);
	    		if(premiumFlag.equals("false")){
	    			preMim.setPremium_flag(0);
	    		}else if(premiumFlag.equals("true")){
	    			preMim.setPremium_flag(1);
	    		}
	    		preMim.update();
	    	}else{
	    		PremiumLeads pLeads = new PremiumLeads();
	    		System.out.println(premiumFlag);
	    		pLeads.setPremium_amount(priceVehical);
	    		if(premiumFlag.equals("false")){
	    			pLeads.setPremium_flag(0);
	    		}else if(premiumFlag.equals("true")){
	    			pLeads.setPremium_flag(1);
	    		}else if(premiumFlag.equals("0")){
	    			pLeads.setPremium_flag(0);
	    		}	
	    		pLeads.setUser(user);
	    		pLeads.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    		pLeads.save();
	    	}
	
	    	return ok();
    	}	
    }
    
    public static Result saveFeaturedConfig(Integer width,Integer height) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	FeaturedImageConfig config = FeaturedImageConfig.findByUser(user);
	    	
	    	if(config != null) {
	    		config.setCropHeight(height);
	    		config.setCropWidth(width);
	    		config.update();
	    	} else {
	           config = new FeaturedImageConfig();
	           config.setCropHeight(height);
	           config.setCropWidth(width);
	           config.user = user;
	           config.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	           config.save();
	        }
	
	    	return ok();
    	}	
    }
    
    
    
    public static Result exportDataAsCSV() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    		return ok(home.render(""));
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
    		return ok(home.render(""));
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
    
    
    public static Result getAllRequestInfo() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    public static Result getAllVehical(){
    		List<Vehicle> vehicles = Vehicle.findByNewArrAndLocation(Long.valueOf(session("USER_LOCATION")));
    		return ok(Json.toJson(vehicles));
    }
    
    
    public static Result getAllCompletedLeadsbyId(Integer leadId){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user;
    		if(leadId == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(leadId);
    		}
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findAllCompletedToUser(user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllCompletedToUser(user);
	    	List<TradeIn> tradeIns = TradeIn.findAllCompletedToUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	
	    	fillLeadsData(listData, requestMoreInfos, tradeIns, infoVMList);
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }
    
    public static Result getTestDirConfirById(Integer leadId){
    	
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user;
    		if(leadId == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(leadId);
    		}
	    	
    		List<ScheduleTest> listData = ScheduleTest.findByConfirmLeads(Long.valueOf(session("USER_LOCATION")), user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmLeads(Long.valueOf(session("USER_LOCATION")), user);
	    	List<TradeIn> tradeIns = TradeIn.findByConfirmLeads(Long.valueOf(session("USER_LOCATION")), user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	
	    	fillLeadsData(listData, requestMoreInfos, tradeIns, infoVMList);
	    	
	    	// java.util.Collections.sort(infoVMList,new infoVMListComparatorCountHigh());
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    	
    }
    
   
	/* public static class infoVMListComparatorCountHigh implements Comparator<RequestInfoVM> {
			@Override
			public int compare(RequestInfoVM o2,RequestInfoVM o1) {
				return o1.confirmDate > o2.confirmDate ? -1 : o1.confirmDate < o2.confirmDate ? 1 : 0;
			}
	    }*/
	
    
    
    public static Result getAllSalesPersonLostAndComp(Integer leadId){
		
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user;
    		if(leadId == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(leadId);
    		}
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findAllAssignedLeadsToUser(user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllAssignedLeadsToUser(user);
	    	List<TradeIn> tradeIns = TradeIn.findAllAssignedLeadsToUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	
	    	fillLeadsData(listData, requestMoreInfos, tradeIns, infoVMList);
	    	
	    /*	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
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
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.bestDay = info.bestDay;
	    		vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status = info.leadStatus;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}	
	    		vm.typeOfLead = "Schedule Test Drive";
	    		List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 0;
	    		infoVMList.add(vm);
	    	}
	    	
	    	for(TradeIn info: tradeIns) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
					vm.price = vehicle.price;
	    		}
	    		vm.name = info.firstName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		//vm.bestDay = info.bestDay;
	    		//vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status = info.status;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 2;
	    		infoVMList.add(vm);
	    	}
	    	
	    	for(RequestMoreInfo info: requestMoreInfos) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
					vm.price = vehicle.price;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		//vm.bestDay = info.bestDay;
	    		//vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status = info.status;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findRequestMore(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 1;
	    		infoVMList.add(vm);
	    	}*/
	    
	    	return ok(Json.toJson(infoVMList));
    	}
    }
    
    public static Result getTestDirConfir(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findByConfirmLeads(Long.valueOf(session("USER_LOCATION")), user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmLeads(Long.valueOf(session("USER_LOCATION")), user);
	    	List<TradeIn> tradeIns = TradeIn.findByConfirmLeads(Long.valueOf(session("USER_LOCATION")), user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	
	    	fillLeadsData(listData, requestMoreInfos, tradeIns, infoVMList);
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    
    }
    
    
    public static void fillLeadsData(List<ScheduleTest> listData, List<RequestMoreInfo> requestMoreInfos, List<TradeIn> tradeIns, List<RequestInfoVM> infoVMList){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm");
    	SimpleDateFormat hourSDF = new SimpleDateFormat("hh:mm a");
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
    		if(info.confirmTime != null){
    			// Date _24HourDt = timedf.parse(info.confirmTime);
    			vm.bestTime = hourSDF.format(info.confirmTime);
    		}	
			vm.howContactedUs = info.contactedFrom;
    		vm.howFoundUs = info.hearedFrom;
    		vm.custZipCode = info.custZipCode;
    		vm.enthicity = info.enthicity;
    		vm.status =info.leadStatus;
    		vm.testDriveCompletedComment = info.testDriveCompletedComment;
    		vm.testDriveCompletedDuration = info.testDriveCompletedDuration;
    		if(info.statusDate != null){
    			vm.statusDate = df.format(info.statusDate);
    		}
    		vm.typeOfLead = "Schedule Test Drive";
    		/*List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);*/
    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
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
    				if(noteObj.saveHistory != null){
        				if(noteObj.saveHistory.equals(1)){
            				nFlag = 1;
            			}
        			}
    			}
    			list.add(obj);
    		}
    		vm.note = list;
    		vm.noteFlag = nFlag;
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
    		vm.option = 0;
    		findSchedulParentChildAndBro(infoVMList, info, df, vm);
    		//infoVMList.add(vm);
    	}
    	
    	for(TradeIn info: tradeIns) {
    		RequestInfoVM vm = new RequestInfoVM();
    		vm.id = info.id;
    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
    		vm.vin = info.vin;
    		if(vehicle != null) {
    			vm.model = vehicle.model;
    			vm.make = vehicle.make;
    			vm.stock = vehicle.stock;
    			vm.mileage = vehicle.mileage;
    			vm.year = vehicle.year;
				vm.price = vehicle.price;
				VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
        		if(vehicleImage!=null) {
        			vm.imgId = vehicleImage.getId().toString();
        		}
        		else {
        			vm.imgId = "/assets/images/no-image.jpg";
        		}
    		}
    		vm.name = info.firstName;
    		vm.phone = info.phone;
    		vm.email = info.email;
    		if(info.bestDay != null){
    			/*String chaArr[] = info.bestDay.split("-");
    			vm.bestDay = chaArr[1]+"/"+chaArr[2]+"/"+chaArr[0];*/
    			vm.bestDay = info.bestDay;
    		}
    		if(info.confirmTime != null){
    			vm.bestTime = hourSDF.format(info.confirmTime);
    		}
			vm.howContactedUs = info.contactedFrom;
    		vm.howFoundUs = info.hearedFrom;
    		vm.custZipCode = info.custZipCode;
    		vm.enthicity = info.enthicity;
    		vm.status =info.status;
    		if(info.statusDate != null){
    			vm.statusDate = df.format(info.statusDate);
    		}
    		vm.typeOfLead = "Trade-In Appraisal";
    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
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
    				if(noteObj.saveHistory != null){
        				if(noteObj.saveHistory.equals(1)){
            				nFlag = 1;
            			}
        			}
    			}
    			list.add(obj);
    		}
    		vm.note = list;
    		vm.noteFlag = nFlag;
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
    		vm.option = 2;
    		findTreadParentChildAndBro(infoVMList, info, df, vm);
    		//infoVMList.add(vm);
    	}
    	
    	for(RequestMoreInfo info: requestMoreInfos) {
    		RequestInfoVM vm = new RequestInfoVM();
    		vm.id = info.id;
    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
    		vm.vin = info.vin;
    		if(vehicle != null) {
    			vm.model = vehicle.model;
    			vm.make = vehicle.make;
    			vm.stock = vehicle.stock;
    			vm.mileage = vehicle.mileage;
    			vm.year = vehicle.year;
				vm.price = vehicle.price;
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
    		if(info.confirmTime != null){
    			vm.bestTime = hourSDF.format(info.confirmTime);
    		}
			vm.howContactedUs = info.contactedFrom;
    		vm.howFoundUs = info.hearedFrom;
    		vm.custZipCode = info.custZipCode;
    		vm.enthicity = info.enthicity;
    		vm.status =info.status;
    		if(info.statusDate != null){
    			vm.statusDate = df.format(info.statusDate);
    		}
    		vm.typeOfLead = "Request More Info";
    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
    				if(noteObj.saveHistory != null){
        				if(noteObj.saveHistory.equals(1)){
            				nFlag = 1;
            			}
        			}
    			}
    			list.add(obj);
    		}
    		vm.note = list;
    		vm.noteFlag = nFlag;
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
    		vm.option = 1;
    		
    		findRequestParentChildAndBro(infoVMList, info, df, vm);
    		//infoVMList.add(vm);
    	}
    }
    
    public static Result getAllCompletedLeads(){
    	AuthUser user = (AuthUser) getLocalUser();
    	
    	List<ScheduleTest> listData = ScheduleTest.findAllCompletedToUser(user);
    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllCompletedToUser(user);
    	List<TradeIn> tradeIns = TradeIn.findAllCompletedToUser(user);
    	List<RequestInfoVM> infoVMList = new ArrayList<>();
    	
    	fillLeadsData(listData, requestMoreInfos, tradeIns, infoVMList);
    	
    	return ok(Json.toJson(infoVMList));
    }
    
    public static Result getAllLostAndCompLeads() {
      	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findAllAssignedLeadsToUser(user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllAssignedLeadsToUser(user);
	    	List<TradeIn> tradeIns = TradeIn.findAllAssignedLeadsToUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	
	    	fillLeadsData(listData, requestMoreInfos, tradeIns, infoVMList);
	    	
	    	
	    	/*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	Calendar time = Calendar.getInstance();*/
	    	
	    	
	    	/*for(ScheduleTest info: listData) {
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
	    		vm.bestDay = info.bestDay;
	    		vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status =info.leadStatus;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Schedule Test Drive";
	    		List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 0;
	    		infoVMList.add(vm);
	    	}
	    	
	    	for(TradeIn info: tradeIns) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
					vm.price = vehicle.price;
	    		}
	    		vm.name = info.firstName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		//vm.bestDay = info.bestDay;
	    		//vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status =info.status;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 2;
	    		infoVMList.add(vm);
	    	}
	    	
	    	for(RequestMoreInfo info: requestMoreInfos) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
					vm.price = vehicle.price;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		//vm.bestDay = info.bestDay;
	    		//vm.bestTime = info.bestTime;
				vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.status =info.status;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findRequestMore(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    		vm.option = 1;
	    		infoVMList.add(vm);
	    	}*/
	    
	    	return ok(Json.toJson(infoVMList));
    	}
    }
    
    public static Result getAllRequestInfoSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    	for(RequestMoreInfo info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.typeofVehicle=vehicle.typeofVehicle;
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
	    		vm.requestDate = df.format(info.requestDate);
	    		
	    		if(!info.custZipCode.equals("null")){
	    			vm.custZipCode = info.custZipCode;
	    		}
	    		
	    		vm.enthicity = info.enthicity;
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findRequestMore(info);
	    		List<NoteVM> list = new ArrayList<>();
	    		Integer nFlag = 0;
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = time.format(noteObj.createdTime);
	    			if(noteObj.saveHistory != null){
	    				if(noteObj.saveHistory != null){
	        				if(noteObj.saveHistory.equals(1)){
	            				nFlag = 1;
	            			}
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
	    		
	    		findRequestParentChildAndBro(infoVMList, info, df, vm);
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }
    
    public static void findSchedulParentChildAndBro(List<RequestInfoVM> infoVMList, ScheduleTest info,SimpleDateFormat df, RequestInfoVM vm){
    	SimpleDateFormat timedf = new SimpleDateFormat("hh:mm a");
    	List<RequestInfoVM> rList2 = new ArrayList<>();
		if(info.parentId != null){
			ScheduleTest sTest = ScheduleTest.findByIdAndParent(info.parentId);
			if(sTest != null){
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = sTest.id;
	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(sTest.vin);
	    		rList1.vin = sTest.vin;
	    		if(vehicle1 != null) {
	    			rList1.model = vehicle1.model;
	    			rList1.make = vehicle1.make;
	    			rList1.stock = vehicle1.stock;
	    			rList1.year = vehicle1.year;
	    			rList1.mileage = vehicle1.mileage;
	    			rList1.price = vehicle1.price;
	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	        		if(vehicleImage!=null) {
	        			rList1.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			rList1.imgId = "/assets/images/no-image.jpg";
	        		}
	    			
	    		}
	    		if(sTest.confirmDate != null){
	    			rList1.bestDay = df.format(sTest.confirmDate);
	    		}
	    		if(sTest.confirmTime != null){
	    			rList1.bestTime = timedf.format(sTest.confirmTime);
	    		}
	    		rList1.name = sTest.name;
	    		rList1.phone = sTest.phone;
	    		rList1.email = sTest.email;
	    		rList1.requestDate = df.format(sTest.scheduleDate);
	    		rList1.typeOfLead = "Schedule Test Drive";
	    		
	    		rList2.add(rList1);
			}
		}
		
		if(info.parentId != null){
			List<ScheduleTest> tIns = ScheduleTest.findAllByParentID(info.parentId);
			for(ScheduleTest info1:tIns){
				if(!info.getId().equals(info1.getId())){
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = info1.id;
	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
	    		rList1.vin = info1.vin;
	    		if(vehicle1 != null) {
	    			rList1.model = vehicle1.model;
	    			rList1.make = vehicle1.make;
	    			rList1.stock = vehicle1.stock;
	    			rList1.year = vehicle1.year;
	    			rList1.mileage = vehicle1.mileage;
	    			rList1.price = vehicle1.price;
	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	        		if(vehicleImage!=null) {
	        			rList1.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			rList1.imgId = "/assets/images/no-image.jpg";
	        		}
	    		}
	    		if(info1.confirmDate != null){
	    			rList1.bestDay = df.format(info1.confirmDate);
	    		}
	    		if(info1.confirmTime != null){
	    			rList1.bestTime = timedf.format(info1.confirmTime);
	    		}
	    		rList1.name = info1.name;
	    		rList1.phone = info1.phone;
	    		rList1.email = info1.email;
	    		rList1.requestDate = df.format(info1.scheduleDate);
	    		rList1.typeOfLead = "Schedule Test Drive";
	    		
	    		rList2.add(rList1);
			}
			}
		}
		
		List<ScheduleTest> tIns = ScheduleTest.findAllByParentID(info.getId());
		for(ScheduleTest info1:tIns){
			RequestInfoVM rList1 = new RequestInfoVM();
			rList1.id = info1.id;
    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    		rList1.vin = info1.vin;
    		if(vehicle1 != null) {
    			rList1.model = vehicle1.model;
    			rList1.make = vehicle1.make;
    			rList1.stock = vehicle1.stock;
    			rList1.year = vehicle1.year;
    			rList1.mileage = vehicle1.mileage;
    			rList1.price = vehicle1.price;
    			rList1.bodyStyle =vehicle1.bodyStyle;
    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
        		if(vehicleImage!=null) {
        			rList1.imgId = vehicleImage.getId().toString();
        		}
        		else {
        			rList1.imgId = "/assets/images/no-image.jpg";
        		}
    		}
    		if(info1.confirmDate != null){
    			rList1.bestDay = df.format(info1.confirmDate);
    		}
    		if(info1.confirmTime != null){
    			rList1.bestTime = timedf.format(info1.confirmTime);
    		}
    		
    		rList1.name = info1.name;
    		rList1.phone = info1.phone;
    		rList1.email = info1.email;
    		rList1.requestDate = df.format(info1.scheduleDate);
    		rList1.typeOfLead = "Schedule Test Drive";
    		
    		rList2.add(rList1);
		}
		vm.parentChildLead = rList2;
		
		infoVMList.add(vm);
    	
    }
    
    public static void findTreadParentChildAndBro(List<RequestInfoVM> infoVMList, TradeIn info,SimpleDateFormat df, RequestInfoVM vm){
    	SimpleDateFormat timedf = new SimpleDateFormat("hh:mm a");
    	List<RequestInfoVM> rList2 = new ArrayList<>();
		if(info.parentId != null){
			TradeIn tIn = TradeIn.findByIdAndParent(info.parentId);
			if(tIn != null){
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = tIn.id;
	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(tIn.vin);
	    		rList1.vin = tIn.vin;
	    		if(vehicle1 != null) {
	    			rList1.model = vehicle1.model;
	    			rList1.make = vehicle1.make;
	    			rList1.stock = vehicle1.stock;
	    			rList1.year = vehicle1.year;
	    			rList1.mileage = vehicle1.mileage;
	    			rList1.price = vehicle1.price;
	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	        		if(vehicleImage!=null) {
	        			rList1.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			rList1.imgId = "/assets/images/no-image.jpg";
	        		}
	    			
	    		}
	    		if(tIn.confirmDate != null){
	    			rList1.bestDay = df.format(tIn.confirmDate);
	    		}
	    		if(tIn.confirmTime != null){
	    			rList1.bestTime = timedf.format(tIn.confirmTime);
	    		}
	    		rList1.name = tIn.firstName;
	    		rList1.phone = tIn.phone;
	    		rList1.email = tIn.email;
	    		rList1.requestDate = df.format(tIn.tradeDate);
	    		rList1.typeOfLead = "Trade-In Appraisal";
	    		
	    		rList2.add(rList1);
			}
		}
		
		if(info.parentId !=null){
			
			List<TradeIn> tIns = TradeIn.findAllByParentID(info.parentId);
			for(TradeIn info1:tIns){
				if(!info.getId().equals(info1.getId())){
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = info1.id;
	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
	    		rList1.vin = info1.vin;
	    		if(vehicle1 != null) {
	    			rList1.model = vehicle1.model;
	    			rList1.make = vehicle1.make;
	    			rList1.stock = vehicle1.stock;
	    			rList1.year = vehicle1.year;
	    			rList1.mileage = vehicle1.mileage;
	    			rList1.price = vehicle1.price;
	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	        		if(vehicleImage!=null) {
	        			rList1.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			rList1.imgId = "/assets/images/no-image.jpg";
	        		}
	    		}
	    		if(info1.confirmDate != null){
	    			rList1.bestDay = df.format(info1.confirmDate);
	    		}
	    		if(info1.confirmTime != null){
	    			rList1.bestTime = timedf.format(info1.confirmTime);
	    		}
	    		rList1.name = info1.firstName;
	    		rList1.phone = info1.phone;
	    		rList1.email = info1.email;
	    		rList1.requestDate = df.format(info1.tradeDate);
	    		rList1.typeOfLead = "Trade-In Appraisal";
	    		
	    		rList2.add(rList1);
			}
			}
	    }
		
		
		List<TradeIn> tIns = TradeIn.findAllByParentID(info.getId());
		for(TradeIn info1:tIns){
			RequestInfoVM rList1 = new RequestInfoVM();
			rList1.id = info1.id;
    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    		rList1.vin = info1.vin;
    		if(vehicle1 != null) {
    			rList1.model = vehicle1.model;
    			rList1.make = vehicle1.make;
    			rList1.stock = vehicle1.stock;
    			rList1.year = vehicle1.year;
    			rList1.mileage = vehicle1.mileage;
    			rList1.price = vehicle1.price;
    			rList1.bodyStyle =vehicle1.bodyStyle;
    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
        		if(vehicleImage!=null) {
        			rList1.imgId = vehicleImage.getId().toString();
        		}
        		else {
        			rList1.imgId = "/assets/images/no-image.jpg";
        		}
    		}
    		if(info1.confirmDate != null){
    			rList1.bestDay = df.format(info1.confirmDate);
    		}
    		if(info1.confirmTime != null){
    			rList1.bestTime = timedf.format(info1.confirmTime);
    		}
    		rList1.name = info1.firstName;
    		rList1.phone = info1.phone;
    		rList1.email = info1.email;
    		rList1.requestDate = df.format(info1.tradeDate);
    		rList1.typeOfLead = "Trade-In Appraisal";
    		
    		rList2.add(rList1);
		}
		vm.parentChildLead = rList2;
		
		infoVMList.add(vm);
    }
    
    public static void findRequestParentChildAndBro(List<RequestInfoVM> infoVMList, RequestMoreInfo info,SimpleDateFormat df, RequestInfoVM vm){
    	SimpleDateFormat timedf = new SimpleDateFormat("hh:mm a");
    	List<RequestInfoVM> rList2 = new ArrayList<>();
		if(info.parentId != null){
			
			RequestMoreInfo rMoreInfo = RequestMoreInfo.findByIdAndParent(info.parentId);
			if(rMoreInfo != null){
				
				RequestInfoVM rList1 = new RequestInfoVM();
				rList1.id = rMoreInfo.id;
	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(rMoreInfo.vin);
	    		rList1.vin = rMoreInfo.vin;
	    		if(vehicle1 != null) {
	    			rList1.model = vehicle1.model;
	    			rList1.make = vehicle1.make;
	    			rList1.stock = vehicle1.stock;
	    			rList1.year = vehicle1.year;
	    			rList1.mileage = vehicle1.mileage;
	    			rList1.price = vehicle1.price;
	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	        		if(vehicleImage!=null) {
	        			rList1.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			rList1.imgId = "/assets/images/no-image.jpg";
	        		}
	    			
	    		}
	    		if(rMoreInfo.confirmDate != null){
	    			rList1.bestDay = df.format(rMoreInfo.confirmDate);
	    		}
	    		if(rMoreInfo.confirmTime != null){
	    			rList1.bestTime = timedf.format(rMoreInfo.confirmTime);
	    		}
	    		rList1.name = rMoreInfo.name;
	    		rList1.phone = rMoreInfo.phone;
	    		rList1.email = rMoreInfo.email;
	    		rList1.requestDate = df.format(rMoreInfo.requestDate);
	    		rList1.typeOfLead = "Request More Info";
	    		
	    		rList2.add(rList1);
			}
		}
		
		if(info.parentId !=null){
			
		List<RequestMoreInfo> rMoreInfo1 = RequestMoreInfo.findAllByParentID(info.parentId);
		for(RequestMoreInfo info1:rMoreInfo1){
			if(!info1.getId().equals(info.getId())){
			RequestInfoVM rList1 = new RequestInfoVM();
			rList1.id = info1.id;
    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    		rList1.vin = info1.vin;
    		if(vehicle1 != null) {
    			rList1.model = vehicle1.model;
    			rList1.make = vehicle1.make;
    			rList1.stock = vehicle1.stock;
    			rList1.year = vehicle1.year;
    			rList1.mileage = vehicle1.mileage;
    			rList1.price = vehicle1.price;
    			rList1.bodyStyle =vehicle1.bodyStyle;
    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
        		if(vehicleImage!=null) {
        			rList1.imgId = vehicleImage.getId().toString();
        		}
        		else {
        			rList1.imgId = "/assets/images/no-image.jpg";
        		}
    		}
    		if(info1.confirmDate != null){
    			rList1.bestDay = df.format(info1.confirmDate);
    		}
    		if(info1.confirmTime != null){
    			rList1.bestTime = timedf.format(info1.confirmTime);
    		}
    		rList1.name = info1.name;
    		rList1.phone = info1.phone;
    		rList1.email = info1.email;
    		rList1.requestDate = df.format(info1.requestDate);
    		rList1.typeOfLead = "Request More Info";
    		
    		rList2.add(rList1);
		 }
		}
    }
		
		List<RequestMoreInfo> requestMoreInfo = RequestMoreInfo.findAllByParentID(info.getId());
		for(RequestMoreInfo info1:requestMoreInfo){
			RequestInfoVM rList1 = new RequestInfoVM();
			rList1.id = info1.id;
    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    		rList1.vin = info1.vin;
    		if(vehicle1 != null) {
    			rList1.model = vehicle1.model;
    			rList1.make = vehicle1.make;
    			rList1.stock = vehicle1.stock;
    			rList1.year = vehicle1.year;
    			rList1.mileage = vehicle1.mileage;
    			rList1.price = vehicle1.price;
    			rList1.bodyStyle =vehicle1.bodyStyle;
    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
        		if(vehicleImage!=null) {
        			rList1.imgId = vehicleImage.getId().toString();
        		}
        		else {
        			rList1.imgId = "/assets/images/no-image.jpg";
        		}
    		}
    		
    		if(info1.confirmDate != null){
    			rList1.bestDay = df.format(info1.confirmDate);
    		}
    		if(info1.confirmTime != null){
    			rList1.bestTime = timedf.format(info1.confirmTime);
    		}
    		rList1.name = info1.name;
    		rList1.phone = info1.phone;
    		rList1.email = info1.email;
    		rList1.requestDate = df.format(info1.requestDate);
    		rList1.typeOfLead = "Request More Info";
    		
    		rList2.add(rList1);
		}
		vm.parentChildLead = rList2;
		infoVMList.add(vm);
    }
    
    public static Result getAllScheduleTest() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    public static Result getAllScheduleTestAssigned() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findAllAssigned(user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllScheduledUser(user);
	    	List<TradeIn> tradeIns = TradeIn.findAllScheduledUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
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
	    		vm.typeOfLead = "Schedule Test Drive";
	    		//List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
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
	    		vm.option = 0;
	    		
	    		findSchedulParentChildAndBro(infoVMList, info, df, vm);
	    		/*List<RequestInfoVM> rList2 = new ArrayList<>();
	    		if(info.parentId != null){
	    			ScheduleTest sTest = ScheduleTest.findByIdAndParent(info.parentId);
	    			if(sTest != null){
	    				RequestInfoVM rList1 = new RequestInfoVM();
	    				rList1.id = sTest.id;
	    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(sTest.vin);
	    	    		rList1.vin = sTest.vin;
	    	    		if(vehicle1 != null) {
	    	    			rList1.model = vehicle1.model;
	    	    			rList1.make = vehicle1.make;
	    	    			rList1.stock = vehicle1.stock;
	    	    			rList1.year = vehicle1.year;
	    	    			rList1.mileage = vehicle1.mileage;
	    	    			rList1.price = vehicle1.price;
	    	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	    	        		if(vehicleImage!=null) {
	    	        			rList1.imgId = vehicleImage.getId().toString();
	    	        		}
	    	        		else {
	    	        			rList1.imgId = "/assets/images/no-image.jpg";
	    	        		}
	    	    			
	    	    		}
	    	    		rList1.name = sTest.name;
	    	    		rList1.phone = sTest.phone;
	    	    		rList1.email = sTest.email;
	    	    		rList1.requestDate = df.format(sTest.scheduleDate);
	    	    		rList1.typeOfLead = "Trade-In Appraisal";
	    	    		
	    	    		rList2.add(rList1);
	    			}
	    		}
	    		
	    		List<ScheduleTest> tIns = ScheduleTest.findAllByParentID(info.getId());
	    		for(ScheduleTest info1:tIns){
	    			RequestInfoVM rList1 = new RequestInfoVM();
    				rList1.id = info1.id;
    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    	    		rList1.vin = info1.vin;
    	    		if(vehicle1 != null) {
    	    			rList1.model = vehicle1.model;
    	    			rList1.make = vehicle1.make;
    	    			rList1.stock = vehicle1.stock;
    	    			rList1.year = vehicle1.year;
    	    			rList1.mileage = vehicle1.mileage;
    	    			rList1.price = vehicle1.price;
    	    			rList1.bodyStyle =vehicle1.bodyStyle;
    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
    	        		if(vehicleImage!=null) {
    	        			rList1.imgId = vehicleImage.getId().toString();
    	        		}
    	        		else {
    	        			rList1.imgId = "/assets/images/no-image.jpg";
    	        		}
    	    		}
    	    		rList1.name = info1.name;
    	    		rList1.phone = info1.phone;
    	    		rList1.email = info1.email;
    	    		rList1.requestDate = df.format(info1.scheduleDate);
    	    		rList1.typeOfLead = "Trade-In Appraisal";
    	    		
    	    		rList2.add(rList1);
	    		}
	    		vm.parentChildLead = rList2;
	    		
	    		infoVMList.add(vm);*/
	    	}
	    	
	    	for(TradeIn info: tradeIns) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
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
	    		vm.name = info.firstName;
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
	    		vm.pdfPath = info.pdfPath;
	    		vm.enthicity = info.enthicity;
	    		LeadVM lVm = new LeadVM();
	    		lVm.id = info.id.toString();
	    		
	    		//if(!info.comments.equals("null")){
	    			lVm.comments = info.comments;
	    		//}
	    		if(info.year != null){
	    			if(!info.year.equals("null")){
		    			lVm.year = info.year;
		    		}
	    		}
	    		
	    		if(info.make != null){
	    			lVm.make = info.make;
	    		}
	    		if(info.model != null){
	    			lVm.model = info.model;
	    		}
	    		
	    		if(info.exteriorColour != null){
	    			if(!info.exteriorColour.equals("null")){
		    			lVm.exteriorColour = info.exteriorColour;
		    		}
	    		}
	    		
	    		if(info.kilometres != null){
	    			if(!info.kilometres.equals("null")){
		    			lVm.kilometres = info.kilometres;
		    		}
	    		}
	    		
	    		if(info.year != null){
	    			if(!info.engine.equals("null")){
	    				lVm.engine = info.engine;
	    			}
	    		}
	    		if(info.doors != null){
		    		if(!info.doors.equals("null")){
		    			lVm.doors = info.doors;
		    		}
	    		}
	    		if(info.transmission != null){
		    		if(!info.transmission.equals("null")){
		    			lVm.transmission = info.transmission;
		    		}
	    		}
	    		if(info.drivetrain != null){
		    		if(!info.drivetrain.equals("null")){
		    			lVm.drivetrain = info.drivetrain;
		    		}
	    		}
	    		if(info.bodyRating != null){
		    		if(!info.bodyRating.equals("null")){
		    			lVm.bodyRating = info.bodyRating;
		    		}
	    		}
	    		
	    		if(info.tireRating != null){
		    		if(!info.tireRating.equals("null")){
		    			lVm.tireRating = info.tireRating;
		    		}
	    		}
	    		
	    		if(info.engineRating != null){
		    		if(!info.engineRating.equals("null")){
		    			lVm.engineRating = info.engineRating;
		    		}
	    		}
	    		//if(!info.glassRating.equals("null")){
	    			lVm.glassRating = info.glassRating;
	    		//}
	    		//if(!info.interiorRating.equals("null")){
	    			lVm.interiorRating = info.interiorRating;
	    		//}
	    		//if(!info.exhaustRating.equals("null")){
	    			lVm.exhaustRating = info.exhaustRating;
	    		//}
	    		//if(!info.leaseOrRental.equals("null")){
	    			lVm.rentalReturn = info.leaseOrRental;
	    		//}
	    		//if(!info.operationalAndAccurate.equals("null")){
	    			lVm.odometerAccurate = info.operationalAndAccurate;
	    	//	}
	    			lVm.serviceRecords = info.serviceRecord;
	    		//if(!info.lienholder.equals("null")){
	    			lVm.lienholder = info.lienholder;
	    		//}
	    			lVm.prefferedContact = info.preferredContact;
	    		//if(!info.equipment.equals("null")){
	    			lVm.equipment = info.equipment;
	    		//}
	    		//if(!info.accidents.equals("null")){
	    			lVm.accidents = info.accidents;
	    		//}
	    		//if(!info.vehiclenew.equals("null")){
	    			lVm.vehiclenew = info.vehiclenew;
	    		//}
	    		//if(!info.paint.equals("null")){
	    			lVm.paint = info.paint;
	    		//}
	    		//if(!info.salvage.equals("null")){
	    			lVm.salvage = info.salvage;
	    		//}
	    		//if(!info.damage.equals("null")){
	    			lVm.damage = info.damage;
	    		//}
	    			lVm.titleholder = info.holdsThisTitle;
	    			lVm.prefferedContact = info.preferredContact;
	    			
	    			
	    			List<String> sList = new ArrayList<>();
	    			String arr[] =  info.optionValue.split(",");
	    			for(int i=0;i<arr.length;i++){
	    				sList.add(arr[i]);
	    			}
	    			lVm.options = sList;
	    			
	    		vm.leadsValue = lVm;	
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
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
	    		vm.option = 2;
	    		
	    		findTreadParentChildAndBro(infoVMList, info, df, vm);
	    		
	    		/*List<RequestInfoVM> rList2 = new ArrayList<>();
	    		if(info.parentId != null){
	    			TradeIn tIn = TradeIn.findByIdAndParent(info.parentId);
	    			if(tIn != null){
	    				RequestInfoVM rList1 = new RequestInfoVM();
	    				rList1.id = tIn.id;
	    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(tIn.vin);
	    	    		rList1.vin = tIn.vin;
	    	    		if(vehicle1 != null) {
	    	    			rList1.model = vehicle1.model;
	    	    			rList1.make = vehicle1.make;
	    	    			rList1.stock = vehicle1.stock;
	    	    			rList1.year = vehicle1.year;
	    	    			rList1.mileage = vehicle1.mileage;
	    	    			rList1.price = vehicle1.price;
	    	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	    	        		if(vehicleImage!=null) {
	    	        			rList1.imgId = vehicleImage.getId().toString();
	    	        		}
	    	        		else {
	    	        			rList1.imgId = "/assets/images/no-image.jpg";
	    	        		}
	    	    			
	    	    		}
	    	    		rList1.name = tIn.firstName;
	    	    		rList1.phone = tIn.phone;
	    	    		rList1.email = tIn.email;
	    	    		rList1.requestDate = df.format(tIn.tradeDate);
	    	    		rList1.typeOfLead = "Trade-In Appraisal";
	    	    		
	    	    		rList2.add(rList1);
	    			}
	    		}
	    		
	    		List<TradeIn> tIns = TradeIn.findAllByParentID(info.getId());
	    		for(TradeIn info1:tIns){
	    			RequestInfoVM rList1 = new RequestInfoVM();
    				rList1.id = info1.id;
    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    	    		rList1.vin = info1.vin;
    	    		if(vehicle1 != null) {
    	    			rList1.model = vehicle1.model;
    	    			rList1.make = vehicle1.make;
    	    			rList1.stock = vehicle1.stock;
    	    			rList1.year = vehicle1.year;
    	    			rList1.mileage = vehicle1.mileage;
    	    			rList1.price = vehicle1.price;
    	    			rList1.bodyStyle =vehicle1.bodyStyle;
    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
    	        		if(vehicleImage!=null) {
    	        			rList1.imgId = vehicleImage.getId().toString();
    	        		}
    	        		else {
    	        			rList1.imgId = "/assets/images/no-image.jpg";
    	        		}
    	    		}
    	    		rList1.name = info1.firstName;
    	    		rList1.phone = info1.phone;
    	    		rList1.email = info1.email;
    	    		rList1.requestDate = df.format(info1.tradeDate);
    	    		rList1.typeOfLead = "Trade-In Appraisal";
    	    		
    	    		rList2.add(rList1);
	    		}
	    		vm.parentChildLead = rList2;
	    		
	    		infoVMList.add(vm);*/
	    	}
	    	
	    	for(RequestMoreInfo info: requestMoreInfos) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
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
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
	    		vm.option = 1;
	    		
	    		findRequestParentChildAndBro(infoVMList, info, df, vm);
	    		
	    		/*List<RequestInfoVM> rList2 = new ArrayList<>();
	    		if(info.parentId != null){
	    			RequestMoreInfo rMoreInfo = RequestMoreInfo.findByIdAndParent(info.parentId);
	    			if(rMoreInfo != null){
	    				RequestInfoVM rList1 = new RequestInfoVM();
	    				rList1.id = rMoreInfo.id;
	    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(rMoreInfo.vin);
	    	    		rList1.vin = rMoreInfo.vin;
	    	    		if(vehicle1 != null) {
	    	    			rList1.model = vehicle1.model;
	    	    			rList1.make = vehicle1.make;
	    	    			rList1.stock = vehicle1.stock;
	    	    			rList1.year = vehicle1.year;
	    	    			rList1.mileage = vehicle1.mileage;
	    	    			rList1.price = vehicle1.price;
	    	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	    	        		if(vehicleImage!=null) {
	    	        			rList1.imgId = vehicleImage.getId().toString();
	    	        		}
	    	        		else {
	    	        			rList1.imgId = "/assets/images/no-image.jpg";
	    	        		}
	    	    			
	    	    		}
	    	    		rList1.name = rMoreInfo.name;
	    	    		rList1.phone = rMoreInfo.phone;
	    	    		rList1.email = rMoreInfo.email;
	    	    		rList1.requestDate = df.format(rMoreInfo.requestDate);
	    	    		rList1.typeOfLead = "Request More Info";
	    	    		
	    	    		rList2.add(rList1);
	    			}
	    		}
	    		
	    		List<RequestMoreInfo> requestMoreInfo = RequestMoreInfo.findAllByParentID(info.getId());
	    		for(RequestMoreInfo info1:requestMoreInfo){
	    			RequestInfoVM rList1 = new RequestInfoVM();
    				rList1.id = info1.id;
    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    	    		rList1.vin = info1.vin;
    	    		if(vehicle1 != null) {
    	    			rList1.model = vehicle1.model;
    	    			rList1.make = vehicle1.make;
    	    			rList1.stock = vehicle1.stock;
    	    			rList1.year = vehicle1.year;
    	    			rList1.mileage = vehicle1.mileage;
    	    			rList1.price = vehicle1.price;
    	    			rList1.bodyStyle =vehicle1.bodyStyle;
    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
    	        		if(vehicleImage!=null) {
    	        			rList1.imgId = vehicleImage.getId().toString();
    	        		}
    	        		else {
    	        			rList1.imgId = "/assets/images/no-image.jpg";
    	        		}
    	    		}
    	    		rList1.name = info1.name;
    	    		rList1.phone = info1.phone;
    	    		rList1.email = info1.email;
    	    		rList1.requestDate = df.format(info1.requestDate);
    	    		rList1.typeOfLead = "Request More Info";
    	    		
    	    		rList2.add(rList1);
	    		}
	    		vm.parentChildLead = rList2;
	    		infoVMList.add(vm);*/
	    	}
	    	
	    	
	    	/*List<ScheduleTest> listData = ScheduleTest.findAllAssigned(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
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
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		if(info.bestDay != null){
	    			vm.bestDay = df.format(info.bestDay);
	    		}
	    		vm.bestTime = info.bestTime;
	    		vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.typeOfLead = "Schedule Test Drive";
	    		List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
					obj.action = noteObj.action;	    			
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = timedf.format(noteObj.createdTime);
	    			list.add(obj);
	    		}
	    		vm.note = list;
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
	    	}*/
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }
    
    public static Result getAllPremiumIn(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<TradeIn> listData = new ArrayList<>();
	    	List<RequestMoreInfo> lInfos = new ArrayList<>();
	    	List<ScheduleTest> sList = new ArrayList<>();
	    	if(user.role == null || user.role.equals("General Manager")) {
    			listData = TradeIn.findAllData();
    		} else {
    			if(user.role.equals("Manager")) {
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
	    		vm.leadType = "Request More Info";
	    		
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
    
    public static Result getAllTradeIn() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }
    
    
    public static Result getAllTradeInSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<TradeIn> listData = TradeIn.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    	for(TradeIn info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.typeofVehicle=vehicle.typeofVehicle;
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
	    		vm.name = (info.firstName!=null?info.firstName:"")+" "+(info.lastName!=null?info.lastName:"");
	    		vm.phone = info.phone!=null ? info.phone:"";
	    		vm.email = info.email!=null ? info.email:"";
	    		vm.howContactedUs = info.contactedFrom;
	    		vm.howFoundUs = info.hearedFrom;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.pdfPath = info.pdfPath;
	    		vm.requestDate = df.format(info.tradeDate);
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		
	    		LeadVM lVm = new LeadVM();
	    		lVm.id = info.id.toString();
	    		//if(!info.comments.equals("null")){
    			lVm.comments = info.comments;
    		//}
    		if(info.year != null){
    			if(!info.year.equals("null")){
	    			lVm.year = info.year;
	    		}
    		}
    		
    		if(info.make != null){
    			lVm.make = info.make;
    		}
    		if(info.model != null){
    			lVm.model = info.model;
    		}
    		
    		if(info.exteriorColour != null){
    			if(!info.exteriorColour.equals("null")){
	    			lVm.exteriorColour = info.exteriorColour;
	    		}
    		}
    		
    		if(info.kilometres != null){
    			if(!info.kilometres.equals("null")){
	    			lVm.kilometres = info.kilometres;
	    		}
    		}
    		
    		if(info.year != null){
    			if(!info.engine.equals("null")){
    				lVm.engine = info.engine;
    			}
    		}
    		if(info.doors != null){
	    		if(!info.doors.equals("null")){
	    			lVm.doors = info.doors;
	    		}
    		}
    		if(info.transmission != null){
	    		if(!info.transmission.equals("null")){
	    			lVm.transmission = info.transmission;
	    		}
    		}
    		if(info.drivetrain != null){
	    		if(!info.drivetrain.equals("null")){
	    			lVm.drivetrain = info.drivetrain;
	    		}
    		}
    		if(info.bodyRating != null){
	    		if(!info.bodyRating.equals("null")){
	    			lVm.bodyRating = info.bodyRating;
	    		}
    		}
    		
    		if(info.tireRating != null){
	    		if(!info.tireRating.equals("null")){
	    			lVm.tireRating = info.tireRating;
	    		}
    		}
    		
    		if(info.engineRating != null){
	    		if(!info.engineRating.equals("null")){
	    			lVm.engineRating = info.engineRating;
	    		}
    		}
    		//if(!info.glassRating.equals("null")){
    			lVm.glassRating = info.glassRating;
    		//}
    		//if(!info.interiorRating.equals("null")){
    			lVm.interiorRating = info.interiorRating;
    		//}
    		//if(!info.exhaustRating.equals("null")){
    			lVm.exhaustRating = info.exhaustRating;
    		//}
    		//if(!info.leaseOrRental.equals("null")){
    			lVm.rentalReturn = info.leaseOrRental;
    		//}
    		//if(!info.operationalAndAccurate.equals("null")){
    			lVm.odometerAccurate = info.operationalAndAccurate;
    	//	}
    			lVm.serviceRecords = info.serviceRecord;
    		//if(!info.lienholder.equals("null")){
    			lVm.lienholder = info.lienholder;
    		//}
    			lVm.prefferedContact = info.preferredContact;
    		//if(!info.equipment.equals("null")){
    			lVm.equipment = info.equipment;
    		//}
    		//if(!info.accidents.equals("null")){
    			lVm.accidents = info.accidents;
    		//}
    		//if(!info.vehiclenew.equals("null")){
    			lVm.vehiclenew = info.vehiclenew;
    		//}
    		//if(!info.paint.equals("null")){
    			lVm.paint = info.paint;
    		//}
    		//if(!info.salvage.equals("null")){
    			lVm.salvage = info.salvage;
    		//}
    		//if(!info.damage.equals("null")){
    			lVm.damage = info.damage;
    		//}
	    			lVm.titleholder = info.holdsThisTitle;
	    			lVm.prefferedContact = info.preferredContact;
    			List<String> sList = new ArrayList<>();
    			String arr[] =  info.optionValue.split(",");
    			for(int i=0;i<arr.length;i++){
    				sList.add(arr[i]);
    			}
    			lVm.options = sList;
    			
    		vm.leadsValue = lVm;
	    		
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
	    		Integer nFlag = 0;
	    		List<NoteVM> list = new ArrayList<>();
	    		for(UserNotes noteObj :notesList) {
	    			NoteVM obj = new NoteVM();
	    			obj.id = noteObj.id;
	    			obj.note = noteObj.note;
	    			obj.action = noteObj.action;
	    			obj.date = df.format(noteObj.createdDate);
	    			obj.time = time.format(noteObj.createdTime);
	    			if(noteObj.saveHistory != null){
	    				if(noteObj.saveHistory.equals(1)){
	        				nFlag = 1;
	        			}
	    			}
	    			list.add(obj);
	    		}
	    		vm.note = list;
	    		vm.noteFlag = nFlag;
	    		vm.requestDate = df.format(info.tradeDate);
	    		if(info.isRead == 0) {
	    			vm.isRead = false;
	    		}
	    		
	    		if(info.isRead == 1) {
	    			vm.isRead = true;
	    		}
	    		
	    		findTreadParentChildAndBro(infoVMList, info, df, vm);
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }
    
    public static Result getInfoCount() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
	    	InfoCountVM vm = new InfoCountVM();
	    	vm.schedule = ScheduleTest.findAll(Long.valueOf(session("USER_LOCATION")));
	    	vm.trade = TradeIn.findAll(Long.valueOf(session("USER_LOCATION")));
	    	vm.req = RequestMoreInfo.findAll(Long.valueOf(session("USER_LOCATION")));
	    	if(user.location != null){
	    		Location location=Location.findById(user.location.id);
		    	vm.locationName=location.name;
	    	}
	    	
	    	List<ScheduleTest> sched = ScheduleTest.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	    	List<RequestMoreInfo> reInfos = RequestMoreInfo.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	    	List<TradeIn> tradeIns = TradeIn.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));

	    	int premi = sched.size() + reInfos.size() + tradeIns.size();
	    	vm.premium = premi;
	    	
	    	vm.userType = user.role;
	    	
	    	return ok(Json.toJson(vm));
    	}
    }
    
    public static Result  getLocationName(Long locationId)     
    {
    	Location location=Location.findById(locationId);
		return ok(location.getName());
    	
    }
    
    public static Result requestInfoMarkRead(String flag,Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    
    public static Result scheduleTestMarkRead(String flag,Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
			todo.task = "Confirm Schedule Test Drive for "+vobj.make+" "+vobj.model+" ("+vobj.vin+")";
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
    
    
    public static Result tradeInMarkRead(String flag,Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    
    public static Result uploadLogoFile() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	
	    	FilePart picture = body.getFile("file0");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"logo");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"logo"+File.separator+fileName;
	    	    File file = picture.getFile();
	    	    try {
	    	    		FileUtils.moveFile(file, new File(filePath));
	    	    		
	    	    		SiteLogo logoObj = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	    		
	    	    		if(logoObj == null) {
		    	    		SiteLogo logo = new SiteLogo();
		    	    		logo.logoImagePath = "/"+session("USER_LOCATION")+"/"+"logo"+"/"+fileName;
		    	    		logo.logoImageName = fileName;
		    	    		logo.user = userObj;
							logo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	    		logo.save();
	    	    		} else {
	    	    			File logoFile = new File(rootDir+File.separator+logoObj.locations.id+File.separator+"logo"+File.separator+logoObj.logoImageName);
	    	    			logoFile.delete();
	    	    			logoObj.setLogoImageName(fileName);
	    	    			logoObj.setLogoImagePath("/"+session("USER_LOCATION")+"/"+"logo"+"/"+fileName);
	    	    			logoObj.update();
	    	    		}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	return ok();
    	}	
    }
    
    
    public static Result uploadFeviconFile() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	
	    	FilePart picture = body.getFile("file0");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"fevicon");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"fevicon"+File.separator+fileName;
	    	    File file = picture.getFile();
	    	    try {
	    	    		FileUtils.moveFile(file, new File(filePath));
	    	    		
	    	    		//SiteLogo logoObj = SiteLogo.findByUser(userObj);
	    	    		SiteLogo logoObj = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	    		
	    	    		if(logoObj == null) {
		    	    		SiteLogo logo = new SiteLogo();
		    	    		logo.faviconImagePath = "/"+session("USER_LOCATION")+"/"+"fevicon"+"/"+fileName;
		    	    		logo.faviconImageName = fileName;
		    	    		logo.user = userObj;
							logo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	    		logo.save();
	    	    		} else {
	    	    			File feviconFile = new File(rootDir+File.separator+logoObj.locations.id+File.separator+"fevicon"+File.separator+logoObj.faviconImageName);
	    	    			feviconFile.delete();
	    	    			logoObj.setFaviconImageName(fileName);
	    	    			logoObj.setFaviconImagePath("/"+session("USER_LOCATION")+"/"+"fevicon"+"/"+fileName);
	    	    			logoObj.update();
	    	    		}
	    	  } catch (FileNotFoundException e) {
	  			e.printStackTrace();
		  		} catch (IOException e) {
		  			e.printStackTrace();
		  		} 
	    	  } 
	    	return ok();
    	}	
    }
    
    
    public static Result saveSiteTabText(String text) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	SiteLogo logoObj = SiteLogo.findByUser(userObj);
	    	if(logoObj == null) {
	    		SiteLogo logo = new SiteLogo();
	    		logo.tabText = text;
	    		logo.user = userObj;
				logo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		logo.save();
	    	} else {
	    		logoObj.setTabText(text);
	    		logoObj.update();
	    	}
	    	return ok();
    	}	
    }
    
    
    public static Result getLogoData() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	SiteLogo logoObj = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	SiteLogoVM vm = new SiteLogoVM();
	    	if(logoObj != null) {
	    		vm.logoName = logoObj.logoImageName;
	    		vm.feviconName = logoObj.faviconImageName;
	    		vm.tabText = logoObj.tabText;
	    	}
	    	
	    	return ok(Json.toJson(vm));
    	}	
    }
    
    
    public static Result getTradeInDataById(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	TradeIn tradeIn = TradeIn.findById(id);
	    	TradeInVM vm = new TradeInVM();
	    	vm.firstName = tradeIn.firstName;
	    	vm.lastName = tradeIn.lastName;
	    	vm.workPhone = tradeIn.workPhone;
	    	vm.phone = tradeIn.phone;
	    	vm.email = tradeIn.email;
	    	vm.preferredContact = tradeIn.preferredContact;
	    	vm.comments = tradeIn.comments;
	    	vm.year = tradeIn.year;
	    	vm.make = tradeIn.make;
	    	vm.model = tradeIn.model;
	    	vm.exteriorColour = tradeIn.exteriorColour;
	    	vm.kilometres = tradeIn.kilometres;
	    	vm.engine = tradeIn.engine;
	    	vm.doors = tradeIn.doors;
	    	vm.transmission = tradeIn.transmission;
	    	vm.drivetrain = tradeIn.drivetrain;
	    	vm.bodyRating = tradeIn.bodyRating;
	    	vm.tireRating = tradeIn.tireRating;
	    	vm.engineRating = tradeIn.engineRating;
	    	vm.transmissionRating = tradeIn.transmissionRating;
	    	vm.glassRating = tradeIn.glassRating;
	    	vm.interiorRating = tradeIn.interiorRating;
	    	vm.exhaustRating = tradeIn.exhaustRating;
	    	vm.leaseOrRental = tradeIn.leaseOrRental;
	    	vm.operationalAndAccurate = tradeIn.operationalAndAccurate;
	    	vm.serviceRecord = tradeIn.serviceRecord;
	    	vm.lienHolder = tradeIn.lienholder;
	    	vm.holdsThisTitle = tradeIn.holdsThisTitle;
	    	vm.equipment = tradeIn.equipment;
	    	vm.vehiclenew = tradeIn.vehiclenew;
	    	vm.accidents = tradeIn.accidents;
	    	vm.damage = tradeIn.damage;
	    	vm.paint = tradeIn.paint;
	    	vm.salvage = tradeIn.salvage;
	    	vm.optionValue = tradeIn.optionValue;
	    	vm.vin = tradeIn.vin;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
    
    public static Result saveBlog() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	Form<BlogVM> form = DynamicForm.form(BlogVM.class).bindFromRequest();
	    	BlogVM vm = form.get();
	    	Blog blogObj = Blog.findByUserAndTitle(userObj, vm.title);
	    	Blog blog = new Blog();
	    	if(blogObj == null) {
		    	blog.title = vm.title;
		    	blog.description = vm.description;
		    	blog.videoUrl = vm.videoUrl;
		    	blog.postedBy = vm.postedBy;
		    	blog.postedDate = new Date();
		    	blog.user = userObj;
				blog.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	blog.save();
		    	
		    	MultipartFormData body = request().body().asMultipartFormData();
		    	
		    	FilePart picture = body.getFile("file0");
		    	  if (picture != null) {
		    	    String fileName = picture.getFilename();
		    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blog.getId());
		    	    if(!fdir.exists()) {
		    	    	fdir.mkdir();
		    	    }
		    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blog.getId()+File.separator+fileName;
		    	    File file = picture.getFile();
		    	    try {
		    	    		FileUtils.moveFile(file, new File(filePath));
		    	    		blog.setImageUrl(File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blog.getId()+File.separator+fileName);
		    	    		blog.setImageName(fileName);
		    	    		blog.update();
		    	  } catch (FileNotFoundException e) {
		  			e.printStackTrace();
			  		} catch (IOException e) {
			  			e.printStackTrace();
			  		} 
		    	  }
	    	} else {
	    		return ok(blogObj.imageUrl);
	    	}
	    	
	    	return ok(blog.imageUrl);
    	}	
    }
    
    
    public static Result getAllBlogs() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	List<Blog> blogList = Blog.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	//List<Blog> blogList = Blog.findByUser(userObj);
	    	List<BlogVM> vmList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	int num = 0;
	    	for(Blog blog: blogList) {
	    		num++;
	    		BlogVM vm = new BlogVM();
	    		vm.id = blog.id;
	    		vm.title = blog.title;
	    		vm.postedBy = blog.postedBy;
	    		vm.postedDate = df.format(blog.postedDate);
	    		vm.number = num;
	    		vmList.add(vm);
	    	}
	    	
	    	return ok(Json.toJson(vmList));
    	}	
    }
    
    
    public static Result deleteBlog(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	Blog blog = Blog.findById(id);
	    	blog.delete();
	    	return ok();
    	}
    }
    
    
    public static Result getBlogById(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	BlogVM vm = new BlogVM();
	    	Blog blog = Blog.findById(id);
	    	vm.id = blog.id;
			vm.title = blog.title;
			vm.postedBy = blog.postedBy;
			vm.description = blog.description;
			vm.imageUrl = blog.imageUrl;
			vm.videoUrl = blog.videoUrl;
	    	return ok(Json.toJson(vm));
    	}
    }
    
    
    public static Result updateBlog() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser userObj = (AuthUser) getLocalUser();
	    	Form<BlogVM> form = DynamicForm.form(BlogVM.class).bindFromRequest();
	    	BlogVM vm = form.get();
	    	Blog blogObj = Blog.findById(vm.id);
	    	if(blogObj != null) {
	    		blogObj.setTitle(vm.title);
	    		blogObj.setDescription(vm.description);
	    		blogObj.setVideoUrl(vm.videoUrl);
	    		blogObj.setPostedBy(vm.postedBy);
	    		blogObj.update();
	    		
	    		MultipartFormData body = request().body().asMultipartFormData();
		    	if(body != null) {
			    	FilePart picture = body.getFile("file0");
			    	  if (picture != null) {
			    		File oldfdir = new File(rootDir+blogObj.getImageUrl());  
			    		oldfdir.delete();
			    	    String fileName = picture.getFilename();
			    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blogObj.getId());
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blogObj.getId()+File.separator+fileName;
			    	    File file = picture.getFile();
			    	    try {
			    	    		FileUtils.moveFile(file, new File(filePath));
			    	    		blogObj.setImageUrl(File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"blogImages"+File.separator+blogObj.getId()+File.separator+fileName);
			    	    		blogObj.setImageName(fileName);
			    	    		blogObj.update();
			    	  } catch (FileNotFoundException e) {
			  			e.printStackTrace();
				  		} catch (IOException e) {
				  			e.printStackTrace();
				  		} 
			    	  }
		    	}
	    	}
	    	return ok(blogObj.imageUrl);
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
  
    public static Result showPdf(Long id) {
    	TradeIn tradeIn = TradeIn.findById(id);
    	File file = new File(rootDir+tradeIn.pdfPath);
    	response().setContentType("application/pdf");
    	response().setHeader("Content-Disposition", "inline; filename=tradeIn.pdf");
		return ok(file);
    }
    
    public static Result getMyProfile() {
    	AuthUser userObj = (AuthUser) getLocalUser();
    	//MyProfile mpObj = MyProfile.findByUser(userObj);
    	List<MyProfile> mpObj = MyProfile.findByLocation(Long.valueOf(session("USER_LOCATION")));
    	profileVM vm = new profileVM();
    	if(mpObj.size() != 0){
    		
    	
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
    
    
    public static Result forgotPass(){
    	String email = Form.form().bindFromRequest().get("email");
    	AuthUser user = AuthUser.find.where().eq("email", email).findUnique();
    	if(user != null) {
			
    	
	        
	 		Properties props = new Properties();
	 		props.put("mail.smtp.auth", "true");
	 		props.put("mail.smtp.starttls.enable", "true");
	 		props.put("mail.smtp.host", "smtp.gmail.com");
	 		props.put("mail.smtp.port", "587");
	  
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
	  			InternetAddress.parse(user.email));
	  			feedback.setSubject("Your forgot password ");	  			
	  			 BodyPart messageBodyPart = new MimeBodyPart();	  	       
	  	         messageBodyPart.setText("Your forget password is "+user.password);	 	    
	  	         Multipart multipart = new MimeMultipart();	  	    
	  	         multipart.addBodyPart(messageBodyPart);	            
	  	         feedback.setContent(multipart);
	  		     Transport.send(feedback);
	       		} catch (MessagingException e) {
	  			  throw new RuntimeException(e);
	  		}
			return ok("success");
		} else {
			return ok("error");
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
    	return ok(msg);
    }
    
    private static void makeToDo(String vin) {
    	AuthUser user = (AuthUser) getLocalUser();
    	ToDo todo = new ToDo();
		Vehicle vobj = Vehicle.findByVinAndStatus(vin);
		if(vobj != null){
			todo.task = "Confirm Schedule Test Drive for "+vobj.make+" "+vobj.model+" ("+vobj.vin+")";
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
		}
		
    }
    
    private static void sendMailpremium() {
    	/*--------------------------------*/
		
		AuthUser aUser = AuthUser.getlocationAndManagerOne(Location.findById(Long.valueOf(session("USER_LOCATION"))));
		final String username = emailUsername;
		final String password = emailPassword;
		
		Properties props = new Properties();  
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");
		     
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		  
		    try {  
		     MimeMessage message = new MimeMessage(session);  
		     message.setFrom(new InternetAddress(username));  
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(aUser.communicationemail));  
		     message.setSubject("Premium Leads");  
		     message.setText("Premium Request has been submitted");  
		       
		     Transport.send(message);  
		  
		     System.out.println("message sent successfully...");  
		   
		     } catch (MessagingException e) {e.printStackTrace();} 
		
		
		/*------------------------------------*/
    }
    
    private static void sendMail(Map map) {
    	
    	AuthUser logoUser = getLocalUser();
    //AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
    	SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); // findByUser(logoUser);
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
					InternetAddress.parse(map.get("email").toString()));
			message.setSubject("TEST DRIVE CONFIRMATION");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/confirmationTemplate.vm"); 
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
	        context.put("mileage", vehicle.mileage);
	        context.put("name", map.get("uname"));
	        context.put("email", map.get("uemail"));
	        context.put("phone",  map.get("uphone"));
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
    
    public static Result saveUser() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
    		AuthUser users = (AuthUser) getLocalUser();
    		MultipartFormData body = request().body().asMultipartFormData();
    		
    		
	    	AuthUser userObj = new AuthUser();
	    	UserVM vm = form.get();
	    	//List<String> aa= new ArrayList<>();
	    	
	    	userObj.firstName = vm.firstName;
	    	userObj.lastName = vm.lastName;
	    	userObj.email = vm.email;
	    	userObj.account = "active";
	    	userObj.communicationemail = vm.email;
	    	userObj.phone = vm.phone;
	    	userObj.role = vm.userType;
	    	userObj.location = Location.findById(Long.parseLong(session("USER_LOCATION")));
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
	    	userObj.imageUrl = vm.imageUrl;
	    	String arr2[] = null;
	    	 if(body != null) {
	    		 String abcd= vm.permissions.get(0);
	 	    	abcd = abcd.replace("]", "");
	 	    	abcd = abcd.replace("[", "");
	 	    	abcd = abcd.replace("\"", "");
	 	    	arr2 = abcd.split(",");
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
	    			   if(obj.name.equals("CRM") || obj.name.equals("My Profile") || obj.name.equals("Dashboard") || obj.name.equals("Show Location")) {
	    				   permissionData.add(obj);
	    			   }
	    		   }
	    		   userObj.permission = permissionData;
	    	   }
	    	   
	    	   
	    	   
	    	   if(vm.userType.equals("Sales Person")) {
	    		   
	    		  // String aa = vm.permissions.get(0);
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
	    			   
	    			   /*if(!obj.name.equals("Home Page Editing") && !obj.name.equals("Blogs") && !obj.name.equals("My Profile") && !obj.name.equals("Account Settings")) {
	    				   permissionData.add(obj);
	    			   }*/
	    			   
	    		   }
	    		   userObj.setPermission(permissionData);
	    	   }
	    	   userObj.save();
	    	   
	    	  
	    	   if(body != null) {
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
		    	    		user.setImageUrl("/"+session("USER_LOCATION")+"/"+user.id+"/"+"userPhoto"+"/"+fileName);
		    	    		user.setImageName(fileName);
		    	    		user.update();	
		    	    		
		    	  } catch (FileNotFoundException e) {
		  			e.printStackTrace();
			  		} catch (IOException e) {
			  			e.printStackTrace();
			  		} 
		    	  } 
	    	   } 
	    	   AuthUser logoUser = AuthUser.findById(userObj.id);//Integer.getInteger(session("USER_KEY")));
	    	   SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));  //findByUser(logoUser);
	    		Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", "smtp.gmail.com");
		 		props.put("mail.smtp.port", "587");
		  
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUsername, emailPassword);
		 			}
		 		  });
		  
		 		try{
		 		   
		  			Message message = new MimeMessage(session);
		  			message.setFrom(new InternetAddress("glider.autos@gmail.com"));
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
    
    /*public static Result getUserLocationByDateInfo(String startDate,String endDate,String locOrPer){
    	
    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
    	Date dateobj = new Date();
    	Calendar cal1 = Calendar.getInstance();
    	AuthUser users = (AuthUser) getLocalUser();
    	String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
		        "August", "September", "October", "November", "December" };
    	
    	Map<String, Integer> mapCar = new HashMap<String, Integer>();
    	
    	Date startD = null;
		try {
			startD = df1.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Date endD = null;
		try {
			endD = df1.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	int requestLeadCount = 0;
    	int scheduleLeadCount = 0;
    	int tradeInLeadCount = 0;
    	
    	int requestLeadCount1 = 0;
    	int scheduleLeadCount1 = 0;
    	int tradeInLeadCount1 = 0;
    	
    	List<RequestMoreInfo> rInfo = null;
    	List<ScheduleTest> sList = null;
    	List<TradeIn> tradeIns = null;
    	List<RequestMoreInfo> rInfoAll = null;
    	List<ScheduleTest> sListAll = null;
    	List<TradeIn> tradeInsAll = null;
    	
    	if(users.role.equals("Manager") && locOrPer.equals("location")){
    		rInfo = RequestMoreInfo.findAllSeenLocationSch(Long.parseLong(session("USER_LOCATION")));
    		sList = ScheduleTest.findAllAssignedLocation(Long.parseLong(session("USER_LOCATION")));
    		tradeIns = TradeIn.findAllSeenLocationSch(Long.parseLong(session("USER_LOCATION")));
    		
    		rInfoAll = RequestMoreInfo.findByLocation(Long.parseLong(session("USER_LOCATION")));
    		sListAll = ScheduleTest.findByLocation(Long.parseLong(session("USER_LOCATION")));
    		tradeInsAll = TradeIn.findByLocation(Long.parseLong(session("USER_LOCATION")));
    		
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		rInfo = RequestMoreInfo.findAllSeenSch(users);
    		sList = ScheduleTest.findAllAssigned(users);
    		tradeIns = TradeIn.findAllSeenSch(users);
    		
    		rInfoAll = RequestMoreInfo.findAllByUser(users);
    		sListAll = ScheduleTest.findAllByUser(users);
    		tradeInsAll = TradeIn.findAllByUser(users);
    	}
    	
    	for(RequestMoreInfo rMoreInfo:rInfo){
    		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
    			requestLeadCount++;
    		}
    	}
    	
    	
    	for(ScheduleTest sTest:sList){
    		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
    			scheduleLeadCount++;
    		}
    	}

    	for(TradeIn tIn:tradeIns){
    		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
    				tradeInLeadCount++;
    		}
    	}
    	
    	for(RequestMoreInfo rMoreInfo:rInfoAll){
    		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
    			requestLeadCount1++;
    		}
    	}
    	
    	
    	for(ScheduleTest sTest:sListAll){
    	if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
    			scheduleLeadCount1++;
    	}
    	}

    	for(TradeIn tIn:tradeInsAll){
    	if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
				tradeInLeadCount1++;
    		}
    	}
    	
    	
    	int countLeads1 = requestLeadCount1 + scheduleLeadCount1 + tradeInLeadCount1;
    	int countLeads = requestLeadCount + scheduleLeadCount + tradeInLeadCount;
    	Location location = Location.findById(Long.parseLong(session("USER_LOCATION")));
    	LocationWiseDataVM lDataVM = new LocationWiseDataVM();
    	lDataVM.imageUrl = location.getImageUrl();
    	//List<AuthUser> uAuthUser = AuthUser.getlocationAndRoleByType(location, "Sales Person");
    	lDataVM.countSalePerson = countLeads;
    	
    	Integer pricecount = 0;
    	int saleCarCount = 0;
    	if(users.role.equals("Manager") && locOrPer.equals("location")){
    		List<Vehicle> vList = Vehicle.findByLocationAndSold(location.id);
        	double sucessCount= (double)vList.size()/(double)countLeads1*100;
        	lDataVM.successRate = (int) sucessCount;
        	
        	for(Vehicle vehList:vList){
        		//	if(vehList.soldDate.after(timeBack)) {
        		if(vehList.soldDate.after(startD) && vehList.soldDate.before(endD)){
            			saleCarCount++;
            			pricecount = pricecount + vehList.price;
            		}
        	}
        	
        	double sucessCount= (double)saleCarCount/(double)countLeads1*100;
        	lDataVM.successRate = (int) sucessCount;
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		
    		List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(users);
    		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(users);
    		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(users);
    		
    	//	saleCarCount = rInfo1.size() + sList1.size() + tradeIns1.size();
    		
    		for(RequestMoreInfo rMoreInfo: rInfo1){
    			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(rMoreInfo.vin,users);
    			for(Vehicle vehicle:vehicleVin){
    			if(vehicle != null){
    				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
            			saleCarCount++;
            			pricecount = pricecount + vehicle.price;
    				}
    			}
    		 }
    		}
    		
    		for(ScheduleTest sTest: sList1){
    			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(sTest.vin,users);
    			for(Vehicle vehicle:vehicleVin){
    			if(vehicle != null){
    				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
            			saleCarCount++;
            			pricecount = pricecount + vehicle.price;
    				}
    			}
    		}
    		}
    		
    		for(TradeIn tradeIn: tradeIns1){
    			List<Vehicle> vehicleVin = Vehicle.findByVidAndUser(tradeIn.vin);
    			for(Vehicle vehicle:vehicleVin){
    			if(vehicle != null){
    				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
            			saleCarCount++;
            			pricecount = pricecount + vehicle.price;
    				}
    			}
    		}
    		}
    		
    		double sucessCount= (double)saleCarCount/(double)countLeads1*100;
    		lDataVM.successRate = (int) sucessCount;
    		
    	}
    	
    	lDataVM.totalSalePrice = pricecount;
    	lDataVM.totalsaleCar = saleCarCount;
    	
    	double valAvlPrice= ((double)pricecount/(double)saleCarCount);
    	lDataVM.angSalePrice = (int) valAvlPrice;
    	
    	Calendar cal = Calendar.getInstance();  
    	String monthCal = monthName[cal.get(Calendar.MONTH)];
    	PlanScheduleMonthlyLocation pMonthlyLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(Long.parseLong(session("USER_LOCATION"))), monthCal);
    	if(pMonthlyLocation != null){
    		double val= ((double)pricecount/Double.parseDouble(pMonthlyLocation.totalEarning));
        	lDataVM.AngSale = (int) (val*100);
    	}
    	
    	
    	
    	List<Vehicle> allVehiList = Vehicle.findByLocation(location.id);
    	int saleCar = 0;
    	int newCar = 0;
    	for(Vehicle vehicle:allVehiList){
    		if(vehicle.status.equals("Sold")){
    			if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
    				saleCar++;
    			}
    		}//else if(vehicle.status.equals("Newly Arrived")){
    				newCar++;
    		//}
    	}
    	
    	double val= ((double)saleCar/(double)newCar);
    	lDataVM.AngSale = (int) (val*100);
        
    	List<LeadsDateWise> lDateWises = LeadsDateWise.findByLocation(Location.findById(Long.parseLong(session("USER_LOCATION")))); 
    	for(LeadsDateWise lWise:lDateWises){
    		
    		if(lWise.goalSetTime.equals("1 week")){
    			cal.setTime(lWise.leadsDate);  
    			cal.add(Calendar.DATE, 7);
    			Date m =  cal.getTime(); 
    			if(m.after(dateobj)) {
    				lDataVM.leads = lWise.leads;
    				lDataVM.goalTime =lWise.goalSetTime;
    				
    			}
    			
    		}else if(lWise.goalSetTime.equals("1 month")){
    			cal.setTime(lWise.leadsDate);  
    			cal.add(Calendar.DATE, 30);
    			Date m =  cal.getTime(); 
    			if(m.after(dateobj)) {
    				lDataVM.leads = lWise.leads;
    				lDataVM.goalTime =lWise.goalSetTime;
    			}
    		}
    		
    	}
    	
    	List<DateAndValueVM> sAndValues = new ArrayList<>();
    	List<Long> sAndLong = new ArrayList<>();
    	int countPlanCarSold = 0;
    	
    	
    	
		Calendar now = Calendar.getInstance();
		String month = monthName[now.get(Calendar.MONTH)];
		lDataVM.monthCurr = month;
    	PlanScheduleMonthlyLocation pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(Long.parseLong(session("USER_LOCATION"))), month);
    	if(pLocation != null){
    		List<Integer> longV = new ArrayList<>();
			DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "Plan";
			longV.add(Integer.parseInt(pLocation.totalEarning));
			sValue.data = longV;
			sAndValues.add(sValue);
    	}
    	
    	
    	int countPlanTotalErForLocation = 0;
    	DateFormat inputDF  = new SimpleDateFormat("MMMM");
    	List<Vehicle> vehicles = Vehicle.findByLocationAndSold(Long.parseLong(session("USER_LOCATION")));
		for(Vehicle veh1:vehicles){
			//countPlanTotalErForLocation = countPlanTotalErForLocation + veh1.price;
			String dateValue = inputDF.format(veh1.soldDate);
			if(month.equals(dateValue)){
				countPlanCarSold = countPlanCarSold + veh1.price;
			}
			
		}
    	
    	
    	if(countPlanCarSold > 0){
    		List<Integer> longV = new ArrayList<>();
    		DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "Record";
			longV.add(countPlanCarSold);
			sValue.data = longV;
			sAndValues.add(sValue);
    	}
    	
    	List<Vehicle> vList2 = Vehicle.findByLocationAndSold(Long.parseLong(session("USER_LOCATION")));
    	
    	if(vList2.size() != 0){
    		List<Integer> longV = new ArrayList<>();
    		DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "You";
			longV.add(countPlanCarSold);
			sValue.data = longV;
			sAndValues.add(sValue);
    	}
    	
    	lDataVM.sendData = sAndValues;

    	return ok(Json.toJson(lDataVM));
    	
    	
    }*/
    
  /* public static Result getUserLocationByDateInfoOther(Integer userKey,String startDate,String endDate,String locOrPer,Long locationId,Integer managerId){
	   AuthUser users = AuthUser.findById(managerId);
		LocationWiseDataVM lDataVM = new LocationWiseDataVM();
	   mystatisticsDateWise(startDate,endDate,"location",locationId,users,lDataVM);
		return ok(Json.toJson(lDataVM));
   }*/
   
   public static Result getUserLocationByDateInfo(Integer userKey,String startDate,String endDate,String locOrPer){
		AuthUser users = AuthUser.findById(userKey);
		LocationWiseDataVM lDataVM = new LocationWiseDataVM();
		/*long locationId = 0;
    	if(session("USER_LOCATION") == null){
    		locationId = 0L;
    	}else{
    		locationId = Long.valueOf(session("USER_LOCATION"));
    	}*/
	   mystatisticsDateWise(startDate,endDate,locOrPer,users.location.id,users,lDataVM);
   		return ok(Json.toJson(lDataVM));
   }
   
   public static void mystatisticsDateWise(String startDate,String endDate,String locOrPer,Long locationId,AuthUser users,LocationWiseDataVM lDataVM){
	   DateFormat onlyMonth = new SimpleDateFormat("MMMM");
   	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
   	DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
   	Date dateobj = new Date();
   	Calendar cal1 = Calendar.getInstance();
   
   	String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
		        "August", "September", "October", "November", "December" };
   	
   	Calendar cal = Calendar.getInstance();  
    	String monthCal = monthName[cal.get(Calendar.MONTH)];
   	
   	Map<String, Integer> mapCar = new HashMap<String, Integer>();
   	
   	Date startD = null;
		try {
			startD = df1.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	Date endD = null;
		try {
			endD = df1.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	
   	int requestLeadCount = 0;
   	int scheduleLeadCount = 0;
   	int tradeInLeadCount = 0;
   	
   	int requestLeadCount1 = 0;
   	int scheduleLeadCount1 = 0;
   	int tradeInLeadCount1 = 0;
   	
   	List<RequestMoreInfo> rInfo = null;
   	List<ScheduleTest> sList = null;
   	List<TradeIn> tradeIns = null;
   	List<RequestMoreInfo> rInfoAll = null;
   	List<ScheduleTest> sListAll = null;
   	List<TradeIn> tradeInsAll = null;
   	
   	if(users.role.equals("Manager") && locOrPer.equals("location")){
   		rInfo = RequestMoreInfo.findAllSeenLocationSch(locationId);
   		sList = ScheduleTest.findAllAssignedLocation(locationId);
   		tradeIns = TradeIn.findAllSeenLocationSch(locationId);
   		
   		rInfoAll = RequestMoreInfo.findByLocationNotCancel(locationId);
   		sListAll = ScheduleTest.findByLocationNotCancel(locationId);
   		tradeInsAll = TradeIn.findByLocationNotCancel(locationId);
   		
   	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
   		rInfo = RequestMoreInfo.findAllSeenSch(users);
   		sList = ScheduleTest.findAllAssigned(users);
   		tradeIns = TradeIn.findAllSeenSch(users);
   		
   		rInfoAll = RequestMoreInfo.findByAssigUserNotCancel(users);
   		sListAll = ScheduleTest.findByAssigUserNotCancel(users);
   		tradeInsAll = TradeIn.findByAssigUserNotCancel(users);
   	}
   	
   	for(RequestMoreInfo rMoreInfo:rInfo){
   		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
   			requestLeadCount++;
   		}
   	}
   	
   	
   	for(ScheduleTest sTest:sList){
   		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
   			scheduleLeadCount++;
   		}
   	}

   	for(TradeIn tIn:tradeIns){
   		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
   				tradeInLeadCount++;
   		}
   	}
   	
   	for(RequestMoreInfo rMoreInfo:rInfoAll){
   		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
   			requestLeadCount1++;
   		}
   	}
   	
   	
   	for(ScheduleTest sTest:sListAll){
   	if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
   			scheduleLeadCount1++;
   	}
   	}

   	for(TradeIn tIn:tradeInsAll){
   	if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
				tradeInLeadCount1++;
   		}
   	}
   	
   	
   	int countLeads1 = requestLeadCount1 + scheduleLeadCount1 + tradeInLeadCount1;
   	int countLeads = requestLeadCount + scheduleLeadCount + tradeInLeadCount;
   	Location location = Location.findById(locationId);
   
   	if(locOrPer.equals("location")){
   		lDataVM.imageUrl = location.getImageUrl();
   	}else if(locOrPer.equals("person")){
   		lDataVM.imageUrl = users.imageUrl;
   	}
   	
   	//List<AuthUser> uAuthUser = AuthUser.getlocationAndRoleByType(location, "Sales Person");
   	lDataVM.countSalePerson = countLeads;
   	
   	Integer monthPriceCount = 0;
   	Integer pricecount = 0;
   	int saleCarCount = 0;
   	if(users.role.equals("Manager") && locOrPer.equals("location")){
   		List<Vehicle> vList = Vehicle.findByLocationAndSold(location.id);
       	
       	
       	for(Vehicle vehList:vList){
       		//	if(vehList.soldDate.after(timeBack)) {
       		if(vehList.soldDate.after(startD) && vehList.soldDate.before(endD)){
           			saleCarCount++;
           			pricecount = pricecount + vehList.price;
           		}
       		if(monthCal.equals(onlyMonth.format(vehList.soldDate))){
       			monthPriceCount = monthPriceCount + vehList.price;
       		}
       	}
       	
       	double sucessCount= (double)saleCarCount/(double)countLeads1*100;
       	lDataVM.successRate = (int) sucessCount;
   	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
   		
   		List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(users);
   		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(users);
   		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(users);
   		
   		List<Vehicle> vList = Vehicle.findBySoldUserAndSold(users);
   		for (Vehicle vehicle : vList) {
   			if(vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) {
       			saleCarCount++;
       			pricecount = pricecount + vehicle.price;
				}
				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
					monthPriceCount = monthPriceCount + vehicle.price;
				}
			}
   		
   		double sucessCount= (double)saleCarCount/(double)countLeads1*100;
   		lDataVM.successRate = (int) sucessCount;
   		
   	}
   	
   	lDataVM.totalSalePrice =  pricecount;
   	lDataVM.totalsaleCar = saleCarCount;
   	
   	double valAvlPrice= ((double)pricecount/(double)saleCarCount);
   	lDataVM.angSalePrice = (int) valAvlPrice;
   	
   	
     	if(users.role.equals("Manager") && locOrPer.equals("location")){
   	
     		PlanScheduleMonthlyLocation pMonthlyLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(locationId), monthCal);
       	if(pMonthlyLocation != null){
       		double val= ((double)pricecount/Double.parseDouble(pMonthlyLocation.totalEarning));
           	lDataVM.AngSale = (int) (val*100);
       	}
       	
   	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
   		PlanScheduleMonthlySalepeople  pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(users, monthCal); 
       	if(pMonthlySalepeople != null){
       		double val= ((double)pricecount/Double.parseDouble(pMonthlySalepeople.totalBrought));
           	lDataVM.AngSale = (int) (val*100);
       	}
   	}
   	
   	
   	
   	
   	List<Vehicle> allVehiList = Vehicle.findByLocation(location.id);
   	int saleCar = 0;
   	int newCar = 0;
   	for(Vehicle vehicle:allVehiList){
   		if(vehicle.status.equals("Sold")){
   			if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
   				saleCar++;
   			}
   		}//else if(vehicle.status.equals("Newly Arrived")){
   				newCar++;
   		//}
   	}
   	
       
   	//List<LeadsDateWise> lDateWises = LeadsDateWise.findByLocation(Location.findById(Long.parseLong(session("USER_LOCATION"))));
   	List<LeadsDateWise> lDateWises = LeadsDateWise.getAllVehicles(users);
   	for(LeadsDateWise lWise:lDateWises){
   		
   		if(lWise.goalSetTime.equals("1 week")){
   			cal.setTime(lWise.leadsDate);  
   			cal.add(Calendar.DATE, 7);
   			Date m =  cal.getTime(); 
   			if(m.after(dateobj)) {
   				lDataVM.leads = lWise.leads;
   				lDataVM.goalTime =lWise.goalSetTime;
   				
   			}
   			
   		}else if(lWise.goalSetTime.equals("1 month")){
   			cal.setTime(lWise.leadsDate);  
   			cal.add(Calendar.DATE, 30);
   			Date m =  cal.getTime(); 
   			if(m.after(dateobj)) {
   				lDataVM.leads = lWise.leads;
   				lDataVM.goalTime =lWise.goalSetTime;
   			}
   		}
   		
   	}
   	
   	List<DateAndValueVM> sAndValues = new ArrayList<>();
   	List<Long> sAndLong = new ArrayList<>();
   	int countPlanCarSold = 0;
   	
   	
   	
		Calendar now = Calendar.getInstance();
		String month = monthName[now.get(Calendar.MONTH)];
		lDataVM.monthCurr = month;
		
		PlanScheduleMonthlyLocation pLocation = null;
		PlanScheduleMonthlySalepeople  pMonthlySalepeople = null;
		if(locOrPer.equals("location")){
			pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(locationId), month);
		}else{
			pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(users, month);
		}
			
		if(locOrPer.equals("location")){
	    	if(pLocation != null){
	    		List<Integer> longV = new ArrayList<>();
				DateAndValueVM sValue = new DateAndValueVM();
				sValue.name = "Plan";
				longV.add(Integer.parseInt(pLocation.totalEarning));
				sValue.data = longV;
				sAndValues.add(sValue);
	    	}
		}else{
			if(pMonthlySalepeople != null){
	    		List<Integer> longV = new ArrayList<>();
				DateAndValueVM sValue = new DateAndValueVM();
				sValue.name = "Plan";
				longV.add(Integer.parseInt(pMonthlySalepeople.totalBrought));
				sValue.data = longV;
				sAndValues.add(sValue);
	    	}
		}
		if(!locOrPer.equals("location")){
   	List<AuthUser> allLoUser = AuthUser.findByLocatio(Location.findById(locationId));
   	int flag = 0;
   	for(AuthUser aUser: allLoUser){
   		int pricecountOther = 0;
   		if(allLoUser != null){
       		
       		List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(aUser);
       		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(aUser);
       		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(aUser);
       		
       		for(RequestMoreInfo rMoreInfo: rInfo1){
       			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(rMoreInfo.vin,aUser);
       			for(Vehicle vehicle:vehicleVin){
       				if(vehicle != null){
       				 if(vehicle.status.equals("Sold")){
       					if(month.equals(onlyMonth.format(vehicle.soldDate))){
       						pricecountOther = pricecountOther + vehicle.price;
                   		}
       				 }
           			}
       			}
       			
       		}
       		
       		for(ScheduleTest sTest: sList1){
       			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(sTest.vin,aUser);
       			for(Vehicle vehicle:vehicleVin){
       			if(vehicle != null){
       				if(vehicle.status.equals("Sold")){
       					if(month.equals(onlyMonth.format(vehicle.soldDate))){
       						pricecountOther = pricecountOther + vehicle.price;
                   		}
       				 }
       				
       			}
       		}
       		}
       		
       		for(TradeIn tradeIn: tradeIns1){
       			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(tradeIn.vin,aUser);
       			for(Vehicle vehicle:vehicleVin){
       			if(vehicle != null){
       				if(vehicle.status.equals("Sold")){
       					if(month.equals(onlyMonth.format(vehicle.soldDate))){
       						pricecountOther = pricecountOther + vehicle.price;
                   		}
       				 }
       			}
       		}
       		}
       		
       	}
   		
   		if(pricecountOther > monthPriceCount && flag == 0){
   			flag = 1;
       		List<Integer> longV = new ArrayList<>();
       		DateAndValueVM sValue = new DateAndValueVM();
   			sValue.name = "Record";
   			longV.add(pricecountOther);
   			sValue.data = longV;
   			sAndValues.add(sValue);
       	}
   	}
   }
   	
   	List<Vehicle> vList2 = Vehicle.findByLocationAndSold(locationId);
   	
   	if(vList2.size() != 0){
   		List<Integer> longV = new ArrayList<>();
   		DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "You";
			longV.add(monthPriceCount);
			sValue.data = longV;
			sAndValues.add(sValue);
   	}
   	
   	lDataVM.sendData = sAndValues;

   }
    
    
    
    /*-----------------------------------------*/
 public static Result gmLocationManager(Long locationId){
	 AuthUser auUser = AuthUser.getlocationAndManagerOne(Location.findById(locationId));
	 return ok(Json.toJson(auUser));
	 
 }
/* public static Result getUserLocationInfoOther(String timeSet,String locOrPer,Long LocationId,Integer managerId){
	 AuthUser users = AuthUser.findById(managerId);
	 LocationWiseDataVM lDataVM = new LocationWiseDataVM();
	 findStatistics(timeSet,"location",LocationId,users, lDataVM);
	 return ok(Json.toJson(lDataVM));
 }*/
 
 public static Result getUserLocationInfo(Integer userkey,String timeSet,String locOrPer){
	 AuthUser users = AuthUser.findById(userkey);
	 LocationWiseDataVM lDataVM = new LocationWiseDataVM();
	 findStatistics(timeSet,locOrPer,users.location.id,users, lDataVM);
 	return ok(Json.toJson(lDataVM));
 }
 
    public static void findStatistics(String timeSet,String locOrPer,Long locationId,AuthUser users, LocationWiseDataVM lDataVM){
    	
    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	DateFormat onlyMonth = new SimpleDateFormat("MMMM");
    	
    	
    	Calendar cal1 = Calendar.getInstance();
    	
    	String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
		        "August", "September", "October", "November", "December" };
    	Date dateobj = new Date();
    	Calendar cal = Calendar.getInstance();  
    	String monthCal = monthName[cal.get(Calendar.MONTH)];
    	Map<String, Integer> mapCar = new HashMap<String, Integer>();
    	
    	
    	Date timeBack = null;
    	
    	if(timeSet.equals("Week")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.DATE, -7);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("week")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.DATE, -7);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("Month")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.MONTH, -1);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("Quater")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.MONTH, -3);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("6 Month")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.MONTH, -6);
			timeBack =  cal1.getTime(); 
    	}else if(timeSet.equals("Year")){
    		cal1.setTime(dateobj);  
			cal1.add(Calendar.MONTH, -12);
			timeBack =  cal1.getTime(); 
    	}
    	
    	int requestLeadCount = 0;
    	int scheduleLeadCount = 0;
    	int tradeInLeadCount = 0;
    	
    	int requestLeadCount1 = 0;
    	int scheduleLeadCount1 = 0;
    	int tradeInLeadCount1 = 0;
    	
    	List<RequestMoreInfo> rInfo = null;
    	List<ScheduleTest> sList = null;
    	List<TradeIn> tradeIns = null;
    	List<RequestMoreInfo> rInfoAll = null;
    	List<ScheduleTest> sListAll = null;
    	List<TradeIn> tradeInsAll = null;
    	
    	if((users.role.equals("Manager") && locOrPer.equals("location"))){
    		rInfo = RequestMoreInfo.findAllSeenLocationSch(locationId);
    		sList = ScheduleTest.findAllAssignedLocation(locationId);
    		tradeIns = TradeIn.findAllSeenLocationSch(locationId);
    		
    		rInfoAll = RequestMoreInfo.findByLocationNotCancel(locationId);
    		sListAll = ScheduleTest.findByLocationNotCancel(locationId);
    		tradeInsAll = TradeIn.findByLocationNotCancel(locationId);
    		
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		rInfo = RequestMoreInfo.findAllSeenSch(users);
    		sList = ScheduleTest.findAllAssigned(users);
    		tradeIns = TradeIn.findAllSeenSch(users);
    		
    		rInfoAll = RequestMoreInfo.findByAssigUserNotCancel(users);
    		sListAll = ScheduleTest.findByAssigUserNotCancel(users);
    		tradeInsAll = TradeIn.findByAssigUserNotCancel(users);
    	}
    	
    	for(RequestMoreInfo rMoreInfo:rInfo){
    		if(rMoreInfo.requestDate != null){
    		  if(rMoreInfo.requestDate.after(timeBack)) {
    			requestLeadCount++;
    		 }
    		}
    	}
    	
    	
    	for(ScheduleTest sTest:sList){
    		if(sTest.scheduleDate != null){
    	    	if(sTest.scheduleDate.after(timeBack)) {
    	    		scheduleLeadCount++;
    	    	}
    		}
    	}

    	for(TradeIn tIn:tradeIns){
    		if(tIn.tradeDate != null){
    			if(tIn.tradeDate.after(timeBack)) {
    				tradeInLeadCount++;
    			}
    		}
    	}
    	
    	for(RequestMoreInfo rMoreInfo:rInfoAll){
    		if(rMoreInfo.requestDate != null){
    		  if(rMoreInfo.requestDate.after(timeBack)) {
    			requestLeadCount1++;
    		 }
    		}
    	}
    	
    	
    	for(ScheduleTest sTest:sListAll){
    		if(sTest.scheduleDate != null){
    			if(sTest.scheduleDate.after(timeBack)) {
    				scheduleLeadCount1++;
    			}
    		}
    	}

    	for(TradeIn tIn:tradeInsAll){
    		if(tIn.tradeDate != null){
    			if(tIn.tradeDate.after(timeBack)) {
    				tradeInLeadCount1++;
    			}
    		}
    	}
    	
    	
    	int countLeads1 = requestLeadCount1 + scheduleLeadCount1 + tradeInLeadCount1;
    	int countLeads = requestLeadCount + scheduleLeadCount + tradeInLeadCount;
    	Location location = Location.findById(locationId);
    	
    	if(locOrPer.equals("location")){
    		lDataVM.imageUrl = location.getImageUrl();
    	}else if(locOrPer.equals("person")){
    		lDataVM.imageUrl = users.getImageUrl();
    	}
    	
    	//List<AuthUser> uAuthUser = AuthUser.getlocationAndRoleByType(location, "Sales Person");
    	lDataVM.countSalePerson = countLeads;
    	
    	Integer pricecount = 0;
    	Integer monthPriceCount = 0;
    	int saleCarCount = 0;
    	if((users.role.equals("Manager") && locOrPer.equals("location"))){
    		List<Vehicle> vList = Vehicle.findByLocationAndSold(location.id);
        	
        	
        	for(Vehicle vehList:vList){
        			if(vehList.soldDate.after(timeBack)) {
            			saleCarCount++;
            			pricecount = pricecount + vehList.price;
            		}
        			if(monthCal.equals(onlyMonth.format(vehList.soldDate))){
            			monthPriceCount = monthPriceCount + vehList.price;
            		}
        	}
        	if(countLeads1 != 0 && saleCarCount != 0){
        		double sucessCount= (double)saleCarCount/(double)countLeads1*100;
            	lDataVM.successRate = (int) sucessCount;
        	}else{
        		lDataVM.successRate = 0;
        	}
        	
        	
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		
    		List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(users);
    		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(users);
    		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(users);
    		List<Vehicle> vList = Vehicle.findBySoldUserAndSold(users);
    		for (Vehicle vehicle : vList) {
    			if(vehicle.soldDate.after(timeBack)) {
        			saleCarCount++;
        			pricecount = pricecount + vehicle.price;
				}
				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
					monthPriceCount = monthPriceCount + vehicle.price;
				}
			}
    
    		double sucessCount= (double)saleCarCount/(double)countLeads1*100;
    		lDataVM.successRate = (int) sucessCount;
    		
    	}
    	
    	lDataVM.totalSalePrice =  pricecount;
    	lDataVM.totalsaleCar = saleCarCount;
    	
    	double valAvlPrice= ((double)pricecount/(double)saleCarCount);
    	lDataVM.angSalePrice = (int) valAvlPrice;
    	
    	
      	if((users.role.equals("Manager") && locOrPer.equals("location"))){
    	
      		PlanScheduleMonthlyLocation pMonthlyLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(locationId), monthCal);
        	if(pMonthlyLocation != null){
        		double val= ((double)pricecount/Double.parseDouble(pMonthlyLocation.totalEarning));
            	lDataVM.AngSale = (int) (val*100);
        	}
        	
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		PlanScheduleMonthlySalepeople  pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(users, monthCal); 
        	if(pMonthlySalepeople != null){
        		double val= ((double)pricecount/Double.parseDouble(pMonthlySalepeople.totalBrought));
            	lDataVM.AngSale = (int) (val*100);
        	}
    	}
    	
    	
    	
    	
    	List<Vehicle> allVehiList = Vehicle.findByLocation(location.id);
    	int saleCar = 0;
    	int newCar = 0;
    	for(Vehicle vehicle:allVehiList){
    		if(vehicle.status.equals("Sold")){
    			if(vehicle.soldDate.after(timeBack)) {
    				saleCar++;
    			}
    		}//else if(vehicle.status.equals("Newly Arrived")){
    			newCar++;
    	//	}
    	}
    	
        
    	//List<LeadsDateWise> lDateWises = LeadsDateWise.findByLocation(Location.findById(Long.parseLong(session("USER_LOCATION"))));
    	List<LeadsDateWise> lDateWises = LeadsDateWise.getAllVehicles(users);
    	for(LeadsDateWise lWise:lDateWises){
    		
    		if(lWise.goalSetTime.equals("1 week")){
    			cal.setTime(lWise.leadsDate);  
    			cal.add(Calendar.DATE, 7);
    			Date m =  cal.getTime(); 
    			if(m.after(dateobj)) {
    				lDataVM.leads = lWise.leads;
    				lDataVM.goalTime =lWise.goalSetTime;
    				
    			}
    			
    		}else if(lWise.goalSetTime.equals("1 month")){
    			cal.setTime(lWise.leadsDate);  
    			cal.add(Calendar.DATE, 30);
    			Date m =  cal.getTime(); 
    			if(m.after(dateobj)) {
    				lDataVM.leads = lWise.leads;
    				lDataVM.goalTime =lWise.goalSetTime;
    			}
    		}
    		
    	}
    	
    	List<DateAndValueVM> sAndValues = new ArrayList<>();
    	List<Long> sAndLong = new ArrayList<>();
    	int countPlanCarSold = 0;
    	
    	
    	
		Calendar now = Calendar.getInstance();
		String month = monthName[now.get(Calendar.MONTH)];
		lDataVM.monthCurr = month;
		
		PlanScheduleMonthlyLocation pLocation = null;
		PlanScheduleMonthlySalepeople  pMonthlySalepeople = null;
		if(locOrPer.equals("location")){
			pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(locationId), month);
		}else{
			pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(users, month);
		}
			
		if(locOrPer.equals("location")){
	    	if(pLocation != null){
	    		List<Integer> longV = new ArrayList<>();
				DateAndValueVM sValue = new DateAndValueVM();
				sValue.name = "Plan";
				longV.add(Integer.parseInt(pLocation.totalEarning));
				sValue.data = longV;
				sAndValues.add(sValue);
	    	}
		}else{
			if(pMonthlySalepeople != null){
	    		List<Integer> longV = new ArrayList<>();
				DateAndValueVM sValue = new DateAndValueVM();
				sValue.name = "Plan";
				longV.add(Integer.parseInt(pMonthlySalepeople.totalBrought));
				sValue.data = longV;
				sAndValues.add(sValue);
	    	}
		}
		if(!locOrPer.equals("location")){
    	List<AuthUser> allLoUser = AuthUser.findByLocatio(Location.findById(locationId));
    	int flag = 0;
    	for(AuthUser aUser: allLoUser){
    		int pricecountOther = 0;
    		if(allLoUser != null){
        		
        		List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(aUser);
        		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(aUser);
        		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(aUser);
        		
        		for(RequestMoreInfo rMoreInfo: rInfo1){
        			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(rMoreInfo.vin,aUser);
        			for(Vehicle vehicle:vehicleVin){
        				if(vehicle != null){
        					if(vehicle.status.equals("Sold")){
            				if(onlyMonth.format(vehicle.soldDate).equals(month)) {
                    			pricecountOther= pricecountOther + vehicle.price;
            				}
        					}
            			}
        			}
        			
        		}
        		
        		for(ScheduleTest sTest: sList1){
        			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(sTest.vin,aUser);
        			for(Vehicle vehicle:vehicleVin){
        			if(vehicle != null){
        				if(vehicle.status.equals("Sold")){
        					if(onlyMonth.format(vehicle.soldDate).equals(month)) {
                    			pricecountOther = pricecountOther + vehicle.price;
            				}
        				}
        				
        			}
        		}
        		}
        		
        		for(TradeIn tradeIn: tradeIns1){
        			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(tradeIn.vin,aUser);
        			for(Vehicle vehicle:vehicleVin){
        			if(vehicle != null){
        				if(vehicle.status.equals("Sold")){
        					if(onlyMonth.format(vehicle.soldDate).equals(month)) {
                    			pricecountOther = pricecountOther + vehicle.price;
            				}
        				}
        			}
        		}
        		}
        		
        	}
    		
    		if(pricecountOther > monthPriceCount && flag == 0){
    			flag = 1;
        		List<Integer> longV = new ArrayList<>();
        		DateAndValueVM sValue = new DateAndValueVM();
    			sValue.name = "Record";
    			longV.add(pricecountOther);
    			sValue.data = longV;
    			sAndValues.add(sValue);
        	}
    	}
    }
    	
    	List<Vehicle> vList2 = Vehicle.findByLocationAndSold(locationId);
    	
    	if(vList2.size() != 0){
    		List<Integer> longV = new ArrayList<>();
    		DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "You";
			longV.add(monthPriceCount);
			sValue.data = longV;
			sAndValues.add(sValue);
    	}
    	
    	lDataVM.sendData = sAndValues;
    }
   
    
    public static Result getAllLocation(String timeSet) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		
    		Date dateobj = new Date();
        	Calendar cal = Calendar.getInstance();  
    		
    		Date timeBack = null;
        	
        	if(timeSet.equals("Week")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.DATE, -7);
    			timeBack =  cal.getTime(); 
        	}else if(timeSet.equals("Month")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.MONTH, -1);
    			timeBack =  cal.getTime(); 
        	}else if(timeSet.equals("Quater")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.MONTH, -3);
    			timeBack =  cal.getTime(); 
        	}else if(timeSet.equals("6 Month")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.MONTH, -6);
    			timeBack =  cal.getTime(); 
        	}else if(timeSet.equals("Year")){
        		cal.setTime(dateobj);  
    			cal.add(Calendar.MONTH, -12);
    			timeBack =  cal.getTime(); 
        	}
    		
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
    			String roles = "Manager";
    			AuthUser users = null;
    			users = AuthUser.getlocationAndManagerByType(location, roles);
    				if(location.manager != null){
    					users = AuthUser.findById(location.manager.id);
    					if(users != null){
    						vm.gmIsManager = 1; 
    					}
    				}else{
    					vm.gmIsManager = 0; 
    				}
    					
    			
    			
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
    			List<AuthUser> saleUsers = AuthUser.getAllUserByLocation(location);
    			vm.salePeopleUserLocation = saleUsers.size();
    			List<Vehicle> vList = Vehicle.findByLocationAndSold(location.id);
    			int carCount = 0;
    			for(Vehicle vehicle:vList){
    				if(vehicle.soldDate.after(timeBack)){
    					totalPrice = totalPrice + vehicle.price; 
    					carCount++;
    				}
    			}
    			vm.carSoldLocation = carCount;
    			vm.totalMoneyBrougthLocation = totalPrice;
    			if(totalPrice != 0 && vm.carSoldLocation != 0){
    				Double values = (double) vm.totalMoneyBrougthLocation / (double) vm.carSoldLocation;
    				vm.avgSaleLocation = values;
    			}
    			
    			List<Vehicle> vListAllSold = Vehicle.findBySold();
    			int totalPriceForAllLocation = 0;
    			for(Vehicle vehicle:vListAllSold){
    				if(vehicle.soldDate.after(timeBack)){
    					totalPriceForAllLocation = totalPriceForAllLocation + vehicle.price; 
    				}
    			}
    			if(totalPrice != 0 && vm.carSoldLocation != 0){
    				vm.percentOfMoney = (double)(totalPrice / totalPriceForAllLocation) * 100;
    			}
    			
    			
    			List<RequestMoreInfo> rInfoAll = RequestMoreInfo.findByLocationNotCancel(location);
    			List<ScheduleTest> sListAll = ScheduleTest.findByLocationNotCancel(location);
    			List<TradeIn> tradeInsAll = TradeIn.findByLocationNotCancel(location);
    			
    			int totalLead = 0;
    			
    			for(RequestMoreInfo rInfo:rInfoAll){
    				if(rInfo.requestDate.after(timeBack)){
    					totalLead++;
    				}
    			}
    			
    			for(ScheduleTest sInfo:sListAll){
    				if(sInfo.scheduleDate.after(timeBack)){
    					totalLead++;
    				}
    			}
    			
    			for(TradeIn tInfo:tradeInsAll){
    				if(tInfo.tradeDate.after(timeBack)){
    					totalLead++;
    				}
    			}
    			
    			if(carCount != 0 && totalLead != 0){
    				double value = (double)carCount / (double)totalLead;
    				vm.successRate = value * 100;
    			}
    			
    			int total = 0;
    			String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
				        "August", "September", "October", "November", "December" };
		    	
		     	String crMonth = monthName[Calendar.MONTH];
		     	
				PlanScheduleMonthlyLocation  pMonthlyLocation = null;
				pMonthlyLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(location, crMonth);
				
				if(pMonthlyLocation != null){
					total = Integer.parseInt(pMonthlyLocation.totalEarning);
		    	}
				if(total > 0 && totalPrice > 0){
					Double PlanPer =  (double)((totalPrice*100)/total);
					vm.PlanPer = PlanPer;		
				}
    			
    			
    				vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getLocationValueForGM(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    public static Result getLocationForGM() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    public static Result getDeactiveLocationForGM() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    public static Result getMangerAndLocation() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    public static Result getAllUsers() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    			
    			if(!vm.userType.equals("Manager")){
    				vmList.add(vm);
    			}
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getUsers(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   	return ok(home.render(""));
		   	} else {
		   	
		   	List<AuthUser> userList = AuthUser.getUserByType();
		   	List<UserVM> vmList = new ArrayList<>();
		   	for(AuthUser user : userList) {
		   	UserVM vm = new UserVM();
		   		vm.fullName = user.firstName + " "+ user.lastName;
		   		vm.firstName = user.firstName;
		   		vm.lastName = user.lastName;
		   		vm.email = user.email;
		   		vm.phone = user.phone;
		   		vm.userType = user.role;
		   		vm.imageName = user.imageName;
		   		vm.imageUrl = user.imageUrl;
		   		vm.id = user.id;
		   		vmList.add(vm);
		   	}
		   	return ok(Json.toJson(vmList));
		   	}
   }
   
   public static Result getgroupInfo(){
	   
	   List<GroupTable> gList = GroupTable.findAllGroup();
		return ok(Json.toJson(gList));
   }
   
   public static Result saveGroup(String createGroup){
	   if(session("USER_KEY") == null || session("USER_KEY") == "") {
	   		return ok(home.render(""));
	   	} else {
	   	  GroupTable gTable = new GroupTable();
	   	  gTable.setName(createGroup);
	   	  gTable.save();
	   	}
	   return ok("");
   }
   
   public static Result deleteGroup(Long groupId){
	   if(session("USER_KEY") == null || session("USER_KEY") == "") {
	   		return ok(home.render(""));
	   	} else {
	   	  GroupTable gTable = GroupTable.findById(groupId);
	   	  gTable.delete();
	   	}
	   return ok("");
   }
    
   public static Result updateUploadLocationImageFile() {
	   
	   if(session("USER_KEY") == null || session("USER_KEY") == "") {
   		return ok(home.render(""));
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
   
    public static Result updateUser() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	    	   userObj.update();
	    	return ok();
    	}    	
    }
    
    public static Result deleteUserById(Integer id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		
    		AuthUser user = AuthUser.findById(id);
    		//Location location = Location.findById(id);
    		List<RequestMoreInfo> rInfo = RequestMoreInfo.findAllByAssignedUser(user);
    		List<ScheduleTest> sTest = ScheduleTest.findAllByUserAssigned(user);
    		List<TradeIn> tIns = TradeIn.findAllByAssignedUser(user);
    		
    		for(RequestMoreInfo rMoreInfo:rInfo){
    			rMoreInfo.setStatus("UNCLAIMED");
    			rMoreInfo.setAssignedTo(null);
    			rMoreInfo.update();
    		}
    		
    		for(ScheduleTest schTest:sTest){
    			schTest.setLeadStatus("UNCLAIMED");
    			schTest.setAssignedTo(null);
    			schTest.update();
    		}
    		
    		for(TradeIn trIn:tIns){
    			trIn.setStatus("UNCLAIMED");
    			trIn.setAssignedTo(null);
    			trIn.update();
    		}
    		
    		user.delete();
    		return ok();
    	}
    }
    
    
    public static Result setVehicleStatus() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    
   /* public static Result setVehicleAndScheduleStatus() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		
    		Date currDate = new Date();
    		List<String> emailList = new ArrayList<>();
    		AuthUser user = (AuthUser) getLocalUser();
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
    		contact.user = user;
    		contact.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
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
    		
    		String vinNo = null;
    		if(vm.typeOfLead.equals("Request More Info")){
    			RequestMoreInfo rInfo = RequestMoreInfo.findById(vm.infoId);
    			rInfo.setLeadStatus("COMPLETE");
    			rInfo.setStatus("COMPLETE");
    			rInfo.setStatusDate(currDate);
    			rInfo.setCustZipCode(vm.custZipCode);
    			rInfo.setEnthicity(vm.enthicity);
    			rInfo.update();
    			
    			UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Vehicle Sold");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.requestMoreInfo = RequestMoreInfo.findById(rInfo.id);
        		uNotes.save();
    			
    			vinNo = rInfo.vin;
    		}else if(vm.typeOfLead.equals("Schedule Test Drive")){
    			ScheduleTest schedule = ScheduleTest.findById(vm.infoId);
        		//schedule.setLeadStatus("SUCCESSFUL");
        		schedule.setLeadStatus("COMPLETE");
        		schedule.setStatusDate(currDate);
        		schedule.setCustZipCode(vm.custZipCode);
        		schedule.setEnthicity(vm.enthicity);
        		schedule.update();
        		
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Vehicle Sold");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.scheduleTest = ScheduleTest.findById(schedule.id);
        		uNotes.save();
        		
        		vinNo = schedule.vin;
    		}else if(vm.typeOfLead.equals("Trade-In Appraisal")){
    			TradeIn tIn = TradeIn.findById(vm.infoId);
    			tIn.setLeadStatus("COMPLETE");
    			tIn.setStatus("COMPLETE");
    			tIn.setStatusDate(currDate);
    			tIn.setCustZipCode(vm.custZipCode);
    			tIn.setEnthicity(vm.enthicity);
    			tIn.update();
    			
    			UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Vehicle Sold");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.tradeIn = TradeIn.findById(tIn.id);
        		uNotes.save();
    			vinNo = tIn.vin;
    		}
    		
    		Vehicle vehicle = Vehicle.findByVinAndStatus(vinNo);
    		Date date = new Date();
    		if(vehicle != null){
	    		vehicle.setStatus("Sold");
	    		
	    		vehicle.setSoldDate(date);
	    		vehicle.setSoldUser(user);
	    		vehicle.update();
    		}
    		
    		for(RequestInfoVM rMoreInfo: vm.parentChildLead){
    			if(rMoreInfo.status.equals("Sold")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("COMPLETE");
    					requestMoreInfo.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(requestMoreInfo.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(requestMoreInfo.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(requestMoreInfo.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setLeadStatus("COMPLETE");
    					schTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(schTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(schTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(schTest.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					TradeIn tTest = TradeIn.findById(rMoreInfo.id);
    					tTest.setStatus("COMPLETE");
    					tTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(tTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(tTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(tTest.vin, currDate);
    				}
    			}else if(rMoreInfo.status.equals("Cancel")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("CANCEL");
    					requestMoreInfo.update();
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setLeadStatus("CANCEL");
    					schTest.update();
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					TradeIn tTest = TradeIn.findById(rMoreInfo.id);
    					tTest.setStatus("CANCEL");
    					tTest.update();
    				}
    			}
    		}
    	
    		return ok();
    	}
    }*/
    
    public static Result setScheduleConfirmClose(Long id,String leadType){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		if(leadType.equals("Schedule Test Drive")) {
    			ScheduleTest schedule = ScheduleTest.findById(id);
    			schedule.setConfirmDate(null);
    			schedule.setConfirmTime(null);
    			schedule.update();
    			
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Test Drive has been canceled");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.scheduleTest = ScheduleTest.findById(schedule.id);
        		uNotes.save();
        		
    		} else if(leadType.equals("Request More Info")) {
    			RequestMoreInfo info = RequestMoreInfo.findById(id);
    			info.setConfirmDate(null);
    			info.setConfirmTime(null);
    			info.update();
    			
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Test Drive has been canceled");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
        		uNotes.save();
        		
    		} else if(leadType.equals("Trade-In Appraisal")) {
    			TradeIn info = TradeIn.findById(id);
    			info.setConfirmDate(null);
    			info.setConfirmTime(null);
    			info.update();
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Test Drive has been canceled");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.tradeIn = TradeIn.findById(info.id);
        		uNotes.save();
    		}
    		return ok();
    	}
    }
    
    public static Result setScheduleStatusClose(Long id,String leadtype,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		if(leadtype.equals("Schedule Test Drive")) {
    			ScheduleTest schedule = ScheduleTest.findById(id);
    			schedule.setLeadStatus("CANCEL");
    			schedule.setStatusDate(currDate);
    			schedule.setStatusTime(currDate);
    			schedule.setReason(reason);
    			schedule.update();
    			
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Client didn't buy vehicle");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.scheduleTest = ScheduleTest.findById(schedule.id);
        		uNotes.save();
        		
    		} else if(leadtype.equals("Request More Info")) {
    			RequestMoreInfo info = RequestMoreInfo.findById(id);
    			info.setStatus("CANCEL");
    			info.setStatusDate(currDate);
    			info.setStatusTime(currDate);
    			info.setReason(reason);
    			info.update();
    			
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Client didn't buy vehicle");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
        		uNotes.save();
        		
    		} else if(leadtype.equals("Trade-In Appraisal")) {
    			TradeIn info = TradeIn.findById(id);
    			info.setStatus("CANCEL");
    			info.setStatusDate(currDate);
    			info.setStatusTime(currDate);
    			info.setReason(reason);
    			info.update();
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Client didn't buy vehicle");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.tradeIn = TradeIn.findById(info.id);
        		uNotes.save();
    		}
    		return ok();
    	}
    }
    
    public static Result setRequestStatusComplete() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		
    		Date currDate = new Date();
    		String msg="success";
    		AuthUser user = (AuthUser) getLocalUser();
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
    		try {
    			Contacts con = Contacts.findByEmail(vm.email);
        		if(con ==null){
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
            		contactsObj.newsLetter = 1;
            		contactsObj.user = user.id;
            		contactsObj.type = "Online";
            		contactsObj.assignedTo = user.id.toString();
            		contactsObj.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
            		contactsObj.save();
        		}else{
        			msg="contact error";
        		}
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		Date date = new Date();
    		if(vm.typeOfLead.equals("Request More Info")){
		    		
		    		RequestMoreInfo info = RequestMoreInfo.findById(vm.infoId);
		    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
		    		if(vehicle != null){
			    		vehicle.setStatus("Sold");
			    		vehicle.setSoldDate(date);
			    		vehicle.setSoldUser(user);
			    		vehicle.setPrice(Integer.parseInt(vm.price));
			    		vehicle.update();
		    		}
		    		info.setStatus("COMPLETE");
		    		info.setCustZipCode(vm.custZipCode);
		    		info.setEnthicity(vm.enthicity);
		    		info.setStatusDate(currDate);
		    		info.setStatusTime(currDate);
		    		
		    		info.update();
		    		
		    		
		    		UserNotes uNotes = new UserNotes();
		    		uNotes.setNote("Vehicle Sold");
		    		uNotes.setAction("Other");
		    		uNotes.createdDate = currDate;
		    		uNotes.createdTime = currDate;
		    		uNotes.user = user;
		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
		    		uNotes.save();
		    		
		    		otherParentChildLeadsStatus(vm,user,currDate);
		    		lostLeadsFunction(info.vin, currDate);
    		}else if(vm.typeOfLead.equals("Trade-In Appraisal")){
    				
    			
        		TradeIn info = TradeIn.findById(vm.infoId);
        		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
        		
        		if(vehicle != null){
        		vehicle.setStatus("Sold");
        		vehicle.setSoldDate(date);
        		vehicle.setSoldUser(user);
        		vehicle.setPrice(Integer.parseInt(vm.price));
        		vehicle.update();
        		}
        		info.setStatus("COMPLETE");
        		info.setCustZipCode(vm.custZipCode);
        		info.setEnthicity(vm.enthicity);
        		info.setStatusDate(currDate);
        		info.setStatusTime(currDate);
        		info.update();
        		
        		
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Vehicle Sold");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.tradeIn = TradeIn.findById(info.id);
        		uNotes.save();
        		
        		otherParentChildLeadsStatus(vm,user,currDate);
        		lostLeadsFunction(info.vin, currDate);
        		
        	
    		}else if(vm.typeOfLead.equals("Schedule Test Drive")){
    			
    			ScheduleTest schedule = ScheduleTest.findById(vm.infoId);
        		schedule.setLeadStatus("COMPLETE");
        		schedule.setStatusDate(currDate);
        		schedule.setStatusTime(currDate);
        		schedule.setCustZipCode(vm.custZipCode);
        		schedule.setEnthicity(vm.enthicity);
        		schedule.update();
        		
        		Vehicle vehicle = Vehicle.findByVinAndStatus(schedule.vin);
        		if(vehicle != null){
	        		vehicle.setStatus("Sold");
	        		vehicle.setSoldDate(date);
	        		vehicle.setSoldUser(user);
	        		vehicle.setPrice(Integer.parseInt(vm.price));
	        		vehicle.update();
        		}
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Vehicle Sold");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.scheduleTest = ScheduleTest.findById(schedule.id);
        		uNotes.save();
        		otherParentChildLeadsStatus(vm,user,currDate);
        		lostLeadsFunction(schedule.vin, currDate);
    		}
    		return ok(msg);
    	}
    }
    
    public static void otherParentChildLeadsStatus(SoldContactVM vm,AuthUser user,Date currDate){
    	if(vm.parentChildLead != null){
			for(RequestInfoVM rMoreInfo: vm.parentChildLead){
    			if(rMoreInfo.status.equals("Sold")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("COMPLETE");
    					requestMoreInfo.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(requestMoreInfo.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(currDate);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.setPrice(rMoreInfo.price);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(requestMoreInfo.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(requestMoreInfo.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setLeadStatus("COMPLETE");
    					schTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(schTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(currDate);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.setPrice(rMoreInfo.price);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(schTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(schTest.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					TradeIn tTest = TradeIn.findById(rMoreInfo.id);
    					tTest.setStatus("COMPLETE");
    					tTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(tTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(currDate);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.setPrice(rMoreInfo.price);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(tTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(tTest.vin, currDate);
    				}
    			}else if(rMoreInfo.status.equals("Cancel")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("CANCEL");
    					requestMoreInfo.update();
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setLeadStatus("CANCEL");
    					schTest.update();
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					TradeIn tTest = TradeIn.findById(rMoreInfo.id);
    					tTest.setStatus("CANCEL");
    					tTest.update();
    				}
    			}
    		}
    		
		}
    }
    
    public static Result saveCompletedLeads(String duration,String comment,String id,String typeOfLead){
    	
    	AuthUser user = getLocalUser();
    	Date currDate = new Date();
		if(typeOfLead.equals("requestMore") || typeOfLead.equals("Request More Info")) {
			RequestMoreInfo requestMore = RequestMoreInfo.findById(Long.parseLong(id));
			requestMore.setTestDriveStatus("TestDriveCompleted");
			requestMore.setTestDriveCompletedComment(comment);
			requestMore.setTestDriveCompletedDuration(duration);
			requestMore.update();
			
			UserNotes uNotes = new UserNotes();
    		uNotes.setNote("Test Drive Successfully Completed. Duration "+duration+" minutes");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.requestMoreInfo = requestMore;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.save();
			
		}
		if(typeOfLead.equals("scheduleTest") || typeOfLead.equals("Schedule Test Drive")) {
			ScheduleTest scheduleTest = ScheduleTest.findById(Long.parseLong(id));
			
			//scheduleTest.setLeadStatus("TestDriveCompleted");
			scheduleTest.setTestDriveStatus("TestDriveCompleted");
			scheduleTest.setTestDriveCompletedComment(comment);
			scheduleTest.setTestDriveCompletedDuration(duration);
			scheduleTest.update();
			
			UserNotes uNotes = new UserNotes();
			uNotes.setNote("Test Drive Successfully Completed. Duration "+duration+" minutes");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.scheduleTest = scheduleTest;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.save();
			
		}
		if(typeOfLead.equals("tradeIn") || typeOfLead.equals("Trade-In Appraisal")) {
			TradeIn tradeIn = TradeIn.findById(Long.parseLong(id));
			
			tradeIn.setTestDriveStatus("TestDriveCompleted");
			tradeIn.setTestDriveCompletedComment(comment);
			tradeIn.setTestDriveCompletedDuration(duration);
			tradeIn.update();
			
			UserNotes uNotes = new UserNotes();
			uNotes.setNote("Test Drive Successfully Completed. Duration "+duration+" minutes");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.tradeIn = tradeIn;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.save();
		}
    	return ok();
    }
    
    public static void lostLeadsFunction(String vin, Date currDate){
    	List<String> emailList = new ArrayList<>();
    	List<TradeIn> tIn = TradeIn.findByVinAndLocation(vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
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
		
		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocation(vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
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
		
		
		List<ScheduleTest> sTests = ScheduleTest.findByVinAndLocation(vin, Location.findById(Long.parseLong(session("USER_LOCATION"))));
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
		
		if(emailList.size()>0){
			vehicleSoldEmail(emailList);
		}
    }
    
    public static Result setRequestStatusCancel(Long id,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		RequestMoreInfo info = RequestMoreInfo.findById(id);
    		info.setStatus("CANCEL");
    		info.setStatusDate(currDate);
    		info.setStatusTime(currDate);
    		info.setReason(reason);
    		info.update();
    		
    		
    		UserNotes uNotes = new UserNotes();
    		uNotes.setNote("Client didn't buy vehicle");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
    		uNotes.save();
    		return ok();
    	}
    }
    
    /*public static Result setTradeInStatusComplete() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		
    		Date currDate = new Date();
    		List<String> emailList = new ArrayList<>();
    		AuthUser user = (AuthUser) getLocalUser();
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
    		TradeIn info = TradeIn.findById(vm.infoId);
    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
    		Date date = new Date();
    		if(vehicle != null){
    		vehicle.setStatus("Sold");
    		vehicle.setSoldDate(date);
    		vehicle.setSoldUser(user);
    		vehicle.update();
    		}
    		info.setStatus("COMPLETE");
    		info.setCustZipCode(vm.custZipCode);
    		info.setEnthicity(vm.enthicity);
    		info.setStatusDate(currDate);
    		info.update();
    		
    		for(RequestInfoVM rMoreInfo: vm.parentChildLead){
    			if(rMoreInfo.status.equals("Sold")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("COMPLETE");
    					requestMoreInfo.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(requestMoreInfo.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(requestMoreInfo.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(requestMoreInfo.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Schedule Test Drive")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setStatus("COMPLETE");
    					schTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(schTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(schTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(schTest.vin, currDate);
    				}else if(rMoreInfo.typeOfLead.equals("Trade-In Appraisal")){
    					ScheduleTest schTest = ScheduleTest.findById(rMoreInfo.id);
    					schTest.setStatus("COMPLETE");
    					schTest.update();
    					
    					Vehicle vehicle1 = Vehicle.findByVinAndStatus(schTest.vin);
    		    		if(vehicle1 != null){
    			    		vehicle1.setStatus("Sold");
    			    		vehicle1.setSoldDate(date);
    			    		vehicle1.setSoldUser(user);
    			    		vehicle1.update();
    		    		}
    		    		
    		    		UserNotes uNotes = new UserNotes();
    		    		uNotes.setNote("Vehicle Sold");
    		    		uNotes.setAction("Other");
    		    		uNotes.createdDate = currDate;
    		    		uNotes.createdTime = currDate;
    		    		uNotes.user = user;
    		    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		    		uNotes.requestMoreInfo = RequestMoreInfo.findById(schTest.id);
    		    		uNotes.save();
    		    		
    		    		lostLeadsFunction(schTest.vin, currDate);
    				}
    			}else if(rMoreInfo.status.equals("Cancel")){
    				if(rMoreInfo.typeOfLead.equals("Request More Info")){
    					RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(rMoreInfo.id);
    					requestMoreInfo.setStatus("CANCEL");
    					requestMoreInfo.update();
    				}
    			}
    		}
    		
    		UserNotes uNotes = new UserNotes();
    		uNotes.setNote("Vehicle Sold");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.tradeIn = TradeIn.findById(info.id);
    		uNotes.save();
    		
    		lostLeadsFunction(info.vin, currDate);
    		
    	
    		return ok();
    	}
    }*/
    
    public static Result setTradeInStatusCancel(Long id,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		TradeIn info = TradeIn.findById(id);
    		info.setStatus("CANCEL");
    		info.setStatusDate(currDate);
    		info.setStatusTime(currDate);
    		info.setReason(reason);
    		info.update();
    		
    		
    		UserNotes uNotes = new UserNotes();
    		uNotes.setNote("Client didn't buy vehicle");
    		uNotes.setAction("Other");
    		uNotes.createdDate = currDate;
    		uNotes.createdTime = currDate;
    		uNotes.user = user;
    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		uNotes.tradeIn = TradeIn.findById(info.id);
    		uNotes.save();
    		return ok();
    	}
    }
   
    public static Result getUserType() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		String userType = user.getRole();
    		if(userType == null) {
    			userType = "";
    		}
    		return ok(userType);
    	}
    }
    
    public static Result getScheduleDates() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		List<SqlRow> rows = ScheduleTest.getScheduleDates(user);
    		List<RequestInfoVM> vmList = new ArrayList<>();
    		for(SqlRow row : rows) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = row.getString("confirm_date");
    			vmList.add(vm);
    		}
    		
    		List<SqlRow> toDoRows = ToDo.getToDoDates();
    		for(SqlRow todo : toDoRows) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = todo.getString("due_date");
    			vmList.add(vm);
    		}
    		
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getScheduleBySelectedDate(String date) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
    		Date dateObj = df.parse(date);
    		List<ScheduleTest> scheduleList = ScheduleTest.findByDateAndAssignedUser(user, dateObj);
    		List<RequestInfoVM> vmList = new ArrayList<>();
    		Calendar time = Calendar.getInstance();
    		for(ScheduleTest test : scheduleList) {
    			RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = test.id;
	    		vm.vin = test.vin;
	    		//vm.isgoogle = test.google_id;
	    		if(test.is_google_data !=null){
	    			vm.is_google = test.is_google_data;
	    		}	    		
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(test.vin);
	    		if(vehicle != null) {
	    			vm.make = vehicle.make;
	    			vm.model = vehicle.model;
	    			vm.trim=vehicle.trim;
	    			vm.year=vehicle.year;
	    			
	    		}
	    		vm.bestTime=test.bestTime;
	    		vm.name = test.name;
	    		vm.email = test.email;
	    		vm.phone = test.phone;
	    		vm.meeting = test.meetingStatus;
	    		if(test.getConfirmDate() != null) {
	    			vm.confirmDate = df2.format(test.getConfirmDate());
	    		}
	    		if(test.getConfirmTime() != null) {
	    			time.setTime(test.getConfirmTime());
	    			String ampm = "";
	    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
	    				ampm = "PM";
	    			} else {
	    				ampm = "AM";
	    			}
	    			vm.confirmTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;
	    		}
	    		vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getUsersToAssign() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		List<AuthUser> usersList = AuthUser.getUserByType();
    		List<UserVM> vmList = new ArrayList<>();
    		for(AuthUser obj: usersList) {
    			UserVM vm = new UserVM();
    			vm.fullName = obj.firstName+" "+obj.lastName;
    			vm.id = obj.id;
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result saveToDoData() throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		Form<ToDoVM> form = DynamicForm.form(ToDoVM.class).bindFromRequest();
    		AuthUser user = getLocalUser();
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		ToDoVM vm = form.get();
    		ToDo toDo = new ToDo();
    		toDo.task = vm.task;
    		toDo.dueDate = df.parse(vm.dueDate);
    		toDo.assignedTo = AuthUser.findById(vm.assignedToId);
    		toDo.priority = vm.priority;
    		toDo.status = "Assigned";
    		toDo.assignedBy = user;
    		toDo.saveas = 0;
			//toDo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		toDo.save();
    		
    		return ok();
    	}
    }
    
    public static Result getToDoList() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		List<ToDo> toDoList = ToDo.findAll();
    		List<ToDoVM> vmList = new ArrayList<>();
    		for(ToDo todo: toDoList) {
    			ToDoVM vm = new ToDoVM();
    			vm.id = todo.id;
    			vm.task = todo.task;
    			vm.dueDate = df.format(todo.dueDate);
    			vm.assignedToId = todo.assignedTo.id;
    			vm.priority = todo.priority;
    			vm.status = todo.status;
    			vm.assignedById = todo.assignedBy.id;
    			vm.saveas = todo.saveas;
    			
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result saveCompleteTodoStatus(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		ToDo todo = ToDo.findById(id);
    		todo.setStatus("Completed");
    		todo.update();
    		return ok();
    	}	
    }
    
    public static Result saveCancelTodoStatus(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		ToDo todo = ToDo.findById(id);
    		todo.setStatus("Deleted");
    		todo.update();
    		return ok();
    	}	
    }
    
    public static Result getToDoBySelectedDate(String date) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    		Date dateObj = df2.parse(date);
    		List<ToDo> toDoList = ToDo.findByDate(dateObj);
    		List<ToDoVM> vmList = new ArrayList<>();
    		for(ToDo todo: toDoList) {
    			ToDoVM vm = new ToDoVM();
    			vm.id = todo.id;
    			vm.task = todo.task;
    			vm.dueDate = df.format(todo.dueDate);
    			vm.assignedToId = todo.assignedTo.id;
    			vm.priority = todo.priority;
    			vm.status = todo.status;
    			vm.assignedById = todo.assignedBy.id;
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    public static Result getSalesUserList(Long managerid){
    	
    	AuthUser user = getLocalUser();	
        List<AuthUser> SalesUserList = AuthUser.findByLocatio(Location.findById(managerid));
        List<UserVM> vmList = new ArrayList<>();
        for(AuthUser obj: SalesUserList) {
        	if(obj.role.equalsIgnoreCase("Sales Person")){
        		UserVM vm = new UserVM();
        		vm.firstName = obj.firstName;
        		vm.id = obj.id;
        		vmList.add(vm);
        	}
     }
		return ok(Json.toJson(vmList));   	
    	    	   	   	
 }
    
    
    
    public static Result getSalesUser() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		
    		//List<AuthUser> SalesUserList = AuthUser.getAllUserByLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    		List<AuthUser> SalesUserList = AuthUser.findByLocatio(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    		
    		
    		List<UserVM> vmList = new ArrayList<>();
    		if(user.role != null) {
    			if(user.role.equals("General Manager") && user.role.equals("Manager")) {
	    			UserVM gmObj = new UserVM();
		    		gmObj.fullName = user.firstName+" "+user.lastName;
		    		gmObj.id = user.id;
					vmList.add(gmObj);
    			}
    		}
    		for(AuthUser obj: SalesUserList) {
    			UserVM vm = new UserVM();
    			vm.fullName = obj.firstName+" "+obj.lastName;
    			vm.id = obj.id;
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getSalesUserValue(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		List<AuthUser> SalesUserList = AuthUser.getAllUserByLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    		return ok(Json.toJson(SalesUserList));
    	}
    }
    
    public static Result getSalesUserOnly(Long locationValue) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		String[] monthName = { "january", "february", "march", "april", "may", "june", "july",
    		        "august", "september", "october", "november", "december" };
    		Calendar now = Calendar.getInstance();
    		String month = monthName[now.get(Calendar.MONTH)];
    		
    		List<PlanScheduleMonthlySalepeople> pSalepeople = PlanScheduleMonthlySalepeople.findByAllLocationAndMonth(Location.findById(Long.valueOf(session("USER_LOCATION"))),month);
    		//session("USER_LOCATION")
    		List<AuthUser> SalesUserList;
    		if(locationValue == 0){
    			SalesUserList = AuthUser.getAllSalesUser();
    		}else{
    			SalesUserList = AuthUser.getAllUserByLocation(Location.findById(locationValue));
    		}
    		
    		List<UserVM> vmList = new ArrayList<>();
    		
    		for(AuthUser obj: SalesUserList) {
    			
    			UserVM vm = new UserVM();
    			vm.fullName = obj.firstName+" "+obj.lastName;
    			vm.id = obj.id;
    			for(PlanScheduleMonthlySalepeople ps:pSalepeople){
    				if(obj.id.equals(ps.user.id)){
        				vm.quota = ps.totalBrought;
        			}
    			}
    			
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getAllSalesPersonScheduleTestAssigned(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user;
    		if(id == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(id);
    		}
	    	
	    	List<ScheduleTest> listData = ScheduleTest.findAllAssigned(user);
	    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findAllScheduledUser(user);
	    	List<TradeIn> tradeIns = TradeIn.findAllScheduledUser(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	Calendar time = Calendar.getInstance();
	    	for(ScheduleTest info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.typeofVehicle=vehicle.typeofVehicle;
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.year = vehicle.year;
	    			vm.mileage = vehicle.mileage;
	    			vm.bodyStyle =vehicle.bodyStyle;
	    			vm.drivetrain = vehicle.drivetrain;
	    			vm.engine = vehicle.engine;
	    			vm.transmission = vehicle.transmission;
	    			vm.price = vehicle.price;
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
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.typeOfLead = "Schedule Test Drive";
	    		/*List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);*/
	    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
	    		Integer nFlag =0;
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
	    		if(info.getConfirmDate() != null) {
	    			//vm.confirmDate = df.format(info.getConfirmDate());
	    			String chaArr[] = df.format(info.getConfirmDate()).split("-");
	    			vm.confirmDate = chaArr[2]+"-"+chaArr[1]+"-"+chaArr[0];
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
	    		vm.option = 0;
	    		
	    		findSchedulParentChildAndBro(infoVMList, info, df, vm);
	    		
	    		/*List<RequestInfoVM> rList2 = new ArrayList<>();
	    		if(info.parentId != null){
	    			ScheduleTest sTest = ScheduleTest.findByIdAndParent(info.parentId);
	    			if(sTest != null){
	    				RequestInfoVM rList1 = new RequestInfoVM();
	    				rList1.id = sTest.id;
	    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(sTest.vin);
	    	    		rList1.vin = sTest.vin;
	    	    		if(vehicle1 != null) {
	    	    			rList1.model = vehicle1.model;
	    	    			rList1.make = vehicle1.make;
	    	    			rList1.stock = vehicle1.stock;
	    	    			rList1.year = vehicle1.year;
	    	    			rList1.mileage = vehicle1.mileage;
	    	    			rList1.price = vehicle1.price;
	    	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	    	        		if(vehicleImage!=null) {
	    	        			rList1.imgId = vehicleImage.getId().toString();
	    	        		}
	    	        		else {
	    	        			rList1.imgId = "/assets/images/no-image.jpg";
	    	        		}
	    	    			
	    	    		}
	    	    		rList1.name = sTest.name;
	    	    		rList1.phone = sTest.phone;
	    	    		rList1.email = sTest.email;
	    	    		rList1.requestDate = df.format(sTest.scheduleDate);
	    	    		rList1.typeOfLead = "Trade-In Appraisal";
	    	    		
	    	    		rList2.add(rList1);
	    			}
	    		}
	    		
	    		List<ScheduleTest> tIns = ScheduleTest.findAllByParentID(info.getId());
	    		for(ScheduleTest info1:tIns){
	    			RequestInfoVM rList1 = new RequestInfoVM();
    				rList1.id = info1.id;
    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    	    		rList1.vin = info1.vin;
    	    		if(vehicle1 != null) {
    	    			rList1.model = vehicle1.model;
    	    			rList1.make = vehicle1.make;
    	    			rList1.stock = vehicle1.stock;
    	    			rList1.year = vehicle1.year;
    	    			rList1.mileage = vehicle1.mileage;
    	    			rList1.price = vehicle1.price;
    	    			rList1.bodyStyle =vehicle1.bodyStyle;
    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
    	        		if(vehicleImage!=null) {
    	        			rList1.imgId = vehicleImage.getId().toString();
    	        		}
    	        		else {
    	        			rList1.imgId = "/assets/images/no-image.jpg";
    	        		}
    	    		}
    	    		rList1.name = info1.name;
    	    		rList1.phone = info1.phone;
    	    		rList1.email = info1.email;
    	    		rList1.requestDate = df.format(info1.scheduleDate);
    	    		rList1.typeOfLead = "Trade-In Appraisal";
    	    		
    	    		rList2.add(rList1);
	    		}
	    		vm.parentChildLead = rList2;
	    		
	    		infoVMList.add(vm);*/
	    	}
	    	
	    	for(TradeIn info: tradeIns) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
	    			vm.bodyStyle =vehicle.bodyStyle;
	    			vm.drivetrain = vehicle.drivetrain;
	    			vm.engine = vehicle.engine;
	    			vm.price = vehicle.price;
	    			vm.transmission = vehicle.transmission;
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
	        		if(vehicleImage!=null) {
	        			vm.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			vm.imgId = "/assets/images/no-image.jpg";
	        		}
	    		}
	    		vm.name = info.firstName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.pdfPath = info.pdfPath;
	    		if(info.bestDay != null){
	    			/*String chaArr[] = info.bestDay.split("-");
	    			vm.bestDay = chaArr[1]+"/"+chaArr[0]+"/"+chaArr[2];*/
	    			vm.bestDay = info.bestDay;
	    		}
	    		vm.bestTime = info.bestTime;
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		
	    		LeadVM lVm = new LeadVM();
	    		lVm.id = info.id.toString();
	    		//if(!info.comments.equals("null")){
    			lVm.comments = info.comments;
    		//}
    		if(info.year != null){
    			if(!info.year.equals("null")){
	    			lVm.year = info.year;
	    		}
    		}
    		
    		if(info.make != null){
    			lVm.make = info.make;
    		}
    		if(info.model != null){
    			lVm.model = info.model;
    		}
    		
    		if(info.exteriorColour != null){
    			if(!info.exteriorColour.equals("null")){
	    			lVm.exteriorColour = info.exteriorColour;
	    		}
    		}
    		
    		if(info.kilometres != null){
    			if(!info.kilometres.equals("null")){
	    			lVm.kilometres = info.kilometres;
	    		}
    		}
    		
    		if(info.year != null){
    			if(!info.engine.equals("null")){
    				lVm.engine = info.engine;
    			}
    		}
    		if(info.doors != null){
	    		if(!info.doors.equals("null")){
	    			lVm.doors = info.doors;
	    		}
    		}
    		if(info.transmission != null){
	    		if(!info.transmission.equals("null")){
	    			lVm.transmission = info.transmission;
	    		}
    		}
    		if(info.drivetrain != null){
	    		if(!info.drivetrain.equals("null")){
	    			lVm.drivetrain = info.drivetrain;
	    		}
    		}
    		if(info.bodyRating != null){
	    		if(!info.bodyRating.equals("null")){
	    			lVm.bodyRating = info.bodyRating;
	    		}
    		}
    		
    		if(info.tireRating != null){
	    		if(!info.tireRating.equals("null")){
	    			lVm.tireRating = info.tireRating;
	    		}
    		}
    		
    		if(info.engineRating != null){
	    		if(!info.engineRating.equals("null")){
	    			lVm.engineRating = info.engineRating;
	    		}
    		}
    		//if(!info.glassRating.equals("null")){
    			lVm.glassRating = info.glassRating;
    		//}
    		//if(!info.interiorRating.equals("null")){
    			lVm.interiorRating = info.interiorRating;
    		//}
    		//if(!info.exhaustRating.equals("null")){
    			lVm.exhaustRating = info.exhaustRating;
    		//}
    		//if(!info.leaseOrRental.equals("null")){
    			lVm.rentalReturn = info.leaseOrRental;
    		//}
    		//if(!info.operationalAndAccurate.equals("null")){
    			lVm.odometerAccurate = info.operationalAndAccurate;
    	//	}
    			lVm.serviceRecords = info.serviceRecord;
    		//if(!info.lienholder.equals("null")){
    			lVm.lienholder = info.lienholder;
    		//}
    			lVm.prefferedContact = info.preferredContact;
    		//if(!info.equipment.equals("null")){
    			lVm.equipment = info.equipment;
    		//}
    		//if(!info.accidents.equals("null")){
    			lVm.accidents = info.accidents;
    		//}
    		//if(!info.vehiclenew.equals("null")){
    			lVm.vehiclenew = info.vehiclenew;
    		//}
    		//if(!info.paint.equals("null")){
    			lVm.paint = info.paint;
    		//}
    		//if(!info.salvage.equals("null")){
    			lVm.salvage = info.salvage;
    		//}
    		//if(!info.damage.equals("null")){
    			lVm.damage = info.damage;
    		//}
	    			lVm.titleholder = info.holdsThisTitle;
	    			lVm.prefferedContact = info.preferredContact;
	    			
	    			
	    			List<String> sList = new ArrayList<>();
	    			String arr[] =  info.optionValue.split(",");
	    			for(int i=0;i<arr.length;i++){
	    				sList.add(arr[i]);
	    			}
	    			lVm.options = sList;
	    			
	    		vm.leadsValue = lVm;	
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
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
	    			}	    			list.add(obj);
	    		}
	    		vm.note = list;
	    		vm.noteFlag = 0;
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
	    		vm.option = 2;
	    		
	    		
	    		findTreadParentChildAndBro(infoVMList, info, df, vm);
	    		/*List<RequestInfoVM> rList2 = new ArrayList<>();
	    		if(info.parentId != null){
	    			TradeIn tIn = TradeIn.findByIdAndParent(info.parentId);
	    			if(tIn != null){
	    				RequestInfoVM rList1 = new RequestInfoVM();
	    				rList1.id = tIn.id;
	    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(tIn.vin);
	    	    		rList1.vin = tIn.vin;
	    	    		if(vehicle1 != null) {
	    	    			rList1.model = vehicle1.model;
	    	    			rList1.make = vehicle1.make;
	    	    			rList1.stock = vehicle1.stock;
	    	    			rList1.year = vehicle1.year;
	    	    			rList1.mileage = vehicle1.mileage;
	    	    			rList1.price = vehicle1.price;
	    	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	    	        		if(vehicleImage!=null) {
	    	        			rList1.imgId = vehicleImage.getId().toString();
	    	        		}
	    	        		else {
	    	        			rList1.imgId = "/assets/images/no-image.jpg";
	    	        		}
	    	    			
	    	    		}
	    	    		rList1.name = tIn.firstName;
	    	    		rList1.phone = tIn.phone;
	    	    		rList1.email = tIn.email;
	    	    		rList1.requestDate = df.format(tIn.tradeDate);
	    	    		rList1.typeOfLead = "Trade-In Appraisal";
	    	    		
	    	    		rList2.add(rList1);
	    			}
	    		}
	    		
	    		List<TradeIn> tIns = TradeIn.findAllByParentID(info.getId());
	    		for(TradeIn info1:tIns){
	    			RequestInfoVM rList1 = new RequestInfoVM();
    				rList1.id = info1.id;
    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    	    		rList1.vin = info1.vin;
    	    		if(vehicle1 != null) {
    	    			rList1.model = vehicle1.model;
    	    			rList1.make = vehicle1.make;
    	    			rList1.stock = vehicle1.stock;
    	    			rList1.year = vehicle1.year;
    	    			rList1.mileage = vehicle1.mileage;
    	    			rList1.price = vehicle1.price;
    	    			rList1.bodyStyle =vehicle1.bodyStyle;
    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
    	        		if(vehicleImage!=null) {
    	        			rList1.imgId = vehicleImage.getId().toString();
    	        		}
    	        		else {
    	        			rList1.imgId = "/assets/images/no-image.jpg";
    	        		}
    	    		}
    	    		rList1.name = info1.firstName;
    	    		rList1.phone = info1.phone;
    	    		rList1.email = info1.email;
    	    		rList1.requestDate = df.format(info1.tradeDate);
    	    		rList1.typeOfLead = "Trade-In Appraisal";
    	    		
    	    		rList2.add(rList1);
	    		}
	    		vm.parentChildLead = rList2;
	    		
	    		
	    		infoVMList.add(vm);*/
	    	}
	    	
	    	for(RequestMoreInfo info: requestMoreInfos) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
	    			vm.bodyStyle =vehicle.bodyStyle;
	    			vm.drivetrain = vehicle.drivetrain;
	    			vm.engine = vehicle.engine;
	    			vm.price = vehicle.price;
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
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		if(info.bestDay != null){
	    			/*String chaArr[] = info.bestDay.split("-");
	    			vm.bestDay = chaArr[1]+"/"+chaArr[2]+"/"+chaArr[0];*/
	    			vm.bestDay = info.bestDay;
	    		}
	    		vm.bestTime = info.bestTime;
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
	    		vm.option = 1;
	    		
	    		findRequestParentChildAndBro(infoVMList, info, df, vm);
	    		
	    		/*List<RequestInfoVM> rList2 = new ArrayList<>();
	    		if(info.parentId != null){
	    			RequestMoreInfo rMoreInfo = RequestMoreInfo.findByIdAndParent(info.parentId);
	    			if(rMoreInfo != null){
	    				RequestInfoVM rList1 = new RequestInfoVM();
	    				rList1.id = rMoreInfo.id;
	    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(rMoreInfo.vin);
	    	    		rList1.vin = rMoreInfo.vin;
	    	    		if(vehicle1 != null) {
	    	    			rList1.model = vehicle1.model;
	    	    			rList1.make = vehicle1.make;
	    	    			rList1.stock = vehicle1.stock;
	    	    			rList1.year = vehicle1.year;
	    	    			rList1.mileage = vehicle1.mileage;
	    	    			rList1.price = vehicle1.price;
	    	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	    	        		if(vehicleImage!=null) {
	    	        			rList1.imgId = vehicleImage.getId().toString();
	    	        		}
	    	        		else {
	    	        			rList1.imgId = "/assets/images/no-image.jpg";
	    	        		}
	    	    			
	    	    		}
	    	    		rList1.name = rMoreInfo.name;
	    	    		rList1.phone = rMoreInfo.phone;
	    	    		rList1.email = rMoreInfo.email;
	    	    		rList1.requestDate = df.format(rMoreInfo.requestDate);
	    	    		rList1.typeOfLead = "Request More Info";
	    	    		
	    	    		rList2.add(rList1);
	    			}
	    		}
	    		
	    		List<RequestMoreInfo> requestMoreInfo = RequestMoreInfo.findAllByParentID(info.getId());
	    		for(RequestMoreInfo info1:requestMoreInfo){
	    			RequestInfoVM rList1 = new RequestInfoVM();
    				rList1.id = info1.id;
    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    	    		rList1.vin = info1.vin;
    	    		if(vehicle1 != null) {
    	    			rList1.model = vehicle1.model;
    	    			rList1.make = vehicle1.make;
    	    			rList1.stock = vehicle1.stock;
    	    			rList1.year = vehicle1.year;
    	    			rList1.mileage = vehicle1.mileage;
    	    			rList1.price = vehicle1.price;
    	    			rList1.bodyStyle =vehicle1.bodyStyle;
    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
    	        		if(vehicleImage!=null) {
    	        			rList1.imgId = vehicleImage.getId().toString();
    	        		}
    	        		else {
    	        			rList1.imgId = "/assets/images/no-image.jpg";
    	        		}
    	    		}
    	    		rList1.name = info1.name;
    	    		rList1.phone = info1.phone;
    	    		rList1.email = info1.email;
    	    		rList1.requestDate = df.format(info1.requestDate);
    	    		rList1.typeOfLead = "Request More Info";
    	    		
    	    		rList2.add(rList1);
	    		}
	    		vm.parentChildLead = rList2;
	    		
	    		infoVMList.add(vm);*/
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}
    	
    	
    }
    
    
    public static Result getAllSalesPersonRequestInfoSeen(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user;
    		if(id == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(id);
    		}
    			
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	for(RequestMoreInfo info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.typeofVehicle=vehicle.typeofVehicle;
	    			vm.stock = vehicle.stock;
	    			vm.mileage = vehicle.mileage;
	    			vm.year = vehicle.year;
	    			vm.bodyStyle =vehicle.bodyStyle;
	    			vm.drivetrain = vehicle.drivetrain;
	    			vm.engine = vehicle.engine;
	    			vm.transmission = vehicle.transmission;
	    			vm.price = vehicle.price;
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
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.price = vehicle.price;
	    		vm.requestDate = df.format(info.requestDate);
	    		vm.typeOfLead = "Request More Info";
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
	    		
	    		findRequestParentChildAndBro(infoVMList, info, df, vm);
	    		
	    		/*List<RequestInfoVM> rList2 = new ArrayList<>();
	    		if(info.parentId != null){
	    			RequestMoreInfo rMoreInfo = RequestMoreInfo.findByIdAndParent(info.parentId);
	    			if(rMoreInfo != null){
	    				RequestInfoVM rList1 = new RequestInfoVM();
	    				rList1.id = rMoreInfo.id;
	    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(rMoreInfo.vin);
	    	    		rList1.vin = rMoreInfo.vin;
	    	    		if(vehicle1 != null) {
	    	    			rList1.model = vehicle1.model;
	    	    			rList1.make = vehicle1.make;
	    	    			rList1.stock = vehicle1.stock;
	    	    			rList1.year = vehicle1.year;
	    	    			rList1.mileage = vehicle1.mileage;
	    	    			rList1.price = vehicle1.price;
	    	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	    	        		if(vehicleImage!=null) {
	    	        			rList1.imgId = vehicleImage.getId().toString();
	    	        		}
	    	        		else {
	    	        			rList1.imgId = "/assets/images/no-image.jpg";
	    	        		}
	    	    			
	    	    		}
	    	    		rList1.name = rMoreInfo.name;
	    	    		rList1.phone = rMoreInfo.phone;
	    	    		rList1.email = rMoreInfo.email;
	    	    		rList1.requestDate = df.format(rMoreInfo.requestDate);
	    	    		rList1.typeOfLead = "Request More Info";
	    	    		
	    	    		rList2.add(rList1);
	    			}
	    		}
	    		
	    		List<RequestMoreInfo> requestMoreInfo = RequestMoreInfo.findAllByParentID(info.getId());
	    		for(RequestMoreInfo info1:requestMoreInfo){
	    			RequestInfoVM rList1 = new RequestInfoVM();
    				rList1.id = info1.id;
    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    	    		rList1.vin = info1.vin;
    	    		if(vehicle1 != null) {
    	    			rList1.model = vehicle1.model;
    	    			rList1.make = vehicle1.make;
    	    			rList1.stock = vehicle1.stock;
    	    			rList1.year = vehicle1.year;
    	    			rList1.mileage = vehicle1.mileage;
    	    			rList1.price = vehicle1.price;
    	    			rList1.bodyStyle =vehicle1.bodyStyle;
    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
    	        		if(vehicleImage!=null) {
    	        			rList1.imgId = vehicleImage.getId().toString();
    	        		}
    	        		else {
    	        			rList1.imgId = "/assets/images/no-image.jpg";
    	        		}
    	    		}
    	    		rList1.name = info1.name;
    	    		rList1.phone = info1.phone;
    	    		rList1.email = info1.email;
    	    		rList1.requestDate = df.format(info1.requestDate);
    	    		rList1.typeOfLead = "Request More Info";
    	    		
    	    		rList2.add(rList1);
	    		}
	    		vm.parentChildLead = rList2;
	    		infoVMList.add(vm);*/
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    
    	
    }
    
    
    public static Result getAllSalesPersonTradeInSeen(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user;
    		if(id == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(id);
    		}
	    	List<TradeIn> listData = TradeIn.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	for(TradeIn info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.typeofVehicle=vehicle.typeofVehicle;
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    			vm.year =vehicle.year;
	    			vm.mileage =vehicle.mileage;
	    			vm.bodyStyle =vehicle.bodyStyle;
	    			vm.drivetrain = vehicle.drivetrain;
	    			vm.engine = vehicle.engine;
	    			vm.price = vehicle.price;
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
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		vm.pdfPath = info.pdfPath;
	    		vm.requestDate = df.format(info.tradeDate);
	    		vm.typeOfLead = "Trade-In Appraisal";
	    		
	    		LeadVM lVm = new LeadVM();
	    		lVm.id = info.id.toString();
	    		if(!info.comments.equals("null")){
	    			lVm.comments = info.comments;
	    		}
	    		if(!info.year.equals("null")){
	    			lVm.year = info.year;
	    		}
	    		if(info.make != null){
	    			lVm.make = info.make;
	    		}
	    		if(info.model != null){
	    			lVm.model = info.model;
	    		}
	    		if(!info.exteriorColour.equals("null")){
	    			lVm.exteriorColour = info.exteriorColour;
	    		}
	    		if(!info.kilometres.equals("null")){
	    			lVm.kilometres = info.kilometres;
	    		}
	    		if(!info.engine.equals("null")){
	    			lVm.engine = info.engine;
	    		}
	    		if(!info.doors.equals("null")){
	    			lVm.doors = info.doors;
	    		}
	    		if(!info.transmission.equals("null")){
	    			lVm.transmission = info.transmission;
	    		}
	    		if(!info.drivetrain.equals("null")){
	    			lVm.drivetrain = info.drivetrain;
	    		}
	    		if(!info.bodyRating.equals("null")){
	    			lVm.bodyRating = info.bodyRating;
	    		}
	    		if(!info.tireRating.equals("null")){
	    			lVm.tireRating = info.tireRating;
	    		}
	    		if(!info.engineRating.equals("null")){
	    			lVm.engineRating = info.engineRating;
	    		}
	    		if(!info.glassRating.equals("null")){
	    			lVm.glassRating = info.glassRating;
	    		}
	    		if(!info.interiorRating.equals("null")){
	    			lVm.interiorRating = info.interiorRating;
	    		}
	    		if(!info.exhaustRating.equals("null")){
	    			lVm.exhaustRating = info.exhaustRating;
	    		}
	    		if(!info.leaseOrRental.equals("null")){
	    			lVm.rentalReturn = info.leaseOrRental;
	    		}
	    		if(!info.operationalAndAccurate.equals("null")){
	    			lVm.odometerAccurate = info.operationalAndAccurate;
	    		}
	    			lVm.serviceRecords = info.serviceRecord;
	    		if(!info.lienholder.equals("null")){
	    			lVm.lienholder = info.lienholder;
	    		}
	    			lVm.prefferedContact = info.preferredContact;
	    		if(!info.equipment.equals("null")){
	    			lVm.equipment = info.equipment;
	    		}
	    		if(!info.accidents.equals("null")){
	    			lVm.accidents = info.accidents;
	    		}
	    		if(!info.vehiclenew.equals("null")){
	    			lVm.vehiclenew = info.vehiclenew;
	    		}
	    		if(!info.paint.equals("null")){
	    			lVm.paint = info.paint;
	    		}
	    		if(!info.salvage.equals("null")){
	    			lVm.salvage = info.salvage;
	    		}
	    		if(!info.damage.equals("null")){
	    			lVm.damage = info.damage;
	    		}
	    			lVm.titleholder = info.holdsThisTitle;
	    			lVm.prefferedContact = info.preferredContact;
    			List<String> sList = new ArrayList<>();
    			String arr[] =  info.optionValue.split(",");
    			for(int i=0;i<arr.length;i++){
    				sList.add(arr[i]);
    			}
    			lVm.options = sList;
    		vm.leadsValue = lVm;
    		
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
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
	    		vm.requestDate = df.format(info.tradeDate);
	    		if(info.isRead == 0) {
	    			vm.isRead = false;
	    		}
	    		
	    		if(info.isRead == 1) {
	    			vm.isRead = true;
	    		}
	    		
	    		findTreadParentChildAndBro(infoVMList, info, df, vm);
	    		
	    		/*List<RequestInfoVM> rList2 = new ArrayList<>();
	    		if(info.parentId != null){
	    			TradeIn tIn = TradeIn.findByIdAndParent(info.parentId);
	    			if(tIn != null){
	    				RequestInfoVM rList1 = new RequestInfoVM();
	    				rList1.id = tIn.id;
	    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(tIn.vin);
	    	    		rList1.vin = tIn.vin;
	    	    		if(vehicle1 != null) {
	    	    			rList1.model = vehicle1.model;
	    	    			rList1.make = vehicle1.make;
	    	    			rList1.stock = vehicle1.stock;
	    	    			rList1.year = vehicle1.year;
	    	    			rList1.mileage = vehicle1.mileage;
	    	    			rList1.price = vehicle1.price;
	    	    			rList1.bodyStyle =vehicle1.bodyStyle;
	    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
	    	        		if(vehicleImage!=null) {
	    	        			rList1.imgId = vehicleImage.getId().toString();
	    	        		}
	    	        		else {
	    	        			rList1.imgId = "/assets/images/no-image.jpg";
	    	        		}
	    	    			
	    	    		}
	    	    		rList1.name = tIn.firstName;
	    	    		rList1.phone = tIn.phone;
	    	    		rList1.email = tIn.email;
	    	    		rList1.requestDate = df.format(tIn.tradeDate);
	    	    		rList1.typeOfLead = "Trade-In Appraisal";
	    	    		
	    	    		rList2.add(rList1);
	    			}
	    		}
	    		
	    		List<TradeIn> tIns = TradeIn.findAllByParentID(info.getId());
	    		for(TradeIn info1:tIns){
	    			RequestInfoVM rList1 = new RequestInfoVM();
    				rList1.id = info1.id;
    	    		Vehicle vehicle1 = Vehicle.findByVinAndStatus(info1.vin);
    	    		rList1.vin = info1.vin;
    	    		if(vehicle1 != null) {
    	    			rList1.model = vehicle1.model;
    	    			rList1.make = vehicle1.make;
    	    			rList1.stock = vehicle1.stock;
    	    			rList1.year = vehicle1.year;
    	    			rList1.mileage = vehicle1.mileage;
    	    			rList1.price = vehicle1.price;
    	    			rList1.bodyStyle =vehicle1.bodyStyle;
    	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle1.vin);
    	        		if(vehicleImage!=null) {
    	        			rList1.imgId = vehicleImage.getId().toString();
    	        		}
    	        		else {
    	        			rList1.imgId = "/assets/images/no-image.jpg";
    	        		}
    	    		}
    	    		rList1.name = info1.firstName;
    	    		rList1.phone = info1.phone;
    	    		rList1.email = info1.email;
    	    		rList1.requestDate = df.format(info1.tradeDate);
    	    		rList1.typeOfLead = "Trade-In Appraisal";
    	    		
    	    		rList2.add(rList1);
	    		}
	    		vm.parentChildLead = rList2;
	    		
	    		infoVMList.add(vm);*/
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    	
    	
    }
    public static Result getWeekChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -7);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList, df.format(date), df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getMonthChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -30);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,df.format(date),df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getThreeMonthChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -90);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,df.format(date),df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getSixMonthChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -180);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,df.format(date),df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getYearChartData(Integer id) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		cal.add(Calendar.DATE, -365);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,df.format(date),df.format(cal.getTime()));
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getRangeChartData(Integer id,String start,String end) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Calendar cal = Calendar.getInstance();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		List<AuthUser> usersList = new ArrayList<>();
    		if(id == 0) {
    			usersList = AuthUser.getAllUsers();
    		} else {
    			AuthUser userData = AuthUser.findById(id);
    			usersList.add(userData);
    		}
    		List<SqlRow> vehicleList = Vehicle.getSoldDataOfRange(user, usersList,end,start);
    		List<BarChartVM> data = new ArrayList<>();
    		BarChartVM price = new BarChartVM();
    		List<List> priceListData = new ArrayList<>();
    		
    		for(SqlRow vehicle: vehicleList) {
    			List l = new ArrayList();
    			Date dt = (Date)vehicle.get("sold_date");
    			cal.setTime(dt);
    			l.add(cal.getTimeInMillis());
    			l.add(Float.parseFloat(vehicle.getString("sum(vehicle.price)")));
    			priceListData.add(l);
    		}
    		price.key = "Quantity";
    		price.bar = true;
    		price.values = priceListData;
    		data.add(price);
    		return ok(Json.toJson(data));
    	}
    }
    
    public static Result getPerformanceOfUser(String top,String worst,String week,String month,String year,String allTime,Integer id,Long locationValue) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
    		String start1 = "";
			String end1 = "";
			Date start = null;
			Date end = null;
    		if(week.equals("true")) {
    			cal.add(Calendar.DATE, -7);
    			start1 = df.format(cal.getTime());
    			end1 = df.format(date);
    			try {
					start = df.parse(start1);
					end = df.parse(end1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(month.equals("true")) {
				cal.add(Calendar.DATE, -30);
				start1 = df.format(cal.getTime());
    			end1 = df.format(date);
    			try {
					start = df.parse(start1);
					end = df.parse(end1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(year.equals("true")) {
				cal.add(Calendar.DATE, -365);
				start1 = df.format(cal.getTime());
    			end1 = df.format(date);
    			try {
					start = df.parse(start1);
					end = df.parse(end1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(allTime.equals("true")) {
				cal.add(Calendar.DATE, -1000);
				start1 = df.format(cal.getTime());
    			end1 = df.format(date);
    			try {
					start = df.parse(start1);
					end = df.parse(end1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			int requestLeadCount = 0;
	    	int scheduleLeadCount = 0;
	    	int tradeInLeadCount = 0;
	    	
	    	int requestLeadCount1 = 0;
	    	int scheduleLeadCount1 = 0;
	    	int tradeInLeadCount1 = 0;
	    	
	    	List<RequestMoreInfo> rInfo = null;
	    	List<ScheduleTest> sList = null;
	    	List<TradeIn> tradeIns = null;
	    	List<RequestMoreInfo> rInfoAll = null;
	    	List<ScheduleTest> sListAll = null;
	    	List<TradeIn> tradeInsAll = null;
			
			List<UserVM> userList = new ArrayList<>();
			
			List<AuthUser> salesUsersList;
			if(locationValue == 0){
				salesUsersList = AuthUser.getAllSalesUser();
			}else{
				salesUsersList = AuthUser.getAllUserByLocation(Location.findById(locationValue));
			}
			
			UserVM[] tempuserList = new UserVM[salesUsersList.size()];
			int index=0;
			    				
			for(AuthUser sales: salesUsersList) {
				
				requestLeadCount = 0;
		    	scheduleLeadCount = 0;
		    	tradeInLeadCount = 0;
		    	
		    	requestLeadCount1 = 0;
		    	scheduleLeadCount1 = 0;
		    	tradeInLeadCount1 = 0;
		    	
		    	rInfo = null;
		    	sList = null;
		    	tradeIns = null;
		    	rInfoAll = null;
		    	sListAll = null;
		    	tradeInsAll = null;
		    	Integer total = 0;
				UserVM vm = new UserVM();
				SimpleDateFormat dform = new SimpleDateFormat("MMMM");

				String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
				        "August", "September", "October", "November", "December" };
		    	
		     	String crMonth = monthName[cal.get(Calendar.MONTH)];
		     	
				PlanScheduleMonthlySalepeople  pMonthlySalepeople = null;
				pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByUserMonth(sales, crMonth);
				
				if(pMonthlySalepeople != null){
					total = Integer.parseInt(pMonthlySalepeople.totalBrought);
		    	}
			
				vm.fullName = sales.firstName+" "+sales.lastName;
				vm.userStatus = sales.account;
				vm.id = sales.id;
				if(sales.imageUrl != null) {
					if(sales.imageName !=null){
						vm.imageUrl = "http://glider-autos.com/glivrImg/images"+sales.imageUrl;
					}else{
						vm.imageUrl = sales.imageUrl;
					}
					
				} else {
					vm.imageUrl = "/profile-pic.jpg";
				}
				
				rInfo = RequestMoreInfo.findAllSeenSch(sales);
	    		sList = ScheduleTest.findAllAssigned(sales);
	    		tradeIns = TradeIn.findAllSeenSch(sales);
	    		
	    		rInfoAll = RequestMoreInfo.findByAssigUserNotCancel(sales);
	    		sListAll = ScheduleTest.findByAssigUserNotCancel(sales);
	    		tradeInsAll = TradeIn.findByAssigUserNotCancel(sales);
	    	
	    	
	    	for(RequestMoreInfo rMoreInfo:rInfo){
	    		if((rMoreInfo.requestDate.after(start) && rMoreInfo.requestDate.before(end)) || rMoreInfo.requestDate.equals(end)){
	    			requestLeadCount++;
	    		}
	    	}
	    	
	    	
	    	for(ScheduleTest sTest:sList){
	    		if((sTest.scheduleDate.after(start) && sTest.scheduleDate.before(end)) || sTest.scheduleDate.equals(end)){
	    			scheduleLeadCount++;
	    		}
	    	}

	    	for(TradeIn tIn:tradeIns){
	    		if((tIn.tradeDate.after(start) && tIn.tradeDate.before(end)) || tIn.tradeDate.equals(end)){
	    				tradeInLeadCount++;
	    		}
	    	}
	    	
	    	for(RequestMoreInfo rMoreInfo:rInfoAll){
	    		if(start != null && end !=null){
	    		if((rMoreInfo.requestDate.after(start) && rMoreInfo.requestDate.before(end)) || rMoreInfo.requestDate.equals(end)){
	    			requestLeadCount1++;
	    		}
	    		}
	    	}
	    	
	    	
	    	for(ScheduleTest sTest:sListAll){
	    		if(start != null && end !=null){
	    		if((sTest.scheduleDate.after(start) && sTest.scheduleDate.before(end)) || sTest.scheduleDate.equals(end)){
	    			scheduleLeadCount1++;
	    		}
	    		}
	    	}

	    	for(TradeIn tIn:tradeInsAll){
	    		if(start != null && end !=null){
	    		if((tIn.tradeDate.after(start) && tIn.tradeDate.before(end)) || tIn.tradeDate.equals(end)){
					tradeInLeadCount1++;
	    		}
	    		}
	    	}
				
	    	int countLeads1 = requestLeadCount1 + scheduleLeadCount1 + tradeInLeadCount1;
	    	int countLeads = requestLeadCount + scheduleLeadCount + tradeInLeadCount;
	    	int saleCarCount = 0;
	    	Long pricecount = 0l;
	    	int per = 0;
	    	List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(sales);
    		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(sales);
    		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(sales);
    		List<Vehicle> salesVehicleList = Vehicle.findBySoldUserAndSold(sales);
    		for (Vehicle vehicle : salesVehicleList) {
    			if(vehicle != null){
    				if((vehicle.soldDate.after(start) && vehicle.soldDate.before(end)) || vehicle.soldDate.equals(end)){
            			saleCarCount++;
            			pricecount = pricecount + vehicle.price;
    				}
    			}
			}
    		
    		double sucessCount = 0;
    		if(countLeads1 != 0){
    			sucessCount= (double)saleCarCount/(double)countLeads1*100;
    		}else{
    			 sucessCount = 0;
    		}
    		
			if(pricecount > 0 && total > 0){
				per = (int) ((pricecount*100)/total);
				vm.per = per;
			}
    		vm.successRate = (int) sucessCount;
    		vm.salesAmount = pricecount;
    		vm.currentLeads = Long.parseLong(String.valueOf(countLeads));
			vm.saleCar = Long.parseLong(String.valueOf(saleCarCount));
			vm.email = sales.communicationemail;
				tempuserList[index] = vm;
				index++;
								
			}
			
    		if(top.equals("true")) {
    			if(id == 0) {
    				
    				for(int i=0;i<tempuserList.length-1;i++) {
    					for(int j=i+1;j<tempuserList.length;j++) {
    						if(tempuserList[i].successRate <= tempuserList[j].successRate) {
    							UserVM temp = tempuserList[i];
    							tempuserList[i] = tempuserList[j];
    							tempuserList[j] = temp;
    						}
    					}
    				}
    				int iValue = 0;
    				for(int i=0;i<tempuserList.length;i++) {
    					
    						userList.add(tempuserList[i]);
    				}
    				
    			  }	
    			
    		
    		}
    		
    		if(worst.equals("true")) {
    			if(id == 0) {
    				for(int i=0;i<tempuserList.length-1;i++) {
    					for(int j=i+1;j<tempuserList.length;j++) {
    						if(tempuserList[i].successRate >= tempuserList[j].successRate) {
    							UserVM temp = tempuserList[i];
    							tempuserList[i] = tempuserList[j];
    							tempuserList[j] = temp;
    						}
    					}
    				}
    				int iValue = 0;
    				for(int i=0;i<tempuserList.length;i++) {
    					
    						userList.add(tempuserList[i]);
    				}
    			}
    		}	
    		
    		/*if(worst.equals("true")) {
    			if(id == 0) {
    				List<AuthUser> salesUsersList = AuthUser.getAllSalesUser();
    				UserVM[] tempuserList = new UserVM[salesUsersList.size()];
    				int index = 0;
    				for(AuthUser sales: salesUsersList) {
    					SqlRow rowData = ScheduleTest.getTopPerformers(start, end, sales.id);
        				UserVM vm = new UserVM();
        				vm.fullName = sales.firstName+" "+sales.lastName;
        				if(sales.imageUrl != null) {
        					if(sales.imageName !=null){
        						vm.imageUrl = "http://glider-autos.com/glivrImg/images"+sales.imageUrl;
        					}else{
        						vm.imageUrl = sales.imageUrl;
        					}
        				} else {
        					vm.imageUrl = "/profile-pic.jpg";
        				}
        				if(rowData.getInteger("success") != null && rowData.getInteger("total") != null && rowData.getInteger("total") != 0) {
        					vm.successRate = rowData.getInteger("success")*(100/rowData.getInteger("total"));
        				} else {
        					vm.successRate = 0;
        				}
        				Integer leads = 0;
        				String count = "";
        				if(rowData.getString("leads") != null) {
        					count = rowData.getString("leads");
        					leads = Integer.parseInt(count);
        					if(rowData.getString("requestleads") != null) {
        						leads = leads + Integer.parseInt(rowData.getString("requestleads"));
        					}
        					if(rowData.getString("tradeInleads") != null) {
        						leads = leads + Integer.parseInt(rowData.getString("tradeInleads"));
        					}
        					vm.currentLeads = leads.toString();
        				} else {
        					vm.currentLeads = "";
        				}
        				if(rowData.getString("amount") != null) {
        					vm.salesAmount = rowData.getString("amount");
        				} else {
        					vm.salesAmount = "0";
        				}
        				tempuserList[index] = vm;
        				index++;
    				}
    				
    				for(int i=0;i<tempuserList.length-1;i++) {
    					for(int j=i+1;j<tempuserList.length;j++) {
    						if(tempuserList[i].successRate >= tempuserList[j].successRate) {
    							UserVM temp = tempuserList[i];
    							tempuserList[i] = tempuserList[j];
    							tempuserList[j] = temp;
    						}
    					}
    				}
    				
    				for(int i=0;i<tempuserList.length;i++) {
    					userList.add(tempuserList[i]);
    				}
    				
    				if(tempuserList.length >=1){
    					userList.add(tempuserList[0]);
    				}	
    				if(tempuserList.length >=2){
    					userList.add(tempuserList[1]);
    				}
    				if(tempuserList.length >=3){
    					userList.add(tempuserList[2]);
    				}	
    				
    			}
    			
    			if(id != 0) {
    				AuthUser salesUser = AuthUser.findById(id);
    				SqlRow rowData = ScheduleTest.getTopPerformers(start, end, id);
    				UserVM vm = new UserVM();
    				vm.fullName = salesUser.firstName+" "+salesUser.lastName;
    				if(salesUser.imageUrl != null) {
    					if(salesUser.imageName !=null){
    						vm.imageUrl = "http://glider-autos.com/glivrImg/images"+salesUser.imageUrl;
    					}else{
    						vm.imageUrl = salesUser.imageUrl;
    					}
    				} else {
    					vm.imageUrl = "/profile-pic.jpg";
    				}
    				if(rowData.getInteger("success") != null && rowData.getInteger("total") != null && rowData.getInteger("total") != 0) {
    					vm.successRate = rowData.getInteger("success")*(100/rowData.getInteger("total"));
    				} else {
    					vm.successRate = 0;
    				}
    				Integer leads = 0;
    				String count = "";
    				if(rowData.getString("leads") != null) {
    					count = rowData.getString("leads");
    					leads = Integer.parseInt(count);
    					if(rowData.getString("requestleads") != null) {
    						leads = leads + Integer.parseInt(rowData.getString("requestleads"));
    					}
    					if(rowData.getString("tradeInleads") != null) {
    						leads = leads + Integer.parseInt(rowData.getString("tradeInleads"));
    					}
    					vm.currentLeads = leads.toString();
    				} else {
    					vm.currentLeads = "";
    				}
    				if(rowData.getString("amount") != null) {
    					vm.salesAmount = rowData.getString("amount");
    				} else {
    					vm.salesAmount = "0";
    				}
    				userList.add(vm);
    			}
    		}*/
    		
    		
    		return ok(Json.toJson(userList));
    	}
    }
    
    public static Result saveNoteOfUser(Long id,String type,String note,String action) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		if(type.equalsIgnoreCase("requestMore")) {
    			RequestMoreInfo requestMore = RequestMoreInfo.findById(id);
    			UserNotes notes = new UserNotes();
    			notes.note = note;
    			notes.action = action;
    			notes.requestMoreInfo = requestMore;
    			notes.saveHistory = 1;
    			if(requestMore.assignedTo !=null){
    				notes.user = requestMore.assignedTo;
    			}/*else{
    				notes.user = user;
    			}*/
    			
    			Date date = new Date();
    			notes.createdDate = date;
    			notes.createdTime = date;
				notes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    			notes.save();
    		}
    		if(type.equalsIgnoreCase("scheduleTest")) {
    			ScheduleTest scheduleTest = ScheduleTest.findById(id);
    			UserNotes notes = new UserNotes();
    			notes.note = note;
    			notes.action = action;
    			notes.scheduleTest = scheduleTest;
    			notes.saveHistory = 1;
    			AuthUser usr = AuthUser.findById(scheduleTest.assignedTo.id);
    			if(scheduleTest.assignedTo != null){
    				notes.user = scheduleTest.assignedTo;
    			}/*else{
    				notes.user = user;
    			}*/
    			Date date = new Date();
    			notes.createdDate = date;
    			notes.createdTime = date;
				notes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    			notes.save();
    			
    			//scheduleTest.setNoteFlag(1);
    			scheduleTest.update();
    		}
    		if(type.equalsIgnoreCase("tradeIn")) {
    			TradeIn tradeIn = TradeIn.findById(id);
    			UserNotes notes = new UserNotes();
    			notes.note = note;
    			notes.action = action;
    			notes.tradeIn = tradeIn;
    			notes.saveHistory = 1;
    			if(tradeIn.assignedTo !=null){
    				notes.user = tradeIn.assignedTo;
    			}/*else{
    				notes.user = user;
    			}*/
    			Date date = new Date();
    			notes.createdDate = date;
    			notes.createdTime = date;
				notes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    			notes.save();
    		}
    		return ok();
    	}
    }
    
    public static Result getAllAction(){
    	List<ActionAdd> actionAdd = ActionAdd.getAll();
    	return ok(Json.toJson(actionAdd));
    }
    
    public static Result saveAction(String actionV){
    	ActionAdd actionAdd = new ActionAdd();
    	actionAdd.value = actionV;
    	actionAdd.save();
    	return ok();
    }
    
    public static Result saveTestDrive() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		
    		String msg = "success";
    		boolean flag = true;
    		int flagFirstComp = 0;
    		AuthUser user = getLocalUser();
    		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		request().body().asJson();
    		Form<RequestInfoVM> form = DynamicForm.form(RequestInfoVM.class).bindFromRequest();
    		RequestInfoVM vm = form.get();
    		Date confirmDate = null;
    		//Vehicle obj = Vehicle.findByVinAndStatus(vm.vin);
    		
    			schTestDrive(vm, msg, flag, confirmDate);
    			
    			if(flag){
        			Map map = new HashMap();
                	map.put("email",vm.email);
                	map.put("confirmDate", vm.bestDay);
                	map.put("confirmTime", vm.bestTime);
                	map.put("vin", vm.vin);
                	map.put("uname", user.firstName+" "+user.lastName);
                	map.put("uphone", user.phone);
                	map.put("uemail", user.email);
                	//makeToDo(vm.vin);
                	sendMail(map);
        		}
    			
    			if(vm.parentChildLead != null){
    				for(RequestInfoVM rVm:vm.parentChildLead){
    					rVm.option = vm.option;
    					msg = "success";
    		    		flag = true;
    					schTestDrive(rVm, msg, flag, confirmDate);
    					if(rVm.bestDay != null && !rVm.bestDay.equals("")){
    						if(flag){
        		    			Map map = new HashMap();
        		            	map.put("email",vm.email);
        		            	map.put("confirmDate", rVm.bestDay);
        		            	map.put("confirmTime", rVm.bestTime);
        		            	map.put("vin", rVm.vin);
        		            	map.put("uname", user.firstName+" "+user.lastName);
        		            	map.put("uphone", user.phone);
        		            	map.put("uemail", user.email);
        		            	//makeToDo(vm.vin);
        		            	sendMail(map);
        		    		}
    					}
    				}
    				
    			}
    			
    		
    		/*if(flag){
    			Map map = new HashMap();
            	map.put("email",vm.email);
            	map.put("email",vm.email);
            	map.put("confirmDate", confirmDate);
            	map.put("confirmTime", vm.bestTime);
            	map.put("vin", vm.vin);
            	map.put("uname", user.firstName+" "+user.lastName);
            	map.put("uphone", user.phone);
            	map.put("uemail", user.email);
            	//makeToDo(vm.vin);
            	sendMail(map);
    		}*/
        	
    		return ok(msg);
    	}
    }
    
   public static void schTestDrive(RequestInfoVM vm, String msg,boolean flag,Date confirmDate){
	   AuthUser user = getLocalUser();
	   Date currDate = new Date();
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
		
		if(vm.bestDay != null && !vm.bestDay.equals("")){
			
	   if(vm.typeOfLead.equals("Request More Info")) {
			RequestMoreInfo requestMoreInfo = RequestMoreInfo.findById(vm.id);
			requestMoreInfo.setName(vm.name);
			requestMoreInfo.setEmail(vm.email);
			requestMoreInfo.setPhone(vm.phone);
			requestMoreInfo.setBestDay(vm.bestDay);
			try {
				confirmDate = df.parse(vm.bestDay);
				requestMoreInfo.setConfirmDate(confirmDate);
				requestMoreInfo.setConfirmTime(parseTime.parse(vm.bestTime));
				List<RequestMoreInfo> list = RequestMoreInfo.findByVin(vm.vin);
				List<TradeIn> list1 = TradeIn.findByVin(vm.vin);
				List<ScheduleTest> list2 = ScheduleTest.findByVin(vm.vin);
				Date date = parseTime.parse(vm.bestTime);
				
				for (RequestMoreInfo info2 : list) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
				for (TradeIn info2 : list1) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
				
				for (ScheduleTest info2 : list2) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
			} catch(Exception e) {}
			requestMoreInfo.setBestTime(vm.bestTime);
			requestMoreInfo.setPreferredContact(vm.prefferedContact);
			requestMoreInfo.setVin(vm.vin);
			requestMoreInfo.setScheduleDate(new Date());
			requestMoreInfo.setUser(user);
			requestMoreInfo.setIsScheduled(true);
			if(msg.equals("success")){
				requestMoreInfo.update();
				
				UserNotes uNotes = new UserNotes();
	    		uNotes.setNote("Test Drive Scheduled");
	    		uNotes.setAction("Other");
	    		uNotes.createdDate = currDate;
	    		uNotes.createdTime = currDate;
	    		uNotes.user = user;
       		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		uNotes.requestMoreInfo = RequestMoreInfo.findById(requestMoreInfo.id);
	    		uNotes.save();
			}
		} else if(vm.typeOfLead.equals("Trade-In Appraisal")) {
			TradeIn tradeIn = TradeIn.findById(vm.id);
			tradeIn.setFirstName(vm.name);
			tradeIn.setEmail(vm.email);
			tradeIn.setPhone(vm.phone);
			tradeIn.setBestDay(vm.bestDay);
			tradeIn.setBestTime(vm.bestTime);
			try {
				confirmDate = df.parse(vm.bestDay);
				tradeIn.setConfirmDate(confirmDate);
				tradeIn.setConfirmTime(parseTime.parse(vm.bestTime));
				
				List<RequestMoreInfo> list = RequestMoreInfo.findByVin(vm.vin);
				List<TradeIn> list1 = TradeIn.findByVin(vm.vin);
				List<ScheduleTest> list2 = ScheduleTest.findByVin(vm.vin);
				Date date = parseTime.parse(vm.bestTime);
				
				for (RequestMoreInfo info2 : list) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
				for (TradeIn info2 : list1) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
				
				for (ScheduleTest info2 : list2) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
			} catch(Exception e) {}
			tradeIn.setPreferredContact(vm.prefferedContact);
			tradeIn.setVin(vm.vin);
			tradeIn.setScheduleDate(new Date());
			tradeIn.setUser(user);
			tradeIn.setIsScheduled(true);
			if(msg.equals("success")){
				tradeIn.update();
				
				UserNotes uNotes = new UserNotes();
	    		uNotes.setNote("Test Drive Scheduled");
	    		uNotes.setAction("Other");
	    		uNotes.createdDate = currDate;
	    		uNotes.createdTime = currDate;
	    		uNotes.user = user;
	    		uNotes.tradeIn = tradeIn.findById(tradeIn.id);
	    		uNotes.save();
			}
		} else if(vm.typeOfLead.equals("Schedule Test Drive")) {
			ScheduleTest scTest = ScheduleTest.findById(vm.id);
			scTest.setName(vm.name);
			scTest.setEmail(vm.email);
			scTest.setPhone(vm.phone);
			scTest.setBestDay(vm.bestDay);
			scTest.setBestTime(vm.bestTime);
			try {
				confirmDate = df.parse(vm.bestDay);
				scTest.setConfirmDate(confirmDate);
				scTest.setConfirmTime(parseTime.parse(vm.bestTime));
				
				List<RequestMoreInfo> list = RequestMoreInfo.findByVin(vm.vin);
				List<TradeIn> list1 = TradeIn.findByVin(vm.vin);
				List<ScheduleTest> list2 = ScheduleTest.findByVin(vm.vin);
				Date date = parseTime.parse(vm.bestTime);
				
				for (RequestMoreInfo info2 : list) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
				for (TradeIn info2 : list1) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
				
				for (ScheduleTest info2 : list2) {
					if(info2.confirmDate != null && info2.confirmTime !=null){
						if(info2.confirmDate.equals(confirmDate)){
							Date newDate = DateUtils.addHours(info2.confirmTime, 1);
							if((date.after(info2.confirmTime) && date.before(newDate)) || date.equals(info2.confirmTime)){
								msg = "error";
								flag = false;
							}
						}
					}
				}
				
			} catch(Exception e) {}
			scTest.setPreferredContact(vm.prefferedContact);
			scTest.setVin(vm.vin);
			scTest.setScheduleDate(new Date());
			scTest.setUser(user);
			//scTest.setIsScheduled(true);
			if(msg.equals("success")){
				scTest.update();
				
				UserNotes uNotes = new UserNotes();
	    		uNotes.setNote("Test Drive Scheduled");
	    		uNotes.setAction("Other");
	    		uNotes.createdDate = currDate;
	    		uNotes.createdTime = currDate;
	    		uNotes.user = user;
	    		uNotes.scheduleTest = scTest;
	    		uNotes.save();
			}
		}
     }
   }
    
    public static Result getAllCanceledLeads() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	List<ScheduleTest> listData = ScheduleTest.getAllFailed(Long.valueOf(session("USER_LOCATION")));
    		
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
	    		vm.status = info.reason;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}	
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		if(info.scheduleDate != null){
	    			vm.requestDate = df.format(info.scheduleDate);
	    		}
	    		
	    		/*List<UserNotes> notesList = UserNotes.findScheduleTestByUser(info, info.assignedTo);*/
	    		List<UserNotes> notesList = UserNotes.findScheduleTest(info);
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
	    		vm.typeOfLead = "Schedule Test";
	    		infoVMList.add(vm);
	    	}
	    	
	    	List<RequestMoreInfo> requestData = RequestMoreInfo.findAllCancel(Long.valueOf(session("USER_LOCATION")));
	    	for(RequestMoreInfo info: requestData) {
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
	    		vm.status = info.reason;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		
	    		//List<UserNotes> notesList = UserNotes.findRequestMoreByUser(info, info.assignedTo);
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
	    		vm.typeOfLead = "Request More Info";
	    		infoVMList.add(vm);
	    	}
	    	
	    	List<TradeIn> tradeInData = TradeIn.findAllCanceled(Long.valueOf(session("USER_LOCATION")));
	    	for(TradeIn info: tradeInData) {
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
	    		vm.status = info.reason;
	    		if(info.statusDate != null){
	    			vm.statusDate = df.format(info.statusDate);
	    		}
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		
	    		//List<UserNotes> notesList = UserNotes.findTradeInByUser(info, info.assignedTo);
	    		List<UserNotes> notesList = UserNotes.findTradeIn(info);
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
	    		vm.typeOfLead = "Trade In";
	    		infoVMList.add(vm);
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}
    }
    
    public static Result releaseLeads(Long id,String leadType) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		if(leadType.equals("Schedule Test")) {
    			ScheduleTest schedule = ScheduleTest.findById(id);
    			schedule.setPremiumFlag(0);
    			schedule.update();
    		}
			if(leadType.equals("Request More Info")) {
			    RequestMoreInfo info = RequestMoreInfo.findById(id);
			    info.setPremiumFlag(0);
			    info.update();
			}
			if(leadType.equals("Trade In")) {
				TradeIn tradeIn = TradeIn.findById(id);
				tradeIn.setPremiumFlag(0);
				tradeIn.update();
			}
    		return ok();
    	}
    }
    
    public static Result deletePremiumLead(Long id,String leadType) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    
    public static Result changeAssignedUser(Long id,Integer user,String leadType) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    
    public static Result getNewToDoCount() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser userObj = getLocalUser();
    		int count = ToDo.findAllNewCountByUser(userObj);
    		Map map = new HashMap();
    		map.put("count", count);
    		if(count==1) {
    			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    			ToDo todo = ToDo.findAllNewByUser(userObj).get(0);
    			ToDoVM doVM = new ToDoVM();
    			doVM.assignedById = todo.getAssignedBy().id;
    			doVM.priority = todo.getPriority();
    			doVM.task = todo.getTask();
    			doVM.dueDate = df.format(todo.dueDate);
    			map.put("data", doVM);
    		}
    		return ok(Json.toJson(map));
    	}
    }
    
    public static Result setTodoSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser userObj = getLocalUser();
    		List<ToDo> todoList = ToDo.findAllNewByUser(userObj);
    		for(ToDo todo : todoList) {
    			todo.setSeen("Seen");
    			todo.update();
    		}
    		return ok();
    	}
    }
    
    public static Result restoreLead(Long id,String type){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		if(type.equals("Request More Info")) {
    			RequestMoreInfo info = RequestMoreInfo.findById(id);
    			info.setStatus(null);
    			info.update();
    		}
			if(type.equals("Schedule Test")) {
			    ScheduleTest schedule = ScheduleTest.findById(id);
			    schedule.setLeadStatus(null);
			    schedule.update();
			}
			if(type.equals("Trade In")) {
				TradeIn tradeIn = TradeIn.findById(id);
				tradeIn.setStatus(null);
				tradeIn.update();
			}
    		return ok();
    	}
    }
    
    public static Result deleteCanceledLead(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		if(type.equals("Request More Info")) {
    			RequestMoreInfo info = RequestMoreInfo.findById(id);
    			List<UserNotes> noteList = UserNotes.findRequestMore(info);
    			for(UserNotes note: noteList) {
    				note.delete();
    			}
    			info.delete();
    		}
			if(type.equals("Schedule Test")) {
			    ScheduleTest schedule = ScheduleTest.findById(id);
			    List<UserNotes> noteList = UserNotes.findScheduleTest(schedule);
			    for(UserNotes note: noteList) {
    				note.delete();
    			}
			    schedule.delete();
			}
			if(type.equals("Trade In")) {
				TradeIn tradeIn = TradeIn.findById(id);
				List<UserNotes> noteList = UserNotes.findTradeIn(tradeIn);
				for(UserNotes note: noteList) {
    				note.delete();
    			}
				tradeIn.delete();
			}
    		return ok();
    	}
    }
    
    public static Result getAssignedLeads() {
    	AuthUser user = (AuthUser)getLocalUser();
    	List<ScheduleTest> tests = ScheduleTest.findAllReassigned(user);
    	List<RequestMoreInfo> infos = RequestMoreInfo.findAllReassigned(user);
    	List<TradeIn> tradeIns = TradeIn.findAllReassigned(user);
    	Map map = new HashMap();
    	map.put("count", tests.size()+infos.size()+tradeIns.size());
    	if(tests.size()+infos.size()+tradeIns.size()==1) {
    		if(tests.size()==1) {
    			RequestInfoVM infoVM = new RequestInfoVM();
    			Vehicle vehicle = Vehicle.findByVinAndStatus(tests.get(0).vin);
    			infoVM.make = vehicle.getMake();
    			infoVM.leadType = "Schedule Test";
    			infoVM.model = vehicle.getModel();
    			infoVM.name = vehicle.getYear();
    			map.put("data", infoVM);
    		} else if(infos.size()==1) {
    			RequestInfoVM infoVM = new RequestInfoVM();
    			Vehicle vehicle = Vehicle.findByVinAndStatus(infos.get(0).vin);
    			infoVM.make = vehicle.getMake();
    			if(infos.get(0).isScheduled) 
    				infoVM.leadType = "Schedule Test";
    			else
    				infoVM.leadType = "Request More Info";
    			infoVM.model = vehicle.getModel();
    			infoVM.name = vehicle.getYear();
    			map.put("data", infoVM);
    		} else {
    			RequestInfoVM infoVM = new RequestInfoVM();
    			Vehicle vehicle = Vehicle.findByVinAndStatus(tradeIns.get(0).vin);
    			infoVM.make = vehicle.getMake();
    			if(tradeIns.get(0).isScheduled) 
    				infoVM.leadType = "Schedule Test";
    			else
    				infoVM.leadType = "Trade In";
    			infoVM.model = vehicle.getModel();
    			infoVM.name = vehicle.getYear();
    			map.put("data", infoVM);
    		}
    	}
    	return ok(Json.toJson(map));
    }
    
    public static Result setLeadSeen() {
    	AuthUser user = (AuthUser)getLocalUser();
    	List<ScheduleTest> tests = ScheduleTest.findAllReassigned(user);
    	List<RequestMoreInfo> infos = RequestMoreInfo.findAllReassigned(user);
    	List<TradeIn> tradeIns = TradeIn.findAllReassigned(user);
    	for(ScheduleTest test : tests) {
    		test.setIsReassigned(false);
    		test.update();
    	}
    	for(RequestMoreInfo info : infos) {
    		info.setIsReassigned(false);
    		info.update();
    	}
    	for(TradeIn tradeIn : tradeIns) {
    		tradeIn.setIsReassigned(false);
    		tradeIn.update();
    	}
    	return ok();
    }
    
    public static Result getAnalystData() {
    	String params = "&type=visitors-online,bounce-rate,pages&limit=all";
    	return ok(Json.parse(callClickAPI(params)));
    }
    
    public static Result getVisitorList(){
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	String params = null;
    	
    	params = "&type=visitors-list&date=last-7-days&limit=all";
    	//if(value == 30){
    		//params = "&type=visitors-list&date="+startDate+","+endDate+"&limit=all";
    	/*}else if(value == 7){
    		params = "&type=visitors-list&date=last-7-days&limit=all";
    	}else if(value == 1){
    		params = "&type=visitors-list&date="+year+"&limit=all";
    	}*/
    	return ok(Json.parse(callClickAPI(params)));
    }
    
    
    public static Result getHeatMapList(Integer value){
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	String params = null;
    	
    	if(value == 30){
    		params = "&type=pages&heatmap_url=1&date=last-30-days&limit=all";
    	}else if(value == 7){
    		params = "&type=pages&heatmap_url=1&date=last-7-days&limit=all";
    	}else if(value == 1){
    		params = "&type=pages&heatmap_url=1&date="+year+"&limit=all";
    	}
    	return ok(Json.parse(callClickAPI(params)));
    }
    
    public static Result getActionList(Integer value){
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	String params = null;
    	
    	if(value == 30){
    		params = "&type=actions-list&date=last-30-days&limit=all";
    	}else if(value == 7){
    		params = "&type=actions-list&date=last-7-days&limit=all";
    	}else if(value == 1){
    		params = "&type=actions-list&date="+year+"&limit=all";
    	}
    	
    	return ok(Json.parse(callClickAPI(params)));
    }
    
    public static Result getSearchList(Integer value){
    	
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	String params = null;
    	
    	if(value == 30){
    		params = "&type=searches&date=last-30-days&limit=all";
    	}else if(value == 7){
    		params = "&type=searches&date=last-7-days&limit=all";
    	}else if(value == 1){
    		params = "&type=searches&date="+year+"&limit=all";
    	}
    	
    	return ok(Json.parse(callClickAPI(params)));
    }
    
    public static Result getContentList(Integer value){
    	
    	String params = "&type=pages&date=last-30-days&limit=all";
    	return ok(Json.parse(callClickAPI(params)));
    }
    
    
    public static Result getAllVehicleDemographics(){
    	Map map = new HashMap(2);
		Map<String, Integer> mapRM = new HashMap<String, Integer>();
		Map<String, Integer> mapWebBro = new HashMap<String, Integer>();
		Map<String, Integer> maplocation = new HashMap<String, Integer>();
		Map<String, Integer> mapoperatingSystem = new HashMap<String, Integer>();
		Map<String, Integer> mapSreenResoluation = new HashMap<String, Integer>();
		
    	String params = "&type=visitors-list&date=last-30-days&limit=all";
    	try {
    		
    		int lagCount = 1;
			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			for(int i=0;i<jsonArray.length();i++){
    			String data = jsonArray.getJSONObject(i).get("landing_page").toString();
    			String arr[] = data.split("/");
    			if(arr.length > 5){
    			  if(arr[5] != null){
    				//  if(arr[5].equals(value)){
    					  
    					  Integer langValue = mapRM.get(jsonArray.getJSONObject(i).get("language").toString()); 
    						if (langValue == null) {
    							mapRM.put(jsonArray.getJSONObject(i).get("language").toString(), lagCount);
    						}else{
    							mapRM.put(jsonArray.getJSONObject(i).get("language").toString(), mapRM.get(jsonArray.getJSONObject(i).get("language").toString()) + 1);
    						}
    						
    						 Integer mapWebBroValue = mapWebBro.get(jsonArray.getJSONObject(i).get("web_browser").toString()); 
     						if (mapWebBroValue == null) {
     							mapWebBro.put(jsonArray.getJSONObject(i).get("web_browser").toString(), lagCount);
     						}else{
     							mapWebBro.put(jsonArray.getJSONObject(i).get("web_browser").toString(), mapWebBro.get(jsonArray.getJSONObject(i).get("web_browser").toString()) + 1);
     						}
     						
     						Integer maplocationValue = maplocation.get(jsonArray.getJSONObject(i).get("geolocation").toString()); 
     						if (maplocationValue == null) {
     							maplocation.put(jsonArray.getJSONObject(i).get("geolocation").toString(), lagCount);
     						}else{
     							maplocation.put(jsonArray.getJSONObject(i).get("geolocation").toString(), maplocation.get(jsonArray.getJSONObject(i).get("geolocation").toString()) + 1);
     						}
     						
     						Integer mapoperatingSystemValue = mapoperatingSystem.get(jsonArray.getJSONObject(i).get("operating_system").toString()); 
     						if (mapoperatingSystemValue == null) {
     							mapoperatingSystem.put(jsonArray.getJSONObject(i).get("operating_system").toString(), lagCount);
     						}else{
     							mapoperatingSystem.put(jsonArray.getJSONObject(i).get("operating_system").toString(), mapoperatingSystem.get(jsonArray.getJSONObject(i).get("operating_system").toString()) + 1);
     						}
     						
     						Integer mapSreenResoluationValue = mapSreenResoluation.get(jsonArray.getJSONObject(i).get("screen_resolution").toString()); 
     						if (mapSreenResoluationValue == null) {
     							mapSreenResoluation.put(jsonArray.getJSONObject(i).get("screen_resolution").toString(), lagCount);
     						}else{
     							mapSreenResoluation.put(jsonArray.getJSONObject(i).get("screen_resolution").toString(), mapSreenResoluation.get(jsonArray.getJSONObject(i).get("screen_resolution").toString()) + 1);
     						}
    						
     						
    				 // }
    			  }
    			}
    			
			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	map.put("language", mapRM);
    	map.put("location", maplocation);
    	map.put("webBrowser", mapWebBro);
    	map.put("operatingSystem", mapoperatingSystem);
    	map.put("screenResoluation", mapSreenResoluation);
    	
    	
    	return ok(Json.toJson(map));
    }
    
    public static Result getDemographics(String value){
    	Map map = new HashMap(2);
		Map<String, Integer> mapRM = new HashMap<String, Integer>();
		Map<String, Integer> mapWebBro = new HashMap<String, Integer>();
		Map<String, Integer> maplocation = new HashMap<String, Integer>();
		Map<String, Integer> mapoperatingSystem = new HashMap<String, Integer>();
		Map<String, Integer> mapSreenResoluation = new HashMap<String, Integer>();
		
    	String params = "&type=visitors-list&date=last-30-days&limit=all";
    	try {
    		
    		int lagCount = 1;
			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			for(int i=0;i<jsonArray.length();i++){
    			String data = jsonArray.getJSONObject(i).get("landing_page").toString();
    			String arr[] = data.split("/");
    			if(arr.length > 5){
    			  if(arr[5] != null){
    				  if(arr[5].equals(value)){
    					  
    					  Integer langValue = mapRM.get(jsonArray.getJSONObject(i).get("language").toString()); 
    						if (langValue == null) {
    							mapRM.put(jsonArray.getJSONObject(i).get("language").toString(), lagCount);
    						}else{
    							mapRM.put(jsonArray.getJSONObject(i).get("language").toString(), mapRM.get(jsonArray.getJSONObject(i).get("language").toString()) + 1);
    						}
    						
    						 Integer mapWebBroValue = mapWebBro.get(jsonArray.getJSONObject(i).get("web_browser").toString()); 
     						if (mapWebBroValue == null) {
     							mapWebBro.put(jsonArray.getJSONObject(i).get("web_browser").toString(), lagCount);
     						}else{
     							mapWebBro.put(jsonArray.getJSONObject(i).get("web_browser").toString(), mapWebBro.get(jsonArray.getJSONObject(i).get("web_browser").toString()) + 1);
     						}
     						
     						Integer maplocationValue = maplocation.get(jsonArray.getJSONObject(i).get("geolocation").toString()); 
     						if (maplocationValue == null) {
     							maplocation.put(jsonArray.getJSONObject(i).get("geolocation").toString(), lagCount);
     						}else{
     							maplocation.put(jsonArray.getJSONObject(i).get("geolocation").toString(), maplocation.get(jsonArray.getJSONObject(i).get("geolocation").toString()) + 1);
     						}
     						
     						Integer mapoperatingSystemValue = mapoperatingSystem.get(jsonArray.getJSONObject(i).get("operating_system").toString()); 
     						if (mapoperatingSystemValue == null) {
     							mapoperatingSystem.put(jsonArray.getJSONObject(i).get("operating_system").toString(), lagCount);
     						}else{
     							mapoperatingSystem.put(jsonArray.getJSONObject(i).get("operating_system").toString(), mapoperatingSystem.get(jsonArray.getJSONObject(i).get("operating_system").toString()) + 1);
     						}
     						
     						Integer mapSreenResoluationValue = mapSreenResoluation.get(jsonArray.getJSONObject(i).get("screen_resolution").toString()); 
     						if (mapSreenResoluationValue == null) {
     							mapSreenResoluation.put(jsonArray.getJSONObject(i).get("screen_resolution").toString(), lagCount);
     						}else{
     							mapSreenResoluation.put(jsonArray.getJSONObject(i).get("screen_resolution").toString(), mapSreenResoluation.get(jsonArray.getJSONObject(i).get("screen_resolution").toString()) + 1);
     						}
    						
     						
    				  }
    			  }
    			}
    			
			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	map.put("language", mapRM);
    	map.put("location", maplocation);
    	map.put("webBrowser", mapWebBro);
    	map.put("operatingSystem", mapoperatingSystem);
    	map.put("screenResoluation", mapSreenResoluation);
    	
    	
    	return ok(Json.toJson(map));
    }
    
    public static Result getAllvehicleStatusList(){

    	String params = "&type=actions-list&date=last-30-days&limit=all";
    	
    	int countSubrequestmoreinfo = 0;
		int countShowrequestmoreinfoshow = 0;
		
		int countSubscheduletest = 0;
		int countShowscheduletestshow = 0;
		
		int countSubtrade = 0;
		int countShowtrade = 0;
		int countenginesound = 0;
		int countvirtualtour = 0;
		
    	int countSubemailtofriend = 0;
		int countShowemailtofriend = 0;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
    	try {
    		JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    		for(int i=0;i<jsonArray1.length();i++){
    			String data = jsonArray1.getJSONObject(i).get("action_url").toString();
    			String dataArr[] = data.split("#");
    			if(dataArr!=null && dataArr.length>0){
    				int count = data.split("#").length - 1;
        		 if(count == 2){
        				if(dataArr[2].equals("requestmoreinfo")){
        					countSubrequestmoreinfo++;
        				}
        				if(dataArr[2].equals("requestmoreinfoshow")){
        					countShowrequestmoreinfoshow++;
        				}
        				
        				if(dataArr[2].equals("scheduletest")){
        					countSubscheduletest++;
        				}
        				if(dataArr[2].equals("scheduletestshow")){
        					countShowscheduletestshow++;
        				}
        				
        				if(dataArr[2].equals("emailtofriend")){
        					countSubemailtofriend++;
        				}
        				
        				if(dataArr[2].equals("emailtofriendshow")){
        					countShowemailtofriend++;
        				}
        				
        				if(dataArr[2].equals("tradeinapp")){
        					countSubtrade++;
        				}
        				if(dataArr[2].equals("tradeinappshow")){
        					countShowtrade++;
        				}
        				if(dataArr[2].equals("enginesound")){
        					countenginesound++;
        				}
        				if(dataArr[2].equals("virtualTour")){
        					countvirtualtour++;
        				}
        			}
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	//AuthUser user = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
    	AuthUser user = getLocalUser();
    	List<PriceAlert> pAlert = PriceAlert.getByLocation(Long.valueOf(session("USER_LOCATION")));
    	int followerCount = pAlert.size();
    	
    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
    	Map<String, Integer> mapIPAdd = new HashMap<String, Integer>();
    	
    	String params1 = "&type=visitors-list&date=last-30-days&limit=all";
    		int totalTime = 0;
    		int lagCount = 0;
    		int newuserCount = 0;
    		int avgDur = 0;
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(callClickAPI(params1)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
				for(int i=0;i<jsonArray.length();i++){
	    			String data = jsonArray.getJSONObject(i).get("landing_page").toString();
	    			String arr[] = data.split("/");
	    			if(arr.length > 5){
	    			  if(arr[5] != null){
	    				//  if(arr[5].equals(value)){
	    					  lagCount++;
	    					  
	    				totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(i).get("time_total").toString());
	    				  
	    				  Integer langValue = mapRM.get(jsonArray.getJSONObject(i).get("uid").toString()); 
  						 if (langValue == null) {
  						 	mapRM.put(jsonArray.getJSONObject(i).get("uid").toString(), 1);
  						 }else{
  							mapRM.put(jsonArray.getJSONObject(i).get("uid").toString(),  1);
  						 }
  						 
  						Integer ipAddessValue = mapIPAdd.get(jsonArray.getJSONObject(i).get("ip_address").toString()); 
 						 if (ipAddessValue == null) {
 							mapIPAdd.put(jsonArray.getJSONObject(i).get("ip_address").toString(),Integer.parseInt(jsonArray.getJSONObject(i).get("total_visits").toString()));
 						 }else{
 							mapIPAdd.put(jsonArray.getJSONObject(i).get("ip_address").toString(), Integer.parseInt(jsonArray.getJSONObject(i).get("total_visits").toString()));
 						 }
  						 
	    				//}
	    			  }
	    			}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (Map.Entry<String, Integer> entry : mapIPAdd.entrySet())
			{
				if(entry.getValue() == 1){
					newuserCount++;
				}
			}	
    			
			
    	if(lagCount != 0){
			avgDur = totalTime/lagCount;	
    	}
		map.put("newUser", newuserCount);
    	map.put("users", mapRM.size());
    	map.put("avgSessDur", avgDur);
		map.put("pageview", lagCount);
    	map.put("followers", followerCount);
    	map.put("enginesound", countenginesound);
    	map.put("virtualtour", countvirtualtour);
    	map.put("requestmoreinfo", countSubrequestmoreinfo);
    	map.put("requestmoreinfoshow", countShowrequestmoreinfoshow);
    	map.put("requestmoreinfoTotal", (countSubrequestmoreinfo+countShowrequestmoreinfoshow));
    	
    	map.put("scheduletest", countSubscheduletest);
    	map.put("scheduletestshow", countShowscheduletestshow);
    	map.put("scheduletestTotal", (countSubscheduletest+countShowscheduletestshow));
    	
    	map.put("tradeinapp", countSubtrade);
    	map.put("tradeinappshow", countShowtrade);
    	map.put("tradeinappTotal", (countSubtrade+countShowtrade));
    	
    	map.put("emailtofriend", countSubemailtofriend);
    	map.put("emailtofriendshow", countShowemailtofriend);
    	map.put("emailtofriendTotal", (countSubemailtofriend+countShowemailtofriend));
    	return ok(Json.toJson(map));
    }
    
    
    public static Result getStatusList(String value){
    	
    	
    	String params = "&type=actions-list&date=last-30-days&limit=all";
    	
    	int countSubrequestmoreinfo = 0;
		int countShowrequestmoreinfoshow = 0;
		
		int countSubscheduletest = 0;
		int countShowscheduletestshow = 0;
		
		int countSubtrade = 0;
		int countShowtrade = 0;
		int countenginesound = 0;
		int countvirtualtour = 0;
		
    	int countSubemailtofriend = 0;
		int countShowemailtofriend = 0;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
    	try {
    		JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    		for(int i=0;i<jsonArray1.length();i++){
    			String data = jsonArray1.getJSONObject(i).get("action_url").toString();
    			String dataArr[] = data.split("#");
    			if(dataArr!=null && dataArr.length>0){
    				int count = data.split("#").length - 1;
        			if(count == 1){
        				if(dataArr[1].equals("requestmoreinfo")){
        					countSubrequestmoreinfo++;
        				}
        				if(dataArr[1].equals("requestmoreinfoshow")){
        					countShowrequestmoreinfoshow++;
        				}
        				
        				if(dataArr[1].equals("scheduletest")){
        					countSubscheduletest++;
        				}
        				if(dataArr[1].equals("scheduletestshow")){
        					countShowscheduletestshow++;
        				}
        				
        				if(dataArr[1].equals("emailtofriend")){
        					countSubemailtofriend++;
        				}
        				if(dataArr[1].equals("emailtofriendshow")){
        					countShowemailtofriend++;
        				}
        				
        				if(dataArr[1].equals("tradeinapp")){
        					countSubtrade++;
        				}
        				if(dataArr[1].equals("tradeinappshow")){
        					countShowtrade++;
        				}
        				
        			} else if(count == 2){
        				if(dataArr[2].equals("requestmoreinfo")&&dataArr[1].equals(value)){
        					countSubrequestmoreinfo++;
        				}
        				if(dataArr[2].equals("requestmoreinfoshow")&&dataArr[1].equals(value)){
        					countShowrequestmoreinfoshow++;
        				}
        				
        				if(dataArr[2].equals("scheduletest")&&dataArr[1].equals(value)){
        					countSubscheduletest++;
        				}
        				if(dataArr[2].equals("scheduletestshow")&&dataArr[1].equals(value)){
        					countShowscheduletestshow++;
        				}
        				
        				if(dataArr[2].equals("emailtofriend")&&dataArr[1].equals(value)){
        					countSubemailtofriend++;
        				}
        				if(dataArr[2].equals("emailtofriendshow")&&dataArr[1].equals(value)){
        					countShowemailtofriend++;
        				}
        				
        				if(dataArr[2].equals("tradeinapp")&&dataArr[1].equals(value)){
        					countSubtrade++;
        				}
        				if(dataArr[2].equals("tradeinappshow")&&dataArr[1].equals(value)){
        					countShowtrade++;
        				}
        				if(dataArr[2].equals("enginesound")&&dataArr[1].equals(value)){
        					countenginesound++;
        				}
        				if(dataArr[2].equals("virtualTour")&&dataArr[1].equals(value)){
        					countvirtualtour++;
        				}
        			}
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    	List<PriceAlert> pAlert = PriceAlert.getEmailsByVin(value, Long.valueOf(session("USER_LOCATION")));
    	int followerCount = pAlert.size();
    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
    	Map<String, Integer> mapIPAdd = new HashMap<String, Integer>();
    	
    	String params1 = "&type=visitors-list&date=last-30-days&limit=all";
    		int totalTime = 0;
    		int lagCount = 0;
    		int newuserCount = 0;
    		int avgDur = 0;
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(callClickAPI(params1)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
				for(int i=0;i<jsonArray.length();i++){
	    			String data = jsonArray.getJSONObject(i).get("landing_page").toString();
	    			String arr[] = data.split("/");
	    			if(arr.length > 5){
	    			  if(arr[5] != null){
	    				  if(arr[5].equals(value)){
	    					  lagCount++;
	    					  
	    				totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(i).get("time_total").toString());
	    				  
	    				  Integer langValue = mapRM.get(jsonArray.getJSONObject(i).get("uid").toString()); 
  						 if (langValue == null) {
  						 	mapRM.put(jsonArray.getJSONObject(i).get("uid").toString(), 1);
  						 }else{
  							mapRM.put(jsonArray.getJSONObject(i).get("uid").toString(),  1);
  						 }
  						 
  						Integer ipAddessValue = mapIPAdd.get(jsonArray.getJSONObject(i).get("ip_address").toString()); 
 						 if (ipAddessValue == null) {
 							mapIPAdd.put(jsonArray.getJSONObject(i).get("ip_address").toString(),Integer.parseInt(jsonArray.getJSONObject(i).get("total_visits").toString()));
 						 }else{
 							mapIPAdd.put(jsonArray.getJSONObject(i).get("ip_address").toString(), Integer.parseInt(jsonArray.getJSONObject(i).get("total_visits").toString()));
 						 }
  						 
	    				}
	    			  }
	    			}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (Map.Entry<String, Integer> entry : mapIPAdd.entrySet())
			{
				if(entry.getValue() == 1){
					newuserCount++;
				}
			}	
    			
			
    	if(lagCount != 0){
			avgDur = totalTime/lagCount;	
    	}
		map.put("newUser", newuserCount);
    	map.put("users", mapRM.size());
    	map.put("avgSessDur", avgDur);
		map.put("pageview", lagCount);
    	map.put("followers", followerCount);
    	map.put("enginesound", countenginesound);
    	map.put("virtualtour",countvirtualtour);
    	map.put("requestmoreinfo", countSubrequestmoreinfo);
    	map.put("requestmoreinfoshow", countShowrequestmoreinfoshow);
    	map.put("requestmoreinfoTotal", (countSubrequestmoreinfo+countShowrequestmoreinfoshow));
    	
    	map.put("scheduletest", countSubscheduletest);
    	map.put("scheduletestshow", countShowscheduletestshow);
    	map.put("scheduletestTotal", (countSubscheduletest+countShowscheduletestshow));
    	
    	map.put("tradeinapp", countSubtrade);
    	map.put("tradeinappshow", countShowtrade);
    	map.put("tradeinappTotal", (countSubtrade+countShowtrade));
    	
    	map.put("emailtofriend", countSubemailtofriend);
    	map.put("emailtofriendshow", countShowemailtofriend);
    	map.put("emailtofriendTotal", (countSubemailtofriend+countShowemailtofriend));
    	return ok(Json.toJson(map));
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
    
    public static Result getVisitorOnline(){
    	JsonNode onlineVisitorsNode = Json.parse(callClickAPI("&type=visitors-online&limit=all"));
    	return ok(Json.toJson(onlineVisitorsNode.get(0).get("dates").get(0).get("items").get(0).get("value").asInt()));
    }
    
    public static Result getMonthlyVisitorsStats(String startDate, String endDate) {
    	
    	Calendar c = Calendar.getInstance();
    	String[] monthsArr = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    	c.add(Calendar.MONTH, -11);
    	List<Integer> allVisitor = new ArrayList<Integer>(12);
    	List<Integer> onlineVisitor = new ArrayList<Integer>(12);
    	List<Integer> actionsList = new ArrayList<Integer>(12);
    	List<Integer> averageActionsList = new ArrayList<Integer>(12);
    	List<String> months = new ArrayList<String>(12);
    	for(int i=0;i<12;i++) {
    		String year = c.get(Calendar.YEAR)+"";
        	String month = c.get(Calendar.MONTH)+1>9?(c.get(Calendar.MONTH)+1)+"":"0"+(c.get(Calendar.MONTH)+1);
    		JsonNode node = Json.parse(callClickAPI("&date="+year+"-"+month+"&type=visitors,visitors-new,actions,actions-average&limit=all"));
    		JsonNode allVisitorNode = node.get(0);
    		JsonNode onlineVisitorNode = node.get(1);
    		JsonNode actionsNode = node.get(2);
    		JsonNode averageActionsNode = node.get(3);
    		allVisitor.add(allVisitorNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		onlineVisitor.add(onlineVisitorNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		actionsList.add(actionsNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		averageActionsList.add(averageActionsNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		months.add(monthsArr[c.get(Calendar.MONTH)]);
    		c.add(Calendar.MONTH, 1);
    	}
    	Date date = new Date();
        c.setTime(date);
        String year = c.get(Calendar.YEAR)+"";
    	String month = c.get(Calendar.MONTH)+1>9?(c.get(Calendar.MONTH)+1)+"":"0"+(c.get(Calendar.MONTH)+1);
    	Integer dateOfMonth = c.get(Calendar.DAY_OF_MONTH);
    	JsonNode onlineVisitorsNode = Json.parse(callClickAPI("&date="+startDate+","+endDate+"&type=visitors-online&limit=all"));
    	
    	JsonNode visitorsNode = Json.parse(callClickAPI("&type=visitors,actions,actions-average,time-total-pretty,time-average-pretty,bounce-rate,goals,revenue&date="+startDate+","+endDate+"&limit=all"));
    	JsonNode pagesNodeList = Json.parse(callClickAPI("&type=pages&date="+startDate+","+endDate+"&limit=all"));
    	JsonNode referersNodeList = Json.parse(callClickAPI("&type=links-domains&date="+startDate+","+endDate+"&limit=all"));
    	JsonNode searchesNodeList = Json.parse(callClickAPI("&type=searches-engines&date="+startDate+","+endDate+"&limit=all"));
    	List<PageVM> pagesList = new ArrayList<>();
    	List<PageVM> referersList = new ArrayList<>();
    	List<PageVM> searchesList = new ArrayList<>();
    	
    	for(JsonNode obj : pagesNodeList.get(0).get("dates").get(0).get("items")) {
    		String arr[] = obj.get("url").textValue().split("/");
    		PageVM vm = new PageVM();
    		vm.count = obj.get("value").textValue();
    		if(arr[arr.length-2].equals("mobile")) {
    			vm.pageUrl = "mobile "+arr[arr.length-1];	
	    	} else {
	    		if(arr[arr.length-1].equals("") || arr[arr.length-1].equals("glivr")) {
	    			vm.pageUrl = "dashboard";
	    		} else {
	    			vm.pageUrl = arr[arr.length-1];
	    		}
	    	}
    		
    		pagesList.add(vm);
    	}
    	
    	for(JsonNode obj : referersNodeList.get(0).get("dates").get(0).get("items")) {
    		PageVM vm = new PageVM();
    		vm.pageUrl = obj.get("title").textValue();
    		vm.count = obj.get("value").textValue();
    		referersList.add(vm);
    	}
    	
    	for(JsonNode obj : searchesNodeList.get(0).get("dates").get(0).get("items")) {
    		PageVM vm = new PageVM();
    		vm.pageUrl = obj.get("title").textValue();
    		vm.count = obj.get("value").textValue();
    		searchesList.add(vm);
    	}
    	
    	Map map = new HashMap(2);
    	map.put("allVisitor", allVisitor);
    	map.put("onlineVisitor", onlineVisitor);
    	map.put("months", months);
    	map.put("onlineVisitors", onlineVisitorsNode.get(0).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("totalVisitors", visitorsNode.get(0).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("actions", visitorsNode.get(1).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("averageActions", visitorsNode.get(2).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("totalTime", visitorsNode.get(3).get("dates").get(0).get("items").get(0).get("value"));
    	map.put("averageTime", visitorsNode.get(4).get("dates").get(0).get("items").get(0).get("value"));
    	map.put("bounceRate", visitorsNode.get(5).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("revenue", visitorsNode.get(7).get("dates").get(0).get("items").get(0).get("value").asInt());
    	map.put("pagesList", pagesList);
    	map.put("referersList", referersList);
    	map.put("searchesList", searchesList);
    	map.put("actionsList", actionsList);
    	map.put("averageActionsList", averageActionsList);
    	return ok(Json.toJson(map));
      }
    
     
    
    
    public static Result getMailDaysUserStats(String value,String lasttime,String mailType) {
    	
    	List<Integer> allVisitor = new ArrayList<Integer>(30);
    	List<String> months = new ArrayList<String>(30);
    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
    	int visitorCount = 0;
    	int totalTime = 0;
    	int avgDur = 0;
    	Calendar c = Calendar.getInstance();
    	
    	if(lasttime.equals("month")){
    		
        	String[] monthsArr = new String[30]; 
        	c.add(Calendar.DAY_OF_YEAR, -29);
        	
        	for(int i=0;i<30;i++) {
        		mapRM.clear();
        		visitorCount = 0;
        		totalTime = 0;
        		avgDur = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		if(days.length() < 2){
        			days = 0+days;
        		}
        		if(month.length() < 2){
        			month = 0+month;
        		}
        		String dates = year + month + days;
        		
        			
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        					int count = data.split("#").length - 1;
        		    		 if(count == 2){
        		    			 if(mailType.equals("requestMore")){
        		    				 if(dataArr[2].equals("requestmoreinfo")&&dataArr[1].equals(value)){
         		    					visitorCount++;
         		    				 }
        		    			 }else if(mailType.equals("scheduleTest")){
        		    				 if(dataArr[2].equals("scheduletest")&&dataArr[1].equals(value)){
          		    					visitorCount++;
          		    				 }
        		    			 }else if(mailType.equals("tradeInApp")){
        		    				 if(dataArr[2].equals("tradeinapp")&&dataArr[1].equals(value)){
           		    					visitorCount++;
           		    				 }
         		    			 }else if(mailType.equals("emailToFrd")){
        		    				 if(dataArr[2].equals("emailtofriend")&&dataArr[1].equals(value)){
            		    					visitorCount++;
            		    				 }
          		    			 }
        		    			 
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		
        			allVisitor.add(visitorCount);
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("week")){
    		
        	String[] monthsArr = new String[7]; 
        	c.add(Calendar.DAY_OF_YEAR, -6);
        	
        	for(int i=0;i<7;i++) {
        		mapRM.clear();
        		visitorCount = 0;
        		totalTime = 0;
        		avgDur = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		if(days.length() < 2){
        			days = 0+days;
        		}
        		if(month.length() < 2){
        			month = 0+month;
        		}
        		String dates = year + month + days;
        		
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        						if(count == 2){
           		    			 if(mailType.equals("requestMore")){
           		    				 if(dataArr[2].equals("requestmoreinfo")&&dataArr[1].equals(value)){
            		    					visitorCount++;
            		    				 }
           		    			 }else if(mailType.equals("scheduleTest")){
           		    				 if(dataArr[2].equals("scheduletest")&&dataArr[1].equals(value)){
             		    					visitorCount++;
             		    				 }
           		    			 }else if(mailType.equals("tradeInApp")){
           		    				 if(dataArr[2].equals("tradeinapp")&&dataArr[1].equals(value)){
              		    					visitorCount++;
              		    				 }
            		    			 }else if(mailType.equals("emailToFrd")){
           		    				 if(dataArr[2].equals("emailtofriend")&&dataArr[1].equals(value)){
               		    					visitorCount++;
               		    				 }
             		    			 }
           		    			 
           		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		
        			allVisitor.add(visitorCount);
        		
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("day")){
    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = new Date();
    		List<String> monthsArr = new ArrayList<>();
    		
    		int chagehr = 0;
    		totalTime = 0;
    		avgDur = 0;
    		
    		String dates = dateFormat.format(date);
        	for(int i=0;i<24;i++) {
        		mapRM.clear();
        		if(i == 0){
        			chagehr = 12;
        		}else{
        			chagehr++;
        		}
        		
        		visitorCount = 0;
        		
        		
        		//if(mailType.equals("enginesound")){
        			
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			//String params = "&date=2015-12-04&type=actions-list&limit=all";
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        		    		    if(count == 2){
        		    		    	if(mailType.equals("requestMore")){
        		    		    	 if(dataArr[2].equals("requestmoreinfo")&&dataArr[1].equals(value)){
        		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
          	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
          	    					    String[] timeset = timevisit.split(",");
      									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
      									if(timeDiv[1].equals(String.valueOf(chagehr))){
      										if(timeDiv[4].equals("am") && i < 12){
    											visitorCount++;
    										}else if(timeDiv[4].equals("pm") && i >= 12){
    											visitorCount++;
    										}
      									}
  	        		    			 }
        		    		       }else if(mailType.equals("scheduleTest")){
        		    		    	   if(dataArr[2].equals("scheduletest")&&dataArr[1].equals(value)){
           		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
             	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
             	    					    String[] timeset = timevisit.split(",");
         									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
         									if(timeDiv[1].equals(String.valueOf(chagehr))){
         										if(timeDiv[4].equals("am") && i < 12){
        											visitorCount++;
        										}else if(timeDiv[4].equals("pm") && i >= 12){
        											visitorCount++;
        										}
         									}
     	        		    			 }
        		    		       }else if(mailType.equals("tradeInApp")){
        		    		    	   if(dataArr[2].equals("tradeinapp")&&dataArr[1].equals(value)){
              		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
                	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
                	    					    String[] timeset = timevisit.split(",");
            									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            									if(timeDiv[1].equals(String.valueOf(chagehr))){
            										if(timeDiv[4].equals("am") && i < 12){
            											visitorCount++;
            										}else if(timeDiv[4].equals("pm") && i >= 12){
            											visitorCount++;
            										}
            									}
        	        		    			 }
        		    		       }else if(mailType.equals("emailToFrd")){
        		    		    	   if(dataArr[2].equals("emailtofriend")&&dataArr[1].equals(value)){
             		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
               	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
               	    					    String[] timeset = timevisit.split(",");
           									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
           									if(timeDiv[1].equals(String.valueOf(chagehr))){
           										if(timeDiv[4].equals("am") && i < 12){
        											visitorCount++;
        										}else if(timeDiv[4].equals("pm") && i >= 12){
        											visitorCount++;
        										}
           									}
       	        		    			 }
        		    		       }
      									
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		
        		if(mailType.equals("sessionDuration")){
        			if(visitorCount != 0){
            			avgDur = totalTime/visitorCount;	
            		}
        			
        			allVisitor.add(avgDur);
        		}else{
        			allVisitor.add(visitorCount);
        		}
        		
        		String valueTime = null;
        		if(i == 0){
        			valueTime = "12am";
        		}
        		if(i >= 12){
        			if(i == 12){
        				valueTime = "12pm";
        			}else{
        				int chang = i - 12;
        				valueTime = chang+"pm";
        			}
        			
        		}else{
        			if(i != 0){
        				valueTime = i+"am";
        			}
        		}
        		months.add(valueTime);
        		
        		if(i == 0){
        			chagehr = 0;
        		}else{
        			if(chagehr == 12){
        				chagehr = 0;
        			}
        		}
        	}
        	
    	}
    
    	Map map = new HashMap();
    	map.put("allVisitor", allVisitor);
    	map.put("months", months);
    	return ok(Json.toJson(map));
    }
    
    public static Result getSessionDaysAllStats(String lasttime,String analyType) {
    	
    	List<Integer> allVisitor = new ArrayList<Integer>(30);
    	List<String> months = new ArrayList<String>(30);
    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
    	int visitorCount = 0;
    	int totalTime = 0;
    	int avgDur = 0;
    	Calendar c = Calendar.getInstance();
    	
    	if(lasttime.equals("month")){
    		
        	String[] monthsArr = new String[30]; 
        	c.add(Calendar.DAY_OF_YEAR, -29);
        	
        	for(int i=0;i<30;i++) {
        		mapRM.clear();
        		visitorCount = 0;
        		totalTime = 0;
        		avgDur = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		if(days.length() < 2){
        			days = 0+days;
        		}
        		if(month.length() < 2){
        			month = 0+month;
        		}
        		String dates = year + month + days;
        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        		    		 if(count == 2){
        		    			 if(analyType.equals("enginesound")){
        		    				if(dataArr[2].equals("enginesound")){
        		    					visitorCount++;
        		    				}
        		    			 }else if(analyType.equals("virtualtour")){
        		    				 if(dataArr[2].equals("virtualTour")){
         		    					visitorCount++;
         		    				}
        		    			 }
        		    				
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        			
        		}else{
        			String params = "&date="+dates+"&type=visitors-list&limit=all";
        			try {
        				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray.length();j++){
        					String data = jsonArray.getJSONObject(j).get("landing_page").toString();
        					String arr[] = data.split("/");
        					if(arr.length > 5){
        						if(arr[5] != null){
        								if(analyType.equals("user")){
        									Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
        									if (langValue == null) {
        										visitorCount = visitorCount + 1;
        										mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
        									}
        								}else if(analyType.equals("pageview")){
        									visitorCount = visitorCount + 1;
        								}else if(analyType.equals("sessionDuration")){
        									visitorCount = visitorCount + 1;
        									totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
        								}
        						}
        					}
        				}	
        			} catch (JSONException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}
        		
        		if(analyType.equals("sessionDuration")){
        			if(visitorCount != 0 || totalTime != 0){
            			avgDur = totalTime/visitorCount;	
            		}
        			
        			allVisitor.add(avgDur);
        		}else{
        			allVisitor.add(visitorCount);
        		}
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("week")){
    		
        	String[] monthsArr = new String[7]; 
        	c.add(Calendar.DAY_OF_YEAR, -6);
        	
        	for(int i=0;i<7;i++) {
        		mapRM.clear();
        		visitorCount = 0;
        		totalTime = 0;
        		avgDur = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		if(days.length() < 2){
        			days = 0+days;
        		}
        		if(month.length() < 2){
        			month = 0+month;
        		}
        		String dates = year + month + days;
            	
        		
        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
        			
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        		    		 if(count == 2){
        		    			 if(analyType.equals("enginesound")){
         		    				if(dataArr[2].equals("enginesound")){
         		    					visitorCount++;
         		    				}
         		    			 }else if(analyType.equals("virtualtour")){
         		    				 if(dataArr[2].equals("virtualTour")){
          		    					visitorCount++;
          		    				}
         		    			 }
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        			
        		}else{
        		  String params = "&date="+dates+"&type=visitors-list&limit=all";
        		  try {
    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    					  
    	    					  if(analyType.equals("user")){
    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
    	    						  if (langValue == null) {
    	    							  visitorCount = visitorCount + 1;
    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
    	    						  	}
    	    					  	}else if(analyType.equals("pageview")){
    	    					  		visitorCount = visitorCount + 1;
    	    					  	}else if(analyType.equals("sessionDuration")){
    	    					  		visitorCount = visitorCount + 1;
    	    					  		totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
    	    					  	}
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        		
        		if(analyType.equals("sessionDuration")){
        			if(visitorCount != 0){
            			avgDur = totalTime/visitorCount;	
            		}
        			
        			allVisitor.add(avgDur);
        		}else{
        			allVisitor.add(visitorCount);
        		}
        		
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("day")){
    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = new Date();
    		List<String> monthsArr = new ArrayList<>();
    		
    		int chagehr = 0;
    		totalTime = 0;
    		avgDur = 0;
    		
    		String dates = dateFormat.format(date);
        	for(int i=0;i<24;i++) {
        		mapRM.clear();
        		if(i == 0){
        			chagehr = 12;
        		}else{
        			chagehr++;
        		}
        		
        		visitorCount = 0;
        		
        		
        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
        			
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			//String params = "&date=2015-12-09&type=actions-list&limit=all";
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        		    		    if(count == 2){
        		    		    	if(analyType.equals("enginesound")){	
        		    		    		if(dataArr[2].equals("enginesound")){
        		    		    			SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
        		    		    			String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
        		    		    			String[] timeset = timevisit.split(",");
        		    		    			String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        		    		    			if(timeDiv[1].equals(String.valueOf(chagehr))){
        		    		    				if(timeDiv[4].equals("am") && i < 12){
        											visitorCount++;
        										}else if(timeDiv[4].equals("pm") && i >= 12){
        											visitorCount++;
        										}
        		    		    			}
        		    		    		}
        		    		    	}else if(analyType.equals("virtualtour")){
        		    		    		if(dataArr[2].equals("virtualTour")){
        		    		    			SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
        		    		    			String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
        		    		    			String[] timeset = timevisit.split(",");
        		    		    			String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        		    		    			if(timeDiv[1].equals(String.valueOf(chagehr))){
        		    		    				if(timeDiv[4].equals("am") && i < 12){
        											visitorCount++;
        										}else if(timeDiv[4].equals("pm") && i >= 12){
        											visitorCount++;
        										}
        		    		    			}
        		    		    		}
        		    		    	}
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        			
        		}else{
        		
        		String params = "&date="+dates+"&type=visitors-list&limit=all";
        		try {
        			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){

    	    				  	SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
    	    					  String timevisit = jsonArray.getJSONObject(j).get("time_pretty").toString();
    	    					  String[] timeset = timevisit.split(",");
									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
									if(timeDiv[1].equals(String.valueOf(chagehr))){
										if(analyType.equals("user")){
		    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
		    	    						  
		    	    						  if (langValue == null) {
		    	    							  visitorCount = visitorCount + 1;
		    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
		    	    						  	}
		    	    					  	}else if(analyType.equals("pageview")){
		    	    					  		if(timeDiv[4].equals("am") && i < 12){
													visitorCount = visitorCount + 1;
												}else if(timeDiv[4].equals("pm") && i >= 12){
													visitorCount = visitorCount + 1;
												}
		    	    					  	}else if(analyType.equals("sessionDuration")){
		    	    					  		
		    	    					  		
		    	    					  		if(timeDiv[4].equals("am") && i < 12){
													visitorCount = visitorCount + 1;
													totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
												}else if(timeDiv[4].equals("pm") && i >= 12){
													visitorCount = visitorCount + 1;
													totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
												}
		    	    					  		
		    	    					  	}
										
									}
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        		
        		if(analyType.equals("sessionDuration")){
        			if(visitorCount != 0){
            			avgDur = totalTime/visitorCount;	
            		}
        			
        			allVisitor.add(avgDur);
        		}else{
        			allVisitor.add(visitorCount);
        		}
        		
        		String valueTime = null;
        		if(i == 0){
        			valueTime = "12am";
        		}
        		if(i >= 12){
        			if(i == 12){
        				valueTime = "12pm";
        			}else{
        				int chang = i - 12;
        				valueTime = chang+"pm";
        			}
        			
        		}else{
        			if(i != 0){
        				valueTime = i+"am";
        			}
        		}
        		months.add(valueTime);
        		
        		if(i == 0){
        			chagehr = 0;
        		}else{
        			if(chagehr == 12){
        				chagehr = 0;
        			}
        		}
        	}
        	
    	}
    
    	Map map = new HashMap();
    	map.put("allVisitor", allVisitor);
    	map.put("months", months);
    	return ok(Json.toJson(map));
    }
    
    public static Result getAllMailDaysUserStats(String lasttime,String analyType) {
    	

    	List<Integer> allVisitor = new ArrayList<Integer>(30);
    	List<String> months = new ArrayList<String>(30);
    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
    	int visitorCount = 0;
    	int totalTime = 0;
    	int avgDur = 0;
    	Calendar c = Calendar.getInstance();
    	
    	if(lasttime.equals("month")){
    		
        	String[] monthsArr = new String[30]; 
        	c.add(Calendar.DAY_OF_YEAR, -29);
        	
        	for(int i=0;i<30;i++) {
        		mapRM.clear();
        		visitorCount = 0;
        		totalTime = 0;
        		avgDur = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		if(days.length() < 2){
        			days = 0+days;
        		}
        		if(month.length() < 2){
        			month = 0+month;
        		}
        		String dates = year + month + days;
        		
        			
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        					int count = data.split("#").length - 1;
        		    		 if(count == 2){
        		    			 if(analyType.equals("requestMore")){
        		    				 if(dataArr[2].equals("requestmoreinfo")){
         		    					visitorCount++;
         		    				 }
        		    			 }else if(analyType.equals("scheduleTest")){
        		    				 if(dataArr[2].equals("scheduletest")){
          		    					visitorCount++;
          		    				 }
        		    			 }else if(analyType.equals("tradeInApp")){
        		    				 if(dataArr[2].equals("tradeinapp")){
           		    					visitorCount++;
           		    				 }
         		    			 }else if(analyType.equals("emailToFrd")){
        		    				 if(dataArr[2].equals("emailtofriend")){
            		    					visitorCount++;
            		    				 }
          		    			 }
        		    			 
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		
        			allVisitor.add(visitorCount);
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("week")){
    		
        	String[] monthsArr = new String[7]; 
        	c.add(Calendar.DAY_OF_YEAR, -6);
        	
        	for(int i=0;i<7;i++) {
        		mapRM.clear();
        		visitorCount = 0;
        		totalTime = 0;
        		avgDur = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		if(days.length() < 2){
        			days = 0+days;
        		}
        		if(month.length() < 2){
        			month = 0+month;
        		}
        		String dates = year + month + days;
        		
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        						if(count == 2){
           		    			 if(analyType.equals("requestMore")){
           		    				 if(dataArr[2].equals("requestmoreinfo")){
            		    					visitorCount++;
            		    				 }
           		    			 }else if(analyType.equals("scheduleTest")){
           		    				 if(dataArr[2].equals("scheduletest")){
             		    					visitorCount++;
             		    				 }
           		    			 }else if(analyType.equals("tradeInApp")){
           		    				 if(dataArr[2].equals("tradeinapp")){
              		    					visitorCount++;
              		    				 }
            		    			 }else if(analyType.equals("emailToFrd")){
           		    				 if(dataArr[2].equals("emailtofriend")){
               		    					visitorCount++;
               		    				 }
             		    			 }
           		    			 
           		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		
        			allVisitor.add(visitorCount);
        		
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("day")){
    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = new Date();
    		List<String> monthsArr = new ArrayList<>();
    		
    		int chagehr = 0;
    		totalTime = 0;
    		avgDur = 0;
    		
    		String dates = dateFormat.format(date);
        	for(int i=0;i<24;i++) {
        		mapRM.clear();
        		if(i == 0){
        			chagehr = 12;
        		}else{
        			chagehr++;
        		}
        		
        		visitorCount = 0;
        		
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			//String params = "&date=2015-12-04&type=actions-list&limit=all";
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        		    		    if(count == 2){
        		    		    	if(analyType.equals("requestMore")){
        		    		    	 if(dataArr[2].equals("requestmoreinfo")){
        		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
          	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
          	    					    String[] timeset = timevisit.split(",");
      									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
      									if(timeDiv[1].equals(String.valueOf(chagehr))){
      										if(timeDiv[4].equals("am") && i < 12){
												visitorCount = visitorCount + 1;
											}else if(timeDiv[4].equals("pm") && i >= 12){
												visitorCount = visitorCount + 1;
											}
      									}
  	        		    			 }
        		    		       }else if(analyType.equals("scheduleTest")){
        		    		    	   if(dataArr[2].equals("scheduletest")){
           		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
             	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
             	    					    String[] timeset = timevisit.split(",");
         									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
         									if(timeDiv[1].equals(String.valueOf(chagehr))){
         										if(timeDiv[4].equals("am") && i < 12){
													visitorCount = visitorCount + 1;
												}else if(timeDiv[4].equals("pm") && i >= 12){
													visitorCount = visitorCount + 1;
												}
         									}
     	        		    			 }
        		    		       }else if(analyType.equals("tradeInApp")){
        		    		    	   if(dataArr[2].equals("tradeinapp")){
              		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
                	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
                	    					    String[] timeset = timevisit.split(",");
            									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            									if(timeDiv[1].equals(String.valueOf(chagehr))){
            										if(timeDiv[4].equals("am") && i < 12){
    													visitorCount = visitorCount + 1;
    												}else if(timeDiv[4].equals("pm") && i >= 12){
    													visitorCount = visitorCount + 1;
    												}
            									}
        	        		    			 }
        		    		       }else if(analyType.equals("emailToFrd")){
        		    		    	   if(dataArr[2].equals("emailtofriend")){
             		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
               	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
               	    					    String[] timeset = timevisit.split(",");
           									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
           									if(timeDiv[1].equals(String.valueOf(chagehr))){
           										if(timeDiv[4].equals("am") && i < 12){
													visitorCount = visitorCount + 1;
												}else if(timeDiv[4].equals("pm") && i >= 12){
													visitorCount = visitorCount + 1;
												}
           									}
       	        		    			 }
        		    		       }
      									
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		
        		
        			allVisitor.add(visitorCount);
        		
        		
        		String valueTime = null;
        		if(i == 0){
        			valueTime = "12am";
        		}
        		if(i >= 12){
        			if(i == 12){
        				valueTime = "12pm";
        			}else{
        				int chang = i - 12;
        				valueTime = chang+"pm";
        			}
        			
        		}else{
        			if(i != 0){
        				valueTime = i+"am";
        			}
        		}
        		months.add(valueTime);
        		
        		if(i == 0){
        			chagehr = 0;
        		}else{
        			if(chagehr == 12){
        				chagehr = 0;
        			}
        		}
        	}
        	
    	}
    
    	Map map = new HashMap();
    	map.put("allVisitor", allVisitor);
    	map.put("months", months);
    	return ok(Json.toJson(map));
    	
    }
    
    public static Result getSessionDaysUserStats(String value,String lasttime,String analyType) {
    	
    	List<Integer> allVisitor = new ArrayList<Integer>(30);
    	List<String> months = new ArrayList<String>(30);
    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
    	int visitorCount = 0;
    	int totalTime = 0;
    	int avgDur = 0;
    	Calendar c = Calendar.getInstance();
    	
    	if(lasttime.equals("month")){
    		
        	String[] monthsArr = new String[30]; 
        	c.add(Calendar.DAY_OF_YEAR, -29);
        	
        	for(int i=0;i<30;i++) {
        		mapRM.clear();
        		visitorCount = 0;
        		totalTime = 0;
        		avgDur = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		if(days.length() < 2){
        			days = 0+days;
        		}
        		if(month.length() < 2){
        			month = 0+month;
        		}
        		String dates = year + month + days;
        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
        			
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        		    		 if(count == 2){
        		    			    if(analyType.equals("enginesound")){
        		    			    	if(dataArr[2].equals("enginesound") && dataArr[1].equals(value)){
        		    			    		visitorCount++;
        		    			    	}
        		    			    }else if(analyType.equals("virtualtour")){
        		    			    
        		    			    	if(dataArr[2].equals("virtualTour") && dataArr[1].equals(value)){
        		    			    		visitorCount++;
        		    			    	}
        		    			    }
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        			
        		}else{
        			String params = "&date="+dates+"&type=visitors-list&limit=all";
        			try {
        				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray.length();j++){
        					String data = jsonArray.getJSONObject(j).get("landing_page").toString();
        					String arr[] = data.split("/");
        					if(arr.length > 5){
        						if(arr[5] != null){
        							if(arr[5].equals(value)){
        								if(analyType.equals("user")){
        									Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
        									if (langValue == null) {
        										visitorCount = visitorCount + 1;
        										mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
        									}
        								}else if(analyType.equals("pageview")){
        									visitorCount = visitorCount + 1;
        								}else if(analyType.equals("sessionDuration")){
        									visitorCount = visitorCount + 1;
        									totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
        								}
        							}
        						}
        					}
        				}	
        			} catch (JSONException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}
        		
        		if(analyType.equals("sessionDuration")){
        			if(visitorCount != 0 || totalTime != 0){
            			avgDur = totalTime/visitorCount;	
            		}
        			
        			allVisitor.add(avgDur);
        		}else{
        			allVisitor.add(visitorCount);
        		}
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("week")){
    		
        	String[] monthsArr = new String[7]; 
        	c.add(Calendar.DAY_OF_YEAR, -6);
        	
        	for(int i=0;i<7;i++) {
        		mapRM.clear();
        		visitorCount = 0;
        		totalTime = 0;
        		avgDur = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		if(days.length() < 2){
        			days = 0+days;
        		}
        		if(month.length() < 2){
        			month = 0+month;
        		}
        		String dates = year + month + days;
            	
        		
        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
        			
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        		    		 if(count == 2){
        		    			 if(analyType.equals("enginesound")){
     		    			    	if(dataArr[2].equals("enginesound") && dataArr[1].equals(value)){
     		    			    		visitorCount++;
     		    			    	}
     		    			    }else if(analyType.equals("virtualtour")){
     		    			    
     		    			    	if(dataArr[2].equals("virtualTour") && dataArr[1].equals(value)){
     		    			    		visitorCount++;
     		    			    	}
     		    			    }
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        			
        		}else{
        		  String params = "&date="+dates+"&type=visitors-list&limit=all";
        		  try {
    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    				  if(arr[5].equals(value)){
    	    					  
    	    					  if(analyType.equals("user")){
    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
    	    						  if (langValue == null) {
    	    							  visitorCount = visitorCount + 1;
    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
    	    						  	}
    	    					  	}else if(analyType.equals("pageview")){
    	    					  		visitorCount = visitorCount + 1;
    	    					  	}else if(analyType.equals("sessionDuration")){
    	    					  		visitorCount = visitorCount + 1;
    	    					  		totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
    	    					  	}
    	    				  }
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        		
        		if(analyType.equals("sessionDuration")){
        			if(visitorCount != 0){
            			avgDur = totalTime/visitorCount;	
            		}
        			
        			allVisitor.add(avgDur);
        		}else{
        			allVisitor.add(visitorCount);
        		}
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("day")){
    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = new Date();
    		List<String> monthsArr = new ArrayList<>();
    		
    		int chagehr = 0;
    		totalTime = 0;
    		avgDur = 0;
    		
    		String dates = dateFormat.format(date);
        	for(int i=0;i<24;i++) {
        		mapRM.clear();
        		if(i == 0){
        			chagehr = 12;
        		}else{
        			chagehr++;
        		}
        		
        		visitorCount = 0;
        		
        		
        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
        			
        			String params = "&date="+dates+"&type=actions-list&limit=all";
        			//String params = "&date=2015-12-09&type=actions-list&limit=all";
        			Map<String, Integer> map = new HashMap<String, Integer>();
        			try {
        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
        				for(int j=0;j<jsonArray1.length();j++){
        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
        					String dataArr[] = data.split("#");
        					if(dataArr!=null && dataArr.length>0){
        						int count = data.split("#").length - 1;
        		    		    if(count == 2){
        		    		    	
        		    		    	 if(analyType.equals("enginesound")){
	        		    		    	if(dataArr[2].equals("enginesound") && dataArr[1].equals(value)){
	        		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
	          	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
	          	    					    String[] timeset = timevisit.split(",");
	      									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
	      									if(i >= 12 && timeDiv[4].equals("pm")){
	      										if(timeDiv[1].equals(String.valueOf(chagehr))){
		      										visitorCount++;
		      									}
	            		    		    	}else if(timeDiv[4].equals("am")){
	            		    		    		if(timeDiv[1].equals(String.valueOf(chagehr))){
		      										visitorCount++;
		      									}
	            		    		    	}
	      									
	      									
	  	        		    			}
        		    		    	 }else if(analyType.equals("virtualtour")){
	        		    		    	if(dataArr[2].equals("virtualTour") && dataArr[1].equals(value)){
	        		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
	          	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
	          	    					    String[] timeset = timevisit.split(",");
	      									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
	      									if(i >= 12 && timeDiv[4].equals("pm")){
	      										if(timeDiv[1].equals(String.valueOf(chagehr))){
		      										visitorCount++;
		      									}
	            		    		    	}else if(i < 12 && timeDiv[4].equals("am")){
	            		    		    		if(timeDiv[1].equals(String.valueOf(chagehr))){
		      										visitorCount++;
		      									}
	            		    		    	}
	        		    		    	}
        		    		    	 }
        		    			}
        					}
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        			
        		}else{
        		
        		String params = "&date="+dates+"&type=visitors-list&limit=all";
        		try {
        			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    				  if(arr[5].equals(value)){
    	    					  SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
    	    					  String timevisit = jsonArray.getJSONObject(j).get("time_pretty").toString();
    	    					  String[] timeset = timevisit.split(",");
									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
									
									if(i >= 12 && timeDiv[4].equals("pm")){
									if(timeDiv[1].equals(String.valueOf(chagehr))){
										if(analyType.equals("user")){
		    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
		    	    						  if (langValue == null) {
		    	    							  visitorCount = visitorCount + 1;
		    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
		    	    						  	}
		    	    					  	}else if(analyType.equals("pageview")){
		    	    					  		visitorCount = visitorCount + 1;
		    	    					  	}else if(analyType.equals("sessionDuration")){
		    	    					  		visitorCount = visitorCount + 1;
		    	    					  		totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
		    	    					  	}
										
									}
									}else if(i < 12 &&timeDiv[4].equals("am")){
										if(timeDiv[1].equals(String.valueOf(chagehr))){
											if(analyType.equals("user")){
			    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
			    	    						  if (langValue == null) {
			    	    							  visitorCount = visitorCount + 1;
			    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
			    	    						  	}
			    	    					  	}else if(analyType.equals("pageview")){
			    	    					  		visitorCount = visitorCount + 1;
			    	    					  	}else if(analyType.equals("sessionDuration")){
			    	    					  		visitorCount = visitorCount + 1;
			    	    					  		totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
			    	    					  	}
											
										}
									  }
									}
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        		
        		if(analyType.equals("sessionDuration")){
        			if(visitorCount != 0){
            			avgDur = totalTime/visitorCount;	
            		}
        			
        			allVisitor.add(avgDur);
        		}else{
        			allVisitor.add(visitorCount);
        		}
        		
        		String valueTime = null;
        		if(i == 0){
        			valueTime = "12am";
        		}
        		if(i >= 12){
        			if(i == 12){
        				valueTime = "12pm";
        			}else{
        				int chang = i - 12;
        				valueTime = chang+"pm";
        			}
        			
        		}else{
        			if(i != 0){
        				valueTime = i+"am";
        			}
        		}
        		months.add(valueTime);
        		
        		if(i == 0){
        			chagehr = 0;
        		}else{
        			if(chagehr == 12){
        				chagehr = 0;
        			}
        		}
        	}
        	
    	}
    
    	Map map = new HashMap();
    	map.put("allVisitor", allVisitor);
    	map.put("months", months);
    	return ok(Json.toJson(map));
    }
    
    
    public static Result getSessionDaysVisitorsStats(String value,String lasttime) {
    	
    	List<Integer> allVisitor = new ArrayList<Integer>(30);
    	List<String> months = new ArrayList<String>(30);
    	int visitorCount = 0;
    	Calendar c = Calendar.getInstance();
    	
    	if(lasttime.equals("month")){
    		
        	String[] monthsArr = new String[30]; 
        	c.add(Calendar.DAY_OF_YEAR, -29);
        	
        	for(int i=0;i<30;i++) {
        		visitorCount = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		String dates = year + month + days;
            	
        		String params = "&date="+dates+"&type=visitors-list&limit=all";
        		try {
    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    				  if(arr[5].equals(value)){
    	    					  visitorCount = visitorCount + 1;
    	    				  }
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		
        		allVisitor.add(visitorCount);
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("week")){
    		
    		
        	String[] monthsArr = new String[7]; 
        	c.add(Calendar.DAY_OF_YEAR, -6);
        	
        	for(int i=0;i<7;i++) {
        		visitorCount = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		String dates = year + month + days;
            	
        		String params = "&date="+dates+"&type=visitors-list&limit=all";
        		try {
    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    				  if(arr[5].equals(value)){
    	    					  visitorCount = visitorCount + 1;
    	    				  }
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		
        		allVisitor.add(visitorCount);
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("day")){
    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = new Date();
    		List<String> monthsArr = new ArrayList<>();
    		
    		int chagehr = 0;
    		String dates = dateFormat.format(date);
    		//String dates = "2015-12-01";
        	for(int i=0;i<24;i++) {
        		
        		if(i == 0){
        			chagehr = 12;
        		}else{
        			chagehr++;
        		}
        		
        		
        		visitorCount = 0;
        		String params = "&date="+dates+"&type=visitors-list&limit=all";
        		try {
        			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    				  if(arr[5].equals(value)){
    	    					  SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
    	    					  String timevisit = jsonArray.getJSONObject(j).get("time_pretty").toString();
    	    					  String[] timeset = timevisit.split(",");
									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
									if(timeDiv[1].equals(String.valueOf(chagehr))){
										if(timeDiv[4].equals("am") && i < 12){
											visitorCount = visitorCount + 1;
										}else if(timeDiv[4].equals("pm") && i >= 12){
											visitorCount = visitorCount + 1;
										}
										
										
									}
    	    				  }
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		
        		allVisitor.add(visitorCount);
        		String valueTime = null;
        		if(i == 0){
        			valueTime = "12am";
        		}
        		if(i >= 12){
        			if(i == 12){
        				valueTime = "12pm";
        			}else{
        				int chang = i - 12;
        				valueTime = chang+"pm";
        			}
        			
        		}else{
        			if(i != 0){
        				valueTime = i+"am";
        			}
        		}
        		months.add(valueTime);
        		
        		if(i == 0){
        			chagehr = 0;
        		}else{
        			if(chagehr == 12){
        				chagehr = 0;
        			}
        		}
        	}
        	
    	}
    
    	Map map = new HashMap();
    	map.put("allVisitor", allVisitor);
    	map.put("months", months);
    	return ok(Json.toJson(map));
    }
    
    public static Result getAllVehicleSession(String lasttime) {
    	List<Integer> allVisitor = new ArrayList<Integer>(30);
    	List<String> months = new ArrayList<String>(30);
    	int visitorCount = 0;
    	Calendar c = Calendar.getInstance();
    	
    	if(lasttime.equals("month")){
    		
        	String[] monthsArr = new String[30]; 
        	c.add(Calendar.DAY_OF_YEAR, -29);
        	
        	for(int i=0;i<30;i++) {
        		visitorCount = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		String dates = year + month + days;
            	
        		String params = "&date="+dates+"&type=visitors-list&limit=all";
        		try {
    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    				 // if(arr[5].equals(value)){
    	    					  visitorCount = visitorCount + 1;
    	    				 // }
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		
        		allVisitor.add(visitorCount);
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("week")){
    		
    		
        	String[] monthsArr = new String[7]; 
        	c.add(Calendar.DAY_OF_YEAR, -6);
        	
        	for(int i=0;i<7;i++) {
        		visitorCount = 0;
        		String year = c.get(Calendar.YEAR)+"-";
        		String days = c.get(Calendar.DATE)+"";
        		Integer addmonth = c.get(Calendar.MONTH);
        		Integer addOneMo = addmonth + 1;
        		String month = String.valueOf(addOneMo)+"-";
        		String dates = year + month + days;
            	
        		String params = "&date="+dates+"&type=visitors-list&limit=all";
        		try {
    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    				  //if(arr[5].equals(value)){
    	    					  visitorCount = visitorCount + 1;
    	    				  //}
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		
        		allVisitor.add(visitorCount);
        		
        		months.add(month+days);
        		c.add(Calendar.DAY_OF_YEAR, 1);
        	}
        	
    	}else if(lasttime.equals("day")){
    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = new Date();
    		List<String> monthsArr = new ArrayList<>();
    		
    		int chagehr = 0;
    		String dates = dateFormat.format(date);
    		//String dates = "2015-12-01";
        	for(int i=0;i<24;i++) {
        		
        		if(i == 0){
        			chagehr = 12;
        		}else{
        			chagehr++;
        		}
        		
        		
        		visitorCount = 0;
        		String params = "&date="+dates+"&type=visitors-list&limit=all";
        		try {
        			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
    				for(int j=0;j<jsonArray.length();j++){
    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
    	    			String arr[] = data.split("/");
    	    			if(arr.length > 5){
    	    			  if(arr[5] != null){
    	    				//  if(arr[5].equals(value)){
    	    					  SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
    	    					  String timevisit = jsonArray.getJSONObject(j).get("time_pretty").toString();
    	    					  String[] timeset = timevisit.split(",");
									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
									if(timeDiv[1].equals(String.valueOf(chagehr))){
										if(timeDiv[4].equals("am") && i < 12){
											visitorCount = visitorCount + 1;
										}else if(timeDiv[4].equals("pm") && i >= 12){
											visitorCount = visitorCount + 1;
										}
									}
    	    				 // }
    	    			  }
    	    			}
    				}	
    				
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		
        		allVisitor.add(visitorCount);
        		String valueTime = null;
        		if(i == 0){
        			valueTime = "12am";
        		}
        		if(i >= 12){
        			if(i == 12){
        				valueTime = "12pm";
        			}else{
        				int chang = i - 12;
        				valueTime = chang+"pm";
        			}
        			
        		}else{
        			if(i != 0){
        				valueTime = i+"am";
        			}
        		}
        		months.add(valueTime);
        		
        		if(i == 0){
        			chagehr = 0;
        		}else{
        			if(chagehr == 12){
        				chagehr = 0;
        			}
        		}
        	}
        	
    	}
    
    	Map map = new HashMap();
    	map.put("allVisitor", allVisitor);
    	map.put("months", months);
    	return ok(Json.toJson(map));
    }
    
    
    public static Result getVisitorStats() {
    	String params = "&type=visitors,visitors-new,bounce-rate&date=last-2-days&daily=1";
    	return ok(Json.parse(callClickAPI(params)));
    }
    
   /* public static Result getVisitedDataOther(String type,String filterBy,String search,String searchBy,Long locationId,Integer managerId, String gmInManag) {
    	AuthUser user = AuthUser.findById(managerId);
    	Map result = new HashMap(3);
    	
    	topListings(type,filterBy,search,searchBy,locationId,user,result,"All",gmInManag);
    	return ok(Json.toJson(result));
    }*/
    
    public static Result getVisitedData(Integer userKey,String type,String filterBy,String search,String searchBy,String vehicles) {
    	
    	Map result = new HashMap(3);
    	AuthUser user = AuthUser.findById(userKey);
    	long locationId = 0;
    	if(user.location == null){
    		locationId = 0L;
    	}else{
    		locationId = user.location.id;
    	}
    	
    	topListings(type,filterBy,search,searchBy,locationId,user,result,vehicles,"0");
    	return ok(Json.toJson(result));
    }
    
    public static void topListings(String type,String filterBy,String search,String searchBy,Long locationId,AuthUser user,Map result,String vehicles,String gmInManag){
    	
    	
    	
    	Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		String start1 = "";
		String end1 = "";
		Date start = null;
		Date end = null;
		if(type.equals("week")) {
			cal.add(Calendar.DATE, -7);
			start1 = df.format(cal.getTime());
			end1 = df.format(date);
			try {
				start = df.parse(start1);
				end = df.parse(end1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(type.equals("month")) {
			cal.add(Calendar.DATE, -30);
			start1 = df.format(cal.getTime());
			end1 = df.format(date);
			try {
				start = df.parse(start1);
				end = df.parse(end1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(type.equals("allTime")) {
			cal.add(Calendar.DATE, -1000);
			start1 = df.format(cal.getTime());
			end1 = df.format(date);
			try {
				start = df.parse(start1);
				end = df.parse(end1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    	
    	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MMM-dd");
    	
    	String params = null;
    	String checkDate = null;
    	Date thedate = null;
    	//String params1 = "&type=visitors-list&date=last-30-days&limit=all";
    	if(type.equals("week")){
    		params = "&type=visitors-list&date=last-7-days&limit=all";
    	}else if(type.equals("month")){
    		params = "&type=visitors-list&date=last-30-days&limit=all";
    	}else if(type.equals("allTime")){
    		params = "&type=visitors-list&date="+start1+","+end1+"&limit=all";
    	}
    	String resultStr = callClickAPI(params);
    	    	
    	JsonNode jsonNode = Json.parse(resultStr).get(0).get("dates").get(0).get("items");
    	    	
    	List<String> vins = new ArrayList<String>();
    	List<String> vins1 = new ArrayList<String>();
    	
    	Map<String,Integer> pagesCount = new HashMap<String,Integer>();
    	Map<String,Integer> vinUnik = new HashMap<String,Integer>();
    	
    	Map<String,Integer> pagesCount1 = new HashMap<String,Integer>();
    	Map<String,Integer> vinUnik1 = new HashMap<String,Integer>();
    	int i = 1;
    	
    	for(JsonNode item:jsonNode) {
    		String data = item.get("landing_page").toString();
			String arrVin[] = data.split("/");
			if(arrVin.length > 5){
				String van[] = arrVin[5].split("\"");
				System.out.println(van[0]);
				Vehicle vehicle = null;
				if(user.role.equals("Sales Person") || user.role.equals("Manager") || gmInManag.equals("1")){
					vehicle = Vehicle.findByVinAndStatusForGM(van[0],Location.findById(locationId));
				}else{
					vehicle = Vehicle.findByVinAndStatus(van[0]);
				}
				
				if(vehicle !=null){
					String arr[] = item.get("time_pretty").toString().split(" ");
					String arrNew[] = arr[3].split(",");
					checkDate = arrNew[0]+"-"+arr[1]+"-"+arr[2];
					try {
						thedate = df1.parse(checkDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					if (vehicle.postedDate.before(thedate) || vehicle.postedDate.equals(thedate)) {
						if(pagesCount1.get(vehicle.vin) !=null){
							int j = pagesCount1.get(vehicle.vin);
							pagesCount1.put(vehicle.vin, j+1);
						}else{
							pagesCount1.put(vehicle.vin, 1);
						}
						
					}
				}
				
			}
    	}
    	
    	List<Vehicle> vlist = null;
    	if(user.role.equals("Sales Person") || user.role.equals("Manager") || gmInManag.equals("1")){
    		vlist = Vehicle.findByNewlyArrivedForGM(Location.findById(locationId));
		}else{
			vlist = Vehicle.findByNewlyArrived();
		}
    	
    	for (Vehicle vehicle : vlist) {
    		if(vehicle.status.equals("Sold")){
    			//if((vehicle.soldDate.after(start) && vehicle.soldDate.before(end)) || vehicle.soldDate.equals(end)){
    				vins1.add(vehicle.vin);
    			//}
    		}else{
    			//if((vehicle.postedDate.after(start) && vehicle.postedDate.before(end)) || vehicle.postedDate.equals(end)){
    				vins1.add(vehicle.vin);
    			//}	
    		}
    		
		}
    	if(user.role.equals("Sales Person") || user.role.equals("Manager") || gmInManag.equals("1")){
			List<RequestMoreInfo> rMoreInfo = RequestMoreInfo.findAllSeenSch(user);
			List<ScheduleTest> sTests = ScheduleTest.findAllAssigned(user);
			List<TradeIn> tIns = TradeIn.findAllSeenSch(user);
			
			for(RequestMoreInfo rInfo :rMoreInfo){
				if((rInfo.requestDate.after(start) && rInfo.requestDate.before(end)) || rInfo.requestDate.equals(end)){
					vinUnik.put(rInfo.vin, 1);
				}
			}
			for(ScheduleTest sTest: sTests){
				if((sTest.scheduleDate.after(start) && sTest.scheduleDate.before(end)) || sTest.scheduleDate.equals(end)){
					vinUnik.put(sTest.vin, 1);
				}	
			}
			for(TradeIn tradeIn: tIns){
				if((tradeIn.tradeDate.after(start) && tradeIn.tradeDate.before(end)) || tradeIn.tradeDate.equals(end)){
					vinUnik.put(tradeIn.vin, 1);
				}
			}
			for (Map.Entry<String, Integer> entry : vinUnik.entrySet()) {
				vins.add(entry.getKey());
			}
    	}
    	List<Vehicle> topVisited =null;
    	List<Vehicle> topVisitedSold =null;
    	
if(vehicles.equals("All")){
	topVisited = Vehicle.findByVins(vins);
}else{
	topVisited = Vehicle.findByVinsAndTypeVehi(vins,vehicles);
}

/*if(vehicles.equals("All")){
	topVisitedSold = Vehicle.findByVinsforSoldUser(user);
}else{
	topVisitedSold = Vehicle.findByVinsAndTypeVehiforSoldUser(user,vehicles);
}*/

    	
    	List<VehicleAnalyticalVM> topVisitedVms = new ArrayList<>();
    	
    	for(Vehicle vehicle:topVisited) {
			
    		VehicleAnalyticalVM analyticalVM = new VehicleAnalyticalVM();
    		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
    		List<ScheduleTest> sList = ScheduleTest.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
    		List<TradeIn> tIns = TradeIn.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
    		
    		analyticalVM.leadsCount = rInfos.size() + sList.size() + tIns.size();
    		
    		if(pagesCount.get(vehicle.getVin()) == null){
    			analyticalVM.count = 0;
    		}else{
    			analyticalVM.count = pagesCount.get(vehicle.getVin());
    		}
    		analyticalVM.stockNumber = vehicle.stock;
    		analyticalVM.followerCount = 0;
    		List<PriceAlert> pAlert = PriceAlert.getEmailsByVin(vehicle.getVin(), locationId);
    		for (PriceAlert priceAlert : pAlert) {
				PriceAlert alt = PriceAlert.findById(priceAlert.id);
				if (vehicle.postedDate.before(alt.currDate) || vehicle.postedDate.equals(alt.currDate)) {
					analyticalVM.followerCount++;
	    		}
			}
    		
    		analyticalVM.vehicleStatus=vehicle.getStatus();
    		analyticalVM.price = vehicle.getPrice();
    		VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.getVin());
    		if(vehicleImage!=null) {
    			analyticalVM.id = vehicleImage.getId();
    			analyticalVM.isImage = true;
    		}
    		else {
    			analyticalVM.defaultImagePath = "/assets/images/no-image.jpg";
    		}
    		analyticalVM.vin = vehicle.getVin();
    	//	analyticalVM.name = vehicle.getMake() + " "+ vehicle.getModel()+ " "+ vehicle.getYear();
    		analyticalVM.name=vehicle.getTitle();
    		if(!searchBy.equals("0") && !search.equals("0")){
	    		if(searchBy.equals("Model")){
    				if(vehicle.model.toUpperCase().startsWith(search.toUpperCase())){
    					topVisitedVms.add(analyticalVM);
    				}
    			}else if(searchBy.equals("Make")){
    				if(vehicle.make.toUpperCase().startsWith(search.toUpperCase())){
    					topVisitedVms.add(analyticalVM);
    				}
    			}
    		}else{
    			topVisitedVms.add(analyticalVM);
    		}
	}
    	
    	/*for(Vehicle vehicle:topVisitedSold) {
    		if((vehicle.soldDate.after(start) && vehicle.soldDate.before(end)) || vehicle.soldDate.equals(end)){
    			VehicleAnalyticalVM analyticalVM = new VehicleAnalyticalVM();
	    		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
	    		List<ScheduleTest> sList = ScheduleTest.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
	    		List<TradeIn> tIns = TradeIn.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
	    		
	    		analyticalVM.leadsCount = rInfos.size() + sList.size() + tIns.size();
	    		
	    		if(pagesCount.get(vehicle.getVin()) == null){
	    			analyticalVM.count = 0;
	    		}else{
	    			analyticalVM.count = pagesCount.get(vehicle.getVin());
	    		}
	    		analyticalVM.stockNumber = vehicle.stock;
	    		analyticalVM.followerCount = 0;
	    		List<PriceAlert> pAlert = PriceAlert.getEmailsByVin(vehicle.getVin(), locationId);
	    		for (PriceAlert priceAlert : pAlert) {
					PriceAlert alt = PriceAlert.findById(priceAlert.id);
					if (vehicle.postedDate.before(alt.currDate) || vehicle.postedDate.equals(alt.currDate)) {
						analyticalVM.followerCount++;
		    		}
				}
	    		
	    		analyticalVM.vehicleStatus=vehicle.getStatus();
	    		analyticalVM.price = vehicle.getPrice();
	    		VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.getVin());
	    		if(vehicleImage!=null) {
	    			analyticalVM.id = vehicleImage.getId();
	    			analyticalVM.isImage = true;
	    		}
	    		else {
	    			analyticalVM.defaultImagePath = "/assets/images/no-image.jpg";
	    		}
	    		analyticalVM.vin = vehicle.getVin();
	    	//	analyticalVM.name = vehicle.getMake() + " "+ vehicle.getModel()+ " "+ vehicle.getYear();
	    		analyticalVM.name=vehicle.getTitle();
	    		if(!searchBy.equals("0") && !search.equals("0")){
		    		if(searchBy.equals("Model")){
	    				if(vehicle.model.toUpperCase().startsWith(search.toUpperCase())){
	    					topVisitedVms.add(analyticalVM);
	    				}
	    			}else if(searchBy.equals("Make")){
	    				if(vehicle.make.toUpperCase().startsWith(search.toUpperCase())){
	    					topVisitedVms.add(analyticalVM);
	    				}
	    			}
	    		}else{
	    			topVisitedVms.add(analyticalVM);
	    		}
    		}
	    		
    	}*/
    	List<VehicleAnalyticalVM> worstVisitedVms = new ArrayList<>();
    	List<Vehicle> notVisitedVehicle = Vehicle.findByNotInVins(vins);
    	for(Vehicle vehicle:notVisitedVehicle) {
    		VehicleAnalyticalVM analyticalVM = new VehicleAnalyticalVM();
    		List<PriceAlert> pAlert;
    		if(user.role.equals("General Manager")){
    			List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinStatus(vehicle.getVin());
        		List<ScheduleTest> sList = ScheduleTest.findByVinStatus(vehicle.getVin());
        		List<TradeIn> tIns = TradeIn.findByVinStatus(vehicle.getVin());
        		analyticalVM.leadsCount = rInfos.size() + sList.size() + tIns.size();
        		pAlert = PriceAlert.getByVin(vehicle.getVin());
    		}else{
    			List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
        		List<ScheduleTest> sList = ScheduleTest.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
        		List<TradeIn> tIns = TradeIn.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
        		analyticalVM.leadsCount = rInfos.size() + sList.size() + tIns.size();
        		pAlert = PriceAlert.getEmailsByVin(vehicle.getVin(), locationId);
    		}
    		
    		
    		analyticalVM.count = 0;
    		analyticalVM.followerCount = 0;
    		for (PriceAlert priceAlert : pAlert) {
				PriceAlert alt = PriceAlert.findById(priceAlert.id);
				if (vehicle.postedDate.before(alt.currDate) || vehicle.postedDate.equals(alt.currDate)) {
					analyticalVM.followerCount++;
	    		}
			}
    		analyticalVM.price = vehicle.getPrice();
    		analyticalVM.stockNumber = vehicle.stock;
    		VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.getVin());
    		if(vehicleImage!=null) {
    			analyticalVM.id = vehicleImage.getId();
    			analyticalVM.isImage = true;
    		}
    		else {
    			analyticalVM.defaultImagePath = "/assets/images/no-image.jpg";
    		}
    		analyticalVM.vin = vehicle.getVin();
    		//analyticalVM.name = vehicle.getMake() + " "+ vehicle.getModel()+ " "+ vehicle.getYear();
    		analyticalVM.name=vehicle.getTitle();
    		
    		if(!searchBy.equals("0") && !search.equals("0")){
    			if(searchBy.equals("Model")){
    				if(vehicle.model.toUpperCase().startsWith(search.toUpperCase())){
    					worstVisitedVms.add(analyticalVM);
    				}
    			}else if(searchBy.equals("Make")){
    				if(vehicle.make.toUpperCase().startsWith(search.toUpperCase())){
    					worstVisitedVms.add(analyticalVM);
    				}
    			}
    		}else{
    			worstVisitedVms.add(analyticalVM);
    		}
    	}  	
    	
    	List<VehicleAnalyticalVM> allVehical = new ArrayList<>();
    	 List<Vehicle> aVehicles =null;
    	//aVehicles = Vehicle.findByNewArrAndLocation(Long.valueOf(session("USER_LOCATION")));
    	 
    	 // aVehicles = Vehicle.findByVins(vins1);
    	 if(vehicles.equals("All")){
    		 aVehicles = Vehicle.findByVins(vins1);
    		}else{
    			aVehicles= Vehicle.findByVinsAndTypeVehi(vins1,vehicles);
    		}
    	 //if((vehicle.postedDate.after(start) && vehicle.postedDate.before(end)) || vehicle.postedDate.equals(end)){
    	 
    	for(Vehicle vehicle:aVehicles) {
    		VehicleAnalyticalVM anVm = new VehicleAnalyticalVM();
    		//anVm.count = pagesCount1.get(vehicle.getVin());
    		VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.getVin());
    		if(vehicleImage!=null) {
    			anVm.id = vehicleImage.getId();
    			anVm.isImage = true;
    		}
    		else {
    			anVm.defaultImagePath = "/assets/images/no-image.jpg";
    		}
    		
    		List<RequestMoreInfo> rInfos;
    		List<ScheduleTest> sList;
    		List<TradeIn> tIns;
    		List<PriceAlert> pAlert;
    		if(user.role.equals("General Manager")){
    			rInfos = RequestMoreInfo.findByVinStatus(vehicle.getVin());
        		sList = ScheduleTest.findByVinStatus(vehicle.getVin());
        		tIns = TradeIn.findByVinStatus(vehicle.getVin());
        		pAlert = PriceAlert.getByVin(vehicle.getVin());
    		}else{
    			rInfos = RequestMoreInfo.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
        		sList = ScheduleTest.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
        		tIns = TradeIn.findByVinAndLocation(vehicle.getVin(), Location.findById(locationId));
        		pAlert = PriceAlert.getEmailsByVin(vehicle.getVin(), locationId);
    		}
    		int requCount = 0;
    		int schedCount = 0;
    		int tradeCount = 0;
    		for(RequestMoreInfo rInfo : rInfos){
    			if((rInfo.requestDate.after(start) && rInfo.requestDate.before(end)) || rInfo.requestDate.equals(end)){
    				requCount++;
    			}
    		}
    		
    		for(ScheduleTest sInfo : sList){
    			if((sInfo.scheduleDate.after(start) && sInfo.scheduleDate.before(end)) || sInfo.scheduleDate.equals(end)){
    				schedCount++;
    			}
    		}
    		
    		for(TradeIn tInfo : tIns){
    			if((tInfo.tradeDate.after(start) && tInfo.tradeDate.before(end)) || tInfo.tradeDate.equals(end)){
    				tradeCount++;
    			}
    		}
    		
    		
    		anVm.leadsCount = requCount + schedCount + tradeCount;
    		
    		Location loc = Location.findById(vehicle.locations.id);
    		anVm.location= loc.name;
    		anVm.vehicleStatus=vehicle.getStatus();
    		anVm.vin = vehicle.getVin();
    		anVm.price = vehicle.getPrice();
    		//anVm.name = vehicle.getMake() + " "+ vehicle.getModel()+ " "+ vehicle.getYear();
    		anVm.name=vehicle.getTitle();
    		
    		if(pagesCount1.get(vehicle.getVin()) !=null){
    			anVm.count =  pagesCount1.get(vehicle.getVin());
    		}else{
    			anVm.count = 0;
    		}
    		
    		anVm.followerCount = 0;
    		anVm.stockNumber = vehicle.stock;
    		for (PriceAlert priceAlert : pAlert) {
				PriceAlert alt = PriceAlert.findById(priceAlert.id);
				if((alt.currDate.after(start) && alt.currDate.before(end)) || alt.currDate.equals(end)){
					if (vehicle.postedDate.before(alt.currDate) || vehicle.postedDate.equals(alt.currDate)) {
						anVm.followerCount++;
		    		}
				}
			}
    		if(!searchBy.equals("0") && !search.equals("0")){
    			if(searchBy.equals("Model")){
    				if(vehicle.model.toUpperCase().startsWith(search.toUpperCase())){
    					allVehical.add(anVm);
    				}
    			}else if(searchBy.equals("Make")){
    				if(vehicle.make.toUpperCase().startsWith(search.toUpperCase())){
    					allVehical.add(anVm);
    				}
    			}
    		}else{
    			allVehical.add(anVm);
    		}
    	}
    	
    	if(filterBy.equals("countLow")){
    		java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorCountHigh());
    		java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorCountHigh());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorCountHigh());
    	}else if(filterBy.equals("countHigh")){
    		java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorCountLow());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorCountLow());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorCountLow());
    	}
    	
    	if(filterBy.equals("priceHigh")){
    		java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorPriceHigh());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorPriceHigh());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorPriceHigh());
    	}else if(filterBy.equals("priceLow")){
    		java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorPriceLow());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorPriceLow());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorPriceLow());
    	}
    	if(filterBy.equals("followerHigh")){
    		java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorFollowerHigh());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorFollowerHigh());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorFollowerHigh());
    	}else if(filterBy.equals("followerLow")){
    		java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorFollowerLow());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorFollowerLow());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorFollowerLow());
    	}
    	
    	if(filterBy.equals("leadsHigh")){
    		java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorLeadsHigh());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorLeadsHigh());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorLeadsHigh());
    	}else if(filterBy.equals("leadsLow")){
    		java.util.Collections.sort(worstVisitedVms,new VehicleVMComparatorLeadsLow());
        	java.util.Collections.sort(topVisitedVms,new VehicleVMComparatorLeadsLow());
        	java.util.Collections.sort(allVehical,new VehicleVMComparatorLeadsLow());
    	}
    	
    	
    	
    	
    	result.put("worstVisited", worstVisitedVms);
    	result.put("topVisited", topVisitedVms);
    	result.put("allVehical", allVehical);
    }
    
    
    public static class VehicleVMComparatorCountHigh implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.count > o2.count ? -1 : o1.count < o2.count ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorPriceHigh implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.price > o2.price ? -1 : o1.price < o2.price ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorFollowerHigh implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.followerCount > o2.followerCount ? -1 : o1.followerCount < o2.followerCount ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorLeadsHigh implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.leadsCount > o2.leadsCount ? -1 : o1.leadsCount < o2.leadsCount ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorCountLow implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.count < o2.count ? -1 : o1.count > o2.count ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorPriceLow implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.price< o2.price ? -1 : o1.price > o2.price ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorFollowerLow implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.followerCount < o2.followerCount ? -1 : o1.followerCount > o2.followerCount ? 1 : 0;
		}
    }
    
    public static class VehicleVMComparatorLeadsLow implements Comparator<VehicleAnalyticalVM> {
		@Override
		public int compare(VehicleAnalyticalVM o2,VehicleAnalyticalVM o1) {
			return o1.leadsCount < o2.leadsCount ? -1 : o1.leadsCount > o2.leadsCount ? 1 : 0;
		}
    }
    
    public static class VehicleAnalyticalVM {
    	public String name;
    	public String vehicleStatus;
    	public String location;
    	public int count;
    	public Long id;
    	public String vin;
    	public String stockNumber;
    	public Integer price;
    	public Integer followerCount;
    	public Integer leadsCount;
    	public Integer flag = 0;
    	public boolean isImage = false;
    	public String defaultImagePath;
    }
    
    public static Result getMakes() {
    	Set<String> makes = new HashSet<String>();
    	
    	List<Vehicle> vehicles = Vehicle.findByLocation(Long.valueOf(session("USER_LOCATION")));
    	for(Vehicle vehicle:vehicles) {
    		makes.add(vehicle.getMake());
    	}
    	Map map = new HashMap();
    	map.put("makes", makes);
    	return ok(Json.toJson(map));
    }
    
    public static Result getModels(String make) {
    	Set<String> models = new HashSet<String>();
    	List<Vehicle> vehicles = Vehicle.getVehiclesByMake(make, Location.findById(Long.valueOf(session("USER_LOCATION"))));
    	for(Vehicle vehicle:vehicles) {
    		models.add(vehicle.getModel());
    	}
    	return ok(Json.toJson(models));
    }
    
    public static Result getStockDetails(String stockNumber) {
    	List<Vehicle> vehicles = Vehicle.findByStock(stockNumber,Location.findById(Long.valueOf(session("USER_LOCATION"))));
    	Map map = new HashMap();
    	if(vehicles.size()>0) {
    		map.put("isData", true);
    		map.put("make", vehicles.get(0).getMake());
    		map.put("model", vehicles.get(0).getModel());
    		map.put("year", vehicles.get(0).getYear());
    		map.put("bodyStyle", vehicles.get(0).getBodyStyle());
    		map.put("engine", vehicles.get(0).getEngine());
    		map.put("stock", vehicles.get(0).getStock());
    		map.put("mileage", vehicles.get(0).getMileage());
    		map.put("transmission", vehicles.get(0).getTransmission());
    		map.put("drivetrain", vehicles.get(0).getDrivetrain());
    		map.put("vin", vehicles.get(0).getVin());
    		//map.put("vehicleImage", vehicles.get(0).getDrivetrain());
    		VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicles.get(0).vin);
    		if(vehicleImage!=null) {
    			map.put("imgId",vehicleImage.getId());
    		}
    		else {
    			map.put("imgId","/assets/images/no-image.jpg");
    		}
    		
    		
    	} else {
    		map.put("isData", false);
    	}
    	return ok(Json.toJson(map));
    	
    }
    
    
    
    /*
    
    for(Vehicle vehicle:topVisited) {
		
		VehicleAnalyticalVM analyticalVM = new VehicleAnalyticalVM();
		List<RequestMoreInfo> rInfos = RequestMoreInfo.findByVinAndLocation(vehicle.getVin(), Location.findById(Long.valueOf(session("USER_LOCATION"))));
		List<ScheduleTest> sList = ScheduleTest.findByVinAndLocation(vehicle.getVin(), Location.findById(Long.valueOf(session("USER_LOCATION"))));
		List<TradeIn> tIns = TradeIn.findByVinAndLocation(vehicle.getVin(), Location.findById(Long.valueOf(session("USER_LOCATION"))));
		
		analyticalVM.leadsCount = rInfos.size() + sList.size() + tIns.size();
		
		if(pagesCount.get(vehicle.getVin()) == null){
			analyticalVM.count = 0;
		}else{
			analyticalVM.count = pagesCount.get(vehicle.getVin());
		}
		
		List<PriceAlert> pAlert = PriceAlert.getEmailsByVin(vehicle.getVin(), Long.valueOf(session("USER_LOCATION")));
		if(pAlert != null){
			analyticalVM.followerCount =  pAlert.size();
		}else{
			analyticalVM.followerCount = 0;
		}
		analyticalVM.price = vehicle.getPrice();
		VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.getVin());
		if(vehicleImage!=null) {
			analyticalVM.id = vehicleImage.getId();
			analyticalVM.isImage = true;
		}
		else {
			analyticalVM.defaultImagePath = "/assets/images/no-image.jpg";
		}
		analyticalVM.vin = vehicle.getVin();
		analyticalVM.name = vehicle.getMake() + " "+ vehicle.getModel()+ " "+ vehicle.getYear();
		
		if(!searchBy.equals("0") && !search.equals("0")){
    		if(searchBy.equals("Model")){
				if(vehicle.model.toUpperCase().startsWith(search.toUpperCase())){
					topVisitedVms.add(analyticalVM);
				}
			}else if(searchBy.equals("Make")){
				if(vehicle.make.toUpperCase().startsWith(search.toUpperCase())){
					topVisitedVms.add(analyticalVM);
				}
			}
		}else{
			topVisitedVms.add(analyticalVM);
		}
}*/
    
    public static Result getDateRangSalePerson(String startDate,String endDate){
    	
    	DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    	
    	Map<String, Integer> mapSalePerson = new HashMap<String, Integer>();
    	
    	Date startD = null;
 		try {
 			startD = df1.parse(startDate);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     	Date endD = null;
 		try {
 			endD = df1.parse(endDate);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		
 		int pricecount = 0;
 		int maxPrice = 0;
 		int salePersonId = 0;
 		
 		List<AuthUser> aUser = AuthUser.getAllUserByLocation(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    	
 		for(AuthUser authUser:aUser){
 			pricecount = 0;
 			List<Vehicle> vList = Vehicle.findBySoldUserAndSold(authUser);
 			for (Vehicle vehicle : vList) {
 				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(startD) || vehicle.soldDate.equals(endD)) {
 	    			pricecount = pricecount + vehicle.price;
 				}
 			}
 			if(pricecount > maxPrice){
 				maxPrice = pricecount;
 				salePersonId = authUser.id;
 			}
 			
 		}
    	
		return ok(Json.toJson(salePersonId));
    }
    
 
    public static Result getComperSalePersonData(Integer id,String startDate,String endDate){
    
    	DateFormat onlyMonth = new SimpleDateFormat("MMMM");
     	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
     	DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
     	Date dateobj = new Date();
     	Calendar cal1 = Calendar.getInstance();
     	//AuthUser users = (AuthUser) getLocalUser();
     	
     	AuthUser users = AuthUser.findById(id);
     	
     	String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
 		        "August", "September", "October", "November", "December" };
     	
     	Calendar cal = Calendar.getInstance();  
      	String monthCal = monthName[cal.get(Calendar.MONTH)];
     	
     	Map<String, Integer> mapByType = new HashMap<String, Integer>();
     	
     	Date startD = null;
 		try {
 			startD = df1.parse(startDate);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     	Date endD = null;
 		try {
 			endD = df1.parse(endDate);
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     	
     	int requestLeadCount = 0;
     	int scheduleLeadCount = 0;
     	int tradeInLeadCount = 0;
     	
     	int lostRequestLeadCount = 0;
     	int lostScheduleLeadCount = 0;
     	int lostTradeInLeadCount = 0;
     	
     	int requestLeadCount1 = 0;
     	int scheduleLeadCount1 = 0;
     	int tradeInLeadCount1 = 0;
     	
     	List<RequestMoreInfo> rInfo = null;
     	List<ScheduleTest> sList = null;
     	List<TradeIn> tradeIns = null;
     	List<RequestMoreInfo> rInfoAll = null;
     	List<ScheduleTest> sListAll = null;
     	List<TradeIn> tradeInsAll = null;
    	List<RequestMoreInfo> lostRInfo = null;
     	List<ScheduleTest> lostSList = null;
     	List<TradeIn> lostTradeIns = null;
     	
	     	lostRInfo = RequestMoreInfo.findAllLostSch(users);
	     	lostSList = ScheduleTest.findAllLostSch(users);
	     	lostTradeIns = TradeIn.findAllLostSch(users);
     	
     		rInfo = RequestMoreInfo.findAllSeenSch(users);
     		sList = ScheduleTest.findAllAssigned(users);
     		tradeIns = TradeIn.findAllSeenSch(users);
     		
     		rInfoAll = RequestMoreInfo.findByAssigUserNotCancel(users);
     		sListAll = ScheduleTest.findByAssigUserNotCancel(users);
     		tradeInsAll = TradeIn.findByAssigUserNotCancel(users);
     		
     		Long difffoll = 0L;
     		Long countFollo = 0L; 
     		SimpleDateFormat convdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     		SimpleDateFormat convdTime = new SimpleDateFormat("hh:mm:ss");
     	for(RequestMoreInfo rMoreInfo:rInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
     			requestLeadCount++;
     			difffoll = 0L;
     			List<UserNotes> uNotes = UserNotes.findRequestMoreAndFirstAdd(rMoreInfo);
     			for(UserNotes uN:uNotes){
     				
     				String CretaeDateTime = df1.format(uN.createdDate)+" "+convdTime.format(uN.createdTime);
     				
     				Date cDate = null;
     				try {
						cDate = convdf.parse(CretaeDateTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
     				
     				difffoll = cDate.getTime() - rMoreInfo.requestDate.getTime();
     				//difffoll = (difffoll / 1000 /60 /60 /24);
     				break;
     			}
     			countFollo = countFollo + difffoll;
     		}
     	}
     	
     	for(ScheduleTest sTest:sList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
     			scheduleLeadCount++;
     			
     			difffoll = 0L;
     			List<UserNotes> uNotes = UserNotes.findScheduleTestAndFirstAdd(sTest);
     			for(UserNotes uN:uNotes){
     				System.out.println(uN.createdDate);
     				String CretaeDateTime = df1.format(uN.createdDate)+" "+convdTime.format(uN.createdTime);
     				
     				Date cDate = null;
     				try {
						cDate = convdf.parse(CretaeDateTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
     				
     				difffoll = cDate.getTime() - sTest.scheduleDate.getTime();
     				//difffoll = (difffoll / 1000 /60 /60 /24);
     				break;
     			}
     			countFollo = countFollo + difffoll;
     		}
     	}

     	for(TradeIn tIn:tradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
     				tradeInLeadCount++;
     				
     				difffoll = 0L;
         			List<UserNotes> uNotes = UserNotes.findTradeInAndFirstAdd(tIn);
         			for(UserNotes uN:uNotes){
         				System.out.println(uN.createdDate);
         				
         				String CretaeDateTime = df1.format(uN.createdDate)+" "+convdTime.format(uN.createdTime);
         				
         				Date cDate = null;
         				try {
    						cDate = convdf.parse(CretaeDateTime);
    					} catch (ParseException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
         				
         				difffoll = cDate.getTime() - tIn.tradeDate.getTime();
         				//difffoll = (difffoll / 1000 /60 /60 /24);
         				break;
         			}
         			countFollo = countFollo + difffoll;
     		}
     	}
     	
     	for(RequestMoreInfo rMoreInfo:rInfoAll){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
     			requestLeadCount1++;
     		}
     	}
     	
     	for(ScheduleTest sTest:sListAll){
     	if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
     			scheduleLeadCount1++;
     	}
     	}

     	for(TradeIn tIn:tradeInsAll){
     	if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
 				tradeInLeadCount1++;
     		}
     	}
     	
    	for(RequestMoreInfo rMoreInfo:lostRInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
     			lostRequestLeadCount++;
     		}
     	}
     	
     	for(ScheduleTest sTest:lostSList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
     			lostScheduleLeadCount++;
     		}
     	}

     	for(TradeIn tIn:lostTradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
     				lostTradeInLeadCount++;
     		}
     	}	
 		
     	
     	
     	int countLeads1 = requestLeadCount1 + scheduleLeadCount1 + tradeInLeadCount1;
     	int countLeads = requestLeadCount + scheduleLeadCount + tradeInLeadCount;
    	int countLostLeads = lostRequestLeadCount + lostScheduleLeadCount + lostTradeInLeadCount;
     	
    	
    	LocationWiseDataVM lDataVM = new LocationWiseDataVM();
     	
     	
     	if(users.imageUrl != null) {
			if(users.imageName !=null){
				lDataVM.imageUrl = "http://glider-autos.com/glivrImg/images"+users.imageUrl;
			}else{
				lDataVM.imageUrl = users.imageUrl;
			}
			
		} else {
			lDataVM.imageUrl = "/profile-pic.jpg";
		}
     	
     	if(countFollo != 0 && countLeads != 0){
     		Long followUpTime = countFollo / countLeads;
	     	
	     	Long seconds = (followUpTime / 1000);
	     	Long minutes = (followUpTime / (1000 * 60));
	     	Long hours = (minutes / 60);
	     	Long displayMin = minutes - (hours * 60);
	     	
	     
	     	lDataVM.followUpTime =hours.toString()+":"+displayMin.toString()+" Hrs";
     		 /*Long followUpTime= (long)((double)countFollo / (double) countLeads) * 24;
     		lDataVM.followUpTime=followUpTime.toString()+":00 Hrs";*/
     	}else{
     		lDataVM.followUpTime = "00:00 Hrs";
     	}
     	
     	lDataVM.countSalePerson = countLeads;
     	lDataVM.lostLeadCount = countLostLeads;
     	lDataVM.id = users.id;
     	Integer monthPriceCount = 0;
     	Integer pricecount = 0;
     	int saleCarCount = 0;
     	
     	Map<String, Integer> priceRang = new HashMap<String, Integer>();
     	
     	addPriceRang(priceRang);
     	
     	//List<Vehicle> totalVehicleVM = Vehicle.findByLocation(Long.valueOf(session("USER_LOCATION")));
     	List<Vehicle> totalVehicleVM = Vehicle.getAllVehicles();
     	for(Vehicle vm:totalVehicleVM ){
     		Integer objectMake = mapByType.get(vm.getBodyStyle());
			if (objectMake == null) {
				mapByType.put(vm.getBodyStyle(), 0);
			}
          }
     	
     	
     	lDataVM.SalePersonName = users.firstName +" "+users.lastName; 
     		
     		List<RequestMoreInfo> rInfo1 = RequestMoreInfo.findAllSeenComplete(users);
     		List<ScheduleTest> sList1 = ScheduleTest.findAllSeenComplete(users);
     		List<TradeIn> tradeIns1 = TradeIn.findAllSeenComplete(users);
     		
     		Integer countBodyStyle = 1;
     		
     		List<Vehicle> vList = Vehicle.findBySoldUserAndSold(users);
    		for (Vehicle vehicle : vList) {
    			if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(startD) || vehicle.soldDate.equals(endD)) {
        			saleCarCount++;
        			pricecount = pricecount + vehicle.price;
        			fillPriceRang(priceRang, vehicle.price);
        			
        			if(vehicle.getBodyStyle() != null){
     					
     					Integer objectMake = mapByType.get(vehicle.getBodyStyle());
     					if (objectMake == null) {
     						mapByType.put(vehicle.getBodyStyle(), countBodyStyle);
     					}else{
     						mapByType.put(vehicle.getBodyStyle(), mapByType.get(vehicle.getBodyStyle()) + 1);
     					}
     				}
				}
				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
					monthPriceCount = monthPriceCount + vehicle.price;
				}
			}
     		
     	/*	for(RequestMoreInfo rMoreInfo: rInfo1){
     			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(rMoreInfo.vin,users);
     			for(Vehicle vehicle:vehicleVin){
     			if(vehicle != null){
     				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
             			saleCarCount++;
             			pricecount = pricecount + vehicle.price;
             			fillPriceRang(priceRang, vehicle.price);
             			
             				if(vehicle.getBodyStyle() != null){
             					
             					Integer objectMake = mapByType.get(vehicle.getBodyStyle());
             					if (objectMake == null) {
             						mapByType.put(vehicle.getBodyStyle(), countBodyStyle);
             					}else{
             						mapByType.put(vehicle.getBodyStyle(), countBodyStyle + 1);
             					}
             				}
             			
             			
     				}
     				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
             			monthPriceCount = monthPriceCount + vehicle.price;
             		}
     			}
     		 }
     		}
     		
     		for(ScheduleTest sTest: sList1){
     			List<Vehicle> vehicleVin = Vehicle.findByVidAndUserWise(sTest.vin,users);
     			for(Vehicle vehicle:vehicleVin){
     			if(vehicle != null){
 				if(vehicle.status.equals("Sold")){
     				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
             			saleCarCount++;
             			pricecount = pricecount + vehicle.price;
             			fillPriceRang(priceRang, vehicle.price);
             			if(vehicle.getBodyStyle() != null){
         					
         					Integer objectMake = mapByType.get(vehicle.getBodyStyle());
         					if (objectMake == null) {
         						mapByType.put(vehicle.getBodyStyle(), countBodyStyle);
         					}else{
         						mapByType.put(vehicle.getBodyStyle(), countBodyStyle + 1);
         					}
         				}
     				}
     				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
             			monthPriceCount = monthPriceCount + vehicle.price;
             		}
 			 }	
     			}
     		}
     		}
     		
     		for(TradeIn tradeIn: tradeIns1){
     			List<Vehicle> vehicleVin = Vehicle.findByVidAndUser(tradeIn.vin);
     			for(Vehicle vehicle:vehicleVin){
     			if(vehicle != null){
 				if(vehicle.status.equals("Sold")){
     				if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD)){
             			saleCarCount++;
             			pricecount = pricecount + vehicle.price;
             			fillPriceRang(priceRang, vehicle.price);
             			if(vehicle.getBodyStyle() != null){
         					
         					Integer objectMake = mapByType.get(vehicle.getBodyStyle());
         					if (objectMake == null) {
         						mapByType.put(vehicle.getBodyStyle(), countBodyStyle);
         					}else{
         						mapByType.put(vehicle.getBodyStyle(), countBodyStyle + 1);
         					}
         				}
     				}
     				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
             			monthPriceCount = monthPriceCount + vehicle.price;
             		}
 				}
     			}
     		}
     		}*/
     		
     		
    		double sucessCount = 0;
    		if(countLeads1 != 0){
    			sucessCount= (double)saleCarCount/(double)countLeads1*100;
    		}else{
    			 sucessCount = 0;
    		}
     		lDataVM.successRate = (int) sucessCount;
     		
     	
     	lDataVM.totalSalePrice =  pricecount;
     	lDataVM.totalsaleCar = saleCarCount;
     	
     	double valAvlPrice= ((double)pricecount/(double)saleCarCount);
     	lDataVM.angSalePrice = (int) valAvlPrice;
     	
     	List<bodyStyleSetVM> bSetVMs = new ArrayList<>();
     	
     	for (Entry<String , Integer> entryValue : mapByType.entrySet()) {
     		bodyStyleSetVM value = new bodyStyleSetVM();
			value.name = entryValue.getKey();
			value.value = entryValue.getValue();
			bSetVMs.add(value);
		}
     	lDataVM.byType = bSetVMs;
     	
     	
     	List<bodyStyleSetVM> bSetVMPriceRang = new ArrayList<>();
     	
     	for (Entry<String , Integer> entryValue : priceRang.entrySet()) {
     		bodyStyleSetVM value = new bodyStyleSetVM();
			value.name = entryValue.getKey();
			value.value = entryValue.getValue();
			bSetVMPriceRang.add(value);
		}
     	lDataVM.priceRang = bSetVMPriceRang;
     	
     	List<bodyStyleSetVM> bSetVMsPlan = new ArrayList<>();
      	List<PlanScheduleMonthlySalepeople> pMonthlySalepeople = PlanScheduleMonthlySalepeople.findByListUser(users);
      	System.out.println(":::::::plancomplete");
      	System.out.println(pMonthlySalepeople);
      	
      	 String[] changVls = { "January", "February", "March", "April", "May", "June", "July",
  		        "August", "September", "October", "November", "December" };
     	for(int i=0;i<12;i++){
     		bodyStyleSetVM nSetVM1 = new bodyStyleSetVM();
     		nSetVM1.name = changVls[i];
     		nSetVM1.value=0;
     		bSetVMsPlan.add(nSetVM1);
     		
     	}
      	 
      	
        for(PlanScheduleMonthlySalepeople pSalepeople: pMonthlySalepeople){
        	//bodyStyleSetVM nSetVM = new bodyStyleSetVM();
        	Date nowDate = new Date();
        	for(bodyStyleSetVM nSetVM:bSetVMsPlan){
        		
        	
        	Calendar calnow = Calendar.getInstance();
        	calnow.setTime(nowDate);
           
            int month = calnow.get(Calendar.MONTH);
            String changVls1 = WordUtils.capitalize(pSalepeople.month);
        	//for(int i=0;i<12;i++){
        		
        		//System.out.println("::::::monthname"+nSetVM.name);
        		
        		if(changVls1.equals(nSetVM.name)){
        		//	if(i <= month){
        				nSetVM.name = changVls1;
        				System.out.println("after if condition"+nSetVM.name);
        	        	double val= ((double)pricecount/Double.parseDouble(pSalepeople.totalBrought));
        	        	nSetVM.value = (int) (val*100);
        	        	
        	        	//bSetVMsPlan.add(nSetVM);
        			//}
        		//}
        	}
        		
        }
       }  
        lDataVM.planComplete = bSetVMsPlan;
        
        List<RequestMoreInfo> offlineRInfo = RequestMoreInfo.findAllAssignedOffine(users);
        List<ScheduleTest> offlineSList = ScheduleTest.findAllAssignedOffine(users);
        List<TradeIn> offlineTradeIns = TradeIn.findAllAssignedOffine(users);
        
        Map<String, Integer> mapOffline = new HashMap<String, Integer>();
        
    	for(RequestMoreInfo rMoreInfo:offlineRInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
     			if(rMoreInfo.contactedFrom != null){
     				Integer objectMake = mapOffline.get(rMoreInfo.contactedFrom);
        			if (objectMake == null) {
        				mapOffline.put(rMoreInfo.contactedFrom, countBodyStyle);
        			}else{
        				mapOffline.put(rMoreInfo.contactedFrom, countBodyStyle + 1);
        			}
     			}
     			
     		}
     	}
     	
     	for(ScheduleTest sTest:offlineSList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
    			if(sTest.contactedFrom != null){
    				Integer objectMake = mapOffline.get(sTest.contactedFrom);
    				if (objectMake == null) {
        				mapOffline.put(sTest.contactedFrom, countBodyStyle);
        			}else{
        				mapOffline.put(sTest.contactedFrom, countBodyStyle + 1);
        			}
    			}
     		}
     	}

     	for(TradeIn tIn:offlineTradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
     			if(tIn.contactedFrom != null){
     				Integer objectMake = mapOffline.get(tIn.contactedFrom);
        			if (objectMake == null) {
        				mapOffline.put(tIn.contactedFrom, countBodyStyle);
        			}else{
        				mapOffline.put(tIn.contactedFrom, countBodyStyle + 1);
        			}
     			}
     		}
     	}
     	List<bodyStyleSetVM> bSetVMsoffline = new ArrayList<>();
     	for (Entry<String , Integer> entryValue : mapOffline.entrySet()) {
     		bodyStyleSetVM value = new bodyStyleSetVM();
			value.name = entryValue.getKey();
			value.value = entryValue.getValue();
			bSetVMsoffline.add(value);
		}
     	lDataVM.offlineLead = bSetVMsoffline;
     	
     	
     	List<RequestMoreInfo> onlineRInfo = RequestMoreInfo.findAllAssignedOnline(users);
        List<ScheduleTest> onlineSList = ScheduleTest.findAllAssignedOnline(users);
        List<TradeIn> onlineTradeIns = TradeIn.findAllAssignedOnline(users);
        
        Map<String, Integer> mapOnline = new HashMap<String, Integer>();
        
        mapOnline.put("Request More Info", 0);
    	for(RequestMoreInfo rMoreInfo:onlineRInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
     			Integer objectMake = mapOnline.get("Request More Info");
    			if (objectMake != null) {
    				
    				mapOnline.put("Request More Info", countBodyStyle + 1);
    			}
     		}
     	}
     	
    	mapOnline.put("Schedule Test Drive", 0);
     	for(ScheduleTest sTest:onlineSList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
     			Integer objectMake = mapOnline.get("Schedule Test Drive");
    			if (objectMake != null) {
    				mapOnline.put("Schedule Test Drive", countBodyStyle + 1);
    			}
     		}
     	}

     	mapOnline.put("Trade-In Inquires", 0);
     	for(TradeIn tIn:onlineTradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
     			Integer objectMake = mapOnline.get("Trade-In Inquires");
    			if (objectMake != null) {
    				mapOnline.put("Trade-In Inquires", countBodyStyle + 1);
    			}
     		}
     	}
     	List<bodyStyleSetVM> bSetVMsonline = new ArrayList<>();
     	for (Entry<String , Integer> entryValue : mapOnline.entrySet()) {
     		bodyStyleSetVM value = new bodyStyleSetVM();
			value.name = entryValue.getKey();
			value.value = entryValue.getValue();
			bSetVMsonline.add(value);
		}
     	lDataVM.onLineLead = bSetVMsonline;
     	
     	int requestGLeadCount = 0;
     	int scheduleGLeadCount = 0;
     	int tradeInGLeadCount = 0;
     	
     	List<RequestMoreInfo> rInfoAllG = RequestMoreInfo.findAllByAssignedUser(users);
     	List<ScheduleTest>  sListAllG = ScheduleTest.findAllByAssignedUser(users);
     	List<TradeIn> tradeInsAllG = TradeIn.findAllByAssignedUser(users);
 		
     	Long totalLeadDay = 0L;
 		
 	for(RequestMoreInfo rMoreInfo:rInfoAllG){
 		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD)){
 			
 			 			
 			Long diff = 0L;
				if(rMoreInfo.statusTime != null){
					diff = rMoreInfo.statusTime.getTime() - rMoreInfo.requestTime.getTime();
					
					//diff = (diff / 1000 /60 /60 /24);
				}else{
					Date nowDate  =new Date();
 					/*String datef = df.format(nowDate);
 					Date  ndate = null;
 					
 					try {
						ndate = df.parse(datef);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
 					
 					diff = nowDate.getTime() - rMoreInfo.requestTime.getTime();
 					//diff = (diff / 1000 /60 /60 /24);
				}
				
				totalLeadDay = totalLeadDay + diff;
				
		
 			requestGLeadCount++;
 		}
 	}
 	
 	for(ScheduleTest sTest:sListAllG){
 		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD)){
 			
 			Long diff = 0L;
			if(sTest.statusTime != null){
				diff = sTest.statusTime.getTime() - sTest.scheduleTime.getTime();
				
			//	diff = (diff / 1000 /60 /60 /24);
			}else{
				Date nowDate  =new Date();
					/*String datef = df.format(nowDate);
					Date  ndate = null;
					
					try {
					ndate = df.parse(datef);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
					
					diff = nowDate.getTime() - sTest.scheduleTime.getTime();
					//diff = (diff / 1000 /60 /60 /24);
			}
			
			totalLeadDay = totalLeadDay + diff;
			
 			scheduleGLeadCount++;
 		}
 	}

 	for(TradeIn tIn:tradeInsAllG){
 		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD)){
 			
 			Long diff = 0L;
			if(tIn.statusTime != null){
				diff = tIn.statusTime.getTime() - tIn.tradeTime.getTime();
				
			//	diff = (diff / 1000 /60 /60 /24);
			}else{
				Date nowDate  =new Date();
					/*String datef = df.format(nowDate);
					Date  ndate = null;
					
					try {
					ndate = df.parse(datef);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
					
					diff = nowDate.getTime() - tIn.tradeTime.getTime();
				//	diff = (diff / 1000 /60 /60 /24);
			}
			totalLeadDay = totalLeadDay + diff;
 				tradeInGLeadCount++;
 		}
 	}
 	
 	int AllGeneratedLead = requestGLeadCount + scheduleGLeadCount + tradeInGLeadCount;
     	lDataVM.allGeneratedLeadCount = AllGeneratedLead;
     	
     	
     	if(totalLeadDay  != 0 || AllGeneratedLead != 0){
	     	Long AvgLeadLifeCyc = totalLeadDay / AllGeneratedLead;
	     	
	     	Long seconds = (AvgLeadLifeCyc / 1000);
	     	Long minutes = (AvgLeadLifeCyc / (1000 * 60));
	     	Long hours = (minutes / 60);
	     	Long displayMin = minutes - (hours * 60);
	     	
	     
	     	lDataVM.avgLeadLifeCycle =hours.toString()+":"+displayMin.toString()+" Hrs";
     	}else{
     		lDataVM.avgLeadLifeCycle = "00:00Hrs";
     	}
     	
     	List<UserNotes> uNotes = UserNotes.findByUserAndcall(users);
     	int callActionCount =  0;
     	for(UserNotes sNot:uNotes){
     		if((sNot.createdDate.after(startD) && sNot.createdDate.before(endD)) || sNot.createdDate.equals(endD)){
     			callActionCount++;
     		}
     	}
     	lDataVM.callMade = callActionCount;
     	
     	List<UserNotes> uNotesEmail = UserNotes.findByUserAndemail(users);
     	int mailActionCount =  0;
     	for(UserNotes sNot:uNotesEmail){
     		if((sNot.createdDate.after(startD) && sNot.createdDate.before(endD)) || sNot.createdDate.equals(endD)){
     			mailActionCount++;
     		}
     	}
     	lDataVM.mailSent = callActionCount;
     	
     	List<UserNotes> uNotesTest = UserNotes.findByUserAndSched(users);
     	int testDriveActionCount =  0;
     	for(UserNotes sNot:uNotesTest){
     		if((sNot.createdDate.after(startD) && sNot.createdDate.before(endD)) || sNot.createdDate.equals(endD)){
     			testDriveActionCount++;
     		}
     	}
     	lDataVM.testDriveSched = callActionCount;
     	
     	
     	List<Comments> comments = Comments.getByListUser(users);
     	int likeCount = 0;
     	
     	for(Comments comm:comments){
     		if((comm.likeDate.after(startD) && comm.likeDate.before(endD)) || comm.likeDate.equals(endD)){
     			likeCount++;
     		}
     	}
     	
     	lDataVM.likeCount = likeCount;
     	
     	
     	
     	Map<String, Integer> returnIng = new HashMap<String, Integer>();
      List<Contacts> cList = Contacts.findByUser(users.id);
      
      for(Contacts contacts:cList){
    	  Integer objectMake = returnIng.get(contacts.email);
  		if (objectMake == null) {
  			returnIng.put(contacts.email, countBodyStyle);
  		}else{
  			returnIng.put(contacts.email, countBodyStyle + 1);
  		}
      }
      
      int returningCount = 0;
 	for (Entry<String , Integer> entryValue : returnIng.entrySet()) {
 		if(entryValue.getValue() >= 2){
 			returningCount++;
 		}
	}
 	lDataVM.returningClints = returningCount;
 	
 	int parDataSalary = Integer.parseInt(users.salary) / 30;
 	int dayCount = 1;
 	int value1 = 2;
 	Date dts = startD;
 	while(value1 > 1){
				Calendar c = Calendar.getInstance(); 
				c.setTime(dts); 
				c.add(Calendar.DATE, 1);
				dts = c.getTime();
				dayCount++;
				String nextDate = df.format(dts);
				Date  dfnext = null;
				try {
				dfnext = df.parse(nextDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
				
				if(dfnext.equals(endD)){
					value1 = 0;
					break;
				}
 	}
      
 	lDataVM.salary = (parDataSalary * dayCount);
 	lDataVM.training = users.trainingPro;
 	int tcl = 0;
 	if(users.trainingCost != null){
 		if(!users.trainingCost.equals("null")){
 			lDataVM.trainingCost = Integer.parseInt(users.trainingCost);
 			tcl = Integer.parseInt(users.trainingCost) / lDataVM.allGeneratedLeadCount;
 		}
 	}else{
 		
 		lDataVM.trainingCost = 0;
 	}
 	lDataVM.comission = (lDataVM.totalSalePrice * Integer.parseInt(users.commission)) / 100; 
 	if(lDataVM.allGeneratedLeadCount != 0){
 		int sl = lDataVM.salary / lDataVM.allGeneratedLeadCount;
 	 	int cl = lDataVM.comission / lDataVM.allGeneratedLeadCount;
 	 	lDataVM.leadCost = sl + tcl + cl;
 	}

     	return ok(Json.toJson(lDataVM));
    }
    
    public static void addPriceRang(Map<String, Integer> priceRang){
    	 priceRang.put("0-$10,000", 0);
 		priceRang.put("$10,000-$20,000", 0);
 		priceRang.put("$20,000-$30,000", 0);
 		priceRang.put("$30,000-$40,000", 0);
 		priceRang.put("$40,000-$50,000", 0);
 		priceRang.put("$50,000-$60,000", 0);
 		priceRang.put("$60,000-$70,000", 0);
 		priceRang.put("$70,000-$80,000", 0);
 		priceRang.put("$80,000-$90,000", 0);
 		priceRang.put("$90,000-$1,00,000", 0);
 		priceRang.put("$1,00,000 +", 0);
 		
    }

    public static void fillPriceRang(Map<String, Integer> priceRang, Integer price){
    	 	int count = 1;	
 		if(price >= 0 && price < 10000){
 			priceRang.put("0-$10,000", priceRang.get("0-$10,000") + 1);
 		}else if(price >= 10000 && price < 20000){
 			priceRang.put("$10,000-$20,000", priceRang.get("$10,000-$20,000") + 1);
 		}else if(price >= 20000 && price < 30000){
 			priceRang.put("$20,000-$30,000", priceRang.get("$20,000-$30,000") + 1);
 		}else if(price >= 30000 && price < 40000){
 			priceRang.put("$30,000-$40,000", priceRang.get("$30,000-$40,000") + 1);
 		}else if(price >= 40000 && price < 50000){
 			priceRang.put("$40,000-$50,000", priceRang.get("$40,000-$50,000") + 1);
 		}else if(price >= 50000 && price < 60000){
 			priceRang.put("$50,000-$60,000", priceRang.get("$50,000-$60,000") + 1);
 		}else if(price >= 60000 && price < 70000){
 			priceRang.put("$60,000-$70,000", priceRang.get("$60,000-$70,000") + 1);
 		}else if(price >= 70000 && price < 80000){
 			priceRang.put("$70,000-$80,000", priceRang.get("$70,000-$80,000") + 1);
 		}else if(price >= 80000 && price < 90000){
 			priceRang.put("$80,000-$90,000", priceRang.get("$80,000-$90,000") + 1);
 		}else if(price >= 90000 && price < 100000){
 			priceRang.put("$90,000-$1,00,000", priceRang.get("$90,000-$1,00,000") + 1);
 		}else if(price >= 100000){
 			priceRang.put("$1,00,000 +", priceRang.get("$1,00,000 +") + 1);
 		}
 		
    }
    
    public static Result getHeardAboutUs(){
    	List<HeardAboutUsVm> vmList = new ArrayList<>();
    	List<HeardAboutUs> list = HeardAboutUs.getAll();
    	for (HeardAboutUs info : list) {
			HeardAboutUsVm vm = new HeardAboutUsVm();
			vm.id = info.id;
			vm.value = info.value;
			vmList.add(vm);
		}
    	return ok(Json.toJson(vmList));
    }
    
    public static Result addHeard(String name){
    	List<HeardAboutUs> nameList = HeardAboutUs.getByValue(name);
    	if(nameList.size() == 0){
    		HeardAboutUs obj = new HeardAboutUs();
    		obj.value = name;
    		obj.save();
    	}
    	return ok();
    }
    
    public static Result createLead() {
    	AuthUser user = (AuthUser)getLocalUser();
    	SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
    	LeadVM leadVM = DynamicForm.form(LeadVM.class).bindFromRequest().get();
    	String makestr = leadVM.make!=null&&!leadVM.make.isEmpty()?leadVM.make:leadVM.makeSelect;
    	String model = leadVM.model!=null&&!leadVM.model.isEmpty()?leadVM.model:leadVM.modelSelect;
    	Date date = new Date();
    	int parentFlag = 0;
    	long parentLeadId = 0L;
    	
    	List<Vehicle> vehicles = Vehicle.findByMakeAndModel(makestr, model);
    	if(leadVM.leadType.equals("1")) {
    		for(VehicleVM vehicleVM:leadVM.stockWiseData){
	    		RequestMoreInfo info = new RequestMoreInfo();
	    		info.setIsReassigned(true);
	    		info.setLeadStatus(null);
	    		info.setEmail(leadVM.custEmail);
	    		info.setName(leadVM.custName);
	    		info.setPhone(leadVM.custNumber);
	    		info.setCustZipCode(leadVM.custZipCode);
	    		info.setEnthicity(leadVM.enthicity);
	    		info.setAssignedTo(user);
	    		Vehicle vehicle = Vehicle.findByStockAndNew(vehicleVM.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    		info.setVin(vehicle.getVin());
	    		info.setUser(user);
				info.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		info.setIsScheduled(false);
	    		info.setIsRead(1);
	    		info.setHearedFrom(leadVM.hearedFrom);
	    		info.setContactedFrom(leadVM.contactedFrom);
	    		info.setPremiumFlag(0);
	    		info.setOnlineOrOfflineLeads(0);
	    		info.setRequestDate(new Date());
	    		info.setRequestTime(new Date());
	    		/*	PremiumLeads pLeads = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    		if(pLeads != null){
	    				if(Integer.parseInt(pLeads.premium_amount) <= vehicle.price){
	    					info.setPremiumFlag(1);
	    				}else{
	    					info.setPremiumFlag(0);
	    					if(pLeads.premium_flag == 0){
	    						info.setAssignedTo(user);
	    					}
	    				}
	    				if(pLeads.premium_flag == 1){
	    					AuthUser aUser = AuthUser.getlocationAndManagerOne(Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    					info.setAssignedTo(aUser);
	    				}
	    			
	    		}else{
					info.setPremiumFlag(0);
						info.setAssignedTo(user);
				}*/
	    		
	    		if(parentFlag == 1){
	    			info.setParentId(parentLeadId);
	    		}
	    		
	    		info.save();
	    		
	    		if(parentFlag == 0){
	    			parentFlag = 1;
	    			parentLeadId = info.getId();
	    		}
	    		
	    		UserNotes uNotes = new UserNotes();
	    		uNotes.setNote("Lead has been created");
	    		uNotes.setAction("Other");
	    		uNotes.createdDate = date;
	    		uNotes.createdTime = date;
	    		uNotes.saveHistory = 1;
	    		uNotes.user = user;
	    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
	    		uNotes.save();
	    		
	    		/*if(info.premiumFlag == 1){
	    			sendMailpremium();
	    		}*/
	    		
    	 }
    	} else if(leadVM.leadType.equals("2")){
    		Date confirmDate = null;
    		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    	
    		
    		for(VehicleVM vehicleVM:leadVM.stockWiseData){
	    		ScheduleTest test = new ScheduleTest();
	    		
	    		test.setIsReassigned(true);
	    		test.setLeadStatus(null);
	    		test.setBestDay(leadVM.bestDay);
	    		test.setBestTime(leadVM.bestTime);
	    		
	    		//if(parentFlag == 0){
	    			try {
		    			confirmDate = df.parse(leadVM.bestDay);
		    			test.setConfirmDate(confirmDate);
		    		} catch(Exception e) {}
		    		try {
		    			test.setConfirmTime(parseTime.parse(leadVM.bestTime));
		    		} catch(Exception e) {}
	    		//}
	    		
	    		test.setEmail(leadVM.custEmail);
	    		test.setName(leadVM.custName);
	    		test.setPhone(leadVM.custNumber);
	    		test.setCustZipCode(leadVM.custZipCode);
	    		test.setEnthicity(leadVM.enthicity);
	    		test.setIsRead(1);
	    		test.setUser(user);
				test.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		test.setHearedFrom(leadVM.hearedFrom);
	    		test.setContactedFrom(leadVM.contactedFrom);
	    		test.setScheduleDate(new Date());
	    		test.setScheduleTime(new Date());
	    		test.setPreferredContact(leadVM.prefferedContact);
	    		Vehicle vehicle = Vehicle.findByStockAndNew(vehicleVM.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
	    		test.setVin(vehicle.getVin());
	    		test.setAssignedTo(user);
	    		test.setPremiumFlag(0);
	    		test.setOnlineOrOfflineLeads(0);
	    		//test.setVin(vehicles.get(0).getVin());
	    		
	    	/*	PremiumLeads pLeads = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    		if(pLeads != null){
    				if(Integer.parseInt(pLeads.premium_amount) <= vehicle.price){
    					test.setPremiumFlag(1);
    				}else{
    					test.setPremiumFlag(0);
    					if(pLeads.premium_flag == 0){
    						test.setAssignedTo(user);
    					}
    				}
    				if(pLeads.premium_flag == 1){
    					AuthUser aUser = AuthUser.getlocationAndManagerOne(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    					test.setAssignedTo(aUser);
    				}
    			
	    		}else{
	    			test.setPremiumFlag(0);
	    			test.setAssignedTo(user);
	    		}*/
	    		
	    		if(parentFlag == 1){
	    			test.setParentId(parentLeadId);
	    		}
	    		
	    		test.save();
	    		
	    		
	    		UserNotes uNotes = new UserNotes();
	    		uNotes.setNote("Lead has been created");
	    		uNotes.setAction("Other");
	    		uNotes.createdDate = date;
	    		uNotes.createdTime = date;
	    		uNotes.user = user;
	    		uNotes.saveHistory = 1;
	    		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		uNotes.scheduleTest = ScheduleTest.findById(test.id);
	    		uNotes.save();
	    		
	    		Map map = new HashMap();
	    		map.put("email",leadVM.custEmail);
	    		map.put("confirmDate", confirmDate);
	    		map.put("confirmTime",leadVM.bestTime);
	    		map.put("vin", vehicle.getVin());
	    		map.put("uname", user.firstName+" "+user.lastName);
	    		map.put("uphone", user.phone);
	    		map.put("uemail", user.email);
	    		
	    		makeToDo(vehicle.vin);
	    		

	    		if(parentFlag == 0){
	    			sendMail(map);
	    			parentFlag = 1;
	    			parentLeadId = test.getId();
	    		}
    		   
    		   
    		   /*if(test.premiumFlag == 1){
   					sendMailpremium();
   				}*/
    	}
    	} else {
    		TradeIn tIn = null;
    		if(leadVM.id != null){
    			tIn = TradeIn.findById(Long.parseLong(leadVM.id));
    		}
    		
    		if(tIn == null){
    			
    		StringBuffer buffer = new StringBuffer();
    		for(String opt:leadVM.options) {
    			buffer.append(opt+",");
    		}
    		if(buffer.length()>0) {
    			buffer.deleteCharAt(buffer.length()-1);
    		}
    		
    		for(VehicleVM vehicleVM:leadVM.stockWiseData){
    			
    			TradeIn tradeIn = new TradeIn();
        		tradeIn.setAccidents(leadVM.accidents);
        		tradeIn.setEnthicity(leadVM.enthicity);
        		
        		tradeIn.setIsReassigned(true);
        		tradeIn.setLeadStatus(null);
        		tradeIn.setBodyRating(leadVM.bodyRating);
        		tradeIn.setComments(leadVM.comments);
        		tradeIn.setContactedFrom(leadVM.contactedFrom);
        		tradeIn.setDamage(leadVM.damage);
        		tradeIn.setDoors(leadVM.doors);
        		tradeIn.setDrivetrain(leadVM.drivetrain);
        		tradeIn.setEmail(leadVM.custEmail);
        		tradeIn.setCustZipCode(leadVM.custZipCode);
        		tradeIn.setEngine(leadVM.engine);
        		tradeIn.setEngineRating(leadVM.engineRating);
        		tradeIn.setEquipment(leadVM.equipment);
        		tradeIn.setExhaustRating(leadVM.exhaustRating);
        		tradeIn.setExteriorColour(leadVM.exteriorColour);
        		tradeIn.setFirstName(leadVM.custName);
        		tradeIn.setGlassRating(leadVM.glassRating);
        		tradeIn.setHearedFrom(leadVM.hearedFrom);
        		tradeIn.setInteriorRating(leadVM.interiorRating);
        		tradeIn.setIsRead(1);
        		tradeIn.setKilometres(leadVM.kilometres);
        		tradeIn.setLeaseOrRental(leadVM.rentalReturn);
        		tradeIn.setLienholder(leadVM.lienholder);
        		tradeIn.setMake(makestr);
        		tradeIn.setModel(model);
        		tradeIn.setOperationalAndAccurate(leadVM.odometerAccurate);
        		tradeIn.setOptionValue(buffer.toString());
        		tradeIn.setPaint(leadVM.paint);
        		tradeIn.setPhone(leadVM.custNumber);
        		tradeIn.setPreferredContact(leadVM.prefferedContact);
        		tradeIn.setSalvage(leadVM.salvage);
        		tradeIn.setScheduleDate(new Date());
        		tradeIn.setTireRating(leadVM.tireRating);
        		tradeIn.setTradeDate(new Date());
        		tradeIn.setTradeTime(new Date());
        		tradeIn.setTransmission(leadVM.transmission);
        		tradeIn.setUser(user);
    			tradeIn.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		tradeIn.setVehiclenew(leadVM.vehiclenew);
        		Vehicle vehicle = Vehicle.findByStockAndNew(vehicleVM.stockNumber,Location.findById(Long.valueOf(session("USER_LOCATION"))));
        		tradeIn.setVin(vehicle.getVin());
        		tradeIn.setAssignedTo(user);
        		tradeIn.setPremiumFlag(0);
        		tradeIn.setOnlineOrOfflineLeads(0);
        		//tradeIn.setVin(vehicles.get(0).getVin());
        		tradeIn.setYear(leadVM.year);
        		
        	/*	PremiumLeads pLeads = PremiumLeads.findByLocation(Long.valueOf(session("USER_LOCATION")));
        		if(pLeads != null){
    				if(Integer.parseInt(pLeads.premium_amount) <= vehicle.price){
    					tradeIn.setPremiumFlag(1);
    				}else{
    					tradeIn.setPremiumFlag(0);
    					if(pLeads.premium_flag == 0){
    						tradeIn.setAssignedTo(user);
    					}
    				}
    				if(pLeads.premium_flag == 1){
    					AuthUser aUser = AuthUser.getlocationAndManagerOne(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    					tradeIn.setAssignedTo(aUser);
    				}
    			
	    		}else{
	    			tradeIn.setPremiumFlag(0);
	    			tradeIn.setAssignedTo(user);
	    		}*/
        		
        		if(parentFlag == 1){
        			tradeIn.setParentId(parentLeadId);
	    		}
	    		
        		tradeIn.save();
	    		
	    		if(parentFlag == 0){
	    			parentFlag = 1;
	    			parentLeadId = tradeIn.getId();
	    		}
        		
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Lead has been created");
        		uNotes.setAction("Other");
        		uNotes.createdDate = date;
        		uNotes.createdTime = date;
        		uNotes.saveHistory = 1;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.tradeIn = tradeIn.findById(tradeIn.id);
        		uNotes.save();
        		
        		/*if(tradeIn.premiumFlag == 1){
	    			sendMailpremium();
	    		}*/
    		
    		
    		VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.getVin());
    		
    		AuthUser defaultUser = getLocalUser();
    		//AuthUser defaultUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
    		SiteLogo siteLogo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
    		SiteContent siteContent = SiteContent.findByLocation(Long.valueOf(session("USER_LOCATION")));
    		String heading1 = "",heading2 = "";
    		if(siteContent.getHeading()!=null) {
    		    int index= siteContent.getHeading().lastIndexOf(" ");
    		    heading1 = siteContent.getHeading().substring(0, index);
    		    heading2 = siteContent.getHeading().substring(index+1);
    		}
    		String filepath = null,findpath = null;

			try {
				Document document = new Document();
				createDir(pdfRootDir, Long.parseLong(session("USER_LOCATION")), tradeIn.getId());
				filepath = pdfRootDir + File.separator + Long.parseLong(session("USER_LOCATION"))
						+ File.separator + "trade_in_pdf" + File.separator
						+ tradeIn.getId() + File.separator + "Trade_In.pdf";
				findpath = File.separator + Long.parseLong(session("USER_LOCATION")) + File.separator + "trade_in_pdf" + File.separator
						+ tradeIn.getId() + File.separator + "Trade_In.pdf";
				// UPDATE table_name
				// SET column1=value1,column2=value2,...
				// WHERE some_column=some_value;
				TradeIn tIn2 = TradeIn.findById(tradeIn.getId());
				tIn2.setPdfPath(findpath);
				tIn2.update();
				PdfWriter pdfWriter = PdfWriter.getInstance(document,
						new FileOutputStream(filepath));

				// Properties
				document.addAuthor("Celinio");
				document.addCreator("Celinio");
				document.addSubject("iText with Maven");
				document.addTitle("Trade-In Appraisal");
				document.addKeywords("iText, Maven, Java");

				document.open();

				/* Chunk chunk = new Chunk("Fourth tutorial"); */
				Font font = new Font();
				font.setStyle(Font.UNDERLINE);
				font.setStyle(Font.ITALIC);
				/* chunk.setFont(font); */
				// chunk.setBackground(Color.BLACK);
				/* document.add(chunk); */

				Font font1 = new Font(FontFamily.HELVETICA, 8, Font.NORMAL,
						BaseColor.BLACK);
				Font font2 = new Font(FontFamily.HELVETICA, 8, Font.BOLD,
						BaseColor.BLACK);

				PdfPTable Titlemain = new PdfPTable(1);
				Titlemain.setWidthPercentage(100);
				float[] TitlemainWidth = { 2f };
				Titlemain.setWidths(TitlemainWidth);

				PdfPCell title = new PdfPCell(new Phrase("Trade-IN Appraisal"));
				title.setBorderColor(BaseColor.WHITE);
				title.setBackgroundColor(new BaseColor(255, 255, 255));
				Titlemain.addCell(title);

				PdfPTable contactInfo = new PdfPTable(4);
				contactInfo.setWidthPercentage(100);
				float[] contactInfoWidth = { 2f, 2f, 2f, 2f };
				contactInfo.setWidths(contactInfoWidth);

				PdfPCell firstname = new PdfPCell(new Phrase("First Name:",
						font1));
				firstname.setBorderColor(BaseColor.WHITE);
				firstname.setBackgroundColor(new BaseColor(255, 255, 255));
				contactInfo.addCell(firstname);

				PdfPCell firstnameValue = new PdfPCell(new Paragraph(
						tradeIn.getFirstName(), font2));
				firstnameValue.setBorderColor(BaseColor.WHITE);
				firstnameValue.setBorderWidth(1f);
				contactInfo.addCell(firstnameValue);

				PdfPCell lastname = new PdfPCell(
						new Phrase("Last Name:", font1));
				lastname.setBorderColor(BaseColor.WHITE);
				contactInfo.addCell(lastname);

				PdfPCell lastnameValue = new PdfPCell(new Paragraph("", font2));
				lastnameValue.setBorderColor(BaseColor.WHITE);
				lastnameValue.setBorderWidth(1f);
				// lastnameValue.setHorizontalAlignment(Element.ALIGN_LEFT);
				contactInfo.addCell(lastnameValue);

				PdfPCell workPhone = new PdfPCell(new Phrase("Work Phone:",
						font1));
				workPhone.setBorderColor(BaseColor.WHITE);
				contactInfo.addCell(workPhone);

				PdfPCell workPhoneValue = new PdfPCell(new Paragraph("", font2));
				workPhoneValue.setBorderColor(BaseColor.WHITE);
				workPhoneValue.setBorderWidth(1f);
				contactInfo.addCell(workPhoneValue);

				PdfPCell phone = new PdfPCell(new Phrase("Phone:", font1));
				phone.setBorderColor(BaseColor.WHITE);
				contactInfo.addCell(phone);

				PdfPCell phoneValue = new PdfPCell(new Paragraph(
						tradeIn.getPhone(), font2));
				phoneValue.setBorderColor(BaseColor.WHITE);
				phoneValue.setBorderWidth(1f);
				contactInfo.addCell(phoneValue);

				PdfPCell email = new PdfPCell(new Phrase("Email", font1));
				email.setBorderColor(BaseColor.WHITE);
				contactInfo.addCell(email);

				PdfPCell emailValue = new PdfPCell(new Paragraph(
						tradeIn.getEmail(), font2));
				emailValue.setBorderColor(BaseColor.WHITE);
				emailValue.setBorderWidth(1f);
				contactInfo.addCell(emailValue);

				PdfPCell options = new PdfPCell(new Phrase("Options:", font1));
				options.setBorderColor(BaseColor.WHITE);
				contactInfo.addCell(options);

				PdfPCell optionsValue = new PdfPCell(new Paragraph(
						buffer.toString(), font2));
				optionsValue.setBorderColor(BaseColor.WHITE);
				optionsValue.setBorderWidth(1f);
				contactInfo.addCell(optionsValue);

				// --------------Vehicle Information

				PdfPTable vehicleInformationTitle = new PdfPTable(1);
				vehicleInformationTitle.setWidthPercentage(100);
				float[] vehicleInformationTitleWidth = { 2f };
				vehicleInformationTitle.setWidths(vehicleInformationTitleWidth);

				PdfPCell vehicleInformationTitleValue = new PdfPCell(
						new Phrase("Vehicle Information"));
				vehicleInformationTitleValue.setBorderColor(BaseColor.WHITE);
				vehicleInformationTitleValue.setBackgroundColor(new BaseColor(
						255, 255, 255));
				vehicleInformationTitle.addCell(vehicleInformationTitleValue);

				// ------------vehicle info data

				PdfPTable vehicleInformation = new PdfPTable(4);
				vehicleInformation.setWidthPercentage(100);
				float[] vehicleInformationWidth = { 2f, 2f, 2f, 2f };
				vehicleInformation.setWidths(vehicleInformationWidth);

				PdfPCell year = new PdfPCell(new Phrase("Year:", font1));
				year.setBorderColor(BaseColor.WHITE);
				year.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleInformation.addCell(year);

				PdfPCell yearValue = new PdfPCell(new Paragraph(
						tradeIn.getYear(), font2));
				yearValue.setBorderColor(BaseColor.WHITE);
				yearValue.setBorderWidth(1f);
				vehicleInformation.addCell(yearValue);

				PdfPCell make = new PdfPCell(new Phrase("Make:", font1));
				make.setBorderColor(BaseColor.WHITE);
				vehicleInformation.addCell(make);

				PdfPCell makeValue = new PdfPCell(new Paragraph(
						tradeIn.getMake(), font2));
				makeValue.setBorderColor(BaseColor.WHITE);
				makeValue.setBorderWidth(1f);
				vehicleInformation.addCell(makeValue);

				PdfPCell Model = new PdfPCell(new Phrase("Model:", font1));
				Model.setBorderColor(BaseColor.WHITE);
				vehicleInformation.addCell(Model);

				PdfPCell modelValue = new PdfPCell(new Paragraph(
						tradeIn.getModel(), font2));
				modelValue.setBorderColor(BaseColor.WHITE);
				modelValue.setBorderWidth(1f);
				vehicleInformation.addCell(modelValue);

				PdfPCell exteriorColour = new PdfPCell(new Phrase(
						"exteriorColour:", font1));
				exteriorColour.setBorderColor(BaseColor.WHITE);
				vehicleInformation.addCell(exteriorColour);

				PdfPCell exteriorColourValue = new PdfPCell(new Paragraph(
						tradeIn.getExteriorColour(), font2));
				exteriorColourValue.setBorderColor(BaseColor.WHITE);
				exteriorColourValue.setBorderWidth(1f);
				vehicleInformation.addCell(exteriorColourValue);

				PdfPCell vin = new PdfPCell(new Phrase("VIN:", font1));
				vin.setBorderColor(BaseColor.WHITE);
				vehicleInformation.addCell(vin);

				PdfPCell vinValue = new PdfPCell(new Paragraph(
						leadVM.vin, font2));
				vinValue.setBorderColor(BaseColor.WHITE);
				vinValue.setBorderWidth(1f);
				vehicleInformation.addCell(vinValue);

				PdfPCell kilometres = new PdfPCell(new Phrase("Kilometres:",
						font1));
				kilometres.setBorderColor(BaseColor.WHITE);
				vehicleInformation.addCell(kilometres);

				PdfPCell kilometresValue = new PdfPCell(new Paragraph(
						tradeIn.getKilometres(), font2));
				kilometresValue.setBorderColor(BaseColor.WHITE);
				kilometresValue.setBorderWidth(1f);
				vehicleInformation.addCell(kilometresValue);

				PdfPCell engine = new PdfPCell(new Phrase("Engine:", font1));
				engine.setBorderColor(BaseColor.WHITE);
				vehicleInformation.addCell(engine);

				PdfPCell engineValue = new PdfPCell(new Paragraph(
						tradeIn.getEngine(), font2));
				engineValue.setBorderColor(BaseColor.WHITE);
				engineValue.setBorderWidth(1f);
				vehicleInformation.addCell(engineValue);

				PdfPCell doors = new PdfPCell(new Phrase("Doors:", font1));
				doors.setBorderColor(BaseColor.WHITE);
				vehicleInformation.addCell(doors);

				PdfPCell doorsValue = new PdfPCell(new Paragraph(
						tradeIn.getDoors(), font2));
				doorsValue.setBorderColor(BaseColor.WHITE);
				doorsValue.setBorderWidth(1f);
				vehicleInformation.addCell(doorsValue);

				PdfPCell transmission = new PdfPCell(new Phrase(
						"Transmission:", font1));
				transmission.setBorderColor(BaseColor.WHITE);
				vehicleInformation.addCell(transmission);

				PdfPCell transmissionValue = new PdfPCell(new Paragraph(
						tradeIn.getTransmission(), font2));
				transmissionValue.setBorderColor(BaseColor.WHITE);
				transmissionValue.setBorderWidth(1f);
				vehicleInformation.addCell(transmissionValue);

				PdfPCell drivetrain = new PdfPCell(new Phrase("Drivetrain:",
						font1));
				drivetrain.setBorderColor(BaseColor.WHITE);
				vehicleInformation.addCell(drivetrain);

				PdfPCell drivetrainValue = new PdfPCell(new Paragraph(
						tradeIn.getDrivetrain(), font2));
				drivetrainValue.setBorderColor(BaseColor.WHITE);
				drivetrainValue.setBorderWidth(1f);
				vehicleInformation.addCell(drivetrainValue);

				// ----------------Vehicle Rating title----

				PdfPTable vehicleRatingTitle = new PdfPTable(1);
				vehicleRatingTitle.setWidthPercentage(100);
				float[] vehicleRatingTitleWidth = { 2f };
				vehicleRatingTitle.setWidths(vehicleRatingTitleWidth);

				PdfPCell vehicleRatingTitleValue = new PdfPCell(new Phrase(
						"Vehicle Rating"));
				vehicleRatingTitleValue.setBorderColor(BaseColor.WHITE);
				vehicleRatingTitleValue.setBackgroundColor(new BaseColor(255,
						255, 255));
				vehicleRatingTitle.addCell(vehicleRatingTitleValue);

				// ------------Vehicle Rating data

				PdfPTable vehicleRatingData = new PdfPTable(4);
				vehicleRatingData.setWidthPercentage(100);
				float[] vehicleRatingDataWidth = { 2f, 2f, 2f, 2f };
				vehicleRatingData.setWidths(vehicleRatingDataWidth);

				PdfPCell body = new PdfPCell(new Phrase("Body :", font1));
				body.setBorderColor(BaseColor.WHITE);
				body.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleRatingData.addCell(body);

				PdfPCell bodyValue = new PdfPCell(new Paragraph(
						tradeIn.getBodyRating(), font2));
				bodyValue.setBorderColor(BaseColor.WHITE);
				bodyValue.setBorderWidth(1f);
				vehicleRatingData.addCell(bodyValue);

				PdfPCell tires = new PdfPCell(new Phrase("Tires :", font1));
				tires.setBorderColor(BaseColor.WHITE);
				tires.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleRatingData.addCell(tires);

				PdfPCell tiresValue = new PdfPCell(new Paragraph(
						tradeIn.getTireRating(), font2));
				tiresValue.setBorderColor(BaseColor.WHITE);
				tiresValue.setBorderWidth(1f);
				vehicleRatingData.addCell(tiresValue);

				PdfPCell engineRate = new PdfPCell(
						new Phrase("Engine :", font1));
				engineRate.setBorderColor(BaseColor.WHITE);
				engineRate.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleRatingData.addCell(engineRate);

				PdfPCell engineRateValue = new PdfPCell(new Paragraph(
						tradeIn.getEngineRating(), font2));
				engineRateValue.setBorderColor(BaseColor.WHITE);
				engineRateValue.setBorderWidth(1f);
				vehicleRatingData.addCell(engineRateValue);

				PdfPCell transmissionRate = new PdfPCell(new Phrase(
						"Transmission :", font1));
				transmissionRate.setBorderColor(BaseColor.WHITE);
				transmissionRate
						.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleRatingData.addCell(transmissionRate);

				PdfPCell transmissionRateValue = new PdfPCell(new Paragraph(
						tradeIn.getTransmissionRating(), font2));
				transmissionRateValue.setBorderColor(BaseColor.WHITE);
				transmissionRateValue.setBorderWidth(1f);
				vehicleRatingData.addCell(transmissionRateValue);

				PdfPCell glass = new PdfPCell(new Phrase("Glass :", font1));
				glass.setBorderColor(BaseColor.WHITE);
				glass.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleRatingData.addCell(glass);

				PdfPCell glassValue = new PdfPCell(new Paragraph(
						tradeIn.getGlassRating(), font2));
				glassValue.setBorderColor(BaseColor.WHITE);
				glassValue.setBorderWidth(1f);
				vehicleRatingData.addCell(glassValue);

				PdfPCell interior = new PdfPCell(
						new Phrase("Interior :", font1));
				interior.setBorderColor(BaseColor.WHITE);
				interior.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleRatingData.addCell(interior);

				PdfPCell interiorValue = new PdfPCell(new Paragraph(
						tradeIn.getInteriorRating(), font2));
				interiorValue.setBorderColor(BaseColor.WHITE);
				interiorValue.setBorderWidth(1f);
				vehicleRatingData.addCell(interiorValue);

				PdfPCell exhaust = new PdfPCell(new Phrase("Exhaust :", font1));
				exhaust.setBorderColor(BaseColor.WHITE);
				exhaust.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleRatingData.addCell(exhaust);

				PdfPCell exhaustValue = new PdfPCell(new Paragraph(
						tradeIn.getExhaustRating(), font2));
				exhaustValue.setBorderColor(BaseColor.WHITE);
				exhaustValue.setBorderWidth(1f);
				vehicleRatingData.addCell(exhaustValue);

				// ----------------Vehicle History title----

				PdfPTable vehiclehistoryTitle = new PdfPTable(1);
				vehiclehistoryTitle.setWidthPercentage(100);
				float[] vehiclehistoryTitleWidth = { 2f };
				vehiclehistoryTitle.setWidths(vehiclehistoryTitleWidth);

				PdfPCell vehiclehistoryValue = new PdfPCell(new Phrase(
						"Vehicle History"));
				vehiclehistoryValue.setBorderColor(BaseColor.WHITE);
				vehiclehistoryValue.setBackgroundColor(new BaseColor(255, 255,
						255));
				vehiclehistoryTitle.addCell(vehiclehistoryValue);

				// ------------Vehicle History data

				PdfPTable vehicleHistoryData = new PdfPTable(4);
				vehicleHistoryData.setWidthPercentage(100);
				float[] vehicleHistoryDataWidth = { 2f, 2f, 2f, 2f };
				vehicleHistoryData.setWidths(vehicleHistoryDataWidth);

				PdfPCell rentalReturn = new PdfPCell(new Phrase(
						"Was it ever a lease or rental return?  :", font1));
				rentalReturn.setBorderColor(BaseColor.WHITE);
				rentalReturn.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleHistoryData.addCell(rentalReturn);

				PdfPCell rentalReturnValue = new PdfPCell(new Paragraph(
						tradeIn.getLeaseOrRental(), font2));
				rentalReturnValue.setBorderColor(BaseColor.WHITE);
				rentalReturnValue.setBorderWidth(1f);
				vehicleHistoryData.addCell(rentalReturnValue);

				PdfPCell operationalAndaccu = new PdfPCell(new Phrase(
						"Is the odometer operational and accurate?  :", font1));
				operationalAndaccu.setBorderColor(BaseColor.WHITE);
				operationalAndaccu.setBackgroundColor(new BaseColor(255, 255,
						255));
				vehicleHistoryData.addCell(operationalAndaccu);

				PdfPCell operationalAndaccuValue = new PdfPCell(new Paragraph(
						tradeIn.getOperationalAndAccurate(), font2));
				operationalAndaccuValue.setBorderColor(BaseColor.WHITE);
				operationalAndaccuValue.setBorderWidth(1f);
				vehicleHistoryData.addCell(operationalAndaccuValue);

				PdfPCell serviceRecodes = new PdfPCell(new Phrase(
						"Detailed service records available?   :", font1));
				serviceRecodes.setBorderColor(BaseColor.WHITE);
				serviceRecodes.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleHistoryData.addCell(serviceRecodes);

				PdfPCell serviceRecodesValue = new PdfPCell(new Paragraph(
						tradeIn.getServiceRecord(), font2));
				serviceRecodesValue.setBorderColor(BaseColor.WHITE);
				serviceRecodesValue.setBorderWidth(1f);
				vehicleHistoryData.addCell(serviceRecodesValue);

				// ----------------Title History title----

				PdfPTable historyTitle = new PdfPTable(1);
				historyTitle.setWidthPercentage(100);
				float[] historyTitleWidth = { 2f };
				historyTitle.setWidths(historyTitleWidth);

				PdfPCell historyTitleValue = new PdfPCell(new Phrase(
						"Title History"));
				historyTitleValue.setBorderColor(BaseColor.WHITE);
				historyTitleValue.setBackgroundColor(new BaseColor(255, 255,
						255));
				historyTitle.addCell(historyTitleValue);

				// ------------Title History data

				PdfPTable historyTitleData = new PdfPTable(2);
				historyTitleData.setWidthPercentage(100);
				float[] historyTitleDataWidth = { 2f, 2f };
				historyTitleData.setWidths(historyTitleDataWidth);

				PdfPCell lineholder = new PdfPCell(new Phrase(
						"Is there a lineholder? :", font1));
				lineholder.setBorderColor(BaseColor.WHITE);
				lineholder.setBackgroundColor(new BaseColor(255, 255, 255));
				historyTitleData.addCell(lineholder);

				PdfPCell lineholderValue = new PdfPCell(new Paragraph(
						tradeIn.getLienholder(), font2));
				lineholderValue.setBorderColor(BaseColor.WHITE);
				lineholderValue.setBorderWidth(1f);
				historyTitleData.addCell(lineholderValue);

				PdfPCell titleholder = new PdfPCell(new Phrase(
						"Who holds this title?  :", font1));
				titleholder.setBorderColor(BaseColor.WHITE);
				titleholder.setBackgroundColor(new BaseColor(255, 255, 255));
				historyTitleData.addCell(titleholder);

				PdfPCell titleholderValue = new PdfPCell(new Paragraph("",
						font2));
				titleholderValue.setBorderColor(BaseColor.WHITE);
				titleholderValue.setBorderWidth(1f);
				historyTitleData.addCell(titleholderValue);

				// ----------------Vehicle Assessment title----

				PdfPTable vehicleAssessmentTitle = new PdfPTable(1);
				vehicleAssessmentTitle.setWidthPercentage(100);
				float[] vehicleAssessmentTitleWidth = { 2f };
				vehicleAssessmentTitle.setWidths(vehiclehistoryTitleWidth);

				PdfPCell vehiclTitle = new PdfPCell(new Phrase(
						"Vehicle Assessment"));
				vehiclTitle.setBorderColor(BaseColor.WHITE);
				vehiclTitle.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleAssessmentTitle.addCell(vehiclTitle);

				// ------------Vehicle Assessment data

				PdfPTable vehicleAssessmentData = new PdfPTable(1);
				vehicleAssessmentData.setWidthPercentage(100);
				float[] vehicleAssessmentDataWidth = { 2f };
				vehicleAssessmentData.setWidths(vehicleAssessmentDataWidth);

				PdfPCell equipment = new PdfPCell(new Phrase(
						"Does all equipment and accessories work correctly? :",
						font1));
				equipment.setBorderColor(BaseColor.WHITE);
				equipment.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleAssessmentData.addCell(rentalReturn);

				PdfPCell equipmentValue = new PdfPCell(new Paragraph(
						tradeIn.getEquipment(), font2));
				equipmentValue.setBorderColor(BaseColor.WHITE);
				equipmentValue.setBorderWidth(1f);
				vehicleAssessmentData.addCell(equipmentValue);

				PdfPCell buyVehicle = new PdfPCell(new Phrase(
						"Did you buy the vehicle new? :", font1));
				buyVehicle.setBorderColor(BaseColor.WHITE);
				buyVehicle.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleAssessmentData.addCell(buyVehicle);

				PdfPCell buyVehicleValue = new PdfPCell(new Paragraph(
						tradeIn.getVehiclenew(), font2));
				buyVehicleValue.setBorderColor(BaseColor.WHITE);
				buyVehicleValue.setBorderWidth(1f);
				vehicleAssessmentData.addCell(buyVehicleValue);

				PdfPCell accidents = new PdfPCell(
						new Phrase(
								"Has the vehicle ever been in any accidents? Cost of repairs? :",
								font1));
				accidents.setBorderColor(BaseColor.WHITE);
				accidents.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleAssessmentData.addCell(accidents);

				PdfPCell accidentsValue = new PdfPCell(new Paragraph(
						tradeIn.getAccidents(), font2));
				accidentsValue.setBorderColor(BaseColor.WHITE);
				accidentsValue.setBorderWidth(1f);
				vehicleAssessmentData.addCell(accidentsValue);

				PdfPCell damage = new PdfPCell(new Phrase(
						"Is there existing damage on the vehicle? Where? :",
						font1));
				damage.setBorderColor(BaseColor.WHITE);
				damage.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleAssessmentData.addCell(damage);

				PdfPCell damageValue = new PdfPCell(new Paragraph(
						tradeIn.getDamage(), font2));
				damageValue.setBorderColor(BaseColor.WHITE);
				damageValue.setBorderWidth(1f);
				vehicleAssessmentData.addCell(damageValue);

				PdfPCell paintWork = new PdfPCell(new Phrase(
						"Has the vehicle ever had paint work performed? :",
						font1));
				paintWork.setBorderColor(BaseColor.WHITE);
				paintWork.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleAssessmentData.addCell(paintWork);

				PdfPCell paintWorkValue = new PdfPCell(new Paragraph(
						tradeIn.getPaint(), font2));
				paintWorkValue.setBorderColor(BaseColor.WHITE);
				paintWorkValue.setBorderWidth(1f);
				vehicleAssessmentData.addCell(paintWorkValue);

				PdfPCell salvage = new PdfPCell(
						new Phrase(
								"Is the title designated 'Salvage' or 'Reconstructed'? Any other? :",
								font1));
				salvage.setBorderColor(BaseColor.WHITE);
				salvage.setBackgroundColor(new BaseColor(255, 255, 255));
				vehicleAssessmentData.addCell(salvage);

				PdfPCell salvageValue = new PdfPCell(new Paragraph(
						tradeIn.getSalvage(), font2));
				salvageValue.setBorderColor(BaseColor.WHITE);
				salvageValue.setBorderWidth(1f);
				vehicleAssessmentData.addCell(salvageValue);

				// ----------sub main Table----------

				PdfPTable AddAllTableInMainTable = new PdfPTable(1);
				AddAllTableInMainTable.setWidthPercentage(100);
				float[] AddAllTableInMainTableWidth = { 2f };
				AddAllTableInMainTable.setWidths(AddAllTableInMainTableWidth);

				PdfPCell hotelVoucherTitlemain1 = new PdfPCell(Titlemain);
				hotelVoucherTitlemain1.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(hotelVoucherTitlemain1);

				PdfPCell contactInfoData = new PdfPCell(contactInfo);
				contactInfoData.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(contactInfoData);

				PdfPCell vehicaleInfoTitle = new PdfPCell(
						vehicleInformationTitle);
				vehicaleInfoTitle.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(vehicaleInfoTitle);

				PdfPCell vehicaleInfoData = new PdfPCell(vehicleInformation);
				vehicaleInfoData.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(vehicaleInfoData);

				PdfPCell vehicleRatingTitles = new PdfPCell(vehicleRatingTitle);
				vehicleRatingTitles.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(vehicleRatingTitles);

				PdfPCell vehicleRatinginfo = new PdfPCell(vehicleRatingData);
				vehicleRatinginfo.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(vehicleRatinginfo);

				PdfPCell vehicleHisTitle = new PdfPCell(vehiclehistoryTitle);
				vehicleHisTitle.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(vehicleHisTitle);

				PdfPCell vehicleHistoryInfo = new PdfPCell(vehicleHistoryData);
				vehicleHistoryInfo.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(vehicleHistoryInfo);

				PdfPCell historyTitles = new PdfPCell(historyTitle);
				historyTitles.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(historyTitles);

				PdfPCell historyTitleinfo = new PdfPCell(historyTitleData);
				historyTitleinfo.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(historyTitleinfo);

				PdfPCell vehicleTitleAssessment = new PdfPCell(
						vehicleAssessmentTitle);
				vehicleTitleAssessment.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(vehicleTitleAssessment);

				PdfPCell vehicleAssessmentDataTitle = new PdfPCell(
						vehicleAssessmentData);
				vehicleAssessmentDataTitle.setBorder(Rectangle.NO_BORDER);
				AddAllTableInMainTable.addCell(vehicleAssessmentDataTitle);

				// ----------main Table----------

				PdfPTable AddMainTable = new PdfPTable(1);
				AddMainTable.setWidthPercentage(100);
				float[] AddMainTableWidth = { 2f };
				AddMainTable.setWidths(AddMainTableWidth);

				PdfPCell AddAllTableInMainTable1 = new PdfPCell(
						AddAllTableInMainTable);
				AddAllTableInMainTable1.setPadding(10);
				AddAllTableInMainTable1.setBorderWidth(1f);
				AddMainTable.addCell(AddAllTableInMainTable1);

				document.add(AddMainTable);

				document.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			final String username = emailUsername;
			final String password = emailPassword;
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.starttls.enable", "true");
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username,
									password);
						}
					});
			try {
				List<AuthUser> users = AuthUser.getAllUsers();

				InternetAddress[] usersArray = new InternetAddress[users.size() + 1];
				int index = 0;
				usersArray[index] = new InternetAddress(user.getEmail());
				
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(emailUsername));
				message.setRecipients(Message.RecipientType.TO, usersArray);
				message.setSubject("Trade-In Appraisal");
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart = new MimeBodyPart();

				VelocityEngine ve = new VelocityEngine();
				ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
						"org.apache.velocity.runtime.log.Log4JLogChute");
				ve.setProperty("runtime.log.logsystem.log4j.logger",
						"clientService");
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
				ve.setProperty("classpath.resource.loader.class",
						ClasspathResourceLoader.class.getName());
				ve.init();

				String urlfind = "http://www.glider-autos.com/dealer/index.html#/tradeIn";

				Template t = ve.getTemplate("/public/emailTemplate/trade_in_app.vm");
				VelocityContext context = new VelocityContext();

				// ---------Trad in info---------------

				// contact info
				context.put("first_name", tradeIn.getFirstName());
				context.put("last_name", "");
				context.put("work_phone", "");
				context.put("email", tradeIn.getEmail());

				context.put("year", tradeIn.getYear());
				context.put("make", tradeIn.getMake());
				context.put("model", tradeIn.getModel());
				context.put("price", "$" + vehicle.getPrice());
				context.put("vin", vehicle.getVin());
				context.put("stock", vehicle.getStock());
				context.put("mileage", vehicle.getMileage());
				context.put("pdffilePath", findpath);

				if (tradeIn.getPreferredContact() != null) {
					context.put("preferred", tradeIn.getPreferredContact());
				} else {
					context.put("preferred", "");
				}

				context.put("optionValue", buffer.toString());

				// vehicale info

				if (tradeIn.getYear() != null) {
					context.put("yearValue", tradeIn.getYear());
				} else {
					context.put("yearValue", "");
				}
				if (tradeIn.getMake() != null) {
					context.put("makeValue", tradeIn.getMake());
				} else {
					context.put("makeValue", "");
				}
				if (tradeIn.getModel() != null) {
					context.put("modelValue", tradeIn.getModel());
				} else {
					context.put("modelValue", "");
				}
				if (tradeIn.getExteriorColour() != null) {
					context.put("exterior_colour", tradeIn.getExteriorColour());
				} else {
					context.put("exterior_colour", "");
				}

				if (tradeIn.getKilometres() != null) {
					context.put("kilometres", tradeIn.getKilometres());
				} else {
					context.put("kilometres", "");
				}

				if (tradeIn.getEngine() != null) {
					context.put("engine", tradeIn.getEngine());
				} else {
					context.put("engine", "");
				}

				if (tradeIn.getModel() != null) {
					context.put("doors", tradeIn.getModel());
				} else {
					context.put("doors", "");
				}

				if (tradeIn.getTransmission() != null) {
					context.put("transmission", tradeIn.getTransmission());
				} else {
					context.put("transmission", "");
				}

				if (tradeIn.getDrivetrain() != null) {
					context.put("drivetrain", tradeIn.getDrivetrain());
				} else {
					context.put("drivetrain", "");
				}

				// vehicale rating

				if (tradeIn.getBodyRating() != null) {
					context.put("body_rating", tradeIn.getBodyRating());
				} else {
					context.put("body_rating", "");
				}

				if (tradeIn.getTireRating() != null) {
					context.put("tire_rating", tradeIn.getTireRating());
				} else {
					context.put("tire_rating", "");
				}

				if (tradeIn.getEngineRating() != null) {
					context.put("engine_rating", tradeIn.getEngineRating());
				} else {
					context.put("engine_rating", "");
				}

				if (tradeIn.getTransmissionRating() != null) {
					context.put("transmission_rating",
							tradeIn.getTransmissionRating());
				} else {
					context.put("transmission_rating", "");
				}

				if (tradeIn.getGlassRating() != null) {
					context.put("glass_rating", tradeIn.getGlassRating());
				} else {
					context.put("glass_rating", "");
				}

				if (tradeIn.getInteriorRating() != null) {
					context.put("interior_rating", tradeIn.getInteriorRating());
				} else {
					context.put("interior_rating", "");
				}

				if (tradeIn.getExhaustRating() != null) {
					context.put("exhaust_rating", tradeIn.getExhaustRating());
				} else {
					context.put("exhaust_rating", "");
				}

				// vehicale History

				if (tradeIn.getLeaseOrRental() != null) {
					context.put("lease_or_rental", tradeIn.getLeaseOrRental());
				} else {
					context.put("lease_or_rental", "");
				}

				if (tradeIn.getOperationalAndAccurate() != null) {
					context.put("operational_and_accurate",
							tradeIn.getOperationalAndAccurate());
				} else {
					context.put("operational_and_accurate", "");
				}

				if (tradeIn.getServiceRecord() != null) {
					context.put("service_record", tradeIn.getServiceRecord());
				} else {
					context.put("service_record", "");
				}

				// title History

				if (tradeIn.getLienholder() != null) {
					context.put("lienholder", tradeIn.getLienholder());
				} else {
					context.put("lienholder", "");
				}

				if (tradeIn.getHoldsThisTitle() != null) {
					context.put("holds_this_title", tradeIn.getHoldsThisTitle());
				} else {
					context.put("holds_this_title", "");
				}

				// Vehicle Assessment

				if (tradeIn.getEquipment() != null) {
					context.put("equipment", tradeIn.getEquipment());
				} else {
					context.put("equipment", "");
				}

				if (tradeIn.getVehiclenew() != null) {
					context.put("vehiclenew", tradeIn.getVehiclenew());
				} else {
					context.put("vehiclenew", "");
				}

				if (tradeIn.getAccidents() != null) {
					context.put("accidents", tradeIn.getAccidents());
				} else {
					context.put("accidents", "");
				}

				if (tradeIn.getDamage() != null) {
					context.put("damage", tradeIn.getDamage());
				} else {
					context.put("damage", "");
				}

				if (tradeIn.getPaint() != null) {
					context.put("paint", tradeIn.getPaint());
				} else {
					context.put("paint", "");
				}

				if (tradeIn.getSalvage() != null) {
					context.put("salvage", tradeIn.getSalvage());
				} else {
					context.put("salvage", "");
				}

				context.put("sitelogo", siteLogo.getLogoImageName());
				context.put("path", siteLogo.getLogoImagePath());
				context.put("heading1", heading1);
				context.put("heading2", heading2);
				context.put("urlLink", vehicleUrlPath);
				context.put("urlfind", urlfind);
				context.put("hostnameimg", imageUrlPath);

				StringWriter writer = new StringWriter();
				t.merge(context, writer);
				String content = writer.toString();
				// attachPart.attachFile(file);
				messageBodyPart.setContent(content, "text/html");
				multipart.addBodyPart(messageBodyPart);

				message.setContent(multipart);
				Transport.send(message);
				System.out.println("Sent test message successfully....");
			} catch (Exception e) {
				e.printStackTrace();
			}
    		}
    	 }else{
    		 
    		 StringBuffer buffer = new StringBuffer();
     		for(String opt:leadVM.options) {
     			buffer.append(opt+",");
     		}
     		if(buffer.length()>0) {
     			buffer.deleteCharAt(buffer.length()-1);
     		}
    		 
    		 tIn.setAccidents(leadVM.accidents);
    		 tIn.setEnthicity(leadVM.enthicity);
    		 tIn.setAssignedTo(user);
    		 tIn.setIsReassigned(true);
    		 tIn.setLeadStatus(null);
    		 tIn.setBodyRating(leadVM.bodyRating);
    		 tIn.setComments(leadVM.comments);
    		 tIn.setContactedFrom(leadVM.contactedFrom);
    		 tIn.setDamage(leadVM.damage);
    		 tIn.setDoors(leadVM.doors);
    		 tIn.setDrivetrain(leadVM.drivetrain);
    		 tIn.setEmail(leadVM.custEmail);
    		 tIn.setCustZipCode(leadVM.custZipCode);
    		 tIn.setEngine(leadVM.engine);
    		 tIn.setEngineRating(leadVM.engineRating);
    		 tIn.setEquipment(leadVM.equipment);
    		 tIn.setExhaustRating(leadVM.exhaustRating);
    		 tIn.setExteriorColour(leadVM.exteriorColour);
    		 tIn.setFirstName(leadVM.custName);
    		 tIn.setGlassRating(leadVM.glassRating);
    		 tIn.setHearedFrom(leadVM.hearedFrom);
    		 tIn.setInteriorRating(leadVM.interiorRating);
    		 tIn.setKilometres(leadVM.kilometres);
    		 tIn.setLeaseOrRental(leadVM.rentalReturn);
    		 tIn.setLienholder(leadVM.lienholder);
    		 tIn.setMake(makestr);
    		 tIn.setModel(model);
    		 tIn.setOperationalAndAccurate(leadVM.odometerAccurate);
    		 tIn.setOptionValue(buffer.toString());
    		 tIn.setPaint(leadVM.paint);
    		 tIn.setPhone(leadVM.custNumber);
    		 tIn.setPreferredContact(leadVM.prefferedContact);
    		 tIn.setSalvage(leadVM.salvage);
    		// tIn.setScheduleDate(new Date());
    		 tIn.setTireRating(leadVM.tireRating);
    		 tIn.setTradeDate(new Date());
    		 tIn.setTransmission(leadVM.transmission);
    		 tIn.setUser(user);
    		 tIn.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    		 tIn.setVehiclenew(leadVM.vehiclenew);
    		 tIn.setYear(leadVM.year);
    		 tIn.update();
     		
     		
    	 }
    	}
    	return ok();
    }
    
	public static void createDir(String pdfRootDir,long locationId, long lastId) {
        File file = new File(pdfRootDir + File.separator+ locationId +File.separator+ "trade_in_pdf"+File.separator+lastId);
        if (!file.exists()) {
                file.mkdirs();
        }
	}
	
	public static Result uploadContactsFile() throws Exception {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
		    	    	 }
	    	    	 
	    	     }
	    	  } 
	    	return ok();
    	}	
    }
	
	public static Result getAllContactsData() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	
	public static Result updateContactsData() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    		return ok();
    	}
	}
	
	public static Result addNewsLetter(String flag,Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	
	public static Result saveContactsData() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    		} else {
    			msg = "Email already exists";
    		}
    		return ok(msg);
    	}
	}
	
	public static Result saveNewsletterDate(String date,String time,Long id,String newsTimeZone) throws ParseException {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
    		if(id == 0) {
	    		NewsletterDate obj = new NewsletterDate();
	    		obj.dateOfMonth = date;
	    		Date dt = new Date();
				obj.newsletterTime = df.parse(time);
				Date d1 = df.parse(time);
				Calendar cal = Calendar.getInstance();
  		   	  	switch(newsTimeZone){
  		   	  
	  		   	  case "EST" : 
	  		   	  
	  		   	  cal = Calendar.getInstance();
	  		   	  cal.setTime(d1);
	  		   	  cal.add(Calendar.HOUR_OF_DAY, 5);
	  		   	  d1 = cal.getTime();
	  		   	  obj.gmtTime = d1;
	  		   	  obj.timeZone = newsTimeZone;
	  		   	  
	  		   	  break;
	  		   	  case "CST" : 
	  		   	  
	  		   	  cal = Calendar.getInstance();
	  		   	  cal.setTime(d1);
	  		   	  cal.add(Calendar.HOUR_OF_DAY, 6);
	  		   	  d1 = cal.getTime();
	  		   	  obj.gmtTime = d1;
	  		   	  obj.timeZone = newsTimeZone;
	  		   	  break;
	  		   	  
	  		   	  case "PST" : 
	  		   	  
	  		   	  cal = Calendar.getInstance();
	  		   	  cal.setTime(d1);
	  		   	  cal.add(Calendar.HOUR_OF_DAY, 8);
	  		   	  d1 = cal.getTime();
	  		   	  obj.gmtTime = d1;
	  		   	  obj.timeZone = newsTimeZone;
	  		   	  break;
	  		   	  
	  		   	  case "MST" : 
	  		   	  
	  		   	  cal = Calendar.getInstance();
	  		   	  cal.setTime(d1);
	  		   	  cal.add(Calendar.HOUR_OF_DAY, 7);
	  		   	  d1 = cal.getTime();
	  		   	  obj.gmtTime = d1;
	  		   	  obj.timeZone = newsTimeZone;
	  		   	  break;
  		   	  }
	    		obj.save();
	    		NewsLetter.newsletterSchedulling();
    		} else {
    			NewsletterDate obj = NewsletterDate.findById(id);
    			obj.setDateOfMonth(date);
    			obj.setNewsletterTime(df.parse(time));
    			Date d1 = df.parse(time);
				Calendar cal = Calendar.getInstance();
  		   	  	switch(newsTimeZone){
  		   	  
	  		   	  case "EST" : 
	  		   	  
	  		   	  cal = Calendar.getInstance();
	  		   	  cal.setTime(d1);
	  		   	  cal.add(Calendar.HOUR_OF_DAY, 5);
	  		   	  d1 = cal.getTime();
	  		   	  obj.setGmtTime(d1);
	  		   	  obj.setTimeZone(newsTimeZone);
	  		   	  
	  		   	  break;
	  		   	  case "CST" : 
	  		   	  
	  		   	  cal = Calendar.getInstance();
	  		   	  cal.setTime(d1);
	  		   	  cal.add(Calendar.HOUR_OF_DAY, 6);
	  		   	  d1 = cal.getTime();
	  		   	  obj.setGmtTime(d1);
	  		   	  obj.setTimeZone(newsTimeZone);
	  		   	  break;
	  		   	  
	  		   	  case "PST" : 
	  		   	  
	  		   	  cal = Calendar.getInstance();
	  		   	  cal.setTime(d1);
	  		   	  cal.add(Calendar.HOUR_OF_DAY, 8);
	  		   	  d1 = cal.getTime();
	  		   	  obj.setGmtTime(d1);
	  		   	  obj.setTimeZone(newsTimeZone);
	  		   	  break;
	  		   	  
	  		   	  case "MST" : 
	  		   	  
	  		   	  cal = Calendar.getInstance();
	  		   	  cal.setTime(d1);
	  		   	  cal.add(Calendar.HOUR_OF_DAY, 7);
	  		   	  d1 = cal.getTime();
	  		   	  obj.setGmtTime(d1);
	  		   	  obj.setTimeZone(newsTimeZone);
	  		   	  break;
  		   	  }
    			obj.update();
    			NewsLetter.newsletterSchedulling();
    		}
    		
    		return ok();
    	}
	}	
	
	public static Result sendNewsletterEmail() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -30);
		List<Blog> blogList = Blog.getBlogsByDate(cal.getTime(),date);
		AuthUser user = getLocalUser();
		//AuthUser user = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
		
		//MyProfile profile = MyProfile.findByUser(user);
		List<MyProfile> profile = MyProfile.findByLocation(Long.valueOf(session("USER_LOCATION")));
		String mapUrl = "http://maps.google.com/?q="+profile.get(0).address+","+profile.get(0).city+","+profile.get(0).zip+","+profile.get(0).state+","+profile.get(0).country;
		if(blogList.size() > 0) {
			AuthUser logoUser = getLocalUser();
			//AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
	    	SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); //findByUser(logoUser);
			List<Contacts> contactsList = Contacts.getAllNewsletter();
			for(Contacts contact : contactsList) {
				Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", "smtp.gmail.com");
		 		props.put("mail.smtp.port", "587");
		  
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUsername, emailPassword);
		 			}
		 		  });
		  
		 		try{
		 		   
		  			Message message = new MimeMessage(session);
		  			message.setFrom(new InternetAddress("glider.autos@gmail.com"));
		  			message.setRecipients(Message.RecipientType.TO,
		  			InternetAddress.parse(contact.getEmail()));
		  			message.setSubject("Newsletter ");	  			
		  			Multipart multipart = new MimeMultipart();
	    			BodyPart messageBodyPart = new MimeBodyPart();
	    			messageBodyPart = new MimeBodyPart();
	    			
	    			VelocityEngine ve = new VelocityEngine();
	    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
	    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
	    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
	    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	    			ve.init();
	    			
	    			Template t = ve.getTemplate("/public/emailTemplate/newsletterTemplate.vm"); 
	    	        VelocityContext context = new VelocityContext();
	    	        context.put("hostnameUrl", imageUrlPath);
	    	        context.put("siteLogo", logo.logoImagePath);
	    	        context.put("title", blogList.get(0).getTitle());
	    	        context.put("blogImage", blogList.get(0).getImageUrl());
	    	        context.put("description", blogList.get(0).getDescription());
	    	        context.put("mapUrl", mapUrl);
	    	        context.put("phone", profile.get(0).phone);
	    	        context.put("email", profile.get(0).email);
	    	        
	    	        StringWriter writer = new StringWriter();
	    	        t.merge( context, writer );
	    	        String content = writer.toString(); 
	    			
	    			messageBodyPart.setContent(content, "text/html");
	    			multipart.addBodyPart(messageBodyPart);
	    			message.setContent(multipart);
	    			Transport.send(message);
	    			System.out.println("email send");
		       		} catch (MessagingException e) {
		  			  throw new RuntimeException(e);
		  		}
			}
		}
		return ok();
	}
	
	public static Result deleteContactsById(Long id ){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   	return ok(home.render(""));
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
	
	public static Result checkEmailOfUser(String email ){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   	return ok(home.render(""));
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
	
	
	public static Result googleConnectionStatus() throws Exception { 
		try {
			authorize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return redirect(authorize());
		
	}
	/*public static Result googleConnectionStatusTasks() throws Exception { 
		try {
			authorizeTasks();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flagValue = 1;
		return redirect(authorizeTasks());
		
	}*/
	
	public static Result oauth2Callback() {
		
		
		/*AuthUser user11 = (AuthUser)getLocalUser();
		session("USER_KEY", user11.id+"");
		session("USER_ROLE", user11.role+"");
		
		if(user11.location != null){
			session("USER_LOCATION", user11.location.id+"");
		}*/
		
		String code = request().getQueryString("code");
		//if(flagValue == 0){
			events1.clear();
			try {
				TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
				credential = flow.createAndStoreCredential(response, "userID");
				client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential).
						setApplicationName(APPLICATION_NAME).build();

				oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
						APPLICATION_NAME).build();
				com.google.api.services.calendar.Calendar.Events events=client.events();
				com.google.api.services.calendar.model.Events eventList=events.list("primary").execute();
				events1 = eventList.getItems();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
				Calendar calendar = Calendar.getInstance();
				Calendar calendar1 = Calendar.getInstance();
				for(Event ev:events1){
					ScheduleTest  scheduleTest = new ScheduleTest();
					calendar.setTimeInMillis(ev.getEnd().getDateTime().getValue());
					calendar1.setTimeInMillis(ev.getStart().getDateTime().getValue());
					String startDate = df.format(calendar1.getTime());
					String starttime = parseTime.format(calendar1.getTime());
					scheduleTest.setConfirmDate(df.parse(startDate));
					scheduleTest.setConfirmTime(parseTime.parse(starttime));
					scheduleTest.setEmail(ev.getSummary());
					scheduleTest.setVin("no");
					scheduleTest.setGoogle_id(ev.getId());
					scheduleTest.setIs_google_data(true);
					scheduleTest.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
					scheduleTest.save();
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			Calendar c1 = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
	        c.add(Calendar.DAY_OF_YEAR, +7);
	        
			tasksList.clear();
			Tasks tasks = null;
			List<String> idtastlist = new ArrayList<>();
			
			try {
				//TokenResponse response = flowtask.newTokenRequest(code).setRedirectUri(redirectURI).execute();
				//credentialtask = flowtask.createAndStoreCredential(response, "userID");
				service = new com.google.api.services.tasks.Tasks.Builder(httpTransport, JSON_FACTORY, credential)
		           .setApplicationName(APPLICATION_NAME).build();
				TaskLists taskLists = service.tasklists().list().execute();
				//String code1 = null;
				for (TaskList taskList : taskLists.getItems()) {
				  idtastlist.add(taskList.getId());
				}
				AuthUser user = getLocalUser();
				for(String taskId:idtastlist){
					tasks = service.tasks().list(taskId).execute();
					tasksList.add(tasks);
					for (Task task : tasks.getItems()) {
						//tasksList.add(task);
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Calendar calendar = Calendar.getInstance();
						try{
							calendar.setTimeInMillis(task.getDue().getValue());
						}catch(Exception e){
							calendar.setTimeInMillis(task.getUpdated().getValue());
						}
						
						String dateValue = df.format(calendar.getTime());
						String googleDate = sdf.format(calendar.getTime());
						String sevenDayDate = sdf.format(c.getTime());
						String currDate = sdf.format(c1.getTime());
						if(sdf.parse(sevenDayDate).after(sdf.parse(googleDate)) && sdf.parse(googleDate).after(sdf.parse(currDate))){
							List<ToDo> toDo1 = ToDo.findByDateAndTask(df.parse(dateValue), task.getTitle()); 
							if(toDo1.size() == 0){
								ToDo toDo = new ToDo();
					    		toDo.setTask(task.getTitle());
					    		toDo.setStatus("Assigned");
					    		toDo.setDueDate(df.parse(dateValue));
					    		toDo.setPriority("High");
					    		toDo.setAssignedBy(user);
					    		toDo.setAssignedTo(user);
					    		toDo.setSaveas(1);
					    		toDo.setGoogle_id(task.getId());
								//toDo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					    		toDo.save();
							}
						}else if(sdf.parse(currDate).equals(sdf.parse(googleDate))){
							List<ToDo> toDo1 = ToDo.findByDateAndTask(df.parse(dateValue), task.getTitle()); 
							if(toDo1.size() == 0){
								ToDo toDo = new ToDo();
								toDo.setTask(task.getTitle());
					    		toDo.setStatus("Assigned");
					    		toDo.setDueDate(df.parse(dateValue));
					    		toDo.setPriority("High");
					    		toDo.setAssignedBy(user);
					    		toDo.setAssignedTo(user);
					    		toDo.setSaveas(1);
					    		toDo.setGoogle_id(task.getId());
								//toDo.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					    		toDo.save();
							}
						}
					}
				}
						
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*AuthUser user = getLocalUser();
			HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
			List<Permission> userPermissions = user.getPermission();
			for(Permission per: userPermissions) {
				permission.put(per.name, true);
			}*/
		//	return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList))));
			return redirect("/authenticate");
	}
	
	

	static List<String> SCOPES = new ArrayList<String>();
	//static List<String> SCOPESTASK = new ArrayList<String>();

	private static String  authorize() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if(flow==null){
			Details web=new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			SCOPES.add(CalendarScopes.CALENDAR);
			SCOPES.add(TasksScopes.TASKS);
			SCOPES.add(TasksScopes.TASKS_READONLY);
			SCOPES.add(Oauth2Scopes.USERINFO_EMAIL);
			SCOPES.add(Oauth2Scopes.USERINFO_PROFILE);
			
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					SCOPES).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
		
		return authorizationUrl.build();
	}

	/*private static String  authorizeTasks() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		System.out.println(flowtask);
		if(flowtask==null){
			Details web=new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecretstask = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			SCOPESTASK.add(TasksScopes.TASKS);
			SCOPESTASK.add(TasksScopes.TASKS_READONLY);
			SCOPESTASK.add(Oauth2Scopes.USERINFO_EMAIL);
			SCOPESTASK.add(Oauth2Scopes.USERINFO_PROFILE);
			flowtask = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecretstask,
					SCOPESTASK).build();
		}
		authorizationUrl = flowtask.newAuthorizationUrl().setRedirectUri(redirectURI);
		
		return authorizationUrl.build();
	}*/

	
	
	public Set<Event> getEvents() throws IOException{
		return this.events;
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
	
	public static Result getScheduleTestData(){
        AuthUser user = getLocalUser();
        List<ScheduleTest> list = ScheduleTest.findAllByUserServiceTest(user);
        
        List<ScheduleTestVM> shList = new ArrayList<ScheduleTestVM>();
        for(ScheduleTest scTest:list){
        	ScheduleTestVM sTestVM = new ScheduleTestVM();
        	sTestVM.id = scTest.id;
        	sTestVM.is_google_data = scTest.is_google_data;
        	sTestVM.google_id = scTest.google_id;
        	sTestVM.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(scTest.confirmDate);
        	sTestVM.confirmTime =  new SimpleDateFormat("hh:mm a").format(scTest.confirmTime);
        	shList.add(sTestVM);
        }
        return ok(Json.toJson(shList));
    }
	
	public static Result getDealerProfile(){
		Map<String,Object> map = new HashMap<>();
		AuthUser user = getLocalUser();
		MyProfile mProfile = MyProfile.findByUser(user);
		map.put("user", user);
		map.put("dealer", mProfile);
		return ok(Json.toJson(map));
	}

	public static Result updateCalender(){
		DynamicForm form = DynamicForm.form().bindFromRequest();
		String id = form.get("id");
		String confDate = form.get("confDate");
		String confTime = form.get("confTime");
		String googleID = form.get("googleID");
		
		SessionUseVM vm = new SessionUseVM();
		vm.id = id;
		vm.cnfDate = confDate;
		vm.cnfTime = confTime;
		vm.eventID = googleID;
		session("sessionVmData", Json.stringify(Json.toJson(vm)));
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		
		try {
			ScheduleTest test = ScheduleTest.findById(Long.parseLong(id));
			test.setConfirmDate(df.parse(confDate));
			test.setConfirmTime(new SimpleDateFormat("hh:mm a").parse(confTime));
			test.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*try {
			if(test.getGoogle_id()!=null){
				return redirect(authorizeUpdate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		/**/
		return redirect("/authenticate");
		//return ok();
		}
	
	private static String  authorizeUpdate() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if(flow==null){
			Details web=new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			SCOPES.add(CalendarScopes.CALENDAR);
			
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					SCOPES).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURIUpdate);
		
		return authorizationUrl.build();
	}
	
	public static Result updatecalenderdata(){
		String  sessionValue =  session("sessionVmData");
		
		String code = request().getQueryString("code");
		events1.clear();
		try {
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURIUpdate).execute();
			credential = flow.createAndStoreCredential(response, "userID");
			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential).
					setApplicationName(APPLICATION_NAME).build();

			oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
					APPLICATION_NAME).build();
			/*com.google.api.services.calendar.Calendar.Events events=client.events();
			Event ev = events.get("primary", vm.eventID).execute();
			Date date = new Date(vm.cnfDate);
			Date time = new Date(vm.cnfTime);
			Date cpl = new Date(date.getYear(), date.getMonth(), date.getDay(), 
                    time.getHours(), time.getMinutes(), time.getSeconds());
			DateTime dateTime = new DateTime(cpl);
			ev.setStart(dateTime);
			Event updatedEvent = events.update("primary", ev.getId(), ev).execute();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok();
	}
	
	public static Result getAllLocations(){
		List<Location> list = Location.findAllActiveType();
		return ok(Json.toJson(list));
	}
	

	public static Result getManagers(Long value){
		Location loc = Location.findById(value);
		List<AuthUser> user = AuthUser.getlocationAndManager(loc);
		return ok(Json.toJson(user));
	}
	
	public static Result getLocationUsers(Long value){
		Location loc = Location.findById(value);
		List<AuthUser> user = AuthUser.getAllUserByLocation(loc);
		return ok(Json.toJson(user));
	}
	public static Result updatePlan(){
		Form<PlanScheduleVM> form = DynamicForm.form(PlanScheduleVM.class).bindFromRequest();
		PlanScheduleVM vm = form.get();
		int flag = 0;
		int flag1 = 0;
		if(vm.scheduleBy.equals("location")){
			flag = updateLocationWise(vm, vm.location);
			
			if(vm.locationList != null){
			for(Long locationid : vm.locationList){
				if(vm.location != null){
					if(locationid != vm.location){
						flag1 = updateLocationWise(vm, locationid);
					}
				}else{
					if(locationid != Long.valueOf(session("USER_LOCATION"))){
						flag1 = updateLocationWise(vm, locationid);
					}
				}
				
			}
			}
		}else if(vm.scheduleBy.equals("salePerson")){
			flag = updateSalepersonWise(vm, vm.salePerson);
			
			if(vm.salesList != null){
				for(Integer salesId : vm.salesList){
					if(!salesId.equals(vm.salePerson)){
						flag1 = updateSalepersonWise(vm, salesId);
					}
				}
				}
		}
		return ok(Json.toJson(flag));
	}
	
	public static int updateLocationWise(PlanScheduleVM vm,Long locationId){
		AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Location location = null;
		String[] parts = vm.startDate.split("-");
		String[] parts1 = vm.endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		int flag = 0;
		
		List<PlanSchedule> planSchedule = null;
		
		if(locationId != null){
			location = Location.findById(locationId);
			planSchedule = PlanSchedule.findAllByLocation(locationId);
		}else{
			if(session("USER_ROLE").equals("Manager")){
				location = Location.findById(Long.valueOf(session("USER_LOCATION")));
				planSchedule = PlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
			}
		}
		
		
		
		for(PlanSchedule checkDate:planSchedule){
			if(checkDate.id != vm.id){
				try {
					if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
						flag = 1;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		if(flag == 0){
			PlanSchedule plSchedule = PlanSchedule.findById(vm.id);
			plSchedule.setCarsSold(vm.carsSold);
			plSchedule.setContractsSign(vm.contractsSign);
			plSchedule.setDayContract(vm.dayContract);
			try {
				plSchedule.setStartDate(df1.parse(startDateString));
				plSchedule.setEndDate(df1.parse(endDateString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			plSchedule.setMeetingSalesAm(vm.meetingSalesAm);
			plSchedule.setMonthContract(vm.monthContract);
			plSchedule.setQuarterContract(vm.quarterContract);
			plSchedule.setSixMonthContract(vm.sixMonthContract);
			plSchedule.setTotalEarn(vm.totalEarn);
			plSchedule.setTotalMeetingAm(vm.totalMeetingAm);
			plSchedule.setWeekContract(vm.weekContract);
			plSchedule.setWorkWithClient(vm.workWithClient);
			plSchedule.setLocations(location);
			plSchedule.setUser(user);
			plSchedule.update();
		
		}
		
		return flag;
	}
	
	public static int updateSalepersonWise(PlanScheduleVM vm,int salesId){
		//AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String[] parts = vm.startDate.split("-");
		String[] parts1 = vm.endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		
		
		Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
		
		AuthUser user = AuthUser.findById(salesId);
		
		List<SalesPlanSchedule> planSchedule = SalesPlanSchedule.findAllByUser(user);
		
		int flag = 0;
		for(SalesPlanSchedule checkDate:planSchedule){
			
			try {
				if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
					flag = 1;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(flag == 0){
				SalesPlanSchedule plSchedule = SalesPlanSchedule.findById(vm.id);
				plSchedule.setCarsSold(vm.carsSold);
				plSchedule.setContractsSign(vm.contractsSign);
				plSchedule.setDayContract(vm.dayContract);
				try {
					plSchedule.setStartDate(df1.parse(startDateString));
					plSchedule.setEndDate(df1.parse(endDateString));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				plSchedule.setMeetingSalesAm(vm.meetingSalesAm);
				plSchedule.setMonthContract(vm.monthContract);
				plSchedule.setQuarterContract(vm.quarterContract);
				plSchedule.setSixMonthContract(vm.sixMonthContract);
				plSchedule.setTotalEarn(vm.totalEarn);
				plSchedule.setTotalMeetingAm(vm.totalMeetingAm);
				plSchedule.setWeekContract(vm.weekContract);
				plSchedule.setWorkWithClient(vm.workWithClient);
				plSchedule.setLocations(location);
				plSchedule.setUser(user);
				plSchedule.update();
		}
		
		return flag;
	}
	
	public static Result savePlan(){
		
		Form<PlanScheduleVM> form = DynamicForm.form(PlanScheduleVM.class).bindFromRequest();
		PlanScheduleVM vm = form.get();
		int flag = 0;
		int flag1 = 0;
		if(vm.scheduleBy.equals("location")){
			flag = saveLocationWise(vm, vm.location);
			
			if(vm.locationList != null){
			for(Long locationid : vm.locationList){
				if(vm.location != null){
					if(locationid != vm.location){
						flag1 = saveLocationWise(vm, locationid);
					}
				}else{
					if(locationid != Long.valueOf(session("USER_LOCATION"))){
						flag1 = saveLocationWise(vm, locationid);
					}
				}
				
			}
			}
		}else if(vm.scheduleBy.equals("salePerson")){
			flag = saveSalepersonWise(vm, vm.salePerson);
			
			if(vm.salesList != null){
				for(Integer salesId : vm.salesList){
					if(!salesId.equals(vm.salePerson)){
						flag1 = saveSalepersonWise(vm, salesId);
					}
				}
				}
		}
		
		
		
		return ok(Json.toJson(flag));
	}
	
	public static int saveSalepersonWise(PlanScheduleVM vm,int salesId){
		//AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String[] parts = vm.startDate.split("-");
		String[] parts1 = vm.endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
		
		AuthUser user = AuthUser.findById(salesId);
		
		List<SalesPlanSchedule> planSchedule = SalesPlanSchedule.findAllByUser(user);
		
		int flag = 0;
		for(SalesPlanSchedule checkDate:planSchedule){
			
			try {
				if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
					flag = 1;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(flag == 0){
			SalesPlanSchedule plSchedule = new SalesPlanSchedule();
			
			plSchedule.setCarsSold(vm.carsSold);
			plSchedule.setContractsSign(vm.contractsSign);
			plSchedule.setDayContract(vm.dayContract);
			try {
				plSchedule.setStartDate(df1.parse(startDateString));
				plSchedule.setEndDate(df1.parse(endDateString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			plSchedule.setMeetingSalesAm(vm.meetingSalesAm);
			plSchedule.setMonthContract(vm.monthContract);
			plSchedule.setQuarterContract(vm.quarterContract);
			plSchedule.setSixMonthContract(vm.sixMonthContract);
			plSchedule.setTotalEarn(vm.totalEarn);
			plSchedule.setTotalMeetingAm(vm.totalMeetingAm);
			plSchedule.setWeekContract(vm.weekContract);
			plSchedule.setWorkWithClient(vm.workWithClient);
			plSchedule.setLocations(location);
			plSchedule.setUser(user);
			plSchedule.save();
		}
		
		return flag;
	}
	
	
	public static int saveLocationWise(PlanScheduleVM vm,Long locationId){
		AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		Location location = null;
		List<PlanSchedule> planSchedule = null;
		String[] parts = vm.startDate.split("-");
		String[] parts1 = vm.endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		if(locationId != null){
			location = Location.findById(locationId);
			planSchedule = PlanSchedule.findAllByLocation(locationId);
		}else{
			if(session("USER_ROLE").equals("Manager")){
				location = Location.findById(Long.valueOf(session("USER_LOCATION")));
				planSchedule = PlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
			}
		}
		int flag = 0;
		for(PlanSchedule checkDate:planSchedule){
			
			try {
				if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
					flag = 1;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(flag == 0){
			PlanSchedule plSchedule = new PlanSchedule();
			plSchedule.setCarsSold(vm.carsSold);
			plSchedule.setContractsSign(vm.contractsSign);
			plSchedule.setDayContract(vm.dayContract);
			try {
				plSchedule.setStartDate(df1.parse(startDateString));
				plSchedule.setEndDate(df1.parse(endDateString));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			plSchedule.setMeetingSalesAm(vm.meetingSalesAm);
			plSchedule.setMonthContract(vm.monthContract);
			plSchedule.setQuarterContract(vm.quarterContract);
			plSchedule.setSixMonthContract(vm.sixMonthContract);
			plSchedule.setTotalEarn(vm.totalEarn);
			plSchedule.setTotalMeetingAm(vm.totalMeetingAm);
			plSchedule.setWeekContract(vm.weekContract);
			plSchedule.setWorkWithClient(vm.workWithClient);
			plSchedule.setLocations(location);
			plSchedule.setUser(user);
			plSchedule.save();
		}
		return flag;
	}
	
	
	public static Result getsalesPlan(int salesId){
		AuthUser user = getLocalUser();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	    Date dateobj = new Date();
	    String stringDate = df.format(dateobj);
	    Date DateString = null;
		try {
			DateString = df.parse(stringDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Integer> intList = new ArrayList<>();
		
		List<PlanScheduleVM> pVms = new ArrayList<>();
		//List<SalesPlanSchedule> plSchedule;
		
		List<SalesPlanSchedule> plSchedule = SalesPlanSchedule.findAllByUser(AuthUser.findById(salesId));
		
		if(plSchedule != null){
			for(SalesPlanSchedule ps:plSchedule){
					PlanScheduleVM pVm = new PlanScheduleVM();
				//if(DateString.after(ps.getStartDate()) && DateString.before(ps.getEndDate())){
					pVm.carsSold = ps.carsSold;
					pVm.contractsSign = ps.contractsSign;
					pVm.dayContract = ps.dayContract;
					pVm.endDate = df.format(ps.endDate);
					pVm.startDate = df.format(ps.startDate);
					pVm.id = ps.id;
					pVm.location = ps.locations.id;
					pVm.meetingSalesAm = ps.meetingSalesAm;
					pVm.monthContract = ps.monthContract;
					pVm.quarterContract = ps.quarterContract;
					pVm.sixMonthContract = ps.sixMonthContract;
					pVm.totalEarn = ps.totalEarn;
					pVm.totalMeetingAm = ps.totalMeetingAm;
					pVm.weekContract = ps.weekContract;
					pVm.workWithClient = ps.workWithClient;
					if(session("USER_ROLE").equals("Manager")){
						List<SalesPlanSchedule> pList = SalesPlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
						if(pList != null){
							for(SalesPlanSchedule ps1:pList){
								if(DateString.after(ps1.getStartDate()) && DateString.before(ps1.getEndDate())){
									intList.add(ps1.user.id);
								}
							}
						}
						pVm.salesList = intList;
					}
					pVms.add(pVm);
			//	}
			}
			
		}
		return ok(Json.toJson(pVms));
	}
	
	public static Result saveLocationTotal(String total,Long locationId){
		AuthUser user = getLocalUser();
		PlanLocationTotal planLocat = null;
		if(locationId == 0){
			 planLocat = PlanLocationTotal.findByLocation(Long.valueOf(session("USER_LOCATION")));
		}else{
			planLocat = PlanLocationTotal.findByLocation(locationId);
		}
		
		if(planLocat == null){
			PlanLocationTotal pTotal = new PlanLocationTotal();
			pTotal.setTotal(total);
			pTotal.setUser(user);
			if(locationId == 0){
				pTotal.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			}else{
				pTotal.setLocations(Location.findById(locationId));
			}
			
			pTotal.save();
		}else{
			planLocat.setTotal(total);
			planLocat.setUser(user);
			planLocat.update();
		}
		return ok();
	}
	
	public static Result saveSalePlan(){
		Form<SalepeopleMonthPlanVM> form = DynamicForm.form(SalepeopleMonthPlanVM.class).bindFromRequest();
		SalepeopleMonthPlanVM vm = form.get();
		AuthUser user = getLocalUser();
		
		for(Integer saleId:vm.salesList){
			
			AuthUser uAuthUser = AuthUser.findById(saleId);
			PlanScheduleMonthlySalepeople pSalePer = PlanScheduleMonthlySalepeople.findByUserMonth(uAuthUser,vm.month);
			
			if(pSalePer == null){
				PlanScheduleMonthlySalepeople planMoth = new PlanScheduleMonthlySalepeople();
				planMoth.setCell(vm.cell);
				planMoth.setEmails(vm.emails);
				planMoth.setMonth(vm.month);
				planMoth.setLeadsToGenerate(vm.leadsToGenerate);
				planMoth.setNewCustomers(vm.newCustomers);
				planMoth.setOutofSale(vm.outofSale);
				planMoth.setReturningCustomers(vm.returningCustomers);
				planMoth.setSuccessRate(vm.successRate);
				planMoth.setTestDrives(vm.testDrives);
				planMoth.setTotalBrought(vm.totalBrought);
				planMoth.setVehicalesToSell(vm.vehicalesToSell);
				planMoth.setUser(uAuthUser);
				planMoth.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
				planMoth.save();
			}else{
				pSalePer.setCell(vm.cell);
				pSalePer.setEmails(vm.emails);
				pSalePer.setMonth(vm.month);
				pSalePer.setLeadsToGenerate(vm.leadsToGenerate);
				pSalePer.setNewCustomers(vm.newCustomers);
				pSalePer.setOutofSale(vm.outofSale);
				pSalePer.setReturningCustomers(vm.returningCustomers);
				pSalePer.setSuccessRate(vm.successRate);
				pSalePer.setTestDrives(vm.testDrives);
				pSalePer.setTotalBrought(vm.totalBrought);
				pSalePer.setVehicalesToSell(vm.vehicalesToSell);
				pSalePer.update();
			}
		}
		
		
		
		/*else{
			pLocation.setAvgCheck(vm.avgCheck);
			pLocation.setMinEarning(vm.minEarning);
			//pLocation.setMonth(vm.month);
			pLocation.setTotalEarning(vm.totalEarning);
			pLocation.setVehiclesSell(vm.vehiclesSell);
			pLocation.setUser(user);
			//pLocation.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
			pLocation.update();
		}*/
				
		
		return ok();
	}
	
	public static Result getSalePerson(String month){
		List<PlanScheduleMonthlySalepeople> sMonthlySalepeoples = PlanScheduleMonthlySalepeople.findByListByMonth(month);
		return ok(Json.toJson(sMonthlySalepeoples));
	}
	
	public static Result getSaleMonthlyPlan(Integer saleId){
		List<PlanScheduleMonthlySalepeople> sMonthlySalepeoples = PlanScheduleMonthlySalepeople.findByListUser(AuthUser.findById(saleId));
		return ok(Json.toJson(sMonthlySalepeoples));
	}
	
	public static Result getlocationsMonthlyPlan(Integer userKey){
		AuthUser aUser = AuthUser.findById(userKey);
		List<PlanScheduleMonthlyLocation> pLocation = PlanScheduleMonthlyLocation.findByLocation(aUser.location.id);
		return ok(Json.toJson(pLocation));
	}
	
	public static Result saveSalesTotal(String total,Integer userkey){
		
		AuthUser user = AuthUser.findById(userkey);
		//AuthUser user = getLocalUser();
		PlanSalesTotal planLocat = PlanSalesTotal.findByUser(user);
		if(planLocat == null){
			PlanSalesTotal pTotal = new PlanSalesTotal();
			pTotal.setTotal(total);
			pTotal.setUser(user);
			pTotal.setLocations(Location.findById(user.location.id));
			pTotal.save();
		}else{
			planLocat.setTotal(total);
			planLocat.setUser(user);
			planLocat.update();
		}
		return ok();
	}
	public static Result saveLocationPlan(){
		
		Form<LocationMonthPlanVM> form = DynamicForm.form(LocationMonthPlanVM.class).bindFromRequest();
		LocationMonthPlanVM vm = form.get();
		//AuthUser user = getLocalUser();

		AuthUser user = AuthUser.findById(vm.userkey);
		
		if(user.role.equals("General Manager")){
			
			for(Long lvalue:vm.locationList){
				PlanScheduleMonthlyLocation pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(lvalue),vm.month);
				
				if(pLocation == null){
					PlanScheduleMonthlyLocation planMoth = new PlanScheduleMonthlyLocation();
					planMoth.setAvgCheck(vm.avgCheck);
					planMoth.setMinEarning(vm.minEarning);
					planMoth.setMonth(vm.month);
					planMoth.setTotalEarning(vm.totalEarning);
					planMoth.setVehiclesSell(vm.vehiclesSell);
					planMoth.setUser(user);
					planMoth.setLocations(Location.findById(lvalue));
					planMoth.save();
				}else{
					pLocation.setAvgCheck(vm.avgCheck);
					pLocation.setMinEarning(vm.minEarning);
					pLocation.setTotalEarning(vm.totalEarning);
					pLocation.setVehiclesSell(vm.vehiclesSell);
					pLocation.setUser(user);
					pLocation.update();
				}
			}
			
		}else if(user.role.equals("Manager")){
			PlanScheduleMonthlyLocation pLocation = PlanScheduleMonthlyLocation.findByLocationAndMonth(Location.findById(user.location.id),vm.month);
			
			if(pLocation == null){
				PlanScheduleMonthlyLocation planMoth = new PlanScheduleMonthlyLocation();
				planMoth.setAvgCheck(vm.avgCheck);
				planMoth.setMinEarning(vm.minEarning);
				planMoth.setMonth(vm.month);
				planMoth.setTotalEarning(vm.totalEarning);
				planMoth.setVehiclesSell(vm.vehiclesSell);
				planMoth.setUser(user);
				planMoth.setLocations(Location.findById(user.location.id));
				planMoth.save();
			}else{
				pLocation.setAvgCheck(vm.avgCheck);
				pLocation.setMinEarning(vm.minEarning);
				pLocation.setTotalEarning(vm.totalEarning);
				pLocation.setVehiclesSell(vm.vehiclesSell);
				pLocation.setUser(user);
				pLocation.update();
			}
		}
		
		return ok();
	}
	
	
	public static Result getLocationPlan(Long locationId){
		
		List<PlanScheduleMonthlyLocation> pLocation = PlanScheduleMonthlyLocation.findByLocation(locationId);
		return ok(Json.toJson(pLocation));
	}
	
	public static Result isValidDatecheck(Long finderId, String checkDates, String scheduleBy){
		int flag = 0;
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String[] parts = checkDates.split("-");
		String dateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		
		List<PlanSchedule> planSchedule = null;
		List<SalesPlanSchedule> salesPlanSchedules = null;
		if(scheduleBy.equals("location")){
			if(finderId != 0){
				 planSchedule = PlanSchedule.findAllByLocation(finderId);
			}else{
				 planSchedule = PlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
			}
			for(PlanSchedule checkDate:planSchedule){
				
				try {
					if(df1.parse(dateString).after(checkDate.getStartDate()) && df1.parse(dateString).before(checkDate.getEndDate())){
						flag = 1;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(scheduleBy.equals("salePerson")){
			
			//if(locationId != 0){
				salesPlanSchedules = SalesPlanSchedule.findAllByUser(AuthUser.findById(finderId.intValue()));
			/*}else{
				salesPlanSchedules = SalesPlanSchedule.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
			}*/
			for(SalesPlanSchedule checkDate:salesPlanSchedules){
				
				try {
					if(df1.parse(dateString).after(checkDate.getStartDate()) && df1.parse(dateString).before(checkDate.getEndDate())){
						flag = 1;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
		return ok(Json.toJson(flag));
	}
	
	public static Result isValidCheckbok(Long locationId, String startDate, String endDate){
		int flag = 0;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		String[] parts = startDate.split("-");
		String[] parts1 = endDate.split("-");
		String startDateString = parts[2]+"-"+parts[1]+"-"+parts[0];
		String endDateString = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		
		List<PlanSchedule> planSchedule = PlanSchedule.findAllByLocation(locationId);
		
		for(PlanSchedule checkDate:planSchedule){
			
			try {
				if((df1.parse(startDateString).after(checkDate.getStartDate()) && df1.parse(startDateString).before(checkDate.getEndDate())) || (df1.parse(endDateString).after(checkDate.getStartDate())&& df1.parse(endDateString).before(checkDate.getEndDate()))){
					flag = 1;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return ok(Json.toJson(flag));
	}
	public static Result getUserForMeeting(String date, String time){
		
		List<UserVM> vmList  = new ArrayList<>();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {			
			Date confirmDate = df.parse(date);
			Date confirmTime = new SimpleDateFormat("hh:mm a").parse(time);
			System.out.println(confirmDate);
			System.out.println(confirmTime);
			AuthUser user = getLocalUser();
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(confirmTime);
			calendar.add(Calendar.HOUR, 1);
			Date maxTime = calendar.getTime();
			
			calendar.setTime(confirmTime);
			calendar.add(Calendar.HOUR, -1);
			Date minTime = calendar.getTime();
			System.out.println(minTime);
			System.out.println(maxTime);
			Location loc = Location.findById(Long.parseLong(session("USER_LOCATION")));
			List<AuthUser> userList = AuthUser.findByLocatioUsers(loc);
			for (AuthUser authUser : userList) {
				Boolean flag = true;
				List<ScheduleTest> testList = ScheduleTest.findAllAssigned(authUser);
				for (ScheduleTest scheduleTest : testList) {
					if(confirmDate.equals(scheduleTest.confirmDate)){
						if((confirmTime.after(minTime) || confirmTime.equals(minTime)) && (confirmTime.before(maxTime) || confirmTime.equals(maxTime))){
							flag = false;
							break;
						}
					}
				}
				if(flag){
					UserVM vm = new UserVM();
					vm.id = authUser.getId();
					vm.fullName = authUser.getFirstName()+" "+authUser.getLastName();
					vm.role = authUser.getRole();
					vm.userStatus = "Yes";
					vm.isSelect = false;
					vmList.add(vm);
				}else{
					UserVM vm = new UserVM();
					vm.id = authUser.getId();
					vm.fullName = authUser.getFirstName()+" "+authUser.getLastName();
					vm.role = authUser.getRole();
					vm.userStatus = "Block";
					vm.isSelect = false;
					vmList.add(vm);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok(Json.toJson(vmList));
	}
	
	
	public static Result saveMeetingSchedule(){
		Location loc=null;
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Form<ScheduleTestVM> form = DynamicForm.form(ScheduleTestVM.class).bindFromRequest();
		ScheduleTestVM vm = form.get();
		AuthUser user = getLocalUser();
		if(vm.getLocation()!=null){
			 loc = Location.findById(Long.parseLong(vm.getLocation()));
			}
		List<AuthUser> userList = new ArrayList<>();
		for (UserVM obj : vm.getUsersList()) {
			AuthUser assi = AuthUser.findById(obj.id);
			ScheduleTest moTest = new ScheduleTest();
			moTest.assignedTo = assi;
			moTest.name=vm.getAssignedTo();
			moTest.bestDay = vm.getBestDay();
			moTest.bestTime = vm.getBestTime();
			moTest.email = user.getEmail();
			moTest.location = vm.getLocation();
			moTest.name = vm.name;
			moTest.meetingStatus = "meeting";
			moTest.phone = user.getPhone();
			moTest.reason = vm.getReason();
			moTest.scheduleDate = new Date();
			moTest.user = user;
			moTest.isReassigned = false;
			moTest.is_google_data = false;
			userList.add(assi);
			try {			
				moTest.confirmDate = df.parse(vm.getBestDay());
				moTest.confirmTime = new SimpleDateFormat("hh:mm a").parse(vm.getBestTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			moTest.save();
		}
		
		
		
		
		
		/*if(vm.getLocation()!=null){
			 loc = Location.findById(Long.parseLong(vm.getLocation()));
			}
		if(vm.allStaff.equals(false)){
				AuthUser assi = AuthUser.findById(Integer.parseInt(vm.getAssignedTo()));
				ScheduleTest moTest = new ScheduleTest();
				moTest.assignedTo = assi;
				moTest.name=vm.getAssignedTo();
				moTest.bestDay = vm.getBestDay();
				moTest.bestTime = vm.getBestTime();
				moTest.email = user.getEmail();
				moTest.location = vm.getLocation();
				moTest.name = vm.name;
				moTest.meetingStatus = "meeting";
				moTest.phone = user.getPhone();
				moTest.reason = vm.getReason();
				moTest.scheduleDate = new Date();
				moTest.user = user;
				moTest.isReassigned = false;
				moTest.is_google_data = false;
				userList.add(assi);
				try {			
					moTest.confirmDate = df.parse(vm.getBestDay());
					moTest.confirmTime = new SimpleDateFormat("hh:mm a").parse(vm.getBestTime());
				} catch (Exception e) {
					e.printStackTrace();
				}
				moTest.save();
		}else{
			List<AuthUser> userList1 = AuthUser.findByLocatioUsers(loc);
			for (AuthUser assi : userList1) {
					ScheduleTest moTest = new ScheduleTest();
					moTest.assignedTo = assi;
					moTest.name=vm.getAssignedTo();
					moTest.bestDay = vm.getBestDay();
					moTest.bestTime = vm.getBestTime();
					moTest.email = user.getEmail();
					moTest.location = vm.getLocation();
					moTest.name = vm.name;
					moTest.meetingStatus = "meeting";
					moTest.phone = user.getPhone();
					moTest.reason = vm.getReason();
					moTest.scheduleDate = new Date();
					moTest.user = user;
					moTest.isReassigned = false;
					moTest.is_google_data = false;
					userList.add(assi);
					try {			
						moTest.confirmDate = df.parse(vm.getBestDay());
						moTest.confirmTime = new SimpleDateFormat("hh:mm a").parse(vm.getBestTime());
					} catch (Exception e) {
						e.printStackTrace();
					}
					moTest.save();
			}
		}*/
		
		
		sendMeetingMailToAssignee(vm, user, userList, loc);
		sendMeetingMailToOrgnizer(vm, user, userList, loc);
		return ok();
	}
	
	public static void sendMeetingMailToAssignee(ScheduleTestVM vm, AuthUser user, List<AuthUser> userList, Location loc){
		InternetAddress[] usersArray = new InternetAddress[userList.size()];
		int index = 0;
		for (AuthUser assi : userList) {
			try {
				usersArray[index] = new InternetAddress(assi.getEmail());
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
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
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailUsername));
			message.setRecipients(Message.RecipientType.TO,usersArray);
			message.setSubject("Meeting Scheduled");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/meetingemailtoassign.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("title", vm.name);
	        context.put("location", loc.getName());
	        context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        context.put("date", vm.getBestDay());
	        context.put("time", vm.getBestTime());
	        context.put("disc", vm.getReason());
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("email Succ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void sendMeetingMailToOrgnizer(ScheduleTestVM vm, AuthUser user, List<AuthUser> userList, Location loc){
		String meetingBy = "";
		for (AuthUser assi : userList) {
			meetingBy = meetingBy + assi.getFirstName()+" "+assi.getLastName()+" , ";
		}
		
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
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailUsername));
			message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(user.getEmail()));
			message.setSubject("Meeting Scheduled");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/meetingemailtoorganizer.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("title", vm.name);
	        context.put("location", loc.getName());
	        context.put("meetingBy", meetingBy);
	        context.put("date", vm.getBestDay());
	        context.put("time", vm.getBestTime());
	        context.put("disc", vm.getReason());
	       
	        StringWriter writer = new StringWriter();
	        t.merge( context, writer );
	        String content = writer.toString();
			
			messageBodyPart.setContent(content, "text/html");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("email Succ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Result getLoginUser(){
		AuthUser user = getLocalUser();
		return ok(Json.toJson(user));
	}
	
	
public static Result getFollowerLeads(Long id, String vin,String status, String startDate, String endDate) {
		
		int flagDate = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Map<Long, Long> mapdateFollower = new HashMap<Long, Long>();
		Map<Long,Long> mapAlldate = new HashMap<Long, Long>();
		List<List<Long>> lonn = new ArrayList<>();
		List<sendDateAndValue> sAndValues = new ArrayList<>();
		sendDateAndValue sValue = new sendDateAndValue();
		
		if (startDate.equals("0") || endDate.equals("0")) {
			flagDate = 1;
		}

		if (status.equals("Newly Arrived")) {
		
			Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
			
			List<PriceAlert> pInfo = PriceAlert.getEmailsByVin(vin, Long.parseLong(session("USER_LOCATION")));
			if(vehicle != null){
				
			for(PriceAlert pAlert:pInfo){
				if (vehicle.postedDate.before(pAlert.currDate) || vehicle.postedDate.equals(pAlert.currDate)) {
					Long countCar = 1L;
					String DateString = df.format(pAlert.currDate);
					Date dateDate = null;
					try {
						dateDate = df.parse(DateString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Long objectDate = mapdateFollower.get(dateDate.getTime()+ (1000 * 60 * 60 * 24));
					if (objectDate == null) {
						
						mapdateFollower.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),countCar);

					} else {
						mapdateFollower.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),objectDate + countCar);
					}
				}
			}
		}
			
			
		}else if (status.equals("Sold")) {
			
			Vehicle vehicle = Vehicle.findById(id);
			
			List<PriceAlert> pInfo = PriceAlert.getEmailsByVin(vin, Long.parseLong(session("USER_LOCATION")));
			
			for(PriceAlert pAlert:pInfo){
				
				if ((vehicle.postedDate.before(pAlert.currDate) && vehicle.soldDate
						.after(pAlert.currDate))
						|| vehicle.postedDate
								.equals(pAlert.currDate)
						|| vehicle.soldDate
								.equals(pAlert.currDate)) {
				
					Long countCar = 1L;
					String DateString = df.format(pAlert.currDate);
					Date dateDate = null;
					try {
						dateDate = df.parse(DateString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Long objectDate = mapdateFollower.get(dateDate.getTime()+ (1000 * 60 * 60 * 24));
					if (objectDate == null) {
						
						mapdateFollower.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),countCar);

					} else {
						mapdateFollower.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),objectDate + countCar);
					}
				}
			}
			
		}
		
		for (Entry<Long, Long> entryValue : mapdateFollower.entrySet()) {
			List<Long> value = new ArrayList<>();
			value.add(entryValue.getKey());
			value.add(entryValue.getValue());
			lonn.add(value);
		}
		
		sValue.data = lonn;
		sValue.name = "Follower";
		
		sAndValues.add(sValue);
		
		Vehicle vehicle1 = null;
		Date aajDate = null;
		if (status.equals("Newly Arrived")) {
			vehicle1 = Vehicle.findByVinAndStatus(vin);
			  aajDate = new Date();
		}else if (status.equals("Sold")) {
			vehicle1 = Vehicle.findById(id);
			aajDate = vehicle1.soldDate;
		}	
			int iDate = 2;
			Date addDates = vehicle1.postedDate;
		
			while(iDate > 0){
				Calendar c = Calendar.getInstance(); 
				c.setTime(addDates); 
				
				
				String DateString1 = df
						.format(c.getTime());
				Date dateDate1 = null;
				try {
					dateDate1 = df.parse(DateString1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				c.add(Calendar.DATE, 1);
				
				Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
				if (objectAllDate == null) {
					mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
				}
				
				String DateString2 = df
						.format(aajDate);
				Date dateDate2 = null;
				try {
					dateDate2 = df.parse(DateString2);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				addDates = c.getTime();
				if(addDates.equals(dateDate2)){
					iDate = 0;
					break;
				}
			}
			
		
			for (sendDateAndValue sAndValue : sAndValues) {
				for (List<Long> longs : sAndValue.data) {
					int i = 0;
					for (Long long1 : longs) {
						if (i == 0) {
							for (Entry<Long, Long> entryValue : mapAlldate
									.entrySet()) {
								if (!entryValue.getValue().equals(0L)) {
									if (!long1.equals(entryValue.getKey())) {
										mapAlldate.put(entryValue.getKey(),
												1L);
									} else {
										mapAlldate.put(entryValue.getKey(),
												0L);
									}
								}

							}
							i++;
						}

					}

				}
				for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
					if (entryValue.getValue().equals(1L)) {
						List<Long> value = new ArrayList<>();
						value.add(entryValue.getKey());
						value.add(0L);// entryValue.getKey(),0L};
						sAndValue.data.add(value);

					} else {
						mapAlldate.put(entryValue.getKey(), 1L);
					}
				}

			}
		
		
		for (sendDateAndValue sAndValue : sAndValues) {

			Collections.sort(sAndValue.data,
					new Comparator<List<Long>>() {
						@Override
						public int compare(List<Long> o1, List<Long> o2) {
							return o1.get(0).compareTo(o2.get(0));
						}
					});
		}
		
			return ok(Json.toJson(sAndValues));
	}
	
	
public static Result getviniewsChartLeads(Long id, String vin,
		String status, String startDate, String endDate) {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MMM-dd");
	Map<Long, Long> mapdateView = new HashMap<Long, Long>();
	Map<Long,Long> mapAlldate = new HashMap<Long, Long>();
	List<List<Long>> lonn = new ArrayList<>();
	List<sendDateAndValue> sAndValues = new ArrayList<>();
	sendDateAndValue sValue = new sendDateAndValue();
	
	
	
	String params1 = "&type=visitors-list&date=last-30-days&limit=all";
		int totalTime = 0;
		int flagDate = 1;
		int lagCount = 0;
		int newuserCount = 0;
		int avgDur = 0;
		
		if (startDate.equals("0") || endDate.equals("0")) {
			flagDate = 1;
		}

		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(callClickAPI(params1)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			for(int i=0;i<jsonArray.length();i++){
				
				String checkDate = null;
				Date thedate = null;
				if (status.equals("Newly Arrived")) {
					
					Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
					
						String arr[] = jsonArray.getJSONObject(i).get("time_pretty").toString().split(" ");
						String arrNew[] = arr[3].split(",");
						checkDate = arrNew[0]+"-"+arr[1]+"-"+arr[2];
						try {
							thedate = df1.parse(checkDate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (vehicle.postedDate.before(thedate) || vehicle.postedDate.equals(thedate)) {
							
							if (flagDate == 1) {
								startDate = df.format(thedate);
								endDate = df.format(thedate);
							}
							try {
								if ((thedate.after(df.parse(startDate)) && thedate.before(df.parse(endDate))) || thedate.equals(df.parse(startDate)) || thedate.equals(df.parse(endDate))) {
								
								Long countCar = 1L;
								String data = jsonArray.getJSONObject(i).get("landing_page").toString();
								String arrVin[] = data.split("/");
								if(arrVin.length > 5){
								  if(arrVin[5] != null){
									  if(arrVin[5].equals(vin)){
										  Long objectDate = mapdateView.get(thedate.getTime()+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												mapdateView.put(thedate.getTime()+ (1000 * 60 * 60 * 24),countCar);
											} else {
												mapdateView.put(thedate.getTime()+ (1000 * 60 * 60 * 24),objectDate + countCar);
											}
									}
								  }
								}
}
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}		
				}else if (status.equals("Sold")) {
					Vehicle vehicle = Vehicle.findById(id);
					
					String arr[] = jsonArray.getJSONObject(i).get("time_pretty").toString().split(" ");
					String arrNew[] = arr[3].split(",");
					checkDate = arrNew[0]+"-"+arr[1]+"-"+arr[2];
					try {
						thedate = df1.parse(checkDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if ((vehicle.postedDate.before(thedate) && vehicle.soldDate
							.after(thedate))
							|| vehicle.postedDate
									.equals(thedate)
							|| vehicle.soldDate
									.equals(thedate)) {
						
						if (flagDate == 1) {
							startDate = df.format(thedate);
							endDate = df.format(thedate);
						}
							
						try {
							if ((thedate.after(df.parse(startDate)) && thedate.before(df.parse(endDate))) || thedate.equals(df.parse(startDate)) || thedate.equals(df.parse(endDate))) {
							
							Long countCar = 1L;
							String data = jsonArray.getJSONObject(i).get("landing_page").toString();
							String arrVin[] = data.split("/");
							if(arrVin.length > 5){
							  if(arrVin[5] != null){
								  if(arrVin[5].equals(vin)){
									  Long objectDate = mapdateView.get(thedate.getTime()+ (1000 * 60 * 60 * 24));
										if (objectDate == null) {
											mapdateView.put(thedate.getTime()+ (1000 * 60 * 60 * 24),countCar);
										} else {
											mapdateView.put(thedate.getTime()+ (1000 * 60 * 60 * 24),objectDate + countCar);
										}
								}
							  }
							}
							
							
								}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			
		    			
					}
				}
    			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		for (Entry<Long, Long> entryValue : mapdateView.entrySet()) {
			List<Long> value = new ArrayList<>();
			value.add(entryValue.getKey());
			value.add(entryValue.getValue());
			lonn.add(value);
		}
		
		sValue.data = lonn;
		sValue.name = "Views";
		
		sAndValues.add(sValue);
		
		Vehicle vehicle1 = null;
		Date aajDate = null;
		if (status.equals("Newly Arrived")) {
			vehicle1 = Vehicle.findByVinAndStatus(vin);
			  aajDate = new Date();
		}else if(status.equals("Sold")) {
			 vehicle1 = Vehicle.findById(id);
				aajDate = vehicle1.soldDate;
		}
			int iDate = 2;
			Date addDates = vehicle1.postedDate;
		
			while(iDate > 0){
				Calendar c = Calendar.getInstance(); 
				c.setTime(addDates); 
				
				
				String DateString1 = df
						.format(c.getTime());
				Date dateDate1 = null;
				try {
					dateDate1 = df.parse(DateString1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				c.add(Calendar.DATE, 1);
				
				Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
				if (objectAllDate == null) {
					mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
				}
				
				String DateString2 = df
						.format(aajDate);
				Date dateDate2 = null;
				try {
					dateDate2 = df.parse(DateString2);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				addDates = c.getTime();
				if(addDates.equals(dateDate2)){
					iDate = 0;
					break;
				}
			}
		
		
		for (sendDateAndValue sAndValue : sAndValues) {
			for (List<Long> longs : sAndValue.data) {
				int i = 0;
				for (Long long1 : longs) {
					if (i == 0) {
						for (Entry<Long, Long> entryValue : mapAlldate
								.entrySet()) {
							if (!entryValue.getValue().equals(0L)) {
								if (!long1.equals(entryValue.getKey())) {
									mapAlldate.put(entryValue.getKey(),
											1L);
								} else {
									mapAlldate.put(entryValue.getKey(),
											0L);
								}
							}

						}
						i++;
					}

				}

			}
			for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
				if (entryValue.getValue().equals(1L)) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(0L);// entryValue.getKey(),0L};
					sAndValue.data.add(value);

				} else {
					mapAlldate.put(entryValue.getKey(), 1L);
				}
			}

		}

		
		for (sendDateAndValue sAndValue : sAndValues) {

			Collections.sort(sAndValue.data,
					new Comparator<List<Long>>() {
						@Override
						public int compare(List<Long> o1, List<Long> o2) {
							return o1.get(0).compareTo(o2.get(0));
						}
					});
		}

	
		return ok(Json.toJson(sAndValues));
}

 
	
	public static Result getCustomerRequestLeads(Long id, String vin,
			String status, String startDate, String endDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
		List<sendDateAndValue> sAndValues = new ArrayList<>();
		int flagDate = 0;
		Map<Long, Long> mapdateOffline = new HashMap<Long, Long>();
		Map<Long, Long> mapdateOnline = new HashMap<Long, Long>();
		List<List<Long>> lonOffline = new ArrayList<>();
		List<List<Long>> lonOnline = new ArrayList<>();
		List<PriceChangeVM> pList = new ArrayList<>(); 

		if (startDate.equals("0") || endDate.equals("0")) {
			flagDate = 1;
		}

		if (status.equals("Newly Arrived")) {

			Vehicle vehicle = Vehicle.findByVinAndStatus(vin);

			if (vehicle != null) {
				/*---------------Request More Info-----------------*/
				List<RequestMoreInfo> rInfo = RequestMoreInfo
						.findByVinAndLocation(vin, Location.findById(Long
								.parseLong(session("USER_LOCATION"))));
				sendDateAndValue sValue = new sendDateAndValue();
				sendDateAndValue sValue1 = new sendDateAndValue();
				for (RequestMoreInfo rMoreInfo : rInfo) {
					if (rMoreInfo.onlineOrOfflineLeads != 1) {

						if (vehicle.postedDate.before(rMoreInfo.requestDate)
								|| vehicle.postedDate
										.equals(rMoreInfo.requestDate)) {

							if (flagDate == 1) {
								startDate = df.format(rMoreInfo.requestDate);
								endDate = df.format(rMoreInfo.requestDate);
							}
							try {
								
								Long countCar = 1L;
								String DateString = df
										.format(rMoreInfo.requestDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df
										.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {


									Long objectDate = mapdateOffline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);

									} else {
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else if (rMoreInfo.onlineOrOfflineLeads == 1) {

						if (vehicle.postedDate.before(rMoreInfo.requestDate)
								|| vehicle.postedDate
										.equals(rMoreInfo.requestDate)) {

							if (flagDate == 1) {
								startDate = df.format(rMoreInfo.requestDate);
								endDate = df.format(rMoreInfo.requestDate);
							}
							try {
								
								Long countCar = 1L;
								String DateString = df
										.format(rMoreInfo.requestDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df
										.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {


									Long objectDate = mapdateOnline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);

									} else {
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}

				/*---------------Trade-In Appraisal-----------------*/
				List<TradeIn> tInfo = TradeIn.findByVinAndLocation(vin,
						Location.findById(Long
								.parseLong(session("USER_LOCATION"))));
				for (TradeIn tIn : tInfo) {
					if (tIn.onlineOrOfflineLeads != 1) {
						if (vehicle.postedDate.before(tIn.tradeDate)
								|| vehicle.postedDate.equals(tIn.tradeDate)) {
							if (flagDate == 1) {
								startDate = df.format(tIn.tradeDate);
								endDate = df.format(tIn.tradeDate);
							}
							try {
								
								Long countCar = 1L;
								String DateString = df
										.format(tIn.tradeDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								
								if ((dateDate.after(df.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {
									
									Long objectDate = mapdateOffline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);
									} else {
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else if (tIn.onlineOrOfflineLeads == 1) {
						if (vehicle.postedDate.before(tIn.tradeDate)
								|| vehicle.postedDate.equals(tIn.tradeDate)) {
							if (flagDate == 1) {
								startDate = df.format(tIn.tradeDate);
								endDate = df.format(tIn.tradeDate);
							}
							try {
								
								Long countCar = 1L;
								String DateString = df
										.format(tIn.tradeDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {
									

									Long objectDate = mapdateOnline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);
									} else {
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}

				/*---------------Schedule Test Drive-----------------*/
				List<ScheduleTest> sInfo = ScheduleTest.findByVinAndLocation(
						vin, Location.findById(Long
								.parseLong(session("USER_LOCATION"))));
				for (ScheduleTest sTest : sInfo) {
					if (sTest.onlineOrOfflineLeads != 1) {
						if (vehicle.postedDate.before(sTest.scheduleDate)
								|| vehicle.postedDate
										.equals(sTest.scheduleDate)) {

							if (flagDate == 1) {
								startDate = df.format(sTest.scheduleDate);
								endDate = df.format(sTest.scheduleDate);
							}
							try {
								Long countCar = 1L;
								String DateString = df
										.format(sTest.scheduleDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df
										.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {


									Long objectDate = mapdateOffline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);
									} else {
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else if (sTest.onlineOrOfflineLeads == 1) {
						if (vehicle.postedDate.before(sTest.scheduleDate)
								|| vehicle.postedDate
										.equals(sTest.scheduleDate)) {

							if (flagDate == 1) {
								startDate = df.format(sTest.scheduleDate);
								endDate = df.format(sTest.scheduleDate);
							}
							try {
								Long countCar = 1L;
								String DateString = df
										.format(sTest.scheduleDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df
										.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {

									

									Long objectDate = mapdateOnline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);
									} else {
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}

				for (Entry<Long, Long> entryValue : mapdateOffline.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue());
					lonOffline.add(value);
				}
				sValue.data = lonOffline;
				sValue.name = "Offline";

				sAndValues.add(sValue);

				for (Entry<Long, Long> entryValue : mapdateOnline.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue());
					lonOnline.add(value);
				}
				sValue1.data = lonOnline;
				sValue1.name = "Online";

				sAndValues.add(sValue1);
				
				
				Vehicle vehicle1 = null;
				Date aajDate = null;
				if (status.equals("Newly Arrived")) {
					vehicle1 = Vehicle.findByVinAndStatus(vin);
					  aajDate = new Date();
				}
					int iDate = 2;
					Date addDates = vehicle1.postedDate;
				
					while(iDate > 0){
						Calendar c = Calendar.getInstance(); 
						c.setTime(addDates); 
						
						
						String DateString1 = df
								.format(c.getTime());
						Date dateDate1 = null;
						try {
							dateDate1 = df.parse(DateString1);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						c.add(Calendar.DATE, 1);
						Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
						if (objectAllDate == null) {
							mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
						}
						
						String DateString2 = df
								.format(aajDate);
						Date dateDate2 = null;
						try {
							dateDate2 = df.parse(DateString2);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						addDates = c.getTime();
						if(addDates.equals(dateDate2)){
							iDate = 0;
							break;
						}
					}
				
				

				for (sendDateAndValue sAndValue : sAndValues) {
					for (List<Long> longs : sAndValue.data) {
						int i = 0;
						for (Long long1 : longs) {
							if (i == 0) {
								for (Entry<Long, Long> entryValue : mapAlldate
										.entrySet()) {
									if (!entryValue.getValue().equals(0L)) {
										if (!long1.equals(entryValue.getKey())) {
											mapAlldate.put(entryValue.getKey(),
													1L);
										} else {
											mapAlldate.put(entryValue.getKey(),
													0L);
										}
									}

								}
								i++;
							}

						}

					}
					for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
						if (entryValue.getValue().equals(1L)) {
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(0L);// entryValue.getKey(),0L};
							sAndValue.data.add(value);

						} else {
							mapAlldate.put(entryValue.getKey(), 1L);
						}
					}

				}

				for (sendDateAndValue sAndValue : sAndValues) {

					Collections.sort(sAndValue.data,
							new Comparator<List<Long>>() {
								@Override
								public int compare(List<Long> o1, List<Long> o2) {
									return o1.get(0).compareTo(o2.get(0));
								}

							});
				}

				
			}
		} else if (status.equals("Sold")) {

			Vehicle vehicle = Vehicle.findById(id);

			if (vehicle != null) {
				/*---------------Request More Info-----------------*/
				List<RequestMoreInfo> rInfo = RequestMoreInfo
						.findByVinSoldAndLocation(vin, Location.findById(Long
								.parseLong(session("USER_LOCATION"))));
				sendDateAndValue sValue = new sendDateAndValue();
				sendDateAndValue sValue1 = new sendDateAndValue();
				for (RequestMoreInfo rMoreInfo : rInfo) {
					if (rMoreInfo.onlineOrOfflineLeads != 1) {
						if ((vehicle.postedDate.before(rMoreInfo.requestDate) && vehicle.soldDate
								.after(rMoreInfo.requestDate))
								|| vehicle.postedDate
										.equals(rMoreInfo.requestDate)
								|| vehicle.soldDate
										.equals(rMoreInfo.requestDate)) {

							if (flagDate == 1) {
								startDate = df.format(rMoreInfo.requestDate);
								endDate = df.format(rMoreInfo.requestDate);
							}
							try {
								
								Long countCar = 1L;
								String DateString = df
										.format(rMoreInfo.requestDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df
										.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {

									

									Long objectDate = mapdateOffline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);

									} else {
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else if (rMoreInfo.onlineOrOfflineLeads == 1) {
						if ((vehicle.postedDate.before(rMoreInfo.requestDate) && vehicle.soldDate
								.after(rMoreInfo.requestDate))
								|| vehicle.postedDate
										.equals(rMoreInfo.requestDate)
								|| vehicle.soldDate
										.equals(rMoreInfo.requestDate)) {

							if (flagDate == 1) {
								startDate = df.format(rMoreInfo.requestDate);
								endDate = df.format(rMoreInfo.requestDate);
							}
							try {
								
								Long countCar = 1L;
								String DateString = df
										.format(rMoreInfo.requestDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df
										.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {


									Long objectDate = mapdateOnline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);

									} else {
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}

				/*---------------Trade-In Appraisal-----------------*/
				List<TradeIn> tInfo = TradeIn.findByVinSoldAndLocation(vin,
						Location.findById(Long
								.parseLong(session("USER_LOCATION"))));

				for (TradeIn tIn : tInfo) {
					if (tIn.onlineOrOfflineLeads != 1) {
						if ((vehicle.postedDate.before(tIn.tradeDate) && vehicle.soldDate
								.after(tIn.tradeDate))
								|| vehicle.postedDate.equals(tIn.tradeDate)
								|| vehicle.soldDate.equals(tIn.tradeDate)) {
							if (flagDate == 1) {
								startDate = df.format(tIn.tradeDate);
								endDate = df.format(tIn.tradeDate);
							}
							try {
								
								Long countCar = 1L;
								String DateString = df
										.format(tIn.tradeDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {
									

									Long objectDate = mapdateOffline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);
									} else {
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else if (tIn.onlineOrOfflineLeads != 1) {
						if ((vehicle.postedDate.before(tIn.tradeDate) && vehicle.soldDate
								.after(tIn.tradeDate))
								|| vehicle.postedDate.equals(tIn.tradeDate)
								|| vehicle.soldDate.equals(tIn.tradeDate)) {
							if (flagDate == 1) {
								startDate = df.format(tIn.tradeDate);
								endDate = df.format(tIn.tradeDate);
							}
							try {
								Long countCar = 1L;
								String DateString = df
										.format(tIn.tradeDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {
									

									Long objectDate = mapdateOnline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);
									} else {
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}

				/*---------------Schedule Test Drive-----------------*/
				List<ScheduleTest> sInfo = ScheduleTest
						.findByVinSoldAndLocation(vin, Location.findById(Long
								.parseLong(session("USER_LOCATION"))));
				for (ScheduleTest sTest : sInfo) {
					if (sTest.onlineOrOfflineLeads != 1) {
						if ((vehicle.postedDate.before(sTest.scheduleDate) && vehicle.soldDate
								.after(sTest.scheduleDate))
								|| vehicle.postedDate
										.equals(sTest.scheduleDate)
								|| vehicle.soldDate.equals(sTest.scheduleDate)) {

							if (flagDate == 1) {
								startDate = df.format(sTest.scheduleDate);
								endDate = df.format(sTest.scheduleDate);
							}
							try {
								
								Long countCar = 1L;
								String DateString = df
										.format(sTest.scheduleDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((dateDate.after(df
										.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {


									Long objectDate = mapdateOffline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);
									} else {
										mapdateOffline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else if (sTest.onlineOrOfflineLeads != 1) {
						if ((vehicle.postedDate.before(sTest.scheduleDate) && vehicle.soldDate
								.after(sTest.scheduleDate))
								|| vehicle.postedDate
										.equals(sTest.scheduleDate)
								|| vehicle.soldDate.equals(sTest.scheduleDate)) {

							if (flagDate == 1) {
								startDate = df.format(sTest.scheduleDate);
								endDate = df.format(sTest.scheduleDate);
							}
							try {
								
								Long countCar = 1L;
								String DateString = df
										.format(sTest.scheduleDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								
								if ((dateDate.after(df
										.parse(startDate)) && dateDate
										.before(df.parse(endDate)))
										|| dateDate.equals(df
												.parse(startDate))
										|| dateDate.equals(df
												.parse(endDate))) {

									
									Long objectDate = mapdateOnline
											.get(dateDate.getTime()
													+ (1000 * 60 * 60 * 24));
									if (objectDate == null) {
										Long objectAllDate = mapAlldate
												.get(dateDate.getTime()
														+ (1000 * 60 * 60 * 24));
										if (objectAllDate == null) {
											mapAlldate
													.put(dateDate.getTime()
															+ (1000 * 60 * 60 * 24),
															1L);
										}
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												countCar);
									} else {
										mapdateOnline.put(dateDate.getTime()
												+ (1000 * 60 * 60 * 24),
												objectDate + countCar);
									}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}

				for (Entry<Long, Long> entryValue : mapdateOffline.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue());
					lonOffline.add(value);
				}
				sValue.data = lonOffline;
				sValue.name = "Offline";

				sAndValues.add(sValue);

				for (Entry<Long, Long> entryValue : mapdateOnline.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue());
					lonOnline.add(value);
				}
				sValue1.data = lonOnline;
				sValue1.name = "Online";

				sAndValues.add(sValue1);
				
				
				
				Vehicle vehicle1 = null;
				Date aajDate = null;
				if(status.equals("Sold")) {
					 vehicle1 = Vehicle.findById(id);
						aajDate = vehicle1.soldDate;
				}
					int iDate = 2;
					Date addDates = vehicle1.postedDate;
				
					while(iDate > 0){
						Calendar c = Calendar.getInstance(); 
						c.setTime(addDates); 
						
						
						String DateString1 = df
								.format(c.getTime());
						Date dateDate1 = null;
						try {
							dateDate1 = df.parse(DateString1);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}
						
						c.add(Calendar.DATE, 1);
						
						Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
						if (objectAllDate == null) {
							mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
						}
						
						String DateString2 = df
								.format(aajDate);
						Date dateDate2 = null;
						try {
							dateDate2 = df.parse(DateString2);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						addDates = c.getTime();
						if(addDates.equals(dateDate2)){
							iDate = 0;
							break;
						}
					}
				
				

				for (sendDateAndValue sAndValue : sAndValues) {
					for (List<Long> longs : sAndValue.data) {
						int i = 0;
						for (Long long1 : longs) {
							if (i == 0) {
								for (Entry<Long, Long> entryValue : mapAlldate
										.entrySet()) {
									if (!entryValue.getValue().equals(0L)) {
										if (!long1.equals(entryValue.getKey())) {
											mapAlldate.put(entryValue.getKey(),
													1L);
										} else {
											mapAlldate.put(entryValue.getKey(),
													0L);
										}
									}

								}
								i++;
							}

						}

					}
					for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
						if (entryValue.getValue().equals(1L)) {
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(0L);// entryValue.getKey(),0L};
							sAndValue.data.add(value);

						} else {
							mapAlldate.put(entryValue.getKey(), 1L);
						}
					}

				}

				for (sendDateAndValue sAndValue : sAndValues) {

					Collections.sort(sAndValue.data,
							new Comparator<List<Long>>() {
								@Override
								public int compare(List<Long> o1, List<Long> o2) {
									return o1.get(0).compareTo(o2.get(0));
								}

							});
				}
				
				List<PriceChange> pChange = PriceChange.findByVin(vin);
				
				PriceChangeVM pVm1 = new PriceChangeVM();
				pVm1.person = "Has been added to the invnetory";
				pVm1.dateTime =vehicle.postedDate.toString();
				pList.add(pVm1);
				
				for(PriceChange pChg:pChange){
					String dateChanege = df.format(pChg.getDateTime());
					Date changDate = null;
					try {
						changDate = df.parse(dateChanege);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (vehicle.postedDate.before(changDate) && vehicle.soldDate.after(changDate)){
						PriceChangeVM pVm = new PriceChangeVM();
						pVm.person = "Price Change"+pChg.price;
						pVm.dateTime = df.format(pChg.dateTime);
						pList.add(pVm);
					}
				}
				
			}
		}
	//	Map map = new HashMap();
	//	map.put("sAndValues",sAndValues);
		//map.put("pList",pList);

		return ok(Json.toJson(sAndValues));
	}
	
	public static Result getPlanTarget(String type){
		
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
		        "August", "September", "October", "November", "December" };
   	
		Calendar cal = Calendar.getInstance();  
    	String monthCal = monthName[cal.get(Calendar.MONTH)];
    	SetPriceChangeFlag sPrice = new SetPriceChangeFlag();
	 	List<PriceFormatDate> sAndValues = new ArrayList<>();
	 	PriceFormatDate pvalue = new PriceFormatDate();
		
		if(type.equalsIgnoreCase("location")){
			Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
			PlanScheduleMonthlyLocation loc = PlanScheduleMonthlyLocation.findByLocationAndMonth(location,monthCal);
			if(loc !=null){
				pvalue.price = Long.parseLong(loc.totalEarning);
			}else{
				pvalue.price = 0l;
			}
			//pvalue.x = 0l;
			pvalue.title = "Plan Target";
			pvalue.text = "Plan "+ pvalue.price;
			sAndValues.add(pvalue);
			//sPrice.y = 100000;
			//sPrice.type = "flags";
			sPrice.data = sAndValues;
			sPrice.name = "Plan Target";
			return ok(Json.toJson(sPrice));
		}else{
			AuthUser user = getLocalUser();
			PlanScheduleMonthlySalepeople obj = PlanScheduleMonthlySalepeople.findByUserMonth(user, monthCal);
			if(obj !=null){
				pvalue.price = Long.parseLong(obj.totalBrought);
			}else{
				pvalue.price = 0l;
			}
			
			pvalue.title = "Plan Target";
			pvalue.text = "Plan "+ pvalue.price;
			sAndValues.add(pvalue);
			sPrice.data = sAndValues;
			sPrice.name = "Plan Target";
			return ok(Json.toJson(sPrice));
		}	
	}
	
	
	
	 public static Result getCustomerRequestFlag(Long id, String vin,String status, String startDate, String endDate) {
		 	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 	Map<Long, Long> mapdateFlag = new HashMap<Long, Long>();
		 	SetPriceChangeFlag sPrice = new SetPriceChangeFlag();
		 	List<PriceFormatDate> sAndValues = new ArrayList<>();
		 	int flagDate = 0;
			//List<List<Long>> lonFlag = new ArrayList<>();
			if(startDate.equals("0") || endDate.equals("0")){
				flagDate = 1;
			}
			
			if(status.equals("Newly Arrived")){
				Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
				
				if(vehicle != null){
					sendDateAndValue sValue3 = new sendDateAndValue();
					
					List<PriceChange> pChanges = PriceChange.findByVin(vin);
					for(PriceChange pCh:pChanges){
						Long countCar = 1L;
						String DateString = df.format(pCh.dateTime);
						Date dateDate = null;
						try {
							dateDate = df.parse(DateString);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(vehicle.postedDate.before(dateDate) || vehicle.postedDate.equals(dateDate)){
							
							if(flagDate == 1){
								startDate = df.format(dateDate);
								endDate = df.format(dateDate);
							}
							try {
								if((dateDate.after(df.parse(startDate)) && dateDate.before(df.parse(endDate))) || dateDate.equals(df.parse(startDate)) || dateDate.equals(df.parse(endDate))){
							
										Long objectDate = mapdateFlag.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
										if (objectDate == null) {
											//Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
											//if(objectAllDate == null){
												//mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
											//}
											mapdateFlag.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), Long.parseLong(pCh.price));
										}else{
											mapdateFlag.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),Long.parseLong(pCh.price));
										}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
					}
					
					
					for (Entry<Long, Long> entryValue : mapdateFlag.entrySet()) {
						PriceFormatDate pvalue = new PriceFormatDate();
						pvalue.x = entryValue.getKey();
						pvalue.title = "Price Change";
						pvalue.text = "Price has changed to "+entryValue.getValue();
						sAndValues.add(pvalue);
					  }
					/*for (Entry<Long, Long> entryValue : mapdateFlag.entrySet()) {
						List<Long> value = new ArrayList<>();
						value.add(entryValue.getKey());
						value.add(entryValue.getValue()); 
						lonFlag.add(value);
					  }*/
					
					sPrice.y = -220;
					sPrice.type = "flags";
					sPrice.data = sAndValues;
					sPrice.name = "Price Changes";
				
				}
			}else if(status.equals("Sold")){
				Vehicle vehicle = Vehicle.findById(id);
				
				if(vehicle != null){
					sendDateAndValue sValue3 = new sendDateAndValue();
					
					List<PriceChange> pChanges = PriceChange.findByVin(vin);
					for(PriceChange pCh:pChanges){
						Long countCar = 1L;
						String DateString = df.format(pCh.dateTime);
						Date dateDate = null;
						try {
							dateDate = df.parse(DateString);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(vehicle.postedDate.before(dateDate) || vehicle.postedDate.equals(dateDate)){
							
							if(flagDate == 1){
								startDate = df.format(dateDate);
								endDate = df.format(dateDate);
							}
							try {
								if((dateDate.after(df.parse(startDate)) && dateDate.before(df.parse(endDate))) || dateDate.equals(df.parse(startDate)) || dateDate.equals(df.parse(endDate))){
							
										Long objectDate = mapdateFlag.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
										if (objectDate == null) {
											//Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
											//if(objectAllDate == null){
												//mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
											//}
											mapdateFlag.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), Long.parseLong(pCh.price));
										}else{
											mapdateFlag.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),Long.parseLong(pCh.price));
										}
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
					}
					
					
					for (Entry<Long, Long> entryValue : mapdateFlag.entrySet()) {
						PriceFormatDate pvalue = new PriceFormatDate();
						pvalue.x = entryValue.getKey();
						pvalue.title = "Price Change";
						pvalue.text = "Price has changed to "+entryValue.getValue();
						sAndValues.add(pvalue);
					  }
					/*for (Entry<Long, Long> entryValue : mapdateFlag.entrySet()) {
						List<Long> value = new ArrayList<>();
						value.add(entryValue.getKey());
						value.add(entryValue.getValue()); 
						lonFlag.add(value);
					  }*/
					
					sPrice.y = -220;
					sPrice.type = "flags";
					sPrice.data = sAndValues;
					sPrice.name = "Price Changes";
				
				}
			}
			
			return ok(Json.toJson(sPrice));
	  }
	
	public static Result getCustomerRequest(Long id,String vin,String status, String startDate, String endDate){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
		List<sendDateAndValue> sAndValues = new ArrayList<>();
		int flagDate = 0;
		Map<Long, Long> mapdateRequest = new HashMap<Long, Long>();
		Map<Long, Long> mapdateTrade = new HashMap<Long, Long>();
		Map<Long, Long> mapdateSchedule = new HashMap<Long, Long>();
		
		List<List<Long>> lonRequest = new ArrayList<>();
		List<List<Long>> lonTrade = new ArrayList<>();
		List<List<Long>> lonSchedule = new ArrayList<>();
		List<List<Long>> lonFlag = new ArrayList<>();
		if(startDate.equals("0") || endDate.equals("0")){
			flagDate = 1;
		}
		
		if(status.equals("Newly Arrived")){
			
			Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
			
				if(vehicle != null){
			/*---------------Request More Info-----------------*/
			List<RequestMoreInfo> rInfo = RequestMoreInfo.findByVinAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
			sendDateAndValue sValue = new sendDateAndValue();
			for(RequestMoreInfo rMoreInfo:rInfo){
			  /*if((vehicle.postedDate.before(rMoreInfo.requestDate) && rMoreInfo.requestDate.before(vehicle.soldDate)) || vehicle.postedDate.equals(rMoreInfo.requestDate) || rMoreInfo.requestDate.equals(vehicle.soldDate)){*/
				if(vehicle.postedDate.before(rMoreInfo.requestDate) || vehicle.postedDate.equals(rMoreInfo.requestDate)){
				
					if(flagDate == 1){
						startDate = df.format(rMoreInfo.requestDate);
						endDate = df.format(rMoreInfo.requestDate);
					}
					try {
						if((rMoreInfo.requestDate.after(df.parse(startDate)) && rMoreInfo.requestDate.before(df.parse(endDate))) || rMoreInfo.requestDate.equals(df.parse(startDate)) || rMoreInfo.requestDate.equals(df.parse(endDate))){
							
							Long countCar = 1L;
							String DateString = df.format(rMoreInfo.requestDate);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Long objectDate = mapdateRequest.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
							if (objectDate == null) {
								Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if(objectAllDate == null){
									mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
								}
								mapdateRequest.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
								
							}else{
								mapdateRequest.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
							}
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 }	
			}
			
			for (Entry<Long, Long> entryValue : mapdateRequest.entrySet()) {
				List<Long> value = new ArrayList<>();
				value.add(entryValue.getKey());
				value.add(entryValue.getValue()); 
				lonRequest.add(value);
			  }
			sValue.data = lonRequest;
			sValue.name = "Request More Info";
		
			sAndValues.add(sValue);
			
		
			/*---------------Trade-In Appraisal-----------------*/
			List<TradeIn> tInfo = TradeIn.findByVinAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
			sendDateAndValue sValue1 = new sendDateAndValue();
			for(TradeIn tIn:tInfo){
				if(vehicle.postedDate.before(tIn.tradeDate) || vehicle.postedDate.equals(tIn.tradeDate)){
					if(flagDate == 1){
						startDate = df.format(tIn.tradeDate);
						endDate = df.format(tIn.tradeDate);
					}
					try {
						if((tIn.tradeDate.after(df.parse(startDate)) && tIn.tradeDate.before(df.parse(endDate))) || tIn.tradeDate.equals(df.parse(startDate)) || tIn.tradeDate.equals(df.parse(endDate))){
							Long countCar = 1L;
							String DateString = df.format(tIn.tradeDate);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Long objectDate = mapdateTrade.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
							if (objectDate == null) {
								Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if(objectAllDate == null){
									mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
								}
								mapdateTrade.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
							}else{
								mapdateTrade.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
							}
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 }
			}
			
			for (Entry<Long, Long> entryValue : mapdateTrade.entrySet()) {
				List<Long> value = new ArrayList<>();
				value.add(entryValue.getKey());
				value.add(entryValue.getValue()); 
				lonTrade.add(value);
			  }
			sValue1.data = lonTrade;
			sValue1.name = "Trade-In Appraisal";
		
			sAndValues.add(sValue1);
			
			/*---------------Schedule Test Drive-----------------*/
			List<ScheduleTest> sInfo = ScheduleTest.findByVinAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
			sendDateAndValue sValue2 = new sendDateAndValue();
			for(ScheduleTest sTest:sInfo){
				if(vehicle.postedDate.before(sTest.scheduleDate) || vehicle.postedDate.equals(sTest.scheduleDate)){
					
					if(flagDate == 1){
						startDate = df.format(sTest.scheduleDate);
						endDate = df.format(sTest.scheduleDate);
					}
					try {
						if((sTest.scheduleDate.after(df.parse(startDate)) && sTest.scheduleDate.before(df.parse(endDate))) || sTest.scheduleDate.equals(df.parse(startDate)) || sTest.scheduleDate.equals(df.parse(endDate))){
					
							Long countCar = 1L;
							String DateString = df.format(sTest.scheduleDate);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Long objectDate = mapdateSchedule.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
							if (objectDate == null) {
								Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if(objectAllDate == null){
									mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
								}
								mapdateSchedule.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
							}else{
								mapdateSchedule.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
							}
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}	 
			}
			
			for (Entry<Long, Long> entryValue : mapdateSchedule.entrySet()) {
				List<Long> value = new ArrayList<>();
				value.add(entryValue.getKey());
				value.add(entryValue.getValue()); 
				lonSchedule.add(value);
			  }
			sValue2.data = lonSchedule;
			sValue2.name = "Schedule Test Drive";
		
			sAndValues.add(sValue2);
			
			
			Vehicle vehicle1 = null;
			Date aajDate = null;
			if (status.equals("Newly Arrived")) {
				vehicle1 = Vehicle.findByVinAndStatus(vin);
				  aajDate = new Date();
			}
				int iDate = 2;
				Date addDates = vehicle1.postedDate;
			
				while(iDate > 0){
					Calendar c = Calendar.getInstance(); 
					c.setTime(addDates); 
					
					String DateString1 = df
							.format(c.getTime());
					Date dateDate1 = null;
					try {
						dateDate1 = df.parse(DateString1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c.add(Calendar.DATE, 1);
					
					Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
					if (objectAllDate == null) {
						mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
					}
					
					String DateString2 = df
							.format(aajDate);
					Date dateDate2 = null;
					try {
						dateDate2 = df.parse(DateString2);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					addDates = c.getTime();
					if(addDates.equals(dateDate2)){
						iDate = 0;
						break;
					}
				}
				
				
			
			
			for(sendDateAndValue sAndValue:sAndValues){
				for(List<Long> longs:sAndValue.data){
					int i = 0;
					for(Long long1:longs){
						if(i == 0){
							for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
								if(!entryValue.getValue().equals(0L)){
									if(!long1.equals(entryValue.getKey())){
										mapAlldate.put(entryValue.getKey(), 1L);
									}else{
										mapAlldate.put(entryValue.getKey(), 0L);
									}
								}
							  }
							i++;
						}
					}
				}
				for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
					if(entryValue.getValue().equals(1L)){
						List<Long> value = new ArrayList<>();
						value.add(entryValue.getKey());
						value.add(0L);//entryValue.getKey(),0L};
						sAndValue.data.add(value);
					}else{
						mapAlldate.put(entryValue.getKey(), 1L);
					}
				  }
			}
			
			

			
			for(sendDateAndValue sAndValue:sAndValues){
				
				Collections.sort(sAndValue.data, new Comparator<List<Long>>(){
					 @Override
			            public int compare(List<Long> o1, List<Long> o2) {
			                return o1.get(0).compareTo(o2.get(0));
			            }
					
				});
			}
		 }	
		}else if(status.equals("Sold")){
			
			Vehicle vehicle = Vehicle.findById(id);
			
			if(vehicle != null){
			/*---------------Request More Info-----------------*/
			List<RequestMoreInfo> rInfo = RequestMoreInfo.findByVinSoldAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
			sendDateAndValue sValue = new sendDateAndValue();
			for(RequestMoreInfo rMoreInfo:rInfo){
				if((vehicle.postedDate.before(rMoreInfo.requestDate) && vehicle.soldDate.after(rMoreInfo.requestDate)) || vehicle.postedDate.equals(rMoreInfo.requestDate) || vehicle.soldDate.equals(rMoreInfo.requestDate)){
				
					if(flagDate == 1){
						startDate = df.format(rMoreInfo.requestDate);
						endDate = df.format(rMoreInfo.requestDate);
					}
					try {
						if((rMoreInfo.requestDate.after(df.parse(startDate)) && rMoreInfo.requestDate.before(df.parse(endDate))) || rMoreInfo.requestDate.equals(df.parse(startDate)) || rMoreInfo.requestDate.equals(df.parse(endDate))){
							
							Long countCar = 1L;
							String DateString = df.format(rMoreInfo.requestDate);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Long objectDate = mapdateRequest.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
							if (objectDate == null) {
								Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if(objectAllDate == null){
									mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
								}
								mapdateRequest.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
								
							}else{
								mapdateRequest.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
							}
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 }	
			}
			
			for (Entry<Long, Long> entryValue : mapdateRequest.entrySet()) {
				List<Long> value = new ArrayList<>();
				value.add(entryValue.getKey());
				value.add(entryValue.getValue()); 
				lonRequest.add(value);
			  }
			sValue.data = lonRequest;
			sValue.name = "Request More Info";
		
			sAndValues.add(sValue);
			
		
			/*---------------Trade-In Appraisal-----------------*/
			List<TradeIn> tInfo = TradeIn.findByVinSoldAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
			sendDateAndValue sValue1 = new sendDateAndValue();
			for(TradeIn tIn:tInfo){
				if((vehicle.postedDate.before(tIn.tradeDate) && vehicle.soldDate.after(tIn.tradeDate)) || vehicle.postedDate.equals(tIn.tradeDate) || vehicle.soldDate.equals(tIn.tradeDate)){
					if(flagDate == 1){
						startDate = df.format(tIn.tradeDate);
						endDate = df.format(tIn.tradeDate);
					}
					try {
						if((tIn.tradeDate.after(df.parse(startDate)) && tIn.tradeDate.before(df.parse(endDate))) || tIn.tradeDate.equals(df.parse(startDate)) || tIn.tradeDate.equals(df.parse(endDate))){
							Long countCar = 1L;
							String DateString = df.format(tIn.tradeDate);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Long objectDate = mapdateTrade.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
							if (objectDate == null) {
								Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if(objectAllDate == null){
									mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
								}
								mapdateTrade.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
							}else{
								mapdateTrade.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
							}
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			 }
			}
			
			for (Entry<Long, Long> entryValue : mapdateTrade.entrySet()) {
				List<Long> value = new ArrayList<>();
				value.add(entryValue.getKey());
				value.add(entryValue.getValue()); 
				lonTrade.add(value);
			  }
			sValue1.data = lonTrade;
			sValue1.name = "Trade-In Appraisal";
		
			sAndValues.add(sValue1);
			
			/*---------------Schedule Test Drive-----------------*/
			List<ScheduleTest> sInfo = ScheduleTest.findByVinSoldAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
			sendDateAndValue sValue2 = new sendDateAndValue();
			for(ScheduleTest sTest:sInfo){
				if((vehicle.postedDate.before(sTest.scheduleDate) && vehicle.soldDate.after(sTest.scheduleDate)) || vehicle.postedDate.equals(sTest.scheduleDate) || vehicle.soldDate.equals(sTest.scheduleDate)){
					
					if(flagDate == 1){
						startDate = df.format(sTest.scheduleDate);
						endDate = df.format(sTest.scheduleDate);
					}
					try {
						if((sTest.scheduleDate.after(df.parse(startDate)) && sTest.scheduleDate.before(df.parse(endDate))) || sTest.scheduleDate.equals(df.parse(startDate)) || sTest.scheduleDate.equals(df.parse(endDate))){
					
							Long countCar = 1L;
							String DateString = df.format(sTest.scheduleDate);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Long objectDate = mapdateSchedule.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
							if (objectDate == null) {
								Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if(objectAllDate == null){
									mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
								}
								mapdateSchedule.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
							}else{
								mapdateSchedule.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
							}
						}
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}	 
			}
			
			for (Entry<Long, Long> entryValue : mapdateSchedule.entrySet()) {
				List<Long> value = new ArrayList<>();
				value.add(entryValue.getKey());
				value.add(entryValue.getValue()); 
				lonSchedule.add(value);
			  }
			sValue2.data = lonSchedule;
			sValue2.name = "Schedule Test Drive";
		
			sAndValues.add(sValue2);
			
			
			Vehicle vehicle1 = null;
			Date aajDate = null;
			if(status.equals("Sold")) {
				 vehicle1 = Vehicle.findById(id);
					aajDate = vehicle1.soldDate;
			}
				int iDate = 2;
				Date addDates = vehicle1.postedDate;
			
				while(iDate > 0){
					Calendar c = Calendar.getInstance(); 
					c.setTime(addDates); 
					
					String DateString1 = df
							.format(c.getTime());
					Date dateDate1 = null;
					try {
						dateDate1 = df.parse(DateString1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c.add(Calendar.DATE, 1);
					
					Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
					if (objectAllDate == null) {
						mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
					}
					
					String DateString2 = df
							.format(aajDate);
					Date dateDate2 = null;
					try {
						dateDate2 = df.parse(DateString2);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					addDates = c.getTime();
					if(addDates.equals(dateDate2)){
						iDate = 0;
						break;
					}
				}
			
			
			for(sendDateAndValue sAndValue:sAndValues){
				for(List<Long> longs:sAndValue.data){
					int i = 0;
					for(Long long1:longs){
						if(i == 0){
							for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
								if(!entryValue.getValue().equals(0L)){
									if(!long1.equals(entryValue.getKey())){
										mapAlldate.put(entryValue.getKey(), 1L);
									}else{
										mapAlldate.put(entryValue.getKey(), 0L);
									}
								}
								
							  }
							i++;
						}
						
					}
					
				}
				for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
					if(entryValue.getValue().equals(1L)){
						List<Long> value = new ArrayList<>();
						value.add(entryValue.getKey());
						value.add(0L);//entryValue.getKey(),0L};
						sAndValue.data.add(value);
						
					}else{
						mapAlldate.put(entryValue.getKey(), 1L);
					}
				  }
				
			}
			
			for(sendDateAndValue sAndValue:sAndValues){
				
				Collections.sort(sAndValue.data, new Comparator<List<Long>>(){
					 @Override
			            public int compare(List<Long> o1, List<Long> o2) {
			                return o1.get(0).compareTo(o2.get(0));
			            }
					
				});
			}
		}	
		}
		
		return ok(Json.toJson(sAndValues));
	}
	
	
	/*public static Result getFinancialVehicleDetailsByBodyStyleOther(Long locationId,Integer managerId){
		AuthUser user = AuthUser.findById(managerId);
		List<sendDateAndValue> sAndValues = new ArrayList<>();
		FinancialVehicleDetails(user,sAndValues);
		return ok(Json.toJson(sAndValues));
	}*/
	
	public static Result getFinancialVehicleDetailsByBodyStyle(String startDate,String enddate){
		AuthUser user = getLocalUser();
		
		List<sendDateAndValue> sAndValues = new ArrayList<>();
		FinancialVehicleDetails(user,sAndValues,startDate,enddate);
		return ok(Json.toJson(sAndValues));
	}
	
	public static void FinancialVehicleDetails(AuthUser user, List<sendDateAndValue> sAndValues,String startD,String endD){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Vehicle> vehicle = null;
		Map<String, Long> mapBodyStyle = new HashMap<String, Long>();
		//Map<Long, Long> mapdate = new HashMap<Long, Long>();
		
		Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
		if(user.role.equals("General Manager")){
			vehicle = Vehicle.findBySold();
		}else if(user.role.equals("Manager")){
			vehicle = Vehicle.findByLocationAndSold(user.location.id);
		}else if(user.role.equals("Sales Person")){
			vehicle = Vehicle.findBySoldUserAndSold(user);
		}
		
		Long countvehical = 1L;
		
		for(Vehicle vhVehicle:vehicle){
			if(vhVehicle.getBodyStyle() != null){
				
				Long objectMake = mapBodyStyle.get(vhVehicle.getBodyStyle());
				if (objectMake == null) {
					mapBodyStyle.put(vhVehicle.getBodyStyle(), countvehical);
				}else{
					mapBodyStyle.put(vhVehicle.getBodyStyle(), countvehical + 1L);
				}
			}
		}
		
		for (Entry<String, Long> entry : mapBodyStyle.entrySet()) {
			Map<Long, Long> mapdate = new HashMap<Long, Long>();
			Map<Long, Long> treeMap = null;
			List<List<Long>> lonnn = new ArrayList<>();
			sendDateAndValue sValue = new sendDateAndValue();
			List<Vehicle> veList = null;
			
			if(user.role.equals("General Manager")){
				veList = Vehicle.findBySold();
			}else if(user.role.equals("Manager")){
				veList = Vehicle.findByBodyStyleAndSoldLocation(entry.getKey(), user.location.id);
			}else if(user.role.equals("Sales Person")){
				veList = Vehicle.findByBodyStyleAndSold(entry.getKey(), user);
			}
			
			Date startDate = null;
			Date endDate = null;
			
			try {
				startDate=df.parse(startD);
				endDate=df.parse(endD);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			//System.out.println(dateFomat);
			
			sValue.name = entry.getKey();
			if(veList != null){
					
				for(Vehicle vhVehicle:veList){
					
					if(vhVehicle.getSoldDate().after(startDate) && vhVehicle.getSoldDate().before(endDate) || vhVehicle.getSoldDate().equals(endDate))
					{
						Long countCar = 1L;
						Long objectDate = mapdate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
						if (objectDate == null) {
							Long objectAllDate = mapAlldate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
							if(objectAllDate == null){
								mapAlldate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), 1L);
							}
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), countCar);
						}else{
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
						}
					}
				}
				
				for (Entry<Long, Long> entryValue : mapdate.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); //= {entryValue.getKey(),entryValue.getValue()};
					lonnn.add(value);
				  }
				sValue.data = lonnn;
			}
			sAndValues.add(sValue);
			
		  }
		
		for(sendDateAndValue sAndValue:sAndValues){
			for(List<Long> longs:sAndValue.data){
				int i = 0;
				for(Long long1:longs){
					if(i == 0){
						for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
							if(!entryValue.getValue().equals(0L)){
								if(!long1.equals(entryValue.getKey())){
									mapAlldate.put(entryValue.getKey(), 1L);
								}else{
									mapAlldate.put(entryValue.getKey(), 0L);
								}
							}
							
						  }
						i++;
					}
					
				}
				
			}
			for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
				if(entryValue.getValue().equals(1L)){
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(0L);//entryValue.getKey(),0L};
					sAndValue.data.add(value);
					
				}else{
					mapAlldate.put(entryValue.getKey(), 1L);
				}
			  }
			
		}
		
		for(sendDateAndValue sValue:sAndValues){
		
			Collections.sort(sValue.data, new Comparator<List<Long>>(){
				 @Override
		            public int compare(List<Long> o1, List<Long> o2) {
		                return o1.get(0).compareTo(o2.get(0));
		            }
				
			});
		}
		
	}
	
	
	/*public static Result getFinancialVehicleDetailsOther(Long location,Integer managerId){
		AuthUser user = AuthUser.findById(managerId);
		List<sendDateAndValue> sAndValues = new ArrayList<>();
		setFinancialVehicle(user,sAndValues);
		return ok(Json.toJson(sAndValues));
	}*/
	
	
	public static Result getFinancialVehicleDetails(String startD,String endD){
		AuthUser user = getLocalUser();
		List<sendDateAndValue> sAndValues = new ArrayList<>();
		setFinancialVehicle(user,sAndValues,startD,endD);
		return ok(Json.toJson(sAndValues));
	}
	
	public static void setFinancialVehicle(AuthUser user,List<sendDateAndValue> sAndValues,String startD,String endD){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Vehicle> vehicle = null;
		Map<String, Long> mapMake = new HashMap<String, Long>();
		Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
		if(user.role.equals("General Manager")){
			vehicle = Vehicle.findBySold();
		}else if(user.role.equals("Manager")){
			vehicle = Vehicle.findByLocationAndSold(user.location.id);
		}else if(user.role.equals("Sales Person")){
			vehicle = Vehicle.findBySoldUserAndSold(user);
		}
		
		Long countvehical = 1L;
		
		for(Vehicle vhVehicle:vehicle){
			if(vhVehicle.getMake() != null){
				
				Long objectMake = mapMake.get(vhVehicle.getMake());
				if (objectMake == null) {
					mapMake.put(vhVehicle.getMake(), countvehical);
				}else{
					mapMake.put(vhVehicle.getMake(), countvehical + 1L);
				}
			}
		}
		
		for (Entry<String, Long> entry : mapMake.entrySet()) {
			Map<Long, Long> mapdate = new HashMap<Long, Long>();
			Map<Long, Long> treeMap = null;
			List<List<Long>> lonnn = new ArrayList<>();
			sendDateAndValue sValue = new sendDateAndValue();
			List<Vehicle> veList  = null;
			if(user.role.equals("General Manager")){
				veList = Vehicle.findBySold();
			}else if(user.role.equals("Manager")){
				veList = Vehicle.findByMakeAndSoldLocation(entry.getKey(), user.location.id);
			}else if(user.role.equals("Sales Person")){
				veList = Vehicle.findByMakeAndSold(entry.getKey(), user);
			}
			 
			Date startDate = null;
			Date endDate = null;
			try {
				startDate=df.parse(startD);
				endDate=df.parse(endD);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sValue.name = entry.getKey();
			if(veList != null){
					
				for(Vehicle vhVehicle:veList){
					
					/*Calendar c = Calendar.getInstance();
					c.setTime(vhVehicle.getSoldDate());
					c.add(Calendar.DATE, -1);
					
					String dateCheck = df.format(c.getTime());
					Date dateFomat = null;
					try {
						dateFomat = df.parse(dateCheck);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
					if(vhVehicle.getSoldDate().after(startDate) && vhVehicle.getSoldDate().before(endDate) || vhVehicle.getSoldDate().equals(endDate))
					{
					
						Long countCar = 1L;
						Long objectDate = mapdate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
						if (objectDate == null) {
							Long objectAllDate = mapAlldate.get(vhVehicle.getSoldDate().getTime() + (1000 * 60 * 60 * 24));
							if(objectAllDate == null){
								mapAlldate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), 1L);
							}
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), countCar);
						}else{
							mapdate.put(vhVehicle.getSoldDate().getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
						}
						
					}
				}
				
				for (Entry<Long, Long> entryValue : mapdate.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); //= {entryValue.getKey(),entryValue.getValue()};
					lonnn.add(value);
				  }
				sValue.data = lonnn;
			}
			sAndValues.add(sValue);
			
		  }
		
		
		
		
		for(sendDateAndValue sAndValue:sAndValues){
			for(List<Long> longs:sAndValue.data){
				int i = 0;
				for(Long long1:longs){
					if(i == 0){
						for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
							if(!entryValue.getValue().equals(0L)){
								if(!long1.equals(entryValue.getKey())){
									mapAlldate.put(entryValue.getKey(), 1L);
								}else{
									mapAlldate.put(entryValue.getKey(), 0L);
								}
							}
							
						  }
						i++;
					}
					
				}
				
			}
			for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
				if(entryValue.getValue().equals(1L)){
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(0L);//entryValue.getKey(),0L};
					sAndValue.data.add(value);
					
				}else{
					mapAlldate.put(entryValue.getKey(), 1L);
				}
			  }
			
		}
		
		for(sendDateAndValue sValue:sAndValues){
		
			Collections.sort(sValue.data, new Comparator<List<Long>>(){
				 @Override
		            public int compare(List<Long> o1, List<Long> o2) {
		                return o1.get(0).compareTo(o2.get(0));
		            }
				
			});
		}
	}
	
	
	
	public static Result getSoldVehicleDetailsAvgSale(String startD,String endD){
		List<Long[]> lonnn = new ArrayList<>();
		AuthUser user = getLocalUser();
		soldSaleVolu(user,lonnn,startD,endD);
		return ok(Json.toJson(lonnn));
	}
	
	/*public static Result getSoldVehicleDetailsAvgSaleOther(Long locationId,Integer managerId){
		AuthUser user = AuthUser.findById(managerId);
		List<Long[]> lonnn = new ArrayList<>();
		soldSaleVolu(user,lonnn);
		return ok(Json.toJson(lonnn));
	}*/
	
	public static void soldSaleVolu(AuthUser user, List<Long[]> lonnn,String startD,String endD){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		Map<Long, String> mapdate = new HashMap<Long, String>();
		Long pricevalue = 0L;
		List<Vehicle> vehicle = null;
		List<Vehicle> vehicaleNew = null;
		
		Map<Long, Long> treeMap = null;
		
		if(user.role.equals("General Manager")){
			vehicle = Vehicle.findBySold();
			vehicaleNew = Vehicle.findByNewlyArrived();
		}else if(user.role.equals("Manager")){
			vehicle = Vehicle.findByLocationAndSold(user.location.id);
			vehicaleNew = Vehicle.findByNewArrAndLocation(user.location.id);
		}else if(user.role.equals("Sales Person")){
			vehicle = Vehicle.findBySoldUserAndSold(user);
			vehicaleNew = Vehicle.findByUserAndNew(user);
		}		
		
		Date startDate = null;
		Date endDate = null;
		try {
			startDate=df.parse(startD);
			endDate=df.parse(endD);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int valueCount = 1;
			for(Vehicle vhVehicle:vehicle){
				if(vhVehicle.price != null){
					pricevalue = vhVehicle.price.longValue();
				}else{
					pricevalue = 0L;
				}
				Calendar c = Calendar.getInstance();
				c.setTime(vhVehicle.getSoldDate());
				c.add(Calendar.DATE, -1);
				
				String dateCheck = df.format(c.getTime());
				Date dateFomat = null;
				try {
					dateFomat = df.parse(dateCheck);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(dateFomat.after(startDate) && dateFomat.before(endDate) || dateFomat.equals(endDate))
				{
					String objectDate = mapdate.get(dateFomat.getTime()+ (1000 * 60 * 60 * 24));
					if (objectDate == null) {
						
						mapdate.put(dateFomat.getTime()+ (1000 * 60 * 60 * 24), pricevalue+","+valueCount);
					}else{
						String arr[] = objectDate.split(",");
						mapdate.put(dateFomat.getTime()+ (1000 * 60 * 60 * 24), Integer.parseInt(arr[0])+pricevalue+","+(Integer.parseInt(arr[1]) + valueCount));
					}
					
				}
			}
			
		
			
			for (Entry<Long, String> entry : mapdate.entrySet()) {
				String arr[] = entry.getValue().split(",");
				//Long avgPic = Long.parseLong(arr[1]);
				Integer totalDayCar = Integer.parseInt(arr[1]) + vehicaleNew.size();
				Long avgPic = Long.parseLong(arr[0]) / totalDayCar.longValue();
				Long[] value = {entry.getKey(),avgPic};
				lonnn.add(value);
			  }
		
			
			Collections.sort(lonnn, new Comparator<Long[]>(){
				 @Override
		            public int compare(Long[] o1, Long[] o2) {
		                return o1[0].compareTo(o2[0]);
		            }
				
			});
	}
	
	
	/*public static Result getSoldVehicleDetailsOther(Long locationId,Integer managerId){
		AuthUser user = AuthUser.findById(managerId);
		List<Long[]> lonnn = new ArrayList<>();
		soldVehicalDetailsSaleVolu(user,lonnn);
		return ok(Json.toJson(lonnn));
	}*/
	
	public static Result getSoldVehicleDetails(String startD,String endD){
		AuthUser user = getLocalUser();
		List<Long[]> lonnn = new ArrayList<>();
		soldVehicalDetailsSaleVolu(user,lonnn,startD,endD);
		return ok(Json.toJson(lonnn));
	}
	
	public static void soldVehicalDetailsSaleVolu(AuthUser user,List<Long[]> lonnn,String startD,String endD){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Map<Long, Long> mapdate = new HashMap<Long, Long>();
		Long pricevalue = 0L;
		List<Vehicle> vehicle = null;
		Map<Long, Long> treeMap = null;
		
		if(user.role.equals("General Manager")){
			vehicle = Vehicle.findBySold();
		}else if(user.role.equals("Manager")){
			vehicle = Vehicle.findByLocationAndSold(user.location.id);
		}else if(user.role.equals("Sales Person")){
			vehicle = Vehicle.findBySoldUserAndSold(user);
		}
			for(Vehicle vhVehicle:vehicle){
				if(vhVehicle.price != null){
					pricevalue = vhVehicle.price.longValue();
				}else{
					pricevalue = 0L;
				}
				
				Calendar c = Calendar.getInstance();
				c.setTime(vhVehicle.getSoldDate());
				c.add(Calendar.DATE, -1);
				
				String dateCheck = df.format(c.getTime());
				Date dateFomat = null;
				Date startDate = null;
				Date endDate = null;
				try {
					dateFomat = df.parse(dateCheck);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					startDate=df.parse(startD);
					endDate=df.parse(endD);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				//System.out.println(dateFomat);
				if(dateFomat.after(startDate) && dateFomat.before(endDate) || dateFomat.equals(endDate))
				{
				Long objectDate = mapdate.get(dateFomat.getTime() + (1000 * 60 * 60 * 24));
				if (objectDate == null) {
					mapdate.put(dateFomat.getTime()+ (1000 * 60 * 60 * 24), pricevalue);
				}else{
					mapdate.put(dateFomat.getTime()+ (1000 * 60 * 60 * 24), objectDate + pricevalue);
				}
				
				}
				
			}
			
			treeMap = new TreeMap<Long, Long>(mapdate);
			printMap(treeMap);
			
			for (Entry<Long, Long> entry : treeMap.entrySet()) {
				Long[] value = {entry.getKey(),entry.getValue()};
				lonnn.add(value);
			  }
		
	}
	
	
	
	public static void printMap(Map<Long, Long> map) {
		for (Map.Entry<Long, Long> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() 
                                      + " Value : " + entry.getValue());
		}
	}
	
	public static Result saveLeads(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();  
    		AuthUser users = (AuthUser) getLocalUser();
	    	Form<LeadDateWiseVM> form = DynamicForm.form(LeadDateWiseVM.class).bindFromRequest();
	    	LeadDateWiseVM vm = form.get();
	    	int flag = 0;
	    	
	    	//List<LeadsDateWise> lDateWises = LeadsDateWise.findByLocation(Location.findById(Long.parseLong(session("USER_LOCATION"))));
	    	List<LeadsDateWise> lDateWises = LeadsDateWise.getAllVehicles(users);
	    	
	    	for(LeadsDateWise lWise:lDateWises){
	    		
	    		if(lWise.goalSetTime.equals("1 week")){
	    			cal.setTime(lWise.leadsDate);  
	    			cal.add(Calendar.DATE, 7);
	    			Date m =  cal.getTime(); 
	    			if(m.after(date)) {
	    				lWise.setLeads(vm.leads);
	    				lWise.setLeadsDate(date);
	    				lWise.setGoalSetTime(vm.goalSetTime);
	    				lWise.setUser(users);
	    				lWise.setLocations(Location.findById(users.location.id));
	    				lWise.update();
	    				flag = 1;
	    			}
	    			
	    		}else if(lWise.goalSetTime.equals("1 month")){
	    			cal.setTime(lWise.leadsDate);  
	    			cal.add(Calendar.DATE, 30);
	    			Date m =  cal.getTime(); 
	    			if(m.after(date)) {
	    				lWise.setLeads(vm.leads);
	    				lWise.setLeadsDate(date);
	    				lWise.setGoalSetTime(vm.goalSetTime);
	    				lWise.setUser(users);
	    				lWise.setLocations(Location.findById(users.location.id));
	    				lWise.update();
	    				flag = 1;
	    			}
	    		}
	    		
	    	}
	    	
	    	
	    	if(flag == 0){
		    	LeadsDateWise lDateWise = new LeadsDateWise();
		    	lDateWise.setLeads(vm.leads);
				lDateWise.setLeadsDate(date);
				lDateWise.setGoalSetTime(vm.goalSetTime);
		    	lDateWise.setUser(users);
		    	lDateWise.setLocations(Location.findById(users.location.id));
		    	lDateWise.save();
	    	}
	    	
	    	
    	}	
		
		return ok();
	}
	
	public static Result exportContactsData(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	
	public static Result downloadStatusFile(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		String filePath = rootDir+File.separator+"CsvFile/contacts.csv";
    		File file = new File(filePath);
    		return ok(file);
    	}
	}
	
	
	public static Result scheduleEmail(List<String> emailList) {
		
			for(String email : emailList) {
				Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", "smtp.gmail.com");
		 		props.put("mail.smtp.port", "587");
		  
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
		  			 feedback.setSubject("Test Drive Alert");	  			
		  			 BodyPart messageBodyPart = new MimeBodyPart();	
		  	         messageBodyPart.setText("Test drive alert message from Glider Autos");	 	    
		  	         Multipart multipart = new MimeMultipart();	  	    
		  	         multipart.addBodyPart(messageBodyPart);	            
		  	         feedback.setContent(multipart);
		  		     Transport.send(feedback);
		 		   
		  			/*Message message = new MimeMessage(session);
		  			message.setFrom(new InternetAddress("glider.autos@gmail.com"));
		  			message.setRecipients(Message.RecipientType.TO,
		  			InternetAddress.parse(email));
		  			message.setSubject("Test Drive Alert");	  			
		  			Multipart multipart = new MimeMultipart();
	    			BodyPart messageBodyPart = new MimeBodyPart();
	    			messageBodyPart = new MimeBodyPart();
	    			
	    			VelocityEngine ve = new VelocityEngine();
	    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
	    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
	    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
	    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	    			ve.init();
	    			
	    	        StringWriter writer = new StringWriter();
	    	        String content = writer.toString(); 
	    			
	    			messageBodyPart.setContent(content, "text/html");
	    			multipart.addBodyPart(messageBodyPart);
	    			message.setContent(multipart);
	    			Transport.send(message);*/
	    			System.out.println("email send");
		       		} catch (MessagingException e) {
		  			  throw new RuntimeException(e);
		  		}
		}
		return ok();
	}
	
	public static Result getScheduleTime(String vin,String comDate){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    			List<String> timeList = new ArrayList<>();
    			try {
    				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    				SimpleDateFormat time = new SimpleDateFormat("h:mm:a");
        			Date d = df1.parse(comDate);
        			List<RequestMoreInfo> moreInfo = RequestMoreInfo.findByVinDate(vin,d);
        			List<ScheduleTest> schedule = ScheduleTest.findByVinDate(vin,d);
        			List<TradeIn> traidInfo = TradeIn.findByVinDate(vin,d);
        			for (TradeIn info : traidInfo) {
        				timeList.add(time.format(info.confirmTime));
					}
        			for (RequestMoreInfo info : moreInfo) {
        				timeList.add(time.format(info.confirmTime));
					}
        			for (ScheduleTest info : schedule) {
        				timeList.add(time.format(info.confirmTime));
					}
        			
				} catch (Exception e) {
					e.printStackTrace();
				}
    			
    		return ok(Json.toJson(timeList));
    	}
	}
	
	public static Result vehicleSoldEmail(List<String> emailList) {
		
			for(String email : emailList) {
				Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", "smtp.gmail.com");
		 		props.put("mail.smtp.port", "587");
		  
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
	
	public static Result getDataFromCrm(){
		AuthUser userObj = (AuthUser) getLocalUser();
		List<ContactsVM> contactsVMList = new ArrayList<>();
		List<Contacts> contactsList = Contacts.getAllContactsByLocation(Long.valueOf(session("USER_LOCATION")));
		//List<Contacts> contactsList = Contacts.getAllContacts();
		for (Contacts contacts : contactsList) {
			ContactsVM vm = new ContactsVM();
			String fullName=null;
			vm.setFirstName(contacts.firstName);
			vm.setLastName(contacts.lastName);
			if(!contacts.phone.equals(null) && !contacts.phone.equals("null")){
				vm.setPhone(contacts.phone);
			}
			
			if(!contacts.email.equals(null) && !contacts.email.equals("null")){
				vm.setEmail(contacts.email);
			}
			
			//if(!contacts.zip.equals(null) && !contacts.zip.equals("null")){
				vm.setZip(contacts.custZipCode);
			//}
			
			if(contacts.firstName !=null && contacts.lastName !=null){
				fullName = contacts.firstName+" "+contacts.lastName;
			}else{
				if(contacts.firstName !=null){
					fullName=contacts.firstName;
				}if(contacts.lastName !=null){
					fullName=contacts.lastName;
				}
			}
			vm.setFullName(fullName);
			contactsVMList.add(vm);
		}
		return ok(Json.toJson(contactsVMList));
	}
	
	
	
	public static Result getPdfPath(long id) {
		String found = ""; 
		//createDir(pdfRootDir,supplierCode);
		TradeIn tIn = TradeIn.findById(id);
		response().setContentType("application/pdf");
		response().setHeader("Content-Disposition", "inline; filename="+"SupplierAgreement.pdf");
		
		String PdfFile = pdfRootDir + File.separator + tIn.locations.id +File.separator+ "trade_in_pdf"+File.separator+tIn.id+File.separator+"Trade_In.pdf";
		File f = new File(PdfFile);
		
		response().setHeader("Content-Length", ((int)f.length())+"");
	    return ok(f);	
		
		
	}
	
	public static Result getUserPermission(){
		AuthUser userObj = (AuthUser) getLocalUser();
		List<Permission> permission = userObj.getPermission();
		for (Permission per : permission) {
			if(per.name.equalsIgnoreCase("Add Vehicle") || per.name.equalsIgnoreCase("Inventory")){
				return ok("true");
			}
		}
		return ok("false");
	}
	
	public static Result updateVehiclePrice(String vin , String price){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		int flag=0;
    		Date currDate = new Date();
    		AuthUser user = getLocalUser();
    		Integer vprice = Integer.parseInt(price);
	    	Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
	    	if(vehicle != null) {
	    		String databaseVal=vehicle.price.toString();
	    		String latestPrice=vprice.toString();
	    		if(!latestPrice.equals(databaseVal))
	    		{
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
	    				change.price = price;
	    				change.person = user.firstName +" "+user.lastName;
	    				change.vin = vin;
	    				change.save();
	    				flag=1;
	    			//	sendPriceAlertMail(vehicle.vin);
	    		}
		    	vehicle.setPrice(vprice);
		    	
		    	vehicle.update();
		    	if(flag==1){
		    		sendPriceAlertMail(vehicle.vin);
		    	}
		    	//sendPriceAlertMail(vehicle.vin);
	    	}
	    	return ok();
    	}	
    } 
	
	public static Result updateVehicleName(String vin , String name){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
	    	Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
	    	if(vehicle != null) {
		    	vehicle.setTitle(name);
		    	vehicle.update();
	    	}
	    	return ok();
    	}	
    }
	
	public static Result updateUserComment(Integer id , String comment){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser userObj = AuthUser.findById(id);
	    	/*Comments comm = Comments.getByUser(userObj);
	    	if(comm != null) {
	    		comm.setComment(comment);
	    		comm.update();
	    	}else{*/
	    	Date currDate = new Date();
	    		Comments cm = new Comments();
	    		cm.comment = comment;
	    		cm.likeDate = currDate;
	    		cm.user = userObj;
	    		cm.save();
	    	//}
	    	likeEmail(userObj.email, comment);
	    	return ok();
    	}	
    }
	
	public static Result likeEmail(String email, String comment) {
		
				Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", "smtp.gmail.com");
		 		props.put("mail.smtp.port", "587");
		  
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
		  			 feedback.setSubject("Manager like your work");	  			
		  			 BodyPart messageBodyPart = new MimeBodyPart();	
		  	         messageBodyPart.setText("Comment: "+comment);	 	    
		  	         Multipart multipart = new MimeMultipart();	  	    
		  	         multipart.addBodyPart(messageBodyPart);	            
		  	         feedback.setContent(multipart);
		  		     Transport.send(feedback);
	    			System.out.println("email send");
		       		} catch (MessagingException e) {
		  			  throw new RuntimeException(e);
		  		}
		return ok();
	}
	
	public static Result getAllContactsByLocation(Long id){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	
	public static Result removeAllContactsData(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	
	public static Result getVehiclePriceLogs(Long id,String vin,String status){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		Vehicle vehicle = null;
    		if(status.equalsIgnoreCase("Newly Arrived")){
    			vehicle = Vehicle.findByVinAndStatus(vin);
    		}else{
    			vehicle = Vehicle.findById(id);
    		}
    		List<PriceChange> pChange = PriceChange.findByVin(vin);
    		 
			List<PriceChangeVM> pList = new ArrayList<>();
			PriceChangeVM pVm1 = new PriceChangeVM();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if(vehicle != null){
				pVm1.person = "Has been added to the invnetory";
				pVm1.dateTime = df.format(vehicle.postedDate);
				pList.add(pVm1);
				
				for(PriceChange pChg:pChange){
					String dateChanege = df.format(pChg.getDateTime());
					Date changDate = null;
					try {
						changDate = df.parse(dateChanege);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(vehicle.soldDate == null){
						if (vehicle.postedDate.before(changDate) || vehicle.postedDate.equals(changDate)) {
							PriceChangeVM pVm = new PriceChangeVM();
							pVm.person = "Price Change "+pChg.price;
							pVm.dateTime = df.format(pChg.dateTime);
							pList.add(pVm);
						}
					}else{
						if ((vehicle.postedDate.before(changDate) || vehicle.postedDate.equals(changDate)) && changDate.before(vehicle.soldDate)) {
							PriceChangeVM pVm = new PriceChangeVM();
							pVm.person = "Price Change"+pChg.price;
							pVm.dateTime = df.format(pChg.dateTime);
							pList.add(pVm);
						}
					}
					
				}
				
				if(vehicle.soldDate !=null){
					pVm1 = new PriceChangeVM();
					pVm1.person = "Vehicle sold";
					pVm1.dateTime = df.format(vehicle.soldDate);
					pList.add(pVm1);
				}
			}
			return ok(Json.toJson(pList));
    	}
	}
	
	public static Result getAllLeadsByUser(Integer id){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
	
    public static Result getAllUsersToAssign() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    		return ok(home.render(""));
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
    
    public static Result deactivateAccount(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = AuthUser.findById(id);
    		if(user !=null){
    			user.setAccount("deactive");
        		user.update();
    		}
        	return ok();
    	}
    }
    public static Result getAllDeactivateUsers(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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
    		return ok(home.render(""));
    	} else {
    		AuthUser user = AuthUser.findById(id);
    		if(user !=null){
    			user.setAccount("active");
        		user.update();
    		}
        	return ok();
    	}
    }
    public static Result deleteAppointById(Long id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		ScheduleTest test = ScheduleTest.findById(id);
    		if(test !=null){
    			test.setConfirmDate(null);
    			test.setConfirmTime(null);
    			test.setLeadStatus(null);
    			test.update();
    		}
        	return ok();
    	}
    }
    
    public static Result deactiveLocationById(Long id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		Location loc = Location.findById(id);
    		if(loc !=null){
    			loc.setType("deactive");
    			loc.update();
    		}
        	return ok();
    	}
    }
    
    public static Result activeLocationById(Long id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
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