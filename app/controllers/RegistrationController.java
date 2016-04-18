package controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.HttpsURLConnection;

import models.ActionAdd;
import models.AuthUser;
import models.Blog;
import models.ClickyVisitorsList;
import models.Comments;
import models.Contacts;
import models.FeaturedImage;
import models.FeaturedImageConfig;
import models.FollowBrand;
import models.GroupTable;
import models.HeardAboutUs;
import models.LeadsDateWise;
import models.Location;
import models.MyProfile;
import models.NewsletterDate;
import models.Permission;
import models.PlanLocationTotal;
import models.PlanSalesTotal;
import models.PlanSchedule;
import models.PlanScheduleMonthlyLocation;
import models.PlanScheduleMonthlySalepeople;
import models.PremiumLeads;
import models.PriceAlert;
import models.PriceChange;
import models.Registration;
import models.RequestMoreInfo;
import models.SalesPlanSchedule;
import models.ScheduleTest;
import models.Site;
import models.SiteContent;
import models.SiteLogo;
import models.SliderImage;
import models.SliderImageConfig;
import models.SoldContact;
import models.ToDo;
import models.TradeIn;
import models.UserNotes;
import models.Vehicle;
import models.VehicleAudio;
import models.VehicleImage;
import models.VehicleImageConfig;
import models.Video;
import models.VirtualTour;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import scheduler.NewsLetter;
import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;
import viewmodel.AssignToVM;
import viewmodel.AudioVM;
import viewmodel.BarChartVM;
import viewmodel.BlogVM;
import viewmodel.ContactsVM;
import viewmodel.DateAndValueVM;
import viewmodel.EditImageVM;
import viewmodel.HeardAboutUsVm;
import viewmodel.ImageVM;
import viewmodel.InfoCountVM;
import viewmodel.LeadDateWiseVM;
import viewmodel.LeadVM;
import viewmodel.LocationMonthPlanVM;
import viewmodel.LocationVM;
import viewmodel.LocationWiseDataVM;
import viewmodel.NoteVM;
import viewmodel.PageVM;
import viewmodel.PinVM;
import viewmodel.PlanScheduleVM;
import viewmodel.PriceChangeVM;
import viewmodel.PriceFormatDate;
import viewmodel.RegisterVM;
import viewmodel.RequestInfoVM;
import viewmodel.SalepeopleMonthPlanVM;
import viewmodel.ScheduleTestVM;
import viewmodel.SetPriceChangeFlag;
import viewmodel.SiteContentVM;
import viewmodel.SiteLogoVM;
import viewmodel.SiteVM;
import viewmodel.SoldContactVM;
import viewmodel.SpecificationVM;
import viewmodel.ToDoVM;
import viewmodel.TradeInVM;
import viewmodel.UserLeadVM;
import viewmodel.UserNoteVM;
import viewmodel.UserVM;
import viewmodel.VehicleVM;
import viewmodel.VideoVM;
import viewmodel.VirtualTourVM;
import viewmodel.bodyStyleSetVM;
import viewmodel.profileVM;
import viewmodel.sendDataVM;
import viewmodel.sendDateAndValue;
import views.html.agreement;
import views.html.home;
import views.html.index;
import akka.actor.ActorSystem;
import au.com.bytecode.opencsv.CSVWriter;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.google.api.services.tasks.model.Tasks;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mnt.dataone.Equipment;
import com.mnt.dataone.InstalledEquipment;
import com.mnt.dataone.Option;
import com.mnt.dataone.OptionalEquipment;
import com.mnt.dataone.ResponseData;
import com.mnt.dataone.Specification;
import com.mnt.dataone.Specification_;
import com.mnt.dataone.Value;

public class RegistrationController extends Controller {
  
	final static String rootDir = Play.application().configuration()
			.getString("image.storage.path");
	final static String pdfRootDir = Play.application().configuration()
			.getString("pdfRootDir");
	
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
			
	
	public static Result getRegistrList() {
    	if(session("USER_KEY") == null || session("USER_KEY") == "") {
    		return ok(home.render(""));
    	} else {
	    	AuthUser user = (AuthUser) getLocalUser();
	    	
	    	List<Registration> regi = Registration.getPending();
	    	
	    	
	    	return ok(Json.toJson(regi));
    	}	
    }
	
	 public static AuthUser getLocalUser() {
	    	String id = session("USER_KEY");
	    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
	    	//AuthUser user = getLocalUser();
			return user;
		}

}