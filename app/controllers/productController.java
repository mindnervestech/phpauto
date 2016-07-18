package controllers;

import java.util.ArrayList;
import java.util.List;

import models.AddCollection;
import models.AuthUser;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.AddCollectionVM;




public class productController extends Controller {
  
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
			
	static String simulatevin = "{    'success': true,    'specification': {        'vin': 'WDDNG7KB7DA494890',        'year': '2013',        'make': 'Mercedes-Benz',        'model': 'S-Class',        'trim_level': 'S65 AMG',        'engine': '6.0L V12 SOHC 36V TURBO',        'style': 'SEDAN 4-DR',        'made_in': 'GERMANY',        'steering_type': 'R&P',        'anti_brake_system': '4-Wheel ABS',        'tank_size': '23.80 gallon',        'overall_height': '58.00 in.',        'overall_length': '206.50 in.',        'overall_width': '73.70 in.',        'standard_seating': '5',        'optional_seating': null,        'highway_mileage': '19 miles/gallon',        'city_mileage': '12 miles/gallon'    },    'vin': 'WDDNG7KB7DA494890'}";

	private static boolean simulate = false;
    
/*	public static Result getSectionCollection(Long id){
		
		
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
			return ok(home.render("",userRegistration));
		} else {
			AuthUser userObj = (AuthUser) getLocalUser();
			Sections section = Sections.findById(id);
			List<AddCollectionVM> collectionVM = new ArrayList<>();
			List<AddCollection> collection = AddCollection.getAllByName(section.title);
			
			for (AddCollection add : collection) {
				AddCollectionVM vm =new AddCollectionVM();
				System.out.println(add.id);
				vm.id = add.id;
				vm.title = add.title;
				collectionVM.add(vm);
			}
			return ok(Json.toJson(collectionVM));
		}
	}*/
	
	 public static Result getCollectionList(){
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	List collection = new ArrayList<>();
			List<AddCollection> listData = AddCollection.findAllCollection();
	    	List<AddCollectionVM> proList = new ArrayList<>();
	    	List<AddCollectionVM> readyList = new ArrayList<>();
	    	List<AddCollectionVM> sectionList = new ArrayList<>();
	    	for(AddCollection coll: listData) {
	    		if(coll.section.equals("readymade")){
	    			AddCollectionVM vm = new AddCollectionVM();
	    			vm.title = coll.title;
	    			vm.id = coll.id;
	    			readyList.add(vm);
	    		}else if(coll.section.equals("product")){
	    			AddCollectionVM vm = new AddCollectionVM();
	    			vm.title = coll.title;
	    			vm.id = coll.id;
	    			proList.add(vm);
	    		}else if(coll.section.equals("accessories") || coll.section.equals("disable")){
	    			
	    		}else{
	    			AddCollectionVM vm = new AddCollectionVM();
	    			vm.title = coll.title;
	    			vm.id = coll.id;
	    			sectionList.add(vm);
	    		}
	    	}
	    	collection.add(readyList);
	    	collection.add(proList);
	    	collection.add(sectionList);
			return ok(Json.toJson(collection));
	    
	    }
	 
	/* public static Result getSections(){
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
				return ok(home.render("",userRegistration));
			} else {
				
				List<SectionsVM> list = new ArrayList<>();
				List<Sections> sections = Sections.findAll();
				for (Sections sec : sections) {
					SectionsVM vm =new SectionsVM();
					vm.id = sec.id;
					vm.value = sec.value;
					vm.title = sec.title;
					list.add(vm);
				}
				return ok(Json.toJson(list));
			}
		}*/
	
	
	 public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
			return user;
		}
	 
	 
	/* public static Result addProduct(){
			MultipartFormData body = request().body().asMultipartFormData();
	    	Form<AddProductVM> form = DynamicForm.form(AddProductVM.class).bindFromRequest();
	    	AddProductVM vm = form.get();
	    	FilePart pdfFile = null ;
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();
	    	AddProductVM productVM = new AddProductVM();
	    	if(body != null){
	    		List<FilePart> filePart =  body.getFiles();
	    		if (filePart != null) {
	    			if(filePart.size() > 0){
	    				AddProduct add = new AddProduct ();
	    				
	    	    		add.title = vm.title;
	    	    		add.description =vm.description;
	    	    		add.price = vm.price;
	    	    		add.cost = vm.cost;
	    	    		add.newFlag = vm.newFlag;
	    	    		add.primaryTitle = vm.primaryTitle;
	    	    		add.year = vm.year;
	    	    		add.designer = vm.designer;
	    				add.sale="null";
	    	    		add.collection = AddCollection.findById(vm.id);
	    	    		add.user = userObj;
	    	    		add.save();
	    	    		
	    	    		productVM.id = add.id;
	    				
	    	    		for(int i= 0; i<filePart.size(); i++){
	    	    			
			    	    		 pdfFile = filePart.get(i);
			       				 String fileName = pdfFile.getFilename().replaceAll("[-+^:,() ]","");
			       	       		 System.out.println(fileName);
			       	     		 String ext = FilenameUtils.getExtension(fileName);
			       	     		 System.out.println(ext);
			       	       		 fileName = vm.getTitle() +"_"+ fileName;
			       	       		 
			       	       	    String contentType = pdfFile.getContentType(); 
			       	       	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"product");
			       	       	    if(!fdir.exists()) {
			       	       	    	fdir.mkdir();
			       	       	    }
			       	       	    	String filePath = rootDir+File.separator+userObj.id+File.separator+"product"+File.separator+fileName;
			       	       	 try {
			       	       		 	Boolean sts = FileUtils.deleteQuietly(new File(filePath));
			       	       		 	System.out.println("delete "+sts);
			       				} catch (Exception e) {
			       					e.printStackTrace();
			       				}
			       	       	    File file = pdfFile.getFile();
			       	       	    try {
			       	       	    	
			       	       	    	if(ext.equalsIgnoreCase("pdf")){
			       	       	    		FileUtils.moveFile(file, new File(filePath));
			       		    		   		AddProduct obj = AddProduct.findById(productVM.id);
			       		    		   		obj.setFileName(fileName);
			       		    		   		obj.setFilePath("/"+userObj.id+"/"+"product"+"/"+fileName);
			       		    		   		obj.update();
			       		    		   		
			       	       	    	}else if(ext.equalsIgnoreCase("cad")||ext.equalsIgnoreCase("zip")||ext.equalsIgnoreCase("rar")){
			       	       	    		FileUtils.moveFile(file, new File(filePath));
			       	       	    			AddProduct obj = AddProduct.findById(productVM.id);
			       	       	    			
			       	       	    			obj.setCadfileName(fileName);
			       	       	    			obj.setCadfilePath("/"+userObj.id+"/"+"product"+"/"+fileName);
			       	       	    			obj.update();
			       	       	    	}
			       	       	    		
			       	       	    		
			       	       	  } catch (FileNotFoundException e) {
			       	     			e.printStackTrace();
			       	    	  		} catch (IOException e) {
			       	    	  			e.printStackTrace();
			       	    	  		}
	    	    		}
	    			}
	            }
	    	}else{
		   		AddProduct add = new AddProduct ();
	    		add.title = vm.title;
	    		add.description =vm.description;
	    		add.price = vm.price;
	    		add.cost = vm.cost;
	    		add.newFlag = vm.newFlag;
	    		add.primaryTitle = vm.primaryTitle;
	    		add.year = vm.year;
	    		add.designer = vm.designer;
				add.sale="null";
	    		add.collection = AddCollection.findById(vm.id);
	    		add.user = userObj;
	    		add.save();
	    		productVM.id = add.id;
	    	}
	    		
	    		return ok(Json.toJson(productVM));
	    }*/
	 
	 
	  /*public static Result removeDefaultProduct(Long newId) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	
		    	ProductImages newImage = ProductImages.findById(newId);
		    	
		    	List<ProductImages> pImages = ProductImages.getDeleteImagePath(newImage.getProduct().getId());
		    	for(ProductImages pimg:pImages){
		    		pimg.defaultImage = (false);
		    		pimg.update();
		    	}
		    	newImage.defaultImage = (true);
		    	newImage.update();
		    	return ok();
	    	}	
	    }*/
	  
	 /* public static Result deleteImage(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	ProductImages image = ProductImages.findById(id);
		    	File file = new File(rootDir+File.separator+id+"-"+user.id+File.separator+image.imageName);
		    	File thumbFile = new File(rootDir+File.separator+id+"-"+user.id+File.separator+"thumbnail_"+image.imageName);
		    	file.delete();
		    	thumbFile.delete();
		    	image.delete();
		    	return ok();
	    	}
	    }
	  
	  public static Result getImagesByProduct(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
		    	AddProduct product = AddProduct.findById(id);
		    	List<ProductImages> imageList = ProductImages.getByProduct(product);
		    	//reorderImagesForFirstTime(imageList);
		    	List<ImageVM> vmList = new ArrayList<>();
		    	for(ProductImages image : imageList) {
		    		ImageVM vm = new ImageVM();
		    		vm.id = image.id;
		    		vm.imgName = image.imageName;
		    		vm.path = image.path;
		    		vm.defaultImage = image.defaultImage;
		    		vmList.add(vm);
		    	}
		    	return ok(Json.toJson(vmList));
	    	}	
	    }
	  
	  public static Result uploadProductPhotos() {
	    	
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	MultipartFormData body = request().body().asMultipartFormData();
		    	String productId = request().getHeader("id");
		    	Long id = Long.parseLong(productId);
		    	Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
		    	String fileName = null;
		    	FilePart picture = body.getFile("file");
		    	  if (picture != null) {
		    	
		    	    fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    	    String contentType = picture.getContentType(); 
		    	    File fdir = new File(rootDir+File.separator+id+"-"+userObj.id);
		    	    if(!fdir.exists()) {
		    	    	fdir.mkdir();
		    	    }
		    	    String filePath = rootDir+File.separator+id+"-"+userObj.id+File.separator+fileName;
		    	    String thumbnailPath = rootDir+File.separator+id+"-"+userObj.id+File.separator+"thumbnail_"+fileName;
		    	    File thumbFile = new File(thumbnailPath);
		    	    File file = picture.getFile();
		    	    try {
		    	    BufferedImage originalImage = ImageIO.read(file);
		    	    Thumbnails.of(originalImage).size(150, 150).toFile(thumbFile);
		    	    File _f = new File(filePath);
					Thumbnails.of(originalImage).scale(1.0).toFile(_f);
					
					ProductImages imageObj = ProductImages.getByImagePath("/"+id+"-"+userObj.id+"/"+fileName);
					if(imageObj == null) {
						ProductImages vImage = new ProductImages();
						vImage.product = AddProduct.findById(id);
						vImage.imageName = fileName.replaceAll(" ","%20");
						vImage.path = "/"+id+"-"+userObj.id+"/"+fileName;
						vImage.path = vImage.path.replaceAll(" ","%20");
						vImage.thumbPath = "/"+id+"-"+userObj.id+"/"+"thumbnail_"+fileName;
						vImage.thumbPath = vImage.thumbPath.replaceAll(" ","%20");
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
	  
	  public static Result getImageById(Long id, String type) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	File file = null;
		    	ProductImages image = ProductImages.findById(id);
		    	if(type.equals("thumbnail")) {
			    	file = new File(rootDir+image.thumbPath.replaceAll("%20"," "));
		    	}
		    	
		    	if(type.equals("full")) {
		    		file = new File(rootDir+image.path.replaceAll("%20"," "));
		    	}
		    	return ok(file);
	    	}	
	    }
	  
	  public static Result getImageDataById(Long id) throws IOException {
		  if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser user = (AuthUser) getLocalUser();
	    		ProductImages image = ProductImages.findById(id);
		    	File file = new File(rootDir+image.path);
		    	
		    	BufferedImage originalImage = ImageIO.read(file);
		    	//FeaturedImageConfig featuredConfig = FeaturedImageConfig.findByUser(user);
		    	List<FeaturedImageConfig> featuredCon = FeaturedImageConfig.findByUser();
		    	FeaturedImageConfig featuredConfig = new FeaturedImageConfig();
		    	featuredConfig = featuredCon.get(0);
		    	ImageVM vm = new ImageVM();
				vm.id = image.id;
				vm.imgName = image.imageName;
				vm.defaultImage = image.defaultImage;
				vm.row = originalImage.getHeight();
				vm.col = originalImage.getWidth();
				vm.path = image.path;
	    		vm.height = featuredConfig.cropHeight;
	    		vm.width = featuredConfig.cropWidth;
				//vm.vin = image.vin;
		    	return ok(Json.toJson(vm));
	    	}	
	    }
	  
	  public static Result editImage() throws IOException {
		  if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AuthUser user = (AuthUser) getLocalUser();
		    	Form<EditImageVM> form = DynamicForm.form(EditImageVM.class).bindFromRequest();
		    	EditImageVM vm = form.get();
		    	
		    	ProductImages image = ProductImages.findById(vm.imageId);
		    	File file = new File(rootDir+image.path);
		    	File thumbFile = new File(rootDir+image.thumbPath);
		    	
		    	BufferedImage originalImage = ImageIO.read(file);
		    	BufferedImage croppedImage = originalImage.getSubimage(vm.x.intValue(), vm.y.intValue(), vm.w.intValue(), vm.h.intValue());
		    	Thumbnails.of(croppedImage).scale(1.0).toFile(file);
		    	
		    	Thumbnails.of(croppedImage).height(155).toFile(thumbFile);
		    	
		    	return ok();
	    	}	
	    }*/
	
	}