package scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import models.NewsletterDate;
import models.RequestMoreInfo;
import models.ScheduleTest;
import models.TradeIn;
import play.libs.Akka;
import play.libs.Time.CronExpression;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.TraitSetter;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;

public class TestDriveScheduler {
	static Cancellable c = null;
	static Cancellable c1 = null;
	
	public static void newsletterSchedulling() {
		System.out.println("TestDriveScheduler");
		ActorSystem newsLetter = Akka.system();
		
		if(c !=null && c1 != null) {
			c.cancel();
			c1.cancel();
		}
		
	    c1 = newsLetter.scheduler().schedule(
			Duration.create(0, TimeUnit.MILLISECONDS),
			Duration.create(1, TimeUnit.DAYS), new Runnable() {
				public void run() {
					
					SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-mm-dd");
					SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
					Date date = new Date();
					Calendar cal = Calendar.getInstance();
					Date newDate = DateUtils.addHours(date, 48);
					Date crDate = null;
					Date ceTime = null;
					Date aftDate = null;
					Date aftTime = null;
					
					try {
							crDate = dateFormatter.parse(dateFormatter.format(date));
							ceTime = timeFormatter.parse(timeFormatter.format(date));
							aftDate = dateFormatter.parse(dateFormatter.format(newDate));
							aftTime = timeFormatter.parse(timeFormatter.format(newDate));
							/*System.out.println(crDate);
							System.out.println(ceTime);
							System.out.println(aftDate);
							System.out.println(aftTime);*/
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					
					List<String> emailList = new ArrayList<>();
					List<RequestMoreInfo> moreList = RequestMoreInfo.findByScheduler();
					List<TradeIn> traidList = TradeIn.findByScheduler();
					List<ScheduleTest> scheduleList = ScheduleTest.findByScheduler();
					System.out.println(crDate);
					System.out.println(aftDate);
					for (RequestMoreInfo info : moreList) {
						if(info.confirmDate !=null){
							System.out.println(info.confirmDate);
							if((info.confirmDate.after(crDate) && info.confirmDate.before(aftDate)) || info.confirmDate.equals(crDate) || info.confirmDate.equals(aftDate)){
								emailList.add(info.email);
								/*System.out.println(info.email);
								System.out.println(info.confirmDate);*/
								info.setScheduleEmail(1);
								info.update();
							}
						}
						
					}
					for (TradeIn info : traidList) {
						if(info.confirmDate !=null){
							if((info.confirmDate.after(crDate) && info.confirmDate.before(aftDate)) || info.confirmDate.equals(crDate) || info.confirmDate.equals(aftDate)){
								emailList.add(info.email);
								/*System.out.println(info.email);
								System.out.println(info.confirmDate);*/
								info.setScheduleEmail(1);
								info.update();
							}
						}
					}
					for (ScheduleTest info : scheduleList) {
						if(info.confirmDate !=null){
							if((info.confirmDate.after(crDate) && info.confirmDate.before(aftDate)) || info.confirmDate.equals(crDate) || info.confirmDate.equals(aftDate)){
								emailList.add(info.email);
								/*System.out.println(info.email);
								System.out.println(info.confirmDate);*/
								info.setScheduleEmail(1);
								info.update();
							}
						}
						
					}
					controllers.Application.scheduleEmail(emailList);
				}
	}, newsLetter.dispatcher());
	}
}
