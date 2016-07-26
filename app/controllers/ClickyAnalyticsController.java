package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.SqlRow;

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
	    	List<Location> locations = Location.findAllData();
	    	
				SqlRow maxDate = ClickyVisitorsList.getMaxDate();
	    	    System.out.println(maxDate.get("maxdate"));
	    	     Date curr = new Date();
	    	    String sDate = df.format(curr);
	    	    Date newcurrDate = null;
				try {
					newcurrDate = df.parse(sDate);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	    	    /* int b=13;
	    	   	   
	    	   	   for(int k=28;k>=b;b++)
	    	   	   {*/
	    	   	   	//String sDate="2016-06-"+b;
	    	         // String sDate="2016-06-28";
	    	           Date sampleDate=(Date) maxDate.get("maxdate");
	    		     	System.out.println(maxDate.get("maxdate"));
	    				GregorianCalendar gcal = new GregorianCalendar();
	    				gcal.setTime(sampleDate);
	    				while (gcal.getTime().before(newcurrDate)) {
	    				    Date d = gcal.getTime();
	    				    sDate=df.format(d);
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
			    				
			    			cVisitorsList.setDateClick(thedate);
			    			
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
		    				
		    			
		    				    				
		    				cPages.setSaveDate(curr);
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
		    				
		    			
		    				    				
		    				cPages.setSaveDate(curr);
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
		    				
		    			
		    			
		    			
		    				cPages.setSaveDate(curr);
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

		    				
		    				
		    				
		    				cPages.setSaveDate(curr);
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

		    				
		    				
		    				
		    				cPages.setSaveDate(curr);
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
		    				    				
		    				cPages.setSaveDate(curr);
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
			    				cPages.setSaveDate(curr);
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

		    				
		    				
		    				
		    				cPages.setSaveDate(curr);
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
		    			
		    			
		    			
		    				cPages.setSaveDate(curr);
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
		    	
		    				cPages.setSaveDate(curr);
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
		    				cPages.setSaveDate(curr);
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
		    				cPages.setSaveDate(curr);
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
		    				cPages.setSaveDate(curr);
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
		    				cPages.setSaveDate(curr);
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
		    				cPage.setSaveDate(curr);
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
		    			
		    			
		    			
		    			cPages.setSaveDate(curr);
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
		    			
		    			cPages.setSaveDate(curr);
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
		    	
		    			
		    			
		    			
		    			cPages.setSaveDate(curr);
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
		    			
		    			cPages.setSaveDate(curr);
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
		    			
		    			cPages.setSaveDate(curr);
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
		    				cPages.setSaveDate(curr);
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
			    			
			    			cAction.setCurrDate(curr);
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
	    	return ok();
	    }

	
}
