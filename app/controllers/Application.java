package controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
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
import models.AddCollection;
import models.AuthUser;
import models.AutoPortal;
import models.Blog;
import models.CampaignsVM;
import models.ClickyActionList;
import models.ClickyPagesList;
import models.ClickyVisitorsList;
import models.Comments;
import models.ContactHeader;
import models.Contacts;
import models.CoverImage;
import models.CreateNewForm;
import models.CustomerPdf;
import models.CustomizationCrm;
import models.CustomizationDataValue;
import models.CustomizationInventory;
import models.Domain;
import models.EmailDetails;
import models.FeaturedImage;
import models.FeaturedImageConfig;
import models.FollowBrand;
import models.GroupTable;
import models.HeardAboutUs;
import models.HoursOfOperation;
import models.InternalPdf;
import models.LeadType;
import models.LeadsDateWise;
import models.Location;
import models.MailchimpSchedular;
import models.MarketingAcounts;
import models.MyProfile;
import models.NewFormWebsite;
import models.NewsletterDate;
import models.Permission;
import models.PhotographerHoursOfOperation;
import models.PlanLocationTotal;
import models.PlanSalesTotal;
import models.PlanSchedule;
import models.PlanScheduleMonthlyLocation;
import models.PlanScheduleMonthlySalepeople;
import models.PremiumLeads;
import models.PriceAlert;
import models.PriceChange;
import models.Registration;
import models.RequestMoreInfo;
import models.SalesPlanSchedule;
import models.ScheduleTest;
import models.Site;
import models.SiteAboutUs;
import models.SiteComparison;
import models.SiteContent;
import models.SiteInventory;
import models.SiteLogo;
import models.SiteTestimonials;
import models.SliderImage;
import models.SliderImageConfig;
import models.SoldContact;
import models.ToDo;
import models.TradeIn;
import models.UserNotes;
import models.Vehicle;
import models.VehicleAudio;
import models.VehicleHeader;
import models.VehicleImage;
import models.VehicleImageConfig;
import models.Video;
import models.VirtualTour;
import models.Warranty;
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
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import viewmodel.AddCollectionVM;
import viewmodel.AssignToVM;
import viewmodel.AudioVM;
import viewmodel.AutoPortalVM;
import viewmodel.BarChartVM;
import viewmodel.BlogVM;
import viewmodel.CampaignsVMs;
import viewmodel.ClickyContentVM;
import viewmodel.ClickyPagesVM;
import viewmodel.ClickyPlatformVM;
import viewmodel.ContactsVM;
import viewmodel.CreateNewFormVM;
import viewmodel.DateAndValueVM;
import viewmodel.DocumentationVM;
import viewmodel.EditImageVM;
import viewmodel.HeardAboutUsVm;
import viewmodel.HoursOperation;
import viewmodel.ImageVM;
import viewmodel.InfoCountVM;
import viewmodel.InventoryVM;
import viewmodel.KeyValueDataVM;
import viewmodel.LeadDateWiseVM;
import viewmodel.LeadTypeVM;
import viewmodel.LeadVM;
import viewmodel.LocationMonthPlanVM;
import viewmodel.LocationVM;
import viewmodel.LocationWiseDataVM;
import viewmodel.NewFormWebsiteVM;
import viewmodel.NoteVM;
import viewmodel.PageVM;
import viewmodel.PinVM;
import viewmodel.PlanScheduleVM;
import viewmodel.PriceChangeVM;
import viewmodel.PriceFormatDate;
import viewmodel.RegisterVM;
import viewmodel.RequestInfoVM;
import viewmodel.SalepeopleMonthPlanVM;
import viewmodel.ScheduleTestVM;
import viewmodel.SetPriceChangeFlag;
import viewmodel.SiteAboutUsVM;
import viewmodel.SiteContentVM;
import viewmodel.SiteInventoryVM;
import viewmodel.SiteLogoVM;
import viewmodel.SiteTestimonialVM;
import viewmodel.SiteVM;
import viewmodel.SoldContactVM;
import viewmodel.SpecificationVM;
import viewmodel.ToDoVM;
import viewmodel.TradeInVM;
import viewmodel.UserLeadVM;
import viewmodel.UserNoteVM;
import viewmodel.UserVM;
import viewmodel.VehicleVM;
import viewmodel.VideoVM;
import viewmodel.VirtualTourVM;
import viewmodel.bodyStyleSetVM;
import viewmodel.domainVM;
import viewmodel.profileVM;
import viewmodel.sendDataVM;
import viewmodel.sendDateAndValue;
import views.html.agreement;
import views.html.home;
import views.html.homeSA;
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
import com.google.api.client.util.DateTime;
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
		

		public static Result preflight(String all) {
	        response().setHeader("Access-Control-Allow-Origin", "*");
	        response().setHeader("Allow", "*");
	        response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
	        response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent");
	        return ok();
	    }
		
		
		
		public static Result locationWise(Long locationId){
			AuthUser user = AuthUser.getOnlyGM();
			
			if(user != null) {

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
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),locationId.toString(),userRegistration));
			    	
			} else {
				return ok(home.render("Invalid Credentials",userRegistration));
			}
			
			
		}
		
		public static Result gmIsManager(Long locationId) {
			
			AuthUser user = AuthUser.getlocationAndManagerOne(Location.findById(locationId));
			
			if(user != null) {

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
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)), "0",userRegistration));
			    	
			} else {
				return ok(home.render("Invalid Credentials",userRegistration));
			}
			
		}
	public static Result login() {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String email = Form.form().bindFromRequest().get("email");
		String password= Form.form().bindFromRequest().get("password");
		String tokanNo= Form.form().bindFromRequest().get("tokan");
		Date curDate = new Date();
		
		AuthUser user = null;
		if(email != null && password != null){
			 user = AuthUser.find.where().eq("email", email).eq("password", password).eq("account", "active").findUnique();
		}else{
			AuthUser user1 = AuthUser.find.where().eq("email", "art@gliderllc.com").eq("password", "123456").eq("account", "active").findUnique();
			
			if(userRegistration.equals("true")){
				
				Registration registration = Registration.getTokanNo(tokanNo);
				
				if(registration != null){
					String cDate = df.format(curDate);
					Date cdates = null;
					try {
						cdates = df.parse(cDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if((cdates.equals(registration.startDate)||cdates.after(registration.startDate)) && ((cdates.equals(registration.expiryDate)||cdates.before(registration.expiryDate)))){

								session("USER_KEY", user1.id+"");
								session("USER_ROLE", user1.role+"");
								
								if(user1.location != null){
									session("USER_LOCATION", user1.location.id+"");
								}else if(user1.location == null){
									Location location = Location.findManagerType(user1);
									if(location != null){
										session("USER_LOCATION", location.id+"");
									}
								}
								
								
								//return  redirect("/dealer/index.html#/");
					    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
					    		List<Permission> userPermissions = user1.getPermission();
					    		for(Permission per: userPermissions) {
					    			permission.put(per.name, true);
					    		}
					    		
					    		
					    		
					    		
					    		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								
								Date curDates = null;
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								
									Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
									df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
									String date1=df2.format(curDate);
									try {
										curDates = formatter.parse(date1);
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								
								
								
								
								
					    		
					    		
					    		
					    		
					    		registration.setActivity(curDates);
					    		registration.update();
					    		
					    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
					    		//return redirect("/googleConnectionStatus");
						
						
					}else{
						return ok(home.render("Your account has been suspended, please contact your management for further questions",userRegistration));
					}
				}else{
					return ok(home.render("Invalid Credentials",userRegistration));
				}
				
				
				
			}
		}
		
		
	
		if(user != null) {
		
			if(user.role.equalsIgnoreCase("Admin")){
				session("USER_KEY", user.id+"");
				session("USER_ROLE", user.role+"");
				HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
	    		List<Permission> userPermissions = user.getPermission();
	    		for(Permission per: userPermissions) {
	    			permission.put(per.name, true);
	    		}
	    		
	    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
			}else if(user.role.equalsIgnoreCase("Photographer")){
				session("USER_KEY", user.id+"");
				session("USER_ROLE", user.role+"");
				if(user.location != null){
					session("USER_LOCATION", user.location.id+"");
				}
				HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
	    		List<Permission> userPermissions = user.getPermission();
	    		for(Permission per: userPermissions) {
	    			permission.put(per.name, true);
	    		}
	    		
	    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
			}else if(user.role.equalsIgnoreCase("General Manager")){
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
		    		
		    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
		    		//return redirect("/googleConnectionStatus");
				}else{
					return ok(home.render(user.getEmail(),userRegistration));
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
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
			    		//return redirect("/googleConnectionStatus");
					}else{
						return ok(home.render(user.getEmail(),userRegistration));
					}
				}else{
					return ok(home.render("Your account has been suspended, please contact your management for further questions",userRegistration));
				}
			}
    		//return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList))));
		} else {
			return ok(home.render("Invalid Credentials",userRegistration));
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
	        	return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
		}else {
			return ok(home.render("Invalid Credentials",userRegistration));
		}
	}
	
	public static Result agreementEmail(){
		
	        String to = "info@gliderllc.com";
	        String from = "glider.autos@gmail.com";
	        String host = "mail.smtp.host";
            
	        
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

	        // Get the Session object.
	        Session session = Session.getInstance(props,
	           new javax.mail.Authenticator() {
	              protected PasswordAuthentication getPasswordAuthentication() {
	                 return new PasswordAuthentication(emailUser, emailPass);
	              }
	           });

	        try {
	           Message message = new MimeMessage(session);
                try{
	           message.setFrom(new InternetAddress(emailUser,emailName));
                }catch(UnsupportedEncodingException e){
                	e.printStackTrace();
                }

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
	
	public static Result mgLogin(){
		AuthUser user = AuthUser.find.where().eq("email", "art@gliderllc.com").eq("password", "123456").eq("account", "active").findUnique();
		
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
	    		
	    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
	    		//return redirect("/googleConnectionStatus");
			}else{
				return ok(home.render(user.getEmail(),userRegistration));
			}
		}else{
			return ok(home.render("Your account has been suspended, please contact your management for further questions",userRegistration));
		}
	}
	
	public static Result salePLogin(){
		AuthUser user = AuthUser.find.where().eq("email", "felocipto@gmail.com").eq("password", "YNMAG7").eq("account", "active").findUnique();
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
				
	    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
	    		List<Permission> userPermissions = user.getPermission();
	    		for(Permission per: userPermissions) {
	    			permission.put(per.name, true);
	    		}
	    		
	    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
			}else{
				return ok(home.render(user.getEmail(),userRegistration));
			}
		}else{
			return ok(home.render("Your account has been suspended, please contact your management for further questions",userRegistration));
		}
	}
	
	public static Result gmLogin(){
		
		AuthUser user = AuthUser.find.where().eq("email", "mindnervesdemo@gmail.com").eq("password", "123456").eq("account", "active").findUnique();
		
		if(userRegistration.equals("true")){
			
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
			    		
			    		 return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
					}else{
							return ok(home.render(user.getEmail(),userRegistration));
						}
					
				
			
		}else{
			return ok(home.render("Invalid Tokan No",userRegistration));
		}
	}
	
	public static Result logout() {
		session().clear();
		return ok(home.render("",userRegistration));
	}
	
	public static Result home() {
		return ok(home.render("",userRegistration));
	}
	public static Result adminHome() {
		return ok(homeSA.render("",userRegistration));
	}
	/*public static Result photographerHome() {
		return ok(homePhotographer.render("",userRegistration));
	}*/
	
	
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
    		return ok(index.render(Json.stringify(Json.toJson(permission)), session("USER_ROLE"),session("USER_KEY"),Json.stringify(Json.toJson(events1)),Json.stringify(Json.toJson(tasksList)),"0",userRegistration));
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

    public static Result getLocalUserId() {
    	String id = session("USER_KEY");
    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
    	//AuthUser user = getLocalUser();
		return ok(Json.toJson(user.id));
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
  
    public static Result getAddPrice(Long id,Integer price,String cDate){
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	Date comingSoonDate=null;
		try {
			comingSoonDate = df.parse(cDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Vehicle vehicle = Vehicle.findById(id);
    	if(vehicle != null){
    		vehicle.setPrice(price);
    		vehicle.setComingSoonDate(comingSoonDate);
    		vehicle.update();
    	}
    	return ok();
    }
    
    public static Result setArrivelDate(Long id,String aDate){
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	Vehicle vehicle = Vehicle.findById(id);
    	if(vehicle != null){
    		try {
				vehicle.setComingSoonDate(df.parse(aDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		vehicle.update();
    	}
    	return ok();
    }
    
    public static Result sendComingSoonPOpUp(){
    	List<PriceAlert> price=PriceAlert.getAllRecordPopUp();
    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    	
    	Date curDate = null;
    	List<RequestInfoVM> rList = new ArrayList<>();
    	
    	Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
			Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
			df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
			String date1=df2.format(date);
			try {
				curDate = formatter.parse(date1);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		List<Vehicle> vehList=Vehicle.findByComingSoonDate(curDate);
    		
    		for(Vehicle vehicle:vehList){
    			if(vehicle.locations != null){
        	RequestInfoVM rVm = new RequestInfoVM();
        					rVm.id = vehicle.id;
        					rVm.vin = vehicle.vin;
        					rVm.make =  vehicle.make;
        					rVm.model = vehicle.model;
        					rVm.year = vehicle.year;
        					rVm.price = vehicle.price;
        					rVm.notifFlag=vehicle.notifFlag;
        					int vCount = 0;
        					List<PriceAlert> vehCount = PriceAlert.getByVin(vehicle.vin);
        					for(PriceAlert pAlert:vehCount){
        						vCount++;
        					}
        					rVm.subscribers = vCount;
        					
        					rVm.comingSoonDate = formatter.format(vehicle.comingSoonDate);
        					VehicleImage vehicleImg = VehicleImage.getDefaultImage(vehicle.vin);
        					if(vehicleImg != null) {
        						rVm.imageUrl = "http://glider-autos.com/glivrImg/images"+vehicleImg.thumbPath;
        					}else {
        						rVm.imageUrl = "/profile-pic.jpg";
        					}
        					
        					rList.add(rVm);
        				
        			
        		}
    		}
    		
    	return ok(Json.toJson(rList));
    }
    
    public  static Result changeVehicleNotif(long id){
    	
    	Vehicle veh=Vehicle.findById(id);
    	veh.setNotifFlag(1);
    	veh.update();
    	return ok();
    }
    
 public  static Result changeNotifFlag(long id,String title){
    	if(title.equalsIgnoreCase("month plan")){
    		PlanScheduleMonthlySalepeople plan=PlanScheduleMonthlySalepeople.findById(id);
    		plan.setNotifFlag(1);
    		plan.update();
    	}
    	else if(title.equalsIgnoreCase("invitation received")){
    		ScheduleTest plan=ScheduleTest.findById(id);
    		plan.setMeetingNotifFlag(1);
    		plan.update();
    	}
    	
    	else if(title.equalsIgnoreCase("accept meeting")){
    		ScheduleTest plan=ScheduleTest.findById(id);
    		plan.setMeetingAcceptFlag(0);
    		plan.update();
    	}
    	else if(title.equalsIgnoreCase("declined meeting")){
    		ScheduleTest plan=ScheduleTest.findById(id);
    		plan.setMeetingDeclineFlag(0);
    		plan.update();
    	}
    	else if(title.equalsIgnoreCase("coming soon")){
    		Vehicle veh=Vehicle.findById(id);
        	veh.setNotifFlag(1);
        	veh.update();
    	}
    	
    	return ok();
    }
    public  static Result sendComingSoonEmail(String vin){
    	
    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    	
    		
    		Vehicle vehicle=Vehicle.findByVinAndComingSoonDate(vin);
    		if(vehicle != null){
    			if(vehicle.locations != null){
    			Date date=new Date();
    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    			
        			Location location = Location.findById(vehicle.locations.id);
        			df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
    			try {
    				String date1=df2.format(date);
    				System.out.println(">>>>>>>>>>");
    				System.out.println(date1);
    				System.out.println(vehicle.comingSoonDate);
    				System.out.println(formatter.parse(date1));
    				
    				if(vehicle.comingSoonDate.equals(formatter.parse(date1))){
    					vehicle.setComingSoonFlag(0);
    					vehicle.update();
    				}
    				
    				List<PriceAlert> price=PriceAlert.getByVin(vin);
    	    		for(PriceAlert alert:price){
    	    		//	Vehicle vehicle1=Vehicle.findByVinAndComingSoonDate(vin);
    	    			if(alert != null){
    	    				if(alert.locations != null){
    	    				alert.setPopupFlag(0);
        					alert.update();
        					String subject=vehicle.make+" "+vehicle.model+" "+"has Arrived";
    	    	    		String comment="Hi"+" "+alert.name+" "+vehicle.make+" "+vehicle.model+" "+"has Arrived";
    	    		sendEmailForComingSoonVehicle(alert.email,subject,comment,vehicle.vin,vehicle.locations.id);
    	    			}
    	    			}
				}
    				
    			}
    			catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			}
    		}
	    	    		
    			
    		
    	return ok();
    }
public static Result sendEmailForComingSoonVehicle(String email,String subject,String comment,String vin,Long location) {
		
		final String username = emailUsername;
		final String password = emailPassword;
		
		Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
		List<Vehicle> sameBodyList = Vehicle.getRandomForComingSoon(vehicle.vin,location);
		 SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION")));
		 VehicleImage sameBodyStyleDefault=null;
		 VehicleImage sameMakeDefault=null;
		 VehicleImage sameEngineDefault=null;
		 Vehicle sameBodyStyle=null;
		 Vehicle sameEngine = null;
		 Vehicle sameMake =null;
		 for(Vehicle veh:sameBodyList){
			 if(vehicle.bodyStyle != null){
			 if(vehicle.bodyStyle.equalsIgnoreCase(veh.bodyStyle)){
				 sameBodyStyleDefault = VehicleImage.getDefaultImage(veh.vin);
				 sameBodyStyle = Vehicle.findByVin(veh.vin);
				 break;
			 }
			 }
			 
			
		 }
		 for(Vehicle veh:sameBodyList){
			 if(vehicle.make != null){
				 if(vehicle.make.equalsIgnoreCase(veh.make)){
					sameMakeDefault = VehicleImage.getDefaultImage(veh.vin); 
					sameMake = Vehicle.findByVin(veh.vin);
					break;
				 }
				 }
		 }
			
		 for(Vehicle veh:sameBodyList){
			 if(vehicle.engine != null){
				 if(vehicle.engine.equalsIgnoreCase(veh.engine)){
					 sameEngineDefault = VehicleImage.getDefaultImage(veh.vin); 
					sameEngine=  Vehicle.findByVin(veh.vin);
					break;
				 }
				 }
		 }
			
			
			
	/*	Properties props = new Properties();  
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");
		System.out.println(email);
		System.out.println(username);
		System.out.println(password);
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		    try {  
		     MimeMessage message = new MimeMessage(session);  
		     message.setFrom(new InternetAddress(username));  
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));  
		     message.setSubject(subject);  
		     message.setText(comment);  
		     Transport.send(message);  
		  
		     System.out.println("message sent successfully...");  
		   
		     } catch (MessagingException e) {
		    	 e.printStackTrace();
		    }*/
		
			
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
					InternetAddress.parse(email));
			message.setSubject("Vehicle that you were interested in has arrived");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/NotifyMeVehicleLaunch.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("hostnameUrl", imageUrlPath);
	        context.put("siteLogo", logo.logoImagePath);
	        
	        context.put("year", vehicle.year);
	        
	        int bodyFlag=0;
	        int MakeFlag=0;
	        int EngineFlag=0;
	        if(sameBodyStyleDefault == null && sameBodyStyle == null ){
	        	bodyFlag=1;
	        	context.put("bodyFlag", bodyFlag);
				 
			 }else{
	        context.put("bodyFlag", bodyFlag);
			 }
	         if(sameMakeDefault == null && sameMake == null ){
	        	 MakeFlag=1;
	        	 context.put("MakeFlag", MakeFlag);
				 
			 }
	         else{
	        	 context.put("MakeFlag", MakeFlag);
	         }
	         if(sameEngineDefault == null && sameEngine == null ){
	        	 EngineFlag=1;
	        	 context.put("EngineFlag", EngineFlag);
	        	 
			 }else{
				 context.put("EngineFlag", EngineFlag);
			 }
	        
	        if(vehicle.make != null) {
	        	context.put("make", vehicle.make);
	        	} else {
	        		context.put("make", "");
	        	}
	        context.put("model", vehicle.model);
	        context.put("vins", vehicle.vin);
	       // context.put("oldPrice", "$"+alert.oldPrice);
	        
	        if(vehicle.price != null) {
	        	context.put("newPrice", "$"+vehicle.price);
	        	} else {
	        		context.put("newPrice", "");
	        	}
	        
	        if(vehicle.bodyStyle != null) {
	        	context.put("bodyStyle", vehicle.bodyStyle);
	        	} else {
	        		context.put("bodyStyle", "");
	        	}
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
	        
	       /* context.put("email", profile.email);
	        String firstThreeDigit=profile.phone;
	        firstThreeDigit=firstThreeDigit.substring(0, 3);
	        String secondThreeDigit=profile.phone;
	        secondThreeDigit=secondThreeDigit.substring(3, 6);
	        String thirdThreeDigit=profile.phone;*/
	       /* thirdThreeDigit=thirdThreeDigit.substring(6, 10);
	        context.put("firstThreeDigit", firstThreeDigit);
	        context.put("secondThreeDigit", secondThreeDigit);
	        context.put("thirdThreeDigit", thirdThreeDigit);*/
	        
	        //context.put("phone", profile.phone);
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
	        	context.put("bodyStyleMake", "");
	        	context.put("bodyStylePrice", "");
	        	context.put("bodyStyleVin", "");
	        	context.put("bodyStyleModel", "");
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
	        	context.put("engineMake", "");
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
	        	context.put("sameMake", "");
	        	context.put("makePrice", "");
	        	context.put("makeVin", "");
	        }
	        
	        if(sameBodyStyleDefault != null) {
	        	String url=sameBodyStyleDefault.thumbPath;
	        	context.put("sameBodyStyleDefault",url.replace(" ","%20") );
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 

				
		return ok();
	}
    
    public static Result saveInternalPdf() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
    		MultipartFormData body = request().body().asMultipartFormData();
    	    InternalPdf pdf=new InternalPdf();
    			if(body != null){
		    		FilePart picture = body.getFile("file0");
		    		if (picture != null) {
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File file = picture.getFile();
		    			try {
		    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Internal_Pdf");
		    	    	    if(!fdir.exists()) {
		    	    	    	fdir.mkdir();
		    	    	    }
		    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Internal_Pdf"+File.separator+fileName;
		    	    	    FileUtils.moveFile(file, new File(filePath));
		    	    	    pdf.pdf_name=fileName;
		    	    	    pdf.pdf_path=session("USER_LOCATION")+File.separator+"Internal_Pdf"+File.separator+fileName;
		    	    	    pdf.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	    	    pdf.save();
		    			} catch (Exception e) {
							e.printStackTrace();
						}
		    		}
    		}
    	}
    	return ok();
    }
    


    public static Result getAutoPortalData(){
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
	
    
    
    
    
    
    
    
    
    public static Result saveCustomerPdf() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
    		MultipartFormData body = request().body().asMultipartFormData();
    	    CustomerPdf pdf=new CustomerPdf();
    			if(body != null){
		    		FilePart picture = body.getFile("file0");
		    		if (picture != null) {
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File file = picture.getFile();
		    			try {
		    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Customer_Pdf");
		    	    	    if(!fdir.exists()) {
		    	    	    	fdir.mkdir();
		    	    	    }
		    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Customer_Pdf"+File.separator+fileName;
		    	    	    FileUtils.moveFile(file, new File(filePath));
		    	    	    pdf.pdf_name=fileName;
		    	    	    pdf.pdf_path=session("USER_LOCATION")+File.separator+"Customer_Pdf"+File.separator+fileName;
		    	    	    pdf.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	    	    pdf.save();
		    			} catch (Exception e) {
							e.printStackTrace();
						}
		    		}
    		}
    	}
    	return ok();
    }
    
    
    
    public static Result  getCustomerPdfForVehicle(String vin){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    	 //CustomerPdf pdf = CustomerPdf.findPdfById(id);
    		Vehicle vehicle=Vehicle.findByVin(vin);
    		DocumentationVM vm = new DocumentationVM();
    		if(vehicle != null){
    			vm.customerPdfName=vehicle.pdfBrochureName;
    			vm.customerPdfPath=vehicle.pdfBrochurePath;
    			vm.vehicleVin=vehicle.vin;
    		}
    		return ok(Json.toJson(vm));
    	}
	}
	
    public static Result getLocationDays(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    public static Result uploadPhotos() {
    	DynamicForm requestData = Form.form().bindFromRequest();
        String vin = requestData.get("vin");
        String locationIdNew = requestData.get("locationIdNew");
        long locatioId=Long.parseLong(locationIdNew);
    	
        MultipartFormData body = request().body().asMultipartFormData();
	    	//Identity user = getLocalUser();
	    	//AuthUser userObj = (AuthUser)user;
	    	//AuthUser userObj=AuthUser.findById(id);
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+locatioId+File.separator+vin);
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+locatioId+File.separator+vin+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+locatioId+File.separator+vin+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    
	    	    try {
	    	    	
	    	    	
	    	    	BufferedImage originalImage = ImageIO.read(file);
			    	Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
			    	File _f = new File(filePath);
					Thumbnails.of(originalImage).scale(1.0).toFile(_f);	
	    	    	
				
				VehicleImage imageObj = VehicleImage.getByImagePath("/"+locatioId+"/"+vin+"/"+fileName);
				if(imageObj == null) {
					VehicleImage vImage = new VehicleImage();
					vImage.vin = vin;
					vImage.imgName = fileName;
					vImage.path = "/"+locatioId+"/"+vin+"/"+fileName;
					vImage.thumbPath = "/"+locatioId+"/"+vin+"/"+"thumbnail_"+fileName;
					//vImage.user = userObj;
					vImage.locations = Location.findById(locatioId);
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
    
    public static Result getImageById(Long id, String type) {
    	
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
    
    public static Result editLeads(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
    		Date currDate = new Date();
    		MultipartFormData bodys = request().body().asMultipartFormData();
    		
    		LeadVM vm = null;
        	
        	Form<LeadVM> form = DynamicForm.form(LeadVM.class).bindFromRequest();
        	if(bodys != null){
        		LeadVM leadVM1 = new LeadVM();
        		saveBilndVm(leadVM1,bodys,form);
        		vm = leadVM1;
        	}else{
        		vm = form.get();
        	}
    		
    		
	    	/*Form<LeadVM> form = DynamicForm.form(LeadVM.class).bindFromRequest();
	    	LeadVM vm = form.get();*/
	    	
	    	
	    	
	    	int parentFlag = 0;
	    	Long parentLeadId = 0L;
	    	Date reqDate = null;
	    	for(VehicleVM productVM:vm.stockWiseData){
	    		if(parentFlag == 0){
	    			
		    	if(vm.leadType.equals("Schedule Test Drive")){
		    		ScheduleTest sInfo = ScheduleTest.findById(Long.parseLong(vm.id));
		    		if(sInfo != null){
		    			sInfo.setVin(productVM.vin);
		    			//sInfo
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
		    			tInfo.setVin(productVM.vin);
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
		    	}else {
		    		RequestMoreInfo rInfo = RequestMoreInfo.findById(Long.parseLong(vm.id));
		    		if(rInfo != null){
		    			rInfo.setVin(productVM.vin);
		    			rInfo.setProductId(productVM.productId);
		    			rInfo.setName(vm.custName);
		    			rInfo.setEmail(vm.custEmail);
		    			rInfo.setPhone(vm.custNumber);
		    			rInfo.setCustZipCode(vm.custZipCode);
		    			rInfo.setEnthicity(vm.enthicity);
		    			rInfo.update();
		    			if(rInfo.isContactusType != null){
			    			if(!rInfo.isContactusType.equals("contactUs")){
			    				saveCustomData(rInfo.id,vm,bodys, Long.parseLong(rInfo.isContactusType));
			    			}
		    			}
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
		    	}
	    	}else{
	    		 if(vm.leadType.equals("Schedule Test Drive")){
	        		
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
	    	    		Vehicle vehicle = Vehicle.findByStockAndNew(productVM.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
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
	    	    		//makeToDo(vehicle.vin);
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
	    			Session session = Session.getInstance(props,
	    					new javax.mail.Authenticator() {
	    						protected PasswordAuthentication getPasswordAuthentication() {
	    							return new PasswordAuthentication(emailUser,
	    									emailPass);
	    						}
	    					});
	    			try {
	    				List<AuthUser> users = AuthUser.getAllUsers();

	    				InternetAddress[] usersArray = new InternetAddress[users.size() + 1];
	    				int index = 0;
	    				usersArray[index] = new InternetAddress(user.getEmail());
	    				
	    				Message message = new MimeMessage(session);
	    	    		try{
	    				message.setFrom(new InternetAddress(emailUsername,emailName));
	    	    		}catch(UnsupportedEncodingException e){
	    	    			e.printStackTrace();
	    	    		}
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
	        	}else{
	        		RequestMoreInfo rInfo = RequestMoreInfo.findById(Long.parseLong(vm.id));
	    			
    	    		RequestMoreInfo info = new RequestMoreInfo();
    	    		info.setIsReassigned(true);
    	    		info.setLeadStatus(null);
    	    		info.setEmail(vm.custEmail);
    	    		info.setName(vm.custName);
    	    		info.setPhone(vm.custNumber);
    	    		info.setCustZipCode(vm.custZipCode);
    	    		info.setEnthicity(vm.enthicity);
    	    		
    	    		Vehicle vehicle = Vehicle.findByStockAndNew(productVM.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
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
    	    		
    	    		
    	    		LeadType lType = LeadType.findByName(vm.leadType);
    	    		info.setIsContactusType(lType.id.toString());
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
	        	}
	    		
	    	 }
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result savePosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    public static Result   saveMakePosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<VehicleHeader> images = mapper.readValue(nodes.toString(), new TypeReference<List<VehicleHeader>>() {});
				
		    	for(VehicleHeader image : images) {
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
   
    public static Result getAllVehiclesData(){
    	int visitorCount = 0;
		
		List <Vehicle> vehicleObjList = Vehicle.findByNewArrAndLocationNoDraft(Long.valueOf(session("USER_LOCATION")));
		
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

	public static Result getGoTodraft(Long id){
		
		Vehicle vehicle= Vehicle.findById(id);
		if(vehicle != null){
			vehicle.setPublicStatus("draft");
			vehicle.update();
		}
		return ok();
		
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
    
    public static void findCustomeInventoryData(Long id,SpecificationVM inventoryvm){
    	List<CustomizationInventory> custData = CustomizationInventory.findByIdList(id);
    	List<KeyValueDataVM> keyValueList = new ArrayList<>();
    	Map<String, String> mapCar = new HashMap<String, String>();
    	for(CustomizationInventory custD:custData){
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
    
    
    
    public static Result uplaodSoundFile() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
	
  /*  public static Result sendEmail(String email, String comment) {

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
    	}*/
    public static Result editImage() throws IOException {
    		
    		//AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	    	VehicleImage image = VehicleImage.findById(vm.imageId);
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	    	Thumbnails.of(croppedImage).scale(1.0).toFile(file);
	    	
	    	//VehicleImageConfig config = VehicleImageConfig.findByUser(user);
	    	Thumbnails.of(croppedImage).height(140).width(210).toFile(thumbFile);
	    	
	    	return ok();
    }
    
    
    public static Result uploadInventoryPhotosComingSoon() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(1200, 315).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteInventory sInventory = SiteInventory.findByLocationAndType(Long.parseLong(session("USER_LOCATION")), "comingSoon");
				
				if(sInventory == null){
					SiteInventory siteInventory = new SiteInventory();
					siteInventory.imageName = fileName;
					siteInventory.imageUrl = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName;
					siteInventory.thumbPath = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName;
					siteInventory.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					siteInventory.vType = "comingSoon";
					siteInventory.findNewId=1L;
					siteInventory.save();
				}else{
					if(sInventory.applyAll == 1){
						List<SiteInventory> sInventory1 = SiteInventory.findByLocation(Long.parseLong(session("USER_LOCATION")));
						for(SiteInventory siteInventory:sInventory1){
							siteInventory.setImageName(fileName);
							siteInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
							siteInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
							Long value = siteInventory.findNewId + 1L;
							siteInventory.setFindNewId(value);
							siteInventory.update();
						}
					}else{
						sInventory.setImageName(fileName);
						sInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
						sInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
						Long value = sInventory.findNewId + 1L;
						sInventory.setFindNewId(value);
						sInventory.update();
					}
					
					
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
    
    
    public static Result uploadInventoryPhotosNew() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(1200, 315).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteInventory sInventory = SiteInventory.findByLocationAndType(Long.parseLong(session("USER_LOCATION")), "New");
				
				if(sInventory == null){
					SiteInventory siteInventory = new SiteInventory();
					siteInventory.imageName = fileName;
					siteInventory.imageUrl = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName;
					siteInventory.thumbPath = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName;
					siteInventory.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					siteInventory.vType = "New";
					siteInventory.findNewId=1L;
					siteInventory.save();
				}else{
					if(sInventory.applyAll == 1){
						List<SiteInventory> sInventory1 = SiteInventory.findByLocation(Long.parseLong(session("USER_LOCATION")));
						for(SiteInventory siteInventory:sInventory1){
							siteInventory.setImageName(fileName);
							siteInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
							siteInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
							Long value = siteInventory.findNewId + 1L;
							siteInventory.setFindNewId(value);
							siteInventory.update();
						}
					}else{
						sInventory.setImageName(fileName);
						sInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
						sInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
						
						Long value = sInventory.findNewId + 1L;
						sInventory.setFindNewId(value);
						sInventory.update();
					}
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
    
    public static Result uploadInventoryPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(1200, 315).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteInventory sInventory = SiteInventory.findByLocationAndType(Long.parseLong(session("USER_LOCATION")), "Used");
				
				if(sInventory == null){
					SiteInventory siteInventory = new SiteInventory();
					siteInventory.imageName = fileName;
					siteInventory.imageUrl = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName;
					siteInventory.thumbPath = "/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName;
					siteInventory.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					siteInventory.vType = "Used";
					siteInventory.findNewId=1L;
					siteInventory.save();
				}else{
					if(sInventory.applyAll == 1){
						List<SiteInventory> sInventory1 = SiteInventory.findByLocation(Long.parseLong(session("USER_LOCATION")));
						for(SiteInventory siteInventory:sInventory1){
							siteInventory.setImageName(fileName);
							siteInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
							siteInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
							Long value = siteInventory.findNewId + 1L;
							siteInventory.setFindNewId(value);
							siteInventory.update();
						}
					}else{
						sInventory.setImageName(fileName);
						sInventory.setImageUrl("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+fileName);
						sInventory.setThumbPath("/"+session("USER_LOCATION")+"/"+"Inventory"+"/"+"CoverImg"+"/"+"thumbnail_"+fileName);
						Long value = sInventory.findNewId + 1L;
						sInventory.setFindNewId(value);
						sInventory.update();
					}
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
    
    public static Result uploadSliderPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    public static Result  uploadContactPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"ContactImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"ContactImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"ContactImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				ContactHeader imageObj = ContactHeader.findByLocations(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					ContactHeader vImage = new ContactHeader();
					vImage.coverImageName = fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"ContactImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"ContactImages"+"/"+"thumbnail_"+fileName;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId=1L;
					vImage.save();
					
				}
				else{
					imageObj.setCoverImageName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+"ContactImages"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+"ContactImages"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
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
    
    public static Result  uploadWarrantyPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Warranty");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Warranty"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Warranty"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				Warranty imageObj = Warranty.findByLocations(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					Warranty vImage = new Warranty();
					vImage.coverImageName = fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"Warranty"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"Warranty"+"/"+"thumbnail_"+fileName;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId = 1L;
					vImage.save();
					
				}
				else{
					imageObj.setCoverImageName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+"Warranty"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+"Warranty"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
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
    
    

    public static Result  uploadComparePhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"Compare");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Compare"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"Compare"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteComparison imageObj = SiteComparison.findByLocations(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					SiteComparison vImage = new SiteComparison();
					vImage.coverImageName = fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"Compare"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"Compare"+"/"+"thumbnail_"+fileName;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId=1L;
					vImage.save();
					
				}
				else{
					imageObj.setCoverImageName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+"Compare"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+"Compare"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
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
    

    
    
    
    
    public static Result  uploadBlogPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"BlogImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"BlogImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"BlogImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				Blog imageObj = Blog .findByLocations(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					Blog vImage = new Blog();
					 vImage.coverImageName=fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"BlogImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"BlogImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId=1L;
					vImage.save();
					
				}
				else{
					imageObj.setCoverImageName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+"BlogImages"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+"BlogImages"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
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
    
    

    
    
    

    public static Result uploadCvrPhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+userObj.id+File.separator+"CvrImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+File.separator+userObj.id+File.separator+"CvrImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+File.separator+userObj.id+File.separator+"CvrImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SiteAboutUs imageObj = SiteAboutUs.findByLocation(Long.valueOf(session("USER_LOCATION")));
				if(imageObj == null) {
					SiteAboutUs vImage = new SiteAboutUs();
					vImage.imgName = fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"CvrImages"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"CvrImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.findNewId=1L;
					vImage.save();
					
				}
				else{
					imageObj.setImgName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"CvrImages"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"CvrImages"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.update();
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    
    
    
    public static Result getInventoryImage(Long id,String vType,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	SiteInventory image = SiteInventory.findByOtherId(id,vType,Long.valueOf(session("USER_LOCATION")));
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.imageUrl);
	    	}
	    	return ok(file);
    	}
    }
    
    
    public static Result contactImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	ContactHeader image = ContactHeader.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
   
    
    public static Result  vehicleProfileImageByIdForCrop(Long id,String makeValue,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	VehicleHeader image = VehicleHeader.findByIdAndMake(id,makeValue);
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    
    
    
    public static Result vehicleProfileImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	VehicleHeader image = VehicleHeader.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    

    public static Result warrantyImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	Warranty image = Warranty.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    
    public static Result compareImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	SiteComparison image = SiteComparison.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    
    
    
    
    
    public static Result blogImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	Blog image = Blog.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
    
    
	   public static Result aboutUsCoverImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	File file = null;
	    	SiteAboutUs image = SiteAboutUs.findByOtherId(id,Long.valueOf(session("USER_LOCATION")));
	    	if(image.thumbPath != null || image.path != null){
	    	if(type.equals("thumbnail")) {
		    	file = new File(rootDir+image.thumbPath);
	    	}
	    	
	    	if(type.equals("full")) {
	    		file = new File(rootDir+image.path);
	    	}
	    	}
	    	return ok(file);
    	}
    }
    
	
    public static Result getFeaturedImageById(Long id,String type) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    public static Result getAllModelList(String make) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<VehicleVM> modelList = new ArrayList<>();
	    	List<Vehicle> list = Vehicle.findByModelAndLocation(make, Long.valueOf(session("USER_LOCATION")));
	    	Map<String, Vehicle> mapModel = new HashMap<>();
    		for (Vehicle vehicle : list) {
    			if(vehicle.getModel() != null){
    				
    				Vehicle objectModel = mapModel.get(vehicle.getModel());
    				mapModel.put(vehicle.getModel(), vehicle);
    					
    			}
    		}
    		for (Entry<String, Vehicle> value : mapModel.entrySet()) {
    			VehicleVM vm=new VehicleVM();
    			vm.model=value.getKey();
    			modelList.add(vm);
    		}
	    	return ok(Json.toJson(modelList));
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
    		return ok(home.render("",userRegistration));
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
    
    public static Result getSliderImageDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
  
    
    public static Result getContactDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	ContactHeader image = ContactHeader.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.coverImageName;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.thumbPath=image.thumbPath;
			CoverImage config = CoverImage.findByUser(user);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
    

    public static Result getVehicleProfileDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	VehicleHeader image = VehicleHeader.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.coverImageName;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.thumbPath=image.thumbPath;
			CoverImage config = CoverImage.findByUser(user);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }

    
    
    
    public static Result getWarDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Warranty image = Warranty.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.findNewId = image.findNewId;
			vm.imgName = image.coverImageName;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.thumbPath=image.thumbPath;
			CoverImage config = CoverImage.findByUser(user);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }
   

    public static Result getCompareDataById (Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SiteComparison image = SiteComparison.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.coverImageName;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.thumbPath=image.thumbPath;
			CoverImage config = CoverImage.findByUser(user);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
	
    
    
    
    public static Result getBlogDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Blog image = Blog.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.coverImageName;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.thumbPath=image.thumbPath;
			CoverImage config = CoverImage.findByUser(user);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
	
    
    
    
    public static Result getCoverDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SiteAboutUs image = SiteAboutUs.findById(id);
	    	File file = new File(rootDir+image.path);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.imgName;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.path;
			vm.thumbPath=image.thumbPath;
			vm.description = image.description;
			vm.link = image.link;
			CoverImage config = CoverImage.findByUser(user);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
	
	
    public static Result getInventoryImageDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SiteInventory image = SiteInventory.findById(id);
	    	File file = new File(rootDir+image.imageUrl);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	
	    	ImageVM vm = new ImageVM();
			vm.id = image.id;
			vm.imgName = image.imageName;
			vm.row = originalImage.getHeight();
			vm.col = originalImage.getWidth();
			vm.path = image.imageUrl;
			//vm.description = image.description;
			//vm.link = image.link;
			CoverImage config = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    	}	
    }
    
    public static Result getFeaturedImageDataById(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
   
    

    public static Result editContactImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	        ContactHeader image = ContactHeader.findById(vm.imageId);
	    	image.setCoverImageName(vm.imgName);
	    	image.update();
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	       	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	       	CoverImage config = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	        Thumbnails.of(croppedImage).size(vm.w.intValue(),vm.h.intValue()).toFile(file);
	       	
	        Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
    	}	
    }
   

    public static Result editVehicleProfileImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	       VehicleHeader image = VehicleHeader.findById(vm.imageId);
	    	image.setCoverImageName(vm.imgName);
	    	image.update();
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	       	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	       	CoverImage config = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	        Thumbnails.of(croppedImage).size(vm.w.intValue(),vm.h.intValue()).toFile(file);
	       	
	        Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
    	}	
    }
   
    

    public static Result  editWarImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	       Warranty image = Warranty.findById(vm.imageId);
	    	image.setCoverImageName(vm.imgName);
	    	image.update();
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	       	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	       	CoverImage config = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	        Thumbnails.of(croppedImage).size(vm.w.intValue(),vm.h.intValue()).toFile(file);
	       	
	        Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
    	}	
    }
   
    
    public static Result  editCompareImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	       SiteComparison image = SiteComparison.findById(vm.imageId);
	    	image.setCoverImageName(vm.imgName);
	    	image.update();
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	       	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	       	CoverImage config = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	        Thumbnails.of(croppedImage).size(config.cropWidth,config.cropHeight).toFile(file);
	       	
	        Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
    	}	
    }
    
    
    
    public static Result editBlogImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	    Blog image = Blog.findById(vm.imageId);
	    	image.setCoverImageName(vm.imgName);
	    	image.update();
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	       	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	       	CoverImage config = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	        Thumbnails.of(croppedImage).size(vm.w.intValue(),vm.h.intValue()).toFile(file);
	       	
	        Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
    	}	
    }
    
    
    
    
    public static Result editCovrImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	    	SiteAboutUs image = SiteAboutUs.findById(vm.imageId);
	    	image.setImgName(vm.imgName);
	    	image.update();
	    	File file = new File(rootDir+image.path);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	       	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	       	CoverImage config = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	        Thumbnails.of(croppedImage).size(config.cropWidth,config.cropHeight).toFile(file);
	       	
	        Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
    	}	
    }
    
   
	
	
	
    public static Result getImageDataById(Long id,Long locationId) throws IOException {
    		
    	//	AuthUser user = (AuthUser) getLocalUser();
    	    //AuthUser user=AuthUser.findById(userId);
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
			VehicleImageConfig config = VehicleImageConfig.findByLocation(locationId);
			vm.width = config.cropWidth;
			vm.height = config.cropHeight;
	    	return ok(Json.toJson(vm));
    }
    
    
    public static Result editSliderImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    public static Result editInventoryImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
	    	EditImageVM vm = form.get();
	    	
	    	SiteInventory image = SiteInventory.findById(vm.imageId);
	    	/*image.setImgName(vm.imgName);
	    	image.setDescription(vm.description);
	    	image.setLink(vm.link);
	    	image.update();*/
	    	File file = new File(rootDir+image.imageUrl);
	    	File thumbFile = new File(rootDir+image.thumbPath);
	    	
	    	BufferedImage originalImage = ImageIO.read(file);
	    	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
	    	CoverImage config = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	Thumbnails.of(croppedImage).size(vm.w.intValue(),vm.h.intValue()).toFile(file);
	    	
	    	Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
	    	
	    	return ok();
	    	
	
    	}	
    }
    
    
    public static Result editFeaturedImage() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
		public static Result getLeadTypeList() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	List<LeadType> lead = LeadType.findByLocation(Long.valueOf(session("USER_LOCATION")));
		    	//List<LeadType> lead = LeadType.getLeadData();
		    	
		    	return ok(Json.toJson(lead));
	    	}	
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
    
    
    
    public static Result getAllVehical(){
    		List<Vehicle> vehicles = Vehicle.findByNewArrAndLocation(Long.valueOf(session("USER_LOCATION")));
    		return ok(Json.toJson(vehicles));
    }
    
    
    public static Result getAllCompletedLeadsbyId(Integer leadId){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    	
    	 AuthUser user = getLocalUser();
    	
    	if(listData != null){
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
        		
        		if(info.user != null){
	        		if(user.id.equals(info.user.id)){
	            		vm.setFlagSameUser = user.id;
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
        			vm.confirmDateOrderBy = info.getConfirmDate();
        		}
        		
        		if(info.getConfirmTime() != null) {
        			/*time.setTime(info.getConfirmTime());
        			String ampm = "";
        			if(time.get(Calendar.AM_PM) == Calendar.PM) {
        				ampm = "PM";
        			} else {
        				ampm = "AM";
        			}
        			vm.confirmTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;*/
        			vm.confirmTime = hourSDF.format(info.getConfirmTime());
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
    		
    		if(info.user != null){
	    		if(user.id.equals(info.user.id)){
	        		vm.setFlagSameUser = user.id;
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
    			vm.confirmDateOrderBy = info.getConfirmDate();
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
    		if(info.user != null){
	    		if(user.id.equals(info.user.id)){
	        		vm.setFlagSameUser = user.id;
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
    		//vm.typeOfLead = "Request More Info";
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
    			vm.confirmDateOrderBy = info.getConfirmDate();
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
    		return ok(home.render("",userRegistration));
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
    
    public static Result getAllContactUsSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    	for(RequestMoreInfo info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
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
	    		vm.typeOfLead = "ContactUs Info";
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
    
    public static Result getAllRequestInfoSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
	    		//vm.typeOfLead = "Request More Info";
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
    			
    		}
    		
    	}
    	vm.customData = keyValueList;
    	vm.customMapData = mapCar;
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
	    		/*rList1.typeOfLead = "Request More Info";
	    		LeadType lType = LeadType.findById(Long.parseLong(rMoreInfo.isContactusType));
	    		if(lType != null){
	    			rList1.typeOfLead = lType.leadName;
	    			vm.leadId = lType.id;
	    		}
	    		*/
	    		
	    		LeadType lType = null;
	    		if(rMoreInfo.isContactusType != null){
		    		if(!rMoreInfo.isContactusType.equals("contactUs")){
		    			lType = LeadType.findById(Long.parseLong(rMoreInfo.isContactusType));
		    		}else{
		    			lType = LeadType.findByName(rMoreInfo.isContactusType);
		    		}
		    		rList1.typeOfLead = lType.leadName;
		    		findCustomeData(rMoreInfo.id,rList1,lType.id);
	    		}else{
	    			rList1.typeOfLead = "Request More Info";
	    			findCustomeData(rMoreInfo.id,rList1,1L);
	    		}
	    		
	    		
	    		
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
    		//rList1.typeOfLead = "Request More Info";
    		
    		LeadType lType = null;
    		if(info1.isContactusType != null){
	    		if(!info1.isContactusType.equals("contactUs")){
	    			lType = LeadType.findById(Long.parseLong(info1.isContactusType));
	    		}else{
	    			lType = LeadType.findByName(info1.isContactusType);
	    		}
	    		rList1.typeOfLead = lType.leadName;
	    		findCustomeData(info1.id,rList1,lType.id);
    		}else{
    			rList1.typeOfLead = "Request More Info";
    			findCustomeData(info1.id,rList1,1L);
    		}
    		
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
    		//rList1.typeOfLead = "Request More Info";
    		LeadType lType = null;
    		if(info1.isContactusType != null){
	    		if(!info1.isContactusType.equals("contactUs")){
	    			lType = LeadType.findById(Long.parseLong(info1.isContactusType));
	    		}else{
	    			lType = LeadType.findByName(info1.isContactusType);
	    		}
	    		rList1.typeOfLead = lType.leadName;
	    		findCustomeData(info1.id,rList1,lType.id);
    		}else{
    			rList1.typeOfLead = "Request More Info";
    			findCustomeData(info1.id,rList1,1L);
    		}
    		
    		rList2.add(rList1);
		}
		vm.parentChildLead = rList2;
		infoVMList.add(vm);
    }
    
    
    
    public static Result getAllScheduleTestAssigned() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
	    		//vm.typeOfLead = "Request More Info";
	    		
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
    
    public static Result getAllTradeInSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    
    public static Result getNotificationData(){
    	Map<String, Object> mapList = new HashMap<>();
		
		AuthUser user = (AuthUser) getLocalUser();
		
		/*-----------------------Like comment---------------------*/
    	
		
		List<UserVM> listU = new ArrayList<>();
		List<Comments> comments = Comments.getByListUserWithFlag(user);
		for(Comments comm:comments){
			UserVM uVm = new UserVM();
			uVm.firstName = comm.commentUser.getFirstName();
			uVm.lastName = comm.commentUser.getLastName();
			uVm.id = comm.commentUser.id;
			uVm.userComment=comm.comment;
			if(comm.commentUser.imageUrl != null) {
				if(comm.commentUser.imageName !=null){
					uVm.imageUrl = "http://glider-autos.com/glivrImg/images"+comm.commentUser.imageUrl;
				}else{
					uVm.imageUrl = comm.commentUser.imageUrl;
				}
				
			} else {
				uVm.imageUrl = "/profile-pic.jpg";
			}
			
			listU.add(uVm);
			
			comm.setCommentFlag(0);
			comm.update();
			
		}
    	
    	mapList.put("commentLike", listU);
    	
    	/*----------------------------Plan schedule-----------------------*/
    	
    	List<PlanScheduleMonthlySalepeople> salepeople = PlanScheduleMonthlySalepeople.findByAllMsg(user);
    	
    	List<RequestInfoVM> rList1 = new ArrayList<>();
    	for(PlanScheduleMonthlySalepeople sales:salepeople){
    	if(sales != null){
    	RequestInfoVM rVm = new RequestInfoVM();
    					rVm.month = sales.month;
    					Date date=new Date();
    					Date newDate=sales.saveDate;
    					Date curDate1=null;
    					Date curDate=null;
    					DateFormat form = new SimpleDateFormat("yyyy-MM-dd");
    					String currD=form.format(date);
    					//String currD="2016-07-01";
    					String arr[]=currD.split("-");
    					String newD=arr[0]+"-"+arr[1]+"-"+"01";
    					if(currD.equals(newD) && newDate != null){
    					String planD=form.format(newDate);
    					//if(currD.equals(arg0))
    					DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    					 DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    				    Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
    						df11.setTimeZone(TimeZone.getTimeZone(location.time_zone));
    						String date1=df11.format(date);
    						String dateNew=df1.format(newDate);
    						String date11="00:00:AM";
    						
    						try {
    							curDate1=df1.parse(date1);
    							curDate = df1.parse(dateNew);
    						} catch (ParseException e1) {
    							// TODO Auto-generated catch block
    							e1.printStackTrace();
    						}
    						
    						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
    						  long diff = curDate1.getTime() - curDate.getTime();
    			  	        long diffSeconds = diff / 1000 % 60;
    			  	        long diffMinutes = diff / (60 * 1000) % 60;
    			  	        	long diffHours = diff / (60 * 60 * 1000)% 24;
    			  	        	int diffInDays = (int) ((curDate1.getTime() - curDate.getTime()) / (1000 * 60 * 60 * 24));
    			    	        String diffDay=null;
    			    	        String diffHr=null;
    			    	        if(diffInDays != 0){
    			    	        if(diffInDays <10){
    			    	        	
    			    	        	diffDay=""+diffInDays;
    			    	        }
    			    	        else{
    			    	        	diffDay=""+diffInDays;
    			    	        }
    			    	        if(diffHours <10){
    			    	        	diffHr="0"+diffHours;
    			    	        }
    			    	        else{
    			    	        	diffHr=""+diffHours;
    			    	        }
    			    	        rVm.diffDays=diffDay+" + days";
    			    	        }
    			    	        else if(diffInDays == 0 && diffHours == 0){
    			    	        	rVm.diffDays=diffMinutes+" minutes ago";;
    			        	     
    			        	        }
    			    	        else{
    			    	        	
    			    	        	 if(diffHours <10){
    			    	    	        	diffHr="0"+diffHours;
    			    	    	        }
    			    	    	        else{
    			    	    	        	diffHr=""+diffHours;
    			    	    	        }
    			    	        	 rVm.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    			    	        }
    			  	        	
    			  	     rVm.id=sales.id;	
    			  	     rVm.flagMsg=sales.flagMsg; 	
    					rList1.add(rVm);
    					}
    			
    		}
		}
    	mapList.put("planScheduleMonthly", rList1);
    for(PlanScheduleMonthlySalepeople sales:salepeople){
    		
    		sales.setFlagMsg(0);
    		sales.update();
    		
    	}
    	
    	/*-------------------Coming soon--------------------------*/
    	
    	List<PriceAlert> price=PriceAlert.getAllRecordPopUp();
    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    	
    	
    	Date curDate = null;
    	Date curDate1 = null;
    	Date curDateNew = null;
    	List<RequestInfoVM> rList = new ArrayList<>();
    	Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
		DateFormat format1 = new SimpleDateFormat("HH:mm:a");
		 DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
			Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
			df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
			df11.setTimeZone(TimeZone.getTimeZone(location.time_zone));
			String date1=df2.format(date);
			String dateNew=df11.format(date);
			String date11="00:00:AM";
			
			try {
				curDate1=df1.parse(dateNew);
				curDate = formatter.parse(date1);
				curDateNew=format1.parse(date11);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
			  long diff = curDate1.getTime() - curDateNew.getTime();
  	        long diffSeconds = diff / 1000 % 60;
  	        long diffMinutes = diff / (60 * 1000) % 60;
  	        	long diffHours = diff / (60 * 60 * 1000)% 24;
			
			
    		List<Vehicle> vehList=Vehicle.findByComingSoonDate(curDate);
    		
    		for(Vehicle vehicle:vehList){
    			if(vehicle.locations != null){
        	RequestInfoVM rVm = new RequestInfoVM();
        					rVm.id = vehicle.id;
        					rVm.vin = vehicle.vin;
        					rVm.make =  vehicle.make;
        					rVm.model = vehicle.model;
        					rVm.year = vehicle.year;
        					rVm.price = vehicle.price;
        					if(diffHours != 0){
        					rVm.diffDays=diffHours+" hours"+diffMinutes+" minutes ago";
        					}
        					else{
        						rVm.diffDays=diffMinutes+" minutes ago";
        					}
        					int vCount = 0;
        					List<PriceAlert> vehCount = PriceAlert.getByVin(vehicle.vin);
        					for(PriceAlert pAlert:vehCount){
        						vCount++;
        					}
        					rVm.subscribers = vCount;
        					
        					rVm.comingSoonDate = formatter.format(vehicle.comingSoonDate);
        					VehicleImage vehicleImg = VehicleImage.getDefaultImage(vehicle.vin);
        					if(vehicleImg != null) {
        						rVm.imageUrl = "http://glider-autos.com/glivrImg/images"+vehicleImg.thumbPath;
        					}else {
        						rVm.imageUrl = "/profile-pic.jpg";
        					}
        					
        					rList.add(rVm);
        				
        			
        		}
    		}
    		
    		
    		mapList.put("comingSoonData", rList);
    		
    	/*-----------------------Invitation---------------------------------*/
    		
    	      DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
  	        Date currD = new Date();
  	        String cDate = df.format(currD);
  	        Date datec = null;
  	        try {
  				datec = df.parse(cDate);
  			} catch (ParseException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
  	        
  		List<ScheduleTest> list = ScheduleTest.findAllByInvitation(user, datec);
  		
  		List<RequestInfoVM> checkData = new ArrayList<>();
  		for(ScheduleTest sche:list){
  			
  			RequestInfoVM sTestVM = new RequestInfoVM();
          	
          	
  			sTestVM.id = sche.id;
          	sTestVM.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(sche.confirmDate);
          	sTestVM.confirmTime = new SimpleDateFormat("hh:mm a").format(sche.confirmTime);
          	sTestVM.confirmDateOrderBy = sche.confirmDate;
          	sTestVM.typeOfLead = "Schedule Test Drive";
          	sTestVM.name = sche.name;
      		sTestVM.phone = sche.phone;
      		sTestVM.email = sche.email;
      		sTestVM.sendInvitation=sche.sendInvitation;
      		AuthUser user2 = AuthUser.findById(sche.user.id);
      		if(user2.imageUrl != null) {
  				if(user2.imageName !=null){
  					sTestVM.imageUrl = "http://glider-autos.com/glivrImg/images"+user2.imageUrl;
  				}else{
  					sTestVM.imageUrl = user2.imageUrl;
  				}
  				
  			} else {
  				sTestVM.imageUrl = "/profile-pic.jpg";
  			}
      		
      		
      		Date schDate=new Date();
			Date schcurDate11=null;
			Date schcurDate1=null;
      		DateFormat schDatedf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
			 DateFormat schDatedf11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
		    Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    schDatedf11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
				String schDate1=schDatedf11.format(schDate);
				String dateNew1=schDatedf11.format(sche.scheduleTime);
				
				try {
					schcurDate11=schDatedf1.parse(schDate1);
					schcurDate1 =schDatedf1.parse(dateNew1);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				  long schdiff = schcurDate11.getTime() - schcurDate1.getTime();
	  	        long diffSeconds1 = diff / 1000 % 60;
	  	        long diffMinutes1 = diff / (60 * 1000) % 60;
	  	        	long diffHours1 = diff / (60 * 60 * 1000)% 24;
	  	        	int diffInDays1 = (int) ((schcurDate11.getTime() - schcurDate1.getTime()) / (1000 * 60 * 60 * 24));
	    	        String diffDay=null;
	    	        String diffHr=null;
	    	        if(diffInDays1 != 0){
	    	        if(diffInDays1<10){
	    	        	
	    	        	diffDay=""+diffInDays1;
	    	        }
	    	        else{
	    	        	diffDay=""+diffInDays1;
	    	        }
	    	        if(diffHours <10){
	    	        	diffHr=""+diffHours1;
	    	        }
	    	        else{
	    	        	diffHr=""+diffHours1;
	    	        }
	    	        sTestVM.diffDays=diffDay+" + days";
	    	        }
	    	        else if(diffInDays1 == 0 && diffHours1 == 0){
	    	        	sTestVM.diffDays=diffMinutes1+" minutes ago";;
	        	     
	        	        }
	    	        else{
	    	        	
	    	        	 if(diffHours1 <10){
	    	    	        	diffHr=""+diffHours1;
	    	    	        }
	    	    	        else{
	    	    	        	diffHr=""+diffHours1;
	    	    	        }
	    	        	 sTestVM.diffDays=diffHr+" hours "+diffMinutes1+" minutes ago";
	    	        }
      		
      		
      		checkData.add(sTestVM);
      		
  			sche.setSendInvitation(0);
  			sche.update();
  		}
  		
  		mapList.put("invitationData", checkData);
  		
  		
  		/*-------------Decline Metting--------------------------------*/
  		
    	
    	List<ScheduleTest> sche = ScheduleTest.getdeclineMeeting(user);
    	List<RequestInfoVM> acList1 = new ArrayList<>();
    	for(ScheduleTest sch1:sche){
    	if(sch1 != null){
    	RequestInfoVM rVm = new RequestInfoVM();
    	AuthUser user1=AuthUser.findById(sch1.assignedTo.id);
    					rVm.firstName = user1.firstName;
    					rVm.lastName = user1.lastName;
    					rVm.name = sch1.name;
    					rVm.id = sch1.id;
    					rVm.declineMeeting=sch1.declineMeeting;
    					rVm.declineReason=sch1.declineReason;
    					Date schDate=new Date();
    					Date schcurDate11=null;
    					Date schcurDate1=null;
    					DateFormat schDatedf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    					 DateFormat schDatedf11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    				    Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
    				    schDatedf11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
    						String schDate1=schDatedf11.format(schDate);
    						String dateNew1=schDatedf11.format(sch1.meetingActionTime);
    						
    						try {
    							schcurDate11=schDatedf1.parse(schDate1);
    							schcurDate1 =schDatedf1.parse(dateNew1);
    						} catch (ParseException e1) {
    							// TODO Auto-generated catch block
    							e1.printStackTrace();
    						}
    						
    						  long schdiff = schcurDate11.getTime() - schcurDate1.getTime();
    			  	        long diffSeconds1 = schdiff / 1000 % 60;
    			  	        long diffMinutes1 = schdiff / (60 * 1000) % 60;
    			  	        	long diffHours1 = diff / (60 * 60 * 1000)% 24;
    			  	        	int diffInDays1 = (int) ((schcurDate11.getTime() - schcurDate1.getTime()) / (1000 * 60 * 60 * 24));
    			    	        String diffDay=null;
    			    	        String diffHr=null;
    			    	        if(diffInDays1 != 0){
    			    	        if(diffInDays1<10){
    			    	        	
    			    	        	diffDay=""+diffInDays1;
    			    	        }
    			    	        else{
    			    	        	diffDay=""+diffInDays1;
    			    	        }
    			    	        if(diffHours <10){
    			    	        	diffHr="0"+diffHours1;
    			    	        }
    			    	        else{
    			    	        	diffHr=""+diffHours1;
    			    	        }
    			    	        rVm.diffDays=diffDay+" + days";
    			    	        }
    			    	        else if(diffInDays1 == 0 && diffHours1 == 0){
    			    	        	rVm.diffDays=diffMinutes1+" minutes ago";;
    			        	     
    			        	        }
    			    	        else{
    			    	        	
    			    	        	 if(diffHours1 <10){
    			    	    	        	diffHr="0"+diffHours1;
    			    	    	        }
    			    	    	        else{
    			    	    	        	diffHr=""+diffHours1;
    			    	    	        }
    			    	        	 rVm.diffDays=diffHr+" hours "+diffMinutes1+" minutes ago";
    			    	        }
    			  	        	
    			  	        	
    			  	        	
    			    	        acList1.add(rVm);
    			    	        
    			    	        for(ScheduleTest sch:sche){
    			    	    		sch.setDeclineMeeting(0);
    			    	    		sch.update();
    			    	    	}
    			
    		}
		}
    	
    	mapList.put("declineMeeting", acList1);
    	
    	
    	
    	
    	/*-----------------accept msg--------------------------*/
		
    	List<ScheduleTest> sche1 = ScheduleTest.getacceptMeeting(user);
    	List<RequestInfoVM> acList = new ArrayList<>();
    	for(ScheduleTest sch1:sche1){
    	if(sch1 != null){
    	RequestInfoVM rVm = new RequestInfoVM();
    	AuthUser usersData = AuthUser.findById(sch1.assignedTo.id);
    					rVm.firstName = usersData.firstName;
    					rVm.lastName = usersData.lastName;
    					rVm.name = sch1.name;
    					rVm.acceptMeeting=sch1.acceptMeeting;
    					rVm.id = sch1.id;
    					Date schDate=new Date();
    					Date schcurDate11=null;
    					Date schcurDate1=null;
    					DateFormat schDatedf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    					 DateFormat schDatedf11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
    				    Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
    				    schDatedf11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
    						String schDate1=schDatedf11.format(schDate);
    						String dateNew1=schDatedf11.format(sch1.meetingActionTime);
    						
    						try {
    							schcurDate11=schDatedf1.parse(schDate1);
    							schcurDate1 =schDatedf1.parse(dateNew1);
    						} catch (ParseException e1) {
    							// TODO Auto-generated catch block
    							e1.printStackTrace();
    						}
    						
    						  long schdiff = schcurDate11.getTime() - schcurDate1.getTime();
    			  	        long diffSeconds1 = diff / 1000 % 60;
    			  	        long diffMinutes1 = diff / (60 * 1000) % 60;
    			  	        	long diffHours1 = diff / (60 * 60 * 1000)% 24;
    			  	        	int diffInDays1 = (int) ((schcurDate11.getTime() - schcurDate1.getTime()) / (1000 * 60 * 60 * 24));
    			    	        String diffDay=null;
    			    	        String diffHr=null;
    			    	        if(diffInDays1 != 0){
    			    	        if(diffInDays1<10){
    			    	        	
    			    	        	diffDay=""+diffInDays1;
    			    	        }
    			    	        else{
    			    	        	diffDay=""+diffInDays1;
    			    	        }
    			    	        if(diffHours <10){
    			    	        	diffHr="0"+diffHours1;
    			    	        }
    			    	        else{
    			    	        	diffHr=""+diffHours1;
    			    	        }
    			    	        rVm.diffDays=diffDay+" + days";
    			    	        }
    			    	        else if(diffInDays1 == 0 && diffHours1 == 0){
    			    	        	rVm.diffDays=diffMinutes1+" minutes ago";;
    			        	     
    			        	        }
    			    	        else{
    			    	        	
    			    	        	 if(diffHours1 <10){
    			    	    	        	diffHr="0"+diffHours1;
    			    	    	        }
    			    	    	        else{
    			    	    	        	diffHr=""+diffHours1;
    			    	    	        }
    			    	        	 rVm.diffDays=diffHr+" hours "+diffMinutes1+" minutes ago";
    			    	        }
    			  	        	
    			  	        	
    			  	        	
    			    	        acList.add(rVm);
    			    	        for(ScheduleTest sch2:sche1){
    			    	    		sch2.setAcceptMeeting(0);
    			    	    		sch2.update();
    			    	    	}
    			
    		}
		}
    	mapList.put("acceptedMeeting", acList);
    	
    	
    	
    	
    	
    	
    	
    	/*---------------------delete meeting-------------------------------*/
    	
    	SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
    	List<ScheduleTest> sche2 = ScheduleTest.getdeleteMsg(user);
    	
    	List<ScheduleTestVM> list1 = new ArrayList<ScheduleTestVM>();
    	for(ScheduleTest sch:sche2){
    		if(sch.declineUser.equals("Host")){
    			if(!user.id.equals(sch.user.id)){
    				
    				sch.setDeleteMsgFlag(0);
    	    		sch.update();
    				
    				ScheduleTestVM sLVm = new ScheduleTestVM();
    	    		sLVm.name = sch.name;
    	    		sLVm.reason = sch.reason;
    	    		sLVm.confirmDate = df3.format(sch.confirmDate);
    	    		sLVm.confirmTime = parseTime.format(sch.confirmTime);
    	    		AuthUser usersData = AuthUser.findById(sch.assignedTo.id);
    	    		sLVm.firstName = usersData.firstName;
    	    		sLVm.lastName = usersData.lastName;
    	    		sLVm.declineUser = sch.declineUser;
    	    		list1.add(sLVm);
    			}
    		}else if(sch.declineUser.equals("this person")){
    			
    			if(!user.id.equals(sch.assignedTo.id)){
    				sch.setDeleteMsgFlag(0);
            		sch.update();
        			
        			ScheduleTestVM sLVm = new ScheduleTestVM();
    	    		sLVm.name = sch.name;
    	    		sLVm.reason = sch.reason;
    	    		sLVm.confirmDate = df.format(sch.confirmDate);
    	    		sLVm.confirmTime = parseTime.format(sch.confirmTime);
    	    		AuthUser usersData = AuthUser.findById(sch.assignedTo.id);
    	    		sLVm.firstName = usersData.firstName;
    	    		sLVm.lastName = usersData.lastName;
    	    		sLVm.declineUser = sch.declineUser;
    	    		list1.add(sLVm);
    			}
    		}
    	}
    	
    	mapList.put("deleteMeeting", list1);
    	
    	
    	/*-------update meeting----------------------------------------*/
    	
    	List<ScheduleTest> schedu = ScheduleTest.getUpdateMeeting(user);
    	List<ScheduleTestVM> listData = new ArrayList<>();
    	for(ScheduleTest sch:schedu){
    		ScheduleTestVM vm = new ScheduleTestVM();
    		vm.confirmTime = new SimpleDateFormat("hh:mm a").format(sch.confirmTime);
    		vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(sch.confirmEndTime);
    		vm.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(sch.confirmDate);
    		vm.name = sch.name;
    		vm.reason = sch.reason;
    		listData.add(vm);
    		sch.setDeclineUpdate(0);
    		
    		
    		Date schDate=new Date();
			Date schcurDate11=null;
			Date schcurDate1=null;
			DateFormat schDatedf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
			 DateFormat schDatedf11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
		    Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    schDatedf11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
				String schDate1=schDatedf11.format(schDate);
				String dateNew1=schDatedf11.format(sch.meetingActionTime);
				
				try {
					schcurDate11=schDatedf1.parse(schDate1);
					schcurDate1 =schDatedf1.parse(dateNew1);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				  long schdiff = schcurDate11.getTime() - schcurDate1.getTime();
	  	        long diffSeconds1 = diff / 1000 % 60;
	  	        long diffMinutes1 = diff / (60 * 1000) % 60;
	  	        	long diffHours1 = diff / (60 * 60 * 1000)% 24;
	  	        	int diffInDays1 = (int) ((schcurDate11.getTime() - schcurDate1.getTime()) / (1000 * 60 * 60 * 24));
	    	        String diffDay=null;
	    	        String diffHr=null;
	    	        if(diffInDays1 != 0){
	    	        if(diffInDays1<10){
	    	        	
	    	        	diffDay=""+diffInDays1;
	    	        }
	    	        else{
	    	        	diffDay=""+diffInDays1;
	    	        }
	    	        if(diffHours <10){
	    	        	diffHr="0"+diffHours1;
	    	        }
	    	        else{
	    	        	diffHr=""+diffHours1;
	    	        }
	    	        vm.diffDays=diffDay+" + days";
	    	        }
	    	        else if(diffInDays1 == 0 && diffHours1 == 0){
	    	        	vm.diffDays=diffMinutes1+" minutes ago";;
	        	     
	        	        }
	    	        else{
	    	        	
	    	        	 if(diffHours1 <10){
	    	    	        	diffHr="0"+diffHours1;
	    	    	        }
	    	    	        else{
	    	    	        	diffHr=""+diffHours1;
	    	    	        }
	    	        	 vm.diffDays=diffHr+" hours "+diffMinutes1+" minutes ago";
	    	        }

    		
    		
    		sch.update();
    	}
    	
    	mapList.put("updateMeeting", listData);
    	
    	/*----------------------------------------*/
    	
    	List<RequestInfoVM> actionVM= new ArrayList<RequestInfoVM>();
    	findReminderPopupFunction(actionVM);
    	mapList.put("reminderPopup", actionVM);

    	
    	return ok(Json.toJson(mapList));
		
    	
    }
    
    public static Result getInfoCount() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    
    public static Result getLeadInfo() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
	    	InfoCountVM vm = new InfoCountVM();
	    	List<ScheduleTest> scList = ScheduleTest.findAllLeads(Long.valueOf(session("USER_LOCATION")));
	    	List<TradeIn> trList = TradeIn.findAllLeads(Long.valueOf(session("USER_LOCATION")));
	    	List<RequestMoreInfo> rList = RequestMoreInfo.findAllLeads(Long.valueOf(session("USER_LOCATION")));
	    	List<RequestInfoVM> list = new ArrayList<>();
	    	Date curr = new Date();
    		Location location = Location.findById(Long.parseLong(session("USER_LOCATION")));
      		 if(user.location != null){
      			 location = Location.findById(user.location.id);
      		 }
      		 DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
      		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
      		df1.setTimeZone(TimeZone.getTimeZone(location.time_zone));	
      		 String dat=df1.format(curr);
      		Date currD=null;
      		 try {
				currD=df2.parse(dat);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
	    	for(ScheduleTest sc: scList){
	    		RequestInfoVM vm1=new RequestInfoVM();
	    		vm1.name=sc.name;
	    		vm1.id=sc.id;
	    		vm1.typeOfLead="Schedule Test";
	    		vm1.type="Schedule Test";
	    		vm1.notifFlag=sc.notifFlag;
	    		vm1.leadTypeForNotif="Schedule Test Drive";
	    		String imagePath=null;
	    		if(sc.vin != null){
	    			VehicleImage image=VehicleImage.getDefaultImage(sc.vin);
	    			if(image != null){
	    				imagePath=image.thumbPath;
	    			}
	    		}
	    		vm1.imagePath=imageUrlPath+imagePath;
	    		
    	        Date dt1=null;
    	        Date dt2=null;
    	        try {
    	        	dt1 =currD;
    	            //dt2 = sc.scheduleTime;
    	        	 String dat1=df1.format(sc.scheduleTime);
    	            dt2=df2.parse(dat1);
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }

    	        // Get msec from each, and subtract.
    	        long diff = dt1.getTime() - dt2.getTime();
    	        vm1.timeDiff=diff;
    	        long diffSeconds = diff / 1000 % 60;
    	        long diffMinutes = diff / (60 * 1000) % 60;
    	        long diffHours = diff / (60 * 60 * 1000)% 24;
    	        int diffInh = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 ));
    	        int diffInDays = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 * 24));
    	        String diffDay=null;
    	        String diffHr=null;
    	        if(diffInDays != 0){
    	        if(diffInDays <10){
    	        	
    	        	diffDay=""+diffInDays;
    	        }
    	        else{
    	        	diffDay=""+diffInDays;
    	        }
    	        if(diffHours <10){
    	        	diffHr="0"+diffHours;
    	        }
    	        else{
    	        	diffHr=""+diffHours;
    	        }
    	        vm1.timeUnit=diffDay+" days "+diffHr+" hours "+diffMinutes+" minutes ago";
    	        vm1.diffDays=diffDay+" + days";
    	        }
    	        else if(diffInDays == 0 && diffHours == 0){
    	        	if(diffMinutes == 1){
    	        		vm1.diffDays=diffMinutes+" minute ago";
            	        vm1.timeUnit=diffMinutes+" minute ago";
    	        	}else{
    	        	vm1.diffDays=diffMinutes+" minutes ago";
        	        vm1.timeUnit=diffMinutes+" minutes ago";
    	        	}
        	     
        	        }
    	        else{
    	        	
    	        	 if(diffHours <10){
    	    	        	diffHr=""+diffHours;
    	    	        }
    	    	        else{
    	    	        	diffHr=""+diffHours;
    	    	        }
    	        	vm1.timeUnit=diffHr+" hours "+diffMinutes+" minutes ago";
    	        	vm1.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    	        }
	    		
	    		list.add(vm1);		
	    		
	    	}
	    	
	    	for(TradeIn sc: trList){
	    		RequestInfoVM vm1=new RequestInfoVM();
	    		vm1.name=sc.firstName+" "+sc.lastName;
	    		vm1.typeOfLead="Trade In";
	    		vm1.leadTypeForNotif="Trade In";
	    		vm1.id=sc.id;
	    		vm1.type="Trade In";
	    		vm1.notifFlag=sc.notifFlag;
	    		String imagePath=null;
	    		if(sc.vin != null){
	    			VehicleImage image=VehicleImage.getDefaultImage(sc.vin);
	    			if(image != null){
	    				imagePath=image.thumbPath;
	    			}
	    		}
	    		vm1.imagePath=imageUrlPath+imagePath;
    	        Date dt1=null;
    	        Date dt2=null;
    	        try {
    	        	dt1 =currD;
    	        	String dat1=df1.format(sc.tradeTime);
    	            dt2=df2.parse(dat1);
    	            //dt2 = sc.tradeTime;
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }

    	        // Get msec from each, and subtract.
    	        long diff = dt1.getTime() - dt2.getTime();
    	        vm1.timeDiff=diff;
    	        long diffSeconds = diff / 1000 % 60;
    	        long diffMinutes = diff / (60 * 1000) % 60;
    	        long diffHours = diff / (60 * 60 * 1000)% 24;
    	        int diffInh = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 ));
    	        int diffInDays = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 * 24));
    	        String diffDay=null;
    	        String diffHr=null;
    	        if(diffInDays != 0){
    	        if(diffInDays <10){
    	        	diffDay=""+diffInDays;
    	        }
    	        else{
    	        	diffDay=""+diffInDays;
    	        }
    	        if(diffHours <10){
    	        	diffHr=""+diffHours;
    	        }
    	        else{
    	        	diffHr=""+diffHours;
    	        }
    	        vm1.timeUnit=diffDay+" days "+diffHr+" hours "+diffMinutes+" minutes ago";
    	        vm1.diffDays=diffDay+" + days";
    	        }
    	        else if(diffInDays == 0 && diffHours == 0){
    	        	if(diffMinutes == 1){
    	        		vm1.diffDays=diffMinutes+" minute ago";
            	        vm1.timeUnit=diffMinutes+" minute ago";
    	        	}else{
    	        	vm1.diffDays=diffMinutes+" minutes ago";
        	        vm1.timeUnit=diffMinutes+" minutes ago";
    	        	}
        	     
        	        }
    	        else{
    	        	
    	        	 if(diffHours <10){
    	    	        	diffHr="0"+diffHours;
    	    	        }
    	    	        else{
    	    	        	diffHr=""+diffHours;
    	    	        }
    	        	vm1.timeUnit=diffHr+" hours "+diffMinutes+" minutes ago";
    	        	vm1.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    	        }
	    		list.add(vm1);		
	    		
	    	}
	    	
	    	for(RequestMoreInfo sc: rList){
	    		RequestInfoVM vm1=new RequestInfoVM();
	    		vm1.name=sc.name;
	    		vm1.id=sc.id;
	    		vm1.type="Request More Info";
	    		vm1.notifFlag=sc.notifFlag;
	    		String imagePath=null;
	    		String typeoflead=null;
	    		if(sc.vin != null || sc.isContactusType == null){
	    			VehicleImage image=VehicleImage.getDefaultImage(sc.vin);
	    			if(image != null){
	    				imagePath=image.thumbPath;
	    			}
	    			typeoflead="Request More";
	    			vm1.leadTypeForNotif="Request More Info";
	    		}
	    		else if(sc.isContactusType.equals("contactUs")){
	    			imagePath="../../../assets/global/images/leadsImages/rmail.png" ;
	    			typeoflead="Contact Us";
	    			vm1.leadTypeForNotif="Contact Us";
	    		}
	    		vm1.typeOfLead=typeoflead;
	    		vm1.imagePath=imageUrlPath+imagePath;
    	        Date dt1=null;
    	        Date dt2=null;
    	        try {
    	        	dt1 =currD;
    	           // dt2 = sc.requestTime;
    	            String dat1=df1.format(sc.requestTime);
    	            dt2=df2.parse(dat1);
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }

    	        // Get msec from each, and subtract.
    	        long diff = dt1.getTime() - dt2.getTime();
    	        vm1.timeDiff=diff;
    	        long diffSeconds = diff / 1000 % 60;
    	        long diffMinutes = diff / (60 * 1000) % 60;
    	        long diffHours = diff / (60 * 60 * 1000)% 24;
    	        int diffInh = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 ));
    	        int diffInDays = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 * 24));
    	        String diffDay=null;
    	        String diffHr=null;
    	        if(diffInDays != 0){
    	        if(diffInDays <10){
    	        	diffDay=""+diffInDays;
    	        }
    	        else{
    	        	diffDay=""+diffInDays;
    	        }
    	        if(diffHours <10){
    	        	diffHr=""+diffHours;
    	        }
    	        else{
    	        	diffHr=""+diffHours;
    	        }
    	        vm1.timeUnit=diffDay+" days "+diffHr+" hours "+diffMinutes+" minutes ago";
    	        vm1.diffDays=diffDay+" + days";
    	        }
    	        else if(diffInDays == 0 && diffHours == 0){
    	        	if(diffMinutes == 1){
    	        		vm1.diffDays=diffMinutes+" minute ago";
            	        vm1.timeUnit=diffMinutes+" minute ago";
    	        	}else{
    	        	vm1.diffDays=diffMinutes+" minutes ago";
        	        vm1.timeUnit=diffMinutes+" minutes ago";
    	        	}
        	     
        	        }
    	        else{
    	        	
    	        	 if(diffHours <10){
    	    	        	diffHr="0"+diffHours;
    	    	        }
    	    	        else{
    	    	        	diffHr=""+diffHours;
    	    	        }
    	        	vm1.timeUnit=diffHr+" hours "+diffMinutes+" minutes ago";
    	        	vm1.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    	        }
	    		
	    		list.add(vm1);		
	    		
	    	}
	    	
	    	
	    	List<ScheduleTest> sched = ScheduleTest.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	    	List<RequestMoreInfo> reInfos = RequestMoreInfo.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));
	    	List<TradeIn> tradeIns = TradeIn.findAllLocationDataManagerPremium(Long.valueOf(session("USER_LOCATION")));

	    	int premi = sched.size() + reInfos.size() + tradeIns.size();
	    	vm.premium = premi;
	    	
	    	for(ScheduleTest sc: sched){
	    		RequestInfoVM vm1=new RequestInfoVM();
	    		vm1.name=sc.name;
	    		vm1.typeOfLead="Premium";
	    		vm1.leadTypeForNotif="Premium Lead";
	    		vm1.type="Schedule Test";
	    		vm1.id=sc.id;
	    		vm1.notifFlag=sc.notifFlag;
	    		String imagePath=null;
	    		if(sc.vin != null){
	    			VehicleImage image=VehicleImage.getDefaultImage(sc.vin);
	    			if(image != null){
	    				imagePath=image.thumbPath;
	    			}
	    		}
	    		vm1.imagePath=imageUrlPath+imagePath;
    	        Date dt1=null;
    	        Date dt2=null;
    	        try {
    	        	dt1 =currD;
    	        	String dat1=df1.format(sc.scheduleTime);
    	            dt2=df2.parse(dat1);
    	           // dt2 = sc.scheduleTime;
    	            
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }

    	        // Get msec from each, and subtract.
    	        long diff = dt1.getTime() - dt2.getTime();
    	        vm1.timeDiff=diff;
    	        long diffSeconds = diff / 1000 % 60;
    	        long diffMinutes = diff / (60 * 1000) % 60;
    	        long diffHours = diff / (60 * 60 * 1000)% 24;
    	        int diffInh = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 ));
    	        int diffInDays = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 * 24));
    	        String diffDay=null;
    	        String diffHr=null;
    	        if(diffInDays != 0){
    	        if(diffInDays <10){
    	        	diffDay=""+diffInDays;
    	        }
    	        else{
    	        	diffDay=""+diffInDays;
    	        }
    	        if(diffHours <10){
    	        	diffHr=""+diffHours;
    	        }
    	        else{
    	        	diffHr=""+diffHours;
    	        }
    	        vm1.timeUnit=diffDay+" days "+diffHr+" hours "+diffMinutes+" minutes ago";
    	        vm1.diffDays=diffDay+" + days";
    	        }
    	        else if(diffInDays == 0 && diffHours == 0){
    	        	if(diffMinutes == 1){
    	        		vm1.diffDays=diffMinutes+" minute ago";
            	        vm1.timeUnit=diffMinutes+" minute ago";
    	        	}else{
    	        	vm1.diffDays=diffMinutes+" minutes ago";
        	        vm1.timeUnit=diffMinutes+" minutes ago";
    	        	}
        	     
        	        }
    	        else{
    	        	
    	        	 if(diffHours <10){
    	    	        	diffHr=""+diffHours;
    	    	        }
    	    	        else{
    	    	        	diffHr=""+diffHours;
    	    	        }
    	        	vm1.timeUnit=diffHr+" hours "+diffMinutes+" minutes ago";
    	        	vm1.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    	        }
	    		list.add(vm1);		
	    		
	    	}
	    	
	    	for(TradeIn sc: tradeIns){
	    		RequestInfoVM vm1=new RequestInfoVM();
	    		vm1.name=sc.firstName+" "+sc.lastName;
	    		vm1.typeOfLead="Premium";
	    		vm1.leadTypeForNotif="Premium Lead";
	    		vm1.type="Trade In";
	    		vm1.id=sc.id;
	    		vm1.notifFlag=sc.notifFlag;
	    		String imagePath=null;
	    		if(sc.vin != null){
	    			VehicleImage image=VehicleImage.getDefaultImage(sc.vin);
	    			if(image != null){
	    				imagePath=image.thumbPath;
	    			}
	    		}
	    		vm1.imagePath=imageUrlPath+imagePath;
    	        Date dt1=null;
    	        Date dt2=null;
    	        try {
    	        	dt1 =currD;
    	            //dt2 = sc.tradeTime;
    	            String dat1=df1.format(sc.tradeTime);
    	            dt2=df2.parse(dat1);
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }

    	        // Get msec from each, and subtract.
    	        long diff = dt1.getTime() - dt2.getTime();
    	        vm1.timeDiff=diff;
    	        long diffSeconds = diff / 1000 % 60;
    	        long diffMinutes = diff / (60 * 1000) % 60;
    	        long diffHours = diff / (60 * 60 * 1000)% 24;
    	        int diffInh = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 ));
    	        int diffInDays = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 * 24));
    	        String diffDay=null;
    	        String diffHr=null;
    	        if(diffInDays != 0){
    	        if(diffInDays <10){
    	        	diffDay=""+diffInDays;
    	        }
    	        else{
    	        	diffDay=""+diffInDays;
    	        }
    	        if(diffHours <10){
    	        	diffHr=""+diffHours;
    	        }
    	        else{
    	        	diffHr=""+diffHours;
    	        }
    	        vm1.timeUnit=diffDay+" days "+diffHr+" hours "+diffMinutes+" minutes ago";
    	        vm1.diffDays=diffDay+" + days";
    	        }
    	        else if(diffInDays == 0 && diffHours == 0){
    	        	if(diffMinutes == 1){
    	        		vm1.diffDays=diffMinutes+" minute ago";
            	        vm1.timeUnit=diffMinutes+" minute ago";
    	        	}else{
    	        	vm1.diffDays=diffMinutes+" minutes ago";
        	        vm1.timeUnit=diffMinutes+" minutes ago";
    	        	}
        	     
        	        }
    	        else{
    	        	
    	        	 if(diffHours <10){
    	    	        	diffHr=""+diffHours;
    	    	        }
    	    	        else{
    	    	        	diffHr=""+diffHours;
    	    	        }
    	        	vm1.timeUnit=diffHr+" hours "+diffMinutes+" minutes ago";
    	        	vm1.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    	        }
	    		
	    		
	    		
	    		list.add(vm1);		
	    		
	    	}
	    	for(RequestMoreInfo sc: reInfos){
	    		RequestInfoVM vm1=new RequestInfoVM();
	    		vm1.name=sc.name;
	    		vm1.typeOfLead="Premium";
	    		vm1.leadTypeForNotif="Premium Lead";
	    		vm1.id=sc.id;
	    		vm1.notifFlag=sc.notifFlag;
	    		vm1.type="Request More Info";
	    		String imagePath=null;
	    		if(sc.vin != null){
	    			VehicleImage image=VehicleImage.getDefaultImage(sc.vin);
	    			if(image != null){
	    				imagePath=image.thumbPath;
	    			}
	    		}
	    		vm1.imagePath=imageUrlPath+imagePath;
	    		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:a");
    	        Date dt1=null;
    	        Date dt2=null;
    	        try {
    	        	dt1 =currD;
    	           // dt2 = sc.requestTime;
    	        	 String dat1=df1.format(sc.requestTime);
     	            dt2=df2.parse(dat1);
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }

    	        // Get msec from each, and subtract.
    	        long diff = dt1.getTime() - dt2.getTime();
    	        vm1.timeDiff=diff;
    	        long diffSeconds = diff / 1000 % 60;
    	        long diffMinutes = diff / (60 * 1000) % 60;
    	        long diffHours = diff / (60 * 60 * 1000)% 24;
    	        int diffInh = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 ));
    	        int diffInDays = (int) ((dt1.getTime() - dt2.getTime()) / (1000 * 60 * 60 * 24));
    	        String diffDay=null;
    	        String diffHr=null;
    	        if(diffInDays != 0){
    	        if(diffInDays <10){
    	        	diffDay=""+diffInDays;
    	        }
    	        else{
    	        	diffDay=""+diffInDays;
    	        }
    	        if(diffHours <10){
    	        	diffHr=""+diffHours;
    	        }
    	        else{
    	        	diffHr=""+diffHours;
    	        }
    	        vm1.timeUnit=diffDay+" days "+diffHr+" hours "+diffMinutes+" minutes ago";
    	        vm1.diffDays=diffDay+" + days";
    	        }
    	        else if(diffInDays == 0 && diffHours == 0){
    	        	if(diffMinutes == 1){
    	        		vm1.diffDays=diffMinutes+" minute ago";
            	        vm1.timeUnit=diffMinutes+" minute ago";
    	        	}else{
    	        	vm1.diffDays=diffMinutes+" minutes ago";
        	        vm1.timeUnit=diffMinutes+" minutes ago";
    	        	}
    	     
    	        }
    	        else{
    	        	
    	        	 if(diffHours <10){
    	    	        	diffHr="0"+diffHours;
    	    	        }
    	    	        else{
    	    	        	diffHr=""+diffHours;
    	    	        }
    	        	vm1.timeUnit=diffHr+" hours "+diffMinutes+" minutes ago";
    	        	vm1.diffDays=diffHr+" hours "+diffMinutes+" minutes ago";
    	        }
	    		
	    		list.add(vm1);		
	    		
	    	}
	    	
	    	vm.userType = user.role;
	    	
	    	return ok(Json.toJson(list));
    	}
    }
    
    public static Result  getLocationName(Long locationId)     
    {
    	Location location=Location.findById(locationId);
		return ok(location.getName());
    	
    }
    
    public static Result uploadLogoFile() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    
    public static Result getTradeInDataById(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    
   
    public static void findReminderPopupFunction(List<RequestInfoVM> actionVM){
    	
    	SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
      	 DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
      	 DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
      	 AuthUser user = (AuthUser) getLocalUser();
           DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
           SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
           Date currD = new Date();
           Date currentDate = null;
           Date aftHrDate = null;
           Date aftDay = null;
           Date aftHrDate1 = null;
           Date aftDay1 = null;
           Date infoDate = null;
           Date datec = null;
           
           Date lessDay = DateUtils.addDays(currD, -1);
           
        //   List<NoteVM> actionVM = new ArrayList<NoteVM>();
           
           
           
           List<ScheduleTest> list = ScheduleTest.findAllByServiceTestPopup(user,lessDay);
           
       	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmGraLeadsToPopUp(user,lessDay);
       	List<TradeIn> tradeIns = TradeIn.findByConfirmGraLeadsToPopup(user,lessDay);
       	
       	//fillLeadsData(list, requestMoreInfos, tradeIns, infoVMList);
       	
       	for(ScheduleTest scTest:list){
          	 
       		RequestInfoVM acti = new RequestInfoVM();
          	 AuthUser aUser = AuthUser.findById(scTest.assignedTo.id);
          	 Location location = Location.findById(aUser.location.id);
          	
          	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
               String IST = df2.format(currD);
              
               Date istTimes = null;
   			try {
   				istTimes = df1.parse(IST);
   			} catch (ParseException e1) {
   				// TODO Auto-generated catch block
   				e1.printStackTrace();
   			}
          	
          	 
          	 String cDate = df.format(istTimes);
               String cTime = parseTime.format(istTimes);
               String crD =    df1.format(istTimes);
      		 
               try {
              	 currentDate = df1.parse(crD);
              	 datec = df.parse(cDate);
              	 aftHrDate = DateUtils.addHours(currentDate, 1);
              	 aftDay = DateUtils.addHours(currentDate, 24);
              	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
              	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
      		} catch (Exception e) {
      			e.printStackTrace();
      		}
          	 
          	 try {
          		 String str = df.format(scTest.confirmDate) +" "+parseTime.format(scTest.confirmTime);
          		 infoDate = df1.parse(str);

          		            	 
          		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
              		 if(scTest.meetingStatus == null){
              			acti.action = "Test drive reminder";
              			acti.notes = "You have a test drive scheduled in 1 hour ";
          			 }else if(scTest.meetingStatus.equals("meeting")){
          				acti.action = "Meeting reminder";
          				acti.notes = "You have a meeting scheduled in 1 hour ";
          			 }
              		 
              		acti.id = scTest.id;
           		Vehicle vehicle = Vehicle.findByVinAndStatus(scTest.vin);
           		acti.vin = scTest.vin;
           		if(vehicle != null) {
           			acti.model = vehicle.model;
           			acti.make = vehicle.make;
           			acti.stock = vehicle.stock;
           			acti.year = vehicle.year;
           			acti.mileage = vehicle.mileage;
           			acti.price = vehicle.price;
           		}
           		
           		acti.name = scTest.name;
           		acti.phone = scTest.phone;
           		acti.email = scTest.email;
           			
           		acti.howContactedUs = scTest.contactedFrom;
           		acti.howFoundUs = scTest.hearedFrom;
           		acti.custZipCode = scTest.custZipCode;
           		acti.enthicity = scTest.enthicity;
           		acti.status =scTest.leadStatus;
           		
           		acti.typeOfLead = "Schedule Test Drive";
           		findSchedulParentChildAndBro(actionVM, scTest, dfs, acti);
           		
              		 
              		 
              		 actionVM.add(acti);
              	 }
          		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
              		 if(scTest.meetingStatus == null){
              			acti.action =  "Test drive reminder";
              			acti.notes = "You have a test drive scheduled in 24 hours ";
          			 }else if(scTest.meetingStatus.equals("meeting")){
          				acti.action = "Meeting reminder";
          				acti.notes =  "You have a meeting scheduled in 24 hours ";
          			 }
              		 
              		 
              		acti.id = scTest.id;
           		Vehicle vehicle1 = Vehicle.findByVinAndStatus(scTest.vin);
           		acti.vin = scTest.vin;
           		if(vehicle1 != null) {
           			acti.model = vehicle1.model;
           			acti.make = vehicle1.make;
           			acti.stock = vehicle1.stock;
           			acti.year = vehicle1.year;
           			acti.mileage = vehicle1.mileage;
           			acti.price = vehicle1.price;
           		}
           		
           		acti.name = scTest.name;
           		acti.phone = scTest.phone;
           		acti.email = scTest.email;
           			
           		acti.howContactedUs = scTest.contactedFrom;
           		acti.howFoundUs = scTest.hearedFrom;
           		acti.custZipCode = scTest.custZipCode;
           		acti.enthicity = scTest.enthicity;
           		acti.status =scTest.leadStatus;
           		
           		acti.typeOfLead = "Schedule Test Drive";
           		findSchedulParentChildAndBro(actionVM, scTest, dfs, acti);
           		
           		Date curDate = null;
            	Date curDate1 = null;
            	Date curDateNew = null;
            	List<RequestInfoVM> rList = new ArrayList<>();
            	Date date=new Date();
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        		DateFormat newdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        		DateFormat format1 = new SimpleDateFormat("HH:mm:a");
        		 DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        			Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
        			df2.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			df11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			String date1=df2.format(date);
        			String dateNew=df11.format(date);
        			String date11="00:00:AM";
        			
        			try {
        				curDate1=newdf1.parse(dateNew);
        				curDate = formatter.parse(date1);
        				curDateNew=format1.parse(date11);
        			} catch (ParseException e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        			}
        			
        			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        			  long diff = curDate1.getTime() - curDateNew.getTime();
          	        long diffSeconds = diff / 1000 % 60;
          	        long diffMinutes = diff / (60 * 1000) % 60;
          	        	long diffHours = diff / (60 * 60 * 1000)% 24;
          	        	if(diffHours != 0){
          	        		acti.diffDays=diffHours+" hours"+diffMinutes+" minutes ago";
        					}
        					else{
        						acti.diffDays=diffMinutes+" minutes ago";
        					}
           		
           		
           		
              		 actionVM.add(acti);
              	 }
   			} catch (Exception e) {
   				e.printStackTrace();
   			}
          	 
          	
           }
           
           for(RequestMoreInfo rInfo:requestMoreInfos){
           	
           	RequestInfoVM acti = new RequestInfoVM();
          	 AuthUser emailUser = AuthUser.findById(rInfo.assignedTo.id);
          	 
          	 Location location = Location.findById(emailUser.location.id);
          	
          	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
               String IST = df2.format(currD);
              
               Date istTimes = null;
   			try {
   				istTimes = df1.parse(IST);
   			} catch (ParseException e1) {
   				// TODO Auto-generated catch block
   				e1.printStackTrace();
   			}
          	
          	 
          	 String cDate = df.format(istTimes);
               String cTime = parseTime.format(istTimes);
               String crD =    df1.format(istTimes);
      		 
               try {
              	 currentDate = df1.parse(crD);
              	 datec = df.parse(cDate);
              	 aftHrDate = DateUtils.addHours(currentDate, 1);
              	 aftDay = DateUtils.addHours(currentDate, 24);
              	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
              	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
      		} catch (Exception e) {
      			e.printStackTrace();
      		}
          	 
          	 
          	 try {
          		 String str = df.format(rInfo.confirmDate) +" "+parseTime.format(rInfo.confirmTime);
          		 infoDate = df1.parse(str);
          		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
          			acti.action = "Test drive reminder";
          			acti.notes = "You have a test drive scheduled in 1 hour ";
          			
          			acti.id = rInfo.id;
           		Vehicle vehicle = Vehicle.findByVinAndStatus(rInfo.vin);
           		acti.vin = rInfo.vin;
           		if(vehicle != null) {
           			acti.model = vehicle.model;
           			acti.make = vehicle.make;
           			acti.stock = vehicle.stock;
           			acti.year = vehicle.year;
           			acti.mileage = vehicle.mileage;
           			acti.price = vehicle.price;
           		}
           		
           		acti.name = rInfo.name;
           		acti.phone = rInfo.phone;
           		acti.email = rInfo.email;
           			
           		acti.howContactedUs = rInfo.contactedFrom;
           		acti.howFoundUs = rInfo.hearedFrom;
           		acti.custZipCode = rInfo.custZipCode;
           		acti.enthicity = rInfo.enthicity;
           		acti.status =rInfo.leadStatus;
           		
           		//acti.typeOfLead = "Request More Info";
           		LeadType lType = null;
	    		if(rInfo.isContactusType != null){
		    		if(!rInfo.isContactusType.equals("contactUs")){
		    			lType = LeadType.findById(Long.parseLong(rInfo.isContactusType));
		    		}else{
		    			lType = LeadType.findByName(rInfo.isContactusType);
		    		}
		    		acti.typeOfLead = lType.leadName;
		    		findCustomeData(rInfo.id,acti,lType.id);
	    		}else{
	    			acti.typeOfLead = "Request More Info";
	    			findCustomeData(rInfo.id,acti,1L);
	    		}
           		
           		findRequestParentChildAndBro(actionVM, rInfo, dfs, acti);
          		 actionVM.add(acti);
          		 }
          		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
          			acti.action =  "Test drive reminder";
          			acti.notes = "You have a test drive scheduled in 24 hours ";
          			
          			
          			acti.id = rInfo.id;
           		Vehicle vehicle = Vehicle.findByVinAndStatus(rInfo.vin);
           		acti.vin = rInfo.vin;
           		if(vehicle != null) {
           			acti.model = vehicle.model;
           			acti.make = vehicle.make;
           			acti.stock = vehicle.stock;
           			acti.year = vehicle.year;
           			acti.mileage = vehicle.mileage;
           			acti.price = vehicle.price;
           		}
           		
           		acti.name = rInfo.name;
           		acti.phone = rInfo.phone;
           		acti.email = rInfo.email;
           			
           		acti.howContactedUs = rInfo.contactedFrom;
           		acti.howFoundUs = rInfo.hearedFrom;
           		acti.custZipCode = rInfo.custZipCode;
           		acti.enthicity = rInfo.enthicity;
           		acti.status =rInfo.leadStatus;
           		
           		acti.typeOfLead = "Request More Info";
           		
           		LeadType lType = null;
	    		if(rInfo.isContactusType != null){
		    		if(!rInfo.isContactusType.equals("contactUs")){
		    			lType = LeadType.findById(Long.parseLong(rInfo.isContactusType));
		    		}else{
		    			lType = LeadType.findByName(rInfo.isContactusType);
		    		}
		    		acti.typeOfLead = lType.leadName;
		    		findCustomeData(rInfo.id,acti,lType.id);
	    		}else{
	    			acti.typeOfLead = "Request More Info";
	    			findCustomeData(rInfo.id,acti,1L);
	    		}
           		findRequestParentChildAndBro(actionVM, rInfo, dfs, acti);
           		
           		
           		
           		Date curDate = null;
            	Date curDate1 = null;
            	Date curDateNew = null;
            	List<RequestInfoVM> rList = new ArrayList<>();
            	Date date=new Date();
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        		DateFormat newdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        		DateFormat format1 = new SimpleDateFormat("HH:mm:a");
        		 DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        			Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
        			df2.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			df11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			String date1=df2.format(date);
        			String dateNew=df11.format(date);
        			String date11="00:00:AM";
        			
        			try {
        				curDate1=newdf1.parse(dateNew);
        				curDate = formatter.parse(date1);
        				curDateNew=format1.parse(date11);
        			} catch (ParseException e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        			}
        			
        			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        			  long diff = curDate1.getTime() - curDateNew.getTime();
          	        long diffSeconds = diff / 1000 % 60;
          	        long diffMinutes = diff / (60 * 1000) % 60;
          	        	long diffHours = diff / (60 * 60 * 1000)% 24;
          	        	if(diffHours != 0){
          	        		acti.diffDays=diffHours+" hours"+diffMinutes+" minutes ago";
        					}
        					else{
        						acti.diffDays=diffMinutes+" minutes ago";
        					}
           		
           		
          		 actionVM.add(acti);
          		 }
   			} catch (Exception e) {
   				e.printStackTrace();
   			}
           }
           
           for(TradeIn tInfo:tradeIns){
           	RequestInfoVM acti = new RequestInfoVM();
          	 AuthUser emailUser = AuthUser.findById(tInfo.assignedTo.id);
          	 
          	 Location location = Location.findById(emailUser.location.id);
          	
          	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
               String IST = df2.format(currD);
              
               Date istTimes = null;
   			try {
   				istTimes = df1.parse(IST);
   			} catch (ParseException e1) {
   				// TODO Auto-generated catch block
   				e1.printStackTrace();
   			}
          	
          	 
          	 String cDate = df.format(istTimes);
               String cTime = parseTime.format(istTimes);
               String crD =    df1.format(istTimes);
      		 
               try {
              	 currentDate = df1.parse(crD);
              	 datec = df.parse(cDate);
              	 aftHrDate = DateUtils.addHours(currentDate, 1);
              	 aftDay = DateUtils.addHours(currentDate, 24);
              	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
              	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
      		} catch (Exception e) {
      			e.printStackTrace();
      		}
          	 
          	 
          	 try {
          		 String str = df.format(tInfo.confirmDate) +" "+parseTime.format(tInfo.confirmTime);
          		 infoDate = df1.parse(str);
          		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
           			acti.action = "Test drive reminder";
              			acti.notes = "You have a test drive scheduled in 1 hour ";
              			
              			acti.id = tInfo.id;
               		Vehicle vehicle = Vehicle.findByVinAndStatus(tInfo.vin);
               		acti.vin = tInfo.vin;
               		if(vehicle != null) {
               			acti.model = vehicle.model;
               			acti.make = vehicle.make;
               			acti.stock = vehicle.stock;
               			acti.year = vehicle.year;
               			acti.mileage = vehicle.mileage;
               			acti.price = vehicle.price;
               		}
               		
               		acti.name = tInfo.firstName;
               		acti.phone = tInfo.phone;
               		acti.email = tInfo.email;
               			
               		acti.howContactedUs = tInfo.contactedFrom;
               		acti.howFoundUs = tInfo.hearedFrom;
               		acti.custZipCode = tInfo.custZipCode;
               		acti.enthicity = tInfo.enthicity;
               		acti.status =tInfo.leadStatus;
               		
               		acti.typeOfLead = "Trade-In Appraisal";
               		findTreadParentChildAndBro(actionVM, tInfo, dfs, acti);
              		 actionVM.add(acti);
          		 }
          		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
          			acti.action =  "Test drive reminder";
          			acti.notes = "You have a test drive scheduled in 24 hours ";
          			
          			
          			acti.id = tInfo.id;
           		Vehicle vehicle = Vehicle.findByVinAndStatus(tInfo.vin);
           		acti.vin = tInfo.vin;
           		if(vehicle != null) {
           			acti.model = vehicle.model;
           			acti.make = vehicle.make;
           			acti.stock = vehicle.stock;
           			acti.year = vehicle.year;
           			acti.mileage = vehicle.mileage;
           			acti.price = vehicle.price;
           		}
           		
           		acti.name = tInfo.firstName;
           		acti.phone = tInfo.phone;
           		acti.email = tInfo.email;
           			
           		acti.howContactedUs = tInfo.contactedFrom;
           		acti.howFoundUs = tInfo.hearedFrom;
           		acti.custZipCode = tInfo.custZipCode;
           		acti.enthicity = tInfo.enthicity;
           		acti.status =tInfo.leadStatus;
           		
           		acti.typeOfLead = "Trade-In Appraisal";
           		findTreadParentChildAndBro(actionVM, tInfo, dfs, acti);
           		
           		
           		Date curDate = null;
            	Date curDate1 = null;
            	Date curDateNew = null;
            	List<RequestInfoVM> rList = new ArrayList<>();
            	Date date=new Date();
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        		DateFormat newdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        		DateFormat format1 = new SimpleDateFormat("HH:mm:a");
        		 DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
        			Location location1 = Location.findById(Long.valueOf(session("USER_LOCATION")));
        			df2.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			df11.setTimeZone(TimeZone.getTimeZone(location1.time_zone));
        			String date1=df2.format(date);
        			String dateNew=df11.format(date);
        			String date11="00:00:AM";
        			
        			try {
        				curDate1=newdf1.parse(dateNew);
        				curDate = formatter.parse(date1);
        				curDateNew=format1.parse(date11);
        			} catch (ParseException e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        			}
        			
        			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        			  long diff = curDate1.getTime() - curDateNew.getTime();
          	        long diffSeconds = diff / 1000 % 60;
          	        long diffMinutes = diff / (60 * 1000) % 60;
          	        	long diffHours = diff / (60 * 60 * 1000)% 24;
          	        	if(diffHours != 0){
          	        		acti.diffDays=diffHours+" hours"+diffMinutes+" minutes ago";
        					}
        					else{
        						acti.diffDays=diffMinutes+" minutes ago";
        					}
           		
           		
          		 actionVM.add(acti);
          		 }
   			} catch (Exception e) {
   				e.printStackTrace();
   			}
           }
    }
    
    public static Result getReminderPopup(){
    	
    	List<RequestInfoVM> actionVM= new ArrayList<RequestInfoVM>();
    	findReminderPopupFunction(actionVM);
        
        
   	return ok(Json.toJson(actionVM));
    }
    
    public static Result showPdf(Long id) {
    	TradeIn tradeIn = TradeIn.findById(id);
    	File file = new File(rootDir+tradeIn.pdfPath);
    	response().setContentType("application/pdf");
    	response().setHeader("Content-Disposition", "inline; filename=tradeIn.pdf");
		return ok(file);
    }
    
    public static Result forgotPass(){
    	String email = Form.form().bindFromRequest().get("email");
    	AuthUser user = AuthUser.find.where().eq("email", email).findUnique();
    	if(user != null) {
			
    	
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
	 		   
	  			Message feedback = new MimeMessage(session);
	  			feedback.setFrom(new InternetAddress(emailUser));
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
    
    
    
   /* private static void makeToDo(String vin) {
    	AuthUser user = (AuthUser) getLocalUser();
    	ToDo todo = new ToDo();
		Vehicle vobj = Vehicle.findByVinAndStatus(vin);
		if(vobj != null){
			todo.task = "Follow up with the client about test Drive for"+vobj.make+" "+vobj.model+" ("+vobj.vin+")";
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
			todo.save();
		}
   
    }*/
	
	 private static void saveBilndVm(LeadVM leadVM,MultipartFormData bodys,Form<LeadVM> form) {
    	 
    	 JSONArray jArr,jArr1;
    	 List<VehicleVM> vmList = new ArrayList<>();
    	 List<KeyValueDataVM> vmList1 = new ArrayList<>();
		try {
			form.data().get("stockWiseData");
			System.out.println(form.data().get("stockWiseData"));
			jArr = new JSONArray(form.data().get("stockWiseData"));
			
			for (int i=0; i < jArr.length(); i++) {
				VehicleVM vm = new VehicleVM();
				JSONObject jsonObj = jArr.getJSONObject(i);
				//vm.id = Long.parseLong(String.valueOf(jsonObj.get("id")));
				vm.imgId = String.valueOf(jsonObj.get("imgId")); 
				//vm.title = String.valueOf(jsonObj.get("title"));
				vm.vin =  String.valueOf(jsonObj.get("vin"));
				vm.stockNumber = String.valueOf(jsonObj.get("stockNumber"));
				vmList.add(vm);
				leadVM.stockWiseData.add(vm);
			}
			
			jArr1 = new JSONArray(form.data().get("customData"));
			
			for (int i=0; i < jArr1.length(); i++) {
				KeyValueDataVM vm1 = new KeyValueDataVM();
				JSONObject jsonObj1 = jArr1.getJSONObject(i);
				vm1.key = String.valueOf(jsonObj1.get("key"));
				vm1.value = String.valueOf(jsonObj1.get("value"));
				vmList1.add(vm1);
				leadVM.customData.add(vm1);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		leadVM.id = form.data().get("id");
		leadVM.custEmail = form.data().get("custEmail");
		leadVM.custName = form.data().get("custName");
		leadVM.custNumber = form.data().get("custNumber");
		leadVM.custZipCode = form.data().get("custZipCode");
		leadVM.prefferedContact = form.data().get("prefferedContact");
		leadVM.leadType =form.data().get("leadType");
		
         
    }
    
    private static void sendMailpremium() {
    	/*--------------------------------*/
		
		AuthUser aUser = AuthUser.getlocationAndManagerOne(Location.findById(Long.valueOf(session("USER_LOCATION"))));
		final String username = emailUsername;
		final String password = emailPassword;
		
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
				return new PasswordAuthentication(username, password);
			}
		});
		
		  
		    try {  
		     MimeMessage message = new MimeMessage(session);  
	    		try{
		     message.setFrom(new InternetAddress(emailUser,emailName));  
	    		}catch(UnsupportedEncodingException e){
	    			e.printStackTrace();
	    		}
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(aUser.communicationemail));  
		     message.setSubject("Premium Leads");  
		     message.setText("Premium Request has been submitted");  
		       
		     Transport.send(message);  
		  
		     System.out.println("message sent successfully...");  
		   
		     } catch (MessagingException e) {e.printStackTrace();} 
		
		
		/*------------------------------------*/
    }
    
private static void cancelTestDriveMail(Map map) {
    	
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
		props.put("mail.smtp.host",gmail);
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
    		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(map.get("email").toString()));
			message.setSubject("TEST DRIVE CANCELLED");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/testDriveCancelled_HTML.vm"); 
	        VelocityContext context = new VelocityContext();
	        /*String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	       
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
	        
	        String monthName = months[month-1];*/
	        context.put("hostnameUrl", imageUrlPath);
	        context.put("siteLogo", logo.logoImagePath);
	        /*context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        context.put("confirmTime", map.get("confirmTime"));*/
	        
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
	        context.put("name", map.get("uname"));
	        context.put("email", map.get("uemail"));
	       // context.put("phone",  map.get("uphone"));
	        String phoneNum=map.get("uphone").toString();
	        String firstThreeDigit=phoneNum;
	        firstThreeDigit=firstThreeDigit.substring(0, 3);
	        String secondThreeDigit=phoneNum;
	        secondThreeDigit=secondThreeDigit.substring(3, 6);
	        String thirdThreeDigit=phoneNum;
	        thirdThreeDigit=thirdThreeDigit.substring(6, 10);
	        String phn = "("+firstThreeDigit+")"+secondThreeDigit+"-"+thirdThreeDigit;
	        context.put("phon", phn);
	     //   context.put("secondThreeDigit", secondThreeDigit);
	    //    context.put("thirdThreeDigit", thirdThreeDigit);
	        
	        /*String weather= map.get("CnfDateNature").toString();
	        String arr1[] = weather.split("&");
	        String nature=arr1[0];
	        String temp=arr1[1];
	        context.put("nature",nature);
	        context.put("temp", temp);*/
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
    
    private static void sendMail(Map map) {
    	
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
	 		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
	 		}catch(UnsupportedEncodingException e){
	 			e.printStackTrace();
	 		}
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
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/testDriveconfirmatioin.vm"); 
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
             context.put("typeofVehicle", vehicle.typeofVehicle);
             if( map.get("clientName")!= null){
   	        	
           	  context.put("clientName", map.get("clientName"));
 	        	 
 	        }
 	        else{
 	        	 context.put("clientName","");
 	        }
	        context.put("name", map.get("uname"));
	        context.put("email", map.get("uemail"));
	        context.put("phone",  map.get("uphone"));
	        String weather= map.get("weatherValue").toString();
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
    
    public static Result sendPdfEmail() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
    		AuthUser users = (AuthUser) getLocalUser();
    		MultipartFormData body = request().body().asMultipartFormData();
    		
    		EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
    		String emailName=details.name;	
    		String port=details.port;
    		String gmail=details.host;
    	final	String emailUser=details.username;
    	final	String emailPass=details.passward;
	    	AuthUser userObj = new AuthUser();
	    	UserVM vm = form.get();
	    	//List<String> aa= new ArrayList<>();
	    		   CustomerPdf iPdf = null;
	    		   
	    		   
	    		Properties props = new Properties();
		 		props.put("mail.smtp.auth", "true");
		 		props.put("mail.smtp.starttls.enable", "true");
		 		props.put("mail.smtp.host", gmail);
		 		props.put("mail.smtp.port",port);
		 		System.out.println(">>port"+port);
		    System.out.println(">>gmailhost"+gmail);
		    System.out.println(">>>>emailUser"+emailUser);
		    System.out.println(">>>>emailPass"+emailPass);
		 		Session session = Session.getInstance(props,
		 		  new javax.mail.Authenticator() {
		 			protected PasswordAuthentication getPasswordAuthentication() {
		 				return new PasswordAuthentication(emailUser,emailPass);
		 			}
		 		  });
		  
		 		try{
		 			System.out.println("emailUsername"+emailUsername);
		 			System.out.println("emailPassword"+emailPassword);
		  			Message message = new MimeMessage(session);
		  			try {
		  				System.out.println(">>>>>>>"+emailName);
						message.setFrom(new InternetAddress(emailUser,emailName));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		  			message.setRecipients(Message.RecipientType.TO,
		  			InternetAddress.parse(vm.email));
		  			
		  			message.setSubject("Requested Documents");	  			
		  			Multipart multipart = new MimeMultipart();
	    			BodyPart messageBodyPart = new MimeBodyPart();
	    			messageBodyPart = new MimeBodyPart();
	    			
	    			VelocityEngine ve = new VelocityEngine();
	    			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
	    			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
	    			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
	    			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	    			ve.init();
	    			
	    			Template t = ve.getTemplate("/public/emailTemplate/PDFsent_HTML.html"); 
	    	        VelocityContext context = new VelocityContext();
	    	        context.put("hostnameUrl", imageUrlPath);
	    	        //context.put("siteLogo", logo.logoImagePath);
	    	        context.put("title", vm.title);
	    	        context.put("text", vm.text);
	    	        context.put("name", users.fullName());
	    	        context.put("email", users.email);
	    	        context.put("phone", users.phone);
	    	        
	    	        StringWriter writer = new StringWriter();
	    	        t.merge( context, writer );
	    	        String content = writer.toString(); 
	    			
	    			messageBodyPart.setContent(content, "text/html");
	    			String nameOfDocument= null;
	    			StringBuffer output = new StringBuffer(110);
	    			if(vm.pdfIds != null){
	    			for(Long ls:vm.pdfIds){
	 	    		   iPdf = CustomerPdf.findPdfById(ls);  
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
	    			 nameOfDocument=iPdf.pdf_name;
	    			 
	    			   output.append(iPdf.pdf_name);
	    			   output.append(", ");
	    			   
	    			 
	 	    	   }
	    			
	    			}
	    			if(vm.vin != null){
	    				
	    				Vehicle vehicle=Vehicle.findByVin(vm.vin);
	    				  String PdfFile = rootDir + File.separator +vehicle.pdfBrochurePath;
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
	    			
	    			String nameDoc= output.toString();
	    			String actionTitle="Followed up with the following documents: "+nameDoc;
	    			String action="Other";
	    			Long ids=(long)vm.id;
	    			saveNoteOfUser(ids,vm.typeOfLead,actionTitle,action);
	 	    		
	    			
	    			
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
	 	List<Vehicle> vehicle1 = Vehicle.findByLocationAndSold(Long.parseLong(session("USER_LOCATION")));
		int flag1=0;
		lDataVM.flagForBestSaleIcon = 0;
		if(vehicle1 != null){
	 	for(Vehicle vehicle:vehicle1){
	 		if((vehicle != null && vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(startD) || vehicle.soldDate.equals(endD)) {
	 			flag1=1;
	 			lDataVM.flagForBestSaleIcon=flag1;
	 			break;
	 		}
	 	}
	 			
	 		}
	
		
   	int requestLeadCount = 0;
   	int scheduleLeadCount = 0;
   	int tradeInLeadCount = 0;
   	
   	int requestLeadCount1 = 0;
   	int scheduleLeadCount1 = 0;
   	int tradeInLeadCount1 = 0;
   	
   	int requestLeadCountTest = 0;
   	int scheduleLeadCountTest = 0;
   	int tradeInLeadCountTest = 0;
   	
   	List<RequestMoreInfo> rInfo = null;
   	List<ScheduleTest> sList = null;
   	List<TradeIn> tradeIns = null;
   	List<RequestMoreInfo> rInfoAll = null;
   	List<ScheduleTest> sListAll = null;
   	List<TradeIn> tradeInsAll = null;
   	
   	List<RequestMoreInfo> rInfoTest = null;
   	List<ScheduleTest> sListTest = null;
   	List<TradeIn> tradeInsTest = null;
   	
   	if(users.role.equals("Manager") && locOrPer.equals("location")){
   		rInfo = RequestMoreInfo.findAllSeenLocationSch(locationId);
   		sList = ScheduleTest.findAllAssignedLocation(locationId);
   		tradeIns = TradeIn.findAllSeenLocationSch(locationId);
   		
   		rInfoAll = RequestMoreInfo.findByLocationNotOpenLead(locationId);
   		sListAll = ScheduleTest.findByLocationNotOpenLead(locationId);
   		tradeInsAll = TradeIn.findByLocationNotOpenLead(locationId);
   		
   	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
   		rInfo = RequestMoreInfo.findAllSeenSch(users);
   		sList = ScheduleTest.findAllAssigned(users);
   		tradeIns = TradeIn.findAllSeenSch(users);
   		
   		rInfoAll = RequestMoreInfo.findAllByNotOpenLead(users);
   		sListAll = ScheduleTest.findAllByNotOpenLead(users);
   		tradeInsAll = TradeIn.findAllByNotOpenLead(users);
   	}
   	
   	for(RequestMoreInfo rMoreInfo:rInfo){
   		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
   			requestLeadCount++;
   		}
   	}
   	
   	
   	for(ScheduleTest sTest:sList){
   		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
   			scheduleLeadCount++;
   		}
   	}

   	for(TradeIn tIn:tradeIns){
   		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
   				tradeInLeadCount++;
   		}
   	}
   	
   	for(RequestMoreInfo rMoreInfo:rInfoAll){
   		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
   			requestLeadCount1++;
   		}
   	}
   	
   	
   	for(ScheduleTest sTest:sListAll){
   	if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
   			scheduleLeadCount1++;
   	}
   	}

   	for(TradeIn tIn:tradeInsAll){
   	if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
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
   	
   	if(users.role.equals("Sales Person") || locOrPer.equals("person")){
   		rInfoTest = RequestMoreInfo.testDriveForSalePerson(users);
   		sListTest = ScheduleTest.testDriveForSalePerson(users);
   		tradeInsTest = TradeIn.testDriveForSalePerson(users);
   		for(RequestMoreInfo rMoreInfo:rInfoTest){
   	   		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
   	   			requestLeadCountTest++;
   	   		}
   	   	}
   	   	
   	   	
   	   	for(ScheduleTest sTest:sListTest){
   	   	if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
   	   			scheduleLeadCountTest++;
   	   	}
   	   	}

   	   	for(TradeIn tIn:tradeInsTest){
   	   	if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
   					tradeInLeadCountTest++;
   	   		}
   	   	}
   		
   	}
   	

   	int countTestDrives = requestLeadCountTest + scheduleLeadCountTest + tradeInLeadCountTest;
   	
   	lDataVM.countTestDrives=countTestDrives;
   	
   	
   	
   	Integer monthPriceCount = 0;
   	Integer pricecount = 0;
   	int saleCarCount = 0;
   	if(users.role.equals("Manager") && locOrPer.equals("location")){
   		List<Vehicle> vList = Vehicle.findByLocationAndSold(location.id);
       	
       	for(Vehicle vehList:vList){
       		//	if(vehList.soldDate.after(timeBack)) {
       		if((vehList.soldDate.after(startD) && vehList.soldDate.before(endD)) || vehList.soldDate.equals(endD) || vehList.soldDate.equals(startD)){
           			saleCarCount++;
           			pricecount = pricecount + vehList.price;
           		}
       		if(monthCal.equals(onlyMonth.format(vehList.soldDate))){
       			monthPriceCount = monthPriceCount + vehList.price;
       		}
       	}
       	if(countLeads1 != 0 ){
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
   			if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD) || vehicle.soldDate.equals(startD)) {
       			saleCarCount++;
       			pricecount = pricecount + vehicle.price;
				}
				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
					monthPriceCount = monthPriceCount + vehicle.price;
				}
			}
   		if(countLeads1 != 0){
   			double sucessCount= (double)saleCarCount/(double)countLeads1*100;
   	   		lDataVM.successRate = (int) sucessCount;
   		}else{
   	   		lDataVM.successRate = 0;
   		}
   		
   		
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
   			if((vehicle.soldDate.after(startD) && vehicle.soldDate.before(endD)) || vehicle.soldDate.equals(endD) || vehicle.soldDate.equals(startD)){
   				saleCar++;
   			}
   		}//else if(vehicle.status.equals("Newly Arrived")){
   				newCar++;
   		//}
   	}
   	
       
   	//List<LeadsDateWise> lDateWises = LeadsDateWise.findByLocation(Location.findById(Long.parseLong(session("USER_LOCATION"))));
   	List<LeadsDateWise> lDateWises = LeadsDateWise.getAllVehicles(users);
   	for(LeadsDateWise lWise:lDateWises){
   		if(lWise.goalSetTime !=null){
   		if(lWise.goalSetTime.equals("1 week") ){
   			cal.setTime(lWise.leadsDate);  
   			cal.add(Calendar.DATE, 7);
   			Date m =  cal.getTime(); 
   			if(m.after(dateobj)) {
   				lDataVM.leads = lWise.leads;
   				lDataVM.goalTime =lWise.goalSetTime;
   				
   			}
   			
   		}else if(lWise.goalSetTime.equals("1 month") && lWise.goalSetTime !=null){
   			cal.setTime(lWise.leadsDate);  
   			cal.add(Calendar.DATE, 30);
   			Date m =  cal.getTime(); 
   			if(m.after(dateobj)) {
   				lDataVM.leads = lWise.leads;
   				lDataVM.goalTime =lWise.goalSetTime;
   			}
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
			
		/*if(locOrPer.equals("location")){
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
		}*/
		if(!locOrPer.equals("location")){
			List<AuthUser> allLoUser = AuthUser.findByLocatio(Location.findById(locationId));
	    	List<Integer> priceList = new ArrayList<>();
	    	int pricecountOther = 0;
	    	for(AuthUser aUser: allLoUser){
	    		pricecountOther = 0;
	    		if(allLoUser != null){
	        		
	    			List<Vehicle> vList = Vehicle.findBySoldUserAndSold(aUser);
	        		for (Vehicle vehicle : vList) {
	    				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
	    					pricecountOther = pricecountOther + vehicle.price;
	    				}
	    			}
	        		priceList.add(pricecountOther);
	        	}
	    	}
	    	Collections.sort(priceList, Collections.reverseOrder());
	    	List<Integer> longV = new ArrayList<>();
			DateAndValueVM sValue = new DateAndValueVM();
			sValue.name = "Highest Result";
			longV.add(priceList.get(0));
			sValue.data = longV;
			sAndValues.add(sValue);
   }
   	
   	List<Vehicle> vList2 = Vehicle.findByLocationAndSold(locationId);
   		
   	if(vList2.size() != 0){
   		List<Integer> longV = new ArrayList<>();
   		DateAndValueVM sValue = new DateAndValueVM();
		   	if(locOrPer.equals("location"))
				sValue.name = "Currently Earned";
			else	
			sValue.name = "Your Result";
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
    	
    	Location loc = Location.findById(Long.parseLong(session("USER_LOCATION")));
   	 	List<AuthUser> authUser = AuthUser.findByLocatioUsers(loc);
    	List<Vehicle> vehicle1 = Vehicle.findByLocationAndSold(Long.parseLong(session("USER_LOCATION")));
   	    int flagForSale=0;	
   	    lDataVM.flagForBestSaleIcon=flagForSale;
    	for(Vehicle vehicle:vehicle1){
    		if(vehicle != null){
    			flagForSale=1;
    			lDataVM.flagForBestSaleIcon=flagForSale;
    			break;
    		}
    		else{
    			flagForSale=0;
    			lDataVM.flagForBestSaleIcon=flagForSale;
    		}
    	}
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
    		
    		rInfoAll = RequestMoreInfo.findByLocation(locationId);
    		sListAll = ScheduleTest.findByLocation(locationId);
    		tradeInsAll = TradeIn.findByLocation(locationId);
    		
    	}else if(users.role.equals("Sales Person") || locOrPer.equals("person")){
    		rInfo = RequestMoreInfo.findAllSeenSch(users);
    		sList = ScheduleTest.findAllAssigned(users);
    		tradeIns = TradeIn.findAllSeenSch(users);
    		
    		rInfoAll = RequestMoreInfo.findAllByNotOpenLead(users);
    		sListAll = ScheduleTest.findAllByNotOpenLead(users);
    		tradeInsAll = TradeIn.findAllByNotOpenLead(users);
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
		if(!locOrPer.equals("location")){
    	List<AuthUser> allLoUser = AuthUser.findByLocatio(Location.findById(locationId));
    	List<Integer> priceList = new ArrayList<>();
    	int pricecountOther = 0;
    	for(AuthUser aUser: allLoUser){
    		pricecountOther = 0;
    		if(allLoUser != null){
        		
    			List<Vehicle> vList = Vehicle.findBySoldUserAndSold(aUser);
        		for (Vehicle vehicle : vList) {
    				if(monthCal.equals(onlyMonth.format(vehicle.soldDate))){
    					pricecountOther = pricecountOther + vehicle.price;
    				}
    			}
        		priceList.add(pricecountOther);
        	}
    	}
    	Collections.sort(priceList, Collections.reverseOrder());
    	List<Integer> longV = new ArrayList<>();
		DateAndValueVM sValue = new DateAndValueVM();
		sValue.name = "Highest Result";
		longV.add(priceList.get(0));
		sValue.data = longV;
		sAndValues.add(sValue);
    }
    	
    	List<Vehicle> vList2 = Vehicle.findByLocationAndSold(locationId);
    	
    	if(vList2.size() != 0){
    		List<Integer> longV = new ArrayList<>();
    		DateAndValueVM sValue = new DateAndValueVM();
    		if(locOrPer.equals("location"))
    			sValue.name = "Currently Earned";
    		else	
			sValue.name = "Your Result";
			longV.add(monthPriceCount);
			sValue.data = longV;
			sAndValues.add(sValue);
    	}
    	
    	lDataVM.sendData = sAndValues;
    }
   
    
    public static Result getAllLocation(String timeSet) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    			
    			
    			List<RequestMoreInfo> rInfoAll = RequestMoreInfo.findByLocation(location.id);
    			List<ScheduleTest> sListAll = ScheduleTest.findByLocation(location.id);
    			List<TradeIn> tradeInsAll = TradeIn.findByLocation(location.id);
    			
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
    
    public static Result getUsers(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   	return ok(home.render("",userRegistration));
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
	   		return ok(home.render("",userRegistration));
	   	} else {
	   	  GroupTable gTable = new GroupTable();
	   	  gTable.setName(createGroup);
	   	  gTable.save();
	   	}
	   return ok("");
   }
   
   public static Result deleteGroup(Long groupId){
	   if(session("USER_KEY") == null || session("USER_KEY") == "") {
	   		return ok(home.render("",userRegistration));
	   	} else {
	   	  GroupTable gTable = GroupTable.findById(groupId);
	   	  gTable.delete();
	   	}
	   return ok("");
   }
    
    public static Result deleteUserById(Integer id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    
   /* public static Result setVehicleAndScheduleStatus() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		String clientEmail = null;
    		if(leadType.equals("Schedule Test Drive")) {
    			ScheduleTest schedule = ScheduleTest.findById(id);
    			schedule.setConfirmDate(null);
    			schedule.setConfirmTime(null);
    			schedule.update();
    			clientEmail = schedule.email;
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Test Drive has been canceled");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.scheduleTest = ScheduleTest.findById(schedule.id);
        		uNotes.save();
        		
        		/* for template mail */
        		Map map = new HashMap();
	    		map.put("email",clientEmail);
	    		map.put("vin", schedule.vin);
	    		map.put("uname", user.firstName+" "+user.lastName);
	    		map.put("uphone", user.phone);
	    		map.put("uemail", user.email);
	    		map.put("clientName",schedule.name);
				cancelTestDriveMail(map);
        		
        		
        		
    		} else if(leadType.equals("Request More Info")) {
    			RequestMoreInfo info = RequestMoreInfo.findById(id);
    			info.setConfirmDate(null);
    			info.setConfirmTime(null);
    			info.update();
    			clientEmail = info.email;
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Test Drive has been canceled");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.requestMoreInfo = RequestMoreInfo.findById(info.id);
        		uNotes.save();
        		String comments="Test Drive has been canceled";
        		String subject = "Test Drive cancelled";
        	  sendEmail(clientEmail,subject,comments);
        		
    		} else if(leadType.equals("Trade-In Appraisal")) {
    			TradeIn info = TradeIn.findById(id);
    			info.setConfirmDate(null);
    			info.setConfirmTime(null);
    			info.update();
    			
    			clientEmail = info.email;
    			
        		UserNotes uNotes = new UserNotes();
        		uNotes.setNote("Test Drive has been canceled");
        		uNotes.setAction("Other");
        		uNotes.createdDate = currDate;
        		uNotes.createdTime = currDate;
        		uNotes.user = user;
        		uNotes.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
        		uNotes.tradeIn = TradeIn.findById(info.id);
        		uNotes.save();
        		String comments="Test Drive has been canceled";
        		String subject = "Test Drive cancelled";
        	  sendEmail(clientEmail,subject,comments);
    		}
    		
    		
    		return ok();
    	}
    }
    
    public static Result setScheduleStatusClose(Long id,String leadtype,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		String clientEmail=null;
    		String clientPhone=null;
    		String clientName=null;
    		
    		AuthUser user = getLocalUser();
    		Date currDate = new Date();
    		
    		String comments="Test Drive has been canceled";
    		String subject = "Test Drive cancelled";
    		
    		if(leadtype.equals("Schedule Test Drive")) {
    			ScheduleTest schedule = ScheduleTest.findById(id);
    			String vin =schedule.vin;
    			
    			schedule.setLeadStatus("CANCEL");
    			schedule.setStatusDate(currDate);
    			schedule.setStatusTime(currDate);
    			schedule.setReason(reason);
    	          clientEmail=schedule.email;
    			//clientEmail="nananevase9766@gmail.com";
    	          /* for template mail */
    	         if(schedule.confirmDate == null){
          		Map map = new HashMap();
  	    		map.put("email",clientEmail);
  	    		map.put("vin", schedule.vin);
  	    		map.put("uname", user.firstName+" "+user.lastName);
  	    		map.put("uphone", user.phone);
  	    		map.put("uemail", user.email);
  	    		map.put("clientName", schedule.name);
  				cancelTestDriveMail(map);
    	          
    	         }
    	          
    			schedule.update();
    			
    			if(schedule.confirmDate != null){
    				Map map = new HashMap();
		    		map.put("email",clientEmail);
		    		map.put("vin", vin);
		    		map.put("uname", user.firstName+" "+user.lastName);
		    		map.put("uphone", user.phone);
		    		map.put("uemail", user.email);
		    		map.put("clientName",schedule.name);
    				cancelTestDriveMail(map);
      			}
    			
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
    			
    			String vin=info.vin;
    			info.setStatus("CANCEL");
    			info.setStatusDate(currDate);
    			info.setStatusTime(currDate);
    			info.setReason(reason);
    			info.update();
    			clientEmail=info.email;
    			
    			if(info.confirmDate != null){
    				Map map = new HashMap();
		    		map.put("email",clientEmail);
		    		map.put("vin", vin);
		    		map.put("uname", user.firstName+" "+user.lastName);
		    		map.put("uphone", user.phone);
		    		map.put("uemail", user.email);
		    		map.put("clintName", info.name);
    				cancelTestDriveMail(map);
        	    	  //sendEmail(clientEmail,subject,comments);
        			}
    			
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
    			String vin=info.vin;
    			info.setStatus("CANCEL");
    			info.setStatusDate(currDate);
    			info.setStatusTime(currDate);
    			info.setReason(reason);
    			info.update();
    			clientEmail=info.email;
    			
    			if(info.confirmDate != null){
    				Map map = new HashMap();
		    		map.put("email",clientEmail);
		    		map.put("vin", vin);
		    		map.put("uname", user.firstName+" "+user.lastName);
		    		map.put("uphone", user.phone);
		    		map.put("uemail", user.email);
    				cancelTestDriveMail(map);
      	    	  //sendEmail(clientEmail,subject,comments);
      			}
    			
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
    		return ok(home.render("",userRegistration));
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
    	List<String> vinList = new ArrayList<>();
    	List<RequestInfoVM>vinAndEmailList =new ArrayList<>();
    	
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
				RequestInfoVM vm=new RequestInfoVM();
				vm.vin=tradeIn.vin;
				AuthUser userObj = AuthUser.findById(tradeIn.assignedTo.id);
				if(userObj !=null){
					//emailList.add(userObj.email);
					vm.email=userObj.email;
				}
				vinAndEmailList.add(vm);
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
				
				RequestInfoVM vm1=new RequestInfoVM();
				vm1.vin=rMoreInfo.vin;
				AuthUser userObj = AuthUser.findById(rMoreInfo.assignedTo.id);
				if(userObj !=null){
				//	emailList.add(userObj.email);
					vm1.email=userObj.email;
					
				}
				vinAndEmailList.add(vm1);
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
				RequestInfoVM vm2=new RequestInfoVM();
				vm2.vin=scheduleTest.vin;
				if(userObj !=null){
					//emailList.add(userObj.email);
					vm2.email=userObj.communicationemail;
					
				}
				vinAndEmailList.add(vm2);
			}
		}
		
		if(vinAndEmailList.size()>0){
			//vehicleSoldEmail(emailList);
			vehicleSoldMail(vinAndEmailList);
		}
    }
    
    public static Result setRequestStatusCancel(Long id,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		String userType = user.getRole();
    		if(userType == null) {
    			userType = "";
    		}
    		return ok(userType);
    	}
    }
    
    public static Result sendEmailDaily(){
    	 //AuthUser user = getLocalUser();
    	 DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
    	 DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
    	 //DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    	 
         DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
         SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
         Date currD = new Date();
         Date currentDate = null;
         Date aftHrDate = null;
         Date aftDay = null;
         Date aftHrDate1 = null;
         Date aftDay1 = null;
         Date infoDate = null;
         Date datec = null;
         
         
         Date lessDay = DateUtils.addDays(currD, -1);
         //https://maps.googleapis.com/maps/api/timezone/json?location=37.7870882,-122.39112790000001&timestamp=1331161200&key=AIzaSyAZDXHvlpRPy2R_LWP4iCoxN_UBDdMg6o4
         //Date newDate = DateUtils.addHours(info2.confirmTime, 4);
         List<ScheduleTest> list = ScheduleTest.findAllByServiceTestEmail(lessDay);
         List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmGraLeadsToEmail(lessDay);
     	 List<TradeIn> tradeIns = TradeIn.findByConfirmGraLeadsToEmail(lessDay);
     	
         for(ScheduleTest scTest:list){
        	 System.out.println("----------------");
        	 System.out.println(scTest.id);
        	 List<AuthUser> listForUser=null;
        	 List<AuthUser> listForUser1=null;
        	 if(scTest.user != null && scTest.assignedTo != null){
        		 AuthUser aUser = AuthUser.findById(scTest.assignedTo.id);
        		 Location location = Location.findById(16l);
        		 if(aUser.location != null){
        			 location = Location.findById(aUser.location.id);
        		 }
            	
            	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
                 String IST = df2.format(currD);
                
                 Date istTimes = null;
    			try {
    				istTimes = df1.parse(IST);
    			} catch (ParseException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
            	
            	 
            	 String cDate = df.format(istTimes);
                 String cTime = parseTime.format(istTimes);
                 String crD =    df1.format(istTimes);
        		 
                 try {
                	 currentDate = df1.parse(crD);
                	 datec = df.parse(cDate);
                	 aftHrDate = DateUtils.addHours(currentDate, 1);
                	 aftDay = DateUtils.addHours(currentDate, 24);
                	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
                	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
            	 AuthUser emailUser = AuthUser.findById(scTest.assignedTo.id);
            	 if(scTest.user != null){
            		 AuthUser userD = AuthUser.findById(scTest.user.id);
                	 try {
                		 System.out.println("scTest.confirmDate " + scTest.confirmDate);
                		 String str = df.format(scTest.confirmDate) +" "+parseTime.format(scTest.confirmTime);
                		 System.out.println("format confirm time " + str);
                		 infoDate = df1.parse(str);
                    	 System.out.println(df1.format(currD));
                    	 System.out.println(parseTime.format(scTest.confirmTime));
                    	 System.out.println(infoDate); //db date
                    	 System.out.println(aftHrDate); //+hour  dt
                    	 System.out.println(aftHrDate1);
                    	 System.out.println(aftDay); // +24hrs
                    	 System.out.println(aftDay1); //+15mins ahead
                    	 System.out.println(emailUser.email);
                    	 System.out.println("meetingStatus : "+scTest.meetingStatus);
                    	 System.out.println("-11---------------");
                    	 System.out.println(scTest.email);
                		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
                    		 if(scTest.meetingStatus == null){
                				 String subject = "Test drive reminder";
                 		    	 String comments = "You have a test drive scheduled in 1 hour \n"+str+" "+scTest.name;
                 		    	 sendEmail(emailUser.communicationemail, subject, comments);
                 		    	sendEmail(scTest.email, subject, comments);
                 		    	
                			 }else if(scTest.meetingStatus.equals("meeting")){
                				 
                				 listForUser=new ArrayList<>();
                				 List<Integer>userIds = new ArrayList<>();
                				 List<ScheduleTest> schedulegroupList = ScheduleTest.findAllGroupMeeting(scTest.groupId);
                				 for(ScheduleTest scList:schedulegroupList){
                					 
                					 AuthUser user = AuthUser.findById(scList.user.id);
                					 if(userIds.contains(user.id)){
                						 continue;
                					 }else{
                						 userIds.add(user.id);
                					 }
                					 listForUser.add(user);
                					 
                				 }
                				 
                				 System.out.println("----^^^^^^^^^^^^^^^-111-----------");
                				 String subject = "IN ONE HOUR";
                 		    	 String comments = "You have a meeting scheduled in 1 hour \n"+str+" "+scTest.name;
        						meetingReminder(listForUser,emailUser.communicationemail, scTest.confirmDate, scTest.confirmTime, subject);
                 		    	meetingReminder(listForUser,userD.communicationemail, scTest.confirmDate, scTest.confirmTime, subject);
                			
                			 }
                    	 }
                		 System.out.println("checking schedular condition.");
                		 //infoDate - dbtime
                		 //aftDay - next day
                		 //aftDay1 - next day 15mins...
                		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
                    		 if(scTest.meetingStatus == null){
                    			 String subject = "Test drive reminder";
                 		    	 String comments = "You have a test drive scheduled in 24 hours \n Date" +infoDate ;
                 		    	 sendEmailAfterDay(emailUser.communicationemail, subject, comments,scTest.vin,scTest.confirmDate,scTest.confirmTime,scTest.user,scTest.name);
                 		    	sendEmailAfterDay(scTest.email, subject, comments,scTest.vin,scTest.confirmDate,scTest.confirmTime,scTest.user,scTest.name);
                			 }else if(scTest.meetingStatus.equals("meeting")){
                				 
                				 listForUser1=new ArrayList<>();
                				 List<Integer>userIds = new ArrayList<>();
                				 List<ScheduleTest> schedulegroupList = ScheduleTest.findAllGroupMeeting(scTest.groupId);
                                  for(ScheduleTest scList:schedulegroupList){
                					 
                					 AuthUser user = AuthUser.findById(scList.user.id);
                					 
                					 if(userIds.contains(user.id)){
                						 continue;
                					 }else{
                						 userIds.add(user.id);
                					 }
                					 listForUser1.add(user);
                					 
                				 }
                				 String subject = "You have a meeting scheduled in 24 hours";
                 		    	 String comments = "You have a meeting scheduled in 24 hours \n"+df.format(scTest.confirmDate)+"   "+parseTime.format(scTest.confirmTime)+" "+scTest.name;
        						meetingReminderAfterDay(listForUser1,emailUser.communicationemail, scTest.confirmDate, scTest.confirmTime, subject);
                 		    	meetingReminderAfterDay(listForUser1,userD.communicationemail, scTest.confirmDate, scTest.confirmTime, subject);
                			 }
                    	 }
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
            	 }
        	 }
        	 
         }
         
         for(RequestMoreInfo rInfo:requestMoreInfos){
        	 AuthUser emailUser = AuthUser.findById(rInfo.assignedTo.id);
        	 
        	 Location location = Location.findById(emailUser.location.id);
        	
        	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
			try {
				istTimes = df1.parse(IST);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	 
        	 String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
    		 
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, 24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	 
        	 
        	 
        	 
        	 try {
        		 String str = df.format(rInfo.confirmDate) +" "+parseTime.format(rInfo.confirmTime);
        		 infoDate = df1.parse(str);
        		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
        			 String subject = "Test drive reminder";
     		    	 String comments = "You have a test drive scheduled in 1 hour ";
     		    	 sendEmail(emailUser.communicationemail, subject, comments);
     		    	sendEmail(rInfo.email, subject, comments);
        		 }
        		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
        			 String subject = "Test drive reminder";
     		    	 String comments = "You have a test drive scheduled in 24 hours ";
     		    	// sendEmail(emailUser.communicationemail, subject, comments);
     		    	sendEmailAfterDay(emailUser.communicationemail, subject, comments,rInfo.vin,rInfo.confirmDate,rInfo.confirmTime,rInfo.user,rInfo.name);
     		    	sendEmailAfterDay(rInfo.email, subject, comments,rInfo.vin,rInfo.confirmDate,rInfo.confirmTime,rInfo.user,rInfo.name);
     		    	//sendEmail(rInfo.email, subject, comments);
        		 }
			} catch (Exception e) {
				e.printStackTrace();
			}
        	 /*if(rInfo.confirmDate.equals(datec)){
        		 if(rInfo.confirmTime.equals(timeSet)){
        			 String subject = "Request test D";
     		    	 String comments = "test D";
     		    	 sendEmail("yogeshpatil424@gmail.com", subject, comments);
        		 }
        	 }*/	 
         }
         
         for(TradeIn tInfo:tradeIns){
        	 
        	 AuthUser emailUser = AuthUser.findById(tInfo.assignedTo.id);
        	 
        	 Location location = Location.findById(emailUser.location.id);
        	
        	 df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
			try {
				istTimes = df1.parse(IST);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	 
        	 String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
    		 
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, 24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	 
        	 
        	 try {
        		 String str = df.format(tInfo.confirmDate) +" "+parseTime.format(tInfo.confirmTime);
        		 infoDate = df1.parse(str);
        		 if((infoDate.equals(aftHrDate)||infoDate.after(aftHrDate)) && ((infoDate.equals(aftHrDate1)||infoDate.before(aftHrDate1)))){
        			 String subject = "Test drive reminder";
     		    	 String comments = "You have a test drive scheduled in 1 hour ";
     		    	 sendEmail(emailUser.communicationemail, subject, comments);
     		    	sendEmail(tInfo.email, subject, comments);
        		 }
        		 if((infoDate.equals(aftDay)||infoDate.after(aftDay)) && ((infoDate.equals(aftDay1)||infoDate.before(aftDay1)))){
        			 String subject = "Test drive reminder";
     		    	 String comments = "You have a test drive scheduled in 24 hours ";
     		    	sendEmailAfterDay(emailUser.communicationemail, subject, comments,tInfo.vin,tInfo.confirmDate,tInfo.confirmTime,tInfo.user,tInfo.firstName+" "+tInfo.lastName);
     		    	sendEmailAfterDay(tInfo.email, subject, comments,tInfo.vin,tInfo.confirmDate,tInfo.confirmTime,tInfo.user,tInfo.firstName+" "+tInfo.lastName);
     		    	// sendEmail(emailUser.communicationemail, subject, comments);
     		    	//sendEmail(tInfo.email, subject, comments);
        		 }
			} catch (Exception e) {
				e.printStackTrace();
			}
        	 /*if(tInfo.confirmDate.equals(datec)){
        		 if(tInfo.confirmTime.equals(timeSet)){
        			 String subject = "TradeIn test D";
     		    	 String comments = "test D";
     		    	 sendEmail("yogeshpatil424@gmail.com", subject, comments);
        		 }
        	 }	 */
         }
         
         
         
        
        	/*_------------------------------------------------------------------------------------------------------------------*/
            // AuthUser user=getLocalUser();
         
         Date date1=new Date();
         DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         String date2 = format1.format(date1);
         Date dat=null;
         try {
        	 dat = format1.parse(date2);
 		} catch (ParseException e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
         Date beforeThreeDays = DateUtils.addDays(dat, -3);
         List <RequestMoreInfo> requestInfos=RequestMoreInfo.findAllNullStatusLeads(beforeThreeDays);
         if(requestInfos != null){
         for(RequestMoreInfo info:requestInfos){
        	 
        	 DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	 
        	 AuthUser emailUser = AuthUser.findById(info.assignedTo.id);
        	 if(emailUser.location != null){
        	 Location location = Location.findById(emailUser.location.id);
             
             df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
    		try {
    			istTimes = df1.parse(IST);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
             String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, -24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        		 Date sourceDate1 = info.requestTime;
        		 //sourceDate = DateUtils.addHours(sourceDate, 24);
        		 System.out.println("source date"+sourceDate1);
        		 
        		// if((sourceDate.equals(aftHrDate)||sourceDate.after(aftHrDate)) && ((sourceDate.equals(aftHrDate1)||sourceDate.before(aftHrDate1)))){
        		 if((sourceDate1.equals(aftDay)||sourceDate1.after(aftDay)) && ((sourceDate1.equals(aftDay1)||sourceDate1.before(aftDay1)))){
        			 //List<UserNotes> note=UserNotes.findRequestMoreList(info);
        			// if(note != null){
        			 //if(!(note.size()>2)){
        				 AuthUser auser=AuthUser.findById(info.assignedTo.id);
        				sendEmailForNotFollowed(info.vin,auser);
        				 
        		// }
        		//	 }
        	 }
        	 
         } 
         }
         }
        
         List <ScheduleTest> test1=ScheduleTest.findAllNullStatusLeads(beforeThreeDays);
         for(ScheduleTest test:test1){
        	
        	 DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	 if(test.assignedTo.id != null){
        	 AuthUser emailUser = AuthUser.findById(test.assignedTo.id);
        	 Location location=null;
        	 if(emailUser !=null){
        		 if(emailUser.location != null){
        	 location = Location.findById(emailUser.location.id);
        		 }
             if(location != null){
             df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
    		try {
    			istTimes = df1.parse(IST);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
             String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, -24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        		 Date sourceDate = test.scheduleTime;
        		// sourceDate = DateUtils.addHours(sourceDate, 24);
        		 System.out.println("source date"+sourceDate);
        		 System.out.println("aftDay"+aftDay);
        		 System.out.println("aftDay1"+aftDay1);
        		 
        		 if((sourceDate.equals(aftDay)||sourceDate.after(aftDay)) && ((sourceDate.equals(aftDay1)||sourceDate.before(aftDay1)))){
        			/*List<UserNotes> note=UserNotes.scheduleList(test);
        			 if(note != null){
        			 if(!(note.size()>2)){
        				 
        				 AuthUser auser=AuthUser.findById(test.assignedTo.id);
        				 sendEmailForNotFollowed(test.vin,auser);
        		 }*/
        			 if(test.meetingStatus == null){
        			 AuthUser auser=AuthUser.findById(test.assignedTo.id);
    				 sendEmailForNotFollowed(test.vin,auser);
        			 }
        	  
        	 }
        	 }
        	 }
        	 }
         }
         
         
         
         List <TradeIn> tradeIns1=TradeIn.findAllNullStatusLeads(beforeThreeDays);
         if(tradeIns1 !=null){
         for(TradeIn trade:tradeIns1){
        	 
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	 
        	 AuthUser emailUser = AuthUser.findById(trade.assignedTo.id);
        	 if(emailUser.location !=null){
        	 Location location = Location.findById(emailUser.location.id);
             
             df2.setTimeZone(TimeZone.getTimeZone(location.time_zone));
             String IST = df2.format(currD);
            
             Date istTimes = null;
    		try {
    			istTimes = df1.parse(IST);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
             String cDate = df.format(istTimes);
             String cTime = parseTime.format(istTimes);
             String crD =    df1.format(istTimes);
             try {
            	 currentDate = df1.parse(crD);
            	 datec = df.parse(cDate);
            	 aftHrDate = DateUtils.addHours(currentDate, 1);
            	 aftDay = DateUtils.addHours(currentDate, -24);
            	 aftHrDate1 = DateUtils.addMinutes(aftHrDate, 15);
            	 aftDay1 = DateUtils.addMinutes(aftDay, 15);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        		 Date sourceDate = trade.tradeTime;
        		 //sourceDate = DateUtils.addHours(sourceDate, 24);
        		 System.out.println("source date"+sourceDate);
        		 
        		 if((sourceDate.equals(aftDay)||sourceDate.after(aftDay)) && ((sourceDate.equals(aftDay1)||sourceDate.before(aftDay1)))){
        			// List<UserNotes> note=UserNotes.tradeInList(trade);
        			// if(note !=null){
        			// if(!(note.size()>2)){
        				 
        				 AuthUser auser=AuthUser.findById(trade.assignedTo.id);
        				 sendEmailForNotFollowed(trade.vin,auser);
        		// }
        		// }	 
        	 }
         }
         }
         }
         
    
    	return ok();
    
}
    
    


    private static void sendEmailForNotFollowed(String vin,AuthUser user) {
     	
     //AuthUser logoUser = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
     //	SiteLogo logo = SiteLogo.findByLocation(Long.valueOf(session("USER_LOCATION"))); // findByUser(logoUser);
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
 			try{
 			message.setFrom(new InternetAddress(emailUser,emailName));
 			}
 			catch(UnsupportedEncodingException e){
 				e.printStackTrace();
 			}
 			//message.setRecipients(Message.RecipientType.TO,
 			//		InternetAddress.parse(map.get("email").toString()));
 			if(user.role.equalsIgnoreCase("Manager")){
 				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(user.communicationemail));
 			}
 			else if(user.role.equalsIgnoreCase("Sales Person")){
 				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(user.communicationemail));
 				
 				AuthUser user1=AuthUser.getlocationAndManagerOne(user.location);
 				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(user1.communicationemail));
 			}
 			
 			
 			message.setSubject("LEAD NOT FOLLOWED");
 			Multipart multipart = new MimeMultipart();
 			BodyPart messageBodyPart = new MimeBodyPart();
 			messageBodyPart = new MimeBodyPart();
 			
 			VelocityEngine ve = new VelocityEngine();
 			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
 			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
 			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
 			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
 			ve.init();
 		
 			
 	        Template t = ve.getTemplate("/public/emailTemplate/leadNotFollowedUp_HTML.html"); 
 	        VelocityContext context = new VelocityContext();
 	        
 	       Vehicle vehicle = Vehicle.findByVinAndStatus(vin.toString());
	        context.put("year", vehicle.year);
	        context.put("make", vehicle.make);
	        context.put("model", vehicle.model);
	        context.put("price", "$"+vehicle.price);
	        context.put("stock", vehicle.stock);
	        context.put("vin", vehicle.vin);
	        context.put("make", vehicle.make);
	        context.put("mileage", vehicle.mileage);
 	        
	        
	        if(user.role.equalsIgnoreCase("Manager")){
	        	context.put("name", user.fullName());
 	 	        context.put("email", user.email);
 	 	        context.put("phone", user.phone);
	        	
 			}
 			else if(user.role.equalsIgnoreCase("Sales Person")){
 				context.put("name", user.fullName());
 	 	        context.put("email", user.email);
 	 	        context.put("phone", user.phone);
 				
 			}
	        
	        VehicleImage image = VehicleImage.getDefaultImage(vehicle.vin);
 	        if(image!=null) {
 	        	context.put("defaultImage", image.path);
 	        } else {
 	        	context.put("defaultImage", "");
 	        }
	        
 	        /*
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
 	        }*/
 	        StringWriter writer = new StringWriter();
 	        t.merge( context, writer );
 	        String content = writer.toString(); 
 			
 			messageBodyPart.setContent(content, "text/html");
 			multipart.addBodyPart(messageBodyPart);
 			message.setContent(multipart);
 			Transport.send(message);
 			System.out.println("email sent not followed");
 			
 		}
 		catch (Exception e)
 		{
 			e.printStackTrace();
 		}
     }
     
     
    
    
    
	public static void meetingReminder(List<AuthUser> listForUser,String communicationEmail, Date confirmDate,Date confirmTime,String subject){
		/*InternetAddress[] usersArray = new InternetAddress[userList.size()];
		int index = 0;
		for (AuthUser assi : userList) {
			try {
				
				usersArray[index] = new InternetAddress(assi.getCommunicationemail());
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : listForUser){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
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
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(communicationEmail));
			/*usersArray*/
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/internalMeetingReminder.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	      //  context.put("title", vm.name);
	       // context.put("location", loc.getName());
	       // context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = formatter.format(confirmDate);
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        context.put("userList",list);
	        
	        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
	        String time = localDateFormat.format(confirmTime);

	        context.put("time",time);
	       // context.put("disc", vm.getReason());
	       
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
	    
    
    
	public static void meetingReminderAfterDay(List<AuthUser> listForUser,String communicationEmail, Date confirmDate,Date confirmTime,String subject){
		/*InternetAddress[] usersArray = new InternetAddress[userList.size()];
		int index = 0;
		for (AuthUser assi : userList) {
			try {
				
				usersArray[index] = new InternetAddress(assi.getCommunicationemail());
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : listForUser){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
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
			message.setFrom(new InternetAddress(emailUser,emailName));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(communicationEmail));
			/*usersArray*/
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/internalMeetingReminderAfterDay.vm"); 
	        VelocityContext context = new VelocityContext();
	        
	      //  context.put("title", vm.name);
	       // context.put("location", loc.getName());
	       // context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = formatter.format(confirmDate);
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        context.put("userList",list);
	        
	        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
	        String time = localDateFormat.format(confirmTime);
	        
	        context.put("time",time);
	       // context.put("disc", vm.getReason());
	       
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
	    
	
	
    
    public static Result getScheduleDates() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		Date curr = new Date();
    		String cDate = df.format(curr);
    		Date cD = null;
    		try {
				cD = df.parse(cDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		AuthUser user = getLocalUser();
    		List<SqlRow> rows = ScheduleTest.getScheduleDates(user, cDate);
    		List<RequestInfoVM> vmList = new ArrayList<>();
    		for(SqlRow row : rows) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = row.getString("confirm_date");
    			vmList.add(vm);
    		}
    		
    		List<SqlRow> rowsRequest = RequestMoreInfo.getRequestedDates(user, cDate);
    		for(SqlRow row : rowsRequest) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = row.getString("confirm_date");
    			vmList.add(vm);
    		}
    		
    		List<SqlRow> rowsTrad = TradeIn.getTradeDates(user, cDate);
    		for(SqlRow row : rowsTrad) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = row.getString("confirm_date");
    			vmList.add(vm);
    		}
    		
    		/*List<SqlRow> toDoRows = ToDo.getToDoDates(cDate);
    		for(SqlRow todo : toDoRows) {
    			RequestInfoVM vm = new RequestInfoVM();
    			vm.confirmDate = todo.getString("due_date");
    			vmList.add(vm);
    		}*/
    		
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getScheduleBySelectedDate(String date) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
    		Date dateObj = df.parse(date);
    		Map<Long,Integer> setGroupid = new HashMap<Long,Integer>();
    		List<ScheduleTest> scheduleList = ScheduleTest.findByDateAndAssignedUser(user, dateObj);
    		List<RequestMoreInfo> requInfos = RequestMoreInfo.findByDateAndAssignedUser(user, dateObj);
    		List<TradeIn> traIns = TradeIn.findByDateAndAssignedUser(user, dateObj);
    		List<RequestInfoVM> vmList = new ArrayList<>();
    		
    		Calendar time = Calendar.getInstance();
    		for(ScheduleTest test : scheduleList) {
    			List<UserVM> listUser = new ArrayList<>();
    			RequestInfoVM vm = new RequestInfoVM();
    			if(test.groupId == null){
    				
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
        	    			/*time.setTime(test.getConfirmTime());
        	    			String ampm = "";
        	    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
        	    				ampm = "PM";
        	    			} else {
        	    				ampm = "AM";
        	    			}
        	    			vm.confirmTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;*/
        	    			vm.confirmTime = new SimpleDateFormat("hh:mm a").format(test.getConfirmTime());
        	    		}
        	    		if(test.getConfirmEndTime() != null) {
        	    			/*time.setTime(test.getConfirmEndTime());
        	    			String ampm = "";
        	    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
        	    				ampm = "PM";
        	    			} else {
        	    				ampm = "AM";
        	    			}
        	    			vm.confirmEndTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;*/
        	    			vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(test.getConfirmEndTime());
        	    		}
        	    		
        	    		
        	    		vmList.add(vm);
    			}else{
    				if(setGroupid.get(test.groupId) == null){
        				setGroupid.put(test.groupId, 1);
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
        	    			/*time.setTime(test.getConfirmTime());
        	    			String ampm = "";
        	    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
        	    				ampm = "PM";
        	    			} else {
        	    				ampm = "AM";
        	    			}
        	    			vm.confirmTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;*/
        	    			vm.confirmTime = new SimpleDateFormat("hh:mm a").format(test.getConfirmTime());
        	    		}
        	    		if(test.getConfirmEndTime() != null) {
        	    			/*time.setTime(test.getConfirmEndTime());
        	    			String ampm = "";
        	    			if(time.get(Calendar.AM_PM) == Calendar.PM) {
        	    				ampm = "PM";
        	    			} else {
        	    				ampm = "AM";
        	    			}
        	    			vm.confirmEndTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + " " + ampm;*/
        	    			vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(test.getConfirmEndTime());
        	    		}
        	    		
        	    		if(test.groupId != null){
        	    			
        	    			List<ScheduleTest> schedulegroupList = ScheduleTest.findAllGroupMeeting(test.groupId);
        	    			
        	    			for(ScheduleTest users:schedulegroupList){
        	    				AuthUser usAuthUser = AuthUser.findById(users.user.id);
            	    			vm.hostName = usAuthUser.firstName+" "+usAuthUser.lastName;
        	    				
        	    				UserVM uVm = new UserVM();
        	    	    		uVm.firstName = users.assignedTo.getFirstName();
        	    	    		uVm.lastName = users.assignedTo.getLastName();
        	    	    		/*if(users.acceptMeeting == 1 && users.declineMeeting == 1){
        	    	    			uVm.meetingFlag = 2;
        	    	    		}else if(users.acceptMeeting == 1 && users.declineMeeting == 0){
        	    	    			uVm.meetingFlag = 1;
        	    	    		}else{
        	    	    			uVm.meetingFlag = 0;
        	    	    		}*/
        	    	    		uVm.meetingFlag = users.meeting;
        	    	    		uVm.id = users.assignedTo.id;
        	    	    	
        	    	    		listUser.add(uVm);
        	    			}
        	    		}
        	    	
        	    	
        	    		
        	    		vm.userdata = listUser;
        	    		vmList.add(vm);
        			}
    			}
    			
	    		
    		}
    		
    		List<ScheduleTest> scheduleList1 = null;
   		 fillLeadsData(scheduleList1, requInfos, traIns, vmList);
    		
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getUsersToAssign() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
    	} else {
    		ToDo todo = ToDo.findById(id);
    		todo.setStatus("Completed");
    		todo.update();
    		return ok();
    	}	
    }
    
    public static Result saveCancelTodoStatus(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		ToDo todo = ToDo.findById(id);
    		todo.setStatus("Deleted");
    		todo.update();
    		return ok();
    	}	
    }
    
    public static Result getToDoBySelectedDate(String date) throws ParseException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    
   
    
    public static Result getSalesUserOnly(Long locationValue) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
	    		
	    		findCustomeData(info.id,vm,2L);
	    		
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
	    		//vm.typeOfLead = "Request More Info";
	    		
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
    
    public static Result getAllSalesPersonContactUsSeen(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(id == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(id);
    		}
    			
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeenContactUs(user);
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
	        		vm.price = vehicle.price;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.custZipCode = info.custZipCode;
	    		vm.enthicity = info.enthicity;
	    		
	    		vm.requestDate = df.format(info.requestDate);
	    		vm.typeOfLead = "Request More Info";
	    		
	    		
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
	    	
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}	
    }
    
    public static Result getAllSalesPersonRequestInfoSeen(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    
    public static Result getPerformanceOfUser(String top,String worst,String week,String month,String year,String allTime,Integer id,Long locationValue,String startD,String endD) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
    		String start1 = "";
			String end1 = "";
			Date startDate=null;
			Date endDate=null;
			Date start = null;
			Date end = null;
			
			try {
				startDate=df1.parse(startD);
				endDate=df1.parse(endD);
				start=startDate;
				end=endDate;
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
    		/*if(week.equals("true")) {
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
			*/
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
				vm.planFlag = 0; 
				if(pMonthlySalepeople != null){
					total = Integer.parseInt(pMonthlySalepeople.totalBrought);
		    	}else{
		    		
						vm.planFlag = 1; 
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
	    		
	    		rInfoAll = RequestMoreInfo.findAllByNotOpenLead(sales);
	    		sListAll = ScheduleTest.findAllByNotOpenLead(sales);
	    		tradeInsAll = TradeIn.findAllByNotOpenLead(sales);
	    	
	    	
	    	for(RequestMoreInfo rMoreInfo:rInfo){
	    		//if((rMoreInfo.requestDate.after(start) && rMoreInfo.requestDate.before(end)) || rMoreInfo.requestDate.equals(end) || rMoreInfo.requestDate.equals(start)){
	    			requestLeadCount++;
	    		//}
	    	}
	    	
	    	
	    	for(ScheduleTest sTest:sList){
	    	//	if((sTest.scheduleDate.after(start) && sTest.scheduleDate.before(end)) || sTest.scheduleDate.equals(end) || sTest.scheduleDate.equals(start)){
	    			scheduleLeadCount++;
	    	//	}
	    	}

	    	for(TradeIn tIn:tradeIns){
	    		//if((tIn.tradeDate.after(start) && tIn.tradeDate.before(end)) || tIn.tradeDate.equals(end) || tIn.tradeDate.equals(start) ){
	    				tradeInLeadCount++;
	    		//}
	    	}
	    	
	    	for(RequestMoreInfo rMoreInfo:rInfoAll){
	    		if(start != null && end !=null){
	    		if((rMoreInfo.requestDate.after(start) && rMoreInfo.requestDate.before(end)) || rMoreInfo.requestDate.equals(end)||  rMoreInfo.requestDate.equals(start)){
	    			requestLeadCount1++;
	    		}
	    		}
	    	}
	    	
	    	
	    	for(ScheduleTest sTest:sListAll){
	    		if(start != null && end !=null){
	    		if((sTest.scheduleDate.after(start) && sTest.scheduleDate.before(end)) || sTest.scheduleDate.equals(end) || sTest.scheduleDate.equals(start)){
	    			scheduleLeadCount1++;
	    		}
	    		}
	    	}

	    	for(TradeIn tIn:tradeInsAll){
	    		if(start != null && end !=null){
	    		if((tIn.tradeDate.after(start) && tIn.tradeDate.before(end)) || tIn.tradeDate.equals(end) || tIn.tradeDate.equals(start)){
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
    				if((vehicle.soldDate.after(start) && vehicle.soldDate.before(end)) || vehicle.soldDate.equals(end) || vehicle.soldDate.equals(start)){
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
                	map.put("weatherValue", vm.weatherValue);
                	map.put("uname", user.firstName+" "+user.lastName);
                	map.put("uphone", user.phone);
                	map.put("uemail", user.email);
                	map.put("clientName",vm.name);
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
        		            	map.put("clientName",vm.name);
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
    		return ok(home.render("",userRegistration));
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
	    		//vm.typeOfLead = "Request More Info";
	    		
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
    
    //added by Vinayak Mane on 23-Apr-2016
    public static Result getAllCanceledLeadsById(Integer leadId) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user;
    		if(leadId == 0){
    			user = getLocalUser();
    		}else{
    			user = AuthUser.findById(leadId);
    		}
	    	List<ScheduleTest> listData = ScheduleTest.getAllFailedById(Long.valueOf(session("USER_LOCATION")), user);
    		
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
	    	
	    	List<RequestMoreInfo> requestData = RequestMoreInfo.findAllCancelByUser(Long.valueOf(session("USER_LOCATION")),user);
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
	    		//vm.typeOfLead = "Request More Info";
	    		
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
	    		
	    		infoVMList.add(vm);
	    	}
	    	
	    	List<TradeIn> tradeInData = TradeIn.findAllCanceledByUser(Long.valueOf(session("USER_LOCATION")),user);
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
    
    public static Result releaseFromNotif(Long id,String leadType) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		if(leadType.equals("Schedule Test")) {
    			ScheduleTest schedule = ScheduleTest.findById(id);
    			schedule.setNotifFlag(1);
    			
    			schedule.update();
    			
    		}
			if(leadType.equals("Request More Info")) {
			    RequestMoreInfo info = RequestMoreInfo.findById(id);
			    info.setNotifFlag(1);
			    info.update();
			    
               
			}
			if(leadType.equals("Trade In")) {
				TradeIn tradeIn = TradeIn.findById(id);
				tradeIn.setNotifFlag(1);
				tradeIn.update();
				
			
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
		/*List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : userList){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
		}
		*/
		
		
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
//	String dateInString = vm.getBestDay();

	
	        
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
	
    public static Result getNewToDoCount() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    			infoVM.id = tests.get(0).id;
    			infoVM.make = vehicle.getMake();
    			infoVM.leadType = "Schedule Test";
    			infoVM.model = vehicle.getModel();
    			infoVM.name = vehicle.getYear();
    			infoVM.trim = vehicle.getTrim();
    			infoVM.price = vehicle.getPrice();
    			infoVM.premiumFlag = tests.get(0).premiumFlag;
    			map.put("data", infoVM);
    		} else if(infos.size()==1) {
    			RequestInfoVM infoVM = new RequestInfoVM();
    			Vehicle vehicle = Vehicle.findByVinAndStatus(infos.get(0).vin);
    			infoVM.id = infos.get(0).id;
    			infoVM.make = vehicle.getMake();
    			if(infos.get(0).isScheduled) 
    				infoVM.leadType = "Schedule Test";
    			else
    				infoVM.leadType = "Request More Info";
    			infoVM.model = vehicle.getModel();
    			infoVM.name = vehicle.getYear();
    			infoVM.trim = vehicle.getTrim();
    			infoVM.price = vehicle.getPrice();
    			infoVM.premiumFlag = infos.get(0).premiumFlag;
    			map.put("data", infoVM);
    		} else {
    			RequestInfoVM infoVM = new RequestInfoVM();
    			Vehicle vehicle = Vehicle.findByVinAndStatus(tradeIns.get(0).vin);
    			infoVM.id = tradeIns.get(0).id;
    			infoVM.make = vehicle.getMake();
    			if(tradeIns.get(0).isScheduled) 
    				infoVM.leadType = "Schedule Test";
    			else
    				infoVM.leadType = "Trade In";
    			infoVM.model = vehicle.getModel();
    			infoVM.name = vehicle.getYear();
    			infoVM.trim = vehicle.getTrim();
    			infoVM.price = vehicle.getPrice();
    			infoVM.premiumFlag = tradeIns.get(0).premiumFlag;
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
    
    
    public static Result getActionListTwo(String startDate,String endDate){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	Date sDate = null;
    	Date eDate = null;
    	
    	try {
			sDate = df.parse(startDate);
			eDate = df.parse(endDate); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	List<ClickyActionList> cList = ClickyActionList.getAll(sDate, eDate);
    	
    	return ok(Json.toJson(cList));
    }
    


    
    public static Result getEngagementActionChart(String startDate,String endDate,String title){
    	String params = null;
    	System.out.println(startDate);
    	System.out.println(endDate);
    	Date d1 = null;
        Date d2 = null;
        Map<String, Object> map = new HashMap<>();
        List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		map.put("dates",dates);
		map.put("data",data);
        List<ClickyPagesVM> clickyList = new ArrayList<>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
    	 try {
             d1 = format.parse(startDate);
             d2 = format.parse(endDate);

             long diff = d2.getTime() - d1.getTime();

             long diffDays = diff / (24 * 60 * 60 * 1000);
             Integer days=(int)diffDays;
          Date beforeStart = DateUtils.addDays(d1, -days); 
             String newDate=format.format(beforeStart);
             System.out.print(newDate + " newDate ");
     	   	
             GregorianCalendar gcal = new GregorianCalendar();
             gcal.setTime(d1);
             sendDataVM vm=new sendDataVM();
             vm.name=title;
             List<Long> lonnn = new ArrayList<>();
             while (!gcal.getTime().after(d2)) {
                 Date d = gcal.getTime();
                 System.out.println(d);
                 String startD=format.format(d);
                 JsonNode jsonList = Json.parse(callClickAPI("&type=engagement-actions&date="+startD+"&limit=all"));
                 
                 
                 for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
              	   	if(title.equals(obj.get("title").textValue())){
                	// ClickyPagesVM vm = new ClickyPagesVM();
              	   	String value = obj.get("value").textValue();
              	   	Long l=(long)Integer.parseInt(value);
              	  lonnn.add(l);
              	   //	vm.value_percent = obj.get("value_percent").textValue();
              	   	
              	   	String chartDate=format1.format(d);
              	     dates.add(chartDate);
              	   
              	   	//clickyList.add(vm);
              	   	}
              	   	}
                gcal.add(Calendar.DAY_OF_MONTH, 1);
                 
             }
             vm.data=lonnn;
             data.add(vm);
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
	    return ok(Json.toJson(map));
	    
    }
    

    public static Result getEngagementTimeChart(String startDate,String endDate,String title){
    	String params = null;
    	System.out.println(startDate);
    	System.out.println(endDate);
    	Date d1 = null;
        Date d2 = null;
        Map<String, Object> map = new HashMap<>();
        List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		map.put("dates",dates);
		map.put("data",data);
        List<ClickyPagesVM> clickyList = new ArrayList<>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
    	 try {
             d1 = format.parse(startDate);
             d2 = format.parse(endDate);

             long diff = d2.getTime() - d1.getTime();

             long diffDays = diff / (24 * 60 * 60 * 1000);
             Integer days=(int)diffDays;
          Date beforeStart = DateUtils.addDays(d1, -days); 
             String newDate=format.format(beforeStart);
             System.out.print(newDate + " newDate ");
     	   	
             GregorianCalendar gcal = new GregorianCalendar();
             gcal.setTime(d1);
             sendDataVM vm=new sendDataVM();
             vm.name=title;
             List<Long> lonnn = new ArrayList<>();
             while (!gcal.getTime().after(d2)) {
                 Date d = gcal.getTime();
                 System.out.println(d);
                 String startD=format.format(d);
                 JsonNode jsonList = Json.parse(callClickAPI("&type=engagement-times&date="+startD+"&limit=all"));
                 
                 
                 for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
              	   	if(title.equals(obj.get("title").textValue())){
                	// ClickyPagesVM vm = new ClickyPagesVM();
              	   	String value = obj.get("value").textValue();
              	   	Long l=(long)Integer.parseInt(value);
              	  lonnn.add(l);
              	   //	vm.value_percent = obj.get("value_percent").textValue();
              	   	
              	   	String chartDate=format1.format(d);
              	     dates.add(chartDate);
              	   
              	   	//clickyList.add(vm);
              	   	}
              	   	}
                gcal.add(Calendar.DAY_OF_MONTH, 1);
                 
             }
             vm.data=lonnn;
             data.add(vm);
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
	    return ok(Json.toJson(map));
	    
    }
    
    public static Result getTrafficScouresChart(String startDate,String endDate,String title){
    	String params = null;
    	System.out.println(startDate);
    	System.out.println(endDate);
    	Date d1 = null;
        Date d2 = null;
        Map<String, Object> map = new HashMap<>();
        List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		map.put("dates",dates);
		map.put("data",data);
        List<ClickyPagesVM> clickyList = new ArrayList<>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
    	 try {
             d1 = format.parse(startDate);
             d2 = format.parse(endDate);

             long diff = d2.getTime() - d1.getTime();

             long diffDays = diff / (24 * 60 * 60 * 1000);
             Integer days=(int)diffDays;
          Date beforeStart = DateUtils.addDays(d1, -days); 
             String newDate=format.format(beforeStart);
             System.out.print(newDate + " newDate ");
     	   	
             GregorianCalendar gcal = new GregorianCalendar();
             gcal.setTime(d1);
             sendDataVM vm=new sendDataVM();
             vm.name=title;
             List<Long> lonnn = new ArrayList<>();
             while (!gcal.getTime().after(d2)) {
                 Date d = gcal.getTime();
                 System.out.println(d);
                 String startD=format.format(d);
                 JsonNode jsonList = Json.parse(callClickAPI("&type=traffic-sources&date="+startD+"&limit=all"));
                 
                 
                 for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
              	   	if(title.equals(obj.get("title").textValue())){
                	// ClickyPagesVM vm = new ClickyPagesVM();
              	   	String value = obj.get("value").textValue();
              	   	Long l=(long)Integer.parseInt(value);
              	  lonnn.add(l);
              	   //	vm.value_percent = obj.get("value_percent").textValue();
              	   	
              	   	String chartDate=format1.format(d);
              	     dates.add(chartDate);
              	   
              	   	//clickyList.add(vm);
              	   	}
              	   	}
                gcal.add(Calendar.DAY_OF_MONTH, 1);
                 
             }
             vm.data=lonnn;
             data.add(vm);
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
	    return ok(Json.toJson(map));
	    
    }
    
     public static Result geLendingPageData(){
    	 Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
 		ClickyPagesVM vm1 = form.get();
 		String startDate=vm1.startDateFilter;
 		String endDate=vm1.endDateFilter;
 		String type=vm1.typeOfReferrer;
 		String refferal = vm1.locationFlag;
 		
 		
 	String params = null;
 	System.out.println(startDate);
 	System.out.println(endDate);
 	Date d1 = null;
     Date d2 = null;
    	 
    	// Date d1= null;
 		//Date d2= null;
 		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 		try{
 			 d1 = format.parse(startDate);
 	         d2 = format.parse(endDate);
 	    } catch (Exception e) {
 	        e.printStackTrace();
 	    }
 		
 		List<ClickyVisitorsList> landingObjList = ClickyVisitorsList.findByLandingPageAndDate(type, d1, d2);
		List<ClickyVisitorsList> allLandinglist = ClickyVisitorsList.getAll(d1, d2);
		
		List <ClickyPagesVM> VMs = new ArrayList<>();
		List<ClickyPlatformVM> platformvm =new ArrayList<>();
		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
		ClickyPagesVM vm = new ClickyPagesVM();
		double count1=0.0;
		double count2=0.0;
		double count3=0.0;
		double count4=0.0;
		double count5=0.0;
		double count6=0.0;
		double count7=0.0;
		Integer vistValue = 0;
		 for(ClickyVisitorsList lis:landingObjList){
	     	if(lis.averageAction != null){
	     		count1=Double.parseDouble(lis.averageAction);
	     	}
			if(lis.bounceRate != null){
				count6=Double.parseDouble(lis.bounceRate);	
				     	}
			if(lis.averageTime != null){
				count5=Double.parseDouble(lis.averageTime);	
				}
			if(lis.timeTotal != null){
				count4=count4+Double.parseDouble(lis.timeTotal);
				}
			if(lis.visitors != null){
				 count2=Double.parseDouble(lis.visitors);
				}
			if(lis.uniqueVisitor!= null){
				count3=Double.parseDouble(lis.uniqueVisitor);
				}
			if(lis.actions != null){
			count7=count7+Double.parseDouble(lis.actions);
			}
			
			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
				if (langValue == null) {
				 vistValue = vistValue + Integer.parseInt(lis.visitors);
				 mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitors));
				}
			
		 }
		 
		 
		 
		 double countAll1=0.0;
			double countAll2=0.0;
			double countAll3=0.0;
			double countAll4=0.0;
			double countAll5=0.0;
			double countAll6=0.0;
			double countAll7=0.0;
			 for(ClickyVisitorsList list:allLandinglist){
				 if(list.averageAction != null){
					 countAll1=count1+Double.parseDouble(list.averageAction);
			     	}
					if(list.bounceRate != null){
						 countAll6=count6+Double.parseDouble(list.bounceRate);	
						     	}
					if(list.averageTime != null){
						countAll5=count5+Double.parseDouble(list.averageTime);	
						}
					if(list.timeTotal != null  && !list.timeTotal.equals("")){
						 countAll4=countAll4+Double.parseDouble(list.timeTotal);
						}
					if(list.visitors != null){
						countAll2=allLandinglist.size();
						}
					if(list.uniqueVisitor != null){
						countAll3=allLandinglist.size();
						}
					if(list.actions != null && !list.actions.equals("")){
						 countAll7=countAll7+Double.parseDouble(list.actions);
					}
				 
				 
		   			
			 }
		 
			 ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors = (double)vistValue;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count7/count2;
			 cVm3.all_visitors = countAll7/countAll2;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count4/count2;
			 cVm5.all_visitors = countAll4/countAll2;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);
		 
		 vm.averageAct=count1;
		 vm.visitor=count2;
		 vm.uniqueV=count3;
		 vm.totalT=count4;
		 vm.averageT=count5;
		 vm.bounceR=count6;
		 vm.action=count7;
		
		 VMs.add(vm);

	 	
	 	return ok(Json.toJson(platformvm));

     }
     
     


      
     
     public static Result getSessionData(String sessionId,String startDate,String endDate){
     	String params=null;
     	List<ClickyPagesVM> clickyList = new ArrayList<>();
     		params = "&type=visitors-list&session_id="+sessionId+"&date="+startDate+","+endDate+"&limit=all";
     	//https://api.clicky.com/api/stats/4?output=json&site_id=100875513&session_id=4247626074&sitekey=d6e7550038b4a34c&type=actions-list&date=last-7-days
     		try {

      	   	JsonNode jsonList = Json.parse(callClickAPI(params));
      	   	for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
      	   	ClickyPagesVM vm = new ClickyPagesVM();
      	   	vm.time=obj.get("time").textValue();
      	  String newtime[]=obj.get("time_pretty").textValue().split(",") ;	
      	  vm.newTime=newtime[1];
      	  String newDate[]=newtime[0].split(" ");
      	  vm.newDate=newDate[1]+" "+newDate[2]+" "+newDate[3];
      	  vm.timePretty=obj.get("time_pretty").textValue();
      	  vm.ipAddress=obj.get("ip_address").textValue();
      	  vm.uid=obj.get("uid").textValue();
      	  vm.geolocation=obj.get("geolocation").textValue();
      	  vm.organization=obj.get("organization").textValue();
      	  vm.actions=obj.get("actions").textValue();
      	  vm.timeTotal=obj.get("time_total").textValue();
      	  vm.latitude=obj.get("latitude").textValue();
      	  vm.longitude=obj.get("longitude").textValue();
      	  vm.referrerDomain=obj.get("referrerDomain").textValue();
      	  vm.referrerUrl=obj.get("referrerUrl").textValue();
      	   clickyList.add(vm);
      	   	
      	   	}
              
          } catch (Exception e) {
              e.printStackTrace();
          }
     	
     	return ok(Json.toJson(clickyList));
     }
     public static Result getSession(String sessionId,String startDate,String endDate){
      	String params=null;
      	List<ClickyPagesVM> clickyList = new ArrayList<>();
      		params = "&type=actions-list&session_id="+sessionId+"&date="+startDate+","+endDate+"&limit=all";
      	//https://api.clicky.com/api/stats/4?output=json&site_id=100875513&session_id=4247626074&sitekey=d6e7550038b4a34c&type=actions-list&date=last-7-days
      		try {

       	   	JsonNode jsonList = Json.parse(callClickAPI(params));
       	   	for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
       	   	ClickyPagesVM vm = new ClickyPagesVM();
       	   	vm.time=obj.get("time").textValue();
       	  String newtime[]=obj.get("time_pretty").textValue().split(",") ;	
       	  vm.newTime=newtime[1];
       	  String newDate[]=newtime[0].split(" ");
       	  vm.newDate=newDate[1]+" "+newDate[2]+" "+newDate[3];
       	  vm.timePretty=obj.get("time_pretty").textValue();
       	  vm.ipAddress=obj.get("ip_address").textValue();
       	  vm.uid=obj.get("uid").textValue();
       	 
        vm.sessionId=obj.get("session_id").textValue();
       	  vm.actionType=obj.get("action_type").textValue();
       	  vm.actionTitle=obj.get("action_title").textValue();
       	  vm.actionUrl=obj.get("action_url").textValue();
       	 
       	  String newAction[]=vm.actionUrl.split(".com");
       	  vm.newActionUrl=newAction[1];
       	vm.heatmapUrl=null;
       	 List<ClickyPagesList> list=ClickyPagesList.getHeatMapUrl(vm.actionUrl);
       	 if(list.size() != 0){
       		 vm.heatmapUrl=list.get(0).url;
       	 }
       	  vm.statsUrl=obj.get("stats_url").textValue();
       	   clickyList.add(vm);
       	   	
       	   	}
               
           } catch (Exception e) {
               e.printStackTrace();
           }
      	
      	return ok(Json.toJson(clickyList));
      }
     
     
     
     
     
     

    
			public static Result getSessionIdForRequest(Long id){
				
				
				RequestMoreInfo List = RequestMoreInfo.findById(id);
				
				return ok(Json.toJson(List));
			} 

		public static Result getSessionIdForSchedule(Long id){
			
			
			ScheduleTest List = ScheduleTest.findById(id);
			
			return ok(Json.toJson(List));
		}

		public static Result getSessionIdForTrade(Long id){
			
			
			TradeIn List = TradeIn.findById(id);
			
			
			return ok(Json.toJson(List));
		}
		
	public static Result getSessionIdData(String id){
			
			String sessionId=id;
			List <ClickyVisitorsList> List = ClickyVisitorsList.getClickyCustomSessionData(id);
			
			if(List.size()==0){
				
				String params = "&type=visitors-list&date=last-7-days&limit=all";
				List<ClickyPagesVM> list =new ArrayList<>();
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArray.length();i++){
						String customSessionId=null;
			    			try{
			    				
			    				JSONObject jsonObj = new JSONObject(jsonArray.getJSONObject(i).get("custom").toString());
			    				customSessionId=jsonObj.get("session_Id").toString();
			    				
			    				if(sessionId.equals(customSessionId)){
			    					
			    					ClickyPagesVM vm = new ClickyPagesVM();
			    					
			    					
			    					String data = jsonArray.getJSONObject(i).get("time").toString();
					    			String data1 = jsonArray.getJSONObject(i).get("time_pretty").toString();
					    			ClickyVisitorsList cVisitorsList = new ClickyVisitorsList();
					    			vm.time=jsonArray.getJSONObject(i).get("time").toString();
					    			vm.timePretty=jsonArray.getJSONObject(i).get("time_pretty").toString();
					    			vm.timeTotal=jsonArray.getJSONObject(i).get("time_total").toString();
					    			vm.ipAddress=jsonArray.getJSONObject(i).get("ip_address").toString();
					    			vm.uid=jsonArray.getJSONObject(i).get("uid").toString();
					    			vm.sessionId=jsonArray.getJSONObject(i).get("session_id").toString();
					    			vm.actions=jsonArray.getJSONObject(i).get("actions").toString();
					    			cVisitorsList.setTotalVisits(jsonArray.getJSONObject(i).get("total_visits").toString());
					    			vm.landingPage=jsonArray.getJSONObject(i).get("landing_page").toString();
					    			vm.webBrowser=jsonArray.getJSONObject(i).get("web_browser").toString();
					    			vm.operatingSystem=jsonArray.getJSONObject(i).get("operating_system").toString();
					    			vm.screenResolution=jsonArray.getJSONObject(i).get("screen_resolution").toString();
					    			vm.language=jsonArray.getJSONObject(i).get("language").toString();
					    			
					    			try{
					    				vm.referrerUrl=jsonArray.getJSONObject(i).get("referrer_url").toString();
					    			}
					    			catch(Exception e){
					    				e.printStackTrace();
					    			}
					    			try{
					    				
					    				JSONObject jsonObj1 = new JSONObject(jsonArray.getJSONObject(i).get("custom").toString());
					    				vm.customSessionId=jsonObj1.get("session_Id").toString();
					    				
					    			}
					    			catch(Exception e){
					    				e.printStackTrace();
					    			}
					    			try{
			                    	   vm.referrerType=jsonArray.getJSONObject(i).get("referrer_type").toString();
					    			}
					    			catch(Exception e){
					    				e.printStackTrace();
					    			}
					    			
			                       try{
			                    	   vm.referrerDomain=jsonArray.getJSONObject(i).get("referrer_domain").toString();
					    				
					    			}
					    			catch(Exception e){
					    				e.printStackTrace();
					    			}
					    			vm.totalVisits=jsonArray.getJSONObject(i).get("total_visits").toString();
					    			vm.geolocation=jsonArray.getJSONObject(i).get("geolocation").toString();
					    			vm.country=jsonArray.getJSONObject(i).get("country_code").toString();
					    			vm.latitude=jsonArray.getJSONObject(i).get("latitude").toString();
					    			vm.longitude=jsonArray.getJSONObject(i).get("longitude").toString();
					    			//cVisitorsList.setHostname(jsonArray.getJSONObject(i).get("hostname").toString());
					    			vm.organization=jsonArray.getJSONObject(i).get("organization").toString();
					    			vm.statsUrl=jsonArray.getJSONObject(i).get("stats_url").toString();
					    			vm.hostname=jsonArray.getJSONObject(i).get("hostname").toString();
					    			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MMM-dd");
					    			String arr[] = jsonArray.getJSONObject(i).get("time_pretty").toString().split(" ");
									String arrNew[] = arr[3].split(",");
									String checkDate = arrNew[0]+"-"+arr[1]+"-"+arr[2];
									Date thedate = null;
									try {
										thedate = df1.parse(checkDate);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					    			
					    			vm.dateClick=thedate;
					    			
					    			list.add(vm);
			    					
			    				}
			    				
			   
		   			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			
					}	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(list.size() != 0){
				
					return ok(Json.toJson(list.get(0)));
				}
				else{
					
					return ok();
				}
				
			}
			else{
				return ok(Json.toJson(List.get(0)));
			}
			
		}


	public static Result getIPAddress(String id){
		
		List<ClickyVisitorsList> List = ClickyVisitorsList.findByTitle(id);
		return ok(Json.toJson(List.get(0)));
	}

    public static Result getVisitorData(Long id){
    	
    	
    	ClickyVisitorsList List = ClickyVisitorsList.findById(id);
    	
    	
    	return ok(Json.toJson(List));
    }
    
    public static Result getHeatMapListDale(String startD,String endD){
    	
    	String params = null;
    	params = "&type=pages&heatmap_url=1&date="+startD+","+endD+"&limit=all";
    	//Location locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	List<ClickyPagesVM> cList = new ArrayList<>();
    	try {
    		
			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			for(int i=0;i<jsonArray.length();i++){
    			String data = jsonArray.getJSONObject(i).get("url").toString();
    			String arr[] = data.split("#_");
    			try {
	    		//	String findLoc[] = arr[0].split("locationId=");
	    		//	String locString = findLoc[1].replace("+", " ");
	    			//if(locations.name.equals(locString)){
	    				ClickyPagesVM cPagesVM = new ClickyPagesVM();
	    				cPagesVM.value = jsonArray.getJSONObject(i).get("value").toString();
	    				cPagesVM.value_percent = jsonArray.getJSONObject(i).get("value_percent").toString();
	    				cPagesVM.title = jsonArray.getJSONObject(i).get("title").toString();
	    				cPagesVM.stats_url = jsonArray.getJSONObject(i).get("stats_url").toString();
	    				cPagesVM.url = jsonArray.getJSONObject(i).get("url").toString();
	    				cPagesVM.showUrl = arr[0];
	    				if(!cPagesVM.title.equals("Some page")){
	    				cList.add(cPagesVM);
	    			}
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		
    			
			}	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	return ok(Json.toJson(cList));
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
    
    public static Result getAllVehicleDemographicsInData(){
    	Map map = new HashMap(2);
		Map<String, Integer> mapRM = new HashMap<String, Integer>();
		Map<String, Integer> mapWebBro = new HashMap<String, Integer>();
		Map<String, Integer> maplocation = new HashMap<String, Integer>();
		Map<String, Integer> mapoperatingSystem = new HashMap<String, Integer>();
		Map<String, Integer> mapSreenResoluation = new HashMap<String, Integer>();
		
		List<ClickyVisitorsList> cList= ClickyVisitorsList.getfindAll();
		//String params = "&type=visitors-list&date=last-30-days&limit=all";
    	//try {
    		
    		int lagCount = 1;
			//JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			for(ClickyVisitorsList click:cList){
    			String data = click.landingPage;
    			String arr[] = data.split("/");
    			if(arr.length > 5){
    			  if(arr[5] != null){
    				//  if(arr[5].equals(value)){
    					  
    					  Integer langValue = mapRM.get(click.language); 
    						if (langValue == null) {
    							mapRM.put(click.language, lagCount);
    						}else{
    							mapRM.put(click.language, mapRM.get(click.language) + 1);
    						}
    						
    						 Integer mapWebBroValue = mapWebBro.get(click.webBrowser); 
     						if (mapWebBroValue == null) {
     							mapWebBro.put(click.webBrowser, lagCount);
     						}else{
     							mapWebBro.put(click.webBrowser, mapWebBro.get(click.webBrowser) + 1);
     						}
     						
     						Integer maplocationValue = maplocation.get(click.geolocation); 
     						if (maplocationValue == null) {
     							maplocation.put(click.geolocation, lagCount);
     						}else{
     							maplocation.put(click.geolocation, maplocation.get(click.geolocation) + 1);
     						}
     						
     						Integer mapoperatingSystemValue = mapoperatingSystem.get(click.operatingSystem); 
     						if (mapoperatingSystemValue == null) {
     							mapoperatingSystem.put(click.operatingSystem, lagCount);
     						}else{
     							mapoperatingSystem.put(click.operatingSystem, mapoperatingSystem.get(click.operatingSystem) + 1);
     						}
     						
     						Integer mapSreenResoluationValue = mapSreenResoluation.get(click.screenResolution); 
     						if (mapSreenResoluationValue == null) {
     							mapSreenResoluation.put(click.screenResolution, lagCount);
     						}else{
     							mapSreenResoluation.put(click.screenResolution, mapSreenResoluation.get(click.screenResolution) + 1);
     						}
    						
     						
    				 // }
    			  }
    			}
    			
			}	
		/*} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

    	map.put("language", mapRM);
    	map.put("location", maplocation);
    	map.put("webBrowser", mapWebBro);
    	map.put("operatingSystem", mapoperatingSystem);
    	map.put("screenResoluation", mapSreenResoluation);
    	
    	
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
    
    private static String getbusinessname(String businessname) {
    	StringBuffer response = new StringBuffer();
    	try {
    		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyCUNed71Bq6SCyI5SauRA7Rs9xT319HPP0&input="+businessname+"&callback=initMap";
		
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
    
    public static Result getbusinessData(String businessname){
    	//JsonNode onlineVisitorsNode = Json.parse(getbusinessname(businessname));
    	//return ok(Json.parse(onlineVisitorsNode));
    	String name = businessname.replaceAll(" ", " ");
    	return ok(Json.parse(getbusinessname(name)));
    }
    
    public static Result getMonthlyVisitorsStats(String startDate, String endDate) {
    	
    	Calendar c = Calendar.getInstance();
    	String[] monthsArr = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    	c.add(Calendar.MONTH, -11);
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	List<Integer> allVisitor = new ArrayList<Integer>(12);
    	List<Integer> onlineVisitor = new ArrayList<Integer>(12);
    	List<Integer> actionsList = new ArrayList<Integer>(12);
    	List<Integer> averageActionsList = new ArrayList<Integer>(12);
    	List<String> months = new ArrayList<String>(12);
    	
    	Location locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	Calendar c1 = Calendar.getInstance();
    	Date sDa = null;
    	Date eDa = null;
		try {
			sDa = dateFormat.parse(startDate);
			eDa = dateFormat.parse(endDate);
			//c1.setTime(dateFormat.parse(startDate));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//c1.add(Calendar.DATE, 1);
		Date addDate = sDa;
		int addFlag = 0;
		while(addFlag <= 0){
			
        	JsonNode node = Json.parse(callClickAPI("&date="+dateFormat.format(addDate)+"&type=visitors,visitors-new,actions,actions-average&limit=all"));
        	
        	JsonNode allVisitorNode = node.get(0);
    		JsonNode onlineVisitorNode = node.get(1);
    		JsonNode actionsNode = node.get(2);
    		JsonNode averageActionsNode = node.get(3);
    		allVisitor.add(allVisitorNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		onlineVisitor.add(onlineVisitorNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		actionsList.add(actionsNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		averageActionsList.add(averageActionsNode.get("dates").get(0).get("items").get(0).get("value").asInt());
    		months.add(dateFormat.format(addDate));
    		
    		if(addDate.equals(eDa)){
				addFlag = 1;
			}
			c1.setTime(addDate);
			c1.add(Calendar.DATE, 1);
			addDate = c1.getTime();
				
		}
    	
    	/*for(int i=0;i<12;i++) {
    		String year = c.get(Calendar.YEAR)+"";
        	String month = c.get(Calendar.MONTH)+1>9?(c.get(Calendar.MONTH)+1)+"":"0"+(c.get(Calendar.MONTH)+1);
    		JsonNode node = Json.parse(callClickAPI("&date="+year+"-"+month+"&type=visitors,visitors-new,actions,actions-average&limit=all"));
        	//JsonNode node = Json.parse(callClickAPI("&date="+startDate+","+endDate+"&type=visitors,visitors-new,actions,actions-average&limit=all"));
    		
        	
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
    	}*/
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
    		try{
	    		//String findLoc[] = obj.get("url").textValue().split("locationId=");
	    		String locString =  obj.get("url").textValue().replace("+", " ");
	    		//if(locations.name.equals(locString)){
	    			String sUrl = obj.get("url").textValue().replace("?", "");
	    			String arr[] = sUrl.split("/");
	        		PageVM vm = new PageVM();
	        		vm.count = obj.get("value").textValue();
	        		if(arr[arr.length-2].equals("mobile")) {
	        			vm.pageUrl = "mobile "+arr[arr.length-1];	
	        			vm.fullUrl = obj.get("url").textValue();
	    	    	} else {
	    	    		if(arr[arr.length-1].equals("") || arr[arr.length-1].equals("glivr")) {
	    	    			vm.pageUrl = "Home Page";
	    	    			vm.fullUrl = obj.get("url").textValue();
	    	    		} else {
	    	    			if(arr[arr.length-1].equals("aboutUs")){
	    	    				vm.pageUrl = "About Us";
	    	    			}else if(arr[arr.length-1].equals("blog")){
	    	    				vm.pageUrl = "Blog";
	    	    			}else if(arr[arr.length-1].equals("contactUs")){
	    	    				vm.pageUrl = "Contact Us";
	    	    			}else if(arr[arr.length-1].equals("warranty")){
	    	    				vm.pageUrl = "Warranty";
	    	    			}else{
	    	    				vm.pageUrl = arr[arr.length-1];
	    	    			}
	    	    			
	    	    			vm.fullUrl = obj.get("url").textValue();
	    	    		}
	    	    	}
	        		if(!vm.pageUrl.equals("pagesome=query")){
	        		pagesList.add(vm);
	    		}
    		}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
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
    
    public static Result getVisitedData(Integer userKey,String type,String filterBy,String search,String searchBy,String vehicles,String startDate,String endDate) {
    	
    	Map result = new HashMap(3);
    	AuthUser user = AuthUser.findById(userKey);
    	long locationId = 0;
    	if(user.location == null){
    		locationId = 0L;
    	}else{
    		locationId = user.location.id;
    	}
    	
    	topListings(type,filterBy,search,searchBy,locationId,user,result,vehicles,"0",startDate,endDate);
    	return ok(Json.toJson(result));
    }
    
    public static void topListings(String type,String filterBy,String search,String searchBy,Long locationId,AuthUser user,Map result,String vehicles,String gmInManag,String startDate,String endDate){
    	
    	
    	
    	Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
		String start1 = "";
		String end1 = "";
		Date start = null;
		Date end = null;
		Date startDat=null;
		Date enddat=null;
		
		
		//if(type.equals("datewise")) {
		try {
			startDat=df2.parse(startDate);
			enddat=df2.parse(endDate);
			start=startDat;
			end=enddat;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//}
		
		/*if(type.equals("week")) {
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
		
		else*/ if(type.equals("allTime")) {
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
    	/*String params1 = "&type=visitors-list&date=last-30-days&limit=all";
    	if(type.equals("week")){
    		params = "&type=visitors-list&date=last-7-days&limit=all";
    	}else if(type.equals("month")){*/
    		//params = "&type=visitors-list&date=last-30-days&limit=all";
    	//}else  if(type.equals("allTime")){
    		///params = "&type=visitors-list&date="+start1+","+end1+"&limit=all";
    	//}
    	
    	List<ClickyVisitorsList> cList = ClickyVisitorsList.getAll(start,end);
    	
    	/*-------------------edit click 12-05-2016---------------------------------------*/	
    		//String resultStr = callClickAPI(params);
    	    	
    //	JsonNode jsonNode = Json.parse(resultStr).get(0).get("dates").get(0).get("items");
    	
    		/*-------------------edit click 12-05-2016---------------------------------------*/	
    		
    	List<String> vins = new ArrayList<String>();
    	List<String> vins1 = new ArrayList<String>();
    	
    	Map<String,Integer> pagesCount = new HashMap<String,Integer>();
    	Map<String,Integer> vinUnik = new HashMap<String,Integer>();
    	
    	Map<String,Integer> pagesCount1 = new HashMap<String,Integer>();
    	Map<String,Integer> vinUnik1 = new HashMap<String,Integer>();
    	int i = 1;
    	
    	/*-------------------edit click 12-05-2016---------------------------------------*/
    	
    	for(ClickyVisitorsList item:cList) {
    		String data = item.landingPage;
			String arrVin[] = data.split("/");
			if(arrVin.length > 5){
				if(!arrVin[5].equals("\"")){
					String van[] = arrVin[5].split("\"");
					Vehicle vehicle = null;
					if(user.role.equals("Sales Person") || user.role.equals("Manager") || gmInManag.equals("1")){
						vehicle = Vehicle.findByVinAndStatusForGM(van[0],Location.findById(locationId));
					}else{
						vehicle = Vehicle.findByVinAndStatus(van[0]);
					}
					
					if(vehicle !=null){
						/*String arr[] = item.get("time_pretty").toString().split(" ");
						String arrNew[] = arr[3].split(",");
						checkDate = arrNew[0]+"-"+arr[1]+"-"+arr[2];*/
						//try {
							//thedate = df1.parse(checkDate);
							thedate = item.DateClick;
						/*} catch (ParseException e) {
							e.printStackTrace();
						}*/
						
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
    	}
    	
    	/*-------------------edit click 12-05-2016---------------------------------------*/
    	
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
				//if((rInfo.requestDate.after(start) && rInfo.requestDate.before(end)) || rInfo.requestDate.equals(end) || rInfo.requestDate.equals(start)){
					vinUnik.put(rInfo.vin, 1);
				//}
			}
			for(ScheduleTest sTest: sTests){
				//if((sTest.scheduleDate.after(start) && sTest.scheduleDate.before(end)) || sTest.scheduleDate.equals(end) || sTest.scheduleDate.equals(start)){
					vinUnik.put(sTest.vin, 1);
				//}	
			}
			for(TradeIn tradeIn: tIns){
				//if((tradeIn.tradeDate.after(start) && tradeIn.tradeDate.before(end)) || tradeIn.tradeDate.equals(end) || tradeIn.tradeDate.equals(start)){
					vinUnik.put(tradeIn.vin, 1);
				//}
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
    		int req = 0;
    		int sche = 0;
    		int trad = 0;
    		for(RequestMoreInfo rInfo :rInfos){
				//if((rInfo.requestDate.after(start) && rInfo.requestDate.before(end)) || rInfo.requestDate.equals(end) || rInfo.requestDate.equals(start)){
					req++;
				//}
			}
			for(ScheduleTest sTest: sList){
				//if((sTest.scheduleDate.after(start) && sTest.scheduleDate.before(end)) || sTest.scheduleDate.equals(end) || sTest.scheduleDate.equals(start)){
					sche++;
				//}	
			}
			for(TradeIn tradeIn: tIns){
				//if((tradeIn.tradeDate.after(start) && tradeIn.tradeDate.before(end)) || tradeIn.tradeDate.equals(end) || tradeIn.tradeDate.equals(start)){
					trad++;
				//}
			}
    		
    		analyticalVM.leadsCount = req + sche + trad;
    		
    		if(pagesCount1.get(vehicle.getVin()) == null){
    			analyticalVM.count = 0;
    		}else{
    			analyticalVM.count = pagesCount1.get(vehicle.getVin());
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
    		analyticalVM.name=vehicle.getTitle();
    		if(!searchBy.equals("0") && !search.equals("0")){
	    		if(searchBy.equals("Model")){
	    			String arrNew[] = search.split("_&_");
					
    				if(vehicle.model.toUpperCase().startsWith(arrNew[0].toUpperCase()) && vehicle.make.toUpperCase().startsWith(arrNew[1].toUpperCase())){
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
    				String arrNew[] = search.split("_&_");
    				if(vehicle.model.toUpperCase().startsWith(arrNew[0].toUpperCase()) && vehicle.make.toUpperCase().startsWith(arrNew[1].toUpperCase())){
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
    			//if((rInfo.requestDate.after(start) && rInfo.requestDate.before(end)) || rInfo.requestDate.equals(end) || rInfo.requestDate.equals(start)){
    				requCount++;
    			//}
    		}
    		
    		for(ScheduleTest sInfo : sList){
    			//if((sInfo.scheduleDate.after(start) && sInfo.scheduleDate.before(end)) || sInfo.scheduleDate.equals(end) || sInfo.scheduleDate.equals(start)){
    				schedCount++;
    			//}
    		}
    		
    		for(TradeIn tInfo : tIns){
    			//if((tInfo.tradeDate.after(start) && tInfo.tradeDate.before(end)) || tInfo.tradeDate.equals(end) || tInfo.tradeDate.equals(start)){
    				tradeCount++;
    			//}
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
				//if((alt.currDate.after(start) && alt.currDate.before(end)) || alt.currDate.equals(end) || alt.currDate.equals(start)){
					if (vehicle.postedDate.before(alt.currDate) || vehicle.postedDate.equals(alt.currDate)) {
						anVm.followerCount++;
		    		}
				//}
			}
    		if(!searchBy.equals("0") && !search.equals("0")){
    			if(searchBy.equals("Model")){
    				String arrNew[] = search.split("_&_");
    				if(vehicle.model.toUpperCase().startsWith(arrNew[0].toUpperCase()) && vehicle.make.toUpperCase().startsWith(arrNew[1].toUpperCase())){
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
     	AuthUser users;
     	users = AuthUser.findById(id);
     	if(users == null){
     		users = (AuthUser) getLocalUser();
     	}
     	
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
     	
     	int cancelRequestLeadCount = 0;
     	int cancelScheduleLeadCount = 0;
     	int cancelTradeInLeadCount = 0;
     	
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
     	
     	List<RequestMoreInfo> reCancelInfo = null;
     	List<ScheduleTest> scCancelInfo = null;
     	List<TradeIn> trCancelInfo = null;
     	
	     	lostRInfo = RequestMoreInfo.findAllLostSch(users);
	     	lostSList = ScheduleTest.findAllLostSch(users);
	     	lostTradeIns = TradeIn.findAllLostSch(users);
     	
     		rInfo = RequestMoreInfo.findAllSeenSch(users);
     		sList = ScheduleTest.findAllAssigned(users);
     		tradeIns = TradeIn.findAllSeenSch(users);
     		
     		rInfoAll = RequestMoreInfo.findAllByNotOpenLead(users);
     		sListAll = ScheduleTest.findAllByNotOpenLead(users);
     		tradeInsAll = TradeIn.findAllByNotOpenLead(users);
     		
     		Long difffoll = 0L;
     		Long countFollo = 0L; 
     		SimpleDateFormat convdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     		SimpleDateFormat convdTime = new SimpleDateFormat("HH:mm:ss");
     	for(RequestMoreInfo rMoreInfo:rInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
     			requestLeadCount++;
     		}
     	}
     	
     	for(ScheduleTest sTest:sList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
     			scheduleLeadCount++;
     		}
     	}

     	for(TradeIn tIn:tradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
     				tradeInLeadCount++;
     		}
     	}
     	int folloLead = 0;
     	for(RequestMoreInfo rMoreInfo:rInfoAll){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
     			requestLeadCount1++;
     			int twoTimes = 0;
     			difffoll = 0L;
     			List<UserNotes> uNotes = UserNotes.findRequestMoreAndFirstAdd(rMoreInfo);
     			for(UserNotes uN:uNotes){
     				if(twoTimes < 2){
     					twoTimes++;
	     				if(!uN.note.equals("Lead has been created")){
	     					folloLead++;
	         				String CretaeDateTime = df1.format(uN.createdDate)+" "+convdTime.format(uN.createdTime);
	         				
	         				Date cDate = null;
	         				try {
	    						cDate = convdf.parse(CretaeDateTime);
	    					} catch (ParseException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	         				
	         				difffoll = cDate.getTime() - rMoreInfo.requestTime.getTime();
	         				break;
	     				}
     				}
     			}
     			countFollo = countFollo + difffoll;
     		}
     	}
     	
     	for(ScheduleTest sTest:sListAll){
     	if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
     			scheduleLeadCount1++;
     			int twoTimes = 0;
     			difffoll = 0L;
     			List<UserNotes> uNotes = UserNotes.findScheduleTestAndFirstAdd(sTest);
     			for(UserNotes uN:uNotes){
     				if(twoTimes < 2){
     					twoTimes++;
	     				if(!uN.note.equals("Lead has been created")){
	     					folloLead++;
	         				String CretaeDateTime = df1.format(uN.createdDate)+" "+convdTime.format(uN.createdTime);
	         				
	         				Date cDate = null;
	         				try {
	    						cDate = convdf.parse(CretaeDateTime);
	    					} catch (ParseException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	         				
	         				difffoll = cDate.getTime() - sTest.scheduleTime.getTime();
	         				//difffoll = (difffoll / 1000 /60 /60 /24);
	         				break;
	     				}
     				}
     			}
     			countFollo = countFollo + difffoll;
     	}
     	}

     	for(TradeIn tIn:tradeInsAll){
     	if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
 				tradeInLeadCount1++;
 				int twoTimes = 0;
 				difffoll = 0L;
     			List<UserNotes> uNotes = UserNotes.findTradeInAndFirstAdd(tIn);
     			for(UserNotes uN:uNotes){
     				if(twoTimes < 2){
	     				if(!uN.note.equals("Lead has been created")){
	     					folloLead++;
	         				
	         				String CretaeDateTime = df1.format(uN.createdDate)+" "+convdTime.format(uN.createdTime);
	         				
	         				Date cDate = null;
	         				try {
	    						cDate = convdf.parse(CretaeDateTime);
	    					} catch (ParseException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	         				
	         				difffoll = cDate.getTime() - tIn.tradeTime.getTime();
	         				//difffoll = (difffoll / 1000 /60 /60 /24);
	         				break;
	     				}
     				}
     			}
     			countFollo = countFollo + difffoll;
 				
     		}
     	}
     	
    	for(RequestMoreInfo rMoreInfo:lostRInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
     			lostRequestLeadCount++;
     		}
     	}
     	
     	for(ScheduleTest sTest:lostSList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
     			lostScheduleLeadCount++;
     		}
     	}

     	for(TradeIn tIn:lostTradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
     				lostTradeInLeadCount++;
     		}
     	}	
 		
     	
     	
     	int countLeads1 = requestLeadCount1 + scheduleLeadCount1 + tradeInLeadCount1;
     	int countLeads = requestLeadCount + scheduleLeadCount + tradeInLeadCount;
    	int countLostLeads = lostRequestLeadCount + lostScheduleLeadCount + lostTradeInLeadCount;
     	
    	
    	LocationWiseDataVM lDataVM = new LocationWiseDataVM();
     	
     	if(users !=null){
     	if(users.imageUrl != null) {
			if(users.imageName !=null){
				lDataVM.imageUrl = "http://glider-autos.com/glivrImg/images"+users.imageUrl;
			}else{
				lDataVM.imageUrl = users.imageUrl;
			}
			
		} 
     	}
     	else {
			lDataVM.imageUrl = "/profile-pic.jpg";
		}
     	
     	if(countFollo != 0 && folloLead != 0){
     		Long followUpTime = countFollo / folloLead;
	     	
	     	Long seconds = (followUpTime / 1000);
	     	Long minutes = (followUpTime / (1000 * 60));
	     	Long hours = (minutes / 60);
	     	Long displayMin = minutes - (hours * 60);
	     	
	     	String hrs=hours.toString();
	     	String mnt=displayMin.toString();
	     	if(hours.toString().length()<=1){
	     		hrs="0"+hours.toString();
	     	}
	     
	     	if(displayMin.toString().length()<=1){
	     		mnt="0"+displayMin.toString();
	     	}
	     	
	     	lDataVM.followUpTime =hrs+":"+mnt+" Hrs";
     		 /*Long followUpTime= (long)((double)countFollo / (double) countLeads) * 24;
     		lDataVM.followUpTime=followUpTime.toString()+":00 Hrs";*/
     	}else{
     		lDataVM.followUpTime = "00:00  Hrs";
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
     	
     	
     	reCancelInfo=RequestMoreInfo.findAllCancelLeadByUser(users);
  	   scCancelInfo=ScheduleTest.findAllCancelLeadsForUser(users);
  		trCancelInfo=TradeIn.findAllCancelLeadsForUser(users);
  		
  		
  		for(RequestMoreInfo rMoreInfo:reCancelInfo){
      		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
      			cancelRequestLeadCount++;
      		}
      	}
      	
      	for(ScheduleTest sTest:scCancelInfo){
      		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
      			cancelScheduleLeadCount++;
      		}
      	}

      	for(TradeIn tIn:trCancelInfo){
      		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
      			cancelTradeInLeadCount++;
      		}
      	}
  		
     	lDataVM.cancelLeadCount=cancelRequestLeadCount + cancelScheduleLeadCount + cancelTradeInLeadCount;
     	
     	
     	
     	
     	
     	
     	
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
      	
      	java.util.Date date= new Date();
      	Calendar cal11 = Calendar.getInstance();
      	cal.setTime(date);
      	int monthValue = cal.get(Calendar.MONTH);
      	 String[] changVls = { "January", "February", "March", "April", "May", "June", "July",
  		        "August", "September", "October", "November", "December" };
     	for(int i=0;i<monthValue+1;i++){
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
        		
        		
        		if(changVls1.equals(nSetVM.name)){
        		//	if(i <= month){
        				nSetVM.name = changVls1;
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
        
        
        
        mapOffline.put("Phone", 0);
        mapOffline.put("Walk In", 0);
        mapOffline.put("Email", 0);
        mapOffline.put("Facebook", 0);
        mapOffline.put("Online", 0);
    	for(RequestMoreInfo rMoreInfo:offlineRInfo){
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
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
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
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
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
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
     		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
     			Integer objectMake = mapOnline.get("Request More Info");
    			if (objectMake != null) {
    				
    				mapOnline.put("Request More Info", countBodyStyle + 1);
    			}
     		}
     	}
     	
    	mapOnline.put("Schedule Test Drive", 0);
     	for(ScheduleTest sTest:onlineSList){
     		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
     			Integer objectMake = mapOnline.get("Schedule Test Drive");
    			if (objectMake != null) {
    				mapOnline.put("Schedule Test Drive", countBodyStyle + 1);
    			}
     		}
     	}

     	mapOnline.put("Trade-In Inquires", 0);
     	for(TradeIn tIn:onlineTradeIns){
     		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
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
 		if((rMoreInfo.requestDate.after(startD) && rMoreInfo.requestDate.before(endD)) || rMoreInfo.requestDate.equals(endD) || rMoreInfo.requestDate.equals(startD)){
 			
 			 			
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
 		if((sTest.scheduleDate.after(startD) && sTest.scheduleDate.before(endD)) || sTest.scheduleDate.equals(endD) || sTest.scheduleDate.equals(startD)){
 			
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
 		if((tIn.tradeDate.after(startD) && tIn.tradeDate.before(endD)) || tIn.tradeDate.equals(endD) || tIn.tradeDate.equals(startD)){
 			
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
	     	Long min1 = minutes % 60;
	     	//Long days= (hours/24);
	     	Long hrs=(hours%24);
/*	     	float min=hours%24;
	     	int mnts=(int) (min-hrs);*/
	        //String mnts =min1.toString();
	        String mnts =displayMin.toString();
	        
	        //String days1 =days.toString();
	        //String hrs1=hrs.toString();
	        String hrs1=hours.toString();
	     	if(min1.toString().length()<=1)
	     	{
	     		mnts="0"+mnts;
	     	}
	     	
	     	/*if(days.toString().length()<=1)
	     	{
	     		days1="0"+ days1;
	     	}*/
	     	
	     	if(hrs.toString().length()<=1)
	     	{
	     		hrs1="0"+hrs1;
	     	}
	     	
	     	//lDataVM.avgLeadLifeCycle =hours.toString()+":"+displayMin.toString()+"Hrs";
	     	lDataVM.avgLeadLifeCycle=hrs1+":"+mnts+"  Hrs";
     	}else{
     		lDataVM.avgLeadLifeCycle = "00:00:00  Hrs";
     	}
     	
     	List<UserNotes> uNotes = UserNotes.findByUserAndcall(users);
     	int callActionCount =  0;
     	for(UserNotes sNot:uNotes){
     		if((sNot.createdDate.after(startD) && sNot.createdDate.before(endD)) || sNot.createdDate.equals(endD) || sNot.createdDate.equals(startD)){
     			callActionCount++;
     		}
     	}
     	lDataVM.callMade = callActionCount;
     	
     	List<UserNotes> uNotesEmail = UserNotes.findByUserAndemail(users);
     	int mailActionCount =  0;
     	for(UserNotes sNot:uNotesEmail){
     		if((sNot.createdDate.after(startD) && sNot.createdDate.before(endD)) || sNot.createdDate.equals(endD) || sNot.createdDate.equals(startD)){
     			mailActionCount++;
     		}
     	}
     	lDataVM.mailSent = callActionCount;
     	
     	List<UserNotes> uNotesTest = UserNotes.findByUserAndSched(users);
     	int testDriveActionCount =  0;
     	for(UserNotes sNot:uNotesTest){
     		if((sNot.createdDate.after(startD) && sNot.createdDate.before(endD)) || sNot.createdDate.equals(endD) || sNot.createdDate.equals(startD)){
     			testDriveActionCount++;
     		}
     	}
     	lDataVM.testDriveSched = callActionCount;
     	
     	
     	List<Comments> comments = Comments.getByListUser(users);
     	int likeCount = 0;
     	
     	for(Comments comm:comments){
     		if((comm.likeDate.after(startD) && comm.likeDate.before(endD)) || comm.likeDate.equals(endD) || comm.likeDate.equals(startD)){
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
 	if(!startD.equals(endD)){
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
 	}
      
 	lDataVM.salary = (parDataSalary * dayCount);
 	lDataVM.training = users.trainingPro;
 	int tcl = 0;
 	if(users.trainingCost != null){
 		if(!users.trainingCost.equals("null")){
 			lDataVM.trainingCost = Integer.parseInt(users.trainingCost);
 			if(lDataVM.allGeneratedLeadCount!=0){
 	 			tcl = Integer.parseInt(users.trainingCost) / lDataVM.allGeneratedLeadCount;
 			}
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
private static void saveCustomData(Long infoId,LeadVM leadVM,MultipartFormData bodys,Long leadtype) {
    	
    	for(KeyValueDataVM custom:leadVM.customData){
    		
    		CustomizationDataValue cDataValue = CustomizationDataValue.findByKeyAndLeadId(custom.key,infoId);
    		if(cDataValue == null){
    			CustomizationDataValue cValue = new CustomizationDataValue();
    			cValue.keyValue = custom.key;
    			cValue.value = custom.value;
    			cValue.leadId = infoId;
    			cValue.leadType = leadtype;
    			
    			if(custom.savecrm == null){
    				cValue.saveCrm = "false";
    			}else{
    				cValue.saveCrm = custom.savecrm;
    			}
    			
    			if(custom.displayGrid == null){
    				cValue.displayGrid = "false";
    			}else{
    				cValue.displayGrid = custom.displayGrid;
    			}
    			
    			cValue.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    			cValue.save();
    			
    		}else{
    			cDataValue.setKeyValue(custom.key);
    			cDataValue.setValue(custom.value);
    			cDataValue.setSaveCrm(custom.savecrm);
    			if(custom.displayGrid == null){
    				cDataValue.setDisplayGrid("false");
    			}else{
    				cDataValue.setDisplayGrid(custom.displayGrid);
    			}
    			
    			cDataValue.update();
    		}
			
		}
    	
    	
    	if(bodys != null){
    		FilePart picture = bodys.getFile("file0");
    		if (picture != null) {
    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
    			File file = picture.getFile();
    			try {
    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"leads"+File.separator+leadtype+File.separator+infoId+File.separator+fileName);
    	    	    if(!fdir.exists()) {
    	    	    	fdir.mkdir();
    	    	    }
    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"leads"+File.separator+leadtype+File.separator+infoId+File.separator+fileName;
    	    	    FileUtils.moveFile(file, new File(filePath));
    	    	    
    				
    				CustomizationDataValue cDataValue = CustomizationDataValue.findByKeyAndLeadId("fileupload",infoId);
    	    		if(cDataValue == null){
    	    			 CustomizationDataValue cValue = new CustomizationDataValue();
    	    				cValue.keyValue = "fileupload";
    	    				cValue.value = session("USER_LOCATION")+File.separator+"leads"+File.separator+leadtype+File.separator+infoId+File.separator+fileName;
    	    				cValue.leadId = infoId;
    	    				cValue.leadType = leadtype;
    	    				cValue.displayGrid = "false";
    	    				cValue.saveCrm = "false";
    	    				cValue.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
    	    				cValue.save();
    	    			
    	    		}else{
    	    			//cDataValue.setKeyValue(custom.key);
    	    			cDataValue.setValue(session("USER_LOCATION")+File.separator+"leads"+File.separator+infoId+File.separator+fileName);
    	    			cDataValue.update();
    	    		}
    				
    			} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    	}
    	
		
    }
 
public static Result createLead() {
	AuthUser user = (AuthUser)getLocalUser();
	
	SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
	MultipartFormData bodys = request().body().asMultipartFormData();
	
	LeadVM leadVM = null;
	
	Form<LeadVM> form = DynamicForm.form(LeadVM.class).bindFromRequest();
	if(bodys != null){
		LeadVM leadVM1 = new LeadVM();
		saveBilndVm(leadVM1,bodys,form);
		leadVM = leadVM1;
	}else{
		leadVM = form.get();
	}
	
	String makestr = leadVM.make!=null&&!leadVM.make.isEmpty()?leadVM.make:leadVM.makeSelect;
	String model = leadVM.model!=null&&!leadVM.model.isEmpty()?leadVM.model:leadVM.modelSelect;
	Date date = new Date();
	int parentFlag = 0;
	long parentLeadId = 0L;
	
	List<Vehicle> vehicles = Vehicle.findByMakeAndModel(makestr, model);
	if(!leadVM.leadType.equals("2") && !leadVM.leadType.equals("3")) {
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
    		Vehicle product = Vehicle.findByStockAndNew(vehicleVM.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
    		if(product != null){
    			info.setVin(product.getVin());
    		}
    		
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
    		info.setIsContactusType(leadVM.leadType);
    		if(parentFlag == 1){
    			info.setParentId(parentLeadId);
    		}
    		
    		info.save();
    		
    		saveCustomData(info.id,leadVM,bodys,Long.parseLong(leadVM.leadType));
    	
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
    		Vehicle product = Vehicle.findByStockAndNew(vehicleVM.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
    		if(product != null){
    			test.setVin(product.getVin());
    		}
    		
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
    		
    		saveCustomData(test.id,leadVM,bodys,Long.parseLong(leadVM.leadType));
    		
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
    		//map.put("vin", product.getId());
    		map.put("uname", user.firstName+" "+user.lastName);
    		map.put("uphone", user.phone);
    		map.put("uemail", user.email);
    		
    		//makeToDo(product.getId());
    		

    		if(parentFlag == 0){
    			sendMail(map);
    			parentFlag = 1;
    			parentLeadId = test.getId();
    		}
		   
		   
		   /*if(test.premiumFlag == 1){
					sendMailpremium();
				}*/
	}
	} else if(leadVM.leadType.equals("3")){
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
    		Vehicle product = Vehicle.findByStockAndNew(vehicleVM.stockNumber, Location.findById(Long.valueOf(session("USER_LOCATION"))));
    		if(product != null){
    			tradeIn.setVin(product.getVin());
    		}
    		//tradeIn.setProductId(product.getId());
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
    		
    		
    		saveCustomData(tradeIn.id,leadVM,bodys,Long.parseLong(leadVM.leadType));
    		
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
		
		
    		//VehicleImage pImages = VehicleImage.getDefaultImage(product.vin);
    		
		VehicleImage pImages = VehicleImage.getDefaultImage(product.getVin());
		
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
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailUser,
								emailPass);
					}
				});
		try {
			List<AuthUser> users = AuthUser.getAllUsers();

			InternetAddress[] usersArray = new InternetAddress[users.size() + 1];
			int index = 0;
			usersArray[index] = new InternetAddress(user.getEmail());
			
			Message message = new MimeMessage(session);
    		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    			
    		}
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
			context.put("price", "$" + product.getPrice());
			context.put("vin", product.getVin());
			context.put("stock", product.getStock());
			context.put("mileage", product.getMileage());
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
		  			try {
						message.setFrom(new InternetAddress(emailUser,emailName));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
	
	public static Result getScheduleTestData(){
        AuthUser user = getLocalUser();
        
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        Date currD = new Date();
        System.out.println("current time");
        System.out.println(currD);
        String cDate = df.format(currD);
        Date datec = null;
        try {
			datec = df.parse(cDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        List<ScheduleTest> list = ScheduleTest.findAllByUserService(user, datec);
        List<ScheduleTest> list1 = ScheduleTest.findForUser(user,datec);
        
        for(ScheduleTest scList:list1){
        	list.add(scList);
        }
    	List<RequestMoreInfo> requestMoreInfos = RequestMoreInfo.findByConfirmGraLeads(Long.valueOf(session("USER_LOCATION")), user, datec);
    	List<TradeIn> tradeIns = TradeIn.findByConfirmGraLeads(Long.valueOf(session("USER_LOCATION")), user, datec);
    	
        Map<Long,Integer> maps = new HashMap<Long, Integer>();
        List<RequestInfoVM> shList = new ArrayList<RequestInfoVM>();
        

    	 Date currD1 = new Date();
    	 Date currentDate = null;
    	 Date infoDate = null;
    	 DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
    	 DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
    	 SimpleDateFormat parseTime = new SimpleDateFormat("HH:mm a");
   		 Location location = Location.findById(16l);
   		 if(user.location != null){
   			 location = Location.findById(user.location.id);
   		 }
   		 df1.setTimeZone(TimeZone.getTimeZone(location.time_zone));
         String IST = df1.format(currD1);
         Date istTimes = null;
         try {
				istTimes = df2.parse(IST);
				String crD =    df2.format(istTimes);
	 			currentDate = df2.parse(crD);
	 			System.out.println("current Date :  "+currentDate);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
         
        for(ScheduleTest scTest:list){
 			try {
 	 			 String str = df.format(scTest.confirmDate) +" "+parseTime.format(scTest.confirmTime);
 		   		 infoDate = df2.parse(str);
 		   		 System.out.println(scTest.id);
 		   		 System.out.println("InfoDate : "+infoDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
        	RequestInfoVM sTestVM = new RequestInfoVM();
        	sTestVM.id = scTest.id;
        	sTestVM.is_google_data = scTest.is_google_data;
        	sTestVM.google_id = scTest.google_id;
        	sTestVM.groupId = scTest.groupId;
        	sTestVM.meetingStatus = scTest.meetingStatus;
        	sTestVM.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(scTest.confirmDate);
        	sTestVM.confirmTime = new SimpleDateFormat("hh:mm a").format(scTest.confirmTime);
        	if(sTestVM.meetingStatus != null){
        		if(sTestVM.meetingStatus.equalsIgnoreCase("meeting")){
            		if(scTest.confirmEndTime != null){
                		sTestVM.confirmEndTime =new SimpleDateFormat("hh:mm a").format(scTest.confirmEndTime);
                	}else{
                		sTestVM.confirmEndTime =new SimpleDateFormat("hh:mm a").format(scTest.confirmTime);
                	}
            	}
        	}
        	sTestVM.confirmDateOrderBy = scTest.confirmDate;
        	sTestVM.reason=scTest.reason;
        	sTestVM.name = scTest.name;
        	sTestVM.typeOfLead = "Schedule Test Drive";
        	int flag = 0;
        	List<UserVM> listUser = new ArrayList<>();
        	if(scTest.groupId != null){
    			List<ScheduleTest> schedulegroupList = ScheduleTest.findAllGroupMeetingCheckMeeting(scTest.groupId);
    			for(ScheduleTest users:schedulegroupList){
    				UserVM uVm = new UserVM();
    	    		uVm.firstName = users.assignedTo.getFirstName();
    	    		uVm.lastName = users.assignedTo.getLastName();
    	    		/*if(users.acceptMeeting == 1 && users.declineMeeting == 1){
    	    			uVm.meetingFlag = 2;
    	    		}else if(users.acceptMeeting == 1 && users.declineMeeting == 0){
    	    			uVm.meetingFlag = 1;
    	    		}else{
    	    			uVm.meetingFlag = 0;
    	    		}*/
    	    		uVm.meetingFlag = users.meeting;
    	    		uVm.id = users.assignedTo.id;
    	    		if(users.declineMeeting == 1){
    	    			flag = 1;
    	    		}
    	    		
    	    		listUser.add(uVm);
    			}
    		}
    	
    	
    		sTestVM.noteFlag = flag;
        	sTestVM.userdata = listUser;
        	
        	
		if(scTest.user != null){
			if(user.id.equals(scTest.user.id)){
        		sTestVM.setFlagSameUser = user.id;
        	}
        }	
        	if(sTestVM.meetingStatus == null){
        		Vehicle vehicle = Vehicle.findByVinAndStatus(scTest.vin);
        		sTestVM.vin = scTest.vin;
        		if(vehicle != null) {
        			sTestVM.model = vehicle.model;
        			sTestVM.make = vehicle.make;
        			sTestVM.typeofVehicle=vehicle.typeofVehicle;
        			sTestVM.stock = vehicle.stock;
        			sTestVM.mileage = vehicle.mileage;
        			sTestVM.year = vehicle.year;
        			sTestVM.bodyStyle =vehicle.bodyStyle;
        			sTestVM.drivetrain = vehicle.drivetrain;
        			sTestVM.engine = vehicle.engine;
        			sTestVM.transmission = vehicle.transmission;
        			sTestVM.price = vehicle.price;
	    			VehicleImage vehicleImage = VehicleImage.getDefaultImage(vehicle.vin);
	        		if(vehicleImage!=null) {
	        			sTestVM.imgId = vehicleImage.getId().toString();
	        		}
	        		else {
	        			sTestVM.imgId = "/assets/images/no-image.jpg";
	        		}
	    		}
        		
        		sTestVM.name = scTest.name;
        		sTestVM.phone = scTest.phone;
        		sTestVM.email = scTest.email;
        		sTestVM.custZipCode = scTest.custZipCode;
        		sTestVM.enthicity = scTest.enthicity;
        		
        		
	    		if(scTest.isRead == 0) {
	    			sTestVM.isRead = false;
	    		}
	    		
	    		if(scTest.isRead == 1) {
	    			sTestVM.isRead = true;
	    		}
        	}else{
        		sTestVM.typeOfLead = "Meeting";
        	}
        	
        	if(infoDate.after(currentDate)){
        		if(sTestVM.groupId != null){
            		if(maps.get(sTestVM.groupId) == null){
    					maps.put(sTestVM.groupId, 1);
    					shList.add(sTestVM);
    				}
            	}else{
            		shList.add(sTestVM);
            	}
        	}
        }
        List<ScheduleTest> listData = null;
        fillLeadsData(listData, requestMoreInfos, tradeIns, shList);
        
        //public static void fillLeadsData(List<ScheduleTest> listData, List<RequestMoreInfo> requestMoreInfos, List<TradeIn> tradeIns, List<RequestInfoVM> infoVMList){
        
        return ok(Json.toJson(shList));
    }
	
	

	public static Result updateCalender(){
		//DynamicForm form = DynamicForm.form().bindFromRequest();
		Form<ScheduleTestVM> form = DynamicForm.form(ScheduleTestVM.class).bindFromRequest();
		ScheduleTestVM vm = form.get();
		List<AuthUser> list=new ArrayList<>();
		List<AuthUser> newlist=new ArrayList<>();
		AuthUser user = getLocalUser();
		Long id =vm.id;
		String confDate = vm.confDate;
		String confTime = vm.confTime;
		String confEndTime = vm.confirmEndTime;
		String googleID = vm.google_id;
		boolean infoChange = false;
	    /* String id = form.get("id");
		String confDate = form.get("confDate");
		String confTime = form.get("confTime");
		String googleID = form.get("googleID");
		
		SessionUseVM vm = new SessionUseVM();
		vm.id = id;
		vm.cnfDate = confDate;
		vm.cnfTime = confTime;
		vm.eventID = googleID;
		session("sessionVmData", Json.stringify(Json.toJson(vm)));
		
		*/
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

		SimpleDateFormat time= new SimpleDateFormat("hh:mm a");
		List<UserVM> newUser = new ArrayList<>();
		
		if(vm.groupId != null){
			try {
				for (UserVM obj : vm.getUsersList()) {
					boolean flg = true;
					List<ScheduleTest> test = ScheduleTest.findAllGroupMeeting(vm.groupId);
					for (ScheduleTest st : test) {
						AuthUser assiTest = AuthUser.findById(st.assignedTo.id);
						if(assiTest.id == obj.id){
							flg = false;
							break;
						}
					}
					if(flg){
						newUser.add(obj);
					}
				}
				
				List<ScheduleTest> test = ScheduleTest.findAllGroupMeeting(vm.groupId);
				for(ScheduleTest testloop:test){
					if(! testloop.getName().equals(vm.name) || ! testloop.getReason().equals(vm.reason) || ! testloop.getConfirmDate().equals(df.parse(confDate)) || ! testloop.getConfirmTime().equals(time.parse(confTime))|| ! testloop.getConfirmEndTime().equals(time.parse(confEndTime))){
						infoChange = true;
					}
					AuthUser assi = AuthUser.findById(testloop.assignedTo.id);
					testloop.setName(vm.name);
					testloop.setReason(vm.reason);
					Date meetingActionTime=new Date();
					testloop.setMeetingActionTime(meetingActionTime);
					if(!testloop.getConfirmDate().equals(df.parse(confDate)) || ! testloop.getConfirmTime().equals(time.parse(confTime))|| ! testloop.getConfirmEndTime().equals(time.parse(confEndTime))){
						list.add(assi);
						testloop.setDeclineUpdate(1);
            	    // testloop.setSendInvitation(1);
						String subject = "Meeting's information has been changed.";
 			   	    	String comments = "Meeting invitation received \n "+user.firstName+" "+user.lastName+"\n"+vm.getConfDate()+" "+vm.getConfirmTime()+".";
 			   	    	//sendEmail(assi.communicationemail, subject, comments);
 			   	    testloop.setConfirmDate(df.parse(confDate));
					testloop.setConfirmTime(new SimpleDateFormat("hh:mm a").parse(confTime));
					if(confEndTime!=null)
					testloop.setConfirmEndTime(new SimpleDateFormat("hh:mm a").parse(confEndTime));
			       }
					testloop.update();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{

			try {
				
				ScheduleTest test = ScheduleTest.findById(id);
				AuthUser assi = AuthUser.findById(test.assignedTo.id);
				test.setConfirmDate(df.parse(confDate));
				test.setConfirmTime(new SimpleDateFormat("hh:mm a").parse(confTime));
				test.setName(vm.name);
				test.setReason(vm.reason);
				if(! test.getName().equals(vm.name) || ! test.getReason().equals(vm.reason) || ! test.getConfirmDate().equals(df.parse(confDate)) || ! test.getConfirmTime().equals(time.parse(confTime))){
					infoChange = true;
				}
				if(!test.getConfirmDate().equals(df.parse(confDate)) || ! test.getConfirmTime().equals(time.parse(confTime))){
					list.add(assi);
					test.setSendInvitation(1);
					String subject = "Meeting invitation.";
			   	    String comments = "New meeting invitation received \n "+user.firstName+" "+user.lastName+"\n"+vm.getConfDate()+" "+vm.getConfirmTime()+".";
					//sendEmail(assi.communicationemail, subject, comments);
			       }
				test.update();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		List<AuthUser> userList = new ArrayList<>();
		for (UserVM obj : newUser) {
			AuthUser assi = AuthUser.findById(obj.id);
			ScheduleTest moTest = new ScheduleTest();
			moTest.assignedTo = assi;
			newlist.add(assi);
			moTest.name=vm.getAssignedTo();
			moTest.bestDay = vm.getBestDay();
			moTest.bestTime = vm.getBestTime();
			moTest.email = user.getEmail();
			moTest.location = vm.getLocation();
			moTest.name = vm.name;
			moTest.groupId = vm.groupId;
			moTest.meetingStatus = "meeting";
		 	moTest.phone = user.getPhone();
			moTest.reason = vm.getReason();
			moTest.scheduleDate = new Date();
			moTest.scheduleTime = new Date();
		    moTest.user = user;
			moTest.isReassigned = false;
			moTest.sendInvitation = 1;
			moTest.acceptMeeting = 1;
			moTest.declineMeeting = 1;
			moTest.meeting = 1;
			moTest.is_google_data = false;
			userList.add(assi);
			try {			
				moTest.confirmDate = df.parse(vm.confDate);
				moTest.confirmTime = new SimpleDateFormat("hh:mm a").parse(vm.confTime);
				if(vm.confirmEndTime != null)
				moTest.confirmEndTime = new SimpleDateFormat("hh:mm a").parse(vm.confirmEndTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			moTest.save();
			
			String subject = "Meeting invitation.";
	   	    String comments = "New meeting invitation received \n "+user.firstName+" "+user.lastName+"\n"+vm.getConfDate()+" "+vm.getConfirmTime()+".";
	   	 //sendMeetingMailInfoChange(vm, user, userList);
		}
		if(newUser.size() > 0){
			String[] dt = vm.confirmDate.split("-");
			vm.bestDay = dt[1]+"-"+dt[0]+"-"+dt[2];
			vm.bestTime = vm.confirmTime;
			vm.bestEndTime = vm.confirmEndTime;
			sendMeetingMailToAssignee(vm, user, userList);
		}
		if(infoChange){
			sendMeetingMailInfoChange(vm, user, list);
		}
		
		
		/*try {
			if(test.getGoogle_id()!=null){
				return redirect(authorizeUpdate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		/**/
		//return redirect("/authenticate");
		return ok();
		}
	

	public static void sendMeetingMailInfoChange(ScheduleTestVM vm, AuthUser user, List<AuthUser> userList){
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
		List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : userList){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
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
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,usersArray);
			/*usersArray*/
			message.setSubject("Meeting Info changed");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/InternalMeetingInfoChanged_HTML.html"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("title", vm.name);
	       // context.put("location", loc.getName());
	        context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

			SimpleDateFormat time= new SimpleDateFormat("hh:mm a");
	        
	        if(!vm.confirmDate.equals(vm.confDate)){
      	         
				vm.confirmDate=vm.confDate;
				
       	   
		       }
	        
	        if( ! vm.confirmTime.equals(vm.confTime)){
     	         
				vm.confirmTime=vm.confTime;
				
       	   
		       }
	        
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
	        	String dateInString = vm.confirmDate;
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[1]);
			        month = Integer.parseInt(arr[0]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        context.put("userList",list);
	        
	        
	       /* SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
	        String confirmTime = localDateFormat.format(vm.confirmTime);
	        */
	        context.put("time",vm.confirmTime);
	        context.put("endTime",vm.confirmEndTime);
	        context.put("date", vm.getBestDay());
	       // context.put("time", vm.getBestTime());
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
			Date date=new Date();
			DateFormat df11 = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:a");
		    Location location = Location.findById(Long.valueOf(session("USER_LOCATION")));
				df11.setTimeZone(TimeZone.getTimeZone(location.time_zone));
				String d1=df11.format(date);
				Date newDate=null;
				try {
					newDate=df.parse(d1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
				planMoth.setFlagMsg(1);
				planMoth.setSaveDate(newDate);
				planMoth.setTotalBrought(vm.totalBrought);
				planMoth.setVehicalesToSell(vm.vehicalesToSell);
				planMoth.setUser(uAuthUser);
				planMoth.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
				planMoth.save();
			}else{
				pSalePer.setSaveDate(newDate);
				pSalePer.setCell(vm.cell);
				pSalePer.setEmails(vm.emails);
				pSalePer.setMonth(vm.month);
				pSalePer.setLeadsToGenerate(vm.leadsToGenerate);
				pSalePer.setNewCustomers(vm.newCustomers);
				pSalePer.setOutofSale(vm.outofSale);
				pSalePer.setReturningCustomers(vm.returningCustomers);
				pSalePer.setSuccessRate(vm.successRate);
				pSalePer.setFlagMsg(1);
				pSalePer.setTestDrives(vm.testDrives);
				pSalePer.setTotalBrought(vm.totalBrought);
				pSalePer.setVehicalesToSell(vm.vehicalesToSell);
				pSalePer.update();
			}
			
			Map map = new HashMap();
    		map.put("email",uAuthUser.communicationemail);
    		map.put("amount", vm.totalBrought);
    		map.put("vehicleTosell", vm.vehicalesToSell);
    		map.put("leadGenerated",vm.leadsToGenerate);
    		map.put("testDrives",vm.testDrives);
    		if(vm.call != null){
    		map.put("callsMake", vm.call);
    		}
    		else{
    			map.put("callsMake", "");
    		}
    		if(vm.successRate !=null){
    			map.put("successRate", vm.successRate+"%");
    		}else{
    			map.put("successRate", "");
    		}
    		if(vm.emails !=null){
    		map.put("emailSend", vm.emails);
    		}else{
    			map.put("emailSend", "");
    		}
    		
    		if(vm.newCustomers !=null){
    		map.put("customers", vm.newCustomers);
    		}else{
    			map.put("customers", "");
    		}
    		map.put("uname",user.firstName+" "+user.lastName);
    		map.put("uphone", user.phone);
    		map.put("uemail", user.email);
    		map.put("month", vm.month.toString().toUpperCase());
    		salesPersonPlanMail(map);
			 	/*String subject = "Plan has been Assigned";
		    	 String comments = "plan for "+vm.month+" has been assigned";
		    	 sendEmail(uAuthUser.communicationemail, subject, comments);*/
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
	
private static void salesPersonPlanMail(Map map) {
    	
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
    		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(map.get("email").toString()));
			message.setSubject("This month's plan has been added");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/planAssigned_HTML.vm"); 
	        VelocityContext context = new VelocityContext();
	        /*String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	       
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
			}*/
	        
	        //String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	        context.put("siteLogo", logo.logoImagePath);
	      //  context.put("dayOfmonth", dayOfmonth);
	      //  context.put("monthName", monthName);
	        context.put("amount", map.get("amount"));
	        context.put("month", map.get("month"));
	        System.out.println(">>>>>>>>>>>>>>>>>"+map.get("month"));
	        context.put("vehicleTosell", map.get("vehicleTosell"));
	        context.put("leadGenerated", map.get("leadGenerated"));
	        context.put("callsMake", map.get("callsMake"));
	        context.put("testDrives", map.get("testDrives"));
	        context.put("successRate", map.get("successRate"));
	        context.put("emailSend", map.get("emailSend"));
	        context.put("customers", map.get("customers"));
	        context.put("name", map.get("uname"));
	        context.put("email", map.get("uemail"));
	        context.put("phone",  map.get("uphone"));
	        /*String weather= map.get("CnfDateNature").toString();
	        String arr1[] = weather.split("&");
	        String nature=arr1[0];
	        String temp=arr1[1];
	        context.put("nature",nature);
	        context.put("temp", temp);*/
	       
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
    
    
	
	
	
	public static Result getSalePerson(String month){
		List<PlanScheduleMonthlySalepeople> sMonthlySalepeoples = PlanScheduleMonthlySalepeople.findByListByMonth(month);
		return ok(Json.toJson(sMonthlySalepeoples));
	}
	
	public static Result getSaleMonthlyPlan(Integer saleId){
		List<PlanScheduleMonthlySalepeople> sMonthlySalepeoples = PlanScheduleMonthlySalepeople.findByListUser(AuthUser.findById(saleId));
		return ok(Json.toJson(sMonthlySalepeoples));
	}
	
	
	public static Result getPlanByMonthAndUserForLocation(Integer saleId,String month){
		int flagForPlan=0;
	   PlanScheduleMonthlyLocation sMonthlySalepeoples = PlanScheduleMonthlyLocation.findByUserMonth(AuthUser.findById(saleId),month);
		if(sMonthlySalepeoples == null){
			flagForPlan=1;
			
			 return ok(Json.toJson(flagForPlan));
		}
	   return ok(Json.toJson(sMonthlySalepeoples));
	}
	
	
	
	public static Result getPlanByMonthAndUser(Integer saleId,String month){
		int flagForPlan=0;
	   PlanScheduleMonthlySalepeople sMonthlySalepeoples = PlanScheduleMonthlySalepeople.findByUserMonth(AuthUser.findById(saleId),month);
		if(sMonthlySalepeoples == null){
			flagForPlan=1;
			
			 return ok(Json.toJson(flagForPlan));
		}
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
	
	public static Result getUserAppointment(String date, String time, String endTime){
		AuthUser authUser = getLocalUser();
		List<ScheduleTestVM> list = new ArrayList<>();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date cuDate = new Date();
			String cdat = df.format(cuDate);
			Date cd = df.parse(cdat);
			
			Date confirmDate = df.parse(date);
			Date confirmTime = new SimpleDateFormat("hh:mm a").parse(time);
			Date confirmEndTime = new SimpleDateFormat("hh:mm a").parse(endTime);
			AuthUser user = getLocalUser();
			
			List<ScheduleTest> testList = ScheduleTest.findAllByUserServiceTest(authUser, cd);
			List<RequestMoreInfo> requestInfo = RequestMoreInfo.findAllByUserServiceTest(authUser, confirmDate);
			List<TradeIn> tradeIn = TradeIn.findAllByUserServiceTest(authUser, confirmDate);
			for (TradeIn scheduleTest : tradeIn) {
				if(confirmDate.equals(scheduleTest.confirmDate)){
					System.out.println(scheduleTest.id);
					if((scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.after(confirmTime)) && (scheduleTest.confirmTime.equals(confirmEndTime) || scheduleTest.confirmTime.before(confirmEndTime))){
						ScheduleTestVM vm = new ScheduleTestVM();
						vm.meetingStatus = null;
						vm.name = scheduleTest.firstName;
						vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
						vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
						AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
						vm.fullName = ass.firstName +" "+ass.lastName;
						list.add(vm);
					}
				}
			}
			for (RequestMoreInfo scheduleTest : requestInfo) {
				if(confirmDate.equals(scheduleTest.confirmDate)){
					System.out.println(scheduleTest.id);
					if((scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.after(confirmTime)) && (scheduleTest.confirmTime.equals(confirmEndTime) || scheduleTest.confirmTime.before(confirmEndTime))){
						ScheduleTestVM vm = new ScheduleTestVM();
						vm.meetingStatus = null;
						vm.name = scheduleTest.name;
						vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
						vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
						AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
						vm.fullName = ass.firstName +" "+ass.lastName;
						list.add(vm);
					}
				}
			}
			for (ScheduleTest scheduleTest : testList) {
				if(scheduleTest.meetingStatus != null){
					if(confirmDate.equals(scheduleTest.confirmDate)){
						if(scheduleTest.confirmTime != null && scheduleTest.confirmEndTime != null){
							if((confirmTime.equals(scheduleTest.confirmTime)||confirmTime.equals(scheduleTest.confirmEndTime)) || (confirmTime.after(scheduleTest.confirmTime) && confirmTime.before(scheduleTest.confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime)) || (scheduleTest.confirmTime.after(confirmTime) && scheduleTest.confirmTime.before(confirmEndTime))){
								ScheduleTestVM vm = new ScheduleTestVM();
								vm.meetingStatus = scheduleTest.meetingStatus;
								vm.name = scheduleTest.name;
								vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
								vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
								vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmEndTime);
								AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
								vm.fullName = ass.firstName +" "+ass.lastName;
								list.add(vm);
							}
						}else if(scheduleTest.confirmTime != null && scheduleTest.confirmEndTime == null){
							if(scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.equals(confirmEndTime) || (scheduleTest.confirmTime.after(confirmTime) && scheduleTest.confirmTime.before(confirmEndTime))){
								ScheduleTestVM vm = new ScheduleTestVM();
								vm.meetingStatus = scheduleTest.meetingStatus;
								vm.name = scheduleTest.name;
								vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
								vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
								AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
								vm.fullName = ass.firstName +" "+ass.lastName;
								list.add(vm);
							}
						}
					}
						/*if((confirmTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.before(confirmEndTime))){
							if(scheduleTest.confirmEndTime != null){
								if((confirmTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.before(confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime))){
									ScheduleTestVM vm = new ScheduleTestVM();
									vm.meetingStatus = scheduleTest.meetingStatus;
									vm.name = scheduleTest.name;
									vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
									vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
									vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmEndTime);
									AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
									vm.fullName = ass.firstName +" "+ass.lastName;
									list.add(vm);
								}
							}else{
								ScheduleTestVM vm = new ScheduleTestVM();
								vm.meetingStatus = scheduleTest.meetingStatus;
								vm.name = scheduleTest.name;
								vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
								vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
								AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
								vm.fullName = ass.firstName +" "+ass.lastName;
								list.add(vm);
							}
						}*/
					//}
				}
				else{
					if(confirmDate.equals(scheduleTest.confirmDate)){
						System.out.println(scheduleTest.id);
						if((scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.after(confirmTime)) && (scheduleTest.confirmTime.equals(confirmEndTime) || scheduleTest.confirmTime.before(confirmEndTime))){
							ScheduleTestVM vm = new ScheduleTestVM();
							vm.meetingStatus = scheduleTest.meetingStatus;
							vm.name = scheduleTest.name;
							vm.confDate = new SimpleDateFormat("dd-MM-yyyy").format(scheduleTest.confirmDate);
							vm.confirmTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
							AuthUser ass = AuthUser.findById(scheduleTest.assignedTo.getId());
							vm.fullName = ass.firstName +" "+ass.lastName;
							list.add(vm);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return ok(Json.toJson(list));
	}
	public static Result getUserForMeeting(String date, String time, String endTime){
		
		List<UserVM> vmList  = new ArrayList<>();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		try {			
			Date cuDate = new Date();
			String cdat = df.format(cuDate);
			Date cd = df.parse(cdat);
			
			Date confirmDate = df.parse(date);
			Date confirmTime = new SimpleDateFormat("hh:mm a").parse(time);
			Date confirmEndTime = new SimpleDateFormat("hh:mm a").parse(endTime);
			AuthUser user = getLocalUser();
			String type = null;
			String schTime = null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(confirmTime);
			calendar.add(Calendar.HOUR, 1);
			Date maxTime = calendar.getTime();
			
			calendar.setTime(confirmTime);
			calendar.add(Calendar.HOUR, -1);
			Date minTime = calendar.getTime();
			Location loc = Location.findById(Long.parseLong(session("USER_LOCATION")));
			List<AuthUser> userList = null;
			if(user.role.equals("Manager")){
				userList = AuthUser.findByLocatioUsersNotManager(loc);
			}else if(user.role.equals("Sales Person")){
				userList = AuthUser.findByLocatioUsersNotGM(loc, user.id);
			}
			System.out.println(userList.size());
			for (AuthUser authUser : userList) {
				Boolean flag = true;
				List<ScheduleTest> testList = ScheduleTest.findAllByUserServiceTest(authUser, cd);
				/*for (ScheduleTest scheduleTest : testList) {
					if(confirmDate.equals(scheduleTest.confirmDate)){
						if((confirmTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.before(confirmEndTime)) || (confirmTime.after(scheduleTest.confirmTime) && confirmTime.before(scheduleTest.confirmEndTime))){
							if(scheduleTest.confirmEndTime != null){
								if((confirmTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.before(confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime))){
									flag = false;
									break;
								}
							}else{
								flag = false;
								break;
							}
						}
					}
				}*/
				for (ScheduleTest scheduleTest : testList) {
					if(scheduleTest.meetingStatus != null){
						if(confirmDate.equals(scheduleTest.confirmDate)){
							if(scheduleTest.confirmTime != null && scheduleTest.confirmEndTime != null){
									if((confirmTime.equals(scheduleTest.confirmTime)||confirmTime.equals(scheduleTest.confirmEndTime)) || (confirmTime.after(scheduleTest.confirmTime) && confirmTime.before(scheduleTest.confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime)) || (scheduleTest.confirmTime.after(confirmTime) && scheduleTest.confirmTime.before(confirmEndTime))){
										type = "Meeting";
										schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime)+"-"+new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmEndTime);
										flag = false;
										break;
									}
							}else if(scheduleTest.confirmTime != null && scheduleTest.confirmEndTime == null){
								if(scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.equals(confirmEndTime) || (scheduleTest.confirmTime.after(confirmTime) && scheduleTest.confirmTime.before(confirmEndTime))){
									type = "Meeting";
									schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
									flag = false;
									break;
								}
							}
							
							/*if(scheduleTest.confirmTime != null){
								if((confirmTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmTime)||scheduleTest.confirmTime.before(confirmEndTime))){
									if(scheduleTest.confirmEndTime != null){
										if((confirmTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.after(confirmTime)) && (confirmEndTime.equals(scheduleTest.confirmEndTime)||scheduleTest.confirmEndTime.before(confirmEndTime)) || (confirmEndTime.after(scheduleTest.confirmTime) && confirmEndTime.before(scheduleTest.confirmEndTime))){
											type = "Meeting";
											schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime)+"-"+new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmEndTime);
											flag = false;
											break;
										}
									}else{
										type = "Meeting";
										schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
										flag = false;
										break;
									}
								}
							}*/
						}
					}
					else{
						if(confirmDate.equals(scheduleTest.confirmDate)){
							if(scheduleTest.confirmTime != null){
								if((scheduleTest.confirmTime.equals(confirmTime) || scheduleTest.confirmTime.after(confirmTime)) && (scheduleTest.confirmTime.equals(confirmEndTime) || scheduleTest.confirmTime.before(confirmEndTime))){
									type = "Test Drive";
									schTime = new SimpleDateFormat("hh:mm a").format(scheduleTest.confirmTime);
									flag = false;
									break;
								}
							}
						}
					}
				}
				
				/*if((confirmTime.after(minTime) || confirmTime.equals(minTime)) && (confirmTime.before(maxTime) || confirmTime.equals(maxTime))){
					flag = false;
					break;
				}*/
				System.out.println(authUser.id);
				if(flag){
					UserVM vm = new UserVM();
					vm.id = authUser.getId();
					vm.fullName = authUser.getFirstName()+" "+authUser.getLastName();
					vm.role = authUser.getRole();
					vm.type = null;
					vm.time = null;
					vm.userStatus = "Yes";
					vm.isSelect = false;
					vmList.add(vm);
				}else{
					UserVM vm = new UserVM();
					vm.id = authUser.getId();
					vm.fullName = authUser.getFirstName()+" "+authUser.getLastName();
					vm.role = authUser.getRole();
					vm.type = type;
					vm.time = schTime;
					vm.userStatus = "N/A";
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
		
    	final String AB = "0123456789";
    	Random rnd = new Random();

    	   StringBuilder sb = new StringBuilder(2);
    	   for( int i = 0; i < 5; i++ ) 
    	      sb.append( AB.charAt(rnd.nextInt(AB.length()) ) );
    	
		
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
			moTest.groupId = Long.parseLong(sb.toString());
			moTest.meetingStatus = "meeting";
			moTest.phone = user.getPhone();
			moTest.reason = vm.getReason();
			moTest.scheduleDate = new Date();
			moTest.scheduleTime = new Date();
			moTest.user = user;
			moTest.isReassigned = false;
			moTest.sendInvitation = 1;
			moTest.acceptMeeting = 1;
			moTest.declineMeeting = 1;
			moTest.meeting = 1;
			moTest.is_google_data = false;
			userList.add(assi);
			try {			
				moTest.confirmDate = df.parse(vm.getBestDay());
				moTest.confirmTime = new SimpleDateFormat("hh:mm a").parse(vm.getBestTime());
				moTest.confirmEndTime = new SimpleDateFormat("hh:mm a").parse(vm.getBestEndTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			moTest.save();
			
			String subject = "Meeting invitation.";
			String comments = "New meeting invitation received \n "+user.firstName+" "+user.lastName+"\n"+vm.getBestDay()+" "+vm.getBestTime()+"-"+vm.getBestEndTime();
			
			//sendEmail(assi.communicationemail, subject, comments);
			//sendMeetingMailToAssignee(vm, user, userList);
		}
		  sendMeetingMailToAssignee(vm, user, userList);
		
		
		
		
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
		
		
		//sendMeetingMailToAssignee(vm, user, userList, loc);
		//sendMeetingMailToOrgnizer(vm, user, userList, loc);
		return ok();
	}
	
	public static void sendMeetingMailToAssignee(ScheduleTestVM vm, AuthUser user, List<AuthUser> userList){
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
		List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : userList){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
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
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,usersArray);
			/*usersArray*/
			message.setSubject("Meeting Invitation");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/internalMeetingInvitation_HTML.html"); 
	        VelocityContext context = new VelocityContext();
	        
	        context.put("title", vm.name);
	       // context.put("location", loc.getName());
	        context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = vm.getBestDay();
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        String dateInString = vm.getBestDay();
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        context.put("userList",list);
	        
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
		
		EmailDetails details=EmailDetails.findByLocation(Long.valueOf(session("USER_LOCATION")));
		String emailName=details.name;
		String port=details.port;
		String gmail=details.host;
		final	String emailUser=details.username;
		final	String emailPass=details.passward;
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
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
				pvalue.price = null;
			}
			//pvalue.x = 0l;
			pvalue.title = "";
			pvalue.text = "Plan "+ pvalue.price;
			sAndValues.add(pvalue);
			//sPrice.y = 100000;
			//sPrice.type = "flags";
			sPrice.data = sAndValues;
			sPrice.name = "";
			return ok(Json.toJson(sPrice));
		}else{
			AuthUser user = getLocalUser();
			PlanScheduleMonthlySalepeople obj = PlanScheduleMonthlySalepeople.findByUserMonth(user, monthCal);
			if(obj !=null){
				pvalue.price = Long.parseLong(obj.totalBrought);
			}else{
				pvalue.price = null;
			}
			
			pvalue.title = "";
			pvalue.text = "Plan "+ pvalue.price;
			sAndValues.add(pvalue);
			sPrice.data = sAndValues;
			sPrice.name = "";
			return ok(Json.toJson(sPrice));
		}	
	}
	
	public static Result getFinancialVehicleDetailsByBodyStyle(String startDate,String enddate){
		AuthUser user = getLocalUser();
		Map<String, Object> sAndValues = new HashMap<>();
		//List<sendDateAndValue> sAndValues = new ArrayList<>();
		FinancialVehicleDetails(user,sAndValues,startDate,enddate);
		return ok(Json.toJson(sAndValues));
	}
	
	public static void FinancialVehicleDetails(AuthUser user, Map<String, Object> sAndValues,String startD,String endD){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM");
		List<Vehicle> vehicle = null;
		Map<String, Long> mapBodyStyle = new HashMap<String, Long>();
		//Map<Long, Long> mapdate = new HashMap<Long, Long>();
		
		Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
		List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		sAndValues.put("dates",dates);
		sAndValues.put("data",data);
		
		Date startDate = null;
		Date endDate = null;
		try {
			startDate=df.parse(startD);
			endDate=df.parse(endD);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			List<Long> lonnn = new ArrayList<>();
			sendDataVM sValue = new sendDataVM();
			List<Vehicle> veList = null;
			
			if(user.role.equals("General Manager")){
				veList = Vehicle.findBySold();
			}else if(user.role.equals("Manager")){
				veList = Vehicle.findByBodyStyleAndSoldLocation(entry.getKey(), user.location.id);
			}else if(user.role.equals("Sales Person")){
				veList = Vehicle.findByBodyStyleAndSold(entry.getKey(), user);
			}
			
			/*Date startDate = null;
			Date endDate = null;
			
			try {
				startDate=df.parse(startD);
				endDate=df.parse(endD);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			
			
			
			sValue.name = entry.getKey();
			if(veList != null){
					
				for(Vehicle vhVehicle:veList){
					
					if((vhVehicle.getSoldDate().after(startDate) && vhVehicle.getSoldDate().before(endDate)) || vhVehicle.getSoldDate().equals(endDate) || vhVehicle.getSoldDate().equals(startDate))
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
				
				
				Date d1 = startDate;
				Date d2 = endDate;
				//Long i =0L;
				while((d1.before(d2)|| d1.equals(d2))){
					//dates.add(d1.getTime() + (1000 * 60 * 60 * 24));
					Long objectDate = mapdate.get(d1.getTime() + (1000 * 60 * 60 * 24));
					if(objectDate == null){
						mapdate.put(d1.getTime() + (1000 * 60 * 60 * 24), 0l);
						lonnn.add(0l);
					}else{
						lonnn.add(objectDate);
					}
					d1 = DateUtils.addHours(d1, 24);
				}
				sValue.data = lonnn;
			}
			
			data.add(sValue);
		  }
		
		Date d1 = startDate;
		Date d2 = endDate;
		while((d1.before(d2)|| d1.equals(d2))){
			Date d = d1;
			try {
				String dt = df1.format(d);
				dates.add(dt);
			} catch (Exception e) {
				e.printStackTrace();
			}
			d1 = DateUtils.addHours(d1, 24);
		}
		
		
		
	/*	for(sendDateAndValue sAndValue:sAndValues){
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
			
		}*/
		
		/*for(sendDataVM sValue:sAndValues){
		
			Collections.sort(sValue.data, new Comparator<List<Long>>(){
				 @Override
		            public int compare(List<Long> o1, List<Long> o2) {
		                return o1.get(0).compareTo(o2.get(0));
		            }
				
			});
		}*/
		
	}
	
	
	public static Result getFinancialVehicleDetails(String startD,String endD){
		AuthUser user = getLocalUser();
		//List<List<>> sAndValues = new ArrayList<>();
		Map<String, Object> sAndValues = new HashMap<>();
		setFinancialVehicle(user,sAndValues,startD,endD);
		return ok(Json.toJson(sAndValues));
	}
	
	
	public static void setFinancialVehicle(AuthUser user,Map<String, Object> sAndValues,String startD,String endD){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM");
		List<Vehicle> vehicle = null;
		Map<String, Long> mapMake = new HashMap<String, Long>();
		Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
		List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		sAndValues.put("dates",dates);
		sAndValues.put("data",data);
		
		Date startDate = null;
		Date endDate = null;
		try {
			startDate=df.parse(startD);
			endDate=df.parse(endD);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
			List<Long> lonnn = new ArrayList<>();
			sendDataVM sValue = new sendDataVM();
			List<Vehicle> veList  = null;
			if(user.role.equals("General Manager")){
				veList = Vehicle.findBySold();
			}else if(user.role.equals("Manager")){
				veList = Vehicle.findByMakeAndSoldLocation(entry.getKey(), user.location.id);
			}else if(user.role.equals("Sales Person")){
				veList = Vehicle.findByMakeAndSold(entry.getKey(), user);
			}
			 
			sValue.name = entry.getKey();
			if(veList != null){
					
				for(Vehicle vhVehicle:veList){
					
					if((vhVehicle.getSoldDate().after(startDate) && vhVehicle.getSoldDate().before(endDate)) || vhVehicle.getSoldDate().equals(endDate) || vhVehicle.getSoldDate().equals(startDate))
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
				Date d1 = startDate;
				Date d2 = endDate;
				while((d1.before(d2)|| d1.equals(d2))){
					//dates.add(d1.getTime() + (1000 * 60 * 60 * 24));
					Long objectDate = mapdate.get(d1.getTime() + (1000 * 60 * 60 * 24));
					if(objectDate == null){
						mapdate.put(d1.getTime() + (1000 * 60 * 60 * 24), 0l);
						lonnn.add(0l);
					}else{
						lonnn.add(objectDate);
					}
					d1 = DateUtils.addHours(d1, 24);
				}
				
				/*for (Entry<Long, Long> entryValue : mapdate.entrySet()) {
					lonnn.add(entryValue.getValue());
				  }*/
				sValue.data = lonnn;
			}
			data.add(sValue);
		  }
		Date d1 = startDate;
		Date d2 = endDate;
		while((d1.before(d2)|| d1.equals(d2))){
			Date d = d1;
			try {
				String dt = df1.format(d);
				dates.add(dt);
			} catch (Exception e) {
				e.printStackTrace();
			}
			d1 = DateUtils.addHours(d1, 24);
		}
		
		
		
		/*for(sendDateAndValue sAndValue:sAndValues){
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
			
		}*/
		
		/*for(sendDateAndValue sValue:sAndValues){
		
			Collections.sort(sValue.data, new Comparator<List<Long>>(){
				 @Override
		            public int compare(List<Long> o1, List<Long> o2) {
		                return o1.get(0).compareTo(o2.get(0));
		            }
				
			});
		}*/
	}
	
	/*public static void setFinancialVehicle(AuthUser user,List<sendDateAndValue> sAndValues,String startD,String endD){
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
					
					if((vhVehicle.getSoldDate().after(startDate) && vhVehicle.getSoldDate().before(endDate)) || vhVehicle.getSoldDate().equals(endDate) || vhVehicle.getSoldDate().equals(startDate))
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
				Date d1 = startDate;
				Date d2 = endDate;
				while((d1.before(d2)|| d1.equals(d2))){
					Long objectDate = mapdate.get(d1.getTime() + (1000 * 60 * 60 * 24));
					if(objectDate == null){
						mapdate.put(d1.getTime() + (1000 * 60 * 60 * 24), 0l);
					}
					d1 = DateUtils.addHours(d1, 24);
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
	*/
	
	
	
	public static Result getSoldVehicleDetailsAvgSale(String startD,String endD){
		List<Long[]> lonnn = new ArrayList<>();
		AuthUser user = getLocalUser();
		soldSaleVolu(user,lonnn,startD,endD);
		return ok(Json.toJson(lonnn));
	}

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
				//Calendar c = Calendar.getInstance();
				//c.setTime(vhVehicle.getSoldDate());
				//c.add(Calendar.DATE, -1);
				
				String dateCheck = df.format(vhVehicle.getSoldDate());
				Date dateFomat = null;
				try {
					dateFomat = df.parse(dateCheck);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if((dateFomat.after(startDate) && dateFomat.before(endDate)) || dateFomat.equals(endDate) || dateFomat.equals(startDate))
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
				//Integer totalDayCar = Integer.parseInt(arr[1]) + vehicaleNew.size();
				Integer totalDayCar = Integer.parseInt(arr[1]);
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
		}else if(user.role.equals("Photographer")){
			vehicle = Vehicle.findBySold();
		}
			for(Vehicle vhVehicle:vehicle){
				if(vhVehicle.price != null){
					pricevalue = vhVehicle.price.longValue();
				}else{
					pricevalue = 0L;
				}
				
				//Calendar c = Calendar.getInstance();
				//c.setTime(vhVehicle.getSoldDate());
				//c.add(Calendar.DATE, -1);
				
				String dateCheck = df.format(vhVehicle.getSoldDate());
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
				
				if((dateFomat.after(startDate) && dateFomat.before(endDate)) || dateFomat.equals(endDate) || dateFomat.equals(startDate))
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
    		return ok(home.render("",userRegistration));
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
	
	public static Result downloadStatusFile(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		String filePath = rootDir+File.separator+"CsvFile/contacts.csv";
    		File file = new File(filePath);
    		return ok(file);
    	}
	}
	
	
	public static Result scheduleEmail(List<String> emailList) {
		
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
		 		props.put("mail.smtp.port", port);
		  
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
    		return ok(home.render("",userRegistration));
    	} else {
    			List<String> timeList = new ArrayList<>();
    			try {
    				AuthUser user = getLocalUser();
    				DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    				SimpleDateFormat time = new SimpleDateFormat("h:mm:a");
        			Date d = df1.parse(comDate);
        			List<RequestMoreInfo> moreInfo = RequestMoreInfo.findByVinDate(vin,d);
        			List<ScheduleTest> schedule = ScheduleTest.findByVinDate(vin,d);
        			List<TradeIn> traidInfo = TradeIn.findByVinDate(vin,d);
        			List<ScheduleTest> schList = ScheduleTest.findByUserDate(user,d);
        			for (TradeIn info : traidInfo) {
        				timeList.add(time.format(info.confirmTime));
					}
        			for (RequestMoreInfo info : moreInfo) {
        				timeList.add(time.format(info.confirmTime));
					}
        			for (ScheduleTest info : schedule) {
        				timeList.add(time.format(info.confirmTime));
					}
        			
        			for (ScheduleTest info : schList) {
        				if(info.meetingStatus != null){
        					if(info.confirmEndTime != null)
        						timeList.add(time.format(info.confirmTime)+"-"+time.format(info.confirmEndTime));
        					else
        						timeList.add(time.format(info.confirmTime));
        				}
        				else{
        					timeList.add(time.format(info.confirmTime));
        				}
        			}
        			
				} catch (Exception e) {
					e.printStackTrace();
				}
    			
    		return ok(Json.toJson(timeList));
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
	
	 private static void vehicleSoldMail(List<RequestInfoVM> vinAndMailList) {
	    	
	         for(RequestInfoVM vm:vinAndMailList){
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
	    		try{
				message.setFrom(new InternetAddress(emailUser,emailName));
	    		}catch(UnsupportedEncodingException e){
	    			e.printStackTrace();
	    		}
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(vm.email.toString()));
				System.out.println(vm.email.toString());
				message.setSubject("VEHICLE SOLD CONFIRMATION");
				Multipart multipart = new MimeMultipart();
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart = new MimeBodyPart();
				
				VelocityEngine ve = new VelocityEngine();
				ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
				ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
				ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
				ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
				ve.init();
			
				
		        Template t = ve.getTemplate("/public/emailTemplate/vehiclehasbeenSOLD.html"); 
		        VelocityContext context = new VelocityContext();
		        /*String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
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
				}*/
		        
		       // String monthName = months[month-1];
		        context.put("hostnameUrl", imageUrlPath);
		        context.put("siteLogo", logo.logoImagePath);
		       /*context.put("dayOfmonth", dayOfmonth);
		       context.put("monthName", monthName);
		        context.put("confirmTime", map.get("confirmTime"));*/
		        
		        Vehicle vehicle = Vehicle.findByVin(vm.vin.toString());
		        context.put("year", vehicle.year);
		        context.put("make", vehicle.make);
		        context.put("model", vehicle.model);
		        context.put("price", "$"+vehicle.price);
		        context.put("stock", vehicle.stock);
		        context.put("vin", vehicle.vin);
		        context.put("mileage", vehicle.mileage);
		        /*context.put("name", map.get("uname"));
		        context.put("email", map.get("uemail"));
		        context.put("phone",  map.get("uphone"));
		        String weather= map.get("weatherValue").toString();
		        String arr1[] = weather.split("&");
		        String nature=arr1[0];
		        String temp=arr1[1];
		        context.put("nature",nature);
		        context.put("temp", temp);*/
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
			if(contacts.phone != null){
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
    	} else {
    		AuthUser user = getLocalUser();
	    	AuthUser userObj = AuthUser.findById(id);
	    	/*Comments comm = Comments.getByUser(userObj);
	    	if(comm != null) {
	    		comm.setComment(comment);
	    		comm.update();
	    	}else{*/
	    	   Date currDate=new Date();
	    		Comments cm = new Comments();
	    		cm.comment = comment;
	    		cm.likeDate =currDate;
	    		cm.newTime=currDate;
	    		cm.user = userObj;
	    		cm.commentUser = user;
	    		cm.commentFlag = 1;
	    		cm.save();
	    	//}
	    		String subject=null;
	    		int flag=0;
	    		if(user.role.equals("General Manager")){
	    			subject = "General Manager likes your work!";
	    			flag=1;
	    		}
	    		else{
	    			subject = "Manager likes your work!";
	    			flag=0;
	    		}
	    	String comments = "Comment : "+comment;
	    	managerLikeWork(userObj.communicationemail, subject, comments,flag);
	    	return ok();
    	}	
    }
	
	private static void managerLikeWork(String email,String subject,String comments,Integer flag) {
    	
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
			Message message = new MimeMessage(session);
    		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
			
	        Template t = ve.getTemplate("/public/emailTemplate/managerLikesyourWork_HTML.vm"); 
	        VelocityContext context = new VelocityContext();
	        context.put("comments",comments);
	        context.put("flag",flag);
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
    

	
	
	
	
	public static Result getSendDemoLink(Long userId){
		
		Registration regi = Registration.findById(userId);
		String subject = "Manager like your work";
    	String comments = "Comment : ";
    	sendEmail(regi.email, subject, comments);
		return ok();
	}
	
	public static Result sendEmail(String email, String subject ,String comment) {
		
		final String username = emailUsername;
		final String password = emailPassword;
		
		
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
		System.out.println(email);
		System.out.println(username);
		System.out.println(password);
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailUser, emailPass);
			}
		});
		    try {  
		     MimeMessage message = new MimeMessage(session);  
	    		try{
		     message.setFrom(new InternetAddress(emailUser,emailName));  
	    		}
	    		catch(UnsupportedEncodingException e){
	    			e.printStackTrace();
	    		}
		     message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));  
		     message.setSubject(subject);  
		     message.setText(comment);  
		     Transport.send(message);  
		  
		     System.out.println("message sent successfully...");  
		   
		     } catch (MessagingException e) {
		    	 e.printStackTrace();
		    }
				
		return ok();
	}
	
public static Result sendEmailAfterDay(String email, String subject ,String comment,String vin,Date confirmDate,Date confirmTime,AuthUser user,String clientName) {
		final String username = emailUsername;
		final String password = emailPassword;
		
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
    		usersArray[1] = new InternetAddress(map.get("custEmail").toString());
    		*/
			Message message = new MimeMessage(session);
    		try{
			message.setFrom(new InternetAddress(emailUser,emailName));
    		}
    		catch(UnsupportedEncodingException e){
    			e.printStackTrace();
    		}
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
		
		    Template t = ve.getTemplate("/public/emailTemplate/testDriveReminder48hours_HTML.vm"); 
	        VelocityContext context = new VelocityContext();
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
 	        	String dateInString = formatter.format(confirmDate); /*confirmDate*/
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime(confirmDate);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        
	        
	        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
			String time1=time.format(confirmTime);
			context.put("confirmTime",time1);
	        Vehicle vehicle = Vehicle.findByVin(vin.toString());
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
             if(clientName != null){
    	        	
              	  context.put("clientName", clientName);
    	        	 
    	        }
    	        else{
    	        	 context.put("clientName","");
    	        }
            context.put("typeofVehicle", vehicle.typeofVehicle);
	        context.put("name",user.firstName+" "+user.lastName);
	        context.put("email", user.email);
	        context.put("phone", user.phone);
	        /*String weather= map.get("weatherValue").toString();
	        String arr1[] = weather.split("&");
	        String nature=arr1[0];
	        String temp=arr1[1];
	        context.put("nature",nature);
	        context.put("temp", temp);*/
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
    
		return ok();
	}
	
	
	/*public static Result sendEmail(final String email, final String subject ,final String comment) {
		ActorSystem newsLetter = Akka.system();
		newsLetter.scheduler().scheduleOnce(Duration.create(0, TimeUnit.MILLISECONDS), 
				new Runnable() {
			public void run() {
				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.host", "smtp.gmail.com");
				props.put("mail.smtp.port", "587");
				props.put("mail.smtp.starttls.enable", "true");
				System.out.println(email);
				Session session = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(emailUsername, emailPassword);
					}
				});
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
	}*/
	
	public static Result getAcceptAndDecline(Long id,String reason,String status){
		
		ScheduleTest stTest = ScheduleTest.findById(id);
		if(stTest != null){
			if(status.equals("accept")){
				
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		    	SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
		    	String date1=df.format(stTest.confirmDate);
				String time1=time.format(stTest.confirmTime);
				Date date=new Date();
				stTest.setMeetingActionTime(date);
				stTest.setAcceptMeeting(2);
				stTest.setMeeting(0);
				stTest.setMeetingAcceptFlag(1);
				stTest.update();
				AuthUser userEmail = AuthUser.findById(stTest.user.id);
				AuthUser assigEmail = AuthUser.findById(stTest.assignedTo.id);
				String subject = assigEmail.firstName+"  "+assigEmail.lastName+" accepted your invitation.";
				String comments = "Your invitation to "+assigEmail.firstName+" "+assigEmail.lastName+" has been accepted \n "+date1+" "+time1+" "+stTest.name;
				
		    	
				
				
				sendEmail(userEmail.communicationemail, subject, comments);
			}
			if(status.equals("decline")){
				
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		    	SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
		    	String date1=df.format(stTest.confirmDate);
				String time1=time.format(stTest.confirmTime);
				Date date=new Date();
				stTest.setMeetingActionTime(date);
					stTest.setDeclineMeeting(2);
				stTest.setMeeting(2);
				stTest.setMeetingDeclineFlag(1);
				stTest.setDeclineReason(reason);
				//stTest.setReason(reason);
				stTest.update();
				AuthUser userEmail = AuthUser.findById(stTest.user.id);
				AuthUser assigEmail = AuthUser.findById(stTest.assignedTo.id);
				String subject = assigEmail.firstName+"  "+assigEmail.lastName+" declined your Meeting Invitation.";
				String fullName=assigEmail.firstName+"  "+assigEmail.lastName;
				String comments = "Your Meeting Invitation to "+assigEmail.firstName+" "+assigEmail.lastName+" has been declined \n Reason : "+reason+"\n "+date1+" "+time1+" "+stTest.name;
				
		    	
				
				
				declineMeetingMail(userEmail.communicationemail,date1,time1,fullName);
			}
		}
		
		return ok();
		
	}
	
	

	public static void declineMeetingMail(String email,String confirmDate,String confirmTime,String hostName){
		/*InternetAddress[] usersArray = new InternetAddress[userList.size()];
		int index = 0;
		for (AuthUser assi : userList) {
			try {
				
				usersArray[index] = new InternetAddress(assi.getCommunicationemail());
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		/*List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : userList){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
		}*/
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
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
			/*usersArray*/
			message.setSubject("Meeting Declined");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/meetingInvitationDeclined.html"); 
	        VelocityContext context = new VelocityContext();
	        
	        //context.put("title", vm.name);
	       // context.put("location", loc.getName());
	        //context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = confirmDate;
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date = formatter.parse(dateInString);
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	        //context.put("userList",list);
	        
	      //  context.put("date", vm.getBestDay());
	        context.put("time", confirmTime);
	        context.put("hostName", hostName);
	        //context.put("disc", vm.getReason());
	       
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

	
	
	
	
	
	
	
	
	public static Result getinvitationMsg(){
		 AuthUser user = getLocalUser();
	        
	        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
	        Date currD = new Date();
	        String cDate = df.format(currD);
	        Date datec = null;
	        try {
				datec = df.parse(cDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		List<ScheduleTest> list = ScheduleTest.findAllByInvitationTest(user, datec);
		
		List<RequestInfoVM> checkData = new ArrayList<>();
		for(ScheduleTest sche:list){
			
			RequestInfoVM sTestVM = new RequestInfoVM();
        	
        	
			sTestVM.id = sche.id;
        	sTestVM.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(sche.confirmDate);
        	sTestVM.confirmTime = new SimpleDateFormat("hh:mm a").format(sche.confirmTime);
        	sTestVM.confirmDateOrderBy = sche.confirmDate;
        	sTestVM.typeOfLead = "Schedule Test Drive";
        	sTestVM.name = sche.name;
    		sTestVM.phone = sche.phone;
    		sTestVM.email = sche.email;
    		
    		AuthUser user2 = AuthUser.findById(sche.user.id);
    		if(user2.imageUrl != null) {
				if(user2.imageName !=null){
					sTestVM.imageUrl = "http://glider-autos.com/glivrImg/images"+user2.imageUrl;
				}else{
					sTestVM.imageUrl = user2.imageUrl;
				}
				
			} else {
				sTestVM.imageUrl = "/profile-pic.jpg";
			}
    		
    		checkData.add(sTestVM);
    		
			sche.setSendInvitation(0);
			//sche.update();
		}
		
		return ok(Json.toJson(checkData));
		
	}
	
	public static Result getcommentLike(){
		AuthUser user = (AuthUser) getLocalUser();
		
		List<UserVM> listU = new ArrayList<>();
		List<Comments> comments = Comments.getByListUserWithFlag(user);
		for(Comments comm:comments){
			UserVM uVm = new UserVM();
			uVm.firstName = comm.commentUser.getFirstName();
			uVm.lastName = comm.commentUser.getLastName();
			uVm.id = comm.commentUser.id;
			uVm.userComment=comm.comment;
			if(comm.commentUser.imageUrl != null) {
				if(comm.commentUser.imageName !=null){
					uVm.imageUrl = "http://glider-autos.com/glivrImg/images"+comm.commentUser.imageUrl;
				}else{
					uVm.imageUrl = comm.commentUser.imageUrl;
				}
				
			} else {
				uVm.imageUrl = "/profile-pic.jpg";
			}
			
			listU.add(uVm);
			
			comm.setCommentFlag(0);
			//comm.update();
			
		}
		
		return ok(Json.toJson(listU));
	}
	
    public static Result getPlanMonday(){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		
		int flag = 0;

		String cDate = df.format(date);
		Date cuD = null;
		try {
			cuD = df.parse(cDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		System.out.println("Current Month is : " + (c.get(Calendar.MONTH) + 1));
		String DateString = 01 + "-" + (c.get(Calendar.MONTH) + 1) + "-"+ c.get(Calendar.YEAR);
		Date dates = null;
		try {
			dates = df.parse(DateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date maxTime = null;
		for (int i = 0; i < 8; i++) {
			maxTime = dates;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(maxTime);
			calendar.add(Calendar.DATE, 1);
			dates = calendar.getTime();
			if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
				if (cuD.equals(dates)) {
					flag = 1;
				}
			}

		}
    
		return ok(Json.toJson(flag));
    }
    
    public static Result getdecline(){
    
    	AuthUser users = getLocalUser();
    	List<ScheduleTest> sche = ScheduleTest.getdecline(users);
    	for(ScheduleTest sch:sche){
    		sch.setDeclineMeeting(0);
    		sch.update();
    	}
    	
    	return ok(Json.toJson(sche));
    }
    
    public static Result getPlanMsg(){
    	AuthUser users = getLocalUser();
    	List<PlanScheduleMonthlySalepeople> salepeople = PlanScheduleMonthlySalepeople.findByAllMsgPlan(users);
    	for(PlanScheduleMonthlySalepeople sales:salepeople){
    		sales.setFlagMsg(0);
    		//sales.update();
    		
    	}
    	
    	return ok(Json.toJson(salepeople));
    }
    
    
    public static Result getdeleteMeeting(){
    	
    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
    	AuthUser users = getLocalUser();
    	List<ScheduleTest> sche = ScheduleTest.getdeleteMsg(users);
    	
    	
    	List<ScheduleTestVM> list = new ArrayList<ScheduleTestVM>();
    	for(ScheduleTest sch:sche){
    		if(sch.declineUser.equals("Host")){
    			if(!users.id.equals(sch.user.id)){
    				
    				sch.setDeleteMsgFlag(0);
    	    		//sch.update();
    				
    				ScheduleTestVM sLVm = new ScheduleTestVM();
    	    		sLVm.name = sch.name;
    	    		sLVm.reason = sch.reason;
    	    		sLVm.confirmDate = df.format(sch.confirmDate);
    	    		sLVm.confirmTime = parseTime.format(sch.confirmTime);
    	    		AuthUser usersData = AuthUser.findById(sch.assignedTo.id);
    	    		sLVm.firstName = usersData.firstName;
    	    		sLVm.lastName = usersData.lastName;
    	    		sLVm.declineUser = sch.declineUser;
    	    		list.add(sLVm);
    			}
    		}else if(sch.declineUser.equals("this person")){
    			
    			if(!users.id.equals(sch.assignedTo.id)){
    				sch.setDeleteMsgFlag(0);
            		//sch.update();
        			
        			ScheduleTestVM sLVm = new ScheduleTestVM();
    	    		sLVm.name = sch.name;
    	    		sLVm.reason = sch.reason;
    	    		sLVm.confirmDate = df.format(sch.confirmDate);
    	    		sLVm.confirmTime = parseTime.format(sch.confirmTime);
    	    		AuthUser usersData = AuthUser.findById(sch.assignedTo.id);
    	    		sLVm.firstName = usersData.firstName;
    	    		sLVm.lastName = usersData.lastName;
    	    		sLVm.declineUser = sch.declineUser;
    	    		list.add(sLVm);
    			}
    		}
    		
    	}
    	
    	
    	return ok(Json.toJson(list));
    }
    
    public static Result getUpdateMeeting(){
    	
    	AuthUser users = getLocalUser();
    	List<ScheduleTest> sche = ScheduleTest.getUpdateMeeting(users);
    	List<ScheduleTestVM> list = new ArrayList<>();
    	for(ScheduleTest sch:sche){
    		ScheduleTestVM vm = new ScheduleTestVM();
    		vm.confirmTime = new SimpleDateFormat("hh:mm a").format(sch.confirmTime);
    		vm.confirmEndTime = new SimpleDateFormat("hh:mm a").format(sch.confirmEndTime);
    		vm.confirmDate = new SimpleDateFormat("MM-dd-yyyy").format(sch.confirmDate);
    		vm.name = sch.name;
    		vm.reason = sch.reason;
    		list.add(vm);
    		sch.setDeclineUpdate(0);
    		sch.update();
    	}
    	
    	return ok(Json.toJson(list));
    }
    
    public static Result getaccepted(){
    	
    	AuthUser users = getLocalUser();
    	List<ScheduleTest> sche = ScheduleTest.getaccepted(users);
    	for(ScheduleTest sch:sche){
    		sch.setAcceptMeeting(0);
    		sch.update();
    	}
    	
    	return ok(Json.toJson(sche));
    }
    
    public static Result deleteAppointById(Long id,String typeOfLead,String reason){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		
    		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
    		AuthUser users = getLocalUser();
    		String clientEmail = null;
    		String comments = null;
    		String subject = null;
    		if(typeOfLead.equals("Schedule Test Drive") || typeOfLead.equals("Meeting")){
    			ScheduleTest test = ScheduleTest.findById(id);
        		
        		if(test !=null){
        			if(test.groupId != null){
        				
        				subject ="Meeting has been canceled";
        				if(test.user.id.equals(users.id)){
        					String uEmail = null;
        					List<ScheduleTest> grouptest = ScheduleTest.findAllGroupMeeting(test.groupId);
        					for(ScheduleTest sche:grouptest){
        						
        						AuthUser userEmail = AuthUser.findById(sche.user.id);
        						/*//sche.setConfirmDate(null);
        						//sche.setConfirmTime(null);
        						sche.setLeadStatus(null);
        						//sche.setDeclineMeeting(2);
        						sche.setMeeting(2);
        						sche.setDeleteMsgFlag(1);
        						sche.setReason(reason);
        						sche.setDeclineUser("Host");
        						sche.update();*/
        						uEmail = userEmail.communicationemail;
        						Date date=new Date();
        						sche.setMeetingActionTime(date);
        						AuthUser userNames = AuthUser.findById(sche.assignedTo.id);
        						
        						comments= userNames.firstName+" "+userNames.lastName+" can't go to the "+sche.name+" \n"+df.format(sche.confirmDate)+"  "+parseTime.format(sche.confirmTime)+"\n"+sche.reason+".";
        						
        						meetingCancelMail(userNames.communicationemail,sche.confirmDate,sche.confirmTime);
        						sche.delete();
        					}
        					sendEmail(uEmail,subject,comments);
        				}else{
        					
        					ScheduleTest oneGrouptest = ScheduleTest.findAllGroupUserMeeting(test.groupId, users);
        					
        					if(oneGrouptest != null){
        						AuthUser userEmail = AuthUser.findById(oneGrouptest.user.id);
        						/*//oneGrouptest.setConfirmDate(null);
            					//oneGrouptest.setConfirmTime(null);
            					oneGrouptest.setLeadStatus(null);
            					//oneGrouptest.setDeclineMeeting(2);
            					oneGrouptest.setDeleteMsgFlag(1);
            					oneGrouptest.setMeeting(2);
            					oneGrouptest.setReason(reason);
            					oneGrouptest.setDeclineUser("this person");
            					oneGrouptest.update();*/
        						Date date=new Date();
        						oneGrouptest.setMeetingActionTime(date);
            					AuthUser userNames = AuthUser.findById(oneGrouptest.assignedTo.id);
        						comments= userNames.firstName+" "+userNames.lastName+" can't go to the "+oneGrouptest.name+" \n"+df.format(oneGrouptest.confirmDate)+"  "+parseTime.format(oneGrouptest.confirmTime)+"\n"+oneGrouptest.reason+".";
        						sendEmail(userEmail.communicationemail,subject,comments);
        						sendEmail(userNames.communicationemail,subject,comments);
        						oneGrouptest.delete();
        					}
        					
        				}
        				
        			}else{
        				Date date=new Date();
        				test.setMeetingActionTime(date);
        				test.setConfirmDate(null);
        				test.setConfirmTime(null);
        				test.setLeadStatus(null);
        				test.setDeclineMeeting(0);
        				test.update();
        				subject ="Test Drive has been canceled";
        				comments="Test Drive has been canceled";
        				sendEmail(test.email,subject,comments);
        				//test.delete();
        			}
        			
        			
        		}
    		}else if(typeOfLead.equals("Request More Info")){
    			
    			
    			RequestMoreInfo rInfo = RequestMoreInfo.findById(id);
    			if(rInfo != null){
    				rInfo.setConfirmDate(null);
        			rInfo.setConfirmTime(null);
        			rInfo.setStatus(null);
        			rInfo.update();
        			subject ="Test Drive has been canceled";
    				comments="Test Drive has been canceled";
    				sendEmail(rInfo.email,subject,comments);
    			}
    			
    		}else if(typeOfLead.equals("Trade-In Appraisal")){
    			TradeIn tIn = TradeIn.findById(id);
    			if(tIn != null){
    				tIn.setConfirmDate(null);
        			tIn.setConfirmTime(null);
        			tIn.setStatus(null);
        			tIn.update();
        			subject ="Test Drive has been canceled";
    				comments="Test Drive has been canceled";
    				sendEmail(tIn.email,subject,comments);
    			}
    		}
    		
    		
        	return ok();
    	}
    }
    
    
    public static void meetingCancelMail(String communicationMail,Date confirmDate,Date confirmTime){
		/*InternetAddress[] usersArray = new InternetAddress[userList.size()];
		int index = 0;
		for (AuthUser assi : userList) {
			try {
				
				usersArray[index] = new InternetAddress(assi.getCommunicationemail());
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		/*List<UserVM> list = new ArrayList<>() ;
		for(AuthUser assi : userList){
			
			UserVM vm1=new UserVM();
			vm1.fullName=assi.firstName+" "+assi.lastName;
			list.add(vm1);
			
			
			
		}
		*/
		
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
    		catch(UnsupportedEncodingException e)
    		{
    			e.printStackTrace();
    		}
    			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(communicationMail));
			/*usersArray*/
			message.setSubject("Meeting Cancelled");
			Multipart multipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			ve.setProperty("runtime.log.logsystem.log4j.logger","clientService");
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
			ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			ve.init();
			
			Template t = ve.getTemplate("/public/emailTemplate/internalMeetingCANCELED_HTML.html"); 
	        VelocityContext context = new VelocityContext();
	        
	        //context.put("title", vm.name);
	       // context.put("location", loc.getName());
	       // context.put("meetingBy", user.getFirstName()+" "+user.getLastName());
	        
	        String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		       
	        int dayOfmonth=1;
	        int month=0;
	        try {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        	String dateInString = formatter.format(confirmDate);
	        	String arr[] = dateInString.toString().split("-");
		        if(arr.length >=2){
		        	dayOfmonth = Integer.parseInt(arr[0]);
			        month = Integer.parseInt(arr[1]);
		        }else{
		        	Date date =confirmDate;
		        	Calendar cal = Calendar.getInstance();
			         cal.setTime((Date)date);
			         dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
			         month = cal.get(Calendar.MONTH)+1;
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//	String dateInString = vm.getBestDay();

	
	        
	        
	        String monthName = months[month-1];
	        context.put("hostnameUrl", imageUrlPath);
	       // context.put("siteLogo", logo.logoImagePath);
	        context.put("dayOfmonth", dayOfmonth);
	        context.put("monthName", monthName);
	        //context.put("confirmTime", map.get("confirmTime"));
	    //    context.put("userList",list);
	        
	       // context.put("date", vm.getBestDay());
	        
	        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm:aa");
	        String time = localDateFormat.format(confirmTime);
	        
	        context.put("time", time);
	        //context.put("disc", vm.getReason());
	       
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
    
}