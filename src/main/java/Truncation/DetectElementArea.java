package Truncation;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import Truncation.ValidatorUtil;

public class DetectElementArea {
	
	ValidatorUtil vu =new ValidatorUtil();
	
	public boolean detection(WebElement element,LinkedHashSet set_)
	{			
		boolean truncation = false;
		boolean detectTruction = false;
				
		int parent_area = this.get_area(element);
		int area=0;
		
		Iterator itr = set_.iterator();
		int i=0;				
		
		while (itr.hasNext()) {						
			String text = itr.next().toString();
			//System.out.println(text);
			if (element.getText().toString().equals(text)|| text.contains(element.getText().toString()) ){
				i++;
				detectTruction= false;
				//System.out.println("detected in previous page "+element.getText());
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("detected "+element.getText());
			}
		}				
		System.out.println("out: "+i);
		if (i!=0){						
			try			
			{					
				System.out.println("Start checking: "+element.getText());							
				WebElement child_first = element.findElement(By.xpath("./*[1]"));			
				System.out.println("Parent web element area: " + element.getTagName() + ": " + this.get_area(element));								
				List<WebElement> objs_temp = child_first.findElements(By.xpath("./following-sibling::*"));								
				System.out.println(objs_temp.size() + 1 + " child web elements:");				
				area = area + output_valid_area(child_first);
								
				for(WebElement e: objs_temp)				
				{										
					area = area + output_valid_area(e);								
				}								
				System.out.println("In total area: " + area);			
			}			
			catch(NoSuchElementException e)			
			{				
				System.out.println("This element has no child!" );			
			}		
		}				
		
		if(parent_area < (area*0.85)) //area*0.95		
		{						
			try {												
				System.out.println("!!!!!!!!!!!!!!!!!!!!!Truncation detected!" +" elementText: "+element.getText());				
				String pa = Integer.toString(parent_area);											
			}catch (Exception error) {										
			}			 						
			truncation = true;		
		}		
		return truncation;	
	}

	public int get_area(WebElement element)	
	{
		int height = element.getSize().height;
		int width = element.getSize().width;
		return height*width;
	}
	
	public boolean valid(WebElement element)
	{		
		boolean valid = false;
		try{							
			if(!element.getText().trim().equals(""))					
				valid = true;		
		}
		catch(StaleElementReferenceException e)		
		{
			System.out.println("[ERROR]: Element is no longer attached to the DOM!");
		}
		return valid;
	}
	
	public int output_valid_area(WebElement e)
	{
		int area=0;
		if(valid(e))
		{
			System.out.println(e.getTagName()+ ": " + this.get_area(e));		
			area = this.get_area(e);
		}
		else
		{
			System.out.println(e.getTagName() + ": " + "This element is not valid");
		}
		return area;
	}
	
	public void area_for_each(WebDriver driver,LinkedHashSet set_,String ProjectID) throws Exception
	{
		//int seq_number = 1;	
		int truncation_count = 0;	
		//List<WebElement> objs_temp = driver.findElements(By.cssSelector("body *"));	
		List<WebElement> objs_temp = driver.findElements(By.xpath("//body//*"));	
		System.out.println("there are "+objs_temp.size()+" node need to be detected.");
			
		for(WebElement e: objs_temp)	
		{				
			if (!ValidatorUtil.isNumber(e.getText())){   //elementExit(driver,e);						
				if(valid(e))			
				{				
					if (elementExit(driver,e))				
					{					
						if(detection(e,set_)){																	
							truncation_count++;					
							ValidatorUtil.highlight_Element(driver, e);					
							System.out.println("there are "+truncation_count+" truncated place in current page!");										
						}				
					}						
					//System.out.println("\n");			
				}			
				else			
				{				
					try				
					{					
						//System.out.println(e.getTagName() + ": " + "This element is not valid. "+e.getText());				
					}				
					catch(StaleElementReferenceException e2)				
					{					
				
					}			
				}			
				//seq_number++;			
			}			
		}		
		if (truncation_count>0) {			
			vu.getScrnTruncation(ProjectID,driver);			
			System.out.println("there are"+" "+truncation_count+" "+"truncated strings in current page");			
			System.out.println("Detetion of the truncation is completed,please refer to truncation folder for screens");		
		}	
		else {	
			System.out.println("There are no truncated string in this page.");	
		}	
	}
	
	public boolean elementExit(WebDriver driver,WebElement element){
		boolean ele = false ;			
		try {				
			boolean visible = element.isDisplayed();				
			if (visible && ExpectedConditions.elementToBeClickable(By.xpath(element.toString()))!=null) {						
				if (elementElement(driver,element.getText())){								
					ele = true;		
				}else {						
					//System.out.println("Detected from elementExit: Element is invalid element");				
					ele = false ;			
				}		
			}	
		}catch (Exception error ){						
			System.out.println("Element not found in the cache");	
		}			
		return ele;		
	}

	public boolean elementElement(WebDriver driver,String str){
		String xpathtemp = "//body//*[text()='%s']"; 	
		String xpath ="temp"; 	
		xpath = String.format(xpathtemp ,str);	
		boolean existElement = false ; 	  
		try {	   
			driver.findElement(By.xpath(xpath));	   
			existElement=true;  
		}catch (Exception error){   
			//System.out.println("Element is not existed in current page");	   
			existElement=false;  
		}       
		return existElement;
	}

	public WebElement getElement (WebDriver driver,String text) {
		String xpathtemp = "//body//*[text()='%s']"; 	 	
		String xpath ="temp";	
		xpath = String.format(xpathtemp ,text);	
		WebElement Element=driver.findElement(By.xpath(xpath));	 	
		return  Element;
	}

}
