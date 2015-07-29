package controllers;
import play.api.Application;
import play.api.data.Form;
import play.api.mvc.Request;
import play.api.templates.Html;
import scala.Option;
import scala.Tuple2;
import securesocial.controllers.DefaultTemplatesPlugin;
public class AuthTemplatesPlugin extends DefaultTemplatesPlugin  {

	public AuthTemplatesPlugin(Application application) {
		super(application);
		// TODO Auto-generated constructor stub
	}
/*
	@Override
	public boolean enabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Tuple2<Option<Txt>, Option<Html>> getAlreadyRegisteredEmail(
			Identity arg0, RequestHeader arg1) {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public <A> Html getLoginPage(Request<A> arg0,
			Form<Tuple2<String, String>> arg1, Option<String> arg2) {
		// TODO Auto-generated method stub
		return views.html.home.render(); // TODO
	}

	/*@Override
	public <A> Option<String> getLoginPage$default$3() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Html getNotAuthorizedPage(Request<A> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Html getPasswordChangePage(SecuredRequest<A> arg0,
			Form<ChangeInfo> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple2<Option<Txt>, Option<Html>> getPasswordChangedNoticeEmail(
			Identity arg0, RequestHeader arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Html getResetPasswordPage(Request<A> arg0,
			Form<Tuple2<String, String>> arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple2<Option<Txt>, Option<Html>> getSendPasswordResetEmail(
			Identity arg0, String arg1, RequestHeader arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple2<Option<Txt>, Option<Html>> getSignUpEmail(String arg0,
			RequestHeader arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Html getSignUpPage(Request<A> arg0, Form<RegistrationInfo> arg1,
			String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Html getStartResetPasswordPage(Request<A> arg0, Form<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> Html getStartSignUpPage(Request<A> arg0, Form<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple2<Option<Txt>, Option<Html>> getUnknownEmailNotice(
			RequestHeader arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple2<Option<Txt>, Option<Html>> getWelcomeEmail(Identity arg0,
			RequestHeader arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}*/

}
