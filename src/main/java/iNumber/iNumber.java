package iNumber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import Common.absI18N;

public class iNumber extends absI18N {
	
	static int issueNumber = 0;
	static String language;
	
	static LinkedHashMap <String, Set<String>> map;
	
	public iNumber (String langauge) {
		
	   iNumber.language= langauge;
	}
	public void start()
	
	{
		System.out.println("***************************************************");
		System.out.println("iNumber Validator 1.0 starts");
		System.out.println("***************************************************");
	    ArrayList<String> iformateLog = new ArrayList<String>();
		boolean context = extractContexts(this.driver, iformateLog);
		if( context )
		{
			this.CaptureScreenShot();					
			
		}
		
		System.out.println("going through for iFormat is over");
	    
	}
	
static boolean hasChild(WebDriver driver, int loc_id)
	 {
		 boolean hasChild = true;
	
		 String child = "//body//*[@loc_id=" + loc_id +"]/*";
		 List<WebElement> children= driver.findElements(By.xpath(child));
	
		 if(children.size() == 0)
		 {
			 hasChild = false;
		 }
		 return hasChild;
	 }
	
	public static ArrayList<String> getNumberFormatList() {
		
		//ENU date format
		//check if the odivalue has a legal format[0,0,0.0] 
		// @return :a boolean value,true or false 
		
		 ArrayList<String> NumberFormatList = new ArrayList<String>();
		 String NumberFormat=""; 
		 
		 NumberFormat ="JPN"+"_"+"[0-9]*" + "," + "[0-9]*" + "," + "[0-9]*" + "." + "[0-9]*";
		 NumberFormatList.add(NumberFormat); //JPN,KRO,CHS,CHT,ENU
		 
		 NumberFormat ="FRA"+"_"+"[0-9]*" + " "+"[0-9]*" + " "+"[0-9]*" + ",";
		 NumberFormatList.add(NumberFormat);//FRA
		 
		 NumberFormat ="GER"+"_"+"[0-9]*\\."+"[0-9]*\\."+"[0-9]*";
		 NumberFormatList.add(NumberFormat); //GER //ITA //ESN //POR
		 
		
		 return  NumberFormatList;
	}
	
	public static LinkedHashMap<String, Set<String>> getNumberformat(){
		
		LinkedHashMap <String, Set<String>> hashMap = new LinkedHashMap<String,Set<String>>();
		List <String> list = getNumberFormatList();
		
		for (String language : list){
			
		   String[] L= language.split("_");
		   
		   Set<String> set=hashMap.get(L[0]);
		 
		   if (set==null){
			   
			   set = new LinkedHashSet<String>();
		   }
		   		set.add(L[1]);
		   
		   
		   	hashMap.put(L[0], set);
		}
		
			return hashMap;
	
	}
		
public static boolean assertFormat(String label,ArrayList<String> formatLog){
	
	 	boolean enuExist = false;
	 	
	 	map = iNumber.getNumberformat();
	 	Set<String> s = map.keySet(); // return the map.keySet() get keys // map.get()// return the key's value
		Iterator<String> iter = s.iterator();
		String str="";
		String CCJK = "CHS"+"CHT"+"JPN"+"KRO";
		String GIEP=  "GER"+"ITA"+"ESN"+"POR";
		boolean turning=false;
		
		if(!CCJK.contains(language)&!GIEP.contains(language)){turning = true;}
	 	
		if (turning) {
 			
	 		while (iter.hasNext()){
	 		 		   
	 	 	    str = (String)iter.next();
	 	 	    	
	 	 	    if (!str.equals(language)){	   
	 	 	    	
	 	 	    	System.out.println(str+ ":" + map.get(str).toString().trim());
	 	 	    	
	 	 	    	Set<String> st = map.get(str);
	 	 	    	Iterator<String> it = st.iterator();
	 	 	    	
	 	 	    	while (it.hasNext()) {
	 	 	    		
	 	 	    		String NumberFormat = (String)it.next();
	 	 	    		Pattern pDate = Pattern.compile(NumberFormat);
		 				Matcher mDate = pDate.matcher(label); 
		 				if(mDate.find()){
		 					enuExist = true;
		 					String strTemp = "[iFormat]:Find an "+ str +" Number formate] in [" + label.trim() + "]\r\n";
		 					System.out.println(strTemp);
		 					formatLog.add(strTemp);
		 					
		 				
		 				}
	 	 	    		
	 	 	    	}
	 	 	    	
	 		 }
	 	 	     
	 	 	    	 }
	 			
	 		}else {
	 			
	 			if (CCJK.contains(language)){
	 				
	 				while (iter.hasNext()){
				 		   
		 	 	 	    str = (String)iter.next();
		 	 	 	    	
		 	 	 	 if(!str.equals("CHS") & !str.equals("CHT") & !str.equals("KRO") & !str.equals("JPN")){	   
			 	 	    	
			 	 	    	System.out.println(str+ ":" + map.get(str).toString().trim());
			 	 	    	
			 	 	    	Set<String> st = map.get(str);
			 	 	    	Iterator<String> it = st.iterator();
			 	 	    	
			 	 	    	while (it.hasNext()) {
			 	 	    		
			 	 	    		String NumberFormat = (String)it.next();
			 	 	    		Pattern pDate = Pattern.compile(NumberFormat);
				 				Matcher mDate = pDate.matcher(label); 
				 				if(mDate.find()){
				 					
				 					enuExist = true;
				 					String strTemp = "[iFormat]:Find an "+ str +" Number formate] in [" + label.trim() + "]\r\n";
				 					System.out.println(strTemp);
				 					formatLog.add(strTemp);
				 					
				 				
				 				}
			 	 	    		
			 	 	    	}
			 	 	    	
			 		 }
		 	 	 	     
		 	 	 }
	 			}else if (GIEP.contains(language)) {
	 				
	 				while (iter.hasNext()){
				 		   
		 	 	 	    str = (String)iter.next();
		 	 	 	    	
		 	 	 	 if(!str.equals("GER") & !str.equals("ITA") & !str.equals("ESN") & !str.equals("PRO")){	   
			 	 	    	
			 	 	    	System.out.println(str+ ":" + map.get(str).toString().trim());
			 	 	    	
			 	 	    	Set<String> st = map.get(str);
			 	 	    	Iterator<String> it = st.iterator();
			 	 	    	
			 	 	    	while (it.hasNext()) {
			 	 	    		
			 	 	    		String NumberFormat = (String)it.next();
			 	 	    		Pattern pDate = Pattern.compile(NumberFormat);
				 				Matcher mDate = pDate.matcher(label); 
				 				
				 				if(mDate.find()){
				 					
				 					enuExist = true;
				 					String strTemp = "[iFormat]:Find an "+ str +" Number formate] in [" + label.trim() + "]\r\n";
				 					System.out.println(strTemp);
				 					formatLog.add(strTemp);
				 					
				 				}
			 	 	    		
			 	 	    	}
			 	 	    	
		 	 	 	  }
		 	 	 	     
	 				}
	 				
	 			}
	 			
	 	}
	 	  					return enuExist;
}
	
	static boolean extractContexts(WebDriver driver,ArrayList<String> iFormatLog)
	{
		int seq = 0;
		boolean ENUdateformatExist = false;
        List<WebElement> elementsList = driver.findElements(By.cssSelector("body *"));//driver.findElements(By.xpath("//td"));// driver.findElements(By.cssSelector("body *"));
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

			}
			catch(WebDriverException e3)
			{
				
			}
              	
		}

		 	return ENUdateformatExist;
	 }
	
}
