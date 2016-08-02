package controllers;

import models.Domain;
import models.Location;
import models.WebAnalytics;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import securesocial.core.Identity;
import viewmodel.WebAnalyticsVM;
import viewmodel.domainVM;

public class ConfigPagesController extends Controller{
	
	
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
	
	
		public static Result saveWebsite(){
		
    		Form<WebAnalyticsVM> form = DynamicForm.form(WebAnalyticsVM.class).bindFromRequest();
    		WebAnalyticsVM vm = form.get();
    		
    			WebAnalytics web=new WebAnalytics();
    			web.tracking_code = vm.tracking_code;
    			web.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
    			web.save();
    		
    		
    		return ok();
    	
		}
}
