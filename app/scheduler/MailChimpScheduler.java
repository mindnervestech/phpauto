package scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import models.MailchimpSchedular;

import play.libs.Akka;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;

public class MailChimpScheduler {
	static Cancellable c = null;
	static Cancellable c1 = null;
	
	public static void mailChimpSchedulling() {
		System.out.println("sheduler");
		ActorSystem mailChip = Akka.system();
		if(c !=null && c1 != null) {
			c.cancel();
			c1.cancel();
		}
		
		Date curr = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String cDate = df.format(curr);
		Date cuDate = null;
		try {
			cuDate = df.parse(cDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar cal1 = Calendar.getInstance();
		
		MailchimpSchedular mScheduler = MailchimpSchedular.findByLocations(16L);
		if(mScheduler != null){
			if(mScheduler.schedularTime.equals("Daily")){
				if(mScheduler.currDate.equals(cuDate)){
					
					cal1.setTime(cuDate);
					cal1.add(Calendar.DATE, 1);
					cuDate = cal1.getTime();
					mScheduler.setCurrDate(cuDate);
					mScheduler.update();
				
				c1 = mailChip.scheduler().schedule(
						Duration.create(0, TimeUnit.MILLISECONDS),
						Duration.create(1, TimeUnit.DAYS), new Runnable() {
							public void run() {
							        controllers.CrmController.getMailChimpList();
							}
						}, mailChip.dispatcher());
				}
			}else if(mScheduler.schedularTime.equals("Weekly")){
				if(mScheduler.currDate.equals(cuDate)){
					
					
					cal1.setTime(cuDate);
					cal1.add(Calendar.DATE, 7);
					cuDate = cal1.getTime();
					mScheduler.setCurrDate(cuDate);
					mScheduler.update();
					
					
					c1 = mailChip.scheduler().schedule(
							Duration.create(0, TimeUnit.MILLISECONDS),
							Duration.create(1, TimeUnit.DAYS), new Runnable() {
								public void run() {
								        controllers.CrmController.getMailChimpList();
								}
							}, mailChip.dispatcher());
				}
			}else if(mScheduler.schedularTime.equals("Monthly")){
				if(mScheduler.currDate.equals(cuDate)){
					
					cal1.setTime(cuDate);
					cal1.add(Calendar.MONTH,1);
					cuDate = cal1.getTime();
					mScheduler.setCurrDate(cuDate);
					mScheduler.update();
					
					
					c1 = mailChip.scheduler().schedule(
							Duration.create(0, TimeUnit.MILLISECONDS),
							Duration.create(1, TimeUnit.DAYS), new Runnable() {
								public void run() {
								        controllers.CrmController.getMailChimpList();
								}
							}, mailChip.dispatcher());
				}
			}
		}
		
	    
	}
}
