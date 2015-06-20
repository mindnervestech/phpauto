package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import org.codehaus.jackson.map.ObjectMapper;

import models.AuthUser;
import models.Site;
import models.Vehicle;
import models.VehicleImage;
import net.coobird.thumbnailator.Thumbnails;
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
import viewmodel.ImageVM;
import viewmodel.PinVM;
import viewmodel.SiteVM;
import viewmodel.SpecificationVM;
import views.html.home;

import com.avaje.ebean.Ebean;

public class Application extends Controller {
  
	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	
	final static String mashapeKey = Play.application().configuration()
			.getString("mashapeKey");
	
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
    
    public static Result getVehicleInfo(String vin) throws IOException {
    	
    	Vehicle vehicle = Vehicle.findByVid(vin); 
    	if(vehicle == null) {
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
			
			PinVM pinObj = new ObjectMapper().readValue(sb.toString(), PinVM.class);
			
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
			specificationVM.standardFeatures1 = vehicle.getStandardFeatures1();
			specificationVM.standardFeatures2 = vehicle.getStandardFeatures2();
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
    	Vehicle vehicleObj = Vehicle.findByVid(vm.vin);
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
	    	vehicle.standardFeatures1 = vm.standardFeatures1;
	    	vehicle.standardFeatures2 = vm.standardFeatures2;
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
    	
    	FilePart picture = body.getFile("file");
    	  if (picture != null) {
    	    String fileName = picture.getFilename();
    	    String contentType = picture.getContentType(); 
    	    File fdir = new File(rootDir+File.separator+vin);
    	    if(!fdir.exists()) {
    	    	fdir.mkdir();
    	    }
    	    String filePath = rootDir+File.separator+vin+File.separator+fileName;
    	    String thumbnailPath = rootDir+File.separator+vin+File.separator+"thumbnail_"+fileName;
    	    File thumbFile = new File(thumbnailPath);
    	    File file = picture.getFile();
    	    try {
    	    BufferedImage originalImage = ImageIO.read(file);
    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
    	    File _f = new File(filePath);
			Thumbnails.of(originalImage).scale(1.0).toFile(_f);
			
			VehicleImage imageObj = VehicleImage.getByImagePath(File.separator+vin+File.separator+fileName);
			if(imageObj == null) {
				VehicleImage vImage = new VehicleImage();
				vImage.vin = vin;
				vImage.imgName = fileName;
				vImage.path = File.separator+vin+File.separator+fileName;
				vImage.thumbPath = File.separator+vin+File.separator+"thumbnail_"+fileName;
				List<VehicleImage> imageList = VehicleImage.getByVin(vin);
				if(imageList.size() == 0) {
					vImage.row = 0;
					vImage.col = 0;
				} else {
					int maxRow = 0,maxCol = 0;
					for(VehicleImage img: imageList) {
						if(maxRow<=img.getRow()) {
							maxRow = img.getRow();
						}
						if(maxCol<=img.getCol()) {
							maxCol = img.getCol();
						}
					}
					vImage.row = maxRow+1;
					vImage.col = maxCol+1;
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
    
    public static Result getImagesByVin(String vin) {
    	List<VehicleImage> imageList = VehicleImage.getByVin(vin);
    	List<ImageVM> vmList = new ArrayList<>();
    	for(VehicleImage image : imageList) {
    		ImageVM vm = new ImageVM();
    		vm.id = image.id;
    		vm.name = image.imgName;
    		vm.defaultImage = image.defaultImage;
    		vm.row = image.row;
    		vm.col = image.col;
    		vm.path = image.path;
    		vmList.add(vm);
    	}
    	return ok(Json.toJson(vmList));
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
    	image.setDefaultImage(true);
    	image.update();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result removeDefault(Long id) {
    	VehicleImage image = VehicleImage.findById(id);
    	image.setDefaultImage(false);
    	image.update();
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result savePosition(Integer row,Integer col,Long id) {
    	VehicleImage image = VehicleImage.findById(id);
    	VehicleImage secondImage = VehicleImage.findByRowCol(row, col, image.getVin());
    	if(secondImage != null) {
    		secondImage.setRow(image.getRow());
    		secondImage.setCol(image.getCol());
    		secondImage.update();
    	}
    	image.setRow(row);
    	image.setCol(col);
    	image.update();
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
	    	vehicle.standardFeatures1 = vm.standardFeatures1;
	    	vehicle.standardFeatures2 = vm.standardFeatures2;
	    	vehicle.description = vm.description;
	    	vehicle.status  =  vm.status;
	    	vehicle.vehicleCnt = VehicleImage.getVehicleImageCountByVIN(vm.vin);
	    	VMs.add(vehicle);
    	}
     	
    	return ok(Json.toJson(VMs));
    }
    
    @SecureSocial.SecuredAction
    public static Result deleteVehicleById(Long id ){
    	Vehicle vm = Vehicle.findById(id);
    	if(vm != null){
    		List<VehicleImage> v = VehicleImage.getByVin(vm.vin);
    		if(v.size() != 0){
    			Ebean.delete(v);
    		}
    		vm.deleteManyToManyAssociations("site");
    		vm.delete();
    	}
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result updateVehicleStatus(Long id ){
    	Vehicle vm = Vehicle.findById(id);
    	
    	if(vm != null){
    		vm.status = "sold";
    		vm.update();
    	}
    	
    	return ok();
    }
    
    @SecureSocial.SecuredAction
    public static Result updateVehicle(){
    	Form<SpecificationVM> form = DynamicForm.form(SpecificationVM.class).bindFromRequest();
    	SpecificationVM vm = form.get();
    	System.out.println(vm.id);
    	Vehicle vehicle = Vehicle.findById(vm.id);
    	if(vehicle != null) {
	    	vehicle.setCategory(vm.category);
	    	vehicle.setVin(vm.vin);
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
	    	vehicle.setStandardFeatures1(vm.standardFeatures1);
	    	vehicle.setStandardFeatures2(vm.standardFeatures2);
	    	vehicle.setDescription(vm.description);
	    	/*List<Site> siteList = new ArrayList<>();
	    	if(vm.siteIds != null) {
		    	for(Long obj: vm.siteIds) {
		    		Site siteObj = Site.findById(obj);
		    		siteList.add(siteObj);
		    	}
		    	vehicle.site = siteList;
	    	}*/
	    	vehicle.update();
    	}
    	return ok();
    } 
    
	
}
