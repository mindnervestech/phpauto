package viewmodel;

import java.util.Date;
import java.util.List;

public class RequestInfoVM {

	public Long id;
	public String vin;
	public String model;
	public String make;
	public String stock;
	public String name;
	public String email;
	public String phone;
	public String requestDate;
	public String bestDay;
	public String bestTime;
	public String confirmDate;
	public String confirmTime;
	public boolean isRead;
	public List<NoteVM> note;
	public String status;
	public String salesRep;
	public String prefferedContact;
	public String leadType;
}
