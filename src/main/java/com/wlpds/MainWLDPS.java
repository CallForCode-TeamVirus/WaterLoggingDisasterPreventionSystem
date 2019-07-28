package com.wlpds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainWLDPS {
//public class MainWLDPS implements CommandLineRunner{
	public static void main(String[] args) {
	 	SpringApplication.run(MainWLDPS.class, args);
	 }
	 
	/* public VisuallyRecognisedData vrd = new VisuallyRecognisedData(); public
	 * SendingNotification sn= new SendingNotification();
	 * 
	 * public void run(String... args) throws Exception { // TODO Auto-generated
	 * method stub
	 * vrd.getVisuallyRecognisedData_Java("C:\\\\Sujay Jain\\waterLog.jpg");
	 * sn.sendTextNotification(vrd.getTextMessage()); }
	 */

}
