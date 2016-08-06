package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.fasterxml.jackson.databind.JsonNode;

import models.AuthUser;
import models.Blog;
import models.ContactHeader;
import models.CoverImage;
import models.FeaturedImage;
import models.FeaturedImageConfig;
import models.Location;
import models.SiteAboutUs;
import models.SiteComparison;
import models.SiteContent;
import models.SiteInventory;
import models.SiteLogo;
import models.SiteTestimonials;
import models.SliderImage;
import models.SliderImageConfig;
import models.Vehicle;
import models.VehicleHeader;
import models.Warranty;
import net.coobird.thumbnailator.Thumbnails;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import securesocial.core.Identity;

import viewmodel.ImageVM;
import viewmodel.SiteAboutUsVM;
import viewmodel.SiteContentVM;
import viewmodel.SiteInventoryVM;
import viewmodel.SiteLogoVM;
import viewmodel.SiteTestimonialVM;
import viewmodel.VehicleVM;
import views.html.home;

public class HomePageController extends Controller{

	
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
	
	
    public static Result  uploadVehiclePhotos() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	String makeValue=request().getQueryString("make");
	    	AuthUser userObj = (AuthUser)getLocalUser();
	    	FilePart picture = body.getFile("file");
	    	  if (picture != null) {
	    	    String fileName = picture.getFilename();
	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"VehicleProfile");
	    	    if(!fdir.exists()) {
	    	    	fdir.mkdirs();
	    	    }
	    	    String fname = fileName.replace("(", "-");
				String fname1=fname.replace(")","-");	
				fileName=fname1.replace(" ", "-");
	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"VehicleProfile"+File.separator+fileName;
	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"VehicleProfile"+File.separator+"thumbnail_"+fileName;
	    	    File thumbFile = new File(thumbnailPath);
	    	    File file = picture.getFile();
	    	    try {
	    	    BufferedImage originalImage = ImageIO.read(file);
	    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
	    	    File _f = new File(filePath);
				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
				
				VehicleHeader imageObj = VehicleHeader.findByLocationsAndMake(Long.valueOf(session("USER_LOCATION")),makeValue);
				if(imageObj == null) {
					VehicleHeader vImage = new VehicleHeader();
					vImage.coverImageName = fname1;
					vImage.path = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"VehicleProfile"+"/"+fileName;
					vImage.thumbPath = "/"+session("USER_LOCATION")+"/"+userObj.id+"/"+"VehicleProfile"+"/"+"thumbnail_"+fileName;
					vImage.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
					vImage.makeValue=makeValue;
					vImage.findNewId=1L;
					vImage.save();
					
				}
				else{
					imageObj.setCoverImageName(fname1);
					imageObj.setPath("/"+session("USER_LOCATION")+"/"+"VehicleProfile"+"/"+fileName);
					imageObj.setThumbPath("/"+session("USER_LOCATION")+"/"+"VehicleProfile"+"/"+"thumbnail_"+fileName);
					Long value = imageObj.findNewId + 1L;
					imageObj.setFindNewId(value);
					imageObj.setMakeValue(makeValue);
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
    
    public static AuthUser getLocalUser() {
    	String id = session("USER_KEY");
    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
    	//AuthUser user = getLocalUser();
		return user;
	}

    public static Result saveSliderPosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    
    public static Result  saveCoveredPosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<SiteAboutUs> images = mapper.readValue(nodes.toString(), new TypeReference<List<SiteAboutUs>>() {});
				
		    	for(SiteAboutUs image : images) {
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
    
    public static Result saveComparePosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<SiteComparison> images = mapper.readValue(nodes.toString(), new TypeReference<List<SiteComparison>>() {});
				
		    	for(SiteComparison image : images) {
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
    
    public static Result   saveBlogPosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<Blog> images = mapper.readValue(nodes.toString(), new TypeReference<List<Blog>>() {});
				
		    	for(Blog image : images) {
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
    
    public static Result saveVehiclePosition() {
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
    
    public static Result   saveWarrantyPosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<Warranty> images = mapper.readValue(nodes.toString(), new TypeReference<List<Warranty>>() {});
				
		    	for(Warranty image : images) {
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
    
    public static Result  saveContactPosition() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	JsonNode nodes = ctx().request().body().asJson();
	    	ObjectMapper mapper = new ObjectMapper();
	    	try {
	    		List<ContactHeader> images = mapper.readValue(nodes.toString(), new TypeReference<List<ContactHeader>>() {});
				
		    	for(ContactHeader image : images) {
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
    
    public static Result getAllMakeList() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		List<VehicleVM> makeList = new ArrayList<>();
	    	List<Vehicle> list = Vehicle.findByNewArrAndLocation(Long.valueOf(session("USER_LOCATION")));
	    	Map<String, Vehicle> mapMake = new HashMap<>();
    		for (Vehicle vehicle : list) {
    			if(vehicle.getMake() != null){
    				
    				Vehicle objectMake = mapMake.get(vehicle.getMake());
    					mapMake.put(vehicle.getMake(), vehicle);
    					
    			}
    		}
    		for (Entry<String, Vehicle> value : mapMake.entrySet()) {
    			VehicleVM vm=new VehicleVM();
    			vm.make=value.getKey();
    			makeList.add(vm);
    		}
	    	return ok(Json.toJson(makeList));
    	}	
    }
    
    public static Result getMakeWiseData(String makeValue) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
    		Map<String,List> map = new HashMap<>();
	    	List<SiteAboutUsVM> vehicleProfile=new ArrayList<>();
	    	List<SiteAboutUsVM> vehicleProfile1=new ArrayList<>();
	    	VehicleHeader header = VehicleHeader.findByLocationsAndMake(Long.valueOf(session("USER_LOCATION")),makeValue);
	    	if(header != null){
	    	SiteAboutUsVM sUs = new SiteAboutUsVM();
	    	sUs.mainTitle = header.mainTitle;
	    	sUs.subtitle=header.subtitle;
	    	sUs.id = header.id;
	    	sUs.path = header.path;
	    	sUs.imgName = header.coverImageName;
	    	sUs.thumbPath=header.thumbPath;
	    	sUs.financeFlag=header.financeFlag;
	    	sUs.makeFlag=header.makeFlag;
	    	sUs.socialFlag=header.socialFlag;
	    	sUs.findById=header.findNewId;
	    	sUs.makeValue=header.makeValue;
	    	vehicleProfile.add(sUs);
	    	
	    	}
	    	map.put("vehicleProfileData", vehicleProfile);
	    	
	    	List <VehicleHeader> header1=VehicleHeader.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(header1 != null)
	    	for(VehicleHeader vheader:header1){
	    		
	    		SiteAboutUsVM sUs = new SiteAboutUsVM();
		    	sUs.mainTitle = vheader.mainTitle;
		    	sUs.subtitle=vheader.subtitle;
		    	sUs.id = vheader.id;
		    	sUs.path = vheader.path;
		    	sUs.imgName = vheader.coverImageName;
		    	sUs.thumbPath=vheader.thumbPath;
		    	sUs.financeFlag=vheader.financeFlag;
		    	sUs.makeFlag=vheader.makeFlag;
		    	sUs.socialFlag=vheader.socialFlag;
		    	sUs.findById=vheader.findNewId;
		    	sUs.makeValue=vheader.makeValue;
		    	vehicleProfile1.add(sUs);
	    	}
	    	map.put("vehProfData", vehicleProfile1);
	    	
	    	return ok(Json.toJson(map));
    	}	
    }
    
    public static Result getSliderAndFeaturedImages() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
	    	
	    	CoverImage cImage = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	List<SliderImageConfig> sIcons = new ArrayList<>();
	    	SliderImageConfig sIcon = new SliderImageConfig();
	    	if(cImage != null){
	    		sIcon.cropWidth = cImage.cropWidth;
	    		sIcon.cropHeight = cImage.cropHeight;
	    		sIcons.add(sIcon);
	    	}
	    	map.put("coverImg", sIcons);
	    	
	    	List<SiteInventoryVM> sVms = new ArrayList<>();
	    	List<SiteInventory> siInventory = SiteInventory.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	for(SiteInventory sInventory:siInventory){
	    		if(sInventory.imageUrl != null){
	    			SiteInventoryVM siteInventory = new SiteInventoryVM();
		    		siteInventory.imageName = sInventory.imageName;
		    		siteInventory.imageUrl = sInventory.imageUrl;
		    		siteInventory.thumbPath = sInventory.thumbPath;
		    		siteInventory.vType = sInventory.vType;
		    		siteInventory.id = sInventory.id;
		    		siteInventory.mainTitle = sInventory.mainTitle;
		    		siteInventory.subTitle = sInventory.subTitle;
		    		siteInventory.sortBy = sInventory.sortBy;
		    		siteInventory.sortType = sInventory.sortType;
		    		siteInventory.defaultView = sInventory.defaultView;
		    		siteInventory.applyAll = sInventory.applyAll.toString();
		    		siteInventory.findById=sInventory.findNewId;
		    		sVms.add(siteInventory);
	    		}
	    	}
	    	map.put("inventoryImg", sVms);
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
	    	
	    	CoverImage Config = CoverImage.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	ImageVM feat = new ImageVM();
	    	if(Config != null) {
	    		feat.height = Config.cropHeight;
	    		feat.width = Config.cropWidth;
	    	} else {
	    		CoverImage ftdconfig = new CoverImage();
	    		ftdconfig.cropHeight = 95;
	    		ftdconfig.cropWidth = 160;
	    		ftdconfig.user = user;
				ftdconfig.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		ftdconfig.save();
	    		feat.height = ftdconfig.cropHeight;
	    		feat.width = ftdconfig.cropWidth;
	    	}
	    	configList.add(feat);
	    	map.put("coverImageConf", configList);
	    	
	    	
	    	
	    	
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
	    	
	    	List<SiteAboutUsVM> siteAboutList=new ArrayList<>();
	    	SiteAboutUs sAboutUs = SiteAboutUs.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs != null){
	    	SiteAboutUsVM sUs = new SiteAboutUsVM();
	    	sUs.mainTitle = sAboutUs.mainTitle;
	    	sUs.textData = sAboutUs.text;
	    	sUs.textData1 = sAboutUs.text1;
	    	sUs.subtitle=sAboutUs.subtitle;
	    	sUs.headerTitle=sAboutUs.headerTitle;
	    	sUs.imageurl = rootDir+sAboutUs.imageurl;
	    	sUs.id = sAboutUs.id;
	    	sUs.path = sAboutUs.path;
	    	sUs.imgName = sAboutUs.imgName;
	    	sUs.row = sAboutUs.row;
	    	sUs.col = sAboutUs.col;
	    	sUs.thumbPath=sAboutUs.thumbPath;
	    	sUs.findById=sAboutUs.findNewId;
	    	siteAboutList.add(sUs);
	    	}
	    	map.put("siteAboutUs", siteAboutList);
	    	
	    	
	    	

	    	List<SiteAboutUsVM> siteAboutList1=new ArrayList<>();
	    	Blog sAboutUs1 = Blog.findByLocations(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs1 != null){
	    	SiteAboutUsVM sUs = new SiteAboutUsVM();
	    	sUs.mainTitle = sAboutUs1.mainTitle;
	    	sUs.subtitle=sAboutUs1.subtitle;
	    	sUs.id = sAboutUs1.id;
	    	sUs.path = sAboutUs1.path;
	    	sUs.imgName = sAboutUs1.coverImageName;
	    	sUs.row = sAboutUs1.row;
	    	sUs.col = sAboutUs1.col;
	    	sUs.findById=sAboutUs1.findNewId;
	    	sUs.thumbPath=sAboutUs1.thumbPath;
	    	siteAboutList1.add(sUs);
	    	}
	    	map.put("blogData", siteAboutList1);
	    	
	    	/*
	    	List<SiteAboutUsVM> vehicleProfile=new ArrayList<>();
	    	VehicleHeader header = VehicleHeader.findByLocations(Long.valueOf(session("USER_LOCATION")));
	    	if(header != null){
	    	SiteAboutUsVM sUs = new SiteAboutUsVM();
	    	sUs.mainTitle = header.mainTitle;
	    	sUs.subtitle=header.subtitle;
	    	sUs.id = header.id;
	    	sUs.path = header.path;
	    	sUs.imgName = header.coverImageName;
	    	sUs.thumbPath=header.thumbPath;
	    	sUs.financeFlag=header.financeFlag;
	    	sUs.makeFlag=header.makeFlag;
	    	sUs.socialFlag=header.socialFlag;
	    	sUs.findById=header.findNewId;
	    	sUs.makeValue=header.makeValue;
	    	vehicleProfile.add(sUs);
	    	}
	    	map.put("vehicleProfileData", vehicleProfile);*/
	    	
	    	List<SiteAboutUsVM> siteAbout=new ArrayList<>();
	    	Warranty sAbout = Warranty.findByLocations(Long.valueOf(session("USER_LOCATION")));
	    	if(sAbout != null){
	    	SiteAboutUsVM sUs = new SiteAboutUsVM();
	    	sUs.mainTitle = sAbout.mainTitle;
	    	sUs.subtitle=sAbout.subtitle;
	    	sUs.id = sAbout.id;
	    	sUs.path = sAbout.path;
	    	sUs.imgName = sAbout.coverImageName;
	    	sUs.thumbPath=sAbout.thumbPath;
	    	sUs.findById = sAbout.findNewId;
	    	sUs.hideMenu = sAbout.hideMenu.toString();
	    	
	    	siteAbout.add(sUs);
	    	}
	    	map.put("warData", siteAbout);
	    	
	    	List<SiteAboutUsVM> siteAbout1=new ArrayList<>();
	    	SiteComparison sAbout1 = SiteComparison.findByLocations(Long.valueOf(session("USER_LOCATION")));
	    	if(sAbout != null){
	    	SiteAboutUsVM sUs = new SiteAboutUsVM();
	    	sUs.mainTitle = sAbout1.mainTitle;
	    	sUs.subtitle=sAbout1.subTitle;
	    	sUs.id = sAbout1.id;
	    	sUs.path = sAbout1.path;
	    	sUs.imgName = sAbout1.coverImageName;
	    	sUs.thumbPath=sAbout1.thumbPath;
	    	sUs.findById=sAbout1.findNewId;
	    	siteAbout1.add(sUs);
	    	}
	    	map.put("compareData", siteAbout1);
	    	
	    	
	    	
	    	
	    	
	    	List<SiteAboutUsVM> siteAboutList2=new ArrayList<>();
	    	ContactHeader sAboutUs2 = ContactHeader.findByLocations(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs2 != null){
	    	SiteAboutUsVM sUs = new SiteAboutUsVM();
	    	sUs.mainTitle = sAboutUs2.mainTitle;
	    	sUs.subtitle=sAboutUs2.subtitle;
	    	sUs.id = sAboutUs2.id;
	    	sUs.path = sAboutUs2.path;
	    	sUs.imgName = sAboutUs2.coverImageName;
	    	sUs.row = sAboutUs2.row;
	    	sUs.col = sAboutUs2.col;
	    	sUs.findById=sAboutUs2.findNewId;
	    	sUs.thumbPath=sAboutUs2.thumbPath;
	    	siteAboutList2.add(sUs);
	    	}
	    	map.put("contactData", siteAboutList2);
	    	
	    	
	    	int i = 1;
	    	List<SiteTestimonialVM> siteList = new ArrayList<>();
 	    	List<SiteTestimonials> siteTestimonials = SiteTestimonials.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
	    	SiteTestimonialVM sitVm = new SiteTestimonialVM();
	    	for(SiteTestimonials st:siteTestimonials){
	    		if(i == 1){
	    			sitVm.testiMonial1 = st.testimonial;
	    			sitVm.signature1 = st.signature;
	    		}
	    		if(i == 2){
	    			sitVm.testiMonial2 = st.testimonial;
	    			sitVm.signature2 = st.signature;
	    		}
	    		if(i == 3){
	    			sitVm.testiMonial3 = st.testimonial;
	    			sitVm.signature3 = st.signature;
	    		}
	    		if(i == 4){
	    			sitVm.testiMonial4 = st.testimonial;
	    			sitVm.signature4 = st.signature;
	    		}
	    		if(i == 5){
	    			sitVm.testiMonial5 = st.testimonial;
	    			sitVm.signature5 = st.signature;
	    		}
	    		if(i == 6){
	    			sitVm.testiMonial6 = st.testimonial;
	    			sitVm.signature6 = st.signature;
	    		}
	    		if(i == 7){
	    			sitVm.testiMonial7 = st.testimonial;
	    			sitVm.signature7 = st.signature;
	    		}
	    		if(i == 8){
	    			sitVm.testiMonial8 = st.testimonial;
	    			sitVm.signature8 = st.signature;
	    		}
	    		if(i == 9){
	    			sitVm.testiMonial9 = st.testimonial;
	    			sitVm.signature9 = st.signature;
	    		}
	    		if(i == 10){
	    			sitVm.testiMonial10 = st.testimonial;
	    			sitVm.signature10 = st.signature;
	    		}
	    		
	    		i++;
	    	}
	    	siteList.add(sitVm);
	    	
	    	map.put("testiMonialVM", siteList);
	    	
	    	return ok(Json.toJson(map));
    	}	
    }
    
    public static Result deleteSliderImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    		return ok(home.render("",userRegistration));
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
    
    public static Result deleteInventoryImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SiteInventory image = SiteInventory.findById(id);
	    	File file = new File(rootDir+File.separator+Long.valueOf(session("USER_LOCATION"))+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+image.imageName);
	    	file.delete();
	    	File thumbfile = new File(rootDir+File.separator+Long.valueOf(session("USER_LOCATION"))+File.separator+"Inventory"+File.separator+"CoverImg"+File.separator+"thumbnail_"+image.imageName);
	    	thumbfile.delete();
	    	image.setImageName(null);
	    	image.setImageUrl(null);
	    	image.setThumbPath(null);
	    	image.update();
	    	return ok();
    	}
    }
    
    public static Result deleteCvrImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SiteAboutUs image = SiteAboutUs.findById(id);
	    	File file = new File(rootDir+File.separator+user.id+File.separator+"CvrImages"+File.separator+image.imgName);
	    	file.delete();
	    	File thumbfile = new File(rootDir+File.separator+user.id+File.separator+"CvrImages"+File.separator+"thumbnail_"+image.imgName);
	    	thumbfile.delete();
	    	//image.delete();
	    	image.setThumbPath(null);
	    	image.setImgName(null);
	    	image.setImageurl(null);
	    	image.setPath(null);
	    	image.setRow(null);
	    	image.setCol(null);
	    	image.update();
	    	return ok();
    	}
    }
    
    public static Result deleteVehicleImage(Long id, String makeValue ) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	VehicleHeader image = VehicleHeader.findByIdAndMake(id,makeValue);
	    	File file = new File(rootDir+File.separator+"VehicleProfile"+File.separator+image.coverImageName);
	    	file.delete();
	    	File thumbfile = new File(rootDir+File.separator+"VehicleProfile"+File.separator+"thumbnail_"+image.coverImageName);
	    	thumbfile.delete();
	    	//image.delete();
	    	image.setThumbPath(null);
	    	image.setCoverImageName(null);
	    	image.setPath(null);
	    	image.update();
	    	return ok();
    	}
    }
    
    public static Result deleteCompareImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	SiteComparison image = SiteComparison.findById(id);
	    	File file = new File(rootDir+File.separator+"Compare"+File.separator+image.coverImageName);
	    	file.delete();
	    	File thumbfile = new File(rootDir+File.separator+"Compare"+File.separator+"thumbnail_"+image.coverImageName);
	    	thumbfile.delete();
	    	//image.delete();
	    	image.setThumbPath(null);
	    	image.setCoverImageName(null);
	    	image.setPath(null);
	    	image.update();
	    	return ok();
    	}
    }
    
    public static Result deleteWarImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Warranty image = Warranty.findById(id);
	    	File file = new File(rootDir+File.separator+"Warranty"+File.separator+image.coverImageName);
	    	file.delete();
	    	File thumbfile = new File(rootDir+File.separator+"Warranty"+File.separator+"thumbnail_"+image.coverImageName);
	    	thumbfile.delete();
	    	//image.delete();
	    	image.setThumbPath(null);
	    	image.setCoverImageName(null);
	    	image.setPath(null);
	    	image.update();
	    	return ok();
    	}
    }
    
    public static Result deleteContactImage(Long id) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	ContactHeader image = ContactHeader.findById(id);
	    	File file = new File(rootDir+File.separator+"ContactImages"+File.separator+image.coverImageName);
	    	file.delete();
	    	File thumbfile = new File(rootDir+File.separator+"ContactImages"+File.separator+"thumbnail_"+image.coverImageName);
	    	thumbfile.delete();
	    	//image.delete();
	    	image.setThumbPath(null);
	    	image.setCoverImageName(null);
	    	image.setPath(null);
	    	image.setRow(null);
	    	image.setCol(null);
	    	image.update();
	    	return ok();
    	}
    }
    
    public static Result saveSitetestiMonial() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	
	    	Form<SiteTestimonialVM> form = DynamicForm.form(SiteTestimonialVM.class).bindFromRequest();
	    	SiteTestimonialVM vm = form.get();
	    	
	    	
	    	List<SiteTestimonials> siteTestDeletes = SiteTestimonials.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
	    	for(SiteTestimonials st:siteTestDeletes){
	    		 st.delete();
	    	}
	    	
	    		SiteTestimonials siTestimonials1 = new SiteTestimonials();
	    		siTestimonials1.testimonial = vm.testiMonial1;
	    		siTestimonials1.signature = vm.signature1;
	    		siTestimonials1.user = user;
	    		siTestimonials1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		siTestimonials1.save();
	    		
	    		if(vm.testiMonial2 != null){
	    			SiteTestimonials siTestimonials2 = new SiteTestimonials();
		    		siTestimonials2.testimonial = vm.testiMonial2;
		    		siTestimonials2.signature = vm.signature2;
		    		siTestimonials2.user = user;
		    		siTestimonials2.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		siTestimonials2.save();
	    		}
	    		
	    		if(vm.testiMonial3 != null){
	    			SiteTestimonials siTestimonials3 = new SiteTestimonials();
		    		siTestimonials3.testimonial = vm.testiMonial3;
		    		siTestimonials3.signature = vm.signature3;
		    		siTestimonials3.user = user;
		    		siTestimonials3.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		siTestimonials3.save();
	    		}
	    		
	    		if(vm.testiMonial4 != null){
	    			SiteTestimonials siTestimonials4 = new SiteTestimonials();
		    		siTestimonials4.testimonial = vm.testiMonial4;
		    		siTestimonials4.signature = vm.signature4;
		    		siTestimonials4.user = user;
		    		siTestimonials4.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		siTestimonials4.save();
	    		}
	    		
	    		if(vm.testiMonial5 != null){
	    			SiteTestimonials siTestimonials5 = new SiteTestimonials();
		    		siTestimonials5.testimonial = vm.testiMonial5;
		    		siTestimonials5.signature = vm.signature5;
		    		siTestimonials5.user = user;
		    		siTestimonials5.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		siTestimonials5.save();
	    		}
	    		
	    		
	    		if(vm.testiMonial6 != null){
	    			SiteTestimonials siTestimonials6 = new SiteTestimonials();
		    		siTestimonials6.testimonial = vm.testiMonial6;
		    		siTestimonials6.signature = vm.signature6;
		    		siTestimonials6.user = user;
		    		siTestimonials6.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		siTestimonials6.save();
	    		}
	    		
	    		
	    		if(vm.testiMonial7 != null){
	    			SiteTestimonials siTestimonials7 = new SiteTestimonials();
		    		siTestimonials7.testimonial = vm.testiMonial7;
		    		siTestimonials7.signature = vm.signature7;
		    		siTestimonials7.user = user;
		    		siTestimonials7.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		siTestimonials7.save();
	    		}
	    		
	    		
	    		if(vm.testiMonial8 != null){
	    			SiteTestimonials siTestimonials8 = new SiteTestimonials();
		    		siTestimonials8.testimonial = vm.testiMonial8;
		    		siTestimonials8.signature = vm.signature8;
		    		siTestimonials8.user = user;
		    		siTestimonials8.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		siTestimonials8.save();
	    		}
	    		
	    		if(vm.testiMonial9 != null){
	    			SiteTestimonials siTestimonials9 = new SiteTestimonials();
		    		siTestimonials9.testimonial = vm.testiMonial9;
		    		siTestimonials9.signature = vm.signature9;
		    		siTestimonials9.user = user;
		    		siTestimonials9.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		siTestimonials9.save();
	    		}
	    		
	    		
	    		if(vm.testiMonial10 != null){
	    			SiteTestimonials siTestimonials10 = new SiteTestimonials();
		    		siTestimonials10.testimonial = vm.testiMonial10;
		    		siTestimonials10.signature = vm.signature10;
		    		siTestimonials10.user = user;
		    		siTestimonials10.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		siTestimonials10.save();
	    		}
	    		
	    		
	    	
    	}
	    	return ok();
    	}
    
    public static Result saveSiteAboutUsHeader(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<SiteAboutUsVM> form = DynamicForm.form(SiteAboutUsVM.class).bindFromRequest();
	    	SiteAboutUsVM vm = form.get();
	    	
	    	SiteAboutUs sAboutUs = SiteAboutUs.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs == null){
	    		
	    		SiteAboutUs siAboutUs = new SiteAboutUs();
		    	siAboutUs.headerTitle = vm.mainTitle;
		    	siAboutUs.subtitle = vm.textData;
		    	siAboutUs.headerFlag =1;
		    	siAboutUs.user = user;
		    	siAboutUs.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	siAboutUs.save();
		    	
		    	
	    	}else{
	    			sAboutUs.setHeaderTitle(vm.mainTitle);
	    			sAboutUs.setSubtitle(vm.textData);
	    			sAboutUs.setHeaderFlag(1);
	    		sAboutUs.update();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveBlogHeader(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<SiteAboutUsVM> form = DynamicForm.form(SiteAboutUsVM.class).bindFromRequest();
	    	SiteAboutUsVM vm = form.get();
	    	
	    	Blog sAboutUs = Blog.findByLocations(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs == null){
	    		
	    		Blog siAboutUs = new Blog();
		    	siAboutUs.mainTitle = vm.mainTitle;
		    	siAboutUs.subtitle = vm.textData;
		    	siAboutUs.user = user;
		    	siAboutUs.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	siAboutUs.headerFlag=1;
		    	siAboutUs.save();
		    	
		    	
	    	}else{
	    			sAboutUs.setMainTitle(vm.mainTitle);
	    			sAboutUs.setSubtitle(vm.textData);
	    			sAboutUs.setHeaderFlag(1);
	    		sAboutUs.update();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveCompareHeader(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<SiteAboutUsVM> form = DynamicForm.form(SiteAboutUsVM.class).bindFromRequest();
	    	SiteAboutUsVM vm = form.get();
	    	
	    	SiteComparison sAboutUs = SiteComparison.findByLocations(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs == null){
	    		
	    		SiteComparison siAboutUs = new SiteComparison();
		    	siAboutUs.mainTitle = vm.mainTitle;
		    	siAboutUs.subTitle = vm.textData;
		    	siAboutUs.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	siAboutUs.compareFlag=vm.headerFlag;
		    	siAboutUs.save();
		    	
		    	
	    	}else{
	    			sAboutUs.setMainTitle(vm.mainTitle);
	    			sAboutUs.setSubTitle(vm.textData);
	    			sAboutUs.setCompareFlag(vm.headerFlag);
	    		sAboutUs.update();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveMakeFlag(int makeFlag){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<VehicleHeader> sAboutUs = VehicleHeader.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs != null){
	    		for(VehicleHeader siAboutUs:sAboutUs){
	    			siAboutUs.setMakeFlag(makeFlag);
			    	
	    			siAboutUs.update();
	    			
	    		}
		    	
	    	}else{
	    		VehicleHeader sAboutUs1=new VehicleHeader();
	    		sAboutUs1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		sAboutUs1.financeFlag=makeFlag;
	    		sAboutUs1.save();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveSocialFlag(int makeFlag){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<VehicleHeader> sAboutUs = VehicleHeader.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs != null){
	    		for(VehicleHeader siAboutUs:sAboutUs){
	    			siAboutUs.setSocialFlag(makeFlag);
	    			siAboutUs.update();
	    			
	    		}
		    	
	    	}else{
	    		VehicleHeader sAboutUs1=new VehicleHeader();
	    		sAboutUs1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		sAboutUs1.socialFlag=makeFlag;
	    		sAboutUs1.save();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveFinanceFlag(int makeFlag){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	List<VehicleHeader> sAboutUs = VehicleHeader.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs != null){
	    		for(VehicleHeader siAboutUs:sAboutUs){
	    			siAboutUs.setFinanceFlag(makeFlag);
	    			siAboutUs.update();
	    			
	    		}
		    	
	    	}else{
	    		VehicleHeader sAboutUs1=new VehicleHeader();
	    		sAboutUs1.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    		sAboutUs1.financeFlag=makeFlag;
	    		sAboutUs1.save();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveprofileHeader(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<SiteAboutUsVM> form = DynamicForm.form(SiteAboutUsVM.class).bindFromRequest();
	    	SiteAboutUsVM vm = form.get();
	    	
	    	VehicleHeader sAboutUs = VehicleHeader.findByLocationsAndMake(Long.valueOf(session("USER_LOCATION")),vm.makeValue);
	    	if(sAboutUs == null){
	    		
	    		VehicleHeader siAboutUs = new VehicleHeader();
		    	siAboutUs.mainTitle = vm.mainTitle;
		    	siAboutUs.subtitle = vm.textData;
		    	siAboutUs.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	siAboutUs.makeValue=vm.makeValue;
		    	siAboutUs.makeFlag=vm.makeFlag;
		    	siAboutUs.financeFlag=vm.financeFlag;
		    	siAboutUs.socialFlag=vm.socialFlag;
		    	siAboutUs.findNewId=1L;
		    	siAboutUs.save();
		    	
	    	}else{
	    		   sAboutUs.setMakeValue(vm.makeValue);
	    			sAboutUs.setMainTitle(vm.mainTitle);
	    			sAboutUs.setSubtitle(vm.textData);
	    			sAboutUs.setMakeFlag(vm.makeFlag);
	    			sAboutUs.setFinanceFlag(vm.financeFlag);
	    			sAboutUs.setSocialFlag(vm.socialFlag);
	    			sAboutUs.setFindNewId(sAboutUs.findNewId + 1);
	    			
	    		sAboutUs.update();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveHeader(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<SiteAboutUsVM> form = DynamicForm.form(SiteAboutUsVM.class).bindFromRequest();
	    	SiteAboutUsVM vm = form.get();
	    	
	    	Warranty sAboutUs = Warranty.findByLocations(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs == null){
	    		
	    		Warranty siAboutUs = new Warranty();
	    		if(vm.hideMenu.equals("true")){
	    			siAboutUs.hideMenu = 1;
	    		}else{
	    			siAboutUs.hideMenu = 0;
	    		}
	    		
		    	siAboutUs.mainTitle = vm.mainTitle;
		    	siAboutUs.subtitle = vm.textData;
		    	siAboutUs.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	siAboutUs.headerFlag=1;
		    	siAboutUs.save();
		    	
		    	
	    	}else{
	    			
	    			if(vm.hideMenu.equals("true")){
	    				sAboutUs.setHideMenu(1);
		    		}else{
		    			sAboutUs.setHideMenu(0);
		    		}
	    			sAboutUs.setMainTitle(vm.mainTitle);
	    			sAboutUs.setSubtitle(vm.textData);
	    			sAboutUs.setHeaderFlag(1);
	    		sAboutUs.update();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveContactHeader(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<SiteAboutUsVM> form = DynamicForm.form(SiteAboutUsVM.class).bindFromRequest();
	    	SiteAboutUsVM vm = form.get();
	    	
	    	ContactHeader sAboutUs = ContactHeader.findByLocations(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs == null){
	    		
	    		ContactHeader siAboutUs = new ContactHeader();
		    	siAboutUs.mainTitle = vm.mainTitle;
		    	siAboutUs.subtitle = vm.textData;
		    	siAboutUs.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	siAboutUs.headerFlag=1;
		    	siAboutUs.save();
		    	
		    	
	    	}else{
	    			sAboutUs.setMainTitle(vm.mainTitle);
	    			sAboutUs.setSubtitle(vm.textData);
	    			sAboutUs.setHeaderFlag(1);
	    		sAboutUs.update();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveSiteAboutUs(){
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	Form<SiteAboutUsVM> form = DynamicForm.form(SiteAboutUsVM.class).bindFromRequest();
	    	SiteAboutUsVM vm = form.get();
	    	
	    	SiteAboutUs sAboutUs = SiteAboutUs.findAllByLocation(Long.valueOf(session("USER_LOCATION")));
	    	if(sAboutUs == null){
	    		
	    		String urls = null;
	    		 MultipartFormData body = request().body().asMultipartFormData();
	    		 	if(body != null) {
	          		FilePart picture = body.getFile("file0");
	        	    	  if (picture != null) {
	        	    	    String fileName = picture.getFilename();
	        	    	    urls = fileName;
	        	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"aboutUs");
	        	    	    if(!fdir.exists()) {
	        	    	    	fdir.mkdir();
	        	    	    }
	        	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"aboutUs"+File.separator+fileName;
	        	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"aboutUs"+File.separator+rootDir+File.separator+session("USER_LOCATION")+File.separator+"aboutUs"+File.separator+"thumbnail_"+fileName;
	        	    	    File thumbFile = new File(thumbnailPath);
	        	    	    File file = picture.getFile();
	        	    	    try {
	        	    	    BufferedImage originalImage = ImageIO.read(file);
	        	    	    Thumbnails.of(originalImage).size(350, 240).toFile(thumbFile);
	        	    	    File _f = new File(filePath);
	        				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
	        	    	    }catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	        	    	  
	        	    	  } 
	          	   }
	    		 	
	    		 
	    		
	    		SiteAboutUs siAboutUs = new SiteAboutUs();
		    	siAboutUs.mainTitle = vm.mainTitle;
		    	siAboutUs.text = vm.textData;
		    	siAboutUs.text1 = vm.textData1;
		    	siAboutUs.imageurl = "/"+session("USER_LOCATION")+"/"+"aboutUs"+"/"+urls;
		    	siAboutUs.themImageurl = "/"+session("USER_LOCATION")+"/"+"aboutUs"+"/"+"thumbnail_"+urls;
		    	siAboutUs.user = user;
		    	
		    	
		    	siAboutUs.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	siAboutUs.save();
		    	
		    	
	    	}else{
	    			String urls = null;
	    			MultipartFormData body = request().body().asMultipartFormData();
	    		 	if(body != null) {
	          	   File file1 = new File(rootDir+sAboutUs.imageurl);
	          	   file1.delete();
	          		FilePart picture = body.getFile("file0");
	        	    	  if (picture != null) {
	        	    	    String fileName = picture.getFilename();
	        	    	    urls = fileName;
	        	    	    File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+"aboutUs");
	        	    	    if(!fdir.exists()) {
	        	    	    	fdir.mkdir();
	        	    	    }
	        	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"aboutUs"+File.separator+fileName;
	        	    	    String thumbnailPath = rootDir+File.separator+session("USER_LOCATION")+File.separator+"aboutUs"+File.separator+"thumbnail_"+fileName;
	        	    	    File thumbFile = new File(thumbnailPath);
	        	    	    File file = picture.getFile();
	        	    	    try {
	        	    	    BufferedImage originalImage = ImageIO.read(file);
	        	    	    Thumbnails.of(originalImage).size(345, 230).toFile(thumbFile);
	        	    	    File _f = new File(filePath);
	        				Thumbnails.of(originalImage).scale(1.0).toFile(_f);
	        	    	    }catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	        	    	   /* try {
	        	    	    	FileUtils.moveFile(file, new File(filePath));
	        	    	    		
	        	    	  } catch (FileNotFoundException e) {
	        	  			e.printStackTrace();
	        		  		} catch (IOException e) {
	        		  			e.printStackTrace();
	        		  		} */
	        	    	  } 
	          	   }
	    		
	    		sAboutUs.setMainTitle(vm.mainTitle);
	    		sAboutUs.setText(vm.textData);
	    		sAboutUs.setText1(vm.textData1);
	    		sAboutUs.setImageurl("/"+session("USER_LOCATION")+"/"+"aboutUs"+"/"+urls);
	    		sAboutUs.setThemImageurl("/"+session("USER_LOCATION")+"/"+"aboutUs"+"/"+"thumbnail_"+urls);
	    		sAboutUs.update();
	    	}
	    	
	    	return ok();
    	}	
    }
    
    public static Result saveSiteDescription() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    public static Result getLogoData() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    public static Result saveSiteTabText(String text) {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render("",userRegistration));
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
    
    public static Result saveSiteInventory(){
    	Identity user=getLocalUser();
    	Form<SiteInventoryVM> form = DynamicForm.form(SiteInventoryVM.class).bindFromRequest();
    	SiteInventoryVM vm = form.get();
    	
    		if(vm.vType.equals("Used") || vm.applyAll.equals("true")){
    			SiteInventory sInventoryUsed = SiteInventory.findByLocationAndType(Long.valueOf(session("USER_LOCATION")),"Used");
    			if(sInventoryUsed == null){
    				SiteInventory siInventory = new SiteInventory();
                	siInventory.defaultView = vm.defaultView;
                	siInventory.mainTitle = vm.mainTitle;
                	siInventory.subTitle = vm.subTitle;
                	siInventory.sortBy = vm.sortBy;
                	siInventory.sortType = vm.sortType;
                	siInventory.vType = "Used";
                	if(vm.applyAll.equals("true")){
                		siInventory.applyAll = 1;
                	}else{
                		siInventory.applyAll = 0;
                	}
                	
                	siInventory.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
                	siInventory.save();
    			}else{
    				sInventoryUsed.setDefaultView(vm.defaultView);
    				sInventoryUsed.setMainTitle(vm.mainTitle);
    				sInventoryUsed.setSubTitle(vm.subTitle);
    				sInventoryUsed.setSortBy(vm.sortBy);
    				sInventoryUsed.setSortType(vm.sortType);
    				sInventoryUsed.setvType("Used");
    				if(vm.applyAll.equals("true")){
    					sInventoryUsed.setApplyAll(1);
                	}else{
                		sInventoryUsed.setApplyAll(0);
                	}
    				sInventoryUsed.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    				sInventoryUsed.update();
    			}
    			
    		}
    		if(vm.vType.equals("New") || vm.applyAll.equals("true")){
    			SiteInventory sInventoryNew = SiteInventory.findByLocationAndType(Long.valueOf(session("USER_LOCATION")),"New");
    			if(sInventoryNew == null){
    				SiteInventory siInventory = new SiteInventory();
                	siInventory.defaultView = vm.defaultView;
                	siInventory.mainTitle = vm.mainTitle;
                	siInventory.subTitle = vm.subTitle;
                	siInventory.sortBy = vm.sortBy;
                	siInventory.sortType = vm.sortType;
                	siInventory.vType = "New";
                	if(vm.applyAll.equals("true")){
                		siInventory.applyAll = 1;
                	}else{
                		siInventory.applyAll = 0;
                	}
                	siInventory.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
                	siInventory.save();
    			}else{
    				sInventoryNew.setDefaultView(vm.defaultView);
    				sInventoryNew.setMainTitle(vm.mainTitle);
    				sInventoryNew.setSubTitle(vm.subTitle);
    				sInventoryNew.setSortBy(vm.sortBy);
    				sInventoryNew.setSortType(vm.sortType);
    				sInventoryNew.setvType("New");
    				if(vm.applyAll.equals("true")){
    					sInventoryNew.setApplyAll(1);
                	}else{
                		sInventoryNew.setApplyAll(0);
                	}
    				sInventoryNew.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    				sInventoryNew.update();
    			}
    			
    		}
    		if(vm.vType.equals("comingSoon") || vm.applyAll.equals("true")){
    			SiteInventory sInventoryComingSoon = SiteInventory.findByLocationAndType(Long.valueOf(session("USER_LOCATION")),"comingSoon");
    			if(sInventoryComingSoon == null){
    				SiteInventory siInventory = new SiteInventory();
                	siInventory.defaultView = vm.defaultView;
                	siInventory.mainTitle = vm.mainTitle;
                	siInventory.subTitle = vm.subTitle;
                	siInventory.sortBy = vm.sortBy;
                	siInventory.sortType = vm.sortType;
                	siInventory.vType = "comingSoon";
                	if(vm.applyAll.equals("true")){
                		siInventory.applyAll = 1;
                	}else{
                		siInventory.applyAll = 0;
                	}
                	siInventory.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
                	siInventory.save();
    			}else{
    				sInventoryComingSoon.setDefaultView(vm.defaultView);
    				sInventoryComingSoon.setMainTitle(vm.mainTitle);
    				sInventoryComingSoon.setSubTitle(vm.subTitle);
    				sInventoryComingSoon.setSortBy(vm.sortBy);
    				sInventoryComingSoon.setSortType(vm.sortType);
    				sInventoryComingSoon.setvType("comingSoon");
    				if(vm.applyAll.equals("true")){
    					sInventoryComingSoon.setApplyAll(1);
                	}else{
                		sInventoryComingSoon.setApplyAll(0);
                	}
    				sInventoryComingSoon.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
    				sInventoryComingSoon.update();
    			}
    			
    		}
    	
    	
    	return ok();
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
}