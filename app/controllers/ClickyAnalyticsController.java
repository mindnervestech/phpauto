package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.AuthUser;
import models.CampaignsVM;
import models.ClickyActionList;
import models.ClickyContentDomain;
import models.ClickyContentDownLoad;
import models.ClickyContentEvent;
import models.ClickyContentExit;
import models.ClickyContentMedia;
import models.ClickyEntranceList;
import models.ClickyPagesActionList;
import models.ClickyPagesList;
import models.ClickyPlatformBrowser;
import models.ClickyPlatformHardware;
import models.ClickyPlatformOperatingSystem;
import models.ClickyPlatformScreen;
import models.ClickySearchesEngine;
import models.ClickySearchesKeyword;
import models.ClickySearchesNewest;
import models.ClickySearchesRanking;
import models.ClickySearchesRecent;
import models.ClickySearchesSearch;
import models.ClickyVisitorActiveVisitor;
import models.ClickyVisitorEngagementAction;
import models.ClickyVisitorEngagementTime;
import models.ClickyVisitorTrafficSource;
import models.ClickyVisitorsList;
import models.Location;
import models.PriceAlert;
import models.PriceChange;
import models.RequestMoreInfo;
import models.SchedularDate;
import models.ScheduleTest;
import models.TradeIn;
import models.Vehicle;

import org.apache.commons.lang.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import viewmodel.CampaignsVMs;
import viewmodel.ClickyContentVM;
import viewmodel.ClickyPagesVM;
import viewmodel.ClickyPlatformVM;
import viewmodel.PriceChangeVM;
import viewmodel.PriceFormatDate;
import viewmodel.SetPriceChangeFlag;
import viewmodel.bodyStyleSetVM;
import viewmodel.sendDataVM;
import viewmodel.sendDateAndValue;

import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;

public class ClickyAnalyticsController extends Controller{

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
	
	 private static String callClickAPI(String params) {
	    	StringBuffer response = new StringBuffer();
	    	try {
	    		String url = "https://api.clicky.com/api/stats/4?output=json&site_id=100875513&sitekey=d6e7550038b4a34c"+params;
			
	    		URL obj = new URL(url);
	    		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	    		con.setRequestMethod("GET");
	    		con.setRequestProperty("User-Agent", USER_AGENT);
	    		BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
	    		String inputLine;
	    		while ((inputLine = in.readLine()) != null) {
	    			response.append(inputLine);
	    		}
	    		in.close();
	    	} catch(Exception e) {}
	    	return response.toString();
	    }
	
	
	 public static Result getClickyVisitorList(){
	    	Long id = 1L;
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MMM-dd");
	    	SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
	    	List<Location> locations = Location.findAllData();
	    	
				SqlRow maxDate = ClickyVisitorsList.getMaxDate();
	    	    System.out.println(maxDate.get("maxdate"));
	    	     Date curr = new Date();
	    	     Long currtime = curr.getTime();
	    	     
	    	    String sDate = df.format(curr);
	    	    Date newcurrDate = null;
				try {
					newcurrDate = df.parse(sDate);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    	    
	    	           Date sampleDate=(Date) maxDate.get("maxdate");
	    		     	System.out.println(maxDate.get("maxdate"));
	    		     	
	    		     	GregorianCalendar gcal = new GregorianCalendar();
	    				gcal.setTime(sampleDate);
	    				while (gcal.getTime().before(newcurrDate)) {
	    				    Date dateClicky = gcal.getTime();
	    				    sDate=df.format(dateClicky);
	    				   // startDateForList=d;
	    				    System.out.println(sDate);
	    	        
	    	          
	               Date startDateForList=null;
	           try {
	        	 startDateForList=df.parse(sDate);
				curr=df.parse(sDate);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        	String params = null;
	        	String paramsPages = null;
	        	String paramsAction = null;
	            	params = "&type=visitors-list&date="+sDate+"&limit=all";
	        	JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArray.length();i++){
						
						List<ClickyVisitorsList> cVisitorsLists = ClickyVisitorsList.getClickyUnikue(jsonArray.getJSONObject(i).get("uid").toString(),jsonArray.getJSONObject(i).get("session_id").toString());
						
						if(cVisitorsLists.size() == 0){
							String geolocation = null;
							String referrer_domain = null;
							String referrer_url = null;
							String operating_system = null;
							String web_browser = null;
							String organization = null;
							String data = jsonArray.getJSONObject(i).get("time").toString();
			    			String data1 = jsonArray.getJSONObject(i).get("time_pretty").toString();
			    			ClickyVisitorsList cVisitorsList = new ClickyVisitorsList();
			    			try{
			    				cVisitorsList.setTime(jsonArray.getJSONObject(i).get("time").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    	   			cVisitorsList.setTimePretty(jsonArray.getJSONObject(i).get("time_pretty").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setTimeTotal(jsonArray.getJSONObject(i).get("time_total").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setIpAddress(jsonArray.getJSONObject(i).get("ip_address").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setUid(jsonArray.getJSONObject(i).get("uid").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setSessionId(jsonArray.getJSONObject(i).get("session_id").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setActions(jsonArray.getJSONObject(i).get("actions").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setTotalVisits(jsonArray.getJSONObject(i).get("total_visits").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setLandingPage(jsonArray.getJSONObject(i).get("landing_page").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setWebBrowser(jsonArray.getJSONObject(i).get("web_browser").toString());
			    				web_browser=jsonArray.getJSONObject(i).get("web_browser").toString();
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setOperatingSystem(jsonArray.getJSONObject(i).get("operating_system").toString());
			    				operating_system=jsonArray.getJSONObject(i).get("operating_system").toString();
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			
			    			try{
			    				cVisitorsList.setScreenResolution(jsonArray.getJSONObject(i).get("screen_resolution").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setJavascript(jsonArray.getJSONObject(i).get("javascript").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setLanguage(jsonArray.getJSONObject(i).get("language").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setReferrerUrl(jsonArray.getJSONObject(i).get("referrer_url").toString());
			    				referrer_url=jsonArray.getJSONObject(i).get("referrer_url").toString();
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				
			    				JSONObject jsonObj = new JSONObject(jsonArray.getJSONObject(i).get("custom").toString());
			    				cVisitorsList.setCustomSessionId(jsonObj.get("session_Id").toString());
			    				
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
	                    	   cVisitorsList.setReferrerType(jsonArray.getJSONObject(i).get("referrer_type").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			
	                       try{
	                    	   cVisitorsList.setReferrerDomain(jsonArray.getJSONObject(i).get("referrer_domain").toString());
	                    	   referrer_domain=jsonArray.getJSONObject(i).get("referrer_domain").toString();
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			
			    			
	                       try{
			    			cVisitorsList.setGeolocation(jsonArray.getJSONObject(i).get("geolocation").toString());
			    			geolocation=jsonArray.getJSONObject(i).get("geolocation").toString();
	                       }
	                       catch(Exception e){
	                    	   e.printStackTrace();
	                       }
			    			try{
			    				cVisitorsList.setCountryCode(jsonArray.getJSONObject(i).get("country_code").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setLatitude(jsonArray.getJSONObject(i).get("latitude").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setLongitude(jsonArray.getJSONObject(i).get("longitude").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			//cVisitorsList.setHostname(jsonArray.getJSONObject(i).get("hostname").toString());
			    			try{
			    				cVisitorsList.setOrganization(jsonArray.getJSONObject(i).get("organization").toString());
			    				organization=jsonArray.getJSONObject(i).get("organization").toString();
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setStatsUrl(jsonArray.getJSONObject(i).get("stats_url").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try{
			    				cVisitorsList.setTotalVisits(jsonArray.getJSONObject(i).get("total_visits").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			try {
			    			cVisitorsList.setHostname(jsonArray.getJSONObject(i).get("hostname").toString());
			    			}
			    			catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    			String arr[] = jsonArray.getJSONObject(i).get("time_pretty").toString().split(" ");
							String arrNew[] = arr[3].split(",");
							String checkDate = arrNew[0]+"-"+arr[1]+"-"+arr[2];
							Date thedate = null;
							try {
								thedate = df1.parse(checkDate);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(geolocation != null){
							if(geolocation.contains(","))
							{
								String city = null;
								String location[]=geolocation.split(",");
					     		city=location[0];
					     		
					     		String country[]=location[1].split(" ");
					     		
					     		System.out.println(country[1]);
								
					     		try{
					     			String country1[]=location[2].split(" ");
								paramsPages = "&type=segmentation&country="+country1[1]+"&segments=summary&date="+sDate+"&limit=all";
					     		}
					     		catch(Exception e){
					     			paramsPages = "&type=segmentation&country="+country[1]+"&segments=summary&date="+sDate+"&limit=all";
					     		}
				     		
							}
							else{
								paramsPages = "&type=segmentation&country="+geolocation+"&segments=summary&date="+sDate+"&limit=all";
							}
			    			JSONArray jsonArrayvisitorspage;
			    			
			    			jsonArrayvisitorspage = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			    				//ClickyVisitorsList cPages = new ClickyVisitorsList();
			    				for(int j=0;j<jsonArrayvisitorspage.length();j++){
			    	    			
			    					
			    					
			    					if(jsonArrayvisitorspage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
			    					
			    						cVisitorsList.setAverageAction(jsonArrayvisitorspage.getJSONObject(j).get("value").toString());
			    					}
			    					
			    					if(jsonArrayvisitorspage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
				    					
			    						cVisitorsList.setAverageTime(jsonArrayvisitorspage.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayvisitorspage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
				    					
			    						cVisitorsList.setTotalTime(jsonArrayvisitorspage.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayvisitorspage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
				    					
			    						cVisitorsList.setBounceRate(jsonArrayvisitorspage.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayvisitorspage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
				    					
			    						cVisitorsList.setVisitors(jsonArrayvisitorspage.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					if(jsonArrayvisitorspage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
				    					
			    						cVisitorsList.setUniqueVisitor(jsonArrayvisitorspage.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					
			    					if(jsonArrayvisitorspage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
				    					
			    						cVisitorsList.setAction(jsonArrayvisitorspage.getJSONObject(j).get("value").toString());
			    						
			    					}
			    	    			
			    			
			    				}
						}
			    				//&type=segmentation&domain="+type+"&segments=summary&date="+startDate+","+endDate+"&limit=all
			    				if(referrer_domain != null){
			    				paramsPages = "&type=segmentation&domain="+referrer_domain+"&segments=summary&date="+sDate+"&limit=all";
			    				
			    				JSONArray jsonArrayvisitorspage1;
				    			
				    			jsonArrayvisitorspage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
				    				//ClickyVisitorsList cPages = new ClickyVisitorsList();
				    				for(int j=0;j<jsonArrayvisitorspage1.length();j++){
				    	    			
				    					
				    					
				    					if(jsonArrayvisitorspage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
					    					
				    						cVisitorsList.setAverageAction1(jsonArrayvisitorspage1.getJSONObject(j).get("value").toString());
				    					}
				    					
				    					if(jsonArrayvisitorspage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
					    					
				    						cVisitorsList.setAverageTime1(jsonArrayvisitorspage1.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					
				    					if(jsonArrayvisitorspage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
					    					
				    						cVisitorsList.setTotalTime1(jsonArrayvisitorspage1.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					
				    					if(jsonArrayvisitorspage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
					    					
				    						cVisitorsList.setBounceRate1(jsonArrayvisitorspage1.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					
				    					if(jsonArrayvisitorspage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
					    					
				    						cVisitorsList.setVisitors1(jsonArrayvisitorspage1.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					if(jsonArrayvisitorspage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
					    					
				    						cVisitorsList.setUniqueVisitor1(jsonArrayvisitorspage1.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					
				    					
				    					if(jsonArrayvisitorspage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
					    					
				    						cVisitorsList.setAction1(jsonArrayvisitorspage1.getJSONObject(j).get("value").toString());
				    						
				    					}
				    	    			
				    			
				    				}
			    				}
			    				
			    				if(referrer_url != null){
				    				String newUrl=null;
				    	    		if(referrer_url.contains("=")){
				    	    			String urlArr[]=referrer_url.split("=");
				    	    			newUrl=urlArr[1];
				    	    		}
				    	    		else{
				    	    			newUrl=referrer_url;
				    	    		}
				    	    		
				    	    		//params = "&type=segmentation&search="+newUrl+"&segments=summary&date="+endDate+","+startDate+"&limit=all";
				    				
				    				paramsPages = "&type=segmentation&search="+newUrl+"&segments=summary&date="+sDate+"&limit=all";
				    				
				    				JSONArray jsonArrayvisitorspage2;
					    			
					    			jsonArrayvisitorspage2 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					    				//ClickyVisitorsList cPages = new ClickyVisitorsList();
					    				for(int j=0;j<jsonArrayvisitorspage2.length();j++){
					    	    			
					    					
					    					
					    					if(jsonArrayvisitorspage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
						    					
					    						cVisitorsList.setAverageAction2(jsonArrayvisitorspage2.getJSONObject(j).get("value").toString());
					    					}
					    					
					    					if(jsonArrayvisitorspage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
						    					
					    						cVisitorsList.setAverageTime2(jsonArrayvisitorspage2.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
						    					
					    						cVisitorsList.setTotalTime2(jsonArrayvisitorspage2.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
						    					
					    						cVisitorsList.setBounceRate2(jsonArrayvisitorspage2.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
						    					
					    						cVisitorsList.setVisitors2(jsonArrayvisitorspage2.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					if(jsonArrayvisitorspage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
						    					
					    						cVisitorsList.setUniqueVisitor2(jsonArrayvisitorspage2.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					
					    					if(jsonArrayvisitorspage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
						    					
					    						cVisitorsList.setAction2(jsonArrayvisitorspage2.getJSONObject(j).get("value").toString());
					    						
					    					}
					    	    			
					    			
					    				}
			    				}
			    				
			    				if(operating_system != null){
				    				paramsPages = "&type=segmentation&os="+operating_system+"&segments=summary&date="+sDate+"&limit=all";
				    				
				    				JSONArray jsonArrayvisitorspageos;
					    			
					    			jsonArrayvisitorspageos = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					    				//ClickyVisitorsList cPages = new ClickyVisitorsList();
					    				for(int j=0;j<jsonArrayvisitorspageos.length();j++){
					    	    			
					    					
					    					
					    					if(jsonArrayvisitorspageos.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
						    					
					    						cVisitorsList.setAverageActionos(jsonArrayvisitorspageos.getJSONObject(j).get("value").toString());
					    					}
					    					
					    					if(jsonArrayvisitorspageos.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
						    					
					    						cVisitorsList.setAverageTimeos(jsonArrayvisitorspageos.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspageos.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
						    					
					    						cVisitorsList.setTotalTimeos(jsonArrayvisitorspageos.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspageos.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
						    					
					    						cVisitorsList.setBounceRateos(jsonArrayvisitorspageos.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspageos.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
						    					
					    						cVisitorsList.setVisitorsos(jsonArrayvisitorspageos.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					if(jsonArrayvisitorspageos.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
						    					
					    						cVisitorsList.setUniqueVisitoros(jsonArrayvisitorspageos.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					
					    					if(jsonArrayvisitorspageos.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
						    					
					    						cVisitorsList.setActionos(jsonArrayvisitorspageos.getJSONObject(j).get("value").toString());
					    						
					    					}
					    	    			
					    			
					    				}
				    				}
			    				
			    				if(web_browser != null){
				    				paramsPages = "&type=segmentation&browser="+web_browser+"&segments=summary&date="+sDate+"&limit=all";
				    				
				    				JSONArray jsonArrayvisitorspagebrowser;
					    			
				    				jsonArrayvisitorspagebrowser = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					    				//ClickyVisitorsList cPages = new ClickyVisitorsList();
					    				for(int j=0;j<jsonArrayvisitorspagebrowser.length();j++){
					    					
					    					if(jsonArrayvisitorspagebrowser.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
						    					
					    						cVisitorsList.setAverageActionbrowser(jsonArrayvisitorspagebrowser.getJSONObject(j).get("value").toString());
					    					}
					    					
					    					if(jsonArrayvisitorspagebrowser.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
						    					
					    						cVisitorsList.setAverageTimebrowser(jsonArrayvisitorspagebrowser.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspagebrowser.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
						    					
					    						cVisitorsList.setTotalTimebrowser(jsonArrayvisitorspagebrowser.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspagebrowser.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
						    					
					    						cVisitorsList.setBounceRatebrowser(jsonArrayvisitorspagebrowser.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspagebrowser.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
						    					
					    						cVisitorsList.setVisitorsbrowser(jsonArrayvisitorspagebrowser.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					if(jsonArrayvisitorspagebrowser.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
						    					
					    						cVisitorsList.setUniqueVisitorbrowser(jsonArrayvisitorspagebrowser.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					
					    					if(jsonArrayvisitorspagebrowser.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
						    					
					    						cVisitorsList.setActionbrowser(jsonArrayvisitorspagebrowser.getJSONObject(j).get("value").toString());
					    						
					    					}
					    	    			
					    			
					    				}
				    				}
			    				
			    				if(organization != null){
			    					String org = null;
			    					if(organization.equals("At&t U-verse")){
			    						org = "At%26t U-verse";
			    					}
			    					else{
			    						org = organization;
			    					}
				    				paramsPages = "&type=segmentation&org="+org+"&segments=summary&date="+sDate+"&limit=all";
				    				
				    				JSONArray jsonArrayvisitorspageorg;
					    			
				    				jsonArrayvisitorspageorg = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					    				//ClickyVisitorsList cPages = new ClickyVisitorsList();
					    				for(int j=0;j<jsonArrayvisitorspageorg.length();j++){
					    	    			
					    					
					    					
					    					if(jsonArrayvisitorspageorg.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
						    					
					    						cVisitorsList.setAverageActionorg(jsonArrayvisitorspageorg.getJSONObject(j).get("value").toString());
					    					}
					    					
					    					if(jsonArrayvisitorspageorg.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
						    					
					    						cVisitorsList.setAverageTimeorg(jsonArrayvisitorspageorg.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspageorg.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
						    					
					    						cVisitorsList.setTotalTimeorg(jsonArrayvisitorspageorg.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspageorg.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
						    					
					    						cVisitorsList.setBounceRateorg(jsonArrayvisitorspageorg.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					if(jsonArrayvisitorspageorg.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
						    					
					    						cVisitorsList.setVisitorsorg(jsonArrayvisitorspageorg.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					if(jsonArrayvisitorspageorg.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
						    					
					    						cVisitorsList.setUniqueVisitororg(jsonArrayvisitorspageorg.getJSONObject(j).get("value").toString());
					    						
					    					}
					    					
					    					
					    					if(jsonArrayvisitorspageorg.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
						    					
					    						cVisitorsList.setActionorg(jsonArrayvisitorspageorg.getJSONObject(j).get("value").toString());
					    						
					    					}
					    	    			
					    			
					    				}
				    				}
			    				
			    			cVisitorsList.setDateClick(dateClicky);
			    			
			    			cVisitorsList.save();
						}
		    			
					}	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
				
				
				paramsPages = "&type=searches-rankings&date="+sDate+"&limit=all";
				JSONArray jsonArrayClickyRanking;
				try {
					List <ClickySearchesRanking> visitor=ClickySearchesRanking.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayClickyRanking = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayClickyRanking.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title=null;
						ClickySearchesRanking cPages = new ClickySearchesRanking();
		    			cPages.setTitle(jsonArrayClickyRanking.getJSONObject(i).get("title").toString());
		    			cPages.setValue(jsonArrayClickyRanking.getJSONObject(i).get("value").toString());
		    			try{
		    			cPages.setStatsUrl(jsonArrayClickyRanking.getJSONObject(i).get("stats_url").toString());
		    			}
		    			catch (Exception e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    			title=jsonArrayClickyRanking.getJSONObject(i).get("title").toString();
		    			//title=URLEncoder.encode(title);
	                    paramsPages = "&type=segmentation&search="+title+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    				
		    			
		    				    				
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
				
				paramsPages = "&type=searches-unique&date="+sDate+"&limit=all";
				JSONArray jsonArrayClickyNewest;
				try {
					List <ClickySearchesNewest> visitor=ClickySearchesNewest.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayClickyNewest = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayClickyNewest.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title=null;
						ClickySearchesNewest cPages = new ClickySearchesNewest();
		    			cPages.setTime(jsonArrayClickyNewest.getJSONObject(i).get("time").toString());
		    			cPages.setTimePretty(jsonArrayClickyNewest.getJSONObject(i).get("time_pretty").toString());
		    			cPages.setTitle(jsonArrayClickyNewest.getJSONObject(i).get("item").toString());
		    			try{
		    			cPages.setStatsUrl(jsonArrayClickyNewest.getJSONObject(i).get("stats_url").toString());
		    			}
		    			catch (Exception e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    			title=jsonArrayClickyNewest.getJSONObject(i).get("item").toString();
		    			title=URLEncoder.encode(title);
	                    paramsPages = "&type=segmentation&search="+title+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    				
		    			
		    				    				
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			
				
				paramsPages = "&type=searches-recent&date="+sDate+"&limit=all";
				JSONArray jsonArrayClickyRecent;
				try {
					List <ClickySearchesRecent> visitor=ClickySearchesRecent.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayClickyRecent = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayClickyRecent.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title=null;
						ClickySearchesRecent cPages = new ClickySearchesRecent();
		    			cPages.setTime(jsonArrayClickyRecent.getJSONObject(i).get("time").toString());
		    			cPages.setTimePretty(jsonArrayClickyRecent.getJSONObject(i).get("time_pretty").toString());
		    			cPages.setTitle(jsonArrayClickyRecent.getJSONObject(i).get("item").toString());
		    			try{
		    			cPages.setStatsUrl(jsonArrayClickyRecent.getJSONObject(i).get("stats_url").toString());
		    			}
		    			catch (Exception e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    			title=jsonArrayClickyRecent.getJSONObject(i).get("item").toString();
		    			title=URLEncoder.encode(title);	
	                 paramsPages = "&type=segmentation&search="+title+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    				
		    			
		    			
		    			
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				paramsPages = "&type=searches&date="+sDate+"&limit=all";
				JSONArray jsonArrayClickySearch;
				try {
					List <ClickySearchesSearch> visitor=ClickySearchesSearch.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayClickySearch = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayClickySearch.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title=null;
						ClickySearchesSearch cPages = new ClickySearchesSearch();
		    			cPages.setValue(jsonArrayClickySearch.getJSONObject(i).get("value").toString());
		    			cPages.setValuePercent(jsonArrayClickySearch.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayClickySearch.getJSONObject(i).get("title").toString());
		    			title=jsonArrayClickySearch.getJSONObject(i).get("title").toString();
		    			cPages.setStatsUrl(jsonArrayClickySearch.getJSONObject(i).get("stats_url").toString());
		    			statsUrl=jsonArrayClickySearch.getJSONObject(i).get("stats_url").toString();
		    			
		    			paramsPages = "&type=segmentation&source=searches&stats_url="+statsUrl+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    				
		    				
		    				paramsPages = "&type=segmentation&search="+title+"&segments=summary&date="+sDate+"&limit=all";
			    			
			    			JSONArray jsonArrayPage2;
			    			
			    				jsonArrayPage2 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
			    				for(int j=0;j<jsonArrayPage2.length();j++){
			    	    			
			    					
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
			    					
			    						cPages.setAverageAction1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
				    					
			    						cPages.setAverageTime1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
				    					
			    						cPages.setTotalTime1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
				    					
			    						cPages.setBounceRate1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
				    					
			    						cPages.setVisitors1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
				    					
			    						cPages.setUniqueVisitor1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
				    					
			    						cPages.setAction1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    	    			
			    			
			    				}

		    				
		    				
		    				
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				 paramsPages = "&type=searches-engines&date="+sDate+"&limit=all";
				JSONArray jsonArrayClickyEngine;
				try {
					List <ClickySearchesEngine> visitor=ClickySearchesEngine.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayClickyEngine = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayClickyEngine.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title=null;
						ClickySearchesEngine cPages = new ClickySearchesEngine();
		    			cPages.setValue(jsonArrayClickyEngine.getJSONObject(i).get("value").toString());
		    			cPages.setValuePercent(jsonArrayClickyEngine.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayClickyEngine.getJSONObject(i).get("title").toString());
		    			title=jsonArrayClickyEngine.getJSONObject(i).get("title").toString();
		    			cPages.setStatsUrl(jsonArrayClickyEngine.getJSONObject(i).get("stats_url").toString());
		    			statsUrl=jsonArrayClickyEngine.getJSONObject(i).get("stats_url").toString();
		    			
		    			paramsPages = "&type=segmentation&source=searches&title="+title+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    				
		    				
		    				paramsPages = "&type=segmentation&domain="+title+"&segments=summary&date="+sDate+"&limit=all";
			    			
			    			JSONArray jsonArrayPage2;
			    			
			    				jsonArrayPage2 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
			    				for(int j=0;j<jsonArrayPage2.length();j++){
			    	    			
			    					
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
			    					
			    						cPages.setAverageAction1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
				    					
			    						cPages.setAverageTime1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
				    					
			    						cPages.setTotalTime1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
				    					
			    						cPages.setBounceRate1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
				    					
			    						cPages.setVisitors1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
				    					
			    						cPages.setUniqueVisitor1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
				    					
			    						cPages.setAction1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    	    			
			    			
			    				}

		    				
		    				
		    				
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
				paramsPages = "&type=searches-keywords&date="+sDate+"&limit=all";
				JSONArray jsonArrayClickyKeywords;
				try {
					List <ClickySearchesKeyword> visitor=ClickySearchesKeyword.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayClickyKeywords = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayClickyKeywords.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title=null;
						ClickySearchesKeyword cPages = new ClickySearchesKeyword();
		    			cPages.setValue(jsonArrayClickyKeywords.getJSONObject(i).get("value").toString());
		    			cPages.setValuePercent(jsonArrayClickyKeywords.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayClickyKeywords.getJSONObject(i).get("title").toString());
		    			title=jsonArrayClickyKeywords.getJSONObject(i).get("title").toString();
		    			try{
		    			cPages.setStatsUrl(jsonArrayClickyKeywords.getJSONObject(i).get("stats_url").toString());
		    			statsUrl=jsonArrayClickyKeywords.getJSONObject(i).get("stats_url").toString();
		    			}
		    			catch (Exception e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    				    				
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				
				
				
				
				
				
				     paramsPages = "&type=visitors-most-active&date="+sDate+"&limit=all";
					JSONArray jsonArrayActiveVisits;
					try {
						List <ClickyVisitorActiveVisitor> visitor=ClickyVisitorActiveVisitor.getAllData(startDateForList);
						if(visitor.size() == 0){
						jsonArrayActiveVisits = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
						for(int i=0;i<jsonArrayActiveVisits.length();i++){
							ClickyVisitorActiveVisitor cPages = new ClickyVisitorActiveVisitor();
			    			cPages.setValue(jsonArrayActiveVisits.getJSONObject(i).get("value").toString());
			    			try{
			    			cPages.setValuePercent(jsonArrayActiveVisits.getJSONObject(i).get("value_percent").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			cPages.setTitle(jsonArrayActiveVisits.getJSONObject(i).get("title").toString());
			    			try{
			    			cPages.setStatsUrl(jsonArrayActiveVisits.getJSONObject(i).get("stats_url").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    				cPages.setSaveDate(dateClicky);
			    				cPages.save();
						}
					}
					}
					 catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				

				paramsPages = "&type=traffic-sources&date="+sDate+"&limit=all";
				JSONArray jsonArrayClickyTraffic;
				try {
					List <ClickyVisitorTrafficSource> visitor=ClickyVisitorTrafficSource.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayClickyTraffic = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayClickyTraffic.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title=null;
		    			ClickyVisitorTrafficSource cPages = new ClickyVisitorTrafficSource();
		    			cPages.setValue(jsonArrayClickyTraffic.getJSONObject(i).get("value").toString());
		    			cPages.setValuePercent(jsonArrayClickyTraffic.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayClickyTraffic.getJSONObject(i).get("title").toString());
		    			title=jsonArrayClickyTraffic.getJSONObject(i).get("title").toString();
		    			cPages.setStatsUrl(jsonArrayClickyTraffic.getJSONObject(i).get("stats_url").toString());
		    			statsUrl=jsonArrayClickyTraffic.getJSONObject(i).get("stats_url").toString();
		    			
		    			paramsPages = "&type=segmentation&stats_url="+statsUrl+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    				
		    				
		    				paramsPages = "&type=segmentation&source="+title+"&segments=summary&date="+sDate+"&limit=all";
			    			
			    			JSONArray jsonArrayPage2;
			    			
			    				jsonArrayPage2 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
			    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
			    				for(int j=0;j<jsonArrayPage2.length();j++){
			    	    			
			    					
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
			    					
			    						cPages.setAverageAction1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
				    					
			    						cPages.setAverageTime1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
				    					
			    						cPages.setTotalTime1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
				    					
			    						cPages.setBounceRate1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
				    					
			    						cPages.setVisitors1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
				    					
			    						cPages.setUniqueVisitor1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    					
			    					
			    					if(jsonArrayPage2.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
				    					
			    						cPages.setAction1(jsonArrayPage2.getJSONObject(j).get("value").toString());
			    						
			    					}
			    	    			
			    			
			    				}

		    				
		    				
		    				
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	    	    
				
				
				
						
			   paramsPages = "&type=engagement-times&date="+sDate+"&limit=all";
				JSONArray jsonArrayEngagementTime;
				try {
					List <ClickyVisitorEngagementTime> visitor=ClickyVisitorEngagementTime.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayEngagementTime = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayEngagementTime.length();i++){
						String url=null;
						ClickyVisitorEngagementTime cPages = new ClickyVisitorEngagementTime();
		    			cPages.setValue(jsonArrayEngagementTime.getJSONObject(i).get("value").toString());
		    			try{
		    			cPages.setValuePercent(jsonArrayEngagementTime.getJSONObject(i).get("value_percent").toString());
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			cPages.setTitle(jsonArrayEngagementTime.getJSONObject(i).get("title").toString());
		    			try{
		    			cPages.setStatsUrl(jsonArrayEngagementTime.getJSONObject(i).get("stats_url").toString());
		    			url=jsonArrayEngagementTime.getJSONObject(i).get("stats_url").toString();
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			if(url != null){
		    			String titleArr[]=url.split("&time=");
	                    if(titleArr[0].contains("-")) { 
	                    	titleArr[0]=titleArr[0].replace("-", ",");
		    			paramsPages = "&type=segmentation&actions="+titleArr[0]+"&segments=summary&date="+sDate+"&limit=all";
	                    }
	                    else{
	                    	paramsPages = "&type=segmentation&actions="+titleArr[0]+"&segments=summary&date="+sDate+"&limit=all";
	                    }
		    			paramsPages = "&type=segmentation&time="+titleArr[1]+"&segments=summary&date="+sDate+"&limit=all";
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}

		    			}
		    			
		    			
		    			
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
				}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

						
						

		 	      paramsPages = "&type=engagement-actions&date="+sDate+"&limit=all";
				JSONArray jsonArrayEngagementAction;
				try {
					List <ClickyVisitorEngagementAction> visitor=ClickyVisitorEngagementAction.getAllData(startDateForList);
					if(visitor.size() == 0){
					String title=null;
					jsonArrayEngagementAction = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayEngagementAction.length();i++){
						ClickyVisitorEngagementAction cPages = new ClickyVisitorEngagementAction();
		    			cPages.setValue(jsonArrayEngagementAction.getJSONObject(i).get("value").toString());
		    			try{
		    			cPages.setValuePercent(jsonArrayEngagementAction.getJSONObject(i).get("value_percent").toString());
		    			}
		    			catch(Exception e){
		    				cPages.setValuePercent("0");
		    				e.printStackTrace();
		    			}
		    			cPages.setTitle(jsonArrayEngagementAction.getJSONObject(i).get("title").toString());
		    			 title=jsonArrayEngagementAction.getJSONObject(i).get("title").toString();
		    			//title=URLEncoder.encode(title);
		    			try{
		    			cPages.setStatsUrl(jsonArrayEngagementAction.getJSONObject(i).get("stats_url").toString());
		    			}
		    			catch(Exception e){
		    		  		cPages.setStatsUrl("null url");	
		    				e.printStackTrace();
		    			}
		    			String titleArr[]=title.split(" ");
	                    if(titleArr[0].contains("-")) { 
	                    	titleArr[0]=titleArr[0].replace("-", ",");
		    			 paramsPages = "&type=segmentation&actions="+titleArr[0]+"&segments=summary&date="+sDate+"&limit=all";
	                    }
	                    else{
	                    	paramsPages = "&type=segmentation&actions="+titleArr[0]+"&segments=summary&date="+sDate+"&limit=all";
	                    }
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    	
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
				}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
				

				paramsPages = "&type=hardware&date="+sDate+"&limit=all";
				JSONArray jsonArrayPlatformHardware;
				try {
					List <ClickyPlatformHardware> visitor=ClickyPlatformHardware.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayPlatformHardware = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayPlatformHardware.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title = null;
		    			ClickyPlatformHardware cPages = new ClickyPlatformHardware();
		    			cPages.setValue(jsonArrayPlatformHardware.getJSONObject(i).get("value").toString());
		    			cPages.setValuePercent(jsonArrayPlatformHardware.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayPlatformHardware.getJSONObject(i).get("title").toString());
		    			title=jsonArrayPlatformHardware.getJSONObject(i).get("title").toString();
		    			title=URLEncoder.encode(title);
		    			cPages.setStatsUrl(jsonArrayPlatformHardware.getJSONObject(i).get("stats_url").toString());
		    			statsUrl=jsonArrayPlatformHardware.getJSONObject(i).get("stats_url").toString();
		    			try{
		    			cPages.setUrl(jsonArrayPlatformHardware.getJSONObject(i).get("url").toString());
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			
		    			paramsPages = "&type=segmentation&hardware="+title+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayHardwarePage;
		    			
		    			jsonArrayHardwarePage = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayHardwarePage.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayHardwarePage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction1(jsonArrayHardwarePage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayHardwarePage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime1(jsonArrayHardwarePage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayHardwarePage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime1(jsonArrayHardwarePage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayHardwarePage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate1(jsonArrayHardwarePage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayHardwarePage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors1(jsonArrayHardwarePage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayHardwarePage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor1(jsonArrayHardwarePage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayHardwarePage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction1(jsonArrayHardwarePage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    			
		    			
		    			paramsPages = "&type=segmentation&stats_url="+statsUrl+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
					}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
				
				
				
				
				paramsPages = "&type=screen-resolutions&date="+sDate+"&limit=all";
				JSONArray jsonArrayPlatformScreen;
				try {
					List <ClickyPlatformScreen> visitor=ClickyPlatformScreen.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayPlatformScreen = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayPlatformScreen.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title = null;
		    			ClickyPlatformScreen cPages = new ClickyPlatformScreen();
		    			cPages.setValue(jsonArrayPlatformScreen.getJSONObject(i).get("value").toString());
		    			cPages.setValuePercent(jsonArrayPlatformScreen.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayPlatformScreen.getJSONObject(i).get("title").toString());
		    			title=jsonArrayPlatformScreen.getJSONObject(i).get("title").toString();
		    			title=URLEncoder.encode(title);
		    			cPages.setStatsUrl(jsonArrayPlatformScreen.getJSONObject(i).get("stats_url").toString());
		    			statsUrl=jsonArrayPlatformScreen.getJSONObject(i).get("stats_url").toString();
		    			try{
		    			cPages.setUrl(jsonArrayPlatformScreen.getJSONObject(i).get("url").toString());
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			
		    			paramsPages = "&type=segmentation&resolution="+title+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPageScreen;
		    			
		    			jsonArrayPageScreen = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPageScreen.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPageScreen.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction1(jsonArrayPageScreen.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPageScreen.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime1(jsonArrayPageScreen.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPageScreen.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime1(jsonArrayPageScreen.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPageScreen.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate1(jsonArrayPageScreen.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPageScreen.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors1(jsonArrayPageScreen.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPageScreen.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor1(jsonArrayPageScreen.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPageScreen.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction1(jsonArrayPageScreen.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    			
		    			
		    			paramsPages = "&type=segmentation&stats_url="+statsUrl+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
				

				paramsPages = "&type=web-browsers&date="+sDate+"&limit=all";
				JSONArray jsonArrayPlatformBrowser;
				try {
					List <ClickyPlatformBrowser> visitor=ClickyPlatformBrowser.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayPlatformBrowser = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayPlatformBrowser.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title=null;
						
		    			ClickyPlatformBrowser cPages = new ClickyPlatformBrowser();
		    			cPages.setValue(jsonArrayPlatformBrowser.getJSONObject(i).get("value").toString());
		    			cPages.setValuePercent(jsonArrayPlatformBrowser.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayPlatformBrowser.getJSONObject(i).get("title").toString());
		    			title=jsonArrayPlatformBrowser.getJSONObject(i).get("title").toString();
		    			title=URLEncoder.encode(title);
		    			cPages.setStatsUrl(jsonArrayPlatformBrowser.getJSONObject(i).get("stats_url").toString());
		    			statsUrl=jsonArrayPlatformBrowser.getJSONObject(i).get("stats_url").toString();
		    			try{
		    			cPages.setUrl(jsonArrayPlatformBrowser.getJSONObject(i).get("url").toString());
		    			}
		    			catch(Exception e){

		    				e.printStackTrace();
		    			}
		    			
		    			paramsPages = "&type=segmentation&browser="+title+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArraybrowserpage;
		    			
		    	     		jsonArraybrowserpage = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArraybrowserpage.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArraybrowserpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						//cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						cPages.setAverageAction1(jsonArraybrowserpage.getJSONObject(j).get("value").toString());
		    					}
		    					
		    					if(jsonArraybrowserpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime1(jsonArraybrowserpage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArraybrowserpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime1(jsonArraybrowserpage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArraybrowserpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate1(jsonArraybrowserpage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArraybrowserpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors1(jsonArraybrowserpage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArraybrowserpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor1(jsonArraybrowserpage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArraybrowserpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction1(jsonArraybrowserpage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    			
		    				}
		    			
		    			
		    			
		    			
		    			paramsPages = "&type=segmentation&stats_url="+statsUrl+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
					paramsPages = "&type=operating-systems&date="+sDate+"&limit=all";
				JSONArray jsonArrayPlatformOperatingSystem;
				try {
					List <ClickyPlatformOperatingSystem> visitor=ClickyPlatformOperatingSystem.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayPlatformOperatingSystem = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayPlatformOperatingSystem.length();i++){
						String locString = null;
						Location locationName = null;
						String statsUrl=null;
						String title=null;
						ClickyPlatformOperatingSystem cPages = new ClickyPlatformOperatingSystem();
		    			cPages.setValue(jsonArrayPlatformOperatingSystem.getJSONObject(i).get("value").toString());
		    			cPages.setValuePercent(jsonArrayPlatformOperatingSystem.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayPlatformOperatingSystem.getJSONObject(i).get("title").toString());
		    			title=jsonArrayPlatformOperatingSystem.getJSONObject(i).get("title").toString();
		    			title=URLEncoder.encode(title);
		    			cPages.setStatsUrl(jsonArrayPlatformOperatingSystem.getJSONObject(i).get("stats_url").toString());
		    			statsUrl=jsonArrayPlatformOperatingSystem.getJSONObject(i).get("stats_url").toString();
		    			try{
		    			cPages.setUrl(jsonArrayPlatformOperatingSystem.getJSONObject(i).get("url").toString());
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			
		    			
		    			paramsPages = "&type=segmentation&os="+title+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayOSPage;
		    			
		    			jsonArrayOSPage = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayOSPage.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayOSPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction1(jsonArrayOSPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayOSPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime1(jsonArrayOSPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayOSPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime1(jsonArrayOSPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayOSPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate1(jsonArrayOSPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayOSPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors1(jsonArrayOSPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayOSPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor1(jsonArrayOSPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayOSPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction1(jsonArrayOSPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    			
		    			
		    			
		    			paramsPages = "&type=segmentation&stats_url="+statsUrl+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    				cPages.setSaveDate(dateClicky);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				
				
			paramsPages = "&type=pages&heatmap_url=1&date="+sDate+"&limit=all";
				
				JSONArray jsonArrayPage;
				try {
					List <ClickyPagesList> visitor=ClickyPagesList.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayPage = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayPage.length();i++){
						String locString = null;
						Location locationName = null;
						String data = jsonArrayPage.getJSONObject(i).get("url").toString();
		    			String arr[] = data.split("#_");
						try{
							
				    		String findLoc[] = arr[0].split("locationId=");
				    		locString = findLoc[1].replace("+", " ");
				    		locationName = Location.findLocationByName(locString);
						}catch(Exception e){
							System.out.println("No Location");
						}
						
		    			ClickyPagesList cPages = new ClickyPagesList();
		    			cPages.setValue(jsonArrayPage.getJSONObject(i).get("value").toString());
		    			cPages.setValue_percent(jsonArrayPage.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayPage.getJSONObject(i).get("title").toString());
		    			cPages.setStats_url(jsonArrayPage.getJSONObject(i).get("stats_url").toString());
		    			cPages.setUrl(jsonArrayPage.getJSONObject(i).get("url").toString());
		    			
		    			cPages.setMainUrl(arr[0]);
		    			if(locationName != null){
		    				cPages.locationName = locationName.name;
		    				cPages.locations = locationName.id;
		    			}
		    			cPages.setSaveDate(curr);
		    			cPages.save();
		    			long idForPage=cPages.id;
		    			
		    			ClickyPagesList lis=ClickyPagesList.findById(idForPage);
		    			String Url[]=lis.url.split(".com");
		    			
		    			String newUrl=Url[1];
		    			String arr1[] = newUrl.split("#_");
		    			String url = arr1[0];
		    			if(url.contains(":8080")){
		    				String ur[]=url.split(":8080");
		    				url=ur[1];
		    			}
		    			
		    			//url=jsonArrayPage.getJSONObject(i).get("url").toString();
		    			//url=URLEncoder.encode(url);
		    			
		    			paramsPages = "&type=segmentation&href="+url+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPagePages;
		    			
		    			jsonArrayPagePages = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				ClickyPagesActionList cPage = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPagePages.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPagePages.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPage.setAverageAction1(jsonArrayPagePages.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPagePages.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPage.setAverageTime1(jsonArrayPagePages.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPagePages.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPage.setTotalTime1(jsonArrayPagePages.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPagePages.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPage.setBounceRate1(jsonArrayPagePages.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPagePages.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPage.setVisitors1(jsonArrayPagePages.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPagePages.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPage.setUniqueVisitor1(jsonArrayPagePages.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPagePages.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPage.setAction1(jsonArrayPagePages.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    			
		    			
		    			
		    			paramsPages = "&type=segmentation&href="+url+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				//ClickyPagesActionList cPage = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPage.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPage.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPage.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPage.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPage.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPage.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPage.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    				cPage.setSaveDate(dateClicky);
		    				cPage.setUrl(url);
		    				cPage.setClickyPagesId(idForPage);
		    				cPage.save();
		    				//cPage.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				

	 			paramsPages = "&type=site-domains&date="+sDate+"&limit=all";
				JSONArray jsonArrayDomain;
				List<ClickyContentDomain> list= ClickyContentDomain.getAllData(startDateForList);
				try {
					if(list.size() == 0){
					jsonArrayDomain = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayDomain.length();i++){
		    			ClickyContentDomain cPages = new ClickyContentDomain();
		    			try{
		    			cPages.setValue(jsonArrayDomain.getJSONObject(i).get("value").toString());
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			try{
		    				cPages.setValuePercent(jsonArrayDomain.getJSONObject(i).get("value_percent").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
		    			try{
		    				cPages.setTitle(jsonArrayDomain.getJSONObject(i).get("title").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
		    			try{
		    				cPages.setUrl(jsonArrayDomain.getJSONObject(i).get("url").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
		    			try{
		    				cPages.setStatsUrl(jsonArrayDomain.getJSONObject(i).get("stats_url").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
		    			
		    			
		    			
		    			cPages.setSaveDate(dateClicky);
		    			//	cPages1.setClickyPagesId(idForPage);
		    				cPages.save();
					}
					}
					}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			


	 			paramsPages = "&type=media&date="+sDate+"&limit=all";
				JSONArray jsonArrayMedia;
				try {
					List <ClickyContentMedia> visitor=ClickyContentMedia.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayMedia = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayMedia.length();i++){
		    			ClickyContentMedia cPages = new ClickyContentMedia();
		    			try{
		    			cPages.setValue(jsonArrayMedia.getJSONObject(i).get("value").toString());
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			try{
		    				cPages.setValuePercent(jsonArrayMedia.getJSONObject(i).get("value_percent").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
		    			try{
		    				cPages.setTitle(jsonArrayMedia.getJSONObject(i).get("title").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
		    			String url = null;
		    			cPages.setStatsUrl(jsonArrayMedia.getJSONObject(i).get("stats_url").toString());
		    			cPages.setUrl(jsonArrayMedia.getJSONObject(i).get("url").toString());
		    			url=jsonArrayMedia.getJSONObject(i).get("url").toString();
		    			url=URLEncoder.encode(url);
		    			
		    			
		    			
		    			paramsPages = "&type=segmentation&video="+url+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArraymediaPage;
		    			
		    			jsonArraymediaPage = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArraymediaPage.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArraymediaPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArraymediaPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArraymediaPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArraymediaPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArraymediaPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArraymediaPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArraymediaPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArraymediaPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArraymediaPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArraymediaPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArraymediaPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArraymediaPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArraymediaPage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArraymediaPage.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    			
		    			cPages.setSaveDate(dateClicky);
		    			//	cPages1.setClickyPagesId(idForPage);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			

				
				
				
				
				

	 			paramsPages = "&type=events&date="+sDate+"&limit=all";
				JSONArray jsonArrayContentEvents;
				try {
					List <ClickyContentEvent> visitor=ClickyContentEvent.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayContentEvents = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayContentEvents.length();i++){
		    			ClickyContentEvent cPages = new ClickyContentEvent();
		    			try{
		    			cPages.setValue(jsonArrayContentEvents.getJSONObject(i).get("value").toString());
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			try{
		    				cPages.setValuePercent(jsonArrayContentEvents.getJSONObject(i).get("value_percent").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
		    			
		    			cPages.setTitle(jsonArrayContentEvents.getJSONObject(i).get("title").toString());
		    			cPages.setStatsUrl(jsonArrayContentEvents.getJSONObject(i).get("stats_url").toString());
		    			cPages.setUrl(jsonArrayContentEvents.getJSONObject(i).get("url").toString());
		    			
		    			String data=jsonArrayContentEvents.getJSONObject(i).get("url").toString();
		    			String arr[] = data.split(".com");	
		    			String href=URLEncoder.encode(arr[1]);
	            paramsPages = "&type=segmentation&href="+href+"&segments=summary&date="+sDate+"&limit=all";
		    			JSONArray jsonArrayContentEx;
		    			
		    			jsonArrayContentEx = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayContentEx.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    	
		    			
		    			
		    			
		    			cPages.setSaveDate(dateClicky);
		    			//	cPages1.setClickyPagesId(idForPage);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				paramsPages = "&type=downloads&date="+sDate+"&limit=all";
				JSONArray jsonArrayContentDownLoad;
				try {
					List <ClickyContentDownLoad> visitor=ClickyContentDownLoad.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayContentDownLoad = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayContentDownLoad.length();i++){
						String locString = null;
						Location locationName = null;
						String data = jsonArrayContentDownLoad.getJSONObject(i).get("url").toString();
		    			String arr[] = data.split(".com");
		    			ClickyContentDownLoad cPages = new ClickyContentDownLoad();
		    			cPages.setEditedUrl(arr[1]);
		    			try{
		    			cPages.setValue(jsonArrayContentDownLoad.getJSONObject(i).get("value").toString());
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			try{
		    				cPages.setValuePercent(jsonArrayContentDownLoad.getJSONObject(i).get("value_percent").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
		    			
		    			cPages.setTitle(jsonArrayContentDownLoad.getJSONObject(i).get("title").toString());
		    			cPages.setStatsUrl(jsonArrayContentDownLoad.getJSONObject(i).get("stats_url").toString());
		    			cPages.setUrl(jsonArrayContentDownLoad.getJSONObject(i).get("url").toString());
		    			
		    			cPages.setSaveDate(dateClicky);
		    			//	cPages1.setClickyPagesId(idForPage);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			

				
				
				
				
				

				
				paramsPages = "&type=pages-exit&date="+sDate+"&limit=all";
				JSONArray jsonArrayContentExit;
				try {
					List <ClickyContentExit> visitor=ClickyContentExit.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayContentExit = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayContentExit.length();i++){
						String locString = null;
						Location locationName = null;
						String data = jsonArrayContentExit.getJSONObject(i).get("url").toString();
		    			String arr[] = data.split(".com");
		    			ClickyContentExit cPages = new ClickyContentExit();
		    			cPages.setEditedUrl(arr[1]);
		    			try{
		    			cPages.setValue(jsonArrayContentExit.getJSONObject(i).get("value").toString());
		    			}
		    			catch(Exception e){
		    				e.printStackTrace();
		    			}
		    			try{
		    				cPages.setValuePercent(jsonArrayContentExit.getJSONObject(i).get("value_percent").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
		    			
		    			cPages.setTitle(jsonArrayContentExit.getJSONObject(i).get("title").toString());
		    			cPages.setStatsUrl(jsonArrayContentExit.getJSONObject(i).get("stats_url").toString());
		    			cPages.setUrl(jsonArrayContentExit.getJSONObject(i).get("url").toString());
		    			
		    			

		    			paramsPages = "&type=segmentation&exit="+arr[1]+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayContentEx;
		    			
		    			jsonArrayContentEx = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayContentEx.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayContentEx.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayContentEx.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    			
		    			cPages.setSaveDate(dateClicky);
		    			//	cPages1.setClickyPagesId(idForPage);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				
				
				
				paramsPages = "&type=pages-entrance&date="+sDate+"&limit=all";
				JSONArray jsonArrayEntrance;
				try {
					List <ClickyEntranceList> visitor=ClickyEntranceList.getAllData(startDateForList);
					if(visitor.size() == 0){
					jsonArrayEntrance = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayEntrance.length();i++){
						String locString = null;
						Location locationName = null;
						String data = jsonArrayEntrance.getJSONObject(i).get("url").toString();
		    			String arr[] = data.split("#_");
						
						
		    			ClickyEntranceList cPages = new ClickyEntranceList();
		    			cPages.setValue(jsonArrayEntrance.getJSONObject(i).get("value").toString());
		    			cPages.setValuePercent(jsonArrayEntrance.getJSONObject(i).get("value_percent").toString());
		    			cPages.setTitle(jsonArrayEntrance.getJSONObject(i).get("title").toString());
		    			cPages.setStatsUrl(jsonArrayEntrance.getJSONObject(i).get("stats_url").toString());
		    			cPages.setUrl(jsonArrayEntrance.getJSONObject(i).get("url").toString());
		    			cPages.setMainUrl(arr[0]);
		    			
		    			String lis=jsonArrayEntrance.getJSONObject(i).get("url").toString();
		    			String Url[]=lis.split(".com");
		    			
		    			String newUrl=Url[1];
		    			String arr1[] = newUrl.split("#_");
		    			String url = arr1[0];
		    			if(url.contains(":8080")){
		    				String ur[]=url.split(":8080");
		    				url=ur[1];
		    			}
		    			paramsPages = "&type=segmentation&href="+url+"&segments=summary&date="+sDate+"&limit=all";
		    			
		    			JSONArray jsonArrayPage1;
		    			
		    				jsonArrayPage1 = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    			//	ClickyPagesActionList cPages1 = new ClickyPagesActionList();
		    				for(int j=0;j<jsonArrayPage1.length();j++){
		    	    			
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
		    					
		    						cPages.setAverageAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
			    					
		    						cPages.setAverageTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
			    					
		    						cPages.setTotalTime(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
			    					
		    						cPages.setBounceRate(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
			    					
		    						cPages.setVisitors(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
			    					
		    						cPages.setUniqueVisitor(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    					
		    					
		    					if(jsonArrayPage1.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
			    					
		    						cPages.setAction(jsonArrayPage1.getJSONObject(j).get("value").toString());
		    						
		    					}
		    	    			
		    	    			
		    			
		    				}
		    				cPages.setSaveDate(dateClicky);
		    				cPages.setUrl(url);
		    			//	cPages1.setClickyPagesId(idForPage);
		    				cPages.save();
					}
					}
				}
				 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				List<ClickyActionList> cActionLists = null;
				try{
				   cActionLists = ClickyActionList.getcurr_date(df.parse(sDate));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				if(cActionLists.size() == 0){
				paramsAction = "&type=actions-list&date="+sDate+"&limit=all";
				
				JSONArray jsonArrayAction;
				try {
					jsonArrayAction = new JSONArray(callClickAPI(paramsAction)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArrayAction.length();i++){
						
						//List<ClickyActionList> cActionLists = ClickyActionList.getClickyUnikue(jsonArrayAction.getJSONObject(i).get("uid").toString(),jsonArrayAction.getJSONObject(i).get("session_id").toString());
						//if(cActionLists.size() == 0){
						String referrer_domain = null;
							ClickyActionList cAction = new ClickyActionList();
			    			cAction.setTime(jsonArrayAction.getJSONObject(i).get("time").toString());
			    			cAction.setTime_pretty(jsonArrayAction.getJSONObject(i).get("time_pretty").toString());
			    			cAction.setIp_address(jsonArrayAction.getJSONObject(i).get("ip_address").toString());
			    			cAction.setUid(jsonArrayAction.getJSONObject(i).get("uid").toString());
			    			cAction.setSession_id(jsonArrayAction.getJSONObject(i).get("session_id").toString());
			    			cAction.setAction_type(jsonArrayAction.getJSONObject(i).get("action_type").toString());
			    			try{
			    			cAction.setAction_title(jsonArrayAction.getJSONObject(i).get("action_title").toString());
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			cAction.setAction_url(jsonArrayAction.getJSONObject(i).get("action_url").toString());
			    			cAction.setStats_url(jsonArrayAction.getJSONObject(i).get("stats_url").toString());
			    			try{
			    			cAction.setReferrer_domain(jsonArrayAction.getJSONObject(i).get("referrer_domain").toString());
			    			referrer_domain=jsonArrayAction.getJSONObject(i).get("referrer_domain").toString();
			    			}
			    			catch(Exception e){
			    				e.printStackTrace();
			    			}
			    			
			    			if(referrer_domain != null){
			    				paramsPages = "&type=segmentation&domain="+referrer_domain+"&segments=summary&date="+sDate+"&limit=all";
			    				 
			    				JSONArray jsonArrayactionpage;
				    			
				    			jsonArrayactionpage = new JSONArray(callClickAPI(paramsPages)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
				    				//ClickyVisitorsList cPages = new ClickyVisitorsList();
				    				for(int j=0;j<jsonArrayactionpage.length();j++){
				    	    			
				    					
				    					
				    					if(jsonArrayactionpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average actions / visit")){
					    					
				    						cAction.setAverageAction(jsonArrayactionpage.getJSONObject(j).get("value").toString());
				    					}
				    					
				    					if(jsonArrayactionpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Average time / visit")){
					    					
				    						cAction.setAverageTime(jsonArrayactionpage.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					
				    					if(jsonArrayactionpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Total time")){
					    					
				    						cAction.setTotalTime(jsonArrayactionpage.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					
				    					if(jsonArrayactionpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Bounce rate")){
					    					
				    						cAction.setBounceRate(jsonArrayactionpage.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					
				    					if(jsonArrayactionpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Visitors")){
					    					
				    						cAction.setVisitors(jsonArrayactionpage.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					if(jsonArrayactionpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Unique visitors")){
					    					
				    						cAction.setUniqueVisitor(jsonArrayactionpage.getJSONObject(j).get("value").toString());
				    						
				    					}
				    					
				    					
				    					if(jsonArrayactionpage.getJSONObject(j).get("title").toString().equalsIgnoreCase("Actions")){
					    					
				    						cAction.setAction(jsonArrayactionpage.getJSONObject(j).get("value").toString());
				    						
				    					}
				    	    			
				    			
				    				}
			    				}
			    			
			    			cAction.setCurrDate(dateClicky);
			    			cAction.save();
						}
		    			
					}	
				 catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				
				
				gcal.add(Calendar.DAY_OF_WEEK, 1);
	    	}
	    	SchedularDate schdate = new SchedularDate();
	    	schdate.start_date = df.format(sampleDate);
	    	schdate.end_date = df.format(newcurrDate);	
	    	schdate.curr_time = df2.format(currtime);
	    	schdate.save();
	    	return ok();
	    }

	 public static Result getEngTimeData(String title,String startdate,String enddate){
	    	

		    Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
				 d1 = format.parse(startdate);
		         d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			String title1 = title.replace("<" , "&amp;lt;");
			List<ClickyVisitorEngagementTime> browserObjList = ClickyVisitorEngagementTime.findByTitleAndDate(title1, d1, d2);
			List<ClickyVisitorEngagementTime> allbrowserlist = ClickyVisitorEngagementTime.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickyVisitorEngagementTime lis:browserObjList){
				 if(lis.averageAction != null && !lis.averageAction.equals("")){
			     		count1=count1+Double.parseDouble(lis.averageAction);
			     	}
					if(lis.bounceRate != null && !lis.bounceRate.equals("")){
						count6=count6+Double.parseDouble(lis.bounceRate);	
						     	}
					if(lis.averageTime != null && !lis.averageTime.equals("")){
						count5=count5+Double.parseDouble(lis.averageTime);	
						}
					if(lis.totalTime != null && !lis.totalTime.equals("")){
						count4=count4+Double.parseDouble(lis.totalTime);
						}
					if(lis.visitors != null && !lis.visitors.equals("")){
						 count2=count2+Double.parseDouble(lis.visitors);
						}
					if(lis.uniqueVisitor!= null && !lis.uniqueVisitor.equals("")){
						count3=count3+Double.parseDouble(lis.uniqueVisitor);
						}
					if(lis.action != null && !lis.action.equals("")){
					count7=count7+Double.parseDouble(lis.action);
					}
				 		
			 }
			 
			 double countAll1=0.0;
				double countAll2=0.0;
				double countAll3=0.0;
				double countAll4=0.0;
				double countAll5=0.0;
				double countAll6=0.0;
				double countAll7=0.0;
				 for(ClickyVisitorEngagementTime list:allbrowserlist){
					 String titleNew=list.title;
						if(!titleNew.contains("&amp;lt;1m") ||!titleNew.contains("&amp;lt;10m")){
				      	if(list.averageAction != null && !list.averageAction.equals("")){
								 countAll1=count1+Double.parseDouble(list.averageAction);
					     	}
							if(list.bounceRate != null && !list.bounceRate.equals("")){
								 countAll6=count6+Double.parseDouble(list.bounceRate);	
								     	}
							if(list.averageTime != null && !list.averageTime.equals("")){
								countAll5=Double.parseDouble(list.averageTime);
								}
							if(list.totalTime != null && !list.totalTime.equals("")){
								 countAll4=count4+Double.parseDouble(list.totalTime);
								}
							if(list.visitors != null && !list.visitors.equals("")){
								countAll2=countAll2+Double.parseDouble(list.visitors);
								}
							if(list.uniqueVisitor!= null && !list.uniqueVisitor.equals("")){
								countAll3=count3+Double.parseDouble(list.uniqueVisitor);
								}
							if(list.action != null && !list.action.equals("")){
								 countAll7=countAll7+Double.parseDouble(list.action);
							}
						 
						}
					 
			   			
				 }
			 
				 ClickyPlatformVM cVm = new ClickyPlatformVM();
				 cVm.title = "visitors";
				 cVm.these_visitors =  count2;
				 cVm.all_visitors = countAll2;
				 cVm.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
				 platformvm.add(cVm);
				 
				 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
				 cVm1.title = "uniqueV";
				 cVm1.these_visitors = count3;
				 cVm1.all_visitors = countAll3;
				 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
				 platformvm.add(cVm1);
				 
				 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
				 cVm2.title = "action";
				 cVm2.these_visitors = count7;
				 cVm2.all_visitors = countAll7;
				 cVm2.images = "//con.tent.network/media/icon_click.gif";
				 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
				 platformvm.add(cVm2);
				 
				 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
				 cVm3.title = "averageAct";
				 cVm3.these_visitors = count1;
				 cVm3.all_visitors = countAll1;
				 cVm3.images = "//con.tent.network/media/icon_click.gif";
				 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
				 platformvm.add(cVm3);
				 
				 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
				 cVm4.title = "totalT";
				 cVm4.these_visitors = count4;
				 cVm4.all_visitors = countAll4;
				 cVm4.images = "//con.tent.network/media/icon_time.gif";
				 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
				 platformvm.add(cVm4);
				 
				 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
				 cVm5.title = "averageT";
				 cVm5.these_visitors = count5;
				 cVm5.all_visitors = countAll5;
				 cVm5.images = "//con.tent.network/media/icon_time.gif";
				 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
				 platformvm.add(cVm5);
				 
				 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
				 cVm6.title = "bounceR";
				 cVm6.these_visitors = count6;
				 cVm6.all_visitors = countAll6;
				 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
				 if(countAll6 !=0){
					 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
				 }
				 else{
					 cVm6.difference = 0.0;
				 }
				 platformvm.add(cVm6);
			 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

		 	
		 	return ok(Json.toJson(platformvm));


	    }
	 
	 
	 public static Result getreferrerTypeData(String type,String locationFlag,String startDate,String endDate){
	     	//String startDate=null;
	     	String params = null; 
	     	startDate = startDate.replaceAll(" ","");
	     	 
	     	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			//startDate=dateFormat.format(date);
	     	List<ClickyPagesVM> clickyList = new ArrayList<>();
	     	String city=null;
	     	if(locationFlag.equalsIgnoreCase("location")){
	     		
	     		 Date d1= null;
	     		Date d2= null;
	     		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	     		try{
	     			 d1 = format.parse(startDate);
	     	         d2 = format.parse(endDate);
	     	    } catch (Exception e) {
	     	        e.printStackTrace();
	     	    }
	     		
	     		List<ClickyVisitorsList> locationObjList = ClickyVisitorsList.getAll(d1, d2);
	    		List<ClickyVisitorsList> alldatalist = ClickyVisitorsList.getAll(d1, d2);
	    		
	    		List <ClickyPagesVM> VMs = new ArrayList<>();
	    		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	    		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	    		Map<String, Integer> timeline = new HashMap<String, Integer>();
	    		ClickyPagesVM vm = new ClickyPagesVM();
	    		double count1=0.0;
	    		double count2=0.0;
	    		double count3=0.0;
	    		double count4=0.0;
	    		double count5=0.0;
	    		double count6=0.0;
	    		double count7=0.0;
	    		double bounceSize = 0;
	    		Integer vistValue = 0;
	    		Integer vistValue1 = 0;
	    		 for(ClickyVisitorsList lis:locationObjList){
	    	     	if(lis.averageAction != null){
	    	     		count1=Double.parseDouble(lis.averageAction);
	    	     	}
	    			if(lis.bounceRate != null){
	    				count6=count6+Double.parseDouble(lis.bounceRate);
	    				
	    				     	}
	    			if(lis.averageTime != null){
	    				count5=Double.parseDouble(lis.averageTime);	
	    				}
	    			if(lis.timeTotal != null){
	    				count4=count4+Double.parseDouble(lis.timeTotal);
	    				}
	    			if(lis.visitors != null){
	    				 count2=count2+Double.parseDouble(lis.visitors);
	    				}
	    			if(lis.uniqueVisitor!= null){
	    				count3=count3+Double.parseDouble(lis.uniqueVisitor);
	    				}
	    			if(lis.actions != null){
	    			count7=count7+Double.parseDouble(lis.actions);
	    			}
	    			bounceSize = locationObjList.size();
	    			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
	    			if(lis.visitors != null){
					if (langValue == null) {
					 vistValue = vistValue + Integer.parseInt(lis.visitors);
					 mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitors));
					 
					 vistValue1 = vistValue1 + Integer.parseInt(lis.uniqueVisitor);
					 mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.uniqueVisitor));
					}
	    			}
	    		 }
	    		 
	    		 
	    		 	double countAll1=0.0;
	    			double countAll2=0.0;
	    			double countAll3=0.0;
	    			double countAll4=0.0;
	    			double countAll5=0.0;
	    			double countAll6=0.0;
	    			double countAll7=0.0;
	    			Integer uniquevisit = 0;
	    			Integer uniquebounce = 0;
	    			 for(ClickyVisitorsList list:alldatalist){
	    				 if(list.averageAction != null){
	    					 countAll1=count1+Double.parseDouble(list.averageAction);
	    			     	}
	    					if(list.bounceRate != null){
	    						 countAll6=count6+Double.parseDouble(list.bounceRate);	
	    						     	}
	    					if(list.averageTime != null){
	    						countAll5=count5+Double.parseDouble(list.averageTime);	
	    						}
	    					if(list.averageTime != null  && !list.averageTime.equals("")){
	    						 countAll4=countAll4+Double.parseDouble(list.averageTime);
	    						}
	    					if(list.visitors != null){
	    						countAll2=alldatalist.size();
	    						}
	    					if(list.uniqueVisitor != null){
	    						countAll3=alldatalist.size();
	    						}
	    					if(list.actions != null && !list.actions.equals("")){
	    						 countAll7=countAll7+Double.parseDouble(list.actions);
	    					}
	    					Integer langValue1 = timeline.get(list.DateClick.toString()); 
	    					if(list.uniqueVisitor != null){
	    						if (langValue1 == null) {
	    							uniquevisit = uniquevisit + Integer.parseInt(list.uniqueVisitor);
	    							timeline.put(list.DateClick.toString(), Integer.parseInt(list.uniqueVisitor));
	    							
	    							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
	    							timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRate));
	    						}
	    					}
	    			 }
	    		 
	    			 ClickyPlatformVM cVm = new ClickyPlatformVM();
	    			 cVm.title = "visitors";
	    			 cVm.these_visitors = (double)vistValue;
	    			 cVm.all_visitors = countAll2;
	    			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm.difference = (((double)vistValue - countAll2) / countAll2) * 100;
	    			 platformvm.add(cVm);
	    			 
	    			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	    			 cVm1.title = "uniqueV";
	    			 cVm1.these_visitors = (double)vistValue1;
	    			 cVm1.all_visitors = (double)uniquevisit;
	    			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm1.difference = (((double)vistValue1 - (double)uniquevisit) / (double)uniquevisit) * 100;
	    			 platformvm.add(cVm1);
	    			 
	    			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	    			 cVm2.title = "action";
	    			 cVm2.these_visitors = count7;
	    			 cVm2.all_visitors = countAll7;
	    			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	    			 platformvm.add(cVm2);
	    			 
	    			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	    			 cVm3.title = "averageAct";
	    			 cVm3.these_visitors = count7/(double)vistValue;
	    			 cVm3.all_visitors = countAll7/countAll2;
	    			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	    			 platformvm.add(cVm3);
	    			 
	    			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	    			 cVm4.title = "totalT";
	    			 cVm4.these_visitors = count4;
	    			 cVm4.all_visitors = countAll4;
	    			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	    			 platformvm.add(cVm4);
	    			 
	    			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	    			 cVm5.title = "averageT";
	    			 cVm5.these_visitors = count4/(double)vistValue;
	    			 cVm5.all_visitors = countAll4/countAll2;
	    			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm5.difference = (((count4/(double)vistValue) - (countAll4/countAll2)) / (countAll4/countAll2)) * 100;
	    			 platformvm.add(cVm5);
	    			 
	    			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	    			 cVm6.title = "bounceR";
	    			 cVm6.these_visitors = count6/bounceSize;
	    			 cVm6.all_visitors = (double)uniquebounce;
	    			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	    			 if(countAll6 !=0){
	    				 cVm6.difference = (((count6/bounceSize) - (double)uniquebounce) / (double)uniquebounce) * 100;
	    			 }
	    			 else{
	    				 cVm6.difference = 0.0;
	    			 }
	    			 platformvm.add(cVm6);
	    		 
	    		 vm.averageAct=count1;
	    		 vm.visitor=count2;
	    		 vm.uniqueV=count3;
	    		 vm.totalT=count4;
	    		 vm.averageT=count5;
	    		 vm.bounceR=count6;
	    		 vm.action=count7;
	    		
	    		 VMs.add(vm);

	    	 	
	    	 	return ok(Json.toJson(platformvm));
	     	
	     	}
	     	else if(locationFlag.equalsIgnoreCase("language")){
	     		
	     		//params = "&type=segmentation&language="+type+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	     		
	     		 Date d1= null;
	      		Date d2= null;
	      		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	      		try{
	      			 d1 = format.parse(startDate);
	      	         d2 = format.parse(endDate);
	      	    } catch (Exception e) {
	      	        e.printStackTrace();
	      	    }
	      		
	      		List<ClickyVisitorsList> languageObjList = ClickyVisitorsList.findByLanguageAndDate(type, d1, d2);
	     		List<ClickyVisitorsList> allLanguagelist = ClickyVisitorsList.getAll(d1, d2);
	     		
	     		List <ClickyPagesVM> VMs = new ArrayList<>();
	     		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	     		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	     		Map<String, Integer> timeline = new HashMap<String, Integer>();
	    		ClickyPagesVM vm = new ClickyPagesVM();
	    		double count1=0.0;
	    		double count2=0.0;
	    		double count3=0.0;
	    		double count4=0.0;
	    		double count5=0.0;
	    		double count6=0.0;
	    		double count7=0.0;
	    		double bounceSize = 0;
	    		Integer vistValue = 0;
	    		Integer vistValue1 = 0;
	     		 for(ClickyVisitorsList lis:languageObjList){
	     	     	if(lis.averageAction != null){
	     	     		count1=Double.parseDouble(lis.averageAction);
	     	     	}
	     			if(lis.bounceRate != null){
	     				count6=count6+Double.parseDouble(lis.bounceRate);	
	     				     	}
	     			if(lis.averageTime != null){
	     				count5=Double.parseDouble(lis.averageTime);	
	     				}
	     			if(lis.timeTotal != null){
	     				count4=count4+Double.parseDouble(lis.timeTotal);
	     				}
	     			if(lis.visitors != null){
	     				 count2=Double.parseDouble(lis.visitors);
	     				}
	     			if(lis.uniqueVisitor!= null){
	     				count3=Double.parseDouble(lis.uniqueVisitor);
	     				}
	     			if(lis.actions != null){
	     			count7=count7+Double.parseDouble(lis.actions);
	     			}
	     			bounceSize = languageObjList.size();
	     			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
	    			if(lis.visitors != null){
	    				if (langValue == null) {
	    					vistValue = vistValue + Integer.parseInt(lis.visitors);
	    					mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitors));
					 
	    					vistValue1 = vistValue1 + Integer.parseInt(lis.uniqueVisitor);
	    					mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.uniqueVisitor));
	    				}
	    			}
	     			
	     		 }
	     		 
	     		 
	     		 
	     		 double countAll1=0.0;
	     			double countAll2=0.0;
	     			double countAll3=0.0;
	     			double countAll4=0.0;
	     			double countAll5=0.0;
	     			double countAll6=0.0;
	     			double countAll7=0.0;
	     			Integer uniquebounce = 0;
	     			Integer uniquevisit = 0;
	     			 for(ClickyVisitorsList list:allLanguagelist){
	     				 if(list.averageAction != null){
	     					 countAll1=count1+Double.parseDouble(list.averageAction);
	     			     	}
	     					if(list.bounceRate != null){
	     						 countAll6=count6+Double.parseDouble(list.bounceRate);	
	     						     	}
	     					if(list.averageTime != null){
	     						countAll5=count5+Double.parseDouble(list.averageTime);	
	     						}
	     					if(list.averageTime != null  && !list.averageTime.equals("")){
	     						 countAll4=countAll4+Double.parseDouble(list.averageTime);
	     						}
	     					if(list.visitors != null){
	     						countAll2=allLanguagelist.size();
	     						}
	     					if(list.uniqueVisitor != null){
	     						countAll3=allLanguagelist.size();
	     						}
	     					if(list.actions != null && !list.actions.equals("")){
	     						 countAll7=countAll7+Double.parseDouble(list.actions);
	     					}
	     					
	    					Integer langValue1 = timeline.get(list.DateClick.toString()); 
	    					if(list.uniqueVisitor != null){
	    						if (langValue1 == null) {
	    							uniquevisit = uniquevisit + Integer.parseInt(list.uniqueVisitor);
	    							timeline.put(list.DateClick.toString(), Integer.parseInt(list.uniqueVisitor));
	    							
	    							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
	    							timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRate));
	    						}
	    					}
	     				 
	     		   			
	     			 }
	     		 
	     			ClickyPlatformVM cVm = new ClickyPlatformVM();
	    			 cVm.title = "visitors";
	    			 cVm.these_visitors = (double)vistValue;
	    			 cVm.all_visitors = countAll2;
	    			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm.difference = (((double)vistValue - countAll2) / countAll2) * 100;
	    			 platformvm.add(cVm);
	    			 
	    			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	    			 cVm1.title = "uniqueV";
	    			 cVm1.these_visitors = (double)vistValue1;
	    			 cVm1.all_visitors = (double)uniquevisit;
	    			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm1.difference = (((double)vistValue1 - (double)uniquevisit) / (double)uniquevisit) * 100;
	    			 platformvm.add(cVm1);
	    			 
	    			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	    			 cVm2.title = "action";
	    			 cVm2.these_visitors = count7;
	    			 cVm2.all_visitors = countAll7;
	    			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	    			 platformvm.add(cVm2);
	    			 
	    			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	    			 cVm3.title = "averageAct";
	    			 cVm3.these_visitors = count7/(double)vistValue;
	    			 cVm3.all_visitors = countAll7/countAll2;
	    			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	    			 platformvm.add(cVm3);
	    			 
	    			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	    			 cVm4.title = "totalT";
	    			 cVm4.these_visitors = count4;
	    			 cVm4.all_visitors = countAll4;
	    			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	    			 platformvm.add(cVm4);
	    			 
	    			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	    			 cVm5.title = "averageT";
	    			 cVm5.these_visitors = count4/(double)vistValue;
	    			 cVm5.all_visitors = countAll4/countAll2;
	    			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm5.difference = (((count4/(double)vistValue) - (countAll4/countAll2)) / (countAll4/countAll2)) * 100;
	    			 platformvm.add(cVm5);
	    			 
	    			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	    			 cVm6.title = "bounceR";
	    			 cVm6.these_visitors = count6/bounceSize;
	    			 cVm6.all_visitors = (double)uniquebounce;
	    			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	    			 if(countAll6 !=0){
	    				 cVm6.difference = (((count6/bounceSize) - (double)uniquebounce) / (double)uniquebounce) * 100;
	    			 }
	    			 else{
	    				 cVm6.difference = 0.0;
	    			 }
	     			 platformvm.add(cVm6);
	     		 
	     		 vm.averageAct=count1;
	     		 vm.visitor=count2;
	     		 vm.uniqueV=count3;
	     		 vm.totalT=count4;
	     		 vm.averageT=count5;
	     		 vm.bounceR=count6;
	     		 vm.action=count7;
	     		
	     		 VMs.add(vm);

	     	 	
	     	 	return ok(Json.toJson(platformvm));

	     	}
	          else if(locationFlag.equalsIgnoreCase("org")){
	     		String encod=URLEncoder.encode(type);
	     		//params = "&type=segmentation&org="+encod+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	     		
	     		 Date d1= null;
	        		Date d2= null;
	        		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        		try{
	        			 d1 = format.parse(startDate);
	        	         d2 = format.parse(endDate);
	        	    } catch (Exception e) {
	        	        e.printStackTrace();
	        	    }
	        		
	        		List<ClickyVisitorsList> orgObjList = ClickyVisitorsList.findByOrgAndDate(type, d1, d2);
	        		List<ClickyVisitorsList> allOrglist = ClickyVisitorsList.getAll(d1, d2);
	       		
	       		List <ClickyPagesVM> VMs = new ArrayList<>();
	       		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	       		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	       		Map<String, Integer> timeline = new HashMap<String, Integer>();
	       		ClickyPagesVM vm = new ClickyPagesVM();
	       		double count1=0.0;
	       		double count2=0.0;
	       		double count3=0.0;
	       		double count4=0.0;
	       		double count5=0.0;
	       		double count6=0.0;
	       		double count7=0.0;
	       		double bounceSize = 0;
	       		Integer vistValue = 0;
    			Integer vistValue1 = 0;
    			
	       		 for(ClickyVisitorsList lis:orgObjList){
	       	     	if(lis.averageActionorg != null){
	       	     		count1=Double.parseDouble(lis.averageActionorg);
	       	     	}
	       			if(lis.bounceRateorg != null){
	       				count6=count6+Double.parseDouble(lis.bounceRateorg);	
	       				     	}
	       			if(lis.averageTimeorg != null){
	       				count5=Double.parseDouble(lis.averageTimeorg);	
	       				}
	       			if(lis.timeTotal != null){
	       				count4=count4+Double.parseDouble(lis.timeTotal);
	       				}
	       			if(lis.visitorsorg != null){
	       				 count2=Double.parseDouble(lis.visitorsorg);
	       				}
	       			if(lis.uniqueVisitororg!= null){
	       				count3=Double.parseDouble(lis.uniqueVisitororg);
	       				}
	       			if(lis.actions != null){
	       			count7=count7+Double.parseDouble(lis.actions);
	       			}
	       			bounceSize = orgObjList.size();
	    			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
	    			if(lis.visitorsorg != null){
					if (langValue == null) {
					 vistValue = vistValue + Integer.parseInt(lis.visitorsorg);
				 mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitorsorg));
				 
				 vistValue1 = vistValue1 + Integer.parseInt(lis.uniqueVisitororg);
				 mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.uniqueVisitororg));
					}		
	       		 }
	       		 }
	       		 
	       		 double countAll1=0.0;
	       			double countAll2=0.0;
	       			double countAll3=0.0;
	       			double countAll4=0.0;
	       			double countAll5=0.0;
	       			double countAll6=0.0;
	       			double countAll7=0.0;
	       			Integer uniquebounce = 0;
	       			Integer uniquevisit = 0;
	       			 for(ClickyVisitorsList list:allOrglist){
	       				 if(list.averageActionorg != null){
	       					 countAll1=count1+Double.parseDouble(list.averageActionorg);
	       			     	}
	       					if(list.bounceRateorg != null){
	       						 countAll6=count6+Double.parseDouble(list.bounceRateorg);	
	       						     	}
	       					if(list.averageTimeorg != null){
	       						countAll5=count5+Double.parseDouble(list.averageTimeorg);	
	       						}
	       					if(list.averageTimeorg != null  && !list.averageTimeorg.equals("")){
	       						 countAll4=countAll4+Double.parseDouble(list.averageTimeorg);
	       						}
	       					if(list.visitorsorg != null){
	       						countAll2=allOrglist.size();
	       						}
	       					if(list.uniqueVisitororg != null){
	       						countAll3=allOrglist.size();
	       						}
	       					if(list.actions != null && !list.actions.equals("")){
	       						 countAll7=countAll7+Double.parseDouble(list.actions);
	       					}
	       					
	       				 Integer langValue1 = timeline.get(list.DateClick.toString()); 
    					if(list.uniqueVisitororg != null){
    					if (langValue1 == null) {
    						uniquevisit = uniquevisit + Integer.parseInt(list.uniqueVisitororg);
    						timeline.put(list.DateClick.toString(), Integer.parseInt(list.uniqueVisitororg));
    					
    						uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
    						timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRate));
    					}
    					}
	       				 
	       		   			
	       			 }
	       		 
	       		 ClickyPlatformVM cVm = new ClickyPlatformVM();
    			 cVm.title = "visitors";
    			 cVm.these_visitors = (double)vistValue;
    			 cVm.all_visitors = countAll2;
    			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
    			 cVm.difference = (((double)vistValue - countAll2) / countAll2) * 100;
    			 platformvm.add(cVm);
    			 
    			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
    			 cVm1.title = "uniqueV";
    			 cVm1.these_visitors = (double)vistValue1;
    			 cVm1.all_visitors = (double)uniquevisit;
    			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
    			 cVm1.difference = (((double)vistValue1 - (double)uniquevisit) / (double)uniquevisit) * 100;
    			 platformvm.add(cVm1);
    			 
    			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
    			 cVm2.title = "action";
    			 cVm2.these_visitors = count7;
    			 cVm2.all_visitors = countAll7;
    			 cVm2.images = "//con.tent.network/media/icon_click.gif";
    			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
    			 platformvm.add(cVm2);
    			 
    			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
    			 cVm3.title = "averageAct";
    			 cVm3.these_visitors = count7/(double)vistValue;
    			 cVm3.all_visitors = countAll7/countAll2;
    			 cVm3.images = "//con.tent.network/media/icon_click.gif";
    			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
    			 platformvm.add(cVm3);
    			 
    			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
    			 cVm4.title = "totalT";
    			 cVm4.these_visitors = count4;
    			 cVm4.all_visitors = countAll4;
    			 cVm4.images = "//con.tent.network/media/icon_time.gif";
    			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
    			 platformvm.add(cVm4);
    			 
    			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
    			 cVm5.title = "averageT";
    			 cVm5.these_visitors = count4/(double)vistValue;
    			 cVm5.all_visitors = countAll4/countAll2;
    			 cVm5.images = "//con.tent.network/media/icon_time.gif";
    			 cVm5.difference = (((count4/(double)vistValue) - (countAll4/countAll2)) / (countAll4/countAll2)) * 100;
    			 platformvm.add(cVm5);
    			 
    			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
    			 cVm6.title = "bounceR";
    			 cVm6.these_visitors = count6/bounceSize;
    			 cVm6.all_visitors = (double)uniquebounce;
    			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
    			 if(countAll6 !=0){
    				 cVm6.difference = (((count6/bounceSize) - (double)uniquebounce) / (double)uniquebounce) * 100;
    			 }
    			 else{
    				 cVm6.difference = 0.0;
    			 }
    			 platformvm.add(cVm6);
	       		 
	       		 vm.averageAct=count1;
	       		 vm.visitor=count2;
	       		 vm.uniqueV=count3;
	       		 vm.totalT=count4;
	       		 vm.averageT=count5;
	       		 vm.bounceR=count6;
	       		 vm.action=count7;
	       		
	       		 VMs.add(vm);

	       	 	
	       	 	return ok(Json.toJson(platformvm));

	     		
	     	}
	          else if(locationFlag.equalsIgnoreCase("host")){
	       		
	       		//params = "&type=segmentation&hostname="+type+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	       		
	        	  Date d1= null;
	        		Date d2= null;
	        		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        		try{
	        			 d1 = format.parse(startDate);
	        	         d2 = format.parse(endDate);
	        	    } catch (Exception e) {
	        	        e.printStackTrace();
	        	    }
	        		
	        	List<ClickyVisitorsList> hostObjList = ClickyVisitorsList.findByHostAndDate(type, d1, d2);
	       		List<ClickyVisitorsList> allHostlist = ClickyVisitorsList.getAll(d1, d2);
	       		
	       		List <ClickyPagesVM> VMs = new ArrayList<>();
	     		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	     		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	     		Map<String, Integer> timeline = new HashMap<String, Integer>();
	    		ClickyPagesVM vm = new ClickyPagesVM();
	    		double count1=0.0;
	    		double count2=0.0;
	    		double count3=0.0;
	    		double count4=0.0;
	    		double count5=0.0;
	    		double count6=0.0;
	    		double count7=0.0;
	    		double bounceSize = 0;
	    		Integer vistValue = 0;
	    		Integer vistValue1 = 0;
	     		 for(ClickyVisitorsList lis:hostObjList){
	     	     	if(lis.averageAction != null){
	     	     		count1=Double.parseDouble(lis.averageAction);
	     	     	}
	     			if(lis.bounceRate != null){
	     				count6=count6+Double.parseDouble(lis.bounceRate);	
	     				     	}
	     			if(lis.averageTime != null){
	     				count5=Double.parseDouble(lis.averageTime);	
	     				}
	     			if(lis.timeTotal != null){
	     				count4=count4+Double.parseDouble(lis.timeTotal);
	     				}
	     			if(lis.visitors != null){
	     				 count2=Double.parseDouble(lis.visitors);
	     				}
	     			if(lis.uniqueVisitor!= null){
	     				count3=Double.parseDouble(lis.uniqueVisitor);
	     				}
	     			if(lis.actions != null){
	     			count7=count7+Double.parseDouble(lis.actions);
	     			}
	     			bounceSize = hostObjList.size();
	     			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
	    			if(lis.visitors != null){
	    				if (langValue == null) {
	    					vistValue = vistValue + Integer.parseInt(lis.visitors);
	    					mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitors));
					 
	    					vistValue1 = vistValue1 + Integer.parseInt(lis.uniqueVisitor);
	    					mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.uniqueVisitor));
	    				}
	    			}
	     			
	     		 }
	     		 
	     		 
	     		 
	     		 double countAll1=0.0;
	     			double countAll2=0.0;
	     			double countAll3=0.0;
	     			double countAll4=0.0;
	     			double countAll5=0.0;
	     			double countAll6=0.0;
	     			double countAll7=0.0;
	     			Integer uniquevisit = 0;
	     			Integer uniquebounce = 0;
	     			 for(ClickyVisitorsList list:allHostlist){
	     				 if(list.averageAction != null){
	     					 countAll1=count1+Double.parseDouble(list.averageAction);
	     			     	}
	     					if(list.bounceRate != null){
	     						 countAll6=count6+Double.parseDouble(list.bounceRate);	
	     						     	}
	     					if(list.averageTime != null){
	     						countAll5=count5+Double.parseDouble(list.averageTime);	
	     						}
	     					if(list.averageTime != null  && !list.averageTime.equals("")){
	     						 countAll4=countAll4+Double.parseDouble(list.averageTime);
	     						}
	     					if(list.visitors != null){
	     						countAll2=allHostlist.size();
	     						}
	     					if(list.uniqueVisitor != null){
	     						countAll3=allHostlist.size();
	     						}
	     					if(list.actions != null && !list.actions.equals("")){
	     						 countAll7=countAll7+Double.parseDouble(list.actions);
	     					}
	     					
	    					Integer langValue1 = timeline.get(list.DateClick.toString()); 
	    					if(list.uniqueVisitor != null){
	    						if (langValue1 == null) {
	    							uniquevisit = uniquevisit + Integer.parseInt(list.uniqueVisitor);
	    							timeline.put(list.DateClick.toString(), Integer.parseInt(list.uniqueVisitor));
	    							
	    							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
	    							timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRate));
	    						}
	    					}
	     				 
	     		   			
	     			 }
	     		 
	     			ClickyPlatformVM cVm = new ClickyPlatformVM();
	    			 cVm.title = "visitors";
	    			 cVm.these_visitors = (double)vistValue;
	    			 cVm.all_visitors = countAll2;
	    			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm.difference = (((double)vistValue - countAll2) / countAll2) * 100;
	    			 platformvm.add(cVm);
	    			 
	    			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	    			 cVm1.title = "uniqueV";
	    			 cVm1.these_visitors = (double)vistValue1;
	    			 cVm1.all_visitors = (double)uniquevisit;
	    			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm1.difference = (((double)vistValue1 - (double)uniquevisit) / (double)uniquevisit) * 100;
	    			 platformvm.add(cVm1);
	    			 
	    			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	    			 cVm2.title = "action";
	    			 cVm2.these_visitors = count7;
	    			 cVm2.all_visitors = countAll7;
	    			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	    			 platformvm.add(cVm2);
	    			 
	    			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	    			 cVm3.title = "averageAct";
	    			 cVm3.these_visitors = count7/(double)vistValue;
	    			 cVm3.all_visitors = countAll7/countAll2;
	    			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	    			 platformvm.add(cVm3);
	    			 
	    			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	    			 cVm4.title = "totalT";
	    			 cVm4.these_visitors = count4;
	    			 cVm4.all_visitors = countAll4;
	    			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	    			 platformvm.add(cVm4);
	    			 
	    			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	    			 cVm5.title = "averageT";
	    			 cVm5.these_visitors = count4/(double)vistValue;
	    			 cVm5.all_visitors = countAll4/countAll2;
	    			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm5.difference = (((count4/(double)vistValue) - (countAll4/countAll2)) / (countAll4/countAll2)) * 100;
	    			 platformvm.add(cVm5);
	    			 
	    			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	    			 cVm6.title = "bounceR";
	    			 cVm6.these_visitors = count6/bounceSize;
	    			 cVm6.all_visitors = (double)uniquebounce;
	    			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	    			 if(countAll6 !=0){
	    				 cVm6.difference = (((count6/bounceSize) - (double)uniquebounce) / (double)uniquebounce) * 100;
	    			 }
	    			 else{
	    				 cVm6.difference = 0.0;
	    			 }
	     			 platformvm.add(cVm6);
	     		 
	     		 vm.averageAct=count1;
	     		 vm.visitor=count2;
	     		 vm.uniqueV=count3;
	     		 vm.totalT=count4;
	     		 vm.averageT=count5;
	     		 vm.bounceR=count6;
	     		 vm.action=count7;
	     		
	     		 VMs.add(vm);
	       	 	
	       	 	return ok(Json.toJson(platformvm));

	       	}
	          else if(locationFlag.equalsIgnoreCase("os")){
	        	  String encod=URLEncoder.encode(type);
	       		//params = "&type=segmentation&os="+encod+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	        	  Date d1= null;
	       		Date d2= null;
	       		/*String Date1 = "2016-06-13";
	       		String Date2 = "2016-06-26";*/
	       		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	       		try{
	       			 d1 = format.parse(startDate);
	       	         d2 = format.parse(endDate);
	       	    } catch (Exception e) {
	       	        e.printStackTrace();
	       	    }
	       		
	       		List<ClickyVisitorsList> operatingObjList = ClickyVisitorsList.getAll(d1, d2);
	      		List<ClickyVisitorsList> allOSlist = ClickyVisitorsList.getAll(d1, d2);
	      		List <ClickyPagesVM> VMs = new ArrayList<>();
	      		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	      		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
				Map<String, Integer> timeline = new HashMap<String, Integer>();
	      		ClickyPagesVM vm = new ClickyPagesVM();
	      		double count1=0.0;
	      		double count2=0.0;
	      		double count3=0.0;
	      		double count4=0.0;
	      		double count5=0.0;
	      		double count6=0.0;
	      		double count7=0.0;
	      		double bounceSize = 0;
	      		Integer vistValue = 0;
				Integer vistValue1 = 0;
	      		 for(ClickyVisitorsList lis:operatingObjList){
	      	     	if(lis.averageActionos != null){
	      	     		count1=Double.parseDouble(lis.averageActionos);
	      	     	}
	      			if(lis.bounceRateos != null){
	      				count6=count6+Double.parseDouble(lis.bounceRateos);	
	      				     	}
	      			if(lis.averageTimeos != null){
	      				count5=Double.parseDouble(lis.averageTimeos);	
	      				}
	      			if(lis.totalTime != null){
	      				count4=count4+Double.parseDouble(lis.timeTotal);
	      				}
	      			if(lis.visitorsos != null){
	      				 count2=Double.parseDouble(lis.visitorsos);
	      				}
	      			if(lis.uniqueVisitoros!= null){
	      				count3=Double.parseDouble(lis.uniqueVisitoros);
	      				}
	      			if(lis.actions != null){
	      			count7=count7+Double.parseDouble(lis.actions);
	      			}
	      			bounceSize = operatingObjList.size();
	    			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
					if(lis.visitorsos != null){
					if (langValue == null) {
				 		vistValue = vistValue + Integer.parseInt(lis.visitorsos);
			 			mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitorsos));
			 
			 			vistValue1 = vistValue1 + Integer.parseInt(lis.uniqueVisitoros);
			 			mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.uniqueVisitoros));
						}		
       		 		}	
	      		 }
	      		 
	      		 double countAll1=0.0;
	      			double countAll2=0.0;
	      			double countAll3=0.0;
	      			double countAll4=0.0;
	      			double countAll5=0.0;
	      			double countAll6=0.0;
	      			double countAll7=0.0;
	      			Integer uniquebounce = 0;
	      			Integer uniquevisit = 0;
	      			 for(ClickyVisitorsList list:allOSlist){
	      				 if(list.averageActionos != null){
	      					 countAll1=count1+Double.parseDouble(list.averageActionos);
	      			     	}
	      					if(list.bounceRateos != null){
	      						 countAll6=count6+Double.parseDouble(list.bounceRateos);	
	      						     	}
	      					if(list.averageTimeos != null){
	      						countAll5=count5+Double.parseDouble(list.averageTimeos);	
	      						}
	      					if(list.averageTimeos != null  && !list.averageTimeos.equals("")){
	      						 countAll4=countAll4+Double.parseDouble(list.averageTimeos);
	      						}
	      					if(list.visitorsos != null){
	      						countAll2=allOSlist.size();
	      						}
	      					if(list.uniqueVisitoros != null){
	      						countAll3=allOSlist.size();
	      						}
	      					if(list.actions != null && !list.actions.equals("")){
	      						 countAll7=countAll7+Double.parseDouble(list.actions);
	      					}
	      					Integer langValue1 = timeline.get(list.DateClick.toString());
	      					if(list.uniqueVisitororg != null){
								if(langValue1 == null) {
									uniquevisit = uniquevisit + Integer.parseInt(list.uniqueVisitoros);
					 				timeline.put(list.DateClick.toString(), Integer.parseInt(list.uniqueVisitoros));
					 				
					 				uniquebounce = uniquebounce + Integer.parseInt(list.bounceRateos);
					 				timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRateos));
								}
							}
	      				 
	      				 
	      			 }
	      		 
	      			ClickyPlatformVM cVm = new ClickyPlatformVM();
	    			 cVm.title = "visitors";
	    			 cVm.these_visitors = (double)vistValue;
	    			 cVm.all_visitors = countAll2;
	    			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm.difference = (((double)vistValue - countAll2) / countAll2) * 100;
	    			 platformvm.add(cVm);
	    			 
	    			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	    			 cVm1.title = "uniqueV";
	    			 cVm1.these_visitors = (double)vistValue1;
	    			 cVm1.all_visitors = (double)uniquevisit;
	    			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm1.difference = (((double)vistValue1 - (double)uniquevisit) / (double)uniquevisit) * 100;
	    			 platformvm.add(cVm1);
	    			 
	    			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	    			 cVm2.title = "action";
	    			 cVm2.these_visitors = count7;
	    			 cVm2.all_visitors = countAll7;
	    			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	    			 platformvm.add(cVm2);
	    			 
	    			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	    			 cVm3.title = "averageAct";
	    			 cVm3.these_visitors = count7/(double)vistValue;
	    			 cVm3.all_visitors = countAll7/countAll2;
	    			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	    			 platformvm.add(cVm3);
	    			 
	    			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	    			 cVm4.title = "totalT";
	    			 cVm4.these_visitors = count4;
	    			 cVm4.all_visitors = countAll4;
	    			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	    			 platformvm.add(cVm4);
	    			 
	    			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	    			 cVm5.title = "averageT";
	    			 cVm5.these_visitors = count4/(double)vistValue;
	    			 cVm5.all_visitors = countAll4/countAll2;
	    			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm5.difference = (((count4/(double)vistValue) - (countAll4/countAll2)) / (countAll4/countAll2)) * 100;
	    			 platformvm.add(cVm5);
	    			 
	    			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	    			 cVm6.title = "bounceR";
	    			 cVm6.these_visitors = count6/bounceSize;
	    			 cVm6.all_visitors = (double)uniquebounce;
	    			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	    			 if(countAll6 !=0){
	    				 cVm6.difference = (((count6/bounceSize) - (double)uniquebounce) / (double)uniquebounce) * 100;
	    			 }
	    			 else{
	    				 cVm6.difference = 0.0;
	    			 }
	    			 platformvm.add(cVm6);
	      		 
	      		 vm.averageAct=count1;
	      		 vm.visitor=count2;
	      		 vm.uniqueV=count3;
	      		 vm.totalT=count4;
	      		 vm.averageT=count5;
	      		 vm.bounceR=count6;
	      		 vm.action=count7;
	      		
	      		 VMs.add(vm);

	      	 	
	      	 	return ok(Json.toJson(platformvm));

	        	  
	       	}
	          else if(locationFlag.equalsIgnoreCase("browser")){
	        	  String encod=URLEncoder.encode(type);
	       		//params = "&type=segmentation&browser="+encod+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	       		
	        	  Date d1= null;
	         		Date d2= null;
	         		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	         		try{
	         			 d1 = format.parse(startDate);
	         	         d2 = format.parse(endDate);
	         	    } catch (Exception e) {
	         	        e.printStackTrace();
	         	    }
	         		
	         		List<ClickyVisitorsList> browserObjList = ClickyVisitorsList.findBybrowserAndDate(type, d1, d2);
	        		List<ClickyVisitorsList> allBrowserlist = ClickyVisitorsList.getAll(d1, d2);
	        		
	        		List <ClickyPagesVM> VMs = new ArrayList<>();
	        		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	        		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	    			Map<String, Integer> timeline = new HashMap<String, Integer>();
	        		ClickyPagesVM vm = new ClickyPagesVM();
	        		double count1=0.0;
	        		double count2=0.0;
	        		double count3=0.0;
	        		double count4=0.0;
	        		double count5=0.0;
	        		double count6=0.0;
	        		double count7=0.0;
	        		double bounceSize = 0;
	        		Integer vistValue = 0;
					Integer vistValue1 = 0;
	        		 for(ClickyVisitorsList lis:browserObjList){
	        	     	if(lis.averageActionbrowser != null){
	        	     		count1=Double.parseDouble(lis.averageActionbrowser);
	        	     	}
	        			if(lis.bounceRatebrowser != null){
	        				count6=count6+Double.parseDouble(lis.bounceRatebrowser);	
	        				     	}
	        			if(lis.averageTimebrowser != null){
	        				count5=Double.parseDouble(lis.averageTimebrowser);	
	        				}
	        			if(lis.timeTotal != null){
	        				count4=count4+Double.parseDouble(lis.timeTotal);
	        				}
	        			if(lis.visitorsbrowser != null){
	        				 count2=Double.parseDouble(lis.visitorsbrowser);
	        				}
	        			if(lis.uniqueVisitorbrowser!= null){
	        				count3=Double.parseDouble(lis.uniqueVisitorbrowser);
	        				}
	        			if(lis.actions != null){
	        			count7=count7+Double.parseDouble(lis.actions);
	        			}
	        			bounceSize = browserObjList.size();
	        			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
			if(lis.visitorsbrowser != null){
			if (langValue == null) {
		 		vistValue = vistValue + Integer.parseInt(lis.visitorsbrowser);
	 			mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitorsbrowser));
	 
	 			vistValue1 = vistValue1 + Integer.parseInt(lis.uniqueVisitorbrowser);
	 			mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.uniqueVisitorbrowser));
				}		
		 		}	
	        		 }
	        		 
	        		 double countAll1=0.0;
	        			double countAll2=0.0;
	        			double countAll3=0.0;
	        			double countAll4=0.0;
	        			double countAll5=0.0;
	        			double countAll6=0.0;
	        			double countAll7=0.0;
	        			Integer uniquebounce = 0;
	        			Integer uniquevisit = 0;
	        			 for(ClickyVisitorsList list:allBrowserlist){
	        				 if(list.averageActionbrowser != null){
	        					 countAll1=count1+Double.parseDouble(list.averageActionbrowser);
	        			     	}
	        					if(list.bounceRatebrowser != null){
	        						 countAll6=count6+Double.parseDouble(list.bounceRatebrowser);	
	        						     	}
	        					if(list.averageTimebrowser != null){
	        						countAll5=count5+Double.parseDouble(list.averageTimebrowser);	
	        						}
	        					if(list.averageTimebrowser != null  && !list.averageTimebrowser.equals("")){
	        						 countAll4=countAll4+Double.parseDouble(list.averageTimebrowser);
	        						}
	        					if(list.visitorsbrowser != null){
	        						countAll2=allBrowserlist.size();
	        						}
	        					if(list.uniqueVisitorbrowser != null){
	        						countAll3=allBrowserlist.size();
	        						}
	        					if(list.actions != null && !list.actions.equals("")){
	        						 countAll7=countAll7+Double.parseDouble(list.actions);
	        					}
	        					Integer langValue1 = timeline.get(list.DateClick.toString());
	        					if(list.uniqueVisitororg != null){
	        						if(langValue1 == null) {
	        							uniquevisit = uniquevisit + Integer.parseInt(list.uniqueVisitorbrowser);
	        							timeline.put(list.DateClick.toString(), Integer.parseInt(list.uniqueVisitorbrowser));
	        						
	        							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRatebrowser);
	        							timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRatebrowser));
	        						}
	        					}		 
	        				 
	        		   			
	        			 }
	        		 
	        			 ClickyPlatformVM cVm = new ClickyPlatformVM();
		    			 cVm.title = "visitors";
		    			 cVm.these_visitors = (double)vistValue;
		    			 cVm.all_visitors = countAll2;
		    			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
		    			 cVm.difference = (((double)vistValue - countAll2) / countAll2) * 100;
		    			 platformvm.add(cVm);
		    			 
		    			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
		    			 cVm1.title = "uniqueV";
		    			 cVm1.these_visitors = (double)vistValue1;
		    			 cVm1.all_visitors = (double)uniquevisit;
		    			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
		    			 cVm1.difference = (((double)vistValue1 - (double)uniquevisit) / (double)uniquevisit) * 100;
		    			 platformvm.add(cVm1);
		    			 
		    			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
		    			 cVm2.title = "action";
		    			 cVm2.these_visitors = count7;
		    			 cVm2.all_visitors = countAll7;
		    			 cVm2.images = "//con.tent.network/media/icon_click.gif";
		    			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
		    			 platformvm.add(cVm2);
		    			 
		    			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
		    			 cVm3.title = "averageAct";
		    			 cVm3.these_visitors = count7/(double)vistValue;
		    			 cVm3.all_visitors = countAll7/countAll2;
		    			 cVm3.images = "//con.tent.network/media/icon_click.gif";
		    			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
		    			 platformvm.add(cVm3);
		    			 
		    			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
		    			 cVm4.title = "totalT";
		    			 cVm4.these_visitors = count4;
		    			 cVm4.all_visitors = countAll4;
		    			 cVm4.images = "//con.tent.network/media/icon_time.gif";
		    			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
		    			 platformvm.add(cVm4);
		    			 
		    			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
		    			 cVm5.title = "averageT";
		    			 cVm5.these_visitors = count4/(double)vistValue;
		    			 cVm5.all_visitors = countAll4/countAll2;
		    			 cVm5.images = "//con.tent.network/media/icon_time.gif";
		    			 cVm5.difference = (((count4/(double)vistValue) - (countAll4/countAll2)) / (countAll4/countAll2)) * 100;
		    			 platformvm.add(cVm5);
		    			 
		    			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
		    			 cVm6.title = "bounceR";
		    			 cVm6.these_visitors = count6/bounceSize;
		    			 cVm6.all_visitors = (double)uniquebounce;
		    			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
		    			 if(countAll6 !=0){
		    				 cVm6.difference = (((count6/bounceSize) - (double)uniquebounce) / (double)uniquebounce) * 100;
		    			 }
		    			 else{
		    				 cVm6.difference = 0.0;
		    			 }
	        			 platformvm.add(cVm6);
	        		 
	        		 vm.averageAct=count1;
	        		 vm.visitor=count2;
	        		 vm.uniqueV=count3;
	        		 vm.totalT=count4;
	        		 vm.averageT=count5;
	        		 vm.bounceR=count6;
	        		 vm.action=count7;
	        		
	        		 VMs.add(vm);

	        	 	
	        	 	return ok(Json.toJson(platformvm));

	        	  
	       	}
			else if(locationFlag.equalsIgnoreCase("Ip")){
	       		
	       		params = "&type=segmentation&ip_address="+type+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	       		
	       	}
	          else if(locationFlag.equalsIgnoreCase("screen")){
	       		
	       		//params = "&type=segmentation&resolution="+type+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	        	  Date d1= null;
	       		Date d2= null;
	       		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	       		try{
	       			 d1 = format.parse(startDate);
	       	         d2 = format.parse(endDate);
	       	    } catch (Exception e) {
	       	        e.printStackTrace();
	       	    }
	        	  
	        	List<ClickyVisitorsList> screenObjList = ClickyVisitorsList.findByScreenAndDate(type, d1, d2);
	      		List<ClickyVisitorsList> allScreenlist = ClickyVisitorsList.getAll(d1, d2);
	      		
	      		List <ClickyPagesVM> VMs = new ArrayList<>();
	      		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	      		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	      		Map<String, Integer> timeline = new HashMap<String, Integer>();
	    		ClickyPagesVM vm = new ClickyPagesVM();
	    		double count1=0.0;
	    		double count2=0.0;
	    		double count3=0.0;
	    		double count4=0.0;
	    		double count5=0.0;
	    		double count6=0.0;
	    		double count7=0.0;
	    		double bounceSize = 0;
	    		Integer vistValue = 0;
	    		Integer vistValue1 = 0;
	      		 for(ClickyVisitorsList lis:screenObjList){
	      	     	if(lis.averageAction != null){
	      	     		count1=Double.parseDouble(lis.averageAction);
	      	     	}
	      			if(lis.bounceRate != null){
	      				count6=count6+Double.parseDouble(lis.bounceRate);	
	      				     	}
	      			if(lis.averageTime != null){
	      				count5=Double.parseDouble(lis.averageTime);	
	      				}
	      			if(lis.timeTotal != null){
	      				count4=count4+Double.parseDouble(lis.timeTotal);
	      				}
	      			if(lis.visitors != null){
	      				 count2=Double.parseDouble(lis.visitors);
	      				}
	      			if(lis.uniqueVisitor!= null){
	      				count3=Double.parseDouble(lis.uniqueVisitor);
	      				}
	      			if(lis.actions != null){
	      			count7=count7+Double.parseDouble(lis.actions);
	      			}
	      			bounceSize = screenObjList.size();
	      			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
	    			if(lis.visitors != null){
	    				if (langValue == null) {
	    					vistValue = vistValue + Integer.parseInt(lis.visitors);
	    					mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitors));
					 
	    					vistValue1 = vistValue1 + Integer.parseInt(lis.uniqueVisitor);
	    					mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.uniqueVisitor));
	    				}
	    			}
	      			
	      		 }
	      		 
	      		 
	      		 
	      		 double countAll1=0.0;
	      			double countAll2=0.0;
	      			double countAll3=0.0;
	      			double countAll4=0.0;
	      			double countAll5=0.0;
	      			double countAll6=0.0;
	      			double countAll7=0.0;
	      			Integer uniquebounce = 0;
	      			Integer uniquevisit = 0;
	      			 for(ClickyVisitorsList list:allScreenlist){
	      				 if(list.averageAction != null){
	      					 countAll1=count1+Double.parseDouble(list.averageAction);
	      			     	}
	      					if(list.bounceRate != null){
	      						 countAll6=count6+Double.parseDouble(list.bounceRate);	
	      						     	}
	      					if(list.averageTime != null){
	      						countAll5=count5+Double.parseDouble(list.averageTime);	
	      						}
	      					if(list.averageTime != null  && !list.averageTime.equals("")){
	      						 countAll4=countAll4+Double.parseDouble(list.averageTime);
	      						}
	      					if(list.visitors != null){
	      						countAll2=allScreenlist.size();
	      						}
	      					if(list.uniqueVisitor != null){
	      						countAll3=allScreenlist.size();
	      						}
	      					if(list.actions != null && !list.actions.equals("")){
	      						 countAll7=countAll7+Double.parseDouble(list.actions);
	      					}
	      					
	    					Integer langValue1 = timeline.get(list.DateClick.toString()); 
	    					if(list.uniqueVisitor != null){
	    						if (langValue1 == null) {
	    							uniquevisit = uniquevisit + Integer.parseInt(list.uniqueVisitor);
	    							timeline.put(list.DateClick.toString(), Integer.parseInt(list.uniqueVisitor));
	    							
	    							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
	    							timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRate));
	    						}
	    					}
	      				 
	      		   			
	      			 }
	      		 
	      			ClickyPlatformVM cVm = new ClickyPlatformVM();
	    			 cVm.title = "visitors";
	    			 cVm.these_visitors = (double)vistValue;
	    			 cVm.all_visitors = countAll2;
	    			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm.difference = (((double)vistValue - countAll2) / countAll2) * 100;
	    			 platformvm.add(cVm);
	    			 
	    			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	    			 cVm1.title = "uniqueV";
	    			 cVm1.these_visitors = (double)vistValue1;
	    			 cVm1.all_visitors = (double)uniquevisit;
	    			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm1.difference = (((double)vistValue1 - (double)uniquevisit) / (double)uniquevisit) * 100;
	    			 platformvm.add(cVm1);
	    			 
	    			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	    			 cVm2.title = "action";
	    			 cVm2.these_visitors = count7;
	    			 cVm2.all_visitors = countAll7;
	    			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	    			 platformvm.add(cVm2);
	    			 
	    			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	    			 cVm3.title = "averageAct";
	    			 cVm3.these_visitors = count7/(double)vistValue;
	    			 cVm3.all_visitors = countAll7/countAll2;
	    			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	    			 platformvm.add(cVm3);
	    			 
	    			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	    			 cVm4.title = "totalT";
	    			 cVm4.these_visitors = count4;
	    			 cVm4.all_visitors = countAll4;
	    			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	    			 platformvm.add(cVm4);
	    			 
	    			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	    			 cVm5.title = "averageT";
	    			 cVm5.these_visitors = count4/(double)vistValue;
	    			 cVm5.all_visitors = countAll4/countAll2;
	    			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm5.difference = (((count4/(double)vistValue) - (countAll4/countAll2)) / (countAll4/countAll2)) * 100;
	    			 platformvm.add(cVm5);
	    			 
	    			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	    			 cVm6.title = "bounceR";
	    			 cVm6.these_visitors = count6/bounceSize;
	    			 cVm6.all_visitors = (double)uniquebounce;
	    			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	    			 if(countAll6 !=0){
	    				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
	    			 }
	    			 else{
	    				 cVm6.difference = 0.0;
	    			 }
	      			 platformvm.add(cVm6);
	      		 
	      		 vm.averageAct=count1;
	      		 vm.visitor=count2;
	      		 vm.uniqueV=count3;
	      		 vm.totalT=count4;
	      		 vm.averageT=count5;
	      		 vm.bounceR=count6;
	      		 vm.action=count7;
	      		
	      		 VMs.add(vm);

	      	 	
	      	 	return ok(Json.toJson(platformvm));

	       	}
	          else if(locationFlag.equalsIgnoreCase("Domain")){
	      		//params = "&type=segmentation&domain="+type+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	        	  Date d1= null;
	       		Date d2= null;
	       		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	       		try{
	       			 d1 = format.parse(startDate);
	       	         d2 = format.parse(endDate);
	       	    } catch (Exception e) {
	       	        e.printStackTrace();
	       	    }
	       		
	       		List<ClickyActionList> domainObjList = ClickyActionList.findByDomainAndDate(type, d1, d2);
	      		List<ClickyActionList> allDomainlist = ClickyActionList.getAll(d1, d2);
	      		
	      		List <ClickyPagesVM> VMs = new ArrayList<>();
	      		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	      		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	      		Map<String, Integer> timeline = new HashMap<String, Integer>();
	      		
	      		ClickyPagesVM vm = new ClickyPagesVM();
	      		double count1=0.0;
	      		double count2=0.0;
	      		double count3=0.0;
	      		double count4=0.0;
	      		double count5=0.0;
	      		double count6=0.0;
	      		double count7=0.0;
	      		double bounceSize = 0;
	      		Integer vistValue = 0;
	      		 for(ClickyActionList lis:domainObjList){
	      	     	if(lis.averageAction != null){
	      	     		count1=Double.parseDouble(lis.averageAction);
	      	     	}
	      			if(lis.bounceRate != null){
	      				count6=count6+Double.parseDouble(lis.bounceRate);	
	      				     	}
	      			if(lis.averageTime != null){
	      				count5=Double.parseDouble(lis.averageTime);	
	      				}
	      			if(lis.totalTime != null){
	      				count4=Double.parseDouble(lis.totalTime);
	      				}
	      			if(lis.visitors != null){
	      				 count2=Double.parseDouble(lis.visitors);
	      				}
	      			if(lis.uniqueVisitor!= null){
	      				count3=Double.parseDouble(lis.uniqueVisitor);
	      				}
	      			if(lis.action != null){
	      			count7=count7+Double.parseDouble(lis.action);
	      			}
	      			bounceSize = domainObjList.size();
	    			Integer langValue = mapOffline.get(lis.currDate.toString()); 
					if (langValue == null) {
					 vistValue = vistValue + Integer.parseInt(lis.visitors);
					 mapOffline.put(lis.currDate.toString(), Integer.parseInt(lis.visitors));
					}		
	      		 }
	      		 
	      		 double countAll1=0.0;
	      			double countAll2=0.0;
	      			double countAll3=0.0;
	      			double countAll4=0.0;
	      			double countAll5=0.0;
	      			double countAll6=0.0;
	      			double countAll7=0.0;
	      			double uniquebounce = 0;
	      			 for(ClickyActionList list:allDomainlist){
	      				 if(list.averageAction != null){
	      					 countAll1=count1+Double.parseDouble(list.averageAction);
	      			     	}
	      					if(list.bounceRate != null){
	      						 countAll6=count6+Double.parseDouble(list.bounceRate);	
	      						     	}
	      					if(list.averageTime != null){
	      						countAll5=count5+Double.parseDouble(list.averageTime);	
	      						}
	      					if(list.averageTime != null  && !list.averageTime.equals("")){
	      						 countAll4=count4+Double.parseDouble(list.averageTime);
	      						}
	      					if(list.visitors != null){
	      						countAll2=count2+Double.parseDouble(list.visitors);
	      						}
	      					if(list.uniqueVisitor != null){
	      						countAll3=count3+Double.parseDouble(list.uniqueVisitor);
	      						}
	      					if(list.action != null && !list.action.equals("")){
	      						 countAll7=count7+Double.parseDouble(list.action);
	      					}
	      					
	      		      		Integer langValue1 = timeline.get(list.currDate.toString()); 
	      					if(list.uniqueVisitor != null){
	      						if (langValue1 == null) {
	      							
	      							
	      							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
	      							timeline.put(list.currDate.toString(), Integer.parseInt(list.bounceRate));
	      						}
	      					}
	      		   			
	      			 }
	      		 
	      			 ClickyPlatformVM cVm = new ClickyPlatformVM();
	      			 cVm.title = "visitors";
	      			 cVm.these_visitors =  count2;
	      			 cVm.all_visitors = countAll2;
	      			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	      			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
	      			 platformvm.add(cVm);
	      			 
	      			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	      			 cVm1.title = "uniqueV";
	      			 cVm1.these_visitors = count3;
	      			 cVm1.all_visitors = countAll3;
	      			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	      			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
	      			 platformvm.add(cVm1);
	      			 
	      			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	      			 cVm2.title = "action";
	      			 cVm2.these_visitors = count7;
	      			 cVm2.all_visitors = countAll7;
	      			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	      			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	      			 platformvm.add(cVm2);
	      			 
	      			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	      			 cVm3.title = "averageAct";
	      			 cVm3.these_visitors = count7/count2;
	      			 cVm3.all_visitors = countAll7/countAll2;
	      			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	      			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	      			 platformvm.add(cVm3);
	      			 
	      			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	      			 cVm4.title = "totalT";
	      			 cVm4.these_visitors = count4;
	      			 cVm4.all_visitors = countAll4;
	      			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	      			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	      			 platformvm.add(cVm4);
	      			 
	      			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	      			 cVm5.title = "averageT";
	      			 cVm5.these_visitors = count4/count2;
	      			 cVm5.all_visitors = countAll4/countAll2;
	      			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	      			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
	      			 platformvm.add(cVm5);
	      			 
	      			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	      			 cVm6.title = "bounceR";
	      			 cVm6.these_visitors = count6/bounceSize;
	      			 cVm6.all_visitors = (double)uniquebounce;
	      			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	      			 if(countAll6 !=0){
	      				 cVm6.difference = (((count6/bounceSize) - (double)uniquebounce) / (double)uniquebounce) * 100;
	      			 }
	      			 else{
	      				 cVm6.difference = 0.0;
	      			 }
	      			 platformvm.add(cVm6);
	      		 
	      		 vm.averageAct=count1;
	      		 vm.visitor=count2;
	      		 vm.uniqueV=count3;
	      		 vm.totalT=count4;
	      		 vm.averageT=count5;
	      		 vm.bounceR=count6;
	      		 vm.action=count7;
	      		
	      		 VMs.add(vm);

	      	 	
	      	 	return ok(Json.toJson(platformvm));

	          
	          }
	     	else if(locationFlag.equalsIgnoreCase("source")){
	     		//params = "&type=segmentation&source="+type+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	     		 Date d1= null;
	      		Date d2= null;
	      		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	      		try{
	      			 d1 = format.parse(startDate);
	      	         d2 = format.parse(endDate);
	      	    } catch (Exception e) {
	      	        e.printStackTrace();
	      	    }
	      		
	      		List<ClickyVisitorsList> referrerObjList = ClickyVisitorsList.findByReferrerTypeAndDate(type, d1, d2);
	     		List<ClickyVisitorsList> allReferrerlist = ClickyVisitorsList.getAll(d1, d2);
	     		
	     		List <ClickyPagesVM> VMs = new ArrayList<>();
	     		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	     		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	     		Map<String, Integer> timeline = new HashMap<String, Integer>();
	      		
	     		ClickyPagesVM vm = new ClickyPagesVM();
	     		double count1=0.0;
	     		double count2=0.0;
	     		double count3=0.0;
	     		double count4=0.0;
	     		double count5=0.0;
	     		double count6=0.0;
	     		double count7=0.0;
	     		double bounceSize = 0;
	     		Integer vistValue = 0;
	     		 for(ClickyVisitorsList lis:referrerObjList){
	     	     	if(lis.averageAction != null){
	     	     		count1=Double.parseDouble(lis.averageAction);
	     	     	}
	     			if(lis.bounceRate != null){
	     				count6=count6+Double.parseDouble(lis.bounceRate);	
	     				     	}
	     			if(lis.averageTime != null){
	     				count5=Double.parseDouble(lis.averageTime);	
	     				}
	     			if(lis.timeTotal != null){
	     				count4=count4+Double.parseDouble(lis.timeTotal);
	     				}
	     			if(lis.visitors != null){
	     				 count2=Double.parseDouble(lis.visitors);
	     				}
	     			if(lis.uniqueVisitor!= null){
	     				count3=Double.parseDouble(lis.uniqueVisitor);
	     				}
	     			if(lis.actions != null){
	     			count7=count7+Double.parseDouble(lis.actions);
	     			}
	     			bounceSize = referrerObjList.size();
	     			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
	 				if (langValue == null) {
	 				 vistValue = vistValue + Integer.parseInt(lis.visitors);
	 				 mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitors));
	 				}
	     			
	     		 }
	     		 
	     		 
	     		 
	     		 double countAll1=0.0;
	     			double countAll2=0.0;
	     			double countAll3=0.0;
	     			double countAll4=0.0;
	     			double countAll5=0.0;
	     			double countAll6=0.0;
	     			double countAll7=0.0;
	     			Integer uniquebounce = 0;
	     			 for(ClickyVisitorsList list:allReferrerlist){
	     				 if(list.averageAction != null){
	     					 countAll1=count1+Double.parseDouble(list.averageAction);
	     			     	}
	     					if(list.bounceRate != null){
	     						 countAll6=count6+Double.parseDouble(list.bounceRate);	
	     						     	}
	     					if(list.averageTime != null){
	     						countAll5=count5+Double.parseDouble(list.averageTime);	
	     						}
	     					if(list.averageTime != null  && !list.averageTime.equals("")){
	     						 countAll4=countAll4+Double.parseDouble(list.averageTime);
	     						}
	     					if(list.visitors != null){
	     						countAll2=allReferrerlist.size();
	     						}
	     					if(list.uniqueVisitor != null){
	     						countAll3=allReferrerlist.size();
	     						}
	     					if(list.actions != null && !list.actions.equals("")){
	     						 countAll7=countAll7+Double.parseDouble(list.actions);
	     					}
	     				 
	     					
	     		      		Integer langValue1 = timeline.get(list.DateClick.toString()); 
	     					if(list.uniqueVisitor != null){
	     						if (langValue1 == null) {
	     							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
	     							timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRate));
	     						}
	     					}
	     			 }
	     		 
	     			 ClickyPlatformVM cVm = new ClickyPlatformVM();
	     			 cVm.title = "visitors";
	     			 cVm.these_visitors = (double)vistValue;
	     			 cVm.all_visitors = countAll2;
	     			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	     			 cVm.difference = (((double)vistValue - countAll2) / countAll2) * 100;
	     			 platformvm.add(cVm);
	     			 
	     			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	     			 cVm1.title = "uniqueV";
	     			 cVm1.these_visitors = count3;
	     			 cVm1.all_visitors = countAll3;
	     			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	     			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
	     			 platformvm.add(cVm1);
	     			 
	     			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	     			 cVm2.title = "action";
	     			 cVm2.these_visitors = count7;
	     			 cVm2.all_visitors = countAll7;
	     			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	     			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	     			 platformvm.add(cVm2);
	     			 
	     			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	     			 cVm3.title = "averageAct";
	     			 cVm3.these_visitors = count7/count2;
	     			 cVm3.all_visitors = countAll7/countAll2;
	     			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	     			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	     			 platformvm.add(cVm3);
	     			 
	     			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	     			 cVm4.title = "totalT";
	     			 cVm4.these_visitors = count4;
	     			 cVm4.all_visitors = countAll4;
	     			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	     			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	     			 platformvm.add(cVm4);
	     			 
	     			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	     			 cVm5.title = "averageT";
	     			 cVm5.these_visitors = count4/count2;
	     			 cVm5.all_visitors = countAll4/countAll2;
	     			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	     			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
	     			 platformvm.add(cVm5);
	     			 
	     			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	     			 cVm6.title = "bounceR";
	     			 cVm6.these_visitors = count6/bounceSize;
	     			 cVm6.all_visitors = (double)uniquebounce;
	     			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	     			 if(countAll6 !=0){
	     				 cVm6.difference = ((count6/bounceSize - (double)uniquebounce) / (double)uniquebounce) * 100;
	     			 }
	     			 else{
	     				 cVm6.difference = 0.0;
	     			 }
	     			 platformvm.add(cVm6);
	     		 
	     		 vm.averageAct=count1;
	     		 vm.visitor=count2;
	     		 vm.uniqueV=count3;
	     		 vm.totalT=count4;
	     		 vm.averageT=count5;
	     		 vm.bounceR=count6;
	     		 vm.action=count7;
	     		
	     		 VMs.add(vm);

	     	 	
	     	 	return ok(Json.toJson(platformvm));

	     	}

	     	else if(locationFlag.equalsIgnoreCase("other")){
	     		//params = "&type=segmentation&source="+type+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	     		 Date d1= null;
	      		Date d2= null;
	      		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	      		try{
	      			 d1 = format.parse(startDate);
	      	         d2 = format.parse(endDate);
	      	    } catch (Exception e) {
	      	        e.printStackTrace();
	      	    }
	      		
	      		List<ClickyVisitorsList> referrerObjList = ClickyVisitorsList.findByReferrerTypeAndDate(type, d1, d2);
	     		List<ClickyVisitorsList> allReferrerlist = ClickyVisitorsList.getAll(d1, d2);
	     		
	     		List <ClickyPagesVM> VMs = new ArrayList<>();
	     		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	     		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	     		Map<String, Integer> timeline = new HashMap<String, Integer>();
	      		
	     		ClickyPagesVM vm = new ClickyPagesVM();
	     		double count1=0.0;
	     		double count2=0.0;
	     		double count3=0.0;
	     		double count4=0.0;
	     		double count5=0.0;
	     		double count6=0.0;
	     		double count7=0.0;
	     		double bounceSize = 0;
	     		Integer vistValue = 0;
	     		 for(ClickyVisitorsList lis:referrerObjList){
	     	     	if(lis.averageAction != null){
	     	     		count1=Double.parseDouble(lis.averageAction);
	     	     	}
	     			if(lis.bounceRate != null){
	     				count6=Double.parseDouble(lis.bounceRate);	
	     				     	}
	     			if(lis.averageTime != null){
	     				count5=Double.parseDouble(lis.averageTime);	
	     				}
	     			if(lis.timeTotal != null){
	     				count4=count4+Double.parseDouble(lis.timeTotal);
	     				}
	     			if(lis.visitors != null){
	     				 count2=Double.parseDouble(lis.visitors);
	     				}
	     			if(lis.uniqueVisitor!= null){
	     				count3=Double.parseDouble(lis.uniqueVisitor);
	     				}
	     			if(lis.actions != null){
	     			count7=count7+Double.parseDouble(lis.actions);
	     			}
	     			bounceSize = referrerObjList.size();
	     			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
	 				if (langValue == null) {
	 				 vistValue = vistValue + Integer.parseInt(lis.visitors);
	 				 mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitors));
	 				}
	     			
	     		 }
	     		 
	     		 
	     		 
	     		 double countAll1=0.0;
	     			double countAll2=0.0;
	     			double countAll3=0.0;
	     			double countAll4=0.0;
	     			double countAll5=0.0;
	     			double countAll6=0.0;
	     			double countAll7=0.0;
	     			Integer uniquebounce = 0;
	     			 for(ClickyVisitorsList list:allReferrerlist){
	     				 if(list.averageAction != null){
	     					 countAll1=count1+Double.parseDouble(list.averageAction);
	     			     	}
	     					if(list.bounceRate != null){
	     						 countAll6=count6+Double.parseDouble(list.bounceRate);	
	     						     	}
	     					if(list.averageTime != null){
	     						countAll5=count5+Double.parseDouble(list.averageTime);	
	     						}
	     					if(list.averageTime != null  && !list.averageTime.equals("")){
	     						 countAll4=countAll4+Double.parseDouble(list.averageTime);
	     						}
	     					if(list.visitors != null){
	     						countAll2=allReferrerlist.size();
	     						}
	     					if(list.uniqueVisitor != null){
	     						countAll3=allReferrerlist.size();
	     						}
	     					if(list.actions != null && !list.actions.equals("")){
	     						 countAll7=countAll7+Double.parseDouble(list.actions);
	     					}
	     					
	     		      		Integer langValue1 = timeline.get(list.DateClick.toString()); 
	     					if(list.uniqueVisitor != null){
	     						if (langValue1 == null) {
	     							
	     							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
	     							timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRate));
	     						}
	     					}
	     		   			
	     			 }
	     		 
	     			 ClickyPlatformVM cVm = new ClickyPlatformVM();
	     			 cVm.title = "visitors";
	     			 cVm.these_visitors = (double)vistValue;
	     			 cVm.all_visitors = countAll2;
	     			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	     			 cVm.difference = (((double)vistValue - countAll2) / countAll2) * 100;
	     			 platformvm.add(cVm);
	     			 
	     			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	     			 cVm1.title = "uniqueV";
	     			 cVm1.these_visitors = count3;
	     			 cVm1.all_visitors = countAll3;
	     			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	     			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
	     			 platformvm.add(cVm1);
	     			 
	     			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	     			 cVm2.title = "action";
	     			 cVm2.these_visitors = count7;
	     			 cVm2.all_visitors = countAll7;
	     			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	     			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	     			 platformvm.add(cVm2);
	     			 
	     			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	     			 cVm3.title = "averageAct";
	     			 cVm3.these_visitors = count7/count2;
	     			 cVm3.all_visitors = countAll7/countAll2;
	     			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	     			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	     			 platformvm.add(cVm3);
	     			 
	     			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	     			 cVm4.title = "totalT";
	     			 cVm4.these_visitors = count4;
	     			 cVm4.all_visitors = countAll4;
	     			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	     			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	     			 platformvm.add(cVm4);
	     			 
	     			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	     			 cVm5.title = "averageT";
	     			 cVm5.these_visitors = count4/count2;
	     			 cVm5.all_visitors = countAll4/countAll2;
	     			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	     			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
	     			 platformvm.add(cVm5);
	     			 
	     			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	     			 cVm6.title = "bounceR";
	     			 cVm6.these_visitors = count6/bounceSize;
	     			 cVm6.all_visitors = (double)uniquebounce;
	     			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	     			 if(countAll6 !=0){
	     				 cVm6.difference = ((count6/bounceSize - (double)uniquebounce) / (double)uniquebounce) * 100;
	     			 }
	     			 else{
	     				 cVm6.difference = 0.0;
	     			 }
	     			 platformvm.add(cVm6);
	     		 
	     		 vm.averageAct=count1;
	     		 vm.visitor=count2;
	     		 vm.uniqueV=count3;
	     		 vm.totalT=count4;
	     		 vm.averageT=count5;
	     		 vm.bounceR=count6;
	     		 vm.action=count7;
	     		
	     		 VMs.add(vm);

	     	 	
	     	 	return ok(Json.toJson(platformvm));

	     	}

	     	try {

	      	   	JsonNode jsonList = Json.parse(callClickAPI(params));
	      	   	for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
	      	   	ClickyPagesVM vm = new ClickyPagesVM();
	 			vm.title = obj.get("title").textValue();
	 			vm.value = obj.get("value").textValue();
	 			if(locationFlag.equalsIgnoreCase("location")){
	 			vm.city=city;
	 			}
	      	   clickyList.add(vm);
	      	   	
	      	   	}
	              
	          } catch (Exception e) {
	              e.printStackTrace();
	          }
	     	
	     	
	     	
	     	
	     	return ok(Json.toJson(clickyList));
	     }

	 public static Result getVisitorDataForLanding(Long id,String flagForLanding ,String endDate,String startDate){
	    	//public static Result getUrlData(Long id){
	    	ClickyVisitorsList List = ClickyVisitorsList.findById(id);
	    	String title=List.referrerUrl;
	    	String domain=List.referrerDomain;
	    	String url=List.referrerUrl;
	    	String params = null; 
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	if(flagForLanding.equalsIgnoreCase("ForSearch")){
	    	params = "&type=segmentation&source=searches&title="+title+"&segments=summary&date="+endDate+","+startDate+"&limit=all";
	    	}
	    	else if(flagForLanding.equalsIgnoreCase("ForDomain")){
	    		//params = "&type=segmentation&source=searches&domain="+domain+"&segments=summary&date="+endDate+","+startDate+"&limit=all";
	    		 Date d1= null;
	        		Date d2= null;
	        		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        		try{
	        			 d1 = format.parse(startDate);
	        	         d2 = format.parse(endDate);
	        	    } catch (Exception e) {
	        	        e.printStackTrace();
	        	    }
	        		
	        	List<ClickyVisitorsList> locationObjList = ClickyVisitorsList.findByIdAndDate(id, d2, d1);
	       		List<ClickyVisitorsList> alldatalist = ClickyVisitorsList.getAll(d2, d1);
	       		
	       		List <ClickyPagesVM> VMs = new ArrayList<>();
	       		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	       		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	       		Map<String, Integer> timeline = new HashMap<String, Integer>();
	      		
	       		ClickyPagesVM vm = new ClickyPagesVM();
	       		double count1=0.0;
	       		double count2=0.0;
	       		double count3=0.0;
	       		double count4=0.0;
	       		double count5=0.0;
	       		double count6=0.0;
	       		double count7=0.0;
	       		double bounceSize =0;
	       		Integer vistValue = 0;
	       		 for(ClickyVisitorsList lis:locationObjList){
	       	     	if(lis.averageAction1 != null){
	       	     		count1=Double.parseDouble(lis.averageAction1);
	       	     	}
	       			if(lis.bounceRate1 != null){
	       				count6=count6+Double.parseDouble(lis.bounceRate1);	
	       				     	}
	       			if(lis.averageTime1 != null){
	       				count5=Double.parseDouble(lis.averageTime1);	
	       				}
	       			if(lis.timeTotal != null){
	       				count4=count4+Double.parseDouble(lis.timeTotal);
	       				}
	       			if(lis.visitors1 != null){
	       				 count2=Double.parseDouble(lis.visitors1);
	       				}
	       			if(lis.uniqueVisitor1!= null){
	       				count3=Double.parseDouble(lis.uniqueVisitor1);
	       				}
	       			if(lis.actions != null){
	       			count7=count7+Double.parseDouble(lis.actions);
	       			}
	       			bounceSize = locationObjList.size();
	    			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
					if (langValue == null) {
					 vistValue = vistValue + Integer.parseInt(lis.visitors);
					 mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitors));
					}		
	       		 }
	       		 
	       		 double countAll1=0.0;
	       			double countAll2=0.0;
	       			double countAll3=0.0;
	       			double countAll4=0.0;
	       			double countAll5=0.0;
	       			double countAll6=0.0;
	       			double countAll7=0.0;
	       			double uniquebounce =0;
	       			 for(ClickyVisitorsList list:alldatalist){
	       				 if(list.averageAction1 != null){
	       					 countAll1=count1+Double.parseDouble(list.averageAction1);
	       			     	}
	       					if(list.bounceRate1 != null){
	       						 countAll6=count6+Double.parseDouble(list.bounceRate1);	
	       						     	}
	       					if(list.averageTime1 != null){
	       						countAll5=count5+Double.parseDouble(list.averageTime1);	
	       						}
	       					if(list.averageTime != null  && !list.averageTime.equals("")){
	       						 countAll4=countAll4+Double.parseDouble(list.averageTime);
	       						}
	       					if(list.visitors1 != null){
	       						countAll2=alldatalist.size();
	       						}
	       					if(list.uniqueVisitor1 != null){
	       						countAll3=alldatalist.size();
	       						}
	       					if(list.actions != null && !list.actions.equals("")){
	       						 countAll7=countAll7+Double.parseDouble(list.actions);
	       					}
	       					
	       		      		Integer langValue1 = timeline.get(list.DateClick.toString()); 
	       					if(list.uniqueVisitor != null){
	       						if (langValue1 == null) {
	       							
	       							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
	       							timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRate));
	       						}
	       					}
	       		   			
	       			 }
	       		 
	       			 ClickyPlatformVM cVm = new ClickyPlatformVM();
	       			 cVm.title = "visitors";
	       			 cVm.these_visitors =  count2;
	       			 cVm.all_visitors = countAll2;
	       			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	       			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
	       			 platformvm.add(cVm);
	       			 
	       			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	       			 cVm1.title = "uniqueV";
	       			 cVm1.these_visitors = count3;
	       			 cVm1.all_visitors = countAll3;
	       			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	       			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
	       			 platformvm.add(cVm1);
	       			 
	       			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	       			 cVm2.title = "action";
	       			 cVm2.these_visitors = count7;
	       			 cVm2.all_visitors = countAll7;
	       			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	       			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	       			 platformvm.add(cVm2);
	       			 
	       			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	       			 cVm3.title = "averageAct";
	       			 cVm3.these_visitors = count7/count2;
	       			 cVm3.all_visitors = countAll7/countAll2;
	       			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	       			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	       			 platformvm.add(cVm3);
	       			 
	       			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	       			 cVm4.title = "totalT";
	       			 cVm4.these_visitors = count4;
	       			 cVm4.all_visitors = countAll4;
	       			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	       			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	       			 platformvm.add(cVm4);
	       			 
	       			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	       			 cVm5.title = "averageT";
	       			 cVm5.these_visitors = count4/count2;
	       			 cVm5.all_visitors = countAll4/countAll2;
	       			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	       			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
	       			 platformvm.add(cVm5);
	       			 
	       			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	       			 cVm6.title = "bounceR";
	       			 cVm6.these_visitors = count6/bounceSize;
	       			 cVm6.all_visitors = (double)uniquebounce;
	       			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	       			 if(countAll6 !=0){
	       				 cVm6.difference = ((count6/bounceSize - (double)uniquebounce) / (double)uniquebounce) * 100;
	       			 }
	       			 else{
	       				 cVm6.difference = 0.0;
	       			 }
	       			 platformvm.add(cVm6);
	       		 
	       		 vm.averageAct=count1;
	       		 vm.visitor=count2;
	       		 vm.uniqueV=count3;
	       		 vm.totalT=count4;
	       		 vm.averageT=count5;
	       		 vm.bounceR=count6;
	       		 vm.action=count7;
	       		
	       		 VMs.add(vm);

	       	 	
	       	 	return ok(Json.toJson(platformvm));

	    	
	    	}
	    	else if(flagForLanding.equalsIgnoreCase("ForRefferalUrl")){
	    		String newUrl=null;
	    		if(url.contains("=")){
	    			String urlArr[]=url.split("=");
	    			newUrl=urlArr[1];
	    		}
	    		else{
	    			newUrl=url;
	    		}
	    		
	    		//params = "&type=segmentation&search="+newUrl+"&segments=summary&date="+endDate+","+startDate+"&limit=all";
	    		 Date d1= null;
	     		Date d2= null;
	     		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	     		try{
	     			 d1 = format.parse(startDate);
	     	         d2 = format.parse(endDate);
	     	    } catch (Exception e) {
	     	        e.printStackTrace();
	     	    }
	     		
	     	List<ClickyVisitorsList> locationObjList = ClickyVisitorsList.findByIdAndDate(id, d2, d1);
	    		List<ClickyVisitorsList> alldatalist = ClickyVisitorsList.getAll(d2, d1);
	    		
	    		List <ClickyPagesVM> VMs = new ArrayList<>();
	    		List<ClickyPlatformVM> platformvm =new ArrayList<>();
	    		Map<String, Integer> mapOffline = new HashMap<String, Integer>();
	    		Map<String, Integer> timeline = new HashMap<String, Integer>();
	      		
	    		ClickyPagesVM vm = new ClickyPagesVM();
	    		double count1=0.0;
	    		double count2=0.0;
	    		double count3=0.0;
	    		double count4=0.0;
	    		double count5=0.0;
	    		double count6=0.0;
	    		double count7=0.0;
	    		double bounceSize = 0;
	    		Integer vistValue = 0;
	    		 for(ClickyVisitorsList lis:locationObjList){
	    	     	if(lis.averageAction != null){
	    	     		count1=Double.parseDouble(lis.averageAction);
	    	     	}
	    			if(lis.bounceRate != null){
	    				count6=count6+Double.parseDouble(lis.bounceRate);	
	    				     	}
	    			if(lis.averageTime != null){
	    				count5=Double.parseDouble(lis.averageTime);	
	    				}
	    			if(lis.timeTotal != null){
	    				count4=count4+Double.parseDouble(lis.timeTotal);
	    				}
	    			if(lis.visitors != null){
	    				 count2=Double.parseDouble(lis.visitors);
	    				}
	    			if(lis.uniqueVisitor!= null){
	    				count3=Double.parseDouble(lis.uniqueVisitor);
	    				}
	    			if(lis.actions != null){
	    			count7=count7+Double.parseDouble(lis.actions);
	    			}
	    			bounceSize = locationObjList.size();
	    			Integer langValue = mapOffline.get(lis.DateClick.toString()); 
					if (langValue == null) {
					 vistValue = vistValue + Integer.parseInt(lis.visitors);
					 mapOffline.put(lis.DateClick.toString(), Integer.parseInt(lis.visitors));
					}		
	    		 }
	    		 
	    		 double countAll1=0.0;
	    			double countAll2=0.0;
	    			double countAll3=0.0;
	    			double countAll4=0.0;
	    			double countAll5=0.0;
	    			double countAll6=0.0;
	    			double countAll7=0.0;
	    			Integer uniquebounce = 0;
	    			 for(ClickyVisitorsList list:alldatalist){
	    				 if(list.averageAction != null){
	    					 countAll1=count1+Double.parseDouble(list.averageAction);
	    			     	}
	    					if(list.bounceRate != null){
	    						 countAll6=count6+Double.parseDouble(list.bounceRate);	
	    						     	}
	    					if(list.averageTime != null){
	    						countAll5=count5+Double.parseDouble(list.averageTime);	
	    						}
	    					if(list.averageTime != null  && !list.averageTime.equals("")){
	    						 countAll4=countAll4+Double.parseDouble(list.averageTime);
	    						}
	    					if(list.visitors != null){
	    						countAll2=alldatalist.size();
	    						}
	    					if(list.uniqueVisitor != null){
	    						countAll3=alldatalist.size();
	    						}
	    					if(list.actions != null && !list.actions.equals("")){
	    						 countAll7=countAll7+Double.parseDouble(list.actions);
	    					}
	    					
	    		      		Integer langValue1 = timeline.get(list.DateClick.toString()); 
	    					if(list.uniqueVisitor != null){
	    						if (langValue1 == null) {
	    							
	    							uniquebounce = uniquebounce + Integer.parseInt(list.bounceRate);
	    							timeline.put(list.DateClick.toString(), Integer.parseInt(list.bounceRate));
	    						}
	    					}
	    		   			
	    			 }
	    		 
	    			 ClickyPlatformVM cVm = new ClickyPlatformVM();
	    			 cVm.title = "visitors";
	    			 cVm.these_visitors =  count2;
	    			 cVm.all_visitors = countAll2;
	    			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
	    			 platformvm.add(cVm);
	    			 
	    			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
	    			 cVm1.title = "uniqueV";
	    			 cVm1.these_visitors = count3;
	    			 cVm1.all_visitors = countAll3;
	    			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
	    			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
	    			 platformvm.add(cVm1);
	    			 
	    			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
	    			 cVm2.title = "action";
	    			 cVm2.these_visitors = count7;
	    			 cVm2.all_visitors = countAll7;
	    			 cVm2.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
	    			 platformvm.add(cVm2);
	    			 
	    			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
	    			 cVm3.title = "averageAct";
	    			 cVm3.these_visitors = count7/count2;
	    			 cVm3.all_visitors = countAll7/countAll2;
	    			 cVm3.images = "//con.tent.network/media/icon_click.gif";
	    			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
	    			 platformvm.add(cVm3);
	    			 
	    			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
	    			 cVm4.title = "totalT";
	    			 cVm4.these_visitors = count4;
	    			 cVm4.all_visitors = countAll4;
	    			 cVm4.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
	    			 platformvm.add(cVm4);
	    			 
	    			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
	    			 cVm5.title = "averageT";
	    			 cVm5.these_visitors = count4/count2;
	    			 cVm5.all_visitors = countAll4/countAll2;
	    			 cVm5.images = "//con.tent.network/media/icon_time.gif";
	    			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
	    			 platformvm.add(cVm5);
	    			 
	    			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
	    			 cVm6.title = "bounceR";
	    			 cVm6.these_visitors = count6/bounceSize;
	    			 cVm6.all_visitors = (double)uniquebounce;
	    			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
	    			 if(countAll6 !=0){
	    				 cVm6.difference = (((count6/bounceSize) - (double)uniquebounce) / (double)uniquebounce) * 100;
	    			 }
	    			 else{
	    				 cVm6.difference = 0.0;
	    			 }
	    			 platformvm.add(cVm6);
	    		 
	    		 vm.averageAct=count1;
	    		 vm.visitor=count2;
	    		 vm.uniqueV=count3;
	    		 vm.totalT=count4;
	    		 vm.averageT=count5;
	    		 vm.bounceR=count6;
	    		 vm.action=count7;
	    		
	    		 VMs.add(vm);

	    	 	
	    	 	return ok(Json.toJson(platformvm));

	    	
	    	}
	    	
	    	else{
	    		params = "&type=segmentation&search="+title+"&segments=summary&date="+endDate+","+startDate+"&limit=all";
	    	}
	    	 try {

	     	   	JsonNode jsonList = Json.parse(callClickAPI(params));
	     	   	for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
	     	   	ClickyPagesVM vm = new ClickyPagesVM();
				vm.title = obj.get("title").textValue();
				vm.value = obj.get("value").textValue();
	     	   	
	     	   clickyList.add(vm);
	     	   	
	     	   	}
	             
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
	    	
	    	
	    	
	    	return ok(Json.toJson(clickyList));
	    }
	 
	 public static Result getEngActionData(String title,String startdate,String enddate){
	    	

		    Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
				 d1 = format.parse(startdate);
		         d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			
			List<ClickyVisitorEngagementAction> browserObjList = ClickyVisitorEngagementAction.findByTitleAndDate(title, d1, d2);
			List<ClickyVisitorEngagementAction> allbrowserlist = ClickyVisitorEngagementAction.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickyVisitorEngagementAction lis:browserObjList){
				 
				 if(lis.averageAction != null && !lis.averageAction.equals("")){
			     		count1=count1+Double.parseDouble(lis.averageAction);
			     	}
					if(lis.bounceRate != null && !lis.bounceRate.equals("")){
						count6=count6+Double.parseDouble(lis.bounceRate);	
						     	}
					if(lis.averageTime != null && !lis.averageTime.equals("")){
						count5=count5+Double.parseDouble(lis.averageTime);	
						}
					if(lis.totalTime != null && !lis.totalTime.equals("")){
						count4=count4+Double.parseDouble(lis.totalTime);
						}
					if(lis.visitors != null && !lis.visitors.equals("")){
						 count2=count2+Double.parseDouble(lis.visitors);
						}
					if(lis.uniqueVisitor!= null && !lis.uniqueVisitor.equals("")){
						count3=count3+Double.parseDouble(lis.uniqueVisitor);
						}
					if(lis.action != null && !lis.action.equals("")){
					count7=count7+Double.parseDouble(lis.action);
					}
				 
				 
			 }
			 
			 double countAll1=0.0;
				double countAll2=0.0;
				double countAll3=0.0;
				double countAll4=0.0;
				double countAll5=0.0;
				double countAll6=0.0;
				double countAll7=0.0;
				 for(ClickyVisitorEngagementAction list:allbrowserlist){
		      	String titleNew=list.title;
				if(!titleNew.contains("1-10 actions")){
		      	if(list.averageAction != null && !list.averageAction.equals("")){
						 countAll1=countAll1+Double.parseDouble(list.averageAction);
			     	}
					if(list.bounceRate != null && !list.bounceRate.equals("")){
						 countAll6=countAll6+Double.parseDouble(list.bounceRate);	
						     	}
					if(list.averageTime != null && !list.averageTime.equals("")){
						countAll5=countAll5+Double.parseDouble(list.averageTime);
						}
					if(list.totalTime != null && !list.totalTime.equals("")){
						 countAll4=countAll4+Double.parseDouble(list.totalTime);
						}
					if(list.visitors != null && !list.visitors.equals("")){
						countAll2=countAll2+Double.parseDouble(list.visitors);
						}
					if(list.uniqueVisitor!= null && !list.uniqueVisitor.equals("")){
						countAll3=countAll3+Double.parseDouble(list.uniqueVisitor);
						}
					if(list.action != null && !list.action.equals("")){
						 countAll7=countAll7+Double.parseDouble(list.action);
					}
				 
				}
			   			
				 }
			 
				 ClickyPlatformVM cVm = new ClickyPlatformVM();
				 cVm.title = "visitors";
				 cVm.these_visitors =  count2;
				 cVm.all_visitors = countAll2;
				 cVm.images = "//con.tent.network/media/icon_visitors.gif";
				 if(countAll2 !=0){
				 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
				 }
				 else{
					 cVm.difference =0.0;
				 }
				 platformvm.add(cVm);
				 
				 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
				 cVm1.title = "uniqueV";
				 cVm1.these_visitors = count3;
				 cVm1.all_visitors = countAll3;
				 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
				
				 
				 if(countAll3 !=0){
					 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
					 }
					 else{
						 cVm1.difference =0.0;
					 }
				 
				 platformvm.add(cVm1);
				 
				 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
				 cVm2.title = "action";
				 cVm2.these_visitors = count7;
				 cVm2.all_visitors = countAll7;
				 cVm2.images = "//con.tent.network/media/icon_click.gif";
				
				 
				 if(countAll7 !=0){
					 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
					 }
					 else{
						 cVm2.difference =0.0;
					 }
				 
				 
				 platformvm.add(cVm2);
				 
				 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
				 cVm3.title = "averageAct";
				 cVm3.these_visitors = count7/count2;
				 cVm3.all_visitors = countAll7/countAll2;
				 cVm3.images = "//con.tent.network/media/icon_click.gif";
				
				 if(countAll3 !=0){
					 cVm3.difference = ((count1 - (countAll7/countAll3)) / (countAll7/countAll3)) * 100;
					 }
					 else{
						 cVm3.difference =0.0;
					 }
				 platformvm.add(cVm3);
				 
				 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
				 cVm4.title = "totalT";
				 cVm4.these_visitors = count4;
				 cVm4.all_visitors = countAll4;
				 cVm4.images = "//con.tent.network/media/icon_time.gif";
				
				 if(countAll4 !=0){
					 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
					 }
					 else{
						 cVm4.difference =0.0;
					 }
				 platformvm.add(cVm4);
				 
				 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
				 cVm5.title = "averageT";
				 cVm5.these_visitors = count4/count2;
				 cVm5.all_visitors = countAll4/countAll2;
				 cVm5.images = "//con.tent.network/media/icon_time.gif";
				 
				 if(countAll5 !=0){
					 cVm5.difference = ((count5 - (countAll4/countAll3)) / (countAll4/countAll3)) * 100;
					 }
					 else{
						 cVm5.difference =0.0;
					 }
				 platformvm.add(cVm5);
				 
				 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
				 cVm6.title = "bounceR";
				 cVm6.these_visitors = count6;
				 cVm6.all_visitors = countAll6;
				 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
				 if(countAll6 !=0){
					 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
				 }
				 else{
					 cVm6.difference = 0.0;
				 }
				 platformvm.add(cVm6);
			 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

		 	
		 	return ok(Json.toJson(platformvm));


	    }
	 
	 public static Result getTrafficSourceData(String title,String startdate,String enddate){
	    	

		    Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
				 d1 = format.parse(startdate);
		         d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			
			List<ClickyVisitorTrafficSource> browserObjList = ClickyVisitorTrafficSource.findByTitleAndDate(title, d1, d2);
			List<ClickyVisitorTrafficSource> allbrowserlist = ClickyVisitorTrafficSource.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickyVisitorTrafficSource lis:browserObjList){
		     	if(lis.averageAction != null){
		     		count1=count1+Double.parseDouble(lis.averageAction);
		     	}
				if(lis.bounceRate != null){
					count6=count6+Double.parseDouble(lis.bounceRate);	
					     	}
				if(lis.averageTime != null){
					count5=count5+Double.parseDouble(lis.averageTime);	
					}
				if(lis.totalTime != null){
					count4=count4+Double.parseDouble(lis.totalTime);
					}
				if(lis.visitors != null){
					 count2=count2+Double.parseDouble(lis.visitors);
					}
				if(lis.uniqueVisitor!= null){
					count3=count3+Double.parseDouble(lis.uniqueVisitor);
					}
				if(lis.action != null){
				count7=count7+Double.parseDouble(lis.action);
				}
			   			
			 }
			 
			 double countAll1=0.0;
				double countAll2=0.0;
				double countAll3=0.0;
				double countAll4=0.0;
				double countAll5=0.0;
				double countAll6=0.0;
				double countAll7=0.0;
				 for(ClickyVisitorTrafficSource list:allbrowserlist){
					 if(list.averageAction != null){
						 countAll1=countAll1+Double.parseDouble(list.averageAction);
				     	}
						if(list.bounceRate != null){
							 countAll6=countAll6+Double.parseDouble(list.bounceRate);	
							     	}
						if(list.averageTime != null){
							countAll5=countAll5+Double.parseDouble(list.averageTime);	
							}
						if(list.totalTime != null  && !list.totalTime.equals("")){
							 countAll4=countAll4+Double.parseDouble(list.totalTime);
							}
						if(list.visitors != null){
							countAll2=countAll2+Double.parseDouble(list.visitors);
							}
						if(list.uniqueVisitor != null){
							countAll3=countAll3+Double.parseDouble(list.uniqueVisitor);
							}
						if(list.action != null && !list.action.equals("")){
							 countAll7=countAll7+Double.parseDouble(list.action);
						}
					 
					 
			   			
				 }
			 
				 ClickyPlatformVM cVm = new ClickyPlatformVM();
				 cVm.title = "visitors";
				 cVm.these_visitors =  count2;
				 cVm.all_visitors = countAll2;
				 cVm.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
				 platformvm.add(cVm);
				 
				 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
				 cVm1.title = "uniqueV";
				 cVm1.these_visitors = count3;
				 cVm1.all_visitors = countAll3;
				 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
				 platformvm.add(cVm1);
				 
				 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
				 cVm2.title = "action";
				 cVm2.these_visitors = count7;
				 cVm2.all_visitors = countAll7;
				 cVm2.images = "//con.tent.network/media/icon_click.gif";
				 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
				 platformvm.add(cVm2);
				 
				 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
				 cVm3.title = "averageAct";
				 cVm3.these_visitors = count7/count2;
				 cVm3.all_visitors = countAll7/countAll2;
				 cVm3.images = "//con.tent.network/media/icon_click.gif";
				 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
				 platformvm.add(cVm3);
				 
				 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
				 cVm4.title = "totalT";
				 cVm4.these_visitors = count4;
				 cVm4.all_visitors = countAll4;
				 cVm4.images = "//con.tent.network/media/icon_time.gif";
				 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
				 platformvm.add(cVm4);
				 
				 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
				 cVm5.title = "averageT";
				 cVm5.these_visitors = count4/count2;
				 cVm5.all_visitors = countAll4/countAll2;
				 cVm5.images = "//con.tent.network/media/icon_time.gif";
				 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
				 platformvm.add(cVm5);
				 
				 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
				 cVm6.title = "bounceR";
				 cVm6.these_visitors = count6;
				 cVm6.all_visitors = countAll6;
				 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
				 if(countAll6 !=0){
					 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
				 }
				 else{
					 cVm6.difference = 0.0;
				 }
				 platformvm.add(cVm6);
			 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

		 	
		 	return ok(Json.toJson(platformvm));


	    }
	 
	    public static Result getbrowserdata(String title , String startdate, String enddate){
    		Date d1= null;
    		Date d2= null;
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    		try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
    		
    		List<ClickyPlatformBrowser> browserObjList = ClickyPlatformBrowser.findByTitleAndDate(title, d1, d2);
    		List<ClickyPlatformBrowser> allbrowserlist = ClickyPlatformBrowser.getAll(d1, d2);
    		List <ClickyPagesVM> VMs = new ArrayList<>();
    		List<ClickyPlatformVM> platformvm =new ArrayList<>();
    		ClickyPagesVM vm = new ClickyPagesVM();
    		double count1=0.0;
    		double count2=0.0;
    		double count3=0.0;
    		double count4=0.0;
    		double count5=0.0;
    		double count6=0.0;
    		double count7=0.0;
    		 for(ClickyPlatformBrowser lis:browserObjList){
             	
    			 count1=count1+Double.parseDouble(lis.averageAction1);
    			 count2=count2+Double.parseDouble(lis.visitors1);
    			 count3=count3+Double.parseDouble(lis.uniqueVisitor1);
    			 count4=count4+Double.parseDouble(lis.totalTime1);
    			 count5=count5+Double.parseDouble(lis.averageTime1);
    			 count6=count6+Double.parseDouble(lis.bounceRate1);
    			 count7=count7+Double.parseDouble(lis.action1);
       	   			
    		 }
    		 
    		 double countAll1=0.0;
     		double countAll2=0.0;
     		double countAll3=0.0;
     		double countAll4=0.0;
     		double countAll5=0.0;
     		double countAll6=0.0;
     		double countAll7=0.0;
     		 for(ClickyPlatformBrowser list:allbrowserlist){
              	
     			 countAll1=countAll1+Double.parseDouble(list.averageAction1);
     			 countAll2=countAll2+Double.parseDouble(list.visitors1);
     			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor1);
     			 countAll4=countAll4+Double.parseDouble(list.totalTime1);
     			 countAll5=countAll5+Double.parseDouble(list.averageTime1);
     			 countAll6=countAll6+Double.parseDouble(list.bounceRate1);
     			 countAll7=countAll7+Double.parseDouble(list.action1);
        	   			
     		 }
    		 
     		ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count7/count2;
			 cVm3.all_visitors = countAll7/countAll2;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count4/count2;
			 cVm5.all_visitors = countAll4/countAll2;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);
    		 
    		 vm.averageAct=count1;
    		 vm.visitor=count2;
    		 vm.uniqueV=count3;
    		 vm.totalT=count4;
    		 vm.averageT=count5;
    		 vm.bounceR=count6;
    		 vm.action=count7;
    		
    		 VMs.add(vm);
    
	     	
	     	
    		 
	     	return ok(Json.toJson(platformvm));
    		
    	}
	 
	    public static Result getOperatingSystemdata(String title , String startdate, String enddate){
			Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			
			List<ClickyPlatformOperatingSystem> operatingObjList = ClickyPlatformOperatingSystem.findByTitleAndDate(title, d1, d2);
			List<ClickyPlatformOperatingSystem> allOSlist = ClickyPlatformOperatingSystem.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickyPlatformOperatingSystem lis:operatingObjList){
	         	
				 count1=count1+Double.parseDouble(lis.averageAction1);
				 count2=count2+Double.parseDouble(lis.visitors1);
				 count3=count3+Double.parseDouble(lis.uniqueVisitor1);
				 count4=count4+Double.parseDouble(lis.totalTime1);
				 count5=count5+Double.parseDouble(lis.averageTime1);
				 count6=count6+Double.parseDouble(lis.bounceRate1);
				 count7=count7+Double.parseDouble(lis.action1);
	         	
			 }
			 
			 double countAll1=0.0;
	 		double countAll2=0.0;
	 		double countAll3=0.0;
	 		double countAll4=0.0;
	 		double countAll5=0.0;
	 		double countAll6=0.0;
	 		double countAll7=0.0;
	 		 for(ClickyPlatformOperatingSystem list:allOSlist){
	          	
	 			 countAll1=countAll1+Double.parseDouble(list.averageAction1);
	 			 countAll2=countAll2+Double.parseDouble(list.visitors1);
	 			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor1);
	 			 countAll4=countAll4+Double.parseDouble(list.totalTime1);
	 			 countAll5=countAll5+Double.parseDouble(list.averageTime1);
	 			 countAll6=countAll6+Double.parseDouble(list.bounceRate1);
	 			 countAll7=countAll7+Double.parseDouble(list.action1);
	    	   			
	 		 }
			 
	 		ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count1/count2;
			 cVm3.all_visitors = countAll1/countAll2;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count5/count2;
			 cVm5.all_visitors = countAll5/countAll2;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);
			 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

	     	
	     	
			 
	     	return ok(Json.toJson(platformvm));
			
		}
	    
	    
	    public static Result getResolutiondata(String title , String startdate, String enddate){
			Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			
			List<ClickyPlatformScreen> screenObjList = ClickyPlatformScreen.findByTitleAndDate(title, d1, d2);
			List<ClickyPlatformScreen> allscreenlist = ClickyPlatformScreen.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickyPlatformScreen lis:screenObjList){
	         	
				 count1=count1+Double.parseDouble(lis.averageAction1);
				 count2=count2+Double.parseDouble(lis.visitors1);
				 count3=count3+Double.parseDouble(lis.uniqueVisitor1);
				 count4=count4+Double.parseDouble(lis.totalTime1);
				 count5=count5+Double.parseDouble(lis.averageTime1);
				 count6=count6+Double.parseDouble(lis.bounceRate1);
				 count7=count7+Double.parseDouble(lis.action1);
	   	   			
			 }
			 
			 double countAll1=0.0;
	 		double countAll2=0.0;
	 		double countAll3=0.0;
	 		double countAll4=0.0;
	 		double countAll5=0.0;
	 		double countAll6=0.0;
	 		double countAll7=0.0;
	 		 for(ClickyPlatformScreen list:allscreenlist){
	          	
	 			 countAll1=countAll1+Double.parseDouble(list.averageAction1);
	 			 countAll2=countAll2+Double.parseDouble(list.visitors1);
	 			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor1);
	 			 countAll4=countAll4+Double.parseDouble(list.totalTime1);
	 			 countAll5=countAll5+Double.parseDouble(list.averageTime1);
	 			 countAll6=countAll6+Double.parseDouble(list.bounceRate1);
	 			 countAll7=countAll7+Double.parseDouble(list.action1);
	    	   			
	 		 }
			 
	 		ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count7/count2;
			 cVm3.all_visitors = countAll7/countAll2;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count4/count2;
			 cVm5.all_visitors = countAll4/countAll2;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);
			 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

	     	
	     	
			 
	     	return ok(Json.toJson(platformvm));
			
		}
	    
	    public static Result getHardwaredata(String title , String startdate, String enddate){
			Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			
			List<ClickyPlatformHardware> hardwareObjList = ClickyPlatformHardware.findByTitleAndDate(title, d1, d2);
			List<ClickyPlatformHardware> allhardwarelist = ClickyPlatformHardware.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickyPlatformHardware lis:hardwareObjList){
	         	
				 count1=count1+Double.parseDouble(lis.averageAction1);
				 count2=count2+Double.parseDouble(lis.visitors1);
				 count3=count3+Double.parseDouble(lis.uniqueVisitor1);
				 count4=count4+Double.parseDouble(lis.totalTime1);
				 count5=count5+Double.parseDouble(lis.averageTime1);
				 count6=count6+Double.parseDouble(lis.bounceRate1);
				 count7=count7+Double.parseDouble(lis.action1);
	   	   			
			 }
			 
			 double countAll1=0.0;
	 		double countAll2=0.0;
	 		double countAll3=0.0;
	 		double countAll4=0.0;
	 		double countAll5=0.0;
	 		double countAll6=0.0;
	 		double countAll7=0.0;
	 		 for(ClickyPlatformHardware list:allhardwarelist){
	          	
	 			 countAll1=countAll1+Double.parseDouble(list.averageAction1);
	 			 countAll2=countAll2+Double.parseDouble(list.visitors1);
	 			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor1);
	 			 countAll4=countAll4+Double.parseDouble(list.totalTime1);
	 			 countAll5=countAll5+Double.parseDouble(list.averageTime1);
	 			 countAll6=countAll6+Double.parseDouble(list.bounceRate1);
	 			 countAll7=countAll7+Double.parseDouble(list.action1);
	    	   			
	 		 }
			 
	 		ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count1;
			 cVm3.all_visitors = countAll1;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count4/countAll2;
			 cVm5.all_visitors = countAll4/countAll2;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);		 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);
			 return ok(Json.toJson(platformvm));
		}
	    
	    public static Result getVisitorList(String startDate,String endDate){
	    	int year = Calendar.getInstance().get(Calendar.YEAR);
	    	String params = null;
	    	
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	Date sDate = null;
	    	Date eDate = null;
	    	
	    	try {
				sDate = df.parse(startDate);
				eDate = df.parse(endDate); 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	List<ClickyVisitorsList> cList = ClickyVisitorsList.getAll(sDate, eDate);
	    	
	    	
	    	return ok(Json.toJson(cList));
	    }
	    
	    public static Result getActionListData(String startDate,String endDate){
	    	int year = Calendar.getInstance().get(Calendar.YEAR);
	    	String params = null;
	    	
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	Date sDate = null;
	    	Date eDate = null;
	    	
	    	try {
				sDate = df.parse(startDate);
				eDate = df.parse(endDate); 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	List<ClickyActionList> cList = ClickyActionList.getAll(sDate, eDate);
	   
	    	List<ClickyPagesVM> infoVMList = new ArrayList<>();
	    	for(ClickyActionList cList1 : cList)
	    	{
	    		ClickyPagesVM vm = new ClickyPagesVM();
	    		List<ClickyVisitorsList> cLists = ClickyVisitorsList.getIPAddress(cList1.ip_address);
	    		if(cLists.size() != 0){
	    			vm.ipAddress = cLists.get(0).ipAddress;
	    			vm.organization = cLists.get(0).organization;
	        		vm.geoLocation = cLists.get(0).geolocation;
	        		vm.operatingSystem = cLists.get(0).operatingSystem;
	        		vm.webBrowser = cLists.get(0).webBrowser;
	        		vm.landingPage = cLists.get(0).landingPage;
	        		vm.referrerUrl = cLists.get(0).referrerUrl;
	        		vm.vid = cLists.get(0).id;
	    		}
	    		
	    		vm.time = cList1.time;
	    		vm.timePretty = cList1.time_pretty;
	    		vm.actionType = cList1.action_type;
	    		vm.actionTitle =cList1.action_title;
	    		vm.actionUrl = cList1.action_url;
	    		vm.statsUrl = cList1.stats_url;
	    		vm.curr_Date = cList1.currDate;
	    		vm.referrerDomain = cList1.referrer_domain;
	    		
	    		infoVMList.add(vm);
	    	}
	    	
	    	
	    	return ok(Json.toJson(infoVMList));
	    }
	    
	    public static Result getEngagementAct(String startDate,String endDate){
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);
	    	  } catch (Exception e) {
	                 e.printStackTrace();
	             }  
	             long diff = d2.getTime() - d1.getTime();

	             long diffMinutes = diff / (60 * 1000) % 60;
	             long diffHours = diff / (60 * 60 * 1000) % 24;
	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays+1;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	          Date newDat=DateUtils.addDays(d1, -1);
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             Map<String, String> mapOffline = new HashMap<String, String>();
	             List <ClickyVisitorEngagementAction> list=ClickyVisitorEngagementAction.getAll(d1, d2) ;
	               Integer valueCount=0;
	               double perCount=0;
	   	            for(ClickyVisitorEngagementAction lis:list){
	            	ClickyPagesVM vm = new ClickyPagesVM();
	      	     	   	vm.id=lis.id;
	      				String value=lis.value;
	      				String valuePercent=null;
	      				if(lis.valuePercent != null){
	      					valuePercent = lis.valuePercent;
	      				}
	      				else{
	      					valuePercent ="0";
	      				}
	      				String title = lis.title;
	      				valueCount=Integer.parseInt(value);
	      				perCount=Double.parseDouble(valuePercent);
	      		     				String objectMake = mapOffline.get(title);
	      		        			if (objectMake == null) {
	      		        				mapOffline.put(title, valueCount+"&"+perCount);
	      		        			}else{
	      		        				 String arr[]=mapOffline.get(title).split("&");
	      		        				mapOffline.put(title, valueCount+Integer.parseInt(arr[0])+"&"+(perCount+Double.parseDouble(arr[1])));
	      		        			}
	   		
	   	           }
	   	            
	   	         List<bodyStyleSetVM> bSetVMsoffline = new ArrayList<>();
			     	for (Entry<String , String> entryValue : mapOffline.entrySet()) {
			     		ClickyPagesVM vm = new ClickyPagesVM();
						vm.title = entryValue.getKey();
						String arr[]=entryValue.getValue().split("&");
						vm.value = arr[0];
						vm.valuePercent=arr[1];
						
						double count=0;
		      	   	    double count1=0;
		      	   	 List <ClickyVisitorEngagementAction> list1=ClickyVisitorEngagementAction.getAll(d1, d2) ;
		      	   	for(ClickyVisitorEngagementAction lis2:list1) {
		    	    	String url = lis2.title;
		    	   		if(url.equals(vm.title)){
		    	   			vm.value_percent2 = lis2.value;
		    	   		  count1=count1+Double.parseDouble(vm.value_percent2);
		    	   		
		    	   		}
		    	   		
		      	   	}	
		      	   	
		      	  double newValuePer=0.0;
	  	      	 for(ClickyVisitorEngagementAction lis2:list1) {
		    	    	String title = lis2.title;
		    	   		if(!title.equals("1-10 actions")){
		    	   			vm.value_percent2 = lis2.value;
		    	   		newValuePer=newValuePer+Double.parseDouble(vm.value_percent2);
		    	   		
		    	   		}
		    	   		
		      	   	}	
	  	      	 vm.newValuePrecentage=(Double.parseDouble(vm.value)/newValuePer)*100; 	
		      	   	
		      	  List <ClickyVisitorEngagementAction> list2=ClickyVisitorEngagementAction.getAll(beforeStart, newDat) ;
		        	   	for(ClickyVisitorEngagementAction lis2:list2) {
		        	    	String url = lis2.title;
		        	   		if(url.equals(vm.title)){
		        	   			vm.value_percent2 = lis2.value;
		        	   		  count=count+Double.parseDouble(vm.value_percent2);
		        	   		
		        	   		}
		      	   			
		        	   	}
		        	   	
		        	   	
		        	   	if(count != 0 & !vm.value.equals("0")){
		        	   vm.averagePercent=(((count1-count)*100)/count);
		        	   	}
		        	   	else{
		        	   		vm.averagePercent=0;
		        	   	}
						vm.secondCount=count;
						
						
						
						clickyList.add(vm);
					}
			     	return ok(Json.toJson(clickyList));
		    
	    }

	    
	    public static Result getEngagementTime(String startDate,String endDate){
	    	String params = null;
		    
		    Date d1 = null;
	        Date d2 = null;
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffMinutes = diff / (60 * 1000) % 60;
	             long diffHours = diff / (60 * 60 * 1000) % 24;
	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays+1;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	          Date newDat=DateUtils.addDays(d1, -1);
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             List <ClickyVisitorEngagementTime> list=ClickyVisitorEngagementTime.getAll(d1, d2) ;
	                  Map<String, String> mapOffline = new HashMap<String, String>();
	                    Integer valueCount=0;
	                    double perCount=0;
	        	            for(ClickyVisitorEngagementTime lis:list){
	                 	ClickyPagesVM vm = new ClickyPagesVM();
	           	     	   	vm.id=lis.id;
	           				String value=lis.value;
	           				String valuePercent=null;
	           				if(lis.valuePercent != null){
	           					valuePercent = lis.valuePercent;
	           				}
	           				else{
	           					valuePercent ="0";
	           				}
	           				String title = lis.title;
	           				valueCount=Integer.parseInt(value);
	           				perCount=Double.parseDouble(valuePercent);
	           		     				String objectMake = mapOffline.get(title);
	           		        			if (objectMake == null) {
	           		        				mapOffline.put(title, valueCount+"/"+perCount);
	           		        			}else{
	           		        				 String arr[]=mapOffline.get(title).split("/");
	           		        				mapOffline.put(title, valueCount+Integer.parseInt(arr[0])+"/"+(perCount+Double.parseDouble(arr[1])));
	           		        			}
	        		
	        	           }
	        	            
	        	         List<bodyStyleSetVM> bSetVMsoffline = new ArrayList<>();
	     		     	for (Entry<String , String> entryValue : mapOffline.entrySet()) {
	     		     		ClickyPagesVM vm = new ClickyPagesVM();
	     					vm.title = entryValue.getKey();
	     					String arr[]=entryValue.getValue().split("/");
	     					vm.value = arr[0];
	     					vm.valuePercent=arr[1];
	     					
	     					double count=0;
	     	      	   	    double count1=0;
	     	      	   	 List <ClickyVisitorEngagementTime> list1=ClickyVisitorEngagementTime.getAll(d1, d2) ;
	     	      	   	for(ClickyVisitorEngagementTime lis2:list1) {
	     	    	    	String url = lis2.title;
	     	    	   		if(url.equals(vm.title)){
	     	    	   			vm.value_percent2 = lis2.value;
	     	    	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	     	    	   		
	     	    	   		}
	     	    	   		
	     	      	   	}	
	     	      	   	double newValuePer=0.0;
	     	      	 for(ClickyVisitorEngagementTime lis2:list1) {
	  	    	    	String title = lis2.title;
	  	    	   		if(!title.equals("&amp;lt;10m")){
	  	    	   			vm.value_percent2 = lis2.value;
	  	    	   		newValuePer=newValuePer+Double.parseDouble(vm.value_percent2);
	  	    	   		
	  	    	   		}
	  	    	   		
	  	      	   	}	
	     	      	 vm.newValuePrecentage=(Double.parseDouble(vm.value)/newValuePer)*100; 	
	     	      	  List <ClickyVisitorEngagementTime> list2=ClickyVisitorEngagementTime.getAll(beforeStart, newDat) ;
	     	        	   	for(ClickyVisitorEngagementTime lis2:list2) {
	     	        	    	String url = lis2.title;
	     	        	   		if(url.equals(vm.title)){
	     	        	   			vm.value_percent2 = lis2.value;
	     	        	   		  count=count+Double.parseDouble(vm.value_percent2);
	     	        	   		
	     	        	   		}
	     	      	   			
	     	        	   	}
	     	        		if(count != 0 & !vm.value.equals("0")){
	     	        	   vm.averagePercent=((count1-count)/count)*100;
	     	        	   	}
	     	        	   	else{
	     	        	   		vm.averagePercent=0;
	     	        	   	}
	     					
	     					
	     					
	     					
	     					clickyList.add(vm);
	     				}
	             

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
		}
	    
	    public static Result getTrafficScoures(String startDate,String endDate){
	    	String params = null;
	    	params = "&type=traffic-sources&date="+startDate+","+endDate+"&limit=all";

		    Date d1 = null;
	        Date d2 = null;
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             //in milliseconds
	             long diff = d2.getTime() - d1.getTime();

	            // long diffSeconds = diff / 1000 % 60;
	             long diffMinutes = diff / (60 * 1000) % 60;
	             long diffHours = diff / (60 * 60 * 1000) % 24;
	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	                    
	             List <ClickyVisitorTrafficSource> list=ClickyVisitorTrafficSource.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	               Integer valueCount=0;
	               double perCount=0;
	               double avgActCount=0;
	               double avgTimeCount=0;
	               double totTimeCount=0;
	               double bounRateCount=0;
	   	            for(ClickyVisitorTrafficSource lis:list){
	            	ClickyPagesVM vm = new ClickyPagesVM();
	      	     	   	vm.id=lis.id;
	      				String value=lis.value;
	      				String valuePercent=null;
	      				if(lis.valuePercent != null){
	      					valuePercent = lis.valuePercent;
	      				}
	      				else{
	      					valuePercent ="0";
	      				}
	      				String averageActions=lis.averageAction;
	      				String averageTime=lis.averageTime;
	      				String totalTime=lis.totalTime;
	      				String bounceRate=lis.bounceRate;
	      				
	      				
	      				String title = lis.title;
	      				valueCount=Integer.parseInt(value);
	      				perCount=Double.parseDouble(valuePercent);
	      				avgActCount=Double.parseDouble(averageActions);
	      				avgTimeCount=Double.parseDouble(averageTime);
	      				totTimeCount=Double.parseDouble(totalTime);
	      				bounRateCount=Double.parseDouble(bounceRate);
	      		     				String objectMake = mapOffline.get(title);
	      		        			if (objectMake == null) {
	      		        				mapOffline.put(title, valueCount+"&"+perCount+"&"+avgActCount+"&"+avgTimeCount+"&"+totTimeCount+"&"+bounRateCount);
	      		        			}else{
	      		        				 String arr[]=mapOffline.get(title).split("&");
	      		        				mapOffline.put(title, valueCount+Integer.parseInt(arr[0])+"&"+(perCount+Double.parseDouble(arr[1]))+"&"+(avgActCount+Double.parseDouble(arr[2]))
	      		        						+"&"+(avgTimeCount+Double.parseDouble(arr[3]))+"&"+(totTimeCount+Double.parseDouble(arr[4]))+"&"+(bounRateCount+Double.parseDouble(arr[5])));
	      		        			}
	   		
	   	           }
	   	            
	   	         List<bodyStyleSetVM> bSetVMsoffline = new ArrayList<>();
			     	for (Entry<String , String> entryValue : mapOffline.entrySet()) {
			     		ClickyPagesVM vm = new ClickyPagesVM();
						vm.title = entryValue.getKey();
						String arr[]=entryValue.getValue().split("&");
						vm.value = arr[0];
						vm.valuePercent=arr[1];
						vm.averageActions=arr[2];
						vm.averageTime=arr[3];
						vm.totalTime=arr[4];
						vm.bounceRate=arr[5];
						double count=0;
		      	   	    double count1=0;
		      	   	 List <ClickyVisitorTrafficSource> list1=ClickyVisitorTrafficSource.getAll(d1, d2) ;
		      	   	for(ClickyVisitorTrafficSource lis2:list1) {
		    	    	String url = lis2.title;
		    	   		if(url.equals(vm.title)){
		    	   			vm.value_percent2 = lis2.value;
		    	   		  count1=count1+Double.parseDouble(vm.value_percent2);
		    	   		
		    	   		}
		    	   		
		      	   	}	
		      	  List <ClickyVisitorTrafficSource> list2=ClickyVisitorTrafficSource.getAll(beforeStart, d1) ;
		        	   	for(ClickyVisitorTrafficSource lis2:list2) {
		        	    	String url = lis2.title;
		        	   		if(url.equals(vm.title)){
		        	   			vm.value_percent2 = lis2.value;
		        	   		  count=count+Double.parseDouble(vm.value_percent2);
		        	   		
		        	   		}
		      	   			
		        	   	}
		        	   	double valueper = 0.0;
		        	   	
		        	   	for(ClickyVisitorTrafficSource lis2:list1)
		        	   	{
		        	   		 valueper = valueper + Double.parseDouble(lis2.value);
		        	   		 
		        	   	}
		        	   	vm.percentage = (Double.parseDouble(vm.value)/valueper)*100;
		        	   	
		        	   	double val = 0.0;
		        	   	double act = 0.0;
		        	    double tim = 0.0;
		        	    double ttim = 0.0;
		        	    double rows = 0;
		        	   	for(ClickyVisitorTrafficSource lis2:list1)
		        	   	{
		        	   		String tit = lis2.title;
		        	   				if(tit.equals(vm.title)){
		        	   		 act = act + Double.parseDouble(lis2.action);
		        	   		 val = val + Double.parseDouble(lis2.value);
		        	   		 tim = tim + Double.parseDouble(lis2.averageTime);
		        	   		 ttim = ttim + Double.parseDouble(lis2.totalTime);
		        	   		 rows++;
		        	   				}
		        	   	}
		        	   	
		        	   	vm.avgaction = act/val;
		        	   	vm.avgtime = tim/rows;
		        	   	vm.totTime = ttim;
		        	   	
		        	   /*	if(count1 != 0){
		        	   vm.averagePercent=((count1-count)/count1)*100;
		        	   	}
		        	   	else{
		        	   		vm.averagePercent=0;
		        	   	}*/
		        		if(count != 0 & !vm.value.equals("0")){
			 	        	   vm.averagePercent=(((count1-count)*100)/count);
			 	        	   	}
			 	        	   	else{
			 	        	   		vm.averagePercent=0;
			 	        	   	}
						
						clickyList.add(vm);
					}

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
		    
	    }
	    
	    public static Result getActiveVisitors(String startDate,String endDate){
	    	String params = null;
	    	params = "&type=visitors-most-active&date="+startDate+","+endDate+"&limit=all";
		    //return ok(Json.parse(callClickAPI(params)));
	    	
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             
	     	   	JsonNode jsonList = Json.parse(callClickAPI(params));
	     	   	
	     	   	for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
	     	 	
	     	   	ClickyPagesVM vm = new ClickyPagesVM();
				vm.value = obj.get("value").textValue();
				//vm.geoLocation = obj.get("geoLocation").textValue();
				//vm.ipAddress = obj.get("ipAddress").textValue();
				vm.value_percent = obj.get("value_percent").textValue();
				vm.title = obj.get("title").textValue();
				
	     	  
				List<ClickyVisitorActiveVisitor> list=ClickyVisitorActiveVisitor.getAll(d1,d2);
		 	   	for(ClickyVisitorActiveVisitor lis:list) {
		 	   	//ClickyPagesVM vm = new ClickyPagesVM();
		 	   
			   	List<ClickyVisitorsList> list1=ClickyVisitorsList.findByTitle(vm.title);
		 	   	if(list1.size() != 0){
		 	   		System.out.println("in if condition");
		 	   	vm.geoLocation=list1.get(0).geolocation;
		 	   	vm.organization=list1.get(0).organization;
		 	    
		 	   	}
		 	   
		 	   	
		 	   	}
				
	     	   	
	     	   clickyList.add(vm);
	     	   	
	     	   	}
	    	 } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));

	    
	    }
	    
	    public static Result getEngActionChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String url=vm1.url;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=url;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 Long l=0L;
	                 //JsonNode jsonList = Json.parse(callClickAPI("&type=downloads&date="+startD+"&limit=all"));
	                 List<ClickyVisitorEngagementAction> list=ClickyVisitorEngagementAction.getAllData(d);
	                 
	                 for(ClickyVisitorEngagementAction lis:list) {
	              	   	if(url.equals(lis.title)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value =lis.value;
	              	  l=l+(long)Integer.parseInt(value);
	              	   	}
	              	   	}
	             	  lonnn.add(l);
	             	   	String chartDate=format1.format(d);
	             	     dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getEngTimeChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String url=vm1.url;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=url;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 Long l=0L;
	                 //JsonNode jsonList = Json.parse(callClickAPI("&type=downloads&date="+startD+"&limit=all"));
	                 List<ClickyVisitorEngagementTime> list=ClickyVisitorEngagementTime.getAllData(d);
	                 
	                 for(ClickyVisitorEngagementTime lis:list) {
	              	   	if(url.equals(lis.title)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value =lis.value;
	              	  l=l+(long)Integer.parseInt(value);
	              	   	}
	              	   	}
	             	  lonnn.add(l);
	             	   	String chartDate=format1.format(d);
	             	     dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getTrafficChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String url=vm1.url;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=url;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 Long l=0L;
	                 //JsonNode jsonList = Json.parse(callClickAPI("&type=downloads&date="+startD+"&limit=all"));
	                 List<ClickyVisitorTrafficSource> list=ClickyVisitorTrafficSource.getAllData(d);
	                 
	                 for(ClickyVisitorTrafficSource lis:list) {
	              	   	if(url.equals(lis.title)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value =lis.value;
	              	  l=l+(long)Integer.parseInt(value);
	              	   	}
	              	   	}
	             	  lonnn.add(l);
	             	   	String chartDate=format1.format(d);
	             	     dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getEntranceData(Long id,String startdate,String enddate){
	    	
	    	ClickyEntranceList event=ClickyEntranceList.findById(id);
			String title=event.url;
			
		    Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
				 d1 = format.parse(startdate);
		         d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			
			List<ClickyEntranceList> browserObjList = ClickyEntranceList.findByTitleAndDate(title, d1, d2);
			List<ClickyEntranceList> allbrowserlist = ClickyEntranceList.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickyEntranceList lis:browserObjList){
		     	if(lis.averageAction != null && !lis.averageAction.equals("")){
		     		count1=count1+Double.parseDouble(lis.averageAction);
		     	}
				if(lis.bounceRate != null && !lis.bounceRate.equals("")){
					count6=count6+Double.parseDouble(lis.bounceRate);	
					     	}
				if(lis.averageTime != null && !lis.averageTime.equals("")){
					count5=count5+Double.parseDouble(lis.averageTime);	
					}
				if(lis.totalTime != null && !lis.totalTime.equals("")){
					count4=count4+Double.parseDouble(lis.totalTime);
					}
				if(lis.visitors != null && !lis.visitors.equals("")){
					 count2=count2+Double.parseDouble(lis.visitors);
					}
				if(lis.uniqueVisitor!= null && !lis.uniqueVisitor.equals("")){
					count3=count3+Double.parseDouble(lis.uniqueVisitor);
					}
				if(lis.action != null && !lis.action.equals("")){
				count7=count7+Double.parseDouble(lis.action);
				}
			   			
			 }
			 
			 double countAll1=0.0;
				double countAll2=0.0;
				double countAll3=0.0;
				double countAll4=0.0;
				double countAll5=0.0;
				double countAll6=0.0;
				double countAll7=0.0;
				 for(ClickyEntranceList list:allbrowserlist){
						
						
						if(list.averageAction != null && !list.averageAction.equals("")){
							 countAll1=countAll1+Double.parseDouble(list.averageAction);
				     	}
						if(list.bounceRate != null && !list.bounceRate.equals("")){
							 countAll6=countAll6+Double.parseDouble(list.bounceRate);	
							     	}
						if(list.averageTime != null && !list.averageTime.equals("")){
							countAll5=countAll5+Double.parseDouble(list.averageTime);
							}
						if(list.totalTime != null && !list.totalTime.equals("")){
							 countAll4=countAll4+Double.parseDouble(list.totalTime);
							}
						if(list.visitors != null && !list.visitors.equals("")){
							countAll2=count2+Double.parseDouble(list.visitors);
							}
						if(list.uniqueVisitor!= null && !list.uniqueVisitor.equals("")){
							countAll3=count3+Double.parseDouble(list.uniqueVisitor);
							}
						if(list.action != null && !list.action.equals("")){
							 countAll7=count7+Double.parseDouble(list.action);
						}
					 
			   			
				 }
			 
				 ClickyPlatformVM cVm = new ClickyPlatformVM();
				 cVm.title = "visitors";
				 cVm.these_visitors =  count2;
				 cVm.all_visitors = countAll2;
				 cVm.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
				 platformvm.add(cVm);
				 
				 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
				 cVm1.title = "uniqueV";
				 cVm1.these_visitors = count3;
				 cVm1.all_visitors = countAll3;
				 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
				 platformvm.add(cVm1);
				 
				 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
				 cVm2.title = "action";
				 cVm2.these_visitors = count7;
				 cVm2.all_visitors = countAll7;
				 cVm2.images = "//con.tent.network/media/icon_click.gif";
				 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
				 platformvm.add(cVm2);
				 
				 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
				 cVm3.title = "averageAct";
				 cVm3.these_visitors = count7/count2;
				 cVm3.all_visitors = countAll7/countAll2;
				 cVm3.images = "//con.tent.network/media/icon_click.gif";
				 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
				 platformvm.add(cVm3);
				 
				 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
				 cVm4.title = "totalT";
				 cVm4.these_visitors = count4;
				 cVm4.all_visitors = countAll4;
				 cVm4.images = "//con.tent.network/media/icon_time.gif";
				 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
				 platformvm.add(cVm4);
				 
				 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
				 cVm5.title = "averageT";
				 cVm5.these_visitors = count4/count2;
				 cVm5.all_visitors = countAll4/count2;
				 cVm5.images = "//con.tent.network/media/icon_time.gif";
				 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
				 platformvm.add(cVm5);
				 
				 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
				 cVm6.title = "bounceR";
				 cVm6.these_visitors = count6;
				 cVm6.all_visitors = countAll6;
				 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
				 if(countAll6 !=0){
					 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
				 }
				 else{
					 cVm6.difference = 0.0;
				 }
				 platformvm.add(cVm6);
			 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

		 	
		 	return ok(Json.toJson(platformvm));
	    	
	    }
	    
	    public static Result getPagesData(Long id,String startdate,String enddate){
	    	

		    Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
				 d1 = format.parse(startdate);
		         d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			ClickyPagesActionList event=ClickyPagesActionList.findById(id);
			String title=event.url;
			List<ClickyPagesActionList> pagesObjList = ClickyPagesActionList.findByTitleAndDate(title, d1, d2);
			List<ClickyPagesActionList> allpageslist = ClickyPagesActionList.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			for(ClickyPagesActionList lis:pagesObjList){
		     	if(lis.averageAction != null && !lis.averageAction.equals("")){
		     		count1=count1+Double.parseDouble(lis.averageAction);
		     	}
				if(lis.bounceRate != null && !lis.bounceRate.equals("")){
					count6=count6+Double.parseDouble(lis.bounceRate);	
					     	}
				if(lis.averageTime != null && !lis.averageTime.equals("")){
					count5=count5+Double.parseDouble(lis.averageTime);	
					}
				if(lis.totalTime != null && !lis.totalTime.equals("")){
					count4=count4+Double.parseDouble(lis.totalTime);
					}
				if(lis.visitors != null && !lis.visitors.equals("")){
					 count2=count2+Double.parseDouble(lis.visitors);
					}
				if(lis.uniqueVisitor!= null && !lis.uniqueVisitor.equals("")){
					count3=count3+Double.parseDouble(lis.uniqueVisitor);
					}
				if(lis.action != null && !lis.action.equals("")){
				count7=count7+Double.parseDouble(lis.action);
				}
			   			
			 }
			 
			 double countAll1=0.0;
				double countAll2=0.0;
				double countAll3=0.0;
				double countAll4=0.0;
				double countAll5=0.0;
				double countAll6=0.0;
				double countAll7=0.0;
				 for(ClickyPagesActionList list:allpageslist){
						
						
						if(list.averageAction != null && !list.averageAction.equals("")){
							 countAll1=countAll1+Double.parseDouble(list.averageAction);
				     	}
						if(list.bounceRate != null && !list.bounceRate.equals("")){
							 countAll6=countAll6+Double.parseDouble(list.bounceRate);	
							     	}
						if(list.averageTime != null && !list.averageTime.equals("")){
							countAll5=countAll5+Double.parseDouble(list.averageTime);
							}
						if(list.totalTime != null && !list.totalTime.equals("")){
							 countAll4=countAll4+Double.parseDouble(list.totalTime);
							}
						if(list.visitors != null && !list.visitors.equals("")){
							countAll2=countAll2+Double.parseDouble(list.visitors);
							}
						if(list.uniqueVisitor!= null && !list.uniqueVisitor.equals("")){
							countAll3=countAll3+Double.parseDouble(list.uniqueVisitor);
							}
						if(list.action != null && !list.action.equals("")){
							 countAll7=countAll7+Double.parseDouble(list.action);
						}
					 
			   			
				 }
				 ClickyPlatformVM cVm = new ClickyPlatformVM();
				 cVm.title = "visitors";
				 cVm.these_visitors =  count2;
				 cVm.all_visitors = countAll2;
				 cVm.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
				 platformvm.add(cVm);
				 
				 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
				 cVm1.title = "uniqueV";
				 cVm1.these_visitors = count3;
				 cVm1.all_visitors = countAll3;
				 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
				 platformvm.add(cVm1);
				 
				 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
				 cVm2.title = "action";
				 cVm2.these_visitors = count7;
				 cVm2.all_visitors = countAll7;
				 cVm2.images = "//con.tent.network/media/icon_click.gif";
				 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
				 platformvm.add(cVm2);
				 
				 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
				 cVm3.title = "averageAct";
				 cVm3.these_visitors = count1;
				 cVm3.all_visitors = countAll1;
				 cVm3.images = "//con.tent.network/media/icon_click.gif";
				 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
				 platformvm.add(cVm3);
				 
				 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
				 cVm4.title = "totalT";
				 cVm4.these_visitors = count4;
				 cVm4.all_visitors = countAll4;
				 cVm4.images = "//con.tent.network/media/icon_time.gif";
				 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
				 platformvm.add(cVm4);
				 
				 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
				 cVm5.title = "averageT";
				 cVm5.these_visitors = count5;
				 cVm5.all_visitors = countAll5;
				 cVm5.images = "//con.tent.network/media/icon_time.gif";
				 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
				 platformvm.add(cVm5);
				 
				 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
				 cVm6.title = "bounceR";
				 cVm6.these_visitors = count6;
				 cVm6.all_visitors = countAll6;
				 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
				 if(countAll6 !=0){
					 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
				 }
				 else{
					 cVm6.difference = 0.0;
				 }
				 platformvm.add(cVm6);
			 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

		 	
		 	return ok(Json.toJson(platformvm));


	    }
	    
	    public static Result getEventData(Long id,String startdate,String enddate){
	    	

		    Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
				 d1 = format.parse(startdate);
		         d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			ClickyContentEvent event=ClickyContentEvent.findById(id);
			String title=event.url;
			List<ClickyContentEvent> browserObjList = ClickyContentEvent.findByTitleAndDate(title, d1, d2);
			List<ClickyContentEvent> allbrowserlist = ClickyContentEvent.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			for(ClickyContentEvent lis:browserObjList){
		     	if(lis.averageAction != null && !lis.averageAction.equals("")){
		     		count1=count1+Double.parseDouble(lis.averageAction);
		     	}
				if(lis.bounceRate != null && !lis.bounceRate.equals("")){
					count6=count6+Double.parseDouble(lis.bounceRate);	
					     	}
				if(lis.averageTime != null && !lis.averageTime.equals("")){
					count5=count5+Double.parseDouble(lis.averageTime);	
					}
				if(lis.totalTime != null && !lis.totalTime.equals("")){
					count4=count4+Double.parseDouble(lis.totalTime);
					}
				if(lis.visitors != null && !lis.visitors.equals("")){
					 count2=count2+Double.parseDouble(lis.visitors);
					}
				if(lis.uniqueVisitor!= null && !lis.uniqueVisitor.equals("")){
					count3=count3+Double.parseDouble(lis.uniqueVisitor);
					}
				if(lis.action != null && !lis.action.equals("")){
				count7=count7+Double.parseDouble(lis.action);
				}
			   			
			 }
			 
			 double countAll1=0.0;
				double countAll2=0.0;
				double countAll3=0.0;
				double countAll4=0.0;
				double countAll5=0.0;
				double countAll6=0.0;
				double countAll7=0.0;
				 for(ClickyContentEvent list:allbrowserlist){
						
						
						if(list.averageAction != null && !list.averageAction.equals("")){
							 countAll1=countAll1+Double.parseDouble(list.averageAction);
				     	}
						if(list.bounceRate != null && !list.bounceRate.equals("")){
							 countAll6=countAll6+Double.parseDouble(list.bounceRate);	
							     	}
						if(list.averageTime != null && !list.averageTime.equals("")){
							countAll5=countAll5+Double.parseDouble(list.averageTime);
							}
						if(list.totalTime != null && !list.totalTime.equals("")){
							 countAll4=countAll4+Double.parseDouble(list.totalTime);
							}
						if(list.visitors != null && !list.visitors.equals("")){
							countAll2=countAll2+Double.parseDouble(list.visitors);
							}
						if(list.uniqueVisitor!= null && !list.uniqueVisitor.equals("")){
							countAll3=countAll3+Double.parseDouble(list.uniqueVisitor);
							}
						if(list.action != null && !list.action.equals("")){
							 countAll7=countAll7+Double.parseDouble(list.action);
						}
					 
			   			
				 }
				 ClickyPlatformVM cVm = new ClickyPlatformVM();
				 cVm.title = "visitors";
				 cVm.these_visitors =  count2;
				 cVm.all_visitors = countAll2;
				 cVm.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
				 platformvm.add(cVm);
				 
				 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
				 cVm1.title = "uniqueV";
				 cVm1.these_visitors = count3;
				 cVm1.all_visitors = countAll3;
				 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
				 platformvm.add(cVm1);
				 
				 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
				 cVm2.title = "action";
				 cVm2.these_visitors = count7;
				 cVm2.all_visitors = countAll7;
				 cVm2.images = "//con.tent.network/media/icon_click.gif";
				 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
				 platformvm.add(cVm2);
				 
				 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
				 cVm3.title = "averageAct";
				 cVm3.these_visitors = count1;
				 cVm3.all_visitors = countAll1;
				 cVm3.images = "//con.tent.network/media/icon_click.gif";
				 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
				 platformvm.add(cVm3);
				 
				 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
				 cVm4.title = "totalT";
				 cVm4.these_visitors = count4;
				 cVm4.all_visitors = countAll4;
				 cVm4.images = "//con.tent.network/media/icon_time.gif";
				 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
				 platformvm.add(cVm4);
				 
				 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
				 cVm5.title = "averageT";
				 cVm5.these_visitors = count5;
				 cVm5.all_visitors = countAll5;
				 cVm5.images = "//con.tent.network/media/icon_time.gif";
				 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
				 platformvm.add(cVm5);
				 
				 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
				 cVm6.title = "bounceR";
				 cVm6.these_visitors = count6;
				 cVm6.all_visitors = countAll6;
				 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
				 if(countAll6 !=0){
					 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
				 }
				 else{
					 cVm6.difference = 0.0;
				 }
				 platformvm.add(cVm6);
			 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

		 	
		 	return ok(Json.toJson(platformvm));


	    }
	    
	    public static Result getExitData(Long id,String startdate,String enddate){
	    	

		    Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
				 d1 = format.parse(startdate);
		         d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			ClickyContentExit event=ClickyContentExit.findById(id);
			String title=event.url;
			List<ClickyContentExit> browserObjList = ClickyContentExit.findByTitleAndDate(title, d1, d2);
			List<ClickyContentExit> allbrowserlist = ClickyContentExit.getAll(d1, d2);
			List <ClickyPagesVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyPagesVM vm = new ClickyPagesVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			for(ClickyContentExit lis:browserObjList){
		     	if(lis.averageAction != null && !lis.averageAction.equals("")){
		     		count1=count1+Double.parseDouble(lis.averageAction);
		     	}
				if(lis.bounceRate != null && !lis.bounceRate.equals("")){
					count6=count6+Double.parseDouble(lis.bounceRate);	
					     	}
				if(lis.averageTime != null && !lis.averageTime.equals("")){
					count5=count5+Double.parseDouble(lis.averageTime);	
					}
				if(lis.totalTime != null && !lis.totalTime.equals("")){
					count4=count4+Double.parseDouble(lis.totalTime);
					}
				if(lis.visitors != null && !lis.visitors.equals("")){
					 count2=count2+Double.parseDouble(lis.visitors);
					}
				if(lis.uniqueVisitor!= null && !lis.uniqueVisitor.equals("")){
					count3=count3+Double.parseDouble(lis.uniqueVisitor);
					}
				if(lis.action != null && !lis.action.equals("")){
				count7=count7+Double.parseDouble(lis.action);
				}
			   			
			 }
			 
			 double countAll1=0.0;
				double countAll2=0.0;
				double countAll3=0.0;
				double countAll4=0.0;
				double countAll5=0.0;
				double countAll6=0.0;
				double countAll7=0.0;
				 for(ClickyContentExit list:allbrowserlist){
						
						
						if(list.averageAction != null && !list.averageAction.equals("")){
							 countAll1=countAll1+Double.parseDouble(list.averageAction);
				     	}
						if(list.bounceRate != null && !list.bounceRate.equals("")){
							 countAll6=countAll6+Double.parseDouble(list.bounceRate);	
							     	}
						if(list.averageTime != null && !list.averageTime.equals("")){
							countAll5=countAll5+Double.parseDouble(list.averageTime);
							}
						if(list.totalTime != null && !list.totalTime.equals("")){
							 countAll4=countAll4+Double.parseDouble(list.totalTime);
							}
						if(list.visitors != null && !list.visitors.equals("")){
							countAll2=countAll2+Double.parseDouble(list.visitors);
							}
						if(list.uniqueVisitor!= null && !list.uniqueVisitor.equals("")){
							countAll3=countAll3+Double.parseDouble(list.uniqueVisitor);
							}
						if(list.action != null && !list.action.equals("")){
							 countAll7=countAll7+Double.parseDouble(list.action);
						}
					 
			   			
				 }
				 ClickyPlatformVM cVm = new ClickyPlatformVM();
				 cVm.title = "visitors";
				 cVm.these_visitors =  count2;
				 cVm.all_visitors = countAll2;
				 cVm.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
				 platformvm.add(cVm);
				 
				 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
				 cVm1.title = "uniqueV";
				 cVm1.these_visitors = count3;
				 cVm1.all_visitors = countAll3;
				 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
				 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
				 platformvm.add(cVm1);
				 
				 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
				 cVm2.title = "action";
				 cVm2.these_visitors = count7;
				 cVm2.all_visitors = countAll7;
				 cVm2.images = "//con.tent.network/media/icon_click.gif";
				 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
				 platformvm.add(cVm2);
				 
				 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
				 cVm3.title = "averageAct";
				 cVm3.these_visitors = count1;
				 cVm3.all_visitors = countAll1;
				 cVm3.images = "//con.tent.network/media/icon_click.gif";
				 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
				 platformvm.add(cVm3);
				 
				 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
				 cVm4.title = "totalT";
				 cVm4.these_visitors = count4;
				 cVm4.all_visitors = countAll4;
				 cVm4.images = "//con.tent.network/media/icon_time.gif";
				 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
				 platformvm.add(cVm4);
				 
				 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
				 cVm5.title = "averageT";
				 cVm5.these_visitors = count5;
				 cVm5.all_visitors = countAll5;
				 cVm5.images = "//con.tent.network/media/icon_time.gif";
				 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
				 platformvm.add(cVm5);
				 
				 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
				 cVm6.title = "bounceR";
				 cVm6.these_visitors = count6;
				 cVm6.all_visitors = countAll6;
				 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
				 if(countAll6 !=0){
					 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
				 }
				 else{
					 cVm6.difference = 0.0;
				 }
				 platformvm.add(cVm6);
			 
			 vm.averageAct=count1;
			 vm.visitor=count2;
			 vm.uniqueV=count3;
			 vm.totalT=count4;
			 vm.averageT=count5;
			 vm.bounceR=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

		 	
		 	return ok(Json.toJson(platformvm));


	    }
	    
	    public static Result getPagesListDale(String startD,String endD){
	    	
	    	String params = null;
	    	List<ClickyPagesVM> cList = new ArrayList<>();
	    	
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startD);
	             d2 = format.parse(endD);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             
	            List <ClickyPagesList> list=ClickyPagesList.getAll(d1, d2) ;
	     	   	//JsonNode jsonList = Json.parse(callClickAPI(params));
	     	   	List<ClickyPagesActionList>list1=ClickyPagesActionList.getAll(d1,d2);
	     	   	for(ClickyPagesList lis:list){
	     	            	ClickyPagesVM vm = new ClickyPagesVM();
	               	   		for(ClickyPagesActionList lis1:list1){
	               	   			if(lis.id.equals(lis1.clickyPagesId)){
	               	   				
	               	   			String data = lis.url;
	               				String arr[] = data.split("#_");	
	               	     	   	vm.id = lis1.id;
	               				vm.value=lis.value;
	               				vm.value_percent = lis.value_percent;
	               				vm.title = lis.title;
	               				vm.stats_url =lis.stats_url;
	               				vm.url = lis.url;
	               				vm.showUrl = arr[0];
	               	   			vm.averageActions=lis1.averageAction;
	               	   			vm.averageTime=lis1.averageTime;
	               	   			vm.totalTime=lis1.totalTime;
	               	   			vm.bounceRate=lis1.bounceRate;
	               	   		List <ClickyPagesList> list2=ClickyPagesList.getAll(beforeStart, d1) ;
	               	   		//JsonNode jsonActionsList = Json.parse(callClickAPI("&type=pages&heatmap_url=1&date="+newDate+","+startD+""));
	               	   	    double count=0;
	               	   	    double count1=0;
	               	   	for(ClickyPagesList lis2:list) {
	             	    	String data1 = lis2.url;
	             	   		String arr1[] = data1.split("#_");
	             	   		String url=arr1[0];
	             	   		if(url.equals(vm.showUrl)){
	             	   			vm.value_percent2 = lis2.value;
	             	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	             	   		
	             	   		}
	             	   		
	               	   	}	
	               	   	    
	                 	   	for(ClickyPagesList lis2:list2) {
	                 	    	String data1 = lis2.url;
	                 	   		String arr1[] = data1.split("#_");
	                 	   		String url=arr1[0];
	                 	   		if(url.equals(vm.showUrl)){
	                 	   			vm.value_percent2 = lis2.value;
	                 	   		  count=count+Double.parseDouble(vm.value_percent2);
	                 	   		
	                 	   		}
	               	   			
	                 	   	}
	                 	   vm.averagePercent=((count1-count)/count1)*100;
	               	   				
	               	   		    clickyList.add(vm);
	               	   			
	               	   			}
	               	   			
	               	   		}
	               	   		
	     	   	}
	     } catch (Exception e) {
	             e.printStackTrace();
	         }
	        return ok(Json.toJson(clickyList));
		    
	    }
	    
	    public static Result getEntranceList(String startDate,String endDate){
	    	String params = null;
	    	//params = "&type=pages-entrance&date="+startDate+","+endDate+"&limit=all";
		    //return ok(Json.parse(callClickAPI(params)));
	        List<ClickyPagesVM> cList = new ArrayList<>();
	    	
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             
	             List <ClickyEntranceList> list=ClickyEntranceList.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	          	 String uniqueurl = null;
	      	   	//JsonNode jsonList = Json.parse(callClickAPI(params));
	      	   	//List<ClickyPagesActionList>list1=ClickyPagesActionList.getAll(d1,d2);
	      	   	            for(ClickyEntranceList lis:list){
	      	            	ClickyPagesVM vm = new ClickyPagesVM();
	      	            	String langValue = mapOffline.get(lis.url); 
	    	            	if (langValue == null) {
	    	            		uniqueurl = lis.url;
	    	            		mapOffline.put(lis.url, lis.url);
	      	            	
	                	   			String data = lis.url;
	                				String arr[] = data.split("#_");	
	                	     	   	vm.id=lis.id;
	                				vm.value=lis.value;
	                				vm.valuePercent = lis.valuePercent;
	                				vm.title = lis.title;
	                				vm.statsUrl =lis.statsUrl;
	                				vm.url = uniqueurl;
	                				vm.showUrl = arr[0];
	                				vm.mainUrl=lis.mainUrl;
	                	   			vm.averageActions=lis.averageAction;
	                	   			vm.averageTime=lis.averageTime;
	                	   			vm.totalTime=lis.totalTime;
	                	   			vm.bounceRate=lis.bounceRate;
	                	   		List <ClickyEntranceList> list2=ClickyEntranceList.getAll(beforeStart, d1) ;
	                	   		//JsonNode jsonActionsList = Json.parse(callClickAPI("&type=pages&heatmap_url=1&date="+newDate+","+startD+""));
	                	   	    double count=0;
	                	   	    double count1=0;
	                	   	for(ClickyEntranceList lis2:list) {
	              	    	String data1 = lis2.url;
	              	   		String arr1[] = data1.split("#_");
	              	   		String url=arr1[0];
	              	   		if(url.equals(vm.showUrl)){
	              	   			vm.value_percent2 = lis2.value;
	              	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	              	   		
	              	   		}
	              	   		
	                	   	}	
	                	   	    
	                  	   	for(ClickyEntranceList lis2:list2) {
	                  	    	String data1 = lis2.url;
	                  	   		String arr1[] = data1.split("#_");
	                  	   		String url=arr1[0];
	                  	   		if(url.equals(vm.showUrl)){
	                  	   			vm.value_percent2 = lis2.value;
	                  	   		  count=count+Double.parseDouble(vm.value_percent2);
	                  	   		
	                  	   		}
	                	   			
	                  	   	}
	                  	   vm.averagePercent=((count1-count)/count1)*100;
	                	   				
	                	   		    clickyList.add(vm);
	      	   	            }
	      	   	            }
	      	   	            
	      	   	       for(ClickyPagesVM vm:clickyList) {
	        	   	    	  double value = 0;
	        	   	    	 for(ClickyEntranceList lis:list){
	        	   	    		 if(vm.url.equals(lis.url)){
	        	   	    			value = value+Double.parseDouble(lis.value);
	        	   	    			vm.value = String.valueOf(value);
	        	   	    			
	        	   	    			vm.averageActions = lis.averageAction; 
	        	   	    			
	        	   	    			vm.totalTime = lis.totalTime; 
	        	   	    			
	        	   	    			vm.averageTime = lis.averageTime; 
	        	   	    			
	        	   	    			vm.bounceRate = lis.bounceRate; 
	        	   	    		
	        	   	    		 }
	        	   	    	  }
	        	   	    
	 	       	   }
	             
	             
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
		    
	    }
	    
	    public static Result getExit(String startDate,String endDate){
	    	String params = null;
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");

	             List <ClickyContentExit> list=ClickyContentExit.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	           	 String uniqueurl = null;
	            	
	      	   	            for(ClickyContentExit lis:list){
	      	            	ClickyPagesVM vm = new ClickyPagesVM();
	      	            	
	        	            	String langValue = mapOffline.get(lis.editedUrl); 
	      	            	if (langValue == null) {
	      	            		uniqueurl = lis.editedUrl;
	      	            		mapOffline.put(lis.editedUrl, lis.editedUrl);
	          	   	      
	                	     	   	vm.id=lis.id;
	                				vm.value=lis.value;
	                				vm.valuePercent = lis.valuePercent;
	                				vm.title = lis.title;
	                				vm.statsUrl =lis.statsUrl;
	                				vm.url = lis.url;
	                				vm.editedUrl= uniqueurl;
	                	   		List <ClickyContentExit> list2=ClickyContentExit.getAll(beforeStart, d1) ;
	                	   		//JsonNode jsonActionsList = Json.parse(callClickAPI("&type=pages&heatmap_url=1&date="+newDate+","+startD+""));
	                	   	    double count=0;
	                	   	    double count1=0;
	                	   	for(ClickyContentExit lis2:list) {
	              	    	String url = lis2.url;
	              	   		if(url.equals(vm.url)){
	              	   			vm.value_percent2 = lis2.value;
	              	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	              	   		
	              	   		}
	              	   		
	                	   	}	
	                	   	    
	                  	   	for(ClickyContentExit lis2:list2) {
	                  	    	String url = lis2.url;
	                  	   		if(url.equals(vm.url)){
	                  	   			vm.value_percent2 = lis2.value;
	                  	   		  count=count+Double.parseDouble(vm.value_percent2);
	                  	   		
	                  	   		}
	                	   			
	                  	   	}
	                  	   vm.averagePercent=((count1-count)/count1)*100;
	                	   				
	                	   		    clickyList.add(vm);
	      	            	}
	      	   	            }
	      	   	      
	    	   	       for(ClickyPagesVM vm:clickyList) {
	      	   	    	  double value = 0;
	      	   	    	 for(ClickyContentExit lis:list){
	      	   	    		 if(vm.editedUrl.equals(lis.editedUrl)){
	      	   	    			value = value+Double.parseDouble(lis.value);
	      	   	    			vm.value = String.valueOf(value);
	      	   	    			
	      	   	    			vm.averageActions = lis.averageAction; 
	      	   	    			
	      	   	    			vm.totalTime = lis.totalTime; 
	      	   	    			
	      	   	    			vm.averageTime = lis.averageTime; 
	      	   	    			
	      	   	    			vm.bounceRate = lis.bounceRate; 
	      	   	    		
	      	   	    		 }
	      	   	    	  }
	      	   	    
		       	   }

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
	}
	    
	    public static Result getDownloads(String startDate,String endDate){
	    	String params = null;
	    	
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             
	       	   List <ClickyContentDownLoad> list=ClickyContentDownLoad.getAll(d1, d2) ;
	           for(ClickyContentDownLoad lis:list){
	    	ClickyPagesVM vm = new ClickyPagesVM();
		     	   	vm.id=lis.id;
					vm.value=lis.value;
					vm.valuePercent = lis.valuePercent;
					if(lis.title != null){
					vm.title = lis.title;
					}
				//	vm.statsUrl =lis.statsUrl;
				//	vm.url = lis.url;
		   		List <ClickyContentDownLoad> list2=ClickyContentDownLoad.getAll(beforeStart, d1) ;
		   		//JsonNode jsonActionsList = Json.parse(callClickAPI("&type=pages&heatmap_url=1&date="+newDate+","+startD+""));
		   	    double count=0;
		   	    double count1=0;
		   	for(ClickyContentDownLoad lis2:list) {
	    	String url = lis2.url;
	   		if(url.equals(vm.url)){
	   			vm.value_percent2 = lis2.value;
	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	   		
	   		}
	   		
		   	}	
		   	    
		   	for(ClickyContentDownLoad lis2:list2) {
		    	String url = lis2.url;
		   		if(url.equals(vm.url)){
		   			vm.value_percent2 = lis2.value;
		   		  count=count+Double.parseDouble(vm.value_percent2);
		   		
		   		}
		   			
		   	}
		   vm.averagePercent=((count1-count)/count1)*100;
		   				
		   		    clickyList.add(vm);
		   			
		
	           	}
	    	 } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
	}
	    
	    public static Result getEvent(String startDate,String endDate){
	    	String params = null;
	    //	params = "&type=events&date="+startDate+","+endDate+"&limit=all";
		    //return ok(Json.parse(callClickAPI(params)));
	    	
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	     	   List <ClickyContentEvent> list=ClickyContentEvent.getAll(d1, d2) ;
	     	  Map<String, String> mapOffline = new HashMap<String, String>();
	        	 String uniqueurl = null;
	         	
		            for(ClickyContentEvent lis:list){
	         	ClickyPagesVM vm = new ClickyPagesVM();
	         	
	            	String langValue = mapOffline.get(lis.url); 
	          	if (langValue == null) {
	          		uniqueurl = lis.url;
	          		mapOffline.put(lis.url, lis.url);
		   	       
	   	     	   	vm.id=lis.id;
	   				vm.value=lis.value;
	   				vm.valuePercent = lis.valuePercent;
	   				vm.title = lis.title;
	   				vm.statsUrl =lis.statsUrl;
	   				vm.url = uniqueurl;
	   	   		List <ClickyContentEvent> list2=ClickyContentEvent.getAll(beforeStart, d1) ;
	   	   		//JsonNode jsonActionsList = Json.parse(callClickAPI("&type=pages&heatmap_url=1&date="+newDate+","+startD+""));
	   	   	    double count=0;
	   	   	    double count1=0;
	   	   	for(ClickyContentEvent lis2:list) {
	 	    	String url = lis2.url;
	 	   		if(url.equals(vm.url)){
	 	   			vm.value_percent2 = lis2.value;
	 	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	 	   		
	 	   		}
	 	   		
	   	   	}	
	   	   	    
	     	   	for(ClickyContentEvent lis2:list2) {
	     	    	String url = lis2.url;
	     	   		if(url.equals(vm.url)){
	     	   			vm.value_percent2 = lis2.value;
	     	   		  count=count+Double.parseDouble(vm.value_percent2);
	     	   		
	     	   		}
	   	   			
	     	   	}
	     	   vm.averagePercent=((count1-count)/count1)*100;
	   	   				
	   	   		    clickyList.add(vm);
	          	}

		            }
		   	       for(ClickyPagesVM vm:clickyList) {
		   	    	  double value = 0;
		   	    	 for(ClickyContentEvent lis:list){
		   	    		 if(vm.url.equals(lis.url)){
		   	    			value = value+Double.parseDouble(lis.value);
		   	    			vm.value = String.valueOf(value);
		   	    			
		   	    			vm.averageActions = lis.averageAction; 
		   	    			
		   	    			vm.totalTime = lis.totalTime; 
		   	    			
		   	    			vm.averageTime = lis.averageTime; 
		   	    			
		   	    			vm.bounceRate = lis.bounceRate; 
		   	    		
		   	    		 }
		   	    	  }
		   	    
	    	   }
	          	
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
	    }
	    
	    public static Result getMediaDetails(String startDate,String endDate){
	    	String params = null;
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   List <ClickyContentMedia> list=ClickyContentMedia.getAll(d1, d2) ;
	     	   Map<String, String> mapOffline = new HashMap<String, String>();
	        	 String uniqueurl = null;
	         	
		            for(ClickyContentMedia lis:list){
	         	ClickyPagesVM vm = new ClickyPagesVM();
	         	
	            	String langValue = mapOffline.get(lis.url); 
	          	if (langValue == null) {
	          		uniqueurl = lis.url;
	          		mapOffline.put(lis.url, lis.url);
		   	       
	   	     	   	vm.id=lis.id;
	   				vm.value=lis.value;
	   				vm.valuePercent = lis.valuePercent;
	   				if(lis.title != null){
	   				vm.title = lis.title;
	   				}
	   				vm.statsUrl =lis.statsUrl;
	   				vm.url = uniqueurl;
	   	   		List <ClickyContentMedia> list2=ClickyContentMedia.getAll(beforeStart, d1) ;
	   	   		//JsonNode jsonActionsList = Json.parse(callClickAPI("&type=pages&heatmap_url=1&date="+newDate+","+startD+""));
	   	   	    double count=0;
	   	   	    double count1=0;
	   	   	for(ClickyContentMedia lis2:list) {
	   	   		if(lis2.url != null){
	 	    	String url = lis2.url;
	 	   		if(url.equals(vm.url)){
	 	   			vm.value_percent2 = lis2.value;
	 	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	 	   		
	 	   		}
	 	   		
	   	   	}	
	   	   	}
	     	   	for(ClickyContentMedia lis2:list2) {
	     	   		if(lis2.url != null){
	     	    	String url = lis2.url;
	     	   		if(url.equals(vm.url)){
	     	   			vm.value_percent2 = lis2.value;
	     	   		  count=count+Double.parseDouble(vm.value_percent2);
	     	   		
	     	   		}
	   	   			
	     	   	}
	     	   	}
	     	   vm.averagePercent=((count1-count)/count1)*100;
	   	   				
	   	   		    clickyList.add(vm);
	          	}	
			
		}
		            
		            for(ClickyPagesVM vm:clickyList) {
	   	   	    	  double value = 0;
	   	   	    	 for(ClickyContentMedia lis:list){
	   	   	    		 if(vm.url.equals(lis.url)){
	   	   	    			value = value+Double.parseDouble(lis.value);
	   	   	    			vm.value = String.valueOf(value);
	   	   	    			
	   	   	    			vm.averageActions = lis.averageAction; 
	   	   	    			
	   	   	    			vm.totalTime = lis.totalTime; 
	   	   	    			
	   	   	    			vm.averageTime = lis.averageTime; 
	   	   	    			
	   	   	    			vm.bounceRate = lis.bounceRate; 
	   	   	    		
	   	   	    		 }
	   	   	    	  }
	   	   	    
	       	   }
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
	    }
	    
	    public static Result getDomains(String startDate,String endDate){
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   List <ClickyContentDomain> list=ClickyContentDomain.getAll(d1, d2) ;
	     	  Map<String, String> mapOffline = new HashMap<String, String>();
	        	 String uniquetitle = null;
	         	
		            for(ClickyContentDomain lis:list){
	         	ClickyPagesVM vm = new ClickyPagesVM();
	         	
	            	String langValue = mapOffline.get(lis.title); 
	          	if (langValue == null) {
	          		uniquetitle = lis.title;
	          		mapOffline.put(lis.title, lis.title);
		   	       
	    	   
	   	     	   	vm.id=lis.id;
	   				vm.value=lis.value;
	   				vm.valuePercent = lis.valuePercent;
	   				if(lis.title != null){
	   				vm.title = uniquetitle;
	   				}
	   			//	vm.statsUrl =lis.statsUrl;
	   			//	vm.url = lis.url;
	   	   		List <ClickyContentDomain> list2=ClickyContentDomain.getAll(beforeStart, d1) ;
	   	   		//JsonNode jsonActionsList = Json.parse(callClickAPI("&type=pages&heatmap_url=1&date="+newDate+","+startD+""));
	   	   	    double count=0;
	   	   	    double count1=0;
	   	   	for(ClickyContentDomain lis2:list) {
	 	    	String url = lis2.title;
	 	   		if(url.equals(vm.title)){
	 	   			vm.value_percent2 = lis2.value;
	 	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	 	   		
	 	   		}
	 	   		
	   	   	}	
	   	   	    
	     	   	for(ClickyContentDomain lis2:list2) {
	     	    	String url = lis2.title;
	     	   		if(url.equals(vm.title)){
	     	   			vm.value_percent2 = lis2.value;
	     	   		  count=count+Double.parseDouble(vm.value_percent2);
	     	   		
	     	   		}
	   	   			
	     	   	}
	     	   vm.averagePercent=((count1-count)/count1)*100;
	   	   				
	   	   		    clickyList.add(vm);
	   	   			
	          	}
		}
		           
	 	   	       for(ClickyPagesVM vm:clickyList) {
	   	   	    	  double value = 0;
	   	   	    	 for(ClickyContentDomain lis:list){
	   	   	    		 if(vm.title.equals(lis.title)){
	   	   	    			value = value+Double.parseDouble(lis.value);
	   	   	    			vm.value = String.valueOf(value);
	   	   	    		}
	   	   	    	  }
	   	   	    
	       	   }
	             

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));

	    
	    }
	    
	    public static Result getMediaData(Long url , String startdate, String enddate){
			Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			ClickyContentMedia oneRow = ClickyContentMedia.findById(url);
			
			List<ClickyContentMedia> mediaObjList = ClickyContentMedia.findByUrlAndDate(oneRow.url, d1, d2);
			List<ClickyContentMedia> allmedialist = ClickyContentMedia.getAll(d1, d2);
			List <ClickyContentVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyContentVM vm = new ClickyContentVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickyContentMedia lis:mediaObjList){
	         	
				 count1=count1+Double.parseDouble(lis.averageAction);
				 count2=count2+Double.parseDouble(lis.visitors);
				 count3=count3+Double.parseDouble(lis.uniqueVisitor);
				 count4=count4+Double.parseDouble(lis.totalTime);
				 count5=count5+Double.parseDouble(lis.averageTime);
				 count6=count6+Double.parseDouble(lis.bounceRate);
				 count7=count7+Double.parseDouble(lis.action);
	   	   			
			 }
			 
			 double countAll1=0.0;
	 		double countAll2=0.0;
	 		double countAll3=0.0;
	 		double countAll4=0.0;
	 		double countAll5=0.0;
	 		double countAll6=0.0;
	 		double countAll7=0.0;
	 		 for(ClickyContentMedia list:allmedialist){
	          	
	 			 countAll1=countAll1+Double.parseDouble(list.averageAction);
	 			 countAll2=countAll2+Double.parseDouble(list.visitors);
	 			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor);
	 			 countAll4=countAll4+Double.parseDouble(list.totalTime);
	 			 countAll5=countAll5+Double.parseDouble(list.averageTime);
	 			 countAll6=countAll6+Double.parseDouble(list.bounceRate);
	 			 countAll7=countAll7+Double.parseDouble(list.action);
	    	   			
	 		 }
			 
	 		 ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count1;
			 cVm3.all_visitors = countAll1;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count5;
			 cVm5.all_visitors = countAll5;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);
			 
			 vm.averageAction=count1;
			 vm.visitor=count2;
			 vm.uniqueVisitor=count3;
			 vm.totalTimes=count4;
			 vm.averageTimes=count5;
			 vm.bounceRates=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

	     	
	     	
			 
	     	return ok(Json.toJson(platformvm));
			
		}
	    
	    public static Result getPagesChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String url=vm1.url;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=url;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 //JsonNode jsonList = Json.parse(callClickAPI("&type=pages&date="+startD+"&limit=all"));
	                 List<ClickyPagesList> list=ClickyPagesList.getAllData(d);
	                 Long l =0L;
	                // for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
	                 for(ClickyPagesList lis:list){
	              	   	if(url.equals(lis.mainUrl)){
	              	   	String value = lis.value;
	              	   	
	              	   	l=l+(long)Integer.parseInt(value);
	              	  
	              	   	}
	              	   	}
	                 lonnn.add(l);
	             	String chartDate=format1.format(d);
	         	     dates.add(chartDate); 
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getEntranceChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String url=vm1.url;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=url;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 List<ClickyEntranceList> list=ClickyEntranceList.getAllData(d);
	               //  JsonNode jsonList = Json.parse(callClickAPI("&type=pages-entrance&date="+startD+"&limit=all"));
	                 
	                 Long l=0L;
	                 for(ClickyEntranceList lis:list) {
	              	   	if(url.equals(lis.mainUrl)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value =lis.value;
	              	   	l=l+(long)Integer.parseInt(value);
	              	  
	              	   	}
	              	   	}
	                 lonnn.add(l);
	                 String chartDate=format1.format(d);
	                 dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getExitChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String url=vm1.url;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=url;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                // JsonNode jsonList = Json.parse(callClickAPI("&type=pages-exit&date="+startD+"&limit=all"));
	             	Long l=0L;
	                 List<ClickyContentExit> list=ClickyContentExit.getAllData(d);
	                 for(ClickyContentExit lis:list) {
	              	   	if(url.equals(lis.url)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value =lis.value;
	              	   l=l+(long)Integer.parseInt(value);
	              	 
	              	   	}
	              	   	}
	             	String chartDate=format1.format(d);
	         	     dates.add(chartDate);
	                 lonnn.add(l);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getDownloadsChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String url=vm1.url;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=url;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 Long l=0L;
	                 //JsonNode jsonList = Json.parse(callClickAPI("&type=downloads&date="+startD+"&limit=all"));
	                 List<ClickyContentDownLoad> list=ClickyContentDownLoad.getAllData(d);
	                 
	                 for(ClickyContentDownLoad lis:list) {
	              	   	if(url.equals(lis.url)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value =lis.value;
	              	  l=l+(long)Integer.parseInt(value);
	              	   	}
	              	   	}
	             	  lonnn.add(l);
	             	   	String chartDate=format1.format(d);
	             	     dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getEventChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String url=vm1.url;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=url;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 Long l=0L;
	                 //JsonNode jsonList = Json.parse(callClickAPI("&type=events&date="+startD+"&limit=all"));
	                 List<ClickyContentEvent> list=ClickyContentEvent.getAllData(d);
	                 for(ClickyContentEvent lis:list) {
	              	   	if(url.equals(lis.url)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value =lis.url;
	              	   	l=l+(long)Integer.parseInt(value);
	              	  
	              	   
	              	   	//clickyList.add(vm);
	              	   	}
	              	   	}
	                    lonnn.add(l);
	            	   	String chartDate=format1.format(d);
	            	     dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getMediaChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String title=vm1.title;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=title;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 List<ClickyContentMedia> list=ClickyContentMedia.getAllData(d);
	                 Long l=0L;
	                 for(ClickyContentMedia lis:list) {
	              	   	if(title.equals(lis.url)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value = lis.value;
	              	   l=l+(long)Integer.parseInt(value);
	              	   	}
	              	   	}
	                    lonnn.add(l);
	            	   	String chartDate=format1.format(d);
	            	     dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getshowDomainsChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String title=vm1.title;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=title;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 //JsonNode jsonList = Json.parse(callClickAPI("&type=site-domains&date="+startD+"&limit=all"));
	                 List<ClickyContentDomain> list=ClickyContentDomain.getAllData(d);
	                 Long l=0L;
	                 for(ClickyContentDomain lis:list) {
	              	   	if(title.equals(lis.title)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value = lis.value;
	              	   	l=l+(long)Integer.parseInt(value);
	              	   	}
	              	   	}
	                 lonnn.add(l);
	            	   	String chartDate=format1.format(d);
	            	     dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getVideoAction(String startDate,String endDate){
	    	String params = null;
	    	params = "&type=video&video_meta=1&date="+startDate+","+endDate+"&limit=all";
		    return ok(Json.parse(callClickAPI(params)));
	    }
	    
	    public static Result getbrowser(String startDate,String endDate){
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             //in milliseconds
	             long diff = d2.getTime() - d1.getTime();

	            // long diffSeconds = diff / 1000 % 60;
	             long diffMinutes = diff / (60 * 1000) % 60;
	             long diffHours = diff / (60 * 60 * 1000) % 24;
	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             
	             List <ClickyPlatformBrowser> list=ClickyPlatformBrowser.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	             	String uniquetitle = null;
	             	
	       	   	            for(ClickyPlatformBrowser lis:list){
	       	            	ClickyPagesVM vm = new ClickyPagesVM();
	       	            	
	       	            	String langValue = mapOffline.get(lis.title); 
	    	 				if (langValue == null) {
	    	 					uniquetitle = lis.title;
	    	 					mapOffline.put(lis.title, lis.title);
	    	 				
	                 	     	   	vm.id=lis.id;
	                 				vm.value=lis.value;
	                 				//vm.valuePercent = lis.valuePercent;
	                 				vm.title = uniquetitle;
	                 				vm.statsUrl =lis.statsUrl;
	                 	   			vm.averageActions=lis.averageAction;
	                 	   			vm.averageTime=lis.averageTime;
	                 	   			vm.totalTime=lis.totalTime;
	                 	   			vm.bounceRate=lis.bounceRate;
	                 	   			
	                 	   		
	       	   	    			
	             	   		List <ClickyPlatformBrowser> list2=ClickyPlatformBrowser.getAll(beforeStart, d1) ;
	             	   		//JsonNode jsonActionsList = Json.parse(callClickAPI("&type=pages&heatmap_url=1&date="+newDate+","+startD+""));
	             	   	    double count=0;
	             	   	    double count1=0;
	                 	   	for(ClickyPlatformBrowser lis2:list) {
	               	    	String url = lis2.title;
		               	   		if(url.equals(vm.title)){
		               	   			vm.value_percent2 = lis2.value;
		               	   		  count1=count1+Double.parseDouble(vm.value_percent2);
		               	   		
		               	   		}
	               	   		}	
	                 	   	for(ClickyPlatformBrowser lis2:list2) {
	                   	    	String url = lis2.title;
	                   	   		if(url.equals(vm.title)){
	                   	   			vm.value_percent2 = lis2.value;
	                   	   		  count=count+Double.parseDouble(vm.value_percent2);
	                   	   		}
	                 	   	}
	                   	   vm.averagePercent=((count1-count)/count1)*100;
	                   	   
	                 	   		    clickyList.add(vm);
	                 	   	}	
	       	   	      }
	       	   	       
	       	   	           
	       	   	      for(ClickyPagesVM vm:clickyList) {
	       	   	    	  double value = 0;
	       	   	    	 double valueper = 0;
	       	   	    	  
	       	   	    	  for(ClickyPlatformBrowser lis:list){
	       	   	    		 if(vm.title.equals(lis.title)){
	       	   	    			value = value+Double.parseDouble(lis.value);
	       	   	    			vm.value = String.valueOf(value);
	       	   	    			
	       	   	    			vm.averageActions = lis.averageAction; 
	       	   	    			
	       	   	    			vm.totalTime = lis.totalTime; 
	       	   	    			
	       	   	    			vm.averageTime = lis.averageTime; 
	       	   	    			
	       	   	    			vm.bounceRate = lis.bounceRate; 
	       	   	    		
	       	   	    		 }
	       	   	    	  }
	       	   	    
		       	   }
	       	   	          
	    	 } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
		    
	    }
	    
	    public static Result getOperatingSystem(String startDate,String endDate){
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             //in milliseconds
	             long diff = d2.getTime() - d1.getTime();

	            // long diffSeconds = diff / 1000 % 60;
	             long diffMinutes = diff / (60 * 1000) % 60;
	             long diffHours = diff / (60 * 60 * 1000) % 24;
	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             List <ClickyPlatformOperatingSystem> list=ClickyPlatformOperatingSystem.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	          	 String uniquetitleos = null;
		            for(ClickyPlatformOperatingSystem lis:list){
		            	ClickyPagesVM vm = new ClickyPagesVM();
		            	String langValue = mapOffline.get(lis.title); 
		            	if (langValue == null) {
		            		uniquetitleos = lis.title;
		            		mapOffline.put(lis.title, lis.title);
	   	     	   	vm.id=lis.id;
	   				vm.value=lis.value;
	   				vm.valuePercent = lis.valuePercent;
	   				vm.title = uniquetitleos;
	   				vm.statsUrl =lis.statsUrl;
	   	   			vm.averageActions=lis.averageAction;
	   	   			vm.averageTime=lis.averageTime;
	   	   			vm.totalTime=lis.totalTime;
	   	   			vm.bounceRate=lis.bounceRate;
	   	   		List <ClickyPlatformOperatingSystem> list2=ClickyPlatformOperatingSystem.getAll(beforeStart, d1) ;
	   	   	    double count=0;
	   	   	    double count1=0;
	   	   	for(ClickyPlatformOperatingSystem lis2:list) {
	 	    	String url = lis2.title;
	 	   		if(url.equals(vm.title)){
	 	   			vm.value_percent2 = lis2.value;
	 	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	 	   		
	 	   		}
	 	   		
	   	   	}	
	   	   	    
	     	   	for(ClickyPlatformOperatingSystem lis2:list2) {
	     	    	String url = lis2.title;
	     	   		if(url.equals(vm.title)){
	     	   			vm.value_percent2 = lis2.value;
	     	   		  count=count+Double.parseDouble(vm.value_percent2);
	     	   		
	     	   		}
	   	   			
	     	   	}
	     	   vm.averagePercent=((count1-count)/count1)*100;
	   	   				
	   	   		    clickyList.add(vm);
				}
		            }         
		            for(ClickyPagesVM vm:clickyList) {
	     	   	    	  double value = 0;
	     	   	    	 
	     	   	    	  System.out.println(vm.title);
	     	   	    	  for(ClickyPlatformOperatingSystem lis:list){
	     	   	    		 if(vm.title.equals(lis.title)){
	     	   	    			value = value+Double.parseDouble(lis.value);
	     	   	    			vm.value = String.valueOf(value);
	     	   	    			
	     	   	    			vm.averageActions = lis.averageAction; 
	     	   	    			
	     	   	    			vm.totalTime = lis.totalTime; 
	     	   	    			
	     	   	    			vm.averageTime = lis.averageTime; 
	     	   	    			
	     	   	    			vm.bounceRate = lis.bounceRate; 
	     	   	    		
	     	   	    		 }
	     	   	    	  }

		       	   }

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
	    	//params = "&type=engagement-actions&date="+startDate+","+endDate+"&limit=all";
		    return ok(Json.toJson(clickyList));
		    
	    }
	    
	    public static Result getScreenResolution(String startDate,String endDate){
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             //in milliseconds
	             long diff = d2.getTime() - d1.getTime();

	            // long diffSeconds = diff / 1000 % 60;
	             long diffMinutes = diff / (60 * 1000) % 60;
	             long diffHours = diff / (60 * 60 * 1000) % 24;
	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             List <ClickyPlatformScreen> list=ClickyPlatformScreen.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	          	 String uniquetitlescreen = null;
	   	            for(ClickyPlatformScreen lis:list){
	            	ClickyPagesVM vm = new ClickyPagesVM();
	            	
	            	String langValue = mapOffline.get(lis.title); 
	            	if (langValue == null) {
	            		uniquetitlescreen = lis.title;
	            		mapOffline.put(lis.title, lis.title);
	            		
	      	     	   	vm.id=lis.id;
	      				vm.value=lis.value;
	      				vm.valuePercent = lis.valuePercent;
	      				vm.title = uniquetitlescreen;
	      				vm.statsUrl =lis.statsUrl;
	      	   			vm.averageActions=lis.averageAction;
	      	   			vm.averageTime=lis.averageTime;
	      	   			vm.totalTime=lis.totalTime;
	      	   			vm.bounceRate=lis.bounceRate;
	      	   		List <ClickyPlatformScreen> list2=ClickyPlatformScreen.getAll(beforeStart, d1) ;
	      	   	    double count=0;
	      	   	    double count1=0;
	      	   	for(ClickyPlatformScreen lis2:list) {
	    	    	String url = lis2.title;
	    	   		if(url.equals(vm.title)){
	    	   			vm.value_percent2 = lis2.value;
	    	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	    	   		
	    	   		}
	    	   		
	      	   	}	
	      	   	    
	        	   	for(ClickyPlatformScreen lis2:list2) {
	        	    	String url = lis2.title;
	        	   		if(url.equals(vm.title)){
	        	   			vm.value_percent2 = lis2.value;
	        	   		  count=count+Double.parseDouble(vm.value_percent2);
	        	   		
	        	   		}
	      	   			
	        	   	}
	        	   vm.averagePercent=((count1-count)/count1)*100;
	      	   				
	      	   		    clickyList.add(vm);
	   	            }
	   	            }
	   	         for(ClickyPagesVM vm:clickyList) {
		   	    	  double value = 0;
		   	    	 
		   	    	  System.out.println(vm.title);
		   	    	  for(ClickyPlatformScreen lis:list){
		   	    		 if(vm.title.equals(lis.title)){
		   	    			value = value+Double.parseDouble(lis.value);
		   	    			vm.value = String.valueOf(value);
		   	    			
		   	    			vm.averageActions = lis.averageAction; 
		   	    			
		   	    			vm.totalTime = lis.totalTime; 
		   	    			
		   	    			vm.averageTime = lis.averageTime; 
		   	    			
		   	    			vm.bounceRate = lis.bounceRate; 
		   	    		
		   	    		 }
		   	    	  }

	      	   }


	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
	    	//params = "&type=engagement-actions&date="+startDate+","+endDate+"&limit=all";
		    return ok(Json.toJson(clickyList));
		    
	    }
	    
	    public static Result getHardware(String startDate,String endDate){
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             //in milliseconds
	             long diff = d2.getTime() - d1.getTime();

	            // long diffSeconds = diff / 1000 % 60;
	             long diffMinutes = diff / (60 * 1000) % 60;
	             long diffHours = diff / (60 * 60 * 1000) % 24;
	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             List <ClickyPlatformHardware> list=ClickyPlatformHardware.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	          	 String uniquetitlehardware = null;
	   	            for(ClickyPlatformHardware lis:list){
	            	ClickyPagesVM vm = new ClickyPagesVM();
	            	String langValue = mapOffline.get(lis.title); 
	            	if (langValue == null) {
	            		uniquetitlehardware = lis.title;
	            		mapOffline.put(lis.title, lis.title);
	            	
	      	     	   	vm.id=lis.id;
	      				vm.value=lis.value;
	      				vm.valuePercent = lis.valuePercent;
	      				vm.title = uniquetitlehardware;
	      				vm.statsUrl =lis.statsUrl;
	      	   			vm.averageActions=lis.averageAction;
	      	   			vm.averageTime=lis.averageTime;
	      	   			vm.totalTime=lis.totalTime;
	      	   			vm.bounceRate=lis.bounceRate;
	      	   		List <ClickyPlatformHardware> list2=ClickyPlatformHardware.getAll(beforeStart, d1) ;
	      	   	    double count=0;
	      	   	    double count1=0;
	      	   	for(ClickyPlatformHardware lis2:list) {
	    	    	String url = lis2.title;
	    	   		if(url.equals(vm.title)){
	    	   			vm.value_percent2 = lis2.value;
	    	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	    	   		
	    	   		}
	    	   		
	      	   	}	
	      	   	    
	        	   	for(ClickyPlatformHardware lis2:list2) {
	        	    	String url = lis2.title;
	        	   		if(url.equals(vm.title)){
	        	   			vm.value_percent2 = lis2.value;
	        	   		  count=count+Double.parseDouble(vm.value_percent2);
	        	   		
	        	   		}
	      	   			
	        	   	}
	        	   vm.averagePercent=((count1-count)/count1)*100;
	      	   				
	      	   		    clickyList.add(vm);
	   	            }
	    	 }   
	   	         for(ClickyPagesVM vm:clickyList) {
		   	    	  double value = 0;
		   	    	 
		   	    	  System.out.println(vm.title);
		   	    	  for(ClickyPlatformHardware lis:list){
		   	    		 if(vm.title.equals(lis.title)){
		   	    			value = value+Double.parseDouble(lis.value);
		   	    			vm.value = String.valueOf(value);
		   	    			
		   	    			vm.averageActions = lis.averageAction; 
		   	    			
		   	    			vm.totalTime = lis.totalTime; 
		   	    			
		   	    			vm.averageTime = lis.averageTime; 
		   	    			
		   	    			vm.bounceRate = lis.bounceRate; 
		   	    		
		   	    		 }
		   	    	  }

	     	   }

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
	    	//params = "&type=engagement-actions&date="+startDate+","+endDate+"&limit=all";
		    return ok(Json.toJson(clickyList));
		    
	    }
	    
	    public static Result getBrowserChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String title=vm1.title;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=title;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 List<ClickyPlatformBrowser> list=ClickyPlatformBrowser.getAllData(d);
	               //  JsonNode jsonList = Json.parse(callClickAPI("&type=pages-entrance&date="+startD+"&limit=all"));
	                 
	                 Long l=0L;
	                 for(ClickyPlatformBrowser lis:list) {
	              	   	if(title.equals(lis.title)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value =lis.value;
	              	   	l=l+(long)Integer.parseInt(value);
	              	  
	              	   	}
	              	   	}
	                 lonnn.add(l);
	                 String chartDate=format1.format(d);
	                 dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getshowOperatingSystemChart(){
	    	
	    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
	    		ClickyPagesVM vm1 = form.get();
	    		String startDate=vm1.startDate;
	    		String endDate=vm1.endDate;
	    		String title=vm1.title;
	    		
	    		
	    	String params = null;
	    	System.out.println(startDate);
	    	System.out.println(endDate);
	    	Date d1 = null;
	        Date d2 = null;
	        Map<String, Object> map = new HashMap<>();
	        List<sendDataVM> data = new ArrayList<>();
			List<Object> dates = new ArrayList<>();
			map.put("dates",dates);
			map.put("data",data);
	        List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	     	   	
	             GregorianCalendar gcal = new GregorianCalendar();
	             gcal.setTime(d1);
	             sendDataVM vm=new sendDataVM();
	             vm.name=title;
	             List<Long> lonnn = new ArrayList<>();
	             while (!gcal.getTime().after(d2)) {
	                 Date d = gcal.getTime();
	                 System.out.println(d);
	                 String startD=format.format(d);
	                 List<ClickyPlatformOperatingSystem> list=ClickyPlatformOperatingSystem.getAllData(d);
	               //  JsonNode jsonList = Json.parse(callClickAPI("&type=pages-entrance&date="+startD+"&limit=all"));
	                 
	                 Long l=0L;
	                 for(ClickyPlatformOperatingSystem lis:list) {
	              	   	if(title.equals(lis.title)){
	                	// ClickyPagesVM vm = new ClickyPagesVM();
	              	   	String value =lis.value;
	              	   	l=l+(long)Integer.parseInt(value);
	              	  
	              	   	}
	              	   	}
	                 lonnn.add(l);
	                 String chartDate=format1.format(d);
	                 dates.add(chartDate);
	                gcal.add(Calendar.DAY_OF_MONTH, 1);
	                 
	             }
	             vm.data=lonnn;
	             data.add(vm);
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(map));
	    	
		    
	    }
	    
	    public static Result getScreenResolutionChart(){
	    	
    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
    		ClickyPagesVM vm1 = form.get();
    		String startDate=vm1.startDate;
    		String endDate=vm1.endDate;
    		String title=vm1.title;
    		
    		
    	String params = null;
    	System.out.println(startDate);
    	System.out.println(endDate);
    	Date d1 = null;
        Date d2 = null;
        Map<String, Object> map = new HashMap<>();
        List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		map.put("dates",dates);
		map.put("data",data);
        List<ClickyPagesVM> clickyList = new ArrayList<>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
    	 try {
             d1 = format.parse(startDate);
             d2 = format.parse(endDate);

             long diff = d2.getTime() - d1.getTime();

             long diffDays = diff / (24 * 60 * 60 * 1000);
             Integer days=(int)diffDays;
          Date beforeStart = DateUtils.addDays(d1, -days); 
             String newDate=format.format(beforeStart);
             System.out.print(newDate + " newDate ");
     	   	
             GregorianCalendar gcal = new GregorianCalendar();
             gcal.setTime(d1);
             sendDataVM vm=new sendDataVM();
             vm.name=title;
             List<Long> lonnn = new ArrayList<>();
             while (!gcal.getTime().after(d2)) {
                 Date d = gcal.getTime();
                 System.out.println(d);
                 String startD=format.format(d);
                 List<ClickyPlatformScreen> list=ClickyPlatformScreen.getAllData(d);
               //  JsonNode jsonList = Json.parse(callClickAPI("&type=pages-entrance&date="+startD+"&limit=all"));
                 
                 Long l=0L;
                 for(ClickyPlatformScreen lis:list) {
              	   	if(title.equals(lis.title)){
                	// ClickyPagesVM vm = new ClickyPagesVM();
              	   	String value =lis.value;
              	   	l=l+(long)Integer.parseInt(value);
              	  
              	   	}
              	   	}
                 lonnn.add(l);
                 String chartDate=format1.format(d);
                 dates.add(chartDate);
                gcal.add(Calendar.DAY_OF_MONTH, 1);
                 
             }
             vm.data=lonnn;
             data.add(vm);
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
	    return ok(Json.toJson(map));
     }
	    
	    public static Result getHardwareChart(){
	    	
    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
    		ClickyPagesVM vm1 = form.get();
    		String startDate=vm1.startDate;
    		String endDate=vm1.endDate;
    		String title=vm1.title;
    		
    		
    	String params = null;
    	System.out.println(startDate);
    	System.out.println(endDate);
    	Date d1 = null;
        Date d2 = null;
        Map<String, Object> map = new HashMap<>();
        List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		map.put("dates",dates);
		map.put("data",data);
        List<ClickyPagesVM> clickyList = new ArrayList<>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
    	 try {
             d1 = format.parse(startDate);
             d2 = format.parse(endDate);

             long diff = d2.getTime() - d1.getTime();

             long diffDays = diff / (24 * 60 * 60 * 1000);
             Integer days=(int)diffDays;
          Date beforeStart = DateUtils.addDays(d1, -days); 
             String newDate=format.format(beforeStart);
             System.out.print(newDate + " newDate ");
     	   	
             GregorianCalendar gcal = new GregorianCalendar();
             gcal.setTime(d1);
             sendDataVM vm=new sendDataVM();
             vm.name=title;
             List<Long> lonnn = new ArrayList<>();
             while (!gcal.getTime().after(d2)) {
                 Date d = gcal.getTime();
                 System.out.println(d);
                 String startD=format.format(d);
                 List<ClickyPlatformHardware> list=ClickyPlatformHardware.getAllData(d);
               //  JsonNode jsonList = Json.parse(callClickAPI("&type=pages-entrance&date="+startD+"&limit=all"));
                 
                 Long l=0L;
                 for(ClickyPlatformHardware lis:list) {
              	   	if(title.equals(lis.title)){
                	// ClickyPagesVM vm = new ClickyPagesVM();
              	   	String value =lis.value;
              	   	l=l+(long)Integer.parseInt(value);
              	  
              	   	}
              	   	}
                 lonnn.add(l);
                 String chartDate=format1.format(d);
                 dates.add(chartDate);
                gcal.add(Calendar.DAY_OF_MONTH, 1);
                 
             }
             vm.data=lonnn;
             data.add(vm);
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
	    return ok(Json.toJson(map));
    }
	    
	    public static Result saveCampaigns(){
	    	
	    	
	    	Form<CampaignsVMs> form = DynamicForm.form(CampaignsVMs.class).bindFromRequest();
	    	CampaignsVMs vm = form.get();
	    	
	    	
	    	CampaignsVM cam = new CampaignsVM();
	    	cam.name = vm.name;
	    	cam.matchType = vm.matchType;
	    	cam.matchString = vm.matchString;
	    	cam.locations=Location.findById(Long.valueOf(session("USER_LOCATION")));
	    	cam.save();
	    	
	    	return ok();
	    }
	    
	    public static Result getCampaign(){
	    	List<CampaignsVM> cVm = CampaignsVM.findByLocation(Long.valueOf(session("USER_LOCATION")));
	    	return ok(Json.toJson(cVm));
	    }
	    
	    public static Result getActionList(Integer value){
	    	int year = Calendar.getInstance().get(Calendar.YEAR);
	    	String params = null;
	    	
	    	if(value == 30){
	    		params = "&type=actions-list&date=last-30-days&limit=all";
	    	}else if(value == 7){
	    		params = "&type=actions-list&date=last-7-days&limit=all";
	    	}else if(value == 1){
	    		params = "&type=actions-list&date="+year+"&limit=all";
	    	}
	    	
	    	return ok(Json.parse(callClickAPI(params)));
	    }
	    
	    public static Result getSearchesListDale(String startDate,String endDate){
	    	String params = null;
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	              
	             List <ClickySearchesSearch> list=ClickySearchesSearch.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	          	 String uniquetitle = null;
	               	
	       	   	            for(ClickySearchesSearch lis:list){
	       	            	ClickyPagesVM vm = new ClickyPagesVM();
	       	            	
	       	               	String langValue = mapOffline.get(lis.title); 
	       	             	if (langValue == null) {
	       	             		uniquetitle = lis.title;
	       	             		mapOffline.put(lis.title, lis.title);
	       	            
	                 	     	   	vm.id=lis.id;
	                 				vm.value=lis.value;
	                 				vm.valuePercent = lis.valuePercent;
	                 				vm.title = uniquetitle;
	                 				vm.statsUrl =lis.statsUrl;
	                 	   			vm.averageActions=lis.averageAction;
	                 	   			vm.averageTime=lis.averageTime;
	                 	   			vm.totalTime=lis.totalTime;
	                 	   			vm.bounceRate=lis.bounceRate;
	                 	   		List <ClickySearchesSearch> list2=ClickySearchesSearch.getAll(beforeStart, d1) ;
	                 	   	    double count=0;
	                 	   	    double count1=0;
	                 	   	for(ClickySearchesSearch lis2:list) {
	               	    	String data1 = lis2.title;
	               	   		if(data1.equals(vm.title)){
	               	   			vm.value_percent2 = lis2.value;
	               	   		  count1=count1+Double.parseDouble(vm.value_percent2);
	               	   		
	               	   		}
	               	   		
	                 	   	}	
	                 	   	    
	                   	   	for(ClickySearchesSearch lis2:list2) {
	                   	   	String data1 = lis2.title;
	               	   		if(data1.equals(vm.title)){
	                   	   			vm.value_percent2 = lis2.value;
	                   	   		  count=count+Double.parseDouble(vm.value_percent2);
	                   	   		
	                   	   		}
	                 	   			
	                   	   	}
	                   	   vm.averagePercent=((count1-count)/count1)*100;
	                 	   				
	                 	   		    clickyList.add(vm);
	       	             	}
	       	   	       }
	       	   	     
	 	            for(ClickyPagesVM vm:clickyList) {
	    	   	    	  double value = 0;
	    	   	    	 for(ClickySearchesSearch lis:list){
	    	   	    		 if(vm.title.equals(lis.title)){
	    	   	    			value = value+Double.parseDouble(lis.value);
	    	   	    			vm.value = String.valueOf(value);
	    	   	    			
	    	   	    			vm.averageActions = lis.averageAction; 
	    	   	    			
	    	   	    			vm.totalTime = lis.totalTime; 
	    	   	    			
	    	   	    			vm.averageTime = lis.averageTime; 
	    	   	    			
	    	   	    			vm.bounceRate = lis.bounceRate; 
	    	   	    		
	    	   	    		 }
	    	   	    	  }
	    	   	    
	        	   }
	             
	             

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
	}
	    
	    public static Result getKeywordsList(String startDate,String endDate){
	    	String params = null;
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	              
	             List <ClickySearchesEngine> list=ClickySearchesEngine.getAll(d1, d2) ;
	       	   	            for(ClickySearchesEngine lis:list){
	       	            	ClickyPagesVM vm = new ClickyPagesVM();
	                 	     	   	vm.id=lis.id;
	                 				vm.value=lis.value;
	                 				vm.valuePercent = lis.valuePercent;
	                 				vm.title = lis.title;
	                 	   		    clickyList.add(vm);
	           	}
	    	 } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));

	    }
	    
	    public static Result getEngines(String startDate,String endDate){
	    	String params = null;
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	              

	             
	             List <ClickySearchesEngine> list=ClickySearchesEngine.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	               Integer valueCount=0;
	               double perCount=0;
	               double avgActCount=0;
	               double avgTimeCount=0;
	               double totTimeCount=0;
	               double bounRateCount=0;
	   	            for(ClickySearchesEngine lis:list){
	            	ClickyPagesVM vm = new ClickyPagesVM();
	      	     	   	vm.id=lis.id;
	      				String value=lis.value;
	      				String valuePercent=null;
	      				if(lis.valuePercent != null){
	      					valuePercent = lis.valuePercent;
	      				}
	      				else{
	      					valuePercent ="0";
	      				}
	      				String averageActions=lis.averageAction;
	      				String averageTime=lis.averageTime;
	      				String totalTime=lis.totalTime;
	      				String bounceRate=lis.bounceRate;
	      				
	      				
	      				String title = lis.title;
	      				valueCount=Integer.parseInt(value);
	      				perCount=Double.parseDouble(valuePercent);
	      				avgActCount=Double.parseDouble(averageActions);
	      				avgTimeCount=Double.parseDouble(averageTime);
	      				totTimeCount=Double.parseDouble(totalTime);
	      				bounRateCount=Double.parseDouble(bounceRate);
	      		     				String objectMake = mapOffline.get(title);
	      		        			if (objectMake == null) {
	      		        				mapOffline.put(title, valueCount+"&"+perCount+"&"+avgActCount+"&"+avgTimeCount+"&"+totTimeCount+"&"+bounRateCount);
	      		        			}else{
	      		        				 String arr[]=mapOffline.get(title).split("&");
	      		        				mapOffline.put(title, valueCount+Integer.parseInt(arr[0])+"&"+(perCount+Double.parseDouble(arr[1]))+"&"+(avgActCount+Double.parseDouble(arr[2]))
	      		        						+"&"+(avgTimeCount+Double.parseDouble(arr[3]))+"&"+(totTimeCount+Double.parseDouble(arr[4]))+"&"+(bounRateCount+Double.parseDouble(arr[5])));
	      		        			}
	   		
	   	           }
	   	            
	   	         List<bodyStyleSetVM> bSetVMsoffline = new ArrayList<>();
			     	for (Entry<String , String> entryValue : mapOffline.entrySet()) {
			     		ClickyPagesVM vm = new ClickyPagesVM();
						vm.title = entryValue.getKey();
						String arr[]=entryValue.getValue().split("&");
						vm.value = arr[0];
						vm.valuePercent=arr[1];
						vm.averageActions=arr[2];
						vm.averageTime=arr[3];
						vm.totalTime=arr[4];
						vm.bounceRate=arr[5];
						double count=0;
		      	   	    double count1=0;
		      	   	 List <ClickySearchesEngine> list1=ClickySearchesEngine.getAll(d1, d2) ;
		      	   	for(ClickySearchesEngine lis2:list1) {
		    	    	String url = lis2.title;
		    	   		if(url.equals(vm.title)){
		    	   			vm.value_percent2 = lis2.value;
		    	   		  count1=count1+Double.parseDouble(vm.value_percent2);
		    	   		
		    	   		}
		    	   		
		      	   	}	
		      	  List <ClickySearchesEngine> list2=ClickySearchesEngine.getAll(beforeStart, d1) ;
		        	   	for(ClickySearchesEngine lis2:list2) {
		        	    	String url = lis2.title;
		        	   		if(url.equals(vm.title)){
		        	   			vm.value_percent2 = lis2.value;
		        	   		  count=count+Double.parseDouble(vm.value_percent2);
		        	   		
		        	   		}
		      	   			
		        	   	}
		        	   	if(count1 != 0){
		        	   vm.averagePercent=((count1-count)/count1)*100;
		        	   	}
		        	   	else{
		        	   		vm.averagePercent=0;
		        	   	}
						
						clickyList.add(vm);
					}

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
	    }
	    
	    public static Result getRecent(String startDate,String endDate){
	    	String params = null;
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	              
	             List <ClickySearchesRecent> list=ClickySearchesRecent.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	          	 String uniqueurl = null;
	               	
	       	   	            for(ClickySearchesRecent lis:list){
	       	            	ClickyPagesVM vm = new ClickyPagesVM();
	       	            	
	       	            	String langValue = mapOffline.get(lis.title); 
	       	             	if (langValue == null) {
	       	             		uniqueurl = lis.title;
	       	             		mapOffline.put(lis.title, lis.title);
	       	            
	                 	     	   	vm.id=lis.id;
	                 				vm.time=lis.time;
	                 				vm.timePretty=lis.timePretty;
	                 				vm.statsUrl = lis.statsUrl;
	                 				vm.title = uniqueurl;
	                 	   		    clickyList.add(vm);
	       	   	}
	       	   	            }   
	    	            

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
	    }
	    
	    public static Result getNewestUni(String startDate,String endDate){
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	              
	             List <ClickySearchesNewest> list=ClickySearchesNewest.getAll(d1, d2) ;
	             Map<String, String> mapOffline = new HashMap<String, String>();
	          	 String uniqueurl = null;
	               	
	       	   	            for(ClickySearchesNewest lis:list){
	       	            	ClickyPagesVM vm = new ClickyPagesVM();
	       	            	
	       	            	
	       	               	String langValue = mapOffline.get(lis.title); 
	       	             	if (langValue == null) {
	       	             		uniqueurl = lis.title;
	       	             		mapOffline.put(lis.title, lis.title);
	       	            
	                 	     	   	vm.id=lis.id;
	                 				vm.time=lis.time;
	                 				vm.timePretty=lis.timePretty;
	                 				vm.statsUrl = lis.statsUrl;
	                 				vm.title = uniqueurl;
	                 	   		    clickyList.add(vm);
	       	   	}
	       	   	            }

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
	    }
	    
	    public static Result getSearchesLocal(String startDate,String endDate){
	    	String params = null;
	    	params = "&type=searches-local&date="+startDate+","+endDate+"&limit=all";
		    return ok(Json.parse(callClickAPI(params)));
	    }
	    
	    public static Result getRankings(String startDate,String endDate){
	    	String params = null;
	    	params = "&type=searches-rankings&date="+startDate+","+endDate+"&limit=all";
		    //return ok(Json.parse(callClickAPI(params)));
	    	
	    	Date d1=null;
	    	Date d2=null;
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    	 try {
	             d1 = format.parse(startDate);
	             d2 = format.parse(endDate);

	             long diff = d2.getTime() - d1.getTime();

	             long diffDays = diff / (24 * 60 * 60 * 1000);
	             Integer days=(int)diffDays;
	          Date beforeStart = DateUtils.addDays(d1, -days); 
	             String newDate=format.format(beforeStart);
	             System.out.print(newDate + " newDate ");
	             
	     	   	JsonNode jsonList = Json.parse(callClickAPI(params));
	     	   	
	     	   	for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
	     	  // 	String data = obj.get("url").textValue();
				//String arr[] = data.split("#_");	
	     	   	ClickyPagesVM vm = new ClickyPagesVM();
				vm.value = obj.get("value").textValue();
				vm.title = obj.get("title").textValue();
				//vm.stats_url = obj.get("stats_url").textValue();
				//vm.url = obj.get("url").textValue();
				//vm.showUrl = arr[0];
	     	   	JsonNode jsonActionsList = Json.parse(callClickAPI("&type=searches-rankings&date="+newDate+","+startDate+""));
	     	   	for(JsonNode obj1 : jsonActionsList.get(0).get("dates").get(0).get("items")) {
	     	    	//String data1 = obj1.get("url").textValue();
	     	   		//String arr1[] = data1.split("#_");
	     	   		//String url=arr1[0];
	     	   		if(obj1.get("title").textValue().equals(vm.title)){
	     	   			vm.value_percent2 = obj1.get("value").textValue();
	     	   			vm.averageActions=obj1.get("value").textValue();
	     	   		vm.averagePercent=((Double.parseDouble(vm.value)-Double.parseDouble(vm.value_percent2))/(Double.parseDouble(vm.value)))*100;
	     	   		}
	     	   	
	     	   	
	     	   	}
	     	   	
	     	   clickyList.add(vm);
	     	   	
	     	   	}
	             

	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
		    return ok(Json.toJson(clickyList));
	    }
	    
	    public static Result getDataForSearch(String url,String startDate,String endDate){
	    	
	    	//ClickyVisitorsList List = ClickyVisitorsList.findById(id);
	    	String title=url;
	    	String params = null; 
	    	List<ClickyPagesVM> clickyList = new ArrayList<>();
	    	params = "&type=segmentation&search="+title+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	    	
	    	/*else if(flagForLanding.equalsIgnoreCase("ForDomain")){
	    		params = "&type=segmentation&source=searches&domain=google.com&segments=summary&date="+startDate+","+endDate+"&limit=all";
	    	}
	    	else{
	    		params = "&type=segmentation&source=searches&search="+title+"&segments=summary&date="+startDate+","+endDate+"&limit=all";
	    	}*/
	    	 try {

	     	   	JsonNode jsonList = Json.parse(callClickAPI(params));
	     	   	for(JsonNode obj : jsonList.get(0).get("dates").get(0).get("items")) {
	     	   	ClickyPagesVM vm = new ClickyPagesVM();
				vm.title = obj.get("title").textValue();
				vm.value = obj.get("value").textValue();
	     	   	
	     	   clickyList.add(vm);
	     	   	
	     	   	}
	             
	         } catch (Exception e) {
	             e.printStackTrace();
	         }
	    	
	    	return ok(Json.toJson(clickyList));
	    }
	    
	    public static Result getsearchInfo(Long id , String startdate, String enddate){
			Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			ClickySearchesSearch oneRow = ClickySearchesSearch.findById(id);
			
			List<ClickySearchesSearch> mediaObjList = ClickySearchesSearch.findByTitleAndDate(oneRow.title, d1, d2);
			List<ClickySearchesSearch> allmedialist = ClickySearchesSearch.getAll(d1, d2);
			List <ClickyContentVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyContentVM vm = new ClickyContentVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickySearchesSearch lis:mediaObjList){
	         	
				 count1=count1+Double.parseDouble(lis.averageAction1);
				 count2=count2+Double.parseDouble(lis.visitors1);
				 count3=count3+Double.parseDouble(lis.uniqueVisitor1);
				 count4=count4+Double.parseDouble(lis.totalTime1);
				 count5=count5+Double.parseDouble(lis.averageTime1);
				 count6=count6+Double.parseDouble(lis.bounceRate1);
				 count7=count7+Double.parseDouble(lis.action1);
	   	   			
			 }
			 
			 double countAll1=0.0;
	 		double countAll2=0.0;
	 		double countAll3=0.0;
	 		double countAll4=0.0;
	 		double countAll5=0.0;
	 		double countAll6=0.0;
	 		double countAll7=0.0;
	 		 for(ClickySearchesSearch list:allmedialist){
	          	
	 			 countAll1=countAll1+Double.parseDouble(list.averageAction1);
	 			 countAll2=countAll2+Double.parseDouble(list.visitors1);
	 			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor1);
	 			 countAll4=countAll4+Double.parseDouble(list.totalTime1);
	 			 countAll5=countAll5+Double.parseDouble(list.averageTime1);
	 			 countAll6=countAll6+Double.parseDouble(list.bounceRate1);
	 			 countAll7=countAll7+Double.parseDouble(list.action1);
	    	   			
	 		 }
			 
	 		ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count1;
			 cVm3.all_visitors = countAll1;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count5;
			 cVm5.all_visitors = countAll5;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);
			 
			 vm.averageAction=count1;
			 vm.visitor=count2;
			 vm.uniqueVisitor=count3;
			 vm.totalTimes=count4;
			 vm.averageTimes=count5;
			 vm.bounceRates=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

	     	
	     	
			 
	     	return ok(Json.toJson(platformvm));
			
		}
	    
	    public static Result getEngineInfo(String title , String startdate, String enddate){
			Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			//ClickySearchesEngine oneRow = ClickySearchesEngine.findById(id);
			
			List<ClickySearchesEngine> mediaObjList = ClickySearchesEngine.findByTitleAndDate(title, d1, d2);
			List<ClickySearchesEngine> allmedialist = ClickySearchesEngine.getAll(d1, d2);
			List <ClickyContentVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyContentVM vm = new ClickyContentVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickySearchesEngine lis:mediaObjList){
	         	
				 count1=count1+Double.parseDouble(lis.averageAction1);
				 count2=count2+Double.parseDouble(lis.visitors1);
				 count3=count3+Double.parseDouble(lis.uniqueVisitor1);
				 count4=count4+Double.parseDouble(lis.totalTime1);
				 count5=count5+Double.parseDouble(lis.averageTime1);
				 count6=count6+Double.parseDouble(lis.bounceRate1);
				 count7=count7+Double.parseDouble(lis.action1);
	   	   			
			 }
			 
			 double countAll1=0.0;
	 		double countAll2=0.0;
	 		double countAll3=0.0;
	 		double countAll4=0.0;
	 		double countAll5=0.0;
	 		double countAll6=0.0;
	 		double countAll7=0.0;
	 		 for(ClickySearchesEngine list:allmedialist){
	          	
	 			 countAll1=countAll1+Double.parseDouble(list.averageAction1);
	 			 countAll2=countAll2+Double.parseDouble(list.visitors1);
	 			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor1);
	 			 countAll4=countAll4+Double.parseDouble(list.totalTime1);
	 			 countAll5=countAll5+Double.parseDouble(list.averageTime1);
	 			 countAll6=countAll6+Double.parseDouble(list.bounceRate1);
	 			 countAll7=countAll7+Double.parseDouble(list.action1);
	    	   			
	 		 }
			 
	 		ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count1;
			 cVm3.all_visitors = countAll1;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count5;
			 cVm5.all_visitors = countAll5;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);		 
			 vm.averageAction=count1;
			 vm.visitor=count2;
			 vm.uniqueVisitor=count3;
			 vm.totalTimes=count4;
			 vm.averageTimes=count5;
			 vm.bounceRates=count6;
			 vm.action=count7;
			
			 VMs.add(vm);
			 return ok(Json.toJson(platformvm));
			
		}
	    
	    public static Result getRecentInfo(Long id , String startdate, String enddate){
			Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			ClickySearchesRecent oneRow = ClickySearchesRecent.findById(id);
			
			List<ClickySearchesRecent> mediaObjList = ClickySearchesRecent.findByTitleAndDate(oneRow.title, d1, d2);
			List<ClickySearchesRecent> allmedialist = ClickySearchesRecent.getAll(d1, d2);
			List <ClickyContentVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyContentVM vm = new ClickyContentVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickySearchesRecent lis:mediaObjList){
	         	
				 count1=count1+Double.parseDouble(lis.averageAction);
				 count2=count2+Double.parseDouble(lis.visitors);
				 count3=count3+Double.parseDouble(lis.uniqueVisitor);
				 count4=count4+Double.parseDouble(lis.totalTime);
				 count5=count5+Double.parseDouble(lis.averageTime);
				 count6=count6+Double.parseDouble(lis.bounceRate);
				 count7=count7+Double.parseDouble(lis.action);
	   	   			
			 }
			 
			 double countAll1=0.0;
	 		double countAll2=0.0;
	 		double countAll3=0.0;
	 		double countAll4=0.0;
	 		double countAll5=0.0;
	 		double countAll6=0.0;
	 		double countAll7=0.0;
	 		 for(ClickySearchesRecent list:allmedialist){
	          	
	 			 countAll1=countAll1+Double.parseDouble(list.averageAction);
	 			 countAll2=countAll2+Double.parseDouble(list.visitors);
	 			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor);
	 			 countAll4=countAll4+Double.parseDouble(list.totalTime);
	 			 countAll5=countAll5+Double.parseDouble(list.averageTime);
	 			 countAll6=countAll6+Double.parseDouble(list.bounceRate);
	 			 countAll7=countAll7+Double.parseDouble(list.action);
	    	   			
	 		 }
			 
	 		ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count1;
			 cVm3.all_visitors = countAll1;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count5;
			 cVm5.all_visitors = countAll5;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);
			 
			 vm.averageAction=count1;
			 vm.visitor=count2;
			 vm.uniqueVisitor=count3;
			 vm.totalTimes=count4;
			 vm.averageTimes=count5;
			 vm.bounceRates=count6;
			 vm.action=count7;
			
			 VMs.add(vm);
			 return ok(Json.toJson(platformvm));
			
		}
	    
	    public static Result getNewestInfo(Long id , String startdate, String enddate){
			Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			ClickySearchesNewest oneRow = ClickySearchesNewest.findById(id);
			
			List<ClickySearchesNewest> mediaObjList = ClickySearchesNewest.findByTitleAndDate(oneRow.title, d1, d2);
			List<ClickySearchesNewest> allmedialist = ClickySearchesNewest.getAll(d1, d2);
			List <ClickyContentVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyContentVM vm = new ClickyContentVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickySearchesNewest lis:mediaObjList){
	         	
				 count1=count1+Double.parseDouble(lis.averageAction);
				 count2=count2+Double.parseDouble(lis.visitors);
				 count3=count3+Double.parseDouble(lis.uniqueVisitor);
				 if(!lis.totalTime.equals("")){
				 count4=count4+Double.parseDouble(lis.totalTime);
				 }
				 count5=count5+Double.parseDouble(lis.averageTime);
				 count6=count6+Double.parseDouble(lis.bounceRate);
				 if(!lis.action.equals("")){
				 count7=count7+Double.parseDouble(lis.action);
				 }
			 }
			 
			 double countAll1=0.0;
	 		double countAll2=0.0;
	 		double countAll3=0.0;
	 		double countAll4=0.0;
	 		double countAll5=0.0;
	 		double countAll6=0.0;
	 		double countAll7=0.0;
	 		 for(ClickySearchesNewest list:allmedialist){
	          	
	 			 countAll1=countAll1+Double.parseDouble(list.averageAction);
	 			 countAll2=countAll2+Double.parseDouble(list.visitors);
	 			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor);
	 			if(!list.totalTime.equals("")){
	 			 countAll4=countAll4+Double.parseDouble(list.totalTime);
	 			}
	 			 countAll5=countAll5+Double.parseDouble(list.averageTime);
	 			 countAll6=countAll6+Double.parseDouble(list.bounceRate);
	 			if(!list.action.equals("")){
	 			 countAll7=countAll7+Double.parseDouble(list.action);
	 			}		
	 		 }
			 
	 		ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm.difference = ((count2 - countAll2) / countAll2) * 100;
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueV";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 cVm1.difference = ((count3 - countAll3) / countAll3) * 100;
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 cVm2.difference = ((count7 - countAll7) / countAll7) * 100;
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAct";
			 cVm3.these_visitors = count1;
			 cVm3.all_visitors = countAll1;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 cVm3.difference = ((count1 - countAll1) / countAll1) * 100;
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalT";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 cVm4.difference = ((count4 - countAll4) / countAll4) * 100;
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageT";
			 cVm5.these_visitors = count5;
			 cVm5.all_visitors = countAll5;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 cVm5.difference = ((count5 - countAll5) / countAll5) * 100;
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceR";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 if(countAll6 !=0){
				 cVm6.difference = ((count6 - countAll6) / countAll6) * 100;
			 }
			 else{
				 cVm6.difference = 0.0;
			 }
			 platformvm.add(cVm6);		 
			 vm.averageAction=count1;
			 vm.visitor=count2;
			 vm.uniqueVisitor=count3;
			 vm.totalTimes=count4;
			 vm.averageTimes=count5;
			 vm.bounceRates=count6;
			 vm.action=count7;
			
			 VMs.add(vm);
			 return ok(Json.toJson(platformvm));
			
		}
	    
	    public static Result getRankingInfo(Long id , String startdate, String enddate){
			Date d1= null;
			Date d2= null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try{
	    		 d1 = format.parse(startdate);
	             d2 = format.parse(enddate);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			ClickySearchesRanking oneRow = ClickySearchesRanking.findById(id);
			
			List<ClickySearchesRanking> mediaObjList = ClickySearchesRanking.findByTitleAndDate(oneRow.title, d1, d2);
			List<ClickySearchesRanking> allmedialist = ClickySearchesRanking.getAll(d1, d2);
			List <ClickyContentVM> VMs = new ArrayList<>();
			List<ClickyPlatformVM> platformvm =new ArrayList<>();
			ClickyContentVM vm = new ClickyContentVM();
			double count1=0.0;
			double count2=0.0;
			double count3=0.0;
			double count4=0.0;
			double count5=0.0;
			double count6=0.0;
			double count7=0.0;
			 for(ClickySearchesRanking lis:mediaObjList){
	         	
				 count1=count1+Double.parseDouble(lis.averageAction);
				 count2=count2+Double.parseDouble(lis.visitors);
				 count3=count3+Double.parseDouble(lis.uniqueVisitor);
				 count4=count4+Double.parseDouble(lis.totalTime);
				 count5=count5+Double.parseDouble(lis.averageTime);
				 count6=count6+Double.parseDouble(lis.bounceRate);
				 count7=count7+Double.parseDouble(lis.action);
	   	   			
			 }
			 
			 double countAll1=0.0;
	 		double countAll2=0.0;
	 		double countAll3=0.0;
	 		double countAll4=0.0;
	 		double countAll5=0.0;
	 		double countAll6=0.0;
	 		double countAll7=0.0;
	 		 for(ClickySearchesRanking list:allmedialist){
	          	
	 			 countAll1=countAll1+Double.parseDouble(list.averageAction);
	 			 countAll2=countAll2+Double.parseDouble(list.visitors);
	 			 countAll3=countAll3+Double.parseDouble(list.uniqueVisitor);
	 			 countAll4=countAll4+Double.parseDouble(list.totalTime);
	 			 countAll5=countAll5+Double.parseDouble(list.averageTime);
	 			 countAll6=countAll6+Double.parseDouble(list.bounceRate);
	 			 countAll7=countAll7+Double.parseDouble(list.action);
	    	   			
	 		 }
			 
			 ClickyPlatformVM cVm = new ClickyPlatformVM();
			 cVm.title = "visitors";
			 cVm.these_visitors =  count2;
			 cVm.all_visitors = countAll2;
			 cVm.images = "//con.tent.network/media/icon_visitors.gif";
			 platformvm.add(cVm);
			 
			 ClickyPlatformVM cVm1 = new ClickyPlatformVM();
			 cVm1.title = "uniqueVisitors";
			 cVm1.these_visitors = count3;
			 cVm1.all_visitors = countAll3;
			 cVm1.images = "//con.tent.network/media/icon_visitors.gif";
			 platformvm.add(cVm1);
			 
			 ClickyPlatformVM cVm2 = new ClickyPlatformVM();
			 cVm2.title = "action";
			 cVm2.these_visitors = count7;
			 cVm2.all_visitors = countAll7;
			 cVm2.images = "//con.tent.network/media/icon_click.gif";
			 platformvm.add(cVm2);
			 
			 ClickyPlatformVM cVm3 = new ClickyPlatformVM();
			 cVm3.title = "averageAction";
			 cVm3.these_visitors = count1;
			 cVm3.all_visitors = countAll1;
			 cVm3.images = "//con.tent.network/media/icon_click.gif";
			 platformvm.add(cVm3);
			 
			 ClickyPlatformVM cVm4 = new ClickyPlatformVM();
			 cVm4.title = "totalTime";
			 cVm4.these_visitors = count4;
			 cVm4.all_visitors = countAll4;
			 cVm4.images = "//con.tent.network/media/icon_time.gif";
			 platformvm.add(cVm4);
			 
			 ClickyPlatformVM cVm5 = new ClickyPlatformVM();
			 cVm5.title = "averageTime";
			 cVm5.these_visitors = count5;
			 cVm5.all_visitors = countAll5;
			 cVm5.images = "//con.tent.network/media/icon_time.gif";
			 platformvm.add(cVm5);
			 
			 ClickyPlatformVM cVm6 = new ClickyPlatformVM();
			 cVm6.title = "bounceRate";
			 cVm6.these_visitors = count6;
			 cVm6.all_visitors = countAll6;
			 cVm6.images = "//con.tent.network/media/icon_bounce.gif";
			 platformvm.add(cVm6);
			 
			 vm.averageAction=count1;
			 vm.visitor=count2;
			 vm.uniqueVisitor=count3;
			 vm.totalTimes=count4;
			 vm.averageTimes=count5;
			 vm.bounceRates=count6;
			 vm.action=count7;
			
			 VMs.add(vm);

	     	return ok(Json.toJson(platformvm));
			
		}
	    
	    public static Result getSearchesPagesChart(){
	    	
    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
    		ClickyPagesVM vm1 = form.get();
    		String startDate=vm1.startDate;
    		String endDate=vm1.endDate;
    		String title=vm1.title;
    		
    		
    	String params = null;
    	System.out.println(startDate);
    	System.out.println(endDate);
    	Date d1 = null;
        Date d2 = null;
        Map<String, Object> map = new HashMap<>();
        List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		map.put("dates",dates);
		map.put("data",data);
        List<ClickyPagesVM> clickyList = new ArrayList<>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
    	 try {
             d1 = format.parse(startDate);
             d2 = format.parse(endDate);

             long diff = d2.getTime() - d1.getTime();

             long diffDays = diff / (24 * 60 * 60 * 1000);
             Integer days=(int)diffDays;
          Date beforeStart = DateUtils.addDays(d1, -days); 
             String newDate=format.format(beforeStart);
             System.out.print(newDate + " newDate ");
     	   	
             GregorianCalendar gcal = new GregorianCalendar();
             gcal.setTime(d1);
             sendDataVM vm=new sendDataVM();
             vm.name=title;
             List<Long> lonnn = new ArrayList<>();
             while (!gcal.getTime().after(d2)) {
                 Date d = gcal.getTime();
                 System.out.println(d);
                 String startD=format.format(d);
                 List<ClickySearchesSearch> list=ClickySearchesSearch.getAllData(d);
               //  JsonNode jsonList = Json.parse(callClickAPI("&type=pages-entrance&date="+startD+"&limit=all"));
                 
                 Long l=0L;
                 for(ClickySearchesSearch lis:list) {
              	   	if(title.equals(lis.title)){
                	// ClickyPagesVM vm = new ClickyPagesVM();
              	   	String value =lis.value;
              	   	l=l+(long)Integer.parseInt(value);
              	  
              	   	}
              	   	}
                 lonnn.add(l);
                 String chartDate=format1.format(d);
                 dates.add(chartDate);
                gcal.add(Calendar.DAY_OF_MONTH, 1);
                 
             }
             vm.data=lonnn;
             data.add(vm);
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
	    return ok(Json.toJson(map));
    }
	    public static Result getEnginesChart(){
	    	
    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
    		ClickyPagesVM vm1 = form.get();
    		String startDate=vm1.startDate;
    		String endDate=vm1.endDate;
    		String title=vm1.title;
    		
    		
    	String params = null;
    	System.out.println(startDate);
    	System.out.println(endDate);
    	Date d1 = null;
        Date d2 = null;
        Map<String, Object> map = new HashMap<>();
        List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		map.put("dates",dates);
		map.put("data",data);
        List<ClickyPagesVM> clickyList = new ArrayList<>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
    	 try {
             d1 = format.parse(startDate);
             d2 = format.parse(endDate);

             long diff = d2.getTime() - d1.getTime();

             long diffDays = diff / (24 * 60 * 60 * 1000);
             Integer days=(int)diffDays;
          Date beforeStart = DateUtils.addDays(d1, -days); 
             String newDate=format.format(beforeStart);
             System.out.print(newDate + " newDate ");
     	   	
             GregorianCalendar gcal = new GregorianCalendar();
             gcal.setTime(d1);
             sendDataVM vm=new sendDataVM();
             vm.name=title;
             List<Long> lonnn = new ArrayList<>();
             while (!gcal.getTime().after(d2)) {
                 Date d = gcal.getTime();
                 System.out.println(d);
                 String startD=format.format(d);
                 List<ClickySearchesEngine> list=ClickySearchesEngine.getAllData(d);
               //  JsonNode jsonList = Json.parse(callClickAPI("&type=pages-entrance&date="+startD+"&limit=all"));
                 
                 Long l=0L;
                 for(ClickySearchesEngine lis:list) {
              	   	if(title.equals(lis.title)){
                	// ClickyPagesVM vm = new ClickyPagesVM();
              	   	String value =lis.value;
              	   	l=l+(long)Integer.parseInt(value);
              	  
              	   	}
              	   	}
                 lonnn.add(l);
                 String chartDate=format1.format(d);
                 dates.add(chartDate);
                gcal.add(Calendar.DAY_OF_MONTH, 1);
                 
             }
             vm.data=lonnn;
             data.add(vm);
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
	    return ok(Json.toJson(map));
    	}
	    
	    public static Result getRankingsChart(){
	    	
    		Form<ClickyPagesVM> form = DynamicForm.form(ClickyPagesVM.class).bindFromRequest();
    		ClickyPagesVM vm1 = form.get();
    		String startDate=vm1.startDate;
    		String endDate=vm1.endDate;
    		String title=vm1.title;
    		
    		
    	String params = null;
    	System.out.println(startDate);
    	System.out.println(endDate);
    	Date d1 = null;
        Date d2 = null;
        Map<String, Object> map = new HashMap<>();
        List<sendDataVM> data = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		map.put("dates",dates);
		map.put("data",data);
        List<ClickyPagesVM> clickyList = new ArrayList<>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat format1 = new SimpleDateFormat("MMM dd");
    	 try {
             d1 = format.parse(startDate);
             d2 = format.parse(endDate);

             long diff = d2.getTime() - d1.getTime();

             long diffDays = diff / (24 * 60 * 60 * 1000);
             Integer days=(int)diffDays;
          Date beforeStart = DateUtils.addDays(d1, -days); 
             String newDate=format.format(beforeStart);
             System.out.print(newDate + " newDate ");
     	   	
             GregorianCalendar gcal = new GregorianCalendar();
             gcal.setTime(d1);
             sendDataVM vm=new sendDataVM();
             vm.name=title;
             List<Long> lonnn = new ArrayList<>();
             while (!gcal.getTime().after(d2)) {
                 Date d = gcal.getTime();
                 System.out.println(d);
                 String startD=format.format(d);
                 List<ClickySearchesRanking> list=ClickySearchesRanking.getAllData(d);
               //  JsonNode jsonList = Json.parse(callClickAPI("&type=pages-entrance&date="+startD+"&limit=all"));
                 
                 Long l=0L;
                 for(ClickySearchesRanking lis:list) {
              	   	if(title.equals(lis.title)){
                	// ClickyPagesVM vm = new ClickyPagesVM();
              	   	String value =lis.value;
              	   	l=l+(long)Integer.parseInt(value);
              	  
              	   	}
              	   	}
                 lonnn.add(l);
                 String chartDate=format1.format(d);
                 dates.add(chartDate);
                gcal.add(Calendar.DAY_OF_MONTH, 1);
                 
             }
             vm.data=lonnn;
             data.add(vm);
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
	    return ok(Json.toJson(map));
    	}
	    
	    public static Result getContentList(Integer value){
	    	
	    	String params = "&type=pages&date=last-30-days&limit=all";
	    	return ok(Json.parse(callClickAPI(params)));
	    }
	    public static Result getVehiclePriceLogs(Long id,String vin,String status){
			
    		Vehicle vehicle = null;
    		if(status.equalsIgnoreCase("Newly Arrived")){
    			vehicle = Vehicle.findByVinAndStatus(vin);
    		}else{
    			vehicle = Vehicle.findById(id);
    		}
    		List<PriceChange> pChange = PriceChange.findByVin(vin);
    		 
			List<PriceChangeVM> pList = new ArrayList<>();
			PriceChangeVM pVm1 = new PriceChangeVM();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if(vehicle != null){
				pVm1.person = "Has been added to the invnetory";
				if(vehicle.comingSoonFlag == 1){
					pVm1.dateTime = df.format(vehicle.comingSoonDate);
				}else{
					pVm1.dateTime = df.format(vehicle.postedDate);
				}
				
				pList.add(pVm1);
				
				for(PriceChange pChg:pChange){
					String dateChanege = df.format(pChg.getDateTime());
					Date changDate = null;
					try {
						changDate = df.parse(dateChanege);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(vehicle.soldDate == null){
						if (vehicle.postedDate.before(changDate) || vehicle.postedDate.equals(changDate)) {
							PriceChangeVM pVm = new PriceChangeVM();
							pVm.person = "Price Change "+pChg.price;
							pVm.dateTime = df.format(pChg.dateTime);
							pList.add(pVm);
						}
					}else{
						if ((vehicle.postedDate.before(changDate) || vehicle.postedDate.equals(changDate)) && changDate.before(vehicle.soldDate)) {
							PriceChangeVM pVm = new PriceChangeVM();
							pVm.person = "Price Change"+pChg.price;
							pVm.dateTime = df.format(pChg.dateTime);
							pList.add(pVm);
						}
					}
					
				}
				
				if(vehicle.soldDate !=null){
					pVm1 = new PriceChangeVM();
					pVm1.person = "Vehicle sold";
					pVm1.dateTime = df.format(vehicle.soldDate);
					pList.add(pVm1);
				}
			}
			return ok(Json.toJson(pList));
    	}
	    
		public static Result getCustomerRequest(Long id,String vin,String status, String startDate, String endDate){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
			List<sendDateAndValue> sAndValues = new ArrayList<>();
			int flagDate = 0;
			Map<Long, Long> mapdateRequest = new HashMap<Long, Long>();
			Map<Long, Long> mapdateTrade = new HashMap<Long, Long>();
			Map<Long, Long> mapdateSchedule = new HashMap<Long, Long>();
			
			List<List<Long>> lonRequest = new ArrayList<>();
			List<List<Long>> lonTrade = new ArrayList<>();
			List<List<Long>> lonSchedule = new ArrayList<>();
			List<List<Long>> lonFlag = new ArrayList<>();
			if(startDate.equals("0") || endDate.equals("0")){
				flagDate = 1;
			}
			
			if(status.equals("Newly Arrived")){
				
				Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
				
					if(vehicle != null){
				/*---------------Request More Info-----------------*/
				List<RequestMoreInfo> rInfo = RequestMoreInfo.findByVinAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
				sendDateAndValue sValue = new sendDateAndValue();
				for(RequestMoreInfo rMoreInfo:rInfo){
				  /*if((vehicle.postedDate.before(rMoreInfo.requestDate) && rMoreInfo.requestDate.before(vehicle.soldDate)) || vehicle.postedDate.equals(rMoreInfo.requestDate) || rMoreInfo.requestDate.equals(vehicle.soldDate)){*/
					if(vehicle.postedDate.before(rMoreInfo.requestDate) || vehicle.postedDate.equals(rMoreInfo.requestDate)){
					
						if(flagDate == 1){
							startDate = df.format(rMoreInfo.requestDate);
							endDate = df.format(rMoreInfo.requestDate);
						}
						try {
							if((rMoreInfo.requestDate.after(df.parse(startDate)) && rMoreInfo.requestDate.before(df.parse(endDate))) || rMoreInfo.requestDate.equals(df.parse(startDate)) || rMoreInfo.requestDate.equals(df.parse(endDate))){
								
								Long countCar = 1L;
								String DateString = df.format(rMoreInfo.requestDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								Long objectDate = mapdateRequest.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if (objectDate == null) {
									Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
									if(objectAllDate == null){
										mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
									}
									mapdateRequest.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
									
								}else{
									mapdateRequest.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
								}
							}
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 }	
				}
				
				for (Entry<Long, Long> entryValue : mapdateRequest.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); 
					lonRequest.add(value);
				  }
				sValue.data = lonRequest;
				sValue.name = "Request More Info";
			
				sAndValues.add(sValue);
				
			
				/*---------------Trade-In Appraisal-----------------*/
				List<TradeIn> tInfo = TradeIn.findByVinAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
				sendDateAndValue sValue1 = new sendDateAndValue();
				for(TradeIn tIn:tInfo){
					if(vehicle.postedDate.before(tIn.tradeDate) || vehicle.postedDate.equals(tIn.tradeDate)){
						if(flagDate == 1){
							startDate = df.format(tIn.tradeDate);
							endDate = df.format(tIn.tradeDate);
						}
						try {
							if((tIn.tradeDate.after(df.parse(startDate)) && tIn.tradeDate.before(df.parse(endDate))) || tIn.tradeDate.equals(df.parse(startDate)) || tIn.tradeDate.equals(df.parse(endDate))){
								Long countCar = 1L;
								String DateString = df.format(tIn.tradeDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								Long objectDate = mapdateTrade.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if (objectDate == null) {
									Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
									if(objectAllDate == null){
										mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
									}
									mapdateTrade.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
								}else{
									mapdateTrade.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
								}
							}
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 }
				}
				
				for (Entry<Long, Long> entryValue : mapdateTrade.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); 
					lonTrade.add(value);
				  }
				sValue1.data = lonTrade;
				sValue1.name = "Trade-In Appraisal";
			
				sAndValues.add(sValue1);
				
				/*---------------Schedule Test Drive-----------------*/
				List<ScheduleTest> sInfo = ScheduleTest.findByVinAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
				sendDateAndValue sValue2 = new sendDateAndValue();
				for(ScheduleTest sTest:sInfo){
					if(vehicle.postedDate.before(sTest.scheduleDate) || vehicle.postedDate.equals(sTest.scheduleDate)){
						
						if(flagDate == 1){
							startDate = df.format(sTest.scheduleDate);
							endDate = df.format(sTest.scheduleDate);
						}
						try {
							if((sTest.scheduleDate.after(df.parse(startDate)) && sTest.scheduleDate.before(df.parse(endDate))) || sTest.scheduleDate.equals(df.parse(startDate)) || sTest.scheduleDate.equals(df.parse(endDate))){
						
								Long countCar = 1L;
								String DateString = df.format(sTest.scheduleDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								Long objectDate = mapdateSchedule.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if (objectDate == null) {
									Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
									if(objectAllDate == null){
										mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
									}
									mapdateSchedule.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
								}else{
									mapdateSchedule.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
								}
							}
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}	 
				}
				
				for (Entry<Long, Long> entryValue : mapdateSchedule.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); 
					lonSchedule.add(value);
				  }
				sValue2.data = lonSchedule;
				sValue2.name = "Schedule Test Drive";
			
				sAndValues.add(sValue2);
				
				
				Vehicle vehicle1 = null;
				Date aajDate = null;
				if (status.equals("Newly Arrived")) {
					vehicle1 = Vehicle.findByVinAndStatus(vin);
					  aajDate = new Date();
				}
					int iDate = 2;
					Date addDates = vehicle1.postedDate;
				
					while(iDate > 0){
						Calendar c = Calendar.getInstance(); 
						c.setTime(addDates); 
						
						String DateString1 = df
								.format(c.getTime());
						Date dateDate1 = null;
						try {
							dateDate1 = df.parse(DateString1);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						c.add(Calendar.DATE, 1);
						
						Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
						if (objectAllDate == null) {
							mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
						}
						
						String DateString2 = df
								.format(aajDate);
						Date dateDate2 = null;
						try {
							dateDate2 = df.parse(DateString2);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						addDates = c.getTime();
						if(addDates.equals(dateDate2)){
							iDate = 0;
							break;
						}
					}
					
					
				
				
				for(sendDateAndValue sAndValue:sAndValues){
					for(List<Long> longs:sAndValue.data){
						int i = 0;
						for(Long long1:longs){
							if(i == 0){
								for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
									if(!entryValue.getValue().equals(0L)){
										if(!long1.equals(entryValue.getKey())){
											mapAlldate.put(entryValue.getKey(), 1L);
										}else{
											mapAlldate.put(entryValue.getKey(), 0L);
										}
									}
								  }
								i++;
							}
						}
					}
					for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
						if(entryValue.getValue().equals(1L)){
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(0L);//entryValue.getKey(),0L};
							sAndValue.data.add(value);
						}else{
							mapAlldate.put(entryValue.getKey(), 1L);
						}
					  }
				}
				
				

				
				for(sendDateAndValue sAndValue:sAndValues){
					
					Collections.sort(sAndValue.data, new Comparator<List<Long>>(){
						 @Override
				            public int compare(List<Long> o1, List<Long> o2) {
				                return o1.get(0).compareTo(o2.get(0));
				            }
						
					});
				}
			 }	
			}else if(status.equals("Sold")){
				
				Vehicle vehicle = Vehicle.findById(id);
				
				if(vehicle != null){
				/*---------------Request More Info-----------------*/
				List<RequestMoreInfo> rInfo = RequestMoreInfo.findByVinSoldAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
				sendDateAndValue sValue = new sendDateAndValue();
				for(RequestMoreInfo rMoreInfo:rInfo){
					if((vehicle.postedDate.before(rMoreInfo.requestDate) && vehicle.soldDate.after(rMoreInfo.requestDate)) || vehicle.postedDate.equals(rMoreInfo.requestDate) || vehicle.soldDate.equals(rMoreInfo.requestDate)){
					
						if(flagDate == 1){
							startDate = df.format(rMoreInfo.requestDate);
							endDate = df.format(rMoreInfo.requestDate);
						}
						try {
							if((rMoreInfo.requestDate.after(df.parse(startDate)) && rMoreInfo.requestDate.before(df.parse(endDate))) || rMoreInfo.requestDate.equals(df.parse(startDate)) || rMoreInfo.requestDate.equals(df.parse(endDate))){
								
								Long countCar = 1L;
								String DateString = df.format(rMoreInfo.requestDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								Long objectDate = mapdateRequest.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if (objectDate == null) {
									Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
									if(objectAllDate == null){
										mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
									}
									mapdateRequest.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
									
								}else{
									mapdateRequest.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
								}
							}
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 }	
				}
				
				for (Entry<Long, Long> entryValue : mapdateRequest.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); 
					lonRequest.add(value);
				  }
				sValue.data = lonRequest;
				sValue.name = "Request More Info";
			
				sAndValues.add(sValue);
				
			
				/*---------------Trade-In Appraisal-----------------*/
				List<TradeIn> tInfo = TradeIn.findByVinSoldAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
				sendDateAndValue sValue1 = new sendDateAndValue();
				for(TradeIn tIn:tInfo){
					if((vehicle.postedDate.before(tIn.tradeDate) && vehicle.soldDate.after(tIn.tradeDate)) || vehicle.postedDate.equals(tIn.tradeDate) || vehicle.soldDate.equals(tIn.tradeDate)){
						if(flagDate == 1){
							startDate = df.format(tIn.tradeDate);
							endDate = df.format(tIn.tradeDate);
						}
						try {
							if((tIn.tradeDate.after(df.parse(startDate)) && tIn.tradeDate.before(df.parse(endDate))) || tIn.tradeDate.equals(df.parse(startDate)) || tIn.tradeDate.equals(df.parse(endDate))){
								Long countCar = 1L;
								String DateString = df.format(tIn.tradeDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								Long objectDate = mapdateTrade.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if (objectDate == null) {
									Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
									if(objectAllDate == null){
										mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
									}
									mapdateTrade.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
								}else{
									mapdateTrade.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
								}
							}
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 }
				}
				
				for (Entry<Long, Long> entryValue : mapdateTrade.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); 
					lonTrade.add(value);
				  }
				sValue1.data = lonTrade;
				sValue1.name = "Trade-In Appraisal";
			
				sAndValues.add(sValue1);
				
				/*---------------Schedule Test Drive-----------------*/
				List<ScheduleTest> sInfo = ScheduleTest.findByVinSoldAndLocation(vin,Location.findById(Long.parseLong(session("USER_LOCATION"))));
				sendDateAndValue sValue2 = new sendDateAndValue();
				for(ScheduleTest sTest:sInfo){
					if((vehicle.postedDate.before(sTest.scheduleDate) && vehicle.soldDate.after(sTest.scheduleDate)) || vehicle.postedDate.equals(sTest.scheduleDate) || vehicle.soldDate.equals(sTest.scheduleDate)){
						
						if(flagDate == 1){
							startDate = df.format(sTest.scheduleDate);
							endDate = df.format(sTest.scheduleDate);
						}
						try {
							if((sTest.scheduleDate.after(df.parse(startDate)) && sTest.scheduleDate.before(df.parse(endDate))) || sTest.scheduleDate.equals(df.parse(startDate)) || sTest.scheduleDate.equals(df.parse(endDate))){
						
								Long countCar = 1L;
								String DateString = df.format(sTest.scheduleDate);
								Date dateDate = null;
								try {
									dateDate = df.parse(DateString);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								Long objectDate = mapdateSchedule.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
								if (objectDate == null) {
									Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
									if(objectAllDate == null){
										mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
									}
									mapdateSchedule.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), countCar);
								}else{
									mapdateSchedule.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), objectDate + countCar);
								}
							}
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}	 
				}
				
				for (Entry<Long, Long> entryValue : mapdateSchedule.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue()); 
					lonSchedule.add(value);
				  }
				sValue2.data = lonSchedule;
				sValue2.name = "Schedule Test Drive";
			
				sAndValues.add(sValue2);
				
				
				Vehicle vehicle1 = null;
				Date aajDate = null;
				if(status.equals("Sold")) {
					 vehicle1 = Vehicle.findById(id);
						aajDate = vehicle1.soldDate;
				}
					int iDate = 2;
					Date addDates = vehicle1.postedDate;
				
					while(iDate > 0){
						Calendar c = Calendar.getInstance(); 
						c.setTime(addDates); 
						
						String DateString1 = df
								.format(c.getTime());
						Date dateDate1 = null;
						try {
							dateDate1 = df.parse(DateString1);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						c.add(Calendar.DATE, 1);
						
						Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
						if (objectAllDate == null) {
							mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
						}
						
						String DateString2 = df
								.format(aajDate);
						Date dateDate2 = null;
						try {
							dateDate2 = df.parse(DateString2);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						addDates = c.getTime();
						if(addDates.equals(dateDate2)){
							iDate = 0;
							break;
						}
					}
				
				
				for(sendDateAndValue sAndValue:sAndValues){
					for(List<Long> longs:sAndValue.data){
						int i = 0;
						for(Long long1:longs){
							if(i == 0){
								for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
									if(!entryValue.getValue().equals(0L)){
										if(!long1.equals(entryValue.getKey())){
											mapAlldate.put(entryValue.getKey(), 1L);
										}else{
											mapAlldate.put(entryValue.getKey(), 0L);
										}
									}
									
								  }
								i++;
							}
							
						}
						
					}
					for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
						if(entryValue.getValue().equals(1L)){
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(0L);//entryValue.getKey(),0L};
							sAndValue.data.add(value);
							
						}else{
							mapAlldate.put(entryValue.getKey(), 1L);
						}
					  }
					
				}
				
				for(sendDateAndValue sAndValue:sAndValues){
					
					Collections.sort(sAndValue.data, new Comparator<List<Long>>(){
						 @Override
				            public int compare(List<Long> o1, List<Long> o2) {
				                return o1.get(0).compareTo(o2.get(0));
				            }
						
					});
				}
			}	
			}
			
			return ok(Json.toJson(sAndValues));
		}
		
		 public static Result getCustomerRequestFlag(Long id, String vin,String status, String startDate, String endDate) {
			 	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			 	Map<Long, Long> mapdateFlag = new HashMap<Long, Long>();
			 	SetPriceChangeFlag sPrice = new SetPriceChangeFlag();
			 	List<PriceFormatDate> sAndValues = new ArrayList<>();
			 	int flagDate = 0;
				//List<List<Long>> lonFlag = new ArrayList<>();
				if(startDate.equals("0") || endDate.equals("0")){
					flagDate = 1;
				}
				
				if(status.equals("Newly Arrived")){
					Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
					
					if(vehicle != null){
						sendDateAndValue sValue3 = new sendDateAndValue();
						
						List<PriceChange> pChanges = PriceChange.findByVin(vin);
						for(PriceChange pCh:pChanges){
							Long countCar = 1L;
							String DateString = df.format(pCh.dateTime);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							if(vehicle.postedDate.before(dateDate) || vehicle.postedDate.equals(dateDate)){
								
								if(flagDate == 1){
									startDate = df.format(dateDate);
									endDate = df.format(dateDate);
								}
								try {
									if((dateDate.after(df.parse(startDate)) && dateDate.before(df.parse(endDate))) || dateDate.equals(df.parse(startDate)) || dateDate.equals(df.parse(endDate))){
								
											Long objectDate = mapdateFlag.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												//Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
												//if(objectAllDate == null){
													//mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
												//}
												mapdateFlag.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), Long.parseLong(pCh.price));
											}else{
												mapdateFlag.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),Long.parseLong(pCh.price));
											}
									}
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							
						}
						
						
						for (Entry<Long, Long> entryValue : mapdateFlag.entrySet()) {
							PriceFormatDate pvalue = new PriceFormatDate();
							pvalue.x = entryValue.getKey();
							pvalue.title = "Price Change";
							pvalue.text = "Price has changed to "+entryValue.getValue();
							sAndValues.add(pvalue);
						  }
						/*for (Entry<Long, Long> entryValue : mapdateFlag.entrySet()) {
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(entryValue.getValue()); 
							lonFlag.add(value);
						  }*/
						
						sPrice.y = -220;
						sPrice.type = "flags";
						sPrice.data = sAndValues;
						sPrice.name = "Price Changes";
					
					}
				}else if(status.equals("Sold")){
					Vehicle vehicle = Vehicle.findById(id);
					
					if(vehicle != null){
						sendDateAndValue sValue3 = new sendDateAndValue();
						
						List<PriceChange> pChanges = PriceChange.findByVin(vin);
						for(PriceChange pCh:pChanges){
							Long countCar = 1L;
							String DateString = df.format(pCh.dateTime);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							if(vehicle.postedDate.before(dateDate) || vehicle.postedDate.equals(dateDate)){
								
								if(flagDate == 1){
									startDate = df.format(dateDate);
									endDate = df.format(dateDate);
								}
								try {
									if((dateDate.after(df.parse(startDate)) && dateDate.before(df.parse(endDate))) || dateDate.equals(df.parse(startDate)) || dateDate.equals(df.parse(endDate))){
								
											Long objectDate = mapdateFlag.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												//Long objectAllDate = mapAlldate.get(dateDate.getTime() + (1000 * 60 * 60 * 24));
												//if(objectAllDate == null){
													//mapAlldate.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), 1L);
												//}
												mapdateFlag.put(dateDate.getTime()+ (1000 * 60 * 60 * 24), Long.parseLong(pCh.price));
											}else{
												mapdateFlag.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),Long.parseLong(pCh.price));
											}
									}
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							
						}
						
						
						for (Entry<Long, Long> entryValue : mapdateFlag.entrySet()) {
							PriceFormatDate pvalue = new PriceFormatDate();
							pvalue.x = entryValue.getKey();
							pvalue.title = "Price Change";
							pvalue.text = "Price has changed to "+entryValue.getValue();
							sAndValues.add(pvalue);
						  }
						/*for (Entry<Long, Long> entryValue : mapdateFlag.entrySet()) {
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(entryValue.getValue()); 
							lonFlag.add(value);
						  }*/
						
						sPrice.y = -220;
						sPrice.type = "flags";
						sPrice.data = sAndValues;
						sPrice.name = "Price Changes";
					
					}
				}
				
				return ok(Json.toJson(sPrice));
		  }
		 
			public static Result getCustomerRequestLeads(Long id, String vin,
					String status, String startDate, String endDate) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Map<Long, Long> mapAlldate = new HashMap<Long, Long>();
				List<sendDateAndValue> sAndValues = new ArrayList<>();
				int flagDate = 0;
				Map<Long, Long> mapdateOffline = new HashMap<Long, Long>();
				Map<Long, Long> mapdateOnline = new HashMap<Long, Long>();
				List<List<Long>> lonOffline = new ArrayList<>();
				List<List<Long>> lonOnline = new ArrayList<>();
				List<PriceChangeVM> pList = new ArrayList<>(); 

				if (startDate.equals("0") || endDate.equals("0")) {
					flagDate = 1;
				}

				if (status.equals("Newly Arrived")) {

					Vehicle vehicle = Vehicle.findByVinAndStatus(vin);

					if (vehicle != null) {
						/*---------------Request More Info-----------------*/
						List<RequestMoreInfo> rInfo = RequestMoreInfo
								.findByVinAndLocation(vin, Location.findById(Long
										.parseLong(session("USER_LOCATION"))));
						sendDateAndValue sValue = new sendDateAndValue();
						sendDateAndValue sValue1 = new sendDateAndValue();
						for (RequestMoreInfo rMoreInfo : rInfo) {
							if (rMoreInfo.onlineOrOfflineLeads != 1) {

								if (vehicle.postedDate.before(rMoreInfo.requestDate)
										|| vehicle.postedDate
												.equals(rMoreInfo.requestDate)) {

									if (flagDate == 1) {
										startDate = df.format(rMoreInfo.requestDate);
										endDate = df.format(rMoreInfo.requestDate);
									}
									try {
										
										Long countCar = 1L;
										String DateString = df
												.format(rMoreInfo.requestDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df
												.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {


											Long objectDate = mapdateOffline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);

											} else {
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							} else if (rMoreInfo.onlineOrOfflineLeads == 1) {

								if (vehicle.postedDate.before(rMoreInfo.requestDate)
										|| vehicle.postedDate
												.equals(rMoreInfo.requestDate)) {

									if (flagDate == 1) {
										startDate = df.format(rMoreInfo.requestDate);
										endDate = df.format(rMoreInfo.requestDate);
									}
									try {
										
										Long countCar = 1L;
										String DateString = df
												.format(rMoreInfo.requestDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df
												.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {


											Long objectDate = mapdateOnline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);

											} else {
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}

						/*---------------Trade-In Appraisal-----------------*/
						List<TradeIn> tInfo = TradeIn.findByVinAndLocation(vin,
								Location.findById(Long
										.parseLong(session("USER_LOCATION"))));
						for (TradeIn tIn : tInfo) {
							if (tIn.onlineOrOfflineLeads != 1) {
								if (vehicle.postedDate.before(tIn.tradeDate)
										|| vehicle.postedDate.equals(tIn.tradeDate)) {
									if (flagDate == 1) {
										startDate = df.format(tIn.tradeDate);
										endDate = df.format(tIn.tradeDate);
									}
									try {
										
										Long countCar = 1L;
										String DateString = df
												.format(tIn.tradeDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										
										if ((dateDate.after(df.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {
											
											Long objectDate = mapdateOffline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);
											} else {
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							} else if (tIn.onlineOrOfflineLeads == 1) {
								if (vehicle.postedDate.before(tIn.tradeDate)
										|| vehicle.postedDate.equals(tIn.tradeDate)) {
									if (flagDate == 1) {
										startDate = df.format(tIn.tradeDate);
										endDate = df.format(tIn.tradeDate);
									}
									try {
										
										Long countCar = 1L;
										String DateString = df
												.format(tIn.tradeDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {
											

											Long objectDate = mapdateOnline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);
											} else {
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}

						/*---------------Schedule Test Drive-----------------*/
						List<ScheduleTest> sInfo = ScheduleTest.findByVinAndLocation(
								vin, Location.findById(Long
										.parseLong(session("USER_LOCATION"))));
						for (ScheduleTest sTest : sInfo) {
							if (sTest.onlineOrOfflineLeads != 1) {
								if (vehicle.postedDate.before(sTest.scheduleDate)
										|| vehicle.postedDate
												.equals(sTest.scheduleDate)) {

									if (flagDate == 1) {
										startDate = df.format(sTest.scheduleDate);
										endDate = df.format(sTest.scheduleDate);
									}
									try {
										Long countCar = 1L;
										String DateString = df
												.format(sTest.scheduleDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df
												.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {


											Long objectDate = mapdateOffline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);
											} else {
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							} else if (sTest.onlineOrOfflineLeads == 1) {
								if (vehicle.postedDate.before(sTest.scheduleDate)
										|| vehicle.postedDate
												.equals(sTest.scheduleDate)) {

									if (flagDate == 1) {
										startDate = df.format(sTest.scheduleDate);
										endDate = df.format(sTest.scheduleDate);
									}
									try {
										Long countCar = 1L;
										String DateString = df
												.format(sTest.scheduleDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df
												.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {

											

											Long objectDate = mapdateOnline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);
											} else {
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}

						for (Entry<Long, Long> entryValue : mapdateOffline.entrySet()) {
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(entryValue.getValue());
							lonOffline.add(value);
						}
						sValue.data = lonOffline;
						sValue.name = "Offline";

						sAndValues.add(sValue);

						for (Entry<Long, Long> entryValue : mapdateOnline.entrySet()) {
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(entryValue.getValue());
							lonOnline.add(value);
						}
						sValue1.data = lonOnline;
						sValue1.name = "Online";

						sAndValues.add(sValue1);
						
						
						Vehicle vehicle1 = null;
						Date aajDate = null;
						if (status.equals("Newly Arrived")) {
							vehicle1 = Vehicle.findByVinAndStatus(vin);
							  aajDate = new Date();
						}
							int iDate = 2;
							Date addDates = vehicle1.postedDate;
						
							while(iDate > 0){
								Calendar c = Calendar.getInstance(); 
								c.setTime(addDates); 
								
								
								String DateString1 = df
										.format(c.getTime());
								Date dateDate1 = null;
								try {
									dateDate1 = df.parse(DateString1);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								c.add(Calendar.DATE, 1);
								Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
								if (objectAllDate == null) {
									mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
								}
								
								String DateString2 = df
										.format(aajDate);
								Date dateDate2 = null;
								try {
									dateDate2 = df.parse(DateString2);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								addDates = c.getTime();
								if(addDates.equals(dateDate2)){
									iDate = 0;
									break;
								}
							}
						
						

						for (sendDateAndValue sAndValue : sAndValues) {
							for (List<Long> longs : sAndValue.data) {
								int i = 0;
								for (Long long1 : longs) {
									if (i == 0) {
										for (Entry<Long, Long> entryValue : mapAlldate
												.entrySet()) {
											if (!entryValue.getValue().equals(0L)) {
												if (!long1.equals(entryValue.getKey())) {
													mapAlldate.put(entryValue.getKey(),
															1L);
												} else {
													mapAlldate.put(entryValue.getKey(),
															0L);
												}
											}

										}
										i++;
									}

								}

							}
							for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
								if (entryValue.getValue().equals(1L)) {
									List<Long> value = new ArrayList<>();
									value.add(entryValue.getKey());
									value.add(0L);// entryValue.getKey(),0L};
									sAndValue.data.add(value);

								} else {
									mapAlldate.put(entryValue.getKey(), 1L);
								}
							}

						}

						for (sendDateAndValue sAndValue : sAndValues) {

							Collections.sort(sAndValue.data,
									new Comparator<List<Long>>() {
										@Override
										public int compare(List<Long> o1, List<Long> o2) {
											return o1.get(0).compareTo(o2.get(0));
										}

									});
						}

						
					}
				} else if (status.equals("Sold")) {

					Vehicle vehicle = Vehicle.findById(id);

					if (vehicle != null) {
						/*---------------Request More Info-----------------*/
						List<RequestMoreInfo> rInfo = RequestMoreInfo
								.findByVinSoldAndLocation(vin, Location.findById(Long
										.parseLong(session("USER_LOCATION"))));
						sendDateAndValue sValue = new sendDateAndValue();
						sendDateAndValue sValue1 = new sendDateAndValue();
						for (RequestMoreInfo rMoreInfo : rInfo) {
							if (rMoreInfo.onlineOrOfflineLeads != 1) {
								if ((vehicle.postedDate.before(rMoreInfo.requestDate) && vehicle.soldDate
										.after(rMoreInfo.requestDate))
										|| vehicle.postedDate
												.equals(rMoreInfo.requestDate)
										|| vehicle.soldDate
												.equals(rMoreInfo.requestDate)) {

									if (flagDate == 1) {
										startDate = df.format(rMoreInfo.requestDate);
										endDate = df.format(rMoreInfo.requestDate);
									}
									try {
										
										Long countCar = 1L;
										String DateString = df
												.format(rMoreInfo.requestDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df
												.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {

											

											Long objectDate = mapdateOffline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);

											} else {
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							} else if (rMoreInfo.onlineOrOfflineLeads == 1) {
								if ((vehicle.postedDate.before(rMoreInfo.requestDate) && vehicle.soldDate
										.after(rMoreInfo.requestDate))
										|| vehicle.postedDate
												.equals(rMoreInfo.requestDate)
										|| vehicle.soldDate
												.equals(rMoreInfo.requestDate)) {

									if (flagDate == 1) {
										startDate = df.format(rMoreInfo.requestDate);
										endDate = df.format(rMoreInfo.requestDate);
									}
									try {
										
										Long countCar = 1L;
										String DateString = df
												.format(rMoreInfo.requestDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df
												.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {


											Long objectDate = mapdateOnline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);

											} else {
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}

						/*---------------Trade-In Appraisal-----------------*/
						List<TradeIn> tInfo = TradeIn.findByVinSoldAndLocation(vin,
								Location.findById(Long
										.parseLong(session("USER_LOCATION"))));

						for (TradeIn tIn : tInfo) {
							if (tIn.onlineOrOfflineLeads != 1) {
								if ((vehicle.postedDate.before(tIn.tradeDate) && vehicle.soldDate
										.after(tIn.tradeDate))
										|| vehicle.postedDate.equals(tIn.tradeDate)
										|| vehicle.soldDate.equals(tIn.tradeDate)) {
									if (flagDate == 1) {
										startDate = df.format(tIn.tradeDate);
										endDate = df.format(tIn.tradeDate);
									}
									try {
										
										Long countCar = 1L;
										String DateString = df
												.format(tIn.tradeDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {
											

											Long objectDate = mapdateOffline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);
											} else {
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							} else if (tIn.onlineOrOfflineLeads != 1) {
								if ((vehicle.postedDate.before(tIn.tradeDate) && vehicle.soldDate
										.after(tIn.tradeDate))
										|| vehicle.postedDate.equals(tIn.tradeDate)
										|| vehicle.soldDate.equals(tIn.tradeDate)) {
									if (flagDate == 1) {
										startDate = df.format(tIn.tradeDate);
										endDate = df.format(tIn.tradeDate);
									}
									try {
										Long countCar = 1L;
										String DateString = df
												.format(tIn.tradeDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {
											

											Long objectDate = mapdateOnline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);
											} else {
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}

						/*---------------Schedule Test Drive-----------------*/
						List<ScheduleTest> sInfo = ScheduleTest
								.findByVinSoldAndLocation(vin, Location.findById(Long
										.parseLong(session("USER_LOCATION"))));
						for (ScheduleTest sTest : sInfo) {
							if (sTest.onlineOrOfflineLeads != 1) {
								if ((vehicle.postedDate.before(sTest.scheduleDate) && vehicle.soldDate
										.after(sTest.scheduleDate))
										|| vehicle.postedDate
												.equals(sTest.scheduleDate)
										|| vehicle.soldDate.equals(sTest.scheduleDate)) {

									if (flagDate == 1) {
										startDate = df.format(sTest.scheduleDate);
										endDate = df.format(sTest.scheduleDate);
									}
									try {
										
										Long countCar = 1L;
										String DateString = df
												.format(sTest.scheduleDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										if ((dateDate.after(df
												.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {


											Long objectDate = mapdateOffline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);
											} else {
												mapdateOffline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							} else if (sTest.onlineOrOfflineLeads != 1) {
								if ((vehicle.postedDate.before(sTest.scheduleDate) && vehicle.soldDate
										.after(sTest.scheduleDate))
										|| vehicle.postedDate
												.equals(sTest.scheduleDate)
										|| vehicle.soldDate.equals(sTest.scheduleDate)) {

									if (flagDate == 1) {
										startDate = df.format(sTest.scheduleDate);
										endDate = df.format(sTest.scheduleDate);
									}
									try {
										
										Long countCar = 1L;
										String DateString = df
												.format(sTest.scheduleDate);
										Date dateDate = null;
										try {
											dateDate = df.parse(DateString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										
										if ((dateDate.after(df
												.parse(startDate)) && dateDate
												.before(df.parse(endDate)))
												|| dateDate.equals(df
														.parse(startDate))
												|| dateDate.equals(df
														.parse(endDate))) {

											
											Long objectDate = mapdateOnline
													.get(dateDate.getTime()
															+ (1000 * 60 * 60 * 24));
											if (objectDate == null) {
												Long objectAllDate = mapAlldate
														.get(dateDate.getTime()
																+ (1000 * 60 * 60 * 24));
												if (objectAllDate == null) {
													mapAlldate
															.put(dateDate.getTime()
																	+ (1000 * 60 * 60 * 24),
																	1L);
												}
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														countCar);
											} else {
												mapdateOnline.put(dateDate.getTime()
														+ (1000 * 60 * 60 * 24),
														objectDate + countCar);
											}
										}
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}

						for (Entry<Long, Long> entryValue : mapdateOffline.entrySet()) {
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(entryValue.getValue());
							lonOffline.add(value);
						}
						sValue.data = lonOffline;
						sValue.name = "Offline";

						sAndValues.add(sValue);

						for (Entry<Long, Long> entryValue : mapdateOnline.entrySet()) {
							List<Long> value = new ArrayList<>();
							value.add(entryValue.getKey());
							value.add(entryValue.getValue());
							lonOnline.add(value);
						}
						sValue1.data = lonOnline;
						sValue1.name = "Online";

						sAndValues.add(sValue1);
						
						
						
						Vehicle vehicle1 = null;
						Date aajDate = null;
						if(status.equals("Sold")) {
							 vehicle1 = Vehicle.findById(id);
								aajDate = vehicle1.soldDate;
						}
							int iDate = 2;
							Date addDates = vehicle1.postedDate;
						
							while(iDate > 0){
								Calendar c = Calendar.getInstance(); 
								c.setTime(addDates); 
								
								
								String DateString1 = df
										.format(c.getTime());
								Date dateDate1 = null;
								try {
									dateDate1 = df.parse(DateString1);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									
								}
								
								c.add(Calendar.DATE, 1);
								
								Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
								if (objectAllDate == null) {
									mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
								}
								
								String DateString2 = df
										.format(aajDate);
								Date dateDate2 = null;
								try {
									dateDate2 = df.parse(DateString2);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								addDates = c.getTime();
								if(addDates.equals(dateDate2)){
									iDate = 0;
									break;
								}
							}
						
						

						for (sendDateAndValue sAndValue : sAndValues) {
							for (List<Long> longs : sAndValue.data) {
								int i = 0;
								for (Long long1 : longs) {
									if (i == 0) {
										for (Entry<Long, Long> entryValue : mapAlldate
												.entrySet()) {
											if (!entryValue.getValue().equals(0L)) {
												if (!long1.equals(entryValue.getKey())) {
													mapAlldate.put(entryValue.getKey(),
															1L);
												} else {
													mapAlldate.put(entryValue.getKey(),
															0L);
												}
											}

										}
										i++;
									}

								}

							}
							for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
								if (entryValue.getValue().equals(1L)) {
									List<Long> value = new ArrayList<>();
									value.add(entryValue.getKey());
									value.add(0L);// entryValue.getKey(),0L};
									sAndValue.data.add(value);

								} else {
									mapAlldate.put(entryValue.getKey(), 1L);
								}
							}

						}

						for (sendDateAndValue sAndValue : sAndValues) {

							Collections.sort(sAndValue.data,
									new Comparator<List<Long>>() {
										@Override
										public int compare(List<Long> o1, List<Long> o2) {
											return o1.get(0).compareTo(o2.get(0));
										}

									});
						}
						
						List<PriceChange> pChange = PriceChange.findByVin(vin);
						
						PriceChangeVM pVm1 = new PriceChangeVM();
						pVm1.person = "Has been added to the invnetory";
						pVm1.dateTime =vehicle.postedDate.toString();
						pList.add(pVm1);
						
						for(PriceChange pChg:pChange){
							String dateChanege = df.format(pChg.getDateTime());
							Date changDate = null;
							try {
								changDate = df.parse(dateChanege);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							if (vehicle.postedDate.before(changDate) && vehicle.soldDate.after(changDate)){
								PriceChangeVM pVm = new PriceChangeVM();
								pVm.person = "Price Change"+pChg.price;
								pVm.dateTime = df.format(pChg.dateTime);
								pList.add(pVm);
							}
						}
						
					}
				}
			//	Map map = new HashMap();
			//	map.put("sAndValues",sAndValues);
				//map.put("pList",pList);

				return ok(Json.toJson(sAndValues));
			}
			
			public static Result getviniewsChartLeads(Long id, String vin,
					String status, String startDate, String endDate) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MMM-dd");
				Map<Long, Long> mapdateView = new HashMap<Long, Long>();
				Map<Long,Long> mapAlldate = new HashMap<Long, Long>();
				List<List<Long>> lonn = new ArrayList<>();
				List<sendDateAndValue> sAndValues = new ArrayList<>();
				sendDateAndValue sValue = new sendDateAndValue();
				
				
				
				String params1 = "&type=visitors-list&date=last-30-days&limit=all";
					int totalTime = 0;
					int flagDate = 1;
					int lagCount = 0;
					int newuserCount = 0;
					int avgDur = 0;
					
					if (startDate.equals("0") || endDate.equals("0")) {
						flagDate = 1;
					}

					JSONArray jsonArray;
					try {
						jsonArray = new JSONArray(callClickAPI(params1)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
						for(int i=0;i<jsonArray.length();i++){
							
							String checkDate = null;
							Date thedate = null;
							if (status.equals("Newly Arrived")) {
								
								Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
								
									String arr[] = jsonArray.getJSONObject(i).get("time_pretty").toString().split(" ");
									String arrNew[] = arr[3].split(",");
									checkDate = arrNew[0]+"-"+arr[1]+"-"+arr[2];
									try {
										thedate = df1.parse(checkDate);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									int flagLoop = 0;
									if(vehicle.comingSoonFlag == 1){
										if (vehicle.comingSoonDate.before(thedate) || vehicle.comingSoonDate.equals(thedate)) {
											flagLoop = 1;
										}
									}else{
										if (vehicle.postedDate.before(thedate) || vehicle.postedDate.equals(thedate)) {
											flagLoop = 1;
										}
									}
									
									if (flagLoop == 1) {
										
										if (flagDate == 1) {
											startDate = df.format(thedate);
											endDate = df.format(thedate);
										}
										try {
											if ((thedate.after(df.parse(startDate)) && thedate.before(df.parse(endDate))) || thedate.equals(df.parse(startDate)) || thedate.equals(df.parse(endDate))) {
											
											Long countCar = 1L;
											String data = jsonArray.getJSONObject(i).get("landing_page").toString();
											String arrVin[] = data.split("/");
											if(arrVin.length > 5){
											  if(arrVin[5] != null){
												  if(arrVin[5].equals(vin)){
													  Long objectDate = mapdateView.get(thedate.getTime()+ (1000 * 60 * 60 * 24));
														if (objectDate == null) {
															mapdateView.put(thedate.getTime()+ (1000 * 60 * 60 * 24),countCar);
														} else {
															mapdateView.put(thedate.getTime()+ (1000 * 60 * 60 * 24),objectDate + countCar);
														}
												}
											  }
											}
			}
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}		
							}else if (status.equals("Sold")) {
								Vehicle vehicle = Vehicle.findById(id);
								
								String arr[] = jsonArray.getJSONObject(i).get("time_pretty").toString().split(" ");
								String arrNew[] = arr[3].split(",");
								checkDate = arrNew[0]+"-"+arr[1]+"-"+arr[2];
								try {
									thedate = df1.parse(checkDate);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if ((vehicle.postedDate.before(thedate) && vehicle.soldDate
										.after(thedate))
										|| vehicle.postedDate
												.equals(thedate)
										|| vehicle.soldDate
												.equals(thedate)) {
									
									if (flagDate == 1) {
										startDate = df.format(thedate);
										endDate = df.format(thedate);
									}
										
									try {
										if ((thedate.after(df.parse(startDate)) && thedate.before(df.parse(endDate))) || thedate.equals(df.parse(startDate)) || thedate.equals(df.parse(endDate))) {
										
										Long countCar = 1L;
										String data = jsonArray.getJSONObject(i).get("landing_page").toString();
										String arrVin[] = data.split("/");
										if(arrVin.length > 5){
										  if(arrVin[5] != null){
											  if(arrVin[5].equals(vin)){
												  Long objectDate = mapdateView.get(thedate.getTime()+ (1000 * 60 * 60 * 24));
													if (objectDate == null) {
														mapdateView.put(thedate.getTime()+ (1000 * 60 * 60 * 24),countCar);
													} else {
														mapdateView.put(thedate.getTime()+ (1000 * 60 * 60 * 24),objectDate + countCar);
													}
											}
										  }
										}
										
										
											}
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					    			
					    			
								}
							}
			    			
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					for (Entry<Long, Long> entryValue : mapdateView.entrySet()) {
						List<Long> value = new ArrayList<>();
						value.add(entryValue.getKey());
						value.add(entryValue.getValue());
						lonn.add(value);
					}
					
					sValue.data = lonn;
					sValue.name = "Views";
					
					sAndValues.add(sValue);
					
					Vehicle vehicle1 = null;
					Date aajDate = null;
					if (status.equals("Newly Arrived")) {
						vehicle1 = Vehicle.findByVinAndStatus(vin);
						  aajDate = new Date();
					}else if(status.equals("Sold")) {
						 vehicle1 = Vehicle.findById(id);
							aajDate = vehicle1.soldDate;
					}
						int iDate = 2;
						Date addDates = vehicle1.postedDate;
					
						while(iDate > 0){
							Calendar c = Calendar.getInstance(); 
							c.setTime(addDates); 
							
							
							String DateString1 = df
									.format(c.getTime());
							Date dateDate1 = null;
							try {
								dateDate1 = df.parse(DateString1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							c.add(Calendar.DATE, 1);
							
							Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
							if (objectAllDate == null) {
								mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
							}
							
							String DateString2 = df
									.format(aajDate);
							Date dateDate2 = null;
							try {
								dateDate2 = df.parse(DateString2);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							addDates = c.getTime();
							if(addDates.equals(dateDate2)){
								iDate = 0;
								break;
							}
						}
					
					
					for (sendDateAndValue sAndValue : sAndValues) {
						for (List<Long> longs : sAndValue.data) {
							int i = 0;
							for (Long long1 : longs) {
								if (i == 0) {
									for (Entry<Long, Long> entryValue : mapAlldate
											.entrySet()) {
										if (!entryValue.getValue().equals(0L)) {
											if (!long1.equals(entryValue.getKey())) {
												mapAlldate.put(entryValue.getKey(),
														1L);
											} else {
												mapAlldate.put(entryValue.getKey(),
														0L);
											}
										}

									}
									i++;
								}

							}

						}
						for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
							if (entryValue.getValue().equals(1L)) {
								List<Long> value = new ArrayList<>();
								value.add(entryValue.getKey());
								value.add(0L);// entryValue.getKey(),0L};
								sAndValue.data.add(value);

							} else {
								mapAlldate.put(entryValue.getKey(), 1L);
							}
						}

					}

					
					for (sendDateAndValue sAndValue : sAndValues) {

						Collections.sort(sAndValue.data,
								new Comparator<List<Long>>() {
									@Override
									public int compare(List<Long> o1, List<Long> o2) {
										return o1.get(0).compareTo(o2.get(0));
									}
								});
					}

				
					return ok(Json.toJson(sAndValues));
			}
			
			public static Result getCustomerLounchDateFlag(Long id, String vin,String status, String startDate, String endDate) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				List<PriceFormatDate> sAndValues = new ArrayList<>();
				SetPriceChangeFlag sPrice = new SetPriceChangeFlag();
				if(status.equals("Newly Arrived")){
					Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
					if(vehicle.comingSoonFlag == 1){
					
						for (int i=0;i<1;i++) {
							PriceFormatDate pvalue = new PriceFormatDate();
							pvalue.x = vehicle.comingSoonDate.getTime()+ (1000 * 60 * 60 * 24);
							pvalue.title = "Launch Date";
							pvalue.text = "Launch Date";
							sAndValues.add(pvalue);
						  }
						sPrice.y = -220;
						sPrice.type = "flags";
						sPrice.data = sAndValues;
						sPrice.name = "Launch Date";
					}
				}	
				
				return ok(Json.toJson(sPrice));
			}
			
			public static Result getFollowerLeads(Long id, String vin,String status, String startDate, String endDate) {
				
				int flagDate = 0;
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Map<Long, Long> mapdateFollower = new HashMap<Long, Long>();
				Map<Long,Long> mapAlldate = new HashMap<Long, Long>();
				List<List<Long>> lonn = new ArrayList<>();
				List<sendDateAndValue> sAndValues = new ArrayList<>();
				sendDateAndValue sValue = new sendDateAndValue();
				
				if (startDate.equals("0") || endDate.equals("0")) {
					flagDate = 1;
				}

				if (status.equals("Newly Arrived")) {
				
					Vehicle vehicle = Vehicle.findByVinAndStatus(vin);
					
					List<PriceAlert> pInfo = PriceAlert.getEmailsByVin(vin, Long.parseLong(session("USER_LOCATION")));
					if(vehicle != null){
						
					for(PriceAlert pAlert:pInfo){
						if (vehicle.postedDate.before(pAlert.currDate) || vehicle.postedDate.equals(pAlert.currDate)) {
							Long countCar = 1L;
							String DateString = df.format(pAlert.currDate);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							Long objectDate = mapdateFollower.get(dateDate.getTime()+ (1000 * 60 * 60 * 24));
							if (objectDate == null) {
								
								mapdateFollower.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),countCar);

							} else {
								mapdateFollower.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),objectDate + countCar);
							}
						}
					}
				}
					
					
				}else if (status.equals("Sold")) {
					
					Vehicle vehicle = Vehicle.findById(id);
					
					List<PriceAlert> pInfo = PriceAlert.getEmailsByVin(vin, Long.parseLong(session("USER_LOCATION")));
					
					for(PriceAlert pAlert:pInfo){
						
						if ((vehicle.postedDate.before(pAlert.currDate) && vehicle.soldDate
								.after(pAlert.currDate))
								|| vehicle.postedDate
										.equals(pAlert.currDate)
								|| vehicle.soldDate
										.equals(pAlert.currDate)) {
						
							Long countCar = 1L;
							String DateString = df.format(pAlert.currDate);
							Date dateDate = null;
							try {
								dateDate = df.parse(DateString);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							Long objectDate = mapdateFollower.get(dateDate.getTime()+ (1000 * 60 * 60 * 24));
							if (objectDate == null) {
								
								mapdateFollower.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),countCar);

							} else {
								mapdateFollower.put(dateDate.getTime()+ (1000 * 60 * 60 * 24),objectDate + countCar);
							}
						}
					}
					
				}
				
				for (Entry<Long, Long> entryValue : mapdateFollower.entrySet()) {
					List<Long> value = new ArrayList<>();
					value.add(entryValue.getKey());
					value.add(entryValue.getValue());
					lonn.add(value);
				}
				
				sValue.data = lonn;
				sValue.name = "Follower";
				
				sAndValues.add(sValue);
				
				Vehicle vehicle1 = null;
				Date aajDate = null;
				if (status.equals("Newly Arrived")) {
					vehicle1 = Vehicle.findByVinAndStatus(vin);
					  aajDate = new Date();
				}else if (status.equals("Sold")) {
					vehicle1 = Vehicle.findById(id);
					aajDate = vehicle1.soldDate;
				}	
					int iDate = 2;
					Date addDates = vehicle1.postedDate;
				
					while(iDate > 0){
						Calendar c = Calendar.getInstance(); 
						c.setTime(addDates); 
						
						
						String DateString1 = df
								.format(c.getTime());
						Date dateDate1 = null;
						try {
							dateDate1 = df.parse(DateString1);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						c.add(Calendar.DATE, 1);
						
						Long objectAllDate = mapAlldate.get(dateDate1.getTime() + (1000 * 60 * 60 * 24));
						if (objectAllDate == null) {
							mapAlldate.put(dateDate1.getTime() + (1000 * 60 * 60 * 24),1L);
						}
						
						String DateString2 = df
								.format(aajDate);
						Date dateDate2 = null;
						try {
							dateDate2 = df.parse(DateString2);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						addDates = c.getTime();
						if(addDates.equals(dateDate2)){
							iDate = 0;
							break;
						}
					}
					
				
					for (sendDateAndValue sAndValue : sAndValues) {
						for (List<Long> longs : sAndValue.data) {
							int i = 0;
							for (Long long1 : longs) {
								if (i == 0) {
									for (Entry<Long, Long> entryValue : mapAlldate
											.entrySet()) {
										if (!entryValue.getValue().equals(0L)) {
											if (!long1.equals(entryValue.getKey())) {
												mapAlldate.put(entryValue.getKey(),
														1L);
											} else {
												mapAlldate.put(entryValue.getKey(),
														0L);
											}
										}

									}
									i++;
								}

							}

						}
						for (Entry<Long, Long> entryValue : mapAlldate.entrySet()) {
							if (entryValue.getValue().equals(1L)) {
								List<Long> value = new ArrayList<>();
								value.add(entryValue.getKey());
								value.add(0L);// entryValue.getKey(),0L};
								sAndValue.data.add(value);

							} else {
								mapAlldate.put(entryValue.getKey(), 1L);
							}
						}

					}
				
				
				for (sendDateAndValue sAndValue : sAndValues) {

					Collections.sort(sAndValue.data,
							new Comparator<List<Long>>() {
								@Override
								public int compare(List<Long> o1, List<Long> o2) {
									return o1.get(0).compareTo(o2.get(0));
								}
							});
				}
				
					return ok(Json.toJson(sAndValues));
			}
			
			public static Result getSessionDaysVisitorsStats(String value,String lasttime) {
		    	
		    	List<Integer> allVisitor = new ArrayList<Integer>(30);
		    	List<String> months = new ArrayList<String>(30);
		    	int visitorCount = 0;
		    	Calendar c = Calendar.getInstance();
		    	
		    	if(lasttime.equals("month")){
		    		
		        	String[] monthsArr = new String[30]; 
		        	c.add(Calendar.DAY_OF_YEAR, -29);
		        	
		        	for(int i=0;i<30;i++) {
		        		visitorCount = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		String dates = year + month + days;
		            	
		        		String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		try {
		    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){
		    	    				  if(arr[5].equals(value)){
		    	    					  visitorCount = visitorCount + 1;
		    	    				  }
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (JSONException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        		
		        		allVisitor.add(visitorCount);
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("week")){
		    		
		    		
		        	String[] monthsArr = new String[7]; 
		        	c.add(Calendar.DAY_OF_YEAR, -6);
		        	
		        	for(int i=0;i<7;i++) {
		        		visitorCount = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		String dates = year + month + days;
		            	
		        		String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		try {
		    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){
		    	    				  if(arr[5].equals(value)){
		    	    					  visitorCount = visitorCount + 1;
		    	    				  }
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (JSONException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        		
		        		allVisitor.add(visitorCount);
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("day")){
		    		
		    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    		Date date = new Date();
		    		List<String> monthsArr = new ArrayList<>();
		    		
		    		int chagehr = 0;
		    		String dates = dateFormat.format(date);
		    		//String dates = "2015-12-01";
		        	for(int i=0;i<24;i++) {
		        		
		        		if(i == 0){
		        			chagehr = 12;
		        		}else{
		        			chagehr++;
		        		}
		        		
		        		
		        		visitorCount = 0;
		        		String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		try {
		        			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){
		    	    				  if(arr[5].equals(value)){
		    	    					  SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		    	    					  String timevisit = jsonArray.getJSONObject(j).get("time_pretty").toString();
		    	    					  String[] timeset = timevisit.split(",");
											String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
											if(timeDiv[1].equals(String.valueOf(chagehr))){
												if(timeDiv[4].equals("am") && i < 12){
													visitorCount = visitorCount + 1;
												}else if(timeDiv[4].equals("pm") && i >= 12){
													visitorCount = visitorCount + 1;
												}
												
												
											}
		    	    				  }
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (JSONException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        		
		        		allVisitor.add(visitorCount);
		        		String valueTime = null;
		        		if(i == 0){
		        			valueTime = "12am";
		        		}
		        		if(i >= 12){
		        			if(i == 12){
		        				valueTime = "12pm";
		        			}else{
		        				int chang = i - 12;
		        				valueTime = chang+"pm";
		        			}
		        			
		        		}else{
		        			if(i != 0){
		        				valueTime = i+"am";
		        			}
		        		}
		        		months.add(valueTime);
		        		
		        		if(i == 0){
		        			chagehr = 0;
		        		}else{
		        			if(chagehr == 12){
		        				chagehr = 0;
		        			}
		        		}
		        	}
		        	
		    	}
		    
		    	Map map = new HashMap();
		    	map.put("allVisitor", allVisitor);
		    	map.put("months", months);
		    	return ok(Json.toJson(map));
		    }
			
			public static Result getStatusList(String value){
		    	
		    	
		    	String params = "&type=actions-list&date=last-30-days&limit=all";
		    	
		    	int countSubrequestmoreinfo = 0;
				int countShowrequestmoreinfoshow = 0;
				
				int countSubscheduletest = 0;
				int countShowscheduletestshow = 0;
				
				int countSubtrade = 0;
				int countShowtrade = 0;
				int countenginesound = 0;
				int countvirtualtour = 0;
				
		    	int countSubemailtofriend = 0;
				int countShowemailtofriend = 0;
				
				Map<String, Integer> map = new HashMap<String, Integer>();
		    	try {
		    		JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    		for(int i=0;i<jsonArray1.length();i++){
		    			String data = jsonArray1.getJSONObject(i).get("action_url").toString();
		    			String dataArr[] = data.split("#");
		    			if(dataArr!=null && dataArr.length>0){
		    				int count = data.split("#").length - 1;
		        			if(count == 1){
		        				if(dataArr[1].equals("requestmoreinfo")){
		        					countSubrequestmoreinfo++;
		        				}
		        				if(dataArr[1].equals("requestmoreinfoshow")){
		        					countShowrequestmoreinfoshow++;
		        				}
		        				
		        				if(dataArr[1].equals("scheduletest")){
		        					countSubscheduletest++;
		        				}
		        				if(dataArr[1].equals("scheduletestshow")){
		        					countShowscheduletestshow++;
		        				}
		        				
		        				if(dataArr[1].equals("emailtofriend")){
		        					countSubemailtofriend++;
		        				}
		        				if(dataArr[1].equals("emailtofriendshow")){
		        					countShowemailtofriend++;
		        				}
		        				
		        				if(dataArr[1].equals("tradeinapp")){
		        					countSubtrade++;
		        				}
		        				if(dataArr[1].equals("tradeinappshow")){
		        					countShowtrade++;
		        				}
		        				
		        			} else if(count == 2){
		        				if(dataArr[2].equals("requestmoreinfo")&&dataArr[1].equals(value)){
		        					countSubrequestmoreinfo++;
		        				}
		        				if(dataArr[2].equals("requestmoreinfoshow")&&dataArr[1].equals(value)){
		        					countShowrequestmoreinfoshow++;
		        				}
		        				
		        				if(dataArr[2].equals("scheduletest")&&dataArr[1].equals(value)){
		        					countSubscheduletest++;
		        				}
		        				if(dataArr[2].equals("scheduletestshow")&&dataArr[1].equals(value)){
		        					countShowscheduletestshow++;
		        				}
		        				
		        				if(dataArr[2].equals("emailtofriend")&&dataArr[1].equals(value)){
		        					countSubemailtofriend++;
		        				}
		        				if(dataArr[2].equals("emailtofriendshow")&&dataArr[1].equals(value)){
		        					countShowemailtofriend++;
		        				}
		        				
		        				if(dataArr[2].equals("tradeinapp")&&dataArr[1].equals(value)){
		        					countSubtrade++;
		        				}
		        				if(dataArr[2].equals("tradeinappshow")&&dataArr[1].equals(value)){
		        					countShowtrade++;
		        				}
		        				if(dataArr[2].equals("enginesound")&&dataArr[1].equals(value)){
		        					countenginesound++;
		        				}
		        				if(dataArr[2].equals("virtualTour")&&dataArr[1].equals(value)){
		        					countvirtualtour++;
		        				}
		        			}
		    			}
		    		}
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	
		    	
		    	List<PriceAlert> pAlert = PriceAlert.getEmailsByVin(value, Long.valueOf(session("USER_LOCATION")));
		    	int followerCount = pAlert.size();
		    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
		    	Map<String, Integer> mapIPAdd = new HashMap<String, Integer>();
		    	
		    	String params1 = "&type=visitors-list&date=last-30-days&limit=all";
		    		int totalTime = 0;
		    		int lagCount = 0;
		    		int newuserCount = 0;
		    		int avgDur = 0;
					JSONArray jsonArray;
					try {
						jsonArray = new JSONArray(callClickAPI(params1)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
						for(int i=0;i<jsonArray.length();i++){
			    			String data = jsonArray.getJSONObject(i).get("landing_page").toString();
			    			String arr[] = data.split("/");
			    			if(arr.length > 5){
			    			  if(arr[5] != null){
			    				  if(arr[5].equals(value)){
			    					  lagCount++;
			    					  
			    				totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(i).get("time_total").toString());
			    				  
			    				  Integer langValue = mapRM.get(jsonArray.getJSONObject(i).get("uid").toString()); 
		  						 if (langValue == null) {
		  						 	mapRM.put(jsonArray.getJSONObject(i).get("uid").toString(), 1);
		  						 }else{
		  							mapRM.put(jsonArray.getJSONObject(i).get("uid").toString(),  1);
		  						 }
		  						 
		  						Integer ipAddessValue = mapIPAdd.get(jsonArray.getJSONObject(i).get("ip_address").toString()); 
		 						 if (ipAddessValue == null) {
		 							mapIPAdd.put(jsonArray.getJSONObject(i).get("ip_address").toString(),Integer.parseInt(jsonArray.getJSONObject(i).get("total_visits").toString()));
		 						 }else{
		 							mapIPAdd.put(jsonArray.getJSONObject(i).get("ip_address").toString(), Integer.parseInt(jsonArray.getJSONObject(i).get("total_visits").toString()));
		 						 }
		  						 
			    				}
			    			  }
			    			}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					for (Map.Entry<String, Integer> entry : mapIPAdd.entrySet())
					{
						if(entry.getValue() == 1){
							newuserCount++;
						}
					}	
		    			
					
		    	if(lagCount != 0){
					avgDur = totalTime/lagCount;	
		    	}
				map.put("newUser", newuserCount);
		    	map.put("users", mapRM.size());
		    	map.put("avgSessDur", avgDur);
				map.put("pageview", lagCount);
		    	map.put("followers", followerCount);
		    	map.put("enginesound", countenginesound);
		    	map.put("virtualtour",countvirtualtour);
		    	map.put("requestmoreinfo", countSubrequestmoreinfo);
		    	map.put("requestmoreinfoshow", countShowrequestmoreinfoshow);
		    	map.put("requestmoreinfoTotal", (countSubrequestmoreinfo+countShowrequestmoreinfoshow));
		    	
		    	map.put("scheduletest", countSubscheduletest);
		    	map.put("scheduletestshow", countShowscheduletestshow);
		    	map.put("scheduletestTotal", (countSubscheduletest+countShowscheduletestshow));
		    	
		    	map.put("tradeinapp", countSubtrade);
		    	map.put("tradeinappshow", countShowtrade);
		    	map.put("tradeinappTotal", (countSubtrade+countShowtrade));
		    	
		    	map.put("emailtofriend", countSubemailtofriend);
		    	map.put("emailtofriendshow", countShowemailtofriend);
		    	map.put("emailtofriendTotal", (countSubemailtofriend+countShowemailtofriend));
		    	return ok(Json.toJson(map));
		    }
			
		    public static Result getDemographics(String value){
		    	Map map = new HashMap(2);
				Map<String, Integer> mapRM = new HashMap<String, Integer>();
				Map<String, Integer> mapWebBro = new HashMap<String, Integer>();
				Map<String, Integer> maplocation = new HashMap<String, Integer>();
				Map<String, Integer> mapoperatingSystem = new HashMap<String, Integer>();
				Map<String, Integer> mapSreenResoluation = new HashMap<String, Integer>();
				
		    	String params = "&type=visitors-list&date=last-30-days&limit=all";
		    	try {
		    		
		    		int lagCount = 1;
					JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArray.length();i++){
		    			String data = jsonArray.getJSONObject(i).get("landing_page").toString();
		    			String arr[] = data.split("/");
		    			if(arr.length > 5){
		    			  if(arr[5] != null){
		    				  if(arr[5].equals(value)){
		    					  
		    					  Integer langValue = mapRM.get(jsonArray.getJSONObject(i).get("language").toString()); 
		    						if (langValue == null) {
		    							mapRM.put(jsonArray.getJSONObject(i).get("language").toString(), lagCount);
		    						}else{
		    							mapRM.put(jsonArray.getJSONObject(i).get("language").toString(), mapRM.get(jsonArray.getJSONObject(i).get("language").toString()) + 1);
		    						}
		    						
		    						 Integer mapWebBroValue = mapWebBro.get(jsonArray.getJSONObject(i).get("web_browser").toString()); 
		     						if (mapWebBroValue == null) {
		     							mapWebBro.put(jsonArray.getJSONObject(i).get("web_browser").toString(), lagCount);
		     						}else{
		     							mapWebBro.put(jsonArray.getJSONObject(i).get("web_browser").toString(), mapWebBro.get(jsonArray.getJSONObject(i).get("web_browser").toString()) + 1);
		     						}
		     						
		     						Integer maplocationValue = maplocation.get(jsonArray.getJSONObject(i).get("geolocation").toString()); 
		     						if (maplocationValue == null) {
		     							maplocation.put(jsonArray.getJSONObject(i).get("geolocation").toString(), lagCount);
		     						}else{
		     							maplocation.put(jsonArray.getJSONObject(i).get("geolocation").toString(), maplocation.get(jsonArray.getJSONObject(i).get("geolocation").toString()) + 1);
		     						}
		     						
		     						Integer mapoperatingSystemValue = mapoperatingSystem.get(jsonArray.getJSONObject(i).get("operating_system").toString()); 
		     						if (mapoperatingSystemValue == null) {
		     							mapoperatingSystem.put(jsonArray.getJSONObject(i).get("operating_system").toString(), lagCount);
		     						}else{
		     							mapoperatingSystem.put(jsonArray.getJSONObject(i).get("operating_system").toString(), mapoperatingSystem.get(jsonArray.getJSONObject(i).get("operating_system").toString()) + 1);
		     						}
		     						
		     						Integer mapSreenResoluationValue = mapSreenResoluation.get(jsonArray.getJSONObject(i).get("screen_resolution").toString()); 
		     						if (mapSreenResoluationValue == null) {
		     							mapSreenResoluation.put(jsonArray.getJSONObject(i).get("screen_resolution").toString(), lagCount);
		     						}else{
		     							mapSreenResoluation.put(jsonArray.getJSONObject(i).get("screen_resolution").toString(), mapSreenResoluation.get(jsonArray.getJSONObject(i).get("screen_resolution").toString()) + 1);
		     						}
		    						
		     						
		    				  }
		    			  }
		    			}
		    			
					}	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		    	map.put("language", mapRM);
		    	map.put("location", maplocation);
		    	map.put("webBrowser", mapWebBro);
		    	map.put("operatingSystem", mapoperatingSystem);
		    	map.put("screenResoluation", mapSreenResoluation);
		    	
		    	
		    	return ok(Json.toJson(map));
		    }
		    
		    public static Result getSessionDaysUserStats(String value,String lasttime,String analyType) {
		    	
		    	List<Integer> allVisitor = new ArrayList<Integer>(30);
		    	List<String> months = new ArrayList<String>(30);
		    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
		    	int visitorCount = 0;
		    	int totalTime = 0;
		    	int avgDur = 0;
		    	Calendar c = Calendar.getInstance();
		    	
		    	if(lasttime.equals("month")){
		    		
		        	String[] monthsArr = new String[30]; 
		        	c.add(Calendar.DAY_OF_YEAR, -29);
		        	
		        	for(int i=0;i<30;i++) {
		        		mapRM.clear();
		        		visitorCount = 0;
		        		totalTime = 0;
		        		avgDur = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		if(days.length() < 2){
		        			days = 0+days;
		        		}
		        		if(month.length() < 2){
		        			month = 0+month;
		        		}
		        		String dates = year + month + days;
		        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
		        			
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        		    		 if(count == 2){
		        		    			    if(analyType.equals("enginesound")){
		        		    			    	if(dataArr[2].equals("enginesound") && dataArr[1].equals(value)){
		        		    			    		visitorCount++;
		        		    			    	}
		        		    			    }else if(analyType.equals("virtualtour")){
		        		    			    
		        		    			    	if(dataArr[2].equals("virtualTour") && dataArr[1].equals(value)){
		        		    			    		visitorCount++;
		        		    			    	}
		        		    			    }
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        			
		        		}else{
		        			String params = "&date="+dates+"&type=visitors-list&limit=all";
		        			try {
		        				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray.length();j++){
		        					String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		        					String arr[] = data.split("/");
		        					if(arr.length > 5){
		        						if(arr[5] != null){
		        							if(arr[5].equals(value)){
		        								if(analyType.equals("user")){
		        									Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
		        									if (langValue == null) {
		        										visitorCount = visitorCount + 1;
		        										mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
		        									}
		        								}else if(analyType.equals("pageview")){
		        									visitorCount = visitorCount + 1;
		        								}else if(analyType.equals("sessionDuration")){
		        									visitorCount = visitorCount + 1;
		        									totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
		        								}
		        							}
		        						}
		        					}
		        				}	
		        			} catch (JSONException e) {
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
		        		}
		        		
		        		if(analyType.equals("sessionDuration")){
		        			if(visitorCount != 0 || totalTime != 0){
		            			avgDur = totalTime/visitorCount;	
		            		}
		        			
		        			allVisitor.add(avgDur);
		        		}else{
		        			allVisitor.add(visitorCount);
		        		}
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("week")){
		    		
		        	String[] monthsArr = new String[7]; 
		        	c.add(Calendar.DAY_OF_YEAR, -6);
		        	
		        	for(int i=0;i<7;i++) {
		        		mapRM.clear();
		        		visitorCount = 0;
		        		totalTime = 0;
		        		avgDur = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		if(days.length() < 2){
		        			days = 0+days;
		        		}
		        		if(month.length() < 2){
		        			month = 0+month;
		        		}
		        		String dates = year + month + days;
		            	
		        		
		        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
		        			
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        		    		 if(count == 2){
		        		    			 if(analyType.equals("enginesound")){
		     		    			    	if(dataArr[2].equals("enginesound") && dataArr[1].equals(value)){
		     		    			    		visitorCount++;
		     		    			    	}
		     		    			    }else if(analyType.equals("virtualtour")){
		     		    			    
		     		    			    	if(dataArr[2].equals("virtualTour") && dataArr[1].equals(value)){
		     		    			    		visitorCount++;
		     		    			    	}
		     		    			    }
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        			
		        		}else{
		        		  String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		  try {
		    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){
		    	    				  if(arr[5].equals(value)){
		    	    					  
		    	    					  if(analyType.equals("user")){
		    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
		    	    						  if (langValue == null) {
		    	    							  visitorCount = visitorCount + 1;
		    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
		    	    						  	}
		    	    					  	}else if(analyType.equals("pageview")){
		    	    					  		visitorCount = visitorCount + 1;
		    	    					  	}else if(analyType.equals("sessionDuration")){
		    	    					  		visitorCount = visitorCount + 1;
		    	    					  		totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
		    	    					  	}
		    	    				  }
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (JSONException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        	}
		        		
		        		if(analyType.equals("sessionDuration")){
		        			if(visitorCount != 0){
		            			avgDur = totalTime/visitorCount;	
		            		}
		        			
		        			allVisitor.add(avgDur);
		        		}else{
		        			allVisitor.add(visitorCount);
		        		}
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("day")){
		    		
		    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    		Date date = new Date();
		    		List<String> monthsArr = new ArrayList<>();
		    		
		    		int chagehr = 0;
		    		totalTime = 0;
		    		avgDur = 0;
		    		
		    		String dates = dateFormat.format(date);
		        	for(int i=0;i<24;i++) {
		        		mapRM.clear();
		        		if(i == 0){
		        			chagehr = 12;
		        		}else{
		        			chagehr++;
		        		}
		        		
		        		visitorCount = 0;
		        		
		        		
		        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
		        			
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			//String params = "&date=2015-12-09&type=actions-list&limit=all";
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        		    		    if(count == 2){
		        		    		    	
		        		    		    	 if(analyType.equals("enginesound")){
			        		    		    	if(dataArr[2].equals("enginesound") && dataArr[1].equals(value)){
			        		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
			          	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
			          	    					    String[] timeset = timevisit.split(",");
			      									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
			      									if(i >= 12 && timeDiv[4].equals("pm")){
			      										if(timeDiv[1].equals(String.valueOf(chagehr))){
				      										visitorCount++;
				      									}
			            		    		    	}else if(timeDiv[4].equals("am")){
			            		    		    		if(timeDiv[1].equals(String.valueOf(chagehr))){
				      										visitorCount++;
				      									}
			            		    		    	}
			      									
			      									
			  	        		    			}
		        		    		    	 }else if(analyType.equals("virtualtour")){
			        		    		    	if(dataArr[2].equals("virtualTour") && dataArr[1].equals(value)){
			        		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
			          	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
			          	    					    String[] timeset = timevisit.split(",");
			      									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
			      									if(i >= 12 && timeDiv[4].equals("pm")){
			      										if(timeDiv[1].equals(String.valueOf(chagehr))){
				      										visitorCount++;
				      									}
			            		    		    	}else if(i < 12 && timeDiv[4].equals("am")){
			            		    		    		if(timeDiv[1].equals(String.valueOf(chagehr))){
				      										visitorCount++;
				      									}
			            		    		    	}
			        		    		    	}
		        		    		    	 }
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        			
		        		}else{
		        		
		        		String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		try {
		        			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){
		    	    				  if(arr[5].equals(value)){
		    	    					  SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		    	    					  String timevisit = jsonArray.getJSONObject(j).get("time_pretty").toString();
		    	    					  String[] timeset = timevisit.split(",");
											String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
											
											if(i >= 12 && timeDiv[4].equals("pm")){
											if(timeDiv[1].equals(String.valueOf(chagehr))){
												if(analyType.equals("user")){
				    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
				    	    						  if (langValue == null) {
				    	    							  visitorCount = visitorCount + 1;
				    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
				    	    						  	}
				    	    					  	}else if(analyType.equals("pageview")){
				    	    					  		visitorCount = visitorCount + 1;
				    	    					  	}else if(analyType.equals("sessionDuration")){
				    	    					  		visitorCount = visitorCount + 1;
				    	    					  		totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
				    	    					  	}
												
											}
											}else if(i < 12 &&timeDiv[4].equals("am")){
												if(timeDiv[1].equals(String.valueOf(chagehr))){
													if(analyType.equals("user")){
					    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
					    	    						  if (langValue == null) {
					    	    							  visitorCount = visitorCount + 1;
					    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
					    	    						  	}
					    	    					  	}else if(analyType.equals("pageview")){
					    	    					  		visitorCount = visitorCount + 1;
					    	    					  	}else if(analyType.equals("sessionDuration")){
					    	    					  		visitorCount = visitorCount + 1;
					    	    					  		totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
					    	    					  	}
													
												}
											  }
											}
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (JSONException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        	}
		        		
		        		if(analyType.equals("sessionDuration")){
		        			if(visitorCount != 0){
		            			avgDur = totalTime/visitorCount;	
		            		}
		        			
		        			allVisitor.add(avgDur);
		        		}else{
		        			allVisitor.add(visitorCount);
		        		}
		        		
		        		String valueTime = null;
		        		if(i == 0){
		        			valueTime = "12am";
		        		}
		        		if(i >= 12){
		        			if(i == 12){
		        				valueTime = "12pm";
		        			}else{
		        				int chang = i - 12;
		        				valueTime = chang+"pm";
		        			}
		        			
		        		}else{
		        			if(i != 0){
		        				valueTime = i+"am";
		        			}
		        		}
		        		months.add(valueTime);
		        		
		        		if(i == 0){
		        			chagehr = 0;
		        		}else{
		        			if(chagehr == 12){
		        				chagehr = 0;
		        			}
		        		}
		        	}
		        	
		    	}
		    
		    	Map map = new HashMap();
		    	map.put("allVisitor", allVisitor);
		    	map.put("months", months);
		    	return ok(Json.toJson(map));
		    }
		    
		    public static Result getMailDaysUserStats(String value,String lasttime,String mailType) {
		    	
		    	List<Integer> allVisitor = new ArrayList<Integer>(30);
		    	List<String> months = new ArrayList<String>(30);
		    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
		    	int visitorCount = 0;
		    	int totalTime = 0;
		    	int avgDur = 0;
		    	Calendar c = Calendar.getInstance();
		    	
		    	if(lasttime.equals("month")){
		    		
		        	String[] monthsArr = new String[30]; 
		        	c.add(Calendar.DAY_OF_YEAR, -29);
		        	
		        	for(int i=0;i<30;i++) {
		        		mapRM.clear();
		        		visitorCount = 0;
		        		totalTime = 0;
		        		avgDur = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		if(days.length() < 2){
		        			days = 0+days;
		        		}
		        		if(month.length() < 2){
		        			month = 0+month;
		        		}
		        		String dates = year + month + days;
		        		
		        			
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        					int count = data.split("#").length - 1;
		        		    		 if(count == 2){
		        		    			 if(mailType.equals("requestMore")){
		        		    				 if(dataArr[2].equals("requestmoreinfo")&&dataArr[1].equals(value)){
		         		    					visitorCount++;
		         		    				 }
		        		    			 }else if(mailType.equals("scheduleTest")){
		        		    				 if(dataArr[2].equals("scheduletest")&&dataArr[1].equals(value)){
		          		    					visitorCount++;
		          		    				 }
		        		    			 }else if(mailType.equals("tradeInApp")){
		        		    				 if(dataArr[2].equals("tradeinapp")&&dataArr[1].equals(value)){
		           		    					visitorCount++;
		           		    				 }
		         		    			 }else if(mailType.equals("emailToFrd")){
		        		    				 if(dataArr[2].equals("emailtofriend")&&dataArr[1].equals(value)){
		            		    					visitorCount++;
		            		    				 }
		          		    			 }
		        		    			 
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        		
		        			allVisitor.add(visitorCount);
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("week")){
		    		
		        	String[] monthsArr = new String[7]; 
		        	c.add(Calendar.DAY_OF_YEAR, -6);
		        	
		        	for(int i=0;i<7;i++) {
		        		mapRM.clear();
		        		visitorCount = 0;
		        		totalTime = 0;
		        		avgDur = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		if(days.length() < 2){
		        			days = 0+days;
		        		}
		        		if(month.length() < 2){
		        			month = 0+month;
		        		}
		        		String dates = year + month + days;
		        		
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        						if(count == 2){
		           		    			 if(mailType.equals("requestMore")){
		           		    				 if(dataArr[2].equals("requestmoreinfo")&&dataArr[1].equals(value)){
		            		    					visitorCount++;
		            		    				 }
		           		    			 }else if(mailType.equals("scheduleTest")){
		           		    				 if(dataArr[2].equals("scheduletest")&&dataArr[1].equals(value)){
		             		    					visitorCount++;
		             		    				 }
		           		    			 }else if(mailType.equals("tradeInApp")){
		           		    				 if(dataArr[2].equals("tradeinapp")&&dataArr[1].equals(value)){
		              		    					visitorCount++;
		              		    				 }
		            		    			 }else if(mailType.equals("emailToFrd")){
		           		    				 if(dataArr[2].equals("emailtofriend")&&dataArr[1].equals(value)){
		               		    					visitorCount++;
		               		    				 }
		             		    			 }
		           		    			 
		           		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        		
		        			allVisitor.add(visitorCount);
		        		
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("day")){
		    		
		    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    		Date date = new Date();
		    		List<String> monthsArr = new ArrayList<>();
		    		
		    		int chagehr = 0;
		    		totalTime = 0;
		    		avgDur = 0;
		    		
		    		String dates = dateFormat.format(date);
		        	for(int i=0;i<24;i++) {
		        		mapRM.clear();
		        		if(i == 0){
		        			chagehr = 12;
		        		}else{
		        			chagehr++;
		        		}
		        		
		        		visitorCount = 0;
		        		
		        		
		        		//if(mailType.equals("enginesound")){
		        			
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			//String params = "&date=2015-12-04&type=actions-list&limit=all";
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        		    		    if(count == 2){
		        		    		    	if(mailType.equals("requestMore")){
		        		    		    	 if(dataArr[2].equals("requestmoreinfo")&&dataArr[1].equals(value)){
		        		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		          	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		          	    					    String[] timeset = timevisit.split(",");
		      									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		      									if(timeDiv[1].equals(String.valueOf(chagehr))){
		      										if(timeDiv[4].equals("am") && i < 12){
		    											visitorCount++;
		    										}else if(timeDiv[4].equals("pm") && i >= 12){
		    											visitorCount++;
		    										}
		      									}
		  	        		    			 }
		        		    		       }else if(mailType.equals("scheduleTest")){
		        		    		    	   if(dataArr[2].equals("scheduletest")&&dataArr[1].equals(value)){
		           		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		             	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		             	    					    String[] timeset = timevisit.split(",");
		         									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		         									if(timeDiv[1].equals(String.valueOf(chagehr))){
		         										if(timeDiv[4].equals("am") && i < 12){
		        											visitorCount++;
		        										}else if(timeDiv[4].equals("pm") && i >= 12){
		        											visitorCount++;
		        										}
		         									}
		     	        		    			 }
		        		    		       }else if(mailType.equals("tradeInApp")){
		        		    		    	   if(dataArr[2].equals("tradeinapp")&&dataArr[1].equals(value)){
		              		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		                	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		                	    					    String[] timeset = timevisit.split(",");
		            									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		            									if(timeDiv[1].equals(String.valueOf(chagehr))){
		            										if(timeDiv[4].equals("am") && i < 12){
		            											visitorCount++;
		            										}else if(timeDiv[4].equals("pm") && i >= 12){
		            											visitorCount++;
		            										}
		            									}
		        	        		    			 }
		        		    		       }else if(mailType.equals("emailToFrd")){
		        		    		    	   if(dataArr[2].equals("emailtofriend")&&dataArr[1].equals(value)){
		             		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		               	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		               	    					    String[] timeset = timevisit.split(",");
		           									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		           									if(timeDiv[1].equals(String.valueOf(chagehr))){
		           										if(timeDiv[4].equals("am") && i < 12){
		        											visitorCount++;
		        										}else if(timeDiv[4].equals("pm") && i >= 12){
		        											visitorCount++;
		        										}
		           									}
		       	        		    			 }
		        		    		       }
		      									
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        		
		        		if(mailType.equals("sessionDuration")){
		        			if(visitorCount != 0){
		            			avgDur = totalTime/visitorCount;	
		            		}
		        			
		        			allVisitor.add(avgDur);
		        		}else{
		        			allVisitor.add(visitorCount);
		        		}
		        		
		        		String valueTime = null;
		        		if(i == 0){
		        			valueTime = "12am";
		        		}
		        		if(i >= 12){
		        			if(i == 12){
		        				valueTime = "12pm";
		        			}else{
		        				int chang = i - 12;
		        				valueTime = chang+"pm";
		        			}
		        			
		        		}else{
		        			if(i != 0){
		        				valueTime = i+"am";
		        			}
		        		}
		        		months.add(valueTime);
		        		
		        		if(i == 0){
		        			chagehr = 0;
		        		}else{
		        			if(chagehr == 12){
		        				chagehr = 0;
		        			}
		        		}
		        	}
		        	
		    	}
		    
		    	Map map = new HashMap();
		    	map.put("allVisitor", allVisitor);
		    	map.put("months", months);
		    	return ok(Json.toJson(map));
		    }
		    
		    public static Result getHeatMapList(Integer value){
		    	int year = Calendar.getInstance().get(Calendar.YEAR);
		    	String params = null;
		    	
		    	if(value == 30){
		    		params = "&type=pages&heatmap_url=1&date=last-30-days&limit=all";
		    	}else if(value == 7){
		    		params = "&type=pages&heatmap_url=1&date=last-7-days&limit=all";
		    	}else if(value == 1){
		    		params = "&type=pages&heatmap_url=1&date="+year+"&limit=all";
		    	}
		    	return ok(Json.parse(callClickAPI(params)));
		    }
		    
		    public static Result getAllVehicleSession(String lasttime) {
		    	List<Integer> allVisitor = new ArrayList<Integer>(30);
		    	List<String> months = new ArrayList<String>(30);
		    	int visitorCount = 0;
		    	Calendar c = Calendar.getInstance();
		    	
		    	if(lasttime.equals("month")){
		    		
		        	String[] monthsArr = new String[30]; 
		        	c.add(Calendar.DAY_OF_YEAR, -29);
		        	
		        	for(int i=0;i<30;i++) {
		        		visitorCount = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		String dates = year + month + days;
		            	
		        		String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		try {
		    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){
		    	    				 // if(arr[5].equals(value)){
		    	    					  visitorCount = visitorCount + 1;
		    	    				 // }
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (JSONException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        		
		        		allVisitor.add(visitorCount);
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("week")){
		    		
		    		
		        	String[] monthsArr = new String[7]; 
		        	c.add(Calendar.DAY_OF_YEAR, -6);
		        	
		        	for(int i=0;i<7;i++) {
		        		visitorCount = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		String dates = year + month + days;
		            	
		        		String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		try {
		    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){
		    	    				  //if(arr[5].equals(value)){
		    	    					  visitorCount = visitorCount + 1;
		    	    				  //}
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (JSONException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        		
		        		allVisitor.add(visitorCount);
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("day")){
		    		
		    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    		Date date = new Date();
		    		List<String> monthsArr = new ArrayList<>();
		    		
		    		int chagehr = 0;
		    		String dates = dateFormat.format(date);
		    		//String dates = "2015-12-01";
		        	for(int i=0;i<24;i++) {
		        		
		        		if(i == 0){
		        			chagehr = 12;
		        		}else{
		        			chagehr++;
		        		}
		        		
		        		
		        		visitorCount = 0;
		        		String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		try {
		        			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){
		    	    				//  if(arr[5].equals(value)){
		    	    					  SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		    	    					  String timevisit = jsonArray.getJSONObject(j).get("time_pretty").toString();
		    	    					  String[] timeset = timevisit.split(",");
											String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
											if(timeDiv[1].equals(String.valueOf(chagehr))){
												if(timeDiv[4].equals("am") && i < 12){
													visitorCount = visitorCount + 1;
												}else if(timeDiv[4].equals("pm") && i >= 12){
													visitorCount = visitorCount + 1;
												}
											}
		    	    				 // }
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (Exception e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        		
		        		allVisitor.add(visitorCount);
		        		String valueTime = null;
		        		if(i == 0){
		        			valueTime = "12am";
		        		}
		        		if(i >= 12){
		        			if(i == 12){
		        				valueTime = "12pm";
		        			}else{
		        				int chang = i - 12;
		        				valueTime = chang+"pm";
		        			}
		        			
		        		}else{
		        			if(i != 0){
		        				valueTime = i+"am";
		        			}
		        		}
		        		months.add(valueTime);
		        		
		        		if(i == 0){
		        			chagehr = 0;
		        		}else{
		        			if(chagehr == 12){
		        				chagehr = 0;
		        			}
		        		}
		        	}
		        	
		    	}
		    
		    	Map map = new HashMap();
		    	map.put("allVisitor", allVisitor);
		    	map.put("months", months);
		    	return ok(Json.toJson(map));
		    }
		    
		    public static Result getAllvehicleStatusList(){

		    	String params = "&type=actions-list&date=last-30-days&limit=all";
		    	
		    	int countSubrequestmoreinfo = 0;
				int countShowrequestmoreinfoshow = 0;
				
				int countSubscheduletest = 0;
				int countShowscheduletestshow = 0;
				
				int countSubtrade = 0;
				int countShowtrade = 0;
				int countenginesound = 0;
				int countvirtualtour = 0;
				
		    	int countSubemailtofriend = 0;
				int countShowemailtofriend = 0;
				
				Map<String, Integer> map = new HashMap<String, Integer>();
		    	try {
		    		JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    		for(int i=0;i<jsonArray1.length();i++){
		    			String data = jsonArray1.getJSONObject(i).get("action_url").toString();
		    			String dataArr[] = data.split("#");
		    			if(dataArr!=null && dataArr.length>0){
		    				int count = data.split("#").length - 1;
		        		 if(count == 2){
		        				if(dataArr[2].equals("requestmoreinfo")){
		        					countSubrequestmoreinfo++;
		        				}
		        				if(dataArr[2].equals("requestmoreinfoshow")){
		        					countShowrequestmoreinfoshow++;
		        				}
		        				
		        				if(dataArr[2].equals("scheduletest")){
		        					countSubscheduletest++;
		        				}
		        				if(dataArr[2].equals("scheduletestshow")){
		        					countShowscheduletestshow++;
		        				}
		        				
		        				if(dataArr[2].equals("emailtofriend")){
		        					countSubemailtofriend++;
		        				}
		        				
		        				if(dataArr[2].equals("emailtofriendshow")){
		        					countShowemailtofriend++;
		        				}
		        				
		        				if(dataArr[2].equals("tradeinapp")){
		        					countSubtrade++;
		        				}
		        				if(dataArr[2].equals("tradeinappshow")){
		        					countShowtrade++;
		        				}
		        				if(dataArr[2].equals("enginesound")){
		        					countenginesound++;
		        				}
		        				if(dataArr[2].equals("virtualTour")){
		        					countvirtualtour++;
		        				}
		        			}
		    			}
		    		}
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	
		    	//AuthUser user = AuthUser.findById(Integer.getInteger(session("USER_KEY")));
		    	AuthUser user = getLocalUser();
		    	List<PriceAlert> pAlert = PriceAlert.getByLocation(Long.valueOf(session("USER_LOCATION")));
		    	int followerCount = pAlert.size();
		    	
		    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
		    	Map<String, Integer> mapIPAdd = new HashMap<String, Integer>();
		    	
		    	String params1 = "&type=visitors-list&date=last-30-days&limit=all";
		    		int totalTime = 0;
		    		int lagCount = 0;
		    		int newuserCount = 0;
		    		int avgDur = 0;
					JSONArray jsonArray;
					try {
						jsonArray = new JSONArray(callClickAPI(params1)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
						for(int i=0;i<jsonArray.length();i++){
			    			String data = jsonArray.getJSONObject(i).get("landing_page").toString();
			    			String arr[] = data.split("/");
			    			if(arr.length > 5){
			    			  if(arr[5] != null){
			    				//  if(arr[5].equals(value)){
			    					  lagCount++;
			    					  
			    				totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(i).get("time_total").toString());
			    				  
			    				  Integer langValue = mapRM.get(jsonArray.getJSONObject(i).get("uid").toString()); 
		  						 if (langValue == null) {
		  						 	mapRM.put(jsonArray.getJSONObject(i).get("uid").toString(), 1);
		  						 }else{
		  							mapRM.put(jsonArray.getJSONObject(i).get("uid").toString(),  1);
		  						 }
		  						 
		  						Integer ipAddessValue = mapIPAdd.get(jsonArray.getJSONObject(i).get("ip_address").toString()); 
		 						 if (ipAddessValue == null) {
		 							mapIPAdd.put(jsonArray.getJSONObject(i).get("ip_address").toString(),Integer.parseInt(jsonArray.getJSONObject(i).get("total_visits").toString()));
		 						 }else{
		 							mapIPAdd.put(jsonArray.getJSONObject(i).get("ip_address").toString(), Integer.parseInt(jsonArray.getJSONObject(i).get("total_visits").toString()));
		 						 }
		  						 
			    				//}
			    			  }
			    			}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					for (Map.Entry<String, Integer> entry : mapIPAdd.entrySet())
					{
						if(entry.getValue() == 1){
							newuserCount++;
						}
					}	
		    			
					
		    	if(lagCount != 0){
					avgDur = totalTime/lagCount;	
		    	}
				map.put("newUser", newuserCount);
		    	map.put("users", mapRM.size());
		    	map.put("avgSessDur", avgDur);
				map.put("pageview", lagCount);
		    	map.put("followers", followerCount);
		    	map.put("enginesound", countenginesound);
		    	map.put("virtualtour", countvirtualtour);
		    	map.put("requestmoreinfo", countSubrequestmoreinfo);
		    	map.put("requestmoreinfoshow", countShowrequestmoreinfoshow);
		    	map.put("requestmoreinfoTotal", (countSubrequestmoreinfo+countShowrequestmoreinfoshow));
		    	
		    	map.put("scheduletest", countSubscheduletest);
		    	map.put("scheduletestshow", countShowscheduletestshow);
		    	map.put("scheduletestTotal", (countSubscheduletest+countShowscheduletestshow));
		    	
		    	map.put("tradeinapp", countSubtrade);
		    	map.put("tradeinappshow", countShowtrade);
		    	map.put("tradeinappTotal", (countSubtrade+countShowtrade));
		    	
		    	map.put("emailtofriend", countSubemailtofriend);
		    	map.put("emailtofriendshow", countShowemailtofriend);
		    	map.put("emailtofriendTotal", (countSubemailtofriend+countShowemailtofriend));
		    	return ok(Json.toJson(map));
		    }
		    
		    public static Result getAllVehicleDemographics(){
		    	Map map = new HashMap(2);
				Map<String, Integer> mapRM = new HashMap<String, Integer>();
				Map<String, Integer> mapWebBro = new HashMap<String, Integer>();
				Map<String, Integer> maplocation = new HashMap<String, Integer>();
				Map<String, Integer> mapoperatingSystem = new HashMap<String, Integer>();
				Map<String, Integer> mapSreenResoluation = new HashMap<String, Integer>();
				
		    	String params = "&type=visitors-list&date=last-30-days&limit=all";
		    	try {
		    		
		    		int lagCount = 1;
					JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
					for(int i=0;i<jsonArray.length();i++){
		    			String data = jsonArray.getJSONObject(i).get("landing_page").toString();
		    			String arr[] = data.split("/");
		    			if(arr.length > 5){
		    			  if(arr[5] != null){
		    				//  if(arr[5].equals(value)){
		    					  
		    					  Integer langValue = mapRM.get(jsonArray.getJSONObject(i).get("language").toString()); 
		    						if (langValue == null) {
		    							mapRM.put(jsonArray.getJSONObject(i).get("language").toString(), lagCount);
		    						}else{
		    							mapRM.put(jsonArray.getJSONObject(i).get("language").toString(), mapRM.get(jsonArray.getJSONObject(i).get("language").toString()) + 1);
		    						}
		    						
		    						 Integer mapWebBroValue = mapWebBro.get(jsonArray.getJSONObject(i).get("web_browser").toString()); 
		     						if (mapWebBroValue == null) {
		     							mapWebBro.put(jsonArray.getJSONObject(i).get("web_browser").toString(), lagCount);
		     						}else{
		     							mapWebBro.put(jsonArray.getJSONObject(i).get("web_browser").toString(), mapWebBro.get(jsonArray.getJSONObject(i).get("web_browser").toString()) + 1);
		     						}
		     						
		     						Integer maplocationValue = maplocation.get(jsonArray.getJSONObject(i).get("geolocation").toString()); 
		     						if (maplocationValue == null) {
		     							maplocation.put(jsonArray.getJSONObject(i).get("geolocation").toString(), lagCount);
		     						}else{
		     							maplocation.put(jsonArray.getJSONObject(i).get("geolocation").toString(), maplocation.get(jsonArray.getJSONObject(i).get("geolocation").toString()) + 1);
		     						}
		     						
		     						Integer mapoperatingSystemValue = mapoperatingSystem.get(jsonArray.getJSONObject(i).get("operating_system").toString()); 
		     						if (mapoperatingSystemValue == null) {
		     							mapoperatingSystem.put(jsonArray.getJSONObject(i).get("operating_system").toString(), lagCount);
		     						}else{
		     							mapoperatingSystem.put(jsonArray.getJSONObject(i).get("operating_system").toString(), mapoperatingSystem.get(jsonArray.getJSONObject(i).get("operating_system").toString()) + 1);
		     						}
		     						
		     						Integer mapSreenResoluationValue = mapSreenResoluation.get(jsonArray.getJSONObject(i).get("screen_resolution").toString()); 
		     						if (mapSreenResoluationValue == null) {
		     							mapSreenResoluation.put(jsonArray.getJSONObject(i).get("screen_resolution").toString(), lagCount);
		     						}else{
		     							mapSreenResoluation.put(jsonArray.getJSONObject(i).get("screen_resolution").toString(), mapSreenResoluation.get(jsonArray.getJSONObject(i).get("screen_resolution").toString()) + 1);
		     						}
		    						
		     						
		    				 // }
		    			  }
		    			}
		    			
					}	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		    	map.put("language", mapRM);
		    	map.put("location", maplocation);
		    	map.put("webBrowser", mapWebBro);
		    	map.put("operatingSystem", mapoperatingSystem);
		    	map.put("screenResoluation", mapSreenResoluation);
		    	
		    	
		    	return ok(Json.toJson(map));
		    }
		    
		    public static Result getSessionDaysAllStats(String lasttime,String analyType) {
		    	
		    	List<Integer> allVisitor = new ArrayList<Integer>(30);
		    	List<String> months = new ArrayList<String>(30);
		    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
		    	int visitorCount = 0;
		    	int totalTime = 0;
		    	int avgDur = 0;
		    	Calendar c = Calendar.getInstance();
		    	
		    	if(lasttime.equals("month")){
		    		
		        	String[] monthsArr = new String[30]; 
		        	c.add(Calendar.DAY_OF_YEAR, -29);
		        	
		        	for(int i=0;i<30;i++) {
		        		mapRM.clear();
		        		visitorCount = 0;
		        		totalTime = 0;
		        		avgDur = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		if(days.length() < 2){
		        			days = 0+days;
		        		}
		        		if(month.length() < 2){
		        			month = 0+month;
		        		}
		        		String dates = year + month + days;
		        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        		    		 if(count == 2){
		        		    			 if(analyType.equals("enginesound")){
		        		    				if(dataArr[2].equals("enginesound")){
		        		    					visitorCount++;
		        		    				}
		        		    			 }else if(analyType.equals("virtualtour")){
		        		    				 if(dataArr[2].equals("virtualTour")){
		         		    					visitorCount++;
		         		    				}
		        		    			 }
		        		    				
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        			
		        		}else{
		        			String params = "&date="+dates+"&type=visitors-list&limit=all";
		        			try {
		        				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray.length();j++){
		        					String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		        					String arr[] = data.split("/");
		        					if(arr.length > 5){
		        						if(arr[5] != null){
		        								if(analyType.equals("user")){
		        									Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
		        									if (langValue == null) {
		        										visitorCount = visitorCount + 1;
		        										mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
		        									}
		        								}else if(analyType.equals("pageview")){
		        									visitorCount = visitorCount + 1;
		        								}else if(analyType.equals("sessionDuration")){
		        									visitorCount = visitorCount + 1;
		        									totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
		        								}
		        						}
		        					}
		        				}	
		        			} catch (JSONException e) {
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
		        		}
		        		
		        		if(analyType.equals("sessionDuration")){
		        			if(visitorCount != 0 || totalTime != 0){
		            			avgDur = totalTime/visitorCount;	
		            		}
		        			
		        			allVisitor.add(avgDur);
		        		}else{
		        			allVisitor.add(visitorCount);
		        		}
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("week")){
		    		
		        	String[] monthsArr = new String[7]; 
		        	c.add(Calendar.DAY_OF_YEAR, -6);
		        	
		        	for(int i=0;i<7;i++) {
		        		mapRM.clear();
		        		visitorCount = 0;
		        		totalTime = 0;
		        		avgDur = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		if(days.length() < 2){
		        			days = 0+days;
		        		}
		        		if(month.length() < 2){
		        			month = 0+month;
		        		}
		        		String dates = year + month + days;
		            	
		        		
		        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
		        			
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        		    		 if(count == 2){
		        		    			 if(analyType.equals("enginesound")){
		         		    				if(dataArr[2].equals("enginesound")){
		         		    					visitorCount++;
		         		    				}
		         		    			 }else if(analyType.equals("virtualtour")){
		         		    				 if(dataArr[2].equals("virtualTour")){
		          		    					visitorCount++;
		          		    				}
		         		    			 }
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        			
		        		}else{
		        		  String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		  try {
		    				JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){
		    	    					  
		    	    					  if(analyType.equals("user")){
		    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
		    	    						  if (langValue == null) {
		    	    							  visitorCount = visitorCount + 1;
		    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
		    	    						  	}
		    	    					  	}else if(analyType.equals("pageview")){
		    	    					  		visitorCount = visitorCount + 1;
		    	    					  	}else if(analyType.equals("sessionDuration")){
		    	    					  		visitorCount = visitorCount + 1;
		    	    					  		totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
		    	    					  	}
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (JSONException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        	}
		        		
		        		if(analyType.equals("sessionDuration")){
		        			if(visitorCount != 0){
		            			avgDur = totalTime/visitorCount;	
		            		}
		        			
		        			allVisitor.add(avgDur);
		        		}else{
		        			allVisitor.add(visitorCount);
		        		}
		        		
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("day")){
		    		
		    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    		Date date = new Date();
		    		List<String> monthsArr = new ArrayList<>();
		    		
		    		int chagehr = 0;
		    		totalTime = 0;
		    		avgDur = 0;
		    		
		    		String dates = dateFormat.format(date);
		        	for(int i=0;i<24;i++) {
		        		mapRM.clear();
		        		if(i == 0){
		        			chagehr = 12;
		        		}else{
		        			chagehr++;
		        		}
		        		
		        		visitorCount = 0;
		        		
		        		
		        		if(analyType.equals("enginesound") || analyType.equals("virtualtour")){
		        			
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			//String params = "&date=2015-12-09&type=actions-list&limit=all";
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        		    		    if(count == 2){
		        		    		    	if(analyType.equals("enginesound")){	
		        		    		    		if(dataArr[2].equals("enginesound")){
		        		    		    			SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		        		    		    			String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		        		    		    			String[] timeset = timevisit.split(",");
		        		    		    			String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		        		    		    			if(timeDiv[1].equals(String.valueOf(chagehr))){
		        		    		    				if(timeDiv[4].equals("am") && i < 12){
		        											visitorCount++;
		        										}else if(timeDiv[4].equals("pm") && i >= 12){
		        											visitorCount++;
		        										}
		        		    		    			}
		        		    		    		}
		        		    		    	}else if(analyType.equals("virtualtour")){
		        		    		    		if(dataArr[2].equals("virtualTour")){
		        		    		    			SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		        		    		    			String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		        		    		    			String[] timeset = timevisit.split(",");
		        		    		    			String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		        		    		    			if(timeDiv[1].equals(String.valueOf(chagehr))){
		        		    		    				if(timeDiv[4].equals("am") && i < 12){
		        											visitorCount++;
		        										}else if(timeDiv[4].equals("pm") && i >= 12){
		        											visitorCount++;
		        										}
		        		    		    			}
		        		    		    		}
		        		    		    	}
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        			
		        		}else{
		        		
		        		String params = "&date="+dates+"&type=visitors-list&limit=all";
		        		try {
		        			JSONArray jsonArray = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		    				for(int j=0;j<jsonArray.length();j++){
		    	    			String data = jsonArray.getJSONObject(j).get("landing_page").toString();
		    	    			String arr[] = data.split("/");
		    	    			if(arr.length > 5){
		    	    			  if(arr[5] != null){

		    	    				  	SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		    	    					  String timevisit = jsonArray.getJSONObject(j).get("time_pretty").toString();
		    	    					  String[] timeset = timevisit.split(",");
											String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
											if(timeDiv[1].equals(String.valueOf(chagehr))){
												if(analyType.equals("user")){
				    	    						  Integer langValue = mapRM.get(jsonArray.getJSONObject(j).get("uid").toString()); 
				    	    						  
				    	    						  if (langValue == null) {
				    	    							  visitorCount = visitorCount + 1;
				    	    							  mapRM.put(jsonArray.getJSONObject(j).get("uid").toString(), 1);
				    	    						  	}
				    	    					  	}else if(analyType.equals("pageview")){
				    	    					  		if(timeDiv[4].equals("am") && i < 12){
															visitorCount = visitorCount + 1;
														}else if(timeDiv[4].equals("pm") && i >= 12){
															visitorCount = visitorCount + 1;
														}
				    	    					  	}else if(analyType.equals("sessionDuration")){
				    	    					  		
				    	    					  		
				    	    					  		if(timeDiv[4].equals("am") && i < 12){
															visitorCount = visitorCount + 1;
															totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
														}else if(timeDiv[4].equals("pm") && i >= 12){
															visitorCount = visitorCount + 1;
															totalTime = totalTime + Integer.parseInt(jsonArray.getJSONObject(j).get("time_total").toString());
														}
				    	    					  		
				    	    					  	}
												
											}
		    	    			  }
		    	    			}
		    				}	
		    				
		    			} catch (JSONException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		        	}
		        		
		        		if(analyType.equals("sessionDuration")){
		        			if(visitorCount != 0){
		            			avgDur = totalTime/visitorCount;	
		            		}
		        			
		        			allVisitor.add(avgDur);
		        		}else{
		        			allVisitor.add(visitorCount);
		        		}
		        		
		        		String valueTime = null;
		        		if(i == 0){
		        			valueTime = "12am";
		        		}
		        		if(i >= 12){
		        			if(i == 12){
		        				valueTime = "12pm";
		        			}else{
		        				int chang = i - 12;
		        				valueTime = chang+"pm";
		        			}
		        			
		        		}else{
		        			if(i != 0){
		        				valueTime = i+"am";
		        			}
		        		}
		        		months.add(valueTime);
		        		
		        		if(i == 0){
		        			chagehr = 0;
		        		}else{
		        			if(chagehr == 12){
		        				chagehr = 0;
		        			}
		        		}
		        	}
		        	
		    	}
		    
		    	Map map = new HashMap();
		    	map.put("allVisitor", allVisitor);
		    	map.put("months", months);
		    	return ok(Json.toJson(map));
		    }
		    
		    public static Result getAllMailDaysUserStats(String lasttime,String analyType) {
		    	

		    	List<Integer> allVisitor = new ArrayList<Integer>(30);
		    	List<String> months = new ArrayList<String>(30);
		    	Map<String, Integer> mapRM = new HashMap<String, Integer>();
		    	int visitorCount = 0;
		    	int totalTime = 0;
		    	int avgDur = 0;
		    	Calendar c = Calendar.getInstance();
		    	
		    	if(lasttime.equals("month")){
		    		
		        	String[] monthsArr = new String[30]; 
		        	c.add(Calendar.DAY_OF_YEAR, -29);
		        	
		        	for(int i=0;i<30;i++) {
		        		mapRM.clear();
		        		visitorCount = 0;
		        		totalTime = 0;
		        		avgDur = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		if(days.length() < 2){
		        			days = 0+days;
		        		}
		        		if(month.length() < 2){
		        			month = 0+month;
		        		}
		        		String dates = year + month + days;
		        		
		        			
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        					int count = data.split("#").length - 1;
		        		    		 if(count == 2){
		        		    			 if(analyType.equals("requestMore")){
		        		    				 if(dataArr[2].equals("requestmoreinfo")){
		         		    					visitorCount++;
		         		    				 }
		        		    			 }else if(analyType.equals("scheduleTest")){
		        		    				 if(dataArr[2].equals("scheduletest")){
		          		    					visitorCount++;
		          		    				 }
		        		    			 }else if(analyType.equals("tradeInApp")){
		        		    				 if(dataArr[2].equals("tradeinapp")){
		           		    					visitorCount++;
		           		    				 }
		         		    			 }else if(analyType.equals("emailToFrd")){
		        		    				 if(dataArr[2].equals("emailtofriend")){
		            		    					visitorCount++;
		            		    				 }
		          		    			 }
		        		    			 
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        		
		        			allVisitor.add(visitorCount);
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("week")){
		    		
		        	String[] monthsArr = new String[7]; 
		        	c.add(Calendar.DAY_OF_YEAR, -6);
		        	
		        	for(int i=0;i<7;i++) {
		        		mapRM.clear();
		        		visitorCount = 0;
		        		totalTime = 0;
		        		avgDur = 0;
		        		String year = c.get(Calendar.YEAR)+"-";
		        		String days = c.get(Calendar.DATE)+"";
		        		Integer addmonth = c.get(Calendar.MONTH);
		        		Integer addOneMo = addmonth + 1;
		        		String month = String.valueOf(addOneMo)+"-";
		        		if(days.length() < 2){
		        			days = 0+days;
		        		}
		        		if(month.length() < 2){
		        			month = 0+month;
		        		}
		        		String dates = year + month + days;
		        		
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        						if(count == 2){
		           		    			 if(analyType.equals("requestMore")){
		           		    				 if(dataArr[2].equals("requestmoreinfo")){
		            		    					visitorCount++;
		            		    				 }
		           		    			 }else if(analyType.equals("scheduleTest")){
		           		    				 if(dataArr[2].equals("scheduletest")){
		             		    					visitorCount++;
		             		    				 }
		           		    			 }else if(analyType.equals("tradeInApp")){
		           		    				 if(dataArr[2].equals("tradeinapp")){
		              		    					visitorCount++;
		              		    				 }
		            		    			 }else if(analyType.equals("emailToFrd")){
		           		    				 if(dataArr[2].equals("emailtofriend")){
		               		    					visitorCount++;
		               		    				 }
		             		    			 }
		           		    			 
		           		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        		
		        			allVisitor.add(visitorCount);
		        		
		        		
		        		months.add(month+days);
		        		c.add(Calendar.DAY_OF_YEAR, 1);
		        	}
		        	
		    	}else if(lasttime.equals("day")){
		    		
		    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    		Date date = new Date();
		    		List<String> monthsArr = new ArrayList<>();
		    		
		    		int chagehr = 0;
		    		totalTime = 0;
		    		avgDur = 0;
		    		
		    		String dates = dateFormat.format(date);
		        	for(int i=0;i<24;i++) {
		        		mapRM.clear();
		        		if(i == 0){
		        			chagehr = 12;
		        		}else{
		        			chagehr++;
		        		}
		        		
		        		visitorCount = 0;
		        		
		        			String params = "&date="+dates+"&type=actions-list&limit=all";
		        			//String params = "&date=2015-12-04&type=actions-list&limit=all";
		        			Map<String, Integer> map = new HashMap<String, Integer>();
		        			try {
		        				JSONArray jsonArray1 = new JSONArray(callClickAPI(params)).getJSONObject(0).getJSONArray("dates").getJSONObject(0).getJSONArray("items");
		        				for(int j=0;j<jsonArray1.length();j++){
		        					String data = jsonArray1.getJSONObject(j).get("action_url").toString();
		        					String dataArr[] = data.split("#");
		        					if(dataArr!=null && dataArr.length>0){
		        						int count = data.split("#").length - 1;
		        		    		    if(count == 2){
		        		    		    	if(analyType.equals("requestMore")){
		        		    		    	 if(dataArr[2].equals("requestmoreinfo")){
		        		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		          	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		          	    					    String[] timeset = timevisit.split(",");
		      									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		      									if(timeDiv[1].equals(String.valueOf(chagehr))){
		      										if(timeDiv[4].equals("am") && i < 12){
														visitorCount = visitorCount + 1;
													}else if(timeDiv[4].equals("pm") && i >= 12){
														visitorCount = visitorCount + 1;
													}
		      									}
		  	        		    			 }
		        		    		       }else if(analyType.equals("scheduleTest")){
		        		    		    	   if(dataArr[2].equals("scheduletest")){
		           		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		             	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		             	    					    String[] timeset = timevisit.split(",");
		         									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		         									if(timeDiv[1].equals(String.valueOf(chagehr))){
		         										if(timeDiv[4].equals("am") && i < 12){
															visitorCount = visitorCount + 1;
														}else if(timeDiv[4].equals("pm") && i >= 12){
															visitorCount = visitorCount + 1;
														}
		         									}
		     	        		    			 }
		        		    		       }else if(analyType.equals("tradeInApp")){
		        		    		    	   if(dataArr[2].equals("tradeinapp")){
		              		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		                	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		                	    					    String[] timeset = timevisit.split(",");
		            									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		            									if(timeDiv[1].equals(String.valueOf(chagehr))){
		            										if(timeDiv[4].equals("am") && i < 12){
		    													visitorCount = visitorCount + 1;
		    												}else if(timeDiv[4].equals("pm") && i >= 12){
		    													visitorCount = visitorCount + 1;
		    												}
		            									}
		        	        		    			 }
		        		    		       }else if(analyType.equals("emailToFrd")){
		        		    		    	   if(dataArr[2].equals("emailtofriend")){
		             		    		    		SimpleDateFormat ra = new SimpleDateFormat("HH:mm:ss");
		               	    					    String timevisit = jsonArray1.getJSONObject(j).get("time_pretty").toString();
		               	    					    String[] timeset = timevisit.split(",");
		           									String[] timeDiv = timeset[1].split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		           									if(timeDiv[1].equals(String.valueOf(chagehr))){
		           										if(timeDiv[4].equals("am") && i < 12){
															visitorCount = visitorCount + 1;
														}else if(timeDiv[4].equals("pm") && i >= 12){
															visitorCount = visitorCount + 1;
														}
		           									}
		       	        		    			 }
		        		    		       }
		      									
		        		    			}
		        					}
		        				}
		        			} catch (Exception e) {
		        				e.printStackTrace();
		        			}
		        		
		        		
		        			allVisitor.add(visitorCount);
		        		
		        		
		        		String valueTime = null;
		        		if(i == 0){
		        			valueTime = "12am";
		        		}
		        		if(i >= 12){
		        			if(i == 12){
		        				valueTime = "12pm";
		        			}else{
		        				int chang = i - 12;
		        				valueTime = chang+"pm";
		        			}
		        			
		        		}else{
		        			if(i != 0){
		        				valueTime = i+"am";
		        			}
		        		}
		        		months.add(valueTime);
		        		
		        		if(i == 0){
		        			chagehr = 0;
		        		}else{
		        			if(chagehr == 12){
		        				chagehr = 0;
		        			}
		        		}
		        	}
		        	
		    	}
		    
		    	Map map = new HashMap();
		    	map.put("allVisitor", allVisitor);
		    	map.put("months", months);
		    	return ok(Json.toJson(map));
		    	
		    }
		    
		    public static AuthUser getLocalUser() {
		    	String id = session("USER_KEY");
		    	AuthUser user = AuthUser.find.byId(Integer.parseInt(id));
		    	//AuthUser user = getLocalUser();
				return user;
			}
}