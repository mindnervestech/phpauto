package controllers;

import java.util.ArrayList;
import java.util.List;

import models.AddCollection;
import models.AuthUser;
import models.LeadType;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.AddCollectionVM;
import views.html.home;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class InventoryController extends Controller {

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

	public static Result getReadyMadeCollection() {
		if (session("USER_KEY") == null || session("USER_KEY") == "") {
			return ok(home.render("", userRegistration));
		} else {
			AuthUser userObj = (AuthUser) getLocalUser();
			List<AddCollection> collectionList = AddCollection
					.getAllReadyMadeCollection();

			List<AddCollectionVM> collectionVMList = new ArrayList<>();
			for (AddCollection collection : collectionList) {
				AddCollectionVM aCollectionVM = new AddCollectionVM();
				aCollectionVM.setId(collection.getId());
				aCollectionVM.setDescription(collection.getDescription());
				aCollectionVM.setSection(collection.getSection());
				aCollectionVM.setTitle(collection.getTitle());
				aCollectionVM.setPath(collection.getPath());
				aCollectionVM.setWebsite(collection.getWebsite());
				aCollectionVM.setImage_name(collection.getImageName());
				collectionVMList.add(aCollectionVM);

			}
			return ok(Json.toJson(collectionVMList));
		}

	}
	
	public static Result saveCollectionOrder(){
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
			return ok(home.render("",userRegistration));
		} else {
			System.out.println("success");
			JsonNode nodes = ctx().request().body().asJson();
			for(int i=0; i< nodes.size(); i++){
	    			ObjectNode obj = (ObjectNode) nodes.get(i);
	    			long id = Long.parseLong(obj.get("id").toString());
	    			//long ord = Long.parseLong(obj.get("collectionOrder").toString());
	    			
	    			AddCollection cl = AddCollection.findById(id);
	    			if(cl !=null){
	    				cl.setCollectionOrder(i);
	    				cl.update();
	    			}
			}
			return ok();
		}
	}
	
	/*public static Result getAllSection() {
		if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   	return ok(home.render("",userRegistration));
		   	} else {
		   		List<Sections> sec = Sections.findAllSection();
		   		List<SectionsVM> vmList = new ArrayList<>();
		   		for (Sections sections : sec) {
					SectionsVM vm = new SectionsVM();
					vm.id = sections.id;
					vm.title = sections.title;
					vm.value = sections.value;
					vmList.add(vm);
				}
		   		return ok(Json.toJson(vmList));
		   	}
	}
	
	   public static Result deleteCollectionById(Long id ){
		   	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   		return ok(home.render("",userRegistration));
		   	} else {
		   		System.out.println("in collection Delete");
		   			AuthUser user = (AuthUser) getLocalUser();
			    	AddCollection vmcoll = AddCollection.findById(id);
			    	List<AddProduct> aProduct = AddProduct.findProductsByCollection(vmcoll);
			    	for(AddProduct ap:aProduct){
			    		 AddProduct vm = AddProduct.findById(ap.id);
			    		 if(vm != null){
			    				String p = vm.filePath;	    		
			    				List<ProductImages> img = ProductImages.getByProduct(vm);
			    				for (ProductImages productImages : img) {
			    					
			    					File file = new File(rootDir+File.separator+id+"-"+user.id+File.separator+productImages.imageName);
			    			    	File thumbFile = new File(rootDir+File.separator+id+"-"+user.id+File.separator+"thumbnail_"+productImages.imageName);
			    			    	file.delete();
			    			    	thumbFile.delete();
			    			    	productImages.delete();
			    				}
			    				
			    		 }
			    		 vm.delete();
			    	}
			    	
			    	File file = new File(rootDir+File.separator+id+"collection"+File.separator+vmcoll.imageName);
			    	vmcoll.delete();
			    	
			    	return ok();
		   	}	
		   }
	   
	   public static Result getAllSale() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
		    	List<AddCollection> collectionList = AddCollection.getAllReadyMade();
		    	List<AddProductVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddProduct> productList = AddProduct.findProductsByCollectionSale(collection);
		    		
		    		for(AddProduct product : productList) {
		    			AddProductVM vm = new AddProductVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.sale = product.sale;
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    	    	vm.countImages = ProductImages.countImage(product.id);
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));
	    	}
	    	
	    }
	   
	   public static Result moveProductById(Long id ){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	AddProduct vm = AddProduct.findById(id);
		    	AuthUser user = (AuthUser) getLocalUser();
		    	AddProduct add = AddProduct.findById(vm.id);
		    	add.setSale("null");
	    		add.update();
				return ok();
	    	}
	 
	    
	      }
	   
	   public static Result saleProductById(Long id ){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		
		    	AddProduct vm = AddProduct.findById(id);
		    	AuthUser user = (AuthUser) getLocalUser();
		    	
		    	AddProduct add = AddProduct.findById(vm.id);
		    	add.setSale("sale");
	    		add.update();
				return ok();
	    	}
	 
	    
	      }
	   
	   public static Result getAllReadyMade() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
		    	List<AddCollection> collectionList = AddCollection.getAllReadyMade();
		    	List<AddProductVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddProduct> productList = AddProduct.findProductsReadyMadeByCollection(collection);
		    		for(AddProduct product : productList) {
		    			AddProductVM vm = new AddProductVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.countImages = ProductImages.countImage(product.id);
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));
	    	}
	    	
	    }
	   
	   public static Result updateProduct(){
	    	
	    	MultipartFormData body = request().body().asMultipartFormData();
	    	Form<AddProductVM> form = DynamicForm.form(AddProductVM.class).bindFromRequest();
	    	AddProductVM vm = form.get();
	    	System.out.println(vm.getId());
	    	FilePart picture = null ;
	    	
	    	if(body != null){
	    		List<FilePart> filePart =  body.getFiles();
	    		if (filePart != null) {
	    			if(filePart.size() > 0){
	    				AddProduct add = AddProduct.findById(vm.getId());
	    				add.setTitle(vm.getTitle());
	            		add.setPrimaryTitle(vm.getPrimaryTitle());
	            		add.setYear(vm.getYear());
	            		add.setDesigner(vm.getDesigner());
	            		add.setDescription(vm.getDescription());
	            		add.setCollection(AddCollection.findById(vm.getCollectionId()));
	            		//add.setFileName(fileName);
	            		add.setNewFlag(vm.getNewFlag());
	            		add.setPrice(vm.getPrice());
	            		add.setCost(vm.getCost());
	            		//add.setFilePath("/"+userObj.id+"/"+"product"+"/"+fileName);
	            		add.update();
	    				
	    				System.out.println("price="+vm.getPrice());
	    		    	System.out.println("File Name="+vm.getFileName());
	    		    	AuthUser userObj = (AuthUser) getLocalUser();
	    		    	
	    		    	for(int i= 0; i<filePart.size(); i++){
	    		    		picture = filePart.get(i);
	    		    		String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");;
	    		      		fileName = vm.getTitle() +"_"+ fileName;
	    		      		String ext = FilenameUtils.getExtension(fileName);
		       	     		System.out.println(ext);
	    		      		
	    		      		String contentType = picture.getContentType();
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
	    		      		
	    		       	    File file = picture.getFile();
	    		       	 try {
		    		       		if(ext.equalsIgnoreCase("pdf")){
		       	       	    		FileUtils.moveFile(file, new File(filePath));
		       		    		   		add.setFileName(fileName);
		       		    		   		add.setFilePath("/"+userObj.id+"/"+"product"+"/"+fileName);
		       		    		   		add.update();
		       		    		   		
		       	       	    	}else if(ext.equalsIgnoreCase("cad")||ext.equalsIgnoreCase("zip")||ext.equalsIgnoreCase("rar")){
		       	       	    		FileUtils.moveFile(file, new File(filePath));
		       	       	    			add.setCadfileName(fileName);
		       	       	    			add.setCadfilePath("/"+userObj.id+"/"+"product"+"/"+fileName);
		       	       	    			add.update();
		       	       	    	}
	    		        		FileUtils.moveFile(file, new File(filePath));
	    					} catch (Exception e) {
	    						e.printStackTrace();
	    					}
	    		    	}
	    			}
	    		}
	    	}
	    		
				return ok();
	      } */
	   
	   
	   /*public static Result deleteProductById(Long id ){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		List<ProductImages> pImages = ProductImages.getDeleteImagePath(id);
	    		if(pImages.size() != 0){
	    			for(ProductImages pI:pImages){
	    				pI.delete();
	    			}
	    		}
		    	AddProduct vm = AddProduct.findById(id);
	    
		    	AuthUser user = (AuthUser) getLocalUser();
		    	if(vm != null){
		    		
		    		vm.delete();
		    		File file = new File(rootDir+File.separator+vm.id+"-"+user.id);
		    		file.delete();
		    	}
		    	return ok();
	    	}	
	    }*/
	   
	   public static Result findLocation(){
		   return ok(Json.toJson(Long.valueOf(session("USER_LOCATION"))));
	   }
	   
	  /* public static Result getOnlyReadyMade() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
		    	List<AddCollection> collectionList = AddCollection.getAllReadyMade();
		    	List<AddProductVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddProduct> productList = AddProduct.findProductsByCollectiongetOnlyReadyMade(collection);
		    		for(AddProduct product : productList) {
		    			AddProductVM vm = new AddProductVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.countImages = ProductImages.countImage(product.id);
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));
	    	}
	    	
	    }*/
	   
	  /* public static Result getAllAccessories() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	       		return ok(home.render("",userRegistration));
	       	} else {
	       		AuthUser userObj = (AuthUser) getLocalUser();
	    	    	List<AddCollection> collectionList = AddCollection.getAllAccessories();
	    	    	
	    	    	List<AddCollectionVM> collectionVMList = new ArrayList<>(); 
	    	    	for(AddCollection collection : collectionList) {
	    	    			AddCollectionVM aCollectionVM = new AddCollectionVM();
	    	    			aCollectionVM.setId(collection.getId());
	    	    			aCollectionVM.setDescription(collection.getDescription());
	    	    			aCollectionVM.setSection(collection.getSection());
	    	    			aCollectionVM.setTitle(collection.getTitle());
	    	    			aCollectionVM.setPath(collection.getPath());
	    	    			aCollectionVM.setWebsite(collection.getWebsite());
	    	    			aCollectionVM.setImage_name(collection.getImageName());
	    	    			collectionVMList.add(aCollectionVM);
	    	    		
	    	    	}
	    	    	return ok(Json.toJson(collectionVMList));
	       	}
	    	    	
   }
	   
	   public static Result getAllProductTypes() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
		    	List<AddCollection> collectionList = AddCollection.getAllProducts();
		    	List<AddProductVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddProduct> productList = AddProduct.findProductsByCollection(collection);
		    		for(AddProduct product : productList) {
		    			AddProductVM vm = new AddProductVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.countImages = ProductImages.countImage(product.id);
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));
	    	}
	    	
	    }
	   
	   public static Result getProductCollectionId(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
	    		AddProduct product = AddProduct.findById(id);
	    		AddCollection coll = AddCollection.findById(product.collection.id);
	    		return ok(Json.toJson(coll.section));
	    	}
	    }
	   public static Result getProductCollection() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	       		return ok(home.render("",userRegistration));
	       	} else {
	       		AuthUser userObj = (AuthUser) getLocalUser();
	    	    	List<AddCollection> collectionList = AddCollection.getAllProductCollection();
	    	    	
	    	    	List<AddCollectionVM> collectionVMList = new ArrayList<>(); 
	    	    	for(AddCollection collection : collectionList) {
	    	    			AddCollectionVM aCollectionVM = new AddCollectionVM();
	    	    			aCollectionVM.setId(collection.getId());
	    	    			aCollectionVM.setDescription(collection.getDescription());
	    	    			aCollectionVM.setSection(collection.getSection());
	    	    			aCollectionVM.setTitle(collection.getTitle());
	    	    			aCollectionVM.setPath(collection.getPath());
	    	    			aCollectionVM.setWebsite(collection.getWebsite());
	    	    			aCollectionVM.setImage_name(collection.getImageName());
	    	    			collectionVMList.add(aCollectionVM);
	    	    		
	    	    	}
	    	    	return ok(Json.toJson(collectionVMList));
	       	}
	    	    	
	}
	   
	   public static Result getProductData(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		AuthUser userObj = (AuthUser) getLocalUser();
	    		String sec=null;
	    		File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"product");
	    		AddProduct product = AddProduct.findById(id);
	    		AddProductVM vm =new AddProductVM();
	    		vm.id = product.id;
	    		vm.title =product.title;
	    		vm.description = product.description;
	    		vm.collectionId =product.collection.id;

	    		AddCollection cl = AddCollection.findById(vm.collectionId);
	    		if(cl!=null){
	    			if(cl.section.equalsIgnoreCase("product")){
	    				sec = "product";
	    			}else if(cl.section.equalsIgnoreCase("readymade")){
	    				sec = "readymade";
	    			}else if(cl.section.equalsIgnoreCase("accessories")){
	    				sec = "accessories";
	    			}else if(cl.section.equalsIgnoreCase("disable")){
	    				sec = "disable";
	    			}else{
	    				sec = "section";
	    			}
	    			
	    		}
	    		vm.collectionTitle = sec;
	    		vm.newFlag = product.newFlag;
	    		vm.designer = product.designer;
	    		vm.year = product.year;
	    		vm.primaryTitle = product.primaryTitle;
	    		vm.fileName = product.fileName;
	    		vm.cadfileName = product.cadfileName;
	    		String dr = fdir.toString();
	    		vm.filePath = dr+product.fileName;
	    		vm.cadfilePath = dr+product.cadfileName;
	    		vm.price = product.price;
	    		vm.cost = product.cost;
	    		return ok(Json.toJson(vm));
	    	}
	    }
	   
	   public static Result collectionList(){
	    	AuthUser userObj = (AuthUser) getLocalUser();
			List<AddCollection> listData = AddCollection.findAllCollection();
	    	List<AddCollectionVM> addVMList = new ArrayList<>();
	    	for(AddCollection coll: listData) {
	    		if(coll.section.equals("readymade") || coll.section.equals("product")){
	    			AddCollectionVM vm = new AddCollectionVM();
	    			vm.title = coll.title;
	    			vm.id = coll.id;
	    			addVMList.add(vm);
	    		}
	    	}
			return ok(Json.toJson(addVMList));
	    
	    }

	   public static Result updateProductInfo(){
	    	
	    	Form<AddProductVM> form = DynamicForm.form(AddProductVM.class).bindFromRequest();
	    	AddProductVM vm = form.get();
	    	
	    	AuthUser userObj = (AuthUser) getLocalUser();

	       	    try {
	       	    	AddProduct add = AddProduct.findById(vm.getId());
	        		add.setTitle(vm.getTitle());
	        		add.setPrimaryTitle(vm.getPrimaryTitle());
	        		add.setYear(vm.getYear());
	        		add.setDesigner(vm.getDesigner());
	        		add.setDescription(vm.getDescription());
	        		add.setPrice(vm.getPrice());
	        		add.setCost(vm.getCost());
	        		add.setNewFlag(vm.getNewFlag());
	        		add.setCollection(AddCollection.findById(vm.getCollectionId()));
	        		add.update();
	    
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ok();
	      }
	   
	   public static Result fileDownload(Long id){
			
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
				return ok(home.render("",userRegistration));
			} else {
				AuthUser userObj = (AuthUser) getLocalUser();
				File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"product"+File.separator);
				AddProduct product = AddProduct.findById(id);
				
				String path = rootDir+product.filePath;
						
				File f = new File(path);
				
				return ok(f);
			}
		} 
	   
	   public static Result cadFileDownload(Long id){
			
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
				return ok(home.render("",userRegistration));
			} else {
				AuthUser userObj = (AuthUser) getLocalUser();
				File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"product"+File.separator);
				AddProduct product = AddProduct.findById(id);
				String path = rootDir+product.cadfilePath;
						
				File f = new File(path);
				
				return ok(f);
			}
		} 
	   
	   
	   public static Result getAllCollection() {
		   	if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   		return ok(home.render("",userRegistration));
		   	} else {
		   		AuthUser userObj = (AuthUser) getLocalUser();
			    	List<AddCollection> collectionList = AddCollection.findAllcollection();
			    	
			    	List<AddCollectionVM> collectionVMList = new ArrayList<>(); 
			    	for(AddCollection collection : collectionList) {
			    			AddCollectionVM aCollectionVM = new AddCollectionVM();
			    			aCollectionVM.setId(collection.getId());
			    			aCollectionVM.setDescription(collection.getDescription());
			    			aCollectionVM.setSection(collection.getSection());
			    			aCollectionVM.setTitle(collection.getTitle());
			    			aCollectionVM.setPath(collection.getPath());
			    			collectionVMList.add(aCollectionVM);
			    		
			    	}
			    	return ok(Json.toJson(collectionVMList));
		   	}
		   	
		   }
	   
	   public static Result getCollectionData(Long id) {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		
	    		AddCollection collection = AddCollection.findById(id);
	    		AddCollectionVM vm =new AddCollectionVM();
	    		vm.setTitle(collection.getTitle());
	    		vm.setDescription(collection.getDescription());
	    		vm.setSection(collection.getSection());
	    		vm.setNewFlag(collection.getNewFlag());
	    		vm.setImage_name(collection.getImageName());
	    		vm.setWebsite(collection.getWebsite());
	    		vm.setThumbnailImageName(collection.thumbnailImageName);
	    		vm.setThumbnailPath(collection.thumbnailPath);
	    		vm.setThumbnailThumbPath(collection.thumbnailThumbPath);
	    		return ok(Json.toJson(vm));
	    	}
	    
	    }
	   
	   public static Result updateCollection() {
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    		
	    	} else {
		    	MultipartFormData body = request().body().asMultipartFormData();
		    	DynamicForm dynamicForm = Form.form().bindFromRequest();
		    	Form<AddCollectionVM> form = DynamicForm.form(AddCollectionVM.class).bindFromRequest();
		    	AddCollectionVM vm = form.get();
		    	Long value = vm.id;
		    
		    	AuthUser userObj = (AuthUser) getLocalUser();
		    	if(body != null){
		    	FilePart picture = body.getFile("file0");
		    	  String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    	  if (picture != null) {
		    	    String contentType = picture.getContentType(); 
		    	    
		    	    File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"collection");
		    	    if(!fdir.exists()) {
		    	    	fdir.mkdir();
		    	    }
		    	    
		    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+fileName;
		    	    String thumbnailPath =rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+"thumbnail_"+fileName;
		    	    File thumbFile = new File(thumbnailPath);
		    	    File file = picture.getFile();
		    	    
		    	    File _f = new File(filePath);
		    		
		    	    try {
		    	    	BufferedImage originalImage = ImageIO.read(file);
						Thumbnails.of(originalImage).height(162).toFile(thumbFile);
						Thumbnails.of(originalImage).scale(1.0).toFile(_f);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		    	    AddCollection coll = AddCollection.findById(vm.id);
		    	    String pathName = "/"+userObj.id+"/"+"collection"+"/"+fileName;
		    	    String thumbPath ="/"+userObj.id+"/"+"collection"+"/"+"thumbnail_"+fileName;
		      		coll.setPath(pathName);
		      		coll.setThumbPath(thumbPath);
		      		coll.setTitle(vm.title);//.title = vm.title;
		      		coll.setDescription(vm.description);//.description =vm.description;
		      		coll.setSection(vm.section);//.section =vm.section;
		      		coll.setWebsite(vm.website);
		      		coll.setNewFlag(vm.newFlag);
		      		coll.setImageName(fileName);
		      		coll.update();
		    	  } 
		    	}else{
		    		AddCollection coll = AddCollection.findById(vm.id);
		    		coll.setTitle(vm.title);//.title = vm.title;
		      		coll.setDescription(vm.description);//.description =vm.description;
		      		coll.setSection(vm.section);//.section =vm.section;
		      		coll.setWebsite(vm.website);
		      		coll.setNewFlag(vm.newFlag);
		      		coll.update();
		    	}
		
		    	  return ok();
	    	}
	    }
	   
	   public static Result updateReadyCollection() {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
	    		MultipartFormData body = request().body().asMultipartFormData();
		    	Form<AddCollectionVM> form = DynamicForm.form(AddCollectionVM.class).bindFromRequest();
		    	AddCollectionVM vm = form.get();
		    	AddCollection coll = AddCollection.findById(vm.id);
		    	AuthUser userObj = (AuthUser) getLocalUser();
		    	if(body != null){
		    		coll.setTitle(vm.title);
		      		coll.setDescription(vm.description);
		      		coll.setSection(vm.section);
		      		coll.setWebsite(vm.website);
		      		coll.setNewFlag(vm.newFlag);
		      		
		    		List<FilePart> list = body.getFiles();
		    		if(list.get(0).getKey().equalsIgnoreCase("featured")){
		    			FilePart picture = list.get(0);
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"collection");
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+fileName;
			    	    String thumbnailPath =rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+"thumbnail_"+fileName;
			    	    File thumbFile = new File(thumbnailPath);
			    	    File file = picture.getFile();
			    	    File _f = new File(filePath);
			    	    try {
			    	    	BufferedImage originalImage = ImageIO.read(file);
							Thumbnails.of(originalImage).height(162).toFile(thumbFile);
							Thumbnails.of(originalImage).scale(1.0).toFile(_f);
						} catch (IOException e) {
							e.printStackTrace();
						}
				    	    String pathName = "/"+userObj.id+"/"+"collection"+"/"+fileName;
				    	    String thumbPath ="/"+userObj.id+"/"+"collection"+"/"+"thumbnail_"+fileName;
				      		coll.setPath(pathName);
				      		coll.setThumbPath(thumbPath);
				      		coll.setImageName(fileName);
		    		}
		    		if(list.get(1).getKey().equalsIgnoreCase("thumbnail")){
		    			FilePart picture = list.get(1);
		    			String fileName = picture.getFilename().replaceAll("[-+^:,() ]","");
		    			File fdir = new File(rootDir+File.separator+userObj.id+File.separator+"collection");
			    	    if(!fdir.exists()) {
			    	    	fdir.mkdir();
			    	    }
			    	    String filePath = rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+fileName;
			    	    String thumbnailPath =rootDir+File.separator+userObj.id+File.separator+"collection"+File.separator+"thumbnail_"+fileName;
			    	    File thumbFile = new File(thumbnailPath);
			    	    File file = picture.getFile();
			    	    File _f = new File(filePath);
			    	    try {
			    	    	BufferedImage originalImage = ImageIO.read(file);
							Thumbnails.of(originalImage).height(162).toFile(thumbFile);
							Thumbnails.of(originalImage).scale(1.0).toFile(_f);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	    String pathName = "/"+userObj.id+"/"+"collection"+"/"+fileName;
				    	    String thumbPath ="/"+userObj.id+"/"+"collection"+"/"+"thumbnail_"+fileName;
				      		coll.setThumbnailPath(pathName);
				      		coll.setThumbnailThumbPath(thumbPath);
				      		coll.setThumbnailImageName(fileName);
		    		}
		    		coll.update();
		    	}else{
		    		coll.setTitle(vm.title);
		      		coll.setDescription(vm.description);
		      		coll.setSection(vm.section);
		      		coll.setWebsite(vm.website);
		      		coll.setNewFlag(vm.newFlag);
		      		coll.update();
		    	}
		    	  return ok();
	    	}
	}
	   
	   
	   public static Result getImagesByCollection(Long id){
	    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
	    		return ok(home.render("",userRegistration));
	    	} else {
		    	Identity user = getLocalUser();
		    	AuthUser userObj = (AuthUser)user;
		    	//AddProduct product = AddProduct.findById(id);
		    	AddCollection imageList = AddCollection.findById(id);
		    	if(imageList.section.equalsIgnoreCase("readymade")){
		    		AddCollectionVM vm = new AddCollectionVM();
		    		vm.id = imageList.id;
		    		vm.image_name = imageList.thumbnailImageName;
		    		vm.path = imageList.thumbnailPath;
		    		vm.section = imageList.section;
		    	return ok(Json.toJson(vm));
		    	}else{
		    		AddCollectionVM vm = new AddCollectionVM();
		    		vm.id = imageList.id;
		    		vm.image_name = imageList.imageName;
		    		vm.path = imageList.path;
		    		vm.section = imageList.section;
		    	return ok(Json.toJson(vm));
		    	}
		    	//reorderImagesForFirstTime(imageList);
		    	
	    	}	
	    }
	   
	   public static Result getAllSectionTypes(Long id) {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
				return ok(home.render("",userRegistration));
			} else {
				AuthUser userObj = (AuthUser) getLocalUser();
				Sections sec = Sections.findById(id);
		    	List<AddCollection> collectionList = AddCollection.getAllBySection(sec.value);
		    	List<AddProductVM> productVMList = new ArrayList<>(); 
		    	for(AddCollection collection : collectionList) {
		    		List<AddProduct> productList = AddProduct.findProductsByCollection(collection);
		    		for(AddProduct product : productList) {
		    			AddProductVM vm = new AddProductVM();
			    		vm.collectionTitle = collection.title;
		    			vm.title = product.title;
		    			vm.description = product.description;
		    			vm.id = product.id;
		    			vm.price = product.price;
		    			vm.cost = product.cost;
		    			vm.countImages = ProductImages.countImage(product.id);
		    			ProductImages img = ProductImages.findDefaultImg(product.id);
		    			if(img !=null){
		    				vm.imgId = img.id;
		    				vm.imgPath = rootDir+img.thumbPath;
		    			}else{
		    				vm.imgPath = null;
		    			}
		    			productVMList.add(vm);
		    		}
		    	}
		    	return ok(Json.toJson(productVMList));
			}
			
		}
	   
	   
	   public static Result getSectionCollectionData(Long id) {
			if(session("USER_KEY") == null || session("USER_KEY") == "") {
		   		return ok(home.render("", userRegistration));
		   	} else {
		   		AuthUser userObj = (AuthUser) getLocalUser();
		   		
		   		Sections sec = Sections.findById(id);
			    	List<AddCollection> collectionList = AddCollection.getAllBySection(sec.value);
			    	
			    	List<AddCollectionVM> collectionVMList = new ArrayList<>(); 
			    	for(AddCollection collection : collectionList) {
			    			AddCollectionVM aCollectionVM = new AddCollectionVM();
			    			aCollectionVM.setId(collection.getId());
			    			aCollectionVM.setDescription(collection.getDescription());
			    			aCollectionVM.setSection(collection.getSection());
			    			aCollectionVM.setTitle(collection.getTitle());
			    			aCollectionVM.setPath(collection.getPath());
			    			aCollectionVM.setWebsite(collection.getWebsite());
			    			aCollectionVM.setImage_name(collection.getImageName());
			    			collectionVMList.add(aCollectionVM);
			    		
			    	}
			    	return ok(Json.toJson(collectionVMList));
		   	}
		}
	   */
	   
	   public static Result getSelectedLeadType() {
		   List<LeadType> lType = LeadType.findByLocationsAndSelected(Long.valueOf(session("USER_LOCATION")));
		   return ok(Json.toJson(lType));
	   }
	   
	public static AuthUser getLocalUser() {
		String id = session("USER_KEY");
		AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
		return user;
	}
	
	

}