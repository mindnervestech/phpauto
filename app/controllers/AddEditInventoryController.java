package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
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

import models.AddCollection;
import models.AuthUser;
import models.CustomizationInventory;
import models.EmailDetails;
import models.FeaturedImageConfig;
import models.FollowBrand;
import models.Inventory;
import models.InventoryImage;
import models.Location;
import models.SiteLogo;
import models.Vehicle;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import securesocial.core.Identity;
import viewmodel.EditImageVM;
import viewmodel.ImageVM;
import viewmodel.InventoryVM;
import viewmodel.KeyValueDataVM;
import viewmodel.VehicleVM;
import views.html.home;

public class AddEditInventoryController extends Controller {

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


	
	   public static Result saveInventory() throws IOException {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {

	    		Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
		    	Form<InventoryVM> form = DynamicForm.form(InventoryVM.class).bindFromRequest();
		    	InventoryVM vm = form.get();
		    	
		    	Inventory inventoryObj = Inventory.getByProductId(vm.productId);
		    	Inventory inventory = new Inventory();
		    	if(inventoryObj == null) {
			    	
		    		inventory.productId = vm.productId;
			    	inventory.title = vm.title;
			    	inventory.description = vm.description;
			    	inventory.cost = vm.cost;
			    	inventory.price = vm.price;
			    	inventory.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
		    		inventory.collection = AddCollection.findById(vm.collection);
		    		inventory.save();
		    	}
		    	
		    	saveCustomInventoryData(inventory.id,vm);
		    	
		    	return ok(Json.toJson(inventory.id));
	    	}	
	    }
	   
	   
	   private static void saveCustomInventoryData(Long InventoryId,InventoryVM vm) {
	       	
	       	for(KeyValueDataVM custom:vm.customData){
	       		
	       		CustomizationInventory cDataValue = CustomizationInventory.findByKeyAndLeadId(custom.key,InventoryId);
	       		if(cDataValue == null){
	       			CustomizationInventory cValue = new CustomizationInventory();
	       			cValue.keyValue = custom.key;
	       			cValue.value = custom.value;
	       			cValue.InventoryId = InventoryId;
	       			cValue.displayGrid = custom.displayGrid;
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
			    			InventoryImage defaultImage = InventoryImage.getDefaultImage(vehicle.vin);
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
	   
	    public static Result saveInventoryPdf(Long id) throws IOException {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
	    		MultipartFormData body = request().body().asMultipartFormData();
	    		Inventory inv = Inventory.findById(id);
	    		if(inv != null) {
	    	       	
	    	       	if(body != null){
	    	       		FilePart picture = body.getFile("file0");
	    	       		if (picture != null) {
	    	       			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
	    	       			File file = picture.getFile();
	    	       			try {
	    	       				File fdir = new File(rootDir+File.separator+session("USER_LOCATION")+File.separator+inv.productId+File.separator+"customization_data"+File.separator+fileName);
	    	       	    	    if(!fdir.exists()) {
	    	       	    	    	fdir.mkdir();
	    	       	    	    }
	    	       	    	    String filePath = rootDir+File.separator+session("USER_LOCATION")+File.separator+inv.productId+File.separator+"customization_data"+File.separator+fileName;
	    	       	    	    FileUtils.moveFile(file, new File(filePath));
	    	       	    	    
	    	       	    	 CustomizationInventory cDataValueEdit = CustomizationInventory.findByKeyAndLeadId("fileupload",id);
	    	       	    		if(cDataValueEdit == null){
	    	       	    			CustomizationInventory cValue = new CustomizationInventory();
	    	       	    				cValue.keyValue = "fileupload";
	    	       	    				cValue.value = session("USER_LOCATION")+File.separator+inv.productId+File.separator+"customization_data"+File.separator+fileName;
	    	       	    				cValue.InventoryId = id;
	    	       	    				cValue.locations = Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	       	    				cValue.save();
	    	       	    			
	    	       	    		}else{
	    	       	    			//cDataValue.setKeyValue(custom.key);
	    	       	    			cDataValueEdit.setValue(session("USER_LOCATION")+File.separator+inv.productId+File.separator+"customization_data"+File.separator+fileName);
	    	       	    			cDataValueEdit.update();
	    	       	    		}
	    	       				
	    	       			} catch (Exception e) {
	    	   					e.printStackTrace();
	    	   				}
	    	       		}
	    	       	}
	    		}
	    		
	    	}
	    	return ok();
	    }
	

	    public static Result getInventoryById(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Inventory inventory = Inventory.findById(id);
				
		    	InventoryVM inventoryVm = new InventoryVM();
				inventoryVm.id = inventory.getId();
				inventoryVm.title = inventory.getTitle();
				inventoryVm.description = inventory.getDescription();
				inventoryVm.cost = inventory.getCost();
				inventoryVm.price = inventory.getPrice();
				inventoryVm.productId = inventory.getProductId();
				inventoryVm.collection=inventory.collection.id;
				findCustomeInventoryData(inventoryVm.id,inventoryVm);
				
				
				
		    	return ok(Json.toJson(inventoryVm));
	    	}	
	    }
	    
	    public static Result getAllInventory(Long locationId) {
		    	List<Inventory> inventoryList = Inventory.getByLocation(Location.findById(locationId));
				List<InventoryVM> iList = new ArrayList<>();
		    	for(Inventory  inventory:inventoryList){
		    		InventoryVM inventoryVm = new InventoryVM();
					inventoryVm.id = inventory.getId();
					inventoryVm.title = inventory.getTitle();
					inventoryVm.description = inventory.getDescription();
					inventoryVm.cost = inventory.getCost();
					inventoryVm.price = inventory.getPrice();
					AddCollection collection = AddCollection.findById(inventory.getCollection().id);
					inventoryVm.collection = inventory.getCollection().id;
					inventoryVm.collectionName = collection.title;
					inventoryVm.productId = inventory.getProductId();
					findCustomeInventoryData(inventoryVm.id,inventoryVm);
					iList.add(inventoryVm);
		    	}
				
		    	return ok(Json.toJson(iList));
	    }
	    
	    
	    
	    public static void findCustomeInventoryData(Long id,InventoryVM inventoryvm){
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
	    
	    public static Result updateInventoryById() throws SocketException, IOException{
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	
	    		Form<InventoryVM> form = DynamicForm.form(InventoryVM.class).bindFromRequest();
		    	InventoryVM vm = form.get();
		    	Inventory inventory = Inventory.findById(vm.id);
		    	if(inventory != null) {
		    		inventory.setProductId(vm.productId);
		    		inventory.setCost(vm.cost);
		    		inventory.setPrice(vm.price);
		    		inventory.setTitle(vm.title);
		    		inventory.setDescription(vm.description);
		    		if(vm.collection != null){
		    		inventory.setCollection(AddCollection.findById(vm.collection));
		    		}
		    		inventory.update();
			    	
			    	saveCustomInventoryData(inventory.id,vm);
			    
			    	
		    	}	
		    	return ok();
	    	}	
	    }  
	    
	    public static Result getImagesByProductId(String productId) {
	    	
		    	List<InventoryImage> imageList = InventoryImage.getByProductId(productId);
		    	//Application.reorderImagesForFirstTime(imageList);
		    	List<ImageVM> vmList = new ArrayList<>();
		    	for(InventoryImage image : imageList) {
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
	    
	    public static Result getImageInv(Long id, String type) {
		    	File file = null;
		    	InventoryImage image = InventoryImage.findById(id);
		    	if(type.equals("thumbnail")) {
			    	file = new File(rootDir+image.thumbPath.replaceAll("%20"," "));
		    	}
		    	
		    	if(type.equals("full")) {
		    		file = new File(rootDir+image.path.replaceAll("%20"," "));
		    	}
		    	return ok(file);
	    }
	    
		  public static Result getInventoryImageById(Long id) throws IOException {
		    		InventoryImage image = InventoryImage.findById(id);
			    	File file = new File(rootDir+image.path);
			    	
			    	BufferedImage originalImage = ImageIO.read(file);
			    	//FeaturedImageConfig featuredConfig = FeaturedImageConfig.findByUser(user);
			    	List<FeaturedImageConfig> featuredCon = FeaturedImageConfig.findByUser();
			    	FeaturedImageConfig featuredConfig = new FeaturedImageConfig();
			    	featuredConfig = featuredCon.get(0);
			    	ImageVM vm = new ImageVM();
					vm.id = image.id;
					//vm.imgName = image.imageName;
					vm.defaultImage = image.defaultImage;
					vm.row = originalImage.getHeight();
					vm.col = originalImage.getWidth();
					vm.path = image.path;
		    		vm.height = featuredConfig.cropHeight;
		    		vm.width = featuredConfig.cropWidth;
					//vm.vin = image.vin;
			    	return ok(Json.toJson(vm));
		    }
		  
		  public static Result editInventoryImage() throws IOException {
			    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
			    	EditImageVM vm = form.get();
			    	
			    	InventoryImage image = InventoryImage.findById(vm.imageId);
			    	File file = new File(rootDir+image.path);
			    	File thumbFile = new File(rootDir+image.thumbPath);
			    	
			    	BufferedImage originalImage = ImageIO.read(file);
			    	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
			    	Thumbnails.of(croppedImage).scale(1.0).toFile(file);
			    	
			    	Thumbnails.of(croppedImage).height(155).toFile(thumbFile);
			    	
			    	return ok();
		    }
	    
		  
		  public static Result uploadPhotos() {
			    	MultipartFormData body = request().body().asMultipartFormData();
			    	DynamicForm requestData = Form.form().bindFromRequest();
			    	String productId = requestData.get("productId");
			    	 String locationIdNew = requestData.get("locationIdNew");
			    	//Identity user = getLocalUser();
			    	//AuthUser userObj = (AuthUser)user;
			    	
			    	FilePart picture = body.getFile("file");
			    	  if (picture != null) {
			    	    String fileName = picture.getFilename();
			    	    File fdir = new File(pdfRootDir+File.separator+locationIdNew+File.separator+productId);
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = pdfRootDir+File.separator+locationIdNew+File.separator+productId+File.separator+fileName;
			    	    String thumbnailPath = pdfRootDir+File.separator+locationIdNew+File.separator+productId+File.separator+"thumbnail_"+fileName;
			    	    File thumbFile = new File(thumbnailPath);
			    	    File file = picture.getFile();
			    	    
			    	    try {
			    	    BufferedImage originalImage = ImageIO.read(file);
			    	  //  Thumbnails.of(originalImage).size(originalImage.getWidth(), originalImage.getHeight()).toFile(thumbFile);
			    	   // File _f = new File(filePath);
						//Thumbnails.of(originalImage).scale(1.0).toFile(_f);
						
						//BufferedImage originalImage = ImageIO.read(file);
			    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
			    	    File _f = new File(filePath);
						Thumbnails.of(originalImage).scale(1.0).toFile(_f);
						
						InventoryImage imageObj = InventoryImage.getByImagePath("/"+locationIdNew+"/"+productId+"/"+fileName);
						if(imageObj == null) {
							InventoryImage vImage = new InventoryImage();
							vImage.productId = productId;
							vImage.imgName = fileName;
							vImage.path = "/"+locationIdNew+"/"+productId+"/"+fileName;
							vImage.thumbPath = "/"+locationIdNew+"/"+productId+"/"+"thumbnail_"+fileName;
							//vImage.user = userObj;
							vImage.locations = Location.findById(Long.valueOf(locationIdNew));
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
		  
		  public static Result deleteInventoryImage(Long id) {
			    	AuthUser user = (AuthUser) getLocalUser();
			    	InventoryImage image = InventoryImage.findById(id);
			    	File file = new File(rootDir+image.path);
			    	File thumbFile = new File(rootDir+image.thumbPath);
			    	file.delete();
			    	thumbFile.delete();
			    	image.delete();
			    	return ok();
		    }
		  
		  
	  public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
			return user;
		}

	
}