package controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import models.AuthUser;
import models.FeaturedImage;
import models.FeaturedImageConfig;
import models.Site;
import models.SliderImage;
import models.SliderImageConfig;
import models.Vehicle;
import models.VehicleAudio;
import models.VehicleImage;
import models.VirtualTour;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
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
import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;
import viewmodel.AudioVM;
import viewmodel.EditImageVM;
import viewmodel.ImageVM;
import viewmodel.PinVM;
import viewmodel.SiteVM;
import viewmodel.SpecificationVM;
import viewmodel.VirtualTourVM;
import views.html.home;

import au.com.bytecode.opencsv.CSVWriter;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

public class Application extends Controller {
  
	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	
	final static String mashapeKey = Play.application().configuration()
			.getString("mashapeKey");
	
	static String simulatevin = "{    'success': true,    'specification': {        'vin': 'WDDNG7KB7DA494890',        'year': '2013',        'make': 'Mercedes-Benz',        'model': 'S-Class',        'trim_level': 'S65 AMG',        'engine': '6.0L V12 SOHC 36V TURBO',        'style': 'SEDAN 4-DR',        'made_in': 'GERMANY',        'steering_type': 'R&P',        'anti_brake_system': '4-Wheel ABS',        'tank_size': '23.80 gallon',        'overall_height': '58.00 in.',        'overall_length': '206.50 in.',        'overall_width': '73.70 in.',        'standard_seating': '5',        'optional_seating': null,        'highway_mileage': '19 miles/gallon',        'city_mileage': '12 miles/gallon'    },    'vin': 'WDDNG7KB7DA494890'}";

	private static boolean simulate = false;
    /*public static Result index() {
        return ok(index.render("Your new application is ready."));
    }*/
	
	public static Result home() {
		return ok(home.render());
	}
	
	@SecureSocial.SecuredAction
    public static Result index() {
        Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
        //return ok(index.render(user));
        return redirect("/dealer/index.html");
    }
	
	@SecureSocial.SecuredAction(ajaxCall = true)
    public static Result getUserInfo() {
		AuthUser user = (AuthUser)ctx().args.get(SecureSocial.USER_KEY);
		return ok(Json.toJson(user));
	}

    @SecureSocial.UserAwareAction
    public static Result userAware() {
        Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
        final String userName = user != null ? user.fullName() : "guest";
        return ok("Hello " + userName);
    }

    @SecureSocial.SecuredAction(ajaxCall = true)
    public static Result ajaxCall() {
        // return some json
    	return null;
    }
  
    public static Result getAllSites() {
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
    
    @SecureSocial.SecuredAction
    public static Result getVehicleInfo(String vin) throws IOException {
    	
    	Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
    	AuthUser userObj = (AuthUser)user;
    	Vehicle vehicle = Vehicle.findByVidAndUser(vin,userObj); 
    	PinVM pinObj;
		if(vehicle == null) {
    		
    		if(!simulate ) {
    		URL url;
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
			
			pinObj = new ObjectMapper().readValue(sb.toString(), PinVM.class);
	    	} else {
	    		pinObj = new ObjectMapper().readValue(simulatevin, PinVM.class);
	    	}
	    	
			return ok(Json.toJson(pinObj));
			
    	} else {
			PinVM pinVM = new PinVM();
			pinVM.success = true;
			pinVM.vin = vehicle.getVin();
			
			SpecificationVM specificationVM = new SpecificationVM();
			specificationVM.vin = vehicle.getVin();
			specificationVM.year = vehicle.getYear();
			specificationVM.make = vehicle.getMake();
			specificationVM.model = vehicle.getModel();
			specificationVM.trim_level = vehicle.getTrim();
			specificationVM.engine = vehicle.getEngine();
			specificationVM.style = vehicle.getBodyStyle();
			specificationVM.made_in = vehicle.getMadeIn();
			specificationVM.steering_type = vehicle.getSteeringType();
			specificationVM.anti_brake_system = vehicle.getAntiBrakeSystem();
			specificationVM.tank_size = vehicle.getFuel();
			specificationVM.overall_height = vehicle.getHeight();
			specificationVM.overall_length = vehicle.getLength();
			specificationVM.overall_width = vehicle.getWidth();
			specificationVM.standard_seating = vehicle.getStandardSeating();
			specificationVM.optional_seating = vehicle.getOptionalSeating();
			specificationVM.highway_mileage = vehicle.getHighwayMileage();
			specificationVM.city_mileage = vehicle.getCityMileage();
			specificationVM.category = vehicle.getCategory();
			specificationVM.colorDesc = vehicle.getColorDescription();
			specificationVM.cost = vehicle.getCost();
			specificationVM.description = vehicle.getDescription();
			specificationVM.doors = vehicle.getDoors();
			specificationVM.drivetrain = vehicle.getDrivetrain();
			specificationVM.extColor = vehicle.getExteriorColor();
			specificationVM.intColor = vehicle.getInteriorColor();
			specificationVM.label = vehicle.getLabel();
			specificationVM.mileage = vehicle.getMileage();
			specificationVM.price = vehicle.getPrice();
			specificationVM.styleFeatures = vehicle.getStyleFeatures();
			specificationVM.safetyFeatures = vehicle.getSafetyFeatures();
			specificationVM.audioNavFeatures = vehicle.getAudioNavFeatures();
			specificationVM.performanceFeatures = vehicle.getPerformanceFeatures();
			specificationVM.otherCarFeatures = vehicle.getOtherCarFeatures();
			specificationVM.location = vehicle.getLocation();
			specificationVM.stereo = vehicle.getStereo();
			specificationVM.stock = vehicle.getStock();
			specificationVM.transmission = vehicle.getTransmission();
			List<Long> siteIds = new ArrayList<>();
			for(Site site: vehicle.getSite()) {
				siteIds.add(site.id);
			}
			specificationVM.siteIds = siteIds;
			
			pinVM.specification = specificationVM;
			
	    	return ok(Json.toJson(pinVM));
    	}
    }
    
    @SecureSocial.SecuredAction
    public static Result saveVehicle() {
    	Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
    	SpecificationVM vm = form.get();
    	AuthUser userObj = (AuthUser)user;
    	Vehicle vehicleObj = Vehicle.findByVidAndUser(vm.vin,userObj);
    	if(vehicleObj == null) {
	    	Vehicle vehicle = new Vehicle();
	    	vehicle.category = vm.category;
	    	vehicle.vin = vm.vin;
	    	vehicle.year = vm.year;
	    	vehicle.make = vm.make;
	    	vehicle.model = vm.model;
	    	vehicle.trim = vm.trim_level;
	    	vehicle.label = vm.label;
	    	vehicle.stock = vm.stock;
	    	vehicle.mileage = vm.mileage;
	    	vehicle.cost = vm.cost;
	    	vehicle.price = vm.price;
	    	vehicle.exteriorColor = vm.extColor;
	    	vehicle.interiorColor = vm.intColor;
	    	vehicle.colorDescription = vm.colorDesc;
	    	vehicle.doors = vm.doors;
	    	vehicle.stereo = vm.stereo;
	    	vehicle.engine = vm.engine;
	    	vehicle.fuel = vm.tank_size;
	    	vehicle.cityMileage = vm.city_mileage;
	    	vehicle.highwayMileage = vm.highway_mileage;
	    	vehicle.bodyStyle = vm.style;
	    	vehicle.drivetrain = vm.drivetrain;
	    	vehicle.transmission = vm.transmission;
	    	vehicle.styleFeatures = vm.styleFeatures;
	    	vehicle.safetyFeatures = vm.safetyFeatures;
	    	vehicle.audioNavFeatures = vm.audioNavFeatures;
	    	vehicle.performanceFeatures = vm.performanceFeatures;
	    	vehicle.otherCarFeatures = vm.otherCarFeatures;
	    	vehicle.location = vm.location;
	    	vehicle.description = vm.description;
			vehicle.user = (AuthUser)user;
	    	vehicle.madeIn = vm.made_in;
	    	vehicle.steeringType = vm.steering_type;
	    	vehicle.antiBrakeSystem = vm.anti_brake_system;
	    	vehicle.height = vm.overall_height;
	    	vehicle.length = vm.overall_length;
	    	vehicle.width = vm.overall_width;
	    	vehicle.standardSeating = vm.standard_seating;
	    	vehicle.optionalSeating = vm.optional_seating;
	    	vehicle.status = "Newly Listed";
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
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result uploadPhotos() {
    	MultipartFormData body = request().body().asMultipartFormData();
    	String vin = request().getHeader("vinNum");
    	
    	Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
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
    
    @SecureSocial.SecuredAction
    public static Result getImagesByVin(String vin) {
    	Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
    	AuthUser userObj = (AuthUser)user;
    	List<VehicleImage> imageList = VehicleImage.getByVin(vin,userObj);
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
    
    @SecureSocial.SecuredAction
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
    
	@SecureSocial.SecuredAction
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
    
	@SecureSocial.SecuredAction
    public static Result getImageDataById(Long id) throws IOException {
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
	
    @SecureSocial.SecuredAction
    public static Result deleteImage(Long id) {
    	VehicleImage image = VehicleImage.findById(id);
    	File file = new File(rootDir+image.path);
    	File thumbFile = new File(rootDir+image.thumbPath);
    	file.delete();
    	thumbFile.delete();
    	image.delete();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result setDefaultImage(Long id) {
    	VehicleImage image = VehicleImage.findById(id);
    	boolean val = true;
    	image.setDefaultImage(val);
    	image.update();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result removeDefault(Long old,Long newId) {
    	VehicleImage image = VehicleImage.findById(old);
    	boolean val = false;
    	image.setDefaultImage(val);
    	image.update();
    	
    	VehicleImage newImage = VehicleImage.findById(newId);
    	val = true;
    	newImage.setDefaultImage(val);
    	newImage.update();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result savePosition() {
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
    
    @SecureSocial.SecuredAction
	public static Result getAllVehicles() {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
    	
    	//List <Vehicle> vehicleObjList = Vehicle.getAllVehicles();
    	List <Vehicle> vehicleObjList = Vehicle.getAllVehicles(user);
    	
    	ArrayList<SpecificationVM> VMs = new ArrayList<>(); 
     	for(Vehicle vm : vehicleObjList){
     		SpecificationVM vehicle = new SpecificationVM();
     		vehicle.id = vm.id;
	    	vehicle.category = vm.category;
	    	vehicle.vin = vm.vin;
	    	vehicle.year = vm.year;
	    	vehicle.year = vm.make;
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
	    	vehicle.styleFeatures = vm.styleFeatures;
	    	vehicle.safetyFeatures = vm.safetyFeatures;
	    	vehicle.audioNavFeatures = vm.audioNavFeatures;
	    	vehicle.performanceFeatures = vm.performanceFeatures;
	    	vehicle.otherCarFeatures = vm.otherCarFeatures;
	    	vehicle.location = vm.location;
	    	vehicle.description = vm.description;
	    	vehicle.status  =  vm.status;
	    	vehicle.vehicleCnt = VehicleImage.getVehicleImageCountByVIN(vm.vin,user);
	    	VMs.add(vehicle);
    	}
     	
    	return ok(Json.toJson(VMs));
    }
    
    @SecureSocial.SecuredAction
    public static Result deleteVehicleById(Long id ){
    	Vehicle vm = Vehicle.findById(id);
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
    	if(vm != null){
    		List<VehicleImage> v = VehicleImage.getByVin(vm.vin,user);
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
    
    @SecureSocial.SecuredAction
    public static Result updateVehicleStatus(Long id ){
    	Vehicle vm = Vehicle.findById(id);
    	
    	if(vm != null){
    		vm.setStatus("Sold");
    		vm.update();
    	}
    	
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result getVehicleById(Long id) {
    	Vehicle vehicle = Vehicle.findById(id);
    	PinVM pinVM = new PinVM();
		pinVM.success = true;
		pinVM.vin = vehicle.getVin();
		
		SpecificationVM specificationVM = new SpecificationVM();
		specificationVM.id = vehicle.getId();
		specificationVM.vin = vehicle.getVin();
		specificationVM.year = vehicle.getYear();
		specificationVM.make = vehicle.getMake();
		specificationVM.model = vehicle.getModel();
		specificationVM.trim_level = vehicle.getTrim();
		specificationVM.engine = vehicle.getEngine();
		specificationVM.style = vehicle.getBodyStyle();
		specificationVM.made_in = vehicle.getMadeIn();
		specificationVM.steering_type = vehicle.getSteeringType();
		specificationVM.anti_brake_system = vehicle.getAntiBrakeSystem();
		specificationVM.tank_size = vehicle.getFuel();
		specificationVM.overall_height = vehicle.getHeight();
		specificationVM.overall_length = vehicle.getLength();
		specificationVM.overall_width = vehicle.getWidth();
		specificationVM.standard_seating = vehicle.getStandardSeating();
		specificationVM.optional_seating = vehicle.getOptionalSeating();
		specificationVM.highway_mileage = vehicle.getHighwayMileage();
		specificationVM.city_mileage = vehicle.getCityMileage();
		specificationVM.category = vehicle.getCategory();
		specificationVM.colorDesc = vehicle.getColorDescription();
		specificationVM.cost = vehicle.getCost();
		specificationVM.description = vehicle.getDescription();
		specificationVM.doors = vehicle.getDoors();
		specificationVM.drivetrain = vehicle.getDrivetrain();
		specificationVM.extColor = vehicle.getExteriorColor();
		specificationVM.intColor = vehicle.getInteriorColor();
		specificationVM.label = vehicle.getLabel();
		specificationVM.mileage = vehicle.getMileage();
		specificationVM.price = vehicle.getPrice();
		specificationVM.styleFeatures = vehicle.styleFeatures;
		specificationVM.safetyFeatures = vehicle.safetyFeatures;
		specificationVM.audioNavFeatures = vehicle.audioNavFeatures;
		specificationVM.performanceFeatures = vehicle.performanceFeatures;
		specificationVM.otherCarFeatures = vehicle.otherCarFeatures;
		specificationVM.location = vehicle.location;
		specificationVM.stereo = vehicle.getStereo();
		specificationVM.stock = vehicle.getStock();
		specificationVM.transmission = vehicle.getTransmission();
		List<Long> siteIds = new ArrayList<>();
		for(Site site: vehicle.getSite()) {
			siteIds.add(site.id);
		}
		specificationVM.siteIds = siteIds;
		
		pinVM.specification = specificationVM;
		
    	return ok(Json.toJson(pinVM));
    }
    
    @SecureSocial.SecuredAction
    public static Result updateVehicle(){
    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
    	SpecificationVM vm = form.get();
    	System.out.println(vm.id);
    	Vehicle vehicle = Vehicle.findById(vm.id);
    	if(vehicle != null) {
	    	vehicle.setStock(vm.stock);
	    	vehicle.setPrice(vm.price);
	    	
	    	vehicle.update();
    	}
    	return ok();
    } 
    
    @SecureSocial.SecuredAction
    public static Result updateVehicleById(){
    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
    	SpecificationVM vm = form.get();
    	Vehicle vehicle = Vehicle.findById(vm.id);
    	if(vehicle != null) {
	    	vehicle.setCategory(vm.category);
	    	vehicle.setYear(vm.year);
	    	vehicle.setMake(vm.make);
	    	vehicle.setModel(vm.model);
	    	vehicle.setTrim(vm.trim_level);
	    	vehicle.setLabel(vm.label);
	    	vehicle.setStock(vm.stock);
	    	vehicle.setMileage(vm.mileage);
	    	vehicle.setCost(vm.cost);
	    	vehicle.setPrice(vm.price);
	    	vehicle.setExteriorColor(vm.extColor);
	    	vehicle.setInteriorColor(vm.intColor);
	    	vehicle.setColorDescription(vm.colorDesc);
	    	vehicle.setDoors(vm.doors);
	    	vehicle.setStereo(vm.stereo);
	    	vehicle.setEngine(vm.engine);
	    	vehicle.setFuel(vm.tank_size);
	    	vehicle.setCityMileage(vm.city_mileage);
	    	vehicle.setHighwayMileage(vm.highway_mileage);
	    	vehicle.setBodyStyle(vm.style);
	    	vehicle.setDrivetrain(vm.drivetrain);
	    	vehicle.setTransmission(vm.transmission);
	    	vehicle.setStyleFeatures(vm.styleFeatures);
	    	vehicle.setSafetyFeatures(vm.safetyFeatures);
	    	vehicle.setAudioNavFeatures(vm.audioNavFeatures);
	    	vehicle.setPerformanceFeatures(vm.performanceFeatures);
	    	vehicle.setOtherCarFeatures(vm.otherCarFeatures);
	    	vehicle.setLocation(vm.location);
	    	vehicle.setDescription(vm.description);
	    	vehicle.setMadeIn(vm.made_in);
	    	vehicle.setSteeringType(vm.steering_type);
	    	vehicle.setAntiBrakeSystem(vm.anti_brake_system);
	    	vehicle.setHeight(vm.overall_height);
	    	vehicle.setLength(vm.overall_length);
	    	vehicle.setWidth(vm.overall_width);
	    	vehicle.setStandardSeating(vm.standard_seating);
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
    	}	
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result uplaodSoundFile() {
    	MultipartFormData body = request().body().asMultipartFormData();
    	DynamicForm dynamicForm = Form.form().bindFromRequest();
		String vin = dynamicForm.get("vinNum");
    	
    	Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
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
	
    @SecureSocial.SecuredAction
    public static Result getAllAudio(String vin) {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
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
    
    @SecureSocial.SecuredAction
    public static Result saveVData() {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
    	Form<VirtualTourVM> form = DynamicForm.form(VirtualTourVM.class).bindFromRequest();
    	VirtualTourVM vm = form.get();
    	VirtualTour vt = new VirtualTour();
    	vt.desktopUrl = vm.desktopUrl;
    	vt.mobileUrl = vm.mobileUrl;
    	vt.vin = vm.vin;
    	vt.user = user;
    	vt.save();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result editImage() throws IOException {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
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
    
    @SecureSocial.SecuredAction
    public static Result uploadSliderPhotos() {
    	MultipartFormData body = request().body().asMultipartFormData();
    	
    	Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
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
				SliderImage vImage = new SliderImage();
				vImage.imgName = fileName;
				vImage.path = "/"+userObj.id+"/"+"SliderImages"+"/"+fileName;
				vImage.thumbPath = "/"+userObj.id+"/"+"SliderImages"+"/"+"thumbnail_"+fileName;
				vImage.user = userObj;
				vImage.save();
				
				SliderImageConfig config = SliderImageConfig.findByUser(userObj);
				
				if(config == null) {
					SliderImageConfig configObj = new SliderImageConfig();
					configObj.cropHeight = 840;
					configObj.cropWidth = 1400;
					configObj.user = userObj;
					configObj.save();
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
    
    @SecureSocial.SecuredAction
    public static Result uploadFeaturedPhotos() {
    	MultipartFormData body = request().body().asMultipartFormData();
    	
    	Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
    	AuthUser userObj = (AuthUser)user;
    	
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
				
				FeaturedImageConfig config = FeaturedImageConfig.findByUser(userObj);
				
				if(config == null) {
					FeaturedImageConfig configObj = new FeaturedImageConfig();
					configObj.cropHeight = 840;
					configObj.cropWidth = 1400;
					configObj.user = userObj;
					configObj.save();
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
    
    @SecureSocial.SecuredAction
    public static Result getSliderImageById(Long id,String type) {
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
    
    @SecureSocial.SecuredAction
    public static Result getFeaturedImageById(Long id,String type) {
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
    
    @SecureSocial.SecuredAction
    public static Result deleteSliderImage(Long id) {
    	SliderImage image = SliderImage.findById(id);
    	File file = new File(rootDir+image.path);
    	file.delete();
    	File thumbfile = new File(rootDir+image.thumbPath);
    	thumbfile.delete();
    	image.delete();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result deleteFeaturedImage(Long id) {
    	FeaturedImage image = FeaturedImage.findById(id);
    	File file = new File(rootDir+image.path);
    	file.delete();
    	File thumbfile = new File(rootDir+image.thumbPath);
    	thumbfile.delete();
    	image.delete();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result deleteAudioFile(Long id) {
    	VehicleAudio audio = VehicleAudio.findById(id);
    	File file = new File(rootDir+audio.path);
    	file.delete();
    	audio.delete();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result getSliderAndFeaturedImages() {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
    	
    	Map<String,List> map = new HashMap<>();
    	List<SliderImage> sliderList = SliderImage.findByUser(user);
    	List<ImageVM> sliderVMList = new ArrayList<>();
    	List<ImageVM> configList = new ArrayList<>();
    	
    	for(SliderImage slider: sliderList) {
    		ImageVM vm = new ImageVM();
    		vm.id = slider.id;
    		vm.path = slider.path;
    		vm.imgName = slider.imgName;
    		sliderVMList.add(vm);
    	}
    	
    	map.put("sliderList", sliderVMList);
    	List<FeaturedImage> featuredList = FeaturedImage.findByUser(user);
    	List<ImageVM> featuredVMList = new ArrayList<>();
    	
    	for(FeaturedImage featured: featuredList) {
    		ImageVM vm = new ImageVM();
    		vm.id = featured.id;
    		vm.path = featured.path;
    		vm.imgName = featured.imgName;
    		featuredVMList.add(vm);
    	}
    	
    	map.put("featuredList", featuredVMList);
    	
    	SliderImageConfig sliderConfig = SliderImageConfig.findByUser(user);
    	ImageVM slider = new ImageVM();
    	slider.height = sliderConfig.cropHeight;
    	slider.width = sliderConfig.cropWidth;
    	configList.add(slider);
    	
    	FeaturedImageConfig featuredConfig = FeaturedImageConfig.findByUser(user);
    	ImageVM featured = new ImageVM();
    	featured.height = featuredConfig.cropHeight;
    	featured.width = featuredConfig.cropWidth;
    	configList.add(featured);
    	map.put("configList", configList);
    	
    	return ok(Json.toJson(map));
    }
    
    @SecureSocial.SecuredAction
    public static Result getSliderImageDataById(Long id) throws IOException {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
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
    
    @SecureSocial.SecuredAction
    public static Result getFeaturedImageDataById(Long id) throws IOException {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
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
    
    @SecureSocial.SecuredAction
    public static Result editSliderImage() throws IOException {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
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
    	Thumbnails.of(croppedImage).scale(1.0).toFile(file);
    	
    	Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
    	
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result editFeaturedImage() throws IOException {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
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
    	Thumbnails.of(croppedImage).scale(1.0).toFile(file);
    	
    	Thumbnails.of(croppedImage).size(150, 150).toFile(thumbFile);
    	
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result getImageConfig() {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
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
    
    @SecureSocial.SecuredAction
    public static Result saveSliderConfig(Integer width,Integer height) {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
    	SliderImageConfig config = SliderImageConfig.findByUser(user);
    	
    	if(config != null) {
    		config.setCropHeight(height);
    		config.setCropWidth(width);
    		config.update();
    	}
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result saveFeaturedConfig(Integer width,Integer height) {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
    	FeaturedImageConfig config = FeaturedImageConfig.findByUser(user);
    	
    	if(config != null) {
    		config.setCropHeight(height);
    		config.setCropWidth(width);
    		config.update();
    	}
    	return ok();
    }
    
    
    @SecureSocial.SecuredAction
    public static Result exportDataAsCSV() throws IOException {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
    	CSVWriter writer = new CSVWriter(new FileWriter("D:\\vehicleInfo.csv"));
    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(user);
    	
    	for(Vehicle vehicle: vehicleList) {
    		String []row = new String[22];
    		row[0] = "";
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
    		row[13] = vehicle.description;
    		row[14] = vehicle.bodyStyle;
    		row[15] = vehicle.engine;
    		row[16] = vehicle.drivetrain;
    		row[17] = vehicle.fuel;
    		row[18] = vehicle.styleFeatures+","+vehicle.safetyFeatures+","+vehicle.audioNavFeatures+","+vehicle.performanceFeatures+","+vehicle.otherCarFeatures;
    		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicle.vin, user);
    		String str = "";
    		for(VehicleImage img : vImageList) {
    			str = str +rootDir+img.path+",";
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
    	 
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result exportCarfaxCSV() throws IOException {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
    	CSVWriter writer = new CSVWriter(new FileWriter("D:\\vehicleCarfaxInfo.csv"));
    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(user);
    	
    	for(Vehicle vehicle: vehicleList) {
    		String []row = new String[42];
    		row[0] = "";
    		row[1] = vehicle.vin;
    		row[2] = vehicle.stock;
    		row[3] = "";
    		row[4] = "";
    		row[5] = vehicle.category;
    		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicle.vin, user);
    		String str = "";
    		for(VehicleImage img : vImageList) {
    			str = str +rootDir+img.path+"|";
    		}
    		row[6] = str;
    		row[7] = "";
    		row[8] = "";
    		row[9] = "";
    		row[10] = "";
    		row[11] = "";
    		row[12] = "";
    		row[13] = "";
    		row[14] = "";
    		row[15] = "";
    		row[16] = "";
    		row[17] = "";
    		row[18] = vehicle.make;
    		row[19] = vehicle.model;
    		row[20] = vehicle.trim;
    		row[21] = vehicle.bodyStyle;
    		row[22] = vehicle.mileage;
    		row[23] = vehicle.year;
    		row[24] = "";
    		row[25] = vehicle.price.toString();
    		row[26] = vehicle.exteriorColor;
    		row[27] = vehicle.interiorColor;
    		row[28] = "";
    		row[29] = vehicle.doors;
    		row[30] = vehicle.fuel;
    		row[31] = vehicle.engine;
    		row[32] = vehicle.drivetrain;
    		row[33] = vehicle.transmission;
    		row[34] = "";
    		row[35] = vehicle.description;
    		row[36] = vehicle.styleFeatures;
    		row[37] = vehicle.safetyFeatures;
    		row[38] = "";
    		row[39] = "";
    		row[40] = "";
    		row[41] = "";
    		
    		writer.writeNext(row);
    	}
    	
    	 writer.close();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result exportCarGurusCSV() throws IOException {
    	AuthUser user = (AuthUser) ctx().args.get(SecureSocial.USER_KEY);
    	CSVWriter writer = new CSVWriter(new FileWriter("D:\\vehicleCarGurusInfo.csv"));
    	List<Vehicle> vehicleList = Vehicle.getAllVehicles(user);
    	
    	for(Vehicle vehicle: vehicleList) {
    		String []row = new String[29];
    		row[0] = vehicle.vin;
    		row[1] = vehicle.make;
    		row[2] = vehicle.model;
    		row[3] = vehicle.year;
    		row[4] = vehicle.trim;
    		row[5] = vehicle.price.toString();
    		row[6] = vehicle.mileage;
    		List<VehicleImage> vImageList = VehicleImage.getByVin(vehicle.vin, user);
    		String str = "";
    		for(VehicleImage img : vImageList) {
    			str = str +rootDir+img.path+",";
    		}
    		row[7] = str;
    		row[8] = vehicle.exteriorColor;
    		row[9] = "";
    		row[10] = vehicle.stock;
    		row[11] = vehicle.transmission;
    		row[12] = vehicle.styleFeatures+","+vehicle.safetyFeatures;
    		row[13] = "";
    		row[14] = "";
    		row[15] = "";
    		row[16] = "";
    		row[17] = "";
    		row[18] = "";
    		row[19] = "";
    		row[20] = "";
    		row[21] = vehicle.interiorColor;
    		row[22] = "";
    		row[23] = "";
    		row[24] = vehicle.engine;
    		row[25] = "";
    		row[26] = "";
    		row[27] = "";
    		row[28] = "";
    		
    		writer.writeNext(row);
    	}
    	
    	 writer.close();
    	return ok();
    }
    
}
