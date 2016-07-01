import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.mvc.Result;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class Global extends GlobalSettings {
	public static final int CHAR_LEN = 200;
	public static final String APP_ENV_LOCAL = "local";
	public static final String APP_ENV_VAR = "CURRENT_APPNAME";
	
	/*public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{GzipFilter.class};
    }*/
	
	private class ActionWrapper extends Action.Simple {
		public ActionWrapper(Action<?> action) {
			this.delegate = action;
		}

		@Override
		public Promise<SimpleResult> call(Http.Context ctx) throws java.lang.Throwable {
			Promise<SimpleResult> result = this.delegate.call(ctx);
			Http.Response response = ctx.response();
			response.setHeader("Access-Control-Allow-Origin", "*");
			return result;
		}
	}
	
	@Override
	public Action<?> onRequest(Http.Request request, java.lang.reflect.Method actionMethod) {
		return new ActionWrapper(super.onRequest(request, actionMethod));
	}
	
	@Override
	public void onStart(Application app) {
	//	NewsLetter.newsletterSchedulling();
	//	TestDriveScheduler.newsletterSchedulling();
		System.err.println("Testing..");
	//	MeetingAndTestDriveScheduler.meetingSchedulling();
	//	ClickyDataScheduler.clickySchedulling();
	}
	
	
	@Override
	public void onStop(Application app) {
		Logger.info("Application shutdown...");
	}
}