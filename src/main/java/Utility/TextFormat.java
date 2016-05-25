package Utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.Select;


public class TextFormat  {
	
	   
	   private static int screen_id =0;
	 
	   static WebDriver driver;
	   static Point text_location;
	   static BufferedImage img ;
	   static String ScreenName="";
	   static List<String> xpaths = new ArrayList();
	   //static Utility utility = new ImpUtility();
	  
	 
	  private  static BufferedImage gsscCreateTooltip (WebDriver driver, WebElement element, int size_h, int size_w, String xpath_select) throws Exception{
		  		
		  	
		  
		  		List<String> strs = new ArrayList <String>();
		  		int time =0;
		  		List <WebElement> options  =null;
///////////////////////////////////////////////////judge the tag type
		  		String tagString = element.getTagName();
		  		
		  		if (tagString.equals("select")){
		  			
		  			Select select  = new Select(driver.findElement(By.xpath(xpath_select))) ;
					
					options  = select.getOptions();
					time = options.size();
					
					for (WebElement option:options){
						  
						  System.out.println("option.getText():"+option.getText());
						  strs.add(option.getText().toString());
					  }
		  			
		  			} else {
		  			
		  				options  = driver.findElements(By.xpath("//li"));
		  				System.out.println(":"+options.size());
		  				time= options.size();
		  				for (int j=0;j<options.size();j++) {
		  					
		  					strs.add(options.get(j).getText());
		  				}
		  				
		  			}
		  		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		  		
				String text_font = element.getCssValue("font-family");
				int tooltip_h = (time)*(size_h);
				
				BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
				Graphics2D tooltip = img.createGraphics();
				
				int tooltip_w = size_w;		//tooltip.getFontMetrics().stringWidth(text)+7;
				img = new BufferedImage(tooltip_w, tooltip_h, BufferedImage.TYPE_INT_ARGB);
				tooltip = img.createGraphics();
				
				//tooltip.setFont(new Font(text_font, Font.PLAIN,size_h));
				tooltip.setPaint(Color.decode("#F5FCDE"));
				tooltip.fillRect(0, 0, tooltip_w, tooltip_h);
				tooltip.setPaint(Color.black);
				tooltip.draw3DRect(0, 0, tooltip_w-1, tooltip_h-1, true);
				int k=0;
				for (int i=0;i<strs.size();i++){
					
					System.out.println(strs.get(i).toString());
					tooltip.drawString(strs.get(i).toString(),0,20*(i+1)); //tooltip.drawString(text,3,tooltip_h-5);
					//System.out.println("k:"+(tooltip_h+i));
				}
					
				
					tooltip.dispose();
				
		  		
		  
		  	
					return img;
	  }
	  
	  public static BufferedImage gsscAddTooltip (WebDriver driver,WebElement element, BufferedImage img, String xpath) throws Exception{	
		  
			// 创建tooltip
		  	BufferedImage tooltip = gsscCreateTooltip(driver, element, element.getSize().height,element.getSize().width,xpath);
			//MainClass.getImg(strs);//gsscCreateTooltip(driver, element, element.getSize().height,element.getSize().width,k);
			
			int tooltip_w = tooltip.getWidth();
			int tooltip_h = tooltip.getHeight();
			BufferedImage buffImg;
			Graphics2D graphic;
			
			if (img != null){
				
			
				
				int img_w = img.getWidth();
				int img_h = img.getHeight();
				
				if (img_w >= (tooltip_w+element.getLocation().getX())){
					buffImg = new BufferedImage(img_w, img_h+tooltip_h+2, BufferedImage.TYPE_INT_ARGB);//new BufferedImage(img_w, img_h+tooltip_h+2, BufferedImage.TYPE_INT_ARGB);
				}else{
					buffImg = new BufferedImage(tooltip_w+element.getLocation().getX()+10, img_h+tooltip_h+2, BufferedImage.TYPE_INT_ARGB); 
				}
				graphic = buffImg.createGraphics();
				graphic.drawImage(img, 0, 0, null);
				graphic.drawImage(tooltip, element.getLocation().x+2, element.getLocation().y+element.getSize().height, null); //(tooltip, element.getLocation().x+20, element.getLocation().y+20, null);
			}else{
				buffImg = new BufferedImage(tooltip_w+3, tooltip_h+2, BufferedImage.TYPE_INT_ARGB);
				graphic = buffImg.createGraphics();
				graphic.drawImage(tooltip, 3, 1, null);
			}
				graphic.dispose();
			
				return buffImg;
		}
	  
	  public static BufferedImage captureScrn (WebDriver driver) throws Exception { 
		  
		  System.out.println("Start capturing screen from GUI from remoteDesktop");
			

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
			String PID = "Tooltips_original";
			String screenName = PID+".png";
			//String screenName = PID + "_" + strDate + "_" + screen_id + ".png";
			String screenName_full =  screenName;
			
			ScreenName =screenName_full;
			
			try {    
				 
				 FileUtils.copyFile(screenshot, new File(path+screenName_full));  
				 screen_id++;
		     } catch (Exception e) {  

		         e.printStackTrace();  
		        
		    		

		     }  
		     	
		     InputStream imagein=new FileInputStream(path+screenName_full);
		     BufferedImage image=ImageIO.read(imagein);
		     
		     
		     return image;		
	  }
	  
public static BufferedImage getScrn(WebDriver driver) 

{   
	
	// String.valueOf(screen_id);
	// capture screen from local side
	String path = "./screen//";
	
	BufferedImage image=null;
	driver.manage().window().maximize();
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss"); 
	format.setTimeZone(TimeZone.getTimeZone("GMT"));
	Date date = new Date();
	String strDate = format.format(date);
	String PID = "Tooltips_original";
	//String screenName = PID + "_" + strDate + "_" + screen_id + ".png";
	String screenName = PID+".png";
	String screenName_full =  screenName;
	ScreenName =screenName_full;
	
	try
	{
		
		
	File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	
	try
	{
	FileUtils.copyFile(scrFile, new File(path+screenName_full));
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
		//utility.waitMin(5, "wait...");
		
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		try
		{
		FileUtils.copyFile(scrFile, new File(path+screenName));
		}
		catch(IOException e2)
		{
			System.out.println(e2.toString());
		}
		screen_id++;
		
	}
		try{
			InputStream imagein=new FileInputStream(path+screenName_full);
			image =ImageIO.read(imagein);
		}catch (Exception error) {}
		
	
			return image;
}
public  static void SetLocalId(WebDriver driver,WebElement element, int loc_id) throws Exception {
  	JavascriptExecutor js = (JavascriptExecutor) driver;       
	 js.executeScript("element = arguments[0];"+"element.setAttribute('local_id',arguments[1]);",element,loc_id);
}
	   
private  static BufferedImage gsscCreateTooltip (WebDriver driver, WebElement element, int size_h, int size_w) throws Exception{
	
	List<String> strs_tooltip = new ArrayList <String>();
	int time =0;
///////////////////////////
	//strs_tooltip.add(element.getAttribute("data-qtip").toString());
	try {
		
		if (!element.getAttribute("data-qtip").toString().equals("")){
			strs_tooltip.add(element.getAttribute("data-qtip").toString());
		}
	}catch(Exception error) {
		
		if (!element.getAttribute("title").toString().equals("")) {
			strs_tooltip.add(element.getAttribute("title").toString());
		}else {
			System.out.println("no attibute");
		}
	}
	
	
///////////////////////////
	
	String text_font = element.getCssValue("font-family");
	int tooltip_h = size_h+7;//(time)*(size_h)+30;
	
	BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	Graphics2D tooltip = img.createGraphics();
	
	int tooltip_w = tooltip.getFontMetrics().stringWidth(strs_tooltip.get(0).toString())+7; //size_w+30;		//tooltip.getFontMetrics().stringWidth(text)+7;
	img = new BufferedImage(tooltip_w, tooltip_h, BufferedImage.TYPE_INT_ARGB);
	tooltip = img.createGraphics();
	
	
	tooltip.setPaint(Color.decode("#F5FCDE"));
	tooltip.fillRect(0, 0, tooltip_w, tooltip_h);
	tooltip.setPaint(Color.black);
	tooltip.draw3DRect(0, 0, tooltip_w-1, tooltip_h-1, true);
	
	for (int i=0;i<strs_tooltip.size();i++){
		
		System.out.println(strs_tooltip.get(i).toString());
		tooltip.drawString(strs_tooltip.get(i).toString(),0,20*(i+1)); //tooltip.drawString(text,3,tooltip_h-5);
		
	}
		
	
		tooltip.dispose();
	
		

	
		return img;
	
}	  

public static BufferedImage gsscAddTooltip (WebDriver driver,WebElement element, BufferedImage img) throws Exception{	
	  
	// 创建tooltip
  	BufferedImage tooltip = gsscCreateTooltip(driver, element, element.getSize().height,element.getSize().width);
	//MainClass.getImg(strs);//gsscCreateTooltip(driver, element, element.getSize().height,element.getSize().width,k);
	
	int tooltip_w = tooltip.getWidth();
	int tooltip_h = tooltip.getHeight();
	BufferedImage buffImg;
	//BufferedImage buffImg_temp;
	Graphics2D graphic;
	
	if (img != null){
		
		
		
		int img_w = img.getWidth();
		int img_h = img.getHeight();
		
		if (img_w >= (tooltip_w+element.getLocation().getX())){
			buffImg = new BufferedImage(img_w, img_h+tooltip_h+2, BufferedImage.TYPE_INT_ARGB);//new BufferedImage(img_w, img_h+tooltip_h+2, BufferedImage.TYPE_INT_ARGB);
		}else{
			buffImg = new BufferedImage(tooltip_w+element.getLocation().getX()+10, img_h+tooltip_h+2, BufferedImage.TYPE_INT_ARGB); 
		}
		graphic = buffImg.createGraphics();
		graphic.drawImage(img, 0, 0, null);
		graphic.drawImage(tooltip, element.getLocation().x+2, element.getLocation().y+element.getSize().height, null); //(tooltip, element.getLocation().x+20, element.getLocation().y+20, null);
	}else{
		buffImg = new BufferedImage(tooltip_w+3, tooltip_h+2, BufferedImage.TYPE_INT_ARGB);
		graphic = buffImg.createGraphics();
		graphic.drawImage(tooltip, 3, 1, null);
	}
		graphic.dispose();
	
		return buffImg;
}
	 



}
