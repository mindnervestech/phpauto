package controllers;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.internet.MimeMessage;
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
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.HttpsURLConnection;

import models.AuthUser;
import models.Blog;
import models.FeaturedImage;
import models.FeaturedImageConfig;
import models.FollowBrand;
import models.Permission;
import models.PriceAlert;
import models.RequestMoreInfo;
import models.ScheduleTest;
import models.Site;
import models.SiteContent;
import models.SiteLogo;
import models.SliderImage;
import models.SliderImageConfig;
import models.ToDo;
import models.TradeIn;
import models.Vehicle;
import models.VehicleAudio;
import models.VehicleImage;
import models.VirtualTour;
import models.MyProfile;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import scala.Array;
import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;
import viewmodel.AudioVM;
import viewmodel.BarChartVM;
import viewmodel.BlogVM;
import viewmodel.EditImageVM;
import viewmodel.ImageVM;
import viewmodel.InfoCountVM;
import viewmodel.PinVM;
import viewmodel.RequestInfoVM;
import viewmodel.SiteContentVM;
import viewmodel.SiteLogoVM;
import viewmodel.SiteVM;
import viewmodel.SpecificationVM;
import viewmodel.ToDoVM;
import viewmodel.TradeInVM;
import viewmodel.UserVM;
import viewmodel.VehicleVM;
import viewmodel.VirtualTourVM;
import viewmodel.profileVM;
import views.html.home;
import views.html.index;
import au.com.bytecode.opencsv.CSVWriter;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;
import com.mnt.dataone.Equipment;
import com.mnt.dataone.InstalledEquipment;
import com.mnt.dataone.Option;
import com.mnt.dataone.OptionalEquipment;
import com.mnt.dataone.ResponseData;
import com.mnt.dataone.Specification;
import com.mnt.dataone.Specification_;
import com.mnt.dataone.Value;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class Application extends Controller {
  
	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	
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
			
			public static int userId = -1361609913;
			
	static String simulatevin = "{    'success': true,    'specification': {        'vin': 'WDDNG7KB7DA494890',        'year': '2013',        'make': 'Mercedes-Benz',        'model': 'S-Class',        'trim_level': 'S65 AMG',        'engine': '6.0L V12 SOHC 36V TURBO',        'style': 'SEDAN 4-DR',        'made_in': 'GERMANY',        'steering_type': 'R&P',        'anti_brake_system': '4-Wheel ABS',        'tank_size': '23.80 gallon',        'overall_height': '58.00 in.',        'overall_length': '206.50 in.',        'overall_width': '73.70 in.',        'standard_seating': '5',        'optional_seating': null,        'highway_mileage': '19 miles/gallon',        'city_mileage': '12 miles/gallon'    },    'vin': 'WDDNG7KB7DA494890'}";

	private static boolean simulate = false;
    /*public static Result index() {
        return ok(index.render("Your new application is ready."));
    }*/
	
	public static Result login() {
		String email = Form.form().bindFromRequest().get("email");
		String password= Form.form().bindFromRequest().get("password");
		AuthUser user = AuthUser.find.where().eq("email", email).eq("password", password).findUnique();
		if(user != null) {
			session("USER_KEY", user.id+"");
			//return  redirect("/dealer/index.html#/");
    		HashMap<String, Boolean> permission = new HashMap<String, Boolean>();
    		List<Permission> userPermissions = user.getPermission();
    		for(Permission per: userPermissions) {
    			permission.put(per.name, true);
    		}
    		return ok(index.render(Json.stringify(Json.toJson(permission))));
		} else {
			return ok(home.render("Invalid Credentials"));
		}
	}
	
	public static Result logout() {
		session().clear();
		return ok(home.render(""));
	}
	
	public static Result home() {
		return ok(home.render(""));
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
    		return ok(index.render(Json.stringify(Json.toJson(permission))));
    	}
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
    	Identity user = getLocalUser();
    	AuthUser userObj = (AuthUser)user;
    	
    	Vehicle vehicle = Vehicle.findByVidAndUser(vin); 
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
	    		pinObj = new ObjectMapper().readValue(simulatevin, PinVM.class);
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
    
    
    public static Result saveVehicle() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	Identity user = getLocalUser();
	    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
	    	SpecificationVM vm = form.get();
	    	AuthUser userObj = (AuthUser)user;
	    	Vehicle vehicleObj = Vehicle.findByVidAndUser(vm.vin);
	    	Vehicle vehicle = new Vehicle();
	    	if(vehicleObj == null) {
		    	
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
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
	    	sendEmailToBrandFollowers();
	    	Vehicle vehicleObj2 = Vehicle.findByVidAndUser(vm.vin);
	    	List<Site> siteList = vehicleObj2.getSite();
	    	MyProfile profile = MyProfile.findByUser(userObj);
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
	    	    String contentType = picture.getContentType(); 
	    	    File fdir = new File(rootDir+File.separator+vin+"-"+userObj.id);
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+vin+"-"+userObj.id+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+vin+"-"+userObj.id+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				VehicleImage imageObj = VehicleImage.getByImagePath("/"+vin+"-"+userObj.id+"/"+fileName);
				if(imageObj == null) {
					VehicleImage vImage = new VehicleImage();
					vImage.vin = vin;
					vImage.imgName = fileName;
					vImage.path = "/"+vin+"-"+userObj.id+"/"+fileName;
					vImage.thumbPath = "/"+vin+"-"+userObj.id+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
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
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
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
    
    
	public static Result getAllVehicles() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	
	    	List <Vehicle> vehicleObjList = Vehicle.getVehiclesByStatus("Newly Arrived");
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	ArrayList<SpecificationVM> NewVMs = new ArrayList<>(); 
	     	for(Vehicle vm : vehicleObjList){
	     		SpecificationVM vehicle = new SpecificationVM();
	     		vehicle.id = vm.id;
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
		    	vehicle.year = vm.year;
		    	vehicle.make = vm.make+" "+vm.model;
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
		    	List<SqlRow> rows = Vehicle.getDriveTimeAndName(vehicle.vin);
		    	for(SqlRow row : rows) {
		    		Date date = (Date) row.get("confirm_date");
		    		Date timeObj = (Date) row.get("confirm_time");
		    		vehicle.testDrive = df.format(date) +" ";
		    		Calendar time = Calendar.getInstance();
		    		time.setTime(timeObj);
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
    
    
	public static Result getAllSoldVehicles() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List <Vehicle> soldVehicleObjList = Vehicle.getVehiclesByStatus("Sold");
	        
	    	ArrayList<SpecificationVM> soldVMs = new ArrayList<>(); 
	     	for(Vehicle vm : soldVehicleObjList){
	     		SpecificationVM vehicle = new SpecificationVM();
	     		vehicle.id = vm.id;
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
		    	vehicle.year = vm.year;
		    	vehicle.make = vm.make+" "+vm.model;
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
	    	
	    	if(vm != null){
	    		vm.setStatus(status);
	    		Date date = new Date();
	    		vm.setSoldDate(date);
	    		vm.update();
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
    
    public static void sendEmailToBrandFollowers() {
    	
    	AuthUser user = (AuthUser) getLocalUser();
    	List<SqlRow> brandFollowers = FollowBrand.getAllBrandFollowers(user);
    	
    	for(SqlRow row: brandFollowers) {
    			AuthUser logoUser = AuthUser.findById(userId);
	    	    SiteLogo logo = SiteLogo.findByUser(logoUser);
		    	String email = (String) row.get("email");
		    	List<FollowBrand> brandList = FollowBrand.getBrands(user, email);
		    	for(FollowBrand brandObj: brandList) {
		    		
		    		List<Vehicle> vehicleList = Vehicle.getVehiclesByMake(user, brandObj.brand);
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
				        
				        context.put("name", brandObj.name);
				        context.put("brand", brandObj.brand);
				        context.put("vehicleList", vehicleList);
				       
				        
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
    }
    
	public static void sendPriceAlertMail() {
		
		AuthUser user = AuthUser.findById(userId);
		MyProfile profile = MyProfile.findByUser(user);
		AuthUser logoUser = AuthUser.findById(userId);
 	    SiteLogo logo = SiteLogo.findByUser(logoUser);
		
		List<PriceAlert> priceAlertList = PriceAlert.getEmailsByStatus(user);
		for(PriceAlert alert: priceAlertList) {
			
			Vehicle vehicle = Vehicle.findByVidAndUser(alert.vin);
			List<Vehicle> sameBodyList = Vehicle.findSameBodyStyle(vehicle.bodyStyle,vehicle.vin);
			Vehicle sameBodyStyle = new Vehicle();
			VehicleImage sameBodyStyleDefault = new VehicleImage();
			if(sameBodyList.size() != 0) {
				sameBodyStyle = sameBodyList.get(0);
				sameBodyStyleDefault = VehicleImage.getDefaultImage(sameBodyStyle.vin);
			}
			
			Vehicle sameEngine = new Vehicle();
			VehicleImage sameEngineDefault = new VehicleImage();
			List<Vehicle> sameEngineList = Vehicle.findSameEngine(vehicle.engine,vehicle.vin);
			if(sameEngineList.size() != 0) {
				sameEngine = sameEngineList.get(0);
				sameEngineDefault = VehicleImage.getDefaultImage(sameEngine.vin);
			}
			Vehicle sameMake = new Vehicle();
			VehicleImage sameMakeDefault = new VehicleImage();
			List<Vehicle> sameMakeList = Vehicle.findSameMake(vehicle.make,vehicle.vin);
			if(sameMakeList.size() != 0) {
				sameMake = sameMakeList.get(0);
				sameMakeDefault = VehicleImage.getDefaultImage(sameMake.vin);
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
		        } else {
		        	context.put("makePrice", "");
		        	context.put("makeVin", "");
		        }
		        
		        if(sameBodyStyleDefault != null) {
		        	context.put("sameBodyStyleDefault", sameBodyStyleDefault.thumbPath);
		        } else {
		        	context.put("sameBodyStyleDefault", "");
		        }
		        if(sameEngineDefault != null) {
		        	context.put("sameEngineDefault", sameEngineDefault.thumbPath);
		        } else {
		        	context.put("sameEngineDefault", "");
		        }
		        if(sameMakeDefault != null) {
		        	context.put("sameMakeDefault", sameMakeDefault.thumbPath);
		        } else {
		        	context.put("sameMakeDefault", "");
		        }
		        VehicleImage image = VehicleImage.getDefaultImage(vehicle.vin);
		        if(image != null) {
		        	context.put("defaultImage", image.path);
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
	    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
	    	SpecificationVM vm = form.get();
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Vehicle vehicle = Vehicle.findById(vm.id);
	    	if(vehicle != null) {
	    		if(vm.price != vehicle.price) {
	    			
	    				List<PriceAlert> alertList = PriceAlert.getEmailsByVin(vehicle.vin, user);
	    				for(PriceAlert priceAlert: alertList) {
	    					priceAlert.setSendEmail("Y");
	    					priceAlert.setOldPrice(vehicle.price);
	    					priceAlert.update();
	    				}
	    				
	    		}
		    	vehicle.setStock(vm.stock);
		    	vehicle.setPrice(vm.price);
		    	
		    	vehicle.update();
		    	sendPriceAlertMail();
	    	}
	    	return ok();
    	}	
    } 
    
    
    public static Result updateVehicleById() throws SocketException, IOException{
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
	    	SpecificationVM vm = form.get();
	    	Vehicle vehicle = Vehicle.findById(vm.id);
	    	if(vehicle != null) {
	    		
	    		if(vm.price != vehicle.price) {
	    			List<PriceAlert> alertList = PriceAlert.getEmailsByVin(vehicle.vin, userObj);
    				for(PriceAlert priceAlert: alertList) {
    					priceAlert.setSendEmail("Y");
    					priceAlert.setOldPrice(vehicle.price);
    					priceAlert.update();
    				}
    				
	    		}
	    		
	    		vehicle.setCategory(vm.category);
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
		    	
		    	sendPriceAlertMail();
		    	
		    	Vehicle vehicleObj2 = Vehicle.findByVidAndUser(vm.vin);
		    	List<Site> siteList2 = vehicleObj2.getSite();
		    	MyProfile profile = MyProfile.findByUser(userObj);
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
	    	    String contentType = picture.getContentType(); 
	    	    File fdir = new File(rootDir+File.separator+vin+"-"+userObj.id+File.separator+"audio");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+vin+"-"+userObj.id+File.separator+"audio"+File.separator+fileName;
	    	    File file = picture.getFile();
	    	    try {
	    	    		FileUtils.moveFile(file, new File(filePath));
	    	    		VehicleAudio audio = new VehicleAudio();
	    	    		audio.vin = vin;
	    	    		audio.path = File.separator+vin+"-"+userObj.id+File.separator+"audio"+File.separator+fileName;
	    	    		audio.fileName = fileName;
	    	    		audio.user = userObj;
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
	    	AuthUser user = (AuthUser) getLocalUser();
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
	    	    String contentType = picture.getContentType();
	    	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"SliderImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"SliderImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+userObj.id+File.separator+"SliderImages"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				SliderImage imageObj = SliderImage.getByImagePath("/"+userObj.id+"/"+"SliderImages"+"/"+fileName);
				if(imageObj == null) {
					List<SliderImage> sliderList = SliderImage.find.all();
					SliderImage vImage = new SliderImage();
					vImage.imgName = fileName;
					vImage.path = "/"+userObj.id+"/"+"SliderImages"+"/"+fileName;
					vImage.thumbPath = "/"+userObj.id+"/"+"SliderImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
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
	    	    String contentType = picture.getContentType(); 
	    	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"FeaturedImages");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    
	    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"FeaturedImages"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+userObj.id+File.separator+"FeaturedImages"+File.separator+"thumbnail_"+fileName;
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
					vImage.path = "/"+userObj.id+"/"+"FeaturedImages"+"/"+fileName;
					vImage.thumbPath = "/"+userObj.id+"/"+"FeaturedImages"+"/"+"thumbnail_"+fileName;
					vImage.user = userObj;
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
	    	List<SliderImage> sliderList = SliderImage.findByUser(user);
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
	    	List<FeaturedImage> featuredList = FeaturedImage.findByUser(user);
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
	    	
	    	SliderImageConfig sliderConfig = SliderImageConfig.findByUser(user);
	    	ImageVM slider = new ImageVM();
	    	if(sliderConfig != null) {
	    		slider.height = sliderConfig.cropHeight;
	    		slider.width = sliderConfig.cropWidth;
	    	} else {
	    		SliderImageConfig slconfig = new SliderImageConfig();
	    		slconfig.cropHeight = 840;
	    		slconfig.cropWidth = 1400;
	    		slconfig.user = user;
	    		slconfig.save();
	    		slider.height = slconfig.cropHeight;
	    		slider.width = slconfig.cropWidth;
	    	}
	    	configList.add(slider);
	    	
	    	FeaturedImageConfig featuredConfig = FeaturedImageConfig.findByUser(user);
	    	ImageVM featured = new ImageVM();
	    	if(featuredConfig != null) {
	    		featured.height = featuredConfig.cropHeight;
	    		featured.width = featuredConfig.cropWidth;
	    	} else {
	    		FeaturedImageConfig ftdconfig = new FeaturedImageConfig();
	    		ftdconfig.cropHeight = 95;
	    		ftdconfig.cropWidth = 160;
	    		ftdconfig.user = user;
	    		ftdconfig.save();
	    		featured.height = ftdconfig.cropHeight;
	    		featured.width = ftdconfig.cropWidth;
	    	}
	    	configList.add(featured);
	    	map.put("configList", configList);
	    	
	    	SiteContent content = SiteContent.findByUser(user);
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
	    	SiteContent content = SiteContent.findByUser(user);
	    	if(content != null) {
	    		content.setHeading(heading);
	    		content.update();
	    	} else {
	    		SiteContent contentObj = new SiteContent();
	    		contentObj.heading = heading;
	    		contentObj.user = user;
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
	    	SiteContent content = SiteContent.findByUser(user);
	    	if(content != null) {
	    		content.setDescHeading(vm.descHeading);
	    		content.setDescription(vm.description);
	    		content.update();
	    	} else {
	    		SiteContent contentObj = new SiteContent();
	    		contentObj.descHeading = vm.descHeading;
	    		contentObj.description = vm.description;
	    		contentObj.user = user;
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
	           config.save();
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
	    	AuthUser user = (AuthUser) getLocalUser();
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
	    		AuthUser userObj = AuthUser.findById(userId);
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
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
    
    public static Result getAllRequestInfoSeen() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	for(RequestMoreInfo info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		AuthUser userObj = AuthUser.findById(userId);
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.note = info.note;
	    		vm.requestDate = df.format(info.requestDate);
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
    
    
    public static Result getAllScheduleTest() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
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
	    	Calendar time = Calendar.getInstance();
	    	for(ScheduleTest info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		AuthUser userObj = AuthUser.findById(userId);
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.bestDay = info.bestDay;
	    		vm.bestTime = info.bestTime;
	    		
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
	    		vm.requestDate = df.format(info.scheduleDate);
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
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	Calendar time = Calendar.getInstance();
	    	for(ScheduleTest info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		AuthUser userObj = AuthUser.findById(userId);
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
	    		vm.bestDay = info.bestDay;
	    		vm.bestTime = info.bestTime;
	    		vm.note = info.note;
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
	    		vm.requestDate = df.format(info.scheduleDate);
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
	    		AuthUser userObj = AuthUser.findById(userId);
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.firstName+" "+info.lastName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
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
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	for(TradeIn info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		AuthUser userObj = AuthUser.findById(userId);
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.firstName+" "+info.lastName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.note = info.note;
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
    
    public static Result getInfoCount() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	InfoCountVM vm = new InfoCountVM();
	    	vm.schedule = ScheduleTest.findAll();
	    	vm.trade = TradeIn.findAll();
	    	vm.req = RequestMoreInfo.findAll();
	    	
	    	return ok(Json.toJson(vm));
    	}
    }
    
    
    public static Result requestInfoMarkRead(String flag,Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
	    	RequestMoreInfo infoObj = RequestMoreInfo.findById(id);
	    		if(flag.equals("true")) {
	    			infoObj.setIsRead(1);
	    			infoObj.setAssignedTo(user);
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
	        		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
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
	    	ScheduleTest scheduleObj = ScheduleTest.findById(id);
	    	AuthUser user = (AuthUser) getLocalUser();
	    	if(flag.equals("true")) {
	    		scheduleObj.setIsRead(1);
	    		scheduleObj.setAssignedTo(user);
			}
			if(flag.equals("false")) {
				scheduleObj.setIsRead(0);
				scheduleObj.setAssignedTo(null);
			}
			
			scheduleObj.update();
			
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
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.bestDay = info.bestDay;
	    		vm.bestTime = info.bestTime;
	    		vm.requestDate = df.format(info.scheduleDate);
	    		
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
    		AuthUser user = (AuthUser) getLocalUser();
	    	TradeIn tradeObj = TradeIn.findById(id);
	    	if(flag.equals("true")) {
	    		tradeObj.setIsRead(1);
	    		tradeObj.setAssignedTo(user);
			}
			if(flag.equals("false")) {
				tradeObj.setIsRead(0);
				tradeObj.setAssignedTo(null);
			}
			tradeObj.update();
			
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
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.firstName+" "+info.lastName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
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
	    	DynamicForm dynamicForm = Form.form().bindFromRequest();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	
	    	FilePart picture = body.getFile("file0");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    String contentType = picture.getContentType(); 
	    	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"logo");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"logo"+File.separator+fileName;
	    	    File file = picture.getFile();
	    	    try {
	    	    		FileUtils.moveFile(file, new File(filePath));
	    	    		
	    	    		SiteLogo logoObj = SiteLogo.findByUser(userObj);
	    	    		
	    	    		if(logoObj == null) {
		    	    		SiteLogo logo = new SiteLogo();
		    	    		logo.logoImagePath = "/"+userObj.id+"/"+"logo"+"/"+fileName;
		    	    		logo.logoImageName = fileName;
		    	    		logo.user = userObj;
		    	    		logo.save();
	    	    		} else {
	    	    			File logoFile = new File(rootDir+File.separator+logoObj.user.id+File.separator+"logo"+File.separator+logoObj.logoImageName);
	    	    			logoFile.delete();
	    	    			logoObj.setLogoImageName(fileName);
	    	    			logoObj.setLogoImagePath("/"+userObj.id+"/"+"logo"+"/"+fileName);
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
	    	DynamicForm dynamicForm = Form.form().bindFromRequest();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	
	    	FilePart picture = body.getFile("file0");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    String contentType = picture.getContentType(); 
	    	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"fevicon");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdir();
	    	    }
	    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"fevicon"+File.separator+fileName;
	    	    File file = picture.getFile();
	    	    try {
	    	    		FileUtils.moveFile(file, new File(filePath));
	    	    		
	    	    		SiteLogo logoObj = SiteLogo.findByUser(userObj);
	    	    		
	    	    		if(logoObj == null) {
		    	    		SiteLogo logo = new SiteLogo();
		    	    		logo.faviconImagePath = "/"+userObj.id+"/"+"fevicon"+"/"+fileName;
		    	    		logo.faviconImageName = fileName;
		    	    		logo.user = userObj;
		    	    		logo.save();
	    	    		} else {
	    	    			File feviconFile = new File(rootDir+File.separator+logoObj.user.id+File.separator+"fevicon"+File.separator+logoObj.faviconImageName);
	    	    			feviconFile.delete();
	    	    			logoObj.setFaviconImageName(fileName);
	    	    			logoObj.setFaviconImagePath("/"+userObj.id+"/"+"fevicon"+"/"+fileName);
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
	    	SiteLogo logoObj = SiteLogo.findByUser(userObj);
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
	    	if(blogObj == null) {
		    	Blog blog = new Blog();
		    	blog.title = vm.title;
		    	blog.description = vm.description;
		    	blog.postedBy = vm.postedBy;
		    	blog.postedDate = new Date();
		    	blog.user = userObj;
		    	blog.save();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    
    public static Result getAllBlogs() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	List<Blog> blogList = Blog.findByUser(userObj);
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
	    	return ok(Json.toJson(vm));
    	}
    }
    
    
    public static Result updateBlog() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	Form<BlogVM> form = DynamicForm.form(BlogVM.class).bindFromRequest();
	    	BlogVM vm = form.get();
	    	Blog blogObj = Blog.findById(vm.id);
	    	if(blogObj != null) {
	    		blogObj.setTitle(vm.title);
	    		blogObj.setDescription(vm.description);
	    		blogObj.setPostedBy(vm.postedBy);
	    		blogObj.update();
	    	}
	    	return ok();
    	}	
    }
    
    public static Result myprofile() {
    	
    	Form<profileVM> form = DynamicForm.form(profileVM.class).bindFromRequest();
    	profileVM vm = form.get();
    	
    	AuthUser userObj = (AuthUser) getLocalUser();
    	MyProfile mpObj = MyProfile.findByUser(userObj);
    	
    	if(mpObj != null) {
    		System.out.println("userObj 8888888 == "+userObj.communicationemail);
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
    		mpObj.setPinterest(vm.pinterest);
    		mpObj.setInstagram(vm.instagram);
    		mpObj.setGoogleplus(vm.googleplus);
    		mpObj.user = (AuthUser) getLocalUser();
    		mpObj.save();
    	}
    	
    	userObj.communicationemail = vm.email;
    	userObj.update();
    	/*System.out.println("userObj after 1 == "+userObj.communicationemail);*/
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
    	MyProfile mpObj = MyProfile.findByUser(userObj);
    	profileVM vm = new profileVM();
    	vm.address = mpObj.address;
    	vm.myname = mpObj.myname;
    	vm.city = mpObj.city;
    	vm.country = mpObj.country;
    	vm.dealer_id = mpObj.dealer_id;
    	vm.email = mpObj.email;
    	vm.facebook = mpObj.facebook;
    	vm.googleplus = mpObj.googleplus;
    	vm.instagram= mpObj.instagram;
    	vm.phone = mpObj.phone;
    	vm.pinterest = mpObj.pinterest;
    	vm.state = mpObj.state;
    	vm.twitter = mpObj.twitter;
    	vm.web = mpObj.web;
    	vm.zip = mpObj.zip;
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
    
    public static Result saveConfirmData() throws ParseException{
    	AuthUser user = (AuthUser) getLocalUser();
    	AuthUser userObj = AuthUser.findById(userId);
    	Form<RequestInfoVM> form = DynamicForm.form(RequestInfoVM.class).bindFromRequest();
    	RequestInfoVM vm = form.get();
    	ScheduleTest  scheduleTest = ScheduleTest.findById(vm.id);
    	if(scheduleTest != null) {
    		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    		SimpleDateFormat parseTime = new SimpleDateFormat("hh:mm a");
    		Date date = df.parse(vm.confirmDate);
    		scheduleTest.setConfirmDate(date);
    		scheduleTest.setConfirmTime(parseTime.parse(vm.confirmTime));
    		scheduleTest.setEmail(vm.email);
    		scheduleTest.update();
    		AuthUser logoUser = AuthUser.findById(userId);
	    	SiteLogo logo = SiteLogo.findByUser(logoUser);
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
    					InternetAddress.parse(scheduleTest.email));
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
    	        Calendar cal = Calendar.getInstance();
    	        cal.setTime(scheduleTest.getConfirmDate());
    	        int dayOfmonth = cal.get(Calendar.DAY_OF_MONTH);
    	        int month = cal.get(Calendar.MONTH);
    	        String monthName = months[month];
    	        context.put("hostnameUrl", imageUrlPath);
    	        context.put("siteLogo", logo.logoImagePath);
    	        context.put("dayOfmonth", dayOfmonth);
    	        context.put("monthName", monthName);
    	        context.put("confirmTime", parseTime.format(scheduleTest.confirmTime));
    	        
    	        Vehicle vehicle = Vehicle.findByVin(scheduleTest.vin);
    	        context.put("year", vehicle.year);
    	        context.put("make", vehicle.make);
    	        context.put("model", vehicle.model);
    	        context.put("price", "$"+vehicle.price);
    	        context.put("stock", vehicle.stock);
    	        context.put("vin", vehicle.vin);
    	        context.put("make", vehicle.make);
    	        context.put("mileage", vehicle.mileage);
    	        context.put("name", user.firstName+" "+user.lastName);
    	        context.put("email", user.email);
    	        context.put("phone", user.phone);
    	        VehicleImage image = VehicleImage.getDefaultImage(vehicle.vin);
    	        context.put("defaultImage", image.path);
    	        
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
    	return ok();
    }
    
    public static Result saveUser() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
    		
	    	AuthUser userObj = new AuthUser();
	    	UserVM vm = form.get();
	    	
	    	userObj.firstName = vm.firstName;
	    	userObj.lastName = vm.lastName;
	    	userObj.email = vm.email;
	    	userObj.phone = vm.phone;
	    	userObj.role = vm.userType;
	    	
	    	
	    	final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    	Random rnd = new Random();

	    	   StringBuilder sb = new StringBuilder( 6 );
	    	   for( int i = 0; i < 6; i++ ) 
	    	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	    	
	    	   userObj.password = sb.toString();
	    	   List<Permission> permissionList = Permission.getAllPermission();
	    	   if(vm.userType.equals("General Manager")) {
	    		   userObj.permission = permissionList;
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
	    	   userObj.save();
	    	   
	    	   MultipartFormData body = request().body().asMultipartFormData();
	    	   if(body != null) {
	    		FilePart picture = body.getFile("file0");
		    	  if (picture != null) {
		    	    String fileName = picture.getFilename();
		    	    String contentType = picture.getContentType(); 
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
	    	   AuthUser logoUser = AuthUser.findById(userId);
	    	   SiteLogo logo = SiteLogo.findByUser(logoUser);
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
	    	        context.put("siteLogo", logo.logoImagePath);
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
    
    public static Result getAllUsers() {
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
    
    public static Result updateUser() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	
	    	Form<UserVM> form = DynamicForm.form(UserVM.class).bindFromRequest();
	    	UserVM vm = form.get();
	    	
	    	AuthUser userObj = AuthUser.findById(vm.id);
	    	userObj.setFirstName(vm.firstName);
	    	userObj.setLastName(vm.lastName);
	    	userObj.setEmail(vm.email);
	    	userObj.setPhone(vm.phone);
	    	userObj.setRole(vm.userType);
	    	
	    	userObj.deleteManyToManyAssociations("permission");
	    	List<Permission> permissionList = Permission.getAllPermission();
	    	   if(vm.userType.equals("General Manager")) {
	    		   userObj.permission.addAll(permissionList);
	    	   }
	    	   
	    	   if(vm.userType.equals("Sales Person")) {
	    		   List<Permission> permissionData = new ArrayList<>();
	    		   for(Permission obj: permissionList) {
	    			   if(!obj.name.equals("Home Page Editing") && !obj.name.equals("Blogs") && !obj.name.equals("My Profile") && !obj.name.equals("Account Settings")) {
	    				   permissionData.add(obj);
	    			   }
	    		   }
	    		   userObj.permission.addAll(permissionData);
	    	   }
	    	  
	    	  
	    	   MultipartFormData body = request().body().asMultipartFormData();
	    	   File file1 = new File(rootDir+userObj.imageUrl);
	    	   file1.delete();
	    		FilePart picture = body.getFile("file0");
		    	  if (picture != null) {
		    	    String fileName = picture.getFilename();
		    	    String contentType = picture.getContentType(); 
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
	    	
	    	userObj.update();
	    	return ok();
    	}    	
    }
    
    public static Result deleteUserById(Integer id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = AuthUser.findById(id);
    		user.delete();
    		return ok();
    	}
    }
    
    public static Result setVehicleAndScheduleStatus(String vin) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		AuthUser userObj = AuthUser.findById(userId);
    		Vehicle vehicle = Vehicle.findByVidAndUser(vin);
    		vehicle.setStatus("Sold");
    		Date date = new Date();
    		vehicle.setSoldDate(date);
    		vehicle.update();
    		List<ScheduleTest> scheduleList = ScheduleTest.findByVinAndAssignedUser(user, vin);
    		for(ScheduleTest obj: scheduleList) {
    			obj.setLeadStatus("SUCCESSFUL");
    			obj.update();
    		}
    		return ok();
    	}
    }
    
    public static Result setScheduleStatusClose(Long id,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		ScheduleTest schedule = ScheduleTest.findById(id);
    		schedule.setLeadStatus("FAILED");
    		schedule.setReason(reason);
    		schedule.update();
    		return ok();
    	}
    }
    
    public static Result setRequestStatusComplete(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		RequestMoreInfo info = RequestMoreInfo.findById(id);
    		Vehicle vehicle = Vehicle.findByVin(info.vin);
    		vehicle.setStatus("Sold");
    		Date date = new Date();
    		vehicle.setSoldDate(date);
    		vehicle.update();
    		info.setStatus("COMPLETE");
    		info.update();
    		return ok();
    	}
    }
    
    public static Result setRequestStatusCancel(Long id,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		RequestMoreInfo info = RequestMoreInfo.findById(id);
    		info.setStatus("CANCEL");
    		info.setReason(reason);
    		info.update();
    		return ok();
    	}
    }
    
    public static Result setTradeInStatusComplete(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		TradeIn info = TradeIn.findById(id);
    		Vehicle vehicle = Vehicle.findByVin(info.vin);
    		vehicle.setStatus("Sold");
    		Date date = new Date();
    		vehicle.setSoldDate(date);
    		vehicle.update();
    		info.setStatus("COMPLETE");
    		info.update();
    		return ok();
    	}
    }
    
    public static Result setTradeInStatusCancel(Long id,String reason) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		TradeIn info = TradeIn.findById(id);
    		info.setStatus("CANCEL");
    		info.setReason(reason);
    		info.update();
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
	    		AuthUser userObj = AuthUser.findById(userId);
	    		Vehicle vehicle = Vehicle.findByVinAndStatus(test.vin);
	    		if(vehicle != null) {
	    			vm.make = vehicle.make;
	    			vm.model = vehicle.model;
	    		}
	    		vm.name = test.name;
	    		vm.email = test.email;
	    		vm.phone = test.phone;
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
    		AuthUser user = getLocalUser();
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
    		toDo.save();
    		
    		return ok();
    	}
    }
    
    public static Result getToDoList() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
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
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result saveCompleteTodoStatus(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
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
    		AuthUser user = getLocalUser();
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
    		AuthUser user = getLocalUser();
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
    
    public static Result getSalesUser() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		List<AuthUser> SalesUserList = AuthUser.getAllSalesUser();
    		List<UserVM> vmList = new ArrayList<>();
    		if(user.role != null) {
    			if(user.role.equals("General Manager")) {
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
    
    public static Result getSalesUserOnly() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		List<AuthUser> SalesUserList = AuthUser.getAllSalesUser();
    		List<UserVM> vmList = new ArrayList<>();
    		
    		for(AuthUser obj: SalesUserList) {
    			UserVM vm = new UserVM();
    			vm.fullName = obj.firstName+" "+obj.lastName;
    			vm.id = obj.id;
    			vmList.add(vm);
    		}
    		return ok(Json.toJson(vmList));
    	}
    }
    
    public static Result getAllSalesPersonScheduleTestAssigned(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = AuthUser.findById(id);
	    	List<ScheduleTest> listData = ScheduleTest.findAllAssigned(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	Calendar time = Calendar.getInstance();
	    	for(ScheduleTest info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		AuthUser userObj = AuthUser.findById(userId);
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
	    		vm.bestDay = info.bestDay;
	    		vm.bestTime = info.bestTime;
	    		vm.note = info.note;
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
	    		vm.requestDate = df.format(info.scheduleDate);
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
    
    
    public static Result getAllSalesPersonRequestInfoSeen(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = AuthUser.findById(id);
	    	List<RequestMoreInfo> listData = RequestMoreInfo.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	for(RequestMoreInfo info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		AuthUser userObj = AuthUser.findById(userId);
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.name;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.note = info.note;
	    		vm.requestDate = df.format(info.requestDate);
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
    
    
    public static Result getAllSalesPersonTradeInSeen(Integer id){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = AuthUser.findById(id);
	    	List<TradeIn> listData = TradeIn.findAllSeen(user);
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	for(TradeIn info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		AuthUser userObj = AuthUser.findById(userId);
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.firstName+" "+info.lastName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.note = info.note;
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
    
    public static Result getPerformanceOfUser(String top,String worst,String week,String month,String year,Integer id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		Date date = new Date();
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(date);
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
    		String start = "";
			String end = "";
    		if(week.equals("true")) {
    			cal.add(Calendar.DATE, -7);
    			start = df.format(cal.getTime());
    			end = df.format(date);
			}
			
			if(month.equals("true")) {
				cal.add(Calendar.DATE, -30);
				start = df.format(cal.getTime());
    			end = df.format(date);
			}
			
			if(year.equals("true")) {
				cal.add(Calendar.DATE, -365);
				start = df.format(cal.getTime());
    			end = df.format(date);
			}
			
			List<UserVM> userList = new ArrayList<>();
    		if(top.equals("true")) {
    			if(id == 0) {
    				List<AuthUser> salesUsersList = AuthUser.getAllSalesUser();
    				UserVM[] tempuserList = new UserVM[salesUsersList.size()];
    				int index=0;
    				for(AuthUser sales: salesUsersList) {
    					SqlRow rowData = ScheduleTest.getTopPerformers(start, end, sales.id);
        				UserVM vm = new UserVM();
        				vm.fullName = sales.firstName+" "+sales.lastName;
        				if(sales.imageUrl != null) {
        					vm.imageUrl = sales.imageUrl;
        				} else {
        					vm.imageUrl = "/profile-pic.jpg";
        				}
        				if(rowData.getInteger("success") != null && rowData.getInteger("total") != null && rowData.getInteger("total") != 0) {
        					vm.successRate = rowData.getInteger("success")*(100/rowData.getInteger("total"));
        				} else {
        					vm.successRate = 0;
        				}
        				if(rowData.getString("leads") != null) {
        					vm.currentLeads = rowData.getString("leads");
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
    						if(tempuserList[i].successRate <= tempuserList[j].successRate) {
    							UserVM temp = tempuserList[i];
    							tempuserList[i] = tempuserList[j];
    							tempuserList[j] = temp;
    						}
    					}
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
    					vm.imageUrl = salesUser.imageUrl;
    				} else {
    					vm.imageUrl = "/profile-pic.jpg";
    				}
    				if(rowData.getInteger("success") != null && rowData.getInteger("total") != null && rowData.getInteger("total") != 0) {
    					vm.successRate = rowData.getInteger("success")*(100/rowData.getInteger("total"));
    				} else {
    					vm.successRate = 0;
    				}
    				if(rowData.getString("leads") != null) {
    					vm.currentLeads = rowData.getString("leads");
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
    		}
    		
    		if(worst.equals("true")) {
    			if(id == 0) {
    				List<AuthUser> salesUsersList = AuthUser.getAllSalesUser();
    				UserVM[] tempuserList = new UserVM[salesUsersList.size()];
    				int index = 0;
    				for(AuthUser sales: salesUsersList) {
    					SqlRow rowData = ScheduleTest.getTopPerformers(start, end, sales.id);
        				UserVM vm = new UserVM();
        				vm.fullName = sales.firstName+" "+sales.lastName;
        				if(sales.imageUrl != null) {
        					vm.imageUrl = sales.imageUrl;
        				} else {
        					vm.imageUrl = "/profile-pic.jpg";
        				}
        				if(rowData.getInteger("success") != null && rowData.getInteger("total") != null && rowData.getInteger("total") != 0) {
        					vm.successRate = rowData.getInteger("success")*(100/rowData.getInteger("total"));
        				} else {
        					vm.successRate = 0;
        				}
        				if(rowData.getString("leads") != null) {
        					vm.currentLeads = rowData.getString("leads");
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
    					vm.imageUrl = salesUser.imageUrl;
    				} else {
    					vm.imageUrl = "/profile-pic.jpg";
    				}
    				if(rowData.getInteger("success") != null && rowData.getInteger("total") != null && rowData.getInteger("total") != 0) {
    					vm.successRate = rowData.getInteger("success")*(100/rowData.getInteger("total"));
    				} else {
    					vm.successRate = 0;
    				}
    				if(rowData.getString("leads") != null) {
    					vm.currentLeads = rowData.getString("leads");
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
    		}
    		
    		
    		return ok(Json.toJson(userList));
    	}
    }
    
    public static Result saveNoteOfUser(Long id,String type,String note) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		if(type.equals("requestMore")) {
    			RequestMoreInfo requestMore = RequestMoreInfo.findById(id);
    			requestMore.setNote(note);
    			requestMore.update();
    		}
    		if(type.equals("scheduleTest")) {
    			ScheduleTest scheduleTest = ScheduleTest.findById(id);
    			scheduleTest.setNote(note);
    			scheduleTest.update();
    		}
    		if(type.equals("tradeIn")) {
    			TradeIn tradeIn = TradeIn.findById(id);
    			tradeIn.setNote(note);
    			tradeIn.update();
    		}
    		return ok();
    	}
    }
    
    public static Result saveTestDrive() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = getLocalUser();
    		Form<RequestInfoVM> form = DynamicForm.form(RequestInfoVM.class).bindFromRequest();
    		RequestInfoVM vm = form.get();
    		ScheduleTest scheduleTest = new ScheduleTest();
    		scheduleTest.name = vm.name;
    		scheduleTest.email = vm.email;
    		scheduleTest.phone = vm.phone;
    		scheduleTest.bestDay = vm.bestDay;
    		scheduleTest.bestTime = vm.bestTime;
    		scheduleTest.preferredContact = vm.prefferedContact;
    		scheduleTest.vin = vm.vin;
    		scheduleTest.scheduleDate = new Date();
    		scheduleTest.user = user;
    		scheduleTest.save();
    		return ok();
    	}
    }
    
    public static Result getAllCanceledLeads() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser user = (AuthUser) getLocalUser();
	    	List<ScheduleTest> listData = ScheduleTest.getAllFailed();
    		
	    	List<RequestInfoVM> infoVMList = new ArrayList<>();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    	Calendar time = Calendar.getInstance();
	    	for(ScheduleTest info: listData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
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
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		vm.requestDate = df.format(info.scheduleDate);
	    		vm.leadType = "Schedule Test";
	    		infoVMList.add(vm);
	    	}
	    	
	    	List<RequestMoreInfo> requestData = RequestMoreInfo.findAllCancel();
	    	for(RequestMoreInfo info: requestData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
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
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		vm.leadType = "Request More Info";
	    		infoVMList.add(vm);
	    	}
	    	
	    	List<TradeIn> tradeInData = TradeIn.findAllCanceled();
	    	for(TradeIn info: tradeInData) {
	    		RequestInfoVM vm = new RequestInfoVM();
	    		vm.id = info.id;
	    		Vehicle vehicle = Vehicle.findByVidAndUser(info.vin);
	    		vm.vin = info.vin;
	    		if(vehicle != null) {
	    			vm.model = vehicle.model;
	    			vm.make = vehicle.make;
	    			vm.stock = vehicle.stock;
	    		}
	    		vm.name = info.firstName+" "+info.lastName;
	    		vm.phone = info.phone;
	    		vm.email = info.email;
	    		vm.status = info.reason;
	    		if(info.assignedTo != null) {
	    			vm.salesRep = info.assignedTo.getFirstName()+" "+info.assignedTo.getLastName();
	    		}
	    		vm.leadType = "Trade In";
	    		infoVMList.add(vm);
	    	}
	    	
	    	return ok(Json.toJson(infoVMList));
    	}
    }
    
    public static Result changeAssignedUser(Long id,Integer user,String leadType) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
    		AuthUser userObj = AuthUser.findById(user);
    		if(leadType.equals("Schedule Test")) {
    			ScheduleTest schedule = ScheduleTest.findById(id);
    			schedule.setAssignedTo(userObj);
    			schedule.setLeadStatus(null);
    			schedule.update();
    		}
			if(leadType.equals("Request More Info")) {
			    RequestMoreInfo info = RequestMoreInfo.findById(id);
			    info.setAssignedTo(userObj);
			    info.setStatus(null);
			    info.update();
			}
			if(leadType.equals("Trade In")) {
				TradeIn tradeIn = TradeIn.findById(id);
				tradeIn.setAssignedTo(userObj);
				tradeIn.setStatus(null);
				tradeIn.update();
			}
    		return ok();
    	}
    }
    
}

