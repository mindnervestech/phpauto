package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.fasterxml.jackson.databind.JsonNode;

import net.coobird.thumbnailator.Thumbnails;

import models.AuthUser;
import models.Blog;
import models.ContactHeader;
import models.CoverImage;
import models.FeaturedImage;
import models.FeaturedImageConfig;
import models.SiteAboutUs;
import models.SiteComparison;
import models.SiteInventory;
import models.SliderImage;
import models.SliderImageConfig;
import models.VehicleHeader;
import models.VehicleImage;
import models.VehicleImageConfig;
import models.Warranty;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.EditImageVM;
import viewmodel.ImageVM;

import views.html.home;

public class CropImageController extends Controller{

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
	
	public static AuthUser getLocalUser() {
    	String id = session("USER_KEY");
    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
    	//AuthUser user = getLocalUser();
		return user;
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
	 
	 public static Result editInventoryImages() throws IOException {
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
}