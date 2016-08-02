package controllers;

import java.util.Date;
import java.util.List;

import models.AddCollection;
import models.LeadType;
import models.Location;
import models.MailchimpSchedular;
import models.WebAnalytics;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.MailchimpPageVM;
import viewmodel.WebAnalyticsVM;

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
		
		public static Result savemailchimpPage(){
			
    		Form<MailchimpPageVM> form = DynamicForm.form(MailchimpPageVM.class).bindFromRequest();
    		MailchimpPageVM vm = form.get();
    		Date date = new Date();
    		
    		MailchimpSchedular lead =  MailchimpSchedular.findById(vm.id);
			if(lead == null){
				MailchimpSchedular mail=new MailchimpSchedular();
    			mail.schedularTime = vm.schedular_time;
    			mail.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
    			mail.currDate = date;
    			mail.save();
				
				
			}
			else{
				lead.setSchedularTime(vm.schedular_time);
				lead.setCurrDate(date);
				lead.update();
    			
			}
    		return ok();
    	}
		
		public static Result getmailchimpData() {
			
			MailchimpSchedular lead = MailchimpSchedular.findByLocations(Long.valueOf(session("USER_LOCATION")));
			
			return ok(Json.toJson(lead)); 
			
		}
		
}
