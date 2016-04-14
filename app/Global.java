import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.mvc.EssentialFilter;
import play.filters.gzip.GzipFilter;
import scheduler.ClickyDataScheduler;
import scheduler.MeetingAndTestDriveScheduler;
import scheduler.NewsLetter;
import scheduler.TestDriveScheduler;

public class Global extends GlobalSettings {
	public static final int CHAR_LEN = 200;
	public static final String APP_ENV_LOCAL = "local";
	public static final String APP_ENV_VAR = "CURRENT_APPNAME";
	
	public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{GzipFilter.class};
    }
	
	@Override
	public void onStart(Application app) {
		NewsLetter.newsletterSchedulling();
		TestDriveScheduler.newsletterSchedulling();
		MeetingAndTestDriveScheduler.meetingSchedulling();
		ClickyDataScheduler.clickySchedulling();
	}
	
	
	@Override
	public void onStop(Application app) {
		Logger.info("Application shutdown...");
	}
}