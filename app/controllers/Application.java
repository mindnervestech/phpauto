package controllers;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.avaje.ebean.Ebean;

import net.coobird.thumbnailator.Thumbnails;

import models.Site;
import models.Vehicle;
import models.VehicleImage;

import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;

import viewmodel.ImageVM;
import viewmodel.PinVM;
import viewmodel.SiteVM;
import viewmodel.SpecificationVM;
import views.html.*;

public class Application extends Controller {
  
	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	
    /*public static Result index() {
        return ok(index.render("Your new application is ready."));
    }*/
	
	public static Result home() {
		return ok(home.render());
	}
	
	@SecureSocial.SecuredAction
    public static Result index() {
        Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
        return ok(index.render(user));
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
    	/*URL url;
			url = new URL("https://vindecoder.p.mashape.com/decode_vin?vin="+vin);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("X-Mashape-Key", "");
		conn.setRequestProperty("Accept", "application/json");
		
		BufferedReader br = new BufferedReader(
                           new InputStreamReader(conn.getInputStream()));*/
		
		PinVM pinVM = new PinVM();
		pinVM.success = true;
		pinVM.pin = "4F2YU09161KM33122";
		
		SpecificationVM specificationVM = new SpecificationVM();
		specificationVM.vin = "4F2YU09161KM33122";
		specificationVM.year = "2001";
		specificationVM.make = "MAZDA";
		specificationVM.model = "TRIBUTE";
		specificationVM.trim_level = "LX";
		specificationVM.engine = "3.0L V6 DOHC 24V";
		specificationVM.style = "SPORT UTILITY 4-DR";
		specificationVM.made_in = "UNITED STATES";
		specificationVM.steering_type = "R&P";
		specificationVM.anti_brake_system = "Non-ABS | 4-Wheel ABS";
		specificationVM.tank_size = "16.40 gallon";
		specificationVM.overall_height = "69.90 in.";
		specificationVM.overall_length = "173.00 in.";
		specificationVM.overall_width = "71.90 in.";
		specificationVM.standard_seating = "5";
		specificationVM.optional_seating = "";
		specificationVM.highway_mileage = "24 miles/gallon";
		specificationVM.city_mileage = "18 miles/gallon";
		pinVM.specification = specificationVM;
		
    	return ok(Json.toJson(pinVM));
    }
    
    public static Result saveVehicle() {
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
    
    public static Result getImageById(Long id) {
    	VehicleImage image = VehicleImage.findById(id);
    	File file = new File(rootDir+image.thumbPath);
    	return ok(file);
    }
    
    public static Result deleteImage(Long id) {
    	VehicleImage image = VehicleImage.findById(id);
    	File file = new File(rootDir+image.path);
    	File thumbFile = new File(rootDir+image.thumbPath);
    	file.delete();
    	thumbFile.delete();
    	image.delete();
    	return ok();
    }
    
    public static Result setDefaultImage(Long id) {
    	VehicleImage image = VehicleImage.findById(id);
    	image.setDefaultImage(true);
    	image.update();
    	return ok();
    }
    
    public static Result removeDefault(Long id) {
    	VehicleImage image = VehicleImage.findById(id);
    	image.setDefaultImage(false);
    	image.update();
    	return ok();
    }
    
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
    
	 public static Result getAllVehicles(){
    	List <Vehicle> vehicleObjList = Vehicle.getAllVehicles();
    	
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
    
    public static Result updateVehicleStatus(Long id ){
    	Vehicle vm = Vehicle.findById(id);
    	
    	if(vm != null){
    		vm.status = "sold";
    		vm.update();
    	}
    	
    	return ok();
    }
    
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
