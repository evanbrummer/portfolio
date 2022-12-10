package testing;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;


public class Main {

	public static void main(String[] args) {
		
		soupAmzn pg = new soupAmzn();
		
		// always ask first 
		pg.getURL();
		
		// creating timer task, timer
		Timer timer = new Timer();
		System.out.println("Connecting to Amazon...");
		timer.schedule(new TimerTask() {
		    public void run() {
		    try {
		    	
				pg.getDocPrice();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //System.out.println("check");
		    //function that checks to see if the price is right or not 
		    
		    }
			}, 500, 1000 );
		      
	}
}



