package controllers;

import models.CustomizationForm;
import models.Location;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.CreateFormVM;

import com.fasterxml.jackson.databind.JsonNode;

public class customizationController extends Controller {

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

	public static Result getLeadCrateForm() {
		JsonNode json = request().body().asJson();
		
		CustomizationForm cForm = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")),json.findPath("formType").textValue());
		if(cForm == null){
			CustomizationForm cust = new CustomizationForm();
			cust.setDataType(json.findPath("formType").textValue());
			cust.setJsonData(json.findPath("jsonform").toString());
			cust.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			cust.save();
		}else{
			//cForm.setDataType("Create Lead");
			cForm.setJsonData(json.findPath("jsonform").toString());
			//cForm.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			cForm.update();
		}
		
		return ok();
	}
	
	public static Result getLeadCrateFormTitle() {
		
		Form<CreateFormVM> form = DynamicForm.form(CreateFormVM.class).bindFromRequest();
		CreateFormVM vm = form.get();
		
		CustomizationForm cForm = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")),vm.formType);
		if(cForm == null){
			CustomizationForm cust = new CustomizationForm();
			cust.setMainTitle(vm.mainTitle);
			cust.setSearchTitle(vm.searchTitle);
			cust.setSearchSubTitle(vm.searchSubTitle);
			cust.setLeadTypeTitle(vm.leadTypeTitle);
			cust.setDataType("Create Lead");
			cust.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			cust.save();
		}else{
			cForm.setMainTitle(vm.mainTitle);
			cForm.setSearchTitle(vm.searchTitle);
			cForm.setSearchSubTitle(vm.searchSubTitle);
			cForm.setLeadTypeTitle(vm.leadTypeTitle);
			cForm.update();
		}
		//JsonNode json = request().body().asJson();
		
		/*CustomizationForm cForm = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")),"Create Lead");
		if(cForm == null){
			CustomizationForm cust = new CustomizationForm();
			cust.setDataType("Create Lead");
			cust.setJsonData(json.toString());
			cust.setLocations(Location.findById(Long.valueOf(session("USER_LOCATION"))));
			cust.save();
		}else{
			cForm.setJsonData(json.toString());
			cForm.update();
		}*/
		
		return ok();
	}
	
	
	public static Result getCustomizationform(String type) {
		CustomizationForm cForm = CustomizationForm.findByLocationsAndType(Long.valueOf(session("USER_LOCATION")),type);
		if(cForm == null){
			int flag = 0;
			return ok(Json.toJson(flag));
		}
		return ok(Json.toJson(cForm));
	}
	
	
}