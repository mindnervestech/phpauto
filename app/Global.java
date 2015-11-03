
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import models.NewsletterDate;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.mvc.EssentialFilter;
import play.libs.Akka;
import play.libs.Time.CronExpression;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorSystem;
import play.filters.gzip.GzipFilter;

public class Global extends GlobalSettings {
	public static final int CHAR_LEN = 200;
	public static final String APP_ENV_LOCAL = "local";
	public static final String APP_ENV_VAR = "CURRENT_APPNAME";
	
	public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{GzipFilter.class};
    }
	
	@Override
	public void onStart(Application app) {
		ActorSystem newsLetter = Akka.system();
		newsLetter.scheduler().schedule(
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
							        System.out.println("saved time  " + (double)(nextValidTimeAfter.getTime() - calObjLocal.getTimeInMillis())/(1000*60*60));
							        Akka.system().scheduler().scheduleOnce(d, new Runnable() {
								        @Override
								        public void run() {
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

	@Override
	public void onStop(Application app) {
		Logger.info("Application shutdown...");
	}
}