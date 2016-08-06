package controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import au.com.bytecode.opencsv.CSVWriter;

import com.avaje.ebean.Ebean;
import com.mnt.dataone.Equipment;
import com.mnt.dataone.InstalledEquipment;
import com.mnt.dataone.Option;
import com.mnt.dataone.OptionalEquipment;
import com.mnt.dataone.ResponseData;
import com.mnt.dataone.Specification;
import com.mnt.dataone.Specification_;
import com.mnt.dataone.Value;

import models.AuthUser;
import models.CustomizationInventory;
import models.EmailDetails;
import models.FollowBrand;
import models.Location;
import models.MyProfile;
import models.PriceAlert;
import models.PriceChange;
import models.RequestMoreInfo;
import models.ScheduleTest;
import models.Site;
import models.SiteLogo;
import models.TradeIn;
import models.UserNotes;
import models.Vehicle;
import models.VehicleAudio;
import models.VehicleImage;
import models.Video;
import models.VirtualTour;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import securesocial.core.Identity;

import viewmodel.AudioVM;
import viewmodel.ImageVM;
import viewmodel.KeyValueDataVM;
import viewmodel.PinVM;
import viewmodel.PriceChangeVM;
import viewmodel.SpecificationVM;
import viewmodel.VehicleVM;
import viewmodel.VideoVM;
import viewmodel.VirtualTourVM;
import views.html.home;

public class AddVehicleController extends Controller{

	
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
	
	public static Result getMakeList(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<VehicleVM> makeList = new ArrayList<>();
    		List<VehicleVM> labelList = new ArrayList<>();
    		List<VehicleVM> madeInList = new ArrayList<>();
    		List<VehicleVM> exteriorColoList = new ArrayList<>();
    		List<VehicleVM> stereoList = new ArrayList<>();
    		List<VehicleVM> driveTypeList = new ArrayList<>();
    		List<VehicleVM> fuelTypeList = new ArrayList<>();
    		
    		List<Vehicle> list = Vehicle.getMakeList();
    		
    		Map<String, Vehicle> mapMake = new HashMap<>();
    		Map<String, Vehicle> mapLabel = new HashMap<>();
    		Map<String, Vehicle> mapMadeIn = new HashMap<>();
    		Map<String, Vehicle> mapExteriorColor = new HashMap<>();
    		Map<String, Vehicle> mapStereo = new HashMap<>();
    		Map<String, Vehicle> mapDriveType = new HashMap<>();
    		Map<String, Vehicle> mapFuelType = new HashMap<>();
    		
    		Map<String, Object> mapList = new HashMap<>();
    		mapList.put("make", makeList);
    		mapList.put("label", labelList);
    		mapList.put("madeIn", madeInList);
    		mapList.put("stereo", stereoList);
    		mapList.put("driveType", driveTypeList);
    		mapList.put("fuelType", fuelTypeList);
    		mapList.put("exteriorColor", exteriorColoList);
    		
    		for (Vehicle vehicle : list) {
    			if(vehicle.getMake() != null){
    				Vehicle objectMake = mapMake.get(vehicle.getMake());
    					mapMake.put(vehicle.getMake(), vehicle);
    			}
    			if(vehicle.getLabel() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getLabel());
    				mapLabel.put(vehicle.getLabel(), vehicle);
    			}
    			if(vehicle.getMadeIn() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getMadeIn());
    				mapMadeIn.put(vehicle.getMadeIn(), vehicle);
    			}
    			if(vehicle.getStereo() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getStereo());
    				mapStereo.put(vehicle.getStereo(), vehicle);
    			}
    			if(vehicle.getDrivetrain() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getDrivetrain());
    				mapDriveType.put(vehicle.getDrivetrain(), vehicle);
    			}
    			if(vehicle.getFuelType() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getFuelType());
    				mapFuelType.put(vehicle.getFuelType(), vehicle);
    			}
    			if(vehicle.getExteriorColor() != null){
    				Vehicle objectMake = mapLabel.get(vehicle.getExteriorColor());
    				mapExteriorColor.put(vehicle.getExteriorColor(), vehicle);
    			}
			}
    		
    		for (Entry<String, Vehicle> value : mapMadeIn.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.madeIn = value.getKey();
    			madeInList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapStereo.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.stereo = value.getKey();
    			stereoList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapDriveType.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.drivetrain = value.getKey();
    			driveTypeList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapFuelType.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.fuelType = value.getKey();
    			fuelTypeList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapExteriorColor.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.extColor = value.getKey();
    			exteriorColoList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapMake.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.make = value.getKey();
    			makeList.add(vm);
    		}
    		for (Entry<String, Vehicle> value : mapLabel.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.label = value.getKey();
    			labelList.add(vm);
    		}
    		return ok(Json.toJson(mapList));
    	}
	}
	
	public static Result getModelList(String make){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<Vehicle> list = Vehicle.findByMake(make);
    		Map<String, Vehicle> mapMake = new HashMap<>();
    		List<VehicleVM> modelList = new ArrayList<>();
    		for (Vehicle vehicle : list) {
    			if(vehicle.getModel() != null){
    				Vehicle objectMake = mapMake.get(vehicle.getModel());
    				mapMake.put(vehicle.getModel(), vehicle);
    			}
			}
    		for (Entry<String, Vehicle> value : mapMake.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.model = value.getKey();
    			modelList.add(vm);
    		}
    		return ok(Json.toJson(modelList));
    	}
	}
	
	public static Result getTrimList(String model){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<Vehicle> list = Vehicle.findByMake(model);
    		Map<String, Vehicle> mapMake = new HashMap<>();
    		List<VehicleVM> trimList = new ArrayList<>();
    		for (Vehicle vehicle : list) {
    			if(vehicle.getTrim() != null){
    				Vehicle objectMake = mapMake.get(vehicle.getTrim());
    				mapMake.put(vehicle.getTrim(), vehicle);
    			}
			}
    		for (Entry<String, Vehicle> value : mapMake.entrySet()) {
    			VehicleVM vm = new VehicleVM();
    			vm.trim = value.getKey();
    			trimList.add(vm);
    		}
    		return ok(Json.toJson(trimList));
    	}
	}	
	
	public static Result checkStockNumber(String stockNumber) {
    	List<Vehicle> vehicles = Vehicle.findByStock(stockNumber,Location.findById(Long.valueOf(session("USER_LOCATION"))));
    	if(vehicles.size() > 0){
        	return ok(Json.toJson(vehicles.size()));
    	}else{
        	return ok(Json.toJson(vehicles.size()));
    	}
    }
	
    public static Result getVehicleInfo(String vin) throws IOException,Exception {
    	
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    public static Result saveVehicle() throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
	    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
	    	SpecificationVM vm = form.get();
	    	
	    	Vehicle vehicleObj = Vehicle.findByVinAndStatus(vm.vin);
	    	Vehicle vehicle = new Vehicle();
	    	if(vehicleObj == null) {
	    		
	    		/*if(body != null){
		    		FilePart picture = body.getFile("file0");
		    		if (picture != null) {
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File file = picture.getFile();
		    			try {
		    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+vm.vin+"-"+userObj.id+File.separator+"PDF_brochure");
		    	    	    if(!fdir.exists()) {
		    	    	    	fdir.mkdir();
		    	    	    }
		    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+vm.vin+"-"+userObj.id+File.separator+"PDF_brochure"+fileName;
		    	    	    FileUtils.moveFile(file, new File(filePath));
		    	    	    vehicle.pdfBrochureName = fileName;
		    	    	    vehicle.pdfBrochurePath = session("USER_LOCATION")+File.separator+vm.vin+"-"+userObj.id+File.separator+"PDF_brochure"+fileName;
						} catch (Exception e) {
							e.printStackTrace();
						}
		    		}
		    	}*/
	    		int comingSoonFlag=0;
	    		if(vm.collection.equals("Coming Soon"))
	    		{
	    			vehicle.comingSoonFlag=1;
	    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    			try {
						vehicle.comingSoonDate=formatter.parse(vm.comingSoonDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			
	    		}else{
	    			vehicle.comingSoonFlag=0;
	    		}
		    	
	    		vehicle.setTitle(vm.make+" "+vm.model+" "+vm.year);
		    	vehicle.category = vm.category;
		    	vehicle.vin = vm.vin;
		    	if(vm.collection.equals("New") || vm.collection.equals("Used")){
		    		vehicle.typeofVehicle=vm.collection;
		    	}
		    	vehicle.publicStatus = "draft";
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
		    	vehicle.engineType = vm.engine;
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
				
				vehicle.collection = vm.collection;
				
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
	    	
	    	saveCustomInventoryData(vehicle.id,vm);
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
	    	
	    	return ok(Json.toJson(vehicle.id));
    	}	
    }
    
    public static Result saveVehiclePdf(Long id) throws IOException {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Identity user = getLocalUser();
	    	AuthUser userObj = (AuthUser)user;
    		MultipartFormData body = request().body().asMultipartFormData();
    		Vehicle vehicle = Vehicle.findById(id);
    		if(vehicle != null) {
    			if(body != null){
		    		FilePart picture = body.getFile("file0");
		    		if (picture != null) {
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File file = picture.getFile();
		    			try {
		    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"PDF_brochure");
		    	    	    if(!fdir.exists()) {
		    	    	    	fdir.mkdir();
		    	    	    }
		    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"PDF_brochure"+fileName;
		    	    	    FileUtils.moveFile(file, new File(filePath));
		    	    	    vehicle.setPdfBrochureName(fileName);
		    	    	    vehicle.setPdfBrochurePath(session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"PDF_brochure"+fileName);
		    	    	    vehicle.update();
		    			} catch (Exception e) {
							e.printStackTrace();
						}
		    		}
		    	}
    		}
    	}
    	return ok();
    }
    
    public static Result addPublicCar(Long id){
    	
    	Vehicle vehicle= Vehicle.findById(id);
    	if(vehicle != null){
    		vehicle.setPublicStatus("public");
        	vehicle.update();
    	}
    	return ok();
    }
    
    public static Result getVehicleById(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
			specificationVM.fileName = vehicle.getPdfBrochureName();
			specificationVM.standardSeating = vehicle.getStandardSeating();
			specificationVM.collection = vehicle.getCollection();
			
			
			
			findCustomeInventoryData(vehicle.id,specificationVM);
			
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if(vehicle.getComingSoonFlag() != null && vehicle.getComingSoonFlag() == 1){
			String dat=df.format(vehicle.getComingSoonDate());
			specificationVM.comingSoonDate=dat;
			specificationVM.comingSoonFlag=vehicle.comingSoonFlag;
			}
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
    
    public static Result getPriceHistory(String vin){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    			
    		
    	}
    	inventoryvm.customData = keyValueList;
    	inventoryvm.customMapData = mapCar;
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
 
 public static AuthUser getLocalUser() {
 	String id = session("USER_KEY");
 	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
 	//AuthUser user = getLocalUser();
		return user;
	}
 
 private static void saveCustomInventoryData(Long InventoryId,SpecificationVM vm) {
    	
    	for(KeyValueDataVM custom:vm.customData){
    		
    		CustomizationInventory cDataValue = CustomizationInventory.findByKeyAndLeadId(custom.key,InventoryId);
    		if(cDataValue == null){
    			CustomizationInventory cValue = new CustomizationInventory();
    			cValue.keyValue = custom.key;
    			cValue.value = custom.value;
    			cValue.InventoryId = InventoryId;
    			
 			
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
    			cDataValue.setDisplayGrid(custom.displayGrid);
    			cDataValue.update();
    		}
			
		}
    }
 
 		public static Result getImagesByVin(String vin) {
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
 		
 		 public static Result removeVehiclePdf(Long id) {
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
 	    	} else {
 	    		Vehicle vehicle = Vehicle.findById(id);
 	    		if(vehicle.getPdfBrochurePath() != null){
 	    			String filePath = rootDir+File.separator+vehicle.getPdfBrochurePath();
 	        		File file = new File(filePath);
 	        		try {
 	        			file.delete();
 	    			} catch (Exception e) {
 	    				e.printStackTrace();
 	    			}
 	        		vehicle.setPdfBrochureName(null);
 	        		vehicle.setPdfBrochurePath(null);
 	        		vehicle.update();
 	    		}
 	    		return ok();
 	    	}
 	    }
 		 
 		public static Result updateVehicleById() throws SocketException, IOException{
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
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
 		    		}
 		    		
 		    		int comingSoonFlag=0;
 		    		if(vm.comingSoonDate != "" && vm.comingSoonDate != null){
 		    			vehicle.setComingSoonFlag(1);
 		    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
 		    			try {
 							vehicle.setComingSoonDate(formatter.parse(vm.comingSoonDate));
 						} catch (Exception e) {
 							// TODO Auto-generated catch block
 							e.printStackTrace();
 						}
 		    			
 		    		}
 		    		else{
 		    			vehicle.setComingSoonFlag(0);
 		    		}
 			    	
 		    		if(vm.collection.equals("New") || vm.collection.equals("Used")){
 		    			vehicle.setTypeofVehicle(vm.collection);
 		    		}
 		    		
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
 			    	vehicle.setEngineType(vm.engine);
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
 			    	if(vm.collection.equals("Coming Soon")){
 			    		vehicle.setComingSoonFlag(1);
 			    		vehicle.setTypeofVehicle(null);
 			    	}
 			    	else{
 			    		vehicle.setComingSoonFlag(0);
 			    	}
 			    	vehicle.setStandardSeating(vm.standardSeating);
 					
 			    	vehicle.setMileage(vm.mileage);
 			    	vehicle.setMadeIn(vm.made_in);
 			    	vehicle.setOptionalSeating(vm.optional_seating);
 			    	vehicle.setCollection(vm.collection);
 			    	
 			    	List<Site> siteList = new ArrayList<>();
 			    	if(vm.siteIds != null) {
 				    	for(Long obj: vm.siteIds) {
 				    		Site siteObj = Site.findById(obj);
 				    		siteList.add(siteObj);
 				    	}
 				    	vehicle.setSite(siteList);
 			    	}
 			    	vehicle.update();
 			    	
 			    	saveCustomInventoryData(vehicle.id,vm);
 			    	
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
 		
 		public static Result updateVehicleByIdPdf(Long id) throws SocketException, IOException{
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
 	    	} else {
 	    		
 	    		AuthUser userObj = (AuthUser) getLocalUser();
 		    	MultipartFormData body = request().body().asMultipartFormData();
 		    	Vehicle vehicle = Vehicle.findById(id);
 		    	if(vehicle != null) {
 		    		
 		    		if(body != null){
 			    		FilePart picture = body.getFile("file0");
 			    		if (picture != null) {
 			    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
 			    			File file = picture.getFile();
 			    			try {
 			    				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"PDF_brochure");
 			    	    	    if(!fdir.exists()) {
 			    	    	    	fdir.mkdir();
 			    	    	    }
 			    	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"PDF_brochure"+fileName;
 			    	    	    FileUtils.moveFile(file, new File(filePath));
 			    	    	    vehicle.setPdfBrochureName(fileName);
 			    	    	    vehicle.setPdfBrochurePath(session("USER_LOCATION")+File.separator+vehicle.vin+"-"+userObj.id+File.separator+"PDF_brochure"+fileName);
 			    	    	    vehicle.update();
 							} catch (Exception e) {
 								e.printStackTrace();
 							}
 			    		}
 			    	}
 		    	}	
 	    		
 	    		return ok();
 	    	}
 	    }
 		
 		 public static Result isCarPublic(Long id){
 	    	
 	    	Vehicle vehicle= Vehicle.findById(id);
 	    	if(vehicle != null && vehicle.getPublicStatus().equals("public")){
 	    		return ok(Json.toJson(false));
 	    	}else{
 	    		return ok(Json.toJson(true));    		
 	    	}
 	    }
 		 
 		 public static Result removeDefault(Long old,Long newId) {
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
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
 		 
 		public static Result setDefaultImage(Long id) {
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
 	    	} else {
 		    	VehicleImage image = VehicleImage.findById(id);
 		    	image.defaultImage = (true);
 		    	image.update();
 		    	return ok();
 	    	}
 	    }
 		
 		 public static Result deleteImage(Long id) {
 		    //	AuthUser user = (AuthUser) getLocalUser();
 		    	VehicleImage image = VehicleImage.findById(id);
 		    	File file = new File(rootDir+File.separator+image.path);
 		    	File thumbFile = new File(rootDir+File.separator+image.thumbPath);
 		    	file.delete();
 		    	thumbFile.delete();
 		    	image.delete();
 		    	return ok();
 	    }
 		 
 		public static Result updateVehicleStatus(Long id,String status){
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
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
 		
 		 public static Result deleteVehicleById(Long id ){
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
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
 		 
 		 public static Result deleteAudioFile(Long id) {
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
 	    	} else {
 		    	VehicleAudio audio = VehicleAudio.findById(id);
 		    	File file = new File(rootDir+audio.path);
 		    	file.delete();
 		    	audio.delete();
 		    	return ok();
 	    	}	
 	    }
 		 
 		 public static Result getAllAudio(String vin) {
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
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
 		 
 		public static Result getVirtualTour(Long vid) {
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
 	    	} else {
 		    	AuthUser user = (AuthUser) getLocalUser();
 		    	Vehicle vehicle = Vehicle.findById(vid);
 		    	VirtualTourVM vtVM = new VirtualTourVM();
 		    	VideoVM vVM = new VideoVM();
 		    	Map<String, Object> map = new HashMap<String, Object>();
 		    	map.put("video", vVM);
 		    	map.put("virtualTour", vtVM);
 		    	
 		    	if(vehicle != null){
 		    		VirtualTour virtualTour = VirtualTour.findByUserAndVin(user, vehicle.vin);
 		    		Video video = Video.findByUserAndVin(user, vehicle.vin);
 			    	if(virtualTour != null) {
 			    		vtVM.desktopUrl = virtualTour.desktopUrl;
 			    		vtVM.mobileUrl = virtualTour.mobileUrl;
 			    	}
 			    	if(video != null) {
 			    		vVM.desktopUrl = video.desktopUrl;
 			    		vVM.mobileUrl = video.mobileUrl;
 			    	}
 		    	}
 		    	return ok(Json.toJson(map));
 	    	}	
 	    }
 		
 		public static Result saveVData() {
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
 	    	} else {
 		    	AuthUser user = (AuthUser) getLocalUser();
 		    	Form<VirtualTourVM> form = DynamicForm.form(VirtualTourVM.class).bindFromRequest();
 		    	VirtualTourVM vm = form.get();
 		    	Vehicle vehicle = Vehicle.findById(vm.vehicleId);
 		    	if(vehicle != null){
 		    		VirtualTour virtualTour = VirtualTour.findByUserAndVin(user, vehicle.vin);
 			    	if(virtualTour == null) {
 				    	VirtualTour vt = new VirtualTour();
 				    	if(vm.desktopUrl == null || vm.desktopUrl == ""){
 			    			vt.desktopUrl = null;
 			    		}else{
 			    			vt.desktopUrl = vm.desktopUrl;
 			    		}
 			    		if(vm.mobileUrl == null || vm.mobileUrl == ""){
 			    			vt.mobileUrl = null;
 			    		}else{
 			    			vt.mobileUrl = vm.mobileUrl;
 			    		}
 				    	vt.vin = vehicle.vin;
 				    	vt.user = user;
 				    	vt.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
 				    	vt.save();
 			    	} else {
 			    		/*virtualTour.setDesktopUrl(vm.desktopUrl);
 			    		virtualTour.setMobileUrl(vm.mobileUrl);*/
 			    		if(vm.desktopUrl == null || vm.desktopUrl == ""){
 			    			virtualTour.setDesktopUrl(null);
 			    		}else{
 			    			virtualTour.setDesktopUrl(vm.desktopUrl);
 			    		}
 			    		if(vm.mobileUrl == null || vm.mobileUrl == ""){
 			    			virtualTour.setMobileUrl(null);
 			    		}else{
 			    			virtualTour.setMobileUrl(vm.mobileUrl);
 			    		}
 			    		virtualTour.update();
 			    	}
 		    	}
 		    	return ok();
 	    	}	
 	    }
 		
 		public static Result saveVideoData() {
 	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
 	    		return ok(home.render("",userRegistration));
 	    	} else {
 		    	AuthUser user = (AuthUser) getLocalUser();
 		    	Form<VideoVM> form = DynamicForm.form(VideoVM.class).bindFromRequest();
 		    	VideoVM vm = form.get();
 		    	Vehicle vehicle = Vehicle.findById(vm.vehicleId);
 		    	if(vehicle != null){
 		    		Video virtualTour = Video.findByUserAndVin(user, vehicle.vin);
 			    	
 			    	if(virtualTour == null) {
 			    		Video vt = new Video();
 			    		if(vm.desktopUrl == null || vm.desktopUrl == ""){
 			    			vt.desktopUrl = null;
 			    		}else{
 			    			vt.desktopUrl = vm.desktopUrl;
 			    		}
 			    		if(vm.mobileUrl == null || vm.mobileUrl == ""){
 			    			vt.mobileUrl = null;
 			    		}else{
 			    			vt.mobileUrl = vm.mobileUrl;
 			    		}
 				    	vt.vin = vehicle.vin;
 				    	vt.user = user;
 				    	vt.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
 				        vt.save();
 				    	
 			    	} else {
 			    		if(vm.desktopUrl == null || vm.desktopUrl == ""){
 			    			virtualTour.setDesktopUrl(null);
 			    		}else{
 			    			virtualTour.setDesktopUrl(vm.desktopUrl);
 			    		}
 			    		if(vm.mobileUrl == null || vm.mobileUrl == ""){
 			    			virtualTour.setMobileUrl(null);
 			    		}else{
 			    			virtualTour.setMobileUrl(vm.mobileUrl);
 			    		}
 			    		virtualTour.update();
 			    	}
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
}