package Truncation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ValidatorUtil {

	public static int screen_id = 0;
	
	private static  String getScreenStatus;
	
	public static String getGetScreenStatus() 
	{
		return getScreenStatus;
	}
	
	public static void setGetScreenStatus(String getScreenStatus) 
	{
		ValidatorUtil.getScreenStatus = getScreenStatus;
	}
	
	/*public void truncationCheck(WebDriver driver,String projectID) throws Exception{
				
		ValidatorUtil.adjustWindow(driver);		
		LinkedHashSet set = new LinkedHashSet();
		List<Thread> threadList = new ArrayList<Thread>();
								
		//ArrayList<String >tag = ValidatorUtil.createTaglist();  //createTaglist(); //ValidatorUtil.createTaglist();
					
		Pattern p = Pattern.compile(".*");			
		Matcher m = p.matcher(driver.findElement(By.cssSelector("body")).getText());			
		System.out.println("test:"+driver.findElement(By.cssSelector("body")).getText());			
			
		while(m.find())			
		{				
			if(!m.group().toString().equals(""))				
			{										
				String text = m.group().toString().trim();					
				System.out.println("Find:" + m.group().toString().trim());					
					
				if(!isNumber(text)){					
					set.add(text);															
				}									
			}												
		}
							
		System.out.println("set.size"+set.size());			
		Iterator itr = set.iterator();			
		int tempTime = set.size();			
			
		while (itr.hasNext()){								
			String text = itr.next().toString();				
			CurrentTucationDetect ct = new CurrentTucationDetect(driver,text,set);				
			Thread td = new Thread(ct);				
			td.setName(text);				
			td.start();				
			threadList.add(td);							
		}
			
		for (Thread td_:threadList){				
			try {					
				td_.join();					
			}catch (Exception error) {					
				error.printStackTrace();				
			}							
		}			
		System.out.println("Closing browser!");			
		ValidatorUtil.waitMin(3);			
		//driver.close();								
	}*/
	
	public void getScrnTruncation (String ProjectID,WebDriver driver) throws Exception {
		
		System.out.println("Start capturing screen for truncation from remote desktop");
		
		String name = getStringDateShort().replaceAll(":", "").replaceAll("\\s", "").replaceAll("-", "");
		
		WebDriver augmenteDriver = (WebDriver) new Augmenter().augment(driver);
		File screenshot = ((TakesScreenshot)augmenteDriver).getScreenshotAs(OutputType.FILE);
				
		String path = "./output/"+ProjectID+"/truncation_screenshot//"; 
		
		 try {    
			 
			 FileUtils.copyFile(screenshot, new File(path+name+".png"));  

	     } catch (Exception e) {  
	         //e.printStackTrace();  
	         ValidatorUtil.getScrn(driver);	     
	     }     		
	}
	
	public static String getStringDateShort() {
				
        Random random = new Random(10);            
		while (true){				  
			Date currentTime = new Date();			  
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			  
			String dateString = formatter.format(currentTime);			  
			return dateString+ random.nextInt();		
		}				 		  	
	} 
		
	/*public static void highlightElement(WebDriver driver, WebElement element)
	{          		
		JavascriptExecutor js = (JavascriptExecutor) driver;       		 
		js.executeScript("element = arguments[0];" +  "original_style = element.getAttribute('style');" +                
			"element.setAttribute('style', original_style + \";" + "background: yellow; border: 2px solid red;\");" +            
			"setTimeout(function(){element.setAttribute('style', original_style);}, 1000);", element); 
	}*/
		
	public static ArrayList<String> createTaglist()
	{
		ArrayList<String> tag_list= new ArrayList<String>();
		//tag_list.add("body");
		tag_list.add("button");
		tag_list.add("span");
		tag_list.add("div");
		tag_list.add("label");
		tag_list.add("input");
		tag_list.add("td");
		tag_list.add("tr");
		tag_list.add("ul");
		tag_list.add("a");
		tag_list.add("h1");
		tag_list.add("h2");
		tag_list.add("b");
		tag_list.add("Strong");
		return tag_list;
	}
	
	public static boolean isNumber (String str) {		
		boolean isNumber = false;	
		isNumber = str.matches("[0-9]+");		
		if (isNumber){				
			isNumber=true;	
		}		
		return isNumber;
	}
	
	public static String TimeCount(long begin) {
		String runtime =null;		 
		try{ 					 
			long k=0,end,times;					 
			SimpleDateFormat sdf=new SimpleDateFormat("mm:ss:SS"); 	    	    
			Date mydate2=new Date();	     
			end=mydate2.getTime();	     
			times=end-begin;	     
			SimpleDateFormat sdftimes=new SimpleDateFormat("mm:ss:SS"); 	          	     
			runtime = sdftimes.format(times);		      	          
		}catch (Exception e){	        	  
			System.out.println ();	          
		}	          
		return runtime+""+"(minutes)";	 	
	}
	
	public static void highlight_Element(WebDriver driver, WebElement element) {
		for (int i = 0; i < 5; i++) {
			String changecolor = "background-color: red;";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(
					"arguments[0].setAttribute('style', arguments[1]);",
					element, changecolor);
		}
	}
	
	/*public static String compose(int level)
	{
		String node="']";;
		for(int i=0;i<level;i++)
		{
			node = node + "/..";
		}
		node = node + "//following-sibling::*[1]//input";
		return node;
	}*/
	
	public static void waitMin(int second)
	{
		System.out.println("Wait " + second + " seconds!");
		try {
			int i;
			for(i=second;i>0;i--)
			{
			System.out.println(i);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void getScrn(WebDriver driver)
			
	{   // capture screen from local side
		//String path = "./truncation_screenshot//"; 
		try
		{
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		String screenName = "./truncation_screenshot//" + String.valueOf(screen_id) + ".png"; 
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
			waitMin(5);
			String screenName = "./truncation_screenshot//" + String.valueOf(screen_id) + ".png"; 
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
	
	public static void adjustWindow(WebDriver driver)
	{
		Dimension window = driver.manage().window().getSize();
				    int height = window.height;
				    height = height/2;
				    Dimension win = new Dimension(window.width,height);
				    //driver.manage().window().setSize(win);
				    driver.manage().window().maximize();
	}
	
	/*public static void highlightElement(WebDriver driver, WebElement element) 	
	{          		
		JavascriptExecutor js = (JavascriptExecutor) driver;       		 
		js.executeScript("element = arguments[0];" +  "original_style = element.getAttribute('style');" +                
			"element.setAttribute('style', original_style + \";" + "background: yellow; border: 2px solid red;\");" +            
			"setTimeout(function(){element.setAttribute('style', original_style);}, 1000);", element); 	
	}*/

	/*public static List<WebElement> elementList (WebDriver driver,String path) 
	{		
		String xpathString_ = path;	
		List<WebElement>  elelist = driver.findElements(By.xpath(xpathString_));	
		List<WebElement> list = new ArrayList<WebElement>();
	
		for (WebElement ele : elelist){							
			try{								
				if (ele.isDisplayed()&&ExpectedConditions.elementToBeClickable(By.xpath(ele.toString()))!=null){								
					//String eleString = ele.getText().toString();				
					list.add(ele);														
				}				
			}catch (Exception error){int k=0; k++;System.out.println("Element is no longer attached to the DOM"+k);}	
		}		
		System.out.println("There are "+list.size()+" "+"elements");	
		return  list;	
	}*/
}
