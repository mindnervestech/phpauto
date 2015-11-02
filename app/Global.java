
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
								if(objList.get(0).newsletterTime != null) {
									cal.setTime(objList.get(0).newsletterTime);
									minutes = cal.get(Calendar.MINUTE);
									hours = cal.get(Calendar.HOUR);
								}
								try {
									CronExpression e = new CronExpression("0 "+minutes+" "+hours+" ? * *");
							        Date nextValidTimeAfter = e.getNextValidTimeAfter(new Date());
							        FiniteDuration d = Duration.create(
							            nextValidTimeAfter.getTime() - System.currentTimeMillis(), 
							            TimeUnit.MILLISECONDS);
							        
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