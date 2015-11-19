package viewmodel;

public class ToDoVM {

	public Long id;
	public String task;
	public String dueDate;
	public String assignedTo;
	public Integer assignedToId;
	public String priority;
	public String status;
	public Integer assignedById;
	public static void main(String[] arg) {
    	String s = "http://glider-autos.com:8080/glivr/vehicleDetails/WBAFR7C51CC813198";
    	String[] arr = s.split("/");
    	System.out.println(s);
    	for(String a:arr) {
    		System.out.println(a);
    	}
    }
}
