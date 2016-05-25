package Truncation;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

public class CurrentTucationDetect implements Runnable {
	
	//private int sleepTime;
	//private String threadName;
	WebDriver driver;
	private String text;
	//private int timeTemp;
	private LinkedHashSet set;
	final String projectId="00000123";
	
	DetectElementArea a = new DetectElementArea();
	ValidatorUtil util = new  ValidatorUtil();
	 
	public CurrentTucationDetect(WebDriver driver_previous,String text_,LinkedHashSet set_) {	
		driver=driver_previous;
		text = text_;
		set= set_;
	}
		
	@Override
	public void run() {	
		
		//System.out.println("Start thread: "+Thread.currentThread().getName());
		
		//Date date = new Date();
		//long longtime = date.getTime();				
		//int k=0;		
		
		ArrayList<String >tag = ValidatorUtil.createTaglist();		
		int count_h=0, count_v=0;
				
		try {										
			int count[];
			
			//System.out.println("running thread: "+Thread.currentThread().getName()+ text);
			count = locate_element(driver, text, tag);
			
			count_h = count_h + count[0];
			count_v = count_v + count[1];
			
			//System.out.println("count.length "+count.length);
			//System.out.println("-------------------------\n");
			
		}catch (Exception error) {		   
			error.printStackTrace();	
		}
		
		if (count_h+count_v>0) {					
			try {				
				String temp ="Start capture screen!";
				ValidatorUtil.setGetScreenStatus(temp);
				this.getScrnTruncation(driver);									
			}catch (Exception error){
				error.printStackTrace();			
			}								
		}else {								
			System.out.println("No truncated strings in current page!");	
			//System.out.println("Page need to detect again!");	
			try{	
				//a.area_for_each(driver,set,projectId);	
			}catch(Exception error){
				error.printStackTrace();
			}
		}			
	}
		
	int[] locate_element(WebDriver driver, String label, ArrayList<String> tag) throws Exception
	{		
		ArrayList<WebElement> objs = new ArrayList<WebElement>();		
		JComponent jl = new JLabel();
		String node = "";
		int text_width=0;
		int text_height = 0;
		int truncation_count_h = 0;
		int truncation_count_v = 0;
		int count[]={0,0};
		
		String font;
		for(int i=0; i<tag.size();i++)
		{				 
			node = "//" + tag.get(i) + "[text()='"+ label + "']";						 
			List<WebElement> objs_temp = driver.findElements(By.xpath(node));				 
			//System.out.println (i+ " For tag: "+ tag.get(i)+" objs_temp: "+objs_temp.size());
				 
			int k = objs_temp.size();				 
			if (k!=0){					
				for(int j=0;j<objs_temp.size();j++)					
				{						
					objs.add(objs_temp.get(j));						
					System.out.println("Node: " + node);
					
					int element_width = objs_temp.get(j).getSize().width;							
					int element_height = objs_temp.get(j).getSize().height;													
					int font_size = (int)Float.parseFloat(objs_temp.get(j).getCssValue("font-size").replace("px", ""));						
					//System.out.println("Dimension: " + objs_temp.get(j).getSize() + "\nFont-size:" + font_size);						
					int font_weight = Integer.parseInt(objs_temp.get(j).getCssValue("font-weight"));					    
					//System.out.println("Font weight: " + font_weight); 					    
					String font_family = objs_temp.get(j).getCssValue("font-family");					    
					//System.out.println("Font family: " + font_family); 	
					    					    
					//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); 						    
					//System.out.println("element_width " + "xxxxxxxxxxxxx"+element_width); 						    
					//System.out.println("element_height " + "xxxxxxxxxxxxx"+element_height); 						    
					int element_area = element_width * element_height;					    
					//System.out.println("element_area " + "xxxxxxxxxxxxx"+element_area); 						    					    
					    
					if(font_family.indexOf(",") != -1)					    
					{					    	
						font = font_family.substring(0, font_family.indexOf(","));					    
					}					    
					else					    
					{					    	
						font = font_family;					    
					}					    
					//System.out.println("Font: " + font); 					    					 
					if(element_width> 0)					 
					{						 
						if(font_weight == 400)						 
						{							 
							Font f = new Font(font, Font.PLAIN, font_size);							 
							FontMetrics fm = jl.getFontMetrics(f);							 
							text_width =  fm.stringWidth(label);							 
							text_height = fm.getHeight();							 							 
							 
							//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); 								 
							//System.out.println("Text Width: " + text_width); 							 
							//System.out.println("Text Height: " + text_height); 							 
							int text_area = text_width * text_height;							 
							//System.out.println("Text area: " + text_area);							 
							//System.out.println("Element area: " + element_area);							 
							//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); 							 							 						 
						}						 
						if(font_weight == 700)						 
						{							 
							Font f = new Font(font, Font.BOLD, font_size);							 
							FontMetrics fm = jl.getFontMetrics(f);							 
							text_width =  fm.stringWidth(label);							 
							text_height = fm.getHeight();							 
							//System.out.println("Text Width: " + text_width); 							 
							//System.out.println("Text Height: " + text_height); 							 
							int text_area = text_width * text_height;							 
							//System.out.println("Text area: " + text_area);							 
							//System.out.println("Element area: " + element_area);						 
						}						 						
						int difference_h =element_width - text_width;						
						//System.out.println("difference_hvvvv: " + difference_h);						
						int difference_v = element_height - text_height;						
						//System.out.println("difference_vvvvvv: " + difference_v);												
						boolean truncation_h = false;						
						boolean truncation_v = false;											
						if (difference_h < -160 && difference_h >-200 )							
						{							
							truncation_h = true;							
							ValidatorUtil.highlight_Element(driver, objs_temp.get(j));						
							truncation_count_h++;						
							//System.out.println("xxxxxxxxxxxxxxxx"+truncation_count_h+":::"+objs_temp.get(j).getText());												
						}						
						if (difference_v<-6)							
						{							
							truncation_v = true;							
							truncation_count_v++;							
							ValidatorUtil.highlight_Element(driver, objs_temp.get(j));							
							//System.out.println("xxxxxxxxxxxxxxxx"+truncation_count_v);														
						}						 
						//System.out.println("Horizonal truncation: " + truncation_h); 						 
						//System.out.println("Vertical truncation: " + truncation_v); 						 
						//System.out.println("\n");																 
					}					 																				
				}				 
			}						
		}		
		count[0] = truncation_count_h;
		count[1] = truncation_count_v;	
		return count;			
	}

	public void getScrnTruncation (WebDriver driver) throws Exception {
		System.out.println("Start capturing screen for truncation by remote");		
		String name = getStringDateShort().replaceAll(":", "").replaceAll("\\s", "").replaceAll("-", "");		
		WebDriver augmenteDriver = (WebDriver) new Augmenter().augment(driver);
		File screenshot = ((TakesScreenshot)augmenteDriver).getScreenshotAs(OutputType.FILE);		
		
		String path = "./truncation_screenshot//"; 		
		 try {    			 
			 FileUtils.copyFile(screenshot, new File(path+name+".png"));  
	     } catch (Exception e) {  
	         System.out.println("try to capture screens from local ");
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
	 
}
