package scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import models.NewsletterDate;
import play.libs.Akka;
import play.libs.Time.CronExpression;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;

public class MeetingAndTestDriveScheduler {
	static Cancellable c = null;
	static Cancellable c1 = null;
	
	public static void newsletterSchedulling() {
		System.out.println("sheduler");
		ActorSystem newsLetter = Akka.system();
		if(c !=null && c1 != null) {
			c.cancel();
			c1.cancel();
		}
		
	    c1 = newsLetter.scheduler().schedule(
			Duration.create(0, TimeUnit.MILLISECONDS),
			Duration.create(1, TimeUnit.DAYS), new Runnable() {
				public void run() {
					Date date = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					Integer dateOfMonth = cal.get(Calendar.DAY_OF_MONTH);
					List<NewsletterDate> objList = NewsletterDate.findAll();
						if(objList.size() != 0) {
							if(objList.get(0).dateOfMonth.equals(dateOfMonth.toString())) {
								int minutes = 0;
								int hours = 0;
									if(objList.get(0).gmtTime != null) {
										cal.setTime(objList.get(0).gmtTime);
										minutes = cal.get(Calendar.MINUTE);
										hours = cal.get(Calendar.HOUR_OF_DAY);
									}
									try {
										   CronExpression e = new CronExpression("0 "+minutes+" "+hours+" ? * *");
									       
									       Calendar calObj = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
									       
									       Calendar calObjLocal = Calendar.getInstance();
									       calObjLocal.set(Calendar.MINUTE, calObj.get(Calendar.MINUTE));
									       calObjLocal.set(Calendar.HOUR_OF_DAY, calObj.get(Calendar.HOUR_OF_DAY));
									       
									       Date nextValidTimeAfter = e.getNextValidTimeAfter(calObjLocal.getTime());
									       FiniteDuration d = Duration.create(
									           nextValidTimeAfter.getTime() - calObjLocal.getTimeInMillis(), 
									           TimeUnit.MILLISECONDS);
									       System.out.println(d);
									       System.out.println("saved time  " + (double)(nextValidTimeAfter.getTime() - calObjLocal.getTimeInMillis())/(1000*60*60));
									       
									       c = Akka.system().scheduler().scheduleOnce(d, new Runnable() {
									    	   @Override
										       public void run() {
										    	   System.out.println("newsletter called");
										        controllers.Application.sendNewsletterEmail();
										       }
										   }, Akka.system().dispatcher());
									       
									} catch(Exception e) {
										e.printStackTrace();
									}
							}
						}
				}
	}, newsLetter.dispatcher());
	}
}
