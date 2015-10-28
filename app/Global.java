
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
import scala.concurrent.duration.Duration;
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

		ActorSystem getLiveGame = Akka.system();
		getLiveGame.scheduler().schedule(
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
								controllers.Application.sendNewsletterEmail();
							}
						}
					}
				}, getLiveGame.dispatcher());

		
		
	}

	@Override
	public void onStop(Application app) {
		Logger.info("Application shutdown...");
	}
}