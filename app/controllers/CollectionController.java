package controllers;

import java.util.Date;
import java.util.List;

import models.AddCollection;
import models.Location;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.AddCollectionVM;

public class CollectionController extends Controller {

	
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
	
	
	public static Result getAllCollectionData() {
		
		List<AddCollection> collection = AddCollection.findByLocation(Long.valueOf(session("USER_LOCATION")));
		//List<LeadType> lead = LeadType.getLeadData();
		return ok(Json.toJson(collection)); 
		
	}
	
	 public static Result addNewCollection() {
			Form<AddCollectionVM> form = DynamicForm.form(AddCollectionVM.class).bindFromRequest();
			AddCollectionVM vm=form.get();
			//AuthUser user=new AuthUser();
			Date date = new Date();
			
			
			AddCollection lead =  AddCollection.findById(vm.id);
			if(lead != null){
				
				lead.setTitle(vm.title);
				lead.update();
			}
			else{
				AddCollection coll = new AddCollection();
				   //lead.id = vm.id;
		    	  coll.title = vm.title;
		    	  coll.locations= Location.findById(Long.valueOf(session("USER_LOCATION")));
		    	  coll.save();
			}
	    	   
	    	return ok();
		}
	 
	 public static Result deleteCollectionData(Long id) {
			
			AddCollection collect = AddCollection.findById(id);
		    	
			
			collect.delete();
		    	
				return ok();
			}
}
