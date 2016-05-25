package Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Augmenter;

public class CaptureScreens {
	
	static int screen_id=0;
	
public static void captureScrn (WebDriver driver) throws Exception {
		
		System.out.println("Start capturing screen from GUI by remote desktop");
		
		waitMin(3, "wait...");
	    Random random = new Random(10000);
	    int i =random.nextInt();
	    String name = String.valueOf(screen_id);
		WebDriver augmenteDriver = (WebDriver) new Augmenter().augment(driver);
		File screenshot = ((TakesScreenshot)augmenteDriver).getScreenshotAs(OutputType.FILE);
		
		
		String path = "./screen//";
		
		 try {    
			 
			 FileUtils.copyFile(screenshot, new File(path+"00000627"+"_"+name+".png"));  
			 screen_id++;
	     } catch (Exception e) {  

	         e.printStackTrace();  

	     }  
	 	
	  
	     
	  }
public static void captureScrn (WebDriver driver, String area) throws Exception {  
	
	//capture screens by remote desktop with mapping
	System.out.println("Start capturing screen by remote ");
	waitMin(3, "wait...");
	Random random = new Random(10000);
    
	driver.manage().window().maximize();
    
    int i =random.nextInt();
    String name = String.valueOf(screen_id);
	WebDriver augmenteDriver = (WebDriver) new Augmenter().augment(driver);
	File screenshot = ((TakesScreenshot)augmenteDriver).getScreenshotAs(OutputType.FILE);
	String path = "./screen//";
	
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss"); 
	format.setTimeZone(TimeZone.getTimeZone("GMT"));
	Date date = new Date();
	String strDate = format.format(date);
	String PID = "12990";
	String screenName = PID + "_" + strDate + "_" + screen_id + ".png";
	String screenName_full =  screenName;
	
	
	
	try {    
		 
		 FileUtils.copyFile(screenshot, new File(path+screenName_full));  
		 screen_id++;
     } catch (Exception e) {  

         e.printStackTrace();  

     }  
     	mapping.mapping.start(driver, screenName_full, area);

  }
public static void getScrn(WebDriver driver)

{   // capture screen from local side
	try
	{
	File scrFile = ((TakesScreenshot) driver)
			.getScreenshotAs(OutputType.FILE);
	String screenName = "./screen/" + String.valueOf(screen_id) + ".png"; 
	try
	{
	FileUtils.copyFile(scrFile, new File(screenName));
	}
	catch(IOException e)
	{
		System.out.println(e.toString());
	}
	screen_id++;
	}
	catch(WebDriverException e)
	{
		System.out.println("//----------Screen Capture failed! Capture again!");
		waitMin(5, "wait...");
		String screenName = "./screen/" + String.valueOf(screen_id) + ".png"; 
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		try
		{
		FileUtils.copyFile(scrFile, new File(screenName));
		}
		catch(IOException e2)
		{
			System.out.println(e2.toString());
		}
		screen_id++;
		
	}

}
public static String getScrn(WebDriver driver,String area,String PID,String dropID) throws Exception{
	 
	// capture screen from local side with mapping 
	Thread.sleep(3000);
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss"); 
	format.setTimeZone(TimeZone.getTimeZone("GMT"));
	Date date = new Date();
	String strDate = format.format(date);
	String screenName = "";//PID + "_" + dropID + strDate + "_"+ ".png";
	String screenName_full = "";
	
	try
	{
	File scrFile = ((TakesScreenshot) driver)
			.getScreenshotAs(OutputType.FILE);
	screenName = "./screen/" + PID + "_" + dropID+"_"+ strDate+ ".png";
	screenName_full =  PID + "_" + dropID+"_"+ strDate + ".png";;
	try
	{
	FileUtils.copyFile(scrFile, new File(screenName));
	}
	catch(IOException e)
	{
		System.out.println(e.toString());
	}
	
	}
	catch(WebDriverException e)
	{
		System.out.println("//----------Screen Capture failed! Capture again!");
		waitMin(5, "wait...");
		screenName = "./screen/" +  PID + "_" + dropID + strDate+ ".png";
		screenName_full =  PID + "_" + dropID+"_"+ strDate + ".png";
		
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		try
		{
		FileUtils.copyFile(scrFile, new File(screenName));
		}
		catch(IOException e2)
		{
			System.out.println(e2.toString());
		}
	}
		return screenName_full;
		//mapping.mapping.start(driver, screenName_full, area);
}

public static void waitMin(int second,String str)
{
	System.out.println("Wait " + second + " seconds!");
	try {
		int i;
		System.out.println(str);
		for(i=second;i>0;i--)
			
		{
		System.out.println(i);
		
			Thread.sleep(1000);
		
		}
		
		
	} catch (InterruptedException e) {
		
		e.printStackTrace();
	}
}

}
