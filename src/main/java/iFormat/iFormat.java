package iFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import Common.absI18N;



public class iFormat extends absI18N
{	
	//static String formatScreenPath = "./iFormat_screenshot/";
	static int issueNumber = 0;
	
	public void start()
	{
		System.out.println("***************************************************");
		System.out.println("iFormat Validator 1.0 starts");
		System.out.println("***************************************************");
	    ArrayList<String> iformateLog = new ArrayList<String>();
		boolean context = extractContexts(this.driver, iformateLog);
		if( context )
		{
			this.CaptureScreenShot();					
			
		}
		
		System.out.println("going through for iFormat is over");
	    
	}
	
	public static ArrayList<String> getDateFormatList()
	{
		 //ENU date format
        ArrayList<String> FormatList = new ArrayList<String>();
        String dateFormat = "(^|\\s)0?([1-9]|10|11|12)[/-]\\d{1,2}[/-]\\d{2,4}(\\s\\d{1,2}:\\d{1,2})?.*"; //  enu date + time:  05/24/2014 2:40 
		FormatList.add(dateFormat); 		
		dateFormat = "(^|\\s)(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)(\\s\\d{1,2})?(,\\s\\d{2,4})?(\\s\\d{1,2}:\\d{1,2})?.*";  //  enu date   May 24, 2014 2:40  
		FormatList.add(dateFormat);
		dateFormat = "(^|\\s)(January|February|March|April|May|June|July|August|September|October|November|December)(\\s\\d{1,2})?(,\\s\\d{2,4})?(\\s\\d{1,2}:\\d{1,2})?.*";  //  enu date   May 24, 2014 2:40  
		FormatList.add(dateFormat);	
		dateFormat = "(^|\\s)Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday((\\s.*)|$)";  //  only weekname
		FormatList.add(dateFormat);	
		dateFormat = "(^|\\s)(SUN|Mon|Tue|Wed|Thu|Fri|Sat)((\\s.*)|$)";  //  only weekname
		FormatList.add(dateFormat);	
		return FormatList;
	}
	
	//to assert the string is ENU datetime format
	public static boolean assertFormat(String label, ArrayList<String> formatLog){
		
        boolean enuExist = false;
        ArrayList<String> formatList = getDateFormatList();
        
		for (int i=0; i< formatList.size();i++){
			String dateFormat = formatList.get(i).trim();
			Pattern pDate = Pattern.compile(dateFormat);
			Matcher mDate = pDate.matcher(label); 
			if(mDate.find()){
				enuExist = true;
				String strTemp = "[iFormat]:Find an English Datetime formate] in [" + label.trim() + "]\r\n";
				System.out.println(strTemp);
				formatLog.add(strTemp);
				break;
			
			}
		}
        return enuExist;
	}
	
	
	
	/*To check if current element is the leaf element */
	
	static boolean hasChild(WebDriver driver, int loc_id)
	 {
		 boolean hasChild = true;
		//		 System.out.println("//body//*[@loc_id=" + loc_id +"]");
		 String child = "//body//*[@loc_id=" + loc_id +"]/*";
		 List<WebElement> children= driver.findElements(By.xpath(child));
		//			WebElement nodeToParent = driver.findElement(By.xpath("//body//*[@loc_id='" + loc_id + "']"));	

		//		 System.out.println(children.size());
		 if(children.size() == 0)
		 {
			 hasChild = false;
		 }
		 return hasChild;
	 }
	
	/*To extract all elements of current one web page, and assert if have ENU format problem. */

	static boolean extractContexts(WebDriver driver,ArrayList<String> iFormatLog)
	{
		int seq = 0;
		boolean ENUdateformatExist = false;
        List<WebElement> elementsList = driver.findElements(By.cssSelector("body *"));
		//System.out.println("Nodes number in Total: " + elementsList.size());		 
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean visible = false;		
		for(WebElement e: elementsList){
			try{
				/*check if element is visible*/
		        visible = e.isDisplayed();
		        
		        
		        /*filter out by visible and clickable*/
		        if(visible)
				{	                	
					/* detect date time format strings in value*/
		            String value = e.getAttribute("value");
		            String tagName= e.getTagName();
		            if(value != null & (tagName.equals("input") | tagName.equals("textarea") ))
					{
						value = value.trim();
		                boolean iformat = assertFormat(value, iFormatLog);
	                	if(iformat)
						{		
	                		ENUdateformatExist = true;
	                		issueNumber++;
	                		highlightElementY(driver, e);	               
		        		}
		            }
		                		
	        		js.executeScript("arguments[0].setAttribute('loc_id', arguments[1]);",e, seq);
	        				
	        			
					boolean hasChildren = hasChild(driver, seq);
					/*if the node has no child*/
	                if(!hasChildren)
	                {
						String context = e.getText().trim();
	                	System.out.println("context=" + context + "---------------");
	                	boolean enuFormat = assertFormat(context, iFormatLog);
	                	if(enuFormat){
	                		ENUdateformatExist = true;
	                		issueNumber++;
	                		highlightElementY(driver, e);
	                	}
	                }
	            }
              	seq++;
			}
			catch(StaleElementReferenceException e2)
			{
				//e2.printStackTrace();
//				System.out.println("[Warning]: Error occurs when detecting element #" + seq);
//				utility.writeToLOG("[Warning]: Error occurs when detecting element #" + seq, "./error.log");
			}
			catch(WebDriverException e3)
			{
//				System.out.println("[Warning]: Error occurs when detecting element #" + seq);
				
//				utility.writeToLOG("[Warning]: Error occurs when detecting element #" + seq, "./error.log");
			}
              	
		}

		 return ENUdateformatExist;
	 }
	

}